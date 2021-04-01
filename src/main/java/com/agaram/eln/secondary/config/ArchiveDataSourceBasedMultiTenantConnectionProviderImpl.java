package com.agaram.eln.secondary.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ArchiveDataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
    private static final String DEFAULT_TENANT_ID = "Archive";

	@Autowired
    private DataSource defaultDS;

    @Autowired
    private ApplicationContext context;
   
    private Map<String, DataSource> map = new HashMap<>();

    public static boolean init = false;

    @PostConstruct
    public void load() {
        map.put(DEFAULT_TENANT_ID, defaultDS);
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return map.get(DEFAULT_TENANT_ID);
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        if (!init) {
            init = true;
            TenantArchiveDataSource tenantArchiveDataSource = context.getBean(TenantArchiveDataSource.class);
            map.putAll(tenantArchiveDataSource.getAll());
        }
        return map.get(tenantIdentifier) != null ? map.get(tenantIdentifier) : map.get(DEFAULT_TENANT_ID);
    }
    
    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
      connection.close();
    }
    
    public boolean addDataSource(DataSource dataSource, String name)
    {
    	if (dataSource != null) {
    		map.put(name, dataSource);
        }
    	return true;
    }
    
}
 
