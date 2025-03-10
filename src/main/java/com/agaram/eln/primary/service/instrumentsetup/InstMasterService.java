package com.agaram.eln.primary.service.instrumentsetup;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.model.equipment.Equipment;
import com.agaram.eln.primary.model.equipment.EquipmentCategory;
import com.agaram.eln.primary.model.instrumentsetup.FileSettings;
import com.agaram.eln.primary.model.instrumentsetup.InstMethod;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentCategory;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentRights;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentType;
import com.agaram.eln.primary.model.instrumentsetup.Rs232Settings;
import com.agaram.eln.primary.model.instrumentsetup.TcpSettings;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.equipment.EquipmentCategoryRepository;
import com.agaram.eln.primary.repository.equipment.EquipmentRepository;
import com.agaram.eln.primary.repository.instrumentsetup.InstCategoryRepository;
import com.agaram.eln.primary.repository.instrumentsetup.InstMasterRepository;
import com.agaram.eln.primary.repository.instrumentsetup.InstMethodRepository;
import com.agaram.eln.primary.repository.instrumentsetup.InstRightsRepository;
import com.agaram.eln.primary.repository.instrumentsetup.InstTypeRepository;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;

/**
 * This Service class is used to access the InstMasterRepository to fetch details
 * from the 'instrumentmaster' table through 'InstrumentMaster' Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   11- Nov- 2019
 */
@Service
public class InstMasterService {
	
	@Autowired
	InstMasterRepository masterRepo;
	
	@Autowired
	LSSiteMasterRepository siteRepo;
	
	@Autowired
	InstCategoryRepository categoryRepo;
	
	@Autowired
	InstTypeRepository typeRepo;
	
	@Autowired
	FileSettingsService fileService;
	
	@Autowired
	Rs232SettingsService rs232Service;
	
	@Autowired
	TcpSettingsService tcpService;	
	
	@Autowired
	LSuserMasterRepository userRepo;
	
	@Autowired
	InstRightsRepository rightsRepo;
	
	@Autowired
	EquipmentCategoryRepository equipmentCategoryRepository;
	
	@Autowired
	EquipmentRepository equipmentRepository;
//	@Autowired
//	ReadWriteXML readWriteXML;
	
//	@Autowired
//	CfrTransactionService cfrTransService;
	
	@Autowired
	InstMethodRepository instMethodRepo;
	

	@Autowired
	LScfttransactionRepository lscfttransactionrepo;
	
	
	/**
     * This method is used to add new instrument master.
     * @param master [InstrumentMaster] -  holding details of all fields in 'InstrumentMaster' Pojo.
     * @param saveAuditTrial [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
     * @param page [Page] entity relating to 'Role'
     * @param request [HttpServletRequest] Request object to ip address of remote client
     * @return Response of newly added instrumentmaster entity
     */
	@Transactional
    public ResponseEntity<Object> createInstMaster(final InstrumentMaster master,
    		 final HttpServletRequest request,InstrumentMaster auditdetails) 
    {      	
		boolean saveAuditTrial=true;
//		final Optional<InstrumentMaster> masterByCode = masterRepo.findByInstrumentcodeAndSiteAndStatus(
//    			master.getInstrumentcode(), master.getSite(), 1);
		final Optional<InstrumentMaster> masterByCode = masterRepo.findByInstrumentcodeAndSiteAndStatus(master.getInstrumentcode(), master.getSite(), 1);
		final List<InstrumentMaster> masterByname = masterRepo.findByInstrumentnameAndSiteAndStatus(master.getInstrumentname(), master.getSite(), 1);
		if (masterByCode.isPresent())
		{
			//Conflict =409 - Duplicate entry

			master.setInfo("Duplicate Entry - " + masterByCode.get().getInstrumentcode());
 			master.setObjsilentaudit(auditdetails.getObjsilentaudit());

//  			return new ResponseEntity<>("Duplicate Entry - " + masterByCode.get().getInstrumentcode(), 
//  					 HttpStatus.CONFLICT);
  			return new ResponseEntity<>(master,HttpStatus.CONFLICT);
		}    		
		else if (!masterByname.isEmpty()){
			
			master.setInfo("Duplicate Entry - " + masterByname.get(0).getInstrumentname());
 			master.setObjsilentaudit(auditdetails.getObjsilentaudit());

//  			return new ResponseEntity<>("Duplicate Entry - " + masterByCode.get().getInstrumentcode(), 
//  					 HttpStatus.CONFLICT);
  			return new ResponseEntity<>(master,HttpStatus.CONFLICT);
		}
		
		else
		{	
			final LSuserMaster createdUser = getCreatedUserByKey(master.getCreatedby().getUsercode());			
			master.setCreatedby(createdUser);
			
			final InstrumentType instType = typeRepo.findOne(master.getInsttype().getInsttypekey());
			master.setInsttype(instType);
	       		
	    	final InstrumentCategory instCategory = categoryRepo.findOne(master.getInstcategory().getInstcatkey()); 
	    	master.setInstcategory(instCategory);
			
			final InstrumentMaster savedMaster = masterRepo.save(master);
			
			saveInstTypeSettings(savedMaster);
			   	
	   		saveAdminInstRights(savedMaster);
			
	   		savedMaster.setDisplayvalue(savedMaster.getInstrumentname());
	   		savedMaster.setScreenname("Instrument");
	   		savedMaster.setObjsilentaudit(auditdetails.getObjsilentaudit());
			if (saveAuditTrial == true)
			{
			
				final Map<String, String> fieldMap = new HashMap<String, String>();
				fieldMap.put("site", "sitename");		
				fieldMap.put("createdby", "loginid");				
				fieldMap.put("insttype", "insttypename");
				fieldMap.put("instcategory", "instcatname");

		
			}			
			

    		return new ResponseEntity<>( savedMaster, HttpStatus.OK);
		}	    	
    }
	
