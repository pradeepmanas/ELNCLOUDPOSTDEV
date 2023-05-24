package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.Lsprotocolsharedby;

public interface LsprotocolsharedbyRepository extends JpaRepository<Lsprotocolsharedby, Integer>{


	Lsprotocolsharedby findBySharebyunifiedidAndSharetounifiedidAndShareprotocolcode(String sharebyunifiedid,
			String sharetounifiedid, Long shareprotocolcode);

	List<Lsprotocolsharedby> findBySharebyunifiedidAndSharestatusOrderBySharedbytoprotocolcodeDesc(
			String sharebyunifiedid, int i);

	Long countBySharebyunifiedidAndSharestatusOrderBySharedbytoprotocolcodeDesc(String sharebyunifiedid, int i);

	Lsprotocolsharedby findByshareprotocolcode(Long shareprotocolcode);

	Lsprotocolsharedby findBysharetoprotocolcodeAndSharestatus(Long shareprotocolcode, int i);

	

	
}