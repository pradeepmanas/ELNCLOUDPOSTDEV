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
import com.agaram.eln.primary.model.protocols.ElnprotocolTemplateworkflow;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolmastertest;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

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
	@Query("update LSprotocolmaster set elnprotocoltemplateworkflow = :workflow, approved= :approved , rejected= :rejected "
			+ "where protocolmastercode in (:protocolmastercode)")
	public void updateFileWorkflow(@Param("workflow") ElnprotocolTemplateworkflow lSsheetworkflow,
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

	List<LSprotocolmaster> findByStatusAndApprovedAndLssitemaster(int i, int j, LSSiteMaster sitecode);

	List<LSprotocolmaster> findByStatusAndApprovedAndLssitemasterAndProtocolmastercodeIn(int i, int j, Integer sitecode,
			List<Integer> lstprotocoltemp);

	List<LSprotocolmaster> findByprotocolmastercodeIn(List<Integer> lstprotocoltemp);

	List<LSprotocolmaster> findByprotocolmasternameIn(List<String> protocolList);

	LSprotocolmaster findFirstByProtocolmastercodeAndCreatedateBetween(Integer protocolmastercode, Date fromdate,
			Date todate);

	long countByElnprotocoltemplateworkflowAndApproved(ElnprotocolTemplateworkflow objflow, int i);

	List<LSprotocolmaster> findByLstestInAndStatusAndApprovedOrderByProtocolmastercodeDesc(
			List<LSprotocolmastertest> lsfiletest, int i, int j);

	List<LSprotocolmaster> findByLstestInAndStatusAndCreatedbyInAndViewoptionAndApprovedOrLstestInAndStatusAndCreatedbyAndViewoptionAndApprovedOrLstestInAndStatusAndCreatedbyInAndViewoptionAndApprovedOrderByProtocolmastercodeDesc(
			List<LSprotocolmastertest> lsfiletest, int i, List<Integer> lstteammap, int j, int k,
			List<LSprotocolmastertest> lsfiletest2, int l, Integer usercode, int m, int n,
			List<LSprotocolmastertest> lsfiletest3, int o, List<Integer> lstteammap2, int p, int q);

	List<LSprotocolmaster> findByLssitemasterAndLstestInAndStatusAndViewoptionAndApprovedOrLstestInAndStatusAndCreatedbyInAndViewoptionAndApprovedOrLstestInAndStatusAndCreatedbyAndViewoptionAndApprovedOrLstestInAndStatusAndCreatedbyInAndViewoptionAndApprovedOrderByProtocolmastercodeDesc(
			Integer sitecode, List<LSprotocolmastertest> lsfiletest, int i, int j, int k,
			List<LSprotocolmastertest> lsfiletest2, int l, List<Integer> lstteammap, int m, int n,
			List<LSprotocolmastertest> lsfiletest3, int o, Integer usercode, int p, int q,
			List<LSprotocolmastertest> lsfiletest4, int r, List<Integer> lstteammap2, int s, int t);
	
	long countByElnprotocoltemplateworkflowAndApprovedOrApprovedIsNull(ElnprotocolTemplateworkflow objflow, int i);
	
	@Transactional@Modifying
	@Query(value="select protocolmastercode from LSprotocolmaster where protocolmastername=?1",nativeQuery=true)
	public List<Integer>  getprotocolcode(String protocolmastername);List<LSprotocolmaster> findByStatusAndApprovedAndLssitemasterAndRetirestatus(int i, int j, Integer sitecode, int k);List<LSprotocolmaster> findByLstestInAndStatusAndRetirestatusAndApprovedOrderByProtocolmastercodeDesc(
			List<LSprotocolmastertest> lsfiletest, int i, int j, int k);

	List<LSprotocolmaster> findByLstestInAndStatusAndCreatedbyInAndViewoptionAndRetirestatusAndApprovedOrLstestInAndStatusAndCreatedbyAndViewoptionAndRetirestatusAndApprovedOrLstestInAndStatusAndCreatedbyInAndViewoptionAndRetirestatusAndApprovedOrderByProtocolmastercodeDesc(
			List<LSprotocolmastertest> lsfiletest, int i, List<Integer> lstteammap, int j, int k, int l,
			List<LSprotocolmastertest> lsfiletest2, int m, Integer usercode, int n, int o, int p,
			List<LSprotocolmastertest> lsfiletest3, int q, List<Integer> lstteammap2, int r, int s, int t);

//	List<LSprotocolmaster> findByLstestInAndStatusAndCreatedbyInAndViewoptionAndRetirestatusAndApprovedOrLstestInAndStatusAndCreatedbyAndViewoptionAndRetirestatusAndApprovedOrLstestInAndStatusAndCreatedbyInAndViewoptionAndRetirestatusAndApprovedOrderByProtocolmastercodeDesc(
//			List<LSprotocolmastertest> lsfiletest, int i, List<Integer> lstteammap, int j, int k,
//			List<LSprotocolmastertest> lsfiletest2, int l, int m, Integer usercode, int n, int o, int p,
//			List<LSprotocolmastertest> lsfiletest3, int q, List<Integer> lstteammap2, int r, int s, int t);

	List<LSprotocolmaster> findByProtocolmastercodeNotAndRetirestatusAndProtocolmasternameIgnoreCase(
			int protocolmastercode, int i, String pname);

	List<LSprotocolmaster> findByRetirestatusAndProtocolmasternameIgnoreCaseAndLssitemaster(int i, String string,
			Integer lssitemaster);

	List<LSprotocolmaster> findByStatusAndLssitemasterAndProtocolmasternameAndRetirestatus(int i, Integer lssitemaster,
			String protocolmastername, int j);

	long countByStatusAndLssitemasterAndCreatedateBetweenAndRetirestatusAndRejected(int i, Integer sitecode,
			Date fromdate, Date todate, int j, int k);

	long countByStatusAndLssitemasterAndCreatedateBetweenAndRetirestatusAndRejectedNotAndApproved(int i,
			Integer sitecode, Date fromdate, Date todate, int j, int k, int l);

	long countByStatusAndLssitemasterAndCreatedateBetweenAndRetirestatusAndRejectedNotAndApprovedIsNull(int i,
			Integer sitecode, Date fromdate, Date todate, int j, int k);

	long countByStatusAndLssitemasterAndCreatedateBetweenAndRetirestatus(int i, Integer sitecode, Date fromdate,
			Date todate, int j);

	long countByLssitemasterAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrCreatedbyAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrCreatedbyInAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, int k, int l, Integer usercode, int m,
			Date fromdate2, Date todate2, int n, int o, int p, List<Integer> usercodelist, int q, Date fromdate3,
			Date todate3, int r, int s, int t);

	long countByLssitemasterAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedIsNullOrCreatedbyAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedIsNullOrCreatedbyInAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedIsNullOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, int k, int l, Integer usercode, int m,
			Date fromdate2, Date todate2, int n, int o, int p, List<Integer> usercodelist, int q, Date fromdate3,
			Date todate3, int r, int s, int t);

	long countByLssitemasterAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedIsNullOrCreatedbyAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedIsNullOrCreatedbyInAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedIsNullOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, int k, Integer usercode, int l, Date fromdate2,
			Date todate2, int m, int n, List<Integer> usercodelist, int o, Date fromdate3, Date todate3, int p, int q);

	long countByLssitemasterAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionOrCreatedbyInAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, int k, Integer usercode, int l, Date fromdate2,
			Date todate2, int m, int n, List<Integer> usercodelist, int o, Date fromdate3, Date todate3, int p, int q);

	long countByLssitemasterAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrCreatedbyAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, int k, int l, Integer usercode, int m,
			Date fromdate2, Date todate2, int n, int o, int p);

	long countByLssitemasterAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrCreatedbyAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, int k, int l, int m, Integer usercode, int n,
			Date fromdate2, Date todate2, int o, int p, int q, int r);

	long countByLssitemasterAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedNotOrCreatedbyAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedNotOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, int k, int l, Integer usercode, int m,
			Date fromdate2, Date todate2, int n, int o, int p);

	long countByLssitemasterAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, int k, int l, Integer usercode, int m,
			Date fromdate2, Date todate2, int n, int o, int p);
	

	List<Protocoltemplateget> findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndLssitemasterOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionAndLssitemasterOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, Integer usercode, int k, Date fromdate2,
			Date todate2, int l, Integer sitecode2, List<Integer> usercodelist, int m, Date fromdate3, Date todate3,
			int n, Integer sitecode3, Pageable pageable);

	long countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndLssitemasterOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionAndLssitemasterOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, Integer usercode, int k, Date fromdate2,
			Date todate2, int l, Integer sitecode2, List<Integer> usercodelist, int m, Date fromdate3, Date todate3,
			int n, Integer sitecode3);
	
	List<Protocoltemplateget> findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndLssitemasterOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, Integer usercode, int k, Date fromdate2,
			Date todate2, int l, Integer sitecode2, Pageable pageable);

	long countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndLssitemasterOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, Date fromdate, Date todate, int j, Integer usercode, int k, Date fromdate2,
			Date todate2, int l, Integer sitecode2);
	
	List<Protocoltemplateget> findByLssitemasterAndStatusAndProtocolmasternameLike(Integer sitecode, int i, String search_Key,
			Pageable pageable);

	long countByLssitemasterAndStatusAndProtocolmasternameLike(Integer sitecode, int i, String search_Key);

	List<Protocoltemplateget> findByLssitemasterAndStatusAndViewoptionAndProtocolmasternameLikeOrCreatedbyAndStatusAndViewoptionAndLssitemasterAndProtocolmasternameLikeOrCreatedbyInAndStatusAndViewoptionAndLssitemasterAndProtocolmasternameLikeOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, int j, String search_Key, Integer usercode, int k, int l, Integer sitecode2,
			String search_Key2, List<Integer> usercodelist, int m, int n, Integer sitecode3, String search_Key3,
			Pageable pageable);

	long countByLssitemasterAndStatusAndViewoptionAndProtocolmasternameLikeOrCreatedbyAndStatusAndViewoptionAndLssitemasterAndProtocolmasternameLikeOrCreatedbyInAndStatusAndViewoptionAndLssitemasterAndProtocolmasternameLikeOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, int j, String search_Key, Integer usercode, int k, int l, Integer sitecode2,
			String search_Key2, List<Integer> usercodelist, int m, int n, Integer sitecode3, String search_Key3
			);
	
	List<Protocoltemplateget> findByLssitemasterAndStatusAndViewoptionAndProtocolmasternameLikeOrCreatedbyAndStatusAndViewoptionAndLssitemasterAndProtocolmasternameLikeOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, int j, String search_Key, Integer usercode, int k, int l, Integer sitecode2,
			String search_Key2, Pageable pageable);

	long countByLssitemasterAndStatusAndViewoptionAndProtocolmasternameLikeOrCreatedbyAndStatusAndViewoptionAndLssitemasterAndProtocolmasternameLikeOrderByProtocolmastercodeDesc(
			Integer sitecode, int i, int j, String search_Key, Integer usercode, int k, int l, Integer sitecode2,
			String search_Key2);
	
	long countByLssitemaster(Integer sitecode);
	@Transactional
	@Query(value = "select retirestatus from lsprotocolmaster where protocolmastername = ?1", nativeQuery = true)
	String getRetirestatus(String templatename);
	
	List<LSprotocolmaster> findByCreatedbyInAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrCreatedbyAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrCreatedbyInAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrderByProtocolmastercodeDesc(
			List<Integer> lstteamuser, List<LSprotocolmastertest> lsfiletest, int i, int j, int k, int l,
			Integer objLoggeduser, List<LSprotocolmastertest> lsfiletest2, int m, int n, int o, int p,
			List<Integer> lstteamuser2, List<LSprotocolmastertest> lsfiletest3, int q, int r, int s, int t);

	List<LSprotocolmaster> findByCreatedbyInAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatedbyAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatedbyInAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrderByProtocolmastercodeDesc(
			List<Integer> lstteamuser, List<LSprotocolmastertest> lsfiletest, int i, int j, int k, int l, int m,
			Integer objLoggeduser, List<LSprotocolmastertest> lsfiletest2, int n, int o, int p, int q, int r,
			List<Integer> lstteamuser2, List<LSprotocolmastertest> lsfiletest3, int s, int t, int u, int v, int w);
	
	List<LSprotocolmaster> findByLssitemasterAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrCreatedbyInAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrCreatedbyAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrCreatedbyInAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrderByProtocolmastercodeDesc(
			Integer sitecode, List<LSprotocolmastertest> lsfiletest, int i, int j, int k, int l,
			List<Integer> lstteammap, List<LSprotocolmastertest> lsfiletest2, int m, int n, int o, int p,
			Integer usercode, List<LSprotocolmastertest> lsfiletest3, int q, int r, int s, int t,
			List<Integer> lstteammap2, List<LSprotocolmastertest> lsfiletest4, int u, int v, int w, int x);

	List<LSprotocolmaster> findByLssitemasterAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatedbyInAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatedbyAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatedbyInAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrderByProtocolmastercodeDesc(
			Integer sitecode, List<LSprotocolmastertest> lsfiletest, int i, int j, int k, int l, int m,
			List<Integer> lstteammap, List<LSprotocolmastertest> lsfiletest2, int n, int o, int p, int q, int r,
			Integer usercode, List<LSprotocolmastertest> lsfiletest3, int s, int t, int u, int v, int w,
			List<Integer> lstteammap2, List<LSprotocolmastertest> lsfiletest4, int x, int y, int z, int a, int b);
}