	/**
	 * This method is used to save the details based on whether the instrument belongs to 
	 * 'FileSettings'/'TcpSettings'/'Rs232Settings'.
	 * @param savedMaster [InstrumentMaster] entity for which the type settings is to be saved
	 * @return map object with saved type settings
	 */
	private Map<String, Object> saveInstTypeSettings(final InstrumentMaster savedMaster)
	{
		final Map<String, Object> savedObject = new HashMap<String, Object>();
    	if (savedMaster.getInsttype().getInsttypename().equalsIgnoreCase("File"))
  		{
  			final FileSettings fileSettings = new FileSettings();
  			fileSettings.setInstmaster(savedMaster);
  			
  			final FileSettings savedFileSettings = (FileSettings)
  					fileService.createFileSettings(fileSettings).getBody();
  			savedObject.put("FileSettings", savedFileSettings);
  		 }
  		 else if (savedMaster.getInsttype().getInsttypename().equalsIgnoreCase("Rs232"))
  		 {
  			 final Rs232Settings rs232Settings = new Rs232Settings();
  			 rs232Settings.setInstmaster(savedMaster);
  			 
  			 final Rs232Settings savedRs232Settings = (Rs232Settings) 
  					 rs232Service.createRs232Settings(rs232Settings).getBody();
  			savedObject.put("Rs232Settings", savedRs232Settings);
  			 
  		 }
  		 else
  		 {
  			 final TcpSettings tcpSettings = new TcpSettings();
  			 tcpSettings.setInstmaster(savedMaster);	
  			 
  			 final TcpSettings savedTcpSettings = (TcpSettings) 
  					 tcpService.createTcpSettings(tcpSettings).getBody();
  			 savedObject.put("TcpSettings", savedTcpSettings);
  		 }
    	
    	return savedObject;
	}
  
