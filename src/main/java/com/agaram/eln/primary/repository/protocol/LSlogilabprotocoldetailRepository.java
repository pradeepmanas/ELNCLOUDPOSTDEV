package com.agaram.eln.primary.repository.protocol;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
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

	public List<Logilabprotocolorders> findFirst20ByProtocolordercodeLessThanOrderByProtocolordercodeDesc(Long protocolordercode);
	public List<Logilabprotocolorders> findFirst20ByOrderByProtocolordercodeDesc();
	
	public List<Logilabprotocolorders> findFirst20ByProtocolordercodeLessThanAndLsprojectmasterInOrderByProtocolordercodeDesc(Long protocolordercode, List<LSprojectmaster> lstproject);
	public List<Logilabprotocolorders> findFirst20ByLsprojectmasterInOrderByProtocolordercodeDesc(List<LSprojectmaster> lstproject);

	public Long countByLsprojectmasterIn(List<LSprojectmaster> lstproject);
	
	public Integer deleteByLsprojectmaster(LSprojectmaster lsproject);
	
	public List<LSlogilabprotocoldetail> findByLsprojectmasterOrderByProtocolordercodeDesc(LSprojectmaster lsproject);


	List<LSlogilabprotocoldetail> findTop10ByProtocoltypeAndOrderflagAndLSprotocolworkflowAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer protocoltype, String string, LSprotocolworkflow lsprotocolworkflow, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findTop10ByOrderflagAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			String string, Date fromdate, Date todate);




	List<LSlogilabprotocoldetail> findTop10ByOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			String string, LSuserMaster lsuserMaster, LSuserMaster assignedto, Date fromdate, Date todate);




	LSlogilabprotocoldetail findByProtocolordercode(Long protocolordercode);


	int countByProtocoltypeAndSitecodeAndOrderflagAndLsuserMasterAndCreatedtimestampBetween(Integer protocoltype,
			Integer sitecode, String string, LSuserMaster lsuserMaster, Date fromdate, Date todate);


//	List<LSlogilabprotocoldetail> findTop10ByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//			Integer protocoltype, Integer sitecode, String string, LSuserMaster assignedto, Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findTop10ByProtocoltypeAndSitecodeAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer protocoltype, Integer sitecode, String string, LSuserMaster lsuserMaster, Date fromdate,
			Date todate);






	List<LSlogilabprotocoldetail> findTop10ByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer protocoltype, Integer sitecode, String string, Date fromdate, Date todate);


	int countByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetween(Integer protocoltype,
			Integer sitecode, String string, Date fromdate, Date todate);



	List<LSlogilabprotocoldetail> findByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer protocoltype, Integer sitecode, String string, Date fromdate, Date todate);


	int countByProtocoltypeAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetween(Integer protocoltype,
			Integer sitecode, LSuserMaster lsuserMaster, LSuserMaster assignedto, Date fromdate, Date todate);


	List<Logilabprotocolorders> findByProtocoltypeAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer protocoltype, Integer sitecode, LSuserMaster lsuserMaster, LSuserMaster assignedto, Date fromdate,
			Date todate);


	int countByProtocoltypeAndSitecodeAndAssignedtoAndCreatedtimestampBetween(Integer protocoltype, Integer sitecode,
			LSuserMaster assignedto, Date fromdate, Date todate);


	List<Logilabprotocolorders> findByProtocoltypeAndSitecodeAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
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


//	List<Logilabprotocolorders> findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
//			LSprojectmaster lsprojectmaster, Integer testcode, int i, Date fromdate, Date todate);
//
//


	List<LSlogilabprotocoldetail> findByAssignedtoAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(LSuserMaster lsloginuser,
			Date fromdate, Date todate);


	List<LSlogilabprotocoldetail> findByAssignedtoAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSuserMaster lsselecteduser, LSuserMaster lsloginuser, Date fromdate, Date todate);


//	List<LSlogilabprotocoldetail> findByLssamplemasterAndViewoptionAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
//			LSsamplemaster lssamplemaster, int i, Integer testcode, int j, Date fromdate, Date todate,
//			LSsamplemaster lssamplemaster2, int k, LSuserMaster lsuserMaster, Integer testcode1, int l, Date fromdate2,
//			Date todate2);


	List<Logilabprotocolorders> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByProtocolordercodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Date fromdate, Date todate);




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


	List<Logilabprotocolorders> findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSprojectmaster lsprojectmaster, Integer testcode, int i, Integer protocoltype, String orderflag,
			Date fromdate, Date todate);


	List<Logilabprotocolorders> findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndProtocoltypeAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSprojectmaster lsprojectmaster, Integer testcode, int i, Integer protocoltype, Date fromdate, Date todate);


	List<Logilabprotocolorders> findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndOrderflagAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
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






	List<Logilabprotocolorders> findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndProtocoltypeAndOrderflagAndRejectedAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSprojectmaster lsprojectmaster, Integer testcode, int i, Integer protocoltype, String orderflag, int j,
			Date fromdate, Date todate);


	List<Logilabprotocolorders> findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndOrderflagAndRejectedAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
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




	List<LSlogilabprotocoldetail> findByAssignedtoAndProtocoltypeAndOrderflagAndRejectedAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSuserMaster lsloginuser, Integer protocoltype, String string, int i, Date fromdate, Date todate);


	List<Logilabprotocolorders> findBySitecodeAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer sitecode, LSuserMaster assignedto, Date fromdate, Date todate);


	List<Logilabprotocolorders> findByProtocoltypeAndOrderflagAndSitecodeAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer protocoltype, String orderflag, Integer sitecode, LSuserMaster assignedto, Date fromdate,
			Date todate);


	List<Logilabprotocolorders> findBySitecodeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer sitecode, String orderflag, LSuserMaster assignedto, Date fromdate, Date todate);


	List<Logilabprotocolorders> findBySitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer sitecode, LSuserMaster lsuserMaster, LSuserMaster assignedto, Date fromdate, Date todate);


	List<Logilabprotocolorders> findByProtocoltypeAndOrderflagAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			Integer protocoltype, String orderflag, Integer sitecode, LSuserMaster lsuserMaster,
			LSuserMaster assignedto, Date fromdate, Date todate);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			String orderflag, Integer sitecode, LSuserMaster lsuserMaster, LSuserMaster assignedto, Date fromdate,
			Date todate);




	List<Logilabprotocolorders> findByOrderflagAndDirectorycodeAndLsprojectmasterAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByProtocolordercodeDesc(
			String orderflag, Long directorycode, LSprojectmaster lsprojectmaster, Integer protocoltype, Date fromdate,
			Date todate);


