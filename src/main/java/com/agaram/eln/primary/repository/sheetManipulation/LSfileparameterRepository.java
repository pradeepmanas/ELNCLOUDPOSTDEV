package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.sheetManipulation.LSfileparameter;

public interface LSfileparameterRepository extends JpaRepository<LSfileparameter, Integer> {
	@Transactional
	@Modifying
	@Query("delete from LSfileparameter where filecode = ?1")
	public void deleteByfilecode(Integer filecode);
	
	@Transactional
	@Modifying
	@Query("delete from LSfileparameter where filecode = ?1 and fileparametercode not in (?2)")
	public void deleteByFilecodeAndFileparametercodeNotIn(Integer filecode, List<Integer> lsfileParametercode);

	public List<LSfileparameter> findByFilecodeOrderByFileparametercode(Integer filecode);
}
