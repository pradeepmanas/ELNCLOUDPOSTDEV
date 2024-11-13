package com.agaram.eln.primary.service.instrumentDetails;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.fetchmodel.getmasters.Projectmaster;
import com.agaram.eln.primary.fetchmodel.getorders.LogilabOrderDetails;
import com.agaram.eln.primary.fetchmodel.getorders.Logilabordermaster;
import com.agaram.eln.primary.fetchmodel.getorders.Logilaborders;
import com.agaram.eln.primary.fetchmodel.getorders.Logilabprotocolorders;
import com.agaram.eln.primary.model.cfr.LSactivity;
//import com.agaram.eln.primary.model.cfr.LSaudittrailconfiguration;
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
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.general.SearchCriteria;
import com.agaram.eln.primary.model.general.SheetCreation;
import com.agaram.eln.primary.model.instrumentDetails.LSOrdernotification;
import com.agaram.eln.primary.model.instrumentDetails.LSSheetOrderStructure;
import com.agaram.eln.primary.model.instrumentDetails.LSfields;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorder;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.instrumentDetails.LSprotocolfolderfiles;
import com.agaram.eln.primary.model.instrumentDetails.LSresultdetails;
import com.agaram.eln.primary.model.instrumentDetails.LSsheetfolderfiles;
import com.agaram.eln.primary.model.instrumentDetails.LsAutoregister;
import com.agaram.eln.primary.model.instrumentDetails.LsMappedInstruments;
import com.agaram.eln.primary.model.instrumentDetails.LsMethodFields;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderSampleUpdate;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.instrumentDetails.LsResultlimsOrderrefrence;
import com.agaram.eln.primary.model.instrumentDetails.LsSheetorderlimsrefrence;
import com.agaram.eln.primary.model.instrumentDetails.Lsbatchdetails;
import com.agaram.eln.primary.model.instrumentDetails.Lsordersharedby;
import com.agaram.eln.primary.model.instrumentDetails.Lsordershareto;
import com.agaram.eln.primary.model.instrumentDetails.Lsorderworkflowhistory;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolordersharedby;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolordershareto;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolorderstructure;
import com.agaram.eln.primary.model.instrumentDetails.Lsresultfororders;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.methodsetup.ELNFileAttachments;
import com.agaram.eln.primary.model.methodsetup.ELNResultDetails;
import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.methodsetup.ParserBlock;
import com.agaram.eln.primary.model.methodsetup.ParserField;
import com.agaram.eln.primary.model.methodsetup.SubParserField;
import com.agaram.eln.primary.model.notification.Email;
import com.agaram.eln.primary.model.protocols.Elnprotocolworkflow;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.reports.lsreportfile;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSfilemethod;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefileversion;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflowgroupmapping;
import com.agaram.eln.primary.model.sheetManipulation.Notification;
import com.agaram.eln.primary.model.templates.LsMappedTemplate;
import com.agaram.eln.primary.model.templates.LsUnmappedTemplate;
import com.agaram.eln.primary.model.usermanagement.LSMultisites;
//import com.agaram.eln.primary.model.usermanagement.LSMultiusergroup;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSactiveUser;
import com.agaram.eln.primary.model.usermanagement.LScentralisedUsers;
import com.agaram.eln.primary.model.usermanagement.LSnotification;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserActions;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;
import com.agaram.eln.primary.repository.cfr.LSactivityRepository;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.cfr.LSpreferencesRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudOrderAttachmentRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudOrderCreationRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudOrderVersionRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudSheetCreationRepository;
import com.agaram.eln.primary.repository.dashboard.LsActiveWidgetsRepository;
import com.agaram.eln.primary.repository.fileManipulation.FileimagesRepository;
import com.agaram.eln.primary.repository.fileManipulation.FileimagestempRepository;
import com.agaram.eln.primary.repository.fileManipulation.LSfileimagesRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSSheetOrderStructureRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSfieldsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSordernotificationRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSprotocolfolderfilesRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSresultdetailsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSsheetfolderfilesRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LogilablimsorderdetailsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsAutoregisterRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsMappedInstrumentsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsMethodFieldsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsOrderSampleUpdateRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsOrderSampleUpdateRepository.UserProjection;
import com.agaram.eln.primary.repository.instrumentDetails.LsOrderattachmentsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsResultlimsOrderrefrenceRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsSheetorderlimsrefrenceRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsbatchdetailsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LselninstrumentmasterRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsordersharedbyRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsordersharetoRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsorderworkflowhistoryRepositroy;
import com.agaram.eln.primary.repository.instrumentDetails.LsprotocolOrderStructureRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsprotocolordersharedbyRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsprotocolordersharetoRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsresultforordersRepository;
import com.agaram.eln.primary.repository.instrumentsetup.InstMasterRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesdataRepository;
import com.agaram.eln.primary.repository.material.ElnmaterialRepository;
import com.agaram.eln.primary.repository.methodsetup.ELNFileAttachmentsRepository;
import com.agaram.eln.primary.repository.methodsetup.ELNResultDetailsRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserBlockRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserFieldRepository;
import com.agaram.eln.primary.repository.methodsetup.SubParserFieldRepository;
import com.agaram.eln.primary.repository.notification.EmailRepository;
import com.agaram.eln.primary.repository.protocol.ElnprotocolworkflowRepository;
import com.agaram.eln.primary.repository.protocol.ElnprotocolworkflowgroupmapRepository;
import com.agaram.eln.primary.repository.protocol.LSlogilabprotocoldetailRepository;
import com.agaram.eln.primary.repository.reports.ReportfileRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfileRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfilemethodRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfileparameterRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSparsedparametersRespository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsamplefileRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsamplefileversionRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsamplemasterRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsampleresultRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LStestparameterRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowgroupmappingRepository;
import com.agaram.eln.primary.repository.sheetManipulation.NotificationRepository;
import com.agaram.eln.primary.repository.templates.LsMappedTemplateRepository;
import com.agaram.eln.primary.repository.templates.LsUnmappedTemplateRepository;
import com.agaram.eln.primary.repository.usermanagement.LSMultisitesRepositery;
//import com.agaram.eln.primary.repository.usermanagement.LSMultiusergroupRepositery;
import com.agaram.eln.primary.repository.usermanagement.LSactiveUserRepository;
import com.agaram.eln.primary.repository.usermanagement.LSnotificationRepository;
import com.agaram.eln.primary.repository.usermanagement.LSprojectmasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSprojectmasterRepository.ProjectOrTaskOrMaterialView;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusersteamRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;
//import com.agaram.eln.primary.repository4mibatis.instrumentDetails.LSlogilablimsorderdetailMibatisRepository;
//import com.agaram.eln.primary.repository4mibatis.LSlogilablimsorderdetailMibatisRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.fileManipulation.FileManipulationservice;
import com.agaram.eln.primary.service.protocol.ProtocolService;
import com.agaram.eln.primary.service.sheetManipulation.FileService;
import com.agaram.eln.primary.service.usermanagement.LoginService;
import com.agaram.eln.primary.service.usermanagement.UserService;
import com.agaram.eln.primary.service.webParser.WebparserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.gridfs.GridFSDBFile;

@Service
//@EnableJpaRepositories(basePackageClasses = LsMethodFieldsRepository.class)
public class InstrumentService {

	@Autowired
	private Environment env;
	@Autowired
	private LsMethodFieldsRepository lsMethodFieldsRepository;
//	@Autowired
//	private LSinstrumentsRepository lSinstrumentsRepository;
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
	private LSfileparameterRepository lsFileparameterRepository;
	@Autowired
	private LSlogilablimsorderdetailRepository lslogilablimsorderdetailRepository;
//	@Autowired
//	private LsresulttagsRepository lsresulttagsRepository;
	@Autowired
	private LSworkflowRepository lsworkflowRepository;
//	@Autowired
//	private LSlimsorderRepository lslimsorderRepository;
	@Autowired
	private LSlogilablimsorderRepository lslogilablimsorderRepository;
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

//	@Autowired
//	private LSlimsorderRepository lSlimsorderRepository;

	@Autowired
	ProtocolService protocolservice;

	@Autowired
	private LStestparameterRepository lStestparameterRepository;

	@Autowired
	private LSuserMasterRepository lsuserMasterRepository;

	@Autowired
	private LsOrderattachmentsRepository lsOrderattachmentsRepository;

	@Autowired
	private ELNFileAttachmentsRepository elnFileattachmentsRepository;

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
	private CloudFileManipulationservice objCloudFileManipulationservice;
	@Autowired
	private CloudOrderCreationRepository cloudOrderCreationRepository;
	@Autowired
	private ReportfileRepository reportfileRepository;

	@Autowired
	private LsresultforordersRepository lsresultforordersRepository;

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

	@Autowired
	private LSpreferencesRepository LSpreferencesRepository;

	@Autowired
	private WebparserService parserService;

	@Autowired
	private ELNResultDetailsRepository ELNResultDetailsRepository;

	@Autowired
	private LSSheetOrderStructureRepository lsSheetOrderStructureRepository;

	@Autowired
	private LSsamplemasterRepository lssamplemasterrepository;

	@Autowired
	private GridFsTemplate gridFsTemplate;

//	@Autowired
//	private UserService userService;

	@Autowired
	private LsprotocolOrderStructureRepository lsprotocolorderStructurerepository;

	@Autowired
	private LSlogilabprotocoldetailRepository LSlogilabprotocoldetailRepository;

//	@Autowired
//	private LStestmasterlocalRepository lstestmasterlocalRepository;

	@Autowired
	private FileService fileService;

	@Autowired
	private LsprotocolordersharedbyRepository lsprotocolordersharedbyRepository;

	@Autowired
	private LsprotocolordersharetoRepository lsprotocolordersharetoRepository;

	@Autowired
	private LSsheetfolderfilesRepository lssheetfolderfilesRepository;

	@Autowired
	ProtocolService ProtocolMasterService;

	@Autowired
	LoginService loginservice;

	@Autowired
	UserService userService;

	@Autowired
	LoginService LoginService;

	@Autowired
	private LSprotocolfolderfilesRepository lsprotocolfolderfilesRepository;

//	@Autowired
//	private LSMultiusergroupRepositery lsMultiusergroupRepositery;

	@Autowired
	private LsbatchdetailsRepository LsbatchdetailsRepository;

	@Autowired
	private LsMappedInstrumentsRepository lsMappedInstrumentsRepository;

	@Autowired
	private LSactiveUserRepository lSactiveUserRepository;

//	@Autowired
//	private LSprotocolorderstephistoryRepository lsprotocolorderstephistoryRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private ElnmaterialRepository elnmaterialRepository;

	@Autowired
	private EmailRepository EmailRepository;
	@Autowired
	private ElnprotocolworkflowRepository elnprotocolworkflowRepository;
	@Autowired
	private ElnprotocolworkflowgroupmapRepository elnprotocolworkflowgroupmapRepository;

	@Autowired
	@Qualifier("entityManagerFactory")
	private EntityManager entityManager;

	@Autowired
	private LSMultisitesRepositery LSMultisitesRepositery;

	@Autowired
	private LSordernotificationRepository lsordernotificationrepo;

	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private LsActiveWidgetsRepository lsActiveWidgetsRepository;

	@Autowired
	private LsAutoregisterRepository lsautoregisterrepo;

	@Autowired
	private LogilablimsorderdetailsRepository logilablimsorderdetailsRepository;

	private Map<Integer, TimerTask> scheduledTasks = new HashMap<>();

	private static Map<String, Timer> timerMap = new HashMap<>();
	private static Map<String, Boolean> timerStatusMap = new HashMap<>();
	 private ConcurrentHashMap<String, LSlogilablimsorderdetail> orderDetailMap = new ConcurrentHashMap<>();
//	public Map<String, Object> getInstrumentparameters(LSSiteMaster lssiteMaster) {
//		Map<String, Object> obj = new HashMap<>();
//		List<String> lsInst = new ArrayList<String>();
//		lsInst.add("INST000");
//		lsInst.add("LPRO");
//		List<LsMethodFields> Methods = lsMethodFieldsRepository.findByinstrumentidNotIn(lsInst);
//
//		if (lssiteMaster.getIsmultitenant() != 1) {
//			List<LSfields> Generalfields = lSfieldsRepository.findByisactive(1);
//			List<LSinstruments> Instruments = lSinstrumentsRepository.findAll();
//			List<InstrumentMaster> InstrMaster = lsInstMasterRepository.findAll();
//			List<LsMappedTemplate> MappedTemplate = LsMappedTemplateRepository.findAll();
//			List<LsUnmappedTemplate> UnmappedTemplate = LsUnmappedTemplateRepository.findAll();
//
//			List<Method> elnMethod = lsMethodRepository.findByStatus(1);
//			List<ParserBlock> ParserBlock = lsParserBlockRepository.findByStatus(1);
//			List<ParserField> ParserField = lsParserRepository.findByStatus(1);
//			List<SubParserField> SubParserField = lsSubParserRepository.findByStatus(1);
//			obj.put("Generalfields", Generalfields);
//			
//			List<ParserField> filteredList = ParserField.stream()
//					.filter(filterParser -> SubParserField.stream()
//							.anyMatch(filterSubParser -> filterParser.getParserfieldkey()
//									.equals(filterSubParser.getParserfield().getParserfieldkey())))
//					.collect(Collectors.toList());
//
//			ParserField.removeAll(filteredList);
//
//			obj.put("Instruments", Instruments);
//			obj.put("Instrmaster", InstrMaster);
//			obj.put("elninstrument", lselninstrumentmasterRepository
//					.findBylssitemasterAndStatusOrderByInstrumentcodeDesc(lssiteMaster, 1));
//			obj.put("Mappedtemplates", MappedTemplate);
//			obj.put("Unmappedtemplates", UnmappedTemplate);
//			obj.put("ELNMethods", elnMethod);
//			obj.put("ParserBlock", ParserBlock);
//			obj.put("ParserField", ParserField);
//			obj.put("SubParserField", SubParserField);
//		} else {
//
//			List<LSfields> Generalfields = lSfieldsRepository.findByisactiveAndMethodnameOrderByFieldordernoAsc(1, "ID_GENERAL");
//
//			List<InstrumentMaster> InstrMaster = lsInstMasterRepository.findByStatus(1);
//
//			List<Method> elnMethod = lsMethodRepository.findByStatus(1);
//			List<ParserBlock> ParserBlock = lsParserBlockRepository.findByStatus(1);
//			List<ParserField> ParserField = lsParserRepository.findByStatus(1);
//			// List<SubParserField> SubParserField = lsSubParserRepository.findAll();
//			List<SubParserField> SubParserField = lsSubParserRepository.findByStatus(1);
//			obj.put("Generalfields", Generalfields);
//
//			List<ParserField> filteredList = ParserField.stream()
//					.filter(filterParser -> SubParserField.stream()
//							.anyMatch(filterSubParser -> filterParser.getParserfieldkey()
//									.equals(filterSubParser.getParserfield().getParserfieldkey())))
//					.collect(Collectors.toList());
//
//			ParserField.removeAll(filteredList);
//
//			obj.put("Instrmaster", InstrMaster);
//			obj.put("ELNMethods", elnMethod);
//			obj.put("ParserBlock", ParserBlock);
//			obj.put("ParserField", ParserField);
//			obj.put("SubParserField", SubParserField);
//		}
//
//		LSpreferences objPrefrence = LSpreferencesRepository.findBySerialno(1);
//
//		if (objPrefrence.getValuesettings().equalsIgnoreCase("Active")) {
//
//			obj.put("Methods", parserService.getwebparsemethods());
//			obj.put("Instruments", parserService.getwebparserInstruments());
//		} else {
//			obj.put("Methods", Methods);
//		}
//
//		return obj;
//	}

	public Map<String, Object> getInstrumentparameters(LSSiteMaster lssiteMaster) {
		Map<String, Object> obj = new HashMap<>();
		List<String> lsInst = new ArrayList<String>();
		lsInst.add("INST000");
		lsInst.add("LPRO");
		List<LsMethodFields> Methods = lsMethodFieldsRepository.findByinstrumentidNotIn(lsInst);
		// List<SubParserField> SubParserField = new ArrayList<SubParserField>();

		if (lssiteMaster.getIsmultitenant() != 1) {
			List<LSfields> Generalfields = lSfieldsRepository.findByisactive(1);
			List<LsMappedInstruments> Instruments = lsMappedInstrumentsRepository.findAll();
			List<InstrumentMaster> InstrMaster = lsInstMasterRepository.findByStatusAndSite(1, lssiteMaster);
			List<LsMappedTemplate> MappedTemplate = LsMappedTemplateRepository.findAll();
			List<LsUnmappedTemplate> UnmappedTemplate = LsUnmappedTemplateRepository.findAll();

			List<Method> elnMethod = lsMethodRepository.findByStatusAndSite(1, lssiteMaster);
			List<ParserBlock> ParserBlock = lsParserBlockRepository.findByStatusAndMethodIn(1, elnMethod);
			List<ParserField> ParserField = lsParserRepository.findByStatusAndParserblockIn(1, ParserBlock);
			List<SubParserField> SubParserField = lsSubParserRepository.findByStatusAndParserfieldIn(1, ParserField);

			// SubParserField = lsSubParserRepository.findByStatus(1);
			obj.put("Generalfields", Generalfields);

			List<ParserField> filteredList = ParserField.stream()
					.filter(filterParser -> SubParserField.stream()
							.anyMatch(filterSubParser -> filterParser.getParserfieldkey()
									.equals(filterSubParser.getParserfield().getParserfieldkey())))
					.collect(Collectors.toList());

			ParserField.removeAll(filteredList);

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

			Generalfields = null;
			Instruments = null;
			InstrMaster = null;
			elnMethod = null;
			ParserBlock = null;
			ParserField = null;
			// SubParserField = null;

		} else {
			List<LSfields> Generalfields = lSfieldsRepository.findByisactiveAndMethodname(1, "ID_GENERAL");

			List<InstrumentMaster> InstrMaster = lsInstMasterRepository.findByStatusAndSite(1, lssiteMaster);
			List<Method> elnMethod = lsMethodRepository.findByStatusAndSite(1, lssiteMaster);
			List<ParserBlock> ParserBlock = lsParserBlockRepository.findByStatusAndMethodIn(1, elnMethod);
			List<ParserField> ParserField = lsParserRepository.findByStatusAndParserblockIn(1, ParserBlock);

			List<SubParserField> SubParserField = lsSubParserRepository.findByStatusAndParserfieldIn(1, ParserField);

			obj.put("Generalfields", Generalfields);

			List<ParserField> filteredList = ParserField.stream()
					.filter(filterParser -> SubParserField.stream()
							.anyMatch(filterSubParser -> filterParser.getParserfieldkey()
									.equals(filterSubParser.getParserfield().getParserfieldkey())))
					.collect(Collectors.toList());

			ParserField.removeAll(filteredList);

			obj.put("Instrmaster", InstrMaster);
			obj.put("ELNMethods", elnMethod);
			obj.put("ParserBlock", ParserBlock);
			obj.put("ParserField", ParserField);
			obj.put("SubParserField", SubParserField);

			Generalfields = null;
			InstrMaster = null;
			elnMethod = null;
			ParserBlock = null;
			ParserField = null;
		}
		if (LSpreferencesRepository.findByTasksettings("WebParser") != null && LSpreferencesRepository
				.findByTasksettings("WebParser").getValuesettings().equalsIgnoreCase("Active")) {
			obj.put("Methods", parserService.getwebparsemethods());
			obj.put("Instruments", parserService.getwebparserInstruments());
		} else {
			obj.put("Methods", Methods);
		}
		Methods = null;
		lsInst = null;
		return obj;
	}

//	public LSlogilablimsorderdetail InsertELNOrder(LSlogilablimsorderdetail objorder) {
//
//		objorder.setLsworkflow(lsworkflowRepository
//				.findTopByAndLssitemasterOrderByWorkflowcodeAsc(objorder.getLsuserMaster().getLssitemaster()));
//
//		objorder.setOrderflag("N");
//
//		String Content = "";
//		String defaultContent = "{\"activeSheet\":\"Sheet1\",\"sheets\":[{\"name\":\"Sheet1\",\"rows\":[],\"columns\":[],\"selection\":\"A1:A1\",\"activeCell\":\"A1:A1\",\"frozenRows\":0,\"frozenColumns\":0,\"showGridLines\":true,\"gridLinesColor\":null,\"mergedCells\":[],\"hyperlinks\":[],\"defaultCellStyle\":{\"fontFamily\":\"Arial\",\"fontSize\":\"12\"},\"drawings\":[]}],\"names\":[],\"columnWidth\":64,\"rowHeight\":20,\"images\":[],\"charts\":[],\"tags\":[],\"fieldcount\":0,\"Batchcoordinates\":{\"resultdirection\":1,\"parameters\":[]}}";
//
//		if (objorder.getLsfile() != null) {
//			if ((objorder.getLsfile().getApproved() != null && objorder.getLsfile().getApproved() == 1)
//					|| (objorder.getFiletype() == 5)) {
//				if (objorder.getIsmultitenant() == 1) {
//					CloudSheetCreation file = cloudSheetCreationRepository
//							.findById((long) objorder.getLsfile().getFilecode());
//					if (file != null) {
//						Content = file.getContent();
//					} else {
//						Content = objorder.getLsfile().getFilecontent();
//					}
//				} else {
//
//					String fileid = "file_" + objorder.getLsfile().getFilecode();
//					GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileid)));
//					if (largefile == null) {
//						largefile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileid)));
//					}
//
//					if (largefile != null) {
//						String filecontent = new BufferedReader(
//								new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
//								.collect(Collectors.joining("\n"));
//						Content = filecontent;
//					} else {
//
//						SheetCreation file = mongoTemplate.findById(objorder.getLsfile().getFilecode(),
//								SheetCreation.class);
//						if (file != null) {
//							Content = file.getContent();
//						} else {
//							Content = objorder.getLsfile().getFilecontent();
//						}
//					}
//
//				}
//			} else {
//				if (objorder.getFiletype() != 4 && objorder.getLsfile().getFilecode() != 1) {
//					Integer lastapprovedvesrion = objorder.getLsfile().getVersionno() > 1
//							? (objorder.getLsfile().getVersionno() - 1)
//							: objorder.getLsfile().getVersionno();
//					objorder.getLsfile().setVersionno(lastapprovedvesrion);
//					objorder.getLsfile().setIsmultitenant(objorder.getIsmultitenant());
//					objorder.getLsfile().setIsmultitenant(objorder.getIsmultitenant());
//					Content = fileService.GetfileverContent(objorder.getLsfile());
//				}
//			}
//
//		}
//
//		if (Content == null || Content.equals("")) {
//			Content = defaultContent;
//		}
//
//		if (objorder.getLssamplefile().getLssamplefileversion() != null) {
//
//			String Contentversion = Content;
//			lssamplefileversionRepository.save(objorder.getLssamplefile().getLssamplefileversion());
//			updateorderversioncontent(Contentversion, objorder.getLssamplefile().getLssamplefileversion().get(0),
//					objorder.getIsmultitenant());
//		}
//
//		lssamplefileRepository.save(objorder.getLssamplefile());
//
//		if (objorder.getAssignedto() != null) {
//			objorder.setLockeduser(objorder.getAssignedto().getUsercode());
//			objorder.setLockedusername(objorder.getAssignedto().getUsername());
//		}
//
//		lslogilablimsorderdetailRepository.save(objorder);
//
//		String Batchid = "ELN" + objorder.getBatchcode();
//
//		if (objorder.getFiletype() == 3) {
//			Batchid = "RESEARCH" + objorder.getBatchcode();
//		} else if (objorder.getFiletype() == 4) {
//			Batchid = "EXCEL" + objorder.getBatchcode();
//		} else if (objorder.getFiletype() == 5) {
//			Batchid = "VALIDATE" + objorder.getBatchcode();
//		}
//		lslogilablimsorderdetailRepository.setbatchidBybatchcode(Batchid, objorder.getBatchcode());
//		objorder.setBatchid(Batchid);
//
//		lslogilablimsorderdetailRepository.save(objorder);
//
//		lssamplefileRepository.setbatchcodeOnsamplefile(objorder.getBatchcode(),
//				objorder.getLssamplefile().getFilesamplecode());
//
//		List<LSlimsorder> lsorder = new ArrayList<LSlimsorder>();
//		String Limsorder = objorder.getBatchcode().toString();
//		if (objorder.getLsfile() != null) {
//			objorder.getLsfile().setLsmethods(
//					LSfilemethodRepository.findByFilecodeOrderByFilemethodcode(objorder.getLsfile().getFilecode()));
//			if (objorder.getLsfile().getLsmethods() != null && objorder.getLsfile().getLsmethods().size() > 0) {
//				int methodindex = 0;
//				for (LSfilemethod objmethod : objorder.getLsfile().getLsmethods()) {
//					LSlimsorder objLimsOrder = new LSlimsorder();
//					String order = "";
//					if (methodindex < 10) {
//						order = Limsorder.concat("0" + methodindex);
//					} else {
//						order = Limsorder.concat("" + methodindex);
//					}
//					objLimsOrder.setOrderid(Long.parseLong(order));
//					objLimsOrder.setBatchid(objorder.getBatchid());
//					objLimsOrder.setMethodcode(objmethod.getMethodid());
//					objLimsOrder.setInstrumentcode(objmethod.getInstrumentid());
//					objLimsOrder.setTestcode(objorder.getTestcode() != null ? objorder.getTestcode().toString() : null);
//					objLimsOrder.setOrderflag("N");
//
//					lsorder.add(objLimsOrder);
//					methodindex++;
//				}
//
//				lslimsorderRepository.save(lsorder);
//			} else {
//				LSfilemethod lsfilemethod = LSfilemethodRepository.findByFilecode(objorder.getLsfile().getFilecode());
//				LSlimsorder objLimsOrder = new LSlimsorder();
//				if (lsfilemethod != null) {
//					objLimsOrder.setMethodcode(lsfilemethod.getMethodid());
//					objLimsOrder.setInstrumentcode(lsfilemethod.getInstrumentid());
//				}
//				objLimsOrder.setOrderid(Long.parseLong(Limsorder.concat("00")));
//				objLimsOrder.setBatchid(objorder.getBatchid());
//				objLimsOrder.setTestcode(objorder.getTestcode() != null ? objorder.getTestcode().toString() : null);
//				objLimsOrder.setOrderflag("N");
//
//				lslimsorderRepository.save(objLimsOrder);
//			}
//		}
//
//		if (objorder.getLssamplefile() != null) {
//			updateordercontent(Content, objorder.getLssamplefile(), objorder.getIsmultitenant());
//		}
//
//		updatenotificationfororder(objorder);
//
//		objorder.setLstworkflow(objorder.getLstworkflow());
//
//		return objorder;
//	}
	public static String generateUniqueTimerId() {
		return UUID.randomUUID().toString();
	}


	public LSlogilablimsorderdetail InsertAutoRegisterOrder(LSlogilablimsorderdetail objorderindex,
			String timerId1) throws IOException, ParseException {

		List<LSlogilablimsorderdetail> oldorder = lslogilablimsorderdetailRepository
				.findBybatchcode(objorderindex.getBatchcode());
		LSlogilablimsorderdetail objorderindex1 = oldorder.get(0);

//		LSlogilablimsorderdetail orderdetail = null;
		List<LsAutoregister> autoorder = lsautoregisterrepo.findByBatchcodeAndScreen(objorderindex1.getBatchcode(),
				objorderindex1.getLsautoregisterorders().getScreen());
		Integer Ismultitenant = autoorder.get(0).getIsmultitenant();

		Integer autoregistercount = objorderindex1.getAutoregistercount();
		if(autoorder != null) {
			autoorder.get(0).setRepeat(false);
			lsautoregisterrepo.save(autoorder.get(0));
		}
		
		if (autoregistercount > 0) {
//			Integer autoregistercountObj = objorderindex1.getAutoregistercount() - 1;
			objorderindex1.setRepeat(false);
			objorderindex1.setAutoregistercount(0);
			lslogilablimsorderdetailRepository.save(objorderindex1);
			
			
		}
		if (autoregistercount != null && autoregistercount > 0) {
			autoregistercount = autoregistercount - 1;
			List<LsAutoregister> listauto = new ArrayList<LsAutoregister>();

			if (autoorder.get(0).getBatchcode().equals(objorderindex1.getBatchcode())) {

				Date currentdate = commonfunction.getCurrentUtcTime();
				if (autoorder.get(0).getDelayinminutes() != null) {
					Instant now = Instant.now();
					Instant futureInstant = now.plus(Duration.ofMillis(autoorder.get(0).getDelayinminutes()));
					Date futureDate = Date.from(futureInstant);
					autoorder.get(0).setAutocreatedate(futureDate);
				} else {
					if (autoorder.get(0).getTimespan().equals("Days")) {
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(currentdate);
						calendar.add(Calendar.DAY_OF_MONTH, autoorder.get(0).getInterval());

						Date futureDate = calendar.getTime();
						autoorder.get(0).setAutocreatedate(futureDate);
					} else if (autoorder.get(0).getTimespan().equals("Week")) {

						Calendar calendar = Calendar.getInstance();
						calendar.setTime(currentdate);
						calendar.add(Calendar.DAY_OF_MONTH, (autoorder.get(0).getInterval() * 7));

						Date futureDate = calendar.getTime();
						autoorder.get(0).setAutocreatedate(futureDate);
					} else {
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(currentdate);
						calendar.add(Calendar.MINUTE, (10));
						Date futureDate = calendar.getTime();
						autoorder.get(0).setAutocreatedate(futureDate);
					}

				}
				autoorder.get(0).setRegcode(null);
				autoorder.get(0).setBatchcode(null);
				autoorder.get(0).setStoptime(null);
				autoorder.get(0).setScreen("Sheet_Order");
//				if (autoregistercount == 0) {
					autoorder.get(0).setIsautoreg(false);
//				} else {
//					autoorder.get(0).setIsautoreg(true);
//				}

				listauto.add(autoorder.get(0));
				objorderindex1.setLsautoregisterorders(listauto.get(0));

			}

			listauto.get(0).setTimerIdname(timerId1);
			lsautoregisterrepo.save(listauto);
			timerStatusMap.put(timerId1, true);
//			objorderindex.setAutoregistercount(autoregistercount);
			// lslogilablimsorderdetailRepository.save(objorderindex);

			objorderindex1.setLsworkflow(lsworkflowRepository.findTopByAndLssitemasterOrderByWorkflowcodeAsc(
					objorderindex1.getLsuserMaster().getLssitemaster()));

			objorderindex1.setOrderflag("N");

			String Content = "";
			String defaultContent = "{\"activeSheet\":\"Sheet1\",\"sheets\":[{\"name\":\"Sheet1\",\"rows\":[],\"columns\":[],\"selection\":\"A1:A1\",\"activeCell\":\"A1:A1\",\"frozenRows\":0,\"frozenColumns\":0,\"showGridLines\":true,\"gridLinesColor\":null,\"mergedCells\":[],\"hyperlinks\":[],\"defaultCellStyle\":{\"fontFamily\":\"Arial\",\"fontSize\":\"12\"},\"drawings\":[]}],\"names\":[],\"columnWidth\":64,\"rowHeight\":20,\"images\":[],\"charts\":[],\"tags\":[],\"fieldcount\":0,\"Batchcoordinates\":{\"resultdirection\":1,\"parameters\":[]}}";

			if (objorderindex1.getLsfile() != null) {
				if ((objorderindex1.getLsfile().getApproved() != null && objorderindex1.getLsfile().getApproved() == 1)
						|| (objorderindex1.getFiletype() == 5)) {
					if (Ismultitenant == 1 || Ismultitenant == 2) {

						CloudSheetCreation objCreation = cloudSheetCreationRepository
								.findById((long) objorderindex1.getLsfile().getFilecode());

						if (objCreation != null) {
							if (objCreation.getContainerstored() == 0) {
								Content = cloudSheetCreationRepository
										.findById((long) objorderindex1.getLsfile().getFilecode()).getContent();
							} else {
								try {
									Content = objCloudFileManipulationservice.retrieveCloudSheets(
											objCreation.getFileuid(), commonfunction.getcontainername(Ismultitenant,
													TenantContext.getCurrentTenant()) + "sheetcreation");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else {
							Content = objorderindex1.getLsfile().getFilecontent();
						}
					} else {

						GridFSDBFile largefile = gridFsTemplate.findOne(new Query(
								Criteria.where("filename").is("file_" + objorderindex1.getLsfile().getFilecode())));
						if (largefile == null) {
							largefile = gridFsTemplate.findOne(new Query(
									Criteria.where("_id").is("file_" + objorderindex1.getLsfile().getFilecode())));
						}

						if (largefile != null) {
							Content = new BufferedReader(
									new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
									.collect(Collectors.joining("\n"));
						} else {
							if (mongoTemplate.findById(objorderindex1.getLsfile().getFilecode(),
									SheetCreation.class) != null) {
								Content = mongoTemplate
										.findById(objorderindex1.getLsfile().getFilecode(), SheetCreation.class)
										.getContent();
							} else {
								Content = objorderindex1.getLsfile().getFilecontent();
							}
						}

					}
				} else {
					if (objorderindex1.getFiletype() != 4 && objorderindex1.getLsfile().getFilecode() != 1) {
						Integer lastapprovedvesrion = objorderindex1.getLsfile().getVersionno() > 1
								? (objorderindex1.getLsfile().getVersionno() - 1)
								: objorderindex1.getLsfile().getVersionno();
						objorderindex1.getLsfile().setVersionno(lastapprovedvesrion);
						objorderindex1.getLsfile().setIsmultitenant(Ismultitenant);
						objorderindex1.getLsfile().setIsmultitenant(Ismultitenant);
						try {
							Content = fileService.GetfileverContent(objorderindex1.getLsfile());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}

			if (Content == null || Content.equals("")) {
				Content = defaultContent;
			}
			LSsamplefileversion sampversion = lssamplefileversionRepository
					.findByBatchcode(objorderindex1.getBatchcode());
			objorderindex1.getLssamplefile().setFilesamplecode(null);
			objorderindex1.getLssamplefile().setBatchcode(0);

			if (sampversion != null) {
				sampversion.setBatchcode(0);
				sampversion.setFilesamplecode(null);

				String Contentversion = Content;
				lssamplefileversionRepository.save(sampversion);
				try {
					updateorderversioncontent(Contentversion, sampversion, Ismultitenant);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Contentversion = null;
			} else {
				LSsamplefileversion sampversion1 = new LSsamplefileversion();
				sampversion1.setCreatebyuser(objorderindex1.getLsuserMaster());
				sampversion1.setCreateby(objorderindex1.getLsuserMaster().getUsercode());
				sampversion1.setCreatedate(commonfunction.getCurrentUtcTime());
				sampversion1.setVersionname("version_1");
				sampversion1.setVersionno(1);
				lssamplefileversionRepository.save(sampversion1);
				
				List<LSsamplefileversion> samplefileVersions = new ArrayList<LSsamplefileversion>();
				samplefileVersions.add(sampversion1);
				
				LSsamplefile samplefile = new LSsamplefile();
				samplefile.setCreatebyuser(objorderindex1.getLsuserMaster());
				samplefile.setLssamplefileversion(samplefileVersions);
//				samplefile.setCreatedate(commonfunction.getCurrentUtcTime());
				samplefile.setVersionno(1);
				
				objorderindex1.setLssamplefile(samplefile);
			}
			try {
				objorderindex1.getLssamplefile().setCreatedate(commonfunction.getCurrentUtcTime());
				if (sampversion != null) {
					sampversion.setCreatedate(commonfunction.getCurrentUtcTime());
				}
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			lssamplefileRepository.save(objorderindex1.getLssamplefile());

//			if (objorderindex.getAssignedto() != null) {
//				objorderindex.setLockeduser(objorderindex.getAssignedto().getUsercode());
//				objorderindex.setLockedusername(objorderindex.getAssignedto().getUsername());
//			}

			try {
				objorderindex1.setCreatedtimestamp(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			objorderindex1.setBatchcode(null);
			objorderindex1.setBatchid(null);

			lslogilablimsorderdetailRepository.save(objorderindex1);
			if (objorderindex1.getLsautoregisterorders() != null) {
				objorderindex1.getLsautoregisterorders().setBatchcode(objorderindex1.getBatchcode());
				if (autoregistercount == 0) {
					autoorder.get(0).setIsautoreg(false);
				} else {
					autoorder.get(0).setIsautoreg(true);
				}
				lsautoregisterrepo.save(objorderindex1.getLsautoregisterorders());
			}

			String Batchid = "ELN" + objorderindex1.getBatchcode();
			if (objorderindex1.getFiletype() == 3) {
				Batchid = "RESEARCH" + objorderindex1.getBatchcode();
			} else if (objorderindex1.getFiletype() == 4) {
				Batchid = "EXCEL" + objorderindex1.getBatchcode();
			} else if (objorderindex1.getFiletype() == 5) {
				Batchid = "VALIDATE" + objorderindex1.getBatchcode();
			}
			lslogilablimsorderdetailRepository.setbatchidBybatchcode(Batchid, objorderindex1.getBatchcode());
			objorderindex1.setBatchid(Batchid);
			objorderindex1.setRepeat(autoregistercount == 0 ? false : true);
			objorderindex1.setAutoregistercount(autoregistercount);
			lslogilablimsorderdetailRepository.save(objorderindex1);

			List<LSlogilablimsorder> lsorder = new ArrayList<LSlogilablimsorder>();
			String Limsorder = objorderindex1.getBatchcode().toString();

			if (objorderindex1.getLsfile() != null) {
				objorderindex1.getLsfile().setLsmethods(LSfilemethodRepository
						.findByFilecodeOrderByFilemethodcode(objorderindex1.getLsfile().getFilecode()));
				if (objorderindex1.getLsfile().getLsmethods() != null
						&& objorderindex1.getLsfile().getLsmethods().size() > 0) {
					int methodindex = 0;
					for (LSfilemethod objmethod : objorderindex1.getLsfile().getLsmethods()) {
						LSlogilablimsorder objLimsOrder = new LSlogilablimsorder();
						String order = "";
						if (methodindex < 10) {
							order = Limsorder.concat("0" + methodindex);
						} else {
							order = Limsorder.concat("" + methodindex);
						}
						objLimsOrder.setOrderid(Long.parseLong(order));
						objLimsOrder.setBatchid(objorderindex1.getBatchid());
						objLimsOrder.setMethodcode(objmethod.getMethodid());
						objLimsOrder.setInstrumentcode(objmethod.getInstrumentid());
						objLimsOrder.setTestcode(
								objorderindex1.getTestcode() != null ? objorderindex1.getTestcode().toString() : null);
						objLimsOrder.setOrderflag("N");
						objLimsOrder.setCreatedtimestamp(objorderindex1.getCreatedtimestamp());

						lsorder.add(objLimsOrder);

						methodindex++;
					}

					lslogilablimsorderRepository.save(lsorder);
				} else {

					LSlogilablimsorder objLimsOrder = new LSlogilablimsorder();
					if (LSfilemethodRepository.findByFilecode(objorderindex1.getLsfile().getFilecode()) != null) {
						objLimsOrder.setMethodcode(LSfilemethodRepository
								.findByFilecode(objorderindex1.getLsfile().getFilecode()).getMethodid());
						objLimsOrder.setInstrumentcode(LSfilemethodRepository
								.findByFilecode(objorderindex1.getLsfile().getFilecode()).getInstrumentid());
					}
					objLimsOrder.setOrderid(Long.parseLong(Limsorder.concat("00")));
					objLimsOrder.setBatchid(objorderindex1.getBatchid());
					objLimsOrder.setTestcode(
							objorderindex1.getTestcode() != null ? objorderindex1.getTestcode().toString() : null);
					objLimsOrder.setOrderflag("N");
					objLimsOrder.setCreatedtimestamp(objorderindex1.getCreatedtimestamp());

					lslogilablimsorderRepository.save(objLimsOrder);
					lsorder.add(objLimsOrder);

				}
			}

			lssamplefileRepository.setbatchcodeOnsamplefile(objorderindex1.getBatchcode(),
					objorderindex1.getLssamplefile().getFilesamplecode());

			final List<LSOrdernotification> ordernotList = new ArrayList<>(1);
			if (objorderindex1.getCautiondate() != null && objorderindex1.getDuedate() != null) {
				LSOrdernotification notobj = new LSOrdernotification();

				notobj.setBatchcode(objorderindex1.getBatchcode());
				notobj.setBatchid(objorderindex1.getBatchid());
				notobj.setCautiondate(objorderindex1.getCautiondate());
				notobj.setCreatedtimestamp(objorderindex1.getCreatedtimestamp());
				notobj.setDuedate(objorderindex1.getDuedate());
				notobj.setPeriod(objorderindex1.getPeriod());
				notobj.setUsercode(objorderindex1.getLsuserMaster().getUsercode());
				notobj.setCautionstatus(1);
				notobj.setDuestatus(1);
				notobj.setOverduestatus(1);
				notobj.setScreen("sheetorder");
				// lsordernotificationrepo.save(notobj);
				ordernotList.add(lsordernotificationrepo.save(notobj));
				if (ordernotList.size() > 0) {
					objorderindex1.setLsordernotification(ordernotList.get(0));
				}
			}
			objorderindex1.setOrdercancell(null);
			lslogilablimsorderdetailRepository.save(objorderindex1);

//			if (objorderindex1.getRepeat() != null && objorderindex1.getLsautoregisterorders() != null
//					&& objorderindex1.getRepeat()) {
//				try {
//					scheduleAutoregduringregister(objorderindex1, autoorder.get(0).getDelayinminutes(),timerId1);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}

			if (objorderindex1.getLssamplefile() != null) {
				try {
					updateordercontent(Content, objorderindex1.getLssamplefile(), Ismultitenant);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//			List<LSlogilablimsorderdetail> neworder = lslogilablimsorderdetailRepository.findBybatchcode(objorderindex.getBatchcode());
//			neworder.get(0).setOrdercancell(null);
//			lslogilablimsorderdetailRepository.save(neworder);

			objorderindex1.setLstworkflow(objorderindex1.getLstworkflow());

			Content = null;
			defaultContent = null;
			Batchid = null;
			Limsorder = null;
			lsorder = null;
			updatenotificationfororder(objorderindex1);

			// return objorder1;
			LScfttransaction auditobj = new LScfttransaction();
			auditobj.setLsuserMaster(objorderindex1.getLsuserMaster().getUsercode());
			try {
				auditobj.setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			auditobj.setModuleName("IDS_SCN_SHEETORDERS");
			auditobj.setActions("IDS_TSK_REGISTERED");
			auditobj.setManipulatetype("IDS_AUDIT_INSERTORDERS");
			auditobj.setComments("order: " + objorderindex1.getBatchid() + " is now autoregistered");
			auditobj.setLssitemaster(objorderindex1.getLsuserMaster().getLssitemaster().getSitecode());
			auditobj.setSystemcoments("Audittrail.Audittrailhistory.Audittype.IDS_AUDIT_SYSTEMGENERATED");
			lscfttransactionRepository.save(auditobj);

		} else {
			String timerId = autoorder.get(0).getTimerIdname();
			if (timerId != null) {
				stopTimer(timerId);

			}

		}

		return objorderindex1;
	}

	
	 public void stopTimer(String timerId) {
	        Timer timer = timerMap.get(timerId);
	        if (timer != null) {
	            timer.cancel();
	            timer.purge(); 
	            timerMap.remove(timerId); 
	            timerStatusMap.remove(timerId);
	            orderDetailMap.remove(timerId);
	            System.out.println("Timer " + timerId + " stopped.");
	        } else {
	            System.out.println("No timer found with ID " + timerId);
	        }
	    }
	
	public LSlogilablimsorderdetail InsertELNOrder(LSlogilablimsorderdetail objorder)
			throws IOException, ParseException {
		objorder.setLsworkflow(lsworkflowRepository
				.findTopByAndLssitemasterOrderByWorkflowcodeAsc(objorder.getLsuserMaster().getLssitemaster()));

		objorder.setOrderflag("N");

		String Content = "";
		String defaultContent = "{\"activeSheet\":\"Sheet1\",\"sheets\":[{\"name\":\"Sheet1\",\"rows\":[],\"columns\":[],\"selection\":\"A1:A1\",\"activeCell\":\"A1:A1\",\"frozenRows\":0,\"frozenColumns\":0,\"showGridLines\":true,\"gridLinesColor\":null,\"mergedCells\":[],\"hyperlinks\":[],\"defaultCellStyle\":{\"fontFamily\":\"Arial\",\"fontSize\":\"12\"},\"drawings\":[]}],\"names\":[],\"columnWidth\":64,\"rowHeight\":20,\"images\":[],\"charts\":[],\"tags\":[],\"fieldcount\":0,\"Batchcoordinates\":{\"resultdirection\":1,\"parameters\":[]}}";

		if (objorder.getLsfile() != null) {
			if ((objorder.getLsfile().getApproved() != null && objorder.getLsfile().getApproved() == 1)
					|| (objorder.getFiletype() == 5)) {
				if (objorder.getIsmultitenant() == 1 || objorder.getIsmultitenant() == 2) {

					CloudSheetCreation objCreation = cloudSheetCreationRepository
							.findById((long) objorder.getLsfile().getFilecode());

					if (objCreation != null) {
						if (objCreation.getContainerstored() == 0) {
							Content = cloudSheetCreationRepository.findById((long) objorder.getLsfile().getFilecode())
									.getContent();
						} else {
							Content = objCloudFileManipulationservice.retrieveCloudSheets(objCreation.getFileuid(),
									commonfunction.getcontainername(objorder.getIsmultitenant(),
											TenantContext.getCurrentTenant()) + "sheetcreation");
						}
					} else {
						Content = objorder.getLsfile().getFilecontent();
					}
				} else {

					GridFSDBFile largefile = gridFsTemplate.findOne(
							new Query(Criteria.where("filename").is("file_" + objorder.getLsfile().getFilecode())));
					if (largefile == null) {
						largefile = gridFsTemplate.findOne(
								new Query(Criteria.where("_id").is("file_" + objorder.getLsfile().getFilecode())));
					}

					if (largefile != null) {
						Content = new BufferedReader(
								new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
								.collect(Collectors.joining("\n"));
					} else {
						if (mongoTemplate.findById(objorder.getLsfile().getFilecode(), SheetCreation.class) != null) {
							Content = mongoTemplate.findById(objorder.getLsfile().getFilecode(), SheetCreation.class)
									.getContent();
						} else {
							Content = objorder.getLsfile().getFilecontent();
						}
					}

				}
			} else {
				if (objorder.getFiletype() != 4 && objorder.getLsfile().getFilecode() != 1) {
					Integer lastapprovedvesrion = objorder.getLsfile().getVersionno() > 1
							? (objorder.getLsfile().getVersionno() - 1)
							: objorder.getLsfile().getVersionno();
					objorder.getLsfile().setVersionno(lastapprovedvesrion);
					objorder.getLsfile().setIsmultitenant(objorder.getIsmultitenant());
					objorder.getLsfile().setIsmultitenant(objorder.getIsmultitenant());
					Content = fileService.GetfileverContent(objorder.getLsfile());
				}
			}

		}

		if (Content == null || Content.equals("")) {
			Content = defaultContent;
		}

		if (objorder.getLssamplefile().getLssamplefileversion() != null) {

			String Contentversion = Content;
			lssamplefileversionRepository.save(objorder.getLssamplefile().getLssamplefileversion());
			updateorderversioncontent(Contentversion, objorder.getLssamplefile().getLssamplefileversion().get(0),
					objorder.getIsmultitenant());

			Contentversion = null;
		}
		try {
			objorder.getLssamplefile().setCreatedate(commonfunction.getCurrentUtcTime());
			if (objorder.getLssamplefile().getLssamplefileversion().get(0) != null) {
				objorder.getLssamplefile().getLssamplefileversion().get(0)
						.setCreatedate(commonfunction.getCurrentUtcTime());
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		lssamplefileRepository.save(objorder.getLssamplefile());

//		if (objorder.getAssignedto() != null) {
//			objorder.setLockeduser(objorder.getAssignedto().getUsercode());
//			objorder.setLockedusername(objorder.getAssignedto().getUsername());
//		}
		try {
			objorder.setCreatedtimestamp(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		lslogilablimsorderdetailRepository.save(objorder);
		long milliseconds = 0;
		String timerId1 = generateUniqueTimerId();
		if (objorder.getRepeat() != null && objorder.getLsautoregisterorderdetail() != null && objorder.getRepeat()) {
			final List<LsAutoregister> autoordernotList = new ArrayList<>(1);

			Map<String, Object> RtnObject = commonfunction.getdelaymillisecondforautoregister( objorder.getLsautoregisterorderdetail().getTimespan(),objorder.getLsautoregisterorderdetail().getInterval());
			Date updatedDate = (Date) RtnObject.get("Date");
			milliseconds = (long) RtnObject.get("delay");
			objorder.getLsautoregisterorderdetail().setAutocreatedate(updatedDate);
			objorder.getLsautoregisterorderdetail().setBatchcode(objorder.getBatchcode());
			objorder.getLsautoregisterorderdetail().setRepeat(true);
			objorder.getLsautoregisterorderdetail().setDelayinminutes(milliseconds);
			objorder.getLsautoregisterorderdetail().setTimerIdname(timerId1);
//			if(objorder.getAutoregistercount()!=null && objorder.getAutoregistercount() >0) {
//				objorder.getLsautoregisterorderdetail().setIsautoreg(true);
//			}
			autoordernotList.add(lsautoregisterrepo.save(objorder.getLsautoregisterorderdetail()));
			objorder.setLsautoregisterorders(autoordernotList.get(0));
			lslogilablimsorderdetailRepository.save(objorder);
		}

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

		List<LSlogilablimsorder> lsorder = new ArrayList<LSlogilablimsorder>();
		String Limsorder = objorder.getBatchcode().toString();

		if (objorder.getLsfile() != null) {
			objorder.getLsfile().setLsmethods(
					LSfilemethodRepository.findByFilecodeOrderByFilemethodcode(objorder.getLsfile().getFilecode()));
			if (objorder.getLsfile().getLsmethods() != null && objorder.getLsfile().getLsmethods().size() > 0) {
				int methodindex = 0;
				for (LSfilemethod objmethod : objorder.getLsfile().getLsmethods()) {
					LSlogilablimsorder objLimsOrder = new LSlogilablimsorder();
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
					objLimsOrder.setCreatedtimestamp(objorder.getCreatedtimestamp());

					lsorder.add(objLimsOrder);

					methodindex++;
				}

				lslogilablimsorderRepository.save(lsorder);
			} else {

				LSlogilablimsorder objLimsOrder = new LSlogilablimsorder();
				if (LSfilemethodRepository.findByFilecode(objorder.getLsfile().getFilecode()) != null) {
					objLimsOrder.setMethodcode(
							LSfilemethodRepository.findByFilecode(objorder.getLsfile().getFilecode()).getMethodid());
					objLimsOrder.setInstrumentcode(LSfilemethodRepository
							.findByFilecode(objorder.getLsfile().getFilecode()).getInstrumentid());
				}
				objLimsOrder.setOrderid(Long.parseLong(Limsorder.concat("00")));
				objLimsOrder.setBatchid(objorder.getBatchid());
				objLimsOrder.setTestcode(objorder.getTestcode() != null ? objorder.getTestcode().toString() : null);
				objLimsOrder.setOrderflag("N");
				objLimsOrder.setCreatedtimestamp(objorder.getCreatedtimestamp());

				lslogilablimsorderRepository.save(objLimsOrder);
				lsorder.add(objLimsOrder);

			}
		}

		final List<LSOrdernotification> ordernotList = new ArrayList<>(1);
		if (objorder.getCautiondate() != null && objorder.getDuedate() != null) {
			LSOrdernotification notobj = new LSOrdernotification();

			notobj.setBatchcode(objorder.getBatchcode());
			notobj.setBatchid(objorder.getBatchid());
			notobj.setCautiondate(objorder.getCautiondate());
			notobj.setCreatedtimestamp(objorder.getCreatedtimestamp());
			notobj.setDuedate(objorder.getDuedate());
			notobj.setPeriod(objorder.getPeriod());
			notobj.setUsercode(objorder.getLsuserMaster().getUsercode());
			notobj.setCautionstatus(1);
			notobj.setDuestatus(1);
			notobj.setOverduestatus(1);
			notobj.setScreen("sheetorder");
			notobj.setIscompleted(false);
			// lsordernotificationrepo.save(notobj);
			ordernotList.add(lsordernotificationrepo.save(notobj));
			if (ordernotList.size() > 0) {
				objorder.setLsordernotification(ordernotList.get(0));
				try {
					loginservice.ValidateNotification(ordernotList.get(0));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			Notification notify = new Notification();
			notify.setBatchid(objorder.getBatchid());
			notify.setOrderid(objorder.getBatchcode());
			notify.setLsusermaster(objorder.getLsuserMaster());
			notify.setAddedby(objorder.getLsuserMaster().getUsername());
			notify.setUsercode(objorder.getLsuserMaster().getUsercode());
			notify.setSitecode(objorder.getLsuserMaster().getLssitemaster().getSitecode());
			notify.setScreen("sheetorder");
			notify.setCurrentdate(commonfunction.getCurrentUtcTime());
			notify.setCautiondate(objorder.getCautiondate());
			notify.setDuedate(objorder.getDuedate());
			notify.setAddedon(commonfunction.getCurrentUtcTime());
			notify.setStatus(1);
			notificationRepository.save(notify);
		}
		lslogilablimsorderdetailRepository.save(objorder);
		if (objorder.getRepeat() != null && objorder.getLsautoregisterorderdetail() != null && objorder.getRepeat()) {
			try {
				scheduleAutoregduringregister(objorder, milliseconds, timerId1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (objorder.getLssamplefile() != null) {
			updateordercontent(Content, objorder.getLssamplefile(), objorder.getIsmultitenant());
		}

		objorder.setLstworkflow(objorder.getLstworkflow());

		final LSlogilablimsorderdetail objLSlogilablimsorder = objorder;

		new Thread(() -> {
			try {
				System.out.println("inside the thread SDMS order call");
				createLogilabLIMSOrder4SDMS(objLSlogilablimsorder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		Content = null;
		defaultContent = null;
		Batchid = null;
		Limsorder = null;
		lsorder = null;
		updatenotificationfororder(objorder);
		return objorder;
	}

//	public void ValidateAutoRegister(LSlogilablimsorderdetail objlogilaborderdetail,long milliseconds) throws ParseException {
//		// lsordernotificationrepo.save(objnotification);
//		scheduleAutoregduringregister(objlogilaborderdetail,milliseconds);
//	}

	public void scheduleAutoregduringregister(LSlogilablimsorderdetail objlogilaborderdetail, long milliseconds,
			String timerId1) throws ParseException {
		Date AutoCreateDate = objlogilaborderdetail.getLsautoregisterorders().getAutocreatedate();
		Instant autocreate = AutoCreateDate.toInstant();
		LocalDateTime AutoCreateTime = LocalDateTime.ofInstant(autocreate, ZoneId.systemDefault());
		LocalDateTime currentTime = LocalDateTime.now();

		if (AutoCreateTime.isAfter(currentTime) && objlogilaborderdetail != null) {
//			Duration duration = Duration.between(currentTime, AutoCreateTime);
//			long delay = duration.toMillis();
			long delay = milliseconds;
			scheduleAutoRegister(objlogilaborderdetail, delay, timerId1);
		}
	}

	private void scheduleAutoRegister(LSlogilablimsorderdetail objlogilaborderdetail, long delay, String timerId1) {
		Set<Integer> runningTasks = new HashSet<>();
	
		int batchcode = objlogilaborderdetail.getBatchcode().intValue();
		synchronized (runningTasks) {
			if (runningTasks.contains(batchcode)) {
				System.out.println("Task already scheduled or running for batch ID: " + batchcode);
				return;
			}
			if ((objlogilaborderdetail.getRepeat() != null && objlogilaborderdetail.getRepeat() != false)) {
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						try {
							LSlogilablimsorderdetail objlogilaborderdetailObject=objlogilaborderdetail;
							 Timer timerobj = timerMap.get(timerId1);
							 if (timerobj == null) {
								 timerMap.put(timerId1, timer);
							 }
							 else {
								 if(orderDetailMap.size()>0) {
									 objlogilaborderdetailObject=orderDetailMap.get(timerId1);
								 }
								 if(objlogilaborderdetailObject == null) {
									 objlogilaborderdetailObject=objlogilaborderdetail;
								 }
								 
							 }
							 
							 objlogilaborderdetailObject=InsertAutoRegisterOrder(objlogilaborderdetailObject, timerId1);
							 orderDetailMap.put(timerId1, objlogilaborderdetailObject);
							 System.out.println("kumu");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							synchronized (runningTasks) {
								runningTasks.remove(batchcode);
							}
							scheduledTasks.remove(batchcode);
						}
					}
				};
				runningTasks.add(batchcode);
				 timerMap.put(timerId1, timer);
//	        timer.schedule(task, delay);
				timer.scheduleAtFixedRate(task, delay, delay);
				scheduledTasks.put(batchcode, task);
			}
		}
	}

	private void createLogilabLIMSOrder4SDMS(LSlogilablimsorderdetail objLSlogilablimsorder) throws IOException {

		List<LSlogilablimsorder> lstLSlogilablimsorder = lslogilablimsorderRepository
				.findBybatchid(objLSlogilablimsorder.getBatchid());

		List<Map<String, Object>> lstMaPObject = new ArrayList<Map<String, Object>>();

		lstLSlogilablimsorder.stream().peek(f -> {

			if (f.getInstrumentcode() != null) {

				Map<String, Object> objResMap = new HashMap<>();

				objResMap.put("batchid", f.getBatchid());
				objResMap.put("sampleid", f.getSampleid());
				objResMap.put("testcode", f.getTestcode());
				objResMap.put("methodcode", f.getMethodcode());
				objResMap.put("instrumentcode", f.getInstrumentcode());
				objResMap.put("instrumentname", f.getInstrumentname());
				objResMap.put("orderid", f.getOrderid());

				lstMaPObject.add(objResMap);
			}

		}).collect(Collectors.toList());

		if (!lstMaPObject.isEmpty())
			sdmsServiceCalling("IntegrationSDMS/createLogilabLIMSOrder", lstMaPObject);
	}

	private String sdmsServiceCalling(String uri, List<Map<String, Object>> lstMaPObject) {

		final String url = env.getProperty("sdms.template.service.url") + uri;

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

		String result = restTemplate.postForObject(url, lstMaPObject, String.class);

		return result;

	}

	public Logilabordermaster GetOrderonClose(LSlogilablimsorderdetail objorder) {

		Logilabordermaster objOrder = lslogilablimsorderdetailRepository
				.findByBatchcodeOrderByBatchcodeAsc(objorder.getBatchcode());

		objOrder.setLstworkflow(lslogilablimsorderdetailRepository
				.findByBatchcodeOrderByBatchcodeAsc(objorder.getBatchcode()).getLstworkflow());

		return objOrder;
	}

	public Map<String, Object> getdOrderCount(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapOrders = new HashMap<String, Object>();

		if (objuser.getUsername().equals("Administrator") && objuser.getObjuser().getOrderfor() == 1) {

			mapOrders.put("pendingorder", lslogilablimsorderdetailRepository.countByOrderflag("N"));
			mapOrders.put("completedorder", lslogilablimsorderdetailRepository.countByOrderflag("R"));

		} else {
			long lstlimscompleted = 0;
			if (objuser.getLstproject() != null && objuser.getLstproject().size() > 0) {
				lstlimscompleted = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetween("R", objuser.getLstproject(),
								fromdate, todate);
			}

			long lstpending = 0;
			if (objuser.getLstproject() != null && objuser.getLstproject().size() > 0) {
				lstpending = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetween("N", objuser.getLstproject(),
								fromdate, todate);
			}

			mapOrders.put("pendingorder", (lstpending));
			mapOrders.put("completedorder", (lstlimscompleted));

		}

		fromdate = null;
		todate = null;

		return mapOrders;
	}

	public void updatenotificationfororder(LSlogilablimsorderdetail objorder) {
		try {
			String Details = "";
			String Notifiction = "";
			if (objorder != null && objorder.getLsprojectmaster() != null
					&& objorder.getLsprojectmaster().getLsusersteam() != null && objorder.getAssignedto() == null) {
//				LSusersteam objteam = lsusersteamRepository
//						.findByteamcode(objorder.getLsprojectmaster().getLsusersteam().getTeamcode());

//				LSuserMaster obj = lsuserMasterRepository.findByusercode(objorder.getObjLoggeduser().getUsercode());

				if (lsusersteamRepository.findByteamcode(objorder.getLsprojectmaster().getLsusersteam().getTeamcode())
						.getLsuserteammapping() != null
						&& lsusersteamRepository
								.findByteamcode(objorder.getLsprojectmaster().getLsusersteam().getTeamcode())
								.getLsuserteammapping().size() > 0) {

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

					List<LSuserteammapping> lstusers = lsusersteamRepository
							.findByteamcode(objorder.getLsprojectmaster().getLsusersteam().getTeamcode())
							.getLsuserteammapping();
					List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
					for (int i = 0; i < lstusers.size(); i++) {
						if (!(objorder.getObjLoggeduser().getUsercode()
								.equals(lstusers.get(i).getLsuserMaster().getUsercode()))) {
							LSnotification objnotify = new LSnotification();
							objnotify.setNotifationfrom(
									lsuserMasterRepository.findByusercode(objorder.getObjLoggeduser().getUsercode()));
							objnotify.setNotifationto(lstusers.get(i).getLsuserMaster());
							objnotify.setNotificationdate(commonfunction.getCurrentUtcTime());
							objnotify.setNotification(Notifiction);
							objnotify.setNotificationdetils(Details);
							objnotify.setIsnewnotification(1);
							objnotify.setNotificationpath("/registertask");
							objnotify.setNotificationfor(2);

							lstnotifications.add(objnotify);
						}
					}

					lsnotificationRepository.save(lstnotifications);
				}
			} else if (objorder.getAssignedto() != null) {

				if (objorder.getOrderflag().equalsIgnoreCase("R")) {
					Notifiction = "ORDERCOMPLETEDASSIGN";

					Details = "{\"ordercode\":\"" + objorder.getBatchcode() + "\", \"order\":\"" + objorder.getBatchid()
							+ "\", \"previousworkflow\":\"" + "" + "\", \"previousworkflowcode\":\"" + -1
							+ "\", \"currentworkflow\":\"" + objorder.getLsworkflow().getWorkflowname()
							+ "\", \"assignedto\":\"" + objorder.getAssignedto().getUsername()
							+ "\", \"completeduser\":\"" + objorder.getObjLoggeduser().getUsername()
							+ "\", \"currentworkflowcode\":\"" + objorder.getLsworkflow().getWorkflowcode() + "\"}";

				} else {

					Notifiction = "ORDERCREATIONANDASSIGN";
					Details = "{\"ordercode\":\"" + objorder.getBatchcode() + "\", \"order\":\"" + objorder.getBatchid()
							+ "\", \"previousworkflow\":\"" + "" + "\", \"previousworkflowcode\":\"" + -1
							+ "\", \"currentworkflow\":\"" + objorder.getLsworkflow().getWorkflowname()
							+ "\", \"assignedto\":\"" + objorder.getAssignedto().getUsername() + "\", \"assignedby\":\""
							+ objorder.getObjLoggeduser().getUsername() + "\", \"currentworkflowcode\":\""
							+ objorder.getLsworkflow().getWorkflowcode() + "\"}";
				}

				LSnotification objnotify = new LSnotification();
				objnotify.setNotifationfrom(
						lsuserMasterRepository.findByusercode(objorder.getObjLoggeduser().getUsercode()));
				objnotify.setNotifationto(objorder.getAssignedto());
				objnotify.setNotificationdate(commonfunction.getCurrentUtcTime());
				objnotify.setNotification(Notifiction);
				objnotify.setNotificationdetils(Details);
				objnotify.setIsnewnotification(1);
				objnotify.setNotificationpath("/registertask");
				objnotify.setNotificationfor(1);
				lsnotificationRepository.save(objnotify);

			}
			Details = null;
			Notifiction = null;
		} catch (Exception e) {

		}
	}

	public List<LSlogilablimsorderdetail> GetsharedordersonFilter(LSlogilablimsorderdetail objorder,
			List<LSlogilablimsorderdetail> lstsharedorder) {
//		List<Logilaborders> lstorder = new ArrayList<Logilaborders>();

		List<LSlogilablimsorderdetail> lstlogilab = new ArrayList<LSlogilablimsorderdetail>();

		List<Long> lstBatchcode = new ArrayList<Long>();
		List<Integer> lstsamplefilecode = new ArrayList<Integer>();
		List<LSsamplefile> idList = new ArrayList<LSsamplefile>();

		List<Long> batchcode = lstsharedorder.stream().map(LSlogilablimsorderdetail::getBatchcode)
				.collect(Collectors.toList());

		List<Integer> filetype = lstsharedorder.stream().map(LSlogilablimsorderdetail::getFiletype)
				.collect(Collectors.toList());

		if (objorder.getSearchCriteria().getContentsearchtype() != null
				&& objorder.getSearchCriteria().getContentsearch() != null) {

			if (objorder.getFiletype() == -1) {
//				lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeAndcreateddate(objorder.getFromdate(),
//						objorder.getTodate());

				lstBatchcode = lslogilablimsorderdetailRepository.findByBatchcodeIn(batchcode);

			} else {
//				lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeonFiletypeAndcreateddate(
//						objorder.getFiletype(), objorder.getFromdate(), objorder.getTodate());

				lstBatchcode = lslogilablimsorderdetailRepository.findByBatchcodeInAndFiletypeIn(batchcode, filetype);
			}

			if (lstBatchcode != null && lstBatchcode.size() > 0) {
				lstsamplefilecode = lssamplefileRepository.getFilesamplecodeByBatchcodeIn(lstBatchcode);
			}

			if (lstsamplefilecode != null && lstsamplefilecode.size() > 0) {
				idList = getsamplefileIds(lstsamplefilecode, objorder.getSearchCriteria(), objorder.getIsmultitenant());
				if (idList != null && idList.size() > 0) {

					final List<LSsamplefile> idlistdata = idList;

					lstlogilab = lstsharedorder.stream()
							.filter(srow1 -> idlistdata.stream()
									.anyMatch(detailrow -> srow1.getBatchcode().equals(detailrow.getBatchcode())))
							.collect(Collectors.toList());
				}
			}

		}

		return lstlogilab;

	}

	public List<Logilaborders> GetmyordersonFilter(LSlogilablimsorderdetail objorder, List<Logilaborders> lstmyorders,
			String Orderflag) {
		List<Logilaborders> lstorder = new ArrayList<Logilaborders>();
		List<Long> lstBatchcode = new ArrayList<Long>();
		List<Integer> lstsamplefilecode = new ArrayList<Integer>();
		List<LSsamplefile> idList = new ArrayList<LSsamplefile>();

//		Integer filetype = objorder.getFiletype();

		List<Long> batchcode = lstmyorders.stream().map(Logilaborders::getBatchcode).collect(Collectors.toList());

		List<Integer> filetypelist = lstmyorders.stream().map(Logilaborders::getFiletype).collect(Collectors.toList());

//		List<String> orderflag = lstmyorders.stream().map(Logilaborders::getOrderflag)
//				.collect(Collectors.toList());

		if (objorder.getSearchCriteria().getContentsearchtype() != null
				&& objorder.getSearchCriteria().getContentsearch() != null) {

			if (objorder.getFiletype() == -1) {
//				lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeAndcreateddate(objorder.getFromdate(),
//						objorder.getTodate());
				lstBatchcode = lslogilablimsorderdetailRepository.findByBatchcodeIn(batchcode);
			} else {
//				lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeonFiletypeAndcreateddate(
//						objorder.getFiletype(), objorder.getFromdate(), objorder.getTodate());
				lstBatchcode = lslogilablimsorderdetailRepository.findByBatchcodeInAndFiletypeIn(batchcode,
						filetypelist);
			}

			if (lstBatchcode != null && lstBatchcode.size() > 0) {
				lstsamplefilecode = lssamplefileRepository.getFilesamplecodeByBatchcodeIn(lstBatchcode);
			}

			if (lstsamplefilecode != null && lstsamplefilecode.size() > 0) {
				idList = getsamplefileIds(lstsamplefilecode, objorder.getSearchCriteria(), objorder.getIsmultitenant());
				if (idList != null && idList.size() > 0) {

					final List<LSsamplefile> idlistdata = idList;

					lstorder = lstmyorders.stream()
							.filter(srow1 -> idlistdata.stream()
									.anyMatch(detailrow -> srow1.getBatchcode().equals(detailrow.getBatchcode())))
							.collect(Collectors.toList());

				}
			}

		}

		return lstorder;

	}

	public List<Logilaborders> GetsampleordersonFilter(LSlogilablimsorderdetail lstorderstr,
			List<Logilaborders> lstorder2) {
		List<Logilaborders> lstorder = new ArrayList<Logilaborders>();
		List<Long> lstBatchcode = new ArrayList<Long>();
		List<Integer> lstsamplefilecode = new ArrayList<Integer>();
		List<LSsamplefile> idList = new ArrayList<LSsamplefile>();

		List<Long> batchcode = lstorder2.stream().map(Logilaborders::getBatchcode).collect(Collectors.toList());

		List<Integer> filetypelist = lstorder2.stream().map(Logilaborders::getFiletype).collect(Collectors.toList());

		if (lstorderstr.getSearchCriteria().getContentsearchtype() != null
				&& lstorderstr.getSearchCriteria().getContentsearch() != null) {

			if (lstorderstr.getFiletype() == -1) {
//				lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeAndcreateddate(lstorderstr.getObjuser().getFromdate(),
//						lstorderstr.getObjuser().getTodate());
				lstBatchcode = lslogilablimsorderdetailRepository.findByBatchcodeIn(batchcode);
			} else {
//				lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeonFiletypeAndcreateddate(
//						lstorderstr.getFiletype(), lstorderstr.getObjuser().getFromdate(),
//						lstorderstr.getObjuser().getTodate());
				lstBatchcode = lslogilablimsorderdetailRepository.findByBatchcodeInAndFiletypeIn(batchcode,
						filetypelist);
			}

			if (lstBatchcode != null && lstBatchcode.size() > 0) {
				lstsamplefilecode = lssamplefileRepository.getFilesamplecodeByBatchcodeIn(lstBatchcode);
			}

			if (lstsamplefilecode != null && lstsamplefilecode.size() > 0) {
				idList = getsamplefileIds(lstsamplefilecode, lstorderstr.getSearchCriteria(),
						lstorderstr.getIsmultitenant());
				if (idList != null && idList.size() > 0) {

//					lstorder = lslogilablimsorderdetailRepository
//							.findByDirectorycodeAndCreatedtimestampBetweenAndLssamplefileIn(lstorderstr.getDirectorycode(),
//									lstorderstr.getObjuser().getFromdate(),lstorderstr.getObjuser().getTodate(), idList);

					final List<LSsamplefile> idlistdata = idList;

					lstorder = lstorder2.stream()
							.filter(srow1 -> idlistdata.stream()
									.anyMatch(detailrow -> srow1.getBatchcode().equals(detailrow.getBatchcode())))
							.collect(Collectors.toList());
				}
			}

		}

		return lstorder;

	}

	public List<Logilaborders> GetordersondirectoryFilter(LSSheetOrderStructure lstorderstr,
			List<Logilaborders> lstorder2) {
		List<Logilaborders> lstorder = new ArrayList<Logilaborders>();
		List<Long> lstBatchcode = new ArrayList<Long>();
		List<Integer> lstsamplefilecode = new ArrayList<Integer>();
		List<LSsamplefile> idList = new ArrayList<LSsamplefile>();

		List<Long> batchcode = lstorder2.stream().map(Logilaborders::getBatchcode).collect(Collectors.toList());

		List<Integer> filetypelist = lstorder2.stream().map(Logilaborders::getFiletype).collect(Collectors.toList());

		if (lstorderstr.getSearchCriteria().getContentsearchtype() != null
				&& lstorderstr.getSearchCriteria().getContentsearch() != null) {

			if (lstorderstr.getFiletype() == -1) {
//				lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeAndcreateddate(lstorderstr.getObjuser().getFromdate(),
//						lstorderstr.getObjuser().getTodate());
				lstBatchcode = lslogilablimsorderdetailRepository.findByBatchcodeIn(batchcode);
			} else {
//				lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeonFiletypeAndcreateddate(
//						lstorderstr.getFiletype(), lstorderstr.getObjuser().getFromdate(),
//						lstorderstr.getObjuser().getTodate());
				lstBatchcode = lslogilablimsorderdetailRepository.findByBatchcodeInAndFiletypeIn(batchcode,
						filetypelist);
			}

			if (lstBatchcode != null && lstBatchcode.size() > 0) {
				lstsamplefilecode = lssamplefileRepository.getFilesamplecodeByBatchcodeIn(lstBatchcode);
			}

			if (lstsamplefilecode != null && lstsamplefilecode.size() > 0) {
				idList = getsamplefileIds(lstsamplefilecode, lstorderstr.getSearchCriteria(),
						lstorderstr.getIsmultitenant());
				if (idList != null && idList.size() > 0) {

//					lstorder = lslogilablimsorderdetailRepository
//							.findByDirectorycodeAndCreatedtimestampBetweenAndLssamplefileIn(lstorderstr.getDirectorycode(),
//									lstorderstr.getObjuser().getFromdate(),lstorderstr.getObjuser().getTodate(), idList);

					final List<LSsamplefile> idlistdata = idList;

					lstorder = lstorder2.stream()
							.filter(srow1 -> idlistdata.stream()
									.anyMatch(detailrow -> srow1.getBatchcode().equals(detailrow.getBatchcode())))
							.collect(Collectors.toList());
				}
			}

		}

		return lstorder;

	}

	public void updatenotificationfororderworkflow(LSlogilablimsorderdetail objorder, LSworkflow previousworkflow) {
		try {
			String Details = "";
			String Notifiction = "";

			LSuserMaster obj = lsuserMasterRepository.findByusercode(objorder.getObjLoggeduser().getUsercode());

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

				if (previousworkflowname.equals(objorder.getLsworkflow().getWorkflowname())
						&& objorder.getApprovelstatus() == 1) {
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
					if (!(objorder.getObjLoggeduser().getUsercode()
							.equals(lstusers.get(i).getLsuserMaster().getUsercode()))) {
						LSnotification objnotify = new LSnotification();
						objnotify.setNotifationfrom(obj);
						objnotify.setNotifationto(lstusers.get(i).getLsuserMaster());
						objnotify.setNotificationdate(objorder.getModifidate());
						objnotify.setNotification(Notifiction);
						objnotify.setNotificationdetils(Details);
						objnotify.setIsnewnotification(1);
						objnotify.setNotificationpath("/registertask");
						objnotify.setNotificationfor(2);
						lstnotifications.add(objnotify);
					}
				}

				lsnotificationRepository.save(lstnotifications);
			}
		} catch (Exception e) {

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
						if (!(objorder.getObjLoggeduser().getUsercode()
								.equals(lstusers.get(i).getLsuserMaster().getUsercode()))) {

							LSnotification objnotify = new LSnotification();
							objnotify.setNotifationfrom(objorder.getObjLoggeduser());
							objnotify.setNotifationto(lstusers.get(i).getLsuserMaster());
							objnotify.setNotificationdate(objorder.getCreatedtimestamp());
							objnotify.setNotification(Notifiction);
							objnotify.setNotificationdetils(Details);
							objnotify.setIsnewnotification(1);
							objnotify.setNotificationpath("/registertask");
							objnotify.setNotificationfor(2);
							lstnotifications.add(objnotify);
						}
					}

					lsnotificationRepository.save(lstnotifications);
				}
			}
		} catch (Exception e) {

		}
	}

	public LSactivity InsertActivities(LSactivity objActivity) {
		try {
			objActivity.setActivityDate(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lsactivityRepository.save(objActivity);
	}

	public List<LSlogilablimsorderdetail> Getorderbytype(LSlogilablimsorderdetail objorder) {

		if (objorder.getOrderflag().equals("N")) {
			return lslogilablimsorderdetailRepository
					.findByFiletypeAndOrderflagAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(),
							objorder.getTodate());
		} else {
			return lslogilablimsorderdetailRepository
					.findByFiletypeAndOrderflagAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(),
							objorder.getTodate());
		}
	}

	public List<LSlogilablimsorderdetail> Getorderbytypeanduser(LSlogilablimsorderdetail objorder) {
		List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objorder.getLsuserMaster());
		List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
				objorder.getLsuserMaster().getLssitemaster());
		List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();

		if (objorder.getOrderflag().equals("N")) {
			lstorder = lslogilablimsorderdetailRepository
					.findByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), lstproject, objorder.getFromdate(),
							objorder.getTodate());
		} else {
			lstorder = lslogilablimsorderdetailRepository
					.findByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), lstproject, objorder.getFromdate(),
							objorder.getTodate());
		}

		lstteammap = null;
		lstteam = null;
		lstproject = null;

		return lstorder;
	}

	public List<LSlogilablimsorderdetail> Getorderbytypeandflag(LSlogilablimsorderdetail objorder,
			Map<String, Object> mapOrders) {
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();

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
					}
				}
			} else {
				lstorder = lslogilablimsorderdetailRepository
						.findByFiletypeAndOrderflagAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
								objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(),
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
						lstorder = lslogilablimsorderdetailRepository
								.findByFiletypeAndOrderflagAndCompletedtimestampBetweenAndLssamplefileIn(
										objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(),
										objorder.getTodate(), idList);

					}
				}
			} else {
				lstorder = lslogilablimsorderdetailRepository
						.findByFiletypeAndOrderflagAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
								objorder.getFiletype(), objorder.getOrderflag(), objorder.getFromdate(),
								objorder.getTodate());
			}
		}

		if (objorder.getObjsilentaudit() != null) {
			objorder.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
			try {
				objorder.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

						lstorder = lslogilablimsorderdetailRepository
								.findByOrderflagAndFiletypeAndCreatedtimestampBetweenAndLssamplefileIn(
										objorder.getOrderflag(), objorder.getFiletype(), objorder.getFromdate(),
										objorder.getTodate(), idList);
					}
				}
			} else {

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

						lstorder = lslogilablimsorderdetailRepository
								.findByOrderflagAndFiletypeAndCompletedtimestampBetweenAndLssamplefileIn(
										objorder.getOrderflag(), objorder.getFiletype(), objorder.getFromdate(),
										objorder.getTodate(), idList);
					}
				}
			} else {

				lstorder = lslogilablimsorderdetailRepository
						.findByOrderflagAndFiletypeAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
								objorder.getOrderflag(), objorder.getFiletype(), objorder.getFromdate(),
								objorder.getTodate());
			}
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

		idList = null;

		return lstorder;
	}

	public List<LSsamplefile> getsamplefileIds(List<Integer> lstsamplefilecode, SearchCriteria searchCriteria,
			Integer ismultitenant) {

		List<Long> idList = new ArrayList<Long>();
		String searchtext = searchCriteria.getContentsearch().replace("[", "\\[").replace("]", "\\]");
		if (ismultitenant == 0) {
			Query query = new Query();
			if (searchCriteria.getContentsearchtype() == 1 || searchCriteria.getContentsearchtype() == 3) {
				query.addCriteria(Criteria.where("contentvalues").regex(searchtext, "i"));
			} else if (searchCriteria.getContentsearchtype() == 2) {
				query.addCriteria(Criteria.where("contentparameter").regex(searchtext, "i"));
			}

			// query.addCriteria(Criteria.where("id").in(lstsamplefilecode)).with(new
			// PageRequest(0, 5));

			List<OrderCreation> orders = mongoTemplate.find(query, OrderCreation.class);
			idList = orders.stream().map(OrderCreation::getId).collect(Collectors.toList());
		} else {
			List<CloudOrderCreation> orders = new ArrayList<CloudOrderCreation>();
			if (searchCriteria.getContentsearchtype() == 1 || searchCriteria.getContentsearchtype() == 3) {
				orders = cloudOrderCreationRepository.findByContentvaluesequal("%" + searchtext + "%");
			} else if (searchCriteria.getContentsearchtype() == 2) {
				orders = cloudOrderCreationRepository.findByContentparameterequal("%" + searchtext + "%");
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
		List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
				objorder.getLsuserMaster().getLssitemaster());
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

						}
					} else {
						lstorder = lslogilablimsorderdetailRepository
								.findByFiletypeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
										objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getLsuserMaster(),
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

						}
					} else {
						lstorder = lslogilablimsorderdetailRepository
								.findByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
										objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getFromdate(),
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

					lstorder = getordersonsamplefileid(lstBatchcode, objorder);
//					lstorder = getordersonsamplefileidlsorder(lstBatchcode, objorder);
				} else {
					lstorder = lslogilablimsorderdetailRepository
							.findByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getFiletype(), objorder.getOrderflag(), lstproject, objorder.getFromdate(),
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
			try {
				objorder.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

//		List<LSprojectmaster> lstproject = objorder.getLstproject();
		List<Logilaborders> lstorder = new ArrayList<Logilaborders>();
		List<Long> lstBatchcode = new ArrayList<Long>();

//		List<LSworkflow> lstworkflow = GetWorkflowonuser(objorder.getLsuserMaster().getLsusergrouptrans());

//		List<LSworkflow> lstworkflow = objorder.getLstworkflow();

		long pendingcount = 0;
		long completedcount = 0;
		long Assignedcount = 0;
		long Assignedpendingcount = 0;
		long Assignedcompletedcount = 0;
		long myordercount = 0;
		long myorderpendingcount = 0;
		long myordercompletedcount = 0;

		if (objorder.getLstproject() != null && objorder.getLstproject().size() > 0) {
			List<Integer> lstprojectcode = objorder.getLstproject().stream().map(LSprojectmaster::getProjectcode)
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
									objorder.getOrderflag(), objorder.getFiletype(), objorder.getLstproject(),
									objorder.getFromdate(), objorder.getTodate());
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
									objorder.getOrderflag(), objorder.getFiletype(), objorder.getLstproject(),
									objorder.getFromdate(), objorder.getTodate());
				}
			}

			if (objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearch().trim() != "") {

				if (objorder.getOrderflag().equals("N")) {
					pendingcount = lstorder.size();

					completedcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "R", objorder.getLstproject(), objorder.getFromdate(),
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
									objorder.getFiletype(), "N", objorder.getLstproject(), objorder.getFromdate(),
									objorder.getTodate());
					completedcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "R", objorder.getLstproject(), objorder.getFromdate(),
									objorder.getTodate());

					myordercount = lslogilablimsorderdetailRepository
							.countByFiletypeAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									objorder.getFiletype(), objorder.getLsuserMaster(), objorder.getFromdate(),
									objorder.getTodate());
				} else if (objorder.getOrderflag().equals("M")) {
					pendingcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "N", objorder.getLstproject(), objorder.getFromdate(),
									objorder.getTodate());
					completedcount = lslogilablimsorderdetailRepository
							.countByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
									objorder.getFiletype(), "R", objorder.getLstproject(), objorder.getFromdate(),
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
									objorder.getFiletype(), "N", objorder.getLstproject(), objorder.getFromdate(),
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
								objorder.getFiletype(), "N", objorder.getLstproject(), objorder.getFromdate(),
								objorder.getTodate());

				completedcount = lslogilablimsorderdetailRepository
						.countByFiletypeAndOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
								objorder.getFiletype(), "R", objorder.getLstproject(), objorder.getFromdate(),
								objorder.getTodate());

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

		lstorder.forEach(objorderDetail -> objorderDetail.setLstworkflow(objorder.getLstworkflow()));

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

		lstBatchcode = null;

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
//		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		if (objorder.getOrderflag().equals("N")) {
			return lslogilablimsorderdetailRepository
					.findFirst10ByFiletypeAndOrderflagAndBatchcodeLessThanAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(),
							objorder.getFromdate(), objorder.getTodate());
		} else {
			return lslogilablimsorderdetailRepository
					.findFirst10ByFiletypeAndOrderflagAndBatchcodeLessThanAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(),
							objorder.getFromdate(), objorder.getTodate());
		}
	}

	public List<LSlogilablimsorderdetail> Getorderbytypeandflaganduserlazy(LSlogilablimsorderdetail objorder,
			Map<String, Object> mapOrders) {
		List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objorder.getLsuserMaster());
		List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
				objorder.getLsuserMaster().getLssitemaster());
//		List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);

		if (objorder.getOrderflag().equals("N")) {
			return lslogilablimsorderdetailRepository
					.findByFiletypeAndOrderflagAndBatchcodeLessThanAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(),
							lsprojectmasterRepository.findByLsusersteamIn(lstteam), objorder.getFromdate(),
							objorder.getTodate());
		} else {
			return lslogilablimsorderdetailRepository
					.findByFiletypeAndOrderflagAndBatchcodeLessThanAndLsprojectmasterInAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), objorder.getOrderflag(), objorder.getBatchcode(),
							lsprojectmasterRepository.findByLsusersteamIn(lstteam), objorder.getFromdate(),
							objorder.getTodate());
		}

//		mapOrders.put("orders", lstorder);

//		return lstorder;
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
		List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
				objorder.getLsuserMaster().getLssitemaster());
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
//		List<LSworkflowgroupmapping> lsworkflowgroupmapping = lsworkflowgroupmappingRepository
//				.findBylsusergroup(objuser.getLsusergroup());
		List<LSworkflow> lsworkflow = lsworkflowRepository.findByLsworkflowgroupmappingInOrderByWorkflowcodeDesc(
				lsworkflowgroupmappingRepository.findBylsusergroup(objuser.getLsusergroup()));

		return lsworkflow;
	}

	public Map<String, Object> GetWorkflowanduseronUsercode(LSuserMaster usercode) {
//		LSuserMaster objuser = lsuserMasterRepository.findByusercode(usercode.getUsercode());
//
//		LSusergroup lsusergroup = usercode.getLsusergrouptrans();

		Map<String, Object> maplogindetails = new HashMap<String, Object>();
		maplogindetails.put("workflow", GetWorkflowonuser(usercode.getLsusergrouptrans()));
		maplogindetails.put("user", lsuserMasterRepository.findByusercode(usercode.getUsercode()));
		return maplogindetails;
	}

	public List<LSworkflow> GetWorkflowonuser(LSusergroup lsusergroup) {

		return lsworkflowRepository.findByLsworkflowgroupmappingInOrderByWorkflowcodeDesc(
				lsworkflowgroupmappingRepository.findBylsusergroup(lsusergroup));
	}

	public LogilabOrderDetails GetorderStatus(LSlogilablimsorderdetail objorder) throws IOException, ParseException {

		LogilabOrderDetails objupdatedorder = logilablimsorderdetailsRepository
				.findByBatchcode(objorder.getBatchcode());
		objupdatedorder.setResponse(new Response());

		if (objupdatedorder.getLockeduser() != null) {

			if (objupdatedorder.getFiletype() != 1 && objupdatedorder.getFiletype()!=0 && !objupdatedorder.getOrderflag().trim().equalsIgnoreCase("R")
					&& objupdatedorder.getAssignedto() == null
					&& objupdatedorder.getLockeduser().equals(objorder.getObjLoggeduser().getUsercode())) {

				objupdatedorder.getResponse().setInformation("IDS_SAME_USER_OPEN");
				objupdatedorder.getResponse().setStatus(false);

				return objupdatedorder;
			} else if (!objorder.getIsmultitenant().equals(2) && objupdatedorder.getFiletype()!=0) {
				LSuserMaster user = new LSuserMaster();
				user.setUsercode(objupdatedorder.getLockeduser());
				List<LSactiveUser> LSactiveUsr = lSactiveUserRepository.findByLsusermaster(user);
				if (LSactiveUsr.isEmpty()) {
					objupdatedorder.setLockeduser(objorder.getObjLoggeduser().getUsercode());
					objupdatedorder.setLockedusername(objorder.getObjLoggeduser().getUsername());
					objupdatedorder.setActiveuser(objorder.getActiveuser());
					logilablimsorderdetailsRepository.UpdateOrderData(objorder.getObjLoggeduser().getUsercode(),
							objorder.getObjLoggeduser().getUsername(), objorder.getActiveuser(),
							objupdatedorder.getBatchcode());
				}
				objupdatedorder.setIsLock(1);
//					lslogilablimsorderdetailRepository.save(objupdatedorder);
			
			}

		} else if (!objorder.getIsmultitenant().equals(2)) {
			objupdatedorder.setLockeduser(objorder.getObjLoggeduser().getUsercode());
			objupdatedorder.setLockedusername(objorder.getObjLoggeduser().getUsername());
			objupdatedorder.setActiveuser(objorder.getActiveuser());
			objupdatedorder.setIsLock(1);
//				lslogilablimsorderdetailRepository.save(objupdatedorder);
			logilablimsorderdetailsRepository.UpdateOrderData(objorder.getObjLoggeduser().getUsercode(),
					objorder.getObjLoggeduser().getUsername(), objorder.getActiveuser(),
					objupdatedorder.getBatchcode());
		}
//			System.out.println("Locked User End  " + dtf.format(LocalDateTime.now()));
//			if (lsLogilaborders != null && lsLogilaborders.size() > 0) {
//				int i = 0;
//				while (lsLogilaborders.size() > i) {
//					lsorderno.add(lsLogilaborders.get(i).getOrderid().toString());
//					i++;
//				}
//			}
//			objupdatedorder.setLsLSlogilablimsorder(lsLogilaborders);
//			System.out.println("Work Flow Start  " + dtf.format(LocalDateTime.now()));
		if (objupdatedorder.getLsprojectmaster() != null && objorder.getLstworkflow() != null) {
			List<Integer> lstworkflowcode = objorder.getLstworkflow().stream().map(LSworkflow::getWorkflowcode)
					.collect(Collectors.toList());
			if (objorder.getLstworkflow() != null && objupdatedorder.getLsworkflow() != null
					&& lstworkflowcode.contains(objupdatedorder.getLsworkflow().getWorkflowcode())) {
				objupdatedorder.setCanuserprocess(true);
			} else {
				objupdatedorder.setCanuserprocess(false);
			}
		}
//			System.out.println("Work Flow ENd  " + dtf.format(LocalDateTime.now()));
		List<LsOrderattachments> lstattach = lsOrderattachmentsRepository
				.findByBatchcodeOrderByAttachmentcodeDesc(objorder.getBatchcode());
		objupdatedorder.setLsOrderattachments(lstattach);
		
		List<ELNFileAttachments> lstelnfileattach = elnFileattachmentsRepository
				.findByBatchcodeOrderByAttachmentcodeDesc(objorder.getBatchcode());
		objupdatedorder.setElnfileAttachments(lstelnfileattach);
		
		
		List<LSlogilablimsorder> lsLogilaborders = lslogilablimsorderRepository.findBybatchid(objupdatedorder.getBatchid());
		objupdatedorder.setLsLSlogilablimsorder(lsLogilaborders);
		
		if (objupdatedorder.getFiletype() == 0) {
			List<Lsbatchdetails> lstbatch = LsbatchdetailsRepository.findByBatchcode(objupdatedorder.getBatchcode());
			objupdatedorder.setLsbatchdetails(lstbatch);
			objupdatedorder.setCanuserprocess(true);
			objupdatedorder
					.setLstestparameter(lStestparameterRepository.findByntestcode(objupdatedorder.getTestcode()));
		}
		lsLogilaborders = null;

		if (objupdatedorder.getLockeduser() != null && objorder.getObjLoggeduser() != null
				&& objupdatedorder.getLockeduser().equals(objorder.getObjLoggeduser().getUsercode())) {
			objupdatedorder.setIsLockbycurrentuser(1);
		} else {
			objupdatedorder.setIsLockbycurrentuser(0);
		}
//			System.out.println("Work Flow Final Step Start  " + dtf.format(LocalDateTime.now()));
		if (objupdatedorder.getFiletype() != 0 && objupdatedorder.getOrderflag().toString().trim().equals("N")) {
			LSworkflow objlastworkflow = lsworkflowRepository
					.findTopByAndLssitemasterOrderByWorkflowcodeDesc(objorder.getObjLoggeduser().getLssitemaster());
			if (objlastworkflow != null && objupdatedorder.getLsworkflow() != null
					&& objupdatedorder.getLsworkflow().equals(objlastworkflow)) {
				objupdatedorder.setIsFinalStep(1);
			} else {
				objupdatedorder.setIsFinalStep(0);
			}
		}
//			System.out.println("Work Flow Final Step End  " + dtf.format(LocalDateTime.now()));
//			System.out.println("Get File Content Start  " + dtf.format(LocalDateTime.now()));
		if (objupdatedorder.getLssamplefile() != null) {
			if (objorder.getIsmultitenant() == 1 || objorder.getIsmultitenant() == 2) {
				CloudOrderCreation objCreation = cloudOrderCreationRepository
						.findById((long) objupdatedorder.getLssamplefile().getFilesamplecode());
				if (objCreation != null && objCreation.getContainerstored() == 0) {
					objupdatedorder.getLssamplefile().setFilecontent(objCreation.getContent());
				} else {
					objupdatedorder.getLssamplefile().setFilecontent(
							objCloudFileManipulationservice.retrieveCloudSheets(objCreation.getFileuid(),
									commonfunction.getcontainername(objorder.getIsmultitenant(),
											TenantContext.getCurrentTenant()) + "ordercreation"));
				}
			} else {

				GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename")
						.is("order_" + objupdatedorder.getLssamplefile().getFilesamplecode())));
				if (largefile == null) {
					largefile = gridFsTemplate.findOne(new Query(Criteria.where("_id")
							.is("order_" + objupdatedorder.getLssamplefile().getFilesamplecode())));
				}

				if (largefile != null) {
					objupdatedorder.getLssamplefile()
							.setFilecontent(new BufferedReader(
									new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
									.collect(Collectors.joining("\n")));
				} else {

					if (mongoTemplate.findById(objupdatedorder.getLssamplefile().getFilesamplecode(),
							OrderCreation.class) != null) {
						objupdatedorder.getLssamplefile().setFilecontent(mongoTemplate
								.findById(objupdatedorder.getLssamplefile().getFilesamplecode(), OrderCreation.class)
								.getContent());
					}
				}
			}
		}
//			System.out.println("Get File Content End  " + dtf.format(LocalDateTime.now()));
		objupdatedorder.setLstworkflow(objorder.getLstworkflow());

		if (objorder.getLsuserMaster() != null && objorder.getLsuserMaster().getUnifieduserid() != null) {
			LScentralisedUsers objUserId = new LScentralisedUsers();
			objUserId.setUnifieduserid(objorder.getLsuserMaster().getUnifieduserid());
			objupdatedorder.setLscentralisedusers(userService.Getcentraliseduserbyid(objUserId));
		}
		objupdatedorder.getResponse().setStatus(true);
		objupdatedorder.setLsorderworkflowhistory(
				lsorderworkflowhistoryRepositroy.findByBatchcodeOrderByHistorycode(objupdatedorder.getBatchcode()));
//			objupdatedorder.setlSprotocolorderstephistory(
//					lsprotocolorderstephistoryRepository.findByBatchcode(objupdatedorder.getBatchcode()));
//			System.out.println("Return   " + dtf.format(LocalDateTime.now()));
		return objupdatedorder;
	}

	public LSlogilablimsorderdetail GetorderStatusFromBatchID(LSlogilablimsorderdetail objorder) {

		LSlogilablimsorderdetail objupdatedorder = lslogilablimsorderdetailRepository
				.findByBatchid(objorder.getBatchid());

		List<LSlogilablimsorder> lsLogilaborders = lslogilablimsorderRepository
				.findBybatchid(objupdatedorder.getBatchid());
		List<String> lsorderno = new ArrayList<String>();

		if (lsLogilaborders != null && lsLogilaborders.size() > 0) {
			int i = 0;

			while (lsLogilaborders.size() > i) {
				lsorderno.add(lsLogilaborders.get(i).getOrderid().toString());
				i++;
			}
		}
		objupdatedorder.setLsLSlogilablimsorder(lsLogilaborders);
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
			LSworkflow objlastworkflow = lsworkflowRepository
					.findTopByAndLssitemasterOrderByWorkflowcodeDesc(objorder.getObjLoggeduser().getLssitemaster());
			if (objlastworkflow != null && objupdatedorder.getLsworkflow().equals(objlastworkflow)) {
				objupdatedorder.setIsFinalStep(1);
			} else {
				objupdatedorder.setIsFinalStep(0);
			}
		}

		if (objupdatedorder.getFiletype() == 0) {
			objupdatedorder
					.setLstestparameter(lStestparameterRepository.findByntestcode(objupdatedorder.getTestcode()));
		}

		if (objupdatedorder.getLsprojectmaster() != null && objorder.getLstworkflow() != null) {
			List<Integer> lstworkflowcode = objorder.getLstworkflow().stream().map(LSworkflow::getWorkflowcode)
					.collect(Collectors.toList());
			if (objorder.getLstworkflow() != null
					&& lstworkflowcode.contains(objupdatedorder.getLsworkflow().getWorkflowcode())) {
				objupdatedorder.setCanuserprocess(true);
			} else {
				objupdatedorder.setCanuserprocess(false);
			}
		} else {
			objupdatedorder.setCanuserprocess(true);
		}

		if (objupdatedorder.getFiletype() == 0) {
			objupdatedorder.setCanuserprocess(true);
		}

		if (objupdatedorder.getLssamplefile() != null) {
			if (objorder.getIsmultitenant() == 1) {
//				CloudOrderCreation file = cloudOrderCreationRepository
//						.findById((long) objupdatedorder.getLssamplefile().getFilesamplecode());
				if (cloudOrderCreationRepository
						.findById((long) objupdatedorder.getLssamplefile().getFilesamplecode()) != null) {
					objupdatedorder.getLssamplefile().setFilecontent(cloudOrderCreationRepository
							.findById((long) objupdatedorder.getLssamplefile().getFilesamplecode()).getContent());
				}
			} else {

//				String fileid = "order_" + objupdatedorder.getLssamplefile().getFilesamplecode();
				GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename")
						.is("order_" + objupdatedorder.getLssamplefile().getFilesamplecode())));
				if (largefile == null) {
					largefile = gridFsTemplate.findOne(new Query(Criteria.where("_id")
							.is("order_" + objupdatedorder.getLssamplefile().getFilesamplecode())));
				}

				if (largefile != null) {
//					String filecontent = new BufferedReader(
//							new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
//									.collect(Collectors.joining("\n"));
					objupdatedorder.getLssamplefile()
							.setFilecontent(new BufferedReader(
									new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
									.collect(Collectors.joining("\n")));
				} else {

//					OrderCreation file = mongoTemplate.findById(objupdatedorder.getLssamplefile().getFilesamplecode(),
//							OrderCreation.class);
					if (mongoTemplate.findById(objupdatedorder.getLssamplefile().getFilesamplecode(),
							OrderCreation.class) != null) {
						objupdatedorder.getLssamplefile().setFilecontent(mongoTemplate
								.findById(objupdatedorder.getLssamplefile().getFilesamplecode(), OrderCreation.class)
								.getContent());
					}
				}
			}
		}

		objorder = null;
		return objupdatedorder;
	}

	private String GetSamplefileconent(LSsamplefile lssamplefile, Integer ismultitenant) throws IOException {
		String content = "";

		if (lssamplefile != null) {
			if (ismultitenant == 1 || ismultitenant == 2) {
				CloudOrderCreation objCreation = cloudOrderCreationRepository
						.findById((long) lssamplefile.getFilesamplecode());
				if (objCreation != null && objCreation.getContainerstored() == 0) {
					content = objCreation.getContent();
				} else {
					content = objCloudFileManipulationservice.retrieveCloudSheets(objCreation.getFileuid(),
							commonfunction.getcontainername(ismultitenant,
									TenantContext.getCurrentTenant()) + "ordercreation");
				}
			} else {
				if (mongoTemplate.findById(lssamplefile.getFilesamplecode(), OrderCreation.class) != null) {
					content = mongoTemplate.findById(lssamplefile.getFilesamplecode(), OrderCreation.class)
							.getContent();
				}
			}
		}

		return content;
	}

//	public LSlogilablimsorderdetail GetdetailorderStatus(LSlogilablimsorderdetail objupdatedorder) {
//
//		objupdatedorder.setLsLSlimsorder(lSlimsorderRepository.findBybatchid(objupdatedorder.getBatchid()));
//
//		LSsamplefile LSsamplefile = objupdatedorder.getLssamplefile();
//		if (LSsamplefile != null) {
//			if (LSsamplefile.getFilesamplecode() != null) {
//				List<LSsamplefileversion> LSsamplefileversion = lssamplefileversionRepository
//						.findByFilesamplecodeOrderByVersionnoDesc(LSsamplefile);
//				objupdatedorder.getLssamplefile().setLssamplefileversion(LSsamplefileversion);
//			}
//		}
//
//		if (objupdatedorder.getFiletype() != 0 && objupdatedorder.getOrderflag().toString().trim().equals("N")) {
//			if (objupdatedorder.getLsworkflow()
//					.equals(lsworkflowRepository.findTopByAndLssitemasterOrderByWorkflowcodeDesc(
//							objupdatedorder.getObjLoggeduser().getLssitemaster()))) {
//				objupdatedorder.setIsFinalStep(1);
//			} else {
//				objupdatedorder.setIsFinalStep(0);
//			}
//		}
//
//		if (objupdatedorder.getFiletype() == 0) {
//			objupdatedorder
//					.setLstestparameter(lStestparameterRepository.findByntestcode(objupdatedorder.getTestcode()));
//		}
//
//		return objupdatedorder;
//	}

	public LSlogilablimsorderdetail GetdetailorderStatus(LSlogilablimsorderdetail objupdatedorder) {

		objupdatedorder
				.setLsLSlogilablimsorder(lslogilablimsorderRepository.findBybatchid(objupdatedorder.getBatchid()));

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

//	public Map<String, Object> GetResults(LSlogilablimsorderdetail objorder) {
//		Map<String, Object> mapOrders = new HashMap<String, Object>();
//
//		List<LSresultdetails> lsResults = new ArrayList<LSresultdetails>();
//		List<LSlimsorder> lsLogilaborders = lslimsorderRepository.findBybatchid(objorder.getBatchid());
//		List<String> lsorderno = new ArrayList<String>();
//
//		if (lsLogilaborders != null && lsLogilaborders.size() > 0) {
//			int i = 0;
//
//			while (lsLogilaborders.size() > i) {
//				lsorderno.add(lsLogilaborders.get(i).getOrderid().toString());
//				i++;
//			}
//		}
//		lsResults = lsresultdetailsRepository.findBylimsreferencecodeIn(lsorderno);
//
//		mapOrders.put("SDMSResults", lsResults);
//
//		List<ELNResultDetails> ELNResults = new ArrayList<ELNResultDetails>();
//		List<LSlogilablimsorderdetail> lslogilablimsorderdetail = lslogilablimsorderdetailRepository
//				.findBybatchcode(objorder.getBatchcode());
//		List<Long> batchcode = new ArrayList<Long>();
//
//		if (lslogilablimsorderdetail != null && lslogilablimsorderdetail.size() > 0) {
//			int i = 0;
//
//			while (lslogilablimsorderdetail.size() > i) {
//				batchcode.add(lslogilablimsorderdetail.get(i).getBatchcode());
//				i++;
//			}
//		}
//		ELNResults = ELNResultDetailsRepository.findBybatchcode(batchcode);
//
//		mapOrders.put("ELNResults", ELNResults);
//
//		return mapOrders;
//	}

	public Map<String, Object> GetResults(LSlogilablimsorderdetail objorder) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();

		List<LSresultdetails> lsResults = new ArrayList<LSresultdetails>();
		List<LSlogilablimsorder> lsLogilaborders = lslogilablimsorderRepository.findBybatchid(objorder.getBatchid());
		List<String> lsorderno = new ArrayList<String>();

		if (lsLogilaborders != null && lsLogilaborders.size() > 0) {
			int i = 0;

			while (lsLogilaborders.size() > i) {
				lsorderno.add(lsLogilaborders.get(i).getOrderid().toString());
				i++;
			}
		}
		lsResults = lsresultdetailsRepository.findBylimsreferencecodeIn(lsorderno);

		List<ELNResultDetails> ELNResults = new ArrayList<ELNResultDetails>();
		List<LSlogilablimsorderdetail> lslogilablimsorderdetail = lslogilablimsorderdetailRepository
				.findBybatchcode(objorder.getBatchcode());
		List<Long> batchcode = new ArrayList<Long>();

		if (lslogilablimsorderdetail != null && lslogilablimsorderdetail.size() > 0) {
			int i = 0;

			while (lslogilablimsorderdetail.size() > i) {
				batchcode.add(lslogilablimsorderdetail.get(i).getBatchcode());
				i++;
			}
		}
		ELNResults = ELNResultDetailsRepository.findBybatchcode(batchcode);

		mapOrders.put("SDMSResults", lsResults);
		mapOrders.put("ELNResults", ELNResults);

		lsLogilaborders = null;
		lslogilablimsorderdetail = null;
		lsResults = null;
		ELNResults = null;

		return mapOrders;
	}

	public Map<String, Object> GetResultsproto(LSlogilabprotocoldetail protoobj) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();

//		List<LSresultdetails> lsResults = new ArrayList<LSresultdetails>();
//		List<LSlogilablimsorder> lsLogilaborders = lslogilablimsorderRepository.findBybatchid(objorder.getBatchid());
//		List<String> lsorderno = new ArrayList<String>();
//
//		if (lsLogilaborders != null && lsLogilaborders.size() > 0) {
//			int i = 0;
//
//			while (lsLogilaborders.size() > i) {
//				lsorderno.add(lsLogilaborders.get(i).getOrderid().toString());
//				i++;
//			}
//		}
//		lsResults = lsresultdetailsRepository.findBylimsreferencecodeIn(lsorderno);

		List<ELNResultDetails> ELNResults = new ArrayList<ELNResultDetails>();
//		List<LSlogilablimsorderdetail> lslogilablimsorderdetail = lslogilablimsorderdetailRepository
//				.findBybatchcode(objorder.getBatchcode());
		List<LSlogilabprotocoldetail> lslogilabpro = LSlogilabprotocoldetailRepository
				.findByProtocolordercodeOrderByProtocolordercodeAsc(protoobj.getProtocolordercode());

		List<Long> protocolordercode = new ArrayList<Long>();

		if (lslogilabpro != null && lslogilabpro.size() > 0) {
			int i = 0;

			while (lslogilabpro.size() > i) {
				protocolordercode.add(lslogilabpro.get(i).getProtocolordercode());
				i++;
			}
		}
		ELNResults = ELNResultDetailsRepository.findBybatchcode(protocolordercode);

		// mapOrders.put("SDMSResults", lsResults);
		mapOrders.put("ELNResults", ELNResults);

		lslogilabpro = null;
		ELNResults = null;

		return mapOrders;
	}

	public void updateorderversioncontent(String Content, LSsamplefileversion objfile, Integer ismultitenant)
			throws IOException {
		if (ismultitenant == 1 || ismultitenant == 2) {
			Map<String, Object> objMap = objCloudFileManipulationservice.storecloudSheetsreturnwithpreUUID(Content,
					commonfunction.getcontainername(ismultitenant, TenantContext.getCurrentTenant()) + "orderversion");
			String fileUUID = (String) objMap.get("uuid");
			String fileURI = objMap.get("uri").toString();

			CloudOrderVersion objsavefile = new CloudOrderVersion();
			if (objfile.getFilesamplecodeversion() != null) {
				objsavefile.setId((long) objfile.getFilesamplecodeversion());
			} else {
				objsavefile.setId(1);
			}
//			objsavefile.setContent(Content);
			objsavefile.setFileuri(fileURI);
			objsavefile.setFileuid(fileUUID);
			objsavefile.setContainerstored(1);

			cloudOrderVersionRepository.save(objsavefile);

			objsavefile = null;
		} else {

			GridFSDBFile largefile = gridFsTemplate.findOne(
					new Query(Criteria.where("filename").is("orderversion_" + objfile.getFilesamplecodeversion())));
			if (largefile != null) {
				gridFsTemplate.delete(
						new Query(Criteria.where("filename").is("orderversion_" + objfile.getFilesamplecodeversion())));
			}
			gridFsTemplate.store(new ByteArrayInputStream(Content.getBytes(StandardCharsets.UTF_8)),
					"orderversion_" + objfile.getFilesamplecodeversion(), StandardCharsets.UTF_16);

		}
	}

	@SuppressWarnings("unused")
	public LSsamplefile SaveResultfile(LSsamplefile objfile) throws IOException {

		List<LSsamplefileversion> lstSampleVer = objfile.getLssamplefileversion();
		Integer target = objfile.getVersionno() == null ? 1 : objfile.getVersionno();

		Integer lastversionindex = IntStream.range(0, lstSampleVer.size())
				.filter(i -> target.equals(lstSampleVer.get(i).getVersionno())).findFirst().orElse(0);

		boolean versionexist = true;
		if (objfile.getLssamplefileversion().size() <= 0) {
			versionexist = false;
			lastversionindex = 0;
			LSsamplefileversion lsversion = new LSsamplefileversion();
			lsversion.setVersionname("1");
			lsversion.setVersionno(1);
			lsversion.setBatchcode(objfile.getBatchcode());
			lsversion.setTestid(objfile.getTestid());
			lsversion.setCreateby(objfile.getCreateby());
			lsversion.setModifiedby(objfile.getModifiedby());
			objfile.getLssamplefileversion().add(lsversion);

			lssamplefileversionRepository.save(objfile.getLssamplefileversion());

		}

		LSlogilablimsorderdetail orderDetail = lslogilablimsorderdetailRepository
				.findByBatchcodeOrderByBatchcodeDesc(objfile.getBatchcode());
		if (orderDetail.getOrdersaved().equals(0)) {
			orderDetail.setOrdersaved(1);
			lslogilablimsorderdetailRepository.save(orderDetail);
		}

		if (objfile.isDoversion() && versionexist) {

			Integer perviousversion = -1;
			if (objfile.getVersionno() != null && objfile.getVersionno() >= 2) {
				perviousversion = objfile.getVersionno() - 2;
			}

			String Contentversion = GetSamplefileconent(objfile, objfile.getIsmultitenant());
			objfile.getLssamplefileversion().get(lastversionindex).setFilecontent(null);
			for (int k = 0; k < objfile.getLssamplefileversion().size(); k++) {
				if (objfile.getLssamplefileversion().get(k).getFilesamplecodeversion() == null) {
					try {
						objfile.getLssamplefileversion().get(k).setCreatedate(commonfunction.getCurrentUtcTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			lssamplefileversionRepository.save(objfile.getLssamplefileversion());
			updateorderversioncontent(objfile.getFilecontent(), objfile.getLssamplefileversion().get(lastversionindex),
					objfile.getIsmultitenant());

		} else {
			updateorderversioncontent(objfile.getFilecontent(), objfile.getLssamplefileversion().get(lastversionindex),
					objfile.getIsmultitenant());
		}

		objfile.setProcessed(1);

		String Content = objfile.getFilecontent();
		objfile.setFilecontent(null);
		objfile.setLssampleresult(null);
		String tagvalues = objfile.getTagvalues();
		String resultvalues = objfile.getResultvalues();
		try {
			objfile.setModifieddate(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lssamplefileRepository.save(objfile);
		updateordercontent(Content, objfile, objfile.getIsmultitenant());
		updateordertagvalues(objfile, tagvalues);
		if (resultvalues != null && resultvalues != "" && !resultvalues.equalsIgnoreCase("[]")) {
			updateorderresultvalues(objfile, resultvalues);
		}
		objfile.setFilecontent(Content);

		if (objfile.getObjActivity() != null) {
			try {
				objfile.getObjActivity().setActivityDate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lsactivityRepository.save(objfile.getObjActivity());
		}

		objfile.setResponse(new Response());
		objfile.getResponse().setStatus(true);
		objfile.getResponse().setInformation("ID_DUMMY1");

		return objfile;
	}

	private void updateordertagvalues(LSsamplefile objfile, String tagvalues) {

		lsreportfile objreport = new lsreportfile();
		objreport.setId((long) objfile.getFilesamplecode());
		objreport.setContent(tagvalues);
		objreport.setBatchcode(objfile.getBatchcode());
		objreport.setContentstored(1);
		reportfileRepository.save(objreport);
		objreport = null;
	}

	private void updateorderresultvalues(LSsamplefile objfile, String tagvalues) {

		Lsresultfororders objreport = new Lsresultfororders();
		objreport.setId((long) objfile.getFilesamplecode());
		objreport.setBatchcode(objfile.getBatchcode());
		objreport.setContent(tagvalues);
		objreport.setContentstored(1);
		lsresultforordersRepository.save(objreport);
		objreport = null;
	}

	public List<Lsresultfororders> onGetResultValuesFromSelectedOrder(LSlogilablimsorderdetail objOrder) {

		List<Lsresultfororders> objResultList = lsresultforordersRepository.findByBatchcode(objOrder.getBatchcode());

		return objResultList;
	}

	public void updateordercontent(String Content, LSsamplefile objfile, Integer ismultitenant) throws IOException {

		String contentParams = "";
		String contentValues = "";

		Map<String, Object> objContent = commonfunction.getParamsAndValues(Content);

		contentValues = (String) objContent.get("values");
		contentParams = (String) objContent.get("parameters");

		if (ismultitenant == 1 || ismultitenant == 2) {

			Map<String, Object> objMap = objCloudFileManipulationservice.storecloudSheetsreturnwithpreUUID(Content,
					commonfunction.getcontainername(ismultitenant, TenantContext.getCurrentTenant()) + "ordercreation");
			String fileUUID = (String) objMap.get("uuid");
			String fileURI = objMap.get("uri").toString();

			CloudOrderCreation objsavefile = new CloudOrderCreation();
			objsavefile.setId((long) objfile.getFilesamplecode());
//			objsavefile.setContent(Content);
			objsavefile.setFileuri(fileURI);
			objsavefile.setFileuid(fileUUID);
			objsavefile.setContainerstored(1);
			objsavefile.setContentvalues(contentValues);
			objsavefile.setContentparameter(contentParams);

			cloudOrderCreationRepository.save(objsavefile);

			objsavefile = null;
		} else {
			OrderCreation objsavefile = new OrderCreation();
			objsavefile.setId((long) objfile.getFilesamplecode());
			objsavefile.setContentvalues(contentValues);
			objsavefile.setContentparameter(contentParams);

			Query query = new Query(Criteria.where("id").is(objsavefile.getId()));

			Boolean recordcount = mongoTemplate.exists(query, OrderCreation.class);

			if (!recordcount) {
				mongoTemplate.insert(objsavefile);
			} else {
				Update update = new Update();
				update.set("contentvalues", contentValues);
				update.set("contentparameter", contentParams);

				mongoTemplate.upsert(query, update, OrderCreation.class);
			}

			GridFSDBFile largefile = gridFsTemplate
					.findOne(new Query(Criteria.where("filename").is("order_" + objfile.getFilesamplecode())));
			if (largefile != null) {
				gridFsTemplate.delete(new Query(Criteria.where("filename").is("order_" + objfile.getFilesamplecode())));
			}
			gridFsTemplate.store(new ByteArrayInputStream(Content.getBytes(StandardCharsets.UTF_8)),
					"order_" + objfile.getFilesamplecode(), StandardCharsets.UTF_16);

			objsavefile = null;
		}

		contentParams = null;
		contentValues = null;
		objContent = null;
	}

//	public LSlogilablimsorderdetail UpdateLimsOrder(LSlogilablimsorderdetail objorder) {
//		List<LSlimsorder> lstorder = lslimsorderRepository.findBybatchid(objorder.getBatchid());
//
//		List<LSfilemethod> methodlst = LSfilemethodRepository
//				.findByFilecodeOrderByFilemethodcode(objorder.getLsfile().getFilecode());
//
//		List<LSfileparameter> lstParam = lsFileparameterRepository
//				.findByFilecodeOrderByFileparametercode(objorder.getLsfile().getFilecode());
//
//		if (!methodlst.isEmpty()) {
//			objorder.setInstrumentcode(methodlst.get(0).getInstrumentid());
//			objorder.setMethodcode(methodlst.get(0).getMethodid());
//		} else {
//			objorder.setInstrumentcode(objorder.getMethodcode());
//			objorder.setMethodcode(objorder.getMethodcode());
//		}
//		objorder.getLsfile().setLsparameter(lstParam);
//
//		lstorder.forEach((orders) -> orders.setMethodcode(objorder.getMethodcode()));
//		lstorder.forEach((orders) -> orders.setInstrumentcode(objorder.getInstrumentcode()));
//
//		objorder.getLssamplefile().setBatchcode(objorder.getBatchcode());
//		lslimsorderRepository.save(lstorder);
//
//		objorder.getLsfile().setIsmultitenant(objorder.getIsmultitenant());
//
//		String contString = getfileoncode(objorder.getLsfile());
//		objorder.getLsfile().setFilecontent(contString);
//
//		objorder.setFilecode(objorder.getLsfile().getFilecode());
//
//		objorder.getLssamplefile().setFilecontent(null);
//		lssamplefileRepository.save(objorder.getLssamplefile());
//
//		lslogilablimsorderdetailRepository.save(objorder);
//
//		LSsamplefileversion objverionfile = lssamplefileversionRepository
//				.findByBatchcodeAndVersionno(objorder.getBatchcode(), 1);
//
//		LSsamplefile objfile = objorder.getLssamplefile();
//
//		if (objverionfile == null && !contString.equals("") && contString != null) {
//
//			List<LSsamplefileversion> lstSampleVersion = new ArrayList<LSsamplefileversion>();
//			LSsamplefileversion objVersion = new LSsamplefileversion();
//			objVersion.setVersionname("1");
//			objVersion.setVersionno(1);
//			objVersion.setBatchcode(objfile.getBatchcode());
//			objVersion.setTestid(objfile.getTestid());
//			objVersion.setCreatebyuser(objorder.getLsuserMaster());
//			objVersion.setModifiedby(objorder.getLsuserMaster());
//			objVersion.setFilesamplecode(objfile);
//			objVersion.setCreatedate(objorder.getCreatedtimestamp());
//			lssamplefileversionRepository.save(objVersion);
////			objfile.getLssamplefileversion().add(objVersion);
//			lstSampleVersion.add(objverionfile);
//			objfile.setLssamplefileversion(lstSampleVersion);
//			updateorderversioncontent(contString, objVersion, objorder.getIsmultitenant());
//
//		}
//
//		if (objorder.getLssamplefile() != null && objorder.getLssamplefile().getProcessed() == 0) {
//			updateordercontent(contString, objorder.getLssamplefile(), objorder.getIsmultitenant());
//			objorder.getLssamplefile().setFilecontent(contString);
//		} else {
//
//			objorder.getLsfile().setFilecontent(null);
//
//			if (objorder.getIsmultitenant() == 1) {
//				CloudOrderCreation file = cloudOrderCreationRepository
//						.findById((long) objorder.getLssamplefile().getFilesamplecode());
//				if (file != null) {
//					objorder.getLssamplefile().setFilecontent(file.getContent());
//				}
//			} else {
//
//				String fileid = "order_" + objorder.getLssamplefile().getFilesamplecode();
//				GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileid)));
//				if (largefile == null) {
//					largefile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileid)));
//				}
//
//				if (largefile != null) {
//					String filecontent = new BufferedReader(
//							new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
//							.collect(Collectors.joining("\n"));
//					objorder.getLssamplefile().setFilecontent(filecontent);
//				} else {
//
//					OrderCreation file = mongoTemplate.findById(objorder.getLssamplefile().getFilesamplecode(),
//							OrderCreation.class);
//					if (file != null) {
//						objorder.getLssamplefile().setFilecontent(file.getContent());
//					}
//				}
//			}
//
//		}
//
//		return objorder;
//	}

	public LSlogilablimsorderdetail UpdateLimsOrder(LSlogilablimsorderdetail objorder) throws IOException {
		List<LSlogilablimsorder> lstorder = lslogilablimsorderRepository.findBybatchid(objorder.getBatchid());
		List<Lsbatchdetails> lstbatch = LsbatchdetailsRepository.findByBatchcode(objorder.getBatchcode());

		if (!LSfilemethodRepository.findByFilecodeOrderByFilemethodcode(objorder.getLsfile().getFilecode()).isEmpty()) {
			objorder.setInstrumentcode(LSfilemethodRepository
					.findByFilecodeOrderByFilemethodcode(objorder.getLsfile().getFilecode()).get(0).getInstrumentid());
			objorder.setMethodcode(LSfilemethodRepository
					.findByFilecodeOrderByFilemethodcode(objorder.getLsfile().getFilecode()).get(0).getMethodid());
		} else {
			objorder.setInstrumentcode(objorder.getMethodcode());
			objorder.setMethodcode(objorder.getMethodcode());
		}
		objorder.getLsfile().setLsparameter(
				lsFileparameterRepository.findByFilecodeOrderByFileparametercode(objorder.getLsfile().getFilecode()));

		lstorder.forEach((orders) -> orders.setMethodcode(objorder.getMethodcode()));
		lstorder.forEach((orders) -> orders.setInstrumentcode(objorder.getInstrumentcode()));

		objorder.setLsbatchdetails(lstbatch);

		objorder.getLssamplefile().setBatchcode(objorder.getBatchcode());
		lslogilablimsorderRepository.save(lstorder);

		objorder.getLsfile().setIsmultitenant(objorder.getIsmultitenant());

		String contString = getfileoncode(objorder.getLsfile());
		objorder.getLsfile().setFilecontent(contString);

		objorder.setFilecode(objorder.getLsfile().getFilecode());

		objorder.getLssamplefile().setFilecontent(null);
		lssamplefileRepository.save(objorder.getLssamplefile());

		lslogilablimsorderdetailRepository.save(objorder);

//		LSsamplefileversion objverionfile = lssamplefileversionRepository
//				.findByBatchcodeAndVersionno(objorder.getBatchcode(), 1);

		LSsamplefile objfile = objorder.getLssamplefile();

		if (lssamplefileversionRepository.findByBatchcodeAndVersionno(objorder.getBatchcode(), 1) == null
				&& !contString.equals("") && contString != null) {

			LSsamplefileversion objVersion = new LSsamplefileversion();
			objVersion.setVersionname("1");
			objVersion.setVersionno(1);
			objVersion.setBatchcode(objfile.getBatchcode());
			objVersion.setTestid(objfile.getTestid());
			objVersion.setCreatebyuser(objorder.getLsuserMaster());
			objVersion.setModifiedby(objorder.getLsuserMaster());
			objVersion.setFilesamplecode(objfile);
			objVersion.setCreatedate(objorder.getCreatedtimestamp());
			lssamplefileversionRepository.save(objVersion);
			objfile.getLssamplefileversion().add(objVersion);
			updateorderversioncontent(contString, objVersion, objorder.getIsmultitenant());

		}

		if (objorder.getLssamplefile() != null && objorder.getLssamplefile().getProcessed() != null
				&& objorder.getLssamplefile().getProcessed() == 0) {
			updateordercontent(contString, objorder.getLssamplefile(), objorder.getIsmultitenant());
			objorder.getLssamplefile().setFilecontent(contString);
		} else {

			objorder.getLsfile().setFilecontent(null);

			if (objorder.getIsmultitenant() == 1) {
//				CloudOrderCreation file = cloudOrderCreationRepository
//						.findById((long) objorder.getLssamplefile().getFilesamplecode());
				if (cloudOrderCreationRepository
						.findById((long) objorder.getLssamplefile().getFilesamplecode()) != null) {
					objorder.getLssamplefile().setFilecontent(cloudOrderCreationRepository
							.findById((long) objorder.getLssamplefile().getFilesamplecode()).getContent());
				}
			} else {

//				String fileid = "order_" + objorder.getLssamplefile().getFilesamplecode();
				GridFSDBFile largefile = gridFsTemplate.findOne(new Query(
						Criteria.where("filename").is("order_" + objorder.getLssamplefile().getFilesamplecode())));
				if (largefile == null) {
					largefile = gridFsTemplate.findOne(new Query(
							Criteria.where("_id").is("order_" + objorder.getLssamplefile().getFilesamplecode())));
				}

				if (largefile != null) {
//					String filecontent = new BufferedReader(
//							new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
//									.collect(Collectors.joining("\n"));
					objorder.getLssamplefile()
							.setFilecontent(new BufferedReader(
									new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
									.collect(Collectors.joining("\n")));
				} else {

//					OrderCreation file = mongoTemplate.findById(objorder.getLssamplefile().getFilesamplecode(),
//							OrderCreation.class);
					if (mongoTemplate.findById(objorder.getLssamplefile().getFilesamplecode(),
							OrderCreation.class) != null) {
						objorder.getLssamplefile()
								.setFilecontent(mongoTemplate
										.findById(objorder.getLssamplefile().getFilesamplecode(), OrderCreation.class)
										.getContent());
					}
				}
			}

		}

		final LSlogilablimsorderdetail objLSlogilablimsorder = objorder;

		new Thread(() -> {
			try {
				System.out.println("inside the thread SDMS order call");
				createLogilabLIMSOrder4SDMS(objLSlogilablimsorder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return objorder;
	}

	public LSlogilablimsorderdetail SheetChangeForLimsOrder(LSlogilablimsorderdetail objorder) throws IOException {
		List<LSlogilablimsorder> lstorder = lslogilablimsorderRepository.findByBatchidOrderByOrderidDesc(objorder.getBatchid());
		List<Lsbatchdetails> lstbatch = LsbatchdetailsRepository.findByBatchcode(objorder.getBatchcode());

//		if (!LSfilemethodRepository.findByFilecodeOrderByFilemethodcode(objorder.getLsfile().getFilecode()).isEmpty()) {
//			objorder.setInstrumentcode(LSfilemethodRepository
//					.findByFilecodeOrderByFilemethodcode(objorder.getLsfile().getFilecode()).get(0).getInstrumentid());
//			objorder.setMethodcode(LSfilemethodRepository
//					.findByFilecodeOrderByFilemethodcode(objorder.getLsfile().getFilecode()).get(0).getMethodid());
//		} else {
//			objorder.setInstrumentcode(objorder.getMethodcode());
//			objorder.setMethodcode(objorder.getMethodcode());
//		}
		objorder.getLsfile().setLsparameter(
				lsFileparameterRepository.findByFilecodeOrderByFileparametercode(objorder.getLsfile().getFilecode()));

//		lslogilablimsorderRepository.delete(lstorder);

		List<LSlogilablimsorder> lsorder = new ArrayList<LSlogilablimsorder>();
		Long Limsorder = lstorder.get(0).getOrderid();

		if (objorder.getLsfile() != null) {
			objorder.getLsfile().setLsmethods(
					LSfilemethodRepository.findByFilecodeOrderByFilemethodcode(objorder.getLsfile().getFilecode()));
			if (objorder.getLsfile().getLsmethods() != null && objorder.getLsfile().getLsmethods().size() > 0) {
				int methodindex = 1;
				for (LSfilemethod objmethod : objorder.getLsfile().getLsmethods()) {
					OptionalInt methodExist = IntStream.range(0, lstorder.size())
				            .filter(i -> lstorder.get(i).getInstrumentcode().equals(objmethod.getInstrumentid()) 
				            		&& lstorder.get(i).getMethodcode().equals(objmethod.getMethodid()))
				            .findFirst();
					if (!methodExist.isPresent()) {
						LSlogilablimsorder objLimsOrder = new LSlogilablimsorder();
						objLimsOrder.setOrderid(Limsorder + methodindex);
						objLimsOrder.setBatchid(objorder.getBatchid());
						objLimsOrder.setMethodcode(objmethod.getMethodid());
						objLimsOrder.setInstrumentcode(objmethod.getInstrumentid());
						objLimsOrder.setTestcode(objorder.getTestcode() != null ? objorder.getTestcode().toString() : null);
						objLimsOrder.setOrderflag("N");
						objLimsOrder.setCreatedtimestamp(objorder.getCreatedtimestamp());
	
						lsorder.add(objLimsOrder);
	
						methodindex++;
					}
				}

				lslogilablimsorderRepository.save(lsorder);
			}
		}

		objorder.setLsbatchdetails(lstbatch);
		objorder.getLssamplefile().setBatchcode(objorder.getBatchcode());
		objorder.getLsfile().setIsmultitenant(objorder.getIsmultitenant());

		String contString = getfileoncode(objorder.getLsfile());
		objorder.getLsfile().setFilecontent(contString);
		objorder.setFilecode(objorder.getLsfile().getFilecode());
		objorder.setFilename(objorder.getLsfile().getFilenameuser());

		objorder.getLssamplefile().setFilecontent(null);
		lssamplefileRepository.save(objorder.getLssamplefile());

		LSsamplefile objfile = objorder.getLssamplefile();

		if (lssamplefileversionRepository.findByBatchcodeAndVersionno(objorder.getBatchcode(), 1) != null
				&& !contString.equals("") && contString != null) {

			LSsamplefileversion objVersion = lssamplefileversionRepository
					.findByBatchcodeAndVersionno(objorder.getBatchcode(), 1);
			objVersion.setModifiedby(objorder.getLsuserMaster());
			objVersion.setFilesamplecode(objfile);
			lssamplefileversionRepository.save(objVersion);
			updateorderversioncontent(contString, objVersion, objorder.getIsmultitenant());

		}

		if (objorder.getLssamplefile() != null && objorder.getLssamplefile().getProcessed() != null
				&& objorder.getLssamplefile().getProcessed() == 1) {
			updateordercontent(contString, objorder.getLssamplefile(), objorder.getIsmultitenant());
			objorder.getLssamplefile().setFilecontent(contString);
		} else {

			objorder.getLsfile().setFilecontent(null);

			if (objorder.getIsmultitenant() == 1) {
				if (cloudOrderCreationRepository
						.findById((long) objorder.getLssamplefile().getFilesamplecode()) != null) {
					objorder.getLssamplefile().setFilecontent(cloudOrderCreationRepository
							.findById((long) objorder.getLssamplefile().getFilesamplecode()).getContent());
				}
			} else {

				String contentParams = "";
				String contentValues = "";

				Map<String, Object> objContent = commonfunction.getParamsAndValues(contString);

				contentValues = (String) objContent.get("values");
				contentParams = (String) objContent.get("parameters");

				OrderCreation objsavefile = new OrderCreation();
				objsavefile.setId((long) objfile.getFilesamplecode());
				objsavefile.setContentvalues(contentValues);
				objsavefile.setContentparameter(contentParams);

				Query query = new Query(Criteria.where("id").is(objsavefile.getId()));

				Boolean recordcount = mongoTemplate.exists(query, OrderCreation.class);

				if (!recordcount) {
					mongoTemplate.insert(objsavefile);
				} else {
					Update update = new Update();
					update.set("contentvalues", contentValues);
					update.set("contentparameter", contentParams);

					mongoTemplate.upsert(query, update, OrderCreation.class);
				}

				GridFSDBFile largefile = gridFsTemplate
						.findOne(new Query(Criteria.where("filename").is("order_" + objfile.getFilesamplecode())));
				if (largefile != null) {
					gridFsTemplate
							.delete(new Query(Criteria.where("filename").is("order_" + objfile.getFilesamplecode())));
				}
				gridFsTemplate.store(new ByteArrayInputStream(contString.getBytes(StandardCharsets.UTF_8)),
						"order_" + objfile.getFilesamplecode(), StandardCharsets.UTF_16);

				objorder.getLssamplefile().setFilecontent(contString);

			}

		}

		lslogilablimsorderdetailRepository.save(objorder);
		final LSlogilablimsorderdetail objLSlogilablimsorder = objorder;

		new Thread(() -> {
			try {
				System.out.println("inside the thread SDMS order call");
				createLogilabLIMSOrder4SDMS(objLSlogilablimsorder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return objorder;
	}

	public String getfileoncode(LSfile objfile) {

		String content = "";

		LSfile objreturnfile = LSfileRepository.findByfilecode(objfile.getFilecode());

		if (objreturnfile != null) {
			if (objfile.getIsmultitenant() == 1) {
//				CloudSheetCreation file = cloudSheetCreationRepository.findById((long) objfile.getFilecode());
				if (cloudSheetCreationRepository.findById((long) objfile.getFilecode()) != null) {
					content = commonfunction.getsheetdatawithfirstsheet(
							cloudSheetCreationRepository.findById((long) objfile.getFilecode()).getContent());
				}
			} else {

//				String fileid = "file_" + objfile.getFilecode();
				GridFSDBFile largefile = gridFsTemplate
						.findOne(new Query(Criteria.where("filename").is("file_" + objfile.getFilecode())));
				if (largefile == null) {
					largefile = gridFsTemplate
							.findOne(new Query(Criteria.where("_id").is("file_" + objfile.getFilecode())));
				}

				if (largefile != null) {
//					String filecontent = new BufferedReader(
//							new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
//									.collect(Collectors.joining("\n"));
					content = new BufferedReader(
							new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
							.collect(Collectors.joining("\n"));
				} else {

//					SheetCreation file = mongoTemplate.findById(objfile.getFilecode(), SheetCreation.class);
					if (mongoTemplate.findById(objfile.getFilecode(), SheetCreation.class) != null) {
						content = mongoTemplate.findById(objfile.getFilecode(), SheetCreation.class).getContent();
					}
				}
			}
		}

		return content;
	}

//	public LSlogilablimsorderdetail Getupdatedorder(LSlogilablimsorderdetail objorder) {
//		LSlogilablimsorderdetail objupdated = lslogilablimsorderdetailRepository.findOne(objorder.getBatchcode());
//		objupdated.setLsLSlimsorder(lSlimsorderRepository.findBybatchid(objorder.getBatchid()));
//
//		if (objorder.getFiletype() == 0) {
//			objupdated.setLstestparameter(lStestparameterRepository.findByntestcode(objupdated.getTestcode()));
//		}
//
//		if (objorder.getLssamplefile() != null) {
//			if (objorder.getIsmultitenant() == 1) {
//				CloudOrderCreation file = cloudOrderCreationRepository
//						.findById((long) objorder.getLssamplefile().getFilesamplecode());
//				if (file != null) {
//					objupdated.getLssamplefile().setFilecontent(file.getContent());
//				}
//			} else {
//				OrderCreation file = mongoTemplate.findById(objorder.getLssamplefile().getFilesamplecode(),
//						OrderCreation.class);
//				if (file != null) {
//					objupdated.getLssamplefile().setFilecontent(file.getContent());
//				}
//			}
//		}
//
//		return objupdated;
//	}

	public LSlogilablimsorderdetail Getupdatedorder(LSlogilablimsorderdetail objorder) {
		LSlogilablimsorderdetail objupdated = lslogilablimsorderdetailRepository.findOne(objorder.getBatchcode());
		objupdated.setLsLSlogilablimsorder(lslogilablimsorderRepository.findBybatchid(objorder.getBatchid()));

		if (objorder.getFiletype() == 0) {
			objupdated.setLstestparameter(lStestparameterRepository.findByntestcode(objupdated.getTestcode()));
		}

		if (objorder.getLssamplefile() != null) {
			if (objorder.getIsmultitenant() == 1) {
//				CloudOrderCreation file = cloudOrderCreationRepository
//						.findById((long) objorder.getLssamplefile().getFilesamplecode());
				if (cloudOrderCreationRepository
						.findById((long) objorder.getLssamplefile().getFilesamplecode()) != null) {
					objupdated.getLssamplefile().setFilecontent(cloudOrderCreationRepository
							.findById((long) objorder.getLssamplefile().getFilesamplecode()).getContent());
				}
			} else {
//				OrderCreation file = mongoTemplate.findById(objorder.getLssamplefile().getFilesamplecode(),
//						OrderCreation.class);
				if (mongoTemplate.findById(objorder.getLssamplefile().getFilesamplecode(),
						OrderCreation.class) != null) {
					objupdated.getLssamplefile()
							.setFilecontent(mongoTemplate
									.findById(objorder.getLssamplefile().getFilesamplecode(), OrderCreation.class)
									.getContent());
				}
			}
		}

		return objupdated;
	}

	public Map<String, Object> Getorderforlink(LSlogilablimsorderdetail objorder) {
		Map<String, Object> mapOrder = new HashMap<String, Object>();
		LSlogilablimsorderdetail objupdated = lslogilablimsorderdetailRepository.findOne(objorder.getBatchcode());

		if (objupdated.getLsprojectmaster() != null) {
			List<Integer> lstworkflowcode = objorder.getLstworkflow().stream().map(LSworkflow::getWorkflowcode)
					.collect(Collectors.toList());
			if (objorder.getLstworkflow() != null
					&& lstworkflowcode.contains(objupdated.getLsworkflow().getWorkflowcode())) {
				objupdated.setCanuserprocess(true);
			} else {
				objupdated.setCanuserprocess(false);
			}
		} else {
			objupdated.setCanuserprocess(true);
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

			if (objorder.getIsmultitenant() == 1 || objorder.getIsmultitenant() == 2) {
				CloudOrderCreation file = cloudOrderCreationRepository
						.findById((long) objupdated.getLssamplefile().getFilesamplecode());
				if (file != null) {
					objupdated.getLssamplefile().setFilecontent(file.getContent());
				}
			} else {

				String fileid = "order_" + objupdated.getLssamplefile().getFilesamplecode();
				GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileid)));
				if (largefile == null) {
					largefile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileid)));
				}

				if (largefile != null) {
					String filecontent = new BufferedReader(
							new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
							.collect(Collectors.joining("\n"));
					objupdated.getLssamplefile().setFilecontent(filecontent);
				} else {

					OrderCreation file = mongoTemplate.findById(objupdated.getLssamplefile().getFilesamplecode(),
							OrderCreation.class);
					if (file != null) {
						objupdated.getLssamplefile().setFilecontent(file.getContent());
					}
				}
			}

		}
		objupdated.setLstworkflow(objorder.getLstworkflow());
		mapOrder.put("order", objupdated);
		mapOrder.put("version",
				lssamplefileversionRepository.findByFilesamplecodeOrderByVersionnoDesc(objupdated.getLssamplefile()));

		return mapOrder;
	}

	public LSlogilablimsorderdetail CompleteOrder(LSlogilablimsorderdetail objorder) throws IOException {
		if (objorder.getLssamplefile().getLssamplefileversion() != null) {
			lssamplefileversionRepository.save(objorder.getLssamplefile().getLssamplefileversion());
		}
		lssampleresultRepository.save(objorder.getLssamplefile().getLssampleresult());
//		String Content = objorder.getLssamplefile().getFilecontent();
		objorder.getLssamplefile().setFilecontent(null);
		lssamplefileRepository.save(objorder.getLssamplefile());
		if(objorder.getLsparsedparameters() !=null) {
			objorder.getLsparsedparameters().forEach((param) -> param.setBatchcode(objorder.getBatchcode()));
			lsparsedparametersRespository.save(objorder.getLsparsedparameters());
			}	
//		lsorderworkflowhistoryRepositroy.save(objorder.getLsorderworkflowhistory());
		List<LsOrderattachments> lstattach = lsOrderattachmentsRepository
				.findByBatchcodeOrderByAttachmentcodeDesc(objorder.getBatchcode());
		objorder.setLsOrderattachments(lstattach);
		try {
			objorder.setCompletedtimestamp(commonfunction.getCurrentUtcTime());
			if (objorder.getLsordernotification() != null) {
				objorder.getLsordernotification().setIscompleted(true);
				lsordernotificationrepo.save(objorder.getLsordernotification());
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Lsorderworkflowhistory> objLstLsorderworkflowhistories = lsorderworkflowhistoryRepositroy
				.findByBatchcodeOrderByHistorycode(objorder.getBatchcode());
		lsorderworkflowhistoryRepositroy.save(objLstLsorderworkflowhistories);

		objorder.setLsorderworkflowhistory(objLstLsorderworkflowhistories);
		lslogilablimsorderdetailRepository.save(objorder);

//		if (objorder.getLssamplefile() != null) {
//			updateordercontent(Content, objorder.getLssamplefile(), objorder.getIsmultitenant());
//			objorder.getLssamplefile().setFilecontent(Content);
//		}

		updatenotificationfororder(objorder);
//		Content = null;
		objorder.setResponse(new Response());
		objorder.getResponse().setStatus(true);
		objorder.getResponse().setInformation("IDS_MSG_ORDERCMPLT");
		return objorder;
	}

	public LSlogilablimsorderdetail updateworflowforOrder(LSlogilablimsorderdetail objorder) throws ParseException {

		LSlogilablimsorderdetail objDbOrder = lslogilablimsorderdetailRepository.findOne(objorder.getBatchcode());

		
		
		updatenotificationfororderworkflow(objorder,
				lslogilablimsorderdetailRepository.findOne(objorder.getBatchcode()).getLsworkflow());

		// for email notify
		String email = env.getProperty("spring.emailnotificationconfig");
		if (email != null && email.equals("true")) {
			try {
				updateemailnotificationorderworkflow(objorder,
						lslogilablimsorderdetailRepository.findOne(objorder.getBatchcode()).getLsworkflow());

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			}

		}

		for (int k = 0; k < objorder.getLsorderworkflowhistory().size(); k++) {
			if (objorder.getLsorderworkflowhistory().get(k).getHistorycode() == null) {
				try {
					objorder.getLsorderworkflowhistory().get(k).setCreatedate(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		lsorderworkflowhistoryRepositroy.save(objorder.getLsorderworkflowhistory());
		try {
			objorder.setModifidate(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		objorder.setLockeduser(objDbOrder.getLockeduser());
		objorder.setLockedusername(objDbOrder.getLockedusername());
		objorder.setActiveuser(objDbOrder.getActiveuser());

		lslogilablimsorderdetailRepository.save(objorder);

		updatenotificationforworkflowapproval(objorder);

		if (objorder.getFiletype() != 0 && objorder.getOrderflag().toString().trim().equals("N")) {
			LSworkflow objlastworkflow = lsworkflowRepository
					.findTopByAndLssitemasterOrderByWorkflowcodeDesc(objorder.getObjLoggeduser().getLssitemaster());
			if (objlastworkflow != null
					&& objorder.getLsworkflow().getWorkflowcode() == objlastworkflow.getWorkflowcode()) {
				objorder.setIsFinalStep(1);
			} else {
				objorder.setIsFinalStep(0);
			}
		}
		// for eln trail order complete
		if (objorder.getIsmultitenant().equals(2) && objorder.getAccouttype().equals(1)) {
			objorder.setOrderflag("R");
			try {
				CompleteOrder(objorder);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(objorder.getApprovelstatus()!=null && objorder.getApprovelstatus()==3) {
			objDbOrder.setRepeat(false);
			objDbOrder.setBatchcode(objorder.getBatchcode());
			objDbOrder.setBatchid(objorder.getBatchid());
			stopautoregister(objDbOrder);
		}
		
		return objorder;
	}

	private void updateemailnotificationorderworkflow(LSlogilablimsorderdetail objorder, LSworkflow previousworkflow) {

		try {

			String Content = "";
			LSuserMaster obj = lsuserMasterRepository.findByusercode(objorder.getObjLoggeduser().getUsercode());

			if (objorder.getApprovelstatus() != null) {
				LSusersteam objteam = lsusersteamRepository
						.findByteamcode(objorder.getLsprojectmaster().getLsusersteam().getTeamcode());

				String previousworkflowname = previousworkflow != null ? previousworkflow.getWorkflowname() : "";

				if (objorder.getApprovelstatus() == 1) {
					Content = "Sheet Order: " + objorder.getBatchid() + " was sent from the workflow level : "
							+ previousworkflowname + " to : " + objorder.getLsworkflow().getWorkflowname() + " by "
							+ obj.getUsername() + "";
				} else if (objorder.getApprovelstatus() == 2) {
					Content = "Sheet Order: " + objorder.getBatchid()
							+ " was returned to the previous level of workflow from : " + previousworkflowname
							+ " to : " + objorder.getLsworkflow().getWorkflowname() + " by " + obj.getUsername() + "";
				} else if (objorder.getApprovelstatus() == 3) {
					Content = "Sheet Order: " + objorder.getBatchid() + " was rejected by " + obj.getUsername()
							+ "  upon review";
				}
				if (previousworkflowname.equals(objorder.getLsworkflow().getWorkflowname())
						&& objorder.getApprovelstatus() == 1) {
					Content = "Sheet Order: " + objorder.getBatchid() + " was approved by the " + obj.getUsername()
							+ " on the Workflow: " + objorder.getLsworkflow().getWorkflowname() + "";
				}
				List<LSuserteammapping> lstusers = objteam.getLsuserteammapping();
				List<Email> lstemail = new ArrayList<Email>();
				for (int i = 0; i < lstusers.size(); i++) {
					if (!(objorder.getObjLoggeduser().getUsercode()
							.equals(lstusers.get(i).getLsuserMaster().getUsercode()))) {
						Email objemail = new Email();
						MimeMessage message = mailSender.createMimeMessage();
						MimeMessageHelper helper = new MimeMessageHelper(message);

						if (lstusers.get(i).getLsuserMaster().getEmailid() != null) {
							objemail.setMailto(lstusers.get(i).getLsuserMaster().getEmailid());
							helper.setTo(lstusers.get(i).getLsuserMaster().getEmailid());
						}
						// email notification
						String subject = "Auto message generation for Sheet Order";
						String from = env.getProperty("spring.mail.username");
						objemail.setMailfrom(from);
						objemail.setMailcontent(Content);
						objemail.setSubject(subject);
						lstemail.add(objemail);
						helper.setSubject(subject);
						helper.setFrom(from);
						helper.setText("<p>" + Content + "</p><br><p> Order : </p><a href='" + objorder.getOrderlink()
								+ "'><b>" + objorder.getBatchid() + "</b></a>", true);

						try {
							mailSender.send(message);
							System.out.println("---email notification sent---");
						} catch (MailSendException e) {
							// TODO: handle exception
							System.out.println("---email notification log---");
							System.out.println(e);
						}

					}
				}

				EmailRepository.save(lstemail);
				lstemail.removeAll(lstemail);

			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	private void updatenotificationforworkflowapproval(LSlogilablimsorderdetail objorder) {
		String Details = "";
		String Notification = "";

		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
		LSuserMaster createby = lsuserMasterRepository.findByusercode(objorder.getLsuserMaster().getUsercode());
		LSuserMaster obj = lsuserMasterRepository.findByusercode(objorder.getObjLoggeduser().getUsercode());
		LSnotification objnotify = new LSnotification();

		try {
			for (int k = 0; k < objorder.getLsworkflow().getLsworkflowgroupmapping().size(); k++) {
//				List<LSMultiusergroup> userobj = lsMultiusergroupRepositery
//						.findBylsusergroup(objorder.getLsworkflow().getLsworkflowgroupmapping().get(k).getLsusergroup());
//	
//				List<Integer> objnotifyuser = userobj.stream().map(LSMultiusergroup::getUsercode) .collect(Collectors.toList());
//				
//				List<LSuserMaster> objuser = lsuserMasterRepository.findByUsercodeInAndUserretirestatusNot(objnotifyuser, 1);

				LSusersteam objteam = lsusersteamRepository
						.findByteamcode(objorder.getLsprojectmaster().getLsusersteam().getTeamcode());

				List<LSuserteammapping> lstusers = objteam.getLsuserteammapping();

				if (objorder.getApprovelstatus() != null && objorder.getIsFinalStep() != 1) {

					for (int i = 0; i < lstusers.size(); i++) {
						if (!(objorder.getObjLoggeduser().getUsercode()
								.equals(lstusers.get(i).getLsuserMaster().getUsercode()))) {

							if (objorder.getApprovelstatus() == 1) {
								Notification = "USERAPPROVAL";
								objnotify.setNotifationto(lstusers.get(i).getLsuserMaster());
							} else if (objorder.getApprovelstatus() == 2) {
								Notification = "USERORDERRETURN";
								objnotify.setNotifationto(lstusers.get(i).getLsuserMaster());

							} else if (objorder.getApprovelstatus() == 3) {
								Notification = "USERREJECT";
								objnotify.setNotifationto(createby);
							}

							Details = "{\"ordercode\":\"" + objorder.getBatchcode() + "\", \"order\":\""
									+ objorder.getBatchid() + "\", \"user\":\"" + obj.getUsername()
									+ "\", \"comment\":\"" + objorder.getComment() + "\", \"workflowname\":\""
									+ objorder.getLsworkflow().getWorkflowname() + "\"}";

							objnotify.setNotifationfrom(obj);
							objnotify.setNotificationdate(commonfunction.getCurrentUtcTime());
							objnotify.setNotification(Notification);
							objnotify.setNotificationdetils(Details);
							objnotify.setIsnewnotification(1);
							objnotify.setNotificationpath("/registertask");
							objnotify.setNotificationfor(1);

							lstnotifications.add(objnotify);
						}
					}
				} else {
					for (int i = 0; i < lstusers.size(); i++) {

						if (!(objorder.getObjLoggeduser().getUsercode()
								.equals(lstusers.get(i).getLsuserMaster().getUsercode()))) {

							if (objorder.getApprovelstatus() == 3 && objorder.getApproved() == null) {
								Notification = "USERREJECT";
								objnotify.setNotifationto(createby);
							} else if (objorder.getApproved() == 1 && objorder.getRejected() == null) {
								Notification = "SHEETORDERAPPROVED";
								objnotify.setNotifationto(createby);
							} else if (objorder.getApprovelstatus() == 2) {
								Notification = "USERRETURN";
								objnotify.setNotifationto(createby);
							}

							Details = "{\"ordercode\":\"" + objorder.getBatchcode() + "\", \"order\":\""
									+ objorder.getBatchid() + "\", \"user\":\"" + obj.getUsername()
									+ "\", \"comment\":\"" + objorder.getComment() + "\"}";
							objnotify.setNotifationfrom(obj);
							objnotify.setNotificationdate(commonfunction.getCurrentUtcTime());
							objnotify.setNotification(Notification);
							objnotify.setNotificationdetils(Details);
							objnotify.setIsnewnotification(1);
							objnotify.setNotificationpath("/registertask");
							objnotify.setNotificationfor(1);

							lstnotifications.add(objnotify);
						}
					}

				}
				lsnotificationRepository.save(lstnotifications);

				lstnotifications.removeAll(lstnotifications);
			}
		} catch (Exception e) {

		}
		Details = null;
		Notification = null;
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
			objfile.getObjActivity().getObjsilentaudit().setTableName("LSfile");
			try {
				objfile.getObjActivity().getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(objfile.getObjActivity().getObjsilentaudit());
		}

		objesixting = null;
		objLatestversion = null;
		objversion = null;

		return true;
	}

	public List<LSsamplefileversion> Getfileversions(LSsamplefile objfile) {
		return lssamplefileversionRepository.findByFilesamplecodeOrderByVersionnoDesc(objfile);
	}

	public String GetfileverContent(LSsamplefile objfile) throws IOException {
		String Content = "";
		if (objfile.getVersionno() == null || objfile.getVersionno() == 0) {
			Content = lssamplefileRepository.findByfilesamplecode(objfile.getFilesamplecode()).getFilecontent();
			if (objfile != null) {
				if (objfile.getIsmultitenant() == 1) {
					CloudOrderCreation objCreation = cloudOrderCreationRepository
							.findById((long) objfile.getFilesamplecode());
					if (objCreation != null && objCreation.getContainerstored() == 0) {
						Content = objCreation.getContent();
					} else {
						Content = objCloudFileManipulationservice.retrieveCloudSheets(objCreation.getFileuid(),
								TenantContext.getCurrentTenant() + "ordercreation");
					}
				} else {
					GridFSDBFile largefile = gridFsTemplate
							.findOne(new Query(Criteria.where("filename").is("order_" + objfile.getFilesamplecode())));
					if (largefile == null) {
						largefile = gridFsTemplate
								.findOne(new Query(Criteria.where("_id").is("order_" + objfile.getFilesamplecode())));
					}

					if (largefile != null) {
						Content = new BufferedReader(
								new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
								.collect(Collectors.joining("\n"));
					} else {
						if (mongoTemplate.findById(objfile.getFilesamplecode(), OrderCreation.class) != null) {
							Content = mongoTemplate.findById(objfile.getFilesamplecode(), OrderCreation.class)
									.getContent();
						}
					}
				}
			}
		} else {
			LSsamplefileversion objVersion = lssamplefileversionRepository
					.findByFilesamplecodeAndVersionnoOrderByVersionnoDesc(objfile, objfile.getVersionno());
			Content = objVersion.getFilecontent();

			if (objVersion != null) {
				if (objfile.getIsmultitenant() == 1 || objfile.getIsmultitenant() == 2) {
					CloudOrderVersion objCreation = cloudOrderVersionRepository
							.findById((long) objVersion.getFilesamplecodeversion());
					if (objCreation != null && objCreation.getContainerstored() == 0) {
						Content = objCreation.getContent();
					} else {
						Content = objCloudFileManipulationservice.retrieveCloudSheets(objCreation.getFileuid(),
								commonfunction.getcontainername(objfile.getIsmultitenant(),
										TenantContext.getCurrentTenant()) + "orderversion");
					}
				} else {

					GridFSDBFile largefile = gridFsTemplate.findOne(new Query(
							Criteria.where("filename").is("orderversion_" + objVersion.getFilesamplecodeversion())));
					if (largefile == null) {
						largefile = gridFsTemplate.findOne(new Query(
								Criteria.where("_id").is("orderversion_" + objVersion.getFilesamplecodeversion())));
					}

					if (largefile != null) {
						Content = new BufferedReader(
								new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
								.collect(Collectors.joining("\n"));
					} else {

						if (mongoTemplate.findById(objVersion.getFilesamplecodeversion(),
								OrderCreation.class) != null) {
							Content = mongoTemplate.findById(objVersion.getFilesamplecodeversion(), OrderCreation.class)
									.getContent();
						}
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
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
					objorder.getLsuserMaster().getLssitemaster());
			List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
//			List<LSsamplemaster> lstsample = lssamplemasterrepository.findByLssitemasterAndStatus(objorder.getLsuserMaster().getLssitemaster(), 1);

			List<Integer> lstsampleint = lssamplemasterrepository.getDistinctByLssitemasterSitecodeAndStatus(
					objorder.getLsuserMaster().getLssitemaster().getSitecode(), 1);
			List<LSsamplemaster> lstsample = new ArrayList<>();
			LSsamplemaster sample = null;
			if (lstsampleint.size() > 0) {
				for (Integer item : lstsampleint) {
					sample = new LSsamplemaster();
					sample.setSamplecode(item);
					lstsample.add(sample);
					sample = null; // Set sample to null after adding it to the list
				}
			}
//			lstorder = lslogilablimsorderdetailRepository
//					.findByFiletypeAndLsfileAndLsprojectmasterInOrderByBatchcodeDesc(objorder.getFiletype(),
//							objorder.getLsfile(), lstproject);

//			lstorder = lslogilablimsorderdetailRepository.
//			findByLsprojectmasterInAndFiletypeAndAssignedtoIsNullAndLsfileOrLsprojectmasterIsNullAndLssamplemasterIsNullAndFiletypeAndAssignedtoIsNullAndLsfileOrLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsfileOrLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsfileOrLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsfile(
//			lstproject, objorder.getFiletype(), objorder.getLsfile(),
//			objorder.getFiletype(), objorder.getLsfile(),
//			lstsample, objorder.getFiletype(), 1,objorder.getLsfile(),
//			lstsample, objorder.getFiletype(), 2,objorder.getLsfile(),
//			lstsample, objorder.getFiletype(), 3,objorder.getLsfile());

			lstorder = lslogilablimsorderdetailRepository
					.findByLsprojectmasterInAndFiletypeAndAssignedtoIsNullAndLsfile(lstproject, objorder.getFiletype(),
							objorder.getLsfile());

			int chunkSize = Integer.parseInt(env.getProperty("lssamplecount"));
			int totalSamples = lstsample.size();
			List<LSlogilablimsorderdetail> lstorderlimsobj = IntStream
					.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel().mapToObj(i -> {
						int startIndex = i * chunkSize;
						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
						List<LSsamplemaster> currentChunk = lstsample.subList(startIndex, endIndex);
						List<LSlogilablimsorderdetail> orderChunk = new ArrayList<>();
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterIsNullAndFiletypeAndAssignedtoIsNullAndLsfileOrderByBatchcodeDesc(
										objorder.getFiletype(), objorder.getLsfile()));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsfileOrderByBatchcodeDesc(
										currentChunk, objorder.getFiletype(), 1, objorder.getLsfile()));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsfileOrderByBatchcodeDesc(
										currentChunk, objorder.getFiletype(), 2, objorder.getLsfile()));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsfileOrderByBatchcodeDesc(
										currentChunk, objorder.getFiletype(), 3, objorder.getLsfile()));
						return orderChunk;
					}).flatMap(List::stream).collect(Collectors.toList());
			System.out.print(lstorderlimsobj);
			lstorderlimsobj.addAll(lstorder);
			lstorderlimsobj.forEach(objorderDetail -> objorderDetail.setLstworkflow(objorder.getLstworkflow()));
			return lstorderlimsobj;
		}

		return lstorder;

	}

//	public Map<String, Object> Getordersbylinkedfiles(Map<String, Object> mapObj) {
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		ObjectMapper object = new ObjectMapper();
//		int filetype = object.convertValue(mapObj.get("filetype"), Integer.class);
//		LSuserMaster lsusMaster = object.convertValue(mapObj.get("lsuserMaster"), LSuserMaster.class);
//		
//		@SuppressWarnings("unchecked")
//		List<LSfile> lSfiles = (List<LSfile>) object.convertValue(mapObj.get("lstLSfiles"), LSuserMaster.class);
//		
//		List<LSlogilablimsorderdetail> lstallorders = new ArrayList<LSlogilablimsorderdetail>();
//
//		for( int ind=0; ind <= lSfiles.size() ; ind++ ) {
//			
//			List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
//			
//			if (lsusMaster.getUsername().trim().toLowerCase().equals("administrator")) {
//				lstorder = lslogilablimsorderdetailRepository
//						.findByFiletypeAndLsfileOrderByBatchcodeDesc(filetype, lSfiles.get(ind));
//			} else {
//				List<LSuserteammapping> lstteammap = lsuserteammappingRepository
//						.findBylsuserMaster(lsusMaster);
//				List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
//						lsusMaster.getLssitemaster());
//				List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
//	
//				List<Integer> lstsampleint = lssamplemasterrepository.getDistinctByLssitemasterSitecodeAndStatus(
//						lsusMaster.getLssitemaster().getSitecode(), 1);
//				List<LSsamplemaster> lstsample = new ArrayList<>();
//				LSsamplemaster sample = null;
//				if (lstsampleint.size() > 0) {
//					for (Integer item : lstsampleint) {
//						sample = new LSsamplemaster();
//						sample.setSamplecode(item);
//						lstsample.add(sample);
//						sample = null; // Set sample to null after adding it to the list
//					}
//				}
//	
//				lstorder = lslogilablimsorderdetailRepository
//						.findByLsprojectmasterInAndFiletypeAndAssignedtoIsNullAndLsfile(lstproject, filetype,
//								lSfiles.get(ind));
//	
//				int chunkSize = Integer.parseInt(env.getProperty("lssamplecount"));
//				int totalSamples = lstsample.size();
//				List<LSlogilablimsorderdetail> lstorderlimsobj = IntStream
//						.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel().mapToObj(i -> {
//							int startIndex = i * chunkSize;
//							int endIndex = Math.min(startIndex + chunkSize, totalSamples);
//							List<LSsamplemaster> currentChunk = lstsample.subList(startIndex, endIndex);
//							List<LSlogilablimsorderdetail> orderChunk = new ArrayList<>();
//							orderChunk.addAll(lslogilablimsorderdetailRepository
//									.findByLsprojectmasterIsNullAndLssamplemasterIsNullAndFiletypeAndAssignedtoIsNullAndLsfile(
//											filetype, lSfiles.get(ind)));
//							orderChunk.addAll(lslogilablimsorderdetailRepository
//									.findByLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsfile(
//											currentChunk, filetype, 1, lSfiles.get(ind)));
//							orderChunk.addAll(lslogilablimsorderdetailRepository
//									.findByLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsfile(
//											currentChunk, filetype, 2, lSfiles.get(ind)));
//							orderChunk.addAll(lslogilablimsorderdetailRepository
//									.findByLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsfile(
//											currentChunk, filetype, 3, lSfiles.get(ind)));
//							return orderChunk;
//						}).flatMap(List::stream).collect(Collectors.toList());
//				System.out.print(lstorderlimsobj);
//				lstorderlimsobj.addAll(lstorder);
////				lstorderlimsobj.forEach(objorderDetail -> objorderDetail.setLstworkflow(objorder.getLstworkflow()));
////				return lstorderlimsobj;
//				lstallorders.addAll(lstorderlimsobj);
//			}
//			lstallorders.addAll(lstorder);
//		}
//		mapObj.put("orders", lstallorders);
//		return mapObj;
//
//	}

	public Map<String, Object> GetOrdersByLinkedFiles(Map<String, Object> mapObj) {
//	    Map<String, Object> map = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();
		int filetype = objectMapper.convertValue(mapObj.get("filetype"), Integer.class);
		LSuserMaster lsusMaster = objectMapper.convertValue(mapObj.get("lsuserMaster"), LSuserMaster.class);
		List<LSfile> lSfiles = objectMapper.convertValue(mapObj.get("lstLSfiles"), new TypeReference<List<LSfile>>() {
		});

		List<LSlogilablimsorderdetail> lstallorders = new ArrayList<>();

		for (LSfile lSfile : lSfiles) {
			List<LSlogilablimsorderdetail> lstorder = new ArrayList<>();

			if ("administrator".equalsIgnoreCase(lsusMaster.getUsername().trim())) {
				lstorder = lslogilablimsorderdetailRepository
						.findByFiletypeAndLsfileAndApprovelstatusNotAndOrdercancellIsNullOrFiletypeAndLsfileAndApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
								filetype, lSfile, 3, filetype, lSfile);
			} else {
				List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(lsusMaster);
				List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
						lsusMaster.getLssitemaster());
				List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);

				List<Integer> lstsampleint = lssamplemasterrepository
						.getDistinctByLssitemasterSitecodeAndStatus(lsusMaster.getLssitemaster().getSitecode(), 1);
				List<Elnmaterial> nmaterialcode = elnmaterialRepository
						.findByNsitecode(lsusMaster.getLssitemaster().getSitecode());
				List<LSsamplemaster> lstsample = new ArrayList<>();
				LSsamplemaster sample = null;
				if (lstsampleint.size() > 0) {
					for (Integer item : lstsampleint) {
						sample = new LSsamplemaster();
						sample.setSamplecode(item);
						lstsample.add(sample);
						sample = null; // Set sample to null after adding it to the list
					}
				}

				lstorder = lslogilablimsorderdetailRepository
						.findByLsprojectmasterInAndFiletypeAndLsfileAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterInAndFiletypeAndAssignedtoIsNullAndLsfileAndApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
								lstproject, filetype, lSfile, 3, lstproject, filetype, lSfile);

				int chunkSize = Integer.parseInt(env.getProperty("lssamplecount"));
				int totalSamples = nmaterialcode.size();

				List<LSlogilablimsorderdetail> lstorderlimsobj = IntStream
						.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel().mapToObj(i -> {
							int startIndex = i * chunkSize;
							int endIndex = Math.min(startIndex + chunkSize, totalSamples);
							List<Elnmaterial> currentChunk = nmaterialcode.subList(startIndex, endIndex);
//							List<LSsamplemaster> currentChunk = lstsample.subList(startIndex, endIndex);

							List<LSlogilablimsorderdetail> orderChunk = new ArrayList<>();
							orderChunk.addAll(lslogilablimsorderdetailRepository
									.findByLsprojectmasterIsNullAndLssamplemasterIsNullAndFiletypeAndAssignedtoIsNullAndLsfileOrderByBatchcodeDesc(
											filetype, lSfile));
							for (int viewOption = 1; viewOption <= 3; viewOption++) {
								orderChunk.addAll(lslogilablimsorderdetailRepository
										.findByLsprojectmasterIsNullAndElnmaterialInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsfileOrderByBatchcodeDesc(
												currentChunk, filetype, viewOption, lSfile));
							}
							return orderChunk;
						}).flatMap(List::stream).collect(Collectors.toList());

				lstorderlimsobj.addAll(lstorder);
				lstallorders.addAll(lstorderlimsobj);
			}
			lstallorders.addAll(lstorder);
		}

		List<LSlogilablimsorderdetail> uniqueOrders = lstallorders.stream().distinct().collect(Collectors.toList());
//		Collections.reverse(uniqueOrders);
		mapObj.put("orders", uniqueOrders);

		return mapObj;
	}

	public LSlogilablimsorderdetail GetorderForLINKsheet(LSlogilablimsorderdetail objorder) throws IOException {

		LSlogilablimsorderdetail objupdatedorder = lslogilablimsorderdetailRepository.findOne(objorder.getBatchcode());
		List<LSlogilablimsorder> lsLogilaborders = lslogilablimsorderRepository
				.findBybatchid(objupdatedorder.getBatchid());
		List<String> lsorderno = new ArrayList<String>();
		objupdatedorder.setResponse(new Response());

		if (lsLogilaborders != null && lsLogilaborders.size() > 0) {
			int i = 0;
			while (lsLogilaborders.size() > i) {
				lsorderno.add(lsLogilaborders.get(i).getOrderid().toString());
				i++;
			}
		}
		objupdatedorder.setLsLSlogilablimsorder(lsLogilaborders);

		if (objupdatedorder.getLsprojectmaster() != null && objorder.getLstworkflow() != null) {
			List<Integer> lstworkflowcode = objorder.getLstworkflow().stream().map(LSworkflow::getWorkflowcode)
					.collect(Collectors.toList());
			if (objorder.getLstworkflow() != null && objupdatedorder.getLsworkflow() != null
					&& lstworkflowcode.contains(objupdatedorder.getLsworkflow().getWorkflowcode())) {
				objupdatedorder.setCanuserprocess(true);
			} else {
				objupdatedorder.setCanuserprocess(false);
			}
		}

		if (objupdatedorder.getFiletype() != 0 && objupdatedorder.getOrderflag().toString().trim().equals("N")) {
			LSworkflow objlastworkflow = lsworkflowRepository
					.findTopByAndLssitemasterOrderByWorkflowcodeDesc(objorder.getObjLoggeduser().getLssitemaster());
			if (objlastworkflow != null && objupdatedorder.getLsworkflow() != null
					&& objupdatedorder.getLsworkflow().equals(objlastworkflow)) {
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
				CloudOrderCreation objCreation = cloudOrderCreationRepository
						.findById((long) objupdatedorder.getLssamplefile().getFilesamplecode());
				if (objCreation != null && objCreation.getContainerstored() == 0) {
					objupdatedorder.getLssamplefile().setFilecontent(objCreation.getContent());
				} else {
					objupdatedorder.getLssamplefile().setFilecontent(
							objCloudFileManipulationservice.retrieveCloudSheets(objCreation.getFileuid(),
									TenantContext.getCurrentTenant() + "ordercreation"));
				}
			} else {

				GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename")
						.is("order_" + objupdatedorder.getLssamplefile().getFilesamplecode())));
				if (largefile == null) {
					largefile = gridFsTemplate.findOne(new Query(Criteria.where("_id")
							.is("order_" + objupdatedorder.getLssamplefile().getFilesamplecode())));
				}

				if (largefile != null) {
					objupdatedorder.getLssamplefile()
							.setFilecontent(new BufferedReader(
									new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
									.collect(Collectors.joining("\n")));
				} else {

					if (mongoTemplate.findById(objupdatedorder.getLssamplefile().getFilesamplecode(),
							OrderCreation.class) != null) {
						objupdatedorder.getLssamplefile().setFilecontent(mongoTemplate
								.findById(objupdatedorder.getLssamplefile().getFilesamplecode(), OrderCreation.class)
								.getContent());
					}
				}
			}
		}

		lsLogilaborders = null;
		objupdatedorder.setLstworkflow(objorder.getLstworkflow());

		if (objorder.getLsuserMaster() != null && objorder.getLsuserMaster().getUnifieduserid() != null) {
			LScentralisedUsers objUserId = new LScentralisedUsers();
			objUserId.setUnifieduserid(objorder.getLsuserMaster().getUnifieduserid());
			objupdatedorder.setLscentralisedusers(userService.Getcentraliseduserbyid(objUserId));
		}
		objupdatedorder.getResponse().setStatus(true);
		return objupdatedorder;
	}

	@SuppressWarnings("unused")
	public List<LSlogilablimsorderdetail> Getexcelorder(LSlogilablimsorderdetail objorder) {

		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		if (objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
			lstorder = lslogilablimsorderdetailRepository
					.findByFiletypeAndApprovelstatusNotAndOrdercancellIsNullOrFiletypeAndApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), 3, objorder.getFiletype());
		} else {
			List<LSuserteammapping> lstteammap = lsuserteammappingRepository
					.findBylsuserMaster(objorder.getLsuserMaster());
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
					objorder.getLsuserMaster().getLssitemaster());
			List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
//			List<LSsamplemaster> lstsample = lssamplemasterrepository.findByLssitemasterAndStatus(objorder.getLsuserMaster().getLssitemaster(), 1);
			List<Integer> lstsampleint = lssamplemasterrepository.getDistinctByLssitemasterSitecodeAndStatus(
					objorder.getLsuserMaster().getLssitemaster().getSitecode(), 1);
			List<LSsamplemaster> lstsample = new ArrayList<>();
			List<Elnmaterial> nmaterialcode = elnmaterialRepository
					.findByNsitecode(objorder.getLsuserMaster().getLssitemaster().getSitecode());
			LSsamplemaster sample = null;
			if (lstsampleint.size() > 0) {
				for (Integer item : lstsampleint) {
					sample = new LSsamplemaster();
					sample.setSamplecode(item);
					lstsample.add(sample);
					sample = null; // Set sample to null after adding it to the list
				}
			}
			lstorder = lslogilablimsorderdetailRepository
					.findByFiletypeAndLsprojectmasterInAndApprovelstatusNotAndOrdercancellIsNullOrFiletypeAndLsprojectmasterInAndApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
							objorder.getFiletype(), lstproject, 3, objorder.getFiletype(), lstproject);

//			lstorder = lslogilablimsorderdetailRepository
//					.findByLsprojectmasterInAndFiletypeAndAssignedtoIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionOrLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionAndLsuserMasterInOrderByBatchcodeDesc(
//					lstproject, objorder.getFiletype(),
//					lstsample, objorder.getFiletype(),1,
//					lstsample, objorder.getFiletype(),2,objorder.getLsuserMaster(), 
//					lstsample, objorder.getFiletype(), 3,objorder.getLstuserMaster());

//			lstorder = lslogilablimsorderdetailRepository
//					.findByLsprojectmasterInAndFiletypeAndAssignedtoIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionOrLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoptionOrLsprojectmasterIsNullAndLssamplemasterInAndFiletypeAndAssignedtoIsNullAndViewoption
//					(lstproject, objorder.getFiletype(),
//					lstsample, objorder.getFiletype(),1,
//					lstsample, objorder.getFiletype(),2,
//					lstsample, objorder.getFiletype(),3);
			int chunkSize = Integer.parseInt(env.getProperty("lssamplecount"));
			int totalSamples = nmaterialcode.size();
			List<LSlogilablimsorderdetail> lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize)
					.parallel().mapToObj(i -> {
						int startIndex = i * chunkSize;
						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
//						List<LSsamplemaster> currentChunk = lstsample.subList(startIndex, endIndex);
						List<Elnmaterial> currentChunk = nmaterialcode.subList(startIndex, endIndex);
						List<LSlogilablimsorderdetail> orderChunk = new ArrayList<>();
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndElnmaterialInAndFiletypeAndAssignedtoIsNullAndViewoption(
										currentChunk, objorder.getFiletype(), 1));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndElnmaterialInAndFiletypeAndAssignedtoIsNullAndViewoption(
										currentChunk, objorder.getFiletype(), 2));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndElnmaterialInAndFiletypeAndAssignedtoIsNullAndViewoption(
										currentChunk, objorder.getFiletype(), 3));
						return orderChunk;
					}).flatMap(List::stream).collect(Collectors.toList());
			System.out.print(lstorderobj);
			lstorderobj.addAll(lstorder);
			lstorderobj.forEach(objorderDetail -> objorderDetail.setLstworkflow(objorder.getLstworkflow()));
			return lstorderobj;
		}
		return lstorder;

	}

	public LSlogilablimsorderdetail updateVersionandWorkflowhistory(LSlogilablimsorderdetail objorder) {
		objorder = lslogilablimsorderdetailRepository.findOne(objorder.getBatchcode());
		objorder.setObjsilentaudit(new LScfttransaction());
		objorder.setObjmanualaudit(new LScfttransaction());
		return objorder;
	}

	public LSsamplefile GetResultfileverContent(LSsamplefile objfile) throws IOException {

		LSsamplefile objesixting = lssamplefileRepository.findByfilesamplecode(objfile.getFilesamplecode());
		if (objesixting != null) {
			if (objfile.getIsmultitenant() == 1) {
				CloudOrderCreation objCreation = cloudOrderCreationRepository
						.findById((long) objfile.getFilesamplecode());
				if (objCreation != null && objCreation.getContainerstored() == 0) {
					lssamplefileRepository.findByfilesamplecode(objfile.getFilesamplecode())
							.setFilecontent(objCreation.getContent());
				} else {
					lssamplefileRepository.findByfilesamplecode(objfile.getFilesamplecode()).setFilecontent(
							objCloudFileManipulationservice.retrieveCloudSheets(objCreation.getFileuid(),
									TenantContext.getCurrentTenant() + "ordercreation"));
				}
			} else {
				if (mongoTemplate.findById(objesixting.getFilesamplecode(), OrderCreation.class) != null) {
					lssamplefileRepository.findByfilesamplecode(objfile.getFilesamplecode()).setFilecontent(
							mongoTemplate.findById(objesixting.getFilesamplecode(), OrderCreation.class).getContent());
				}
			}
		}
		return objesixting;
	}

	public LSlogilablimsorderdetail Uploadattachments(MultipartFile file, Long batchcode, String filename,
			String fileexe, Integer usercode, Date currentdate, Integer islargefile) throws IOException {

		LSlogilablimsorderdetail objorder = lslogilablimsorderdetailRepository.findOne(batchcode);

		LsOrderattachments parentobjattachment = lsOrderattachmentsRepository
				.findFirst1ByFilenameAndBatchcodeOrderByAttachmentcodeDesc(filename, batchcode);

		LsOrderattachments objattachment = new LsOrderattachments();

		if (islargefile == 0) {
//			OrderAttachment objfile = fileManipulationservice.storeattachment(file);
			if (fileManipulationservice.storeattachment(file) != null) {
				objattachment.setFileid(fileManipulationservice.storeattachment(file).getId());
			}
		} else {
//			String id = fileManipulationservice.storeLargeattachment(filename, file);
			if (fileManipulationservice.storeLargeattachment(filename, file) != null) {
				objattachment.setFileid(fileManipulationservice.storeLargeattachment(filename, file));
			}
		}

		if (parentobjattachment != null && filename != null && filename.lastIndexOf(".") > -1) {
			Integer versiondata = parentobjattachment.getVersion() != null ? parentobjattachment.getVersion() + 1 : 1;
			String originalname = filename.substring(0, filename.lastIndexOf("."));
			String extension = filename.substring(filename.lastIndexOf("."), filename.length());
			objattachment.setFilename(originalname + "_V" + (versiondata) + extension);
			parentobjattachment.setVersion(versiondata);
			lsOrderattachmentsRepository.save(parentobjattachment);
		} else {
			objattachment.setFilename(filename);
		}

		objattachment.setFileextension(fileexe);
		objattachment.setCreateby(lsuserMasterRepository.findByusercode(usercode));
		objattachment.setCreatedate(currentdate);
		objattachment.setBatchcode(objorder.getBatchcode());
		objattachment.setIslargefile(islargefile);
		objattachment.setVersion(0);

		LSuserMaster username = lsuserMasterRepository.findByusercode(usercode);
		String name = username.getUsername();
		LScfttransaction list = new LScfttransaction();
		list.setModuleName("Register Task Orders & Execute");
		list.setComments(
				name + " " + "Uploaded the attachement in Order ID: " + objorder.getBatchid() + " " + "successfully");
		list.setActions("Insert");
		list.setSystemcoments("System Generated");
		list.setTableName("profile");
//		list.setTransactiondate(currentdate);
		try {
			list.setTransactiondate(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		list.setLsuserMaster(usercode);
		lscfttransactionRepository.save(list);
		
		if (objorder != null) {
		    if (objorder.getLsOrderattachments() == null) {
		        objorder.setLsOrderattachments(new ArrayList<LsOrderattachments>());
		    }
		    objorder.getLsOrderattachments().add(objattachment);
		    objorder.getLsOrderattachments().sort((a1, a2) -> {
		        Long code1 = a1.getAttachmentcode();
		        Long code2 = a2.getAttachmentcode();
		        
		        if (code1 == null && code2 == null) {
		            return 0; // Both are null, considered equal
		        } else if (code1 == null) {
		            return -1; // Null values are placed first
		        } else if (code2 == null) {
		            return 1; // Null values are placed first
		        } else {
		            return code2.compareTo(code1); // Regular comparison, descending order
		        }
		    });
		}

		lsOrderattachmentsRepository.save(objorder.getLsOrderattachments());

		username = null;

		return objorder;
	}

	public LSlogilablimsorderdetail CloudUploadattachments(MultipartFile file, Long batchcode, String filename,
			String fileexe, Integer usercode, Date currentdate, Integer islargefile) throws IOException {

		LSlogilablimsorderdetail objorder = lslogilablimsorderdetailRepository.findOne(batchcode);

		LsOrderattachments parentobjattachment = lsOrderattachmentsRepository
				.findFirst1ByFilenameAndBatchcodeOrderByAttachmentcodeDesc(filename, batchcode);

		LsOrderattachments objattachment = new LsOrderattachments();

		if (islargefile == 0) {
			CloudOrderAttachment objfile = cloudFileManipulationservice.storeattachment(file);
			if (objfile != null) {
				objattachment.setFileid(objfile.getFileid());
			}
		}

		if (parentobjattachment != null && filename != null && filename.lastIndexOf(".") > -1) {
			Integer versiondata = parentobjattachment.getVersion() != null ? parentobjattachment.getVersion() + 1 : 1;
			String originalname = filename.substring(0, filename.lastIndexOf("."));
			String extension = filename.substring(filename.lastIndexOf("."), filename.length());
			objattachment.setFilename(originalname + "_V" + (versiondata) + extension);
			parentobjattachment.setVersion(versiondata);
			lsOrderattachmentsRepository.save(parentobjattachment);
		} else {
			objattachment.setFilename(filename);
		}
		objattachment.setFileextension(fileexe);
		objattachment.setCreateby(lsuserMasterRepository.findByusercode(usercode));
		objattachment.setCreatedate(currentdate);
		objattachment.setBatchcode(objorder.getBatchcode());
		objattachment.setIslargefile(islargefile);
		objattachment.setVersion(0);

		LSuserMaster username = lsuserMasterRepository.findByusercode(usercode);
		String name = username.getUsername();
		LScfttransaction list = new LScfttransaction();
		list.setModuleName("Register Task Orders & Execute");
		list.setComments(
				name + " " + "Uploaded the attachement in Order ID: " + objorder.getBatchid() + " " + "successfully");
		list.setActions("Insert");
		list.setSystemcoments("System Generated");
		list.setTableName("profile");
//		list.setTransactiondate(currentdate);
		try {
			list.setTransactiondate(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		list.setLsuserMaster(usercode);
		lscfttransactionRepository.save(list);
		
		if (objorder != null) {
		    if (objorder.getLsOrderattachments() == null) {
		        objorder.setLsOrderattachments(new ArrayList<LsOrderattachments>());
		    }
		    objorder.getLsOrderattachments().add(objattachment);
		    objorder.getLsOrderattachments().sort((a1, a2) -> {
		        Long code1 = a1.getAttachmentcode();
		        Long code2 = a2.getAttachmentcode();
		        
		        if (code1 == null && code2 == null) {
		            return 0; // Both are null, considered equal
		        } else if (code1 == null) {
		            return -1; // Null values are placed first
		        } else if (code2 == null) {
		            return 1; // Null values are placed first
		        } else {
		            return code2.compareTo(code1); // Regular comparison, descending order
		        }
		    });
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

		username = null;

		return objorder;
	}

//	public LSlogilablimsorderdetail CloudELNFileUploadattachments(MultipartFile file, Long batchcode, String filename,
//			String fileexe, Integer usercode, Date currentdate, Integer islargefile, Integer methodkey)
//			throws IOException {
//
//		LSlogilablimsorderdetail objorder = lslogilablimsorderdetailRepository.findOne(batchcode);
//
//		ELNFileAttachments objattachment = new ELNFileAttachments();
//
//		if (islargefile == 0) {
//			CloudOrderAttachment objfile = cloudFileManipulationservice.storeattachment(file);
//			if (objfile != null) {
//				objattachment.setFileid(objfile.getFileid());
//			}
//		}
//
//		objattachment.setFilename(filename);
//		objattachment.setFileextension(fileexe);
//		objattachment.setCreateby(lsuserMasterRepository.findByusercode(usercode));
//		objattachment.setCreatedate(currentdate);
//		objattachment.setBatchcode(objorder.getBatchcode());
//		objattachment.setIslargefile(islargefile);
//		objattachment.setMethodkey(methodkey);
//
//		LSuserMaster username = lsuserMasterRepository.findByusercode(usercode);
//		String name = username.getUsername();
//		LScfttransaction list = new LScfttransaction();
//		list.setModuleName("Register Task Orders & Execute");
//		list.setComments(
//				name + " " + "Uploaded the attachement in Order ID: " + objorder.getBatchid() + " " + "successfully");
//		list.setActions("Insert");
//		list.setSystemcoments("System Generated");
//		list.setTableName("profile");
//		list.setTransactiondate(currentdate);
//		list.setLsuserMaster(usercode);
//		lscfttransactionRepository.save(list);
//		if (objorder != null && objorder.getELNFileAttachments() != null) {
//			objorder.getELNFileAttachments().add(objattachment);
//		} else {
//			objorder.setELNFileAttachments(new ArrayList<ELNFileAttachments>());
//			objorder.getELNFileAttachments().add(objattachment);
//		}
//
//		elnFileattachmentsRepository.save(objorder.getELNFileAttachments());
//
//		if (islargefile == 1) {
//			@SuppressWarnings("unlikely-arg-type")
//			String filenameval = "attach_"
//					+ objorder.getBatchcode() + "_" + objorder.getLsOrderattachments()
//							.get(objorder.getLsOrderattachments().lastIndexOf(objattachment)).getAttachmentcode()
//					+ "_" + filename;
//			String id = cloudFileManipulationservice.storeLargeattachment(filenameval, file);
//			if (id != null) {
//				objattachment.setFileid(id);
//			}
//
//			elnFileattachmentsRepository.save(objorder.getELNFileAttachments());
//		}
//
//		return objorder;
//	}
//
//	public LSlogilablimsorderdetail Uploadelnfileattachments(MultipartFile file, Long batchcode, String filename,
//			String fileexe, Integer usercode, Date currentdate, Integer islargefile) throws IOException {
//
//		LSlogilablimsorderdetail objorder = lslogilablimsorderdetailRepository.findOne(batchcode);
//
//		ELNFileAttachments objattachment = new ELNFileAttachments();
//
//		if (islargefile == 0) {
//			OrderAttachment objfile = fileManipulationservice.storeattachment(file);
//			if (objfile != null) {
//				objattachment.setFileid(objfile.getId());
//			}
//		} else {
//			String id = fileManipulationservice.storeLargeattachment(filename, file);
//			if (id != null) {
//				objattachment.setFileid(id);
//			}
//		}
//
//		objattachment.setFilename(filename);
//		objattachment.setFileextension(fileexe);
//		objattachment.setCreateby(lsuserMasterRepository.findByusercode(usercode));
//		objattachment.setCreatedate(currentdate);
//		objattachment.setBatchcode(objorder.getBatchcode());
//		objattachment.setIslargefile(islargefile);
//
//		LSuserMaster username = lsuserMasterRepository.findByusercode(usercode);
//		String name = username.getUsername();
//		LScfttransaction list = new LScfttransaction();
//		list.setModuleName("Register Task Orders & Execute");
//		list.setComments(
//				name + " " + "Uploaded the attachement in Order ID: " + objorder.getBatchid() + " " + "successfully");
//		list.setActions("Insert");
//		list.setSystemcoments("System Generated");
//		list.setTableName("profile");
//		list.setTransactiondate(currentdate);
//		list.setLsuserMaster(usercode);
//		lscfttransactionRepository.save(list);
//		if (objorder != null && objorder.getELNFileAttachments() != null) {
//			objorder.getELNFileAttachments().add(objattachment);
//		} else {
//			objorder.setELNFileAttachments(new ArrayList<ELNFileAttachments>());
//			objorder.getELNFileAttachments().add(objattachment);
//		}
//
//		elnFileattachmentsRepository.save(objorder.getELNFileAttachments());
//
//		return objorder;
//	}

	public Map<String, Object> CloudELNFileUploadattachments(MultipartFile file, Long batchcode, String filename,
			String fileexe, Integer usercode, Date currentdate, Integer islargefile, Integer methodkey)
			throws IOException {

		Map<String, Object> map = new HashMap<>();

		LSlogilablimsorderdetail objorder = lslogilablimsorderdetailRepository.findOne(batchcode);
		LSlogilabprotocoldetail objproto = LSlogilabprotocoldetailRepository.findOne(batchcode);

		ELNFileAttachments objattachment = new ELNFileAttachments();

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
//		objattachment.setBatchcode(objorder.getBatchcode());
		objattachment.setIslargefile(islargefile);
		objattachment.setMethodkey(methodkey);

		LSuserMaster username = lsuserMasterRepository.findByusercode(usercode);
		String name = username.getUsername();
		LScfttransaction list = new LScfttransaction();
//		list.setModuleName("Register Task Orders & Execute");
//		list.setComments(
//				name + " " + "Uploaded the attachement in Order ID: " + objorder.getBatchid() + " " + "successfully");
//		list.setActions("Insert");
//		list.setSystemcoments("System Generated");
//		list.setTableName("profile");
//		try {
//			list.setTransactiondate(commonfunction.getCurrentUtcTime());
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		list.setLsuserMaster(usercode);
//		
		if (objorder != null && objorder.getELNFileAttachments() != null) {
			objorder.getELNFileAttachments().add(objattachment);
			elnFileattachmentsRepository.save(objorder.getELNFileAttachments());
		} else if (objorder != null) {
			objorder.setELNFileAttachments(new ArrayList<ELNFileAttachments>());
			objorder.getELNFileAttachments().add(objattachment);
			elnFileattachmentsRepository.save(objorder.getELNFileAttachments());
		}

		if (objorder == null) {
			objattachment.setBatchcode(objproto.getProtocolordercode());
			list.setModuleName("Register Protocol Orders");
			list.setComments(name + " " + "Uploaded the attachement in Protocol ID: " + objproto.getProtoclordername()
					+ " " + "successfully");
			list.setActions("Insert");
			list.setSystemcoments("System Generated");
			list.setTableName("profile");
			try {
				list.setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list.setLsuserMaster(usercode);

		} else {
			objattachment.setBatchcode(objorder.getBatchcode());
			list.setModuleName("Register Task Orders & Execute");
			list.setComments(name + " " + "Uploaded the attachement in Order ID: " + objorder.getBatchid() + " "
					+ "successfully");
			list.setActions("Insert");
			list.setSystemcoments("System Generated");
			list.setTableName("profile");
			try {
				list.setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list.setLsuserMaster(usercode);

		}

		lscfttransactionRepository.save(list);
//		if (objorder != null && objorder.getELNFileAttachments() != null) {
//			objorder.getELNFileAttachments().add(objattachment);
//		} else {
//			objorder.setELNFileAttachments(new ArrayList<ELNFileAttachments>());
//			objorder.getELNFileAttachments().add(objattachment);
//		}
//
//		elnFileattachmentsRepository.save(objorder.getELNFileAttachments());
//
//		objproto.getls
		if (islargefile == 1) {
			@SuppressWarnings("unlikely-arg-type")
			String filenameval = "attach_"
					+ objorder.getBatchcode() + "_" + objorder.getLsOrderattachments()
							.get(objorder.getLsOrderattachments().lastIndexOf(objattachment)).getAttachmentcode()
					+ "_" + filename;
			String id = cloudFileManipulationservice.storeLargeattachment(filenameval, file);
			if (id != null) {
				objattachment.setFileid(id);
			}

			elnFileattachmentsRepository.save(objorder.getELNFileAttachments());
		}
		map.put("attachmentdetails", objattachment);

		return map;
	}

//public LSlogilablimsorderdetail Uploadelnfileattachments(MultipartFile file, Long batchcode, String filename,
//		String fileexe, Integer usercode, Date currentdate, Integer islargefile) throws IOException {

	public Map<String, Object> Uploadelnfileattachments(MultipartFile file, Long batchcode, String filename,
			String fileexe, Integer usercode, Date currentdate, Integer islargefile) throws IOException {

		Map<String, Object> map = new HashMap<>();

		LSlogilablimsorderdetail objorder = lslogilablimsorderdetailRepository.findOne(batchcode);
		LSlogilabprotocoldetail objpro = LSlogilabprotocoldetailRepository.findByProtocolordercode(batchcode);

		ELNFileAttachments objattachment = new ELNFileAttachments();

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
		LSuserMaster username = lsuserMasterRepository.findByusercode(usercode);
		String name = username.getUsername();
		LScfttransaction list = new LScfttransaction();
		if (objorder == null) {
			objattachment.setBatchcode(objpro.getProtocolordercode());
			list.setModuleName("Register Protocol Orders");
			list.setComments(name + " " + "Uploaded the attachement in Protocol ID: " + objpro.getProtoclordername()
					+ " " + "successfully");
			list.setActions("Insert");
			list.setSystemcoments("System Generated");
			list.setTableName("profile");
			try {
				list.setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list.setLsuserMaster(usercode);

		} else {

			objattachment.setFilename(filename);
			objattachment.setFileextension(fileexe);
			objattachment.setCreateby(lsuserMasterRepository.findByusercode(usercode));
			objattachment.setCreatedate(currentdate);
			objattachment.setBatchcode(objorder.getBatchcode());
			objattachment.setIslargefile(islargefile);

			list.setModuleName("Register Task Orders & Execute");
			list.setComments(name + " " + "Uploaded the attachement in Order ID: " + objorder.getBatchid() + " "
					+ "successfully");
			list.setActions("Insert");
			list.setSystemcoments("System Generated");
			list.setTableName("profile");
//		list.setTransactiondate(currentdate);
			try {
				list.setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list.setLsuserMaster(usercode);
		}
		lscfttransactionRepository.save(list);
		if (objorder != null && objorder.getELNFileAttachments() != null) {
			objorder.getELNFileAttachments().add(objattachment);
		} else {
			objorder.setELNFileAttachments(new ArrayList<ELNFileAttachments>());
			objorder.getELNFileAttachments().add(objattachment);
		}

		elnFileattachmentsRepository.save(objorder.getELNFileAttachments());

		map.put("attachmentdetails", objattachment);

		return map;

	}

	public LsOrderattachments downloadattachments(LsOrderattachments objattachments) {
		OrderAttachment objfile = fileManipulationservice.retrieveFile(objattachments);
		if (objfile != null) {
			objattachments.setFile(objfile.getFile());

		}
//		silent audit
		if (objattachments.getObjsilentaudit() != null) {
			objattachments.getObjsilentaudit().setTableName("LsOrderattachments");
			try {
				objattachments.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(objattachments.getObjsilentaudit());
		}
		return objattachments;
	}

	public ELNFileAttachments downloadparserattachments(ELNFileAttachments objattachments) {
		OrderAttachment objfile = fileManipulationservice.retrieveFile(objattachments);
		if (objfile != null) {
			objattachments.setFile(objfile.getFile());

		}
//		silent audit
		if (objattachments.getObjsilentaudit() != null) {
			objattachments.getObjsilentaudit().setTableName("ELNFileAttachments");
			try {
				objattachments.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			try {
				objattachments.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(objattachments.getObjsilentaudit());
		}
		return objattachments;
	}

	public LsOrderattachments Cloudparserdownloadattachments(LsOrderattachments objattachments) {
		CloudOrderAttachment objfile = cloudFileManipulationservice.retrieveFile(objattachments);
		if (objfile != null) {
			objattachments.setFile(objfile.getFile());

		}
//		silent audit
		if (objattachments.getObjsilentaudit() != null) {
			objattachments.getObjsilentaudit().setTableName("LsOrderattachments");
			try {
				objattachments.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	public InputStream retrieveColudParserFile(String fileid, String tenant) throws IOException {
		return cloudFileManipulationservice.retrieveCloudFile(fileid, tenant + "parserfile");
		// return cloudFileManipulationservice.retrieveLargeFile(fileid);
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
			try {
				objattachments.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			try {
				objattachments.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

		String Details = "";
		String Notification = "";
		Notification = "SHAREORDER";

		Details = "{\"ordercode\":\"" + objordershareto.getOrder().getBatchcode() + "\", \"order\":\""
				+ objordershareto.getSharebatchid() + "\", \"sharedby\":\"" + objordershareto.getSharebyusername()
				+ "\", \"sharerights\":\"" + objordershareto.getSharerights() + "\"}";

		LSnotification objnotify = new LSnotification();
		objnotify.setNotifationfrom(objordershareto.getOrder().getObjLoggeduser());
		objnotify.setNotifationto(objordershareto.getUsersharedon());
		objnotify.setNotificationdate(objordershareto.getSharedon());
		objnotify.setNotification(Notification);
		objnotify.setNotificationdetils(Details);
		objnotify.setIsnewnotification(1);
		objnotify.setNotificationpath("/registertask");
		objnotify.setNotificationfor(1);
		lsnotificationRepository.save(objnotify);

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

	public Lsordersharedby GetsharedorderStatus(Lsordersharedby objorder) throws IOException, ParseException {

		LSlogilablimsorderdetail objorgorder = new LSlogilablimsorderdetail();

		objorgorder.setBatchcode(objorder.getSharebatchcode());
		objorgorder.setObjLoggeduser(objorder.getObjLoggeduser());
		objorgorder.setObjsilentaudit(objorder.getObjsilentaudit());
		objorgorder.setObjmanualaudit(objorder.getObjmanualaudit());
		objorgorder.setIsmultitenant(objorder.getIsmultitenant());

		LogilabOrderDetails objorgorderobj = GetorderStatus(objorgorder);

		objorder = lsordersharedbyRepository.findOne(objorder.getSharedbycode());

//		objorder.setObjorder(objorgorder);

		return objorder;
	}

	public Lsordershareto GetsharedtomeorderStatus(Lsordershareto objorder) throws IOException, ParseException {

		LSlogilablimsorderdetail objorgorder = new LSlogilablimsorderdetail();

		objorgorder.setBatchcode(objorder.getSharebatchcode());
		objorgorder.setObjLoggeduser(objorder.getObjLoggeduser());
		objorgorder.setObjsilentaudit(objorder.getObjsilentaudit());
		objorgorder.setObjmanualaudit(objorder.getObjmanualaudit());
		objorgorder.setIsmultitenant(objorder.getIsmultitenant());

		LogilabOrderDetails objorgorderobj = GetorderStatus(objorgorder);

		// objorder= lsordersharetoRepository.findOne(objorder.getSharetocode());

//		objorder.setObjorder(objorgorder);

		return objorder;
	}

	public LSsamplefile GetResultsharedfileverContent(LSsamplefile objfile) throws IOException {
		return GetResultfileverContent(objfile);
	}

	public LSsamplefile SaveSharedResultfile(LSsamplefile objfile) throws IOException {
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

	public ResponseEntity<InputStreamResource> downloadparserattachmentsNonCloud(String param, String fileid)
			throws IOException {

		if (param == null) {
			return null;
		}

		ELNFileAttachments objattach = elnFileattachmentsRepository.findFirst1ByfileidOrderByAttachmentcodeDesc(fileid);
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

	public ResponseEntity<InputStreamResource> downloadelnparserattachments(String param, String fileid) {

		if (param == null) {
			return null;
		}

		ELNFileAttachments objattach = elnFileattachmentsRepository.findFirst1ByfileidOrderByAttachmentcodeDesc(fileid);
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

	public ResponseEntity<InputStreamResource> downloadparserattachments(String param, String fileid) {

		if (param == null) {
			return null;
		}

		ELNFileAttachments objattach = elnFileattachmentsRepository.findFirst1ByfileidOrderByAttachmentcodeDesc(fileid);
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
			try {
				ordersample.setCreateddate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	public Map<String, Object> Consumableinventoryotification(Map<String, Object> argobj) {

		String Details = "";
		String Notification = "";
		ObjectMapper mapper = new ObjectMapper();
		LsOrderSampleUpdate ordersample = new LsOrderSampleUpdate();

		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
		Lsrepositoriesdata lsrepositoriesdata = new Lsrepositoriesdata();
		lsrepositoriesdata = mapper.convertValue(argobj.get("LsrepositoriesdataSeletedObj"), Lsrepositoriesdata.class);
		LsrepositoriesdataRepository.save(lsrepositoriesdata);
		LSuserMaster lsusermaster = new ObjectMapper().convertValue(argobj.get("usermaster"),
				new TypeReference<LSuserMaster>() {
				});

		ordersample.setLsusermaster(lsusermaster.getLsusermaster());

		LSnotification objnotify = new LSnotification();
		if (argobj.containsKey("Ordersampleobj")) {

			ordersample = mapper.convertValue(argobj.get("Ordersampleobj"), LsOrderSampleUpdate.class);
			if (ordersample.getInventoryused() == 20) {
				Notification = "INVENTORYCONSUMED";

				objnotify.setNotifationto(lsusermaster.getLsusermaster());

				Details = "{\"inventory\":\"" + lsrepositoriesdata.getInventoryid()
						+ lsrepositoriesdata.getRepositorydatacode() + "\", \"user\":\""
						+ lsrepositoriesdata.getAddedby() + "\"}";

				objnotify.setNotificationdate(ordersample.getCreateddate());
				objnotify.setNotification(Notification);
				objnotify.setNotificationdetils(Details);
				objnotify.setIsnewnotification(1);
				objnotify.setNotificationpath("/inventory");
				objnotify.setNotificationfor(1);

				lstnotifications.add(objnotify);
			}

			lsnotificationRepository.save(lstnotifications);

		}
		return argobj;

	}

	public Map<String, Object> Outofstockinventoryotification(Map<String, Object> argobj) {

		String Details = "";
		String Notification = "";
		ObjectMapper mapper = new ObjectMapper();
		LsOrderSampleUpdate ordersample = new LsOrderSampleUpdate();
		ordersample = mapper.convertValue(argobj.get("Ordersampleobj"), LsOrderSampleUpdate.class);
		lsOrderSampleUpdateRepository.save(ordersample);

		List<UserProjection> ordersample1 = lsOrderSampleUpdateRepository
				.getDistinctRepositorydatacode(ordersample.getRepositorydatacode());

		ArrayList<LSnotification> lstnotifications = new ArrayList<LSnotification>();

		for (int i = 0; i < ordersample1.size(); i++) {
			LSnotification objnotify = new LSnotification();
			LSuserMaster objuser = lsuserMasterRepository.findByusercode(ordersample1.get(i).getUsercode());
			if (ordersample.getInventoryused() == 0) {
				Notification = "INVENTORYOUTOFSTOCK";

				Details = "{\"orderid\":\"" + ordersample1.get(i).getBatchname()

						+ "\"}";
				objnotify.setNotifationto(objuser);
				objnotify.setNotificationdate(ordersample.getCreateddate());
				objnotify.setNotification(Notification);
				objnotify.setNotificationdetils(Details);
				objnotify.setIsnewnotification(1);
				objnotify.setNotificationpath("/inventory");
				objnotify.setNotificationfor(1);
				lstnotifications.add(objnotify);
				objnotify = null;

			}
			i++;

		}

		lsnotificationRepository.save(lstnotifications);
		return argobj;

	}

	public List<Lsrepositoriesdata> GetEditedOrderResources(Lsrepositoriesdata objorder) {
		// TODO Auto-generated method stub
		return LsrepositoriesdataRepository.findByRepositorycodeAndSitecodeAndItemstatusOrderByRepositorydatacodeDesc(
				objorder.getRepositorycode(), objorder.getSitecode(), 1);
	}

	public String getsampledata(Long batchcode, Integer indexrow, Integer cellindex, Integer sheetval, String tagdata,
			String tagvalue, String samplevalue, String sampledetails, Integer lssamplefile, Integer multitenant)
			throws IOException {

		String Content = "";
		if (multitenant == 1) {
			CloudOrderCreation objCreation = cloudOrderCreationRepository.findById((long) lssamplefile);
			if (objCreation != null && objCreation.getContainerstored() == 0) {
				Content = objCreation.getContent();
			} else {
				Content = objCloudFileManipulationservice.retrieveCloudSheets(objCreation.getFileuid(),
						TenantContext.getCurrentTenant() + "ordercreation");
			}

		} else {
			OrderCreation objsavefile = mongoTemplate.findById(lssamplefile, OrderCreation.class);
			if (objsavefile != null) {
				Content = objsavefile.getContent();
			}
		}

		return Content;
	}

	public Map<String, List<Integer>> Getuserworkflow(LSusergroup lsusergroup) {
		Map<String, List<Integer>> Rtn_object = new HashMap<>();
		if (lsusergroup != null) {
			List<LSworkflowgroupmapping> lsworkflowgroupmapping = lsworkflowgroupmappingRepository
					.findBylsusergroup(lsusergroup);

			List<LSworkflow> lsworkflow = lsworkflowRepository.findByLsworkflowgroupmappingIn(lsworkflowgroupmapping);

			Rtn_object.put("Protocol_Order",
					elnprotocolworkflowRepository
							.findByelnprotocolworkflowgroupmapIn(
									elnprotocolworkflowgroupmapRepository.findBylsusergroup(lsusergroup))
							.stream().map(Elnprotocolworkflow::getWorkflowcode).collect(Collectors.toList()));
			List<Integer> lstworkflow = new ArrayList<Integer>();
			if (lsworkflow != null && lsworkflow.size() > 0) {
				lstworkflow = lsworkflow.stream().map(LSworkflow::getWorkflowcode).collect(Collectors.toList());
			}
			Rtn_object.put("Sheet_Order", lstworkflow);

		} else {
//			List<Integer> lstworkflow = new ArrayList<Integer>();
			Rtn_object.put("Sheet_Order", Collections.emptyList());
			Rtn_object.put("Protocol_Order", Collections.emptyList());

		}
		return Rtn_object;
	}

	public Map<String, Object> Getuserprojects(LSuserMaster objuser) {
		if (objuser.getUsercode() != null) {
			Map<String, Object> objmap = new HashMap<>();
			List<LSuserteammapping> lstteammap = lsuserteammappingRepository
					.findByLsuserMasterAndTeamcodeNotNull(objuser);
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
					objuser.getLssitemaster());
			List<LSprojectmaster> lstprojectmaster = lsprojectmasterRepository.findByLsusersteamInAndStatus(lstteam, 1);

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
			if (lstteammap != null && lstteammap.size() > 0 && !lstteamcode.isEmpty()) {
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
		List<Long> immutableNegativeValues = Arrays.asList(-3L, -22L);
		if (objorder.getLsuserMaster() != null && objorder.getLsuserMaster().getUsername() != null) {
			if (objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
				mapOrders.put("orders", Getadministratororder(objorder));
				mapOrders.put("ordercount", lslogilablimsorderdetailRepository.count());
			} else {
				List<LSSheetOrderStructure> lstdir = new ArrayList<LSSheetOrderStructure>();
				if (objorder.getLstuserMaster().size() == 0) {
					lstdir = lsSheetOrderStructureRepository
							.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrderByDirectorycode(
									objorder.getLsuserMaster().getLssitemaster(), 1, immutableNegativeValues,
									objorder.getLsuserMaster(), 2);
				} else {
					lstdir = lsSheetOrderStructureRepository
							.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
									objorder.getLsuserMaster().getLssitemaster(), 1, immutableNegativeValues,
									objorder.getLsuserMaster(), 2, objorder.getLsuserMaster().getLssitemaster(), 3,
									objorder.getLstuserMaster());
				}
				lstdir.addAll(lsSheetOrderStructureRepository.findByDirectorycodeIn(immutableNegativeValues));
				List<Long> directorycode = lstdir.stream().map(LSSheetOrderStructure::getDirectorycode)
						.collect(Collectors.toList());
				mapOrders.put("directorycode", directorycode);
				mapOrders.put("orders", Getuserorder(objorder, directorycode));
				mapOrders.put("ordercount", lslogilablimsorderdetailRepository
						.countByLsprojectmasterIn(objorder.getLsuserMaster().getLstproject()));
			}
		}
		return mapOrders;
	}

	public List<Logilabordermaster> Getremainingorders(LSlogilablimsorderdetail objorder) {
		if (objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
			return Getadministratororder(objorder);
		} else {
			return Getuserorder(objorder, objorder.getLstdirectorycode());
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

	public List<Logilabordermaster> Getuserorder(LSlogilablimsorderdetail objorder, List<Long> directorycode) {
		List<LSprojectmaster> lstproject = objorder.getLsuserMaster().getLstproject();
		List<Logilabordermaster> lstorders = new ArrayList<Logilabordermaster>();
		try {
			if (lstproject != null) {
				List<LSworkflow> lstworkflow = objorder.getLsuserMaster().getLstworkflow();
				if (objorder.getBatchcode() == 0) {
					if (directorycode.size() == 0) {
						lstorders = lslogilablimsorderdetailRepository
								.findFirst20ByLsprojectmasterInOrderByBatchcodeDesc(lstproject);
					} else {
						lstorders = lslogilablimsorderdetailRepository
								.findFirst20ByLsprojectmasterInOrDirectorycodeInOrderByBatchcodeDesc(lstproject,
										directorycode);
					}
				} else {
					if (directorycode.size() == 0) {
						lstorders = lslogilablimsorderdetailRepository
								.findFirst20ByBatchcodeLessThanAndLsprojectmasterInOrderByBatchcodeDesc(
										objorder.getBatchcode(), lstproject);
					} else {
						lstorders = lslogilablimsorderdetailRepository
								.findFirst20ByBatchcodeLessThanAndLsprojectmasterInOrBatchcodeLessThanAndDirectorycodeInOrderByBatchcodeDesc(
										objorder.getBatchcode(), lstproject, objorder.getBatchcode(), directorycode);

					}
				}
				if (lstworkflow.size() > 0) {
					lstorders.forEach(objord -> objord.setLstworkflow(lstworkflow));
				}

			}
			return lstorders;
		} catch (Exception e) {
			return lstorders;
		}

	}

	public Map<String, Object> uploadsheetimages(MultipartFile file, String originurl, String username,
			String sitecode) {
		Map<String, Object> map = new HashMap<String, Object>();
//		int multitenant = Integer.parseInt(env.getProperty("ismultitenant"));
		String id = null;
		try {
			id = cloudFileManipulationservice.storecloudfilesreturnUUID(file, "sheetimagestemp");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final String getExtn = FilenameUtils.getExtension(file.getOriginalFilename()) == "" ? "png"
				: FilenameUtils.getExtension(file.getOriginalFilename());

//		if(multitenant == 2) {
//			map.put("link", originurl + "/Instrument/downloadsheetimagestemp/" + id + "/" + commonfunction.getcontainername(multitenant, TenantContext.getCurrentTenant())
//			+ "/" + FilenameUtils.removeExtension(file.getOriginalFilename()) + "/" + getExtn);
//		}else {
		map.put("link", originurl + "/Instrument/downloadsheetimagestemp/" + id + "/" + TenantContext.getCurrentTenant()
				+ "/" + FilenameUtils.removeExtension(file.getOriginalFilename()) + "/" + getExtn);
//		}

		return map;
	}

	public Map<String, Object> uploadfilessheetfolder(MultipartFile file, String uid, Long directorycode,
			String filefor, String tenantid, Integer ismultitenant, Integer usercode, Integer sitecode,
			Date createddate, Integer fileviewfor) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		LSsheetfolderfiles objfile = new LSsheetfolderfiles();
		objfile.setFilename(file.getOriginalFilename());
		objfile.setDirectorycode(directorycode);
		objfile.setFilefor(filefor);
		Response response = new Response();

//		Response response = validatefileexistonfolder(objfile);
//		if (response.getStatus()) {
		String uuID = "";
		if (ismultitenant == 1 || ismultitenant == 2) {
			uuID = cloudFileManipulationservice.storecloudfilesreturnwithpreUUID(file, "sheetfolderfiles", uid,
					ismultitenant);
		} else {
			uuID = fileManipulationservice.storeLargeattachmentwithpreuid(file.getOriginalFilename(), file, uid);
		}

		LSsheetfolderfiles parentobjattachment = lssheetfolderfilesRepository
				.findFirst1ByDirectorycodeAndFilenameOrderByFolderfilecode(directorycode, file.getOriginalFilename());

		LSsheetfolderfiles lsfiles = new LSsheetfolderfiles();
		lsfiles.setUuid(uuID);
		lsfiles.setFilesize(file.getSize());
		lsfiles.setDirectorycode(directorycode);

		if (parentobjattachment != null && file.getOriginalFilename() != null
				&& file.getOriginalFilename().lastIndexOf(".") > -1) {
			Integer versiondata = parentobjattachment.getVersion() != null ? parentobjattachment.getVersion() + 1 : 1;
			String originalname = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
			String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."),
					file.getOriginalFilename().length());
			lsfiles.setFilename(originalname + "_V" + (versiondata) + extension);
			parentobjattachment.setVersion(versiondata);
			lssheetfolderfilesRepository.save(parentobjattachment);
		} else {
			lsfiles.setFilename(file.getOriginalFilename());
		}

		LSuserMaster lsuser = new LSuserMaster();
		lsuser.setUsercode(usercode);
		lsfiles.setCreateby(lsuser);
		LSSiteMaster lssite = new LSSiteMaster();
		lssite.setSitecode(sitecode);
		lsfiles.setLssitemaster(lssite);
		lsfiles.setFilefor(filefor);
		lsfiles.setCreatedtimestamp(createddate);
		lsfiles.setFileviewfor(fileviewfor);
		lsfiles.setVersion(0);

		lssheetfolderfilesRepository.save(lsfiles);

//		} else {
//			response.setInformation("IDS_INFO_FILEEXIST");
//		}
		response.setStatus(true);
		map.put("res", response);
		map.put("uid", uid);
		map.put("name", lsfiles.getFilename());
		return map;
	}

	public Map<String, Object> removefilessheetfolder(String uid, Long directorycode, String filefor, String tenantid,
			Integer ismultitenant, Integer usercode, Integer sitecode, Date createddate, Integer fileviewfor)
			throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("file", lssheetfolderfilesRepository.findByUuid(uid));
		lssheetfolderfilesRepository.deleteByUuid(uid);
		if (ismultitenant == 1 || ismultitenant == 2) {
			cloudFileManipulationservice.deletecloudFile(uid, "sheetfolderfiles");
		} else {
			fileManipulationservice.deletelargeattachments(uid);
		}
		map.put("uid", uid);
		return map;
	}

	public Map<String, Object> removemultifilessheetfolder(LSsheetfolderfiles[] files, Long directorycode,
			String filefor, String tenantid,

			Integer ismultitenant, Integer usercode, Integer sitecode, Date createddate, Integer fileviewfor)
			throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();

		List<LSsheetfolderfiles> lstfile = Arrays.asList(files);
		if (lstfile.size() > 0) {
			List<String> lstfilesid = lstfile.stream().map(LSsheetfolderfiles::getUuid).collect(Collectors.toList());

			lssheetfolderfilesRepository.deleteByUuidIn(lstfilesid);

			for (String uuididex : lstfilesid) {

				if (ismultitenant == 1 || ismultitenant == 2) {
					cloudFileManipulationservice.deletecloudFile(uuididex, "sheetfolderfiles");
				} else {
					fileManipulationservice.deletelargeattachments(uuididex);
				}

			}

		}

		map.put("lstfilesid", lstfile);
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
//			int ismultitenant = Integer.parseInt(env.getProperty("ismultitenant"));
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

		final String getExtn = FilenameUtils.getExtension(file.getOriginalFilename()) == "" ? "png"
				: FilenameUtils.getExtension(file.getOriginalFilename());

		map.put("link", originurl + "/Instrument/downloadsheetimagestempsql/" + randomUUIDString + "/"
				+ FilenameUtils.removeExtension(file.getOriginalFilename()) + "/" + getExtn);

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
			String oldfileid = fileObj.get("oldfileid");
			String isnew = fileObj.get("isnew");

			Fileimagestemp oldFile = FileimagestempRepository.findByFileid(fileid);

			if (isnew != null && isnew.equals("true")) {

				Fileimages newFile = new Fileimages();

				newFile.setFile(oldFile.getFile());
				newFile.setFileid(fileid);
				newFile.setId(oldFile.getId());

				FileimagesRepository.save(newFile);
				FileimagestempRepository.delete(oldFile.getFileid());

			}
			Fileimages oldVerFile = FileimagesRepository.findByFileid(oldfileid);

			Integer id = oldFile == null ? oldVerFile.getId() : oldFile.getId();

			Fileimages newFile = new Fileimages();

			newFile.setFile(oldVerFile.getFile());
			newFile.setFileid(newFileid);
			newFile.setId(id);

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
			objupdated.setLockeduser(objorder.getObjLoggeduser().getUsercode());
			objupdated.setLockedusername(objorder.getObjLoggeduser().getUsername());
			objupdated.setIsLock(1);
		}

		lslogilablimsorderdetailRepository.save(objupdated);

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

		LSfile objFile = LSfileRepository.findByfilecode(objupdated.getFilecode());

		objFile.setIsmultitenant(objorder.getIsmultitenant());
		objupdated.setLsfile(objFile);

		if (objupdated.getLsfile() != null) {

			String contString = getfileoncode(objupdated.getLsfile());
			objupdated.getLsfile().setFilecontent(contString);

		}

		if (objupdated.getLssamplefile() != null) {

			if (objorder.getIsmultitenant() == 1) {
				CloudOrderCreation file = cloudOrderCreationRepository
						.findById((long) objupdated.getLssamplefile().getFilesamplecode());
				if (file != null) {
					objupdated.getLssamplefile().setFilecontent(file.getContent());
				}
			} else {

				String fileid = "order_" + objupdated.getLssamplefile().getFilesamplecode();
				GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileid)));
				if (largefile == null) {
					largefile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileid)));
				}

				if (largefile != null) {
					String filecontent = new BufferedReader(
							new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
							.collect(Collectors.joining("\n"));
					objupdated.getLssamplefile().setFilecontent(filecontent);
				} else {

					OrderCreation file = mongoTemplate.findById(objupdated.getLssamplefile().getFilesamplecode(),
							OrderCreation.class);
					if (file != null) {
						objupdated.getLssamplefile().setFilecontent(file.getContent());
					}
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

	public LSSheetOrderStructure Insertdirectory(LSSheetOrderStructure objdir) {
		Response objResponse = new Response();
		LSSheetOrderStructure lstdir = null;
		if (objdir.getDirectorycode() != null) {
			lstdir = lsSheetOrderStructureRepository
					.findByDirectorycodeAndParentdircodeAndDirectorynameNotAndSitemaster(objdir.getDirectorycode(),
							objdir.getParentdircode(), objdir.getDirectoryname(), objdir.getSitemaster());
		} else {
			lstdir = lsSheetOrderStructureRepository.findByDirectorynameIgnoreCaseAndParentdircodeAndSitemaster(
					objdir.getDirectoryname(), objdir.getParentdircode(), objdir.getSitemaster());
		}
		if (lstdir != null) {
			objResponse.setStatus(false);
			objResponse.setInformation("IDS_FolderExist");
		} else {
			objResponse.setStatus(true);
			objResponse.setInformation("IDS_FolderAdded");
		}
		objdir.setResponse(objResponse);
		return objdir;
	}

	public LSSheetOrderStructure Insertnewdirectory(LSSheetOrderStructure objdir) {
		lsSheetOrderStructureRepository.save(objdir);
		return objdir;
	}

	public Map<String, Object> Getfoldersfororders(LSlogilablimsorderdetail objorder) {

		Map<String, Object> mapfolders = new HashMap<String, Object>();
		List<LSSheetOrderStructure> lstdir = new ArrayList<LSSheetOrderStructure>();
		List<Long> immutableNegativeValues = Arrays.asList(-3L, -22L);
		if (objorder.getLstuserMaster() == null) {
			lstdir = lsSheetOrderStructureRepository
					.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
							objorder.getLsuserMaster().getLssitemaster(), 1, immutableNegativeValues,
							objorder.getLsuserMaster(), 2, objorder.getLsuserMaster(), 3);
		} else {
			lstdir = lsSheetOrderStructureRepository
					.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
							objorder.getLsuserMaster().getLssitemaster(), 1, immutableNegativeValues,
							objorder.getLsuserMaster(), 2, objorder.getLsuserMaster().getLssitemaster(), 3,
							objorder.getLstuserMaster());
		}
		lstdir.addAll(lsSheetOrderStructureRepository.findByDirectorycodeIn(immutableNegativeValues));
		if (objorder.getLstproject() != null && objorder.getLstproject().size() > 0) {
			ArrayList<List<Object>> lsttest = new ArrayList<List<Object>>();
			List<Integer> lsprojectcode = objorder.getLstproject().stream().map(LSprojectmaster::getProjectcode)
					.collect(Collectors.toList());
			List<LSprojectmaster> lstproject = lsprojectmasterRepository.findAll(lsprojectcode);
			lsttest = lslogilablimsorderdetailRepository
					.getLstestmasterlocalByOrderdisplaytypeAndLsprojectmasterInAndTestcodeIsNotNull(lsprojectcode);
//			lstorders.forEach(objorderDetail -> objorderDetail.setLstworkflow(objorder.getLstworkflow()));
			mapfolders.put("tests", lsttest);
			mapfolders.put("projects", lstproject);
		} else {
			mapfolders.put("tests", new ArrayList<Logilaborders>());
			mapfolders.put("projects", new ArrayList<Projectmaster>());
		}

//		List<LSsamplemaster> lstsample = lssamplemasterrepository.findSamplecodeAndSamplenameBystatusAndLssitemaster(1,
//				objorder.getLsuserMaster().getLssitemaster());
//		if (lstsample != null && lstsample.size() > 0) {
//			List<Integer> lssamplecode = lstsample.stream().map(LSsamplemaster::getSamplecode)
//					.collect(Collectors.toList());
//			int totalSamples = lssamplecode.size();
//			int chunkSize = Integer.parseInt(env.getProperty("lssamplecount"));
//			List<Object> lsttest = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel()
//					.mapToObj(i -> {
//						int startIndex = i * chunkSize;
//						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
//						List<Integer> currentChunk = lssamplecode.subList(startIndex, endIndex);
//
//						List<Object> orderChunk = lslogilablimsorderdetailRepository
//								.getLstestmasterlocalByOrderdisplaytypeAndLSsamplemasterInAndTestcodeIsNotNull(
//										currentChunk);
//						return orderChunk;
//					}).flatMap(List::stream) // Flatten the list of arrays to a single list
//					.collect(Collectors.toList());
		List<Elnmaterial> Matireal_List = elnmaterialRepository
				.findByNsitecodeOrderByNmaterialcodeDesc(objorder.getLsuserMaster().getLssitemaster().getSitecode());
		List<Object> Material_List_Ordes = lslogilablimsorderdetailRepository
				.getLstestmasterlocalAndmaterialByOrderdisplaytypeAndLSsamplemasterInAndTestcodeIsNotNull(
						objorder.getLsuserMaster().getLssitemaster().getSitecode());
		mapfolders.put("Materialtest", Material_List_Ordes);
		mapfolders.put("Material", Matireal_List);
//			mapfolders.put("sampletests", lsttest);
//			mapfolders.put("samples", lstsample);
//		} else {
//			mapfolders.put("Material", new ArrayList<Elnmaterial>());
////			mapfolders.put("sampletests", new ArrayList<Logilaborders>());
////			mapfolders.put("samples", new ArrayList<Samplemaster>());
//		}

//		List<LSusersteam> obj = lsusersteamRepository
//				.findBylssitemasterAndStatus(objorder.getLsuserMaster().getLssitemaster(), 1);
//
//		lstdir.forEach(e -> e.setTeamname(obj.stream().filter(a -> a.getTeamcode().equals(e.getTeamcode()))
//				.map(LSusersteam::getTeamname).findAny().orElse(null)));

		mapfolders.put("directory", lstdir);
//		new Thread(() -> {
//			try {
//				
//				System.out.println("duedatenot");
//				Notification ordernot = new Notification();
//				ordernot.setCurrentdate(new Date());
//				ordernot.setUsercode(objorder.getLsuserMaster().getUsercode());
//				LoginService.Duedatenotification(ordernot);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}).start();

//		new Thread(() -> {
//			try {
//				
//				System.out.println("autoregisterorder");
//
//				List<LSlogilablimsorderdetail> orderobjdata = new ArrayList<LSlogilablimsorderdetail>();
//				orderobjdata=lslogilablimsorderdetailRepository.findByLsuserMasterAndRepeat(objorder.getLsuserMaster(),true);
//				
//				InsertAutoRegisterOrder(orderobjdata);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}).start();

		return mapfolders;
	}

	public LSlogilablimsorderdetail UpdateFolderfororder(LSlogilablimsorderdetail order) {
		lslogilablimsorderdetailRepository.updatedirectory(order.getDirectorycode(), order.getBatchcode());
		return order;
	}

	public List<LSlogilablimsorderdetail> UpdateFolderfororders(LSlogilablimsorderdetail[] orderary) {
		List<LSlogilablimsorderdetail> order = Arrays.asList(orderary);
		if (order.size() > 0) {
			List<Long> lstorders = order.stream().map(LSlogilablimsorderdetail::getBatchcode)
					.collect(Collectors.toList());
			lslogilablimsorderdetailRepository.updatedirectory(order.get(0).getDirectorycode(), lstorders);
		}
		return order;
	}

	public List<Logilaborders> Getordersondirectory(LSSheetOrderStructure objdir) {
		List<Logilaborders> lstorder = new ArrayList<Logilaborders>();

//		LSSheetOrderStructure lstorderstr = new LSSheetOrderStructure();
		List<Logilaborders> lstorderstrcarray = new ArrayList<Logilaborders>();

		Date fromdate = objdir.getObjuser().getFromdate();
		Date todate = objdir.getObjuser().getTodate();
		Integer filetype = objdir.getFiletype();

		if (filetype != null && filetype == -1) {
			if (objdir.getLstuserMaster() == null) {

				if (objdir.getDirectorycode() == -3L || objdir.getDirectorycode() == -22L) {
//					
					lstorder = lslogilablimsorderdetailRepository
							.findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
									objdir.getDirectorycode(), 1, fromdate, todate, objdir.getCreatedby());
				} else {
					lstorder = lslogilablimsorderdetailRepository
							.findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									objdir.getDirectorycode(), 1, fromdate, todate);

				}
				lstorder.addAll(lslogilablimsorderdetailRepository
						.findByDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								objdir.getDirectorycode(), 2, objdir.getCreatedby(), fromdate, todate,
								objdir.getDirectorycode(), 3, objdir.getCreatedby(), fromdate, todate));

			} else {
				if (objdir.getDirectorycode() == -3L || objdir.getDirectorycode() == -22L) {
					lstorder = lslogilablimsorderdetailRepository
							.findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
									objdir.getDirectorycode(), 1, fromdate, todate, objdir.getCreatedby());
				} else {
					lstorder = lslogilablimsorderdetailRepository
							.findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									objdir.getDirectorycode(), 1, fromdate, todate);

				}
				lstorder.addAll(lslogilablimsorderdetailRepository
						.findByDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInOrderByBatchcodeDesc(
								objdir.getDirectorycode(), 2, objdir.getCreatedby(), fromdate, todate,
								objdir.getDirectorycode(), 3, fromdate, todate, objdir.getLstuserMaster()));

			}
		} else {
			if (objdir.getLstuserMaster() == null) {
				if (objdir.getDirectorycode() == -3L || objdir.getDirectorycode() == -22L) {
					lstorder = lslogilablimsorderdetailRepository
							.findByDirectorycodeAndViewoptionAndFiletypeAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
									objdir.getDirectorycode(), 1, filetype, fromdate, todate, objdir.getCreatedby());
				} else {
					lstorder = lslogilablimsorderdetailRepository
							.findByDirectorycodeAndViewoptionAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									objdir.getDirectorycode(), 1, filetype, fromdate, todate);
				}
				lstorder.addAll(lslogilablimsorderdetailRepository
						.findByDirectorycodeAndViewoptionAndLsuserMasterAndFiletypeAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndLsuserMasterAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								objdir.getDirectorycode(), 2, objdir.getCreatedby(), filetype, fromdate, todate,
								objdir.getDirectorycode(), 3, objdir.getCreatedby(), filetype, fromdate, todate));

			} else {
				if (objdir.getDirectorycode() == -3L || objdir.getDirectorycode() == -22L) {
					lstorder = lslogilablimsorderdetailRepository
							.findByDirectorycodeAndViewoptionAndFiletypeAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
									objdir.getDirectorycode(), 1, filetype, fromdate, todate, objdir.getCreatedby());
				} else {
					lstorder = lslogilablimsorderdetailRepository
							.findByDirectorycodeAndViewoptionAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									objdir.getDirectorycode(), 1, filetype, fromdate, todate);
				}
				lstorder.addAll(lslogilablimsorderdetailRepository
						.findByDirectorycodeAndViewoptionAndLsuserMasterAndFiletypeAndCreatedtimestampBetweenOrDirectorycodeAndViewoptionAndFiletypeAndCreatedtimestampBetweenAndLsuserMasterInOrderByBatchcodeDesc(
								objdir.getDirectorycode(), 2, objdir.getCreatedby(), filetype, fromdate, todate,
								objdir.getDirectorycode(), 3, filetype, fromdate, todate, objdir.getLstuserMaster()));

			}
		}
		lstorder.forEach(objorderDetail -> objorderDetail.setLstworkflow(objdir.getLstworkflow()));
		if (objdir.getSearchCriteria() != null && objdir.getSearchCriteria().getContentsearchtype() != null
				&& objdir.getSearchCriteria().getContentsearch() != null) {

			lstorderstrcarray = GetordersondirectoryFilter(objdir, lstorder);
			return lstorderstrcarray;
		}
	
//		if (objdir.getSearchCriteria() != null && objdir.getSearchCriteria().getContentsearchtype() != null
//				&& objdir.getSearchCriteria().getContentsearch() != null) {
//			// lstorderstrcarray.forEach(objorderDetail ->
//			// objorderDetail.setCanuserprocess(true));
//
//		
//		} 
//			else {
//			// lstorder.forEach(objorderDetail -> objorderDetail.setCanuserprocess(true));
		
//
			return lstorder;
//		}
			
	}

	public List<LSSheetOrderStructure> Deletedirectories(LSSheetOrderStructure[] directories) {
		List<LSSheetOrderStructure> lstdirectories = Arrays.asList(directories);

		lstdirectories.forEach(structure -> {
			if (structure.getParentdircode() == -2) {
				lsSheetOrderStructureRepository.delete(structure.getDirectorycode());
				lslogilablimsorderdetailRepository.updateparentdirectory(structure.getDircodetomove(),
						structure.getDirectorycode());
			} else {
				lsSheetOrderStructureRepository.updatedirectory(structure.getParentdircode(), structure.getPath(),
						structure.getDirectorycode(), structure.getDirectoryname());
			}
		});

		return lstdirectories;
	}

	public List<LSSheetOrderStructure> Deletemultidirectories(LSSheetOrderStructure[] directories) {
		List<LSSheetOrderStructure> lstdirectories = Arrays.asList(directories);

		lstdirectories.forEach(structure -> {
			if (structure.getParentdircode() == -2) {
				lsSheetOrderStructureRepository.delete(structure.getDirectorycode());
				lslogilablimsorderdetailRepository.updateparentdirectory(structure.getDircodetomove(),
						structure.getDirectorycode());
			} else {
				lsSheetOrderStructureRepository.updatedirectory(structure.getParentdircode(), structure.getPath(),
						structure.getDirectorycode(), structure.getDirectoryname());
			}
		});
		return lstdirectories;
	}

	public LSSheetOrderStructure getMoveDirectory(LSSheetOrderStructure objdir) {
		Response objResponse = new Response();
		LSSheetOrderStructure lstdir = null;
		String dir = objdir.getDirectoryname();
		if (objdir.getDirectorycode() != null) {
			lstdir = lsSheetOrderStructureRepository.findByDirectorycodeAndParentdircodeAndDirectorynameNot(
					objdir.getDirectorycode(), objdir.getParentdircode(), objdir.getDirectoryname());
		} else {
			lstdir = lsSheetOrderStructureRepository.findByParentdircodeAndDirectoryname(objdir.getParentdircode(),
					objdir.getDirectoryname());
		}
		while (lstdir != null) {
			if (dir.charAt(dir.length() - 1) == ')') {
				char temp = dir.charAt(dir.length() - 2);
				int n = Character.getNumericValue(temp);
				n = n + 1;
				dir = dir.substring(0, dir.length() - 2) + n + dir.substring(dir.length() - 1);
				objdir.setDirectoryname(dir);
			} else {
				dir = dir + " (2)";
				objdir.setDirectoryname(dir);
			}
			lstdir = lsSheetOrderStructureRepository.findByParentdircodeAndDirectoryname(objdir.getParentdircode(),
					objdir.getDirectoryname());

		}
		objdir.setResponse(objResponse);
		return objdir;
	}

	public LSSheetOrderStructure Movedirectory(LSSheetOrderStructure directory) {

		lsSheetOrderStructureRepository.updatedirectory(directory.getParentdircode(), directory.getPath(),
				directory.getDirectorycode(), directory.getDirectoryname());
		return directory;
	}

	public List<LSsamplefileversion> getlsorderfileversion(LSsamplefile objfile) {
		List<LSsamplefileversion> objList = new ArrayList<LSsamplefileversion>();
		if (objfile.getFilesamplecode() != null) {

			objList = lssamplefileversionRepository.findByFilesamplecodeOrderByVersionnoDesc(objfile);

		}
		return objList;
	}

	public List<LSlogilablimsorderdetail> GetAssignedtoUserorders(LSlogilablimsorderdetail order) {
		return lslogilablimsorderdetailRepository
				.findByOrderflagAndAssignedtoAndLockeduserIsNotNullOrderByBatchcodeDesc("N", order.getLsuserMaster());
	}

	public List<LSlogilablimsorderdetail> GetLockedOrders(LSlogilablimsorderdetail objorder) {

		if (objorder.getLsuserMaster().getUsername().equalsIgnoreCase("Administrator")) {
			List<LSMultisites> obj = LSMultisitesRepositery
					.findByLssiteMaster(objorder.getLsuserMaster().getLssitemaster());
			List<Integer> usercode = obj.parallelStream().map(LSMultisites::getUsercode).collect(Collectors.toList());
			return lslogilablimsorderdetailRepository
					.findByOrderflagAndLockeduserIsNotNullAndLockeduserInAndAssignedtoIsNullOrderByBatchcodeDesc("N",
							usercode);
		} else {
			List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
			List<Elnmaterial> nmaterialcode = elnmaterialRepository
					.findByNsitecode(objorder.getLsuserMaster().getLssitemaster().getSitecode());
			lstorder = lslogilablimsorderdetailRepository
					.findByOrderflagAndLsprojectmasterInAndAssignedtoIsNullAndLockeduserIsNotNull("N",
							objorder.getLstproject());

			int chunkSize = Integer.parseInt(env.getProperty("lssamplecount"));
			int totalSamples = nmaterialcode.size();
			List<LSlogilablimsorderdetail> lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize)
					.parallel().mapToObj(i -> {
						int startIndex = i * chunkSize;
						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
						List<Elnmaterial> currentChunk = nmaterialcode.subList(startIndex, endIndex);
						List<LSlogilablimsorderdetail> orderChunk = new ArrayList<>();
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByOrderflagAndLsprojectmasterIsNullAndElnmaterialInAndAssignedtoIsNullAndViewoptionAndLockeduserIsNotNull(
										"N", currentChunk, 1));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByOrderflagAndLsprojectmasterIsNullAndElnmaterialInAndAssignedtoIsNullAndViewoptionAndLsuserMasterAndLockeduserIsNotNullOrderByBatchcodeDesc(
										"N", currentChunk, 2, objorder.getLsuserMaster()));
						return orderChunk;
					}).flatMap(List::stream).collect(Collectors.toList());

			lstorderobj.addAll(lstorder);
			lstorderobj.forEach(objorderDetail -> objorderDetail.setLstworkflow(objorder.getLstworkflow()));
			return lstorderobj;
//			List<Integer> lstsampleint = lssamplemasterrepository.getDistinctByLssitemasterSitecodeAndStatus(
//					objorder.getLsuserMaster().getLssitemaster().getSitecode(), 1);
//			List<LSsamplemaster> lstsample = new ArrayList<>();
//			LSsamplemaster sample = null;
//			if (lstsampleint.size() > 0) {
//				for (Integer item : lstsampleint) {
//					sample = new LSsamplemaster();
//					sample.setSamplecode(item);
//					lstsample.add(sample);
//					sample = null; // Set sample to null after adding it to the list
//				}
//			}
//
//			lstorder = lslogilablimsorderdetailRepository
//					.findByOrderflagAndLsprojectmasterInAndAssignedtoIsNullAndLockeduserIsNotNull("N",
//							objorder.getLstproject());
//
//			int chunkSize = Integer.parseInt(env.getProperty("lssamplecount"));
//			int totalSamples = lstsample.size();
//			List<LSlogilablimsorderdetail> lstorderlimsobj = IntStream
//					.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel().mapToObj(i -> {
//						int startIndex = i * chunkSize;
//						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
//						List<LSsamplemaster> currentChunk = lstsample.subList(startIndex, endIndex);
//						List<LSlogilablimsorderdetail> orderChunk = new ArrayList<>();
//						orderChunk.addAll(lslogilablimsorderdetailRepository
//								.findByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndAssignedtoIsNullAndViewoptionAndLockeduserIsNotNull(
//										"N", currentChunk, 1));
//						orderChunk.addAll(lslogilablimsorderdetailRepository
//								.findByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndAssignedtoIsNullAndViewoptionAndLsuserMasterAndLockeduserIsNotNullOrderByBatchcodeDesc(
//										"N", currentChunk, 2, objorder.getLsuserMaster()));
//						return orderChunk;
//					}).flatMap(List::stream).collect(Collectors.toList());
//			System.out.print(lstorderlimsobj);

		}

	}

	public Response UnLockOrders(LSlogilablimsorderdetail[] lstOrder) {

		Response objResponse = new Response();

		try {

			List<LSlogilablimsorderdetail> lsOrder = Arrays.asList(lstOrder);

			if (!lsOrder.isEmpty()) {

				lsOrder = lsOrder.stream().peek(f -> {
					f.setLockeduser(null);
					f.setLockedusername(null);
					f.setActiveuser(null);
				}).collect(Collectors.toList());

				lslogilablimsorderdetailRepository.save(lsOrder);

			}

			objResponse.setStatus(true);
			objResponse.setInformation("Success");

		} catch (Exception e) {
			objResponse.setStatus(false);
			objResponse.setInformation("Failure");
		}

		return objResponse;
	}

	public Map<String, Object> GetSheetorderversions(Map<String, Object> objMap) {
		// TODO Auto-generated method stub
		int filecode = (int) objMap.get("filesamplecode");

		Map<String, Object> objmap = new HashMap<>();
		LSsamplefile objfile = new LSsamplefile();
		objfile.setFilesamplecode(filecode);
		List<LSsamplefileversion> lstfilesamle = lssamplefileversionRepository
				.findByFilesamplecodeOrderByVersionnoDesc(objfile);

		objmap.put("lsorderversion", lstfilesamle);
		return objmap;

	}

	public Map<String, Object> Getfoldersforprotocolorders(LSlogilabprotocoldetail objusermaster) {
		List<Long> immutableNegativeValues = Arrays.asList(-3L, -22L);
		Map<String, Object> mapfolders = new HashMap<String, Object>();
		List<Lsprotocolorderstructure> lstdir = new ArrayList<Lsprotocolorderstructure>();
		if (objusermaster.getLstuserMaster() == null) {
			lstdir = lsprotocolorderStructurerepository
					.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
							objusermaster.getLsuserMaster().getLssitemaster(), 1, immutableNegativeValues,
							objusermaster.getLsuserMaster(), 2, objusermaster.getLsuserMaster(), 3);
		} else {
			lstdir = lsprotocolorderStructurerepository
					.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
							objusermaster.getLsuserMaster().getLssitemaster(), 1, immutableNegativeValues,
							objusermaster.getLsuserMaster(), 2, objusermaster.getLsuserMaster().getLssitemaster(), 3,
							objusermaster.getLstuserMaster());
		}
		lstdir.addAll(lsprotocolorderStructurerepository.findByDirectorycodeIn(immutableNegativeValues));
		if (objusermaster.getLstproject() != null && objusermaster.getLstproject().size() > 0) {
			List<Integer> lsprojectcode = objusermaster.getLstproject().stream().map(LSprojectmaster::getProjectcode)
					.collect(Collectors.toList());
			List<LSprojectmaster> lstproject = lsprojectmasterRepository.findAll(lsprojectcode);
			ArrayList<List<Object>> lsttest = LSlogilabprotocoldetailRepository
					.getLstestmasterlocalByOrderdisplaytypeAndLsprojectmasterInAndTestcodeIsNotNull(lsprojectcode);
//			lstorders.forEach(objorderDetail -> objorderDetail.setLstworkflow(objorder.getLstworkflow()));
			mapfolders.put("tests", lsttest);
			mapfolders.put("projects", lstproject);

		} else {
			mapfolders.put("tests", new ArrayList<Logilaborders>());
			mapfolders.put("projects", new ArrayList<Projectmaster>());
		}

//		List<LSsamplemaster> lstsample = lssamplemasterrepository.findSamplecodeAndSamplenameBystatusAndLssitemaster(1,
//				objusermaster.getLsuserMaster().getLssitemaster());
//		if (lstsample != null && lstsample.size() > 0) {
//			List<Integer> lssamplecode = lstsample.stream().map(LSsamplemaster::getSamplecode)
//					.collect(Collectors.toList());
//
//			int totalSamples = lssamplecode.size();
//			int chunkSize = Integer.parseInt(env.getProperty("lssamplecount"));
//			List<Object> lsttest = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel()
//					.mapToObj(i -> {
//						int startIndex = i * chunkSize;
//						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
//						List<Integer> currentChunk = lssamplecode.subList(startIndex, endIndex);
//						List<Object> orderChunk = lslogilablimsorderdetailRepository
//								.getLstestmasterlocalByOrderdisplaytypeAndLSsamplemasterInAndTestcodeIsNotNull(
//										currentChunk);
//						return orderChunk;
//					}).flatMap(List::stream) // Flatten the list of arrays to a single list
//					.collect(Collectors.toList());
//
//			mapfolders.put("sampletests", lsttest);
//			mapfolders.put("samples", lstsample);
//
//		} else {
//			mapfolders.put("sampletests", new ArrayList<Logilaborders>());
//			mapfolders.put("samples", new ArrayList<Samplemaster>());
//		}
		List<Elnmaterial> Matireal_List = elnmaterialRepository.findByNsitecodeOrderByNmaterialcodeDesc(
				objusermaster.getLsuserMaster().getLssitemaster().getSitecode());
		List<Object> Material_List_Ordes = LSlogilabprotocoldetailRepository
				.getLstestmasterlocalAndmaterialByOrderdisplaytypeAndLSsamplemasterInAndTestcodeIsNotNull(
						objusermaster.getLsuserMaster().getLssitemaster().getSitecode());
		mapfolders.put("Materialtest", Material_List_Ordes);
		mapfolders.put("Material", Matireal_List);
//		List<LSusersteam> obj = lsusersteamRepository
//				.findBylssitemasterAndStatus(objusermaster.getLsuserMaster().getLssitemaster(), 1);
//
//		lstdir.forEach(e -> e.setTeamname(obj.stream().filter(a -> a.getTeamcode().equals(e.getTeamcode()))
//				.map(LSusersteam::getTeamname).findAny().orElse(null)));

		mapfolders.put("directory", lstdir);
//		
//		new Thread(() -> {
//			try {
//				
//				System.out.println("autoregisterorder");
//
//				List<LSlogilabprotocoldetail> orderobjdata = new ArrayList<LSlogilabprotocoldetail>();
//				orderobjdata=LSlogilabprotocoldetailRepository.findByLsuserMasterAndRepeat(objusermaster.getLsuserMaster(),true);
//				
//				protocolservice.addautoProtocolOrder(orderobjdata);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}).start();
		return mapfolders;
//	
	}

	public Map<String, Object> Getuserorders(Map<String, LSuserMaster> objusers) {
		Map<String, Object> mapuserorders = new HashMap<String, Object>();

		ObjectMapper mapper = new ObjectMapper();
		LSuserMaster lsloginuser = mapper.convertValue(objusers.get("loginuser"), LSuserMaster.class);
		LSuserMaster lsselecteduser = mapper.convertValue(objusers.get("selecteduser"), LSuserMaster.class);
		Date fromdate = lsselecteduser.getObjuser().getFromdate();
		Date todate = lsselecteduser.getObjuser().getTodate();
		Integer directory = mapper.convertValue(objusers.get("directorycode"), Integer.class);
		Integer filetype = mapper.convertValue(objusers.get("filetype"), Integer.class);
		if (lsloginuser.getUsercode() == lsselecteduser.getUsercode()) {
			if (filetype == -1) {
				mapuserorders.put("assigned", lslogilablimsorderdetailRepository
						.findByAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(lsloginuser, fromdate, todate));
				mapuserorders.put("sharebyme",
						lsordersharedbyRepository
								.findByUsersharedbyAndSharestatusAndSharedonBetweenOrderBySharedbycodeDesc(
										lsselecteduser, 1, fromdate, todate));
				mapuserorders.put("sharetome",
						lsordersharetoRepository
								.findByUsersharedonAndSharestatusAndSharedonBetweenOrderBySharetocodeDesc(
										lsselecteduser, 1, fromdate, todate));
			} else {
				mapuserorders.put("assigned",
						lslogilablimsorderdetailRepository
								.findByAssignedtoAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(lsloginuser,
										filetype, fromdate, todate));
				mapuserorders.put("sharebyme",
						lsordersharedbyRepository
								.findByUsersharedbyAndOrdertypeAndSharestatusAndSharedonBetweenOrderBySharedbycodeDesc(
										lsselecteduser, filetype, 1, fromdate, todate));
				mapuserorders.put("sharetome",
						lsordersharetoRepository
								.findByUsersharedonAndOrdertypeAndSharestatusAndSharedonBetweenOrderBySharetocodeDesc(
										lsselecteduser, filetype, 1, fromdate, todate));
			}
		} else {
			if (filetype == -1) {
				mapuserorders.put("assigned",
						lslogilablimsorderdetailRepository
								.findByAssignedtoAndLsuserMasterAndCreatedtimestampBetweenOrderByBatchcodeDesc(
										lsselecteduser, lsloginuser, fromdate, todate));
				mapuserorders.put("sharebyme", lsordersharedbyRepository
						.findByUsersharedbyAndUsersharedonAndSharestatusAndSharedonBetweenOrderBySharedbycodeDesc(
								lsloginuser, lsselecteduser, 1, fromdate, todate));
			} else {
				mapuserorders.put("assigned", lslogilablimsorderdetailRepository
						.findByAssignedtoAndLsuserMasterAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								lsselecteduser, lsloginuser, filetype, fromdate, todate));
				mapuserorders.put("sharebyme", lsordersharedbyRepository
						.findByUsersharedbyAndUsersharedonAndOrdertypeAndSharestatusAndSharedonBetweenOrderBySharedbycodeDesc(
								lsloginuser, lsselecteduser, filetype, 1, fromdate, todate));
			}
		}

		mapuserorders.put("directorycode", directory);

		return mapuserorders;
	}

	public Lsprotocolorderstructure Insertdirectoryonprotocol(Lsprotocolorderstructure objdir) {

		Response objResponse = new Response();
		Lsprotocolorderstructure lstdir = null;
		if (objdir.getDirectorycode() != null) {
			lstdir = lsprotocolorderStructurerepository.findByDirectorycodeAndParentdircodeAndDirectorynameNot(
					objdir.getDirectorycode(), objdir.getParentdircode(), objdir.getDirectoryname());
		} else {
			lstdir = lsprotocolorderStructurerepository.findByParentdircodeAndDirectorynameIgnoreCase(
					objdir.getParentdircode(), objdir.getDirectoryname());
		}
		if (lstdir != null) {
			objResponse.setStatus(false);
			objResponse.setInformation("IDS_FolderExist");
		} else {
			objResponse.setStatus(true);
			objResponse.setInformation("IDS_FolderAdded");
		}
		objdir.setResponse(objResponse);
		return objdir;

	}

	public Lsprotocolorderstructure Insertnewdirectoryonprotocol(Lsprotocolorderstructure objdir) {

		lsprotocolorderStructurerepository.save(objdir);
		return objdir;

	}

	public List<Lsprotocolorderstructure> Deletedirectoriesonprotocol(Lsprotocolorderstructure[] directories) {

		List<Lsprotocolorderstructure> lstdirectories = Arrays.asList(directories);

		lstdirectories.forEach(structure -> {
			if (structure.getParentdircode() == -2) {
				lsprotocolorderStructurerepository.delete(structure.getDirectorycode());
				LSlogilabprotocoldetailRepository.updateparentdirectory(structure.getDircodetomove(),
						structure.getDirectorycode());
			} else {
				lsprotocolorderStructurerepository.updatedirectory(structure.getParentdircode(), structure.getPath(),
						structure.getDirectorycode(), structure.getDirectoryname());
			}
		});

		return lstdirectories;

	}

	public Lsprotocolorderstructure Movedirectoryonprotocolorder(Lsprotocolorderstructure directory) {

		lsprotocolorderStructurerepository.updatedirectory(directory.getParentdircode(), directory.getPath(),
				directory.getDirectorycode(), directory.getDirectoryname());
		return directory;

	}

	public Lsprotocolorderstructure getMoveDirectoryonprotocolorder(Lsprotocolorderstructure objdir) {

		Response objResponse = new Response();
		Lsprotocolorderstructure lstdir = null;
		String dir = objdir.getDirectoryname();
		if (objdir.getDirectorycode() != null) {
			lstdir = lsprotocolorderStructurerepository.findByDirectorycodeAndParentdircodeAndDirectorynameNot(
					objdir.getDirectorycode(), objdir.getParentdircode(), objdir.getDirectoryname());
		} else {
			lstdir = lsprotocolorderStructurerepository.findByParentdircodeAndDirectoryname(objdir.getParentdircode(),
					objdir.getDirectoryname());
		}
		while (lstdir != null) {
			if (dir.charAt(dir.length() - 1) == ')') {
				char temp = dir.charAt(dir.length() - 2);
				int n = Character.getNumericValue(temp);
				n = n + 1;
				dir = dir.substring(0, dir.length() - 2) + n + dir.substring(dir.length() - 1);
				objdir.setDirectoryname(dir);
			} else {
				dir = dir + " (2)";
				objdir.setDirectoryname(dir);
			}
			lstdir = lsprotocolorderStructurerepository.findByParentdircodeAndDirectoryname(objdir.getParentdircode(),
					objdir.getDirectoryname());

		}
		objdir.setResponse(objResponse);
		return objdir;

	}

	public List<LSlogilabprotocoldetail> UpdateFolderforprotocolorders(LSlogilabprotocoldetail[] orderary) {

		List<LSlogilabprotocoldetail> order = Arrays.asList(orderary);
		if (order.size() > 0) {
			List<Long> lstorders = order.stream().map(LSlogilabprotocoldetail::getProtocolordercode)
					.collect(Collectors.toList());
			LSlogilabprotocoldetailRepository.updatedirectory(order.get(0).getDirectorycode(), lstorders);
		}
		return order;

	}

	public Map<String, Object> Getprotocolordersondirectory(Lsprotocolorderstructure objdir) {

		Map<String, Object> retuobjts = new HashMap<>();
		Date fromdate = objdir.getObjuser().getFromdate();
		Date todate = objdir.getObjuser().getTodate();
		Integer protocoltype = objdir.getProtocoltype();
		List<LSlogilabprotocoldetail> retuobj = new ArrayList<LSlogilabprotocoldetail>();

		if (protocoltype == -1 && objdir.getOrderflag() == null) {
			System.out.print(objdir);
			if ((objdir.getLstuserMaster() == null) || objdir.getLstuserMaster().length == 0) {
				retuobj = LSlogilabprotocoldetailRepository
						.findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrderByProtocolordercodeDesc(
								objdir.getDirectorycode(), 1, fromdate, todate, objdir.getSitemaster().getSitecode(),
								objdir.getDirectorycode(), 2, objdir.getCreatedby(), fromdate, todate,
								objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 3,
								objdir.getCreatedby(), fromdate, todate, objdir.getSitemaster().getSitecode());

			} else {
				retuobj = LSlogilabprotocoldetailRepository
						.findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndSitecodeOrderByProtocolordercodeDesc(
								objdir.getDirectorycode(), 1, fromdate, todate, objdir.getSitemaster().getSitecode(),
								objdir.getDirectorycode(), 2, objdir.getCreatedby(), fromdate, todate,
								objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 3, fromdate, todate,
								objdir.getLstuserMaster(), objdir.getSitemaster().getSitecode());

			}

		} else if (protocoltype != -1 && objdir.getOrderflag() != null) {
			if (objdir.getLstuserMaster().length == 0) {
				if (objdir.getRejected() != null) {

					retuobj = LSlogilabprotocoldetailRepository
							.findByDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrderByProtocolordercodeDesc(
									objdir.getDirectorycode(), 1, protocoltype, objdir.getOrderflag(), 1, fromdate,
									todate, objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 2,
									protocoltype, objdir.getOrderflag(), 1, objdir.getCreatedby(), fromdate, todate,
									objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 3, protocoltype,
									objdir.getOrderflag(), 1, objdir.getCreatedby(), fromdate, todate,
									objdir.getSitemaster().getSitecode());
				} else {
					retuobj = LSlogilabprotocoldetailRepository
							.findByDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrderByProtocolordercodeDesc(
									objdir.getDirectorycode(), 1, protocoltype, objdir.getOrderflag(), fromdate, todate,
									objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 2, protocoltype,
									objdir.getOrderflag(), objdir.getCreatedby(), fromdate, todate,
									objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 3, protocoltype,
									objdir.getOrderflag(), objdir.getCreatedby(), fromdate, todate,
									objdir.getSitemaster().getSitecode());
				}
			} else {
				if (objdir.getRejected() != null) {

					retuobj = LSlogilabprotocoldetailRepository
							.findByDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndRejectedAndCreatedtimestampBetweenAndCreatebyInAndSitecodeOrderByProtocolordercodeDesc(
									objdir.getDirectorycode(), 1, protocoltype, objdir.getOrderflag(), 1, fromdate,
									todate, objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 2,
									protocoltype, objdir.getOrderflag(), 1, objdir.getCreatedby(), fromdate, todate,
									objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 3, protocoltype,
									objdir.getOrderflag(), 1, fromdate, todate, objdir.getLstuserMaster(),
									objdir.getSitemaster().getSitecode());
				} else {
					retuobj = LSlogilabprotocoldetailRepository
							.findByDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndOrderflagAndCreatedtimestampBetweenAndCreatebyInAndSitecodeOrderByProtocolordercodeDesc(
									objdir.getDirectorycode(), 1, protocoltype, objdir.getOrderflag(), fromdate, todate,
									objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 2, protocoltype,
									objdir.getOrderflag(), objdir.getCreatedby(), fromdate, todate,
									objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 3, protocoltype,
									objdir.getOrderflag(), fromdate, todate, objdir.getLstuserMaster(),
									objdir.getSitemaster().getSitecode());
				}
			}
		} else if (protocoltype == -1 && objdir.getOrderflag() != null) {
			if (objdir.getLstuserMaster().length == 0) {
				if (objdir.getRejected() != null) {
					retuobj = LSlogilabprotocoldetailRepository
							.findByDirectorycodeAndViewoptionAndOrderflagAndRejectedAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndOrderflagAndRejectedAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndOrderflagAndRejectedAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrderByProtocolordercodeDesc(
									objdir.getDirectorycode(), 1, objdir.getOrderflag(), 1, fromdate, todate,
									objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 2,
									objdir.getOrderflag(), 1, objdir.getCreatedby(), fromdate, todate,
									objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 3,
									objdir.getOrderflag(), 1, objdir.getCreatedby(), fromdate, todate,
									objdir.getSitemaster().getSitecode());
				} else {
					retuobj = LSlogilabprotocoldetailRepository
							.findByDirectorycodeAndViewoptionAndOrderflagAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrderByProtocolordercodeDesc(
									objdir.getDirectorycode(), 1, objdir.getOrderflag(), fromdate, todate,
									objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 2,
									objdir.getOrderflag(), objdir.getCreatedby(), fromdate, todate,
									objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 3,
									objdir.getOrderflag(), objdir.getCreatedby(), fromdate, todate,
									objdir.getSitemaster().getSitecode());
				}
			} else {
				if (objdir.getRejected() != null) {
					retuobj = LSlogilabprotocoldetailRepository
							.findByDirectorycodeAndViewoptionAndOrderflagAndRejectedAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndOrderflagAndRejectedAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndOrderflagAndRejectedAndCreatedtimestampBetweenAndCreatebyInAndSitecodeOrderByProtocolordercodeDesc(
									objdir.getDirectorycode(), 1, objdir.getOrderflag(), 1, fromdate, todate,
									objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 2,
									objdir.getOrderflag(), 1, objdir.getCreatedby(), fromdate, todate,
									objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 3,
									objdir.getOrderflag(), 1, fromdate, todate, objdir.getLstuserMaster(),
									objdir.getSitemaster().getSitecode());
				} else {
					retuobj = LSlogilabprotocoldetailRepository
							.findByDirectorycodeAndViewoptionAndOrderflagAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndOrderflagAndCreatedtimestampBetweenAndCreatebyInAndSitecodeOrderByProtocolordercodeDesc(
									objdir.getDirectorycode(), 1, objdir.getOrderflag(), fromdate, todate,
									objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 2,
									objdir.getOrderflag(), objdir.getCreatedby(), fromdate, todate,
									objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 3,
									objdir.getOrderflag(), fromdate, todate, objdir.getLstuserMaster(),
									objdir.getSitemaster().getSitecode());
				}

			}
		} else if (protocoltype != -1 && objdir.getOrderflag() == null) {
			if (objdir.getLstuserMaster().length == 0) {
				retuobj = LSlogilabprotocoldetailRepository
						.findByDirectorycodeAndViewoptionAndProtocoltypeAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrderByProtocolordercodeDesc(
								objdir.getDirectorycode(), 1, protocoltype, fromdate, todate,
								objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 2, protocoltype,
								objdir.getCreatedby(), fromdate, todate, objdir.getSitemaster().getSitecode(),
								objdir.getDirectorycode(), 3, protocoltype, objdir.getCreatedby(), fromdate, todate,
								objdir.getSitemaster().getSitecode());
			} else {
				retuobj = LSlogilabprotocoldetailRepository
						.findByDirectorycodeAndViewoptionAndProtocoltypeAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndLsuserMasterAndCreatedtimestampBetweenAndSitecodeOrDirectorycodeAndViewoptionAndProtocoltypeAndCreatedtimestampBetweenAndCreatebyInAndSitecodeOrderByProtocolordercodeDesc(
								objdir.getDirectorycode(), 1, protocoltype, fromdate, todate,
								objdir.getSitemaster().getSitecode(), objdir.getDirectorycode(), 2, protocoltype,
								objdir.getCreatedby(), fromdate, todate, objdir.getSitemaster().getSitecode(),
								objdir.getDirectorycode(), 3, protocoltype, fromdate, todate, objdir.getLstuserMaster(),
								objdir.getSitemaster().getSitecode());
			}

		}
//		retuobj.forEach(objorderDetail -> objorderDetail.setLstworkflow(objdir.getLstworkflow()));
		retuobj.forEach(objorderDetail -> objorderDetail.setLstelnprotocolworkflow(objdir.getLstelnprotocolworkflow()));
		List<Long> protocolordercode = new ArrayList<>();
		if (retuobj.size() > 0 && objdir.getSearchCriteriaType() != null) {
			protocolordercode = retuobj.stream().map(LSlogilabprotocoldetail::getProtocolordercode)
					.collect(Collectors.toList());
			retuobjts.put("protocolordercodeslist", protocolordercode);
		}
		retuobjts.put("protocolorders", retuobj);

		return retuobjts;
//				LSlogilabprotocoldetailRepository.findByDirectorycodeAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(objdir.getDirectorycode(),fromdate,todate);
	}

	public LSlogilabprotocoldetail UpdatesingleFolderforprotocolorder(LSlogilabprotocoldetail order) {
		LSlogilabprotocoldetailRepository.updatesingledirectory(order.getDirectorycode(), order.getProtocolordercode());
		return order;
	}

	public List<Logilaborders> Getordersonproject(LSlogilablimsorderdetail objorder) {

		List<Logilaborders> lstorder = new ArrayList<Logilaborders>();

		Date fromdate = objorder.getFromdate();
		Date todate = objorder.getTodate();
		Integer filetype = objorder.getFiletype();
//		if (objorder.getSearchCriteria().getContentsearchtype() != null
//				&& objorder.getSearchCriteria().getContentsearch() != null) {
//			lstorder = GetordersonFilter(objorder);
//
//		} 
		// else if (objorder.getSearchCriteria().getContentsearch() == null) {

		if (filetype == null) {
			lstorder = lslogilablimsorderdetailRepository
					.findByLsprojectmasterAndLstestmasterlocalAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							objorder.getOrderflag(), objorder.getLsprojectmaster(), objorder.getLstestmasterlocal(),
							fromdate, todate);
		} else if (filetype == -1 && objorder.getOrderflag() == null) {
			lstorder = lslogilablimsorderdetailRepository
					.findByLsprojectmasterAndLstestmasterlocalAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							objorder.getLsprojectmaster(), objorder.getLstestmasterlocal(), fromdate, todate);
		} else if (filetype == -1 && objorder.getOrderflag() != null) {
			lstorder = lslogilablimsorderdetailRepository
					.findByOrderflagAndLsprojectmasterAndLstestmasterlocalAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							objorder.getOrderflag(), objorder.getLsprojectmaster(), objorder.getLstestmasterlocal(),
							fromdate, todate);
		}
		// get sheet orders
		else if (filetype == 4) {
			lstorder = lslogilablimsorderdetailRepository
					.findByLsprojectmasterAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							objorder.getLsprojectmaster(), filetype, fromdate, todate);
		} else if (objorder.getOrderflag() == null) {
			lstorder = lslogilablimsorderdetailRepository
					.findByFiletypeAndLsprojectmasterAndLstestmasterlocalAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							filetype, objorder.getLsprojectmaster(), objorder.getLstestmasterlocal(), fromdate, todate);
		} else if (objorder.getOrderflag() != null && objorder.getApprovelstatus() != null
				&& objorder.getApprovelstatus() == 3) {
			lstorder = lslogilablimsorderdetailRepository
					.findByOrderflagAndApprovelstatusAndFiletypeAndLsprojectmasterAndLstestmasterlocalAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							objorder.getOrderflag(), objorder.getApprovelstatus(), filetype,
							objorder.getLsprojectmaster(), objorder.getLstestmasterlocal(), fromdate, todate);
		} else if (objorder.getOrderflag() != null && objorder.getApprovelstatus() != null
				&& objorder.getApprovelstatus() == 1) {
			lstorder = lslogilablimsorderdetailRepository
					.findByOrderflagAndApprovelstatusAndFiletypeAndLsprojectmasterAndLstestmasterlocalAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							objorder.getOrderflag(), objorder.getApprovelstatus(), filetype,
							objorder.getLsprojectmaster(), objorder.getLstestmasterlocal(), fromdate, todate);
		} else {
			lstorder = lslogilablimsorderdetailRepository
					.findByOrderflagAndFiletypeAndLsprojectmasterAndLstestmasterlocalAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							objorder.getOrderflag(), filetype, objorder.getLsprojectmaster(),
							objorder.getLstestmasterlocal(), fromdate, todate);
		}

		lstorder.addAll(Getordersondirectory(objorder.getLssheetOrderStructure()));
		// }
		if (objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearchtype() != null
				&& objorder.getSearchCriteria().getContentsearch() != null) {

			lstorder = GetsampleordersonFilter(objorder, lstorder);

			lstorder.forEach(objorderDetail -> objorderDetail.setLstworkflow(objorder.getLstworkflow()));
			return lstorder;
		} else {
			lstorder.forEach(objorderDetail -> objorderDetail.setLstworkflow(objorder.getLstworkflow()));
			return lstorder;
		}
	}

	public List<Logilaborders> Getordersonsample(LSlogilablimsorderdetail objorder) {

		List<Logilaborders> lstorder = new ArrayList<Logilaborders>();

		Date fromdate = objorder.getFromdate();
		Date todate = objorder.getTodate();
		Integer filetype = objorder.getFiletype();
//		if (objorder.getSearchCriteria().getContentsearchtype() != null
//				&& objorder.getSearchCriteria().getContentsearch() != null) {
//			lstorder = GetordersonFilter(objorder);
//
//		}
		if (objorder.getSearchCriteria().getContentsearch() == null) {
			if (filetype == null) {

				lstorder = lslogilablimsorderdetailRepository
						.findByLssamplemasterAndViewoptionAndLstestmasterlocalAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndLstestmasterlocalAndFiletypeAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								objorder.getLssamplemaster(), 1, objorder.getLstestmasterlocal(), 2, fromdate, todate,
								objorder.getLssamplemaster(), 2, objorder.getLsuserMaster(),
								objorder.getLstestmasterlocal(), filetype, 2, fromdate, todate);

			} else if (filetype == -1 && objorder.getOrderflag() == null) {
//				lstorder = lslogilablimsorderdetailRepository
//						.findByLssamplemasterAndViewoptionAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//								objorder.getLssamplemaster(), 1, 2, fromdate, todate, objorder.getLssamplemaster(), 2,
//								objorder.getLsuserMaster(), 2, fromdate, todate);
				lstorder = lslogilablimsorderdetailRepository
						.findByLssamplemasterAndViewoptionAndLstestmasterlocalAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								objorder.getLssamplemaster(), 1, objorder.getLstestmasterlocal(), 2, fromdate, todate,
								objorder.getLssamplemaster(), 2, objorder.getLsuserMaster(), 2, fromdate, todate);

			} else if (filetype == 0) {

				lstorder = lslogilablimsorderdetailRepository
						.findByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
								objorder.getOrderflag(), filetype, fromdate, todate);
			}

		} else if (filetype == -1 && objorder.getOrderflag() != null) {
			lstorder = lslogilablimsorderdetailRepository
					.findByOrderflagAndLssamplemasterAndViewoptionAndLstestmasterlocalAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndLstestmasterlocalAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							objorder.getOrderflag(), objorder.getLssamplemaster(), 1, objorder.getLstestmasterlocal(),
							2, fromdate, todate, objorder.getLssamplemaster(), 2, objorder.getLsuserMaster(),
							objorder.getLstestmasterlocal(), 2, fromdate, todate);

		} else if (filetype == 4) {

			lstorder = lslogilablimsorderdetailRepository
					.findByLssamplemasterAndViewoptionAndFiletypeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndFiletypeAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							objorder.getLssamplemaster(), 1, filetype, 2, fromdate, todate,
							objorder.getLssamplemaster(), 2, objorder.getLsuserMaster(), filetype, 2, fromdate, todate);

		} else if (objorder.getOrderflag() == null && filetype == -1) {
//			lstorder = lslogilablimsorderdetailRepository
//					.findByLssamplemasterAndViewoptionAndLstestmasterlocalAndFiletypeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndLstestmasterlocalAndFiletypeAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//							objorder.getLssamplemaster(), 1, objorder.getLstestmasterlocal(), filetype, 2, fromdate,
//							todate, objorder.getLssamplemaster(), 2, objorder.getLsuserMaster(),
//							objorder.getLstestmasterlocal(), filetype, 2, fromdate, todate);

			lstorder = lslogilablimsorderdetailRepository
					.findByLssamplemasterAndViewoptionAndLstestmasterlocalAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndLstestmasterlocalAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							objorder.getLssamplemaster(), 1, objorder.getLstestmasterlocal(), 2, fromdate, todate,
							objorder.getLssamplemaster(), 2, objorder.getLsuserMaster(),
							objorder.getLstestmasterlocal(), 2, fromdate, todate);

		} else if (objorder.getOrderflag() != null && objorder.getApprovelstatus() != null
				&& objorder.getApprovelstatus() == 3) {
			lstorder = lslogilablimsorderdetailRepository
					.findByOrderflagAndApprovelstatusAndLssamplemasterAndViewoptionAndLstestmasterlocalAndFiletypeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndLstestmasterlocalAndFiletypeAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							objorder.getOrderflag(), objorder.getApprovelstatus(), objorder.getLssamplemaster(), 1,
							objorder.getLstestmasterlocal(), filetype, 2, fromdate, todate,
							objorder.getLssamplemaster(), 2, objorder.getLsuserMaster(),
							objorder.getLstestmasterlocal(), filetype, 2, fromdate, todate);

		} else if (objorder.getOrderflag() != null && objorder.getApprovelstatus() != null
				&& objorder.getApprovelstatus() == 1) {
			lstorder = lslogilablimsorderdetailRepository
					.findByOrderflagAndApprovelstatusAndLssamplemasterAndViewoptionAndLstestmasterlocalAndFiletypeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndLstestmasterlocalAndFiletypeAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							objorder.getOrderflag(), objorder.getApprovelstatus(), objorder.getLssamplemaster(), 1,
							objorder.getLstestmasterlocal(), filetype, 2, fromdate, todate,
							objorder.getLssamplemaster(), 2, objorder.getLsuserMaster(),
							objorder.getLstestmasterlocal(), filetype, 2, fromdate, todate);

		}

		else {
			lstorder = lslogilablimsorderdetailRepository
					.findByLssamplemasterAndViewoptionAndLstestmasterlocalAndFiletypeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndLstestmasterlocalAndFiletypeAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							objorder.getLssamplemaster(), 1, objorder.getLstestmasterlocal(), filetype, 2, fromdate,
							todate, objorder.getLssamplemaster(), 2, objorder.getLsuserMaster(),
							objorder.getLstestmasterlocal(), filetype, 2, fromdate, todate);

		}

		if (objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearchtype() != null
				&& objorder.getSearchCriteria().getContentsearch() != null) {

			lstorder = GetsampleordersonFilter(objorder, lstorder);

			lstorder.forEach(objorderDetail -> objorderDetail.setLstworkflow(objorder.getLstworkflow()));
			return lstorder;
		} else {

			lstorder.forEach(objorderDetail -> objorderDetail.setCanuserprocess(true));
			return lstorder;
		}
	}

	public Map<String, Object> Getorderbyflaganduser(LSlogilablimsorderdetail objorder) {
		Map<String, Object> rtn_object=new HashMap<>();
		List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objorder.getLsuserMaster());
		List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
				objorder.getLsuserMaster().getLssitemaster());
		List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
		List<Elnmaterial> nmaterialcode = elnmaterialRepository
				.findByNsitecode(objorder.getLsuserMaster().getLssitemaster().getSitecode());
		List<Logilaborders> lstorder = new ArrayList<Logilaborders>();
		Date fromdate = objorder.getFromdate();
		Date todate = objorder.getTodate();
		Integer testcode = objorder.getTestcode();
		Integer filetype = objorder.getFiletype();
//		Notification notobj = new Notification();
		List<LSSheetOrderStructure> lstdir;
		long Directorycode_Not = -3L;
		if (objorder.getLstuserMaster() == null) {
			lstdir = lsSheetOrderStructureRepository
					.findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrCreatedbyAndViewoptionAndDirectorycodeNotOrderByDirectorycode(
							objorder.getLsuserMaster().getLssitemaster(), 1, objorder.getLsuserMaster(), 2,
							objorder.getLsuserMaster(), 3, Directorycode_Not);
		} else {
			lstdir = lsSheetOrderStructureRepository
					.findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInAndDirectorycodeNotOrderByDirectorycode(
							objorder.getLsuserMaster().getLssitemaster(), 1, objorder.getLsuserMaster(), 2,
							objorder.getLsuserMaster().getLssitemaster(), 3, objorder.getLstuserMaster(),
							Directorycode_Not);
		}

		List<Long> Directory_Code = lstdir.stream().map(LSSheetOrderStructure::getDirectorycode)
				.filter(code -> code > 0).collect(Collectors.toList());
		if (filetype == -1 && objorder.getOrderflag() == null) {
			lstorder = lslogilablimsorderdetailRepository
					.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndOrdercancellIsNull(
							objorder.getOrderflag(), lstproject, fromdate, todate);
			int chunkSize = Integer.parseInt(env.getProperty("lssamplecount"));
			int totalSamples = nmaterialcode.size();
			List<Logilaborders> lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel()
					.mapToObj(i -> {
						int startIndex = i * chunkSize;
						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
						List<Elnmaterial> currentChunk = nmaterialcode.subList(startIndex, endIndex);
						List<Logilaborders> orderChunk = new ArrayList<>();
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByOrderflagAndLsprojectmasterIsNullAndDirectorycodeIsNullAndElnmaterialInAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndOrdercancellIsNull(
										objorder.getOrderflag(), currentChunk, fromdate, todate, 1));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByOrderflagAndLsprojectmasterIsNullAndDirectorycodeIsNullAndElnmaterialInAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullOrderByBatchcodeDesc(
										objorder.getOrderflag(), currentChunk, fromdate, todate, 2,
										objorder.getLsuserMaster()));
						return orderChunk;
					}).flatMap(List::stream).collect(Collectors.toList());

			lstorder.addAll(lstorderobj);

		} else if (filetype == -1 && objorder.getOrderflag() != null) {

			lstorder = lslogilablimsorderdetailRepository
					.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndOrdercancellIsNull(
							objorder.getOrderflag(), lstproject, fromdate, todate);
			int chunkSize = Integer.parseInt(env.getProperty("lssamplecount"));
			int totalSamples = nmaterialcode.size();
			List<Logilaborders> lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel()
					.mapToObj(i -> {
						int startIndex = i * chunkSize;
						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
						List<Elnmaterial> currentChunk = nmaterialcode.subList(startIndex, endIndex);
						List<Logilaborders> orderChunk = new ArrayList<>();
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByOrderflagAndLsprojectmasterIsNullAndDirectorycodeIsNullAndElnmaterialInAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndOrdercancellIsNull(
										objorder.getOrderflag(), currentChunk, fromdate, todate, 1));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByOrderflagAndLsprojectmasterIsNullAndDirectorycodeIsNullAndElnmaterialInAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullOrderByBatchcodeDesc(
										objorder.getOrderflag(), currentChunk, fromdate, todate, 2,
										objorder.getLsuserMaster()));
						return orderChunk;
					}).flatMap(List::stream).collect(Collectors.toList());
			lstorder.addAll(lstorderobj);

			if (objorder.getLstuserMaster() != null) {
				lstorder.addAll(lslogilablimsorderdetailRepository
						.findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
								Directory_Code, 1, fromdate, todate, objorder.getOrderflag(), Directory_Code, 2,
								objorder.getLsuserMaster(), fromdate, todate, objorder.getOrderflag(), Directory_Code,
								3, fromdate, todate, objorder.getLstuserMaster(), objorder.getOrderflag()));

			} else {

				lstorder.addAll(lslogilablimsorderdetailRepository
						.findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
								Directory_Code, 1, fromdate, todate, objorder.getOrderflag(), Directory_Code, 2,
								objorder.getLsuserMaster(), fromdate, todate, objorder.getOrderflag(), Directory_Code,
								3, objorder.getLsuserMaster(), fromdate, todate, objorder.getOrderflag()));

			}
		} else if (objorder.getOrderflag() != null && objorder.getApprovelstatus() != null
				&& objorder.getApprovelstatus() == 3) {

			lstorder = lslogilablimsorderdetailRepository
					.findByOrderflagAndApprovelstatusAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndOrdercancellIsNull(
							objorder.getOrderflag(), objorder.getApprovelstatus(), lstproject, filetype, fromdate,
							todate);

			int chunkSize = Integer.parseInt(env.getProperty("lssamplecount"));
			int totalSamples = nmaterialcode.size();
			List<Logilaborders> lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel()
					.mapToObj(i -> {
						int startIndex = i * chunkSize;
						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
						List<Elnmaterial> currentChunk = nmaterialcode.subList(startIndex, endIndex);
						List<Logilaborders> orderChunk = new ArrayList<>();
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByOrderflagAndApprovelstatusAndLsprojectmasterIsNullAndDirectorycodeIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndOrdercancellIsNull(
										objorder.getOrderflag(), objorder.getApprovelstatus(), currentChunk, filetype,
										fromdate, todate, 1));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByOrderflagAndApprovelstatusAndLsprojectmasterIsNullAndDirectorycodeIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullOrderByBatchcodeDesc(
										objorder.getOrderflag(), objorder.getApprovelstatus(), currentChunk, filetype,
										fromdate, todate, 2, objorder.getLsuserMaster()));
						return orderChunk;
					}).flatMap(List::stream).collect(Collectors.toList());  //kumu
			lstorder.addAll(lstorderobj);
			if (objorder.getLstuserMaster() != null) {
				lstorder.addAll(lslogilablimsorderdetailRepository
						.findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrderByBatchcodeDesc(
								Directory_Code, 1, fromdate, todate, objorder.getOrderflag(), filetype,
								objorder.getApprovelstatus(), Directory_Code, 2, objorder.getLsuserMaster(), fromdate,
								todate, objorder.getOrderflag(), filetype, objorder.getApprovelstatus(), Directory_Code,
								3, fromdate, todate, objorder.getLstuserMaster(), objorder.getOrderflag(), filetype,
								objorder.getApprovelstatus()));

			} else {

				lstorder.addAll(lslogilablimsorderdetailRepository
						.findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrderByBatchcodeDesc(
								Directory_Code, 1, fromdate, todate, objorder.getOrderflag(), filetype,
								objorder.getApprovelstatus(), Directory_Code, 2, objorder.getLsuserMaster(), fromdate,
								todate, objorder.getOrderflag(), filetype, objorder.getApprovelstatus(), Directory_Code,
								3, objorder.getLsuserMaster(), fromdate, todate, objorder.getOrderflag(), filetype,
								objorder.getApprovelstatus()));

			}

		} else if (objorder.getOrderflag() != null && objorder.getApprovelstatus() != null
				&& objorder.getApprovelstatus() == 1) {

			lstorder = lslogilablimsorderdetailRepository
					.findByOrderflagAndApprovelstatusAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndOrdercancellIsNull(
							objorder.getOrderflag(), objorder.getApprovelstatus(), lstproject, filetype, fromdate,
							todate);

			int chunkSize = Integer.parseInt(env.getProperty("lssamplecount"));
			int totalSamples = nmaterialcode.size();
			List<Logilaborders> lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel()
					.mapToObj(i -> {
						int startIndex = i * chunkSize;
						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
						List<Elnmaterial> currentChunk = nmaterialcode.subList(startIndex, endIndex);
						List<Logilaborders> orderChunk = new ArrayList<>();
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByOrderflagAndApprovelstatusAndLsprojectmasterIsNullAndDirectorycodeIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndOrdercancellIsNull(
										objorder.getOrderflag(), objorder.getApprovelstatus(), currentChunk, filetype,
										fromdate, todate, 1));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByOrderflagAndApprovelstatusAndLsprojectmasterIsNullAndDirectorycodeIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullOrderByBatchcodeDesc(
										objorder.getOrderflag(), objorder.getApprovelstatus(), currentChunk, filetype,
										fromdate, todate, 2, objorder.getLsuserMaster()));
						return orderChunk;
					}).flatMap(List::stream).collect(Collectors.toList());
			lstorder.addAll(lstorderobj);

			if (objorder.getLstuserMaster() != null) {
				lstorder.addAll(lslogilablimsorderdetailRepository
						.findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrderByBatchcodeDesc(
								Directory_Code, 1, fromdate, todate, objorder.getOrderflag(), filetype,
								objorder.getApprovelstatus(), Directory_Code, 2, objorder.getLsuserMaster(), fromdate,
								todate, objorder.getOrderflag(), filetype, objorder.getApprovelstatus(), Directory_Code,
								3, fromdate, todate, objorder.getLstuserMaster(), objorder.getOrderflag(), filetype,
								objorder.getApprovelstatus()));

			} else {

				lstorder.addAll(lslogilablimsorderdetailRepository
						.findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndApprovelstatusAndOrdercancellIsNullOrderByBatchcodeDesc(
								Directory_Code, 1, fromdate, todate, objorder.getOrderflag(), filetype,
								objorder.getApprovelstatus(), Directory_Code, 2, objorder.getLsuserMaster(), fromdate,
								todate, objorder.getOrderflag(), filetype, objorder.getApprovelstatus(), Directory_Code,
								3, objorder.getLsuserMaster(), fromdate, todate, objorder.getOrderflag(), filetype,
								objorder.getApprovelstatus()));

			}

		} else if (testcode != null && testcode != -1 && objorder.getLsprojectmaster() == null) {
			lstorder = lslogilablimsorderdetailRepository
					.findByOrderflagAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeAndOrdercancellIsNullOrderByBatchcodeDesc(
							objorder.getOrderflag(), lstproject, filetype, fromdate, todate, testcode);
			int chunkSize = Integer.parseInt(env.getProperty("lssamplecount"));
			int totalSamples = nmaterialcode.size();
			List<Logilaborders> lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel()
					.mapToObj(i -> {
						int startIndex = i * chunkSize;
						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
						List<Elnmaterial> currentChunk = nmaterialcode.subList(startIndex, endIndex);
						List<Logilaborders> orderChunk = new ArrayList<>();
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByOrderflagAndTestcodeAndLsprojectmasterIsNullAndDirectorycodeIsNullAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInOrOrderflagAndTestcodeAndLsprojectmasterIsNullAndDirectorycodeIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoption(
										objorder.getOrderflag(), testcode, filetype, fromdate, todate, currentChunk,
										objorder.getOrderflag(), testcode, currentChunk, filetype, fromdate, todate,
										1));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByOrderflagAndTestcodeAndLsprojectmasterIsNullAndDirectorycodeIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrderByBatchcodeDesc(
										objorder.getOrderflag(), testcode, currentChunk, filetype, fromdate, todate, 2,
										objorder.getLsuserMaster()));
						return orderChunk;
					}).flatMap(List::stream).collect(Collectors.toList());
			lstorder.addAll(lstorderobj);

			if (objorder.getLstuserMaster() != null) {
				lstorder.addAll(lslogilablimsorderdetailRepository
						.findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeOrderByBatchcodeDesc(
								Directory_Code, 1, fromdate, todate, objorder.getOrderflag(), filetype, testcode,
								Directory_Code, 2, objorder.getLsuserMaster(), fromdate, todate,
								objorder.getOrderflag(), filetype, testcode, Directory_Code, 3, fromdate, todate,
								objorder.getLstuserMaster(), objorder.getOrderflag(), filetype, testcode));

			} else {

				lstorder.addAll(lslogilablimsorderdetailRepository
						.findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndTestcodeOrderByBatchcodeDesc(
								Directory_Code, 1, fromdate, todate, objorder.getOrderflag(), filetype, testcode,
								Directory_Code, 2, objorder.getLsuserMaster(), fromdate, todate,
								objorder.getOrderflag(), filetype, testcode, Directory_Code, 3,
								objorder.getLsuserMaster(), fromdate, todate, objorder.getOrderflag(), filetype,
								testcode));

			}

		} else if (objorder.getLsprojectmaster() != null && objorder.getLsprojectmaster().getProjectcode() != -1
				&& testcode != null && testcode != -1) {

			lstorder = lslogilablimsorderdetailRepository
					.findByOrderflagAndLsprojectmasterInAndTestcodeAndFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNull(
							objorder.getOrderflag(), lstproject, testcode, filetype, objorder.getLsprojectmaster(),
							fromdate, todate);

			int chunkSize = Integer.parseInt(env.getProperty("lssamplecount"));
			int totalSamples = nmaterialcode.size();
			List<Logilaborders> lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel()
					.mapToObj(i -> {
						int startIndex = i * chunkSize;
						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
						List<Elnmaterial> currentChunk = nmaterialcode.subList(startIndex, endIndex);
						List<Logilaborders> orderChunk = new ArrayList<>();
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByOrderflagAndLsprojectmasterIsNullAndTestcodeAndElnmaterialInAndFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoption(
										objorder.getOrderflag(), testcode, currentChunk, filetype,
										objorder.getLsprojectmaster(), fromdate, todate, 1));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByOrderflagAndLsprojectmasterIsNullAndTestcodeAndElnmaterialInAndFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterOrderByBatchcodeDesc(
										objorder.getOrderflag(), testcode, currentChunk, filetype,
										objorder.getLsprojectmaster(), fromdate, todate, 2,
										objorder.getLsuserMaster()));
						return orderChunk;
					}).flatMap(List::stream).collect(Collectors.toList());
			lstorder.addAll(lstorderobj);

		} else if (objorder.getLsprojectmaster() != null && objorder.getLsprojectmaster().getProjectcode() != -1) {
			lstorder = lslogilablimsorderdetailRepository
					.findByOrderflagAndFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNull(
							objorder.getOrderflag(), filetype, objorder.getLsprojectmaster(), fromdate, todate);
		} else if (filetype == 0 && objorder.getOrderflag() != null) {
			lstorder = lslogilablimsorderdetailRepository
					.findByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
							objorder.getOrderflag(), filetype, fromdate, todate);
		}

		else {
			if (objorder.getLstuserMaster() != null) {
				lstorder = lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
								objorder.getOrderflag(), lstproject, filetype, fromdate, todate);

//				lstorder = lslogilablimsorderdetailRepository
//						.findByOrderflagAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//								objorder.getOrderflag(), lstproject, filetype, fromdate, todate);

				int chunkSize = Integer.parseInt(env.getProperty("lssamplecount"));
				int totalSamples = nmaterialcode.size();

				List<Logilaborders> lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize)
						.parallel().mapToObj(i -> {
							int startIndex = i * chunkSize;
							int endIndex = Math.min(startIndex + chunkSize, totalSamples);
							List<Elnmaterial> currentChunk = nmaterialcode.subList(startIndex, endIndex);

							List<Logilaborders> orderChunk = new ArrayList<>();

							orderChunk.addAll(lslogilablimsorderdetailRepository
									.findByOrderflagAndLsprojectmasterIsNullAndDirectorycodeIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNull(
											objorder.getOrderflag(), currentChunk, filetype, fromdate, todate, 2,
											objorder.getLsuserMaster()));

							orderChunk.addAll(lslogilablimsorderdetailRepository
									.findByOrderflagAndLsprojectmasterIsNullAndDirectorycodeIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndOrdercancellIsNull(
											objorder.getOrderflag(), currentChunk, filetype, fromdate, todate, 1));

							orderChunk.addAll(lslogilablimsorderdetailRepository
									.findByOrderflagAndLsprojectmasterIsNullAndDirectorycodeIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullOrderByBatchcodeDesc(
											objorder.getOrderflag(), currentChunk, filetype, fromdate, todate, 3,
											objorder.getLsuserMaster()));

							return orderChunk;
						}).flatMap(List::stream).collect(Collectors.toList());

				lstorder.addAll(lstorderobj);

				lstorder.addAll(lslogilablimsorderdetailRepository
						.findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterInAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
								Directory_Code, 1, fromdate, todate, objorder.getOrderflag(), filetype, Directory_Code,
								2, objorder.getLsuserMaster(), fromdate, todate, objorder.getOrderflag(), filetype,
								Directory_Code, 3, fromdate, todate, objorder.getLstuserMaster(),
								objorder.getOrderflag(), filetype));

				lstorder.forEach(objorderDetail -> objorderDetail.setLstworkflow(objorder.getLstworkflow()));

			} else {
				lstorder = lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNull(
								objorder.getOrderflag(), lstproject, filetype, fromdate, todate);

				lstorder.addAll(lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterIsNullAndDirectorycodeIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNull(
								objorder.getOrderflag(), nmaterialcode, filetype, fromdate, todate, 2,
								objorder.getLsuserMaster()));

				lstorder.addAll(lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterIsNullAndDirectorycodeIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndOrdercancellIsNull(
								objorder.getOrderflag(), nmaterialcode, filetype, fromdate, todate, 1));

				lstorder.addAll(lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterIsNullAndDirectorycodeIsNullAndElnmaterialInAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullOrderByBatchcodeDesc(
								objorder.getOrderflag(), nmaterialcode, filetype, fromdate, todate, 3,
								objorder.getLsuserMaster()));

				lstorder.addAll(lslogilablimsorderdetailRepository
						.findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndOrderflagAndFiletypeAndAssignedtoIsNullOrderByBatchcodeDesc(
								Directory_Code, 1, fromdate, todate, objorder.getOrderflag(), filetype, Directory_Code,
								2, objorder.getLsuserMaster(), fromdate, todate, objorder.getOrderflag(), filetype,
								Directory_Code, 3, objorder.getLsuserMaster(), fromdate, todate,
								objorder.getOrderflag(), filetype));

			}

		}

		if (objorder.getSearchCriteria().getContentsearchtype() != null
				&& objorder.getSearchCriteria().getContentsearch() != null) {
			List<Long> lstBatchcode = lstorder.stream().map(Logilaborders::getBatchcode).collect(Collectors.toList());
			if (lstBatchcode != null && lstBatchcode.size() > 0) {
				lstorder = Onsearchordercontent(lstBatchcode, objorder);
			}
		}

		lstorder.forEach(objorderDetail -> objorderDetail.setLstworkflow(objorder.getLstworkflow()));
		rtn_object.put("Orders", lstorder);
//		rtn_object.get("Orders")
		return rtn_object;

	}

	public List<Logilaborders> Onsearchordercontent(List<Long> lstBatchcode, LSlogilablimsorderdetail objorder) {
		List<Integer> lstsamplefilecode = new ArrayList<Integer>();
		lstsamplefilecode = lssamplefileRepository.getFilesamplecodeByBatchcodeIn(lstBatchcode);
		List<Logilaborders> lstorder = new ArrayList<Logilaborders>();
		List<LSsamplefile> idList = new ArrayList<LSsamplefile>();
		if (lstsamplefilecode != null && lstsamplefilecode.size() > 0) {
			idList = getsamplefileIds(lstsamplefilecode, objorder.getSearchCriteria(), objorder.getIsmultitenant());
			if (idList != null && idList.size() > 0) {

				if (objorder.getFiletype() == -1 && objorder.getOrderflag() != null) {
					lstorder = lslogilablimsorderdetailRepository
							.findByOrderflagAndCreatedtimestampBetweenAndLssamplefileIn(objorder.getOrderflag(),
									objorder.getFromdate(), objorder.getTodate(), idList);
				} else if (objorder.getFiletype() == -1 && objorder.getOrderflag() == null) {
					lstorder = lslogilablimsorderdetailRepository.findByCreatedtimestampBetweenAndLssamplefileIn(
							objorder.getFromdate(), objorder.getTodate(), idList);
				}

				else if (objorder.getOrderflag() == null) {
					lstorder = lslogilablimsorderdetailRepository
							.findByFiletypeAndCreatedtimestampBetweenAndLssamplefileIn(objorder.getFiletype(),
									objorder.getFromdate(), objorder.getTodate(), idList);
				} else {
					lstorder = lslogilablimsorderdetailRepository
							.findByOrderflagAndFiletypeAndCreatedtimestampBetweenAndLssamplefileIn(
									objorder.getOrderflag(), objorder.getFiletype(), objorder.getFromdate(),
									objorder.getTodate(), idList);
				}

			}

		}

		return lstorder;
	}

	public Map<String, Object> Getprotocolordersonproject(LSlogilabprotocoldetail objorder) {
		Map<String, Object> retuobjts = new HashMap<>();
		List<Logilabprotocolorders> lstorder = new ArrayList<Logilabprotocolorders>();
		Date fromdate = objorder.getFromdate();
		Date todate = objorder.getTodate();
		Integer protocoltype = objorder.getProtocoltype();
		if (protocoltype == -1 && objorder.getOrderflag() == null) {
//			lstorder = LSlogilabprotocoldetailRepository
//					.findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
//							objorder.getLsprojectmaster(), objorder.getTestcode(), 1, fromdate, todate);

			lstorder = LSlogilabprotocoldetailRepository
					.findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndCreatedtimestampBetweenOrLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
							objorder.getLsprojectmaster(), objorder.getTestcode(), 1, fromdate, todate,
							objorder.getLsprojectmaster(), objorder.getTestcode(), 2, fromdate, todate);
		} else if (protocoltype != -1 && objorder.getOrderflag() != null) {
			if (objorder.getRejected() != null) {
				lstorder = LSlogilabprotocoldetailRepository
						.findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndProtocoltypeAndOrderflagAndRejectedAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
								objorder.getLsprojectmaster(), objorder.getTestcode(), 1, protocoltype,
								objorder.getOrderflag(), 1, fromdate, todate);
			} else {
				lstorder = LSlogilabprotocoldetailRepository
						.findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
								objorder.getLsprojectmaster(), objorder.getTestcode(), 1, protocoltype,
								objorder.getOrderflag(), fromdate, todate);
			}
		} else if (protocoltype == -1 && objorder.getOrderflag() != null) {

			if (objorder.getRejected() != null) {
				lstorder = LSlogilabprotocoldetailRepository
						.findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndOrderflagAndRejectedAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
								objorder.getLsprojectmaster(), objorder.getTestcode(), 1, objorder.getOrderflag(), 1,
								fromdate, todate);
			}

			lstorder = LSlogilabprotocoldetailRepository
					.findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndOrderflagAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
							objorder.getLsprojectmaster(), objorder.getTestcode(), 1, objorder.getOrderflag(), fromdate,
							todate);
		} else if (protocoltype != -1 && objorder.getOrderflag() == null) {
			lstorder = LSlogilabprotocoldetailRepository
					.findByLsprojectmasterAndTestcodeAndOrderdisplaytypeAndAssignedtoIsNullAndProtocoltypeAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
							objorder.getLsprojectmaster(), objorder.getTestcode(), 1, protocoltype, fromdate, todate);
		}
//		lstorder.forEach(objorderDetail -> objorderDetail.setLstworkflow(objorder.getLstworkflow()));
		Map<String, Object> obj = Getprotocolordersondirectory(objorder.getLsprotocolorderstructure());
		if (obj.containsKey("protocolorders")) {
			@SuppressWarnings("unchecked")
			List<LSlogilabprotocoldetail> protocolOrders = (List<LSlogilabprotocoldetail>) obj.get("protocolorders");
			lstorder.addAll(protocolOrders.stream().map(lsOrderDetail -> new Logilabprotocolorders(
					lsOrderDetail.getProtocolordercode(), lsOrderDetail.getTeamcode(),
					lsOrderDetail.getProtoclordername(), lsOrderDetail.getOrderflag(), lsOrderDetail.getProtocoltype(),
					lsOrderDetail.getCreatedtimestamp(), lsOrderDetail.getCompletedtimestamp(),
					lsOrderDetail.getLsprotocolmaster(), lsOrderDetail.getlSprotocolworkflow(),
					lsOrderDetail.getLssamplemaster(), lsOrderDetail.getLsprojectmaster(), lsOrderDetail.getKeyword(),
					lsOrderDetail.getDirectorycode(), lsOrderDetail.getCreateby(), lsOrderDetail.getAssignedto(),
					lsOrderDetail.getLsrepositoriesdata(), lsOrderDetail.getLsrepositories(),
					lsOrderDetail.getElnmaterial(), lsOrderDetail.getElnmaterialinventory(),
					lsOrderDetail.getApproved(), lsOrderDetail.getRejected(), lsOrderDetail.getOrdercancell(),
					lsOrderDetail.getViewoption(), lsOrderDetail.getOrderstarted(), lsOrderDetail.getOrderstartedby(),
					lsOrderDetail.getOrderstartedon(), lsOrderDetail.getLockeduser(), lsOrderDetail.getLockedusername(),
					lsOrderDetail.getVersionno(), lsOrderDetail.getElnprotocolworkflow(),
					lsOrderDetail.getLsordernotification(), lsOrderDetail.getLsautoregister(),
					lsOrderDetail.getRepeat(), lsOrderDetail.getSentforapprovel(), lsOrderDetail.getApprovelaccept(),
					lsOrderDetail.getAutoregistercount(), lsOrderDetail.getLsuserMaster()))
					.collect(Collectors.toList()));

		}

		lstorder.forEach(
				objorderDetail -> objorderDetail.setLstelnprotocolworkflow(objorder.getLstelnprotocolworkflow()));
		List<Long> protocolordercode = new ArrayList<>();
		if (lstorder.size() > 0 && objorder.getSearchCriteriaType() != null) {
			protocolordercode = lstorder.stream().map(Logilabprotocolorders::getProtocolordercode)
					.collect(Collectors.toList());
			retuobjts.put("protocolordercodeslist", protocolordercode);
		}
		retuobjts.put("protocolorders", lstorder);

		return retuobjts;

	}

	public Map<String, Object> Getuserprotocolorders(Map<String, Object> objusers) {
		Map<String, Object> mapuserorders = new HashMap<String, Object>();

		ObjectMapper mapper = new ObjectMapper();
		LSuserMaster lsloginuser = mapper.convertValue(objusers.get("loginuser"), LSuserMaster.class);
		LSuserMaster lsselecteduser = mapper.convertValue(objusers.get("selecteduser"), LSuserMaster.class);
		Date fromdate = lsselecteduser.getObjuser().getFromdate();
		Date todate = lsselecteduser.getObjuser().getTodate();
		Integer directory = mapper.convertValue(objusers.get("directorycode"), Integer.class);
		LSuserMaster lsselectedfulluser = lsuserMasterRepository.findByusercode(lsselecteduser.getUsercode());
		Integer protocoltype = mapper.convertValue(objusers.get("protocoltype"), Integer.class);
		String Orderflag = null;
		if (objusers.get("Orderflag") != null) {
			Orderflag = mapper.convertValue(objusers.get("Orderflag"), String.class);
		}

		if (lsloginuser.getUsercode() == lsselecteduser.getUsercode()) {
			if (protocoltype == -1 && Orderflag == null) {
				mapuserorders.put("assigned",
						LSlogilabprotocoldetailRepository
								.findByAssignedtoAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(lsloginuser,
										fromdate, todate));
				mapuserorders.put("sharebyme", lsprotocolordersharedbyRepository
						.findBySharebyunifiedidAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
								lsselectedfulluser.getUnifieduserid(), 1, fromdate, todate));
				mapuserorders.put("sharetome", lsprotocolordersharetoRepository
						.findBySharetounifiedidAndSharestatusAndSharedonBetweenOrderBySharetoprotocolordercodeDesc(
								lsselectedfulluser.getUnifieduserid(), 1, fromdate, todate));

			} else if (protocoltype != -1 && Orderflag != null) {
				mapuserorders.put("assigned", LSlogilabprotocoldetailRepository
						.findByAssignedtoAndProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
								lsloginuser, protocoltype, Orderflag, fromdate, todate));
				mapuserorders.put("sharebyme", lsprotocolordersharedbyRepository
						.findBySharebyunifiedidAndProtocoltypeAndOrderflagAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
								lsselectedfulluser.getUnifieduserid(), protocoltype, Orderflag, 1, fromdate, todate));
				mapuserorders.put("sharetome", lsprotocolordersharetoRepository
						.findBySharetounifiedidAndProtocoltypeAndOrderflagAndSharestatusAndSharedonBetweenOrderBySharetoprotocolordercodeDesc(
								lsselectedfulluser.getUnifieduserid(), protocoltype, Orderflag, 1, fromdate, todate));

			} else if (protocoltype == -1 && Orderflag != null) {
				mapuserorders.put("assigned",
						LSlogilabprotocoldetailRepository
								.findByAssignedtoAndOrderflagAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
										lsloginuser, Orderflag, fromdate, todate));
				mapuserorders.put("sharebyme", lsprotocolordersharedbyRepository
						.findBySharebyunifiedidAndOrderflagAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
								lsselectedfulluser.getUnifieduserid(), Orderflag, 1, fromdate, todate));
				mapuserorders.put("sharetome", lsprotocolordersharetoRepository
						.findBySharetounifiedidAndOrderflagAndSharestatusAndSharedonBetweenOrderBySharetoprotocolordercodeDesc(
								lsselectedfulluser.getUnifieduserid(), Orderflag, 1, fromdate, todate));
			} else if (protocoltype != -1 && Orderflag == null) {
				mapuserorders.put("assigned",
						LSlogilabprotocoldetailRepository
								.findByAssignedtoAndProtocoltypeAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
										lsloginuser, protocoltype, fromdate, todate));
				mapuserorders.put("sharebyme", lsprotocolordersharedbyRepository
						.findBySharebyunifiedidAndProtocoltypeAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
								lsselectedfulluser.getUnifieduserid(), protocoltype, 1, fromdate, todate));
				mapuserorders.put("sharetome", lsprotocolordersharetoRepository
						.findBySharetounifiedidAndProtocoltypeAndSharestatusAndSharedonBetweenOrderBySharetoprotocolordercodeDesc(
								lsselectedfulluser.getUnifieduserid(), protocoltype, 1, fromdate, todate));
			}

		} else {
			if (protocoltype == -1 && Orderflag == null) {
				mapuserorders.put("assigned",
						LSlogilabprotocoldetailRepository
								.findByAssignedtoAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
										lsselectedfulluser, lsloginuser, fromdate, todate));
				mapuserorders.put("sharebyme", lsprotocolordersharedbyRepository
						.findBySharebyunifiedidAndSharetounifiedidAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
								lsloginuser.getUnifieduserid(), lsselectedfulluser.getUnifieduserid(), 1, fromdate,
								todate));
			} else if (protocoltype != -1 && Orderflag != null) {
				mapuserorders.put("assigned", LSlogilabprotocoldetailRepository
						.findByAssignedtoAndProtocoltypeAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
								lsselectedfulluser, protocoltype, Orderflag, lsloginuser, fromdate, todate));
				mapuserorders.put("sharebyme", lsprotocolordersharedbyRepository
						.findBySharebyunifiedidAndProtocoltypeAndOrderflagAndSharetounifiedidAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
								lsloginuser.getUnifieduserid(), protocoltype, Orderflag,
								lsselectedfulluser.getUnifieduserid(), 1, fromdate, todate));
			} else if (protocoltype == -1 && Orderflag != null) {
				mapuserorders.put("assigned", LSlogilabprotocoldetailRepository
						.findByAssignedtoAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
								lsselectedfulluser, Orderflag, lsloginuser, fromdate, todate));
				mapuserorders.put("sharebyme", lsprotocolordersharedbyRepository
						.findBySharebyunifiedidAndOrderflagAndSharetounifiedidAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
								lsloginuser.getUnifieduserid(), Orderflag, lsselectedfulluser.getUnifieduserid(), 1,
								fromdate, todate));
			} else if (protocoltype != -1 && Orderflag == null) {
				mapuserorders.put("assigned", LSlogilabprotocoldetailRepository
						.findByAssignedtoAndProtocoltypeAndLsuserMasterAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
								lsselectedfulluser, protocoltype, lsloginuser, fromdate, todate));
				mapuserorders.put("sharebyme", lsprotocolordersharedbyRepository
						.findBySharebyunifiedidAndProtocoltypeAndSharetounifiedidAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
								lsloginuser.getUnifieduserid(), protocoltype, lsselectedfulluser.getUnifieduserid(), 1,
								fromdate, todate));
			}
		}

		if (objusers.containsKey("searchCriteriaType")) {
			List<Long> protocolordercodeonassigned = new ArrayList<>();
			List<Long> protocolordercodesharebyme = new ArrayList<>();
			List<Long> protocolordercodesharetome = new ArrayList<>();
			List<Map<String, Object>> passmap = new ArrayList<Map<String, Object>>();
			Map<String, Object> passorderlistmap = new HashMap<String, Object>();
			if (mapuserorders.get("assigned") != null) {
				List<LSlogilabprotocoldetail> obj = mapper.convertValue(mapuserorders.get("assigned"),
						new TypeReference<List<LSlogilabprotocoldetail>>() {
						});
				protocolordercodeonassigned = obj.stream().map(LSlogilabprotocoldetail::getProtocolordercode)
						.collect(Collectors.toList());
				passorderlistmap.put("assigned", protocolordercodeonassigned);

			}
			if (mapuserorders.get("sharebyme") != null) {
				List<Lsprotocolordersharedby> obj = mapper.convertValue(mapuserorders.get("sharebyme"),
						new TypeReference<List<Lsprotocolordersharedby>>() {
						});
				protocolordercodesharebyme = obj.stream().map(Lsprotocolordersharedby::getShareprotocolordercode)
						.collect(Collectors.toList());
				passorderlistmap.put("sharebyme", protocolordercodesharebyme);
			}

			if (mapuserorders.get("sharetome") != null) {
				List<Lsprotocolordershareto> obj = mapper.convertValue(mapuserorders.get("sharetome"),
						new TypeReference<List<Lsprotocolordershareto>>() {
						});
				protocolordercodesharetome = obj.stream().map(Lsprotocolordershareto::getShareprotocolordercode)
						.collect(Collectors.toList());
				passorderlistmap.put("sharetome", protocolordercodesharetome);
			}
			passmap.add(passorderlistmap);
			mapuserorders = ProtocolMasterService.getAllProtocolStepLstonsharedorder(
					mapper.convertValue(objusers.get("ismultitenant"), Integer.class),
					mapper.convertValue(objusers.get("searchcontent"), String.class),
					mapper.convertValue(objusers.get("sitecode"), Integer.class), passmap);

		}

		mapuserorders.put("directorycode", directory);

		return mapuserorders;
	}

	public Map<String, Object> Getprotocolordersonsample(LSlogilabprotocoldetail objorder) {
		List<LSlogilabprotocoldetail> lstorder = new ArrayList<LSlogilabprotocoldetail>();
		Map<String, Object> retuobjts = new HashMap<>();
		Date fromdate = objorder.getFromdate();
		Date todate = objorder.getTodate();
		Integer protocoltype = objorder.getProtocoltype();
		if (protocoltype == -1 && objorder.getOrderflag() == null) {
//			lstorder = LSlogilabprotocoldetailRepository
//					.findByLssamplemasterAndViewoptionAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
//							objorder.getLssamplemaster(), 1, objorder.getTestcode(), 2, fromdate, todate,
//							objorder.getLssamplemaster(), 2, objorder.getLsuserMaster(), objorder.getTestcode(), 2,
//							fromdate, todate);

			lstorder = LSlogilabprotocoldetailRepository
					.findByLssamplemasterAndViewoptionAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
							objorder.getLssamplemaster(), 1, objorder.getTestcode(), 2, fromdate, todate,
							objorder.getLssamplemaster(), 1, objorder.getTestcode(), 1, fromdate, todate,
							objorder.getLssamplemaster(), 2, objorder.getLsuserMaster(), objorder.getTestcode(), 2,
							fromdate, todate);
		} else if (protocoltype != -1 && objorder.getOrderflag() != null) {
			if (objorder.getRejected() != null) {
				lstorder = LSlogilabprotocoldetailRepository
						.findByLssamplemasterAndViewoptionAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndTestcodeAndOrderdisplaytypeAndProtocoltypeAndOrderflagAndRejectedAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
								objorder.getLssamplemaster(), 1, objorder.getTestcode(), 2, fromdate, todate,
								objorder.getLssamplemaster(), 2, objorder.getLsuserMaster(), objorder.getTestcode(), 2,
								protocoltype, objorder.getOrderflag(), 1, fromdate, todate);
			} else {
				lstorder = LSlogilabprotocoldetailRepository
						.findByLssamplemasterAndViewoptionAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndTestcodeAndOrderdisplaytypeAndProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
								objorder.getLssamplemaster(), 1, objorder.getTestcode(), 2, fromdate, todate,
								objorder.getLssamplemaster(), 2, objorder.getLsuserMaster(), objorder.getTestcode(), 2,
								protocoltype, objorder.getOrderflag(), fromdate, todate);
			}

		} else if (protocoltype == -1 && objorder.getOrderflag() != null) {
			if (objorder.getRejected() != null) {
				lstorder = LSlogilabprotocoldetailRepository
						.findByLssamplemasterAndViewoptionAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndTestcodeAndOrderdisplaytypeAndOrderflagAndRejectedAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
								objorder.getLssamplemaster(), 1, objorder.getTestcode(), 2, fromdate, todate,
								objorder.getLssamplemaster(), 2, objorder.getLsuserMaster(), objorder.getTestcode(), 2,
								objorder.getOrderflag(), 1, fromdate, todate);

			} else {
				lstorder = LSlogilabprotocoldetailRepository
						.findByLssamplemasterAndViewoptionAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndTestcodeAndOrderdisplaytypeAndOrderflagAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
								objorder.getLssamplemaster(), 1, objorder.getTestcode(), 2, fromdate, todate,
								objorder.getLssamplemaster(), 2, objorder.getLsuserMaster(), objorder.getTestcode(), 2,
								objorder.getOrderflag(), fromdate, todate);
			}
		} else if (protocoltype != -1 && objorder.getOrderflag() == null) {
			lstorder = LSlogilabprotocoldetailRepository
					.findByLssamplemasterAndViewoptionAndTestcodeAndOrderdisplaytypeAndCreatedtimestampBetweenOrLssamplemasterAndViewoptionAndLsuserMasterAndTestcodeAndOrderdisplaytypeAndProtocoltypeAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
							objorder.getLssamplemaster(), 1, objorder.getTestcode(), 2, fromdate, todate,
							objorder.getLssamplemaster(), 2, objorder.getLsuserMaster(), objorder.getTestcode(), 2,
							protocoltype, fromdate, todate);
		}
//		lstorder.forEach(objorderDetail -> objorderDetail.setLstworkflow(objorder.getLstworkflow()));
		lstorder.forEach(
				objorderDetail -> objorderDetail.setLstelnprotocolworkflow(objorder.getLstelnprotocolworkflow()));
		List<Long> protocolordercode = new ArrayList<>();
		if (lstorder.size() > 0 && objorder.getSearchCriteriaType() != null) {
			protocolordercode = lstorder.stream().map(LSlogilabprotocoldetail::getProtocolordercode)
					.collect(Collectors.toList());
			retuobjts.put("protocolordercodeslist", protocolordercode);
		}
		retuobjts.put("protocolorders", lstorder);

		return retuobjts;
	}

	public Map<String, Object> Getprotocolorderbyflaganduser(LSlogilabprotocoldetail objorder) {

		Map<String, Object> retuobjts = new HashMap<>();
		List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objorder.getLsuserMaster());
		List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
				objorder.getLsuserMaster().getLssitemaster());
		List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
		List<Logilabprotocolorders> lstorder = new ArrayList<Logilabprotocolorders>();
		Date fromdate = objorder.getFromdate();
		Date todate = objorder.getTodate();
		Integer protocoltype = objorder.getProtocoltype();
//		List<LSlogilabprotocoldetail> retuobj = new ArrayList<LSlogilabprotocoldetail>();
//		List<LSsamplemaster> lstsample = lssamplemasterrepository.findSamplecodeAndSamplenameBystatusAndLssitemaster(1,
//				objorder.getLsuserMaster().getLssitemaster());
		List<Elnmaterial> nmaterialcode = elnmaterialRepository
				.findByNsitecode(objorder.getLsuserMaster().getLssitemaster().getSitecode());
		List<Integer> userlist = objorder.getLstuserMaster() != null
				? objorder.getLstuserMaster().stream().map(LSuserMaster::getUsercode).collect(Collectors.toList())
				: new ArrayList<Integer>();

		int chunkSize = Integer.parseInt(env.getProperty("lssamplecount"));
		int totalSamples = nmaterialcode.size();
		List<Long> immutableNegativeValues = Arrays.asList(-3L, -22L);
		List<Lsprotocolorderstructure> lstdir;
		if (objorder.getLstuserMaster() == null) {
			lstdir = lsprotocolorderStructurerepository
					.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
							objorder.getLsuserMaster().getLssitemaster(), 1, immutableNegativeValues,
							objorder.getLsuserMaster(), 2, objorder.getLsuserMaster(), 3);
		} else {
			lstdir = lsprotocolorderStructurerepository
					.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
							objorder.getLsuserMaster().getLssitemaster(), 1, immutableNegativeValues,
							objorder.getLsuserMaster(), 2, objorder.getLsuserMaster().getLssitemaster(), 3,
							objorder.getLstuserMaster());
		}
		lstdir.addAll(lsprotocolorderStructurerepository.findByDirectorycodeIn(immutableNegativeValues));
		List<Long> Directory_Code = lstdir.stream().map(Lsprotocolorderstructure::getDirectorycode)
				.collect(Collectors.toList());
		if (objorder.getTestcode() == null && objorder.getLsprojectmaster() == null && objorder.getRejected() == null) {
			lstorder.addAll(LSlogilabprotocoldetailRepository
					.findByOrderflagAndLsprojectmasterInAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndOrdercancellIsNull(
							objorder.getOrderflag(), lstproject, protocoltype, fromdate, todate));

//			lstorder.addAll(LSlogilabprotocoldetailRepository
//					.findByOrderflagAndLsprojectmasterInAndProtocoltypeAndCreatedtimestampBetween(
//							objorder.getOrderflag(), lstproject, protocoltype, fromdate, todate));

			List<Logilabprotocolorders> lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize)
					.parallel().mapToObj(i -> {
						int startIndex = i * chunkSize;
						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
						List<Elnmaterial> currentChunk = nmaterialcode.subList(startIndex, endIndex);
						List<Logilabprotocolorders> orderChunk = new ArrayList<>();
//						AndElnmaterialIn
						orderChunk.addAll(LSlogilabprotocoldetailRepository
								.findByOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInAndViewoptionAndOrdercancellIsNull(
										objorder.getOrderflag(), protocoltype, fromdate, todate, currentChunk, 1));
						orderChunk.addAll(LSlogilabprotocoldetailRepository
								.findByOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInAndViewoptionAndCreatebyAndOrdercancellIsNull(
										objorder.getOrderflag(), protocoltype, fromdate, todate, currentChunk, 2,
										objorder.getLsuserMaster().getUsercode()));
						orderChunk.addAll(LSlogilabprotocoldetailRepository
								.findByOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInAndViewoptionAndCreatebyInAndOrdercancellIsNullOrderByProtocolordercodeDesc(
										objorder.getOrderflag(), protocoltype, fromdate, todate, currentChunk, 3,
										userlist));
						return orderChunk;
					}).flatMap(List::stream).collect(Collectors.toList());
			lstorder.addAll(lstorderobj);

			if (objorder.getLstuserMaster() == null || objorder.getLstuserMaster().size() == 0) {
				lstorder.addAll(LSlogilabprotocoldetailRepository
						.findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndAssignedtoIsNullAndOrdercancellIsNullOrderByProtocolordercodeDesc(
								Directory_Code, 1, fromdate, todate, objorder.getOrderflag(), protocoltype,
								Directory_Code, 2, objorder.getLsuserMaster(), fromdate, todate,
								objorder.getOrderflag(), protocoltype, Directory_Code, 3, objorder.getLsuserMaster(),
								fromdate, todate, objorder.getOrderflag(), protocoltype));
			} else {
				lstorder.addAll(LSlogilabprotocoldetailRepository
						.findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndAssignedtoIsNullAndOrdercancellIsNullOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndAssignedtoIsNullAndOrdercancellIsNullOrderByProtocolordercodeDesc(
								Directory_Code, 1, fromdate, todate, objorder.getOrderflag(), protocoltype,
								Directory_Code, 2, objorder.getLsuserMaster(), fromdate, todate,
								objorder.getOrderflag(), protocoltype, Directory_Code, 3, fromdate, todate, userlist,
								objorder.getOrderflag(), protocoltype));

			}

		} else if (objorder.getTestcode() == null && objorder.getLsprojectmaster() == null
				&& objorder.getRejected() != null) {

			List<Logilabprotocolorders> lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize)
					.parallel().mapToObj(i -> {
						int startIndex = i * chunkSize;
						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
						List<Elnmaterial> currentChunk = nmaterialcode.subList(startIndex, endIndex);
						List<Logilabprotocolorders> orderChunk = new ArrayList<>();
						orderChunk = LSlogilabprotocoldetailRepository
								.findByOrderflagAndRejectedAndLsprojectmasterInAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndRejectedAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInOrderByProtocolordercodeDesc(
										objorder.getOrderflag(), 1, lstproject, protocoltype, fromdate, todate,
										objorder.getOrderflag(), 1, protocoltype, fromdate, todate, currentChunk);
						return orderChunk;
					}).flatMap(List::stream).collect(Collectors.toList());
			lstorder.addAll(lstorderobj);

			if (objorder.getLstuserMaster().size() == 0) {
				lstorder.addAll(LSlogilabprotocoldetailRepository
						.findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedOrderByProtocolordercodeDesc(
								Directory_Code, 1, fromdate, todate, objorder.getOrderflag(), protocoltype, 1,
								Directory_Code, 2, objorder.getLsuserMaster(), fromdate, todate,
								objorder.getOrderflag(), protocoltype, 1, Directory_Code, 3, objorder.getLsuserMaster(),
								fromdate, todate, objorder.getOrderflag(), protocoltype, 1));
			} else {
				lstorder.addAll(LSlogilabprotocoldetailRepository
						.findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedOrderByProtocolordercodeDesc(
								Directory_Code, 1, fromdate, todate, objorder.getOrderflag(), protocoltype, 1,
								Directory_Code, 2, objorder.getLsuserMaster(), fromdate, todate,
								objorder.getOrderflag(), protocoltype, 1, Directory_Code, 3, fromdate, todate, userlist,
								objorder.getOrderflag(), protocoltype, 1));

			}

		} else if (objorder.getTestcode() == null && objorder.getLsprojectmaster() != null
				&& objorder.getRejected() == null) {

			List<Logilabprotocolorders> lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize)
					.parallel().mapToObj(i -> {
						int startIndex = i * chunkSize;
						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
						List<Elnmaterial> currentChunk = nmaterialcode.subList(startIndex, endIndex);
						List<Logilabprotocolorders> orderChunk = new ArrayList<>();
						orderChunk = LSlogilabprotocoldetailRepository
								.findByOrderflagAndLsprojectmasterAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInOrderByProtocolordercodeDesc(
										objorder.getOrderflag(), objorder.getLsprojectmaster(), protocoltype, fromdate,
										todate, objorder.getOrderflag(), protocoltype, fromdate, todate, currentChunk);
						return orderChunk;
					}).flatMap(List::stream).collect(Collectors.toList());
			lstorder.addAll(lstorderobj);

		} else if (objorder.getTestcode() != null && objorder.getLsprojectmaster() == null
				&& objorder.getRejected() == null) {

			List<Logilabprotocolorders> lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize)
					.parallel().mapToObj(i -> {
						int startIndex = i * chunkSize;
						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
						List<Elnmaterial> currentChunk = nmaterialcode.subList(startIndex, endIndex);
						List<Logilabprotocolorders> orderChunk = new ArrayList<>();
						orderChunk = LSlogilabprotocoldetailRepository
								.findByOrderflagAndLsprojectmasterInAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInOrderByProtocolordercodeDesc(
										objorder.getOrderflag(), lstproject, objorder.getTestcode(), protocoltype,
										fromdate, todate, objorder.getOrderflag(), objorder.getTestcode(), protocoltype,
										fromdate, todate, currentChunk);
						return orderChunk;
					}).flatMap(List::stream).collect(Collectors.toList());
			lstorder.addAll(lstorderobj);

			if (objorder.getLstuserMaster().size() == 0) {
				lstorder.addAll(LSlogilabprotocoldetailRepository
						.findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndTestcodeOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndTestcodeOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndTestcodeOrderByProtocolordercodeDesc(
								Directory_Code, 1, fromdate, todate, objorder.getOrderflag(), protocoltype,
								objorder.getTestcode(), Directory_Code, 2, objorder.getLsuserMaster(), fromdate, todate,
								objorder.getOrderflag(), protocoltype, objorder.getTestcode(), Directory_Code, 3,
								objorder.getLsuserMaster(), fromdate, todate, objorder.getOrderflag(), protocoltype,
								objorder.getTestcode()));
			} else {
				lstorder.addAll(LSlogilabprotocoldetailRepository
						.findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndTestcodeOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndTestcodeOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndTestcodeOrderByProtocolordercodeDesc(
								Directory_Code, 1, fromdate, todate, objorder.getOrderflag(), protocoltype,
								objorder.getTestcode(), Directory_Code, 2, objorder.getLsuserMaster(), fromdate, todate,
								objorder.getOrderflag(), protocoltype, objorder.getTestcode(), Directory_Code, 3,
								fromdate, todate, userlist, objorder.getOrderflag(), protocoltype,
								objorder.getTestcode()));

			}

		} else if (objorder.getTestcode() != null && objorder.getLsprojectmaster() != null
				&& objorder.getRejected() == null) {

			List<Logilabprotocolorders> lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize)
					.parallel().mapToObj(i -> {
						int startIndex = i * chunkSize;
						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
						List<Elnmaterial> currentChunk = nmaterialcode.subList(startIndex, endIndex);
						List<Logilabprotocolorders> orderChunk = new ArrayList<>();
						orderChunk = LSlogilabprotocoldetailRepository
								.findByOrderflagAndLsprojectmasterAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInOrderByProtocolordercodeDesc(
										objorder.getOrderflag(), objorder.getLsprojectmaster(), objorder.getTestcode(),
										protocoltype, fromdate, todate, objorder.getOrderflag(), objorder.getTestcode(),
										protocoltype, fromdate, todate, currentChunk);
						return orderChunk;
					}).flatMap(List::stream).collect(Collectors.toList());
			lstorder.addAll(lstorderobj);

		} else if (objorder.getTestcode() != null && objorder.getLsprojectmaster() == null
				&& objorder.getRejected() != null) {

			List<Logilabprotocolorders> lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize)
					.parallel().mapToObj(i -> {
						int startIndex = i * chunkSize;
						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
						List<Elnmaterial> currentChunk = nmaterialcode.subList(startIndex, endIndex);
						List<Logilabprotocolorders> orderChunk = new ArrayList<>();
						orderChunk = LSlogilabprotocoldetailRepository
								.findByOrderflagAndRejectedAndLsprojectmasterInAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndRejectedAndLsprojectmasterIsNullAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInOrderByProtocolordercodeDesc(
										objorder.getOrderflag(), 1, lstproject, objorder.getTestcode(), protocoltype,
										fromdate, todate, objorder.getOrderflag(), 1, objorder.getTestcode(),
										protocoltype, fromdate, todate, currentChunk);
						return orderChunk;
					}).flatMap(List::stream).collect(Collectors.toList());
			lstorder.addAll(lstorderobj);

			if (objorder.getLstuserMaster() == null || objorder.getLstuserMaster().size() == 0) {
				lstorder.addAll(LSlogilabprotocoldetailRepository
						.findByDirectorycodeAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedAndTestcodeOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedAndTestcodeOrDirectorycodeAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedAndTestcodeOrderByProtocolordercodeDesc(
								Directory_Code, 1, fromdate, todate, objorder.getOrderflag(), protocoltype, 1,
								objorder.getTestcode(), Directory_Code, 2, objorder.getLsuserMaster(), fromdate, todate,
								objorder.getOrderflag(), protocoltype, 1, objorder.getTestcode(), Directory_Code, 3,
								objorder.getLsuserMaster(), fromdate, todate, objorder.getOrderflag(), protocoltype, 1,
								objorder.getTestcode()));
			} else {
				lstorder.addAll(LSlogilabprotocoldetailRepository
						.findByDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedAndTestcodeOrDirectorycodeInAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedAndTestcodeOrDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndOrderflagAndLsprojectmasterIsNullAndProtocoltypeAndRejectedAndTestcodeOrderByProtocolordercodeDesc(
								Directory_Code, 1, fromdate, todate, objorder.getOrderflag(), protocoltype, 1,
								Directory_Code, objorder.getTestcode(), 2, objorder.getLsuserMaster(), fromdate, todate,
								objorder.getOrderflag(), protocoltype, 1, objorder.getTestcode(), Directory_Code, 3,
								fromdate, todate, userlist, objorder.getOrderflag(), protocoltype, 1,
								objorder.getTestcode()));

			}

		} else if (objorder.getTestcode() == null && objorder.getLsprojectmaster() != null
				&& objorder.getRejected() != null) {

			List<Logilabprotocolorders> lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize)
					.parallel().mapToObj(i -> {
						int startIndex = i * chunkSize;
						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
						List<Elnmaterial> currentChunk = nmaterialcode.subList(startIndex, endIndex);
						List<Logilabprotocolorders> orderChunk = new ArrayList<>();
						orderChunk = LSlogilabprotocoldetailRepository
								.findByOrderflagAndRejectedAndLsprojectmasterAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndRejectedAndLsprojectmasterIsNullAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInOrderByProtocolordercodeDesc(
										objorder.getOrderflag(), 1, objorder.getLsprojectmaster(), protocoltype,
										fromdate, todate, objorder.getOrderflag(), 1, protocoltype, fromdate, todate,
										currentChunk);
						return orderChunk;
					}).flatMap(List::stream).collect(Collectors.toList());
			lstorder.addAll(lstorderobj);

		} else if (objorder.getTestcode() != null && objorder.getLsprojectmaster() != null
				&& objorder.getRejected() != null) {

			List<Logilabprotocolorders> lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize)
					.parallel().mapToObj(i -> {
						int startIndex = i * chunkSize;
						int endIndex = Math.min(startIndex + chunkSize, totalSamples);
						List<Elnmaterial> currentChunk = nmaterialcode.subList(startIndex, endIndex);
						List<Logilabprotocolorders> orderChunk = new ArrayList<>();
						orderChunk = LSlogilabprotocoldetailRepository
								.findByOrderflagAndRejectedAndLsprojectmasterAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndRejectedAndLsprojectmasterIsNullAndTestcodeAndProtocoltypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndElnmaterialInOrderByProtocolordercodeDesc(
										objorder.getOrderflag(), 1, objorder.getLsprojectmaster(),
										objorder.getTestcode(), protocoltype, fromdate, todate, objorder.getOrderflag(),
										1, objorder.getTestcode(), protocoltype, fromdate, todate, currentChunk);
						return orderChunk;
					}).flatMap(List::stream).collect(Collectors.toList());
			lstorder.addAll(lstorderobj);

		}

		lstorder.forEach(
				objorderDetail -> objorderDetail.setLstelnprotocolworkflow(objorder.getLstelnprotocolworkflow()));
		List<Long> protocolordercode = new ArrayList<>();
		if (lstorder.size() > 0 && objorder.getSearchCriteriaType() != null) {
			protocolordercode = lstorder.stream().map(Logilabprotocolorders::getProtocolordercode)
					.collect(Collectors.toList());
			retuobjts.put("protocolordercodeslist", protocolordercode);
		}
		retuobjts.put("protocolorders", lstorder);

		return retuobjts;

	}

	public List<LSsheetfolderfiles> Getfilesforfolder(LSsheetfolderfiles objfiles) throws Exception {
		List<LSsheetfolderfiles> lstfiles = new ArrayList<LSsheetfolderfiles>();
		try {
			lstfiles = lssheetfolderfilesRepository
					.findByDirectorycodeAndFileforAndCreatedtimestampBetweenOrderByFolderfilecode(
							objfiles.getDirectorycode(), objfiles.getFilefor(), objfiles.getFromdate(),
							objfiles.getTodate());
			return lstfiles;
		} catch (Exception e) {
			return lstfiles;
		}

//		List<LSsheetfolderfiles> lstfiles = new ArrayList<LSsheetfolderfiles>();
//
//		lstfiles = lssheetfolderfilesRepository
//				.findByDirectorycodeAndFileforOrderByFolderfilecode(objfiles.getDirectorycode(), objfiles.getFilefor());
//
//		return lstfiles;
	}

	public Response validatefileexistonfolder(LSsheetfolderfiles objfile) {
		Response response = new Response();
		long filecount = lssheetfolderfilesRepository.countByDirectorycodeAndFileforAndFilename(
				objfile.getDirectorycode(), objfile.getFilefor(), objfile.getFilename());
		if (filecount > 0) {
			response.setStatus(false);
		} else {
			response.setStatus(true);
		}
		return response;
	}

	public ResponseEntity<InputStreamResource> downloadsheetfileforfolder(Integer multitenant, String tenant,
			String fileid) throws IOException {
		HttpHeaders header = new HttpHeaders();
		header.set("Content-Disposition", "attachment; filename=" + fileid);

		if (multitenant == 1) {
			InputStream fileStream = cloudFileManipulationservice.retrieveCloudFile(fileid,
					tenant + "sheetfolderfiles");
			InputStreamResource resource = null;
			byte[] content = IOUtils.toByteArray(fileStream);
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
		} else {
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
	}

	public List<LSsheetfolderfiles> Getaddedfilesforfolder(List<String> lstuuid) {
		List<LSsheetfolderfiles> lstfiles = new ArrayList<LSsheetfolderfiles>();
		lstfiles = lssheetfolderfilesRepository.findByUuidInOrderByFolderfilecode(lstuuid);

		return lstfiles;
	}

	public List<Logilaborders> GetordersonFilter(LSlogilablimsorderdetail objorder) {
		List<Logilaborders> lstorder = new ArrayList<Logilaborders>();
		List<Long> lstBatchcode = new ArrayList<Long>();
		List<Integer> lstsamplefilecode = new ArrayList<Integer>();
		List<LSsamplefile> idList = new ArrayList<LSsamplefile>();

		if (objorder.getSearchCriteria().getContentsearchtype() != null
				&& objorder.getSearchCriteria().getContentsearch() != null) {

			if (objorder.getFiletype() == -1) {
				lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeAndcreateddate(objorder.getFromdate(),
						objorder.getTodate());
			} else {
				lstBatchcode = lslogilablimsorderdetailRepository.getBatchcodeonFiletypeAndcreateddate(
						objorder.getFiletype(), objorder.getFromdate(), objorder.getTodate());
			}

			if (lstBatchcode != null && lstBatchcode.size() > 0) {
				lstsamplefilecode = lssamplefileRepository.getFilesamplecodeByBatchcodeIn(lstBatchcode);
			}

			if (lstsamplefilecode != null && lstsamplefilecode.size() > 0) {
				idList = getsamplefileIds(lstsamplefilecode, objorder.getSearchCriteria(), objorder.getIsmultitenant());
				if (idList != null && idList.size() > 0) {

					if (objorder.getFiletype() == -1 && objorder.getOrderflag() != null) {
						lstorder = lslogilablimsorderdetailRepository
								.findByOrderflagAndCreatedtimestampBetweenAndLssamplefileIn(objorder.getOrderflag(),
										objorder.getFromdate(), objorder.getTodate(), idList);
					} else if (objorder.getFiletype() == -1 && objorder.getOrderflag() == null) {
						lstorder = lslogilablimsorderdetailRepository.findByCreatedtimestampBetweenAndLssamplefileIn(
								objorder.getFromdate(), objorder.getTodate(), idList);
					}

					else if (objorder.getOrderflag() == null) {
						lstorder = lslogilablimsorderdetailRepository
								.findByFiletypeAndCreatedtimestampBetweenAndLssamplefileIn(objorder.getFiletype(),
										objorder.getFromdate(), objorder.getTodate(), idList);
					} else {
						lstorder = lslogilablimsorderdetailRepository
								.findByOrderflagAndFiletypeAndCreatedtimestampBetweenAndLssamplefileIn(
										objorder.getOrderflag(), objorder.getFiletype(), objorder.getFromdate(),
										objorder.getTodate(), idList);
					}

				}
			}

		}

		return lstorder;

	}

	public List<LSsheetfolderfiles> UpdateFolderforfiles(LSsheetfolderfiles[] files) throws Exception {
		List<LSsheetfolderfiles> lstfile = Arrays.asList(files);
		if (lstfile.size() > 0) {
			List<Integer> lstfilesid = lstfile.stream().map(LSsheetfolderfiles::getFolderfilecode)
					.collect(Collectors.toList());
			lssheetfolderfilesRepository.updatedirectory(lstfile.get(0).getDirectorycode(), lstfilesid);
		}
		return lstfile;
	}

	public LSsheetfolderfiles UpdateFolderforfile(LSsheetfolderfiles file) throws Exception {
		lssheetfolderfilesRepository.updatedirectory(file.getDirectorycode(), file.getFolderfilecode());
		return file;
	}

	public List<Logilaborders> Getordersonassignedandmyorders(Map<String, Object> objusers) {
		ObjectMapper obj = new ObjectMapper();
		LSlogilablimsorderdetail lslogilablimsorderdetail = obj.convertValue(objusers.get("lslogilablimsorderdetail"),
				new TypeReference<LSlogilablimsorderdetail>() {
				});
		List<Logilaborders> lstorder = new ArrayList<Logilaborders>();
		Integer filetype = lslogilablimsorderdetail.getFiletype();
		String Orderflag = null;
		if (objusers.containsKey("orderflag")) {
			Orderflag = obj.convertValue(objusers.get("orderflag"), String.class);
		}

		if (lslogilablimsorderdetail.getOrderflag().equals("A")) {

			if (filetype == -1 && Orderflag == null) {
				lstorder = lslogilablimsorderdetailRepository
						.findByLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
								lslogilablimsorderdetail.getLsuserMaster(), lslogilablimsorderdetail.getLsuserMaster(),
								lslogilablimsorderdetail.getFromdate(), lslogilablimsorderdetail.getTodate());
			} else if (filetype != -1 && Orderflag != null) {
				lstorder = lslogilablimsorderdetailRepository
						.findByLsuserMasterAndFiletypeAndOrderflagAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
								lslogilablimsorderdetail.getLsuserMaster(), lslogilablimsorderdetail.getFiletype(),
								Orderflag, lslogilablimsorderdetail.getLsuserMaster(),
								lslogilablimsorderdetail.getFromdate(), lslogilablimsorderdetail.getTodate());

			} else if (filetype == -1 && Orderflag != null) {
				lstorder = lslogilablimsorderdetailRepository
						.findByLsuserMasterAndOrderflagAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
								lslogilablimsorderdetail.getLsuserMaster(), Orderflag,
								lslogilablimsorderdetail.getLsuserMaster(), lslogilablimsorderdetail.getFromdate(),
								lslogilablimsorderdetail.getTodate());

			} else if (filetype != -1 && Orderflag == null) {
				lstorder = lslogilablimsorderdetailRepository
						.findByLsuserMasterAndFiletypeAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrderByBatchcodeDesc(
								lslogilablimsorderdetail.getLsuserMaster(), lslogilablimsorderdetail.getFiletype(),
								lslogilablimsorderdetail.getLsuserMaster(), lslogilablimsorderdetail.getFromdate(),
								lslogilablimsorderdetail.getTodate());
			}

		} else if (lslogilablimsorderdetail.getOrderflag().equals("M")) {

			if (filetype == -1 && Orderflag == null) {
				lstorder = lslogilablimsorderdetailRepository
						.findByAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								lslogilablimsorderdetail.getLsuserMaster(), lslogilablimsorderdetail.getFromdate(),
								lslogilablimsorderdetail.getTodate());
			} else if (filetype != -1 && Orderflag != null) {
				lstorder = lslogilablimsorderdetailRepository
						.findByAssignedtoAndFiletypeAndOrderflagAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								lslogilablimsorderdetail.getLsuserMaster(), lslogilablimsorderdetail.getFiletype(),
								Orderflag, lslogilablimsorderdetail.getFromdate(),
								lslogilablimsorderdetail.getTodate());
			} else if (filetype == -1 && Orderflag != null) {
				lstorder = lslogilablimsorderdetailRepository
						.findByAssignedtoAndOrderflagAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								lslogilablimsorderdetail.getLsuserMaster(), Orderflag,
								lslogilablimsorderdetail.getFromdate(), lslogilablimsorderdetail.getTodate());

			} else if (filetype != -1 && Orderflag == null) {
				lstorder = lslogilablimsorderdetailRepository
						.findByAssignedtoAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								lslogilablimsorderdetail.getLsuserMaster(), lslogilablimsorderdetail.getFiletype(),
								lslogilablimsorderdetail.getFromdate(), lslogilablimsorderdetail.getTodate());
			}
		}

		if (lslogilablimsorderdetail.getSearchCriteria() != null
				&& lslogilablimsorderdetail.getSearchCriteria().getContentsearchtype() != null
				&& lslogilablimsorderdetail.getSearchCriteria().getContentsearch() != null) {

			lstorder = GetmyordersonFilter(lslogilablimsorderdetail, lstorder, Orderflag);

			lstorder.forEach(
					objorderDetail -> objorderDetail.setLstworkflow(lslogilablimsorderdetail.getLstworkflow()));
			return lstorder;
		} else {

			lstorder.forEach(
					objorderDetail -> objorderDetail.setLstworkflow(lslogilablimsorderdetail.getLstworkflow()));
			return lstorder;
		}
	}

	public Map<String, Object> Getusersharedorders(Map<String, Object> objusers) {

		Map<String, Object> mapuserorders = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();

		LSuserMaster lsselecteduser = mapper.convertValue(objusers.get("selecteduser"), LSuserMaster.class);
		Date fromdate = lsselecteduser.getObjuser().getFromdate();
		Date todate = lsselecteduser.getObjuser().getTodate();
		Long directory = mapper.convertValue(objusers.get("directorycode"), Long.class);
		Integer filetype = mapper.convertValue(objusers.get("filetype"), Integer.class);

		Integer ismultitenant = mapper.convertValue(objusers.get("ismultitenant"), Integer.class);

		List<LSlogilablimsorderdetail> orderlst = new ArrayList<LSlogilablimsorderdetail>();

		LSlogilablimsorderdetail logisearch = mapper.convertValue(objusers.get("lslogilablimsorderdetail"),
				LSlogilablimsorderdetail.class);

		List<Lsordersharedby> sharedbyobj = new ArrayList<Lsordersharedby>();
		List<Lsordershareto> sharedtoobj = new ArrayList<Lsordershareto>();

		if (objusers.get("filefor").equals("OSBM")) {
			if (filetype == -1) {

				sharedbyobj = lsordersharedbyRepository
						.findByUsersharedbyAndSharestatusAndSharedonBetweenOrderBySharedbycodeDesc(lsselecteduser, 1,
								fromdate, todate);
				mapuserorders.put("sharebyme", sharedbyobj);
			} else {
				sharedbyobj = lsordersharedbyRepository
						.findByUsersharedbyAndOrdertypeAndSharestatusAndSharedonBetweenOrderBySharedbycodeDesc(
								lsselecteduser, filetype, 1, fromdate, todate);
				mapuserorders.put("sharebyme", sharedbyobj);
			}
		} else if (objusers.get("filefor").equals("OSTM")) {
			if (filetype == -1) {
				sharedtoobj = lsordersharetoRepository
						.findByUsersharedonAndSharestatusAndSharedonBetweenOrderBySharetocodeDesc(lsselecteduser, 1,
								fromdate, todate);
				mapuserorders.put("sharetome", sharedtoobj);
			} else {
				sharedtoobj = lsordersharetoRepository
						.findByUsersharedonAndOrdertypeAndSharestatusAndSharedonBetweenOrderBySharetocodeDesc(
								lsselecteduser, filetype, 1, fromdate, todate);
				mapuserorders.put("sharetome", sharedtoobj);
			}
		}

		List<Lsordersharedby> finalsharedbylist = new ArrayList<Lsordersharedby>();
		List<Lsordershareto> finalsharedtolist = new ArrayList<Lsordershareto>();

		// List<Lsordersharedby> retrievedList = (List<Lsordersharedby>)
		// mapuserorders.get("sharebyme");
		if (!sharedbyobj.isEmpty()) {
			List<LSlogilablimsorderdetail> lstorder = sharedbyobj.stream().map(Lsordersharedby::getOrder)
					.collect(Collectors.toList());

			if (logisearch.getSearchCriteria() != null && logisearch.getSearchCriteria().getContentsearchtype() != null
					&& logisearch.getSearchCriteria().getContentsearch() != null) {

				LSlogilablimsorderdetail orderdetail = new LSlogilablimsorderdetail();
				orderdetail.setFromdate(fromdate);
				orderdetail.setTodate(todate);
				orderdetail.setIsmultitenant(ismultitenant);
				orderdetail.setSearchCriteria(logisearch.getSearchCriteria());
				orderdetail.setFiletype(filetype);
				orderdetail.setDirectorycode(directory);

				orderlst = GetsharedordersonFilter(orderdetail, lstorder);

//			 for(Lsordersharedby row1 : sharedbyobj) {
//				
//				for(Logilaborders detailrow :orderlst) {
//									
//			    	if(row1.getSharebatchcode().equals(detailrow.getBatchcode()) ) {
//			    			//&& row1.getSharebatchid().equals(detailrow.getBatchid()) ) {
//					  
//			    		finalsharedbylist.add(row1);
//			    		
//				   }
//				}
//			  }
				final List<LSlogilablimsorderdetail> finalOrderlst = orderlst;

				finalsharedbylist = sharedbyobj.stream()
						.filter(srow1 -> finalOrderlst.stream()
								.anyMatch(detailrow -> srow1.getSharebatchcode().equals(detailrow.getBatchcode())))
						.collect(Collectors.toList());

			}
		} else {
			List<LSlogilablimsorderdetail> lstorder = sharedtoobj.stream().map(Lsordershareto::getOrder)
					.collect(Collectors.toList());

			if (logisearch.getSearchCriteria() != null && logisearch.getSearchCriteria().getContentsearchtype() != null
					&& logisearch.getSearchCriteria().getContentsearch() != null) {

				LSlogilablimsorderdetail orderdetails = new LSlogilablimsorderdetail();

				orderdetails.setFromdate(fromdate);
				orderdetails.setTodate(todate);
				orderdetails.setIsmultitenant(ismultitenant);
				orderdetails.setSearchCriteria(logisearch.getSearchCriteria());
				orderdetails.setFiletype(filetype);
				orderdetails.setDirectorycode(directory);

				orderlst = GetsharedordersonFilter(orderdetails, lstorder);

//				 for(Lsordershareto srow1 : sharedtoobj) {
//					
//					for(Logilaborders detailrow :orderlst) {
//										
//				    	if(srow1.getSharebatchcode().equals(detailrow.getBatchcode()) ) {
//				    			//&& row1.getSharebatchid().equals(detailrow.getBatchid()) ) {
//						  
//				    		finalsharedtolist.add(srow1);
//				    		
//					   }
//					}
//				  }

				final List<LSlogilablimsorderdetail> finalOrderlst = orderlst;

				finalsharedtolist = sharedtoobj.stream()
						.filter(srow1 -> finalOrderlst.stream()
								.anyMatch(detailrow -> srow1.getSharebatchcode().equals(detailrow.getBatchcode())))
						.collect(Collectors.toList());

			}
		}

		if (logisearch.getSearchCriteria() != null && logisearch.getSearchCriteria().getContentsearchtype() != null
				&& logisearch.getSearchCriteria().getContentsearch() != null) {

			if (!finalsharedbylist.isEmpty()) {
				map.put("sharebyme", finalsharedbylist);
				map.put("directorycode", directory);
				return map;
			} else {
				map.put("sharetome", finalsharedtolist);
				map.put("directorycode", directory);
				return map;
			}
		} else {
			mapuserorders.put("directorycode", directory);
			return mapuserorders;
		}
	}

	public List<LSSheetOrderStructure> Updateparentforfolder(LSSheetOrderStructure[] folders) {
		List<LSSheetOrderStructure> lstfolders = Arrays.asList(folders);
		List<LSSheetOrderStructure> existinglist = new ArrayList<LSSheetOrderStructure>();
		if (lstfolders.size() > 0) {
			List<Long> lstfoldersid = lstfolders.stream().map(LSSheetOrderStructure::getDirectorycode)
					.collect(Collectors.toList());
			existinglist = lsSheetOrderStructureRepository.findByDirectorycodeIn(lstfoldersid);
			if (existinglist != null) {
				existinglist = existinglist.stream().map(folder -> {
					Long directorycode = folder.getDirectorycode();
					Optional<LSSheetOrderStructure> objfolder = lstfolders.stream()
							.filter(argfolder -> argfolder.getDirectorycode().equals(directorycode)).findFirst();
					if (objfolder != null) {
						folder.setParentdircode(objfolder.get().getParentdircode());
						folder.setPath(objfolder.get().getPath());
					}
					return folder;
				}).collect(Collectors.toList());
				lsSheetOrderStructureRepository.save(existinglist);
			}
		}
		return existinglist;
	}

	public Response validateprotocolexistonfolder(LSprotocolfolderfiles objfile) {
		Response response = new Response();
		long filecount = lsprotocolfolderfilesRepository.countByDirectorycodeAndFileforAndFilename(
				objfile.getDirectorycode(), objfile.getFilefor(), objfile.getFilename());

		if (filecount > 0) {
			response.setStatus(false);
		} else {
			response.setStatus(true);
		}
		return response;
	}

	public Map<String, Object> uploadfilesprotocolfolder(MultipartFile file, String uid, Long directorycode,
			String filefor, String tenantid, Integer ismultitenant, Integer usercode, Integer sitecode,
			Date createddate, Integer fileviewfor) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		LSprotocolfolderfiles objfile = new LSprotocolfolderfiles();
		objfile.setFilename(file.getOriginalFilename());
		objfile.setDirectorycode(directorycode);
		objfile.setFilefor(filefor);
		Response response = new Response();
//		Response response = validateprotocolexistonfolder(objfile);
//		if (response.getStatus()) {
		String uuID = "";
		if (ismultitenant == 1 || ismultitenant == 2) {
			uuID = cloudFileManipulationservice.storecloudfilesreturnwithpreUUID(file, "protocolfolderfiles", uid,
					ismultitenant);
		} else {
			uuID = fileManipulationservice.storeLargeattachmentwithpreuid(file.getOriginalFilename(), file, uid);
		}

		LSprotocolfolderfiles parentobjattachment = lsprotocolfolderfilesRepository
				.findFirst1ByDirectorycodeAndFilenameOrderByFolderfilecode(directorycode, file.getOriginalFilename());

		LSprotocolfolderfiles lsfiles = new LSprotocolfolderfiles();
		lsfiles.setUuid(uuID);
		lsfiles.setFilesize(file.getSize());
		lsfiles.setDirectorycode(directorycode);
		if (parentobjattachment != null && file.getOriginalFilename() != null
				&& file.getOriginalFilename().lastIndexOf(".") > -1) {
			Integer versiondata = parentobjattachment.getVersion() != null ? parentobjattachment.getVersion() + 1 : 1;
			String originalname = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
			String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."),
					file.getOriginalFilename().length());
			lsfiles.setFilename(originalname + "_V" + (versiondata) + extension);
			parentobjattachment.setVersion(versiondata);
			lsprotocolfolderfilesRepository.save(parentobjattachment);
		} else {
			lsfiles.setFilename(file.getOriginalFilename());
		}
		LSuserMaster lsuser = new LSuserMaster();
		lsuser.setUsercode(usercode);
		lsfiles.setCreateby(lsuser);
		LSSiteMaster lssite = new LSSiteMaster();
		lssite.setSitecode(sitecode);
		lsfiles.setLssitemaster(lssite);
		lsfiles.setFilefor(filefor);
		lsfiles.setCreatedtimestamp(createddate);
		lsfiles.setFileviewfor(fileviewfor);

		lsprotocolfolderfilesRepository.save(lsfiles);

//		} else {
//			response.setInformation("IDS_INFO_FILEEXIST");
//		}
		response.setStatus(true);
		map.put("res", response);
		map.put("uid", uid);
		map.put("name", lsfiles.getFilename());
		return map;
	}

	public List<LSprotocolfolderfiles> Getfilesforprotocolfolder(LSprotocolfolderfiles objfiles) {

		List<LSprotocolfolderfiles> lstfiles = new ArrayList<LSprotocolfolderfiles>();

		lstfiles = lsprotocolfolderfilesRepository
				.findByDirectorycodeAndFileforAndCreatedtimestampBetweenOrderByFolderfilecode(
						objfiles.getDirectorycode(), objfiles.getFilefor(), objfiles.getFromdate(),
						objfiles.getTodate());

		return lstfiles;
	}

	public ResponseEntity<InputStreamResource> downloadprotocolfileforfolder(Integer multitenant, String tenant,
			String fileid) throws IOException {
		HttpHeaders header = new HttpHeaders();
		header.set("Content-Disposition", "attachment; filename=" + fileid);

		if (multitenant == 1) {
			InputStream fileStream = cloudFileManipulationservice.retrieveCloudFile(fileid,
					tenant + "protocolfolderfiles");
			InputStreamResource resource = null;
			byte[] content = IOUtils.toByteArray(fileStream);
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
		} else {
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
	}

	public Map<String, Object> deleteprotocolfilesforfolder(String uid, Long directorycode, String filefor,
			String tenantid, Integer ismultitenant, Integer usercode, Integer sitecode, Date createddate,
			Integer fileviewfor) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("file", lsprotocolfolderfilesRepository.findByUuid(uid));
		lsprotocolfolderfilesRepository.deleteByUuid(uid);
		if (ismultitenant == 1 || ismultitenant == 2) {
			cloudFileManipulationservice.deletecloudFile(uid, "protocolfolderfiles");
		} else {
			fileManipulationservice.deletelargeattachments(uid);
		}
		map.put("uid", uid);
		return map;
	}

	public List<LSprotocolfolderfiles> UpdateprotocolFolderforfiles(LSprotocolfolderfiles[] files) {
		List<LSprotocolfolderfiles> lstfile = Arrays.asList(files);
		if (lstfile.size() > 0) {
			List<Integer> lstfilesid = lstfile.stream().map(LSprotocolfolderfiles::getFolderfilecode)
					.collect(Collectors.toList());
			lsprotocolfolderfilesRepository.updatedirectory(lstfile.get(0).getDirectorycode(), lstfilesid);
		}
		return lstfile;
	}

	public List<Lsprotocolorderstructure> Updateprotocolparentforfolder(Lsprotocolorderstructure[] folders) {
		List<Lsprotocolorderstructure> lstfolders = Arrays.asList(folders);
		List<Lsprotocolorderstructure> existinglist = new ArrayList<Lsprotocolorderstructure>();
		if (lstfolders.size() > 0) {
			List<Long> lstfoldersid = lstfolders.stream().map(Lsprotocolorderstructure::getDirectorycode)
					.collect(Collectors.toList());
			existinglist = lsprotocolorderStructurerepository.findByDirectorycodeIn(lstfoldersid);
			if (existinglist != null) {
				existinglist = existinglist.stream().map(folder -> {
					Optional<Lsprotocolorderstructure> objfolder = lstfolders.stream()
							.filter(argfolder -> argfolder.getDirectorycode() == folder.getDirectorycode()).findFirst();
					folder.setParentdircode(objfolder.get().getParentdircode());
					folder.setPath(objfolder.get().getPath());
					return folder;
				}).collect(Collectors.toList());
				lsprotocolorderStructurerepository.save(existinglist);
			}
		}
		return existinglist;
	}

	public LSprotocolfolderfiles UpdateprotocolFolderforfile(LSprotocolfolderfiles file) {
		lsprotocolfolderfilesRepository.updatedirectory(file.getDirectorycode(), file.getFolderfilecode());
		return file;
	}

	public List<LSprotocolfolderfiles> Getaddedprotocolfilesforfolder(List<String> lstuuid) {
		List<LSprotocolfolderfiles> lstfiles = new ArrayList<LSprotocolfolderfiles>();
		lstfiles = lsprotocolfolderfilesRepository.findByUuidInOrderByFolderfilecode(lstuuid);
		return lstfiles;
	}

	public LSlogilablimsorderdetail cancelprotocolorder(LSlogilablimsorderdetail body) {

		LSlogilablimsorderdetail obj = lslogilablimsorderdetailRepository.findByBatchid(body.getBatchid());
		obj.setOrdercancell(body.getOrdercancell());
		lslogilablimsorderdetailRepository.save(obj);
		try {
			updateNotificationForCancelOrder(body);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Number batchid = body.getBatchcode();
		lsActiveWidgetsRepository.updateRetirestatus(batchid);
		return body;
	}

	private void updateNotificationForCancelOrder(LSlogilablimsorderdetail objorder) throws ParseException {
		String Details = "";
		String Notifiction = "";

		int workflowcode = objorder.getLsworkflow() != null ? objorder.getLsworkflow().getWorkflowcode() : -1;
		String workflowname = objorder.getLsworkflow() != null ? objorder.getLsworkflow().getWorkflowname() : "";

		if (objorder != null && objorder.getLsprojectmaster() != null
				&& objorder.getLsprojectmaster().getLsusersteam() != null && objorder.getAssignedto() == null) {

			if (lsusersteamRepository.findByteamcode(objorder.getLsprojectmaster().getLsusersteam().getTeamcode())
					.getLsuserteammapping() != null
					&& lsusersteamRepository
							.findByteamcode(objorder.getLsprojectmaster().getLsusersteam().getTeamcode())
							.getLsuserteammapping().size() > 0) {

				Notifiction = "ORDERCANCEL";

				Details = "{\"ordercode\":\"" + objorder.getBatchcode() + "\", \"order\":\"" + objorder.getBatchid()
						+ "\", \"currentworkflow\":\"" + workflowname + "\", \"currentworkflowcode\":\"" + workflowcode
						+ "\", \"notifyFor\":\"" + 2 + "\", \"user\":\"" + objorder.getObjLoggeduser().getUsername()
						+ "\"}";

				List<LSuserteammapping> lstusers = lsusersteamRepository
						.findByteamcode(objorder.getLsprojectmaster().getLsusersteam().getTeamcode())
						.getLsuserteammapping();
				List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
				for (int i = 0; i < lstusers.size(); i++) {
					if (!(objorder.getObjLoggeduser().getUsercode()
							.equals(lstusers.get(i).getLsuserMaster().getUsercode()))
							&& !(objorder.getLsuserMaster().getUsercode()
									.equals(lstusers.get(i).getLsuserMaster().getUsercode()))) {
						LSnotification objnotify = new LSnotification();
						objnotify.setNotifationfrom(objorder.getObjLoggeduser());
						objnotify.setNotifationto(lstusers.get(i).getLsuserMaster());
						objnotify.setNotificationdate(commonfunction.getCurrentUtcTime());
						objnotify.setNotification(Notifiction);
						objnotify.setNotificationdetils(Details);
						objnotify.setIsnewnotification(1);
						objnotify.setNotificationpath("/registertask");
						objnotify.setNotificationfor(2);

						lstnotifications.add(objnotify);
					}
				}

				lsnotificationRepository.save(lstnotifications);
			}
		}

		Notifiction = "ORDERCANCEL";

		Details = "{\"ordercode\":\"" + objorder.getBatchcode() + "\", \"order\":\"" + objorder.getBatchid()
				+ "\", \"currentworkflow\":\"" + workflowname + "\", \"currentworkflowcode\":\"" + workflowcode
				+ "\", \"notifyFor\":\"" + 1 + "\", \"user\":\"" + objorder.getObjLoggeduser().getUsername() + "\"}";

//		LSuserMaster createby = lsuserMasterRepository.findByusercode(objorder.getLsuserMaster().getUsercode());
		LSnotification objnotify = new LSnotification();
		objnotify.setNotifationfrom(objorder.getObjLoggeduser());
		objnotify.setNotifationto(objorder.getLsuserMaster());
		objnotify.setNotificationdate(commonfunction.getCurrentUtcTime());
		objnotify.setNotification(Notifiction);
		objnotify.setNotificationdetils(Details);
		objnotify.setIsnewnotification(1);
		objnotify.setNotificationpath("/registertask");
		objnotify.setNotificationfor(1);

		lsnotificationRepository.save(objnotify);
	}

	public Map<String, Object> removemultifilessheetfolderonprotocol(LSprotocolfolderfiles[] files, Long directorycode,
			String filefor, String tenantid, Integer ismultitenant, Integer usercode, Integer sitecode,
			Date createddate, Integer fileviewfor) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<LSprotocolfolderfiles> lstfile = Arrays.asList(files);
		if (lstfile.size() > 0) {
			List<String> lstfilesid = lstfile.stream().map(LSprotocolfolderfiles::getUuid).collect(Collectors.toList());

			lsprotocolfolderfilesRepository.deleteByUuidIn(lstfilesid);

			for (String uuididex : lstfilesid) {

				if (ismultitenant == 1 || ismultitenant == 2 ) {
					cloudFileManipulationservice.deletecloudFile(uuididex, "protocolfolderfiles");
				} else {
					fileManipulationservice.deletelargeattachments(uuididex);
				}

			}

		}

		map.put("lstfilesid", lstfile);
		return map;
	}

	public Map<String, Object> Getfoldersfordashboard(LSuserMaster lsusermaster) {
		Map<String, Object> mapfolders = new HashMap<String, Object>();
		List<Long> immutableNegativeValues = Arrays.asList(-3L, -22L);
		List<Lsprotocolorderstructure> lstdirpro = new ArrayList<Lsprotocolorderstructure>();
		if (lsusermaster.getActiveusercode() != null && lsusermaster.getActiveusercode() == 2) {
			if (lsusermaster.getUsernotify() == null) {
				lstdirpro = lsprotocolorderStructurerepository
						.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
								lsusermaster.getLssitemaster(), 1, immutableNegativeValues, lsusermaster, 2,
								lsusermaster, 3);
			} else {
				lstdirpro = lsprotocolorderStructurerepository
						.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
								lsusermaster.getLssitemaster(), 1, immutableNegativeValues, lsusermaster, 2,
								lsusermaster.getLssitemaster(), 3, lsusermaster.getUsernotify());
			}
			lstdirpro.addAll(lsprotocolorderStructurerepository.findByDirectorycodeIn(immutableNegativeValues));
			mapfolders.put("directorypro", lstdirpro);
		} else if (lsusermaster.getActiveusercode() != null && lsusermaster.getActiveusercode() == 1) {
			List<LSSheetOrderStructure> lstdir = new ArrayList<LSSheetOrderStructure>();

			if (lsusermaster.getUsernotify() == null) {
				lstdir = lsSheetOrderStructureRepository
						.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
								lsusermaster.getLssitemaster(), 1, immutableNegativeValues, lsusermaster, 2,
								lsusermaster, 3);
			} else {
				lstdir = lsSheetOrderStructureRepository
						.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
								lsusermaster.getLssitemaster(), 1, immutableNegativeValues, lsusermaster, 2,
								lsusermaster.getLssitemaster(), 3, lsusermaster.getUsernotify());

			}
			lstdir.addAll(lsSheetOrderStructureRepository.findByDirectorycodeIn(immutableNegativeValues));
			mapfolders.put("directory", lstdir);
		} else {
			List<LSSheetOrderStructure> lstdir = new ArrayList<LSSheetOrderStructure>();

			if (lsusermaster.getUsernotify() == null) {
				lstdir = lsSheetOrderStructureRepository
						.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
								lsusermaster.getLssitemaster(), 1, immutableNegativeValues, lsusermaster, 2,
								lsusermaster, 3);
			} else {
				lstdir = lsSheetOrderStructureRepository
						.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
								lsusermaster.getLssitemaster(), 1, immutableNegativeValues, lsusermaster, 2,
								lsusermaster.getLssitemaster(), 3, lsusermaster.getUsernotify());

			}
			lstdir.addAll(lsSheetOrderStructureRepository.findByDirectorycodeIn(immutableNegativeValues));
			if (lsusermaster.getUsernotify() == null) {
				lstdirpro = lsprotocolorderStructurerepository
						.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
								lsusermaster.getLssitemaster(), 1, immutableNegativeValues, lsusermaster, 2,
								lsusermaster, 3);
			} else {
				lstdirpro = lsprotocolorderStructurerepository
						.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
								lsusermaster.getLssitemaster(), 1, immutableNegativeValues, lsusermaster, 2,
								lsusermaster.getLssitemaster(), 3, lsusermaster.getUsernotify());
			}
			lstdirpro.addAll(lsprotocolorderStructurerepository.findByDirectorycodeIn(immutableNegativeValues));
			mapfolders.put("directorypro", lstdirpro);
			mapfolders.put("directory", lstdir);

		}
		if (lsusermaster.getLstproject() != null && lsusermaster.getLstproject().size() > 0) {
			ArrayList<List<Object>> lsttest = new ArrayList<List<Object>>();
			List<Integer> lsprojectcode = lsusermaster.getLstproject().stream().map(LSprojectmaster::getProjectcode)
					.collect(Collectors.toList());
			List<LSprojectmaster> lstproject = lsprojectmasterRepository.findAll(lsprojectcode);
			lsttest = lslogilablimsorderdetailRepository
					.getLstestmasterlocalByOrderdisplaytypeAndLsprojectmasterInAndTestcodeIsNotNull(lsprojectcode);

			mapfolders.put("tests", lsttest);
			mapfolders.put("projects", lstproject);
		} else {
			mapfolders.put("tests", new ArrayList<Logilaborders>());
			mapfolders.put("projects", new ArrayList<Projectmaster>());
		}
		List<Elnmaterial> Matireal_List = elnmaterialRepository
				.findByNsitecodeOrderByNmaterialcodeDesc(lsusermaster.getLssitemaster().getSitecode());
		List<Object> Material_List_Ordes = LSlogilabprotocoldetailRepository
				.getLstestmasterlocalAndmaterialByOrderdisplaytypeAndLSsamplemasterInAndTestcodeIsNotNull(
						lsusermaster.getLssitemaster().getSitecode());
		mapfolders.put("Materialtest", Material_List_Ordes);
		mapfolders.put("Material", Matireal_List);

		return mapfolders;
	}

	public void onDeleteforCancel(List<String> lstuuid, String screen) {
		if (!lstuuid.isEmpty()) {
			if (screen.equals("sheet")) {
				lssheetfolderfilesRepository.removeForFile(lstuuid);
			} else if (screen.equals("protocol")) {
				lsprotocolfolderfilesRepository.removeForFile(lstuuid);
			}
		}

	}

	public Map<String, Object> getimagesforlink(Map<String, Object> obj) {
		Map<String, Object> rtnobj = new HashMap<String, Object>();
		Integer onTabKey = (Integer) obj.get("ontabkey");
		long directorycode = ((Number) obj.get("directorycode")).longValue();
		Integer uploadtype = (Integer) obj.get("uploadtype");
		if (uploadtype == 1) {
			String jpql = onTabKey == 1
					? "SELECT f FROM LSsheetfolderfiles f WHERE (f.directorycode=:directorycode) And (f.filename LIKE LOWER(:png) OR f.filename LIKE :jpg OR f.filename LIKE :jpeg)"
					: "SELECT f FROM LSprotocolfolderfiles f WHERE (f.directorycode=:directorycode) AND (f.filename LIKE LOWER(:png) OR f.filename LIKE :jpg OR f.filename LIKE :jpeg)";
			TypedQuery<LSsheetfolderfiles> query1;
			TypedQuery<LSprotocolfolderfiles> query2;
			if (onTabKey == 1) {
				query1 = entityManager.createQuery(jpql, LSsheetfolderfiles.class);
				query1.setParameter("png", "%.png%");
				query1.setParameter("jpg", "%.jpg%");
				query1.setParameter("jpeg", "%.jpeg%");
				query1.setParameter("directorycode", directorycode);
				rtnobj.put("LSsheetfolderfiles", query1.getResultList());
			} else {
				query2 = entityManager.createQuery(jpql, LSprotocolfolderfiles.class);
				query2.setParameter("png", "%.png%");
				query2.setParameter("jpg", "%.jpg%");
				query2.setParameter("jpeg", "%.jpeg%");
				query2.setParameter("directorycode", directorycode);
				rtnobj.put("LSprotocolfolderfiles", query2.getResultList());
			}
		} else {
			String filefor = (String) obj.get("filefor");
			if (onTabKey == 1) {
				List<LSsheetfolderfiles> lstfiles = new ArrayList<LSsheetfolderfiles>();
				lstfiles = lssheetfolderfilesRepository.findByDirectorycodeAndFilefor(directorycode, filefor);
				rtnobj.put("LSsheetfolderfiles", lstfiles);
			} else {
				List<LSprotocolfolderfiles> lstfiles = new ArrayList<LSprotocolfolderfiles>();
				lstfiles = lsprotocolfolderfilesRepository.findByDirectorycodeAndFilefor(directorycode, filefor);
				rtnobj.put("LSprotocolfolderfiles", lstfiles);
			}
		}
		return rtnobj;
	}

	public List<Logilaborders> Getordersonmaterial(LSlogilablimsorderdetail objorder) {
		List<Logilaborders> lstorder = new ArrayList<Logilaborders>();

		Date fromdate = objorder.getFromdate();
		Date todate = objorder.getTodate();
		Integer filetype = objorder.getFiletype();
		List<LSprojectmaster> lstproject = objorder.getLstproject();
		if (filetype == 0) {
			return new ArrayList<Logilaborders>();
		} else if (filetype == -1 && objorder.getOrderflag() == null) {
//			lstorder = lslogilablimsorderdetailRepository
//					.findByElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//							objorder.getElnmaterial(), objorder.getLstestmasterlocal(), fromdate, todate);
			lstorder = lslogilablimsorderdetailRepository
					.findByLsprojectmasterInAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterInOrderByBatchcodeDesc(
							lstproject, objorder.getElnmaterial(), objorder.getLstestmasterlocal(), fromdate, todate,
							objorder.getElnmaterial(), objorder.getLstestmasterlocal(), fromdate, todate, 1,
							objorder.getElnmaterial(), objorder.getLstestmasterlocal(), fromdate, todate, 2,
							objorder.getLsuserMaster(), objorder.getElnmaterial(), objorder.getLstestmasterlocal(),
							fromdate, todate, 3, objorder.getLstuserMaster());
		} else if (filetype != -1 && objorder.getOrderflag() != null) {
			if (objorder.getApprovelstatus() != null) {
//				lstorder = lslogilablimsorderdetailRepository
//						.findByElnmaterialAndLstestmasterlocalAndFiletypeAndOrderflagAndApprovelstatusAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//								objorder.getElnmaterial(), objorder.getLstestmasterlocal(), filetype,
//								objorder.getOrderflag(), objorder.getApprovelstatus(), fromdate, todate);
				lstorder = lslogilablimsorderdetailRepository
						.findByLsprojectmasterInAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndFiletypeAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndFiletypeAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndFiletypeAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterInAndFiletypeAndOrderflagAndApprovelstatusOrderByBatchcodeDesc(
								lstproject, objorder.getElnmaterial(), objorder.getLstestmasterlocal(), fromdate,
								todate, filetype, objorder.getOrderflag(), objorder.getApprovelstatus(),
								objorder.getElnmaterial(), objorder.getLstestmasterlocal(), fromdate, todate, 1,
								filetype, objorder.getOrderflag(), objorder.getApprovelstatus(),
								objorder.getElnmaterial(), objorder.getLstestmasterlocal(), fromdate, todate, 2,
								objorder.getLsuserMaster(), filetype, objorder.getOrderflag(),
								objorder.getApprovelstatus(), objorder.getElnmaterial(),
								objorder.getLstestmasterlocal(), fromdate, todate, 3, objorder.getLstuserMaster(),
								filetype, objorder.getOrderflag(), objorder.getApprovelstatus());
			} else {
//				lstorder = lslogilablimsorderdetailRepository
//						.findByElnmaterialAndLstestmasterlocalAndFiletypeAndOrderflagAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//								objorder.getElnmaterial(), objorder.getLstestmasterlocal(), filetype,
//								objorder.getOrderflag(), fromdate, todate);

				lstorder = lslogilablimsorderdetailRepository
						.findByLsprojectmasterInAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndFiletypeAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndFiletypeAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndFiletypeAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterInAndFiletypeAndOrderflagOrderByBatchcodeDesc(
								lstproject, objorder.getElnmaterial(), objorder.getLstestmasterlocal(), fromdate,
								todate, filetype, objorder.getOrderflag(), objorder.getElnmaterial(),
								objorder.getLstestmasterlocal(), fromdate, todate, 1, filetype, objorder.getOrderflag(),
								objorder.getElnmaterial(), objorder.getLstestmasterlocal(), fromdate, todate, 2,
								objorder.getLsuserMaster(), filetype, objorder.getOrderflag(),
								objorder.getElnmaterial(), objorder.getLstestmasterlocal(), fromdate, todate, 3,
								objorder.getLstuserMaster(), filetype, objorder.getOrderflag());
			}

		} else if (filetype == -1 && objorder.getOrderflag() != null) {
			if (objorder.getApprovelstatus() != null) {

//				lstorder = lslogilablimsorderdetailRepository
//						.findByElnmaterialAndLstestmasterlocalAndOrderflagAndApprovelstatusAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//								objorder.getElnmaterial(), objorder.getLstestmasterlocal(), objorder.getOrderflag(),
//								objorder.getApprovelstatus(), fromdate, todate);

				lstorder = lslogilablimsorderdetailRepository
						.findByLsprojectmasterInAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterInAndOrderflagAndApprovelstatusOrderByBatchcodeDesc(
								lstproject, objorder.getElnmaterial(), objorder.getLstestmasterlocal(), fromdate,
								todate, objorder.getOrderflag(), objorder.getApprovelstatus(),
								objorder.getElnmaterial(), objorder.getLstestmasterlocal(), fromdate, todate, 1,
								objorder.getOrderflag(), objorder.getApprovelstatus(), objorder.getElnmaterial(),
								objorder.getLstestmasterlocal(), fromdate, todate, 2, objorder.getLsuserMaster(),
								objorder.getOrderflag(), objorder.getApprovelstatus(), objorder.getElnmaterial(),
								objorder.getLstestmasterlocal(), fromdate, todate, 3, objorder.getLstuserMaster(),
								objorder.getOrderflag(), objorder.getApprovelstatus());
			} else {
//				lstorder = lslogilablimsorderdetailRepository
//						.findByElnmaterialAndLstestmasterlocalAndOrderflagAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//								objorder.getElnmaterial(), objorder.getLstestmasterlocal(), objorder.getOrderflag(),
//								fromdate, todate);
				lstorder = lslogilablimsorderdetailRepository
						.findByLsprojectmasterInAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterInAndOrderflagOrderByBatchcodeDesc(
								lstproject, objorder.getElnmaterial(), objorder.getLstestmasterlocal(), fromdate,
								todate, objorder.getOrderflag(), objorder.getElnmaterial(),
								objorder.getLstestmasterlocal(), fromdate, todate, 1, objorder.getOrderflag(),
								objorder.getElnmaterial(), objorder.getLstestmasterlocal(), fromdate, todate, 2,
								objorder.getLsuserMaster(), objorder.getOrderflag(), objorder.getElnmaterial(),
								objorder.getLstestmasterlocal(), fromdate, todate, 3, objorder.getLstuserMaster(),
								objorder.getOrderflag());
			}
		} else if (filetype != -1 && objorder.getOrderflag() == null) {
//			lstorder = lslogilablimsorderdetailRepository
//					.findByElnmaterialAndLstestmasterlocalAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//							objorder.getElnmaterial(), objorder.getLstestmasterlocal(), filetype, fromdate, todate);

			lstorder = lslogilablimsorderdetailRepository
					.findByLsprojectmasterInAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndFiletypeOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndFiletypeOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndFiletypeOrLsprojectmasterIsNullAndElnmaterialAndLstestmasterlocalAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterInAndFiletypeOrderByBatchcodeDesc(
							lstproject, objorder.getElnmaterial(), objorder.getLstestmasterlocal(), fromdate, todate,
							filetype, objorder.getElnmaterial(), objorder.getLstestmasterlocal(), fromdate, todate, 1,
							filetype, objorder.getElnmaterial(), objorder.getLstestmasterlocal(), fromdate, todate, 2,
							objorder.getLsuserMaster(), filetype, objorder.getElnmaterial(),
							objorder.getLstestmasterlocal(), fromdate, todate, 3, objorder.getLstuserMaster(),
							filetype);
		}
		if (objorder.getSearchCriteria() != null && objorder.getSearchCriteria().getContentsearchtype() != null
				&& objorder.getSearchCriteria().getContentsearch() != null) {

			lstorder = GetsampleordersonFilter(objorder, lstorder);

			lstorder.forEach(objorderDetail -> objorderDetail.setLstworkflow(objorder.getLstworkflow()));
			return lstorder;
		} else {

			lstorder.forEach(objorderDetail -> objorderDetail.setCanuserprocess(true));
			return lstorder;
		}

	}

	public Map<String, Object> Getprotocolordersonmaterial(LSlogilabprotocoldetail objorder) {
		List<Logilabprotocolorders> lstorder = new ArrayList<Logilabprotocolorders>();
		Map<String, Object> retuobjts = new HashMap<>();
		Date fromdate = objorder.getFromdate();
		Date todate = objorder.getTodate();
		Integer protocoltype = objorder.getProtocoltype();
		List<LSprojectmaster> lstproject = objorder.getLstproject();
		List<Integer> userlist = objorder.getLstuserMaster() != null
				? objorder.getLstuserMaster().stream().map(LSuserMaster::getUsercode).collect(Collectors.toList())
				: new ArrayList<Integer>();
		if (protocoltype == -1 && objorder.getOrderflag() == null) {

//			lstorder = LSlogilabprotocoldetailRepository.findByElnmaterialAndTestcodeAndCreatedtimestampBetween(
//					objorder.getElnmaterial(), objorder.getTestcode(), fromdate, todate);

			lstorder = LSlogilabprotocoldetailRepository
					.findByLsprojectmasterInAndElnmaterialAndTestcodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndCreatebyIn(
							lstproject, objorder.getElnmaterial(), objorder.getTestcode(), fromdate, todate,
							objorder.getElnmaterial(), objorder.getTestcode(), fromdate, todate, 1,
							objorder.getElnmaterial(), objorder.getTestcode(), fromdate, todate, 2,
							objorder.getLsuserMaster(), objorder.getElnmaterial(), objorder.getTestcode(), fromdate,
							todate, 3, userlist);

		} else if (protocoltype != -1 && objorder.getOrderflag() != null) {
			if (objorder.getRejected() != null) {

//				lstorder = LSlogilabprotocoldetailRepository
//						.findByElnmaterialAndTestcodeAndProtocoltypeAndOrderflagAndRejectedAndCreatedtimestampBetween(
//								objorder.getElnmaterial(), objorder.getTestcode(), protocoltype,
//								objorder.getOrderflag(), 1, fromdate, todate);

				lstorder = LSlogilabprotocoldetailRepository
						.findByLsprojectmasterInAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndProtocoltypeAndOrderflagAndRejectedOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndProtocoltypeAndOrderflagAndRejectedOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndProtocoltypeAndOrderflagAndRejectedOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndCreatebyInAndProtocoltypeAndOrderflagAndRejected(
								lstproject, objorder.getElnmaterial(), objorder.getTestcode(), fromdate, todate,
								protocoltype, objorder.getOrderflag(), 1, objorder.getElnmaterial(),
								objorder.getTestcode(), fromdate, todate, 1, protocoltype, objorder.getOrderflag(), 1,
								objorder.getElnmaterial(), objorder.getTestcode(), fromdate, todate, 2, protocoltype,
								objorder.getOrderflag(), 1, objorder.getLsuserMaster(), objorder.getElnmaterial(),
								objorder.getTestcode(), fromdate, todate, 3, userlist, protocoltype,
								objorder.getOrderflag(), 1);

			} else {

//				lstorder = LSlogilabprotocoldetailRepository
//						.findByElnmaterialAndTestcodeAndProtocoltypeAndOrderflagAndCreatedtimestampBetween(
//								objorder.getElnmaterial(), objorder.getTestcode(), protocoltype,
//								objorder.getOrderflag(), fromdate, todate);

				lstorder = LSlogilabprotocoldetailRepository
						.findByLsprojectmasterInAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndProtocoltypeAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndProtocoltypeAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndProtocoltypeAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndCreatebyInAndProtocoltypeAndOrderflag(
								lstproject, objorder.getElnmaterial(), objorder.getTestcode(), fromdate, todate,
								protocoltype, objorder.getOrderflag(), objorder.getElnmaterial(),
								objorder.getTestcode(), fromdate, todate, 1, protocoltype, objorder.getOrderflag(),
								objorder.getElnmaterial(), objorder.getTestcode(), fromdate, todate, 2, protocoltype,
								objorder.getOrderflag(), objorder.getLsuserMaster(), objorder.getElnmaterial(),
								objorder.getTestcode(), fromdate, todate, 3, userlist, protocoltype,
								objorder.getOrderflag());

			}

		} else if (protocoltype == -1 && objorder.getOrderflag() != null) {
			if (objorder.getRejected() != null) {

//				lstorder = LSlogilabprotocoldetailRepository
//						.findByElnmaterialAndTestcodeAndOrderflagAndRejectedAndCreatedtimestampBetween(
//								objorder.getElnmaterial(), objorder.getTestcode(), objorder.getOrderflag(), 1, fromdate,
//								todate);

				lstorder = LSlogilabprotocoldetailRepository
						.findByLsprojectmasterInAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndOrderflagAndRejectedOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndOrderflagAndRejectedOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndOrderflagAndRejectedOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndCreatebyInAndOrderflagAndRejected(
								lstproject, objorder.getElnmaterial(), objorder.getTestcode(), fromdate, todate,
								objorder.getOrderflag(), 1, objorder.getElnmaterial(), objorder.getTestcode(), fromdate,
								todate, 1, objorder.getOrderflag(), 1, objorder.getElnmaterial(),
								objorder.getTestcode(), fromdate, todate, 2, objorder.getOrderflag(), 1,
								objorder.getLsuserMaster(), objorder.getElnmaterial(), objorder.getTestcode(), fromdate,
								todate, 3, userlist, objorder.getOrderflag(), 1);

			} else {
//				lstorder = LSlogilabprotocoldetailRepository
//						.findByElnmaterialAndTestcodeAndOrderflagAndCreatedtimestampBetween(objorder.getElnmaterial(),
//								objorder.getTestcode(), objorder.getOrderflag(), fromdate, todate);

				lstorder = LSlogilabprotocoldetailRepository
						.findByLsprojectmasterInAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndOrderflagOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndCreatebyInAndOrderflag(
								lstproject, objorder.getElnmaterial(), objorder.getTestcode(), fromdate, todate,
								objorder.getOrderflag(), objorder.getElnmaterial(), objorder.getTestcode(), fromdate,
								todate, 1, objorder.getOrderflag(), objorder.getElnmaterial(), objorder.getTestcode(),
								fromdate, todate, 2, objorder.getOrderflag(), objorder.getLsuserMaster(),
								objorder.getElnmaterial(), objorder.getTestcode(), fromdate, todate, 3, userlist,
								objorder.getOrderflag());

			}
		} else if (protocoltype != -1 && objorder.getOrderflag() == null) {
//			lstorder = LSlogilabprotocoldetailRepository
//					.findByElnmaterialAndTestcodeAndProtocoltypeAndCreatedtimestampBetween(objorder.getElnmaterial(),
//							objorder.getTestcode(), protocoltype, fromdate, todate);
			lstorder = LSlogilabprotocoldetailRepository
					.findByLsprojectmasterInAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndProtocoltypeOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndProtocoltypeOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndLsuserMasterAndProtocoltypeOrLsprojectmasterIsNullAndElnmaterialAndTestcodeAndCreatedtimestampBetweenAndViewoptionAndCreatebyInAndProtocoltype

					(lstproject, objorder.getElnmaterial(), objorder.getTestcode(), fromdate, todate, protocoltype,
							objorder.getElnmaterial(), objorder.getTestcode(), fromdate, todate, 1, protocoltype,
							objorder.getElnmaterial(), objorder.getTestcode(), fromdate, todate, 2, protocoltype,
							objorder.getLsuserMaster(), objorder.getElnmaterial(), objorder.getTestcode(), fromdate,
							todate, 3, userlist, protocoltype);

		}
		Map<String, Object> obj = Getprotocolordersondirectory(objorder.getLsprotocolorderstructure());
		if (obj.containsKey("protocolorders")) {
			@SuppressWarnings("unchecked")
			List<LSlogilabprotocoldetail> protocolOrders = (List<LSlogilabprotocoldetail>) obj.get("protocolorders");
			lstorder.addAll(protocolOrders.stream().map(lsOrderDetail -> new Logilabprotocolorders(
					lsOrderDetail.getProtocolordercode(), lsOrderDetail.getTeamcode(),
					lsOrderDetail.getProtoclordername(), lsOrderDetail.getOrderflag(), lsOrderDetail.getProtocoltype(),
					lsOrderDetail.getCreatedtimestamp(), lsOrderDetail.getCompletedtimestamp(),
					lsOrderDetail.getLsprotocolmaster(), lsOrderDetail.getlSprotocolworkflow(),
					lsOrderDetail.getLssamplemaster(), lsOrderDetail.getLsprojectmaster(), lsOrderDetail.getKeyword(),
					lsOrderDetail.getDirectorycode(), lsOrderDetail.getCreateby(), lsOrderDetail.getAssignedto(),
					lsOrderDetail.getLsrepositoriesdata(), lsOrderDetail.getLsrepositories(),
					lsOrderDetail.getElnmaterial(), lsOrderDetail.getElnmaterialinventory(),
					lsOrderDetail.getApproved(), lsOrderDetail.getRejected(), lsOrderDetail.getOrdercancell(),
					lsOrderDetail.getViewoption(), lsOrderDetail.getOrderstarted(), lsOrderDetail.getOrderstartedby(),
					lsOrderDetail.getOrderstartedon(), lsOrderDetail.getLockeduser(), lsOrderDetail.getLockedusername(),
					lsOrderDetail.getVersionno(), lsOrderDetail.getElnprotocolworkflow(),
					lsOrderDetail.getLsordernotification(), lsOrderDetail.getLsautoregister(),
					lsOrderDetail.getRepeat(), lsOrderDetail.getSentforapprovel(), lsOrderDetail.getApprovelaccept(),
					lsOrderDetail.getAutoregistercount(), lsOrderDetail.getLsuserMaster()))
					.collect(Collectors.toList()));

		}

		lstorder.forEach(
				objorderDetail -> objorderDetail.setLstelnprotocolworkflow(objorder.getLstelnprotocolworkflow()));
		List<Long> protocolordercode = new ArrayList<>();
		if (lstorder.size() > 0 && objorder.getSearchCriteriaType() != null) {
			protocolordercode = lstorder.stream().map(Logilabprotocolorders::getProtocolordercode)
					.collect(Collectors.toList());
			retuobjts.put("protocolordercodeslist", protocolordercode);
		}
		retuobjts.put("protocolorders", lstorder);

		return retuobjts;
	}

	public List<Logilaborders> Getcancelledordes(LSlogilablimsorderdetail objdir) {
		List<Logilaborders> lstorders = new ArrayList<Logilaborders>();
//		List<Logilaborders> lstorderstrcarray = new ArrayList<Logilaborders>();
		Date fromdate = objdir.getFromdate();
		Date todate = objdir.getTodate();
		Integer filetype = objdir.getFiletype();
		String orderflag = objdir.getOrderflag();
		Integer rejected = objdir.getRejected();
		List<LSprojectmaster> lstproject = objdir.getLstproject();

		if (filetype == -1 && orderflag == null && rejected == null) {
			lstorders = lslogilablimsorderdetailRepository
					.findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellOrAssignedtoAndCreatedtimestampBetweenAndOrdercancell(
							1, lstproject, fromdate, todate, 1, objdir.getLsuserMaster(), fromdate, todate, 1, 2,
							objdir.getLsuserMaster(), fromdate, todate, 1, 3, objdir.getLsuserMaster(), fromdate,
							todate, 1, objdir.getLsuserMaster(), objdir.getLsuserMaster(), fromdate, todate, 1,
							objdir.getLsuserMaster(), fromdate, todate, 1);

		} else if (filetype != -1 && orderflag == null && rejected == null) {
			lstorders = lslogilablimsorderdetailRepository
					.findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndFiletypeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndFiletypeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndFiletypeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullAndFiletypeOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndFiletypeOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndFiletype(
							1, lstproject, fromdate, todate, filetype, 1, objdir.getLsuserMaster(), fromdate, todate, 1,
							filetype, 2, objdir.getLsuserMaster(), fromdate, todate, 1, filetype, 3,
							objdir.getLsuserMaster(), fromdate, todate, 1, filetype, objdir.getLsuserMaster(),
							objdir.getLsuserMaster(), fromdate, todate, 1, filetype, objdir.getLsuserMaster(), fromdate,
							todate, 1, filetype);
		} else if (filetype != -1 && orderflag != null && rejected == null) {
			lstorders = lslogilablimsorderdetailRepository
					.findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndFiletypeAndOrderflagOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndFiletypeAndOrderflagOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndFiletypeAndOrderflagOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullAndFiletypeAndOrderflagOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndFiletypeAndOrderflagOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndFiletypeAndOrderflag(
							1, lstproject, fromdate, todate, filetype, orderflag, 1, objdir.getLsuserMaster(), fromdate,
							todate, 1, filetype, orderflag, 2, objdir.getLsuserMaster(), fromdate, todate, 1, filetype,
							orderflag, 3, objdir.getLsuserMaster(), fromdate, todate, 1, filetype, orderflag,
							objdir.getLsuserMaster(), objdir.getLsuserMaster(), fromdate, todate, 1, filetype,
							orderflag, objdir.getLsuserMaster(), fromdate, todate, 1, filetype, orderflag);
		} else if (filetype != -1 && orderflag == null && rejected != null) {
			lstorders = lslogilablimsorderdetailRepository
					.findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndFiletypeAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndFiletypeAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndFiletypeAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullAndFiletypeAndApprovelstatusOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndFiletypeAndApprovelstatusOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndFiletypeAndApprovelstatus(
							1, lstproject, fromdate, todate, filetype, 3, 1, objdir.getLsuserMaster(), fromdate, todate,
							1, filetype, 3, 2, objdir.getLsuserMaster(), fromdate, todate, 1, filetype, 3, 3,
							objdir.getLsuserMaster(), fromdate, todate, 1, filetype, 3, objdir.getLsuserMaster(),
							objdir.getLsuserMaster(), fromdate, todate, 1, filetype, 3, objdir.getLsuserMaster(),
							fromdate, todate, 1, filetype, 3);
		} else if (filetype == -1 && orderflag != null && rejected == null) {
			lstorders = lslogilablimsorderdetailRepository
					.findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndOrderflagOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndOrderflagOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndOrderflagOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullAndOrderflagOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndOrderflagOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndOrderflag(
							1, lstproject, fromdate, todate, orderflag, 1, objdir.getLsuserMaster(), fromdate, todate,
							1, orderflag, 2, objdir.getLsuserMaster(), fromdate, todate, 1, orderflag, 3,
							objdir.getLsuserMaster(), fromdate, todate, 1, orderflag, objdir.getLsuserMaster(),
							objdir.getLsuserMaster(), fromdate, todate, 1, orderflag, objdir.getLsuserMaster(),
							fromdate, todate, 1, orderflag);
		} else if (filetype == -1 && orderflag != null && rejected != null) {
			lstorders = lslogilablimsorderdetailRepository
					.findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullAndOrderflagAndApprovelstatusOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndOrderflagAndApprovelstatusOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndOrderflagAndApprovelstatus(
							1, lstproject, fromdate, todate, orderflag, 3, 1, objdir.getLsuserMaster(), fromdate,
							todate, 1, orderflag, 3, 2, objdir.getLsuserMaster(), fromdate, todate, 1, orderflag, 3, 3,
							objdir.getLsuserMaster(), fromdate, todate, 1, orderflag, 3, objdir.getLsuserMaster(),
							objdir.getLsuserMaster(), fromdate, todate, 1, orderflag, 3, objdir.getLsuserMaster(),
							fromdate, todate, 1, orderflag, 3);
		} else if (filetype == -1 && orderflag == null && rejected != null) {
			lstorders = lslogilablimsorderdetailRepository
					.findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullAndApprovelstatusOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndApprovelstatusOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndApprovelstatus(
							1, lstproject, fromdate, todate, 3, 1, objdir.getLsuserMaster(), fromdate, todate, 1, 3, 2,
							objdir.getLsuserMaster(), fromdate, todate, 1, 3, 3, objdir.getLsuserMaster(), fromdate,
							todate, 1, 3, objdir.getLsuserMaster(), objdir.getLsuserMaster(), fromdate, todate, 1, 3,
							objdir.getLsuserMaster(), fromdate, todate, 1, 3);
		} else {
			lstorders = lslogilablimsorderdetailRepository
					.findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndFiletypeAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndFiletypeAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndFiletypeAndOrderflagAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullAndFiletypeAndOrderflagAndApprovelstatusOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndFiletypeAndOrderflagAndApprovelstatusOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndFiletypeAndOrderflagAndApprovelstatus(
							1, lstproject, fromdate, todate, filetype, orderflag, 3, 1, objdir.getLsuserMaster(),
							fromdate, todate, 1, filetype, orderflag, 3, 2, objdir.getLsuserMaster(), fromdate, todate,
							1, filetype, orderflag, 3, 3, objdir.getLsuserMaster(), fromdate, todate, 1, filetype,
							orderflag, 3, objdir.getLsuserMaster(), objdir.getLsuserMaster(), fromdate, todate, 1,
							filetype, orderflag, 3, objdir.getLsuserMaster(), fromdate, todate, 1, filetype, orderflag,
							3);
		}
		if (objdir.getSearchCriteria() != null && objdir.getSearchCriteria().getContentsearchtype() != null
				&& objdir.getSearchCriteria().getContentsearch() != null) {

			lstorders = GetmyordersonFilter(objdir, lstorders, orderflag);

			lstorders.forEach(objorderDetail -> objorderDetail.setLstworkflow(objdir.getLstworkflow()));
			return lstorders;
		} else {

			lstorders.forEach(objorderDetail -> objorderDetail.setLstworkflow(objdir.getLstworkflow()));
			return lstorders;
		}
	}

	public LSlogilablimsorderdetail sendapprovel(LSlogilablimsorderdetail objdir) {
		List<LSlogilablimsorderdetail> logiobj = new ArrayList<LSlogilablimsorderdetail>();
		logiobj = logilablimsorderdetailsRepository.getOrderDetails(objdir.getBatchcode());
		logiobj.get(0).setSentforapprovel(objdir.getSentforapprovel());
		logiobj.get(0).setApprovelaccept(objdir.getApprovelaccept());
		logiobj.get(0).setCanuserprocess(false);
		logilablimsorderdetailsRepository.save(logiobj.get(0));

		String screen = "Sheet Order";
		String Notification = "SENDFORAPPROVEL";

		LSuserMaster notifyfrom = logiobj.get(0).getLsuserMaster();
		LSuserMaster notifyto = logiobj.get(0).getAssignedto();
		try {
			sendnotification(logiobj.get(0), Notification, screen, notifyto, notifyfrom);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return logiobj.get(0);
	}

	public LSlogilablimsorderdetail acceptapprovel(LSlogilablimsorderdetail objdir) throws ParseException {
		List<LSlogilablimsorderdetail> logiobj = new ArrayList<LSlogilablimsorderdetail>();
		logiobj = lslogilablimsorderdetailRepository.findByBatchcodeAndBatchid(objdir.getBatchcode(),
				objdir.getBatchid());
		String screen = "Sheet Order";

		if (objdir.getApprovelaccept().equals("3")) {
			logiobj.get(0).setApprovelaccept(objdir.getApprovelaccept());
			logiobj.get(0).setApprovelstatus(objdir.getApprovelstatus());
			logiobj.get(0).setOrderflag("R");
			logiobj.get(0).setDirectorycode(null);
			logiobj.get(0).setCompletedtimestamp(commonfunction.getCurrentUtcTime());

			String Notification = "REJECTALERT";

			// LSuserMaster notifyfrom = logiobj.get(0).getLsuserMaster();
			// LSuserMaster notifyto = logiobj.get(0).getAssignedto();

			LSuserMaster notifyfrom = logiobj.get(0).getAssignedto();
			LSuserMaster notifyto = logiobj.get(0).getLsuserMaster();

			sendnotification(logiobj.get(0), Notification, screen, notifyto, notifyfrom);

		} else if (objdir.getApprovelaccept().equals("2")) {
			logiobj.get(0).setApprovelaccept(objdir.getApprovelaccept());
			// logiobj.get(0).setCanuserprocess(true);
			logiobj.get(0).setSentforapprovel(objdir.getSentforapprovel());

			String Notification = "RETURNALERT";

//			LSuserMaster notifyfrom = logiobj.get(0).getLsuserMaster();
//			LSuserMaster notifyto = logiobj.get(0).getAssignedto();

			LSuserMaster notifyfrom = logiobj.get(0).getAssignedto();
			LSuserMaster notifyto = logiobj.get(0).getLsuserMaster();

			sendnotification(logiobj.get(0), Notification, screen, notifyto, notifyfrom);
			// logiobj.get(0).setSentforapprovel(objdir.getSentforapprovel());
		} else {

			logiobj.get(0).setApprovelaccept(objdir.getApprovelaccept());

			String Notification = "APPROVEALERT";
//			LSuserMaster notifyfrom = logiobj.get(0).getLsuserMaster();
//			LSuserMaster notifyto = logiobj.get(0).getAssignedto();
			LSuserMaster notifyfrom = logiobj.get(0).getAssignedto();
			LSuserMaster notifyto = logiobj.get(0).getLsuserMaster();
			sendnotification(logiobj.get(0), Notification, screen, notifyto, notifyfrom);

		}
		lslogilablimsorderdetailRepository.save(logiobj);

		return logiobj.get(0);
	}

	public void sendnotification(LSlogilablimsorderdetail objdir, String Notification, String screen,
			LSuserMaster notifyfrom, LSuserMaster notifyto) throws ParseException {

		LSnotification LSnotification = new LSnotification();

		String Details = "{\"ordercode\" :\"" + objdir.getBatchcode() + "\",\"order\" :\"" + objdir.getBatchid()
				+ "\",\"user\":\"" + objdir.getLsuserMaster().getUsername() + "\",\"notifyto\":\""
				+ objdir.getAssignedto().getUsername() + "\"}";

		LSnotification.setIsnewnotification(1);
		LSnotification.setNotification(Notification);
		LSnotification.setNotificationdate(commonfunction.getCurrentUtcTime());
		LSnotification.setNotificationdetils(Details);
		LSnotification.setNotificationpath(screen.equals("Sheet Order") ? "/registertask" : "/Protocolorder");
		LSnotification.setNotifationfrom(notifyfrom);
		LSnotification.setNotifationto(notifyto);
		LSnotification.setRepositorycode(0);
		LSnotification.setRepositorydatacode(0);
		LSnotification.setNotificationfor(1);

		lsnotificationRepository.save(LSnotification);

	}

	public LSlogilablimsorderdetail stopautoregister(LSlogilablimsorderdetail objdir) throws ParseException {
		List<LSlogilablimsorderdetail> logiobj = new ArrayList<LSlogilablimsorderdetail>();
		logiobj = lslogilablimsorderdetailRepository.findByBatchcodeAndBatchid(objdir.getBatchcode(),
				objdir.getBatchid());

		logiobj.get(0).setRepeat(objdir.getRepeat());
		logiobj.get(0).setAutoregistercount(0);
		lslogilablimsorderdetailRepository.save(logiobj);

		List<LsAutoregister> autoobj = lsautoregisterrepo.findByBatchcodeAndScreen(objdir.getBatchcode(),
				"Sheet_Order");
		if (!autoobj.isEmpty()) {
			autoobj.get(0).setRepeat(objdir.getRepeat());
			autoobj.get(0).setStoptime(commonfunction.getCurrentUtcTime());
			logiobj.get(0).setLsautoregisterorders(autoobj.get(0));
			logiobj.get(0).setRepeat(false);
			logiobj.get(0).setCanuserprocess(true);
			lsautoregisterrepo.save(autoobj);
			String timerId = autoobj.get(0).getTimerIdname();
			if (timerId != null) {
				stopTimer(timerId);

			}
		}
		lslogilablimsorderdetailRepository.save(logiobj);
		
		return logiobj.get(0);
	}

	public LSlogilabprotocoldetail stopprotoautoregister(LSlogilabprotocoldetail objdir) throws ParseException {
		LSlogilabprotocoldetail logiobj = new LSlogilabprotocoldetail();
		logiobj = LSlogilabprotocoldetailRepository.findByProtocolordercodeAndProtoclordername(
				objdir.getProtocolordercode(), objdir.getProtoclordername());

		logiobj.setRepeat(objdir.getRepeat());
		LSlogilabprotocoldetailRepository.save(logiobj);

		List<LsAutoregister> autoobj = lsautoregisterrepo.findByBatchcodeAndScreen(objdir.getProtocolordercode(),
				objdir.getLsautoregister().getScreen());
		if (!autoobj.isEmpty()) {
			autoobj.get(0).setRepeat(objdir.getRepeat());
			autoobj.get(0).setStoptime(commonfunction.getCurrentUtcTime());
			lsautoregisterrepo.save(autoobj);
		}
		LSlogilabprotocoldetailRepository.save(logiobj);
		return logiobj;
	}

	public LSlogilablimsorderdetail getsingleorder(LSlogilablimsorderdetail body) {
		LSlogilablimsorderdetail obj = lslogilablimsorderdetailRepository.findByBatchid(body.getBatchid());
		return obj;
	}

	public LSlogilablimsorderdetail Getsingleorder(LSlogilablimsorderdetail objorder) {
		return lslogilablimsorderdetailRepository.findOne(objorder.getBatchcode());
	}

//	public void saveResulttags(Lsresulttags objorder) {
//		lsresulttagsRepository.save(objorder);
//	}

	public List<ProjectOrTaskOrMaterialView> Suggesionforfolder(Map<String, Object> searchobj) {
		String Searchkey = "%" + searchobj.get("Searchkey") + "%";
		Integer sitecode = (Integer) searchobj.get("sitecode");
		List<ProjectOrTaskOrMaterialView> rtnobj = lsprojectmasterRepository
				.getProjectOrTaskOrMaterialSearchBased(Searchkey, sitecode);
		return rtnobj;
	}

	public Map<String, Object> Getordersonfiles(LSfile[] objfiles) {
//		List<LSfile> files = Arrays.asList(objfiles);
		Map<String, Object> mapRtnObj = new HashMap<String, Object>();
//		List<Logilaborders> orders = lslogilablimsorderdetailRepository.findByLsfileIn(files);
//		List<Long> batchcode = orders.stream().map(Logilaborders::getBatchcode)
//				.collect(Collectors.toList());
//		mapRtnObj.put("orders", orders);
//		mapRtnObj.put("results", lsresultforordersRepository.findByBatchcodeInOrderByIdDesc(batchcode));
//		mapRtnObj.put("tags", lsresulttagsRepository.findByOrderidInOrderByIdDesc(batchcode));
		return mapRtnObj;
	}

	public Map<String, Object> Getordersonfiles(Map<String, Object> mapObj) throws ParseException {

		ObjectMapper objm = new ObjectMapper();

		List<LSfile> files = objm.convertValue(mapObj.get("filelist"), new TypeReference<List<LSfile>>() {
		});

		// Extract fromdate and todate
		Long fromdateStr = (Long) mapObj.get("fromdate");
		Long todateStr = (Long) mapObj.get("todate");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		// Parse the date strings into Date objects
		Date fromdate = new Date(fromdateStr); // formatter.parse(fromdateStr);
		Date todate = new Date(todateStr);

		// Set the end of the day for todate
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(todate);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		todate = calendar.getTime();

//		List<LSfile> files = Arrays.asList(objfiles);
		Map<String, Object> mapRtnObj = new HashMap<String, Object>();
		List<Logilaborders> orders = lslogilablimsorderdetailRepository
				.findByOrderflagAndLsfileInAndCreatedtimestampBetweenAndAssignedtoIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
						"R", files, fromdate, todate);
		List<Long> batchcode = orders.stream().map(Logilaborders::getBatchcode).collect(Collectors.toList());
		List<Integer> filecode = files.stream().map(LSfile::getFilecode).collect(Collectors.toList());

		mapRtnObj.put("orders", orders);
		mapRtnObj.put("results", lsresultforordersRepository.findByBatchcodeInOrderByIdDesc(batchcode));
		mapRtnObj.put("tags", reportfileRepository.findByBatchcodeInOrderByIdDesc(batchcode));
		mapRtnObj.put("files", LSfileRepository.findByFilecodeIn(filecode));

		return mapRtnObj;
	}

	public List<LSlogilablimsorderdetail> GetOrdersbyuseronDetailview(LSlogilablimsorderdetail obj) {
		List<LSlogilablimsorderdetail> lstorders = new ArrayList<LSlogilablimsorderdetail>();

		lstorders = lslogilablimsorderdetailRepository
				.findByFiletypeAndOrderflagAndLsprojectmasterInAndApprovelstatusNotAndCompletedtimestampBetweenAndAssignedtoIsNullOrderByBatchcodeDesc(
						obj.getFiletype(), "R", obj.getLstproject(), 3, obj.getFromdate(), obj.getTodate());
		return lstorders;
	}

}
