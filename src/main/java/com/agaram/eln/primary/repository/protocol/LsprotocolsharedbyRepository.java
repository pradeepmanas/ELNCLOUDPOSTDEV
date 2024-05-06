package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.protocols.Lsprotocolsharedby;

public interface LsprotocolsharedbyRepository extends JpaRepository<Lsprotocolsharedby, Integer>{


	Lsprotocolsharedby findBySharebyunifiedidAndSharetounifiedidAndShareprotocolcode(String sharebyunifiedid,
			String sharetounifiedid, Long shareprotocolcode);

	List<Lsprotocolsharedby> findBySharebyunifiedidAndSharestatusOrderBySharedbytoprotocolcodeDesc(
			String sharebyunifiedid, int i);

	Long countBySharebyunifiedidAndSharestatusOrderBySharedbytoprotocolcodeDesc(String sharebyunifiedid, int i);

	Lsprotocolsharedby findByshareprotocolcode(Long shareprotocolcode);

	Lsprotocolsharedby findBysharetoprotocolcodeAndSharestatus(Long shareprotocolcode, int i);

	List<Lsprotocolsharedby> findByShareprotocolcodeAndSharestatusOrderBySharedbytoprotocolcodeDesc(Long integer,
			int i);
	@Transactional
	@Modifying
	@Query(value = "update Lsprotocolsharedby set retirestatus =1 where shareprotocolcode = ?1", nativeQuery = true)
	void updateRetirestatus(int protocolmastercode);

	

	
}