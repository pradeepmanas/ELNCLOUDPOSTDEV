package com.agaram.eln.primary.controller.sheetManipulation;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.fetchmodel.gettemplate.Sheettemplateget;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSfiletest;
import com.agaram.eln.primary.model.sheetManipulation.LSfileversion;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.sheetManipulation.Lsfilesharedby;
import com.agaram.eln.primary.model.sheetManipulation.Lsfileshareto;
import com.agaram.eln.primary.model.sheetManipulation.Lssheetworkflowhistory;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.service.sheetManipulation.FileService;

@RestController
@RequestMapping(value = "/File")
public class FileController {

	@Autowired
	private FileService fileService;

	@PostMapping("/InsertupdateSheet")
	public LSfile InsertupdateSheet(@RequestBody LSfile objfile) {
		return fileService.InsertupdateSheet(objfile);
	}

	@PostMapping("/UpdateFilecontent")
	public LSfile UpdateFilecontent(@RequestBody LSfile objfile) {
		return fileService.UpdateFilecontent(objfile);
	}

	@PostMapping("/GetSheets")
	public List<LSfile> GetSheets(@RequestBody LSuserMaster objuser) {
		return fileService.GetSheets(objuser);
	}

//	@PostMapping("/GetSheetsbyuseronDetailview")
//	public List<LSfile> GetSheetsbyuseronDetailview(@RequestBody LSuserMaster objuser)
//	{
//		return fileService.GetSheetsbyuseronDetailview(objuser);
//	}

	@PostMapping("/GetSheetsbyuseronDetailview")
	public List<Sheettemplateget> GetSheetsbyuseronDetailview(@RequestBody LSuserMaster objuser) {
		return fileService.GetSheetsbyuseronDetailview(objuser);
	}

	@PostMapping("/getSheetscount")
	public Map<String, Object> getSheetscount(@RequestBody LSuserMaster objusers) {
		return fileService.getSheetscount(objusers);
	}

	@PostMapping("/UpdateFiletest")
	public LSfiletest UpdateFiletest(@RequestBody LSfiletest objtest) {
//		if(objtest.getObjuser() != null) {
//			
//			LSuserMaster userClass = auditService.CheckUserPassWord(objtest.getObjuser());
//			
//			if(userClass.getObjResponse().getStatus()) {
//				
//				objtest.setLSuserMaster(userClass);
//				
//				return fileService.UpdateFiletest(objtest);
//			}
//			else
//			{
//				objtest.getObjsilentaudit().setComments("Entered invalid username and password");
//				Map<String, Object> map=new HashMap<>();
//				map.put("objsilentaudit",objtest.getObjsilentaudit());
//				map.put("objmanualaudit",objtest.getObjmanualaudit());
//				map.put("objUser",objtest.getObjuser());
//				auditService.AuditConfigurationrecord(map);
//				objtest.setResponse(new Response());
//				objtest.getResponse().setStatus(false);
//				objtest.getResponse().setInformation("ID_VALIDATION");
//				return objtest;
//			}
//			
//		}
		return fileService.UpdateFiletest(objtest);
	}

	@PostMapping("/GetfilesOnTestcode")
	public List<Sheettemplateget> GetfilesOnTestcode(@RequestBody LSfiletest objtest) {
		return fileService.GetfilesOnTestcode(objtest);
	}

	@PostMapping("/GetUnapprovedsheets")
	public List<LSfile> GetUnapprovedsheets(@RequestBody LSuserMaster objuser) {
		return fileService.GetApprovedSheets(0, objuser);
	}

	@PostMapping("/InsertUpdateWorkflow")
	public List<LSworkflow> InsertUpdateWorkflow(@RequestBody LSworkflow[] workflow) {

//		if(lstworkflow.get(0).getObjuser()!= null) {
//			
//			LSuserMaster userClass = auditService.CheckUserPassWord(lstworkflow.get(0).getObjuser());
//            if(userClass.getObjResponse().getStatus()) {
//				
//            	lstworkflow.get(0).setLSuserMaster(userClass);
//				
//            	return fileService.InsertUpdateWorkflow(lstworkflow);
//			}
//			else
//			{
//				lstworkflow.get(0).getObjsilentaudit().setComments("Entered invalid username and password");
//				Map<String, Object> map=new HashMap<>();
//				map.put("objsilentaudit",lstworkflow.get(0).getObjsilentaudit());
//				map.put("objmanualaudit",lstworkflow.get(0).getObjmanualaudit());
//				map.put("objUser",lstworkflow.get(0).getObjuser());
//				auditService.AuditConfigurationrecord(map);
//				lstworkflow.get(0).setResponse(new Response());
//				lstworkflow.get(0).getResponse().setStatus(false);
//				lstworkflow.get(0).getResponse().setInformation("ID_VALIDATION");
//				return lstworkflow;
//			}	
//		}
		return fileService.InsertUpdateWorkflow(workflow);
	}

