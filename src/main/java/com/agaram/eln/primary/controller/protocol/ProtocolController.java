package com.agaram.eln.primary.controller.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocolsteps;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolordersampleupdates;
import com.agaram.eln.primary.model.protocols.LSprotocolsampleupdates;
import com.agaram.eln.primary.model.protocols.LSprotocolstep;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflow;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.service.protocol.ProtocolService;

@RestController
@RequestMapping(value = "/protocol", method = RequestMethod.POST)
public class ProtocolController {

	@Autowired
	ProtocolService ProtocolMasterService;

	@RequestMapping(value = "/getProtocolMasterInit")
	protected Map<String, Object> getProtocolMasterInit(@RequestBody Map<String, Object> argObj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getProtocolMasterInit(argObj);
		return objMap;
	}

	@RequestMapping(value = "/addProtocolMaster")
	protected Map<String, Object> addProtocolMaster(@RequestBody Map<String, Object> argObj) {

		return ProtocolMasterService.addProtocolMaster(argObj);

	}

	@RequestMapping(value = "/deleteProtocolMaster")
	protected Map<String, Object> deleteProtocolMaster(@RequestBody Map<String, Object> argObj) {

		return ProtocolMasterService.deleteProtocolMaster(argObj);
	}

	@RequestMapping(value = "/getProtocolMasterLst")
	protected Map<String, Object> getProtocolMasterLst(@RequestBody Map<String, Object> argObj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getLSProtocolMasterLst(argObj);
		return objMap;
	}

	@RequestMapping(value = "/getProtocolStepLst")
	protected Map<String, Object> getProtocolStepLst(@RequestBody Map<String, Object> argObj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getProtocolStepLst(argObj);
		return objMap;
	}

	@RequestMapping(value = "/getAllProtocolStepLst")
	protected Map<String, Object> getAllProtocolStepLst(@RequestBody Map<String, Object> argObj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getAllProtocolStepLst(argObj);
		return objMap;
	}

	@RequestMapping(value = "/getOrdersLinkedToProtocol")
	protected Map<String, Object> getOrdersLinkedToProtocol(@RequestBody Map<String, Object> argObj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getOrdersLinkedToProtocol(argObj);
		return objMap;
	}

	@RequestMapping(value = "/addProtocolStep")
	protected Map<String, Object> addProtocolStep(@RequestBody Map<String, Object> argObj) {

		return ProtocolMasterService.addProtocolStep(argObj);
	}

	@RequestMapping(value = "/deleteProtocolStep")
	protected Map<String, Object> deleteProtocolStep(@RequestBody Map<String, Object> argObj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.deleteProtocolStep(argObj);
		return objMap;
	}

	@RequestMapping(value = "/sharewithteam")
	protected Map<String, Object> sharewithteam(@RequestBody Map<String, Object> argObj) {

		return ProtocolMasterService.sharewithteam(argObj);
	}

