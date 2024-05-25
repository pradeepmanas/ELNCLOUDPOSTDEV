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
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail.ordersinterface;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public interface LSlogilablimsorderdetailRepository extends JpaRepository<LSlogilablimsorderdetail, Long> {

	@Transactional
	@Modifying
	@Query("update LSlogilablimsorderdetail o set o.batchid = ?1 where o.batchcode = ?2")
	void setbatchidBybatchcode(String batchid, Long batchcode);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagOrderByBatchcodeDesc(Integer filetype,
			String orderflag);

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

	public long countByOrderflagAndApprovelstatusNotAndCreatedtimestampBetween(String orderflag, int i, Date fromdate,
			Date todate);

	public long countByOrderflagAndApprovelstatusNotAndOrdercancellNotAndCreatedtimestampBetween(String orderflag,
			int i, int j, Date fromdate, Date todate);

	public long countByApprovelstatusAndCreatedtimestampBetween(int i, Date fromdate, Date todate);

	public long countByOrdercancellAndCompletedtimestampBetween(int i, Date fromdate, Date todate);

	public long countByOrdercancellAndCreatedtimestampBetween(int i, Date fromdate, Date todate);

	public long countByOrderflagAndCompletedtimestampBetween(String orderflag, Date fromdate, Date todate);

	public long countByOrderflagAndCompletedtimestampBetweenAndApprovelstatusNot(String orderflag, Date fromdate,
			Date todate, int i);

	public long countByFiletypeAndCreatedtimestampBetween(Integer filetype, Date fromdate, Date todate);

	public Long countByLsprojectmasterInAndCreatedtimestampBetween(List<LSprojectmaster> lstproject, Date fromdate,
			Date todate);

	public long countByLsprojectmasterInOrFiletypeAndCreatedtimestampBetween(List<LSprojectmaster> lstproject,
			Integer filetype, Date fromdate, Date todate);

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

	public List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	public List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndApprovelstatusAndApprovedAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Integer approvelstatus, Integer approved, Date fromdate,
			Date todate);

	public long countByOrderflagAndLsprojectmasterInAndApprovelstatusAndApprovedAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Integer approvelstatus, Integer approved, Date fromdate,
			Date todate);

	public List<Logilaborders> findByOrderflagAndCreatedtimestampBetweenOrderByBatchcodeDesc(String orderflag,
			Date fromdate, Date todate);

//	public List<Logilaborders> findByOrderflagAndApprovelstatusAndApprovedAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//			String orderflag, Integer approvelstatus, Integer approved, Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndLsprojectmasterInOrderByBatchcodeAsc(int i,
			String string, List<LSprojectmaster> lstproject);

	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagOrderByBatchcodeAsc(int i, String string);

	public Logilaborders findByBatchcode(Long batchcode);
	
	public LSlogilablimsorderdetail findByBatchcodeOrderByBatchcodeDesc(Long batchcode);

	public Logilabordermaster findByBatchcodeOrderByBatchcodeAsc(Long batchcode);
//
//	public List<Logilaborders> findByOrderflagAndLsprojectmasterInAndLsworkflowInAndCreatedtimestampBetween(
//			String orderflag, List<LSprojectmaster> lstproject, List<LSworkflow> lsworkflow, Date fromdate,
//			Date todate);

	List<Logilabordermaster> findByOrderflagAndLssamplefileInAndCreatedtimestampBetween(String string,
			List<LSsamplefile> lssamplefile, Date fromdate, Date todate);

	List<Logilabordermaster> findByLsprojectmasterInAndCreatedtimestampBetween(List<LSprojectmaster> lstproject,
			Date fromdate, Date todate);

	public List<Logilabordermaster> findFirst20ByBatchcodeLessThanOrderByBatchcodeDesc(Long batchcode);

	public List<Logilabordermaster> findFirst20ByBatchcodeLessThanAndLsprojectmasterInOrderByBatchcodeDesc(
			Long batchcode, List<LSprojectmaster> lstproject);

	public List<Logilabordermaster> findFirst20ByOrderByBatchcodeDesc();

	public List<Logilabordermaster> findFirst20ByLsprojectmasterInOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject);

	public Long countByLsprojectmasterIn(List<LSprojectmaster> lstproject);

	public List<LSlogilablimsorderdetail> findByLsprojectmasterOrderByBatchcodeDesc(LSprojectmaster lsproject);

	public Integer deleteByLsprojectmaster(LSprojectmaster lsproject);

	List<LSlogilablimsorderdetail> findByFiletypeAndApprovelstatusAndOrderflagOrderByBatchcodeDesc(int i, int j,
			String string);

	List<LSlogilablimsorderdetail> findByFiletypeAndApprovelstatusAndOrderflagAndLsprojectmasterInOrderByBatchcodeAsc(
			int i, int j, String string, List<LSprojectmaster> lstproject);

	List<LSlogilablimsorderdetail> findBybatchcode(Long batchcode);

	public List<Logilaborders> findByDirectorycodeAndCreatedtimestampBetweenOrderByBatchcodeDesc(Long directorycode,
			Date fromdate, Date todate);

	public List<Logilaborders> findByOrderflagOrderByBatchcodeDesc(String orderflag);

	@Transactional
	@Modifying
	@Query("update LSlogilablimsorderdetail o set o.directorycode = ?1 where o.batchcode = ?2")
	void updatedirectory(Long directorycode, Long batchcode);

	@Transactional
	@Modifying
	@Query("update LSlogilablimsorderdetail o set o.directorycode = ?1 where o.batchcode in (?2)")
	void updatedirectory(Long directorycode, List<Long> batchcode);

	@Transactional
	@Modifying
	@Query("update LSlogilablimsorderdetail o set o.directorycode = ?1 where o.directorycode = ?2")
	void updateparentdirectory(Long newdirectorycode, Long olddirectorycode);

	List<LSlogilablimsorderdetail> findByLssamplemaster(LSsamplemaster objClass);

	List<LSlogilablimsorderdetail> findByTestcode(Integer testcode);

	List<LSlogilablimsorderdetail> findByOrderflagAndAssignedtoAndLockeduserIsNotNullOrderByBatchcodeDesc(String string,
			LSuserMaster lSuserMaster);

	List<LSlogilablimsorderdetail> findByAssignedtoAndLockeduserIsNotNullOrderByBatchcodeDesc(
			LSuserMaster lSuserMaster);



	List<LSlogilablimsorderdetail> findByAssignedtoAndLsuserMasterAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			LSuserMaster lsselecteduser, LSuserMaster lsloginuser, Date fromdate, Date todate);

//	List<LSlogilablimsorderdetail> findByOrderflagAndLockeduserIsNotNullAndAssignedtoIsNullOrderByBatchcodeDesc(
//			String string);

	List<LSlogilablimsorderdetail> findByOrderflagAndLsprojectmasterInAndLsworkflowInAndLockeduserIsNotNullAndAssignedtoIsNullOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, List<LSworkflow> lstworkflow);

	List<Logilaborders> findByOrderdisplaytypeAndLsprojectmasterInAndTestcodeIsNotNullAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Integer orderdisplaytype, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	@Transactional
	@Modifying
	@Query(value = "select distinct LSlogilablimsorderdetail.lstestmasterlocal_testcode, LSlogilablimsorderdetail.lsprojectmaster_projectcode, CAST((select testname from lstestmasterlocal where testcode =  LSlogilablimsorderdetail.testcode) as varchar(250))as testname   from LSlogilablimsorderdetail as LSlogilablimsorderdetail"
			+ " where LSlogilablimsorderdetail.lstestmasterlocal_testcode is not null and LSlogilablimsorderdetail.lsprojectmaster_projectcode is not null and LSlogilablimsorderdetail.lsprojectmaster_projectcode in (?1)", nativeQuery = true)
	public ArrayList<List<Object>> getLstestmasterlocalByOrderdisplaytypeAndLsprojectmasterInAndTestcodeIsNotNull(
			List<Integer> lsprojectcode);

	List<Logilaborders> findByOrderdisplaytypeAndLssamplemasterInAndViewoptionAndTestcodeIsNotNullOrOrderdisplaytypeAndLsuserMasterAndViewoptionAndLssamplemasterInAndTestcodeIsNotNullAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Integer orderdisplaytype, List<LSsamplemaster> lstsample, Integer siteview, Integer orderdisplayuser,
			LSuserMaster lsloginuser, Integer userview, List<LSsamplemaster> lstsampleuser, Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findDistinctLstestmasterlocalByLsprojectmasterNotNullAndLstestmasterlocalNotNull();

	@Transactional
	@Modifying
	@Query(value = "select distinct LSlogilablimsorderdetail.lstestmasterlocal_testcode, LSlogilablimsorderdetail.lssamplemaster_samplecode,  CAST((select testname from lstestmasterlocal where testcode =  LSlogilablimsorderdetail.lstestmasterlocal_testcode) as varchar(10))as testname  from LSlogilablimsorderdetail as LSlogilablimsorderdetail"
			+ " where LSlogilablimsorderdetail.lstestmasterlocal_testcode is not null and LSlogilablimsorderdetail.lssamplemaster_samplecode is not null and LSlogilablimsorderdetail.lssamplemaster_samplecode in (?1)", nativeQuery = true)
	public List<Object> getLstestmasterlocalByOrderdisplaytypeAndLSsamplemasterInAndTestcodeIsNotNull(
			List<Integer> lssamplecode);
	
	@Transactional
	@Modifying
	@Query(value = "select distinct LSlogilablimsorderdetail.lstestmasterlocal_testcode, LSlogilablimsorderdetail.elnmaterial_nmaterialcode,  CAST((select testname from lstestmasterlocal where testcode =  LSlogilablimsorderdetail.lstestmasterlocal_testcode) as varchar(10))as testname  from LSlogilablimsorderdetail as LSlogilablimsorderdetail"
			+ " where LSlogilablimsorderdetail.lstestmasterlocal_testcode is not null and LSlogilablimsorderdetail.elnmaterial_nmaterialcode is not null and LSlogilablimsorderdetail.elnmaterial_nmaterialcode in (select nmaterialcode from elnmaterial where nsitecode=?1)", nativeQuery = true)
	public List<Object> getLstestmasterlocalAndmaterialByOrderdisplaytypeAndLSsamplemasterInAndTestcodeIsNotNull(Integer sitecode);

	public List<Logilaborders> findByLssamplemasterAndViewoptionAndLstestmasterlocalAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndLstestmasterlocalAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			LSsamplemaster lssample, Integer siteview, LStestmasterlocal lstest, Integer displaytype, Date fromdate,
			Date todate, LSsamplemaster lsusersample, Integer userview, LSuserMaster lsloginuser,
			LStestmasterlocal lstestuser, Integer displaytypeuser, Date fromdateuser, Date todateuser);

	public List<LSlogilablimsorderdetail> findByLssamplemasterAndViewoptionAndLstestmasterlocalAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			LSsamplemaster lssample, Integer siteview, LStestmasterlocal lstest, Integer displaytype, Date fromdate,
			Date todate);

	public List<Logilaborders> findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Long directorycode, Integer siteview, Date fromdate, Date todate, Long directorycodeuser, Integer userview,
			LSuserMaster lsloginuser, Date fromdateuser, Date todateuser);

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

//	List<Logilaborders> findByOrderflagAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
//			String orderflag, List<LSprojectmaster> lstproject, Integer filetype, Date fromdate, Date todate);

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

	long countByOrderflagAndApprovelstatusNotOrApprovelstatusIsNullAndOrdercancellIsNullAndLsprojectmasterInAndCreatedtimestampBetween(
			String string, int i, int j, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	long countByOrderflagAndOrdercancellIsNullAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotOrApprovelstatusIsNull(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i);

	long countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetween(int i, List<LSprojectmaster> lstproject,
			Date fromdate, Date todate);

	long countByOrderflagAndOrdercancellIsNullAndCreatedtimestampBetweenAndApprovelstatusNotOrApprovelstatusIsNull(
			String string, Date fromdate, Date todate, int i);

	long countByOrderflagAndOrdercancellIsNullAndCreatedtimestampBetween(String string, Date fromdate, Date todate);


	List<Logilabordermaster> findByOrderflagAndCreatedtimestampBetweenAndApprovelstatusNotOrApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
			String string, Date fromdate, Date todate, int i);



	List<Logilabordermaster> findByCreatedtimestampBetweenAndApprovelstatusOrderByBatchcodeDesc(Date fromdate,
			Date todate, int i, Pageable pageable);

	List<Logilabordermaster> findByCreatedtimestampBetweenAndOrdercancellOrderByBatchcodeDesc(Date fromdate,
			Date todate, int i, Pageable pageable);

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

	List<Logilabordermaster> findByFiletypeAndCreatedtimestampBetween(int i, Date fromdate, Date todate);

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

//	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, String string2,
//			List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2, String string3, int j, Date fromdate3,
//			Date todate3, Pageable pageable);

//	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, String string2, int i,
//			Date fromdate2, Date todate2, Pageable pageable);

	List<Logilabordermaster> findByOrderflagAndLssamplefileInAndCreatedtimestampBetween(String string,
			List<LSsamplefile> lssamplefile, Date fromdate, Date todate, Pageable pageable);

	List<Logilabordermaster> findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetween(int i,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Pageable pageable);

	List<Logilabordermaster> findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetween(int i,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Pageable pageable);

//	long countByLsprojectmasterInAndCreatedtimestampBetweenOrFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//			List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Date fromdate2, Date todate2);

//	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, String string2,
//			List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2, String string3, int j, Date fromdate3,
//			Date todate3);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, String string2, int i,
			Date fromdate2, Date todate2);

//	Object findByCreatedtimestampBetweenOrderByBatchcodeDesc(Date fromdate, Date todate);

	long countByCreatedtimestampBetweenOrderByBatchcodeDesc(Date fromdate, Date todate);

	long countByOrderflagAndLssamplefileInAndCreatedtimestampBetweenOrderByBatchcodeDesc(String string,
			List<LSsamplefile> lssamplefile, Date fromdate, Date todate);

	long countByCreatedtimestampBetweenAndApprovelstatusOrderByBatchcodeDesc(Date fromdate, Date todate, int i);

	long countByCreatedtimestampBetweenAndOrdercancellOrderByBatchcodeDesc(Date fromdate, Date todate, int i);

	List<LSlogilablimsorderdetail> findByCreatedtimestampBetweenOrderByBatchcodeDesc(Date fromdate, Date todate,
			Pageable pageable);

//	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Integer testcode,
//			String string2, List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2, Integer testcode2,
//			String string3, int j, Date fromdate3, Date todate3, Integer testcode3, Pageable pageable);

//	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Integer testcode,
//			String string2, List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2, Integer testcode2,
//			String string3, int j, Date fromdate3, Date todate3, Integer testcode3);

//	List<Logilabordermaster> findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
//			String string, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, int i, String string2,
//			LSprojectmaster lstprojectforfilter2, Date fromdate2, Date todate2, String string3, int j, Date fromdate3,
//			Date todate3, LSprojectmaster lstprojectforfilter3, Pageable pageable);

//	long countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
//			String string, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, int i, String string2,
//			LSprojectmaster lstprojectforfilter2, Date fromdate2, Date todate2, String string3, int j, Date fromdate3,
//			Date todate3, LSprojectmaster lstprojectforfilter3);

