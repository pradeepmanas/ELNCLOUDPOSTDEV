package com.agaram.eln.primary.service.syncwordconverter;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.spi.IIORegistry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.reports.reportdesigner.ReportTemplateMapping;
import com.agaram.eln.primary.model.reports.reportdesigner.Reporttemplate;
import com.agaram.eln.primary.model.reports.reportviewer.Reports;
import com.agaram.eln.primary.model.reports.reportviewer.ReportsVersion;
import com.agaram.eln.primary.model.syncwordconverter.CustomParameter;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.repository.reports.reportdesigner.ReportTemplateMappingRepository;
import com.agaram.eln.primary.repository.reports.reportviewer.ReportsRepository;
import com.agaram.eln.primary.repository.reports.reportviewer.ReportsVersionRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.protocol.Commonservice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.gridfs.GridFSDBFile;
import com.syncfusion.ej2.wordprocessor.FormatType;
import com.syncfusion.ej2.wordprocessor.MetafileImageParsedEventArgs;
import com.syncfusion.ej2.wordprocessor.MetafileImageParsedEventHandler;
import com.syncfusion.ej2.wordprocessor.WordProcessorHelper;
import com.syncfusion.javahelper.system.collections.generic.ListSupport;
import com.syncfusion.javahelper.system.io.StreamSupport;
import com.syncfusion.javahelper.system.reflection.AssemblySupport;
import com.twelvemonkeys.imageio.plugins.tiff.TIFFImageReaderSpi;

@Service
public class DocumentViewerService {

    @Autowired
    private Commonservice commonservice;

    @Autowired
    private ReportsRepository reportsRepository;
    
    @Autowired
    private ReportsVersionRepository reportsVersionRepository;
    
    @Autowired
    private ReportTemplateMappingRepository reportTemplateMappingRepository;
    
    @Autowired
    private CloudFileManipulationservice objCloudFileManipulationservice;
    
