package com.agaram.eln.primary.controller.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.configuration.ConfigurationService;

@RestController
@RequestMapping(value="/configuration", method=RequestMethod.POST)
public class ConfigurationController {

	@Autowired
	ConfigurationService objConfigurationService;
	

	
	@RequestMapping(value="/getAllConfigurations")
	public Map<String, Object> getConfiguration()throws Exception{
		Map<String, Object> rtnObj = new HashMap<String, Object>();
		try {
			rtnObj = objConfigurationService.getAllConfigurations();
		}catch (Exception e) {
		
		}
		return rtnObj;
	}
	
	@RequestMapping(value="/getConfigurationForFTP")
	public Map<String, Object> getConfigurationForFTP()throws Exception{
		Map<String, Object> rtnObj = new HashMap<String, Object>();
		try {
			rtnObj = objConfigurationService.getConfigurationForFTP();
		}catch (Exception e) {
		
		}
		return rtnObj;
	}
	
	@RequestMapping(value="/AddAllConfiguration")
	public Map<String, Object> AddAllConfiguration(@RequestBody Map<String, Object> argMap)throws Exception{
		Map<String, Object> rtnObj = new HashMap<String, Object>();
		try {
			rtnObj = objConfigurationService.AddAllConfiguration(argMap);
		}catch (Exception e) {
			
		}
		return rtnObj;
	}
	
	@RequestMapping(value = "/testConnection")
	protected Map<String, Object> testConnection(@RequestBody Map<String, Object> argMap)throws Exception{
		Map<String, Object> ObjMap = new HashMap<>();
		try{
			ObjMap = objConfigurationService.testConnection(argMap);
		}catch(Exception e) {
			
			ObjMap.put("status", "fail");
		}
		return ObjMap;
	}
	
	@RequestMapping(value = "/testFTPConnection")
	protected Map<String, Object> testFTPConnection(@RequestBody Map<String, Object> argMap)throws Exception{
		Map<String, Object> ObjMap = new HashMap<>();
		try{
			ObjMap = objConfigurationService.testFTPConnection(argMap);
		}catch(Exception e) {
			
		}
		return ObjMap;
	}
	
	@RequestMapping(value = "/testUrlConnection")
	protected Map<String, Object> testUrlConnection(@RequestBody Map<String, Object> argMap)throws Exception{
		Map<String, Object> ObjMap = new HashMap<>();
		try{
			ObjMap = objConfigurationService.testUrlConnection(argMap);
		}catch(Exception e) {
		
		}
		return ObjMap;
	}
	
}
