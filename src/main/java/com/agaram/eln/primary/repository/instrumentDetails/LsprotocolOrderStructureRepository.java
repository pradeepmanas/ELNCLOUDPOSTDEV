package com.agaram.eln.primary.repository.instrumentDetails;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolorderstructure;


public interface LsprotocolOrderStructureRepository extends JpaRepository<Lsprotocolorderstructure, Long> {

	Lsprotocolorderstructure findByDirectorycodeAndParentdircodeAndDirectorynameNot(Long directorycode, Long parentdircode,
			String directoryname);

	Lsprotocolorderstructure findByParentdircodeAndDirectoryname(Long parentdircode, String directoryname);

	@Transactional
	@Modifying
	@Query("update Lsprotocolorderstructure o set o.parentdircode = ?1, o.path = ?2, o.directoryname = ?4 where o.directorycode = ?3")
	void updatedirectory(Long parentdircode , String path, Long directorycode, String directoryname);
	
	

}
