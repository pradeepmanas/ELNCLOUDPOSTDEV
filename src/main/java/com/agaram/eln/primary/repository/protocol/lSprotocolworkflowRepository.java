package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolworkflow;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflowgroupmap;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

public interface lSprotocolworkflowRepository extends JpaRepository<LSprotocolworkflow, Integer>{
	
	public List<LSprotocolworkflow> findBylssitemaster(LSSiteMaster LSSiteMaster);
	public LSprotocolworkflow findByworkflowcode(Integer workflowcode);
	public LSprotocolworkflow findTopByAndLssitemasterOrderByWorkflowcodeAsc(LSSiteMaster lssitemaster);

	public List<LSprotocolworkflow> findByLsprotocolworkflowgroupmapInOrderByWorkflowcodeDesc(
			List<LSprotocolworkflowgroupmap> lsworkflowgroupmapping);
	
	
}
