package com.agaram.eln.primary.service.iotconnect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.model.cfr.LSpreferences;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentCategory;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentType;
import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.methodsetup.MethodFieldTechnique;


import com.agaram.eln.primary.model.iotconnect.RCTCPResultFieldValues;
import com.agaram.eln.primary.model.iotconnect.RCTCPFileDetails;
import com.agaram.eln.primary.model.iotconnect.RCTCPResultDetails;
//import com.agaram.eln.primary.model.methodsetup.RCTCPResultDetails;
//import com.agaram.eln.primary.model.methodsetup.RCTCPResultFieldValues;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.cfr.LSpreferencesRepository;
import com.agaram.eln.primary.repository.instrumentsetup.InstCategoryRepository;
import com.agaram.eln.primary.repository.instrumentsetup.InstMasterRepository;
import com.agaram.eln.primary.repository.iotconnect.RCTCPFileDetailsRepository;
import com.agaram.eln.primary.repository.iotconnect.RCTCPResultDetailsRepository;
import com.agaram.eln.primary.repository.iotconnect.RCTCPResultFieldValuesRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.fileManipulation.FileManipulationservice;
import com.agaram.eln.primary.service.fileuploaddownload.FileStorageService;

@Service
public class IotconnectService {
	
	@Autowired
	InstMasterRepository masterRepo;
	
	@Autowired
	MethodRepository methodRepo;
	
	
	@Autowired
	InstCategoryRepository categoryRepo;
	
	@Autowired
	private LSpreferencesRepository LSpreferencesRepository;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileManipulationservice filemanipulationservice;

	@Autowired
	private RCTCPResultDetailsRepository RCTCPResultDetailsRepository;
	
	@Autowired
	private RCTCPResultFieldValuesRepository RCTCPResultFieldValuesRepository;
	
	@Autowired
	private RCTCPFileDetailsRepository RCTCPFileDetailsRepository;
	
	@Autowired
	private CloudFileManipulationservice cloudFileManipulationservice;
	
	
	public List<InstrumentCategory> getInstcategory()
	{
		return categoryRepo.findAll();
	}
	
