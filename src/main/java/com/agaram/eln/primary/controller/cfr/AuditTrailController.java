package com.agaram.eln.primary.controller.cfr;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.commonfunction.commonfunction;
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
	public List<LScfrreasons> getreasons(@RequestBody Map<String, Object> objMap)throws Exception {
		return auditService.getreasons(objMap);
	}

	@GetMapping("/CFRTranUsername")
	public List<LSuserMaster> CFRTranUsername(HttpServletRequest request)throws Exception {
		return auditService.CFRTranUsername();
	}

	@GetMapping(path = "/CFRTranModuleName")
	public List<String> CFRTranModuleName(HttpServletRequest request)throws Exception {
		return auditService.CFRTranModuleName();
	}

	@PostMapping("/InsertupdateReasons")
	public LScfrreasons InsertupdateReasons(@RequestBody LScfrreasons objClass)throws Exception {

		return auditService.InsertupdateReasons(objClass);
	}

	@PostMapping("/GetAuditconfigUser")
	public Map<String, Object> GetAuditconfigUser(@RequestBody LSaudittrailconfiguration LSaudittrailconfiguration)throws Exception {
		return auditService.GetAuditconfigUser(LSaudittrailconfiguration);
	}

	@PostMapping("/SaveAuditconfigUser")
	public List<LSaudittrailconfiguration> SaveAuditconfigUser(@RequestBody LSaudittrailconfiguration[] lsAudit)throws Exception {
		return auditService.SaveAuditconfigUser(lsAudit);
	}

	@PostMapping("/GetCFRTransactions")
	public List<LScfttransaction> GetCFRTransactions(@RequestBody Map<String, Object> objCFRFilter)
			throws ParseException {
		return auditService.GetCFRTransactions(objCFRFilter);
	}

	@PostMapping("/CheckUserPassWord")
	public LSuserMaster CheckUserPassWord(@RequestBody LoggedUser objuser)throws Exception {
		return auditService.CheckUserPassWord(objuser);
	}

	@PostMapping("/ReviewBtnValidation")
	public List<LSreviewdetails> ReviewBtnValidation(@RequestBody LSreviewdetails[] objreview)throws Exception {

		return auditService.ReviewBtnValidation(objreview);
	}

	@PostMapping("/GetReviewDetails")
	public List<LSreviewdetails> GetReviewDetails(@RequestBody List<LSreviewdetails> objreviewdetails)throws Exception {

		if (objreviewdetails.get(0).getObjuser() != null) {

			LSuserMaster userClass = auditService.CheckUserPassWord(objreviewdetails.get(0).getObjuser());

			if (userClass.getObjResponse().getStatus()) {

				objreviewdetails.get(0).setLsusermaster(userClass);

				return auditService.GetReviewDetails(objreviewdetails);
			}

		}

		return auditService.GetReviewDetails(objreviewdetails);
	}

	@PostMapping("/GetReviewDetails12")
	public Map<String, Object> GetReviewDetails12(@RequestBody LSreviewdetails[] objreviewdetails)throws Exception {
		Response objResponse = new Response();
		Map<String, Object> objreview = new HashMap<String, Object>();

		objreview.put("transaction", auditService.GetReviewDetails12(objreviewdetails));
		objResponse.setStatus(true);
		objreview.put("objResponse", objResponse);
		return objreview;
	}

	@PostMapping("/exportDataFile")
	public Map<String, Object> exportDataFile(@RequestBody LSuserMaster objuser) throws Exception {

		if (objuser.getObjuser() != null) {

			LSuserMaster userClass = auditService.CheckUserPassWord(objuser.getObjuser());

			if (userClass.getObjResponse().getStatus()) {

				return auditService.exportData(objuser);
			} else {
				objuser.getObjsilentaudit().setComments("Entered invalid username and password");
				Map<String, Object> map = new HashMap<>();
				map.put("objsilentaudit", objuser.getObjsilentaudit());
				map.put("objmanualaudit", objuser.getObjmanualaudit());
				map.put("objUser", objuser.getObjuser());
				auditService.AuditConfigurationrecord(map);
				objuser.setObjResponse(new Response());
				objuser.getObjResponse().setStatus(false);
				objuser.getObjResponse().setInformation("ID_VALIDATION");
				map.put("cfttDeatils", objuser);
				return map;
			}

		}
		return auditService.exportData(objuser);
	}

	@PostMapping("/AuditConfigurationrecord")
	public LScfttransaction AuditConfigurationrecord(@RequestBody Map<String, Object> objmanualaudit)
			throws ParseException {
		return auditService.AuditConfigurationrecord(objmanualaudit);
	}

	@PostMapping("/silentandmanualRecordHandler")
	public LScfttransaction silentandmanualRecordHandler(@RequestBody Map<String, Object> mapObj)
			throws ParseException {
		return auditService.silentandmanualRecordHandler(mapObj);
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/checkManualAudit")
	public Map<String, Object> checkManualAudit(@RequestBody Map<String, Object> reqMap) throws ParseException {
		Map<String, Object> rMap = new HashMap<>();

		Map<String, Object> vMap = (Map<String, Object>) reqMap.get("manualAuditPass");

		LoggedUser objuser = new ObjectMapper().convertValue(vMap.get("objuser"), new TypeReference<LoggedUser>() {
		});

		if (objuser.getLoggedfrom() == 1) {

			rMap.put("audit", true);
			rMap.put("objuser", reqMap.get("valuePass"));
			return rMap;

		} else {
			if (commonfunction.checkuseronmanualaudit(objuser.getEncryptedpassword(), objuser.getsPassword())) {
				rMap.put("audit", true);
				rMap.put("objuser", reqMap.get("valuePass"));
				
			
				return rMap;
			}

			rMap.put("audit", false);
			rMap.put("objuser", reqMap.get("valuePass"));
			return rMap;
		}
	}
	
		@PostMapping("/GetCFRTransactionsdid")
	public List<LScfttransaction> GetCFRTransactionsdid(@RequestBody Map<String, Object> objCFRFilter)
			throws ParseException {
		return auditService.GetCFRTransactionsdid(objCFRFilter);
	}
}
