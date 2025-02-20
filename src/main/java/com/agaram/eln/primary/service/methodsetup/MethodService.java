package com.agaram.eln.primary.service.methodsetup;


import java.io.BufferedInputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedReader;


import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.util.Matrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.exception.FileStorageException;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.equipment.Equipment;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.methodsetup.CloudParserFile;
import com.agaram.eln.primary.model.methodsetup.CustomField;
import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.methodsetup.MethodVersion;
import com.agaram.eln.primary.model.methodsetup.ParserBlock;
import com.agaram.eln.primary.model.methodsetup.ParserField;
import com.agaram.eln.primary.model.methodsetup.ParserTechnique;
import com.agaram.eln.primary.model.methodsetup.SampleExtract;
import com.agaram.eln.primary.model.methodsetup.SampleLineSplit;
import com.agaram.eln.primary.model.methodsetup.SampleTextSplit;
import com.agaram.eln.primary.model.methodsetup.SubParserField;
import com.agaram.eln.primary.model.methodsetup.SubParserTechnique;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.property.FileStorageProperties;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.equipment.EquipmentRepository;
import com.agaram.eln.primary.repository.instrumentsetup.InstMasterRepository;
import com.agaram.eln.primary.repository.methodsetup.CloudParserFileRepository;
import com.agaram.eln.primary.repository.methodsetup.CustomFieldRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodVersionRepository;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.fileuploaddownload.FileStorageService;
import com.aspose.pdf.Document;
import com.aspose.pdf.ExcelSaveOptions;
import com.aspose.pdf.License;
import com.aspose.pdf.TextAbsorber;
import com.aspose.pdf.TextExtractionOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.pdf.PdfReader;
//import com.itextpdf.text.pdf.eln.PdfTextExtractor;
import com.mongodb.gridfs.GridFSDBFile;

//import okhttp3.OkHttpClient;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;

import org.springframework.util.StringUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import okhttp3.*;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

/**
 * This Service class is used to access the MethodRepository to fetch details
 * from the 'method' table through Method Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   07- Feb- 2020
 */
@Service
public class MethodService {

	
	@Autowired
	MethodRepository methodRepo;
	
	

	@Autowired
	LSSiteMasterRepository siteRepo;
	
	@Autowired
	LSuserMasterRepository userRepo;
	
	@Autowired
	InstMasterRepository instMastRepo;
	
	@Autowired	
	ParserSetupService parserSetupService;
		
	@Autowired
	SampleTextSplitService textSplitService;
	
	@Autowired
	SampleLineSplitService lineSplitService;
	
	@Autowired
	SampleExtractService extractService;
	
	@Autowired
	SampleSplitService sampleSplitService;

	@Autowired
	CustomFieldRepository customFieldRepo;
	
	@Autowired
	EvaluateParserService evaluateParserService;	
	
	@Autowired
	CustomFieldService customFieldService;
	
    @Autowired
    private CloudParserFileRepository cloudparserfilerepository;
    
	@Autowired
	GridFsOperations gridFsOps;

//	@Autowired
//	private MongoTemplate mongoTemplate;

	@Autowired
	private GridFsTemplate gridFsTemplate;
	
	
    @Autowired
    private CloudFileManipulationservice cloudFileManipulationservice;
    
    @Autowired
	LScfttransactionRepository lscfttransactionrepo;
		
	@Autowired
	MethodVersionRepository methodversionrepository;

	@Autowired
	private Environment env;

	private Path fileStorageLocation;

	@Autowired
	private EquipmentRepository equipmentrepository;
    
    @Autowired
    public MethodService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
   
	/**
	 * This method is used to retrieve list of active methods in the site.
	 * @param site [Site] object for which the methods are to be fetched
	 * @return list of methods based on site.
	 */
	@Transactional
	public ResponseEntity<Object> getActiveMethodBySite(final LSSiteMaster site){
//		final List<Method> methodList =  methodRepo.findBySiteAndStatus(site, 1,
		//	new Sort(Sort.Direction.DESC, "methodkey"));
		final List<Method> methodList =  methodRepo.findBySite(site,
				new Sort(Sort.Direction.DESC, "methodkey"));
		return new ResponseEntity<>(methodList, HttpStatus.OK);
	}
	
	@Transactional
	public ResponseEntity<Object> findByStatus(Integer status){
		final List<LSSiteMaster> siteList =  siteRepo.findByIstatus(status);
		return new ResponseEntity<>(siteList, HttpStatus.OK);
	}
	
	@Transactional
	public ResponseEntity<Object> getmethodversion(final int methodKey){
		final List<MethodVersion> methodversionList =  methodversionrepository.findByMethodkeyOrderByVersionDesc(methodKey);
	//	final List<MethodVersion> methodversionList =  methodversionrepository.findAll(new Sort(Sort.Direction.DESC, "methodkey"));
		
		return new ResponseEntity<>(methodversionList, HttpStatus.OK);
	}
   /**
    * This method is used to add new Method entity.
    * The method name can be a duplicate name of any other method. Any active instrument of the site can be
    * associated to the method. The input raw data file can be a pdf/txt/csv file
    * @param  methodMaster [Method] -  holding details of methodname, intrawdataurl, createdby, status and createddate.
    * @param site [Site] object for which the methods are to be fetched
    * @param saveAuditTrail [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
    * @param page [Page] entity relating to 'MethodMaster'
    * @param request [HttpServletRequest] Request object to ip address of remote client
    * @return Response of newly added method entity
    */
//	@Transactional
//	public ResponseEntity<Object> createMethod(final Method methodMaster, final LSSiteMaster site, final HttpServletRequest request,Method auditdetails)
//	{			
//		boolean saveAuditTrail=true;
//		final InstrumentMaster instMaster = instMastRepo.findOne(methodMaster.getInstmaster().getInstmastkey());
//		final LSuserMaster createdUser = getCreatedUserByKey(methodMaster.getCreatedby().getUsercode());
//		
//		if (instMaster != null) {			
//			final Optional<Method> methodByName = methodRepo.findByMethodnameAndInstmasterAndStatusAndSite(
//					methodMaster.getMethodname(), instMaster, 1,site);
//			if (methodByName.isPresent())
//			{
//				//Conflict =409 - Duplicate entry
//				if (saveAuditTrail == true)
//				{	
//				}
//				methodMaster.setInfo("Duplicate Entry - " + methodMaster.getMethodname() + " for inst : " + instMaster.getInstrumentcode());
//				methodMaster.setObjsilentaudit(auditdetails.getObjsilentaudit());
//
//				return new ResponseEntity<>(methodMaster, HttpStatus.CONFLICT);
//			}
//			else
//			{							
//				methodMaster.setCreatedby(createdUser);
//				methodMaster.setInstmaster(instMaster);					
//				final Method savedMethod = methodRepo.save(methodMaster);
//				 
//				methodversionrepository.save(methodMaster.getMethodversion());
//				
//				savedMethod.setDisplayvalue(savedMethod.getMethodname());
//				savedMethod.setScreenname("Methodmaster");
//				savedMethod.setObjsilentaudit(auditdetails.getObjsilentaudit());
//					final Map<String, String> fieldMap = new HashMap<String, String>();
//					fieldMap.put("site", "sitename");				
//					fieldMap.put("createdby", "loginid");				
//					fieldMap.put("instmaster", "instrumentcode");				
//
//				return new ResponseEntity<>( savedMethod, HttpStatus.OK);			
//			}
//		}
//		else {
//			//Instrument not found
//			if (saveAuditTrail == true)
//			{						
////				final String comments = "Create Failed as Instrument not found";
//				
////				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
////						"Create", comments, site, "",
////						createdUser, request.getRemoteAddr());
//				  LScfttransaction LScfttransaction = new LScfttransaction();
//					
//					LScfttransaction.setActions("Insert");
//					LScfttransaction.setComments("Create Failed as Instrument not found");
//					LScfttransaction.setLssitemaster(site.getSitecode());
//					LScfttransaction.setLsuserMaster(methodMaster.getCreatedby().getUsercode());
//					LScfttransaction.setManipulatetype("View/Load");
//					LScfttransaction.setModuleName("Method Delimiter");
//					LScfttransaction.setTransactiondate(methodMaster.getCreateddate());
//					LScfttransaction.setUsername(methodMaster.getUsername());
//					LScfttransaction.setTableName("Method");
//					LScfttransaction.setSystemcoments("System Generated");
//					
//					lscfttransactionrepo.save(LScfttransaction);
//			}
//  			return new ResponseEntity<>("Invalid Instrument", HttpStatus.NOT_FOUND);
//		}		
//	}		
	
	
	/* equipment change*/
	
