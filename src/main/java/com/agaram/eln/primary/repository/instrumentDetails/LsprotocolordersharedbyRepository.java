package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.Lsordersharedby;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolordersharedby;

public interface LsprotocolordersharedbyRepository extends JpaRepository<Lsprotocolordersharedby, Integer>{

	Lsprotocolordersharedby findBySharebyunifiedidAndSharetounifiedidAndProtocoltypeAndShareprotocolordercode(
			String sharebyunifiedid, String sharetounifiedid, Integer protocoltype, Long shareprotocolordercode);

	List<Lsprotocolordersharedby> findBySharebyunifiedidAndProtocoltypeAndSharestatusOrderBySharedbytoprotocolordercodeDesc(
			String sharebyunifiedid, Integer protocoltype, int i);

	List<Lsprotocolordersharedby> findBySharebyunifiedidAndProtocoltypeAndSharestatus(String sharebyunifiedid,
			Integer protocoltype, int i);

	Lsprotocolordersharedby findBySharedbytoprotocolordercode(Long sharedbytoprotocolordercode);

	int countBySharebyunifiedidAndProtocoltypeAndSharestatusOrderBySharedbytoprotocolordercodeDesc(
			String unifielduserid, Integer protocoltype, int i);

//	Lsprotocolordersharedby findOne(Long sharedbytoprotocolordercode);

}
