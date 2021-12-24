package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

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
	@Query("select new com.agaram.eln.primary.model.sheetManipulation.LSfile(filecode,filenameuser) from LSfile where filecode >1 and approved= ?1 ORDER BY filecode DESC")
	public List<LSfile> getsheetGreaterthanoneandapprovel(Integer Approved);
	
	@Transactional
	@Modifying
	@Query("select new com.agaram.eln.primary.model.sheetManipulation.LSfile(filecode,filenameuser) from LSfile where filecode >1 and approved= ?1 and rejected=0 and createby in (?2) ORDER BY filecode DESC")
	public List<LSfile> getsheetGreaterthanoneandapprovelanduserIn(Integer Approved, List<LSuserMaster> lstusermaster);

	public LSfile findByfilenameuserIgnoreCase(String filenameuser);
	
	/**
	 * Added by sathishkumar chandrasekar 
	 * 
	 * for reducing sheet loading time on sheet template creation 
	 */
	
//	public List<Sheettemplateget> findByFilecodeGreaterThanAndCreatebyInOrderByFilecodeDesc( Integer filecode,List<LSuserMaster> lstusermaster);
	
	public List<Sheettemplateget> findByFilecodeGreaterThanAndCreatebyInOrderByFilecodeDesc( Integer filecode,List<LSuserMaster> lstusermaster);
	
//	public List<Sheettemplateget> findByCreatebyInAndFilecodeGreaterThanOrderByFilecodeDesc(List<LSuserMaster> lstusermaster, Integer filecode);
	
	public List<Sheettemplateget> findByCreatebyAndCreatedateBetweenOrderByFilecodeDesc(LSuserMaster lsusermaster,Date fromdate, Date todate);
	
	public List<Sheettemplateget> findByCreatedateBetweenAndFilecodeGreaterThanOrderByFilecodeDesc(Date fromdate, Date todate, Integer filecode);
	
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

	public List<Sheettemplateget> findByFilecodeGreaterThanAndCreatebyInOrderByFilecodeDesc(int filecode,
			LSuserMaster objuser);
	
	public List<Sheettemplateget> findByApprovedAndLssitemasterAndFilecodeGreaterThan(Integer approvelstatus,LSSiteMaster lssitemaster, Integer filecode);

	public List<Sheettemplateget> findByLssitemasterAndFilecodeGreaterThan(LSSiteMaster site, int j);

	public List<LSfile> findByFilecodeGreaterThanAndApprovedOrderByCreatedateDesc(int i, int j);

	public List<LSfile> findByFilecodeGreaterThanAndApprovedAndLssitemasterOrderByCreatedateDesc(int i, int j,
			LSSiteMaster site);

}
