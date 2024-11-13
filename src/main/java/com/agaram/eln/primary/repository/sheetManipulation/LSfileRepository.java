package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agaram.eln.primary.fetchmodel.gettemplate.Sheettemplateget;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSfiletest;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;


public interface LSfileRepository extends JpaRepository<LSfile, Integer>{
	
//	@Query("select filecode, extension, filenameuser, testID, isactive, siteID, versionno, createby, createdate, filetypecode from lsfile where filecode = ?1")
//	public LSfile findByID(Integer filecode);
	
//	@Query("select filecode, extension, filenameuser, testID, isactive, siteID, versionno, createby, createdate, filetypecode from lsfile where testID = ?1")
//	public LSfile findByTest(Integer testid);
	
	public List<Sheettemplateget> findBylstestIn(List<LSfiletest> lsfiletest);
	
	public List<LSfile> findByfilecodeGreaterThan(Integer filecode);
	
	public List<LSfile> findByApprovedAndFilecodeGreaterThan(Integer approvelstatus, Integer filecode);
	
	public LSfile findByfilecode(Integer filecode);
	
	public LSfile findByfilenameuser(String filename);
	
	public List<LSfile> findByCreatebyInAndFilecodeGreaterThanOrderByFilecodeDesc(List<LSuserMaster> lstusermaster, Integer filecode);
	
	public List<LSfile> findByApprovedAndCreatebyInAndFilecodeGreaterThan(Integer approvelstatus,List<LSuserMaster> lstusermaster, Integer filecode);

	public List<LSfile> findByFilenameuserAndFilecodeNot(String filenameuser, Integer filecode);

	public List<LSfile> findByFilenameuser(String filenameuser);

	public List<LSfile> findByFilecodeAndFilenameuserNot(Integer filecode, String filenameuser);

	public  List<LSfile> findByFilecodeAndFilenameuser(Integer filecode, String filenameuser);

	//public  List<LSfile> findByApprovedAndCreateby(Integer approval, Integer createdby);
	
	public  List<LSfile> findByApproved(Integer approval);
	
	public LSfile findByFilecodeAndApproved(Integer filecode, Integer Approved);
	
	public List<Sheettemplateget> findByCreatebyInAndLstestInAndFilecodeGreaterThan(List<LSuserMaster> lstusermaster,List<LSfiletest> lsfiletest, Integer filecode);
	
	public Sheettemplateget findByCreatebyAndLstestInAndFilecodeGreaterThan(LSuserMaster lstusermaster,List<LSfiletest> lsfiletest, Integer filecode);
	
//	public  List<LSfile> findByFilenameuserNotAndRejectedNot(String filenameuser, Integer reject);
	
	@Transactional
	@Modifying
	@Query("update LSfile set lssheetworkflow= :workflow, approved= :approved, rejected= :rejected  where filecode in (:filecode)")
	public void updateFileWorkflow(@Param("workflow")  LSsheetworkflow lssheetworkflow, @Param("approved")  Integer approved, @Param("rejected")  Integer rejected, @Param("filecode")  Integer filecode);
	
	public long countByLssheetworkflowAndApproved( LSsheetworkflow lssheetworkflow, Integer Approved);
	
	@Transactional
	@Modifying
	@Query("update LSfile o set o.lssheetworkflow = null where o.lssheetworkflow = ?1 and approved= ?2")
	void setWorkflownullforApprovedfile(LSsheetworkflow lssheetworkflow, Integer Approved);
	
	@Transactional
	@Modifying
	@Query("select new com.agaram.eln.primary.model.sheetManipulation.LSfile(filecode,filenameuser) from LSfile where filecode >1 ORDER BY filecode DESC")
	public List<LSfile> getsheetGreaterthanone();
	
	@Transactional
	@Modifying
	@Query("select new com.agaram.eln.primary.model.sheetManipulation.LSfile(filecode,filenameuser) from LSfile where filecode >1 and createby in (?1) ORDER BY filecode DESC")
	public List<LSfile> getsheetGreaterthanOneAndCreatedByUserIN(List<LSuserMaster> lstusermaster);
	
