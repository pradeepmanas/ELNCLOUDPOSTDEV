package com.agaram.eln.primary.service.protocol;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.poi.util.IOUtils;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
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
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.config.CustomMultipartFile;
import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.fetchmodel.getorders.Logilabprotocolorders;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cloudFileManip.CloudUserSignature;
import com.agaram.eln.primary.model.cloudProtocol.CloudLSprotocolorderversionstep;
import com.agaram.eln.primary.model.cloudProtocol.CloudLSprotocolstepInfo;
import com.agaram.eln.primary.model.cloudProtocol.CloudLSprotocolversionstep;
import com.agaram.eln.primary.model.cloudProtocol.CloudLsLogilabprotocolstepInfo;
import com.agaram.eln.primary.model.cloudProtocol.LSprotocolstepInformation;
import com.agaram.eln.primary.model.fileManipulation.UserSignature;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSprotocolfolderfiles;
import com.agaram.eln.primary.model.instrumentDetails.LSsheetfolderfiles;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolordersharedby;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolordershareto;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolorderstructure;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocolsteps;
import com.agaram.eln.primary.model.protocols.LSprotocolfiles;
import com.agaram.eln.primary.model.protocols.LSprotocolfilesContent;
import com.agaram.eln.primary.model.protocols.LSprotocolimages;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolmastertest;
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
import com.agaram.eln.primary.model.protocols.Lsprotocolsharedby;
import com.agaram.eln.primary.model.protocols.Lsprotocolshareto;
import com.agaram.eln.primary.model.protocols.ProtocolImage;
import com.agaram.eln.primary.model.protocols.ProtocolorderImage;
import com.agaram.eln.primary.model.protocols.Protocolordervideos;
import com.agaram.eln.primary.model.protocols.Protocolvideos;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflowgroupmapping;
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
import com.agaram.eln.primary.repository.instrumentDetails.LSprotocolfolderfilesRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSsheetfolderfilesRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsprotocolOrderStructureRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsprotocolordersharedbyRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsprotocolordersharetoRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesdataRepository;
import com.agaram.eln.primary.repository.protocol.LSProtocolMasterRepository;
import com.agaram.eln.primary.repository.protocol.LSProtocolStepRepository;
import com.agaram.eln.primary.repository.protocol.LSlogilabprotocoldetailRepository;
import com.agaram.eln.primary.repository.protocol.LSlogilabprotocolstepsRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolfilesContentRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolfilesRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolimagesRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolmastertestRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolorderfilesContentRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolorderfilesRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolorderimagesRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolordersampleupdatesRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolordersampleupdatesRepository.UserProjection1;
import com.agaram.eln.primary.repository.protocol.LSprotocolorderstephistoryRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolorderstepversionRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolorderversionRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolordervideosRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolorderworkflowhistoryRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolsampleupdatesRepository;
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
import com.agaram.eln.primary.repository.sheetManipulation.LSsheetworkflowRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LStestmasterlocalRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowgroupmappingRepository;
import com.agaram.eln.primary.repository.usermanagement.LSMultiusergroupRepositery;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSnotificationRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusergroupRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusersteamRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;
import com.agaram.eln.primary.service.basemaster.BaseMasterService;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.fileManipulation.FileManipulationservice;
import com.agaram.eln.primary.service.material.TransactionService;
import com.google.gson.Gson;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.mongodb.gridfs.GridFSDBFile;

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
	private LSSiteMasterRepository LSSiteMasterRepository;

	@Autowired
	private CloudLSprotocolstepInfoRepository CloudLSprotocolstepInfoRepository;

	@Autowired
	private LSlogilabprotocoldetailRepository LSlogilabprotocoldetailRepository;

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

	@Autowired
	private LSsheetworkflowRepository lssheetworkflowRepository;

	@Autowired
	private LSprotocolorderstephistoryRepository lsprotocolorderstephistoryRepository;

	@Autowired
	private LSworkflowgroupmappingRepository lsworkflowgroupmappingRepository;

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
	
	@Autowired
	private GridFsTemplate gridFsTemplate;

