package com.agaram.eln.primary.controller.sheetManipulation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSfiletest;
import com.agaram.eln.primary.model.sheetManipulation.LSfileversion;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.service.cfr.AuditService;
import com.agaram.eln.primary.service.sheetManipulation.FileService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/File")
public class FileController {
	
	@Autowired
    private FileService fileService;
	@Autowired
	private AuditService auditService;

	@PostMapping("/InsertupdateSheet")
	public LSfile InsertupdateSheet(@RequestBody LSfile objfile)
	{
         if(objfile.getObjuser() != null) {
			
			LSuserMaster userClass = auditService.CheckUserPassWord(objfile.getObjuser());
			
			if(userClass.getObjResponse().getStatus()) {
				
				objfile.setLSuserMaster(userClass);
				
				return fileService.InsertupdateSheet(objfile);
			}
			else
			{
				objfile.getObjsilentaudit().setComments("Entered invalid username and password");
				Map<String, Object> map=new HashMap<>();
				map.put("objsilentaudit",objfile.getObjsilentaudit());
				map.put("objmanualaudit",objfile.getObjmanualaudit());
				map.put("objUser",objfile.getObjuser());
				auditService.AuditConfigurationrecord(map);
				objfile.setResponse(new Response());
				objfile.getResponse().setStatus(false);
				objfile.getResponse().setInformation("ID_VALIDATION");
				return objfile;
			}
			
		}
		return fileService.InsertupdateSheet(objfile);
	}
	
	@PostMapping("/GetSheets")
	public List<LSfile> GetSheets(@RequestBody LSuserMaster objuser)
	{
		return fileService.GetSheets(objuser);
	}
	
	@PostMapping("/GetSheetsbyuseronDetailview")
	public List<LSfile> GetSheetsbyuseronDetailview(@RequestBody LSuserMaster objuser)
	{
		return fileService.GetSheetsbyuseronDetailview(objuser);
	}
	
	@PostMapping("/UpdateFiletest")
	public LSfiletest UpdateFiletest(@RequestBody LSfiletest objtest)
	{
		if(objtest.getObjuser() != null) {
			
			LSuserMaster userClass = auditService.CheckUserPassWord(objtest.getObjuser());
			
			if(userClass.getObjResponse().getStatus()) {
				
				objtest.setLSuserMaster(userClass);
				
				return fileService.UpdateFiletest(objtest);
			}
			else
			{
				objtest.getObjsilentaudit().setComments("Entered invalid username and password");
				Map<String, Object> map=new HashMap<>();
				map.put("objsilentaudit",objtest.getObjsilentaudit());
				map.put("objmanualaudit",objtest.getObjmanualaudit());
				map.put("objUser",objtest.getObjuser());
				auditService.AuditConfigurationrecord(map);
				objtest.setResponse(new Response());
				objtest.getResponse().setStatus(false);
				objtest.getResponse().setInformation("ID_VALIDATION");
				return objtest;
			}
			
		}
		return fileService.UpdateFiletest(objtest);
	}
	
	@PostMapping("/GetfilesOnTestcode")
	public List<LSfile> GetfilesOnTestcode(@RequestBody LSfiletest objtest)
	{
		return fileService.GetfilesOnTestcode(objtest);
	}
	
	@PostMapping("/InsertUpdateWorkflow")
	public List<LSworkflow> InsertUpdateWorkflow(@RequestBody List<LSworkflow> lstworkflow)
	{
		
		if(lstworkflow.get(0).getObjuser()!= null) {
			
			LSuserMaster userClass = auditService.CheckUserPassWord(lstworkflow.get(0).getObjuser());
            if(userClass.getObjResponse().getStatus()) {
				
            	lstworkflow.get(0).setLSuserMaster(userClass);
				
            	return fileService.InsertUpdateWorkflow(lstworkflow);
			}
			else
			{
				lstworkflow.get(0).getObjsilentaudit().setComments("Entered invalid username and password");
				Map<String, Object> map=new HashMap<>();
				map.put("objsilentaudit",lstworkflow.get(0).getObjsilentaudit());
				map.put("objmanualaudit",lstworkflow.get(0).getObjmanualaudit());
				map.put("objUser",lstworkflow.get(0).getObjuser());
				auditService.AuditConfigurationrecord(map);
				lstworkflow.get(0).setResponse(new Response());
				lstworkflow.get(0).getResponse().setStatus(false);
				lstworkflow.get(0).getResponse().setInformation("ID_VALIDATION");
				return lstworkflow;
			}	
		}
		return fileService.InsertUpdateWorkflow(lstworkflow);
	}
	
