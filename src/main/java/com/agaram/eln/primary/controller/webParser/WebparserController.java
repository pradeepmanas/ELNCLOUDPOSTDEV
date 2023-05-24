package com.agaram.eln.primary.controller.webParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.webParser.WebparserService;

@RestController
@RequestMapping(value = "/Webparser", method = RequestMethod.POST)
public class WebparserController {

	@Autowired
	private WebparserService parserService;

	@PostMapping("/ParserFieldSync")
	public String ImportLimsTest(@RequestBody String str) throws Exception {
		return parserService.ImportLimsTest(str);
	}

}