//	@Autowired
//	private LSMultiusergroupRepositery lsMultiusergroupRepositery;

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
			mapObj.put("templatecount",LSProtocolMasterRepositoryObj
					.countByStatusAndLssitemasterAndViewoptionOrCreatedbyAndStatusAndLssitemasterAndViewoptionOrCreatedbyInAndStatusAndLssitemasterAndViewoptionOrderByCreatedateDesc(
							1, objusers.getLssitemaster().getSitecode(), 1, objusers.getUsercode(), 1,
							objusers.getLssitemaster().getSitecode(), 2, lstuser, 1,
							objusers.getLssitemaster().getSitecode(), 3));

		} else {
			mapObj.put("templatecount",LSProtocolMasterRepositoryObj
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
				.findByStatusAndApprovedAndLssitemaster(1, 1, site.getSitecode());

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
				if (multitenent == 1) {
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
			if (multitenent == 1) {
				mapObj.put("ProtocolData",objCloudFileManipulationservice.retrieveCloudSheets(lsProtocolMaster.getFileuid(),
						TenantContext.getCurrentTenant() + "protocol"));
			} else {
				GridFSDBFile largefile = gridFsTemplate.findOne(new Query(
						Criteria.where("filename").is("protocol_" + lsProtocolMaster.getProtocolmastercode())));
				mapObj.put("ProtocolData",new BufferedReader(
						new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
								.collect(Collectors.joining("\n")));
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
				if (multitenent == 1) {
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
		List<LSworkflow> workflow = obj.convertValue(argObj.get("lstworkflow"), new TypeReference<List<LSworkflow>>() {
		});
		List<LSlogilabprotocolsteps> LSlogilabprotocolsteps = LSlogilabprotocolstepsRepository
				.findByStatusAndSitecodeAndProtocolordercodeIn(1, sitecode, protocolordercode);
		ArrayList<Long> ordercode = new ArrayList<Long>();
		for (LSlogilabprotocolsteps LSprotocolstepObj1 : LSlogilabprotocolsteps) {
			if ((int) argObj.get("ismultitenant") == 1) {
				CloudLsLogilabprotocolstepInfo newLSprotocolstepInfo = CloudLsLogilabprotocolstepInfoRepository
						.findByContentvaluesequal(searchcontent, LSprotocolstepObj1.getProtocolorderstepcode());
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
					LSlogilabprotocoldetailArray.forEach(objorderDetail -> objorderDetail.setLstworkflow(workflow));
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
					if (ismultitenant == 1) {
						CloudLsLogilabprotocolstepInfo newLSprotocolstepInfo = CloudLsLogilabprotocolstepInfoRepository
								.findByContentvaluesequal(searchcontent, LSprotocolstepObj1.getProtocolorderstepcode());
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

			if (LSprotocolstepObj.getIsmultitenant() == 1) {
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
			if (LSprotocolstepObj.getIsmultitenant() == 1) {
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

			LSsheetworkflow lssheetworkflow = lssheetworkflowRepository
					.findTopByAndLssitemasterOrderByWorkflowcodeAsc(lssitemaster);

			protocolMaster.setApproved(0);
//			protocolMaster.setlSprotocolworkflow(lsprotocolworkflow);
			protocolMaster.setLssheetworkflow(lssheetworkflow);
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

			LSsheetworkflow lssheetworkflow = lssheetworkflowRepository
					.findTopByAndLssitemasterOrderByWorkflowcodeAsc(lssitemaster);

			protocolMaster.setApproved(0);
//			protocolMaster.setlSprotocolworkflow(lsprotocolworkflow);
			protocolMaster.setLssheetworkflow(lssheetworkflow);
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
	public Map<String, Object> addProtocolMaster(Map<String, Object> argObj) throws IOException {
		Map<String, Object> mapObj = new HashMap<String, Object>();

		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if (argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
		}
		ObjectMapper objMapper = new ObjectMapper();
		Response response = new Response();

		if (argObj.containsKey("newProtocolMasterObj")) {
			LSuserMaster LsuserMasterObj = LSuserMasterRepositoryObj
					.findByusercode(LScfttransactionobj.getLsuserMaster());
			LSprotocolmaster newProtocolMasterObj = new LSprotocolmaster();
			if (argObj.containsKey("edit")) {

				int protocolmastercode = new ObjectMapper().convertValue(argObj.get("protocolmastercode"),
						Integer.class);

				if (LSProtocolMasterRepositoryObj.findByProtocolmastercodeNotAndProtocolmasternameIgnoreCase(
						protocolmastercode, (String) argObj.get("protocolmastername")).size() != 0) {
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
				if (LSProtocolMasterRepositoryObj.findByProtocolmasternameIgnoreCaseAndLssitemaster(
						argObj.get("protocolmastername").toString().trim(),
						LScfttransactionobj.getLssitemaster()) != null) {
					response.setStatus(false);
					response.setInformation("IDS_MSG_ALREADY");
					mapObj.put("response", response);
					return mapObj;
				}
				newProtocolMasterObj.setProtocolmastername((String) argObj.get("protocolmastername"));
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
				if (newProtocolMasterObj.getLssheetworkflow() == null) {
					newProtocolMasterObj.setLssheetworkflow(
							lssheetworkflowRepository.findTopByAndLssitemasterOrderByWorkflowcodeAsc(lssitemaster));
				}
//				newProtocolMasterObj.setlSprotocolworkflow(lsprotocolworkflow);

			}
			LSProtocolMasterRepositoryObj.save(newProtocolMasterObj);

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
					.findByStatusAndLssitemasterAndProtocolmastername(1, LScfttransactionobj.getLssitemaster(),
							newProtocolMasterObj.getProtocolmastername());

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
				if (newProtocolMasterObj.getIsmultitenant() == 1) {
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
			if (LSprotocolsteplst != null) {
				mapObj.put("protocolstepLst", LSprotocolstepLst);
				Integer isMultitenant = (Integer) argObj.get("ismultitenant");
				Gson gson = new Gson();
				String protocolDataJson = gson.toJson(argObj.get("protocolData"));
				
				if(isMultitenant == 1) {
					commonservice.updateProtocolContent(protocolDataJson, newProtocolMasterObj);					
				} else {
					if (gridFsTemplate.findOne(
							new Query(Criteria.where("filename").is("protocol_" + newProtocolMasterObj.getProtocolmastercode()))) == null) {
						try {
							gridFsTemplate.store(new ByteArrayInputStream(protocolDataJson.getBytes(StandardCharsets.UTF_8)),
									"protocol_" + newProtocolMasterObj.getProtocolmastercode(), StandardCharsets.UTF_16);
						} catch (Exception e) {
							System.out.println(
									"error protocoldata lsprotocolmaster content update mongodb" + newProtocolMasterObj.getProtocolmastercode());
						}
					}
				}
				mapObj.put("ProtocolData",protocolDataJson);
			} else {
				mapObj.put("protocolstepLst", new ArrayList<>());
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
		Details = "{\"order\":\"" + newProtocolMasterObj.getProtocolmastername() + "\", \"createduser\":\""
				+ newProtocolMasterObj.getCreatedbyusername() + "\"}";
		List<LSuserMaster> lstnotified = new ArrayList<LSuserMaster>();

		for (int i = 0; i < objteam.size(); i++) {
			LSusersteam objteam1 = lsusersteamRepository.findByteamcode(objteam.get(i).getTeamcode());

			List<LSuserteammapping> lstusers = objteam1.getLsuserteammapping();

			for (int j = 0; j < lstusers.size(); j++) {

				if (newProtocolMasterObj.getLSuserMaster().getUsercode() != lstusers.get(j).getLsuserMaster()
						.getUsercode()) {
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
			if (argObj1 != null) {
				LScfttransaction LScfttransactionobj = new LScfttransaction();
				LScfttransactionobj = new ObjectMapper().convertValue(argObj1.get("objsilentaudit"),
						new TypeReference<LScfttransaction>() {
						});

//			LSprotocolupdates lSprotocolupdates =(LSprotocolupdates) argObj1.get("objsilentaudit");
				LScfttransactionobj.setComments("Protocol" + " " + newProtocolMasterObj1.getProtocolmastername() + " "
						+ " was versioned to version_" + Versionnumber + " " + "by the user" + " "
						+ newProtocolMasterObj1.getCreatedbyusername());
				LScfttransactionobj.setTableName("LSfile");
				LScfttransactionobj.setTableName("LSprotocolmaster");
				lscfttransactionRepository.save(LScfttransactionobj);
			}

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

	public Map<String, Object> updateworkflowforProtocol(LSprotocolmaster objClass) {

		Map<String, Object> mapObj = new HashMap<String, Object>();

		int approved = 0;

		if (objClass.getApproved() != null) {
			approved = objClass.getApproved();
		}

		LSProtocolMasterRepositoryObj.updateFileWorkflow(objClass.getLssheetworkflow(), approved,
				objClass.getRejected(), objClass.getProtocolmastercode());

		LSprotocolmaster LsProto = LSProtocolMasterRepositoryObj
				.findFirstByProtocolmastercode(objClass.getProtocolmastercode());

		LsProto.setLssheetworkflow(objClass.getLssheetworkflow());
		if (LsProto.getApproved() == null) {
			LsProto.setApproved(0);
		}
		List<LSprotocolworkflowhistory> obj = objClass.getLsprotocolworkflowhistory();
		obj = obj.stream()
	            .map(workflowhistory -> {
	                try {
	                	if(workflowhistory.getHistorycode()==null) {
	                	workflowhistory.setCreatedate(commonfunction.getCurrentUtcTime());
	                	}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                return workflowhistory;
	            })
	            .collect(Collectors.toList());
		lsprotocolworkflowhistoryRepository.save(obj);
		LsProto.setLsprotocolworkflowhistory(obj);
		mapObj.put("ProtocolObj", LsProto);
		mapObj.put("status", "success");
		if (objClass.getViewoption() == null || objClass.getViewoption() != null && objClass.getViewoption() != 2) {
			if (objClass.getProtocolmastername() != null) {
				LSsheetworkflow objlastworkflow = lssheetworkflowRepository
						.findTopByAndLssitemasterOrderByWorkflowcodeDesc(objClass.getIsfinalstep().getLssitemaster());
				if (objlastworkflow != null
						&& objClass.getCurrentStep().getWorkflowcode() == objlastworkflow.getWorkflowcode()) {
					objClass.setFinalworkflow(1);
					;
				} else {
					objClass.setFinalworkflow(0);
					;
				}
			}

			try {
				updatenotificationforprotocolworkflowapproval(objClass, LsProto.getLssheetworkflow());
				updatenotificationforprotocol(objClass, LsProto.getLssheetworkflow());
			} catch (Exception e) {

			}
		}
		return mapObj;
	}

	private void updatenotificationforprotocolworkflowapproval(LSprotocolmaster objClass,
			LSsheetworkflow lssheetworkflow) {
		String Details = "";
		String Notification = "";
		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
		LSprotocolmaster LsProto = LSProtocolMasterRepositoryObj
				.findFirstByProtocolmastercode(objClass.getProtocolmastercode());
		LSuserMaster createby = lsusermasterRepository.findByusercode(objClass.getCreatedby());
		LSnotification objnotify = new LSnotification();

		for (int k = 0; k < objClass.getLssheetworkflow().getLssheetworkflowgroupmap().size(); k++) {

			List<LSMultiusergroup> userobj = lsMultiusergroupRepositery.findBylsusergroup(
					objClass.getLssheetworkflow().getLssheetworkflowgroupmap().get(k).getLsusergroup());

			List<Integer> objnotifyuser = userobj.stream().map(LSMultiusergroup::getUsercode)
					.collect(Collectors.toList());
			List<LSuserMaster> objuser = lsusermasterRepository.findByUsercodeInAndUserretirestatusNot(objnotifyuser,
					1);

			List<LSuserteammapping> objteam = LSuserteammappingRepositoryObj
					.findByTeamcodeNotNullAndLsuserMaster(objClass.getLSuserMaster());
			for (int j = 0; j < objteam.size(); j++) {
				LSusersteam objteam1 = lsusersteamRepository.findByteamcode(objteam.get(j).getTeamcode());

				List<LSuserteammapping> lstusers = objteam1.getLsuserteammapping();

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
	}

	private void updatenotificationforprotocol(LSprotocolmaster objClass, LSsheetworkflow lsprotocolworkflow) {
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
					+ objClass.getLsprotocolworkflowhistory().get(0).getLssheetworkflow().getWorkflowname()
					+ "\", \"currentworkflowcode\":\"" + objClass.getLssheetworkflow().getWorkflowcode() + "\"}";

			List<LSuserMaster> lstnotified = new ArrayList<LSuserMaster>();

			for (int i = 0; i < objteam.size(); i++) {
				LSusersteam objteam1 = lsusersteamRepository.findByteamcode(objteam.get(i).getTeamcode());

				List<LSuserteammapping> lstusers = objteam1.getLsuserteammapping();

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

	}

	public Map<String, Object> updateworkflowforProtocolorder(LSlogilabprotocoldetail objClass) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		try {
			int approved = 0;

			if (objClass.getApproved() != null) {
				approved = objClass.getApproved();
			}
//		LSlogilabprotocoldetailRepository.updateFileWorkflow(objClass.getlSprotocolworkflow(), approved,
//				objClass.getRejected(), objClass.getProtocolordercode());
			LSlogilabprotocoldetailRepository.updateFileWorkflow(objClass.getLsworkflow(), approved,
					objClass.getRejected(), objClass.getProtocolordercode());

			LSlogilabprotocoldetail LsProto = LSlogilabprotocoldetailRepository
					.findOne(objClass.getProtocolordercode());

			LsProto.setLsworkflow(objClass.getLsworkflow());
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
			List<LSworkflow> lstworkflow = objClass.getLstworkflow();
			LSlogilabprotocoldetail.forEach(objorderDetail -> objorderDetail.setLstworkflow(lstworkflow));
			LsProto.setLstworkflow(lstworkflow);
			mapObj.put("curentprotocolorder", LSlogilabprotocoldetail);
			mapObj.put("ProtocolObj", LsProto);
			mapObj.put("status", "success");
			if (objClass.getProtocoltype() != null) {
				LSworkflow objlastworkflow = lsworkflowRepository
						.findTopByAndLssitemasterOrderByWorkflowcodeDesc(objClass.getIsfinalstep().getLssitemaster());
				if (objlastworkflow != null
						&& objClass.getCurrentStep().getWorkflowcode() == objlastworkflow.getWorkflowcode()) {
					objClass.setFinalworkflow(1);
					;
				} else {
					objClass.setFinalworkflow(0);
					;
				}
			}
			updatenotificationfororderworkflowapprovel(objClass);
			updatenotificationfororderworkflow(objClass, LsProto.getLsworkflow());

		} catch (Exception e) {
			mapObj.put("ERROR", e);
		}
		return mapObj;
	}

	private void updatenotificationfororderworkflowapprovel(LSlogilabprotocoldetail objClass) {
		String Details = "";
		String Notifiction = "";

		LSuserMaster obj = lsusermasterRepository.findByusercode(objClass.getLsuserMaster().getUsercode());
		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
		LSlogilabprotocoldetail LsProto = LSlogilabprotocoldetailRepository.findOne(objClass.getProtocolordercode());
		LSuserMaster createby = lsusermasterRepository.findByusercode(LsProto.getCreateby());
		LSnotification objnotify = new LSnotification();

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
								+ objClass.getProtoclordername() + "\", \"user\":\""
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
								+ objClass.getProtoclordername() + "\", \"user\":\""
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

	private void updatenotificationfororderworkflow(LSlogilabprotocoldetail objClass, LSworkflow previousworkflow) {

		String Details = "";
		String Notifiction = "";
		LSlogilabprotocoldetail LsProto = LSlogilabprotocoldetailRepository.findOne(objClass.getProtocolordercode());

//		LSuserMaster obj = lsusermasterRepository.findByusercode(objClass.getObjLoggeduser().getUsercode());

		LSusersteam objteam = lsusersteamRepository
				.findByteamcode(objClass.getLsprojectmaster().getLsusersteam().getTeamcode());
		String previousworkflowname = previousworkflow != null ? previousworkflow.getWorkflowname() : "";

		if (previousworkflowname.equals(objClass.getLsworkflow().getWorkflowname()) && LsProto.getApproved() == 1) {
			Notifiction = "PROTOCOLORDERFINALAPPROVAL";
		} else if (LsProto.getApproved() == 0 && objClass.getRejected() == null) {
			Notifiction = "PROTOCOLORDERAPPROVED";
		} else if (LsProto.getApproved() == 2 && objClass.getRejected() == null) {
			Notifiction = "PROTOCOLORRETURNED";
		} else if (LsProto.getRejected() == 1) {
			Notifiction = "PROTOCOLORDERREJECTED";
		}

		Details = "{\"ordercode\":\"" + objClass.getProtocolordercode() + "\", \"order\":\""
				+ objClass.getProtoclordername() + "\", \"previousworkflow\":\"" + previousworkflowname
				+ "\", \"currentworkflow\":\""
				+ objClass.getLsprotocolorderworkflowhistory().get(0).getLsworkflow().getWorkflowname()
				+ "\", \"currentworkflowcode\":\"" + objClass.getLsworkflow().getWorkflowcode() + "\"}";

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

		LSuserMaster obj = lsusermasterRepository.findByusercode(objClass.getLsuserMaster().getUsercode());
		LSuserMaster createby = lsusermasterRepository.findByusercode(objClass.getCreateby());
		if (objClass.getAssignedto() != null) {

			Notifiction = "PROTOCOLORDERCREATIONANDASSIGN";

			Details = "{\"ordercode\":\"" + objClass.getProtocolordercode() + "\", \"order\":\""
					+ objClass.getProtoclordername() + "\", \"previousworkflow\":\"" + "" + "\", \"assignedby\":\""
					+ objClass.getCreatedbyusername() + "\", \"previousworkflowcode\":\"" + -1
					+ "\", \"currentworkflow\":\"" + objClass.getLsworkflow().getWorkflowname()
					+ "\", \"currentworkflowcode\":\"" + objClass.getLsworkflow().getWorkflowcode() + "\"}";
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
							+ objClass.getProtoclordername() + "\", \"currentworkflow\":\""
							+ objClass.getLsworkflow().getWorkflowname() + "\", \"completeduser\":\""
							+ objClass.getLsuserMaster().getUsername() + "\"}";

				} else {
					Notifiction = "PROTOCOLORDERCREATION";

					Details = "{\"ordercode\":\"" + objClass.getProtocolordercode() + "\", \"order\":\""
							+ objClass.getProtoclordername() + "\", \"previousworkflow\":\"" + ""
							+ "\", \"previousworkflowcode\":\"" + -1 + "\", \"currentworkflow\":\""
							+ objClass.getLsworkflow().getWorkflowname() + "\", \"currentworkflowcode\":\""
							+ objClass.getLsworkflow().getWorkflowcode() + "\"}";
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

				if (lSlogilabprotocoldetail.getIsmultitenant() == 1) {
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

	public Map<String, Object> addProtocolOrder(LSlogilabprotocoldetail lSlogilabprotocoldetail) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
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

			LSlogilabprotocoldetailRepository.save(lSlogilabprotocoldetail);

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
				if(!lststep1.isEmpty()) {
					for (LSlogilabprotocolsteps LSprotocolstepObj1 : lststep1) {
	
						LSprotocolstepObj1.setModifiedusername(null);
						LSprotocolstepObj1.setProtocolordercode(lSlogilabprotocoldetail.getProtocolordercode());
						LSprotocolstepObj1.setOrderstepflag("N");
	//					LSprotocolstepObj1.setVersionno(0);
	
						LSlogilabprotocolstepsRepository.save(LSprotocolstepObj1);
	
						if (lSlogilabprotocoldetail.getIsmultitenant() == 1) {
							
							LSprotocolstepInformation lsprotocolstepInformation = lsprotocolstepInformationRepository
									.findById(LSprotocolstepObj1.getProtocolstepcode());
							if (lsprotocolstepInformation != null) {
								LSprotocolstepObj1.setLsprotocolstepInfo(lsprotocolstepInformation.getLsprotocolstepInfo());
							} else {
								Gson g = new Gson();
								LSprotocolstepObj1.setLsprotocolstepInfo(g.toJson(""));
							}
							CloudLsLogilabprotocolstepInfo CloudLSprotocolstepInfoObj = new CloudLsLogilabprotocolstepInfo();
							CloudLSprotocolstepInfoObj.setId(LSprotocolstepObj1.getProtocolorderstepcode());
							CloudLSprotocolstepInfoObj.setLsprotocolstepInfo(LSprotocolstepObj1.getLsprotocolstepInfo());
							CloudLsLogilabprotocolstepInfoRepository.save(CloudLSprotocolstepInfoObj);
	
							objinfo.add(CloudLSprotocolstepInfoObj);
	
	//						mapObj.put("CloudLsLogilabprotocolstepInfo", objinfo);
	
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
											String id = file.getFileid() + lSlogilabprotocoldetail.getProtoclordername();
	
											String con = LSprotocolstepObj1.getLsprotocolstepInfo();
											String finalinfo = con.replaceAll(file.getFileid(), id);
	
											LSprotocolstepObj1.setLsprotocolstepInfo(finalinfo);
										}
									}
	
								}
								if (objvideo.size() != 0) {
									for (LSprotocolvideos video : objvideo) {
										if (video.getFileid() != null) {
											String id = video.getFileid() + lSlogilabprotocoldetail.getProtoclordername();
	
											String con = LSprotocolstepObj1.getLsprotocolstepInfo();
											String finalinfo = con.replaceAll(video.getFileid(), id);
											LSprotocolstepObj1.setLsprotocolstepInfo(finalinfo);
										}
									}
								}
							}
							LsLogilabprotocolstepInfo LsLogilabprotocolstepInfoObj = new LsLogilabprotocolstepInfo();
	//						Gson g = new Gson();
	//						String str = g.toJson(LSprotocolstepObj1.getLsprotocolstepInfo());
							LsLogilabprotocolstepInfoObj.setId(LSprotocolstepObj1.getProtocolorderstepcode());
							LsLogilabprotocolstepInfoObj.setContent(LSprotocolstepObj1.getLsprotocolstepInfo());
							mongoTemplate.insert(LsLogilabprotocolstepInfoObj);
	
							objmongoinfo.add(LsLogilabprotocolstepInfoObj);
	
						}
	
					}
				} else {
					LSprotocolmaster lsprotocolmasterobj = LSProtocolMasterRepositoryObj.findByprotocolmastercode(
							lSlogilabprotocoldetail.getLsprotocolmaster().getProtocolmastercode());
					if (lSlogilabprotocoldetail.getIsmultitenant() == 1) {
						if (lsprotocolmasterobj.getContainerstored() == 0) {
	//							Content = cloudSheetCreationRepository.findById((long) objorder.getLsfile().getFilecode())
	//									.getContent();
						} else {
							try {
								Content = objCloudFileManipulationservice.retrieveCloudSheets(lsprotocolmasterobj.getFileuid(),
										TenantContext.getCurrentTenant() + "protocol");
								JSONObject protocolJson = new JSONObject(Content);
								protocolJson.put("protocolname", lSlogilabprotocoldetail.getProtoclordername());
								updateProtocolOrderContent(protocolJson.toString(), lSlogilabprotocoldetail, lSlogilabprotocoldetail.getIsmultitenant());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else {
						GridFSDBFile data = gridFsTemplate
								.findOne(new Query(Criteria.where("filename").is("protocol_" + lsprotocolmasterobj.getProtocolmastercode())));
						Content = new BufferedReader(
								new InputStreamReader(data.getInputStream(), StandardCharsets.UTF_8)).lines()
										.collect(Collectors.joining("\n"));
						if (gridFsTemplate.findOne(
								new Query(Criteria.where("filename").is("protocolorder_" + lSlogilabprotocoldetail.getProtocolordercode()))) == null) {
							try {
								gridFsTemplate.store(new ByteArrayInputStream(Content.getBytes(StandardCharsets.UTF_8)),
										"protocolorder_" + lSlogilabprotocoldetail.getProtocolordercode(), StandardCharsets.UTF_16);
							} catch (Exception e) {
								System.out.println(
										"error protocoldata lslogilabprotocoldetail content update mongodb" + lSlogilabprotocoldetail.getProtocolordercode());
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

				lSlogilabprotocoldetail
						.setLsworkflow(lsworkflowRepository.findTopByAndLssitemasterOrderByWorkflowcodeAsc(site));

				LSlogilabprotocoldetailRepository.save(lSlogilabprotocoldetail);

				lSlogilabprotocoldetail.setLstworkflow(lSlogilabprotocoldetail.getLstworkflow());
			}
			mapObj.put("AddedProtocol", lSlogilabprotocoldetail);
		}

		;
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
		return mapObj;
	}

	public void updateProtocolOrderContent(String Content, LSlogilabprotocoldetail objOrder, Integer ismultitenant) throws IOException {

		if (ismultitenant == 1) {

			Map<String, Object> objMap = objCloudFileManipulationservice.storecloudSheetsreturnwithpreUUID(Content,
					TenantContext.getCurrentTenant() + "protocolorder");
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

//		LSusergroup userGroup = LSusergroupRepository
//				.findOne(lSlogilabprotocoldetail.getObjuser().getMultiusergroupcode());

//		List<LSprotocolworkflowgroupmap> lsworkflowgroupmapping = LSprotocolworkflowgroupmapRepository
//				.findBylsusergroupAndWorkflowcodeNotNull(userGroup);

		List<LSworkflow> lstworkflow = lSlogilabprotocoldetail.getLstworkflow();

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

				if (lstworkflow != null && objorder.getLsworkflow() != null && lstworkflow.size() > 0) {
					// if(lstworkflow.contains(this.lsworkflow))

					List<Integer> lstprotocolworkflowcode = new ArrayList<Integer>();
					if (lstworkflow != null && lstworkflow.size() > 0) {
						lstprotocolworkflowcode = lstworkflow.stream().map(LSworkflow::getWorkflowcode)
								.collect(Collectors.toList());

						if (lstprotocolworkflowcode.contains(objorder.getLsworkflow().getWorkflowcode())) {
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

//			List<LSprotocolworkflowgroupmap> lsworkflowgroupmapping = LSprotocolworkflowgroupmapRepository
//					.findBylsusergroupAndWorkflowcodeNotNull(userGroup);

			List<LSworkflow> lstworkflow = lSlogilabprotocoldetail.getLstworkflow();

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

					if (lstworkflow != null && objorder.getLsworkflow() != null && lstworkflow.size() > 0) {
						// if(lstworkflow.contains(this.lsworkflow))

						List<Integer> lstprotocolworkflowcode = new ArrayList<Integer>();
						if (lstworkflow != null && lstworkflow.size() > 0) {
							lstprotocolworkflowcode = lstworkflow.stream().map(LSworkflow::getWorkflowcode)
									.collect(Collectors.toList());

							if (lstprotocolworkflowcode.contains(objorder.getLsworkflow().getWorkflowcode())) {
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
		if (LSprotocolstepObj.getIsmultitenant() == 1) {
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

			if (LSprotocolstepObj.getIsmultitenant() == 1) {

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
	
					if (multitenent == 1) {
	
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
	
	//				Collections.sort(LSprotocolorderversion, Collections.reverseOrder());
	
					LSprotocolstepLst.add(LSprotocolstepObj1);
	
				}
//			} else {
				if (multitenent == 1) {
					LSlogilabprotocoldetail lslogilabprotocoldetail = LSlogilabprotocoldetailRepository
							.findByProtocolordercode(ipInt);
					try {
						Content = objCloudFileManipulationservice.retrieveCloudSheets(lslogilabprotocoldetail.getFileuid(),
								TenantContext.getCurrentTenant() + "protocolorder");
						mapObj.put("protocolData", Content);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					LSlogilabprotocoldetail lslogilabprotocoldetail = LSlogilabprotocoldetailRepository
							.findByProtocolordercode(ipInt);
					GridFSDBFile largefile = gridFsTemplate.findOne(new Query(
							Criteria.where("filename").is("protocolorder_" + lslogilabprotocoldetail.getProtocolordercode())));
					mapObj.put("protocolData",new BufferedReader(
							new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
									.collect(Collectors.joining("\n")));
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
		LSuserMaster createby = lSuserMasterRepository.findByusercode(LSprotocolmasterrecord.getCreatedby());
		List<LSprotocolworkflowhistory> lsprotocolworkflowhistory = lsprotocolworkflowhistoryRepository
				.findByProtocolmastercode(lSprotocolmaster.getProtocolmastercode());
		List<LSprotocolupdates> modifiedlist = lsprotocolupdatesRepository
				.findByprotocolmastercode(lSprotocolmaster.getProtocolmastercode());

		List<LSprotocolversion> lsprotocolversion = lsprotocolversionRepository
				.findByprotocolmastercode(lSprotocolmaster.getProtocolmastercode());

		mapObj.put("createby", createby);
		mapObj.put("lsprotocolworkflowhistory", lsprotocolworkflowhistory);
		mapObj.put("modifiedlist", modifiedlist);
		mapObj.put("lsprotocolversion", lsprotocolversion);

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
			if (multitenent == 1) {
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
			if (multitenent == 1 && LSprotocolstepInformation != null) {
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

		if (ismultitenant == 1) {
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
			if (multitenent == 1) {
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
			if (multitenent == 1) {

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

			List<LSworkflowgroupmapping> lsworkflowgroupmapping = lsworkflowgroupmappingRepository
					.findBylsusergroupAndWorkflowcodeNotNull(userGroup);

			if (lsworkflowgroupmapping != null && lsworkflowgroupmapping.size() > 0) {
				List<LSworkflow> lsprotocolworkflow = lsworkflowRepository
						.findByLsworkflowgroupmappingInOrderByWorkflowcodeDesc(lsworkflowgroupmapping);

				rtobj.forEach(objorder -> objorder.setLstworkflow(lsprotocolworkflow));

			} else {
				rtobj.forEach(objorder -> objorder.setCanuserprocess(false));
			}

			return rtobj;
		}
		return rtobj;

	}

	public Map<String, Object> Getinitialorders(LSlogilabprotocoldetail objorder) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		if (objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
			mapOrders.put("orders", Getadministratororder(objorder));
			mapOrders.put("ordercount", LSlogilabprotocoldetailRepository.count());
		} else {

			List<Lsprotocolorderstructure> lstdir = new ArrayList<Lsprotocolorderstructure>();
			List<Long> directorycode = new ArrayList<Long>();
			if (objorder.getLstuserMaster().size() == 0) {
				lstdir = lsprotocolorderStructurerepository
						.findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
								objorder.getLsuserMaster().getLssitemaster(), 1, objorder.getLsuserMaster(), 2);
				directorycode = lstdir.stream().map(Lsprotocolorderstructure::getDirectorycode)
						.collect(Collectors.toList());
			} else {
				lstdir = lsprotocolorderStructurerepository
						.findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
								objorder.getLsuserMaster().getLssitemaster(), 1, objorder.getLsuserMaster(), 2,
								objorder.getLsuserMaster().getLssitemaster(), 3, objorder.getLstuserMaster());
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
		lstorders.forEach(objorderDetail -> objorderDetail.setLstworkflow(objorder.getLsuserMaster().getLstworkflow()));
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
			Integer protocolmastercode, Integer stepno, String protocolstepname, String originurl) {
		Map<String, Object> map = new HashMap<String, Object>();

		String id = null;
		try {
			id = cloudFileManipulationservice.storecloudfilesreturnUUID(file, "protocolfiles");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LSprotocolfiles objfile = new LSprotocolfiles();
		objfile.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
		objfile.setFileid(id);
		objfile.setProtocolmastercode(protocolmastercode);
		objfile.setProtocolstepcode(protocolstepcode);
		objfile.setProtocolstepname(protocolstepname);
		objfile.setStepno(stepno);
		objfile.setFilename(FilenameUtils.removeExtension(file.getOriginalFilename()));

		lsprotocolfilesRepository.save(objfile);

		map.put("link", originurl + "/protocol/downloadprotocolfile/" + objfile.getFileid() + "/"
				+ TenantContext.getCurrentTenant() + "/" + objfile.getFilename() + "/" + objfile.getExtension());
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
//		String url = originurl + "/protocol/downloadprotocolimage/" + objimg.getFileid() + "/"
//				+ TenantContext.getCurrentTenant() + "/" + objimg.getFilename() + "/" + objimg.getExtension();
//		Gson g = new Gson();
//		String str = g.toJson(url);
//		objimg.setSrc(str);
//		lsprotocolimagesRepository.save(objimg);
//		map.put("link", originurl + "/protocol/downloadprotocolimage/" + objimg.getFileid() + "/"
//				+ TenantContext.getCurrentTenant() + "/" + objimg.getFilename() + "/" + objimg.getExtension());

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

	public ByteArrayInputStream downloadprotocolfile(String fileid, String tenant) {

		byte[] data = null;
		try {
			data = StreamUtils
					.copyToByteArray(cloudFileManipulationservice.retrieveCloudFile(fileid, tenant + "protocolfiles"));
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
					if (ismultitenant == 1) {
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
					if (ismultitenant == 1) {
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
				if (ismultitenant == 1) {

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
				if (ismultitenant == 1) {
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
			LSprotocolmaster protocolMaster = LSProtocolMasterRepositoryObj.findByprotocolmastercode(lSprotocolmaster.getProtocolmastercode());
//			LSProtocolMasterRepositoryObj.save(lSprotocolmaster);
			Integer ismultitenant = object.convertValue(body.get("ismultitenant"), Integer.class);
			int sitecode = object.convertValue(body.get("sitecode"), Integer.class);
			
			if ((protocolMaster.getApproved() != null && protocolMaster.getApproved() == 1 )) {

				LSSiteMaster lssitemaster = LSSiteMasterRepository.findBysitecode(sitecode);
//				LSprotocolworkflow lsprotocolworkflow = lSprotocolworkflowRepository
//						.findTopByAndLssitemasterOrderByWorkflowcodeAsc(lssitemaster);.

				LSsheetworkflow lssheetworkflow = lssheetworkflowRepository
						.findTopByAndLssitemasterOrderByWorkflowcodeAsc(lssitemaster);

				protocolMaster.setApproved(0);
//				lSprotocolmaster.setlSprotocolworkflow(lsprotocolworkflow);
				protocolMaster.setLssheetworkflow(lssheetworkflow);
				protocolMaster.setVersionno(protocolMaster.getVersionno() + 1);

//				LSProtocolMasterRepositoryObj.save(lSprotocolmaster);
				List<LSprotocolmastertest> LSprotocolmastertest = protocolMaster.getLstest();
				for (LSprotocolmastertest test : LSprotocolmastertest) {
					test.setTestcode(null);
					LSprotocolmastertestRepository.save(test);
				}
			}
			
			if (!body.get("protocolData").equals("")) {
				
//				String str = object.convertValue(body.get("protocolData"), String.class);
				Gson gson = new Gson();
				String protocolDataJson = gson.toJson(body.get("protocolData"));
				try {
					protocolMaster.setLastmodified(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (ismultitenant == 1) {
					protocolMaster.setIsmultitenant(ismultitenant);
					commonservice.updateProtocolContent(protocolDataJson, protocolMaster);
				} else {
					
					GridFSDBFile largefile = gridFsTemplate
							.findOne(new Query(Criteria.where("filename").is("protocol_" + protocolMaster.getProtocolmastercode())));
					if (largefile != null) {
						gridFsTemplate.delete(new Query(Criteria.where("filename").is("protocol_" + protocolMaster.getProtocolmastercode())));
					}
					gridFsTemplate.store(new ByteArrayInputStream(protocolDataJson.getBytes(StandardCharsets.UTF_8)),
							"protocol_" + protocolMaster.getProtocolmastercode(), StandardCharsets.UTF_16);
					
				}
				response.setInformation("IDS_MSG_PROTOCOLSAVE");
				response.setStatus(true);
				lsProtocolMasterRepository.save(protocolMaster);
				mapObj.put("ProtocolMaster", lsProtocolMasterRepository.findByprotocolmastercode(protocolMaster.getProtocolmastercode()));
			}
		}
		mapObj.put("protocolData", body.get("protocolData"));
		mapObj.put("response", response);
		return mapObj;
	}
	
	
	public Map<String, Object> protocolOrderSave(Map<String, Object> body) throws IOException {

		Map<String, Object> mapObj = new HashMap<String, Object>();
		Response response = new Response();
		ObjectMapper object = new ObjectMapper();
		
		if (body.get("ProtocolOrder") != null) {
			LSlogilabprotocoldetail lSlogilabprotocoldetail = object.convertValue(body.get("ProtocolOrder"),
					LSlogilabprotocoldetail.class);
			LSlogilabprotocoldetail protocolOrder = LSlogilabprotocoldetailRepository.findByProtocolordercode(lSlogilabprotocoldetail.getProtocolordercode());
			Integer ismultitenant = object.convertValue(body.get("ismultitenant"), Integer.class);
			int sitecode = object.convertValue(body.get("sitecode"), Integer.class);
	
			if ((protocolOrder.getApproved() == null || protocolOrder.getApproved() != 1 )) {

//				LSSiteMaster lssitemaster = LSSiteMasterRepository.findBysitecode(sitecode);
//				LSworkflow lsworkflow = lsworkflowRepository.findTopByAndLssitemasterOrderByWorkflowcodeAsc(lssitemaster);

				protocolOrder.setApproved(0);
				protocolOrder.setLsworkflow(lSlogilabprotocoldetail.getLsworkflow());
				protocolOrder.setVersionno(protocolOrder.getVersionno() + 1);
			
				if (!body.get("protocolData").equals("")) {	
					Gson gson = new Gson();
					String protocolDataJson = gson.toJson(body.get("protocolData"));
					if (ismultitenant == 1) {
						protocolOrder.setIsmultitenant(ismultitenant);
						updateProtocolOrderContent(protocolDataJson, protocolOrder, ismultitenant);
					} else {
						GridFSDBFile largefile = gridFsTemplate
								.findOne(new Query(Criteria.where("filename").is("protocolorder_" + protocolOrder.getProtocolordercode())));
						if (largefile != null) {
							gridFsTemplate.delete(new Query(Criteria.where("filename").is("protocolorder_" + protocolOrder.getProtocolordercode())));
						}
						gridFsTemplate.store(new ByteArrayInputStream(protocolDataJson.getBytes(StandardCharsets.UTF_8)),
								"protocolorder_" + protocolOrder.getProtocolordercode(), StandardCharsets.UTF_16);
					}
					mapObj.put("protocolOrder", protocolOrder);
					response.setInformation("IDS_MSG_PROTOCOLORDERSAVE");
					response.setStatus(true);
				}
			}
		}
		if((boolean) body.get("ismaterialreduce")) {
			@SuppressWarnings("unchecked")
			Map<String, Object> mapObj11=(Map<String, Object>) body.get("materialinventory");
			transactionService.createMaterialResultUsedForList(mapObj11);
		}
		mapObj.put("protocolData", body.get("protocolData"));
		mapObj.put("response", response);
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
				if (ismultitenant == 1) {
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
				if (ismultitenant == 1) {
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

			if (ismultitenant == 1) {
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
				if (ismultitenant == 1) {
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

				if (ismultitenant == 1) {

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
			lsfiles = LSProtocolMasterRepositoryObj.findByLstestInAndStatusAndApproved(lsfiletest, 1,1);
		} else {

			List<Integer> lstteammap = LSuserteammappingRepositoryObj
					.getTeamcodeByLsuserMaster(objtest.getObjLoggeduser().getUsercode());
			if (lstteammap.size() > 0) {
				List<LSuserMaster> lstteamuser = LSuserteammappingRepositoryObj.getLsuserMasterByTeamcode(lstteammap);
				lstteamuser.add(objtest.getObjLoggeduser());
				lstteammap = lstteamuser.stream().map(LSuserMaster::getUsercode).collect(Collectors.toList());
				lsfiles = LSProtocolMasterRepositoryObj
						.findByLstestInAndStatusAndCreatedbyInAndViewoptionAndApprovedOrLstestInAndStatusAndCreatedbyAndViewoptionAndApprovedOrLstestInAndStatusAndCreatedbyInAndViewoptionAndApproved(
								lsfiletest, 1, lstteammap, 1, 1,
								lsfiletest, 1, objtest.getObjLoggeduser().getUsercode(),2, 1,
								lsfiletest, 1, lstteammap, 3,1);

			} else {
				lstteammap.add(objtest.getObjLoggeduser().getUsercode());
				lsfiles = LSProtocolMasterRepositoryObj
						.findByLstestInAndStatusAndCreatedbyInAndViewoptionAndApprovedOrLstestInAndStatusAndCreatedbyAndViewoptionAndApprovedOrLstestInAndStatusAndCreatedbyInAndViewoptionAndApproved(
								lsfiletest, 1, lstteammap, 1,1,
								lsfiletest, 1, objtest.getObjLoggeduser().getUsercode(),2,1,
								lsfiletest, 1, lstteammap, 3,1);

			}
			// lsfiles = LSProtocolMasterRepositoryObj.findByLstestInAndStatus(lsfiletest, 1);
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
			Integer protocolmastercode, Integer stepno, String protocolstepname, String originurl) throws IOException {

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
		objfile.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
		objfile.setFileid(Fieldid);
		objfile.setProtocolmastercode(protocolmastercode);
		objfile.setProtocolstepcode(protocolstepcode);
		objfile.setProtocolstepname(protocolstepname);
		objfile.setStepno(stepno);
		objfile.setFilename(FilenameUtils.removeExtension(file.getOriginalFilename()));

		lsprotocolfilesRepository.save(objfile);

		LSprotocolfilesContent objattachment = new LSprotocolfilesContent();
		objattachment.setId(objfile.getProtocolstepfilecode());
		objattachment.setName(objfile.getFilename());
		objattachment.setFileid(Fieldid);
		objattachment.setFile(new Binary(BsonBinarySubType.BINARY, file.getBytes()));

		objattachment = lsprotocolfilesContentRepository.insert(objattachment);

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
				.findByProtocolordercode(lsprotocolorderworkflowhistory.getProtocolordercode());
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
		if (lslogilabprotocolsteps.getIsmultitenant() == 1) {

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
				.findByProtocolordercodeAndStatus(lSlogilabprotocoldetail.getProtocolordercode(), 1);
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
		lstorder.forEach(objorderDetail -> objorderDetail.setLstworkflow(lSlogilabprotocoldetail.getLstworkflow()));
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
		List<LSworkflow> workflow = mapper.convertValue(objusers.get("workflow"),
				new TypeReference<List<LSworkflow>>() {
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
				sharebyme.forEach(objorderDetail -> objorderDetail.getProtocolorders().setLstworkflow(workflow));
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
				sharetome.forEach(objorderDetail -> objorderDetail.getProtocolorders().setLstworkflow(workflow));
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

			if (LSprotocolstepObj1.getIsmultitenant() == 1
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

				if (LSprotocolstepObj1.getIsmultitenant() == 1) {
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
			cloudStepVersion=null;
			protoVersStep = null;
			rtnobj.add(LSprotocolstep);
		}

		if (isversion) {
			if (argObj.get(0).getIsmultitenant() == 1) {
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
		if (body.getIsmultitenant() == 1) {
			try {
				updateCloudProtocolVersion(body.getProtocolmastercode(), body.getProtocolstepcode(), null, 0,
						body.getSitecode(), null, body.getCreatedbyusername(), body.getCreatedby(), null);

			} catch (Exception e) {
				// TODO: handle exception
			}
					} else {
						try {
							updateCloudProtocolVersiononSQL(body, body.getSitecode(), body.getCreatedbyusername(), body.getCreatedby());

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
		return body;
	}

	public List<LSprotocolorderstephistory> getprotocolstephistory(LSprotocolorderstephistory objuser) {
		if (objuser.getProtocolordercode() != null) {
			List<LSprotocolorderstephistory> rtobj = lsprotocolorderstephistoryRepository
					.findByProtocolordercodeOrderByProtocolorderstephistorycodeDesc(objuser.getProtocolordercode());
			return rtobj;
		} else if (objuser.getBatchcode() != null) {
			List<LSprotocolorderstephistory> rtobj = lsprotocolorderstephistoryRepository
					.findByBatchcode(objuser.getBatchcode());
			return rtobj;
		}
		return null;
	}

	public LSprotocolorderstephistory updatetransactionhistory(LSprotocolorderstephistory objuser) throws ParseException {
		if (objuser.getProtocolordercode() != null || objuser.getBatchcode() != null) {
			if(objuser.getStepstartdate()!=null) {
				objuser.setStepstartdate(commonfunction.getCurrentUtcTime());
			}else if(objuser.getStepskipeddate()!=null) {
				objuser.setStepskipeddate(commonfunction.getCurrentUtcTime());
			}else if(objuser.getStependdate()!=null) {
				objuser.setStependdate(commonfunction.getCurrentUtcTime());
			}
			lsprotocolorderstephistoryRepository.save(objuser);
		}
		return objuser;
	}

	public List<LSprotocolmaster> getsingleprotocol(LSprotocolmaster objuser) {

		return LSProtocolMasterRepositoryObj.findByProtocolmastercode(objuser.getProtocolmastercode());
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
		if (isMultitenant == 1) {
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
			List<ProtocolImage> protocolImage =new ArrayList<>();
			listofimg = gridFsFile.parallelStream().map(items -> {
				String fieldId = Generatetenantpassword();
				LSprotocolimages objImg = new LSprotocolimages();
				String filename="";
				if(onTabKey==1) {
					LSsheetfolderfiles LSsheetfolderfiles = lssheetfolderfilesRepository.findByUuid(items.getFilename());
					filename=LSsheetfolderfiles.getFilename()!=null?LSsheetfolderfiles.getFilename(): "No Name";
				}else {
					LSprotocolfolderfiles LSprotocolfolderfiles=lsprotocolfolderfilesRepository.findByUuid(items.getFilename());
					filename=LSprotocolfolderfiles.getFilename()!=null?LSprotocolfolderfiles.getFilename():"No Name";
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
		if(isMultitenant==1) {
		boolean isdone = cloudFileManipulationservice.tocopyoncontainertoanothercontainer(fileIds, sourceContainerName,
				destinationContainerName);
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
					objimg.setLink(originurl);
					return objimg;
				}).collect(Collectors.toList());

				lsprotocolfilesRepository.save(listofimg);
			}

		}
		}else {

			List<GridFSDBFile> gridFsFile = fileManipulationservice.retrieveLargeFileinlist(fileIds);
			List<LSprotocolfilesContent> protocolImage =new ArrayList<>();
			listofimg = gridFsFile.parallelStream().map(items -> {
				String fieldId = Generatetenantpassword();
				LSprotocolfiles objImg = new LSprotocolfiles();
				String filename="";
				if(onTabKey==1) {
					LSsheetfolderfiles LSsheetfolderfiles = lssheetfolderfilesRepository.findByUuid(items.getFilename());
					filename=LSsheetfolderfiles.getFilename()!=null?LSsheetfolderfiles.getFilename(): "No Name";
				}else {
					LSprotocolfolderfiles LSprotocolfolderfiles=lsprotocolfolderfilesRepository.findByUuid(items.getFilename());
					filename=LSprotocolfolderfiles.getFilename()!=null?LSprotocolfolderfiles.getFilename():"No Name";
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
			LSlogilabprotocoldetail lSlogilabprotocoldetail = object.convertValue(body.get("ProtocolOrder"), LSlogilabprotocoldetail.class);
			LSlogilabprotocoldetail protocolOrder = LSlogilabprotocoldetailRepository.findByProtocolordercode(lSlogilabprotocoldetail.getProtocolordercode());
	
			protocolOrder.setOrderstarted(1);
			protocolOrder.setOrderstartedby(lsusermasterRepository.findByusercode(object.convertValue(body.get("usercode"), Integer.class)));
			try {
				protocolOrder.setOrderstartedon(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LSlogilabprotocoldetailRepository.save(protocolOrder);
			Logilabprotocolorders lsprotocol = LSlogilabprotocoldetailRepository.findByProtocolordercodeOrderByProtocolordercodeDesc(protocolOrder.getProtocolordercode());
			mapObj.put("protocolOrder", lsprotocol);
			response.setInformation("IDS_MSG_PROTOCOLORDERSTART");
			response.setStatus(true);
		}
		
		mapObj.put("response", response);
		return mapObj;
	}
}