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
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
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
	@Query("update LSlogilabprotocoldetail set lsworkflow = :workflow, approved= :approved , rejected= :rejected "
			+ "where protocolordercode in (:protocolordercode)")
	public void updateFileWorkflow(@Param("workflow") LSworkflow lSworkflow,
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


	List<Logilabprotocolorders> findByOrderdisplaytypeAndLsprojectmasterInAndTestcodeIsNotNull(int i,
			List<LSprojectmaster> lstproject);


	List<Logilabprotocolorders> findByOrderdisplaytypeAndLssamplemasterInAndTestcodeIsNotNull(int i,
			List<LSsamplemaster> lstsample);

	@Transactional
	@Modifying
	@Query("update LSlogilabprotocoldetail o set o.directorycode = ?1 where o.directorycode = ?2")
	void updateparentdirectory(Long newdirectorycode , Long olddirectorycode);

	@Transactional
	@Modifying
	@Query("update LSlogilabprotocoldetail o set o.directorycode = ?1 where o.protocolordercode in (?2)")
	void updatedirectory(Long directorycode , List<Long> protocolordercode);

	
	@Transactional
	@Modifying
	@Query("update LSlogilabprotocoldetail o set o.directorycode = ?1 where o.protocolordercode = ?2")
	void updatesingledirectory(Long directorycode , Long batchcode);


	List<Logilabprotocolorders> findByOrderdisplaytypeAndLssamplemasterInAndViewoptionAndTestcodeIsNotNullOrOrderdisplaytypeAndLsuserMasterAndViewoptionAndLssamplemasterInAndTestcodeIsNotNull(
			int i, List<LSsamplemaster> lstsample, int j, int k, LSuserMaster objusermaster, int l,
			List<LSsamplemaster> lstsample2);


	List<Logilabprotocolorders> findByAssignedtoOrderByProtocolordercodeDesc(LSuserMaster lsloginuser);


	List<Logilabprotocolorders> findByAssignedtoAndLsuserMasterOrderByProtocolordercodeDesc(LSuserMaster lsselecteduser,
			LSuserMaster lsloginuser);

	
	@Transactional
	@Modifying
	@Query(value = "select distinct LSlogilabprotocoldetail.testcode, LSlogilabprotocoldetail.lsprojectmaster_projectcode, CAST((select testname from lstestmasterlocal where testcode =  LSlogilabprotocoldetail.testcode) as varchar(10))as testname  from LSlogilabprotocoldetail as LSlogilabprotocoldetail"
			+ " where LSlogilabprotocoldetail.testcode is not null and assignedto_usercode is null and LSlogilabprotocoldetail.lsprojectmaster_projectcode is not null and LSlogilabprotocoldetail.lsprojectmaster_projectcode in (?1)", nativeQuery = true)
	public ArrayList<List<Object>>  getLstestmasterlocalByOrderdisplaytypeAndLsprojectmasterInAndTestcodeIsNotNull(List<Integer> lsprojectcode);

	@Transactional
	@Modifying
	@Query(value = "select distinct LSlogilabprotocoldetail.testcode, LSlogilabprotocoldetail.lssamplemaster_samplecode, CAST((select testname from lstestmasterlocal where testcode =  LSlogilabprotocoldetail.testcode) as varchar(10))as testname from LSlogilabprotocoldetail as LSlogilabprotocoldetail"
			+ " where LSlogilabprotocoldetail.testcode is not null and assignedto_usercode is null and LSlogilabprotocoldetail.lssamplemaster_samplecode is not null and LSlogilabprotocoldetail.lssamplemaster_samplecode in (?1)", nativeQuery = true)
	public ArrayList<List<Object>>  getLstestmasterlocalByOrderdisplaytypeAndLSsamplemasterInAndTestcodeIsNotNull(List<Integer> lssamplecode);


	List<Logilabprotocolorders> findByDirectorycodeOrderByProtocolordercodeDesc(Long directorycode);


	List<LSlogilabprotocoldetail> findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSprojectmaster lsprojectmaster, Integer testcode, int i, Date fromdate, Date todate);




	List<LSlogilabprotocoldetail> findByAssignedtoAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(LSuserMaster lsloginuser,
			Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findByAssignedtoAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSuserMaster lsselecteduser, LSuserMaster lsloginuser, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findByLssamplemasterAndViewoptionAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSsamplemaster lssamplemaster, int i, Integer testcode, int j, Date fromdate, Date todate,
			LSsamplemaster lssamplemaster2, int k, LSuserMaster lsuserMaster, Integer testcode1, int l, Date fromdate2,
			Date todate2);


	List<Logilabprotocolorders> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByProtocolordercodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Date fromdate, Date todate);


	List<Logilabprotocolorders> findByOrderflagAndLsprojectmasterInAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByProtocolordercodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Integer protocoltype, Date fromdate, Date todate);
	

	List<LSlogilabprotocoldetail> findByLssamplemasterAndViewoptionAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndTestcodeAndOrderdisplaytypeAndProtocoltypeAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSsamplemaster lssamplemaster, int i, Integer testcode, int j, Date fromdate, Date todate,
			LSsamplemaster lssamplemaster2, int k, LSuserMaster lsuserMaster, Integer testcode1, int l,
			Integer protocoltype, Date fromdate2, Date todate2);


	List<LSlogilabprotocoldetail> findByProtocolordercodeIn(ArrayList<Long> log);


	List<LSlogilabprotocoldetail> findByProtocolordercodeInAndProtocoltype(ArrayList<Long> log, Integer protocoltype);
	
	
	@Transactional
	@Modifying
	@Query(value = "select protocolordercode from "
			+ "LSlogilabprotocoldetail where assignedto_usercode = ?1 and createdtimestamp BETWEEN ?2 and ?3 order by protocolordercode desc", nativeQuery = true)
	List<Integer> getAssignedtoAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(LSuserMaster lsloginuser,
			Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSprojectmaster lsprojectmaster, Integer testcode, int i, Integer protocoltype, String orderflag,
			Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndProtocoltypeAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSprojectmaster lsprojectmaster, Integer testcode, int i, Integer protocoltype, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndOrderflagAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSprojectmaster lsprojectmaster, Integer testcode, int i, String orderflag, Date fromdate, Date todate);





	List<LSlogilabprotocoldetail> findByLssamplemasterAndViewoptionAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndTestcodeAndOrderdisplaytypeAndProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSsamplemaster lssamplemaster, int i, Integer testcode, int j, Date fromdate, Date todate,
			LSsamplemaster lssamplemaster2, int k, LSuserMaster lsuserMaster, Integer testcode1, int l,
			Integer protocoltype, String orderflag, Date fromdate2, Date todate2);


	List<LSlogilabprotocoldetail> findByLssamplemasterAndViewoptionAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndTestcodeAndOrderdisplaytypeAndOrderflagAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSsamplemaster lssamplemaster, int i, Integer testcode, int j, Date fromdate, Date todate,
			LSsamplemaster lssamplemaster2, int k, LSuserMaster lsuserMaster, Integer testcode1, int l, String orderflag,
			Date fromdate2, Date todate2);


	List<LSlogilabprotocoldetail> findByAssignedtoAndProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSuserMaster lsloginuser, Integer protocoltype, String orderflag, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findByAssignedtoAndOrderflagAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(LSuserMaster lsloginuser,
			String orderflag, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findByAssignedtoAndProtocoltypeAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSuserMaster lsloginuser, Integer protocoltype, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findByAssignedtoAndProtocoltypeAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSuserMaster lsselectedfulluser, Integer protocoltype, String orderflag, LSuserMaster lsloginuser,
			Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findByAssignedtoAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSuserMaster lsselectedfulluser, String orderflag, LSuserMaster lsloginuser, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findByAssignedtoAndProtocoltypeAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSuserMaster lsselectedfulluser, Integer protocoltype, LSuserMaster lsloginuser, Date fromdate,
			Date todate);






	List<LSlogilabprotocoldetail> findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndProtocoltypeAndOrderflagAndRejectedAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSprojectmaster lsprojectmaster, Integer testcode, int i, Integer protocoltype, String orderflag, int j,
			Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndOrderflagAndRejectedAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSprojectmaster lsprojectmaster, Integer testcode, int i, String orderflag, int j, Date fromdate,
			Date todate);


	List<LSlogilabprotocoldetail> findByLssamplemasterAndViewoptionAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndTestcodeAndOrderdisplaytypeAndProtocoltypeAndOrderflagAndRejectedAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSsamplemaster lssamplemaster, int i, Integer testcode, int j, Date fromdate, Date todate,
			LSsamplemaster lssamplemaster2, int k, LSuserMaster lsuserMaster, Integer testcode1, int l,
			Integer protocoltype, String orderflag, int m, Date fromdate2, Date todate2);


	List<LSlogilabprotocoldetail> findByLssamplemasterAndViewoptionAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndTestcodeAndOrderdisplaytypeAndOrderflagAndRejectedAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSsamplemaster lssamplemaster, int i, Integer testcode, int j, Date fromdate, Date todate,
			LSsamplemaster lssamplemaster2, int k, LSuserMaster lsuserMaster, Integer testcode1, int l, String orderflag,
			int m, Date fromdate2, Date todate2);


	List<Logilabprotocolorders> findByOrderflagAndRejectedAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByProtocolordercodeDesc(
			String orderflag, int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate);


	List<Logilabprotocolorders> findByOrderflagAndRejectedAndLsprojectmasterInAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByProtocolordercodeDesc(
			String orderflag, int i, List<LSprojectmaster> lstproject, Integer protocoltype, Date fromdate,
			Date todate);


	List<LSlogilabprotocoldetail> findByAssignedtoAndProtocoltypeAndOrderflagAndRejectedAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSuserMaster lsloginuser, Integer protocoltype, String string, int i, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findBySitecodeAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer sitecode, LSuserMaster assignedto, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findByProtocoltypeAndOrderflagAndSitecodeAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer protocoltype, String orderflag, Integer sitecode, LSuserMaster assignedto, Date fromdate,
			Date todate);


	List<LSlogilabprotocoldetail> findBySitecodeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer sitecode, String orderflag, LSuserMaster assignedto, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findBySitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer sitecode, LSuserMaster lsuserMaster, LSuserMaster assignedto, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findByProtocoltypeAndOrderflagAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer protocoltype, String orderflag, Integer sitecode, LSuserMaster lsuserMaster,
			LSuserMaster assignedto, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findByOrderflagAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			String orderflag, Integer sitecode, LSuserMaster lsuserMaster, LSuserMaster assignedto, Date fromdate,
			Date todate);


	List<Logilabprotocolorders> findByOrderflagAndLsprojectmasterAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByProtocolordercodeDesc(
			String orderflag, LSprojectmaster lsprojectmaster, Integer protocoltype, Date fromdate, Date todate);


	List<Logilabprotocolorders> findByOrderflagAndLsprojectmasterInAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByProtocolordercodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Integer testcode, Integer protocoltype, Date fromdate,
			Date todate);


	List<Logilabprotocolorders> findByOrderflagAndLsprojectmasterAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByProtocolordercodeDesc(
			String orderflag, LSprojectmaster lsprojectmaster, Integer testcode, Integer protocoltype, Date fromdate,
			Date todate);


	List<Logilabprotocolorders> findByOrderflagAndRejectedAndLsprojectmasterInAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByProtocolordercodeDesc(
			String orderflag, int i, List<LSprojectmaster> lstproject, Integer testcode, Integer protocoltype,
			Date fromdate, Date todate);


	List<Logilabprotocolorders> findByOrderflagAndRejectedAndLsprojectmasterAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByProtocolordercodeDesc(
			String orderflag, int i, LSprojectmaster lsprojectmaster, Integer protocoltype, Date fromdate, Date todate);


	List<Logilabprotocolorders> findByOrderflagAndRejectedAndLsprojectmasterAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByProtocolordercodeDesc(
			String orderflag, int i, LSprojectmaster lsprojectmaster, Integer testcode, Integer protocoltype,
			Date fromdate, Date todate);


	List<Logilabprotocolorders> findByOrderflagAndDirectorycodeAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByProtocolordercodeDesc(
			String orderflag, Long directorycode, Integer protocoltype, Date fromdate, Date todate);


	List<Logilabprotocolorders> findByOrderflagAndRejectedAndDirectorycodeAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByProtocolordercodeDesc(
			String orderflag, int i, Long directorycode, Integer protocoltype, Date fromdate, Date todate);


	List<Logilabprotocolorders> findByOrderflagAndDirectorycodeAndLsprojectmasterAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByProtocolordercodeDesc(
			String orderflag, Long directorycode, LSprojectmaster lsprojectmaster, Integer protocoltype, Date fromdate,
			Date todate);


	List<Logilabprotocolorders> findByOrderflagAndDirectorycodeAndLsprojectmasterIsNullAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByProtocolordercodeDesc(
			String orderflag, Long directorycode, Integer testcode, Integer protocoltype, Date fromdate, Date todate);


	List<Logilabprotocolorders> findByOrderflagAndRejectedAndDirectorycodeAndLsprojectmasterIsNullAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByProtocolordercodeDesc(
			String orderflag, int i, Long directorycode, List<LSprojectmaster> lstproject, Integer testcode,
			Integer protocoltype, Date fromdate, Date todate);

