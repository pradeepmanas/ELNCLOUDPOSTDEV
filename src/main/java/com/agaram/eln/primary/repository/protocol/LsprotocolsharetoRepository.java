package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.protocols.Lsprotocolshareto;


public interface LsprotocolsharetoRepository extends JpaRepository<Lsprotocolshareto, Integer>{

	Lsprotocolshareto findBySharebyunifiedidAndSharetounifiedidAndShareprotocolcode(String sharebyunifiedid,
			String sharetounifiedid, Long shareprotocolcode);


	List<Lsprotocolshareto> findBySharetounifiedidAndSharestatusOrderBySharetoprotocolcodeDesc(String sharetounifiedid,
			int i);


	Long countBySharetounifiedidAndSharestatusOrderBySharetoprotocolcodeDesc(String sharetounifiedid, int i);


	Lsprotocolshareto findBysharetoprotocolcode(Long sharetoprotocolcode);


	List<Lsprotocolshareto> findByShareprotocolcodeAndSharestatusOrderBySharetoprotocolcodeDesc(Long integer, int i);

	@Transactional
	@Modifying
	@Query(value = "update Lsprotocolshareto set retirestatus =1 where shareprotocolcode = ?1", nativeQuery = true)
	void updateRetirestatus(int protocolmastercode);


}
