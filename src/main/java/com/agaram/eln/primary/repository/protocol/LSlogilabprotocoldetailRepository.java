package com.agaram.eln.primary.repository.protocol;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agaram.eln.primary.fetchmodel.getorders.Logilabprotocolorders;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;

public interface LSlogilabprotocoldetailRepository extends JpaRepository<LSlogilabprotocoldetail, Long>{

//	List<LSlogilabprotocoldetail> findByProtocoltype(Integer protocotype);
	
	List<LSlogilabprotocoldetail> findByProtocoltypeAndOrderflag(Integer protocotype, String orderflag);
//	List<LSlogilabprotocoldetail> findByLsprotocolmasterIn(List<Integer> protocolmastercodeArray);
	@Query("select lsorder from LSlogilabprotocoldetail lsorder where lsorder.Lsprotocolmaster IN (:protocolmastercodeArray)") 
	List<LSlogilabprotocoldetail> findByLsprotocolmaster(@Param("protocolmastercodeArray") List<LSprotocolmaster> protocolmastercodeArray);
//	@Query("select lsorder from LSlogilabprotocoldetail lsorder where lsorder.lsprotocolmaster_protocolmastercode IN (:protocolmastercodeArray)") 
//	List<LSlogilabprotocoldetail> findByLsprotocolmaster(List<Integer> protocolmastercodeArray);

	
	/**
	 * Added by sathishkumar chandrasekar 
	 * 
	 * for getting protocol orders in dashboard
	 * 
	 * @param orderflag
	 * @return
	 */
	
	List<Logilabprotocolorders> findByCreatedtimestampBetween(Date fromdate, Date todate);

	List<Logilabprotocolorders> findByProtocoltype(Integer protocotype);
	
	long countByOrderflag(String orderflg);
	
	long countByOrderflagAndCreatedtimestampBetween(String orderflg, Date fromdate, Date todate);
	
	long countByCreatedtimestampBetween(Date fromdate, Date todate);
}
