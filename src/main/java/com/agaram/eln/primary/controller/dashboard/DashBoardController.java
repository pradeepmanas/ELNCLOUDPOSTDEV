package com.agaram.eln.primary.controller.dashboard;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.fetchmodel.getmasters.Repositorymaster;
import com.agaram.eln.primary.fetchmodel.getorders.LogilabOrdermastersh;
import com.agaram.eln.primary.fetchmodel.getorders.LogilabProtocolOrderssh;
import com.agaram.eln.primary.fetchmodel.getorders.Logilabordermaster;
import com.agaram.eln.primary.model.cfr.LSactivity;
import com.agaram.eln.primary.model.dashboard.LsActiveWidgets;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.service.dashboard.DashBoardService;

@RestController
@RequestMapping(value = "/DashBoard", method = RequestMethod.POST)
public class DashBoardController {
	@Autowired
	private DashBoardService dashBoardService;

	@PostMapping("/Getdashboarddetails")
	public Map<String, Object> Getdashboarddetails(@RequestBody LSuserMaster objuser)throws Exception {
		if (objuser.getObjuser() != null && objuser.getObjuser().getFromdate() != null
				&& objuser.getObjuser().getTodate() != null && objuser.getObjuser().getFiltertype() != null) {
			return dashBoardService.Getdashboarddetailsonfilters(objuser);
		} else {
			return dashBoardService.Getdashboarddetails(objuser);
		}
	}

	@PostMapping("/GetActivitiesonLazy")
	public List<LSactivity> GetActivitiesonLazy(@RequestBody LSactivity objactivities)throws Exception {
		return dashBoardService.GetActivitiesonLazy(objactivities);
	}

	@PostMapping("/Getdashboardordercount")
	public Map<String, Object> Getdashboardordercount(@RequestBody LSuserMaster objuser)throws Exception {
		return dashBoardService.Getdashboardordercount(objuser);
	}

	@PostMapping("/Getdashboardorders")
	public Map<String, Object> Getdashboardorders(@RequestBody LSuserMaster objuser)throws Exception {

		if (objuser.getObjuser().getOrderfor() == 1) {
			return dashBoardService.Getdashboardorders(objuser);
		} else {
			return dashBoardService.Getdashboardprotocolorders(objuser);
		}

	}

	@PostMapping("/Getdashboardparameters")
	public Map<String, Object> Getdashboardparameters(@RequestBody LSuserMaster objuser)throws Exception {
		return dashBoardService.Getdashboardparameters(objuser);
	}

	@PostMapping("/Getdashboardactivities")
	public Map<String, Object> Getdashboardactivities(@RequestBody LSuserMaster objuser)throws Exception {
		return dashBoardService.Getdashboardactivities(objuser);
	}

	@PostMapping("/Getdashboardsheets")
	public Map<String, Object> Getdashboardsheets(@RequestBody LSuserMaster objuser)throws Exception {
		if (objuser.getObjuser().getTemplatefor() == 1) {
			return dashBoardService.Getdashboardsheets(objuser);
		} else {
			return dashBoardService.Getdashboardprotocoltemplate(objuser);
		}
	}
	@PostMapping("/Getglobalsearchfortemplate")
	public Map<String, Object> Getglobalsearchfortemplate(@RequestBody LSuserMaster objuser)throws Exception {
		if (objuser.getObjuser().getTemplatefor() == 1) {
			return dashBoardService.Getglobalsearchforsheettemplate(objuser);
		} else {
			return dashBoardService.Getglobalsearchforprotocoltemplate(objuser);
		}
	}
	
	@PostMapping("/getDashboardSheetCount")
	public Map<String, Object> getDashboardSheetCount(@RequestBody LSuserMaster objuser)throws Exception {
		if (objuser.getObjuser().getTemplatefor() == 1) {
			return dashBoardService.getCountFromSheetTemplate(objuser);
		} else {
			return dashBoardService.getCountFromProtocolTemplate(objuser);
		}
	}
	
	@PostMapping("/Getordersharebyme")
	public Map<String, Object> Getordersharebyme(@RequestBody LSuserMaster objuser)throws Exception {
		return dashBoardService.Getordersharebyme(objuser);
	}
	
	@PostMapping("/Getordersharetome")
	public Map<String, Object> Getordersharetome(@RequestBody LSuserMaster objuser)throws Exception {
		return dashBoardService.Getordersharetome(objuser);
	}
	
	@PostMapping("/Getorder")
	public Logilabordermaster Getorder(@RequestBody LSlogilablimsorderdetail objorder)throws Exception
	{
		return dashBoardService.Getorder(objorder);
	}
	
	@PostMapping("/Getordersinuserworkflow")
	public Map<String, Object> Getordersinuserworkflow(@RequestBody LSuserMaster objuser)throws Exception
	{
		return dashBoardService.Getordersinuserworkflow(objuser);
	}
	
	@RequestMapping("/Getallrepositories")
	public List<Repositorymaster> Getallrepositories(@RequestBody LSuserMaster objuser)throws Exception
	{
		return dashBoardService.Getallrepositories(objuser);
	}
	
	@RequestMapping("/Getapprovedsheet")
	public Map<String,Object> Getapprovedsheet(@RequestBody Integer[] lsfile)throws Exception
	{
		return dashBoardService.Getapprovedsheet(lsfile);
	}
	
