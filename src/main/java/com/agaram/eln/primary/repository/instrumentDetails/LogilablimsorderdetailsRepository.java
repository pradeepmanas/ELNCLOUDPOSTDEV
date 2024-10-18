package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.fetchmodel.getorders.LogilabOrderDetails;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public interface LogilablimsorderdetailsRepository extends JpaRepository<LSlogilablimsorderdetail, Long> {
	
	LogilabOrderDetails findByBatchcode(Long batchcode);	
	
	@Transactional
	@Modifying
	@Query(value="update LSlogilablimsorderdetail set lockeduser=?1 ,lockedusername=?2 ,activeuser=?3 where batchcode=?4",nativeQuery=true)
	void UpdateOrderData(Integer usercode, String username, Integer activeuser, Long long1);

	@Transactional
	@Modifying
	@Query(value = "update LSlogilablimsorderdetail set lockeduser = null, lockedusername = null, activeuser = null where batchcode = ?1", nativeQuery = true)
	void UpdateOrderOnunlockData(Long batchcode);
	
	@Transactional
	@Query(value="select * from LSlogilablimsorderdetail where batchcode= ?1 ", nativeQuery=true)
	List<LSlogilablimsorderdetail> getOrderDetails(Long batchcode);

	long countByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndApprovelstatusNotOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndApprovelstatusIsNull(
			String string, int i, Date fromdate, Date todate, int j, String string2, int k, Date fromdate2,
			Date todate2);

	long countByOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndApprovelstatusNotOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndApprovelstatusIsNull(
			String string, int i, LSuserMaster objuser, Date fromdate, Date todate, int j, String string2, int k,
			LSuserMaster objuser2, Date fromdate2, Date todate2);

	long countByOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInAndAssignedtoIsNullAndApprovelstatusNotOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInAndAssignedtoIsNullAndApprovelstatusIsNull(
			String string, int i, Date fromdate, Date todate, List<LSuserMaster> usernotify, int j, String string2,
			int k, Date fromdate2, Date todate2, List<LSuserMaster> usernotify2);

	long countByOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusNotOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusIsNull(
			String string, LSuserMaster objuser, LSuserMaster objuser2, Date fromdate, Date todate, int i,
			String string2, LSuserMaster objuser3, LSuserMaster objuser4, Date fromdate2, Date todate2);

	long countByOrderflagAndAssignedtoAndCreatedtimestampBetweenAndApprovelstatusNotOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndApprovelstatusIsNull(
			String string, LSuserMaster objuser, Date fromdate, Date todate, int i, String string2,
			LSuserMaster objuser2, Date fromdate2, Date todate2);

	long countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndApprovelstatusNotOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndApprovelstatusIsNull(
			String string, List<LSprojectmaster> lstproject, Date fromdate, Date todate, int i, String string2,
			List<LSprojectmaster> lstproject2, Date fromdate2, Date todate2);

	long countByOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndApprovelstatusNotOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndApprovelstatusIsNull(
			String string, List<LSprojectmaster> lstproject, int i, LSuserMaster objuser, Date fromdate, Date todate,
			int j, String string2, List<LSprojectmaster> lstproject2, int k, LSuserMaster objuser2, Date fromdate2,
			Date todate2);
	

}