//	directorybased


	List<Logilabprotocolorders> findByDirectorycodeInAndViewoptionAndAssignedtoIsNullAndCreatedtimestampBetweenOrDirectorycodeInAndViewoptionAndAssignedtoIsNullAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			List<Long> directorycode, int i, Date fromdate, Date todate, List<Long> directorycode2, int j,
			LSuserMaster objuser, Date fromdate2, Date todate2);


	List<Logilabprotocolorders> findByDirectorycodeInAndViewoptionAndAssignedtoIsNullAndCreatedtimestampBetweenOrDirectorycodeInAndViewoptionAndAssignedtoIsNullAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeInAndViewoptionAndAssignedtoIsNullAndCreatedtimestampBetweenAndTeamcodeInOrderByProtocolordercodeDesc(
			List<Long> directorycode, int i, Date fromdate, Date todate, List<Long> directorycode2, int j,
			LSuserMaster objuser, Date fromdate2, Date todate2, List<Long> directorycode3, int k, Date fromdate3,
			Date todate3, List<Integer> teamcode);


	List<Logilabprotocolorders> findFirst20ByLsprojectmasterInOrDirectorycodeInOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, List<Long> directorycode);

	public Long countByLsprojectmasterInOrDirectorycodeIn(List<LSprojectmaster> lstproject, List<Long> directorycode);


	List<Logilabprotocolorders> findFirst20ByProtocolordercodeLessThanAndLsprojectmasterInOrProtocolordercodeLessThanAndDirectorycodeInOrderByProtocolordercodeDesc(
			Long protocolordercode, List<LSprojectmaster> lstproject, Long protocolordercode2,
			List<Long> directorycode);



	List<Logilabprotocolorders> findByOrderflagAndRejectedAndLsprojectmasterInAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndRejectedAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLssamplemasterInOrderByProtocolordercodeDesc(
			String orderflag, int i, List<LSprojectmaster> lstproject, Integer protocoltype, Date fromdate, Date todate,
			String orderflag2, int j, Integer protocoltype2, Date fromdate2, Date todate2,
			List<LSsamplemaster> lstsample);


	List<Logilabprotocolorders> findByOrderflagAndLsprojectmasterAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLssamplemasterInOrderByProtocolordercodeDesc(
			String orderflag, LSprojectmaster lsprojectmaster, Integer protocoltype, Date fromdate, Date todate,
			String orderflag2, Integer protocoltype2, Date fromdate2, Date todate2, List<LSsamplemaster> lstsample);


	List<Logilabprotocolorders> findByOrderflagAndLsprojectmasterInAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLssamplemasterInOrderByProtocolordercodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Integer testcode, Integer protocoltype, Date fromdate,
			Date todate, String orderflag2, Integer testcode2, Integer protocoltype2, Date fromdate2, Date todate2,
			List<LSsamplemaster> lstsample);


	List<Logilabprotocolorders> findByOrderflagAndLsprojectmasterAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLssamplemasterInOrderByProtocolordercodeDesc(
			String orderflag, LSprojectmaster lsprojectmaster, Integer testcode, Integer protocoltype, Date fromdate,
			Date todate, String orderflag2, Integer testcode2, Integer protocoltype2, Date fromdate2, Date todate2,
			List<LSsamplemaster> lstsample);


	List<Logilabprotocolorders> findByOrderflagAndRejectedAndLsprojectmasterInAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndRejectedAndLsprojectmasterIsNullAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLssamplemasterInOrderByProtocolordercodeDesc(
			String orderflag, int i, List<LSprojectmaster> lstproject, Integer testcode, Integer protocoltype,
			Date fromdate, Date todate, String orderflag2, int j, Integer testcode2, Integer protocoltype2,
			Date fromdate2, Date todate2, List<LSsamplemaster> lstsample);


	List<Logilabprotocolorders> findByOrderflagAndRejectedAndLsprojectmasterAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndRejectedAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLssamplemasterInOrderByProtocolordercodeDesc(
			String orderflag, int i, LSprojectmaster lsprojectmaster, Integer protocoltype, Date fromdate, Date todate,
			String orderflag2, int j, Integer protocoltype2, Date fromdate2, Date todate2,
			List<LSsamplemaster> lstsample);


	List<Logilabprotocolorders> findByOrderflagAndRejectedAndLsprojectmasterAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndRejectedAndLsprojectmasterIsNullAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLssamplemasterInOrderByProtocolordercodeDesc(
			String orderflag, int i, LSprojectmaster lsprojectmaster, Integer testcode, Integer protocoltype,
			Date fromdate, Date todate, String orderflag2, int j, Integer testcode2, Integer protocoltype2,
			Date fromdate2, Date todate2, List<LSsamplemaster> lstsample);


//	List<Logilabprotocolorders> findBySitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterIn(
//			Integer sitecode, java.util.Date fromdate2, java.util.Date todate, List<LSprojectmaster> lstproject);


