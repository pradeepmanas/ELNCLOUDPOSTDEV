package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.fetchmodel.getorders.Logilabordermaster;
import com.agaram.eln.primary.fetchmodel.getorders.Logilaborders;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public interface LSlogilablimsorderdetailRepository extends JpaRepository<LSlogilablimsorderdetail, Long> {

	@Transactional
	@Modifying
	@Query("update LSlogilablimsorderdetail o set o.batchid = ?1 where o.batchcode = ?2")
	void setbatchidBybatchcode(String batchid, Long batchcode);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagOrderByBatchcodeDesc(Integer filetype,String orderflag);

	public long countByFiletypeAndOrderflagOrderByBatchcodeDesc(Integer filetype, String orderflag);

	public List<LSlogilablimsorderdetail> findByBatchcodeAndBatchid(Long batchcode, String batchid);

	public LSlogilablimsorderdetail findByBatchid(String Batchid);

	public LSlogilablimsorderdetail findBylssamplefile(LSsamplefile lssamplefile);

	public List<LSlogilablimsorderdetail> findByOrderflag(String orderflag);

	public List<LSlogilablimsorderdetail> findByOrderflagAndLsprojectmaster(String orderflag,
			LSprojectmaster lsproject);

	public List<LSlogilablimsorderdetail> findByOrderflagAndLsprojectmasterIn(String orderflag,
			List<LSprojectmaster> lstproject);

//	public long countByOrderflag(String orderflag);

	public long countByOrderflagAndLssamplefileIn(String orderflag, List<LSsamplefile> lssamplefile);

	public List<LSlogilablimsorderdetail> findByOrderflagAndLssamplefileIn(String orderflag,
			List<LSsamplefile> lssamplefile);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndLsprojectmasterInAndLsworkflowInOrderByBatchcodeDesc(
			Integer filetype, String orderflag, List<LSprojectmaster> lstproject, List<LSworkflow> lsworkflow);
	
	public List<LSlogilablimsorderdetail> findByOrderflagAndLsprojectmasterInAndLsworkflowInOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, List<LSworkflow> lsworkflow);
	
	public List<LSlogilablimsorderdetail> findByOrderflagAndLsprojectmasterInAndLsworkflowInAndLockeduserIsNotNullOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, List<LSworkflow> lsworkflow);

	public long countByOrderflagAndLsprojectmasterInAndLsworkflowInOrderByBatchcodeDesc(String orderflag,
			List<LSprojectmaster> lstproject, List<LSworkflow> lsworkflow);

	public List<LSlogilablimsorderdetail> findByOrderflagAndLsprojectmasterInOrderByBatchcodeDesc(String orderflag,
			List<LSprojectmaster> lstproject);

	public long countByOrderflagAndLsprojectmasterInOrderByBatchcodeDesc(String orderflag,
			List<LSprojectmaster> lstproject);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndLsprojectmasterInOrderByBatchcodeDesc(
			Integer filetype, String orderflag, List<LSprojectmaster> lstproject);

	public List<LSlogilablimsorderdetail> findByFiletypeOrderByBatchcodeDesc(Integer filetype);

	public long countByFiletypeOrderByBatchcodeDesc(Integer filetype);

	public List<LSlogilablimsorderdetail> findByFiletypeAndLssamplefileInOrderByBatchcodeDesc(Integer filetype,
			List<LSsamplefile> lssamplefile);

	public long countByFiletypeAndOrderflagAndLssamplefileIn(Integer filetype, String orderflag,
			List<LSsamplefile> lssamplefile);

	public List<LSlogilablimsorderdetail> findByLsprojectmasterInOrderByBatchcodeDesc(List<LSprojectmaster> lstproject);

	public long countByLsprojectmasterInOrderByBatchcodeDesc(List<LSprojectmaster> lstproject);

	public List<LSlogilablimsorderdetail> findByOrderflagAndLssamplefileInAndLsprojectmasterIn(String orderflag,
			List<LSsamplefile> lssamplefile, List<LSprojectmaster> lstproject);

	public long countByOrderflagAndLssamplefileInAndLsprojectmasterIn(String orderflag, List<LSsamplefile> lssamplefile,
			List<LSprojectmaster> lstproject);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, Date fromdate, Date todate);

	public long countByFiletypeAndOrderflagAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findFirst15ByFiletypeAndOrderflagAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Integer filetype, String orderflag, Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findFirst10ByFiletypeAndOrderflagAndBatchcodeLessThanAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, Long batchcode, Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndBatchcodeLessThanAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, Long batchcode, Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, Date fromdate, Date todate);

	public long countByFiletypeAndOrderflagAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findFirst15ByFiletypeAndOrderflagAndCompletedtimestampBetweenOrderByBatchcodeDesc(
			Integer filetype, String orderflag, Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findFirst10ByFiletypeAndOrderflagAndBatchcodeLessThanAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, Long batchcode, Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndBatchcodeLessThanAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, Long batchcode, Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndLsprojectmasterInAndLsworkflowInAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Integer filetype, String orderflag, List<LSprojectmaster> lstproject, List<LSworkflow> lsworkflow,
			Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	public long countByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findFirst15ByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Integer filetype, String orderflag, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findFirst10ByFiletypeAndOrderflagAndBatchcodeLessThanAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, Long batchcode, List<LSprojectmaster> lstproject, Date fromdate,
			Date todate);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndBatchcodeLessThanAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, Long batchcode, List<LSprojectmaster> lstproject, Date fromdate,
			Date todate);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	public long countByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findFirst15ByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenOrderByBatchcodeDesc(
			Integer filetype, String orderflag, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findFirst10ByFiletypeAndOrderflagAndBatchcodeLessThanAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, Long batchcode, List<LSprojectmaster> lstproject, Date fromdate,
			Date todate);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndBatchcodeLessThanAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, Long batchcode, List<LSprojectmaster> lstproject, Date fromdate,
			Date todate);

	public List<LSlogilablimsorderdetail> findByFiletypeAndLsfileOrderByBatchcodeDesc(Integer filetype, LSfile lSfile);

	public List<LSlogilablimsorderdetail> findByFiletypeAndLsfileAndLsprojectmasterInOrderByBatchcodeDesc(
			Integer filetype, LSfile lSfile, List<LSprojectmaster> lstproject);

	public List<LSlogilablimsorderdetail> findByFiletypeAndLsprojectmasterInOrderByBatchcodeDesc(Integer filetype,
			List<LSprojectmaster> lstproject);

	public long countByLsworkflowAndOrderflag(LSworkflow lsworkflow, String orderflag);

	@Transactional
	@Modifying
	@Query("update LSlogilablimsorderdetail o set o.lsworkflow = null where o.lsworkflow = ?1")
	void setWorkflownullforcompletedorder(LSworkflow lsworkflow);

	@Transactional
	@Modifying
	@Query(value = "select batchcode from "
			+ "LSlogilablimsorderdetail where filetype = ?1 and orderflag = ?2 and createdtimestamp BETWEEN ?3 and ?4 and assignedto_usercode IS NULL ORDER BY batchcode DESC", nativeQuery = true)
	public List<Long> getBatchcodeonFiletypeAndFlagandcreateddate(Integer filetype, String flag, Date fromdate,
			Date todate);

	@Transactional
	@Modifying
	@Query(value = "select batchcode from "
			+ "LSlogilablimsorderdetail where filetype = ?1 and orderflag = ?2 and completedtimestamp BETWEEN ?3 and ?4 and assignedto_usercode IS NULL ORDER BY batchcode DESC", nativeQuery = true)
	public List<Long> getBatchcodeonFiletypeAndFlagandCompletedtime(Integer filetype, String flag, Date fromdate,
			Date todate);

	@Transactional
	@Modifying
	@Query(value = "select batchcode from "
			+ "LSlogilablimsorderdetail where filetype = ?1 and orderflag = ?2 and lsprojectmaster_projectcode in (?3) and "
			+ "createdtimestamp BETWEEN ?4 and ?5 and assignedto_usercode IS NULL ORDER BY batchcode DESC", nativeQuery = true)
	public List<Long> getBatchcodeonFiletypeAndFlagandProjectandcreateddate(Integer filetype, String flag,
			List<Integer> lstproject, Date fromdate, Date todate);

	@Transactional
	@Modifying
	@Query(value = "select batchcode from "
			+ "LSlogilablimsorderdetail where filetype = ?1 and orderflag = ?2 and lsprojectmaster_projectcode in (?3) and "
			+ "completedtimestamp BETWEEN ?4 and ?5 and assignedto_usercode IS NULL ORDER BY batchcode DESC", nativeQuery = true)
	public List<Long> getBatchcodeonFiletypeAndFlagandProjectandCompletedtime(Integer filetype, String flag,
			List<Integer> lstproject, Date fromdate, Date todate);

	@Transactional
	@Modifying
	@Query(value = "select batchcode from "
			+ "LSlogilablimsorderdetail where orderflag = ?1 and lsprojectmaster_projectcode in (?2) and lsworkflow_workflowcode in (?3) "
			+ "and lsuserMaster_usercode = ?4", nativeQuery = true)
	public List<Long> countByOrderflagAndLsprojectmasterInAndLsworkflowInOrderByBatchcodeDesc(String orderflag,
			List<LSprojectmaster> lstproject, List<LSworkflow> lsworkflow, Integer usercode);

	@Transactional
	@Modifying
	@Query(value = "select batchcode from "
			+ "LSlogilablimsorderdetail where orderflag = ?1 and lsprojectmaster_projectcode in (?2)", nativeQuery = true)
	public List<Long> countByOrderflagAndLsprojectmasterInAndLsworkflowInOrderByBatchcodeDesc(String orderflag,
			List<LSprojectmaster> lstproject);

	@Transactional
	@Modifying
	@Query(value = "select batchcode from"
			+ " LSlogilablimsorderdetail where orderflag = ?1 and lsprojectmaster_projectcode in (?2) and lsuserMaster_usercode = ?3", nativeQuery = true)
	public List<Long> countByOrderflagAndLsprojectmasterInOrderByBatchcodeDesc(String orderflag,
			List<LSprojectmaster> lstproject, Integer usercode);

	@Transactional
	@Modifying
	@Query(value = "select batchcode from"
			+ " LSlogilablimsorderdetail where orderflag = ?1 and lsprojectmaster_projectcode in (?2)", nativeQuery = true)
	public List<Long> countByOrderflagAndLsprojectmasterInOrderByBatchcodeDesc1(String orderflag,
			List<LSprojectmaster> lstproject);

	@Transactional
	@Modifying
	@Query(value = "select batchcode from"
			+ " LSlogilablimsorderdetail where orderflag = ?1  and lssamplefile_filesamplecode in (?2) and lsprojectmaster_projectcode in (?3) "
			+ "and lsuserMaster_usercode = ?4", nativeQuery = true)
	public List<Long> countByOrderflagAndLssamplefileInAndLsprojectmasterIn(String orderflag,
			List<LSsamplefile> lssamplefile, List<LSprojectmaster> lstproject, Integer usercode);

	public List<Long> findBatchcodeByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String flag, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndLssamplefileIn(
			Integer filetype, String flag, List<Integer> lstproject, Date fromdate, Date todate,
			List<LSsamplefile> idList);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndLssamplefileIn(
			Integer filetype, String flag, List<Integer> lstproject, Date fromdate, Date todate,
			List<LSsamplefile> idList);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndCreatedtimestampBetweenAndLssamplefileIn(
			Integer filetype, String flag, Date fromdate, Date todate, List<LSsamplefile> idList);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndCompletedtimestampBetweenAndLssamplefileIn(
			Integer filetype, String flag, Date fromdate, Date todate, List<LSsamplefile> idList);
	
	public List<LSlogilablimsorderdetail> findByOrderflagAndLockeduserIsNotNullOrderByBatchcodeDesc(String flag);

	public List<LSlogilablimsorderdetail> findByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Integer filetype, LSuserMaster assignedto, Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
			Integer filetype, LSuserMaster user, LSuserMaster assignedto, Date fromdate, Date todate);

	public Long countByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(Integer filetype,
			LSuserMaster assignedto, Date fromdate, Date todate);

	public Long countByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
			Integer filetype, LSuserMaster user, LSuserMaster assignedto, Date fromdate, Date todate);

	@Transactional
	@Modifying
	@Query(value = "select batchcode from LSlogilablimsorderdetail where filetype = ?1 and assignedto_usercode = ?2 and "
			+ "createdtimestamp BETWEEN ?3 and ?4 ORDER BY batchcode DESC", nativeQuery = true)
	public List<Long> getBatchcodeonFiletypeAndFlagandAssignedtoAndCreatedtimestamp(Integer filetype,
			Integer assignedto, Date fromdate, Date todate);

	@Transactional
	@Modifying
	@Query(value = "select batchcode from LSlogilablimsorderdetail where filetype = ?1 and lsuserMaster_usercode = ?2 and assignedto_usercode <> ?3 and "
			+ "createdtimestamp BETWEEN ?4 and ?5 ORDER BY batchcode DESC", nativeQuery = true)
	public List<Long> getBatchcodeonFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestamp(Integer filetype,
			Integer user, Integer assignedto, Date fromdate, Date todate);

	@Transactional
	@Modifying
	@Query(value = "select batchcode from LSlogilablimsorderdetail where filetype = ?1 and orderflag = ?2 and lsuserMaster_usercode = ?3 and assignedto_usercode <> ?4 and "
			+ "createdtimestamp BETWEEN ?5 and ?6 ORDER BY batchcode DESC", nativeQuery = true)
	public List<Long> getBatchcodeonFiletypeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestamp(
			Integer filetype, String flag, Integer user, Integer assignedto, Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
			Integer filetype, String flag, LSuserMaster user, LSuserMaster assignedto, Date fromdate, Date todate);

	public Long countByFiletypeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
			Integer filetype, String flag, LSuserMaster user, LSuserMaster assignedto, Date fromdate, Date todate);

	long countByFiletypeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(Integer filetype,
			String string, LSuserMaster lsuserMaster, Date fromdate, Date todate);

	List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Integer filetype, String string, LSuserMaster lsuserMaster, Date fromdate, Date todate);

	@Transactional
	@Modifying
	@Query(value = "select batchcode from"
			+ " LSlogilablimsorderdetail where ( orderflag = ?1 and lsprojectmaster_projectcode in (?2) and approvelstatus = ?3 ) or ( approved= ?4 and orderflag = ?5 )", nativeQuery = true)
	public List<Long> countByOrderflagAndLsprojectmasterInOrderByBatchcodeDescInprogress(String orderflag,
			List<LSprojectmaster> lstproject, Integer approvelstatus, Integer approved, String orderflag1);

	public Long countByCreatedtimestampBetween(Date fromdate, Date todate);
	
	public Long countByOrderflag(String orderflag);

	public long countByOrderflagAndCreatedtimestampBetween(String orderflag, Date fromdate, Date todate);
	
	public long countByOrderflagAndApprovelstatusNotAndCreatedtimestampBetween(String orderflag,int i, Date fromdate, Date todate);
	
	public long countByOrderflagAndApprovelstatusNotAndOrdercancellNotAndCreatedtimestampBetween(String orderflag,int i,int j,Date fromdate, Date todate);
	
	public long  countByApprovelstatusAndCreatedtimestampBetween(int i, Date fromdate, Date todate);
	
	public long countByOrdercancellAndCompletedtimestampBetween(int i, Date fromdate, Date todate);
	
	public long countByOrdercancellAndCreatedtimestampBetween(int i, Date fromdate, Date todate);

	public long countByOrderflagAndCompletedtimestampBetween(String orderflag, Date fromdate, Date todate);
	
	public long countByOrderflagAndCompletedtimestampBetweenAndApprovelstatusNot(String orderflag, Date fromdate, Date todate,int i);

	public long countByOrderflagAndLssamplefileInAndCreatedtimestampBetween(String orderflag,
			List<LSsamplefile> lssamplefile, Date fromdate, Date todate);

	public long countByFiletypeAndCreatedtimestampBetween(Integer filetype, Date fromdate, Date todate);

	public Long countByLsprojectmasterInAndCreatedtimestampBetween(List<LSprojectmaster> lstproject, Date fromdate,
			Date todate);
	
	public long countByLsprojectmasterInOrFiletypeAndCreatedtimestampBetween(List<LSprojectmaster> lstproject,Integer filetype, Date fromdate,
			Date todate);

	public long countByFiletypeAndOrderflagAndCreatedtimestampBetween(Integer filetype, String orderflag, Date fromdate,
			Date todate);

	public long countByOrderflagAndLsprojectmasterInAndCompletedtimestampBetween(String orderflag,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate);
	
	public long countByOrderflagAndLsprojectmasterInOrFiletypeAndCompletedtimestampBetween(String orderflag,
			List<LSprojectmaster> lstproject, Integer filetype, Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findByOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	public long countByFiletypeAndOrderflagAndLssamplefileInAndCreatedtimestampBetween(Integer filetype,
			String orderflag, List<LSsamplefile> lssamplefile, Date fromdate, Date todate);

	@Transactional
	@Modifying
	@Query(value = "select batchcode from"
			+ " LSlogilablimsorderdetail where ( orderflag = ?1 and lsprojectmaster_projectcode in (?2) and approvelstatus = ?3  and createdtimestamp BETWEEN (?6) AND (?7) ) or ( approved= ?4 and orderflag = ?5 and createdtimestamp BETWEEN (?6) AND (?7) )", nativeQuery = true)
	public List<Long> countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenOrderByBatchcodeDescInprogress(
			String orderflag, List<LSprojectmaster> lstproject, Integer approvelstatus, Integer approved,
			String orderflag1, Date fromdate, Date todate);

	public long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetween(String orderflag,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate);
	
	public long countByOrderflagAndLsprojectmasterInOrFiletypeAndCreatedtimestampBetween(String orderflag,
			List<LSprojectmaster> lstproject, Integer filetype, Date fromdate, Date todate);

	public List<Logilaborders> findByOrderByBatchcodeDesc();

//	public List<Logilaborders> findByCreatedtimestampBetweenOrderByBatchcodeDesc(Date fromdate, Date todate);

	public List<Logilaborders> findByCreatedtimestampBeforeOrderByBatchcodeDesc(Date todate);

	public List<Logilabordermaster> findByLsprojectmasterInAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	public List<Logilaborders> findByLssamplefileInAndFiletypeOrderByBatchcodeDesc(List<LSsamplefile> lssamplefile,
			Integer filetype);

	public List<Logilaborders> findByOrderflagAndFiletypeAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			String orderflag, Integer filetype, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	public List<Logilaborders> findByLsuserMasterAndFiletypeAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
			LSuserMaster user, Integer filetype, LSuserMaster assignedto, Date fromdate, Date todate);

	public List<Logilaborders> findByOrderflagAndFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
			String flag, Integer filetype, LSuserMaster user, LSuserMaster assignedto, Date fromdate, Date todate);

	public List<Logilaborders> findByAssignedtoAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			LSuserMaster assignedto, Integer filetype, Date fromdate, Date todate);

	public List<Logilaborders> findByOrderflagAndFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, Integer filetype, LSuserMaster lsuserMaster, Date fromdate, Date todate);

	public List<Logilaborders> findByOrderflagAndFiletypeAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			String orderflag, Integer filetype, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	public List<Logilaborders> findByOrderflagAndFiletypeAndCreatedtimestampBetweenAndLssamplefileIn(String flag,
			Integer filetype, Date fromdate, Date todate, List<LSsamplefile> idList);

	public List<Logilaborders> findByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			String orderflag, Integer filetype, Date fromdate, Date todate);

	public List<Logilaborders> findByOrderflagAndFiletypeAndCompletedtimestampBetweenAndLssamplefileIn(String flag,
			Integer filetype, Date fromdate, Date todate, List<LSsamplefile> idList);

	public List<Logilaborders> findByOrderflagAndFiletypeAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			String orderflag, Integer filetype, Date fromdate, Date todate);

	public List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenOrderByBatchcodeDesc(String orderflag,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	public List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndApprovelstatusAndApprovedAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Integer approvelstatus, Integer approved, Date fromdate,
			Date todate);
	
	public long countByOrderflagAndLsprojectmasterInAndApprovelstatusAndApprovedAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Integer approvelstatus, Integer approved, Date fromdate,
			Date todate);
	
	public List<Logilaborders> findByOrderflagAndLssamplefileInAndCreatedtimestampBetweenOrderByBatchcodeDesc(String orderflag,
			List<LSsamplefile> lssamplefile, Date fromdate, Date todate,Pageable pageable);
	
	public List<Logilaborders> findByOrderflagAndCreatedtimestampBetweenOrderByBatchcodeDesc(String orderflag, Date fromdate, Date todate);

	public List<Logilaborders> findByOrderflagAndApprovelstatusAndApprovedAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String orderflag, Integer approvelstatus, Integer approved, Date fromdate,
			Date todate);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndLsprojectmasterInOrderByBatchcodeAsc(int i,
			String string, List<LSprojectmaster> lstproject);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagOrderByBatchcodeAsc(int i, String string);
	
    public Logilaborders findByBatchcode(Long batchcode);
    
    public Logilabordermaster findByBatchcodeOrderByBatchcodeAsc(Long batchcode);
    
	public List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndLsworkflowInAndCreatedtimestampBetween(
			String orderflag, List<LSprojectmaster> lstproject, List<LSworkflow> lsworkflow, Date fromdate, Date todate);
	
	List<Logilabordermaster> findByOrderflagAndLssamplefileInAndCreatedtimestampBetween(String string,
			List<LSsamplefile> lssamplefile, Date fromdate, Date todate);

	List<Logilabordermaster> findByLsprojectmasterInAndCreatedtimestampBetween(List<LSprojectmaster> lstproject,
			Date fromdate, Date todate);
	
	public List<Logilabordermaster> findFirst20ByBatchcodeLessThanOrderByBatchcodeDesc(Long batchcode);
	
	public List<Logilabordermaster> findFirst20ByBatchcodeLessThanAndLsprojectmasterInOrderByBatchcodeDesc(Long batchcode, List<LSprojectmaster> lstproject);
	
	public List<Logilabordermaster> findFirst20ByOrderByBatchcodeDesc();
	
	public List<Logilabordermaster> findFirst20ByLsprojectmasterInOrderByBatchcodeDesc(List<LSprojectmaster> lstproject);

	public Long countByLsprojectmasterIn(List<LSprojectmaster> lstproject);
	
	public List<LSlogilablimsorderdetail> findByLsprojectmasterOrderByBatchcodeDesc(LSprojectmaster lsproject);
	
	public Integer deleteByLsprojectmaster(LSprojectmaster lsproject);

	List<LSlogilablimsorderdetail> findByFiletypeAndApprovelstatusAndOrderflagOrderByBatchcodeDesc(int i, int j,
			String string);

	List<LSlogilablimsorderdetail> findByFiletypeAndApprovelstatusAndOrderflagAndLsprojectmasterInOrderByBatchcodeAsc(
			int i, int j, String string, List<LSprojectmaster> lstproject);


	List<LSlogilablimsorderdetail> findBybatchcode(Long batchcode);
	
	public List<Logilaborders> findByDirectorycodeAndCreatedtimestampBetweenOrderByBatchcodeDesc(Long directorycode, Date fromdate, Date todate);
	
	public List<Logilaborders> findByOrderflagOrderByBatchcodeDesc(String orderflag);

	@Transactional
	@Modifying
	@Query("update LSlogilablimsorderdetail o set o.directorycode = ?1 where o.batchcode = ?2")
	void updatedirectory(Long directorycode , Long batchcode);


	@Transactional
	@Modifying
	@Query("update LSlogilablimsorderdetail o set o.directorycode = ?1 where o.batchcode in (?2)")
	void updatedirectory(Long directorycode , List<Long> batchcode);
	
	@Transactional
	@Modifying
	@Query("update LSlogilablimsorderdetail o set o.directorycode = ?1 where o.directorycode = ?2")
	void updateparentdirectory(Long newdirectorycode , Long olddirectorycode);

	List<LSlogilablimsorderdetail> findByLssamplemaster(LSsamplemaster objClass);

	List<LSlogilablimsorderdetail> findByTestcode(Integer testcode);

	List<LSlogilablimsorderdetail> findByOrderflagAndAssignedtoAndLockeduserIsNotNullOrderByBatchcodeDesc(String string,
			LSuserMaster lSuserMaster);
	
	List<LSlogilablimsorderdetail> findByAssignedtoAndLockeduserIsNotNullOrderByBatchcodeDesc(LSuserMaster lSuserMaster);
	List<Logilaborders> findByAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(LSuserMaster lSuserMaster, Date fromdate, Date todate);
	List<LSlogilablimsorderdetail> findByAssignedtoAndLsuserMasterAndCreatedtimestampBetweenOrderByBatchcodeDesc(LSuserMaster lsselecteduser, LSuserMaster lsloginuser, Date fromdate, Date todate);

	List<LSlogilablimsorderdetail> findByOrderflagAndLockeduserIsNotNullAndAssignedtoIsNullOrderByBatchcodeDesc(
			String string);

	List<LSlogilablimsorderdetail> findByOrderflagAndLsprojectmasterInAndLsworkflowInAndLockeduserIsNotNullAndAssignedtoIsNullOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, List<LSworkflow> lstworkflow);
	

	List<Logilaborders> findByOrderdisplaytypeAndLsprojectmasterInAndTestcodeIsNotNullAndCreatedtimestampBetweenOrderByBatchcodeDesc(Integer orderdisplaytype, List<LSprojectmaster> lstproject, Date fromdate, Date todate);
	
	@Transactional
	@Modifying
	@Query(value = "select distinct LSlogilablimsorderdetail.lstestmasterlocal_testcode, LSlogilablimsorderdetail.lsprojectmaster_projectcode, CAST((select testname from lstestmasterlocal where testcode =  LSlogilablimsorderdetail.testcode) as varchar(10))as testname   from LSlogilablimsorderdetail as LSlogilablimsorderdetail"
			+ " where LSlogilablimsorderdetail.lstestmasterlocal_testcode is not null and LSlogilablimsorderdetail.lsprojectmaster_projectcode is not null and LSlogilablimsorderdetail.lsprojectmaster_projectcode in (?1)", nativeQuery = true)
	public ArrayList<List<Object>> getLstestmasterlocalByOrderdisplaytypeAndLsprojectmasterInAndTestcodeIsNotNull(List<Integer> lsprojectcode);

	
	List<Logilaborders> findByOrderdisplaytypeAndLssamplemasterInAndViewoptionAndTestcodeIsNotNullOrOrderdisplaytypeAndLsuserMasterAndViewoptionAndLssamplemasterInAndTestcodeIsNotNullAndCreatedtimestampBetweenOrderByBatchcodeDesc
	(Integer orderdisplaytype, List<LSsamplemaster> lstsample,Integer siteview,Integer orderdisplayuser,LSuserMaster lsloginuser,Integer userview,List<LSsamplemaster> lstsampleuser, Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findDistinctLstestmasterlocalByLsprojectmasterNotNullAndLstestmasterlocalNotNull();
	
	@Transactional
	@Modifying
	@Query(value = "select distinct LSlogilablimsorderdetail.lstestmasterlocal_testcode, LSlogilablimsorderdetail.lssamplemaster_samplecode,  CAST((select testname from lstestmasterlocal where testcode =  LSlogilablimsorderdetail.lstestmasterlocal_testcode) as varchar(10))as testname  from LSlogilablimsorderdetail as LSlogilablimsorderdetail"
			+ " where LSlogilablimsorderdetail.lstestmasterlocal_testcode is not null and LSlogilablimsorderdetail.lssamplemaster_samplecode is not null and LSlogilablimsorderdetail.lssamplemaster_samplecode in (?1)", nativeQuery = true)
	public  ArrayList<List<Object>>  getLstestmasterlocalByOrderdisplaytypeAndLSsamplemasterInAndTestcodeIsNotNull(List<Integer> lssamplecode);
		
	public List<Logilaborders> findByLssamplemasterAndViewoptionAndLstestmasterlocalAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndLstestmasterlocalAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			LSsamplemaster lssample,Integer siteview, LStestmasterlocal lstest,Integer displaytype, Date fromdate, Date todate,LSsamplemaster lsusersample,Integer userview,LSuserMaster lsloginuser, 
			LStestmasterlocal lstestuser,Integer displaytypeuser, Date fromdateuser, Date todateuser);
	
	public List<LSlogilablimsorderdetail> findByLssamplemasterAndViewoptionAndLstestmasterlocalAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			LSsamplemaster lssample,Integer siteview, LStestmasterlocal lstest,Integer displaytype, Date fromdate, Date todate);
	
	public List<Logilaborders> findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrderByBatchcodeDesc(Long directorycode,
			Integer siteview, Date fromdate, Date todate,Long directorycodeuser,Integer userview,LSuserMaster lsloginuser, Date fromdateuser, Date todateuser);
	
	public List<Logilaborders> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	List<Logilaborders> findByLsprojectmasterAndLstestmasterlocalAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			LSprojectmaster lsprojectmaster, LStestmasterlocal lstestmasterlocal, Integer filetype, Date fromdate,
			Date todate);

	List<Logilaborders> findByLsprojectmasterAndLstestmasterlocalAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			LSprojectmaster lsprojectmaster, LStestmasterlocal lstestmasterlocal, Date fromdate, Date todate);

	List<Logilaborders> findByLssamplemasterAndViewoptionAndLstestmasterlocalAndFiletypeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndLstestmasterlocalAndFiletypeAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			LSsamplemaster lssamplemaster, int i, LStestmasterlocal lstestmasterlocal, Integer filetype, int j,
			Date fromdate, Date todate, LSsamplemaster lssamplemaster2, int k, LSuserMaster lsuserMaster,
			LStestmasterlocal lstestmasterlocal2, Integer filetype2, int l, Date fromdate2, Date todate2);

	List<Logilaborders> findByOrderflagAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Integer filetype, Date fromdate, Date todate);

	List<Logilaborders> findByAssignedtoAndLsuserMasterAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			LSuserMaster lsselecteduser, LSuserMaster lsloginuser, Integer filetype, Date fromdate, Date todate);

	List<Logilaborders> findByDirectorycodeAndViewoptionAndFiletypeAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Long directorycode, int i, Integer filetype, Date fromdate, Date todate, Long directorycode2, int j,
			LSuserMaster createdby, Integer filetype2, Date fromdate2, Date todate2);

	List<Logilaborders> findByLsprojectmasterAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			LSprojectmaster lsprojectmaster, Integer filetype, Date fromdate, Date todate);
	@Transactional
	@Modifying
	@Query(value = "select batchcode from "
			+ "LSlogilablimsorderdetail where  orderflag = ?1 and createdtimestamp BETWEEN ?2 and ?3 and assignedto_usercode IS NULL ORDER BY batchcode DESC", nativeQuery = true)
	List<Long> getBatchcodeAndFlagandcreateddate(String orderflag, Date fromdate, Date todate);

	List<Logilaborders> findByOrderflagAndCreatedtimestampBetweenAndLssamplefileIn(String orderflag, Date fromdate,
			Date todate, List<LSsamplefile> idList);
	@Transactional
	@Modifying
	@Query(value = "select batchcode from "
			+ "LSlogilablimsorderdetail where createdtimestamp BETWEEN ?1 and ?2 and assignedto_usercode IS NULL ORDER BY batchcode DESC", nativeQuery = true)
	List<Long> getBatchcodeAndcreateddate(Date fromdate, Date todate);
	

	@Transactional
	@Modifying
	@Query(value = "select batchcode from "
			+ "LSlogilablimsorderdetail where filetype = ?1 and createdtimestamp BETWEEN ?2 and ?3 and assignedto_usercode IS NULL ORDER BY batchcode DESC", nativeQuery = true)
	List<Long> getBatchcodeonFiletypeAndcreateddate(Integer filetype, Date fromdate, Date todate);

	List<Logilaborders> findByFiletypeAndCreatedtimestampBetweenAndLssamplefileIn(Integer filetype, Date fromdate,
			Date todate, List<LSsamplefile> idList);

	List<Logilaborders> findByCreatedtimestampBetweenAndLssamplefileIn(Date fromdate, Date todate,
			List<LSsamplefile> idList);

	List<Logilaborders> findByOrderflagAndLsprojectmasterAndLstestmasterlocalAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String orderflag, LSprojectmaster lsprojectmaster, LStestmasterlocal lstestmasterlocal, Integer filetype,
			Date fromdate, Date todate);

	List<Logilaborders> findByOrderflagAndLsprojectmasterAndLstestmasterlocalAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String orderflag, LSprojectmaster lsprojectmaster, LStestmasterlocal lstestmasterlocal, Date fromdate,
			Date todate);

	List<Logilaborders> findByLsprojectmasterAndLstestmasterlocalAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String orderflag, LSprojectmaster lsprojectmaster, LStestmasterlocal lstestmasterlocal, Date fromdate,
			Date todate);

	List<Logilaborders> findByFiletypeAndLsprojectmasterAndLstestmasterlocalAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Integer filetype, LSprojectmaster lsprojectmaster, LStestmasterlocal lstestmasterlocal, Date fromdate,
			Date todate);

	List<Logilaborders> findByFiletypeAndLsprojectmasterAndLstestmasterlocalAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Integer filetype, String orderflag, LSprojectmaster lsprojectmaster, LStestmasterlocal lstestmasterlocal,
			Date fromdate, Date todate);

	List<Logilaborders> findByapprovelstatusAndLsprojectmasterAndLstestmasterlocalAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Integer approvelstatus, String orderflag, LSprojectmaster lsprojectmaster,
			LStestmasterlocal lstestmasterlocal, Date fromdate, Date todate);

	List<Logilaborders> findByLssamplemasterAndViewoptionAndLstestmasterlocalAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndLstestmasterlocalAndFiletypeAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			LSsamplemaster lssamplemaster, int i, LStestmasterlocal lstestmasterlocal, int j, Date fromdate,
			Date todate, LSsamplemaster lssamplemaster2, int k, LSuserMaster lsuserMaster,
			LStestmasterlocal lstestmasterlocal2, Integer filetype, int l, Date fromdate2, Date todate2);

	List<Logilaborders> findByLssamplemasterAndViewoptionAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			LSsamplemaster lssamplemaster, int i, int j, Date fromdate, Date todate, LSsamplemaster lssamplemaster2,
			int k, LSuserMaster lsuserMaster, int l, Date fromdate2, Date todate2);

	List<Logilaborders> findByOrderflagAndLssamplemasterAndViewoptionAndLstestmasterlocalAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndLstestmasterlocalAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String orderflag, LSsamplemaster lssamplemaster, int i, LStestmasterlocal lstestmasterlocal, int j,
			Date fromdate, Date todate, LSsamplemaster lssamplemaster2, int k, LSuserMaster lsuserMaster,
			LStestmasterlocal lstestmasterlocal2, int l, Date fromdate2, Date todate2);

	List<Logilaborders> findByLssamplemasterAndViewoptionAndFiletypeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndFiletypeAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			LSsamplemaster lssamplemaster, int i, Integer filetype, int j, Date fromdate, Date todate,
			LSsamplemaster lssamplemaster2, int k, LSuserMaster lsuserMaster, Integer filetype2, int l, Date fromdate2,
			Date todate2);

	List<Logilaborders> findByOrderflagAndApprovelstatusAndLssamplemasterAndViewoptionAndLstestmasterlocalAndFiletypeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndLstestmasterlocalAndFiletypeAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String orderflag, Integer approvelstatus, LSsamplemaster lssamplemaster, int i,
			LStestmasterlocal lstestmasterlocal, Integer filetype, int j, Date fromdate, Date todate,
			LSsamplemaster lssamplemaster2, int k, LSuserMaster lsuserMaster, LStestmasterlocal lstestmasterlocal2,
			Integer filetype2, int l, Date fromdate2, Date todate2);

	List<Logilaborders> findByOrderflagAndFiletypeAndLsprojectmasterAndLstestmasterlocalAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String orderflag, Integer filetype, LSprojectmaster lsprojectmaster, LStestmasterlocal lstestmasterlocal,
			Date fromdate, Date todate);

	List<Logilaborders> findByOrderflagAndApprovelstatusAndFiletypeAndLsprojectmasterAndLstestmasterlocalAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String orderflag, Integer approvelstatus, Integer filetype, LSprojectmaster lsprojectmaster,
			LStestmasterlocal lstestmasterlocal, Date fromdate, Date todate);

	List<Logilaborders> findByOrderflagAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			String orderflag, String orderflag2, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	List<Logilaborders> findByOrderflagAndApprovelstatusAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			String orderflag, Integer approvelstatus, List<LSprojectmaster> lstproject, Integer filetype, Date fromdate,
			Date todate);

	List<Logilaborders> findByLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
			LSuserMaster lsuserMaster, LSuserMaster lsuserMaster2, Date fromdate, Date todate);

	List<Logilaborders> findByLsuserMasterAndFiletypeAndOrderflagAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
			LSuserMaster lsuserMaster, Integer filetype, String orderflag, LSuserMaster lsuserMaster2, Date fromdate,
			Date todate);

	List<Logilaborders> findByLsuserMasterAndOrderflagAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
			LSuserMaster lsuserMaster, String orderflag, LSuserMaster lsuserMaster2, Date fromdate, Date todate);

	List<Logilaborders> findByAssignedtoAndFiletypeAndOrderflagAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			LSuserMaster lsuserMaster, Integer filetype, String orderflag, Date fromdate, Date todate);

	List<Logilaborders> findByAssignedtoAndOrderflagAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			LSuserMaster lsuserMaster, String orderflag, Date fromdate, Date todate);

	List<Logilaborders> findByOrderflagAndTestcodeAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			String orderflag, Integer testcode, List<LSprojectmaster> lstproject, Integer filetype, Date fromdate,
			Date todate);

	List<Logilaborders> findByOrderflagAndTestcodeAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			String orderflag, LStestmasterlocal lstestmasterlocal, List<LSprojectmaster> lstproject, Integer filetype,
			Date fromdate, Date todate);

	List<Logilaborders> findByOrderflagAndTestcodeAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			String orderflag, Integer testcode, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	List<Logilaborders> findByOrderflagAndTestcodeAndApprovelstatusAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			String orderflag, Integer testcode, Integer approvelstatus, List<LSprojectmaster> lstproject,
			Integer filetype, Date fromdate, Date todate);

	List<Logilaborders> findByOrderflagAndLsprojectmasterInAndFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Integer filetype, LSprojectmaster lsprojectmaster,
			Date fromdate, Date todate);

	List<Logilaborders> findByOrderflagAndTestcodeAndLsprojectmasterInAndFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			String orderflag, Integer testcode, List<LSprojectmaster> lstproject, Integer filetype,
			LSprojectmaster lsprojectmaster, Date fromdate, Date todate);



	List<Logilabordermaster> findFirst20ByLsprojectmasterInOrDirectorycodeInOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, List<Long> directorycode);

	List<Logilabordermaster> findFirst20ByBatchcodeLessThanAndLsprojectmasterInOrBatchcodeLessThanAndDirectorycodeInOrderByBatchcodeDesc(
			Long batchcode, List<LSprojectmaster> lstproject, Long batchcode2, List<Long> directorycode);

	List<Logilaborders> findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInOrderByBatchcodeDesc(
			Long directorycode, int i, Date fromdate, Date todate, Long directorycode2, int j, LSuserMaster createdby,
			Date fromdate2, Date todate2, Long directorycode3, int k, Date fromdate3, Date todate3,
			List<LSuserMaster> lstuserMaster);

	List<Logilaborders> findByDirectorycodeAndViewoptionAndFiletypeAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndFiletypeAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndFiletypeAndCreatedtimestampBetweenAndLsuserMasterInOrderByBatchcodeDesc(
			Long directorycode, int i, Integer filetype, Date fromdate, Date todate, Long directorycode2, int j,
			LSuserMaster createdby, Integer filetype2, Date fromdate2, Date todate2, Long directorycode3, int k,
			Integer filetype3, Date fromdate3, Date todate3, List<LSuserMaster> lstuserMaster);

	List<LSlogilablimsorderdetail> findByLsprojectmaster(LSprojectmaster objClass);


	List<Logilaborders> findByLssamplemasterAndViewoptionAndLstestmasterlocalAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			LSsamplemaster lssamplemaster, int i, LStestmasterlocal lstestmasterlocal, int j, Date fromdate,
			Date todate, LSsamplemaster lssamplemaster2, int k, LSuserMaster lsuserMaster, int l, Date fromdate2,
			Date todate2);

	long countByOrderflagAndApprovelstatusNotAndOrdercancellNotAndLsprojectmasterInAndCreatedtimestampBetween(
			String string, int i, int j, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	long countByOrdercancellAndApprovelstatusNotAndLsprojectmasterInAndCreatedtimestampBetween(int i, int j,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	long countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetween(int i, List<LSprojectmaster> lstproject,
			Date fromdate, Date todate);

	long countByOrderflagAndApprovelstatusNotOrApprovelstatusIsNullAndOrdercancellIsNullAndLsprojectmasterInAndCreatedtimestampBetween(
			String string, int i, int j, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	long countByOrderflagAndOrdercancellIsNullAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotOrApprovelstatusIsNull(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i);

	long countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetween(int i, List<LSprojectmaster> lstproject,
			Date fromdate, Date todate);

	long countByOrderflagAndOrdercancellIsNullAndCreatedtimestampBetweenAndApprovelstatusNotOrApprovelstatusIsNull(
			String string, Date fromdate, Date todate, int i);
	
	long countByOrderflagAndOrdercancellIsNullAndCreatedtimestampBetween(
			String string, Date fromdate, Date todate);

	List<Logilabordermaster> findByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenOrderByBatchcodeDesc(String string, int i,
			Date fromdate, Date todate);

	List<Logilabordermaster> findByOrderflagAndCreatedtimestampBetweenAndApprovelstatusNotOrApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
			String string, Date fromdate, Date todate, int i);

	List<Logilabordermaster> findByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullOrderByBatchcodeDesc(
			String string, int i, Date fromdate, Date todate);

	List<Logilabordermaster> findByCreatedtimestampBetweenAndApprovelstatusOrderByBatchcodeDesc(Date fromdate, Date todate, int i,Pageable pageable);

	List<Logilabordermaster> findByCreatedtimestampBetweenAndOrdercancellOrderByBatchcodeDesc(Date fromdate, Date todate, int i,Pageable pageable);

	List<Logilabordermaster> findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetween(int i,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	List<Logilabordermaster> findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetween(int i,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovedIsNullOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate);



	long countByOrderflagAndFiletypeAndCreatedtimestampBetween(String string, int i, Date fromdate, Date todate);

	long countByOrderflagAndFiletypeAndCreatedtimestampBetweenAndApprovelstatusNotOrApprovelstatusIsNull(String string,
			int i, Date fromdate, Date todate, int j);

//	List<Logilabordermaster> findByOrderflagAndFiletypeAndCreatedtimestampBetween(String string, int i,
//			Date fromdate, Date todate);

	List<Logilabordermaster> findByFiletypeAndCreatedtimestampBetween(int i, Date fromdate,
			Date todate);
	
	long countByOrderflagAndOrdercancellIsNullAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNull(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNot(String string,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i);

	long countByOrderflagAndFiletypeAndCreatedtimestampBetweenAndApprovelstatusNot(String string, int i, Date fromdate,
			Date todate, int j);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNull(String string,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	long countByLsrepositoriesdataAndOrderflag(Lsrepositoriesdata lsrepositoriesdata, String string);

	long countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNull(String string, Date fromdate, Date todate);

	List<Logilabordermaster> findByOrderflagAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrderByBatchcodeDesc(
			String string, Date fromdate, Date todate, int i);

//	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullOrderByBatchcodeDesc(
//			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNull(String string,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate);



	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, String string2,
			List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2);

	List<Logilabordermaster> findByLsprojectmasterInAndCreatedtimestampBetweenOrFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Date fromdate2, Date todate2,
			Pageable pageable);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, String string2,
			List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2, String string3, int j, Date fromdate3,
			Date todate3, Pageable pageable);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, String string2, int i,
			Date fromdate2, Date todate2, Pageable pageable);

	List<Logilabordermaster> findByOrderflagAndLssamplefileInAndCreatedtimestampBetween(String string,
			List<LSsamplefile> lssamplefile, Date fromdate, Date todate, Pageable pageable);

	List<Logilabordermaster> findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetween(int i,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Pageable pageable);

	List<Logilabordermaster> findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetween(int i,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Pageable pageable);

	List<Logilabordermaster> findByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, int i, Date fromdate, Date todate, String string2, int j, Date fromdate2, Date todate2,
			Pageable pageable);


