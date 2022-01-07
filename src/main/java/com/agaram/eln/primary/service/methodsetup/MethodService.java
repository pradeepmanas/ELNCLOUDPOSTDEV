package com.agaram.eln.primary.service.methodsetup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.util.Matrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
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
import com.agaram.eln.primary.repository.instrumentsetup.InstMasterRepository;
import com.agaram.eln.primary.repository.methodsetup.CustomFieldRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	public ResponseEntity<Object> updateMethod(final Method method, final LSSiteMaster site, final int doneByUserKey, 
			   final String comments, final boolean saveAuditTrail, final HttpServletRequest request)
	{	  		
		final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);		
		final InstrumentMaster instMaster = instMastRepo.findOne(method.getInstmaster().getInstmastkey());
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
	    			}
	    			
	    			return new ResponseEntity<>("Duplicate Entry - " + method.getMethodname() + " - " + instMaster.getInstrumentcode(), 
		  					 HttpStatus.CONFLICT);      		
				}
		   }
		   else
		   {
			   //Invalid methodkey		   
			   if (saveAuditTrail) {				
//					cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//							"Edit", "Update Failed - Method Not Found", site, "", createdUser, request.getRemoteAddr());
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
		   final boolean saveAuditTrial, final HttpServletRequest request)
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
		   			
//					cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//							"Delete", sysComments, method.getSite(), "", createdUser, request.getRemoteAddr());
	    	    }
			    return new ResponseEntity<>(method.getMethodname() , HttpStatus.IM_USED);//status code - 226	
		   }
		   else {
		   
			   //copy of object for using 'Diffable' to compare objects
			   final Method methodBeforeSave = new Method(method); 

    		   //Its not associated in transaction
			   method.setStatus(-1);
			   final Method savedMethod = methodRepo.save(method);   
					   
			    if (saveAuditTrial)
    			{				    	
	    			final String xmlData = convertMethodObjectToXML(methodBeforeSave, savedMethod);
	    			
	    			//final String actionType = EnumerationInfo.CFRActionType.USER.getActionType();
//					cfrTransService.saveCfrTransaction(page, actionType, "Delete", comments, 
//							site, xmlData, createdUser, request.getRemoteAddr());
    			}
			   
			   return new ResponseEntity<>(savedMethod, HttpStatus.OK); 
		   }
	   }
	   else
	   {
		   //Invalid methodkey
		   if (saveAuditTrial) {				
//				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//						"Delete", "Delete Failed - Method Not Found", site, "", 
//						createdUser, request.getRemoteAddr());
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
    * @param fileName [String] name of pdf/txt/csv file to convert
    * @return string of text file content
    */
   @SuppressWarnings("unused")
   public String getFileData(final String fileName)
   {	   
	   //"pdftotext.exe -layout " + "\"C://Users/Ate153/Downloads/RS 1.pdf\""
	   try
        {			
		   File file = null;
		   final String ext = FilenameUtils.getExtension(fileName); 
		   String rawDataText="";
		 
		   if (ext.equalsIgnoreCase("pdf")) {		
		   //if (fileName.toLowerCase().endsWith(".pdf")) {
			   //final String filePath = "uploads/"+fileName.substring(0, fileName.lastIndexOf(".")) +".txt";
			   final String name = FilenameUtils.getBaseName(fileName);				  
			   final String filePath = "uploads/"+ name + ".txt";
			     
			   file = new File(filePath);
			   if (!file.exists()) {
				   
//		           final String command =  "pdftotext.exe -layout " + '"'+ "uploads/" + fileName + '"';
//			        
//		           final Runtime run  = Runtime.getRuntime(); 
//		           final Process proc = run.exec(command); 	
//		        
//		           try {
//					Thread.sleep(3000);
//				} catch (InterruptedException e2) {
//					// TODO Auto-generated catch block
//					e2.printStackTrace();
//				}
				   
				   String parsedText = "";
				   PDFParser parser = null;
				    PDDocument pdDoc = null;
				    COSDocument cosDoc = null;
				    PDFTextStripper pdfStripper;

				    try {
				    	RandomAccessBufferedFileInputStream raFile = new RandomAccessBufferedFileInputStream(new File("uploads/"+fileName));
				        parser = new PDFParser(raFile);
				        parser.setLenient(true);
				        parser.parse();
				        cosDoc = parser.getDocument();
				        pdfStripper = new PDFTextStripper();
				        pdDoc = new PDDocument(cosDoc);
				        pdfStripper.setAddMoreFormatting(true);
				        pdfStripper.setWordSeparator("\t");
				        pdfStripper.setSuppressDuplicateOverlappingText(true);
				        Matrix matrix = new Matrix();
				        matrix.clone();
				        pdfStripper.setTextLineMatrix(matrix);
				   
				        parsedText = pdfStripper.getText(pdDoc);
				        
				        if (!file.exists()) {
				            file.createNewFile();
				        }

				        FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
				        BufferedWriter bw = new BufferedWriter(fw);

				        bw.write(parsedText);
				        bw.close();
				        System.out.println(parsedText.replaceAll("[^A-Za-z0-9. ]+", ""));
				        
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
		        
		          
			   }		           
		   }
		   else
		   {
			   final String filePath = "uploads/"+fileName;
			   file = new File(filePath);
		   } 
		   
		   if (file != null && file.exists()) {
	           byte[] bytesArray = new byte[(int) file.length()]; 	
	           FileInputStream fis = new FileInputStream(file);
	           fis.read(bytesArray); //read file into bytes[]
	           fis.close();	
	           
	          rawDataText = new String(bytesArray, StandardCharsets.ISO_8859_1);	
	          if (ext.equalsIgnoreCase("pdf")) {
	          rawDataText = rawDataText.replaceAll("\r\n\r\n", "\r\n");
	          }
           }
           
           return rawDataText;
         
        } 	  
        catch (IOException e) 
        { 
        	return null;
        } 
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
   public ResponseEntity<Object> createCopyMethod(final HttpServletRequest request, final Map<String, Object> mapObject){
	   
	   final ObjectMapper mapper = new ObjectMapper();
	   
	   final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
	   final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
	   final int doneByUserKey = (Integer) mapObject.get("doneByUserKey");
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
		    return new ResponseEntity<>(savedMethod, HttpStatus.OK);
		  
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
