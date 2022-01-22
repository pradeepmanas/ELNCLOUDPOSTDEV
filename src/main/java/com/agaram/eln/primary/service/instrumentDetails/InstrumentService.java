package com.agaram.eln.primary.service.instrumentDetails;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.fetchmodel.getorders.Logilabordermaster;
import com.agaram.eln.primary.fetchmodel.getorders.Logilaborders;
import com.agaram.eln.primary.model.cfr.LSactivity;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cloudFileManip.CloudOrderAttachment;
import com.agaram.eln.primary.model.cloudFileManip.CloudOrderCreation;
import com.agaram.eln.primary.model.cloudFileManip.CloudOrderVersion;
import com.agaram.eln.primary.model.cloudFileManip.CloudSheetCreation;
import com.agaram.eln.primary.model.fileManipulation.Fileimages;
import com.agaram.eln.primary.model.fileManipulation.Fileimagestemp;
import com.agaram.eln.primary.model.fileManipulation.LSfileimages;
import com.agaram.eln.primary.model.fileManipulation.OrderAttachment;
import com.agaram.eln.primary.model.fileManipulation.ResultorderlimsRefrence;
import com.agaram.eln.primary.model.fileManipulation.SheetorderlimsRefrence;
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
import com.agaram.eln.primary.model.instrumentDetails.LsOrderSampleUpdate;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.instrumentDetails.LsResultlimsOrderrefrence;
import com.agaram.eln.primary.model.instrumentDetails.LsSheetorderlimsrefrence;
import com.agaram.eln.primary.model.instrumentDetails.Lsordersharedby;
import com.agaram.eln.primary.model.instrumentDetails.Lsordershareto;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.methodsetup.ParserBlock;
import com.agaram.eln.primary.model.methodsetup.ParserField;
import com.agaram.eln.primary.model.methodsetup.SubParserField;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSfilemethod;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefileversion;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflowgroupmapping;
import com.agaram.eln.primary.model.templates.LsMappedTemplate;
import com.agaram.eln.primary.model.templates.LsUnmappedTemplate;
import com.agaram.eln.primary.repository.sheetManipulation.LSfileRepository;

