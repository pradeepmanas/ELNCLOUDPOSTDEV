package com.agaram.eln.primary.controller.usermanagement;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.agaram.eln.config.ADS_Connection;
import com.agaram.eln.config.AESEncryption;
import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.model.cfr.LSpreferences;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.multitenant.DataSourceConfig;
import com.agaram.eln.primary.model.sheetManipulation.Notification;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSdomainMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.repository.cfr.LSpreferencesRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.service.multitenant.DatasourceService;
import com.agaram.eln.primary.service.usermanagement.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping(value = "/Login", method = RequestMethod.POST)
public class LoginController {

	@Autowired
	private LoginService loginService;
	@Autowired
	private LSpreferencesRepository LSpreferencesRepository;
	@Autowired
	private LSuserMasterRepository lsuserMasterRepository;
	
	@Autowired
	private DatasourceService datasourceService;
	
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @RequestMapping(value = "/importchemdata")
    public ResponseEntity<Object> importchemdata(@RequestBody MolExportRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            String requestBody = objectMapper.writeValueAsString(request);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Object> response = restTemplate.exchange(
                "https://marvinjs-demo.chemaxon.com/rest-v1/util/calculate/molExport",
                HttpMethod.POST,
                entity,
                Object.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while fetching data");
        }
    }

	@GetMapping("/LoadSite")
	public List<LSSiteMaster> loadSite(HttpServletRequest request) throws Exception {
		return loginService.loadSite();
	}

	@GetMapping("/LoadSiteMaster")
	public List<LSSiteMaster> LoadSiteMaster(HttpServletRequest request) throws Exception {
		return loginService.LoadSiteMaster();
	}

	@PostMapping("/LoadDomain")
	public List<LSdomainMaster> LoadDomain(@RequestBody LSSiteMaster objsite) throws Exception {
		return loginService.loadDomain(objsite);
	}
	
	@PostMapping("/adsUserValidation")
	public Map<String, Object> adsUserValidation(@RequestBody LoggedUser objuser) throws Exception {
		return loginService.adsUserValidation(objuser);
	}

	@PostMapping("/Login")
	public Map<String, Object> Login(@RequestBody LoggedUser objuser) throws Exception {
		return loginService.Login(objuser);
	}

//	@PostMapping("/ActiveUserEntry")
//	public LSactiveUser activeUserEntry(@RequestBody LSactiveUser objsite) throws Exception {
//		return loginService.activeUserEntry(objsite);
//	}

	@PostMapping("/CheckUserAndPassword")
	public List<LSuserMaster> CheckUserAndPassword(@RequestBody LoggedUser objuser) throws Exception {
		return loginService.CheckUserAndPassword(objuser);
	}

	@PostMapping("/UpdatePassword")
	public LSuserMaster UpdatePassword(@RequestBody LoggedUser objuser) throws Exception {
		return loginService.UpdatePassword(objuser);
	}

	@PostMapping("/Logout")
	public Boolean Logout(@RequestBody LSuserMaster lsuserMaster) throws Exception {
		return loginService.Logout(lsuserMaster);
	}

	@PostMapping("/ChangePassword")
	public LSuserMaster ChangePassword(@RequestBody LoggedUser objuser) throws Exception {
		return loginService.ChangePassword(objuser);
	}

	@PostMapping("/InsertUpdateDomain")
	public LSdomainMaster InsertupdateDomain(@RequestBody LSdomainMaster objClass) throws Exception {
		return loginService.InsertupdateDomain(objClass);
	}

	@PostMapping("/importADSScreen")
	public LSuserMaster importADSScreen(@RequestBody LSuserMaster objClass) throws Exception {
		return loginService.importADSScreen(objClass);
	}

	@PostMapping(path = "/ADSDomainServerConnection")
	public Response adsDomainServerConnection(@RequestBody Map<String, Object> objMap) throws Exception {
		return loginService.ADSDomainServerConnection(objMap);
	}

	@RequestMapping(value = "/importADSGroups")
	Map<String, Object> importADSGroups(@RequestBody Map<String, Object> objMap) throws Exception {
		Map<String, Object> rtnImportAdS = new HashMap<>();

		rtnImportAdS.putAll(ADS_Connection.importADSGroups(objMap));

		return rtnImportAdS;
	}

	@RequestMapping(value = "/importADSUsersByGroup")
	Map<String, List<Map<String, String>>> importADSUsers(@RequestBody Map<String, Object> objMap) throws Exception {
		Map<String, List<Map<String, String>>> rtnImportAdS = new HashMap<>();

		rtnImportAdS.putAll(ADS_Connection.importADSUsersByGroup(objMap));

		return rtnImportAdS;
	}

	@RequestMapping(value = "/addImportADSUsers")
	public Map<String, Object> addImportADSUsers(@RequestBody Map<String, Object> objMap) throws Exception {

		Map<String, Object> rtnMap = new HashMap<>();
		Map<String, Object> isCompleted = new HashMap<>();


		LSpreferences objPrefrencenamed = LSpreferencesRepository.findByTasksettingsAndValuesettings("MainFormUser","Active");
		List<LSuserMaster> lstActUsrs = lsuserMasterRepository.findByUserretirestatus(0);
	
			 if(objPrefrencenamed != null) {
				String dvalue1 = objPrefrencenamed.getValueencrypted();			
				if(dvalue1 != null) {
					String sMainFormUser = AESEncryption.decrypt(dvalue1);
					sMainFormUser = sMainFormUser.replaceAll("\\s", "");
					int nsMainFormUsers = Integer.parseInt(sMainFormUser);
					if (lstActUsrs.size() <= nsMainFormUsers) {
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
					}else {
						rtnMap.put("msg", "User license not available, Please contact administrator");
					}
					
				}	
			}
		
		return rtnMap;
	}

