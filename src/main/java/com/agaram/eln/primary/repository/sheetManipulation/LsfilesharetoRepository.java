package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sheetManipulation.Lsfileshareto;



public interface LsfilesharetoRepository extends JpaRepository<Lsfileshareto, Integer>{

	Lsfileshareto findBySharebyunifiedidAndSharetounifiedidAndSharefilecode(String sharebyunifiedid,
			String sharetounifiedid, Long sharefilecode);

	List<Lsfileshareto> findBySharetounifiedidAndSharestatusOrderBySharetofilecodeDesc(String sharetounifiedid, int i);

	Object countBySharetounifiedidAndSharestatus(String sharetounifiedid, int i);

	Lsfileshareto findBySharetofilecode(Long sharetofilecode);

}