//	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterIn(
//			String string, Integer sitecode, java.util.Date fromdate2, java.util.Date todate,
//			List<LSprojectmaster> lstproject);


	Object countBySitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(Integer sitecode, Date fromdate, Date todate,
			List<LSprojectmaster> lstproject);


//	Object countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(String string, Integer sitecode,
//			Date fromdate, Date todate, List<LSprojectmaster> lstproject);	
//	
	Object countByApprovelstatusNotAndOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(Integer i,String string, Integer sitecode,
			Date fromdate, Date todate, List<LSprojectmaster> lstproject);


	Object countByOrdercancellNotAndApprovelstatusNotAndOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(
			int i, int j, String string, Integer sitecode, Date fromdate, Date todate,
			List<LSprojectmaster> lstproject);


	Object countByApprovelstatusAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(int i, Integer sitecode,
			Date fromdate, Date todate, List<LSprojectmaster> lstproject);


	Object countByOrdercancellNotAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(int i, Integer sitecode,
			Date fromdate, Date todate, List<LSprojectmaster> lstproject);


	Object countByOrdercancellIsNullAndOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndApprovelstatusNotOrApprovelstatusIsNull(
			int i, String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			int j);

	Object countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndApprovelstatusNotOrApprovelstatusIsNullAndOrdercancellIsNull(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, int i);


	List<Logilabprotocolorders> findByApprovelstatusAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(int i,
			Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject);


//	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(int i,
//			Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			Long directorycode, int i, Date fromdate, Date todate, Long directorycode2, int j, LSuserMaster createdby,
			Date fromdate2, Date todate2);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			Long directorycode, int i, Date fromdate, Date todate, Long directorycode2, int j, LSuserMaster createdby,
			Date fromdate2, Date todate2, Long directorycode3, int k, Date fromdate3, Date todate3,
			Integer[] lstuserMaster);



	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			Long directorycode, int i, Integer protocoltype, String orderflag, int j, Date fromdate, Date todate,
			Long directorycode2, int k, Integer protocoltype2, String orderflag2, int l, LSuserMaster createdby,
			Date fromdate2, Date todate2, Long directorycode3, int m, Integer protocoltype3, String orderflag3, int n,
			Date fromdate3, Date todate3, Integer[] lstuserMaster);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			Long directorycode, int i, Integer protocoltype, String orderflag, Date fromdate, Date todate,
			Long directorycode2, int j, Integer protocoltype2, String orderflag2, LSuserMaster createdby,
			Date fromdate2, Date todate2, Long directorycode3, int k, Integer protocoltype3, String orderflag3,
			Date fromdate3, Date todate3, Integer[] lstuserMaster);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndOrderflagAndRejectedAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndOrderflagAndRejectedAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndOrderflagAndRejectedAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			Long directorycode, int i, String orderflag, int j, Date fromdate, Date todate, Long directorycode2, int k,
			String orderflag2, int l, LSuserMaster createdby, Date fromdate2, Date todate2, Long directorycode3, int m,
			String orderflag3, int n, Date fromdate3, Date todate3, Integer[] lstuserMaster);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndOrderflagAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndOrderflagAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			Long directorycode, int i, String orderflag, Date fromdate, Date todate, Long directorycode2, int j,
			String orderflag2, LSuserMaster createdby, Date fromdate2, Date todate2, Long directorycode3, int k,
			String orderflag3, Date fromdate3, Date todate3, Integer[] lstuserMaster);



//	Object countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndApprovelstatusNotAndOrdercancellIsNull(
//			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, int i);


	List<LSlogilabprotocoldetail>  countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNull(String string,
			Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, int i);
	
	Object countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNull(String string,
			Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject);


//	List<Logilabprotocolorders> findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(int i,
//			Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject);



	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNull(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject);


	List<Logilabprotocolorders> findBySitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(Integer sitecode,
			Date fromdate, Date todate, List<LSprojectmaster> lstproject, Pageable pageable);

	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInOrRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			String string2, Integer sitecode2, Date fromdate2, Date todate2, List<LSprojectmaster> lstproject2, int i,
			Integer sitecode3, Date fromdate3, Date todate3, List<LSprojectmaster> lstproject3, int j,
			Integer sitecode4, Date fromdate4, Date todate4, List<LSprojectmaster> lstproject4);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndRejectedIsNull(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject);



//	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndRejectedIsNullOrRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(
//			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
//			String string2, Integer sitecode2, Date fromdate2, Date todate2, List<LSprojectmaster> lstproject2, int i,
//			Integer sitecode3, Date fromdate3, Date todate3, List<LSprojectmaster> lstproject3, int j,
//			Integer sitecode4, Date fromdate4, Date todate4, List<LSprojectmaster> lstproject4);


