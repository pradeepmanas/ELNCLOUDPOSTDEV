package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.instrumentDetails.Lsorderworkflowhistory;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;

public interface LsorderworkflowhistoryRepositroy  extends JpaRepository<Lsorderworkflowhistory, String> {

	@Transactional
	@Modifying
	@Query("update Lsorderworkflowhistory o set o.currentworkflow = null where o.currentworkflow = ?1")
	void setWorkflownullforHistory(LSworkflow lsworkflow);
	
	List<Lsorderworkflowhistory> findByBatchcodeOrderByHistorycode(long batchcode);
}
