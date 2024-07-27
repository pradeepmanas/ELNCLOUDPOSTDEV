package com.agaram.eln.primary.service.syncwordconverter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.reports.reportviewer.Reports;
import com.agaram.eln.primary.model.reports.reportviewer.ReportsVersion;
import com.agaram.eln.primary.repository.reports.reportviewer.ReportsRepository;
import com.agaram.eln.primary.repository.reports.reportviewer.ReportsVersionRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.protocol.Commonservice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.gridfs.GridFSDBFile;

@Service
public class DocumentViewerService {

    @Autowired
    private Commonservice commonservice;

    @Autowired
    private ReportsRepository reportsRepository;
    
    @Autowired
    private ReportsVersionRepository reportsVersionRepository;
    
    @Autowired
    private CloudFileManipulationservice objCloudFileManipulationservice;
    
    @Autowired
    private GridFsTemplate gridFsTemplate;
    
    @Autowired
    private MongoTemplate mongoTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String convertObjectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
    
    public Reports save(Reports data) throws Exception {
        Response response = new Response();
        boolean isNewTemplate = data.getReportcode() == null;
        String jsonContent = convertObjectToJson(data.getReporttemplatecontent());
        byte[] documentBytes = jsonContent.getBytes(StandardCharsets.UTF_8);
        String uniqueDocumentName = data.getReportname() + "_" + UUID.randomUUID().toString() + ".json";

        try {
            if (data.getIsmultitenant() == 1) {
                if (isNewTemplate) {
                    handleNewTemplate(data, documentBytes, uniqueDocumentName, response);
                } else {
                    handleExistingTemplate(data, documentBytes, response);
                }
            } else {
                saveToGridFSonReport(data, documentBytes);
                updateReportTimestamps(data, isNewTemplate);
                response.setStatus(true);
                reportsRepository.save(data);
            }
            data.setResponse(response);
            return data;
        } catch (IOException e) {
            throw new IOException("Failed to save JSON file to GridFS: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new Exception("Failed to save document: " + e.getMessage(), e);
        }
    }

    private void handleNewTemplate(Reports data, byte[] documentBytes, String uniqueDocumentName, Response response) throws Exception {
        Optional<Reports> existingReportOpt = Optional.ofNullable(reportsRepository.findTopByReportnameIgnoreCaseAndSitemaster(data.getReportname(), data.getSitemaster()));
        
        if (!existingReportOpt.isPresent()) {
            data = updateReportContent(data, documentBytes, uniqueDocumentName);
            setReportCreationData(data);
            response.setStatus(true);
            reportsRepository.save(data);
            createReportVersion(data, documentBytes, uniqueDocumentName);
        } else {
            response.setStatus(false);
            response.setInformation("IDS_MSG_ALREADY");
        }
    }

    private void handleExistingTemplate(Reports data, byte[] documentBytes, Response response) throws Exception {
        Reports existingReport = reportsRepository.findOne(data.getReportcode());
        String uniqueDocumentName = existingReport.getReportname() + "_" + UUID.randomUUID().toString() + ".json";

        data = updateReportContent(data, documentBytes, uniqueDocumentName);
        
        data.setDatemodified(commonfunction.getCurrentUtcTime());
        response.setStatus(true);
        
        if(data.isIsnewversion()) {
        	data.setVersionno(existingReport.getVersionno() + 1);
        	createReportVersion(data, documentBytes, uniqueDocumentName);
        }else {
        	ReportsVersion version = reportsVersionRepository.findByVersionnoAndReportcode(existingReport.getVersionno(),existingReport.getReportcode());
        	updateReportVersionContent(version, documentBytes, uniqueDocumentName);
        }
        
        reportsRepository.save(data);
    }
    
    private void setReportCreationData(Reports data) throws ParseException {
        data.setDatecreated(commonfunction.getCurrentUtcTime());
        data.setDatemodified(commonfunction.getCurrentUtcTime());
        data.setVersionno(1);
    }

    private void createReportVersion(Reports data, byte[] documentBytes, String uniqueDocumentName) throws Exception {
    	
    	ReportsVersion version = new ReportsVersion();
        version.setReportcode(data.getReportcode());
        version.setContainerstored(data.getContainerstored());
        version.setCreatedby(data.getCreatedby().getUsercode());
        version.setDatecreated(data.getDatecreated());
        version.setFileextention(data.getFileextention());
        version.setFileuid(data.getFileuid());
        version.setSitecode(data.getSitemaster().getSitecode());
        version.setVersionno(data.getVersionno());
        
        updateReportVersionContent(version, documentBytes, uniqueDocumentName);
        
        reportsVersionRepository.save(version);
    }

    private void updateReportVersionContent(ReportsVersion version, byte[] documentBytes, String uniqueDocumentName) throws Exception {
        commonservice.uploadToAzureBlobStorage(documentBytes, version, uniqueDocumentName);
    }

    private Reports updateReportContent(Reports data, byte[] documentBytes, String uniqueDocumentName) throws Exception {
        return commonservice.uploadToAzureBlobStorage(documentBytes, data, uniqueDocumentName);
    }

    private void updateReportTimestamps(Reports data, boolean isNewTemplate) throws ParseException {
        if (isNewTemplate) {
            data.setDatecreated(commonfunction.getCurrentUtcTime());
        }
        data.setDatemodified(commonfunction.getCurrentUtcTime());
    }

    private void saveToGridFSonReport(Reports data, byte[] documentBytes) throws IOException {
        try {
            String reportName = "report_" + data.getFileuid();
            gridFsTemplate.delete(new Query(Criteria.where("reportname").is(reportName)));
            gridFsTemplate.store(new ByteArrayInputStream(documentBytes), reportName, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw new IOException("Failed to upload file to GridFS: " + e.getMessage(), e);
        }
    }

    public Reports getReportData(Reports objReport) {
        try {
            if (objReport.getIsmultitenant() == 1 || objReport.getIsmultitenant() == 2) {
                String tenant = Optional.ofNullable(TenantContext.getCurrentTenant()).orElse("defaultTenant");
                if (objReport.getIsmultitenant() == 2) {
                    tenant = "freeusers";
                    objReport.setReporttemplatecontent("");
                    return objReport;
                }
                String containerName = tenant + "report";
                String documentName = objReport.getFileuid();

                if (objReport.getIsmultitenant() == 1) {
                    Optional<byte[]> documentBytesOpt = Optional.ofNullable(
                        objCloudFileManipulationservice.retrieveCloudReportFile(containerName, documentName)
                    );

                    if (documentBytesOpt.isPresent()) {
                        byte[] documentBytes = documentBytesOpt.get();
                        String jsonContent = new String(documentBytes, StandardCharsets.UTF_8);
                        objReport.setReporttemplatecontent(jsonContent);
                        
//                        @SuppressWarnings("unchecked")
//						Map<String, String> map = objectMapper.readValue(jsonContent, LinkedHashMap.class);
//                        
//                        objReport.setReporttemplatecontent(map.getOrDefault("content", ""));
//                        objReport.setKeystorevariable(map.getOrDefault("keystorevariable", ""));
                        
                    } else {
                        System.out.println("Failed to retrieve JSON content from Azure Blob Storage.");
                    }
                } else {
                    String jsonContent = retrieveJsonContentFromGridFS(objReport);
                    objReport.setReporttemplatecontent(jsonContent);
                }
            }
        } catch (Exception e) {
            System.out.println("Error retrieving report data -->" + e);
        }
        return objReport;
    }

    private String retrieveJsonContentFromGridFS(Reports objReport) throws IOException {
        String jsonContent = "";
        GridFSDBFile largefile = gridFsTemplate
            .findOne(new Query(Criteria.where("reportname").is("report_" + objReport.getFileuid())));

        if (largefile == null) {
            largefile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is("report_" + objReport.getFileuid())));
        }

        if (largefile != null) {
            jsonContent = new BufferedReader(new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));
        } else {
            Reports report = mongoTemplate.findById(objReport.getFileuid(), Reports.class);
            if (report != null) {
                jsonContent = report.getReporttemplatecontent();
            }
        }
        return jsonContent;
    }
	
	public Reports approveReport(Reports objReport) {
	    
		Optional<Reports> optionalReport = Optional.ofNullable(reportsRepository.findOne(objReport.getReportcode()));

	    return optionalReport.map(report -> {
	        report.setTemplatetype(2);
	        try {
	            report.setCompleteddate(commonfunction.getCurrentUtcTime());
	        } catch (Exception e) {
	        }
	        return reportsRepository.save(report);
	    }).orElseThrow(() -> new IllegalArgumentException("Report not found with code: " + objReport.getReportcode()));
	}
}
