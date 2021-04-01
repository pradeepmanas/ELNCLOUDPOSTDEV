package com.agaram.eln.primary.repository.report;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.report.LSdocreportsversionhistory;

public interface LSdocreportsversionHistoryRepository extends JpaRepository<LSdocreportsversionhistory, Integer>{

	@SuppressWarnings("unchecked")
	LSdocreportsversionhistory save(LSdocreportsversionhistory obj);
	
	void delete(LSdocreportsversionhistory obj);
	
	List<LSdocreportsversionhistory> findAllByStatus(Integer status);
	
	List<LSdocreportsversionhistory> findAllByDocReportsCodeAndStatus(Integer docReportsCode, Integer status);
	
	LSdocreportsversionhistory findFirstByDocReportsCodeAndStatusOrderByVersionNoDesc(Integer docReportsCode, Integer status);
}
