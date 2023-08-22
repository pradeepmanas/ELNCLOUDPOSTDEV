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

import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.reports.reportdesigner.Cloudreporttemplate;
import com.agaram.eln.primary.model.reports.reportdesigner.ReportDesignerStructure;
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
	protected Cloudreporttemplate gettemplatedata(@RequestBody Reporttemplate template) throws ServletException, IOException {
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
}
