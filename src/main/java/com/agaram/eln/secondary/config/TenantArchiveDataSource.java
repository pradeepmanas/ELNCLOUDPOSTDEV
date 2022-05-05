package com.agaram.eln.secondary.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.agaram.eln.secondary.repository.multitenant.DataSourceConfigRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.agaram.eln.primary.config.TenantDataSource;
import com.agaram.eln.secondary.model.multitenant.DataSourceConfig;


@Component
public class TenantArchiveDataSource implements Serializable {

    private HashMap<String, DataSource> dataSources = new HashMap<>();

    @Autowired
    private TenantDataSource tenantDataSource;

    public DataSource getDataSource(String name) {
        if (dataSources.get(name) != null) {
            return dataSources.get(name);
        }
        DataSource dataSource = createDataSource(name);
        if (dataSource != null) {
            dataSources.put(name, dataSource);
        }
        
        return dataSource;
    }

    @PostConstruct
    public Map<String, DataSource> getAll() {

        Map<String, DataSource> result = new HashMap<>();
        result = tenantDataSource.archivedataSources;
        for (String name : result.keySet()) {
            Flyway flyway = Flyway.configure().dataSource(result.get(name)).locations("classpath:db/migration_archive").load();
            flyway.repair();
            flyway.migrate();
        }
    	
        return result;
    }

    private DataSource createDataSource(String name) {
        DataSourceConfig config = new DataSourceConfig();
        		//configRepo.findByArchivename(name);
        if (config != null) {
//            DataSourceBuilder factory = DataSourceBuilder
//                    .create().driverClassName(config.getDriverClassName())
//                    .username(config.getUsername())
//                    .password(config.getPassword())
//                    .url(config.getArchiveurl());
//            DataSource ds = factory.build();     
//            return ds;
        	 HikariConfig configuration = new HikariConfig();
             configuration.setDriverClassName(config.getDriverClassName());
             configuration.setJdbcUrl(config.getArchiveurl());
             configuration.setUsername(config.getUsername());
             configuration.setPassword(config.getPassword());
             configuration.setMaximumPoolSize(1);
             
             HikariDataSource dataSource = new HikariDataSource(configuration);
             return dataSource;
        }
        return null;
    }   
    
    public boolean addDataSource(DataSource dataSource, String name)
    {
    	if (dataSource != null) {
            dataSources.put(name, dataSource);
        }
    	return true;
    }

}