	@Transactional
	public ResponseEntity<Object> createMethod(final Method methodMaster, final LSSiteMaster site, final HttpServletRequest request,Method auditdetails)
	{			
		boolean saveAuditTrail=true;
//		final InstrumentMaster instMaster = instMastRepo.findOne(methodMaster.getInstmaster().getInstmastkey());
		
		final LSuserMaster createdUser = getCreatedUserByKey(methodMaster.getCreatedby().getUsercode());
		final Equipment equipment = equipmentrepository.findOne(methodMaster.getEquipment().getNequipmentcode());
		 
		if (equipment != null) {			
			final Optional<Method> methodByName = methodRepo.findByMethodnameAndEquipmentAndStatusAndSite(
					methodMaster.getMethodname(), equipment, 1,site);
			if (methodByName.isPresent())
			{
				//Conflict =409 - Duplicate entry
				if (saveAuditTrail == true)
				{	
				}
				methodMaster.setInfo("Duplicate Entry - " + methodMaster.getMethodname() + " for Equipment : " + equipment.getNequipmentcode());
				methodMaster.setObjsilentaudit(auditdetails.getObjsilentaudit());

				return new ResponseEntity<>(methodMaster, HttpStatus.CONFLICT);
			}
			else
			{							
				methodMaster.setCreatedby(createdUser);
				methodMaster.setEquipment(equipment);					
				final Method savedMethod = methodRepo.save(methodMaster);
				 
				methodversionrepository.save(methodMaster.getMethodversion());
				
				savedMethod.setDisplayvalue(savedMethod.getMethodname());
				savedMethod.setScreenname("Methodmaster");
				savedMethod.setObjsilentaudit(auditdetails.getObjsilentaudit());
					final Map<String, String> fieldMap = new HashMap<String, String>();
					fieldMap.put("site", "sitename");				
					fieldMap.put("createdby", "loginid");				
					fieldMap.put("instmaster", "instrumentcode");				

				return new ResponseEntity<>( savedMethod, HttpStatus.OK);			
			}
		}
		else {
			//Instrument not found
			if (saveAuditTrail == true)
			{						

				  LScfttransaction LScfttransaction = new LScfttransaction();
					
					LScfttransaction.setActions("Insert");
					LScfttransaction.setComments("Create Failed as Instrument not found");
					LScfttransaction.setLssitemaster(site.getSitecode());
					LScfttransaction.setLsuserMaster(methodMaster.getCreatedby().getUsercode());
					LScfttransaction.setManipulatetype("View/Load");
					LScfttransaction.setModuleName("Method Delimiter");
					LScfttransaction.setTransactiondate(methodMaster.getCreateddate());
					LScfttransaction.setUsername(methodMaster.getUsername());
					LScfttransaction.setTableName("Method");
					LScfttransaction.setSystemcoments("System Generated");
					
					lscfttransactionrepo.save(LScfttransaction);
			}
  			return new ResponseEntity<>("Invalid Instrument", HttpStatus.NOT_FOUND);
		}		
	}		
	
	
	
	/**
	 * This method is used to update selected Method object.
	 * The method name can be updated any time.
	 * The associated instrument can be changed only with any other instrument from the same instrument category.
	 * The raw data file can be updated only before sample splitting /parsing is done for the method.
	 * @param method [Method] object to update
	 * @param site [Site] object for which the methods are to be fetched
	 * @param doneByUserKey [int] primary key of logged-in user who done this task
	 * @param comments [String] comments given for audit trail recording
	 * @param saveAuditTrail [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
	 * @param page [Page] entity relating to 'MethodMaster'
	 * @param request [HttpServletRequest] Request object to ip address of remote client
	 * @return Response of updated method master entity
	 * @throws ParseException 
	 */
	
