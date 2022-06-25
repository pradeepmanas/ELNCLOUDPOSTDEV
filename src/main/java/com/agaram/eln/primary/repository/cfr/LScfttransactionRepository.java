package com.agaram.eln.primary.repository.cfr;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agaram.eln.primary.model.cfr.LScfttransaction;


public interface LScfttransactionRepository extends JpaRepository<LScfttransaction, Integer> {

	List<LScfttransaction> findByserialnoIn(Integer serialno);
    List<LScfttransaction> findByserialnoIn(List<Integer> lstserailno);
    List<LScfttransaction> findBytransactiondateBetween(Object object, Object object2);
	List<LScfttransaction> findByTransactiondateBetweenOrderBySerialnoDesc(Date fromDate, Date toDate);
//	List<LScfttransaction> findByLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(LSuserMaster objuser,Date fromDate, Date toDate);
	List<LScfttransaction> findByLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(Integer objuser,Date fromDate, Date toDate);
	List<LScfttransaction> findByModuleNameAndTransactiondateBetweenOrderBySerialnoDesc(String ModuleName, Date fromDate, Date toDate);
	List<LScfttransaction> findBysystemcomentsAndTransactiondateBetweenOrderBySerialnoDesc(String Audit, Date fromDate, Date toDate);//kumaresan
//	List<LScfttransaction> findByModuleNameAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(String ModuleName, LSuserMaster objuser,Date fromDate, Date toDate);
	List<LScfttransaction> findByModuleNameAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(String ModuleName, Integer objuser,Date fromDate, Date toDate);
//	LScfttransaction findFirstByModuleNameAndLsuserMasterOrderBySerialnoDesc(String ModuleName, LSuserMaster objuser);
	LScfttransaction findFirstByModuleNameAndLsuserMasterOrderBySerialnoDesc(String ModuleName, Integer objuser);
//	List<LScfttransaction> findBysystemcomentsAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(String Audit,LSuserMaster objuser,Date fromDate, Date toDate);
	List<LScfttransaction> findBysystemcomentsAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(String Audit,Integer objuser,Date fromDate, Date toDate);
	List<LScfttransaction> findBysystemcomentsAndModuleNameAndTransactiondateBetweenOrderBySerialnoDesc(String Audit,String ModuleName,Date fromDate, Date toDate);
//	List<LScfttransaction> findBysystemcomentsAndModuleNameAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(String Audit,String ModuleName,LSuserMaster objuser,Date fromDate, Date toDate);
	List<LScfttransaction> findBysystemcomentsAndModuleNameAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(String Audit,String ModuleName,Integer objuser,Date fromDate, Date toDate);

