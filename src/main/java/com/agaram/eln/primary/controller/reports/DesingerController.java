package com.agaram.eln.primary.controller.reports;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.instrumentDetails.LSprotocolfolderfiles;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolorderstructure;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.reports.reportdesigner.ReportDesignerStructure;
import com.agaram.eln.primary.model.reports.reportdesigner.ReportTemplateVersion;
import com.agaram.eln.primary.model.reports.reportdesigner.Reporttemplate;
import com.agaram.eln.primary.service.reports.DesingerService;

@RestController
@RequestMapping(value = "/designereports", method = RequestMethod.POST)
public class DesingerController {

	@Autowired
	private DesingerService desingerservice;
	
	@RequestMapping(value = "/getreportsource")
	protected Map<String, Object> getreportsource(@RequestBody Map<String, Object> argObj) throws ServletException, IOException {
		return desingerservice.getreportsource(argObj);
	}
	
	@RequestMapping(value = "/savereporttemplate")
	protected Reporttemplate savereporttemplate(@RequestBody Reporttemplate template) throws ServletException, IOException {
		return desingerservice.savereporttemplate(template);
	}
	
	@RequestMapping(value = "/gettemplatedata")
	protected Reporttemplate gettemplatedata(@RequestBody Reporttemplate template) throws ServletException, IOException {
		return desingerservice.gettemplatedata(template);
	}
	
	@RequestMapping("/getfolders")
	public Map<String, Object> getfolders(@RequestBody ReportDesignerStructure objdir) throws ServletException, IOException {
		return desingerservice.getfolders(objdir);
	}
	
	@RequestMapping("/insertdirectory")
	public ReportDesignerStructure insertdirectory(@RequestBody ReportDesignerStructure objdir)throws Exception {
		return desingerservice.insertdirectory(objdir);
	}
	
	@RequestMapping("/insertnewdirectory")
	public ReportDesignerStructure insertnewdirectory(@RequestBody ReportDesignerStructure objdir)throws Exception {
		return desingerservice.insertnewdirectory(objdir);
	}
	
	@RequestMapping("/gettemplateonfolder")
	public List<Reporttemplate> gettemplateonfolder(@RequestBody ReportDesignerStructure objdir)throws Exception {
		return desingerservice.gettemplateonfolder(objdir);
	}
	
	@RequestMapping("/getordersonreport")
	public Map<String,Object> getordersonreport (@RequestBody Map<String, Object> objMap){
		return desingerservice.getordersonreport(objMap);
	}
	
	@RequestMapping("/approvereporttemplate")
	public Reporttemplate approvereporttemplate(@RequestBody Reporttemplate objdir)throws Exception {
		return desingerservice.approvereporttemplate(objdir);
	}
	
	@RequestMapping("/gettemplateonfoldermapping")
	public List<Reporttemplate> gettemplateonfoldermapping(@RequestBody ReportDesignerStructure objdir)throws Exception {
		return desingerservice.gettemplateonfoldermapping(objdir);
	}
	
	@RequestMapping("/updatereporttemplatemapping")
	public Reporttemplate updatereporttemplatemapping(@RequestBody Reporttemplate objdir)throws Exception {
		return desingerservice.updatereporttemplatemapping(objdir);
	}
	
	@RequestMapping("/onGetReportTemplateBasedOnProject")
	public Map<String, Object> onGetReportTemplateBasedOnProject(@RequestBody Map<String, Object> objMap)throws Exception {
		return desingerservice.onGetReportTemplateBasedOnProject(objMap);
	}
	
	@RequestMapping(value = "/gettemplateversiondata")
	protected ReportTemplateVersion gettemplateversiondata(@RequestBody ReportTemplateVersion template) throws ServletException, IOException {
		return desingerservice.gettemplateversiondata(template);
	}
	
	@RequestMapping("/UpdateFolderforReportDesignerStructure")
	public ReportDesignerStructure UpdateFolderforReportDesignerStructure(@RequestBody ReportDesignerStructure folders)throws Exception
	{
		return desingerservice.UpdateFolderforReportDesignerStructure(folders);
	}
	
	@RequestMapping("/DeletedirectoriesonReportDesigner")
	public List<ReportDesignerStructure> DeletedirectoriesonReportDesigner(@RequestBody ReportDesignerStructure[] directories)throws Exception
	{
		return desingerservice.DeletedirectoriesonReportDesigner(directories);
	}
	
	@RequestMapping("/getMoveDirectoryonReportDesigner")
	public ReportDesignerStructure getMoveDirectoryonReportDesigner(@RequestBody ReportDesignerStructure objdir)throws Exception {
		return desingerservice.getMoveDirectoryonReportDesigner(objdir);
	}
	
	@RequestMapping("/UpdateReporttemplate")
	public List<Reporttemplate> UpdateReporttemplate(@RequestBody Reporttemplate[] files)throws Exception
	{
		return desingerservice.UpdateReporttemplate(files);
	}
	
	@RequestMapping("/UpdateReporttemplateforsinglefile")
	public Reporttemplate UpdateReporttemplateforsinglefile(@RequestBody Reporttemplate file)throws Exception
	{
		return desingerservice.UpdateReporttemplateforsinglefile(file);
	}
	
	@RequestMapping("/MovedirectoryonReporttemplate")
	public ReportDesignerStructure MovedirectoryonReporttemplate(@RequestBody ReportDesignerStructure directory)throws Exception
	{
		return desingerservice.MovedirectoryonReporttemplate(directory);
	}
}
