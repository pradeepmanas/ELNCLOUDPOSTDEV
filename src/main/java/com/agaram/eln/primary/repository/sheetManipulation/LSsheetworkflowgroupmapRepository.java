package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflowgroupmap;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;

public interface LSsheetworkflowgroupmapRepository extends JpaRepository<LSsheetworkflowgroupmap, Integer>{
	public List<LSsheetworkflowgroupmap> findBylsusergroup(LSusergroup lsusergroup);
}
