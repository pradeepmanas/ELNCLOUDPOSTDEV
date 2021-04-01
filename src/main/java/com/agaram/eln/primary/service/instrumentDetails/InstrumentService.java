package com.agaram.eln.primary.service.instrumentDetails;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.model.cfr.LSactivity;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cloudFileManip.CloudOrderAttachment;
import com.agaram.eln.primary.model.cloudFileManip.CloudOrderCreation;
import com.agaram.eln.primary.model.cloudFileManip.CloudOrderVersion;
import com.agaram.eln.primary.model.cloudFileManip.CloudSheetCreation;
import com.agaram.eln.primary.model.fileManipulation.OrderAttachment;
import com.agaram.eln.primary.model.general.OrderCreation;
import com.agaram.eln.primary.model.general.OrderVersion;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.general.SearchCriteria;
import com.agaram.eln.primary.model.general.SheetCreation;
import com.agaram.eln.primary.model.instrumentDetails.LSfields;
import com.agaram.eln.primary.model.instrumentDetails.LSinstruments;
import com.agaram.eln.primary.model.instrumentDetails.LSlimsorder;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.instrumentDetails.LSresultdetails;
import com.agaram.eln.primary.model.instrumentDetails.LsMethodFields;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.instrumentDetails.Lsordersharedby;
import com.agaram.eln.primary.model.instrumentDetails.Lsordershareto;
import com.agaram.eln.primary.model.sheetManipulation.LSfilemethod;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefileversion;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflowgroupmapping;
import com.agaram.eln.primary.model.templates.LsMappedTemplate;
import com.agaram.eln.primary.model.templates.LsUnmappedTemplate;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSnotification;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserActions;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;
import com.agaram.eln.primary.repository.cfr.LSactivityRepository;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudOrderCreationRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudOrderVersionRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudSheetCreationRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSfieldsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSinstrumentsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlimsorderRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSresultdetailsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsMethodFieldsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsOrderattachmentsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LselninstrumentmasterRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsordersharedbyRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsordersharetoRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsorderworkflowhistoryRepositroy;
import com.agaram.eln.primary.repository.notification.EmailRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfilemethodRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSparsedparametersRespository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsamplefileRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsamplefileversionRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsampleresultRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LStestparameterRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowgroupmappingRepository;
import com.agaram.eln.primary.repository.templates.LsMappedTemplateRepository;
import com.agaram.eln.primary.repository.templates.LsUnmappedTemplateRepository;
import com.agaram.eln.primary.repository.usermanagement.LSnotificationRepository;
import com.agaram.eln.primary.repository.usermanagement.LSprojectmasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusersteamRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.fileManipulation.FileManipulationservice;
import com.mongodb.gridfs.GridFSDBFile;

@Service
@EnableJpaRepositories(basePackageClasses = LsMethodFieldsRepository.class)
public class InstrumentService {
	
	static final Logger logger = Logger.getLogger(InstrumentService.class.getName());
	
	@Autowired
    private LsMethodFieldsRepository lsMethodFieldsRepository;
	@Autowired
    private LSinstrumentsRepository lSinstrumentsRepository;
	@Autowired
    private LSfieldsRepository lSfieldsRepository;
	@Autowired
    private LSfilemethodRepository LSfilemethodRepository;
	@Autowired
    private LSlogilablimsorderdetailRepository lslogilablimsorderdetailRepository;
	@Autowired
	private LSworkflowRepository lsworkflowRepository;
	@Autowired
	private LSlimsorderRepository lslimsorderRepository;
	@Autowired
	private LSsamplefileRepository lssamplefileRepository;
	@Autowired
	private LSresultdetailsRepository lsresultdetailsRepository;
	@Autowired
	private LSactivityRepository lsactivityRepository;
	@Autowired
	private LScfttransactionRepository lscfttransactionRepository;
	
	@Autowired
	private LSsampleresultRepository lssampleresultRepository;
	
	@Autowired
	private LSparsedparametersRespository lsparsedparametersRespository;
	
	@Autowired
	private LSuserteammappingRepository lsuserteammappingRepository;
	
	@Autowired
	private LSusersteamRepository lsusersteamRepository;
	
	@Autowired
	private LSprojectmasterRepository lsprojectmasterRepository;
	
	@Autowired
	private LSworkflowgroupmappingRepository lsworkflowgroupmappingRepository;
	
	@Autowired
	private LSsamplefileversionRepository lssamplefileversionRepository;
	
	@Autowired
	private LselninstrumentmasterRepository lselninstrumentmasterRepository;
	
	@Autowired
	private LsorderworkflowhistoryRepositroy lsorderworkflowhistoryRepositroy;
	
	@Autowired
	private LSlimsorderRepository lSlimsorderRepository;
	
	@Autowired
	private LStestparameterRepository lStestparameterRepository;
	
	@Autowired
	private LSuserMasterRepository lsuserMasterRepository;
	
	@Autowired
	private LsOrderattachmentsRepository lsOrderattachmentsRepository;
	
	@Autowired
	private LSnotificationRepository lsnotificationRepository;
	
	@Autowired
	private LsMappedTemplateRepository LsMappedTemplateRepository;
	
	@Autowired
	private LsUnmappedTemplateRepository LsUnmappedTemplateRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private FileManipulationservice fileManipulationservice;
	
	@Autowired
	private CloudSheetCreationRepository cloudSheetCreationRepository;
	
	@Autowired
	private CloudOrderCreationRepository cloudOrderCreationRepository;
	
	@Autowired
	private CloudOrderVersionRepository cloudOrderVersionRepository;
	
	@Autowired
	private CloudFileManipulationservice cloudFileManipulationservice;
	
	@Autowired
	private LsordersharetoRepository lsordersharetoRepository;
	
	@Autowired
	private LsordersharedbyRepository lsordersharedbyRepository;
	
	
	public Map<String, Object> getInstrumentparameters(LSSiteMaster lssiteMaster)
	{
		Map<String, Object> obj = new HashMap<>();
		List<String> lsInst = new ArrayList<String>();
		lsInst.add("INST000");
		lsInst.add("LPRO");
		List<LsMethodFields> Methods = lsMethodFieldsRepository.findByinstrumentidNotIn(lsInst);
		
		if(lssiteMaster.getIsmultitenant()!=1) {
			List<LSfields> Generalfields = lSfieldsRepository.findByisactive(1);
			 List<LSinstruments> Instruments = lSinstrumentsRepository.findAll();
	         List<LsMappedTemplate> MappedTemplate = LsMappedTemplateRepository.findAll();
			List<LsUnmappedTemplate> UnmappedTemplate = LsUnmappedTemplateRepository.findAll();	
			obj.put("Generalfields", Generalfields);
			obj.put("Instruments", Instruments);
			obj.put("elninstrument",lselninstrumentmasterRepository.findBylssitemasterAndStatusOrderByInstrumentcodeDesc(lssiteMaster,1));
			obj.put("Mappedtemplates", MappedTemplate);
			obj.put("Unmappedtemplates", UnmappedTemplate);
		}
		else {
			List<LSfields> Generalfields = lSfieldsRepository.findBymethodname("ID_GENERAL");
			obj.put("Generalfields", Generalfields);
		}
		
		obj.put("Methods", Methods);

		
		return obj;
	}
	
	public LSlogilablimsorderdetail InsertELNOrder(LSlogilablimsorderdetail objorder) {
		
		objorder.setLsworkflow(lsworkflowRepository.findTopByAndLssitemasterOrderByWorkflowcodeAsc(objorder.getLsuserMaster().getLssitemaster()));
		objorder.setCreatedtimestamp(new Date());
		objorder.setOrderflag("N");
		logger.info("InsertELNOrder : "+objorder);
		String Content = "";
		logger.info("InsertELNOrder getLssamplefile(): "+objorder.getLssamplefile());
		if(objorder.getLssamplefile() == null)
		{
			LSsamplefile objsamplefile = new LSsamplefile();
			if(objorder.getLsfile() != null)
			{
				logger.info("InsertELNOrder getLsfile() : "+objorder.getLsfile());
				if(objorder.getIsmultitenant() == 1)
				{
					CloudSheetCreation file = cloudSheetCreationRepository.findById((long)objorder.getLsfile().getFilecode());
					if(file != null)
					{
						Content = file.getContent();
					}
					else
					{
						Content = objorder.getLsfile().getFilecontent();
					}
				}
				else
				{
					SheetCreation file = mongoTemplate.findById(objorder.getLsfile().getFilecode(), SheetCreation.class);
					if(file != null)
					{
						Content = file.getContent();
					}
					else
					{
						Content = objorder.getLsfile().getFilecontent();
					}
				}
				
			}
			logger.info("InsertELNOrder objsamplefile : "+objsamplefile);
			objsamplefile.setCreatedate(new Date());
			logger.info("InsertELNOrder objsamplefile : "+objorder.getTestcode());
			logger.info("InsertELNOrder objsamplefile : "+objorder.getBatchcode());
			objsamplefile.setTestid(objorder.getTestcode());
			objsamplefile.setBatchcode(objorder.getBatchcode());
			objsamplefile.setProcessed(0);
			logger.info("InsertELNOrder objsamplefile : "+objsamplefile);
			objorder.setLssamplefile(objsamplefile);
		}
		else
		{
			logger.info("InsertELNOrder  objorder.getLssamplefile().getTempfilecontent() : "+ objorder.getLssamplefile().getTempfilecontent());
			Content = objorder.getLssamplefile().getTempfilecontent();
		}
		
		objorder.getLssamplefile().setFilecontent(null);
	
		lssamplefileRepository.save(objorder.getLssamplefile());
		
		//kumaresan
		if(objorder.getAssignedto() != null)
		{
			objorder.setLockeduser( objorder.getAssignedto().getUsercode());
		}
		
		lslogilablimsorderdetailRepository.save(objorder);
		String Batchid = "ELN"+objorder.getBatchcode();
		logger.info("InsertELNOrder Batchid : "+Batchid);
		lssamplefileRepository.setbatchcodeOnsamplefile(objorder.getBatchcode(), objorder.getLssamplefile().getFilesamplecode());
		if(objorder.getFiletype() == 3)
		{
			Batchid = "RESEARCH"+objorder.getBatchcode();
			logger.info("InsertELNOrder Batchid : "+Batchid);
		}
		else if(objorder.getFiletype() == 4)
		{
			Batchid = "EXCEL"+objorder.getBatchcode();
			logger.info("InsertELNOrder Batchid : "+Batchid);
		}
		else if(objorder.getFiletype() == 5)
		{
			Batchid = "VALIDATE" +objorder.getBatchcode();
			logger.info("InsertELNOrder Batchid : "+Batchid);
		}
		lslogilablimsorderdetailRepository.setbatchidBybatchcode(Batchid,objorder.getBatchcode());
		objorder.setBatchid(Batchid);
		logger.info("InsertELNOrder objorder : "+objorder);
		
		List<LSlimsorder> lsorder = new ArrayList<LSlimsorder>();
		String Limsorder = objorder.getBatchcode().toString();
		logger.info("InsertELNOrder Limsorder : "+Limsorder);
		
		if(objorder.getLsfile() != null && objorder.getLsfile().getLsmethods() != null && objorder.getLsfile().getLsmethods().size() > 0)
		{
			int methodindex = 0;
			for(LSfilemethod objmethod: objorder.getLsfile().getLsmethods())
			{
				logger.info("InsertELNOrder objmethod : "+objmethod);
				LSlimsorder objLimsOrder = new LSlimsorder();
				String order="";
				if(methodindex<10)
				{
					order = Limsorder.concat("0"+methodindex);
					logger.info("InsertELNOrder order : "+methodindex);
				}
				else
				{
					order = Limsorder.concat(""+methodindex);
				}
				objLimsOrder.setOrderid(Long.parseLong(order));
				objLimsOrder.setBatchid(Batchid);
				objLimsOrder.setMethodcode(objmethod.getMethodid());
				objLimsOrder.setInstrumentcode(objmethod.getInstrumentid());
				objLimsOrder.setTestcode(objorder.getTestcode() != null ?objorder.getTestcode().toString():null);
				objLimsOrder.setCreatedtimestamp(new Date());
				objLimsOrder.setOrderflag("N");
				
				lsorder.add(objLimsOrder);
				logger.info("InsertELNOrder objLimsOrder : "+objLimsOrder);
				methodindex++;
			}
			
			lslimsorderRepository.save(lsorder);
		}
		else
		{
			LSfilemethod lsfilemethod = LSfilemethodRepository.findByFilecode(objorder.getLsfile().getFilecode());
			LSlimsorder objLimsOrder = new LSlimsorder();
			if(lsfilemethod != null) {
				objLimsOrder.setMethodcode(lsfilemethod.getMethodid());
				objLimsOrder.setInstrumentcode(lsfilemethod.getInstrumentid());
			}
			objLimsOrder.setOrderid(Long.parseLong(Limsorder.concat("00")));
			objLimsOrder.setBatchid(Batchid);
			objLimsOrder.setTestcode(objorder.getTestcode() != null ?objorder.getTestcode().toString():null);
			objLimsOrder.setCreatedtimestamp(new Date());
			objLimsOrder.setOrderflag("N");
			
			lslimsorderRepository.save(objLimsOrder);
		}
		
		if(objorder.getObjuser()!=null) {
			objorder.getObjmanualaudit().setComments(objorder.getObjuser().getComments());
			objorder.getObjmanualaudit().setTableName("LSlogilablimsorderdetail");
    		lscfttransactionRepository.save(objorder.getObjmanualaudit());
		}
		
		if(objorder.getObjsilentaudit() != null)
    	{
			objorder.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
    		lscfttransactionRepository.save(objorder.getObjsilentaudit());
    	}
		
		if(objorder.getLssamplefile()!= null)
		{
			updateordercontent(Content, objorder.getLssamplefile(), objorder.getIsmultitenant());
		}
		
		updatenotificationfororder(objorder);

		logger.info("InsertELNOrder order : "+objorder);
		
		return objorder;
	}
	
