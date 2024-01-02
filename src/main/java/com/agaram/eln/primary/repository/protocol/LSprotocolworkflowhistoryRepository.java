package com.agaram.eln.primary.repository.protocol;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.protocols.ElnprotocolTemplateworkflow;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflowhistory;


public interface LSprotocolworkflowhistoryRepository extends JpaRepository<LSprotocolworkflowhistory, Integer>{

	List<LSprotocolworkflowhistory> findByProtocolmastercode(Integer protocolmastercode);

	List<LSprotocolworkflowhistory> findByProtocolmastercodeAndCreatedateBetween(Integer protocolmastercode,
			Date fromdate, Date todate);
	@Transactional
	@Modifying
	@Query(value="update LSprotocolworkflowhistory  set elnprotocoltemplateworkflow_workflowcode = null where elnprotocoltemplateworkflow_workflowcode = ?1",nativeQuery = true)
	void setWorkflownullforHistory(int i);

}
