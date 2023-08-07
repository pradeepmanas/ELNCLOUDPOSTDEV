package com.agaram.eln.primary.service.reports;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.methodsetup.Delimiter;
import com.agaram.eln.primary.model.reports.reportdesigner.Cloudreporttemplate;
import com.agaram.eln.primary.model.reports.reportdesigner.ReportDesignerStructure;
import com.agaram.eln.primary.model.reports.reportdesigner.Reporttemplate;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.repository.reports.reportdesigner.CloudreporttemplateRepository;
import com.agaram.eln.primary.repository.reports.reportdesigner.ReportDesignerStructureRepository;
import com.agaram.eln.primary.repository.reports.reportdesigner.ReporttemplateRepository;

@Service
public class DesingerService {
	
	@Autowired
	private ReporttemplateRepository reporttemplaterepository;
	
	@Autowired
	private CloudreporttemplateRepository cloudreporttemplaterepository;
	
	@Autowired
	private ReportDesignerStructureRepository reportDesignerStructureRepository;
	
	
	public Map<String, Object> getreportsource(Map<String, Object> argObj)
	{
		Map<String, Object> rtnObj = new HashMap<String, Object>();
		
		Integer key =  (Integer) argObj.get("key");
		
		List<Map<String, Object>> lstorder = new ArrayList<Map<String, Object>>();
		Map<String, Object> order = new HashMap<String, Object>();
		order.put("Order ID", "");
		order.put("Order Code", 1);
		order.put("Order Type", "");
		order.put("Date Created", new Date());
		order.put("Date Completed", new Date());
		order.put("Version Number", 0);
		order.put("Order Content", "[{\"Sheet Name\":\"\",\"Row Index\":\"1\",\"Column Index\":\"1\",\"Value\":\"\",\"Tag\":{\"Field Key\":\"\",\"Field Name\":\"\",\"Instrument Name\":\"\",\"Method Name\":\"\", \"Label\":\"\", \"Value\":\"\"}}]");
		
		Map<String, Object> project = new HashMap<String, Object>();
		project.put("Project Code", "");
		project.put("Project Name", "");
		if(key != 2)
		{
			order.put("Project", project);
		}
		
		Map<String, Object> sample = new HashMap<String, Object>();
		sample.put("Sample Code", "1");
		sample.put("Sample Name", "sample");
		if(key != 3)
		{
			order.put("Sample", sample);
		}
		
		
		Map<String, Object> task = new HashMap<String, Object>();
		task.put("Task Code", "");
		task.put("Task Name", "");
		if(key != 4)
		{
			order.put("Task", task);
		}
		
		lstorder.add(order);
		if(key == 1)
		{
			rtnObj.put("Sheet Order", lstorder);
		}
		else if(key == 2)
		{
			List<Map<String, Object> > lstproject = new ArrayList<Map<String, Object> >();
			
			project.put("Sheet Order",lstorder);
			lstproject.add(project);
			
			rtnObj.put("Project", lstproject);
		}
		else if(key == 3)
		{
			List<Map<String, Object> > lstsample = new ArrayList<Map<String, Object> >();
			
			sample.put("Sheet Order",lstorder);
			lstsample.add(sample);
			
			rtnObj.put("Sample", lstsample);
		}
		else if(key == 4)
		{
			List<Map<String, Object> > lsttask = new ArrayList<Map<String, Object> >();
			
			task.put("Sheet Order",lstorder);
			lsttask.add(task);
			
			rtnObj.put("Task", lsttask);
		}
		else
		{
			rtnObj.put("template", new ArrayList<LSlogilablimsorderdetail>());
		}
		
		
		return rtnObj;
	}
	
	public Reporttemplate savereporttemplate(Reporttemplate template)
	{
		Cloudreporttemplate objcoludrepo = new Cloudreporttemplate();
		objcoludrepo.setTemplatecontent(template.getTemplatecontent());
		
		 final Optional<Reporttemplate> templateByName = reporttemplaterepository
 				 .findByTemplatenameIgnoreCase(template.getTemplatename());
		
		 if(templateByName.isPresent()) {
			 template.setResponse(new Response());
			 template.getResponse().setStatus(true);
			 template.getResponse().setInformation("ID_EXIST");
			 
			   return template;
		 }
		 else {
		reporttemplaterepository.save(template);
		objcoludrepo.setTemplatecode(template.getTemplatecode());
		
		cloudreporttemplaterepository.save(objcoludrepo);
		 }
		return template;
	}
	
	public Cloudreporttemplate gettemplatedata(Reporttemplate template)
	{
		return cloudreporttemplaterepository.findOne(template.getTemplatecode());
	}
	
	public Map<String, Object> getfolders(ReportDesignerStructure objdir)
	{
		Map<String, Object> rtnObj = new HashMap<String, Object>();
		List<ReportDesignerStructure> lstdir = new ArrayList<ReportDesignerStructure>();
		
		if (objdir.getLstuserMaster() !=null && objdir.getLstuserMaster().size() == 0) {
		
	lstdir = reportDesignerStructureRepository
					.findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
							objdir.getLsuserMaster().getLssitemaster(), 1, objdir.getLsuserMaster(), 2);
		} else {
			lstdir = reportDesignerStructureRepository
					.findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
							objdir.getLsuserMaster().getLssitemaster(), 1, objdir.getLsuserMaster(), 2,
							objdir.getLsuserMaster().getLssitemaster(), 3, objdir.getLstuserMaster());
		}
		rtnObj.put("directory", lstdir);
		return rtnObj;
	}
	
	public ReportDesignerStructure insertdirectory( ReportDesignerStructure objdir)
	{
		Response objResponse = new Response();
		ReportDesignerStructure lstdir = null;
		if (objdir.getDirectorycode() != null) {
			lstdir = reportDesignerStructureRepository.findByDirectorycodeAndParentdircodeAndDirectorynameNot(
					objdir.getDirectorycode(), objdir.getParentdircode(), objdir.getDirectoryname());
		} else {
			lstdir = reportDesignerStructureRepository.findByParentdircodeAndDirectoryname(objdir.getParentdircode(),
					objdir.getDirectoryname());
		}
		if (lstdir != null) {
			objResponse.setStatus(false);
			objResponse.setInformation("IDS_FolderExist");
		} else {
			objResponse.setStatus(true);
			objResponse.setInformation("IDS_FolderAdded");
		}
		objdir.setResponse(objResponse);
		return objdir;
	}
	
	public ReportDesignerStructure insertnewdirectory(ReportDesignerStructure objdir)throws Exception {
		return reportDesignerStructureRepository.save(objdir);
	}
	
	public List<Reporttemplate> gettemplateonfolder(ReportDesignerStructure objdir)throws Exception {
		List<Reporttemplate> lsttemplate = new ArrayList<Reporttemplate>();
		lsttemplate = reporttemplaterepository.findByReportdesignstructure(objdir);
		return lsttemplate;
	}
}
