package com.agaram.eln.primary.controller.inventory;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.instrumentDetails.LSinstrumentmaster;
import com.agaram.eln.primary.model.inventory.LSinstrument;
import com.agaram.eln.primary.model.inventory.LSmaterial;
import com.agaram.eln.primary.model.inventory.LSsection;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.service.inventory.MaterialService;

@RestController
@RequestMapping(value = "/Material", method = RequestMethod.POST)
public class MaterialController {
	@Autowired
    private MaterialService materialService;
	
	
	@GetMapping("/GetSection")
	public List<LSsection> getSection() {
		return materialService.getSection();
	}
	
	/**
	 *For Get Masters Materials and Instruments
	 * 
	 * @param objMap
	 * @return List<class>
	 */
	
	@RequestMapping("/getlsMaterial")
	public List<LSmaterial> getlsMaterial(@RequestBody LSuserMaster objuser) {
		return materialService.getlsMaterial(objuser);
	}
	
	@RequestMapping("/getLsInstrument")
	public List<LSinstrument> getLsInstrument(@RequestBody LSuserMaster objuser) {
		return materialService.getLsInstrument(objuser);
	}
	
	@RequestMapping("/getLsInstrumentMaster")
	public List<LSinstrumentmaster> getLsInstrumentMaster(@RequestBody LSuserMaster objuser) {
		return materialService.getLsInstrumentMaster(objuser);
	}
	
	@RequestMapping("/getEquipmentDetails")
	public Map<String, Object> getEquipmentDetails(@RequestBody Map<String, Object> objMap)
	{
		return materialService.getEquipmentDetails(objMap);
	}
	
	@RequestMapping("/getMaterialDetails")
	public Map<String, Object> getMaterialDetails(@RequestBody Map<String, Object> objMap) throws Exception
	{
		return materialService.getMaterialDetails(objMap);
	}
}
