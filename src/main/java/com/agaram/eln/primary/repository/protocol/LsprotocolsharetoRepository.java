package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.Lsprotocolshareto;


public interface LsprotocolsharetoRepository extends JpaRepository<Lsprotocolshareto, Integer>{

	Lsprotocolshareto findBySharebyunifiedidAndSharetounifiedidAndShareprotocolcode(String sharebyunifiedid,
			String sharetounifiedid, Long shareprotocolcode);


	List<Lsprotocolshareto> findBySharetounifiedidAndSharestatusOrderBySharetoprotocolcodeDesc(String sharetounifiedid,
			int i);


	Long countBySharetounifiedidAndSharestatusOrderBySharetoprotocolcodeDesc(String sharetounifiedid, int i);


	Lsprotocolshareto findBysharetoprotocolcode(Long sharetoprotocolcode);

}