//	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndRejectedIsNullOrRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrderByProtocolordercodeDesc(
//			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
//			String string2, Integer sitecode2, Date fromdate2, Date todate2, List<LSprojectmaster> lstproject2, int i,
//			Integer sitecode3, Date fromdate3, Date todate3, List<LSprojectmaster> lstproject3, int j,
//			Integer sitecode4, Date fromdate4, Date todate4, List<LSprojectmaster> lstproject4, Pageable pageable);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndRejectedIsNullOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			Pageable pageable);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			Pageable pageable);



	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullOrRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter,
			String string2, Integer sitecode2, Date fromdate2, Date todate2, LSprojectmaster lstprojectforfilter2,
			int i, Integer sitecode3, Date fromdate3, Date todate3, LSprojectmaster lstprojectforfilter3, int j,
			Integer sitecode4, Date fromdate4, Date todate4, LSprojectmaster lstprojectforfilter4, Pageable pageable);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullOrRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmaster(
			String string, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter,
			String string2, Integer sitecode2, Date fromdate2, Date todate2, LSprojectmaster lstprojectforfilter2,
			int i, Integer sitecode3, Date fromdate3, Date todate3, LSprojectmaster lstprojectforfilter3, int j,
			Integer sitecode4, Date fromdate4, Date todate4, LSprojectmaster lstprojectforfilter4);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndTestcodeOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullAndTestcodeOrRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter,
			Integer testcode, String string2, Integer sitecode2, Date fromdate2, Date todate2,
			LSprojectmaster lstprojectforfilter2, Integer testcode2, int i, Integer sitecode3, Date fromdate3,
			Date todate3, LSprojectmaster lstprojectforfilter3, Integer testcode3, int j, Integer sitecode4,
			Date fromdate4, Date todate4, LSprojectmaster lstprojectforfilter4, Integer testcode4, Pageable pageable);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndTestcodeOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullAndTestcodeOrRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(
			String string, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter,
			Integer testcode, String string2, Integer sitecode2, Date fromdate2, Date todate2,
			LSprojectmaster lstprojectforfilter2, Integer testcode2, int i, Integer sitecode3, Date fromdate3,
			Date todate3, LSprojectmaster lstprojectforfilter3, Integer testcode3, int j, Integer sitecode4,
			Date fromdate4, Date todate4, LSprojectmaster lstprojectforfilter4, Integer testcode4);




	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter,
			Pageable pageable);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNull(
			String string, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullAndTestcodeOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter,
			Integer testcode, Pageable pageable);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullAndTestcode(
			String string, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter,
			Integer testcode);




	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndTestcodeOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter,
			Integer testcode, Pageable pageable);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndTestcode(
			String string, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter,
			Integer testcode);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter,
			Pageable pageable);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNull(
			String string, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter);



	List<Logilabprotocolorders> findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter,
			Pageable pageable);


	long countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmaster(int i, Integer sitecode, Date fromdate,
			Date todate, LSprojectmaster lstprojectforfilter);


	List<Logilabprotocolorders> findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter, Integer testcode,
			Pageable pageable);


	long countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(int i, Integer sitecode,
			Date fromdate, Date todate, LSprojectmaster lstprojectforfilter, Integer testcode);



	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter,
			Pageable pageable);


	long countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmaster(int i, Integer sitecode,
			Date fromdate, Date todate, LSprojectmaster lstprojectforfilter);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter, Integer testcode,
			Pageable pageable);


	long countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(int i, Integer sitecode,
			Date fromdate, Date todate, LSprojectmaster lstprojectforfilter, Integer testcode);


	long countBySitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcode(Integer sitecode, Date fromdate,
			Date todate, List<LSprojectmaster> lstproject, Integer testcode);


	long countBySitecodeAndCreatedtimestampBetweenAndLsprojectmaster(Integer sitecode, Date fromdate, Date todate,
			LSprojectmaster lstprojectforfilter);


	long countBySitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(Integer sitecode, Date fromdate,
			Date todate, LSprojectmaster lstprojectforfilter, Integer testcode);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndTestcodeAndRejectedIsNull(
			String string, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter,
			Integer testcode);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			Long directorycode, int i, Date fromdate, Date todate, Long directorycode2, int j, LSuserMaster createdby,
			Date fromdate2, Date todate2, Long directorycode3, int k, LSuserMaster createdby2, Date fromdate3,
			Date todate3);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			Long directorycode, int i, Integer protocoltype, String orderflag, int j, Date fromdate, Date todate,
			Long directorycode2, int k, Integer protocoltype2, String orderflag2, int l, LSuserMaster createdby,
			Date fromdate2, Date todate2, Long directorycode3, int m, Integer protocoltype3, String orderflag3, int n,
			LSuserMaster createdby2, Date fromdate3, Date todate3);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			Long directorycode, int i, Integer protocoltype, String orderflag, Date fromdate, Date todate,
			Long directorycode2, int j, Integer protocoltype2, String orderflag2, LSuserMaster createdby,
			Date fromdate2, Date todate2, Long directorycode3, int k, Integer protocoltype3, String orderflag3,
			LSuserMaster createdby2, Date fromdate3, Date todate3);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndOrderflagAndRejectedAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndOrderflagAndRejectedAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndOrderflagAndRejectedAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			Long directorycode, int i, String orderflag, int j, Date fromdate, Date todate, Long directorycode2, int k,
			String orderflag2, int l, LSuserMaster createdby, Date fromdate2, Date todate2, Long directorycode3, int m,
			String orderflag3, int n, LSuserMaster createdby2, Date fromdate3, Date todate3);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndOrderflagAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			Long directorycode, int i, String orderflag, Date fromdate, Date todate, Long directorycode2, int j,
			String orderflag2, LSuserMaster createdby, Date fromdate2, Date todate2, Long directorycode3, int k,
			String orderflag3, LSuserMaster createdby2, Date fromdate3, Date todate3);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndProtocoltypeAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			Long directorycode, int i, Integer protocoltype, Date fromdate, Date todate, Long directorycode2, int j,
			Integer protocoltype2, LSuserMaster createdby, Date fromdate2, Date todate2, Long directorycode3, int k,
			Integer protocoltype3, LSuserMaster createdby2, Date fromdate3, Date todate3);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndProtocoltypeAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndProtocoltypeAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			Long directorycode, int i, Integer protocoltype, Date fromdate, Date todate, Long directorycode2, int j,
			Integer protocoltype2, LSuserMaster createdby, Date fromdate2, Date todate2, Long directorycode3, int k,
			Integer protocoltype3, Date fromdate3, Date todate3, Integer[] lstuserMaster);



	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullAndTestcodeOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			Integer testcode, String string2, Integer sitecode2, int i, Date fromdate2, Date todate2, Integer testcode2,
			String string3, Integer sitecode3, int j, Date fromdate3, Integer usercode, Date todate3, Integer testcode3,
			String string4, Integer sitecode4, List<LSprojectmaster> lstproject2, int k, Date fromdate4, Date todate4,
			Integer testcode4, Pageable pageable);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullAndTestcodeOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			Integer testcode, String string2, Integer sitecode2, int i, Date fromdate2, Date todate2, Integer testcode2,
			String string3, Integer sitecode3, int j, Date fromdate3, Integer usercode, Date todate3, Integer testcode3,
			String string4, Integer sitecode4, List<LSprojectmaster> lstproject2, int k, Date fromdate4, Date todate4,
			Integer testcode4);


	List<Logilabprotocolorders> findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, int j,
			Integer sitecode2, int k, Date fromdate2, Date todate2, int l, Integer sitecode3, int m, Integer usercode,
			Date fromdate3, Date todate3, int n, Integer sitecode4, List<LSprojectmaster> lstproject2, int o,
			Date fromdate4, Date todate4, Pageable pageable);




	List<Logilabprotocolorders> findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, Integer testcode,
			int j, Integer sitecode2, int k, Date fromdate2, Date todate2, Integer testcode2, int l, Integer sitecode3,
			int m, Integer usercode, Date fromdate3, Date todate3, Integer testcode3, int n, Integer sitecode4,
			List<LSprojectmaster> lstproject2, int o, Date fromdate4, Date todate4, Integer testcode4,
			Pageable pageable);


	long countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, Integer testcode,
			int j, Integer sitecode2, int k, Date fromdate2, Date todate2, Integer testcode2, int l, Integer sitecode3,
			int m, Integer usercode, Date fromdate3, Date todate3, Integer testcode3, int n, Integer sitecode4,
			List<LSprojectmaster> lstproject2, int o, Date fromdate4, Date todate4, Integer testcode4);


	Object countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(int i, Integer sitecode,
			Date fromdate, Date todate, List<LSprojectmaster> lstproject);


	Object countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullAndTestcode(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			Integer testcode);


	Object countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndTestcodeAndRejectedIsNull(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			Integer testcode);


	Object countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcode(int i, Integer sitecode,
			Date fromdate, Date todate, List<LSprojectmaster> lstproject, Integer testcode);




	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, Integer testcode,
			int j, Integer sitecode2, int k, Date fromdate2, Date todate2, Integer testcode2, int l, Integer sitecode3,
			int m, Integer usercode, Date fromdate3, Date todate3, Integer testcode3, int n, Integer sitecode4,
			List<LSprojectmaster> lstproject2, int o, Date fromdate4, Date todate4, Integer testcode4,
			Pageable pageable);


	long countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, Integer testcode,
			int j, Integer sitecode2, int k, Date fromdate2, Date todate2, Integer testcode2, int l, Integer sitecode3,
			int m, Integer usercode, Date fromdate3, Date todate3, Integer testcode3, int n, Integer sitecode4,
			List<LSprojectmaster> lstproject2, int o, Date fromdate4, Date todate4, Integer testcode4);


	Object countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(int i, Integer sitecode,
			Date fromdate, Date todate, List<LSprojectmaster> lstproject);


	Object countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcode(int i,
			Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, Integer testcode);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			String string2, Integer sitecode2, int i, Date fromdate2, Date todate2, String string3, Integer sitecode3,
			int j, Integer usercode, Date fromdate3, Date todate3, String string4, Integer sitecode4,
			List<LSprojectmaster> lstproject2, int k, Date fromdate4, Date todate4, Pageable pageable);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			String string2, Integer sitecode2, int i, Date fromdate2, Date todate2, String string3, Integer sitecode3,
			int j, Integer usercode, Date fromdate3, Date todate3, String string4, Integer sitecode4,
			List<LSprojectmaster> lstproject2, int k, Date fromdate4, Date todate4);




	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			Integer testcode, String string2, Integer sitecode2, int i, Date fromdate2, Date todate2, Integer testcode2,
			String string3, Integer sitecode3, int j, Integer usercode, Date fromdate3, Date todate3, Integer testcode3,
			String string4, Integer sitecode4, List<LSprojectmaster> lstproject2, int k, Date fromdate4, Date todate4,
			Integer testcode4, Pageable pageable);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			Integer testcode, String string2, Integer sitecode2, int i, Date fromdate2, Date todate2, Integer testcode2,
			String string3, Integer sitecode3, int j, Integer usercode, Date fromdate3, Date todate3, Integer testcode3,
			String string4, Integer sitecode4, List<LSprojectmaster> lstproject2, int k, Date fromdate4, Date todate4,
			Integer testcode4);


