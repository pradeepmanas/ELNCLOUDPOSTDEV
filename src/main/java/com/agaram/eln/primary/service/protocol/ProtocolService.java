package com.agaram.eln.primary.service.protocol;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
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
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.config.CustomMultipartFile;
import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cloudFileManip.CloudUserSignature;
import com.agaram.eln.primary.model.cloudProtocol.CloudLSprotocolstepInfo;
import com.agaram.eln.primary.model.cloudProtocol.CloudLSprotocolversionstep;
import com.agaram.eln.primary.model.cloudProtocol.CloudLsLogilabprotocolstepInfo;
import com.agaram.eln.primary.model.cloudProtocol.LSprotocolstepInformation;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolordersharedby;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolordershareto;
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
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.cloudProtocol.CloudLSprotocolstepInfoRepository;
import com.agaram.eln.primary.repository.cloudProtocol.CloudLSprotocolversionstepRepository;
import com.agaram.eln.primary.repository.cloudProtocol.CloudLsLogilabprotocolstepInfoRepository;
import com.agaram.eln.primary.repository.cloudProtocol.LSprotocolstepInformationRepository;
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
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusergroupRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;
import com.agaram.eln.primary.service.basemaster.BaseMasterService;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

@Service
@EnableJpaRepositories(basePackageClasses = LSProtocolMasterRepository.class)
public class ProtocolService {

	@Autowired
	LSProtocolMasterRepository LSProtocolMasterRepositoryObj;

	@Autowired
	LSProtocolStepRepository LSProtocolStepRepositoryObj;

	@Autowired
	LSuserteammappingRepository LSuserteammappingRepositoryObj;

	@Autowired
	LSuserMasterRepository LSuserMasterRepositoryObj;

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
	private ProtocolordervideosRepository ProtocolordervideosRepository;

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
		List<LSprotocolmaster> LSprotocolmaster=new ArrayList<LSprotocolmaster>();	
		if(lstuser.size()!=0) {
		 LSprotocolmaster = LSProtocolMasterRepositoryObj
					.findByCreatedbyInAndStatusAndLssitemasterOrderByCreatedateDesc(lstuser, 1,
							objusers.getLssitemaster().getSitecode());
		}else {
			 LSprotocolmaster = LSProtocolMasterRepositoryObj
					.findByCreatedbyInAndStatusAndLssitemasterOrderByCreatedateDesc(objusers.getUsercode(), 1,
							objusers.getLssitemaster().getSitecode());
		}
	
