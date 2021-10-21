package com.agaram.eln.primary.repository.protocol;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agaram.eln.primary.fetchmodel.getorders.Logilabprotocolorders;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;

public interface LSlogilabprotocoldetailRepository extends JpaRepository<LSlogilabprotocoldetail, Long>{

//	List<LSlogilabprotocoldetail> findByProtocoltype(Integer protocotype);
	
	List<LSlogilabprotocoldetail> findByProtocoltypeAndOrderflag(Integer protocotype, String orderflag);
//	List<LSlogilabprotocoldetail> findByLsprotocolmasterIn(List<Integer> protocolmastercodeArray);
//	@Query("select lsorder from LSlogilabprotocoldetail lsorder where lsorder.Lsprotocolmaster IN (:protocolmastercodeArray)") 
//	List<LSlogilabprotocoldetail> findByLsprotocolmaster(@Param("protocolmastercodeArray") List<LSprotocolmaster> protocolmastercodeArray);
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
	List<LSlogilabprotocoldetail> findTop10ByProtocoltypeAndOrderflagOrderByCreatedtimestampDesc(Integer protocoltype,
			String string);
	int countByProtocoltypeAndOrderflag(Integer protocoltype, String string);
	
	
	@Transactional
	@Modifying
	@Query(value = "select * from "
			+ "LSlogilabprotocoldetail where protocoltype = ?1 and orderflag = ?2  ORDER BY createdtimestamp DESC offset 10 row", nativeQuery = true)
	List<LSlogilabprotocoldetail> getProtocoltypeAndOrderflag(Integer protocoltype, String string);
	
	@Query("select lsorder from LSlogilabprotocoldetail lsorder where lsorder.lsprotocolmaster IN (:protocolmastercodeArray)") 
	List<LSlogilabprotocoldetail> findByLsprotocolmaster(@Param("protocolmastercodeArray") List<LSprotocolmaster> protocolmastercodeArray);


	LSlogilabprotocoldetail findByProtocolordercodeAndProtoclordername(Long protocolordercode, String protoclordername);

	public List<LSlogilabprotocoldetail> findFirst20ByProtocolordercodeLessThanOrderByProtocolordercodeDesc(Long protocolordercode);
	public List<LSlogilabprotocoldetail> findFirst20ByOrderByProtocolordercodeDesc();
	
	public List<LSlogilabprotocoldetail> findFirst20ByProtocolordercodeLessThanAndLsprojectmasterInOrderByProtocolordercodeDesc(Long protocolordercode, List<LSprojectmaster> lstproject);
	public List<LSlogilabprotocoldetail> findFirst20ByLsprojectmasterInOrderByProtocolordercodeDesc(List<LSprojectmaster> lstproject);

	public Long countByLsprojectmasterIn(List<LSprojectmaster> lstproject);
	
	public Integer deleteByLsprojectmaster(LSprojectmaster lsproject);
	
	public List<LSlogilabprotocoldetail> findByLsprojectmasterOrderByProtocolordercodeDesc(LSprojectmaster lsproject);


	List<Logilabprotocolorders> findByOrderflagAndCreatedtimestampBetween(String string, Date fromdate, Date todate);
}
