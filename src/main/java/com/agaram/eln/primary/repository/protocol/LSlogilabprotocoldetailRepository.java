package com.agaram.eln.primary.repository.protocol;

import java.util.ArrayList;
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
import com.agaram.eln.primary.model.protocols.LSprotocolworkflow;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

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
	
//	List<Logilabprotocolorders> findByCreatedtimestampBetween(Date fromdate, Date todate);

	List<Logilabprotocolorders> findByProtocoltype(Integer protocotype);
	
	long countByOrderflag(String orderflg);
	
//	long countByOrderflagAndCreatedtimestampBetween(String orderflg, Date fromdate, Date todate);
	
//	long countByCreatedtimestampBetween(Date fromdate, Date todate);
//	List<LSlogilabprotocoldetail> findTop10ByProtocoltypeAndOrderflagOrderByCreatedtimestampDesc(Integer protocoltype,
//			String string);
	int countByProtocoltypeAndOrderflag(Integer protocoltype, String string);
	
	
//	@Transactional
//	@Modifying
//	@Query(value = "select * from "
//			+ "LSlogilabprotocoldetail where protocoltype = ?1 and orderflag = ?2  ORDER BY createdtimestamp DESC offset 10 row", nativeQuery = true)
//	List<LSlogilabprotocoldetail> getProtocoltypeAndOrderflag(Integer protocoltype, String string);
	
	@Transactional
	@Modifying
	@Query(value = "select * from "
			+ "LSlogilabprotocoldetail where protocoltype = ?1 and sitecode=?2 and orderflag = ?3 and createdtimestamp BETWEEN ?4 and ?5 and assignedto_usercode IS NULL  ORDER BY createdtimestamp DESC offset 10 row", nativeQuery = true)
	List<LSlogilabprotocoldetail> getProtocoltypeAndSitecodeAndOrderflagAndCreatedtimestampBetween(Integer protocoltype, Integer sitecode, String string,Date fromdate, Date todate);

	@Transactional
	@Modifying
	@Query(value = "select * from "
			+ "LSlogilabprotocoldetail where protocoltype = ?1 and lsprotocolworkflow_workflowcode =?2 and orderflag = ?3 "
			+ "and createdtimestamp BETWEEN ?4 and ?5  ORDER BY createdtimestamp DESC offset 10 row", nativeQuery = true)
	List<LSlogilabprotocoldetail> getProtocoltypeAndLsprotocolworkflowAndOrderflagAndCreatedtimestampBetween
	(Integer protocoltype,Integer workflowcode, String string,Date fromdate, Date todate);

	
	@Query("select lsorder from LSlogilabprotocoldetail lsorder where lsorder.lsprotocolmaster IN (:protocolmastercodeArray)") 
	List<LSlogilabprotocoldetail> findByLsprotocolmaster(@Param("protocolmastercodeArray") List<LSprotocolmaster> protocolmastercodeArray);

	@Transactional
	@Modifying
	@Query("update LSlogilabprotocoldetail set lSprotocolworkflow = :workflow, approved= :approved , rejected= :rejected "
			+ "where protocolordercode in (:protocolordercode)")
	public void updateFileWorkflow(@Param("workflow") LSprotocolworkflow lsprotocolworkflow,
			@Param("approved") Integer approved, @Param("rejected") Integer rejected,
			@Param("protocolordercode") Long protocolordercode);

	LSlogilabprotocoldetail findByProtocolordercodeAndProtoclordername(Long protocolordercode, String protoclordername);

	public List<LSlogilabprotocoldetail> findFirst20ByProtocolordercodeLessThanOrderByProtocolordercodeDesc(Long protocolordercode);
	public List<LSlogilabprotocoldetail> findFirst20ByOrderByProtocolordercodeDesc();
	
	public List<LSlogilabprotocoldetail> findFirst20ByProtocolordercodeLessThanAndLsprojectmasterInOrderByProtocolordercodeDesc(Long protocolordercode, List<LSprojectmaster> lstproject);
	public List<LSlogilabprotocoldetail> findFirst20ByLsprojectmasterInOrderByProtocolordercodeDesc(List<LSprojectmaster> lstproject);

	public Long countByLsprojectmasterIn(List<LSprojectmaster> lstproject);
	
	public Integer deleteByLsprojectmaster(LSprojectmaster lsproject);
	
	public List<LSlogilabprotocoldetail> findByLsprojectmasterOrderByProtocolordercodeDesc(LSprojectmaster lsproject);


