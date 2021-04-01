package com.agaram.eln.primary.repository.protocol;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolworkflowgroupmap;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;

public interface LSprotocolworkflowgroupmapRepository extends JpaRepository<LSprotocolworkflowgroupmap, Integer>{
	
	public LSprotocolworkflowgroupmap findBylsusergroup(LSusergroup LSusergroup);
	
}
