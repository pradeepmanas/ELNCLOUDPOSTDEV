package com.agaram.eln.primary.repository.protocol;

import java.util.ArrayList;
//import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agaram.eln.primary.fetchmodel.gettemplate.Protocoltemplateget;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolmastertest;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;

public interface LSProtocolMasterRepository extends JpaRepository<LSprotocolmaster, Integer> {

	List<LSprotocolmaster> findByStatus(Integer status);

	List<LSprotocolmaster> findByStatusAndLssitemaster(Integer status, Integer site);

	List<LSprotocolmaster> findByStatusAndLssitemasterAndApproved(Integer status, Integer site, Integer approve);

	LSprotocolmaster findFirstByProtocolmastercode(Integer status);

	LSprotocolmaster findFirstByProtocolmastercodeAndStatusAndLssitemaster(Integer protocolmastercode, Integer status,
			Integer site);

//	List<LSprotocolmaster> findByCreatedbyAndStatusAndLssitemaster(Integer createdby, Integer status, LSSiteMaster site);
	List<LSprotocolmaster> findByCreatedbyAndStatusAndLssitemaster(Integer createdby, Integer status, Integer site);

	List<LSprotocolmaster> findByCreatedbyAndStatusAndLssitemasterAndLSprotocolworkflow(Integer createdby,
			Integer status, Integer site, LSprotocolworkflow LSprotocolworkflow);

	List<LSprotocolmaster> findByStatusAndLssitemasterAndLSprotocolworkflowAndCreatedbyNot(Integer status, Integer site,
			LSprotocolworkflow lSprotocolworkflow, Integer createdby);

	List<LSprotocolmaster> findByStatusAndLssitemasterAndLSprotocolworkflowAndCreatedbyNotAndSharewithteam(
			Integer status, Integer site, LSprotocolworkflow lSprotocolworkflow, Integer createdby,
			Integer Sharewithteam);

	List<LSprotocolmaster> findByCreatedbyAndStatusAndLssitemasterAndSharewithteam(Integer createdby, Integer status,
			Integer site, Integer Sharewithteam);

	List<LSprotocolmaster> findByCreatedbyNotAndStatusAndLssitemasterAndSharewithteam(Integer createdby, Integer status,
			Integer site, Integer Sharewithteam);

	List<LSprotocolmaster> findByCreatedbyNotAndStatusAndLssitemasterAndSharewithteamAndLSprotocolworkflowNot(
			Integer createdby, Integer status, Integer site, Integer Sharewithteam,
			LSprotocolworkflow lSprotocolworkflow);

//	List<LSprotocolmaster> findByInCreatedbyAndStatusAndLssitemaster(Integer createdby, Integer status, LSSiteMaster site);



	@SuppressWarnings("unchecked")
	LSprotocolmaster save(LSprotocolmaster LSprotocolmasterObj);

//	List<LSprotocolmaster> findByStatusAndLssitemasterAndProtocolmastername(Integer status, LSSiteMaster site, String protocolmastername);
	List<LSprotocolmaster> findByStatusAndLssitemasterAndProtocolmastername(Integer status, Integer site,
			String protocolmastername);

//	void Save(LSprotocolmaster LSprotocolmasterObj);
//	@Transactional
//	@Modifying
//	@Query("update LSprotocolmaster set lsprotocolworkflow = :workflow, "
//			+ "approved= :approved where protocolmastercode in (:protocolmastercode)")
//	public void updateProtocolWorkflow(@Param("workflow")  lSprotocolworkflow lsprotocolworkflow,
//			@Param("approved")  Integer approved,@Param("protocolmastercode")  Integer protocolmastercode);

	@Transactional
	@Modifying
	@Query("update LSprotocolmaster set lssheetworkflow = :workflow, approved= :approved , rejected= :rejected "
			+ "where protocolmastercode in (:protocolmastercode)")
	public void updateFileWorkflow(@Param("workflow") LSsheetworkflow lSsheetworkflow,
			@Param("approved") Integer approved, @Param("rejected") Integer rejected,
			@Param("protocolmastercode") Integer protocolmastercode);

	public long countBylSprotocolworkflowAndApproved(LSprotocolworkflow lSprotocolworkflow, Integer Approved);

//	@Transactional
//	@Modifying
//	@Query("update LSprotocolmaster o set o.LSprotocolworkflow = null where o.LSprotocolworkflow = ?1 and approved= ?2")
//	void updateProtcolWorkflowNull(lSprotocolworkflow LSprotocolworkflow, Integer Approved);