//	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode,
//			String string2, int i, Date fromdate2, Date todate2, Integer testcode2, Pageable pageable);

	List<Logilabordermaster> findByOrderflagAndLssamplefileInAndCreatedtimestampBetweenAndTestcode(String string,
			List<LSsamplefile> lssamplefile, Date fromdate, Date todate, Integer testcode, Pageable pageable);

	long countByOrderflagAndLssamplefileInAndCreatedtimestampBetweenAndTestcode(String string,
			List<LSsamplefile> lssamplefile, Date fromdate, Date todate, Integer testcode);

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

	List<Logilabordermaster> findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcode(int i,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode, Pageable pageable);

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

	List<Logilabordermaster> findByCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(
			Date fromdate, Date todate, int i, Integer testcode, Pageable pageable);

	long countByCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(Date fromdate, Date todate,
			int i, Integer testcode);

	List<Logilabordermaster> findByCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterOrderByBatchcodeDesc(
			Date fromdate, Date todate, int i, LSprojectmaster lstprojectforfilter, Pageable pageable);

	long countByCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterOrderByBatchcodeDesc(Date fromdate,
			Date todate, int i, LSprojectmaster lstprojectforfilter);

	List<Logilabordermaster> findByCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
			Date fromdate, Date todate, int i, LSprojectmaster lstprojectforfilter, Integer testcode,
			Pageable pageable);

	long countByCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(Date fromdate,
			Date todate, int i, LSprojectmaster lstprojectforfilter, Integer testcode);

	List<Logilabordermaster> findByCreatedtimestampBetweenAndOrdercancellAndTestcodeOrderByBatchcodeDesc(Date fromdate,
			Date todate, int i, Integer testcode, Pageable pageable);

	long countByCreatedtimestampBetweenAndOrdercancellAndTestcodeOrderByBatchcodeDesc(Date fromdate, Date todate, int i,
			Integer testcode);

	List<Logilabordermaster> findByCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterOrderByBatchcodeDesc(
			Date fromdate, Date todate, int i, LSprojectmaster lstprojectforfilter, Pageable pageable);

	long countByCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterOrderByBatchcodeDesc(Date fromdate, Date todate,
			int i, LSprojectmaster lstprojectforfilter);

	List<Logilabordermaster> findByCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
			Date fromdate, Date todate, int i, LSprojectmaster lstprojectforfilter, Integer testcode,
			Pageable pageable);

	long countByCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(Date fromdate,
			Date todate, int i, LSprojectmaster lstprojectforfilter, Integer testcode);

	long countByCreatedtimestampBetweenAndTestcode(Date fromdate, Date todate, Integer testcode);

	long countByOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcode(String string, int i, Date fromdate,
			Date todate, Integer testcode);

	long countByApprovelstatusAndCreatedtimestampBetweenAndTestcode(int i, Date fromdate, Date todate,
			Integer testcode);

	long countByOrdercancellAndCreatedtimestampBetweenAndTestcode(int i, Date fromdate, Date todate, Integer testcode);

	long countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmaster(String string, Date fromdate,
			Date todate, LSprojectmaster lstprojectforfilter);

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

	long countByApprovelstatusAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(int i, Date fromdate, Date todate,
			LSprojectmaster lstprojectforfilter, Integer testcode);

	long countByOrdercancellAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(int i, Date fromdate, Date todate,
			LSprojectmaster lstprojectforfilter, Integer testcode);

	long countByOrderflagAndLssamplefileInAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(String string,
			List<LSsamplefile> lssamplefile, Date fromdate, Date todate, LSprojectmaster lstprojectforfilter,
			Integer testcode);

	long countByLsprojectmasterInAndCreatedtimestampBetweenOrFiletypeAndCreatedtimestampBetween(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Date fromdate2, Date todate2);

	List<Logilaborders> findByDirectorycodeAndViewoptionAndFiletypeAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndFiletypeAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Long directorycode, int i, Integer filetype, Date fromdate, Date todate, Long directorycode2, int j,
			LSuserMaster createdby, Integer filetype2, Date fromdate2, Date todate2, Long directorycode3, int k,
			LSuserMaster createdby2, Integer filetype3, Date fromdate3, Date todate3);

	List<Logilaborders> findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Long directorycode, int i, Date fromdate, Date todate, Long directorycode2, int j, LSuserMaster createdby,
			Date fromdate2, Date todate2, Long directorycode3, int k, LSuserMaster createdby2, Date fromdate3,
			Date todate3);

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

	List<Logilaborders> findByOrderflagAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			String orderflag, String orderflag2, List<LSprojectmaster> lstproject, Date fromdate, Date todate,
			String orderflag3, String orderflag4, List<LSsamplemaster> lstsample, Date fromdate2, Date todate2);




	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String string, int j, int k,
			Integer testcode, List<LSsamplemaster> lstsample2, int l, Date fromdate2, Date todate2,
			LSuserMaster objuser, String string2, int m, int n, Integer testcode2, int o, Date fromdate3, Date todate3,
			LSuserMaster objuser2, String string3, int p, int q, Integer testcode3, List<Long> directorycode, int r,
			Date fromdate4, Date todate4, String string4, int s, int t, Integer testcode4, List<Long> directorycode2,
			int u, Date fromdate5, Date todate5, LSuserMaster objuser3, String string5, int v, int w, Integer testcode5,
			List<Long> directorycode3, int x, Date fromdate6, Date todate6, LSuserMaster objuser4, String string6,
			int y, int z, Integer testcode6);

	long countByOrderflagAndCompletedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNull(String string,
			Date fromdate, Date todate, int i, Integer testcode);

	long countByOrderflagAndCompletedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterAndOrdercancellIsNull(
			String string, Date fromdate, Date todate, int i, LSprojectmaster lstprojectforfilter);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterInAndOrdercancellIsNullOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, String string2,
			List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2, String string3, int j, Date fromdate3,
			Date todate3, String string4, int k, LSuserMaster objuser, Date fromdate4, Date todate4, int l,
			String string5, int m, LSuserMaster objuser2, Date fromdate5, Date todate5, int n, String string6, int o,
			LSuserMaster objuser3, Date fromdate6, Date todate6, int p, List<LSprojectmaster> lstproject3,
			String string7, int q, LSuserMaster objuser4, Date fromdate7, Date todate7, String string8, int r,
			LSuserMaster objuser5, Date fromdate8, Date todate8, String string9, int s, LSuserMaster objuser6,
			Date fromdate9, Date todate9, List<LSprojectmaster> lstproject4);

	long countByOrderflagAndCompletedtimestampBetweenAndTestcodeAndApprovelstatusIsNullAndOrdercancellIsNull(
			String string, Date fromdate, Date todate, Integer testcode);

	long countByOrderflagAndCompletedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNull(String string,
			Date fromdate, Date todate);

	long countByOrderflagAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterAndOrdercancellIsNull(
			String string, Date fromdate, Date todate, int i, LSprojectmaster lstprojectforfilter);

	List<LSlogilablimsorderdetail> findByLstestmasterlocalAndFiletypeNot(LStestmasterlocal lstest, int i);

	List<Logilaborders> findByDirectorycodeAndCreatedtimestampBetweenAndLssamplefileIn(Long directorycode,
			Date fromdate, Date todate, List<LSsamplefile> idList);

	List<Long> findByBatchcodeIn(List<Long> batchcode);

	List<Long> findByBatchcodeInAndFiletypeIn(List<Long> batchcode, List<Integer> filetype);

	public List<LSlogilablimsorderdetail> findByCreatedtimestampBetweenOrderByBatchcodeDesc(Date fromdate, Date todate);

	public List<LSlogilablimsorderdetail> findFirst2ByBatchcodeLessThanAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Long batchcode, Date fromdate, Date todate);

//	List<Logilaborders> findByOrderflagAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterInOrderByBatchcodeDesc(
//			String orderflag, List<LSprojectmaster> lstproject, Integer filetype, Date fromdate, Date todate,
//			String orderflag2, List<LSsamplemaster> lstsample, Integer filetype2, Date fromdate2, Date todate2, int i,
//			String orderflag3, List<LSsamplemaster> lstsample2, Integer filetype3, Date fromdate3, Date todate3, int j,
//			LSuserMaster lsuserMaster, String orderflag4, List<LSsamplemaster> lstsample3, Integer filetype4,
//			Date fromdate4, Date todate4, int k, List<LSuserMaster> lstuserMaster);

	List<Logilaborders> findByOrderflagAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Integer filetype, Date fromdate, Date todate,
			String orderflag2, List<LSsamplemaster> lstsample, Integer filetype2, Date fromdate2, Date todate2, int i,
			String orderflag3, List<LSsamplemaster> lstsample2, Integer filetype3, Date fromdate3, Date todate3, int j,
			LSuserMaster lsuserMaster, String orderflag4, List<LSsamplemaster> lstsample3, Integer filetype4,
			Date fromdate4, Date todate4, int k);

//	List<Logilaborders> findByOrderflagAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrderByBatchcodeDesc(
//			String orderflag, List<LSprojectmaster> lstproject, Integer filetype, Date fromdate, Date todate,
//			String orderflag2, List<LSsamplemaster> lstsample, Integer filetype2, Date fromdate2, Date todate2, int i,
//			String orderflag3, List<LSsamplemaster> lstsample2, Integer filetype3, Date fromdate3, Date todate3, int j,
//			LSuserMaster lsuserMaster, String orderflag4, List<LSsamplemaster> lstsample3, Integer filetype4,
//			Date fromdate4, Date todate4, int k, LSuserMaster lsuserMaster2);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, String string2, int i,
			Date fromdate2, Date todate2, String string3, int j, LSuserMaster objuser, Date fromdate3, Date todate3,
			String string4, int k, LSuserMaster objuser2, Date fromdate4, Date todate4, String string5,
			List<LSprojectmaster> lstproject2, int l, LSuserMaster objuser3, Date fromdate5, Date todate5,
			String string6, int m, LSuserMaster objuser4, Date fromdate6, Date todate6, Pageable pageable);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, String string2, int i,
			Date fromdate2, Date todate2, String string3, int j, LSuserMaster objuser, Date fromdate3, Date todate3,
			String string4, int k, LSuserMaster objuser2, Date fromdate4, Date todate4, String string5,
			List<LSprojectmaster> lstproject2, int l, LSuserMaster objuser3, Date fromdate5, Date todate5,
			String string6, int m, LSuserMaster objuser4, Date fromdate6, Date todate6);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterInAndOrdercancellIsNullOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, String string2,
			List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2, String string3, int j, Date fromdate3,
			Date todate3, String string4, int k, LSuserMaster objuser, Date fromdate4, Date todate4, int l,
			String string5, int m, LSuserMaster objuser2, Date fromdate5, Date todate5, int n, String string6, int o,
			LSuserMaster objuser3, Date fromdate6, Date todate6, int p, List<LSprojectmaster> lstproject3,
			String string7, int q, LSuserMaster objuser4, Date fromdate7, Date todate7, String string8, int r,
			LSuserMaster objuser5, Date fromdate8, Date todate8, String string9, int s, LSuserMaster objuser6,
			Date fromdate9, Date todate9, List<LSprojectmaster> lstproject4);

	long countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNull(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int j, LSuserMaster objuser,
			Date fromdate2, Date todate2, int k, int l, LSuserMaster objuser2, Date fromdate3, Date todate3, int m,
			int n, LSuserMaster objuser3, Date fromdate4, Date todate4, int o, int p, LSuserMaster objuser4,
			Date fromdate5, Date todate5, int q);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, String string2,
			List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2, String string3, int j, Date fromdate3,
			Date todate3, String string4, int k, LSuserMaster objuser, Date fromdate4, Date todate4, int l,
			String string5, int m, LSuserMaster objuser2, Date fromdate5, Date todate5, int n, String string6, int o,
			LSuserMaster objuser3, Date fromdate6, Date todate6, int p, List<LSprojectmaster> lstproject3,
			String string7, int q, LSuserMaster objuser4, Date fromdate7, Date todate7, String string8, int r,
			LSuserMaster objuser5, Date fromdate8, Date todate8, String string9, int s, LSuserMaster objuser6,
			Date fromdate9, Date todate9, Pageable pageable);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, String string2,
			List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2, String string3, int j, Date fromdate3,
			Date todate3, String string4, int k, LSuserMaster objuser, Date fromdate4, Date todate4, int l,
			String string5, int m, LSuserMaster objuser2, Date fromdate5, Date todate5, int n, String string6, int o,
			LSuserMaster objuser3, Date fromdate6, Date todate6, int p, List<LSprojectmaster> lstproject3,
			String string7, int q, LSuserMaster objuser4, Date fromdate7, Date todate7, String string8, int r,
			LSuserMaster objuser5, Date fromdate8, Date todate8, String string9, int s, LSuserMaster objuser6,
			Date fromdate9, Date todate9);

	List<Logilabordermaster> findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNull(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int j, LSuserMaster objuser,
			Date fromdate2, Date todate2, int k, int l, LSuserMaster objuser2, Date fromdate3, Date todate3, int m,
			int n, LSuserMaster objuser3, Date fromdate4, Date todate4, int o, int p, LSuserMaster objuser4,
			Date fromdate5, Date todate5, int q, Pageable pageable);

	List<LSlogilablimsorderdetail> findByOrderflagAndLsprojectmasterInAndFiletypeAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsuserMasterInOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Integer filetype, String orderflag2,
			List<LSsamplemaster> lstsample, Integer filetype2, int i, String orderflag3,
			List<LSsamplemaster> lstsample2, Integer filetype3, int j, LSuserMaster lsuserMaster, String orderflag4,
			List<LSsamplemaster> lstsample3, Integer filetype4, int k, List<LSuserMaster> lstuserMaster);

	List<LSlogilablimsorderdetail> findByOrderflagAndLsprojectmasterInAndFiletypeAndAssignedtoIsNullAndLsfileOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsfileOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsuserMasterAndLsfileOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsuserMasterInAndLsfileOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Integer filetype, LSfile lsfile, String orderflag2,
			List<LSsamplemaster> lstsample, Integer filetype2, LSfile lsfile2, int i, String orderflag3,
			List<LSsamplemaster> lstsample2, Integer filetype3, LSfile lsfile3, int j, LSuserMaster lsuserMaster,
			String orderflag4, List<LSsamplemaster> lstsample3, Integer filetype4, int k,
			List<LSuserMaster> lstuserMaster, LSfile lsfile4);

	List<LSlogilablimsorderdetail> findByLsprojectmasterInAndFiletypeAndAssignedtoIsNullAndLsfileOrLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsfileOrLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsfileOrLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsfile(
			List<LSprojectmaster> lstproject, Integer filetype, LSfile lsfile, List<LSsamplemaster> lstsample,
			Integer filetype2, int i, LSfile lsfile2, List<LSsamplemaster> lstsample2, Integer filetype3, int j,
			LSfile lsfile3, List<LSsamplemaster> lstsample3, Integer filetype4, int k, LSfile lsfile4);

	List<LSlogilablimsorderdetail> findByLsprojectmasterInAndFiletypeAndAssignedtoIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionOrLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionOrLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoption(
			List<LSprojectmaster> lstproject, Integer filetype, List<LSsamplemaster> lstsample, Integer filetype2,
			int i, List<LSsamplemaster> lstsample2, Integer filetype3, int j, List<LSsamplemaster> lstsample3,
			Integer filetype4, int k);

	List<LSlogilablimsorderdetail> findByLsprojectmasterInAndFiletypeAndAssignedtoIsNullAndLsfileOrLsprojectmasterIsNullAndLssamplemasterIsNullAndFiletypeAndAssignedtoIsNullAndLsfileOrLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsfileOrLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsfileOrLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsfile(
			List<LSprojectmaster> lstproject, Integer filetype, LSfile lsfile, Integer filetype2, LSfile lsfile2,
			List<LSsamplemaster> lstsample, Integer filetype3, int i, LSfile lsfile3, List<LSsamplemaster> lstsample2,
			Integer filetype4, int j, LSfile lsfile4, List<LSsamplemaster> lstsample3, Integer filetype5, int k,
			LSfile lsfile5);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, String string2, int i,
			Date fromdate2, Date todate2, String string3, int j, LSuserMaster objuser, Date fromdate3, Date todate3,
			String string4, int k, LSuserMaster objuser2, Date fromdate4, Date todate4, String string5,
			List<LSprojectmaster> lstproject2, int l, LSuserMaster objuser3, Date fromdate5, Date todate5,
			String string6, int m, Date fromdate6, Date todate6, List<LSuserMaster> usernotify, Pageable pageable);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, String string2, int i,
			Date fromdate2, Date todate2, String string3, int j, LSuserMaster objuser, Date fromdate3, Date todate3,
			String string4, int k, LSuserMaster objuser2, Date fromdate4, Date todate4, String string5,
			List<LSprojectmaster> lstproject2, int l, LSuserMaster objuser3, Date fromdate5, Date todate5,
			String string6, int m, Date fromdate6, Date todate6, List<LSuserMaster> usernotify);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterInAndOrdercancellIsNullAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, String string2, int i,
			Date fromdate2, Date todate2, String string3, int j, LSuserMaster objuser, Date fromdate3, Date todate3,
			String string4, int k, LSuserMaster objuser2, Date fromdate4, Date todate4, String string5,
			List<LSprojectmaster> lstproject2, int l, LSuserMaster objuser3, Date fromdate5, Date todate5,
			String string6, int m, List<LSuserMaster> usernotify, Date fromdate6, Date todate6);

	List<Logilabordermaster> findByLsprojectmasterInAndCreatedtimestampBetweenOrFiletypeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInOrViewoptionAndLsuserMasterInAndCreatedtimestampBetweenAndLsprojectmasterIsNullOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Date fromdate2, Date todate2, int j,
			LSuserMaster objuser, Date fromdate3, Date todate3, int k, LSuserMaster objuser2, Date fromdate4,
			Date todate4, int l, LSuserMaster objuser3, Date fromdate5, Date todate5, List<LSprojectmaster> lstproject2,
			int m, List<LSuserMaster> usernotify, Date fromdate6, Date todate6, Pageable pageable);

	long countByLsprojectmasterInAndCreatedtimestampBetweenOrFiletypeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInOrViewoptionAndLsuserMasterInAndCreatedtimestampBetweenAndLsprojectmasterIsNullOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Date fromdate2, Date todate2, int j,
			LSuserMaster objuser, Date fromdate3, Date todate3, int k, LSuserMaster objuser2, Date fromdate4,
			Date todate4, int l, LSuserMaster objuser3, Date fromdate5, Date todate5, List<LSprojectmaster> lstproject2,
			int m, List<LSuserMaster> usernotify, Date fromdate6, Date todate6);

	long countByLsprojectmasterInAndCreatedtimestampBetweenOrFiletypeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Date fromdate2, Date todate2, int j,
			LSuserMaster objuser, Date fromdate3, Date todate3, int k, LSuserMaster objuser2, Date fromdate4,
			Date todate4, int l, LSuserMaster objuser3, Date fromdate5, Date todate5);

	List<Logilabordermaster> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, String searchkeywords, String searchkeywords2,
			String searchkeywords3, int j, Date fromdate2, Date todate2, LSuserMaster objuser2, String searchkeywords4,
			List<Long> directorycode, int k, Date fromdate3, Date todate3, String searchkeywords5,
			String searchkeywords6, String searchkeywords7, List<Long> directorycode2, int l, Date fromdate4,
			Date todate4, String searchkeywords8, List<Long> directorycode3, int m, Date fromdate5, Date todate5,
			LSuserMaster objuser3, String searchkeywords9, String searchkeywords10, String searchkeywords11,
			List<Long> directorycode4, int n, Date fromdate6, Date todate6, LSuserMaster objuser4,
			String searchkeywords12, List<Long> directorycode5, int o, Date fromdate7, Date todate7,
			LSuserMaster objuser5, String searchkeywords13, String searchkeywords14, String searchkeywords15,
			List<Long> directorycode6, int p, Date fromdate8, Date todate8, LSuserMaster objuser6,
			String searchkeywords16, Pageable pageable);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String searchkeywords,
			String searchkeywords2, String searchkeywords3, LSuserMaster objuser);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String searchkeywords,
			LSuserMaster objuser);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, LSuserMaster objuser,
			String searchkeywords, String searchkeywords2, String searchkeywords3);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, LSuserMaster objuser,
			String searchkeywords);

	long countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, String searchkeywords, String searchkeywords2,
			String searchkeywords3, int j, Date fromdate2, Date todate2, LSuserMaster objuser2, String searchkeywords4,
			List<Long> directorycode, int k, Date fromdate3, Date todate3, String searchkeywords5,
			String searchkeywords6, String searchkeywords7, List<Long> directorycode2, int l, Date fromdate4,
			Date todate4, String searchkeywords8, List<Long> directorycode3, int m, Date fromdate5, Date todate5,
			LSuserMaster objuser3, String searchkeywords9, String searchkeywords10, String searchkeywords11,
			List<Long> directorycode4, int n, Date fromdate6, Date todate6, LSuserMaster objuser4,
			String searchkeywords12, List<Long> directorycode5, int o, Date fromdate7, Date todate7,
			LSuserMaster objuser5, String searchkeywords13, String searchkeywords14, String searchkeywords15,
			List<Long> directorycode6, int p, Date fromdate8, Date todate8, LSuserMaster objuser6,
			String searchkeywords16);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, LSuserMaster objuser,
			String searchkeywords, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, LSuserMaster objuser,
			String searchkeywords, String searchkeywords2, String searchkeywords3, Pageable pageable);

