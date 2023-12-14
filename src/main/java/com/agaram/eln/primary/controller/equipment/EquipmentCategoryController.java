package com.agaram.eln.primary.controller.equipment;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.equipment.EquipmentCategory;
import com.agaram.eln.primary.service.equipment.EquipmentCategoryService;

@RestController
@RequestMapping("/equipmentCat")
public class EquipmentCategoryController {

	@Autowired
	private EquipmentCategoryService equipmentCategoryService;
	
	@PostMapping(value = "/getEquipmentCategory")
	public ResponseEntity<Object> getequipmentCategory(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		
		return equipmentCategoryService.getequipmentCategory(nsiteInteger);
	}	
	
	@PostMapping(value = "/createEquipmentCategory")
	public ResponseEntity<Object> createEquipmentCategory(@RequestBody Map<String, Object> inputMap) throws Exception {
		return equipmentCategoryService.createEquipmentCategory(inputMap);
	}

	@PostMapping(value = "/updateEquipmentCategory")
	public ResponseEntity<Object> updateEquipmentCategory(@RequestBody Map<String, Object> inputMap) throws Exception {
		final ObjectMapper objmapper = new ObjectMapper();
		final EquipmentCategory objEquipmentCategory = objmapper.convertValue(inputMap.get("EquipmentCategory"),EquipmentCategory.class);
		
		return equipmentCategoryService.updateEquipmentCategory(objEquipmentCategory);
	}
	
	@PostMapping(value = "/deleteEquipmentCategory")
	public ResponseEntity<Object> deleteEquipmentCategory(@RequestBody Map<String, Object> inputMap) throws Exception {
		final ObjectMapper objmapper = new ObjectMapper();
		final EquipmentCategory objEquipmentCategory = objmapper.convertValue(inputMap.get("EquipmentCategory"),EquipmentCategory.class);
		final LScfttransaction objsilentaudit = objmapper.convertValue(inputMap.get("objsilentaudit"),LScfttransaction.class);
		return equipmentCategoryService.deleteEquipmentCategory(objEquipmentCategory,objsilentaudit);
	}
}