	@PostMapping("/Getglobalsearchorders")
	public Map<String, Object> Getglobalsearchorders(@RequestBody Map<String, Object> obj)throws Exception {
			return dashBoardService.Getglobalsearchorders(obj);
	}
	
	@RequestMapping("/Getglobalsearchfolder")
	public Map<String, Object> Getglobalsearchfolder(@RequestBody LSuserMaster objusermaster)throws Exception {
		return dashBoardService.Getglobalsearchfolder(objusermaster);
	}
	@RequestMapping("/Getglobalsearchforfile")
	public Map<String, Object> Getglobalsearchforfile(@RequestBody LSuserMaster objusermaster)throws Exception {
		return dashBoardService.Getglobalsearchforfile(objusermaster);
	}
	
	@RequestMapping("/Getglobalsearchforlogbook")
	public Map<String, Object> Getglobalsearchforlogbook(@RequestBody LSuserMaster objusermaster)throws Exception
	{
		return dashBoardService.Getglobalsearchforlogbook(objusermaster);
	}
	@RequestMapping("/Getglobalsearchforinventory")
	public Map<String, Object> Getglobalsearchforinventory(@RequestBody LSuserMaster objusermaster)throws Exception
	{
		return dashBoardService.Getglobalsearchforinventory(objusermaster);
	}
	@RequestMapping("/getActivewidgetsdata")
	public List<LsActiveWidgets> getActivewidgetsdata(@RequestBody LSuserMaster objusermaster)throws Exception
	{
		return dashBoardService.getActivewidgetsdata(objusermaster);
	}
	
	@RequestMapping("/Onsaveactivewidgetsdata")
	public LsActiveWidgets Onsaveactivewidgetsdata(@RequestBody LsActiveWidgets activeWidgets)throws Exception
	{
		return dashBoardService.Onsaveactivewidgetsdata(activeWidgets);
	}
	
	@PostMapping("/Getorderonproject")
	public List<LogilabOrdermastersh> Getorderonproject(@RequestBody LSprojectmaster objproject)throws Exception
	{
		return dashBoardService.Getorderonproject(objproject);
	}
	
	@PostMapping("/Getprotocolorderonproject")
	public List<LogilabProtocolOrderssh> Getprotocolorderonproject(@RequestBody LSprojectmaster objproject)throws Exception
	{
		return dashBoardService.Getprotocolorderonproject(objproject);
	}
	
	@PostMapping("/Getprojectsonuser")
	public List<LSprojectmaster> Getprojectsonuser(@RequestBody LSuserMaster objuser)throws Exception {
		return dashBoardService.Getprojectsonuser(objuser);
	}
	
	@PostMapping("/GetInitprojectsonuser")
	public List<LSprojectmaster> GetInitprojectsonuser(@RequestBody LSuserMaster objuser)throws Exception {
		return dashBoardService.GetInitprojectsonuser(objuser);
	}
	
	@PostMapping("/Getprojectscountonuser")
	public Map<String, Object> Getprojectscountonuser(@RequestBody LSuserMaster objuser) {
		return dashBoardService.Getprojectscountonuser(objuser);
	}
	@PostMapping("/Getinprogressprojectsonuser")
	public List<LSprojectmaster> Getinprogressprojectsonuser(@RequestBody LSuserMaster objuser) throws ParseException {
		return dashBoardService.Getinprogressprojectsonuser(objuser);
	}
	@PostMapping("/Getcomprojectsonuser")
	public List<LSprojectmaster> Getcomprojectsonuser(@RequestBody LSuserMaster objuser)throws Exception {
		return dashBoardService.Getcomprojectsonuser(objuser);
	}
	@PostMapping("/GetOverdueprojectsonuser")
	public List<LSprojectmaster> GetOverdueprojectsonuser(@RequestBody LSuserMaster objuser)throws Exception {
		return dashBoardService.GetOverdueprojectsonuser(objuser);
	}
	@PostMapping("/Getintprojectscountonuser")
	public Map<String, Object> Getintprojectscountonuser(@RequestBody LSuserMaster objuser) throws ParseException {
		return dashBoardService.Getintprojectscountonuser(objuser);
	}
	@PostMapping("/Getinprojectscountonuser")
	public Map<String, Object> Getinprojectscountonuser(@RequestBody LSuserMaster objuser) throws ParseException {
		return dashBoardService.Getinprojectscountonuser(objuser);
	}
	@PostMapping("/Getcomprojectscountonuser")
	public Map<String, Object> Getcomprojectscountonuser(@RequestBody LSuserMaster objuser) throws ParseException {
		return dashBoardService.Getcomprojectscountonuser(objuser);
	}
	@PostMapping("/Getoverdueprojectscountonuser")
	public Map<String, Object> Getoverdueprojectscountonuser(@RequestBody LSuserMaster objuser) throws ParseException {
		return dashBoardService.Getoverdueprojectscountonuser(objuser);
	}
	@PostMapping("/Getprojectsoverdueonuser")
	public List<LSprojectmaster> Getprojectsoverdueonuser(@RequestBody LSuserMaster objuser)throws Exception {
		return dashBoardService.Getprojectsoverdueonuser(objuser);
	}
	@PostMapping("/Getprojectsoverduecountonuser")
	public Map<String, Object> Getprojectsoverduecountonuser(@RequestBody LSuserMaster objuser) throws ParseException {
		return dashBoardService.Getprojectsoverduecountonuser(objuser);
	}
}
