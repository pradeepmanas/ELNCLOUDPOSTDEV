package com.agaram.eln.primary.controller.usermanagement;

import java.text.ParseException;
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
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.sheetManipulation.Notification;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSdomainMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.service.usermanagement.LoginService;

@RestController
@RequestMapping(value = "/Login", method = RequestMethod.POST)
public class LoginController {

	@Autowired
	private LoginService loginService;


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
	public Boolean Logout(@RequestBody LSuserMaster lsuserMaster) {
		return loginService.Logout(lsuserMaster);
	}

	@PostMapping("/ChangePassword")
	public LSuserMaster ChangePassword(@RequestBody LoggedUser objuser) {
		return loginService.ChangePassword(objuser);
	}

	@PostMapping("/InsertUpdateDomain")
	public LSdomainMaster InsertupdateDomain(@RequestBody LSdomainMaster objClass) {
		return loginService.InsertupdateDomain(objClass);
	}

	@PostMapping("/importADSScreen")
	public LSuserMaster importADSScreen(@RequestBody LSuserMaster objClass) {
		return loginService.importADSScreen(objClass);
	}

	@PostMapping(path = "/ADSDomainServerConnection")
	public Response adsDomainServerConnection(@RequestBody Map<String, Object> objMap) {
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
	public Map<String, Object> addImportADSUsers(@RequestBody Map<String, Object> objMap) {

		Map<String, Object> rtnMap = new HashMap<>();
		Map<String, Object> isCompleted = new HashMap<>();

		isCompleted = loginService.addImportADSUsers(objMap);

		if (isCompleted.get("isCompleted").equals(true)) {
			List<LSuserMaster> lstUsers = new ArrayList<>();

			LSusergroup userGroup = (LSusergroup) isCompleted.get("LSusergroup");
			LSSiteMaster sSiteCode = (LSSiteMaster) isCompleted.get("LSSiteMaster");

			lstUsers = loginService.UserMasterDetails(userGroup, sSiteCode);

			rtnMap.put("LSuserMaster", lstUsers);
			rtnMap.put("status", true);
			rtnMap.put("sinformation", "Users imported successfully");
		} else {
			rtnMap.put("status", false);
			rtnMap.put("sinformation", "Imported users are not saved");
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
	public LSuserMaster Validateuser(@RequestBody LSuserMaster objClass) {
		return loginService.validateuser(objClass);
	}

	@PostMapping("/LinkLogin")
	public LSuserMaster LinkLogin(@RequestBody LSuserMaster objClass) {
		return loginService.LinkLogin(objClass);
	}

	@PostMapping("/InsertupdateSite")
	public LSSiteMaster InsertupdateSite(@RequestBody LSSiteMaster objClass) {
		return loginService.InsertupdateSite(objClass);
	}

	@RequestMapping(value = "/azureusertokengenrate", method = RequestMethod.POST)
	public ResponseEntity<?> azureusertokengenrate(@RequestBody LSuserMaster objClass) throws Exception {
		return loginService.azureusertokengenrate(objClass);
	}

	@PostMapping("/azureauthenticatelogin")
	public Map<String, Object> azureauthenticatelogin(@RequestBody LoggedUser objClass) {
		return loginService.azureauthenticatelogin(objClass);
	}

	@PostMapping("/createuserforazure")
	public LSuserMaster createuserforazure(@RequestBody LSuserMaster objClass) {
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

	// added for notification
	@PostMapping("/Loginnotification")
	public Notification Loginnotification(@RequestBody Notification objNotification) throws ParseException {
		return loginService.Loginnotification(objNotification);
	}
	// added for notification
}
