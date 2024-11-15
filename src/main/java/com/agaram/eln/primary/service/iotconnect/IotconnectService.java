package com.agaram.eln.primary.service.iotconnect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.model.cfr.LSpreferences;
import com.agaram.eln.primary.model.equipment.Equipment;
import com.agaram.eln.primary.model.equipment.EquipmentCategory;
import com.agaram.eln.primary.model.equipment.EquipmentType;
import com.agaram.eln.primary.model.instrumentDetails.LSOrderElnMethod;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentCategory;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentType;
import com.agaram.eln.primary.model.methodsetup.ELNResultDetails;
import com.agaram.eln.primary.model.methodsetup.LSResultFieldValues;
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
import com.agaram.eln.primary.repository.equipment.EquipmentCategoryRepository;
import com.agaram.eln.primary.repository.equipment.EquipmentRepository;
import com.agaram.eln.primary.repository.equipment.EquipmentTypeRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSOrderElnMethodRepository;
import com.agaram.eln.primary.repository.instrumentsetup.InstCategoryRepository;
import com.agaram.eln.primary.repository.instrumentsetup.InstMasterRepository;
import com.agaram.eln.primary.repository.iotconnect.RCTCPFileDetailsRepository;
import com.agaram.eln.primary.repository.iotconnect.RCTCPResultDetailsRepository;
import com.agaram.eln.primary.repository.iotconnect.RCTCPResultFieldValuesRepository;
import com.agaram.eln.primary.repository.methodsetup.ELNResultDetailsRepository;
import com.agaram.eln.primary.repository.methodsetup.LSResultFieldValuesRepository;
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
	
	@Autowired
	private LSResultFieldValuesRepository LSResultFieldValuesRepository;
	
	@Autowired
	private ELNResultDetailsRepository ELNResultDetailsRepository;
	
	@Autowired
	private EquipmentTypeRepository equipmentTypeRepository;
	
	@Autowired
	private EquipmentCategoryRepository equipmentCategoryRepository;
	@Autowired
	private EquipmentRepository equipmentRepository;
	
	@Autowired
	private LSOrderElnMethodRepository LSOrderElnMethodRepository;
	
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
	public String ConvertRawDataToFile(String rawData,Integer methodkey,Integer nequipmentcode,Integer isMultitenant) throws IOException {
		
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
		 	  //objfile.setInstrumentkey(instmastkey);
		 	  objfile.setNequipmentcode(nequipmentcode);
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
	                // rctcpresultdetailsobj.setInstrument(technique.getParserfield().getParserblock().getMethod().getInstmaster());   
	                 rctcpresultdetailsobj.setEquipment(technique.getParserfield().getParserblock().getMethod().getEquipment());
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

	public void InsertIntoOrders(Object parsedData , Long batchcode) throws IOException, ParseException {
		

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
	                // RCTCPResultDetails rctcpresultdetailsobj = new RCTCPResultDetails();
	                 ELNResultDetails elnresultdetails = new ELNResultDetails();
	                 
	                 elnresultdetails.setParserblockkey(technique.getParserfield().getParserblock().getParserblockkey());     
	                 elnresultdetails.setMethodkey(technique.getParserfield().getParserblock().getMethod().getMethodkey());     
	                 elnresultdetails.setBatchcode(batchcode);
	                 elnresultdetails.setParserfieldkey(technique.getParserfield().getParserfieldkey());    
	                 elnresultdetails.setParamname(technique.getFieldname());     
	                 elnresultdetails.setCreateddate(commonfunction.getCurrentUtcTime());
	                 elnresultdetails.setCreatedby(userobj);
	                 elnresultdetails.setSite(siteobj);
	                 	
	                 if(technique.getParseddata().size() == 1) {
	                	 elnresultdetails.setResults(technique.getParseddata().get(0));
	                 }else {
	                	 elnresultdetails.setResults(technique.getParseddata().get(0));
	                	 List<String> fieldresults = technique.getParseddata();
	                	 
	                	 List<LSResultFieldValues> resultfieldvaluearray = new ArrayList<>();
	                	 for(int i=0;i<fieldresults.size();i++) {
	                		 LSResultFieldValues LSResultFieldValuesobj = new LSResultFieldValues();

	                		 LSResultFieldValuesobj.setFieldname(technique.getFieldname());
	                		 LSResultFieldValuesobj.setFieldvalue(technique.getParseddata().get(i));
		                	 resultfieldvaluearray.add(LSResultFieldValuesobj);
	                	 }
	                	 LSResultFieldValuesRepository.save(resultfieldvaluearray);
	                	 elnresultdetails.setLsresultfieldvalues(resultfieldvaluearray); 
	          
	                 }
	                 ELNResultDetailsRepository.save(elnresultdetails);
	             }
	         }
	     }
	}

	public List<RCTCPResultDetails> getiotresultdetails(List<Integer> methodkeys , List<Integer> instkeys) {
		
		List<RCTCPResultDetails> results = RCTCPResultDetailsRepository.findByMethodMethodkeyInAndEquipmentNequipmentcodeIn(methodkeys,instkeys);
		return results;

		//List<RCTCPResultDetails> rctcpresultdetails  = RCTCPResultDetailsRepository.findByMethodAndEquipment(methodkeys , instkeys);
	
		
	}
	public LSpreferences getpreferencedata(Map<String, Object> mapObject) {
		LSpreferences IsRegulated = LSpreferencesRepository.findByTasksettings("RegulatedIndustry");
		return IsRegulated;
	}
	
	public ResponseEntity<Object> getEquipmenttype(Integer nsiteInteger) {

		List<EquipmentType> lstTypes =  new ArrayList<EquipmentType>();
		lstTypes = equipmentTypeRepository.findByNequipmenttypecodeNotAndNstatusAndNsitecodeOrderByNequipmenttypecodeDesc(-1,1,nsiteInteger);

		return new ResponseEntity<>(lstTypes, HttpStatus.OK);
	}
	
	public List<EquipmentCategory> getEquipmentcat(EquipmentType equipmenttype) {
		return equipmentCategoryRepository.findByEquipmenttypeAndNsitecodeAndNstatus(equipmenttype,equipmenttype.getNsitecode(), 1);
	}

	public List<Equipment> getEquipment(EquipmentCategory equicat) {
		
		List<Equipment> lstEquipments = new ArrayList<Equipment>();

		
		//lstEquipments = equipmentRepository.findByEquipmentcategoryAndStatusNotIn(equicat,1);
		
		lstEquipments = equipmentRepository.findByEquipmentcategoryAndNstatus(equicat,1);
		return lstEquipments;
	}

	public List<Method> getEquipmentmethod(Equipment equ) {
		return methodRepo.findByEquipmentAndStatus( equ, 1);

		}
	
	public List<LSOrderElnMethod> getOrdersBasedOnmethod(Method Methodobj) {
		
		List<LSOrderElnMethod> orderelnmethod =LSOrderElnMethodRepository.findByMethodOrderByOrderelnmethodcodeDesc(Methodobj);
		
		return orderelnmethod;	
	}

}