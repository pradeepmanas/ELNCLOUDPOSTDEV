package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;

public interface LSsamplefileRepository extends JpaRepository<LSsamplefile, Integer>{
	public List<LSsamplefile> findByprocessed(Integer processed);
	public List<LSsamplefile> findByfilesamplecodeIn(List<Integer> lstSampleid);
	public LSsamplefile findByfilesamplecode(Integer filesamplecode);
	
	@Transactional
	@Modifying
	@Query(value = "select filesamplecode from "
			+ "LSsamplefile where batchcode in (?1) ORDER BY batchcode DESC", nativeQuery=true)
	public List<Integer> getFilesamplecodeByBatchcodeIn(List<Long> lstbatchcode);
	
	@Transactional
	 @Modifying
	 @Query("update LSsamplefile o set o.batchcode = ?1 where o.filesamplecode = ?2")
	 void setbatchcodeOnsamplefile(Long batchcode, Integer filesamplecode);
	
}
