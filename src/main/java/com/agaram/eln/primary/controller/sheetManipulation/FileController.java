package com.agaram.eln.primary.controller.sheetManipulation;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
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
import com.agaram.eln.primary.model.protocols.ElnprotocolTemplateworkflow;
import com.agaram.eln.primary.model.protocols.Elnprotocolworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSfiletest;
import com.agaram.eln.primary.model.sheetManipulation.LSfileversion;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetupdates;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.sheetManipulation.Lsfilesharedby;
import com.agaram.eln.primary.model.sheetManipulation.Lsfileshareto;
import com.agaram.eln.primary.model.sheetManipulation.Lssheetworkflowhistory;
import com.agaram.eln.primary.model.sheetManipulation.Notification;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.service.sheetManipulation.FileService;

@RestController
@RequestMapping(value = "/File")
public class FileController {

	@Autowired
	private FileService fileService;

	@PostMapping("/InsertupdateSheet")
	public LSfile InsertupdateSheet(@RequestBody LSfile objfile)throws Exception {
		return fileService.InsertupdateSheet(objfile);
	}
	
	@PostMapping("/updateTemplateOnBatch")
	public LSfile updateTemplateOnBatch(@RequestBody LSfile objfile)throws Exception {
		return fileService.updateTemplateOnBatch(objfile);
	}

	@PostMapping("/UpdateFilecontent")
	public LSfile UpdateFilecontent(@RequestBody LSfile objfile)throws Exception {
		return fileService.UpdateFilecontent(objfile);
	}

	@PostMapping("/GetSheets")
	public List<LSfile> GetSheets(@RequestBody LSuserMaster objuser)throws Exception {
		return fileService.GetSheets(objuser);
	}

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

	@PostMapping("/getApprovedTemplates")
	public Map<String, Object> getApprovedTemplates(@RequestBody LSuserMaster objuser) {
		return fileService.getApprovedTemplates(objuser);
	}
	
	@PostMapping("/GetMastersforordercreate")
	public Map<String, Object> GetMastersforordercreate(@RequestBody LSuserMaster objuser) {
		return fileService.GetMastersforordercreate(objuser);
	}
	
	@PostMapping("/GetMastersforsheetsetting")
	public Map<String, Object> GetMastersforsheetsetting(@RequestBody LSuserMaster objuser) {
		return fileService.GetMastersforsheetsetting(objuser);
	}