    /**
     * This method is used to assign the specified instrument to the 'Administrator' user
     * once the instrument is newly added to the site.
     * @param savedMaster [InstrumentMaster] entity for which the administrator has to be
     *  					assigned with rights to use it
     * @return assigned instrument rights
     */
    private InstrumentRights saveAdminInstRights(final InstrumentMaster savedMaster) {
    	
    	//Administrator id has to be used  	
    	final LSuserMaster user =  userRepo.findOne(1);
     	final LSSiteMaster userSite =  user.getLssitemaster();
    	
    	final InstrumentRights rights =  new InstrumentRights();
    	rights.setUsersite(userSite);
    	rights.setMaster(savedMaster);
    	rights.setCreatedby(savedMaster.getCreatedby());

    	return rightsRepo.save(rights);    	
    } 
    
   
    /**
     * This method is used to update the selected 'master' details based on provided 
     * primary key id and details of the  updated fields.
     * Need to validate that if instrument is associated with method setup then its instrumenttype
     * cannot be changed so restricting updating instrument in this case
     * @param master [InstrumentMaster] -  holding details of 'InstrumentMaster' Pojo to update.
     * @param saveAuditTrial [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
     * @param page [Page] entity relating to 'InstrumentMaster'
     * @param comments [String] comments given for audit trail recording
     * @param request [HttpServletRequest] Request object to ip address of remote client
     * @return Response entity relevant to update response
     */
    @Transactional
        
    
    public ResponseEntity<Object> updateInstMaster(final InstrumentMaster master, final String comments, final HttpServletRequest request,InstrumentMaster auditdetails) {
    	    
    	final Optional<InstrumentMaster> instrumentByCode = masterRepo.findByInstrumentcodeAndSiteAndStatus(
   			master.getInstrumentcode(), master.getSite(), 1);
    	
    	final Optional<InstrumentMaster> instrumentBykey = masterRepo.findByInstmastkeyAndSiteAndStatus(
    			master.getInstmastkey(), master.getSite(), 1);
    	
    	if (instrumentBykey.isPresent())
 	    {		    		
    		final List<InstMethod> methodList = instMethodRepo.findByInstmasterAndStatus(instrumentBykey.get(), 1);
        	
    		boolean isEditable = false;
    		if (methodList.isEmpty()) {
    			//instrument not associated with Method setup
    			isEditable = true;
    		}
    		else {
    			//instrument associated with method setup
    			if (master.getInsttype().getInsttypekey().equals(instrumentBykey.get().getInsttype().getInsttypekey())) {
    				//valid to edit instrument
    				isEditable = true;
    			}
    			else {
    				//invalid to edit associated instrument if its instrumenttype changed
    				isEditable = false;
    			}
    		}
    		
    		if (isEditable) {
    			//instrument name should be unique
    			if(instrumentByCode.isPresent())
				{
    				
				
	     		//instrument already available		
	     		if (instrumentByCode.get().getInstmastkey().equals(master.getInstmastkey()))
	     		{   
	     			final InstrumentMaster instrumentToSave = instrumentByCode.get();
	     			
	     			//copy of instrumentToSave object for using 'Diffable' to compare objects
	     			final InstrumentMaster instrumentBeforeSave = new InstrumentMaster(instrumentToSave); 
	
	     			/*
	     			 *  Update other fields with existing instrument code
	     			 *  ok=200
	     			 */
	        		
	            	final InstrumentType instType = typeRepo.findOne(
	            				master.getInsttype().getInsttypekey());
	            	instrumentToSave.setInsttype(instType);
	            	
	            	final InstrumentCategory instCategory = categoryRepo.findOne(
	            			master.getInstcategory().getInstcatkey()); 
	            	instrumentToSave.setInstcategory(instCategory);       	   	
	            	
	            	instrumentToSave.setElectrodeno(master.getElectrodeno());        	
	            	instrumentToSave.setInstiopno(master.getInstiopno());
	            	instrumentToSave.setInstmake(master.getInstmake());
	            	instrumentToSave.setInstmodel(master.getInstmodel());
	            	instrumentToSave.setInstrumentname(master.getInstrumentname());
	            	instrumentToSave.setInstused(master.getInstused());
	     			
	     			final InstrumentMaster savedInstrument = masterRepo.save(instrumentToSave);     
	     			
	     			savedInstrument.setDisplayvalue(savedInstrument.getInstrumentname());
	     			savedInstrument.setScreenname("Instrument");
	     			savedInstrument.setObjsilentaudit(auditdetails.getObjsilentaudit());

	     		     			
	     			if (instrumentBeforeSave.getInsttype().getInsttypekey() != master.getInsttype().getInsttypekey())
	       			{		       		 
	  	       		    //-----start --deleting existing datasource settings based on instrument type
		       			deleteInstTypeSettings(instrumentBeforeSave);
		       			
		       			saveInstTypeSettings(savedInstrument);    	       			
	       			}
	     			
	 	       		return new ResponseEntity<>(savedInstrument, HttpStatus.OK);   	     		
	     		}
	     		else
	     		{ 	
	     		     			
	     			master.setInfo("Duplicate Entry - " + master.getInstrumentcode());
	     			master.setObjsilentaudit(auditdetails.getObjsilentaudit());
	     			return new ResponseEntity<>(master, HttpStatus.CONFLICT); 
	     		}
		} 
    			else
    			{
    				
		    		//copy of object for using 'Diffable' to compare objects
//	    			final InstrumentMaster instrumentBeforeSave = new InstrumentMaster(instrumentBykey.get());
	    			
		    		//Updating fields with a new delimiter name
	    			
		    		final InstrumentMaster savedMethod = masterRepo.save(master);
		    		
		    		savedMethod.setDisplayvalue(savedMethod.getInstrumentcode());
		    		savedMethod.setScreenname("Instrument");
		    		savedMethod.setObjsilentaudit(auditdetails.getObjsilentaudit());

		    		return new ResponseEntity<>(savedMethod , HttpStatus.OK);	
    			}
    		}
     		else {
     			
     			master.setInfo("Associated : "+master.getInstrumentcode());
     			master.setObjsilentaudit(auditdetails.getObjsilentaudit());
     			//return new ResponseEntity<>(master.getInstrumentcode(), HttpStatus.IM_USED);//status code - 226
     			return new ResponseEntity<>(master, HttpStatus.IM_USED);//status code - 226
     		}
     	}
     	else
     	{
     		return new ResponseEntity<>("Instrument not found", HttpStatus.NOT_FOUND);
     	}	
          
    }    
    
    
    
    
    
