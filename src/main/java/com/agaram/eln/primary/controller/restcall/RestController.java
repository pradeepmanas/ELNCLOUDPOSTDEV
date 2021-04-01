package com.agaram.eln.primary.controller.restcall;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.service.cfr.AuditService;
import com.agaram.eln.primary.service.restcall.RestService;
import com.fasterxml.jackson.databind.ObjectMapper;

@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/Restcall", method = RequestMethod.POST)
public class RestController {
	
	@Autowired
    private RestService restService;
	@Autowired
	private AuditService auditService;
//	@Autowired
//	private AuditService auditService;
	
	@PostMapping("/ImportLimsTest")
	public String ImportLimsTest(@RequestBody String str) throws Exception {
		return restService.ImportLimsTest(str);
	}
	
	@PostMapping("/ImportLimsOrder")
	public String ImportLimsOrder(@RequestBody String str) throws Exception {
		return restService.ImportLimsOrder(str);
	}
	
	@RequestMapping(value = "/importLIMSTable")
	public String importLIMSMaterial(@RequestBody String str) throws Exception {
		return restService.importLIMSMaterial();
	}
	
	@RequestMapping(value = "/importLIMSMaterialTrans")
	public String importLIMSMaterialTrans(@RequestBody String str) throws Exception {
		return restService.importLIMSMaterialTrans();
	}
	
	@RequestMapping(value = "/exportsdmslabsheetdetail")
	public Response getUpdateSdmslabsheetDetail(@RequestBody Map<String, Object> objMap) throws Exception {
		
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
				return restService.getUpdateSdmslabsheetDetail(objMap);
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
		return restService.getUpdateSdmslabsheetDetail(objMap);
	}
	
	@RequestMapping(value = "/syncLimsMasters")
	public boolean syncLimsMasters(@RequestBody Map<String, Object> objMap) throws Exception {
		return restService.syncLimsMasters(objMap);
	}

}
