package com.agaram.eln.primary.repository.sequence;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.sequence.SequenceTableTask;

public interface SequenceTableTaskRepository extends JpaRepository<SequenceTableTask,Integer>{
	SequenceTableTask findBySequencecodeAndTestcode(Integer sequencecode, Integer testcode);
	
	@Transactional
	@Modifying
	@Query("update SequenceTableTask s set s.tasksequence = ?1 where s.sequencecode = ?2 and s.testcode = ?3")
	void setinitialtasksequence(Long tasksequence, Integer sequencecode, Integer testcode);
}