	@Transactional
	@Modifying
	@Query("update LScfttransaction set reviewedstatus='reviewed'  where serialno in (:transcode)")
	public void updateReviewedstatus(@Param("transcode") List<Integer> transcode);
// 
//	@Transactional
//	@Modifying
//	@Query(value = "select lc.*,ls.username as username from LScfttransaction lc inner join \r\n" + 
//			"lsusermaster ls on ls.usercode=lc.lsusermaster_usercode where lc.transactiondate BETWEEN ?1 and ?2", nativeQuery=true)
//	List<LScfttransaction> findByTransactiondateBetweenOrderBySerialnoDesc(Date fromDate, Date toDate);
//	
//	@Transactional
//	@Modifying
//	@Query(value = "select ls.username, lc.* from LScfttransaction lc inner join \r\n" + 
//			"lsusermaster ls on ls.usercode=lc.lsusermaster_usercode where \r\n" + 
//			"lc.transactiondate between ?2 and ?3 and \r\n" + 
//			"ls.usercode = ?1", nativeQuery=true)
//	List<LScfttransaction> findByLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(Integer objuser,Date fromDate, Date toDate);
//
//	@Transactional
//	@Modifying
//	@Query(value = "select ls.username, lc.* from LScfttransaction lc inner join \r\n" + 
//			"lsusermaster ls on ls.usercode=lc.lsusermaster_usercode where \r\n" + 
//			"lc.transactiondate between ?2 and ?3 and \r\n" + 
//			"lc.modulename = ?1", nativeQuery=true)
//	List<LScfttransaction> findByModuleNameAndTransactiondateBetweenOrderBySerialnoDesc(String ModuleName, Date fromDate, Date toDate);
//
//	@Transactional
//	@Modifying
//	@Query(value = "select ls.username, lc.* from LScfttransaction lc inner join \r\n" + 
//			"lsusermaster ls on ls.usercode=lc.lsusermaster_usercode where \r\n" + 
//			"lc.transactiondate between ?3 and ?4 and \r\n" + 
//			"lc.modulename = ?1 and ls.usercode = ?2", nativeQuery=true)
//	List<LScfttransaction> findByModuleNameAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(String ModuleName, Integer objuser,Date fromDate, Date toDate);
//	
//	@Transactional
//	@Modifying
//	@Query(value = "select ls.username, lc.* from LScfttransaction lc inner join \r\n" + 
//			"lsusermaster ls on ls.usercode=lc.lsusermaster_usercode where \r\n" + 
//			"lc.transactiondate between ?3 and ?4 and \r\n" + 
//			"lc.systemcoments = ?1", nativeQuery=true)
//	List<LScfttransaction> findBysystemcomentsAndTransactiondateBetweenOrderBySerialnoDesc(String Audit, Date fromDate, Date toDate);
//
//	@Transactional
//	@Modifying
//	@Query(value = "select ls.username, lc.* from LScfttransaction lc inner join \r\n" + 
//			"lsusermaster ls on ls.usercode=lc.lsusermaster_usercode where \r\n" + 
//			"lc.transactiondate between ?3 and ?4 and \r\n" + 
//			"lc.systemcoments = ?1 and lc.modulename = ?2", nativeQuery=true)
//	List<LScfttransaction> findBysystemcomentsAndModuleNameAndTransactiondateBetweenOrderBySerialnoDesc(String Audit,String ModuleName,Date fromDate, Date toDate);
//
//	@Transactional
//	@Modifying
//	@Query(value = "select ls.username, lc.* from LScfttransaction lc inner join \r\n" + 
//			"lsusermaster ls on ls.usercode=lc.lsusermaster_usercode where \r\n" + 
//			"lc.transactiondate between ?3 and ?4 and \r\n" + 
//			"lc.systemcoments = ?1 and ls.usercode = ?2", nativeQuery=true)
//	List<LScfttransaction> findBysystemcomentsAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(String Audit,Integer objuser,Date fromDate, Date toDate);
//	
//	@Transactional
//	@Modifying
//	@Query(value = "select ls.username, lc.* from LScfttransaction lc inner join \r\n" + 
//			"lsusermaster ls on ls.usercode=lc.lsusermaster_usercode where \r\n" + 
//			"lc.transactiondate between ?4 and ?5 and \r\n" + 
//			"lc.systemcoments = ?1 and ls.modulename = ?2 and lc.usercode = ?3", nativeQuery=true)
//	List<LScfttransaction> findBysystemcomentsAndModuleNameAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(String Audit,String ModuleName,Integer objuser,Date fromDate, Date toDate);
	////////////////
//	@Transactional
//	@Modifying
//	@Query(value = "select ls.username, lc.* from LScfttransaction lc inner join \r\n" + 
//			"lsusermaster ls on ls.usercode=lc.lsusermaster_usercode where lc.transactiondate BETWEEN ?1 and ?2", nativeQuery=true)
//	List<Map<String, Object>> findByTransactiondateBetweenOrderBySerialnoDesc(Date fromDate, Date toDate);
//	
//	@Transactional
//	@Modifying
//	@Query(value = "select ls.username, lc.* from LScfttransaction lc inner join \r\n" + 
//			"lsusermaster ls on ls.usercode=lc.lsusermaster_usercode where \r\n" + 
//			"lc.transactiondate between ?2 and ?3 and \r\n" + 
//			"ls.usercode = ?1", nativeQuery=true)
//	List<Map<String, Object>> findByLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(Integer objuser,Date fromDate, Date toDate);
//
//	@Transactional
//	@Modifying
//	@Query(value = "select ls.username, lc.* from LScfttransaction lc inner join \r\n" + 
//			"lsusermaster ls on ls.usercode=lc.lsusermaster_usercode where \r\n" + 
//			"lc.transactiondate between ?2 and ?3 and \r\n" + 
//			"lc.modulename = ?1", nativeQuery=true)
//	List<Map<String, Object>> findByModuleNameAndTransactiondateBetweenOrderBySerialnoDesc(String ModuleName, Date fromDate, Date toDate);
//
//	@Transactional
//	@Modifying
//	@Query(value = "select ls.username, lc.* from LScfttransaction lc inner join \r\n" + 
//			"lsusermaster ls on ls.usercode=lc.lsusermaster_usercode where \r\n" + 
//			"lc.transactiondate between ?3 and ?4 and \r\n" + 
//			"lc.modulename = ?1 and ls.usercode = ?2", nativeQuery=true)
//	List<Map<String, Object>> findByModuleNameAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(String ModuleName, Integer objuser,Date fromDate, Date toDate);
//	
//	@Transactional
//	@Modifying
//	@Query(value = "select ls.username, lc.* from LScfttransaction lc inner join \r\n" + 
//			"lsusermaster ls on ls.usercode=lc.lsusermaster_usercode where \r\n" + 
//			"lc.transactiondate between ?3 and ?4 and \r\n" + 
//			"lc.systemcoments = ?1", nativeQuery=true)
//	List<Map<String, Object>> findBysystemcomentsAndTransactiondateBetweenOrderBySerialnoDesc(String Audit, Date fromDate, Date toDate);
//
//	@Transactional
//	@Modifying
//	@Query(value = "select ls.username, lc.* from LScfttransaction lc inner join \r\n" + 
//			"lsusermaster ls on ls.usercode=lc.lsusermaster_usercode where \r\n" + 
//			"lc.transactiondate between ?3 and ?4 and \r\n" + 
//			"lc.systemcoments = ?1 and lc.modulename = ?2", nativeQuery=true)
//	List<Map<String, Object>> findBysystemcomentsAndModuleNameAndTransactiondateBetweenOrderBySerialnoDesc(String Audit,String ModuleName,Date fromDate, Date toDate);
//
//	@Transactional
//	@Modifying
//	@Query(value = "select ls.username, lc.* from LScfttransaction lc inner join \r\n" + 
//			"lsusermaster ls on ls.usercode=lc.lsusermaster_usercode where \r\n" + 
//			"lc.transactiondate between ?3 and ?4 and \r\n" + 
//			"lc.systemcoments = ?1 and ls.usercode = ?2", nativeQuery=true)
//	List<Map<String, Object>> findBysystemcomentsAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(String Audit,Integer objuser,Date fromDate, Date toDate);
//	
//	@Transactional
//	@Modifying
//	@Query(value = "select ls.username, lc.* from LScfttransaction lc inner join \r\n" + 
//			"lsusermaster ls on ls.usercode=lc.lsusermaster_usercode where \r\n" + 
//			"lc.transactiondate between ?4 and ?5 and \r\n" + 
//			"lc.systemcoments = ?1 and ls.modulename = ?2 and lc.usercode = ?3", nativeQuery=true)
//	List<Map<String, Object>> findBysystemcomentsAndModuleNameAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(String Audit,String ModuleName,Integer objuser,Date fromDate, Date toDate);
	List<LScfttransaction> findByLssitemasterAndSystemcomentsAndModuleNameAndTransactiondateBetweenOrderBySerialnoDesc(
			Integer site, String audit, String module, Date fromdate, Date todate);
	List<LScfttransaction> findByLssitemasterAndSystemcomentsAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(
			Integer site, String audit, Integer usercode, Date fromdate, Date todate);
	List<LScfttransaction> findByLssitemasterAndSystemcomentsAndModuleNameAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(
			Integer site, String audit, String module, Integer usercode, Date fromdate, Date todate);
	List<LScfttransaction> findByLssitemasterAndSystemcomentsAndTransactiondateBetweenOrderBySerialnoDesc(Integer site,
			String audit, Date fromdate, Date todate);
	List<LScfttransaction> findByLssitemasterAndModuleNameAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(
			Integer site, String module, Integer usercode, Date fromdate, Date todate);
	List<LScfttransaction> findByLssitemasterAndModuleNameAndTransactiondateBetweenOrderBySerialnoDesc(Integer site,
			String module, Date fromdate, Date todate);
	List<LScfttransaction> findByLssitemasterAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(Integer site,
			Integer usercode, Date fromdate, Date todate);
	List<LScfttransaction> findByLssitemasterAndTransactiondateBetweenOrderBySerialnoDesc(Integer site, Date fromdate,
			Date todate);
}
