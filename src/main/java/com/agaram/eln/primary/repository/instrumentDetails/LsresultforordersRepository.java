package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.instrumentDetails.Lsresultfororders;

public interface LsresultforordersRepository  extends JpaRepository<Lsresultfororders, Long>{

	@Transactional
	@Modifying
	@Query(value = "select * from  lsresultfororders where id in (?1) ORDER BY id DESC", nativeQuery=true)
	List<Lsresultfororders> getResultOrders(List<Integer> sampleFileCodeList1);
}
