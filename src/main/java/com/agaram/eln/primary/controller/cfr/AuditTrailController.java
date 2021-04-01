package com.agaram.eln.primary.controller.cfr;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.cfr.LSaudittrailconfiguration;
import com.agaram.eln.primary.model.cfr.LScfrreasons;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cfr.LSreviewdetails;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.service.cfr.AuditService;

@RestController
@RequestMapping(value = "/AuditTrail", method = RequestMethod.POST)
public class AuditTrailController {
	
	@Autowired
	private AuditService auditService;
	
	@GetMapping("/GetReasons")
	public List<LScfrreasons> getreasons(@RequestBody Map<String, Object> objMap) {
		return auditService.getreasons(objMap);
	}
	
	@GetMapping("/CFRTranUsername")
	public List<LSuserMaster> CFRTranUsername(HttpServletRequest request) {
		return auditService.CFRTranUsername();
	}
	
	@GetMapping(path = "/CFRTranModuleName")
	public List<String> CFRTranModuleName(HttpServletRequest request) {
		return auditService.CFRTranModuleName();
	}
	
	@PostMapping("/InsertupdateReasons")
	public LScfrreasons InsertupdateReasons(@RequestBody LScfrreasons objClass)
	{
		if(objClass.getObjuser() != null) {
			
			LSuserMaster userClass = auditService.CheckUserPassWord(objClass.getObjuser());
			
			if(userClass.getObjResponse().getStatus()) {
				
				objClass.setLSuserMaster(userClass);
				
				return auditService.InsertupdateReasons(objClass);
			}
			else
			{
				objClass.getObjsilentaudit().setComments("Entered invalid username and password");
				Map<String, Object> map=new HashMap<>();
				map.put("objsilentaudit",objClass.getObjsilentaudit());
				map.put("objmanualaudit",objClass.getObjmanualaudit());
				map.put("objUser",objClass.getObjuser());
				auditService.AuditConfigurationrecord(map);
				objClass.setResponse(new Response());
				objClass.getResponse().setStatus(false);
				objClass.getResponse().setInformation("ID_VALIDATION");
				return objClass;
			}
			
		}
		
		return auditService.InsertupdateReasons(objClass);
	}
	
	@PostMapping("/GetAuditconfigUser")
	public Map<String, Object> GetAuditconfigUser(@RequestBody LSaudittrailconfiguration LSaudittrailconfiguration)
	{
		return auditService.GetAuditconfigUser(LSaudittrailconfiguration);
	}
	
	@PostMapping("/SaveAuditconfigUser")
	public List<LSaudittrailconfiguration> SaveAuditconfigUser(@RequestBody List<LSaudittrailconfiguration> lsAudit)
	{
        if(lsAudit.get(0).getObjuser() != null) {
			
			LSuserMaster userClass = auditService.CheckUserPassWord(lsAudit.get(0).getObjuser());
			
			if(userClass.getObjResponse().getStatus()) {
				
				lsAudit.get(0).setLsuserMaster(userClass);
				
				return auditService.SaveAuditconfigUser(lsAudit);
			}
			else
			{
				lsAudit.get(0).getObjsilentauditlst().get(0).setComments("Entered invalid username and password");
				Map<String, Object> map=new HashMap<>();
				map.put("objsilentaudit",lsAudit.get(0).getObjsilentauditlst().get(0));
				map.put("objmanualaudit",lsAudit.get(0).getObjmanualaudit());
				map.put("objUser",lsAudit.get(0).getObjuser());
				auditService.AuditConfigurationrecord(map);
				lsAudit.get(0).setResponse(new Response());
				lsAudit.get(0).getResponse().setStatus(false);
				lsAudit.get(0).getResponse().setInformation("ID_VALIDATION");
				return lsAudit;
			}
			
		}
		return auditService.SaveAuditconfigUser(lsAudit);
	}

	@PostMapping("/GetCFRTransactions")
	public List<LScfttransaction> GetCFRTransactions(@RequestBody Map<String, Object> objCFRFilter) throws ParseException
	{
		return auditService.GetCFRTransactions(objCFRFilter);
	}
	