	/* change for equipment*/
//	@Transactional
//	
//	public ResponseEntity<Object> updateMethod(final Method method, final LSSiteMaster site, final int doneByUserKey, 
//			    final HttpServletRequest request,Method auditdetails)
//	{	  		
//		boolean saveAuditTrail=true;	
//		final InstrumentMaster instMaster = instMastRepo.findOne(method.getInstmaster().getInstmastkey());
//		
//		 final Optional<Method> methodByKey = methodRepo.findByMethodkeyAndStatusAndSite(method.getMethodkey(), 1,site);
//		 
//		 if(methodByKey.isPresent()) {		   
//
//		
//		if (instMaster != null) 
//		{
//
//			final Optional<Method> methodByName = methodRepo.findByMethodnameAndInstmasterAndStatusAndSite(
//					method.getMethodname(), instMaster, 1,site);
//			
//		
//			if (methodByName.isPresent())
//        {
//				 
//				if(methodByName.get().getMethodkey().equals(method.getMethodkey()))
//		    	{   
//				//copy of object for using 'Diffable' to compare objects
////	    			final Method methodBeforeSave = new Method(methodByName.get());
//	    			
//					method.setInstmaster(instMaster);		    			
//		    		final Method savedMethod = methodRepo.save(method);
//		    		
//		    		savedMethod.setDisplayvalue(savedMethod.getMethodname());
//		    		savedMethod.setScreenname("Methodmaster");
//		    		savedMethod.setObjsilentaudit(auditdetails.getObjsilentaudit());	
//		    		return new ResponseEntity<>(savedMethod , HttpStatus.OK);	
//		    	}
//				else {
//					method.setInfo("Duplicate Entry - " + method.getMethodname() + " for inst " + instMaster.getInstrumentcode());
//					method.setObjsilentaudit(auditdetails.getObjsilentaudit());
//	    			return new ResponseEntity<>(method, HttpStatus.CONFLICT);   
//				}
//		   }
//			
//			
//			else
//	    	{			    		
//	    	
//	    		final Method savedMethod = methodRepo.save(method);
//	    		
//	    		savedMethod.setDisplayvalue(savedMethod.getMethodname());
//	    		savedMethod.setObjsilentaudit(auditdetails.getObjsilentaudit());
//
//				savedMethod.setScreenname("Methodmaster");
//					    		return new ResponseEntity<>(savedMethod , HttpStatus.OK);			    		
//	    	}	
//			   }
//			
//			
//			
//		   else
//		   {
//			   //Invalid methodkey		   
//			   if (saveAuditTrail) {				
////					cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
////							"Edit", "Update Failed - Method Not Found", site, "", createdUser, request.getRemoteAddr());
//				   LScfttransaction LScfttransaction = new LScfttransaction();
//					
//					LScfttransaction.setActions("Update");
//					LScfttransaction.setComments(" Update Failed - Method Not Found");
//					LScfttransaction.setLssitemaster(site.getSitecode());
//					LScfttransaction.setLsuserMaster(method.getCreatedby().getUsercode());
//					LScfttransaction.setManipulatetype("View/Load");
//					LScfttransaction.setModuleName("Method Master");
//					LScfttransaction.setTransactiondate(method.getCreateddate());
//					LScfttransaction.setUsername(method.getUsername());
//					LScfttransaction.setTableName("Method");
//					LScfttransaction.setSystemcoments("System Generated");
//					
//					lscfttransactionrepo.save(LScfttransaction);
//	   		    }			
//				return new ResponseEntity<>("Update Failed - Method Not Found", HttpStatus.NOT_FOUND);
//		   }
//		}
//		else {
//			//Instrument not found
//			if (saveAuditTrail == true)
//			{		
//				 LScfttransaction LScfttransaction = new LScfttransaction();
//					
//					LScfttransaction.setActions("Update");
//					LScfttransaction.setComments("Invalid Instrument");
//					LScfttransaction.setLssitemaster(site.getSitecode());
//					LScfttransaction.setLsuserMaster(method.getCreatedby().getUsercode());
//					LScfttransaction.setManipulatetype("View/Load");
//					LScfttransaction.setModuleName("Method Master");
//					LScfttransaction.setTransactiondate(method.getCreateddate());
//					LScfttransaction.setUsername(method.getUsername());
//					LScfttransaction.setTableName("Method");
//					LScfttransaction.setSystemcoments("System Generated");
//					
//					lscfttransactionrepo.save(LScfttransaction);
//
//			}
//			return new ResponseEntity<>("Invalid Instrument", HttpStatus.NOT_FOUND);
//		}
//}	
	
	
    @Transactional
	public ResponseEntity<Object> updateMethod(final Method method, final LSSiteMaster site, final int doneByUserKey, 
			    final HttpServletRequest request,Method auditdetails) throws ParseException
	{	  		
		boolean saveAuditTrail=true;	
		//final InstrumentMaster instMaster = instMastRepo.findOne(method.getInstmaster().getInstmastkey());
		 final Equipment equipment = equipmentrepository.findOne(method.getEquipment().getNequipmentcode());
		 final Optional<Method> methodByKey = methodRepo.findByMethodkeyAndStatusAndSite(method.getMethodkey(), 1,site);
		 
		 if(methodByKey.isPresent()) {		   

		
		if (equipment != null) 
		{

			final Optional<Method> methodByName = methodRepo.findByMethodnameAndEquipmentAndStatusAndSite(
					method.getMethodname(), equipment, 1,site);
			
		
			if (methodByName.isPresent())
        {
				 
				if(methodByName.get().getMethodkey().equals(method.getMethodkey()))
		    	{   
					method.setEquipment(equipment);		
					method.setModifieddate(commonfunction.getCurrentUtcTime());
					
		    		final Method savedMethod = methodRepo.save(method);
		    		
		    		savedMethod.setDisplayvalue(savedMethod.getMethodname());
		    		savedMethod.setScreenname("Methodmaster");
		    		savedMethod.setObjsilentaudit(auditdetails.getObjsilentaudit());	
		    		return new ResponseEntity<>(savedMethod , HttpStatus.OK);	
		    	}
				else {
					method.setInfo("Duplicate Entry - " + method.getMethodname() + " for equipment " + equipment.getNequipmentcode());
					method.setObjsilentaudit(auditdetails.getObjsilentaudit());
	    			return new ResponseEntity<>(method, HttpStatus.CONFLICT);   
				}
		   }
			else
	    	{			    		
				method.setModifieddate(commonfunction.getCurrentUtcTime());
	    		final Method savedMethod = methodRepo.save(method);
	    		
	    		savedMethod.setDisplayvalue(savedMethod.getMethodname());
	    		savedMethod.setObjsilentaudit(auditdetails.getObjsilentaudit());

				savedMethod.setScreenname("Methodmaster");
					    		return new ResponseEntity<>(savedMethod , HttpStatus.OK);			    		
	    	}	
			   }
		
		   else
		   {
			   //Invalid methodkey		   
			   if (saveAuditTrail) {				
				   LScfttransaction LScfttransaction = new LScfttransaction();
					
					LScfttransaction.setActions("Update");
					LScfttransaction.setComments(" Update Failed - Method Not Found");
					LScfttransaction.setLssitemaster(site.getSitecode());
					LScfttransaction.setLsuserMaster(method.getCreatedby().getUsercode());
					LScfttransaction.setManipulatetype("View/Load");
					LScfttransaction.setModuleName("Method Master");
					LScfttransaction.setTransactiondate(method.getCreateddate());
					LScfttransaction.setUsername(method.getUsername());
					LScfttransaction.setTableName("Method");
					LScfttransaction.setSystemcoments("System Generated");
					
					lscfttransactionrepo.save(LScfttransaction);
	   		    }			
				return new ResponseEntity<>("Update Failed - Method Not Found", HttpStatus.NOT_FOUND);
		   }
		}
		else {
			//Instrument not found
			if (saveAuditTrail == true)
			{		
				 LScfttransaction LScfttransaction = new LScfttransaction();
					
					LScfttransaction.setActions("Update");
					LScfttransaction.setComments("Invalid Instrument");
					LScfttransaction.setLssitemaster(site.getSitecode());
					LScfttransaction.setLsuserMaster(method.getCreatedby().getUsercode());
					LScfttransaction.setManipulatetype("View/Load");
					LScfttransaction.setModuleName("Method Master");
					LScfttransaction.setTransactiondate(method.getCreateddate());
					LScfttransaction.setUsername(method.getUsername());
					LScfttransaction.setTableName("Method");
					LScfttransaction.setSystemcoments("System Generated");
					
					lscfttransactionrepo.save(LScfttransaction);
			}
			return new ResponseEntity<>("Invalid Instrument", HttpStatus.NOT_FOUND);
		}
}	
	/**
	 * This method is used to delete selected Method object based on its primary key.
	 * Need to validate that sample splitting /parsing is not done for that Method before deleting.
	 * @param methodKey [int] primary key of Method entity
	 * @param site [Site] object for which the methods are to be fetched
	 * @param comments [String] comments given for audit trail recording
	 * @param doneByUserKey [int] primary key of logged-in user who done this task
	 * @param saveAuditTrial  [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
	 * @param page [Page] entity relating to 'MethodMaster'
	 * @param request [HttpServletRequest] Request object to ip address of remote client
	 * @return Response of deleted method master entity
	 * @throws ParseException 
	 */
	 @Transactional()
	   public ResponseEntity<Object> deleteMethod(final int methodKey, 
			   final LSSiteMaster site, final String comments, final int doneByUserKey, 
			   final HttpServletRequest request,final Method otherdetails,Method auditdetails) throws ParseException
	   {	   
		  boolean saveAuditTrial=true;
		   final Optional<Method> methodByKey = methodRepo.findByMethodkeyAndStatus(methodKey, 1);
//		   final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);
		   
		   if(methodByKey.isPresent()) {

			   final Method method = methodByKey.get();
//			   if ((method.getSamplesplit() != null && method.getSamplesplit() == 1)
//					   || (method.getParser() != null && method.getParser() == 1)) {
//				    if (saveAuditTrial)
//		    		{		
//		    	    }
//				    method.setInfo("Associated - "+ method.getMethodname());
//				    method.setObjsilentaudit(auditdetails.getObjsilentaudit());
//				    
//			
//				    return new ResponseEntity<>(method , HttpStatus.IM_USED);//status code - 226
//			   }
//			   else
			   {
						
				       method.setStatus(-1);
					   method.setMethodstatus("D");
					   method.setModifieddate(commonfunction.getCurrentUtcTime());
					   final Method savedMethod = methodRepo.save(method);   
					   
					   savedMethod.setDisplayvalue(savedMethod.getMethodname());
					   savedMethod.setScreenname("Methodmaster");
					   savedMethod.setObjsilentaudit(auditdetails.getObjsilentaudit());
				   return new ResponseEntity<>(savedMethod, HttpStatus.OK); 
			   }
		   }
		   else
		   {
			   //Invalid methodkey
			   if (saveAuditTrial) {				
//							
	  		    }			
				return new ResponseEntity<>("Delete Failed - Method Not Found", HttpStatus.NOT_FOUND);
		   }
	   }  

