package com.agaram.eln.primary.controller.equipment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.equipment.EquipmentType;
import com.agaram.eln.primary.service.equipment.EquipmentTypeService;

@RestController
@RequestMapping("/equipmentType")
public class EquipmentTypeController {

	@Autowired
	private EquipmentTypeService equipmentTypeService;
	
	@RequestMapping(value = "/getEquipmentype", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getEquipmentype(@RequestBody EquipmentType objMaterialType) throws Exception {		
		return (ResponseEntity<Object>) equipmentTypeService.getEquipmentype(objMaterialType);
	}
	
	@RequestMapping(value = "/createEquipmentType", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createEquipmentType(@RequestBody EquipmentType objMaterialType) throws Exception {		
		return (ResponseEntity<Object>) equipmentTypeService.createEquipmentType(objMaterialType);
	}
	
	@RequestMapping(value = "/getEquipmentTypeField", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getEquipmentTypeField(@RequestBody EquipmentType objMaterialType) throws Exception {		
		return (ResponseEntity<Object>) equipmentTypeService.getEquipmentTypeField(objMaterialType);
	}

}
