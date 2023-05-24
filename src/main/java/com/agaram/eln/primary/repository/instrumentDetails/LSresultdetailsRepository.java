package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.LSresultdetails;

public interface LSresultdetailsRepository  extends JpaRepository<LSresultdetails, String> {
	public List<LSresultdetails> findBylimsreferencecodeIn(List<String> lstordercode);
}