//	List<Logilabordermaster> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullderByBatchcodeDesc(
//			int i, Date fromdate, Date todate, LSuserMaster objuser, String searchkeywords, String searchkeywords2,
//			String searchkeywords3, int j, Date fromdate2, Date todate2, LSuserMaster objuser2, String searchkeywords4,
//			List<Long> directorycode, int k, Date fromdate3, Date todate3, String searchkeywords5,
//			String searchkeywords6, String searchkeywords7, List<Long> directorycode2, int l, Date fromdate4,
//			Date todate4, String searchkeywords8, List<Long> directorycode3, int m, Date fromdate5, Date todate5,
//			LSuserMaster objuser3, String searchkeywords9, String searchkeywords10, String searchkeywords11,
//			List<Long> directorycode4, int n, Date fromdate6, Date todate6, LSuserMaster objuser4,
//			String searchkeywords12, List<Long> directorycode5, int o, Date fromdate7, Date todate7,
//			LSuserMaster objuser5, String searchkeywords13, String searchkeywords14, String searchkeywords15,
//			List<Long> directorycode6, int p, Date fromdate8, Date todate8, LSuserMaster objuser6,
//			String searchkeywords16, Pageable pageable);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, LSuserMaster objuser,
			String searchkeywords);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String searchkeywords, LSuserMaster obj);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String searchkeywords,
			String searchkeywords2, String searchkeywords3, LSuserMaster userma);

//	long countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullderByBatchcodeDesc(
//			int i, Date fromdate, Date todate, LSuserMaster objuser, String searchkeywords, String searchkeywords2,
//			String searchkeywords3, int j, Date fromdate2, Date todate2, LSuserMaster objuser2, String searchkeywords4,
//			List<Long> directorycode, int k, Date fromdate3, Date todate3, String searchkeywords5,
//			String searchkeywords6, String searchkeywords7, List<Long> directorycode2, int l, Date fromdate4,
//			Date todate4, String searchkeywords8, List<Long> directorycode3, int m, Date fromdate5, Date todate5,
//			LSuserMaster objuser3, String searchkeywords9, String searchkeywords10, String searchkeywords11,
//			List<Long> directorycode4, int n, Date fromdate6, Date todate6, LSuserMaster objuser4,
//			String searchkeywords12, List<Long> directorycode5, int o, Date fromdate7, Date todate7,
//			LSuserMaster objuser5, String searchkeywords13, String searchkeywords14, String searchkeywords15,
//			List<Long> directorycode6, int p, Date fromdate8, Date todate8, LSuserMaster objuser6,
//			String searchkeywords16);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, LSuserMaster objuser,
			String searchkeywords);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, LSuserMaster objuser,
			String searchkeywords, String searchkeywords2, String searchkeywords3);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String searchkeywords,
			LSuserMaster objuser);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String searchkeywords,
			String searchkeywords2, String searchkeywords3, LSuserMaster objuser);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, LSuserMaster objuser,
			String searchkeywords, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, LSuserMaster objuser,
			String searchkeywords, String searchkeywords2, String searchkeywords3, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String searchkeywords,
			LSuserMaster objuser, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String searchkeywords,
			String searchkeywords2, String searchkeywords3, LSuserMaster objuser, Pageable pageable);

	List<Logilabordermaster> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, String searchkeywords, String searchkeywords2,
			String searchkeywords3, int j, Date fromdate2, Date todate2, LSuserMaster objuser2, String searchkeywords4,
			List<Long> directorycode, int k, Date fromdate3, Date todate3, String searchkeywords5,
			String searchkeywords6, String searchkeywords7, List<Long> directorycode2, int l, Date fromdate4,
			Date todate4, String searchkeywords8, List<Long> directorycode3, int m, Date fromdate5, Date todate5,
			LSuserMaster objuser3, String searchkeywords9, String searchkeywords10, String searchkeywords11,
			List<Long> directorycode4, int n, Date fromdate6, Date todate6, LSuserMaster objuser4,
			String searchkeywords12, List<Long> directorycode5, int o, Date fromdate7, Date todate7,
			LSuserMaster objuser5, String searchkeywords13, String searchkeywords14, String searchkeywords15,
			List<Long> directorycode6, int p, Date fromdate8, Date todate8, LSuserMaster objuser6,
			String searchkeywords16, Pageable pageable);

	long countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, String searchkeywords, String searchkeywords2,
			String searchkeywords3, int j, Date fromdate2, Date todate2, LSuserMaster objuser2, String searchkeywords4,
			List<Long> directorycode, int k, Date fromdate3, Date todate3, String searchkeywords5,
			String searchkeywords6, String searchkeywords7, List<Long> directorycode2, int l, Date fromdate4,
			Date todate4, String searchkeywords8, List<Long> directorycode3, int m, Date fromdate5, Date todate5,
			LSuserMaster objuser3, String searchkeywords9, String searchkeywords10, String searchkeywords11,
			List<Long> directorycode4, int n, Date fromdate6, Date todate6, LSuserMaster objuser4,
			String searchkeywords12, List<Long> directorycode5, int o, Date fromdate7, Date todate7,
			LSuserMaster objuser5, String searchkeywords13, String searchkeywords14, String searchkeywords15,
			List<Long> directorycode6, int p, Date fromdate8, Date todate8, LSuserMaster objuser6,
			String searchkeywords16);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String searchkeywords,
			String searchkeywords2, String searchkeywords3, LSuserMaster objuser, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String searchkeywords,
			LSuserMaster objuser, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String searchkeywords,
			String searchkeywords2, String searchkeywords3, LSuserMaster objuser, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, String searchkeywords,
			LSuserMaster objuser, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, LSuserMaster objuser,
			String searchkeywords, String searchkeywords2, String searchkeywords3, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample, int i, Date fromdate, Date todate, LSuserMaster objuser,
			String searchkeywords, Pageable pageable);

	List<Logilaborders> findByOrderflagAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMaster(
			String orderflag, List<LSprojectmaster> lstproject, Integer filetype, Date fromdate, Date todate,
			String orderflag2, List<LSsamplemaster> lstsample, Integer filetype2, Date fromdate2, Date todate2, int i,
			String orderflag3, List<LSsamplemaster> lstsample2, Integer filetype3, Date fromdate3, Date todate3, int j,
			LSuserMaster lsuserMaster);

	List<Logilaborders> findByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterInOrderByBatchcodeDesc(
			String orderflag, List<LSsamplemaster> lstsample, Integer filetype, Date fromdate, Date todate, int i,
			List<LSuserMaster> lstuserMaster);


	LSlogilablimsorderdetail findByBatchidOrBatchcode(String batchid, Long batchcode);

	List<Logilabordermaster> findByLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInAndAssignedtoIsNullOrViewoptionAndLsuserMasterInAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Date fromdate2, Date todate2,
			List<LSsamplemaster> lstsample1, Date fromdate3, Date todate3, int j, LSuserMaster objuser, Date fromdate4,
			Date todate4, int k, LSuserMaster objuser2, Date fromdate5, Date todate5, int l, LSuserMaster objuser3,
			Date fromdate6, Date todate6, List<LSprojectmaster> lstproject2, int m, List<LSuserMaster> usernotify,
			Date fromdate7, Date todate7, LSuserMaster objuser4, LSuserMaster objuser5, Date fromdate8, Date todate8,
			LSuserMaster objuser6, Date fromdate9, Date todate9, Pageable pageable);

	long countByLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInAndAssignedtoIsNullOrViewoptionAndLsuserMasterInAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Date fromdate2, Date todate2,
			List<LSsamplemaster> lstsample1, Date fromdate3, Date todate3, int j, LSuserMaster objuser, Date fromdate4,
			Date todate4, int k, LSuserMaster objuser2, Date fromdate5, Date todate5, int l, LSuserMaster objuser3,
			Date fromdate6, Date todate6, List<LSprojectmaster> lstproject2, int m, List<LSuserMaster> usernotify,
			Date fromdate7, Date todate7, LSuserMaster objuser4, LSuserMaster objuser5, Date fromdate8, Date todate8,
			LSuserMaster objuser6, Date fromdate9, Date todate9);

	List<Logilabordermaster> findByLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrFiletypeAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode, int i, Date fromdate2,
			Date todate2, Integer testcode2, int j, LSuserMaster objuser, Date fromdate3, Date todate3,
			Integer testcode3, int k, LSuserMaster objuser2, Date fromdate4, Date todate4, Integer testcode4, int l,
			LSuserMaster objuser3, Date fromdate5, Date todate5, Integer testcode5, LSuserMaster objuser4,
			LSuserMaster objuser5, Date fromdate6, Date todate6, Integer testcode6, LSuserMaster objuser6,
			Date fromdate7, Date todate7, Integer testcode7, Pageable pageable);

	long countByLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrFiletypeAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode, int i, Date fromdate2,
			Date todate2, Integer testcode2, int j, LSuserMaster objuser, Date fromdate3, Date todate3,
			Integer testcode3, int k, LSuserMaster objuser2, Date fromdate4, Date todate4, Integer testcode4, int l,
			LSuserMaster objuser3, Date fromdate5, Date todate5, Integer testcode5, LSuserMaster objuser4,
			LSuserMaster objuser5, Date fromdate6, Date todate6, Integer testcode6, LSuserMaster objuser6,
			Date fromdate7, Date todate7, Integer testcode7);

	List<Logilabordermaster> findByLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
			LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, int i,
			LSprojectmaster lstprojectforfilter2, Date fromdate2, Date todate2, LSuserMaster objuser,
			LSuserMaster objuser2, Date fromdate3, Date todate3, LSprojectmaster lstprojectforfilter3,
			LSuserMaster objuser3, Date fromdate4, Date todate4, LSprojectmaster lstprojectforfilter4,
			Pageable pageable);

	long countByLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
			LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, int i,
			LSprojectmaster lstprojectforfilter2, Date fromdate2, Date todate2, LSuserMaster objuser,
			LSuserMaster objuser2, Date fromdate3, Date todate3, LSprojectmaster lstprojectforfilter3,
			LSuserMaster objuser3, Date fromdate4, Date todate4, LSprojectmaster lstprojectforfilter4);

	List<Logilabordermaster> findByLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndTestcodeAndLsprojectmasterOrderByBatchcodeDesc(
			LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, Integer testcode, int i,
			LSprojectmaster lstprojectforfilter2, Date fromdate2, Date todate2, Integer testcode2, LSuserMaster objuser,
			LSuserMaster objuser2, Date fromdate3, Date todate3, Integer testcode3,
			LSprojectmaster lstprojectforfilter3, LSuserMaster objuser3, Date fromdate4, Date todate4,
			Integer testcode4, LSprojectmaster lstprojectforfilter4, Pageable pageable);

	long countByLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndTestcodeAndLsprojectmasterOrderByBatchcodeDesc(
			LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, Integer testcode, int i,
			LSprojectmaster lstprojectforfilter2, Date fromdate2, Date todate2, Integer testcode2, LSuserMaster objuser,
			LSuserMaster objuser2, Date fromdate3, Date todate3, Integer testcode3,
			LSprojectmaster lstprojectforfilter3, LSuserMaster objuser3, Date fromdate4, Date todate4,
			Integer testcode4, LSprojectmaster lstprojectforfilter4);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
			String string, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, int i, String string2,
			LSprojectmaster lstprojectforfilter2, Date fromdate2, Date todate2, String string3, int j, Date fromdate3,
			Date todate3, LSprojectmaster lstprojectforfilter3, String string4, LSuserMaster objuser,
			LSuserMaster objuser2, Date fromdate4, Date todate4, LSprojectmaster lstprojectforfilter4, String string5,
			LSuserMaster objuser3, Date fromdate5, Date todate5, LSprojectmaster lstprojectforfilter5,
			Pageable pageable);

	long countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
			String string, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, int i, String string2,
			LSprojectmaster lstprojectforfilter2, Date fromdate2, Date todate2, String string3, int j, Date fromdate3,
			Date todate3, LSprojectmaster lstprojectforfilter3, String string4, LSuserMaster objuser,
			LSuserMaster objuser2, Date fromdate4, Date todate4, LSprojectmaster lstprojectforfilter4, String string5,
			LSuserMaster objuser3, Date fromdate5, Date todate5, LSprojectmaster lstprojectforfilter5);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
			String string, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, int i, Integer testcode,
			String string2, LSprojectmaster lstprojectforfilter2, Date fromdate2, Date todate2, Integer testcode2,
			String string3, int j, Date fromdate3, Date todate3, LSprojectmaster lstprojectforfilter3,
			Integer testcode3, String string4, LSuserMaster objuser, LSuserMaster objuser2, Date fromdate4,
			Date todate4, LSprojectmaster lstprojectforfilter4, Integer testcode4, String string5,
			LSuserMaster objuser3, Date fromdate5, Date todate5, LSprojectmaster lstprojectforfilter5,
			Integer testcode5, Pageable pageable);

	long countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
			String string, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, int i, Integer testcode,
			String string2, LSprojectmaster lstprojectforfilter2, Date fromdate2, Date todate2, Integer testcode2,
			String string3, int j, Date fromdate3, Date todate3, LSprojectmaster lstprojectforfilter3,
			Integer testcode3, String string4, LSuserMaster objuser, LSuserMaster objuser2, Date fromdate4,
			Date todate4, LSprojectmaster lstprojectforfilter4, Integer testcode4, String string5,
			LSuserMaster objuser3, Date fromdate5, Date todate5, LSprojectmaster lstprojectforfilter5,
			Integer testcode5);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
			String string, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, Integer testcode,
			String string2, int i, Date fromdate2, Date todate2, LSprojectmaster lstprojectforfilter2,
			Integer testcode2, String string3, LSuserMaster objuser, LSuserMaster objuser2, Date fromdate3,
			Date todate3, LSprojectmaster lstprojectforfilter3, Integer testcode3, String string4,
			LSuserMaster objuser3, Date fromdate4, Date todate4, LSprojectmaster lstprojectforfilter4,
			Integer testcode4, Pageable pageable);

	long countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
			String string, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, Integer testcode,
			String string2, int i, Date fromdate2, Date todate2, LSprojectmaster lstprojectforfilter2,
			Integer testcode2, String string3, LSuserMaster objuser, LSuserMaster objuser2, Date fromdate3,
			Date todate3, LSprojectmaster lstprojectforfilter3, Integer testcode3, String string4,
			LSuserMaster objuser3, Date fromdate4, Date todate4, LSprojectmaster lstprojectforfilter4,
			Integer testcode4);

	long countByOrderflagAndLssamplefileInAndCreatedtimestampBetween(String string, List<LSsamplefile> lssamplefile,
			Date fromdate, Date todate);

	List<LSlogilablimsorderdetail> findByActiveuser(Integer activeuser);

	List<LSlogilablimsorderdetail> findByActiveuserIn(List<Integer> activeuser);

//	List<Logilabordermaster> findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellOrAssignedtoAndCreatedtimestampBetweenAndOrdercancell(
//			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int j, LSuserMaster objuser,
//			Date fromdate2, Date todate2, int k, int l, LSuserMaster objuser2, Date fromdate3, Date todate3, int m,
//			int n, LSuserMaster objuser3, Date fromdate4, Date todate4, int o, LSuserMaster objuser4,
//			LSuserMaster objuser5, Date fromdate5, Date todate5, int p, LSuserMaster objuser6, Date fromdate6,
//			Date todate6, int q, Pageable pageable);

	long countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellOrAssignedtoAndCreatedtimestampBetweenAndOrdercancell(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int j, LSuserMaster objuser,
			Date fromdate2, Date todate2, int k, int l, LSuserMaster objuser2, Date fromdate3, Date todate3, int m,
			int n, LSuserMaster objuser3, Date fromdate4, Date todate4, int o, LSuserMaster objuser4,
			LSuserMaster objuser5, Date fromdate5, Date todate5, int p, LSuserMaster objuser6, Date fromdate6,
			Date todate6, int q);

	List<Logilabordermaster> findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullAndTestcodeOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndTestcode(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode, int j,
			LSuserMaster objuser, Date fromdate2, Date todate2, int k, Integer testcode2, int l, LSuserMaster objuser2,
			Date fromdate3, Date todate3, int m, Integer testcode3, int n, LSuserMaster objuser3, Date fromdate4,
			Date todate4, int o, Integer testcode4, LSuserMaster objuser4, LSuserMaster objuser5, Date fromdate5,
			Date todate5, int p, Integer testcode5, LSuserMaster objuser6, Date fromdate6, Date todate6, int q,
			Integer testcode6, Pageable pageable);

	long countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullAndTestcodeOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndTestcode(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode, int j,
			LSuserMaster objuser, Date fromdate2, Date todate2, int k, Integer testcode2, int l, LSuserMaster objuser2,
			Date fromdate3, Date todate3, int m, Integer testcode3, int n, LSuserMaster objuser3, Date fromdate4,
			Date todate4, int o, Integer testcode4, LSuserMaster objuser4, LSuserMaster objuser5, Date fromdate5,
			Date todate5, int p, Integer testcode5, LSuserMaster objuser6, Date fromdate6, Date todate6, int q,
			Integer testcode6);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
			String string, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, String string2, int i,
			Date fromdate2, Date todate2, LSprojectmaster lstprojectforfilter2, String string3, LSuserMaster objuser,
			LSuserMaster objuser2, Date fromdate3, Date todate3, LSprojectmaster lstprojectforfilter3, String string4,
			LSuserMaster objuser3, Date fromdate4, Date todate4, LSprojectmaster lstprojectforfilter4,
			Pageable pageable);

	long countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
			String string, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, String string2, int i,
			Date fromdate2, Date todate2, LSprojectmaster lstprojectforfilter2, String string3, LSuserMaster objuser,
			LSuserMaster objuser2, Date fromdate3, Date todate3, LSprojectmaster lstprojectforfilter3, String string4,
			LSuserMaster objuser3, Date fromdate4, Date todate4, LSprojectmaster lstprojectforfilter4);

	List<Logilabordermaster> findByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmaster(
			int i, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, LSuserMaster objuser,
			LSuserMaster objuser2, Date fromdate2, Date todate2, int j, LSprojectmaster lstprojectforfilter2,
			LSuserMaster objuser3, Date fromdate3, Date todate3, int k, LSprojectmaster lstprojectforfilter3,
			Pageable pageable);

	long countByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmaster(
			int i, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, LSuserMaster objuser,
			LSuserMaster objuser2, Date fromdate2, Date todate2, int j, LSprojectmaster lstprojectforfilter2,
			LSuserMaster objuser3, Date fromdate3, Date todate3, int k, LSprojectmaster lstprojectforfilter3);

	List<Logilabordermaster> findByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusAndLsprojectmasterAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterAndTestcode(
			int i, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, Integer testcode,
			LSuserMaster objuser, LSuserMaster objuser2, Date fromdate2, Date todate2, int j,
			LSprojectmaster lstprojectforfilter2, Integer testcode2, LSuserMaster objuser3, Date fromdate3,
			Date todate3, int k, LSprojectmaster lstprojectforfilter3, Integer testcode3, Pageable pageable);

	long countByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusAndLsprojectmasterAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterAndTestcode(
			int i, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, Integer testcode,
			LSuserMaster objuser, LSuserMaster objuser2, Date fromdate2, Date todate2, int j,
			LSprojectmaster lstprojectforfilter2, Integer testcode2, LSuserMaster objuser3, Date fromdate3,
			Date todate3, int k, LSprojectmaster lstprojectforfilter3, Integer testcode3);

	List<Logilabordermaster> findByOrdercancellAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmaster(
			int i, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, LSuserMaster objuser,
			LSuserMaster objuser2, Date fromdate2, Date todate2, int j, LSprojectmaster lstprojectforfilter2,
			LSuserMaster objuser3, Date fromdate3, Date todate3, int k, LSprojectmaster lstprojectforfilter3,
			Pageable pageable);

	long countByOrdercancellAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmaster(
			int i, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, LSuserMaster objuser,
			LSuserMaster objuser2, Date fromdate2, Date todate2, int j, LSprojectmaster lstprojectforfilter2,
			LSuserMaster objuser3, Date fromdate3, Date todate3, int k, LSprojectmaster lstprojectforfilter3);

	List<Logilabordermaster> findByOrdercancellAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndLsprojectmasterAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterAndTestcode(
			int i, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, Integer testcode,
			LSuserMaster objuser, LSuserMaster objuser2, Date fromdate2, Date todate2, int j,
			LSprojectmaster lstprojectforfilter2, Integer testcode2, LSuserMaster objuser3, Date fromdate3,
			Date todate3, int k, LSprojectmaster lstprojectforfilter3, Integer testcode3, Pageable pageable);

	long countByOrdercancellAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndLsprojectmasterAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterAndTestcode(
			int i, LSprojectmaster lstprojectforfilter, Date fromdate, Date todate, Integer testcode,
			LSuserMaster objuser, LSuserMaster objuser2, Date fromdate2, Date todate2, int j,
			LSprojectmaster lstprojectforfilter2, Integer testcode2, LSuserMaster objuser3, Date fromdate3,
			Date todate3, int k, LSprojectmaster lstprojectforfilter3, Integer testcode3);