	@PostMapping("/GetWorkflow")
	public List<LSworkflow> GetWorkflow(@RequestBody LSworkflow objflow) {
		return fileService.GetWorkflow(objflow);
	}

	@PostMapping("/Deleteworkflow")
	public Response Deleteworkflow(@RequestBody LSworkflow objflow) {
		return fileService.Deleteworkflow(objflow);
	}

	@PostMapping("/GetMastersfororders")
	public Map<String, Object> GetMastersfororders(@RequestBody LSuserMaster objuser) {
		return fileService.GetMastersfororders(objuser);
	}

	@PostMapping("/GetMastersforsheetsetting")
	public Map<String, Object> GetMastersforsheetsetting(@RequestBody LSuserMaster objuser) {
		return fileService.GetMastersforsheetsetting(objuser);
	}

	@PostMapping("/InsertUpdatesheetWorkflow")
	public List<LSsheetworkflow> InsertUpdatesheetWorkflow(@RequestBody LSsheetworkflow[] sheetworkflow) {

//		if(lstsheetworkflow.get(0).getObjuser()!= null) {
//			
//			LSuserMaster userClass = auditService.CheckUserPassWord(lstsheetworkflow.get(0).getObjuser());
//            if(userClass.getObjResponse().getStatus()) {
//				
//            	lstsheetworkflow.get(0).setLSuserMaster(userClass);
//				
//            	return fileService.InsertUpdatesheetWorkflow(lstsheetworkflow);
//			}
//			else
//			{
//				lstsheetworkflow.get(0).getObjsilentaudit().setComments("Entered invalid username and password");
//				Map<String, Object> map=new HashMap<>();
//				map.put("objsilentaudit",lstsheetworkflow.get(0).getObjsilentaudit());
//				map.put("objmanualaudit",lstsheetworkflow.get(0).getObjmanualaudit());
//				map.put("objUser",lstsheetworkflow.get(0).getObjuser());
//				auditService.AuditConfigurationrecord(map);
//				lstsheetworkflow.get(0).setResponse(new Response());
//				lstsheetworkflow.get(0).getResponse().setStatus(false);
//				lstsheetworkflow.get(0).getResponse().setInformation("ID_VALIDATION");
//				return lstsheetworkflow;
//			}			
//		}

		return fileService.InsertUpdatesheetWorkflow(sheetworkflow);
	}

	@PostMapping("/GetsheetWorkflow")
	public List<LSsheetworkflow> GetsheetWorkflow(@RequestBody LSsheetworkflow objuser) {
		return fileService.GetsheetWorkflow(objuser);
	}

	@PostMapping("/Deletesheetworkflow")
	public Response Deletesheetworkflow(@RequestBody LSsheetworkflow objflow) {
		return fileService.Deletesheetworkflow(objflow);
	}

	@PostMapping("/updateworkflowforFile")
	public LSfile updateworkflowforFile(@RequestBody LSfile objfile) {
		return fileService.updateworkflowforFile(objfile);
	}

	@RequestMapping(value = "/lockorder")
	public Map<String, Object> lockorder(@RequestBody Map<String, Object> objMap) throws Exception {
//		LoggedUser objuser = new LoggedUser();
//		Response response = new Response();
//		ObjectMapper mapper = new ObjectMapper();
//		LScfttransaction objsilentaudit = new LScfttransaction();
//		LScfttransaction objmanualaudit = new LScfttransaction();
//		
//		if(objMap.containsKey("objuser"))
//		{
//			objuser = mapper.convertValue(objMap.get("objuser"),LoggedUser.class);
//		}
//		
//		if(objMap.containsKey("objsilentaudit"))
//		{
//			objsilentaudit = mapper.convertValue(objMap.get("objsilentaudit"),LScfttransaction.class);
//		}
//		if(objMap.containsKey("objmanualaudit"))
//		{
//			objmanualaudit = mapper.convertValue(objMap.get("objmanualaudit"),LScfttransaction.class);
//		}
//		
//		if(objuser.getsUsername() != null) {
//			
//			LSuserMaster userClass = auditService.CheckUserPassWord(objuser);
//			
//			if(userClass.getObjResponse().getStatus()) {
//				return fileService.lockorder(objMap);
//			}
//			else
//			{
//				objsilentaudit.setComments("Entered invalid username and password");
//				Map<String, Object> map=new HashMap<>();
//				map.put("objsilentaudit",objsilentaudit);
//				map.put("objmanualaudit",objmanualaudit);
//				map.put("objUser",objuser);
//				auditService.AuditConfigurationrecord(map);
//				
//				response.setStatus(false);
//				response.setInformation("ID_VALIDATION");
//				map.put("response",response);
//				return map;
//			}
//			
//		}
		return fileService.lockorder(objMap);
	}