	@RequestMapping(value = "/updateworkflowforProtocol")
	protected Map<String, Object> updateworkflowforProtocol(@RequestBody LSprotocolmaster objClass) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.updateworkflowforProtocol(objClass);
		return objMap;
	}

	@PostMapping("/GetProtocolWorkflow")
	public List<LSprotocolworkflow> GetProtocolWorkflow(@RequestBody LSprotocolworkflow objclass) {
		return ProtocolMasterService.GetProtocolWorkflow(objclass);
	}

	@PostMapping("/InsertUpdatesheetWorkflow")
	public List<LSprotocolworkflow> InsertUpdatesheetWorkflow(
			@RequestBody List<LSprotocolworkflow> LSprotocolworkflow) {
		return ProtocolMasterService.InsertUpdatesheetWorkflow(LSprotocolworkflow);
	}

	@PostMapping("/Deletesheetworkflow")
	public Response Deletesheetworkflow(@RequestBody LSprotocolworkflow objflow) {
		return ProtocolMasterService.Deletesheetworkflow(objflow);
	}

	@RequestMapping(value = "/getProtocolMasterList")
	protected List<LSprotocolmaster> getProtocolMasterList(@RequestBody LSuserMaster objClass) {

		return ProtocolMasterService.getProtocolMasterList(objClass);
	}

	@RequestMapping(value = "/addProtocolOrder")
	protected Map<String, Object> addProtocolOrder(@RequestBody LSlogilabprotocoldetail LSlogilabprotocoldetail) {

		return ProtocolMasterService.addProtocolOrder(LSlogilabprotocoldetail);

	}

	@RequestMapping(value = "/getProtocolOrderList")
	protected Map<String, Object> getProtocolOrderList(@RequestBody LSlogilabprotocoldetail LSlogilabprotocoldetail) {
		return ProtocolMasterService.getProtocolOrderList(LSlogilabprotocoldetail);
	}
	
	
	@RequestMapping(value = "/getProtocolOrderListfortabchange")
	protected Map<String, Object> getProtocolOrderListfortabchange(@RequestBody LSlogilabprotocoldetail LSlogilabprotocoldetail) {
		return ProtocolMasterService.getProtocolOrderListfortabchange(LSlogilabprotocoldetail);
	}
	
	@RequestMapping(value = "/getreminProtocolOrderList")
	protected Map<String, Object> getreminProtocolOrderList(@RequestBody LSlogilabprotocoldetail LSlogilabprotocoldetail) {
		return ProtocolMasterService.getreminProtocolOrderList(LSlogilabprotocoldetail);
	}
	
	@RequestMapping(value = "/getreminProtocolOrderListontab")
	protected Map<String, Object> getreminProtocolOrderListontab(@RequestBody LSlogilabprotocoldetail LSlogilabprotocoldetail) {
		return ProtocolMasterService.getreminProtocolOrderListontab(LSlogilabprotocoldetail);
	}

	@RequestMapping(value = "/updateProtocolOrderStep")
	protected Map<String, Object> updateProtocolOrderStep(@RequestBody Map<String, Object> argObj) {
		return ProtocolMasterService.updateProtocolOrderStep(argObj);
	}

	@RequestMapping(value = "/getProtocolOrderStepLst")
	protected Map<String, Object> getProtocolOrderStepLst(@RequestBody Map<String, Object> argObj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getProtocolOrderStepLst(argObj);
		return objMap;
	}

	@RequestMapping(value = "/getAllMasters")
	protected Map<String, Object> getAllMasters(@RequestBody LSuserMaster objuser) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getAllMasters(objuser);
		return objMap;
	}

	@RequestMapping(value = "/startStep")
	protected Map<String, Object> startStep(@RequestBody LSuserMaster objuser) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.startStep(objuser);
		return objMap;
	}

	@RequestMapping(value = "/updateStepStatus")
	protected Map<String, Object> updateStepStatus(@RequestBody Map<String, Object> argMap) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.updateStepStatus(argMap);
		return objMap;
	}

	@RequestMapping(value = "/updateOrderStatus")
	protected Map<String, Object> updateOrderStatus(@RequestBody LSlogilabprotocoldetail argMap) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.updateOrderStatus(argMap);
		return objMap;
	}

	@RequestMapping(value = "/getLsrepositoriesLst")
	protected Map<String, Object> getLsrepositoriesLst(@RequestBody Map<String, Object> argMap) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getLsrepositoriesLst(argMap);
		return objMap;
	}

	@RequestMapping(value = "/getLsrepositoriesDataLst")
	protected Map<String, Object> getLsrepositoriesDataLst(@RequestBody Map<String, Object> argMap) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getLsrepositoriesDataLst(argMap);
		return objMap;
	}

	@PostMapping("/GetProtocolTransactionDetails")
	public Map<String, Object> GetProtocolTransactionDetails(@RequestBody LSprotocolmaster LSprotocolmaster) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.GetProtocolTransactionDetails(LSprotocolmaster);
		return objMap;
	}
//	@RequestMapping(value="/addProtocolOrderStep")
//	protected Map<String, Object> addProtocolOrderStep(@RequestBody Map<String, Object> argObj){
//		Map<String, Object> objMap = new HashMap<String, Object>();
//		objMap = ProtocolMasterService.addProtocolOrderStep(argObj);
//		return objMap;
//	}

	@RequestMapping(value = "/addProtocolStepforsaveas")
	protected Map<String, Object> addProtocolStepforsaveas(@RequestBody Map<String, Object> argObj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.addProtocolStepforsaveas(argObj);
		return objMap;
	}

	@RequestMapping("/GetProtocolResourcesQuantitylst")
	public List<LSprotocolsampleupdates> GetProtocolResourcesQuantitylst(@RequestBody LSprotocolstep LSprotocolstep) {
		return ProtocolMasterService.GetProtocolResourcesQuantitylst(LSprotocolstep);
	}

	@RequestMapping("/GetProtocolVersionDetails")
	public Map<String, Object> GetProtocolVersionDetails(@RequestBody Map<String, Object> argObj) {
		return ProtocolMasterService.GetProtocolVersionDetails(argObj);
	}

	@RequestMapping("/GetProtocolorderResourcesQuantitylst")
	public List<LSprotocolordersampleupdates> GetProtocolorderResourcesQuantitylst(
			@RequestBody LSlogilabprotocolsteps LSlogilabprotocolsteps) {
		return ProtocolMasterService.GetProtocolorderResourcesQuantitylst(LSlogilabprotocolsteps);
	}

	@RequestMapping("/GetProtocolTemplateVerionLst")
	public Map<String, Object> GetProtocolTemplateVerionLst(@RequestBody Map<String, Object> argObj) {
		return ProtocolMasterService.GetProtocolTemplateVerionLst(argObj);
	}
	
	@PostMapping("/getprotocols")
	public List <LSprotocolmaster> getprotocols(@RequestBody LSuserMaster objusers)
	{
		return ProtocolMasterService.getprotocol(objusers);
	}
}