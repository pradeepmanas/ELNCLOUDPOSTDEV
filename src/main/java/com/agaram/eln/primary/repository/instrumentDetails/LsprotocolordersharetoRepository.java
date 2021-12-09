package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolordershareto;

public interface LsprotocolordersharetoRepository extends JpaRepository<Lsprotocolordershareto, Integer>{

	Lsprotocolordershareto findBySharebyunifiedidAndSharetounifiedidAndProtocoltypeAndShareprotocolordercode(
			String sharebyunifiedid, String sharetounifiedid, Integer protocoltype, Long shareprotocolordercode);



//	List<Lsprotocolordershareto> findBySharetounifiedidAndProtocoltypeAndSharestatusOrderBySharetoprotocolordercodeDesc(
//			String sharebyunifiedid, Integer protocoltype, int i);



	Lsprotocolordershareto findBySharetoprotocolordercode(Long sharetoprotocolordercode);



//	int countBySharetounifiedidAndProtocoltypeAndSharestatusOrderBySharetoprotocolordercodeDesc(String unifielduserid,
//			Integer protocoltype, int i);



	List<Lsprotocolordershareto> findBySharetounifiedidAndProtocoltypeAndSharestatusAndSharedonBetweenOrderBySharetoprotocolordercodeDesc(
			String sharetounifiedid, Integer protocoltype, int i, Date fromdate, Date todate);



	int countBySharetounifiedidAndProtocoltypeAndSharestatusAndSharedonBetweenOrderBySharetoprotocolordercodeDesc(
			String sharebyunifiedid, Integer protocoltype, int i, Date fromdate, Date fromdate2);



//	Lsprotocolordershareto findOne(Long sharetoprotocolordercode);

}
