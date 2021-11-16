package com.agaram.eln.primary.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.agaram.eln.primary.fetchtenantsource.Datasourcemaster;
import com.agaram.eln.primary.model.multitenant.DataSourceConfig;
import com.agaram.eln.primary.repository.multitenant.DataSourceConfigRepository;

@Primary
@Component
public class TenantDataSource implements Serializable {
	
    private HashMap<String, DataSource> dataSources = new HashMap<>();
    
    public HashMap<String, DataSource> archivedataSources = new HashMap<>();
    
    Flyway flyway;

    @Autowired
    private DataSourceConfigRepository configRepo;
    
    @SuppressWarnings("unused")
	@Autowired
	private Environment env;

    public DataSource getDataSource(String name, String arcivedb) {
        if (dataSources.get(name) != null) {
            return dataSources.get(name);
        }
        
        if(archivedataSources.get(arcivedb) == null)
        {
        	DataSource archievedataSource = createarchiveDataSource(arcivedb);
            if (archievedataSource != null) {
            	
//            	Flyway flyway = Flyway.configure().dataSource(archievedataSource).locations("filesystem:./src/main/resources/db/migration_archive").load();
//                flyway.repair();
//                flyway.migrate();
            
            	archivedataSources.put(arcivedb, archievedataSource);
            }
        }
        
        DataSource dataSource = createDataSource(name);
        if (dataSource != null) {
            dataSources.put(name, dataSource);
        }
        
        return dataSource;
    }

	@PostConstruct
    public Map<String, DataSource> getAll() {
//		agaramtech.onmicrosoft.com
		//List<Datasourcemaster> configList = configRepo.findBytenantid("agaramtech.onmicrosoft.com");
        List<Datasourcemaster> configList = configRepo.findByinitialize(true);
//        logger.info("Get all datasource");
        Map<String, DataSource> result = new HashMap<>();
        try {
	        for (Datasourcemaster config : configList) {
	            DataSource dataSource = getDataSource(config.getName(), config.getArchivename());
	            result.put(config.getName(), dataSource); 
	            
	            Flyway flyway = Flyway.configure().dataSource(dataSource).load();
	            flyway.repair();
	            flyway.migrate();
	       
	
	        }
        }
        catch(Exception e) {
//        	logger.error(e.getLocalizedMessage());
        }
        return result;
    }

    private DataSource createDataSource(String name) {
        DataSourceConfig config = configRepo.findByName(name);
        if (config != null) {
            DataSourceBuilder factory = DataSourceBuilder
                    .create().driverClassName(config.getDriverClassName())
                    .username(config.getUsername())
                    .password(config.getPassword())
                    .url(config.getUrl());
            DataSource ds = factory.build();     
            return ds;
        }
        return null;
    }   
    
    private DataSource createarchiveDataSource(String name) {
        DataSourceConfig config = configRepo.findByArchivename(name);
        if (config != null) {
            DataSourceBuilder factory = DataSourceBuilder
                    .create().driverClassName(config.getDriverClassName())
                    .username(config.getUsername())
                    .password(config.getPassword())
                    .url(config.getArchiveurl());
            DataSource ds = factory.build();     
            return ds;
        }
        return null;
    }   
    
    public boolean addDataSource(DataSource dataSource, String name)
    {
    	if (dataSource != null) {
            dataSources.put(name, dataSource);
            
            Flyway flyway = Flyway.configure().dataSource(dataSource).load();
            flyway.repair();
            flyway.migrate();
           
        }
    	return true;
    }
    
    public boolean addarchiveDataSource(DataSource dataSource, String name)
    {
    	if (dataSource != null) {
    		archivedataSources.put(name, dataSource);
            
            Flyway flyway = Flyway.configure().dataSource(dataSource).locations("classpath:db/migration_archive").load();
            flyway.repair();
            flyway.migrate();
            
        }
    	return true;
    }

}