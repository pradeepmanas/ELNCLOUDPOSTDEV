package com.agaram.eln.primary.controller.usermanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.config.ADS_Connection;
import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSdomainMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.service.cfr.AuditService;
import com.agaram.eln.primary.service.usermanagement.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/Login", method = RequestMethod.POST)
public class LoginController {

	@Autowired
    private LoginService loginService;
	
	@Autowired
	private AuditService auditService;
	
	@GetMapping("/LoadSite")
	public List<LSSiteMaster> loadSite(HttpServletRequest request) {
		return loginService.loadSite();
	}
	
	
	
	@GetMapping("/LoadSiteMaster")
	public List<LSSiteMaster> LoadSiteMaster(HttpServletRequest request) {
		return loginService.LoadSiteMaster();
	}
	
	@PostMapping("/LoadDomain")
	public List<LSdomainMaster> LoadDomain(@RequestBody LSSiteMaster objsite) {
		return loginService.loadDomain(objsite);
	}
	
	@PostMapping("/Login")
	public Map<String, Object> Login(@RequestBody LoggedUser objuser) throws Exception {
		return loginService.Login(objuser);
	}
	
	@PostMapping("/CheckUserAndPassword")
	public LSuserMaster CheckUserAndPassword(@RequestBody LoggedUser objuser) {
		return loginService.CheckUserAndPassword(objuser);
	}
	
	@PostMapping("/UpdatePassword")
	public LSuserMaster UpdatePassword(@RequestBody LoggedUser objuser) {
		return loginService.UpdatePassword(objuser);
	}
	@PostMapping("/Logout")
	public Boolean Logout(@RequestBody LSuserMaster lsuserMaster)
	{
		return loginService.Logout(lsuserMaster);
	}
	
	@PostMapping("/ChangePassword")
	public LSuserMaster ChangePassword(@RequestBody LoggedUser objuser) {
//		LSuserMaster userClass=new LSuserMaster();
//            if(objuser.getObjuser() != null) {
//			
//			 userClass = auditService.CheckUserPassWord(objuser.getObjuser());
//			
//			if(userClass.getObjResponse().getStatus()) {
//				
//				objuser.setLsusermaster(userClass);
//				
//				return loginService.ChangePassword(objuser);
//			}
//			else
//			{
//				Map<String, Object> map=new HashMap<>();
//				objuser.getObjsilentaudit().setComments("Entered invalid username and password");
//				map.put("objsilentaudit",objuser.getObjsilentaudit());
//				map.put("objmanualaudit",objuser.getObjmanualaudit());
//				map.put("objUser",objuser.getObjuser());
//				auditService.AuditConfigurationrecord(map);
//				userClass.setObjResponse(new Response());
//				userClass.getObjResponse().setStatus(false);
//				userClass.getObjResponse().setInformation("ID_VALIDATION");
//				return userClass;
//			}
//			
//		}
		return loginService.ChangePassword(objuser);
	}
	
	@PostMapping("/InsertUpdateDomain")
	public LSdomainMaster InsertupdateDomain(@RequestBody LSdomainMaster objClass)
	{
//		if(objClass.getObjuser() != null) {
//			
//			LSuserMaster userClass = auditService.CheckUserPassWord(objClass.getObjuser());
//			
//			if(userClass.getObjResponse().getStatus()) {
//				
//				objClass.setLSuserMaster(userClass);
//				
//				return loginService.InsertupdateDomain(objClass);
//			}
//			else
//			{
//				Map<String, Object> map=new HashMap<>();
//				objClass.getObjsilentaudit().setComments("Entered invalid username and password");
//				map.put("objsilentaudit",objClass.getObjsilentaudit());
//				map.put("objmanualaudit",objClass.getObjmanualaudit());
//				map.put("objUser",objClass.getObjuser());
//				auditService.AuditConfigurationrecord(map);
//				objClass.setResponse(new Response());
//				objClass.getResponse().setStatus(false);
//				objClass.getResponse().setInformation("ID_VALIDATION");
//				return objClass;
//			}
//			
//		}
		return loginService.InsertupdateDomain(objClass);
	}
	
	@PostMapping("/importADSScreen")
	public LSuserMaster importADSScreen(@RequestBody LSuserMaster objClass)
	{
//		if(objClass.getObjuser() != null) {
//			
//			LSuserMaster userClass = auditService.CheckUserPassWord(objClass.getObjuser());
//			
//			if(userClass.getObjResponse().getStatus()) {
//				
//				return loginService.importADSScreen(objClass);
//			}
//			else
//			{
//				objClass.setResponse(new Response());
//				objClass.getResponse().setStatus(false);
//				objClass.getResponse().setInformation("Username And PassWord invalid");
//				return objClass;
//			}
//			
//		}
		return loginService.importADSScreen(objClass);
	}
	
