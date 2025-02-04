package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.fetchmodel.getorders.LogilabOrderDetails;
import com.agaram.eln.primary.fetchmodel.getorders.LogilabOrdermastersh;
import com.agaram.eln.primary.fetchmodel.getorders.Logilaborders;
import com.agaram.eln.primary.fetchmodel.getorders.Logilaborderssh;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
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
	        "WHERE"+
	        "(o.orderflag = ?1 AND createdtimestamp BETWEEN ?3 AND ?4 " +
	        "AND ( " +
	        "   (filetype = ?2 AND (o.Approvelstatus != ?13 OR o.Approvelstatus IS NULL)) " +
	        
	        "   OR "+
	        "   (lsprojectmaster_projectcode IS NULL AND ordercancell IS NULL AND assignedto_usercode IS NULL AND createdtimestamp BETWEEN ?3 AND ?4 " +
			 "       AND (o.Approvelstatus != ?13 OR o.Approvelstatus IS NULL) " +
			 "       AND (viewoption = ?8 AND teamselected = true AND batchcode IN ?15)"+ 
			 "   ) " +
			 
			 "   OR "+
	        "   (lsprojectmaster_projectcode IS NULL AND ordercancell IS NULL AND assignedto_usercode IS NULL AND createdtimestamp BETWEEN ?3 AND ?4" +
	        "       AND (o.Approvelstatus != ?13 OR o.Approvelstatus IS NULL) " +
	        "       AND ((viewoption = ?6 AND lsusermaster_usercode = ?5) OR " +
	        "             (viewoption = ?7 AND lsusermaster_usercode = ?5) OR " +
	        "             (viewoption = ?8 AND lsusermaster_usercode IN (?9) AND teamselected = false)"+
	        "           ) " +
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
	        "       AND (o.Approvelstatus != ?13 OR o.Approvelstatus IS NULL)"+
	        "		AND (viewoption != ?6 AND viewoption != ?7 AND viewoption != ?8)"+
	        "	) " +
	        ")) " +
	      "OR "+
	        "( " +
	        "    (o.orderflag = ?14 AND filetype = ?2 AND createdtimestamp BETWEEN ?3 AND ?4 AND assignedto_usercode IS NULL) " +
	        "    OR (o.orderflag = ?14 AND lsprojectmaster_projectcode IS NULL AND " +
	        "        ("+
			"		((viewoption = ?6 AND lsusermaster_usercode = ?5) OR " +
            "             (viewoption = ?7 AND lsusermaster_usercode = ?5) OR " +
            "             (viewoption = ?8 AND lsusermaster_usercode IN (?9) AND teamselected = false)"+
            "       ) " +
	        "         AND createdtimestamp BETWEEN ?3 AND ?4 AND " +
	        "         ((approvelstatus != ?13 AND ordercancell IS NULL AND assignedto_usercode IS NULL) " +
	        "         OR (approvelstatus IS NULL AND ordercancell IS NULL AND assignedto_usercode IS NULL))"+
	        "		) " +
	        "    ) " +
	        
			  "    OR (o.orderflag = ?14 AND lsprojectmaster_projectcode IS NULL AND " +
			  "        ("+
			  "         (viewoption = ?8 AND batchcode IN ?15 AND teamselected = true)"+
			  "         AND createdtimestamp BETWEEN ?3 AND ?4 AND " +
			  "         ((approvelstatus != ?13 AND ordercancell IS NULL AND assignedto_usercode IS NULL) " +
			  "         OR (approvelstatus IS NULL AND ordercancell IS NULL AND assignedto_usercode IS NULL))"+
			  "		) " +
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
	        "			 AND viewoption != ?6 AND viewoption != ?7 AND viewoption != ?8"+
	        "        ) " +
	        "    ) " +
	        "    OR (o.orderflag = ?14 AND lsprojectmaster_projectcode IS NULL AND elnmaterial_nmaterialcode IN (" +
	        "            SELECT DISTINCT elnmaterial_nmaterialcode FROM lslogilablimsorderdetail WHERE elnmaterial_nmaterialcode IN (" +
	        "                SELECT m.nmaterialcode FROM elnmaterial m WHERE m.nsitecode = ?10 " +
	        "            ) " +
	        "            AND createdtimestamp BETWEEN ?3 AND ?4 " +
	        "            AND approvelstatus != ?13 AND ordercancell IS NULL AND assignedto_usercode IS NULL " +
	        "            AND lsusermaster_usercode != ?5 " +
	        "			 AND viewoption != ?6 AND viewoption != ?7 AND viewoption != ?8"+
	        "        ) " +
	        "    ) " +
	        ") " +
	        "  OR(" + 
	        
		  " (lsprojectmaster_projectcode IS NULL AND "
			+ "("+
				"(viewoption = ?8 AND batchcode IN ?15 AND lsusermaster_usercode IN (?9) AND teamselected = true)"
				+ "AND createdtimestamp BETWEEN ?3 AND ?4 AND approvelstatus =?13 AND assignedto_usercode IS NULL"+
			"	)"
			+ ")"
	
	        + "OR (lsprojectmaster_projectcode IS NULL AND "
			+ "        ("+
			"((viewoption = ?6 AND lsusermaster_usercode = ?5) OR " +
			"             (viewoption = ?7 AND lsusermaster_usercode = ?5) OR " +
			"             (viewoption = ?8 AND lsusermaster_usercode IN (?9) AND teamselected = false)"+
			"       ) " 
			+ "   AND createdtimestamp BETWEEN ?3 AND ?4 AND approvelstatus =?13 AND assignedto_usercode IS NULL"+
			"		)"
			+ ")"
			+" OR (lsusermaster_usercode = ?5 AND  assignedto_usercode != ?5 AND createdtimestamp BETWEEN ?3 AND ?4 AND assignedto_usercode IS NULL AND approvelstatus =?13)"
			+ "OR(assignedto_usercode = ?5 AND createdtimestamp BETWEEN ?3 AND ?4 AND approvelstatus =?13)"
			+ "OR(approvelstatus =?13 AND o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM TeamProjects ) AND createdtimestamp BETWEEN ?3 AND ?4 AND assignedto_usercode IS NULL)"
			+ ")" +

			" OR (" 
				+ " (lsprojectmaster_projectcode IS NULL AND "
				+ " ("+
				"(viewoption = ?8 AND batchcode IN ?15 AND teamselected = true)"
				+ " AND createdtimestamp BETWEEN ?3 AND ?4 AND ordercancell =?6 AND assignedto_usercode IS NULL "+
				")"
				+ ")"
				+"OR(lsprojectmaster_projectcode IS NULL AND "
				+ "        ("+
				"((viewoption = ?6 AND lsusermaster_usercode = ?5 AND createdtimestamp BETWEEN ?3 AND ?4 AND ordercancell =?6 AND assignedto_usercode IS NULL) OR " +
				"             (viewoption = ?7 AND lsusermaster_usercode = ?5 AND createdtimestamp BETWEEN ?3 AND ?4 AND ordercancell =?6 AND assignedto_usercode IS NULL) OR " +
				"             (viewoption = ?8 AND lsusermaster_usercode IN (?9) AND teamselected = false AND createdtimestamp BETWEEN ?3 AND ?4 AND ordercancell =?6 AND assignedto_usercode IS NULL)"+
				"       ) " 
				+ ")"
				+ ")"
			+"OR (ordercancell =?6 AND o.lsprojectmaster_projectcode IN (SELECT lsprojectmaster_projectcode FROM TeamProjects ) AND createdtimestamp BETWEEN ?3 AND ?4 AND assignedto_usercode IS NULL)"
			+"OR(lsusermaster_usercode = ?5 AND assignedto_usercode = ?5 AND createdtimestamp BETWEEN ?3 AND ?4 AND assignedto_usercode IS NOT NULL AND ordercancell =?6)"
			+"OR(assignedto_usercode = ?5 AND createdtimestamp BETWEEN ?3 AND ?4 AND ordercancell =?6 )"+
			")" +
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
	        String completedOrderFlag,
	        List<Long> selectedbatchcode);