		return LSprotocolmaster;
	}

	public Map<String, Object> getProtocolcount(LSuserMaster objusers) {

		Map<String, Object> mapObj = new HashMap<String, Object>();

		List<Integer> lstuser = objusers.getObjuser().getTeamuserscode();

		mapObj.put("templatecount",
				LSProtocolMasterRepositoryObj.countByCreatedbyInAndStatusAndLssitemasterOrderByCreatedateDesc(lstuser,
						1, objusers.getLssitemaster().getSitecode()));
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

	public Map<String, Object> getProtocolStepLst(Map<String, Object> argObj) {
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

		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if (argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			@SuppressWarnings("unused")
			LSprotocolmaster newProtocolMasterObj = new ObjectMapper().convertValue(argObj.get("ProtocolMasterObj"),
					new TypeReference<LSprotocolmaster>() {
					});
			List<LSprotocolstep> LSprotocolsteplst = LSProtocolStepRepositoryObj.findByStatusAndSitecode(1,
					LScfttransactionobj.getLssitemaster());
			List<LSprotocolstep> LSprotocolstepLstUpdate = new ArrayList<LSprotocolstep>();
			for (LSprotocolstep LSprotocolstepObj1 : LSprotocolsteplst) {
				/**
				 * Added by sathishkumar chandrasekar for smultitenant
				 * 
				 */
				if ((int) argObj.get("ismultitenant") == 1) {
					CloudLSprotocolstepInfo newLSprotocolstepInfo = CloudLSprotocolstepInfoRepository
							.findById(LSprotocolstepObj1.getProtocolstepcode());
					if (newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
					}
				} else {
					LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate
							.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
					if (newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
					}
				}
//				LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
//				if(newLSprotocolstepInfo != null) {
//					LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
//				}
				LSprotocolstepLstUpdate.add(LSprotocolstepObj1);
//				LSprotocolstepObj1.setLsprotocolstepInfo(mongoTemplate.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class).getContent());
			}
			if (LSprotocolsteplst != null) {
				mapObj.put("protocolstepLst", LSprotocolstepLstUpdate);
			} else {
				mapObj.put("protocolstepLst", new ArrayList<>());
			}
		}
		return mapObj;
	}

	public Map<String, Object> getOrdersLinkedToProtocol(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		@SuppressWarnings("unused")
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if (argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			@SuppressWarnings("unchecked")
			ArrayList<Integer> protocolmastercodeArray = (ArrayList<Integer>) argObj.get("protocolmastercodeArray");
			List<LSprotocolmaster> protocolmasterArray = LSProtocolMasterRepositoryObj
					.findByProtocolmastercodeIn(protocolmastercodeArray);
			List<LSlogilabprotocoldetail> LSlogilabprotocoldetailLst = LSlogilabprotocoldetailRepository
					.findByLsprotocolmaster(protocolmasterArray);
			if (LSlogilabprotocoldetailLst != null) {
				mapObj.put("LSlogilabprotocoldetailLst", LSlogilabprotocoldetailLst);
			} else {
				mapObj.put("LSlogilabprotocoldetailLst", new ArrayList<>());
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

				updateCloudProtocolVersion(LSprotocolstepObj.getProtocolmastercode(),
						LSprotocolstepObj.getProtocolstepcode(), LSprotocolstepObj.getLsprotocolstepInfo(),
						LSprotocolstepObj.getNewStep(), LScfttransactionobj.getLssitemaster(), LSprotocolstepObj);

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
				updateCloudProtocolVersiononSQL(LSprotocolstepObj, LScfttransactionobj.getLssitemaster());

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
			if (argObj.containsKey("repositorydata")) {
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

	private void updateCloudProtocolVersiononSQL(LSprotocolstep lSprotocolstepObj, Integer sitecode) {

		LSprotocolmaster protocolMaster = LSProtocolMasterRepositoryObj
				.findByprotocolmastercode(lSprotocolstepObj.getProtocolmastercode());
		List<LSprotocolstep> lststep = LSProtocolStepRepositoryObj
				.findByProtocolmastercode(lSprotocolstepObj.getProtocolmastercode());

		if (protocolMaster.getApproved() != null && protocolMaster.getApproved() == 1) {

			LSSiteMaster lssitemaster = LSSiteMasterRepository.findBysitecode(sitecode);
			LSprotocolworkflow lsprotocolworkflow = lSprotocolworkflowRepository
					.findTopByAndLssitemasterOrderByWorkflowcodeAsc(lssitemaster);

			protocolMaster.setApproved(0);
			protocolMaster.setlSprotocolworkflow(lsprotocolworkflow);
			protocolMaster.setVersionno(protocolMaster.getVersionno() + 1);

			LSProtocolMasterRepositoryObj.save(protocolMaster);

			int i = 0;
			List<LSprotocolstepversion> lstVersStep = new ArrayList<LSprotocolstepversion>();

			while (i < lststep.size()) {

				LSprotocolstepversion protoVersStep = new LSprotocolstepversion();

				protoVersStep.setProtocolmastercode(lststep.get(i).getProtocolmastercode());
				protoVersStep.setProtocolstepcode(lststep.get(i).getProtocolstepcode());
				protoVersStep.setProtocolstepname(lststep.get(i).getProtocolstepname());
				protoVersStep.setStatus(lststep.get(i).getStatus());
				protoVersStep.setStepno(lststep.get(i).getStepno());
				protoVersStep.setVersionno(protocolMaster.getVersionno());

				lstVersStep.add(protoVersStep);

				i++;
			}

			LSprotocolstepversionRepository.save(lstVersStep);

			i = 0;

			while (i < lstVersStep.size()) {

				if (lstVersStep.get(i).getProtocolstepcode().equals(lSprotocolstepObj.getProtocolstepcode())) {

					LSprotocolversionstepInfo LsLogilabprotocolstepInfoObj = new LSprotocolversionstepInfo();

					LsLogilabprotocolstepInfoObj.setId(lstVersStep.get(i).getProtocolstepversioncode());
					LsLogilabprotocolstepInfoObj.setStepcode(lSprotocolstepObj.getProtocolstepcode());
					LsLogilabprotocolstepInfoObj.setContent(lSprotocolstepObj.getLsprotocolstepInfo());
					LsLogilabprotocolstepInfoObj.setVersionno(protocolMaster.getVersionno());

					mongoTemplate.insert(LsLogilabprotocolstepInfoObj);
				}
				i++;
			}

			LSprotocolversion versProto = new LSprotocolversion();

			versProto.setProtocolmastercode(lSprotocolstepObj.getProtocolmastercode());
			versProto.setProtocolmastername(protocolMaster.getProtocolmastername());
			versProto.setProtocolstatus(1);
			versProto.setVersionno(protocolMaster.getVersionno());
			versProto.setVersionname("version_" + protocolMaster.getVersionno());

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
				LsLogilabprotocolstepInfoObj.setContent(lSprotocolstepObj.getLsprotocolstepInfo());
				LsLogilabprotocolstepInfoObj.setVersionno(protocolMaster.getVersionno());

				mongoTemplate.insert(LsLogilabprotocolstepInfoObj);
			} else {

				LSprotocolstepversion protocolStep = LSprotocolstepversionRepository.findByprotocolstepcodeAndVersionno(
						lSprotocolstepObj.getProtocolstepcode(), protocolMaster.getVersionno());
				if (protocolStep != null) {
					Query query = new Query(Criteria.where("id").is(protocolStep.getProtocolstepversioncode()));

					Update update = new Update();
					update.set("content", lSprotocolstepObj.getLsprotocolstepInfo());

					mongoTemplate.upsert(query, update, LSprotocolversionstepInfo.class);
				}
			}
		}
	}

	private void updateCloudProtocolVersion(Integer protocolmastercode, Integer protocolstepcode,
			String lsprotocolstepInfo, Integer newStep, Integer sitecode, LSprotocolstep lSprotocolstepObj) {

		LSprotocolmaster protocolMaster = LSProtocolMasterRepositoryObj.findByprotocolmastercode(protocolmastercode);
		List<LSprotocolstep> lststep = LSProtocolStepRepositoryObj.findByProtocolmastercode(protocolmastercode);

		if (protocolMaster.getApproved() != null && protocolMaster.getApproved() == 1) {

			LSSiteMaster lssitemaster = LSSiteMasterRepository.findBysitecode(sitecode);
			LSprotocolworkflow lsprotocolworkflow = lSprotocolworkflowRepository
					.findTopByAndLssitemasterOrderByWorkflowcodeAsc(lssitemaster);

			protocolMaster.setApproved(0);
			protocolMaster.setlSprotocolworkflow(lsprotocolworkflow);
			protocolMaster.setVersionno(protocolMaster.getVersionno() + 1);

			LSProtocolMasterRepositoryObj.save(protocolMaster);

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
					CloudLSprotocolstepInfo newLSprotocolstepInfo = CloudLSprotocolstepInfoRepository
							.findById(protocolstepcode);
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

			lsprotocolversionRepository.save(versProto);

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

		List<LSprotocolstep> updateLSprotocolstepLst = new ObjectMapper().convertValue(argObj.get("protocolstepLst"),
				new TypeReference<List<LSprotocolstep>>() {
				});
		for (LSprotocolstep LSprotocolstepObj1 : updateLSprotocolstepLst) {
			LSProtocolStepRepositoryObj.save(LSprotocolstepObj1);
		}

		List<LSprotocolstep> tempLSprotocolstepLst = LSProtocolStepRepositoryObj
				.findByProtocolmastercodeAndStatus((Integer) argObj.get("protocolmastercode"), 1);
		List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();
		for (LSprotocolstep LSprotocolstepObj1 : tempLSprotocolstepLst) {
			/**
			 * Added by sathishkumar chandrasekar for multitenant
			 */
			if (deleteprotocolstep.getIsmultitenant() == 1) {

				LSprotocolstepInformation newobj = lsprotocolstepInformationRepository
						.findById(LSprotocolstepObj1.getProtocolstepcode());
				if (newobj != null) {
					LSprotocolstepObj1.setLsprotocolstepInformation(newobj.getLsprotocolstepInfo());
				}

//				CloudLSprotocolstepInfo newLSprotocolstepInfo = CloudLSprotocolstepInfoRepository
//						.findById(LSprotocolstepObj1.getProtocolstepcode());
//				if (newLSprotocolstepInfo != null) {
//					LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
//				}
			} else {
				LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate
						.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
				if (newLSprotocolstepInfo != null) {
					LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
				}
			}

			LSprotocolstepLst.add(LSprotocolstepObj1);
		}
		mapObj.put("protocolstepLst", LSprotocolstepLst);

		return mapObj;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public Map<String, Object> addProtocolMaster(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if (argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
		}
		ObjectMapper objMapper = new ObjectMapper();
		Response response = new Response();

		/**
		 * // silent audit if (LScfttransactionobj != null) {
		 * LScfttransactionobj.setTableName("LSprotocolmaster"); if
		 * (argObj.containsKey("username")) { String username =
		 * objMapper.convertValue(argObj.get("username"), String.class);
		 * 
		 * LSSiteMaster objsite =
		 * LSSiteMasterRepository.findBysitecode(LScfttransactionobj.getLssitemaster());
		 * LSuserMaster objuser =
		 * LSuserMasterRepositoryObj.findByusernameAndLssitemaster(username, objsite);
		 * LScfttransactionobj.setLsuserMaster(objuser.getUsercode());
		 * LScfttransactionobj.setLssitemaster(objuser.getLssitemaster().getSitecode());
		 * LScfttransactionobj.setUsername(username); }
		 * lscfttransactionRepository.save(LScfttransactionobj); } // manual audit if
		 * (argObj.containsKey("objuser")) { Map<String, Object> mapObjUser =
		 * (Map<String, Object>) argObj.get("objuser");
		 * 
		 * if (argObj.containsKey("objmanualaudit")) { LScfttransaction objmanualaudit =
		 * new LScfttransaction(); objmanualaudit =
		 * objMapper.convertValue(argObj.get("objmanualaudit"), LScfttransaction.class);
		 * 
		 * objmanualaudit.setComments((String) mapObjUser.get("comments"));
		 * lscfttransactionRepository.save(objmanualaudit); } }
		 */

		if (argObj.containsKey("newProtocolMasterObj")) {
			LSuserMaster LsuserMasterObj = LSuserMasterRepositoryObj
					.findByusercode(LScfttransactionobj.getLsuserMaster());
			LSprotocolmaster newProtocolMasterObj = new LSprotocolmaster();
			if (argObj.containsKey("edit")) {

				int protocolmastercode = new ObjectMapper().convertValue(argObj.get("protocolmastercode"),
						Integer.class);
				newProtocolMasterObj = LSProtocolMasterRepositoryObj
						.findFirstByProtocolmastercodeAndStatusAndLssitemaster(protocolmastercode, 1,
								LScfttransactionobj.getLssitemaster());
				newProtocolMasterObj.setProtocolmastername((String) argObj.get("protocolmastername"));
//				newProtocolMasterObj.setVersionno(newProtocolMasterObj.getVersionno() + 1);
				newProtocolMasterObj.setIsmultitenant((Integer) argObj.get("ismultitenant"));
				@SuppressWarnings("unused")
				Object LSprotocolupdates = new LSprotocolupdates();
				Map<String, Object> argObj1 = new HashMap<String, Object>();
				argObj1 = (Map<String, Object>) argObj.get("LSprotocolupdates");
//				UpdateProtocolversion(newProtocolMasterObj, argObj1, LSprotocolupdates.class);
			} else {
				newProtocolMasterObj.setProtocolmastername((String) argObj.get("protocolmastername"));
				newProtocolMasterObj.setProtocolstatus((Integer) argObj.get("protocolstatus"));
				newProtocolMasterObj.setStatus((Integer) argObj.get("status"));
				newProtocolMasterObj.setCreatedby((Integer) argObj.get("createdby"));
				newProtocolMasterObj.setIsmultitenant((Integer) argObj.get("ismultitenant"));

				newProtocolMasterObj.setCreatedate(new Date());
				newProtocolMasterObj.setLssitemaster(LScfttransactionobj.getLssitemaster());
				newProtocolMasterObj.setCreatedbyusername(LsuserMasterObj.getUsername());
				newProtocolMasterObj.setVersionno(0);
				LSSiteMaster lssitemaster = LSSiteMasterRepository
						.findBysitecode(LScfttransactionobj.getLssitemaster());
				LSprotocolworkflow lsprotocolworkflow = lSprotocolworkflowRepository
						.findTopByAndLssitemasterOrderByWorkflowcodeAsc(lssitemaster);
				newProtocolMasterObj.setlSprotocolworkflow(lsprotocolworkflow);
			}
			LSProtocolMasterRepositoryObj.save(newProtocolMasterObj);

			if (argObj.containsKey("edit")) {
			} else {
				LSprotocolversion versProto = new LSprotocolversion();

				versProto.setProtocolmastercode(newProtocolMasterObj.getProtocolmastercode());
				versProto.setProtocolmastername(newProtocolMasterObj.getProtocolmastername());
				versProto.setProtocolstatus(1);
				versProto.setVersionno(newProtocolMasterObj.getVersionno());
				versProto.setVersionname("version_" + newProtocolMasterObj.getVersionno());

				lsprotocolversionRepository.save(versProto);
			}

//			List<LSprotocolmaster> LSprotocolmasterLst = LSProtocolMasterRepositoryObj
//					.findByCreatedbyAndStatusAndLssitemaster(LScfttransactionobj.getLsuserMaster(), 1,
//							LScfttransactionobj.getLssitemaster());
			List<LSprotocolmaster> AddedLSprotocolmasterObj = LSProtocolMasterRepositoryObj
					.findByStatusAndLssitemasterAndProtocolmastername(1, LScfttransactionobj.getLssitemaster(),
							newProtocolMasterObj.getProtocolmastername());

			LSprotocolworkflow lsprotocolworkflow = new LSprotocolworkflow();
			List<LSprotocolmaster> LSprotocolmasterLst=new ArrayList<LSprotocolmaster>();	
			List<Integer> lstuser = new ObjectMapper().convertValue(argObj.get("teamuserscode"),ArrayList.class);
			if(lstuser.size()!=0) {
				 LSprotocolmasterLst = LSProtocolMasterRepositoryObj
							.findByCreatedbyInAndStatusAndLssitemasterOrderByCreatedateDesc(lstuser, 1,
									LScfttransactionobj.getLssitemaster());
				}else {
					int usercode = objMapper.convertValue(argObj.get("usercode"), Integer.class);
					LSprotocolmasterLst = LSProtocolMasterRepositoryObj
							.findByCreatedbyInAndStatusAndLssitemasterOrderByCreatedateDesc(usercode, 1,
									LScfttransactionobj.getLssitemaster());
				}

			if (argObj.containsKey("multiusergroups")) {

//				LSusergroup lsusergroup = new LSusergroup();
////				LSMultiusergroup objLSMultiusergroup = new LSMultiusergroup();
////				int multiusergroupscode = new ObjectMapper().convertValue(argObj.get("multiusergroups"), Integer.class);
////				objLSMultiusergroup = LSMultiusergroupRepositery.findBymultiusergroupcode(multiusergroupscode);
////				lsusergroup = objLSMultiusergroup.getLsusergroup();

				int lsusergroupcode = objMapper.convertValue(argObj.get("multiusergroups"), Integer.class);

				LSusergroup lsusergroup = LSusergroupRepository.findOne(lsusergroupcode);

				List<LSprotocolworkflowgroupmap> lsprotocolworkflowgroupmap = LSprotocolworkflowgroupmapRepository
						.findBylsusergroupAndWorkflowcodeNotNull(lsusergroup);

				if (lsprotocolworkflowgroupmap != null && lsprotocolworkflowgroupmap.size() > 0) {
					lsprotocolworkflow = lSprotocolworkflowRepository
							.findByworkflowcode(lsprotocolworkflowgroupmap.get(0).getWorkflowcode());

//					List<LSprotocolmaster> LSprotocolmasterLst1 = LSProtocolMasterRepositoryObj
//							.findByStatusAndLssitemasterAndLSprotocolworkflowAndCreatedbyNot(1,
//									LScfttransactionobj.getLssitemaster(), lsprotocolworkflow,
//									LScfttransactionobj.getLsuserMaster());
//
//					LSprotocolmasterLst.addAll(LSprotocolmasterLst1);
//
//					List<LSprotocolmaster> LSprotocolmasterLst2 = LSProtocolMasterRepositoryObj
//							.findByStatusAndLssitemasterAndLSprotocolworkflowNotAndCreatedbyNotAndSharewithteamAndApproved(
//									1, LScfttransactionobj.getLssitemaster(), lsprotocolworkflow,
//									LScfttransactionobj.getLsuserMaster(), 0, 0);
//
//					LSprotocolmasterLst.addAll(LSprotocolmasterLst2);
				}
			}
			List<LSprotocolstep> LSprotocolsteplst = LSProtocolStepRepositoryObj
					.findByProtocolmastercodeAndStatus(newProtocolMasterObj.getProtocolmastercode(), 1);
			List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();
			for (LSprotocolstep LSprotocolstepObj1 : LSprotocolsteplst) {
				if (newProtocolMasterObj.getIsmultitenant() == 1) {
//					CloudLSprotocolstepInfo newLSprotocolstepInfo = CloudLSprotocolstepInfoRepository
//							.findById(LSprotocolstepObj1.getProtocolstepcode());
//					if (newLSprotocolstepInfo != null) {
//						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
//					}
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
					}
				}
				LSprotocolstepLst.add(LSprotocolstepObj1);
			}
			if (LSprotocolsteplst != null) {
				mapObj.put("protocolstepLst", LSprotocolstepLst);
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

//				if (newProtocolMasterObj.getIsmultitenant() == 1) {
//					CloudLSprotocolversionstep.setId(newProtocolMasterObj.getProtocolmastercode());
//					CloudLSprotocolversionstep.setLsprotocolstepInfo(versiondetails);
//					CloudLSprotocolversionstep.setStatus(newProtocolMasterObj.getStatus());
//					CloudLSprotocolversionstep.setVersionno(lsprotocolversion.getProtocolversioncode());
//					CloudLSprotocolversionstepRepository.save(CloudLSprotocolversionstep);
//				}
//
//				else {
//					Query query = new Query(Criteria.where("id").is(lsprotocolversion.getProtocolversioncode()));
//					Update update = new Update();
//					update.set("content", versiondetails);
//
//					mongoTemplate.upsert(query, update, LSprotocolversionstepInfo.class);
//				}
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
			mapObj.put("response", response);

		}

		return mapObj;
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

	public Map<String, Object> deleteProtocolMaster(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();

		LScfttransaction LScfttransactionobj = new LScfttransaction();

		if (argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
		}

		Response response = new Response();
//		silent audit
//		if (LScfttransactionobj != null) {
//			LScfttransactionobj.setTableName("LSprotocolmaster");
//			if (argObj.containsKey("username")) {
//				String username = objMapper.convertValue(argObj.get("username"), String.class);
//				LSSiteMaster objsite = LSSiteMasterRepository.findBysitecode(LScfttransactionobj.getLssitemaster());
//				LSuserMaster objuser = LSuserMasterRepositoryObj.findByusernameAndLssitemaster(username, objsite);
//				LScfttransactionobj.setLsuserMaster(objuser.getUsercode());
////				cfttransaction.setLssitemaster(objuser.getLssitemaster());
//				LScfttransactionobj.setLssitemaster(objuser.getLssitemaster().getSitecode());
//				LScfttransactionobj.setUsername(username);
//			}
//			lscfttransactionRepository.save(LScfttransactionobj);
//		}
////		manual audit
//		if (argObj.containsKey("objuser")) {
////			objUser=objMapper.convertValue(argObj.get("objuser"), LoggedUser.class);
//			Map<String, Object> mapObjUser = (Map<String, Object>) argObj.get("objuser");
//
//			if (argObj.containsKey("objmanualaudit")) {
//				LScfttransaction objmanualaudit = new LScfttransaction();
//				objmanualaudit = objMapper.convertValue(argObj.get("objmanualaudit"), LScfttransaction.class);
//
//				objmanualaudit.setComments((String) mapObjUser.get("comments"));
//				lscfttransactionRepository.save(objmanualaudit);
//			}
//		}
//			if(argObj.containsKey("ProtocolMasterObj")) {
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

//				LSusergroup lsusergroup = new ObjectMapper().convertValue(argObj.get("lsusergroup"), new TypeReference<LSusergroup>)
				LSusergroup lsusergroup = new ObjectMapper().convertValue(argObj.get("lsusergroup"),
						new TypeReference<LSusergroup>() {
						});
//				LSMultiusergroup objLSMultiusergroup = new LSMultiusergroup();
//				int multiusergroupscode = new ObjectMapper().convertValue(argObj.get("multiusergroups"), Integer.class);
//				objLSMultiusergroup = LSMultiusergroupRepositery.findBymultiusergroupcode(multiusergroupscode);
//				lsusergroup = objLSMultiusergroup.getLsusergroup();

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

		LSProtocolMasterRepositoryObj.updateFileWorkflow(objClass.getlSprotocolworkflow(), approved,
				objClass.getRejected(), objClass.getProtocolmastercode());

		LSprotocolmaster LsProto = LSProtocolMasterRepositoryObj
				.findFirstByProtocolmastercode(objClass.getProtocolmastercode());

		LsProto.setlSprotocolworkflow(objClass.getlSprotocolworkflow());
		if (LsProto.getApproved() == null) {
			LsProto.setApproved(0);
		}
		lsprotocolworkflowhistoryRepository.save(objClass.getLsprotocolworkflowhistory());
		mapObj.put("ProtocolObj", LsProto);
		mapObj.put("status", "success");

		return mapObj;
	}

	public Map<String, Object> updateworkflowforProtocolorder(LSlogilabprotocoldetail objClass) {

		Map<String, Object> mapObj = new HashMap<String, Object>();

		int approved = 0;

		if (objClass.getApproved() != null) {
			approved = objClass.getApproved();
		}

		LSlogilabprotocoldetailRepository.updateFileWorkflow(objClass.getlSprotocolworkflow(), approved,
				objClass.getRejected(), objClass.getProtocolordercode());

		LSlogilabprotocoldetail LsProto = LSlogilabprotocoldetailRepository.findOne(objClass.getProtocolordercode());

		LsProto.setlSprotocolworkflow(objClass.getlSprotocolworkflow());
		if (LsProto.getApproved() == null) {
			LsProto.setApproved(0);
		}
		lsprotocolorderworkflowhistoryRepository.save(objClass.getLsprotocolorderworkflowhistory());
		mapObj.put("ProtocolObj", LsProto);
		mapObj.put("status", "success");

		return mapObj;
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
		if (lSlogilabprotocoldetail.getProtocolordercode() != null) {
//		List<LSprotocolstep> lstSteps = LSProtocolStepRepositoryObj.findByProtocolmastercode(
//				lSlogilabprotocoldetail.getLsprotocolmaster().getProtocolmastercode());

//		List<LSlogilabprotocolsteps> lststep1 = new ObjectMapper().convertValue(lstSteps,
//				new TypeReference<List<LSlogilabprotocolsteps>>() {
//				});
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
					
					updateCloudProtocolorderVersion(lSlogilabprotocoldetail,LSprotocolstepObj1);
					
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
		}
		return null;

	}
	
	void updateCloudProtocolorderVersion(LSlogilabprotocoldetail lslogilabprotocoldetail,LSlogilabprotocolsteps LSprotocolstepObj1){
		if (lslogilabprotocoldetail.getApproved() != null && lslogilabprotocoldetail.getApproved() == 1) {
			
		}else {
			
		}
	}

	public Map<String, Object> addProtocolOrder(LSlogilabprotocoldetail lSlogilabprotocoldetail) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		if (lSlogilabprotocoldetail != null) {

			LSlogilabprotocoldetailRepository.save(lSlogilabprotocoldetail);

			if (lSlogilabprotocoldetail.getProtocolordercode() != null) {

				String ProtocolOrderName = "ELN" + lSlogilabprotocoldetail.getProtocolordercode();

				lSlogilabprotocoldetail.setProtoclordername(ProtocolOrderName);

				lSlogilabprotocoldetail.setOrderflag("N");
				if (lSlogilabprotocoldetail.getAssignedto() == null) {
					lSlogilabprotocoldetail.setLsuserMaster(null);
				}

				List<LSprotocolstep> lstSteps = LSProtocolStepRepositoryObj.findByProtocolmastercode(
						lSlogilabprotocoldetail.getLsprotocolmaster().getProtocolmastercode());

				List<LSlogilabprotocolsteps> lststep1 = new ObjectMapper().convertValue(lstSteps,
						new TypeReference<List<LSlogilabprotocolsteps>>() {
						});

				for (LSlogilabprotocolsteps LSprotocolstepObj1 : lststep1) {

					LSprotocolstepObj1.setModifiedusername(null);
					LSprotocolstepObj1.setProtocolordercode(lSlogilabprotocoldetail.getProtocolordercode());
					LSprotocolstepObj1.setOrderstepflag("N");

					LSlogilabprotocolstepsRepository.save(LSprotocolstepObj1);

//					List<LSprotocolimages> objimg = new ArrayList<>();
//					List<LSprotocolfiles> objfile = new ArrayList<>();
//					objfile = lsprotocolfilesRepository
//							.findByProtocolstepcode(LSprotocolstepObj1.getProtocolstepcode());
//					objimg = lsprotocolimagesRepository
//							.findByProtocolstepcode(LSprotocolstepObj1.getProtocolstepcode());
//					if (lSlogilabprotocoldetail.getIsmultitenant() == 1) {
//						try {
//							if (objimg.size() != 0) {
//								updateprotocolorderimages(objimg, "protocolorderimages", "protocolimages",
//										lSlogilabprotocoldetail, LSprotocolstepObj1);
//							}
//							if (objfile.size() != 0) {
//								updateprotocolorderfile(objfile, "protocolorderfiles", "protocolfiles",
//										lSlogilabprotocoldetail, LSprotocolstepObj1);
//							}
//						} catch (IOException e) {
//
//							e.printStackTrace();
//						}
//					} else {
//						if (objimg.size() != 0) {
//							updateprotocolorderimagesforsql(objimg, lSlogilabprotocoldetail, LSprotocolstepObj1);
//						}
//						if (objfile.size() != 0) {
//							updateprotocolorderfileforsql(objfile, lSlogilabprotocoldetail, LSprotocolstepObj1);
//						}
//					}

					if (lSlogilabprotocoldetail.getIsmultitenant() == 1) {

						LSprotocolstepInformation lsprotocolstepInformation = lsprotocolstepInformationRepository
								.findById(LSprotocolstepObj1.getProtocolstepcode());
						if (lsprotocolstepInformation != null && lSlogilabprotocoldetail.getProtocoltype() == 1) {
							String stepinfo = lsprotocolstepInformation.getLsprotocolstepInfo();
							stepinfo = stepinfo.replaceAll("<p>", "<p contenteditable='false'>");
							String stepinfono = stepinfo.replaceAll("<p contenteditable='false'><br></p>",
									"<p><br></p>");
						
							LSprotocolstepObj1.setLsprotocolstepInfo(stepinfono);
						} else {
							LSprotocolstepObj1.setLsprotocolstepInfo(lsprotocolstepInformation.getLsprotocolstepInfo());
						}

						CloudLsLogilabprotocolstepInfo CloudLSprotocolstepInfoObj = new CloudLsLogilabprotocolstepInfo();
						CloudLSprotocolstepInfoObj.setId(LSprotocolstepObj1.getProtocolorderstepcode());
						CloudLSprotocolstepInfoObj.setLsprotocolstepInfo(LSprotocolstepObj1.getLsprotocolstepInfo());
						CloudLsLogilabprotocolstepInfoRepository.save(CloudLSprotocolstepInfoObj);
					} else {
						LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate
								.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
//		
						@SuppressWarnings("unchecked")
						List<LSprotocolorderimages> lsprotocolorderimages = lsprotocolorderimagesRepository
								.findByProtocolordercodeAndProtocolorderstepcode(
										lSlogilabprotocoldetail.getProtocolordercode(),
										LSprotocolstepObj1.getProtocolorderstepcode());

						List<LSprotocolorderfiles> lsprotocolorderfile = lsprotocolorderfilesRepository
								.findByProtocolordercodeAndProtocolorderstepcodeOrderByProtocolorderstepfilecodeDesc(
										lSlogilabprotocoldetail.getProtocolordercode(),
										LSprotocolstepObj1.getProtocolorderstepcode());
						if (newLSprotocolstepInfo != null) {
							LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
						}
//						String stepinfo = newLSprotocolstepInfo.getContent();
						String jsonObject = new JsonParser().parse(newLSprotocolstepInfo.getContent()).getAsString();
						String stepinfono = "";
						if (lSlogilabprotocoldetail.getProtocoltype() == 1) {
							String stepinfo = jsonObject.replaceAll("<p>", "<p contenteditable='false' >");
							stepinfono = stepinfo.replaceAll("<p contenteditable='false' ><br></p>", "<p><br></p>");
						} else {
							stepinfono = jsonObject;
						}
						if (lsprotocolorderimages.size() != 0) {
							for (LSprotocolorderimages LSprotocolorderimages : lsprotocolorderimages) {
								String finalinfo = stepinfono.replaceAll(LSprotocolorderimages.getOldfileid(),
										LSprotocolorderimages.getFileid());
//							lslogilabprotocolsteps.setLsprotocolstepInfo(stepinfono);
								stepinfono = finalinfo;

								LSprotocolstepObj1.setLsprotocolstepInfo(finalinfo);
							}
						}
						if (lsprotocolorderfile.size() != 0) {
							for (LSprotocolorderfiles LSprotocolorderfiles : lsprotocolorderfile) {
								String finalinfo = stepinfono.replaceAll(LSprotocolorderfiles.getOldfileid(),
										LSprotocolorderfiles.getFileid());
								stepinfono = finalinfo;

								LSprotocolstepObj1.setLsprotocolstepInfo(finalinfo);
							}
						}
						LsLogilabprotocolstepInfo LsLogilabprotocolstepInfoObj = new LsLogilabprotocolstepInfo();
						Gson g = new Gson();
						String str = g.toJson(LSprotocolstepObj1.getLsprotocolstepInfo());
						LsLogilabprotocolstepInfoObj.setId(LSprotocolstepObj1.getProtocolorderstepcode());
						LsLogilabprotocolstepInfoObj.setContent(str);
						mongoTemplate.insert(LsLogilabprotocolstepInfoObj);

					}
				}

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

				lSlogilabprotocoldetail.setlSprotocolworkflow(
						lSprotocolworkflowRepository.findTopByAndLssitemasterOrderByWorkflowcodeAsc(site));

				LSlogilabprotocoldetailRepository.save(lSlogilabprotocoldetail);
			}

			mapObj.put("AddedProtocol", lSlogilabprotocoldetail);
		}

		LSusergroup userGroup = LSusergroupRepository
				.findOne(lSlogilabprotocoldetail.getObjuser().getMultiusergroupcode());

		List<LSprotocolworkflowgroupmap> lsworkflowgroupmapping = LSprotocolworkflowgroupmapRepository
				.findBylsusergroupAndWorkflowcodeNotNull(userGroup);

		List<LSlogilabprotocoldetail> lstPendingOrder = new ArrayList<>();
		List<LSuserteammapping> LSuserteammapping = LSuserteammappingRepositoryObj
				.findByLsuserMasterAndTeamcodeNotNull(lSlogilabprotocoldetail.getLsuserMaster());
		int pendingcount = 0;
		if (lsworkflowgroupmapping != null && lsworkflowgroupmapping.size() > 0) {
//			LSprotocolworkflow lsprotocolworkflow = lSprotocolworkflowRepository
//					.findByworkflowcode(lsworkflowgroupmapping.get(0).getWorkflowcode());
			List<LSprotocolworkflow> lsprotocolworkflow = lSprotocolworkflowRepository
					.findByLsprotocolworkflowgroupmapInOrderByWorkflowcodeDesc(lsworkflowgroupmapping);

			lstPendingOrder = LSlogilabprotocoldetailRepository
					.findByProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
							lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getFromdate(),
							lSlogilabprotocoldetail.getTodate());

//			lstPendingOrder.forEach(objorder -> objorder
//					.setCanuserprocess(lsprotocolworkflow.equals(objorder.getlSprotocolworkflow()) ? true : false));

			lstPendingOrder.forEach((objorder) -> {

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
			
			pendingcount = LSlogilabprotocoldetailRepository.countByProtocoltypeAndOrderflagAndCreatedtimestampBetween(
					lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getFromdate(),
					lSlogilabprotocoldetail.getTodate());
		}else if(LSuserteammapping != null && LSuserteammapping.size() > 0) {
			lstPendingOrder = LSlogilabprotocoldetailRepository
					.findByProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
							lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getFromdate(),
							lSlogilabprotocoldetail.getTodate());
			lstPendingOrder.forEach(objorder -> objorder
			.setCanuserprocess(false));
			
			pendingcount = LSlogilabprotocoldetailRepository.countByProtocoltypeAndOrderflagAndCreatedtimestampBetween(
					lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getFromdate(),
					lSlogilabprotocoldetail.getTodate());
		}
//		int pendingcount = LSlogilabprotocoldetailRepository.countByProtocoltypeAndOrderflagAndCreatedtimestampBetween(
//				lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getFromdate(),
//				lSlogilabprotocoldetail.getTodate());

//		List<LSlogilabprotocoldetail> lstOrder = LSlogilabprotocoldetailRepository
//				.findByProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), "N");
//		int pendingcount = LSlogilabprotocoldetailRepository
//				.countByProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), "N");
//		Collections.sort(lstOrder, Collections.reverseOrder());
		mapObj.put("ListOfProtocol", lstPendingOrder);
		mapObj.put("pendingcount", pendingcount);
		return mapObj;
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

	@SuppressWarnings("unlikely-arg-type")
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
				.findTop10ByProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
						lSlogilabprotocoldetail.getProtocoltype(), "R", lSlogilabprotocoldetail.getFromdate(),
						lSlogilabprotocoldetail.getTodate());

		LSusergroup userGroup = LSusergroupRepository
				.findOne(lSlogilabprotocoldetail.getObjuser().getMultiusergroupcode());

		List<LSprotocolworkflowgroupmap> lsworkflowgroupmapping = LSprotocolworkflowgroupmapRepository
				.findBylsusergroupAndWorkflowcodeNotNull(userGroup);

		List<LSlogilabprotocoldetail> lstPendingOrder = new ArrayList<>();

		List<LSuserteammapping> LSuserteammapping = LSuserteammappingRepositoryObj
				.findByLsuserMasterAndTeamcodeNotNull(lSlogilabprotocoldetail.getLsuserMaster());
		int pendingcount = 0;
		if (lsworkflowgroupmapping != null && lsworkflowgroupmapping.size() > 0) {
//			LSprotocolworkflow lsprotocolworkflow = lSprotocolworkflowRepository
//					.findByworkflowcode(lsworkflowgroupmapping.get(0).getWorkflowcode());

			List<LSprotocolworkflow> lsprotocolworkflow = lSprotocolworkflowRepository
					.findByLsprotocolworkflowgroupmapInOrderByWorkflowcodeDesc(lsworkflowgroupmapping);

			lstPendingOrder = LSlogilabprotocoldetailRepository
					.findTop10ByProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
							lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getFromdate(),
							lSlogilabprotocoldetail.getTodate());

			lstPendingOrder.forEach((objorder) -> {

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

			pendingcount = LSlogilabprotocoldetailRepository.countByProtocoltypeAndOrderflagAndCreatedtimestampBetween(
					lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getFromdate(),
					lSlogilabprotocoldetail.getTodate());
//			lstPendingOrder.forEach(objorder -> objorder
//					.setCanuserprocess(lsprotocolworkflow.equals(objorder.getlSprotocolworkflow()) ? true : false));
		} else if (LSuserteammapping != null && LSuserteammapping.size() == 0) {
			lstPendingOrder = LSlogilabprotocoldetailRepository
					.findTop10ByProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
							lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getFromdate(),
							lSlogilabprotocoldetail.getTodate());
			lstPendingOrder.forEach(objorder -> objorder.setCanuserprocess(false));

			pendingcount = LSlogilabprotocoldetailRepository.countByProtocoltypeAndOrderflagAndCreatedtimestampBetween(
					lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getFromdate(),
					lSlogilabprotocoldetail.getTodate());
		}

//		 pendingcount = LSlogilabprotocoldetailRepository.countByProtocoltypeAndOrderflagAndCreatedtimestampBetween(
//				lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getFromdate(),
//				lSlogilabprotocoldetail.getTodate());
		int completedcount = LSlogilabprotocoldetailRepository
				.countByProtocoltypeAndOrderflagAndCreatedtimestampBetween(lSlogilabprotocoldetail.getProtocoltype(),
						"R", lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

		int myordercount = (int) LSlogilabprotocoldetailRepository
				.countByProtocoltypeAndOrderflagAndAssignedtoAndCreatedtimestampBetween(
						lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getAssignedto(),
						lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

		int assignedcount = (int) LSlogilabprotocoldetailRepository
				.countByProtocoltypeAndOrderflagAndLsuserMasterAndCreatedtimestampBetween(
						lSlogilabprotocoldetail.getProtocoltype(), "N",
//				.countByOrderflagAndCreatebyAndCreatedtimestampBetween("N",
						lSlogilabprotocoldetail.getLsuserMaster(), lSlogilabprotocoldetail.getFromdate(),
						lSlogilabprotocoldetail.getTodate());

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

	public Map<String, Object> getProtocolOrderListfortabchange(LSlogilabprotocoldetail lSlogilabprotocoldetail) {
		Map<String, Object> lstOrder = new HashMap<String, Object>();

		if (lSlogilabprotocoldetail.getOrderflag().equals("N")) {
//			List<LSlogilabprotocoldetail> lstPendingOrder = LSlogilabprotocoldetailRepository
//					.findTop10ByProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//							lSlogilabprotocoldetail.getProtocoltype(), "N", 
//							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

			LSusergroup userGroup = LSusergroupRepository
					.findOne(lSlogilabprotocoldetail.getObjuser().getMultiusergroupcode());

			List<LSprotocolworkflowgroupmap> lsworkflowgroupmapping = LSprotocolworkflowgroupmapRepository
					.findBylsusergroupAndWorkflowcodeNotNull(userGroup);

			List<LSlogilabprotocoldetail> lstPendingOrder = new ArrayList<>();

			List<LSuserteammapping> LSuserteammapping = LSuserteammappingRepositoryObj
					.findByLsuserMasterAndTeamcodeNotNull(lSlogilabprotocoldetail.getLsuserMaster());
			int pendingcount = 0;

			if (lsworkflowgroupmapping != null && lsworkflowgroupmapping.size() > 0) {
//				LSprotocolworkflow lsprotocolworkflow = lSprotocolworkflowRepository
//						.findByworkflowcode(lsworkflowgroupmapping.get(0).getWorkflowcode());

				List<LSprotocolworkflow> lsprotocolworkflow = lSprotocolworkflowRepository
						.findByLsprotocolworkflowgroupmapInOrderByWorkflowcodeDesc(lsworkflowgroupmapping);

				lstPendingOrder = LSlogilabprotocoldetailRepository
						.findTop10ByProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
								lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getFromdate(),
								lSlogilabprotocoldetail.getTodate());

//				lstPendingOrder.forEach(objorder -> objorder
//						.setCanuserprocess(lsprotocolworkflow.equals(objorder.getlSprotocolworkflow()) ? true : false));
				lstPendingOrder.forEach((objorder) -> {

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

				pendingcount = LSlogilabprotocoldetailRepository
						.countByProtocoltypeAndOrderflagAndCreatedtimestampBetween(
								lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getFromdate(),
								lSlogilabprotocoldetail.getTodate());
			} else if (LSuserteammapping != null && LSuserteammapping.size() > 0) {
				lstPendingOrder = LSlogilabprotocoldetailRepository
						.findTop10ByProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
								lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getFromdate(),
								lSlogilabprotocoldetail.getTodate());
				lstPendingOrder.forEach(objorder -> objorder.setCanuserprocess(false));

				pendingcount = LSlogilabprotocoldetailRepository
						.countByProtocoltypeAndOrderflagAndCreatedtimestampBetween(
								lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getFromdate(),
								lSlogilabprotocoldetail.getTodate());
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
					.countByProtocoltypeAndOrderflagAndCreatedtimestampBetween(
							lSlogilabprotocoldetail.getProtocoltype(), "R", lSlogilabprotocoldetail.getFromdate(),
							lSlogilabprotocoldetail.getTodate());
			List<LSlogilabprotocoldetail> lstCompletedOrder = LSlogilabprotocoldetailRepository
					.findTop10ByProtocoltypeAndOrderflagAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
							lSlogilabprotocoldetail.getProtocoltype(), "R", lSlogilabprotocoldetail.getFromdate(),
							lSlogilabprotocoldetail.getTodate());
			lstOrder.put("lstCompletedOrder", lstCompletedOrder);
			lstOrder.put("completedcount", completedcount);
		} else if (lSlogilabprotocoldetail.getOrderflag().equals("M")) {
			int completedcount = (int) LSlogilabprotocoldetailRepository
					.countByProtocoltypeAndOrderflagAndAssignedtoAndCreatedtimestampBetween(
							lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getAssignedto(),
							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

			List<LSlogilabprotocoldetail> lstCompletedOrder = LSlogilabprotocoldetailRepository
					.findTop10ByProtocoltypeAndOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
							lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getAssignedto(),
							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

			lstOrder.put("lstMyOrder", lstCompletedOrder);
			lstOrder.put("myordercount", completedcount);
		} else if (lSlogilabprotocoldetail.getOrderflag().equals("A")) {
			int completedcount = (int) LSlogilabprotocoldetailRepository
					.countByProtocoltypeAndOrderflagAndLsuserMasterAndCreatedtimestampBetween(
							lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getLsuserMaster(),
							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

//			List<LSlogilabprotocoldetail> lstCompletedOrder = LSlogilabprotocoldetailRepository
//					.findTop10ByOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenOrderByCreatedtimestampDesc("N",
//							lSlogilabprotocoldetail.getLsuserMaster(),lSlogilabprotocoldetail.getAssignedto(),
//							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

			List<LSlogilabprotocoldetail> lstCompletedOrder = LSlogilabprotocoldetailRepository
					.findTop10ByProtocoltypeAndOrderflagAndLsuserMasterAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
							lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getLsuserMaster(),
							lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

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
					.getProtocoltypeAndOrderflagAndCreatedtimestampBetween(lSlogilabprotocoldetail.getProtocoltype(),
							"N", lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());

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
		} else if (LSuserteammapping != null && LSuserteammapping.size() > 0) {
			lstreminingPendingOrder = LSlogilabprotocoldetailRepository
					.getProtocoltypeAndOrderflagAndCreatedtimestampBetween(lSlogilabprotocoldetail.getProtocoltype(),
							"N", lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
			lstreminingPendingOrder.forEach(objorder -> objorder.setCanuserprocess(false));
		}

		List<LSlogilabprotocoldetail> lstreminingCompletedOrder = LSlogilabprotocoldetailRepository
				.getProtocoltypeAndOrderflagAndCreatedtimestampBetween(lSlogilabprotocoldetail.getProtocoltype(), "R",
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
						.getProtocoltypeAndOrderflagAndCreatedtimestampBetween(
								lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getFromdate(),
								lSlogilabprotocoldetail.getTodate());

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

			} else if (LSuserteammapping != null && LSuserteammapping.size() > 0) {
				lstreminingPendingOrder = LSlogilabprotocoldetailRepository
						.getProtocoltypeAndOrderflagAndCreatedtimestampBetween(
								lSlogilabprotocoldetail.getProtocoltype(), "N", lSlogilabprotocoldetail.getFromdate(),
								lSlogilabprotocoldetail.getTodate());

				lstreminingPendingOrder.forEach(objorder -> objorder.setCanuserprocess(false));
			}

			lstOrder.put("lstreminingPendingOrder", lstreminingPendingOrder);
		} else if (lSlogilabprotocoldetail.getOrderflag().equals("R")) {
			List<LSlogilabprotocoldetail> lstreminingCompletedOrder = LSlogilabprotocoldetailRepository
					.getProtocoltypeAndOrderflagAndCreatedtimestampBetween(lSlogilabprotocoldetail.getProtocoltype(),
							"R", lSlogilabprotocoldetail.getFromdate(), lSlogilabprotocoldetail.getTodate());
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

	public Map<String, Object> getProtocolOrderStepLst(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		@SuppressWarnings("unused")
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if (argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});

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

				LSprotocolstepLst.add(LSprotocolstepObj1);
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

	public Map<String, Object> startStep(LSuserMaster objuser) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("lslogilabprotocolsteps");
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}
		return mapOrders;
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
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if (argMap.getObjsilentaudit() != null) {
			LScfttransactionobj = argMap.getObjsilentaudit();
//			 LScfttransactionobj = new ObjectMapper().convertValue(argMap.getObjsilentaudit(), new TypeReference<LScfttransaction>() {}); 
//			LScfttransactionobj = new ObjectMapper().convertValue(argMap.get("objsilentaudit"),
//					new TypeReference<LScfttransaction>() {
//					});
//			LSlogilabprotocoldetail newProtocolOrderObj = new ObjectMapper().
//					convertValue(argMap.get("ProtocolOrderObj"),new TypeReference<LSlogilabprotocoldetail>(){});
			LScfttransactionobj.setTableName("LSlogilabprotocoldetail");
			LSlogilabprotocoldetailRepository.save(argMap);

			lscfttransactionRepository.save(LScfttransactionobj);
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
//		if(argObj.containsKey("objsilentaudit")) {
		LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
				new TypeReference<LScfttransaction>() {
				});
		int multitenent = new ObjectMapper().convertValue(argObj.get("ismultitenant"), Integer.class);
//			long protocolmastercode = ((Number) argObj.get("olsprotocolmastercode")).longValue();
//			LSprotocolmaster newProtocolMasterObj = new ObjectMapper().convertValue(argObj.get("ProtocolMasterObj"), new TypeReference<LSprotocolmaster>(){});
//			List<LSprotocolstep> LSprotocolsteplst = LSProtocolStepRepositoryObj.findByProtocolmastercodeAndStatus(newProtocolMasterObj.getProtocolmastercode() , 1);
		List<LSprotocolstep> LSprotocolsteplst = LSProtocolStepRepositoryObj
				.findByProtocolmastercodeAndStatus(argObj.get("olsprotocolmastercode"), 1);
//		CloudLSprotocolstepInfo CloudLSprotocolstepInfo = new CloudLSprotocolstepInfo();
		LSprotocolstepInformation LSprotocolstepInformation = new LSprotocolstepInformation();
		LSprotocolstepInfo newLSprotocolstepInfo = new LSprotocolstepInfo();
		@SuppressWarnings("unused")
		List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();
		int i = 0;
		for (LSprotocolstep LSprotocolstepObj1 : LSprotocolsteplst) {
			if (multitenent == 1) {
//				CloudLSprotocolstepInfo = CloudLSprotocolstepInfoRepository
//						.findById(LSprotocolstepObj1.getProtocolstepcode());
				LSprotocolstepInformation = lsprotocolstepInformationRepository
						.findById(LSprotocolstepObj1.getProtocolstepcode());
			} else {
				newLSprotocolstepInfo = mongoTemplate.findById(LSprotocolstepObj1.getProtocolstepcode(),
						LSprotocolstepInfo.class);
			}

			LSprotocolstep LSprotocolstep = new LSprotocolstep();
//					long newprotocolmastercode = ((Number) argObj.get("newprotocolmastercode")).longValue();
			LSprotocolstep LSprotocolstepObj = new LSprotocolstep();
			int newprotocolmastercode = new ObjectMapper().convertValue(argObj.get("newprotocolmastercode"),
					Integer.class);
			LSprotocolstepObj.setProtocolmastercode(newprotocolmastercode);
//					LSprotocolstep LSprotocolstepObj = new ObjectMapper().convertValue(argObj.get("newProtocolstepObj"), new TypeReference<LSprotocolstep>() {});
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

			if (multitenent == 1) {

//				updateCloudProtocolVersion(LSprotocolstep.getProtocolmastercode(), LSprotocolstep.getProtocolstepcode(),
//						LSprotocolstep.getLsprotocolstepInfo(), LSprotocolstep.getNewStep(), LScfttransactionobj,
//						LSprotocolstep);

			} else {
				updateCloudProtocolVersiononSQL(LSprotocolstep, LScfttransactionobj.getLssitemaster());
			}

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

			List<LSprotocolstep> LSprotocolsteplstforsecond = LSProtocolStepRepositoryObj
					.findByProtocolmastercodeAndStatus(LSprotocolstepObj.getProtocolmastercode(), 1);
			for (int j = i; j < LSprotocolsteplstforsecond.size(); j++) {
				if (multitenent == 1 && LSprotocolstepInformation != null) {
					LSprotocolstepInformation CloudLSprotocolstepInfoforinsert = new LSprotocolstepInformation();
					CloudLSprotocolstepInfoforinsert.setId(LSprotocolsteplstforsecond.get(i).getProtocolstepcode());
					CloudLSprotocolstepInfoforinsert
							.setLsprotocolstepInfo(LSprotocolstepInformation.getLsprotocolstepInfo());
					lsprotocolstepInformationRepository.save(CloudLSprotocolstepInfoforinsert);
					i++;
					break;
				} else if (newLSprotocolstepInfo != null && multitenent == 0) {

					Query query = new Query(
							Criteria.where("id").is(LSprotocolsteplstforsecond.get(i).getProtocolstepcode()));
					Update update = new Update();
					update.set("content", newLSprotocolstepInfo.getContent());

					mongoTemplate.upsert(query, update, LSprotocolstepInfo.class);
					i++;
					break;
				}
			}

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
	public Map<String, Object> GetProtocolTemplateVerionLst(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();

		ObjectMapper objm = new ObjectMapper();

		LSprotocolversion versionMaster = objm.convertValue(argObj.get("CloudLSprotocolversionstep"),
				LSprotocolversion.class);

		// CloudLSprotocolversionstep versionMaster = objm.convertValue(
		// argObj.get("CloudLSprotocolversionstep"), CloudLSprotocolversionstep.class);

		int multitenent = objm.convertValue(argObj.get("ismultitenant"), Integer.class);

		List<LSprotocolstepversion> LSprotocolstepversion = LSprotocolstepversionRepository
				.findByprotocolmastercodeAndVersionno(versionMaster.getProtocolmastercode(),
						versionMaster.getVersionno());

		LSprotocolstepversion = LSprotocolstepversion.stream().distinct().collect(Collectors.toList());

		List<LSprotocolstep> LSprotocolsteplst = new ArrayList<LSprotocolstep>();

		int k = 0;

		while (k < LSprotocolstepversion.size()) {

			LSprotocolstep lsStep = LSProtocolStepRepositoryObj
					.findByProtocolstepcodeAndStatus(LSprotocolstepversion.get(k).getProtocolstepcode(), 1);

			lsStep.setVersionno(LSprotocolstepversion.get(k).getVersionno());

			lsStep.setProtocolstepversioncode(LSprotocolstepversion.get(k).getProtocolstepversioncode());

			LSprotocolsteplst.add(lsStep);

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
				if (newLSprotocolstepInfo != null) {
//					LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
					LSprotocolstepObj1.setLsprotocolstepInformation(newLSprotocolstepInfo.getLsprotocolstepInfo());
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

	public LSlogilabprotocoldetail getsingleprotocolorder(LSlogilabprotocoldetail objusers) {

		if (objusers.getProtocolordercode() != null && objusers.getProtoclordername() != null) {
			return LSlogilabprotocoldetailRepository.findByProtocolordercodeAndProtoclordername(
					objusers.getProtocolordercode(), objusers.getProtoclordername());
		}
		return objusers;

	}

	public Map<String, Object> Getinitialorders(LSlogilabprotocoldetail objorder) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		if (objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
			mapOrders.put("orders", Getadministratororder(objorder));
			mapOrders.put("ordercount", LSlogilabprotocoldetailRepository.count());
		} else {
			mapOrders.put("orders", Getuserorder(objorder));
			mapOrders.put("ordercount", LSlogilabprotocoldetailRepository
					.countByLsprojectmasterIn(objorder.getLsuserMaster().getLstproject()));
		}

		return mapOrders;
	}

	public List<LSlogilabprotocoldetail> Getremainingorders(LSlogilabprotocoldetail objorder) {
		if (objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
			return Getadministratororder(objorder);
		} else {
			return Getuserorder(objorder);
		}
	}

	public List<LSlogilabprotocoldetail> Getadministratororder(LSlogilabprotocoldetail objorder) {
		List<LSlogilabprotocoldetail> lstorders = new ArrayList<LSlogilabprotocoldetail>();
		if (objorder.getProtocolordercode() == 0) {
			lstorders = LSlogilabprotocoldetailRepository.findFirst20ByOrderByProtocolordercodeDesc();
		} else {
			lstorders = LSlogilabprotocoldetailRepository
					.findFirst20ByProtocolordercodeLessThanOrderByProtocolordercodeDesc(
							objorder.getProtocolordercode());
		}
		return lstorders;
	}

	public List<LSlogilabprotocoldetail> Getuserorder(LSlogilabprotocoldetail objorder) {
		List<LSprojectmaster> lstproject = objorder.getLsuserMaster().getLstproject();
		List<LSlogilabprotocoldetail> lstorders = new ArrayList<LSlogilabprotocoldetail>();
		if (lstproject != null) {
			if (objorder.getProtocolordercode() == 0) {
				lstorders = LSlogilabprotocoldetailRepository
						.findFirst20ByLsprojectmasterInOrderByProtocolordercodeDesc(lstproject);
			} else {
				lstorders = LSlogilabprotocoldetailRepository
						.findFirst20ByProtocolordercodeLessThanAndLsprojectmasterInOrderByProtocolordercodeDesc(
								objorder.getProtocolordercode(), lstproject);
			}
		}
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
		String url = originurl + "/protocol/downloadprotocolimage/" + objimg.getFileid() + "/"
				+ TenantContext.getCurrentTenant() + "/" + objimg.getFilename() + "/" + objimg.getExtension();
		Gson g = new Gson();
		String str = g.toJson(url);
		objimg.setSrc(str);
		lsprotocolimagesRepository.save(objimg);
		map.put("link", originurl + "/protocol/downloadprotocolimage/" + objimg.getFileid() + "/"
				+ TenantContext.getCurrentTenant() + "/" + objimg.getFilename() + "/" + objimg.getExtension());

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

	public Map<String, Object> uploadprotocols(Map<String, Object> body) {

		Map<String, Object> mapObj = new HashMap<String, Object>();
		Response response = new Response();
		LSprotocolstepInformation obj = new LSprotocolstepInformation();
		ObjectMapper object = new ObjectMapper();
		if (!body.get("protocolstepname").equals("")) {
			LSprotocolstep LSprotocolstepObj = new LSprotocolstep();
			String protocolstepname = object.convertValue(body.get("protocolstepname"), String.class);
			String content = object.convertValue(body.get("content"), String.class);
			Integer protocolmastercode = object.convertValue(body.get("protocolmastercode"), Integer.class);
//			boolean NewStep = object.convertValue(body.get("NewStep"), boolean.class);
			Integer NewStep = object.convertValue(body.get("NewStep"), Integer.class);

			Integer stepno = object.convertValue(body.get("stepno"), Integer.class);
			Integer ismultitenant = object.convertValue(body.get("ismultitenant"), Integer.class);
			Gson g = new Gson();
			String str = g.toJson(content);
//			  String jsonObject = new JsonParser().parse(str).getAsString();

			int sitecode = object.convertValue(body.get("sitecode"), Integer.class);
			LSprotocolstep Object = LSProtocolStepRepositoryObj
					.findByProtocolmastercodeAndProtocolstepnameAndStatus(protocolmastercode, protocolstepname, 1);
			if (Object != null && NewStep == 1) {
				response.setStatus(false);
				mapObj.put("response", response);
			} else {
				if (NewStep == 0 && body.get("protocolstepcode") != null) {
					int protocolstepcode = object.convertValue(body.get("protocolstepcode"), Integer.class);

					LSprotocolstepObj = LSProtocolStepRepositoryObj
							.findByProtocolmastercodeAndProtocolstepcodeAndStatus(protocolmastercode, protocolstepcode,
									1);
					LSprotocolstepObj.setProtocolstepname(protocolstepname);
					String modifiedusername = object.convertValue(body.get("modifiedusername"), String.class);
					LSprotocolstepObj.setModifiedusername(modifiedusername);
					LSprotocolstepObj.setLsprotocolstepInformation(str);
					LSProtocolStepRepositoryObj.save(LSprotocolstepObj);
					LSprotocolstepObj.setNewStep(NewStep);
					if (ismultitenant == 1) {
						obj.setId(protocolstepcode);
						obj.setLsprotocolstepInfo(str);
						lsprotocolstepInformationRepository.save(obj);
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

				if (ismultitenant == 1) {

					updateCloudProtocolVersion(protocolmastercode, LSprotocolstepObj.getProtocolstepcode(),
							LSprotocolstepObj.getLsprotocolstepInformation(), NewStep, sitecode, LSprotocolstepObj);

					if (NewStep != 1) {
						LSprotocolmaster protocolmaster = LSProtocolMasterRepositoryObj
								.findByprotocolmastercode(LSprotocolstepObj.getProtocolmastercode());
						mapObj.put("protocolmaster", protocolmaster);
						List<LSprotocolversion> LSprotocolversionlst = lsprotocolversionRepository
								.findByprotocolmastercode(LSprotocolstepObj.getProtocolmastercode());

						Collections.sort(LSprotocolversionlst, Collections.reverseOrder());

						mapObj.put("LSprotocolversionlst", LSprotocolversionlst);
					}

				} else {

					updateCloudProtocolVersiononSQL(LSprotocolstepObj, sitecode);

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
	public Map<String, Object> Getprotocollinksignature(Map<String, String> body) {
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

		return obj;
	}

	public Map<String, Object> uploadprotocolsordersstep(Map<String, Object> body) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		Response response = new Response();
		CloudLsLogilabprotocolstepInfo CloudLSprotocolstepInfoObj = new CloudLsLogilabprotocolstepInfo();
		ObjectMapper object = new ObjectMapper();
		if (!body.get("protocolstepname").equals("")) {

			LSlogilabprotocolsteps lslogilabprotocolsteps = new LSlogilabprotocolsteps();
			String protocolstepname = object.convertValue(body.get("protocolstepname"), String.class);
			String content = object.convertValue(body.get("content"), String.class);
			Long protocolordercode = object.convertValue(body.get("protocolordercode"), Long.class);
//		boolean NewStep = object.convertValue(body.get("NewStep"), boolean.class);
			Integer NewStep = object.convertValue(body.get("NewStep"), Integer.class);
			Integer stepno = object.convertValue(body.get("stepno"), Integer.class);
			Integer ismultitenant = object.convertValue(body.get("ismultitenant"), Integer.class);
			Integer protocolmastercode = object.convertValue(body.get("protocolmastercode"), Integer.class);
			Integer protocolstepcode = object.convertValue(body.get("protocolstepcode"), Integer.class);
			Gson g = new Gson();
			String str = g.toJson(content);
			int sitecode = object.convertValue(body.get("sitecode"), Integer.class);
//		LSprotocolstep Object=LSProtocolStepRepositoryObj.findByProtocolmastercodeAndProtocolstepnameAndStatus(protocolmastercode,protocolstepname,1);
			String orderstepflag = object.convertValue(body.get("orderstepflag"), String.class);

			if (NewStep == 0) {
				int protocolorderstepcode = object.convertValue(body.get("protocolorderstepcode"), Integer.class);
				lslogilabprotocolsteps = LSlogilabprotocolstepsRepository
						.findByProtocolorderstepcode(protocolorderstepcode);
				lslogilabprotocolsteps.setProtocolstepname(protocolstepname);
				lslogilabprotocolsteps.setOrderstepflag(orderstepflag);
				String modifiedusername = object.convertValue(body.get("modifiedusername"), String.class);
				lslogilabprotocolsteps.setModifiedusername(modifiedusername);
				LSlogilabprotocolstepsRepository.save(lslogilabprotocolsteps);
			} else {
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
					lslogilabprotocolsteps.setProtocolstepcode(protocolstepcode);

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
					response.setInformation("Protocols.IDS_INFO");
					response.setStatus(true);
				}
			} else {
				if (NewStep == 0) {

					Query query = new Query(Criteria.where("id").is(lslogilabprotocolsteps.getProtocolorderstepcode()));
					Update update = new Update();
					update.set("content", str);

					mongoTemplate.upsert(query, update, LsLogilabprotocolstepInfo.class);
				} else {
					LsLogilabprotocolstepInfo LsLogilabprotocolstepInfoObj = new LsLogilabprotocolstepInfo();

					LsLogilabprotocolstepInfoObj.setId(lslogilabprotocolsteps.getProtocolorderstepcode());
					LsLogilabprotocolstepInfoObj.setContent(str);
					mongoTemplate.insert(LsLogilabprotocolstepInfoObj);
				}
			}

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
			mapObj.put("protocolstepLst", LSprotocolstepLst);
			mapObj.put("countforstep", countforstep);
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
		lsprotocolorderimagesRepository.save(objimg);
		map.put("link", originurl + "/protocol/downloadprotocolorderimage/" + objimg.getFileid() + "/"
				+ TenantContext.getCurrentTenant() + "/" + objimg.getFilename() + "/" + objimg.getExtension());

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

		return objprotocolordershareto;
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
				.findByshareprotocolcode(objordershareby.getShareprotocolcode());

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
			lsfiles = LSProtocolMasterRepositoryObj.findByLstestInAndStatus(lsfiletest, 1);
		} else {

			lsfiles = LSProtocolMasterRepositoryObj.findByLstestInAndStatus(lsfiletest, 1);
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
		String url = originurl + "/protocol/downloadprotocolimagesql/" + objimg.getFileid() + "/" + objimg.getFilename()
				+ "/" + objimg.getExtension();
		Gson g = new Gson();
		String str = g.toJson(url);
		objimg.setSrc(str);
		lsprotocolimagesRepository.save(objimg);

		ProtocolImage protocolImage = new ProtocolImage();
		protocolImage.setId(objimg.getProtocolstepimagecode());
		protocolImage.setName(objimg.getFilename());
		protocolImage.setFileid(Fieldid);
		protocolImage.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
		protocolImage = protocolImageRepository.insert(protocolImage);

		map.put("link", originurl + "/protocol/downloadprotocolimagesql/" + protocolImage.getFileid() + "/"
				+ objimg.getFilename() + "/" + objimg.getExtension());

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
		return LSprotocolordervideosRepository.findByFileid(fileid);
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

}