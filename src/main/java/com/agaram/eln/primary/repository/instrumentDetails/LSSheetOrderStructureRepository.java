package com.agaram.eln.primary.repository.instrumentDetails;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.instrumentDetails.LSSheetOrderStructure;

public interface LSSheetOrderStructureRepository extends JpaRepository<LSSheetOrderStructure, Long> {
	@Transactional
	@Modifying
	@Query("update LSSheetOrderStructure o set o.parentdircode = ?1, o.path = ?2 where o.directorycode = ?3")
	void updatedirectory(Long parentdircode , String path, Long directorycode);
	
	@Transactional
	@Modifying
	@Query("delete from LSSheetOrderStructure o where o.directorycode = ?1")
	void deletedirectory(Long directorycode);
	
}
