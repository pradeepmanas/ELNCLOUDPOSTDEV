package com.agaram.eln.primary.repository.protocol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agaram.eln.primary.fetchmodel.getorders.Logilabprotocolorders;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolorderstructure;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.protocols.Elnprotocolworkflow;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail.Protocolorder;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
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

//	@Transactional
//	@Modifying
//	@Query("update LSlogilabprotocoldetail set lsworkflow = :workflow, approved= :approved , rejected= :rejected "
//			+ "where protocolordercode in (:protocolordercode)")
//	public void updateFileWorkflow(@Param("workflow") LSworkflow lSworkflow,
//			@Param("approved") Integer approved, @Param("rejected") Integer rejected,
//			@Param("protocolordercode") Long protocolordercode);
	@Transactional
	@Modifying
	@Query("update LSlogilabprotocoldetail set elnprotocolworkflow = :workflow, approved= :approved , rejected= :rejected "
			+ "where protocolordercode in (:protocolordercode)")
	public void updateFileWorkflow(@Param("workflow") Elnprotocolworkflow lSworkflow,
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

	Logilabprotocolorders findByProtocolordercodeOrderByProtocolordercodeDesc(Long protocolordercode);

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

	
//	@Transactional
//	@Modifying
//	@Query(value = "select distinct LSlogilabprotocoldetail.testcode, LSlogilabprotocoldetail.lsprojectmaster_projectcode,  (select testname from lstestmasterlocal where testcode = LSlogilabprotocoldetail.testcode) as testname  from LSlogilabprotocoldetail as LSlogilabprotocoldetail"
//			+ " where LSlogilabprotocoldetail.testcode is not null and assignedto_usercode is null and LSlogilabprotocoldetail.lsprojectmaster_projectcode is not null and LSlogilabprotocoldetail.lsprojectmaster_projectcode in (?1)", nativeQuery = true)
//	public ArrayList<List<Object>>  getLstestmasterlocalByOrderdisplaytypeAndLsprojectmasterInAndTestcodeIsNotNull(List<Integer> lsprojectcode);

	@Transactional
	@Modifying
	@Query(value = "SELECT DISTINCT l.testcode, l.lsprojectmaster_projectcode, CAST(t.testname AS varchar(250)) AS testname " +
	        "FROM LSlogilabprotocoldetail l " +
	        "JOIN lstestmasterlocal t ON t.testcode = l.testcode " +
	        "WHERE l.testcode IS NOT NULL " +
	        "AND l.assignedto_usercode IS NULL " +
	        "AND l.lsprojectmaster_projectcode IS NOT NULL " +
	        "AND l.lsprojectmaster_projectcode IN (?1)", nativeQuery = true)
	public ArrayList<List<Object>> getLstestmasterlocalByOrderdisplaytypeAndLsprojectmasterInAndTestcodeIsNotNull(List<Integer> lsprojectcode);

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


	long countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(int i, Integer sitecode,
			Date fromdate, Date todate, List<LSprojectmaster> lstproject);


	Object countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullAndTestcode(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			Integer testcode);


	Object countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndTestcodeAndRejectedIsNull(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			Integer testcode);


	long countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcode(int i, Integer sitecode,
			Date fromdate, Date todate, List<LSprojectmaster> lstproject, Integer testcode);


	long countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(int i, Integer sitecode,
			Date fromdate, Date todate, List<LSprojectmaster> lstproject);


	long countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcode(int i,
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

	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyInOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			String string2, Integer sitecode2, int i, Date fromdate2, Date todate2, String string3, Integer sitecode3,
			int j, Integer usercode, Date fromdate3, Date todate3, String string4, Integer sitecode4, int k,
			Date fromdate4, Date todate4, List<Integer> userlist);

	List<LSlogilabprotocoldetail> findByLssamplemasterAndViewoptionAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSsamplemaster lssamplemaster, int i, Integer testcode, int j, Date fromdate, Date todate,
			LSsamplemaster lssamplemaster2, int k, Integer testcode2, int l, Date fromdate2, Date todate2,
			LSsamplemaster lssamplemaster3, int m, LSuserMaster lsuserMaster, Integer testcode3, int n, Date fromdate3,
			Date todate3);


	List<Logilabprotocolorders> findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndCreatedtimestampBetweenOrLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			LSprojectmaster lsprojectmaster, Integer testcode, int i, Date fromdate, Date todate,
			LSprojectmaster lsprojectmaster2, Integer testcode2, int j, Date fromdate2, Date todate2);


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


	List<Logilabprotocolorders> findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, int j, Integer sitecode2, Integer usercode,
			Date fromdate2, Date todate2, int k, Integer sitecode3, Date fromdate3, Date todate3,
			List<Integer> userlist, Pageable pageable);


	List<Logilabprotocolorders> findByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, int i, Integer sitecode, Date fromdate, Date todate, Pageable pageable);


	List<Logilabprotocolorders> findByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer sitecode, Pageable pageable);


	long countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyIn(
			int i, Integer sitecode, Date fromdate, Date todate, int j, Integer sitecode2, Integer usercode,
			Date fromdate2, Date todate2, int k, Integer sitecode3, Date fromdate3, Date todate3,
			List<Integer> userlist);


	long countByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetween(List<LSprojectmaster> lstproject,
			int i, Integer sitecode, Date fromdate, Date todate);


	long countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecode(List<LSprojectmaster> lstproject, Date fromdate,
			Date todate, Integer sitecode);


	List<Logilabprotocolorders> findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, Integer testcode, int j, Integer sitecode2,
			Integer usercode, Date fromdate2, Date todate2, Integer testcode2, Pageable pageable);


	List<Logilabprotocolorders> findByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndTestcodeOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer sitecode, Integer testcode,
			Pageable pageable);


	List<Logilabprotocolorders> findByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, int i, Integer sitecode, Date fromdate, Date todate, Integer testcode,
			Pageable pageable);


	long countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndTestcode(
			int i, Integer sitecode, Date fromdate, Date todate, Integer testcode, int j, Integer sitecode2,
			Integer usercode, Date fromdate2, Date todate2, Integer testcode2);


	long countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndTestcode(List<LSprojectmaster> lstproject,
			Date fromdate, Date todate, Integer sitecode, Integer testcode);


	long countByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcode(
			List<LSprojectmaster> lstproject, int i, Integer sitecode, Date fromdate, Date todate, Integer testcode);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			String string, Integer sitecode, int i, Date fromdate, Date todate, String string2, Integer sitecode2,
			int j, Integer usercode, Date fromdate2, Date todate2, String string3, Integer sitecode3, int k,
			Date fromdate3, Date todate3, List<Integer> userlist, Pageable pageable);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			Pageable pageable);


	long countByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyIn(
			String string, Integer sitecode, int i, Date fromdate, Date todate, String string2, Integer sitecode2,
			int j, Integer usercode, Date fromdate2, Date todate2, String string3, Integer sitecode3, int k,
			Date fromdate3, Date todate3, List<Integer> userlist);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNull(String string,
			Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			String string, Integer sitecode, int i, Date fromdate, Date todate, Integer testcode, String string2,
			Integer sitecode2, int j, Integer usercode, Date fromdate2, Date todate2, Integer testcode2,
			Pageable pageable);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullAndTestcodeOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			Integer testcode, Pageable pageable);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			String string, Integer sitecode, List<LSprojectmaster> lstproject, int i, Date fromdate, Date todate,
			Integer testcode, Pageable pageable);


	long countByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcode(
			String string, Integer sitecode, int i, Date fromdate, Date todate, Integer testcode, String string2,
			Integer sitecode2, int j, Integer usercode, Date fromdate2, Date todate2, Integer testcode2);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullAndTestcode(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			Integer testcode);


	long countByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcode(
			String string, Integer sitecode, List<LSprojectmaster> lstproject, int i, Date fromdate, Date todate,
			Integer testcode);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyInOrderByProtocolordercodeDesc(
			String string, Integer sitecode, int i, Date fromdate, Date todate, String string2, Integer sitecode2,
			int j, Integer usercode, Date fromdate2, Date todate2, String string3, Integer sitecode3, int k,
			Date fromdate3, Date todate3, List<Integer> userlist, Pageable pageable);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			Pageable pageable);


	long countByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyIn(
			String string, Integer sitecode, int i, Date fromdate, Date todate, String string2, Integer sitecode2,
			int j, Integer usercode, Date fromdate2, Date todate2, String string3, Integer sitecode3, int k,
			Date fromdate3, Date todate3, List<Integer> userlist);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
			String string, Integer sitecode, int i, Date fromdate, Date todate, Integer testcode, String string2,
			Integer sitecode2, int j, Date fromdate2, Integer usercode, Date todate2, Integer testcode2,
			Pageable pageable);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			Integer testcode, Pageable pageable);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
			String string, Integer sitecode, List<LSprojectmaster> lstproject, int i, Date fromdate, Date todate,
			Integer testcode, Pageable pageable);


	long countByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNull(
			String string, Integer sitecode, int i, Date fromdate, Date todate, Integer testcode, String string2,
			Integer sitecode2, int j, Date fromdate2, Integer usercode, Date todate2, Integer testcode2);


	long countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullAndTestcodeAndRejectedIsNull(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			Integer testcode);


	long countByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNull(
			String string, Integer sitecode, List<LSprojectmaster> lstproject, int i, Date fromdate, Date todate,
			Integer testcode);


	List<Logilabprotocolorders> findByRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			int i, Integer sitecode, int j, Date fromdate, Date todate, int k, Integer sitecode2, int l,
			Integer usercode, Date fromdate2, Date todate2, int m, Integer sitecode3, int n, Date fromdate3,
			Date todate3, List<Integer> userlist, Pageable pageable);


	List<Logilabprotocolorders> findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, Pageable pageable);


	long countByRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyIn(
			int i, Integer sitecode, int j, Date fromdate, Date todate, int k, Integer sitecode2, int l,
			Integer usercode, Date fromdate2, Date todate2, int m, Integer sitecode3, int n, Date fromdate3,
			Date todate3, List<Integer> userlist);


	List<Logilabprotocolorders> findByRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, int j, Date fromdate, Date todate, Integer testcode, int k, Integer sitecode2,
			int l, Integer usercode, Date fromdate2, Date todate2, Integer testcode2, Pageable pageable);


	List<Logilabprotocolorders> findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, Integer testcode,
			Pageable pageable);


	List<Logilabprotocolorders> findByRejectedAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, List<LSprojectmaster> lstproject, int j, Date fromdate, Date todate,
			Integer testcode, Pageable pageable);


	long countByRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcode(
			int i, Integer sitecode, int j, Date fromdate, Date todate, Integer testcode, int k, Integer sitecode2,
			int l, Integer usercode, Date fromdate2, Date todate2, Integer testcode2);


	long countByRejectedAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcode(int i,
			Integer sitecode, List<LSprojectmaster> lstproject, int j, Date fromdate, Date todate, Integer testcode);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			int i, Integer sitecode, int j, Date fromdate, Date todate, int k, Integer sitecode2, int l,
			Integer usercode, Date fromdate2, Date todate2, int m, Integer sitecode3, int n, Date fromdate3,
			Date todate3, List<Integer> userlist, Pageable pageable);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, Pageable pageable);


	long countByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyIn(
			int i, Integer sitecode, int j, Date fromdate, Date todate, int k, Integer sitecode2, int l,
			Integer usercode, Date fromdate2, Date todate2, int m, Integer sitecode3, int n, Date fromdate3,
			Date todate3, List<Integer> userlist);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, int j, Date fromdate, Date todate, Integer testcode, int k, Integer sitecode2,
			int l, Integer usercode, Date fromdate2, Date todate2, Integer testcode2, Pageable pageable);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, Integer testcode,
			Pageable pageable);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, List<LSprojectmaster> lstproject, int j, Date fromdate, Date todate,
			Integer testcode, Pageable pageable);


	long countByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcode(
			int i, Integer sitecode, int j, Date fromdate, Date todate, Integer testcode, int k, Integer sitecode2,
			int l, Integer usercode, Date fromdate2, Date todate2, Integer testcode2);


	long countByOrdercancellAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcode(int i,
			Integer sitecode, List<LSprojectmaster> lstproject, int j, Date fromdate, Date todate, Integer testcode);


	Object countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			String string, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			String string2, Integer sitecode2, int i, Date fromdate2, Date todate2, String string3, Integer sitecode3,
			int j, Integer usercode, Date fromdate3, Date todate3, String string4, Integer sitecode4, int k,
			Date fromdate4, Date todate4, List<Integer> userlist);


	Object countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, int j,
			Integer sitecode2, int k, Date fromdate2, Date todate2, int l, Integer sitecode3, int m, Integer usercode,
			Date fromdate3, Date todate3, int n, Integer sitecode4, int o, Date fromdate4, Date todate4,
			List<Integer> userlist);


	Object countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, int j,
			Integer sitecode2, int k, Date fromdate2, Date todate2, int l, Integer sitecode3, int m, Integer usercode,
			Date fromdate3, Date todate3, int n, Integer sitecode4, int o, Date fromdate4, Date todate4,
			List<Integer> userlist);


	List<Logilabprotocolorders> findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, String searchkeywords, String searchkeywords2, int j,
			Integer sitecode2, Date fromdate2, Date todate2, String searchkeywords3, String searchkeywords4, int k,
			Integer sitecode3, Integer usercode, Date fromdate3, Date todate3, String searchkeywords5,
			String searchkeywords6, int l, Integer sitecode4, Integer usercode2, Date fromdate4, Date todate4,
			String searchkeywords7, String searchkeywords8, int m, Integer sitecode5, Date fromdate5, Date todate5,
			List<Integer> userlist, String searchkeywords9, String searchkeywords10, int n, Integer sitecode6,
			Date fromdate6, Date todate6, List<Integer> userlist2, String searchkeywords11, String searchkeywords12,
			Pageable pageable);


	List<Logilabprotocolorders> findByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer sitecode, String searchkeywords,
			String searchkeywords2, Pageable pageable);


	List<Logilabprotocolorders> findByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer sitecode, String searchkeywords,
			String searchkeywords2, Pageable pageable);


	List<Logilabprotocolorders> findByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, int i, Integer sitecode, Date fromdate, Date todate,
			String searchkeywords, String searchkeywords2, Pageable pageable);


	List<Logilabprotocolorders> findByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, int i, Integer sitecode, Date fromdate, Date todate,
			String searchkeywords, String searchkeywords2, Pageable pageable);


	long countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, String searchkeywords, String searchkeywords2, int j,
			Integer sitecode2, Date fromdate2, Date todate2, String searchkeywords3, String searchkeywords4, int k,
			Integer sitecode3, Integer usercode, Date fromdate3, Date todate3, String searchkeywords5,
			String searchkeywords6, int l, Integer sitecode4, Integer usercode2, Date fromdate4, Date todate4,
			String searchkeywords7, String searchkeywords8, int m, Integer sitecode5, Date fromdate5, Date todate5,
			List<Integer> userlist, String searchkeywords9, String searchkeywords10, int n, Integer sitecode6,
			Date fromdate6, Date todate6, List<Integer> userlist2, String searchkeywords11, String searchkeywords12);


	long countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer sitecode, String searchkeywords,
			String searchkeywords2);


	long countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer sitecode, String searchkeywords,
			String searchkeywords2);


	long countByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, int i, Integer sitecode, Date fromdate, Date todate,
			String searchkeywords, String searchkeywords2);


	long countByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, int i, Integer sitecode, Date fromdate, Date todate,
			String searchkeywords, String searchkeywords2);


