package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.Elnprotocolworkflowgroupmap;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflowgroupmapping;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;

public interface ElnprotocolworkflowgroupmapRepository extends JpaRepository<Elnprotocolworkflowgroupmap, Integer>{

	List<Elnprotocolworkflowgroupmap> findBylsusergroup(LSusergroup lsusergroup);

	List<Elnprotocolworkflowgroupmap> findBylsusergroupAndWorkflowcodeNotNull(LSusergroup userGroup);

}
