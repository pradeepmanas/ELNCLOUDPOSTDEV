package com.agaram.eln.secondary.config;


import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.hibernate.cfg.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.agaram.eln.secondary.repository.*",
      entityManagerFactoryRef = "archiveEntityManagerFactory",
      transactionManagerRef= "archiveTransactionManager"
)
@ComponentScan(basePackages = {"com.agaram.eln.secondary"})
@EntityScan({"com.agaram.eln.secondary"})
public class ArchieveDataSourceConfiguration {
	@Autowired
    private org.springframework.core.env.Environment env;
	
	@Autowired
    private JpaProperties jpaProperties;
	
	@Autowired
    private TenantArchiveSchemaResolver tenantArchiveSchemaResolver;
	
	@Autowired
    private ArchiveDataSourceBasedMultiTenantConnectionProviderImpl archiveDataSourceBasedMultiTenantConnectionProviderImpl;

    @Bean
    JpaVendorAdapter jpaVendorAdapter() {
    	 HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    	    vendorAdapter.setGenerateDdl(true);
        return vendorAdapter;
    }
    
//	@Autowired
//    private Environment env;
	
	@Bean
	@ConfigurationProperties("app.datasource.archive")
	public DataSourceProperties archieveDataSourceProperties() {
	    return new DataSourceProperties();
	}
	@Bean(destroyMethod = "")
	@ConfigurationProperties("app.datasource.archive.hikari")
	public DataSource archiveDataSource() {
	    return archieveDataSourceProperties().initializeDataSourceBuilder()
	            .type(BasicDataSource.class).build();
	}
	
	@Bean(name = "archiveEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean archiveEntityManagerFactory(
			 DataSource dataSource,
	            MultiTenantConnectionProvider multiTenantConnectionProviderImpl,
	            CurrentTenantIdentifierResolver currentTenantIdentifierResolverImpl
	    ) {
		
		 Map<String, Object> jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());
	        jpaPropertiesMap.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
	        jpaPropertiesMap.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, archiveDataSourceBasedMultiTenantConnectionProviderImpl);
	        jpaPropertiesMap.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantArchiveSchemaResolver);
	        jpaPropertiesMap.put(Environment.FORMAT_SQL, true);
	        jpaPropertiesMap.put(Environment.SHOW_SQL, true);

	        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
	        em.setDataSource(archiveDataSource());
	        em.setPackagesToScan(new String[]{"com.agaram.eln.secondary.model.*"});
	        em.setJpaVendorAdapter(this.jpaVendorAdapter());
	        
	        if(env.getProperty("spring.jpa.hibernate.ddl-auto") != null && !env.getProperty("spring.jpa.hibernate.ddl-auto").equals("none"))
	        {
		        Properties jpaProperties = new Properties();
		        jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
		        jpaProperties.put("hibernate.connection.useUnicode", true);
		        jpaProperties.put("hibernate.connection.characterEncoding", "UTF-8");
		        em.setJpaProperties(jpaProperties);
	        }
	        else
	        {
	        	em.setJpaPropertyMap(jpaPropertiesMap);
	        }
	        return em;
//		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
//        factory.setDataSource(archiveDataSource());
//        factory.setPackagesToScan(new String[]{"com.agaram.eln.model.archive"
//        		, "com.agaram.eln.controller.archive"
//        		, "com.agaram.eln.service.archive"});
//        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//     
        
     
//        return factory;
	}

	@Bean(name="archiveTransactionManager")
	public PlatformTransactionManager archiveTransactionManager(
	            final @Qualifier("archiveEntityManagerFactory") LocalContainerEntityManagerFactoryBean archiveEntityManagerFactory) {
	        return new JpaTransactionManager(archiveEntityManagerFactory.getObject());
	}
	
}
