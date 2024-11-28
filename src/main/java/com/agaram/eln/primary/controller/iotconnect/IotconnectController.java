package com.agaram.eln.primary.controller.iotconnect;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.model.cfr.LSpreferences;
import com.agaram.eln.primary.model.equipment.Equipment;
import com.agaram.eln.primary.model.equipment.EquipmentCategory;
import com.agaram.eln.primary.model.equipment.EquipmentType;
import com.agaram.eln.primary.model.instrumentDetails.LSOrderElnMethod;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentCategory;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.iotconnect.RCTCPFileDetails;
import com.agaram.eln.primary.model.iotconnect.RCTCPResultDetails;
import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.service.iotconnect.IotconnectService;
import com.agaram.eln.primary.service.methodsetup.EvaluateParserService;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/iotconnect", method = RequestMethod.POST)
public class IotconnectController {
	
	@Autowired
	IotconnectService iotconnectservice;
	
	@Autowired
	EvaluateParserService parserService;
	
//	@Autowired
//	TenantContext TenantContext;
	
	@RequestMapping("/getInstcategory")
	public List<InstrumentCategory> getInstcategory(@RequestBody InstrumentCategory instcat){
		return iotconnectservice.getInstcategory();
	}

	@RequestMapping("/getInstruments")
	public List<InstrumentMaster> getInstruments(@RequestBody InstrumentCategory instcat){
		return iotconnectservice.getInstruments(instcat);
	}
	
	@RequestMapping(value = "/getRCPortdata")
	public void getRCPortSqlData(final HttpServletRequest request, @RequestBody Map<String, Object> mapObject) throws Exception {

		final ObjectMapper mapper = new ObjectMapper();
		final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
		
		final Equipment equipobj = mapper.convertValue(mapObject.get("equipment"), Equipment.class);
		
		final Method methodobj = mapper.convertValue(mapObject.get("method"), Method.class);
		final String rawData =  mapper.convertValue(mapObject.get("rawData"), String.class);
		final LSuserMaster userobj = mapper.convertValue(mapObject.get("user"), LSuserMaster.class);
		final Integer isMultitenant = mapper.convertValue(mapObject.get("ismultitenant"), Integer.class);
		final String tenant =  mapper.convertValue(mapObject.get("tenant"), String.class);
		final LSOrderElnMethod orderelnmethod = mapper.convertValue(mapObject.get("batchcode"), LSOrderElnMethod.class);

		final ResponseEntity<Object> parsedData ;
		
		List<RCTCPFileDetails> filelist = iotconnectservice.ConvertRawDataToFile(rawData,methodobj.getMethodkey(),equipobj.getNequipmentcode(),isMultitenant,orderelnmethod,userobj,site);
		
		if(isMultitenant==0) {
			parsedData = parserService.evaluateSQLParser(methodobj.getMethodkey(), site, rawData,isMultitenant,request);
		}else {
			parsedData = parserService.evaluateParser(methodobj.getMethodkey(), site, rawData,tenant,isMultitenant,request);
		}
		System.out.println("parsedData:" +parsedData);

		if(orderelnmethod.getBatchcode() == null || orderelnmethod.getBatchcode() == 0) {
			iotconnectservice.InsertRCTCPResultDetails(parsedData.getBody(),userobj,site,filelist);
		}else {
			iotconnectservice.InsertIntoOrders(parsedData.getBody(),orderelnmethod.getBatchcode());
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getiotresultdetails")
	public  List<RCTCPResultDetails> getiotresultdetails(final HttpServletRequest request, @RequestBody Map<String, Object> mapObject) throws Exception {

		final ObjectMapper mapper = new ObjectMapper();
		
        List<Integer> methodKeys = (List<Integer>) (mapObject.get("methodkeyList"));
	    List<Integer> instkeys = (List<Integer>)(mapObject.get("instList"));
	    
		final RCTCPResultDetails resultobj = mapper.convertValue(mapObject.get("rctcpresultdetails"), RCTCPResultDetails.class);

	    if (methodKeys == null || instkeys == null) {
	        throw new IllegalArgumentException("Required parameters are missing");
	    }
	    
		return iotconnectservice.getiotresultdetails(methodKeys , instkeys ,resultobj);
	}
	
	@RequestMapping("/getpreferencedata")
	public LSpreferences getpreferencedata(@RequestBody Map<String, Object> mapObject){
		return iotconnectservice.getpreferencedata(mapObject);
		
	}
	@RequestMapping(value = "/getEquipmenttype")
	public ResponseEntity<Object> getEquipmenttype(@RequestBody Map<String, Object> inputMap) throws Exception {	
		Integer nsiteInteger = (Integer) inputMap.get("sitecode");

		return (ResponseEntity<Object>) iotconnectservice.getEquipmenttype(nsiteInteger);
	}
	@RequestMapping("/getEquipmentcat")
	public List<EquipmentCategory> getEquipmentcat(@RequestBody EquipmentType equipmenttype){
		return iotconnectservice.getEquipmentcat(equipmenttype);
	}
	@RequestMapping("/getEquipment")
	public List<Equipment> getEquipment(@RequestBody EquipmentCategory equicat){
		return iotconnectservice.getEquipment(equicat);
	}
	@RequestMapping("/getEquipmentmethod")
	public List<Method> getEquipmentmethod(@RequestBody Equipment equ){
		return iotconnectservice.getEquipmentmethod(equ);
	}
	
	@RequestMapping(value = "/getOrdersBasedOnmethod")
	public List<LSOrderElnMethod> getOrdersBasedOnmethod(@RequestBody Map<String, Object> inputMap) throws Exception {

		final ObjectMapper mapper = new ObjectMapper();
		final Method Methodobj = mapper.convertValue(inputMap.get("method"), Method.class);
		return  iotconnectservice.getOrdersBasedOnmethod(Methodobj);

	}
	@RequestMapping(value = "/checkforIOTAttchment")
	public Map<String, Object> checkforIOTAttchment(@RequestBody Map<String, Object> inputMap) throws Exception {
		final ObjectMapper mapper = new ObjectMapper();
		Integer batchcode = (Integer) inputMap.get("batchcode");
		Long longbatchcode = Long.valueOf(batchcode);
		return  iotconnectservice.checkforIOTAttchment(longbatchcode);
	}

}