//	List<Logilabprotocolorders> findByOrderflagAndLsprojectmasterInAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNull(
//			String orderflag, List<LSprojectmaster> lstproject, Integer protocoltype, Date fromdate, Date todate);



	List<LSlogilabprotocoldetail> findBylsprojectmasterAndTestcodeInAndLssamplemasterIn(LSprojectmaster lsproject,
			List<Integer> testid, List<LSsamplemaster> lssample);


	List<LSlogilabprotocoldetail> findBylsprojectmasterAndTestcodeIn(LSprojectmaster lsproject, List<Integer> testid);

	List<LSlogilabprotocoldetail> findByLsprotocolmasterInAndTestcodeIn(List<LSprotocolmaster> lstpm,
			List<Integer> testid);

	List<LSlogilabprotocoldetail> findByProtocolordercodeOrderByProtocolordercodeAsc(Long protocolordercode);


	List<LSlogilabprotocoldetail> findByLssamplemasterIn(List<LSsamplemaster> lsSampleLst);


	@Transactional
	@Modifying
	@Query(value="update LSlogilabprotocoldetail set lockeduser=null,lockedusername=null where protocolordercode in(?1)")
	void Updatelockedusersonptocolorders(List<Long> protocolorderscode);


	List<Logilabprotocolorders> findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndLockeduserIsNotNullAndOrderflagAndLockeduserNotOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndLockeduserIsNotNullAndOrderflagAndLockeduserNotOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyInAndLockeduserIsNotNullAndOrderflagAndLockeduserNotOrderByProtocolordercodeDesc(
			int i, Integer sitecode, String string, Integer usercode, int j, Integer sitecode2, Integer usercode2,
			String string2, Integer usercode3, int k, Integer sitecode3, List<Integer> userlist, String string3,
			Integer usercode4);



	
	
	@Transactional
	@Modifying
	@Query(value ="update LSlogilabprotocoldetail  set elnprotocolworkflow_workflowcode = null where elnprotocolworkflow_workflowcode = ?1", nativeQuery = true)
	void setWorkflownullforcompletedorder(Elnprotocolworkflow lsworkflow);


	long countByelnprotocolworkflowAndOrderflag(Elnprotocolworkflow objflow, String string);
	
	@Transactional
	@Modifying
	@Query(value = "select distinct LSlogilabprotocoldetail.testcode, LSlogilabprotocoldetail.elnmaterial_nmaterialcode,  CAST((select testname from lstestmasterlocal where testcode =  LSlogilabprotocoldetail.testcode) as varchar(250))as testname  from LSlogilabprotocoldetail as LSlogilabprotocoldetail"
			+ " where LSlogilabprotocoldetail.testcode is not null and LSlogilabprotocoldetail.elnmaterial_nmaterialcode is not null and LSlogilabprotocoldetail.elnmaterial_nmaterialcode in (select nmaterialcode from elnmaterial where nsitecode=?1)", nativeQuery = true)
	public List<Object> getLstestmasterlocalAndmaterialByOrderdisplaytypeAndLSsamplemasterInAndTestcodeIsNotNull(Integer sitecode);


	List<Logilabprotocolorders> findByOrderflagAndSitecodeAndAndLockeduserIsNotNullAndAssignedtoIsNullOrderByProtocolordercodeDesc(
			String string, Integer sitecode);


	List<Logilabprotocolorders> findByLsprojectmasterInAndViewoptionAndSitecodeAndLockeduserIsNotNullAndOrderflagAndLockeduserOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, int i, Integer sitecode, String string, Integer usercode);




	List<LSlogilabprotocoldetail> findByActiveuserIn(List<Integer> activeuser);


	List<Logilabprotocolorders>  findByLsprojectmasterInAndSitecodeAndLockeduserIsNotNullAndOrderflagOrderByProtocolordercodeDesc(
			List<LSprojectmaster> lstproject, Integer sitecode, String string);


	List<Protocolorder> findByOrderflagAndLsprojectmasterInAndElnprotocolworkflowInAndCreatedtimestampBetween(
			String string, List<LSprojectmaster> lstproject, List<Elnprotocolworkflow> lstworkflow_protocol,
			Date fromdate, Date todate);


	List<Logilabprotocolorders> findByLsprojectmasterInAndElnmaterialAndTestcodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndCreatebyIn(
			List<LSprojectmaster> lstproject, Elnmaterial elnmaterial, Integer testcode, Date fromdate, Date todate,
			Elnmaterial elnmaterial2, Integer testcode2, Date fromdate2, Date todate2, int i, Elnmaterial elnmaterial3,
			Integer testcode3, Date fromdate3, Date todate3, int j, LSuserMaster lsuserMaster, Elnmaterial elnmaterial4,
			Integer testcode4, Date fromdate4, Date todate4, int k, List<Integer> userlist);


	List<Logilabprotocolorders> findByLsprojectmasterInAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndProtocoltypeAndOrderflagAndRejectedOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndProtocoltypeAndOrderflagAndRejectedOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndProtocoltypeAndOrderflagAndRejectedOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndCreatebyInAndProtocoltypeAndOrderflagAndRejected(
			List<LSprojectmaster> lstproject, Elnmaterial elnmaterial, Integer testcode, Date fromdate, Date todate,
			Integer protocoltype, String orderflag, int i, Elnmaterial elnmaterial2, Integer testcode2, Date fromdate2,
			Date todate2, int j, Integer protocoltype2, String orderflag2, int k, Elnmaterial elnmaterial3,
			Integer testcode3, Date fromdate3, Date todate3, int l, Integer protocoltype3, String orderflag3, int m,
			LSuserMaster lsuserMaster, Elnmaterial elnmaterial4, Integer testcode4, Date fromdate4, Date todate4, int n,
			List<Integer> userlist, Integer protocoltype4, String orderflag4, int o);


	List<Logilabprotocolorders> findByLsprojectmasterInAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndProtocoltypeAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndProtocoltypeAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndProtocoltypeAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndCreatebyInAndProtocoltypeAndOrderflag(
			List<LSprojectmaster> lstproject, Elnmaterial elnmaterial, Integer testcode, Date fromdate, Date todate,
			Integer protocoltype, String orderflag, Elnmaterial elnmaterial2, Integer testcode2, Date fromdate2,
			Date todate2, int i, Integer protocoltype2, String orderflag2, Elnmaterial elnmaterial3, Integer testcode3,
			Date fromdate3, Date todate3, int j, Integer protocoltype3, String orderflag3, LSuserMaster lsuserMaster,
			Elnmaterial elnmaterial4, Integer testcode4, Date fromdate4, Date todate4, int k, List<Integer> userlist,
			Integer protocoltype4, String orderflag4);


	List<Logilabprotocolorders> findByLsprojectmasterInAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndOrderflagAndRejectedOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndOrderflagAndRejectedOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndOrderflagAndRejectedOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndCreatebyInAndOrderflagAndRejected(
			List<LSprojectmaster> lstproject, Elnmaterial elnmaterial, Integer testcode, Date fromdate, Date todate,
			String orderflag, int i, Elnmaterial elnmaterial2, Integer testcode2, Date fromdate2, Date todate2, int j,
			String orderflag2, int k, Elnmaterial elnmaterial3, Integer testcode3, Date fromdate3, Date todate3, int l,
			String orderflag3, int m, LSuserMaster lsuserMaster, Elnmaterial elnmaterial4, Integer testcode4,
			Date fromdate4, Date todate4, int n, List<Integer> userlist, String orderflag4, int o);


	List<Logilabprotocolorders> findByLsprojectmasterInAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndCreatebyInAndOrderflag(
			List<LSprojectmaster> lstproject, Elnmaterial elnmaterial, Integer testcode, Date fromdate, Date todate,
			String orderflag, Elnmaterial elnmaterial2, Integer testcode2, Date fromdate2, Date todate2, int i,
			String orderflag2, Elnmaterial elnmaterial3, Integer testcode3, Date fromdate3, Date todate3, int j,
			String orderflag3, LSuserMaster lsuserMaster, Elnmaterial elnmaterial4, Integer testcode4, Date fromdate4,
			Date todate4, int k, List<Integer> userlist, String orderflag4);