	@PostMapping(path = "/ADSDomainServerConnection")
	public Response adsDomainServerConnection(@RequestBody Map<String, Object> objMap) {
		LoggedUser objuser = new LoggedUser();
		Response objResponse = new Response();
		ObjectMapper mapper = new ObjectMapper();
		LScfttransaction objsilentaudit = new LScfttransaction();
		LScfttransaction objmanualaudit = new LScfttransaction();
		if(objMap.containsKey("objuser"))
		{
			objuser = mapper.convertValue(objMap.get("objuser"),LoggedUser.class);
		}
		
		if(objMap.containsKey("objsilentaudit"))
		{
			objsilentaudit = mapper.convertValue(objMap.get("objsilentaudit"),LScfttransaction.class);
		}
		if(objMap.containsKey("objmanualaudit"))
		{
			objmanualaudit = mapper.convertValue(objMap.get("objmanualaudit"),LScfttransaction.class);
		}
		
		if(objuser.getsUsername() != null) {
			
			LSuserMaster userClass = auditService.CheckUserPassWord(objuser);
			
			if(userClass.getObjResponse().getStatus()) {
				return loginService.ADSDomainServerConnection(objMap);	
			}
			else
			{
				objsilentaudit.setComments("Entered invalid username and password");
				Map<String, Object> map=new HashMap<>();
				map.put("objsilentaudit",objsilentaudit);
				map.put("objmanualaudit",objmanualaudit);
				map.put("objUser",objuser);
				auditService.AuditConfigurationrecord(map);
				objResponse.setStatus(false);
				objResponse.setInformation("ID_VALIDATION");
				return objResponse;
			}
			
		}
		return loginService.ADSDomainServerConnection(objMap);	
	}
	
	@RequestMapping(value = "/importADSGroups")
	Map<String, Object> importADSGroups(@RequestBody Map<String, Object> objMap) {
		Map<String, Object> rtnImportAdS = new HashMap<>();
		
		rtnImportAdS.putAll(ADS_Connection.importADSGroups(objMap));

		return rtnImportAdS;
	}
	
	@RequestMapping(value = "/importADSUsersByGroup")
	Map<String, List<Map<String, String>>> importADSUsers(@RequestBody Map<String, Object> objMap) {
		Map<String, List<Map<String, String>>> rtnImportAdS = new HashMap<>();
		
		rtnImportAdS.putAll(ADS_Connection.importADSUsersByGroup(objMap));
		
		return rtnImportAdS;
	}
	
	@RequestMapping(value = "/addImportADSUsers")
	public Map<String, Object>  addImportADSUsers(@RequestBody Map<String, Object> objMap) {
		
		Map<String, Object> rtnMap=new HashMap<>();
		Map<String, Object> isCompleted =new HashMap<>();
		LoggedUser objuser = new LoggedUser();
		Response objResponse = new Response();
		ObjectMapper mapper = new ObjectMapper();
		LScfttransaction objsilentaudit = new LScfttransaction();
		LScfttransaction objmanualaudit = new LScfttransaction();
		
		if(objMap.containsKey("objuser"))
		{
			objuser = mapper.convertValue(objMap.get("objuser"),LoggedUser.class);
		}
		
		if(objMap.containsKey("objsilentaudit"))
		{
			objsilentaudit = mapper.convertValue(objMap.get("objsilentaudit"),LScfttransaction.class);
		}
		if(objMap.containsKey("objmanualaudit"))
		{
			objmanualaudit = mapper.convertValue(objMap.get("objmanualaudit"),LScfttransaction.class);
		}
		
		if(objuser.getsUsername() != null) {
			
			LSuserMaster userClass = auditService.CheckUserPassWord(objuser);
			
			if(userClass.getObjResponse().getStatus()) {
				
				 isCompleted=loginService.addImportADSUsers(objMap);
				
				if (isCompleted.get("isCompleted").equals(true)) {
					List<LSuserMaster> lstUsers=new ArrayList<>();
					
					LSusergroup userGroup = (LSusergroup) isCompleted.get("LSusergroup");
					LSSiteMaster sSiteCode = (LSSiteMaster) isCompleted.get("LSSiteMaster");
					
					lstUsers = loginService.UserMasterDetails(userGroup,sSiteCode);
					
					rtnMap.put("LSuserMaster",lstUsers);
					rtnMap.put("status",true);
					rtnMap.put("sinformation","Users imported successfully");
				}
				else {
					rtnMap.put("status",false);
					rtnMap.put("sinformation","Imported users are not saved");
				}
			}
			else
			{
				objsilentaudit.setComments("Entered invalid username and password");
				Map<String, Object> map=new HashMap<>();
				map.put("objsilentaudit",objsilentaudit);
				map.put("objmanualaudit",objmanualaudit);
				map.put("objUser",objuser);
				auditService.AuditConfigurationrecord(map);
				
				objResponse.setStatus(false);
				objResponse.setInformation("ID_VALIDATION");
				map.put("objResponse",objResponse);
				return map;
			}
			
		}
		else {
			isCompleted=loginService.addImportADSUsers(objMap);
		
		if (isCompleted.get("isCompleted").equals(true)) {
			List<LSuserMaster> lstUsers=new ArrayList<>();
			
			LSusergroup userGroup = (LSusergroup) isCompleted.get("LSusergroup");
			LSSiteMaster sSiteCode = (LSSiteMaster) isCompleted.get("LSSiteMaster");
			
			lstUsers = loginService.UserMasterDetails(userGroup,sSiteCode);
			
			rtnMap.put("LSuserMaster",lstUsers);
			rtnMap.put("status",true);
			rtnMap.put("sinformation","Users imported successfully");
		}
		else {
			rtnMap.put("status",false);
			rtnMap.put("sinformation","Imported users are not saved");
		}
		}
		return rtnMap;
	}
	
