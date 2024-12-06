package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.fetchmodel.getorders.LogilabOrderDetails;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
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
	
	@Transactional
	@Modifying
	@Query(value = "WITH TeamProjects AS ( " +
	        "   SELECT DISTINCT lsprojectmaster_projectcode " +
	        "   FROM LSlogilablimsorderdetail " +
	        "   WHERE lsprojectmaster_projectcode IN ( " +
	        "       SELECT projectcode " +
	        "       FROM LSprojectmaster " +
	        "       WHERE lsusersteam_teamcode IN ( " +
	        "           SELECT DISTINCT teamcode " +
	        "           FROM LSuserteammapping " +
	        "           WHERE lsuserMaster_usercode = ?5 AND teamcode IS NOT NULL) " +
	        "       AND status = 1 " +
	        ") " +
	        ") " +
	        "SELECT * FROM LSlogilablimsorderdetail o " +
	        "WHERE (o.orderflag = ?1 AND createdtimestamp BETWEEN ?3 AND ?4 " +
	        "AND ( " +
	        "   (filetype = ?2 AND (o.Approvelstatus != ?13 OR o.Approvelstatus IS NULL)) " +
	        "   OR (lsprojectmaster_projectcode IS NULL AND ordercancell IS NULL AND assignedto_usercode IS NULL " +
	        "       AND (o.Approvelstatus != ?13 OR o.Approvelstatus IS NULL) " +
	        "       AND ((viewoption = ?6 AND lsusermaster_usercode = ?5) OR " +
	        "             (viewoption = ?7 AND lsusermaster_usercode = ?5) OR " +
	        "             (viewoption = ?8 AND lsusermaster_usercode IN (?9))) " +
	        "   ) " +
	        "   OR (lsusermaster_usercode = ?5 AND assignedto_usercode != ?5 AND assignedto_usercode IS NOT NULL AND (o.Approvelstatus != ?13 OR o.Approvelstatus IS NULL)) " +
	        "   OR (assignedto_usercode = ?5 AND (o.Approvelstatus != ?13 OR o.Approvelstatus IS NULL)) " +
	        "   OR (o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM TeamProjects) " +
	        "       AND ordercancell IS NULL AND assignedto_usercode IS NULL AND (o.Approvelstatus != ?13 OR o.Approvelstatus IS NULL)) " +
	        "   OR (o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM TeamProjects) " +
	        "       AND lsusermaster_usercode = ?5 AND ordercancell IS NULL AND (o.Approvelstatus != ?13 OR o.Approvelstatus IS NULL)) " +
	        "   OR (lsprojectmaster_projectcode IS NULL AND elnmaterial_nmaterialcode IN " +
	        "       (SELECT DISTINCT elnmaterial_nmaterialcode " +
	        "        FROM lslogilablimsorderdetail " +
	        "        WHERE elnmaterial_nmaterialcode IN " +
	        "              (SELECT m.nmaterialcode " +
	        "               FROM elnmaterial m " +
	        "               WHERE m.nsitecode = ?10)) " +
	        "       AND ordercancell IS NULL AND assignedto_usercode IS NULL AND lsusermaster_usercode != ?5 " +
	        "       AND (o.Approvelstatus != ?13 OR o.Approvelstatus IS NULL)) " +
	        ")) " +
	        "OR ( " +
	        "    (o.orderflag = ?14 AND filetype = ?2 AND createdtimestamp BETWEEN ?3 AND ?4 AND assignedto_usercode IS NULL) " +
	        "    OR (o.orderflag = ?14 AND lsprojectmaster_projectcode IS NULL AND " +
	        "        ((viewoption = ?6 OR viewoption = ?7 OR viewoption = ?8) AND lsusermaster_usercode = ?5 " +
	        "         AND createdtimestamp BETWEEN ?3 AND ?4 AND " +
	        "         ((approvelstatus != ?13 AND ordercancell IS NULL AND assignedto_usercode IS NULL) " +
	        "         OR (approvelstatus IS NULL AND ordercancell IS NULL AND assignedto_usercode IS NULL))) " +
	        "    ) " +
	        "    OR (o.orderflag = ?14 AND " +
	        "        ((lsusermaster_usercode = ?5 AND assignedto_usercode != ?5 AND createdtimestamp BETWEEN ?3 AND ?4 AND assignedto_usercode IS NOT NULL) " +
	        "         OR (assignedto_usercode = ?5 AND createdtimestamp BETWEEN ?3 AND ?4)) " +
	        "    ) " +
	        "    OR (o.orderflag = ?14 AND " +
	        "        (o.lsprojectmaster_projectcode IN ( " +
	        "            SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail " +
	        "            WHERE lsprojectmaster_projectcode IN ( " +
	        "                SELECT DISTINCT projectcode FROM LSprojectmaster " +
	        "                WHERE lsusersteam_teamcode IN ( " +
	        "                    SELECT teamcode FROM LSuserteammapping " +
	        "                    WHERE lsuserMaster_usercode = ?5 AND teamcode IS NOT NULL" +
	        "                ) AND status = 1 " +
	        "            )" +
	        "        ) AND createdtimestamp BETWEEN ?3 AND ?4 " +
	        "        AND approvelstatus != ?13 AND ordercancell IS NULL AND assignedto_usercode IS NULL) " +
	        "    ) " +
	        "    OR (o.orderflag = ?14 AND lsprojectmaster_projectcode IS NULL AND elnmaterial_nmaterialcode IN (" +
	        "            SELECT DISTINCT elnmaterial_nmaterialcode FROM lslogilablimsorderdetail WHERE elnmaterial_nmaterialcode IN (" +
	        "                SELECT m.nmaterialcode FROM elnmaterial m WHERE m.nsitecode = ?10 " +
	        "            ) " +
	        "            AND createdtimestamp BETWEEN ?3 AND ?4 " +
	        "            AND approvelstatus IS NULL AND ordercancell IS NULL AND assignedto_usercode IS NULL " +
	        "            AND lsusermaster_usercode != ?5 " +
	        "        ) " +
	        "    ) " +
	        "    OR (o.orderflag = ?14 AND lsprojectmaster_projectcode IS NULL AND elnmaterial_nmaterialcode IN (" +
	        "            SELECT DISTINCT elnmaterial_nmaterialcode FROM lslogilablimsorderdetail WHERE elnmaterial_nmaterialcode IN (" +
	        "                SELECT m.nmaterialcode FROM elnmaterial m WHERE m.nsitecode = ?10 " +
	        "            ) " +
	        "            AND createdtimestamp BETWEEN ?3 AND ?4 " +
	        "            AND approvelstatus != ?13 AND ordercancell IS NULL AND assignedto_usercode IS NULL " +
	        "            AND lsusermaster_usercode != ?5 " +
	        "        ) " +
	        "    ) " +
	        ") " +
	        "  OR(" + " (lsprojectmaster_projectcode IS NULL AND "
			+ "        ((viewoption = ?6 OR viewoption = ?7 OR viewoption = ?8) AND lsusermaster_usercode = ?5 "
			+ "         AND createdtimestamp BETWEEN ?3 AND ?4 AND approvelstatus =?13 AND assignedto_usercode IS NULL )"
			+ "    ) OR (lsusermaster_usercode = ?5 AND  assignedto_usercode != ?5 AND createdtimestamp BETWEEN ?3 AND ?4 AND assignedto_usercode IS NULL AND approvelstatus =?13)"
			+ "OR(assignedto_usercode = ?5 AND createdtimestamp BETWEEN ?3 AND ?4 AND approvelstatus =?13)"
			+ "OR(approvelstatus =?13 AND o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM TeamProjects ) AND createdtimestamp BETWEEN ?3 AND ?4 AND assignedto_usercode IS NULL)"
			+ ")" +

			" OR (" + " (lsprojectmaster_projectcode IS NULL AND "
			+ "        ((viewoption = ?6 OR viewoption = ?7 OR viewoption = ?8) AND lsusermaster_usercode = ?5 "
			+ "         AND createdtimestamp BETWEEN ?3 AND ?4 AND ordercancell =?6 AND assignedto_usercode IS NULL )"
			+ ") OR (ordercancell =?6 AND o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM TeamProjects ) AND createdtimestamp BETWEEN ?3 AND ?4 AND assignedto_usercode IS NULL)"
			+"OR(lsusermaster_usercode = ?5 AND assignedto_usercode = ?5 AND createdtimestamp BETWEEN ?3 AND ?4 AND assignedto_usercode IS NULL AND ordercancell =?6)"
			+"OR(assignedto_usercode = ?5 AND createdtimestamp BETWEEN ?3 AND ?4 AND ordercancell =?6 ))" +
	        "ORDER BY batchcode DESC " +
	        "OFFSET ?11 ROWS FETCH NEXT ?12 ROWS ONLY", nativeQuery = true)
	List<LSlogilablimsorderdetail> getLSlogilablimsorderdetaildashboardforallorders(
	        String orderFlag,
	        int fileType,
	        Date fromDate,
	        Date toDate,
	        LSuserMaster user,
	        int viewOption1,
	        int viewOption2,
	        int viewOption3,
	        List<LSuserMaster> userNotify,
	        LSSiteMaster siteMaster,
	        int offset,
	        Integer pageSize,
	        Integer approvalStatus,
	        String completedOrderFlag);

	List<LogilabOrderDetails> findByFiletypeAndLsfileInAndApprovelstatusNotOrFiletypeAndLsfileInAndApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
			int filetype, List<LSfile> lSfiles, int i, int filetype2, List<LSfile> lSfiles2);

	List<LogilabOrderDetails> findByLsprojectmasterInAndFiletypeAndLsfileInAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterInAndFiletypeAndAssignedtoIsNullAndLsfileInAndApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
			List<LSprojectmaster> projects, int filetype, List<LSfile> lSfiles, int i, List<LSprojectmaster> projects2,
			int filetype2, List<LSfile> lSfiles2);

	List<LogilabOrderDetails> findByLsprojectmasterIsNullAndLssamplemasterIsNullAndFiletypeAndAssignedtoIsNullAndLsfileInOrderByBatchcodeDesc(
			int filetype, List<LSfile> lSfiles);

	List<LogilabOrderDetails> findByLsprojectmasterIsNullAndElnmaterialInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsfileInOrderByBatchcodeDesc(
			List<Elnmaterial> materials, int filetype, int i, List<LSfile> lSfiles);

	List<LogilabOrderDetails> findByFiletypeAndApprovelstatusNotAndOrdercancellIsNullOrFiletypeAndApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
			Integer filetype, int i, Integer filetype2);

	List<LogilabOrderDetails> findByFiletypeAndLsprojectmasterInAndApprovelstatusNotAndOrdercancellIsNullOrFiletypeAndLsprojectmasterInAndApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
			Integer filetype, List<LSprojectmaster> lstproject, int i, Integer filetype2,
			List<LSprojectmaster> lstproject2);

	List<LogilabOrderDetails> findByLsprojectmasterIsNullAndElnmaterialInAndFiletypeAndAssignedtoIsNullAndViewoption(
			List<Elnmaterial> nmaterialcode, Integer filetype, int i);
	
}