	@Transactional
	@Modifying
	@Query("update LSprotocolmaster o set o.lSprotocolworkflow = null where o.lSprotocolworkflow = ?1 and approved= ?2")
	void setWorkflownullforApprovedProtcol(LSprotocolworkflow lSprotocolworkflow, Integer Approved);

	List<LSprotocolmaster> findByStatusAndLssitemasterAndLSprotocolworkflow(int i, Integer lssitemaster,
			LSprotocolworkflow lsprotocolworkflow);

	List<LSprotocolmaster> findByStatusAndLssitemasterAndLSprotocolworkflowNotAndCreatedbyNotAndSharewithteam(int i,
			Integer lssitemaster, LSprotocolworkflow lsprotocolworkflow, Integer lsuserMaster, int j);

	List<LSprotocolmaster> findByStatusAndLssitemasterAndLSprotocolworkflowNotAndCreatedbyNotAndSharewithteamAndApproved(
			int i, Integer lssitemaster, LSprotocolworkflow lsprotocolworkflow, Integer lsuserMaster, int j, int k);

	List<LSprotocolmaster> findByProtocolmastercodeIn(ArrayList<Integer> ids);

	LSprotocolmaster findByprotocolmastercode(int protocolusercode);

	/**
	 * Added for getting protocol template for dash board
	 * 
	 * @param status
	 * @return
	 */
	List<Protocoltemplateget> findBystatus(Integer status);

//	List<Protocoltemplateget> findBystatusAndcreatedateBetween(Date fDate,Date tDate);

	List<Protocoltemplateget> findByCreatedbyAndStatusAndLssitemasterAndSharewithteamAndCreatedateBetween(
			Integer createdby, Integer status, Integer site, Integer Sharewithteam, Date fDate, Date tDate);

	List<Protocoltemplateget> findByStatusAndLssitemasterAndCreatedateBetween(Integer status, Integer site, Date fDate,
			Date tDate);

	List<Protocoltemplateget> findByCreatedbyAndStatusAndLssitemasterAndLSprotocolworkflowNotAndCreatedateBetween(
			Integer createdby, Integer status, Integer site, LSprotocolworkflow lSprotocolworkflow, Date fDate,
			Date tDate);

	List<Protocoltemplateget> findByCreatedbyAndStatusAndLssitemasterAndCreatedateBetween(Integer createdby,
			Integer status, Integer site, Date fDate, Date tDate);

	List<Protocoltemplateget> findByCreatedbyNotAndStatusAndLssitemasterAndLSprotocolworkflowAndCreatedateBetween(
			Integer createdby, Integer status, Integer site, LSprotocolworkflow lSprotocolworkflow, Date fDate,
			Date tDate);

	List<Protocoltemplateget> findByCreatedbyAndStatusAndLssitemasterAndCreatedateBetweenOrderByProtocolmastercodeDesc(
			Integer createdby, Integer status, Integer site, Date fDate, Date tDate);

	List<Protocoltemplateget> findByCreatedbyNotAndStatusAndLssitemasterAndLSprotocolworkflowAndCreatedateBetweenOrderByProtocolmastercodeDesc(
			Integer createdby, Integer status, Integer site, LSprotocolworkflow lSprotocolworkflow, Date fDate,
			Date tDate);
	
	List<Protocoltemplateget> findByCreatedbyOrLSprotocolworkflowAndStatusAndLssitemasterAndCreatedateBetweenOrderByProtocolmastercodeDesc(
			Integer createdby,LSprotocolworkflow lSprotocolworkflow, Integer status, Integer site, Date fDate,
			Date tDate);

	List<Protocoltemplateget> findByCreatedbyAndStatusAndLssitemasterAndSharewithteamAndLSprotocolworkflowNotAndCreatedateBetween(
			Integer createdby, Integer status, Integer site, Integer Sharewithteam,
			LSprotocolworkflow lSprotocolworkflow, Date fDate, Date tDate);


	
	List<LSprotocolmaster> findByCreatedbyInAndLssitemaster(List<Integer> createdby, Integer site);

//	List<LSprotocolmaster> findByCreatedbyInAndLssitemasterOrderByCreatedateDesc(List<Integer> lstuser,
//			Integer sitecode);


	Long countByCreatedbyIn(List<Integer> lstuser);
	
	Long countByCreatedby(Integer usercode);
	
	List<LSprotocolmaster> findFirst20ByOrderByProtocolmastercodeDesc();
	