   /**
    * This method is used to fetch list of instruments that are not yet associated with
    * any of the Method master.
    * @param site  [Site] object for which the active instruments are to be fetched
    * @return list of un-associated instruments
    */
   @Transactional
   public ResponseEntity<Object> getInstListToAssociateMethod(final LSSiteMaster site){
	   return new ResponseEntity<>(new ArrayList<InstrumentMaster>(), HttpStatus.OK);
	   //return new ResponseEntity<>(methodRepo.getInstListToAssociateMethod(site.getSitecode()), HttpStatus.OK);
   }
	 
	/**
	 * This method is used to retrieve the 'Users' details based on the
	 * input primary key- userKey.
	 * @param userKey [int] primary key of Users entity
	 * @return Users Object relating to the userKey
	 */
	private LSuserMaster getCreatedUserByKey(final int userKey)
	{
		final LSuserMaster createdBy =  userRepo.findOne(userKey);
		final LSuserMaster createdUser = new LSuserMaster();
		createdUser.setUsername(createdBy.getUsername());
		createdUser.setUsercode(createdBy.getUsercode());;
		
		return createdUser;
	}
		
	/**
	 * This method is used to convert the Method entity to xml with the difference in object
	 * before and after update for recording in Audit Trial
	 * @param methodBeforeSave [Method] Object before update
	 * @param savedMethod [Method] object after update
	 * @return string formatted xml data
	 */
   public String convertMethodObjectToXML(final Method methodBeforeSave, final Method savedMethod)
   {
	  	final Map<Integer, Map<String, Object>> dataModified = new HashMap<Integer, Map<String, Object>>();
			final Map<String, Object> diffObject = new HashMap<String, Object>();    			
			
			dataModified.put(savedMethod.getMethodkey(), diffObject);
			
			final Map<String, String> fieldMap = new HashMap<String, String>();
			fieldMap.put("site", "sitename");				
			fieldMap.put("createdby", "loginid");				
			fieldMap.put("instmaster", "instrumentcode");
		
			
		//return readWriteXML.saveXML(new Method(savedMethod), Method.class, dataModified, "individualpojo", fieldMap);
			return "";
   }
	   
   /**
    * This method is used to convert a new pdf file to text file and then to convert text file content 
    * to byte array using InputStream. 
    * If the .txt version of the file already exists, it will convert to byte[] without pdf to txt conversion.  
    * If the input file is a '.txt' file, it will convert to byte array.	    
 * @param retrivedfile 
    * @param fileName [String] name of pdf/txt/csv file to convert
 * @param ok 
    * @return string of text file content
 * @throws Exception 
    */
        