	@RequestMapping(value = "/unlockorder")
	public Map<String, Object> unlockorder(@RequestBody Map<String, Object> objMap) throws Exception {
//		LoggedUser objuser = new LoggedUser();
//		Response response = new Response();
//		ObjectMapper mapper = new ObjectMapper();
//		LScfttransaction objsilentaudit = new LScfttransaction();
//		LScfttransaction objmanualaudit = new LScfttransaction();
//		
//		if(objMap.containsKey("objuser"))
//		{
//			objuser = mapper.convertValue(objMap.get("objuser"),LoggedUser.class);
//		}
//		
//		if(objMap.containsKey("objsilentaudit"))
//		{
//			objsilentaudit = mapper.convertValue(objMap.get("objsilentaudit"),LScfttransaction.class);
//		}
//		if(objMap.containsKey("objmanualaudit"))
//		{
//			objmanualaudit = mapper.convertValue(objMap.get("objmanualaudit"),LScfttransaction.class);
//		}
//		
//		if(objuser.getsUsername() != null) {
//			
//			LSuserMaster userClass = auditService.CheckUserPassWord(objuser);
//			
//			if(userClass.getObjResponse().getStatus()) {
//				return fileService.unlockorder(objMap);
//			}
//			else
//			{
//				objsilentaudit.setComments("Entered invalid username and password");
//				Map<String, Object> map=new HashMap<>();
//				map.put("objsilentaudit",objsilentaudit);
//				map.put("objmanualaudit",objmanualaudit);
//				map.put("objUser",objuser);
//				auditService.AuditConfigurationrecord(map);
//				
//				response.setStatus(false);
//				response.setInformation("ID_VALIDATION");
//				map.put("response",response);
//				return map;
//			}
//			
//		}
		return fileService.unlockorder(objMap);
	}

	@PostMapping("/Getfileversions")
	public List<LSfileversion> Getfileversions(@RequestBody LSfile objfile) {
		return fileService.Getfileversions(objfile);
	}

	@PostMapping("/Getfileworkflowhistory")
	public List<Lssheetworkflowhistory> Getfilehistory(@RequestBody LSfile objfile) {
		return fileService.Getfilehistory(objfile);
	}

	@PostMapping("/GetfileverContent")
	public String GetfileverContent(@RequestBody LSfile objfile) {
		return fileService.GetfileverContent(objfile);
	}

	@PostMapping(value = "/getSheetOrder")
	public List<LSlogilablimsorderdetail> getSheetOrder(@RequestBody LSlogilablimsorderdetail objClass)
			throws Exception {
		return fileService.getSheetOrder(objClass);
	}

	@PostMapping(value = "/getfileoncode")
	public LSfile getfileoncode(@RequestBody LSfile objfile) {
		return fileService.getfileoncode(objfile);
	}

	@PostMapping(value = "/getfilemasteroncode")
	public Sheettemplateget getfilemasteroncode(@RequestBody LSfile objfile) {
		return fileService.getfilemasteroncode(objfile);
	}

	@PostMapping(value = "/Getinitialsheet")
	public Map<String, Object> Getinitialsheet(@RequestBody LSfile objfile) {
		return fileService.Getinitialsheet(objfile);
	}

	@PostMapping(value = "/Getremainingsheets")
	public List<Sheettemplateget> Getremainingsheets(@RequestBody LSfile objfile) {
		return fileService.Getremainingsheets(objfile);
	}

	@PostMapping("/Insertsharefile")
	public Lsfileshareto Insertsharefile(@RequestBody Lsfileshareto objprotocolordershareto) {
		return fileService.Insertsharefile(objprotocolordershareto);
	}

	@PostMapping("/Insertsharefileby")
	public Map<String, Object> Insertsharefileby(@RequestBody Lsfilesharedby objprotocolordersharedby) {
		return fileService.Insertsharefileby(objprotocolordersharedby);
	}

	@PostMapping("/Getfilesharedbyme")
	public List<Lsfilesharedby> Getfilesharedbyme(@RequestBody Lsfilesharedby lsordersharedby) {
		return fileService.Getfilesharedbyme(lsordersharedby);
	}

	@PostMapping("/Getfilesharetome")
	public List<Lsfileshareto> Getfilesharetome(@RequestBody Lsfileshareto lsordershareto) {
		return fileService.Getfilesharetome(lsordershareto);
	}

	@PostMapping("/Unsharefileby")
	public Lsfilesharedby Unsharefileby(@RequestBody Lsfilesharedby objordershareby) {
		return fileService.Unsharefileby(objordershareby);
	}

	@PostMapping("/Unsharefileto")
	public Lsfileshareto Unsharefileto(@RequestBody Lsfileshareto lsordershareto) {
		return fileService.Unsharefileto(lsordershareto);
	}

	@PostMapping("/updateSharedFile")
	public Boolean updateSharedFile(@RequestBody Lsfilesharedby lsordersharedby) {
		return fileService.updateSharedFile(lsordersharedby);
	}

	@PostMapping("/updateSharedToFile")
	public Boolean updateSharedToFile(@RequestBody Lsfileshareto lsordersharedby) {
		return fileService.updateSharedToFile(lsordersharedby);
	}
}
