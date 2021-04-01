package com.agaram.eln.primary.repository.cfr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.cfr.LSaudittrailconfigmaster;

public interface LSaudittrailconfigmasterRepository extends JpaRepository<LSaudittrailconfigmaster, Integer> {

	List<LSaudittrailconfigmaster> findByOrderByOrdersequnce();

}
