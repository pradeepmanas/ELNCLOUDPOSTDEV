package com.agaram.eln.secondary.repository.multitenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.multitenant.DataSourceConfig;

@Repository
public interface DataSourceConfigRepository extends JpaRepository<DataSourceConfig, Long> {
    DataSourceConfig findByName(String name);
    DataSourceConfig findByTenantid(String tenantid);
    DataSourceConfig findByNameAndTenantid(String name, String tenantid);
}