	@Transactional
	@Modifying
//	@Query("select new com.agaram.eln.primary.model.sheetManipulation.LSfile(filecode,filenameuser) from LSfile where lssitemaster_sitecode=?2 and filecode >1 and approved= ?1 or versionno > 1 ORDER BY filecode DESC")
	@Query("SELECT NEW com.agaram.eln.primary.model.sheetManipulation.LSfile(filecode, filenameuser) \r\n" + 
			"FROM com.agaram.eln.primary.model.sheetManipulation.LSfile \r\n" + 
			"WHERE retirestatus=0 AND lssitemaster_sitecode = ?1 AND \r\n" + 
			"      ((filecode > 1 AND approved != 1) OR approved IS NULL) AND \r\n" + 
			"      ((rejected != 1) OR (versionno > 1)) \r\n" + 
			"ORDER BY filecode DESC")
	public List<LSfile> getsheetGreaterthanoneandapprovel(Integer sitecode);
	
	@Transactional
	@Modifying
	@Query("select new com.agaram.eln.primary.model.sheetManipulation.LSfile(filecode,filenameuser) from LSfile where lssitemaster_sitecode=?3 and filecode >1 and approved= ?1 or versionno > 1 and rejected=0 and createby in (?2) ORDER BY filecode DESC")
	public List<LSfile> getsheetGreaterthanoneandapprovelanduserIn(Integer Approved, List<LSuserMaster> lstusermaster,Integer sitecode);
   
	@Transactional
	@Modifying
//	@Query("select new com.agaram.eln.primary.model.sheetManipulation.LSfile(filecode,filenameuser) from LSfile where lssitemaster_sitecode=?3 and filecode >1 and approved= ?1 and rejected!=1	and versionno >0 or versionno is null and approved !=1	and rejected=0 or approved is null and createby in (?2) ORDER BY filecode DESC")
	@Query("SELECT NEW com.agaram.eln.primary.model.sheetManipulation.LSfile(filecode, filenameuser) \r\n" + 
			"FROM com.agaram.eln.primary.model.sheetManipulation.LSfile \r\n" + 
			"WHERE retirestatus=0 AND lssitemaster_sitecode = ?2 AND \r\n" + 
			"      ((filecode > 1 AND approved != 1 AND rejected != 1) OR approved IS NULL) AND \r\n" + 
			"      ((rejected != 1) OR (versionno > 1)) \r\n" + 
			" and createby in (?1)ORDER BY filecode DESC")
	public List<LSfile> getsheetapprovelanduserIn(List<LSuserMaster> lstusermaster,Integer sitecode);
	
	@Transactional
	@Modifying
	@Query("SELECT NEW com.agaram.eln.primary.model.sheetManipulation.LSfile(filecode, filenameuser) \r\n" + 
			"FROM com.agaram.eln.primary.model.sheetManipulation.LSfile \r\n" + 
			"WHERE retirestatus = 0 And (tagsheet = 1 OR resultsheet = 1) AND lssitemaster_sitecode = ?2 AND \r\n" + 
			"      (filecode > 1 AND approved = 1 AND rejected != 1) AND \r\n" + 
			"      (rejected != 1) \r\n" + 
			" and createby in (?1)ORDER BY filecode DESC")
	public List<LSfile> gettemplateapprovelanduserIn(List<LSuserMaster> lstusermaster,Integer sitecode);
	
	//public List<LSfile> getsheetapprovelanduserIn(List<LSuserMaster> lstusermaster,Integer sitecode);
	public List<LSfile> findByFilecodeGreaterThanAndApprovedOrFilecodeGreaterThanAndVersionnoGreaterThanOrderByFilecodeDesc(int filecode, int approved, int orfilecode, int version);
	
	public List<LSfile> findByFilecodeGreaterThanAndCreatebyInAndRejectedAndApprovedOrFilecodeGreaterThanAndCreatebyInAndRejectedAndVersionnoGreaterThanOrderByFilecodeDesc
	(int filecode, List<LSuserMaster> lstusermaster, int rejected, int approved, int orfilecode, List<LSuserMaster> orlstusermaster, int orrejected, int version);
	
	public LSfile findByfilenameuserIgnoreCase(String filenameuser);
	