	public void updatenotificationfororder(LSlogilablimsorderdetail objorder)
	{
		try
		{
			if(objorder != null && objorder.getLsprojectmaster() != null
					&& objorder.getLsprojectmaster().getLsusersteam() != null)
			{
				LSusersteam objteam = lsusersteamRepository.findByteamcode(objorder.getLsprojectmaster().getLsusersteam().getTeamcode());
				if(objteam.getLsuserteammapping() != null && objteam.getLsuserteammapping().size()>0)
				{
					String Details ="";
					String Notifiction="";
					
					if(objorder.getAssignedto() == null)
					{
						if(objorder.getOrderflag().equalsIgnoreCase("R")) {
							Notifiction="ORDERCOMPLETED";
							
							Details ="{\"ordercode\":\""+objorder.getBatchcode()+"\", \"order\":\""+objorder.getBatchid()
							+"\", \"previousworkflow\":\""+""
							+"\", \"previousworkflowcode\":\""+ -1
							+"\", \"currentworkflow\":\""+objorder.getLsworkflow().getWorkflowname()
							+"\", \"completeduser\":\""+objorder.getObjLoggeduser().getUsername()
							+"\", \"currentworkflowcode\":\""+objorder.getLsworkflow().getWorkflowcode()+"\"}";
						}
						else {
							Notifiction="ORDERCREATION";
							
							Details ="{\"ordercode\":\""+objorder.getBatchcode()+"\", \"order\":\""+objorder.getBatchid()
							+"\", \"previousworkflow\":\""+""
							+"\", \"previousworkflowcode\":\""+ -1
							+"\", \"currentworkflow\":\""+objorder.getLsworkflow().getWorkflowname()
							+"\", \"currentworkflowcode\":\""+objorder.getLsworkflow().getWorkflowcode()+"\"}";	
						}
					}
					else
					{
						Notifiction="ORDERCREATIONANDASSIGN";
						
						Details ="{\"ordercode\":\""+objorder.getBatchcode()+"\", \"order\":\""+objorder.getBatchid()
						+"\", \"previousworkflow\":\""+""
						+"\", \"previousworkflowcode\":\""+ -1
						+"\", \"currentworkflow\":\""+objorder.getLsworkflow().getWorkflowname()
						+"\", \"assignedto\":\""+objorder.getAssignedto().getUsername()
						+"\", \"assignedby\":\""+objorder.getObjLoggeduser().getUsername()
						+"\", \"currentworkflowcode\":\""+objorder.getLsworkflow().getWorkflowcode()+"\"}";
					}
					
					/*
					 * Details
					 * ="{\"ordercode\":\""+objorder.getBatchcode()+"\", \"order\":\""+objorder.
					 * getBatchid() +"\", \"previousworkflow\":\""+""
					 * +"\", \"previousworkflowcode\":\""+ -1
					 * +"\", \"currentworkflow\":\""+objorder.getLsworkflow().getWorkflowname()
					 * +"\", \"currentworkflowcode\":\""+objorder.getLsworkflow().getWorkflowcode()+
					 * "\"}";
					 */
					
					logger.info("Notification Type : "+Notifiction);
					logger.info("Notification Details : "+Details);
					
					if(objorder.getAssignedto() == null)
					{
						List<LSuserteammapping> lstusers = objteam.getLsuserteammapping();
						List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
						for(int i=0; i<lstusers.size(); i++)
						{
							if(objorder.getLsuserMaster().getUsercode() != lstusers.get(i).getLsuserMaster().getUsercode())
							{
								LSnotification objnotify = new LSnotification();
								objnotify.setNotifationfrom(objorder.getLsuserMaster());
								objnotify.setNotifationto(lstusers.get(i).getLsuserMaster());
								objnotify.setNotificationdate(objorder.getCreatedtimestamp());
								objnotify.setNotification(Notifiction);
								objnotify.setNotificationdetils(Details);
								objnotify.setIsnewnotification(1);
								objnotify.setNotificationpath("/registertask");
								
								lstnotifications.add(objnotify);
							}
						}
						
						
						lsnotificationRepository.save(lstnotifications);
					}
					else
					{
						LSnotification objnotify = new LSnotification();
						objnotify.setNotifationfrom(objorder.getLsuserMaster());
						objnotify.setNotifationto(objorder.getAssignedto());
						objnotify.setNotificationdate(objorder.getCreatedtimestamp());
						objnotify.setNotification(Notifiction);
						objnotify.setNotificationdetils(Details);
						objnotify.setIsnewnotification(1);
						objnotify.setNotificationpath("/registertask");
						lsnotificationRepository.save(objnotify);
					}
				}
			}
		}
		catch(Exception e) 
		{
			logger.error("updatenotificationfororder : "+e.getMessage());
		}
	}
	
	public void updatenotificationfororderworkflow(LSlogilablimsorderdetail objorder, LSworkflow previousworkflow)
	{
		try
		{
			String Details ="";
			String Notifiction="";
			if(objorder.getApprovelstatus() != null ) {
				LSusersteam objteam = lsusersteamRepository.findByteamcode(objorder.getLsprojectmaster().getLsusersteam().getTeamcode());
			
				
				if(objorder.getApprovelstatus() == 1) {
					Notifiction="ORDERAPPROVED";
				}
				else if(objorder.getApprovelstatus() == 2) {
					Notifiction="ORDERRETERNED";
				}
				else if(objorder.getApprovelstatus() == 3) {
					Notifiction="ORDERREJECTED";
				}
				
				int perviousworkflowcode = previousworkflow != null ? previousworkflow.getWorkflowcode() : -1;
				String previousworkflowname = previousworkflow != null ? previousworkflow.getWorkflowname():"";
				
				 if(previousworkflowname.equals(objorder.getLsworkflow().getWorkflowname())) {
						Notifiction="ORDERFINALAPPROVAL";
					}
				 
				Details ="{\"ordercode\":\""+objorder.getBatchcode()
				+"\", \"order\":\""+objorder.getBatchid()
				+"\", \"previousworkflow\":\""+ previousworkflowname
				+"\", \"previousworkflowcode\":\""+perviousworkflowcode
				+"\", \"currentworkflow\":\""+objorder.getLsworkflow().getWorkflowname()
				+"\", \"currentworkflowcode\":\""+objorder.getLsworkflow().getWorkflowcode()+"\"}";
				
				List<LSuserteammapping> lstusers = objteam.getLsuserteammapping();
				List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
				for(int i=0; i<lstusers.size(); i++)
				{
					if(objorder.getObjLoggeduser().getUsercode() != lstusers.get(i).getLsuserMaster().getUsercode())
					{
						LSnotification objnotify = new LSnotification();
						objnotify.setNotifationfrom(objorder.getLsuserMaster());
						objnotify.setNotifationto(lstusers.get(i).getLsuserMaster());
						objnotify.setNotificationdate(objorder.getCreatedtimestamp());
						objnotify.setNotification(Notifiction);
						objnotify.setNotificationdetils(Details);
						objnotify.setIsnewnotification(1);
						objnotify.setNotificationpath("/registertask");
						
						lstnotifications.add(objnotify);
					}
				}
				
				lsnotificationRepository.save(lstnotifications);
			}
		}
		catch(Exception e) 
		{
			logger.error("updatenotificationfororderworkflow : "+e.getMessage());
		}
	}
	