	@RequestMapping(path = "/ADSServerDomainCombo")
	public Map<String, Object> adsServerDomainCombo(@RequestBody LSuserMaster Objclass) throws Exception {

		Map<String, Object> objrtnMap = new HashMap<>();

		objrtnMap = loginService.ADSServerDomainCombo(Objclass);

		return objrtnMap;
	}

	@PostMapping("/LoadDomainMaster")
	public List<LSdomainMaster> LoadDomainMaster(@RequestBody LSSiteMaster objsite) throws Exception {
		return loginService.LoadDomainMaster(objsite);
	}

	@PostMapping("/LoadDomainMasterAdmin")
	public List<LSdomainMaster> LoadDomainMasterAdmin(@RequestBody LSSiteMaster objsite) throws Exception {
		return loginService.LoadDomainMasterAdmin(objsite);
	}

	@PostMapping("/Validateuser")
	public LSuserMaster Validateuser(@RequestBody LSuserMaster objClass) throws Exception {
		return loginService.validateuser(objClass);
	}

	@PostMapping("/LinkLogin")
	public LSuserMaster LinkLogin(@RequestBody LSuserMaster objClass) throws Exception {
		return loginService.LinkLogin(objClass);
	}

	@PostMapping("/InsertupdateSite")
	public LSSiteMaster InsertupdateSite(@RequestBody LSSiteMaster objClass) throws Exception {
		return loginService.InsertupdateSite(objClass);
	}

	@RequestMapping(value = "/azureusertokengenrate", method = RequestMethod.POST)
	public ResponseEntity<?> azureusertokengenrate(@RequestBody LSuserMaster objClass) throws Exception {
		return loginService.azureusertokengenrate(objClass);
	}

	@PostMapping("/azureauthenticatelogin")
	public Map<String, Object> azureauthenticatelogin(@RequestBody LoggedUser objClass) throws Exception {
		return loginService.azureauthenticatelogin(objClass);
	}

	@PostMapping("/createuserforazure")
	public LSuserMaster createuserforazure(@RequestBody LSuserMaster objClass) throws Exception {
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

	@PostMapping("/Loginnotification")
	public Notification Loginnotification(@RequestBody Notification objNotification) throws ParseException {
		return loginService.Loginnotification(objNotification);
	}
	@PostMapping("/Duedatenotification")
	public Notification Duedatenotification(@RequestBody Notification objNotification) throws ParseException {
		return loginService.Duedatenotification(objNotification);
	}

	@PostMapping("/Resourcenotification")
	public Lsrepositoriesdata Resourcenotification(@RequestBody Lsrepositoriesdata objNotification)
			throws ParseException {
		return loginService.Resourcenotification(objNotification);
	}

	@PostMapping("/ValidateuserAndPassword")
	public List<LSuserMaster> ValidateuserAndPassword(@RequestBody LoggedUser objuser) throws Exception {
		return loginService.ValidateuserAndPassword(objuser);
	}

	@PostMapping("/CheckUserPassword")
	public Map<String, Object> CheckUserPassword(@RequestBody LoggedUser objuser) throws Exception {
		return loginService.CheckUserPassword(objuser);
	}
	
	@PostMapping("/updateActiveUserTime")
	public void updateActiveUserTime(@RequestBody Map<String, Object> objMap) throws Exception {
		loginService.updateActiveUserTime(objMap);
	}
	
	@PostMapping("/getCurrentUTCDate")
	public Date getCurrentUTCDate(@RequestBody LoggedUser objuser) throws Exception {
		return commonfunction.getCurrentUtcTime();
	}
	
	@PostMapping("/autoLogout")
	public Boolean autoLogout(@RequestBody LSuserMaster lsuserMaster) throws Exception {
		return loginService.autoLogout(lsuserMaster);
	}
	
	@PostMapping("/getlicense")
	public Map<String,Object> getlicense(@RequestBody Map<String,Object> obj) throws Exception {
		return loginService.getlicense(obj);
	}
	
	@PostMapping("/loadmultisite")
	public List<LSSiteMaster> loadmultisite(@RequestBody LSuserMaster objsite) throws Exception {
		return loginService.loadmultisite(objsite);
	}
	
	@GetMapping("/LoadSitewithoutgzip")
	public List<LSSiteMaster> LoadSitewithoutgzip(HttpServletRequest request) throws Exception {
		return loginService.loadSite();
	}
	
	@GetMapping("/Logintenat/{Tenantname}/{Username}")
	public ResponseEntity<Object> Logintenat(
	    @PathVariable String Tenantname,
	    @PathVariable String Username,
	    HttpServletRequest request) throws Exception {
	    DataSourceConfig sendobj = new DataSourceConfig();
	    sendobj.setTenantid(Tenantname);
	    TenantContext.setCurrentTenant("MAIN");
	    DataSourceConfig rtnobj = datasourceService.Validatetenant(sendobj);
	    if (!rtnobj.getObjResponse().getStatus()) {
	        return new ResponseEntity<>("TENANT_NOT_VALID", HttpStatus.OK);
	    }
	    TenantContext.setCurrentTenant(rtnobj.getTenantid());
	    List<LSuserMaster> users = lsuserMasterRepository.findByusername(Username);
	    if (users.isEmpty()) {
	        return new ResponseEntity<>("USER_NOT_EXIST", HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(users, HttpStatus.OK);
	    }
	}



}
