package com.agaram.eln.primary.config;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.support.nativejdbc.SimpleNativeJdbcExtractor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.agaram.eln.primary.repository.*",
      entityManagerFactoryRef = "entityManagerFactory",
      transactionManagerRef= "transactionManager"
)
@ComponentScan(basePackages = {"com.agaram.eln.primary"})
@EntityScan({"com.agaram.eln.primary"})
public class HibernateConfig {
	@Autowired
    private org.springframework.core.env.Environment env;
	
	@Autowired
    private JpaProperties jpaProperties;
	
	@Autowired
    private TenantSchemaResolver tenantSchemaResolver;
	
	@Autowired
    private DataSourceBasedMultiTenantConnectionProviderImpl dataSourceBasedMultiTenantConnectionProviderImpl;

    @Bean
    JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }
    
    @Bean
	@Primary
	@ConfigurationProperties("app.datasource.eln")
	public DataSourceProperties elnDataSourceProperties() {
	    return new DataSourceProperties();
	}
    
    @Bean
	@Primary
	@ConfigurationProperties("app.datasource.eln.configuration")
	public DataSource elnDataSource() {
	    return elnDataSourceProperties().initializeDataSourceBuilder()
	            .type(HikariDataSource.class).build();
	}

    @Primary
    @Bean(name = "entityManagerFactory")
    LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            MultiTenantConnectionProvider multiTenantConnectionProviderImpl,
            CurrentTenantIdentifierResolver currentTenantIdentifierResolverImpl
    ) {

    	 Map<String, Object> jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());
        jpaPropertiesMap.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        jpaPropertiesMap.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, dataSourceBasedMultiTenantConnectionProviderImpl);
        jpaPropertiesMap.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantSchemaResolver);
        jpaPropertiesMap.put(Environment.FORMAT_SQL, true);
        jpaPropertiesMap.put(Environment.SHOW_SQL, true);
        
     
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(elnDataSource());
        em.setPackagesToScan(new String[]{"com.agaram.eln.primary.model.*"});
        em.setJpaVendorAdapter(this.jpaVendorAdapter());
        
        
        if(env.getProperty("spring.jpa.hibernate.ddl-auto") != null && !env.getProperty("spring.jpa.hibernate.ddl-auto").equals("none"))
        {
	        Properties jpaProperties = new Properties();
	        if(env.getProperty("spring.jpa.hibernate.ddl-auto") != null)
	        {
	      	  jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
	        }
	        else
	        {
	      	  jpaProperties.put("hibernate.hbm2ddl.auto", "update");
	        }
	      
	        jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
	        jpaProperties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
	        jpaProperties.put("hibernate.connection.useUnicode", true);
	        jpaProperties.put("hibernate.connection.characterEncoding", "UTF-8");
	      
	        em.setJpaProperties(jpaProperties);
        }
        else
        {
        	em.setJpaPropertyMap(jpaPropertiesMap);
        }
        
        return em;
    }
    
    @Primary
  @Bean(name="transactionManager")
  public PlatformTransactionManager elnTransactionManager(
          final @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
      return new JpaTransactionManager(entityManagerFactory.getObject());
  }
    
    @Bean
    public DataSourceInitializer securityDataSourceInitializer() 
    {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(elnDataSource());
        if(env.getProperty("spring.jpa.hibernate.ddl-auto") != null && !env.getProperty("spring.jpa.hibernate.ddl-auto").equals("none"))
        {
	        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
	        boolean enableinitialize = env.getProperty("app.datasource.eln.initialize", Boolean.class, false);
	        if(enableinitialize)
	        {
		        databasePopulator.addScript(new ClassPathResource("import_usermaster_ls.sql"));
	        }
	
	        if(env.getProperty("spring.jpa.hibernate.ddl-auto") != null)
	        {
	        	if(env.getProperty("spring.jpa.hibernate.ddl-auto")=="create"
	        		&& enableinitialize)
		        {
	        		databasePopulator.addScript(new ClassPathResource("import_patchs_ls.sql"));
		        }
	        	else 
		        {
		        	databasePopulator.addScript(new ClassPathResource("import_patchs_ls.sql"));
		        }
	        }
	        else 
	        {
	        	databasePopulator.addScript(new ClassPathResource("import_patchs_ls.sql"));
	        }
	        
	        dataSourceInitializer.setDatabasePopulator(databasePopulator);
	        dataSourceInitializer.setEnabled(true);
        }
        else
        {
        	dataSourceInitializer.setEnabled(false);
        }
        return dataSourceInitializer;
    }  
}