	@RequestMapping(path = "/ADSServerDomainCombo")
	public Map<String, Object> adsServerDomainCombo(@RequestBody LSuserMaster Objclass) {
		
		Map<String, Object> objrtnMap = new HashMap<>();

		objrtnMap = loginService.ADSServerDomainCombo(Objclass);

		return objrtnMap;
	}
	
	@PostMapping("/LoadDomainMaster")
	public List<LSdomainMaster> LoadDomainMaster(@RequestBody LSSiteMaster objsite) {
		return loginService.LoadDomainMaster(objsite);
	}
	@PostMapping("/LoadDomainMasterAdmin")
	public List<LSdomainMaster> LoadDomainMasterAdmin(@RequestBody LSSiteMaster objsite) {
		return loginService.LoadDomainMasterAdmin(objsite);
	}
	
	@PostMapping("/Validateuser")
	public LSuserMaster Validateuser(@RequestBody LSuserMaster objClass)
	{
		return loginService.validateuser(objClass);
	}
	
	@PostMapping("/LinkLogin")
	public LSuserMaster LinkLogin(@RequestBody LSuserMaster objClass)
	{
		return loginService.LinkLogin(objClass);
	}

	@PostMapping("/InsertupdateSite")
	public LSSiteMaster InsertupdateSite(@RequestBody LSSiteMaster objClass)
	{
		return loginService.InsertupdateSite(objClass);
	}
	
	@RequestMapping(value = "/azureusertokengenrate", method = RequestMethod.POST)
	public ResponseEntity<?> azureusertokengenrate(@RequestBody LSuserMaster objClass) throws Exception {
		return loginService.azureusertokengenrate(objClass);
	}
	
	@PostMapping("/azureauthenticatelogin")
	public Map<String, Object> azureauthenticatelogin(@RequestBody LoggedUser objClass)
	{
		return loginService.azureauthenticatelogin(objClass);
	}
	
	@PostMapping("/createuserforazure")
	public LSuserMaster createuserforazure(@RequestBody LSuserMaster objClass)
	{
		return loginService.createuserforazure(objClass);
	}
	
	@RequestMapping(value = "/limsloginusertokengenarate", method = RequestMethod.POST)
	public ResponseEntity<?> limsloginusertokengenarate(@RequestBody LSuserMaster objClass) throws Exception {
		return loginService.limsloginusertokengenarate(objClass);
	}
	
	
	@PostMapping("/Switchusergroup")
	public Map<String, Object> Switchusergroup(@RequestBody LSuserMaster lsuserMaster) throws Exception {
		return loginService.Switchusergroup(lsuserMaster);
	}
	
	@PostMapping("/serverDateFormat")
	public Map<String, Object> serverDateFormat(@RequestBody LSuserMaster lsuserMaster) throws Exception {
		
		Map<String, Object> rMap = new HashMap<>();
		
		rMap.put("serverDateFormat", commonfunction.getServerDateFormat());
		
		return rMap;
	}
}