	public void updatenotificationfororder(LSlogilablimsorderdetail objorder,String currentworkflow)
	{
		try
		{
			if(objorder != null && objorder.getLsprojectmaster() != null
					&& objorder.getLsprojectmaster().getLsusersteam() != null)
			{
				LSusersteam objteam = lsusersteamRepository.findByteamcode(objorder.getLsprojectmaster().getLsusersteam().getTeamcode());
				if(objteam.getLsuserteammapping() != null && objteam.getLsuserteammapping().size()>0)
				{
					String Details ="";
					String Notifiction="";
					
					if(objorder.getApprovelstatus() != null) {
						
						if(objorder.getApprovelstatus() == 1) {
							Notifiction="ORDERAPPROVED";
							Details ="{\"ordercode\":\""+objorder.getBatchcode()
							+"\", \"order\":\""+objorder.getBatchid()
							+"\", \"currentworkflow\":\""+currentworkflow
							+"\", \"movedworkflow\":\""+objorder.getLsworkflow().getWorkflowname()+"\"}";
						}
						else if(objorder.getApprovelstatus() == 3) {
							Notifiction="ORDERREJECTED";
							Details ="{\"ordercode\":\""+objorder.getBatchcode()
							+"\", \"order\":\""+objorder.getBatchid()
							+"\", \"workflowname\":\""+objorder.getLsworkflow().getWorkflowname()+"\"}";
						}
					}
					
					List<LSuserteammapping> lstusers = objteam.getLsuserteammapping();
					List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
					for(int i=0; i<lstusers.size(); i++)
					{
						if(objorder.getLsuserMaster().getUsercode() != lstusers.get(i).getLsuserMaster().getUsercode())
						{
							LSnotification objnotify = new LSnotification();
							objnotify.setNotifationfrom(objorder.getLsuserMaster());
							objnotify.setNotifationto(lstusers.get(i).getLsuserMaster());
							objnotify.setNotificationdate(objorder.getCreatedtimestamp());
							objnotify.setNotification(Notifiction);
							objnotify.setNotificationdetils(Details);
							objnotify.setIsnewnotification(1);
							objnotify.setNotificationpath("/registertask");
							
							lstnotifications.add(objnotify);
						}
					}
					
					lsnotificationRepository.save(lstnotifications);
				}
			}
		}
		catch(Exception e) 
		{
			logger.error("updatenotificationfororder : "+e.getMessage());
		}
	}
	
	public LSactivity InsertActivities(LSactivity objActivity)
	{
		return lsactivityRepository.save(objActivity);
	}
	
	public List<LSlogilablimsorderdetail> Getorderbytype(LSlogilablimsorderdetail objorder)
	{
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		if(objorder.getOrderflag().equals("N"))
		{
		lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
				objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(), objorder.getTodate());
		}
		else
		{
			lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
					objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(), objorder.getTodate());
		}
		
		if(objorder.getObjsilentaudit() != null)
    	{
			objorder.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
    		lscfttransactionRepository.save(objorder.getObjsilentaudit());
    	}
		
		
		return lstorder;
	}
	
	public List<LSlogilablimsorderdetail> Getorderbytypeanduser(LSlogilablimsorderdetail objorder)
	{
		List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objorder.getLsuserMaster());
		List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingIn(lstteammap);
		List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		
		if(objorder.getOrderflag().equals("N"))
		{
			lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
					objorder.getFiletype(), objorder.getOrderflag(),lstproject, objorder.getFromdate(), objorder.getTodate());
		}
		else
		{
			lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
					objorder.getFiletype(), objorder.getOrderflag(),lstproject, objorder.getFromdate(), objorder.getTodate());
		}
		
