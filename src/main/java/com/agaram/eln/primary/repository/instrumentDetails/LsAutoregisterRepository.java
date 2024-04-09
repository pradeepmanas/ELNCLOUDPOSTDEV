package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.model.instrumentDetails.LsAutoregister;

public interface LsAutoregisterRepository extends JpaRepository<LsAutoregister, Integer>{

	@Transactional
	List<LsAutoregister> findByAutocreatedateBetweenAndBatchcodeIn(Date fromDate, Date toDate, List<Long> batchcode);

	List<LsAutoregister> findByBatchcode(Long batchcode);

}