List<Logilabprotocolorders> findByLsprojectmasterInAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndProtocoltypeOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndProtocoltypeOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndProtocoltypeOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndCreatebyInAndProtocoltype(
			List<LSprojectmaster> lstproject, Elnmaterial elnmaterial, Integer testcode, Date fromdate, Date todate,
			Integer protocoltype, Elnmaterial elnmaterial2, Integer testcode2, Date fromdate2, Date todate2, int i,
			Integer protocoltype2, Elnmaterial elnmaterial3, Integer testcode3, Date fromdate3, Date todate3, int j,
			Integer protocoltype3, LSuserMaster lsuserMaster, Elnmaterial elnmaterial4, Integer testcode4,
			Date fromdate4, Date todate4, int k, List<Integer> userlist, Integer protocoltype4);
List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
			int i, Integer sitecode, int j, Date fromdate, Date todate, int k, Integer sitecode2, int l,
			Integer usercode, Date fromdate2, Date todate2, int m, Integer sitecode3, int n, Date fromdate3,
			Date todate3, List<Integer> userlist);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndProtocoltypeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtocoltypeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndProtocoltypeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, int j, Date fromdate, Date todate, Integer protocoltype, int k, Integer sitecode2,
			int l, Integer usercode, Date fromdate2, Date todate2, Integer protocoltype2, int m, Integer sitecode3,
			int n, Date fromdate3, Date todate3, List<Integer> userlist, Integer protocoltype3);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndProtocoltypeOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject,
			Integer protocoltype);

	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndOrderflagOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndOrderflagOrderByProtocolordercodeDesc(
			int i, Integer sitecode, int j, Date fromdate, Date todate, String orderflag, int k, Integer sitecode2,
			int l, Integer usercode, Date fromdate2, Date todate2, String orderflag2, int m, Integer sitecode3, int n,
			Date fromdate3, Date todate3, List<Integer> userlist, String orderflag3);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrderflagOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, String orderflag);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndRejectedOrderByProtocolordercodeDesc(
			int i, Integer sitecode, int j, Date fromdate, Date todate, int k, int l, Integer sitecode2, int m,
			Integer usercode, Date fromdate2, Date todate2, int n, int o, Integer sitecode3, int p, Date fromdate3,
			Date todate3, List<Integer> userlist, int q);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, int j);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndProtocoltypeAndOrderflagOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtocoltypeAndOrderflagOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndProtocoltypeAndOrderflagOrderByProtocolordercodeDesc(
			int i, Integer sitecode, int j, Date fromdate, Date todate, Integer protocoltype, String orderflag, int k,
			Integer sitecode2, int l, Integer usercode, Date fromdate2, Date todate2, Integer protocoltype2,
			String orderflag2, int m, Integer sitecode3, int n, Date fromdate3, Date todate3, List<Integer> userlist,
			Integer protocoltype3, String orderflag3);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndProtocoltypeAndOrderflagOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, Integer protocoltype,
			String orderflag);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndProtocoltypeAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtocoltypeAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndProtocoltypeAndRejectedOrderByProtocolordercodeDesc(
			int i, Integer sitecode, int j, Date fromdate, Date todate, Integer protocoltype, int k, int l,
			Integer sitecode2, int m, Integer usercode, Date fromdate2, Date todate2, Integer protocoltype2, int n,
			int o, Integer sitecode3, int p, Date fromdate3, Date todate3, List<Integer> userlist,
			Integer protocoltype3, int q);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndProtocoltypeAndRejectedOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, Integer protocoltype,
			int j);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndOrderflagAndRejectedOrderByProtocolordercodeDesc(
			int i, Integer sitecode, int j, Date fromdate, Date todate, String orderflag, int k, int l,
			Integer sitecode2, int m, Integer usercode, Date fromdate2, Date todate2, String orderflag2, int n, int o,
			Integer sitecode3, int p, Date fromdate3, Date todate3, List<Integer> userlist, String orderflag3, int q);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrderflagAndRejectedOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, String orderflag,
			int j);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndProtocoltypeAndOrderflagAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtocoltypeAndOrderflagAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndProtocoltypeAndOrderflagAndRejectedOrderByProtocolordercodeDesc(
			int i, Integer sitecode, int j, Date fromdate, Date todate, Integer protocoltype, String orderflag, int k,
			int l, Integer sitecode2, int m, Integer usercode, Date fromdate2, Date todate2, Integer protocoltype2,
			String orderflag2, int n, int o, Integer sitecode3, int p, Date fromdate3, Date todate3,
			List<Integer> userlist, Integer protocoltype3, String orderflag3, int q);


	List<Logilabprotocolorders> findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndProtocoltypeAndOrderflagAndRejectedOrderByProtocolordercodeDesc(
			int i, Integer sitecode, Date fromdate, Date todate, List<LSprojectmaster> lstproject, Integer protocoltype,
			String orderflag, int j);


	List<Protocolorder> findByOrderflagAndLsprojectmasterInAndElnprotocolworkflowInAndAssignedtoIsNullAndCreatedtimestampBetweenOrSitecodeAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
			String string, List<LSprojectmaster> lstproject, List<Elnprotocolworkflow> lstworkflow_protocol,
			Date fromdate, Date todate, Integer sitecode, LSuserMaster objuser, Date fromdate2, Date todate2);

	@Transactional
	@Query(value = "select ordercancell from LSlogilabprotocoldetail where protocolordername = ?1", nativeQuery = true)
	String getRetirestatus(String templatename);


	List<LSlogilabprotocoldetail> findByProtocolordercodeInAndOrderflag(List<Long> batchcode, String orderflag);


	List<LSlogilabprotocoldetail> findByProtoclordernameInAndOrderflag(List<String> batchid, String string);


	List<LSlogilabprotocoldetail> findByLsuserMasterAndRepeat(LSuserMaster lsuserMaster, boolean b);

	@Transactional
	List<LSlogilabprotocoldetail> findByProtocolordercodeIn(List<Long> protocolordercodeauto);


	List<Logilabprotocolorders>  findByOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInAndViewoptionAndOrdercancellIsNull(
			String orderflag, Integer protocoltype, Date fromdate, Date todate, List<Elnmaterial> currentChunk, int i);


	List<Logilabprotocolorders>  findByOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInAndViewoptionAndCreatebyAndOrdercancellIsNull(
			String orderflag, Integer protocoltype, Date fromdate, Date todate, List<Elnmaterial> currentChunk, int i,
			Integer usercode);


	List<Logilabprotocolorders>  findByOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInAndViewoptionAndCreatebyInAndOrdercancellIsNullOrderByProtocolordercodeDesc(
			String orderflag, Integer protocoltype, Date fromdate, Date todate, List<Elnmaterial> currentChunk, int i,
			List<Integer> userlist);


	List<Logilabprotocolorders> findByOrderflagAndRejectedAndLsprojectmasterInAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndRejectedAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInOrderByProtocolordercodeDesc(
			String orderflag, int i, List<LSprojectmaster> lstproject, Integer protocoltype, Date fromdate, Date todate,
			String orderflag2, int j, Integer protocoltype2, Date fromdate2, Date todate2,
			List<Elnmaterial> currentChunk);


	List<Logilabprotocolorders> findByOrderflagAndLsprojectmasterAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInOrderByProtocolordercodeDesc(
			String orderflag, LSprojectmaster lsprojectmaster, Integer protocoltype, Date fromdate, Date todate,
			String orderflag2, Integer protocoltype2, Date fromdate2, Date todate2, List<Elnmaterial> currentChunk);


	List<Logilabprotocolorders> findByOrderflagAndLsprojectmasterInAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInOrderByProtocolordercodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Integer testcode, Integer protocoltype, Date fromdate,
			Date todate, String orderflag2, Integer testcode2, Integer protocoltype2, Date fromdate2, Date todate2,
			List<Elnmaterial> currentChunk);


	List<Logilabprotocolorders> findByOrderflagAndLsprojectmasterAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInOrderByProtocolordercodeDesc(
			String orderflag, LSprojectmaster lsprojectmaster, Integer testcode, Integer protocoltype, Date fromdate,
			Date todate, String orderflag2, Integer testcode2, Integer protocoltype2, Date fromdate2, Date todate2,
			List<Elnmaterial> currentChunk);


	List<Logilabprotocolorders> findByOrderflagAndRejectedAndLsprojectmasterInAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndRejectedAndLsprojectmasterIsNullAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInOrderByProtocolordercodeDesc(
			String orderflag, int i, List<LSprojectmaster> lstproject, Integer testcode, Integer protocoltype,
			Date fromdate, Date todate, String orderflag2, int j, Integer testcode2, Integer protocoltype2,
			Date fromdate2, Date todate2, List<Elnmaterial> currentChunk);


	List<Logilabprotocolorders> findByOrderflagAndRejectedAndLsprojectmasterAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndRejectedAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInOrderByProtocolordercodeDesc(
			String orderflag, int i, LSprojectmaster lsprojectmaster, Integer protocoltype, Date fromdate, Date todate,
			String orderflag2, int j, Integer protocoltype2, Date fromdate2, Date todate2,
			List<Elnmaterial> currentChunk);


	List<Logilabprotocolorders> findByOrderflagAndRejectedAndLsprojectmasterAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndRejectedAndLsprojectmasterIsNullAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInOrderByProtocolordercodeDesc(
			String orderflag, int i, LSprojectmaster lsprojectmaster, Integer testcode, Integer protocoltype,
			Date fromdate, Date todate, String orderflag2, int j, Integer testcode2, Integer protocoltype2,
			Date fromdate2, Date todate2, List<Elnmaterial> currentChunk);



	List<Logilabprotocolorders> findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedOrderByProtocolordercodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer protocoltype, int j,
			List<Long> directory_Code2, int k, LSuserMaster lsuserMaster, Date fromdate2, Date todate2,
			String orderflag2, Integer protocoltype2, int l, List<Long> directory_Code3, int m,
			LSuserMaster lsuserMaster2, Date fromdate3, Date todate3, String orderflag3, Integer protocoltype3, int n);


	List<Logilabprotocolorders> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedOrderByProtocolordercodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer protocoltype, int j,
			List<Long> directory_Code2, int k, LSuserMaster lsuserMaster, Date fromdate2, Date todate2,
			String orderflag2, Integer protocoltype2, int l, List<Long> directory_Code3, int m, Date fromdate3,
			Date todate3, List<Integer> userlist, String orderflag3, Integer protocoltype3, int n);


	List<Logilabprotocolorders> findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndTestcodeOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndTestcodeOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndTestcodeOrderByProtocolordercodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer protocoltype,
			Integer testcode, List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2,
			Date todate2, String orderflag2, Integer protocoltype2, Integer testcode2, List<Long> directory_Code3,
			int k, LSuserMaster lsuserMaster2, Date fromdate3, Date todate3, String orderflag3, Integer protocoltype3,
			Integer testcode3);


	List<Logilabprotocolorders> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndTestcodeOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndTestcodeOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndTestcodeOrderByProtocolordercodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer protocoltype,
			Integer testcode, List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2,
			Date todate2, String orderflag2, Integer protocoltype2, Integer testcode2, List<Long> directory_Code3,
			int k, Date fromdate3, Date todate3, List<Integer> userlist, String orderflag3, Integer protocoltype3,
			Integer testcode3);


	List<Logilabprotocolorders> findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedAndTestcodeOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedAndTestcodeOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedAndTestcodeOrderByProtocolordercodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer protocoltype, int j,
			Integer testcode, List<Long> directory_Code2, int k, LSuserMaster lsuserMaster, Date fromdate2,
			Date todate2, String orderflag2, Integer protocoltype2, int l, Integer testcode2,
			List<Long> directory_Code3, int m, LSuserMaster lsuserMaster2, Date fromdate3, Date todate3,
			String orderflag3, Integer protocoltype3, int n, Integer testcode3);


	List<Logilabprotocolorders> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedAndTestcodeOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedAndTestcodeOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedAndTestcodeOrderByProtocolordercodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer protocoltype, int j,
			List<Long> directory_Code2, Integer testcode, int k, LSuserMaster lsuserMaster, Date fromdate2,
			Date todate2, String orderflag2, Integer protocoltype2, int l, Integer testcode2,
			List<Long> directory_Code3, int m, Date fromdate3, Date todate3, List<Integer> userlist, String orderflag3,
			Integer protocoltype3, int n, Integer testcode3);

	Collection<? extends Logilabprotocolorders> findByOrderflagAndLsprojectmasterInAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndOrdercancellIsNull(
			String orderflag, List<LSprojectmaster> lstproject, Integer protocoltype, Date fromdate, Date todate);

	List<LSlogilabprotocoldetail> findByRepeatAndAutoregistercountGreaterThan(boolean b, int i);


	List<Logilabprotocolorders> findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndAssignedtoIsNullAndOrdercancellIsNullOrderByProtocolordercodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer protocoltype,
			List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2, Date todate2,
			String orderflag2, Integer protocoltype2, List<Long> directory_Code3, int k, LSuserMaster lsuserMaster2,
			Date fromdate3, Date todate3, String orderflag3, Integer protocoltype3);


	List<Logilabprotocolorders> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndAssignedtoIsNullAndOrdercancellIsNullOrderByProtocolordercodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer protocoltype,
			List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2, Date todate2,
			String orderflag2, Integer protocoltype2, List<Long> directory_Code3, int k, Date fromdate3, Date todate3,
			List<Integer> userlist, String orderflag3, Integer protocoltype3);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrderByProtocolordercodeDesc(
			Long directorycode, int i, Date fromdate, Date todate, Integer sitecode, Long directorycode2, int j,
			LSuserMaster createdby, Date fromdate2, Date todate2, Integer sitecode2, Long directorycode3, int k,
			LSuserMaster createdby2, Date fromdate3, Date todate3, Integer sitecode3);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndSitecodeOrderByProtocolordercodeDesc(
			Long directorycode, int i, Date fromdate, Date todate, Integer sitecode, Long directorycode2, int j,
			LSuserMaster createdby, Date fromdate2, Date todate2, Integer sitecode2, Long directorycode3, int k,
			Date fromdate3, Date todate3, Integer[] lstuserMaster, Integer sitecode3);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrderByProtocolordercodeDesc(
			Long directorycode, int i, Integer protocoltype, String orderflag, int j, Date fromdate, Date todate,
			Integer sitecode, Long directorycode2, int k, Integer protocoltype2, String orderflag2, int l,
			LSuserMaster createdby, Date fromdate2, Date todate2, Integer sitecode2, Long directorycode3, int m,
			Integer protocoltype3, String orderflag3, int n, LSuserMaster createdby2, Date fromdate3, Date todate3,
			Integer sitecode3);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrderByProtocolordercodeDesc(
			Long directorycode, int i, Integer protocoltype, String orderflag, Date fromdate, Date todate,
			Integer sitecode, Long directorycode2, int j, Integer protocoltype2, String orderflag2,
			LSuserMaster createdby, Date fromdate2, Date todate2, Integer sitecode2, Long directorycode3, int k,
			Integer protocoltype3, String orderflag3, LSuserMaster createdby2, Date fromdate3, Date todate3,
			Integer sitecode3);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndCreatedtimestampBetweenAndCreatebyInAndSitecodeOrderByProtocolordercodeDesc(
			Long directorycode, int i, Integer protocoltype, String orderflag, int j, Date fromdate, Date todate,
			Integer sitecode, Long directorycode2, int k, Integer protocoltype2, String orderflag2, int l,
			LSuserMaster createdby, Date fromdate2, Date todate2, Integer sitecode2, Long directorycode3, int m,
			Integer protocoltype3, String orderflag3, int n, Date fromdate3, Date todate3, Integer[] lstuserMaster,
			Integer sitecode3);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndCreatedtimestampBetweenAndCreatebyInAndSitecodeOrderByProtocolordercodeDesc(
			Long directorycode, int i, Integer protocoltype, String orderflag, Date fromdate, Date todate,
			Integer sitecode, Long directorycode2, int j, Integer protocoltype2, String orderflag2,
			LSuserMaster createdby, Date fromdate2, Date todate2, Integer sitecode2, Long directorycode3, int k,
			Integer protocoltype3, String orderflag3, Date fromdate3, Date todate3, Integer[] lstuserMaster,
			Integer sitecode3);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndOrderflagAndRejectedAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndOrderflagAndRejectedAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndOrderflagAndRejectedAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrderByProtocolordercodeDesc(
			Long directorycode, int i, String orderflag, int j, Date fromdate, Date todate, Integer sitecode,
			Long directorycode2, int k, String orderflag2, int l, LSuserMaster createdby, Date fromdate2, Date todate2,
			Integer sitecode2, Long directorycode3, int m, String orderflag3, int n, LSuserMaster createdby2,
			Date fromdate3, Date todate3, Integer sitecode3);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndOrderflagAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrderByProtocolordercodeDesc(
			Long directorycode, int i, String orderflag, Date fromdate, Date todate, Integer sitecode,
			Long directorycode2, int j, String orderflag2, LSuserMaster createdby, Date fromdate2, Date todate2,
			Integer sitecode2, Long directorycode3, int k, String orderflag3, LSuserMaster createdby2, Date fromdate3,
			Date todate3, Integer sitecode3);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndOrderflagAndRejectedAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndOrderflagAndRejectedAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndOrderflagAndRejectedAndCreatedtimestampBetweenAndCreatebyInAndSitecodeOrderByProtocolordercodeDesc(
			Long directorycode, int i, String orderflag, int j, Date fromdate, Date todate, Integer sitecode,
			Long directorycode2, int k, String orderflag2, int l, LSuserMaster createdby, Date fromdate2, Date todate2,
			Integer sitecode2, Long directorycode3, int m, String orderflag3, int n, Date fromdate3, Date todate3,
			Integer[] lstuserMaster, Integer sitecode3);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndOrderflagAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndOrderflagAndCreatedtimestampBetweenAndCreatebyInAndSitecodeOrderByProtocolordercodeDesc(
			Long directorycode, int i, String orderflag, Date fromdate, Date todate, Integer sitecode,
			Long directorycode2, int j, String orderflag2, LSuserMaster createdby, Date fromdate2, Date todate2,
			Integer sitecode2, Long directorycode3, int k, String orderflag3, Date fromdate3, Date todate3,
			Integer[] lstuserMaster, Integer sitecode3);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndProtocoltypeAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrderByProtocolordercodeDesc(
			Long directorycode, int i, Integer protocoltype, Date fromdate, Date todate, Integer sitecode,
			Long directorycode2, int j, Integer protocoltype2, LSuserMaster createdby, Date fromdate2, Date todate2,
			Integer sitecode2, Long directorycode3, int k, Integer protocoltype3, LSuserMaster createdby2,
			Date fromdate3, Date todate3, Integer sitecode3);


	List<LSlogilabprotocoldetail> findByDirectorycodeAndViewoptionAndProtocoltypeAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndCreatedtimestampBetweenAndCreatebyInAndSitecodeOrderByProtocolordercodeDesc(
			Long directorycode, int i, Integer protocoltype, Date fromdate, Date todate, Integer sitecode,
			Long directorycode2, int j, Integer protocoltype2, LSuserMaster createdby, Date fromdate2, Date todate2,
			Integer sitecode2, Long directorycode3, int k, Integer protocoltype3, Date fromdate3, Date todate3,
			Integer[] lstuserMaster, Integer sitecode3);


	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM lslogilabprotocoldetail o " +
	        "INNER JOIN lsprotocolmaster l ON o.lsprotocolmaster_protocolmastercode = l.protocolmastercode " +
	        "INNER JOIN lstestmasterlocal m ON m.testcode = o.testcode " +
	        "WHERE (" +
	        // First condition for project-based search
	        "  o.lsprojectmaster_projectcode IN (" +
	        "    SELECT lsprojectmaster_projectcode " +
	        "    FROM LSlogilablimsorderdetail " +
	        "    WHERE lsprojectmaster_projectcode IN (" +
	        "      SELECT projectcode FROM LSprojectmaster " +
	        "      WHERE lsusersteam_teamcode IN (" +
	        "        SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?1" +
	        "      )" +
	        "    )" +
	        "  )" +
	        "  AND o.ordercancell IS NULL " +
	        "  AND (" +
	        "    LOWER(o.protocolordername) LIKE LOWER(?2) " +
	        "    OR LOWER(m.testname) LIKE LOWER(?2) " +
	        "    OR LOWER(o.keyword) LIKE LOWER(?2) " +
	        "    OR LOWER(l.protocolmastername) LIKE LOWER(?2)" +
	        "  )" +
	        ")" +
	        // Second condition for null project and assigned to null
	        " OR (" +
	        "  o.lsprojectmaster_projectcode IS NULL " +
	        "  AND o.assignedto_usercode IS NULL " +
	        "  AND o.elnmaterial_nmaterialcode IN (SELECT nmaterialcode FROM elnmaterial WHERE nsitecode = ?3) " +
	        "  AND (" +
	        "    LOWER(o.protocolordername) LIKE LOWER(?2) " +
	        "    OR LOWER(m.testname) LIKE LOWER(?2) " +
	        "    OR LOWER(o.keyword) LIKE LOWER(?2) " +
	        "    OR LOWER(l.protocolmastername) LIKE LOWER(?2)" +
	        "  ) " +
	        "  AND o.viewoption = ?4 " +
	        "  AND o.ordercancell IS NULL" +
	        ")" +
	        // Third condition for null project, assigned to null, created by user
	        " OR (" +
	        "  o.lsprojectmaster_projectcode IS NULL " +
	        "  AND o.assignedto_usercode IS NULL " +
	        "  AND o.elnmaterial_nmaterialcode IN (SELECT nmaterialcode FROM elnmaterial WHERE nsitecode = ?3) " +
	        "  AND (" +
	        "    LOWER(o.protocolordername) LIKE LOWER(?2) " +
	        "    OR LOWER(m.testname) LIKE LOWER(?2) " +
	        "    OR LOWER(o.keyword) LIKE LOWER(?2) " +
	        "    OR LOWER(l.protocolmastername) LIKE LOWER(?2)" +
	        "  ) " +
	        "  AND o.viewoption = ?5 " +
	        "  AND o.createby = ?1 " +
	        "  AND o.ordercancell IS NULL" +
	        ")" +
	        // Fourth condition for null project, assigned to null, created by list of users
	        " OR (" +
	        "  o.lsprojectmaster_projectcode IS NULL " +
	        "  AND o.assignedto_usercode IS NULL " +
	        "  AND o.elnmaterial_nmaterialcode IN (SELECT nmaterialcode FROM elnmaterial WHERE nsitecode = ?3) " +
	        "  AND (" +
	        "    LOWER(o.protocolordername) LIKE LOWER(?2) " +
	        "    OR LOWER(m.testname) LIKE LOWER(?2) " +
	        "    OR LOWER(o.keyword) LIKE LOWER(?2) " +
	        "    OR LOWER(l.protocolmastername) LIKE LOWER(?2)" +
	        "  ) " +
	        "  AND o.viewoption = ?5 " +
	        "  AND o.createby IN (?6) " +
	        "  AND o.ordercancell IS NULL" +
	        ")" +
	        // Fifth condition for directory-based filtering
	        " OR (" +
	        "  o.directorycode IN (?7) " +
	        "  AND o.viewoption = ?3 " +
	        "  AND o.lsprojectmaster_projectcode IS NULL " +
	        "  AND o.assignedto_usercode IS NULL " +
	        "  AND o.ordercancell IS NULL " +
	        "  AND (" +
	        "    LOWER(o.protocolordername) LIKE LOWER(?2) " +
	        "    OR LOWER(m.testname) LIKE LOWER(?2) " +
	        "    OR LOWER(o.keyword) LIKE LOWER(?2) " +
	        "    OR LOWER(l.protocolmastername) LIKE LOWER(?2)" +
	        "  )" +
	        ")" +
	        // Sixth condition for directory-based filtering with usercode
	        " OR (" +
	        "  o.directorycode IN (?7) " +
	        "  AND o.viewoption = ?4 " +
	        "  AND o.lsuserMaster_usercode = ?1 " +
	        "  AND o.lsprojectmaster_projectcode IS NULL " +
	        "  AND o.assignedto_usercode IS NULL " +
	        "  AND o.ordercancell IS NULL " +
	        "  AND (" +
	        "    LOWER(o.protocolordername) LIKE LOWER(?2) " +
	        "    OR LOWER(m.testname) LIKE LOWER(?2) " +
	        "    OR LOWER(o.keyword) LIKE LOWER(?2) " +
	        "    OR LOWER(l.protocolmastername) LIKE LOWER(?2)" +
	        "  )" +
	        ")" +
	        // Seventh condition for directory-based filtering with list of created users
	        " OR (" +
	        "  o.directorycode IN (?7) " +
	        "  AND o.viewoption = ?5 " +
	        "  AND o.createby IN (?6) " +
	        "  AND o.lsprojectmaster_projectcode IS NULL " +
	        "  AND o.assignedto_usercode IS NULL " +
	        "  AND o.ordercancell IS NULL " +
	        "  AND (" +
	        "    LOWER(o.protocolordername) LIKE LOWER(?2) " +
	        "    OR LOWER(m.testname) LIKE LOWER(?2) " +
	        "    OR LOWER(o.keyword) LIKE LOWER(?2) " +
	        "    OR LOWER(l.protocolmastername) LIKE LOWER(?2)" +
	        "  )" +
	        ")ORDER BY protocolordercode DESC OFFSET ?8 ROWS FETCH NEXT ?9 ROWS ONLY ",
	        nativeQuery = true)
	List<LSlogilabprotocoldetail> getSearchedRecords(Integer integer, String searchkeywords, int i, int j, int k, List<Integer> userlist, List<Lsprotocolorderstructure> lstdir, int l, Integer integer2);

	
	@Transactional
