package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflowgroupmap;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

public interface LSsheetworkflowRepository  extends JpaRepository<LSsheetworkflow, Integer>{

	public LSsheetworkflow findTopByOrderByWorkflowcodeDesc();
	public LSsheetworkflow findTopByOrderByWorkflowcodeAsc();
	public LSsheetworkflow findTopByAndLssitemasterOrderByWorkflowcodeDesc(LSSiteMaster lssitemaster);
	public LSsheetworkflow findTopByAndLssitemasterOrderByWorkflowcodeAsc(LSSiteMaster lssitemaster);
	public List<LSsheetworkflow> findByLssheetworkflowgroupmapIn(List<LSsheetworkflowgroupmap> lssheetworkflowgroupmap);
	public List<LSsheetworkflow> findBylssitemaster(LSSiteMaster LSSiteMaster);
	public LSsheetworkflow findByLssitemaster_sitecode(Integer sitecode);
   
	//public LSsheetworkflow findByLssitemaster_sitecode(Integer sitecode);
	public LSsheetworkflow findTopByAndLssitemaster_sitecodeOrderByWorkflowcodeAsc(Integer sitecode);
	
}
