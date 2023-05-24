package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.Lsbatchdetails;

public interface LsbatchdetailsRepository extends JpaRepository<Lsbatchdetails, Integer> {
	
	List<Lsbatchdetails> findByBatchcode(Long batchcode);

}
