package com.agaram.eln.primary.repository.sequence;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.sequence.SequenceTableProject;

public interface SequenceTableProjectRepository extends JpaRepository<SequenceTableProject,Integer>{
	SequenceTableProject findBySequencecodeAndProjectcode(Integer sequencecode, Integer projectcode);
	
	@Transactional
	@Modifying
	@Query("update SequenceTableProject s set s.projectsequence = ?1 where s.sequencecode = ?2 and s.projectcode = ?3")
	void setinitialprojectsequence(Long projectsequence, Integer sequencecode, Integer projectcode);
}
