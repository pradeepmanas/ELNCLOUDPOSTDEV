package com.agaram.eln.primary.repository.sequence;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.sequence.SequenceTableTaskLevel;

public interface SequenceTableTaskLevelRepository extends JpaRepository<SequenceTableTaskLevel,Integer>{
	SequenceTableTaskLevel findByTestcode(Integer testcode);
	
	@Transactional
	@Modifying
	@Query("update SequenceTableTaskLevel s set s.tasksequence = ?1 where s.testcode = ?2")
	void setinitialtasksequence(Long tasksequence, Integer testcode);
	
	@Transactional
	@Modifying
	@Query("update SequenceTableTaskLevel s set s.tasksequence = 0")
	void resettasksequence();
}
