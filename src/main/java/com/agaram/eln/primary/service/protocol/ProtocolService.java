package com.agaram.eln.primary.service.protocol;

import java.awt.Color;
import java.io.BufferedReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.poi.util.IOUtils;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.config.CustomMultipartFile;
//import com.agaram.eln.primary.commonfunction.Constants;
import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.fetchmodel.getorders.LogilabProtocolOrderssh;
import com.agaram.eln.primary.fetchmodel.getorders.Logilabprotocolorders;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cloudFileManip.CloudUserSignature;
import com.agaram.eln.primary.model.cloudProtocol.CloudLSprotocolorderversionstep;
import com.agaram.eln.primary.model.cloudProtocol.CloudLSprotocolstepInfo;
import com.agaram.eln.primary.model.cloudProtocol.CloudLSprotocolversionstep;
import com.agaram.eln.primary.model.cloudProtocol.CloudLsLogilabprotocolstepInfo;
import com.agaram.eln.primary.model.cloudProtocol.LSprotocolstepInformation;
import com.agaram.eln.primary.model.dashboard.LsActiveWidgets;
import com.agaram.eln.primary.model.fileManipulation.UserSignature;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSOrdernotification;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorder;
import com.agaram.eln.primary.model.instrumentDetails.LSprotocolfolderfiles;
import com.agaram.eln.primary.model.instrumentDetails.LSsheetfolderfiles;
import com.agaram.eln.primary.model.instrumentDetails.LsAutoregister;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolordersharedby;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolordershareto;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolorderstructure;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.notification.Email;
import com.agaram.eln.primary.model.protocols.ElnprotocolTemplateworkflow;
import com.agaram.eln.primary.model.protocols.Elnprotocolworkflow;
import com.agaram.eln.primary.model.protocols.Elnprotocolworkflowgroupmap;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocolsteps;
import com.agaram.eln.primary.model.protocols.LSprotocolfiles;
import com.agaram.eln.primary.model.protocols.LSprotocolfilesContent;
import com.agaram.eln.primary.model.protocols.LSprotocolimages;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolmastertest;
import com.agaram.eln.primary.model.protocols.LSprotocolmethod;
import com.agaram.eln.primary.model.protocols.LSprotocolorderfiles;
import com.agaram.eln.primary.model.protocols.LSprotocolorderfilesContent;
import com.agaram.eln.primary.model.protocols.LSprotocolorderimages;
import com.agaram.eln.primary.model.protocols.LSprotocolordersampleupdates;
import com.agaram.eln.primary.model.protocols.LSprotocolorderstephistory;
import com.agaram.eln.primary.model.protocols.LSprotocolorderstepversion;
import com.agaram.eln.primary.model.protocols.LSprotocolorderversion;
import com.agaram.eln.primary.model.protocols.LSprotocolorderversionstepInfo;
import com.agaram.eln.primary.model.protocols.LSprotocolordervideos;
import com.agaram.eln.primary.model.protocols.LSprotocolorderworkflowhistory;
import com.agaram.eln.primary.model.protocols.LSprotocolsampleupdates;
import com.agaram.eln.primary.model.protocols.LSprotocolselectedteam;
import com.agaram.eln.primary.model.protocols.LSprotocolstep;
import com.agaram.eln.primary.model.protocols.LSprotocolstepInfo;
import com.agaram.eln.primary.model.protocols.LSprotocolstepversion;
import com.agaram.eln.primary.model.protocols.LSprotocolupdates;
import com.agaram.eln.primary.model.protocols.LSprotocolversion;
import com.agaram.eln.primary.model.protocols.LSprotocolversionstepInfo;
import com.agaram.eln.primary.model.protocols.LSprotocolvideos;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflow;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflowgroupmap;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflowhistory;
import com.agaram.eln.primary.model.protocols.LsLogilabprotocolstepInfo;
import com.agaram.eln.primary.model.protocols.Lsprotocolorderdata;
import com.agaram.eln.primary.model.protocols.Lsprotocolorderversiondata;
import com.agaram.eln.primary.model.protocols.Lsprotocolsharedby;
import com.agaram.eln.primary.model.protocols.Lsprotocolshareto;
import com.agaram.eln.primary.model.protocols.Lsprotocoltemplatedata;
import com.agaram.eln.primary.model.protocols.Lsprotocoltemplateversiondata;
import com.agaram.eln.primary.model.protocols.ProtocolImage;
import com.agaram.eln.primary.model.protocols.ProtocolorderImage;
import com.agaram.eln.primary.model.protocols.Protocolordervideos;
import com.agaram.eln.primary.model.protocols.Protocolvideos;
import com.agaram.eln.primary.model.sequence.SequenceTable;
import com.agaram.eln.primary.model.sequence.SequenceTableOrderType;
import com.agaram.eln.primary.model.sequence.SequenceTableProject;
import com.agaram.eln.primary.model.sequence.SequenceTableProjectLevel;
import com.agaram.eln.primary.model.sequence.SequenceTableSite;
import com.agaram.eln.primary.model.sequence.SequenceTableTask;
import com.agaram.eln.primary.model.sequence.SequenceTableTaskLevel;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.sheetManipulation.Notification;
import com.agaram.eln.primary.model.usermanagement.LSMultiusergroup;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSnotification;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.cloudProtocol.CloudLSprotocolorderversionstepRepository;
import com.agaram.eln.primary.repository.cloudProtocol.CloudLSprotocolstepInfoRepository;
import com.agaram.eln.primary.repository.cloudProtocol.CloudLSprotocolversionstepRepository;
import com.agaram.eln.primary.repository.cloudProtocol.CloudLsLogilabprotocolstepInfoRepository;
import com.agaram.eln.primary.repository.cloudProtocol.LSprotocolstepInformationRepository;
import com.agaram.eln.primary.repository.dashboard.LsActiveWidgetsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSordernotificationRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSprotocolfolderfilesRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSsheetfolderfilesRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LogilablimsorderdetailsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsAutoregisterRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsprotocolOrderStructureRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsprotocolordersharedbyRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsprotocolordersharetoRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesdataRepository;
import com.agaram.eln.primary.repository.notification.EmailRepository;
import com.agaram.eln.primary.repository.protocol.ElnprotocolTemplateworkflowRepository;
import com.agaram.eln.primary.repository.protocol.ElnprotocolworkflowRepository;
import com.agaram.eln.primary.repository.protocol.ElnprotocolworkflowgroupmapRepository;
import com.agaram.eln.primary.repository.protocol.LSProtocolMasterRepository;
import com.agaram.eln.primary.repository.protocol.LSProtocolStepRepository;
import com.agaram.eln.primary.repository.protocol.LSlogilabprotocoldetailRepository;
import com.agaram.eln.primary.repository.protocol.LSlogilabprotocolstepsRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolfilesContentRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolfilesRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolimagesRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolmastertestRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolmethodRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolorderfilesContentRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolorderfilesRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolorderimagesRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolordersampleupdatesRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolordersampleupdatesRepository.UserProjection1;
import com.agaram.eln.primary.repository.sequence.SequenceTableOrderTypeRepository;
import com.agaram.eln.primary.repository.sequence.SequenceTableProjectLevelRepository;
import com.agaram.eln.primary.repository.sequence.SequenceTableProjectRepository;
import com.agaram.eln.primary.repository.sequence.SequenceTableRepository;
import com.agaram.eln.primary.repository.sequence.SequenceTableSiteRepository;
import com.agaram.eln.primary.repository.sequence.SequenceTableTaskLevelRepository;
import com.agaram.eln.primary.repository.sequence.SequenceTableTaskRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolorderstephistoryRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolorderstepversionRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolorderversionRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolordervideosRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolorderworkflowhistoryRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolsampleupdatesRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolselectedteamRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolstepversionRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolupdatesRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolversionRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolvideosRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolworkflowgroupmapRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolworkflowhistoryRepository;
import com.agaram.eln.primary.repository.protocol.LsprotocolsharedbyRepository;
import com.agaram.eln.primary.repository.protocol.LsprotocolsharetoRepository;
import com.agaram.eln.primary.repository.protocol.ProtocolImageRepository;
import com.agaram.eln.primary.repository.protocol.ProtocolorderImageRepository;
import com.agaram.eln.primary.repository.protocol.ProtocolordervideosRepository;
import com.agaram.eln.primary.repository.protocol.ProtocolvideosRepository;
import com.agaram.eln.primary.repository.protocol.lSprotocolworkflowRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LStestmasterlocalRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowRepository;
import com.agaram.eln.primary.repository.sheetManipulation.NotificationRepository;
import com.agaram.eln.primary.repository.usermanagement.LSMultisitesRepositery;
import com.agaram.eln.primary.repository.usermanagement.LSMultiusergroupRepositery;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSnotificationRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository.UserProjection;
import com.agaram.eln.primary.repository.usermanagement.LSusergroupRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusersteamRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;
import com.agaram.eln.primary.service.basemaster.BaseMasterService;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.fileManipulation.FileManipulationservice;
import com.agaram.eln.primary.service.material.TransactionService;
import com.agaram.eln.primary.service.usermanagement.LoginService;
import com.google.gson.Gson;
//import com.groupdocs.assembly.License;
//import com.groupdocs.editor.EditableDocument;
//import com.groupdocs.editor.Editor;
//import com.groupdocs.editor.formats.WordProcessingFormats;
//import com.groupdocs.editor.options.WordProcessingEditOptions;
//import com.groupdocs.editor.options.WordProcessingSaveOptions;
import com.aspose.words.*;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.mongodb.gridfs.GridFSDBFile;
import com.spire.doc.BookmarkStart;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.TextWatermark;
import com.spire.doc.documents.BreakType;
import com.spire.doc.documents.HyperlinkType;
import com.spire.doc.documents.Paragraph;

@Service
@EnableJpaRepositories(basePackageClasses = LSProtocolMasterRepository.class)
public class ProtocolService {
	@Autowired
	LSusersteamRepository lsusersteamRepository;
	@Autowired
	LSuserMasterRepository lsusermasterRepository;
	@Autowired
	LSnotificationRepository lsnotificationRepository;
	@Autowired
	LSProtocolMasterRepository LSProtocolMasterRepositoryObj;

	@Autowired
	SequenceTableRepository sequenceTableRepository;
	
	@Autowired
	LsAutoregisterRepository lsautoregisterrepo;

	@Autowired
	LSProtocolStepRepository LSProtocolStepRepositoryObj;

	@Autowired
	LSuserteammappingRepository LSuserteammappingRepositoryObj;

	@Autowired
	LSuserMasterRepository LSuserMasterRepositoryObj;

	@Autowired
	LSProtocolMasterRepository lsProtocolMasterRepository;

	@Autowired
	private CloudFileManipulationservice objCloudFileManipulationservice;

	@Autowired
	Commonservice commonservice;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private LScfttransactionRepository lscfttransactionRepository;

	@Autowired
	private lSprotocolworkflowRepository lSprotocolworkflowRepository;

	@Autowired
	private LSprotocolworkflowgroupmapRepository LSprotocolworkflowgroupmapRepository;

	@Autowired
	LoginService loginservice;

	@Autowired
	private LSSiteMasterRepository LSSiteMasterRepository;

	@Autowired
	private CloudLSprotocolstepInfoRepository CloudLSprotocolstepInfoRepository;

	@Autowired
	private LSlogilabprotocoldetailRepository LSlogilabprotocoldetailRepository;

//	@Autowired
//	private LSsamplemasterRepository lssamplemasterrepository;

	@Autowired
	private LSlogilabprotocolstepsRepository LSlogilabprotocolstepsRepository;

	@Autowired
	private BaseMasterService masterService;

	@Autowired
	private LsrepositoriesRepository LsrepositoriesRepository;

	@Autowired
	private LsrepositoriesdataRepository LsrepositoriesdataRepository;

	@Autowired
	private LSprotocolworkflowhistoryRepository lsprotocolworkflowhistoryRepository;

	@Autowired
	private LSuserMasterRepository lSuserMasterRepository;

	@Autowired
	private LSprotocolupdatesRepository lsprotocolupdatesRepository;

	@Autowired
	private LSprotocolsampleupdatesRepository LSprotocolsampleupdatesRepository;

	@Autowired
	private LSprotocolversionRepository lsprotocolversionRepository;

	@Autowired
	private CloudLSprotocolversionstepRepository CloudLSprotocolversionstepRepository;

	@Autowired
	private CloudLsLogilabprotocolstepInfoRepository CloudLsLogilabprotocolstepInfoRepository;

	@Autowired
	private LSprotocolordersampleupdatesRepository lsprotocolordersampleupdatesRepository;

	@Autowired
	private LSprotocolstepversionRepository LSprotocolstepversionRepository;

	@Autowired
	private LSusergroupRepository LSusergroupRepository;

	@Autowired
	private CloudFileManipulationservice cloudFileManipulationservice;

	@Autowired
	private LSprotocolimagesRepository lsprotocolimagesRepository;

	@Autowired
	private LSprotocolfilesRepository lsprotocolfilesRepository;

	@Autowired
	private LSprotocolstepInformationRepository lsprotocolstepInformationRepository;

	@Autowired
	private LSprotocolorderimagesRepository lsprotocolorderimagesRepository;

	@Autowired
	private LSprotocolorderfilesRepository lsprotocolorderfilesRepository;

	@Autowired
	private Environment env;

	@Autowired
	private LsprotocolordersharedbyRepository lsprotocolordersharedbyRepository;

	@Autowired
	private LsprotocolordersharetoRepository lsprotocolordersharetoRepository;

	@Autowired
	private LsprotocolsharetoRepository LsprotocolsharetoRepository;

	@Autowired
	private LsprotocolsharedbyRepository LsprotocolsharedbyRepository;

	@Autowired
	private LSprotocolmastertestRepository LSprotocolmastertestRepository;

	@Autowired
	private ProtocolImageRepository protocolImageRepository;

	@Autowired
	private LSprotocolfilesContentRepository lsprotocolfilesContentRepository;

	@Autowired
	private ProtocolorderImageRepository ProtocolorderImageRepository;

	@Autowired
	private LSprotocolorderfilesContentRepository LSprotocolorderfilesContentRepository;

	@Autowired
	private LSprotocolorderworkflowhistoryRepository lsprotocolorderworkflowhistoryRepository;

	@Autowired
	private LSprotocolvideosRepository lsprotocolvideosRepository;

	@Autowired
	private ProtocolvideosRepository ProtocolvideosRepository;

	@Autowired
	private LSprotocolordervideosRepository LSprotocolordervideosRepository;

	@Autowired
	private CloudLSprotocolorderversionstepRepository CloudLSprotocolorderversionstepRepository;

	@Autowired
	private ProtocolordervideosRepository ProtocolordervideosRepository;

	@Autowired
	private LSprotocolorderstepversionRepository lsprotocolorderstepversionRepository;

	@Autowired
	private LSprotocolorderversionRepository lsprotocolorderversionRepository;

	@Autowired
	private LStestmasterlocalRepository lstestmasterlocalRepository;

	@Autowired
	private FileManipulationservice fileManipulationservice;

	@Autowired
	private LSworkflowRepository lsworkflowRepository;

//	@Autowired
//	private LSsheetworkflowRepository lssheetworkflowRepository;

	@Autowired
	private LSprotocolorderstephistoryRepository lsprotocolorderstephistoryRepository;

//	@Autowired
//	private LSworkflowgroupmappingRepository lsworkflowgroupmappingRepository;

	@Autowired
	private LsprotocolOrderStructureRepository lsprotocolorderStructurerepository;

//	@Autowired
//	private LSuserteammappingRepository lsuserteammappingRepository;
	@Autowired
	private LSMultiusergroupRepositery lsMultiusergroupRepositery;

	@Autowired
	private LSsheetfolderfilesRepository lssheetfolderfilesRepository;

	@Autowired
	private LSprotocolfolderfilesRepository lsprotocolfolderfilesRepository;

	@Autowired
	TransactionService transactionService;

//	@Autowired
//	private GridFsTemplate gridFsTemplate;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private EmailRepository EmailRepository;

	@Autowired
	private LSlogilablimsorderRepository lslogilablimsorderrepo;

//	@Autowired
//	private LSMultiusergroupRepositery lsMultiusergroupRepositery;

	@Autowired
	private LSprotocolmethodRepository lsprotocolmethodrepo;
	@Autowired
	private ElnprotocolworkflowRepository elnprotocolworkflowRepository;

	@Autowired
	private ElnprotocolworkflowgroupmapRepository elnprotocolworkflowgroupmapRepository;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private ElnprotocolTemplateworkflowRepository elnprotocolTemplateworkflowRepository;

	@Autowired
	private LSMultisitesRepositery LSMultisitesRepositery;

//	@Autowired
//	private ElnprotocolTemplateworkflowgroupmapRepository elnprotocolTemplateworkflowgroupmapRepository;

	@Autowired
	private LSuserteammappingRepository lsuserteammappingRepository;
	@Autowired
	private LSordernotificationRepository lsordernotificationrepo;

	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private LsActiveWidgetsRepository lsActiveWidgetsRepository;
	
	@Autowired
	private SequenceTableRepository sequencetableRepository;
	
	@Autowired
	private SequenceTableSiteRepository sequencetablesiteRepository;
	
	@Autowired
	private SequenceTableProjectRepository sequencetableprojectRepository;
	
	@Autowired
	private SequenceTableTaskRepository sequencetabletaskRepository;

	@Autowired
	private SequenceTableOrderTypeRepository sequencetableordertyperepository;
	
	@Autowired
	private SequenceTableProjectLevelRepository sequencetableprojectlevelrepository;
	
	@Autowired
	private SequenceTableTaskLevelRepository sequencetabletasklevelrepository;
	
	@Autowired
	private LogilablimsorderdetailsRepository logilablimsorderdetailsRepository;
	
	@Autowired
	private LSprotocolselectedteamRepository lsprotoselectedteamRepo;
	
	private static Map<String, Timer> timerMapPro = new HashMap<>();
	private static Map<String, Boolean> timerStatusMapPro = new HashMap<>();
	 private ConcurrentHashMap<String, LSlogilabprotocoldetail> orderDetailMapPro = new ConcurrentHashMap<>();

	public Map<String, Object> getProtocolMasterInit(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();

		if (argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			LScfttransactionobj.setTableName("LSprotocolmaster");
			lscfttransactionRepository.save(LScfttransactionobj);
		}
		@SuppressWarnings("unchecked")
		List<LSprotocolmaster> LSprotocolmasterLst = (List<LSprotocolmaster>) getLSProtocolMasterLst(argObj)
				.get("LSProtocolMasterLst");
		List<LSprotocolversion> LSprotocolversionlst = lsprotocolversionRepository.findAll();
		Integer isMulitenant = (Integer) argObj.get("ismultitenant");

		if (LSprotocolmasterLst.size() > 0) {
			List<LSprotocolstep> LSprotocolstepLst = LSProtocolStepRepositoryObj
					.findByProtocolmastercodeAndStatus(LSprotocolmasterLst.get(0).getProtocolmastercode(), 1);
			for (LSprotocolstep LSprotocolstepObj : LSprotocolstepLst) {

				if (isMulitenant == 1) {
					CloudLSprotocolstepInfo newLSprotocolstepInfo = CloudLSprotocolstepInfoRepository
							.findById(LSprotocolstepObj.getProtocolstepcode());
					if (newLSprotocolstepInfo != null) {
						LSprotocolstepObj.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
					}
				} else {
					LSprotocolstepInfo LSprotocolstepInfoObj = mongoTemplate
							.findById(LSprotocolstepObj.getProtocolstepcode(), LSprotocolstepInfo.class);
					if (LSprotocolstepInfoObj != null) {
						LSprotocolstepObj.setLsprotocolstepInfo(LSprotocolstepInfoObj.getContent());
					}
				}
//					LSprotocolstepInfo LSprotocolstepInfoObj = mongoTemplate.findById(LSprotocolstepObj.getProtocolstepcode(), LSprotocolstepInfo.class);
//					if(LSprotocolstepInfoObj != null) {
//						LSprotocolstepObj.setLsprotocolstepInfo(LSprotocolstepInfoObj.getContent());
//					}
			}
			LSprotocolmasterLst = LSprotocolmasterLst.stream().distinct().collect(Collectors.toList());

			Collections.sort(LSprotocolmasterLst, Collections.reverseOrder());

			mapObj.put("protocolmasterLst", LSprotocolmasterLst);
			mapObj.put("protocolstepLst", LSprotocolstepLst);
			mapObj.put("LSprotocolversionlst", LSprotocolversionlst);

		} else {
			LSprotocolmasterLst = LSprotocolmasterLst.stream().distinct().collect(Collectors.toList());

			Collections.sort(LSprotocolmasterLst, Collections.reverseOrder());

			mapObj.put("protocolmasterLst", LSprotocolmasterLst);
			mapObj.put("protocolstepLst", new ArrayList<>());
			mapObj.put("LSprotocolversionlst", new ArrayList<>());
		}

		return mapObj;
	}

	public List<LSprotocolmaster> getprotocol(LSuserMaster objusers) {
		List<Integer> lstuser = objusers.getObjuser().getTeamuserscode();
		List<LSprotocolmaster> LSprotocolmaster = new ArrayList<LSprotocolmaster>();

		if (lstuser.size() != 0) {
			LSprotocolmaster = LSProtocolMasterRepositoryObj
					.findByStatusAndLssitemasterAndViewoptionOrCreatedbyAndStatusAndLssitemasterAndViewoptionOrCreatedbyInAndStatusAndLssitemasterAndViewoptionOrderByCreatedateDesc(
							1, objusers.getLssitemaster().getSitecode(), 1, objusers.getUsercode(), 1,
							objusers.getLssitemaster().getSitecode(), 2, lstuser, 1,
							objusers.getLssitemaster().getSitecode(), 3);

		} else {
			LSprotocolmaster = LSProtocolMasterRepositoryObj
					.findByStatusAndLssitemasterAndViewoptionOrCreatedbyAndStatusAndLssitemasterAndViewoptionOrderByCreatedateDesc(
							1, objusers.getLssitemaster().getSitecode(), 1, objusers.getUsercode(), 1,
							objusers.getLssitemaster().getSitecode(), 2);

		}
//		if (lstuser.size() != 0) {
//			LSprotocolmaster = LSProtocolMasterRepositoryObj
//					.findByCreatedbyInAndStatusAndLssitemasterOrderByCreatedateDesc(lstuser, 1,
//							objusers.getLssitemaster().getSitecode());
//		} else {
//			LSprotocolmaster = LSProtocolMasterRepositoryObj
//					.findByCreatedbyInAndStatusAndLssitemasterOrderByCreatedateDesc(objusers.getUsercode(), 1,
//							objusers.getLssitemaster().getSitecode());
//		}

		return LSprotocolmaster;
	}

	public Map<String, Object> getProtocolcount(LSuserMaster objusers) {

		Map<String, Object> mapObj = new HashMap<String, Object>();

		List<Integer> lstuser = objusers.getObjuser().getTeamuserscode();
//		if (lstuser.size()== 0) {
//			mapObj.put("templatecount",
//					LSProtocolMasterRepositoryObj.countByStatusAndLssitemasterAndViewoptionOrCreatedbyAndStatusAndLssitemasterAndViewoptionOrderByCreatedateDesc(
//							1, objusers.getLssitemaster().getSitecode(), 1, objusers.getUsercode(), 1,
//							objusers.getLssitemaster().getSitecode(), 2));
//		} else {
//			mapObj.put("templatecount",
//					LSProtocolMasterRepositoryObj.countByCreatedbyInAndStatusAndLssitemasterOrderByCreatedateDesc(
//							lstuser, 1, objusers.getLssitemaster().getSitecode()));
//
//		}
		if (lstuser.size() != 0) {
			mapObj.put("templatecount", LSProtocolMasterRepositoryObj
					.countByStatusAndLssitemasterAndViewoptionOrCreatedbyAndStatusAndLssitemasterAndViewoptionOrCreatedbyInAndStatusAndLssitemasterAndViewoptionOrderByCreatedateDesc(
							1, objusers.getLssitemaster().getSitecode(), 1, objusers.getUsercode(), 1,
							objusers.getLssitemaster().getSitecode(), 2, lstuser, 1,
							objusers.getLssitemaster().getSitecode(), 3));

		} else {
			mapObj.put("templatecount", LSProtocolMasterRepositoryObj
					.countByStatusAndLssitemasterAndViewoptionOrCreatedbyAndStatusAndLssitemasterAndViewoptionOrderByCreatedateDesc(
							1, objusers.getLssitemaster().getSitecode(), 1, objusers.getUsercode(), 1,
							objusers.getLssitemaster().getSitecode(), 2));

		}
		mapObj.put("sharedbyme",
				LsprotocolsharedbyRepository.countBySharebyunifiedidAndSharestatusOrderBySharedbytoprotocolcodeDesc(
						objusers.getSharebyunifiedid(), 1));
		mapObj.put("sharedtome",
				LsprotocolsharetoRepository.countBySharetounifiedidAndSharestatusOrderBySharetoprotocolcodeDesc(
						objusers.getSharetounifiedid(), 1));

		return mapObj;
	}

	public Map<String, Object> getLSProtocolMasterLst(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if (argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});

			List<LSprotocolmaster> LSprotocolmasterLst = new ArrayList<>();

			LSprotocolworkflow lsprotocolworkflow = new LSprotocolworkflow();
			if (LScfttransactionobj.getUsername().equalsIgnoreCase("Administrator")) {

				LSSiteMaster siteObj = new ObjectMapper().convertValue(argObj.get("lssitemaster"),
						new TypeReference<LSSiteMaster>() {
						});

				LSprotocolmasterLst = LSProtocolMasterRepositoryObj.findByStatusAndLssitemaster(1,
						siteObj.getSitecode());

			} else {
				LSprotocolmasterLst = LSProtocolMasterRepositoryObj.findByCreatedbyAndStatusAndLssitemaster(
						LScfttransactionobj.getLsuserMaster(), 1, LScfttransactionobj.getLssitemaster());

				if (argObj.containsKey("multiusergroups")) {

					ObjectMapper objMapper = new ObjectMapper();
					int lsusergroupcode = objMapper.convertValue(argObj.get("multiusergroups"), Integer.class);

					LSusergroup lsusergroup = LSusergroupRepository.findOne(lsusergroupcode);

					List<LSprotocolworkflowgroupmap> lsprotocolworkflowgroupmap = LSprotocolworkflowgroupmapRepository
							.findBylsusergroupAndWorkflowcodeNotNull(lsusergroup);

					if (lsprotocolworkflowgroupmap != null && lsprotocolworkflowgroupmap.size() > 0) {
						lsprotocolworkflow = lSprotocolworkflowRepository
								.findByworkflowcode(lsprotocolworkflowgroupmap.get(0).getWorkflowcode());

						List<LSprotocolmaster> LSprotocolmasterLst1 = LSProtocolMasterRepositoryObj
								.findByStatusAndLssitemasterAndLSprotocolworkflowAndCreatedbyNotAndSharewithteam(1,
										LScfttransactionobj.getLssitemaster(), lsprotocolworkflow,
										LScfttransactionobj.getLsuserMaster(), 0);
						LSprotocolmasterLst.addAll(LSprotocolmasterLst1);

						List<LSprotocolmaster> LSprotocolmasterLst2 = LSProtocolMasterRepositoryObj
								.findByStatusAndLssitemasterAndLSprotocolworkflowNotAndCreatedbyNotAndSharewithteamAndApproved(
										1, LScfttransactionobj.getLssitemaster(), lsprotocolworkflow,
										LScfttransactionobj.getLsuserMaster(), 0, 0);

						LSprotocolmasterLst.addAll(LSprotocolmasterLst2);
					}
				}
			}

			List<Integer> teamCodeLst = LSuserteammappingRepositoryObj
					.getTeamcodeByLsuserMaster4postgressandsql(LScfttransactionobj.getLsuserMaster());

			if (teamCodeLst.size() > 0) {
				List<LSuserMaster> lsusermasterLst = LSuserteammappingRepositoryObj
						.getLsuserMasterByTeamcode(teamCodeLst);

				if (lsusermasterLst.size() > 0) {
					for (LSuserMaster lsusermasterObj : lsusermasterLst) {

						List<LSprotocolmaster> LSprotocolmasterTempLst = new ArrayList<>();
						if (lsprotocolworkflow.getWorkflowname() != null) {
							LSprotocolmasterTempLst = LSProtocolMasterRepositoryObj
									.findByCreatedbyNotAndStatusAndLssitemasterAndSharewithteamAndLSprotocolworkflowNot(
											lsusermasterObj.getUsercode(), 1,
											lsusermasterObj.getLssitemaster().getSitecode(), 1, lsprotocolworkflow);
						} else {
							LSprotocolmasterTempLst = LSProtocolMasterRepositoryObj
									.findByCreatedbyNotAndStatusAndLssitemasterAndSharewithteam(
											lsusermasterObj.getUsercode(), 1,
											lsusermasterObj.getLssitemaster().getSitecode(), 1);
						}
						if (LSprotocolmasterTempLst.size() > 0) {
							LSprotocolmasterLst.addAll(LSprotocolmasterTempLst);
						}
//						}
					}
				}
			}

			LSprotocolmasterLst = LSprotocolmasterLst.stream().distinct().collect(Collectors.toList());

			Collections.sort(LSprotocolmasterLst, Collections.reverseOrder());

			mapObj.put("LSProtocolMasterLst", LSprotocolmasterLst);

		}

		return mapObj;
	}

	public Map<String, Object> getApprovedprotocolLst(LSSiteMaster site) {

		Map<String, Object> mapObj = new HashMap<String, Object>();

		List<LSprotocolmaster> LSprotocolmasterLst = LSProtocolMasterRepositoryObj
				.findByStatusAndApprovedAndLssitemasterAndRetirestatus(1, 1, site.getSitecode(), 0);

		mapObj.put("protocolLst", LSprotocolmasterLst);

		return mapObj;
	}

	public Map<String, Object> getProtocolStepLst(Map<String, Object> argObj) throws IOException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
//		@SuppressWarnings("unused")
//		LScfttransaction LScfttransactionobj = new LScfttransaction();

		if (argObj.containsKey("protocolmastercode")) {

//			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
//					new TypeReference<LScfttransaction>() {
//					});
			List<LSprotocolmastertest> testcode = LSprotocolmastertestRepository
					.findByProtocolmastercode(argObj.get("protocolmastercode"));
			if (testcode != null ) {
//				LStestmasterlocal test = lstestmasterlocalRepository.findByTestcode(testcode.getTestcode());
//				if (test != null && test.getTestname() != null) {
//					testcode.setTestname(test.getTestname());
					mapObj.put("LSprotocolmastertest", testcode);
//				}

			}

			List<LSprotocolstep> LSprotocolsteplst = LSProtocolStepRepositoryObj
					.findByProtocolmastercodeAndStatus(argObj.get("protocolmastercode"), 1);
			List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();

			ObjectMapper objm = new ObjectMapper();

			int multitenent = objm.convertValue(argObj.get("ismultitenant"), Integer.class);
			int protocolmastercode = objm.convertValue(argObj.get("protocolmastercode"), Integer.class);

			List<LSprotocolversion> LSprotocolversionlst = lsprotocolversionRepository
					.findByprotocolmastercode(protocolmastercode);

			Collections.sort(LSprotocolversionlst, Collections.reverseOrder());

			// List<CloudLSprotocolversionstep> LSprotocolversionlst =
			// CloudLSprotocolversionstepRepository.findByprotocolmastercode(protocolmastercode);

			for (LSprotocolstep LSprotocolstepObj1 : LSprotocolsteplst) {
				if (multitenent == 1 || multitenent == 2) {
//					CloudLSprotocolstepInfo newLSprotocolstepInfo = CloudLSprotocolstepInfoRepository
//							.findById(LSprotocolstepObj1.getProtocolstepcode());
//					if (newLSprotocolstepInfo != null) {
//						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
//					}

					LSprotocolstepInformation newobj = lsprotocolstepInformationRepository
							.findById(LSprotocolstepObj1.getProtocolstepcode());
					if (newobj != null) {
						LSprotocolstepObj1.setLsprotocolstepInformation(newobj.getLsprotocolstepInfo());
					}
				} else {
					LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate
							.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
					if (newLSprotocolstepInfo != null) {
//						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
						LSprotocolstepObj1.setLsprotocolstepInformation(newLSprotocolstepInfo.getContent());
					}
				}

				LSprotocolstepLst.add(LSprotocolstepObj1);
			}
			if (LSprotocolsteplst != null) {
				mapObj.put("protocolstepLst", LSprotocolstepLst);
				mapObj.put("LSprotocolversionlst", LSprotocolversionlst);
			} else {
				mapObj.put("protocolstepLst", new ArrayList<>());
				mapObj.put("LSprotocolversionlst", new ArrayList<>());
			}
			LSprotocolmaster lsProtocolMaster = lsProtocolMasterRepository.findByprotocolmastercode(protocolmastercode);
			if (multitenent == 1 || multitenent == 2) {
				mapObj.put("ProtocolData", objCloudFileManipulationservice.retrieveCloudSheets(
						lsProtocolMaster.getFileuid(),
						commonfunction.getcontainername(multitenent, TenantContext.getCurrentTenant()) + "protocol"));
			} else {
//				Lsprotocoltemplatedata lsprotocoldata = mongoTemplate
//				.findById(lsProtocolMaster.getProtocolmastercode(), Lsprotocoltemplatedata.class);
//				if(lsprotocoldata != null && lsprotocoldata.getContent() != null) {
//					mapObj.put("ProtocolData", lsprotocoldata.getContent());
//				} else {
				GridFSDBFile largefile = gridFsTemplate.findOne(new Query(
						Criteria.where("filename").is("protocol_" + lsProtocolMaster.getProtocolmastercode())));
				mapObj.put("ProtocolData",
						new BufferedReader(new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8))
								.lines().collect(Collectors.joining("\n")));
//				}
			}

		}
		return mapObj;
	}

	public Map<String, Object> getProtocolStepLstForShare(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		@SuppressWarnings("unused")
		LScfttransaction LScfttransactionobj = new LScfttransaction();

		if (argObj.containsKey("objsilentaudit")) {

			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});

			List<LSprotocolstep> LSprotocolsteplst = LSProtocolStepRepositoryObj
					.findByProtocolmastercodeAndStatus(argObj.get("protocolmastercode"), 1);
			List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();

			ObjectMapper objm = new ObjectMapper();

			int multitenent = objm.convertValue(argObj.get("ismultitenant"), Integer.class);
			int protocolmastercode = objm.convertValue(argObj.get("protocolmastercode"), Integer.class);

			List<LSprotocolversion> LSprotocolversionlst = lsprotocolversionRepository
					.findByprotocolmastercode(protocolmastercode);

			Collections.sort(LSprotocolversionlst, Collections.reverseOrder());

			// List<CloudLSprotocolversionstep> LSprotocolversionlst =
			// CloudLSprotocolversionstepRepository.findByprotocolmastercode(protocolmastercode);

			for (LSprotocolstep LSprotocolstepObj1 : LSprotocolsteplst) {
				if (multitenent == 1 || multitenent == 2) {
					CloudLSprotocolstepInfo newLSprotocolstepInfo = CloudLSprotocolstepInfoRepository
							.findById(LSprotocolstepObj1.getProtocolstepcode());
					if (newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
					}
					LSprotocolstepInformation newobj = lsprotocolstepInformationRepository
							.findById(LSprotocolstepObj1.getProtocolstepcode());
					if (newobj != null) {
						LSprotocolstepObj1.setLsprotocolstepInformation(newobj.getLsprotocolstepInfo());
					}
				} else {
					LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate
							.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
					if (newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
						LSprotocolstepObj1.setLsprotocolstepInformation(newLSprotocolstepInfo.getContent());
					}
				}

				LSprotocolstepLst.add(LSprotocolstepObj1);
			}
			if (LSprotocolsteplst != null) {
				mapObj.put("protocolstepLst", LSprotocolstepLst);
				mapObj.put("LSprotocolversionlst", LSprotocolversionlst);
			} else {
				mapObj.put("protocolstepLst", new ArrayList<>());
				mapObj.put("LSprotocolversionlst", new ArrayList<>());
			}
		}
		mapObj.put("SelectedProtocol", LSProtocolMasterRepositoryObj
				.findFirstByProtocolmastercode((Integer) argObj.get("protocolmastercode")));
		return mapObj;
	}

	public Map<String, Object> getAllProtocolStepLst(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		ObjectMapper obj = new ObjectMapper();
		int sitecode = obj.convertValue(argObj.get("sitecode"), Integer.class);
		String searchcontent = obj.convertValue(argObj.get("searchcontent"), String.class);
		List<Long> protocolordercode = obj.convertValue(argObj.get("protocolordercodelist"),
				new TypeReference<List<Long>>() {
				});
//		List<LSworkflow> workflow = obj.convertValue(argObj.get("lstworkflow"), new TypeReference<List<LSworkflow>>() {
//		});
		List<Elnprotocolworkflow> workflow = obj.convertValue(argObj.get("lstelnprotocolworkflow"),
				new TypeReference<List<LSworkflow>>() {
				});
		List<LSlogilabprotocolsteps> LSlogilabprotocolsteps = LSlogilabprotocolstepsRepository
				.findByStatusAndSitecodeAndProtocolordercodeIn(1, sitecode, protocolordercode);
		ArrayList<Long> ordercode = new ArrayList<Long>();
		for (LSlogilabprotocolsteps LSprotocolstepObj1 : LSlogilabprotocolsteps) {
			if ((int) argObj.get("ismultitenant") == 1 || (int) argObj.get("ismultitenant") == 2) {
				CloudLsLogilabprotocolstepInfo newLSprotocolstepInfo = CloudLsLogilabprotocolstepInfoRepository
						.findByContentvaluesequal("%" + searchcontent + "%",
								LSprotocolstepObj1.getProtocolorderstepcode());
				if (newLSprotocolstepInfo != null) {
//						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
					ordercode.add(LSprotocolstepObj1.getProtocolordercode());
				}
			} else {
				Query query = new Query();
				query.addCriteria(Criteria.where("content").regex(searchcontent, "i"));
				query.addCriteria(Criteria.where("id").in(LSprotocolstepObj1.getProtocolorderstepcode()))
						.with(new PageRequest(0, 5));
				List<LsLogilabprotocolstepInfo> newLSprotocolstepInfo = mongoTemplate.find(query,
						LsLogilabprotocolstepInfo.class);

//					LsLogilabprotocolstepInfo newLSprotocolstepInfo = mongoTemplate
//							.findById(LSprotocolstepObj1.getProtocolorderstepcode(), LsLogilabprotocolstepInfo.class);
				if (newLSprotocolstepInfo.size() > 0) {
					ordercode.add(LSprotocolstepObj1.getProtocolordercode());
				}
			}
		}
		if (ordercode.size() > 0) {
			if (argObj.containsKey("OSBM") || argObj.containsKey("OSTM")) {
				if (argObj.containsKey("OSBM")) {
					List<Lsprotocolordersharedby> lsprotocolordersharedby = lsprotocolordersharedbyRepository
							.findByShareprotocolordercodeIn(ordercode);
					mapObj.put("sharebyme", lsprotocolordersharedby);
				} else {
					List<Lsprotocolordershareto> lsprotocolordershareto = lsprotocolordersharetoRepository
							.findByShareprotocolordercodeIn(ordercode);
					mapObj.put("sharetome", lsprotocolordershareto);
				}
				mapObj.put("directorycode", argObj.get("directorycode"));
			} else {
				List<LSlogilabprotocoldetail> LSlogilabprotocoldetailArray = LSlogilabprotocoldetailRepository
						.findByProtocolordercodeIn(ordercode);
				if (LSlogilabprotocoldetailArray != null) {
//					LSlogilabprotocoldetailArray.forEach(objorderDetail -> objorderDetail.setLstworkflow(workflow));
					LSlogilabprotocoldetailArray
							.forEach(objorderDetail -> objorderDetail.setLstelnprotocolworkflow(workflow));
					mapObj.put("protocolorders", LSlogilabprotocoldetailArray);
				} else {
					mapObj.put("protocolorders", new ArrayList<>());
				}
			}

		} else if (argObj.containsKey("OSBM") || argObj.containsKey("OSTM")) {
			mapObj.put("directorycode", argObj.get("directorycode"));
		}

		return mapObj;
	}

	public Map<String, Object> getAllProtocolStepLstonsharedorder(Integer ismultitenant, String searchcontent,
			Integer sitecode, List<Map<String, Object>> protocolordercodelist) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		ObjectMapper obj = new ObjectMapper();
		if (protocolordercodelist.size() > 0) {
//			for (int index = 0; index < protocolordercodelist.size(); index++) {
			Map<String, Object> listobj = protocolordercodelist.get(0);

			listobj.forEach((key, value) -> {
				List<Long> protocolordercode = new ArrayList<Long>();
				protocolordercode = obj.convertValue(value, new TypeReference<List<Long>>() {
				});
				List<LSlogilabprotocolsteps> LSlogilabprotocolsteps = LSlogilabprotocolstepsRepository
						.findByStatusAndSitecodeAndProtocolordercodeIn(1, sitecode, protocolordercode);
				ArrayList<Long> ordercode = new ArrayList<Long>();
				for (LSlogilabprotocolsteps LSprotocolstepObj1 : LSlogilabprotocolsteps) {
					if (ismultitenant == 1 || ismultitenant == 2) {
						CloudLsLogilabprotocolstepInfo newLSprotocolstepInfo = CloudLsLogilabprotocolstepInfoRepository
								.findByContentvaluesequal("%" + searchcontent + "%",
										LSprotocolstepObj1.getProtocolorderstepcode());
						if (newLSprotocolstepInfo != null) {
							ordercode.add(LSprotocolstepObj1.getProtocolordercode());
						}
					} else {
						Query query = new Query();
						query.addCriteria(Criteria.where("content").regex(searchcontent, "i"));
						query.addCriteria(Criteria.where("id").in(LSprotocolstepObj1.getProtocolorderstepcode()))
								.with(new PageRequest(0, 5));
						List<LsLogilabprotocolstepInfo> newLSprotocolstepInfo = mongoTemplate.find(query,
								LsLogilabprotocolstepInfo.class);
						if (newLSprotocolstepInfo.size() > 0) {
							ordercode.add(LSprotocolstepObj1.getProtocolordercode());
						}
					}
				}
				if (ordercode.size() > 0 && key == "assigned") {

					List<LSlogilabprotocoldetail> LSlogilabprotocoldetailArray = LSlogilabprotocoldetailRepository
							.findByProtocolordercodeIn(ordercode);
					if (LSlogilabprotocoldetailArray != null) {
						mapObj.put("assigned", LSlogilabprotocoldetailArray);
					}
				} else if (ordercode.size() > 0 && key == "sharebyme") {
					List<Lsprotocolordersharedby> orders = lsprotocolordersharedbyRepository
							.findByShareprotocolordercodeIn(ordercode);
					if (orders != null) {
						mapObj.put("sharebyme", orders);
					}

				} else if (ordercode.size() > 0 && key == "sharetome") {
					List<Lsprotocolordershareto> orders = lsprotocolordersharetoRepository
							.findByShareprotocolordercodeIn(ordercode);
					if (orders != null) {
						mapObj.put("sharetome", orders);
					}
				}
			});

//			}

		}

		return mapObj;
	}

	public Map<String, Object> getOrdersLinkedToProtocol(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		@SuppressWarnings("unused")
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if (argObj.containsKey("protocolmastercodeArray")) {
			@SuppressWarnings("unchecked")
			ArrayList<Integer> protocolmastercodeArray = (ArrayList<Integer>) argObj.get("protocolmastercodeArray");

			ArrayList<Long> log = (ArrayList<Long>) protocolmastercodeArray.stream().mapToLong(Integer::longValue)
					.boxed().collect(Collectors.toList());
			ObjectMapper obj = new ObjectMapper();

			if (argObj.containsKey("protocoltype")) {
				Integer protocoltype = obj.convertValue(argObj.get("protocoltype"), Integer.class);
				List<LSlogilabprotocoldetail> LSlogilabprotocoldetailArray = LSlogilabprotocoldetailRepository
						.findByProtocolordercodeInAndProtocoltype(log, protocoltype);
				if (LSlogilabprotocoldetailArray != null) {
					mapObj.put("protocolorders", LSlogilabprotocoldetailArray);
				} else {
					mapObj.put("protocolorders", new ArrayList<>());
				}
			} else {
				List<LSlogilabprotocoldetail> LSlogilabprotocoldetailArray = LSlogilabprotocoldetailRepository
						.findByProtocolordercodeIn(log);
				if (LSlogilabprotocoldetailArray != null) {
					mapObj.put("protocolorders", LSlogilabprotocoldetailArray);
				} else {
					mapObj.put("protocolorders", new ArrayList<>());
				}
			}

		}
		return mapObj;
	}

	@SuppressWarnings({ "unused" })
	public Map<String, Object> addProtocolStep(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if (argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
		}
		ObjectMapper objMapper = new ObjectMapper();
		LoggedUser objUser = new LoggedUser();
		Response response = new Response();

		if (argObj.containsKey("newProtocolstepObj")) {
			LSprotocolstep LSprotocolstepObj = new ObjectMapper().convertValue(argObj.get("newProtocolstepObj"),
					new TypeReference<LSprotocolstep>() {
					});
			List<LSprotocolstep> LSprotocolstepObjforstepno = LSProtocolStepRepositoryObj
					.findByProtocolmastercodeAndStatus(LSprotocolstepObj.getProtocolmastercode(), 1);
			if (LSprotocolstepObj.getNewStep() == 1
					&& LSprotocolstepObjforstepno.size() >= LSprotocolstepObj.getStepno()) {
				LSprotocolstepObj.setStepno(LSprotocolstepObjforstepno.size() + 1);
			}

			LSuserMaster LsuserMasterObj = LSuserMasterRepositoryObj
					.findByusercode(LScfttransactionobj.getLsuserMaster());
			if (LSprotocolstepObj.getStatus() == null) {
				LSprotocolstepObj.setStatus(1);
				LSprotocolstepObj.setCreatedby(LScfttransactionobj.getLsuserMaster());
				LSprotocolstepObj.setCreatedbyusername(LsuserMasterObj.getUsername());
				LSprotocolstepObj.setCreateddate(new Date());
				LSprotocolstepObj.setSitecode(LScfttransactionobj.getLssitemaster());
			}
			LSProtocolStepRepositoryObj.save(LSprotocolstepObj);

			CloudLSprotocolstepInfo CloudLSprotocolstepInfoObj = new CloudLSprotocolstepInfo();

			if (LSprotocolstepObj.getIsmultitenant() == 1 || LSprotocolstepObj.getIsmultitenant() == 2) {
				LScfttransaction objaudit1 = new LScfttransaction();
				try {
					updateCloudProtocolVersion(LSprotocolstepObj.getProtocolmastercode(),
							LSprotocolstepObj.getProtocolstepcode(), LSprotocolstepObj.getLsprotocolstepInfo(),
							LSprotocolstepObj.getNewStep(), LScfttransactionobj.getLssitemaster(), LSprotocolstepObj,
							LsuserMasterObj.getUsername(), LsuserMasterObj.getUsercode(), objaudit1);

				} catch (Exception e) {
					// TODO: handle exception
				}

				if (LSprotocolstepObj.getNewStep() == 1) {
					CloudLSprotocolstepInfoObj.setId(LSprotocolstepObj.getProtocolstepcode());
					CloudLSprotocolstepInfoObj.setLsprotocolstepInfo(LSprotocolstepObj.getLsprotocolstepInfo());
					CloudLSprotocolstepInfoRepository.save(CloudLSprotocolstepInfoObj);
				} else {
					CloudLSprotocolstepInfo updateLSprotocolstepInfo = CloudLSprotocolstepInfoRepository
							.findById(LSprotocolstepObj.getProtocolstepcode());
					updateLSprotocolstepInfo.setLsprotocolstepInfo(LSprotocolstepObj.getLsprotocolstepInfo());
					CloudLSprotocolstepInfoRepository.save(updateLSprotocolstepInfo);

					LSprotocolmaster protocolmaster = LSProtocolMasterRepositoryObj
							.findByprotocolmastercode(LSprotocolstepObj.getProtocolmastercode());
					mapObj.put("protocolmaster", protocolmaster);
					List<LSprotocolversion> LSprotocolversionlst = lsprotocolversionRepository
							.findByprotocolmastercode(LSprotocolstepObj.getProtocolmastercode());

					Collections.sort(LSprotocolversionlst, Collections.reverseOrder());

					mapObj.put("LSprotocolversionlst", LSprotocolversionlst);
				}

			} else {
				try {
					updateCloudProtocolVersiononSQL(LSprotocolstepObj, LScfttransactionobj.getLssitemaster(),
							LsuserMasterObj.getUsername(), LsuserMasterObj.getUsercode());

				} catch (Exception e) {
					// TODO: handle exception
				}

				Query query = new Query(Criteria.where("id").is(LSprotocolstepObj.getProtocolstepcode()));
				Update update = new Update();
				update.set("content", LSprotocolstepObj.getLsprotocolstepInfo());

				mongoTemplate.upsert(query, update, LSprotocolstepInfo.class);

				if (LSprotocolstepObj.getNewStep() != 1) {
					LSprotocolmaster protocolmaster = LSProtocolMasterRepositoryObj
							.findByprotocolmastercode(LSprotocolstepObj.getProtocolmastercode());
					mapObj.put("protocolmaster", protocolmaster);
					List<LSprotocolversion> LSprotocolversionlst = lsprotocolversionRepository
							.findByprotocolmastercode(LSprotocolstepObj.getProtocolmastercode());

					Collections.sort(LSprotocolversionlst, Collections.reverseOrder());

					mapObj.put("LSprotocolversionlst", LSprotocolversionlst);
				}
			}

			List<LSprotocolstep> tempLSprotocolstepLst = LSProtocolStepRepositoryObj
					.findByProtocolmastercodeAndStatus(LSprotocolstepObj.getProtocolmastercode(), 1);
			List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();
//				for(LSprotocolstep LSprotocolstepObj1: tempLSprotocolstepLst) {
			if (LSprotocolstepObj.getIsmultitenant() == 1 || LSprotocolstepObj.getIsmultitenant() == 2) {
				if (LSprotocolstepObj.getNewStep() == 1) {
					LSprotocolstepObj.setLsprotocolstepInfo(CloudLSprotocolstepInfoObj.getLsprotocolstepInfo());
					LSprotocolstepLst.add(LSprotocolstepObj);
				} else {
					CloudLSprotocolstepInfo newLSprotocolstepInfo = CloudLSprotocolstepInfoRepository
							.findById(LSprotocolstepObj.getProtocolstepcode());
					if (newLSprotocolstepInfo != null) {
//								if(LSprotocolstepObj1.getProtocolstepcode()== newLSprotocolstepInfo.getId()) {
						LSprotocolstepObj.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
//								}
					}
					LSprotocolstepLst.add(LSprotocolstepObj);
				}
			} else {
				LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate
						.findById(LSprotocolstepObj.getProtocolstepcode(), LSprotocolstepInfo.class);
				if (newLSprotocolstepInfo != null) {
					LSprotocolstepObj.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
				}
				LSprotocolstepLst.add(LSprotocolstepObj);
			}

//				}
//			if (argObj.containsKey("modifiedsamplestep")) {
//				LSprotocolsampleupdates sample = new ObjectMapper().convertValue(argObj.get("modifiedsamplestep"),
//						new TypeReference<LSprotocolsampleupdates>() {
//						});
//				sample.setProtocolstepcode(LSprotocolstepObj.getProtocolstepcode());
//				sample.setProtocolmastercode(LSprotocolstepObj.getProtocolmastercode());
//				LSprotocolsampleupdatesRepository.save(sample);
//			}
			if (argObj.containsKey("modifiedsamplestep")) {
				List<LSprotocolsampleupdates> lsprotocolsampleupdates = new ObjectMapper().convertValue(
						argObj.get("modifiedsamplestep"), new TypeReference<List<LSprotocolsampleupdates>>() {
						});
				for (LSprotocolsampleupdates lSprotocolsampleupdates : lsprotocolsampleupdates) {

					LSprotocolsampleupdates sample = lSprotocolsampleupdates;
//					if(sample.getProtocolstepcode() != null) {
					sample.setProtocolstepcode(LSprotocolstepObj.getProtocolstepcode());
					sample.setProtocolmastercode(LSprotocolstepObj.getProtocolmastercode());
//					}
					LSprotocolsampleupdatesRepository.save(sample);
				}

			}
			if (argObj.containsKey("repositorydata"))

			{
				List<Lsrepositoriesdata> lsrepositoriesdata = new ObjectMapper()
						.convertValue(argObj.get("repositorydata"), new TypeReference<List<Lsrepositoriesdata>>() {
						});
//				Lsrepositoriesdata lsrepositoriesdata = new ObjectMapper().convertValue(argObj.get("repositorydata"),
//						new TypeReference<Lsrepositoriesdata>() {
//						});
				for (Lsrepositoriesdata lsrepositoriesdataobj : lsrepositoriesdata) {
					LsrepositoriesdataRepository.save(lsrepositoriesdataobj);
				}

			}
			response.setStatus(true);
			response.setInformation("");
			mapObj.put("response", response);
			mapObj.put("protocolstepLst", LSprotocolstepLst);
		}

		return mapObj;
	}

	private void updateCloudProtocolVersiononSQL(LSprotocolstep lSprotocolstepObj, Integer sitecode, String usercode,
			Integer usercode1) {

		LSprotocolmaster protocolMaster = LSProtocolMasterRepositoryObj
				.findByprotocolmastercode(lSprotocolstepObj.getProtocolmastercode());
		List<LSprotocolstep> lststep = LSProtocolStepRepositoryObj
				.findByProtocolmastercodeAndStatus(lSprotocolstepObj.getProtocolmastercode(), 1);

		if ((protocolMaster.getApproved() != null && protocolMaster.getApproved() == 1
				&& lSprotocolstepObj.getNewStep() != 1)
				|| (protocolMaster.getApproved() != null && protocolMaster.getApproved() == 1
						&& lSprotocolstepObj.getNewStep() == 1)) {

			LSSiteMaster lssitemaster = LSSiteMasterRepository.findBysitecode(sitecode);
//			LSprotocolworkflow lsprotocolworkflow = lSprotocolworkflowRepository
//					.findTopByAndLssitemasterOrderByWorkflowcodeAsc(lssitemaster);

//			LSsheetworkflow lssheetworkflow = lssheetworkflowRepository
//					.findTopByAndLssitemasterOrderByWorkflowcodeAsc(lssitemaster);

			ElnprotocolTemplateworkflow lssheetworkflow = elnprotocolTemplateworkflowRepository
					.findTopByAndLssitemasterAndStatusOrderByWorkflowcodeAsc(lssitemaster, 1);

			protocolMaster.setApproved(0);
//			protocolMaster.setlSprotocolworkflow(lsprotocolworkflow);
			protocolMaster.setElnprotocoltemplateworkflow(lssheetworkflow);
			protocolMaster.setVersionno(protocolMaster.getVersionno() + 1);

			LSProtocolMasterRepositoryObj.save(protocolMaster);

			int i = 0;
//			List<LSprotocolstepversion> lstVersStep = new ArrayList<LSprotocolstepversion>();

			while (i < lststep.size()) {

				LSprotocolstepversion protoVersStep = new LSprotocolstepversion();
				LSprotocolversionstepInfo LsLogilabprotocolstepInfoObj = new LSprotocolversionstepInfo();
				protoVersStep.setProtocolmastercode(lststep.get(i).getProtocolmastercode());
				protoVersStep.setProtocolstepcode(lststep.get(i).getProtocolstepcode());
				protoVersStep.setProtocolstepname(lststep.get(i).getProtocolstepname());
				protoVersStep.setStatus(lststep.get(i).getStatus());
				protoVersStep.setStepno(lststep.get(i).getStepno());
				protoVersStep.setVersionno(protocolMaster.getVersionno());

				LSprotocolstepversionRepository.save(protoVersStep);

				if (lststep.get(i).getProtocolstepcode().equals(lSprotocolstepObj.getProtocolstepcode())) {
					LsLogilabprotocolstepInfoObj.setContent(lSprotocolstepObj.getLsprotocolstepInformation());
				} else {
					LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate
							.findById(protoVersStep.getProtocolstepcode(), LSprotocolstepInfo.class);
					if (newLSprotocolstepInfo != null) {
						LsLogilabprotocolstepInfoObj.setContent(newLSprotocolstepInfo.getContent());
					}
				}
				LsLogilabprotocolstepInfoObj.setId(protoVersStep.getProtocolstepversioncode());
				LsLogilabprotocolstepInfoObj.setStepcode(lststep.get(i).getProtocolstepcode());
				LsLogilabprotocolstepInfoObj.setVersionno(protocolMaster.getVersionno());
				mongoTemplate.insert(LsLogilabprotocolstepInfoObj);
				i++;
			}
			LSprotocolversion versProto = new LSprotocolversion();

			versProto.setProtocolmastercode(lSprotocolstepObj.getProtocolmastercode());
			versProto.setProtocolmastername(protocolMaster.getProtocolmastername());
			versProto.setProtocolstatus(1);
			versProto.setVersionno(protocolMaster.getVersionno());
			versProto.setVersionname("version_" + protocolMaster.getVersionno());
			try {
				versProto.setCreatedate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (usercode != "" && usercode1 != 0) {
				versProto.setCreatedbyusername(usercode);
				versProto.setCreatedby(usercode1);
			}

			lsprotocolversionRepository.save(versProto);

		} else {
			if (lSprotocolstepObj.getNewStep() == 1) {

				LSprotocolstepversion protoVersStep = new LSprotocolstepversion();

				protoVersStep.setProtocolmastercode(lSprotocolstepObj.getProtocolmastercode());
				protoVersStep.setProtocolstepcode(lSprotocolstepObj.getProtocolstepcode());
				protoVersStep.setProtocolstepname(lSprotocolstepObj.getProtocolstepname());
				protoVersStep.setStatus(lSprotocolstepObj.getStatus());
				protoVersStep.setStepno(lSprotocolstepObj.getStepno());
				protoVersStep.setVersionno(protocolMaster.getVersionno());

				LSprotocolstepversionRepository.save(protoVersStep);

				LSprotocolversionstepInfo LsLogilabprotocolstepInfoObj = new LSprotocolversionstepInfo();

				LsLogilabprotocolstepInfoObj.setId(protoVersStep.getProtocolstepversioncode());
				LsLogilabprotocolstepInfoObj.setStepcode(lSprotocolstepObj.getProtocolstepcode());
				LsLogilabprotocolstepInfoObj.setContent(lSprotocolstepObj.getLsprotocolstepInformation());
				LsLogilabprotocolstepInfoObj.setVersionno(protocolMaster.getVersionno());

				mongoTemplate.insert(LsLogilabprotocolstepInfoObj);
			} else {

				LSprotocolstepversion protocolStep = LSprotocolstepversionRepository.findByprotocolstepcodeAndVersionno(
						lSprotocolstepObj.getProtocolstepcode(), protocolMaster.getVersionno());
				if (protocolStep != null) {
					Query query = new Query(Criteria.where("id").is(protocolStep.getProtocolstepversioncode()));

					Update update = new Update();
					update.set("content", lSprotocolstepObj.getLsprotocolstepInformation());

					mongoTemplate.upsert(query, update, LSprotocolversionstepInfo.class);
				}
			}
		}
	}

	private void updateCloudProtocolVersion(Integer protocolmastercode, Integer protocolstepcode,
			String lsprotocolstepInfo, Integer newStep, Integer sitecode, LSprotocolstep lSprotocolstepObj,
			String usercode, Integer usercode1, LScfttransaction objaudit1) {

		LSprotocolmaster protocolMaster = LSProtocolMasterRepositoryObj.findByprotocolmastercode(protocolmastercode);
		List<LSprotocolstep> lststep = LSProtocolStepRepositoryObj.findByProtocolmastercode(protocolmastercode);
		if ((protocolMaster.getApproved() != null && protocolMaster.getApproved() == 1 && newStep != 1)
				|| (protocolMaster.getApproved() != null && protocolMaster.getApproved() == 1 && newStep == 1)) {

			LSSiteMaster lssitemaster = LSSiteMasterRepository.findBysitecode(sitecode);
//			LSprotocolworkflow lsprotocolworkflow = lSprotocolworkflowRepository
//					.findTopByAndLssitemasterOrderByWorkflowcodeAsc(lssitemaster);.

			ElnprotocolTemplateworkflow lssheetworkflow = elnprotocolTemplateworkflowRepository
					.findTopByAndLssitemasterAndStatusOrderByWorkflowcodeAsc(lssitemaster, 1);

			protocolMaster.setApproved(0);
//			protocolMaster.setlSprotocolworkflow(lsprotocolworkflow);
			protocolMaster.setElnprotocoltemplateworkflow(lssheetworkflow);
			protocolMaster.setVersionno(protocolMaster.getVersionno() + 1);

			LSProtocolMasterRepositoryObj.save(protocolMaster);
			List<LSprotocolmastertest> LSprotocolmastertest = protocolMaster.getLstest();
			for (LSprotocolmastertest test : LSprotocolmastertest) {
				test.setTestcode(null);
				LSprotocolmastertestRepository.save(test);
			}

			int i = 0;
			List<LSprotocolstepversion> lstVersStep = new ArrayList<LSprotocolstepversion>();
			List<CloudLSprotocolversionstep> lstcloudStepVersion = new ArrayList<CloudLSprotocolversionstep>();

			while (i < lststep.size()) {

				LSprotocolstepversion protoVersStep = new LSprotocolstepversion();
				CloudLSprotocolversionstep cloudStepVersion = new CloudLSprotocolversionstep();

				protoVersStep.setProtocolmastercode(lststep.get(i).getProtocolmastercode());
				protoVersStep.setProtocolstepcode(lststep.get(i).getProtocolstepcode());
				protoVersStep.setProtocolstepname(lststep.get(i).getProtocolstepname());
				protoVersStep.setStatus(lststep.get(i).getStatus());
				protoVersStep.setStepno(lststep.get(i).getStepno());
				protoVersStep.setVersionno(protocolMaster.getVersionno());

				LSprotocolstepversionRepository.save(protoVersStep);

				cloudStepVersion.setId(protoVersStep.getProtocolstepversioncode());
				cloudStepVersion.setProtocolmastercode(protocolmastercode);

				if (protocolstepcode.equals(lststep.get(i).getProtocolstepcode())) {
					cloudStepVersion.setLsprotocolstepInfo(lsprotocolstepInfo);
				} else {
//					CloudLSprotocolstepInfo newLSprotocolstepInfo = CloudLSprotocolstepInfoRepository
//							.findById(protocolstepcode);
					LSprotocolstepInformation newLSprotocolstepInfo = lsprotocolstepInformationRepository
							.findById(lststep.get(i).getProtocolstepcode());
					if (newLSprotocolstepInfo != null) {
						cloudStepVersion.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
					}
				}

				cloudStepVersion.setVersionname("version_" + protocolMaster.getVersionno());
				cloudStepVersion.setVersionno(protocolMaster.getVersionno());

				lstVersStep.add(protoVersStep);
				lstcloudStepVersion.add(cloudStepVersion);
				i++;
			}

			CloudLSprotocolversionstepRepository.save(lstcloudStepVersion);

			LSprotocolversion versProto = new LSprotocolversion();

			versProto.setProtocolmastercode(protocolmastercode);
			versProto.setProtocolmastername(protocolMaster.getProtocolmastername());
			versProto.setProtocolstatus(1);
			versProto.setVersionno(protocolMaster.getVersionno());
			versProto.setVersionname("version_" + protocolMaster.getVersionno());
			try {
				versProto.setCreatedate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (usercode != "" && usercode1 != 0) {
				versProto.setCreatedbyusername(usercode);
				versProto.setCreatedby(usercode1);
			}
			lsprotocolversionRepository.save(versProto);
			if (objaudit1 != null && objaudit1.getLssitemaster() != null) {
				lscfttransactionRepository.save(objaudit1);
			}
		} else {

			if (newStep == 1) {
				CloudLSprotocolversionstep cloudStepVersion = new CloudLSprotocolversionstep();

				LSprotocolstepversion protoVersStep = new LSprotocolstepversion();

				protoVersStep.setProtocolmastercode(lSprotocolstepObj.getProtocolmastercode());
				protoVersStep.setProtocolstepcode(lSprotocolstepObj.getProtocolstepcode());
				protoVersStep.setProtocolstepname(lSprotocolstepObj.getProtocolstepname());
				protoVersStep.setStatus(lSprotocolstepObj.getStatus());
				protoVersStep.setStepno(lSprotocolstepObj.getStepno());
				protoVersStep.setVersionno(protocolMaster.getVersionno());

				LSprotocolstepversionRepository.save(protoVersStep);

				cloudStepVersion.setId(protoVersStep.getProtocolstepversioncode());
				cloudStepVersion.setProtocolmastercode(protocolmastercode);
				cloudStepVersion.setLsprotocolstepInfo(lsprotocolstepInfo);
				cloudStepVersion.setVersionno(protocolMaster.getVersionno());
				cloudStepVersion.setVersionname("version_" + protocolMaster.getVersionno());

				CloudLSprotocolversionstepRepository.save(cloudStepVersion);
			} else {

				LSprotocolstepversion protocolStep = LSprotocolstepversionRepository.findByprotocolstepcodeAndVersionno(
						lSprotocolstepObj.getProtocolstepcode(), protocolMaster.getVersionno());
				if (protocolStep != null) {
					CloudLSprotocolversionstep cloudStepVersion = CloudLSprotocolversionstepRepository
							.findById(protocolStep.getProtocolstepversioncode());

					cloudStepVersion.setLsprotocolstepInfo(lsprotocolstepInfo);

					CloudLSprotocolversionstepRepository.save(cloudStepVersion);
				}

			}
		}
	}

	public Map<String, Object> deleteProtocolStep(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();

		LSprotocolstep deleteprotocolstep = new ObjectMapper().convertValue(argObj.get("deleteprotocolstep"),
				new TypeReference<LSprotocolstep>() {
				});

//		List<LSprotocolstep> updateLSprotocolstepLst = new ObjectMapper().convertValue(argObj.get("protocolstepLst"),
//				new TypeReference<List<LSprotocolstep>>() {
//				});
//		for (LSprotocolstep LSprotocolstepObj1 : updateLSprotocolstepLst) {
		LSProtocolStepRepositoryObj.save(deleteprotocolstep);
//		}

		List<LSprotocolstep> tempLSprotocolstepLst = LSProtocolStepRepositoryObj
				.findByProtocolmastercodeAndStatus((Integer) argObj.get("protocolmastercode"), 1);
//		List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();
//		for (LSprotocolstep LSprotocolstepObj1 : tempLSprotocolstepLst) {
//
//			if (deleteprotocolstep.getIsmultitenant() == 1) {
//
//				LSprotocolstepInformation newobj = lsprotocolstepInformationRepository
//						.findById(LSprotocolstepObj1.getProtocolstepcode());
//				if (newobj != null) {
//					LSprotocolstepObj1.setLsprotocolstepInformation(newobj.getLsprotocolstepInfo());
//				}
//			} else {
//				LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate
//						.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
//				if (newLSprotocolstepInfo != null) {
//					LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
//					LSprotocolstepObj1.setLsprotocolstepInformation(newLSprotocolstepInfo.getContent());
//
//				}
//			}
//
//			LSprotocolstepLst.add(LSprotocolstepObj1);
//		}
		ObjectMapper object = new ObjectMapper();
		LSprotocolupdates lSprotocolupdates = object.convertValue(argObj.get("LSprotocolupdates"),
				LSprotocolupdates.class);
		lsprotocolupdatesRepository.save(lSprotocolupdates);
		mapObj.put("protocoloverallstepLst", tempLSprotocolstepLst);
		mapObj.put("protocolstepLst", deleteprotocolstep);

		return mapObj;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public Map<String, Object> addProtocolMaster(Map<String, Object> argObj) throws IOException, ParseException {
		Map<String, Object> mapObj = new HashMap<String, Object>();

		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if (argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
		}
		ObjectMapper objMapper = new ObjectMapper();
		Response response = new Response();
		Number protocolcode = null;
		if (argObj.containsKey("newProtocolMasterObj")) {
			LSuserMaster LsuserMasterObj = LSuserMasterRepositoryObj
					.findByusercode(LScfttransactionobj.getLsuserMaster());
			LSprotocolmaster newProtocolMasterObj = new LSprotocolmaster();
			if (argObj.containsKey("edit")) {

				int protocolmastercode = new ObjectMapper().convertValue(argObj.get("protocolmastercode"),
						Integer.class);
				String pname = (String) argObj.get("protocolmastername");
				List<LSprotocolmaster> lstpmobj = LSProtocolMasterRepositoryObj
						.findByProtocolmastercodeNotAndRetirestatusAndProtocolmasternameIgnoreCase(protocolmastercode,
								0, pname);
				if (lstpmobj != null && lstpmobj.size() != 0) {
					response.setStatus(false);
					response.setInformation("IDS_MSG_ALREADY");
					mapObj.put("response", response);
					return mapObj;
				}
				newProtocolMasterObj = LSProtocolMasterRepositoryObj
						.findFirstByProtocolmastercodeAndStatusAndLssitemaster(protocolmastercode, 1,
								LScfttransactionobj.getLssitemaster());
				newProtocolMasterObj.setProtocolmastername((String) argObj.get("protocolmastername"));
				newProtocolMasterObj.setViewoption((Integer) argObj.get("viewoption"));
				newProtocolMasterObj.setIsmultitenant((Integer) argObj.get("ismultitenant"));
				newProtocolMasterObj.setCategory((String) argObj.get("category"));
				newProtocolMasterObj.setLastmodified(commonfunction.getCurrentUtcTime());
				newProtocolMasterObj.setModifiedby((String) argObj.get("modifiedby"));
				Object LSprotocolupdates = new LSprotocolupdates();
				Map<String, Object> argObj1 = new HashMap<String, Object>();
				argObj1 = (Map<String, Object>) argObj.get("LSprotocolupdates");
//				UpdateProtocolversion(newProtocolMasterObj, argObj1, LSprotocolupdates.class);
			} else {
				try {
					newProtocolMasterObj.setCreatedate(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				List<LSprotocolmaster> lstpmobj = LSProtocolMasterRepositoryObj
						.findByRetirestatusAndProtocolmasternameIgnoreCaseAndLssitemaster(0,
								(String) argObj.get("protocolmastername"), LScfttransactionobj.getLssitemaster());

//					if (LSProtocolMasterRepositoryObj.findByProtocolmasternameIgnoreCaseAndLssitemasterAndRetirestatus(
//							argObj.get("protocolmastername").toString().trim(),
//							LScfttransactionobj.getLssitemaster(),0) != null) {
				if (lstpmobj != null && lstpmobj.size() > 0) {
					response.setStatus(false);
					response.setInformation("IDS_MSG_ALREADY");
					mapObj.put("response", response);
					return mapObj;
				}
				newProtocolMasterObj.setProtocolmastername((String) argObj.get("protocolmastername"));
				if(argObj.get("protocoldatainfo") != null && argObj.get("ismultitenant").equals(2)) {					
					Gson gson = new Gson();
					String Content = gson.toJson(argObj.get("protocoldatainfo"));
				newProtocolMasterObj.setProtocoldatainfo(Content);
				}
				newProtocolMasterObj.setProtocolstatus((Integer) argObj.get("protocolstatus"));
				newProtocolMasterObj.setStatus((Integer) argObj.get("status"));
				newProtocolMasterObj.setCreatedby((Integer) argObj.get("createdby"));
				newProtocolMasterObj.setIsmultitenant((Integer) argObj.get("ismultitenant"));

				LSprotocolmaster obj = objMapper.convertValue(argObj.get("LSprotocolmaster"), LSprotocolmaster.class);
//				newProtocolMasterObj.setCreatedate(obj.getCreatedate());
				newProtocolMasterObj.setLssitemaster(LScfttransactionobj.getLssitemaster());
				newProtocolMasterObj.setCreatedbyusername(LsuserMasterObj.getUsername());
				newProtocolMasterObj.setVersionno(1);
				newProtocolMasterObj.setViewoption((Integer) argObj.get("viewoption"));
				newProtocolMasterObj.setCategory((String) argObj.get("category"));
				LSSiteMaster lssitemaster = LSSiteMasterRepository
						.findBysitecode(LScfttransactionobj.getLssitemaster());
//				LSprotocolworkflow lsprotocolworkflow = lSprotocolworkflowRepository
//						.findTopByAndLssitemasterOrderByWorkflowcodeAsc(lssitemaster);
				if(argObj.get("liteworkflow") != null && argObj.get("ismultitenant").equals(2)) {
					newProtocolMasterObj.setElnprotocoltemplateworkflow(elnprotocolTemplateworkflowRepository
							.findTopByAndLssitemasterAndStatusOrderByWorkflowcodeDesc(lssitemaster, 1));
				}
				if (newProtocolMasterObj.getElnprotocoltemplateworkflow() == null) {
					newProtocolMasterObj.setElnprotocoltemplateworkflow(elnprotocolTemplateworkflowRepository
							.findTopByAndLssitemasterAndStatusOrderByWorkflowcodeAsc(lssitemaster, 1));
				}
				
				
//				newProtocolMasterObj.setlSprotocolworkflow(lsprotocolworkflow);

			}
			int retirestatus = (int) argObj.get("retirestatus");
			newProtocolMasterObj.setRetirestatus(retirestatus);
			newProtocolMasterObj.setRetirestatus(retirestatus);
			if (newProtocolMasterObj.getIsmultitenant() != null && newProtocolMasterObj.getIsmultitenant().equals(2)
					&& argObj.get("approved") != null) {
				newProtocolMasterObj.setApproved((Integer) argObj.get("approved"));
			}
			
			if(argObj.containsKey("lstest"))
			{
//				List<LSprotocolmastertest> lstprojecttest = new ObjectMapper().convertValue(argObj.get("lstest"), ArrayList<LSprotocolmastertest.class>());
				List<LSprotocolmastertest> lstprojecttest = new ObjectMapper().convertValue(argObj.get("lstest"), new TypeReference<List<LSprotocolmastertest>>(){});
				newProtocolMasterObj.setLstest(lstprojecttest);
				LSprotocolmastertestRepository.save(newProtocolMasterObj.getLstest());
			}
			if(argObj.containsKey("testcode") && argObj.get("testcode") != null) {
				int testcode = (int) argObj.get("testcode");
				LStestmasterlocal lstest = lstestmasterlocalRepository.findBytestcode(testcode);
				newProtocolMasterObj.setTask(lstest.getTestname());
			}
			

			LSProtocolMasterRepositoryObj.save(newProtocolMasterObj);
			protocolcode = newProtocolMasterObj.getProtocolmastercode();
			LsActiveWidgets lsactvewidgobj = new LsActiveWidgets();
			lsactvewidgobj.setActivewidgetsdetails(newProtocolMasterObj.getProtocolmastername());
			lsactvewidgobj.setActivewidgetsdetailscode(Long.valueOf(newProtocolMasterObj.getProtocolmastercode()));
			lsactvewidgobj.setActivityType("Insert");
			lsactvewidgobj.setScreenname("Protocol_Template");
			lsactvewidgobj.setUserId(newProtocolMasterObj.getCreatedby());
			try {
				lsactvewidgobj.setActivedatatimestamp(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			lsActiveWidgetsRepository.save(lsactvewidgobj);
			if (argObj.containsKey("edit")) {
			} else {
				LSprotocolversion versProto = new LSprotocolversion();

				versProto.setCreatedate(newProtocolMasterObj.getCreatedate());
				versProto.setCreatedby(newProtocolMasterObj.getCreatedby());
				versProto.setCreatedbyusername(newProtocolMasterObj.getCreatedbyusername());
				versProto.setProtocolmastercode(newProtocolMasterObj.getProtocolmastercode());
				versProto.setProtocolmastername(newProtocolMasterObj.getProtocolmastername());
				versProto.setProtocolstatus(1);
				versProto.setVersionno(newProtocolMasterObj.getVersionno());
				versProto.setVersionname("version_" + newProtocolMasterObj.getVersionno());

				lsprotocolversionRepository.save(versProto);
			}

			List<LSprotocolmaster> AddedLSprotocolmasterObj = LSProtocolMasterRepositoryObj
					.findByStatusAndLssitemasterAndProtocolmasternameAndRetirestatus(1,
							LScfttransactionobj.getLssitemaster(), newProtocolMasterObj.getProtocolmastername(), 0);

			LSprotocolworkflow lsprotocolworkflow = new LSprotocolworkflow();
			List<LSprotocolmaster> LSprotocolmasterLst = new ArrayList<LSprotocolmaster>();
			List<Integer> lstuser = new ObjectMapper().convertValue(argObj.get("teamuserscode"), ArrayList.class);
//			if (lstuser.size() != 0) {
//				LSprotocolmasterLst = LSProtocolMasterRepositoryObj
//						.findByCreatedbyInAndStatusAndLssitemasterOrderByCreatedateDesc(lstuser, 1,
//								LScfttransactionobj.getLssitemaster());
//			} else {
//				Integer usercode = objMapper.convertValue(argObj.get("usercode"), Integer.class);
//				LSprotocolmasterLst = LSProtocolMasterRepositoryObj
//						.findByCreatedbyInAndStatusAndLssitemasterOrderByCreatedateDesc(usercode, 1,
//								LScfttransactionobj.getLssitemaster());
//			}
			Integer usercode = objMapper.convertValue(argObj.get("usercode"), Integer.class);
			if (lstuser.size() != 0) {
				LSprotocolmasterLst = LSProtocolMasterRepositoryObj
						.findByStatusAndLssitemasterAndViewoptionOrCreatedbyAndStatusAndLssitemasterAndViewoptionOrCreatedbyInAndStatusAndLssitemasterAndViewoptionOrderByCreatedateDesc(
								1, LScfttransactionobj.getLssitemaster(), 1, usercode, 1,
								LScfttransactionobj.getLssitemaster(), 2, lstuser, 1,
								LScfttransactionobj.getLssitemaster(), 3);

			} else {
				LSprotocolmasterLst = LSProtocolMasterRepositoryObj
						.findByStatusAndLssitemasterAndViewoptionOrCreatedbyAndStatusAndLssitemasterAndViewoptionOrderByCreatedateDesc(
								1, LScfttransactionobj.getLssitemaster(), 1, usercode, 1,
								LScfttransactionobj.getLssitemaster(), 2);

			}

			if (argObj.containsKey("multiusergroups")) {

				int lsusergroupcode = objMapper.convertValue(argObj.get("multiusergroups"), Integer.class);

				LSusergroup lsusergroup = LSusergroupRepository.findOne(lsusergroupcode);

				List<LSprotocolworkflowgroupmap> lsprotocolworkflowgroupmap = LSprotocolworkflowgroupmapRepository
						.findBylsusergroupAndWorkflowcodeNotNull(lsusergroup);

				if (lsprotocolworkflowgroupmap != null && lsprotocolworkflowgroupmap.size() > 0) {
					lsprotocolworkflow = lSprotocolworkflowRepository
							.findByworkflowcode(lsprotocolworkflowgroupmap.get(0).getWorkflowcode());

				}
			}
			List<LSprotocolstep> LSprotocolsteplst = LSProtocolStepRepositoryObj
					.findByProtocolmastercodeAndStatus(newProtocolMasterObj.getProtocolmastercode(), 1);
			List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();
			for (LSprotocolstep LSprotocolstepObj1 : LSprotocolsteplst) {
				if (newProtocolMasterObj.getIsmultitenant() == 1 || newProtocolMasterObj.getIsmultitenant() == 2) {
					LSprotocolstepInformation newLSprotocolstepInfo = lsprotocolstepInformationRepository
							.findById(LSprotocolstepObj1.getProtocolstepcode());
					if (newLSprotocolstepInfo != null) {
//						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
						LSprotocolstepObj1.setLsprotocolstepInformation(newLSprotocolstepInfo.getLsprotocolstepInfo());
					}
				} else {
					LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate
							.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
					if (newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
						LSprotocolstepObj1.setLsprotocolstepInformation(newLSprotocolstepInfo.getContent());
					}
				}
				LSprotocolstepLst.add(LSprotocolstepObj1);
			}

			if (argObj.containsKey("protocolData")) {
				Integer isMultitenant = (Integer) argObj.get("ismultitenant");
				Gson gson = new Gson();
				String protocolDataJson = gson.toJson(argObj.get("protocolData"));

				if (isMultitenant == 1 || isMultitenant == 2) {
					commonservice.updateProtocolContent(protocolDataJson, newProtocolMasterObj);
				} else {
//					Lsprotocoltemplatedata lsprotocoldata = new Lsprotocoltemplatedata();
//					lsprotocoldata.setId(newProtocolMasterObj.getProtocolmastercode());
//					lsprotocoldata.setContent(protocolDataJson);
//					mongoTemplate.insert(lsprotocoldata);
					if (gridFsTemplate.findOne(new Query(Criteria.where("filename")
							.is("protocol_" + newProtocolMasterObj.getProtocolmastercode()))) == null) {
						try {
							gridFsTemplate.store(
									new ByteArrayInputStream(protocolDataJson.getBytes(StandardCharsets.UTF_8)),
									"protocol_" + newProtocolMasterObj.getProtocolmastercode(),
									StandardCharsets.UTF_16);
						} catch (Exception e) {
							System.out.println("error protocoldata lsprotocolmaster content update mongodb"
									+ newProtocolMasterObj.getProtocolmastercode());
						}
					}
				}
				mapObj.put("ProtocolData", protocolDataJson);
			}

			if (argObj.containsKey("edit")) {
				Map<String, Object> argObj1 = new HashMap<String, Object>();
				argObj1.put("objsilentaudit", argObj.get("objsilentaudit"));
//					argObj1.put("ProtocolMasterObj", argObj.get("newProtocolMasterObj"));
				argObj1.put("protocolmastercode", newProtocolMasterObj.getProtocolmastercode());
				argObj1.put("ismultitenant", newProtocolMasterObj.getIsmultitenant());
				Map<String, Object> ProtocolStepLstMap = getProtocolStepLst(argObj1);
				mapObj.put("protocolstepLst", ProtocolStepLstMap.get("protocolstepLst"));

				if (argObj.containsKey("modifiedlist")) {
					lsprotocolupdatesRepository
							.save(objMapper.convertValue(argObj.get("modifiedlist"), LSprotocolupdates.class));
				}
				String versiondetails = "";
				if (argObj.containsKey("versiondetails")) {
					versiondetails = objMapper.convertValue(argObj.get("versiondetails"), String.class);
				}
				CloudLSprotocolversionstep CloudLSprotocolversionstep = new CloudLSprotocolversionstep();
				LSprotocolversion lsprotocolversion = lsprotocolversionRepository
						.findFirstByProtocolmastercodeOrderByVersionnoDesc(
								newProtocolMasterObj.getProtocolmastercode());

			} else {
				List<Integer> teamCodeLst = LSuserteammappingRepositoryObj
						.getTeamcodeByLsuserMaster4postgressandsql(LScfttransactionobj.getLsuserMaster());

				if (teamCodeLst.size() > 0) {
					List<LSuserMaster> lsusermasterLst = LSuserteammappingRepositoryObj
							.getLsuserMasterByTeamcode(teamCodeLst);
//						LSprotocolmasterLst.get(0).setCreateby(lsusermasterLst);
					if (lsusermasterLst.size() > 0) {
						for (LSuserMaster lsusermasterObj : lsusermasterLst) {
							List<LSprotocolmaster> LSprotocolmasterTempLst = new ArrayList<>();
							if (lsprotocolworkflow.getWorkflowname() != null) {
								LSprotocolmasterTempLst = LSProtocolMasterRepositoryObj
										.findByCreatedbyNotAndStatusAndLssitemasterAndSharewithteamAndLSprotocolworkflowNot(
												lsusermasterObj.getUsercode(), 1,
												lsusermasterObj.getLssitemaster().getSitecode(), 1, lsprotocolworkflow);
							} else {
								LSprotocolmasterTempLst = LSProtocolMasterRepositoryObj
										.findByCreatedbyNotAndStatusAndLssitemasterAndSharewithteam(
												lsusermasterObj.getUsercode(), 1,
												lsusermasterObj.getLssitemaster().getSitecode(), 1);
							}
							if (LSprotocolmasterTempLst.size() > 0) {
								LSprotocolmasterLst.addAll(LSprotocolmasterTempLst);
							}
						}
					}
				}
				mapObj.put("protocolstepLst", new ArrayList<Object>());
			}
			LSprotocolmasterLst = LSprotocolmasterLst.stream().distinct().collect(Collectors.toList());

			Collections.sort(LSprotocolmasterLst, Collections.reverseOrder());
			response.setStatus(true);
			response.setInformation("");

			List<LSprotocolversion> LSprotocolversionlst = lsprotocolversionRepository.findAll();
			mapObj.put("LSprotocolversionlst", LSprotocolversionlst);
			mapObj.put("protocolmasterLst", LSprotocolmasterLst);
			mapObj.put("AddedLSprotocolmasterObj", AddedLSprotocolmasterObj);
//			mapObj.put("ProtocolData",objCloudFileManipulationservice.retrieveCloudSheets(newProtocolMasterObj.getFileuid(),
//					TenantContext.getCurrentTenant() + "protocol"));
			mapObj.put("response", response);
			updatenotificationforprotocolcreation(newProtocolMasterObj, argObj, false);

		}
		argObj.put("protocolmastercode", protocolcode);
		return mapObj;
	}

//	@SuppressWarnings("unchecked")
	private void updatenotificationforprotocolcreation(LSprotocolmaster newProtocolMasterObj,
			Map<String, Object> argObj, Boolean Isnewprotocol) {
		LSuserMaster lsusermaster = new ObjectMapper().convertValue(argObj.get("lsuserMaster"),
				new TypeReference<LSuserMaster>() {
				});
		newProtocolMasterObj.setLSuserMaster(lsusermaster);
		List<LSuserteammapping> objteam = LSuserteammappingRepositoryObj
				.findByTeamcodeNotNullAndLsuserMaster(lsusermaster);
		String Details = "";
		String Notifiction = "";
		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
		Notifiction = "PROTOCOLCREATED";
		Details = "{\"order\":\"" + newProtocolMasterObj.getProtocolmastername() + "\", \"ordercode\":\""
				+ newProtocolMasterObj.getProtocolmastercode() + "\", \"createduser\":\""
				+ newProtocolMasterObj.getCreatedbyusername() + "\"}";
		List<LSuserMaster> lstnotified = new ArrayList<LSuserMaster>();

		for (int i = 0; i < objteam.size(); i++) {
			LSusersteam objteam1 = lsusersteamRepository.findByteamcode(objteam.get(i).getTeamcode());

//			List<LSuserteammapping> lstusers = objteam1.getLsuserteammapping();
			List<LSuserteammapping> lstusers = LSuserteammappingRepositoryObj.findByteamcode(objteam1.getTeamcode());
			if (lstusers != null) {
				for (int j = 0; j < lstusers.size(); j++) {

					if (newProtocolMasterObj.getLSuserMaster().getUsercode().intValue() != lstusers.get(j).getLsuserMaster()
							.getUsercode().intValue()) {
						if (lstnotified.contains(lstusers.get(j).getLsuserMaster()))
							continue;

						lstnotified.add(lstusers.get(j).getLsuserMaster());
						LSnotification objnotify = new LSnotification();

						objnotify.setNotificationdate(newProtocolMasterObj.getCreatedate());

						objnotify.setNotifationfrom(newProtocolMasterObj.getLSuserMaster());
						objnotify.setNotifationto(lstusers.get(j).getLsuserMaster());
						objnotify.setNotificationdate(newProtocolMasterObj.getCreatedate());
						objnotify.setNotification(Notifiction);
						objnotify.setNotificationdetils(Details);
						objnotify.setIsnewnotification(1);
						objnotify.setNotificationpath("/protocols");
						objnotify.setNotificationfor(2);
						lstnotifications.add(objnotify);
					}
				}
			}
			lsnotificationRepository.save(lstnotifications);

		}
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private boolean UpdateProtocolversion(LSprotocolmaster newProtocolMasterObj1, Map<String, Object> argObj1,
			Class<LSprotocolupdates> class1) {

		int Versionnumber = 0;

		Map<String, Object> mapObj = new HashMap<String, Object>();
		LSprotocolversion objLatestversion = lsprotocolversionRepository
				.findFirstByProtocolmastercodeOrderByVersionnoDesc(newProtocolMasterObj1.getProtocolmastercode());
		if (objLatestversion != null) {
			Versionnumber = objLatestversion.getVersionno();
		}

		Versionnumber++;

		Map<LSuserMaster, Object> mapObj1 = (Map<LSuserMaster, Object>) argObj1.get("modifiedby");
		@SuppressWarnings("unlikely-arg-type")
		int usercode = new ObjectMapper().convertValue(mapObj1.get("usercode"), Integer.class);
		Date date = new ObjectMapper().convertValue(argObj1.get("protocolmodifiedDate"), Date.class);
		LSuserMaster LSuserMaster = new LSuserMaster();
		LSuserMaster.setUsercode(usercode);
		if (newProtocolMasterObj1 != null) {
			ObjectMapper mapper = new ObjectMapper();

			// Jackson's use of generics here are completely unsafe, but that's another
			// issue
//			List<LSuserMaster> lsusermaster = mapper.convertValue(
//					argObj1.get("modifiedby"), 
//			    new TypeReference<List<LSuserMaster>>(){}
//			);
//		LSuserMaster lsusermaster =(LSuserMaster) argObj1.get("modifiedby");
			LSprotocolversion objversion = new LSprotocolversion();

			objversion.setApproved(newProtocolMasterObj1.getApproved());
			objversion.setCreatedby(newProtocolMasterObj1.getCreatedby());
			objversion.setCreatedate(newProtocolMasterObj1.getCreatedate());
//			objversion.setModifiedby(lSprotocolupdates.getModifiedby());

//			objversion.setModifieddate(lSprotocolupdates.getProtocolmodifiedDate());
			objversion.setModifieddate(date);
			objversion.setProtocolmastercode(newProtocolMasterObj1.getProtocolmastercode());
			objversion.setProtocolmastername(newProtocolMasterObj1.getProtocolmastername());
			objversion.setProtocolstatus(newProtocolMasterObj1.getProtocolstatus());
			objversion.setCreatedbyusername(newProtocolMasterObj1.getCreatedbyusername());
			objversion.setSharewithteam(newProtocolMasterObj1.getSharewithteam());
			objversion.setLssitemaster(newProtocolMasterObj1.getLssitemaster());
			objversion.setRejected(newProtocolMasterObj1.getRejected());
			objversion.setVersionname("Version_" + Versionnumber);
			objversion.setVersionno(Versionnumber);

			if (newProtocolMasterObj1.getLsprotocolversion() != null) {
				newProtocolMasterObj1.getLsprotocolversion().add(objversion);
			} else {
				List<LSprotocolversion> lstversion = new ArrayList<LSprotocolversion>();
				lstversion.add(objversion);
				newProtocolMasterObj1.setLsprotocolversion(lstversion);
			}

			lsprotocolversionRepository.save(newProtocolMasterObj1.getLsprotocolversion());
//			if (argObj1 != null) {
//				LScfttransaction LScfttransactionobj = new LScfttransaction();
//				LScfttransactionobj = new ObjectMapper().convertValue(argObj1.get("objsilentaudit"),
//						new TypeReference<LScfttransaction>() {
//						});
//
////			LSprotocolupdates lSprotocolupdates =(LSprotocolupdates) argObj1.get("objsilentaudit");
//				LScfttransactionobj.setComments("Protocol" + " " + newProtocolMasterObj1.getProtocolmastername() + " "
//						+ " was versioned to version_" + Versionnumber + " " + "by the user" + " "
//						+ newProtocolMasterObj1.getCreatedbyusername());
//				LScfttransactionobj.setTableName("LSfile");
//				LScfttransactionobj.setTableName("LSprotocolmaster");
//				lscfttransactionRepository.save(LScfttransactionobj);
//			}

		}
		return true;

	}

//	public boolean UpdateProtocolversion(LSprotocolmaster newProtocolMasterObj1, LSprotocolupdates lSprotocolupdates)
//	{
//		int Versionnumber = 0;
//		LSprotocolversion objLatestversion = lsprotocolversionRepository.findFirstByProtocolmastercodeOrderByVersionnoDesc(newProtocolMasterObj1.getProtocolmastercode());
//		if(objLatestversion != null)
//		{
//			Versionnumber = objLatestversion.getVersionno();
//		}
//		
//		Versionnumber++;
//		
//		
//		if(newProtocolMasterObj1 != null)
//		{
//		
//			LSprotocolversion objversion = new LSprotocolversion();
//			
//			objversion.setApproved(newProtocolMasterObj1.getApproved());
//			objversion.setCreatedby(newProtocolMasterObj1.getCreatedby());
//			objversion.setCreatedate(newProtocolMasterObj1.getCreatedate());
//			objversion.setModifiedby(lSprotocolupdates.getModifiedby());
//			objversion.setModifieddate(lSprotocolupdates.getProtocolmodifiedDate());
//			objversion.setProtocolmastercode(newProtocolMasterObj1.getProtocolmastercode());
//			objversion.setProtocolmastername(newProtocolMasterObj1.getProtocolmastername());
//			objversion.setProtocolstatus(newProtocolMasterObj1.getProtocolstatus());
//			objversion.setCreatedbyusername(newProtocolMasterObj1.getCreatedbyusername());
//			objversion.setSharewithteam(newProtocolMasterObj1.getSharewithteam());
//			objversion.setLssitemaster(newProtocolMasterObj1.getLssitemaster());
//			objversion.setRejected(newProtocolMasterObj1.getRejected());
//			objversion.setVersionname("Version_"+ Versionnumber);
//			objversion.setVersionno(Versionnumber);
//			
//			if(newProtocolMasterObj1.getLsprotocolversion() != null)
//			{
//				newProtocolMasterObj1.getLsprotocolversion().add(objversion);
//			}
//			else
//			{
//				List<LSprotocolversion> lstversion = new ArrayList<LSprotocolversion>();
//				lstversion.add(objversion);
//				newProtocolMasterObj1.setLsprotocolversion(lstversion);
//			}
//			
//			lsprotocolversionRepository.save(newProtocolMasterObj1.getLsprotocolversion());
//			if(lSprotocolupdates!= null)
//	    	{
//				lSprotocolupdates.getObjsilentaudit().setComments("Protocol"+" "+newProtocolMasterObj1.getProtocolmastername()+" "+" was versioned to version_"+Versionnumber +" "+"by the user"+ " "+newProtocolMasterObj1.getCreatedbyusername());
//		        lSprotocolupdates.getObjsilentaudit().setTableName("LSfile");
//		        lSprotocolupdates.getObjsilentaudit().setTableName("LSprotocolmaster");
//	    		lscfttransactionRepository.save(lSprotocolupdates.getObjsilentaudit());
//	    	}
//			
//		}
//		return true;
//		
//	}

	public Map<String, Object> deleteProtocolMaster(Map<String, Object> argObj) throws IOException {
		Map<String, Object> mapObj = new HashMap<String, Object>();

		LScfttransaction LScfttransactionobj = new LScfttransaction();

		if (argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
		}

		Response response = new Response();

		if (argObj.containsKey("protocolmastercode")) {

			int protocolusercode = new ObjectMapper().convertValue(argObj.get("protocolmastercode"), Integer.class);
			LSprotocolmaster newProtocolMasterObj = LSProtocolMasterRepositoryObj
					.findByprotocolmastercode(protocolusercode);
			newProtocolMasterObj.setProtocolstatus(0);
			newProtocolMasterObj.setStatus(0);
			LSProtocolMasterRepositoryObj.save(newProtocolMasterObj);
			List<LSprotocolmaster> LSprotocolmasterLst = LSProtocolMasterRepositoryObj
					.findByCreatedbyAndStatusAndLssitemaster(LScfttransactionobj.getLsuserMaster(), 1,
							LScfttransactionobj.getLssitemaster());

			LSprotocolworkflow lsprotocolworkflow = new LSprotocolworkflow();

			if (argObj.containsKey("lsusergroup")) {

				LSusergroup lsusergroup = new ObjectMapper().convertValue(argObj.get("lsusergroup"),
						new TypeReference<LSusergroup>() {
						});

				List<LSprotocolworkflowgroupmap> lsprotocolworkflowgroupmap = LSprotocolworkflowgroupmapRepository
						.findBylsusergroupAndWorkflowcodeNotNull(lsusergroup);

				if (lsprotocolworkflowgroupmap != null && lsprotocolworkflowgroupmap.size() > 0) {
					lsprotocolworkflow = lSprotocolworkflowRepository
							.findByworkflowcode(lsprotocolworkflowgroupmap.get(0).getWorkflowcode());

					List<LSprotocolmaster> LSprotocolmasterLst1 = LSProtocolMasterRepositoryObj
							.findByStatusAndLssitemasterAndLSprotocolworkflowAndCreatedbyNot(1,
									LScfttransactionobj.getLssitemaster(), lsprotocolworkflow,
									LScfttransactionobj.getLsuserMaster());

					LSprotocolmasterLst.addAll(LSprotocolmasterLst1);
				}
			}

			LSprotocolmasterLst = LSprotocolmasterLst.stream().distinct().collect(Collectors.toList());

			Collections.sort(LSprotocolmasterLst, Collections.reverseOrder());

			response.setStatus(true);
			response.setInformation("");
			mapObj.put("response", response);
			mapObj.put("protocolmasterLst", LSprotocolmasterLst);
			mapObj.put("protocol", newProtocolMasterObj);

			Map<String, Object> argObj1 = new HashMap<String, Object>();
			argObj1.put("objsilentaudit", argObj.get("objsilentaudit"));
			if (LSprotocolmasterLst.size() > 0) {

				int ismultitenant = new ObjectMapper().convertValue(argObj.get("ismultitenant"), Integer.class);
				LSprotocolmasterLst.get(0).setIsmultitenant(ismultitenant);

				argObj1.put("protocolmastercode", LSprotocolmasterLst.get(0).getProtocolmastercode());
				argObj1.put("ismultitenant", LSprotocolmasterLst.get(0).getIsmultitenant());

				Map<String, Object> ProtocolStepLstMap = getProtocolStepLst(argObj1);
				mapObj.put("protocolstepLst", ProtocolStepLstMap.get("protocolstepLst"));
			}
		}

		return mapObj;

	}

	public Map<String, Object> sharewithteam(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		@SuppressWarnings("unused")
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if (argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});

			// silent audit
//			if (LScfttransactionobj != null) {
//				LScfttransactionobj.setTableName("LSprotocolmaster");
//				if (argObj.containsKey("username")) {
//					String username = objMapper.convertValue(argObj.get("username"), String.class);
//					// String sitecode= objMapper.convertValue(argObj.get("lssitemaster"),
//					// String.class);
//					LSSiteMaster objsite = LSSiteMasterRepository.findBysitecode(LScfttransactionobj.getLssitemaster());
//					LSuserMaster objuser = LSuserMasterRepositoryObj.findByusernameAndLssitemaster(username, objsite);
//					LScfttransactionobj.setLsuserMaster(objuser.getUsercode());
////				cfttransaction.setLssitemaster(objuser.getLssitemaster());
//					LScfttransactionobj.setLssitemaster(objuser.getLssitemaster().getSitecode());
//					LScfttransactionobj.setUsername(username);
//				}
//				lscfttransactionRepository.save(LScfttransactionobj);
//			}
////		manual audit
//			if (argObj.containsKey("objuser")) {
////			objUser=objMapper.convertValue(argObj.get("objuser"), LoggedUser.class);
//				Map<String, Object> mapObjUser = (Map<String, Object>) argObj.get("objuser");
//
//				if (argObj.containsKey("objmanualaudit")) {
//					LScfttransaction objmanualaudit = new LScfttransaction();
//					objmanualaudit = objMapper.convertValue(argObj.get("objmanualaudit"), LScfttransaction.class);
//
//					objmanualaudit.setComments((String) mapObjUser.get("comments"));
//					lscfttransactionRepository.save(objmanualaudit);
//				}
//			}
//			LSprotocolmaster LSprotocolmasterObj = new ObjectMapper().convertValue(argObj.get("ProtocolMasterObj"), new TypeReference<LSprotocolmaster>() { });

			int protocolusercode = new ObjectMapper().convertValue(argObj.get("protocolmastercode"), Integer.class);
			LSprotocolmaster LSprotocolmasterObj = LSProtocolMasterRepositoryObj
					.findByprotocolmastercode(protocolusercode);

			LSprotocolmasterObj.setSharewithteam(1);
			LSProtocolMasterRepositoryObj.save(LSprotocolmasterObj);
			Map<String, Object> LSProtocolMasterLstMap = getLSProtocolMasterLst(argObj);
			mapObj.put("LSProtocolMasterLst", LSProtocolMasterLstMap.get("LSProtocolMasterLst"));
			mapObj.put("status", "success");
		}
		return mapObj;
	}

//	public Map<String, Object> updateworkflowforProtocol(LSprotocolmaster objClass) {
//
//		Map<String, Object> mapObj = new HashMap<String, Object>();
//
//		int approved = 0;
//
//		if (objClass.getApproved() != null) {
//			approved = objClass.getApproved();
//		}
//
//		LSProtocolMasterRepositoryObj.updateFileWorkflow(objClass.getElnprotocoltemplateworkflow(), approved,
//				objClass.getRejected(), objClass.getProtocolmastercode());
//
//		LSprotocolmaster LsProto = LSProtocolMasterRepositoryObj
//				.findFirstByProtocolmastercode(objClass.getProtocolmastercode());
//
//		LsProto.setElnprotocoltemplateworkflow(objClass.getElnprotocoltemplateworkflow());
//		if (LsProto.getApproved() == null) {
//			LsProto.setApproved(0);
//		}
//		List<LSprotocolworkflowhistory> obj = objClass.getLsprotocolworkflowhistory();
//		obj = obj.stream().map(workflowhistory -> {
//			try {
//				if (workflowhistory.getHistorycode() == null) {
//					workflowhistory.setCreatedate(commonfunction.getCurrentUtcTime());
//				}
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return workflowhistory;
//		}).collect(Collectors.toList());
//		lsprotocolworkflowhistoryRepository.save(obj);
//		LsProto.setLsprotocolworkflowhistory(obj);
//		mapObj.put("ProtocolObj", LsProto);
//		mapObj.put("status", "success");
//		if (objClass.getViewoption() == null || objClass.getViewoption() != null && objClass.getViewoption() != 2) {
//			if (objClass.getProtocolmastername() != null) {
////				LSsheetworkflow objlastworkflow = lssheetworkflowRepository
////						.findTopByAndLssitemasterOrderByWorkflowcodeDesc(objClass.getIsfinalstep().getLssitemaster());
//				ElnprotocolTemplateworkflow objlastworkflow = elnprotocolTemplateworkflowRepository
//						.findTopByAndLssitemasterOrderByWorkflowcodeDesc(objClass.getIsfinalstep().getLssitemaster());
//				if (objlastworkflow != null
//						&& objClass.getCurrentStep().getWorkflowcode() == objlastworkflow.getWorkflowcode()) {
//					objClass.setFinalworkflow(1);
//					;
//				} else {
//					objClass.setFinalworkflow(0);
//					;
//				}
//			}
//
//			try {
//				updatenotificationforprotocolworkflowapproval(objClass, LsProto.getElnprotocoltemplateworkflow());
//				updatenotificationforprotocol(objClass, LsProto.getElnprotocoltemplateworkflow());
//			} catch (Exception e) {
//
//			}
//		}
//		return mapObj;
//	}
	@Async
	public CompletableFuture<List<LSprotocolmaster>> updatenotificationforprotocolworkflowapproval(
			LSprotocolmaster objClass, ElnprotocolTemplateworkflow lssheetworkflow) throws IOException {
		String Details = "";
		String Notification = "";
		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
		LSprotocolmaster LsProto = LSProtocolMasterRepositoryObj
				.findFirstByProtocolmastercode(objClass.getProtocolmastercode());
		LSuserMaster createby = lsusermasterRepository.findByusercode(objClass.getCreatedby());
		LSnotification objnotify = new LSnotification();

		for (int k = 0; k < objClass.getElnprotocoltemplateworkflow().getElnprotocoltemplateworkflowgroupmap()
				.size(); k++) {

			List<LSMultiusergroup> userobj = lsMultiusergroupRepositery.findBylsusergroup(objClass
					.getElnprotocoltemplateworkflow().getElnprotocoltemplateworkflowgroupmap().get(k).getLsusergroup());

			List<Integer> objnotifyuser = userobj.stream().map(LSMultiusergroup::getUsercode)
					.collect(Collectors.toList());
			List<LSuserMaster> objuser = lsusermasterRepository.findByUsercodeInAndUserretirestatusNot(objnotifyuser,
					1);

			List<LSuserteammapping> objteam = LSuserteammappingRepositoryObj
					.findByTeamcodeNotNullAndLsuserMaster(objClass.getLSuserMaster());
			for (int j = 0; j < objteam.size(); j++) {
				LSusersteam objteam1 = lsusersteamRepository.findByteamcode(objteam.get(j).getTeamcode());
				List<LSuserteammapping> lstusers = LSuserteammappingRepositoryObj
						.findByteamcode(objteam1.getTeamcode());
//				List<LSuserteammapping> lstusers = objteam1.getLsuserteammapping();

				if (LsProto.getApproved() != null && objClass.getFinalworkflow() != 1) {

					for (int i = 0; i < lstusers.size(); i++) {
						if (!(objClass.getLSuserMaster().getUsercode()
								.equals(lstusers.get(i).getLsuserMaster().getUsercode()))) {
							if (LsProto.getApproved() == 0 && objClass.getRejected() == null
									|| objClass.getApproved() == null) {
								Notification = "PROTOCOLAPPROVALSENT";
								objnotify.setNotifationto(lstusers.get(i).getLsuserMaster());
							} else if (objClass.getApproved() == 2 && objClass.getRejected() == null) {
								Notification = "PROTOCOLAPPROVALRETURN";
								objnotify.setNotifationto(lstusers.get(i).getLsuserMaster());

							} else if (objClass.getRejected() != null && objClass.getRejected() == 1) {
								Notification = "PROTOCOLAPPROVALREJECT";
								objnotify.setNotifationto(createby);
							}

							Details = "{\"ordercode\":\"" + objClass.getProtocolmastercode() + "\", \"order\":\""
									+ objClass.getProtocolmastername() + "\", \"username\":\""

									+ objClass.getLSuserMaster().getUsername() + "\"}";
							objnotify.setNotifationfrom(objClass.getLSuserMaster());
							objnotify.setNotificationdate(objClass.getNotificationdate());
							objnotify.setNotification(Notification);
							objnotify.setNotificationdetils(Details);
							objnotify.setIsnewnotification(1);
							objnotify.setNotificationpath("/protocols");
							objnotify.setNotificationfor(1);

							lstnotifications.add(objnotify);
						}
					}
				} else {

					for (int i = 0; i < lstusers.size(); i++) {

						if (!(objClass.getLSuserMaster().getUsercode()
								.equals(lstusers.get(i).getLsuserMaster().getUsercode())))

						{
							if (objClass.getApproved() == 1 && objClass.getRejected() == null) {
								Notification = "PROTOCOLAPPROVAL";
								objnotify.setNotifationto(createby);
							} else if (objClass.getApproved() == 3) {
								Notification = "PROTOCOLAPPROVALREJECT";
								objnotify.setNotifationto(createby);

							} else if (objClass.getApproved() == 2 && objClass.getRejected() == null) {
								Notification = "PROTOCOLAPPROVALRETURN";
								objnotify.setNotifationto(lstusers.get(i).getLsuserMaster());

							}
							Details = "{\"ordercode\":\"" + objClass.getProtocolmastercode() + "\", \"order\":\""
									+ objClass.getProtocolmastername() + "\", \"username\":\""

									+ objClass.getLSuserMaster().getUsername() + "\"}";
							objnotify.setNotifationfrom(objClass.getLSuserMaster());
							objnotify.setNotificationdate(objClass.getNotificationdate());
							objnotify.setNotification(Notification);
							objnotify.setNotificationdetils(Details);

							objnotify.setIsnewnotification(1);
							objnotify.setNotificationpath("/protocols");
							objnotify.setNotificationfor(1);

							lstnotifications.add(objnotify);
						}
					}
				}

				lsnotificationRepository.save(lstnotifications);
			}
		}
		List<LSprotocolmaster> obj = new ArrayList<>();
//		obj.add(objfile);
		return CompletableFuture.completedFuture(obj);
	}

	@Async
	public CompletableFuture<List<LSprotocolmaster>> updatenotificationforprotocol(LSprotocolmaster objClass,
			ElnprotocolTemplateworkflow lsprotocolworkflow) throws IOException {
		List<LSuserteammapping> objteam = LSuserteammappingRepositoryObj
				.findByTeamcodeNotNullAndLsuserMaster(objClass.getLSuserMaster());
		LSprotocolmaster LsProto = LSProtocolMasterRepositoryObj
				.findFirstByProtocolmastercode(objClass.getProtocolmastercode());

		if (objteam != null && objteam.size() > 0) {
			String Details = "";
			String Notifiction = "";
			List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
			int perviousworkflowcode = lsprotocolworkflow != null ? lsprotocolworkflow.getWorkflowcode() : -1;
			String previousworkflowname = lsprotocolworkflow != null ? lsprotocolworkflow.getWorkflowname() : "";

			if (LsProto.getApproved() == 0 && objClass.getRejected() == null) {
				Notifiction = "PROTOCOLMOVED";

			} else if (LsProto.getApproved() == 1 && objClass.getRejected() == null || objClass.getApproved() == null) {
				Notifiction = "PROTOCOLAPPROVED";

			} else if (objClass.getApproved() == 2 && objClass.getRejected() == null) {
				Notifiction = "PROTOCOLRETURNED";
			} else if (objClass.getRejected() != null && objClass.getRejected() == 1) {
				Notifiction = "PROTOCOLREJECTED";
			}

			Details = "{\"ordercode\":\"" + objClass.getProtocolmastercode() + "\", \"order\":\""
					+ objClass.getProtocolmastername() + "\", \"previousworkflow\":\"" + previousworkflowname
					+ "\", \"previousworkflowcode\":\"" + perviousworkflowcode + "\", \"currentworkflow\":\""
					+ objClass.getLsprotocolworkflowhistory().get(objClass.getLsprotocolworkflowhistory().size() - 1)
							.getElnprotocoltemplateworkflow().getWorkflowname()
					+ "\", \"currentworkflowcode\":\"" + objClass.getElnprotocoltemplateworkflow().getWorkflowcode()
					+ "\"}";

			List<LSuserMaster> lstnotified = new ArrayList<LSuserMaster>();

			for (int i = 0; i < objteam.size(); i++) {
				LSusersteam objteam1 = lsusersteamRepository.findByteamcode(objteam.get(i).getTeamcode());
				List<LSuserteammapping> lstusers = LSuserteammappingRepositoryObj
						.findByteamcode(objteam1.getTeamcode());
//				List<LSuserteammapping> lstusers = objteam1.getLsuserteammapping();

				for (int j = 0; j < lstusers.size(); j++) {

					if (objClass.getLSuserMaster().getUsercode() != lstusers.get(j).getLsuserMaster().getUsercode()) {
						if (lstnotified.contains(lstusers.get(j).getLsuserMaster()))
							continue;

						lstnotified.add(lstusers.get(j).getLsuserMaster());
						LSnotification objnotify = new LSnotification();

						objnotify.setNotifationfrom(objClass.getLSuserMaster());
						objnotify.setNotifationto(lstusers.get(j).getLsuserMaster());
						objnotify.setNotificationdate(objClass.getNotificationdate());
						objnotify.setNotification(Notifiction);
						objnotify.setNotificationdetils(Details);
						objnotify.setIsnewnotification(1);
						objnotify.setNotificationpath("/protocols");
						objnotify.setNotificationfor(2);
						lstnotifications.add(objnotify);
					}
				}
			}

			lsnotificationRepository.save(lstnotifications);
		}
		List<LSprotocolmaster> obj = new ArrayList<>();
//		obj.add(objfile);
		return CompletableFuture.completedFuture(obj);
	}

	public Map<String, Object> updateworkflowforProtocolorder(LSlogilabprotocoldetail objClass) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		try {
			int approved = 0;

			if (objClass.getApproved() != null) {
				approved = objClass.getApproved();
			}

//			LSlogilabprotocoldetailRepository.updateFileWorkflow(objClass.getLsworkflow(), approved,
//					objClass.getRejected(), objClass.getProtocolordercode());

			LSlogilabprotocoldetailRepository.updateFileWorkflow(objClass.getElnprotocolworkflow(), approved,
					objClass.getRejected(), objClass.getProtocolordercode());

			LSlogilabprotocoldetail LsProto = LSlogilabprotocoldetailRepository
					.findOne(objClass.getProtocolordercode());

//			LsProto.setLsworkflow(objClass.getLsworkflow());
			LsProto.setElnprotocolworkflow(objClass.getElnprotocolworkflow());
			if (LsProto.getApproved() == null) {
				LsProto.setApproved(0);
			}
			for (int k = 0; k < objClass.getLsprotocolorderworkflowhistory().size(); k++) {
				if (objClass.getLsprotocolorderworkflowhistory().get(k).getHistorycode() == null) {
					try {
						objClass.getLsprotocolorderworkflowhistory().get(k)
								.setCreatedate(commonfunction.getCurrentUtcTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			lsprotocolorderworkflowhistoryRepository.save(objClass.getLsprotocolorderworkflowhistory());

			List<LSlogilabprotocoldetail> LSlogilabprotocoldetail = new ArrayList<LSlogilabprotocoldetail>();
			LSlogilabprotocoldetail.add(objClass);
//			List<LSworkflow> lstworkflow = objClass.getLstworkflow();
			List<Elnprotocolworkflow> lstworkflow = objClass.getLstelnprotocolworkflow();
//			LSlogilabprotocoldetail.forEach(objorderDetail -> objorderDetail.setLstworkflow(lstworkflow));
//			LsProto.setLstworkflow(lstworkflow);
			LSlogilabprotocoldetail.forEach(objorderDetail -> objorderDetail.setLstelnprotocolworkflow(lstworkflow));
			LsProto.setLstelnprotocolworkflow(lstworkflow);
			mapObj.put("curentprotocolorder", LSlogilabprotocoldetail);
			mapObj.put("ProtocolObj", LsProto);
			mapObj.put("status", "success");
			if (objClass.getProtocoltype() != null) {
				LSworkflow objlastworkflow = lsworkflowRepository
						.findTopByAndLssitemasterOrderByWorkflowcodeDesc(objClass.getIsfinalstep().getLssitemaster());
				if (objlastworkflow != null
						&& objClass.getCurrentStep().getWorkflowcode() == objlastworkflow.getWorkflowcode()) {
					objClass.setFinalworkflow(1);

				} else {
					objClass.setFinalworkflow(0);

				}
			}
			updatenotificationfororderworkflowapprovel(objClass);
			updatenotificationfororderworkflow(objClass, LsProto.getElnprotocolworkflow());
			String email = env.getProperty("spring.emailnotificationconfig");
			if (email != null && email.equals("true")) {
				try {
					updateemailnotificationfororderworkflow(objClass, LsProto.getElnprotocolworkflow());
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
				}

			}

		} catch (Exception e) {
			mapObj.put("ERROR", e);
		}

		// for eln trail order complete
		if (objClass.getIsmultitenant().equals(2) && objClass.getAccouttype().equals(1)) {
			objClass.setOrderflag("R");
			try {
				updateOrderStatus(objClass);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			}
		}
		return mapObj;
	}

	private void updateemailnotificationfororderworkflow(LSlogilabprotocoldetail objClass,
			Elnprotocolworkflow lsworkflow) throws MessagingException {

		String Content = "";
		LSlogilabprotocoldetail LsProto = LSlogilabprotocoldetailRepository.findOne(objClass.getProtocolordercode());

		LSuserMaster obj = lsusermasterRepository.findByusercode(objClass.getObjLoggeduser().getUsercode());
		LSusersteam objteam = lsusersteamRepository
				.findByteamcode(objClass.getLsprojectmaster().getLsusersteam().getTeamcode());
		String previousworkflowname = lsworkflow != null ? lsworkflow.getWorkflowname() : "";
		String currnetworkflow = objClass.getLsprotocolorderworkflowhistory()
				.get(objClass.getLsprotocolorderworkflowhistory().size() - 1).getElnprotocolworkflow()
				.getWorkflowname();
		if (previousworkflowname.equals(objClass.getElnprotocolworkflow().getWorkflowname())
				&& LsProto.getApproved() == 1) {
			Content = "Protocol Order: " + objClass.getProtoclordername() + " was approved by the " + obj.getUsername()
					+ " on the Workflow: " + currnetworkflow + "";
		} else if (LsProto.getApproved() == 0 && objClass.getRejected() == null) {
			Content = "Protocol Order: " + objClass.getProtoclordername() + " was sent from the workflow level : "
					+ currnetworkflow + " to : " + previousworkflowname + " by " + obj.getUsername() + "";
		} else if (LsProto.getApproved() == 2 && objClass.getRejected() == null) {
			Content = "Protocol Order: " + objClass.getProtoclordername()
					+ " was returned to the previous level of workflow from :" + currnetworkflow + " to : "
					+ previousworkflowname + " by " + obj.getUsername() + "";
		} else if (LsProto.getRejected() == 1) {
			Content = "Protocol Order: " + objClass.getProtoclordername() + " was rejected by " + obj.getUsername()
					+ "  upon review";
		}

		List<LSuserteammapping> lstusers = objteam.getLsuserteammapping();
		List<Email> lstemail = new ArrayList<Email>();
		for (int i = 0; i < lstusers.size(); i++) {
			if (!(objClass.getLsuserMaster().getUsercode().equals(lstusers.get(i).getLsuserMaster().getUsercode()))) {

				Email objemail = new Email();
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message);

				if (lstusers.get(i).getLsuserMaster().getEmailid() != null) {
					objemail.setMailto(lstusers.get(i).getLsuserMaster().getEmailid());
					helper.setTo(lstusers.get(i).getLsuserMaster().getEmailid());
				}
				// email notification
				String subject = "Auto message generation for Protocol Order";
				String from = env.getProperty("spring.mail.username");
				objemail.setMailfrom(from);
				objemail.setMailcontent(Content);
				objemail.setSubject(subject);
				lstemail.add(objemail);
				helper.setSubject(subject);
				helper.setFrom(from);
				helper.setText("<p>" + Content + "</p><br><p> Order : </p><a href='" + objClass.getOrderlink() + "'><b>"
						+ objClass.getProtoclordername() + "</b></a>", true);
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

	private void updatenotificationfororderworkflowapprovel(LSlogilabprotocoldetail objClass) {
		String Details = "";
		String Notifiction = "";

		LSuserMaster obj = lsusermasterRepository.findByusercode(objClass.getLsuserMaster().getUsercode());
		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
		LSlogilabprotocoldetail LsProto = LSlogilabprotocoldetailRepository.findOne(objClass.getProtocolordercode());
		LSuserMaster createby = lsusermasterRepository.findByusercode(LsProto.getCreateby());
		LSnotification objnotify = new LSnotification();

		String protocolordername = "";
		SequenceTable seqobj =  sequenceTableRepository.findBySequencecodeOrderBySequencecode(2);
		Boolean Applicationseq = seqobj.getSequenceview().equals(2) ? true : false;
		protocolordername = Applicationseq 
				?  objClass.getSequenceid() != null
					? objClass.getSequenceid() : objClass.getProtoclordername()
				: objClass.getProtoclordername();
		
//		for (int k = 0; k < objClass.getLsworkflow().getLsworkflowgroupmapping().size(); k++) {
//			List<LSMultiusergroup> userobj = lsMultiusergroupRepositery
//					.findBylsusergroup(objClass.getLsworkflow().getLsworkflowgroupmapping().get(k).getLsusergroup());
//
//			List<Integer> objnotifyuser = userobj.stream().map(LSMultiusergroup::getUsercode)
//					.collect(Collectors.toList());
//			List<LSuserMaster> objuser = lsusermasterRepository.findByUsercodeInAndUserretirestatusNot(objnotifyuser,
//					1);
		LSusersteam objteam = lsusersteamRepository
				.findByteamcode(objClass.getLsprojectmaster().getLsusersteam().getTeamcode());
		List<LSuserteammapping> lstusers = objteam.getLsuserteammapping();

		if (objClass.getApprovelstatus() != null && objClass.getFinalworkflow() == 1) {

			for (int i = 0; i < lstusers.size(); i++) {
				if (!(objClass.getLsuserMaster().getUsercode().equals(lstusers.get(i).getLsuserMaster().getUsercode())))

				{
					if (LsProto.getApproved() == 1
							&& !(objClass.getLsuserMaster().getUsercode().equals(createby.getUsercode()))) {
						Notifiction = "PROTOCOLFINALAPPROVED";
						objnotify.setNotifationto(createby);
					} else if (objClass.getApprovelstatus() == 3
							&& !(objClass.getLsuserMaster().getUsercode().equals(createby.getUsercode()))) {
						Notifiction = "PROTOCOLUSERREJECT";
						objnotify.setNotifationto(createby);
					} else if (LsProto.getApproved() == 2 && objClass.getRejected() == null) {
						Notifiction = "PROTOCOLUSERRETURN";
						objnotify.setNotifationto(lstusers.get(i).getLsuserMaster());

					}

					if (Notifiction != "") {
						Details = "{\"ordercode\":\"" + objClass.getProtocolordercode() + "\", \"order\":\""
								+ protocolordername + "\", \"user\":\""
								+ objClass.getLsuserMaster().getUsername() + "\", \"comment\":\""
								+ objClass.getComment() + "\"}";

						objnotify.setNotifationfrom(objClass.getLsuserMaster());

						objnotify.setNotificationdate(objClass.getNotificationdate());
						objnotify.setNotification(Notifiction);
						objnotify.setNotificationdetils(Details);
						objnotify.setIsnewnotification(1);
						objnotify.setNotificationpath("/Protocolorder");
						objnotify.setNotificationfor(1);
						lstnotifications.add(objnotify);
					}
				}
				Notifiction = "";
			}

		} else {
			for (int i = 0; i < lstusers.size(); i++) {
				if (!(objClass.getLsuserMaster().getUsercode()
						.equals(lstusers.get(i).getLsuserMaster().getUsercode()))) {

					if ((objClass.getApproved() == null && LsProto.getApproved() == 0
							|| objClass.getApproved() == 0 && objClass.getRejected() == null)
							|| (objClass.getApproved() == 2 && objClass.getApprovelstatus() == 1
									&& LsProto.getApproved() == 2 && LsProto.getApprovelstatus() == null
									&& objClass.getApprovelstatus() == 1)) {
						Notifiction = "PROTOCOLUSERAPPROVAL";
						objnotify.setNotifationto(lstusers.get(i).getLsuserMaster());
					} else if (objClass.getApprovelstatus() == 3
							&& !(objClass.getLsuserMaster().getUsercode().equals(createby.getUsercode()))) {
						Notifiction = "PROTOCOLUSERREJECT";
						objnotify.setNotifationto(createby);
					} else if (LsProto.getApproved() == 2 && objClass.getRejected() == null
							&& objClass.getApprovelstatus() != 1) {
						Notifiction = "PROTOCOLUSERRETURN";
						objnotify.setNotifationto(lstusers.get(i).getLsuserMaster());

					}
					if (Notifiction != "") {
						Details = "{\"ordercode\":\"" + objClass.getProtocolordercode() + "\", \"order\":\""
								+ protocolordername + "\", \"user\":\""
								+ objClass.getLsuserMaster().getUsername() + "\", \"comment\":\""
								+ objClass.getComment() + "\"}";

						objnotify.setNotifationfrom(obj);

						objnotify.setNotificationdate(objClass.getNotificationdate());
						objnotify.setNotification(Notifiction);
						objnotify.setNotificationdetils(Details);
						objnotify.setIsnewnotification(1);
						objnotify.setNotificationpath("/Protocolorder");
						objnotify.setNotificationfor(1);
						lstnotifications.add(objnotify);
					}
				}
				Notifiction = "";
			}

		}
//		}

		lsnotificationRepository.save(lstnotifications);
	}

	private void updatenotificationfororderworkflow(LSlogilabprotocoldetail objClass,
			Elnprotocolworkflow previousworkflow) {

		String Details = "";
		String Notifiction = "";
		LSlogilabprotocoldetail LsProto = LSlogilabprotocoldetailRepository.findOne(objClass.getProtocolordercode());

//		LSuserMaster obj = lsusermasterRepository.findByusercode(objClass.getObjLoggeduser().getUsercode());

		String protocolordername = "";
		SequenceTable seqobj =  sequenceTableRepository.findBySequencecodeOrderBySequencecode(2);
		Boolean Applicationseq = seqobj.getSequenceview().equals(2) ? true : false;
		protocolordername = Applicationseq 
				?  objClass.getSequenceid() != null
					? objClass.getSequenceid() : objClass.getProtoclordername()
				: objClass.getProtoclordername();
		

		LSusersteam objteam = lsusersteamRepository
				.findByteamcode(objClass.getLsprojectmaster().getLsusersteam().getTeamcode());
		String previousworkflowname = previousworkflow != null ? previousworkflow.getWorkflowname() : "";

		if (previousworkflowname.equals(objClass.getElnprotocolworkflow().getWorkflowname())
				&& LsProto.getApproved() == 1) {
			Notifiction = "PROTOCOLORDERFINALAPPROVAL";
		} else if (LsProto.getApproved() == 0 && objClass.getRejected() == null) {
			Notifiction = "PROTOCOLORDERAPPROVED";
		} else if (LsProto.getApproved() == 2 && objClass.getRejected() == null) {
			Notifiction = "PROTOCOLORRETURNED";
		} else if (LsProto.getRejected() == 1) {
			Notifiction = "PROTOCOLORDERREJECTED";
		}

		Details = "{\"ordercode\":\"" + objClass.getProtocolordercode() + "\", \"order\":\""
				+ protocolordername + "\", \"previousworkflow\":\"" + previousworkflowname
				+ "\", \"currentworkflow\":\""
				+ objClass.getLsprotocolorderworkflowhistory()
						.get(objClass.getLsprotocolorderworkflowhistory().size() - 1).getElnprotocolworkflow()
						.getWorkflowname()
				+ "\", \"currentworkflowcode\":\"" + objClass.getElnprotocolworkflow().getWorkflowcode() + "\"}";

		List<LSuserteammapping> lstusers = objteam.getLsuserteammapping();
		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
		for (int i = 0; i < lstusers.size(); i++) {
			if (!(objClass.getLsuserMaster().getUsercode().equals(lstusers.get(i).getLsuserMaster().getUsercode()))) {

				LSnotification objnotify = new LSnotification();
				objnotify.setNotifationfrom(objClass.getObjLoggeduser());
				objnotify.setNotifationto(lstusers.get(i).getLsuserMaster());
				objnotify.setNotificationdate(objClass.getNotificationdate());
				objnotify.setNotification(Notifiction);
				objnotify.setNotificationdetils(Details);
				objnotify.setIsnewnotification(1);
				objnotify.setNotificationpath("/Protocolorder");
				objnotify.setNotificationfor(2);

				lstnotifications.add(objnotify);
			}

		}

		lsnotificationRepository.save(lstnotifications);

	}

	private void updatenotificationforprotocolorder(LSlogilabprotocoldetail objClass) {

		String Details = "";
		String Notifiction = "";
		
		String protocolordername = "";
		SequenceTable seqobj =  sequenceTableRepository.findBySequencecodeOrderBySequencecode(2);
		Boolean Applicationseq = seqobj.getSequenceview().equals(2) ? true : false;
		protocolordername = Applicationseq 
				?  objClass.getSequenceid() != null
					? objClass.getSequenceid() : objClass.getProtoclordername()
				: objClass.getProtoclordername();
		

		LSuserMaster obj = lsusermasterRepository.findByusercode(objClass.getLsuserMaster().getUsercode());
		LSuserMaster createby = lsusermasterRepository.findByusercode(objClass.getCreateby());
		if (objClass.getAssignedto() != null) {

			if (objClass.getOrderflag().equalsIgnoreCase("R")) {

				Notifiction = "PROTOCOLORDERCOMPLETED";

				Details = "{\"ordercode\":\"" + objClass.getProtocolordercode() + "\", \"order\":\""
						+ protocolordername + "\", \"completeduser\":\""
						+ objClass.getLsuserMaster().getUsername() + "\"}";

			} else {
				Notifiction = "PROTOCOLORDERCREATIONANDASSIGN";

				Details = "{\"ordercode\":\"" + objClass.getProtocolordercode() + "\", \"order\":\""
						+ protocolordername + "\", \"previousworkflow\":\"" + "" + "\", \"assignedby\":\""
						+ objClass.getCreatedbyusername() + "\", \"previousworkflowcode\":\"" + -1
						+ "\", \"currentworkflow\":\"" + objClass.getElnprotocolworkflow().getWorkflowname()
						+ "\", \"currentworkflowcode\":\"" + objClass.getElnprotocolworkflow().getWorkflowcode()
						+ "\"}";
			}
			LSnotification objnotify = new LSnotification();
			objnotify.setNotifationfrom(obj);
			objnotify.setNotifationto(objClass.getAssignedto());
			objnotify.setNotificationdate(objClass.getCreatedtimestamp());
			objnotify.setNotification(Notifiction);
			objnotify.setNotificationdetils(Details);
			objnotify.setIsnewnotification(1);
			objnotify.setNotificationpath("/Protocolorder");
			objnotify.setNotificationfor(1);
			lsnotificationRepository.save(objnotify);

		} else if (objClass != null && objClass.getLsprojectmaster() != null
				&& objClass.getLsprojectmaster().getLsusersteam() != null) {
			LSusersteam objteam = lsusersteamRepository
					.findByteamcode(objClass.getLsprojectmaster().getLsusersteam().getTeamcode());

			if (objteam.getLsuserteammapping() != null && objteam.getLsuserteammapping().size() > 0) {
				// objClass.setOrderflag("R");

				if (objClass.getOrderflag().equalsIgnoreCase("R")) {

					Notifiction = "PROTOCOLORDERCOMPLETED";

					Details = "{\"ordercode\":\"" + objClass.getProtocolordercode() + "\", \"order\":\""
							+ protocolordername + "\", \"currentworkflow\":\""
							+ objClass.getElnprotocolworkflow().getWorkflowname() + "\", \"completeduser\":\""
							+ objClass.getLsuserMaster().getUsername() + "\"}";

				} else {
					Notifiction = "PROTOCOLORDERCREATION";

					Details = "{\"ordercode\":\"" + objClass.getProtocolordercode() + "\", \"order\":\""
							+ protocolordername + "\", \"previousworkflow\":\"" + ""
							+ "\", \"previousworkflowcode\":\"" + -1 + "\", \"currentworkflow\":\""
							+ objClass.getElnprotocolworkflow().getWorkflowname() + "\", \"currentworkflowcode\":\""
							+ objClass.getElnprotocolworkflow().getWorkflowcode() + "\"}";
				}

				List<LSuserteammapping> lstusers = objteam.getLsuserteammapping();
				List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
				for (int i = 0; i < lstusers.size(); i++) {
					if (objClass.getLsuserMaster().getUsercode() != lstusers.get(i).getLsuserMaster().getUsercode()) {
						LSnotification objnotify = new LSnotification();
						if (objClass.getOrderflag().equalsIgnoreCase("R")) {
							objnotify.setNotifationto(lstusers.get(i).getLsuserMaster());
							objnotify.setNotifationfrom(obj);
							objnotify.setNotificationdate(objClass.getCompletedtimestamp());

						} else {
							objnotify.setNotifationto(lstusers.get(i).getLsuserMaster());
							objnotify.setNotifationfrom(obj);
							objnotify.setNotificationdate(objClass.getCreatedtimestamp());
						}

						objnotify.setNotification(Notifiction);
						objnotify.setNotificationdetils(Details);
						objnotify.setIsnewnotification(1);
						objnotify.setNotificationpath("/Protocolorder");
						objnotify.setNotificationfor(2);

						lstnotifications.add(objnotify);
						lsnotificationRepository.save(lstnotifications);
					}
				}

			}
		}
	}

	public List<LSprotocolworkflow> GetProtocolWorkflow(LSprotocolworkflow objclass) {
		return lSprotocolworkflowRepository.findBylssitemaster(objclass.getLssitemaster());
	}

	public List<LSprotocolworkflow> InsertUpdatesheetWorkflow(LSprotocolworkflow[] protocolworkflow) {

		List<LSprotocolworkflow> lSprotocolworkflow = Arrays.asList(protocolworkflow);
		for (LSprotocolworkflow flow : lSprotocolworkflow) {
			LSprotocolworkflowgroupmapRepository.save(flow.getLsprotocolworkflowgroupmap());
			lSprotocolworkflowRepository.save(flow);
		}

		if (lSprotocolworkflow.get(0).getObjsilentaudit() != null) {
			lSprotocolworkflow.get(0).getObjsilentaudit().setTableName("lSprotocolworkflow");

		}
		lSprotocolworkflow.get(0).setResponse(new Response());
		lSprotocolworkflow.get(0).getResponse().setStatus(true);
		lSprotocolworkflow.get(0).getResponse().setInformation("ID_SHEETMSG");

		return lSprotocolworkflow;
	}

	public Response Deletesheetworkflow(LSprotocolworkflow objflow) {
		Response response = new Response();

		long onprocess = LSProtocolMasterRepositoryObj.countBylSprotocolworkflowAndApproved(objflow, 0);
		if (onprocess > 0) {
			response.setStatus(false);
		} else {
			LSProtocolMasterRepositoryObj.setWorkflownullforApprovedProtcol(objflow, 1);
			lSprotocolworkflowRepository.delete(objflow);
			LSprotocolworkflowgroupmapRepository.delete(objflow.getLsprotocolworkflowgroupmap());
			response.setStatus(true);
			if (objflow.getObjsilentaudit() != null) {
				objflow.getObjsilentaudit().setTableName("LSprotocolworkflow");
//				lscfttransactionRepository.save(objflow.getObjsilentaudit());
			}
		}
		return response;
	}

	public List<LSprotocolmaster> getProtocolMasterList(LSuserMaster objClass) {
		return LSProtocolMasterRepositoryObj.findByStatusAndLssitemasterAndApproved(1,
				objClass.getLssitemaster().getSitecode(), 1);
	}

	public Map<String, Object> addProtocolOrderafter(LSlogilabprotocoldetail lSlogilabprotocoldetail) {
		Map<String, Object> lstOrder = new HashMap<String, Object>();
		if (lSlogilabprotocoldetail.getProtocolordercode() != null) {

			List<LSlogilabprotocolsteps> lstSteps = LSlogilabprotocolstepsRepository
					.findByProtocolordercodeAndProtocolmastercode(lSlogilabprotocoldetail.getProtocolordercode(),
							lSlogilabprotocoldetail.getLsprotocolmaster().getProtocolmastercode());

			for (LSlogilabprotocolsteps LSprotocolstepObj1 : lstSteps) {
				List<LSprotocolimages> objimg = new ArrayList<>();
				List<LSprotocolfiles> objfile = new ArrayList<>();
				List<LSprotocolvideos> objvideo = new ArrayList<>();
				objfile = lsprotocolfilesRepository.findByProtocolstepcode(LSprotocolstepObj1.getProtocolstepcode());
				objimg = lsprotocolimagesRepository.findByProtocolstepcode(LSprotocolstepObj1.getProtocolstepcode());
				objvideo = lsprotocolvideosRepository.findByProtocolstepcode(LSprotocolstepObj1.getProtocolstepcode());

				if (lSlogilabprotocoldetail.getIsmultitenant() == 1
						|| lSlogilabprotocoldetail.getIsmultitenant() == 2) {
					try {
						if (objimg.size() != 0) {
							updateprotocolorderimages(objimg, "protocolorderimages", "protocolimages",
									lSlogilabprotocoldetail, LSprotocolstepObj1);
						}
						if (objfile.size() != 0) {
							updateprotocolorderfile(objfile, "protocolorderfiles", "protocolfiles",
									lSlogilabprotocoldetail, LSprotocolstepObj1);
						} else if (objvideo.size() != 0) {
							updateprotocolordervideos(objvideo, "protocolordervideo", "protocolvideo",
									lSlogilabprotocoldetail, LSprotocolstepObj1);
						}
					} catch (IOException e) {

						e.printStackTrace();
					}

				} else {
					if (objimg.size() != 0) {
						updateprotocolorderimagesforsql(objimg, lSlogilabprotocoldetail, LSprotocolstepObj1);
					}
					if (objfile.size() != 0) {
						updateprotocolorderfileforsql(objfile, lSlogilabprotocoldetail, LSprotocolstepObj1);
					} else if (objvideo.size() != 0) {
						updateprotocolordervideosforsql(objvideo, lSlogilabprotocoldetail, LSprotocolstepObj1);
					}
				}
			}

//			int myordercount = (int) LSlogilabprotocoldetailRepository
//					.countByProtocoltypeAndSitecodeAndAssignedtoAndCreatedtimestampBetween(
//							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(),
//							lSlogilabprotocoldetail.getAssignedto(), lSlogilabprotocoldetail.getFromdate(),
//							lSlogilabprotocoldetail.getTodate());
//
//			if (lSlogilabprotocoldetail.getAssignedto() != null) {
//				int assignedcount = LSlogilabprotocoldetailRepository
//						.countByProtocoltypeAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetween(
//								lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(),
//								lSlogilabprotocoldetail.getLsuserMaster(), lSlogilabprotocoldetail.getAssignedto(),
//								lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
//
//				lstOrder.put("assignedordercount", assignedcount);
//			}
//			int sharedbymecount = lsprotocolordersharedbyRepository
//					.countBySharebyunifiedidAndProtocoltypeAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
//							lSlogilabprotocoldetail.getUnifielduserid(), lSlogilabprotocoldetail.getProtocoltype(), 1,
//							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
//
//			int sheredtomecount = lsprotocolordersharetoRepository
//					.countBySharetounifiedidAndProtocoltypeAndSharestatusAndSharedonBetweenOrderBySharetoprotocolordercodeDesc(
//							lSlogilabprotocoldetail.getUnifielduserid(), lSlogilabprotocoldetail.getProtocoltype(), 1,
//							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
//
//			if (lSlogilabprotocoldetail.getOrderflag().equals("M")) {
//				int completedcount = (int) LSlogilabprotocoldetailRepository
//						.countByProtocoltypeAndSitecodeAndAssignedtoAndCreatedtimestampBetween(
//								lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(),
//								lSlogilabprotocoldetail.getAssignedto(), lSlogilabprotocoldetail.getFromdate(),
//								lSlogilabprotocoldetail.getTodate());
//
//				List<LSlogilabprotocoldetail> lstCompletedOrder = LSlogilabprotocoldetailRepository
//						.findByProtocoltypeAndSitecodeAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//								lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(),
//								lSlogilabprotocoldetail.getAssignedto(), lSlogilabprotocoldetail.getFromdate(),
//								lSlogilabprotocoldetail.getTodate());
//
//				lstOrder.put("lstMyOrder", lstCompletedOrder);
//				lstOrder.put("myordercount", completedcount);
//			} 
//		else if (lSlogilabprotocoldetail.getOrderflag().equals("A")) {
//				int completedcount = (int) LSlogilabprotocoldetailRepository
//					.countByProtocoltypeAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetween(
//								lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(),
//								lSlogilabprotocoldetail.getLsuserMaster(), lSlogilabprotocoldetail.getAssignedto(),
//								lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
//
//				List<LSlogilabprotocoldetail> lstCompletedOrder = LSlogilabprotocoldetailRepository
//					.findByProtocoltypeAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//								lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(),
//								lSlogilabprotocoldetail.getLsuserMaster(), lSlogilabprotocoldetail.getAssignedto(),
//								lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
//
//				lstOrder.put("lstAssignedOrder", lstCompletedOrder);
//				lstOrder.put("assignedordercount", completedcount);
//			}

//			lstOrder.put("myordercount", myordercount);
//
//			lstOrder.put("sharedbymecount", sharedbymecount);
//			lstOrder.put("sheredtomecount", sheredtomecount);

		}
		return lstOrder;

	}

	public LSlogilabprotocoldetail addautoProtocolOrder(LSlogilabprotocoldetail lSlogilabprotocoldetail, String timerId1)
			throws ParseException {

		LSlogilabprotocoldetail lSlogilabprotocoldetail1 = LSlogilabprotocoldetailRepository
				.findByProtocolordercode(lSlogilabprotocoldetail.getProtocolordercode());

		List<LsAutoregister> autoorder = lsautoregisterrepo.findByBatchcodeAndScreen(
				lSlogilabprotocoldetail1.getProtocolordercode(),
				lSlogilabprotocoldetail1.getLsautoregister().getScreen());
		Map<String, Object> mapObj = new HashMap<String, Object>();
		Integer Ismultitenant = autoorder.get(0).getIsmultitenant();
		
		Integer autoregistercount = lSlogilabprotocoldetail1.getAutoregistercount();
		
		if(autoorder != null) {
			autoorder.get(0).setRepeat(false);
			lsautoregisterrepo.save(autoorder.get(0));
		}
		
		if (autoregistercount > 0) {
			lSlogilabprotocoldetail1.setRepeat(false);
			lSlogilabprotocoldetail1.setAutoregistercount(0);
			LSlogilabprotocoldetailRepository.save(lSlogilabprotocoldetail1);
			
			
		}
//		Integer autoregistercount = lSlogilabprotocoldetail1.getAutoregistercount() - 1;
		LSlogilabprotocoldetailRepository.save(lSlogilabprotocoldetail1);
		if (autoregistercount != null && autoregistercount > 0) {
			autoregistercount = autoregistercount - 1;
//		if (lSlogilabprotocoldetail1.getAutoregistercount() != null
//				&& lSlogilabprotocoldetail1.getAutoregistercount() > 0) {
			autoorder.stream().forEach(autocode -> {
				if (autocode.getBatchcode().equals(lSlogilabprotocoldetail1.getProtocolordercode())) {
					if (autocode.getDelayinminutes() != null) {
						Instant now = Instant.now();
						Instant futureInstant = now.plus(Duration.ofMillis(autocode.getDelayinminutes()));
						Date futureDate = Date.from(futureInstant);
						autocode.setAutocreatedate(futureDate);
					}else {
						Date currentdate = null;
						try {
							currentdate = commonfunction.getCurrentUtcTime();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (autocode.getTimespan().equals("Days")) {

							Calendar calendar = Calendar.getInstance();
							calendar.setTime(currentdate);
							calendar.add(Calendar.DAY_OF_MONTH, autocode.getInterval());

							Date futureDate = calendar.getTime();
							autocode.setAutocreatedate(futureDate);
						} else if (autocode.getTimespan().equals("Week")) {

							Calendar calendar = Calendar.getInstance();
							calendar.setTime(currentdate);
							calendar.add(Calendar.DAY_OF_MONTH, (autocode.getInterval() * 7));

							Date futureDate = calendar.getTime();
							autocode.setAutocreatedate(futureDate);
						} else {
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(currentdate);
							calendar.add(Calendar.MINUTE, (2));
							Date futureDate = calendar.getTime();
							autocode.setAutocreatedate(futureDate);

						}
					}

					autocode.setBatchcode(null);
					autocode.setRegcode(null);
					autocode.setStoptime(null);
					autocode.setScreen("Protocol_Order");
					autocode.setIsautoreg(false);
					autocode.setTimerIdname(timerId1);
					lsautoregisterrepo.save(autocode);
					
					lSlogilabprotocoldetail1.setLsautoregister(autocode);
				}
			});
			timerStatusMapPro.put(timerId1, true);
			lSlogilabprotocoldetail1.setProtoclordername(null);
			lSlogilabprotocoldetail1.setProtocolordercode(null);

			String Content = "";
			try {
				lSlogilabprotocoldetail1.setCreatedtimestamp(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (lSlogilabprotocoldetail1 != null) {
				lSlogilabprotocoldetail1.setVersionno(0);

				if (lSlogilabprotocoldetail1.getProtocoltype() == 2
						&& lSlogilabprotocoldetail1.getLsprotocolmaster().getProtocolmastercode() == -2) {
					LSprotocolmaster lsprotocolmasterobj = LSProtocolMasterRepositoryObj.findByDefaulttemplate(1);
					if (lsprotocolmasterobj == null) {
						LSprotocolmaster lsprotocolmaster = new LSprotocolmaster();
						lsprotocolmaster.setProtocolmastername("Default Protocol");
						lsprotocolmaster.setStatus(0);
						lsprotocolmaster.setCreatedby(lSlogilabprotocoldetail1.getCreateby());
						lsprotocolmaster.setCreatedate(lSlogilabprotocoldetail1.getCreatedtimestamp());
						lsprotocolmaster.setLssitemaster(lSlogilabprotocoldetail1.getSitecode());
						LSProtocolMasterRepositoryObj.save(lsprotocolmaster);
						lSlogilabprotocoldetail1.setLsprotocolmaster(lsprotocolmaster);
					} else {
						lSlogilabprotocoldetail1.setLsprotocolmaster(lsprotocolmasterobj);
					}

				}

				if (lSlogilabprotocoldetail1.getOrdercancell() != null) {
					lSlogilabprotocoldetail1.setOrdercancell(null);
				}

				LSlogilabprotocoldetailRepository.save(lSlogilabprotocoldetail1);

				// sri
				List<LSlogilablimsorder> lsorder = new ArrayList<LSlogilablimsorder>();
				List<LSprotocolmethod> protmethod = lsprotocolmethodrepo.findByProtocolmastercode(
						lSlogilabprotocoldetail1.getLsprotocolmaster().getProtocolmastercode());
				if (protmethod != null && protmethod.size() > 0) {
					int protocolmethodindex = 0;

					// int methodindex = 0;
					for (LSprotocolmethod objmethod : lSlogilabprotocoldetail1.getLsprotocolmaster()
							.getLsprotocolmethod()) {
						LSlogilablimsorder objLimsOrder = new LSlogilablimsorder();
						String Limsorder = lSlogilabprotocoldetail1.getProtocolordercode().toString();

						// lSlogilabprotocoldetail.setProbatchid("PRO"+Limsorder);
						String order = "";
						if (protocolmethodindex < 10) {
							order = Limsorder.concat("0" + protocolmethodindex);
						} else {
							order = Limsorder.concat("" + protocolmethodindex);
						}
						objLimsOrder.setOrderid(Long.parseLong(order));
						objLimsOrder.setBatchid("ELN" + lSlogilabprotocoldetail1.getProtocolordercode());
						objLimsOrder.setMethodcode(objmethod.getMethodid());
						objLimsOrder.setInstrumentcode(objmethod.getInstrumentid());
						objLimsOrder.setTestcode(lSlogilabprotocoldetail1.getTestcode() != null
								? lSlogilabprotocoldetail1.getTestcode().toString()
								: null);
						objLimsOrder.setOrderflag("N");
						objLimsOrder.setCreatedtimestamp(lSlogilabprotocoldetail1.getCreatedtimestamp());

						lsorder.add(objLimsOrder);
						protocolmethodindex++;
					}

					lslogilablimsorderrepo.save(lsorder);
					lSlogilabprotocoldetail1.setLsLSlogilablimsorder(lsorder);
				}

				if (lSlogilabprotocoldetail1.getProtocolordercode() != null) {

					String ProtocolOrderName = "ELN" + lSlogilabprotocoldetail1.getProtocolordercode();

					lSlogilabprotocoldetail1.setProtoclordername(ProtocolOrderName);

					lSlogilabprotocoldetail1.setOrderflag("N");
					LSlogilabprotocoldetailRepository.save(lSlogilabprotocoldetail1);

					List<LSprotocolstep> lstSteps = LSProtocolStepRepositoryObj.findByProtocolmastercodeAndStatus(
							lSlogilabprotocoldetail1.getLsprotocolmaster().getProtocolmastercode(), 1);

					List<LSlogilabprotocolsteps> lststep1 = new ObjectMapper().convertValue(lstSteps,
							new TypeReference<List<LSlogilabprotocolsteps>>() {
							});
					List<CloudLsLogilabprotocolstepInfo> objinfo = new ArrayList<CloudLsLogilabprotocolstepInfo>();
					List<LsLogilabprotocolstepInfo> objmongoinfo = new ArrayList<LsLogilabprotocolstepInfo>();
					if (!lststep1.isEmpty()) {
						for (LSlogilabprotocolsteps LSprotocolstepObj1 : lststep1) {

							LSprotocolstepObj1.setModifiedusername(null);
							LSprotocolstepObj1.setProtocolordercode(lSlogilabprotocoldetail1.getProtocolordercode());
							LSprotocolstepObj1.setOrderstepflag("N");
							// LSprotocolstepObj1.setVersionno(0);

							LSlogilabprotocolstepsRepository.save(LSprotocolstepObj1);

							if (lSlogilabprotocoldetail1.getIsmultitenant() == 1
									|| lSlogilabprotocoldetail1.getIsmultitenant() == 2) {

								LSprotocolstepInformation lsprotocolstepInformation = lsprotocolstepInformationRepository
										.findById(LSprotocolstepObj1.getProtocolstepcode());
								if (lsprotocolstepInformation != null) {
									LSprotocolstepObj1
											.setLsprotocolstepInfo(lsprotocolstepInformation.getLsprotocolstepInfo());
								} else {
									Gson g = new Gson();
									LSprotocolstepObj1.setLsprotocolstepInfo(g.toJson(""));
								}
								CloudLsLogilabprotocolstepInfo CloudLSprotocolstepInfoObj = new CloudLsLogilabprotocolstepInfo();
								CloudLSprotocolstepInfoObj.setId(LSprotocolstepObj1.getProtocolorderstepcode());
								CloudLSprotocolstepInfoObj
										.setLsprotocolstepInfo(LSprotocolstepObj1.getLsprotocolstepInfo());
								CloudLsLogilabprotocolstepInfoRepository.save(CloudLSprotocolstepInfoObj);

								objinfo.add(CloudLSprotocolstepInfoObj);

								// mapObj.put("CloudLsLogilabprotocolstepInfo", objinfo);

							} else {
								LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate
										.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);

								List<LSprotocolimages> objimg = new ArrayList<>();
								List<LSprotocolfiles> objfile = new ArrayList<>();
								List<LSprotocolvideos> objvideo = new ArrayList<>();
								objfile = lsprotocolfilesRepository
										.findByProtocolstepcode(LSprotocolstepObj1.getProtocolstepcode());
								objimg = lsprotocolimagesRepository
										.findByProtocolstepcode(LSprotocolstepObj1.getProtocolstepcode());
								objvideo = lsprotocolvideosRepository
										.findByProtocolstepcode(LSprotocolstepObj1.getProtocolstepcode());

								if (newLSprotocolstepInfo != null) {
									LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
									LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
									if (objimg.size() != 0) {
										for (LSprotocolimages img : objimg) {
											if (img.getFileid() != null) {
												String id = img.getFileid()
														+ lSlogilabprotocoldetail1.getProtoclordername();

												String con = LSprotocolstepObj1.getLsprotocolstepInfo();
												String finalinfo = con.replaceAll(img.getFileid(), id);

												LSprotocolstepObj1.setLsprotocolstepInfo(finalinfo);
											}

										}
									}
									if (objfile.size() != 0) {
										for (LSprotocolfiles file : objfile) {
											if (file.getFileid() != null) {
												String id = file.getFileid()
														+ lSlogilabprotocoldetail1.getProtoclordername();

												String con = LSprotocolstepObj1.getLsprotocolstepInfo();
												String finalinfo = con.replaceAll(file.getFileid(), id);

												LSprotocolstepObj1.setLsprotocolstepInfo(finalinfo);
											}
										}

									}
									if (objvideo.size() != 0) {
										for (LSprotocolvideos video : objvideo) {
											if (video.getFileid() != null) {
												String id = video.getFileid()
														+ lSlogilabprotocoldetail1.getProtoclordername();

												String con = LSprotocolstepObj1.getLsprotocolstepInfo();
												String finalinfo = con.replaceAll(video.getFileid(), id);
												LSprotocolstepObj1.setLsprotocolstepInfo(finalinfo);
											}
										}
									}
								}
								LsLogilabprotocolstepInfo LsLogilabprotocolstepInfoObj = new LsLogilabprotocolstepInfo();
								// Gson g = new Gson();
								// String str = g.toJson(LSprotocolstepObj1.getLsprotocolstepInfo());
								LsLogilabprotocolstepInfoObj.setId(LSprotocolstepObj1.getProtocolorderstepcode());
								LsLogilabprotocolstepInfoObj.setContent(LSprotocolstepObj1.getLsprotocolstepInfo());
								mongoTemplate.insert(LsLogilabprotocolstepInfoObj);

								objmongoinfo.add(LsLogilabprotocolstepInfoObj);

							}

						}
					} else {
						LSprotocolmaster lsprotocolmasterobj = LSProtocolMasterRepositoryObj.findByprotocolmastercode(
								lSlogilabprotocoldetail1.getLsprotocolmaster().getProtocolmastercode());
						if (Ismultitenant == 1 || Ismultitenant == 2) {
							if (lsprotocolmasterobj.getContainerstored() == null
									&& lSlogilabprotocoldetail1.getContent() != null
									&& !lSlogilabprotocoldetail1.getContent().isEmpty()) {

								try {
									JSONObject protocolJson = new JSONObject(lSlogilabprotocoldetail1.getContent());
									protocolJson.put("protocolname", lSlogilabprotocoldetail1.getProtoclordername());
									updateProtocolOrderContent(protocolJson.toString(), lSlogilabprotocoldetail1,
											lSlogilabprotocoldetail1.getIsmultitenant());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								try {
									Content = objCloudFileManipulationservice.retrieveCloudSheets(
											lsprotocolmasterobj.getFileuid(),
											commonfunction.getcontainername(Ismultitenant,
													TenantContext.getCurrentTenant()) + "protocol");
									if(Content==null) {
										Content =lSlogilabprotocoldetail.getContent();
										lSlogilabprotocoldetail1.setContent(Content);
									}
									JSONObject protocolJson = new JSONObject(Content);
									protocolJson.put("protocolname", lSlogilabprotocoldetail1.getProtoclordername());
									updateProtocolOrderContent(protocolJson.toString(), lSlogilabprotocoldetail1,
											Ismultitenant);
									
									
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else {

							GridFSDBFile data = gridFsTemplate.findOne(new Query(Criteria.where("filename")
									.is("protocol_" + lsprotocolmasterobj.getProtocolmastercode())));
							if (data == null && lSlogilabprotocoldetail1.getContent() != null
									&& !lSlogilabprotocoldetail1.getContent().isEmpty()) {
								JSONObject protocolJson = new JSONObject(lSlogilabprotocoldetail1.getContent());
								protocolJson.put("protocolname", lSlogilabprotocoldetail1.getProtoclordername());
								Content = protocolJson.toString();
							} else {
								Content = new BufferedReader(
										new InputStreamReader(data.getInputStream(), StandardCharsets.UTF_8)).lines()
										.collect(Collectors.joining("\n"));
							}
						}
						mapObj.put("protocolData", Content);
					}
					if (objinfo.size() != 0) {
						lSlogilabprotocoldetail1.setCloudLsLogilabprotocolstepInfo(objinfo);
					} else if (objmongoinfo.size() != 0) {
						lSlogilabprotocoldetail1.setLsLogilabprotocolstepInfo(objmongoinfo);
					}

//						if (lSlogilabprotocoldetail.getIsmultitenant() == 1) {
					boolean isversion = true;
					boolean nochanges = true;

//						checkagain
//						updateCloudProtocolorderVersion(objorderindex.getProtocolordercode(), null, null, null,
//								isversion, objorderindex.getSitecode(), nochanges,
//								Ismultitenant, objorderindex.getCreatedbyusername(),
//								objorderindex.getCreateby());

					List<LSprotocolsampleupdates> lstsamplelst = LSprotocolsampleupdatesRepository
							.findByProtocolmastercode(
									lSlogilabprotocoldetail1.getLsprotocolmaster().getProtocolmastercode());

					List<LSprotocolordersampleupdates> protocolordersample = new ObjectMapper()
							.convertValue(lstsamplelst, new TypeReference<List<LSprotocolordersampleupdates>>() {
							});

					for (LSprotocolordersampleupdates samplelist : protocolordersample) {

						samplelist.setProtocolordercode(lSlogilabprotocoldetail1.getProtocolordercode());
						lsprotocolordersampleupdatesRepository.save(samplelist);
					}

					LSSiteMaster site = LSSiteMasterRepository.findBysitecode(lSlogilabprotocoldetail1.getSitecode());

//						lSlogilabprotocoldetail
//								.setLsworkflow(lsworkflowRepository.findTopByAndLssitemasterOrderByWorkflowcodeAsc(site));
					lSlogilabprotocoldetail1.setElnprotocolworkflow(
							elnprotocolworkflowRepository.findTopByAndLssitemasterOrderByWorkflowcodeAsc(site));

					if (lSlogilabprotocoldetail1.getLsActiveWidgets() != null) {
						LsActiveWidgets lsActiveWidgets = lSlogilabprotocoldetail1.getLsActiveWidgets();
						lsActiveWidgets.setActivewidgetsdetails(lSlogilabprotocoldetail1.getProtoclordername());
						lsActiveWidgets.setActivewidgetsdetailscode(lSlogilabprotocoldetail1.getProtocolordercode());
						try {
							lsActiveWidgets.setActivedatatimestamp(commonfunction.getCurrentUtcTime());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						lsActiveWidgetsRepository.save(lsActiveWidgets);
						lsActiveWidgets = null;
					}
					lSlogilabprotocoldetail1
							.setLstelnprotocolworkflow(lSlogilabprotocoldetail1.getLstelnprotocolworkflow());
//						lSlogilabprotocoldetail.setLstworkflow(lSlogilabprotocoldetail.getLstworkflow());

					lSlogilabprotocoldetail1.setOrdercancell(null);

					if (lSlogilabprotocoldetail1.getLsautoregister() != null) {

						lSlogilabprotocoldetail1.getLsautoregister()
								.setBatchcode(lSlogilabprotocoldetail1.getProtocolordercode());
						if (autoregistercount == 0) {
							lSlogilabprotocoldetail1.getLsautoregister().setIsautoreg(false);
						} else {
							lSlogilabprotocoldetail1.getLsautoregister().setIsautoreg(true);
						}
						lsautoregisterrepo.save(lSlogilabprotocoldetail1.getLsautoregister());
//							lSlogilabprotocoldetail.setRepeat(true);
						lSlogilabprotocoldetail1.setRepeat(autoregistercount == 0 ? false : true);
						lSlogilabprotocoldetail1.setAutoregistercount(autoregistercount);
						LSlogilabprotocoldetailRepository.save(lSlogilabprotocoldetail1);

					}

//					if (lSlogilabprotocoldetail1.getLsautoregister() != null && lSlogilabprotocoldetail1.getRepeat()
//							&& lSlogilabprotocoldetail1.getRepeat() != null) {
//
//						lSlogilabprotocoldetail1.getLsautoregister()
//								.setBatchcode(lSlogilabprotocoldetail1.getProtocolordercode());
//						lsautoregisterrepo.save(lSlogilabprotocoldetail1.getLsautoregister());
//						lSlogilabprotocoldetail.setLsautoregister(lSlogilabprotocoldetail1.getLsautoregister());
//						ValidateProtocolAutoRegistration(lSlogilabprotocoldetail1);
//					}

				}

				mapObj.put("AddedProtocol", lSlogilabprotocoldetail1);
			}
			LScfttransaction auditobj = new LScfttransaction();
			auditobj.setLsuserMaster(lSlogilabprotocoldetail1.getLsuserMaster().getUsercode());
			try {
				auditobj.setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			auditobj.setModuleName("IDS_SCN_PROTOCOLORDERS");
			auditobj.setActions("IDS_TSK_REGISTERED");
			auditobj.setManipulatetype("IDS_AUDIT_INSERTORDERS");
			auditobj.setComments(
					"order: " + lSlogilabprotocoldetail1.getProtocolordercode() + " is now autoregistered");
			auditobj.setLssitemaster(lSlogilabprotocoldetail1.getLsuserMaster().getLssitemaster().getSitecode());
			auditobj.setSystemcoments("Audittrail.Audittrailhistory.Audittype.IDS_AUDIT_SYSTEMGENERATED");
			lscfttransactionRepository.save(auditobj);

			// lsprotocolorderstephistoryRepository.save(lsprotocolorderstephistory);
			// });
		}else {
			String timerId = autoorder.get(0).getTimerIdname();
			if (timerId != null) {
				stopTimer(timerId1);

			}

		}
		return lSlogilabprotocoldetail1;

	}
	
	 public void stopTimer(String timerId) {
	        Timer timer = timerMapPro.get(timerId);
	        if (timer != null) {
	            timer.cancel();
	            timer.purge(); 
	            timerMapPro.remove(timerId); 
	            timerStatusMapPro.remove(timerId);
	            orderDetailMapPro.remove(timerId);
	            System.out.println("Timer " + timerId + " stopped.");
	        } else {
	            System.out.println("No timer found with ID " + timerId);
	        }
	    }
	 
	 public SequenceTable validateandupdatesheetordersequencenumber(LSlogilabprotocoldetail objorder, SequenceTableProjectLevel objprojectseq, SequenceTableTaskLevel objtaskseq)
		{
			SequenceTable seqorder= new SequenceTable();
			int sequence =2;
			seqorder = sequencetableRepository.findOne(sequence);
			
			if(seqorder!=null && seqorder.getApplicationsequence()==-1)
			{
				long appcount = LSlogilabprotocoldetailRepository.count();
				sequencetableRepository.setinitialapplicationsequence(appcount,sequence);
				seqorder.setApplicationsequence(appcount);
			}
			
			if(sequencetablesiteRepository.findBySequencecodeAndSitecode(sequence,objorder.getSitecode()) == null)
			{
				SequenceTableSite objsiteseq= new SequenceTableSite();
				objsiteseq.setSequencecode(sequence);
				objsiteseq.setSitecode(objorder.getSitecode());
				
				LSSiteMaster site = new LSSiteMaster(objorder.getSitecode());
				
				List<LSuserMaster> lstuser = lsusermasterRepository.findByLssitemasterOrderByCreateddateDesc(site);
				if(lstuser != null)
				{
//					objsiteseq.setSitesequence(LSlogilabprotocoldetailRepository.countByLsuserMasterIn(lstuser));
					objsiteseq.setSitesequence(LSlogilabprotocoldetailRepository.countBySitecode(objorder.getSitecode()));
				}
				else
				{
					objsiteseq.setSitesequence((long)0);
				}
				sequencetablesiteRepository.save(objsiteseq);
				
				if(seqorder.getSequencetablesite() !=null)
				{
					seqorder.getSequencetablesite().add(objsiteseq);
				}
				else
				{
					List<SequenceTableSite> lstseq= new ArrayList<SequenceTableSite>();
					lstseq.add(objsiteseq);
					seqorder.setSequencetablesite(lstseq);
				}
			}
			
			if(objorder.getLsprojectmaster() != null && sequencetableprojectRepository.findBySequencecodeAndProjectcode(sequence,objorder.getLsprojectmaster().getProjectcode()) == null)
			{
				SequenceTableProject objsiteseq= new SequenceTableProject();
				objsiteseq.setSequencecode(sequence);
				objsiteseq.setProjectcode(objorder.getLsprojectmaster().getProjectcode());
				
				if(objorder.getLsprojectmaster() != null)
				{
					objsiteseq.setProjectsequence(LSlogilabprotocoldetailRepository.countByLsprojectmaster(objorder.getLsprojectmaster()));
				}
				else
				{
					objsiteseq.setProjectsequence((long)0);
				}
				sequencetableprojectRepository.save(objsiteseq);
				
				if(seqorder.getSequencetableproject() !=null)
				{
					seqorder.getSequencetableproject().add(objsiteseq);
				}
				else
				{
					List<SequenceTableProject> lstseq= new ArrayList<SequenceTableProject>();
					lstseq.add(objsiteseq);
					seqorder.setSequencetableproject(lstseq);
				}
			}
			
			if(objorder.getLsprojectmaster() != null && sequencetableprojectlevelrepository.findByProjectcode(objorder.getLsprojectmaster().getProjectcode()) == null)
			{
				SequenceTableProjectLevel objprolevel = new SequenceTableProjectLevel();
				objprolevel.setProjectcode(objorder.getLsprojectmaster().getProjectcode());
				long projectseq =0;
				
				projectseq = projectseq + logilablimsorderdetailsRepository.countByLsprojectmaster(objorder.getLsprojectmaster());
				
				projectseq = projectseq + LSlogilabprotocoldetailRepository.countByLsprojectmaster(objorder.getLsprojectmaster());
				
				objprolevel.setProjectsequence(projectseq);
				
				objprojectseq = sequencetableprojectlevelrepository.save(objprolevel);
			}
			
			if(objorder.getLstestmasterlocal() != null && sequencetabletasklevelrepository.findByTestcode(objorder.getLstestmasterlocal().getTestcode()) == null)
			{
				SequenceTableTaskLevel objtsklevel = new SequenceTableTaskLevel();
				objtsklevel.setTestcode(objorder.getLstestmasterlocal().getTestcode());
				long taskseq =0;
				
				taskseq = taskseq + logilablimsorderdetailsRepository.countByLstestmasterlocal(objorder.getLstestmasterlocal());
				
				taskseq = taskseq + LSlogilabprotocoldetailRepository.countByLstestmasterlocal(objorder.getLstestmasterlocal());
				
				objtsklevel.setTasksequence(taskseq);
				
				objtaskseq = sequencetabletasklevelrepository.save(objtsklevel);
			}
			
			if(objorder.getLstestmasterlocal() != null && sequencetabletaskRepository.findBySequencecodeAndTestcode(sequence,objorder.getLstestmasterlocal().getTestcode()) == null)
			{
				SequenceTableTask objsiteseq= new SequenceTableTask();
				objsiteseq.setSequencecode(sequence);
				objsiteseq.setTestcode(objorder.getLstestmasterlocal().getTestcode());
				if(objorder.getLstestmasterlocal() != null)
				{
					objsiteseq.setTasksequence(LSlogilabprotocoldetailRepository.countByLstestmasterlocal(objorder.getLstestmasterlocal()));
				}
				else
				{
				objsiteseq.setTasksequence((long)0);
				}
				sequencetabletaskRepository.save(objsiteseq);
				
				if(seqorder.getSequencesabletask() !=null)
				{
					seqorder.getSequencesabletask().add(objsiteseq);
				}
				else
				{
					List<SequenceTableTask> lstseq= new ArrayList<SequenceTableTask>();
					lstseq.add(objsiteseq);
					seqorder.setSequencesabletask(lstseq);
				}
			}
			
			if(objorder.getProtocoltype() != null && sequencetableordertyperepository.findBySequencecodeAndOrdertype(sequence,objorder.getProtocoltype()) == null)
			{
				SequenceTableOrderType objordertype = new SequenceTableOrderType();
				objordertype.setSequencecode(sequence);
				objordertype.setOrdertype(objorder.getProtocoltype());
				if(objorder.getProtocoltype() != null)
				{
					objordertype.setOrdertypesequence(LSlogilabprotocoldetailRepository.countByProtocoltype(objorder.getProtocoltype()));
				}
				else
				{
					objordertype.setOrdertypesequence((long)0);
				}
				sequencetableordertyperepository.save(objordertype);
				
				if(seqorder.getSequencetableordertype() !=null)
				{
					seqorder.getSequencetableordertype().add(objordertype);
				}
				else
				{
					List<SequenceTableOrderType> lstseq= new ArrayList<SequenceTableOrderType>();
					lstseq.add(objordertype);
					seqorder.setSequencetableordertype(lstseq);
				}
			}
			
			return seqorder;
		}
	 
	 public void GetSequences(LSlogilabprotocoldetail objorder,SequenceTable seqorder, SequenceTableProjectLevel objprojectseq, SequenceTableTaskLevel objtaskseq) throws ParseException
		{
			SequenceTable sqa = seqorder;
			
			if(sqa != null)
			{
				objorder.setApplicationsequence(sqa.getApplicationsequence()+1);
				
//				if(objorder !=null && objorder.getLsuserMaster() != null&&
//						objorder.getLsuserMaster().getLssitemaster()!=null && 
//						objorder.getLsuserMaster().getLssitemaster().getSitecode()!=null)
				if(objorder !=null && objorder.getSitecode()!=null)
				{
					SequenceTableSite sqsite = sqa.getSequencetablesite().stream()
					        .filter(sq -> sq.getSitecode().equals(objorder.getSitecode())
					        && sq.getSequencecode().equals(sqa.getSequencecode())).findFirst().orElse(null);
					if(sqsite != null)
					{
						objorder.setSitesequence(sqsite.getSitesequence()+1);
					}
				}
				
				
				if(objorder !=null && objorder.getLsprojectmaster() != null
						&& objorder.getLsprojectmaster().getProjectcode() != null) {
					SequenceTableProject sqproject = sqa.getSequencetableproject().stream()
					        .filter(sq -> sq.getProjectcode().equals(objorder.getLsprojectmaster().getProjectcode())
					        && sq.getSequencecode().equals(sqa.getSequencecode())).findFirst().orElse(null);
					
					if(sqproject != null)
					{
						objorder.setProjectsequence(sqproject.getProjectsequence()+1);
					}
					
					if(objprojectseq == null|| objprojectseq.getProjectcode() == null)
					{
						objprojectseq = sequencetableprojectlevelrepository.findByProjectcode(objorder.getLsprojectmaster().getProjectcode());
					}
					
					if(objprojectseq != null)
					{
						objorder.setProjectlevelsequence(objprojectseq.getProjectsequence()+1);
					}
				}
				
				if(objorder !=null && objorder.getLstestmasterlocal() != null
						&& objorder.getLstestmasterlocal().getTestcode() != null) {
					SequenceTableTask sqtask = sqa.getSequencesabletask().stream()
					        .filter(sq -> sq.getTestcode().equals(objorder.getLstestmasterlocal().getTestcode())
					        && sq.getSequencecode().equals(sqa.getSequencecode())).findFirst().orElse(null);
					
					if(sqtask != null)
					{
						objorder.setTasksequence(sqtask.getTasksequence()+1);
					}
					
					if(objtaskseq == null || objtaskseq.getTestcode() == null)
					{
						objtaskseq = sequencetabletasklevelrepository.findByTestcode(objorder.getLstestmasterlocal().getTestcode());
					}
					
					if(objtaskseq != null)
					{
						objorder.setTasklevelsequence(objtaskseq.getTasksequence()+1);
					}
				}
				
				if(objorder !=null && objorder.getProtocoltype() != null) {
					SequenceTableOrderType sqordertype = sqa.getSequencetableordertype().stream()
					        .filter(sq -> sq.getOrdertype().equals(objorder.getProtocoltype())
					        && sq.getSequencecode().equals(sqa.getSequencecode())).findFirst().orElse(null);
					
					if(sqordertype != null)
					{
						objorder.setOrdertypesequence(sqordertype.getOrdertypesequence()+1);
					}
				}
				
				String sequence = objorder.getSequenceid();
				String sequencetext = sequence;
				if(sequence.contains("{s&") && sequence.contains("$s}"))
				{
					sequencetext = sequence.substring(sequence.indexOf("{s&")+3, sequence.indexOf("$s}"));
					String replacedseq ="";
					if(sqa.getSequenceview().equals(2) && objorder.getApplicationsequence()!=null && !sequencetext.equals(""))
					{
						replacedseq = String.format("%0"+sequencetext.length()+"d", objorder.getApplicationsequence());
					}
					else if(sqa.getSequenceview().equals(3) && objorder.getSitesequence() != null && !sequencetext.equals(""))
					{
						replacedseq = String.format("%0"+sequencetext.length()+"d", objorder.getSitesequence());
						
					}else if(sqa.getSequenceview().equals(4) && objorder.getOrdertypesequence()!=null && !sequencetext.equals(""))
					{
						replacedseq = String.format("%0"+sequencetext.length()+"d", objorder.getOrdertypesequence());
					}
					else if(sqa.getSequenceview().equals(5) && objorder.getTasksequence() != null && !sequencetext.equals(""))
					{
						replacedseq = String.format("%0"+sequencetext.length()+"d", objorder.getTasksequence());
					}
					else if(sqa.getSequenceview().equals(6)&& objorder.getProjectsequence() != null && !sequencetext.equals(""))
					{
						replacedseq = String.format("%0"+sequencetext.length()+"d", objorder.getProjectsequence());
					}
					else if(!sequencetext.equals("") && objorder.getApplicationsequence()!=null)
					{
						replacedseq = String.format("%0"+sequencetext.length()+"d", objorder.getApplicationsequence());
					}
					
					if(!sequencetext.equals("") && !replacedseq.equals(""))
					{
						sequencetext = sequence.substring(0, sequence.indexOf("{s&"))+replacedseq+sequence.substring(sequence.indexOf("$s}")+3, sequence.length());
					}
				}
				
				if(sequence.contains("{sp&") && sequence.contains("$sp}"))
				{
					String projectleveltext = sequencetext.substring(sequencetext.indexOf("{sp&")+4, sequencetext.indexOf("$sp}"));
					if(objorder.getProjectlevelsequence() != null && !projectleveltext.equals(""))
					{
						String replacedseq = String.format("%0"+projectleveltext.length()+"d", objorder.getProjectlevelsequence());
						sequencetext = sequencetext.substring(0, sequencetext.indexOf("{sp&"))+replacedseq+sequencetext.substring(sequencetext.indexOf("$sp}")+4, sequencetext.length());
					}
				}
				
				if(sequence.contains("{st&") && sequence.contains("$st}"))
				{
					String taskleveltext = sequencetext.substring(sequencetext.indexOf("{st&")+4, sequencetext.indexOf("$st}"));
					if(objorder.getTasklevelsequence() != null && !taskleveltext.equals(""))
					{
						String replacedseq = String.format("%0"+taskleveltext.length()+"d", objorder.getTasklevelsequence());
						sequencetext = sequencetext.substring(0, sequencetext.indexOf("{st&"))+replacedseq+sequencetext.substring(sequencetext.indexOf("$st}")+4, sequencetext.length());
					}
				}
				
				sequencetext = commonfunction.Updatedatesinsequence(sequence, sequencetext);
				
				objorder.setSequenceid(sequencetext);
			}
		}
	 
	 public void updatesequence(Integer sequenceno, LSlogilabprotocoldetail objorder)
		{
			if(objorder.getApplicationsequence() != null)
			{
				sequencetableRepository.setinitialapplicationsequence(objorder.getApplicationsequence(),sequenceno);
			}
			
			if(objorder.getSitesequence() != null && objorder.getSitecode()!=null)
			{
				sequencetablesiteRepository.setinitialsitesequence(objorder.getSitesequence(), sequenceno,
						objorder.getSitecode());
			}
			
			if(objorder.getProjectsequence() != null && objorder.getLsprojectmaster() != null
					&& objorder.getLsprojectmaster().getProjectcode() != null)
			{
				sequencetableprojectRepository.setinitialprojectsequence(objorder.getProjectsequence(), sequenceno,objorder.getLsprojectmaster().getProjectcode());
			}
			
			if(objorder.getTasksequence() != null && objorder.getLstestmasterlocal() != null
					&& objorder.getLstestmasterlocal().getTestcode() != null)
			{
				sequencetabletaskRepository.setinitialtasksequence(objorder.getTasksequence(), sequenceno, objorder.getLstestmasterlocal().getTestcode());
			}
			
			if(objorder.getOrdertypesequence() != null && objorder.getProtocoltype() != null)
			{
				sequencetableordertyperepository.setinitialordertypesequence(objorder.getOrdertypesequence(), sequenceno, objorder.getProtocoltype());
			}
			
			if(objorder.getProjectlevelsequence() != null&& objorder.getLsprojectmaster() != null
					&& objorder.getLsprojectmaster().getProjectcode() != null)
			{
				sequencetableprojectlevelrepository.setinitialprojectsequence(objorder.getProjectlevelsequence(), objorder.getLsprojectmaster().getProjectcode());
			}
			
			if(objorder.getTasklevelsequence() != null && objorder.getLstestmasterlocal() != null
					&& objorder.getLstestmasterlocal().getTestcode() != null)
			{
				sequencetabletasklevelrepository.setinitialtasksequence(objorder.getTasklevelsequence(), objorder.getLstestmasterlocal().getTestcode());
			}
		}

	public Map<String, Object> addProtocolOrder(LSlogilabprotocoldetail lSlogilabprotocoldetail) throws ParseException, IOException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		SequenceTableProjectLevel objprojectseq = new SequenceTableProjectLevel();
		SequenceTableTaskLevel objtaskseq = new SequenceTableTaskLevel();
		SequenceTable seqorder = validateandupdatesheetordersequencenumber(lSlogilabprotocoldetail, objprojectseq, objtaskseq);
		boolean isrest = false;
		seqorder = commonfunction.ResetSequence(seqorder, isrest);
		String Content = "";
		try {
			lSlogilabprotocoldetail.setCreatedtimestamp(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (lSlogilabprotocoldetail != null) {
			lSlogilabprotocoldetail.setVersionno(0);

			if (lSlogilabprotocoldetail.getProtocoltype() == 2
					&& lSlogilabprotocoldetail.getLsprotocolmaster().getProtocolmastercode() == -2) {
				LSprotocolmaster lsprotocolmasterobj = LSProtocolMasterRepositoryObj.findByDefaulttemplate(1);
				if (lsprotocolmasterobj == null) {
					LSprotocolmaster lsprotocolmaster = new LSprotocolmaster();
					lsprotocolmaster.setProtocolmastername("Default Protocol");
					lsprotocolmaster.setStatus(0);
					lsprotocolmaster.setCreatedby(lSlogilabprotocoldetail.getCreateby());
					lsprotocolmaster.setCreatedate(lSlogilabprotocoldetail.getCreatedtimestamp());
					lsprotocolmaster.setLssitemaster(lSlogilabprotocoldetail.getSitecode());
					LSProtocolMasterRepositoryObj.save(lsprotocolmaster);
					lSlogilabprotocoldetail.setLsprotocolmaster(lsprotocolmaster);
				} else {
					lSlogilabprotocoldetail.setLsprotocolmaster(lsprotocolmasterobj);
				}

			}
			GetSequences(lSlogilabprotocoldetail, seqorder, objprojectseq, objtaskseq);
			
			if(!lSlogilabprotocoldetail.getLsprotocolselectedTeam().isEmpty()) {
				lSlogilabprotocoldetail.getLsprotocolselectedTeam().forEach(item -> {
				    item.setDirectorycode(lSlogilabprotocoldetail.getDirectorycode());
				    try {
						item.setCreatedtimestamp(commonfunction.getCurrentUtcTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
					if(lSlogilabprotocoldetail.getElnmaterial() != null) {
						lSlogilabprotocoldetail.getLsprotocolselectedTeam().forEach(item -> item.setElnmaterial(lSlogilabprotocoldetail.getElnmaterial()));
					}
					lsprotoselectedteamRepo.save(lSlogilabprotocoldetail.getLsprotocolselectedTeam());
			}
			
			LSlogilabprotocoldetailRepository.save(lSlogilabprotocoldetail);
			updatesequence(2,lSlogilabprotocoldetail);
			
			// sri
			List<LSlogilablimsorder> lsorder = new ArrayList<LSlogilablimsorder>();
			if (lSlogilabprotocoldetail.getLsprotocolmaster().getLsprotocolmethod() != null
					&& lSlogilabprotocoldetail.getLsprotocolmaster().getLsprotocolmethod().size() > 0) {
				int protocolmethodindex = 0;

				// int methodindex = 0;
				for (LSprotocolmethod objmethod : lSlogilabprotocoldetail.getLsprotocolmaster().getLsprotocolmethod()) {
					LSlogilablimsorder objLimsOrder = new LSlogilablimsorder();
					String Limsorder = lSlogilabprotocoldetail.getProtocolordercode().toString();

					// lSlogilabprotocoldetail.setProbatchid("PRO"+Limsorder);
					String order = "";
					if (protocolmethodindex < 10) {
						order = Limsorder.concat("0" + protocolmethodindex);
					} else {
						order = Limsorder.concat("" + protocolmethodindex);
					}
					objLimsOrder.setOrderid(Long.parseLong(order));
					objLimsOrder.setBatchid("ELN" + lSlogilabprotocoldetail.getProtocolordercode());
					objLimsOrder.setMethodcode(objmethod.getMethodid());
					objLimsOrder.setInstrumentcode(objmethod.getInstrumentid());
					objLimsOrder.setTestcode(lSlogilabprotocoldetail.getTestcode() != null
							? lSlogilabprotocoldetail.getTestcode().toString()
							: null);
					objLimsOrder.setOrderflag("N");
					objLimsOrder.setCreatedtimestamp(lSlogilabprotocoldetail.getCreatedtimestamp());

					lsorder.add(objLimsOrder);
					protocolmethodindex++;
				}

				lslogilablimsorderrepo.save(lsorder);
				lSlogilabprotocoldetail.setLsLSlogilablimsorder(lsorder);
			}
			long milliseconds = 0;
			String timerId1 = UUID.randomUUID().toString();
			if (lSlogilabprotocoldetail.getLsautoregisterorder() != null && lSlogilabprotocoldetail.getRepeat()
					&& lSlogilabprotocoldetail.getRepeat() != null) {
				lSlogilabprotocoldetail.getLsautoregisterorder()
						.setBatchcode(lSlogilabprotocoldetail.getProtocolordercode());
				Map<String, Object> RtnObject = commonfunction.getdelaymillisecondforautoregister(
						lSlogilabprotocoldetail.getLsautoregisterorder().getTimespan(),
						lSlogilabprotocoldetail.getLsautoregisterorder().getInterval());
				Date updatedDate = (Date) RtnObject.get("Date");
				milliseconds = (long) RtnObject.get("delay");
				lSlogilabprotocoldetail.getLsautoregisterorder().setAutocreatedate(updatedDate);
				lSlogilabprotocoldetail.getLsautoregisterorder().setRepeat(true);
				lSlogilabprotocoldetail.getLsautoregisterorder().setDelayinminutes(milliseconds);
				lSlogilabprotocoldetail.getLsautoregisterorder().setTimerIdname(timerId1);
				lsautoregisterrepo.save(lSlogilabprotocoldetail.getLsautoregisterorder());
				lSlogilabprotocoldetail.setLsautoregister(lSlogilabprotocoldetail.getLsautoregisterorder());
				ValidateProtocolAutoRegistration(lSlogilabprotocoldetail, milliseconds, timerId1);

			}

			if (lSlogilabprotocoldetail.getProtocolordercode() != null) {

				String ProtocolOrderName = "ELN" + lSlogilabprotocoldetail.getProtocolordercode();

				lSlogilabprotocoldetail.setProtoclordername(ProtocolOrderName);

				lSlogilabprotocoldetail.setOrderflag("N");
//				if (lSlogilabprotocoldetail.getAssignedto() == null) {
//					lSlogilabprotocoldetail.setLsuserMaster(null);
//				}

//				List<LSprotocolstep> lstSteps = LSProtocolStepRepositoryObj.findByProtocolmastercode(
//						lSlogilabprotocoldetail.getLsprotocolmaster().getProtocolmastercode());
				List<LSprotocolstep> lstSteps = LSProtocolStepRepositoryObj.findByProtocolmastercodeAndStatus(
						lSlogilabprotocoldetail.getLsprotocolmaster().getProtocolmastercode(), 1);

				List<LSlogilabprotocolsteps> lststep1 = new ObjectMapper().convertValue(lstSteps,
						new TypeReference<List<LSlogilabprotocolsteps>>() {
						});
				List<CloudLsLogilabprotocolstepInfo> objinfo = new ArrayList<CloudLsLogilabprotocolstepInfo>();
				List<LsLogilabprotocolstepInfo> objmongoinfo = new ArrayList<LsLogilabprotocolstepInfo>();
				if (!lststep1.isEmpty()) {
					for (LSlogilabprotocolsteps LSprotocolstepObj1 : lststep1) {

						LSprotocolstepObj1.setModifiedusername(null);
						LSprotocolstepObj1.setProtocolordercode(lSlogilabprotocoldetail.getProtocolordercode());
						LSprotocolstepObj1.setOrderstepflag("N");
						// LSprotocolstepObj1.setVersionno(0);

						LSlogilabprotocolstepsRepository.save(LSprotocolstepObj1);

						if (lSlogilabprotocoldetail.getIsmultitenant() == 1
								|| lSlogilabprotocoldetail.getIsmultitenant() == 2) {

							LSprotocolstepInformation lsprotocolstepInformation = lsprotocolstepInformationRepository
									.findById(LSprotocolstepObj1.getProtocolstepcode());
							if (lsprotocolstepInformation != null) {
								LSprotocolstepObj1
										.setLsprotocolstepInfo(lsprotocolstepInformation.getLsprotocolstepInfo());
							} else {
								Gson g = new Gson();
								LSprotocolstepObj1.setLsprotocolstepInfo(g.toJson(""));
							}
							CloudLsLogilabprotocolstepInfo CloudLSprotocolstepInfoObj = new CloudLsLogilabprotocolstepInfo();
							CloudLSprotocolstepInfoObj.setId(LSprotocolstepObj1.getProtocolorderstepcode());
							CloudLSprotocolstepInfoObj
									.setLsprotocolstepInfo(LSprotocolstepObj1.getLsprotocolstepInfo());
							CloudLsLogilabprotocolstepInfoRepository.save(CloudLSprotocolstepInfoObj);

							objinfo.add(CloudLSprotocolstepInfoObj);

							// mapObj.put("CloudLsLogilabprotocolstepInfo", objinfo);

						} else {
							LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate
									.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);

							List<LSprotocolimages> objimg = new ArrayList<>();
							List<LSprotocolfiles> objfile = new ArrayList<>();
							List<LSprotocolvideos> objvideo = new ArrayList<>();
							objfile = lsprotocolfilesRepository
									.findByProtocolstepcode(LSprotocolstepObj1.getProtocolstepcode());
							objimg = lsprotocolimagesRepository
									.findByProtocolstepcode(LSprotocolstepObj1.getProtocolstepcode());
							objvideo = lsprotocolvideosRepository
									.findByProtocolstepcode(LSprotocolstepObj1.getProtocolstepcode());

							if (newLSprotocolstepInfo != null) {
								LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
								LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
								if (objimg.size() != 0) {
									for (LSprotocolimages img : objimg) {
										if (img.getFileid() != null) {
											String id = img.getFileid() + lSlogilabprotocoldetail.getProtoclordername();

											String con = LSprotocolstepObj1.getLsprotocolstepInfo();
											String finalinfo = con.replaceAll(img.getFileid(), id);

											LSprotocolstepObj1.setLsprotocolstepInfo(finalinfo);
										}

									}
								}
								if (objfile.size() != 0) {
									for (LSprotocolfiles file : objfile) {
										if (file.getFileid() != null) {
											String id = file.getFileid()
													+ lSlogilabprotocoldetail.getProtoclordername();

											String con = LSprotocolstepObj1.getLsprotocolstepInfo();
											String finalinfo = con.replaceAll(file.getFileid(), id);

											LSprotocolstepObj1.setLsprotocolstepInfo(finalinfo);
										}
									}

								}
								if (objvideo.size() != 0) {
									for (LSprotocolvideos video : objvideo) {
										if (video.getFileid() != null) {
											String id = video.getFileid()
													+ lSlogilabprotocoldetail.getProtoclordername();

											String con = LSprotocolstepObj1.getLsprotocolstepInfo();
											String finalinfo = con.replaceAll(video.getFileid(), id);
											LSprotocolstepObj1.setLsprotocolstepInfo(finalinfo);
										}
									}
								}
							}
							LsLogilabprotocolstepInfo LsLogilabprotocolstepInfoObj = new LsLogilabprotocolstepInfo();
							// Gson g = new Gson();
							// String str = g.toJson(LSprotocolstepObj1.getLsprotocolstepInfo());
							LsLogilabprotocolstepInfoObj.setId(LSprotocolstepObj1.getProtocolorderstepcode());
							LsLogilabprotocolstepInfoObj.setContent(LSprotocolstepObj1.getLsprotocolstepInfo());
							mongoTemplate.insert(LsLogilabprotocolstepInfoObj);

							objmongoinfo.add(LsLogilabprotocolstepInfoObj);

						}

					}
				} else {
					LSprotocolmaster lsprotocolmasterobj = LSProtocolMasterRepositoryObj.findByprotocolmastercode(
							lSlogilabprotocoldetail.getLsprotocolmaster().getProtocolmastercode());
					if(lsprotocolmasterobj.getApproved() != null && lsprotocolmasterobj.getApproved() == 0) {
						List<LSprotocolversion> lstproversions = lsprotocolversionRepository.findByprotocolmastercodeOrderByVersionnoDesc(
								lsprotocolmasterobj.getProtocolmastercode());
						if (lSlogilabprotocoldetail.getIsmultitenant() == 1	|| lSlogilabprotocoldetail.getIsmultitenant() == 2) {
							String tenant = TenantContext.getCurrentTenant();
							if (lSlogilabprotocoldetail.getIsmultitenant() == 2) {
								tenant = "freeusers";
							}
							Content = objCloudFileManipulationservice.retrieveCloudSheets(lstproversions.get(1).getFileuid(),
									tenant + "protocolversion");
//							Map<String, Object> objMap = objCloudFileManipulationservice
//									.storecloudSheetsreturnwithpreUUID(content, tenant + "protocolversion");
							JSONObject protocolJson = new JSONObject(Content);
							protocolJson.put("protocolname", lSlogilabprotocoldetail.getProtoclordername());
							updateProtocolOrderContent(protocolJson.toString(), lSlogilabprotocoldetail,
									lSlogilabprotocoldetail.getIsmultitenant());
						} else {
							GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is("protocol_"
									+ lsprotocolmasterobj.getProtocolmastercode() + "version_" + lstproversions.get(1).getVersionno())));
							if (largefile != null) {
								Content = new BufferedReader(
										new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
										.collect(Collectors.joining("\n"));
							}

							if (gridFsTemplate.findOne(new Query(Criteria.where("filename")
									.is("protocolorder_" + lSlogilabprotocoldetail.getProtocolordercode()))) == null) {
								try {
									gridFsTemplate.store(new ByteArrayInputStream(Content.getBytes(StandardCharsets.UTF_8)),
											"protocolorder_" + lSlogilabprotocoldetail.getProtocolordercode(),
											StandardCharsets.UTF_16);
								} catch (Exception e) {
									System.out.println("error protocoldata lslogilabprotocoldetail content update mongodb"
											+ lSlogilabprotocoldetail.getProtocolordercode());
								}
							}
						}
					} else {
						if (lSlogilabprotocoldetail.getIsmultitenant() == 1
								|| lSlogilabprotocoldetail.getIsmultitenant() == 2) {
							if (lsprotocolmasterobj.getContainerstored() == null
									&& lSlogilabprotocoldetail.getContent() != null
									&& !lSlogilabprotocoldetail.getContent().isEmpty()) {
								// Content = cloudSheetCreationRepository.findById((long)
								// objorder.getLsfile().getFilecode())
								// .getContent();
								try {
									JSONObject protocolJson = new JSONObject(lSlogilabprotocoldetail.getContent());
									protocolJson.put("protocolname", lSlogilabprotocoldetail.getProtoclordername());
									updateProtocolOrderContent(protocolJson.toString(), lSlogilabprotocoldetail,
											lSlogilabprotocoldetail.getIsmultitenant());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								try {
									Content = objCloudFileManipulationservice.retrieveCloudSheets(
											lsprotocolmasterobj.getFileuid(),
											commonfunction.getcontainername(lSlogilabprotocoldetail.getIsmultitenant(),
													TenantContext.getCurrentTenant()) + "protocol");
									JSONObject protocolJson = new JSONObject(Content);
									protocolJson.put("protocolname", lSlogilabprotocoldetail.getProtoclordername());
									updateProtocolOrderContent(protocolJson.toString(), lSlogilabprotocoldetail,
											lSlogilabprotocoldetail.getIsmultitenant());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else {
							GridFSDBFile data = gridFsTemplate.findOne(new Query(Criteria.where("filename")
									.is("protocol_" + lsprotocolmasterobj.getProtocolmastercode())));
							if (data == null && lSlogilabprotocoldetail.getContent() != null
									&& !lSlogilabprotocoldetail.getContent().isEmpty()) {
								JSONObject protocolJson = new JSONObject(lSlogilabprotocoldetail.getContent());
								protocolJson.put("protocolname", lSlogilabprotocoldetail.getProtoclordername());
								Content = protocolJson.toString();
							} else {
								Content = new BufferedReader(
										new InputStreamReader(data.getInputStream(), StandardCharsets.UTF_8)).lines()
										.collect(Collectors.joining("\n"));
							}
	
							if (gridFsTemplate.findOne(new Query(Criteria.where("filename")
									.is("protocolorder_" + lSlogilabprotocoldetail.getProtocolordercode()))) == null) {
								try {
									gridFsTemplate.store(new ByteArrayInputStream(Content.getBytes(StandardCharsets.UTF_8)),
											"protocolorder_" + lSlogilabprotocoldetail.getProtocolordercode(),
											StandardCharsets.UTF_16);
								} catch (Exception e) {
									System.out.println("error protocoldata lslogilabprotocoldetail content update mongodb"
											+ lSlogilabprotocoldetail.getProtocolordercode());
								}
							}
						}
					}
					mapObj.put("protocolData", Content);
				}
				if (objinfo.size() != 0) {
					lSlogilabprotocoldetail.setCloudLsLogilabprotocolstepInfo(objinfo);
				} else if (objmongoinfo.size() != 0) {
					lSlogilabprotocoldetail.setLsLogilabprotocolstepInfo(objmongoinfo);
				}

//				if (lSlogilabprotocoldetail.getIsmultitenant() == 1) {
				boolean isversion = true;
				boolean nochanges = true;
//				if (lSlogilabprotocoldetail.getProtocoltype() == 1) {
//						updateCloudProtocolorderVersion(lSlogilabprotocoldetail,LSprotocolstepObj1);
				updateCloudProtocolorderVersion(lSlogilabprotocoldetail.getProtocolordercode(), null, null, null,
						isversion, lSlogilabprotocoldetail.getSitecode(), nochanges,
						lSlogilabprotocoldetail.getIsmultitenant(), lSlogilabprotocoldetail.getCreatedbyusername(),
						lSlogilabprotocoldetail.getCreateby());
//				}
//				}

				List<LSprotocolsampleupdates> lstsamplelst = LSprotocolsampleupdatesRepository.findByProtocolmastercode(
						lSlogilabprotocoldetail.getLsprotocolmaster().getProtocolmastercode());

				List<LSprotocolordersampleupdates> protocolordersample = new ObjectMapper().convertValue(lstsamplelst,
						new TypeReference<List<LSprotocolordersampleupdates>>() {
						});

				for (LSprotocolordersampleupdates samplelist : protocolordersample) {

					samplelist.setProtocolordercode(lSlogilabprotocoldetail.getProtocolordercode());
					lsprotocolordersampleupdatesRepository.save(samplelist);
				}

				LSSiteMaster site = LSSiteMasterRepository.findBysitecode(lSlogilabprotocoldetail.getSitecode());

//				lSlogilabprotocoldetail
//						.setLsworkflow(lsworkflowRepository.findTopByAndLssitemasterOrderByWorkflowcodeAsc(site));
				lSlogilabprotocoldetail.setElnprotocolworkflow(
						elnprotocolworkflowRepository.findTopByAndLssitemasterOrderByWorkflowcodeAsc(site));

				if(lSlogilabprotocoldetail.getIsDefault() && lSlogilabprotocoldetail.getIsDefault() ) {
					lSlogilabprotocoldetail.setSequenceid(lSlogilabprotocoldetail.getProtoclordername());
					LSlogilabprotocoldetailRepository.save(lSlogilabprotocoldetail);
				}
				 
				String widgetname = lSlogilabprotocoldetail.getIsDefault() ?  lSlogilabprotocoldetail.getProtoclordername()  : lSlogilabprotocoldetail.getSequenceid() ;
				
				LsActiveWidgets lsActiveWidgets = lSlogilabprotocoldetail.getLsActiveWidgets();
				lsActiveWidgets.setActivewidgetsdetails(widgetname);
				LSlogilabprotocoldetailRepository.save(lSlogilabprotocoldetail);
				lsActiveWidgets.setActivewidgetsdetailscode(lSlogilabprotocoldetail.getProtocolordercode());
				lsActiveWidgets.setActivedatatimestamp(commonfunction.getCurrentUtcTime());
				lsActiveWidgetsRepository.save(lsActiveWidgets);
				lsActiveWidgets = null;

				lSlogilabprotocoldetail.setLstelnprotocolworkflow(lSlogilabprotocoldetail.getLstelnprotocolworkflow());
//				lSlogilabprotocoldetail.setLstworkflow(lSlogilabprotocoldetail.getLstworkflow());
			}

			mapObj.put("AddedProtocol", lSlogilabprotocoldetail);
		}

		final List<LSOrdernotification> ordernotList = new ArrayList<>(1);
		if (lSlogilabprotocoldetail.getCautiondate() != null && lSlogilabprotocoldetail.getDuedate() != null) {
			LSOrdernotification notobj = new LSOrdernotification();
			notobj.setBatchcode(lSlogilabprotocoldetail.getProtocolordercode());
			notobj.setBatchid(lSlogilabprotocoldetail.getProtoclordername());
			notobj.setCautiondate(lSlogilabprotocoldetail.getCautiondate());
			notobj.setCreatedtimestamp(lSlogilabprotocoldetail.getCreatedtimestamp());
			notobj.setDuedate(lSlogilabprotocoldetail.getDuedate());
			notobj.setUsercode(lSlogilabprotocoldetail.getLsuserMaster().getUsercode());
			notobj.setPeriod(lSlogilabprotocoldetail.getPeriod());
			notobj.setCautionstatus(1);
			notobj.setDuestatus(1);
			notobj.setIscompleted(false);
			notobj.setOverduestatus(1);
			notobj.setScreen("Protocolorder");

			ordernotList.add(lsordernotificationrepo.save(notobj));
			
			Notification notify = new Notification();
			notify.setBatchid(lSlogilabprotocoldetail.getProtoclordername());
			notify.setOrderid(lSlogilabprotocoldetail.getProtocolordercode());
			notify.setLsusermaster(lSlogilabprotocoldetail.getLsuserMaster());
			notify.setAddedby(lSlogilabprotocoldetail.getLsuserMaster().getUsername());
			notify.setUsercode(lSlogilabprotocoldetail.getLsuserMaster().getUsercode());
			notify.setSitecode(lSlogilabprotocoldetail.getLsuserMaster().getLssitemaster().getSitecode());
			notify.setScreen("sheetorder");
			notify.setCurrentdate(commonfunction.getCurrentUtcTime());
			notify.setCautiondate(lSlogilabprotocoldetail.getCautiondate());
			notify.setDuedate(lSlogilabprotocoldetail.getDuedate());
			notify.setAddedon(commonfunction.getCurrentUtcTime());
			notify.setStatus(1);
			notificationRepository.save(notify);
		}
		if (ordernotList.size() > 0) {
			lSlogilabprotocoldetail.setLsordernotification(ordernotList.get(0));
			try {
				loginservice.ValidateNotification(ordernotList.get(0));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		LSlogilabprotocoldetailRepository.save(lSlogilabprotocoldetail);
//		LSusergroup userGroup = LSusergroupRepository
//				.findOne(lSlogilabprotocoldetail.getMultiusergroupcode());
//		List<LSworkflow> lstworkflow = lSlogilabprotocoldetail.getLstworkflow();
//		List<LSlogilabprotocoldetail> lstPendingOrder = new ArrayList<>();
//		List<LSuserteammapping> LSuserteammapping = LSuserteammappingRepositoryObj
//				.findByLsuserMasterAndTeamcodeNotNull(lSlogilabprotocoldetail.getLsuserMaster());
//		int pendingcount = 0;
//		if (lstworkflow != null && lstworkflow.size() > 0) {
//
//			lstPendingOrder = LSlogilabprotocoldetailRepository
////					findTop10ByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByCreatedtimestampDesc
//					.findByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "N",
//							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getCreatedtimestamp());
//
//			lstPendingOrder.forEach((objorder) -> {
//
//				if (lstworkflow != null && objorder.getLsworkflow() != null
//						&& lstworkflow.size() > 0) {
//					// if(lstworkflow.contains(this.lsworkflow))
//
//					List<Integer> lstprotocolworkflowcode = new ArrayList<Integer>();
//					if (lstworkflow != null && lstworkflow.size() > 0) {
//						lstprotocolworkflowcode = lstworkflow.stream().map(LSworkflow::getWorkflowcode).collect(Collectors.toList());
//
//						if (lstprotocolworkflowcode.contains(objorder.getLsworkflow().getWorkflowcode())) {
//							objorder.setCanuserprocess(true);
//						} else {
//							objorder.setCanuserprocess(false);
//						}
//					} else {
//						objorder.setCanuserprocess(false);
//					}
//				} else {
//					objorder.setCanuserprocess(false);
//				}
//			});
//
//			pendingcount = LSlogilabprotocoldetailRepository
//					.countByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetween(
//							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "N",
//							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
//		} else if (LSuserteammapping != null && LSuserteammapping.size() == 0) {
//			lstPendingOrder = LSlogilabprotocoldetailRepository
//					.findByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "N",
//							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
//			lstPendingOrder.forEach(objorder -> objorder.setCanuserprocess(false));
//
//			pendingcount = LSlogilabprotocoldetailRepository
//					.countByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetween(
//							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "N",
//							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
//		}

//		mapObj.put("ListOfProtocol", lstPendingOrder);
//		mapObj.put("pendingcount", pendingcount);
		try {
			updatenotificationforprotocolorder(lSlogilabprotocoldetail);
		} catch (Exception e) {
			// TODO: handle exception
		}

//sri		
		new Thread(() -> {
			try {
				System.out.println("inside the thread SDMS order call");
				createLogilabLIMSOrder4SDMS(lSlogilabprotocoldetail);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		return mapObj;
	}

	public void ValidateProtocolAutoRegistration(LSlogilabprotocoldetail objprotocolorder, long milliseconds,
			String timerId1) throws ParseException {
		// lsordernotificationrepo.save(objnotification);
		scheduleAutoregduringregister(objprotocolorder, milliseconds, timerId1);
	}

	public void scheduleAutoregduringregister(LSlogilabprotocoldetail objprotocolorder, long milliseconds,
			String timerId1) throws ParseException {
		Date AutoCreateDate = objprotocolorder.getLsautoregister().getAutocreatedate();
		Instant autocreate = AutoCreateDate.toInstant();

		LocalDateTime AutoCreateTime = LocalDateTime.ofInstant(autocreate, ZoneId.systemDefault());
		LocalDateTime currentTime = LocalDateTime.now();

		if (AutoCreateTime.isAfter(currentTime) && objprotocolorder != null) {
//			Duration duration = Duration.between(currentTime, AutoCreateTime);
//			long delay = duration.toMillis();
			long delay =milliseconds;
			scheduleAutoRegister(objprotocolorder, delay,timerId1);

		}
	}

	private Map<Integer, TimerTask> scheduledTasks = new HashMap<>();

	private void scheduleAutoRegister(LSlogilabprotocoldetail objprotocolorder, long delay, String timerId1) {
		Set<Integer> runningTasks = new HashSet<>();
		synchronized (runningTasks) {
			if (scheduledTasks.containsKey(Integer.parseInt(objprotocolorder.getProtocolordercode().toString()))) {
				System.out.println("Task already scheduled for protocolordercode: "
						+ Integer.parseInt(objprotocolorder.getProtocolordercode().toString()));
				return;
			}

			if ((objprotocolorder.getRepeat() != null && objprotocolorder.getRepeat() != false)) {
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						try {
							LSlogilabprotocoldetail objlogilaborderdetailObject=objprotocolorder;
							 Timer timerobj = timerMapPro.get(timerId1);
							 if (timerobj == null) {
								 timerMapPro.put(timerId1, timer);
							 }else {
								 objlogilaborderdetailObject=orderDetailMapPro.get(timerId1);
							 }
							 objlogilaborderdetailObject=addautoProtocolOrder(objlogilaborderdetailObject,timerId1);
							orderDetailMapPro.put(timerId1, objlogilaborderdetailObject);
							 System.out.println("kumu");
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							synchronized (runningTasks) {
								runningTasks
										.remove(Integer.parseInt(objprotocolorder.getProtocolordercode().toString()));
							}
							scheduledTasks.remove(Integer.parseInt(objprotocolorder.getProtocolordercode().toString()));
						}
					}
				};
				runningTasks.add(Integer.parseInt(objprotocolorder.getProtocolordercode().toString()));
				
//				timer.schedule(task, delay);
				timer.scheduleAtFixedRate(task, delay, delay);
				scheduledTasks.put(Integer.parseInt(objprotocolorder.getProtocolordercode().toString()), task);
			}
		}
	}

	private void createLogilabLIMSOrder4SDMS(LSlogilabprotocoldetail objLSlogilabprotoorder) throws IOException {

		List<LSlogilablimsorder> lstLSlogilablimsorder = lslogilablimsorderrepo
				.findBybatchid(objLSlogilabprotoorder.getProtoclordername());

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

	public void updateProtocolOrderContent(String Content, LSlogilabprotocoldetail objOrder, Integer ismultitenant)
			throws IOException {

		if (ismultitenant == 1 || ismultitenant == 2) {

			Map<String, Object> objMap = objCloudFileManipulationservice.storecloudSheetsreturnwithpreUUID(Content,
					commonfunction.getcontainername(ismultitenant, TenantContext.getCurrentTenant()) + "protocolorder");
			String fileUUID = (String) objMap.get("uuid");
			String fileURI = objMap.get("uri").toString();

			objOrder.setFileuri(fileURI);
			objOrder.setFileuid(fileUUID);
			objOrder.setContainerstored(1);
			LSlogilabprotocoldetailRepository.save(objOrder);

		} else {
//			OrderCreation objsavefile = new OrderCreation();
//			objsavefile.setId((long) objfile.getFilesamplecode());
//			objsavefile.setContentvalues(contentValues);
//			objsavefile.setContentparameter(contentParams);
//
//			Query query = new Query(Criteria.where("id").is(objsavefile.getId()));
//
//			Boolean recordcount = mongoTemplate.exists(query, OrderCreation.class);
//
//			if (!recordcount) {
//				mongoTemplate.insert(objsavefile);
//			} else {
//				Update update = new Update();
//				update.set("contentvalues", contentValues);
//				update.set("contentparameter", contentParams);
//
//				mongoTemplate.upsert(query, update, OrderCreation.class);
//			}

//			GridFSDBFile largefile = gridFsTemplate
//					.findOne(new Query(Criteria.where("filename").is("order_" + objfile.getFilesamplecode())));
//			if (largefile != null) {
//				gridFsTemplate.delete(new Query(Criteria.where("filename").is("order_" + objfile.getFilesamplecode())));
//			}
//			gridFsTemplate.store(new ByteArrayInputStream(Content.getBytes(StandardCharsets.UTF_8)),
//					"order_" + objfile.getFilesamplecode(), StandardCharsets.UTF_16);

//			objsavefile = null;
		}
	}

	public void updateprotocolorderfileforsql(List<LSprotocolfiles> objimg,
			LSlogilabprotocoldetail lSlogilabprotocoldetail, LSlogilabprotocolsteps lslogilabprotocolsteps) {

		for (LSprotocolfiles LSprotocolfiles : objimg) {
			String oldfieldif = LSprotocolfiles.getFileid();
			String id = oldfieldif + lSlogilabprotocoldetail.getProtoclordername();
//				String id = LSprotocolfiles.getFileid()+lSlogilabprotocoldetail.getProtoclordername();
			LSprotocolorderfiles objorderimag = new LSprotocolorderfiles();
			objorderimag.setExtension(LSprotocolfiles.getExtension());
			objorderimag.setFileid(id);
			objorderimag.setProtocolordercode(lSlogilabprotocoldetail.getProtocolordercode());
			objorderimag.setProtocolorderstepcode(lslogilabprotocolsteps.getProtocolorderstepcode());
			objorderimag.setProtocolstepname(LSprotocolfiles.getProtocolstepname());
			objorderimag.setStepno(LSprotocolfiles.getStepno());
			objorderimag.setOldfileid(oldfieldif);
			objorderimag.setFilename(LSprotocolfiles.getFilename());

			lsprotocolorderfilesRepository.save(objorderimag);

			LSprotocolfilesContent content = lsprotocolfilesContentRepository.findByFileid(LSprotocolfiles.getFileid());
			LSprotocolorderfilesContent orderfilecontent = new LSprotocolorderfilesContent();
			orderfilecontent.setFileid(id);
			orderfilecontent.setName(content.getName());
			orderfilecontent.setId(objorderimag.getProtocolorderstepfilecode());
			orderfilecontent.setFile(new Binary(BsonBinarySubType.BINARY, content.getFile().getData()));
			LSprotocolorderfilesContentRepository.insert(orderfilecontent);
		}
	}

	public void updateprotocolordervideosforsql(List<LSprotocolvideos> objimg,
			LSlogilabprotocoldetail lSlogilabprotocoldetail, LSlogilabprotocolsteps lslogilabprotocolsteps) {
		if (objimg.size() != 0) {
			for (LSprotocolvideos LSprotocolimagesobj : objimg) {
				String oldfieldif = LSprotocolimagesobj.getFileid();
				String id = oldfieldif + lSlogilabprotocoldetail.getProtoclordername();

				LSprotocolordervideos objorderimag = new LSprotocolordervideos();
				objorderimag.setExtension(LSprotocolimagesobj.getExtension());
				objorderimag.setFileid(id);
				objorderimag.setProtocolordercode(lSlogilabprotocoldetail.getProtocolordercode());
				objorderimag.setProtocolorderstepcode(lslogilabprotocolsteps.getProtocolorderstepcode());
				objorderimag.setProtocolstepname(LSprotocolimagesobj.getProtocolstepname());
				objorderimag.setStepno(LSprotocolimagesobj.getStepno());
				objorderimag.setFilename(LSprotocolimagesobj.getFilename());

				LSprotocolordervideosRepository.save(objorderimag);

				Protocolvideos protocolImage = ProtocolvideosRepository.findByFileid(LSprotocolimagesobj.getFileid());
				Protocolordervideos poimg = new Protocolordervideos();
				poimg.setFileid(id);
				poimg.setVideo(new Binary(BsonBinarySubType.BINARY, protocolImage.getVideo().getData()));
				poimg.setName(protocolImage.getName());
				poimg.setId(objorderimag.getProtocolorderstepvideoscode());
				ProtocolordervideosRepository.insert(poimg);
			}
		}

	}

	public void updateprotocolorderimagesforsql(List<LSprotocolimages> objimg,
			LSlogilabprotocoldetail lSlogilabprotocoldetail, LSlogilabprotocolsteps lslogilabprotocolsteps) {
		if (objimg.size() != 0) {
			for (LSprotocolimages LSprotocolimagesobj : objimg) {

				String oldfieldif = LSprotocolimagesobj.getFileid();
				String id = oldfieldif + lSlogilabprotocoldetail.getProtoclordername();

				LSprotocolorderimages objorderimag = new LSprotocolorderimages();
				objorderimag.setExtension(LSprotocolimagesobj.getExtension());
				objorderimag.setFileid(id);
				objorderimag.setProtocolordercode(lSlogilabprotocoldetail.getProtocolordercode());
				objorderimag.setProtocolorderstepcode(lslogilabprotocolsteps.getProtocolorderstepcode());
				objorderimag.setProtocolstepname(LSprotocolimagesobj.getProtocolstepname());
				objorderimag.setStepno(LSprotocolimagesobj.getStepno());
				objorderimag.setFilename(LSprotocolimagesobj.getFilename());
				objorderimag.setOldfileid(LSprotocolimagesobj.getFileid());
				if (LSprotocolimagesobj.getSrc() != null) {
					objorderimag.setOldsrc(LSprotocolimagesobj.getSrc());
				}

//				String url = lSlogilabprotocoldetail.getOriginurl() + "/protocol/downloadprotocolorderfile/"
//						+ objorderimag.getFileid() + "/" + TenantContext.getCurrentTenant() + "/"
//						+ objorderimag.getFilename() + "/" + objorderimag.getExtension();
//				Gson g = new Gson();
//				String str = g.toJson(url);
//
//				objorderimag.setSrc(str);
				lsprotocolorderimagesRepository.save(objorderimag);

				ProtocolImage protocolImage = protocolImageRepository.findByFileid(LSprotocolimagesobj.getFileid());
//				ProtocolorderImage poimg=new ObjectMapper().convertValue(protocolImage,
//						new TypeReference<ProtocolImage>() {
//						});
				ProtocolorderImage poimg = new ProtocolorderImage();
				poimg.setFileid(id);
				poimg.setImage(new Binary(BsonBinarySubType.BINARY, protocolImage.getImage().getData()));
				poimg.setName(protocolImage.getName());
				poimg.setId(objorderimag.getProtocolorderstepimagecode());
				ProtocolorderImageRepository.insert(poimg);
			}
		}
	}

	public void updateprotocolordervideos(List<LSprotocolvideos> objimg, String findcontainername, String insertcontain,
			LSlogilabprotocoldetail lSlogilabprotocoldetail, LSlogilabprotocolsteps lslogilabprotocolsteps)
			throws IOException {

		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer containerfororder = null;
		CloudBlobContainer containerforprotocol = null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");

		try {

			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			containerfororder = blobClient.getContainerReference(TenantContext.getCurrentTenant() + findcontainername);

			// Create the container if it does not exist with public access.
			System.out.println("Creating container: " + containerfororder.getName());
			containerfororder.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
					new OperationContext());

			for (LSprotocolvideos LSprotocolfiles : objimg) {

				containerforprotocol = blobClient
						.getContainerReference(TenantContext.getCurrentTenant() + insertcontain);
				blob = containerforprotocol.getBlockBlobReference(LSprotocolfiles.getFileid());

				File targetFile = new File(System.getProperty("java.io.tmpdir") + "/" + LSprotocolfiles.getFileid());

				FileUtils.copyInputStreamToFile(blob.openInputStream(), targetFile);

				CloudBlockBlob blob1 = containerfororder.getBlockBlobReference(targetFile.getName());

				System.out.println("Uploading the sample file ");
				blob1.uploadFromFile(targetFile.getAbsolutePath());

				String id = LSprotocolfiles.getFileid();
				LSprotocolordervideos objorderimag = new LSprotocolordervideos();
				objorderimag.setExtension(LSprotocolfiles.getExtension());
				objorderimag.setFileid(id);
				objorderimag.setProtocolordercode(lSlogilabprotocoldetail.getProtocolordercode());
				objorderimag.setProtocolorderstepcode(lslogilabprotocolsteps.getProtocolorderstepcode());
				objorderimag.setProtocolstepname(LSprotocolfiles.getProtocolstepname());
				objorderimag.setStepno(LSprotocolfiles.getStepno());
				objorderimag.setFilename(LSprotocolfiles.getFilename());

				LSprotocolordervideosRepository.save(objorderimag);

			}

		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
			throw new IOException(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void updateprotocolorderfile(List<LSprotocolfiles> objimg, String findcontainername, String insertcontain,
			LSlogilabprotocoldetail lSlogilabprotocoldetail, LSlogilabprotocolsteps lslogilabprotocolsteps)
			throws IOException {

		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer containerfororder = null;
		CloudBlobContainer containerforprotocol = null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {

			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			containerfororder = blobClient.getContainerReference(TenantContext.getCurrentTenant() + findcontainername);

			// Create the container if it does not exist with public access.
			System.out.println("Creating container: " + containerfororder.getName());
			containerfororder.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
					new OperationContext());

			for (LSprotocolfiles LSprotocolfiles : objimg) {

				containerforprotocol = blobClient
						.getContainerReference(TenantContext.getCurrentTenant() + insertcontain);
				blob = containerforprotocol.getBlockBlobReference(LSprotocolfiles.getFileid());

				File targetFile = new File(System.getProperty("java.io.tmpdir") + "/" + LSprotocolfiles.getFileid());

				FileUtils.copyInputStreamToFile(blob.openInputStream(), targetFile);

				CloudBlockBlob blob1 = containerfororder.getBlockBlobReference(targetFile.getName());

				System.out.println("Uploading the sample file ");
				blob1.uploadFromFile(targetFile.getAbsolutePath());

				String id = LSprotocolfiles.getFileid();
				LSprotocolorderfiles objorderimag = new LSprotocolorderfiles();
				objorderimag.setExtension(LSprotocolfiles.getExtension());
				objorderimag.setFileid(id);
				objorderimag.setProtocolordercode(lSlogilabprotocoldetail.getProtocolordercode());
				objorderimag.setProtocolorderstepcode(lslogilabprotocolsteps.getProtocolorderstepcode());
				objorderimag.setProtocolstepname(LSprotocolfiles.getProtocolstepname());
				objorderimag.setStepno(LSprotocolfiles.getStepno());
				objorderimag.setFilename(LSprotocolfiles.getFilename());

				lsprotocolorderfilesRepository.save(objorderimag);

			}

		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
			throw new IOException(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	public void updateprotocolorderimages(List<LSprotocolimages> objimg, String findcontainername, String insertcontain,
			LSlogilabprotocoldetail lSlogilabprotocoldetail, LSlogilabprotocolsteps lslogilabprotocolsteps)
			throws IOException {
		if (objimg.size() != 0) {

			CloudStorageAccount storageAccount;
			CloudBlobClient blobClient = null;
			CloudBlobContainer containerfororder = null;
			CloudBlobContainer containerforprotocol = null;
			CloudBlockBlob blob = null;
			String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
			try {

				storageAccount = CloudStorageAccount.parse(storageConnectionString);
				blobClient = storageAccount.createCloudBlobClient();
				containerfororder = blobClient
						.getContainerReference(TenantContext.getCurrentTenant() + findcontainername);

				System.out.println("Creating container: " + containerfororder.getName());
				containerfororder.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
						new OperationContext());

				for (LSprotocolimages LSprotocolimagesobj : objimg) {

					containerforprotocol = blobClient
							.getContainerReference(TenantContext.getCurrentTenant() + insertcontain);
					blob = containerforprotocol.getBlockBlobReference(LSprotocolimagesobj.getFileid());

					File targetFile = new File(
							System.getProperty("java.io.tmpdir") + "/" + LSprotocolimagesobj.getFileid());

					try {
						FileUtils.copyInputStreamToFile(blob.openInputStream(), targetFile);
					} catch (Exception e) {
						continue;
					} finally {

					}

					CloudBlockBlob blob1 = containerfororder.getBlockBlobReference(targetFile.getName());

					System.out.println("Uploading the sample file ");
					blob1.uploadFromFile(targetFile.getAbsolutePath());

					String id = LSprotocolimagesobj.getFileid();
					LSprotocolorderimages objorderimag = new LSprotocolorderimages();
					objorderimag.setExtension(LSprotocolimagesobj.getExtension());
					objorderimag.setFileid(id);
					objorderimag.setProtocolordercode(lSlogilabprotocoldetail.getProtocolordercode());
					objorderimag.setProtocolorderstepcode(lslogilabprotocolsteps.getProtocolorderstepcode());
					objorderimag.setProtocolstepname(LSprotocolimagesobj.getProtocolstepname());
					objorderimag.setStepno(LSprotocolimagesobj.getStepno());
					objorderimag.setFilename(LSprotocolimagesobj.getFilename());
					objorderimag.setOldfileid(LSprotocolimagesobj.getFileid());
					if (LSprotocolimagesobj.getSrc() != null) {
						objorderimag.setOldsrc(LSprotocolimagesobj.getSrc());
					}

					String url = lSlogilabprotocoldetail.getOriginurl() + "/protocol/downloadprotocolorderfile/"
							+ objorderimag.getFileid() + "/" + TenantContext.getCurrentTenant() + "/"
							+ objorderimag.getFilename() + "/" + objorderimag.getExtension();
					Gson g = new Gson();
					String str = g.toJson(url);

					objorderimag.setSrc(str);
					lsprotocolorderimagesRepository.save(objorderimag);

				}

			} catch (StorageException ex) {
				System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
						ex.getHttpStatusCode(), ex.getErrorCode()));
				throw new IOException(String.format("Error returned from the service. Http code: %d and error code: %s",
						ex.getHttpStatusCode(), ex.getErrorCode()));
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}

		}

	}

	public Map<String, Object> getProtocolOrderList(LSlogilabprotocoldetail lSlogilabprotocoldetail) {
		Map<String, Object> lstOrder = new HashMap<String, Object>();

//		List<LSlogilabprotocoldetail> lstPendingOrder = LSlogilabprotocoldetailRepository
//				.findTop10ByProtocoltypeAndOrderflagOrderByCreatedtimestampDesc(
//						lSlogilabprotocoldetail.getProtocoltype(), "N");
//		int pendingcount = LSlogilabprotocoldetailRepository
//				.countByProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), "N");
//		int completedcount = LSlogilabprotocoldetailRepository
//				.countByProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), "R");
//		List<LSlogilabprotocoldetail> lstCompletedOrder = LSlogilabprotocoldetailRepository
//				.findTop10ByProtocoltypeAndOrderflagOrderByCreatedtimestampDesc(
//						lSlogilabprotocoldetail.getProtocoltype(), "R");

		List<LSlogilabprotocoldetail> lstCompletedOrder = LSlogilabprotocoldetailRepository
				.findTop10ByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByCompletedtimestampDesc(
						lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "R",
						lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

		List<Elnprotocolworkflow> lstworkflow = lSlogilabprotocoldetail.getLstelnprotocolworkflow();
//		List<LSworkflow> lstworkflow = lSlogilabprotocoldetail.getLstworkflow();

		List<LSlogilabprotocoldetail> lstPendingOrder = new ArrayList<>();

		List<LSuserteammapping> LSuserteammapping = LSuserteammappingRepositoryObj
				.findByLsuserMasterAndTeamcodeNotNull(lSlogilabprotocoldetail.getLsuserMaster());
		int pendingcount = 0;
		if (lstworkflow != null && lstworkflow.size() > 0) {

//			List<LSprotocolworkflow> lsprotocolworkflow = lSprotocolworkflowRepository
//					.findByLsprotocolworkflowgroupmapInOrderByWorkflowcodeDesc(lsworkflowgroupmapping);

			lstPendingOrder = LSlogilabprotocoldetailRepository
					.findTop10ByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "N",
							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

			lstPendingOrder.forEach((objorder) -> {

				if (lstworkflow != null && objorder.getElnprotocolworkflow() != null && lstworkflow.size() > 0) {
					// if(lstworkflow.contains(this.lsworkflow))

					List<Integer> lstprotocolworkflowcode = new ArrayList<Integer>();
					if (lstworkflow != null && lstworkflow.size() > 0) {
						lstprotocolworkflowcode = lstworkflow.stream().map(Elnprotocolworkflow::getWorkflowcode)
								.collect(Collectors.toList());

						if (lstprotocolworkflowcode.contains(objorder.getElnprotocolworkflow().getWorkflowcode())) {
							objorder.setCanuserprocess(true);
						} else {
							objorder.setCanuserprocess(false);
						}
					} else {
						objorder.setCanuserprocess(false);
					}
				} else {
					objorder.setCanuserprocess(false);
				}
			});

			pendingcount = LSlogilabprotocoldetailRepository
					.countByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetween(
							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "N",
							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
//			lstPendingOrder.forEach(objorder -> objorder
//					.setCanuserprocess(lsprotocolworkflow.equals(objorder.getlSprotocolworkflow()) ? true : false));
		} else if (LSuserteammapping != null && LSuserteammapping.size() == 0) {
			lstPendingOrder = LSlogilabprotocoldetailRepository
					.findTop10ByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "N",
							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
			lstPendingOrder.forEach(objorder -> objorder.setCanuserprocess(false));

			pendingcount = LSlogilabprotocoldetailRepository
					.countByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetween(
							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "N",
							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
		}

//		 pendingcount = LSlogilabprotocoldetailRepository.countByProtocoltypeAndOrderflagAndCreatedtimestampBetween(
//				lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getFromdate(),
//				lSlogilabprotocoldetail.getTodate());
		int completedcount = LSlogilabprotocoldetailRepository
//				.countByProtocoltypeAndSitecodeAndOrderflagAndCreatedtimestampBetween(
				.countByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetween(
						lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "R",
						lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

		int myordercount = (int) LSlogilabprotocoldetailRepository
				.countByProtocoltypeAndSitecodeAndAssignedtoAndCreatedtimestampBetween(
						lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(),
						lSlogilabprotocoldetail.getAssignedto(), lSlogilabprotocoldetail.getFromdate(),
						lSlogilabprotocoldetail.getTodate());

		int assignedcount = (int) LSlogilabprotocoldetailRepository
//				.countByProtocoltypeAndSitecodeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetween
				.countByProtocoltypeAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetween(
//				.countByProtocoltypeAndSitecodeAndOrderflagAndLsuserMasterAndCreatedtimestampBetween(
						lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(),
//				.countByOrderflagAndCreatebyAndCreatedtimestampBetween("N",
						lSlogilabprotocoldetail.getLsuserMaster(), lSlogilabprotocoldetail.getAssignedto(),
						lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

		int sharedbymecount = lsprotocolordersharedbyRepository
				.countBySharebyunifiedidAndProtocoltypeAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
						lSlogilabprotocoldetail.getUnifielduserid(), lSlogilabprotocoldetail.getProtocoltype(), 1,
						lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

		int sheredtomecount = lsprotocolordersharetoRepository
				.countBySharetounifiedidAndProtocoltypeAndSharestatusAndSharedonBetweenOrderBySharetoprotocolordercodeDesc(
						lSlogilabprotocoldetail.getUnifielduserid(), lSlogilabprotocoldetail.getProtocoltype(), 1,
						lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

		lstOrder.put("lstPendingOrder", lstPendingOrder);
		lstOrder.put("lstCompletedOrder", lstCompletedOrder);
		lstOrder.put("pendingcount", pendingcount);
		lstOrder.put("completedcount", completedcount);
		lstOrder.put("myordercount", myordercount);
		lstOrder.put("assignedordercount", assignedcount);
		lstOrder.put("sharedbymecount", sharedbymecount);
		lstOrder.put("sheredtomecount", sheredtomecount);

		return lstOrder;
	}

	@SuppressWarnings("unused")
	public Map<String, Object> getProtocolOrderListfortabchange(LSlogilabprotocoldetail lSlogilabprotocoldetail) {
		Map<String, Object> lstOrder = new HashMap<String, Object>();

		if (lSlogilabprotocoldetail.getOrderflag().equals("N")) {
//			List<LSlogilabprotocoldetail> lstPendingOrder = LSlogilabprotocoldetailRepository
//					.findTop10ByProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//							lSlogilabprotocoldetail.getProtocoltype(), "N", 
//							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

			LSusergroup userGroup = LSusergroupRepository
					.findOne(lSlogilabprotocoldetail.getObjuser().getMultiusergroupcode());

//			List<LSworkflow> lstworkflow = lSlogilabprotocoldetail.getLstworkflow();
			List<Elnprotocolworkflow> lstworkflow = lSlogilabprotocoldetail.getLstelnprotocolworkflow();

			List<LSlogilabprotocoldetail> lstPendingOrder = new ArrayList<>();

			List<LSuserteammapping> LSuserteammapping = LSuserteammappingRepositoryObj
					.findByLsuserMasterAndTeamcodeNotNull(lSlogilabprotocoldetail.getLsuserMaster());
			int pendingcount = 0;

			if (lstworkflow != null && lstworkflow.size() > 0) {
//				List<LSprotocolworkflow> lsprotocolworkflow = lSprotocolworkflowRepository
//						.findByLsprotocolworkflowgroupmapInOrderByWorkflowcodeDesc(lsworkflowgroupmapping);

				lstPendingOrder = LSlogilabprotocoldetailRepository
						.findTop10ByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
								lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "N",
								lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

//				lstPendingOrder.forEach(objorder -> objorder
//						.setCanuserprocess(lsprotocolworkflow.equals(objorder.getlSprotocolworkflow()) ? true : false));
				lstPendingOrder.forEach((objorder) -> {

					if (lstworkflow != null && objorder.getElnprotocolworkflow() != null && lstworkflow.size() > 0) {
						// if(lstworkflow.contains(this.lsworkflow))

						List<Integer> lstprotocolworkflowcode = new ArrayList<Integer>();
						if (lstworkflow != null && lstworkflow.size() > 0) {
							lstprotocolworkflowcode = lstworkflow.stream().map(Elnprotocolworkflow::getWorkflowcode)
									.collect(Collectors.toList());

							if (lstprotocolworkflowcode.contains(objorder.getElnprotocolworkflow().getWorkflowcode())) {
								objorder.setCanuserprocess(true);
							} else {
								objorder.setCanuserprocess(false);
							}
						} else {
							objorder.setCanuserprocess(false);
						}
					} else {
						objorder.setCanuserprocess(false);
					}
				});

				pendingcount = LSlogilabprotocoldetailRepository
						.countByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetween(
								lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "N",
								lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
			} else if (LSuserteammapping != null && LSuserteammapping.size() == 0) {
				lstPendingOrder = LSlogilabprotocoldetailRepository
						.findTop10ByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
								lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "N",
								lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
				lstPendingOrder.forEach(objorder -> objorder.setCanuserprocess(false));

				pendingcount = LSlogilabprotocoldetailRepository
						.countByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetween(
								lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "N",
								lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
			}

//			if (lstPendingOrder.size() == 0) {
//				pendingcount = 0;
//				lstOrder.put("pendingcount", pendingcount);
//			} else {
//				lstOrder.put("pendingcount", pendingcount);
//			}
			lstOrder.put("lstPendingOrder", lstPendingOrder);
//			lstOrder.put("pendingcount", pendingcount);
		} else if (lSlogilabprotocoldetail.getOrderflag().equals("R")) {
			int completedcount = LSlogilabprotocoldetailRepository
					.countByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetween(
							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "R",
							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
			List<LSlogilabprotocoldetail> lstCompletedOrder = LSlogilabprotocoldetailRepository
//					.findTop10ByProtocoltypeAndSitecodeAndOrderflagAndCreatedtimestampBetweenOrderByCompletedtimestampDesc(
					.findTop10ByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoIsNullAndCreatedtimestampBetweenOrderByCompletedtimestampDesc(
							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "R",
							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
			lstOrder.put("lstCompletedOrder", lstCompletedOrder);
			lstOrder.put("completedcount", completedcount);
		} else if (lSlogilabprotocoldetail.getOrderflag().equals("M")) {
//			int completedcount = (int) LSlogilabprotocoldetailRepository
//					.countByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoAndCreatedtimestampBetween(
//							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "N",
//							lSlogilabprotocoldetail.getAssignedto(), lSlogilabprotocoldetail.getFromdate(),
//							lSlogilabprotocoldetail.getTodate());

			int completedcount = (int) LSlogilabprotocoldetailRepository
					.countByProtocoltypeAndSitecodeAndAssignedtoAndCreatedtimestampBetween(
							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(),
							lSlogilabprotocoldetail.getAssignedto(), lSlogilabprotocoldetail.getFromdate(),
							lSlogilabprotocoldetail.getTodate());

//			List<LSlogilabprotocoldetail> lstCompletedOrder = LSlogilabprotocoldetailRepository
//					.findByProtocoltypeAndSitecodeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "N",
//							lSlogilabprotocoldetail.getAssignedto(), lSlogilabprotocoldetail.getFromdate(),
//							lSlogilabprotocoldetail.getTodate());

			List<Logilabprotocolorders> lstCompletedOrder = LSlogilabprotocoldetailRepository
					.findByProtocoltypeAndSitecodeAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(),
							lSlogilabprotocoldetail.getAssignedto(), lSlogilabprotocoldetail.getFromdate(),
							lSlogilabprotocoldetail.getTodate());

			lstOrder.put("lstMyOrder", lstCompletedOrder);
			lstOrder.put("myordercount", completedcount);
		} else if (lSlogilabprotocoldetail.getOrderflag().equals("A")) {
			int completedcount = (int) LSlogilabprotocoldetailRepository
//					.countByProtocoltypeAndSitecodeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetween(
					.countByProtocoltypeAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetween(
							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(),
							lSlogilabprotocoldetail.getLsuserMaster(), lSlogilabprotocoldetail.getAssignedto(),
							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

//			int completedcount = (int) LSlogilabprotocoldetailRepository
//					.countByProtocoltypeAndSitecodeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetween(
//							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "R",
//							lSlogilabprotocoldetail.getLsuserMaster(), lSlogilabprotocoldetail.getAssignedto(),
//							lSlogilabprotocoldetail.getFromdate(),
//							lSlogilabprotocoldetail.getTodate());
//			completedcount=completedcount+pendingcount;
//			List<LSlogilabprotocoldetail> lstCompletedOrder = LSlogilabprotocoldetailRepository
//					.findTop10ByOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc("N",
//							lSlogilabprotocoldetail.getLsuserMaster(),lSlogilabprotocoldetail.getAssignedto(),
//							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

			List<Logilabprotocolorders> lstCompletedOrder = LSlogilabprotocoldetailRepository
//					.findByProtocoltypeAndSitecodeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
					.findByProtocoltypeAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(),
							lSlogilabprotocoldetail.getLsuserMaster(), lSlogilabprotocoldetail.getAssignedto(),
							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

//			List<LSlogilabprotocoldetail> lstCompletedOrder = LSlogilabprotocoldetailRepository
//					.findByProtocoltypeAndSitecodeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "R",
//							lSlogilabprotocoldetail.getLsuserMaster(),lSlogilabprotocoldetail.getAssignedto(), 
//							lSlogilabprotocoldetail.getFromdate(),
//							lSlogilabprotocoldetail.getTodate());
//			if(lstCompletedOrder.size()!=0) {
//			lstCompletedOrder.addAll(0, lstpendingOrder);
//			}
			lstOrder.put("lstAssignedOrder", lstCompletedOrder);
			lstOrder.put("assignedordercount", completedcount);
		}

//		if (lSlogilabprotocoldetail.getOrderflag().equals("N")) {
//			List<LSlogilabprotocoldetail> lstPendingOrder = LSlogilabprotocoldetailRepository
//					.findTop10ByProtocoltypeAndOrderflagOrderByCreatedtimestampDesc(
//							lSlogilabprotocoldetail.getProtocoltype(), "N");
//			int pendingcount = LSlogilabprotocoldetailRepository
//					.countByProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), "N");
//			lstOrder.put("lstPendingOrder", lstPendingOrder);
//			lstOrder.put("pendingcount", pendingcount);
//		} else if (lSlogilabprotocoldetail.getOrderflag().equals("R")) {
//			int completedcount = LSlogilabprotocoldetailRepository
//					.countByProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), "R");
//			List<LSlogilabprotocoldetail> lstCompletedOrder = LSlogilabprotocoldetailRepository
//					.findTop10ByProtocoltypeAndOrderflagOrderByCreatedtimestampDesc(
//							lSlogilabprotocoldetail.getProtocoltype(), "R");
//			lstOrder.put("lstCompletedOrder", lstCompletedOrder);
//			lstOrder.put("completedcount", completedcount);
//		}
		return lstOrder;
	}

	public Map<String, Object> getreminProtocolOrderList(LSlogilabprotocoldetail lSlogilabprotocoldetail) {

		Map<String, Object> lstOrder = new HashMap<String, Object>();

//		List<LSlogilabprotocoldetail> lstreminingPendingOrder = LSlogilabprotocoldetailRepository
//				.getProtocoltypeAndOrderflagAndCreatedtimestampBetween(
//						lSlogilabprotocoldetail.getProtocoltype(), "N", 
//						lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

		LSusergroup userGroup = LSusergroupRepository
				.findOne(lSlogilabprotocoldetail.getObjuser().getMultiusergroupcode());

		List<LSprotocolworkflowgroupmap> lsworkflowgroupmapping = LSprotocolworkflowgroupmapRepository
				.findBylsusergroupAndWorkflowcodeNotNull(userGroup);

		List<LSlogilabprotocoldetail> lstreminingPendingOrder = new ArrayList<>();
		List<LSuserteammapping> LSuserteammapping = LSuserteammappingRepositoryObj
				.findByLsuserMasterAndTeamcodeNotNull(lSlogilabprotocoldetail.getLsuserMaster());
//		int pendingcount=0;

		if (lsworkflowgroupmapping != null && lsworkflowgroupmapping.size() > 0) {
//			LSprotocolworkflow lsprotocolworkflow = lSprotocolworkflowRepository
//					.findByworkflowcode(lsworkflowgroupmapping.get(0).getWorkflowcode());

			List<LSprotocolworkflow> lsprotocolworkflow = lSprotocolworkflowRepository
					.findByLsprotocolworkflowgroupmapInOrderByWorkflowcodeDesc(lsworkflowgroupmapping);

			lstreminingPendingOrder = LSlogilabprotocoldetailRepository
					.getProtocoltypeAndSitecodeAndOrderflagAndCreatedtimestampBetween(
							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "N",
							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

//			lstreminingPendingOrder.forEach(objorder -> objorder
//					.setCanuserprocess(lsprotocolworkflow.equals(objorder.getlSprotocolworkflow()) ? true : false));

			lstreminingPendingOrder.forEach((objorder) -> {

				if (lsprotocolworkflow != null && objorder.getlSprotocolworkflow() != null
						&& lsprotocolworkflow.size() > 0) {
					// if(lstworkflow.contains(this.lsworkflow))

					List<Integer> lstprotocolworkflowcode = new ArrayList<Integer>();
					if (lsprotocolworkflow != null && lsprotocolworkflow.size() > 0) {
						lstprotocolworkflowcode = lsprotocolworkflow.stream().map(LSprotocolworkflow::getWorkflowcode)
								.collect(Collectors.toList());

						if (lstprotocolworkflowcode.contains(objorder.getlSprotocolworkflow().getWorkflowcode())) {
							objorder.setCanuserprocess(true);
						} else {
							objorder.setCanuserprocess(false);
						}
					} else {
						objorder.setCanuserprocess(false);
					}
				} else {
					objorder.setCanuserprocess(false);
				}
			});
		} else if (LSuserteammapping != null && LSuserteammapping.size() == 0) {
			lstreminingPendingOrder = LSlogilabprotocoldetailRepository
					.getProtocoltypeAndSitecodeAndOrderflagAndCreatedtimestampBetween(
							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "N",
							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
			lstreminingPendingOrder.forEach(objorder -> objorder.setCanuserprocess(false));
		}

		List<LSlogilabprotocoldetail> lstreminingCompletedOrder = LSlogilabprotocoldetailRepository
				.getProtocoltypeAndSitecodeAndOrderflagAndCreatedtimestampBetween(
						lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "R",
						lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

		lstOrder.put("lstreminingPendingOrder", lstreminingPendingOrder);
		lstOrder.put("lstreminingCompletedOrder", lstreminingCompletedOrder);
//		lstOrder.put("pendingcount", pendingcount);
//		lstOrder.put("completedcount", completedcount);

		return lstOrder;
	}

	public Map<String, Object> getreminProtocolOrderListontab(LSlogilabprotocoldetail lSlogilabprotocoldetail) {
		Map<String, Object> lstOrder = new HashMap<String, Object>();

		if (lSlogilabprotocoldetail.getOrderflag().equals("N")) {
			LSusergroup userGroup = LSusergroupRepository
					.findOne(lSlogilabprotocoldetail.getObjuser().getMultiusergroupcode());

			List<LSprotocolworkflowgroupmap> lsworkflowgroupmapping = LSprotocolworkflowgroupmapRepository
					.findBylsusergroupAndWorkflowcodeNotNull(userGroup);

			List<LSlogilabprotocoldetail> lstreminingPendingOrder = new ArrayList<>();
			List<LSuserteammapping> LSuserteammapping = LSuserteammappingRepositoryObj
					.findByLsuserMasterAndTeamcodeNotNull(lSlogilabprotocoldetail.getLsuserMaster());

			if (lsworkflowgroupmapping != null && lsworkflowgroupmapping.size() > 0) {
//				LSprotocolworkflow lsprotocolworkflow = lSprotocolworkflowRepository
//						.findByworkflowcode(lsworkflowgroupmapping.get(0).getWorkflowcode());
				List<LSprotocolworkflow> lsprotocolworkflow = lSprotocolworkflowRepository
						.findByLsprotocolworkflowgroupmapInOrderByWorkflowcodeDesc(lsworkflowgroupmapping);

				lstreminingPendingOrder = LSlogilabprotocoldetailRepository
						.getProtocoltypeAndSitecodeAndOrderflagAndCreatedtimestampBetween(
								lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "N",
								lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

//				lstreminingPendingOrder.forEach(objorder -> objorder
//						.setCanuserprocess(lsprotocolworkflow.equals(objorder.getlSprotocolworkflow()) ? true : false));

				lstreminingPendingOrder.forEach((objorder) -> {

					if (lsprotocolworkflow != null && objorder.getlSprotocolworkflow() != null
							&& lsprotocolworkflow.size() > 0) {
						// if(lstworkflow.contains(this.lsworkflow))

						List<Integer> lstprotocolworkflowcode = new ArrayList<Integer>();
						if (lsprotocolworkflow != null && lsprotocolworkflow.size() > 0) {
							lstprotocolworkflowcode = lsprotocolworkflow.stream()
									.map(LSprotocolworkflow::getWorkflowcode).collect(Collectors.toList());

							if (lstprotocolworkflowcode.contains(objorder.getlSprotocolworkflow().getWorkflowcode())) {
								objorder.setCanuserprocess(true);
							} else {
								objorder.setCanuserprocess(false);
							}
						} else {
							objorder.setCanuserprocess(false);
						}
					} else {
						objorder.setCanuserprocess(false);
					}
				});

			} else if (LSuserteammapping != null && LSuserteammapping.size() == 0) {
				lstreminingPendingOrder = LSlogilabprotocoldetailRepository
						.getProtocoltypeAndSitecodeAndOrderflagAndCreatedtimestampBetween(
								lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "N",
								lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

				lstreminingPendingOrder.forEach(objorder -> objorder.setCanuserprocess(false));
			}

			lstOrder.put("lstreminingPendingOrder", lstreminingPendingOrder);
		} else if (lSlogilabprotocoldetail.getOrderflag().equals("R")) {
			List<LSlogilabprotocoldetail> lstreminingCompletedOrder = LSlogilabprotocoldetailRepository
					.getProtocoltypeAndSitecodeAndOrderflagAndCreatedtimestampBetween(
							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(), "R",
							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
			lstOrder.put("lstreminingCompletedOrder", lstreminingCompletedOrder);
		}
		return lstOrder;
	}

	public Map<String, Object> updateProtocolOrderStep(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();

		LSlogilabprotocolsteps LSprotocolstepObj = new ObjectMapper().convertValue(argObj.get("ProtocolOrderStepObj"),
				new TypeReference<LSlogilabprotocolsteps>() {
				});

		LSlogilabprotocolstepsRepository.save(LSprotocolstepObj);

		CloudLsLogilabprotocolstepInfo CloudLSprotocolstepInfoObj = new CloudLsLogilabprotocolstepInfo();
		if (LSprotocolstepObj.getIsmultitenant() == 1 || LSprotocolstepObj.getIsmultitenant() == 2) {
			if (LSprotocolstepObj.getNewStep() == 0) {
				CloudLsLogilabprotocolstepInfo updateLSprotocolstepInfo = CloudLsLogilabprotocolstepInfoRepository
						.findById(LSprotocolstepObj.getProtocolorderstepcode());
				updateLSprotocolstepInfo.setLsprotocolstepInfo(LSprotocolstepObj.getLsprotocolstepInfo());
				CloudLsLogilabprotocolstepInfoRepository.save(updateLSprotocolstepInfo);
			} else {

				CloudLSprotocolstepInfoObj.setId(LSprotocolstepObj.getProtocolorderstepcode());
				CloudLSprotocolstepInfoObj.setLsprotocolstepInfo(LSprotocolstepObj.getLsprotocolstepInfo());
				CloudLsLogilabprotocolstepInfoRepository.save(CloudLSprotocolstepInfoObj);
			}
		} else {
			if (LSprotocolstepObj.getNewStep() == 0) {
				Query query = new Query(Criteria.where("id").is(LSprotocolstepObj.getProtocolorderstepcode()));
				Update update = new Update();
				update.set("content", LSprotocolstepObj.getLsprotocolstepInfo());

				mongoTemplate.upsert(query, update, LsLogilabprotocolstepInfo.class);
			} else {
				LsLogilabprotocolstepInfo LsLogilabprotocolstepInfoObj = new LsLogilabprotocolstepInfo();

				LsLogilabprotocolstepInfoObj.setId(LSprotocolstepObj.getProtocolorderstepcode());
				LsLogilabprotocolstepInfoObj.setContent(LSprotocolstepObj.getLsprotocolstepInfo());
				mongoTemplate.insert(LsLogilabprotocolstepInfoObj);
			}
		}
		if (argObj.containsKey("modifiedsamplestep")) {
//		LSprotocolordersampleupdates sample = new ObjectMapper().convertValue(argObj.get("modifiedsamplestep"),
//				new TypeReference<LSprotocolordersampleupdates>() {
//				});
			List<LSprotocolordersampleupdates> lsprotocolordersampleupdates = new ObjectMapper().convertValue(
					argObj.get("modifiedsamplestep"), new TypeReference<List<LSprotocolordersampleupdates>>() {
					});
			for (LSprotocolordersampleupdates sample : lsprotocolordersampleupdates) {
				sample.setProtocolstepcode(LSprotocolstepObj.getProtocolstepcode());
				sample.setProtocolmastercode(LSprotocolstepObj.getProtocolmastercode());
				lsprotocolordersampleupdatesRepository.save(sample);
			}

//		 LSprotocolsampleupdatesRepository.save(sample);

		}
		if (argObj.containsKey("repositorydata")) {
//		Lsrepositoriesdata lsrepositoriesdata = new ObjectMapper().convertValue(argObj.get("repositorydata"),
//				new TypeReference<Lsrepositoriesdata>() {
//				});
			List<Lsrepositoriesdata> lsrepositoriesdata = new ObjectMapper().convertValue(argObj.get("repositorydata"),
					new TypeReference<List<Lsrepositoriesdata>>() {
					});
			for (Lsrepositoriesdata lsrepositoriesdataobj : lsrepositoriesdata) {
				LsrepositoriesdataRepository.save(lsrepositoriesdataobj);
			}

		}

//	List<LSlogilabprotocolsteps> LSprotocolsteplst = LSlogilabprotocolstepsRepository
//			.findByProtocolordercode(LSprotocolstepObj.getProtocolordercode());
		List<LSlogilabprotocolsteps> LSprotocolsteplst = LSlogilabprotocolstepsRepository
				.findByProtocolordercodeAndStatus(LSprotocolstepObj.getProtocolordercode(), 1);
		int countforstep = LSlogilabprotocolstepsRepository
				.countByProtocolordercodeAndStatus(LSprotocolstepObj.getProtocolordercode(), 1);
		List<LSlogilabprotocolsteps> LSprotocolstepLst = new ArrayList<LSlogilabprotocolsteps>();

		for (LSlogilabprotocolsteps LSprotocolstepObj1 : LSprotocolsteplst) {

			if (LSprotocolstepObj.getIsmultitenant() == 1 || LSprotocolstepObj.getIsmultitenant() == 2) {

				if (LSprotocolstepObj.getNewStep() == 1) {
					LSprotocolstepObj1.setLsprotocolstepInfo(CloudLSprotocolstepInfoObj.getLsprotocolstepInfo());
				} else {
					CloudLsLogilabprotocolstepInfo newLSprotocolstepInfo = CloudLsLogilabprotocolstepInfoRepository
							.findById(LSprotocolstepObj1.getProtocolorderstepcode());
					if (newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
					}
				}
			} else {
				LsLogilabprotocolstepInfo newLSprotocolstepInfo = mongoTemplate
						.findById(LSprotocolstepObj1.getProtocolorderstepcode(), LsLogilabprotocolstepInfo.class);
				if (newLSprotocolstepInfo != null) {
					LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
				}
			}

			LSprotocolstepLst.add(LSprotocolstepObj1);
		}

		mapObj.put("protocolstepLst", LSprotocolstepLst);
		mapObj.put("countforstep", countforstep);

		return mapObj;
	}

	@SuppressWarnings("unused")
	public Map<String, Object> getProtocolOrderStepLst(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
//		LScfttransaction LScfttransactionobj = new LScfttransaction();
//		if (argObj.containsKey("objsilentaudit")) {
//			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
//					new TypeReference<LScfttransaction>() {
//					});
		String Content = "";
		if (argObj.containsKey("ismultitenant")) {
			ObjectMapper objm = new ObjectMapper();
			int multitenent = objm.convertValue(argObj.get("ismultitenant"), Integer.class);

			int countforstep = 0;
			long ipInt = ((Number) argObj.get("protocolmastercode")).longValue();
			List<LSlogilabprotocolsteps> LSprotocolsteplst = new ArrayList<LSlogilabprotocolsteps>();
			if (argObj.containsKey("getfirstlist")) {
				int firstrecord = (int) argObj.get("getfirstlist");
				if (firstrecord == 1) {
					LSprotocolsteplst = LSlogilabprotocolstepsRepository
							.findByProtocolordercodeAndStatusAndStepno(ipInt, 1, 1);
					countforstep = LSlogilabprotocolstepsRepository.countByProtocolordercodeAndStatus(ipInt, 1);
				} else {
					LSprotocolsteplst = LSlogilabprotocolstepsRepository
							.findByProtocolordercodeAndStatusAndStepnoNot(ipInt, 1, 1);
					countforstep = LSlogilabprotocolstepsRepository.countByProtocolordercodeAndStatus(ipInt, 1);
				}
			} else {
				LSprotocolsteplst = LSlogilabprotocolstepsRepository.findByProtocolordercode(ipInt);
			}

			List<LSlogilabprotocolsteps> LSprotocolstepLst = new ArrayList<LSlogilabprotocolsteps>();

//			if(!LSprotocolsteplst.isEmpty()) {
			for (LSlogilabprotocolsteps LSprotocolstepObj1 : LSprotocolsteplst) {

				if (multitenent == 1 || multitenent == 2) {

					CloudLsLogilabprotocolstepInfo newLSprotocolstepInfo = CloudLsLogilabprotocolstepInfoRepository
							.findById(LSprotocolstepObj1.getProtocolorderstepcode());
					List<LSprotocolorderimages> lsprotocolorderimages = lsprotocolorderimagesRepository
							.findByProtocolorderstepcode(LSprotocolstepObj1.getProtocolorderstepcode());
					if (newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
					}
					if (lsprotocolorderimages.size() != 0) {
						LSprotocolstepObj1.setLsprotocolorderimages(lsprotocolorderimages);
					}
				} else {

					LsLogilabprotocolstepInfo newLSprotocolstepInfo = mongoTemplate
							.findById(LSprotocolstepObj1.getProtocolorderstepcode(), LsLogilabprotocolstepInfo.class);
					if (newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
					}
				}

				// Collections.sort(LSprotocolorderversion, Collections.reverseOrder());

				LSprotocolstepLst.add(LSprotocolstepObj1);

			}
//			} else {
			if (multitenent == 1 || multitenent == 2) {
				LSlogilabprotocoldetail lslogilabprotocoldetail = LSlogilabprotocoldetailRepository
						.findByProtocolordercode(ipInt);
				LSuserMaster createby = lsusermasterRepository.findByusercode(lslogilabprotocoldetail.getCreateby());
				lslogilabprotocoldetail.setCreatedbyusername(createby.getUsername());
				mapObj.put("orderDetail", lslogilabprotocoldetail);

				List<LSlogilablimsorder> lslogilablimsorder = lslogilablimsorderrepo
						.findBybatchid(lslogilabprotocoldetail.getProtoclordername());
				lslogilabprotocoldetail.setLsLSlogilablimsorder(lslogilablimsorder);

				try {
					Content = objCloudFileManipulationservice.retrieveCloudSheets(lslogilabprotocoldetail.getFileuid(),
							commonfunction.getcontainername(multitenent, TenantContext.getCurrentTenant())
									+ "protocolorder");
					mapObj.put("protocolData", Content);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				LSlogilabprotocoldetail lslogilabprotocoldetail = LSlogilabprotocoldetailRepository
						.findByProtocolordercode(ipInt);
				LSuserMaster createby = lsusermasterRepository.findByusercode(lslogilabprotocoldetail.getCreateby());
				lslogilabprotocoldetail.setCreatedbyusername(createby.getUsername());
				mapObj.put("orderDetail", lslogilabprotocoldetail);

				List<LSlogilablimsorder> lslogilablimsorder = lslogilablimsorderrepo
						.findBybatchid(lslogilabprotocoldetail.getProtoclordername());
				lslogilabprotocoldetail.setLsLSlogilablimsorder(lslogilablimsorder);

//				Lsprotocolorderdata lsprotocolorderdata = mongoTemplate
//						.findById(lslogilabprotocoldetail.getProtocolordercode(), Lsprotocolorderdata.class);
//				mapObj.put("protocolData", lsprotocolorderdata.getContent());

				GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename")
						.is("protocolorder_" + lslogilabprotocoldetail.getProtocolordercode())));
				mapObj.put("protocolData",
						new BufferedReader(new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8))
								.lines().collect(Collectors.joining("\n")));
			}
//			}

			List<LSprotocolorderversion> LSprotocolorderversion = lsprotocolorderversionRepository
					.findByProtocolordercodeOrderByVersionnoDesc(ipInt);
			if (LSprotocolorderversion != null) {
				mapObj.put("protocolorderversionLst", LSprotocolorderversion);
			}
			if (LSprotocolsteplst != null) {
				mapObj.put("protocolstepLst", LSprotocolstepLst);
				mapObj.put("countforstep", countforstep);
			} else {
				mapObj.put("protocolstepLst", new ArrayList<>());
			}
		}
		return mapObj;
	}

	public Map<String, Object> getAllMasters(LSuserMaster objuser) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();

		mapOrders.put("test", masterService.getTestmaster(objuser));
		mapOrders.put("sample", masterService.getsamplemaster(objuser));
		mapOrders.put("project", masterService.getProjectmaster(objuser));
//		mapOrders.put("sheets", GetApprovedSheets(0,objuser));
		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("LSfiletest");
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}
		return mapOrders;
	}

	public LSprotocolorderstephistory startStep(LSprotocolorderstephistory objuser) {

		if (objuser.getCreateby().getObjsilentaudit() != null) {
			objuser.getCreateby().getObjsilentaudit().setTableName("lslogilabprotocolsteps");
			try {
				objuser.getCreateby().getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(objuser.getCreateby().getObjsilentaudit());
		}
		try {
			objuser.setStepstartdate(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lsprotocolorderstephistoryRepository.save(objuser);
		return objuser;
	}

	public Map<String, Object> updateStepStatus(Map<String, Object> argMap) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if (argMap.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argMap.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			LSlogilabprotocolsteps LSlogilabprotocolstepsObj = new ObjectMapper()
					.convertValue(argMap.get("protocolstep"), new TypeReference<LSlogilabprotocolsteps>() {
					});
			LSlogilabprotocolstepsRepository.save(LSlogilabprotocolstepsObj);

//			mapOrders = getProtocolOrderStepLst(argMap);
			LScfttransactionobj.setTableName("lslogilabprotocolsteps");
			lscfttransactionRepository.save(LScfttransactionobj);
		}
		return mapOrders;
	}

//	public Map<String, Object> updateOrderStatus(Map<String, Object> argMap)
//	{
//		Map<String, Object> mapOrders = new HashMap<String, Object>();
//		LScfttransaction LScfttransactionobj = new LScfttransaction();
//		if(argMap.containsKey("objsilentaudit")) {
//			LScfttransactionobj = new ObjectMapper().convertValue(argMap.get("objsilentaudit"),
//					new TypeReference<LScfttransaction>() {
//					});
//			LSlogilabprotocoldetail newProtocolOrderObj = new ObjectMapper().
//					convertValue(argMap.get("ProtocolOrderObj"),new TypeReference<LSlogilabprotocoldetail>(){});
//			LSlogilabprotocoldetailRepository.save(newProtocolOrderObj);
//			LScfttransactionobj.setTableName("LSlogilabprotocoldetail");
//    		lscfttransactionRepository.save(LScfttransactionobj);
//		}
//		return mapOrders;
//	}

	public Map<String, Object> updateOrderStatus(LSlogilabprotocoldetail argMap) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();
//		LScfttransaction LScfttransactionobj = new LScfttransaction();
//		if (argMap.getObjsilentaudit() != null) {
//			LScfttransactionobj = argMap.getObjsilentaudit();
		if (argMap.getActivekey() != null && argMap.getActivekey() == 5) {
			Lsprotocolordersharedby obj = lsprotocolordersharedbyRepository
					.findByShareprotocolordercode(argMap.getProtocolordercode());
			Lsprotocolordershareto objto = lsprotocolordersharetoRepository
					.findByShareprotocolordercode(argMap.getProtocolordercode());
			obj.setOrderflag(argMap.getOrderflag());
			objto.setOrderflag(argMap.getOrderflag());
			lsprotocolordersharedbyRepository.save(obj);
			lsprotocolordersharetoRepository.save(objto);
		}

		LSlogilabprotocoldetail lslogilabprotocoldetail = LSlogilabprotocoldetailRepository
				.findByProtocolordercode(argMap.getProtocolordercode());
		lslogilabprotocoldetail.setOrderflag(argMap.getOrderflag());
//		lslogilabprotocoldetail.setCompletedtimestamp(argMap.getCompletedtimestamp());
		try {
			lslogilabprotocoldetail.setCompletedtimestamp(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LSlogilabprotocoldetailRepository.save(lslogilabprotocoldetail);

		if (lslogilabprotocoldetail.getLsordernotification() != null) {
			lslogilabprotocoldetail.getLsordernotification().setIscompleted(true);
			lsordernotificationrepo.save(lslogilabprotocoldetail.getLsordernotification());
		}

//			LScfttransactionobj.setTableName("LSlogilabprotocoldetail");
		mapOrders.put("curentprotocolorder", argMap);

//			lscfttransactionRepository.save(LScfttransactionobj);
//		}
		try {
			lslogilabprotocoldetail.setLsuserMaster(argMap.getLsuserMaster());
			updatenotificationforprotocolorder(lslogilabprotocoldetail);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return mapOrders;
	}

	public Map<String, Object> getLsrepositoriesLst(Map<String, Object> argMap) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if (argMap.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argMap.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			List<Lsrepositories> LsrepositoriesLst = LsrepositoriesRepository
					.findBySitecodeOrderByRepositorycodeAsc(LScfttransactionobj.getLssitemaster());
			if (LsrepositoriesLst.size() > 0) {
				mapObj.put("LsrepositoriesLst", LsrepositoriesLst);
//				List<Lsrepositoriesdata> LsrepositoriesdataLst=LsrepositoriesdataRepository.findByRepositorycodeAndSitecodeOrderByRepositorydatacodeDesc(LsrepositoriesLst.get(0).getRepositorycode(), LScfttransactionobj.getLssitemaster());
				Map<String, Object> temp = new HashMap<>();
				temp.put("objsilentaudit", LScfttransactionobj);
				temp.put("LsrepositoriesObj", LsrepositoriesLst.get(0));
				mapObj.putAll(getLsrepositoriesDataLst(temp));
			} else {
				mapObj.put("LsrepositoriesLst", LsrepositoriesLst);
				mapObj.put("LsrepositoriesdataLst", new ArrayList<>());
			}

		}
		return mapObj;
	}

	public Map<String, Object> getLsrepositoriesDataLst(Map<String, Object> argMap) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if (argMap.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argMap.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			Lsrepositories LsrepositoriesLst = new ObjectMapper().convertValue(argMap.get("LsrepositoriesObj"),
					new TypeReference<Lsrepositories>() {
					});
			if (LsrepositoriesLst.getRepositorycode() > 0) {
				List<Lsrepositoriesdata> LsrepositoriesdataLst = LsrepositoriesdataRepository
						.findByRepositorycodeAndSitecodeAndItemstatusOrderByRepositorydatacodeDesc(
								LsrepositoriesLst.getRepositorycode(), LScfttransactionobj.getLssitemaster(), 1);
				mapObj.put("LsrepositoriesdataLst", LsrepositoriesdataLst);
			} else {
				mapObj.put("LsrepositoriesdataLst", new ArrayList<>());
			}

		}
		return mapObj;
	}

	public Map<String, Object> GetProtocolTransactionDetails(LSprotocolmaster lSprotocolmaster) {

		Map<String, Object> mapObj = new HashMap<String, Object>();
		LSprotocolmaster LSprotocolmasterrecord = LSProtocolMasterRepositoryObj
				.findFirstByProtocolmastercode(lSprotocolmaster.getProtocolmastercode());
		LSprotocolmaster LSprotocolmasterrecordtransaction = LSProtocolMasterRepositoryObj
				.findFirstByProtocolmastercodeAndCreatedateBetween(lSprotocolmaster.getProtocolmastercode(),
						lSprotocolmaster.getFromdate(), lSprotocolmaster.getTodate());
		LSuserMaster createby = lSuserMasterRepository.findByusercode(LSprotocolmasterrecord.getCreatedby());
		List<LSprotocolworkflowhistory> lsprotocolworkflowhistory = lsprotocolworkflowhistoryRepository
				.findByProtocolmastercodeAndCreatedateBetween(lSprotocolmaster.getProtocolmastercode(),
						lSprotocolmaster.getFromdate(), lSprotocolmaster.getTodate());
		List<LSprotocolupdates> modifiedlist = lsprotocolupdatesRepository
				.findByprotocolmastercode(lSprotocolmaster.getProtocolmastercode());

		List<LSprotocolversion> lsprotocolversion = lsprotocolversionRepository
				.findByprotocolmastercodeAndCreatedateBetween(lSprotocolmaster.getProtocolmastercode(),
						lSprotocolmaster.getFromdate(), lSprotocolmaster.getTodate());
		mapObj.put("createby", createby);
		mapObj.put("lsprotocolworkflowhistory", lsprotocolworkflowhistory);
		mapObj.put("modifiedlist", modifiedlist);
		mapObj.put("lsprotocolversion", lsprotocolversion);
		mapObj.put("lsprotocoltransaction", LSprotocolmasterrecordtransaction);

		return mapObj;
	}

	public Map<String, Object> addProtocolStepforsaveas(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
				new TypeReference<LScfttransaction>() {
				});
		int multitenent = new ObjectMapper().convertValue(argObj.get("ismultitenant"), Integer.class);
		List<LSprotocolstep> LSprotocolsteplst = LSProtocolStepRepositoryObj
				.findByProtocolmastercodeAndStatus(argObj.get("olsprotocolmastercode"), 1);
		LSprotocolstepInformation LSprotocolstepInformation = new LSprotocolstepInformation();
		LSprotocolstepInfo newLSprotocolstepInfo = new LSprotocolstepInfo();
		@SuppressWarnings("unused")
		List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();
		int i = 0;
		for (LSprotocolstep LSprotocolstepObj1 : LSprotocolsteplst) {
			if (multitenent == 1 || multitenent == 2) {
				LSprotocolstepInformation = lsprotocolstepInformationRepository
						.findById(LSprotocolstepObj1.getProtocolstepcode());
			} else {
				newLSprotocolstepInfo = mongoTemplate.findById(LSprotocolstepObj1.getProtocolstepcode(),
						LSprotocolstepInfo.class);
			}

			LSprotocolstep LSprotocolstep = new LSprotocolstep();
			LSprotocolstep LSprotocolstepObj = new LSprotocolstep();
			int newprotocolmastercode = new ObjectMapper().convertValue(argObj.get("newprotocolmastercode"),
					Integer.class);
			LSprotocolstepObj.setProtocolmastercode(newprotocolmastercode);
			LSuserMaster LsuserMasterObj = LSuserMasterRepositoryObj
					.findByusercode(LScfttransactionobj.getLsuserMaster());
			if (LSprotocolstepObj.getStatus() == null) {

				LSprotocolstep.setProtocolmastercode(LSprotocolstepObj.getProtocolmastercode());
				LSprotocolstep.setStepno(LSprotocolstepObj1.getStepno());
				LSprotocolstep.setProtocolstepname(LSprotocolstepObj1.getProtocolstepname());

				LSprotocolstep.setStatus(1);
				LSprotocolstep.setCreatedby(LScfttransactionobj.getLsuserMaster());
				LSprotocolstep.setCreatedbyusername(LsuserMasterObj.getUsername());
				LSprotocolstep.setCreateddate(new Date());
				LSprotocolstep.setSitecode(LScfttransactionobj.getLssitemaster());
				LSprotocolstep.setNewStep(1);
			}
			LSProtocolStepRepositoryObj.save(LSprotocolstep);
			List<LSprotocolsampleupdates> sampleupdatelst = new ArrayList<LSprotocolsampleupdates>();
			if (LSprotocolstep.getProtocolstepcode() != null) {
				sampleupdatelst = LSprotocolsampleupdatesRepository.findByprotocolstepcodeAndProtocolmastercode(
						LSprotocolstepObj1.getProtocolstepcode(), argObj.get("olsprotocolmastercode"));
				for (LSprotocolsampleupdates sampleupdate : sampleupdatelst) {
					sampleupdate.setProtocolmastercode(LSprotocolstep.getProtocolmastercode());
					sampleupdate.setProtocolstepcode(LSprotocolstep.getProtocolstepcode());
					LSprotocolsampleupdatesRepository.save(sampleupdate);
				}
			}
			LSprotocolstepversion protoVersStep = new LSprotocolstepversion();
			CloudLSprotocolversionstep cloudStepVersion = new CloudLSprotocolversionstep();
			LSprotocolversionstepInfo LsLogilabprotocolstepInfoObj = new LSprotocolversionstepInfo();
			protoVersStep.setProtocolmastercode(LSprotocolstep.getProtocolmastercode());
			protoVersStep.setProtocolstepcode(LSprotocolstep.getProtocolstepcode());
			protoVersStep.setProtocolstepname(LSprotocolstep.getProtocolstepname());
			protoVersStep.setStatus(LSprotocolstep.getStatus());
			protoVersStep.setStepno(LSprotocolstep.getStepno());
			protoVersStep.setVersionno(1);

			LSprotocolstepversionRepository.save(protoVersStep);
			if ((multitenent == 1 || multitenent == 2) && LSprotocolstepInformation != null) {
				LSprotocolstepInformation CloudLSprotocolstepInfoforinsert = new LSprotocolstepInformation();
				CloudLSprotocolstepInfoforinsert.setId(LSprotocolstep.getProtocolstepcode());
				CloudLSprotocolstepInfoforinsert
						.setLsprotocolstepInfo(LSprotocolstepInformation.getLsprotocolstepInfo());
				lsprotocolstepInformationRepository.save(CloudLSprotocolstepInfoforinsert);

				cloudStepVersion.setId(protoVersStep.getProtocolstepversioncode());
				cloudStepVersion.setProtocolmastercode(LSprotocolstep.getProtocolmastercode());
				cloudStepVersion.setLsprotocolstepInfo(LSprotocolstepInformation.getLsprotocolstepInfo());
				cloudStepVersion.setVersionname("version_" + 1);
				cloudStepVersion.setVersionno(1);

				CloudLSprotocolversionstepRepository.save(cloudStepVersion);

			} else if (newLSprotocolstepInfo != null && multitenent == 0) {
				Query query = new Query(Criteria.where("id").is(LSprotocolstep.getProtocolstepcode()));
				Update update = new Update();
				update.set("content", newLSprotocolstepInfo.getContent());

				mongoTemplate.upsert(query, update, LSprotocolstepInfo.class);

				LsLogilabprotocolstepInfoObj.setContent(LSprotocolstepInformation.getLsprotocolstepInfo());
				LsLogilabprotocolstepInfoObj.setId(protoVersStep.getProtocolstepversioncode());
				LsLogilabprotocolstepInfoObj.setStepcode(protoVersStep.getProtocolstepcode());
				LsLogilabprotocolstepInfoObj.setVersionno(1);
				mongoTemplate.insert(LsLogilabprotocolstepInfoObj);

			}
//			List<LSprotocolstep> LSprotocolsteplstforsecond = LSProtocolStepRepositoryObj
//					.findByProtocolmastercodeAndStatus(LSprotocolstepObj.getProtocolmastercode(), 1);
//			for (int j = i; j < LSprotocolsteplstforsecond.size(); j++) {
//				if (multitenent == 1 && LSprotocolstepInformation != null) {
//					LSprotocolstepInformation CloudLSprotocolstepInfoforinsert = new LSprotocolstepInformation();
//					CloudLSprotocolstepInfoforinsert.setId(LSprotocolsteplstforsecond.get(i).getProtocolstepcode());
//					CloudLSprotocolstepInfoforinsert
//							.setLsprotocolstepInfo(LSprotocolstepInformation.getLsprotocolstepInfo());
//					lsprotocolstepInformationRepository.save(CloudLSprotocolstepInfoforinsert);
//					i++;
//					break;
//				} else if (newLSprotocolstepInfo != null && multitenent == 0) {
//
//					Query query = new Query(
//							Criteria.where("id").is(LSprotocolsteplstforsecond.get(i).getProtocolstepcode()));
//					Update update = new Update();
//					update.set("content", newLSprotocolstepInfo.getContent());
//
//					mongoTemplate.upsert(query, update, LSprotocolstepInfo.class);
//					i++;
//					break;
//				}
//			}

		}

		return mapObj;
	}

	public Map<String, Object> GetProtocolResourcesQuantitylst(LSprotocolstep LSprotocolstep) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		List<LSprotocolsampleupdates> sampleupdatelst = new ArrayList<LSprotocolsampleupdates>();
		if (LSprotocolstep.getProtocolstepcode() != null) {
			sampleupdatelst = LSprotocolsampleupdatesRepository
					.findByprotocolstepcodeAndIndexofIsNotNullAndStatus(LSprotocolstep.getProtocolstepcode(), 1);
			mapObj.put("sampleupdatelst", sampleupdatelst);

			List<LSprotocolsampleupdates> retiredsampleupdatelst = LSprotocolsampleupdatesRepository
					.findByprotocolstepcodeAndIndexofIsNotNullAndStatus(LSprotocolstep.getProtocolstepcode(), 0);
			mapObj.put("retiredsampleupdatelst", retiredsampleupdatelst);
		}
		return mapObj;
	}

	public Map<String, Object> GetProtocolVersionDetails(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LSprotocolversion versiondetail = new LSprotocolversion();
		if (argObj.containsKey("lsprotocolversion")) {
			versiondetail = new ObjectMapper().convertValue(argObj.get("lsprotocolversion"),
					new TypeReference<LSprotocolversion>() {
					});
		}
		List<LSprotocolstep> LSprotocolsteplst = LSProtocolStepRepositoryObj
				.findByProtocolmastercodeAndStatus(versiondetail.getProtocolmastercode(), 1);

		Integer ismultitenant = new ObjectMapper().convertValue(argObj.get("ismultitenant"),
				new TypeReference<Integer>() {
				});

		String mangoversioncontent = "";
		CloudLSprotocolversionstep versioncontent = new CloudLSprotocolversionstep();

		if (ismultitenant == 1 || ismultitenant == 2) {
			versioncontent = CloudLSprotocolversionstepRepository
					.findByVersionno(versiondetail.getProtocolversioncode());

			if (versioncontent != null) {
				mangoversioncontent = versioncontent.getLsprotocolstepInfo();
			}
		} else {
			LSprotocolversionstepInfo newLSprotocolstepInfo = mongoTemplate
					.findById(versiondetail.getProtocolversioncode(), LSprotocolversionstepInfo.class);
			if (newLSprotocolstepInfo != null) {
				mangoversioncontent = newLSprotocolstepInfo.getContent();
			}

		}

		mapObj.put("mangoversioncontent", mangoversioncontent);
		mapObj.put("LSprotocolsteplst", LSprotocolsteplst);
		return mapObj;
	}

	public Map<String, Object> GetProtocolorderResourcesQuantitylst(LSlogilabprotocolsteps LSlogilabprotocolsteps) {

//		List<LSprotocolordersampleupdates> sampleupdatelst = new ArrayList<LSprotocolordersampleupdates>();
//		if (LSlogilabprotocolsteps.getProtocolordercode() != null) {
//			sampleupdatelst = lsprotocolordersampleupdatesRepository.findByprotocolordercodeAndProtocolstepcode(
//					LSlogilabprotocolsteps.getProtocolordercode(), LSlogilabprotocolsteps.getProtocolstepcode());
//		}
//		return sampleupdatelst;		
		Map<String, Object> mapObj = new HashMap<String, Object>();
		List<LSprotocolordersampleupdates> sampleupdatelst = new ArrayList<LSprotocolordersampleupdates>();
		if (LSlogilabprotocolsteps.getProtocolordercode() != null) {
			sampleupdatelst = lsprotocolordersampleupdatesRepository
					.findByProtocolordercodeAndProtocolstepcodeAndIndexofIsNotNullAndStatus(
							LSlogilabprotocolsteps.getProtocolordercode(), LSlogilabprotocolsteps.getProtocolstepcode(),
							1);
			mapObj.put("sampleupdatelst", sampleupdatelst);

			List<LSprotocolordersampleupdates> retiredsampleupdatelst = lsprotocolordersampleupdatesRepository
					.findByProtocolstepcodeAndIndexofIsNotNullAndStatus(LSlogilabprotocolsteps.getProtocolstepcode(),
							0);
			mapObj.put("retiredsampleupdatelst", retiredsampleupdatelst);
		}
		return mapObj;
	}
	/*
	 * public Map<String, Object> addProtocolOrderStep(Map<String, Object> argObj) {
	 * 
	 * Map<String, Object> mapObj = new HashMap<String, Object>(); LScfttransaction
	 * LScfttransactionobj = new LScfttransaction();
	 * if(argObj.containsKey("objsilentaudit")) { LScfttransactionobj = new
	 * ObjectMapper().convertValue(argObj.get("objsilentaudit"), new
	 * TypeReference<LScfttransaction>() { }); } ObjectMapper objMapper= new
	 * ObjectMapper(); LoggedUser objUser = new LoggedUser(); // silent audit
	 * if(LScfttransactionobj!=null ) {
	 * LScfttransactionobj.setTableName("LSprotocolmaster");
	 * if(argObj.containsKey("username")) { String username=
	 * objMapper.convertValue(argObj.get("username"), String.class); LSSiteMaster
	 * objsite =
	 * LSSiteMasterRepository.findBysitecode(LScfttransactionobj.getLssitemaster());
	 * LSuserMaster objuser=
	 * LSuserMasterRepositoryObj.findByusernameAndLssitemaster(username, objsite);
	 * LScfttransactionobj.setLsuserMaster(objuser.getUsercode());
	 * LScfttransactionobj.setLssitemaster(objuser.getLssitemaster().getSitecode());
	 * LScfttransactionobj.setUsername(username); }
	 * lscfttransactionRepository.save(LScfttransactionobj); } // manual audit
	 * if(argObj.containsKey("objuser")) {
	 * objUser=objMapper.convertValue(argObj.get("objuser"), LoggedUser.class);
	 * if(argObj.containsKey("objmanualaudit")) { LScfttransaction
	 * objmanualaudit=new LScfttransaction(); objmanualaudit =
	 * objMapper.convertValue(argObj.get("objmanualaudit"), LScfttransaction.class);
	 * 
	 * objmanualaudit.setComments(objUser.getComments());
	 * lscfttransactionRepository.save(objmanualaudit); } }
	 * 
	 * if(argObj.containsKey("newProtocolstepObj")) { LSlogilabprotocolsteps
	 * LSprotocolstepObj = new
	 * ObjectMapper().convertValue(argObj.get("newProtocolstepObj"), new
	 * TypeReference<LSlogilabprotocolsteps>() {});
	 * 
	 * CloudLSlogilabprotocolstepsInfo CloudLSprotocolstepInfoObj = new
	 * CloudLSlogilabprotocolstepsInfo(); if(LSprotocolstepObj.getIsmultitenant() ==
	 * 1) { // if(LSprotocolstepObj.getNewStep() == 1) {
	 * CloudLSprotocolstepInfoObj.setId(LSprotocolstepObj.getProtocolorderstepcode()
	 * ); CloudLSprotocolstepInfoObj.setLsprotocolstepInfo(LSprotocolstepObj.
	 * getLsprotocolstepInfo());
	 * CloudLSlogilabprotocolstepsInfoRepository.save(CloudLSprotocolstepInfoObj);
	 * // }else { // CloudLSlogilabprotocolstepsInfo updateLSprotocolstepInfo =
	 * CloudLSlogilabprotocolstepsInfoRepository. //
	 * findById(LSprotocolstepObj.getProtocolorderstepcode()); //
	 * updateLSprotocolstepInfo.setLsprotocolstepInfo(LSprotocolstepObj.
	 * getLsprotocolstepInfo()); //
	 * CloudLSlogilabprotocolstepsInfoRepository.save(updateLSprotocolstepInfo); //
	 * } } else { Query query = new
	 * Query(Criteria.where("id").is(LSprotocolstepObj.getProtocolorderstepcode()));
	 * Update update=new Update();
	 * update.set("content",LSprotocolstepObj.getLsprotocolstepInfo());
	 * mongoTemplate.upsert(query, update, LSlogilabprotocolstepsInfo.class); }
	 * 
	 * List<LSlogilabprotocolsteps> tempLSprotocolstepLst =
	 * LSlogilabprotocolstepsRepository.findByprotocolorderstepcode(
	 * LSprotocolstepObj.getProtocolorderstepcode()); List<LSlogilabprotocolsteps>
	 * LSprotocolstepLst = new ArrayList<LSlogilabprotocolsteps>();
	 * for(LSlogilabprotocolsteps LSprotocolstepObj1: tempLSprotocolstepLst) {
	 * if(LSprotocolstepObj.getIsmultitenant() == 1) { //
	 * if(LSprotocolstepObj.getNewStep() == 1) {
	 * LSprotocolstepObj1.setLsprotocolstepInfo(CloudLSprotocolstepInfoObj.
	 * getLsprotocolstepInfo()); // }else { // CloudLSlogilabprotocolstepsInfo
	 * newLSprotocolstepInfo =
	 * CloudLSlogilabprotocolstepsInfoRepository.findById(LSprotocolstepObj.
	 * getProtocolstepcode()); // if(newLSprotocolstepInfo != null) { //
	 * LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.
	 * getLsprotocolstepInfo()); // } // } } else { LSlogilabprotocolstepsInfo
	 * newLSprotocolstepInfo =
	 * mongoTemplate.findById(LSprotocolstepObj1.getProtocolorderstepcode(),
	 * LSlogilabprotocolstepsInfo.class); if(newLSprotocolstepInfo != null) {
	 * LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
	 * } } LSprotocolstepLst.add(LSprotocolstepObj1); }
	 * mapObj.put("protocolstepLst", LSprotocolstepLst); }
	 * 
	 * return mapObj; }
	 */

	@SuppressWarnings("unused")
	public Map<String, Object> GetProtocolorderVerionLst(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();

		ObjectMapper objm = new ObjectMapper();

		LSprotocolorderversion versionMaster = objm.convertValue(argObj.get("CloudLSprotocolversionstep"),
				LSprotocolorderversion.class);
		int multitenent = objm.convertValue(argObj.get("ismultitenant"), Integer.class);
		int countforstep = 0;
		List<LSprotocolorderstepversion> LSprotocolorderstepversion = lsprotocolorderstepversionRepository
				.findByProtocolordercodeAndVersionnoAndStatusOrderByVersionno(versionMaster.getProtocolordercode(),
						versionMaster.getVersionno(), 1);
		countforstep = lsprotocolorderstepversionRepository.countByProtocolordercodeAndStatusAndVersionno(
				versionMaster.getProtocolordercode(), 1, versionMaster.getVersionno());
		mapObj.put("countforstep", countforstep);
//		LSprotocolorderstepversion = LSprotocolorderstepversion.stream().distinct().collect(Collectors.toList());
		List<LSlogilabprotocolsteps> LSlogilabprotocolsteps = new ArrayList<LSlogilabprotocolsteps>();
		for (LSprotocolorderstepversion lsprotocolorderstepversion : LSprotocolorderstepversion) {
			LSlogilabprotocolsteps LSprotocolsteplst = LSlogilabprotocolstepsRepository
					.findByProtocolorderstepcode(lsprotocolorderstepversion.getProtocolorderstepcode());
			if (multitenent == 1 || multitenent == 2) {
				CloudLSprotocolorderversionstep cloudlsprotocolorderversionstep = CloudLSprotocolorderversionstepRepository
						.findByProtocolorderstepversioncode(
								lsprotocolorderstepversion.getProtocolorderstepversioncode());
//		CloudLsLogilabprotocolstepInfo newLSprotocolstepInfo = CloudLsLogilabprotocolstepInfoRepository
//				.findById(LSprotocolsteplst.getProtocolorderstepcode());
				LSprotocolsteplst.setLsprotocolstepInfo(cloudlsprotocolorderversionstep.getLsprotocolstepInfo());
			} else {
				LSprotocolorderversionstepInfo newLSprotocolstepInfo = mongoTemplate.findById(
						lsprotocolorderstepversion.getProtocolorderstepversioncode(),
						LSprotocolorderversionstepInfo.class);
				if (newLSprotocolstepInfo != null) {
					LSprotocolsteplst.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
				}
			}
			LSlogilabprotocolsteps.add(LSprotocolsteplst);
		}
//		List<LSprotocolorderversion> LSprotocolorderversion =lsprotocolorderversionRepository.findByProtocolordercodeOrderByVersionnoDesc(versionMaster.getProtocolordercode());
////		Collections.sort(LSprotocolorderversion, Collections.reverseOrder());
//		
//		if(LSprotocolorderversion!=null) {
//		mapObj.put("protocolorderversionLst", LSprotocolorderversion);
//		}
		if (LSlogilabprotocolsteps != null) {
			mapObj.put("protocolstepLst", LSlogilabprotocolsteps);
		} else {
			mapObj.put("protocolstepLst", new ArrayList<>());
		}
		return mapObj;
	}

	@SuppressWarnings("unused")
	public Map<String, Object> GetProtocolTemplateVerionLst(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();

		ObjectMapper objm = new ObjectMapper();

		LSprotocolversion versionMaster = objm.convertValue(argObj.get("CloudLSprotocolversionstep"),
				LSprotocolversion.class);

		// CloudLSprotocolversionstep versionMaster = objm.convertValue(
		// argObj.get("CloudLSprotocolversionstep"), CloudLSprotocolversionstep.class);

		int multitenent = objm.convertValue(argObj.get("ismultitenant"), Integer.class);

		List<LSprotocolstepversion> LSprotocolstepversion = LSprotocolstepversionRepository
				.findByprotocolmastercodeAndVersionnoAndStatus(versionMaster.getProtocolmastercode(),
						versionMaster.getVersionno(), 1);

		LSprotocolstepversion = LSprotocolstepversion.stream().distinct().collect(Collectors.toList());

		List<LSprotocolstep> LSprotocolsteplst = new ArrayList<LSprotocolstep>();

		int k = 0;

		while (k < LSprotocolstepversion.size()) {

			LSprotocolstep lsStep = LSProtocolStepRepositoryObj
					.findByProtocolstepcode(LSprotocolstepversion.get(k).getProtocolstepcode());
			if (lsStep != null) {
				lsStep.setVersionno(LSprotocolstepversion.get(k).getVersionno());

				lsStep.setProtocolstepversioncode(LSprotocolstepversion.get(k).getProtocolstepversioncode());

				LSprotocolsteplst.add(lsStep);
			}

			k++;
		}

		List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();

		List<CloudLSprotocolversionstep> LSprotocolStepversionlst = CloudLSprotocolversionstepRepository
				.findByprotocolmastercode(versionMaster.getProtocolmastercode());

		for (LSprotocolstep LSprotocolstepObj1 : LSprotocolsteplst) {
			if (multitenent == 1 || multitenent == 2) {

				CloudLSprotocolversionstep newLSprotocolstepInfo = CloudLSprotocolversionstepRepository
						.findByIdAndVersionno(LSprotocolstepObj1.getProtocolstepversioncode(),
								LSprotocolstepObj1.getVersionno());
				if (newLSprotocolstepInfo.getLsprotocolstepInfo() != null) {
//					LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
					LSprotocolstepObj1.setLsprotocolstepInformation(newLSprotocolstepInfo.getLsprotocolstepInfo());
				} else {
					LSprotocolstepInformation newobj = lsprotocolstepInformationRepository
							.findById(LSprotocolstepObj1.getProtocolstepcode());

					if (newobj.getLsprotocolstepInfo() != null) {
						LSprotocolstepObj1.setLsprotocolstepInformation(newobj.getLsprotocolstepInfo());
					}
				}
			} else {

				LSprotocolversionstepInfo newLSprotocolstepInfo = mongoTemplate
						.findById(LSprotocolstepObj1.getProtocolstepversioncode(), LSprotocolversionstepInfo.class);

				if (newLSprotocolstepInfo != null) {
//					LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
					LSprotocolstepObj1.setLsprotocolstepInformation(newLSprotocolstepInfo.getContent());
				}
			}
			LSprotocolstepLst.add(LSprotocolstepObj1);
		}

		if (LSprotocolsteplst != null) {
			mapObj.put("protocolstepLst", LSprotocolstepLst);
		} else {
			mapObj.put("protocolstepLst", new ArrayList<>());
		}
		return mapObj;
	}

	public List<LSlogilabprotocoldetail> getsingleprotocolorder(LSlogilabprotocoldetail objusers) {
		List<LSlogilabprotocoldetail> rtobj = new ArrayList<>();
		if (objusers.getProtocolordercode() != null) {
			ArrayList<Long> obj = new ArrayList<>();
			obj.add(objusers.getProtocolordercode());
			rtobj = LSlogilabprotocoldetailRepository.findByProtocolordercodeIn(obj);

			LSusergroup userGroup = LSusergroupRepository.findOne(objusers.getMultiusergroupcode());

//			List<LSworkflowgroupmapping> lsworkflowgroupmapping = lsworkflowgroupmappingRepository
//					.findBylsusergroupAndWorkflowcodeNotNull(userGroup);
			List<Elnprotocolworkflowgroupmap> elnprotocolworkflowgroupmapobj = elnprotocolworkflowgroupmapRepository
					.findBylsusergroupAndWorkflowcodeNotNull(userGroup);

			if (elnprotocolworkflowgroupmapobj != null && elnprotocolworkflowgroupmapobj.size() > 0) {
				List<Elnprotocolworkflow> lsprotocolworkflow = elnprotocolworkflowRepository
						.findByelnprotocolworkflowgroupmapInOrderByWorkflowcodeDesc(elnprotocolworkflowgroupmapobj);

//				rtobj.forEach(objorder -> objorder.setLstworkflow(lsprotocolworkflow));
				rtobj.forEach(objorder -> objorder.setLstelnprotocolworkflow(lsprotocolworkflow));

			} else {
				rtobj.forEach(objorder -> objorder.setCanuserprocess(false));
			}

			return rtobj;
		}
		return rtobj;

	}

	public Map<String, Object> Getinitialorders(LSlogilabprotocoldetail objorder) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		List<Long> immutableNegativeValues = Arrays.asList(-3L, -22L);
		if (objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
			mapOrders.put("orders", Getadministratororder(objorder));
			mapOrders.put("ordercount", LSlogilabprotocoldetailRepository.count());
		} else {

			List<Lsprotocolorderstructure> lstdir = new ArrayList<Lsprotocolorderstructure>();
			List<Long> directorycode = new ArrayList<Long>();
			if (objorder.getLstuserMaster().size() == 0) {
				lstdir = lsprotocolorderStructurerepository
						.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrderByDirectorycode(
								objorder.getLsuserMaster().getLssitemaster(), 1, immutableNegativeValues,
								objorder.getLsuserMaster(), 2);
				directorycode = lstdir.stream().map(Lsprotocolorderstructure::getDirectorycode)
						.collect(Collectors.toList());
			} else {
				lstdir = lsprotocolorderStructurerepository
						.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
								objorder.getLsuserMaster().getLssitemaster(), 1, immutableNegativeValues,
								objorder.getLsuserMaster(), 2, objorder.getLsuserMaster().getLssitemaster(), 3,
								objorder.getLstuserMaster());
				lstdir.addAll(lsprotocolorderStructurerepository.findByDirectorycodeIn(immutableNegativeValues));
				directorycode = lstdir.stream().map(Lsprotocolorderstructure::getDirectorycode)
						.collect(Collectors.toList());

			}
			mapOrders.put("test", lstestmasterlocalRepository.findBystatusAndLssitemasterOrderByTestcodeDesc(1,
					objorder.getLsuserMaster().getLssitemaster()));
			mapOrders.put("directorycode", directorycode);
			mapOrders.put("orders", Getuserorder(objorder, directorycode));
			mapOrders.put("ordercount", LSlogilabprotocoldetailRepository.countByLsprojectmasterInOrDirectorycodeIn(
					objorder.getLsuserMaster().getLstproject(), directorycode));
		}

		return mapOrders;
	}

	public List<Logilabprotocolorders> Getremainingorders(LSlogilabprotocoldetail objorder) {
		if (objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
			return Getadministratororder(objorder);
		} else {
			return Getuserorder(objorder, objorder.getLstdirectorycode());
		}
	}

	public List<Logilabprotocolorders> Getadministratororder(LSlogilabprotocoldetail objorder) {
		List<Logilabprotocolorders> lstorders = new ArrayList<Logilabprotocolorders>();
		if (objorder.getProtocolordercode() == 0) {
			lstorders = LSlogilabprotocoldetailRepository.findFirst20ByOrderByProtocolordercodeDesc();
		} else {
			lstorders = LSlogilabprotocoldetailRepository
					.findFirst20ByProtocolordercodeLessThanOrderByProtocolordercodeDesc(
							objorder.getProtocolordercode());
		}
		return lstorders;
	}

	public List<Logilabprotocolorders> Getuserorder(LSlogilabprotocoldetail objorder, List<Long> directorycode) {
		List<LSprojectmaster> lstproject = objorder.getLsuserMaster().getLstproject();
		List<Logilabprotocolorders> lstorders = new ArrayList<Logilabprotocolorders>();
		if (lstproject != null) {
			if (objorder.getProtocolordercode() == 0) {
				if (directorycode.size() == 0) {
					lstorders = LSlogilabprotocoldetailRepository
							.findFirst20ByLsprojectmasterInOrderByProtocolordercodeDesc(lstproject);

				} else {
					lstorders = LSlogilabprotocoldetailRepository
							.findFirst20ByLsprojectmasterInOrDirectorycodeInOrderByProtocolordercodeDesc(lstproject,
									directorycode);
				}
			} else {
				if (directorycode.size() == 0) {
					lstorders = LSlogilabprotocoldetailRepository
							.findFirst20ByProtocolordercodeLessThanAndLsprojectmasterInOrderByProtocolordercodeDesc(
									objorder.getProtocolordercode(), lstproject);

				} else {
					lstorders = LSlogilabprotocoldetailRepository
							.findFirst20ByProtocolordercodeLessThanAndLsprojectmasterInOrProtocolordercodeLessThanAndDirectorycodeInOrderByProtocolordercodeDesc(
									objorder.getProtocolordercode(), lstproject, objorder.getProtocolordercode(),
									directorycode);

				}
			}
		}
//		lstorders.forEach(objorderDetail -> objorderDetail.setLstelnprotocolworkflow(objorder.getLsuserMaster().getLstworkflow()));
		lstorders.forEach(objorderDetail -> objorderDetail
				.setLstelnprotocolworkflow(objorder.getLsuserMaster().getLstelnprotocolworkflow()));
		return lstorders;
	}

	public Map<String, Object> Getinitialtemplates(LSprotocolmaster objprotocol) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		if (objprotocol.getLSuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
			mapOrders.put("template", Getadministratortemplates(objprotocol));
			mapOrders.put("templatecount", LSProtocolMasterRepositoryObj.count());
		} else {
			List<Integer> lstteamuser = objprotocol.getLSuserMaster().getObjuser().getTeamuserscode();

			if (lstteamuser != null && lstteamuser.size() > 0) {
				lstteamuser.add(objprotocol.getLSuserMaster().getUsercode());
				mapOrders.put("templatecount", LSProtocolMasterRepositoryObj.countByCreatedbyIn(lstteamuser));
			} else {
				mapOrders.put("templatecount",
						LSProtocolMasterRepositoryObj.countByCreatedby(objprotocol.getLSuserMaster().getUsercode()));
			}
			mapOrders.put("template", Getusertemplates(objprotocol));

		}
		return mapOrders;
	}

	public List<LSprotocolmaster> Getremainingtemplates(LSprotocolmaster objprotocol) {
		if (objprotocol.getLSuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
			return Getadministratortemplates(objprotocol);
		} else {
			return Getusertemplates(objprotocol);
		}
	}

	public List<LSprotocolmaster> Getadministratortemplates(LSprotocolmaster objprotocol) {
		List<LSprotocolmaster> lstprotocols = new ArrayList<LSprotocolmaster>();
		if (objprotocol.getProtocolmastercode() == 0) {
			lstprotocols = LSProtocolMasterRepositoryObj.findFirst20ByOrderByProtocolmastercodeDesc();
		} else {
			lstprotocols = LSProtocolMasterRepositoryObj
					.findFirst20ByProtocolmastercodeLessThanOrderByProtocolmastercodeDesc(
							objprotocol.getProtocolmastercode());
		}
		return lstprotocols;
	}

	public List<LSprotocolmaster> Getusertemplates(LSprotocolmaster objprotocol) {
		List<LSprotocolmaster> lstprotocols = new ArrayList<LSprotocolmaster>();
		List<Integer> lstteamuser = objprotocol.getLSuserMaster().getObjuser().getTeamuserscode();
		if (objprotocol.getProtocolmastercode() == 0) {
			if (lstteamuser != null && lstteamuser.size() > 0) {
				lstteamuser.add(objprotocol.getLSuserMaster().getUsercode());
				lstprotocols = LSProtocolMasterRepositoryObj
						.findFirst20ByCreatedbyInOrderByProtocolmastercodeDesc(lstteamuser);
			} else {
				lstprotocols = LSProtocolMasterRepositoryObj.findFirst20ByCreatedbyOrderByProtocolmastercodeDesc(
						objprotocol.getLSuserMaster().getUsercode());
			}
		} else {
			if (lstteamuser != null && lstteamuser.size() > 0) {
				lstteamuser.add(objprotocol.getLSuserMaster().getUsercode());
				lstprotocols = LSProtocolMasterRepositoryObj
						.findFirst20ByProtocolmastercodeLessThanAndCreatedbyInOrderByProtocolmastercodeDesc(
								objprotocol.getProtocolmastercode(), lstteamuser);
			} else {
				lstprotocols = LSProtocolMasterRepositoryObj
						.findFirst20ByProtocolmastercodeLessThanAndCreatedbyOrderByProtocolmastercodeDesc(
								objprotocol.getProtocolmastercode(), objprotocol.getLSuserMaster().getUsercode());
			}
		}

		return lstprotocols;
	}

	public Map<String, Object> uploadprotocolsfile(MultipartFile file, Integer protocolstepcode,
			Integer protocolmastercode, Integer stepno, String protocolstepname, String originurl, String editoruuid) {
		Map<String, Object> map = new HashMap<String, Object>();

		String id = null;
		try {
			id = cloudFileManipulationservice.storecloudfilesreturnUUID(file, "protocolfiles");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String filenamedata = FilenameUtils.removeExtension(file.getOriginalFilename());

		LSprotocolfiles parentfile = lsprotocolfilesRepository
				.findFirst1ByEditoruuidAndFilenameOrderByProtocolstepfilecodeDesc(editoruuid, filenamedata);

		LSprotocolfiles objfile = new LSprotocolfiles();
		objfile.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
		objfile.setFileid(id);
		objfile.setProtocolmastercode(protocolmastercode);
		objfile.setProtocolstepcode(protocolstepcode);
		objfile.setProtocolstepname(protocolstepname);
		objfile.setStepno(stepno);
		objfile.setEditoruuid(editoruuid);
		if (parentfile != null) {
			Integer versiondata = parentfile.getVersion() != null ? parentfile.getVersion() + 1 : 1;
			objfile.setFilename(filenamedata + "_V" + (versiondata));
			parentfile.setVersion(versiondata);
			lsprotocolfilesRepository.save(parentfile);
		} else {
			objfile.setFilename(filenamedata);
		}
		objfile.setVersion(0);

		lsprotocolfilesRepository.save(objfile);
		map.put("name", objfile.getFilename());
		map.put("extension", "." + objfile.getExtension());
		map.put("link", originurl + "/protocol/downloadprotocolfile/" + objfile.getFileid() + "/"
				+ TenantContext.getCurrentTenant() + "/" + objfile.getFilename() + "/" + objfile.getExtension());
		map.put("fileid", objfile.getFileid());	
		return map;
	}

	public Map<String, Object> Uploadprotocolimage(MultipartFile file, Integer protocolstepcode,
			Integer protocolmastercode, Integer stepno, String protocolstepname, String originurl) {
		Map<String, Object> map = new HashMap<String, Object>();

		String id = null;
		try {
			id = cloudFileManipulationservice.storecloudfilesreturnUUID(file, "protocolimages");
		} catch (IOException e) {

			e.printStackTrace();
		}

		LSprotocolimages objimg = new LSprotocolimages();
		objimg.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
		objimg.setFileid(id);
		objimg.setProtocolmastercode(protocolmastercode);
		objimg.setProtocolstepcode(protocolstepcode);
		objimg.setProtocolstepname(protocolstepname);
		objimg.setStepno(stepno);
		objimg.setFilename(FilenameUtils.removeExtension(file.getOriginalFilename()));
		objimg.setIslinkimage(true);

		String filename = "No Name";
		if (!objimg.getFilename().isEmpty()) {
			filename = objimg.getFilename();
		}
		String url = originurl + "/protocol/downloadprotocolimage/" + objimg.getFileid() + "/"
				+ TenantContext.getCurrentTenant() + "/" + filename + "/" + objimg.getExtension();
		Gson g = new Gson();
		String str = g.toJson(url);
		objimg.setSrc(str);
		lsprotocolimagesRepository.save(objimg);

		map.put("extension", "." + objimg.getExtension());
		map.put("name", filename);
		map.put("link", originurl + "/protocol/downloadprotocolimage/" + objimg.getFileid() + "/"
				+ TenantContext.getCurrentTenant() + "/" + filename + "/" + objimg.getExtension());

		return map;
	}

	public ByteArrayInputStream downloadprotocolimage(String fileid, String tenant) {
		TenantContext.setCurrentTenant(tenant);
		byte[] data = null;
		try {
			data = StreamUtils
					.copyToByteArray(cloudFileManipulationservice.retrieveCloudFile(fileid, tenant + "protocolimages"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(data);

		return bis;
	}

	public ByteArrayInputStream downloadprotocolfile(String fileid, String tenant, String ontabkey) {

		byte[] data = null;
		String containerName = "";
		if (ontabkey.equals("protocolfile")) {
			containerName = tenant + "protocolfiles";
		} else if (ontabkey.equals("sheet")) {
			containerName = tenant + "sheetfolderfiles";
		} else {
			containerName = tenant + "protocolfolderfiles";
		}
		try {
			data = StreamUtils.copyToByteArray(cloudFileManipulationservice.retrieveCloudFile(fileid, containerName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(data);

		return bis;
	}

	public boolean removeprotocolimage(Map<String, String> body) {
		String filid = body.get("fileid");
		cloudFileManipulationservice.deletecloudFile(filid, "protocolimages");
		return true;
	}

	public Map<String, Object> uploadprotocols(Map<String, Object> body) throws IOException {

		Map<String, Object> mapObj = new HashMap<String, Object>();
		Response response = new Response();
		LSprotocolstepInformation obj = new LSprotocolstepInformation();
		ObjectMapper object = new ObjectMapper();
		Boolean Isnew = false;
		if (body.get("LSprotocolupdates") != null) {
			LSprotocolupdates lSprotocolupdates = object.convertValue(body.get("LSprotocolupdates"),
					LSprotocolupdates.class);
			try {
				lSprotocolupdates.setProtocolmodifiedDate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lsprotocolupdatesRepository.save(lSprotocolupdates);
		}
		if (!body.get("protocolstepname").equals("")) {
			LSprotocolstep LSprotocolstepObj = new LSprotocolstep();
			String protocolstepname = object.convertValue(body.get("protocolstepname"), String.class);
			String str = object.convertValue(body.get("content"), String.class);
			Integer protocolmastercode = object.convertValue(body.get("protocolmastercode"), Integer.class);
//			boolean NewStep = object.convertValue(body.get("NewStep"), boolean.class);
			Integer NewStep = object.convertValue(body.get("newStep"), Integer.class);

			Integer stepno = object.convertValue(body.get("stepno"), Integer.class);
			Integer ismultitenant = object.convertValue(body.get("ismultitenant"), Integer.class);
//			Gson g = new Gson();
//			String str = g.toJson(content);
//			  String jsonObject = new JsonParser().parse(str).getAsString();

			int sitecode = object.convertValue(body.get("sitecode"), Integer.class);
			List<LSprotocolstep> Object = LSProtocolStepRepositoryObj
					.findByProtocolmastercodeAndProtocolstepnameAndStatus(protocolmastercode, protocolstepname, 1);
			if (Object.size() != 0 && NewStep == 1) {
				response.setStatus(false);
				mapObj.put("response", response);
			} else {
				if (NewStep == 0 && body.get("protocolstepcode") != null) {
					Isnew = true;
					int protocolstepcode = object.convertValue(body.get("protocolstepcode"), Integer.class);

					LSprotocolstepObj = LSProtocolStepRepositoryObj
							.findByProtocolmastercodeAndProtocolstepcodeAndStatus(protocolmastercode, protocolstepcode,
									1);
					LSprotocolstepObj.setProtocolstepname(protocolstepname);
					String modifiedusername = object.convertValue(body.get("modifiedusername"), String.class);
					LSprotocolstepObj.setModifiedusername(modifiedusername);
					LSprotocolstepObj.setLsprotocolstepInformation(str);
					LSProtocolStepRepositoryObj.save(LSprotocolstepObj);
					LSprotocolstepObj.setIsmultitenant(ismultitenant);

					LSprotocolstepObj.setNewStep(NewStep);
					mapObj.put("curentprotocolstep", LSprotocolstepObj);
					if (ismultitenant == 1 || ismultitenant == 2) {
						obj.setId(protocolstepcode);
						obj.setLsprotocolstepInfo(str);
						lsprotocolstepInformationRepository.save(obj);
//						commonservice.updateProtocolContent(str, LSprotocolstepObj, Isnew);
					} else {
						Query query = new Query(Criteria.where("id").is(protocolstepcode));
						Update update = new Update();
						update.set("content", str);
//						update.set("content", content);

						mongoTemplate.upsert(query, update, LSprotocolstepInfo.class);

					}
				} else if (NewStep == 1) {
					List<LSprotocolstep> LSprotocolstepObjforstepno = LSProtocolStepRepositoryObj
							.findByProtocolmastercodeAndStatus(protocolmastercode, 1);
					if (NewStep == 1 && LSprotocolstepObjforstepno.size() >= stepno) {
						LSprotocolstepObj.setStepno(LSprotocolstepObjforstepno.size() + 1);
					}

					int usercode = object.convertValue(body.get("createdby"), Integer.class);
					String createdbyusername = object.convertValue(body.get("createdbyusername"), String.class);

					if (LSprotocolstepObj.getStatus() == null) {
						LSprotocolstepObj.setProtocolmastercode(protocolmastercode);
						LSprotocolstepObj.setProtocolstepname(protocolstepname);
						LSprotocolstepObj.setStepno(stepno);
						LSprotocolstepObj.setStatus(1);
						LSprotocolstepObj.setCreatedby(usercode);
						LSprotocolstepObj.setCreatedbyusername(createdbyusername);
						LSprotocolstepObj.setCreateddate(new Date());
						LSprotocolstepObj.setSitecode(sitecode);
						LSprotocolstepObj.setNewStep(NewStep);
						LSprotocolstepObj.setLsprotocolstepInformation(str);
					}
					LSProtocolStepRepositoryObj.save(LSprotocolstepObj);

					LSprotocolstepObj.setNewStep(NewStep);
					LSprotocolstepObj.setIsmultitenant(ismultitenant);
					mapObj.put("curentprotocolstep", LSprotocolstepObj);
					if (ismultitenant == 1 || ismultitenant == 2) {
						obj.setId(LSprotocolstepObj.getProtocolstepcode());

						obj.setLsprotocolstepInfo(str);

						lsprotocolstepInformationRepository.save(obj);
						response.setInformation("Protocols.IDS_INFO");
						response.setStatus(true);
					} else {
						Query query = new Query(Criteria.where("id").is(LSprotocolstepObj.getProtocolstepcode()));
						Update update = new Update();
						update.set("content", str);
//						update.set("content", content);
						mongoTemplate.upsert(query, update, LSprotocolstepInfo.class);
					}
				}
				LSprotocolmaster protocolMaster = LSProtocolMasterRepositoryObj
						.findByprotocolmastercode(protocolmastercode);
				String username = "";
				int usercode = 0;
				if (NewStep == 1 && protocolMaster.getApproved() == null) {
					username = object.convertValue(body.get("createdbyusername"), String.class);
					usercode = object.convertValue(body.get("createdby"), Integer.class);
				} else {
					username = object.convertValue(body.get("versioncreatedby"), String.class);
					usercode = object.convertValue(body.get("versioncreatedbycode"), Integer.class);
				}
				if (ismultitenant == 1 || ismultitenant == 2) {

//					if(body.containsKey("newProtocolstepObj")) {
//					int 	
//					}
					LScfttransaction objaudit = new LScfttransaction();
					if (body.get("objsilentaudit") != null) {
						objaudit = object.convertValue(body.get("objsilentaudit"), LScfttransaction.class);
					}
					try {
						updateCloudProtocolVersion(protocolmastercode, LSprotocolstepObj.getProtocolstepcode(),
								LSprotocolstepObj.getLsprotocolstepInformation(), NewStep, sitecode, LSprotocolstepObj,
								username, usercode, objaudit);

					} catch (Exception e) {
						// TODO: handle exception
					}

//					if (NewStep != 1) {
					LSprotocolmaster protocolmaster = LSProtocolMasterRepositoryObj
							.findByprotocolmastercode(LSprotocolstepObj.getProtocolmastercode());
					mapObj.put("protocolmaster", protocolmaster);
					List<LSprotocolversion> LSprotocolversionlst = lsprotocolversionRepository
							.findByprotocolmastercode(LSprotocolstepObj.getProtocolmastercode());

					Collections.sort(LSprotocolversionlst, Collections.reverseOrder());

					mapObj.put("LSprotocolversionlst", LSprotocolversionlst);

//					}

				} else {
					try {
						updateCloudProtocolVersiononSQL(LSprotocolstepObj, sitecode, username, usercode);

					} catch (Exception e) {
						// TODO: handle exception
					}

//					if (LSprotocolstepObj.getNewStep() != 1) {
					LSprotocolmaster protocolmaster = LSProtocolMasterRepositoryObj
							.findByprotocolmastercode(LSprotocolstepObj.getProtocolmastercode());
					mapObj.put("protocolmaster", protocolmaster);
					List<LSprotocolversion> LSprotocolversionlst = lsprotocolversionRepository
							.findByprotocolmastercode(LSprotocolstepObj.getProtocolmastercode());

					Collections.sort(LSprotocolversionlst, Collections.reverseOrder());

					mapObj.put("LSprotocolversionlst", LSprotocolversionlst);
//					}

				}
				List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();
//			for(LSprotocolstep LSprotocolstepObj1: tempLSprotocolstepLst) {
				if (ismultitenant == 1 || ismultitenant == 2) {
					if (NewStep == 1) {
						LSprotocolstepObj.setLsprotocolstepInformation(obj.getLsprotocolstepInfo());
						LSprotocolstepLst.add(LSprotocolstepObj);
					} else {
						LSprotocolstepInformation newobj = lsprotocolstepInformationRepository
								.findById(LSprotocolstepObj.getProtocolstepcode());
						if (newobj != null) {
							LSprotocolstepObj.setLsprotocolstepInformation(newobj.getLsprotocolstepInfo());
						}
						LSprotocolstepLst.add(LSprotocolstepObj);
					}
				} else {
					LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate
							.findById(LSprotocolstepObj.getProtocolstepcode(), LSprotocolstepInfo.class);
					if (newLSprotocolstepInfo != null) {
						LSprotocolstepObj.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
						LSprotocolstepObj.setLsprotocolstepInformation(newLSprotocolstepInfo.getContent());
					}
					LSprotocolstepLst.add(LSprotocolstepObj);
				}
				response.setStatus(true);
				response.setInformation("");
				mapObj.put("response", response);
				mapObj.put("protocolstepLst", LSprotocolstepLst);

			}

		} else {

			response.setInformation("Protocols.IDS_PROTOCOLSTEPNAMEEMPTY");
			response.setStatus(false);
			mapObj.put("response", response);
		}
		return mapObj;
	}

	public Map<String, Object> protocolTemplateSave(Map<String, Object> body) throws IOException {

		Map<String, Object> mapObj = new HashMap<String, Object>();
		Response response = new Response();
		ObjectMapper object = new ObjectMapper();

		if (body.get("newProtocolMaster") != null) {
			LSprotocolmaster lSprotocolmaster = object.convertValue(body.get("newProtocolMaster"),
					LSprotocolmaster.class);
			LSprotocolmaster protocolMaster = LSProtocolMasterRepositoryObj
					.findByprotocolmastercode(lSprotocolmaster.getProtocolmastercode());
//			LSProtocolMasterRepositoryObj.save(lSprotocolmaster);
			Integer ismultitenant = object.convertValue(body.get("ismultitenant"), Integer.class);
			int sitecode = object.convertValue(body.get("sitecode"), Integer.class);
			int usercode = object.convertValue(body.get("usercode"), Integer.class);
			String username = (String) body.get("username");

			if (body.get("protocoldatainfo") != null) {
				lSprotocolmaster.setProtocoldatainfo((String) body.get("protocoldatainfo"));
				protocolMaster.setProtocoldatainfo((String) body.get("protocoldatainfo"));
			}

			if (lSprotocolmaster.getLsprotocolmethod() != null && !lSprotocolmaster.getLsprotocolmethod().isEmpty()) {
				// lSprotocolmaster.getLsprotocolmethod().get(0).setProtocolmastercode(lSprotocolmaster.getProtocolmastercode());
				lsprotocolmethodrepo.deleteByprotocolmastercode(lSprotocolmaster.getProtocolmastercode());
				lsprotocolmethodrepo.save(lSprotocolmaster.getLsprotocolmethod());
			}

			if ((protocolMaster.getApproved() != null && protocolMaster.getApproved() == 1)) {

				LSprotocolversion version = new LSprotocolversion();
				if (lsprotocolversionRepository.findFirstByProtocolmastercodeAndVersionno(
						protocolMaster.getProtocolmastercode(), protocolMaster.getVersionno()) != null) {
					version = lsprotocolversionRepository.findFirstByProtocolmastercodeAndVersionno(
							protocolMaster.getProtocolmastercode(), protocolMaster.getVersionno());
				} else {
					version = lsprotocolversionRepository
							.findFirstByProtocolmastercodeAndVersionno(protocolMaster.getProtocolmastercode(), 1);
					protocolMaster.setVersionno(1);
				}
				if (ismultitenant == 1 || ismultitenant == 2) {
					String tenant = TenantContext.getCurrentTenant();
					if (ismultitenant == 2) {
						tenant = "freeusers";
					}
					String content = objCloudFileManipulationservice.retrieveCloudSheets(protocolMaster.getFileuid(),
							tenant + "protocol");
					Map<String, Object> objMap = objCloudFileManipulationservice
							.storecloudSheetsreturnwithpreUUID(content, tenant + "protocolversion");
					String fileUUID = (String) objMap.get("uuid");
					String fileURI = objMap.get("uri").toString();

					version.setFileuid(fileUUID);
					version.setFileuri(fileURI);
				} else {
//					Lsprotocoltemplatedata lsprotocoldata = mongoTemplate
//					.findById(protocolMaster.getProtocolmastercode(), Lsprotocoltemplatedata.class);
//			
//					Lsprotocoltemplateversiondata lsprotocolversion = new Lsprotocoltemplateversiondata();
//					lsprotocolversion.setId(version.getProtocolversioncode());
//					lsprotocolversion.setContent(lsprotocoldata.getContent());
//					lsprotocolversion.setVersionno(version.getVersionno());
//					mongoTemplate.insert(lsprotocolversion);
//			
//					List<LSprotocolversion> lstLSprotocolversions = lsprotocolversionRepository.findByprotocolmastercode(
//							protocolMaster.getProtocolmastercode());
//					int i=0;
//					while(lstLSprotocolversions.size()>i) {
//						Lsprotocoltemplateversiondata lsprotocolversiondata = mongoTemplate
//								.findById(lstLSprotocolversions.get(i).getProtocolversioncode(), Lsprotocoltemplateversiondata.class);
//						if(lsprotocolversiondata != null) {
//							String vcontent = lsprotocolversiondata.getContent();
//							GridFSDBFile file1 = gridFsTemplate.findOne(new Query(
//									Criteria.where("filename").is("protocol_" + protocolMaster.getProtocolmastercode() 
//										+ "version_" + lstLSprotocolversions.get(i).getVersionno())));
//							if (file1 != null) {
//								gridFsTemplate.delete(new Query(
//										Criteria.where("filename").is("protocol_" + protocolMaster.getProtocolmastercode() 
//											+ "version_" + lstLSprotocolversions.get(i).getVersionno())));
//							}
//							gridFsTemplate.store(new ByteArrayInputStream(vcontent.getBytes(StandardCharsets.UTF_8)),
//									"protocol_" + protocolMaster.getProtocolmastercode() + "version_" + lstLSprotocolversions.get(i).getVersionno(), 
//										StandardCharsets.UTF_16);
//							
//							Query query = new Query(Criteria.where("id").is(lstLSprotocolversions.get(i).getProtocolversioncode()));
//							mongoTemplate.remove(query, Lsprotocoltemplateversiondata.class);
//		
//						}
//						i++;
//					}

					String Content = "";

					GridFSDBFile largefile = gridFsTemplate.findOne(new Query(
							Criteria.where("filename").is("protocol_" + protocolMaster.getProtocolmastercode())));

					if (largefile != null) {
						Content = new BufferedReader(
								new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
								.collect(Collectors.joining("\n"));
					}
//					else if(lsprotocoldata != null && largefile == null) {
//						byte[] bytes = lsprotocoldata.getContent().getBytes(StandardCharsets.UTF_16);
//						Content = new String(bytes, StandardCharsets.UTF_16);
//						Query query = new Query(Criteria.where("id").is(protocolMaster.getProtocolmastercode()));
//						mongoTemplate.remove(query, Lsprotocoltemplatedata.class);
//					} 

					GridFSDBFile largefile1 = gridFsTemplate.findOne(new Query(Criteria.where("filename").is("protocol_"
							+ protocolMaster.getProtocolmastercode() + "version_" + version.getVersionno())));
					if (largefile1 != null) {
						gridFsTemplate.delete(new Query(Criteria.where("filename").is("protocol_"
								+ protocolMaster.getProtocolmastercode() + "version_" + version.getVersionno())));
					}
					gridFsTemplate.store(new ByteArrayInputStream(Content.getBytes(StandardCharsets.UTF_8)),
							"protocol_" + protocolMaster.getProtocolmastercode() + "version_" + version.getVersionno(),
							StandardCharsets.UTF_16);
				}
				version.setApproved(protocolMaster.getApproved());
				lsprotocolversionRepository.save(version);

				LSprotocolversion objversion = new LSprotocolversion();
				objversion.setApproved(0);
				objversion.setCreatedby(usercode);
				try {
					objversion.setCreatedate(commonfunction.getCurrentUtcTime());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//				objversion.setModifiedby(lSprotocolupdates.getModifiedby());
//				objversion.setModifieddate(lSprotocolupdates.getProtocolmodifiedDate());
				objversion.setProtocolmastercode(protocolMaster.getProtocolmastercode());
				objversion.setProtocolmastername(protocolMaster.getProtocolmastername());
				objversion.setProtocolstatus(protocolMaster.getProtocolstatus());
				objversion.setCreatedbyusername(username);
				objversion.setSharewithteam(protocolMaster.getSharewithteam());
				objversion.setLssitemaster(protocolMaster.getLssitemaster());
				objversion.setRejected(protocolMaster.getRejected());
				objversion.setVersionname("version_" + (protocolMaster.getVersionno() + 1));
				objversion.setVersionno(protocolMaster.getVersionno() + 1);

				if (protocolMaster.getLsprotocolversion() != null) {
					protocolMaster.getLsprotocolversion().add(objversion);
				} else {
					List<LSprotocolversion> lstversion = new ArrayList<LSprotocolversion>();
					lstversion.add(objversion);
					protocolMaster.setLsprotocolversion(lstversion);
				}

				lsprotocolversionRepository.save(protocolMaster.getLsprotocolversion());

				LSSiteMaster lssitemaster = LSSiteMasterRepository.findBysitecode(sitecode);
				ElnprotocolTemplateworkflow lssheetworkflow = elnprotocolTemplateworkflowRepository
						.findTopByAndLssitemasterAndStatusOrderByWorkflowcodeAsc(lssitemaster, 1);
				protocolMaster.setApproved(0);
//				protocolMaster.setLssheetworkflow(lssheetworkflow);
				protocolMaster.setElnprotocoltemplateworkflow(lssheetworkflow);
				protocolMaster.setVersionno(protocolMaster.getVersionno() + 1);
			}

			if (!body.get("protocolData").equals("")) {

//				String str = object.convertValue(body.get("protocolData"), String.class);
				Gson gson = new Gson();
				String protocolDataJson = gson.toJson(body.get("protocolData"));
				try {
					protocolMaster.setLastmodified(commonfunction.getCurrentUtcTime());
					protocolMaster.setModifiedby(lSprotocolmaster.getModifiedby());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (ismultitenant == 1 || ismultitenant == 2) {
					protocolMaster.setIsmultitenant(ismultitenant);
					commonservice.updateProtocolContent(protocolDataJson, protocolMaster);
				} else {
//					Query query = new Query(Criteria.where("id").is(protocolMaster.getProtocolmastercode()));
//					Update update = new Update();
//					update.set("content", protocolDataJson);
//
//					mongoTemplate.upsert(query, update, Lsprotocoltemplatedata.class);

					byte[] bytes = protocolDataJson.getBytes(StandardCharsets.UTF_16);
					String Content = new String(bytes, StandardCharsets.UTF_16);

					GridFSDBFile largefile = gridFsTemplate.findOne(new Query(
							Criteria.where("filename").is("protocol_" + protocolMaster.getProtocolmastercode())));
					if (largefile != null) {
						gridFsTemplate.delete(new Query(
								Criteria.where("filename").is("protocol_" + protocolMaster.getProtocolmastercode())));
					}
					gridFsTemplate.store(new ByteArrayInputStream(Content.getBytes(StandardCharsets.UTF_8)),
							"protocol_" + protocolMaster.getProtocolmastercode(), StandardCharsets.UTF_16);

				}
				response.setInformation("IDS_MSG_PROTOCOLSAVE");
				response.setStatus(true);
				lsProtocolMasterRepository.save(protocolMaster);
				mapObj.put("ProtocolMaster",
						lsProtocolMasterRepository.findByprotocolmastercode(protocolMaster.getProtocolmastercode()));
			}
			mapObj.put("LSprotocolversionlst", lsprotocolversionRepository
					.findByprotocolmastercodeOrderByVersionnoDesc(protocolMaster.getProtocolmastercode()));
		}
		mapObj.put("protocolData", body.get("protocolData"));
		mapObj.put("response", response);

		// for protocol comments nottification
		LSuserMaster lsuserfrom = object.convertValue(body.get("lsuserMaster"), LSuserMaster.class);
		if (lsuserfrom != null) {
			@SuppressWarnings("unchecked")
			List<LinkedHashMap<String, String>> notifyto = (List<LinkedHashMap<String, String>>) body.get("notifyto");
			for (LinkedHashMap<String, String> to : notifyto) {
				if (to != null) {
					Integer usercode = Integer.parseInt(to.get("value"));
					LSuserMaster createby = lsusermasterRepository.findByUsercode(usercode);
					String Details = "{\"protocolname\":\"" + body.get("protocolmastername") + "\", \"createduser\":\""
							+ body.get("username") + "\", \"protocolmastercode\":" + body.get("protocolmastercode")
							+ ", \"isprocess\": true, \"isprotocolprocess\":true, \"screen\": \"" + to.get("screen")
							+ "\" }";
					LSnotification objnotify = new LSnotification();
					objnotify.setNotifationfrom(lsuserfrom);
					objnotify.setNotifationto(createby);
					try {
						objnotify.setNotificationdate(commonfunction.getCurrentUtcTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					objnotify.setNotification("PROTOCOLCOMMENT");
					objnotify.setNotificationdetils(Details);
					objnotify.setIsnewnotification(1);
					objnotify.setNotificationpath("/protocols");
					objnotify.setNotificationfor(1);
					lsnotificationRepository.save(objnotify);
				}
			}
		}

		return mapObj;
	}

	public Map<String, Object> protocolOrderSave(Map<String, Object> body) throws IOException {

		Map<String, Object> mapObj = new HashMap<String, Object>();
		Response response = new Response();
		ObjectMapper object = new ObjectMapper();
		
		LSlogilabprotocoldetail lSlogilabprotocoldetail = new LSlogilabprotocoldetail();
		LSlogilabprotocoldetail protocolOrder = new LSlogilabprotocoldetail();
		
		if (body.get("ProtocolOrder") != null) {
		   lSlogilabprotocoldetail = object.convertValue(body.get("ProtocolOrder"),
					LSlogilabprotocoldetail.class);
		   protocolOrder = LSlogilabprotocoldetailRepository
					.findByProtocolordercode(lSlogilabprotocoldetail.getProtocolordercode());
			Integer ismultitenant = object.convertValue(body.get("ismultitenant"), Integer.class);
			int sitecode = object.convertValue(body.get("sitecode"), Integer.class);
			Integer usercode = object.convertValue(body.get("usercode"), Integer.class);
			String username = (String) body.get("username");
			Boolean doversion = object.convertValue(body.get("doversion"), Boolean.class);
			Boolean idletimesave = object.convertValue(body.get("idletimesave"), Boolean.class);
			
			// idle time save unlock order if user equal 
			if(protocolOrder.getProtocolordercode() != null && idletimesave != null && idletimesave == true ) {
				if (usercode != null && protocolOrder.getLockeduser() != null
						&& usercode.equals(protocolOrder.getLockeduser())) {
					protocolOrder.setLockeduser(null);
					protocolOrder.setLockedusername(null);
					protocolOrder.setActiveuser(null);
				}
			}
			
			if (body.get("protocoldatainfo") != null) {
				lSlogilabprotocoldetail.setProtocoldatainfo((String) body.get("protocoldatainfo"));
				protocolOrder.setProtocoldatainfo((String) body.get("protocoldatainfo"));
			}

			if ((protocolOrder.getApproved() == null || protocolOrder.getApproved() != 1)) {

				if (doversion) {
					LSprotocolorderversion version = new LSprotocolorderversion();
					if (lsprotocolorderversionRepository.findFirstByProtocolordercodeAndVersionno(
							protocolOrder.getProtocolordercode(), protocolOrder.getVersionno()) != null) {
						version = lsprotocolorderversionRepository.findFirstByProtocolordercodeAndVersionno(
								protocolOrder.getProtocolordercode(), protocolOrder.getVersionno());
					} else {
						version = lsprotocolorderversionRepository
								.findFirstByProtocolordercodeAndVersionno(protocolOrder.getProtocolordercode(), 1);
						protocolOrder.setVersionno(1);
					}

					if (ismultitenant == 1 || ismultitenant == 2) {
						String content = objCloudFileManipulationservice.retrieveCloudSheets(protocolOrder.getFileuid(),
								commonfunction.getcontainername(ismultitenant, TenantContext.getCurrentTenant())
										+ "protocolorder");
						Map<String, Object> objMap = objCloudFileManipulationservice.storecloudSheetsreturnwithpreUUID(
								content,
								commonfunction.getcontainername(ismultitenant, TenantContext.getCurrentTenant())
										+ "protocolorderversion");
						String fileUUID = (String) objMap.get("uuid");
						String fileURI = objMap.get("uri").toString();

						version.setFileuid(fileUUID);
						version.setFileuri(fileURI);
					} else {
//						Lsprotocolorderdata lsprotocoldata = mongoTemplate
//						.findById(protocolOrder.getProtocolordercode(), Lsprotocolorderdata.class);
//				
//						Lsprotocolorderversiondata lsprotocolversion = new Lsprotocolorderversiondata();
//						lsprotocolversion.setId(version.getProtocolorderversioncode());
//						lsprotocolversion.setContent(lsprotocoldata.getContent());
//						lsprotocolversion.setVersionno(version.getVersionno());
//						mongoTemplate.insert(lsprotocolversion);

						String Content = "";

						GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename")
								.is("protocolorder_" + protocolOrder.getProtocolordercode())));

						if (largefile != null) {
							Content = new BufferedReader(
									new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
									.collect(Collectors.joining("\n"));
						}

						GridFSDBFile largefile1 = gridFsTemplate
								.findOne(new Query(Criteria.where("filename").is("protocolorder_"
										+ protocolOrder.getProtocolordercode() + "version_" + version.getVersionno())));
						if (largefile1 != null) {
							gridFsTemplate.delete(new Query(Criteria.where("filename").is("protocolorder_"
									+ protocolOrder.getProtocolordercode() + "version_" + version.getVersionno())));
						}
						gridFsTemplate.store(
								new ByteArrayInputStream(Content.getBytes(StandardCharsets.UTF_8)), "protocolorder_"
										+ protocolOrder.getProtocolordercode() + "version_" + version.getVersionno(),
								StandardCharsets.UTF_16);
					}
					lsprotocolorderversionRepository.save(version);

					LSprotocolorderversion objversion = new LSprotocolorderversion();
					objversion.setCreatedbyusername(username);
					objversion.setCreatedby(usercode);
					try {
						objversion.setCreatedate(commonfunction.getCurrentUtcTime());
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					objversion.setModifiedby(lSprotocolupdates.getModifiedby());
//					objversion.setModifieddate(lSprotocolupdates.getProtocolmodifiedDate());
					objversion.setProtocolordercode(protocolOrder.getProtocolordercode());
					objversion.setStatus(1);
					objversion.setVersionname("version_" + (protocolOrder.getVersionno() + 1));
					objversion.setVersionno(protocolOrder.getVersionno() + 1);

					if (protocolOrder.getlSprotocolorderversion() != null) {
						protocolOrder.getlSprotocolorderversion().add(objversion);
					} else {
						List<LSprotocolorderversion> lstversion = new ArrayList<LSprotocolorderversion>();
						lstversion.add(objversion);
						protocolOrder.setlSprotocolorderversion(lstversion);
					}

					lsprotocolorderversionRepository.save(protocolOrder.getlSprotocolorderversion());

					protocolOrder.setVersionno(protocolOrder.getVersionno() + 1);
				}

				if (!body.get("protocolData").equals("")) {
					Gson gson = new Gson();
					String protocolDataJson = gson.toJson(body.get("protocolData"));
					if (ismultitenant == 1 || ismultitenant == 2) {
						protocolOrder.setIsmultitenant(ismultitenant);
						try {
							protocolOrder.setModifieddate(commonfunction.getCurrentUtcTime());
							protocolOrder.setModifiedby(lSlogilabprotocoldetail.getModifiedby());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						updateProtocolOrderContent(protocolDataJson, protocolOrder, ismultitenant);
					} else {
//						Query query = new Query(Criteria.where("id").is(protocolOrder.getProtocolordercode()));
//						Update update = new Update();
//						update.set("content", protocolDataJson);
//
//						mongoTemplate.upsert(query, update, Lsprotocolorderdata.class);

						byte[] bytes = protocolDataJson.getBytes(StandardCharsets.UTF_16);
						String Content = new String(bytes, StandardCharsets.UTF_16);

						GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename")
								.is("protocolorder_" + protocolOrder.getProtocolordercode())));
						if (largefile != null) {
							gridFsTemplate.delete(new Query(Criteria.where("filename")
									.is("protocolorder_" + protocolOrder.getProtocolordercode())));
						}
						gridFsTemplate.store(new ByteArrayInputStream(Content.getBytes(StandardCharsets.UTF_8)),
								"protocolorder_" + protocolOrder.getProtocolordercode(), StandardCharsets.UTF_16);
					}
					mapObj.put("protocolOrder", protocolOrder);
					mapObj.put("protocolorderversionlst", lsprotocolorderversionRepository
							.findByProtocolordercodeOrderByVersionnoDesc(protocolOrder.getProtocolordercode()));
					response.setInformation("IDS_MSG_PROTOCOLORDERSAVE");
					response.setStatus(true);
				}
			}
		}
//		if ((boolean) body.get("ismaterialreduce")) {
//			@SuppressWarnings("unchecked")
//			Map<String, Object> mapObj11 = (Map<String, Object>) body.get("materialinventory");
//			transactionService.createMaterialResultUsedForList(mapObj11);
//		}
		mapObj.put("protocolData", body.get("protocolData"));
		mapObj.put("response", response);

		// comments nottification
		LSuserMaster lsuserfrom = object.convertValue(body.get("lsuserMaster"), LSuserMaster.class);
		
		if (lsuserfrom != null) {
			@SuppressWarnings("unchecked")
			List<LinkedHashMap<String, String>> notifyto = (List<LinkedHashMap<String, String>>) body.get("notifyto");
			for (LinkedHashMap<String, String> to : notifyto) {
				
				String protocolordername = "";
				SequenceTable seqobj =  sequenceTableRepository.findBySequencecodeOrderBySequencecode(2);
				Boolean Applicationseq = seqobj.getSequenceview().equals(2) ? true : false;
				//protocolordername = Applicationseq ?  protocolOrder.getSequenceid() :  (String) body.get("protoclordername");
				protocolordername = Applicationseq 
						?  protocolOrder.getSequenceid() != null
							? protocolOrder.getSequenceid() : (String) body.get("protoclordername")
						: protocolOrder.getProtoclordername();
				
				if (to != null) {
					Integer usercode = Integer.parseInt(to.get("value"));
					LSuserMaster createby = lsusermasterRepository.findByUsercode(usercode);
					String Details = "{\"ordercode\":\"" + body.get("protocolordercode") + "\", \"order\":\""
							+ protocolordername + "\", \"screen\": \"" + to.get("screen") + "\" }";

					LSnotification objnotify = new LSnotification();
					objnotify.setNotifationfrom(lsuserfrom);
					objnotify.setNotifationto(createby);
					try {
						objnotify.setNotificationdate(commonfunction.getCurrentUtcTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					objnotify.setNotification("PROTOCOLORDERCOMMENT");
					objnotify.setNotificationdetils(Details);
					objnotify.setIsnewnotification(1);
					objnotify.setNotificationpath("/Protocolorder");
					objnotify.setNotificationfor(1);
					lsnotificationRepository.save(objnotify);
				}
			}
		}
		return mapObj;
	}

	@SuppressWarnings("unused")
	public List<Lsrepositoriesdata> reducecunsumablefield(Lsrepositoriesdata[] lsrepositoriesdata) {
		List<Lsrepositoriesdata> lsrepositoriesdataobj = Arrays.asList(lsrepositoriesdata);
		for (Lsrepositoriesdata data : lsrepositoriesdataobj) {
			LsrepositoriesdataRepository.save(lsrepositoriesdataobj);
		}

//	Response response =new Response();
//	response.setStatus(true);
//	lsrepositoriesdata.setObjResponse(response);
		return lsrepositoriesdataobj;
	}

	public Map<String, Object> protocolsampleupdates(LSprotocolsampleupdates lsprotocolsampleupdates) {
		Map<String, Object> obj = new HashMap<String, Object>();
		LSprotocolsampleupdatesRepository.save(lsprotocolsampleupdates);
		List<LSprotocolsampleupdates> lsprotocolsamplelist = LSprotocolsampleupdatesRepository
				.findByprotocolstepcodeAndProtocolmastercodeAndIndexofIsNotNullAndStatus(
						lsprotocolsampleupdates.getProtocolstepcode(), lsprotocolsampleupdates.getProtocolmastercode(),
						1);
		obj.put("lsprotocolsampleupdates", lsprotocolsampleupdates);
		obj.put("lsprotocolsamplelist", lsprotocolsamplelist);
		return obj;
	}

	public List<Lsrepositoriesdata> getrepositoriesdata(Integer[] lsrepositoriesdata) {
		List<Integer> lsrepositoriesdataobj = Arrays.asList(lsrepositoriesdata);
		List<Lsrepositoriesdata> returnlst = new ArrayList<Lsrepositoriesdata>();
		for (Integer lsrepositoriesdatacode : lsrepositoriesdataobj) {
			List<Lsrepositoriesdata> lst = LsrepositoriesdataRepository
					.findByRepositorydatacode(lsrepositoriesdatacode);
			returnlst.add(lst.get(0));
		}
//		 lst= LsrepositoriesdataRepository.getRepositorydatacode(lsrepositoriesdataobj);
		return returnlst;
	}

	public Map<String, Object> updateprotocolsampleupdates(LSprotocolsampleupdates[] lsprotocolsampleupdates) {
		List<LSprotocolsampleupdates> lsrepositoriesdataobj = Arrays.asList(lsprotocolsampleupdates);
		Map<String, Object> obj = new HashMap<String, Object>();
		LSprotocolsampleupdatesRepository.save(lsrepositoriesdataobj);
		List<LSprotocolsampleupdates> lsprotocolsamplelist = LSprotocolsampleupdatesRepository
				.findByprotocolstepcodeAndProtocolmastercodeAndIndexofIsNotNullAndStatus(
						lsrepositoriesdataobj.get(0).getProtocolstepcode(),
						lsrepositoriesdataobj.get(0).getProtocolmastercode(), 1);
		List<LSprotocolsampleupdates> retiredsampleupdatelst = LSprotocolsampleupdatesRepository
				.findByprotocolstepcodeAndProtocolmastercodeAndIndexofIsNotNullAndStatus(
						lsrepositoriesdataobj.get(0).getProtocolstepcode(),
						lsrepositoriesdataobj.get(0).getProtocolmastercode(), 0);
		// obj.put("lsprotocolsampleupdates", lsrepositoriesdataobj);
		obj.put("lsprotocolsamplelist", lsprotocolsamplelist);
		obj.put("retiredsampleupdatelst", retiredsampleupdatelst);
		return obj;
	}

	public Map<String, Object> updateprotocolordersampleupdates(
			LSprotocolordersampleupdates[] lsprotocolordersampleupdates) {
		List<LSprotocolordersampleupdates> lsrepositoriesdataobj = Arrays.asList(lsprotocolordersampleupdates);
		Map<String, Object> mapObj = new HashMap<String, Object>();
		lsprotocolordersampleupdatesRepository.save(lsrepositoriesdataobj);

		List<LSprotocolordersampleupdates> sampleupdatelst = new ArrayList<LSprotocolordersampleupdates>();
		if (lsrepositoriesdataobj.get(0).getProtocolordercode() != null) {
			sampleupdatelst = lsprotocolordersampleupdatesRepository
					.findByProtocolordercodeAndProtocolstepcodeAndIndexofIsNotNullAndStatus(
							lsrepositoriesdataobj.get(0).getProtocolordercode(),
							lsrepositoriesdataobj.get(0).getProtocolstepcode(), 1);
			mapObj.put("lsprotocolsamplelist", sampleupdatelst);

			List<LSprotocolordersampleupdates> retiredsampleupdatelst = lsprotocolordersampleupdatesRepository
					.findByProtocolstepcodeAndIndexofIsNotNullAndStatus(
							lsrepositoriesdataobj.get(0).getProtocolstepcode(), 0);
			mapObj.put("retiredsampleupdatelst", retiredsampleupdatelst);
		}

		return mapObj;
	}

	public List<LSprotocolfiles> loadprotocolfiles(Map<String, String> body) {
		List<LSprotocolfiles> lst = new ArrayList<LSprotocolfiles>();
		ObjectMapper object = new ObjectMapper();
		Integer protocolmastercode = object.convertValue(body.get("protocolmastercode"), Integer.class);
		int protocolstepcode = object.convertValue(body.get("protocolstepcode"), Integer.class);
		lst = lsprotocolfilesRepository.findByProtocolmastercodeAndProtocolstepcodeOrderByProtocolstepfilecodeDesc(
				protocolmastercode, protocolstepcode);
		return lst;
	}

	@SuppressWarnings("unused")
	public Map<String, Object> Getprotocollinksignature(Map<String, String> body) throws ParseException {
		Map<String, Object> obj = new HashMap<String, Object>();
		ObjectMapper object = new ObjectMapper();

		String originurl = object.convertValue(body.get("originurl"), String.class);
		String protocolstepname = object.convertValue(body.get("protocolstepname"), String.class);
		Integer protocolmastercode = object.convertValue(body.get("protocolmastercode"), Integer.class);
		Long protocolordercode = new Long(protocolmastercode);
		Integer stepno = object.convertValue(body.get("stepno"), Integer.class);
		Integer protocolstepcode = object.convertValue(body.get("protocolstepcode"), Integer.class);
		Integer usercode = object.convertValue(body.get("usercode"), Integer.class);
		Integer signaturefrom = object.convertValue(body.get("signaturefrom"), Integer.class);

		CloudUserSignature objsignature = cloudFileManipulationservice.getSignature(usercode);

		byte[] data = null;

		if (objsignature != null) {
			data = objsignature.getImage().getData();
		} else {
			try {
				data = StreamUtils.copyToByteArray(new ClassPathResource("images/nosignature.png").getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(data);

		String fileName = "signature.png";
		CustomMultipartFile customMultipartFile = new CustomMultipartFile(data, fileName);

		if (signaturefrom == 1) {
			obj = Uploadprotocolimage(customMultipartFile, protocolstepcode, protocolmastercode, stepno,
					protocolstepname, originurl);
		} else {
			obj = Uploadprotocolorderimage(customMultipartFile, protocolstepcode, protocolordercode, stepno,
					protocolstepname, originurl);
		}
		obj.put("curendateandtime", commonfunction.getCurrentUtcTime());
		return obj;
	}

	@SuppressWarnings("unused")
	private void updateCloudProtocolorderVersion(Long protocolordercode, Integer protocolorderstepcode, String str,
			LSlogilabprotocolsteps lslogilabprotocolsteps, boolean isversion, Integer sitecode, boolean nochanges,
			Integer ismultitenant, String usercode, Integer usercode1) {
		LSlogilabprotocoldetail lslogilabprotocoldetail = LSlogilabprotocoldetailRepository
				.findByProtocolordercode(protocolordercode);
		List<LSlogilabprotocolsteps> Lslogilabprotocolsteps = LSlogilabprotocolstepsRepository
//				.findByProtocolordercode(protocolordercode);
				.findByProtocolordercodeAndStatus(protocolordercode, 1);
		if (isversion) {
			LSSiteMaster lssitemaster = LSSiteMasterRepository.findBysitecode(sitecode);
			LSprotocolworkflow lsprotocolworkflow = lSprotocolworkflowRepository
					.findTopByAndLssitemasterOrderByWorkflowcodeAsc(lssitemaster);

			if (lslogilabprotocoldetail.getApproved() != null && lslogilabprotocoldetail.getApproved() == 1) {
				lslogilabprotocoldetail.setApproved(0);
				lslogilabprotocoldetail.setlSprotocolworkflow(lsprotocolworkflow);
				lslogilabprotocoldetail.setVersionno(lslogilabprotocoldetail.getVersionno() + 1);

			} else {
				if (lslogilabprotocoldetail.getVersionno() != null) {
					lslogilabprotocoldetail.setVersionno(lslogilabprotocoldetail.getVersionno() + 1);
				} else {
					lslogilabprotocoldetail.setVersionno(1);
				}
			}
			LSlogilabprotocoldetailRepository.save(lslogilabprotocoldetail);
			List<CloudLSprotocolorderversionstep> lstcloudStepVersion = new ArrayList<CloudLSprotocolorderversionstep>();

			for (LSlogilabprotocolsteps LSlogilabprotocolsteps : Lslogilabprotocolsteps) {
				LSprotocolorderstepversion protoorderVersStep = new LSprotocolorderstepversion();
				CloudLSprotocolorderversionstep obj = new CloudLSprotocolorderversionstep();

				protoorderVersStep.setProtocolorderstepcode(LSlogilabprotocolsteps.getProtocolorderstepcode());
				protoorderVersStep.setProtocolordercode(LSlogilabprotocolsteps.getProtocolordercode());
				protoorderVersStep.setProtocolmastercode(LSlogilabprotocolsteps.getProtocolmastercode());
				protoorderVersStep.setStepno(LSlogilabprotocolsteps.getStepno());
				protoorderVersStep.setProtocolstepname(LSlogilabprotocolsteps.getProtocolstepname());
				protoorderVersStep.setStatus(LSlogilabprotocolsteps.getStatus());
				protoorderVersStep.setVersionno(lslogilabprotocoldetail.getVersionno());
//				protoorderVersStep.setVersionname();
				lsprotocolorderstepversionRepository.save(protoorderVersStep);
				if (ismultitenant == 1 || ismultitenant == 2) {
					obj.setProtocolordercode(lslogilabprotocoldetail.getProtocolordercode());
					obj.setProtocolorderstepversioncode(protoorderVersStep.getProtocolorderstepversioncode());
					obj.setIdversioncode(lslogilabprotocoldetail.getVersionno());
					obj.setVersionname("version_" + lslogilabprotocoldetail.getVersionno());
					if (protocolorderstepcode != null && str != null) {
						if (protocolorderstepcode.equals(LSlogilabprotocolsteps.getProtocolorderstepcode())) {
							obj.setLsprotocolstepInfo(str);
						} else {
							CloudLsLogilabprotocolstepInfo cloudLsLogilabprotocolstepInfo = CloudLsLogilabprotocolstepInfoRepository
									.findById(LSlogilabprotocolsteps.getProtocolorderstepcode());
							obj.setLsprotocolstepInfo(cloudLsLogilabprotocolstepInfo.getLsprotocolstepInfo());
						}
					} else {
						CloudLsLogilabprotocolstepInfo cloudLsLogilabprotocolstepInfo = CloudLsLogilabprotocolstepInfoRepository
								.findById(LSlogilabprotocolsteps.getProtocolorderstepcode());
						if (cloudLsLogilabprotocolstepInfo != null) {
							obj.setLsprotocolstepInfo(cloudLsLogilabprotocolstepInfo.getLsprotocolstepInfo());
						} else {

							Gson g = new Gson();
							obj.setLsprotocolstepInfo(g.toJson(""));

//							obj.setLsprotocolstepInfo("");
						}

					}
					CloudLSprotocolorderversionstepRepository.save(obj);
				} else {
					LSprotocolorderversionstepInfo LSprotocolorderversionstepInfo = new LSprotocolorderversionstepInfo();
					LSprotocolorderversionstepInfo.setStepcode(LSlogilabprotocolsteps.getProtocolorderstepcode());
					LSprotocolorderversionstepInfo.setId(protoorderVersStep.getProtocolorderstepversioncode());
					LSprotocolorderversionstepInfo.setVersionno(lslogilabprotocoldetail.getVersionno());
					if (protocolorderstepcode != null && str != null) {
						if (protocolorderstepcode.equals(LSlogilabprotocolsteps.getProtocolorderstepcode())) {
//						obj.setLsprotocolstepInfo(str);
							LSprotocolorderversionstepInfo.setContent(str);
						} else {
//						CloudLsLogilabprotocolstepInfo cloudLsLogilabprotocolstepInfo = CloudLsLogilabprotocolstepInfoRepository
//								.findById(LSlogilabprotocolsteps.getProtocolorderstepcode());
//						obj.setLsprotocolstepInfo(cloudLsLogilabprotocolstepInfo.getLsprotocolstepInfo());

							LsLogilabprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(
									LSlogilabprotocolsteps.getProtocolorderstepcode(), LsLogilabprotocolstepInfo.class);
							if (newLSprotocolstepInfo != null) {
//							LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
								LSprotocolorderversionstepInfo.setContent(newLSprotocolstepInfo.getContent());
							}
						}
					} else {

						LsLogilabprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(
								LSlogilabprotocolsteps.getProtocolorderstepcode(), LsLogilabprotocolstepInfo.class);
						if (newLSprotocolstepInfo != null) {
//							LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
							LSprotocolorderversionstepInfo.setContent(newLSprotocolstepInfo.getContent());
						}
					}

					mongoTemplate.insert(LSprotocolorderversionstepInfo);
				}
			}
			LSprotocolorderversion LSprotocolorderversion = new LSprotocolorderversion();
			LSprotocolorderversion.setProtocolordercode(lslogilabprotocoldetail.getProtocolordercode());
			LSprotocolorderversion.setStatus(1);
			LSprotocolorderversion.setVersionname("version_" + lslogilabprotocoldetail.getVersionno());
			LSprotocolorderversion.setVersionno(lslogilabprotocoldetail.getVersionno());
			LSprotocolorderversion.setCreatedate(new Date());
			if (!usercode.equals("") && usercode1 != 0) {
				LSprotocolorderversion.setCreatedbyusername(usercode);
				LSprotocolorderversion.setCreatedby(usercode1);
			}
			lsprotocolorderversionRepository.save(LSprotocolorderversion);
		} else {
//			for (LSlogilabprotocolsteps LSlogilabprotocolsteps : Lslogilabprotocolsteps) {
//				if (protocolorderstepcode.equals(LSlogilabprotocolsteps.getProtocolorderstepcode()) && nochanges) {
//					LSlogilabprotocolsteps.setIsfirstime(0);
//					LSlogilabprotocolstepsRepository.save(LSlogilabprotocolsteps);
//				}
//
//			}
			LSprotocolorderstepversion protoorderVersStep = lsprotocolorderstepversionRepository
					.findByProtocolorderstepcodeAndVersionno(lslogilabprotocolsteps.getProtocolorderstepcode(),
							lslogilabprotocoldetail.getVersionno());
			if (protoorderVersStep != null) {
				if (ismultitenant == 1 || ismultitenant == 2) {
					CloudLSprotocolorderversionstep object = CloudLSprotocolorderversionstepRepository
							.findByProtocolorderstepversioncode(protoorderVersStep.getProtocolorderstepversioncode());
					object.setLsprotocolstepInfo(str);
					CloudLSprotocolorderversionstepRepository.save(object);
				} else {
					Query query = new Query(
							Criteria.where("id").is(protoorderVersStep.getProtocolorderstepversioncode()));

					Update update = new Update();
					update.set("content", str);

					mongoTemplate.upsert(query, update, LSprotocolorderversionstepInfo.class);
				}
			}
		}

	}

	public Map<String, Object> uploadprotocolsordersstep(Map<String, Object> body) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		Response response = new Response();
		CloudLsLogilabprotocolstepInfo CloudLSprotocolstepInfoObj = new CloudLsLogilabprotocolstepInfo();
		ObjectMapper object = new ObjectMapper();
		if (!body.get("protocolstepname").equals("")) {

			LSlogilabprotocolsteps lslogilabprotocolsteps = new LSlogilabprotocolsteps();
			String protocolstepname = object.convertValue(body.get("protocolstepname"), String.class);
			String str = object.convertValue(body.get("content"), String.class);
			Long protocolordercode = object.convertValue(body.get("protocolordercode"), Long.class);
			boolean isversion = object.convertValue(body.get("isversion"), boolean.class);
//			boolean nochanges =object.convertValue(body.get("nochanges"), boolean.class);
			boolean nochanges = true;
			Integer NewStep = object.convertValue(body.get("NewStep"), Integer.class);
			Integer stepno = object.convertValue(body.get("stepno"), Integer.class);
			Integer ismultitenant = object.convertValue(body.get("ismultitenant"), Integer.class);
			Integer protocolmastercode = object.convertValue(body.get("protocolmastercode"), Integer.class);
//			Integer protocolstepcode = object.convertValue(body.get("protocolstepcode"), Integer.class);
//			Gson g = new Gson();
//			String str = g.toJson(content);
			int sitecode = object.convertValue(body.get("sitecode"), Integer.class);
//		LSprotocolstep Object=LSProtocolStepRepositoryObj.findByProtocolmastercodeAndProtocolstepnameAndStatus(protocolmastercode,protocolstepname,1);
			String orderstepflag = object.convertValue(body.get("orderstepflag"), String.class);
//int protocolstepcount =LSlogilabprotocolstepsRepository.countByProtocolordercodeAndStatus(protocolordercode, 1)
			if (NewStep == 0) {
				int protocolorderstepcode = object.convertValue(body.get("protocolorderstepcode"), Integer.class);
				lslogilabprotocolsteps = LSlogilabprotocolstepsRepository
						.findByProtocolorderstepcode(protocolorderstepcode);
				lslogilabprotocolsteps.setProtocolstepname(protocolstepname);
				lslogilabprotocolsteps.setOrderstepflag(orderstepflag);
				String modifiedusername = object.convertValue(body.get("modifiedusername"), String.class);
				lslogilabprotocolsteps.setModifiedusername(modifiedusername);
				LSlogilabprotocolstepsRepository.save(lslogilabprotocolsteps);
				lslogilabprotocolsteps.setNewStep(NewStep);
				mapObj.put("curentprotocolorderstep", lslogilabprotocolsteps);
			} else if (NewStep == 1) {
				lslogilabprotocolsteps = object.convertValue(body.get("newstepobj"), LSlogilabprotocolsteps.class);
				List<LSlogilabprotocolsteps> LSlogilabprotocolstepslist = LSlogilabprotocolstepsRepository
						.findByProtocolordercodeAndStatus(protocolordercode, 1);
				if (NewStep == 1 && LSlogilabprotocolstepslist.size() >= stepno) {
					lslogilabprotocolsteps.setStepno(LSlogilabprotocolstepslist.size() + 1);
				} else {
					lslogilabprotocolsteps.setStepno(stepno);
				}

				int usercode = object.convertValue(body.get("createdby"), Integer.class);
				String createdbyusername = object.convertValue(body.get("createdbyusername"), String.class);

				if (lslogilabprotocolsteps.getStatus() == null) {
					lslogilabprotocolsteps.setProtocolmastercode(protocolmastercode);
					lslogilabprotocolsteps.setProtocolstepname(protocolstepname);
					lslogilabprotocolsteps.setProtocolordercode(protocolordercode);
//					lslogilabprotocolsteps.setProtocolstepcode(protocolstepcode);

					lslogilabprotocolsteps.setStatus(1);
//				lslogilabprotocolsteps.setSitecode(sitecode);
					lslogilabprotocolsteps.setOrderstepflag("N");
					lslogilabprotocolsteps.setCreatedby(usercode);
					lslogilabprotocolsteps.setCreatedbyusername(createdbyusername);
					lslogilabprotocolsteps.setCreateddate(new Date());
					lslogilabprotocolsteps.setSitecode(sitecode);
					lslogilabprotocolsteps.setNewStep(NewStep);
				}
				LSlogilabprotocolstepsRepository.save(lslogilabprotocolsteps);

				lslogilabprotocolsteps.setNewStep(NewStep);
				lslogilabprotocolsteps.setIsmultitenant(ismultitenant);
				mapObj.put("curentprotocolorderstep", lslogilabprotocolsteps);
			}

			if (ismultitenant == 1 || ismultitenant == 2) {
				if (NewStep == 0 && body.get("protocolorderstepcode") != null) {

					CloudLSprotocolstepInfoObj.setId(lslogilabprotocolsteps.getProtocolorderstepcode());
					CloudLSprotocolstepInfoObj.setLsprotocolstepInfo(str);

					CloudLsLogilabprotocolstepInfoRepository.save(CloudLSprotocolstepInfoObj);

				} else if (NewStep == 1) {

					CloudLSprotocolstepInfoObj.setId(lslogilabprotocolsteps.getProtocolorderstepcode());

					CloudLSprotocolstepInfoObj.setLsprotocolstepInfo(str);
					CloudLsLogilabprotocolstepInfoRepository.save(CloudLSprotocolstepInfoObj);
//					response.setInformation("Protocols.IDS_INFO");
//					response.setStatus(true);
				}

//				if (NewStep == 0) {
//					int protocolorderstepcode = object.convertValue(body.get("protocolorderstepcode"), Integer.class);
//					updateCloudProtocolorderVersion(protocolordercode, protocolorderstepcode, str,
//							lslogilabprotocolsteps, isversion, sitecode, nochanges, ismultitenant);
//				}else if(NewStep == 1) {
//					
//				}

				response.setInformation("Protocols.IDS_INFO");
				response.setStatus(true);
				mapObj.put("response", response);
			} else {
				if (NewStep == 0) {

					Query query = new Query(Criteria.where("id").is(lslogilabprotocolsteps.getProtocolorderstepcode()));
					Update update = new Update();
					update.set("content", str);

					mongoTemplate.upsert(query, update, LsLogilabprotocolstepInfo.class);
//					response.setInformation("Protocols.IDS_INFO");
//					response.setStatus(true);
				} else {
					LsLogilabprotocolstepInfo LsLogilabprotocolstepInfoObj = new LsLogilabprotocolstepInfo();

					LsLogilabprotocolstepInfoObj.setId(lslogilabprotocolsteps.getProtocolorderstepcode());
					LsLogilabprotocolstepInfoObj.setContent(str);
					mongoTemplate.insert(LsLogilabprotocolstepInfoObj);
				}
				response.setInformation("Protocols.IDS_INFO");
				response.setStatus(true);
			}
			String username = "";
			int usercode = 0;
			if (NewStep == 0) {
				int protocolorderstepcode = object.convertValue(body.get("protocolorderstepcode"), Integer.class);
				username = object.convertValue(body.get("versioncreatedby"), String.class);
				usercode = object.convertValue(body.get("versioncreatedbycode"), Integer.class);
				updateCloudProtocolorderVersion(protocolordercode, protocolorderstepcode, str, lslogilabprotocolsteps,
						isversion, sitecode, nochanges, ismultitenant, username, usercode);
			} else if (NewStep == 1) {
				LSlogilabprotocoldetail protocoldetail = LSlogilabprotocoldetailRepository
						.findByProtocolordercode(protocolordercode);

				LSprotocolorderstepversion protoorderVersStep = new LSprotocolorderstepversion();
				CloudLSprotocolorderversionstep obj = new CloudLSprotocolorderversionstep();

				protoorderVersStep.setProtocolorderstepcode(lslogilabprotocolsteps.getProtocolorderstepcode());
				protoorderVersStep.setProtocolordercode(lslogilabprotocolsteps.getProtocolordercode());
				protoorderVersStep.setProtocolmastercode(lslogilabprotocolsteps.getProtocolmastercode());
				protoorderVersStep.setStepno(lslogilabprotocolsteps.getStepno());
				protoorderVersStep.setProtocolstepname(lslogilabprotocolsteps.getProtocolstepname());
				protoorderVersStep.setStatus(lslogilabprotocolsteps.getStatus());
				protoorderVersStep.setVersionno(protocoldetail.getVersionno());

				lsprotocolorderstepversionRepository.save(protoorderVersStep);
				if (ismultitenant == 1 || ismultitenant == 2) {
					obj.setProtocolordercode(protocoldetail.getProtocolordercode());
					obj.setProtocolorderstepversioncode(protoorderVersStep.getProtocolorderstepversioncode());
					obj.setIdversioncode(protocoldetail.getVersionno());
					obj.setVersionname("version_" + protocoldetail.getVersionno());
					obj.setLsprotocolstepInfo(str);
					CloudLSprotocolorderversionstepRepository.save(obj);
				} else {
					LSprotocolorderversionstepInfo LSprotocolorderversionstepInfo = new LSprotocolorderversionstepInfo();
					LSprotocolorderversionstepInfo.setStepcode(lslogilabprotocolsteps.getStepno());
					LSprotocolorderversionstepInfo.setId(protoorderVersStep.getProtocolorderstepversioncode());
					LSprotocolorderversionstepInfo.setVersionno(protocoldetail.getVersionno());
					LSprotocolorderversionstepInfo.setContent(str);
					mongoTemplate.insert(LSprotocolorderversionstepInfo);
				}

			}
			mapObj.put("response", response);
			List<LSlogilabprotocolsteps> LSprotocolsteplst = LSlogilabprotocolstepsRepository
					.findByProtocolordercodeAndStatus(lslogilabprotocolsteps.getProtocolordercode(), 1);
			int countforstep = LSlogilabprotocolstepsRepository
					.countByProtocolordercodeAndStatus(lslogilabprotocolsteps.getProtocolordercode(), 1);
			List<LSlogilabprotocolsteps> LSprotocolstepLst = new ArrayList<LSlogilabprotocolsteps>();

			for (LSlogilabprotocolsteps LSprotocolstepObj1 : LSprotocolsteplst) {

				if (ismultitenant == 1 || ismultitenant == 2) {

					if (NewStep == 1) {
						LSprotocolstepObj1.setLsprotocolstepInfo(CloudLSprotocolstepInfoObj.getLsprotocolstepInfo());
					} else {
						CloudLsLogilabprotocolstepInfo newLSprotocolstepInfo = CloudLsLogilabprotocolstepInfoRepository
								.findById(LSprotocolstepObj1.getProtocolorderstepcode());
						if (newLSprotocolstepInfo != null) {
							LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
						}
					}
				} else {
					LsLogilabprotocolstepInfo newLSprotocolstepInfo = mongoTemplate
							.findById(LSprotocolstepObj1.getProtocolorderstepcode(), LsLogilabprotocolstepInfo.class);
					if (newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
					}
				}

				LSprotocolstepLst.add(LSprotocolstepObj1);
			}

			List<LSprotocolorderversion> LSprotocolorderversion = lsprotocolorderversionRepository
					.findByProtocolordercodeOrderByVersionnoDesc(protocolordercode);
//			Collections.sort(LSprotocolorderversion, Collections.reverseOrder());

//			LSprotocolstepLst.add(LSprotocolstepObj1);
//			if(LSprotocolorderversion!=null) {
			mapObj.put("protocolorderversionLst", LSprotocolorderversion);
//			}

			mapObj.put("protocolstepLst", LSprotocolstepLst);
			mapObj.put("countforstep", countforstep);
//			mapObj.put("response", response);
		} else {

			response.setInformation("Protocols.IDS_PROTOCOLSTEPNAMEEMPTY");
			response.setStatus(false);
			mapObj.put("response", response);
		}
		return mapObj;
	}

	public ByteArrayInputStream downloadprotocolorderimage(String fileid, String tenant) {
		TenantContext.setCurrentTenant(tenant);
		byte[] data = null;
		try {
			data = StreamUtils.copyToByteArray(
					cloudFileManipulationservice.retrieveCloudFile(fileid, tenant + "protocolorderimages"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(data);

		return bis;
	}

	public ByteArrayInputStream downloadprotocolorderfiles(String fileid, String tenant) {
		byte[] data = null;
		try {
			data = StreamUtils.copyToByteArray(
					cloudFileManipulationservice.retrieveCloudFile(fileid, tenant + "protocolorderfiles"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(data);

		return bis;
	}

	public Map<String, Object> Uploadprotocolorderimage(MultipartFile file, Integer protocolorderstepcode,
			Long protocolordercode, Integer stepno, String protocolstepname, String originurl) {
		Map<String, Object> map = new HashMap<String, Object>();

		String id = null;
		try {
			id = cloudFileManipulationservice.storecloudfilesreturnUUID(file, "protocolorderimages");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LSprotocolorderimages objimg = new LSprotocolorderimages();
		objimg.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
		objimg.setFileid(id);
		objimg.setProtocolorderstepcode(protocolorderstepcode);
		objimg.setProtocolordercode(protocolordercode);
//		objimg.setProtocolmastercode(protocolmastercode);
//		objimg.setProtocolstepcode(protocolstepcode);
		objimg.setProtocolstepname(protocolstepname);
		objimg.setStepno(stepno);
		objimg.setFilename(FilenameUtils.removeExtension(file.getOriginalFilename()));
//		String url = originurl + "/protocol/downloadprotocolimage/" + objimg.getFileid() + "/"
//				+ TenantContext.getCurrentTenant() + "/" + objimg.getFilename() + "/" + objimg.getExtension();
//		Gson g = new Gson();
//		String str = g.toJson(url);
//		objimg.setSrc(str);

		String filename = "No Name";
		if (!objimg.getFilename().isEmpty()) {
			filename = objimg.getFilename();
			objimg.setFilename(filename);
		}

		lsprotocolorderimagesRepository.save(objimg);
		map.put("link", originurl + "/protocol/downloadprotocolorderimage/" + objimg.getFileid() + "/"
				+ TenantContext.getCurrentTenant() + "/" + filename + "/" + objimg.getExtension());

		return map;
	}

	public Map<String, Object> uploadprotocolsorderfile(MultipartFile file, Integer protocolorderstepcode,
			Long protocolordercode, Integer stepno, String protocolstepname, String originurl) {
		Map<String, Object> map = new HashMap<String, Object>();

		String id = null;
		try {
			id = cloudFileManipulationservice.storecloudfilesreturnUUID(file, "protocolorderfiles");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LSprotocolorderfiles objfile = new LSprotocolorderfiles();
		objfile.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
		objfile.setFileid(id);
		objfile.setProtocolordercode(protocolordercode);
		objfile.setProtocolorderstepcode(protocolorderstepcode);
//		objfile.setProtocolmastercode(protocolmastercode);
//		objfile.setProtocolstepcode(protocolstepcode);
		objfile.setProtocolstepname(protocolstepname);
		objfile.setStepno(stepno);
		objfile.setFilename(FilenameUtils.removeExtension(file.getOriginalFilename()));

		lsprotocolorderfilesRepository.save(objfile);

		map.put("link", originurl + "/protocol/downloadprotocolorderfiles/" + objfile.getFileid() + "/"
				+ TenantContext.getCurrentTenant() + "/" + objfile.getFilename() + "/" + objfile.getExtension());
		return map;
	}

	public boolean removeprotocoorderlimage(Map<String, String> body) {
		String filid = body.get("fileid");
		cloudFileManipulationservice.deletecloudFile(filid, "protocolorderimages");
		return true;
	}

	public List<LSprotocolorderfiles> loadprotocolorderfiles(Map<String, String> body) {
		List<LSprotocolorderfiles> lst = new ArrayList<LSprotocolorderfiles>();
		ObjectMapper object = new ObjectMapper();
		Long protocolordercode = object.convertValue(body.get("protocolordercode"), Long.class);
		int protocolorderstepcode = object.convertValue(body.get("protocolorderstepcode"), Integer.class);
		lst = lsprotocolorderfilesRepository
				.findByProtocolordercodeAndProtocolorderstepcodeOrderByProtocolorderstepfilecodeDesc(protocolordercode,
						protocolorderstepcode);
		return lst;
	}

	public Lsprotocolordershareto Insertshareprotocolorder(Lsprotocolordershareto objprotocolordershareto) {
		
		Lsprotocolordershareto existingshare = lsprotocolordersharetoRepository
				.findBySharebyunifiedidAndSharetounifiedidAndProtocoltypeAndShareprotocolordercode(
						objprotocolordershareto.getSharebyunifiedid(), objprotocolordershareto.getSharetounifiedid(),
						objprotocolordershareto.getProtocoltype(), objprotocolordershareto.getShareprotocolordercode());
		if (existingshare != null) {
			objprotocolordershareto.setSharetoprotocolordercode(existingshare.getSharetoprotocolordercode());
			// objordershareto.setSharedon(existingshare.getSharedon());
		}

		lsprotocolordersharetoRepository.save(objprotocolordershareto);

		String Details = "";
		String Notification = "";
		Notification = "PROTOCOLSHAREORDER";

		Details = "{\"ordercode\":\"" + objprotocolordershareto.getSharetoprotocolordercode() + "\", \"order\":\""
				+ objprotocolordershareto.getShareprotoclordername() + "\", \"sharedby\":\""
				+ objprotocolordershareto.getSharebyusername() + "\", \"sharerights\":\""
				+ objprotocolordershareto.getSharerights() + "\"}";

		LSnotification objnotify = new LSnotification();
		objnotify.setNotifationfrom(objprotocolordershareto.getProtocolorders().getObjLoggeduser());
		objnotify.setNotifationto(objprotocolordershareto.getObjLoggeduser());
		objnotify.setNotificationdate(objprotocolordershareto.getSharedon());
		objnotify.setNotification(Notification);
		objnotify.setNotificationdetils(Details);
		objnotify.setIsnewnotification(1);
		objnotify.setNotificationpath("/Protocolorder");
		objnotify.setNotificationfor(1);
		lsnotificationRepository.save(objnotify);

		return objprotocolordershareto;
	}

	public Map<String, Object> Insertshareprotocolorderby(Lsprotocolordersharedby objprotocolordersharedby) {
		Map<String, Object> map = new HashMap<>();
		Lsprotocolordersharedby existingshare = lsprotocolordersharedbyRepository
				.findBySharebyunifiedidAndSharetounifiedidAndProtocoltypeAndShareprotocolordercode(
						objprotocolordersharedby.getSharebyunifiedid(), objprotocolordersharedby.getSharetounifiedid(),
						objprotocolordersharedby.getProtocoltype(),
						objprotocolordersharedby.getShareprotocolordercode());
		if (existingshare != null) {
			objprotocolordersharedby.setSharedbytoprotocolordercode(existingshare.getSharedbytoprotocolordercode());

		}
		lsprotocolordersharedbyRepository.save(objprotocolordersharedby);

		map.put("order", objprotocolordersharedby);

		return map;
	}

	public List<Lsprotocolordersharedby> Getprotocolordersharedbyme(Lsprotocolordersharedby objprotocolordersharedby) {
		List<Lsprotocolordersharedby> obj = lsprotocolordersharedbyRepository
				.findBySharebyunifiedidAndProtocoltypeAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
						objprotocolordersharedby.getSharebyunifiedid(), objprotocolordersharedby.getProtocoltype(), 1,
						objprotocolordersharedby.getFromdate(), objprotocolordersharedby.getTodate());

		return obj;
	}

	public List<Lsprotocolordershareto> Getprotocolordersharedtome(Lsprotocolordershareto objprotocolordershareto) {
		List<Lsprotocolordershareto> obj = lsprotocolordersharetoRepository
				.findBySharetounifiedidAndProtocoltypeAndSharestatusAndSharedonBetweenOrderBySharetoprotocolordercodeDesc(
						objprotocolordershareto.getSharetounifiedid(), objprotocolordershareto.getProtocoltype(), 1,
						objprotocolordershareto.getFromdate(), objprotocolordershareto.getTodate());
		return obj;
	}

	public Lsprotocolshareto Insertshareprotocol(Lsprotocolshareto objprotocolordershareto) {

		Lsprotocolshareto existingshare = LsprotocolsharetoRepository
				.findBySharebyunifiedidAndSharetounifiedidAndShareprotocolcode(
						objprotocolordershareto.getSharebyunifiedid(), objprotocolordershareto.getSharetounifiedid(),
						objprotocolordershareto.getShareprotocolcode());

		if (existingshare != null) {
			objprotocolordershareto.setRetirestatus(objprotocolordershareto.getRetirestatus());
			objprotocolordershareto.setSharetoprotocolcode(existingshare.getSharetoprotocolcode());
		}

		LsprotocolsharetoRepository.save(objprotocolordershareto);
		updatenotificationforprotocolshare(objprotocolordershareto);
		return objprotocolordershareto;
	}

	private void updatenotificationforprotocolshare(Lsprotocolshareto objprotocolordershareto) {
		String Details = "";
		String Notifiction = "";

		Notifiction = "PROTOCOLTEMPSHARE";
		Details = "{\"shareduser\":\"" + objprotocolordershareto.getSharebyusername() + "\", \"privileges\":\""
				+ objprotocolordershareto.getSharerights() + "\", \"protocol\":\""
				+ objprotocolordershareto.getShareprotocolname() + "\"}";

		LSnotification objnotify = new LSnotification();
		objnotify.setNotifationfrom(objprotocolordershareto.getObjLoggeduser());
		objnotify.setNotificationdate(objprotocolordershareto.getSharedon());
		objnotify.setNotifationto(objprotocolordershareto.getSharetousercode());
		objnotify.setNotification(Notifiction);
		objnotify.setNotificationdetils(Details);
		objnotify.setIsnewnotification(1);
		objnotify.setNotificationpath("/protocols");
		objnotify.setNotificationfor(1);

		lsnotificationRepository.save(objnotify);
		Details = null;
		Notifiction = null;
		objnotify = null;
	}

	public Map<String, Object> Insertshareprotocolby(Lsprotocolsharedby objprotocolordersharedby) {
		Map<String, Object> map = new HashMap<>();

		Lsprotocolsharedby existingshare = LsprotocolsharedbyRepository
				.findBySharebyunifiedidAndSharetounifiedidAndShareprotocolcode(
						objprotocolordersharedby.getSharebyunifiedid(), objprotocolordersharedby.getSharetounifiedid(),
						objprotocolordersharedby.getShareprotocolcode());

		if (existingshare != null) {
			objprotocolordersharedby.setSharedbytoprotocolcode(existingshare.getSharedbytoprotocolcode());
		}

		LsprotocolsharedbyRepository.save(objprotocolordersharedby);

		map.put("templateshared", objprotocolordersharedby);

		return map;
	}

	public List<Lsprotocolsharedby> Getprotocolsharedbyme(Lsprotocolsharedby objprotocolordersharedby) {

		List<Lsprotocolsharedby> obj = LsprotocolsharedbyRepository
				.findBySharebyunifiedidAndSharestatusOrderBySharedbytoprotocolcodeDesc(
						objprotocolordersharedby.getSharebyunifiedid(), 1);

		return obj;
	}

	public List<Lsprotocolshareto> Getprotocolsharedtome(Lsprotocolshareto objprotocolordershareto) {
		List<Lsprotocolshareto> obj = LsprotocolsharetoRepository
				.findBySharetounifiedidAndSharestatusOrderBySharetoprotocolcodeDesc(
						objprotocolordershareto.getSharetounifiedid(), 1);
		return obj;
	}

	public Lsprotocolsharedby Unshareprotocolby(Lsprotocolshareto objordershareby) {

		Lsprotocolsharedby existingshare = LsprotocolsharedbyRepository
				.findBysharetoprotocolcodeAndSharestatus(objordershareby.getSharetoprotocolcode(), 1);

		existingshare.setSharestatus(0);
		existingshare.setUnsharedon(objordershareby.getUnsharedon());

		LsprotocolsharedbyRepository.save(existingshare);

		return existingshare;
	}

	public Lsprotocolshareto Unshareorderto(Lsprotocolshareto lsordershareto) {
		Lsprotocolshareto existingshare = LsprotocolsharetoRepository
				.findBysharetoprotocolcode(lsordershareto.getSharetoprotocolcode());

		existingshare.setSharestatus(0);
		existingshare.setUnsharedon(lsordershareto.getUnsharedon());
		existingshare.setShareprotocolcode(lsordershareto.getShareprotocolcode());

		LsprotocolsharetoRepository.save(existingshare);

		return existingshare;
	}

	public Lsprotocolordersharedby Unshareprotocolorderby(Lsprotocolordersharedby objprotocolordersharedby) {
		Lsprotocolordersharedby existingshare = lsprotocolordersharedbyRepository
				.findBySharedbytoprotocolordercode(objprotocolordersharedby.getSharedbytoprotocolordercode());

		existingshare.setSharestatus(0);
		existingshare.setUnsharedon(objprotocolordersharedby.getUnsharedon());
		lsprotocolordersharedbyRepository.save(existingshare);

		return existingshare;

	}

	public Lsprotocolordershareto Unshareprotocolorderto(Lsprotocolordershareto objprotocolordershareto) {
		Lsprotocolordershareto existingshare = lsprotocolordersharetoRepository
				.findBySharetoprotocolordercode(objprotocolordershareto.getSharetoprotocolordercode());

		existingshare.setSharestatus(0);
		existingshare.setUnsharedon(objprotocolordershareto.getUnsharedon());
		existingshare.setSharedbytoprotocolordercode(objprotocolordershareto.getSharedbytoprotocolordercode());
		lsprotocolordersharetoRepository.save(existingshare);

		return existingshare;
	}

	public Map<String, Object> countsherorders(Lsprotocolordersharedby lsprotocolordersharedby) {
		Map<String, Object> lstOrder = new HashMap<String, Object>();
		int sharedbymecount = lsprotocolordersharedbyRepository
				.countBySharebyunifiedidAndProtocoltypeAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
						lsprotocolordersharedby.getSharebyunifiedid(), lsprotocolordersharedby.getProtocoltype(), 1,
						lsprotocolordersharedby.getFromdate(), lsprotocolordersharedby.getTodate());

		int sheredtomecount = lsprotocolordersharetoRepository
				.countBySharetounifiedidAndProtocoltypeAndSharestatusAndSharedonBetweenOrderBySharetoprotocolordercodeDesc(
						lsprotocolordersharedby.getSharebyunifiedid(), lsprotocolordersharedby.getProtocoltype(), 1,
						lsprotocolordersharedby.getFromdate(), lsprotocolordersharedby.getTodate());
		lstOrder.put("sharedbymecount", sharedbymecount);
		lstOrder.put("sheredtomecount", sheredtomecount);
		return lstOrder;
	}

	public LSprotocolmastertest UpdateProtocoltest(LSprotocolmastertest objtest) {

//	if (objtest.getLSfileparameter() != null) {
//		lSfileparameterRepository.save(objtest.getLSfileparameter());
//	}

		LSprotocolmastertestRepository.save(objtest);

		objtest.setResponse(new Response());
		objtest.getResponse().setStatus(true);
		objtest.getResponse().setInformation("ID_SHEETGRP");
		return objtest;
	}

	public List<LSprotocolmaster> getProtocolOnTestcode(LSprotocolmastertest objtest) {

		List<LSprotocolmaster> lsfiles = new ArrayList<LSprotocolmaster>();

		List<LSprotocolmastertest> lsfiletest = LSprotocolmastertestRepository
				.findByTestcodeAndTesttype(objtest.getTestcode(), objtest.getTesttype());

		if (objtest.getObjLoggeduser().getUsername().trim().toLowerCase().equals("administrator")) {
			lsfiles = LSProtocolMasterRepositoryObj
					.findByLstestInAndStatusAndRetirestatusAndApprovedOrderByProtocolmastercodeDesc(lsfiletest, 1, 0,
							1);
		} else {

			List<Integer> lstteammap = LSuserteammappingRepositoryObj
					.getTeamcodeByLsuserMaster(objtest.getObjLoggeduser().getUsercode());
			if (lstteammap.size() > 0) {
				List<LSuserMaster> lstteamuser = LSuserteammappingRepositoryObj.getLsuserMasterByTeamcode(lstteammap);
				lstteamuser.add(objtest.getObjLoggeduser());
				lstteammap = lstteamuser.stream().map(LSuserMaster::getUsercode).collect(Collectors.toList());
			} else {
				lstteammap.add(objtest.getObjLoggeduser().getUsercode());
			}
			
			lsfiles = LSProtocolMasterRepositoryObj
					.findByLssitemasterAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrCreatedbyInAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrCreatedbyAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrCreatedbyInAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrderByProtocolmastercodeDesc(
							objtest.getSitecode(), lsfiletest, 1,0, 1, 1,
							lstteammap, lsfiletest, 1,0, 1, 1,
							objtest.getObjLoggeduser().getUsercode(), lsfiletest, 1, 0,2, 1,
							lstteammap, lsfiletest, 1,0, 3, 1);
			lsfiles.addAll(LSProtocolMasterRepositoryObj
					.findByLssitemasterAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatedbyInAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatedbyAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatedbyInAndLstestInAndProtocolmastercodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrderByProtocolmastercodeDesc(
							objtest.getSitecode(), lsfiletest, 1,0, 1, 0, 1,
							lstteammap, lsfiletest, 1,0, 1, 0, 1,
							objtest.getObjLoggeduser().getUsercode(), lsfiletest, 1,0, 2, 0, 1,
							lstteammap, lsfiletest, 1,0, 3, 0, 1));
		}

		return lsfiles;
	}

	public Map<String, Object> Uploadprotocolimagesql(MultipartFile file, Integer protocolstepcode,
			Integer protocolmastercode, Integer stepno, String protocolstepname, String originurl) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();

//	String id = null;
//	try {
//		id = cloudFileManipulationservice.storecloudfilesreturnUUID(file, "protocolimages");
//	} catch (IOException e) {
//
//		e.printStackTrace();
//	}
		String Fieldid = Generatetenantpassword();
		LSprotocolimages objimg = new LSprotocolimages();
		objimg.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
		objimg.setFileid(Fieldid);
		objimg.setProtocolmastercode(protocolmastercode);
		objimg.setProtocolstepcode(protocolstepcode);
		objimg.setProtocolstepname(protocolstepname);
		objimg.setStepno(stepno);
		objimg.setFilename(FilenameUtils.removeExtension(file.getOriginalFilename()));
		String filename = "No Name";
		if (objimg.getFilename() != null) {
			filename = objimg.getFilename();
		}
		String url = originurl + "/protocol/downloadprotocolimagesql/" + objimg.getFileid() + "/" + filename + "/"
				+ objimg.getExtension();
		Gson g = new Gson();
		String str = g.toJson(url);
		objimg.setSrc(str);
		lsprotocolimagesRepository.save(objimg);

		ProtocolImage protocolImage = new ProtocolImage();
		protocolImage.setId(objimg.getProtocolstepimagecode());
		protocolImage.setName(filename);
		protocolImage.setFileid(Fieldid);
		protocolImage.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
		protocolImage = protocolImageRepository.insert(protocolImage);

		map.put("extension", "." + objimg.getExtension());
		map.put("name", filename);
		map.put("link", originurl + "/protocol/downloadprotocolimagesql/" + protocolImage.getFileid() + "/" + filename
				+ "/" + objimg.getExtension());

		return map;
	}

	public String Generatetenantpassword() {

		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		String pwd = RandomStringUtils.random(15, characters);

		return pwd;
	}

	public ProtocolImage downloadprotocolimagesql(String fileid) {
		return protocolImageRepository.findByFileid(fileid);
	}

	public Map<String, Object> uploadprotocolsfilesql(MultipartFile file, Integer protocolstepcode,
			Integer protocolmastercode, Integer stepno, String protocolstepname, String originurl, String editoruuid)
			throws IOException {

		Map<String, Object> map = new HashMap<String, Object>();

//	String id = null;
//	try {
//		id = cloudFileManipulationservice.storecloudfilesreturnUUID(file, "protocolfiles");
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
		String Fieldid = Generatetenantpassword();
		LSprotocolfiles objfile = new LSprotocolfiles();

		String filenamedata = FilenameUtils.removeExtension(file.getOriginalFilename());

		LSprotocolfiles parentfile = lsprotocolfilesRepository
				.findFirst1ByEditoruuidAndFilenameOrderByProtocolstepfilecodeDesc(editoruuid, filenamedata);

		objfile.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
		objfile.setFileid(Fieldid);
		objfile.setProtocolmastercode(protocolmastercode);
		objfile.setProtocolstepcode(protocolstepcode);
		objfile.setProtocolstepname(protocolstepname);
		objfile.setStepno(stepno);
		objfile.setEditoruuid(editoruuid);
		if (parentfile != null) {
			Integer versiondata = parentfile.getVersion() != null ? parentfile.getVersion() + 1 : 1;
			objfile.setFilename(filenamedata + "_V" + (versiondata));
			parentfile.setVersion(versiondata);
			lsprotocolfilesRepository.save(parentfile);
		} else {
			objfile.setFilename(filenamedata);
		}
		objfile.setVersion(0);

		lsprotocolfilesRepository.save(objfile);

		LSprotocolfilesContent objattachment = new LSprotocolfilesContent();
		objattachment.setId(objfile.getProtocolstepfilecode());
		objattachment.setName(objfile.getFilename());
		objattachment.setFileid(Fieldid);
		objattachment.setFile(new Binary(BsonBinarySubType.BINARY, file.getBytes()));

		objattachment = lsprotocolfilesContentRepository.insert(objattachment);
		map.put("name", objfile.getFilename());
		map.put("extension", "." + objfile.getExtension());
		map.put("link", originurl + "/protocol/downloadprotocolfilesql/" + objfile.getFileid() + "/"
				+ objfile.getFilename() + "/" + objfile.getExtension());
		return map;

	}

	public LSprotocolfilesContent downloadprotocolfilesql(String fileid) {
		return lsprotocolfilesContentRepository.findByFileid(fileid);
	}

	public boolean removeprotocolimagesql(Map<String, String> body) {
		String filid = body.get("fileid");
		protocolImageRepository.delete(filid);
		return true;
	}

	public ProtocolorderImage downloadprotocolorderimagesql(String fileid) {
		return ProtocolorderImageRepository.findByFileid(fileid);
	}

	public LSprotocolfilesContent downloadprotocolorderfilesql(String fileid) {
		return LSprotocolorderfilesContentRepository.findByFileid(fileid);
	}

	public Map<String, Object> protocolordersampleupdates(LSprotocolordersampleupdates lsprotocolordersampleupdates) {
		Map<String, Object> obj = new HashMap<String, Object>();
		lsprotocolordersampleupdatesRepository.save(lsprotocolordersampleupdates);
		List<LSprotocolordersampleupdates> lsprotocolsamplelist = lsprotocolordersampleupdatesRepository
				.findByProtocolordercodeAndProtocolstepcodeAndIndexofIsNotNullAndStatus(
						lsprotocolordersampleupdates.getProtocolordercode(),
						lsprotocolordersampleupdates.getProtocolstepcode(), 1);
		obj.put("lsprotocolsampleupdates", lsprotocolordersampleupdates);
		obj.put("lsprotocolsamplelist", lsprotocolsamplelist);

		return obj;
	}

	public Map<String, Object> consumableinventorynotificationprotocol(
			LSprotocolordersampleupdates lsprotocolordersampleupdates) {
		String Details = "";
		String Notification = "";
		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
//		Map<String, Object> obj = new HashMap<String, Object>();
		LSnotification objnotify = new LSnotification();
		LSuserMaster createby = lsusermasterRepository.findByusercode(lsprotocolordersampleupdates.getUsercode());
		if (lsprotocolordersampleupdates.getInventoryconsumed() == 20) {
			Notification = "INVENTORYCONSUMED";
			Details = "{\"inventory\":\"" + lsprotocolordersampleupdates.getInventoryid()
					+ lsprotocolordersampleupdates.getRepositorydatacode() + "\"}";
			objnotify.setNotificationdate(lsprotocolordersampleupdates.getCreateddate());
			objnotify.setNotifationto(createby);
			objnotify.setNotification(Notification);
			objnotify.setNotificationdetils(Details);
			objnotify.setIsnewnotification(1);
			objnotify.setNotificationpath("/inventory");
			objnotify.setNotificationfor(1);
			lstnotifications.add(objnotify);
			lsnotificationRepository.save(lstnotifications);
		}
		return null;
	}

	public Map<String, Object> Outofstockinventorynotificationprotocol(
			LSprotocolordersampleupdates lsprotocolordersampleupdates) {

		String Details = "";
		String Notification = "";

		List<UserProjection1> ordersample1 = lsprotocolordersampleupdatesRepository
				.getDistinctRepositorydatacode(lsprotocolordersampleupdates.getRepositorydatacode());

		ArrayList<LSnotification> lstnotifications = new ArrayList<LSnotification>();

		for (int i = 0; i < ordersample1.size(); i++) {
			LSnotification objnotify = new LSnotification();
			LSuserMaster objuser = lsusermasterRepository.findByusercode(ordersample1.get(i).getUsercode());
			if (lsprotocolordersampleupdates.getInventoryconsumed() == 0) {
				Notification = "INVENTORYOUTOFSTOCK";

				Details = "{\"orderid\":\"" + ordersample1.get(i).getProtocolordercode()

						+ "\"}";
				objnotify.setNotifationto(objuser);
				objnotify.setNotificationdate(lsprotocolordersampleupdates.getCreateddate());
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
		return null;

	}

	public List<LSprotocolorderworkflowhistory> getProtocolOrderworkflowhistoryList(
			LSprotocolorderworkflowhistory lsprotocolorderworkflowhistory) {
		List<LSprotocolorderworkflowhistory> LSprotocolorderworkflowhistory = lsprotocolorderworkflowhistoryRepository
				.findByProtocolordercodeAndCreatedateBetween(lsprotocolorderworkflowhistory.getProtocolordercode(),
						lsprotocolorderworkflowhistory.getFromdate(), lsprotocolorderworkflowhistory.getTodate());
		return LSprotocolorderworkflowhistory;
	}

	public Map<String, Object> uploadvideo(MultipartFile file, Integer protocolstepcode, Integer protocolmastercode,
			Integer stepno, String protocolstepname, String originurl) {
		Map<String, Object> map = new HashMap<String, Object>();
		String id = null;
		try {
			id = cloudFileManipulationservice.storecloudfilesreturnUUID(file, "protocolvideo");
		} catch (IOException e) {

			e.printStackTrace();
		}
		LSprotocolvideos objimg = new LSprotocolvideos();
		objimg.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
		objimg.setFileid(id);
		objimg.setProtocolmastercode(protocolmastercode);
		objimg.setProtocolstepcode(protocolstepcode);
		objimg.setProtocolstepname(protocolstepname);
		objimg.setStepno(stepno);
		objimg.setFilename(FilenameUtils.removeExtension(file.getOriginalFilename()));
//		String url = originurl + "/protocol/downloadprotocolimage/" + objimg.getFileid() + "/"
//				+ TenantContext.getCurrentTenant() + "/" + objimg.getFilename() + "/" + objimg.getExtension();
//		Gson g = new Gson();
//		String str = g.toJson(url);
//		objimg.setSrc(str);
		lsprotocolvideosRepository.save(objimg);
		map.put("link", originurl + "/protocol/downloadprotocolvideo/" + objimg.getFileid() + "/"
				+ TenantContext.getCurrentTenant() + "/" + objimg.getFilename() + "/" + objimg.getExtension());

		return map;

	}

	public ByteArrayInputStream downloadprotocolvideo(String fileid, String tenant) {
		TenantContext.setCurrentTenant(tenant);
		byte[] data = null;
		try {
			data = StreamUtils
					.copyToByteArray(cloudFileManipulationservice.retrieveCloudFile(fileid, tenant + "protocolvideo"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(data);

		return bis;

	}

	public boolean removeprotocolvideo(Map<String, String> body) {
		String filid = body.get("fileid");
		cloudFileManipulationservice.deletecloudFile(filid, "protocolvideo");
		return true;
	}

	public Map<String, Object> uploadvideosql(MultipartFile file, Integer protocolstepcode, Integer protocolmastercode,
			Integer stepno, String protocolstepname, String originurl) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		String Fieldid = Generatetenantpassword();

		LSprotocolvideos objimg = new LSprotocolvideos();
		objimg.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
		objimg.setFileid(Fieldid);
		objimg.setProtocolmastercode(protocolmastercode);
		objimg.setProtocolstepcode(protocolstepcode);
		objimg.setProtocolstepname(protocolstepname);
		objimg.setStepno(stepno);
		objimg.setFilename(FilenameUtils.removeExtension(file.getOriginalFilename()));
		lsprotocolvideosRepository.save(objimg);

		Protocolvideos protocolImage = new Protocolvideos();
		protocolImage.setId(objimg.getProtocolstepvideoscode());
		protocolImage.setName(objimg.getFilename());
		protocolImage.setFileid(Fieldid);
		protocolImage.setVideo(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
		protocolImage = ProtocolvideosRepository.insert(protocolImage);

		map.put("link", originurl + "/protocol/downloadprotocolvideosql/" + protocolImage.getFileid() + "/"
				+ objimg.getFilename() + "/" + objimg.getExtension());

		return map;
	}

	public Protocolvideos downloadprotocolvideosql(String fileid) {
		return ProtocolvideosRepository.findByFileid(fileid);
	}

	public boolean removeprotocolvideossql(Map<String, String> body) {
		String filid = body.get("fileid");
		ProtocolvideosRepository.delete(filid);
		return true;
	}

	public Map<String, Object> uploadprotocolordervideo(MultipartFile file, Integer protocolorderstepcode,
			Long protocolordercode, Integer stepno, String protocolstepname, String originurl) {

		Map<String, Object> map = new HashMap<String, Object>();

		String id = null;
		try {
			id = cloudFileManipulationservice.storecloudfilesreturnUUID(file, "protocolordervideo");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LSprotocolordervideos objimg = new LSprotocolordervideos();
		objimg.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
		objimg.setFileid(id);
		objimg.setProtocolorderstepcode(protocolorderstepcode);
		objimg.setProtocolordercode(protocolordercode);
//		objimg.setProtocolmastercode(protocolmastercode);
//		objimg.setProtocolstepcode(protocolstepcode);
		objimg.setProtocolstepname(protocolstepname);
		objimg.setStepno(stepno);
		objimg.setFilename(FilenameUtils.removeExtension(file.getOriginalFilename()));
//		String url = originurl + "/protocol/downloadprotocolimage/" + objimg.getFileid() + "/"
//				+ TenantContext.getCurrentTenant() + "/" + objimg.getFilename() + "/" + objimg.getExtension();
//		Gson g = new Gson();
//		String str = g.toJson(url);
//		objimg.setSrc(str);
		LSprotocolordervideosRepository.save(objimg);
		map.put("link", originurl + "/protocol/downloadprotocolordervideo/" + objimg.getFileid() + "/"
				+ TenantContext.getCurrentTenant() + "/" + objimg.getFilename() + "/" + objimg.getExtension());

		return map;

	}

	public ByteArrayInputStream downloadprotocolordervideo(String fileid, String tenant) {

		TenantContext.setCurrentTenant(tenant);
		byte[] data = null;
		try {
			data = StreamUtils.copyToByteArray(
					cloudFileManipulationservice.retrieveCloudFile(fileid, tenant + "protocolordervideo"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(data);

		return bis;

	}

	public Map<String, Object> Uploadprotocolorderimagesql(MultipartFile file, Integer protocolorderstepcode,
			Long protocolordercode, Integer stepno, String protocolstepname, String originurl) throws IOException {

		Map<String, Object> map = new HashMap<String, Object>();

		String Fieldid = Generatetenantpassword();
		LSprotocolorderimages objorderimag = new LSprotocolorderimages();
		objorderimag.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
		objorderimag.setFileid(Fieldid);
		objorderimag.setProtocolordercode(protocolordercode);
		objorderimag.setProtocolorderstepcode(protocolorderstepcode);
		objorderimag.setProtocolstepname(protocolstepname);
		objorderimag.setStepno(stepno);
		objorderimag.setFilename(FilenameUtils.removeExtension(file.getOriginalFilename()));
		lsprotocolorderimagesRepository.save(objorderimag);

//			ProtocolImage protocolImage = protocolImageRepository.findByFileid(LSprotocolimagesobj.getFileid());
		ProtocolorderImage poimg = new ProtocolorderImage();
		poimg.setFileid(Fieldid);
		poimg.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
		poimg.setName(objorderimag.getFilename());
		poimg.setId(objorderimag.getProtocolorderstepimagecode());
		ProtocolorderImageRepository.insert(poimg);

		map.put("link", originurl + "/protocol/downloadprotocolorderimagesql/" + poimg.getFileid() + "/"
				+ objorderimag.getFilename() + "/" + objorderimag.getExtension());
//		}
		return map;

	}

	public Map<String, Object> uploadprotocolsorderfilesql(MultipartFile file, Integer protocolorderstepcode,
			Long protocolordercode, Integer stepno, String protocolstepname, String originurl) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		String Fieldid = Generatetenantpassword();
		LSprotocolorderfiles objorderimag = new LSprotocolorderfiles();
		objorderimag.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
		objorderimag.setFileid(Fieldid);
		objorderimag.setProtocolordercode(protocolordercode);
		objorderimag.setProtocolorderstepcode(protocolorderstepcode);
		objorderimag.setProtocolstepname(protocolstepname);
		objorderimag.setStepno(stepno);
//		objorderimag.setOldfileid(oldfieldif);
		objorderimag.setFilename(FilenameUtils.removeExtension(file.getOriginalFilename()));

		lsprotocolorderfilesRepository.save(objorderimag);

//		LSprotocolfilesContent content = lsprotocolfilesContentRepository.findByFileid(LSprotocolfiles.getFileid());
		LSprotocolorderfilesContent orderfilecontent = new LSprotocolorderfilesContent();
		orderfilecontent.setFileid(Fieldid);
		orderfilecontent.setName(objorderimag.getFilename());
		orderfilecontent.setId(objorderimag.getProtocolorderstepfilecode());
		orderfilecontent.setFile(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
		LSprotocolorderfilesContentRepository.insert(orderfilecontent);

		map.put("link", originurl + "/protocol/downloadprotocolorderfilesql/" + objorderimag.getFileid() + "/"
				+ objorderimag.getFilename() + "/" + objorderimag.getExtension());

		return map;
	}

	public Map<String, Object> uploadprotocolordervideosql(MultipartFile file, Integer protocolorderstepcode,
			Long protocolordercode, Integer stepno, String protocolstepname, String originurl) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();

		String Fieldid = Generatetenantpassword();
		LSprotocolordervideos objorderimag = new LSprotocolordervideos();
		objorderimag.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
		objorderimag.setFileid(Fieldid);
		objorderimag.setProtocolordercode(protocolordercode);
		objorderimag.setProtocolorderstepcode(protocolorderstepcode);
		objorderimag.setProtocolstepname(protocolstepname);
		objorderimag.setStepno(stepno);
		objorderimag.setFilename(FilenameUtils.removeExtension(file.getOriginalFilename()));

		LSprotocolordervideosRepository.save(objorderimag);

//		Protocolvideos protocolImage =ProtocolvideosRepository.findByFileid(LSprotocolimagesobj.getFileid());
		Protocolordervideos poimg = new Protocolordervideos();
		poimg.setFileid(Fieldid);
		poimg.setVideo(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
		poimg.setName(objorderimag.getFilename());
		poimg.setId(objorderimag.getProtocolorderstepvideoscode());
		ProtocolordervideosRepository.insert(poimg);

		map.put("link", originurl + "/protocol/downloadprotocolordervideosql/" + poimg.getFileid() + "/"
				+ objorderimag.getFilename() + "/" + objorderimag.getExtension());
		return map;
	}

	public Protocolordervideos downloadprotocolordervideosql(String fileid) {
		return ProtocolordervideosRepository.findByFileid(fileid);
	}

	public boolean removeprotocoorderlimagesql(Map<String, String> body) {
		String filid = body.get("fileid");
		ProtocolorderImageRepository.delete(filid);
		return true;
	}

	public boolean removeprotocolordervideo(Map<String, String> body) {
		String filid = body.get("fileid");
		cloudFileManipulationservice.deletecloudFile(filid, "protocolordervideo");
		return true;
	}

	public boolean removeprotocolordervideossql(Map<String, String> body) {
		String filid = body.get("fileid");
		ProtocolordervideosRepository.delete(filid);
		return true;
	}

	public boolean getprojectteam(LSuserMaster objClass) {
		if (objClass.getUsercode() != null) {
			List<LSuserteammapping> obj = LSuserteammappingRepositoryObj.findByLsuserMasterAndTeamcodeNotNull(objClass);
			if (obj.size() != 0) {
				return true;
			}
		}
		return false;
	}

	public LSlogilabprotocolsteps skipprotocolstep(LSlogilabprotocolsteps lslogilabprotocolsteps) {
		LSlogilabprotocolstepsRepository.save(lslogilabprotocolsteps);
		if (lslogilabprotocolsteps.getIsmultitenant() == 1 || lslogilabprotocolsteps.getIsmultitenant() == 2) {

			CloudLsLogilabprotocolstepInfo newLSprotocolstepInfo = CloudLsLogilabprotocolstepInfoRepository
					.findById(lslogilabprotocolsteps.getProtocolorderstepcode());

			if (newLSprotocolstepInfo != null) {
				lslogilabprotocolsteps.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
			}

		} else {

			LsLogilabprotocolstepInfo newLSprotocolstepInfo = mongoTemplate
					.findById(lslogilabprotocolsteps.getProtocolorderstepcode(), LsLogilabprotocolstepInfo.class);
			if (newLSprotocolstepInfo != null) {
				lslogilabprotocolsteps.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
			}
		}
//		LSprotocolstepInformation lsprotocolstepInformation = lsprotocolstepInformationRepository
//				.findById(lslogilabprotocolsteps.getProtocolstepcode());
//		lslogilabprotocolsteps.setLsprotocolstepInfo(lsprotocolstepInformation.getLsprotocolstepInfo());
		return lslogilabprotocolsteps;
	}

	public List<LStestmasterlocal> gettaskmaster() {
		List<LStestmasterlocal> LStestmasterlocal = lstestmasterlocalRepository.findAll();
		return LStestmasterlocal;
	}

	@SuppressWarnings("unused")
	public Map<String, Object> getswitchdata(LSlogilabprotocoldetail lSlogilabprotocoldetail) {
		Map<String, Object> lstOrder = new HashMap<String, Object>();
		ObjectMapper obj = new ObjectMapper();
		String orderflag = obj.convertValue(lSlogilabprotocoldetail.getOrderflag(), String.class);
		if (lSlogilabprotocoldetail.getOrderflag().equals("M")) {

			int completedcount = (int) LSlogilabprotocoldetailRepository
//					.countByProtocoltypeAndSitecodeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetween(
					.countByProtocoltypeAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetween(
							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(),
							lSlogilabprotocoldetail.getLsuserMaster(), lSlogilabprotocoldetail.getAssignedto(),
							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

			List<Logilabprotocolorders> lstCompletedOrder = LSlogilabprotocoldetailRepository
					.findByProtocoltypeAndSitecodeAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(),
							lSlogilabprotocoldetail.getAssignedto(), lSlogilabprotocoldetail.getFromdate(),
							lSlogilabprotocoldetail.getTodate());

			lstOrder.put("lstMyOrder", lstCompletedOrder);
			lstOrder.put("assignedordercount", completedcount);

		} else if (lSlogilabprotocoldetail.getOrderflag().equals("A")) {

			int completedcount = (int) LSlogilabprotocoldetailRepository
//					.countByProtocoltypeAndSitecodeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetween(
					.countByProtocoltypeAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetween(
							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(),
							lSlogilabprotocoldetail.getLsuserMaster(), lSlogilabprotocoldetail.getAssignedto(),
							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

			List<Logilabprotocolorders> lstCompletedOrder = LSlogilabprotocoldetailRepository
//					.findByProtocoltypeAndSitecodeAndOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
					.findByProtocoltypeAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
							lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(),
							lSlogilabprotocoldetail.getLsuserMaster(), lSlogilabprotocoldetail.getAssignedto(),
							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
			lstOrder.put("lstAssignedOrder", lstCompletedOrder);
			lstOrder.put("assignedordercount", completedcount);

//			lstOrder.put("assignedordercount", completedcount);
		}

		return lstOrder;
	}

	public Map<String, Object> updatesharetomeorder(Lsprotocolordershareto lsprotocolordershareto) {
		Map<String, Object> mapobject = new HashMap<String, Object>();
//		Lsprotocolordershareto existingshare = lsprotocolordersharetoRepository
//				.findBySharetoprotocolordercode(lsprotocolordershareto.getSharetoprotocolordercode());
		Lsprotocolordershareto obj = lsprotocolordersharetoRepository
				.findBySharetoprotocolordercodeAndSharestatus(lsprotocolordershareto.getSharetoprotocolordercode(), 1);
		if (obj != null) {
			obj.setShareitemdetails(lsprotocolordershareto.getShareitemdetails());
			if (lsprotocolordershareto.getOrderflag() != null) {
				Lsprotocolordersharedby object = lsprotocolordersharedbyRepository
						.findByShareprotocolordercodeAndSharestatus(obj.getShareprotocolordercode(), 1);
				mapobject.put("Lsprotocolordersharedby", object);
			}
			lsprotocolordersharetoRepository.save(obj);
			mapobject.put("curentorder", obj);
		}
		return mapobject;
	}

	public Map<String, Object> updatesharebymemeorder(Lsprotocolordersharedby lsprotocolordersharedby) {
		if (lsprotocolordersharedby.getShareitemdetails() != null) {
			lsprotocolordersharedbyRepository.save(lsprotocolordersharedby);
		}
		return null;
	}

	@SuppressWarnings("unused")
	public Map<String, Object> Getprotocollinksignaturesql(Map<String, String> body) {
		Map<String, Object> obj = new HashMap<String, Object>();
		ObjectMapper object = new ObjectMapper();

		Integer signaturefrom = object.convertValue(body.get("signaturefrom"), Integer.class);
		String originurl = object.convertValue(body.get("originurl"), String.class);
		String protocolstepname = object.convertValue(body.get("protocolstepname"), String.class);
		Integer protocolmastercode = object.convertValue(body.get("protocolmastercode"), Integer.class);
		Long protocolordercode = new Long(protocolmastercode);
		Integer stepno = object.convertValue(body.get("stepno"), Integer.class);
		Integer protocolstepcode = object.convertValue(body.get("protocolstepcode"), Integer.class);
		Integer usercode = object.convertValue(body.get("usercode"), Integer.class);

//		CloudUserSignature objsignature = cloudFileManipulationservice.getSignature(usercode);

		UserSignature objsignature = fileManipulationservice.getsignature(usercode);

		byte[] data = null;

		if (objsignature != null) {
			data = objsignature.getImage().getData();
		} else {
			try {
				data = StreamUtils.copyToByteArray(new ClassPathResource("images/nosignature.png").getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(data);

		String fileName = "signature.png";
		CustomMultipartFile customMultipartFile = new CustomMultipartFile(data, fileName);

		if (signaturefrom == 1) {
			try {
				obj = Uploadprotocolimagesql(customMultipartFile, protocolstepcode, protocolmastercode, stepno,
						protocolstepname, originurl);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
//			obj = Uploadprotocolorderimage(customMultipartFile, protocolstepcode, protocolordercode, stepno,
//					protocolstepname, originurl);
			try {
				obj = Uploadprotocolorderimagesql(customMultipartFile, protocolstepcode, protocolordercode, stepno,
						protocolstepname, originurl);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			obj.put("curendateandtime", commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	public Map<String, Object> addProtocolOrderafterfirstofter(LSlogilabprotocoldetail lSlogilabprotocoldetail) {
		Map<String, Object> obj = new HashMap<>();
		if (lSlogilabprotocoldetail.getProtocoltype() == 1) {
			if (lSlogilabprotocoldetail.getCloudLsLogilabprotocolstepInfo() != null) {
				CloudLsLogilabprotocolstepInfoRepository
						.save(lSlogilabprotocoldetail.getCloudLsLogilabprotocolstepInfo());
				obj.put("true", true);
				obj.put("addprotocl", lSlogilabprotocoldetail);
			} else {
				obj.put("false", false);
			}
			if (lSlogilabprotocoldetail.getLsLogilabprotocolstepInfo() != null) {
				for (LsLogilabprotocolstepInfo object : lSlogilabprotocoldetail.getLsLogilabprotocolstepInfo()) {
					Query query = new Query(Criteria.where("id").is(object.getId()));
					Update update = new Update();
					update.set("content", object.getContent());
					mongoTemplate.upsert(query, update, LsLogilabprotocolstepInfo.class);
				}
				obj.put("addprotocl", lSlogilabprotocoldetail);
				obj.put("true", true);
			} else {
				obj.put("false", false);
			}
		}
		return obj;
	}

	public Map<String, Object> GetProtocolorderVersionDetails(LSlogilabprotocoldetail lSlogilabprotocoldetail) {
		Map<String, Object> mapObj = new HashMap<String, Object>();

		List<LSprotocolorderversion> lsprotocolorderversion = lsprotocolorderversionRepository
				.findByProtocolordercodeAndStatusAndCreatedateBetween(lSlogilabprotocoldetail.getProtocolordercode(), 1,
						lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
		mapObj.put("lsprotocolorderversion", lsprotocolorderversion);

		return mapObj;

	}

	public List<LSprotocolstep> updateprotocolstepno(LSprotocolstep[] lSprotocolstep) {
		List<LSprotocolstep> LSprotocolstepobj = Arrays.asList(lSprotocolstep);
		if (lSprotocolstep != null) {
			LSProtocolStepRepositoryObj.save(LSprotocolstepobj);
		}

		return LSprotocolstepobj;
	}

	public List<LSlogilabprotocolsteps> updateprotocolstepnoonorder(LSlogilabprotocolsteps[] lSlogilabprotocolsteps) {
		List<LSlogilabprotocolsteps> LSprotocolstepobj = Arrays.asList(lSlogilabprotocolsteps);
		if (LSprotocolstepobj != null) {
			LSlogilabprotocolstepsRepository.save(LSprotocolstepobj);
		}

		return LSprotocolstepobj;
	}

	public LSprotocolstepInformation getprotocolperticulerstep(Integer InteInteprotocolstepcodeorordercode,
			Integer multitenant, Integer protocoltype) {
		LSprotocolstepInformation newobj = new LSprotocolstepInformation();
		if (multitenant == 1) {
			newobj = lsprotocolstepInformationRepository.findById(InteInteprotocolstepcodeorordercode);
		} else {
			LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(InteInteprotocolstepcodeorordercode,
					LSprotocolstepInfo.class);
			if (newLSprotocolstepInfo != null) {
				newobj.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());

			}
		}
		return newobj;
	}

	public Map<String, Object> Getprotocolordersonassignedandmyorders(Map<String, Object> objusers) {
		ObjectMapper obj = new ObjectMapper();
		LSlogilabprotocoldetail lSlogilabprotocoldetail = obj.convertValue(objusers.get("lSlogilabprotocoldetail"),
				new TypeReference<LSlogilabprotocoldetail>() {
				});
		List<Logilabprotocolorders> lstorder = new ArrayList<Logilabprotocolorders>();
		Map<String, Object> retuobjts = new HashMap<>();
		Integer protocoltype = lSlogilabprotocoldetail.getProtocoltype();
		String Orderflag = null;
		if (objusers.containsKey("orderflag")) {
			Orderflag = obj.convertValue(objusers.get("orderflag"), String.class);
		}
		if (lSlogilabprotocoldetail.getOrderflag().equals("M")) {

			if (protocoltype == -1 && Orderflag == null) {
				lstorder = LSlogilabprotocoldetailRepository
						.findBySitecodeAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
								lSlogilabprotocoldetail.getSitecode(), lSlogilabprotocoldetail.getAssignedto(),
								lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
			} else if (protocoltype != -1 && Orderflag != null) {
				lstorder = LSlogilabprotocoldetailRepository
						.findByProtocoltypeAndOrderflagAndSitecodeAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
								lSlogilabprotocoldetail.getProtocoltype(), Orderflag,
								lSlogilabprotocoldetail.getSitecode(), lSlogilabprotocoldetail.getAssignedto(),
								lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

			} else if (protocoltype == -1 && Orderflag != null) {
				lstorder = LSlogilabprotocoldetailRepository
						.findBySitecodeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
								lSlogilabprotocoldetail.getSitecode(), Orderflag,
								lSlogilabprotocoldetail.getAssignedto(), lSlogilabprotocoldetail.getFromdate(),
								lSlogilabprotocoldetail.getTodate());

			} else if (protocoltype != -1 && Orderflag == null) {
				lstorder = LSlogilabprotocoldetailRepository
						.findByProtocoltypeAndSitecodeAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
								lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(),
								lSlogilabprotocoldetail.getAssignedto(), lSlogilabprotocoldetail.getFromdate(),
								lSlogilabprotocoldetail.getTodate());
			}
			retuobjts.put("protocolorders", lstorder);
		} else if (lSlogilabprotocoldetail.getOrderflag().equals("A")) {

			if (protocoltype == -1 && Orderflag == null) {
				lstorder = LSlogilabprotocoldetailRepository
						.findBySitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
								lSlogilabprotocoldetail.getSitecode(), lSlogilabprotocoldetail.getLsuserMaster(),
								lSlogilabprotocoldetail.getAssignedto(), lSlogilabprotocoldetail.getFromdate(),
								lSlogilabprotocoldetail.getTodate());
			} else if (protocoltype != -1 && Orderflag != null) {
				lstorder = LSlogilabprotocoldetailRepository
						.findByProtocoltypeAndOrderflagAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
								lSlogilabprotocoldetail.getProtocoltype(), Orderflag,
								lSlogilabprotocoldetail.getSitecode(), lSlogilabprotocoldetail.getLsuserMaster(),
								lSlogilabprotocoldetail.getAssignedto(), lSlogilabprotocoldetail.getFromdate(),
								lSlogilabprotocoldetail.getTodate());

			} else if (protocoltype == -1 && Orderflag != null) {
				lstorder = LSlogilabprotocoldetailRepository
						.findByOrderflagAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
								Orderflag, lSlogilabprotocoldetail.getSitecode(),
								lSlogilabprotocoldetail.getLsuserMaster(), lSlogilabprotocoldetail.getAssignedto(),
								lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

			} else if (protocoltype != -1 && Orderflag == null) {
				lstorder = LSlogilabprotocoldetailRepository
						.findByProtocoltypeAndSitecodeAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
								lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getSitecode(),
								lSlogilabprotocoldetail.getLsuserMaster(), lSlogilabprotocoldetail.getAssignedto(),
								lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
			}
			retuobjts.put("protocolorders", lstorder);
		}
		lstorder.forEach(objorderDetail -> objorderDetail
				.setLstelnprotocolworkflow(lSlogilabprotocoldetail.getLstelnprotocolworkflow()));
		List<Long> protocolordercode = new ArrayList<>();
		if (lstorder.size() > 0 && lSlogilabprotocoldetail.getSearchCriteriaType() != null) {
			protocolordercode = lstorder.stream().map(Logilabprotocolorders::getProtocolordercode)
					.collect(Collectors.toList());
			retuobjts.put("protocolordercodeslist", protocolordercode);
		}

		return retuobjts;
	}

	public Map<String, Object> Getprotocolordersonshared(Map<String, Object> objusers) {
		Map<String, Object> mapuserorders = new HashMap<String, Object>();

		ObjectMapper mapper = new ObjectMapper();
//		LSuserMaster lsloginuser = mapper.convertValue(objusers.get("loginuser"), LSuserMaster.class);
		LSuserMaster lsselecteduser = mapper.convertValue(objusers.get("selecteduser"), LSuserMaster.class);
		Date fromdate = lsselecteduser.getObjuser().getFromdate();
		Date todate = lsselecteduser.getObjuser().getTodate();
//		Integer directory = mapper.convertValue(objusers.get("directorycode"), Integer.class);
//		String Unifieduserid =(String) objusers.get("Unifieduserid");
//		Integer protocoltype =(Integer) objusers.get("protocoltype") mapper.convertValue(objusers.get("protocoltype"), Integer.class);
		String Orderflag = null;
		if (objusers.get("Orderflag") != null) {
			Orderflag = mapper.convertValue(objusers.get("Orderflag"), String.class);
		}
		List<Lsprotocolordershareto> sharetome = new ArrayList<>();
		List<Lsprotocolordersharedby> sharebyme = new ArrayList<>();
//		List<LSworkflow> workflow = mapper.convertValue(objusers.get("workflow"),
//				new TypeReference<List<LSworkflow>>() {
//				});
		List<Elnprotocolworkflow> workflow = mapper.convertValue(objusers.get("workflow"),
				new TypeReference<List<Elnprotocolworkflow>>() {
				});
		if (objusers.get("filefor").equals("OSBM")) {

			if ((Integer) objusers.get("protocoltype") == -1 && Orderflag == null) {
//				mapuserorders.put("sharebyme",
				sharebyme = lsprotocolordersharedbyRepository
						.findBySharebyunifiedidAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
								(String) objusers.get("Unifieduserid"), 1, fromdate, todate);

			} else if ((Integer) objusers.get("protocoltype") != -1 && Orderflag != null) {

				sharebyme = lsprotocolordersharedbyRepository
						.findBySharebyunifiedidAndProtocoltypeAndOrderflagAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
								(String) objusers.get("Unifieduserid"), (Integer) objusers.get("protocoltype"),
								Orderflag, 1, fromdate, todate);
			} else if ((Integer) objusers.get("protocoltype") == -1 && Orderflag != null) {
				sharebyme = lsprotocolordersharedbyRepository
						.findBySharebyunifiedidAndOrderflagAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
								(String) objusers.get("Unifieduserid"), Orderflag, 1, fromdate, todate);
			} else if ((Integer) objusers.get("protocoltype") != -1 && Orderflag == null) {
				sharebyme = lsprotocolordersharedbyRepository
						.findBySharebyunifiedidAndProtocoltypeAndSharestatusAndSharedonBetweenOrderBySharedbytoprotocolordercodeDesc(
								(String) objusers.get("Unifieduserid"), (Integer) objusers.get("protocoltype"), 1,
								fromdate, todate);
			}
			if (sharebyme.size() > 0) {
				sharebyme.forEach(
						objorderDetail -> objorderDetail.getProtocolorders().setLstelnprotocolworkflow(workflow));
				mapuserorders.put("sharebyme", sharebyme);
			}

		} else if (objusers.get("filefor").equals("OSTM")) {

			if ((Integer) objusers.get("protocoltype") == -1 && Orderflag == null) {
//				mapuserorders.put("sharetome",
				sharetome = lsprotocolordersharetoRepository
						.findBySharetounifiedidAndSharestatusAndSharedonBetweenOrderBySharetoprotocolordercodeDesc(
								(String) objusers.get("Unifieduserid"), 1, fromdate, todate);

			} else if ((Integer) objusers.get("protocoltype") != -1 && Orderflag != null) {

				sharetome = lsprotocolordersharetoRepository
						.findBySharetounifiedidAndProtocoltypeAndOrderflagAndSharestatusAndSharedonBetweenOrderBySharetoprotocolordercodeDesc(
								(String) objusers.get("Unifieduserid"), (Integer) objusers.get("protocoltype"),
								Orderflag, 1, fromdate, todate);

			} else if ((Integer) objusers.get("protocoltype") == -1 && Orderflag != null) {

				sharetome = lsprotocolordersharetoRepository
						.findBySharetounifiedidAndOrderflagAndSharestatusAndSharedonBetweenOrderBySharetoprotocolordercodeDesc(
								(String) objusers.get("Unifieduserid"), Orderflag, 1, fromdate, todate);
			} else if ((Integer) objusers.get("protocoltype") != -1 && Orderflag == null) {
				sharetome = lsprotocolordersharetoRepository
						.findBySharetounifiedidAndProtocoltypeAndSharestatusAndSharedonBetweenOrderBySharetoprotocolordercodeDesc(
								(String) objusers.get("Unifieduserid"), (Integer) objusers.get("protocoltype"), 1,
								fromdate, todate);
			}
			if (sharetome.size() > 0) {
				sharetome.forEach(
						objorderDetail -> objorderDetail.getProtocolorders().setLstelnprotocolworkflow(workflow));
				mapuserorders.put("sharetome", sharetome);
			}

		}
		List<Long> protocolordercodesharebyme = new ArrayList<>();
		List<Long> protocolordercodesharetome = new ArrayList<>();

		if (mapuserorders.size() > 0 && objusers.containsKey("searchCriteriaType")) {
			if (objusers.get("filefor").equals("OSBM")) {
				if (sharebyme.size() > 0) {
					protocolordercodesharebyme = sharebyme.stream()
							.map(Lsprotocolordersharedby::getShareprotocolordercode).collect(Collectors.toList());
					mapuserorders.put("protocolordercodeslist", protocolordercodesharebyme);

				}
			} else {
				if (sharetome.size() > 0) {
					protocolordercodesharetome = sharetome.stream()
							.map(Lsprotocolordershareto::getShareprotocolordercode).collect(Collectors.toList());
					mapuserorders.put("protocolordercodeslist", protocolordercodesharetome);

				}

			}

		}
		mapuserorders.put("directorycode", objusers.get("directorycode"));
		mapper = null;
		return mapuserorders;

	}

	public Map<String, Object> impoertjsonforprotocol(LSprotocolstep[] argObj1) {
		List<LSprotocolstep> argObj = Arrays.asList(argObj1);
		List<LSprotocolstep> rtnobj = new ArrayList<LSprotocolstep>();
		LSprotocolmaster protocolMaster = argObj.get(0).getLsprotocolmaster();
		Map<String, Object> mapObj = new HashMap<String, Object>();
//		LSprotocolstepversion protoVersStep = new LSprotocolstepversion();
//		CloudLSprotocolversionstep cloudStepVersion = new CloudLSprotocolversionstep();
		Boolean isversion = argObj.get(0).getIsversion();
		LSprotocolversionstepInfo LsLogilabprotocolstepInfoObj = new LSprotocolversionstepInfo();
		for (LSprotocolstep LSprotocolstepObj1 : argObj) {
			CloudLSprotocolversionstep cloudStepVersion = new CloudLSprotocolversionstep();
			LSprotocolstepversion protoVersStep = new LSprotocolstepversion();
			LSprotocolstep LSprotocolstep = new LSprotocolstep();
			LSprotocolstep.setProtocolmastercode(LSprotocolstepObj1.getProtocolmastercode());
			LSprotocolstep.setStepno(LSprotocolstepObj1.getStepno());
			LSprotocolstep.setProtocolstepname(LSprotocolstepObj1.getProtocolstepname());

			LSprotocolstep.setStatus(1);
			LSprotocolstep.setCreatedby(LSprotocolstepObj1.getCreatedby());
			LSprotocolstep.setCreatedbyusername(LSprotocolstepObj1.getCreatedbyusername());
			LSprotocolstep.setCreateddate(LSprotocolstepObj1.getCreateddate());
			LSprotocolstep.setSitecode(LSprotocolstepObj1.getSitecode());
			LSprotocolstep.setNewStep(1);
			LSProtocolStepRepositoryObj.save(LSprotocolstep);

			if ((LSprotocolstepObj1.getIsmultitenant() == 1 || LSprotocolstepObj1.getIsmultitenant() == 2)
					&& LSprotocolstepObj1.getLsprotocolstepInformation() != null) {
				LSprotocolstepInformation CloudLSprotocolstepInfoforinsert = new LSprotocolstepInformation();
				CloudLSprotocolstepInfoforinsert.setId(LSprotocolstep.getProtocolstepcode());
				CloudLSprotocolstepInfoforinsert
						.setLsprotocolstepInfo(LSprotocolstepObj1.getLsprotocolstepInformation());
				lsprotocolstepInformationRepository.save(CloudLSprotocolstepInfoforinsert);
				LSprotocolstep.setLsprotocolstepInformation(LSprotocolstepObj1.getLsprotocolstepInformation());
			} else if (LSprotocolstepObj1.getLsprotocolstepInformation() != null
					&& LSprotocolstepObj1.getIsmultitenant() == 0) {

				Query query = new Query(Criteria.where("id").is(LSprotocolstep.getProtocolstepcode()));
				Update update = new Update();
				update.set("content", LSprotocolstepObj1.getLsprotocolstepInformation());

				mongoTemplate.upsert(query, update, LSprotocolstepInfo.class);
				LSprotocolstep.setLsprotocolstepInformation(LSprotocolstepObj1.getLsprotocolstepInformation());
			}

			if (!isversion) {
				protoVersStep.setProtocolmastercode(LSprotocolstep.getProtocolmastercode());
				protoVersStep.setProtocolstepcode(LSprotocolstep.getProtocolstepcode());
				protoVersStep.setProtocolstepname(LSprotocolstep.getProtocolstepname());
				protoVersStep.setStatus(LSprotocolstep.getStatus());
				protoVersStep.setStepno(LSprotocolstep.getStepno());
				protoVersStep.setVersionno(protocolMaster.getVersionno());

				LSprotocolstepversionRepository.save(protoVersStep);

				if (LSprotocolstepObj1.getIsmultitenant() == 1 || LSprotocolstepObj1.getIsmultitenant() == 2) {
					cloudStepVersion.setId(protoVersStep.getProtocolstepversioncode());
					cloudStepVersion.setProtocolmastercode(LSprotocolstep.getProtocolmastercode());
					cloudStepVersion.setLsprotocolstepInfo(LSprotocolstepObj1.getLsprotocolstepInformation());
					cloudStepVersion.setVersionname("version_" + protocolMaster.getVersionno());
					cloudStepVersion.setVersionno(protocolMaster.getVersionno());

					CloudLSprotocolversionstepRepository.save(cloudStepVersion);
				} else {

					LsLogilabprotocolstepInfoObj.setContent(LSprotocolstepObj1.getLsprotocolstepInformation());
					LsLogilabprotocolstepInfoObj.setId(protoVersStep.getProtocolstepversioncode());
					LsLogilabprotocolstepInfoObj.setStepcode(LSprotocolstep.getProtocolstepcode());
					LsLogilabprotocolstepInfoObj.setVersionno(protocolMaster.getVersionno());
					mongoTemplate.insert(LsLogilabprotocolstepInfoObj);
				}
			}
			cloudStepVersion = null;
			protoVersStep = null;
			rtnobj.add(LSprotocolstep);
		}

		if (isversion) {
			if (argObj.get(0).getIsmultitenant() == 1 || argObj.get(0).getIsmultitenant() == 2) {
				try {
					updateCloudProtocolVersion(protocolMaster.getProtocolmastercode(), 0, null, 0,
							argObj.get(0).getSitecode(), null, argObj.get(0).getCreatedbyusername(),
							argObj.get(0).getCreatedby(), null);
				} catch (Exception e) {
					// TODO: handle exception
				}

			} else {
				LSprotocolstep dummyobj = new LSprotocolstep();
				dummyobj.setProtocolstepcode(0);
				dummyobj.setNewStep(0);
				try {
					updateCloudProtocolVersiononSQL(dummyobj, argObj.get(0).getSitecode(),
							argObj.get(0).getCreatedbyusername(), argObj.get(0).getCreatedby());

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}
		mapObj.put("protocolmaster",
				LSProtocolMasterRepositoryObj.findByprotocolmastercode(protocolMaster.getProtocolmastercode()));
		mapObj.put("LSprotocolstep", rtnobj);
		mapObj.put("LSprotocolversionlst",
				lsprotocolversionRepository.findByprotocolmastercode(protocolMaster.getProtocolmastercode()));

		return mapObj;
	}

	public Map<String, Object> deleteprotocolstepversion(LSprotocolstep body) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		if (body.getIsmultitenant() == 1 || body.getIsmultitenant() == 2) {
			try {
				updateCloudProtocolVersion(body.getProtocolmastercode(), body.getProtocolstepcode(), null, 0,
						body.getSitecode(), null, body.getCreatedbyusername(), body.getCreatedby(), null);

			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			try {
				updateCloudProtocolVersiononSQL(body, body.getSitecode(), body.getCreatedbyusername(),
						body.getCreatedby());

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		List<LSprotocolversion> LSprotocolversionlst = lsprotocolversionRepository
				.findByprotocolmastercode(body.getProtocolmastercode());

		Collections.sort(LSprotocolversionlst, Collections.reverseOrder());

		LSprotocolmaster protocolmaster = LSProtocolMasterRepositoryObj
				.findByprotocolmastercode(body.getProtocolmastercode());
		mapObj.put("protocolmaster", protocolmaster);

		mapObj.put("LSprotocolversionlst", LSprotocolversionlst);
		return mapObj;
	}

	public LSlogilabprotocoldetail cancelprotocolorder(LSlogilabprotocoldetail body) {

		LSlogilabprotocoldetail obj = LSlogilabprotocoldetailRepository
				.findByProtocolordercode(body.getProtocolordercode());
		obj.setOrdercancell(body.getOrdercancell());
		LSlogilabprotocoldetailRepository.save(obj);
		// Dashboard lsactivewidgets update
		Number protocolordercode = body.getProtocolordercode();
		lsActiveWidgetsRepository.updateRetirestatus(protocolordercode);

		return body;
	}

	public List<LSprotocolorderstephistory> getprotocolstephistory(LSprotocolorderstephistory objuser) {
		if (objuser.getProtocolordercode() != null) {
			List<LSprotocolorderstephistory> rtobj = lsprotocolorderstephistoryRepository
					.findByProtocolordercodeAndStepstartdateBetweenOrderByProtocolorderstephistorycodeDesc(
							objuser.getProtocolordercode(), objuser.getFromdate(), objuser.getTodate());
			rtobj.addAll(lsprotocolorderstephistoryRepository
					.findByProtocolordercodeAndStependdateBetweenOrderByProtocolorderstephistorycodeDesc(
							objuser.getProtocolordercode(), objuser.getFromdate(), objuser.getTodate()));

			return rtobj;
		} else if (objuser.getBatchcode() != null) {
			List<LSprotocolorderstephistory> rtobj = lsprotocolorderstephistoryRepository
					.findByBatchcode(objuser.getBatchcode());
			return rtobj;
		}
		return null;
	}

	public LSprotocolupdates updatetProtocolTemplateTransaction(LSprotocolupdates objuser) throws ParseException {
		if (objuser.getProtocolmastercode() != null) {
			if (objuser.getProtocolmodifiedDate() != null) {
				objuser.setProtocolmodifiedDate(commonfunction.getCurrentUtcTime());
			}
			lsprotocolupdatesRepository.save(objuser);
		}
		return objuser;
	}

	public LSprotocolorderstephistory updatetransactionhistory(LSprotocolorderstephistory objuser)
			throws ParseException {
		if (objuser.getProtocolordercode() != null || objuser.getBatchcode() != null) {
			if (objuser.getStepstartdate() != null) {
				objuser.setStepstartdate(commonfunction.getCurrentUtcTime());
			} else if (objuser.getStepskipeddate() != null) {
				objuser.setStepskipeddate(commonfunction.getCurrentUtcTime());
			} else if (objuser.getStependdate() != null) {
				objuser.setStependdate(commonfunction.getCurrentUtcTime());
			}
			lsprotocolorderstephistoryRepository.save(objuser);

//			if(objuser.getBatchcode() != null) {
//				if(objuser.getAction().contains("Processed")||objuser.getAction().contains("Opened")) {
//					LsActiveWidgets lsActiveWidgets = new LsActiveWidgets();
//					lsActiveWidgets.setActivewidgetsdetails(objuser.getBatchid());
//					lsActiveWidgets.setActivewidgetsdetailscode(objuser.getBatchcode());
//					lsActiveWidgets.setActivityType("Open");
//					lsActiveWidgets.setScreenname("Sheet_Order");
//					lsActiveWidgets.setUserId(objuser.getCreateby().getUsercode());
//					lsActiveWidgets.setActivedatatimestamp(commonfunction.getCurrentUtcTime());
//					lsActiveWidgetsRepository.save(lsActiveWidgets);
//				}
//			}
		}
		return objuser;
	}

	public List<LSprotocolorderstephistory> updateprotocolordertransactions(LSprotocolorderstephistory[] mapObj) {
		List<LSprotocolorderstephistory> obj = Arrays.asList(mapObj);
		lsprotocolorderstephistoryRepository.save(obj);
		return obj;
	}

	public Map<String, Object> getsingleprotocol(Map<String, Object> objuser) {
		Map<String, Object> Returntemplate_Objects = new HashMap<>();
		if (objuser.get("activekey") != null && (Integer) objuser.get("activekey") == 2) {
			Returntemplate_Objects.put("Lsprotocolsharedby",
					LsprotocolsharedbyRepository.findByShareprotocolcodeAndSharestatusOrderBySharedbytoprotocolcodeDesc(
							((Number) objuser.get("protocolmastercode")).longValue(), 1));
		} else if (objuser.get("activekey") != null && (Integer) objuser.get("activekey") == 3) {
			Returntemplate_Objects.put("Lsprotocolshareto",
					LsprotocolsharetoRepository.findByShareprotocolcodeAndSharestatusOrderBySharetoprotocolcodeDesc(
							((Number) objuser.get("protocolmastercode")).longValue(), 1));
		} else {
			Returntemplate_Objects.put("LSprotocolmaster", LSProtocolMasterRepositoryObj
					.findByProtocolmastercode((Integer) objuser.get("protocolmastercode")));
		}

		return Returntemplate_Objects;
	}

	public Map<String, Object> insertlinkimages(Map<String, Object> obj) throws IOException {
		Map<String, Object> imagelist = new HashMap<String, Object>();
		Integer onTabKey = (Integer) obj.get("ontabkey");
		ObjectMapper objectmap = new ObjectMapper();
		List<String> fileIds = new ArrayList<String>();
		String destinationContainerName = (String) obj.get("Tenantname") + "protocolimages";
		Integer isMultitenant = (Integer) obj.get("isMultitenant");
		String sourceContainerName = "";
		LSprotocolstep protocol = objectmap.convertValue(obj.get("LSprotocolstep"),
				new TypeReference<LSprotocolstep>() {
				});
		List<LSsheetfolderfiles> imgsheet = new ArrayList<LSsheetfolderfiles>();
		List<LSprotocolfolderfiles> imgprotocol = new ArrayList<LSprotocolfolderfiles>();
		String originurl = (String) obj.get("originurl");
		List<LSprotocolimages> listofimg = new ArrayList<>();
		if (onTabKey == 1) {
			imgsheet = objectmap.convertValue(obj.get("LSsheetfolderfiles"),
					new TypeReference<List<LSsheetfolderfiles>>() {
					});
			fileIds = imgsheet.stream().map(LSsheetfolderfiles::getUuid).collect(Collectors.toList());
			sourceContainerName = (String) obj.get("Tenantname") + "sheetfolderfiles";
		} else {
			imgprotocol = objectmap.convertValue(obj.get("LSprotocolfolderfiles"),
					new TypeReference<List<LSprotocolfolderfiles>>() {
					});
			fileIds = imgprotocol.stream().map(LSprotocolfolderfiles::getUuid).collect(Collectors.toList());
			sourceContainerName = (String) obj.get("Tenantname") + "protocolfolderfiles";
		}
		if (isMultitenant == 1 || isMultitenant == 2) {
			boolean isdone = cloudFileManipulationservice.tocopyoncontainertoanothercontainer(fileIds,
					sourceContainerName, destinationContainerName);
			if (isdone) {
				if (onTabKey == 1) {

					listofimg = imgsheet.stream().map(items -> {
						LSprotocolimages objimg = new LSprotocolimages();
						objimg.setExtension(FilenameUtils.getExtension(items.getFilename()));
						objimg.setFileid(items.getUuid());
						objimg.setProtocolmastercode(protocol.getProtocolmastercode());
						objimg.setProtocolstepcode(protocol.getProtocolstepcode());
						objimg.setProtocolstepname(protocol.getProtocolstepname());
						objimg.setStepno(protocol.getStepno());
						objimg.setIslinkimage(true);
						objimg.setFilename(FilenameUtils.removeExtension(items.getFilename()));
						String filename = "No Name";
						if (!objimg.getFilename().isEmpty()) {
							filename = objimg.getFilename();
						}
						String url = originurl + "/protocol/downloadprotocolimage/" + objimg.getFileid() + "/"
								+ TenantContext.getCurrentTenant() + "/" + filename + "/" + objimg.getExtension();
						Gson g = new Gson();
						String str = g.toJson(url);
						objimg.setSrc(str);
						return objimg;
					}).collect(Collectors.toList());

					lsprotocolimagesRepository.save(listofimg);
				} else {
					listofimg = imgprotocol.stream().map(items -> {
						LSprotocolimages objimg = new LSprotocolimages();
						objimg.setExtension(FilenameUtils.getExtension(items.getFilename()));
						objimg.setFileid(items.getUuid());
						objimg.setProtocolmastercode(protocol.getProtocolmastercode());
						objimg.setProtocolstepcode(protocol.getProtocolstepcode());
						objimg.setProtocolstepname(protocol.getProtocolstepname());
						objimg.setStepno(protocol.getStepno());
						objimg.setIslinkimage(true);
						objimg.setFilename(FilenameUtils.removeExtension(items.getFilename()));
						String filename = "No Name";
						if (!objimg.getFilename().isEmpty()) {
							filename = objimg.getFilename();
						}
						String url = originurl + "/protocol/downloadprotocolimage/" + objimg.getFileid() + "/"
								+ TenantContext.getCurrentTenant() + "/" + filename + "/" + objimg.getExtension();
						Gson g = new Gson();
						String str = g.toJson(url);
						objimg.setSrc(str);
						return objimg;
					}).collect(Collectors.toList());

					lsprotocolimagesRepository.save(listofimg);
				}

			}
		} else {
			List<GridFSDBFile> gridFsFile = fileManipulationservice.retrieveLargeFileinlist(fileIds);
			List<ProtocolImage> protocolImage = new ArrayList<>();
			listofimg = gridFsFile.parallelStream().map(items -> {
				String fieldId = Generatetenantpassword();
				LSprotocolimages objImg = new LSprotocolimages();
				String filename = "";
				if (onTabKey == 1) {
					LSsheetfolderfiles LSsheetfolderfiles = lssheetfolderfilesRepository
							.findByUuid(items.getFilename());
					filename = LSsheetfolderfiles.getFilename() != null ? LSsheetfolderfiles.getFilename() : "No Name";
				} else {
					LSprotocolfolderfiles LSprotocolfolderfiles = lsprotocolfolderfilesRepository
							.findByUuid(items.getFilename());
					filename = LSprotocolfolderfiles.getFilename() != null ? LSprotocolfolderfiles.getFilename()
							: "No Name";
				}
				objImg.setExtension(FilenameUtils.getExtension(filename));
				objImg.setFileid(fieldId);
				objImg.setProtocolmastercode(protocol.getProtocolmastercode());
				objImg.setProtocolstepcode(protocol.getProtocolstepcode());
				objImg.setProtocolstepname(protocol.getProtocolstepname());
				objImg.setStepno(protocol.getStepno());
				objImg.setIslinkimage(true);
				objImg.setFilename(FilenameUtils.removeExtension(filename));

//				String filename = objImg.getFilename() != null ? objImg.getFilename() : "No Name";
				String url = originurl + "/protocol/downloadprotocolimagesql/" + objImg.getFileid() + "/" + filename
						+ "/" + objImg.getExtension();
				String str = new Gson().toJson(url);
				objImg.setSrc(str);
				lsprotocolimagesRepository.save(objImg);
				ProtocolImage protocolImageObj = new ProtocolImage();
				protocolImageObj.setId(objImg.getProtocolstepimagecode());
				protocolImageObj.setName(filename);
				protocolImageObj.setFileid(fieldId);
				try {
					byte[] imageBytes = IOUtils.toByteArray(items.getInputStream());
					Binary binaryImage = new Binary(BsonBinarySubType.BINARY, imageBytes);
					protocolImageObj.setImage(binaryImage);
				} catch (IOException e) {
					e.printStackTrace();
				}
				protocolImage.add(protocolImageObj);
				return objImg;
			}).collect(Collectors.toList());
			if (!protocolImage.isEmpty()) {
				protocolImageRepository.insert(protocolImage);
			}

		}
		imagelist.put("listofimg", listofimg);
		return imagelist;
	}

	public Map<String, Object> insertlinkfiles(Map<String, Object> obj) throws IOException {
		Map<String, Object> imagelist = new HashMap<String, Object>();
		Integer onTabKey = (Integer) obj.get("ontabkey");
		ObjectMapper objectmap = new ObjectMapper();
		List<String> fileIds = new ArrayList<String>();
		String destinationContainerName = (String) obj.get("Tenantname") + "protocolfiles";
		Integer isMultitenant = (Integer) obj.get("isMultitenant");
		String sourceContainerName = "";
		LSprotocolstep protocol = objectmap.convertValue(obj.get("LSprotocolstep"),
				new TypeReference<LSprotocolstep>() {
				});
		List<LSsheetfolderfiles> imgsheet = new ArrayList<LSsheetfolderfiles>();
		List<LSprotocolfolderfiles> imgprotocol = new ArrayList<LSprotocolfolderfiles>();
		String originurl = (String) obj.get("originurl");
		List<LSprotocolfiles> listofimg = new ArrayList<>();
		if (onTabKey == 1) {
			imgsheet = objectmap.convertValue(obj.get("LSsheetfolderfiles"),
					new TypeReference<List<LSsheetfolderfiles>>() {
					});
			fileIds = imgsheet.stream().map(LSsheetfolderfiles::getUuid).collect(Collectors.toList());
			sourceContainerName = (String) obj.get("Tenantname") + "sheetfolderfiles";
		} else {
			imgprotocol = objectmap.convertValue(obj.get("LSprotocolfolderfiles"),
					new TypeReference<List<LSprotocolfolderfiles>>() {
					});
			fileIds = imgprotocol.stream().map(LSprotocolfolderfiles::getUuid).collect(Collectors.toList());
			sourceContainerName = (String) obj.get("Tenantname") + "protocolfolderfiles";
		}
		if (isMultitenant == 1 || isMultitenant == 2) {
			boolean isdone = cloudFileManipulationservice.tocopyoncontainertoanothercontainer(fileIds,
					sourceContainerName, destinationContainerName);
			if (isdone) {
				if (onTabKey == 1) {

					listofimg = imgsheet.stream().map(items -> {
						LSprotocolfiles objimg = new LSprotocolfiles();
						objimg.setExtension(FilenameUtils.getExtension(items.getFilename()));
						objimg.setFileid(items.getUuid());
						objimg.setProtocolmastercode(protocol.getProtocolmastercode());
						objimg.setProtocolstepcode(protocol.getProtocolstepcode());
						objimg.setProtocolstepname(protocol.getProtocolstepname());
						objimg.setStepno(protocol.getStepno());
						objimg.setIslinkfile(true);
						objimg.setFilename(FilenameUtils.removeExtension(items.getFilename()));
						String filename = "No Name";
						if (!objimg.getFilename().isEmpty()) {
							filename = objimg.getFilename();
						}
						String url = originurl + "/protocol/downloadprotocolfile/" + objimg.getFileid() + "/"
								+ TenantContext.getCurrentTenant() + "/" + filename + "/" + objimg.getExtension();
						objimg.setLink(url);
						return objimg;
					}).collect(Collectors.toList());

					lsprotocolfilesRepository.save(listofimg);
				} else {
					listofimg = imgprotocol.stream().map(items -> {
						LSprotocolfiles objimg = new LSprotocolfiles();
						objimg.setExtension(FilenameUtils.getExtension(items.getFilename()));
						objimg.setFileid(items.getUuid());
						objimg.setProtocolmastercode(protocol.getProtocolmastercode());
						objimg.setProtocolstepcode(protocol.getProtocolstepcode());
						objimg.setProtocolstepname(protocol.getProtocolstepname());
						objimg.setStepno(protocol.getStepno());
						objimg.setIslinkfile(true);
						objimg.setFilename(FilenameUtils.removeExtension(items.getFilename()));
						String filename = "No Name";
						if (!objimg.getFilename().isEmpty()) {
							filename = objimg.getFilename();
						}
						String url = originurl + "/protocol/downloadprotocolfile/" + objimg.getFileid() + "/"
								+ TenantContext.getCurrentTenant() + "/" + filename + "/" + objimg.getExtension();
						objimg.setLink(url);
						return objimg;
					}).collect(Collectors.toList());

					lsprotocolfilesRepository.save(listofimg);
				}

			}
		} else {

			List<GridFSDBFile> gridFsFile = fileManipulationservice.retrieveLargeFileinlist(fileIds);
			List<LSprotocolfilesContent> protocolImage = new ArrayList<>();
			listofimg = gridFsFile.parallelStream().map(items -> {
				String fieldId = Generatetenantpassword();
				LSprotocolfiles objImg = new LSprotocolfiles();
				String filename = "";
				if (onTabKey == 1) {
					LSsheetfolderfiles LSsheetfolderfiles = lssheetfolderfilesRepository
							.findByUuid(items.getFilename());
					filename = LSsheetfolderfiles.getFilename() != null ? LSsheetfolderfiles.getFilename() : "No Name";
				} else {
					LSprotocolfolderfiles LSprotocolfolderfiles = lsprotocolfolderfilesRepository
							.findByUuid(items.getFilename());
					filename = LSprotocolfolderfiles.getFilename() != null ? LSprotocolfolderfiles.getFilename()
							: "No Name";
				}
				objImg.setExtension(FilenameUtils.getExtension(filename));
				objImg.setFileid(fieldId);
				objImg.setProtocolmastercode(protocol.getProtocolmastercode());
				objImg.setProtocolstepcode(protocol.getProtocolstepcode());
				objImg.setProtocolstepname(protocol.getProtocolstepname());
				objImg.setStepno(protocol.getStepno());
				objImg.setIslinkfile(true);
				objImg.setFilename(FilenameUtils.removeExtension(filename));

//				String filename = objImg.getFilename() != null ? objImg.getFilename() : "No Name";
				String url = originurl + "/protocol/downloadprotocolfilesql/" + objImg.getFileid() + "/" + filename
						+ "/" + objImg.getExtension();
				objImg.setLink(originurl);

				lsprotocolfilesRepository.save(objImg);
				LSprotocolfilesContent objattachment = new LSprotocolfilesContent();
				objattachment.setId(objImg.getProtocolstepfilecode());
				objattachment.setName(filename);
				objattachment.setFileid(fieldId);
				try {
					byte[] imageBytes = IOUtils.toByteArray(items.getInputStream());
					Binary binaryImage = new Binary(BsonBinarySubType.BINARY, imageBytes);
					objattachment.setFile(binaryImage);
				} catch (IOException e) {
					e.printStackTrace();
				}
				protocolImage.add(objattachment);
				return objImg;
			}).collect(Collectors.toList());
			if (!protocolImage.isEmpty()) {
				lsprotocolfilesContentRepository.insert(protocolImage);
			}
		}
		imagelist.put("listoffile", listofimg);
		return imagelist;
	}

	public Map<String, Object> onStartProtocolOrder(Map<String, Object> body) throws IOException {

		Map<String, Object> mapObj = new HashMap<String, Object>();
		Response response = new Response();
		ObjectMapper object = new ObjectMapper();

		if (body.get("ProtocolOrder") != null) {
			LSlogilabprotocoldetail lSlogilabprotocoldetail = object.convertValue(body.get("ProtocolOrder"),
					LSlogilabprotocoldetail.class);
			LSlogilabprotocoldetail protocolOrder = LSlogilabprotocoldetailRepository
					.findByProtocolordercode(lSlogilabprotocoldetail.getProtocolordercode());

			protocolOrder.setOrderstarted(1);
			protocolOrder.setOrderstartedby(
					lsusermasterRepository.findByusercode(object.convertValue(body.get("usercode"), Integer.class)));
			try {
				protocolOrder.setOrderstartedon(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LSlogilabprotocoldetailRepository.save(protocolOrder);
			Logilabprotocolorders lsprotocol = LSlogilabprotocoldetailRepository
					.findByProtocolordercodeOrderByProtocolordercodeDesc(protocolOrder.getProtocolordercode());
			mapObj.put("protocolOrder", lsprotocol);
			response.setInformation("IDS_MSG_PROTOCOLORDERSTART");
			response.setStatus(true);
		}

		mapObj.put("response", response);
		return mapObj;
	}

	public Map<String, Object> getProtocolTemplateVersionChanges(Map<String, Object> argObj) throws IOException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		ObjectMapper object = new ObjectMapper();

		LSprotocolversion versionMaster = object.convertValue(argObj.get("Lsprotocolversion"), LSprotocolversion.class);
		int multitenent = object.convertValue(argObj.get("ismultitenant"), Integer.class);
		LSprotocolmaster protocol = lsProtocolMasterRepository
				.findFirstByProtocolmastercode(versionMaster.getProtocolmastercode());
		if (multitenent == 1 || multitenent == 2) {
			String tenant = TenantContext.getCurrentTenant();
			if (multitenent == 2) {
				tenant = "freeusers";
			}
			if (versionMaster.getFileuid() != null) {
				mapObj.put("ProtocolData", objCloudFileManipulationservice
						.retrieveCloudSheets(versionMaster.getFileuid(), tenant + "protocolversion"));
			} else {
				mapObj.put("ProtocolData", objCloudFileManipulationservice.retrieveCloudSheets(protocol.getFileuid(),
						tenant + "protocol"));
			}
		} else {
//			Lsprotocoltemplateversiondata lsprotocolversiondata = mongoTemplate
//			.findById(versionMaster.getProtocolversioncode(), Lsprotocoltemplateversiondata.class);
//			if(lsprotocolversiondata != null) {
//				mapObj.put("ProtocolData", lsprotocolversiondata.getContent());
//			} else {
			GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(
					"protocol_" + versionMaster.getProtocolmastercode() + "version_" + versionMaster.getVersionno())));
			if (largefile != null) {
				mapObj.put("ProtocolData",
						new BufferedReader(new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8))
								.lines().collect(Collectors.joining("\n")));
			} else {
//					Lsprotocoltemplatedata lsprotocoldata = mongoTemplate
//							.findById(protocol.getProtocolmastercode(), Lsprotocoltemplatedata.class);
//					if(lsprotocoldata != null) {
//						mapObj.put("ProtocolData", lsprotocoldata.getContent());
//					} else {
				GridFSDBFile largefile1 = gridFsTemplate.findOne(
						new Query(Criteria.where("filename").is("protocol_" + versionMaster.getProtocolmastercode())));
				mapObj.put("ProtocolData",
						new BufferedReader(new InputStreamReader(largefile1.getInputStream(), StandardCharsets.UTF_8))
								.lines().collect(Collectors.joining("\n")));
//					}
			}
//			}
		}

		return mapObj;
	}

	public Map<String, Object> getProtocolOrderVersionChanges(Map<String, Object> argObj) throws IOException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		ObjectMapper object = new ObjectMapper();

		LSprotocolorderversion versionMaster = object.convertValue(argObj.get("Lsprotocolorderversion"),
				LSprotocolorderversion.class);
		int multitenent = object.convertValue(argObj.get("ismultitenant"), Integer.class);
		LSlogilabprotocoldetail protocol = LSlogilabprotocoldetailRepository
				.findByProtocolordercode(versionMaster.getProtocolordercode());
		if (multitenent == 1 || multitenent == 2) {
			if (versionMaster.getFileuid() != null) {
				mapObj.put("ProtocolData",
						objCloudFileManipulationservice.retrieveCloudSheets(versionMaster.getFileuid(),
								commonfunction.getcontainername(multitenent, TenantContext.getCurrentTenant())
										+ "protocolorderversion"));
			} else {
				mapObj.put("ProtocolData",
						objCloudFileManipulationservice.retrieveCloudSheets(protocol.getFileuid(),
								commonfunction.getcontainername(multitenent, TenantContext.getCurrentTenant())
										+ "protocolorder"));
			}
		} else {
//			Lsprotocolorderversiondata lsprotocolversiondata = mongoTemplate
//			.findById(versionMaster.getProtocolorderversioncode(), Lsprotocolorderversiondata.class);
//			if(lsprotocolversiondata != null) {
//				mapObj.put("ProtocolData", lsprotocolversiondata.getContent());
//			} else {
//				Lsprotocolorderdata lsprotocoldata = mongoTemplate
//						.findById(protocol.getProtocolordercode(), Lsprotocolorderdata.class);
//				mapObj.put("ProtocolData", lsprotocoldata.getContent());
//			}

//			GridFSDBFile largefile = gridFsTemplate.findOne(
//					new Query(Criteria.where("filename").is("protocol_" + versionMaster.getProtocolordercode())));
//			mapObj.put("ProtocolData",
//					new BufferedReader(new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8))
//							.lines().collect(Collectors.joining("\n")));

			GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is("protocolorder_"
					+ versionMaster.getProtocolordercode() + "version_" + versionMaster.getVersionno())));
			if (largefile != null) {
				mapObj.put("ProtocolData",
						new BufferedReader(new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8))
								.lines().collect(Collectors.joining("\n")));
			} else {
				GridFSDBFile largefile1 = gridFsTemplate.findOne(new Query(
						Criteria.where("filename").is("protocolorder_" + versionMaster.getProtocolordercode())));
				mapObj.put("ProtocolData",
						new BufferedReader(new InputStreamReader(largefile1.getInputStream(), StandardCharsets.UTF_8))
								.lines().collect(Collectors.joining("\n")));
			}
		}

		return mapObj;
	}

	public Map<String, Object> getProtocolTemplateVersionsForCompare(Map<String, Object> argObj) throws IOException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		ObjectMapper object = new ObjectMapper();

		LSprotocolmaster protocol = lsProtocolMasterRepository
				.findFirstByProtocolmastercode(object.convertValue(argObj.get("protocolmastercode"), Integer.class));
		LSprotocolversion compare1 = lsprotocolversionRepository.findFirstByProtocolmastercodeAndVersionno(
				protocol.getProtocolmastercode(), object.convertValue(argObj.get("compare1"), Integer.class));
		LSprotocolversion compare2 = lsprotocolversionRepository.findFirstByProtocolmastercodeAndVersionno(
				protocol.getProtocolmastercode(), object.convertValue(argObj.get("compare2"), Integer.class));
		int multitenant = object.convertValue(argObj.get("ismultitenant"), Integer.class);
		if (multitenant == 1 || multitenant == 2) {
			mapObj.put("comparedata1", retrieveCloudSheets(compare1.getFileuid(), protocol.getFileuid(), "Template"));
			mapObj.put("comparedata2", retrieveCloudSheets(compare2.getFileuid(), protocol.getFileuid(), "Template"));
		} else {
			Lsprotocoltemplateversiondata lsprotocolversiondata1 = mongoTemplate
					.findById(compare1.getProtocolversioncode(), Lsprotocoltemplateversiondata.class);
			Lsprotocoltemplateversiondata lsprotocolversiondata2 = mongoTemplate
					.findById(compare2.getProtocolversioncode(), Lsprotocoltemplateversiondata.class);

//			if(lsprotocolversiondata1 != null) {
//				mapObj.put("comparedata1", lsprotocolversiondata1.getContent());				
//			} else {
//				Lsprotocoltemplatedata lsprotocoldata = mongoTemplate
//						.findById(protocol.getProtocolmastercode(), Lsprotocoltemplatedata.class);
//				mapObj.put("comparedata1", lsprotocoldata.getContent());
			GridFSDBFile largefile1 = gridFsTemplate.findOne(new Query(Criteria.where("filename")
					.is("protocol_" + protocol.getProtocolmastercode() + "version_" + compare1.getVersionno())));
			if (largefile1 != null) {
				mapObj.put("comparedata1",
						new BufferedReader(new InputStreamReader(largefile1.getInputStream(), StandardCharsets.UTF_8))
								.lines().collect(Collectors.joining("\n")));
			} else {
//					Lsprotocoltemplatedata lsprotocoldata = mongoTemplate
//							.findById(protocol.getProtocolmastercode(), Lsprotocoltemplatedata.class);
//					if(lsprotocoldata != null) {
//						mapObj.put("comparedata1", lsprotocoldata.getContent());
//					} else {
				GridFSDBFile largefile2 = gridFsTemplate.findOne(
						new Query(Criteria.where("filename").is("protocol_" + compare1.getProtocolmastercode())));
				mapObj.put("comparedata1",
						new BufferedReader(new InputStreamReader(largefile2.getInputStream(), StandardCharsets.UTF_8))
								.lines().collect(Collectors.joining("\n")));
//					}
			}
//			}

//			if(lsprotocolversiondata2 != null) {
//				mapObj.put("comparedata2", lsprotocolversiondata2.getContent());
//			} else {
//				Lsprotocoltemplatedata lsprotocoldata = mongoTemplate
//						.findById(protocol.getProtocolmastercode(), Lsprotocoltemplatedata.class);
//				mapObj.put("comparedata2", lsprotocoldata.getContent());
			GridFSDBFile largefile3 = gridFsTemplate.findOne(new Query(Criteria.where("filename")
					.is("protocol_" + protocol.getProtocolmastercode() + "version_" + compare2.getVersionno())));
			if (largefile3 != null) {
				mapObj.put("comparedata2",
						new BufferedReader(new InputStreamReader(largefile3.getInputStream(), StandardCharsets.UTF_8))
								.lines().collect(Collectors.joining("\n")));
			} else {
//					Lsprotocoltemplatedata lsprotocoldata = mongoTemplate
//							.findById(protocol.getProtocolmastercode(), Lsprotocoltemplatedata.class);
//					if(lsprotocoldata != null) {
//						mapObj.put("comparedata2", lsprotocoldata.getContent());
//					} else {
				GridFSDBFile largefile4 = gridFsTemplate.findOne(
						new Query(Criteria.where("filename").is("protocol_" + compare2.getProtocolmastercode())));
				mapObj.put("comparedata2",
						new BufferedReader(new InputStreamReader(largefile4.getInputStream(), StandardCharsets.UTF_8))
								.lines().collect(Collectors.joining("\n")));
//					}
			}
//			}

//			GridFSDBFile largefile = gridFsTemplate
//					.findOne(new Query(Criteria.where("filename").is("protocol_" + protocol.getProtocolmastercode())));
//			mapObj.put("ProtocolData",
//					new BufferedReader(new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8))
//							.lines().collect(Collectors.joining("\n")));
		}

		return mapObj;
	}

	public Map<String, Object> getProtocolOrderVersionsForCompare(Map<String, Object> argObj) throws IOException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		ObjectMapper object = new ObjectMapper();

		LSlogilabprotocoldetail protocol = LSlogilabprotocoldetailRepository
				.findByProtocolordercode(object.convertValue(argObj.get("protocolordercode"), Long.class));
		LSprotocolorderversion compare1 = lsprotocolorderversionRepository.findFirstByProtocolordercodeAndVersionno(
				protocol.getProtocolordercode(), object.convertValue(argObj.get("compare1"), Integer.class));
		LSprotocolorderversion compare2 = lsprotocolorderversionRepository.findFirstByProtocolordercodeAndVersionno(
				protocol.getProtocolordercode(), object.convertValue(argObj.get("compare2"), Integer.class));
		int multitenant = object.convertValue(argObj.get("ismultitenant"), Integer.class);
		if (multitenant == 1) {
			mapObj.put("comparedata1", retrieveCloudSheets(compare1.getFileuid(), protocol.getFileuid(), "Order"));
			mapObj.put("comparedata2", retrieveCloudSheets(compare2.getFileuid(), protocol.getFileuid(), "Order"));
		} else {
//			Lsprotocolorderversiondata lsprotocolversiondata1 = mongoTemplate
//			.findById(compare1.getProtocolorderversioncode(), Lsprotocolorderversiondata.class);
//			Lsprotocolorderversiondata lsprotocolversiondata2 = mongoTemplate
//					.findById(compare2.getProtocolorderversioncode(), Lsprotocolorderversiondata.class);
//			
//			if(lsprotocolversiondata1 != null) {
//				mapObj.put("comparedata1", lsprotocolversiondata1.getContent());				
//			} else {
//				Lsprotocolorderdata lsprotocoldata = mongoTemplate
//						.findById(protocol.getProtocolordercode(), Lsprotocolorderdata.class);
//				mapObj.put("comparedata1", lsprotocoldata.getContent());
//			}
//			
//			if(lsprotocolversiondata2 != null) {
//				mapObj.put("comparedata2", lsprotocolversiondata2.getContent());
//			} else {
//				Lsprotocolorderdata lsprotocoldata = mongoTemplate
//						.findById(protocol.getProtocolordercode(), Lsprotocolorderdata.class);
//				mapObj.put("comparedata2", lsprotocoldata.getContent());
//			}

//			GridFSDBFile largefile = gridFsTemplate
//					.findOne(new Query(Criteria.where("filename").is("protocol_" + protocol.getProtocolordercode())));
//			mapObj.put("ProtocolData",
//					new BufferedReader(new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8))
//							.lines().collect(Collectors.joining("\n")));

			GridFSDBFile largefile1 = gridFsTemplate.findOne(new Query(Criteria.where("filename")
					.is("protocolorder_" + protocol.getProtocolordercode() + "version_" + compare1.getVersionno())));
			if (largefile1 != null) {
				mapObj.put("comparedata1",
						new BufferedReader(new InputStreamReader(largefile1.getInputStream(), StandardCharsets.UTF_8))
								.lines().collect(Collectors.joining("\n")));
			} else {
				GridFSDBFile largefile2 = gridFsTemplate.findOne(
						new Query(Criteria.where("filename").is("protocolorder_" + compare1.getProtocolordercode())));
				mapObj.put("comparedata1",
						new BufferedReader(new InputStreamReader(largefile2.getInputStream(), StandardCharsets.UTF_8))
								.lines().collect(Collectors.joining("\n")));
			}

			GridFSDBFile largefile3 = gridFsTemplate.findOne(new Query(Criteria.where("filename")
					.is("protocolorder_" + protocol.getProtocolordercode() + "version_" + compare2.getVersionno())));
			if (largefile3 != null) {
				mapObj.put("comparedata2",
						new BufferedReader(new InputStreamReader(largefile3.getInputStream(), StandardCharsets.UTF_8))
								.lines().collect(Collectors.joining("\n")));
			} else {
				GridFSDBFile largefile4 = gridFsTemplate.findOne(
						new Query(Criteria.where("filename").is("protocolorder_" + compare2.getProtocolordercode())));
				mapObj.put("comparedata2",
						new BufferedReader(new InputStreamReader(largefile4.getInputStream(), StandardCharsets.UTF_8))
								.lines().collect(Collectors.joining("\n")));
			}
		}

		return mapObj;
	}

	private Object retrieveCloudSheets(String fileUid, String defaultFileUid, String screen) throws IOException {
		String tenant = TenantContext.getCurrentTenant();
		if (tenant == null) {
			tenant = "freeusers";
		}
		if (screen.equals("Template")) {
			if (fileUid != null) {
				return objCloudFileManipulationservice.retrieveCloudSheets(fileUid, tenant + "protocolversion");
			} else {
				return objCloudFileManipulationservice.retrieveCloudSheets(defaultFileUid, tenant + "protocol");
			}
		} else {
			if (fileUid != null) {
				return objCloudFileManipulationservice.retrieveCloudSheets(fileUid, tenant + "protocolorderversion");
			} else {
				return objCloudFileManipulationservice.retrieveCloudSheets(defaultFileUid, tenant + "protocolorder");
			}
		}
	}

	public LSlogilabprotocoldetail LockUnlockprotocolorders(LSlogilabprotocoldetail protocolorders)
			throws ParseException {

		LSlogilabprotocoldetail rtnobj = LSlogilabprotocoldetailRepository
				.findOne(protocolorders.getProtocolordercode());
		
		SequenceTable seqobj =  sequenceTableRepository.findBySequencecodeOrderBySequencecode(2);
		Boolean Applicationseq = seqobj.getSequenceview().equals(2) ? true : false;
		
		if(Applicationseq) {
			rtnobj.setProtoclordername(rtnobj.getSequenceid());
		}
		
		if (!protocolorders.getIsmultitenant().equals(2) && (rtnobj != null && rtnobj.getLockeduser() == null)
				&& protocolorders.getComment().equals("Order_Lock")) {
			if (!protocolorders.getIsmultitenant().equals(2)) {
				rtnobj.setLockeduser(protocolorders.getLockeduser());
				rtnobj.setLockedusername(protocolorders.getLockedusername());
				rtnobj.setActiveuser(protocolorders.getActiveuser());
				rtnobj.setComment("Order_Lock");
			} else {
				rtnobj.setLockeduser(null);
				rtnobj.setLockedusername(null);
				rtnobj.setActiveuser(null);
				rtnobj.setComment("Order_Unlock");
			}
			LsActiveWidgets lsActiveWidgets = new LsActiveWidgets();
			lsActiveWidgets.setActivewidgetsdetails(rtnobj.getProtoclordername());
			lsActiveWidgets.setActivewidgetsdetailscode(rtnobj.getProtocolordercode());
			lsActiveWidgets.setActivityType("Open");
			lsActiveWidgets.setScreenname("Protocol_Order");
			lsActiveWidgets.setUserId(protocolorders.getLockeduser());
			lsActiveWidgets.setActivedatatimestamp(commonfunction.getCurrentUtcTime());
			lsActiveWidgetsRepository.save(lsActiveWidgets);
			LSlogilabprotocoldetailRepository.save(rtnobj);
		} else if (rtnobj.getLockeduser() != null && protocolorders.getLockeduser().equals(rtnobj.getLockeduser())
				&& protocolorders.getComment().equals("Order_Lock")) {
			rtnobj.setComment("IDS_SAME_USER_OPEN");
		} else if (protocolorders.getComment().equals("Order_Unlock")) {
			rtnobj.setLockeduser(null);
			rtnobj.setLockedusername(null);
			rtnobj.setActiveuser(null);
			LSlogilabprotocoldetailRepository.save(rtnobj);
			rtnobj.setComment("Order_Unlock");
		}
		return rtnobj;

	}

	public List<Logilabprotocolorders> GetUnlockscreendata(LSuserMaster objuser) {

		if (objuser.getUsername().equalsIgnoreCase("Administrator")) {
			return LSlogilabprotocoldetailRepository
//					.findByOrderflagAndLockeduserIsNotNullAndAssignedtoIsNullOrderByBatchcodeDesc("N");
					.findByOrderflagAndSitecodeAndAndLockeduserIsNotNullAndAssignedtoIsNullOrderByProtocolordercodeDesc(
							"N", objuser.getLssitemaster().getSitecode());
		} else {
			List<Logilabprotocolorders> lstorder = new ArrayList<Logilabprotocolorders>();
			List<Integer> userlist = objuser.getUsernotify() != null
					? objuser.getUsernotify().stream().map(LSuserMaster::getUsercode).collect(Collectors.toList())
					: new ArrayList<Integer>();
			lstorder = LSlogilabprotocoldetailRepository
					.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndLockeduserIsNotNullAndOrderflagAndLockeduserNotOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndLockeduserIsNotNullAndOrderflagAndLockeduserNotOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyInAndLockeduserIsNotNullAndOrderflagAndLockeduserNotOrderByProtocolordercodeDesc(
							1, objuser.getLssitemaster().getSitecode(), "N", objuser.getUsercode(), 2,
							objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), "N", objuser.getUsercode(),
							3, objuser.getLssitemaster().getSitecode(), userlist, "N", objuser.getUsercode());
			List<LSprojectmaster> lstproject = objuser.getLstproject();
			if (lstproject != null) {
				lstorder.addAll(LSlogilabprotocoldetailRepository
						.findByLsprojectmasterInAndViewoptionAndSitecodeAndLockeduserIsNotNullAndOrderflagAndLockeduserOrderByProtocolordercodeDesc(
								lstproject, 3, objuser.getLssitemaster().getSitecode(), "N", objuser.getUsercode()));

				lstorder.addAll(LSlogilabprotocoldetailRepository
						.findByLsprojectmasterInAndSitecodeAndLockeduserIsNotNullAndOrderflagOrderByProtocolordercodeDesc(
								lstproject, objuser.getLssitemaster().getSitecode(), "N"));

				return lstorder;
			}
			return lstorder;
		}

	}

	public Boolean Unloackprotocolorders(Long[] protocolorders) {

		if (protocolorders.length > 0) {
			List<Long> protocolorderscode = Arrays.asList(protocolorders);
			LSlogilabprotocoldetailRepository.Updatelockedusersonptocolorders(protocolorderscode);
			return true;
		} else {
			return false;
		}

	}
	
//	public Map<String, Object> Getprotocolcancelledorders(LSlogilabprotocoldetail objorder) {
//		Map<String, Object> retuobjts = new HashMap<>();
//		Integer protocoltype = objorder.getProtocoltype();
//		String Orderflag = null;
//		if (objorder.getOrderflag() != null) {
//			Orderflag = objorder.getOrderflag();
//		}
//		Integer reject = null;
//		if (objorder.getRejected() != null) {
//			reject = objorder.getRejected();
//		}
//		List<Logilabprotocolorders> lstorders = new ArrayList<Logilabprotocolorders>();
//		List<Integer> userlist = objorder.getLsuserMaster().getUsernotify() != null ? objorder.getLsuserMaster()
//				.getUsernotify().stream().map(LSuserMaster::getUsercode).collect(Collectors.toList())
//				: new ArrayList<Integer>();
//		if (protocoltype == -1 && Orderflag == null && reject == null) {
//			lstorders = LSlogilabprotocoldetailRepository
//					.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
//							1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 1, objorder.getFromdate(),
//							objorder.getTodate(), 1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 2,
//							objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(), objorder.getTodate(), 1,
//							objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3, objorder.getFromdate(),
//							objorder.getTodate(), userlist);
//
//			lstorders.addAll(LSlogilabprotocoldetailRepository
//					.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrderByProtocolordercodeDesc(
//							1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), objorder.getFromdate(),
//							objorder.getTodate(), objorder.getLstproject()));
//		} else if (protocoltype != -1 && Orderflag == null && reject == null) {
//			lstorders = LSlogilabprotocoldetailRepository
//					.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndProtocoltypeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtocoltypeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndProtocoltypeOrderByProtocolordercodeDesc(
//							1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 1, objorder.getFromdate(),
//							objorder.getTodate(), protocoltype, 1,
//							objorder.getLsuserMaster().getLssitemaster().getSitecode(), 2,
//							objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(), objorder.getTodate(),
//							protocoltype, 1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3,
//							objorder.getFromdate(), objorder.getTodate(), userlist, protocoltype);
//
//			lstorders.addAll(LSlogilabprotocoldetailRepository
//					.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndProtocoltypeOrderByProtocolordercodeDesc(
//							1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), objorder.getFromdate(),
//							objorder.getTodate(), objorder.getLstproject(), protocoltype));
//		} else if (protocoltype == -1 && Orderflag != null && reject == null) {
//			lstorders = LSlogilabprotocoldetailRepository
//					.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndOrderflagOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndOrderflagOrderByProtocolordercodeDesc(
//							1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 1, objorder.getFromdate(),
//							objorder.getTodate(), Orderflag, 1,
//							objorder.getLsuserMaster().getLssitemaster().getSitecode(), 2,
//							objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(), objorder.getTodate(),
//							Orderflag, 1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3,
//							objorder.getFromdate(), objorder.getTodate(), userlist, Orderflag);
//
//			lstorders.addAll(LSlogilabprotocoldetailRepository
//					.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrderflagOrderByProtocolordercodeDesc(
//							1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), objorder.getFromdate(),
//							objorder.getTodate(), objorder.getLstproject(), Orderflag));
//
//		} else if (protocoltype == -1 && Orderflag == null && reject != null) {
//			lstorders = LSlogilabprotocoldetailRepository
//					.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndRejectedOrderByProtocolordercodeDesc(
//							1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 1, objorder.getFromdate(),
//							objorder.getTodate(), 1, 1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 2,
//							objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(), objorder.getTodate(), 1,
//							1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3, objorder.getFromdate(),
//							objorder.getTodate(), userlist, 1);
//
//			lstorders.addAll(LSlogilabprotocoldetailRepository
//					.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedOrderByProtocolordercodeDesc(
//							1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), objorder.getFromdate(),
//							objorder.getTodate(), objorder.getLstproject(), 1));
//
//		} else if (protocoltype != -1 && Orderflag != null && reject == null) {
//			lstorders = LSlogilabprotocoldetailRepository
//					.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndProtocoltypeAndOrderflagOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtocoltypeAndOrderflagOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndProtocoltypeAndOrderflagOrderByProtocolordercodeDesc(
//							1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 1, objorder.getFromdate(),
//							objorder.getTodate(), protocoltype, Orderflag, 1,
//							objorder.getLsuserMaster().getLssitemaster().getSitecode(), 2,
//							objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(), objorder.getTodate(),
//							protocoltype, Orderflag, 1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3,
//							objorder.getFromdate(), objorder.getTodate(), userlist, protocoltype, Orderflag);
//
//			lstorders.addAll(LSlogilabprotocoldetailRepository
//					.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndProtocoltypeAndOrderflagOrderByProtocolordercodeDesc(
//							1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), objorder.getFromdate(),
//							objorder.getTodate(), objorder.getLstproject(), protocoltype, Orderflag));
//
//		} else if (protocoltype != -1 && Orderflag == null && reject != null) {
//			lstorders = LSlogilabprotocoldetailRepository
//					.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndProtocoltypeAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtocoltypeAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndProtocoltypeAndRejectedOrderByProtocolordercodeDesc(
//							1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 1, objorder.getFromdate(),
//							objorder.getTodate(), protocoltype, 1, 1,
//							objorder.getLsuserMaster().getLssitemaster().getSitecode(), 2,
//							objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(), objorder.getTodate(),
//							protocoltype, 1, 1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3,
//							objorder.getFromdate(), objorder.getTodate(), userlist, protocoltype, 1);
//
//			lstorders.addAll(LSlogilabprotocoldetailRepository
//					.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndProtocoltypeAndRejectedOrderByProtocolordercodeDesc(
//							1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), objorder.getFromdate(),
//							objorder.getTodate(), objorder.getLstproject(), protocoltype, 1));
//
//		} else if (protocoltype == -1 && Orderflag != null && reject != null) {
//			lstorders = LSlogilabprotocoldetailRepository
//					.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndOrderflagAndRejectedOrderByProtocolordercodeDesc(
//							1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 1, objorder.getFromdate(),
//							objorder.getTodate(), Orderflag, 1, 1,
//							objorder.getLsuserMaster().getLssitemaster().getSitecode(), 2,
//							objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(), objorder.getTodate(),
//							Orderflag, 1, 1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3,
//							objorder.getFromdate(), objorder.getTodate(), userlist, Orderflag, 1);
//
//			lstorders.addAll(LSlogilabprotocoldetailRepository
//					.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrderflagAndRejectedOrderByProtocolordercodeDesc(
//							1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), objorder.getFromdate(),
//							objorder.getTodate(), objorder.getLstproject(), Orderflag, 1));
//		} else { // protocoltype != -1 && Orderflag != null && reject!=null
//			lstorders = LSlogilabprotocoldetailRepository
//					.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndProtocoltypeAndOrderflagAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtocoltypeAndOrderflagAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndProtocoltypeAndOrderflagAndRejectedOrderByProtocolordercodeDesc(
//							1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 1, objorder.getFromdate(),
//							objorder.getTodate(), protocoltype, Orderflag, 1, 1,
//							objorder.getLsuserMaster().getLssitemaster().getSitecode(), 2,
//							objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(), objorder.getTodate(),
//							protocoltype, Orderflag, 1, 1, objorder.getLsuserMaster().getLssitemaster().getSitecode(),
//							3, objorder.getFromdate(), objorder.getTodate(), userlist, protocoltype, Orderflag, 1);
//			lstorders.addAll(LSlogilabprotocoldetailRepository
//					.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndProtocoltypeAndOrderflagAndRejectedOrderByProtocolordercodeDesc(
//							1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), objorder.getFromdate(),
//							objorder.getTodate(), objorder.getLstproject(), protocoltype, Orderflag, 1));
//
//		}
//		lstorders.forEach(
//				objorderDetail -> objorderDetail.setLstelnprotocolworkflow(objorder.getLstelnprotocolworkflow()));
//		List<Long> protocolordercode = new ArrayList<>();
//		if (lstorders.size() > 0 && objorder.getSearchCriteriaType() != null) {
//			protocolordercode = lstorders.stream().map(Logilabprotocolorders::getProtocolordercode)
//					.collect(Collectors.toList());
//			retuobjts.put("protocolordercodeslist", protocolordercode);
//		}
//		retuobjts.put("protocolorders", lstorders);
//		return retuobjts;
//	}

	
	public Map<String, Object> Getprotocolcancelledorders(LSlogilabprotocoldetail objorder) {
	Map<String, Object> retuobjts = new HashMap<>();
	Integer protocoltype = objorder.getProtocoltype();
	String Orderflag = null;
	if (objorder.getOrderflag() != null) {
		Orderflag = objorder.getOrderflag();
	}
	Integer reject = null;
	if (objorder.getRejected() != null) {
		reject = objorder.getRejected();
	}
	
	Date fromdate = objorder.getFromdate();
	Date todate = objorder.getTodate();
	
	
	List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objorder.getLsuserMaster());
	List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
			objorder.getLsuserMaster().getLssitemaster());
	
	List<LSprotocolselectedteam> selectedteamorders = lsprotoselectedteamRepo.findByUserteamInAndCreatedtimestampBetween(lstteam,fromdate,todate);

	List<Long> selectedteamprotcolorderList = (selectedteamorders != null && !selectedteamorders.isEmpty())
		    ? selectedteamorders.stream()
		        .map(LSprotocolselectedteam::getProtocolordercode)
		        .filter(Objects::nonNull)
		        .distinct()
		        .collect(Collectors.toList())
		    : Collections.singletonList(-1L);
		        
	List<Logilabprotocolorders> lstorders = new ArrayList<Logilabprotocolorders>();
	List<Integer> userlist = objorder.getLstuserMaster()!= null ? objorder.getLstuserMaster()
			.stream().map(LSuserMaster::getUsercode).collect(Collectors.toList())
			: new ArrayList<Integer>();
	if (protocoltype == -1 && Orderflag == null && reject == null) {
		lstorders = LSlogilabprotocoldetailRepository
				.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndTeamselectedOrTeamselectedAndProtocolordercodeInAndOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 1, objorder.getFromdate(),objorder.getTodate(),
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 2,objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(), objorder.getTodate(),
						1,objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3, objorder.getFromdate(),objorder.getTodate(), userlist,false,
						true , selectedteamprotcolorderList,1,objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3, objorder.getFromdate(),objorder.getTodate(), userlist);

		lstorders.addAll(LSlogilabprotocoldetailRepository
				.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrderByProtocolordercodeDesc(
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), objorder.getFromdate(),
						objorder.getTodate(), objorder.getLstproject()));
	} else if (protocoltype != -1 && Orderflag == null && reject == null) {
		lstorders = LSlogilabprotocoldetailRepository
				.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndProtocoltypeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtocoltypeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndProtocoltypeAndTeamselectedOrTeamselectedAndProtocolordercodeInAndOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndProtocoltypeOrderByProtocolordercodeDesc(
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 1, objorder.getFromdate(),objorder.getTodate(), protocoltype, 
						1,objorder.getLsuserMaster().getLssitemaster().getSitecode(), 2,objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(), objorder.getTodate(),protocoltype,
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3,objorder.getFromdate(), objorder.getTodate(), userlist, protocoltype,false,
						true , selectedteamprotcolorderList ,1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3,objorder.getFromdate(), objorder.getTodate(), userlist, protocoltype);


		lstorders.addAll(LSlogilabprotocoldetailRepository
				.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndProtocoltypeOrderByProtocolordercodeDesc(
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), objorder.getFromdate(),
						objorder.getTodate(), objorder.getLstproject(), protocoltype));
	} else if (protocoltype == -1 && Orderflag != null && reject == null) {
		lstorders = LSlogilabprotocoldetailRepository
				.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndOrderflagOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndOrderflagAndTeamselectedOrTeamselectedAndProtocolordercodeInAndOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndOrderflagOrderByProtocolordercodeDesc(
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 1, objorder.getFromdate(),objorder.getTodate(), Orderflag,
						1,objorder.getLsuserMaster().getLssitemaster().getSitecode(), 2,objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(), objorder.getTodate(),Orderflag,
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3,objorder.getFromdate(), objorder.getTodate(), userlist, Orderflag,false,
						true , selectedteamprotcolorderList , 1 , objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3,objorder.getFromdate(), objorder.getTodate(), userlist, Orderflag);


		lstorders.addAll(LSlogilabprotocoldetailRepository
				.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrderflagOrderByProtocolordercodeDesc(
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), objorder.getFromdate(),
						objorder.getTodate(), objorder.getLstproject(), Orderflag));

	} else if (protocoltype == -1 && Orderflag == null && reject != null) {
		lstorders = LSlogilabprotocoldetailRepository
				.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndRejectedAndTeamselectedOrTeamselectedAndProtocolordercodeInAndOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndRejectedOrderByProtocolordercodeDesc(
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 1, objorder.getFromdate(),objorder.getTodate(), 1,
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 2,objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(), objorder.getTodate(), 1,
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3, objorder.getFromdate(),objorder.getTodate(), userlist, 1,false,
						true , selectedteamprotcolorderList ,1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3, objorder.getFromdate(),objorder.getTodate(), userlist, 1);


		lstorders.addAll(LSlogilabprotocoldetailRepository
				.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedOrderByProtocolordercodeDesc(
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), objorder.getFromdate(),
						objorder.getTodate(), objorder.getLstproject(), 1));

	} else if (protocoltype != -1 && Orderflag != null && reject == null) {
		lstorders = LSlogilabprotocoldetailRepository
				.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndProtocoltypeAndOrderflagOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtocoltypeAndOrderflagOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndProtocoltypeAndOrderflagAndTeamselectedOrTeamselectedAndProtocolordercodeInAndOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndProtocoltypeAndOrderflagOrderByProtocolordercodeDesc(
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 1, objorder.getFromdate(),objorder.getTodate(), protocoltype, Orderflag,
						1,objorder.getLsuserMaster().getLssitemaster().getSitecode(), 2,objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(), objorder.getTodate(),protocoltype, Orderflag,
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3,objorder.getFromdate(), objorder.getTodate(), userlist, protocoltype, Orderflag,false,
						true , selectedteamprotcolorderList ,1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3,objorder.getFromdate(), objorder.getTodate(), userlist, protocoltype, Orderflag);


		lstorders.addAll(LSlogilabprotocoldetailRepository
				.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndProtocoltypeAndOrderflagOrderByProtocolordercodeDesc(
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), objorder.getFromdate(),
						objorder.getTodate(), objorder.getLstproject(), protocoltype, Orderflag));

	} else if (protocoltype != -1 && Orderflag == null && reject != null) {
		lstorders = LSlogilabprotocoldetailRepository
				.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndProtocoltypeAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtocoltypeAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndProtocoltypeAndRejectedAndTeamselectedOrTeamselectedAndProtocolordercodeInAndOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndProtocoltypeAndRejectedOrderByProtocolordercodeDesc(
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 1, objorder.getFromdate(),objorder.getTodate(), protocoltype, 1,
						1,objorder.getLsuserMaster().getLssitemaster().getSitecode(), 2,objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(), objorder.getTodate(),protocoltype, 1,
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3,objorder.getFromdate(), objorder.getTodate(), userlist, protocoltype, 1,false,
						true , selectedteamprotcolorderList ,1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3,objorder.getFromdate(), objorder.getTodate(), userlist, protocoltype, 1);


		lstorders.addAll(LSlogilabprotocoldetailRepository
				.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndProtocoltypeAndRejectedOrderByProtocolordercodeDesc(
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), objorder.getFromdate(),
						objorder.getTodate(), objorder.getLstproject(), protocoltype, 1));

	} else if (protocoltype == -1 && Orderflag != null && reject != null) {
		lstorders = LSlogilabprotocoldetailRepository
				.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndOrderflagAndRejectedAndTeamselectedOrTeamselectedAndProtocolordercodeInAndOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndOrderflagAndRejectedOrderByProtocolordercodeDesc(
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 1, objorder.getFromdate(),objorder.getTodate(), Orderflag, 1,
						1,objorder.getLsuserMaster().getLssitemaster().getSitecode(), 2,objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(), objorder.getTodate(),Orderflag, 1,
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3,objorder.getFromdate(), objorder.getTodate(), userlist, Orderflag, 1,false,
						true , selectedteamprotcolorderList ,1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 3,objorder.getFromdate(), objorder.getTodate(), userlist, Orderflag, 1);


		lstorders.addAll(LSlogilabprotocoldetailRepository
				.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrderflagAndRejectedOrderByProtocolordercodeDesc(
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), objorder.getFromdate(),
						objorder.getTodate(), objorder.getLstproject(), Orderflag, 1));
	} else { // protocoltype != -1 && Orderflag != null && reject!=null
		lstorders = LSlogilabprotocoldetailRepository
				.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndProtocoltypeAndOrderflagAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtocoltypeAndOrderflagAndRejectedOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndProtocoltypeAndOrderflagAndRejectedAndTeamselectedOrTeamselectedAndProtocolordercodeInAndOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndProtocoltypeAndOrderflagAndRejectedOrderByProtocolordercodeDesc(
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), 1, objorder.getFromdate(),objorder.getTodate(), protocoltype, Orderflag, 1, 
						1,objorder.getLsuserMaster().getLssitemaster().getSitecode(), 2,objorder.getLsuserMaster().getUsercode(), objorder.getFromdate(), objorder.getTodate(),protocoltype, Orderflag, 1,
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(),3, objorder.getFromdate(), objorder.getTodate(), userlist, protocoltype, Orderflag, 1,false,
						true , selectedteamprotcolorderList ,1, objorder.getLsuserMaster().getLssitemaster().getSitecode(),3, objorder.getFromdate(), objorder.getTodate(), userlist, protocoltype, Orderflag, 1);
		lstorders.addAll(LSlogilabprotocoldetailRepository
				.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndProtocoltypeAndOrderflagAndRejectedOrderByProtocolordercodeDesc(
						1, objorder.getLsuserMaster().getLssitemaster().getSitecode(), objorder.getFromdate(),
						objorder.getTodate(), objorder.getLstproject(), protocoltype, Orderflag, 1));

	}
	lstorders.forEach(
			objorderDetail -> objorderDetail.setLstelnprotocolworkflow(objorder.getLstelnprotocolworkflow()));
	List<Long> protocolordercode = new ArrayList<>();
	if (lstorders.size() > 0 && objorder.getSearchCriteriaType() != null) {
		protocolordercode = lstorders.stream().map(Logilabprotocolorders::getProtocolordercode)
				.collect(Collectors.toList());
		retuobjts.put("protocolordercodeslist", protocolordercode);
	}
	retuobjts.put("protocolorders", lstorders);
	return retuobjts;
}
	
	public List<Integer> getprotocolcode(LSprotocolmaster objuser) {
		return lsProtocolMasterRepository.getprotocolcode(objuser.getProtocolmastername());

	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> RetireProtocolMaster(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LSprotocolmaster newProtocolMasterObj = new LSprotocolmaster();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		ObjectMapper objMapper = new ObjectMapper();
		LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
				new TypeReference<LScfttransaction>() {
				});
		int protocolmastercode = new ObjectMapper().convertValue(argObj.get("protocolmastercode"), Integer.class);
		newProtocolMasterObj = LSProtocolMasterRepositoryObj.findFirstByProtocolmastercode(protocolmastercode);
		newProtocolMasterObj.setRetirestatus((Integer) argObj.get("retirestatus"));
		LSProtocolMasterRepositoryObj.save(newProtocolMasterObj);

		Integer usercode = objMapper.convertValue(argObj.get("usercode"), Integer.class);
		List<Integer> lstuser = new ObjectMapper().convertValue(argObj.get("teamuserscode"), ArrayList.class);
		List<LSprotocolmaster> LSprotocolmasterLst = new ArrayList<LSprotocolmaster>();
		if (lstuser != null && lstuser.size() != 0) {
			LSprotocolmasterLst = LSProtocolMasterRepositoryObj
					.findByStatusAndLssitemasterAndViewoptionOrCreatedbyAndStatusAndLssitemasterAndViewoptionOrCreatedbyInAndStatusAndLssitemasterAndViewoptionOrderByCreatedateDesc(
							1, LScfttransactionobj.getLssitemaster(), 1, usercode, 1,
							LScfttransactionobj.getLssitemaster(), 2, lstuser, 1, LScfttransactionobj.getLssitemaster(),
							3);

		} else {
			LSprotocolmasterLst = LSProtocolMasterRepositoryObj
					.findByStatusAndLssitemasterAndViewoptionOrCreatedbyAndStatusAndLssitemasterAndViewoptionOrderByCreatedateDesc(
							1, LScfttransactionobj.getLssitemaster(), 1, usercode, 1,
							LScfttransactionobj.getLssitemaster(), 2);

		}
		// Lsprotocolshareto retirestatus update
		LsprotocolsharetoRepository.updateRetirestatus(protocolmastercode);

		// Lsprotocolsharedby retirestatus update
		LsprotocolsharedbyRepository.updateRetirestatus(protocolmastercode);

		// Dashboard lsactivewidgets update
		lsActiveWidgetsRepository.updateRetirestatus(protocolmastercode);

		mapObj.put("RetiredLSprotocolmasterObj", newProtocolMasterObj);
		mapObj.put("protocolmasterLst", LSprotocolmasterLst);

		return mapObj;
	}

	public Map<String, Object> protocolTemplateContentMove(Map<String, Object> body) throws IOException {

		Map<String, Object> mapObj = new HashMap<String, Object>();
		Response response = new Response();
		ObjectMapper object = new ObjectMapper();

		List<LSprotocolmaster> lstLSprotocolmaster = lsProtocolMasterRepository.findAll();
		int i = 0;
		while (lstLSprotocolmaster.size() > i) {
			Lsprotocoltemplatedata lsprotocoltemplatedata = mongoTemplate
					.findById(lstLSprotocolmaster.get(i).getProtocolmastercode(), Lsprotocoltemplatedata.class);
			if (lsprotocoltemplatedata != null) {
				String content = lsprotocoltemplatedata.getContent();
				GridFSDBFile file = gridFsTemplate.findOne(new Query(Criteria.where("filename")
						.is("protocol_" + lstLSprotocolmaster.get(i).getProtocolmastercode())));
				if (file == null) {
//					gridFsTemplate.delete(new Query(
//							Criteria.where("filename").is("protocol_" + lstLSprotocolmaster.get(i).getProtocolmastercode())));
					gridFsTemplate.store(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)),
							"protocol_" + lstLSprotocolmaster.get(i).getProtocolmastercode(), StandardCharsets.UTF_16);
				}

//				Query query = new Query(Criteria.where("id").is(lstLSprotocolmaster.get(i).getProtocolmastercode()));
//				mongoTemplate.remove(query, Lsprotocoltemplatedata.class);

			}

			List<LSprotocolversion> lstLSprotocolversions = lsprotocolversionRepository
					.findByprotocolmastercode(lstLSprotocolmaster.get(i).getProtocolmastercode());
			int j = 0;
			while (lstLSprotocolversions.size() > j) {
				Lsprotocoltemplateversiondata lsprotocolversiondata = mongoTemplate.findById(
						lstLSprotocolversions.get(j).getProtocolversioncode(), Lsprotocoltemplateversiondata.class);
				if (lsprotocolversiondata != null) {
					String vcontent = lsprotocolversiondata.getContent();
					GridFSDBFile file1 = gridFsTemplate.findOne(new Query(Criteria.where("filename")
							.is("protocol_" + lstLSprotocolversions.get(j).getProtocolmastercode() + "version_"
									+ lstLSprotocolversions.get(j).getVersionno())));
					if (file1 == null) {
//						gridFsTemplate.delete(new Query(
//								Criteria.where("filename").is("protocol_" + lstLSprotocolversions.get(j).getProtocolmastercode() 
//									+ "version_" + lstLSprotocolversions.get(j).getVersionno())));
						gridFsTemplate.store(new ByteArrayInputStream(vcontent.getBytes(StandardCharsets.UTF_8)),
								"protocol_" + lstLSprotocolversions.get(j).getProtocolmastercode() + "version_"
										+ lstLSprotocolversions.get(j).getVersionno(),
								StandardCharsets.UTF_16);
					}

//					Query query = new Query(Criteria.where("id").is(lstLSprotocolversions.get(j).getProtocolversioncode()));
//					mongoTemplate.remove(query, Lsprotocoltemplateversiondata.class);

				}

				j++;
			}
			i++;
		}

		List<LSlogilabprotocoldetail> lstLSlogilabprotocoldetails = LSlogilabprotocoldetailRepository.findAll();
		int k = 0;
		while (lstLSlogilabprotocoldetails.size() > k) {
			Lsprotocolorderdata lsprotocolorderdata = mongoTemplate
					.findById(lstLSlogilabprotocoldetails.get(k).getProtocolordercode(), Lsprotocolorderdata.class);
			if (lsprotocolorderdata != null) {
				String content = lsprotocolorderdata.getContent();
				GridFSDBFile file = gridFsTemplate.findOne(new Query(Criteria.where("filename")
						.is("protocolorder_" + lstLSlogilabprotocoldetails.get(k).getProtocolordercode())));
				if (file == null) {
//					gridFsTemplate.delete(new Query(
//							Criteria.where("filename").is("protocolorder_" + lstLSlogilabprotocoldetails.get(k).getProtocolordercode())));
					gridFsTemplate.store(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)),
							"protocolorder_" + lstLSlogilabprotocoldetails.get(k).getProtocolordercode(),
							StandardCharsets.UTF_16);
				}

//				Query query = new Query(Criteria.where("id").is(lstLSlogilabprotocoldetails.get(k).getProtocolordercode()));
//				mongoTemplate.remove(query, Lsprotocolorderdata.class);

			}

			List<LSprotocolorderversion> lstLSprotocolversions = lsprotocolorderversionRepository
					.findByProtocolordercode(lstLSlogilabprotocoldetails.get(k).getProtocolordercode());
			int j = 0;
			while (lstLSprotocolversions.size() > j) {
				Lsprotocolorderversiondata lsprotocolversiondata = mongoTemplate.findById(
						lstLSprotocolversions.get(j).getProtocolorderversioncode(), Lsprotocolorderversiondata.class);
				if (lsprotocolversiondata != null) {
					String vcontent = lsprotocolversiondata.getContent();
					GridFSDBFile file1 = gridFsTemplate.findOne(new Query(Criteria.where("filename")
							.is("protocolorder_" + lstLSprotocolversions.get(j).getProtocolordercode() + "version_"
									+ lstLSprotocolversions.get(j).getVersionno())));
					if (file1 == null) {
//						gridFsTemplate.delete(new Query(
//								Criteria.where("filename").is("protocolorder_" + lstLSprotocolversions.get(j).getProtocolordercode() 
//									+ "version_" + lstLSprotocolversions.get(j).getVersionno())));					
						gridFsTemplate.store(new ByteArrayInputStream(vcontent.getBytes(StandardCharsets.UTF_8)),
								"protocol_" + lstLSprotocolversions.get(j).getProtocolordercode() + "version_"
										+ lstLSprotocolversions.get(j).getVersionno(),
								StandardCharsets.UTF_16);
					}

//					Query query = new Query(Criteria.where("id").is(lstLSprotocolversions.get(j).getProtocolorderversioncode()));
//					mongoTemplate.remove(query, Lsprotocolorderversiondata.class);

				}

				j++;
			}
			k++;
		}
		return mapObj;
	}

	public List<UserProjection> getusercodeandusername(LSSiteMaster argObj) {
		Map<String, Object> rtnobjects = new HashMap<>();
		List<Integer> usercode = LSMultisitesRepositery.getusernameandusercode(argObj);
		return lsusermasterRepository.getUsernameAndUsercode(usercode);
//	rtnobjects.put("lsusermaster", usermaster);
//	return rtnobjects;
	}

	public LSlogilabprotocoldetail sendapprovel(LSlogilabprotocoldetail objdir) {
		LSlogilabprotocoldetail logiobj = new LSlogilabprotocoldetail();
//		logiobj = LSlogilabprotocoldetailRepository.findByProtocolordercodeAndProtoclordername(
//				objdir.getProtocolordercode(), objdir.getProtoclordername());

		logiobj = LSlogilabprotocoldetailRepository.findByProtocolordercode(
				objdir.getProtocolordercode());
		
		logiobj.setSentforapprovel(objdir.getSentforapprovel());
		logiobj.setApprovelaccept(objdir.getApprovelaccept());
		LSlogilabprotocoldetailRepository.save(logiobj);

		String screen = "Sheet Order";
		String Notification = "SENDFORAPPROVEL";

//		LSuserMaster notifyfrom = logiobj.getLsuserMaster();
//		LSuserMaster notifyto = logiobj.getAssignedto();

		LSuserMaster notifyfrom = logiobj.getAssignedto();
		LSuserMaster notifyto = logiobj.getLsuserMaster();

		try {
			sendnotification(logiobj, Notification, screen, notifyto, notifyfrom);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return logiobj;
	}

//	public LSlogilabprotocoldetail acceptapprovel(LSlogilabprotocoldetail objdir) {
//		LSlogilabprotocoldetail logiobj = new LSlogilabprotocoldetail();
//		logiobj = LSlogilabprotocoldetailRepository.findByProtocolordercodeAndProtoclordername(
//				objdir.getProtocolordercode(), objdir.getProtoclordername());
//		logiobj.setApprovelaccept(objdir.getApprovelaccept());
//		LSlogilabprotocoldetailRepository.save(logiobj);
//		return logiobj;
//	}

	@SuppressWarnings("unlikely-arg-type")
	public LSlogilabprotocoldetail acceptapprovel(LSlogilabprotocoldetail objdir) throws ParseException {
		LSlogilabprotocoldetail logiobj = new LSlogilabprotocoldetail();
		logiobj = LSlogilabprotocoldetailRepository.findByProtocolordercode(
				objdir.getProtocolordercode());

		String screen = "Protocol Order";
		if (objdir.getApprovelaccept().equals("3")) {
			logiobj.setApprovelaccept(objdir.getApprovelaccept());
			logiobj.setApprovelstatus(objdir.getApprovelstatus());
			logiobj.setOrderflag("R");
			logiobj.setApproved(0);
			logiobj.setRejected(1);
			logiobj.setDirectorycode(null);
			logiobj.setCompletedtimestamp(commonfunction.getCurrentUtcTime());

			String Notification = "REJECTALERT";
			LSuserMaster notifyfrom = logiobj.getLsuserMaster();
			LSuserMaster notifyto = logiobj.getAssignedto();
//			LSuserMaster notifyfrom = logiobj.getAssignedto();
//			LSuserMaster notifyto = logiobj.getLsuserMaster();

			sendnotification(logiobj, Notification, screen, notifyto, notifyfrom);

		} else if (objdir.getApprovelaccept().equals("2")) {
			logiobj.setApprovelaccept(objdir.getApprovelaccept());
			logiobj.setSentforapprovel(objdir.getSentforapprovel());

			String Notification = "RETURNALERT";
			LSuserMaster notifyfrom = logiobj.getLsuserMaster();
			LSuserMaster notifyto = logiobj.getAssignedto();

//            LSuserMaster notifyfrom = logiobj.getAssignedto();
//			LSuserMaster notifyto = logiobj.getLsuserMaster();

			sendnotification(logiobj, Notification, screen, notifyto, notifyfrom);
		} else {

			logiobj.setApprovelaccept(objdir.getApprovelaccept());
			String Notification = "APPROVEALERT";
			LSuserMaster notifyfrom = logiobj.getLsuserMaster();
			LSuserMaster notifyto = logiobj.getAssignedto();

//			LSuserMaster notifyfrom = logiobj.getAssignedto();
//			LSuserMaster notifyto = logiobj.getLsuserMaster();

			sendnotification(logiobj, Notification, screen, notifyto, notifyfrom);
		}
		LSlogilabprotocoldetailRepository.save(logiobj);
		return logiobj;
	}

	@SuppressWarnings("unlikely-arg-type")
	public void sendnotification(LSlogilabprotocoldetail objdir, String Notification, String screen,
			LSuserMaster notifyto, LSuserMaster notifyfrom) throws ParseException {

		String protocolordername = "";
		SequenceTable seqobj =  sequenceTableRepository.findBySequencecodeOrderBySequencecode(2);
		Boolean Applicationseq = seqobj.getSequenceview().equals(2) ? true : false;
		protocolordername = Applicationseq 
				?  objdir.getSequenceid() != null
					? objdir.getSequenceid() : objdir.getProtoclordername()
				: objdir.getProtoclordername();
		
		LSnotification LSnotification = new LSnotification();

		String Details = "{\"ordercode\" :\"" + objdir.getProtocolordercode() + "\",\"order\" :\""
				+ protocolordername + "\",\"user\":\"" + objdir.getLsuserMaster().getUsername()
				+ "\",\"notifyto\":\"" + objdir.getAssignedto().getUsername() + "\"}";

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

	public boolean Validateprotocolcountforfreeuser(LSSiteMaster lssitemaster) {
		boolean countexceeded = false;
		long sheetcount = LSProtocolMasterRepositoryObj.countByLssitemaster(lssitemaster.getSitecode());
		if (sheetcount < 10) {
			countexceeded = false;
		} else {
			countexceeded = true;
		}
		return countexceeded;
	}

	public LSlogilabprotocoldetail stopprotoautoregister(LSlogilabprotocoldetail objdir) throws ParseException {
		LSlogilabprotocoldetail logiobj = new LSlogilabprotocoldetail();
		logiobj = LSlogilabprotocoldetailRepository.findByProtocolordercodeAndProtoclordername(
				objdir.getProtocolordercode(), objdir.getProtoclordername());

		LStestmasterlocal testmast = new LStestmasterlocal();
		testmast = lstestmasterlocalRepository.findByTestcode(logiobj.getTestcode());

		logiobj.setRepeat(objdir.getRepeat());
		logiobj.setTestname(testmast.getTestname());
		logiobj.setAutoregistercount(0);
		LSlogilabprotocoldetailRepository.save(logiobj);

		List<LsAutoregister> autoobj = lsautoregisterrepo.findByBatchcodeAndScreen(objdir.getProtocolordercode(),
				"Protocol_Order");
		if (!autoobj.isEmpty()) {
			autoobj.get(0).setRepeat(objdir.getRepeat());
			autoobj.get(0).setStoptime(commonfunction.getCurrentUtcTime());
			// lsautoregister
			logiobj.setLsautoregister(autoobj.get(0));
			logiobj.setCanuserprocess(true);
//			/objdir.setRepeat(false);
			lsautoregisterrepo.save(autoobj);
		}
		LSlogilabprotocoldetailRepository.save(logiobj);
		String timerId = autoobj.get(0).getTimerIdname();
		if (timerId != null) {
			stopTimer(timerId);

		}
		return logiobj;
	}

	static void saveNewDocument(InputStream document) {
		try /* JAVA: was using */
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			byte[] buf = new byte[1024];
			int len;
			while ((len = document.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			document.close();
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public ByteArrayInputStream Exportwithgroupdocs(LSprotocolmaster protocol) throws IOException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap.put("protocolmastercode", protocol.getProtocolmastercode());
		objMap.put("ismultitenant", 1);
		objMap = getProtocolStepLst(objMap);

//		String licensePath = Constants.LICENSE;

//		String documentPath = Constants.SAMPLE_DOCX;

		byte[] data = null;
		try {

//        	License license = new License();
//        	license.setLicense(licensePath);
//        	String outputFilePath = System.getProperty("java.io.tmpdir") + "\\"+protocol.getProtocolmastername()+".docx";
//        	File.createTempFile(protocol.getProtocolmastername(), ".docx", new File(System.getProperty("java.io.tmpdir")));

			com.aspose.words.Document docA = new com.aspose.words.Document();

			TextWatermarkOptions options = new TextWatermarkOptions();
			options.setFontFamily("Arial");
			options.setFontSize(36);
			options.setColor(Color.blue);
			options.setLayout(WatermarkLayout.DIAGONAL);
			options.isSemitrasparent(false);

			docA.getWatermark().setText("Agaram", options);

			DocumentBuilder builder = new DocumentBuilder(docA);

			// Insert text to the document start.
			builder.moveToDocumentStart();
			builder.insertHtml(protocol.getProtocoldatainfo());
//        	formhtmldataforprotocols(docA, objMap);

//        	Document docB = new Document(System.getProperty("java.io.tmpdir") + "\\"+protocol.getProtocolmastername()+".docx");
			// Add document B to the and of document A, preserving document B formatting.
//        	docA.appendDocument(docB, ImportFormatMode.KEEP_SOURCE_FORMATTING);
//
//        	docA.save(System.getProperty("java.io.tmpdir") + "\\"+protocol.getProtocolmastername()+".docx");

//        	Editor editor = new Editor(outputFilePath); //passing path to the constructor, default WordProcessingLoadOptions will be applied automatically
////        	
//        	WordProcessingEditOptions editOptions = new WordProcessingEditOptions();
//        	editOptions.setEnableLanguageInformation(true);
////
//        	editor.edit(editOptions);
//        	EditableDocument afterEdit = EditableDocument.fromMarkup(formhtmldataforprotocols(objMap), null);
////        	
//////        			Constants.getOutputDirectoryPath("newdocdes.docx");
////      	
//        	WordProcessingSaveOptions saveOptions = new WordProcessingSaveOptions(WordProcessingFormats.Docx);
//        	editor.save(afterEdit, outputFilePath, saveOptions);

//        	File targetFile = new File(System.getProperty("java.io.tmpdir") + "\\" + "newdocdes.docx");
//        	try (FileInputStream inputStream = new FileInputStream(targetFile)) {
//                inputStream.read(data);
//            }
			ByteArrayOutputStream docOutStream = new ByteArrayOutputStream();
			docA.save(docOutStream, SaveFormat.DOCX);

			// Convert the document to byte form.
			data = docOutStream.toByteArray();

//        	data = FileUtils.readFileToByteArray(new File(System.getProperty("java.io.tmpdir") + "\\" + protocol.getProtocolmastername()+".docx"));
//        			Files.readAllBytes(System.getProperty("java.io.tmpdir") + "\\" + "newdocdes.docx");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(data);

		return bis;
	}

	public com.aspose.words.DocumentBuilder formhtmldataforprotocols(com.aspose.words.Document doc,
			Map<String, Object> protocoldatamap) {
		com.aspose.words.DocumentBuilder builder = new com.aspose.words.DocumentBuilder(doc);

		// Insert text to the document start.
		builder.moveToDocumentStart();

		try {

			JSONObject jsonObj = new JSONObject(protocoldatamap.get("ProtocolData").toString());
			JSONObject obsrtactobj = jsonObj.getJSONObject("abstract");
			JSONArray objsection = jsonObj.getJSONArray("sections");
			builder.insertHtml(obsrtactobj.getString("value"));

			for (Object item : objsection) {
				JSONObject objitem = (JSONObject) item;
				JSONArray objsteps = objitem.getJSONArray("steps");
				for (Object stepitem : objsteps) {
					JSONArray objeditors = ((JSONObject) stepitem).getJSONArray("editors");

					for (Object editoritem : objeditors) {
						JSONObject objeditor = (JSONObject) editoritem;

						switch (objeditor.getString("editortype")) {
						case "documenteditor":
							JSONObject objeditorvalue = objeditor.getJSONObject("value");
							builder.insertHtml(objeditorvalue.getString("data"));
							break;
						case "sheeteditor":
							JSONObject objsheeteditorvalue = objeditor.getJSONObject("value");
							JSONObject sheetcontent = objsheeteditorvalue.getJSONObject("data");
							JSONArray sheets = objsheeteditorvalue.getJSONArray("sheets");
							String sheetdata = "";
							int lastrowindex = -1;
							int lastcellindex = -1;
							for (Object sheet : sheets) {

							}
							for (Object sheet : sheets) {
								sheetdata += " <Table key={sheetindex} stripped bordered hover size=\"sm\"><tbody>";

								sheetdata += "</tbody></Table>";
							}
							break;

						default:

						}

					}
				}
			}

//        	editorvalue = "<html> <head><title>Validate</title></head>" + 
//        			"<body><div>"+
//        			editorvalue+
//        			editorvalue.replaceAll("clear: both;", "").
//        			replaceAll("text-transform: none;", "").replaceAll(" white-space: normal;", "").
//        			replaceAll("white-space: pre-wrap;", "").replaceAll("white-space: pre !important;", "")
//        			.replaceAll("white-space: nowrap;", "")+

//        			"</div></body></html>";
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		return builder;
	}

	public ByteArrayInputStream Exportwithspire(LSprotocolmaster protocol) throws IOException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap.put("protocolmastercode", protocol.getProtocolmastercode());
		objMap.put("ismultitenant", 1);
		objMap = getProtocolStepLst(objMap);
		byte[] data = null;

		Document doc = new Document();

		Section section = doc.addSection();

//		Paragraph paragraph = new Paragraph(doc);
//		 paragraph.appendHTML(protocol.getProtocoldatainfo());
//		 paragraph.app

		// Create a TextWatermark instance
		TextWatermark txtWatermark = new TextWatermark();

		// Set the format of the text watermark
		txtWatermark.setText("Agaram");
		txtWatermark.setFontSize(40);
		txtWatermark.setColor(Color.blue);
		txtWatermark.setLayout(com.spire.doc.documents.WatermarkLayout.Diagonal);

		// Add the text watermark to document
		section.getDocument().setWatermark(txtWatermark);

		Paragraph paragraph = section.addParagraph();
//		paragraph.appendHyperlink("https://www-iceblue.com/", "Home Page", HyperlinkType.Web_Link);

		paragraph.appendBreak(BreakType.Line_Break);
		paragraph.appendBreak(BreakType.Line_Break);

//		        paragraph.appendHyperlink("mailto:support@e-iceblue.com", "Mail Us", HyperlinkType.E_Mail_Link);

		// Append line breaks
//		        paragraph.appendBreak(BreakType.Line_Break);
//		        paragraph.appendBreak(BreakType.Line_Break);
		// Add a file link
//		        String filePath = "C:\\Users\\Administrator\\Desktop\\report.xlsx";
//		        paragraph.appendHyperlink(filePath, "Click to open the report", HyperlinkType.File_Link);

		// Append line breaks
//		        paragraph.appendBreak(BreakType.Line_Break);
//		        paragraph.appendBreak(BreakType.Line_Break);
		// Add another section and create a bookmark
//		        Section section2 = doc.addSection();
//		        Paragraph bookmarkParagraph = section2.addParagraph();
//		        bookmarkParagraph.appendText("Here is a bookmark");
//		        BookmarkStart start = bookmarkParagraph.appendBookmarkStart("myBookmark");
//		        bookmarkParagraph.getItems().insert(0, start);
//		        bookmarkParagraph.appendBookmarkEnd("myBookmark");
		// Link to the bookmark
//		        paragraph.appendHyperlink("myBookmark", "Jump to a location inside this document", HyperlinkType.Bookmark);

		// Append line breaks
//		        paragraph.appendBreak(BreakType.Line_Break);
//		        paragraph.appendBreak(BreakType.Line_Break);

		paragraph.appendHTML(protocol.getProtocoldatainfo());

		ByteArrayOutputStream docOutStream = new ByteArrayOutputStream();
		doc.saveToFile(docOutStream, FileFormat.Docx_2013);
		data = docOutStream.toByteArray();

		ByteArrayInputStream bis = new ByteArrayInputStream(data);

		return bis;
	}
	
	public ResponseEntity<Object> GetAllorders(LSuserMaster objuser)
	{
		List<LogilabProtocolOrderssh> lstorders = new ArrayList<LogilabProtocolOrderssh>();
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		lstorders = LSlogilabprotocoldetailRepository.findBySitecodeAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
				objuser.getLssitemaster().getSitecode(), fromdate, todate);
		return new ResponseEntity<>(lstorders, HttpStatus.OK);
	}
	
}