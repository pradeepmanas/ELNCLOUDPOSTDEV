package com.agaram.eln.primary.controller.templates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.service.templates.TemplateMasterService;

@RestController
@RequestMapping(value = "/Templatemaster", method = RequestMethod.POST)
public class TemplateMasterController {
	
	@Autowired
    private TemplateMasterService templatemasterservice; 
	
	@PostMapping("/ImportTemplate")
	public Response ImportTemplate() throws Exception {
		return templatemasterservice.ImportTemplate();
	}

}