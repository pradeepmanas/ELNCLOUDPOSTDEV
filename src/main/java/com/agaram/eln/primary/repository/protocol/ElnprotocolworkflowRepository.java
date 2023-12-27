package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.Elnprotocolworkflow;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

public interface ElnprotocolworkflowRepository extends JpaRepository<Elnprotocolworkflow, Integer>{

	List<Elnprotocolworkflow> findByLssitemasterOrderByWorkflowcodeAsc(LSSiteMaster lssitemaster);

}