//	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrderByBatchcodeDesc(
//			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Pageable pageable);

//	List<Logilabordermaster> findByOrderflagAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullAndAssignedtoIsNullOrderByBatchcodeDesc(
//			String string, int i, LSuserMaster objuser, Date fromdate, Date todate, int j,
//			List<LSprojectmaster> lstproject, Pageable pageable);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNull(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	long countByOrderflagAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullAndAssignedtoIsNull(
			String string, int i, LSuserMaster objuser, Date fromdate, Date todate, int j,
			List<LSprojectmaster> lstproject);

//	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrderByBatchcodeDesc(
//			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Pageable pageable);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNull(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Pageable pageable);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, int i, LSuserMaster objuser, Date fromdate, Date todate,
			Pageable pageable);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNull(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate);

	long countByOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNull(
			String string, List<LSprojectmaster> lstproject, int i, LSuserMaster objuser, Date fromdate, Date todate);

	List<Logilabordermaster> findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNull(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Pageable pageable);

	long countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNull(int i,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate);


	List<Logilaborders> findByOrderflagAndLsprojectmasterInAndFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNull(
			String orderflag, List<LSprojectmaster> lstproject, Integer filetype, LSprojectmaster lsprojectmaster,
			Date fromdate, Date todate);

	List<Logilaborders> findByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrderByBatchcodeDesc(
			String orderflag, List<LSsamplemaster> lstsample, Integer filetype, LSprojectmaster lsprojectmaster,
			Date fromdate, Date todate, int i, LSuserMaster lsuserMaster);

	List<Logilaborders> findByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoption(
			String orderflag, List<LSsamplemaster> lstsample, Integer filetype, LSprojectmaster lsprojectmaster,
			Date fromdate, Date todate, int i);

//
//	List<Logilaborders> findByOrderflagAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNull(
//			String orderflag, List<LSprojectmaster> lstproject, Integer filetype, Date fromdate, Date todate);

	List<Logilabordermaster> findByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
			String string, int i, Date fromdate, Date todate, Integer testcode, String string2, int j,
			LSuserMaster objuser, Date fromdate2, Date todate2, int k, Integer testcode2, String string3, int l,
			LSuserMaster objuser2, Date fromdate3, Date todate3, int m, Integer testcode3, String string4, int n,
			LSuserMaster objuser3, Date fromdate4, Date todate4, int o, List<LSprojectmaster> lstproject,
			Integer testcode4, String string5, int p, LSuserMaster objuser4, Date fromdate5, Date todate5,
			Integer testcode5, String string6, int q, LSuserMaster objuser5, Date fromdate6, Date todate6,
			Integer testcode6, String string7, int r, LSuserMaster objuser6, Date fromdate7, Date todate7,
			Integer testcode7, String string8, LSuserMaster objuser7, LSuserMaster objuser8, Date fromdate8,
			Date todate8, Integer testcode8, String string9, LSuserMaster objuser9, Date fromdate9, Date todate9,
			Integer testcode9, Pageable pageable);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Integer testcode,
			Pageable pageable);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode,
			Pageable pageable);

	long countByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndTestcode(
			String string, int i, Date fromdate, Date todate, Integer testcode, String string2, int j,
			LSuserMaster objuser, Date fromdate2, Date todate2, int k, Integer testcode2, String string3, int l,
			LSuserMaster objuser2, Date fromdate3, Date todate3, int m, Integer testcode3, String string4, int n,
			LSuserMaster objuser3, Date fromdate4, Date todate4, int o, List<LSprojectmaster> lstproject,
			Integer testcode4, String string5, int p, LSuserMaster objuser4, Date fromdate5, Date todate5,
			Integer testcode5, String string6, int q, LSuserMaster objuser5, Date fromdate6, Date todate6,
			Integer testcode6, String string7, int r, LSuserMaster objuser6, Date fromdate7, Date todate7,
			Integer testcode7, String string8, LSuserMaster objuser7, LSuserMaster objuser8, Date fromdate8,
			Date todate8, Integer testcode8, String string9, LSuserMaster objuser9, Date fromdate9, Date todate9,
			Integer testcode9);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcode(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, Integer testcode);

	List<Logilabordermaster> findByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
			String string, int i, Date fromdate, Date todate, Integer testcode, String string2, int j,
			LSuserMaster objuser, Date fromdate2, Date todate2, Integer testcode2, String string3, int k,
			LSuserMaster objuser2, Date fromdate3, Date todate3, Integer testcode3, String string4,
			List<LSprojectmaster> lstproject, int l, LSuserMaster objuser3, Date fromdate4, Date todate4,
			Integer testcode4, String string5, int m, Date fromdate5, Date todate5, List<LSuserMaster> usernotify,
			Integer testcode5, String string6, LSuserMaster objuser4, LSuserMaster objuser5, Date fromdate6,
			Date todate6, Integer testcode6, String string7, LSuserMaster objuser6, Date fromdate7, Date todate7,
			Integer testcode7, Pageable pageable);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode,
			Pageable pageable);

	long countByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndTestcode(
			String string, int i, Date fromdate, Date todate, Integer testcode, String string2, int j,
			LSuserMaster objuser, Date fromdate2, Date todate2, Integer testcode2, String string3, int k,
			LSuserMaster objuser2, Date fromdate3, Date todate3, Integer testcode3, String string4,
			List<LSprojectmaster> lstproject, int l, LSuserMaster objuser3, Date fromdate4, Date todate4,
			Integer testcode4, String string5, int m, Date fromdate5, Date todate5, List<LSuserMaster> usernotify,
			Integer testcode5, String string6, LSuserMaster objuser4, LSuserMaster objuser5, Date fromdate6,
			Date todate6, Integer testcode6, String string7, LSuserMaster objuser6, Date fromdate7, Date todate7,
			Integer testcode7);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndTestcode(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode);

	List<Logilabordermaster> findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcode(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode, Pageable pageable);

	long countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcode(int i,
			List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer testcode);

	List<Logilabordermaster> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, List<Long> directorycode, int j, Date fromdate2,
			Date todate2, List<Long> directorycode2, int k, Date fromdate3, Date todate3, LSuserMaster objuser2,
			List<Long> directorycode3, int l, Date fromdate4, Date todate4, LSuserMaster objuser3, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser,
			Pageable pageable);

	long countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMaster(
			int i, Date fromdate, Date todate, LSuserMaster objuser, List<Long> directorycode, int j, Date fromdate2,
			Date todate2, List<Long> directorycode2, int k, Date fromdate3, Date todate3, LSuserMaster objuser2,
			List<Long> directorycode3, int l, Date fromdate4, Date todate4, LSuserMaster objuser3);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndDirectorycodeIsNull(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndDirectorycodeIsNull(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser);

	List<Logilabordermaster> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, Integer testcode, List<Long> directorycode, int j,
			Date fromdate2, Date todate2, Integer testcode2, List<Long> directorycode2, int k, Date fromdate3,
			Date todate3, LSuserMaster objuser2, Integer testcode3, List<Long> directorycode3, int l, Date fromdate4,
			Date todate4, LSuserMaster objuser3, Integer testcode4, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, Integer testcode, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, Integer testcode,
			Pageable pageable);

	long countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcode(
			int i, Date fromdate, Date todate, LSuserMaster objuser, Integer testcode, List<Long> directorycode, int j,
			Date fromdate2, Date todate2, Integer testcode2, List<Long> directorycode2, int k, Date fromdate3,
			Date todate3, LSuserMaster objuser2, Integer testcode3, List<Long> directorycode3, int l, Date fromdate4,
			Date todate4, LSuserMaster objuser3, Integer testcode4);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndDirectorycodeIsNull(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, Integer testcode);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeAndDirectorycodeIsNull(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, Integer testcode);

	List<Logilabordermaster> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, String string, int j, int k, Date fromdate2,
			Date todate2, LSuserMaster objuser2, String string2, List<Long> directorycode, int l, Date fromdate3,
			Date todate3, String string3, int m, List<Long> directorycode2, int n, Date fromdate4, Date todate4,
			String string4, List<Long> directorycode3, int o, Date fromdate5, Date todate5, LSuserMaster objuser3,
			String string5, int p, List<Long> directorycode4, int q, Date fromdate6, Date todate6,
			LSuserMaster objuser4, String string6, List<Long> directorycode5, int r, Date fromdate7, Date todate7,
			LSuserMaster objuser5, String string7, int s, List<Long> directorycode6, int t, Date fromdate8,
			Date todate8, LSuserMaster objuser6, String string8, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, String string, int j,
			Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, String string, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, String string,
			int j, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, String string,
			Pageable pageable);

	long countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNull(
			int i, Date fromdate, Date todate, LSuserMaster objuser, String string, int j, int k, Date fromdate2,
			Date todate2, LSuserMaster objuser2, String string2, List<Long> directorycode, int l, Date fromdate3,
			Date todate3, String string3, int m, List<Long> directorycode2, int n, Date fromdate4, Date todate4,
			String string4, List<Long> directorycode3, int o, Date fromdate5, Date todate5, LSuserMaster objuser3,
			String string5, int p, List<Long> directorycode4, int q, Date fromdate6, Date todate6,
			LSuserMaster objuser4, String string6, List<Long> directorycode5, int r, Date fromdate7, Date todate7,
			LSuserMaster objuser5, String string7, int s, List<Long> directorycode6, int t, Date fromdate8,
			Date todate8, LSuserMaster objuser6, String string8);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndDirectorycodeIsNull(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, String string, int j);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndDirectorycodeIsNull(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, String string);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndDirectorycodeIsNull(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, String string,
			int j);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndDirectorycodeIsNull(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, String string);

	List<Logilabordermaster> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, String string, int j, Integer testcode, int k,
			Date fromdate2, Date todate2, LSuserMaster objuser2, String string2, Integer testcode2,
			List<Long> directorycode, int l, Date fromdate3, Date todate3, String string3, int m, Integer testcode3,
			List<Long> directorycode2, int n, Date fromdate4, Date todate4, String string4, Integer testcode4,
			List<Long> directorycode3, int o, Date fromdate5, Date todate5, LSuserMaster objuser3, String string5,
			int p, Integer testcode5, List<Long> directorycode4, int q, Date fromdate6, Date todate6,
			LSuserMaster objuser4, String string6, Integer testcode6, List<Long> directorycode5, int r, Date fromdate7,
			Date todate7, LSuserMaster objuser5, String string7, int s, Integer testcode7, List<Long> directorycode6,
			int t, Date fromdate8, Date todate8, LSuserMaster objuser6, String string8, Integer testcode8,
			Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, String string, int j, Integer testcode,
			Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, String string, Integer testcode,
			Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, String string,
			int j, Integer testcode, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, String string,
			Integer testcode, Pageable pageable);

	long countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcode(
			int i, Date fromdate, Date todate, LSuserMaster objuser, String string, int j, Integer testcode, int k,
			Date fromdate2, Date todate2, LSuserMaster objuser2, String string2, Integer testcode2,
			List<Long> directorycode, int l, Date fromdate3, Date todate3, String string3, int m, Integer testcode3,
			List<Long> directorycode2, int n, Date fromdate4, Date todate4, String string4, Integer testcode4,
			List<Long> directorycode3, int o, Date fromdate5, Date todate5, LSuserMaster objuser3, String string5,
			int p, Integer testcode5, List<Long> directorycode4, int q, Date fromdate6, Date todate6,
			LSuserMaster objuser4, String string6, Integer testcode6, List<Long> directorycode5, int r, Date fromdate7,
			Date todate7, LSuserMaster objuser5, String string7, int s, Integer testcode7, List<Long> directorycode6,
			int t, Date fromdate8, Date todate8, LSuserMaster objuser6, String string8, Integer testcode8);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeAndDirectorycodeIsNull(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, String string, int j, Integer testcode);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeAndDirectorycodeIsNull(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, String string, Integer testcode);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeAndDirectorycodeIsNull(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, String string,
			int j, Integer testcode);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeAndDirectorycodeIsNull(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, String string,
			Integer testcode);

	List<Logilabordermaster> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, String string, List<Long> directorycode, int j,
			Date fromdate2, Date todate2, String string2, List<Long> directorycode2, int k, Date fromdate3,
			Date todate3, LSuserMaster objuser2, String string3, List<Long> directorycode3, int l, Date fromdate4,
			Date todate4, LSuserMaster objuser3, String string4, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, String string, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, String string,
			Pageable pageable);

	long countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, String string, List<Long> directorycode, int j,
			Date fromdate2, Date todate2, String string2, List<Long> directorycode2, int k, Date fromdate3,
			Date todate3, LSuserMaster objuser2, String string3, List<Long> directorycode3, int l, Date fromdate4,
			Date todate4, LSuserMaster objuser3, String string4);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, String string);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, String string);

	List<Logilabordermaster> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, int j, List<Long> directorycode, int k,
			Date fromdate2, Date todate2, int l, List<Long> directorycode2, int m, Date fromdate3, Date todate3,
			LSuserMaster objuser2, int n, List<Long> directorycode3, int o, Date fromdate4, Date todate4,
			LSuserMaster objuser3, int p, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, int j, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, int j,
			Pageable pageable);

	long countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, int j, List<Long> directorycode, int k,
			Date fromdate2, Date todate2, int l, List<Long> directorycode2, int m, Date fromdate3, Date todate3,
			LSuserMaster objuser2, int n, List<Long> directorycode3, int o, Date fromdate4, Date todate4,
			LSuserMaster objuser3, int p);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, int j);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, int j);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, int j,
			Integer testcode, Pageable pageable);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, int j,
			Integer testcode);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, int j);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, int j);

	long countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, int j, List<Long> directorycode, int k,
			Date fromdate2, Date todate2, int l, List<Long> directorycode2, int m, Date fromdate3, Date todate3,
			LSuserMaster objuser2, int n, List<Long> directorycode3, int o, Date fromdate4, Date todate4,
			LSuserMaster objuser3, int p);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, int j,
			Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, int j, Pageable pageable);

	List<Logilabordermaster> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, int j, List<Long> directorycode, int k,
			Date fromdate2, Date todate2, int l, List<Long> directorycode2, int m, Date fromdate3, Date todate3,
			LSuserMaster objuser2, int n, List<Long> directorycode3, int o, Date fromdate4, Date todate4,
			LSuserMaster objuser3, int p, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeAndDirectorycodeIsNullOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, int j, Integer testcode,
			Pageable pageable);

	long countByLsprojectmasterIsNullAndTestcodeAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndDirectorycodeIsNull(
			int i, List<LSsamplemaster> lstsample1, int j, Date fromdate, Date todate, Integer testcode);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, String string, Integer testcode,
			Pageable pageable);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, String string, Integer testcode);

	long countByFiletypeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrViewoptionAndLsuserMasterInAndCreatedtimestampBetweenAndLsprojectmasterIsNullOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, List<LSsamplemaster> lstsample1, Date fromdate2, Date todate2, int j,
			LSuserMaster objuser, Date fromdate3, Date todate3, int k, LSuserMaster objuser2, Date fromdate4,
			Date todate4, int l, List<LSuserMaster> usernotify, Date fromdate5, Date todate5);

	@Transactional
	@Modifying
