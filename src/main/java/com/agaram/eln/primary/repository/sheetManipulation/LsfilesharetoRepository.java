package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.sheetManipulation.Lsfileshareto;



public interface LsfilesharetoRepository extends JpaRepository<Lsfileshareto, Integer>{

	Lsfileshareto findBySharebyunifiedidAndSharetounifiedidAndSharefilecode(String sharebyunifiedid,
			String sharetounifiedid, Long sharefilecode);

	List<Lsfileshareto> findBySharetounifiedidAndSharestatusOrderBySharetofilecodeDesc(String sharetounifiedid, int i);

	Object countBySharetounifiedidAndSharestatus(String sharetounifiedid, int i);

	Lsfileshareto findBySharetofilecode(Long sharetofilecode);


	List<Lsfileshareto> findBySharefilecode(Long filecode);
	@Transactional
	@Modifying
	@Query(value = "update Lsfileshareto set retirestatus =1 where sharefilecode = ?1", nativeQuery = true)
	void updateRetirestatus(Integer filecode);
}