	List<LSprotocolmaster> findFirst20ByProtocolmastercodeLessThanOrderByProtocolmastercodeDesc(Integer protocolcode);
	
	List<LSprotocolmaster> findFirst20ByCreatedbyInOrderByProtocolmastercodeDesc(List<Integer> lstuser);
	
	List<LSprotocolmaster> findFirst20ByCreatedbyOrderByProtocolmastercodeDesc(Integer usercode);
	
	List<LSprotocolmaster> findFirst20ByProtocolmastercodeLessThanAndCreatedbyInOrderByProtocolmastercodeDesc(Integer protocolcode, List<Integer> lstuser);
	
	List<LSprotocolmaster> findFirst20ByProtocolmastercodeLessThanAndCreatedbyOrderByProtocolmastercodeDesc(Integer protocolcode, Integer lsuser);

	List<LSprotocolmaster> findByStatusAndApprovedAndLssitemaster(int i, int j, Integer sitecode);

	Long countByCreatedbyInAndStatusAndLssitemasterOrderByCreatedateDesc(List<Integer> lstuser, int i,
			Integer sitecode);

//	List<LSprotocolmaster> findByStatusAndLssitemasterAndlstestIn(int i, Integer sitecode,
//			List<LSprotocolmastertest> lsfiletest);

//	List<LSprotocolmaster> findByStatusAndlstestIn(int i, List<LSprotocolmastertest> lsfiletest);

	List<LSprotocolmaster> findByLstestInAndStatus(List<LSprotocolmastertest> lsfiletest,int i);

//	List<LSprotocolmaster> findByCreatedbyInAndStatusAndLssitemasterOrderByCreatedateDesc(Integer usercode, int i,
//			Integer sitecode);

	LSprotocolmaster findByDefaulttemplate(int i);

	List<LSprotocolmaster> findByProtocolmastercodeIn(List<Integer> listobjfilecode);

	List<LSprotocolmaster> findByProtocolmastercodeNotAndProtocolmasternameIgnoreCase(int protocolmastercode, String trim);

//	Object findByProtocolmastername(String trim);

//	List<Protocoltemplateget> findByCreatedbyInAndStatusAndCreatedateBetweenOrderByProtocolmastercodeDesc(
//			List<Integer> usercodelist, int i, Date fromdate, Date todate);


	List<LSprotocolmaster> findByStatusAndLssitemasterAndViewoptionOrCreatedbyAndStatusAndLssitemasterAndViewoptionOrCreatedbyInAndStatusAndLssitemasterAndViewoptionOrderByCreatedateDesc(
			int i, Integer sitecode, int j, Integer usercode, int k, Integer sitecode2, int l, List<Integer> lstuser,
			int m, Integer sitecode3, int n);

	List<LSprotocolmaster> findByStatusAndLssitemasterAndViewoptionOrCreatedbyAndStatusAndLssitemasterAndViewoptionOrderByCreatedateDesc(
			int i, Integer sitecode, int j, Integer usercode, int k, Integer sitecode2, int l);

