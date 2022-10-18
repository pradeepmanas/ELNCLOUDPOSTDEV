package com.agaram.eln.primary.controller.material;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	@PostMapping(value = "/createMaterialInventoryTrans")
	public ResponseEntity<Object> createMaterialInventoryTrans(@RequestBody Map<String, Object> inputMap) throws Exception {

		return transactionService.createMaterialInventoryTrans(inputMap);
	}
}