	@PostMapping("/GetWorkflow")
	public List<LSworkflow> GetWorkflow(@RequestBody LSworkflow objflow){
		return fileService.GetWorkflow(objflow);
	}
	
	@PostMapping("/Deleteworkflow")
	public Response Deleteworkflow(@RequestBody LSworkflow objflow)
	{
		return fileService.Deleteworkflow(objflow);
	}
	
	@PostMapping("/GetMastersfororders")
	public Map<String, Object> GetMastersfororders(@RequestBody LSuserMaster objuser)
	{
		return fileService.GetMastersfororders(objuser);
	}
	
	@PostMapping("/GetMastersforsheetsetting")
	public Map<String, Object> GetMastersforsheetsetting(@RequestBody LSuserMaster objuser)
	{
		return fileService.GetMastersforsheetsetting(objuser);
	}
	
	@PostMapping("/InsertUpdatesheetWorkflow")
	public List<LSsheetworkflow> InsertUpdatesheetWorkflow(@RequestBody List<LSsheetworkflow> lstsheetworkflow)
	{
		
		if(lstsheetworkflow.get(0).getObjuser()!= null) {
			
			LSuserMaster userClass = auditService.CheckUserPassWord(lstsheetworkflow.get(0).getObjuser());
            if(userClass.getObjResponse().getStatus()) {
				
            	lstsheetworkflow.get(0).setLSuserMaster(userClass);
				
            	return fileService.InsertUpdatesheetWorkflow(lstsheetworkflow);
			}
			else
			{
				lstsheetworkflow.get(0).getObjsilentaudit().setComments("Entered invalid username and password");
				Map<String, Object> map=new HashMap<>();
				map.put("objsilentaudit",lstsheetworkflow.get(0).getObjsilentaudit());
				map.put("objmanualaudit",lstsheetworkflow.get(0).getObjmanualaudit());
				map.put("objUser",lstsheetworkflow.get(0).getObjuser());
				auditService.AuditConfigurationrecord(map);
				lstsheetworkflow.get(0).setResponse(new Response());
				lstsheetworkflow.get(0).getResponse().setStatus(false);
				lstsheetworkflow.get(0).getResponse().setInformation("ID_VALIDATION");
				return lstsheetworkflow;
			}
			
			
		}
		
		return fileService.InsertUpdatesheetWorkflow(lstsheetworkflow);
	}
	
	@PostMapping("/GetsheetWorkflow")
	public List<LSsheetworkflow> GetsheetWorkflow(@RequestBody LSsheetworkflow objuser){
		return fileService.GetsheetWorkflow(objuser);
	}
	
	@PostMapping("/Deletesheetworkflow")
	public Response Deletesheetworkflow(@RequestBody LSsheetworkflow objflow)
	{
		return fileService.Deletesheetworkflow(objflow);
	}
	
	@PostMapping("/updateworkflowforFile")
	public LSfile updateworkflowforFile(@RequestBody LSfile objfile)
	{
		return fileService.updateworkflowforFile(objfile);
	}
	
