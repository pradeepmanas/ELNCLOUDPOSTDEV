package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.sheetManipulation.LSparsedparameters;

public interface LSparsedparametersRespository extends JpaRepository<LSparsedparameters, Integer>{
	
	public List<LSparsedparameters> findByBatchcodeIn(List<Long> lstBatchcode);
	
	@Transactional
	@Modifying
	@Query("select new com.agaram.eln.primary.model.sheetManipulation.LSparsedparameters(parsedparametercode,batchcode,fieldcode,analysedate,resultvalue,value) from LSparsedparameters where batchcode in (?1) and LENGTH(fieldcode) >3 ORDER BY batchcode DESC")
	public List<LSparsedparameters> getByBatchcodeIn(List<Long> lstBatchcode);
	
	@Transactional
	@Modifying
	@Query("select new com.agaram.eln.primary.model.sheetManipulation.LSparsedparameters(parsedparametercode,batchcode,fieldcode,analysedate,resultvalue,value) from LSparsedparameters where LENGTH(fieldcode) >3 ORDER BY batchcode DESC")
	public List<LSparsedparameters> getallrecords();

}