    @Autowired
    private GridFsTemplate gridFsTemplate;

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
        	 if (data.getIsmultitenant() != 2) {
                 if (isNewTemplate) {
                     handleNewTemplate(data, documentBytes, uniqueDocumentName, response);
                 } else {
                     handleExistingTemplate(data, documentBytes, response);
                 }
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
        	
        	data.setDatecreated(commonfunction.getCurrentUtcTime());
        	data.setDatemodified(commonfunction.getCurrentUtcTime());
        	reportsRepository.save(data);
        	
        	if(data.getIsmultitenant() == 1) {
                data = updateReportContent(data, documentBytes, uniqueDocumentName);
        	}else {
        		saveToGridFSonReport(data, documentBytes);
        	}
        	
            response.setStatus(true);
            
            createReportVersion(data, documentBytes, uniqueDocumentName);
        } else {
            response.setStatus(false);
            response.setInformation("IDS_MSG_ALREADY");
        }
    }

    private void handleExistingTemplate(Reports data, byte[] documentBytes, Response response) throws Exception {
        Reports existingReport = reportsRepository.findOne(data.getReportcode());
        String uniqueDocumentName = existingReport.getReportname() + "_" + UUID.randomUUID().toString() + ".json";

        if(data.getIsmultitenant() == 1) {
        	 data = updateReportContent(data, documentBytes, uniqueDocumentName);
        }else {
        	saveToGridFSonReport(data, documentBytes);
        }       
        
        data.setDatemodified(commonfunction.getCurrentUtcTime());
        data.setModifieddate(commonfunction.getCurrentUtcTime());

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
        
        if(data.getIsmultitenant() == 1) {
        	updateReportVersionContent(version, documentBytes, uniqueDocumentName);
        }else {
        	saveToGridFSonReportVersion(version,documentBytes);
        }        
        reportsVersionRepository.save(version);
    }

    private void updateReportVersionContent(ReportsVersion version, byte[] documentBytes, String uniqueDocumentName) throws Exception {
        commonservice.uploadToAzureBlobStorage(documentBytes, version, uniqueDocumentName);
    }

    private Reports updateReportContent(Reports data, byte[] documentBytes, String uniqueDocumentName) throws Exception {
        return commonservice.uploadToAzureBlobStorage(documentBytes, data, uniqueDocumentName);
    }
    
    private void saveToGridFSonReportVersion(ReportsVersion data, byte[] documentBytes) throws IOException {
        try {        	
        	String reportName = "reportversion_" + data.getReportversioncode();
        	GridFSDBFile file = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(reportName)));
    	    if (file != null) {
    	        gridFsTemplate.delete(new Query(Criteria.where("filename").is(reportName)));
    	    }
    	    gridFsTemplate.store(new ByteArrayInputStream(documentBytes), reportName, StandardCharsets.UTF_16);
            
        } catch (Exception e) {
            throw new IOException("Failed to upload file to GridFS: " + e.getMessage(), e);
        }
    }

    private void saveToGridFSonReport(Reports data, byte[] documentBytes) throws IOException {
        try {
        	String reportName = "report_" + data.getReportcode();
        	GridFSDBFile file = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(reportName)));
    	    if (file != null) {
    	        gridFsTemplate.delete(new Query(Criteria.where("filename").is(reportName)));
    	    }
    	    gridFsTemplate.store(new ByteArrayInputStream(documentBytes), reportName, StandardCharsets.UTF_16);
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
         GridFSDBFile largefile = gridFsTemplate.findOne(new Query(
 				Criteria.where("filename").is("report_" + objReport.getReportcode())));
 		jsonContent = new BufferedReader(new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8))
 						.lines().collect(Collectors.joining("\n"));
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

	public List<Reports> getReportsBasedProject(LSprojectmaster objClass) {
		
		Optional<List<ReportTemplateMapping>> mapLst = reportTemplateMappingRepository.findByLsprojectmasterOrderByTemplatemapid(objClass);
		
		if(mapLst.isPresent()) {
		
			// Assuming mapLst is already defined as Optional<List<ReportTemplateMapping>>
			Optional<List<Long>> templateCodesOptional = mapLst.map(lst -> 
			    lst.stream()
			       .map(ReportTemplateMapping::getTemplatecode)
			       .collect(Collectors.toList())
			);

			// To get the list of template codes, you can use templateCodesOptional.orElse(Collections.emptyList());
			List<Long> templateCodes = templateCodesOptional.orElse(Collections.emptyList());
			
			List<Reports> mapReportLst = reportsRepository.findByReportcodeIn(templateCodes);
			
			return mapReportLst;
			
		}		
		return null;
	}
	
	public Reporttemplate getReportTemplateData(Reporttemplate objfile) {
        objfile.setResponse(new Response());

        if (objfile.getIsmultitenant() == 1 || objfile.getIsmultitenant() == 2) {
            String tenant = TenantContext.getCurrentTenant();
            if (objfile.getIsmultitenant() == 2) {
                return objfile;
            }
            String containerName = tenant + "reportdocument";
            String documentName = objfile.getFileuid();
            byte[] documentBytes = objCloudFileManipulationservice.retrieveCloudReportFile(containerName, documentName);

            Optional<String> optionalContent = Optional.ofNullable(documentBytes).map(bytes -> new String(bytes, StandardCharsets.UTF_8));
            
            if (optionalContent.isPresent()) {
                objfile.setTemplatecontent(optionalContent.get());
            } else {
            	System.out.println("Failed to retrieve JSON content from Azure Blob Storage.");
            }
        } else {
            GridFSDBFile largefile = gridFsTemplate.findOne(new Query(
                    Criteria.where("filename").is("ReportTemplate_" + objfile.getTemplatecode())));

            if (largefile != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8))) {
                    String jsonContent = reader.lines().collect(Collectors.joining("\n"));
                    objfile.setTemplatecontent(jsonContent);
                } catch (Exception e) {
                	System.out.println("Error reading JSON content from GridFS --> "+ e);
                }
            } else {
            	System.out.println("No file found in GridFS with the specified template code.");
            }
        }
        return objfile;
    }

	public String systemClipboard(CustomParameter param) {
		if (param.content != null && param.content != "") {
			try {
				MetafileImageParsedEventHandler metafileImageParsedEvent = new MetafileImageParsedEventHandler() {

					ListSupport<MetafileImageParsedEventHandler> delegateList = new ListSupport<MetafileImageParsedEventHandler>(
							MetafileImageParsedEventHandler.class);

					// Represents event handling for MetafileImageParsedEventHandlerCollection.
					public void invoke(Object sender, MetafileImageParsedEventArgs args) throws Exception {
						onMetafileImageParsed(sender, args);
					}

					// Represents the method that handles MetafileImageParsed event.
					public void dynamicInvoke(Object... args) throws Exception {
						onMetafileImageParsed((Object) args[0], (MetafileImageParsedEventArgs) args[1]);
					}

					// Represents the method that handles MetafileImageParsed event to add
					// collection item.
					public void add(MetafileImageParsedEventHandler delegate) throws Exception {
						if (delegate != null)
							delegateList.add(delegate);
					}

					// Represents the method that handles MetafileImageParsed event to remove
					// collection
					// item.
					public void remove(MetafileImageParsedEventHandler delegate) throws Exception {
						if (delegate != null)
							delegateList.remove(delegate);
					}
				};
				// Hooks MetafileImageParsed event.
				WordProcessorHelper.MetafileImageParsed.add("OnMetafileImageParsed", metafileImageParsedEvent);
				String json = WordProcessorHelper.loadString(param.content, getFormatType(param.type.toLowerCase()));
				// Unhooks MetafileImageParsed event.
				WordProcessorHelper.MetafileImageParsed.remove("OnMetafileImageParsed", metafileImageParsedEvent);
				return json;
			} catch (Exception e) {
				return "";
			}
		}
		return "";
	}
	
	// Converts Metafile to raster image.
		private static void onMetafileImageParsed(Object sender, MetafileImageParsedEventArgs args) throws Exception {
			if (args.getIsMetafile()) {
				// You can write your own method definition for converting Metafile to raster
				// image using any third-party image converter.
				args.setImageStream(convertMetafileToRasterImage(args.getMetafileStream()));
			} else {
				InputStream inputStream = StreamSupport.toStream(args.getMetafileStream());
				// Use ByteArrayOutputStream to collect data into a byte array
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

				// Read data from the InputStream and write it to the ByteArrayOutputStream
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					byteArrayOutputStream.write(buffer, 0, bytesRead);
				}

				// Convert the ByteArrayOutputStream to a byte array
				byte[] tiffData = byteArrayOutputStream.toByteArray();
				// Read TIFF image from byte array
				ByteArrayInputStream tiffInputStream = new ByteArrayInputStream(tiffData);
				IIORegistry.getDefaultInstance().registerServiceProvider(new TIFFImageReaderSpi());

				// Create ImageReader and ImageWriter instances
				ImageReader tiffReader = ImageIO.getImageReadersByFormatName("TIFF").next();
				ImageWriter pngWriter = ImageIO.getImageWritersByFormatName("PNG").next();

				// Set up input and output streams
				tiffReader.setInput(ImageIO.createImageInputStream(tiffInputStream));
				ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
				pngWriter.setOutput(ImageIO.createImageOutputStream(pngOutputStream));

				// Read the TIFF image and write it as a PNG
				BufferedImage image = tiffReader.read(0);
				pngWriter.write(image);
				pngWriter.dispose();
				byte[] jpgData = pngOutputStream.toByteArray();
				InputStream jpgStream = new ByteArrayInputStream(jpgData);
				args.setImageStream(StreamSupport.toStream(jpgStream));
			}
		}
		
		private static StreamSupport convertMetafileToRasterImage(StreamSupport ImageStream) throws Exception {
			// Here we are loading a default raster image as fallback.
			StreamSupport imgStream = getManifestResourceStream("ImageNotFound.jpg");
			return imgStream;
			// To do : Write your own logic for converting Metafile to raster image using
			// any third-party image converter(Syncfusion doesn't provide any image
			// converter).
		}
		
		private static StreamSupport getManifestResourceStream(String fileName) throws Exception {
			AssemblySupport assembly = AssemblySupport.getExecutingAssembly();
			return assembly.getManifestResourceStream("ImageNotFound.jpg");
		}
	
	static FormatType getFormatType(String format) {
		switch (format) {
		case ".dotx":
		case ".docx":
		case ".docm":
		case ".dotm":
			return FormatType.Docx;
		case ".dot":
		case ".doc":
			return FormatType.Doc;
		case ".rtf":
			return FormatType.Rtf;
		case ".txt":
			return FormatType.Txt;
		case ".xml":
			return FormatType.WordML;
		case ".html":
			return FormatType.Html;
		default:
			return FormatType.Docx;
		}
	}
}