import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSnotification;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserActions;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;
import com.agaram.eln.primary.repository.cfr.LSactivityRepository;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudOrderAttachmentRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudOrderCreationRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudOrderVersionRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudSheetCreationRepository;
import com.agaram.eln.primary.repository.fileManipulation.FileimagesRepository;
import com.agaram.eln.primary.repository.fileManipulation.FileimagestempRepository;
import com.agaram.eln.primary.repository.fileManipulation.LSfileimagesRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSfieldsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSinstrumentsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlimsorderRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSresultdetailsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsMethodFieldsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsOrderSampleUpdateRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsOrderattachmentsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsSheetorderlimsrefrenceRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LselninstrumentmasterRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsordersharedbyRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsordersharetoRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsorderworkflowhistoryRepositroy;
import com.agaram.eln.primary.repository.instrumentsetup.InstMasterRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesdataRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserBlockRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserFieldRepository;
import com.agaram.eln.primary.repository.methodsetup.SubParserFieldRepository;
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
import com.agaram.eln.primary.repository.instrumentDetails.LsResultlimsOrderrefrenceRepository;
import com.agaram.eln.primary.repository.usermanagement.LSnotificationRepository;
import com.agaram.eln.primary.repository.usermanagement.LSprojectmasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusersteamRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.fileManipulation.FileManipulationservice;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	private InstMasterRepository lsInstMasterRepository;
	@Autowired
	private MethodRepository lsMethodRepository;
	@Autowired
	private ParserBlockRepository lsParserBlockRepository;
	@Autowired
	private ParserFieldRepository lsParserRepository;
	@Autowired
	private SubParserFieldRepository lsSubParserRepository;
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

	@Autowired
	private LsOrderSampleUpdateRepository lsOrderSampleUpdateRepository;

	@Autowired
	private LsrepositoriesdataRepository LsrepositoriesdataRepository;

	@Autowired
	private CloudOrderAttachmentRepository CloudOrderAttachmentRepository;

	@Autowired
	private LSfileimagesRepository LSfileimagesRepository;

	@Autowired
	private FileimagesRepository FileimagesRepository;

	@Autowired
	private FileimagestempRepository FileimagestempRepository;

	@Autowired
	private LsSheetorderlimsrefrenceRepository lssheetorderlimsrefrenceRepository;

	@Autowired
	private LsResultlimsOrderrefrenceRepository LsResultlimsOrderrefrenceRepository;

	@Autowired
	private LSfileRepository LSfileRepository;

	public Map<String, Object> getInstrumentparameters(LSSiteMaster lssiteMaster) {
		Map<String, Object> obj = new HashMap<>();
		List<String> lsInst = new ArrayList<String>();
		lsInst.add("INST000");
		lsInst.add("LPRO");
		List<LsMethodFields> Methods = lsMethodFieldsRepository.findByinstrumentidNotIn(lsInst);

		if (lssiteMaster.getIsmultitenant() != 1) {
			List<LSfields> Generalfields = lSfieldsRepository.findByisactive(1);
			List<LSinstruments> Instruments = lSinstrumentsRepository.findAll();
			List<InstrumentMaster> InstrMaster = lsInstMasterRepository.findAll();
			List<LsMappedTemplate> MappedTemplate = LsMappedTemplateRepository.findAll();
			List<LsUnmappedTemplate> UnmappedTemplate = LsUnmappedTemplateRepository.findAll();

			List<Method> elnMethod = lsMethodRepository.findAll();
			List<ParserBlock> ParserBlock = lsParserBlockRepository.findAll();
			List<ParserField> ParserField = lsParserRepository.findAll();
			List<SubParserField> SubParserField = lsSubParserRepository.findAll();
			obj.put("Generalfields", Generalfields);
			obj.put("Instruments", Instruments);
			obj.put("Instrmaster", InstrMaster);
			obj.put("elninstrument", lselninstrumentmasterRepository
					.findBylssitemasterAndStatusOrderByInstrumentcodeDesc(lssiteMaster, 1));
			obj.put("Mappedtemplates", MappedTemplate);
			obj.put("Unmappedtemplates", UnmappedTemplate);
			obj.put("ELNMethods", elnMethod);
			obj.put("ParserBlock", ParserBlock);
			obj.put("ParserField", ParserField);
			obj.put("SubParserField", SubParserField);
		} else {
			List<LSfields> Generalfields = lSfieldsRepository.findBymethodname("ID_GENERAL");

			List<InstrumentMaster> InstrMaster = lsInstMasterRepository.findAll();
			List<Method> elnMethod = lsMethodRepository.findAll();
			List<ParserBlock> ParserBlock = lsParserBlockRepository.findAll();
			List<ParserField> ParserField = lsParserRepository.findAll();
			List<SubParserField> SubParserField = lsSubParserRepository.findAll();
			obj.put("Generalfields", Generalfields);

			obj.put("Instrmaster", InstrMaster);
			obj.put("ELNMethods", elnMethod);
			obj.put("ParserBlock", ParserBlock);
			obj.put("ParserField", ParserField);
			obj.put("SubParserField", SubParserField);
		}

		obj.put("Methods", Methods);

		return obj;
	}

	public LSlogilablimsorderdetail InsertELNOrder(LSlogilablimsorderdetail objorder) {

		objorder.setLsworkflow(lsworkflowRepository
				.findTopByAndLssitemasterOrderByWorkflowcodeAsc(objorder.getLsuserMaster().getLssitemaster()));
		objorder.setOrderflag("N");

		String Content = "";
		if (objorder.getLssamplefile() == null) {
			LSsamplefile objsamplefile = new LSsamplefile();
			if (objorder.getLsfile() != null) {
				if (objorder.getIsmultitenant() == 1) {
					CloudSheetCreation file = cloudSheetCreationRepository
							.findById((long) objorder.getLsfile().getFilecode());
					if (file != null) {
						Content = file.getContent();
					} else {
						Content = objorder.getLsfile().getFilecontent();
					}
				} else {
					SheetCreation file = mongoTemplate.findById(objorder.getLsfile().getFilecode(),
							SheetCreation.class);
					if (file != null) {
						Content = file.getContent();
					} else {
						Content = objorder.getLsfile().getFilecontent();
					}
				}

			}
			objsamplefile.setCreatedate(new Date());
			objsamplefile.setTestid(objorder.getTestcode());
			objsamplefile.setBatchcode(objorder.getBatchcode());
			objsamplefile.setProcessed(0);
			objorder.setLssamplefile(objsamplefile);
		} else {
			Content = objorder.getLssamplefile().getFilecontent();
		}

		if (objorder.getLssamplefile().getLssamplefileversion() != null) {

			String Contentversion = objorder.getLssamplefile().getLssamplefileversion().get(0).getFilecontent();
			objorder.getLssamplefile().getLssamplefileversion().get(0).setFilecontent(null);
			lssamplefileversionRepository.save(objorder.getLssamplefile().getLssamplefileversion());
			updateorderversioncontent(Contentversion, objorder.getLssamplefile().getLssamplefileversion().get(0),
					objorder.getIsmultitenant());
		}

		objorder.getLssamplefile().setFilecontent(null);
		lssamplefileRepository.save(objorder.getLssamplefile());

		if (objorder.getAssignedto() != null) {
			objorder.setLockeduser(objorder.getAssignedto().getUsercode());
		}

		lslogilablimsorderdetailRepository.save(objorder);

		String Batchid = "ELN" + objorder.getBatchcode();

		if (objorder.getFiletype() == 3) {
			Batchid = "RESEARCH" + objorder.getBatchcode();
		} else if (objorder.getFiletype() == 4) {
			Batchid = "EXCEL" + objorder.getBatchcode();
		} else if (objorder.getFiletype() == 5) {
			Batchid = "VALIDATE" + objorder.getBatchcode();
		}
		lslogilablimsorderdetailRepository.setbatchidBybatchcode(Batchid, objorder.getBatchcode());
		objorder.setBatchid(Batchid);

		lslogilablimsorderdetailRepository.save(objorder);

		lssamplefileRepository.setbatchcodeOnsamplefile(objorder.getBatchcode(),
				objorder.getLssamplefile().getFilesamplecode());

		List<LSlimsorder> lsorder = new ArrayList<LSlimsorder>();
		String Limsorder = objorder.getBatchcode().toString();

		if (objorder.getLsfile() != null && objorder.getLsfile().getLsmethods() != null
				&& objorder.getLsfile().getLsmethods().size() > 0) {
			int methodindex = 0;
			for (LSfilemethod objmethod : objorder.getLsfile().getLsmethods()) {
				LSlimsorder objLimsOrder = new LSlimsorder();
				String order = "";
				if (methodindex < 10) {
					order = Limsorder.concat("0" + methodindex);
				} else {
					order = Limsorder.concat("" + methodindex);
				}
				objLimsOrder.setOrderid(Long.parseLong(order));
				objLimsOrder.setBatchid(objorder.getBatchid());
				objLimsOrder.setMethodcode(objmethod.getMethodid());
				objLimsOrder.setInstrumentcode(objmethod.getInstrumentid());
				objLimsOrder.setTestcode(objorder.getTestcode() != null ? objorder.getTestcode().toString() : null);
				objLimsOrder.setOrderflag("N");

				lsorder.add(objLimsOrder);
				methodindex++;
			}

			lslimsorderRepository.save(lsorder);
		} else {
			LSfilemethod lsfilemethod = LSfilemethodRepository.findByFilecode(objorder.getLsfile().getFilecode());
			LSlimsorder objLimsOrder = new LSlimsorder();
			if (lsfilemethod != null) {
				objLimsOrder.setMethodcode(lsfilemethod.getMethodid());
				objLimsOrder.setInstrumentcode(lsfilemethod.getInstrumentid());
			}
			objLimsOrder.setOrderid(Long.parseLong(Limsorder.concat("00")));
			objLimsOrder.setBatchid(objorder.getBatchid());
			objLimsOrder.setTestcode(objorder.getTestcode() != null ? objorder.getTestcode().toString() : null);
			objLimsOrder.setOrderflag("N");

			lslimsorderRepository.save(objLimsOrder);
		}

		if (objorder.getObjuser() != null) {
			objorder.getObjmanualaudit().setComments(objorder.getObjuser().getComments());
			objorder.getObjmanualaudit().setTableName("LSlogilablimsorderdetail");
//			lscfttransactionRepository.save(objorder.getObjmanualaudit());
		}

		if (objorder.getObjsilentaudit() != null) {
			objorder.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
//			lscfttransactionRepository.save(objorder.getObjsilentaudit());
		}

		if (objorder.getLssamplefile() != null) {
			updateordercontent(Content, objorder.getLssamplefile(), objorder.getIsmultitenant());
		}

		updatenotificationfororder(objorder);

		return objorder;
	}

	public void updatenotificationfororder(LSlogilablimsorderdetail objorder) {
		try {
			if (objorder != null && objorder.getLsprojectmaster() != null
					&& objorder.getLsprojectmaster().getLsusersteam() != null) {
				LSusersteam objteam = lsusersteamRepository
						.findByteamcode(objorder.getLsprojectmaster().getLsusersteam().getTeamcode());
				if (objteam.getLsuserteammapping() != null && objteam.getLsuserteammapping().size() > 0) {
					String Details = "";
					String Notifiction = "";

					if (objorder.getAssignedto() == null) {
						if (objorder.getOrderflag().equalsIgnoreCase("R")) {
							Notifiction = "ORDERCOMPLETED";

							Details = "{\"ordercode\":\"" + objorder.getBatchcode() + "\", \"order\":\""
									+ objorder.getBatchid() + "\", \"previousworkflow\":\"" + ""
									+ "\", \"previousworkflowcode\":\"" + -1 + "\", \"currentworkflow\":\""
									+ objorder.getLsworkflow().getWorkflowname() + "\", \"completeduser\":\""
									+ objorder.getObjLoggeduser().getUsername() + "\", \"currentworkflowcode\":\""
									+ objorder.getLsworkflow().getWorkflowcode() + "\"}";
						} else {
							Notifiction = "ORDERCREATION";

							Details = "{\"ordercode\":\"" + objorder.getBatchcode() + "\", \"order\":\""
									+ objorder.getBatchid() + "\", \"previousworkflow\":\"" + ""
									+ "\", \"previousworkflowcode\":\"" + -1 + "\", \"currentworkflow\":\""
									+ objorder.getLsworkflow().getWorkflowname() + "\", \"currentworkflowcode\":\""
									+ objorder.getLsworkflow().getWorkflowcode() + "\"}";
						}
					} else {
						Notifiction = "ORDERCREATIONANDASSIGN";

						Details = "{\"ordercode\":\"" + objorder.getBatchcode() + "\", \"order\":\""
								+ objorder.getBatchid() + "\", \"previousworkflow\":\"" + ""
								+ "\", \"previousworkflowcode\":\"" + -1 + "\", \"currentworkflow\":\""
								+ objorder.getLsworkflow().getWorkflowname() + "\", \"assignedto\":\""
								+ objorder.getAssignedto().getUsername() + "\", \"assignedby\":\""
								+ objorder.getObjLoggeduser().getUsername() + "\", \"currentworkflowcode\":\""
								+ objorder.getLsworkflow().getWorkflowcode() + "\"}";
					}

					if (objorder.getAssignedto() == null) {
						List<LSuserteammapping> lstusers = objteam.getLsuserteammapping();
						List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
						for (int i = 0; i < lstusers.size(); i++) {
							if (objorder.getLsuserMaster().getUsercode() != lstusers.get(i).getLsuserMaster()
									.getUsercode()) {
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
					} else {
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
		} catch (Exception e) {
			logger.error("updatenotificationfororder : " + e.getMessage());
		}
	}

	public void updatenotificationfororderworkflow(LSlogilablimsorderdetail objorder, LSworkflow previousworkflow) {
		try {
			String Details = "";
			String Notifiction = "";
			if (objorder.getApprovelstatus() != null) {
				LSusersteam objteam = lsusersteamRepository
						.findByteamcode(objorder.getLsprojectmaster().getLsusersteam().getTeamcode());

				if (objorder.getApprovelstatus() == 1) {
					Notifiction = "ORDERAPPROVED";
				} else if (objorder.getApprovelstatus() == 2) {
					Notifiction = "ORDERRETERNED";
				} else if (objorder.getApprovelstatus() == 3) {
					Notifiction = "ORDERREJECTED";
				}

				int perviousworkflowcode = previousworkflow != null ? previousworkflow.getWorkflowcode() : -1;
				String previousworkflowname = previousworkflow != null ? previousworkflow.getWorkflowname() : "";

				if (previousworkflowname.equals(objorder.getLsworkflow().getWorkflowname())) {
					Notifiction = "ORDERFINALAPPROVAL";
				}

				Details = "{\"ordercode\":\"" + objorder.getBatchcode() + "\", \"order\":\"" + objorder.getBatchid()
						+ "\", \"previousworkflow\":\"" + previousworkflowname + "\", \"previousworkflowcode\":\""
						+ perviousworkflowcode + "\", \"currentworkflow\":\""
						+ objorder.getLsworkflow().getWorkflowname() + "\", \"currentworkflowcode\":\""
						+ objorder.getLsworkflow().getWorkflowcode() + "\"}";

				List<LSuserteammapping> lstusers = objteam.getLsuserteammapping();
				List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
				for (int i = 0; i < lstusers.size(); i++) {
					if (objorder.getObjLoggeduser().getUsercode() != lstusers.get(i).getLsuserMaster().getUsercode()) {
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
		} catch (Exception e) {
			logger.error("updatenotificationfororderworkflow : " + e.getMessage());
		}
	}

	public void updatenotificationfororder(LSlogilablimsorderdetail objorder, String currentworkflow) {
		try {
			if (objorder != null && objorder.getLsprojectmaster() != null
					&& objorder.getLsprojectmaster().getLsusersteam() != null) {
				LSusersteam objteam = lsusersteamRepository
						.findByteamcode(objorder.getLsprojectmaster().getLsusersteam().getTeamcode());
				if (objteam.getLsuserteammapping() != null && objteam.getLsuserteammapping().size() > 0) {
					String Details = "";
					String Notifiction = "";

					if (objorder.getApprovelstatus() != null) {

						if (objorder.getApprovelstatus() == 1) {
							Notifiction = "ORDERAPPROVED";
							Details = "{\"ordercode\":\"" + objorder.getBatchcode() + "\", \"order\":\""
									+ objorder.getBatchid() + "\", \"currentworkflow\":\"" + currentworkflow
									+ "\", \"movedworkflow\":\"" + objorder.getLsworkflow().getWorkflowname() + "\"}";
						} else if (objorder.getApprovelstatus() == 3) {
							Notifiction = "ORDERREJECTED";
							Details = "{\"ordercode\":\"" + objorder.getBatchcode() + "\", \"order\":\""
									+ objorder.getBatchid() + "\", \"workflowname\":\""
									+ objorder.getLsworkflow().getWorkflowname() + "\"}";
						}
					}

					List<LSuserteammapping> lstusers = objteam.getLsuserteammapping();
					List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
					for (int i = 0; i < lstusers.size(); i++) {
						if (objorder.getLsuserMaster().getUsercode() != lstusers.get(i).getLsuserMaster()
								.getUsercode()) {
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
		} catch (Exception e) {
			logger.error("updatenotificationfororder : " + e.getMessage());
		}
	}

	public LSactivity InsertActivities(LSactivity objActivity) {
		return lsactivityRepository.save(objActivity);
	}

	public List<LSlogilablimsorderdetail> Getorderbytype(LSlogilablimsorderdetail objorder) {
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		if (objorder.getOrderflag().equals("N")) {
			lstorder = lslogilablimsorderdetailRepository
					.findByFiletypeAndOrderflagAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(),
							objorder.getTodate());
		} else {
			lstorder = lslogilablimsorderdetailRepository
					.findByFiletypeAndOrderflagAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(),
							objorder.getTodate());
		}

		if (objorder.getObjsilentaudit() != null) {
			objorder.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
			lscfttransactionRepository.save(objorder.getObjsilentaudit());
		}

		return lstorder;
	}

	public List<LSlogilablimsorderdetail> Getorderbytypeanduser(LSlogilablimsorderdetail objorder) {
		List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objorder.getLsuserMaster());
		List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingIn(lstteammap);
		List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();

		if (objorder.getOrderflag().equals("N")) {
			lstorder = lslogilablimsorderdetailRepository
					.findByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), lstproject, objorder.getFromdate(),
							objorder.getTodate());

//			lstorder = lslogilablimsorderdetailRepository.findByOrderflagAndFiletypeAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
//			  objorder.getOrderflag(),objorder.getFiletype(),lstproject, objorder.getFromdate(), objorder.getTodate());
		} else {
			lstorder = lslogilablimsorderdetailRepository
					.findByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), lstproject, objorder.getFromdate(),
							objorder.getTodate());

//			lstorder = lslogilablimsorderdetailRepository.findByOrderflagAndFiletypeAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
//					objorder.getOrderflag(),objorder.getFiletype(), lstproject, objorder.getFromdate(), objorder.getTodate());
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

	public List<LSlogilablimsorderdetail> Getorderbytypeandflag(LSlogilablimsorderdetail objorder,
			Map<String, Object> mapOrders) {
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
//		List<Logilaborders> lstorder = new ArrayList<Logilaborders>();
		List<Long> lstBatchcode = new ArrayList<Long>();
		List<Integer> lstsamplefilecode = new ArrayList<Integer>();
		List<LSsamplefile> idList = new ArrayList<LSsamplefile>();
		if (objorder.getOrderflag().equals("N")) {

			if (objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearch().trim() != "") {

				lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeonFiletypeAndFlagandcreateddate(
						objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(), objorder.getTodate());

				if (lstBatchcode != null && lstBatchcode.size() > 0) {
					lstsamplefilecode = lssamplefileRepository.getFilesamplecodeByBatchcodeIn(lstBatchcode);
				}

				if (lstsamplefilecode != null && lstsamplefilecode.size() > 0) {
					idList = getsamplefileIds(lstsamplefilecode, objorder.getSearchCriteria(),
							objorder.getIsmultitenant());
					if (idList != null && idList.size() > 0) {
						lstorder = lslogilablimsorderdetailRepository
								.findByFiletypeAndOrderflagAndCreatedtimestampBetweenAndLssamplefileIn(
										objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(),
										objorder.getTodate(), idList);

//						lstorder= lslogilablimsorderdetailRepository.findByOrderflagAndFiletypeAndCreatedtimestampBetweenAndLssamplefileIn
//								( objorder.getOrderflag(),objorder.getFiletype(), objorder.getFromdate(), objorder.getTodate(), idList);
					}
				}
			} else {
				lstorder = lslogilablimsorderdetailRepository
						.findByFiletypeAndOrderflagAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
								objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(),
								objorder.getTodate());

//				lstorder= lslogilablimsorderdetailRepository.findByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc
//						( objorder.getOrderflag(), objorder.getFiletype(),objorder.getFromdate(), objorder.getTodate());
			}

		} else {
			if (objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearch().trim() != "") {
				lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeonFiletypeAndFlagandCompletedtime(
						objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(), objorder.getTodate());

				if (lstBatchcode != null && lstBatchcode.size() > 0) {
					lstsamplefilecode = lssamplefileRepository.getFilesamplecodeByBatchcodeIn(lstBatchcode);
				}

				if (lstsamplefilecode != null && lstsamplefilecode.size() > 0) {
					idList = getsamplefileIds(lstsamplefilecode, objorder.getSearchCriteria(),
							objorder.getIsmultitenant());
					if (idList != null && idList.size() > 0) {
						lstorder = lslogilablimsorderdetailRepository
								.findByFiletypeAndOrderflagAndCompletedtimestampBetweenAndLssamplefileIn(
										objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(),
										objorder.getTodate(), idList);

//						lstorder= lslogilablimsorderdetailRepository.findByOrderflagAndFiletypeAndCompletedtimestampBetweenAndLssamplefileIn
//								( objorder.getOrderflag(), objorder.getFiletype(),objorder.getFromdate(), objorder.getTodate(), idList);
					}
				}
			} else {
				lstorder = lslogilablimsorderdetailRepository
						.findByFiletypeAndOrderflagAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
								objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(),
								objorder.getTodate());

//				lstorder= lslogilablimsorderdetailRepository.findByOrderflagAndFiletypeAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc
//						( objorder.getOrderflag(),objorder.getFiletype(), objorder.getFromdate(), objorder.getTodate());
			}
		}

		if (objorder.getObjsilentaudit() != null) {
			objorder.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
			lscfttransactionRepository.save(objorder.getObjsilentaudit());
		}

		long pendingcount = 0;
		long completedcount = 0;

		if (objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearch().trim() != "") {
			if (objorder.getOrderflag().equals("N")) {
				if (idList != null) {
					pendingcount = idList.size();
				}

				completedcount = lslogilablimsorderdetailRepository
						.countByFiletypeAndOrderflagAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
								objorder.getFiletype(), "R", objorder.getFromdate(), objorder.getTodate());
			} else {
				if (idList != null) {
					completedcount = idList.size();
				}

				pendingcount = lslogilablimsorderdetailRepository
						.countByFiletypeAndOrderflagAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
								objorder.getFiletype(), "N", objorder.getFromdate(), objorder.getTodate());
			}

		} else {
			pendingcount = lslogilablimsorderdetailRepository
					.countByFiletypeAndOrderflagAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), "N", objorder.getFromdate(), objorder.getTodate());

			completedcount = lslogilablimsorderdetailRepository
					.countByFiletypeAndOrderflagAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), "R", objorder.getFromdate(), objorder.getTodate());
		}

		long sharedbycount = 0;
		long sharetomecount = 0;

		if (objorder.getObjLoggeduser() != null && objorder.getObjLoggeduser().getUnifieduserid() != null) {
			sharedbycount = lsordersharedbyRepository
					.countBySharebyunifiedidAndOrdertypeAndSharestatusOrderBySharedbycodeDesc(
							objorder.getObjLoggeduser().getUnifieduserid(), objorder.getFiletype(), 1);
			sharetomecount = lsordersharetoRepository
					.countBySharetounifiedidAndOrdertypeAndSharestatusOrderBySharetocodeDesc(
							objorder.getObjLoggeduser().getUnifieduserid(), objorder.getFiletype(), 1);
		}

		mapOrders.put("orders", lstorder);
		mapOrders.put("pendingcount", pendingcount);
		mapOrders.put("completedcount", completedcount);
		mapOrders.put("sharedbycount", sharedbycount);
		mapOrders.put("sharetomecount", sharetomecount);

		return lstorder;
	}

	public List<Logilaborders> GetorderbytypeandflagOrdersonly(LSlogilablimsorderdetail objorder,
			Map<String, Object> mapOrders) {
//		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilabLogilabsheetdetailsget();
		List<Logilaborders> lstorder = new ArrayList<Logilaborders>();
		List<Long> lstBatchcode = new ArrayList<Long>();
		List<Integer> lstsamplefilecode = new ArrayList<Integer>();
		List<LSsamplefile> idList = new ArrayList<LSsamplefile>();
		if (objorder.getOrderflag().equals("N")) {

			if (objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearch().trim() != "") {

				lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeonFiletypeAndFlagandcreateddate(
						objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(), objorder.getTodate());

				if (lstBatchcode != null && lstBatchcode.size() > 0) {
					lstsamplefilecode = lssamplefileRepository.getFilesamplecodeByBatchcodeIn(lstBatchcode);
				}

				if (lstsamplefilecode != null && lstsamplefilecode.size() > 0) {
					idList = getsamplefileIds(lstsamplefilecode, objorder.getSearchCriteria(),
							objorder.getIsmultitenant());
					if (idList != null && idList.size() > 0) {
//						lstorder= lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndCreatedtimestampBetweenAndLssamplefileIn(objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(), objorder.getTodate(), idList);

						lstorder = lslogilablimsorderdetailRepository
								.findByOrderflagAndFiletypeAndCreatedtimestampBetweenAndLssamplefileIn(
										objorder.getOrderflag(), objorder.getFiletype(), objorder.getFromdate(),
										objorder.getTodate(), idList);
					}
				}
			} else {
//				lstorder= lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(), objorder.getTodate());

				lstorder = lslogilablimsorderdetailRepository
						.findByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
								objorder.getOrderflag(), objorder.getFiletype(), objorder.getFromdate(),
								objorder.getTodate());
			}

		} else {
			if (objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearch().trim() != "") {
				lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeonFiletypeAndFlagandCompletedtime(
						objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(), objorder.getTodate());

				if (lstBatchcode != null && lstBatchcode.size() > 0) {
					lstsamplefilecode = lssamplefileRepository.getFilesamplecodeByBatchcodeIn(lstBatchcode);
				}

				if (lstsamplefilecode != null && lstsamplefilecode.size() > 0) {
					idList = getsamplefileIds(lstsamplefilecode, objorder.getSearchCriteria(),
							objorder.getIsmultitenant());
					if (idList != null && idList.size() > 0) {
//						lstorder= lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndCompletedtimestampBetweenAndLssamplefileIn(objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(), objorder.getTodate(), idList);

						lstorder = lslogilablimsorderdetailRepository
								.findByOrderflagAndFiletypeAndCompletedtimestampBetweenAndLssamplefileIn(
										objorder.getOrderflag(), objorder.getFiletype(), objorder.getFromdate(),
										objorder.getTodate(), idList);
					}
				}
			} else {
//				lstorder= lslogilablimsorderdetailRepository.findByFiletypeAndOrderflagAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(), objorder.getTodate());

				lstorder = lslogilablimsorderdetailRepository
						.findByOrderflagAndFiletypeAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
								objorder.getOrderflag(), objorder.getFiletype(), objorder.getFromdate(),
								objorder.getTodate());
			}
		}

		if (objorder.getObjsilentaudit() != null) {
			objorder.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
//			lscfttransactionRepository.save(objorder.getObjsilentaudit());
		}

		long pendingcount = 0;
		long completedcount = 0;

		if (objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearch().trim() != "") {
			if (objorder.getOrderflag().equals("N")) {
				if (idList != null) {
					pendingcount = idList.size();
				}

				completedcount = lslogilablimsorderdetailRepository
						.countByFiletypeAndOrderflagAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
								objorder.getFiletype(), "R", objorder.getFromdate(), objorder.getTodate());
			} else {
				if (idList != null) {
					completedcount = idList.size();
				}

				pendingcount = lslogilablimsorderdetailRepository
						.countByFiletypeAndOrderflagAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
								objorder.getFiletype(), "N", objorder.getFromdate(), objorder.getTodate());
			}

		} else {
			pendingcount = lslogilablimsorderdetailRepository
					.countByFiletypeAndOrderflagAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), "N", objorder.getFromdate(), objorder.getTodate());

			completedcount = lslogilablimsorderdetailRepository
					.countByFiletypeAndOrderflagAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), "R", objorder.getFromdate(), objorder.getTodate());
		}

		long sharedbycount = 0;
		long sharetomecount = 0;

		if (objorder.getObjLoggeduser() != null && objorder.getObjLoggeduser().getUnifieduserid() != null) {
			sharedbycount = lsordersharedbyRepository
					.countBySharebyunifiedidAndOrdertypeAndSharestatusOrderBySharedbycodeDesc(
							objorder.getObjLoggeduser().getUnifieduserid(), objorder.getFiletype(), 1);
			sharetomecount = lsordersharetoRepository
					.countBySharetounifiedidAndOrdertypeAndSharestatusOrderBySharetocodeDesc(
							objorder.getObjLoggeduser().getUnifieduserid(), objorder.getFiletype(), 1);
		}

		mapOrders.put("orders", lstorder);
		mapOrders.put("pendingcount", pendingcount);
		mapOrders.put("completedcount", completedcount);
		mapOrders.put("sharedbycount", sharedbycount);
		mapOrders.put("sharetomecount", sharetomecount);

		return lstorder;
	}

	public List<LSsamplefile> getsamplefileIds(List<Integer> lstsamplefilecode, SearchCriteria searchCriteria,
			Integer ismultitenant) {

		List<Long> idList = new ArrayList<Long>();
		String searchtext = searchCriteria.getContentsearch().replace("[", "\\[").replace("]", "\\]");
		if (ismultitenant == 0) {
			Query query = new Query();
			if (searchCriteria.getContentsearchtype() == 1) {
				query.addCriteria(Criteria.where("contentvalues").regex(searchtext, "i"));
			} else if (searchCriteria.getContentsearchtype() == 2) {
				query.addCriteria(Criteria.where("contentparameter").regex(searchtext, "i"));
			}

			query.addCriteria(Criteria.where("id").in(lstsamplefilecode)).with(new PageRequest(0, 5));

			List<OrderCreation> orders = mongoTemplate.find(query, OrderCreation.class);
			idList = orders.stream().map(OrderCreation::getId).collect(Collectors.toList());
		} else {
			List<CloudOrderCreation> orders = new ArrayList<CloudOrderCreation>();
			if (searchCriteria.getContentsearchtype() == 1) {
				orders = cloudOrderCreationRepository.findByContentvaluesequal(searchtext);
			} else if (searchCriteria.getContentsearchtype() == 2) {
				orders = cloudOrderCreationRepository.findByContentparameterequal(searchtext);
			}
			idList = orders.stream().map(CloudOrderCreation::getId).collect(Collectors.toList());
		}

		List<LSsamplefile> lstsample = new ArrayList<LSsamplefile>();

		if (idList != null && idList.size() > 0) {
			List<Integer> lssample = new ArrayList<Integer>();
			idList.forEach((n) -> lssample.add(Math.toIntExact(n)));

			lstsample = lssamplefileRepository.findByfilesamplecodeIn(lssample);
		}

		return lstsample;
	}

	public List<LSlogilablimsorderdetail> Getorderbytypeandflaganduser(LSlogilablimsorderdetail objorder,
			Map<String, Object> mapOrders) {
		List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objorder.getLsuserMaster());
		List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingIn(lstteammap);
		List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
