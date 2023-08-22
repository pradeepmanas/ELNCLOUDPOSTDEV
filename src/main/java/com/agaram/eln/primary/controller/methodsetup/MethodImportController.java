package com.agaram.eln.primary.controller.methodsetup;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.methodsetup.MethodExportService;
import com.agaram.eln.primary.service.methodsetup.MethodImportService;

@RestController
@RequestMapping(value = "/MethodImportController", method = RequestMethod.POST)

public class MethodImportController {
	
	@Autowired
	MethodImportService methodImportService;
	
	@Transactional
	@PostMapping(value = "/importMethods")
	public ResponseEntity<Object> getimportMethod(@Valid @RequestBody Map<String, Object> mapObject) throws Exception {
		System.out.println("started");
		return new ResponseEntity<Object>(methodImportService.importMethodByEntity(mapObject), HttpStatus.OK);
	}
}

