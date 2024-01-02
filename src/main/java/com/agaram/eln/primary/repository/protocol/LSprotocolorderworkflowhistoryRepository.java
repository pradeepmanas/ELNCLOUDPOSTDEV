package com.agaram.eln.primary.repository.protocol;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.protocols.Elnprotocolworkflow;
import com.agaram.eln.primary.model.protocols.LSprotocolorderworkflowhistory;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;

public interface LSprotocolorderworkflowhistoryRepository extends JpaRepository<LSprotocolorderworkflowhistory, Integer>{

	List<LSprotocolorderworkflowhistory> findByProtocolordercode(Integer protocolordercode);

	List<LSprotocolorderworkflowhistory> findByProtocolordercodeAndCreatedateBetween(Integer protocolordercode,
			Date fromdate, Date todate);
	@Transactional
	@Modifying
	@Query(value="update LSprotocolorderworkflowhistory  set elnprotocolworkflow_workflowcode = null where elnprotocolworkflow_workflowcode = ?1",nativeQuery = true)
	void setWorkflownullforHistory(Elnprotocolworkflow lsworkflow);
}
