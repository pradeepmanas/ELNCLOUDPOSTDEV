package com.agaram.eln.secondary.repository.archive;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.secondary.model.archive.LScfrArchiveHistory;


public interface LScfrArchiveHistoryRepository  extends JpaRepository<LScfrArchiveHistory, Integer> {
	public List<LScfrArchiveHistory> findByOrderByArchivecodeDesc();
}
