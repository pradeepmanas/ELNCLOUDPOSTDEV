package com.agaram.eln.primary.repository.sequence;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.fetchmodel.sequence.SequenceTablesh;
import com.agaram.eln.primary.model.sequence.SequenceTable;

public interface SequenceTableRepository extends JpaRepository<SequenceTable, Integer>{
	@Transactional
	@Modifying
	@Query("update SequenceTable s set s.applicationsequence = ?1 where s.sequencecode = ?2")
	void setinitialapplicationsequence(Long applicationsequence, Integer sequencecode);
	
	List<SequenceTablesh> findBySequencecodeNotOrderBySequencecode(int code);
	
	@Transactional
	@Modifying
	@Query("update SequenceTable s set s.resetperiod = ?1, s.sequenceview = ?2, "
			+ "s.sequenceformat = ?3, s.seperator = ?4 where s.sequencecode = ?5")
	void updatesequencedata(Integer resetperiod, Integer sequenceview, String sequenceformat,String seperator, Integer sequencecode);

	SequenceTable findBySequencecodeOrderBySequencecode(int i);
}
