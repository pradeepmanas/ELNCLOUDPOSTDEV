package com.agaram.eln.primary.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Primary
@Component
public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
    private static final String DEFAULT_TENANT_ID = "test";

	@Autowired
    private DataSource defaultDS;

    @Autowired
    private ApplicationContext context;
    
    @Autowired
	private Environment env;

    private Map<String, DataSource> map = new HashMap<>();

    boolean init = false;
    
    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void load() {
        map.put(DEFAULT_TENANT_ID, defaultDS);
    }

    @Override
    protected DataSource selectAnyDataSource() {
    	dataSource= map.get(DEFAULT_TENANT_ID);
        return map.get(DEFAULT_TENANT_ID);
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        if (!init) {
            init = true;
            TenantDataSource tenantDataSource = context.getBean(TenantDataSource.class);
            map.putAll(tenantDataSource.getAll());
        }
        dataSource= map.get(tenantIdentifier) != null ? map.get(tenantIdentifier) : map.get(DEFAULT_TENANT_ID);
        return dataSource;
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
    
    @Override
    public Connection getAnyConnection() throws SQLException {
      return dataSource.getConnection();
    }
    
    
    
}