//		List<Logilaborders> lstorder = new ArrayList<Logilaborders>();
		List<Long> lstBatchcode = new ArrayList<Long>();

		long pendingcount = 0;
		long completedcount = 0;
		long Assignedcount = 0;
		long Assignedpendingcount = 0;
		long Assignedcompletedcount = 0;
		long myordercount = 0;
		long myorderpendingcount = 0;
		long myordercompletedcount = 0;

		if (lstproject.size() > 0) {
			List<Integer> lstprojectcode = lsprojectmasterRepository.findProjectcodeByLsusersteamIn(lstteam);

			if (objorder.getOrderflag().equals("N")) {
				if (objorder.getSearchCriteria() != null
						&& objorder.getSearchCriteria().getContentsearch().trim() != "") {

					lstBatchcode = lslogilablimsorderdetailRepository
							.getBatchcodeonFiletypeAndFlagandProjectandcreateddate(objorder.getFiletype(),
									objorder.getOrderflag(), lstprojectcode, objorder.getFromdate(),
									objorder.getTodate());

					lstorder = getordersonsamplefileid(lstBatchcode, objorder);
//					lstorder = getordersonsamplefileidlsorder(lstBatchcode, objorder);

				} else {
					lstorder = lslogilablimsorderdetailRepository
							.findByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getFiletype(), objorder.getOrderflag(), lstproject, objorder.getFromdate(),
									objorder.getTodate());

//					lstorder = lslogilablimsorderdetailRepository.findByOrderflagAndFiletypeAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
//							 objorder.getOrderflag(),objorder.getFiletype(),lstproject, objorder.getFromdate(), objorder.getTodate());
				}
			} else if (objorder.getOrderflag().equals("A")) {
				if (objorder.getSearchCriteria() != null
						&& objorder.getSearchCriteria().getContentsearch().trim() != "") {
					lstBatchcode = lslogilablimsorderdetailRepository
							.getBatchcodeonFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestamp(
									objorder.getFiletype(), objorder.getLsuserMaster().getUsercode(),
									objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(),
									objorder.getTodate());

					lstorder = getordersonsamplefileid(lstBatchcode, objorder);
//					lstorder = getordersonsamplefileidlsorder(lstBatchcode, objorder);
					Assignedcount = lstorder.size();

					Assignedpendingcount = lstorder.stream()
							.filter(obj -> "N".equals(obj.getOrderflag() != null ? obj.getOrderflag().trim() : ""))
							.count();
					Assignedcompletedcount = lstorder.stream()
							.filter(obj -> "R".equals(obj.getOrderflag() != null ? obj.getOrderflag().trim() : ""))
							.count();

					if (objorder.getLsuserMaster() != null && objorder.getLsuserMaster().getLsuserActions() != null) {
						LSuserActions objaction = objorder.getLsuserMaster().getLsuserActions();
						if (objaction.getAssignedordershowpending() != null && objaction.getAssignedordershowall() != 1
								&& objaction.getAssignedordershowpending() == 1) {
							lstorder = lstorder.stream().filter(
									obj -> "N".equals(obj.getOrderflag() != null ? obj.getOrderflag().trim() : ""))
									.collect(Collectors.toList());
						} else if (objaction.getAssignedordershowcompleted() != null
								&& objaction.getAssignedordershowall() != 1
								&& objaction.getAssignedordershowcompleted() == 1) {
							lstorder = lstorder.stream().filter(
									obj -> "R".equals(obj.getOrderflag() != null ? obj.getOrderflag().trim() : ""))
									.collect(Collectors.toList());
						}
					}

				} else {
					if (objorder.getLsuserMaster() != null && objorder.getLsuserMaster().getLsuserActions() != null) {
						LSuserActions objaction = objorder.getLsuserMaster().getLsuserActions();
						if (objaction.getAssignedordershowall() != null && objaction.getAssignedordershowall() == 1) {
							lstorder = lslogilablimsorderdetailRepository
									.findByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
											objorder.getFiletype(), objorder.getLsuserMaster(),
											objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());

//							lstorder = lslogilablimsorderdetailRepository.findByLsuserMasterAndFiletypeAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
//									objorder.getLsuserMaster(),objorder.getFiletype(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
						} else if (objaction.getAssignedordershowpending() != null
								&& objaction.getAssignedordershowpending() == 1) {
							lstorder = lslogilablimsorderdetailRepository
									.findByFiletypeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
											objorder.getFiletype(), "N", objorder.getLsuserMaster(),
											objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());

//							lstorder = lslogilablimsorderdetailRepository.findByOrderflagAndFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
//									"N",objorder.getFiletype(),objorder.getLsuserMaster(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
						} else if (objaction.getAssignedordershowcompleted() != null
								&& objaction.getAssignedordershowcompleted() == 1) {
							lstorder = lslogilablimsorderdetailRepository
									.findByFiletypeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
											objorder.getFiletype(), "R", objorder.getLsuserMaster(),
											objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());

//							lstorder = lslogilablimsorderdetailRepository.findByOrderflagAndFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
//									"R",objorder.getFiletype(),objorder.getLsuserMaster(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
						}
					} else {
						lstorder = lslogilablimsorderdetailRepository
								.findByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
										objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getLsuserMaster(),
										objorder.getFromdate(), objorder.getTodate());

//						lstorder = lslogilablimsorderdetailRepository.findByLsuserMasterAndFiletypeAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
//								objorder.getLsuserMaster(),objorder.getFiletype(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
					}

				}
			} else if (objorder.getOrderflag().equals("M")) {
				if (objorder.getSearchCriteria() != null
						&& objorder.getSearchCriteria().getContentsearch().trim() != "") {
					lstBatchcode = lslogilablimsorderdetailRepository
							.getBatchcodeonFiletypeAndFlagandAssignedtoAndCreatedtimestamp(objorder.getFiletype(),
									objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(),
									objorder.getTodate());

					lstorder = getordersonsamplefileid(lstBatchcode, objorder);
//					lstorder = getordersonsamplefileidlsorder(lstBatchcode, objorder);
				}

				// kumaresan
				else {
					if (objorder.getLsuserMaster() != null && objorder.getLsuserMaster().getLsuserActions() != null) {
						LSuserActions objaction = objorder.getLsuserMaster().getLsuserActions();
						if (objaction.getMyordershowall() != null && objaction.getMyordershowall() == 1) {
							lstorder = lslogilablimsorderdetailRepository
									.findByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
											objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getFromdate(),
											objorder.getTodate());

//							lstorder = lslogilablimsorderdetailRepository.findByAssignedtoAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//									objorder.getLsuserMaster(),objorder.getFiletype(), objorder.getFromdate(), objorder.getTodate());
						} else if (objaction.getMyordershowpending() != null
								&& objaction.getMyordershowpending() == 1) {
							lstorder = lslogilablimsorderdetailRepository
									.findByFiletypeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
											objorder.getFiletype(), "N", objorder.getLsuserMaster(),
											objorder.getFromdate(), objorder.getTodate());

//							lstorder = lslogilablimsorderdetailRepository.findByOrderflagAndFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//									"N",objorder.getFiletype(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
						} else if (objaction.getMyordershowcompleted() != null
								&& objaction.getMyordershowcompleted() == 1) {
							lstorder = lslogilablimsorderdetailRepository
									.findByFiletypeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
											objorder.getFiletype(), "R", objorder.getLsuserMaster(),
											objorder.getFromdate(), objorder.getTodate());

//							lstorder = lslogilablimsorderdetailRepository.findByOrderflagAndFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//									"R",objorder.getFiletype(),objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
						}
					} else {
						lstorder = lslogilablimsorderdetailRepository
								.findByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
										objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getFromdate(),
										objorder.getTodate());

//						lstorder = lslogilablimsorderdetailRepository.findByAssignedtoAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//								objorder.getLsuserMaster(),objorder.getFiletype(), objorder.getFromdate(), objorder.getTodate());
					}
				}

			} else {

				if (objorder.getSearchCriteria() != null
						&& objorder.getSearchCriteria().getContentsearch().trim() != "") {
					lstBatchcode = lslogilablimsorderdetailRepository
							.getBatchcodeonFiletypeAndFlagandProjectandCompletedtime(objorder.getFiletype(),
									objorder.getOrderflag(), lstprojectcode, objorder.getFromdate(),
									objorder.getTodate());

					lstorder = getordersonsamplefileid(lstBatchcode, objorder);
//					lstorder = getordersonsamplefileidlsorder(lstBatchcode, objorder);
				} else {
					lstorder = lslogilablimsorderdetailRepository
							.findByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getFiletype(), objorder.getOrderflag(), lstproject, objorder.getFromdate(),
									objorder.getTodate());

//					lstorder = lslogilablimsorderdetailRepository.findByOrderflagAndFiletypeAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
//							 objorder.getOrderflag(),objorder.getFiletype(),lstproject, objorder.getFromdate(), objorder.getTodate());
				}
			}

			if (objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearch().trim() != "") {

				if (objorder.getOrderflag().equals("N")) {
					pendingcount = lstorder.size();

					completedcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "R", lstproject, objorder.getFromdate(),
									objorder.getTodate());

					Assignedcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
									objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getLsuserMaster(),
									objorder.getFromdate(), objorder.getTodate());
					myordercount = lslogilablimsorderdetailRepository
							.countByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getFromdate(),
									objorder.getTodate());

				} else if (objorder.getOrderflag().equals("A")) {
					pendingcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "N", lstproject, objorder.getFromdate(),
									objorder.getTodate());
					completedcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "R", lstproject, objorder.getFromdate(),
									objorder.getTodate());

