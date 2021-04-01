package com.agaram.eln.secondary.repository.archive;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.secondary.model.archive.LScfrArchiveHistory;
import com.agaram.eln.secondary.model.archive.LScfrachivetransaction;


public interface LScfrachivetransactionRepository extends JpaRepository<LScfrachivetransaction, Integer> {

	public List<LScfrachivetransaction> findByLscfrArchiveHistoryOrderBySerialnoDesc(LScfrArchiveHistory objhistory);

}