    /**
     * This method is used to delete the assigned type settings for a specific instrument
     * and called once the type settings of the instrument has been changed
     * before it is being assigned to a user. 
     * @param master  [InstrumentMaster] for which the type settings is to be deleted
     */
    private void deleteInstTypeSettings(final InstrumentMaster master)
    {    	
   		 if (master.getInsttype().getInsttypename().equalsIgnoreCase("File")){
   			fileService.deleteByInstrument(master);
   		 }
   		 else if (master.getInsttype().getInsttypename().equalsIgnoreCase("Rs232")) {
   			rs232Service.deleteByInstrument(master);
   		 }
   		 else {
   			tcpService.deleteByInstrument(master);
   		 }	   		
    }    
	
	
    /**
     * This method is used to retrieve list of active instruments (status=1) for the
     * specified site.
     * @param site [Site] entity for which instruments are to be fetched.
     * @return list of active instruments
     */
    @Transactional
    public ResponseEntity<Object> getInstMaster(final LSSiteMaster site) 
    {        	    	
    	final List<InstrumentMaster>  instobj = masterRepo.findBySite(site, new Sort(Sort.Direction.DESC, "instmastkey"));
    	return new ResponseEntity<>(instobj, HttpStatus.OK);
    	
    }
    
    
    /**
     * This method is used to delete the selected instrument along with its associated insttype. 
     * The instrument has to be validated that it should not be associated with either with user's instrumnet access rights
     * or method setup before deleting.
     * @param instMastKey [int] primary key of InstrumentMaster which is to be deleted.
     * @param saveAuditTrial [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
     * @param comments [String] comments given for audit trail recording
     * @param userKey [int] primary key of logged-in user who done this task
     * @param page [Page] entity relating to 'InstrumentMaster'
     * @param request [HttpServletRequest] Request object to ip address of remote client
     * @param LScfttransaction 
     * @return response entity with deleted entity and status
     */

    
    
  //  @Transactional
    public ResponseEntity<Object> deleteInstMaster(final Integer instMastKey,
    		   final String comments, final Integer userKey, final HttpServletRequest request,InstrumentMaster otherdetails,LSSiteMaster site,InstrumentMaster auditdetails) {
         
    	boolean saveAuditTrial1 = true;
      	//This should be done only if the instrument is not binded in method setup
      	final InstrumentMaster instMaster = masterRepo.findOne(instMastKey);
                   	
      	final List<InstMethod> methodList = instMethodRepo.findByInstmasterAndSiteAndStatus(instMaster,site ,1);
            	
       if (methodList.isEmpty()) {
     	
          	final InstrumentMaster masterObj = instMaster;
         	
          		
//          		final InstrumentMaster instrumentBeforeSave = new InstrumentMaster(masterObj); 
  	        	//Deleting existing 'Instrumenttype' settings record 
  	        	deleteInstTypeSettings(masterObj);
  	        	
  	        	//---start -to delete this  instrument associated for 'Administrator' in 'InstrumentRights' by changing status to '-1'. 
//  	        	final LSSiteMaster sitedata = masterObj.getSite();    	
  	        	 //Administrator id has to be used  	
  	        	final LSuserMaster user =  userRepo.findOne(1);
  	        			
  	        	final LSSiteMaster userSite =  user.getLssitemaster();
  	        				        	
  	        	final Optional<InstrumentRights> rightsList = rightsRepo.findByMasterAndUsersite(masterObj, userSite);
  	        	
  		        if (rightsList.isPresent()) {
  		        	final InstrumentRights rights =  rightsList.get();        	
  		        	rights.setStatus(-1);        	  	
  		        	rightsRepo.save(rights);
  	        	}
  	        	//---end          
  	            
  	       	    masterObj.setStatus(-1);	 
  	       	    masterObj.setInststatus("D");
	            final InstrumentMaster savedInstrument = masterRepo.save(masterObj);    
	        
	            savedInstrument.setDisplayvalue(savedInstrument.getInstrumentname());
	            savedInstrument.setScreenname("Instrument");
	            savedInstrument.setObjsilentaudit(auditdetails.getObjsilentaudit());
	            
  	            	
  	            return new ResponseEntity<>(savedInstrument, HttpStatus.OK);//status code - 200   
  	                	}
       
      	
      	 else {
 			   //Associated with Method master
 			   if (saveAuditTrial1)
 			   {
// 		  
 				  instMaster.setInfo("Instrument Associated : "+instMaster.getInstrumentcode()+" cannot be deleted");
 				  instMaster.setObjsilentaudit(auditdetails.getObjsilentaudit());
 			   }
 			 //  return new ResponseEntity<>(instMaster.getInstrumentname() , HttpStatus.IM_USED);//status code - 226		
 			  return new ResponseEntity<>(instMaster , HttpStatus.IM_USED);//status code - 226	
 		   }
      	} 

    
    