	public List<InstrumentMaster> getInstruments(InstrumentCategory instcat)
	{
		InstrumentType insttype = new InstrumentType();
		insttype.setInsttypekey(1);
		return masterRepo.findByInstcategoryAndStatusAndInsttypeNotIn(instcat,1,insttype);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public String ConvertRawDataToFile(String rawData,Integer methodkey,Integer instmastkey,Integer isMultitenant) throws IOException {
		
		   System.out.println("entered service");
		   String FileName = "Tempfile";
		   final File tempFile = File.createTempFile(FileName, "csv");
		   String largefileUUID = null;
		   
		 
	       String filePath = tempFile.getAbsolutePath();
	       try {
	            FileWriter writer = new FileWriter(filePath);
	            writer.write(rawData);
	            writer.close();

	            System.out.println("Successfully wrote the string to the file.");
	            System.out.println("File is : "+tempFile);
	            
	        } catch (IOException e) {
	            System.out.println("An error occurred while writing to the file.");
	            e.printStackTrace();
	        }
	       
	        FileInputStream inputStream = new FileInputStream(filePath);
	        MultipartFile multipartFile = new MockMultipartFile("file",tempFile.getName(),"text/plain", inputStream );                    
	        
	        RCTCPFileDetails objfile = new RCTCPFileDetails();
	        if(isMultitenant==0) {
	        	largefileUUID = filemanipulationservice.storeLargeattachment(FileName,multipartFile);
			
	        }else {
	        	largefileUUID = cloudFileManipulationservice.storecloudfilesreturnUUID(multipartFile, "iotconnectfile");
	        }
	        
			  objfile.setFileUUID(largefileUUID);
		 	  objfile.setFilename(FileName);
		 	  objfile.setMethodkey(methodkey);
		 	  objfile.setInstrumentkey(instmastkey);
		 	  
		 	  RCTCPFileDetailsRepository.save(objfile);
		 	  largefileUUID="";
		 	
		return rawData;
		
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public void InsertRCTCPResultDetails(Object parsedData) throws IOException, ParseException {
		
		System.out.println(parsedData); 
		Map<String, List<List<MethodFieldTechnique>>> textBlocks = (Map<String, List<List<MethodFieldTechnique>>>) parsedData; // Your map initialization

		LSuserMaster userobj = new LSuserMaster();
       	userobj.setUsercode(1);
       	
       	LSSiteMaster siteobj =new LSSiteMaster();
       	siteobj.setSitecode(1);
       	
	     for (Map.Entry<String, List<List<MethodFieldTechnique>>> entry : textBlocks.entrySet()) {
	         String key = entry.getKey();
	         List<List<MethodFieldTechnique>> value = entry.getValue();

	         System.out.println("Key: " + key);

	         for (List<MethodFieldTechnique> innerList : value) {
	             for (MethodFieldTechnique technique : innerList) {
	                 
	                 System.out.println(technique);  
	                 RCTCPResultDetails rctcpresultdetailsobj = new RCTCPResultDetails();
	                 
	                 rctcpresultdetailsobj.setParserblockkey(technique.getParserfield().getParserblock().getParserblockkey());     
	                 rctcpresultdetailsobj.setMethod(technique.getParserfield().getParserblock().getMethod());     
	                 rctcpresultdetailsobj.setInstrument(technique.getParserfield().getParserblock().getMethod().getInstmaster());   
	                 rctcpresultdetailsobj.setParserfieldkey(technique.getParserfield().getParserfieldkey());    
	                 rctcpresultdetailsobj.setFieldname(technique.getFieldname());     
	                 rctcpresultdetailsobj.setCreateddate(commonfunction.getCurrentUtcTime());
	                 rctcpresultdetailsobj.setCreatedby(userobj);
	                 rctcpresultdetailsobj.setSite(siteobj);
	                 	
	                 if(technique.getParseddata().size() == 1) {
	                	 rctcpresultdetailsobj.setResults(technique.getParseddata().get(0));
	                 }else {
	                	 rctcpresultdetailsobj.setResults(technique.getParseddata().get(0));
	                	 List<String> fieldresults = technique.getParseddata();
	                	 
	                	 List<RCTCPResultFieldValues> resultfieldvaluearray = new ArrayList<>();
	                	 for(int i=0;i<fieldresults.size();i++) {
	    	                 RCTCPResultFieldValues RCTCPResultFieldValuesobj = new RCTCPResultFieldValues();

		                	 RCTCPResultFieldValuesobj.setFieldname(technique.getFieldname());
		                	 RCTCPResultFieldValuesobj.setFieldvalue(technique.getParseddata().get(i));
		                	 resultfieldvaluearray.add(RCTCPResultFieldValuesobj);
	                	 }
	                	 RCTCPResultFieldValuesRepository.save(resultfieldvaluearray);
	                	 rctcpresultdetailsobj.setRCTCPResultFieldValues(resultfieldvaluearray); 
	                	
	                 }
	                 RCTCPResultDetailsRepository.save(rctcpresultdetailsobj);
	             }
	         }
	     }

	}

public List<RCTCPResultDetails> getiotresultdetails(RCTCPResultDetails resultdetailsobj) {
		
		System.out.println(resultdetailsobj);
		
		List<RCTCPResultDetails> rctcpresultdetails  = RCTCPResultDetailsRepository.findByMethodAndInstrument(resultdetailsobj.getMethod(),resultdetailsobj.getInstrument());
		System.out.println(rctcpresultdetails);
		return rctcpresultdetails;
		//return null;
	}

	public LSpreferences getpreferencedata(Map<String, Object> mapObject) {
		LSpreferences IsRegulated = LSpreferencesRepository.findByTasksettingsAndValuesettings("RegulatedIndustry","1");
		return IsRegulated;
	}
	
	public List<Method> getMethods(InstrumentMaster instmast)
	{
		return methodRepo.findByInstmasterAndStatus(instmast,1);
		
	}
}
