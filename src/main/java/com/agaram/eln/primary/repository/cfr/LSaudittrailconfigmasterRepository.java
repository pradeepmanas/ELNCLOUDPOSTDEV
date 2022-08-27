package com.agaram.eln.primary.repository.cfr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.cfr.LSaudittrailconfigmaster;

public interface LSaudittrailconfigmasterRepository extends JpaRepository<LSaudittrailconfigmaster, Integer> {

	List<LSaudittrailconfigmaster> findByOrderByOrdersequnce();
	@Query("SELECT DISTINCT r.screenname FROM LSaudittrailconfigmaster r")
	List<String> findAllByOrderByScreennameAsc();

}