//	@Query(value = "SELECT o.batchcode,o.batchid,o.approved,o.approvelstatus, CAST((SELECT filenameuser FROM lsfile WHERE filecode = o.lsfile_filecode) AS varchar(10)) AS filename, testname, CAST((SELECT projectname FROM LSprojectmaster WHERE projectcode = o.lsprojectmaster_projectcode) AS varchar(10)) AS projectname, CAST((SELECT samplename FROM LSsamplemaster WHERE samplecode = o.lssamplemaster_samplecode) AS varchar(10)) AS samplename, o.createdtimestamp, o.keyword  FROM LSlogilablimsorderdetail o " +
	@Query(value = "SELECT * FROM LSlogilablimsorderdetail o "
			+ "WHERE (o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail WHERE lsprojectmaster_projectcode IN (SELECT projectcode FROM LSprojectmaster WHERE lsusersteam_teamcode IN (SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?2) AND status = 1)) "
			+ "AND (LOWER(o.batchid) LIKE LOWER(?1) OR LOWER(o.testname) LIKE LOWER(?1) OR LOWER(o.keyword) LIKE LOWER(?1))) "
			+ "OR (o.filetype = 0 AND (LOWER(o.batchid) LIKE LOWER(?1) OR LOWER(o.testname) LIKE LOWER(?1) OR LOWER(o.keyword) LIKE LOWER(?1))) "
			+ "OR (o.lsprojectmaster_projectcode IS NULL "
			+ "AND o.lssamplemaster_samplecode IN (SELECT DISTINCT m.samplecode FROM LSsamplemaster m JOIN lslogilablimsorderdetail d ON m.samplecode = d.lssamplemaster_samplecode WHERE m.lssitemaster_sitecode = ?4 AND m.status = 1) "
			+ "AND (LOWER(o.batchid) LIKE LOWER(?1) OR LOWER(o.testname) LIKE LOWER(?1) OR LOWER(o.keyword) LIKE LOWER(?1))) "
			+ "OR (o.lsprojectmaster_projectcode IS NULL " + "AND o.viewoption = 1 "
			+ "AND o.lsusermaster_usercode = (?2) "
			+ "AND (LOWER(o.batchid) LIKE LOWER(?1) OR LOWER(o.testname) LIKE LOWER(?1) OR LOWER(o.keyword) LIKE LOWER(?1))) "
			+ "OR (o.lsprojectmaster_projectcode IS NULL " + "AND o.viewoption = 2 "
			+ "AND o.lsusermaster_usercode = (?2) "
			+ "AND (LOWER(o.batchid) LIKE LOWER(?1) OR LOWER(o.testname) LIKE LOWER(?1) OR LOWER(o.keyword) LIKE LOWER(?1))) "
			+ "OR (o.viewoption = 3 " + "AND o.lsusermaster_usercode = (?2) "
			+ "AND o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail WHERE lsprojectmaster_projectcode IN (SELECT projectcode FROM LSprojectmaster WHERE lsusersteam_teamcode IN (SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?2) AND status = 1)) "
			+ "AND (LOWER(o.batchid) LIKE LOWER(?1) OR LOWER(o.testname) LIKE LOWER(?1) OR LOWER(o.keyword) LIKE LOWER(?1))) "
			+ "OR (o.viewoption = 3 " + "AND o.lsusermaster_usercode IN (?3) "
			+ "AND o.lsprojectmaster_projectcode IS NULL "
			+ "AND (LOWER(o.batchid) LIKE LOWER(?1) OR LOWER(o.testname) LIKE LOWER(?1) OR LOWER(o.keyword) LIKE LOWER(?1)))"
			+ "ORDER BY batchcode DESC OFFSET ?5 ROWS FETCH NEXT ?6 ROWS ONLY", nativeQuery = true)
	public List<LSlogilablimsorderdetail> getLSlogilablimsorderdetailsearchrecords(String searchkey, Integer userCode,
			List<LSuserMaster> userCodes, Integer sitecode, int i, Integer pageperorder);

	@Transactional
	@Query(value = "SELECT count(*) FROM LSlogilablimsorderdetail o "
			+ "WHERE (o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail WHERE lsprojectmaster_projectcode IN (SELECT projectcode FROM LSprojectmaster WHERE lsusersteam_teamcode IN (SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?2) AND status = 1)) "
			+ "AND (LOWER(o.batchid) LIKE LOWER(?1) OR LOWER(o.testname) LIKE LOWER(?1) OR LOWER(o.keyword) LIKE LOWER(?1))) "
			+ "OR (o.filetype = 0 AND (LOWER(o.batchid) LIKE LOWER(?1) OR LOWER(o.testname) LIKE LOWER(?1) OR LOWER(o.keyword) LIKE LOWER(?1))) "
			+ "OR (o.lsprojectmaster_projectcode IS NULL "
			+ "AND o.lssamplemaster_samplecode IN (SELECT DISTINCT m.samplecode FROM LSsamplemaster m JOIN lslogilablimsorderdetail d ON m.samplecode = d.lssamplemaster_samplecode WHERE m.lssitemaster_sitecode = ?4 AND m.status = 1) "
			+ "AND (LOWER(o.batchid) LIKE LOWER(?1) OR LOWER(o.testname) LIKE LOWER(?1) OR LOWER(o.keyword) LIKE LOWER(?1))) "
			+ "OR (o.lsprojectmaster_projectcode IS NULL " + "AND o.viewoption = 1 "
			+ "AND o.lsusermaster_usercode = (?2) "
			+ "AND (LOWER(o.batchid) LIKE LOWER(?1) OR LOWER(o.testname) LIKE LOWER(?1) OR LOWER(o.keyword) LIKE LOWER(?1))) "
			+ "OR (o.lsprojectmaster_projectcode IS NULL " + "AND o.viewoption = 2 "
			+ "AND o.lsusermaster_usercode = (?2) "
			+ "AND (LOWER(o.batchid) LIKE LOWER(?1) OR LOWER(o.testname) LIKE LOWER(?1) OR LOWER(o.keyword) LIKE LOWER(?1))) "
			+ "OR (o.viewoption = 3 " + "AND o.lsusermaster_usercode = (?2) "
			+ "AND o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail WHERE lsprojectmaster_projectcode IN (SELECT projectcode FROM LSprojectmaster WHERE lsusersteam_teamcode IN (SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?2) AND status = 1)) "
			+ "AND (LOWER(o.batchid) LIKE LOWER(?1) OR LOWER(o.testname) LIKE LOWER(?1) OR LOWER(o.keyword) LIKE LOWER(?1))) "
			+ "OR (o.viewoption = 3 " + "AND o.lsusermaster_usercode IN (?3) "
			+ "AND o.lsprojectmaster_projectcode IS NULL "
			+ "AND (LOWER(o.batchid) LIKE LOWER(?1) OR LOWER(o.testname) LIKE LOWER(?1) OR LOWER(o.keyword) LIKE LOWER(?1)))", nativeQuery = true)
	public Long countLSlogilablimsorderdetail(String searchkey, Integer userCode, List<LSuserMaster> userCodes,
			Integer sitecode);

	List<Logilabordermaster> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, String searchkeywords, String searchkeywords2,
			String searchkeywords3, int j, Date fromdate2, Date todate2, LSuserMaster objuser2, String searchkeywords4,
			List<Long> directorycode, int k, Date fromdate3, Date todate3, String searchkeywords5,
			String searchkeywords6, String searchkeywords7, List<Long> directorycode2, int l, Date fromdate4,
			Date todate4, String searchkeywords8, List<Long> directorycode3, int m, Date fromdate5, Date todate5,
			LSuserMaster objuser3, String searchkeywords9, String searchkeywords10, String searchkeywords11,
			List<Long> directorycode4, int n, Date fromdate6, Date todate6, LSuserMaster objuser4,
			String searchkeywords12, List<Long> directorycode5, int o, Date fromdate7, Date todate7,
			LSuserMaster objuser5, String searchkeywords13, String searchkeywords14, String searchkeywords15,
			List<Long> directorycode6, int p, Date fromdate8, Date todate8, LSuserMaster objuser6,
			String searchkeywords16, Pageable pageable);

	long countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, String searchkeywords, String searchkeywords2,
			String searchkeywords3, int j, Date fromdate2, Date todate2, LSuserMaster objuser2, String searchkeywords4,
			List<Long> directorycode, int k, Date fromdate3, Date todate3, String searchkeywords5,
			String searchkeywords6, String searchkeywords7, List<Long> directorycode2, int l, Date fromdate4,
			Date todate4, String searchkeywords8, List<Long> directorycode3, int m, Date fromdate5, Date todate5,
			LSuserMaster objuser3, String searchkeywords9, String searchkeywords10, String searchkeywords11,
			List<Long> directorycode4, int n, Date fromdate6, Date todate6, LSuserMaster objuser4,
			String searchkeywords12, List<Long> directorycode5, int o, Date fromdate7, Date todate7,
			LSuserMaster objuser5, String searchkeywords13, String searchkeywords14, String searchkeywords15,
			List<Long> directorycode6, int p, Date fromdate8, Date todate8, LSuserMaster objuser6,
			String searchkeywords16);

	List<LSlogilablimsorderdetail> findByOrderflagAndLsprojectmasterInAndAssignedtoIsNullAndLockeduserIsNotNull(
			String string, List<LSprojectmaster> lstproject);

	List<LSlogilablimsorderdetail> findByOrderflagAndLsprojectmasterIsNullAndElnmaterialInAndAssignedtoIsNullAndViewoptionAndLockeduserIsNotNull(
			String string, List<Elnmaterial> currentChunk, int i);

	List<LSlogilablimsorderdetail> findByOrderflagAndLsprojectmasterIsNullAndElnmaterialInAndAssignedtoIsNullAndViewoptionAndLsuserMasterAndLockeduserIsNotNullOrderByBatchcodeDesc(
			String string, List<Elnmaterial> currentChunk, int i, LSuserMaster lsuserMaster);

	List<Logilabordermaster> findByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, int i, Date fromdate, Date todate, String string2, int j, LSuserMaster objuser,
			Date fromdate2, Date todate2, String string3, int k, LSuserMaster objuser2, Date fromdate3, Date todate3,
			String string4, int l, Date fromdate4, Date todate4, List<LSuserMaster> usernotify, String string5,
			LSuserMaster objuser3, LSuserMaster objuser4, Date fromdate5, Date todate5, String string6,
			LSuserMaster objuser5, Date fromdate6, Date todate6, Pageable pageable);

	long countByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, int i, Date fromdate, Date todate, String string2, int j, LSuserMaster objuser,
			Date fromdate2, Date todate2, String string3, int k, LSuserMaster objuser2, Date fromdate3, Date todate3,
			String string4, int l, Date fromdate4, Date todate4, List<LSuserMaster> usernotify, String string5,
			LSuserMaster objuser3, LSuserMaster objuser4, Date fromdate5, Date todate5, String string6,
			LSuserMaster objuser5, Date fromdate6, Date todate6);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
			String string, List<LSsamplemaster> currentChunk, Date fromdate, Date todate, LSuserMaster objuser,
			Pageable pageable);

//	long countByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
//			String string, List<LSsamplemaster> currentChunk, Date fromdate, Date todate, LSuserMaster objuser);

//	List<Logilabordermaster> findByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//			String string, int i, Date fromdate, Date todate, String string2, int j, LSuserMaster objuser,
//			Date fromdate2, Date todate2, int k, String string3, int l, LSuserMaster objuser2, Date fromdate3,
//			Date todate3, int m, String string4, int n, LSuserMaster objuser3, Date fromdate4, Date todate4,
//			String string5, int o, LSuserMaster objuser4, Date fromdate5, Date todate5, String string6, int p,
//			LSuserMaster objuser5, Date fromdate6, Date todate6, String string7, LSuserMaster objuser6,
//			LSuserMaster objuser7, Date fromdate7, Date todate7, String string8, LSuserMaster objuser8, Date fromdate8,
//			Date todate8, Pageable pageable);

	long countByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, int i, Date fromdate, Date todate, String string2, int j, LSuserMaster objuser,
			Date fromdate2, Date todate2, int k, String string3, int l, LSuserMaster objuser2, Date fromdate3,
			Date todate3, int m, String string4, int n, LSuserMaster objuser3, Date fromdate4, Date todate4,
			String string5, int o, LSuserMaster objuser4, Date fromdate5, Date todate5, String string6, int p,
			LSuserMaster objuser5, Date fromdate6, Date todate6, String string7, LSuserMaster objuser6,
			LSuserMaster objuser7, Date fromdate7, Date todate7, String string8, LSuserMaster objuser8, Date fromdate8,
			Date todate8);

//	long countByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
//			String string, List<LSsamplemaster> currentChunk, Date fromdate, Date todate, LSuserMaster objuser);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatus(
			int i, LSuserMaster objuser, Date fromdate, Date todate, int j, int k, LSuserMaster objuser2,
			Date fromdate2, Date todate2, int l, int m, LSuserMaster objuser3, Date fromdate3, Date todate3, int n,
			int o, LSuserMaster objuser4, Date fromdate4, Date todate4, int p, LSuserMaster objuser5,
			LSuserMaster objuser6, Date fromdate5, Date todate5, int q, LSuserMaster objuser7, Date fromdate6,
			Date todate6, int r, Pageable pageable);

	List<Logilabordermaster> findByApprovelstatusAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsuserMasterNot(
			int i, List<LSsamplemaster> currentChunk, Date fromdate, Date todate, LSuserMaster objuser,
			Pageable pageable);

	long countByLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatus(
			int i, LSuserMaster objuser, Date fromdate, Date todate, int j, int k, LSuserMaster objuser2,
			Date fromdate2, Date todate2, int l, int m, LSuserMaster objuser3, Date fromdate3, Date todate3, int n,
			int o, LSuserMaster objuser4, Date fromdate4, Date todate4, int p, LSuserMaster objuser5,
			LSuserMaster objuser6, Date fromdate5, Date todate5, int q, LSuserMaster objuser7, Date fromdate6,
			Date todate6, int r);

//	long countByApprovelstatusAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsuserMasterNot(
//			int i, List<LSsamplemaster> currentChunk, Date fromdate, Date todate, LSuserMaster objuser);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeAndLsuserMasterNotOrderByBatchcodeDesc(
			String string, List<LSsamplemaster> currentChunk, Date fromdate, Date todate, int i, Integer testcode,
			LSuserMaster objuser, Pageable pageable);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeAndLsuserMasterNotOrderByBatchcodeDesc(
			String string, List<LSsamplemaster> currentChunk, Date fromdate, Date todate, Integer testcode,
			LSuserMaster objuser, Pageable pageable);

//	long countByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeAndLsuserMasterNot(
//			String string, List<LSsamplemaster> currentChunk, Date fromdate, Date todate, int i, Integer testcode,
//			LSuserMaster objuser);

//	long countByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeAndLsuserMasterNot(
//			String string, List<LSsamplemaster> currentChunk, Date fromdate, Date todate, Integer testcode,
//			LSuserMaster objuser);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeAndLsuserMasterNotOrderByBatchcodeDesc(
			String string, List<LSsamplemaster> currentChunk, Date fromdate, Date todate, Integer testcode,
			LSuserMaster objuser, Pageable pageable);

//	long countByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeAndLsuserMasterNot(
//			String string, List<LSsamplemaster> currentChunk, Date fromdate, Date todate, Integer testcode,
//			LSuserMaster objuser);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNullAndAssignedtoIsNullAndTestcodeOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatusAndTestcode(
			int i, LSuserMaster objuser, Date fromdate, Date todate, int j, Integer testcode, int k,
			LSuserMaster objuser2, Date fromdate2, Date todate2, int l, Integer testcode2, int m, LSuserMaster objuser3,
			Date fromdate3, Date todate3, int n, Integer testcode3, int o, LSuserMaster objuser4, Date fromdate4,
			Date todate4, int p, Integer testcode4, LSuserMaster objuser5, LSuserMaster objuser6, Date fromdate5,
			Date todate5, int q, Integer testcode5, LSuserMaster objuser7, Date fromdate6, Date todate6, int r,
			Integer testcode6, Pageable pageable);

	List<Logilabordermaster> findByApprovelstatusAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeAndLsuserMasterNot(
			int i, List<LSsamplemaster> currentChunk, Date fromdate, Date todate, Integer testcode,
			LSuserMaster objuser, Pageable pageable);

	long countByLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNullAndAssignedtoIsNullAndTestcodeOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatusAndTestcode(
			int i, LSuserMaster objuser, Date fromdate, Date todate, int j, Integer testcode, int k,
			LSuserMaster objuser2, Date fromdate2, Date todate2, int l, Integer testcode2, int m, LSuserMaster objuser3,
			Date fromdate3, Date todate3, int n, Integer testcode3, int o, LSuserMaster objuser4, Date fromdate4,
			Date todate4, int p, Integer testcode4, LSuserMaster objuser5, LSuserMaster objuser6, Date fromdate5,
			Date todate5, int q, Integer testcode5, LSuserMaster objuser7, Date fromdate6, Date todate6, int r,
			Integer testcode6);

//	long countByApprovelstatusAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeAndLsuserMasterNot(
//			int i, List<LSsamplemaster> currentChunk, Date fromdate, Date todate, Integer testcode,
//			LSuserMaster objuser);

//	List<Logilabordermaster> findByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
//			String string, List<LSsamplemaster> currentChunk, Date fromdate, Date todate, int i, LSuserMaster objuser,
//			Pageable pageable);

//	List<Logilabordermaster> findByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
//			String string, List<LSsamplemaster> currentChunk, Date fromdate, Date todate, LSuserMaster objuser,
//			Pageable pageable);

