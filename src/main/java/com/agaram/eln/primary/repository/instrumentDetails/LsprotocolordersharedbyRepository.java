package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.Lsordersharedby;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolordersharedby;

public interface LsprotocolordersharedbyRepository extends JpaRepository<Lsprotocolordersharedby, Integer>{

	Lsprotocolordersharedby findBySharebyunifiedidAndSharetounifiedidAndProtocoltypeAndShareprotocolordercode(
			String sharebyunifiedid, String sharetounifiedid, Integer protocoltype, Long shareprotocolordercode);

//	List<Lsprotocolordersharedby> findBySharebyunifiedidAndProtocoltypeAndSharestatusOrderBySharedbytoprotocolordercodeDesc(
//			String sharebyunifiedid, Integer protocoltype, int i);
	
	

	List<Lsprotocolordersharedby> findBySharebyunifiedidAndProtocoltypeAndSharestatus(String sharebyunifiedid,
			Integer protocoltype, int i);

	Lsprotocolordersharedby findBySharedbytoprotocolordercode(Long sharedbytoprotocolordercode);

//	int countBySharebyunifiedidAndProtocoltypeAndSharestatusOrderBySharedbytoprotocolordercodeDesc(
//			String unifielduserid, Integer protocoltype, int i);

	List<Lsprotocolordersharedby> findBySharebyunifiedidAndProtocoltypeAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
			String sharebyunifiedid, Integer protocoltype, int i, Date fromdate, Date todate);

	int countBySharebyunifiedidAndProtocoltypeAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
			String sharebyunifiedid, Integer protocoltype, int i, Date fromdate, Date fromdate2);

	Lsprotocolordersharedby findByShareprotocolordercodeAndSharestatus(Long shareprotocolordercode, int i);

//	Lsprotocolordersharedby findByShareprotocolordercode(Long shareprotocolordercode);

//	Lsprotocolordersharedby findOne(Long sharedbytoprotocolordercode);

}
