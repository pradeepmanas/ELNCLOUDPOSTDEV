package com.agaram.eln.primary.controller.equipment;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.equipment.Equipment;
import com.agaram.eln.primary.service.equipment.EquipmentService;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

	@Autowired
	private EquipmentService equipmentService;
	
	@RequestMapping(value = "/getEquipment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getEquipment(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return (ResponseEntity<Object>) equipmentService.getEquipment(inputMap);
	}
	
	@RequestMapping(value = "/getEquipmentByFilter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getEquipmentByFilter(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return (ResponseEntity<Object>) equipmentService.getEquipmentByFilter(inputMap);
	}
	
	@RequestMapping(value = "/getEquipmentPropsForFilter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getEquipmentPropsForFilter(@RequestBody Map<String, Object> inputMap) throws Exception {

		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		
		return (ResponseEntity<Object>) equipmentService.getEquipmentPropsForFilter(nsiteInteger);
	}
	
	@RequestMapping(value = "/getEquipmentProps", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getEquipmentProps(@RequestBody Map<String, Object> inputMap) throws Exception {

		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		
		return (ResponseEntity<Object>) equipmentService.getEquipmentProps(nsiteInteger);
	}
	
	@RequestMapping(value = "/getEquipmentTypeBasedCat", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getEquipmentTypeBasedCat(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return (ResponseEntity<Object>) equipmentService.getEquipmentTypeBasedCat(inputMap);
	}
	
	@RequestMapping(value = "/createEquipment", method = RequestMethod.POST)
	public ResponseEntity<Object> createEquipment(@RequestBody Equipment obj) throws Exception {

		return equipmentService.createEquipment(obj);
	}
	
	@RequestMapping(value = "/updateEquipment", method = RequestMethod.POST)
	public ResponseEntity<Object> updateEquipment(@RequestBody Equipment obj) throws Exception {

		return equipmentService.updateEquipment(obj);
	}
	
	@RequestMapping(value = "/getEquipmentBySearchField", method = RequestMethod.POST)
	public ResponseEntity<Object> getEquipmentBySearchField(@RequestBody Map<String, Object> inputMap) throws Exception {

		return equipmentService.getEquipmentBySearchField(inputMap);
	}
	
	@RequestMapping(value = "/OsearchEquipment", method = RequestMethod.POST)
	public ResponseEntity<Object> OsearchEquipment(@RequestBody String Searchname) throws Exception {
		return equipmentService.OsearchEquipment(Searchname);
	}
	
	@RequestMapping(value = "/onGetEquipmentSelect", method = RequestMethod.POST)
	public ResponseEntity<Object> onGetEquipmentSelect(@RequestBody Map<String, Object> inputMap)
			throws Exception {
		return equipmentService.onGetEquipmentSelect(inputMap);
	}
}
