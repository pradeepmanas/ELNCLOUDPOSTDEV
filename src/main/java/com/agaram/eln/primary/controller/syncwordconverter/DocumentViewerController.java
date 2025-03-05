package com.agaram.eln.primary.controller.syncwordconverter;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.reports.reportdesigner.Reporttemplate;
import com.agaram.eln.primary.model.reports.reportviewer.Reports;
import com.agaram.eln.primary.model.syncwordconverter.CustomParameter;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.service.syncwordconverter.DocumentViewerService;

@RestController
@RequestMapping(value = "/documentviewer")
public class DocumentViewerController {

    @Autowired
    private DocumentViewerService documentViewerService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/api/wordeditor/Save")
    public ResponseEntity<Reports> save(@RequestBody Reports data) {
        try {
            Reports savedData = documentViewerService.save(data);
            return new ResponseEntity<>(savedData, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/api/wordeditor/SystemClipboard")
	public String systemClipboard(@RequestBody CustomParameter param) {
		return documentViewerService.systemClipboard(param);
	}
    
    @RequestMapping(value = "/getReportData")
	protected ResponseEntity<Reports> getReportData(@RequestBody Reports template) throws ServletException, IOException {
    	 try {
             Reports savedData = documentViewerService.getReportData(template);
             return new ResponseEntity<>(savedData, HttpStatus.OK);
         } catch (Exception e) {
             return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
         }
	}

    @RequestMapping("/approvereport")
	public ResponseEntity<Reports> approveReport(@RequestBody Reports objReport)throws Exception {
    	try {
            Reports savedData = documentViewerService.approveReport(objReport);
            return new ResponseEntity<>(savedData, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}
    
    @RequestMapping("/getReportsBasedProject")
   	public ResponseEntity<List<Reports>> getReportsBasedProject(@RequestBody LSprojectmaster objClass)throws Exception {
       	try {
               List<Reports> savedData = documentViewerService.getReportsBasedProject(objClass);
               return new ResponseEntity<>(savedData, HttpStatus.OK);
           } catch (Exception e) {
               return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
           }
   	}
    
    @RequestMapping(value = "/getReportTemplateData")
	protected ResponseEntity<Reporttemplate> getReportTemplateData(@RequestBody Reporttemplate template) throws ServletException, IOException {
    	try {
    		Reporttemplate savedData = documentViewerService.getReportTemplateData(template);
            return new ResponseEntity<>(savedData, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}
}