	@RequestMapping(value = "/lockorder")
	public Map<String, Object> lockorder(@RequestBody Map<String, Object> objMap) throws Exception {
		LoggedUser objuser = new LoggedUser();
		Response response = new Response();
		ObjectMapper mapper = new ObjectMapper();
		LScfttransaction objsilentaudit = new LScfttransaction();
		LScfttransaction objmanualaudit = new LScfttransaction();
		
		if(objMap.containsKey("objuser"))
		{
			objuser = mapper.convertValue(objMap.get("objuser"),LoggedUser.class);
		}
		
		if(objMap.containsKey("objsilentaudit"))
		{
			objsilentaudit = mapper.convertValue(objMap.get("objsilentaudit"),LScfttransaction.class);
		}
		if(objMap.containsKey("objmanualaudit"))
		{
			objmanualaudit = mapper.convertValue(objMap.get("objmanualaudit"),LScfttransaction.class);
		}
		
		if(objuser.getsUsername() != null) {
			
			LSuserMaster userClass = auditService.CheckUserPassWord(objuser);
			
			if(userClass.getObjResponse().getStatus()) {
				return fileService.lockorder(objMap);
			}
			else
			{
				objsilentaudit.setComments("Entered invalid username and password");
				Map<String, Object> map=new HashMap<>();
				map.put("objsilentaudit",objsilentaudit);
				map.put("objmanualaudit",objmanualaudit);
				map.put("objUser",objuser);
				auditService.AuditConfigurationrecord(map);
				
				response.setStatus(false);
				response.setInformation("ID_VALIDATION");
				map.put("response",response);
				return map;
			}
			
		}
		return fileService.lockorder(objMap);
	}
	
	@RequestMapping(value = "/unlockorder")
	public Map<String, Object> unlockorder(@RequestBody Map<String, Object> objMap) throws Exception {
		LoggedUser objuser = new LoggedUser();
		Response response = new Response();
		ObjectMapper mapper = new ObjectMapper();
		LScfttransaction objsilentaudit = new LScfttransaction();
		LScfttransaction objmanualaudit = new LScfttransaction();
		
		if(objMap.containsKey("objuser"))
		{
			objuser = mapper.convertValue(objMap.get("objuser"),LoggedUser.class);
		}
		
		if(objMap.containsKey("objsilentaudit"))
		{
			objsilentaudit = mapper.convertValue(objMap.get("objsilentaudit"),LScfttransaction.class);
		}
		if(objMap.containsKey("objmanualaudit"))
		{
			objmanualaudit = mapper.convertValue(objMap.get("objmanualaudit"),LScfttransaction.class);
		}
		
		if(objuser.getsUsername() != null) {
			
			LSuserMaster userClass = auditService.CheckUserPassWord(objuser);
			
			if(userClass.getObjResponse().getStatus()) {
				return fileService.unlockorder(objMap);
			}
			else
			{
				objsilentaudit.setComments("Entered invalid username and password");
				Map<String, Object> map=new HashMap<>();
				map.put("objsilentaudit",objsilentaudit);
				map.put("objmanualaudit",objmanualaudit);
				map.put("objUser",objuser);
				auditService.AuditConfigurationrecord(map);
				
				response.setStatus(false);
				response.setInformation("ID_VALIDATION");
				map.put("response",response);
				return map;
			}
			
		}
		return fileService.unlockorder(objMap);
	}
	
	@PostMapping("/Getfileversions")
	public List<LSfileversion> Getfileversions(@RequestBody LSfile objfile)
	{
		return fileService.Getfileversions(objfile);
	}
	
	@PostMapping("/GetfileverContent")
	public String GetfileverContent(@RequestBody LSfile objfile)
	{
		return fileService.GetfileverContent(objfile);
	}
	
	@PostMapping(value = "/getSheetOrder")
	public List<LSlogilablimsorderdetail> getSheetOrder(@RequestBody LSlogilablimsorderdetail objClass) throws Exception 
	{	
			return fileService.getSheetOrder(objClass);
	}
	
	@PostMapping(value = "/getfileoncode")
	public LSfile getfileoncode(@RequestBody LSfile objfile)
	{
		return fileService.getfileoncode(objfile);
	}
}
