package com.agaram.eln.primary.controller.methodsetup;

import java.util.Map;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.methodsetup.MethodExportService;

@RestController
public class MethodExportController {
	
	@Autowired
	MethodExportService methodExportService;
	
	@PostMapping(value = "/exportMethods")
	public ResponseEntity<Object> getAllByMethod(@Valid @RequestBody Map<String, Object> mapObject) throws  Exception {
		final int methodKey = (Integer) mapObject.get("methodKey");
		return new ResponseEntity<Object>(methodExportService.getAllByMethodKey(methodKey).getBody(), HttpStatus.OK);
	}
}