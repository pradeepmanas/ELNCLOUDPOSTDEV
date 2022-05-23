package com.agaram.eln.primary.controller.sheetManipulation;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
import com.agaram.eln.primary.model.sheetManipulation.Notification;

@RestController
@RequestMapping(value = "/File")
public class FileController {

	@Autowired
	private FileService fileService;

	@PostMapping("/InsertupdateSheet")
	public LSfile InsertupdateSheet(@RequestBody LSfile objfile)throws Exception {
		return fileService.InsertupdateSheet(objfile);
	}

	@PostMapping("/UpdateFilecontent")
	public LSfile UpdateFilecontent(@RequestBody LSfile objfile)throws Exception {
		return fileService.UpdateFilecontent(objfile);
	}

	@PostMapping("/GetSheets")
	public List<LSfile> GetSheets(@RequestBody LSuserMaster objuser)throws Exception {
		return fileService.GetSheets(objuser);
	}

//	@PostMapping("/GetSheetsbyuseronDetailview")
//	public List<LSfile> GetSheetsbyuseronDetailview(@RequestBody LSuserMaster objuser)
//	{
//		return fileService.GetSheetsbyuseronDetailview(objuser);
//	}

	@PostMapping("/GetSheetsbyuseronDetailview")
	public List<Sheettemplateget> GetSheetsbyuseronDetailview(@RequestBody LSuserMaster objuser)throws Exception {
		return fileService.GetSheetsbyuseronDetailview(objuser);
	}

	@PostMapping("/getSheetscount")
	public Map<String, Object> getSheetscount(@RequestBody LSuserMaster objusers)throws Exception {
		return fileService.getSheetscount(objusers);
	}

	@PostMapping("/UpdateFiletest")
	public LSfiletest UpdateFiletest(@RequestBody LSfiletest objtest)throws Exception {
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
	public List<LSsheetworkflow> GetsheetWorkflow(@RequestBody LSsheetworkflow objuser)throws Exception {
		return fileService.GetsheetWorkflow(objuser);
	}

	@PostMapping("/Deletesheetworkflow")
	public Response Deletesheetworkflow(@RequestBody LSsheetworkflow objflow)throws Exception {
		return fileService.Deletesheetworkflow(objflow);
	}

	@PostMapping("/updateworkflowforFile")
	public LSfile updateworkflowforFile(@RequestBody LSfile objfile)throws Exception {
		return fileService.updateworkflowforFile(objfile);
	}

	@RequestMapping(value = "/lockorder")
	public Map<String, Object> lockorder(@RequestBody Map<String, Object> objMap) throws Exception {

		return fileService.lockorder(objMap);
	}

	@RequestMapping(value = "/unlockorder")
	public Map<String, Object> unlockorder(@RequestBody Map<String, Object> objMap) throws Exception {

		return fileService.unlockorder(objMap);
	}

	@PostMapping("/Getfileversions")
	public List<LSfileversion> Getfileversions(@RequestBody LSfile objfile)throws Exception {
		return fileService.Getfileversions(objfile);
	}

	@PostMapping("/Getfileworkflowhistory")
	public List<Lssheetworkflowhistory> Getfilehistory(@RequestBody LSfile objfile)throws Exception {
		return fileService.Getfilehistory(objfile);
	}

	@PostMapping("/GetfileverContent")
	public String GetfileverContent(@RequestBody LSfile objfile)throws Exception {
		return fileService.GetfileverContent(objfile);
	}

	@PostMapping(value = "/getSheetOrder")
	public List<LSlogilablimsorderdetail> getSheetOrder(@RequestBody LSlogilablimsorderdetail objClass)
			throws Exception {
		return fileService.getSheetOrder(objClass);
	}

	@PostMapping(value = "/getfileoncode")
	public LSfile getfileoncode(@RequestBody LSfile objfile)throws Exception {
		return fileService.getfileoncode(objfile);
	}

	@PostMapping(value = "/getfilemasteroncode")
	public Sheettemplateget getfilemasteroncode(@RequestBody LSfile objfile)throws Exception {
		return fileService.getfilemasteroncode(objfile);
	}

	@PostMapping(value = "/Getinitialsheet")
	public Map<String, Object> Getinitialsheet(@RequestBody LSfile objfile)throws Exception {
		return fileService.Getinitialsheet(objfile);
	}

	@PostMapping(value = "/Getremainingsheets")
	public List<Sheettemplateget> Getremainingsheets(@RequestBody LSfile objfile)throws Exception {
		return fileService.Getremainingsheets(objfile);
	}

	@PostMapping("/Insertsharefile")
	public Lsfileshareto Insertsharefile(@RequestBody Lsfileshareto objprotocolordershareto)throws Exception {
		return fileService.Insertsharefile(objprotocolordershareto);
	}

	@PostMapping("/Insertsharefileby")
	public Lsfilesharedby Insertsharefileby(@RequestBody Lsfilesharedby objprotocolordersharedby)throws Exception {
		return fileService.Insertsharefileby(objprotocolordersharedby);
	}

	@PostMapping("/Getfilesharedbyme")
	public List<Lsfilesharedby> Getfilesharedbyme(@RequestBody Lsfilesharedby lsordersharedby)throws Exception {
		return fileService.Getfilesharedbyme(lsordersharedby);
	}

	@PostMapping("/Getfilesharetome")
	public List<Lsfileshareto> Getfilesharetome(@RequestBody Lsfileshareto lsordershareto)throws Exception {
		return fileService.Getfilesharetome(lsordershareto);
	}

	@PostMapping("/Unsharefileby")
	public Lsfilesharedby Unsharefileby(@RequestBody Lsfilesharedby objordershareby)throws Exception {
		return fileService.Unsharefileby(objordershareby);
	}

	@PostMapping("/Unsharefileto")
	public Lsfileshareto Unsharefileto(@RequestBody Lsfileshareto lsordershareto)throws Exception {
		return fileService.Unsharefileto(lsordershareto);
	}

	@PostMapping("/updateSharedFile")
	public Boolean updateSharedFile(@RequestBody Lsfilesharedby lsordersharedby)throws Exception {
		return fileService.updateSharedFile(lsordersharedby);
	}

	@PostMapping("/updateSharedToFile")
	public Boolean updateSharedToFile(@RequestBody Lsfileshareto lsordersharedby)throws Exception {
		return fileService.updateSharedToFile(lsordersharedby);
	}

	@PostMapping("/ValidateNotification")
	public Notification ValidateNotification(@RequestBody Notification objnotification) throws ParseException {
		return fileService.ValidateNotification(objnotification);
	}

	@PostMapping("/UploadLimsFile")
	public Map<String, Object> UploadLimsFile(@RequestParam("file") MultipartFile file,
			@RequestParam("order") Long batchcode, @RequestParam("filename") String filename) throws IOException {
		return fileService.UploadLimsFile(file, batchcode, filename);
	}
}