//	List<Logilabprotocolorders> findByOrderflagAndCreatedtimestampBetween(String string, Date fromdate, Date todate);


//	List<LSlogilabprotocoldetail> findTop10ByProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//			Integer protocoltype, String string, Date fromdate, Date todate);


//	int countByProtocoltypeAndOrderflagAndCreatedtimestampBetween(Integer protocoltype, String string, Date fromdate,
//			Date todate);

//	int countByOrderflagAndCreatedtimestampBetween(Integer protocoltype, String string, Date fromdate,
//			Date todate);

	List<LSlogilabprotocoldetail> findTop10ByProtocoltypeAndOrderflagAndLSprotocolworkflowAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer protocoltype, String string, LSprotocolworkflow lsprotocolworkflow, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findTop10ByOrderflagAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			String string, Date fromdate, Date todate);


//	List<LSlogilabprotocoldetail> findTop10ByOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//			String string, LSuserMaster lsusermaster, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findTop10ByOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			String string, LSuserMaster lsuserMaster, LSuserMaster assignedto, Date fromdate, Date todate);


//	int countByOrderflagAndCreatebyAndCreatedtimestampBetween(String string, Integer integer,
//			Date fromdate, Date todate);


//	List<LSlogilabprotocoldetail> findTop10ByOrderflagAndLsuserMasterAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//			String string, LSuserMaster lsuserMaster, Date fromdate, Date todate);
	
//	List<LSlogilabprotocoldetail> findTop10ByOrderflagAndCreatebyAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//			String string, Integer integer, Date fromdate, Date todate);


//	int countByOrderflagAndAssignedtoAndCreatedtimestampBetween(String string, LSuserMaster assignedto, Date fromdate,
//			Date todate);


//	int countByOrderflagAndLsuserMasterAndCreatedtimestampBetween(String string, LSuserMaster lsuserMaster,
//			Date fromdate, Date todate);


//	List<LSlogilabprotocoldetail> findByProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//			Integer protocoltype, String string, Date fromdate, Date todate);


//	int countByProtocoltypeAndOrderflagAndAssignedtoAndCreatedtimestampBetween(Integer protocoltype, String string,
//			LSuserMaster assignedto, Date fromdate, Date todate);


//	List<LSlogilabprotocoldetail> findTop10ByProtocoltypeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//			Integer protocoltype, String string, LSuserMaster assignedto, Date fromdate, Date todate);


//	int countByProtocoltypeAndOrderflagAndLsuserMasterAndCreatedtimestampBetween(Integer protocoltype, String string,
//			LSuserMaster lsuserMaster, Date fromdate, Date todate);


//	List<LSlogilabprotocoldetail> findTop10ByProtocoltypeAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//			Integer protocoltype, String string, LSuserMaster lsuserMaster, Date fromdate, Date todate);


	LSlogilabprotocoldetail findByProtocolordercode(Long protocolordercode);


//	List<LSlogilabprotocoldetail> findTop10ByProtocoltypeAndSitecodeAndOrderflagAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//			Integer protocoltype, Integer sitecode, String string, Date fromdate, Date todate);


//	int countByProtocoltypeAndSitecodeAndOrderflagAndCreatedtimestampBetween(Integer protocoltype, Integer sitecode,
//			String string, Date fromdate, Date todate);