	/**
	 * Added by sathishkumar chandrasekar 
	 * 
	 * for reducing sheet loading time on sheet template creation 
	 */
	
//	public List<Sheettemplateget> findByFilecodeGreaterThanAndCreatebyInOrderByFilecodeDesc( Integer filecode,List<LSuserMaster> lstusermaster);
	
//	public List<Sheettemplateget> findByFilecodeGreaterThanAndCreatebyInOrderByFilecodeDesc( Integer filecode,List<LSuserMaster> lstusermaster);
	
//	public List<Sheettemplateget> findByCreatebyInAndFilecodeGreaterThanOrderByFilecodeDesc(List<LSuserMaster> lstusermaster, Integer filecode);
	
	public List<Sheettemplateget> findByCreatebyAndCreatedateBetweenOrderByFilecodeDesc(LSuserMaster lsusermaster,Date fromdate, Date todate);
	

	public List<Sheettemplateget> findByFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenOrderByFilecodeDesc(Integer filecode,List<LSuserMaster> lstusermaster, Date fromdate, Date todate);
	
	public List<Sheettemplateget> findByFilecodeGreaterThanAndCreatebyAndCreatedateBetweenOrderByFilecodeDesc(Integer filecode, LSuserMaster lsusermaster,Date fromdate, Date todate);

	public List<LSfile> findByApprovedOrderByCreatedateDesc(int i);
	
	public Sheettemplateget findByFilecode(Integer filecode);
	
	public Long countByCreatebyIn(List<LSuserMaster> lstusermaster);
	
	public Long countByCreateby(LSuserMaster lsusermaster);
	
	public Long countByFilecodeGreaterThan(Integer filecode);
	
	public List<Sheettemplateget> findFirst20ByFilecodeGreaterThanOrderByFilecodeDesc(Integer filecode);
	
	public List<Sheettemplateget> findFirst20ByFilecodeGreaterThanAndFilecodeLessThanOrderByFilecodeDesc(Integer defaultfilecode,Integer filecode);
	
	public List<Sheettemplateget> findFirst20ByCreatebyInOrderByFilecodeDesc(List<LSuserMaster> lstusermaster);
	
	public List<Sheettemplateget> findFirst20ByCreatebyOrderByFilecodeDesc(LSuserMaster lsusermaster);
	
	public List<Sheettemplateget> findFirst20ByFilecodeLessThanAndCreatebyInOrderByFilecodeDesc(Integer filecode, List<LSuserMaster> lstusermaster);
	
	public List<Sheettemplateget> findFirst20ByFilecodeLessThanAndCreatebyOrderByFilecodeDesc(Integer filecode, LSuserMaster lsusermaster);

//	public List<Sheettemplateget> findByFilecodeGreaterThanAndCreatebyInOrderByFilecodeDesc(int filecode,
//			LSuserMaster objuser);
	
	public List<Sheettemplateget> findByApprovedAndLssitemasterAndFilecodeGreaterThan(Integer approvelstatus,LSSiteMaster lssitemaster, Integer filecode);

	public List<Sheettemplateget> findByLssitemasterAndFilecodeGreaterThan(LSSiteMaster site, int j);

	public List<LSfile> findByFilecodeGreaterThanAndApprovedOrderByCreatedateDesc(int i, int j);

	public List<LSfile> findByFilecodeGreaterThanAndApprovedAndLssitemasterOrderByCreatedateDesc(int i, int j,
			LSSiteMaster site);

	public List<LSfile> findBylssitemaster_sitecode(Integer sitecode);

	public Object findByfilenameuserIgnoreCaseAndLssitemaster(String filenameuser, LSSiteMaster lssitemaster);

	public List<LSfile> findByFilecode(List<Integer> listobjfilecode);

	public List<Sheettemplateget> findByFilecodeIn(List<Integer> listobjfilecode);

//	public List<LSfile> findByFilenameuserAndCategory(String filenameuser, String category);

	public Object countByCreatebyIn(Integer sitecode);

	public List<LSfile> findByFilecodeNotAndFilenameuser(Integer filecode, String filenameuser);

