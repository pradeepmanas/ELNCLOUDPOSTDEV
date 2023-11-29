package com.agaram.eln.primary.controller.material;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.material.MappedTemplateFieldPropsMaterial;
import com.agaram.eln.primary.model.material.MaterialConfig;
import com.agaram.eln.primary.service.material.TransactionService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

	@Autowired
	TransactionService transactionService;

	@PostMapping(value = "/getLoadOnInventoryData")
	public ResponseEntity<Object> getLoadOnInventoryData(@RequestBody Map<String, Object> inputMap) throws Exception {

		return transactionService.getLoadOnInventoryData(inputMap);
	}

	@PostMapping(value = "/getInventoryTransaction")
	public ResponseEntity<Object> getInventoryTransaction(@RequestBody Map<String, Object> inputMap) throws Exception {

		return transactionService.getInventoryTransaction(inputMap);
	}
	
	@PostMapping(value = "/getResultInventoryTransaction")
	public ResponseEntity<Object> getResultInventoryTransaction(@RequestBody Map<String, Object> inputMap) throws Exception {

		return transactionService.getResultInventoryTransaction(inputMap);
	}

	@PostMapping(value = "/createMaterialInventoryTrans")
	public ResponseEntity<Object> createMaterialInventoryTrans(@RequestBody Map<String, Object> inputMap)
			throws Exception {

		return transactionService.createMaterialInventoryTrans(inputMap);
	}
	
	@PostMapping(value = "/createMaterialResultUsed")
	public ResponseEntity<Object> createMaterialResultUsed(@RequestBody Map<String, Object> inputMap) throws Exception {

		return transactionService.createMaterialResultUsed(inputMap);
	}

	@PostMapping(value = "/updateMaterialDynamicTable")
	public ResponseEntity<Object> updateMaterialDynamicTable(@RequestBody MaterialConfig[] objLstClass)
			throws Exception {

		return transactionService.updateMaterialDynamicTable(objLstClass);
	}
	
	@PostMapping(value = "/updateMappedTemplateFieldPropsMaterialTable")
	public ResponseEntity<Object> updateMappedTemplateFieldPropsMaterialTable(@RequestBody MappedTemplateFieldPropsMaterial[] objLstClass)
			throws Exception {

		return transactionService.updateMappedTemplateFieldPropsMaterialTable(objLstClass);
	}
	
	@PostMapping(value = "/getMaterialLst4DashBoard")
	public ResponseEntity<Object> getMaterialLst4DashBoard(@RequestBody Map<String, Object> inputMap)
			throws Exception {

		return transactionService.getMaterialLst4DashBoard(inputMap);
	}
	
	@PostMapping(value="/updateMaterialInventoryNotification")
	public void updateMaterialInventoryNotification(@RequestBody Map<String, Object> inputMap)
			throws Exception {

		transactionService.updateMaterialInventoryNotification(inputMap);
	}	
	
	@PostMapping(value = "/getMaterialLst4NewMaterial")
	public ResponseEntity<Object> getMaterialLst4NewMaterial(@RequestBody Map<String, Object> inputMap)
			throws Exception {

		return transactionService.getMaterialLst4NewMaterial(inputMap);
	}
	
	@PostMapping(value = "/getMaterialInvLst4NewMaterialInv")
	public ResponseEntity<Object> getMaterialInvLst4NewMaterialInv(@RequestBody Map<String, Object> inputMap)
			throws Exception {

		return transactionService.getMaterialInvLst4NewMaterialInv(inputMap);
	}
	
	@RequestMapping(value = "/getTransactionResultsByDate", method = RequestMethod.POST)
	public ResponseEntity<Object> getTransactionResultsByDate(@RequestBody Map<String, Object> inputMap) throws Exception {

		return transactionService.getTransactionResultsByDate(inputMap);
	}
}