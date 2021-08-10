package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolworkflowgroupmap;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;

public interface LSprotocolworkflowgroupmapRepository extends JpaRepository<LSprotocolworkflowgroupmap, Integer>{
	
	
	public List<LSprotocolworkflowgroupmap>  findBylsusergroup(LSusergroup LSusergroup);

	public List<LSprotocolworkflowgroupmap> findBylsusergroupAndWorkflowcodeNotNull(LSusergroup lsusergroup);

	public List<LSprotocolworkflowgroupmap> findBylsusergroupAndWorkflowcodeNotNull(int lsusergroup);
	
}
