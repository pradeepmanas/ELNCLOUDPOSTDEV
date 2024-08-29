package com.agaram.eln.primary.controller.barcode;


import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.agaram.eln.primary.model.barcode.BarcodeMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.service.barcode.BarcodeMasterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping(value = "/Barcode", method = RequestMethod.POST)
public class BarcodeMasterController {
	@Autowired
	private BarcodeMasterService barcodemasterservice;
	
	@PostMapping("/InsertBarcode")
	public ResponseEntity<Object> InsertBarcode(MultipartHttpServletRequest request)
			throws JsonMappingException, JsonProcessingException, ParseException {
		return barcodemasterservice.InsertBarcode(request);
	}
	
	@PostMapping("/GetBarcodemaster")
	public List<BarcodeMaster> GetBarcodemaster(@RequestBody LSuserMaster objuser)
	{
		return barcodemasterservice.GetBarcodemaster(objuser);
	}
	
	@RequestMapping(value = "/Getbarcodefileoncode/{barcode}/{ismultitenant}/{tenant}/{screen}/{primarykey}/{path}/{username}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> Getbarcodefileoncode(@PathVariable String barcode,
			@PathVariable String ismultitenant, @PathVariable String tenant,
			@PathVariable String screen, @PathVariable String primarykey, 
			@PathVariable String path, @PathVariable String username)
			throws ParseException, IOException {
		return barcodemasterservice.getbarcodefileoncode(barcode, ismultitenant, tenant,screen,primarykey,path,username);
	}
	
}