//					Assignedcount = lstorder.size();
					myordercount = lslogilablimsorderdetailRepository
							.countByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getFromdate(),
									objorder.getTodate());
				} else if (objorder.getOrderflag().equals("M")) {
					pendingcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "N", lstproject, objorder.getFromdate(),
									objorder.getTodate());
					completedcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "R", lstproject, objorder.getFromdate(),
									objorder.getTodate());

					Assignedcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
									objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getLsuserMaster(),
									objorder.getFromdate(), objorder.getTodate());

					myordercount = lstorder.size();
				} else {
					completedcount = lstorder.size();

					pendingcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "N", lstproject, objorder.getFromdate(),
									objorder.getTodate());
					Assignedcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
									objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getLsuserMaster(),
									objorder.getFromdate(), objorder.getTodate());

					myordercount = lslogilablimsorderdetailRepository
							.countByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getFromdate(),
									objorder.getTodate());
				}
			} else {
				pendingcount = lslogilablimsorderdetailRepository
						.countByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
								objorder.getFiletype(), "N", lstproject, objorder.getFromdate(), objorder.getTodate());

				completedcount = lslogilablimsorderdetailRepository
						.countByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
								objorder.getFiletype(), "R", lstproject, objorder.getFromdate(), objorder.getTodate());

				Assignedcount = lslogilablimsorderdetailRepository
						.countByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
								objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getLsuserMaster(),
								objorder.getFromdate(), objorder.getTodate());

				if (objorder.getOrderflag().equals("A")) {
					Assignedpendingcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "N", objorder.getLsuserMaster(), objorder.getLsuserMaster(),
									objorder.getFromdate(), objorder.getTodate());

					Assignedcompletedcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "R", objorder.getLsuserMaster(), objorder.getLsuserMaster(),
									objorder.getFromdate(), objorder.getTodate());
				} else if (objorder.getOrderflag().equals("M")) {
					// kumaresan
					myorderpendingcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									objorder.getFiletype(), "N", objorder.getLsuserMaster(), objorder.getFromdate(),
									objorder.getTodate());

					myordercompletedcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									objorder.getFiletype(), "R", objorder.getLsuserMaster(), objorder.getFromdate(),
									objorder.getTodate());
				}
				myordercount = lslogilablimsorderdetailRepository
						.countByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getFromdate(),
								objorder.getTodate());
			}

		}
		if (objorder.getObjsilentaudit() != null) {
			objorder.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
			lscfttransactionRepository.save(objorder.getObjsilentaudit());
		}

		long sharedbycount = 0;
		long sharetomecount = 0;

		if (objorder.getLsuserMaster() != null && objorder.getLsuserMaster().getUnifieduserid() != null) {
			sharedbycount = lsordersharedbyRepository
					.countBySharebyunifiedidAndOrdertypeAndSharestatusOrderBySharedbycodeDesc(
							objorder.getLsuserMaster().getUnifieduserid(), objorder.getFiletype(), 1);
			sharetomecount = lsordersharetoRepository
					.countBySharetounifiedidAndOrdertypeAndSharestatusOrderBySharetocodeDesc(
							objorder.getLsuserMaster().getUnifieduserid(), objorder.getFiletype(), 1);
		}

		mapOrders.put("orders", lstorder);
		mapOrders.put("pendingcount", pendingcount);
		mapOrders.put("completedcount", completedcount);
		mapOrders.put("Assignedcount", Assignedcount);
		mapOrders.put("myordercount", myordercount);

		mapOrders.put("Assignedpendingcount", Assignedpendingcount);
		mapOrders.put("Assignedcompletedcount", Assignedcompletedcount);
		mapOrders.put("myorderpendingcount", myorderpendingcount);
		mapOrders.put("myordercompletedcount", myordercompletedcount);

		mapOrders.put("sharedbycount", sharedbycount);
		mapOrders.put("sharetomecount", sharetomecount);

		return lstorder;
	}

	public List<Logilaborders> GetorderbytypeandflaganduserOrdersonly(LSlogilablimsorderdetail objorder,
			Map<String, Object> mapOrders) {

		List<LSprojectmaster> lstproject = objorder.getLstproject();
		List<Logilaborders> lstorder = new ArrayList<Logilaborders>();
		List<Long> lstBatchcode = new ArrayList<Long>();

//		List<LSworkflow> lstworkflow = GetWorkflowonuser(objorder.getLsuserMaster().getLsusergrouptrans());

		List<LSworkflow> lstworkflow = objorder.getLstworkflow();

		long pendingcount = 0;
		long completedcount = 0;
		long Assignedcount = 0;
		long Assignedpendingcount = 0;
		long Assignedcompletedcount = 0;
		long myordercount = 0;
		long myorderpendingcount = 0;
		long myordercompletedcount = 0;

		if (lstproject != null && lstproject.size() > 0) {
			List<Integer> lstprojectcode = lstproject.stream().map(LSprojectmaster::getProjectcode)
					.collect(Collectors.toList());

			if (objorder.getOrderflag().equals("N")) {
				if (objorder.getSearchCriteria() != null
						&& objorder.getSearchCriteria().getContentsearch().trim() != "") {

					lstBatchcode = lslogilablimsorderdetailRepository
							.getBatchcodeonFiletypeAndFlagandProjectandcreateddate(objorder.getFiletype(),
									objorder.getOrderflag(), lstprojectcode, objorder.getFromdate(),
									objorder.getTodate());

					lstorder = getordersonsamplefileidlsorder(lstBatchcode, objorder);

				} else {

					lstorder = lslogilablimsorderdetailRepository
							.findByOrderflagAndFiletypeAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getOrderflag(), objorder.getFiletype(), lstproject, objorder.getFromdate(),
									objorder.getTodate());
				}
			} else if (objorder.getOrderflag().equals("A")) {
				if (objorder.getSearchCriteria() != null
						&& objorder.getSearchCriteria().getContentsearch().trim() != "") {
					lstBatchcode = lslogilablimsorderdetailRepository
							.getBatchcodeonFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestamp(
									objorder.getFiletype(), objorder.getLsuserMaster().getUsercode(),
									objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(),
									objorder.getTodate());

					lstorder = getordersonsamplefileidlsorder(lstBatchcode, objorder);
					Assignedcount = lstorder.size();

					Assignedpendingcount = lstorder.stream()
							.filter(obj -> "N".equals(obj.getOrderflag() != null ? obj.getOrderflag().trim() : ""))
							.count();
					Assignedcompletedcount = lstorder.stream()
							.filter(obj -> "R".equals(obj.getOrderflag() != null ? obj.getOrderflag().trim() : ""))
							.count();

					if (objorder.getLsuserMaster() != null && objorder.getLsuserMaster().getLsuserActions() != null) {
						LSuserActions objaction = objorder.getLsuserMaster().getLsuserActions();
						if (objaction.getAssignedordershowpending() != null && objaction.getAssignedordershowall() != 1
								&& objaction.getAssignedordershowpending() == 1) {
							lstorder = lstorder.stream().filter(
									obj -> "N".equals(obj.getOrderflag() != null ? obj.getOrderflag().trim() : ""))
									.collect(Collectors.toList());
						} else if (objaction.getAssignedordershowcompleted() != null
								&& objaction.getAssignedordershowall() != 1
								&& objaction.getAssignedordershowcompleted() == 1) {
							lstorder = lstorder.stream().filter(
									obj -> "R".equals(obj.getOrderflag() != null ? obj.getOrderflag().trim() : ""))
									.collect(Collectors.toList());
						}
					}

				} else {
					if (objorder.getLsuserMaster() != null && objorder.getLsuserMaster().getLsuserActions() != null) {
						LSuserActions objaction = objorder.getLsuserMaster().getLsuserActions();
						if (objaction.getAssignedordershowall() != null && objaction.getAssignedordershowall() == 1) {

							lstorder = lslogilablimsorderdetailRepository
									.findByLsuserMasterAndFiletypeAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
											objorder.getLsuserMaster(), objorder.getFiletype(),
											objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
						} else if (objaction.getAssignedordershowpending() != null
								&& objaction.getAssignedordershowpending() == 1) {

							lstorder = lslogilablimsorderdetailRepository
									.findByOrderflagAndFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
											"N", objorder.getFiletype(), objorder.getLsuserMaster(),
											objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
						} else if (objaction.getAssignedordershowcompleted() != null
								&& objaction.getAssignedordershowcompleted() == 1) {

							lstorder = lslogilablimsorderdetailRepository
									.findByOrderflagAndFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
											"R", objorder.getFiletype(), objorder.getLsuserMaster(),
											objorder.getLsuserMaster(), objorder.getFromdate(), objorder.getTodate());
						}
					} else {

						lstorder = lslogilablimsorderdetailRepository
								.findByLsuserMasterAndFiletypeAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
										objorder.getLsuserMaster(), objorder.getFiletype(), objorder.getLsuserMaster(),
										objorder.getFromdate(), objorder.getTodate());
					}

				}
			} else if (objorder.getOrderflag().equals("M")) {
				if (objorder.getSearchCriteria() != null
						&& objorder.getSearchCriteria().getContentsearch().trim() != "") {
					lstBatchcode = lslogilablimsorderdetailRepository
							.getBatchcodeonFiletypeAndFlagandAssignedtoAndCreatedtimestamp(objorder.getFiletype(),
									objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(),
									objorder.getTodate());

					lstorder = getordersonsamplefileidlsorder(lstBatchcode, objorder);
				}

				// kumaresan
				else {
					if (objorder.getLsuserMaster() != null && objorder.getLsuserMaster().getLsuserActions() != null) {
						LSuserActions objaction = objorder.getLsuserMaster().getLsuserActions();
						if (objaction.getMyordershowall() != null && objaction.getMyordershowall() == 1) {

							lstorder = lslogilablimsorderdetailRepository
									.findByAssignedtoAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
											objorder.getLsuserMaster(), objorder.getFiletype(), objorder.getFromdate(),
											objorder.getTodate());
						} else if (objaction.getMyordershowpending() != null
								&& objaction.getMyordershowpending() == 1) {

							lstorder = lslogilablimsorderdetailRepository
									.findByOrderflagAndFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
											"N", objorder.getFiletype(), objorder.getLsuserMaster(),
											objorder.getFromdate(), objorder.getTodate());
						} else if (objaction.getMyordershowcompleted() != null
								&& objaction.getMyordershowcompleted() == 1) {

							lstorder = lslogilablimsorderdetailRepository
									.findByOrderflagAndFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
											"R", objorder.getFiletype(), objorder.getLsuserMaster(),
											objorder.getFromdate(), objorder.getTodate());
						}
					} else {

						lstorder = lslogilablimsorderdetailRepository
								.findByAssignedtoAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
										objorder.getLsuserMaster(), objorder.getFiletype(), objorder.getFromdate(),
										objorder.getTodate());
					}
				}

			} else {

				if (objorder.getSearchCriteria() != null
						&& objorder.getSearchCriteria().getContentsearch().trim() != "") {
					lstBatchcode = lslogilablimsorderdetailRepository
							.getBatchcodeonFiletypeAndFlagandProjectandCompletedtime(objorder.getFiletype(),
									objorder.getOrderflag(), lstprojectcode, objorder.getFromdate(),
									objorder.getTodate());

					lstorder = getordersonsamplefileidlsorder(lstBatchcode, objorder);
				} else {

					lstorder = lslogilablimsorderdetailRepository
							.findByOrderflagAndFiletypeAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getOrderflag(), objorder.getFiletype(), lstproject, objorder.getFromdate(),
									objorder.getTodate());
				}
			}

			if (objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearch().trim() != "") {

				if (objorder.getOrderflag().equals("N")) {
					pendingcount = lstorder.size();

					completedcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "R", lstproject, objorder.getFromdate(),
									objorder.getTodate());

					Assignedcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
									objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getLsuserMaster(),
									objorder.getFromdate(), objorder.getTodate());
					myordercount = lslogilablimsorderdetailRepository
							.countByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getFromdate(),
									objorder.getTodate());

				} else if (objorder.getOrderflag().equals("A")) {
					pendingcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "N", lstproject, objorder.getFromdate(),
									objorder.getTodate());
					completedcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "R", lstproject, objorder.getFromdate(),
									objorder.getTodate());

					myordercount = lslogilablimsorderdetailRepository
							.countByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getFromdate(),
									objorder.getTodate());
				} else if (objorder.getOrderflag().equals("M")) {
					pendingcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "N", lstproject, objorder.getFromdate(),
									objorder.getTodate());
					completedcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "R", lstproject, objorder.getFromdate(),
									objorder.getTodate());

					Assignedcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
									objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getLsuserMaster(),
									objorder.getFromdate(), objorder.getTodate());

					myordercount = lstorder.size();
				} else {
					completedcount = lstorder.size();

					pendingcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "N", lstproject, objorder.getFromdate(),
									objorder.getTodate());
					Assignedcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
									objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getLsuserMaster(),
									objorder.getFromdate(), objorder.getTodate());

					myordercount = lslogilablimsorderdetailRepository
							.countByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getFromdate(),
									objorder.getTodate());
				}
			} else {
				pendingcount = lslogilablimsorderdetailRepository
						.countByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
								objorder.getFiletype(), "N", lstproject, objorder.getFromdate(), objorder.getTodate());

				completedcount = lslogilablimsorderdetailRepository
						.countByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
								objorder.getFiletype(), "R", lstproject, objorder.getFromdate(), objorder.getTodate());

				Assignedcount = lslogilablimsorderdetailRepository
						.countByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
								objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getLsuserMaster(),
								objorder.getFromdate(), objorder.getTodate());

				if (objorder.getOrderflag().equals("A")) {
					Assignedpendingcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "N", objorder.getLsuserMaster(), objorder.getLsuserMaster(),
									objorder.getFromdate(), objorder.getTodate());

					Assignedcompletedcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "R", objorder.getLsuserMaster(), objorder.getLsuserMaster(),
									objorder.getFromdate(), objorder.getTodate());
				} else if (objorder.getOrderflag().equals("M")) {
					// kumaresan
					myorderpendingcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									objorder.getFiletype(), "N", objorder.getLsuserMaster(), objorder.getFromdate(),
									objorder.getTodate());

					myordercompletedcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									objorder.getFiletype(), "R", objorder.getLsuserMaster(), objorder.getFromdate(),
									objorder.getTodate());
				}
				myordercount = lslogilablimsorderdetailRepository
						.countByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getFromdate(),
								objorder.getTodate());
			}

		}

		if (objorder.getObjsilentaudit() != null) {
			objorder.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
		}

		long sharedbycount = 0;
		long sharetomecount = 0;

		if (objorder.getLsuserMaster() != null && objorder.getLsuserMaster().getUnifieduserid() != null) {
			sharedbycount = lsordersharedbyRepository
					.countBySharebyunifiedidAndOrdertypeAndSharestatusOrderBySharedbycodeDesc(
							objorder.getLsuserMaster().getUnifieduserid(), objorder.getFiletype(), 1);
			sharetomecount = lsordersharetoRepository
					.countBySharetounifiedidAndOrdertypeAndSharestatusOrderBySharetocodeDesc(
							objorder.getLsuserMaster().getUnifieduserid(), objorder.getFiletype(), 1);
		}

		lstorder.forEach(objorderDetail -> objorderDetail.setLstworkflow(lstworkflow));

		mapOrders.put("orders", lstorder);
		mapOrders.put("pendingcount", pendingcount);
		mapOrders.put("completedcount", completedcount);
		mapOrders.put("Assignedcount", Assignedcount);
		mapOrders.put("myordercount", myordercount);

		mapOrders.put("Assignedpendingcount", Assignedpendingcount);
		mapOrders.put("Assignedcompletedcount", Assignedcompletedcount);
		mapOrders.put("myorderpendingcount", myorderpendingcount);
		mapOrders.put("myordercompletedcount", myordercompletedcount);

		mapOrders.put("sharedbycount", sharedbycount);
		mapOrders.put("sharetomecount", sharetomecount);

		return lstorder;
	}

	public List<LSlogilablimsorderdetail> getordersonsamplefileid(List<Long> lstBatchcode,
			LSlogilablimsorderdetail objorder) {
		List<LSsamplefile> idList = new ArrayList<LSsamplefile>();
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		List<Integer> lstsamplefilecode = new ArrayList<Integer>();

		if (lstBatchcode != null && lstBatchcode.size() > 0) {
			lstsamplefilecode = lssamplefileRepository.getFilesamplecodeByBatchcodeIn(lstBatchcode);
		}

		if (lstsamplefilecode != null && lstsamplefilecode.size() > 0) {
			idList = getsamplefileIds(lstsamplefilecode, objorder.getSearchCriteria(), objorder.getIsmultitenant());
			if (idList != null && idList.size() > 0) {
				lstorder = lslogilablimsorderdetailRepository
						.findByFiletypeAndLssamplefileInOrderByBatchcodeDesc(objorder.getFiletype(), idList);
			}
		}

		return lstorder;
	}

	public List<Logilaborders> getordersonsamplefileidlsorder(List<Long> lstBatchcode,
			LSlogilablimsorderdetail objorder) {
		List<LSsamplefile> idList = new ArrayList<LSsamplefile>();
		List<Logilaborders> lstorder = new ArrayList<Logilaborders>();
		List<Integer> lstsamplefilecode = new ArrayList<Integer>();

		if (lstBatchcode != null && lstBatchcode.size() > 0) {
			lstsamplefilecode = lssamplefileRepository.getFilesamplecodeByBatchcodeIn(lstBatchcode);
		}

		if (lstsamplefilecode != null && lstsamplefilecode.size() > 0) {
			idList = getsamplefileIds(lstsamplefilecode, objorder.getSearchCriteria(), objorder.getIsmultitenant());
			if (idList != null && idList.size() > 0) {
				lstorder = lslogilablimsorderdetailRepository
						.findByLssamplefileInAndFiletypeOrderByBatchcodeDesc(idList, objorder.getFiletype());
			}
		}

		return lstorder;
	}

	public List<LSlogilablimsorderdetail> Getorderbytypeandflaglazy(LSlogilablimsorderdetail objorder,
			Map<String, Object> mapOrders) {
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		if (objorder.getOrderflag().equals("N")) {
			lstorder = lslogilablimsorderdetailRepository
					.findFirst10ByFiletypeAndOrderflagAndBatchcodeLessThanAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(),
							objorder.getFromdate(), objorder.getTodate());
		} else {
			lstorder = lslogilablimsorderdetailRepository
					.findFirst10ByFiletypeAndOrderflagAndBatchcodeLessThanAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(),
							objorder.getFromdate(), objorder.getTodate());
		}

		mapOrders.put("orders", lstorder);

		return lstorder;
	}

	public List<LSlogilablimsorderdetail> Getorderbytypeandflaganduserlazy(LSlogilablimsorderdetail objorder,
			Map<String, Object> mapOrders) {
		List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objorder.getLsuserMaster());
		List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingIn(lstteammap);
		List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();

		if (objorder.getOrderflag().equals("N")) {
			lstorder = lslogilablimsorderdetailRepository
					.findByFiletypeAndOrderflagAndBatchcodeLessThanAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(), lstproject,
							objorder.getFromdate(), objorder.getTodate());
		} else {
			lstorder = lslogilablimsorderdetailRepository
					.findByFiletypeAndOrderflagAndBatchcodeLessThanAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(), lstproject,
							objorder.getFromdate(), objorder.getTodate());
		}

		mapOrders.put("orders", lstorder);

		return lstorder;
	}

	public List<LSlogilablimsorderdetail> Getorderallbytypeandflaglazy(LSlogilablimsorderdetail objorder,
			Map<String, Object> mapOrders) {
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		if (objorder.getOrderflag().equals("N")) {
			lstorder = lslogilablimsorderdetailRepository
					.findByFiletypeAndOrderflagAndBatchcodeLessThanAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(),
							objorder.getFromdate(), objorder.getTodate());
		} else {
			lstorder = lslogilablimsorderdetailRepository
					.findByFiletypeAndOrderflagAndBatchcodeLessThanAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(),
							objorder.getFromdate(), objorder.getTodate());
		}

		mapOrders.put("orders", lstorder);

		return lstorder;
	}

	public List<LSlogilablimsorderdetail> Getorderallbytypeandflaganduserlazy(LSlogilablimsorderdetail objorder,
			Map<String, Object> mapOrders) {
		List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objorder.getLsuserMaster());
		List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingIn(lstteammap);
		List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();

		if (objorder.getOrderflag().equals("N")) {
			lstorder = lslogilablimsorderdetailRepository
					.findFirst10ByFiletypeAndOrderflagAndBatchcodeLessThanAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(), lstproject,
							objorder.getFromdate(), objorder.getTodate());
		} else {
			lstorder = lslogilablimsorderdetailRepository
					.findFirst10ByFiletypeAndOrderflagAndBatchcodeLessThanAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(), lstproject,
							objorder.getFromdate(), objorder.getTodate());
		}

		mapOrders.put("orders", lstorder);

		return lstorder;
	}

	public List<LSworkflow> GetWorkflowonUser(LSuserMaster objuser) {
		List<LSworkflowgroupmapping> lsworkflowgroupmapping = lsworkflowgroupmappingRepository
				.findBylsusergroup(objuser.getLsusergroup());
		List<LSworkflow> lsworkflow = lsworkflowRepository
				.findByLsworkflowgroupmappingInOrderByWorkflowcodeDesc(lsworkflowgroupmapping);

		return lsworkflow;
	}

	public Map<String, Object> GetWorkflowanduseronUsercode(LSuserMaster usercode) {
		LSuserMaster objuser = lsuserMasterRepository.findByusercode(usercode.getUsercode());
//		LSMultiusergroup objLSMultiusergroup = new LSMultiusergroup();
//		objLSMultiusergroup = LSMultiusergroupRepositery.findBymultiusergroupcode(usercode.getMultiusergroups());
//		objuser.setLsusergroup(objLSMultiusergroup.getLsusergroup());

		LSusergroup lsusergroup = usercode.getLsusergrouptrans();

		Map<String, Object> maplogindetails = new HashMap<String, Object>();
		maplogindetails.put("workflow", GetWorkflowonuser(lsusergroup));
		maplogindetails.put("user", objuser);
		return maplogindetails;
	}

	public List<LSworkflow> GetWorkflowonuser(LSusergroup lsusergroup) {
		List<LSworkflowgroupmapping> lsworkflowgroupmapping = lsworkflowgroupmappingRepository
				.findBylsusergroup(lsusergroup);

		List<LSworkflow> lsworkflow = lsworkflowRepository
				.findByLsworkflowgroupmappingInOrderByWorkflowcodeDesc(lsworkflowgroupmapping);

		return lsworkflow;
	}

	public LSlogilablimsorderdetail GetorderStatus(LSlogilablimsorderdetail objorder) {
		if (objorder.getObjsilentaudit() != null) {
			objorder.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
//			lscfttransactionRepository.save(objorder.getObjsilentaudit());
		}

		LSlogilablimsorderdetail objupdatedorder = lslogilablimsorderdetailRepository.findOne(objorder.getBatchcode());

		if (objupdatedorder.getLockeduser() != null) {
			objupdatedorder.setIsLock(1);
		} else {
			objupdatedorder.setIsLock(0);
		}

		if (objupdatedorder.getLockeduser() != null && objorder.getObjLoggeduser() != null
				&& objupdatedorder.getLockeduser().equals(objorder.getObjLoggeduser().getUsercode())) {
			objupdatedorder.setIsLockbycurrentuser(1);
		} else {
			objupdatedorder.setIsLockbycurrentuser(0);
		}

		if (objupdatedorder.getFiletype() != 0 && objupdatedorder.getOrderflag().toString().trim().equals("N")) {
			if (objupdatedorder.getLsworkflow().equals(lsworkflowRepository
					.findTopByAndLssitemasterOrderByWorkflowcodeDesc(objorder.getObjLoggeduser().getLssitemaster()))) {
				objupdatedorder.setIsFinalStep(1);
			} else {
				objupdatedorder.setIsFinalStep(0);
			}
		}

		if (objupdatedorder.getFiletype() == 0) {
			objupdatedorder
					.setLstestparameter(lStestparameterRepository.findByntestcode(objupdatedorder.getTestcode()));
		}

		if (objupdatedorder.getLssamplefile() != null) {
			if (objorder.getIsmultitenant() == 1) {
				CloudOrderCreation file = cloudOrderCreationRepository
						.findById((long) objupdatedorder.getLssamplefile().getFilesamplecode());
				if (file != null) {
					objupdatedorder.getLssamplefile().setFilecontent(file.getContent());
				}
			} else {
				OrderCreation file = mongoTemplate.findById(objupdatedorder.getLssamplefile().getFilesamplecode(),
						OrderCreation.class);
				if (file != null) {
					objupdatedorder.getLssamplefile().setFilecontent(file.getContent());
				}
			}
		}

		return objupdatedorder;
	}

	private String GetSamplefileconent(LSsamplefile lssamplefile, Integer ismultitenant) {
		String content = "";

		if (lssamplefile != null) {
			if (ismultitenant == 1) {
				CloudOrderCreation file = cloudOrderCreationRepository
						.findById((long) lssamplefile.getFilesamplecode());
				if (file != null) {
					content = file.getContent();
				}
			} else {
				OrderCreation file = mongoTemplate.findById(lssamplefile.getFilesamplecode(), OrderCreation.class);
				if (file != null) {
					content = file.getContent();
				}
			}
		}

		return content;
	}

	public LSlogilablimsorderdetail GetdetailorderStatus(LSlogilablimsorderdetail objupdatedorder) {

		objupdatedorder.setLsLSlimsorder(lSlimsorderRepository.findBybatchid(objupdatedorder.getBatchid()));

		LSsamplefile LSsamplefile = objupdatedorder.getLssamplefile();
		if (LSsamplefile != null) {
			if (LSsamplefile.getFilesamplecode() != null) {
				List<LSsamplefileversion> LSsamplefileversion = lssamplefileversionRepository
						.findByFilesamplecodeOrderByVersionnoDesc(LSsamplefile);
				objupdatedorder.getLssamplefile().setLssamplefileversion(LSsamplefileversion);
			}
		}

		if (objupdatedorder.getFiletype() != 0 && objupdatedorder.getOrderflag().toString().trim().equals("N")) {
			if (objupdatedorder.getLsworkflow()
					.equals(lsworkflowRepository.findTopByAndLssitemasterOrderByWorkflowcodeDesc(
							objupdatedorder.getObjLoggeduser().getLssitemaster()))) {
				objupdatedorder.setIsFinalStep(1);
			} else {
				objupdatedorder.setIsFinalStep(0);
			}
		}

		if (objupdatedorder.getFiletype() == 0) {
			objupdatedorder
					.setLstestparameter(lStestparameterRepository.findByntestcode(objupdatedorder.getTestcode()));
		}

		return objupdatedorder;
	}

	public List<LSresultdetails> GetResults(LSlogilablimsorderdetail objorder) {
		List<LSresultdetails> lsResults = new ArrayList<LSresultdetails>();
		List<LSlimsorder> lsLogilaborders = lslimsorderRepository.findBybatchid(objorder.getBatchid());
		List<String> lsorderno = new ArrayList<String>();

		if (lsLogilaborders != null && lsLogilaborders.size() > 0) {
			int i = 0;

			while (lsLogilaborders.size() > i) {
				lsorderno.add(lsLogilaborders.get(i).getOrderid().toString());
				i++;
			}
		}
		lsResults = lsresultdetailsRepository.findBylimsreferencecodeIn(lsorderno);

		return lsResults;
	}

	public void updateorderversioncontent(String Content, LSsamplefileversion objfile, Integer ismultitenant) {
		if (ismultitenant == 1) {
			CloudOrderVersion objsavefile = new CloudOrderVersion();
			objsavefile.setId((long) objfile.getFilesamplecodeversion());
			objsavefile.setContent(Content);

			cloudOrderVersionRepository.save(objsavefile);
		} else {
			OrderVersion objsavefile = new OrderVersion();
			objsavefile.setId((long) objfile.getFilesamplecodeversion());
			objsavefile.setContent(Content);

			// build query
			Query query = new Query(Criteria.where("id").is(objsavefile.getId()));

			Boolean recordcount = mongoTemplate.exists(query, OrderVersion.class);

			if (!recordcount) {
				mongoTemplate.insert(objsavefile);
			} else {
				Update update = new Update();
				update.set("content", Content);

				mongoTemplate.upsert(query, update, OrderVersion.class);
			}
		}
		// objfile.setFilecontent(Content);
	}

	public LSsamplefile SaveResultfile(LSsamplefile objfile) {

		Integer lastversionindex = objfile.getVersionno() != null ? objfile.getVersionno() - 1 : 0;

		boolean versionexist = true;
		if (objfile.getLssamplefileversion().size() <= 0) {
			versionexist = false;
			lastversionindex = 0;
			LSsamplefileversion lsversion = new LSsamplefileversion();
			lsversion.setVersionname("1");
			lsversion.setVersionno(1);
			lsversion.setBatchcode(objfile.getBatchcode());
			lsversion.setTestid(objfile.getTestid());
			lsversion.setCreatedate(objfile.getCreatedate());
			lsversion.setCreateby(objfile.getCreateby());
			lsversion.setModifieddate(objfile.getModifieddate());
			lsversion.setModifiedby(objfile.getModifiedby());
			objfile.getLssamplefileversion().add(lsversion);

			lssamplefileversionRepository.save(objfile.getLssamplefileversion());

		}

		if (objfile.isDoversion() && versionexist) {

			Integer perviousversion = -1;
			if (objfile.getVersionno() != null && objfile.getVersionno() >= 2) {
				perviousversion = objfile.getVersionno() - 2;
			}

//			if (lastversionindex == -1) {
//				Contentversion = objfile.getFilecontent();
//				lastversionindex = 0;
//				lssamplefileversionRepository.save(objfile.getLssamplefileversion());
//			} else {
//			
			String Contentversion = GetSamplefileconent(objfile, objfile.getIsmultitenant());
			objfile.getLssamplefileversion().get(lastversionindex).setFilecontent(null);
			lssamplefileversionRepository.save(objfile.getLssamplefileversion());
			updateorderversioncontent(objfile.getFilecontent(), objfile.getLssamplefileversion().get(lastversionindex),
					objfile.getIsmultitenant());
			if (perviousversion > -1) {
				updateorderversioncontent(Contentversion, objfile.getLssamplefileversion().get(perviousversion),
						objfile.getIsmultitenant());
			}

			// objfile.setVersionno(objfile.getVersionno()+1);
//			}

//			if (objfile.getLssamplefileversion() != null ) {
//				if (objfile.getObjActivity().getObjsilentaudit() != null) {
//					String versionname = "";
//					if (objfile.getLssamplefileversion().size() > 0) {
//						versionname = objfile.getLssamplefileversion().get(lastversionindex).getVersionname();
//					} else {
//						versionname = "version_1";
//					}
//
//					objfile.getObjActivity().getObjsilentaudit()
//							.setComments("Order ID: ELN" + objfile.getFilesamplecode() + " " + " was versioned to" + " "
//									+ versionname + " " + "by the user" + " " + objfile.getModifiedby().getUsername());
//					objfile.getObjActivity().getObjsilentaudit().setTableName("LSfile");
//					lscfttransactionRepository.save(objfile.getObjActivity().getObjsilentaudit());
//				}
//			}
		} else {
			updateorderversioncontent(objfile.getFilecontent(), objfile.getLssamplefileversion().get(lastversionindex),
					objfile.getIsmultitenant());
		}

		objfile.setProcessed(1);

		String Content = objfile.getFilecontent();
		objfile.setFilecontent(null);
		lssamplefileRepository.save(objfile);
		updateordercontent(Content, objfile, objfile.getIsmultitenant());

		objfile.setFilecontent(Content);

		if (objfile.getObjActivity() != null) {
			lsactivityRepository.save(objfile.getObjActivity());
		}

		objfile.setResponse(new Response());
		objfile.getResponse().setStatus(true);
		objfile.getResponse().setInformation("ID_DUMMY1");
		return objfile;
	}

	public void updateordercontent(String Content, LSsamplefile objfile, Integer ismultitenant) {
		if (ismultitenant == 1) {
			CloudOrderCreation objsavefile = new CloudOrderCreation();
			objsavefile.setId((long) objfile.getFilesamplecode());
			objsavefile.setContent(Content);
			objsavefile.setContentvalues(objfile.getContentvalues());
			objsavefile.setContentparameter(objfile.getContentparameter());

			cloudOrderCreationRepository.save(objsavefile);
		} else {
			OrderCreation objsavefile = new OrderCreation();
			objsavefile.setId((long) objfile.getFilesamplecode());
			objsavefile.setContent(Content);
			objsavefile.setContentvalues(objfile.getContentvalues());
			objsavefile.setContentparameter(objfile.getContentparameter());

			// build query
			Query query = new Query(Criteria.where("id").is(objsavefile.getId()));

			Boolean recordcount = mongoTemplate.exists(query, OrderCreation.class);

			if (!recordcount) {
				mongoTemplate.insert(objsavefile);
			} else {
				Update update = new Update();
				update.set("content", Content);
				update.set("contentvalues", objfile.getContentvalues());
				update.set("contentparameter", objfile.getContentparameter());

				mongoTemplate.upsert(query, update, OrderCreation.class);
			}
		}
		// objfile.setFilecontent(Content);
	}

	public LSlogilablimsorderdetail UpdateLimsOrder(LSlogilablimsorderdetail objorder) {
		List<LSlimsorder> lstorder = lslimsorderRepository.findBybatchid(objorder.getBatchid());
		lstorder.forEach((orders) -> orders.setMethodcode(objorder.getMethodcode()));
		lstorder.forEach((orders) -> orders.setInstrumentcode(objorder.getInstrumentcode()));
		objorder.getLssamplefile().setBatchcode(objorder.getBatchcode());
		lslimsorderRepository.save(lstorder);
		String Content = objorder.getLssamplefile().getFilecontent();
		objorder.getLssamplefile().setFilecontent(null);
		lssamplefileRepository.save(objorder.getLssamplefile());

		lslogilablimsorderdetailRepository.save(objorder);

		if (objorder.getObjsilentaudit() != null) {
			lscfttransactionRepository.save(objorder.getObjsilentaudit());
		}
		// manual audit
		if (objorder.getObjuser() != null) {
			objorder.getObjmanualaudit().setComments(objorder.getObjuser().getComments());
			objorder.getObjmanualaudit().setTableName("LSlogilablimsorderdetail");
			lscfttransactionRepository.save(objorder.getObjmanualaudit());
		}

		if (objorder.getLssamplefile() != null) {
			updateordercontent(Content, objorder.getLssamplefile(), objorder.getIsmultitenant());
			objorder.getLssamplefile().setFilecontent(Content);
		}

		return objorder;
	}

	public LSlogilablimsorderdetail Getupdatedorder(LSlogilablimsorderdetail objorder) {
		LSlogilablimsorderdetail objupdated = lslogilablimsorderdetailRepository.findOne(objorder.getBatchcode());
		objupdated.setLsLSlimsorder(lSlimsorderRepository.findBybatchid(objorder.getBatchid()));

		if (objorder.getFiletype() == 0) {
			objupdated.setLstestparameter(lStestparameterRepository.findByntestcode(objupdated.getTestcode()));
		}

		if (objorder.getLssamplefile() != null) {
			if (objorder.getIsmultitenant() == 1) {
				CloudOrderCreation file = cloudOrderCreationRepository
						.findById((long) objorder.getLssamplefile().getFilesamplecode());
				if (file != null) {
					objupdated.getLssamplefile().setFilecontent(file.getContent());
				}
			} else {
				OrderCreation file = mongoTemplate.findById(objorder.getLssamplefile().getFilesamplecode(),
						OrderCreation.class);
				if (file != null) {
					objupdated.getLssamplefile().setFilecontent(file.getContent());
				}
			}
		}

		return objupdated;
	}

	public Map<String, Object> Getorderforlink(LSlogilablimsorderdetail objorder) {
		Map<String, Object> mapOrder = new HashMap<String, Object>();
		LSlogilablimsorderdetail objupdated = lslogilablimsorderdetailRepository.findOne(objorder.getBatchcode());

		if (objupdated.getLockeduser() != null) {
			objupdated.setIsLock(1);
		} else {
			objupdated.setIsLock(0);
		}

		if (objupdated.getLockeduser() != null && objorder.getObjLoggeduser() != null
				&& objupdated.getLockeduser().equals(objorder.getObjLoggeduser().getUsercode())) {
			objupdated.setIsLockbycurrentuser(1);
		} else {
			objupdated.setIsLockbycurrentuser(0);
		}

		if (objupdated.getFiletype() != 0 && objupdated.getOrderflag().toString().trim().equals("N")) {
			if (objupdated.getLsworkflow().equals(lsworkflowRepository
					.findTopByAndLssitemasterOrderByWorkflowcodeDesc(objorder.getObjLoggeduser().getLssitemaster()))) {
				objupdated.setIsFinalStep(1);
			} else {
				objupdated.setIsFinalStep(0);
			}
		}

		if (objupdated.getFiletype() == 0) {
			objupdated.setLstestparameter(lStestparameterRepository.findByntestcode(objupdated.getTestcode()));
		}

		if (objupdated.getLssamplefile() != null) {
			if (objorder.getIsmultitenant() == 1) {
				CloudOrderCreation file = cloudOrderCreationRepository
						.findById((long) objupdated.getLssamplefile().getFilesamplecode());
				if (file != null) {
					objupdated.getLssamplefile().setFilecontent(file.getContent());
				}
			} else {
				OrderCreation file = mongoTemplate.findById(objupdated.getLssamplefile().getFilesamplecode(),
						OrderCreation.class);
				if (file != null) {
					objupdated.getLssamplefile().setFilecontent(file.getContent());
				}
			}
		}
		mapOrder.put("order", objupdated);
		mapOrder.put("version",
				lssamplefileversionRepository.findByFilesamplecodeOrderByVersionnoDesc(objupdated.getLssamplefile()));

		return mapOrder;
	}

	/*
	 * public Map<String, Object> GetLimsOrder(LSlimsorder clsOrder) {
	 * 
	 * Map<String, Object> mapOrder = new HashMap<String, Object>();
	 * 
	 * List<LSlimsorder> pendingOrder=lslimsorderRepository.findByorderflag("N");
	 * List<LSlimsorder> completOrder=lslimsorderRepository.findByorderflag("R");
	 * 
	 * mapOrder.put("PendingOrder", pendingOrder); mapOrder.put("CompletedOrder",
	 * completOrder);
	 * 
	 * return mapOrder; }
	 */

	public LSlogilablimsorderdetail CompleteOrder(LSlogilablimsorderdetail objorder) {
		if (objorder.getLssamplefile().getLssamplefileversion() != null) {
			lssamplefileversionRepository.save(objorder.getLssamplefile().getLssamplefileversion());
		}
		lssampleresultRepository.save(objorder.getLssamplefile().getLssampleresult());
		String Content = objorder.getLssamplefile().getFilecontent();
		objorder.getLssamplefile().setFilecontent(null);
		lssamplefileRepository.save(objorder.getLssamplefile());
		objorder.getLsparsedparameters().forEach((param) -> param.setBatchcode(objorder.getBatchcode()));
		lsparsedparametersRespository.save(objorder.getLsparsedparameters());
		lsorderworkflowhistoryRepositroy.save(objorder.getLsorderworkflowhistory());
		List<LsOrderattachments> lstattach = lsOrderattachmentsRepository
				.findByBatchcodeOrderByAttachmentcodeDesc(objorder.getBatchcode());
		objorder.setLsOrderattachments(lstattach);
		lslogilablimsorderdetailRepository.save(objorder);

		if (objorder.getLssamplefile() != null) {
			updateordercontent(Content, objorder.getLssamplefile(), objorder.getIsmultitenant());
			objorder.getLssamplefile().setFilecontent(Content);
		}
		// silent audit
		if (objorder.getObjsilentaudit() != null) {

			objorder.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
//			lscfttransactionRepository.save(objorder.getObjsilentaudit());
		}
		// manual audit
		if (objorder.getObjuser() != null) {
			objorder.getObjmanualaudit().setComments(objorder.getObjuser().getComments());
			objorder.getObjmanualaudit().setTableName("LSlogilablimsorderdetail");
//			lscfttransactionRepository.save(objorder.getObjmanualaudit());
		}
		updatenotificationfororder(objorder);
		objorder.setResponse(new Response());
		objorder.getResponse().setStatus(true);
		objorder.getResponse().setInformation("ID_ORDERCMPLT");
		return objorder;
	}

	public LSlogilablimsorderdetail updateworflowforOrder(LSlogilablimsorderdetail objorder) {

		LSlogilablimsorderdetail lsOrder = lslogilablimsorderdetailRepository.findOne(objorder.getBatchcode());

		updatenotificationfororderworkflow(objorder, lsOrder.getLsworkflow());

		lsorderworkflowhistoryRepositroy.save(objorder.getLsorderworkflowhistory());

		lslogilablimsorderdetailRepository.save(objorder);

//		silent audit
//		if (objorder.getLsorderworkflowhistory().get(objorder.getLsorderworkflowhistory().size() - 1)
//				.getObjsilentaudit() != null) {
//			objorder.getLsorderworkflowhistory().get(objorder.getLsorderworkflowhistory().size() - 1)
//					.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
//			lscfttransactionRepository.save(objorder.getLsorderworkflowhistory()
//					.get(objorder.getLsorderworkflowhistory().size() - 1).getObjsilentaudit());
//		}
		return objorder;
	}

	public boolean Updatesamplefileversion(LSsamplefile objfile) {
		int Versionnumber = 0;
		LSsamplefileversion objLatestversion = lssamplefileversionRepository
				.findFirstByFilesamplecodeOrderByVersionnoDesc(objfile);
		if (objLatestversion != null) {
			Versionnumber = objLatestversion.getVersionno();
		}

		Versionnumber++;

		LSsamplefile objesixting = lssamplefileRepository.findByfilesamplecode(objfile.getFilesamplecode());
		if (objesixting == null) {
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
		if (objfile.getObjActivity().getObjsilentaudit() != null) {
			// objpwd.getObjsilentaudit().setModuleName("UserManagement");
			objfile.getObjActivity().getObjsilentaudit()
					.setComments("Sheet" + " " + objfile.getFilesamplecode() + " " + " was versioned to version_"
							+ Versionnumber + " " + "by the user" + " " + objfile.getLsuserMaster().getUsername());
			// objpwd.getObjsilentaudit().setActions("view");
			// objpwd.getObjsilentaudit().setSystemcoments("System Generated");
			objfile.getObjActivity().getObjsilentaudit().setTableName("LSfile");
			lscfttransactionRepository.save(objfile.getObjActivity().getObjsilentaudit());
		}

		return true;
	}

	public List<LSsamplefileversion> Getfileversions(LSsamplefile objfile) {
		return lssamplefileversionRepository.findByFilesamplecodeOrderByVersionnoDesc(objfile);
	}

	public String GetfileverContent(LSsamplefile objfile) {
		String Content = "";

		if (objfile.getVersionno() == null || objfile.getVersionno() == 0) {
			LSsamplefile objesixting = lssamplefileRepository.findByfilesamplecode(objfile.getFilesamplecode());
			Content = objesixting.getFilecontent();
			if (objfile != null) {
				if (objfile.getIsmultitenant() == 1) {
					CloudOrderCreation file = cloudOrderCreationRepository.findById((long) objfile.getFilesamplecode());
					if (file != null) {
						Content = file.getContent();
					}
				} else {
					OrderCreation file = mongoTemplate.findById(objfile.getFilesamplecode(), OrderCreation.class);
					if (file != null) {
						Content = file.getContent();
					}
				}
			}
		} else {
			LSsamplefileversion objVersion = lssamplefileversionRepository
					.findByFilesamplecodeAndVersionnoOrderByVersionnoDesc(objfile, objfile.getVersionno());
			Content = objVersion.getFilecontent();

			if (objVersion != null) {
				if (objfile.getIsmultitenant() == 1) {
					CloudOrderVersion file = cloudOrderVersionRepository
							.findById((long) objVersion.getFilesamplecodeversion());
					if (file != null) {
						Content = file.getContent();
					}
				} else {
					OrderVersion file = mongoTemplate.findById(objVersion.getFilesamplecodeversion(),
							OrderVersion.class);
					if (file != null) {
						Content = file.getContent();
					}
				}
			}
		}

		return Content;
	}

	public List<LSlogilablimsorderdetail> Getorderbyfile(LSlogilablimsorderdetail objorder) {
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();

		if (objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
			lstorder = lslogilablimsorderdetailRepository
					.findByFiletypeAndLsfileOrderByBatchcodeDesc(objorder.getFiletype(), objorder.getLsfile());
		} else {
			List<LSuserteammapping> lstteammap = lsuserteammappingRepository
					.findBylsuserMaster(objorder.getLsuserMaster());
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingIn(lstteammap);
			List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
			lstorder = lslogilablimsorderdetailRepository
					.findByFiletypeAndLsfileAndLsprojectmasterInOrderByBatchcodeDesc(objorder.getFiletype(),
							objorder.getLsfile(), lstproject);
		}

		return lstorder;
	}

	public List<LSlogilablimsorderdetail> Getexcelorder(LSlogilablimsorderdetail objorder) {
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		if (objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
			lstorder = lslogilablimsorderdetailRepository.findByFiletypeOrderByBatchcodeDesc(objorder.getFiletype());
		} else {
			List<LSuserteammapping> lstteammap = lsuserteammappingRepository
					.findBylsuserMaster(objorder.getLsuserMaster());
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingIn(lstteammap);
			List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
			lstorder = lslogilablimsorderdetailRepository
					.findByFiletypeAndLsprojectmasterInOrderByBatchcodeDesc(objorder.getFiletype(), lstproject);
		}
		return lstorder;
	}

	public LSlogilablimsorderdetail updateVersionandWorkflowhistory(LSlogilablimsorderdetail objorder) {
		objorder = lslogilablimsorderdetailRepository.findOne(objorder.getBatchcode());
		objorder.setObjsilentaudit(new LScfttransaction());
		objorder.setObjmanualaudit(new LScfttransaction());
		return objorder;
	}

	public LSsamplefile GetResultfileverContent(LSsamplefile objfile) {

		LSsamplefile objesixting = lssamplefileRepository.findByfilesamplecode(objfile.getFilesamplecode());
		if (objesixting != null) {
			if (objfile.getIsmultitenant() == 1) {
				CloudOrderCreation file = cloudOrderCreationRepository.findById((long) objfile.getFilesamplecode());
				if (file != null) {
					objesixting.setFilecontent(file.getContent());
				}
			} else {
				OrderCreation file = mongoTemplate.findById(objesixting.getFilesamplecode(), OrderCreation.class);
				if (file != null) {
					objesixting.setFilecontent(file.getContent());
				}
			}
		}
		return objesixting;
	}

	public LSlogilablimsorderdetail Uploadattachments(MultipartFile file, Long batchcode, String filename,
			String fileexe, Integer usercode, Date currentdate, Integer islargefile) throws IOException {

		LSlogilablimsorderdetail objorder = lslogilablimsorderdetailRepository.findOne(batchcode);

		LsOrderattachments objattachment = new LsOrderattachments();

		if (islargefile == 0) {
			OrderAttachment objfile = fileManipulationservice.storeattachment(file);
			if (objfile != null) {
				objattachment.setFileid(objfile.getId());
			}
		} else {
			String id = fileManipulationservice.storeLargeattachment(filename, file);
			if (id != null) {
				objattachment.setFileid(id);
			}
		}

		objattachment.setFilename(filename);
		objattachment.setFileextension(fileexe);
		objattachment.setCreateby(lsuserMasterRepository.findByusercode(usercode));
		objattachment.setCreatedate(currentdate);
		objattachment.setBatchcode(objorder.getBatchcode());
		objattachment.setIslargefile(islargefile);

		LSuserMaster username = lsuserMasterRepository.findByusercode(usercode);
		String name = username.getUsername();
		LScfttransaction list = new LScfttransaction();
		list.setModuleName("Register Task Orders & Execute");
		list.setComments(
				name + " " + "Uploaded the attachement in Order ID: " + objorder.getBatchid() + " " + "successfully");
		list.setActions("Insert");
		list.setSystemcoments("System Generated");
		list.setTableName("profile");
		list.setTransactiondate(currentdate);
		list.setLsuserMaster(usercode);
		lscfttransactionRepository.save(list);
		if (objorder != null && objorder.getLsOrderattachments() != null) {
			objorder.getLsOrderattachments().add(objattachment);
		} else {
			objorder.setLsOrderattachments(new ArrayList<LsOrderattachments>());
			objorder.getLsOrderattachments().add(objattachment);
		}

		lsOrderattachmentsRepository.save(objorder.getLsOrderattachments());

		return objorder;
	}

	public LSlogilablimsorderdetail CloudUploadattachments(MultipartFile file, Long batchcode, String filename,
			String fileexe, Integer usercode, Date currentdate, Integer islargefile) throws IOException {

		LSlogilablimsorderdetail objorder = lslogilablimsorderdetailRepository.findOne(batchcode);

		LsOrderattachments objattachment = new LsOrderattachments();

		if (islargefile == 0) {
			CloudOrderAttachment objfile = cloudFileManipulationservice.storeattachment(file);
			if (objfile != null) {
				objattachment.setFileid(objfile.getFileid());
			}
		}

		objattachment.setFilename(filename);
		objattachment.setFileextension(fileexe);
		objattachment.setCreateby(lsuserMasterRepository.findByusercode(usercode));
		objattachment.setCreatedate(currentdate);
		objattachment.setBatchcode(objorder.getBatchcode());
		objattachment.setIslargefile(islargefile);

		LSuserMaster username = lsuserMasterRepository.findByusercode(usercode);
		String name = username.getUsername();
		LScfttransaction list = new LScfttransaction();
		list.setModuleName("Register Task Orders & Execute");
		list.setComments(
				name + " " + "Uploaded the attachement in Order ID: " + objorder.getBatchid() + " " + "successfully");
		list.setActions("Insert");
		list.setSystemcoments("System Generated");
		list.setTableName("profile");
		list.setTransactiondate(currentdate);
		list.setLsuserMaster(usercode);
		lscfttransactionRepository.save(list);
		if (objorder != null && objorder.getLsOrderattachments() != null) {
			objorder.getLsOrderattachments().add(objattachment);
		} else {
			objorder.setLsOrderattachments(new ArrayList<LsOrderattachments>());
			objorder.getLsOrderattachments().add(objattachment);
		}

		lsOrderattachmentsRepository.save(objorder.getLsOrderattachments());

		if (islargefile == 1) {
			String filenameval = "attach_"
					+ objorder.getBatchcode() + "_" + objorder.getLsOrderattachments()
							.get(objorder.getLsOrderattachments().lastIndexOf(objattachment)).getAttachmentcode()
					+ "_" + filename;
			String id = cloudFileManipulationservice.storeLargeattachment(filenameval, file);
			if (id != null) {
				objattachment.setFileid(id);
			}

			lsOrderattachmentsRepository.save(objorder.getLsOrderattachments());
		}

		return objorder;
	}

	public LsOrderattachments downloadattachments(LsOrderattachments objattachments) {
		OrderAttachment objfile = fileManipulationservice.retrieveFile(objattachments);
		if (objfile != null) {
			objattachments.setFile(objfile.getFile());

		}
//		silent audit
		if (objattachments.getObjsilentaudit() != null) {
			objattachments.getObjsilentaudit().setTableName("LsOrderattachments");
			lscfttransactionRepository.save(objattachments.getObjsilentaudit());
		}
		return objattachments;
	}

	public LsOrderattachments Clouddownloadattachments(LsOrderattachments objattachments) {
		CloudOrderAttachment objfile = cloudFileManipulationservice.retrieveFile(objattachments);
		if (objfile != null) {
			objattachments.setFile(objfile.getFile());

		}
//		silent audit
		if (objattachments.getObjsilentaudit() != null) {
			objattachments.getObjsilentaudit().setTableName("LsOrderattachments");
			lscfttransactionRepository.save(objattachments.getObjsilentaudit());
		}
		return objattachments;
	}

	public GridFSDBFile retrieveLargeFile(String fileid) throws IllegalStateException, IOException {
		return fileManipulationservice.retrieveLargeFile(fileid);
	}

	public InputStream retrieveColudLargeFile(String fileid) throws IOException {
		return cloudFileManipulationservice.retrieveLargeFile(fileid);
	}

	public LsOrderattachments deleteattachments(LsOrderattachments objattachments) {
		Response objresponse = new Response();
		Long deletecount = (long) -1;

		if (objattachments.getIslargefile() == 0) {
			deletecount = fileManipulationservice.deleteattachments(objattachments.getFileid());
		} else {
			fileManipulationservice.deletelargeattachments(objattachments.getFileid());
			deletecount = (long) 1;
		}

		deletecount = lsOrderattachmentsRepository.deleteByAttachmentcode(objattachments.getAttachmentcode());

		if (deletecount > -1) {
			objresponse.setStatus(true);
		} else {
			objresponse.setStatus(false);
		}

		objattachments.setResponse(objresponse);
//		silent audit
		if (objattachments.getObjsilentaudit() != null) {
			objattachments.getObjsilentaudit().setTableName("LsOrderattachments");
			lscfttransactionRepository.save(objattachments.getObjsilentaudit());
		}
		return objattachments;
	}

	public LsOrderattachments Clouddeleteattachments(LsOrderattachments objattachments) {
		Response objresponse = new Response();
		Long deletecount = (long) -1;

		if (objattachments.getIslargefile() == 0) {
			deletecount = cloudFileManipulationservice.deleteattachments(objattachments.getFileid());
		} else {
			cloudFileManipulationservice.deletelargeattachments(objattachments.getFileid());
			deletecount = (long) 1;
		}

		deletecount = lsOrderattachmentsRepository.deleteByAttachmentcode(objattachments.getAttachmentcode());

		if (deletecount > -1) {
			objresponse.setStatus(true);
		} else {
			objresponse.setStatus(false);
		}

		objattachments.setResponse(objresponse);
//		silent audit
		if (objattachments.getObjsilentaudit() != null) {
			objattachments.getObjsilentaudit().setTableName("LsOrderattachments");
			lscfttransactionRepository.save(objattachments.getObjsilentaudit());
		}
		return objattachments;
	}

	public Lsordershareto Insertshareorder(Lsordershareto objordershareto) {
		Lsordershareto existingshare = lsordersharetoRepository
				.findBySharebyunifiedidAndSharetounifiedidAndOrdertypeAndSharebatchcode(
						objordershareto.getSharebyunifiedid(), objordershareto.getSharetounifiedid(),
						objordershareto.getOrdertype(), objordershareto.getSharebatchcode());
		if (existingshare != null) {
			objordershareto.setSharetocode(existingshare.getSharetocode());
			// objordershareto.setSharedon(existingshare.getSharedon());
		}

		lsordersharetoRepository.save(objordershareto);

		return objordershareto;
	}

	public Map<String, Object> Insertshareorderby(Lsordersharedby objordershareby) {
		Map<String, Object> map = new HashMap<>();
		Lsordersharedby existingshare = lsordersharedbyRepository
				.findBySharebyunifiedidAndSharetounifiedidAndOrdertypeAndSharebatchcode(
						objordershareby.getSharebyunifiedid(), objordershareby.getSharetounifiedid(),
						objordershareby.getOrdertype(), objordershareby.getSharebatchcode());
		if (existingshare != null) {
			objordershareby.setSharedbycode(existingshare.getSharedbycode());
			// objordershareby.setSharedon(existingshare.getSharedon());
		}
		lsordersharedbyRepository.save(objordershareby);

		long sharedbycount = 0;

		sharedbycount = lsordersharedbyRepository
				.countBySharebyunifiedidAndOrdertypeAndSharestatusOrderBySharedbycodeDesc(
						objordershareby.getSharebyunifiedid(), objordershareby.getOrdertype(), 1);

		map.put("order", objordershareby);
		map.put("sharedbycount", sharedbycount);

		return map;
	}

	public List<Lsordersharedby> Getordersharedbyme(Lsordersharedby lsordersharedby) {
		return lsordersharedbyRepository.findBySharebyunifiedidAndOrdertypeAndSharestatusOrderBySharedbycodeDesc(
				lsordersharedby.getSharebyunifiedid(), lsordersharedby.getOrdertype(), 1);
	}

	public List<Lsordershareto> Getordersharetome(Lsordershareto lsordershareto) {
		return lsordersharetoRepository.findBySharetounifiedidAndOrdertypeAndSharestatusOrderBySharetocodeDesc(
				lsordershareto.getSharetounifiedid(), lsordershareto.getOrdertype(), 1);
	}

	public Lsordersharedby Unshareorderby(Lsordersharedby objordershareby) {
		Lsordersharedby existingshare = lsordersharedbyRepository.findOne(objordershareby.getSharedbycode());

		existingshare.setSharestatus(0);
		existingshare.setUnsharedon(objordershareby.getUnsharedon());
		lsordersharedbyRepository.save(existingshare);

		return existingshare;
	}

	public Lsordershareto Unshareorderto(Lsordershareto lsordershareto) {
		Lsordershareto existingshare = lsordersharetoRepository.findOne(lsordershareto.getSharetocode());

		existingshare.setSharestatus(0);
		existingshare.setUnsharedon(lsordershareto.getUnsharedon());
		existingshare.setSharedbycode(lsordershareto.getSharedbycode());
		lsordersharetoRepository.save(existingshare);

		return existingshare;
	}

	public Lsordersharedby GetsharedorderStatus(Lsordersharedby objorder) {

		LSlogilablimsorderdetail objorgorder = new LSlogilablimsorderdetail();

		objorgorder.setBatchcode(objorder.getSharebatchcode());
		objorgorder.setObjLoggeduser(objorder.getObjLoggeduser());
		objorgorder.setObjsilentaudit(objorder.getObjsilentaudit());
		objorgorder.setObjmanualaudit(objorder.getObjmanualaudit());
		objorgorder.setIsmultitenant(objorder.getIsmultitenant());

		objorgorder = GetorderStatus(objorgorder);

		objorder = lsordersharedbyRepository.findOne(objorder.getSharedbycode());

		objorder.setObjorder(objorgorder);

		return objorder;
	}

	public Lsordershareto GetsharedtomeorderStatus(Lsordershareto objorder) {

		LSlogilablimsorderdetail objorgorder = new LSlogilablimsorderdetail();

		objorgorder.setBatchcode(objorder.getSharebatchcode());
		objorgorder.setObjLoggeduser(objorder.getObjLoggeduser());
		objorgorder.setObjsilentaudit(objorder.getObjsilentaudit());
		objorgorder.setObjmanualaudit(objorder.getObjmanualaudit());
		objorgorder.setIsmultitenant(objorder.getIsmultitenant());

		objorgorder = GetorderStatus(objorgorder);

		// objorder= lsordersharetoRepository.findOne(objorder.getSharetocode());

		objorder.setObjorder(objorgorder);

		return objorder;
	}

	public LSsamplefile GetResultsharedfileverContent(LSsamplefile objfile) {
		return GetResultfileverContent(objfile);
	}

	public LSsamplefile SaveSharedResultfile(LSsamplefile objfile) {
		return SaveResultfile(objfile);
	}

	public LSlogilablimsorderdetail SharedCloudUploadattachments(MultipartFile file, Long batchcode, String filename,
			String fileexe, Integer usercode, Date currentdate, Integer islargefile) throws IOException {
		return CloudUploadattachments(file, batchcode, filename, fileexe, usercode, currentdate, islargefile);
	}

	public LSlogilablimsorderdetail SharedUploadattachments(MultipartFile file, Long batchcode, String filename,
			String fileexe, Integer usercode, Date currentdate, Integer islargefile) throws IOException {
		return Uploadattachments(file, batchcode, filename, fileexe, usercode, currentdate, islargefile);
	}

	public LsOrderattachments SharedClouddeleteattachments(LsOrderattachments objattachments) {
		return Clouddeleteattachments(objattachments);
	}

	public LsOrderattachments shareddeleteattachments(LsOrderattachments objattachments) {
		return deleteattachments(objattachments);
	}

	public LsOrderattachments SharedClouddownloadattachments(LsOrderattachments objattachments) {
		return Clouddownloadattachments(objattachments);
	}

	public LsOrderattachments Shareddownloadattachments(LsOrderattachments objattachments) {
		return downloadattachments(objattachments);
	}

	public InputStream sharedretrieveColudLargeFile(String fileid) throws IOException {
		return cloudFileManipulationservice.retrieveLargeFile(fileid);
	}

	public GridFSDBFile sharedretrieveLargeFile(String fileid) throws IllegalStateException, IOException {
		return fileManipulationservice.retrieveLargeFile(fileid);
	}

	public ResponseEntity<InputStreamResource> downloadattachmentsNonCloud(String param, String fileid)
			throws IOException {

		if (param == null) {
			return null;
		}

		LsOrderattachments objattach = lsOrderattachmentsRepository.findFirst1ByfileidOrderByAttachmentcodeDesc(fileid);
		HttpHeaders header = new HttpHeaders();
		header.set("Content-Disposition", "attachment; filename=" + objattach.getFilename());

		if (Integer.parseInt(param) == 0) {
			CloudOrderAttachment objfile = CloudOrderAttachmentRepository.findByFileid(fileid);
//			OrderAttachment objfile = fileManipulationservice.retrieveFile(objattach);

			InputStreamResource resource = null;
			byte[] content = objfile.getFile().getData();
			int size = content.length;
			InputStream is = null;
			try {
				is = new ByteArrayInputStream(content);
				resource = new InputStreamResource(is);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (is != null)
						is.close();
				} catch (Exception ex) {

				}
			}

			header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			header.setContentLength(size);
			return new ResponseEntity<>(resource, header, HttpStatus.OK);
		} else if (Integer.parseInt(param) == 1) {
			InputStream fileDtream = cloudFileManipulationservice.retrieveLargeFile(fileid);

			InputStreamResource resource = null;
			byte[] content = IOUtils.toByteArray(fileDtream);
			int size = content.length;
			InputStream is = null;
			try {
				is = new ByteArrayInputStream(content);
				resource = new InputStreamResource(is);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (is != null)
						is.close();
				} catch (Exception ex) {

				}
			}

			header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			header.setContentLength(size);
			return new ResponseEntity<>(resource, header, HttpStatus.OK);

		}

		return null;

	}

	public ResponseEntity<InputStreamResource> downloadattachments(String param, String fileid) {

		if (param == null) {
			return null;
		}

		LsOrderattachments objattach = lsOrderattachmentsRepository.findFirst1ByfileidOrderByAttachmentcodeDesc(fileid);
		HttpHeaders header = new HttpHeaders();
		header.set("Content-Disposition", "attachment; filename=" + objattach.getFilename());

		if (Integer.parseInt(param) == 0) {
			OrderAttachment objfile = fileManipulationservice.retrieveFile(objattach);

			InputStreamResource resource = null;
			byte[] content = objfile.getFile().getData();
			int size = content.length;
			InputStream is = null;
			try {
				is = new ByteArrayInputStream(content);
				resource = new InputStreamResource(is);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (is != null)
						is.close();
				} catch (Exception ex) {

				}
			}

			header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			header.setContentLength(size);
			return new ResponseEntity<>(resource, header, HttpStatus.OK);
		} else if (Integer.parseInt(param) == 1) {
			GridFSDBFile gridFsFile = null;

			try {
				gridFsFile = retrieveLargeFile(fileid);
			} catch (IllegalStateException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
			System.out.println(gridFsFile.getContentType());
			header.setContentType(MediaType.parseMediaType(gridFsFile.getContentType()));
			header.setContentLength(gridFsFile.getLength());
			return new ResponseEntity<>(new InputStreamResource(gridFsFile.getInputStream()), header, HttpStatus.OK);
		}
		return null;

	}

	public String getsampledata() {
		String jsondata = "{\r\n" + "    \"rowHeight\": 20,\r\n" + "    \"tags\": [],\r\n"
				+ "    \"columnWidth\": 64,\r\n" + "    \"fieldcount\": 0,\r\n" + "    \"activeSheet\": \"Sheet1\",\r\n"
				+ "    \"charts\": [],\r\n" + "    \"Batchcoordinates\": {\r\n" + "        \"resultdirection\": 1,\r\n"
				+ "        \"parameters\": []\r\n" + "    },\r\n" + "    \"names\": [],\r\n" + "    \"sheets\": [\r\n"
				+ "        {\r\n" + "            \"gridLinesColor\": null,\r\n"
				+ "            \"selection\": \"E5:E5\",\r\n" + "            \"name\": \"Sheet1\",\r\n"
				+ "            \"frozenColumns\": 0,\r\n" + "            \"showGridLines\": true,\r\n"
				+ "            \"defaultCellStyle\": {\r\n" + "                \"fontFamily\": \"Arial\",\r\n"
				+ "                \"fontSize\": \"12\"\r\n" + "            },\r\n"
				+ "            \"hyperlinks\": [],\r\n" + "            \"rows\": [\r\n" + "                {\r\n"
				+ "                    \"index\": 2,\r\n" + "                    \"cells\": [\r\n"
				+ "                        {\r\n" + "                            \"index\": 4,\r\n"
				+ "                            \"value\": \"nreee\"\r\n" + "                        }\r\n"
				+ "                    ]\r\n" + "                }\r\n" + "            ],\r\n"
				+ "            \"activeCell\": \"E5:E5\",\r\n" + "            \"drawings\": [],\r\n"
				+ "            \"mergedCells\": [],\r\n" + "            \"columns\": [],\r\n"
				+ "            \"frozenRows\": 0\r\n" + "        }\r\n" + "    ],\r\n" + "    \"images\": []\r\n" + "}";

		return jsondata;
	}

	public List<LsOrderSampleUpdate> GetOrderResourcesQuantitylst(LsOrderSampleUpdate objorder) {
		// TODO Auto-generated method stub
		List<LsOrderSampleUpdate> sampleupdatelst = new ArrayList<LsOrderSampleUpdate>();
		if (objorder.getOrdersampleusedDetail() != null) {
			sampleupdatelst = lsOrderSampleUpdateRepository
					.findByOrdersampleusedDetail(objorder.getOrdersampleusedDetail());
		}
		return sampleupdatelst;
	}

	public Map<String, Object> SaveOrderResourcesQuantity(Map<String, Object> argobj) {
		// TODO Auto-generated method stub
		Map<String, Object> objmap = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		LsOrderSampleUpdate ordersample = new LsOrderSampleUpdate();
		Lsrepositoriesdata lsrepositoriesdata = new Lsrepositoriesdata();
		if (argobj.containsKey("Ordersampleobj")) {
			ordersample = mapper.convertValue(argobj.get("Ordersampleobj"), LsOrderSampleUpdate.class);
//			lsOrderSampleUpdateRepository.save(ordersample);
//			ordersample.setOrdersampleusedDetail(ordersample.getOrdersampleusedDetail()+ " id:" + ordersample.getOrdersamplecode());
			lsOrderSampleUpdateRepository.save(ordersample);
		}
		if (argobj.containsKey("LsrepositoriesdataSeletedObj")) {
			lsrepositoriesdata = mapper.convertValue(argobj.get("LsrepositoriesdataSeletedObj"),
					Lsrepositoriesdata.class);
			LsrepositoriesdataRepository.save(lsrepositoriesdata);
		}
		if (argobj.containsKey("LsrepositoriesObj")) {
			@SuppressWarnings("unused")
			Lsrepositories LsrepositoriesObj = mapper.convertValue(argobj.get("LsrepositoriesObj"),
					Lsrepositories.class);
		}
		objmap.put("ordersample", ordersample);
		objmap.put("lsrepositoriesdata", lsrepositoriesdata);
		return objmap;
	}

	public List<Lsrepositoriesdata> GetEditedOrderResources(Lsrepositoriesdata objorder) {
		// TODO Auto-generated method stub
		return LsrepositoriesdataRepository.findByRepositorycodeAndSitecodeAndItemstatusOrderByRepositorydatacodeDesc(
				objorder.getRepositorycode(), objorder.getSitecode(), 1);
	}

	public String getsampledata(Long batchcode, Integer indexrow, Integer cellindex, Integer sheetval, String tagdata,
			String tagvalue, String samplevalue, String sampledetails, Integer lssamplefile, Integer multitenant) {
//		LSsamplefile LSsamplefile= lssamplefileRepository.findByfilesamplecode(lssamplefile);
//		OrderCreation objsavefile = new OrderCreation();
		String Content = "";
		if (multitenant == 1) {
			CloudOrderCreation file = cloudOrderCreationRepository.findById((long) lssamplefile);

			if (file != null) {
				Content = file.getContent();
			}
//			else
//			{
//				Content = objorder.getLsfile().getFilecontent();
//			}
		} else {
			OrderCreation objsavefile = mongoTemplate.findById(lssamplefile, OrderCreation.class);
			if (objsavefile != null) {
				Content = objsavefile.getContent();
			} else {
//				Content = objorder.getLsfile().getFilecontent();
			}
		}

		return Content;
	}

	public List<Integer> Getuserworkflow(LSusergroup lsusergroup) {
		if (lsusergroup != null) {
			List<LSworkflowgroupmapping> lsworkflowgroupmapping = lsworkflowgroupmappingRepository
					.findBylsusergroup(lsusergroup);

			List<LSworkflow> lsworkflow = lsworkflowRepository.findByLsworkflowgroupmappingIn(lsworkflowgroupmapping);

			List<Integer> lstworkflow = new ArrayList<Integer>();
			if (lsworkflow != null && lsworkflow.size() > 0) {
				lstworkflow = lsworkflow.stream().map(LSworkflow::getWorkflowcode).collect(Collectors.toList());
			}

			return lstworkflow;
		} else {
			List<Integer> lstworkflow = new ArrayList<Integer>();
			return lstworkflow;
		}
	}

	public Map<String, Object> Getuserprojects(LSuserMaster objuser) {
		if (objuser.getUsercode() != null) {
			Map<String, Object> objmap = new HashMap<>();
			List<LSuserteammapping> lstteammap = lsuserteammappingRepository
					.findByLsuserMasterAndTeamcodeNotNull(objuser);
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingIn(lstteammap);
			List<LSprojectmaster> lstprojectmaster = lsprojectmasterRepository.findByLsusersteamIn(lstteam);

			List<Integer> lstproject = new ArrayList<Integer>();
			if (lstprojectmaster != null && lstprojectmaster.size() > 0) {
				lstproject = lstprojectmaster.stream().map(LSprojectmaster::getProjectcode)
						.collect(Collectors.toList());
			}

			List<Integer> lstteamcode = new ArrayList<Integer>();
			if (lstteam != null && lstteam.size() > 0) {
				lstteamcode = lstteam.stream().map(LSusersteam::getTeamcode).collect(Collectors.toList());
			}

			List<Integer> lstteamusercode = new ArrayList<Integer>();
			if (lstteammap != null && lstteammap.size() > 0) {
				List<LSuserMaster> lstusers = lsuserteammappingRepository.getLsuserMasterByTeamcode(lstteamcode);
				if (lstusers != null && lstusers.size() > 0) {
					lstteamusercode = lstusers.stream().map(LSuserMaster::getUsercode).collect(Collectors.toList());
				}
			}

			objmap.put("project", lstproject);
			objmap.put("team", lstteamcode);
			objmap.put("teamuser", lstteamusercode);
			return objmap;
		} else {
			Map<String, Object> objmap = new HashMap<>();
			return objmap;
		}
	}

	public Map<String, Object> Getinitialorders(LSlogilablimsorderdetail objorder) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		if (objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
			mapOrders.put("orders", Getadministratororder(objorder));
			mapOrders.put("ordercount", lslogilablimsorderdetailRepository.count());
		} else {
			mapOrders.put("orders", Getuserorder(objorder));
			mapOrders.put("ordercount", lslogilablimsorderdetailRepository
					.countByLsprojectmasterIn(objorder.getLsuserMaster().getLstproject()));
		}

		return mapOrders;
	}

	public List<Logilabordermaster> Getremainingorders(LSlogilablimsorderdetail objorder) {
		if (objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
			return Getadministratororder(objorder);
		} else {
			return Getuserorder(objorder);
		}
	}

	public List<Logilabordermaster> Getadministratororder(LSlogilablimsorderdetail objorder) {
		List<Logilabordermaster> lstorders = new ArrayList<Logilabordermaster>();
		if (objorder.getBatchcode() == 0) {
			lstorders = lslogilablimsorderdetailRepository.findFirst20ByOrderByBatchcodeDesc();
		} else {
			lstorders = lslogilablimsorderdetailRepository
					.findFirst20ByBatchcodeLessThanOrderByBatchcodeDesc(objorder.getBatchcode());
		}
		return lstorders;
	}

	public List<Logilabordermaster> Getuserorder(LSlogilablimsorderdetail objorder) {
		List<LSprojectmaster> lstproject = objorder.getLsuserMaster().getLstproject();
		List<Logilabordermaster> lstorders = new ArrayList<Logilabordermaster>();
		if (lstproject != null) {
			List<LSworkflow> lstworkflow = objorder.getLsuserMaster().getLstworkflow();
			if (objorder.getBatchcode() == 0) {
				lstorders = lslogilablimsorderdetailRepository
						.findFirst20ByLsprojectmasterInOrderByBatchcodeDesc(lstproject);
			} else {
				lstorders = lslogilablimsorderdetailRepository
						.findFirst20ByBatchcodeLessThanAndLsprojectmasterInOrderByBatchcodeDesc(objorder.getBatchcode(),
								lstproject);
			}
			lstorders.forEach(objord -> objord.setLstworkflow(lstworkflow));
		}
		return lstorders;
	}

	public Map<String, Object> uploadsheetimages(MultipartFile file, String originurl, String username,
			String sitecode) {
		Map<String, Object> map = new HashMap<String, Object>();

		String id = null;
		try {
			id = cloudFileManipulationservice.storecloudfilesreturnUUID(file, "sheetimagestemp");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		map.put("link",
				originurl + "/Instrument/downloadsheetimagestemp/" + id + "/" + TenantContext.getCurrentTenant() + "/"
						+ FilenameUtils.removeExtension(file.getOriginalFilename()) + "/"
						+ FilenameUtils.getExtension(file.getOriginalFilename()));
		return map;
	}

	public ByteArrayInputStream downloadsheetimages(String fileid, String tenant)
			throws FileNotFoundException, IOException {
		TenantContext.setCurrentTenant(tenant);
		byte[] data = null;
		try {
			data = StreamUtils
					.copyToByteArray(cloudFileManipulationservice.retrieveCloudFile(fileid, tenant + "sheetimages"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			data = null;
		}

		if (data == null) {
			String[] arrOffiledid = fileid.split("_", 2);
			String Originalfieldid = arrOffiledid[0];
			try {
				data = StreamUtils.copyToByteArray(
						cloudFileManipulationservice.retrieveCloudFile(Originalfieldid, tenant + "sheetimages"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				data = null;
			}

			if (data == null) {
				try {
					data = StreamUtils.copyToByteArray(cloudFileManipulationservice.retrieveCloudFile(Originalfieldid,
							tenant + "sheetimagestemp"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					data = null;
				}
			}
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(data);

		return bis;
	}

	public ByteArrayInputStream downloadsheetimagestemp(String fileid, String tenant)
			throws FileNotFoundException, IOException {
		TenantContext.setCurrentTenant(tenant);
		byte[] data = null;
		try {
			data = StreamUtils.copyToByteArray(
					cloudFileManipulationservice.retrieveCloudFile(fileid, tenant + "sheetimagestemp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(data);

		return bis;
	}

	public Response removesheetimage(Map<String, String> body) {
		Response objresponse = new Response();
		String filid = body.get("fileid");
		cloudFileManipulationservice.deletecloudFile(filid, "sheetimages");
		objresponse.setStatus(true);
		return objresponse;
	}

	public boolean updatesheetimagesforversion(List<Map<String, String>> lstfiles) {
		for (Map<String, String> fileObj : lstfiles) {
			String copyfrom = fileObj.get("copyfrom");
			String copyto = fileObj.get("copyto");
			String isnew = fileObj.get("isnew");

			if (isnew.equals("true")) {
				cloudFileManipulationservice.movefiletoanothercontainerandremove(
						TenantContext.getCurrentTenant() + "sheetimagestemp",
						TenantContext.getCurrentTenant() + "sheetimages", copyfrom);
			}

			try {
				cloudFileManipulationservice.updateversionCloudFile(copyfrom,
						TenantContext.getCurrentTenant() + "sheetimages", copyto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return true;
	}

	public boolean deletesheetimagesforversion(List<Map<String, String>> lstfiles) {
		for (Map<String, String> fileObj : lstfiles) {
			String fileid = fileObj.get("fieldid");

			cloudFileManipulationservice.deleteFile(fileid, TenantContext.getCurrentTenant() + "sheetimages");

			String[] arrOffiledid = fileid.split("_", 2);
			String Originalfieldid = arrOffiledid[0];

			cloudFileManipulationservice.deleteFile(Originalfieldid, TenantContext.getCurrentTenant() + "sheetimages");

			cloudFileManipulationservice.deleteFile(Originalfieldid,
					TenantContext.getCurrentTenant() + "sheetimagestemp");

		}
		return true;
	}

	public Map<String, Object> uploadsheetimagesSql(MultipartFile file, String originurl, String username,
			String sitecode) throws IOException {

		Map<String, Object> map = new HashMap<String, Object>();

		UUID objGUID = UUID.randomUUID();
		String randomUUIDString = objGUID.toString();

		LSfileimages fileObj = new LSfileimages();
		fileObj.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
		fileObj.setFileid(randomUUIDString);

		LSfileimagesRepository.save(fileObj);

		Fileimagestemp fileImg = new Fileimagestemp();

		fileImg.setId(fileObj.getFileimagecode());
		fileImg.setFileid(randomUUIDString);
		fileImg.setFile(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
		fileImg = FileimagestempRepository.insert(fileImg);

		map.put("link",
				originurl + "/Instrument/downloadsheetimagestempsql/" + randomUUIDString + "/"
						+ FilenameUtils.removeExtension(file.getOriginalFilename()) + "/"
						+ FilenameUtils.getExtension(file.getOriginalFilename()));

		return map;
	}

	public Fileimagestemp downloadsheetimagestempsql(String fileid) {
		return FileimagestempRepository.findByFileid(fileid);
	}

	public Fileimages downloadsheetimagessql(String fileid) {
		return FileimagesRepository.findByFileid(fileid);
	}

	public boolean updatesheetimagesforversionSql(List<Map<String, String>> lstfiles) throws IOException {
		for (Map<String, String> fileObj : lstfiles) {
			String fileid = fileObj.get("copyfrom");
			String newFileid = fileObj.get("copyto");
			String isnew = fileObj.get("isnew");

			if (isnew.equals("true")) {

				Fileimagestemp oldFile = FileimagestempRepository.findByFileid(fileid);

				Fileimages newFile = new Fileimages();

				newFile.setFile(oldFile.getFile());
				newFile.setFileid(fileid);
				newFile.setId(oldFile.getId());

				FileimagesRepository.save(newFile);
				FileimagestempRepository.delete(oldFile.getFileid());

			}
			Fileimagestemp oldFile = FileimagestempRepository.findByFileid(fileid);

			Fileimages newFile = new Fileimages();

			newFile.setFile(oldFile.getFile());
			newFile.setFileid(newFileid);
			newFile.setId(oldFile.getId());

			FileimagesRepository.save(newFile);

		}

		return true;
	}

	public boolean deletesheetimagesforversionSql(List<Map<String, String>> lstfiles) {
		for (Map<String, String> fileObj : lstfiles) {

			String fileid = fileObj.get("fieldid");

			FileimagesRepository.delete(fileid);

			String[] arrOffiledid = fileid.split("_", 2);
			String Originalfieldid = arrOffiledid[0];

			FileimagesRepository.delete(Originalfieldid);
			FileimagestempRepository.delete(Originalfieldid);

		}
		return true;
	}

	public Map<String, Object> UploadLimsFile(MultipartFile file, Long batchcode, String filename) throws IOException {

		Map<String, Object> mapObj = new HashMap<String, Object>();

		LsSheetorderlimsrefrence objattachment = new LsSheetorderlimsrefrence();

		SheetorderlimsRefrence objfile = fileManipulationservice.storeLimsSheetRefrence(file);

		if (objfile != null) {
			objattachment.setFileid(objfile.getId());
		}

		objattachment.setFilename(filename);
		objattachment.setBatchcode(batchcode);
//		objattachment.setTestcode(testcode);

		lssheetorderlimsrefrenceRepository.save(objattachment);

		mapObj.put("elnSheet", objattachment);

		return mapObj;
	}

	public LsSheetorderlimsrefrence downloadSheetFromELN(LsSheetorderlimsrefrence objattachments) {

		SheetorderlimsRefrence objfile = fileManipulationservice.LimsretrieveELNsheet(objattachments);

		if (objfile != null) {
			objattachments.setFile(objfile.getFile());

		}

		return objattachments;
	}

	public Map<String, Object> GetLimsorderid(String orderid) {

		Map<String, Object> map = new HashMap<>();

		LSlogilablimsorderdetail objOrder = lslogilablimsorderdetailRepository.findByBatchid(orderid);

		if (objOrder != null) {
			map.put("ordercode", objOrder.getBatchcode());
		} else {
			map.put("ordercode", -1);
		}

		return map;
	}

	public Map<String, Object> GetorderforlinkLIMS(LSlogilablimsorderdetail objorder) {

		Map<String, Object> mapOrder = new HashMap<String, Object>();

		LSlogilablimsorderdetail objupdated = lslogilablimsorderdetailRepository.findByBatchid(objorder.getBatchid());

		if (objupdated.getLockeduser() != null) {
			objupdated.setIsLock(1);
		} else {
			objupdated.setIsLock(0);
		}

		if (objupdated.getLockeduser() != null && objorder.getObjLoggeduser() != null
				&& objupdated.getLockeduser().equals(objorder.getObjLoggeduser().getUsercode())) {
			objupdated.setIsLockbycurrentuser(1);
		} else {
			objupdated.setIsLockbycurrentuser(0);
		}

		if (objupdated.getFiletype() != 0 && objupdated.getOrderflag().toString().trim().equals("N")) {
			if (objupdated.getLsworkflow().equals(lsworkflowRepository
					.findTopByAndLssitemasterOrderByWorkflowcodeDesc(objorder.getObjLoggeduser().getLssitemaster()))) {
				objupdated.setIsFinalStep(1);
			} else {
				objupdated.setIsFinalStep(0);
			}
		}

		if (objupdated.getFiletype() == 0) {
			objupdated.setLstestparameter(lStestparameterRepository.findByntestcode(objupdated.getTestcode()));
		}

		if (objupdated.getLssamplefile() != null) {
			if (objorder.getIsmultitenant() == 1) {
				CloudOrderCreation file = cloudOrderCreationRepository
						.findById((long) objupdated.getLssamplefile().getFilesamplecode());
				if (file != null) {
					objupdated.getLssamplefile().setFilecontent(file.getContent());
				}
			} else {
				OrderCreation file = mongoTemplate.findById(objupdated.getLssamplefile().getFilesamplecode(),
						OrderCreation.class);
				if (file != null) {
					objupdated.getLssamplefile().setFilecontent(file.getContent());
				}
			}
		}

		LSfile objFile = LSfileRepository.findByfilecode(objupdated.getFilecode());

		objupdated.setLsfile(objFile);

		if (objupdated.getLsfile() != null) {
			if (objorder.getIsmultitenant() == 1) {
				Long filecode = new Long(objupdated.getFilecode());
				CloudSheetCreation file = cloudSheetCreationRepository.findById(filecode);
				if (file != null) {
					objupdated.getLsfile().setFilecontent(file.getContent());
				}
			} else {
				SheetCreation file = mongoTemplate.findById(objupdated.getFilecode(), SheetCreation.class);
				if (file != null) {
					objupdated.getLsfile().setFilecontent(file.getContent());
				}
			}
		}

		mapOrder.put("order", objupdated);
		mapOrder.put("version",
				lssamplefileversionRepository.findByFilesamplecodeOrderByVersionnoDesc(objupdated.getLssamplefile()));

		return mapOrder;
	}

	public Map<String, Object> UploadLimsResultFile(MultipartFile file, Long batchcode, String filename)
			throws IOException {

		System.out.print("Inside UploadLimsFile");

		Map<String, Object> mapObj = new HashMap<String, Object>();

		LsResultlimsOrderrefrence objattachment = new LsResultlimsOrderrefrence();

		ResultorderlimsRefrence objfile = fileManipulationservice.storeResultLimsSheetRefrence(file);

		if (objfile != null) {
			objattachment.setFileid(objfile.getId());

			System.out.print("Inside UploadLimsFile filecode value " + batchcode.intValue());
		}

		objattachment.setFilename(filename);
		objattachment.setBatchid(filename);

		LsResultlimsOrderrefrenceRepository.save(objattachment);

		mapObj.put("elnSheet", objattachment);

		return mapObj;
	}

}