    /**
     * This method is used to fetch instrument list based on site and instrument category.
     * @param category [InstCategory] category object for which list is to be fetched
     * @param site [Site] site object for which list is to be fetched
     * @return list of instruments in the specified category and site
     */
    public ResponseEntity<Object> getInstListByCategoryAndSite(final InstrumentCategory category, final LSSiteMaster site){
   		return new ResponseEntity<>(masterRepo.findBySiteAndInstcategoryAndStatus(site, category, 1),
   				HttpStatus.OK);
   	}

    /**
   	 * This method is used to convert the InstrumentMaster entity to xml with the difference in object
   	 * before and after update for recording in Audit Trial
   	 * @param instMasterBeforeSave [InstrumentMaster] Object before update
   	 * @param savedInstMaster [InstrumentMaster] object after update
   	 * @return string formatted xml data
   	 */
   	public String convertInstrumentMasterToXML(final InstrumentMaster instMasterBeforeSave,
   			final InstrumentMaster savedInstMaster) {
      				
   		final Map<Integer, Map<String, Object>> instMasterModified = new HashMap<Integer, Map<String, Object>>();
   		final Map<String, Object> diffObject = new HashMap<String, Object>();    			
   		
//   		final DiffResult diffResult = instMasterBeforeSave.diff(savedInstMaster);        			
   		
//   		for(Diff<?> d: diffResult.getDiffs()) {
//   			diffObject.put(d.getFieldName(), d.getKey()+" -> "+d.getValue());
//   		}
   		
   		instMasterModified.put(savedInstMaster.getInstmastkey(), diffObject);
   		
		final Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("site", "sitename");		
		fieldMap.put("createdby", "loginid");
		fieldMap.put("insttype", "insttypename");
		fieldMap.put("instcategory", "instcatname");
		
//   		return readWriteXML.saveXML(new InstrumentMaster(savedInstMaster), InstrumentMaster.class, 
//   				instMasterModified, "individualpojo", fieldMap);
		
		return "";

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
		createdUser.setUsercode(createdBy.getUsercode());
   		
   		return createdUser;
   	}

    public ResponseEntity<Object> getInstListBySite(final LSSiteMaster site){
   		return new ResponseEntity<>(masterRepo.findByStatusAndSite(1,site),
   				HttpStatus.OK);
   	}
   	
    public ResponseEntity<Object> getAllEquipment(LSSiteMaster site) throws ParseException {
		
    		List<Equipment> lstEquipment = equipmentRepository
    				.findByNsitecodeAndCmmsettingTrueOrderByNequipmentcodeDesc(site.getSitecode());
  
    		return new ResponseEntity<>(lstEquipment, HttpStatus.OK);
    }

	public ResponseEntity<Object> getEquipmentCatBasedOnSite(EquipmentCategory eqipmentcategory, final LSSiteMaster site) {

		List<Equipment> lstEquipments = new ArrayList<Equipment>();
		
		lstEquipments = equipmentRepository.findByCmmsettingTrueAndEquipmentcategoryAndNsitecodeAndNstatus(eqipmentcategory,site.getSitecode(), 1);
				
		return new ResponseEntity<>(lstEquipments, HttpStatus.OK);
	}
}