//	@Transactional
//	@Modifying
//	@Query(value = "WITH TeamProjects AS ( " +
//	        "   SELECT DISTINCT lsprojectmaster_projectcode " +
//	        "   FROM LSlogilablimsorderdetail " +
//	        "   WHERE lsprojectmaster_projectcode IN ( " +
//	        "       SELECT projectcode " +
//	        "       FROM LSprojectmaster " +
//	        "       WHERE lsusersteam_teamcode IN ( " +
//	        "           SELECT DISTINCT teamcode " +
//	        "           FROM LSuserteammapping " +
//	        "           WHERE lsuserMaster_usercode = ?5 AND teamcode IS NOT NULL) " +
//	        "       AND status = 1 " +
//	        ") " +
//	        ") " +
//	        "SELECT * FROM LSlogilablimsorderdetail o " +
//	        "WHERE"+
//	        "( " +
//	        "    (o.orderflag = ?14 AND o.orderflag != ?1 AND filetype = ?2 AND createdtimestamp BETWEEN ?3 AND ?4 AND assignedto_usercode IS NULL) " +
//	        "    OR (o.orderflag = ?14 AND lsprojectmaster_projectcode IS NULL AND " +
//	        "        ("+
//			"		((viewoption = ?6 AND lsusermaster_usercode = ?5) OR " +
//            "             (viewoption = ?7 AND lsusermaster_usercode = ?5) OR " +
//            "             (viewoption = ?8 AND lsusermaster_usercode IN (?9) AND teamselected = false)"+
//            "       ) " +
//	        "         AND createdtimestamp BETWEEN ?3 AND ?4 AND " +
//	        "         ((approvelstatus != ?13 AND ordercancell IS NULL AND assignedto_usercode IS NULL) " +
//	        "         OR (approvelstatus IS NULL AND ordercancell IS NULL AND assignedto_usercode IS NULL))"+
//	        "		) " +
//	        "    ) " +
//	        
//			  "    OR (o.orderflag = ?14 AND lsprojectmaster_projectcode IS NULL AND " +
//			  "        ("+
//			  "         (viewoption = ?8 AND batchcode IN ?15 AND teamselected = true)"+
//			  "         AND createdtimestamp BETWEEN ?3 AND ?4 AND " +
//			  "         ((approvelstatus != ?13 AND ordercancell IS NULL AND assignedto_usercode IS NULL) " +
//			  "         OR (approvelstatus IS NULL AND ordercancell IS NULL AND assignedto_usercode IS NULL))"+
//			  "		) " +
//			  "    ) " +
//  
//  
//	        "    OR (o.orderflag = ?14 AND " +
//	        "        ((lsusermaster_usercode = ?5 AND assignedto_usercode != ?5 AND createdtimestamp BETWEEN ?3 AND ?4 AND assignedto_usercode IS NOT NULL) " +
//	        "         OR (assignedto_usercode = ?5 AND createdtimestamp BETWEEN ?3 AND ?4)) " +
//	        "    ) " +
//	        "    OR (o.orderflag = ?14 AND " +
//	        "        (o.lsprojectmaster_projectcode IN ( " +
//	        "            SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail " +
//	        "            WHERE lsprojectmaster_projectcode IN ( " +
//	        "                SELECT DISTINCT projectcode FROM LSprojectmaster " +
//	        "                WHERE lsusersteam_teamcode IN ( " +
//	        "                    SELECT teamcode FROM LSuserteammapping " +
//	        "                    WHERE lsuserMaster_usercode = ?5 AND teamcode IS NOT NULL" +
//	        "                ) AND status = 1 " +
//	        "            )" +
//	        "        ) AND createdtimestamp BETWEEN ?3 AND ?4 " +
//	        "        AND approvelstatus != ?13 AND ordercancell IS NULL AND assignedto_usercode IS NULL) " +
//	        "    ) " +
//	        "    OR (o.orderflag = ?14 AND lsprojectmaster_projectcode IS NULL AND elnmaterial_nmaterialcode IN (" +
//	        "            SELECT DISTINCT elnmaterial_nmaterialcode FROM lslogilablimsorderdetail WHERE elnmaterial_nmaterialcode IN (" +
//	        "                SELECT m.nmaterialcode FROM elnmaterial m WHERE m.nsitecode = ?10 " +
//	        "            ) " +
//	        "            AND createdtimestamp BETWEEN ?3 AND ?4 " +
//	        "            AND approvelstatus IS NULL AND ordercancell IS NULL AND assignedto_usercode IS NULL " +
//	        "            AND lsusermaster_usercode != ?5 " +
//	        "			 AND viewoption != ?6 AND viewoption != ?7 AND viewoption != ?8"+
//	        "        ) " +
//	        "    ) " +
//	        "    OR (o.orderflag = ?14 AND lsprojectmaster_projectcode IS NULL AND elnmaterial_nmaterialcode IN (" +
//	        "            SELECT DISTINCT elnmaterial_nmaterialcode FROM lslogilablimsorderdetail WHERE elnmaterial_nmaterialcode IN (" +
//	        "                SELECT m.nmaterialcode FROM elnmaterial m WHERE m.nsitecode = ?10 " +
//	        "            ) " +
//	        "            AND createdtimestamp BETWEEN ?3 AND ?4 " +
//	        "            AND approvelstatus != ?13 AND ordercancell IS NULL AND assignedto_usercode IS NULL " +
//	        "            AND lsusermaster_usercode != ?5 " +
//	        "			AND viewoption != ?6 AND viewoption != ?7 AND viewoption != ?8"+
//	        "        ) " +
//	        "    ) " +
//	        ") " +
//	        "ORDER BY batchcode DESC " +
//	        "OFFSET ?11 ROWS FETCH NEXT ?12 ROWS ONLY", nativeQuery = true)
//	List<LSlogilablimsorderdetail> getLSlogilablimsorderdetaildashboardforallorders(
//	        String orderFlag,
//	        int fileType,
//	        Date fromDate,
//	        Date toDate,
//	        LSuserMaster user,
//	        int viewOption1,
//	        int viewOption2,
//	        int viewOption3,
//	        List<LSuserMaster> userNotify,
//	        LSSiteMaster siteMaster,
//	        int offset,
//	        Integer pageSize,
//	        Integer approvalStatus,
//	        String completedOrderFlag,
//	        List<Long> selectedbatchcode);
	
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
	
	long countByLsuserMasterIn(List<LSuserMaster> lstuser);

	long countByLsprojectmaster(LSprojectmaster objproject);
	long countByLstestmasterlocal(LStestmasterlocal objtest);
	long countByFiletype(Integer filetype);

	List<Logilaborders> findByBatchcodeIn(List<Long> selectedteambatchCodeList);

	List<Logilaborders> findByDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndBatchcodeInOrderByBatchcodeDesc(
			Long directorycode, int i, LSuserMaster createdby, Date fromdate, Date todate, Long directorycode2, int j,
			Date fromdate2, Date todate2, List<LSuserMaster> lstuserMaster, List<Long> selectedteambatchCodeList);

	List<Logilaborders> findByDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsselectedTeamIsNullOrDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsselectedTeamIsNotNullAndBatchcodeInOrderByBatchcodeDesc(
			Long directorycode, int i, LSuserMaster createdby, Date fromdate, Date todate, Long directorycode2, int j,
			Date fromdate2, Date todate2, List<LSuserMaster> lstuserMaster, Long directorycode3, int k, Date fromdate3,
			Date todate3, List<LSuserMaster> lstuserMaster2, List<Long> selectedteambatchCodeList);

	List<LSlogilablimsorderdetail> findByDirectorycodeAndViewoption(Long directorycode, int i);

	List<Logilaborders> findByDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsselectedTeamIsNotNullAndBatchcodeInOrderByBatchcodeDesc(
			Long directorycode, int i, LSuserMaster createdby, Date fromdate, Date todate, Long directorycode2, int j,
			Date fromdate2, Date todate2, List<LSuserMaster> lstuserMaster, List<Long> selectedteambatchCodeList);

	Collection<? extends Logilaborders> findByDirectorycodeAndViewoptionAndLsuserMasterAndFiletypeAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndFiletypeAndCreatedtimestampBetweenAndLsselectedTeamIsNullOrDirectorycodeAndViewoptionAndLsuserMasterAndFiletypeAndCreatedtimestampBetweenAndLsselectedTeamIsNotNullAndBatchcodeInOrderByBatchcodeDesc(
			Long directorycode, int i, LSuserMaster createdby, Integer filetype, Date fromdate, Date todate,
			Long directorycode2, int j, LSuserMaster createdby2, Integer filetype2, Date fromdate2, Date todate2,
			Long directorycode3, int k, LSuserMaster createdby3, Integer filetype3, Date fromdate3, Date todate3,
			List<Long> selectedteambatchCodeList);

	Collection<? extends Logilaborders> findByDirectorycodeAndViewoptionAndLsuserMasterAndFiletypeAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndFiletypeAndCreatedtimestampBetweenAndLsuserMasterInAndLsselectedTeamIsNullOrDirectorycodeAndViewoptionAndFiletypeAndCreatedtimestampBetweenAndLsuserMasterInAndLsselectedTeamIsNotNullAndBatchcodeInOrderByBatchcodeDesc(
			Long directorycode, int i, LSuserMaster createdby, Integer filetype, Date fromdate, Date todate,
			Long directorycode2, int j, Integer filetype2, Date fromdate2, Date todate2,
			List<LSuserMaster> lstuserMaster, Long directorycode3, int k, Integer filetype3, Date fromdate3,
			Date todate3, List<LSuserMaster> lstuserMaster2, List<Long> selectedteambatchCodeList);

	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullAndLsselectedTeamIsNullOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullAndLsselectedTeamIsNotNullAndBatchcodeInAndDirectorycodeInOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, List<LSuserMaster> lstuserMaster,
			String orderflag, Integer filetype, List<Long> directory_Code2, int j, Date fromdate2, Date todate2,
			List<LSuserMaster> lstuserMaster2, String orderflag2, Integer filetype2,
			List<Long> selectedteambatchCodeList, List<Long> selectedteamdirctoryList);

	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullAndLsselectedTeamIsNullOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, List<LSuserMaster> lstuserMaster,
			String orderflag, Integer filetype);


	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullAndLsselectedTeamIsNotNullAndBatchcodeInOrderByBatchcodeDesc(
			List<Long> selectedteamdirctoryList, int i, Date fromdate, Date todate, List<LSuserMaster> lstuserMaster,
			String orderflag, Integer filetype, List<Long> selectedteambatchCodeList);

	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullAndLsselectedTeamIsNullOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer filetype,
			List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2, Date todate2,
			String orderflag2, Integer filetype2, List<Long> directory_Code3, int k, Date fromdate3, Date todate3,
			List<LSuserMaster> lstuserMaster, String orderflag3, Integer filetype3);


	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeAndLsselectedTeamIsNullOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer filetype,
			Integer testcode, List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2,
			Date todate2, String orderflag2, Integer filetype2, Integer testcode2, List<Long> directory_Code3, int k,
			LSuserMaster lsuserMaster2, Date fromdate3, Date todate3, String orderflag3, Integer filetype3,
			Integer testcode3);

	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeAndLsselectedTeamIsNullOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer filetype,
			Integer testcode, List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2,
			Date todate2, String orderflag2, Integer filetype2, Integer testcode2, List<Long> directory_Code3, int k,
			Date fromdate3, Date todate3, List<LSuserMaster> lstuserMaster, String orderflag3, Integer filetype3,
			Integer testcode3);

	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullAndLsselectedTeamIsNullOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer filetype,
			Integer approvelstatus, List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2,
			Date todate2, String orderflag2, Integer filetype2, Integer approvelstatus2, List<Long> directory_Code3,
			int k, LSuserMaster lsuserMaster2, Date fromdate3, Date todate3, String orderflag3, Integer filetype3,
			Integer approvelstatus3);

	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndLsselectedTeamIsNullOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer filetype,
			List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2, Date todate2,
			String orderflag2, Integer filetype2, List<Long> directory_Code3, int k, LSuserMaster lsuserMaster2,
			Date fromdate3, Date todate3, String orderflag3, Integer filetype3);

	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullAndLsselectedTeamIsNullOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer filetype,
			Integer approvelstatus, List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2,
			Date todate2, String orderflag2, Integer filetype2, Integer approvelstatus2, List<Long> directory_Code3,
			int k, Date fromdate3, Date todate3, List<LSuserMaster> lstuserMaster, String orderflag3, Integer filetype3,
			Integer approvelstatus3);

	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullAndBatchcodeNotInOrBatchcodeInAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, List<LSuserMaster> lstuserMaster,
			String orderflag, Integer filetype, List<Long> selectedteambatchCodeList,
			List<Long> selectedteambatchCodeList2, List<Long> directory_Code2, int j, Date fromdate2, Date todate2,
			List<LSuserMaster> lstuserMaster2, String orderflag2, Integer filetype2);

	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullAndTeamselectedOrTeamselectedAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer filetype,
			List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2, Date todate2,
			String orderflag2, Integer filetype2, List<Long> directory_Code3, int k, Date fromdate3, Date todate3,
			List<LSuserMaster> lstuserMaster, String orderflag3, Integer filetype3, boolean b, boolean c,
			List<Long> directory_Code4, int l, Date fromdate4, Date todate4, List<LSuserMaster> distinctLsuserMasters,
			String orderflag4, Integer filetype4);

	List<LogilabOrdermastersh> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTeamselectedOrLsprojectmasterIsNullAndDirectorycodeInOrViewoptionAndTeamselectedAndCreatedtimestampBetweenAndBatchcodeInOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, boolean b, List<Long> directorycode, int j,
			boolean c, Date fromdate2, Date todate2, List<Long> selectedteambatchCodeList, LSuserMaster objuser2,
			List<Long> directorycode2, int k, Date fromdate3, Date todate3, List<Long> directorycode3, int l,
			Date fromdate4, Date todate4, LSuserMaster objuser3, List<Long> directorycode4, int m, Date fromdate5,
			Date todate5, LSuserMaster objuser4, Pageable pageable);

	List<LogilabOrdermastersh> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedOrViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTeamselectedAndOrderflagAndOrdercancellIsNullOrViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, String string, List<Long> directorycode, boolean b,
			int j, boolean c, List<Long> selectedteambatchCodeList, Date fromdate2, Date todate2, LSuserMaster objuser2,
			String string2, List<Long> directorycode2, int k, Date fromdate3, Date todate3, String string3,
			List<Long> directorycode3, int l, Date fromdate4, Date todate4, LSuserMaster objuser3, String string4,
			List<Long> directorycode4, int m, Date fromdate5, Date todate5, LSuserMaster objuser4, boolean d,
			String string5, int n, boolean e, List<Long> selectedteambatchCodeList2, Date fromdate6, Date todate6,
			LSuserMaster objuser5, String string6, Pageable pageable);

	List<LogilabOrdermastersh> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedOrViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, String string, List<Long> directorycode, boolean b,
			int j, boolean c, List<Long> selectedteambatchCodeList, Date fromdate2, Date todate2, LSuserMaster objuser2,
			String string2, List<Long> directorycode2, Pageable pageable);

	List<LogilabOrdermastersh> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, String string, List<Long> directorycode,
			boolean b);

	List<LogilabOrdermastersh> findByViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInOrderByBatchcodeDesc(
			int i, boolean b, List<Long> selectedteambatchCodeList, Date fromdate, Date todate, LSuserMaster objuser,
			String string, List<Long> directorycode);

	List<LogilabOrdermastersh> findByViewoptionAndTeamselectedAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInOrderByBatchcodeDesc(
			int i, boolean b, Date fromdate, Date todate, LSuserMaster objuser, String string, List<Long> directorycode,
			Pageable pageable);

	
	
	
	
	
	
	
	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndOrdercancellIsNullAndTeamselectedOrTeamselectedAndBatchcodeInAndDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, List<Long> directory_Code2,
			int j, LSuserMaster lsuserMaster, Date fromdate2, Date todate2, String orderflag2,
			List<Long> directory_Code3, int k, LSuserMaster lsuserMaster2, Date fromdate3, Date todate3,
			String orderflag3, boolean b, boolean c, List<Long> selectedteambatchCodeList, List<Long> directory_Code4,
			int l, LSuserMaster lsuserMaster3, Date fromdate4, Date todate4, String orderflag4);

	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndOrdercancellIsNullAndTeamselectedOrTeamselectedAndBatchcodeInAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, List<Long> directory_Code2,
			int j, LSuserMaster lsuserMaster, Date fromdate2, Date todate2, String orderflag2,
			List<Long> directory_Code3, int k, Date fromdate3, Date todate3, List<LSuserMaster> lstuserMaster,
			String orderflag3, boolean b, boolean c, List<Long> selectedteambatchCodeList, List<Long> directory_Code4,
			int l, Date fromdate4, Date todate4, List<LSuserMaster> lstuserMaster2, String orderflag4);

	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullAndTeamselectedOrTeamselectedAndBatchcodeInAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer filetype,
			Integer approvelstatus, List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2,
			Date todate2, String orderflag2, Integer filetype2, Integer approvelstatus2, List<Long> directory_Code3,
			int k, Date fromdate3, Date todate3, List<LSuserMaster> lstuserMaster, String orderflag3, Integer filetype3,
			Integer approvelstatus3, boolean b, boolean c, List<Long> selectedteambatchCodeList,
			List<Long> directory_Code4, int l, Date fromdate4, Date todate4, List<LSuserMaster> lstuserMaster2,
			String orderflag4, Integer filetype4, Integer approvelstatus4);

	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullAndTeamselectedOrTeamselectedAndBatchcodeInAndDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer filetype,
			Integer approvelstatus, List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2,
			Date todate2, String orderflag2, Integer filetype2, Integer approvelstatus2, List<Long> directory_Code3,
			int k, LSuserMaster lsuserMaster2, Date fromdate3, Date todate3, String orderflag3, Integer filetype3,
			Integer approvelstatus3, boolean b, boolean c, List<Long> selectedteambatchCodeList,
			List<Long> directory_Code4, int l, LSuserMaster lsuserMaster3, Date fromdate4, Date todate4,
			String orderflag4, Integer filetype4, Integer approvelstatus4);

	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullAndTeamselectedOrTeamselectedAndBatchcodeInAndDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer filetype,
			Integer approvelstatus, List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2,
			Date todate2, String orderflag2, Integer filetype2, Integer approvelstatus2, List<Long> directory_Code3,
			int k, Date fromdate3, Date todate3, List<LSuserMaster> lstuserMaster, String orderflag3, Integer filetype3,
			Integer approvelstatus3, boolean b, boolean c, List<Long> selectedteambatchCodeList,
			List<Long> directory_Code4, int l, Date fromdate4, Date todate4, List<LSuserMaster> lstuserMaster2,
			String orderflag4, Integer filetype4, Integer approvelstatus4);

	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeAndTeamselectedOrTeamselectedAndBatchcodeInAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer filetype,
			Integer testcode, List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2,
			Date todate2, String orderflag2, Integer filetype2, Integer testcode2, List<Long> directory_Code3, int k,
			Date fromdate3, Date todate3, List<LSuserMaster> lstuserMaster, String orderflag3, Integer filetype3,
			Integer testcode3, boolean b, boolean c, List<Long> selectedteambatchCodeList, List<Long> directory_Code4,
			int l, Date fromdate4, Date todate4, List<LSuserMaster> lstuserMaster2, String orderflag4,
			Integer filetype4, Integer testcode4);

	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeAndTeamselectedOrTeamselectedAndBatchcodeInAndDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer filetype,
			Integer testcode, List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2,
			Date todate2, String orderflag2, Integer filetype2, Integer testcode2, List<Long> directory_Code3, int k,
			LSuserMaster lsuserMaster2, Date fromdate3, Date todate3, String orderflag3, Integer filetype3,
			Integer testcode3, boolean b, boolean c, List<Long> selectedteambatchCodeList, List<Long> directory_Code4,
			int l, LSuserMaster lsuserMaster3, Date fromdate4, Date todate4, String orderflag4, Integer filetype4,
			Integer testcode4);

	Collection<? extends Logilaborderssh> findByOrderflagAndLsprojectmasterIsNullAndDirectorycodeIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterInAndOrdercancellIsNullAndTeamselectedOrTeamselectedAndBatchcodeInAndOrderflagAndLsprojectmasterIsNullAndDirectorycodeIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterInAndOrdercancellIsNullOrderByBatchcodeDesc(
			String orderflag, List<Elnmaterial> currentChunk, Integer filetype, Date fromdate, Date todate, int i,
			List<LSuserMaster> lstuserMaster, boolean b, boolean c, List<Long> selectedteambatchCodeList,
			String orderflag2, List<Elnmaterial> currentChunk2, Integer filetype2, Date fromdate2, Date todate2, int j,
			List<LSuserMaster> lstuserMaster2);

	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndTeamselectedOrTeamselectedAndBatchcodeInAndDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer filetype,
			List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2, Date todate2,
			String orderflag2, Integer filetype2, List<Long> directory_Code3, int k, LSuserMaster lsuserMaster2,
			Date fromdate3, Date todate3, String orderflag3, Integer filetype3, boolean b, boolean c,
			List<Long> selectedteambatchCodeList, List<Long> directory_Code4, int l, LSuserMaster lsuserMaster3,
			Date fromdate4, Date todate4, String orderflag4, Integer filetype4);

	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullAndTeamselectedOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer filetype,
			List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2, Date todate2,
			String orderflag2, Integer filetype2, List<Long> directory_Code3, int k, Date fromdate3, Date todate3,
			List<LSuserMaster> lstuserMaster, String orderflag3, Integer filetype3, boolean b);
	
	@Transactional
	@Modifying
	@Query(value = 
    "SELECT * FROM LSlogilablimsorderdetail o " +
    "WHERE " +
    "    (" +
    "        o.orderflag = ?1 " +
    "        AND filetype = ?2 " +
    "        AND createdtimestamp BETWEEN ?3 AND ?4 " +
    "        AND assignedto_usercode IS NULL " +
    "    ) " +
    "    OR (" +
    "        o.orderflag = ?1 " +
    "        AND lsprojectmaster_projectcode IS NULL " +
    "        AND ((" +
    "            (viewoption = ?7 OR viewoption = ?8 OR (viewoption = ?9 AND teamselected=false)) " +
    "            AND lsusermaster_usercode = ?5 " +
    "            AND createdtimestamp BETWEEN ?3 AND ?4 " +
    "            AND (" +
    "                (approvelstatus != ?6 AND ordercancell IS NULL AND assignedto_usercode IS NULL) " +
    "                OR (approvelstatus IS NULL AND ordercancell IS NULL AND assignedto_usercode IS NULL) " +
    "            ) " +
    "        ) " +
    "		OR (" +
    	    " (viewoption = ?9 AND teamselected=true AND batchcode IN ?12) " +
    	  
    	    " AND createdtimestamp BETWEEN ?3 AND ?4 " +
    	    " AND (" +
    	    " (approvelstatus != ?6 AND ordercancell IS NULL AND assignedto_usercode IS NULL) " +
    	    " OR (approvelstatus IS NULL AND ordercancell IS NULL AND assignedto_usercode IS NULL) " +
    	    " ) " +
    	    ")) " +
    "    ) " +
    "ORDER BY batchcode DESC " +
    "OFFSET ?10 ROWS FETCH NEXT ?11 ROWS ONLY", nativeQuery = true
)

	List<LSlogilablimsorderdetail> getLSlogilablimsorderdetaildashboardforcompleted(String string, int i, Date fromdate,
			Date todate,LSuserMaster objuser , int j, int k, int l, int m,int n,
			Integer pageperorder,List<Long> batchcode);
	
	@Transactional
	@Modifying
	@Query(value = 
    "SELECT * FROM LSlogilablimsorderdetail o " +
    "WHERE " +
    "    (" +
    "        o.orderflag = ?1 " +
    "        AND filetype = ?2 " +
    "        AND createdtimestamp BETWEEN ?3 AND ?4 " +
    "        AND assignedto_usercode IS NULL " +
    "    ) " +
    "    OR (" +
    "        o.orderflag = ?1 " +
    "        AND lsprojectmaster_projectcode IS NULL " +
    "        AND ((" +
    "            (viewoption = ?7 OR viewoption = ?8 OR (viewoption = ?9 AND teamselected=false)) " +
    "            AND lsusermaster_usercode = ?5 " +
    "            AND createdtimestamp BETWEEN ?3 AND ?4 " +
    "            AND (" +
    "                (approvelstatus != ?6 AND ordercancell IS NULL AND assignedto_usercode IS NULL) " +
    "                OR (approvelstatus IS NULL AND ordercancell IS NULL AND assignedto_usercode IS NULL) " +
    "            ) " +
    "        ) " +
    "		OR (" +
    " 			(viewoption = ?9 AND teamselected=true AND batchcode IN ?12) " +
    " 			AND createdtimestamp BETWEEN ?3 AND ?4 " +
    " 			AND (" +
    " 			(approvelstatus != ?6 AND ordercancell IS NULL AND assignedto_usercode IS NULL) " +
    " 			OR (approvelstatus IS NULL AND ordercancell IS NULL AND assignedto_usercode IS NULL) " +
    " 			) " +
    "		)) " +
    "    ) " +
    "    OR (" +
    "        o.orderflag = ?1 " +
    "        AND (" +
    "            (lsusermaster_usercode = ?5 AND assignedto_usercode != ?5 AND createdtimestamp BETWEEN ?3 AND ?4 AND assignedto_usercode IS NOT NULL) " +
    "            OR (assignedto_usercode = ?5 AND createdtimestamp BETWEEN ?3 AND ?4) " +
    "        ) " +
    "    ) " +
    "    OR (" +
    "        o.orderflag = ?1 " +
    "        AND (" +
    "            (" +
    "                o.lsprojectmaster_projectcode IN (" +
    "                    SELECT lsprojectmaster_projectcode " +
    "                    FROM LSlogilablimsorderdetail " +
    "                    WHERE lsprojectmaster_projectcode IN (" +
    "                        SELECT DISTINCT projectcode " +
    "                        FROM LSprojectmaster " +
    "                        WHERE lsusersteam_teamcode IN (" +
    "                            SELECT teamcode " +
    "                            FROM LSuserteammapping " +
    "                            WHERE lsuserMaster_usercode = ?5 AND teamcode IS NOT NULL " +
    "                        ) AND status = 1 " +
    "                    ) " +
    "                ) " +
    "                AND createdtimestamp BETWEEN ?3 AND ?4 " +
    "                AND approvelstatus != ?6 " +
    "                AND ordercancell IS NULL " +
    "                AND assignedto_usercode IS NULL " +
    "            ) " +
    "            OR (" +
    "                o.lsprojectmaster_projectcode IN (" +
    "                    SELECT lsprojectmaster_projectcode " +
    "                    FROM LSlogilablimsorderdetail " +
    "                    WHERE lsprojectmaster_projectcode IN (" +
    "                        SELECT DISTINCT projectcode " +
    "                        FROM LSprojectmaster " +
    "                        WHERE lsusersteam_teamcode IN (" +
    "                            SELECT teamcode " +
    "                            FROM LSuserteammapping " +
    "                            WHERE lsuserMaster_usercode = ?5 AND teamcode IS NOT NULL " +
    "                        ) AND status = 1 " +
    "                    ) " +
    "                ) " +
    "                AND createdtimestamp BETWEEN ?3 AND ?4 " +
    "                AND approvelstatus IS NULL " +
    "                AND ordercancell IS NULL " +
    "                AND assignedto_usercode IS NULL " +
    "            ) " +
    "            OR (" +
    "                o.lsprojectmaster_projectcode IN (" +
    "                    SELECT lsprojectmaster_projectcode " +
    "                    FROM LSlogilablimsorderdetail " +
    "                    WHERE lsprojectmaster_projectcode IN (" +
    "                        SELECT DISTINCT projectcode " +
    "                        FROM LSprojectmaster " +
    "                        WHERE lsusersteam_teamcode IN (" +
    "                            SELECT teamcode " +
    "                            FROM LSuserteammapping " +
    "                            WHERE lsuserMaster_usercode = ?5 AND teamcode IS NOT NULL " +
    "                        ) AND status = 1 " +
    "                    ) " +
    "                ) " +
    "                AND createdtimestamp BETWEEN ?3 AND ?4 " +
    "                AND viewoption = ?9 " +
    "                AND lsusermaster_usercode = ?5 " +
    "                AND approvelstatus != ?6 " +
    "                AND ordercancell IS NULL " +
    "                AND assignedto_usercode IS NULL " +
    "            ) " +
    "        ) " +
    "    ) " +
    "    OR (" +
    "        o.orderflag = ?1 " +
    "        AND lsprojectmaster_projectcode IS NULL " +
    "        AND elnmaterial_nmaterialcode IN (" +
    "            SELECT DISTINCT elnmaterial_nmaterialcode " +
    "            FROM lslogilablimsorderdetail " +
    "            WHERE elnmaterial_nmaterialcode IN (" +
    "                SELECT m.nmaterialcode " +
    "                FROM elnmaterial m " +
    "                WHERE m.nsitecode = ?10 " +
    "            ) " +
    "        ) " +
    "        AND createdtimestamp BETWEEN ?3 AND ?4 " +
    "        AND approvelstatus IS NULL " +
    "        AND ordercancell IS NULL " +
    "        AND assignedto_usercode IS NULL " +
    "        AND lsusermaster_usercode != ?5 " +
    "    ) " +
    "    OR (" +
    "        o.orderflag = ?1 " +
    "        AND lsprojectmaster_projectcode IS NULL " +
    "        AND elnmaterial_nmaterialcode IN (" +
    "            SELECT DISTINCT elnmaterial_nmaterialcode " +
    "            FROM lslogilablimsorderdetail " +
    "            WHERE elnmaterial_nmaterialcode IN (" +
    "                SELECT m.nmaterialcode " +
    "                FROM elnmaterial m " +
    "                WHERE m.nsitecode = ?10 " +
    "            ) " +
    "        ) " +
    "        AND createdtimestamp BETWEEN ?3 AND ?4 " +
    "        AND approvelstatus != ?6 " +
    "        AND ordercancell IS NULL " +
    "        AND assignedto_usercode IS NULL " +
    "        AND lsusermaster_usercode != ?5 " +
    "    ) " +
    "ORDER BY batchcode DESC " +
    "OFFSET ?11 ROWS FETCH NEXT ?12 ROWS ONLY", nativeQuery = true
)

	List<LSlogilablimsorderdetail> getLSlogilablimsorderdetaildashboardforcompleted(String string, int i, Date fromdate,
			Date todate, LSuserMaster objuser, int j, int k, int l, int m, LSSiteMaster lssitemaster, int n,
			Integer pageperorder);

	List<LogilabOrdermastersh> findByLsprojectmasterIsNullAndViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNull(
			int i, boolean b, List<Long> selectedteambatchCodeList, Date fromdate, Date todate, int j);

	Collection<? extends Logilaborderssh> findByTeamselectedAndBatchcodeInAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
			boolean b, List<Long> selectedteambatchCodeList, List<Long> directory_Code, int i, Date fromdate,
			Date todate, List<LSuserMaster> lstuserMaster, String orderflag, Integer filetype);

	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTeamselectedAndAssignedtoIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer filetype,
			List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2, Date todate2,
			String orderflag2, Integer filetype2, List<Long> directory_Code3, int k, Date fromdate3, Date todate3,
			List<LSuserMaster> lstuserMaster, String orderflag3, Integer filetype3, boolean b);

	long countByOrderflagAndTeamselectedAndLsprojectmasterIsNullAndViewoptionAndBatchcodeInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrderByBatchcodeDesc(
			String string, boolean b, int i, List<Long> selectedteambatchCodeList, Date fromdate, Date todate);

	long countByLsprojectmasterIsNullAndViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNull(
			int i, boolean b, List<Long> selectedteambatchCodeList, Date fromdate, Date todate, int j);

	List<Logilaborderssh> findByOrderflagAndFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNull(
			String orderflag, Integer filetype, LSprojectmaster lsprojectmaster, Date fromdate, Date todate);

	List<LogilabOrdermastersh> findByTeamselectedAndBatchcodeInAndLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
			boolean b, List<Long> selectedteambatchCodeList, List<Long> directorycode, int i, Date fromdate,
			Date todate, LSuserMaster objuser, Pageable pageable);

	List<LogilabOrdermastersh> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndTeamselectedAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedAndBatchcodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, String string, boolean b, List<Long> directorycode,
			int j, Date fromdate2, Date todate2, String string2, List<Long> directorycode2, int k, Date fromdate3,
			Date todate3, LSuserMaster objuser2, String string3, List<Long> directorycode3, boolean c, int l,
			Date fromdate4, Date todate4, LSuserMaster objuser3, String string4, int m, boolean d,
			List<Long> selectedteambatchCodeList, Date fromdate5, Date todate5, LSuserMaster objuser4, String string5,
			List<Long> directorycode4, boolean e, List<Long> selectedteambatchCodeList2, int n, Date fromdate6,
			Date todate6, LSuserMaster objuser5, String string6, Pageable pageable);

	List<LogilabOrdermastersh> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeAndTeamselectedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeAndTeamselectedOrViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedAndBatchcodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, Integer testcode, boolean b,
			List<Long> directorycode, int j, Date fromdate2, Date todate2, Integer testcode2, List<Long> directorycode2,
			int k, Date fromdate3, Date todate3, LSuserMaster objuser2, Integer testcode3, List<Long> directorycode3,
			int l, Date fromdate4, Date todate4, LSuserMaster objuser3, Integer testcode4, boolean c, int m, boolean d,
			List<Long> selectedteambatchCodeList, Date fromdate5, Date todate5, LSuserMaster objuser4,
			Integer testcode5, List<Long> directorycode4, boolean e, List<Long> selectedteambatchCodeList2, int n,
			Date fromdate6, Date todate6, LSuserMaster objuser5, Integer testcode6, Pageable pageable);

	List<LogilabOrdermastersh> findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTeamselectedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTeamselectedOrViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedAndBatchcodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
			int i, Date fromdate, Date todate, LSuserMaster objuser, boolean b, List<Long> directorycode, int j,
			Date fromdate2, Date todate2, List<Long> directorycode2, int k, Date fromdate3, Date todate3,
			LSuserMaster objuser2, List<Long> directorycode3, int l, Date fromdate4, Date todate4,
			LSuserMaster objuser3, boolean c, int m, boolean d, List<Long> selectedteambatchCodeList, Date fromdate5,
			Date todate5, LSuserMaster objuser4, List<Long> directorycode4, boolean e,
			List<Long> selectedteambatchCodeList2, int n, Date fromdate6, Date todate6, LSuserMaster objuser5,
			Pageable pageable);

	long countByViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedAndBatchcodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNull(
			int i, boolean b, List<Long> selectedteambatchCodeList, Date fromdate, Date todate, LSuserMaster objuser,
			String string, List<Long> directorycode, boolean c, List<Long> selectedteambatchCodeList2, int j,
			Date fromdate2, Date todate2, LSuserMaster objuser2, String string2);

	long countByViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedAndBatchcodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrderByBatchcodeDesc(
			int i, boolean b, List<Long> selectedteambatchCodeList, Date fromdate, Date todate, LSuserMaster objuser,
			int j, List<Long> directorycode, boolean c, List<Long> selectedteambatchCodeList2, int k, Date fromdate2,
			Date todate2, LSuserMaster objuser2, int l);

	Collection<? extends Logilaborderssh> findByOrderflagAndLsprojectmasterIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterInAndOrdercancellIsNullAndTeamselectedOrTeamselectedAndBatchcodeInAndOrderflagAndLsprojectmasterIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterInAndOrdercancellIsNullOrderByBatchcodeDesc(
			String orderflag, List<Elnmaterial> currentChunk, Integer filetype, Date fromdate, Date todate, int i,
			List<LSuserMaster> lstuserMaster, boolean b, boolean c, List<Long> selectedteambatchCodeList,
			String orderflag2, List<Elnmaterial> currentChunk2, Integer filetype2, Date fromdate2, Date todate2, int j,
			List<LSuserMaster> lstuserMaster2);

	Collection<? extends Logilaborderssh> findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullOrTeamselectedAndBatchcodeInAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndElnmaterialIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTeamselectedAndAssignedtoIsNullAndElnmaterialIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
			List<Long> directory_Code, int i, Date fromdate, Date todate, String orderflag, Integer filetype,
			List<Long> directory_Code2, int j, LSuserMaster lsuserMaster, Date fromdate2, Date todate2,
			String orderflag2, Integer filetype2, boolean b, List<Long> selectedteambatchCodeList,
			List<Long> directory_Code3, int k, Date fromdate3, Date todate3, List<LSuserMaster> lstuserMaster,
			String orderflag3, Integer filetype3, List<Long> directory_Code4, int l, Date fromdate4, Date todate4,
			List<LSuserMaster> lstuserMaster2, String orderflag4, Integer filetype4, boolean c);

	Collection<? extends Logilaborderssh> findByOrderflagAndLsprojectmasterIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndOrdercancellIsNullAndTeamselectedOrTeamselectedAndBatchcodeInAndOrderflagAndLsprojectmasterIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndOrdercancellIsNullOrderByBatchcodeDesc(
			String orderflag, List<Elnmaterial> currentChunk, Integer filetype, Date fromdate, Date todate, int i,
			boolean b, boolean c, List<Long> selectedteambatchCodeList, String orderflag2,
			List<Elnmaterial> currentChunk2, Integer filetype2, Date fromdate2, Date todate2, int j);

}
