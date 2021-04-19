package com.agaram.eln.primary.controller.helpdocument;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.agaram.eln.primary.service.helpdocument.helpdocumentservice;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;



@RestController
@RequestMapping(value = "/helpdocument",method = RequestMethod.POST)
public class Documentcontroller {

	@Autowired
	helpdocumentservice helpdocumentservice;
	
	@RequestMapping(value="/adddocument")
	public Map<String, Object> adddocument(@RequestBody Map<String, Object> argObj ) {
		
		return helpdocumentservice.adddocument(argObj);
//		return argObj;
		
	}
	
	@GetMapping(value="/getdocumentcontent")
	public Map<String, Object> getdocumentcontent(HttpServletRequest request){
		
		return helpdocumentservice.getdocumentcontent();
	
		
	}


}
