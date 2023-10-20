package com.agaram.eln.primary.repository.reports;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.reports.lsreportfile;

public interface ReportfileRepository extends JpaRepository<lsreportfile, Long>{
	@Transactional
	@Modifying
	@Query(value = "select * from "
			+ "lsreportfile where id in (?1) ORDER BY id DESC", nativeQuery=true)
	List<lsreportfile> getReportFile(List<Integer> sampleFileCodeList1);

}
