package com.agaram.eln.primary.repository.sheetManipulation;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefileversion;

public interface LSsamplefileversionRepository  extends JpaRepository<LSsamplefileversion, Integer>{
	public LSsamplefileversion findFirstByFilesamplecodeOrderByVersionnoDesc(LSsamplefile objsamplefile);
	public List<LSsamplefileversion> findByFilesamplecodeOrderByVersionnoDesc(LSsamplefile filecode);
	public LSsamplefileversion findByFilesamplecodeAndVersionnoOrderByVersionnoDesc(LSsamplefile filecode, Integer versionnumber);
	
	@Transactional
	@Modifying
	@Query("select new com.agaram.eln.primary.model.sheetManipulation.LSsamplefileversion(filesamplecodeversion,testid,versionno, versionname"
			+ ",createdate, modifieddate,modifiedby ) from "
			+ "LSsamplefileversion where batchcode = ?1 ORDER BY filesamplecodeversion DESC")
	public List<LSsamplefileversion> getfileversiononbatchcode(long batchcode);
}
