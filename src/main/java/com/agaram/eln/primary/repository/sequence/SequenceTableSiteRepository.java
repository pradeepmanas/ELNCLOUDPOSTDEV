package com.agaram.eln.primary.repository.sequence;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.sequence.SequenceTableSite;

public interface SequenceTableSiteRepository extends JpaRepository<SequenceTableSite,Integer>{

	SequenceTableSite findBySequencecodeAndSitecode(Integer sequencecode, Integer sitecode);
	
	@Transactional
	@Modifying
	@Query("update SequenceTableSite s set s.sitesequence = ?1 where s.sequencecode = ?2 and sitecode=?3")
	void setinitialsitesequence(Long sitesequence, Integer sequencecode, Integer sitecode);
}
