package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.LsOrderLinks;

public interface LsOrderLinkRepository extends JpaRepository<LsOrderLinks, Integer> {
		
	List<LsOrderLinks> findByBatchcodeAndNstatus(Long batchcode,Integer status);
}
