package com.agaram.eln.primary.controller.helpdocument;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.helpdocument.Helpdocument;
import com.agaram.eln.primary.model.helpdocument.Helptittle;
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
	
	@GetMapping(value="/savenode")
	public Helptittle savenode(@RequestBody Helptittle objhelp)
	{
		return helpdocumentservice.savenode(objhelp);
	}

	@GetMapping(value="/gethelpnodes")
	public List<Helptittle> gethelpnodes(@RequestBody Helptittle objhelp)
	{
		return helpdocumentservice.gethelpnodes(objhelp);
	}
	
	@GetMapping(value="/getdocumentonid")
	public Helpdocument getdocumentonid(@RequestBody Helpdocument objhelp)
	{
		return helpdocumentservice.getdocumentonid(objhelp);
	}
	
	@GetMapping(value="/savedocument")
	public Helpdocument savedocument(@RequestBody Helpdocument objhelp)
	{
		return helpdocumentservice.savedocument(objhelp);
	}
	
	@GetMapping(value="/sortNodesforhelp")
	public List<Helptittle> sortNodesforhelp(@RequestBody List<Helptittle> objhelp)
	{
		return helpdocumentservice.sortNodesforhelp(objhelp);
	}

}
