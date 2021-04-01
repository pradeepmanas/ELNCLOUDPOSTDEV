package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public interface LSlogilablimsorderdetailRepository extends JpaRepository<LSlogilablimsorderdetail, Long> {
	
	@Transactional
	 @Modifying
	 @Query("update LSlogilablimsorderdetail o set o.batchid = ?1 where o.batchcode = ?2")
	 void setbatchidBybatchcode(String batchid, Long batchcode);
	
	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagOrderByBatchcodeDesc(Integer filetype, String orderflag);
	
	public long countByFiletypeAndOrderflagOrderByBatchcodeDesc(Integer filetype, String orderflag);
	
	public LSlogilablimsorderdetail findByBatchcode(Long batchcode);
	
	public LSlogilablimsorderdetail findByBatchid(String Batchid);
	
	public LSlogilablimsorderdetail findBylssamplefile(LSsamplefile lssamplefile);
	
	public List<LSlogilablimsorderdetail> findByOrderflag(String orderflag);
	
	public List<LSlogilablimsorderdetail> findByOrderflagAndLsprojectmaster(String orderflag,LSprojectmaster lsproject);
	
	public List<LSlogilablimsorderdetail> findByOrderflagAndLsprojectmasterIn(String orderflag,List<LSprojectmaster> lstproject);
	
	public long countByOrderflag(String orderflag);
	
	public long countByOrderflagAndLssamplefileIn(String orderflag, List<LSsamplefile> lssamplefile);
	
	public List<LSlogilablimsorderdetail> findByOrderflagAndLssamplefileIn(String orderflag, List<LSsamplefile> lssamplefile);
	
	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndLsprojectmasterInAndLsworkflowInOrderByBatchcodeDesc(Integer filetype, String orderflag,List<LSprojectmaster> lstproject, List<LSworkflow> lsworkflow);
	
	public List<LSlogilablimsorderdetail> findByOrderflagAndLsprojectmasterInAndLsworkflowInOrderByBatchcodeDesc(String orderflag,List<LSprojectmaster> lstproject, List<LSworkflow> lsworkflow);
	
	public long countByOrderflagAndLsprojectmasterInAndLsworkflowInOrderByBatchcodeDesc(String orderflag,List<LSprojectmaster> lstproject, List<LSworkflow> lsworkflow);
	
	public List<LSlogilablimsorderdetail> findByOrderflagAndLsprojectmasterInOrderByBatchcodeDesc(String orderflag,List<LSprojectmaster> lstproject);
	
	public long countByOrderflagAndLsprojectmasterInOrderByBatchcodeDesc(String orderflag,List<LSprojectmaster> lstproject);
	
	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndLsprojectmasterInOrderByBatchcodeDesc(Integer filetype, String orderflag,List<LSprojectmaster> lstproject);

	public List<LSlogilablimsorderdetail> findByFiletypeOrderByBatchcodeDesc(Integer filetype);
	
	public long countByFiletypeOrderByBatchcodeDesc(Integer filetype);
	
	public List<LSlogilablimsorderdetail> findByFiletypeAndLssamplefileInOrderByBatchcodeDesc(Integer filetype, List<LSsamplefile> lssamplefile);
	
	public long countByFiletypeAndOrderflagAndLssamplefileIn(Integer filetype, String orderflag, List<LSsamplefile> lssamplefile);
	
	public List<LSlogilablimsorderdetail> findByLsprojectmasterInOrderByBatchcodeDesc(List<LSprojectmaster> lstproject);
	
	public long countByLsprojectmasterInOrderByBatchcodeDesc(List<LSprojectmaster> lstproject);
	
	public List<LSlogilablimsorderdetail> findByOrderflagAndLssamplefileInAndLsprojectmasterIn(String orderflag, List<LSsamplefile> lssamplefile,List<LSprojectmaster> lstproject);
	
	public long countByOrderflagAndLssamplefileInAndLsprojectmasterIn(String orderflag, List<LSsamplefile> lssamplefile,List<LSprojectmaster> lstproject);
	
	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(Integer filetype, String orderflag, Date fromdate, Date todate);
	
	public long countByFiletypeAndOrderflagAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(Integer filetype, String orderflag, Date fromdate, Date todate);
	
	public List<LSlogilablimsorderdetail> findFirst15ByFiletypeAndOrderflagAndCreatedtimestampBetweenOrderByBatchcodeDesc(Integer filetype, String orderflag, Date fromdate, Date todate);
	
	public List<LSlogilablimsorderdetail> findFirst10ByFiletypeAndOrderflagAndBatchcodeLessThanAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(Integer filetype, String orderflag, Long batchcode, Date fromdate, Date todate);
	
	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndBatchcodeLessThanAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(Integer filetype, String orderflag, Long batchcode, Date fromdate, Date todate);
	
	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(Integer filetype, String orderflag, Date fromdate, Date todate);
	
	public long countByFiletypeAndOrderflagAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(Integer filetype, String orderflag, Date fromdate, Date todate);
	
	public List<LSlogilablimsorderdetail> findFirst15ByFiletypeAndOrderflagAndCompletedtimestampBetweenOrderByBatchcodeDesc(Integer filetype, String orderflag, Date fromdate, Date todate);
	
	public List<LSlogilablimsorderdetail> findFirst10ByFiletypeAndOrderflagAndBatchcodeLessThanAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(Integer filetype, String orderflag, Long batchcode, Date fromdate, Date todate);
	
	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndBatchcodeLessThanAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(Integer filetype, String orderflag, Long batchcode, Date fromdate, Date todate);
	
	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndLsprojectmasterInAndLsworkflowInAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Integer filetype, String orderflag,List<LSprojectmaster> lstproject, List<LSworkflow> lsworkflow, Date fromdate, Date todate);
	
	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag,List<LSprojectmaster> lstproject, Date fromdate, Date todate);
	
	public long countByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag,List<LSprojectmaster> lstproject, Date fromdate, Date todate);
	
	public List<LSlogilablimsorderdetail> findFirst15ByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenOrderByBatchcodeDesc(
			Integer filetype, String orderflag,List<LSprojectmaster> lstproject, Date fromdate, Date todate);
	
	public List<LSlogilablimsorderdetail> findFirst10ByFiletypeAndOrderflagAndBatchcodeLessThanAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, Long batchcode,List<LSprojectmaster> lstproject, Date fromdate, Date todate);
	
	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndBatchcodeLessThanAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, Long batchcode,List<LSprojectmaster> lstproject, Date fromdate, Date todate);
	
	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag,List<LSprojectmaster> lstproject, Date fromdate, Date todate);
	
	public long countByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag,List<LSprojectmaster> lstproject, Date fromdate, Date todate);
	
	public List<LSlogilablimsorderdetail> findFirst15ByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenOrderByBatchcodeDesc(
			Integer filetype, String orderflag,List<LSprojectmaster> lstproject, Date fromdate, Date todate);
	
	public List<LSlogilablimsorderdetail> findFirst10ByFiletypeAndOrderflagAndBatchcodeLessThanAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, Long batchcode,List<LSprojectmaster> lstproject, Date fromdate, Date todate);
	
	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndBatchcodeLessThanAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
			Integer filetype, String orderflag, Long batchcode,List<LSprojectmaster> lstproject, Date fromdate, Date todate);
	
	public List<LSlogilablimsorderdetail> findByFiletypeAndLsfileOrderByBatchcodeDesc(Integer filetype, LSfile lSfile);
	
	public List<LSlogilablimsorderdetail> findByFiletypeAndLsfileAndLsprojectmasterInOrderByBatchcodeDesc(Integer filetype, LSfile lSfile,List<LSprojectmaster> lstproject);
	
	public List<LSlogilablimsorderdetail> findByFiletypeAndLsprojectmasterInOrderByBatchcodeDesc(Integer filetype,List<LSprojectmaster> lstproject);
	
	public long countByLsworkflowAndOrderflag(LSworkflow lsworkflow, String orderflag);
	
	@Transactional
	@Modifying
	@Query("update LSlogilablimsorderdetail o set o.lsworkflow = null where o.lsworkflow = ?1")
	void setWorkflownullforcompletedorder(LSworkflow lsworkflow);
	

	@Transactional
	@Modifying
	@Query(value = "select batchcode from "
			+ "LSlogilablimsorderdetail where filetype = ?1 and orderflag = ?2 and createdtimestamp BETWEEN ?3 and ?4 and assignedto_usercode IS NULL ORDER BY batchcode DESC", nativeQuery=true)
	public List<Long> getBatchcodeonFiletypeAndFlagandcreateddate(Integer filetype, String flag, Date fromdate, Date todate);
		
	@Transactional
	@Modifying
	@Query(value = "select batchcode from "
			+ "LSlogilablimsorderdetail where filetype = ?1 and orderflag = ?2 and completedtimestamp BETWEEN ?3 and ?4 and assignedto_usercode IS NULL ORDER BY batchcode DESC", nativeQuery=true)
	public List<Long> getBatchcodeonFiletypeAndFlagandCompletedtime(Integer filetype, String flag, Date fromdate, Date todate);
		
	@Transactional
	@Modifying
	@Query(value = "select batchcode from "
			+ "LSlogilablimsorderdetail where filetype = ?1 and orderflag = ?2 and lsprojectmaster_projectcode in (?3) and "
			+ "createdtimestamp BETWEEN ?4 and ?5 and assignedto_usercode IS NULL ORDER BY batchcode DESC", nativeQuery=true)
	public List<Long> getBatchcodeonFiletypeAndFlagandProjectandcreateddate(Integer filetype, String flag,List<Integer> lstproject, Date fromdate, Date todate);
	

	@Transactional
	@Modifying
	@Query(value = "select batchcode from "
			+ "LSlogilablimsorderdetail where filetype = ?1 and orderflag = ?2 and lsprojectmaster_projectcode in (?3) and "
			+ "completedtimestamp BETWEEN ?4 and ?5 and assignedto_usercode IS NULL ORDER BY batchcode DESC", nativeQuery=true)
	public List<Long> getBatchcodeonFiletypeAndFlagandProjectandCompletedtime(Integer filetype, String flag,List<Integer> lstproject, Date fromdate, Date todate);
	
	@Transactional
	@Modifying
	@Query(value = "select batchcode from " + 
	"LSlogilablimsorderdetail where orderflag = ?1 and lsprojectmaster_projectcode in (?2) and lsworkflow_workflowcode in (?3) "
	+ "and lsuserMaster_usercode = ?4", nativeQuery=true)
	public List<Long> countByOrderflagAndLsprojectmasterInAndLsworkflowInOrderByBatchcodeDesc(String orderflag,List<LSprojectmaster> lstproject, List<LSworkflow> lsworkflow,Integer usercode);
	
	@Transactional
	@Modifying
	@Query(value = "select batchcode from " + 
	"LSlogilablimsorderdetail where orderflag = ?1 and lsprojectmaster_projectcode in (?2)", nativeQuery=true)
	public List<Long> countByOrderflagAndLsprojectmasterInAndLsworkflowInOrderByBatchcodeDesc(String orderflag,List<LSprojectmaster> lstproject);
	
	@Transactional
	@Modifying
	@Query(value = "select batchcode from"
	+ " LSlogilablimsorderdetail where orderflag = ?1 and lsprojectmaster_projectcode in (?2) and lsuserMaster_usercode = ?3", nativeQuery=true)
	public List<Long> countByOrderflagAndLsprojectmasterInOrderByBatchcodeDesc(String orderflag,List<LSprojectmaster> lstproject,Integer usercode);
	
	@Transactional
	@Modifying
	@Query(value = "select batchcode from"
	+ " LSlogilablimsorderdetail where orderflag = ?1 and lsprojectmaster_projectcode in (?2)", nativeQuery=true)
	public List<Long> countByOrderflagAndLsprojectmasterInOrderByBatchcodeDesc1(String orderflag,List<LSprojectmaster> lstproject);
	
	@Transactional
	@Modifying
	@Query(value = "select batchcode from"
	+" LSlogilablimsorderdetail where orderflag = ?1  and lssamplefile_filesamplecode in (?2) and lsprojectmaster_projectcode in (?3) "
	+ "and lsuserMaster_usercode = ?4", nativeQuery=true)
	public List<Long> countByOrderflagAndLssamplefileInAndLsprojectmasterIn(String orderflag, List<LSsamplefile> lssamplefile,List<LSprojectmaster> lstproject,Integer usercode);
	
	public List<Long> findBatchcodeByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(Integer filetype, String flag,List<LSprojectmaster> lstproject, Date fromdate, Date todate);
	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndLssamplefileIn(Integer filetype, String flag,List<Integer> lstproject, Date fromdate, Date todate, List<LSsamplefile> idList);
	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndLssamplefileIn(Integer filetype, String flag,List<Integer> lstproject, Date fromdate, Date todate, List<LSsamplefile> idList);
	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndCreatedtimestampBetweenAndLssamplefileIn(Integer filetype, String flag, Date fromdate, Date todate, List<LSsamplefile> idList);
	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndCompletedtimestampBetweenAndLssamplefileIn(Integer filetype, String flag, Date fromdate, Date todate, List<LSsamplefile> idList);
	public List<LSlogilablimsorderdetail> findByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(Integer filetype, LSuserMaster assignedto, Date fromdate, Date todate);
	public List<LSlogilablimsorderdetail> findByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(Integer filetype, LSuserMaster user, LSuserMaster assignedto, Date fromdate, Date todate);
	public Long countByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(Integer filetype, LSuserMaster assignedto, Date fromdate, Date todate);
	public Long countByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(Integer filetype, LSuserMaster user, LSuserMaster assignedto, Date fromdate, Date todate);
	
	@Transactional
	@Modifying
	@Query(value = "select batchcode from LSlogilablimsorderdetail where filetype = ?1 and assignedto_usercode = ?2 and "
			+ "createdtimestamp BETWEEN ?3 and ?4 ORDER BY batchcode DESC", nativeQuery=true)
	public List<Long> getBatchcodeonFiletypeAndFlagandAssignedtoAndCreatedtimestamp(Integer filetype, Integer assignedto, Date fromdate, Date todate);
	
	@Transactional
	@Modifying
	@Query(value = "select batchcode from LSlogilablimsorderdetail where filetype = ?1 and lsuserMaster_usercode = ?2 and assignedto_usercode <> ?3 and "
			+ "createdtimestamp BETWEEN ?4 and ?5 ORDER BY batchcode DESC", nativeQuery=true)
	public List<Long> getBatchcodeonFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestamp(Integer filetype, Integer user, Integer assignedto, Date fromdate, Date todate);
	@Transactional
	@Modifying
	@Query(value = "select batchcode from LSlogilablimsorderdetail where filetype = ?1 and orderflag = ?2 and lsuserMaster_usercode = ?3 and assignedto_usercode <> ?4 and "
			+ "createdtimestamp BETWEEN ?5 and ?6 ORDER BY batchcode DESC", nativeQuery=true)
	public List<Long> getBatchcodeonFiletypeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestamp(Integer filetype, String flag, Integer user, Integer assignedto, Date fromdate, Date todate);
	public List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(Integer filetype, String flag, LSuserMaster user, LSuserMaster assignedto, Date fromdate, Date todate);
	public Long countByFiletypeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(Integer filetype, String flag, LSuserMaster user, LSuserMaster assignedto, Date fromdate, Date todate);

	//kumaresan
	long countByFiletypeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(Integer filetype,String string, LSuserMaster lsuserMaster, Date fromdate, Date todate);

	List<LSlogilablimsorderdetail> findByFiletypeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(Integer filetype, String string, LSuserMaster lsuserMaster, Date fromdate, Date todate);

	@Transactional
	@Modifying
	@Query(value = "select batchcode from"
	+ " LSlogilablimsorderdetail where ( orderflag = ?1 and lsprojectmaster_projectcode in (?2) and approvelstatus = ?3 ) or ( approved= ?4 and orderflag = ?5 )", nativeQuery=true)
	public List<Long> countByOrderflagAndLsprojectmasterInOrderByBatchcodeDescInprogress(String orderflag,List<LSprojectmaster> lstproject, Integer approvelstatus , Integer approved,String orderflag1);


}