	public List<Sheettemplateget> findByFilecodeGreaterThanAndLssitemasterAndViewoptionOrFilecodeGreaterThanAndCreatebyAndViewoptionOrFilecodeGreaterThanAndCreatebyInAndViewoptionOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, int j, int k, LSuserMaster objuser, int l, int m,
			List<LSuserMaster> lstteamuser, int n);

	public List<Sheettemplateget> findByFilecodeGreaterThanAndLssitemasterAndViewoptionOrFilecodeGreaterThanAndCreatebyInAndViewoptionOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, int j, int k, LSuserMaster objuser, int l);

//	public List<Sheettemplateget> findByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionOrderByFilecodeDesc(
//			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, LSuserMaster objuser,
//			Date fromdate2, Date todate2, int l);

//	public List<Sheettemplateget> findByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndViewoptionOrderByFilecodeDesc(
//			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, LSuserMaster objuser,
//			Date fromdate2, Date todate2, int l, int m, List<LSuserMaster> lstteamuser, Date fromdate3, Date todate3,
//			int n);

	public Object countByFilecodeGreaterThanAndLssitemasterAndViewoptionOrFilecodeGreaterThanAndCreatebyAndViewoptionOrFilecodeGreaterThanAndCreatebyInAndViewoptionOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, int j, int k, LSuserMaster objusers, int l, int m,
			List<LSuserMaster> lstteamuser, int n);

	public Object countByFilecodeGreaterThanAndLssitemasterAndViewoptionOrFilecodeGreaterThanAndCreatebyInAndViewoptionOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, int j, int k, LSuserMaster objusers, int l);

	public Long countByCreatedateBetweenAndFilecodeGreaterThanAndRejectedOrderByFilecodeDesc(Date fromdate,
			Date todate, int i, int j);

	public Long countByCreatedateBetweenAndFilecodeGreaterThanAndApprovedAndRejectedNotOrderByFilecodeDesc(
			Date fromdate, Date todate, int i, int j, int k);

	public Long countByCreatedateBetweenAndFilecodeGreaterThanAndApprovedNotAndRejectedNotOrderByFilecodeDesc(
			Date fromdate, Date todate, int i, int j, int k);

	public Long countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndRejectedOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndRejectedOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndViewoptionAndRejectedOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, LSuserMaster objuser,
			Date fromdate2, Date todate2, int m, int n, int o, List<LSuserMaster> lstteamuser, Date fromdate3,
			Date todate3, int p, int q);

	public Long countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, int m,
			LSuserMaster objuser, Date fromdate2, Date todate2, int n, int o, int p, int q,
			List<LSuserMaster> lstteamuser, Date fromdate3, Date todate3, int r, int s, int t);

	public Long countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedNotOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedNotOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, int m,
			LSuserMaster objuser, Date fromdate2, Date todate2, int n, int o, int p, int q,
			List<LSuserMaster> lstteamuser, Date fromdate3, Date todate3, int r, int s, int t);

