package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.instrumentDetails.Lsordersharedby;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolordersharedby;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

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

	List<Lsprotocolordersharedby> findBySharebyunifiedidAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
			String string, int i, Date fromdate, Date todate);

	List<Lsprotocolordersharedby> findBySharebyunifiedidAndSharetounifiedidAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
			String unifieduserid, String unifieduserid2, int i, Date fromdate, Date todate);

	Lsprotocolordersharedby findByShareprotocolordercode(Long protocolordercode);



	List<Lsprotocolordersharedby> findBySharebyunifiedidAndOrderflagAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
			String unifieduserid, String orderflag, int i, Date fromdate, Date todate);

	List<Lsprotocolordersharedby>  findBySharebyunifiedidAndProtocoltypeAndOrderflagAndSharetounifiedidAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
			String unifieduserid, Integer protocoltype, String orderflag, String unifieduserid2, int i, Date fromdate,
			Date todate);

	List<Lsprotocolordersharedby> findBySharebyunifiedidAndOrderflagAndSharetounifiedidAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
			String unifieduserid, String orderflag, String unifieduserid2, int i, Date fromdate, Date todate);

	List<Lsprotocolordersharedby> findBySharebyunifiedidAndProtocoltypeAndSharetounifiedidAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
			String unifieduserid, Integer protocoltype, String unifieduserid2, int i, Date fromdate, Date todate);

	List<Lsprotocolordersharedby> findBySharebyunifiedidAndProtocoltypeAndOrderflagAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
			String unifieduserid, Integer protocoltype, String orderflag, int i, Date fromdate, Date todate);

	List<Lsprotocolordersharedby> findByShareprotocolordercodeIn(ArrayList<Long> ordercode);




//	Lsprotocolordersharedby findByShareprotocolordercode(Long shareprotocolordercode);

//	Lsprotocolordersharedby findOne(Long sharedbytoprotocolordercode);

}
