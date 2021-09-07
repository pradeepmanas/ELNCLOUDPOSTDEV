package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agaram.eln.primary.model.sheetManipulation.LSworkflowgroupmapping;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;

public interface LSworkflowgroupmappingRepository  extends JpaRepository<LSworkflowgroupmapping, Integer>{

	public List<LSworkflowgroupmapping> findBylsusergroup(LSusergroup lsusergroup);
	
}
