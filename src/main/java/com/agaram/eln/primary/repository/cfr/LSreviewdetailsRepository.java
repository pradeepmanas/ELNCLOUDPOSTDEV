package com.agaram.eln.primary.repository.cfr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.cfr.LSreviewdetails;

public interface LSreviewdetailsRepository extends JpaRepository<LSreviewdetails,Integer>{
	
		public List<LSreviewdetails> findByserialnoIn(List<Integer> lstserailno);

		public List<LSreviewdetails> findByAuditserialnoIn(List<Integer> lstserailno);




}
