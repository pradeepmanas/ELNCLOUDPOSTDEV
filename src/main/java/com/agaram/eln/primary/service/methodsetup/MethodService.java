package com.agaram.eln.primary.service.methodsetup;


import java.io.BufferedReader;


import java.io.BufferedWriter;
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

import com.agaram.eln.primary.exception.FileStorageException;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
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

	//private static String ;

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
	@Transactional
	public ResponseEntity<Object> createMethod(final Method methodMaster, final LSSiteMaster site, final HttpServletRequest request,Method auditdetails)
	{			
		boolean saveAuditTrail=true;
		final InstrumentMaster instMaster = instMastRepo.findOne(methodMaster.getInstmaster().getInstmastkey());
		final LSuserMaster createdUser = getCreatedUserByKey(methodMaster.getCreatedby().getUsercode());
		
		if (instMaster != null) {			
			final Optional<Method> methodByName = methodRepo.findByMethodnameAndInstmasterAndStatusAndSite(
					methodMaster.getMethodname(), instMaster, 1,site);
			if (methodByName.isPresent())
			{
				//Conflict =409 - Duplicate entry
				if (saveAuditTrail == true)
				{	
				}
				methodMaster.setInfo("Duplicate Entry - " + methodMaster.getMethodname() + " for inst : " + instMaster.getInstrumentcode());
				methodMaster.setObjsilentaudit(auditdetails.getObjsilentaudit());

				return new ResponseEntity<>(methodMaster, HttpStatus.CONFLICT);
			}
			else
			{							
				methodMaster.setCreatedby(createdUser);
				methodMaster.setInstmaster(instMaster);					
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
//				final String comments = "Create Failed as Instrument not found";
				
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
	
	public ResponseEntity<Object> updateMethod(final Method method, final LSSiteMaster site, final int doneByUserKey, 
			    final HttpServletRequest request,Method auditdetails)
	{	  		
		boolean saveAuditTrail=true;	
		final InstrumentMaster instMaster = instMastRepo.findOne(method.getInstmaster().getInstmastkey());
		
		 final Optional<Method> methodByKey = methodRepo.findByMethodkeyAndStatusAndSite(method.getMethodkey(), 1,site);
		 
		 if(methodByKey.isPresent()) {		   

		
		if (instMaster != null) 
		{

			final Optional<Method> methodByName = methodRepo.findByMethodnameAndInstmasterAndStatusAndSite(
					method.getMethodname(), instMaster, 1,site);
			
		
			if (methodByName.isPresent())
        {
				 
				if(methodByName.get().getMethodkey().equals(method.getMethodkey()))
		    	{   
				//copy of object for using 'Diffable' to compare objects
//	    			final Method methodBeforeSave = new Method(methodByName.get());
	    			
					method.setInstmaster(instMaster);		    			
		    		final Method savedMethod = methodRepo.save(method);
		    		
		    		savedMethod.setDisplayvalue(savedMethod.getMethodname());
		    		savedMethod.setScreenname("Methodmaster");
		    		savedMethod.setObjsilentaudit(auditdetails.getObjsilentaudit());	
		    		return new ResponseEntity<>(savedMethod , HttpStatus.OK);	
		    	}
				else {
					method.setInfo("Duplicate Entry - " + method.getMethodname() + " for inst " + instMaster.getInstrumentcode());
					method.setObjsilentaudit(auditdetails.getObjsilentaudit());
	    			return new ResponseEntity<>(method, HttpStatus.CONFLICT);   
				}
		   }
			
			
			else
	    	{			    		
	    	
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
			   final HttpServletRequest request,final Method otherdetails,Method auditdetails)
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
 * @throws FileNotFoundException 
 * @throws IOException 
    */
     
        
   @SuppressWarnings("resource")
public String getFileData(final String fileName,String tenant,Integer methodKey) throws FileNotFoundException, IOException
 {
	   try
        {			
		   File file = null;
		   final String ext = FilenameUtils.getExtension(fileName); 
		   String rawDataText="";
		   
		   final String name = FilenameUtils.getBaseName(fileName);
		   
		   CloudParserFile obj = cloudparserfilerepository.findTop1Byfilename(fileName);
		   String fileid = obj.fileid;
		   System.out.println("going to retrieve file from blob");
		   file = stream2file(cloudFileManipulationservice.retrieveCloudFile(fileid, tenant + "parserfile"),fileName, ext);
		   
		   byte[] bytes = null;

		    if(file !=null)
		    {
 	
		if (ext.equalsIgnoreCase("pdf")) {  	
	        List<Method> methodobj = methodRepo.findByMethodkey(methodKey);
  				
  				Integer converterstatus = methodobj.get(0).getConverterstatus();
  				if(converterstatus == 1) //pdf to txt
  				{
  				    String userDirectory = new File("").getAbsolutePath();
	 				   System.out.println("userDirectory:"+userDirectory);
	 				
	 				String commanddev =  userDirectory+"\\src\\main\\resources\\" +"Aspose License";//folder path in development 
	 				System.out.println("commanddev:"+commanddev);
			        File folder = new File(commanddev);
//
			        if (folder.exists() && folder.isDirectory()) {//presence of folder check in dev
			            System.out.println("Folder exists in dev");
			            
			          License asposePdfLicenseText = new License();
	 		            try {
	 		            	
							asposePdfLicenseText.setLicense(userDirectory+"\\src\\main\\resources\\Aspose License\\Aspose.PDF.Java.lic");
							System.out.println("license is set");
						} catch (Exception e1) {
							
							e1.printStackTrace();
							System.out.println("license is not set");
						}
	 		            
	 		        RandomAccessBufferedFileInputStream raFileinputstream = new RandomAccessBufferedFileInputStream(file);
  		            Document convertPDFDocumentToText = new Document(raFileinputstream);

  		            TextAbsorber textAbsorber = new TextAbsorber(new TextExtractionOptions(TextExtractionOptions.TextFormattingMode.Pure));

  		            convertPDFDocumentToText.getPages().accept(textAbsorber);

  		            String ExtractedText = textAbsorber.getText();
  		            BufferedWriter writer = null;

				    	final File tempFile = File.createTempFile(name, ".txt");
				    	
			            String tempPath = tempFile.toString();
			            
			            
  					try {
  						writer = new BufferedWriter(new FileWriter(tempPath));
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
  		            
  		          byte[] bytesArray = new byte[(int) tempFile.length()]; 	
     	          FileInputStream fis = new FileInputStream(tempFile);
	              fis.read(bytesArray); //read file into bytes[]
	              fis.close();	
	           		 	           
	 	          rawDataText = new String(bytesArray, StandardCharsets.UTF_8);
	 	          System.out.println("PDF-TXT-rawDataText:"+rawDataText);
	 	          tempFile.delete();
	 	          tempPath="";
	 			}else {// in cloudpath
	 				System.out.println("Folder exist in azure");
//			         String azure = "src/main/resources/"+"Aspose License";
// 		 				 System.out.println("azure:"+azure);
//  	 			         File azurefolder = new File(azure);
//  	
//  	 			         System.out.println("azureexists:"+azurefolder.exists());
//  	 			         System.out.println("azurefolderisdir:"+azurefolder.isDirectory());
//  	 			         System.out.println("License path i created :"+"src/main/resources/Aspose License/Aspose.PDF.Java.lic"); 
//  	 			         
//  	
//  	 			         String azure1 = "src//main//resources//"+"Aspose License";
//		 				 System.out.println("azure:"+azure1);
//	 			         File azurefolder1 = new File(azure1);
//	
//	 			         System.out.println("azureexists1:"+azurefolder1.exists());
//	 			         System.out.println("azurefolderisdir1:"+azurefolder1.isDirectory());
//	 			         System.out.println("License path i created :"+"src//main//resources//Aspose License//Aspose.PDF.Java.lic"); 
//	
//	 			         
//	 			         String azure2 = "src\\main\\resources\\"+"Aspose License";
//		 				 System.out.println("azure:"+azure2);
//	 			         File azurefolder2 = new File(azure2);
//	
//	 			         System.out.println("azureexists2:"+azurefolder2.exists());
//	 			         System.out.println("azurefolderisdir2:"+azurefolder2.isDirectory());
//	 			         System.out.println("License path i created :"+"src\\main\\resources\\Aspose License\\Aspose.PDF.Java.lic"); 
//	
//	 			         String azure3 = userDirectory+"src/main/resources/"+"Aspose License";
//	  		 				 System.out.println("azure:"+azure3);
//	   	 			         File azurefolder3 = new File(azure3);
//	   	
//	   	 			         System.out.println("azureexists3:"+azurefolder3.exists());
//	   	 			         System.out.println("azurefolderisdir3:"+azurefolder3.isDirectory());
//	   	 			         System.out.println("License path i created :"+userDirectory+"src/main/resources/Aspose License/Aspose.PDF.Java.lic"); 
//	   	 			        
//	   	 			     
//	   	 			     String azure4 = "src/main/resources/Aspose.PDF.Java.lic";  
//	   	 			     File f = new File(azure4);
//	   	 			     System.out.println("azureexists4:"+f.exists());
//	   	 			
//
//	   	 			     String azure5 = "src//main//resources//Aspose.PDF.Java.lic";  
//	   	 			     File f1 = new File(azure5);
//	   	 			     System.out.println("azureexists5:"+f1.exists());
//	   	 			
//	   	 		     String azure6 = "/src/main/resources/Aspose.PDF.Java.lic";  
//   	 			     File f2 = new File(azure6);
//   	 			     System.out.println("azureexists6:"+f2.exists());
//   	 			
//   	 		        String azure7 = "//src//main//resources//Aspose.PDF.Java.lic";  
//	 			     File f7 = new File(azure7);
//	 			     System.out.println("azureexists7:"+f7.exists());
//	 			
//	 			    String azure8 = "/Aspose.PDF.Java.lic";  
//	 			     File f8 = new File(azure8);
//	 			     System.out.println("azureexists8:"+f8.exists());
//	 			
//	 			    String azure9 = "//Aspose.PDF.Java.lic";  
//	 			     File f9 = new File(azure9);
//	 			     System.out.println("azureexists9:"+f9.exists());
//	 			
//			         String azure10 = userDirectory+"src/main/java/com/agaram/eln/Aspose.PDF.Java.lic";
//			         File f10 = new File(azure10);
//		  		 				 System.out.println("azureexists10:"+f10.exists());
//		   	 			 
//		
//		   	 			         String azure11 = userDirectory+"/src//main//java//com//agaram//eln/Aspose.PDF.Java.lic";
//		   	 		         File f11 = new File(azure11);
//	  		 				 System.out.println("azureexists11:"+f11.exists());
//	
//	   	 		         String azure12 = "src/main/java/com/agaram/eln/Aspose.PDF.Java.lic";
//	   	 	         File f12 = new File(azure12);
//		 				 System.out.println("azureexists12:"+f12.exists());
//
//   	 			         
//   	 		         String azure13 = "src//main//java//com//agaram//eln/Aspose.PDF.Java.lic";
//   	 	             File f13 = new File(azure13);
//	 				 System.out.println("azureexists13:"+f13.exists());
//	
//	 				String azure14 = "/pradeepmanas/ELNCLOUDPOSTDEV/blob/main/Aspose.PDF.Java.lic";
//	 				 File f14 = new File(azure14);
//	 				 System.out.println("azureexists14:"+f14.exists());
//	
//	  				String azure15 = "//pradeepmanas//ELNCLOUDPOSTDEV//blob//main//Aspose.PDF.Java.lic";
//	 				 File f15 = new File(azure15);
//	 				 System.out.println("azureexists15:"+f15.exists());
//	
//	 			//	https://github.com/pradeepmanas/ELNCLOUDPOSTDEV/tree/main/src/main/resources/Aspose%20License
//	 					
//	 			         String azure16 ="/pradeepmanas/ELNCLOUDPOSTDEV/tree/main/src/main/resources/Aspose%20License";
//	 		  		 	 System.out.println("azure:"+azure16);
//	 		   	 		 File azurefolder16 = new File(azure16);
//	 		   	 		 System.out.println("azureexists16:"+azurefolder16.exists());
//	 		   	 		
//	 		   	 	 String azure17 ="//pradeepmanas//ELNCLOUDPOSTDEV//tree//main//src//main//resources//Aspose%20License";
// 		  		 	 System.out.println("azure:"+azure17);
// 		   	 		 File azurefolder17 = new File(azure17);
// 		   	 		 System.out.println("azureexists17:"+azurefolder17.exists());
// 		   	 		
//
//	 		   	 	 String azure22 ="pradeepmanas//ELNCLOUDPOSTDEV//tree//main//src//main//resources//Aspose%20License";
// 		  		 	 System.out.println("azure:"+azure22);
// 		   	 		 File azurefolder22 = new File(azure22);
// 		   	 		 System.out.println("azureexists22:"+azurefolder22.exists());
// 		   	 		 
// 		   	 	   String azure25 ="pradeepmanas/ELNCLOUDPOSTDEV/tree/main/src/main/resources/Aspose%20License";
//		  		 	 System.out.println("azure:"+azure25);
//		   	 		 File azurefolder25 = new File(azure25);
//		   	 		 System.out.println("azureexists25:"+azurefolder25.exists());
//		   	 		
// 		   	 //	https://github.com/pradeepmanas/ELNCLOUDPOSTDEV/blob/main/src/main/java/com/agaram/eln/Aspose.PDF.Java.lic
// 		   	 		
// 	  				String azure18 = "//pradeepmanas//ELNCLOUDPOSTDEV//blob//main//java//com//agaram//eln//Aspose.PDF.Java.lic";
//				 File f18 = new File(azure18);
//				 System.out.println("azureexists18:"+f18.exists());
//
//	  				String azure19 = "/pradeepmanas/ELNCLOUDPOSTDEV/blob/main/java/com/agaram/eln/Aspose.PDF.Java.lic";
//				 File f19 = new File(azure19);
//				 System.out.println("azureexists19:"+f19.exists());
//
//	  				String azure23 = "pradeepmanas/ELNCLOUDPOSTDEV/blob/main/java/com/agaram/eln/Aspose.PDF.Java.lic";
//				 File f23 = new File(azure23);
//				 System.out.println("azureexists23:"+f23.exists());
//
//	  				String azure24 = "pradeepmanas//ELNCLOUDPOSTDEV//blob//main//java//com//agaram//eln//Aspose.PDF.Java.lic";
//				 File f24 = new File(azure24);
//				 System.out.println("azureexists24:"+f24.exists());
//
//			     String azure20 ="/pradeepmanas/ELNCLOUDPOSTDEV/tree/main/src/main/resources/Aspose License";
//		  		 	 System.out.println("azure:"+azure20);
//		   	 		 File azurefolder20 = new File(azure20);
//		   	 		 System.out.println("azureexists20:"+azurefolder20.exists());
//		   	 		
//		   	 	 String azure21 ="//pradeepmanas//ELNCLOUDPOSTDEV//tree//main//src//main//resources//Aspose License";
//	  		 	 System.out.println("azure:"+azure21);
//	   	 		 File azurefolder21 = new File(azure21);
//	   	 		 System.out.println("azureexists21:"+azurefolder21.exists());
//	   	 		
//	   	 	 String azure26 ="pradeepmanas//ELNCLOUDPOSTDEV//tree//main//src//main//resources//Aspose License";
//  		 	 System.out.println("azure:"+azure26);
//   	 		 File azurefolder26= new File(azure26);
//   	 		 System.out.println("azureexists26:"+azurefolder26.exists());
//   	 		
//		     String azure27 ="pradeepmanas/ELNCLOUDPOSTDEV/tree/main/src/main/resources/Aspose License";
//  		 	 System.out.println("azure:"+azure27);
//   	 		 File azurefolder27 = new File(azure27);
//   	 		 System.out.println("azureexists27:"+azurefolder27.exists());


//	         String azure = "src/main/resources/asposelicense";
//	 				 System.out.println("azure:"+azure);
//	 				 String absoluteTestPath = new File(azure).getAbsolutePath();
//			         File azurefolder = new File(absoluteTestPath); 	
//			         System.out.println("azureexists:"+azurefolder.exists());
//			         System.out.println("azurefolderisdir:"+azurefolder.isDirectory());
//			      	 			         
//	 			     
//	 			     String azure5 = "src/main/resources/asposelicense/Aspose.PDF.Java.lic";  
//	 			    String absoluteTestPath1 = new File(azure5).getAbsolutePath();
//	 			     File f = new File(absoluteTestPath1);
//	 			     System.out.println("azureexists4:"+f.exists());
//	 			
//	 			 String azure4 = "src/main/resources/Aspose.PDF.Java.lic";  
//	 			 String absoluteTestPath2 = new File(azure4).getAbsolutePath();
//			     File f1 = new File(absoluteTestPath2);
//			     System.out.println("azureexists4:"+f1.exists());
//			
//			    String azure8 = "/Aspose.PDF.Java.lic";  
//			   String absoluteTestPath3 = new File(azure8).getAbsolutePath();
//			     File f8 = new File(absoluteTestPath3);
//			     System.out.println("azureexists8:"+f8.exists());
//			
//
//	 		         String azure12 = "src/main/java/com/agaram/eln/Aspose.PDF.Java.lic";
//	 		     String absoluteTestPath4 = new File(azure12).getAbsolutePath();
//	 	         File f12 = new File(azure12);
// 				 System.out.println("azureexists12:"+f12.exists());


//   	 		 File f = new File("Aspose.PDF.Java.lic"); 
//   	 	     String absolute = f.getAbsolutePath(); 
//   	 	     Path path = Paths.get(absolute);
//
//   	     	 System.out.println("stringabsolute:"+absolute); 
//   	         System.out.println("absolutepath:"+path);
//   	     	 Boolean value=Files.exists(path);
   	 			
				String filePath="src/main/resources/Aspose.PDF.Java.lic";
		        Path relativePath = Paths.get(filePath);
		        Path absolutePath = relativePath.toAbsolutePath();
		        Boolean value=Files.exists(absolutePath);
		        System.out.println("existscheck:"+value);
		        
		      
		       new ClassPathResource("Aspose.PDF.Java.lic");
		       System.out.println("classpath:"+new ClassPathResource("Aspose.PDF.Java.lic"));

		        String resourceLocation = "src/main/resources/Aspose.PDF.Java.lic";

		        // Create a ClassPathResource object
		        Resource resource = new ClassPathResource(resourceLocation);
		        Boolean value1=resource.exists();
		        System.out.println("resourceexistscheck:"+value1);
		        

		        String fileName2 = "Aspose.PDF.Java.lic";

		        // Use the ClassLoader to get the resource URL
		        ClassLoader classLoader = MethodService.class.getClassLoader();
		        java.net.URL resourceUrl = classLoader.getResource(fileName2);

		        // Check if the resource URL is not null (file exists in the classpath)
		        if (resourceUrl != null) {
		            System.out.println("File '" + fileName2 + "' exists in the classpath.");
		            System.out.println("File URL: " + resourceUrl);
		            
		            // Extract the classpath entry from the URL
		          //  String classpathEntry = extractClasspathEntry(resourceUrl);

		         // Convert the URL to a string
		            String urlString = resourceUrl.toString();
		            System.out.println("urlString: " + urlString);
		            // Check if the URL is a file URL
		             if (urlString.startsWith("jar:file:")) {
		                // For JAR file URLs, extract the JAR file path
		            	 System.out.println("For JAR file URLs, extract the JAR file path");
		                int jarSeparatorIndex = urlString.indexOf('!');
		                if (jarSeparatorIndex != -1) {
		                	urlString= urlString.substring("jar:file:".length(), jarSeparatorIndex);
		                }
		            }

		            // If the URL format is not recognized, return the full URL
		          //  return urlString;
		            
		            // Print the classpath entry
		            System.out.println("Classpath Entry for '" + fileName2 + "':");
		            System.out.println(urlString);
		            
		        } else {
		            System.out.println("File '" + fileName2 + "' does not exist in the classpath.");
		        }

	 				 License asposePdfLicenseText = new License();
	 		            try {
	 		            	
							//asposePdfLicenseText.setLicense("jar:file:/tmp/app.jar!/BOOT-INF/classes!/Aspose.PDF.Java.lic");
							//System.out.println("license is set for path: jar:file:/tmp/app.jar!/BOOT-INF/classes!/Aspose.PDF.Java.lic");
//							asposePdfLicenseText.setLicense("/BOOT-INF/classes!/Aspose.PDF.Java.lic");
//							System.out.println("license is set for path: /BOOT-INF/classes!/Aspose.PDF.Java.lic");
	 		            	asposePdfLicenseText.setLicense("/tmp/app.jar!/BOOT-INF/classes!/Aspose.PDF.Java.lic");
							System.out.println("license is set for path:/tmp/app.jar!/BOOT-INF/classes!/Aspose.PDF.Java.lic");
						} catch (Exception e1) {
							                                                                                                                              
							e1.printStackTrace();
							System.out.println("license is not set");
						}
	 		            
	 		        RandomAccessBufferedFileInputStream raFileinputstream = new RandomAccessBufferedFileInputStream(file);
		            Document convertPDFDocumentToText = new Document(raFileinputstream);

		            TextAbsorber textAbsorber = new TextAbsorber(new TextExtractionOptions(TextExtractionOptions.TextFormattingMode.Pure));

		            convertPDFDocumentToText.getPages().accept(textAbsorber);

		            String ExtractedText = textAbsorber.getText();
		            BufferedWriter writer = null;

				    	final File tempFile = File.createTempFile(name, ".txt");
				    	
			            String tempPath = tempFile.toString();
			            
			            
					try {
						writer = new BufferedWriter(new FileWriter(tempPath));
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
		            
		          byte[] bytesArray = new byte[(int) tempFile.length()]; 	
  	              FileInputStream fis = new FileInputStream(tempFile);
	              fis.read(bytesArray); //read file into bytes[]
	              fis.close();	
	           		 	           
	 	          rawDataText = new String(bytesArray, StandardCharsets.UTF_8);
	 	          System.out.println("PDF-TXT-rawDataText:"+rawDataText);
	 	          tempFile.delete();
	 	          tempPath="";			
	 			  }
  				}  //pdf to txt
  			 		
  				else { //pdf to csv

  	 				    String userDirectory = new File("").getAbsolutePath();
  	 				    System.out.println(userDirectory);
  	 					String commanddev =  userDirectory+"\\src\\main\\resources\\" +"Aspose License";//folder path in development
    	 				 System.out.println("commanddev:"+commanddev);
  	 			        File folder = new File(commanddev);
  	//
  	 			        if (folder.exists() && folder.isDirectory()) { //presence of folder check in dev
  	 			            System.out.println("Folder exists in dev");
  	 			            
  	 			           License asposePdfLicenseCSV = new License();
  	 			           try {
  	 			        	   
  	 							asposePdfLicenseCSV.setLicense(userDirectory+"\\src\\main\\resources\\Aspose License\\Aspose.PDF.Java.lic");
  	 							System.out.println("License is set");
  	 						} catch (Exception e) {
  	 							
  	 				    		e.printStackTrace();
  	 				    		System.out.println("License not set");
  	 						}       
  	 				        
  	 				    
  	 			           RandomAccessBufferedFileInputStream raFileinputstream = new RandomAccessBufferedFileInputStream(file);
  	 				       Document convertPDFDocumentToCSV = new Document(raFileinputstream);
  	 				        
  	 				        ExcelSaveOptions csvSave = new ExcelSaveOptions();
  	 				        csvSave.setFormat(ExcelSaveOptions.ExcelFormat.CSV);
  	 				        
  	 				    	final File tempFile = File.createTempFile(name, ".csv");
  	 			            String tempPath = tempFile.toString();

  	 				    	 convertPDFDocumentToCSV.save(tempPath, csvSave);
  	 				        System.out.println("Done");

  	 				        byte[] bytesArray = new byte[(int) tempFile.length()]; 	
  			        	    FileInputStream fis = new FileInputStream(tempFile);
  			 	            fis.read(bytesArray); //read file into bytes[]
  			 	            fis.close();	
  			 	           		 	           
  		 		 	          rawDataText = new String(bytesArray, StandardCharsets.UTF_8);
  		 		 	          System.out.println("PDF-CSV-rawDataText:"+rawDataText);
  		 		 	          rawDataText = rawDataText.replaceAll("\"", "");
  		 		 	          tempFile.delete();
  			 	              tempPath="";
  		 			        
  	 			        } else {
  	 			         System.out.println("Folder exists in azure");
  	 			         String azure = "src/main/resources/"+"Aspose License";
  	 	 		 				 System.out.println("azure:"+azure);
  	 	  	 			         File azurefolder = new File(azure);
  	 	  	
  	 	  	 			         System.out.println("azureexists:"+azurefolder.exists());
  	 	  	 			         System.out.println("azurefolderisdir:"+azurefolder.isDirectory());
  	 	  	 			         System.out.println("License path i created :"+"src/main/resources/Aspose License/Aspose.PDF.Java.lic"); 
  	 	  	 			         
  	 	  	
  	 	  	 			         String azure1 = "src//main//resources//"+"Aspose License";
  	 			 				 System.out.println("azure:"+azure1);
  	 		 			         File azurefolder1 = new File(azure1);
  	 		
  	 		 			         System.out.println("azureexists1:"+azurefolder1.exists());
  	 		 			         System.out.println("azurefolderisdir1:"+azurefolder1.isDirectory());
  	 		 			         System.out.println("License path i created :"+"src//main//resources//Aspose License//Aspose.PDF.Java.lic"); 
  	 		
  	 		 			         
  	 		 			         String azure2 = "src\\main\\resources\\"+"Aspose License";
  	 			 				 System.out.println("azure:"+azure2);
  	 		 			         File azurefolder2 = new File(azure2);
  	 		
  	 		 			         System.out.println("azureexists2:"+azurefolder2.exists());
  	 		 			         System.out.println("azurefolderisdir2:"+azurefolder2.isDirectory());
  	 		 			         System.out.println("License path i created :"+"src\\main\\resources\\Aspose License\\Aspose.PDF.Java.lic"); 
  	 		
  	 		 			         String azure3 = userDirectory+"src/main/resources/"+"Aspose License";
  	 		  		 				 System.out.println("azure:"+azure3);
  	 		   	 			         File azurefolder3 = new File(azure3);
  	 		   	
  	 		   	 			         System.out.println("azureexists3:"+azurefolder3.exists());
  	 		   	 			         System.out.println("azurefolderisdir3:"+azurefolder3.isDirectory());
  	 		   	 			         System.out.println("License path i created :"+userDirectory+"src/main/resources/Aspose License/Aspose.PDF.Java.lic"); 
  	 		   	 			          			         
  	 		   	 			     String azure4 = userDirectory+"Aspose.PDF.Java.lic";  
  	 		   	 			     File f = new File(azure4);
  	 		   	 			     System.out.println("azureexists4:"+f.exists());
  	 		   	 			    
	 			           License asposePdfLicenseCSV = new License();
	 			           try {
	 			        	   
	 							asposePdfLicenseCSV.setLicense(azure4);
	 							System.out.println("License is set");
	 						} catch (Exception e) {
	 							
	 				    		e.printStackTrace();
	 				    		System.out.println("License not set");
	 						}       
	 				        
	 				    
	 			           RandomAccessBufferedFileInputStream raFileinputstream = new RandomAccessBufferedFileInputStream(file);
	 				       Document convertPDFDocumentToCSV = new Document(raFileinputstream);
	 				        
	 				        ExcelSaveOptions csvSave = new ExcelSaveOptions();
	 				        csvSave.setFormat(ExcelSaveOptions.ExcelFormat.CSV);
	 				        
	 				    	final File tempFile = File.createTempFile(name, ".csv");
	 			            String tempPath = tempFile.toString();

	 				    	 convertPDFDocumentToCSV.save(tempPath, csvSave);
	 				        System.out.println("Done");

	 				        byte[] bytesArray = new byte[(int) tempFile.length()]; 	
			        	    FileInputStream fis = new FileInputStream(tempFile);
			 	            fis.read(bytesArray); //read file into bytes[]
			 	            fis.close();	
			 	           		 	           
		 		 	          rawDataText = new String(bytesArray, StandardCharsets.UTF_8);
		 		 	          System.out.println("PDF-CSV-rawDataText:"+rawDataText);
		 		 	          rawDataText = rawDataText.replaceAll("\"", "");
		 		 	          tempFile.delete();
			 	              tempPath="";

  	 			          }
         				}
 				    }else if (ext.equalsIgnoreCase("csv")) {
 				    	
     		    	 RandomAccessBufferedFileInputStream raFileinputstream = new RandomAccessBufferedFileInputStream(file);
     		    	 
 				     File templocfile = stream2file(raFileinputstream,fileName, ".csv");
	 		  		 File newfile = new File(templocfile.getAbsolutePath());
	 		  			 
   	  	              byte[] bytesArray = new byte[(int) templocfile.length()]; 	
        	          FileInputStream fis = new FileInputStream(newfile);
	                  fis.read(bytesArray); //read file into bytes[]
 	                  fis.close();	
		 	          
	 	              rawDataText = new String(bytesArray, StandardCharsets.UTF_8);
	 	              rawDataText = rawDataText.replaceAll("\"", "");
	 	               System.out.println("CSVFile-rawDataText:"+rawDataText);
	 	          
  			 }
  			else
  			{
  				 RandomAccessBufferedFileInputStream raFileinputstream = new RandomAccessBufferedFileInputStream(file);
  			    rawDataText = new BufferedReader(
  				new InputStreamReader(raFileinputstream, StandardCharsets.UTF_8)).lines()
  					.collect(Collectors.joining("\n"));
  			   System.out.println("TXTFile-rawDataText:"+rawDataText);
  
  		    } 

		    }	   
			System.out.println(rawDataText); 
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
   
   @SuppressWarnings("resource")
  	public String getSQLFileData(String fileName,Integer methodKey) throws IOException, InterruptedException {
//  		String Content = "";
  		String rawDataText="";
  		byte[] bytes = null;

  		final String ext = FilenameUtils.getExtension(fileName); 
  		final String name = FilenameUtils.getBaseName(fileName);
  		
  	   String fileid = fileName;
  		GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileid)));
  		if (largefile == null) {
  			largefile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileid)));
  		}

  		if (largefile != null) {
  			
  			if (ext.equalsIgnoreCase("pdf")) {
  				
//  				List<Method> methodobj = methodRepo.findByInstrawdataurl(fileName);
  				List<Method> methodobj = methodRepo.findByMethodkey(methodKey);
  				
  				Integer converterstatus = methodobj.get(0).getConverterstatus();
  				if(converterstatus == 1) //pdf to txt
  				{
  				    String userDirectory = new File("").getAbsolutePath();
	 				   System.out.println(userDirectory);
	 				
	 				String commanddev =  userDirectory+"\\src\\main\\resources\\" +"Aspose License";//folder path in development 
	 				System.out.println(commanddev);
			        File folder = new File(commanddev);
//
			        if (folder.exists() && folder.isDirectory()) {//presence of folder check in dev
			            System.out.println("Folder exists in dev");
			            
			          License asposePdfLicenseText = new License();
	 		            try {
							asposePdfLicenseText.setLicense(userDirectory+"\\src\\main\\resources\\Aspose License\\Aspose.PDF.Java.lic");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
	 	
  		            Document convertPDFDocumentToText = new Document(largefile.getInputStream());

  		            TextAbsorber textAbsorber = new TextAbsorber(new TextExtractionOptions(TextExtractionOptions.TextFormattingMode.Pure));

  		            convertPDFDocumentToText.getPages().accept(textAbsorber);

  		            String ExtractedText = textAbsorber.getText();
  		            BufferedWriter writer = null;

				    	final File tempFile = File.createTempFile(name, ".txt");
			            String tempPath = tempFile.toString();
			            
  					try {
  						writer = new BufferedWriter(new FileWriter(tempPath));
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
  		            
  		          byte[] bytesArray = new byte[(int) tempFile.length()]; 	
     	          FileInputStream fis = new FileInputStream(tempFile);
	              fis.read(bytesArray); //read file into bytes[]
	              fis.close();	
	           		 	           
	 	          rawDataText = new String(bytesArray, StandardCharsets.UTF_8);
	 	          tempFile.delete();
	 	          tempPath="";
	 			}else { //folder path for tomcat
	 				 
  				  License asposePdfLicenseText = new License();
  				System.out.println("now entering into tomcat");
  				   System.out.println(env.getProperty("parserconvertor"));
  				 System.out.println(env.getProperty("parserconvertor")+"Aspose License/Aspose.PDFforJava.lic");
  				 
	 		            try {      	
	 		            	asposePdfLicenseText.setLicense(env.getProperty("parserconvertor")+"Aspose License/Aspose.PDF.Java.lic");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
	 	
		            Document convertPDFDocumentToText = new Document(largefile.getInputStream());

		            TextAbsorber textAbsorber = new TextAbsorber(new TextExtractionOptions(TextExtractionOptions.TextFormattingMode.Pure));

		            convertPDFDocumentToText.getPages().accept(textAbsorber);

		            String ExtractedText = textAbsorber.getText();

		            BufferedWriter writer = null;

				    	final File tempFile = File.createTempFile(name, ".txt");
			            String tempPath = tempFile.toString();
			            
					try {
						writer = new BufferedWriter(new FileWriter(tempPath));
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
		            
		          byte[] bytesArray = new byte[(int) tempFile.length()]; 	
   	              FileInputStream fis = new FileInputStream(tempFile);
	              fis.read(bytesArray); //read file into bytes[]
	              fis.close();	
	           		 	           
	 	          rawDataText = new String(bytesArray, StandardCharsets.UTF_8);
	 	          tempFile.delete();
	 	          tempPath="";
  				}
	 				
  				}  //pdf to txt
  					
  					
  				else { //pdf to csv

  	 				    String userDirectory = new File("").getAbsolutePath();
  	 				    System.out.println(userDirectory);
  	 					String commanddev =  userDirectory+"\\src\\main\\resources\\" +"Aspose License";//folder path in development
    	 				 System.out.println(commanddev);
  	 			        File folder = new File(commanddev);
  	//
  	 			        if (folder.exists() && folder.isDirectory()) { //presence of folder check in dev
  	 			            System.out.println("Folder exists in dev");
  	 			            
  	 			           License asposePdfLicenseCSV = new License();
  	 			           try {
  	 							asposePdfLicenseCSV.setLicense(userDirectory+"\\src\\main\\resources\\Aspose License\\Aspose.PDF.Java.lic");
  	 						} catch (Exception e) {
  	 				    		e.printStackTrace();
  	 						}       
  	 				        
  	 				      //  Document convertPDFDocumentToCSV = new Document("uploads/" + fileName);
  	 				       Document convertPDFDocumentToCSV = new Document(largefile.getInputStream());
  	 				        
  	 				        ExcelSaveOptions csvSave = new ExcelSaveOptions();
  	 				        csvSave.setFormat(ExcelSaveOptions.ExcelFormat.CSV);
  	 				        
  	 				    	final File tempFile = File.createTempFile(name, ".csv");
  	 			            String tempPath = tempFile.toString();
  	 			            
  	 				      //  convertPDFDocumentToCSV.save("uploads/" + name + ".csv", csvSave);
//  	 				       convertPDFDocumentToCSV.save(userDirectory+"\\src\\main\\resources\\conversion-files\\GUAVA export.csv", csvSave);

  	 				    	 convertPDFDocumentToCSV.save(tempPath, csvSave);
  	 				        System.out.println("Done");

  	    				    //   File file = new File(userDirectory+"\\src\\main\\resources\\conversion-files\\GUAVA export.csv");
  			  		    	
  		    	  	               byte[] bytesArray = new byte[(int) tempFile.length()]; 	
  			        	           FileInputStream fis = new FileInputStream(tempFile);
  			 	                   fis.read(bytesArray); //read file into bytes[]
  			 	                   fis.close();	
  			 	           		 	           
  		 		 	          rawDataText = new String(bytesArray, StandardCharsets.UTF_8);
  		 		 	          rawDataText = rawDataText.replaceAll("\"", "");
  		 		 	          tempFile.delete();
  			 	              tempPath="";
  		 			        
  	 			        } else {//folder path in tomcat
  	 			        	 License asposePdfLicenseCSV = new License();
  	 			            System.out.println("now entering into tomcat");

  	 	  				   System.out.println(env.getProperty("parserconvertor"));
  	 	  				 System.out.println(env.getProperty("parserconvertor")+"Aspose License/Aspose.PDFforJava.lic");
  	 			           try {
  								asposePdfLicenseCSV.setLicense(env.getProperty("parserconvertor")+"Aspose License/Aspose.PDF.Java.lic");
  							} catch (Exception e) {
  						
  								e.printStackTrace();
  							}      
  	 			        
  						 System.out.println(userDirectory);
  					
  						  Document convertPDFDocumentToCSV = new Document(largefile.getInputStream());
  					        
  					        ExcelSaveOptions csvSave = new ExcelSaveOptions();
  					        csvSave.setFormat(ExcelSaveOptions.ExcelFormat.CSV);
  					        
  					    	final File tempFile = File.createTempFile(name, ".csv");
  				            String tempPath = tempFile.toString();

  					    	 convertPDFDocumentToCSV.save(tempPath, csvSave);
  					        System.out.println("Done");
                                                                                                                                                
  		    	  	               byte[] bytesArray = new byte[(int) tempFile.length()]; 	
  			        	           FileInputStream fis = new FileInputStream(tempFile);
  			 	                   fis.read(bytesArray); //read file into bytes[]
  			 	                   fis.close();	
  			 	           		 	                                                                             
  		 		 	          rawDataText = new String(bytesArray, StandardCharsets.UTF_8);
  		 		 	          rawDataText = rawDataText.replaceAll("\"", "");
  		 		 	          tempFile.delete();
  			 	              tempPath="";
  	 			           }			
         				}
 				    }else if (ext.equalsIgnoreCase("csv")) {

 				     File templocfile = stream2file(largefile.getInputStream(),fileName, ".csv");
	 		  		 File newfile = new File(templocfile.getAbsolutePath());
	 		  			 
   	  	              byte[] bytesArray = new byte[(int) templocfile.length()]; 	
        	          FileInputStream fis = new FileInputStream(newfile);
	                  fis.read(bytesArray); //read file into bytes[]
 	                  fis.close();	
		 	          
	 	              rawDataText = new String(bytesArray, StandardCharsets.UTF_8);
	 	              rawDataText = rawDataText.replaceAll("\"", "");
	 	          
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
	   final String methodstatus= (String) mapObject.get("methodstatus");
//	   final String comments = (String) mapObject.get("comments");
	   
	   final Optional<Method> methodByKey = methodRepo.findByMethodkeyAndStatus(methodKey, 1);	   
	   final InstrumentMaster instMaster = instMastRepo.findOne(instrumentKey);
	   final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);
	   
	   final Method cft = mapper.convertValue(mapObject.get("auditdetails"), Method.class);
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
//		   final Method methodBeforeSave = new Method(methodByKey.get());
		   
		   //Making entry in 'method' table for the selected instrument
		   final Method newMethod = new Method(methodByKey.get());
		   newMethod.setMethodkey(0);
		   newMethod.setMethodname(methodName);
		   newMethod.setInstmaster(instMaster);
		   newMethod.setCreatedby(createdUser);
		   newMethod.setCreateddate(date);
		   newMethod.setMethodstatus(methodstatus);
		   newMethod.setObjsilentaudit(cft.getObjsilentaudit());
		   
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