   @SuppressWarnings("resource")
public String getFileData(final String fileName,String tenant,Integer methodKey) throws FileNotFoundException, IOException
 {
	   try
        {			
		   File file = null;
		   final String ext = FilenameUtils.getExtension(fileName); 
		   String rawDataText="";
		   
		   CloudParserFile obj = cloudparserfilerepository.findTop1Byfilename(fileName);
		   String fileid = obj.fileid;
			 
		   System.out.println("getFileData - fileid:"+fileid);
		   System.out.println("getFileData - fileName:"+fileName);
		   
		   file = stream2file(cloudFileManipulationservice.retrieveCloudFile(fileid, tenant + "parserfile"),fileName, ext);
		   
		   byte[] bytes = null;
           
		    if(file !=null)
		    {
		    	System.out.println("filepresentinblob");
			   if (ext.equalsIgnoreCase("pdf")) {
				   
	                List<Method> methodobj = methodRepo.findByMethodkey(methodKey);
	  				Integer converterstatus = methodobj.get(0).getConverterstatus();
	  				
	  				if(converterstatus.equals(1)) {
	
	  					 InputStream licenseStream = getLicenseStream();
	  					 
					    final File tempconvertedtextFile = File.createTempFile(fileName, "txt");
						
						 License asposePdfLicenseText = new License();
		 		            try {
								asposePdfLicenseText.setLicense(licenseStream);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
		 	
			            Document convertPDFDocumentToText = new Document(file.getAbsolutePath());

			            TextAbsorber textAbsorber = new TextAbsorber(new TextExtractionOptions(TextExtractionOptions.TextFormattingMode.Pure));

			            convertPDFDocumentToText.getPages().accept(textAbsorber);

			            String ExtractedText = textAbsorber.getText();
			            BufferedWriter writer = null;

						try {
							writer = new BufferedWriter(new FileWriter(tempconvertedtextFile));
						} catch (IOException e) {
							e.printStackTrace();
						}
		
			            try {
							writer.write(ExtractedText);
						} catch (IOException e) {
							e.printStackTrace();
						}
			            
			            try {
							writer.close();
						} catch (IOException e) {
							e.printStackTrace();
						}

			            System.out.println("Done");
			            
				        
			          byte[] bytesArray = new byte[(int) tempconvertedtextFile.length()]; 	
	 	              FileInputStream fis = new FileInputStream(tempconvertedtextFile);
		              fis.read(bytesArray); //read file into bytes[]
		              fis.close();	
		           		 	           
		 	          rawDataText = new String(bytesArray, StandardCharsets.UTF_8);
	  				}else { //pdf to csv
	  					InputStream licenseStream = getLicenseStream();
						 
		 				  final File convertedcsvfile = File.createTempFile(fileName, "csv");
							
		 			           License asposePdfLicenseCSV = new License();
		 			           try {
		 							asposePdfLicenseCSV.setLicense(licenseStream);
		 						} catch (Exception e) {
		 				    		e.printStackTrace();
		 						}       
		 				        
		 				        Document convertPDFDocumentToCSV = new Document(file.getAbsolutePath());
		 				        
		 				        ExcelSaveOptions csvSave = new ExcelSaveOptions();
		 				        csvSave.setFormat(ExcelSaveOptions.ExcelFormat.CSV);
		 				        
		 				    	convertPDFDocumentToCSV.save(convertedcsvfile.getAbsolutePath(), csvSave);
		 				        System.out.println("Done");

			    	  	        byte[] bytesArray = new byte[(int) convertedcsvfile.length()]; 	
				        	    FileInputStream fis = new FileInputStream(convertedcsvfile);
				 	            fis.read(bytesArray); //read file into bytes[]
				 	            fis.close();	
				 	           		 	           
			 		 	        rawDataText = new String(bytesArray, StandardCharsets.UTF_8);
			 		 	        rawDataText = rawDataText.substring(1); // Start from index 1 to the end
				 	            rawDataText = rawDataText.replace("\n\"", "\n");
				 	            rawDataText = rawDataText.replace("\",", ",");
				 	            rawDataText = rawDataText.replace(",\"", ",");
				 	            rawDataText = rawDataText.replace("\"\"", "\"");
				 	            rawDataText=rawDataText.replace("\"\r\n", "\r\n");
		 			        
					   
	  				}
	  				
			   }  else if (ext.equalsIgnoreCase("csv")) {
				   
					byte[] bytesArray = new byte[(int) file.length()];
					FileInputStream fis = new FileInputStream(file);
					fis.read(bytesArray); // read file into bytes[]
					fis.close();

	 	              rawDataText = new String(bytesArray, StandardCharsets.UTF_8);  

	 	              rawDataText = rawDataText.substring(1); // Start from index 1 to the end
	 	              rawDataText = rawDataText.replace("\n\"", "\n");
	 	              rawDataText = rawDataText.replace("\",", ",");
	 	       
	 	              rawDataText = rawDataText.replace(",\"", ",");
	 	          
	 	             rawDataText = rawDataText.replace("\"\"", "\"");
	 	            rawDataText=rawDataText.replace("\"\r\n", "\r\n");
			 }
			   else if (ext.equalsIgnoreCase("xls") || ext.equalsIgnoreCase("xlsx") ) {
			
					final File tempFile = File.createTempFile(fileName, "csv");
					
				        try (FileInputStream fis = new FileInputStream(file);
				                //HSSFWorkbook workbook = new HSSFWorkbook(fis);
				                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

				        	 // Determine whether the file is .xls or .xlsx
				            Workbook workbook = null;
				            if (ext.equals("xls")) {
				                workbook = new HSSFWorkbook(fis); // For .xls
				            } else if (ext.equals("xlsx")) {
				                workbook = new XSSFWorkbook(fis); // For .xlsx
				            } else {
				                throw new IllegalArgumentException("The specified file is not Excel (.xls or .xlsx)");
				            }
				            
				            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet
				            DataFormatter formatter = new DataFormatter(); // To format the data as it appears in Excel

				               for (Row row : sheet) {
				                   StringBuilder sb = new StringBuilder();
				                   for (Cell cell : row) {
				                       switch (cell.getCellType()) {
				                           case STRING:
				                               sb.append(cell.getStringCellValue());
				                               break;
				                           case NUMERIC:
				                               // Use the formatter to get the exact cell value without rounding
				                               String formattedValue = formatter.formatCellValue(cell);
				                               sb.append(formattedValue);
				                               break;
				                           case BOOLEAN:
				                               sb.append(cell.getBooleanCellValue());
				                               break;
				                           case FORMULA:
				                               sb.append(cell.getCellFormula()); // Handle formula as needed
				                               break;
				                           default:
				                               sb.append("");
				                               break;
				                       }
				                       sb.append(","); // Separate columns by a comma
				                   }
				                   // Remove the last comma and add a newline
				                   writer.write(sb.toString().replaceAll(",$", ""));
				                   writer.newLine();
				               }

				               writer.flush();
				              
				           } catch (IOException e) {
				               e.printStackTrace();
				           }
				        
				           bytes = FileUtils.readFileToByteArray(tempFile);
				    	   rawDataText = new String(bytes, StandardCharsets.UTF_8); 
			               System.out.println("Conversion complete. Check " + rawDataText);
					}
				
			   else
			   {
				   RandomAccessBufferedFileInputStream raFileinputstream = new RandomAccessBufferedFileInputStream(file);
	  			    rawDataText = new BufferedReader(
	  				new InputStreamReader(raFileinputstream, StandardCharsets.UTF_8)).lines()
	  					.collect(Collectors.joining("\n"));
	  			   System.out.println("TXTFile-rawDataText:"+rawDataText);
	  			   }
		   
			   //rawDataText = new String(Files.readAllBytes(file.toPath()), StandardCharsets.ISO_8859_1);
		    }else {
		    	System.out.println("filenotpresentinblob");
		    }
           return rawDataText;    
        } 	  
        catch (IOException e) 
        { 
        	return null;
        } 
   }

   public static File stream2file (InputStream in,String filename,String ext) throws IOException {
       final File tempFile = File.createTempFile(filename, ext);
       tempFile.deleteOnExit();
       try (FileOutputStream out = new FileOutputStream(tempFile)) {
           IOUtils.copy(in, out);
       }
       return tempFile;
   }
   
   private static InputStream getLicenseStream() {
       
   	String licenseContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
   	"<License>\n"+
   	 "<Data>\n"+
   	    "<LicensedTo>Agaram Technologies Private Ltd</LicensedTo>\n"+
   	    "<EmailTo>mukunth@agaramtech.com</EmailTo>\n"+
   	    "<LicenseType>Developer OEM</LicenseType>\n"+
   	    "<LicenseNote>1 Developer And Unlimited Deployment Locations</LicenseNote>\n"+
   	    "<OrderID>230713115513</OrderID>\n"+
   	    "<UserID>961336</UserID>\n"+
   	    "<OEM>This is a redistributable license</OEM>\n"+
   	    "<Products>\n"+
   	      "<Product>Aspose.PDF for Java</Product>\n"+
   	    "</Products>\n"+
   	    "<EditionType>Professional</EditionType>\n"+
   	    "<SerialNumber>70819740-45dc-48d2-ac0c-ec13bf2b3a78</SerialNumber>\n"+
   	    "<SubscriptionExpiry>20240713</SubscriptionExpiry>\n"+
   	    "<LicenseVersion>3.0</LicenseVersion>\n"+
   	   " <LicenseInstructions>https://purchase.aspose.com/policies/use-license</LicenseInstructions>\n"+
   	  "</Data>\n"+
   	  "<Signature>cegenUxQNwdae0mvL7NDVJTGjnfp+b5xzb1+8AcIb9KAXtrragPtqL2lgsm13NjMpVLgH8MB/DSB/WW7Cy1n4XpRUUO67QEbRfDXhStHnyGR8k4mVimOBwifbjyBkKmKHMkKhiO/xRMLd6qap06HggZoy3Bzb2Qn6THYzU75GOFnZ+xii7c5OXKd4LJ1idZ/wzUPnPdCyJ642wsQ5eSrHyD39hAk4Hhv5ZSkN8KW+JMJSh4KcIjgSt3ehEXXoRvUs9SICWE3aUAIFGumrRomiwHmCgvNCbEbzIpuxQ+J13+7RBAstZq3dHfvKnvy562rKGkC+ls/VNb9aVzxLpWdYw==</Signature>\n"+
   	"</License>";

   	
       // Convert the license string to an InputStream and return it
       InputStream licenseStream = new ByteArrayInputStream(licenseContent.getBytes(StandardCharsets.UTF_8));
       
       // Explicitly clear the license content string to allow for garbage collection
       licenseContent = null; // Make the string eligible for GC

       return licenseStream;
   }
   
   @SuppressWarnings("resource")
	public String getSQLFileData(String fileName, Integer methodKey) throws IOException, InterruptedException {

		String rawDataText = "";
		byte[] bytes = null;

		final String ext = FilenameUtils.getExtension(fileName);

		String fileid = fileName;
		GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileid)));
		if (largefile == null) {
			largefile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileid)));
		}

		if (largefile != null) {

			File originalfiletemploc = stream2file(largefile.getInputStream(), fileName, "."+ext);
			
			if (ext.equalsIgnoreCase("pdf")) {

				List<Method> methodobj = methodRepo.findByMethodkey(methodKey);

				Integer converterstatus = methodobj.get(0).getConverterstatus();
				if (converterstatus.equals(1)) // pdf to txt
				{
					 InputStream licenseStream = getLicenseStream();
					 
					 final File tempconvertedtextFile = File.createTempFile(fileName, "txt");
					
					 License asposePdfLicenseText = new License();
	 		            try {
							asposePdfLicenseText.setLicense(licenseStream);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
	 	
		            Document convertPDFDocumentToText = new Document(largefile.getInputStream());

		            TextAbsorber textAbsorber = new TextAbsorber(new TextExtractionOptions(TextExtractionOptions.TextFormattingMode.Pure));

		            convertPDFDocumentToText.getPages().accept(textAbsorber);

		            String ExtractedText = textAbsorber.getText();
		            BufferedWriter writer = null;

					try {
						writer = new BufferedWriter(new FileWriter(tempconvertedtextFile));
					} catch (IOException e) {
						e.printStackTrace();
					}
	
		            try {
						writer.write(ExtractedText);
					} catch (IOException e) {
						e.printStackTrace();
					}
		            
		            try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

		            System.out.println("Done");
		            
			        
		          byte[] bytesArray = new byte[(int) tempconvertedtextFile.length()]; 	
	              FileInputStream fis = new FileInputStream(tempconvertedtextFile);
	              fis.read(bytesArray); //read file into bytes[]
	              fis.close();	
	           		 	           
	 	          rawDataText = new String(bytesArray, StandardCharsets.UTF_8);
	 	         
				}else { //pdf to csv
 					InputStream licenseStream = getLicenseStream();
					 
	 				  final File convertedcsvfile = File.createTempFile(fileName, "csv");
						
	 			           License asposePdfLicenseCSV = new License();
	 			           try {
	 							asposePdfLicenseCSV.setLicense(licenseStream);
	 						} catch (Exception e) {
	 				    		e.printStackTrace();
	 						}       
	 				        
	 				        Document convertPDFDocumentToCSV = new Document(largefile.getInputStream());
	 				        
	 				        ExcelSaveOptions csvSave = new ExcelSaveOptions();
	 				        csvSave.setFormat(ExcelSaveOptions.ExcelFormat.CSV);
	 				        
	 				    	convertPDFDocumentToCSV.save(convertedcsvfile.getAbsolutePath(), csvSave);
	 				        System.out.println("Done");

		    	  	        byte[] bytesArray = new byte[(int) convertedcsvfile.length()]; 	
			        	    FileInputStream fis = new FileInputStream(convertedcsvfile);
			 	            fis.read(bytesArray); //read file into bytes[]
			 	            fis.close();	
			 	           		 	           
		 		 	        rawDataText = new String(bytesArray, StandardCharsets.UTF_8);
		 		 	        rawDataText = rawDataText.substring(1); // Start from index 1 to the end
			 	            rawDataText = rawDataText.replace("\n\"", "\n");
			 	            rawDataText = rawDataText.replace("\",", ",");
			 	            rawDataText = rawDataText.replace(",\"", ",");
			 	            rawDataText = rawDataText.replace("\"\"", "\"");
			 	            rawDataText=rawDataText.replace("\"\r\n", "\r\n");
				}

			} else if (ext.equalsIgnoreCase("csv")) {
				
					byte[] bytesArray = new byte[(int) originalfiletemploc.length()];
					FileInputStream fis = new FileInputStream(originalfiletemploc);
					fis.read(bytesArray); // read file into bytes[]
					fis.close();

	 	              rawDataText = new String(bytesArray, StandardCharsets.UTF_8);  

	 	              rawDataText = rawDataText.substring(1); // Start from index 1 to the end
	 	              rawDataText = rawDataText.replace("\n\"", "\n");
	 	              rawDataText = rawDataText.replace("\",", ",");
	 	       
	 	              rawDataText = rawDataText.replace(",\"", ",");
	 	          
	 	             rawDataText = rawDataText.replace("\"\"", "\"");
	 	            rawDataText=rawDataText.replace("\"\r\n", "\r\n");
			 }else if (ext.equalsIgnoreCase("xls") || ext.equalsIgnoreCase("xlsx") ) {
				  
					final File tempconvertedFile = File.createTempFile(fileName, "csv");
					
				        try (FileInputStream fis = new FileInputStream(originalfiletemploc.getAbsolutePath());
				                BufferedWriter writer = new BufferedWriter(new FileWriter(tempconvertedFile))) {
				      
				            Workbook workbook = null;
				            if (ext.equals("xls")) {
				                workbook = new HSSFWorkbook(fis); // For .xls
				            } else if (ext.equals("xlsx")) {
				                workbook = new XSSFWorkbook(fis); // For .xlsx
				            } else {
				                throw new IllegalArgumentException("The specified file is not Excel (.xls or .xlsx)");
				            }
				            
				            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet
				            DataFormatter formatter = new DataFormatter(); // To format the data as it appears in Excel

				               for (Row row : sheet) {
				                   StringBuilder sb = new StringBuilder();
				                   for (Cell cell : row) {
				                       switch (cell.getCellType()) {
				                           case STRING:
				                               sb.append(cell.getStringCellValue());
				                               break;
				                           case NUMERIC:
				                               // Use the formatter to get the exact cell value without rounding
				                               String formattedValue = formatter.formatCellValue(cell);
				                               sb.append(formattedValue);
				                               break;
				                           case BOOLEAN:
				                               sb.append(cell.getBooleanCellValue());
				                               break;
				                           case FORMULA:
				                               sb.append(cell.getCellFormula()); // Handle formula as needed
				                               break;
				                           default:
				                               sb.append("");
				                               break;
				                       }
				                       sb.append(","); // Separate columns by a comma
				                   }
				                   // Remove the last comma and add a newline
				                   writer.write(sb.toString().replaceAll(",$", ""));
				                   writer.newLine();
				               }

				               writer.flush();
				              
				           } catch (IOException e) {
				               e.printStackTrace();
				           }
				        
				           bytes = FileUtils.readFileToByteArray(tempconvertedFile);
				    	   rawDataText = new String(bytes, StandardCharsets.UTF_8); 
			               System.out.println("Conversion complete. Check " + rawDataText);
					}
			else {
				rawDataText = new BufferedReader(
						new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
						.collect(Collectors.joining("\n"));

			}
		}
		return rawDataText;
	}  

    /**
    * This method is used to get Method entity based on its primary key
    * @param methodKey [int] primary key of method entity
    * @return Method entity based on primary key
    */
   public ResponseEntity<Object> findById(final int methodKey) {
	   //Method method = methodRepo.findOne(methodKey);
	   List<Method> method = methodRepo.findByMethodkey(methodKey);
	   return new ResponseEntity<>(method.get(0), HttpStatus.OK);
   }
	  
   
   
   /**
   	 * This method is used to copy the selected 'Method', its sampling techniques, parsing techniques
	 * and custom fields to a new instrument that has not yet associated with that Method before.
	 * This copying is to be done only if the selected Method has either sample splitting techniques or parsing techniques
	 * @param request [HttpServletRequest] Request object
	 * @param mapObject [Map] object with selected Method to be loaded and the instrument key for which the Method is to be
	 * loaded. 
	 * @return response object with copied Method entity.
	 **/  
   @SuppressWarnings("unchecked")
   @Transactional
   public ResponseEntity<Object> createCopyMethod(final HttpServletRequest request, final Map<String, Object> mapObject, final int doneByUserKey){
	   
	   final ObjectMapper mapper = new ObjectMapper();
	   
	   final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
	   final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
	 //  final int doneByUserKey = (Integer) mapObject.get("doneByUserKey");
	   //final Page page = mapper.convertValue(mapObject.get("modulePage"), Page.class);
	   final int methodKey= (Integer) mapObject.get("methodKey");
	   final String methodName= (String) mapObject.get("methodName");
//	   final int instrumentKey = (Integer) mapObject.get("instMasterKey");
	   final int nequipmentcode = (Integer) mapObject.get("nequipmentcode");
	   final String methodstatus= (String) mapObject.get("methodstatus");
   
	   final MethodVersion methodversion = mapper.convertValue(mapObject.get("methodversion"), MethodVersion.class);   
	   final Optional<Method> methodByKey = methodRepo.findByMethodkeyAndStatus(methodKey, 1);	   
	   final Equipment equipment = equipmentrepository.findOne(nequipmentcode);
	   final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);
	   
	   final Method cft = mapper.convertValue(mapObject.get("auditdetails"), Method.class);
	   Date date = new Date();

	   final Optional<Method> methodByName = methodRepo.findByMethodnameAndEquipmentAndStatusAndSite(
			   methodName, equipment, 1,site);
	   
	   if (methodByKey.isPresent() && equipment != null) {		 
		   
//		   if(methodName.equals(methodByKey.get().getMethodname()))
		   if(methodByName.isPresent())
		   {
               if(saveAuditTrail) {			   
			   LScfttransaction LScfttransaction = new LScfttransaction();
			   
				LScfttransaction.setActions("Update");
				LScfttransaction.setComments("Duplicate Entry "+methodByKey.get().getMethodname());
				LScfttransaction.setLssitemaster(site.getSitecode());
				LScfttransaction.setLsuserMaster(doneByUserKey);
				LScfttransaction.setManipulatetype("View/Load");
				LScfttransaction.setModuleName("Method Master");
				LScfttransaction.setTransactiondate(date);
				LScfttransaction.setUsername(createdUser.getUsername());
				LScfttransaction.setTableName("Method");
				LScfttransaction.setSystemcoments("System Generated");
				
				lscfttransactionrepo.save(LScfttransaction);
                }
			   return new ResponseEntity<>("Duplicate Entry - " + methodByKey.get().getMethodname() +" method cannot be copied", 
  					 HttpStatus.CONFLICT); 
		   }
		   else {
//		   final Method methodBeforeSave = new Method(methodByKey.get());
		   
		   //Making entry in 'method' table for the selected instrument
		   final Method newMethod = new Method(methodByKey.get());
		   
		   MethodVersion versionobj =  methodversionrepository.save(methodversion);
		   newMethod.setMethodkey(0);
		   newMethod.setMethodname(methodName);
		   newMethod.setEquipment(equipment);
		   newMethod.setCreatedby(createdUser);
		   newMethod.setCreateddate(date);
		   newMethod.setMethodstatus(methodstatus);
		   newMethod.setObjsilentaudit(cft.getObjsilentaudit());
		   
		   final Method savedMethod = methodRepo.save(newMethod);
		   versionobj.setMethodkey(savedMethod.getMethodkey());
		   methodversionrepository.save(versionobj);
			
		   //Start - Making entries for SampleTextSplit, SampleLineSplit, SampleExtract for the newly created Method
		   
		   final List<SampleTextSplit> textList = (List<SampleTextSplit>) textSplitService.getSampleTextSplitByMethod(methodKey).getBody();		   
		   final List<SampleLineSplit> lineList = (List<SampleLineSplit>) lineSplitService.getSampleLineSplitByMethod(methodKey).getBody();		   
		   final List<SampleExtract> extractList = (List<SampleExtract>) extractService.getSampleExtractByMethod(methodKey).getBody();		   
		   	
		   final List<SampleTextSplit> textListBeforeSave	= textList.stream().map(SampleTextSplit::new).collect(Collectors.toList());
		   final List<SampleLineSplit> lineListBeforeSave	= lineList.stream().map(SampleLineSplit::new).collect(Collectors.toList());
		   final List<SampleExtract> extractListBeforeSave	= extractList.stream().map(SampleExtract::new).collect(Collectors.toList());
			
		   final Map<String, SampleTextSplit> newTextMap = new HashMap<String, SampleTextSplit>();
		   textListBeforeSave.forEach(item->{
			    int pKey = item.getSampletextsplitkey();
				item.setMethod(savedMethod);				
				item.setSampletextsplitkey(0);
				item.setCreatedby(createdUser);					
				item.setCreateddate(date);	
				newTextMap.put(Integer.toString(pKey), item);
			});	
		   
		   final Map<String, SampleLineSplit> newLineMap = new HashMap<String, SampleLineSplit>();
		   lineListBeforeSave.forEach(item->{
			    int pKey = item.getSamplelinesplitkey();
				item.setMethod(savedMethod);				
				item.setSamplelinesplitkey(0);
				item.setCreatedby(createdUser);					
				item.setCreateddate(date);	
				newLineMap.put(Integer.toString(pKey), item);
			});	
		   
		   final Map<String, SampleExtract> newExtractMap = new HashMap<String, SampleExtract>();
		   extractListBeforeSave.forEach(item->{
			    int pKey = item.getSampleextractkey();
				item.setMethod(savedMethod);				
				item.setSampleextractkey(0);
				item.setCreatedby(createdUser);					
				item.setCreateddate(date);	
				newExtractMap.put(Integer.toString(pKey), item);
			});	
		   
		    final Map<String, Object> savedSampleSplitMap = sampleSplitService.saveSampleSplitTechniques(createdUser, newTextMap, newLineMap, newExtractMap,savedMethod, mapper);
		 	  
		    savedSampleSplitMap.put("TextListBeforeSave", textList);
	    	savedSampleSplitMap.put("LineListBeforeSave", lineList);
	    	savedSampleSplitMap.put("ExtractListBeforeSave", extractList);
	    	
		    //End - Making entries for SampleTextSplit, SampleLineSplit, SampleExtract for the newly created Method
		   
		    //Start - Making entries for ParserSetup tables  for the newly created Method
		    
		    final Map<String, Object> parserObjectMap = (Map<String, Object>) parserSetupService.getParserFieldTechniqueList(methodKey).getBody();
			
		    final List<ParserBlock> parserBlockList = (List<ParserBlock>) parserObjectMap.get("ParserBlockList");
			final List<ParserField> parserFieldList = (List<ParserField>)parserObjectMap.get("ParserFieldList");
			final List<ParserTechnique> parserTechniqueList = (List<ParserTechnique>)parserObjectMap.get("ParserTechniqueList");
			final List<SubParserField> subParserFieldList = (List<SubParserField>)parserObjectMap.get("SubParserFieldList");
			final List<SubParserTechnique> subParserTechniqueList = (List<SubParserTechnique>)parserObjectMap.get("SubParserTechniqueList");
			
			final List<ParserBlock> parserBlockListBS	= parserBlockList.stream().map(ParserBlock::new).collect(Collectors.toList());
			final List<ParserField> parserFieldListBS = parserFieldList.stream().map(ParserField::new).collect(Collectors.toList());
			final List<ParserTechnique> parserTechniqueListBS =	parserTechniqueList.stream().map(ParserTechnique::new).collect(Collectors.toList());
			final List<SubParserField> subParserFieldListBS = subParserFieldList.stream().map(SubParserField::new).collect(Collectors.toList());
			final List<SubParserTechnique> subParserTechniqueListBS = subParserTechniqueList.stream().map(SubParserTechnique::new).collect(Collectors.toList());
			
			
			parserBlockListBS.forEach(item->{
				item.setMethod(savedMethod);
				item.setCreatedby(createdUser);
				item.setCreateddate(date);
				item.setParserblockkey(0);
			});
			
			parserFieldListBS.forEach(item->{
				
				final ParserBlock block = new ParserBlock();
				block.setParserblockname(item.getParserblock().getParserblockname());
				item.setParserblock(block);
				
				item.setCreatedby(createdUser);
				item.setCreateddate(date);				
				item.setParserfieldkey(0);
				
				final String fieldId = UUID.randomUUID().toString();
				
				parserTechniqueListBS.forEach(parserTechniqueItem->{
					if(item.getFieldid().equalsIgnoreCase(parserTechniqueItem.getParserfield().getFieldid()))
					{
						final ParserField field = new ParserField();
						field.setFieldid(fieldId);
						parserTechniqueItem.setParserfield(field);
						parserTechniqueItem.setCreatedby(createdUser);
						//	parserTechniqueItem.setCreateddate(date);
						parserTechniqueItem.setParsertechniquekey(0);
					}
				});
				
				subParserTechniqueListBS.forEach(subParserTechniqueItem->{
					if(item.getFieldid().equalsIgnoreCase(subParserTechniqueItem.getParserfield().getFieldid()))
					{
						final ParserField field = new ParserField();
						field.setFieldid(fieldId);
						subParserTechniqueItem.setParserfield(field);
						subParserTechniqueItem.setCreatedby(createdUser);
						subParserTechniqueItem.setCreateddate(date);
						subParserTechniqueItem.setSubparsertechniquekey(0);
					}
				});
				
				subParserFieldListBS.forEach(subParserFieldItem->{
					if(item.getFieldid().equalsIgnoreCase(subParserFieldItem.getParserfield().getFieldid()))
					{
						final ParserField field = new ParserField();
						field.setFieldid(fieldId);
						subParserFieldItem.setParserfield(field);
						subParserFieldItem.setCreatedby(createdUser);
						subParserFieldItem.setFieldid(UUID.randomUUID().toString());
						subParserFieldItem.setCreateddate(date);
						subParserFieldItem.setSubparserfieldkey(0);
					}
				});
				
				item.setFieldid(fieldId);
			});			

			
			final Map<String, Object> parserInputMap = new HashMap<String, Object>();
			parserInputMap.put("parserBlockList", parserBlockListBS);
			parserInputMap.put("parserFieldList", parserFieldListBS);
			parserInputMap.put("parserTechniqueList", parserTechniqueListBS);
			parserInputMap.put("subParserTechniqueList", subParserTechniqueListBS);
			parserInputMap.put("subParserFieldList", subParserFieldListBS);
			
			final Map<String, Object> savedParserMap = parserSetupService.saveParserSetupEntities(savedMethod, createdUser, mapper, parserInputMap);
			savedParserMap.put("ParserBlockListBeforeSave", parserBlockList);
			savedParserMap.put("ParserFieldListBeforeSave", parserFieldList);
			savedParserMap.put("ParserTechniqueListBeforeSave",parserTechniqueList);
			savedParserMap.put("SubParserFieldListBeforeSave", subParserFieldList);
			savedParserMap.put("SubParserTechniqueListBeforeSave", subParserTechniqueList);
			//End - Making entries for ParserSetup tables  for the newly created Method
			
			//Start -Saving custom fields
			
			final List<CustomField> customFieldList = customFieldRepo.findByMethodAndStatus(methodByKey.get(), 1);
			final List<CustomField> customFieldListBS	= customFieldList.stream().map(CustomField::new).collect(Collectors.toList());
						
			customFieldListBS.forEach(item->{
				item.setMethod(savedMethod);					
				item.setCreatedby(createdUser);
				item.setFieldid(UUID.randomUUID().toString());
				item.setCreateddate(date);
				item.setCustomfieldkey(0);
			});
			
//			final List<CustomField> savedCustomFieldList =  
					customFieldRepo.save(customFieldListBS);					
			//End- Saving custom fields
			
			Integer parser = null; 
		  
			if(((List<ParserBlock>)savedParserMap.get("ParserBlockListAfterSave")).isEmpty()) {
				parser = 0;
		    }
		    else {
		    	parser = 1;
		    }
			Integer sampleSplit = null; 
		 
		    if(((List<SampleTextSplit>)savedSampleSplitMap.get("TextListAfterSave")).isEmpty() 
		    		&& ((List<SampleLineSplit>)savedSampleSplitMap.get("LineListAfterSave")).isEmpty()
		    		&& ((List<SampleExtract>)savedSampleSplitMap.get("ExtractListAfterSave")).isEmpty()) {
		    	sampleSplit = 0;
		    }
		    else {
		    	sampleSplit = 1;
			 }		    
		   
	     	savedMethod.setParser(parser);
	     	savedMethod.setSamplesplit(sampleSplit);
	     	savedMethod.setDisplayvalue(savedMethod.getMethodname());
	     	savedMethod.setObjsilentaudit(cft.getObjsilentaudit());
//	    	final Method updatedMethod =
	    			methodRepo.save(savedMethod);
		
		    return new ResponseEntity<>(savedMethod, HttpStatus.OK);
		   }
	   }
	 
	   else {
		   
		   return new ResponseEntity<>("Method/Instrument Not Found", HttpStatus.NOT_FOUND);
	   }	
   } 
                                                                                                                                                                                   
                                 
   /**
    * This method is used to audit trail the 'Copy Method'.
    * @param methodBeforeSave [Method] object before saving to data base
    * @param updatedMethod [Method] object after copying to database
    * @param request [HttpServletRequest] object
    * @param savedSampleSplitMap [Map] object holding sample splitting techniques
    * @param savedParserMap [Map] object holding parsing techniques.
    * @param customFieldList [List] object holding customfield entities before copying
    * @param savedCustomFieldList [List] object holding CustomField entities after copying
    * @param createdUser [CreatedUser] Object whose has done this task
    * @param page [Page] object holding "Method" as pagename
    * @param comments [String] comments given by the user for audit recording
    * @param site [Site] object for which audit trail recording is to be done
    */ 
   /**
	 * This method is used to validate whether the instrument is already associated with the specified Method.
	 * @param method [Method] object which is to be validated
	 * @param instMasterKey [int] primary key of InstrumentMaster to be validated
	 * @return boolean value of validated response.
	 */
