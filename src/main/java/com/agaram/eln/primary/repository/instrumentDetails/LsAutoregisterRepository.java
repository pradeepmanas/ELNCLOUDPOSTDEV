package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.Date;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.instrumentDetails.LsAutoregister;


public interface LsAutoregisterRepository extends JpaRepository<LsAutoregister, Integer>{

	List<LsAutoregister> findByBatchcode(Long batchcode);

	@Transactional
	List<LsAutoregister> findByAutocreatedateBetweenAndBatchcodeIn(Date fromDate, Date toDate, List<Long> batchcode);

	List<LsAutoregister> findByAutocreatedateBetweenAndBatchcode(Date fromDate, Date toDate, Long batchcode);

//	LsAutoregister findTopByOrderByValueDesc();

	LsAutoregister findTopByOrderByRegcode();

	LsAutoregister findTopByOrderByRegcodeDesc();

	List<LsAutoregister> findByBatchcodeAndScreen(Long batchcode, String screen);
}