//	public Long findByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndRejectedOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndRejectedOrderByFilecodeDesc(
//			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, LSuserMaster objuser,
//			Date fromdate2, Date todate2, int m, int n);
//
//	public Long findByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrderByFilecodeDesc(
//			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, int m,
//			LSuserMaster objuser, Date fromdate2, Date todate2, int n, int o, int p);
//
//	public Long findByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedNotOrderByFilecodeDesc(
//			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, int m,
//			LSuserMaster objuser, Date fromdate2, Date todate2, int n, int o, int p);
//	
//	public Object findByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedNotOrderByFilecodeDesc(
//			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, LSuserMaster objuser,
//			Date fromdate2, Date todate2, int m, int n);

	public List<Sheettemplateget> findByCreatebyInAndLstestInAndFilecodeGreaterThanAndViewoptionOrCreatebyAndLstestInAndFilecodeGreaterThanAndViewoptionOrCreatebyInAndLstestInAndFilecodeGreaterThanAndViewoption(
			List<LSuserMaster> lstteamuser, List<LSfiletest> lsfiletest, int i, int j, LSuserMaster objLoggeduser,
			List<LSfiletest> lsfiletest2, int k, int l, List<LSuserMaster> lstteamuser2, List<LSfiletest> lsfiletest3,
			int m, int n);

	public Object countByCreatedateBetweenAndFilecodeGreaterThanAndApprovedIsNullAndRejectedNotOrderByFilecodeDesc(
			Date fromdate, Date todate, int i, int j);

	public Object countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedNotOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedNotOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, LSuserMaster objuser,
			Date fromdate2, Date todate2, int m, int n, int o, List<LSuserMaster> lstteamuser, Date fromdate3,
			Date todate3, int p, int q);

	public Object countByCreatedateBetweenAndFilecodeGreaterThanAndApprovedAndRejectedOrderByFilecodeDesc(Date fromdate,
			Date todate, int i, int j, int k);

	public long countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndViewoptionOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, LSuserMaster objuser,
			Date fromdate2, Date todate2, int l, int m, List<LSuserMaster> lstteamuser, Date fromdate3, Date todate3,
			int n);

	public List<Sheettemplateget> findByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndViewoptionOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, LSuserMaster objuser,
			Date fromdate2, Date todate2, int l, int m, List<LSuserMaster> lstteamuser, Date fromdate3, Date todate3,
			int n, Pageable pageable);

	public List<Sheettemplateget> findByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, LSuserMaster objuser,
			Date fromdate2, Date todate2, int l, Pageable pageable);

	public long countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, LSuserMaster objuser,
			Date fromdate2, Date todate2, int l);

	public List<Sheettemplateget> findByCreatedateBetweenAndFilecodeGreaterThanAndLssitemasterAndViewoptionOrCreatedateBetweenAndFilecodeGreaterThanAndCreatebyInAndViewoptionOrderByFilecodeDesc(
			Date fromdate, Date todate, int i, LSSiteMaster lssitemaster, int j, Date fromdate2, Date todate2, int k,
			LSuserMaster objuser, int l, Pageable pageable);

	public long countByCreatedateBetweenAndFilecodeGreaterThanAndLssitemasterAndViewoptionOrCreatedateBetweenAndFilecodeGreaterThanAndCreatebyInAndViewoptionOrderByFilecodeDesc(
			Date fromdate, Date todate, int i, LSSiteMaster lssitemaster, int j, Date fromdate2, Date todate2, int k,
			LSuserMaster objuser, int l);

	public List<Sheettemplateget> findByFilecodeNotAndFilenameuserIgnoreCase(Integer filecode, String filenameuser);

	public long countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndRejectedOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndRejectedOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, LSuserMaster objuser,
			Date fromdate2, Date todate2, int m, int n);

	public long countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, int m,
			LSuserMaster objuser, Date fromdate2, Date todate2, int n, int o, int p);

	public long countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedNotOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, LSuserMaster objuser,
			Date fromdate2, Date todate2, int m, int n);

	public List<Sheettemplateget> findBylstestInAndApproved(List<LSfiletest> lsfiletest, int i);

	public List<Sheettemplateget> findByCreatebyInAndLstestInAndFilecodeGreaterThanAndViewoptionAndApprovedOrCreatebyAndLstestInAndFilecodeGreaterThanAndViewoptionAndApprovedOrCreatebyInAndLstestInAndFilecodeGreaterThanAndViewoptionAndApproved(
			List<LSuserMaster> lstteamuser, List<LSfiletest> lsfiletest, int i, int j, int k,
			LSuserMaster objLoggeduser, List<LSfiletest> lsfiletest2, int l, int m, int n,
			List<LSuserMaster> lstteamuser2, List<LSfiletest> lsfiletest3, int o, int p, int q);

	public List<LSfile> findByFilecodeGreaterThanAndLssitemasterAndApprovedOrFilecodeGreaterThanAndVersionnoGreaterThanOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Integer approvelstatus, int j, int k);

	public List<LSfile> findByFilecodeNotAndLssitemasterAndFilenameuserIgnoreCase(Integer filecode,
			LSSiteMaster lssitemaster, String filenameuser);

	public List<Sheettemplateget> findByCreatebyInAndLstestInAndFilecodeGreaterThanAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatebyAndLstestInAndFilecodeGreaterThanAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatebyInAndLstestInAndFilecodeGreaterThanAndViewoptionAndApprovedAndVersionnoGreaterThan(
			List<LSuserMaster> lstteamuser, List<LSfiletest> lsfiletest, int i, int j, int k, int l,
			LSuserMaster objLoggeduser, List<LSfiletest> lsfiletest2, int m, int n, int o, int p,
			List<LSuserMaster> lstteamuser2, List<LSfiletest> lsfiletest3, int q, int r, int s, int t);

	public List<LSfile> findByFilecodeGreaterThanAndLssitemasterAndViewoptionAndRejectedAndApprovedOrFilecodeGreaterThanAndCreatebyAndViewoptionAndRejectedAndApprovedOrFilecodeGreaterThanAndCreatebyInAndViewoptionAndRejectedAndApprovedOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, int j, int k, Integer approvelstatus, int l, LSuserMaster objuser, int m,
			int n, Integer approvelstatus2, int o, List<LSuserMaster> lstteamuser, int p, int q,
			Integer approvelstatus3);

	public List<LSfile> findByFilecodeGreaterThanAndLssitemasterAndViewoptionAndRejectedAndApprovedOrFilecodeGreaterThanAndCreatebyInAndViewoptionAndRejectedAndApprovedOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, int j, int k, Integer approvelstatus, int l,
			List<LSuserMaster> lstteamuser, int m, int n, Integer approvelstatus2);

	public List<Sheettemplateget> findByLssitemasterAndLstestInAndFilecodeGreaterThanAndViewoptionAndApprovedOrCreatebyInAndLstestInAndFilecodeGreaterThanAndViewoptionAndApprovedOrCreatebyAndLstestInAndFilecodeGreaterThanAndViewoptionAndApprovedOrCreatebyInAndLstestInAndFilecodeGreaterThanAndViewoptionAndApproved(
			LSSiteMaster lssitemaster, List<LSfiletest> lsfiletest, int i, int j, int k, List<LSuserMaster> lstteamuser,
			List<LSfiletest> lsfiletest2, int l, int m, int n, LSuserMaster objLoggeduser, List<LSfiletest> lsfiletest3,
			int o, int p, int q, List<LSuserMaster> lstteamuser2, List<LSfiletest> lsfiletest4, int r, int s, int t);

	public List<Sheettemplateget> findByLssitemasterAndCreatebyInAndLstestInAndFilecodeGreaterThanAndViewoptionAndApprovedOrCreatebyAndLstestInAndFilecodeGreaterThanAndViewoptionAndApprovedOrCreatebyInAndLstestInAndFilecodeGreaterThanAndViewoptionAndApproved(
			LSSiteMaster lssitemaster, List<LSuserMaster> lstteamuser, List<LSfiletest> lsfiletest, int i, int j, int k,
			LSuserMaster objLoggeduser, List<LSfiletest> lsfiletest2, int l, int m, int n,
			List<LSuserMaster> lstteamuser2, List<LSfiletest> lsfiletest3, int o, int p, int q);public List<LSfile> findByfilenameuserIgnoreCaseAndLssitemasterAndRetirestatus(String trim,LSSiteMaster lssitemaster, int i);public List<LSfile> findByFilecodeGreaterThanAndLssitemasterAndApprovedAndRetirestatusOrFilecodeGreaterThanAndRetirestatusAndVersionnoGreaterThanOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Integer approvelstatus, int j, int k, int l, int m);

	public long countByLssheetworkflowAndApprovedOrApprovedIsNull(LSsheetworkflow objflow, int i);
			
	public List<LSfile> findByFilecodeGreaterThanAndRetirestatusAndLssitemasterAndViewoptionAndRejectedAndApprovedOrFilecodeGreaterThanAndRetirestatusAndCreatebyAndViewoptionAndRejectedAndApprovedOrFilecodeGreaterThanAndRetirestatusAndCreatebyInAndViewoptionAndRejectedAndApprovedOrderByFilecodeDesc(
			int i, int j, LSSiteMaster lssitemaster, int k, int l, Integer approvelstatus, int m, int n,
			LSuserMaster objuser, int o, int p, Integer approvelstatus2, int q, int r, List<LSuserMaster> lstteamuser,
			int s, int t, Integer approvelstatus3);

	public List<LSfile> findByFilecodeGreaterThanAndRetirestatusAndLssitemasterAndViewoptionAndRejectedAndApprovedOrFilecodeGreaterThanAndRetirestatusAndCreatebyInAndViewoptionAndRejectedAndApprovedOrderByFilecodeDesc(
			int i, int j, LSSiteMaster lssitemaster, int k, int l, Integer approvelstatus, int m, int n,
			List<LSuserMaster> lstteamuser, int o, int p, Integer approvelstatus2);

	public List<Sheettemplateget> findBylstestInAndApprovedAndRetirestatus(List<LSfiletest> lsfiletest, int i, int j);

	public long countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndRetirestatusOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndRetirestatusOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndRetirestatusAndViewoptionOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, LSuserMaster objuser,
			Date fromdate2, Date todate2, int m, int n, int o, List<LSuserMaster> lstteamuser, Date fromdate3,
			Date todate3, int p, int q);

	public long countByCreatedateBetweenAndFilecodeGreaterThanAndRetirestatusOrderByFilecodeDesc(Date fromdate,
			Date todate, int i, int j);

	public long countByCreatedateBetweenAndRetirestatusAndFilecodeGreaterThanAndRejectedOrderByFilecodeDesc(
			Date fromdate, Date todate, int i, int j, int k);

	public long countByCreatedateBetweenAndRetirestatusAndFilecodeGreaterThanAndApprovedAndRejectedNotOrderByFilecodeDesc(
			Date fromdate, Date todate, int i, int j, int k, int l);

	public long countByCreatedateBetweenAndRetirestatusAndFilecodeGreaterThanAndApprovedIsNullAndRejectedNotOrderByFilecodeDesc(
			Date fromdate, Date todate, int i, int j, int k);

	public long countByCreatedateBetweenAndRetirestatusAndFilecodeGreaterThanAndApprovedAndRejectedOrderByFilecodeDesc(
			Date fromdate, Date todate, int i, int j, int k, int l);

	public long countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, int m,
			LSuserMaster objuser, Date fromdate2, Date todate2, int n, int o, int p, int q,
			List<LSuserMaster> lstteamuser, Date fromdate3, Date todate3, int r, int s, int t);

	public long countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, int m, int n,
			LSuserMaster objuser, Date fromdate2, Date todate2, int o, int p, int q, int r, int s,
			List<LSuserMaster> lstteamuser, Date fromdate3, Date todate3, int t, int u, int v, int w);

	public long countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedNotOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedNotOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, int m,
			LSuserMaster objuser, Date fromdate2, Date todate2, int n, int o, int p, int q,
			List<LSuserMaster> lstteamuser, Date fromdate3, Date todate3, int r, int s, int t);

	public long countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, int m,
			LSuserMaster objuser, Date fromdate2, Date todate2, int n, int o, int p);

	public long countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, int m, int n,
			LSuserMaster objuser, Date fromdate2, Date todate2, int o, int p, int q, int r);

	public long countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedNotOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, int m,
			LSuserMaster objuser, Date fromdate2, Date todate2, int n, int o, int p);

	public long countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndRetirestatusAndViewoptionOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndRetirestatusAndViewoptionOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, LSuserMaster objuser,
			Date fromdate2, Date todate2, int m, int n);
	
	public List<Sheettemplateget> findByFilecodeGreaterThanAndLssitemasterAndViewoptionAndFilenameuserLikeOrFilecodeGreaterThanAndCreatebyInAndViewoptionAndFilenameuserLikeOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, int j, String token, int k, LSuserMaster objuser, int l, String token2,
			Pageable pageable);

	public long countByFilecodeGreaterThanAndLssitemasterAndViewoptionAndFilenameuserLikeOrFilecodeGreaterThanAndCreatebyInAndViewoptionAndFilenameuserLikeOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, int j, String token, int k, LSuserMaster objuser, int l, String token2);
	
	public List<Sheettemplateget> findByFilecodeGreaterThanAndLssitemasterAndViewoptionAndFilenameuserLikeOrFilecodeGreaterThanAndCreatebyAndViewoptionAndFilenameuserLikeOrFilecodeGreaterThanAndCreatebyInAndViewoptionAndFilenameuserLikeOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, int j, String token, int k, LSuserMaster objuser, int l, String token2,
			int m, List<LSuserMaster> lstteamuser, int n, String token3, Pageable pageable);

	public long countByFilecodeGreaterThanAndLssitemasterAndViewoptionAndFilenameuserLikeOrFilecodeGreaterThanAndCreatebyAndViewoptionAndFilenameuserLikeOrFilecodeGreaterThanAndCreatebyInAndViewoptionAndFilenameuserLikeOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, int j, String token, int k, LSuserMaster objuser, int l, String token2,
			int m, List<LSuserMaster> lstteamuser, int n, String token3);
	
	public List<Sheettemplateget> findByFilecodeGreaterThanAndLssitemasterAndViewoptionAndFilenameuserLikeOrFilecodeGreaterThanAndCreatebyAndViewoptionAndFilenameuserLikeOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, int j, String token, int k, LSuserMaster objuser, int l,
			String search_Key, Pageable pageable);

	public long countByFilecodeGreaterThanAndLssitemasterAndViewoptionAndFilenameuserLikeOrFilecodeGreaterThanAndCreatebyAndViewoptionAndFilenameuserLikeOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, int j, String token, int k, LSuserMaster objuser, int l,
			String search_Key);
	
	public long countByLssitemaster(LSSiteMaster lssitemaster);
	@Transactional
	@Query(value = "select retirestatus from lsfile where filenameuser = ?1", nativeQuery = true)
	public String getRetirestatus(String templatename);

	public List<Sheettemplateget> findByCreatebyInAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrCreatebyAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrCreatebyInAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrderByFilecodeDesc(
			List<LSuserMaster> lstteamuser, List<LSfiletest> lsfiletest, int i, int j, int k, int l,
			LSuserMaster objLoggeduser, List<LSfiletest> lsfiletest2, int m, int n, int o, int p,
			List<LSuserMaster> lstteamuser2, List<LSfiletest> lsfiletest3, int q, int r, int s, int t);

	public List<Sheettemplateget> findByCreatebyInAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatebyAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatebyInAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrderByFilecodeDesc(
			List<LSuserMaster> lstteamuser, List<LSfiletest> lsfiletest, int i, int j, int k, int l, int m,
			LSuserMaster objLoggeduser, List<LSfiletest> lsfiletest2, int n, int o, int p, int q, int r,
			List<LSuserMaster> lstteamuser2, List<LSfiletest> lsfiletest3, int s, int t, int u, int v, int w);

	public List<LSfile> findByFilecodeOrderByFilecodeDesc(Integer filecode);

	public List<Sheettemplateget> findByLssitemasterAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrCreatebyInAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrCreatebyAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrCreatebyInAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrderByFilecodeDesc(
			LSSiteMaster lssitemaster, List<LSfiletest> lsfiletest, int i, int j, int k, int l,
			List<LSuserMaster> lstteamuser, List<LSfiletest> lsfiletest2, int m, int n, int o, int p,
			LSuserMaster objLoggeduser, List<LSfiletest> lsfiletest3, int q, int r, int s, int t,
			List<LSuserMaster> lstteamuser2, List<LSfiletest> lsfiletest4, int u, int v, int w, int x);

	public List<Sheettemplateget> findByLssitemasterAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatebyInAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatebyAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatebyInAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrderByFilecodeDesc(
			LSSiteMaster lssitemaster, List<LSfiletest> lsfiletest, int i, int j, int k, int l, int m,
			List<LSuserMaster> lstteamuser, List<LSfiletest> lsfiletest2, int n, int o, int p, int q, int r,
			LSuserMaster objLoggeduser, List<LSfiletest> lsfiletest3, int s, int t, int u, int v, int w,
			List<LSuserMaster> lstteamuser2, List<LSfiletest> lsfiletest4, int x, int y, int z, int a, int b);


}