package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sheetManipulation.LSworkflowgroupmapping;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;

public interface LSworkflowgroupmappingRepository  extends JpaRepository<LSworkflowgroupmapping, Integer>{

	public List<LSworkflowgroupmapping> findBylsusergroup(LSusergroup lsusergroup);

	public List<LSworkflowgroupmapping> findBylsusergroupAndWorkflowcodeNotNull(LSusergroup userGroup);
	
}
