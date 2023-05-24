package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;
import com.agaram.eln.primary.model.sheetManipulation.Lssheetworkflowhistory;

public interface LssheetworkflowhistoryRepository  extends JpaRepository<Lssheetworkflowhistory, Integer>{
	@Transactional
	@Modifying
	@Query("update Lssheetworkflowhistory o set o.currentworkflow = null where o.currentworkflow = ?1")
	void setWorkflownullforHistory(LSsheetworkflow lssheetworkflow);
	
	public List<Lssheetworkflowhistory> findByFilecode(Integer filecode);
}
