package com.agaram.eln.primary.repository.instrumentDetails;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsordergroup;

public interface LSlogilablimsordergroupRepository extends JpaRepository<LSlogilablimsordergroup, String>{

	public LSlogilablimsordergroup findByBatchid(String batchid);

}