//	int countByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoAndCreatedtimestampBetween(Integer protocoltype,
//			Integer sitecode, String string, LSuserMaster assignedto, Date fromdate, Date todate);


	int countByProtocoltypeAndSitecodeAndOrderflagAndLsuserMasterAndCreatedtimestampBetween(Integer protocoltype,
			Integer sitecode, String string, LSuserMaster lsuserMaster, Date fromdate, Date todate);


//	List<LSlogilabprotocoldetail> findTop10ByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//			Integer protocoltype, Integer sitecode, String string, LSuserMaster assignedto, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findTop10ByProtocoltypeAndSitecodeAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer protocoltype, Integer sitecode, String string, LSuserMaster lsuserMaster, Date fromdate,
			Date todate);


//	List<LSlogilabprotocoldetail> findByProtocoltypeAndSitecodeAndOrderflagAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//			Integer protocoltype, Integer sitecode, String string, Date fromdate, Date todate);


//	List<Logilabprotocolorders> findBySitecodeAndCreatedtimestampBetween(Integer sitecode, Date fromdate, Date todate);
	
	List<Logilabprotocolorders> findBySitecodeAndCreatedtimestampBetweenAndAssignedtoIsNull(Integer sitecode, Date fromdate, Date todate);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNull(String string, Integer sitecode,
			Date fromdate, Date todate);


	Object countBySitecodeAndCreatedtimestampBetween(Integer sitecode, Date fromdate, Date todate);


	Object countByOrderflagAndSitecodeAndCreatedtimestampBetween(String string, Integer sitecode, Date fromdate,
			Date todate);


	List<LSlogilabprotocoldetail> findTop10ByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer protocoltype, Integer sitecode, String string, Date fromdate, Date todate);


	int countByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetween(Integer protocoltype,
			Integer sitecode, String string, Date fromdate, Date todate);


//	List<LSlogilabprotocoldetail> findByProtocoltypeAndSitecodeAndOrderflagAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByCreatedtimestampDesc(
//			Integer protocoltype, Integer sitecode, String string, Date fromdate, Date todate);


//	int countByProtocoltypeAndSitecodeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetween(
//			Integer protocoltype, Integer sitecode, String string, LSuserMaster lsuserMaster, LSuserMaster assignedto,
//			Date fromdate, Date todate);


//	List<LSlogilabprotocoldetail> findTop10ByProtocoltypeAndSitecodeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//			Integer protocoltype, Integer sitecode, String string, LSuserMaster lsuserMaster, LSuserMaster assignedto,
//			Date fromdate, Date todate);


//	List<LSlogilabprotocoldetail> findByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//			Integer protocoltype, Integer sitecode, String string, LSuserMaster assignedto, Date fromdate, Date todate);


//	List<LSlogilabprotocoldetail> findByProtocoltypeAndSitecodeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//			Integer protocoltype, Integer sitecode, String string, LSuserMaster lsuserMaster, LSuserMaster assignedto,
//			Date fromdate, Date todate);


//	List<LSlogilabprotocoldetail> findTop10ByProtocoltypeAndSitecodeAndOrderflagAndCreatedtimestampBetweenOrderByCompletedtimestampDesc(
//			Integer protocoltype, Integer sitecode, String string, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer protocoltype, Integer sitecode, String string, Date fromdate, Date todate);


	int countByProtocoltypeAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetween(Integer protocoltype,
			Integer sitecode, LSuserMaster lsuserMaster, LSuserMaster assignedto, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findByProtocoltypeAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer protocoltype, Integer sitecode, LSuserMaster lsuserMaster, LSuserMaster assignedto, Date fromdate,
			Date todate);


	int countByProtocoltypeAndSitecodeAndAssignedtoAndCreatedtimestampBetween(Integer protocoltype, Integer sitecode,
			LSuserMaster assignedto, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findByProtocoltypeAndSitecodeAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer protocoltype, Integer sitecode, LSuserMaster assignedto, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findTop10ByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByCompletedtimestampDesc(
			Integer protocoltype, Integer sitecode, String string, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findByProtocolordercodeInAndOrderflag(ArrayList<Long> log, String orderflag);






}
