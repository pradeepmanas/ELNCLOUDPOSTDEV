package com.agaram.eln.primary.repository.configuration;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.configuration.LSConfiguration;

public interface LSconfigurationRepository extends JpaRepository<LSConfiguration, Integer>{
	
	List<LSConfiguration> findAllByStatus(Integer status);
	
	LSConfiguration findBySerialnoAndStatus(Integer serialno, Integer status);
	
	LSConfiguration findByConfiggrouptypeAndStatus(String Configgrouptype, Integer status);
	
	LSConfiguration findByConfiggrouptypeAndConfigactiveAndStatus(String Configgrouptype, Integer isactive, Integer status);
	
	List<LSConfiguration> findAllByConfiggrouptypeAndStatus(String Configgrouptype, Integer status);
	
	@SuppressWarnings("unchecked")
	LSConfiguration save(LSConfiguration obj);

}
