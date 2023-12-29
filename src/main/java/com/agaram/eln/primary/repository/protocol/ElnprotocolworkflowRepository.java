package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.Elnprotocolworkflow;
import com.agaram.eln.primary.model.protocols.Elnprotocolworkflowgroupmap;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

public interface ElnprotocolworkflowRepository extends JpaRepository<Elnprotocolworkflow, Integer>{

	List<Elnprotocolworkflow> findByLssitemasterOrderByWorkflowcodeAsc(LSSiteMaster lssitemaster);

	List<Elnprotocolworkflow> findByelnprotocolworkflowgroupmapIn(List<Elnprotocolworkflowgroupmap> lsworkflowgroupmapping);

	List<Elnprotocolworkflow> findByelnprotocolworkflowgroupmapInOrderByWorkflowcodeDesc(
			List<Elnprotocolworkflowgroupmap> elnprotocolworkflowgroupmapobj);

	Elnprotocolworkflow findTopByAndLssitemasterOrderByWorkflowcodeAsc(LSSiteMaster site);

}
