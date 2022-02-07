package com.agaram.eln.primary.repository.protocol;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agaram.eln.primary.fetchmodel.gettemplate.Protocoltemplateget;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolmastertest;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflow;

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

	List<LSprotocolmaster> findAll();

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
	@Query("update LSprotocolmaster set lSprotocolworkflow = :workflow, approved= :approved , rejected= :rejected "
			+ "where protocolmastercode in (:protocolmastercode)")
	public void updateFileWorkflow(@Param("workflow") LSprotocolworkflow lsprotocolworkflow,
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

	List<Protocoltemplateget> findByCreatedbyOrLSprotocolworkflowAndStatusAndCreatedateBetweenOrderByProtocolmastercodeDesc(
			Integer usercode, LSprotocolworkflow lsprotocolworkflow, int i, Date fromdate, Date todate);
	
	List<LSprotocolmaster> findByCreatedbyInAndLssitemaster(List<Integer> createdby, Integer site);

//	List<LSprotocolmaster> findByCreatedbyInAndLssitemasterOrderByCreatedateDesc(List<Integer> lstuser,
//			Integer sitecode);

	List<LSprotocolmaster> findByCreatedbyInAndStatusAndLssitemasterOrderByCreatedateDesc(List<Integer> lstuser, int i,
			Integer sitecode);
	
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

	List<LSprotocolmaster> findByCreatedbyInAndStatusAndLssitemasterOrderByCreatedateDesc(Integer usercode, int i,
			Integer sitecode);

	LSprotocolmaster findByDefaulttemplate(int i);
}