	@PostMapping("/CheckUserPassWord")
	public LSuserMaster CheckUserPassWord(@RequestBody LoggedUser objuser)
	{
		return auditService.CheckUserPassWord(objuser);
	}
	@PostMapping("/ReviewBtnValidation")
	public List<LSreviewdetails> ReviewBtnValidation(@RequestBody List<LSreviewdetails> objreview) {
		
		if(objreview.get(0).getObjuser() != null) {
			
			LSuserMaster userClass = auditService.CheckUserPassWord(objreview.get(0).getObjuser());
			
			if(userClass.getObjResponse().getStatus()) {
				
				objreview.get(0).setLsusermaster(userClass);
				
				return auditService.ReviewBtnValidation(objreview);
			}
			else
			{
				objreview.get(0).getObjsilentaudit().setComments("Entered invalid username and password");
				Map<String, Object> map=new HashMap<>();
				map.put("objsilentaudit",objreview.get(0).getObjsilentaudit());
				map.put("objmanualaudit",objreview.get(0).getObjmanualaudit());
				map.put("objUser",objreview.get(0).getObjuser());
				auditService.AuditConfigurationrecord(map);
				objreview.get(0).setResponse(new Response());
				objreview.get(0).getResponse().setStatus(false);
				objreview.get(0).getResponse().setInformation("ID_VALIDATION");
				return objreview;
			}
			
		}
		
		return auditService.ReviewBtnValidation(objreview);
}

	@PostMapping("/GetReviewDetails")
	public List<LSreviewdetails> GetReviewDetails(@RequestBody List<LSreviewdetails> objreviewdetails) {
	
		if(objreviewdetails.get(0).getObjuser() != null) {
					
					LSuserMaster userClass = auditService.CheckUserPassWord(objreviewdetails.get(0).getObjuser());
					
					if(userClass.getObjResponse().getStatus()) {
						
						objreviewdetails.get(0).setLsusermaster(userClass);
						
						return auditService.GetReviewDetails(objreviewdetails);
					}
					
					
				}
				
				return auditService.GetReviewDetails(objreviewdetails);
		}
	

	
	@PostMapping("/GetReviewDetails12")
	public Map<String, Object> GetReviewDetails12(@RequestBody List<LSreviewdetails> objreviewdetails)
	{
		Response objResponse = new Response();
		Map<String, Object> objreview = new HashMap<String, Object>();
		if(objreviewdetails.get(0).getObjuser() != null) {
			
			LSuserMaster userClass = auditService.CheckUserPassWord(objreviewdetails.get(0).getObjuser());
			
			if(userClass.getObjResponse().getStatus()) {
				
				objreviewdetails.get(0).setLsusermaster(userClass);
				objreview.put("transaction", auditService.GetReviewDetails12(objreviewdetails));
				objResponse.setStatus(true);
				objreview.put("objResponse",objResponse);
				return objreview;
			}
			
			else
			{
				
				objreviewdetails.get(0).getObjsilentaudit().setComments("Entered invalid username and password");
				Map<String, Object> map=new HashMap<>();
			  	map.put("objsilentaudit",objreviewdetails.get(0).getObjsilentaudit());
			  	map.put("objmanualaudit",objreviewdetails.get(0).getObjmanualaudit());
			    map.put("objUser",objreviewdetails.get(0).getObjuser());
		     	auditService.AuditConfigurationrecord(map);
				objResponse.setStatus(false);
				objResponse.setInformation("ID_VALIDATION");
				objreview.put("objResponse",objResponse);
				return objreview;
			}
			
		}
		
		objreview.put("transaction", auditService.GetReviewDetails12(objreviewdetails));
		objResponse.setStatus(true);
		objreview.put("objResponse",objResponse);
		return objreview;
	}

	@PostMapping("/exportDataFile")
	public  Map<String, Object>  exportDataFile(@RequestBody LSuserMaster objuser) throws Exception {
		
		if(objuser.getObjuser() != null) {
				
				LSuserMaster userClass = auditService.CheckUserPassWord(objuser.getObjuser());
				
				if(userClass.getObjResponse().getStatus()) {
					
					return auditService.exportData(objuser);
				}
				else
				{
					objuser.getObjsilentaudit().setComments("Entered invalid username and password");
					Map<String, Object> map=new HashMap<>();
				  	map.put("objsilentaudit",objuser.getObjsilentaudit());
				  	map.put("objmanualaudit",objuser.getObjmanualaudit());
				    map.put("objUser",objuser.getObjuser());
			     	auditService.AuditConfigurationrecord(map);
					objuser.setObjResponse(new Response());
					objuser.getObjResponse().setStatus(false);
					objuser.getObjResponse().setInformation("ID_VALIDATION");
					map.put("cfttDeatils",objuser);
					return map;
				}
				
			}
		return auditService.exportData(objuser);
	}
	@PostMapping("/AuditConfigurationrecord")
	public LScfttransaction AuditConfigurationrecord(@RequestBody Map<String, Object> objmanualaudit) throws ParseException
	{
		return auditService.AuditConfigurationrecord(objmanualaudit);
	}
}