	@PostMapping("/InsertUpdatesheetWorkflow")
	public List<LSsheetworkflow> InsertUpdatesheetWorkflow(@RequestBody LSsheetworkflow[] sheetworkflow) {

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
	
	@RequestMapping(value = "/unlockorderOnViewClose")
	public Map<String, Object> unlockorderOnViewClose(@RequestBody Map<String, Object> objMap) throws Exception {

		return fileService.unlockorderOnViewClose(objMap);
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

	@PostMapping(value = "/getFileDetails")
	public LSfile getFileDetails(@RequestBody LSfile objfile)throws Exception {
		return fileService.getFileDetails(objfile);
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
	public void ValidateNotification(@RequestBody Notification objnotification) throws ParseException {
		fileService.ValidateNotification(objnotification);
	}

	@PostMapping("/UploadLimsFile")
	public Map<String, Object> UploadLimsFile(@RequestParam("file") MultipartFile file,
			@RequestParam("order") Long batchcode, @RequestParam("filename") String filename) throws IOException {
		return fileService.UploadLimsFile(file, batchcode, filename);
	}
	
	@PostMapping(value = "/updatefilename")
	public LSfile updatefilename(@RequestBody LSfile objfile)throws Exception {
		return fileService.updatefilename(objfile);
	}
	
	@PostMapping("/GetProtocolOrderWorkflow")
	public List<Elnprotocolworkflow> GetProtocolOrderWorkflow(@RequestBody Elnprotocolworkflow objflow) {
		return fileService.GetProtocolOrderWorkflow(objflow);
	}
	
	@PostMapping("/InsertUpdateprotocolorderWorkflow")
	public List<Elnprotocolworkflow> InsertUpdateprotocolorderWorkflow(@RequestBody Elnprotocolworkflow[] workflow) {

		return fileService.InsertUpdateprotocolorderWorkflow(workflow);
	}
	
	@PostMapping("/Deleteprotocolorderworkflow")
	public Response Deleteprotocolorderworkflow(@RequestBody Elnprotocolworkflow objflow) {
		return fileService.Deleteprotocolorderworkflow(objflow);
	}
	
	@PostMapping("/GetProtocoltempleteWorkflow")
	public List<ElnprotocolTemplateworkflow> GetProtocoltempleteWorkflow(@RequestBody ElnprotocolTemplateworkflow objuser)throws Exception {
		return fileService.GetProtocoltempleteWorkflow(objuser);
	}
	
	@PostMapping("/InsertUpdateprotocoltemplateWorkflow")
	public List<ElnprotocolTemplateworkflow> InsertUpdateprotocoltemplateWorkflow(@RequestBody ElnprotocolTemplateworkflow[] ElnprotocolTemplateworkflow) {

		return fileService.InsertUpdateprotocoltemplateWorkflow(ElnprotocolTemplateworkflow);
	}
	
	@PostMapping("/DeleteprotocolTemplateworkflow")
	public Response DeleteprotocolTemplateworkflow(@RequestBody ElnprotocolTemplateworkflow objflow)throws Exception {
		return fileService.DeleteprotocolTemplateworkflow(objflow);
	}
	
	@PostMapping("/GetprotocoltemplateWorkflow")
	public List<ElnprotocolTemplateworkflow> GetprotocoltemplateWorkflow(@RequestBody ElnprotocolTemplateworkflow objuser)throws Exception {
		return fileService.GetprotocoltemplateWorkflow(objuser);
	}
	@PostMapping("/lsfileRetire")
	public LSfile lsfileRetire(@RequestBody LSfile objfile)throws Exception {
		return fileService.lsfileRetire(objfile);
	}
	@PostMapping("/updatetSheetTemplateTransaction")
	public LSsheetupdates updatetSheetTemplateTransaction(@RequestBody LSsheetupdates objsheetupdates)throws Exception {
		return fileService.updatetSheetTemplateTransaction(objsheetupdates);
	}
	@PostMapping("/GetSheetTransactionDetails")
	public Map<String, Object> GetSheetTransactionDetails(@RequestBody LSfile objfile)throws Exception {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = fileService.GetSheetTransactionDetails(objfile);
		return objMap;
	}
	
	@PostMapping("/Validatesheetcountforfreeuser")
	public boolean Validatesheetcountforfreeuser(@RequestBody LSSiteMaster lssitemaster)
	{
		return fileService.Validatesheetcountforfreeuser(lssitemaster);
	}
	
	@PostMapping("/updateResultForTemplate")
	public void updateResultForTemplate(@RequestBody LSfile lsfile)
	{
		fileService.updateResultForTemplate(lsfile);
	}
	
	@PostMapping("/getApprovedTemplatesWithTask")
	public Map<String, Object> getApprovedTemplatesWithTask(@RequestBody LSuserMaster objuser) {
		return fileService.getApprovedTemplatesWithTask(objuser);
	}
	
	@PostMapping("/getApprovedTemplatesByTask")
	public Map<String, Object> getApprovedTemplatesByTask(@RequestBody LSuserMaster objuser) {
		return fileService.getApprovedTemplatesByTask(objuser);
	}
	
	@PostMapping("/updateResultKeys")
	public void updateResultKeys(@RequestBody LSfile lsfile)
	{
		fileService.updateResultKeys(lsfile);
	}
	
	@PostMapping("/updateTagKeys")
	public void updateTagKeys(@RequestBody LSfile lsfile)
	{
		fileService.updateTagKeys(lsfile);
	}
	
	@PostMapping("/onGetResultTagFromTemplate")
	public Map<String, Object> onGetResultTagFromTemplate(@RequestBody LSfile objOrder) {
		return fileService.onGetResultTagFromTemplate(objOrder);
	}
}