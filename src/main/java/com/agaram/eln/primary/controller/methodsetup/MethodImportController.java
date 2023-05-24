package com.agaram.eln.primary.controller.methodsetup;

import java.util.Map;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.methodsetup.MethodImportService;



@RestController
public class MethodImportController {
	
	@Autowired
	MethodImportService methodImportService;
	
	@PostMapping(value = "/importMethods")
	public ResponseEntity<Object> importMethod(@Valid @RequestBody Map<String, Object> mapObject) throws Exception {
		return new ResponseEntity<Object>(methodImportService.importMethodByEntity(mapObject), HttpStatus.OK);
	}
}