//	List<Logilabprotocolorders> findByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
//			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer sitecode, int i, Integer sitecode2,
//			Date fromdate2, Date todate2, int j, Integer sitecode3, Integer usercode, Date fromdate3, Date todate3,
//			List<LSprojectmaster> lstproject2, int k, Integer sitecode4, Date fromdate4, Date todate4,
//			Pageable pageable);


//	long countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
//			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer sitecode, int i, Integer sitecode2,
//			Date fromdate2, Date todate2, int j, Integer sitecode3, Integer usercode, Date fromdate3, Date todate3,
//			List<LSprojectmaster> lstproject2, int k, Integer sitecode4, Date fromdate4, Date todate4
//			);


	List<Logilabprotocolorders> findByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer sitecode, Integer testcode, int i,
			Integer sitecode2, Date fromdate2, Date todate2, Integer testcode2, int j, Integer sitecode3,
			Integer usercode, Date fromdate3, Date todate3, Integer testcode3, List<LSprojectmaster> lstproject2, int k,
			Integer sitecode4, Date fromdate4, Date todate4, Integer testcode4, Pageable pageable);


	long countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer sitecode, Integer testcode, int i,
			Integer sitecode2, Date fromdate2, Date todate2, Integer testcode2, int j, Integer sitecode3,
			Integer usercode, Date fromdate3, Date todate3, Integer testcode3, List<LSprojectmaster> lstproject2, int k,
			Integer sitecode4, Date fromdate4, Date todate4, Integer testcode4);

	List<Logilabprotocolorders> findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, int j, Integer usercode, Date fromdate2, Date todate2,
			int k, Integer usercode2, Date fromdate3, Date todate3, Pageable pageable);


	long countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, int j, Integer usercode, Date fromdate2, Date todate2,
			int k, Integer usercode2, Date fromdate3, Date todate3);


	List<Logilabprotocolorders> findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, Integer testcode, int j, Integer usercode,
			Date fromdate2, Date todate2, Integer testcode2, int k, Integer usercode2, Date fromdate3, Date todate3,
			Integer testcode3, Pageable pageable);


	long countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, Integer testcode, int j, Integer usercode,
			Date fromdate2, Date todate2, Integer testcode2, int k, Integer usercode2, Date fromdate3, Date todate3,
			Integer testcode3);


	List<Logilabprotocolorders> findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, String string, int j, Integer usercode, Date fromdate2,
			Date todate2, String string2, int k, Integer usercode2, Date fromdate3, Date todate3, String string3,
			Pageable pageable);


	long countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, String string, int j, Integer usercode, Date fromdate2,
			Date todate2, String string2, int k, Integer usercode2, Date fromdate3, Date todate3, String string3);


	List<Logilabprotocolorders> findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, String string, Integer testcode, int j,
			Integer usercode, Date fromdate2, Date todate2, String string2, Integer testcode2, int k, Integer usercode2,
			Date fromdate3, Date todate3, String string3, Integer testcode3, Pageable pageable);


	long countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, String string, Integer testcode, int j,
			Integer usercode, Date fromdate2, Date todate2, String string2, Integer testcode2, int k, Integer usercode2,
			Date fromdate3, Date todate3, String string3, Integer testcode3);


	List<Logilabprotocolorders> findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, String string, int j, Integer usercode, Date fromdate2,
			Date todate2, String string2, int k, Integer usercode2, Date fromdate3, Date todate3, String string3,
			Pageable pageable);


	long countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, String string, int j, Integer usercode, Date fromdate2,
			Date todate2, String string2, int k, Integer usercode2, Date fromdate3, Date todate3, String string3);


	List<Logilabprotocolorders> findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, String string, Integer testcode, int j,
			Integer usercode, Date fromdate2, Date todate2, String string2, Integer testcode2, int k, Integer usercode2,
			Date fromdate3, Date todate3, String string3, Integer testcode3, Pageable pageable);


	long countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, String string, Integer testcode, int j,
			Integer usercode, Date fromdate2, Date todate2, String string2, Integer testcode2, int k, Integer usercode2,
			Date fromdate3, Date todate3, String string3, Integer testcode3);



	List<Logilabprotocolorders> findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, int j, int k, Integer usercode, Date fromdate2,
			Date todate2, int l, int m, Integer usercode2, Date fromdate3, Date todate3, int n, Pageable pageable);


	long countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, int j, int k, Integer usercode, Date fromdate2,
			Date todate2, int l, int m, Integer usercode2, Date fromdate3, Date todate3, int n);


	List<Logilabprotocolorders> findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, int j, Integer testcode, int k, Integer usercode,
			Date fromdate2, Date todate2, int l, Integer testcode2, int m, Integer usercode2, Date fromdate3,
			Date todate3, int n, Integer testcode3, Pageable pageable);


	long countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, int j, Integer testcode, int k, Integer usercode,
			Date fromdate2, Date todate2, int l, Integer testcode2, int m, Integer usercode2, Date fromdate3,
			Date todate3, int n, Integer testcode3);


	List<Logilabprotocolorders> findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndRejectedOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, int j, int k, Integer usercode, Date fromdate2,
			Date todate2, int l, int m, Integer usercode2, Date fromdate3, Date todate3, int n, Pageable pageable);


	long countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndRejectedOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, int j, int k, Integer usercode, Date fromdate2,
			Date todate2, int l, int m, Integer usercode2, Date fromdate3, Date todate3, int n);


	List<Logilabprotocolorders> findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndRejectedAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, int j, Integer testcode, int k, Integer usercode,
			Date fromdate2, Date todate2, int l, Integer testcode2, int m, Integer usercode2, Date fromdate3,
			Date todate3, int n, Integer testcode3, Pageable pageable);


	long countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndRejectedAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, int j, Integer testcode, int k, Integer usercode,
			Date fromdate2, Date todate2, int l, Integer testcode2, int m, Integer usercode2, Date fromdate3,
			Date todate3, int n, Integer testcode3);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			String string2, Integer sitecode2, int i, Date fromdate2, Date todate2, String string3, Integer sitecode3,
			int j, Integer usercode, Date fromdate3, Date todate3, String string4, Integer sitecode4,
			List<LSprojectmaster> lstproject2, int k, Date fromdate4, Date todate4);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			Integer testcode, String string2, Integer sitecode2, int i, Date fromdate2, Date todate2, Integer testcode2,
			String string3, Integer sitecode3, int j, Date fromdate3, Integer usercode, Date todate3, Integer testcode3,
			String string4, Integer sitecode4, List<LSprojectmaster> lstproject2, int k, Date fromdate4, Date todate4,
			Integer testcode4, Pageable pageable);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			Integer testcode, String string2, Integer sitecode2, int i, Date fromdate2, Date todate2, Integer testcode2,
			String string3, Integer sitecode3, int j, Date fromdate3, Integer usercode, Date todate3, Integer testcode3,
			String string4, Integer sitecode4, List<LSprojectmaster> lstproject2, int k, Date fromdate4, Date todate4,
			Integer testcode4);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndRejectedIsNullOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter,
			Pageable pageable);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndRejectedIsNull(
			String string, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndTestcodeAndRejectedIsNull(
			String string, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter,
			Integer testcode);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter,
			Integer testcode, Pageable pageable);




	long countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndRejectedIsNullOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, String string, int j, Integer usercode, Date fromdate2,
			Date todate2, String string2, int k, Integer usercode2, Date fromdate3, Date todate3, String string3);


	long countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, String string, Integer testcode, int j,
			Integer usercode, Date fromdate2, Date todate2, String string2, Integer testcode2, int k, Integer usercode2,
			Date fromdate3, Date todate3, String string3, Integer testcode3);


	List<Logilabprotocolorders> findByOrderflagAndLsprojectmasterInAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLssamplemasterInAndViewoptionOrOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLssamplemasterInAndViewoptionAndCreatebyOrOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLssamplemasterInAndViewoptionAndCreatebyInOrderByProtocolordercodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Integer protocoltype, Date fromdate, Date todate,
			String orderflag2, Integer protocoltype2, Date fromdate2, Date todate2, List<LSsamplemaster> lstsample,
			int i, String orderflag3, Integer protocoltype3, Date fromdate3, Date todate3,
			List<LSsamplemaster> lstsample2, int j, Integer usercode, String orderflag4, Integer protocoltype4,
			Date fromdate4, Date todate4, List<LSsamplemaster> lstsample3, int k, List<Integer> userlist);


	List<Logilabprotocolorders> findByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer sitecode, int i, Integer sitecode2,
			Date fromdate2, Date todate2, int j, Integer sitecode3, Integer usercode, Date fromdate3, Date todate3,
			List<LSprojectmaster> lstproject2, int k, Integer sitecode4, Date fromdate4, Date todate4, int l,
			Integer sitecode5, Date fromdate5, Date todate5, List<Integer> userlist, Pageable pageable);


	long countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer sitecode, int i, Integer sitecode2,
			Date fromdate2, Date todate2, int j, Integer sitecode3, Integer usercode, Date fromdate3, Date todate3,
			List<LSprojectmaster> lstproject2, int k, Integer sitecode4, Date fromdate4, Date todate4, int l,
			Integer sitecode5, Date fromdate5, Date todate5, List<Integer> userlist);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyInOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			String string2, Integer sitecode2, int i, Date fromdate2, Date todate2, String string3, Integer sitecode3,
			int j, Integer usercode, Date fromdate3, Date todate3, String string4, Integer sitecode4, int k,
			Date fromdate4, Date todate4, List<Integer> userlist);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			String string2, Integer sitecode2, int i, Date fromdate2, Date todate2, String string3, Integer sitecode3,
			int j, Integer usercode, Date fromdate3, Date todate3, String string4, Integer sitecode4, int k,
			Date fromdate4, Date todate4, List<Integer> userlist);


	long countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, int j,
			Integer sitecode2, int k, Date fromdate2, Date todate2, int l, Integer sitecode3, int m, Integer usercode,
			Date fromdate3, Date todate3, int n, Integer sitecode4, int o, Date fromdate4, Date todate4,
			List<Integer> userlist);


	long countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, int j,
			Integer sitecode2, int k, Date fromdate2, Date todate2, int l, Integer sitecode3, int m, Integer usercode,
			Date fromdate3, Date todate3, int n, Integer sitecode4, int o, Date fromdate4, Date todate4,
			List<Integer> userlist);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			String string2, Integer sitecode2, int i, Date fromdate2, Date todate2, String string3, Integer sitecode3,
			int j, Integer usercode, Date fromdate3, Date todate3, String string4, Integer sitecode4, int k,
			Date fromdate4, Date todate4, List<Integer> userlist, Pageable pageable);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyInOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			String string2, Integer sitecode2, int i, Date fromdate2, Date todate2, String string3, Integer sitecode3,
			int j, Integer usercode, Date fromdate3, Date todate3, String string4, Integer sitecode4, int k,
			Date fromdate4, Date todate4, List<Integer> userlist, Pageable pageable);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			String string2, Integer sitecode2, int i, Date fromdate2, Date todate2, String string3, Integer sitecode3,
			int j, Integer usercode, Date fromdate3, Date todate3, String string4, Integer sitecode4, int k,
			Date fromdate4, Date todate4, List<Integer> userlist);


	List<Logilabprotocolorders> findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, int j,
			Integer sitecode2, int k, Date fromdate2, Date todate2, int l, Integer sitecode3, int m, Integer usercode,
			Date fromdate3, Date todate3, int n, Integer sitecode4, int o, Date fromdate4, Date todate4,
			List<Integer> userlist, Pageable pageable);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, int j,
			Integer sitecode2, int k, Date fromdate2, Date todate2, int l, Integer sitecode3, int m, Integer usercode,
			Date fromdate3, Date todate3, int n, Integer sitecode4, int o, Date fromdate4, Date todate4,
			List<Integer> userlist, Pageable pageable);


	List<Logilabprotocolorders> findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndCreatedtimestampBetweenOrLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSprojectmaster lsprojectmaster, Integer testcode, int i, Date fromdate, Date todate,
			LSprojectmaster lsprojectmaster2, Integer testcode2, int j, Date fromdate2, Date todate2);


	List<LSlogilabprotocoldetail> findByLssamplemasterAndViewoptionAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSsamplemaster lssamplemaster, int i, Integer testcode, int j, Date fromdate, Date todate,
			LSsamplemaster lssamplemaster2, int k, Integer testcode2, int l, Date fromdate2, Date todate2,
			LSsamplemaster lssamplemaster3, int m, LSuserMaster lsuserMaster, Integer testcode3, int n, Date fromdate3,
			Date todate3);

	List<Logilabprotocolorders> findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, String searchkeywords, String searchkeywords2, int j,
			Integer sitecode2, Date fromdate2, Date todate2, String searchkeywords3, String searchkeywords4, int k,
			Integer usercode, Date fromdate3, Date todate3, String searchkeywords5, String searchkeywords6, int l,
			Integer usercode2, Date fromdate4, Date todate4, String searchkeywords7, String searchkeywords8, int m,
			Integer usercode3, Date fromdate5, Date todate5, String searchkeywords9, String searchkeywords10, int n,
			Integer usercode4, Date fromdate6, Date todate6, String searchkeywords11, String searchkeywords12,
			Pageable pageableprotocol);


	long countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, String searchkeywords, String searchkeywords2, int j,
			Integer sitecode2, Date fromdate2, Date todate2, String searchkeywords3, String searchkeywords4, int k,
			Integer usercode, Date fromdate3, Date todate3, String searchkeywords5, String searchkeywords6, int l,
			Integer usercode2, Date fromdate4, Date todate4, String searchkeywords7, String searchkeywords8, int m,
			Integer usercode3, Date fromdate5, Date todate5, String searchkeywords9, String searchkeywords10, int n,
			Integer usercode4, Date fromdate6, Date todate6, String searchkeywords11, String searchkeywords12);


	long countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer sitecode, String searchkeywords,
			String searchkeywords2, List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2, Integer sitecode2,
			String searchkeywords3, String searchkeywords4, int i, Integer sitecode3, Date fromdate3, Date todate3,
			String searchkeywords5, String searchkeywords6, int j, Integer sitecode4, Date fromdate4, Date todate4,
			String searchkeywords7, String searchkeywords8, int k, Integer sitecode5, Integer usercode, Date fromdate5,
			Date todate5, String searchkeywords9, String searchkeywords10, int l, Integer sitecode6, Integer usercode2,
			Date fromdate6, Date todate6, String searchkeywords11, String searchkeywords12,
			List<LSprojectmaster> lstproject3, int m, Integer sitecode7, Date fromdate7, Date todate7,
			String searchkeywords13, String searchkeywords14, List<LSprojectmaster> lstproject4, int n,
			Integer sitecode8, Date fromdate8, Date todate8, String searchkeywords15, String searchkeywords16, int o,
			Integer sitecode9, Date fromdate9, Date todate9, List<Integer> userlist, String searchkeywords17,
			String searchkeywords18, int p, Integer sitecode10, Date fromdate10, Date todate10, List<Integer> userlist2,
			String searchkeywords19, String searchkeywords20);


	List<Logilabprotocolorders> findByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer sitecode, String searchkeywords,
			String searchkeywords2, List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2, Integer sitecode2,
			String searchkeywords3, String searchkeywords4, int i, Integer sitecode3, Date fromdate3, Date todate3,
			String searchkeywords5, String searchkeywords6, int j, Integer sitecode4, Date fromdate4, Date todate4,
			String searchkeywords7, String searchkeywords8, int k, Integer sitecode5, Integer usercode, Date fromdate5,
			Date todate5, String searchkeywords9, String searchkeywords10, int l, Integer sitecode6, Integer usercode2,
			Date fromdate6, Date todate6, String searchkeywords11, String searchkeywords12,
			List<LSprojectmaster> lstproject3, int m, Integer sitecode7, Date fromdate7, Date todate7,
			String searchkeywords13, String searchkeywords14, List<LSprojectmaster> lstproject4, int n,
			Integer sitecode8, Date fromdate8, Date todate8, String searchkeywords15, String searchkeywords16, int o,
			Integer sitecode9, Date fromdate9, Date todate9, List<Integer> userlist, String searchkeywords17,
			String searchkeywords18, int p, Integer sitecode10, Date fromdate10, Date todate10, List<Integer> userlist2,
			String searchkeywords19, String searchkeywords20, Pageable pageable);


	List<Logilabprotocolorders> findByOrderflagAndLsprojectmasterInAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLssamplemasterInAndViewoptionOrOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLssamplemasterInAndViewoptionAndCreateby(
			String orderflag, List<LSprojectmaster> lstproject, Integer protocoltype, Date fromdate, Date todate,
			String orderflag2, Integer protocoltype2, Date fromdate2, Date todate2, List<LSsamplemaster> lstsample,
			int i, String orderflag3, Integer protocoltype3, Date fromdate3, Date todate3,
			List<LSsamplemaster> lstsample2, int j, Integer usercode);


	List<Logilabprotocolorders> findByOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLssamplemasterInAndViewoptionAndCreatebyInOrderByProtocolordercodeDesc(
			String orderflag, Integer protocoltype, Date fromdate, Date todate, List<LSsamplemaster> lstsample, int i,
			List<Integer> userlist);


	List<Logilabprotocolorders>  findByOrderflagAndLsprojectmasterInAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNull(
			String orderflag, List<LSprojectmaster> lstproject, Integer protocoltype, Date fromdate, Date todate);


	List<Logilabprotocolorders>  findByOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLssamplemasterInAndViewoption(
			String orderflag, Integer protocoltype, Date fromdate, Date todate, List<LSsamplemaster> lstsample, int i);


	List<Logilabprotocolorders>  findByOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLssamplemasterInAndViewoptionAndCreateby(
			String orderflag, Integer protocoltype, Date fromdate, Date todate, List<LSsamplemaster> lstsample, int i,
			Integer usercode);
}

