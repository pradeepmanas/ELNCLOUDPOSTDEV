package com.agaram.eln.primary.repository.sequence;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.sequence.SequenceTableProjectLevel;

public interface SequenceTableProjectLevelRepository extends JpaRepository<SequenceTableProjectLevel,Integer>{
	SequenceTableProjectLevel findByProjectcode(Integer projectcode);
	
	@Transactional
	@Modifying
	@Query("update SequenceTableProjectLevel s set s.projectsequence = ?1 where s.projectcode = ?2")
	void setinitialprojectsequence(Long projectsequence, Integer projectcode);
	
	@Transactional
	@Modifying
	@Query("update SequenceTableProjectLevel s set s.projectsequence = 0")
	void resetprojectsequence();
}
