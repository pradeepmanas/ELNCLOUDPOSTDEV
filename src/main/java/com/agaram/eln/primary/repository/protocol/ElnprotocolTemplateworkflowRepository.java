package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.ElnprotocolTemplateworkflow;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

public interface ElnprotocolTemplateworkflowRepository extends JpaRepository<ElnprotocolTemplateworkflow, Integer>{

	List<ElnprotocolTemplateworkflow> findBylssitemasterAndStatusOrderByWorkflowcodeAsc(LSSiteMaster lssitemaster,
			int i);

}