//	Long countByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndLsuserMasterNot(
//			String string, List<LSsamplemaster> currentChunk, Date fromdate, Date todate, int i, LSuserMaster objuser);

	List<LSlogilablimsorderdetail> findByLsprojectmasterInAndFiletypeAndAssignedtoIsNullAndLsfile(
			List<LSprojectmaster> lstproject, Integer filetype, LSfile lsfile);

	List<LSlogilablimsorderdetail> findByLsprojectmasterIsNullAndLssamplemasterIsNullAndFiletypeAndAssignedtoIsNullAndLsfile(
			Integer filetype, LSfile lsfile);

	List<LSlogilablimsorderdetail> findByLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsfile(
			List<LSsamplemaster> currentChunk, Integer filetype, int i, LSfile lsfile);

	List<LSlogilablimsorderdetail> findByLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoption(
			List<LSsamplemaster> currentChunk, Integer filetype, int i);

	List<Logilabordermaster> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, String string, Integer testcode,
			List<Long> directorycode, int j, Date fromdate2, Date todate2, String string2, Integer testcode2,
			List<Long> directorycode2, int k, Date fromdate3, Date todate3, LSuserMaster objuser2, String string3,
			Integer testcode3, List<Long> directorycode3, int l, Date fromdate4, Date todate4, LSuserMaster objuser3,
			String string4, Integer testcode4, Pageable pageable);

	long countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, String string, Integer testcode,
			List<Long> directorycode, int j, Date fromdate2, Date todate2, String string2, Integer testcode2,
			List<Long> directorycode2, int k, Date fromdate3, Date todate3, LSuserMaster objuser2, String string3,
			Integer testcode3, List<Long> directorycode3, int l, Date fromdate4, Date todate4, LSuserMaster objuser3,
			String string4, Integer testcode4);

	List<Logilabordermaster> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, int j, Integer testcode, List<Long> directorycode,
			int k, Date fromdate2, Date todate2, int l, Integer testcode2, List<Long> directorycode2, int m,
			Date fromdate3, Date todate3, LSuserMaster objuser2, int n, Integer testcode3, List<Long> directorycode3,
			int o, Date fromdate4, Date todate4, LSuserMaster objuser3, int p, Integer testcode4, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeAndDirectorycodeIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, int j, Integer testcode,
			LSuserMaster objuser, Pageable pageable);

	long countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, int j, Integer testcode, List<Long> directorycode,
			int k, Date fromdate2, Date todate2, int l, Integer testcode2, List<Long> directorycode2, int m,
			Date fromdate3, Date todate3, LSuserMaster objuser2, int n, Integer testcode3, List<Long> directorycode3,
			int o, Date fromdate4, Date todate4, LSuserMaster objuser3, int p, Integer testcode4);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeAndDirectorycodeIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, int j, Integer testcode,
			LSuserMaster objuser);

	List<Logilabordermaster> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, int j, Integer testcode, List<Long> directorycode,
			int k, Date fromdate2, Date todate2, int l, Integer testcode2, List<Long> directorycode2, int m,
			Date fromdate3, Date todate3, LSuserMaster objuser2, int n, Integer testcode3, List<Long> directorycode3,
			int o, Date fromdate4, Date todate4, LSuserMaster objuser3, int p, Integer testcode4, Pageable pageable);

	List<Logilabordermaster> findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcode(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, int j,
			Integer testcode, Pageable pageable);

	long countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, int j, Integer testcode, List<Long> directorycode,
			int k, Date fromdate2, Date todate2, int l, Integer testcode2, List<Long> directorycode2, int m,
			Date fromdate3, Date todate3, LSuserMaster objuser2, int n, Integer testcode3, List<Long> directorycode3,
			int o, Date fromdate4, Date todate4, LSuserMaster objuser3, int p, Integer testcode4);

	long countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcode(
			List<LSsamplemaster> lstsample1, int i, Date fromdate, Date todate, LSuserMaster objuser, int j,
			Integer testcode);

	List<LSlogilablimsorderdetail> findByLssamplemasterIn(List<LSsamplemaster> lsSampleLst);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM LSlogilablimsorderdetail o "
			+ "WHERE (o.orderflag =?1 And filetype=?2 And createdtimestamp BETWEEN ?3 And ?4)"
			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And viewoption=?7 And lsusermaster_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus !=?6 And ordercancell IS NULL And assignedto_usercode IS NULL)"
			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And viewoption=?8 And lsusermaster_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus !=?6 And ordercancell IS NULL And assignedto_usercode IS NULL)"
			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And viewoption=?7 And lsusermaster_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus IS NULL And ordercancell IS NULL And assignedto_usercode IS NULL)"
			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And viewoption=?8 And lsusermaster_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus IS NULL And ordercancell IS NULL And assignedto_usercode IS NULL)"
			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And viewoption=?9 And lsusermaster_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus IS NULL And ordercancell IS NULL And assignedto_usercode IS NULL)"
			+ "OR(o.orderflag =?1 And lsusermaster_usercode =?5 And assignedto_usercode !=?5 And createdtimestamp BETWEEN ?3 And ?4 And assignedto_usercode IS NOT NULL)"
			+ "OR(o.orderflag =?1 And  assignedto_usercode=?5 And createdtimestamp BETWEEN ?3 And ?4)"
			+ "OR(o.orderflag =?1 And o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail WHERE lsprojectmaster_projectcode IN (SELECT DISTINCT projectcode FROM LSprojectmaster WHERE lsusersteam_teamcode IN (SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?5 and teamcode is not null) AND status = 1)) And createdtimestamp BETWEEN ?3 And ?4 "
			+ "AND approvelstatus !=?6 And ordercancell IS NULL And assignedto_usercode IS NULL)"
			+ "OR(o.orderflag =?1 And o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail WHERE lsprojectmaster_projectcode IN (SELECT DISTINCT projectcode FROM LSprojectmaster WHERE lsusersteam_teamcode IN (SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?5 and teamcode is not null) AND status = 1)) And createdtimestamp BETWEEN ?3 And ?4 "
			+ "And approvelstatus IS NULL And ordercancell IS NULL And assignedto_usercode IS NULL)"
			+ "OR(o.orderflag =?1 And o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail WHERE lsprojectmaster_projectcode IN (SELECT DISTINCT projectcode FROM LSprojectmaster WHERE lsusersteam_teamcode IN (SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?5 and teamcode is not null) AND status = 1)) And createdtimestamp BETWEEN ?3 And ?4 "
			+ "ANd viewoption=?9 And lsusermaster_usercode =?5 AND approvelstatus !=?6 And ordercancell IS NULL And assignedto_usercode IS NULL)"
			+ "OR (o.orderflag =?10 And filetype=?2 And createdtimestamp BETWEEN ?3 And ?4)"
			+ "OR(o.orderflag =?10 And lsprojectmaster_projectcode IS NULL And viewoption=?7 And lsusermaster_usercode =?5 And ordercancell IS NULL And createdtimestamp BETWEEN ?3 And ?4 And assignedto_usercode IS NULL)"
			+ "OR(o.orderflag =?10 And lsprojectmaster_projectcode IS NULL And viewoption=?8 And lsusermaster_usercode =?5 And ordercancell IS NULL And createdtimestamp BETWEEN ?3 And ?4 And assignedto_usercode IS NULL)"
			+ "OR(o.orderflag =?10 And lsprojectmaster_projectcode IS NULL And viewoption=?9 And ordercancell IS NULL And createdtimestamp BETWEEN ?3 And ?4 and lsusermaster_usercode in(?11) And assignedto_usercode IS NULL)"
			+ "OR(o.orderflag =?10 And lsusermaster_usercode =?5 And assignedto_usercode !=?5 And createdtimestamp BETWEEN ?3 And ?4 And assignedto_usercode IS NOT NULL)"
			+ "OR(o.orderflag =?10 And  assignedto_usercode=?5 And createdtimestamp BETWEEN ?3 And ?4)"
			+ "OR(o.orderflag =?10 And o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail WHERE lsprojectmaster_projectcode IN (SELECT DISTINCT projectcode FROM LSprojectmaster WHERE lsusersteam_teamcode IN (SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?5 and teamcode is not null) AND status = 1))  And createdtimestamp BETWEEN ?3 And ?4 And ordercancell IS NULL And assignedto_usercode IS NULL)"
			+ "OR(o.orderflag =?10 And o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail WHERE lsprojectmaster_projectcode IN (SELECT DISTINCT projectcode FROM LSprojectmaster WHERE lsusersteam_teamcode IN (SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?5 and teamcode is not null) AND status = 1)) And viewoption=?9 And lsusermaster_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And ordercancell IS NULL And assignedto_usercode IS NULL)"
			+ "OR(o.lsprojectmaster_projectcode IS NULL And viewoption=?7 And lsusermaster_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus =?6 And assignedto_usercode IS NULL)"
			+ "OR(o.lsprojectmaster_projectcode IS NULL And viewoption=?8 And lsusermaster_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus =?6 And assignedto_usercode IS NULL)"
			+ "OR(o.lsprojectmaster_projectcode IS NULL And viewoption=?9 And lsusermaster_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus =?6 And assignedto_usercode IS NULL)"
			+ "OR(lsusermaster_usercode =?5 And assignedto_usercode !=?5 And createdtimestamp BETWEEN ?3 And ?4 And assignedto_usercode IS NOT NULL And approvelstatus =?6)"
			+ "OR(assignedto_usercode =?5  And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus =?6)"
			+ "OR(approvelstatus =?6 And o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail WHERE lsprojectmaster_projectcode IN (SELECT DISTINCT projectcode FROM LSprojectmaster WHERE lsusersteam_teamcode IN (SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?5 and teamcode is not null) AND status = 1)) And createdtimestamp BETWEEN ?3 And ?4 And assignedto_usercode IS NULL)"
			+ "OR(ordercancell =?12 And o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail WHERE lsprojectmaster_projectcode IN (SELECT DISTINCT projectcode FROM LSprojectmaster WHERE lsusersteam_teamcode IN (SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?5 and teamcode is not null) AND status = 1)) And createdtimestamp BETWEEN ?3 And ?4 And assignedto_usercode IS NULL)"
			+ "OR(lsprojectmaster_projectcode IS NULL And viewoption=?7 And lsusermaster_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And ordercancell =?12 And assignedto_usercode IS NULL)"
			+ "OR(lsprojectmaster_projectcode IS NULL And viewoption=?8 And lsusermaster_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And ordercancell =?12 And assignedto_usercode IS NULL)"
			+ "OR(lsprojectmaster_projectcode IS NULL And viewoption=?9 And lsusermaster_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And ordercancell =?12 And assignedto_usercode IS NULL)"
			+ "OR(lsusermaster_usercode =?5 And assignedto_usercode !=?5 And createdtimestamp BETWEEN ?3 And ?4 And assignedto_usercode IS NOT NULL And ordercancell =?12)"
			+ "OR(assignedto_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And ordercancell =?12)"
			+ "OR(approvelstatus =?6  And lsprojectmaster_projectcode IS NULL And elnmaterial_nmaterialcode in (select DISTINCT elnmaterial_nmaterialcode from lslogilablimsorderdetail where elnmaterial_nmaterialcode in (select m.nmaterialcode from elnmaterial m where  m.nsitecode =?13)) And createdtimestamp BETWEEN ?3 And ?4  And assignedto_usercode IS NULL And lsusermaster_usercode !=?5)"
			+ "OR(o.orderflag =?10 And lsprojectmaster_projectcode IS NULL And elnmaterial_nmaterialcode in (select DISTINCT elnmaterial_nmaterialcode from lslogilablimsorderdetail where elnmaterial_nmaterialcode in (select m.nmaterialcode from elnmaterial m where  m.nsitecode =?13))And createdtimestamp BETWEEN ?3 And ?4  And ordercancell IS NULL And assignedto_usercode IS NULL And lsusermaster_usercode !=?5)"
			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And elnmaterial_nmaterialcode in (select DISTINCT elnmaterial_nmaterialcode from lslogilablimsorderdetail where elnmaterial_nmaterialcode in (select m.nmaterialcode from elnmaterial m where  m.nsitecode =?13))And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus !=?6 And ordercancell IS NULL And assignedto_usercode IS NULL And lsusermaster_usercode !=?5)"
			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And elnmaterial_nmaterialcode in (select DISTINCT elnmaterial_nmaterialcode from lslogilablimsorderdetail where elnmaterial_nmaterialcode in (select m.nmaterialcode from elnmaterial m where  m.nsitecode =?13))And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus IS NULL And ordercancell IS NULL And assignedto_usercode IS NULL And lsusermaster_usercode !=?5)"
			+ "ORDER BY batchcode DESC OFFSET ?14 ROWS FETCH NEXT ?15 ROWS ONLY", nativeQuery = true)
	public List<LSlogilablimsorderdetail> getLSlogilablimsorderdetaildashboard(String string, int i, Date fromdate,
			Date todate, LSuserMaster objuser, int j, int k, int l, int m, String string2, List<LSuserMaster> list,
			int n, LSSiteMaster lsSiteMaster, int o, Integer integer);

	@Transactional
	@Modifying
//	@Query(value = "SELECT * FROM LSlogilablimsorderdetail o "
//			+ "WHERE (o.orderflag =?1 And filetype=?2 And createdtimestamp BETWEEN ?3 And ?4 And assignedto_usercode IS NULL)"
//			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And viewoption=?7 And lsusermaster_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus !=?6 And ordercancell IS NULL And assignedto_usercode IS NULL)"
//			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And viewoption=?8 And lsusermaster_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus !=?6 And ordercancell IS NULL And assignedto_usercode IS NULL)"
//			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And viewoption=?7 And lsusermaster_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus IS NULL And ordercancell IS NULL And assignedto_usercode IS NULL)"
//			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And viewoption=?8 And lsusermaster_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus IS NULL And ordercancell IS NULL And assignedto_usercode IS NULL)"
//			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And viewoption=?9 And lsusermaster_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus IS NULL And ordercancell IS NULL And assignedto_usercode IS NULL)"
//			+ "OR(o.orderflag =?1 And lsusermaster_usercode =?5 And assignedto_usercode !=?5 And createdtimestamp BETWEEN ?3 And ?4 And assignedto_usercode IS NOT NULL)"
//			+ "OR(o.orderflag =?1 And  assignedto_usercode=?5 And createdtimestamp BETWEEN ?3 And ?4)"
//			+ "OR(o.orderflag =?1 And o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail WHERE lsprojectmaster_projectcode IN (SELECT DISTINCT projectcode FROM LSprojectmaster WHERE lsusersteam_teamcode IN (SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?5 and teamcode is not null) AND status = 1)) And createdtimestamp BETWEEN ?3 And ?4 "
//			+ "AND approvelstatus !=?6 And ordercancell IS NULL And assignedto_usercode IS NULL)"
//			+ "OR(o.orderflag =?1 And o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail WHERE lsprojectmaster_projectcode IN (SELECT DISTINCT projectcode FROM LSprojectmaster WHERE lsusersteam_teamcode IN (SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?5 and teamcode is not null) AND status = 1)) And createdtimestamp BETWEEN ?3 And ?4 "
//			+ "And approvelstatus IS NULL And ordercancell IS NULL And assignedto_usercode IS NULL)"
//			+ "OR(o.orderflag =?1 And o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail WHERE lsprojectmaster_projectcode IN (SELECT DISTINCT projectcode FROM LSprojectmaster WHERE lsusersteam_teamcode IN (SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?5 and teamcode is not null) AND status = 1)) And createdtimestamp BETWEEN ?3 And ?4 "
//			+ "ANd viewoption=?9 And lsusermaster_usercode =?5 AND approvelstatus !=?6 And ordercancell IS NULL And assignedto_usercode IS NULL)"
//			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And elnmaterial_nmaterialcode in (select DISTINCT elnmaterial_nmaterialcode from lslogilablimsorderdetail where elnmaterial_nmaterialcode in (select m.nmaterialcode from elnmaterial m where  m.nsitecode =?10))And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus IS NULL And ordercancell IS NULL And assignedto_usercode IS NULL And lsusermaster_usercode !=?5)"
//			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And elnmaterial_nmaterialcode in (select DISTINCT elnmaterial_nmaterialcode from lslogilablimsorderdetail where elnmaterial_nmaterialcode in (select m.nmaterialcode from elnmaterial m where  m.nsitecode =?10))And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus !=?6 And ordercancell IS NULL And assignedto_usercode IS NULL And lsusermaster_usercode !=?5)"
//			+ "ORDER BY batchcode DESC OFFSET ?11 ROWS FETCH NEXT ?12 ROWS ONLY", nativeQuery = true)
	@Query(value ="SELECT * FROM LSlogilablimsorderdetail o "
	        + "WHERE "
	        + "    ("
	        + "        o.orderflag = ?1 "
	        + "        AND filetype = ?2 "
	        + "        AND createdtimestamp BETWEEN ?3 AND ?4 "
	        + "        AND assignedto_usercode IS NULL "
	        + "    ) "
	        + "    OR ("
	        + "        o.orderflag = ?1 "
	        + "        AND lsprojectmaster_projectcode IS NULL "
	        + "        AND ("
	        + "            (viewoption = ?7 OR viewoption = ?8 OR viewoption = ?9) "
	        + "            AND lsusermaster_usercode = ?5 "
	        + "            AND createdtimestamp BETWEEN ?3 AND ?4 "
	        + "            AND ("
	        + "                (approvelstatus != ?6 AND ordercancell IS NULL AND assignedto_usercode IS NULL) "
	        + "                OR (approvelstatus IS NULL AND ordercancell IS NULL AND assignedto_usercode IS NULL) "
	        + "            ) "
	        + "        ) "
	        + "    ) "
	        + "    OR ("
	        + "        o.orderflag = ?1 "
	        + "        AND ("
	        + "            (lsusermaster_usercode = ?5 AND assignedto_usercode != ?5 AND createdtimestamp BETWEEN ?3 AND ?4 AND assignedto_usercode IS NOT NULL) "
	        + "            OR (assignedto_usercode = ?5 AND createdtimestamp BETWEEN ?3 AND ?4) "
	        + "        ) "
	        + "    ) "
	        + "    OR ("
	        + "        o.orderflag = ?1 "
	        + "        AND ("
	        + "            ("
	        + "                o.lsprojectmaster_projectcode IN ("
	        + "                    SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail WHERE lsprojectmaster_projectcode IN ("
	        + "                        SELECT DISTINCT projectcode FROM LSprojectmaster WHERE lsusersteam_teamcode IN ("
	        + "                            SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?5 AND teamcode IS NOT NULL"
	        + "                        ) AND status = 1"
	        + "                    )"
	        + "                ) "
	        + "                AND createdtimestamp BETWEEN ?3 AND ?4 "
	        + "                AND approvelstatus != ?6 "
	        + "                AND ordercancell IS NULL "
	        + "                AND assignedto_usercode IS NULL "
	        + "            ) "
	        + "            OR ("
	        + "                o.lsprojectmaster_projectcode IN ("
	        + "                    SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail WHERE lsprojectmaster_projectcode IN ("
	        + "                        SELECT DISTINCT projectcode FROM LSprojectmaster WHERE lsusersteam_teamcode IN ("
	        + "                            SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?5 AND teamcode IS NOT NULL"
	        + "                        ) AND status = 1"
	        + "                    )"
	        + "                ) "
	        + "                AND createdtimestamp BETWEEN ?3 AND ?4 "
	        + "                AND approvelstatus IS NULL "
	        + "                AND ordercancell IS NULL "
	        + "                AND assignedto_usercode IS NULL "
	        + "            ) "
	        + "            OR ("
	        + "                o.lsprojectmaster_projectcode IN ("
	        + "                    SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail WHERE lsprojectmaster_projectcode IN ("
	        + "                        SELECT DISTINCT projectcode FROM LSprojectmaster WHERE lsusersteam_teamcode IN ("
	        + "                            SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?5 AND teamcode IS NOT NULL"
	        + "                        ) AND status = 1"
	        + "                    )"
	        + "                ) "
	        + "                AND createdtimestamp BETWEEN ?3 AND ?4 "
	        + "                AND viewoption = ?9 "
	        + "                AND lsusermaster_usercode = ?5 "
	        + "                AND approvelstatus != ?6 "
	        + "                AND ordercancell IS NULL "
	        + "                AND assignedto_usercode IS NULL "
	        + "            ) "
	        + "        ) "
	        + "    ) "
	        + "    OR ("
	        + "        o.orderflag = ?1 "
	        + "        AND lsprojectmaster_projectcode IS NULL "
	        + "        AND elnmaterial_nmaterialcode IN ("
	        + "            SELECT DISTINCT elnmaterial_nmaterialcode FROM lslogilablimsorderdetail WHERE elnmaterial_nmaterialcode IN ("
	        + "                SELECT m.nmaterialcode FROM elnmaterial m WHERE m.nsitecode = ?10"
	        + "            )"
	        + "        ) "
	        + "        AND createdtimestamp BETWEEN ?3 AND ?4 "
	        + "        AND approvelstatus IS NULL "
	        + "        AND ordercancell IS NULL "
	        + "        AND assignedto_usercode IS NULL "
	        + "        AND lsusermaster_usercode != ?5 "
	        + "    ) "
	        + "    OR ("
	        + "        o.orderflag = ?1 "
	        + "        AND lsprojectmaster_projectcode IS NULL "
	        + "        AND elnmaterial_nmaterialcode IN ("
	        + "            SELECT DISTINCT elnmaterial_nmaterialcode FROM lslogilablimsorderdetail WHERE elnmaterial_nmaterialcode IN ("
	        + "                SELECT m.nmaterialcode FROM elnmaterial m WHERE m.nsitecode = ?10"
	        + "            )"
	        + "        ) "
	        + "        AND createdtimestamp BETWEEN ?3 AND ?4 "
	        + "        AND approvelstatus != ?6 "
	        + "        AND ordercancell IS NULL "
	        + "        AND assignedto_usercode IS NULL "
	        + "        AND lsusermaster_usercode != ?5 "
	        + "    ) "
	        + "ORDER BY batchcode DESC "
	        + "OFFSET ?11 ROWS FETCH NEXT ?12 ROWS ONLY", nativeQuery = true)
	List<LSlogilablimsorderdetail> getLSlogilablimsorderdetaildashboardforcompleted(
	        String string, int i, Date fromdate, Date todate, LSuserMaster objuser, int j, int k, int l, int m,
	        LSSiteMaster lssitemaster, int n, Integer pageperorder);

	@Transactional
	@Modifying
	@Query(value = "WITH TeamProjects AS (SELECT lsprojectmaster_projectcode "
			+ " FROM LSlogilablimsorderdetail   WHERE lsprojectmaster_projectcode IN ("
			+ " SELECT DISTINCT projectcode FROM LSprojectmaster " + " WHERE lsusersteam_teamcode IN ("
			+ " SELECT teamcode FROM LSuserteammapping " + " WHERE lsuserMaster_usercode = ?5 AND teamcode IS NOT NULL"
			+ " ) AND status = 1))" + "SELECT  *  FROM LSlogilablimsorderdetail o "
			+ "WHERE (o.orderflag =?1 And filetype=?2 And createdtimestamp BETWEEN ?3 And ?4)"
			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And viewoption=?6 And lsusermaster_usercode =?5 And ordercancell IS NULL And createdtimestamp BETWEEN ?3 And ?4 And assignedto_usercode IS NULL)"
			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And viewoption=?7 And lsusermaster_usercode =?5 And ordercancell IS NULL And createdtimestamp BETWEEN ?3 And ?4 And assignedto_usercode IS NULL)"
			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And viewoption=?8 And ordercancell IS NULL And createdtimestamp BETWEEN ?3 And ?4 and lsusermaster_usercode in(?9) And assignedto_usercode IS NULL)"
			+ "OR(o.orderflag =?1 And lsusermaster_usercode =?5 And assignedto_usercode !=?5 And createdtimestamp BETWEEN ?3 And ?4 And assignedto_usercode IS NOT NULL)"
			+ "OR(o.orderflag =?1 And  assignedto_usercode=?5 And createdtimestamp BETWEEN ?3 And ?4)"