	List<Protocoltemplateget> findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, Integer usercode, int k, Date fromdate2,
			Date todate2, int l, Pageable pageable);

	List<Protocoltemplateget> findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, Integer usercode, int k, Date fromdate2,
			Date todate2, int l, List<Integer> usercodelist, int m, Date fromdate3, Date todate3, int n, Pageable pageable);

	Long countByStatusAndLssitemasterAndCreatedateBetweenAndRejected(int i, Integer sitecode, Date fromdate,
			Date todate, int j);

	Long countByStatusAndLssitemasterAndCreatedateBetweenAndRejectedNotAndApproved(int i, Integer sitecode,
			Date fromdate, Date todate, int j, int k);

	Long countByStatusAndLssitemasterAndCreatedateBetweenAndRejectedNotAndApprovedNot(int i, Integer sitecode,
			Date fromdate, Date todate, int j, int k);

	long countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndRejectedOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndRejectedOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionAndRejectedOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, int k, Integer usercode, int l, Date fromdate2,
			Date todate2, int m, int n, List<Integer> usercodelist, int o, Date fromdate3, Date todate3, int p, int q);

	List<Protocoltemplateget> findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, int k, int l, Integer usercode, int m,
			Date fromdate2, Date todate2, int n, int o, int p, List<Integer> usercodelist, int q, Date fromdate3,
			Date todate3, int r, int s, int t);

	List<Protocoltemplateget> findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedNotOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedNotOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedNotOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, int k, int l, Integer usercode, int m,
			Date fromdate2, Date todate2, int n, int o, int p, List<Integer> usercodelist, int q, Date fromdate3,
			Date todate3, int r, int s, int t);

	long countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndRejectedOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndRejectedOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, int k, Integer usercode, int l, Date fromdate2,
			Date todate2, int m, int n);

	long countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, int k, int l, Integer usercode, int m,
			Date fromdate2, Date todate2, int n, int o, int p);

	List<Protocoltemplateget> findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedNotOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedNotOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, int k, int l, Integer usercode, int m,
			Date fromdate2, Date todate2, int n, int o, int p);

	long countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedIsNullOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedIsNullOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedIsNullOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, int k, Integer usercode, int l, Date fromdate2,
			Date todate2, int m, int n, List<Integer> usercodelist, int o, Date fromdate3, Date todate3, int p, int q);

	long countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedIsNullOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedIsNullOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedIsNullOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, Integer usercode, int k, Date fromdate2,
			Date todate2, int l, List<Integer> usercodelist, int m, Date fromdate3, Date todate3, int n);

	List<Protocoltemplateget> findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, int k, int l, Integer usercode, int m,
			Date fromdate2, Date todate2, int n, int o, int p, List<Integer> usercodelist, int q, Date fromdate3,
			Date todate3, int r, int s, int t);

	List<Protocoltemplateget> findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, int k, int l, Integer usercode, int m,
			Date fromdate2, Date todate2, int n, int o, int p, List<Integer> usercodelist, int q, Date fromdate3,
			Date todate3, int r, int s, int t);

	List<LSprotocolmaster> findByLstestInAndStatusAndCreatedbyInAndViewoptionOrLstestInAndStatusAndCreatedbyAndViewoptionOrLstestInAndStatusAndCreatedbyInAndViewoption(
			List<LSprotocolmastertest> lsfiletest, int i, List<Integer> lstteammap, int j,
			List<LSprotocolmastertest> lsfiletest2, int k, Integer usercode, int l,
			List<LSprotocolmastertest> lsfiletest3, int m, List<Integer> lstteammap2, int n);

	long countByStatusAndLssitemasterAndCreatedateBetweenAndRejectedNotAndApprovedIsNull(int i, Integer sitecode,
			Date fromdate, Date todate, int j);

	long countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedNotOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedNotOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, int k, Integer usercode, int l, Date fromdate2,
			Date todate2, int m, int n);

	List<Protocoltemplateget> findByLssitemasterAndStatusAndCreatedateBetween(Integer sitecode, int i, Date fromdate, Date todate,
			Pageable pageable);

	long countByLssitemasterAndStatusAndCreatedateBetween(Integer sitecode, int i, Date fromdate, Date todate);

	long countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, Integer usercode, int k, Date fromdate2,
			Date todate2, int l, List<Integer> usercodelist, int m, Date fromdate3, Date todate3, int n);

	long countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, Integer usercode, int k, Date fromdate2,
			Date todate2, int l);

	Object findByProtocolmasternameIgnoreCaseAndLssitemaster(String trim, Integer lssitemaster);

	List<LSprotocolmaster> findByProtocolmastercode(Integer protocolmastercode);

	long countByStatusAndLssitemasterAndViewoptionOrCreatedbyAndStatusAndLssitemasterAndViewoptionOrCreatedbyInAndStatusAndLssitemasterAndViewoptionOrderByCreatedateDesc(
			int i, Integer sitecode, int j, Integer usercode, int k, Integer sitecode2, int l, List<Integer> lstuser,
			int m, Integer sitecode3, int n);

	long countByStatusAndLssitemasterAndViewoptionOrCreatedbyAndStatusAndLssitemasterAndViewoptionOrderByCreatedateDesc(
			int i, Integer sitecode, int j, Integer usercode, int k, Integer sitecode2, int l);

	List<LSprotocolmaster> findByLstestInAndStatusAndCreatedbyInAndViewoptionAndApprovedOrLstestInAndStatusAndCreatedbyAndViewoptionAndApprovedOrLstestInAndStatusAndCreatedbyInAndViewoptionAndApproved(
			List<LSprotocolmastertest> lsfiletest, int i, List<Integer> lstteammap, int j, int k,
			List<LSprotocolmastertest> lsfiletest2, int l, Integer usercode, int m, int n,
			List<LSprotocolmastertest> lsfiletest3, int o, List<Integer> lstteammap2, int p, int q);

	List<LSprotocolmaster> findByLstestInAndStatusAndApproved(List<LSprotocolmastertest> lsfiletest, int i, int j);
}