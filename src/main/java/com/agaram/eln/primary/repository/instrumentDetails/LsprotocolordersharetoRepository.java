package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolordersharedby;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolordershareto;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

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



	Lsprotocolordershareto findBySharetoprotocolordercodeAndSharestatus(Long sharetoprotocolordercode, int i);



	Object findBySharetounifiedidAndSharestatusOrderBySharetoprotocolordercodeDesc(LSuserMaster lsselecteduser, int i);



	List<Lsprotocolordershareto> findBySharetounifiedidAndSharestatusAndSharedonBetweenOrderBySharetoprotocolordercodeDesc(
			String string, int i, Date fromdate, Date todate);



	Lsprotocolordershareto findByShareprotocolordercode(Long protocolordercode);






	List<Lsprotocolordershareto> findBySharetounifiedidAndOrderflagAndSharestatusAndSharedonBetweenOrderBySharetoprotocolordercodeDesc(
			String unifieduserid, String orderflag, int i, Date fromdate, Date todate);



	List<Lsprotocolordershareto> findBySharetounifiedidAndProtocoltypeAndOrderflagAndSharestatusAndSharedonBetweenOrderBySharetoprotocolordercodeDesc(
			String unifieduserid, Integer protocoltype, String orderflag, int i, Date fromdate, Date todate);



	List<Lsprotocolordershareto> findByShareprotocolordercodeIn(ArrayList<Long> ordercode);



	



//	Lsprotocolordershareto findOne(Long sharetoprotocolordercode);

}
