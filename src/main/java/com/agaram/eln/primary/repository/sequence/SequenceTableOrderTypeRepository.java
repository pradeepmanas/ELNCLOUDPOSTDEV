package com.agaram.eln.primary.repository.sequence;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.sequence.SequenceTableOrderType;

public interface SequenceTableOrderTypeRepository extends JpaRepository<SequenceTableOrderType, Integer>{
	SequenceTableOrderType findBySequencecodeAndOrdertype(Integer sequencecode, Integer ordertype);
	
	@Transactional
	@Modifying
	@Query("update SequenceTableOrderType s set s.ordertypesequence = ?1 where s.sequencecode = ?2 and s.ordertype= ?3")
	void setinitialordertypesequence(Long ordertypesequence, Integer sequencecode, Integer ordertype);
}
