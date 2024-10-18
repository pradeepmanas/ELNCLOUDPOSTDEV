package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorder;

public interface LSlogilablimsorderRepository extends JpaRepository<LSlogilablimsorder, String>{

	public List<LSlogilablimsorder> findBybatchid(String batchid);
	
	public LSlogilablimsorder findByBatchid(String batchid);
	
	public LSlogilablimsorder findFirstByBatchidOrderByOrderidDesc(String batchid);
	
	public List<LSlogilablimsorder> findByorderflag(String orderflag);

	public List<LSlogilablimsorder> findByBatchidOrderByOrderidDesc(String batchid);
}
