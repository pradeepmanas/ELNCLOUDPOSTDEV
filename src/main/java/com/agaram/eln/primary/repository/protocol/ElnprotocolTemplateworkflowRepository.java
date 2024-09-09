package com.agaram.eln.primary.repository.protocol;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.model.protocols.ElnprotocolTemplateworkflow;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

public interface ElnprotocolTemplateworkflowRepository extends JpaRepository<ElnprotocolTemplateworkflow, Integer> {

	List<ElnprotocolTemplateworkflow> findBylssitemasterAndStatusOrderByWorkflowcodeAsc(LSSiteMaster lssitemaster,
			int i);

	@Query(value = "SELECT MAX(workflowcode) FROM ElnprotocolTemplateworkflow")
	int getlargeworkflowecode();
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO ElnprotocolTemplateworkflow (workflowcode, status, workflowname, lssitemaster_sitecode) " +
	               "VALUES (:workflowcode, :status, :workflowname, :sitecode)", nativeQuery = true)
	void customInsertElnprotocolTemplateworkflow(
	        @Param("workflowcode") Integer workflowcode,
	        @Param("status") Integer status,
	        @Param("workflowname") String workflowname,
	        @Param("sitecode") Integer sitecode
	);

	ElnprotocolTemplateworkflow findTopByAndLssitemasterOrderByWorkflowcodeDesc(LSSiteMaster lssitemaster);

	ElnprotocolTemplateworkflow findTopByAndLssitemasterAndStatusOrderByWorkflowcodeAsc(LSSiteMaster lssitemaster, int i);

	ElnprotocolTemplateworkflow findTopByAndLssitemasterAndStatusOrderByWorkflowcodeDesc(LSSiteMaster lssitemaster, int i);


}
