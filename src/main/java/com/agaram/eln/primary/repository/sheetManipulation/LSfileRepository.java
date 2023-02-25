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
	@Query("select new com.agaram.eln.primary.model.sheetManipulation.LSfile(filecode,filenameuser) from LSfile where lssitemaster_sitecode=?2 and filecode >1 and approved= ?1 or versionno > 1 ORDER BY filecode DESC")
	public List<LSfile> getsheetGreaterthanoneandapprovel(Integer Approved,Integer sitecode);
	
	@Transactional
	@Modifying
	@Query("select new com.agaram.eln.primary.model.sheetManipulation.LSfile(filecode,filenameuser) from LSfile where lssitemaster_sitecode=?3 and filecode >1 and approved= ?1 or versionno > 1 and rejected=0 and createby in (?2) ORDER BY filecode DESC")
	public List<LSfile> getsheetGreaterthanoneandapprovelanduserIn(Integer Approved, List<LSuserMaster> lstusermaster,Integer sitecode);
   
	@Transactional
	@Modifying
	@Query("select new com.agaram.eln.primary.model.sheetManipulation.LSfile(filecode,filenameuser) from LSfile where lssitemaster_sitecode=?3 and filecode >1 and approved= ?1 and rejected=0 or approved is null and rejected=0 or versionno > 0 and approved !=1 and rejected=0 and createby in (?2) ORDER BY filecode DESC")
	public List<LSfile> getsheetapprovelanduserIn(List<LSuserMaster> lstusermaster,Integer sitecode);
	
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
	
	public List<Sheettemplateget> findByCreatedateBetweenAndFilecodeGreaterThanOrderByFilecodeDesc(Date fromdate, Date todate, Integer filecode, Pageable pageable);
	
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

	public Long findByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndRejectedOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndRejectedOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, LSuserMaster objuser,
			Date fromdate2, Date todate2, int m, int n);

	public Long findByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, int m,
			LSuserMaster objuser, Date fromdate2, Date todate2, int n, int o, int p);

	public Long findByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedNotOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, int m,
			LSuserMaster objuser, Date fromdate2, Date todate2, int n, int o, int p);

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

	public Object findByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedNotOrderByFilecodeDesc(
			int i, LSSiteMaster lssitemaster, Date fromdate, Date todate, int j, int k, int l, LSuserMaster objuser,
			Date fromdate2, Date todate2, int m, int n);

	public Object countByCreatedateBetweenAndFilecodeGreaterThanAndApprovedAndRejectedOrderByFilecodeDesc(Date fromdate,
			Date todate, int i, int j, int k);

	public long countByCreatedateBetweenAndFilecodeGreaterThanOrderByFilecodeDesc(Date fromdate, Date todate, int i);

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




}