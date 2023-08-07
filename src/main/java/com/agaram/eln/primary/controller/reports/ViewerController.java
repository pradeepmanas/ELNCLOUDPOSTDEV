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
import com.agaram.eln.primary.model.reports.reportviewer.Cloudreports;
import com.agaram.eln.primary.model.reports.reportviewer.ReportViewerStructure;
import com.agaram.eln.primary.model.reports.reportviewer.Reports;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.service.reports.ViewerService;

@RestController
@RequestMapping(value = "/viewreports", method = RequestMethod.POST)
public class ViewerController {
	
	@Autowired
	private ViewerService viewerservice;
	
	@RequestMapping(value = "/getreportdata")
	protected Map<String, Object> getreportdata(@RequestBody Reports report) throws ServletException, IOException {
		return viewerservice.getreportdata(report);
	}
	
	@RequestMapping(value = "/getordercontent")
	public String getordercontent(@RequestBody LSsamplefile lssample)throws ServletException, IOException
	{
		return viewerservice.getordercontent(lssample);
	}
	
	@RequestMapping(value = "/getremainingorderdata")
	protected Map<String, Object> getremainingorderdata(@RequestBody LSlogilablimsorderdetail objorder) throws ServletException, IOException {
		return viewerservice.getremainingorderdata(objorder);
	}
	
	@RequestMapping("/getfolders")
	public Map<String, Object> getfolders(@RequestBody ReportViewerStructure objdir) throws ServletException, IOException {
		return viewerservice.getfolders(objdir);
	}
	
	@RequestMapping("/insertdirectory")
	public ReportViewerStructure insertdirectory(@RequestBody ReportViewerStructure objdir)throws Exception {
		return viewerservice.insertdirectory(objdir);
	}
	
	@RequestMapping("/insertnewdirectory")
	public ReportViewerStructure insertnewdirectory(@RequestBody ReportViewerStructure objdir)throws Exception {
		return viewerservice.insertnewdirectory(objdir);
	}
	
	@RequestMapping(value = "/savereporttemplate")
	protected Reports savereport(@RequestBody Reports report) throws ServletException, IOException {
		return viewerservice.savereport(report);
	}
	
	@RequestMapping("/getreportsonfolder")
	public List<Reports> getreportsonfolder(@RequestBody ReportViewerStructure reportstructure)throws Exception {
		return viewerservice.getreportsonfolder(reportstructure);
	}
	
	@RequestMapping(value = "/getreportcontent")
	protected Cloudreports getreportcontent(@RequestBody Reports report) throws Exception {
		return viewerservice.getreportcontent(report);
	}
}
