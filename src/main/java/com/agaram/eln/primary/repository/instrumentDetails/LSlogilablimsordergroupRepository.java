package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsordergroup;

public interface LSlogilablimsordergroupRepository extends JpaRepository<LSlogilablimsordergroup, String>{

	public List<LSlogilablimsordergroup> findByBatchid(String batchid);

	public LSlogilablimsordergroup findByLimsprimarycode(Long limsprimarycode);
	
	public List<LSlogilablimsordergroup> findByGroupid(String groupid);

}