//			+ "OR(o.orderflag =?1 And o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail WHERE lsprojectmaster_projectcode IN (SELECT DISTINCT projectcode FROM LSprojectmaster WHERE lsusersteam_teamcode IN (SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?5 and teamcode is not null) AND status = 1))  And createdtimestamp BETWEEN ?3 And ?4 And ordercancell IS NULL And assignedto_usercode IS NULL)"
//			+ "OR(o.orderflag =?1 And o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail WHERE lsprojectmaster_projectcode IN (SELECT DISTINCT projectcode FROM LSprojectmaster WHERE lsusersteam_teamcode IN (SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = ?5 and teamcode is not null) AND status = 1)) And viewoption=?8 And lsusermaster_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And ordercancell IS NULL And assignedto_usercode IS NULL)"
			+ "OR(o.orderflag =?1 And o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM TeamProjects)  And createdtimestamp BETWEEN ?3 And ?4 And ordercancell IS NULL And assignedto_usercode IS NULL)"
			+ "OR(o.orderflag =?1 And o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM TeamProjects) And viewoption=?8 And lsusermaster_usercode =?5 And createdtimestamp BETWEEN ?3 And ?4 And ordercancell IS NULL And assignedto_usercode IS NULL)"
			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And elnmaterial_nmaterialcode in (select DISTINCT elnmaterial_nmaterialcode from lslogilablimsorderdetail where elnmaterial_nmaterialcode in (select m.nmaterialcode from elnmaterial m where  m.nsitecode =?10))And createdtimestamp BETWEEN ?3 And ?4  And ordercancell IS NULL And assignedto_usercode IS NULL And lsusermaster_usercode !=?5)"
			+ "ORDER BY batchcode DESC OFFSET ?11 ROWS FETCH NEXT ?12 ROWS ONLY", nativeQuery = true)
	List<LSlogilablimsorderdetail> getLSlogilablimsorderdetaildashboardforpending(String string, int i, Date fromdate,
			Date todate, LSuserMaster objuser, int j, int k, int l, List<LSuserMaster> usernotify,
			LSSiteMaster lssitemaster, int m, Integer pageperorder);
	
	
	@Transactional
//	@Modifying
	@Query(value = "SELECT  *  FROM LSlogilablimsorderdetail o "
			+ "WHERE (o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And elnmaterial_nmaterialcode in (select DISTINCT elnmaterial_nmaterialcode from lslogilablimsorderdetail where elnmaterial_nmaterialcode in (select m.nmaterialcode from elnmaterial m where  m.nsitecode =?6))And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus !=?2 And ordercancell IS NULL And assignedto_usercode IS NULL And lsusermaster_usercode !=?5)"
			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And elnmaterial_nmaterialcode in (select DISTINCT elnmaterial_nmaterialcode from lslogilablimsorderdetail where elnmaterial_nmaterialcode in (select m.nmaterialcode from elnmaterial m where  m.nsitecode =?6))And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus IS NULL And ordercancell IS NULL And assignedto_usercode IS NULL And lsusermaster_usercode !=?5) ORDER BY  batchcode DESC", nativeQuery = true)
	List<LSlogilablimsorderdetail> getLSlogilablimsorderdetaildashboardformaterial(String string, int i, Date fromdate, Date todate,
			LSuserMaster objuser, LSSiteMaster lssitemaster);
	
	@Transactional
//	@Modifying
	@Query(value = "SELECT  * FROM LSlogilablimsorderdetail o "
			+ "WHERE (o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And elnmaterial_nmaterialcode in (select DISTINCT elnmaterial_nmaterialcode from lslogilablimsorderdetail where elnmaterial_nmaterialcode in (select m.nmaterialcode from elnmaterial m where  m.nsitecode =?2))And createdtimestamp BETWEEN ?3 And ?4  And ordercancell IS NULL And assignedto_usercode IS NULL And lsusermaster_usercode !=?5) ORDER BY  batchcode DESC", nativeQuery = true)
	List<LSlogilablimsorderdetail> getLSlogilablimsorderdetaildashboardformaterial(String string, LSSiteMaster lssitemaster, Date fromdate,
			Date todate, LSuserMaster objuser);
	
	
	@Transactional
//	@Modifying
	@Query(value = "SELECT  * FROM LSlogilablimsorderdetail o "
			+ "WHERE (approvelstatus =?1  And lsprojectmaster_projectcode IS NULL And elnmaterial_nmaterialcode in (select DISTINCT elnmaterial_nmaterialcode from lslogilablimsorderdetail where elnmaterial_nmaterialcode in (select m.nmaterialcode from elnmaterial m where  m.nsitecode =?5)) And createdtimestamp BETWEEN ?2 And ?3  And assignedto_usercode IS NULL And lsusermaster_usercode !=?4) ORDER BY  batchcode DESC", nativeQuery = true)
	List<LSlogilablimsorderdetail> getLSlogilablimsorderdetaildashboardforrejectmaterial(int i, Date fromdate, Date todate, LSuserMaster objuser,
			LSSiteMaster lssitemaster);

	@Transactional
//	@Modifying
	@Query(value = "SELECT  count(*)  FROM LSlogilablimsorderdetail o "
			+ "WHERE (o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And elnmaterial_nmaterialcode in (select DISTINCT elnmaterial_nmaterialcode from lslogilablimsorderdetail where elnmaterial_nmaterialcode in (select m.nmaterialcode from elnmaterial m where  m.nsitecode =?6))And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus !=?2 And ordercancell IS NULL And assignedto_usercode IS NULL And lsusermaster_usercode !=?5)"
			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And elnmaterial_nmaterialcode in (select DISTINCT elnmaterial_nmaterialcode from lslogilablimsorderdetail where elnmaterial_nmaterialcode in (select m.nmaterialcode from elnmaterial m where  m.nsitecode =?6))And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus IS NULL And ordercancell IS NULL And assignedto_usercode IS NULL And lsusermaster_usercode !=?5)", nativeQuery = true)
	long getLSlogilablimsorderdetaildashboardforcount(String string, int i, Date fromdate, Date todate,
			LSuserMaster objuser, LSSiteMaster lssitemaster);

	@Transactional
//	@Modifying
	@Query(value = "SELECT  count(*) FROM LSlogilablimsorderdetail o "
			+ "WHERE (o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And elnmaterial_nmaterialcode in (select DISTINCT elnmaterial_nmaterialcode from lslogilablimsorderdetail where elnmaterial_nmaterialcode in (select m.nmaterialcode from elnmaterial m where  m.nsitecode =?2))And createdtimestamp BETWEEN ?3 And ?4  And ordercancell IS NULL And assignedto_usercode IS NULL And lsusermaster_usercode !=?5)", nativeQuery = true)
	long getLSlogilablimsorderdetaildashboardforpendingcount(String string, LSSiteMaster lssitemaster, Date fromdate,
			Date todate, LSuserMaster objuser);

	@Transactional
//	@Modifying
	@Query(value = "SELECT  count(*) FROM LSlogilablimsorderdetail o "
			+ "WHERE (approvelstatus =?1  And lsprojectmaster_projectcode IS NULL And elnmaterial_nmaterialcode in (select DISTINCT elnmaterial_nmaterialcode from lslogilablimsorderdetail where elnmaterial_nmaterialcode in (select m.nmaterialcode from elnmaterial m where  m.nsitecode =?5)) And createdtimestamp BETWEEN ?2 And ?3  And assignedto_usercode IS NULL And lsusermaster_usercode !=?4)", nativeQuery = true)
	long getLSlogilablimsorderdetaildashboardforrejectcount(int i, Date fromdate, Date todate, LSuserMaster objuser,
			LSSiteMaster lssitemaster);

	@Transactional
//	@Modifying
	@Query(value = "SELECT  count(*)  FROM LSlogilablimsorderdetail o "
			+ "WHERE (o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And elnmaterial_nmaterialcode in (select DISTINCT elnmaterial_nmaterialcode from lslogilablimsorderdetail where elnmaterial_nmaterialcode in (select m.nmaterialcode from elnmaterial m where  m.nsitecode =?6))And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus !=?2 And ordercancell IS NULL And assignedto_usercode IS NULL And lsusermaster_usercode !=?5 And testcode=?7)"
			+ "OR(o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And elnmaterial_nmaterialcode in (select DISTINCT elnmaterial_nmaterialcode from lslogilablimsorderdetail where elnmaterial_nmaterialcode in (select m.nmaterialcode from elnmaterial m where  m.nsitecode =?6))And createdtimestamp BETWEEN ?3 And ?4 And approvelstatus IS NULL And ordercancell IS NULL And assignedto_usercode IS NULL And lsusermaster_usercode !=?5 And testcode=?7)", nativeQuery = true)
	public long getLSlogilablimsorderdetaildashboardforcompletecountfilter(String string, int i, Date fromdate,
			Date todate, LSuserMaster objuser, LSSiteMaster lssitemaster, Integer testcode);

	@Transactional
//	@Modifying
	@Query(value = "SELECT  count(*) FROM LSlogilablimsorderdetail o "
			+ "WHERE (o.orderflag =?1 And lsprojectmaster_projectcode IS NULL And elnmaterial_nmaterialcode in (select DISTINCT elnmaterial_nmaterialcode from lslogilablimsorderdetail where elnmaterial_nmaterialcode in (select m.nmaterialcode from elnmaterial m where  m.nsitecode =?2))And createdtimestamp BETWEEN ?3 And ?4  And ordercancell IS NULL And assignedto_usercode IS NULL And lsusermaster_usercode !=?5 And testcode=?6)", nativeQuery = true)
	long getLSlogilablimsorderdetaildashboardforpendingcountfilter(String string, LSSiteMaster lssitemaster,
			Date fromdate, Date todate, LSuserMaster objuser, Integer testcode);

	@Transactional
//	@Modifying
	@Query(value = "SELECT  count(*) FROM LSlogilablimsorderdetail o "
			+ "WHERE (approvelstatus =?1  And lsprojectmaster_projectcode IS NULL And elnmaterial_nmaterialcode in (select DISTINCT elnmaterial_nmaterialcode from lslogilablimsorderdetail where elnmaterial_nmaterialcode in (select m.nmaterialcode from elnmaterial m where  m.nsitecode =?5)) And createdtimestamp BETWEEN ?2 And ?3  And assignedto_usercode IS NULL And lsusermaster_usercode !=?4 And testcode=?6)", nativeQuery = true)
	long getLSlogilablimsorderdetaildashboardforrejectcountfilter(int i, Date fromdate, Date todate,
			LSuserMaster objuser, LSSiteMaster lssitemaster, Integer testcode);

	List<Logilabordermaster> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrOrderflagAndAssignedtoAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrOrderflagAndAssignedtoAndCreatedtimestampBetweenOrApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatusOrOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellOrderByBatchcodeDesc(String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, String string2, List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2, String string3, int j, Date fromdate3, Date todate3, String string4, int k, LSuserMaster objuser, Date fromdate4, Date todate4, int l, String string5, int m, LSuserMaster objuser2, Date fromdate5, Date todate5, int n, String string6, int o, LSuserMaster objuser3, Date fromdate6, Date todate6, int p, List<LSprojectmaster> lstproject3, String string7, int q, LSuserMaster objuser4, Date fromdate7, Date todate7, String string8, int r, LSuserMaster objuser5, Date fromdate8, Date todate8, String string9, int s, LSuserMaster objuser6, Date fromdate9, Date todate9, String string10, LSuserMaster objuser7, LSuserMaster objuser8, Date fromdate10, Date todate10, String string11, LSuserMaster objuser9, Date fromdate11, Date todate11, String string12, List<LSprojectmaster> lstproject4, Date fromdate12, Date todate12, String string13, int t, Date fromdate13, Date todate13, String string14, int u, LSuserMaster objuser10, Date fromdate14, Date todate14, String string15, int v, LSuserMaster objuser11, Date fromdate15, Date todate15, String string16, List<LSprojectmaster> lstproject5, int w, LSuserMaster objuser12, Date fromdate16, Date todate16, String string17, int x, Date fromdate17, Date todate17, List<LSuserMaster> list, String string18, LSuserMaster objuser13, LSuserMaster objuser14, Date fromdate18, Date todate18, String string19, LSuserMaster objuser15, Date fromdate19, Date todate19, int y, List<LSprojectmaster> lstproject6, Date fromdate20, Date todate20, int z, LSuserMaster objuser16, Date fromdate21, Date todate21, int a, int b, LSuserMaster objuser17, Date fromdate22, Date todate22, int c, int d, LSuserMaster objuser18, Date fromdate23, Date todate23, int e, int f, LSuserMaster objuser19, Date fromdate24, Date todate24, int g, LSuserMaster objuser20, LSuserMaster objuser21, Date fromdate25, Date todate25, int h, LSuserMaster objuser22, Date fromdate26, Date todate26, int int1, int int2, List<LSprojectmaster> lstproject7, Date fromdate27, Date todate27, int int3, LSuserMaster objuser23, Date fromdate28, Date todate28, int int4, int int5, LSuserMaster objuser24, Date fromdate29, Date todate29, int int6, int int7, LSuserMaster objuser25, Date fromdate30, Date todate30, int int8, LSuserMaster objuser26, LSuserMaster objuser27, Date fromdate31, Date todate31, int int9, LSuserMaster objuser28, Date fromdate32, Date todate32, int int10, Pageable pageable
);

	List<Logilaborders> findByElnmaterialAndViewoptionAndLstestmasterlocalAndOrderdisplaytypeAndCreatedtimestampBetweenOrElnmaterialAndViewoptionAndLsuserMasterAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Elnmaterial elnmaterial, int i, LStestmasterlocal lstestmasterlocal, int j, Date fromdate, Date todate,
			Elnmaterial elnmaterial2, int k, LSuserMaster lsuserMaster, int l, Date fromdate2, Date todate2);


	List<LSlogilablimsorderdetail> findByOrderflagAndLockeduserIsNotNullAndLockeduserInAndAssignedtoIsNullOrderByBatchcodeDesc(
			String string, List<Integer> usercode);

	List<Logilaborders> findByLsprojectmasterInAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterInOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Elnmaterial elnmaterial, LStestmasterlocal lstestmasterlocal,
			Date fromdate, Date todate, Elnmaterial elnmaterial2, LStestmasterlocal lstestmasterlocal2, Date fromdate2,
			Date todate2, int i, Elnmaterial elnmaterial3, LStestmasterlocal lstestmasterlocal3, Date fromdate3,
			Date todate3, int j, LSuserMaster lsuserMaster, Elnmaterial elnmaterial4,
			LStestmasterlocal lstestmasterlocal4, Date fromdate4, Date todate4, int k,
			List<LSuserMaster> lstuserMaster);


	List<Logilaborders> findByLsprojectmasterInAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndFiletypeAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndFiletypeAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndFiletypeAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterInAndFiletypeAndOrderflagOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Elnmaterial elnmaterial, LStestmasterlocal lstestmasterlocal,
			Date fromdate, Date todate, Integer filetype, String orderflag, Elnmaterial elnmaterial2,
			LStestmasterlocal lstestmasterlocal2, Date fromdate2, Date todate2, int i, Integer filetype2,
			String orderflag2, Elnmaterial elnmaterial3, LStestmasterlocal lstestmasterlocal3, Date fromdate3,
			Date todate3, int j, LSuserMaster lsuserMaster, Integer filetype3, String orderflag3,
			Elnmaterial elnmaterial4, LStestmasterlocal lstestmasterlocal4, Date fromdate4, Date todate4, int k,
			List<LSuserMaster> lstuserMaster, Integer filetype4, String orderflag4);

	List<Logilaborders> findByLsprojectmasterInAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterInAndOrderflagAndApprovelstatusOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Elnmaterial elnmaterial, LStestmasterlocal lstestmasterlocal,
			Date fromdate, Date todate, String orderflag, Integer approvelstatus, Elnmaterial elnmaterial2,
			LStestmasterlocal lstestmasterlocal2, Date fromdate2, Date todate2, int i, String orderflag2,
			Integer approvelstatus2, Elnmaterial elnmaterial3, LStestmasterlocal lstestmasterlocal3, Date fromdate3,
			Date todate3, int j, LSuserMaster lsuserMaster, String orderflag3, Integer approvelstatus3,
			Elnmaterial elnmaterial4, LStestmasterlocal lstestmasterlocal4, Date fromdate4, Date todate4, int k,
			List<LSuserMaster> lstuserMaster, String orderflag4, Integer approvelstatus4);

	List<Logilaborders> findByLsprojectmasterInAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterInAndOrderflagOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Elnmaterial elnmaterial, LStestmasterlocal lstestmasterlocal,
			Date fromdate, Date todate, String orderflag, Elnmaterial elnmaterial2,
			LStestmasterlocal lstestmasterlocal2, Date fromdate2, Date todate2, int i, String orderflag2,
			Elnmaterial elnmaterial3, LStestmasterlocal lstestmasterlocal3, Date fromdate3, Date todate3, int j,
			LSuserMaster lsuserMaster, String orderflag3, Elnmaterial elnmaterial4,
			LStestmasterlocal lstestmasterlocal4, Date fromdate4, Date todate4, int k, List<LSuserMaster> lstuserMaster,
			String orderflag4);

	List<Logilaborders> findByLsprojectmasterInAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndFiletypeOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndFiletypeOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndFiletypeOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterInAndFiletypeOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Elnmaterial elnmaterial, LStestmasterlocal lstestmasterlocal,
			Date fromdate, Date todate, Integer filetype, Elnmaterial elnmaterial2,
			LStestmasterlocal lstestmasterlocal2, Date fromdate2, Date todate2, int i, Integer filetype2,
			Elnmaterial elnmaterial3, LStestmasterlocal lstestmasterlocal3, Date fromdate3, Date todate3, int j,
			LSuserMaster lsuserMaster, Integer filetype3, Elnmaterial elnmaterial4,
			LStestmasterlocal lstestmasterlocal4, Date fromdate4, Date todate4, int k, List<LSuserMaster> lstuserMaster,
			Integer filetype4);

	List<Logilaborders> findByLsprojectmasterInAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndFiletypeAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndFiletypeAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndFiletypeAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterInAndFiletypeAndOrderflagAndApprovelstatusOrderByBatchcodeDesc(
			List<LSprojectmaster> lstproject, Elnmaterial elnmaterial, LStestmasterlocal lstestmasterlocal,
			Date fromdate, Date todate, Integer filetype, String orderflag, Integer approvelstatus,
			Elnmaterial elnmaterial2, LStestmasterlocal lstestmasterlocal2, Date fromdate2, Date todate2, int i,
			Integer filetype2, String orderflag2, Integer approvelstatus2, Elnmaterial elnmaterial3,
			LStestmasterlocal lstestmasterlocal3, Date fromdate3, Date todate3, int j, LSuserMaster lsuserMaster,
			Integer filetype3, String orderflag3, Integer approvelstatus3, Elnmaterial elnmaterial4,
			LStestmasterlocal lstestmasterlocal4, Date fromdate4, Date todate4, int k, List<LSuserMaster> lstuserMaster,
			Integer filetype4, String orderflag4, Integer approvelstatus4);
	
