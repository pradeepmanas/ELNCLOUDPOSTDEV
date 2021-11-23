package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolordershareto;

public interface LsprotocolordersharetoRepository extends JpaRepository<Lsprotocolordershareto, Integer>{

	Lsprotocolordershareto findBySharebyunifiedidAndSharetounifiedidAndProtocoltypeAndShareprotocolordercode(
			String sharebyunifiedid, String sharetounifiedid, Integer protocoltype, Long shareprotocolordercode);



	List<Lsprotocolordershareto> findBySharetounifiedidAndProtocoltypeAndSharestatusOrderBySharetoprotocolordercodeDesc(
			String sharebyunifiedid, Integer protocoltype, int i);

}
