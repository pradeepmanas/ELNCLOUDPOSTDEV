package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.sheetManipulation.LSfileversion;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;

public interface LSfileversionRepository   extends JpaRepository<LSfileversion, Integer>{
	public LSfileversion findFirstByFilecodeOrderByVersionnoDesc(Integer filecode);
	public List<LSfileversion> findByFilecodeOrderByVersionnoDesc(Integer filecode);
	public LSfileversion findByFilecodeAndVersionnoOrderByVersionnoDesc(Integer filecode, Integer versionnumber);
	
	public long countByFilecode(Integer filecode);
	
	@Transactional
	@Modifying
	@Query("update LSfileversion o set o.lssheetworkflow = null where o.lssheetworkflow = ?1")
	void setWorkflownullforHistory(LSsheetworkflow lssheetworkflow);
}
