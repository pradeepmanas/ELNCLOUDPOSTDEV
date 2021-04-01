package com.agaram.eln.primary.controller.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.configuration.ConfigurationService;
import com.agaram.eln.primary.service.report.ReportsService;

@RestController
@RequestMapping(value="/configuration", method=RequestMethod.POST)
public class ConfigurationController {

	@Autowired
	ConfigurationService objConfigurationService;
	
	static final Logger logger = Logger.getLogger(ReportsService.class.getName());
	
	@RequestMapping(value="/getAllConfigurations")
	public Map<String, Object> getConfiguration(){
		Map<String, Object> rtnObj = new HashMap<String, Object>();
		try {
			rtnObj = objConfigurationService.getAllConfigurations();
		}catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return rtnObj;
	}
	
	@RequestMapping(value="/getConfigurationForFTP")
	public Map<String, Object> getConfigurationForFTP(){
		Map<String, Object> rtnObj = new HashMap<String, Object>();
		try {
			rtnObj = objConfigurationService.getConfigurationForFTP();
		}catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return rtnObj;
	}
	
	@RequestMapping(value="/AddAllConfiguration")
	public Map<String, Object> AddAllConfiguration(@RequestBody Map<String, Object> argMap){
		Map<String, Object> rtnObj = new HashMap<String, Object>();
		try {
			rtnObj = objConfigurationService.AddAllConfiguration(argMap);
		}catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return rtnObj;
	}
	
	@RequestMapping(value = "/testConnection")
	protected Map<String, Object> testConnection(@RequestBody Map<String, Object> argMap){
		Map<String, Object> ObjMap = new HashMap<>();
		try{
			ObjMap = objConfigurationService.testConnection(argMap);
		}catch(Exception e) {
			logger.error(e.getLocalizedMessage());
			ObjMap.put("status", "fail");
		}
		return ObjMap;
	}
	
	@RequestMapping(value = "/testFTPConnection")
	protected Map<String, Object> testFTPConnection(@RequestBody Map<String, Object> argMap){
		Map<String, Object> ObjMap = new HashMap<>();
		try{
			ObjMap = objConfigurationService.testFTPConnection(argMap);
		}catch(Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return ObjMap;
	}
	
	@RequestMapping(value = "/testUrlConnection")
	protected Map<String, Object> testUrlConnection(@RequestBody Map<String, Object> argMap){
		Map<String, Object> ObjMap = new HashMap<>();
		try{
			ObjMap = objConfigurationService.testUrlConnection(argMap);
		}catch(Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return ObjMap;
	}
	
}