//   @Transactional
//   public ResponseEntity<Object> getMethodByInstrument(final Method method, final int instMasterKey){
//	   
//	   final InstrumentMaster instMaster = instMastRepo.findOne(instMasterKey);
//	   final Optional<Method> methodByInst = methodRepo.findByMethodnameAndInstmasterAndStatus(method.getMethodname(), 
//			   instMaster, 1);
//	   boolean instrumentBinded = false;
//	   if (methodByInst.isPresent()) {
//		   instrumentBinded = true;
//	   }
//	   return new ResponseEntity<>(instrumentBinded, HttpStatus.OK);
//   }
   
//   @Transactional
//   public ResponseEntity<Object> getMethodContainingByInstrument(final Method method, final int instMasterKey){
//	   
//	   final InstrumentMaster instMaster = instMastRepo.findOne(instMasterKey);
//	   final List<Method> methodByVersion = methodRepo.findByMethodnameContainingAndInstmasterAndStatus(method.getMethodname().split("@")[0].concat("@"), 
//			   instMaster, 1);
//	   String existingMethod = "";
//	   
//	   if(methodByVersion.size()>0) {
//		   final List<Integer> version = methodByVersion.stream().map((item) -> Integer.parseInt(item.getMethodname().substring(item.getMethodname().indexOf('@')+2))).collect(Collectors.toList());
//		   Integer max = version.stream().max(Integer::compare).get();
//		   existingMethod = method.getMethodname().split("@")[0].concat("@v").concat( String.valueOf(max+1));
//	   } else {
//		   final Optional<Method> methodByCopy = methodRepo.findByMethodnameAndInstmasterAndStatus(method.getMethodname(), instMaster, 1);
//		   if (methodByCopy.isPresent()) {
//			   existingMethod = method.getMethodname() + "@v2";
//		   } else {
//			   existingMethod = method.getMethodname().split("@")[0]; 
//		   }
//		   
//		   
//	   }	   
//		   
//	   return new ResponseEntity<>(existingMethod, HttpStatus.OK);
//   }


   @Transactional
   public ResponseEntity<Object> getMethodContainingByEquipment(final Method method, final int nequipmentcode){
	   
	   //final InstrumentMaster instMaster = instMastRepo.findOne(instMasterKey);
	   final Equipment equipment = equipmentrepository.findOne(nequipmentcode);
	   final List<Method> methodByVersion = methodRepo.findByMethodnameContainingAndEquipmentAndStatus(method.getMethodname().split("@")[0].concat("@"), 
			   equipment, 1);
	   String existingMethod = "";
	   
	   if(methodByVersion.size()>0) {
		   final List<Integer> version = methodByVersion.stream().map((item) -> Integer.parseInt(item.getMethodname().substring(item.getMethodname().indexOf('@')+2))).collect(Collectors.toList());
		   Integer max = version.stream().max(Integer::compare).get();
		   existingMethod = method.getMethodname().split("@")[0].concat("@v").concat( String.valueOf(max+1));
	   } else {
		   final Optional<Method> methodByCopy = methodRepo.findByMethodnameAndEquipmentAndStatus(method.getMethodname(), equipment, 1);
		   if (methodByCopy.isPresent()) {
			   existingMethod = method.getMethodname() + "@v2";
		   } else {
			   existingMethod = method.getMethodname().split("@")[0]; 
		   }
		   
		   
	   }	   
		   
	   return new ResponseEntity<>(existingMethod, HttpStatus.OK);
   }

   
}
