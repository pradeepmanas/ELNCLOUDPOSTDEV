package com.agaram.eln.primary.controller.material;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.material.ManufacturerService;

@RestController
@RequestMapping(value = "/manufacturer", method = RequestMethod.POST)
public class ManufacturerController {
	
	@Autowired
	private ManufacturerService manufacturerService;
	
	@PostMapping(value = "/getManufacturer")
	public ResponseEntity<Object> getManufacturer(@RequestBody Map<String, Object> inputMap)  throws Exception{

		int nmanufcode = 0;
		return manufacturerService.getManufacturer(nmanufcode);
		 
	}

}
