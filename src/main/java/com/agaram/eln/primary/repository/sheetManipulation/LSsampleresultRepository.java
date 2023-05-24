package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sheetManipulation.LSsampleresult;

public interface LSsampleresultRepository extends JpaRepository<LSsampleresult, Integer>{
	
	public List<LSsampleresult> findByOrderidAndTestcode(long orderid,Integer testcode);
	
	public List<LSsampleresult> findByBatchcodeAndTestcode(long batchcode,Integer testcode);
	
	public LSsampleresult findByBatchcode(Long batchcode);
	
}
