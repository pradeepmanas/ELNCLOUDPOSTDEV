package com.agaram.eln.primary.repository.cfr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.cfr.LSaudittrailconfiguration;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public interface LSaudittrailconfigurationRepository extends JpaRepository<LSaudittrailconfiguration, Integer> {
	public List<LSaudittrailconfiguration> findByLsusermasterOrderByOrdersequnce(LSuserMaster lsusermaster);
	
	public List<LSaudittrailconfiguration> findByLssitemasterOrderByOrdersequnce(LSSiteMaster lssitemaster);

	

	
}
