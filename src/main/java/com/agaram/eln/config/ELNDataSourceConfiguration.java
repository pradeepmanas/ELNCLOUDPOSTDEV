//package com.agaram.eln.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.env.Environment;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.datasource.init.DataSourceInitializer;
//import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import com.zaxxer.hikari.HikariDataSource;
//
//import java.util.Properties;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(basePackages = "com.agaram.eln.repository",
//        entityManagerFactoryRef = "entityManagerFactory",
//        transactionManagerRef= "transactionManager"
//)
//public class ELNDataSourceConfiguration {
//	
//	@Autowired
//    private Environment env;
//	
//	@Bean
//	@Primary
//	@ConfigurationProperties("app.datasource.eln")
//	public DataSourceProperties elnDataSourceProperties() {
//	    return new DataSourceProperties();
//	}
//	
//	@Bean
//	@Primary
//	@ConfigurationProperties("app.datasource.eln.configuration")
//	public DataSource elnDataSource() {
//	    return elnDataSourceProperties().initializeDataSourceBuilder()
//	            .type(HikariDataSource.class).build();
//	}
//	
//	@Primary
//    @Bean(name = "entityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
//		
//		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
//        factory.setDataSource(elnDataSource());
//        factory.setPackagesToScan(new String[]{"com.agaram.eln.model.cfr"
//        		, "com.agaram.eln.model.configuration"
//        		, "com.agaram.eln.model.general"
//        		, "com.agaram.eln.model.instrumentDetails"
//        		, "com.agaram.eln.model.inventory"
//        		, "com.agaram.eln.model.report"
//        		, "com.agaram.eln.model.sheetManipulation"
//        		, "com.agaram.eln.model.usermanagement"
//				, "com.agaram.eln.model.protocols"
//				, "com.agaram.eln.model.templates"});
//        
//        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//     
//        Properties jpaProperties = new Properties();
//        if(env.getProperty("spring.jpa.hibernate.ddl-auto") != null)
//        {
//        	jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
//        }
//        else
//        {
//        	jpaProperties.put("hibernate.hbm2ddl.auto", "update");
//        }
//        
//        jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
//        jpaProperties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
//        jpaProperties.put("hibernate.connection.useUnicode", true);
//        jpaProperties.put("hibernate.connection.characterEncoding", "UTF-8");
//        
//        factory.setJpaProperties(jpaProperties);
//     
//        return factory;
//        
////        return builder
////                .dataSource(elnDataSource())
////                .packages("com.agaram.eln.model.cfr"
////                		, "com.agaram.eln.model.configuration"
////                		, "com.agaram.eln.model.general"
////                		, "com.agaram.eln.model.instrumentDetails"
////                		, "com.agaram.eln.model.inventory"
////                		, "com.agaram.eln.model.report"
////                		, "com.agaram.eln.model.sheetManipulation"
////                		, "com.agaram.eln.model.usermanagement")
////                .build();
//    }
//
//    @Primary
//    @Bean(name="transactionManager")
//    public PlatformTransactionManager elnTransactionManager(
//            final @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
//        return new JpaTransactionManager(entityManagerFactory.getObject());
//    }
//    
//    @Bean
//    public DataSourceInitializer securityDataSourceInitializer() 
//    {
//        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
//        dataSourceInitializer.setDataSource(elnDataSource());
//        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
//        boolean enableinitialize = env.getProperty("app.datasource.eln.initialize", Boolean.class, false);
//        if(enableinitialize)
//        {
//	        databasePopulator.addScript(new ClassPathResource("import_usermaster_ls.sql"));
//	        databasePopulator.addScript(new ClassPathResource("import_lsfields_ls.sql"));
//	        databasePopulator.addScript(new ClassPathResource("import_usergrouprights_ls.sql"));
//	        databasePopulator.addScript(new ClassPathResource("import_usergrouprightsmaster_ls.sql"));
//	        databasePopulator.addScript(new ClassPathResource("import_auditconfig_ls.sql"));
//        }
//
//        if(env.getProperty("spring.jpa.hibernate.ddl-auto") != null)
//        {
//        	if(env.getProperty("spring.jpa.hibernate.ddl-auto")=="create"
//        		&& enableinitialize)
//	        {
//        		databasePopulator.addScript(new ClassPathResource("import_patchs_ls.sql"));
//	        }
//        }
//        else 
//        {
//        	databasePopulator.addScript(new ClassPathResource("import_patchs_ls.sql"));
//        }
//        
//        dataSourceInitializer.setDatabasePopulator(databasePopulator);
//        dataSourceInitializer.setEnabled(true);
//        return dataSourceInitializer;
//    }  
//   
//
//}
