package com.agaram.eln.primary.service.methodsetup;


import java.io.BufferedReader;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.util.Matrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.web.multipart.MultipartFile;

import org.springframework.mock.web.MockMultipartFile;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.SheetCreation;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.methodsetup.CloudParserFile;

import com.agaram.eln.primary.model.methodsetup.CustomField;
import com.agaram.eln.primary.model.methodsetup.Method;
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
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.instrumentsetup.InstMasterRepository;
import com.agaram.eln.primary.repository.methodsetup.CloudParserFileRepository;
import com.agaram.eln.primary.repository.methodsetup.CustomFieldRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;

import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.gridfs.GridFSDBFile;


/**
 * This Service class is used to access the MethodRepository to fetch details
 * from the 'method' table through Method Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   07- Feb- 2020
 */
@Service
public class MethodService {

	//private static String ;

	@Autowired
	MethodRepository methodRepo;
	
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

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private GridFsTemplate gridFsTemplate;
	
	
    @Autowired
    private CloudFileManipulationservice cloudFileManipulationservice;
    
    @Autowired
	LScfttransactionRepository lscfttransactionrepo;
		
    
    
	/**
	 * This method is used to retrieve list of active methods in the site.
	 * @param site [Site] object for which the methods are to be fetched
	 * @return list of methods based on site.
	 */
	@Transactional
	public ResponseEntity<Object> getActiveMethodBySite(final LSSiteMaster site){
		final List<Method> methodList =  methodRepo.findBySiteAndStatus(site, 1,
			new Sort(Sort.Direction.DESC, "methodkey"));
		return new ResponseEntity<>(methodList, HttpStatus.OK);
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
	@Transactional
	public ResponseEntity<Object> createMethod(final Method methodMaster, final LSSiteMaster site,
			final boolean saveAuditTrail, final HttpServletRequest request)
	{			
		final InstrumentMaster instMaster = instMastRepo.findOne(methodMaster.getInstmaster().getInstmastkey());
		final LSuserMaster createdUser = getCreatedUserByKey(methodMaster.getCreatedby().getUsercode());
		
		if (instMaster != null) {			
			final Optional<Method> methodByName = methodRepo.findByMethodnameAndInstmasterAndStatus(
					methodMaster.getMethodname(), instMaster, 1);
			if (methodByName.isPresent())
			{
				//Conflict =409 - Duplicate entry
				if (saveAuditTrail == true)
				{						
					final String comments = "Create Failed for duplicate method name -"+ methodMaster.getMethodname()
					+ " for the instrument - " + instMaster.getInstrumentcode();
					
//					cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//							"Create", comments, site, "",
//							createdUser, request.getRemoteAddr());
					
					LScfttransaction LScfttransaction = new LScfttransaction();
					
					LScfttransaction.setActions("Insert");
					LScfttransaction.setComments("Duplicate Entry  -"+methodMaster.getMethodname());
					LScfttransaction.setLssitemaster(site.getSitecode());
					LScfttransaction.setLsuserMaster(methodMaster.getCreatedby().getUsercode());
					LScfttransaction.setManipulatetype("View/Load");
					LScfttransaction.setModuleName("Method Master");
					LScfttransaction.setTransactiondate(methodMaster.getCreateddate());
					LScfttransaction.setUsername(methodMaster.getUsername());
					LScfttransaction.setTableName("Method");
					LScfttransaction.setSystemcoments("System Generated");
					
					lscfttransactionrepo.save(LScfttransaction);
				}
	  			return new ResponseEntity<>("Duplicate Entry - " + methodMaster.getMethodname() + " - " + instMaster.getInstrumentcode(), 
	  					 HttpStatus.CONFLICT);
			}
			else
			{							
				methodMaster.setCreatedby(createdUser);
				methodMaster.setInstmaster(instMaster);					
				final Method savedMethod = methodRepo.save(methodMaster);					
				if (saveAuditTrail == true)
				{
					final Map<String, String> fieldMap = new HashMap<String, String>();
					fieldMap.put("site", "sitename");				
					fieldMap.put("createdby", "loginid");				
					fieldMap.put("instmaster", "instrumentcode");				
					
//					final String xmlData = readWriteXML.saveXML(savedMethod, Method.class, null, "individualpojo", fieldMap);
//							
//				    cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//							"Create", "", site, xmlData, createdUser, request.getRemoteAddr());
					
	                LScfttransaction LScfttransaction = new LScfttransaction();
					
					LScfttransaction.setActions("Insert");
					LScfttransaction.setComments(methodMaster.getMethodname()+" was created by "+methodMaster.getUsername());
					LScfttransaction.setLssitemaster(site.getSitecode());
					LScfttransaction.setLsuserMaster(methodMaster.getCreatedby().getUsercode());
					LScfttransaction.setManipulatetype("View/Load");
					LScfttransaction.setModuleName("Method Master");
					LScfttransaction.setTransactiondate(methodMaster.getCreateddate());
					LScfttransaction.setUsername(methodMaster.getUsername());
					LScfttransaction.setTableName("Method");
					LScfttransaction.setSystemcoments("System Generated");
					
					lscfttransactionrepo.save(LScfttransaction);
					
				}
			
				return new ResponseEntity<>( savedMethod, HttpStatus.OK);			
			}
		}
		else {
			//Instrument not found
			if (saveAuditTrail == true)
			{						
				final String comments = "Create Failed as Instrument not found";
				
//				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//						"Create", comments, site, "",
//						createdUser, request.getRemoteAddr());
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
	 */
	@Transactional
//	public ResponseEntity<Object> updateMethod(final Method method, final LSSiteMaster site, final int doneByUserKey, 
//			   final String comments, final boolean saveAuditTrail, final HttpServletRequest request)
//	{	  		
//		final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);		
//		final InstrumentMaster instMaster = instMastRepo.findOne(method.getInstmaster().getInstmastkey());
//		
//		 final Optional<Method> methodByKey = methodRepo.findByMethodkeyAndStatus(method.getMethodkey(), 1);
//		 
//		 if(methodByKey.isPresent()) {		   
//
//		
//		if (instMaster != null) 
//		{
//
//			final Optional<Method> methodByName = methodRepo.findByMethodnameAndInstmasterAndStatus(
//					method.getMethodname(), instMaster, 1);
//			
//		
//			if (methodByName.isPresent())
//              {
//				 
//				if(methodByName.get().getMethodkey().equals(method.getMethodkey()))
//		    	{   
//				//copy of object for using 'Diffable' to compare objects
//	    			final Method methodBeforeSave = new Method(methodByName.get());
//	    			
//					method.setInstmaster(instMaster);		    			
//		    		final Method savedMethod = methodRepo.save(method);
//		    		
//		    		if (saveAuditTrail)
//	    			{
//		    			final String xmlData = convertMethodObjectToXML(methodBeforeSave, savedMethod);
//		    			
////		    			cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.USER.getActionType(),
////								"Edit", comments, site, xmlData, createdUser, request.getRemoteAddr());
//	    			}
//		    		
//		    		return new ResponseEntity<>(savedMethod , HttpStatus.OK);	
//		    	}
//				else {
//					//Conflict =409 - Duplicate entry
//	    			if (saveAuditTrail == true)
//	    			{						
//	    				final String sysComments = "Update Failed for duplicate method name - "+ method.getMethodname();
//	    				
////	    				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
////	    						"Create", sysComments, site, "",createdUser, request.getRemoteAddr());
//	    			}
//	    			
//	    			return new ResponseEntity<>("Duplicate Entry - " + method.getMethodname() + " - " + instMaster.getInstrumentcode(), 
//		  					 HttpStatus.CONFLICT);      		
//				}
//		   }
//			
//			
//			else
//	    	{			    		
//	    		//copy of object for using 'Diffable' to compare objects
//    			final Method methodBeforeSave = new Method(methodByKey.get());
//    			
//	    		//Updating fields with a new delimiter name
//    			
//	    		final Method savedMethod = methodRepo.save(method);
//	    		
//	    		if (saveAuditTrail)
//    			{
//	    			final String xmlData = convertMethodObjectToXML(methodBeforeSave, savedMethod);
//	    			
////	    			cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.USER.getActionType(),
////							"Edit", comments, site, xmlData, createdUser, request.getRemoteAddr());
//    			}
//	    		
//	    		return new ResponseEntity<>(savedMethod , HttpStatus.OK);			    		
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
//	   		    }			
//				return new ResponseEntity<>("Update Failed - Method Not Found", HttpStatus.NOT_FOUND);
//		   }
//		}
//		else {
//			//Instrument not found
//			if (saveAuditTrail == true)
//			{		
////				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
////						"Edit", "Update Failed", site, "",
////						createdUser, request.getRemoteAddr());
//
//			}
//  			return new ResponseEntity<>("Invalid Instrument", HttpStatus.NOT_FOUND);
//		}
//   }	
	
	public ResponseEntity<Object> updateMethod(final Method method, final LSSiteMaster site, final int doneByUserKey, 
			   final String comments, final boolean saveAuditTrail, final HttpServletRequest request)
	{	  		
		final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);		
		final InstrumentMaster instMaster = instMastRepo.findOne(method.getInstmaster().getInstmastkey());
		
		 final Optional<Method> methodByKey = methodRepo.findByMethodkeyAndStatus(method.getMethodkey(), 1);
		 
		 if(methodByKey.isPresent()) {		   

		
		if (instMaster != null) 
		{

			final Optional<Method> methodByName = methodRepo.findByMethodnameAndInstmasterAndStatus(
					method.getMethodname(), instMaster, 1);
			
		
			if (methodByName.isPresent())
        {
				 
				if(methodByName.get().getMethodkey().equals(method.getMethodkey()))
		    	{   
				//copy of object for using 'Diffable' to compare objects
	    			final Method methodBeforeSave = new Method(methodByName.get());
	    			
					method.setInstmaster(instMaster);		    			
		    		final Method savedMethod = methodRepo.save(method);
		    		
		    		if (saveAuditTrail)
	    			{
		    			final String xmlData = convertMethodObjectToXML(methodBeforeSave, savedMethod);
		    			
//		    			cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.USER.getActionType(),
//								"Edit", comments, site, xmlData, createdUser, request.getRemoteAddr());
		    			
		    			 LScfttransaction LScfttransaction = new LScfttransaction();
							
							LScfttransaction.setActions("Update");
							LScfttransaction.setComments(method.getMethodname()+" was updated by "+method.getUsername());
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
		    		
		    		return new ResponseEntity<>(savedMethod , HttpStatus.OK);	
		    	}
				else {
					//Conflict =409 - Duplicate entry
	    			if (saveAuditTrail == true)
	    			{						
	    				final String sysComments = "Update Failed for duplicate method name - "+ method.getMethodname();
	    				
//	    				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//	    						"Create", sysComments, site, "",createdUser, request.getRemoteAddr());
	    				
	    				 LScfttransaction LScfttransaction = new LScfttransaction();
	 					
	 					LScfttransaction.setActions("Update");
	 					LScfttransaction.setComments(" Duplicate Entry method name - "+ method.getMethodname());
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
	    			
	    			return new ResponseEntity<>("Duplicate Entry - " + method.getMethodname() + " - " + instMaster.getInstrumentcode(), 
		  					 HttpStatus.CONFLICT);      		
				}
		   }
			
			
			else
	    	{			    		
	    		//copy of object for using 'Diffable' to compare objects
			final Method methodBeforeSave = new Method(methodByKey.get());
			
	    		//Updating fields with a new delimiter name
			
	    		final Method savedMethod = methodRepo.save(method);
	    		
	    		if (saveAuditTrail)
			{
	    			final String xmlData = convertMethodObjectToXML(methodBeforeSave, savedMethod);
	    			
//	    			cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.USER.getActionType(),
//							"Edit", comments, site, xmlData, createdUser, request.getRemoteAddr());
	    			
	    			 LScfttransaction LScfttransaction = new LScfttransaction();
	 					
	 					LScfttransaction.setActions("Update");
	 					LScfttransaction.setComments(method.getMethodname()+" was updated by "+method.getUsername());
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
	    		
	    		
	    		return new ResponseEntity<>(savedMethod , HttpStatus.OK);			    		
	    	}	
			   }
			
			
			
		   else
		   {
			   //Invalid methodkey		   
			   if (saveAuditTrail) {				
//					cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//							"Edit", "Update Failed - Method Not Found", site, "", createdUser, request.getRemoteAddr());
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
//				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//						"Edit", "Update Failed", site, "",
//						createdUser, request.getRemoteAddr());
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
	 */
	 @Transactional()
	   public ResponseEntity<Object> deleteMethod(final int methodKey, 
			   final LSSiteMaster site, final String comments, final int doneByUserKey, 
			   final boolean saveAuditTrial, final HttpServletRequest request,final Method otherdetails)
	   {	   
		   final Optional<Method> methodByKey = methodRepo.findByMethodkeyAndStatus(methodKey, 1);
		   final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);
		   
		   if(methodByKey.isPresent()) {

			   final Method method = methodByKey.get();
			   if ((method.getSamplesplit() != null && method.getSamplesplit() == 1)
					   || (method.getParser() != null && method.getParser() == 1)) {
				    if (saveAuditTrial)
		    		{	
					   final String sysComments = "Delete Failed as method - "+ method.getMethodname() + " is associated samlesplit/parser";
			   			
//						cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//								"Delete", sysComments, method.getSite(), "", createdUser, request.getRemoteAddr());
					   
					   LScfttransaction LScfttransaction = new LScfttransaction();
						
						LScfttransaction.setActions("Delete"); 
						LScfttransaction.setComments("Associated - "+ method.getMethodname());
						LScfttransaction.setLssitemaster(site.getSitecode());
						LScfttransaction.setLsuserMaster(doneByUserKey);
						LScfttransaction.setManipulatetype("View/Load");
						LScfttransaction.setModuleName("Method Master");
						LScfttransaction.setTransactiondate(otherdetails.getTransactiondate());
						LScfttransaction.setUsername(otherdetails.getUsername());
						LScfttransaction.setTableName("Method");
						LScfttransaction.setSystemcoments("System Generated");
						
						lscfttransactionrepo.save(LScfttransaction);
						
		    	    }
				    return new ResponseEntity<>(method.getMethodname() , HttpStatus.IM_USED);//status code - 226	
			   }
			   else {
			   

						   
				    if (saveAuditTrial)
	    			{				    	
		    		// String xmlData = convertMethodObjectToXML(methodBeforeSave, savedMethod);
		    			
		    			//final String actionType = EnumerationInfo.CFRActionType.USER.getActionType();
//						cfrTransService.saveCfrTransaction(page, actionType, "Delete", comments, 
//								site, xmlData, createdUser, request.getRemoteAddr());
		    			 LScfttransaction LScfttransaction = new LScfttransaction();
							
							LScfttransaction.setActions("Delete"); 
							LScfttransaction.setComments(method.getMethodname()+" was deleted by "+otherdetails.getUsername());
							LScfttransaction.setLssitemaster(site.getSitecode());
							LScfttransaction.setLsuserMaster(doneByUserKey);
							LScfttransaction.setManipulatetype("View/Load");
							LScfttransaction.setModuleName("Method Master");
							LScfttransaction.setTransactiondate(otherdetails.getTransactiondate());
							LScfttransaction.setUsername(otherdetails.getUsername());
							LScfttransaction.setTableName("Method");
							LScfttransaction.setSystemcoments("System Generated");
							
							lscfttransactionrepo.save(LScfttransaction);
	    			}
				    
					   //copy of object for using 'Diffable' to compare objects
					   final Method methodBeforeSave = new Method(method); 

		    		   //Its not associated in transaction
					   method.setStatus(-1);
					   final Method savedMethod = methodRepo.save(method);   
					   
				   return new ResponseEntity<>(savedMethod, HttpStatus.OK); 
			   }
		   }
		   else
		   {
			   //Invalid methodkey
			   if (saveAuditTrial) {				
//					cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//							"Delete", "Delete Failed - Method Not Found", site, "", 
//							createdUser, request.getRemoteAddr());
				   				
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
			
			final DiffResult diffResult = methodBeforeSave.diff(savedMethod);        			
//			for(Diff<?> d: diffResult.getDiffs()) {	
//					diffObject.put(d.getFieldName(), d.getKey()+" -> "+d.getValue());
//			}
			
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
 * @throws FileNotFoundException 
 * @throws IOException 
    */
  
   
//   public String getFileData(final String fileName,String tenant) throws FileNotFoundException, IOException
//   {	   
//	   //"pdftotext.exe -layout " + "\"C://Users/Ate153/Downloads/RS 1.pdf\""
//	  
////		 
//	   try
//        {			
//		   File file = null;
//		   final String ext = FilenameUtils.getExtension(fileName); 
//		   String rawDataText="";
//		   		   		 
//		   
//		   CloudParserFile obj = cloudparserfilerepository.findByfilename(fileName);
//		    String fileid = obj.fileid;
//			   
//		//retrieving pdf data from blob	
//			byte[] data = null;
//			try {
//				data = StreamUtils
//						.copyToByteArray(cloudFileManipulationservice.retrieveCloudFile(fileid, tenant + "parserfile"));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		//	ByteArrayInputStream bis = new ByteArrayInputStream(data);
//			//converting byte stream to file
//			
//			 final String retrivedfile = FilenameUtils.getBaseName(fileName);	
//			 try (FileOutputStream fileOuputStream  = new FileOutputStream("uploads/"+ retrivedfile )){
//				 fileOuputStream .write(data);
//				 }
//			
//			 String textfilename = retrivedfile+".txt";
//		   
//		   
//		   if (ext.equalsIgnoreCase("pdf")) {		
//	
////			   final String name = FilenameUtils.getBaseName(fileName);				  
////			   final String filePath = "uploads/"+ name + ".txt";
//			     
//			 //  final String name = FilenameUtils.getBaseName(fileName);				  
//			   final String filePath = "uploads/"+ retrivedfile + ".txt";
//			   
//			   file = new File(filePath);
//			   if (!file.exists()) {
//
//				   String parsedText = "";
//				   PDFParser parser = null;
//				    PDDocument pdDoc = null;
//				    COSDocument cosDoc = null;
//				    PDFTextStripper pdfStripper;
//
//				    try {
//				    //	RandomAccessBufferedFileInputStream raFile = new RandomAccessBufferedFileInputStream(new File("uploads/"+fileName));
//				    	RandomAccessBufferedFileInputStream raFile = new RandomAccessBufferedFileInputStream(new File("uploads/"+retrivedfile));
//				        parser = new PDFParser(raFile);
//				        parser.setLenient(true);
//				        parser.parse();
//				        cosDoc = parser.getDocument();
//				        pdfStripper = new PDFTextStripper();
//				        pdfStripper.setSortByPosition( true );
//				              			       
//				        pdDoc = new PDDocument(cosDoc);
//				    //    pdfStripper.setAddMoreFormatting(true);
//				        pdfStripper.setWordSeparator("\t");
//				        pdfStripper.setSuppressDuplicateOverlappingText(true);
//				        Matrix matrix = new Matrix();
//				        matrix.clone();
//				        pdfStripper.setTextLineMatrix(matrix);
//				   
//				        parsedText = pdfStripper.getText(pdDoc);
//				                                                                                                             
//				                                                                                                                                     
//				        if (!file.exists()) {
//				            file.createNewFile();
//				        }
//				        
//				        
//				        FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
//				        BufferedWriter bw = new BufferedWriter(fw);
//
//				        bw.write(parsedText);
//				        bw.close();
//				        System.out.println(parsedText.replaceAll("[^A-Za-z0-9. ]+", ""));
//				            
//					      //converting text file to multipart file
//					        
//					        Path path = Paths.get("uploads/"+retrivedfile+".txt");
//					        String txtfilename = retrivedfile+".txt";
//					        String originalFileName = retrivedfile;
//					        String contentType = "text/plain";
//					        byte[] content = null;
//					        try {
//					            content = Files.readAllBytes(path);
//					        } catch (final IOException e) {
//					        }
//					        MultipartFile convertedmultipartfile = new MockMultipartFile(txtfilename,
//					                             originalFileName, contentType, content);
//					        
//					        //storing file in blob
//					        
//					        String textid = null;
//				    		try {
//				    			textid = cloudFileManipulationservice.storecloudfilesreturnUUID(convertedmultipartfile, "parsertextfile");
//				    		} catch (IOException e) {
//				    			// TODO Auto-generated catch block
//				    			e.printStackTrace();
//				    		}
//		
//				    		CloudParserFile objfile = new CloudParserFile();
//				    		objfile.setFileid(textid);
//		
//				    		//objfile.setFile(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
//				    		objfile.setExtension(".txt");
//				    		objfile.setFilename(txtfilename);
//				    			
//				    		cloudparserfilerepository.save(objfile);
//				    } catch (Exception e) {
//				        e.printStackTrace();
//				        try {
//				            if (cosDoc != null)
//				                cosDoc.close();
//				            if (pdDoc != null)
//				                pdDoc.close();
//				        } catch (Exception e1) {
//				            e1.printStackTrace();
//				        }
//
//				    }
//		        		          
//			   }		           
//		   }
//		   else
//		   {
//			   final String filePath = "uploads/"+fileName;
//			   file = new File(filePath);
//		   } 
//		   
//		   CloudParserFile txtobj = cloudparserfilerepository.findByfilename(textfilename);
//		   String txtfileid = txtobj.fileid;
//		   
//		 //retrieving txt data from blob	
//			byte[] txtdata = null;
//			try {
//				txtdata = StreamUtils
//						.copyToByteArray(cloudFileManipulationservice.retrieveCloudFile(txtfileid, tenant + "parsertextfile"));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		   
//			//converting byte stream to file
//			
////			 final String retrivedtxtfile = FilenameUtils.getBaseName(textfilename);	
////			 try (FileOutputStream fileOuputStream  = new FileOutputStream("uploads/"+ retrivedtxtfile)){
////				 fileOuputStream .write(data);
////				 }
//		   
//		   
////		   if (file != null && file.exists()) {
////	           byte[] bytesArray = new byte[(int) file.length()]; 	
////	           FileInputStream fis = new FileInputStream(file);
////	           fis.read(bytesArray); //read file into bytes[]
////	           fis.close();	
////	           
////	          rawDataText = new String(bytesArray, StandardCharsets.ISO_8859_1);	
////	          if (ext.equalsIgnoreCase("pdf")) {
////	          rawDataText = rawDataText.replaceAll("\r\n\r\n", "\r\n");
////	          
////	           }
////           }
//           
//		//	 String textfile = "uploads/"+ retrivedtxtfile +".txt";
//			 
//		   if (textfilename != null) {
////	           byte[] bytesArray = new byte[(int) textfile.length()]; 	
////	           FileInputStream fis = new FileInputStream(textfile);
////	           fis.read(bytesArray); //read file into bytes[]
////	           fis.close();	
//	           
//	          rawDataText = new String(txtdata, StandardCharsets.ISO_8859_1);	
//	          if (ext.equalsIgnoreCase("pdf")) {
//	          rawDataText = rawDataText.replaceAll("\r\n\r\n", "\r\n");
//	          
//	           }
//           }
//		   
//           return rawDataText;
//         
//        } 	  
//        catch (IOException e) 
//        { 
//        	return null;
//        } 
//   }
//   
        
   public String getFileData(final String fileName,String tenant) throws FileNotFoundException, IOException

   {
	   try
        {			
		   File file = null;
		   final String ext = FilenameUtils.getExtension(fileName); 
		   String rawDataText="";
		   
		   final String name = FilenameUtils.getBaseName(fileName);
		   
		   CloudParserFile obj = cloudparserfilerepository.findTop1Byfilename(fileName);
		   String fileid = obj.fileid;
			 
		   file = stream2file(cloudFileManipulationservice.retrieveCloudFile(fileid, tenant + "parserfile"),fileName, ext);
		    
		    if(file !=null)
		    {
			   if (ext.equalsIgnoreCase("pdf")) {
	
					   String parsedText = "";
					   PDFParser parser = null;
					    PDDocument pdDoc = null;
					    COSDocument cosDoc = null;
					    PDFTextStripper pdfStripper;
	
					    try {
					    	RandomAccessBufferedFileInputStream raFile = new RandomAccessBufferedFileInputStream(file);
					        parser = new PDFParser(raFile);
					        parser.setLenient(true);
					        parser.parse();
					        cosDoc = parser.getDocument();
					        pdfStripper = new PDFTextStripper();
					        pdfStripper.setSortByPosition( true );
					              			       
					        pdDoc = new PDDocument(cosDoc);
					        pdfStripper.setWordSeparator("\t");
					        pdfStripper.setSuppressDuplicateOverlappingText(true);
					        Matrix matrix = new Matrix();
					        matrix.clone();
					        pdfStripper.setTextLineMatrix(matrix);
					   
					        parsedText = pdfStripper.getText(pdDoc);
					      					        
					        //converting into multipart file
						        MultipartFile convertedmultipartfile = new MockMultipartFile(fileName,
						        		fileName, "text/plain", parsedText.getBytes());
						        
						        //storing file in blob
						        String textid = null;
					    		try {
					    			textid = cloudFileManipulationservice.storecloudfilesreturnUUID(convertedmultipartfile, "parsertextfile");
					    		} catch (IOException e) {
					    			// TODO Auto-generated catch block
					    			e.printStackTrace();
					    		}
			
					    		CloudParserFile objfile = new CloudParserFile();
					    		objfile.setFileid(textid);
					    		objfile.setExtension(".txt");
					    		objfile.setFilename(name+".txt");
					    			
					    		cloudparserfilerepository.save(objfile);
					    } catch (Exception e) {
					        e.printStackTrace();
					        try {
					            if (cosDoc != null)
					                cosDoc.close();
					            if (pdDoc != null)
					                pdDoc.close();
					        } catch (Exception e1) {
					            e1.printStackTrace();
					        }
	
					    }    
					    
					    rawDataText = new String(parsedText.getBytes(), StandardCharsets.ISO_8859_1);
				 
				        rawDataText = rawDataText.replaceAll("\r\n\r\n", "\r\n");
					   
			   }
			   else
			   {
				   rawDataText = new String(Files.readAllBytes(file.toPath()), StandardCharsets.ISO_8859_1);
			   }
		   
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
   
   
   public String getSQLFileData(String fileName) throws IOException {
	
		String Content = "";
		  String rawDataText="";
		final String ext = FilenameUtils.getExtension(fileName); 
		
	   String fileid = fileName;
		GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileid)));
		if (largefile == null) {
			largefile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileid)));
		}

		if (largefile != null) {
			
			if (ext.equalsIgnoreCase("pdf")) {
				
				   String parsedText = "";
				   PDFParser parser = null;
				    PDDocument pdDoc = null;
				    COSDocument cosDoc = null;
				    PDFTextStripper pdfStripper;

//				    try {
				    	RandomAccessBufferedFileInputStream raFile = new RandomAccessBufferedFileInputStream(largefile.getInputStream());
				        parser = new PDFParser(raFile);
				        parser.setLenient(true);
				        parser.parse();
				        cosDoc = parser.getDocument();
				        pdfStripper = new PDFTextStripper();
				        pdfStripper.setSortByPosition( true );
				              			       
				        pdDoc = new PDDocument(cosDoc);
				        pdfStripper.setWordSeparator("\t");
				        pdfStripper.setSuppressDuplicateOverlappingText(true);
				        Matrix matrix = new Matrix();
				        matrix.clone();
				        pdfStripper.setTextLineMatrix(matrix);
				   
				        parsedText = pdfStripper.getText(pdDoc);
				        
				        rawDataText = new String(parsedText.getBytes(), StandardCharsets.ISO_8859_1);
				        rawDataText = rawDataText.replaceAll("\r\n\r\n", "\r\n");
			}
			else
			{
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
	   return new ResponseEntity<>(methodRepo.findOne(methodKey), HttpStatus.OK);
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
	   final int instrumentKey = (Integer) mapObject.get("instMasterKey");
	   final String comments = (String) mapObject.get("comments");
	   
	   final Optional<Method> methodByKey = methodRepo.findByMethodkeyAndStatus(methodKey, 1);	   
	   final InstrumentMaster instMaster = instMastRepo.findOne(instrumentKey);
	   final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);
	   
	   Date date = new Date();
	   
	   if (methodByKey.isPresent() && instMaster != null) {		 
		   
		   if(methodName.equals(methodByKey.get().getMethodname()))
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
		   final Method methodBeforeSave = new Method(methodByKey.get());
		   
		   //Making entry in 'method' table for the selected instrument
		   final Method newMethod = new Method(methodByKey.get());
		   newMethod.setMethodkey(0);
		   newMethod.setMethodname(methodName);
		   newMethod.setInstmaster(instMaster);
		   newMethod.setCreatedby(createdUser);
		   newMethod.setCreateddate(date);
		   
		   final Method savedMethod = methodRepo.save(newMethod);
		   
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
			
			final List<CustomField> savedCustomFieldList =  customFieldRepo.save(customFieldListBS);					
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
	    	final Method updatedMethod = methodRepo.save(savedMethod);
				   
//		    if (saveAuditTrail)
//			{			  
//		    	auditMethodCopy(methodBeforeSave, updatedMethod, request, savedSampleSplitMap, savedParserMap, customFieldListBS,
//		    			savedCustomFieldList, createdUser, comments, site);				
//			}
	    	
			LScfttransaction LScfttransaction = new LScfttransaction();
			LScfttransaction.setActions("Insert");
			LScfttransaction.setComments("Method copied from : "+methodByKey.get().getMethodname()+" to "+methodName);
			LScfttransaction.setLssitemaster(site.getSitecode());
			LScfttransaction.setLsuserMaster(doneByUserKey);
			LScfttransaction.setManipulatetype("View/Load");
			LScfttransaction.setModuleName("Method Master");
			LScfttransaction.setUsername(createdUser.getUsername());

			LScfttransaction.setTransactiondate(date);
			LScfttransaction.setTableName("SampleExtract");
			LScfttransaction.setSystemcoments("System Generated");
			
			lscfttransactionrepo.save(LScfttransaction);
		    return new ResponseEntity<>(savedMethod, HttpStatus.OK);
		   }
	   }
	   else {
		   
//		   final String actionType = EnumerationInfo.CFRActionType.SYSTEM.getActionType();
//			
//		   cfrTransService.saveCfrTransaction(page, actionType, "Copy Method", "Copy Method Failed", 
//						site, "", createdUser, request.getRemoteAddr());
//			
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
   private void auditMethodCopy(final Method methodBeforeSave, final Method updatedMethod,
		   final HttpServletRequest request, final Map<String, Object> savedSampleSplitMap,
		   final Map<String, Object> savedParserMap, final List<CustomField> customFieldList, 
		   final List<CustomField> savedCustomFieldList, final LSuserMaster createdUser,
		   final String comments, final LSuserMaster site) {
	    String methodXML = convertMethodObjectToXML(methodBeforeSave, updatedMethod);
		
   		final Map<String, String> xmlMap = sampleSplitService.getXMLData(savedSampleSplitMap);
   		String textXML = (String) xmlMap.get("textXML");
   		String lineXML = (String) xmlMap.get("lineXML");
   		String extractXML = (String) xmlMap.get("extractXML");
   	
   		final StringBuffer xmlDataBuffer = new StringBuffer();	
   		if (methodXML.length() != 0 && methodXML.contains("<?xml")){
   			methodXML = methodXML.substring(textXML.indexOf("?>")+2);
   		}
   	
   		if (textXML.length() != 0 && textXML.contains("<?xml")){
			textXML = textXML.substring(textXML.indexOf("?>")+2);
			textXML = textXML.replace("<sampletextsplits>", "").replace("</sampletextsplits>", "");									
		}
		if (lineXML.length() != 0 && lineXML.contains("<?xml")){
			lineXML = lineXML.substring(lineXML.indexOf("?>")+2);
			lineXML = lineXML.replace("<samplelinesplits>", "").replace("</samplelinesplits>", "");
			
		}
		if (extractXML.length() != 0 && extractXML.contains("<?xml")){
			extractXML = extractXML.substring(extractXML.indexOf("?>")+2);
			extractXML = extractXML.replace("<sampleextracts>", "").replace("</sampleextracts>", "");
		}	
		
		final Map<String, String> parserXMLMap = parserSetupService.getXMLData(savedParserMap);
   	
		String blockXML = (String) parserXMLMap.get("blockXML");
		String parserFieldXML = (String) parserXMLMap.get("parserFieldXML");
		String parserTechniqueXML = (String) parserXMLMap.get("parserTechniqueXML");
		String subParserFieldXML = (String) parserXMLMap.get("subParserFieldXML");
		String subParserTechXML = (String) parserXMLMap.get("subParserTechXML");
   			    
		if (blockXML.length() != 0 && blockXML.contains("<?xml")){
			blockXML = blockXML.substring(blockXML.indexOf("?>")+2);
			blockXML = blockXML.replace("<parserblocks>", "").replace("</parserblocks>", "");									
		}
		if (parserFieldXML.length() != 0 && parserFieldXML.contains("<?xml")){
			parserFieldXML = parserFieldXML.substring(parserFieldXML.indexOf("?>")+2);
			parserFieldXML = parserFieldXML.replace("<parserfields>", "").replace("</parserfields>", "");
			
		}
		if (parserTechniqueXML.length() != 0 && parserTechniqueXML.contains("<?xml")){
			parserTechniqueXML = parserTechniqueXML.substring(parserTechniqueXML.indexOf("?>")+2);
			parserTechniqueXML = parserTechniqueXML.replace("<parsertechniques>", "").replace("</parsertechniques>", "");
		}
		if (subParserTechXML.length() != 0 && subParserTechXML.contains("<?xml")){
			subParserTechXML = subParserTechXML.substring(subParserTechXML.indexOf("?>")+2);
			subParserTechXML = subParserTechXML.replace("<subparsertechniques>", "").replace("</subparsertechniques>", "");
		}
		if (subParserFieldXML.length() != 0 && subParserFieldXML.contains("<?xml")){
			subParserFieldXML = subParserFieldXML.substring(subParserFieldXML.indexOf("?>")+2);
			subParserFieldXML = subParserFieldXML.replace("<subparserfields>", "").replace("</subparserfields>", "");
		}
		
		String customFieldXML = customFieldService.convertCustomFieldListToXML(customFieldList, savedCustomFieldList);
		 
		if (customFieldXML.length() != 0 && customFieldXML.contains("<?xml")){
			customFieldXML = customFieldXML.substring(customFieldXML.indexOf("?>")+2);
			customFieldXML = customFieldXML.replace("<customfields>", "").replace("</customfields>", "");
		}
		xmlDataBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-16\"?><combinedxml>" + methodXML + textXML + lineXML + 
				extractXML+ blockXML + parserFieldXML + parserTechniqueXML + subParserTechXML + subParserFieldXML
				 + customFieldXML 
				 + "</combinedxml>");				
		
//		final String actionType = EnumerationInfo.CFRActionType.SYSTEM.getActionType();
//		cfrTransService.saveCfrTransaction(page, actionType, "Copy Method", comments, 
//					site, xmlDataBuffer.toString(), createdUser, request.getRemoteAddr());
   }
   
   /**
	 * This method is used to validate whether the instrument is already associated with the specified Method.
	 * @param method [Method] object which is to be validated
	 * @param instMasterKey [int] primary key of InstrumentMaster to be validated
	 * @return boolean value of validated response.
	 */
   @Transactional
   public ResponseEntity<Object> getMethodByInstrument(final Method method, final int instMasterKey){
	   
	   final InstrumentMaster instMaster = instMastRepo.findOne(instMasterKey);
	   final Optional<Method> methodByInst = methodRepo.findByMethodnameAndInstmasterAndStatus(method.getMethodname(), 
			   instMaster, 1);
	   boolean instrumentBinded = false;
	   if (methodByInst.isPresent()) {
		   instrumentBinded = true;
	   }
	   return new ResponseEntity<>(instrumentBinded, HttpStatus.OK);
   }
   
   @Transactional
   public ResponseEntity<Object> getMethodContainingByInstrument(final Method method, final int instMasterKey){
	   
	   final InstrumentMaster instMaster = instMastRepo.findOne(instMasterKey);
	   final List<Method> methodByVersion = methodRepo.findByMethodnameContainingAndInstmasterAndStatus(method.getMethodname().split("@")[0].concat("@"), 
			   instMaster, 1);
	   String existingMethod = "";
	   
	   if(methodByVersion.size()>0) {
		   final List<Integer> version = methodByVersion.stream().map((item) -> Integer.parseInt(item.getMethodname().substring(item.getMethodname().indexOf('@')+2))).collect(Collectors.toList());
		   Integer max = version.stream().max(Integer::compare).get();
		   existingMethod = method.getMethodname().split("@")[0].concat("@v").concat( String.valueOf(max+1));
	   } else {
		   final Optional<Method> methodByCopy = methodRepo.findByMethodnameAndInstmasterAndStatus(method.getMethodname(), instMaster, 1);
		   if (methodByCopy.isPresent()) {
			   existingMethod = method.getMethodname() + "@v2";
		   } else {
			   existingMethod = method.getMethodname().split("@")[0]; 
		   }
		   
		   
	   }	   
		   
	   return new ResponseEntity<>(existingMethod, HttpStatus.OK);
   }




   
}
