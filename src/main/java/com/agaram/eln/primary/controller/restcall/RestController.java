package com.agaram.eln.primary.controller.restcall;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.service.restcall.RestService;

@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/Restcall", method = RequestMethod.POST)
public class RestController {
	
	@Autowired
    private RestService restService;
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
	
	@PostMapping("/forSyncOrderFromLims")
	public String forSyncOrderFromLims(@RequestBody Map<String, Object> map) throws Exception {
		return restService.forSyncOrderFromLims();
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
		return restService.getUpdateSdmslabsheetDetail(objMap);
	}
	
	@RequestMapping(value = "/syncLimsMasters")
	public boolean syncLimsMasters(@RequestBody Map<String, Object> objMap) throws Exception {
		return restService.syncLimsMasters(objMap);
	}

	@RequestMapping("/SDMSMappedInstrumentsSync")
	public String ImportSDMSTest(@RequestBody Map<String, Object> objMap) throws Exception {
		return restService.ImportSDMSTest(objMap);
	}
}
