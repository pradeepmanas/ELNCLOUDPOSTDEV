package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.LSlimsorder;

public interface LSlimsorderRepository extends JpaRepository<LSlimsorder, String>{

	public List<LSlimsorder> findBybatchid(String batchid);
	
	public LSlimsorder findByBatchid(String batchid);
	
	public LSlimsorder findFirstByBatchidOrderByOrderidDesc(String batchid);
	
	public List<LSlimsorder> findByorderflag(String orderflag);
}