//		if(objorder.getObjsilentaudit() != null)
//    	{
//			objorder.getObjsilentaudit().setModuleName("Register Task Orders & Execute");
//			objorder.getObjsilentaudit().setComments("Allow to get Orders");
//			objorder.getObjsilentaudit().setActions("View");
//			objorder.getObjsilentaudit().setSystemcoments("System Generated");
//			objorder.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
//    		lscfttransactionRepository.save(objorder.getObjsilentaudit());
//    	}
		
		return lstorder;
	}
	
	public List<LSlogilablimsorderdetail> Getorderbytypeandflag(LSlogilablimsorderdetail objorder, Map<String, Object> mapOrders)
	{
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		List<Long> lstBatchcode = new ArrayList<Long>();
		List<Integer> lstsamplefilecode = new ArrayList<Integer>();
		List<LSsamplefile> idList = new ArrayList<LSsamplefile>();
		if(objorder.getOrderflag().equals("N"))
		{
		
			if(objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearch().trim() != "")
			{
			
				lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeonFiletypeAndFlagandcreateddate(objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(), objorder.getTodate());
				
				if(lstBatchcode != null && lstBatchcode.size() >0)
				{
					lstsamplefilecode = lssamplefileRepository.getFilesamplecodeByBatchcodeIn(lstBatchcode);
				}
				
				if(lstsamplefilecode != null && lstsamplefilecode.size() >0)
				{
					idList = getsamplefileIds(lstsamplefilecode, objorder.getSearchCriteria(), objorder.getIsmultitenant());
					if(idList != null && idList.size() >0)
					{
						lstorder= lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndCreatedtimestampBetweenAndLssamplefileIn(objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(), objorder.getTodate(), idList);
					}
				}
			}
			else
			{
				lstorder= lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(), objorder.getTodate());
			}
			
		}
		else
		{
			if(objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearch().trim() != "")
			{
				lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeonFiletypeAndFlagandCompletedtime(objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(), objorder.getTodate());
				
				if(lstBatchcode != null && lstBatchcode.size() >0)
				{
					lstsamplefilecode = lssamplefileRepository.getFilesamplecodeByBatchcodeIn(lstBatchcode);
				}
				
				if(lstsamplefilecode != null && lstsamplefilecode.size() >0)
				{
					idList = getsamplefileIds(lstsamplefilecode, objorder.getSearchCriteria(), objorder.getIsmultitenant());
					if(idList != null && idList.size() >0)
					{
						lstorder= lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndCompletedtimestampBetweenAndLssamplefileIn(objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(), objorder.getTodate(), idList);
					}
				}
			}
			else
			{
				lstorder= lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(), objorder.getTodate());
			}
		}
		
		if(objorder.getObjsilentaudit() != null)
    	{
			objorder.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
    		lscfttransactionRepository.save(objorder.getObjsilentaudit());
    	}
		
		long pendingcount = 0;
		long completedcount = 0;
		
		if(objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearch().trim() != "")
		{
			if(objorder.getOrderflag().equals("N"))
			{
				if(idList != null)
				{
					pendingcount = idList.size();
				}
				
				completedcount = lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
						objorder.getFiletype(), "R", objorder.getFromdate(), objorder.getTodate());
			}
			else
			{
				if(idList != null)
				{
					completedcount = idList.size();
				}
				
				pendingcount = lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
						objorder.getFiletype(), "N", objorder.getFromdate(), objorder.getTodate());
			}

		}
		else
		{
			pendingcount = lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
				objorder.getFiletype(), "N", objorder.getFromdate(), objorder.getTodate());
		
			completedcount = lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
				objorder.getFiletype(), "R", objorder.getFromdate(), objorder.getTodate());
		}
		
		long sharedbycount = 0;
		long sharetomecount =0;
		
		if(objorder.getObjLoggeduser() != null && objorder.getObjLoggeduser().getUnifieduserid() != null)
		{
			sharedbycount = lsordersharedbyRepository.countBySharebyunifiedidAndOrdertypeAndSharestatusOrderBySharedbycodeDesc(objorder.getObjLoggeduser().getUnifieduserid(), objorder.getFiletype(), 1);
			sharetomecount = lsordersharetoRepository.countBySharetounifiedidAndOrdertypeAndSharestatusOrderBySharetocodeDesc(objorder.getObjLoggeduser().getUnifieduserid(), objorder.getFiletype(), 1);
		}
		
		mapOrders.put("orders", lstorder);
		mapOrders.put("pendingcount", pendingcount);
		mapOrders.put("completedcount", completedcount);
		mapOrders.put("sharedbycount", sharedbycount);
		mapOrders.put("sharetomecount", sharetomecount);
		
		
		return lstorder;
	}
	
	public List<LSsamplefile> getsamplefileIds(List<Integer>  lstsamplefilecode, SearchCriteria searchCriteria, Integer ismultitenant)
	{

		List<Long> idList = new ArrayList<Long>();
		String searchtext =searchCriteria.getContentsearch().replace("[", "\\[").replace("]", "\\]");
		if(ismultitenant == 1)
		{
			Query query = new Query();
			if(searchCriteria.getContentsearchtype() == 1)
			{
				query.addCriteria(Criteria.where("contentvalues").regex(searchtext, "i"));
			}
			else if(searchCriteria.getContentsearchtype() == 2)
			{
				query.addCriteria(Criteria.where("contentparameter").regex(searchtext, "i"));
			}
				
			query.addCriteria(Criteria.where("id").in(lstsamplefilecode))
					  .with(new PageRequest(0, 5));				
					
			List<OrderCreation> orders = mongoTemplate.find(query, OrderCreation.class);
			idList = orders.stream().map(OrderCreation::getId).collect(Collectors.toList());
		}
		else
		{
			List<CloudOrderCreation> orders = new ArrayList<CloudOrderCreation>();
			if(searchCriteria.getContentsearchtype() == 1)
			{
				orders = cloudOrderCreationRepository.findByContentvaluesContainingIgnoreCase(searchtext);
			}
			else if(searchCriteria.getContentsearchtype() == 2)
			{
				orders = cloudOrderCreationRepository.findByContentparameterContainingIgnoreCase(searchtext);
			}
			idList = orders.stream().map(CloudOrderCreation::getId).collect(Collectors.toList());
		}
		
		
		List<LSsamplefile> lstsample = new ArrayList<LSsamplefile>();
		
		if(idList != null && idList.size() >0)
		{
			List<Integer> lssample = new ArrayList<Integer>();
			idList.forEach((n) -> lssample.add(Math.toIntExact(n)));
			
			lstsample = lssamplefileRepository.findByfilesamplecodeIn(lssample);
		}
		
		return lstsample;
	}
	
	public List<LSlogilablimsorderdetail> Getorderbytypeandflaganduser(LSlogilablimsorderdetail objorder, Map<String, Object> mapOrders)
	{
		List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objorder.getLsuserMaster());
		List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingIn(lstteammap);
		List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		List<Long> lstBatchcode = new ArrayList<Long>();
		
		
		long pendingcount = 0;
		long completedcount = 0;
		long Assignedcount = 0;
		long Assignedpendingcount = 0;
		long Assignedcompletedcount = 0;
		long myordercount = 0;
		long myorderpendingcount = 0;
		long myordercompletedcount = 0;
		
		if(lstproject.size() > 0)
		{
			List<Integer> lstprojectcode = lsprojectmasterRepository.findProjectcodeByLsusersteamIn(lstteam);
			
			
			if(objorder.getOrderflag().equals("N"))
			{
				if(objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearch().trim() != "")
				{
				
					lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeonFiletypeAndFlagandProjectandcreateddate(
							objorder.getFiletype(), objorder.getOrderflag(),lstprojectcode, objorder.getFromdate(), objorder.getTodate());
					
					lstorder = getordersonsamplefileid(lstBatchcode, objorder);
			
				}
				else
				{
					lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(),lstproject, objorder.getFromdate(), objorder.getTodate());
				}
			}
			else if(objorder.getOrderflag().equals("A"))
			{
				if(objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearch().trim() != "")
				{
					lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeonFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestamp(
							objorder.getFiletype(),objorder.getLsuserMaster().getUsercode(),objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(), objorder.getTodate());
					
					lstorder = getordersonsamplefileid(lstBatchcode, objorder);
					Assignedcount = lstorder.size();
					
					Assignedpendingcount = lstorder.stream().filter(obj -> "N".equals(obj.getOrderflag()!=null?obj.getOrderflag().trim():"")).count();
					Assignedcompletedcount = lstorder.stream().filter(obj -> "R".equals(obj.getOrderflag()!=null?obj.getOrderflag().trim():"")).count();
					
					if(objorder.getLsuserMaster() != null && objorder.getLsuserMaster().getLsuserActions() != null )
					{
						LSuserActions objaction = objorder.getLsuserMaster().getLsuserActions();
						if(objaction.getAssignedordershowpending() != null && objaction.getAssignedordershowall() !=1 && objaction.getAssignedordershowpending() ==1)
						{
							lstorder =  lstorder.stream().filter(obj -> "N".equals(obj.getOrderflag()!=null?obj.getOrderflag().trim():"")).collect(Collectors.toList());
						}
						else if(objaction.getAssignedordershowcompleted() != null && objaction.getAssignedordershowall() !=1 && objaction.getAssignedordershowcompleted() ==1)
						{
							lstorder =  lstorder.stream().filter(obj -> "R".equals(obj.getOrderflag()!=null?obj.getOrderflag().trim():"")).collect(Collectors.toList());
						}
					}
					
				}
				else
				{
					if(objorder.getLsuserMaster() != null && objorder.getLsuserMaster().getLsuserActions() != null )
					{
						LSuserActions objaction = objorder.getLsuserMaster().getLsuserActions();
						if(objaction.getAssignedordershowall() != null && objaction.getAssignedordershowall() ==1)
						{
							lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
								objorder.getFiletype(),objorder.getLsuserMaster(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
						}
						else if(objaction.getAssignedordershowpending() != null && objaction.getAssignedordershowpending() ==1)
						{
							lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
									objorder.getFiletype(),"N",objorder.getLsuserMaster(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
						}
						else if(objaction.getAssignedordershowcompleted() != null && objaction.getAssignedordershowcompleted() ==1)
						{
							lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
									objorder.getFiletype(),"R",objorder.getLsuserMaster(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
						}
					}
					else
					{
						lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
								objorder.getFiletype(),objorder.getLsuserMaster(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
					}
			
				}
			}
			else if(objorder.getOrderflag().equals("M"))
			{
				if(objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearch().trim() != "")
				{
					lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeonFiletypeAndFlagandAssignedtoAndCreatedtimestamp(
							objorder.getFiletype(),objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(), objorder.getTodate());
					
					lstorder = getordersonsamplefileid(lstBatchcode, objorder);
				}
				
				//kumaresan
				else
				{
					if(objorder.getLsuserMaster() != null && objorder.getLsuserMaster().getLsuserActions() != null) 
					{
						LSuserActions objaction = objorder.getLsuserMaster().getLsuserActions();
						if(objaction.getMyordershowall() != null && objaction.getMyordershowall() ==1)
						{
							lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									objorder.getFiletype(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
						}
						else if(objaction.getMyordershowpending() != null && objaction.getMyordershowpending() ==1)
						{
							lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									objorder.getFiletype(),"N",objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
						}
						else if(objaction.getMyordershowcompleted() != null && objaction.getMyordershowcompleted() ==1)
						{
							lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									objorder.getFiletype(),"R",objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
						}	
					}
					else
					{
						lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							objorder.getFiletype(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
					}
				}
				
				
				
			}
			else
			{
	
				if(objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearch().trim() != "")
				{
					lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeonFiletypeAndFlagandProjectandCompletedtime(
							objorder.getFiletype(), objorder.getOrderflag(),lstprojectcode, objorder.getFromdate(), objorder.getTodate());
					
					lstorder = getordersonsamplefileid(lstBatchcode, objorder);
				}
				else
				{
					lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(),lstproject, objorder.getFromdate(), objorder.getTodate());
				}
			}
			
			if(objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearch().trim() != "")
			{

				if(objorder.getOrderflag().equals("N"))
				{
					pendingcount = lstorder.size();
					
					completedcount = lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), "R",lstproject, objorder.getFromdate(), objorder.getTodate());
					
					Assignedcount = lslogilablimsorderdetailRepository.countByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
							objorder.getFiletype(),objorder.getLsuserMaster(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
					myordercount = lslogilablimsorderdetailRepository.countByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							objorder.getFiletype(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
					
				}
				else if(objorder.getOrderflag().equals("A"))
				{
					pendingcount = lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), "N",lstproject, objorder.getFromdate(), objorder.getTodate());
					completedcount = lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), "R",lstproject, objorder.getFromdate(), objorder.getTodate());
					
//					Assignedcount = lstorder.size();
					myordercount = lslogilablimsorderdetailRepository.countByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							objorder.getFiletype(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
				}
				else if(objorder.getOrderflag().equals("M"))
				{
					pendingcount = lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), "N",lstproject, objorder.getFromdate(), objorder.getTodate());
					completedcount = lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), "R",lstproject, objorder.getFromdate(), objorder.getTodate());
					
					Assignedcount = lslogilablimsorderdetailRepository.countByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
							objorder.getFiletype(),objorder.getLsuserMaster(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
					
					myordercount = lstorder.size();
				}
				else
				{
					completedcount = lstorder.size();
					
					pendingcount = lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), "N",lstproject, objorder.getFromdate(), objorder.getTodate());
					Assignedcount = lslogilablimsorderdetailRepository.countByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
							objorder.getFiletype(),objorder.getLsuserMaster(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
					
					myordercount = lslogilablimsorderdetailRepository.countByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							objorder.getFiletype(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
				}
			}
			else
			{
				pendingcount = lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
						objorder.getFiletype(), "N",lstproject, objorder.getFromdate(), objorder.getTodate());
				
				completedcount = lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
						objorder.getFiletype(), "R",lstproject, objorder.getFromdate(), objorder.getTodate());
				
				Assignedcount = lslogilablimsorderdetailRepository.countByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
						objorder.getFiletype(),objorder.getLsuserMaster(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
				
				if(objorder.getOrderflag().equals("A"))
				{
					Assignedpendingcount = lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
						objorder.getFiletype(), "N",objorder.getLsuserMaster(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
				
					Assignedcompletedcount = lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
						objorder.getFiletype(), "R",objorder.getLsuserMaster(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
				}
				else if(objorder.getOrderflag().equals("M"))
				{
					//kumaresan
					myorderpendingcount =  lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							objorder.getFiletype(),"N",objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
					
					myordercompletedcount =  lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							objorder.getFiletype(),"R",objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
				}
				myordercount = lslogilablimsorderdetailRepository.countByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
						objorder.getFiletype(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
			}
			
		}
		if(objorder.getObjsilentaudit() != null)
    	{
			objorder.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
    		lscfttransactionRepository.save(objorder.getObjsilentaudit());
    	}
		
		long sharedbycount = 0;
		long sharetomecount =0;
		
		if(objorder.getLsuserMaster() != null && objorder.getLsuserMaster().getUnifieduserid() != null)
		{
			sharedbycount = lsordersharedbyRepository.countBySharebyunifiedidAndOrdertypeAndSharestatusOrderBySharedbycodeDesc(objorder.getLsuserMaster().getUnifieduserid(), objorder.getFiletype(), 1);
			sharetomecount = lsordersharetoRepository.countBySharetounifiedidAndOrdertypeAndSharestatusOrderBySharetocodeDesc(objorder.getLsuserMaster().getUnifieduserid(), objorder.getFiletype(), 1);
		}
		
		
		mapOrders.put("orders", lstorder);
		mapOrders.put("pendingcount", pendingcount);
		mapOrders.put("completedcount", completedcount);
		mapOrders.put("Assignedcount",Assignedcount);
		mapOrders.put("myordercount",myordercount);
			
		mapOrders.put("Assignedpendingcount",Assignedpendingcount);
		mapOrders.put("Assignedcompletedcount",Assignedcompletedcount);
		mapOrders.put("myorderpendingcount",myorderpendingcount);
		mapOrders.put("myordercompletedcount",myordercompletedcount);
			
		mapOrders.put("sharedbycount", sharedbycount);
		mapOrders.put("sharetomecount", sharetomecount);
			
		return lstorder;
	}
	
	public List<LSlogilablimsorderdetail> getordersonsamplefileid(List<Long> lstBatchcode, LSlogilablimsorderdetail objorder)
	{
		List<LSsamplefile> idList = new ArrayList<LSsamplefile>();
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		List<Integer> lstsamplefilecode = new ArrayList<Integer>();
		
		if(lstBatchcode != null && lstBatchcode.size() >0)
		{
			lstsamplefilecode = lssamplefileRepository.getFilesamplecodeByBatchcodeIn(lstBatchcode);
		}
		
		if(lstsamplefilecode != null && lstsamplefilecode.size() >0)
		{
			idList = getsamplefileIds(lstsamplefilecode, objorder.getSearchCriteria(),objorder.getIsmultitenant());
			if(idList != null && idList.size() >0)
			{
				lstorder= lslogilablimsorderdetailRepository.findByFiletypeAndLssamplefileInOrderByBatchcodeDesc(
						objorder.getFiletype(), idList);
			}
		}
		
		return lstorder;
	}
	
	public List<LSlogilablimsorderdetail> Getorderbytypeandflaglazy(LSlogilablimsorderdetail objorder, Map<String, Object> mapOrders)
	{
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		if(objorder.getOrderflag().equals("N"))
		{
			lstorder = lslogilablimsorderdetailRepository.findFirst10ByFiletypeAndOrderflagAndBatchcodeLessThanAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
				objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(), objorder.getFromdate(), objorder.getTodate());
		}
		else
		{
			lstorder = lslogilablimsorderdetailRepository.findFirst10ByFiletypeAndOrderflagAndBatchcodeLessThanAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
					objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(), objorder.getFromdate(), objorder.getTodate());
		}
		
		mapOrders.put("orders", lstorder);
		
		return lstorder;
	}
	
	public List<LSlogilablimsorderdetail> Getorderbytypeandflaganduserlazy(LSlogilablimsorderdetail objorder, Map<String, Object> mapOrders)
	{
		List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objorder.getLsuserMaster());
		List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingIn(lstteammap);
		List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		
		if(objorder.getOrderflag().equals("N"))
		{
			lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndBatchcodeLessThanAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
					objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(),lstproject, objorder.getFromdate(), objorder.getTodate());
		}
		else
		{
			lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndBatchcodeLessThanAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
					objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(),lstproject, objorder.getFromdate(), objorder.getTodate());
		}
		
		mapOrders.put("orders", lstorder);
		
		return lstorder;
	}
	
	public List<LSlogilablimsorderdetail> Getorderallbytypeandflaglazy(LSlogilablimsorderdetail objorder, Map<String, Object> mapOrders)
	{
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		if(objorder.getOrderflag().equals("N"))
		{
			lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndBatchcodeLessThanAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
				objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(), objorder.getFromdate(), objorder.getTodate());
		}
		else
		{
			lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndBatchcodeLessThanAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
					objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(), objorder.getFromdate(), objorder.getTodate());
		}
		
		mapOrders.put("orders", lstorder);
		
		return lstorder;
	}
	
	public List<LSlogilablimsorderdetail> Getorderallbytypeandflaganduserlazy(LSlogilablimsorderdetail objorder, Map<String, Object> mapOrders)
	{
		List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objorder.getLsuserMaster());
		List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingIn(lstteammap);
		List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		
		if(objorder.getOrderflag().equals("N"))
		{
			lstorder = lslogilablimsorderdetailRepository.findFirst10ByFiletypeAndOrderflagAndBatchcodeLessThanAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
					objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(),lstproject, objorder.getFromdate(), objorder.getTodate());
		}
		else
		{
			lstorder = lslogilablimsorderdetailRepository.findFirst10ByFiletypeAndOrderflagAndBatchcodeLessThanAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
					objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(),lstproject, objorder.getFromdate(), objorder.getTodate());
		}
		
		mapOrders.put("orders", lstorder);
		
		return lstorder;
	}
	
	public List<LSworkflow> GetWorkflowonUser(LSuserMaster objuser)
	{
		List<LSworkflowgroupmapping> lsworkflowgroupmapping = lsworkflowgroupmappingRepository.findBylsusergroup(objuser.getLsusergroup());
		List<LSworkflow> lsworkflow = lsworkflowRepository.findByLsworkflowgroupmappingIn(lsworkflowgroupmapping);
		
		return lsworkflow;
	}
	
	public Map<String, Object> GetWorkflowanduseronUsercode(LSuserMaster usercode)
	{
		LSuserMaster objuser = lsuserMasterRepository.findByusercode(usercode.getUsercode());
		List<LSworkflowgroupmapping> lsworkflowgroupmapping = lsworkflowgroupmappingRepository.findBylsusergroup(objuser.getLsusergroup());
		List<LSworkflow> lsworkflow = lsworkflowRepository.findByLsworkflowgroupmappingIn(lsworkflowgroupmapping);
		
		Map<String, Object> maplogindetails = new HashMap<String, Object>();
		maplogindetails.put("workflow", lsworkflow);
		maplogindetails.put("user", objuser);
		return maplogindetails;
	}
	
	public LSlogilablimsorderdetail GetorderStatus(LSlogilablimsorderdetail objorder)
	{
		LSlogilablimsorderdetail objupdatedorder = lslogilablimsorderdetailRepository.findByBatchcode(objorder.getBatchcode());
		objupdatedorder.setLsLSlimsorder(lSlimsorderRepository.findBybatchid(objorder.getBatchid()));
		if(objupdatedorder.getLockeduser() != null)
		{
			objupdatedorder.setIsLock(1);
		}
		else
		{
			objupdatedorder.setIsLock(0);
		}
		
		if(objupdatedorder.getLockeduser() != null && objorder.getObjLoggeduser() != null 
				&& objupdatedorder.getLockeduser().equals(objorder.getObjLoggeduser().getUsercode()))
		{
			objupdatedorder.setIsLockbycurrentuser(1);
		}
		else
		{
			objupdatedorder.setIsLockbycurrentuser(0);
		}
		
		
		if(objupdatedorder.getFiletype() != 0 && objupdatedorder.getOrderflag().toString().trim().equals("N")) {
			if(objupdatedorder.getLsworkflow().equals(lsworkflowRepository.findTopByAndLssitemasterOrderByWorkflowcodeDesc(objorder.getObjLoggeduser().getLssitemaster())))
			{
				objupdatedorder.setIsFinalStep(1);
			}
			else
			{
				objupdatedorder.setIsFinalStep(0);
			}	
		}
		
		if(objupdatedorder.getFiletype() == 0)
		{
			objupdatedorder.setLstestparameter(lStestparameterRepository.findByntestcode(objupdatedorder.getTestcode()));
		}
		
		if(objupdatedorder.getLssamplefile() != null)
		{
			if(objorder.getIsmultitenant() == 1)
			{
				CloudOrderCreation file = cloudOrderCreationRepository.findById((long)objupdatedorder.getLssamplefile().getFilesamplecode());
				if(file != null)
				{
					objupdatedorder.getLssamplefile().setFilecontent(file.getContent());
				}
			}
			else
			{
				OrderCreation file = mongoTemplate.findById(objupdatedorder.getLssamplefile().getFilesamplecode(), OrderCreation.class);
				if(file != null)
				{
					objupdatedorder.getLssamplefile().setFilecontent(file.getContent());
				}
			}
		}
		if(objorder.getObjsilentaudit() != null)
    	{
			objorder.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
    		lscfttransactionRepository.save(objorder.getObjsilentaudit());
    	}
		
		return objupdatedorder;
	}
	
	public List<LSresultdetails> GetResults(LSlogilablimsorderdetail objorder)
	{
		List<LSresultdetails> lsResults = new ArrayList<LSresultdetails>();
		List<LSlimsorder> lsLogilaborders = lslimsorderRepository.findBybatchid(objorder.getBatchid());
		List<String> lsorderno = new ArrayList<String>();
		
		if(lsLogilaborders != null && lsLogilaborders.size() >0)
		{
			int i=0;
			
			while(lsLogilaborders.size()>i) {
				lsorderno.add(lsLogilaborders.get(i).getOrderid().toString());
				i++;
			}
		}
		lsResults = lsresultdetailsRepository.findBylimsreferencecodeIn(lsorderno);
		
		return lsResults;
	}
	
	public void updateorderversioncontent(String Content, LSsamplefileversion objfile, Integer ismultitenant)
	{
		if(ismultitenant == 1)
		{
			CloudOrderVersion objsavefile = new CloudOrderVersion();
			objsavefile.setId((long)objfile.getFilesamplecodeversion());
			objsavefile.setContent(Content);
			
			cloudOrderVersionRepository.save(objsavefile);
		}
		else
		{
			OrderVersion objsavefile = new OrderVersion();
			objsavefile.setId((long)objfile.getFilesamplecodeversion());
			objsavefile.setContent(Content);
					
			//build query
			Query query = new Query(Criteria.where("id").is(objsavefile.getId()));
					
			Boolean recordcount = mongoTemplate.exists(query, OrderVersion.class);
					
			if(!recordcount)
			{
				mongoTemplate.insert(objsavefile);
			}
			else
			{
				Update update=new Update();
				update.set("content",Content);
				
				mongoTemplate.upsert(query, update, OrderVersion.class);
			}
		}
		//objfile.setFilecontent(Content);	
	}
	
	public LSsamplefile SaveResultfile(LSsamplefile objfile)
	{
		//Updatesamplefileversion(objfile);
		Integer lastversionindex = objfile.getLssamplefileversion().size()-1;
//		String Contentversion = objfile.getLssamplefileversion().get(lastversionindex).getFilecontent();
//		objfile.getLssamplefileversion().get(lastversionindex).setFilecontent(null);
//		lssamplefileversionRepository.save(objfile.getLssamplefileversion());
//		updateorderversioncontent(Contentversion,objfile.getLssamplefileversion().get(lastversionindex));
//		objfile.setProcessed(1);
		String Contentversion;
		if(lastversionindex == -1) {
			Contentversion = objfile.getFilecontent();
			lastversionindex = 0;
			lssamplefileversionRepository.save(objfile.getLssamplefileversion());
		}
		else {
			Contentversion = objfile.getLssamplefileversion().get(lastversionindex).getFilecontent();
			objfile.getLssamplefileversion().get(lastversionindex).setFilecontent(null);
			lssamplefileversionRepository.save(objfile.getLssamplefileversion());
			updateorderversioncontent(Contentversion,objfile.getLssamplefileversion().get(lastversionindex), objfile.getIsmultitenant());
		}
		objfile.setProcessed(1);
		if(objfile.getLssamplefileversion()!=null) {
			if(objfile.getObjActivity().getObjsilentaudit() != null)
	    	{
				String versionname ="";
				if(objfile.getLssamplefileversion().size() > 0){
					versionname = objfile.getLssamplefileversion().get(lastversionindex).getVersionname();
				}else {
					versionname = "version_1";
				}
				
				objfile.getObjActivity().getObjsilentaudit().setComments("Order ID: ELN"
						+objfile.getFilesamplecode()+" "+" was versioned to"+" " 
						+ versionname 
						+" "+"by the user"+ " "+objfile.getModifiedby().getUsername());
				objfile.getObjActivity().getObjsilentaudit().setTableName("LSfile");
	    		lscfttransactionRepository.save(objfile.getObjActivity().getObjsilentaudit());
	    	}
		}
		String Content = objfile.getFilecontent();
		objfile.setFilecontent(null);
		lssamplefileRepository.save(objfile);
		updateordercontent(Content, objfile, objfile.getIsmultitenant());
		
		objfile.setFilecontent(objfile.getTempfilecontent());
		
		if(objfile.getObjActivity() != null)
		{
			lsactivityRepository.save(objfile.getObjActivity());
		}
	
		if(objfile.getObjsilentaudit() != null)
    	{
			objfile.getObjsilentaudit().setTableName("LSsamplefile");
    		lscfttransactionRepository.save(objfile.getObjsilentaudit());
    	}
		//manual audit
		if(objfile.getObjuser()!=null) {
			objfile.getObjmanualaudit().setComments(objfile.getObjuser().getComments());
			objfile.getObjmanualaudit().setTableName("LSlogilablimsorderdetail");
    		lscfttransactionRepository.save(objfile.getObjmanualaudit());
		}
		objfile.setResponse(new Response());
		objfile.getResponse().setStatus(true);
		objfile.getResponse().setInformation("ID_DUMMY1");
		return objfile;
	}
	
	public void updateordercontent(String Content, LSsamplefile objfile, Integer ismultitenant)
	{
		if(ismultitenant == 1)
		{
			CloudOrderCreation objsavefile = new CloudOrderCreation();
			objsavefile.setId((long)objfile.getFilesamplecode());
			objsavefile.setContent(Content);
			objsavefile.setContentvalues(objfile.getContentvalues());
			objsavefile.setContentparameter(objfile.getContentparameter());
			
			cloudOrderCreationRepository.save(objsavefile);
		}
		else
		{
			OrderCreation objsavefile = new OrderCreation();
			objsavefile.setId((long)objfile.getFilesamplecode());
			objsavefile.setContent(Content);
			objsavefile.setContentvalues(objfile.getContentvalues());
			objsavefile.setContentparameter(objfile.getContentparameter());
					
			//build query
			Query query = new Query(Criteria.where("id").is(objsavefile.getId()));
					
			Boolean recordcount = mongoTemplate.exists(query, OrderCreation.class);
					
			if(!recordcount)
			{
				mongoTemplate.insert(objsavefile);
			}
			else
			{
				Update update=new Update();
				update.set("content",Content);
				update.set("contentvalues",objfile.getContentvalues());
				update.set("contentparameter",objfile.getContentparameter());	
				
				mongoTemplate.upsert(query, update, OrderCreation.class);
			}
		}
		//objfile.setFilecontent(Content);	
	}
	
	public LSlogilablimsorderdetail UpdateLimsOrder(LSlogilablimsorderdetail objorder)
	{
		List<LSlimsorder> lstorder = lslimsorderRepository.findBybatchid(objorder.getBatchid());
		lstorder.forEach((orders) -> orders.setMethodcode(objorder.getMethodcode()));
		lstorder.forEach((orders) -> orders.setInstrumentcode(objorder.getInstrumentcode()));
		objorder.getLssamplefile().setBatchcode(objorder.getBatchcode());
		lslimsorderRepository.save(lstorder);
		String Content = objorder.getLssamplefile().getFilecontent();
		objorder.getLssamplefile().setFilecontent(null);
		lssamplefileRepository.save(objorder.getLssamplefile());
		
		lslogilablimsorderdetailRepository.save(objorder);
		
		if(objorder.getObjsilentaudit() != null)
    	{
    		lscfttransactionRepository.save(objorder.getObjsilentaudit());
    	}
		//manual audit
		if(objorder.getObjuser()!=null) {
			objorder.getObjmanualaudit().setComments(objorder.getObjuser().getComments());
			objorder.getObjmanualaudit().setTableName("LSlogilablimsorderdetail");
    		lscfttransactionRepository.save(objorder.getObjmanualaudit());
		}
		
		if(objorder.getLssamplefile() != null)
		{
			updateordercontent(Content, objorder.getLssamplefile(), objorder.getIsmultitenant());
			objorder.getLssamplefile().setFilecontent(Content);
		}
		
		return objorder;
	}
	
	public LSlogilablimsorderdetail Getupdatedorder(LSlogilablimsorderdetail objorder)
	{
		LSlogilablimsorderdetail objupdated = lslogilablimsorderdetailRepository.findByBatchcode(objorder.getBatchcode());
		objupdated.setLsLSlimsorder(lSlimsorderRepository.findBybatchid(objorder.getBatchid()));

		if(objorder.getFiletype() == 0)
		{
			objupdated.setLstestparameter(lStestparameterRepository.findByntestcode(objupdated.getTestcode()));
		}
		
		if(objorder.getLssamplefile() != null)
		{
			if(objorder.getIsmultitenant() == 1)
			{
				CloudOrderCreation file = cloudOrderCreationRepository.findById((long)objorder.getLssamplefile().getFilesamplecode());
				if(file != null)
				{
					objupdated.getLssamplefile().setFilecontent(file.getContent());
				}
			}
			else
			{
				OrderCreation file = mongoTemplate.findById(objorder.getLssamplefile().getFilesamplecode(), OrderCreation.class);
				if(file != null)
				{
					objupdated.getLssamplefile().setFilecontent(file.getContent());
				}
			}
		}
		
		return objupdated;
	}
	
	public Map<String, Object> Getorderforlink(LSlogilablimsorderdetail objorder)
	{
		Map<String, Object> mapOrder = new HashMap<String, Object>();
		LSlogilablimsorderdetail objupdated = lslogilablimsorderdetailRepository.findByBatchcode(objorder.getBatchcode());
		
		if(objupdated.getLockeduser() != null)
		{
			objupdated.setIsLock(1);
		}
		else
		{
			objupdated.setIsLock(0);
		}
		
		if(objupdated.getLockeduser() != null && objorder.getObjLoggeduser() != null 
				&& objupdated.getLockeduser().equals(objorder.getObjLoggeduser().getUsercode()))
		{
			objupdated.setIsLockbycurrentuser(1);
		}
		else
		{
			objupdated.setIsLockbycurrentuser(0);
		}
		
		
		if(objupdated.getFiletype() != 0 && objupdated.getOrderflag().toString().trim().equals("N")) {
			if(objupdated.getLsworkflow().equals(lsworkflowRepository.findTopByAndLssitemasterOrderByWorkflowcodeDesc(objorder.getObjLoggeduser().getLssitemaster())))
			{
				objupdated.setIsFinalStep(1);
			}
			else
			{
				objupdated.setIsFinalStep(0);
			}	
		}
		
		if(objupdated.getFiletype() == 0)
		{
			objupdated.setLstestparameter(lStestparameterRepository.findByntestcode(objupdated.getTestcode()));
		}
		
		if(objupdated.getLssamplefile() != null)
		{
			if(objorder.getIsmultitenant() == 1)
			{
				CloudOrderCreation file = cloudOrderCreationRepository.findById((long)objupdated.getLssamplefile().getFilesamplecode());
				if(file != null)
				{
					objupdated.getLssamplefile().setFilecontent(file.getContent());
				}
			}
			else
			{
				OrderCreation file = mongoTemplate.findById(objupdated.getLssamplefile().getFilesamplecode(), OrderCreation.class);
				if(file != null)
				{
					objupdated.getLssamplefile().setFilecontent(file.getContent());
				}
			}
		}
		mapOrder.put("order", objupdated);
		mapOrder.put("version",lssamplefileversionRepository.findByFilesamplecodeOrderByVersionnoDesc(objupdated.getLssamplefile()));
		
		return mapOrder;
	}

	/*public Map<String, Object> GetLimsOrder(LSlimsorder clsOrder) {
		
		Map<String, Object> mapOrder = new HashMap<String, Object>();
		
		List<LSlimsorder> pendingOrder=lslimsorderRepository.findByorderflag("N");
		List<LSlimsorder> completOrder=lslimsorderRepository.findByorderflag("R");
		
		mapOrder.put("PendingOrder", pendingOrder);
		mapOrder.put("CompletedOrder", completOrder);
		
		return mapOrder;
	}*/
	
	public LSlogilablimsorderdetail CompleteOrder(LSlogilablimsorderdetail objorder)
	{
		if(objorder.getLssamplefile().getLssamplefileversion() != null)
		{
			lssamplefileversionRepository.save(objorder.getLssamplefile().getLssamplefileversion());
		}
		lssampleresultRepository.save(objorder.getLssamplefile().getLssampleresult());
		String Content = objorder.getLssamplefile().getFilecontent();
		objorder.getLssamplefile().setFilecontent(null);
		lssamplefileRepository.save(objorder.getLssamplefile());
		objorder.getLsparsedparameters().forEach((param) -> param.setBatchcode(objorder.getBatchcode()));
		lsparsedparametersRespository.save(objorder.getLsparsedparameters());
		lsorderworkflowhistoryRepositroy.save(objorder.getLsorderworkflowhistory());
		lslogilablimsorderdetailRepository.save(objorder);
		
		if(objorder.getLssamplefile()!= null)
		{
			updateordercontent(Content, objorder.getLssamplefile(), objorder.getIsmultitenant());
			objorder.getLssamplefile().setFilecontent(Content);
		}
		//silent audit
		if(objorder.getObjsilentaudit() != null)
    	{
	
			objorder.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
    		lscfttransactionRepository.save(objorder.getObjsilentaudit());
    	}
		//manual audit
		if(objorder.getObjuser()!=null) {
			objorder.getObjmanualaudit().setComments(objorder.getObjuser().getComments());
			objorder.getObjmanualaudit().setTableName("LSlogilablimsorderdetail");
    		lscfttransactionRepository.save(objorder.getObjmanualaudit());
		}
		updatenotificationfororder(objorder);
		objorder.setResponse(new Response());
		objorder.getResponse().setStatus(true);
		objorder.getResponse().setInformation("ID_ORDERCMPLT");
		return objorder;
	}
	
	public LSlogilablimsorderdetail updateworflowforOrder(LSlogilablimsorderdetail objorder)
	{
		LSlogilablimsorderdetail lsOrder =lslogilablimsorderdetailRepository.findByBatchcode(objorder.getBatchcode());

		updatenotificationfororderworkflow(objorder, lsOrder.getLsworkflow());
		
		lsorderworkflowhistoryRepositroy.save(objorder.getLsorderworkflowhistory());
		lslogilablimsorderdetailRepository.save(objorder);
		
//		silent audit
		if(objorder.getLsorderworkflowhistory().get(objorder.getLsorderworkflowhistory().size()-1).getObjsilentaudit() != null)
    	{
			objorder.getLsorderworkflowhistory().get(objorder.getLsorderworkflowhistory().size()-1).getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
    		lscfttransactionRepository.save(objorder.getLsorderworkflowhistory().get(objorder.getLsorderworkflowhistory().size()-1).getObjsilentaudit());
    	}
		return objorder;
	}
	
	public boolean Updatesamplefileversion(LSsamplefile objfile)
	{
		int Versionnumber = 0;
		LSsamplefileversion objLatestversion = lssamplefileversionRepository.findFirstByFilesamplecodeOrderByVersionnoDesc(objfile);
		if(objLatestversion != null)
		{
			Versionnumber = objLatestversion.getVersionno();
		}
		
		Versionnumber++;
		
		LSsamplefile objesixting  = lssamplefileRepository.findByfilesamplecode(objfile.getFilesamplecode());
		if(objesixting == null)
		{
			objesixting = objfile;
		}
		LSsamplefileversion objversion = new LSsamplefileversion();
		
		objversion.setBatchcode(objesixting.getBatchcode());
		objversion.setCreateby(objesixting.getCreateby());
		objversion.setCreatebyuser(objesixting.getCreatebyuser());
		objversion.setCreatedate(objesixting.getCreatedate());
		objversion.setModifiedby(objesixting.getModifiedby());
		objversion.setModifieddate(objesixting.getModifieddate());
		objversion.setFilecontent(objesixting.getFilecontent());
		objversion.setProcessed(objesixting.getProcessed());
		objversion.setResetflag(objesixting.getResetflag());
		objversion.setTestid(objesixting.getTestid());
		objversion.setVersionno(Versionnumber);
		objversion.setFilesamplecode(objesixting);
		
		lssamplefileversionRepository.save(objversion);
		if(objfile.getObjActivity().getObjsilentaudit() != null)
    	{
			//objpwd.getObjsilentaudit().setModuleName("UserManagement");
			objfile.getObjActivity().getObjsilentaudit().setComments("Sheet"+" "+objfile.getFilesamplecode()+" "+" was versioned to version_"+Versionnumber +" "+"by the user"+ " "+objfile.getLsuserMaster().getUsername());
			//objpwd.getObjsilentaudit().setActions("view");
			//objpwd.getObjsilentaudit().setSystemcoments("System Generated");
			objfile.getObjActivity().getObjsilentaudit().setTableName("LSfile");
    		lscfttransactionRepository.save(objfile.getObjActivity().getObjsilentaudit());
    	}
		
		return true;
	}
	
	public List<LSsamplefileversion> Getfileversions(LSsamplefile objfile)
	{
		return lssamplefileversionRepository.findByFilesamplecodeOrderByVersionnoDesc(objfile);
	}
	
	public String GetfileverContent(LSsamplefile objfile)
	{
		String Content = "";
		
		if(objfile.getVersionno() == null || objfile.getVersionno() == 0)
		{
			LSsamplefile objesixting = lssamplefileRepository.findByfilesamplecode(objfile.getFilesamplecode());
			Content = objesixting.getFilecontent();
			if(objfile != null)
			{
				if(objfile.getIsmultitenant() == 1)
				{
					CloudOrderCreation file = cloudOrderCreationRepository.findById((long)objfile.getFilesamplecode());
					if(file != null)
					{
						Content = file.getContent();
					}
				}
				else
				{
					OrderCreation file = mongoTemplate.findById(objfile.getFilesamplecode(), OrderCreation.class);
					if(file != null)
					{
						Content = file.getContent();
					}
				}
			}
		}
		else
		{
			LSsamplefileversion objVersion = lssamplefileversionRepository.findByFilesamplecodeAndVersionnoOrderByVersionnoDesc(objfile, objfile.getVersionno());
			Content = objVersion.getFilecontent();
			
			if(objVersion != null)
			{
				if(objfile.getIsmultitenant() == 1)
				{
					CloudOrderVersion file = cloudOrderVersionRepository.findById((long)objVersion.getFilesamplecodeversion());
					if(file != null)
					{
						Content = file.getContent();
					}
				}
				else
				{
					OrderVersion file = mongoTemplate.findById(objVersion.getFilesamplecodeversion(), OrderVersion.class);
					if(file != null)
					{
						Content = file.getContent();
					}
				}
			}
		}
	
		return Content;
	}

	public List<LSlogilablimsorderdetail> Getorderbyfile(LSlogilablimsorderdetail objorder)
	{
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		
		if(objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator"))
		{
			lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndLsfileOrderByBatchcodeDesc(objorder.getFiletype(), objorder.getLsfile());
		}
		else
		{
			List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objorder.getLsuserMaster());
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingIn(lstteammap);
			List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
			lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndLsfileAndLsprojectmasterInOrderByBatchcodeDesc(objorder.getFiletype(), objorder.getLsfile(), lstproject);
		}
		
		return lstorder;
	}
	
	public List<LSlogilablimsorderdetail> Getexcelorder(LSlogilablimsorderdetail objorder)
	{
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		if(objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator"))
		{
			lstorder = lslogilablimsorderdetailRepository.findByFiletypeOrderByBatchcodeDesc(objorder.getFiletype());
		}
		else
		{
			List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objorder.getLsuserMaster());
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingIn(lstteammap);
			List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
			lstorder = lslogilablimsorderdetailRepository.findByFiletypeAndLsprojectmasterInOrderByBatchcodeDesc(objorder.getFiletype(), lstproject);
		}
		return lstorder;
	}
	
	public LSlogilablimsorderdetail updateVersionandWorkflowhistory(LSlogilablimsorderdetail objorder)
	{
		objorder = lslogilablimsorderdetailRepository.findByBatchcode(objorder.getBatchcode());
//		if(objorder.getLssamplefile() != null)
//		{
//			objorder.getLssamplefile().setLssamplefileversion(lssamplefileversionRepository.getfileversiononbatchcode(objorder.getBatchcode()));
//		}
//		else
//		{
//			LSsamplefile objsample = new LSsamplefile();
//			objsample.setLssamplefileversion(lssamplefileversionRepository.getfileversiononbatchcode(objorder.getBatchcode()));
//			objorder.setLssamplefile(objsample);
//		}
//		
//		objorder.setLsorderworkflowhistory(lsorderworkflowhistoryRepositroy.findByBatchcodeOrderByHistorycode(objorder.getBatchcode()));
		
		return objorder;
	}

	public LSsamplefile GetResultfileverContent(LSsamplefile objfile) {

			LSsamplefile objesixting = lssamplefileRepository.findByfilesamplecode(objfile.getFilesamplecode());
			if(objesixting != null)
			{
				if(objfile.getIsmultitenant() == 1)
				{
					CloudOrderCreation file = cloudOrderCreationRepository.findById((long)objfile.getFilesamplecode());
					if(file != null)
					{
						objesixting.setFilecontent(file.getContent());
					}
				}
				else
				{
					OrderCreation file = mongoTemplate.findById(objesixting.getFilesamplecode(), OrderCreation.class);
					if(file != null)
					{
						objesixting.setFilecontent(file.getContent());
					}
				}
			}
		return objesixting;
	}
	
	public LSlogilablimsorderdetail Uploadattachments(MultipartFile file, Long batchcode, String filename ,
			String fileexe, Integer usercode, Date currentdate,Integer islargefile) throws IOException
	{
		
		LSlogilablimsorderdetail objorder = lslogilablimsorderdetailRepository.findByBatchcode(batchcode);
		
		LsOrderattachments objattachment = new LsOrderattachments();
		
		if(islargefile == 0)
		{
			OrderAttachment objfile = fileManipulationservice.storeattachment(file);
			if(objfile != null)
			{
				objattachment.setFileid(objfile.getId());
			}
		}
		else
		{
			String id=  fileManipulationservice.storeLargeattachment(filename, file);
			if(id != null)
			{
				objattachment.setFileid(id);
			}
		}
		
		objattachment.setFilename(filename);
		objattachment.setFileextension(fileexe);
		objattachment.setCreateby(lsuserMasterRepository.findByusercode(usercode));
		objattachment.setCreatedate(currentdate);
		objattachment.setBatchcode(objorder.getBatchcode());
		objattachment.setIslargefile(islargefile);
		
		LSuserMaster username=lsuserMasterRepository.findByusercode(usercode);
    	String name=username.getUsername();
       	LScfttransaction list= new LScfttransaction();
    	list.setModuleName("Register Task Orders & Execute");
    	list.setComments(name+" "+"Uploaded the attachement in Order ID: "+objorder.getBatchid() +" "+"successfully");
    	list.setActions("Insert");
    	list.setSystemcoments("System Generated");
    	list.setTableName("profile");
    	list.setTransactiondate(currentdate);
    	list.setLsuserMaster(usercode);
		lscfttransactionRepository.save(list);
		if(objorder!= null && objorder.getLsOrderattachments()!=null )
		{
			objorder.getLsOrderattachments().add(objattachment);
		}
		else
		{
			objorder.setLsOrderattachments(new ArrayList<LsOrderattachments>());
			objorder.getLsOrderattachments().add(objattachment);
		}
			
		lsOrderattachmentsRepository.save(objorder.getLsOrderattachments());
		
		
		return objorder;
	}
	
	public LSlogilablimsorderdetail CloudUploadattachments(MultipartFile file, Long batchcode, String filename ,
			String fileexe, Integer usercode, Date currentdate,Integer islargefile) throws IOException
	{
		
		LSlogilablimsorderdetail objorder = lslogilablimsorderdetailRepository.findByBatchcode(batchcode);
		
		LsOrderattachments objattachment = new LsOrderattachments();
		
		if(islargefile == 0)
		{
			CloudOrderAttachment objfile = cloudFileManipulationservice.storeattachment(file);
			if(objfile != null)
			{
				objattachment.setFileid(objfile.getId().toString());
			}
		}
		
		
		objattachment.setFilename(filename);
		objattachment.setFileextension(fileexe);
		objattachment.setCreateby(lsuserMasterRepository.findByusercode(usercode));
		objattachment.setCreatedate(currentdate);
		objattachment.setBatchcode(objorder.getBatchcode());
		objattachment.setIslargefile(islargefile);
		
		LSuserMaster username=lsuserMasterRepository.findByusercode(usercode);
    	String name=username.getUsername();
       	LScfttransaction list= new LScfttransaction();
    	list.setModuleName("Register Task Orders & Execute");
    	list.setComments(name+" "+"Uploaded the attachement in Order ID: "+objorder.getBatchid() +" "+"successfully");
    	list.setActions("Insert");
    	list.setSystemcoments("System Generated");
    	list.setTableName("profile");
    	list.setTransactiondate(currentdate);
    	list.setLsuserMaster(usercode);
		lscfttransactionRepository.save(list);
		if(objorder!= null && objorder.getLsOrderattachments()!=null )
		{
			objorder.getLsOrderattachments().add(objattachment);
		}
		else
		{
			objorder.setLsOrderattachments(new ArrayList<LsOrderattachments>());
			objorder.getLsOrderattachments().add(objattachment);
		}
			
		lsOrderattachmentsRepository.save(objorder.getLsOrderattachments());
		
		
		if(islargefile == 1)
		{
			String filenameval = "attach_" +objorder.getBatchcode() +"_" + objorder.getLsOrderattachments().get(objorder.getLsOrderattachments().lastIndexOf(objattachment)).getAttachmentcode()+"_" + filename;
			String id=  cloudFileManipulationservice.storeLargeattachment(filenameval, file);
			if(id != null)
			{
				objattachment.setFileid(id);
			}
			lsOrderattachmentsRepository.save(objorder.getLsOrderattachments());
		}
		
		return objorder;
	}
	
	public LsOrderattachments downloadattachments(LsOrderattachments objattachments)
	{
		OrderAttachment objfile = fileManipulationservice.retrieveFile(objattachments);
		if(objfile != null)
		{
			objattachments.setFile(objfile.getFile());

		}
//		silent audit
		if(objattachments.getObjsilentaudit() != null)
    	{
			objattachments.getObjsilentaudit().setTableName("LsOrderattachments");
    		lscfttransactionRepository.save(objattachments.getObjsilentaudit());
    	}
		return objattachments;
	}
	
	public LsOrderattachments Clouddownloadattachments(LsOrderattachments objattachments)
	{
		CloudOrderAttachment objfile = cloudFileManipulationservice.retrieveFile(objattachments);
		if(objfile != null)
		{
			objattachments.setFile(objfile.getFile());

		}
//		silent audit
		if(objattachments.getObjsilentaudit() != null)
    	{
			objattachments.getObjsilentaudit().setTableName("LsOrderattachments");
    		lscfttransactionRepository.save(objattachments.getObjsilentaudit());
    	}
		return objattachments;
	}
	
	public GridFSDBFile retrieveLargeFile(String fileid) throws IllegalStateException, IOException
	{
		return fileManipulationservice.retrieveLargeFile(fileid);
	}
	
	public InputStream  retrieveColudLargeFile(String fileid) throws IOException
	{
		return cloudFileManipulationservice.retrieveLargeFile(fileid);
	}
	
	public LsOrderattachments deleteattachments(LsOrderattachments objattachments)
	{
		Response objresponse = new Response();
		Long deletecount = (long) -1;
		
		if(objattachments.getIslargefile() ==0) 
		{
			deletecount = fileManipulationservice.deleteattachments(objattachments.getFileid());
		}
		else
		{
			fileManipulationservice.deletelargeattachments(objattachments.getFileid());
			deletecount = (long) 1;
		}
		
		deletecount = lsOrderattachmentsRepository.deleteByAttachmentcode(objattachments.getAttachmentcode());
		
		if(deletecount > -1)
		{
			objresponse.setStatus(true);
		}
		else
		{
			objresponse.setStatus(false);
		}
		
		objattachments.setResponse(objresponse);
//		silent audit
		if(objattachments.getObjsilentaudit() != null)
    	{
			objattachments.getObjsilentaudit().setTableName("LsOrderattachments");
    		lscfttransactionRepository.save(objattachments.getObjsilentaudit());
    	}
		return objattachments;
	}
	
	public LsOrderattachments Clouddeleteattachments(LsOrderattachments objattachments)
	{
		Response objresponse = new Response();
		Long deletecount = (long) -1;
		
		if(objattachments.getIslargefile() ==0) 
		{
			deletecount = cloudFileManipulationservice.deleteattachments(objattachments.getFileid());
		}
		else
		{
			cloudFileManipulationservice.deletelargeattachments(objattachments.getFileid());
			deletecount = (long) 1;
		}
		
		deletecount = lsOrderattachmentsRepository.deleteByAttachmentcode(objattachments.getAttachmentcode());
		
		if(deletecount > -1)
		{
			objresponse.setStatus(true);
		}
		else
		{
			objresponse.setStatus(false);
		}
		
		objattachments.setResponse(objresponse);
//		silent audit
		if(objattachments.getObjsilentaudit() != null)
    	{
			objattachments.getObjsilentaudit().setTableName("LsOrderattachments");
    		lscfttransactionRepository.save(objattachments.getObjsilentaudit());
    	}
		return objattachments;
	}
	
	public Lsordershareto Insertshareorder(Lsordershareto objordershareto)
	{
		Lsordershareto existingshare = lsordersharetoRepository.findBySharebyunifiedidAndSharetounifiedidAndOrdertypeAndSharebatchcode(
				objordershareto.getSharebyunifiedid(), objordershareto.getSharetounifiedid(), objordershareto.getOrdertype(), objordershareto.getSharebatchcode());
		if(existingshare != null)
		{
			objordershareto.setSharetocode(existingshare.getSharetocode());
			objordershareto.setSharedon(existingshare.getSharedon());
		}
		
		lsordersharetoRepository.save(objordershareto);
		
		return objordershareto;
	}	
	
	public Map<String, Object>  Insertshareorderby(Lsordersharedby objordershareby)
	{
		Map<String, Object> map=new HashMap<>();
		Lsordersharedby existingshare = lsordersharedbyRepository.findBySharebyunifiedidAndSharetounifiedidAndOrdertypeAndSharebatchcode(
				objordershareby.getSharebyunifiedid(), objordershareby.getSharetounifiedid(), objordershareby.getOrdertype(), objordershareby.getSharebatchcode());
		if(existingshare != null)
		{
			objordershareby.setSharedbycode(existingshare.getSharedbycode());
			objordershareby.setSharedon(existingshare.getSharedon());
		}
		lsordersharedbyRepository.save(objordershareby);
		
		long sharedbycount = 0;
		
		sharedbycount = lsordersharedbyRepository.countBySharebyunifiedidAndOrdertypeAndSharestatusOrderBySharedbycodeDesc(objordershareby.getSharebyunifiedid(), objordershareby.getOrdertype(), 1);
				
		map.put("order", objordershareby);
		map.put("sharedbycount", sharedbycount);
		
		return map;
	}	
	
	public List<Lsordersharedby> Getordersharedbyme(Lsordersharedby lsordersharedby)
	{
		return lsordersharedbyRepository.findBySharebyunifiedidAndOrdertypeAndSharestatusOrderBySharedbycodeDesc(lsordersharedby.getSharebyunifiedid(), lsordersharedby.getOrdertype(),1);
	}
	
	public List<Lsordershareto> Getordersharetome(Lsordershareto lsordershareto)
	{
		return lsordersharetoRepository.findBySharetounifiedidAndOrdertypeAndSharestatusOrderBySharetocodeDesc(lsordershareto.getSharetounifiedid(), lsordershareto.getOrdertype(),1);
	}
	
	public Lsordersharedby Unshareorderby(Lsordersharedby objordershareby)
	{
		Lsordersharedby existingshare = lsordersharedbyRepository.findOne(objordershareby.getSharedbycode());
		
		existingshare.setSharestatus(0);
		existingshare.setUnsharedon(objordershareby.getUnsharedon());
		lsordersharedbyRepository.save(existingshare);
		
		return existingshare;
	}

	public Lsordershareto Unshareorderto(Lsordershareto lsordershareto)
	{
		Lsordershareto existingshare = lsordersharetoRepository.findOne(lsordershareto.getSharetocode());
		
		existingshare.setSharestatus(0);
		existingshare.setUnsharedon(lsordershareto.getUnsharedon());
		lsordersharetoRepository.save(existingshare);
		
		return existingshare;
	}
	
	public Lsordersharedby GetsharedorderStatus(Lsordersharedby objorder)
	{
		
		LSlogilablimsorderdetail objorgorder = new LSlogilablimsorderdetail();
		
		objorgorder.setBatchcode(objorder.getSharebatchcode());
		objorgorder.setObjLoggeduser(objorder.getObjLoggeduser());
		objorgorder.setObjsilentaudit(objorder.getObjsilentaudit());
		objorgorder.setObjmanualaudit(objorder.getObjmanualaudit());
		objorgorder.setIsmultitenant(objorder.getIsmultitenant());
		
		objorgorder = GetorderStatus(objorgorder);
		
		objorder= lsordersharedbyRepository.findOne(objorder.getSharedbycode());
		
		objorder.setObjorder(objorgorder);
		
		return objorder;
	}
	
	public Lsordershareto GetsharedtomeorderStatus(Lsordershareto objorder)
	{
		
		LSlogilablimsorderdetail objorgorder = new LSlogilablimsorderdetail();
		
		objorgorder.setBatchcode(objorder.getSharebatchcode());
		objorgorder.setObjLoggeduser(objorder.getObjLoggeduser());
		objorgorder.setObjsilentaudit(objorder.getObjsilentaudit());
		objorgorder.setObjmanualaudit(objorder.getObjmanualaudit());
		objorgorder.setIsmultitenant(objorder.getIsmultitenant());
		
		objorgorder = GetorderStatus(objorgorder);
		
		//objorder= lsordersharetoRepository.findOne(objorder.getSharetocode());
		
		objorder.setObjorder(objorgorder);
		
		return objorder;
	}
	
	public LSsamplefile GetResultsharedfileverContent(LSsamplefile objfile) {
		return  GetResultfileverContent(objfile);
	}
	
	public LSsamplefile SaveSharedResultfile(LSsamplefile objfile)
	{
		return SaveResultfile(objfile);
	}
	
	public LSlogilablimsorderdetail SharedCloudUploadattachments(MultipartFile file, Long batchcode, String filename ,
			String fileexe, Integer usercode, Date currentdate,Integer islargefile) throws IOException
	{
		return CloudUploadattachments(file, batchcode, filename, fileexe,usercode,currentdate, islargefile);
	}
	
	public LSlogilablimsorderdetail SharedUploadattachments(MultipartFile file, Long batchcode, String filename ,
			String fileexe, Integer usercode, Date currentdate,Integer islargefile) throws IOException
	{
		return Uploadattachments(file, batchcode, filename , fileexe, usercode, currentdate, islargefile);
	}
	
	public LsOrderattachments SharedClouddeleteattachments(LsOrderattachments objattachments)
	{
		return Clouddeleteattachments(objattachments);
	}
	
	public LsOrderattachments shareddeleteattachments(LsOrderattachments objattachments)
	{
		return deleteattachments(objattachments);
	}
	
	public LsOrderattachments SharedClouddownloadattachments(LsOrderattachments objattachments)
	{
		return Clouddownloadattachments(objattachments);
	}
	
	public LsOrderattachments Shareddownloadattachments(LsOrderattachments objattachments)
	{
		return downloadattachments(objattachments);
	}
	
	public InputStream  sharedretrieveColudLargeFile(String fileid) throws IOException
	{
		return cloudFileManipulationservice.retrieveLargeFile(fileid);
	}
	
	public GridFSDBFile sharedretrieveLargeFile(String fileid) throws IllegalStateException, IOException
	{
		return fileManipulationservice.retrieveLargeFile(fileid);
	}

}
	