List<Logilaborders> findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellOrAssignedtoAndCreatedtimestampBetweenAndOrdercancell(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int j, LSuserMaster lsuserMaster,
			Date fromdate2, Date todate2, int k, int l, LSuserMaster lsuserMaster2, Date fromdate3, Date todate3, int m,
			int n, LSuserMaster lsuserMaster3, Date fromdate4, Date todate4, int o, LSuserMaster lsuserMaster4,
			LSuserMaster lsuserMaster5, Date fromdate5, Date todate5, int p, LSuserMaster lsuserMaster6, Date fromdate6,
			Date todate6, int q);

	List<Logilaborders> findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndFiletypeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndFiletypeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndFiletypeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullAndFiletypeOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndFiletypeOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndFiletype(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer filetype, int j,
			LSuserMaster lsuserMaster, Date fromdate2, Date todate2, int k, Integer filetype2, int l,
			LSuserMaster lsuserMaster2, Date fromdate3, Date todate3, int m, Integer filetype3, int n,
			LSuserMaster lsuserMaster3, Date fromdate4, Date todate4, int o, Integer filetype4,
			LSuserMaster lsuserMaster4, LSuserMaster lsuserMaster5, Date fromdate5, Date todate5, int p,
			Integer filetype5, LSuserMaster lsuserMaster6, Date fromdate6, Date todate6, int q, Integer filetype6);

	List<Logilaborders> findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndFiletypeAndOrderflagOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndFiletypeAndOrderflagOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndFiletypeAndOrderflagOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullAndFiletypeAndOrderflagOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndFiletypeAndOrderflagOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndFiletypeAndOrderflag(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer filetype, String orderflag,
			int j, LSuserMaster lsuserMaster, Date fromdate2, Date todate2, int k, Integer filetype2, String orderflag2,
			int l, LSuserMaster lsuserMaster2, Date fromdate3, Date todate3, int m, Integer filetype3,
			String orderflag3, int n, LSuserMaster lsuserMaster3, Date fromdate4, Date todate4, int o,
			Integer filetype4, String orderflag4, LSuserMaster lsuserMaster4, LSuserMaster lsuserMaster5,
			Date fromdate5, Date todate5, int p, Integer filetype5, String orderflag5, LSuserMaster lsuserMaster6,
			Date fromdate6, Date todate6, int q, Integer filetype6, String orderflag6);

	List<Logilaborders> findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndFiletypeAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndFiletypeAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndFiletypeAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullAndFiletypeAndApprovelstatusOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndFiletypeAndApprovelstatusOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndFiletypeAndApprovelstatus(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer filetype, int j, int k,
			LSuserMaster lsuserMaster, Date fromdate2, Date todate2, int l, Integer filetype2, int m, int n,
			LSuserMaster lsuserMaster2, Date fromdate3, Date todate3, int o, Integer filetype3, int p, int q,
			LSuserMaster lsuserMaster3, Date fromdate4, Date todate4, int r, Integer filetype4, int s,
			LSuserMaster lsuserMaster4, LSuserMaster lsuserMaster5, Date fromdate5, Date todate5, int t,
			Integer filetype5, int u, LSuserMaster lsuserMaster6, Date fromdate6, Date todate6, int v,
			Integer filetype6, int w);

	List<Logilaborders> findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndOrderflagOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndOrderflagOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndOrderflagOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullAndOrderflagOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndOrderflagOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndOrderflag(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, String orderflag, int j,
			LSuserMaster lsuserMaster, Date fromdate2, Date todate2, int k, String orderflag2, int l,
			LSuserMaster lsuserMaster2, Date fromdate3, Date todate3, int m, String orderflag3, int n,
			LSuserMaster lsuserMaster3, Date fromdate4, Date todate4, int o, String orderflag4,
			LSuserMaster lsuserMaster4, LSuserMaster lsuserMaster5, Date fromdate5, Date todate5, int p,
			String orderflag5, LSuserMaster lsuserMaster6, Date fromdate6, Date todate6, int q, String orderflag6);

	List<Logilaborders> findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullAndOrderflagAndApprovelstatusOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndOrderflagAndApprovelstatusOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndOrderflagAndApprovelstatus(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, String orderflag, int j, int k,
			LSuserMaster lsuserMaster, Date fromdate2, Date todate2, int l, String orderflag2, int m, int n,
			LSuserMaster lsuserMaster2, Date fromdate3, Date todate3, int o, String orderflag3, int p, int q,
			LSuserMaster lsuserMaster3, Date fromdate4, Date todate4, int r, String orderflag4, int s,
			LSuserMaster lsuserMaster4, LSuserMaster lsuserMaster5, Date fromdate5, Date todate5, int t,
			String orderflag5, int u, LSuserMaster lsuserMaster6, Date fromdate6, Date todate6, int v,
			String orderflag6, int w);

	List<Logilaborders> findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullAndApprovelstatusOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndApprovelstatusOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndApprovelstatus(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int j, int k,
			LSuserMaster lsuserMaster, Date fromdate2, Date todate2, int l, int m, int n, LSuserMaster lsuserMaster2,
			Date fromdate3, Date todate3, int o, int p, int q, LSuserMaster lsuserMaster3, Date fromdate4, Date todate4,
			int r, int s, LSuserMaster lsuserMaster4, LSuserMaster lsuserMaster5, Date fromdate5, Date todate5, int t,
			int u, LSuserMaster lsuserMaster6, Date fromdate6, Date todate6, int v, int w);

	List<Logilaborders> findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndFiletypeAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndFiletypeAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndFiletypeAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullAndFiletypeAndOrderflagAndApprovelstatusOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndFiletypeAndOrderflagAndApprovelstatusOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndFiletypeAndOrderflagAndApprovelstatus(
			int i, List<LSprojectmaster> lstproject, Date fromdate, Date todate, Integer filetype, String orderflag,
			int j, int k, LSuserMaster lsuserMaster, Date fromdate2, Date todate2, int l, Integer filetype2,
			String orderflag2, int m, int n, LSuserMaster lsuserMaster2, Date fromdate3, Date todate3, int o,
			Integer filetype3, String orderflag3, int p, int q, LSuserMaster lsuserMaster3, Date fromdate4,
			Date todate4, int r, Integer filetype4, String orderflag4, int s, LSuserMaster lsuserMaster4,
			LSuserMaster lsuserMaster5, Date fromdate5, Date todate5, int t, Integer filetype5, String orderflag5,
			int u, LSuserMaster lsuserMaster6, Date fromdate6, Date todate6, int v, Integer filetype6,
			String orderflag6, int w);

	List<Logilabordermaster> LsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellOrAssignedtoAndCreatedtimestampBetweenAndOrdercancell(
			int i, LSuserMaster objuser, Date fromdate, Date todate, int j, int k, List<LSprojectmaster> lstproject,
			Date fromdate2, Date todate2, int l, LSuserMaster objuser2, Date fromdate3, Date todate3, int m, int n,
			LSuserMaster objuser3, Date fromdate4, Date todate4, int o, LSuserMaster objuser4, LSuserMaster objuser5,
			Date fromdate5, Date todate5, int p, LSuserMaster objuser6, Date fromdate6, Date todate6, int q,
			Pageable pageable);
	

	List<Logilaborders> findByOrderflagAndLsprojectmasterIsNullAndElnmaterialInAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoption(
			String orderflag, List<Elnmaterial> currentChunk, Date fromdate, Date todate, int i);

	List<Logilaborders> findByOrderflagAndLsprojectmasterIsNullAndElnmaterialInAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrderByBatchcodeDesc(
			String orderflag, List<Elnmaterial> currentChunk, Date fromdate, Date todate, int i,
			LSuserMaster lsuserMaster);

	List<Logilaborders> findByOrderflagAndApprovelstatusAndLsprojectmasterIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoption(
			String orderflag, Integer approvelstatus, List<Elnmaterial> currentChunk, Integer filetype, Date fromdate,
			Date todate, int i);

	List<Logilaborders> findByOrderflagAndApprovelstatusAndLsprojectmasterIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrderByBatchcodeDesc(
			String orderflag, Integer approvelstatus, List<Elnmaterial> currentChunk, Integer filetype, Date fromdate,
			Date todate, int i, LSuserMaster lsuserMaster);


	List<Logilaborders> findByOrderflagAndLsprojectmasterIsNullAndTestcodeAndElnmaterialInAndFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoption(
			String orderflag, Integer testcode, List<Elnmaterial> currentChunk, Integer filetype,
			LSprojectmaster lsprojectmaster, Date fromdate, Date todate, int i);

	List<Logilaborders> findByOrderflagAndLsprojectmasterIsNullAndTestcodeAndElnmaterialInAndFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrderByBatchcodeDesc(
			String orderflag, Integer testcode, List<Elnmaterial> currentChunk, Integer filetype,
			LSprojectmaster lsprojectmaster, Date fromdate, Date todate, int i, LSuserMaster lsuserMaster);

	List<Logilaborders> findByOrderflagAndLsprojectmasterIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMaster(
			String orderflag, List<Elnmaterial> currentChunk, Integer filetype, Date fromdate, Date todate, int i,
			LSuserMaster lsuserMaster);

	List<Logilaborders> findByOrderflagAndLsprojectmasterIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoption(
			String orderflag, List<Elnmaterial> currentChunk, Integer filetype, Date fromdate, Date todate, int i);

	List<Logilaborders> findByOrderflagAndLsprojectmasterIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrderByBatchcodeDesc(
			String orderflag, List<Elnmaterial> currentChunk, Integer filetype, Date fromdate, Date todate, int i,
			LSuserMaster lsuserMaster);


	List<Logilaborders> findByOrderflagAndFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNull(
			String orderflag, Integer filetype, LSprojectmaster lsprojectmaster, Date fromdate, Date todate);

	List<Logilaborders> findByOrderflagAndTestcodeAndLsprojectmasterIsNullAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInOrOrderflagAndTestcodeAndLsprojectmasterIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoption(
			String orderflag, Integer testcode, Integer filetype, Date fromdate, Date todate,
			List<Elnmaterial> currentChunk, String orderflag2, Integer testcode2, List<Elnmaterial> currentChunk2,
			Integer filetype2, Date fromdate2, Date todate2, int i);

	List<Logilaborders> findByOrderflagAndTestcodeAndLsprojectmasterIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrderByBatchcodeDesc(
			String orderflag, Integer testcode, List<Elnmaterial> currentChunk, Integer filetype, Date fromdate,
			Date todate, int i, LSuserMaster lsuserMaster);
	
	
	List<Logilaborders> findByAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(LSuserMaster lSuserMaster,
			Date fromdate, Date todate);

	List<ordersinterface> findByOrderflagAndLsprojectmasterInAndLsworkflowInAndCreatedtimestampBetweenOrAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, List<LSworkflow> lstworkflow, Date fromdate, Date todate,
			LSuserMaster objuser, Date fromdate2, Date todate2);

	List<LSlogilablimsorderdetail> findByLsuserMasterAndRepeat(LSuserMaster lsuserMaster, boolean b);

    @Transactional
	List<LSlogilablimsorderdetail> findByBatchcodeInOrderByBatchcodeAsc(List<Long> batchcodeauto);

	List<ordersinterface> findByOrderflagAndLsprojectmasterInAndLsworkflowInAndAssignedtoIsNullAndCreatedtimestampBetweenOrAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			String string, List<LSprojectmaster> lstproject, List<LSworkflow> lstworkflow, Date fromdate, Date todate,
			LSuserMaster objuser, Date fromdate2, Date todate2);

	List<LSlogilablimsorderdetail> findByBatchcodeInAndOrderflag(List<Long> batchcode, String string);
	@Transactional
	@Query(value = "select ordercancell from LSlogilablimsorderdetail where batchid = ?1", nativeQuery = true)
	String getRetirestatus(String templatename);

	List<LSlogilablimsorderdetail> findByBatchidInAndOrderflag(List<String> batchid, String string);

	List<Logilaborders> findByOrderflagAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndDirectorycodeInOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Integer filetype, Date fromdate, Date todate,
			String orderflag2, Integer filetype2, Date fromdate2, Date todate2, List<Long> directory_Code);

	List<Logilaborders> findByOrderflagAndLsprojectmasterInAndTestcodeAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndTestcodeAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndDirectorycodeIn(
			String orderflag, List<LSprojectmaster> lstproject, Integer testcode, Integer filetype, Date fromdate,
			Date todate, String orderflag2, Integer testcode2, Integer filetype2, Date fromdate2, Date todate2,
			List<Long> directory_Code);

	List<Logilaborders> findByOrderflagAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeAndDirectorycodeInOrderByBatchcodeDesc(
			String orderflag, List<LSprojectmaster> lstproject, Integer filetype, Date fromdate, Date todate,
			Integer testcode, String orderflag2, Integer filetype2, Date fromdate2, Date todate2, Integer testcode2,
			List<Long> directory_Code);

	List<Logilaborders> findByOrderflagAndApprovelstatusAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndApprovelstatusAndLsprojectmasterIsNullAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndDirectorycodeIn(
			String orderflag, Integer approvelstatus, List<LSprojectmaster> lstproject, Integer filetype, Date fromdate,
			Date todate, String orderflag2, Integer approvelstatus2, Integer filetype2, Date fromdate2, Date todate2,
			List<Long> directory_Code);

	List<Logilaborders> findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndDirectorycodeIn(
			String orderflag, List<LSprojectmaster> lstproject, Date fromdate, Date todate, String orderflag2,
			Date fromdate2, Date todate2, List<Long> directory_Code);

	List<LSlogilablimsorderdetail> findByLsprojectmasterInAndFiletypeAndAssignedtoIsNullAndLsfileAndOrdercancellNot(
			List<LSprojectmaster> lstproject, int filetype, LSfile lSfile, int i);
	}