//	directorybased

	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndAssignedtoIsNullAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndAssignedtoIsNullAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			Long directorycode, int i, Date fromdate, Date todate, Long directorycode2, int j, LSuserMaster createdby,
			Date fromdate2, Date todate2);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndAssignedtoIsNullAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			Long directorycode, int i, Integer protocoltype, String orderflag, Date fromdate, Date todate,
			Long directorycode2, int j, Integer protocoltype2, String orderflag2, LSuserMaster createdby,
			Date fromdate2, Date todate2);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndAssignedtoIsNullAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndAssignedtoIsNullAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			Long directorycode, int i, Integer protocoltype, String orderflag, int j, Date fromdate, Date todate,
			Long directorycode2, int k, Integer protocoltype2, String orderflag2, int l, LSuserMaster createdby,
			Date fromdate2, Date todate2);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndOrderflagAndAssignedtoIsNullAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			Long directorycode, int i, String orderflag, Date fromdate, Date todate, Long directorycode2, int j,
			String orderflag2, LSuserMaster createdby, Date fromdate2, Date todate2);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndOrderflagAndRejectedAndAssignedtoIsNullAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndOrderflagAndRejectedAndAssignedtoIsNullAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			Long directorycode, int i, String orderflag, int j, Date fromdate, Date todate, Long directorycode2, int k,
			String orderflag2, int l, LSuserMaster createdby, Date fromdate2, Date todate2);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndProtocoltypeAndAssignedtoIsNullAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndAssignedtoIsNullAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			Long directorycode, int i, Integer protocoltype, Date fromdate, Date todate, Long directorycode2, int j,
			Integer protocoltype2, LSuserMaster createdby, Date fromdate2, Date todate2);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndAssignedtoIsNullAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndAssignedtoIsNullAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndAssignedtoIsNullAndCreatedtimestampBetweenAndTeamcodeInOrderByProtocolordercodeDesc(
			Long directorycode, int i, Date fromdate, Date todate, Long directorycode2, int j, LSuserMaster createdby,
			Date fromdate2, Date todate2, Long directorycode3, int k, Date fromdate3, Date todate3,
			List<Integer> lSuserteammappingobj);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndAssignedtoIsNullAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndAssignedtoIsNullAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndAssignedtoIsNullAndCreatedtimestampBetweenAndTeamcodeInOrderByProtocolordercodeDesc(
			Long directorycode, int i, Integer protocoltype, String orderflag, int j, Date fromdate, Date todate,
			Long directorycode2, int k, Integer protocoltype2, String orderflag2, int l, LSuserMaster createdby,
			Date fromdate2, Date todate2, Long directorycode3, int m, Integer protocoltype3, String orderflag3, int n,
			Date fromdate3, Date todate3, List<Integer> lSuserteammappingobj);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndAssignedtoIsNullAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenAndTeamcodeInOrderByProtocolordercodeDesc(
			Long directorycode, int i, Integer protocoltype, String orderflag, Date fromdate, Date todate,
			Long directorycode2, int j, Integer protocoltype2, String orderflag2, LSuserMaster createdby,
			Date fromdate2, Date todate2, Long directorycode3, int k, Integer protocoltype3, String orderflag3,
			Date fromdate3, Date todate3, List<Integer> lSuserteammappingobj);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndOrderflagAndRejectedAndAssignedtoIsNullAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndOrderflagAndRejectedAndAssignedtoIsNullAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndOrderflagAndRejectedAndAssignedtoIsNullAndCreatedtimestampBetweenAndTeamcodeInOrderByProtocolordercodeDesc(
			Long directorycode, int i, String orderflag, int j, Date fromdate, Date todate, Long directorycode2, int k,
			String orderflag2, int l, LSuserMaster createdby, Date fromdate2, Date todate2, Long directorycode3, int m,
			String orderflag3, int n, Date fromdate3, Date todate3, List<Integer> lSuserteammappingobj);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndOrderflagAndAssignedtoIsNullAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			Long directorycode, int i, String orderflag, Date fromdate, Date todate, Long directorycode2, int j,
			String orderflag2, LSuserMaster createdby, Date fromdate2, Date todate2, Long directorycode3, int k,
			String orderflag3, Date fromdate3, Date todate3, List<Integer> lSuserteammappingobj);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndProtocoltypeAndAssignedtoIsNullAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndAssignedtoIsNullAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndAssignedtoIsNullAndCreatedtimestampBetweenAndTeamcodeInOrderByProtocolordercodeDesc(
			Long directorycode, int i, Integer protocoltype, Date fromdate, Date todate, Long directorycode2, int j,
			Integer protocoltype2, LSuserMaster createdby, Date fromdate2, Date todate2, Long directorycode3, int k,
			Integer protocoltype3, Date fromdate3, Date todate3, List<Integer> lSuserteammappingobj);





	




	
}