//	long countByLsprojectmasterInAndCreatedtimestampBetweenOrFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//			List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Date fromdate2, Date todate2);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, String string2,
			List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2, String string3, int j, Date fromdate3,
			Date todate3);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, String string2, int i,
			Date fromdate2, Date todate2);

	Object findByCreatedtimestampBetweenOrderByBatchcodeDesc(Date fromdate, Date todate);

	long countByCreatedtimestampBetweenOrderByBatchcodeDesc(Date fromdate, Date todate);

	long countByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, int i, Date fromdate, Date todate, String string2, int j, Date fromdate2, Date todate2);

	long countByOrderflagAndLssamplefileInAndCreatedtimestampBetweenOrderByBatchcodeDesc(String string,
			List<LSsamplefile> lssamplefile, Date fromdate, Date todate);

	long countByCreatedtimestampBetweenAndApprovelstatusOrderByBatchcodeDesc(Date fromdate, Date todate, int i
			);

	long countByCreatedtimestampBetweenAndOrdercancellOrderByBatchcodeDesc(Date fromdate, Date todate, int i);

	List<LSlogilablimsorderdetail> findByCreatedtimestampBetweenOrderByBatchcodeDesc(Date fromdate, Date todate,
			Pageable pageable);

	long countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, Date fromdate, Date todate, String string2, int i, Date fromdate2, Date todate2);

	List<Logilaborders> findByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndCreatedtimestampBetweenAndFiletypeOrderByBatchcodeDesc(
			String string, Date fromdate, Date todate, String string2, Date fromdate2, Date todate2, int i,
			Pageable pageable);


	List<Logilabordermaster> findByLsprojectmasterAndCreatedtimestampBetweenOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, int i,
			LSprojectmaster lstprojectforfilter2, Date fromdate2, Date todate2, Pageable pageable);

	long countByLsprojectmasterAndCreatedtimestampBetweenOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, int i,
			LSprojectmaster lstprojectforfilter2, Date fromdate2, Date todate2);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Integer testcode,
			String string2, List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2, Integer testcode2,
			String string3, int j, Date fromdate3, Date todate3, Integer testcode3, Pageable pageable);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Integer testcode,
			String string2, List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2, Integer testcode2,
			String string3, int j, Date fromdate3, Date todate3, Integer testcode3);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
			String string, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, int i, String string2,
			LSprojectmaster lstprojectforfilter2, Date fromdate2, Date todate2, String string3, int j, Date fromdate3,
			Date todate3, LSprojectmaster lstprojectforfilter3, Pageable pageable);

	long countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
			String string, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, int i, String string2,
			LSprojectmaster lstprojectforfilter2, Date fromdate2, Date todate2, String string3, int j, Date fromdate3,
			Date todate3, LSprojectmaster lstprojectforfilter3);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode,
			String string2, int i, Date fromdate2, Date todate2, Integer testcode2, Pageable pageable);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode,
			String string2, int i, Date fromdate2, Date todate2, Integer testcode2);

	long countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
			String string, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, String string2, int i,
			Date fromdate2, Date todate2, LSprojectmaster lstprojectforfilter2);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
			String string, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, String string2, int i,
			Date fromdate2, Date todate2, LSprojectmaster lstprojectforfilter2, Pageable pageable);

	List<Logilabordermaster> findByOrderflagAndLssamplefileInAndCreatedtimestampBetweenAndTestcode(String string,
			List<LSsamplefile> lssamplefile, Date fromdate, Date todate, Integer testcode, Pageable pageable);

	long countByOrderflagAndLssamplefileInAndCreatedtimestampBetweenAndTestcode(String string,
			List<LSsamplefile> lssamplefile, Date fromdate, Date todate, Integer testcode);

	List<Logilabordermaster> findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcode(int i,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode, Pageable pageable);

	long countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcode(int i,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode);

	List<Logilabordermaster> findByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetween(int i,
			LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, Pageable pageable);

	long countByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetween(int i, LSprojectmaster lstprojectforfilter,
			Date fromdate, Date todate);

	List<Logilabordermaster> findByLsprojectmasterAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
			LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, Integer testcode, int i,
			LSprojectmaster lstprojectforfilter2, Date fromdate2, Date todate2, Integer testcode2, Pageable pageable);

	long countByLsprojectmasterAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
			LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, Integer testcode, int i,
			LSprojectmaster lstprojectforfilter2, Date fromdate2, Date todate2, Integer testcode2);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
			String string, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, int i, Integer testcode,
			String string2, LSprojectmaster lstprojectforfilter2, Date fromdate2, Date todate2, Integer testcode2,
			String string3, int j, Date fromdate3, Date todate3, LSprojectmaster lstprojectforfilter3,
			Integer testcode3, Pageable pageable);

	long countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
			String string, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, int i, Integer testcode,
			String string2, LSprojectmaster lstprojectforfilter2, Date fromdate2, Date todate2, Integer testcode2,
			String string3, int j, Date fromdate3, Date todate3, LSprojectmaster lstprojectforfilter3,
			Integer testcode3);

	List<Logilabordermaster> findByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetweenAndTestcode(int i,
			LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, Integer testcode, Pageable pageable);

	long countByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetweenAndTestcode(int i,
			LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, Integer testcode);

	List<Logilabordermaster> findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcode(int i,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode, Pageable pageable);

	long countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcode(int i,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode);

	List<Logilabordermaster> findByOrdercancellAndLsprojectmasterAndCreatedtimestampBetween(int i,
			LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, Pageable pageable);

	long countByOrdercancellAndLsprojectmasterAndCreatedtimestampBetween(int i, LSprojectmaster lstprojectforfilter,
			Date fromdate, Date todate);

	List<Logilabordermaster> findByOrdercancellAndLsprojectmasterAndCreatedtimestampBetweenAndTestcode(int i,
			LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, Integer testcode, Pageable pageable);

	long countByOrdercancellAndLsprojectmasterAndCreatedtimestampBetweenAndTestcode(int i,
			LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, Integer testcode);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
			String string, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, Integer testcode,
			String string2, int i, Date fromdate2, Date todate2, LSprojectmaster lstprojectforfilter2,
			Integer testcode2, Pageable pageable);


	List<LSlogilablimsorderdetail> findByCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(Date fromdate,
			Date todate, Integer testcode, Pageable pageable);

	long countByCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(Date fromdate, Date todate, Integer testcode);

	List<LSlogilablimsorderdetail> findByCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(Date fromdate,
			Date todate, LSprojectmaster lstprojectforfilter, Pageable pageable);

	long countByCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(Date fromdate, Date todate,
			LSprojectmaster lstprojectforfilter);

	List<LSlogilablimsorderdetail> findByCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
			Date fromdate, Date todate, LSprojectmaster lstprojectforfilter, Integer testcode, Pageable pageable);

	long countByCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(Date fromdate, Date todate,
			LSprojectmaster lstprojectforfilter, Integer testcode);

	List<Logilabordermaster> findByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
			String string, int i, Date fromdate, Date todate, Integer testcode, String string2, int j, Date fromdate2,
			Date todate2, Integer testcode2, Pageable pageable);

	long countByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
			String string, int i, Date fromdate, Date todate, Integer testcode, String string2, int j, Date fromdate2,
			Date todate2, Integer testcode2);

	List<Logilabordermaster> findByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
			String string, int i, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter, String string2,
			int j, Date fromdate2, Date todate2, LSprojectmaster lstprojectforfilter2, Pageable pageable);

	long countByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
			String string, int i, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter, String string2,
			int j, Date fromdate2, Date todate2, LSprojectmaster lstprojectforfilter2);

	List<Logilabordermaster> findByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
			String string, int i, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter, Integer testcode,
			String string2, int j, Date fromdate2, Date todate2, LSprojectmaster lstprojectforfilter2,
			Integer testcode2, Pageable pageable);

	long countByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
			String string, int i, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter, Integer testcode,
			String string2, int j, Date fromdate2, Date todate2, LSprojectmaster lstprojectforfilter2,
			Integer testcode2);

	List<Logilabordermaster> findByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndCreatedtimestampBetweenAndFiletypeAndTestcodeOrderByBatchcodeDesc(
			String string, Date fromdate, Date todate, Integer testcode, String string2, Date fromdate2, Date todate2,
			int i, Integer testcode2, Pageable pageable);

	long countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
			String string, Date fromdate, Date todate, Integer testcode, String string2, int i, Date fromdate2,
			Date todate2, Integer testcode2);

	List<Logilabordermaster> findByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterOrOrderflagAndCreatedtimestampBetweenAndFiletypeAndLsprojectmasterOrderByBatchcodeDesc(
			String string, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter, String string2,
			Date fromdate2, Date todate2, int i, LSprojectmaster lstprojectforfilter2, Pageable pageable);

	long countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
			String string, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter, String string2, int i,
			Date fromdate2, Date todate2, LSprojectmaster lstprojectforfilter2);

	List<Logilabordermaster> findByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterAndTestcodeOrOrderflagAndCreatedtimestampBetweenAndFiletypeAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
			String string, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter, Integer testcode,
			String string2, Date fromdate2, Date todate2, int i, LSprojectmaster lstprojectforfilter2,
			Integer testcode2, Pageable pageable);

	long countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
			String string, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter, Integer testcode,
			String string2, int i, Date fromdate2, Date todate2, LSprojectmaster lstprojectforfilter2,
			Integer testcode2);

	List<Logilabordermaster> findByCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(Date fromdate, Date todate,
			int i, Integer testcode, Pageable pageable);

	long countByCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(Date fromdate, Date todate,
			int i, Integer testcode);

	List<Logilabordermaster> findByCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterOrderByBatchcodeDesc(Date fromdate,
			Date todate, int i, LSprojectmaster lstprojectforfilter, Pageable pageable);

	long countByCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterOrderByBatchcodeDesc(Date fromdate,
			Date todate, int i, LSprojectmaster lstprojectforfilter);

	List<Logilabordermaster> findByCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
			Date fromdate, Date todate, int i, LSprojectmaster lstprojectforfilter, Integer testcode,
			Pageable pageable);

	long countByCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(Date fromdate,
			Date todate, int i, LSprojectmaster lstprojectforfilter, Integer testcode);

	List<Logilabordermaster> findByCreatedtimestampBetweenAndOrdercancellAndTestcodeOrderByBatchcodeDesc(Date fromdate, Date todate,
			int i, Integer testcode, Pageable pageable);

	long countByCreatedtimestampBetweenAndOrdercancellAndTestcodeOrderByBatchcodeDesc(Date fromdate, Date todate, int i,
			Integer testcode);

	List<Logilabordermaster> findByCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterOrderByBatchcodeDesc(Date fromdate,
			Date todate, int i, LSprojectmaster lstprojectforfilter, Pageable pageable);

	long countByCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterOrderByBatchcodeDesc(Date fromdate, Date todate,
			int i, LSprojectmaster lstprojectforfilter);

	List<Logilabordermaster> findByCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(Date fromdate,
			Date todate, int i, LSprojectmaster lstprojectforfilter, Integer testcode, Pageable pageable);

	long countByCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(Date fromdate,
			Date todate, int i, LSprojectmaster lstprojectforfilter, Integer testcode);

	long countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
			String string, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, Integer testcode,
			String string2, int i, Date fromdate2, Date todate2, LSprojectmaster lstprojectforfilter2,
			Integer testcode2);

	long countByCreatedtimestampBetweenAndTestcode(Date fromdate, Date todate, Integer testcode);

	long countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcode(String string, Date fromdate,
			Date todate, Integer testcode);

	long countByOrderflagAndCompletedtimestampBetweenAndApprovelstatusNotAndTestcode(String string, Date fromdate,
			Date todate, int i, Integer testcode);

	long countByOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcode(String string, int i, Date fromdate,
			Date todate, Integer testcode);

	long countByApprovelstatusAndCreatedtimestampBetweenAndTestcode(int i, Date fromdate, Date todate,
			Integer testcode);

	long countByOrdercancellAndCreatedtimestampBetweenAndTestcode(int i, Date fromdate, Date todate,
			Integer testcode);

	long countByCreatedtimestampBetweenAndLsprojectmaster(Date fromdate, Date todate,
			LSprojectmaster lstprojectforfilter);

	long countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmaster(String string,
			Date fromdate, Date todate, LSprojectmaster lstprojectforfilter);

	long countByOrderflagAndCompletedtimestampBetweenAndApprovelstatusNotAndLsprojectmaster(String string,
			Date fromdate, Date todate, int i, LSprojectmaster lstprojectforfilter);

	long countByOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmaster(String string, int i, Date fromdate,
			Date todate, LSprojectmaster lstprojectforfilter);

	long countByApprovelstatusAndCreatedtimestampBetweenAndLsprojectmaster(int i, Date fromdate, Date todate,
			LSprojectmaster lstprojectforfilter);

	long countByOrdercancellAndCreatedtimestampBetweenAndLsprojectmaster(int i, Date fromdate, Date todate,
			LSprojectmaster lstprojectforfilter);

	long countByOrderflagAndLssamplefileInAndCreatedtimestampBetweenAndLsprojectmaster(String string,
			List<LSsamplefile> lssamplefile, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter);

	long countByCreatedtimestampBetweenAndLsprojectmasterAndTestcode(Date fromdate, Date todate,
			LSprojectmaster lstprojectforfilter, Integer testcode);

	long countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterAndTestcode(String string,
			Date fromdate, Date todate, LSprojectmaster lstprojectforfilter, Integer testcode);

	long countByOrderflagAndCompletedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterAndTestcode(String string,
			Date fromdate, Date todate, int i, LSprojectmaster lstprojectforfilter, Integer testcode);

	long countByOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(String string, int i,
			Date fromdate, Date todate, LSprojectmaster lstprojectforfilter, Integer testcode);

	long countByApprovelstatusAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(int i, Date fromdate,
			Date todate, LSprojectmaster lstprojectforfilter, Integer testcode);

	long countByOrdercancellAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(int i, Date fromdate, Date todate,
			LSprojectmaster lstprojectforfilter, Integer testcode);

	long countByOrderflagAndLssamplefileInAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(String string,
			List<LSsamplefile> lssamplefile, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter,
			Integer testcode);

	long countByLsprojectmasterInAndCreatedtimestampBetweenOrFiletypeAndCreatedtimestampBetween(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Date fromdate2, Date todate2);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetween(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, String string2, int i,
			Date fromdate2, Date todate2);

	long countByOrderflagAndLsprojectmasterInAndApprovelstatusAndApprovedAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, int i, int j, Date fromdate, Date todate,
			Integer testcode);



	long countByOrderflagAndLsprojectmasterAndApprovelstatusAndApprovedAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, LSprojectmaster lstprojectforfilter, int i, int j, Date fromdate, Date todate);



	List<Logilaborders> findByDirectorycodeAndViewoptionAndFiletypeAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndFiletypeAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Long directorycode, int i, Integer filetype, Date fromdate, Date todate, Long directorycode2, int j,
			LSuserMaster createdby, Integer filetype2, Date fromdate2, Date todate2, Long directorycode3, int k,
			LSuserMaster createdby2, Integer filetype3, Date fromdate3, Date todate3);

	List<Logilaborders> findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Long directorycode, int i, Date fromdate, Date todate, Long directorycode2, int j, LSuserMaster createdby,
			Date fromdate2, Date todate2, Long directorycode3, int k, LSuserMaster createdby2, Date fromdate3,
			Date todate3);


	List<Logilabordermaster> findByOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, int i, LSuserMaster objuser, Date fromdate, Date todate, String string2, int j,
			LSuserMaster objuser2, Date fromdate2, Date todate2, String string3, LSuserMaster objuser3,
			List<LSsamplemaster> lstsample, int k, Date fromdate3, Date todate3, String string4, LSuserMaster objuser4,
			List<LSsamplemaster> lstsample2, int l, Date fromdate4, Date todate4, Pageable pageable);


	List<Logilabordermaster> findByLsprojectmasterInAndCreatedtimestampBetweenOrFiletypeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Date fromdate2, Date todate2, int j,
			LSuserMaster objuser, Date fromdate3, Date todate3, int k, LSuserMaster objuser2, Date fromdate4,
			Date todate4, int l, LSuserMaster objuser3, Date fromdate5, Date todate5, List<LSprojectmaster> lstproject2,
			Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeAndLsprojectmasterInOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode, int i, Date fromdate2,
			Date todate2, Integer testcode2, int j, LSuserMaster objuser, Date fromdate3, Date todate3,
			Integer testcode3, int k, LSuserMaster objuser2, Date fromdate4, Date todate4, Integer testcode4, int l,
			LSuserMaster objuser3, Date fromdate5, Date todate5, Integer testcode5, List<LSprojectmaster> lstproject2,
			Pageable pageable);

	long countByLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode, int i, Date fromdate2,
			Date todate2, Integer testcode2, int j, LSuserMaster objuser, Date fromdate3, Date todate3,
			Integer testcode3, int k, LSuserMaster objuser2, Date fromdate4, Date todate4, Integer testcode4);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterInOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, String string2,
			List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2, String string3, int j, Date fromdate3,
			Date todate3, String string4, int k, LSuserMaster objuser, Date fromdate4, Date todate4, int l,
			String string5, int m, LSuserMaster objuser2, Date fromdate5, Date todate5, int n, String string6, int o,
			LSuserMaster objuser3, Date fromdate6, Date todate6, int p, List<LSprojectmaster> lstproject3,
			String string7, int q, LSuserMaster objuser4, Date fromdate7, Date todate7, String string8, int r,
			LSuserMaster objuser5, Date fromdate8, Date todate8, String string9, int s, LSuserMaster objuser6,
			Date fromdate9, Date todate9, List<LSprojectmaster> lstproject4, Pageable pageable);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterInOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, String string2,
			List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2, String string3, int j, Date fromdate3,
			Date todate3, String string4, int k, LSuserMaster objuser, Date fromdate4, Date todate4, int l,
			String string5, int m, LSuserMaster objuser2, Date fromdate5, Date todate5, int n, String string6, int o,
			LSuserMaster objuser3, Date fromdate6, Date todate6, int p, List<LSprojectmaster> lstproject3,
			String string7, int q, LSuserMaster objuser4, Date fromdate7, Date todate7, String string8, int r,
			LSuserMaster objuser5, Date fromdate8, Date todate8, String string9, int s, LSuserMaster objuser6,
			Date fromdate9, Date todate9, List<LSprojectmaster> lstproject4);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterInAndTestcodeOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Integer testcode,
			String string2, List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2, Integer testcode2,
			String string3, int j, Date fromdate3, Date todate3, Integer testcode3, String string4, int k,
			LSuserMaster objuser, Date fromdate4, Date todate4, int l, Integer testcode4, String string5, int m,
			LSuserMaster objuser2, Date fromdate5, Date todate5, int n, Integer testcode5, String string6, int o,
			LSuserMaster objuser3, Date fromdate6, Date todate6, int p, List<LSprojectmaster> lstproject3,
			Integer testcode6, String string7, int q, LSuserMaster objuser4, Date fromdate7, Date todate7,
			Integer testcode7, String string8, int r, LSuserMaster objuser5, Date fromdate8, Date todate8,
			Integer testcode8, String string9, int s, LSuserMaster objuser6, Date fromdate9, Date todate9,
			List<LSprojectmaster> lstproject4, Integer testcode9, Pageable pageable);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterInAndTestcodeOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Integer testcode,
			String string2, List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2, Integer testcode2,
			String string3, int j, Date fromdate3, Date todate3, Integer testcode3, String string4, int k,
			LSuserMaster objuser, Date fromdate4, Date todate4, int l, Integer testcode4, String string5, int m,
			LSuserMaster objuser2, Date fromdate5, Date todate5, int n, Integer testcode5, String string6, int o,
			LSuserMaster objuser3, Date fromdate6, Date todate6, int p, List<LSprojectmaster> lstproject3,
			Integer testcode6, String string7, int q, LSuserMaster objuser4, Date fromdate7, Date todate7,
			Integer testcode7, String string8, int r, LSuserMaster objuser5, Date fromdate8, Date todate8,
			Integer testcode8, String string9, int s, LSuserMaster objuser6, Date fromdate9, Date todate9,
			List<LSprojectmaster> lstproject4, Integer testcode9);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, String string2, int i,
			Date fromdate2, Date todate2, String string3, int j, LSuserMaster objuser, Date fromdate3, Date todate3,
			String string4, int k, LSuserMaster objuser2, Date fromdate4, Date todate4, String string5,
			List<LSprojectmaster> lstproject2, int l, LSuserMaster objuser3, Date fromdate5, Date todate5,
			Pageable pageable);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, String string2, int i,
			Date fromdate2, Date todate2, String string3, int j, LSuserMaster objuser, Date fromdate3, Date todate3,
			String string4, int k, LSuserMaster objuser2, Date fromdate4, Date todate4, String string5,
			List<LSprojectmaster> lstproject2, int l, LSuserMaster objuser3, Date fromdate5, Date todate5);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode,
			String string2, int i, Date fromdate2, Date todate2, Integer testcode2, String string3, int j,
			LSuserMaster objuser, Date fromdate3, Date todate3, Integer testcode3, String string4, int k,
			LSuserMaster objuser2, Date fromdate4, Date todate4, Integer testcode4, String string5,
			List<LSprojectmaster> lstproject2, int l, LSuserMaster objuser3, Date fromdate5, Date todate5,
			Integer testcode5, Pageable pageable);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode,
			String string2, int i, Date fromdate2, Date todate2, Integer testcode2, String string3, int j,
			LSuserMaster objuser, Date fromdate3, Date todate3, Integer testcode3, String string4, int k,
			LSuserMaster objuser2, Date fromdate4, Date todate4, Integer testcode4, String string5,
			List<LSprojectmaster> lstproject2, int l, LSuserMaster objuser3, Date fromdate5, Date todate5,
			Integer testcode5);


	long countByLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode, int i, Date fromdate2,
			Date todate2, Integer testcode2);



	List<Logilabordermaster> findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterInAndTestcode(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode, int j,
			LSuserMaster objuser, Date fromdate2, Date todate2, int k, Integer testcode2, int l, LSuserMaster objuser2,
			Date fromdate3, Date todate3, int m, Integer testcode3, int n, LSuserMaster objuser3, Date fromdate4,
			Date todate4, int o, List<LSprojectmaster> lstproject2, Integer testcode4, Pageable pageable);

	long countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterInAndTestcode(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode, int j,
			LSuserMaster objuser, Date fromdate2, Date todate2, int k, Integer testcode2, int l, LSuserMaster objuser2,
			Date fromdate3, Date todate3, int m, Integer testcode3, int n, LSuserMaster objuser3, Date fromdate4,
			Date todate4, int o, List<LSprojectmaster> lstproject2, Integer testcode4);

	List<Logilabordermaster> findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIn(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int j, LSuserMaster objuser,
			Date fromdate2, Date todate2, int k, int l, LSuserMaster objuser2, Date fromdate3, Date todate3, int m,
			int n, LSuserMaster objuser3, Date fromdate4, Date todate4, int o, int p, LSuserMaster objuser4,
			Date fromdate5, Date todate5, int q, List<LSprojectmaster> lstproject2, Pageable pageable);

	long countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIn(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int j, LSuserMaster objuser,
			Date fromdate2, Date todate2, int k, int l, LSuserMaster objuser2, Date fromdate3, Date todate3, int m,
			int n, LSuserMaster objuser3, Date fromdate4, Date todate4, int o, int p, LSuserMaster objuser4,
			Date fromdate5, Date todate5, int q, List<LSprojectmaster> lstproject2);

	List<Logilabordermaster> findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIn(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int j, LSuserMaster objuser,
			Date fromdate2, Date todate2, int k, int l, LSuserMaster objuser2, Date fromdate3, Date todate3, int m,
			int n, LSuserMaster objuser3, Date fromdate4, Date todate4, int o, List<LSprojectmaster> lstproject2,
			Pageable pageable);

	long countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIn(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int j, LSuserMaster objuser,
			Date fromdate2, Date todate2, int k, int l, LSuserMaster objuser2, Date fromdate3, Date todate3, int m,
			int n, LSuserMaster objuser3, Date fromdate4, Date todate4, int o, List<LSprojectmaster> lstproject2);

	List<Logilabordermaster> findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterInAndTestcode(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode, int j,
			LSuserMaster objuser, Date fromdate2, Date todate2, int k, Integer testcode2, int l, LSuserMaster objuser2,
			Date fromdate3, Date todate3, int m, Integer testcode3, int n, LSuserMaster objuser3, Date fromdate4,
			Date todate4, int o, Integer testcode4, int p, LSuserMaster objuser4, Date fromdate5, Date todate5, int q,
			List<LSprojectmaster> lstproject2, Integer testcode5, Pageable pageable);

	long countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterInAndTestcode(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode, int j,
			LSuserMaster objuser, Date fromdate2, Date todate2, int k, Integer testcode2, int l, LSuserMaster objuser2,
			Date fromdate3, Date todate3, int m, Integer testcode3, int n, LSuserMaster objuser3, Date fromdate4,
			Date todate4, int o, Integer testcode4, int p, LSuserMaster objuser4, Date fromdate5, Date todate5, int q,
			List<LSprojectmaster> lstproject2, Integer testcode5);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndApprovelstatusAndApprovedOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndApprovelstatusAndApprovedOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndApprovelstatusAndApprovedOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndApprovelstatusAndApprovedOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndApprovelstatusAndApprovedOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, int j, String string2,
			int k, Date fromdate2, Date todate2, int l, int m, String string3, int n, LSuserMaster objuser,
			Date fromdate3, Date todate3, int o, int p, String string4, int q, LSuserMaster objuser2, Date fromdate4,
			Date todate4, int r, int s, String string5, List<LSprojectmaster> lstproject2, int t, LSuserMaster objuser3,
			Date fromdate5, Date todate5, int u, int v);

	long countByLsprojectmasterInAndCreatedtimestampBetweenOrFiletypeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Date fromdate2, Date todate2, int j,
			LSuserMaster objuser, Date fromdate3, Date todate3, int k, LSuserMaster objuser2, Date fromdate4,
			Date todate4, int l, LSuserMaster objuser3, Date fromdate5, Date todate5,
			List<LSprojectmaster> lstproject2);



	List<Logilaborders> findByOrderflagAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			String orderflag, String orderflag2, List<LSprojectmaster> lstproject, Date fromdate, Date todate,
			String orderflag3, String orderflag4, List<LSsamplemaster> lstsample, Date fromdate2, Date todate2);

	List<Logilaborders> findByOrderflagAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Integer filetype, Date fromdate, Date todate,
			String orderflag2, List<LSsamplemaster> lstsample, Integer filetype2, Date fromdate2, Date todate2, int i,
			String orderflag3, List<LSsamplemaster> lstsample2, Integer filetype3, Date fromdate3, Date todate3, int j,
			LSuserMaster lsuserMaster);

	List<Logilaborders> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Date fromdate, Date todate, String orderflag2,
			List<LSsamplemaster> lstsample, Date fromdate2, Date todate2, int i, String orderflag3,
			List<LSsamplemaster> lstsample2, Date fromdate3, Date todate3, int j, LSuserMaster lsuserMaster);

	List<Logilaborders> findByOrderflagAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionOrOrderflagAndOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrderByBatchcodeDesc(
			String orderflag, String orderflag2, List<LSprojectmaster> lstproject, Date fromdate, Date todate,
			String orderflag3, String orderflag4, List<LSsamplemaster> lstsample, Date fromdate2, Date todate2, int i,
			String orderflag5, String orderflag6, List<LSsamplemaster> lstsample2, Date fromdate3, Date todate3, int j,
			LSuserMaster lsuserMaster);

	List<Logilaborders> findByOrderflagAndApprovelstatusAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndApprovelstatusAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionOrOrderflagAndApprovelstatusAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrderByBatchcodeDesc(
			String orderflag, Integer approvelstatus, List<LSprojectmaster> lstproject, Integer filetype, Date fromdate,
			Date todate, String orderflag2, Integer approvelstatus2, List<LSsamplemaster> lstsample, Integer filetype2,
			Date fromdate2, Date todate2, int i, String orderflag3, Integer approvelstatus3,
			List<LSsamplemaster> lstsample2, Integer filetype3, Date fromdate3, Date todate3, int j,
			LSuserMaster lsuserMaster);

	List<Logilaborders> findByOrderflagAndTestcodeAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndTestcodeAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionOrOrderflagAndTestcodeAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrderByBatchcodeDesc(
			String orderflag, Integer testcode, List<LSprojectmaster> lstproject, Integer filetype, Date fromdate,
			Date todate, String orderflag2, Integer testcode2, List<LSsamplemaster> lstsample, Integer filetype2,
			Date fromdate2, Date todate2, int i, String orderflag3, Integer testcode3, List<LSsamplemaster> lstsample2,
			Integer filetype3, Date fromdate3, Date todate3, int j, LSuserMaster lsuserMaster);

	List<Logilaborders> findByOrderflagAndLsprojectmasterInAndTestcodeAndFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndTestcodeAndLssamplemasterInAndFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionOrOrderflagAndLsprojectmasterIsNullAndTestcodeAndLssamplemasterInAndFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Integer testcode, Integer filetype,
			LSprojectmaster lsprojectmaster, Date fromdate, Date todate, String orderflag2, Integer testcode2,
			List<LSsamplemaster> lstsample, Integer filetype2, LSprojectmaster lsprojectmaster2, Date fromdate2,
			Date todate2, int i, String orderflag3, Integer testcode3, List<LSsamplemaster> lstsample2,
			Integer filetype3, LSprojectmaster lsprojectmaster3, Date fromdate3, Date todate3, int j,
			LSuserMaster lsuserMaster);

	List<Logilaborders> findByOrderflagAndLsprojectmasterInAndFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Integer filetype, LSprojectmaster lsprojectmaster,
			Date fromdate, Date todate, String orderflag2, List<LSsamplemaster> lstsample, Integer filetype2,
			LSprojectmaster lsprojectmaster2, Date fromdate2, Date todate2, int i, String orderflag3,
			List<LSsamplemaster> lstsample2, Integer filetype3, LSprojectmaster lsprojectmaster3, Date fromdate3,
			Date todate3, int j, LSuserMaster lsuserMaster);


	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, List<LSsamplemaster> lstsample2, int j,
			Date fromdate2, Date todate2, LSuserMaster objuser, int k, Date fromdate3, Date todate3,
			LSuserMaster objuser2, List<Long> directorycode, int l, Date fromdate4, Date todate4,
			List<Long> directorycode2, int m, Date fromdate5, Date todate5, LSuserMaster objuser3,
			List<Long> directorycode3, int n, Date fromdate6, Date todate6, LSuserMaster objuser4);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, List<LSsamplemaster> lstsample2, int j,
			Date fromdate2, Date todate2, LSuserMaster objuser, int k, Date fromdate3, Date todate3,
			LSuserMaster objuser2, List<Long> directorycode, int l, Date fromdate4, Date todate4,
			List<Long> directorycode2, int m, Date fromdate5, Date todate5, LSuserMaster objuser3,
			List<Long> directorycode3, int n, Date fromdate6, Date todate6, LSuserMaster objuser4, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, Integer testcode,
			List<LSsamplemaster> lstsample2, int j, Date fromdate2, Date todate2, LSuserMaster objuser,
			Integer testcode2, int k, Date fromdate3, Date todate3, LSuserMaster objuser2, Integer testcode3,
			List<Long> directorycode, int l, Date fromdate4, Date todate4, Integer testcode4, List<Long> directorycode2,
			int m, Date fromdate5, Date todate5, LSuserMaster objuser3, Integer testcode5, List<Long> directorycode3,
			int n, Date fromdate6, Date todate6, LSuserMaster objuser4, Integer testcode6, Pageable pageable);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, Integer testcode,
			List<LSsamplemaster> lstsample2, int j, Date fromdate2, Date todate2, LSuserMaster objuser,
			Integer testcode2, int k, Date fromdate3, Date todate3, LSuserMaster objuser2, Integer testcode3,
			List<Long> directorycode, int l, Date fromdate4, Date todate4, Integer testcode4, List<Long> directorycode2,
			int m, Date fromdate5, Date todate5, LSuserMaster objuser3, Integer testcode5, List<Long> directorycode3,
			int n, Date fromdate6, Date todate6, LSuserMaster objuser4, Integer testcode6);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String string, int j,
			List<LSsamplemaster> lstsample2, int k, Date fromdate2, Date todate2, LSuserMaster objuser, String string2,
			int l, int m, Date fromdate3, Date todate3, LSuserMaster objuser2, String string3, int n,
			List<Long> directorycode, int o, Date fromdate4, Date todate4, String string4, int p,
			List<Long> directorycode2, int q, Date fromdate5, Date todate5, LSuserMaster objuser3, String string5,
			int r, List<Long> directorycode3, int s, Date fromdate6, Date todate6, LSuserMaster objuser4,
			String string6, int t, Pageable pageable);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String string, int j,
			List<LSsamplemaster> lstsample2, int k, Date fromdate2, Date todate2, LSuserMaster objuser, String string2,
			int l, int m, Date fromdate3, Date todate3, LSuserMaster objuser2, String string3, int n,
			List<Long> directorycode, int o, Date fromdate4, Date todate4, String string4, int p,
			List<Long> directorycode2, int q, Date fromdate5, Date todate5, LSuserMaster objuser3, String string5,
			int r, List<Long> directorycode3, int s, Date fromdate6, Date todate6, LSuserMaster objuser4,
			String string6, int t);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String string, int j, Integer testcode,
			List<LSsamplemaster> lstsample2, int k, Date fromdate2, Date todate2, LSuserMaster objuser, String string2,
			int l, Integer testcode2, int m, Date fromdate3, Date todate3, LSuserMaster objuser2, String string3, int n,
			Integer testcode3, List<Long> directorycode, int o, Date fromdate4, Date todate4, String string4, int p,
			Integer testcode4, List<Long> directorycode2, int q, Date fromdate5, Date todate5, LSuserMaster objuser3,
			String string5, int r, Integer testcode5, List<Long> directorycode3, int s, Date fromdate6, Date todate6,
			LSuserMaster objuser4, String string6, int t, Integer testcode6, Pageable pageable);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String string, int j, Integer testcode,
			List<LSsamplemaster> lstsample2, int k, Date fromdate2, Date todate2, LSuserMaster objuser, String string2,
			int l, Integer testcode2, int m, Date fromdate3, Date todate3, LSuserMaster objuser2, String string3, int n,
			Integer testcode3, List<Long> directorycode, int o, Date fromdate4, Date todate4, String string4, int p,
			Integer testcode4, List<Long> directorycode2, int q, Date fromdate5, Date todate5, LSuserMaster objuser3,
			String string5, int r, Integer testcode5, List<Long> directorycode3, int s, Date fromdate6, Date todate6,
			LSuserMaster objuser4, String string6, int t, Integer testcode6);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String string,
			List<LSsamplemaster> lstsample2, int j, Date fromdate2, Date todate2, LSuserMaster objuser, String string2,
			int k, Date fromdate3, Date todate3, LSuserMaster objuser2, String string3, List<Long> directorycode, int l,
			Date fromdate4, Date todate4, String string4, List<Long> directorycode2, int m, Date fromdate5,
			Date todate5, LSuserMaster objuser3, String string5, List<Long> directorycode3, int n, Date fromdate6,
			Date todate6, LSuserMaster objuser4, String string6, Pageable pageable);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String string,
			List<LSsamplemaster> lstsample2, int j, Date fromdate2, Date todate2, LSuserMaster objuser, String string2,
			int k, Date fromdate3, Date todate3, LSuserMaster objuser2, String string3, List<Long> directorycode, int l,
			Date fromdate4, Date todate4, String string4, List<Long> directorycode2, int m, Date fromdate5,
			Date todate5, LSuserMaster objuser3, String string5, List<Long> directorycode3, int n, Date fromdate6,
			Date todate6, LSuserMaster objuser4, String string6);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String string, Integer testcode,
			List<LSsamplemaster> lstsample2, int j, Date fromdate2, Date todate2, LSuserMaster objuser, String string2,
			Integer testcode2, int k, Date fromdate3, Date todate3, LSuserMaster objuser2, String string3,
			Integer testcode3, List<Long> directorycode, int l, Date fromdate4, Date todate4, String string4,
			Integer testcode4, List<Long> directorycode2, int m, Date fromdate5, Date todate5, LSuserMaster objuser3,
			String string5, Integer testcode5, List<Long> directorycode3, int n, Date fromdate6, Date todate6,
			LSuserMaster objuser4, String string6, Integer testcode6, Pageable pageable);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String string, Integer testcode,
			List<LSsamplemaster> lstsample2, int j, Date fromdate2, Date todate2, LSuserMaster objuser, String string2,
			Integer testcode2, int k, Date fromdate3, Date todate3, LSuserMaster objuser2, String string3,
			Integer testcode3, List<Long> directorycode, int l, Date fromdate4, Date todate4, String string4,
			Integer testcode4, List<Long> directorycode2, int m, Date fromdate5, Date todate5, LSuserMaster objuser3,
			String string5, Integer testcode5, List<Long> directorycode3, int n, Date fromdate6, Date todate6,
			LSuserMaster objuser4, String string6, Integer testcode6);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, int j, List<LSsamplemaster> lstsample2,
			int k, Date fromdate2, Date todate2, LSuserMaster objuser, int l, int m, Date fromdate3, Date todate3,
			LSuserMaster objuser2, int n, List<Long> directorycode, int o, Date fromdate4, Date todate4, int p,
			List<Long> directorycode2, int q, Date fromdate5, Date todate5, LSuserMaster objuser3, int r,
			List<Long> directorycode3, int s, Date fromdate6, Date todate6, LSuserMaster objuser4, int t,
			Pageable pageable);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, int j, List<LSsamplemaster> lstsample2,
			int k, Date fromdate2, Date todate2, LSuserMaster objuser, int l, int m, Date fromdate3, Date todate3,
			LSuserMaster objuser2, int n, List<Long> directorycode, int o, Date fromdate4, Date todate4, int p,
			List<Long> directorycode2, int q, Date fromdate5, Date todate5, LSuserMaster objuser3, int r,
			List<Long> directorycode3, int s, Date fromdate6, Date todate6, LSuserMaster objuser4, int t);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, int j, Integer testcode,
			List<LSsamplemaster> lstsample2, int k, Date fromdate2, Date todate2, LSuserMaster objuser, int l,
			Integer testcode2, int m, Date fromdate3, Date todate3, LSuserMaster objuser2, int n, Integer testcode3,
			List<Long> directorycode, int o, Date fromdate4, Date todate4, int p, Integer testcode4,
			List<Long> directorycode2, int q, Date fromdate5, Date todate5, LSuserMaster objuser3, int r,
			Integer testcode5, List<Long> directorycode3, int s, Date fromdate6, Date todate6, LSuserMaster objuser4,
			int t, Integer testcode6, Pageable pageable);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, int j, Integer testcode,
			List<LSsamplemaster> lstsample2, int k, Date fromdate2, Date todate2, LSuserMaster objuser, int l,
			Integer testcode2, int m, Date fromdate3, Date todate3, LSuserMaster objuser2, int n, Integer testcode3,
			List<Long> directorycode, int o, Date fromdate4, Date todate4, int p, Integer testcode4,
			List<Long> directorycode2, int q, Date fromdate5, Date todate5, LSuserMaster objuser3, int r,
			Integer testcode5, List<Long> directorycode3, int s, Date fromdate6, Date todate6, LSuserMaster objuser4,
			int t, Integer testcode6);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, int j, List<LSsamplemaster> lstsample2,
			int k, Date fromdate2, Date todate2, LSuserMaster objuser, int l, int m, Date fromdate3, Date todate3,
			LSuserMaster objuser2, int n, List<Long> directorycode, int o, Date fromdate4, Date todate4, int p,
			List<Long> directorycode2, int q, Date fromdate5, Date todate5, LSuserMaster objuser3, int r,
			List<Long> directorycode3, int s, Date fromdate6, Date todate6, LSuserMaster objuser4, int t,
			Pageable pageable);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, int j, List<LSsamplemaster> lstsample2,
			int k, Date fromdate2, Date todate2, LSuserMaster objuser, int l, int m, Date fromdate3, Date todate3,
			LSuserMaster objuser2, int n, List<Long> directorycode, int o, Date fromdate4, Date todate4, int p,
			List<Long> directorycode2, int q, Date fromdate5, Date todate5, LSuserMaster objuser3, int r,
			List<Long> directorycode3, int s, Date fromdate6, Date todate6, LSuserMaster objuser4, int t);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, int j, Integer testcode,
			List<LSsamplemaster> lstsample2, int k, Date fromdate2, Date todate2, LSuserMaster objuser, int l,
			Integer testcode2, int m, Date fromdate3, Date todate3, LSuserMaster objuser2, int n, Integer testcode3,
			List<Long> directorycode, int o, Date fromdate4, Date todate4, int p, Integer testcode4,
			List<Long> directorycode2, int q, Date fromdate5, Date todate5, LSuserMaster objuser3, int r,
			Integer testcode5, List<Long> directorycode3, int s, Date fromdate6, Date todate6, LSuserMaster objuser4,
			int t, Integer testcode6, Pageable pageable);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, int j, Integer testcode,
			List<LSsamplemaster> lstsample2, int k, Date fromdate2, Date todate2, LSuserMaster objuser, int l,
			Integer testcode2, int m, Date fromdate3, Date todate3, LSuserMaster objuser2, int n, Integer testcode3,
			List<Long> directorycode, int o, Date fromdate4, Date todate4, int p, Integer testcode4,
			List<Long> directorycode2, int q, Date fromdate5, Date todate5, LSuserMaster objuser3, int r,
			Integer testcode5, List<Long> directorycode3, int s, Date fromdate6, Date todate6, LSuserMaster objuser4,
			int t, Integer testcode6);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String string, int j, int k,
			List<LSsamplemaster> lstsample2, int l, Date fromdate2, Date todate2, LSuserMaster objuser, String string2,
			int m, int n, int o, Date fromdate3, Date todate3, LSuserMaster objuser2, String string3, int p, int q,
			List<Long> directorycode, int r, Date fromdate4, Date todate4, String string4, int s, int t,
			List<Long> directorycode2, int u, Date fromdate5, Date todate5, LSuserMaster objuser3, String string5,
			int v, int w, List<Long> directorycode3, int x, Date fromdate6, Date todate6, LSuserMaster objuser4,
			String string6, int y, int z);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String string, int j, int k,
			Integer testcode, List<LSsamplemaster> lstsample2, int l, Date fromdate2, Date todate2,
			LSuserMaster objuser, String string2, int m, int n, Integer testcode2, int o, Date fromdate3, Date todate3,
			LSuserMaster objuser2, String string3, int p, int q, Integer testcode3, List<Long> directorycode, int r,
			Date fromdate4, Date todate4, String string4, int s, int t, Integer testcode4, List<Long> directorycode2,
			int u, Date fromdate5, Date todate5, LSuserMaster objuser3, String string5, int v, int w, Integer testcode5,
			List<Long> directorycode3, int x, Date fromdate6, Date todate6, LSuserMaster objuser4, String string6,
			int y, int z, Integer testcode6);
















}