//	@Modifying
	@Query(value = "SELECT count(*) FROM lslogilabprotocoldetail o " +
	        "INNER JOIN lsprotocolmaster l ON o.lsprotocolmaster_protocolmastercode = l.protocolmastercode " +
	        "INNER JOIN lstestmasterlocal m ON m.testcode = o.testcode " +
	        "WHERE (" +
	        // First condition for project-based search
	        "  o.lsprojectmaster_projectcode IN (" +
	        "    SELECT lsprojectmaster_projectcode " +
	        "    FROM LSlogilablimsorderdetail " +
	        "    WHERE lsprojectmaster_projectcode IN (" +
	        "      SELECT projectcode FROM LSprojectmaster " +
	        "      WHERE lsusersteam_teamcode IN (" +
	        "        SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?1" +
	        "      )" +
	        "    )" +
	        "  )" +
	        "  AND o.ordercancell IS NULL " +
	        "  AND (" +
	        "    LOWER(o.protocolordername) LIKE LOWER(?2) " +
	        "    OR LOWER(m.testname) LIKE LOWER(?2) " +
	        "    OR LOWER(o.keyword) LIKE LOWER(?2) " +
	        "    OR LOWER(l.protocolmastername) LIKE LOWER(?2)" +
	        "  )" +
	        ")" +
	        // Second condition for null project and assigned to null
	        " OR (" +
	        "  o.lsprojectmaster_projectcode IS NULL " +
	        "  AND o.assignedto_usercode IS NULL " +
	        "  AND o.elnmaterial_nmaterialcode IN (SELECT nmaterialcode FROM elnmaterial WHERE nsitecode = ?3) " +
	        "  AND (" +
	        "    LOWER(o.protocolordername) LIKE LOWER(?2) " +
	        "    OR LOWER(m.testname) LIKE LOWER(?2) " +
	        "    OR LOWER(o.keyword) LIKE LOWER(?2) " +
	        "    OR LOWER(l.protocolmastername) LIKE LOWER(?2)" +
	        "  ) " +
	        "  AND o.viewoption = ?4 " +
	        "  AND o.ordercancell IS NULL" +
	        ")" +
	        // Third condition for null project, assigned to null, created by user
	        " OR (" +
	        "  o.lsprojectmaster_projectcode IS NULL " +
	        "  AND o.assignedto_usercode IS NULL " +
	        "  AND o.elnmaterial_nmaterialcode IN (SELECT nmaterialcode FROM elnmaterial WHERE nsitecode = ?3) " +
	        "  AND (" +
	        "    LOWER(o.protocolordername) LIKE LOWER(?2) " +
	        "    OR LOWER(m.testname) LIKE LOWER(?2) " +
	        "    OR LOWER(o.keyword) LIKE LOWER(?2) " +
	        "    OR LOWER(l.protocolmastername) LIKE LOWER(?2)" +
	        "  ) " +
	        "  AND o.viewoption = ?5 " +
	        "  AND o.createby = ?1 " +
	        "  AND o.ordercancell IS NULL" +
	        ")" +
	        // Fourth condition for null project, assigned to null, created by list of users
	        " OR (" +
	        "  o.lsprojectmaster_projectcode IS NULL " +
	        "  AND o.assignedto_usercode IS NULL " +
	        "  AND o.elnmaterial_nmaterialcode IN (SELECT nmaterialcode FROM elnmaterial WHERE nsitecode = ?3) " +
	        "  AND (" +
	        "    LOWER(o.protocolordername) LIKE LOWER(?2) " +
	        "    OR LOWER(m.testname) LIKE LOWER(?2) " +
	        "    OR LOWER(o.keyword) LIKE LOWER(?2) " +
	        "    OR LOWER(l.protocolmastername) LIKE LOWER(?2)" +
	        "  ) " +
	        "  AND o.viewoption = ?5 " +
	        "  AND o.createby IN (?6) " +
	        "  AND o.ordercancell IS NULL" +
	        ")" +
	        // Fifth condition for directory-based filtering
	        " OR (" +
	        "  o.directorycode IN (?7) " +
	        "  AND o.viewoption = ?3 " +
	        "  AND o.lsprojectmaster_projectcode IS NULL " +
	        "  AND o.assignedto_usercode IS NULL " +
	        "  AND o.ordercancell IS NULL " +
	        "  AND (" +
	        "    LOWER(o.protocolordername) LIKE LOWER(?2) " +
	        "    OR LOWER(m.testname) LIKE LOWER(?2) " +
	        "    OR LOWER(o.keyword) LIKE LOWER(?2) " +
	        "    OR LOWER(l.protocolmastername) LIKE LOWER(?2)" +
	        "  )" +
	        ")" +
	        // Sixth condition for directory-based filtering with usercode
	        " OR (" +
	        "  o.directorycode IN (?7) " +
	        "  AND o.viewoption = ?4 " +
	        "  AND o.lsuserMaster_usercode = ?1 " +
	        "  AND o.lsprojectmaster_projectcode IS NULL " +
	        "  AND o.assignedto_usercode IS NULL " +
	        "  AND o.ordercancell IS NULL " +
	        "  AND (" +
	        "    LOWER(o.protocolordername) LIKE LOWER(?2) " +
	        "    OR LOWER(m.testname) LIKE LOWER(?2) " +
	        "    OR LOWER(o.keyword) LIKE LOWER(?2) " +
	        "    OR LOWER(l.protocolmastername) LIKE LOWER(?2)" +
	        "  )" +
	        ")" +
	        // Seventh condition for directory-based filtering with list of created users
	        " OR (" +
	        "  o.directorycode IN (?7) " +
	        "  AND o.viewoption = ?5 " +
	        "  AND o.createby IN (?6) " +
	        "  AND o.lsprojectmaster_projectcode IS NULL " +
	        "  AND o.assignedto_usercode IS NULL " +
	        "  AND o.ordercancell IS NULL " +
	        "  AND (" +
	        "    LOWER(o.protocolordername) LIKE LOWER(?2) " +
	        "    OR LOWER(m.testname) LIKE LOWER(?2) " +
	        "    OR LOWER(o.keyword) LIKE LOWER(?2) " +
	        "    OR LOWER(l.protocolmastername) LIKE LOWER(?2)" +
	        "  )" +
	        ")",
	        nativeQuery = true)
	public Long getcountSearchedRecords(Integer integer, String searchkeywords, int i, int j, int k, List<Integer> userlist, List<Lsprotocolorderstructure> lstdir);


	}

