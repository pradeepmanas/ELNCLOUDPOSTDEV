package com.agaram.eln.primary.service.protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cloudProtocol.CloudLSprotocolstepInfo;
import com.agaram.eln.primary.model.cloudProtocol.CloudLSprotocolversionstep;
import com.agaram.eln.primary.model.cloudProtocol.CloudLsLogilabprotocolstepInfo;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocolsteps;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolordersampleupdates;
import com.agaram.eln.primary.model.protocols.LSprotocolsampleupdates;
import com.agaram.eln.primary.model.protocols.LSprotocolstep;
import com.agaram.eln.primary.model.protocols.LSprotocolstepInfo;
import com.agaram.eln.primary.model.protocols.LSprotocolstepversion;
import com.agaram.eln.primary.model.protocols.LSprotocolupdates;
import com.agaram.eln.primary.model.protocols.LSprotocolversion;
import com.agaram.eln.primary.model.protocols.LSprotocolversionstepInfo;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflow;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflowgroupmap;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflowhistory;
import com.agaram.eln.primary.model.protocols.LsLogilabprotocolstepInfo;
import com.agaram.eln.primary.model.usermanagement.LSMultiusergroup;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.cloudProtocol.CloudLSprotocolstepInfoRepository;
import com.agaram.eln.primary.repository.cloudProtocol.CloudLSprotocolversionstepRepository;
import com.agaram.eln.primary.repository.cloudProtocol.CloudLsLogilabprotocolstepInfoRepository;
import com.agaram.eln.primary.repository.protocol.LSProtocolMasterRepository;
import com.agaram.eln.primary.repository.protocol.LSProtocolStepRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolworkflowgroupmapRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolworkflowhistoryRepository;
import com.agaram.eln.primary.repository.protocol.lSprotocolworkflowRepository;
import com.agaram.eln.primary.repository.usermanagement.LSMultiusergroupRepositery;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusergroupRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;
import com.agaram.eln.primary.service.basemaster.BaseMasterService;
import com.agaram.eln.primary.repository.protocol.LSlogilabprotocoldetailRepository;
import com.agaram.eln.primary.repository.protocol.LSlogilabprotocolstepsRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolordersampleupdatesRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolupdatesRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolversionRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesdataRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolsampleupdatesRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolstepversionRepository;

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
	private LSMultiusergroupRepositery LSMultiusergroupRepositery;

	@Autowired
	private LSprotocolstepversionRepository LSprotocolstepversionRepository;
	
	@Autowired
	private LSusergroupRepository LSusergroupRepository;

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
	
	public List <LSprotocolmaster> getprotocol(LSuserMaster objusers)
	{
		List<Integer> lstuser = objusers.getObjuser().getTeamuserscode();
		return LSProtocolMasterRepositoryObj.findByCreatedbyInAndStatusAndLssitemasterOrderByCreatedateDesc(lstuser,1, objusers.getLssitemaster().getSitecode());
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
		// silent audit
//		if (LScfttransactionobj != null) {
//			LScfttransactionobj.setTableName("LSprotocolmaster");
//			if (argObj.containsKey("username")) {
//				String username = objMapper.convertValue(argObj.get("username"), String.class);
//				LSSiteMaster objsite = LSSiteMasterRepository.findBysitecode(LScfttransactionobj.getLssitemaster());
//				LSuserMaster objuser = LSuserMasterRepositoryObj.findByusernameAndLssitemaster(username, objsite);
//				LScfttransactionobj.setLsuserMaster(objuser.getUsercode());
//
//				LScfttransactionobj.setLssitemaster(objuser.getLssitemaster().getSitecode());
//				LScfttransactionobj.setUsername(username);
//			}
//			lscfttransactionRepository.save(LScfttransactionobj);
//		}
//		manual audit
//		if (argObj.containsKey("objuser")) {
////			objUser=objMapper.convertValue(argObj.get("objuser"), LoggedUser.class);
//			@SuppressWarnings("unchecked")
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

		if (argObj.containsKey("newProtocolstepObj")) {
			LSprotocolstep LSprotocolstepObj = new ObjectMapper().convertValue(argObj.get("newProtocolstepObj"),
					new TypeReference<LSprotocolstep>() {
					});
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
						LSprotocolstepObj.getNewStep(), LScfttransactionobj, LSprotocolstepObj);

				if (LSprotocolstepObj.getNewStep() == 1) {
					CloudLSprotocolstepInfoObj.setId(LSprotocolstepObj.getProtocolstepcode());
					CloudLSprotocolstepInfoObj.setLsprotocolstepInfo(LSprotocolstepObj.getLsprotocolstepInfo());
					CloudLSprotocolstepInfoRepository.save(CloudLSprotocolstepInfoObj);
				} else {
					CloudLSprotocolstepInfo updateLSprotocolstepInfo = CloudLSprotocolstepInfoRepository
							.findById(LSprotocolstepObj.getProtocolstepcode());
					updateLSprotocolstepInfo.setLsprotocolstepInfo(LSprotocolstepObj.getLsprotocolstepInfo());
					CloudLSprotocolstepInfoRepository.save(updateLSprotocolstepInfo);
				}

			} else {
				updateCloudProtocolVersiononSQL(LSprotocolstepObj, LScfttransactionobj);

				Query query = new Query(Criteria.where("id").is(LSprotocolstepObj.getProtocolstepcode()));
				Update update = new Update();
				update.set("content", LSprotocolstepObj.getLsprotocolstepInfo());

				mongoTemplate.upsert(query, update, LSprotocolstepInfo.class);
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
				List<LSprotocolsampleupdates> lsprotocolsampleupdates =  new ObjectMapper().convertValue(argObj.get("modifiedsamplestep"),
						new TypeReference<List<LSprotocolsampleupdates>>() {
						});
				for(LSprotocolsampleupdates lSprotocolsampleupdates :lsprotocolsampleupdates) {	
					
					LSprotocolsampleupdates sample = lSprotocolsampleupdates;
//					if(sample.getProtocolstepcode() != null) {
						sample.setProtocolstepcode(LSprotocolstepObj.getProtocolstepcode());
						sample.setProtocolmastercode(LSprotocolstepObj.getProtocolmastercode());
//					}
					LSprotocolsampleupdatesRepository.save(sample);
				}	
			
			}
			if (argObj.containsKey("repositorydata")) {
				List<Lsrepositoriesdata> lsrepositoriesdata =  new ObjectMapper().convertValue(argObj.get("repositorydata"),
						new TypeReference<List<Lsrepositoriesdata>>() {
						});
//				Lsrepositoriesdata lsrepositoriesdata = new ObjectMapper().convertValue(argObj.get("repositorydata"),
//						new TypeReference<Lsrepositoriesdata>() {
//						});
				for(Lsrepositoriesdata lsrepositoriesdataobj :lsrepositoriesdata) {	
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

	private void updateCloudProtocolVersiononSQL(LSprotocolstep lSprotocolstepObj,
			LScfttransaction lScfttransactionobj) {

		LSprotocolmaster protocolMaster = LSProtocolMasterRepositoryObj
				.findByprotocolmastercode(lSprotocolstepObj.getProtocolmastercode());
		List<LSprotocolstep> lststep = LSProtocolStepRepositoryObj
				.findByProtocolmastercode(lSprotocolstepObj.getProtocolmastercode());

		if (protocolMaster.getApproved() != null && protocolMaster.getApproved() == 1) {

			LSSiteMaster lssitemaster = LSSiteMasterRepository.findBysitecode(lScfttransactionobj.getLssitemaster());
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

				if (lstVersStep.get(i).getProtocolstepcode() == lSprotocolstepObj.getProtocolstepcode()) {

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

				Query query = new Query(Criteria.where("id").is(protocolStep.getProtocolstepversioncode()));

				Update update = new Update();
				update.set("content", lSprotocolstepObj.getLsprotocolstepInfo());

				mongoTemplate.upsert(query, update, LSprotocolversionstepInfo.class);
			}
		}
	}

	private void updateCloudProtocolVersion(Integer protocolmastercode, Integer protocolstepcode,
			String lsprotocolstepInfo, Integer newStep, LScfttransaction LScfttransactionobj,
			LSprotocolstep lSprotocolstepObj) {

		LSprotocolmaster protocolMaster = LSProtocolMasterRepositoryObj.findByprotocolmastercode(protocolmastercode);
		List<LSprotocolstep> lststep = LSProtocolStepRepositoryObj.findByProtocolmastercode(protocolmastercode);

		if (protocolMaster.getApproved() != null && protocolMaster.getApproved() == 1) {

			LSSiteMaster lssitemaster = LSSiteMasterRepository.findBysitecode(LScfttransactionobj.getLssitemaster());
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

				if (protocolstepcode == lststep.get(i).getProtocolstepcode()) {
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

//			LSprotocolstepversionRepository.save(lstVersStep);

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
if(protocolStep != null) {
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
		@SuppressWarnings("unused")
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if (argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});

			LSprotocolstep deleteprotocolstep = new ObjectMapper().convertValue(argObj.get("deleteprotocolstep"),
					new TypeReference<LSprotocolstep>() {
					});

			List<LSprotocolstep> updateLSprotocolstepLst = new ObjectMapper()
					.convertValue(argObj.get("protocolstepLst"), new TypeReference<List<LSprotocolstep>>() {
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

				LSprotocolstepLst.add(LSprotocolstepObj1);
			}
			mapObj.put("protocolstepLst", LSprotocolstepLst);
		}
		return mapObj;
	}

	@SuppressWarnings("unchecked")
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

			List<LSprotocolmaster> LSprotocolmasterLst = LSProtocolMasterRepositoryObj
					.findByCreatedbyAndStatusAndLssitemaster(LScfttransactionobj.getLsuserMaster(), 1,
							LScfttransactionobj.getLssitemaster());
			List<LSprotocolmaster> AddedLSprotocolmasterObj = LSProtocolMasterRepositoryObj
					.findByStatusAndLssitemasterAndProtocolmastername(1, LScfttransactionobj.getLssitemaster(),
							newProtocolMasterObj.getProtocolmastername());

			LSprotocolworkflow lsprotocolworkflow = new LSprotocolworkflow();

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

					List<LSprotocolmaster> LSprotocolmasterLst1 = LSProtocolMasterRepositoryObj
							.findByStatusAndLssitemasterAndLSprotocolworkflowAndCreatedbyNot(1,
									LScfttransactionobj.getLssitemaster(), lsprotocolworkflow,
									LScfttransactionobj.getLsuserMaster());

					LSprotocolmasterLst.addAll(LSprotocolmasterLst1);

					List<LSprotocolmaster> LSprotocolmasterLst2 = LSProtocolMasterRepositoryObj
							.findByStatusAndLssitemasterAndLSprotocolworkflowNotAndCreatedbyNotAndSharewithteamAndApproved(
									1, LScfttransactionobj.getLssitemaster(), lsprotocolworkflow,
									LScfttransactionobj.getLsuserMaster(), 0, 0);

					LSprotocolmasterLst.addAll(LSprotocolmasterLst2);
				}
			}
			List<LSprotocolstep> LSprotocolsteplst = LSProtocolStepRepositoryObj
					.findByProtocolmastercodeAndStatus(newProtocolMasterObj.getProtocolmastercode(), 1);
			List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();
			for (LSprotocolstep LSprotocolstepObj1 : LSprotocolsteplst) {
				if (newProtocolMasterObj.getIsmultitenant() == 1) {
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

	public List<LSprotocolworkflow> GetProtocolWorkflow(LSprotocolworkflow objclass) {
		return lSprotocolworkflowRepository.findBylssitemaster(objclass.getLssitemaster());
	}

	public List<LSprotocolworkflow> InsertUpdatesheetWorkflow(List<LSprotocolworkflow> lSprotocolworkflow) {

		for (LSprotocolworkflow flow : lSprotocolworkflow) {
			LSprotocolworkflowgroupmapRepository.save(flow.getLsprotocolworkflowgroupmap());
			lSprotocolworkflowRepository.save(flow);
		}

		if (lSprotocolworkflow.get(0).getObjsilentaudit() != null) {
			lSprotocolworkflow.get(0).getObjsilentaudit().setTableName("lSprotocolworkflow");
		}

		if (lSprotocolworkflow.get(0).getObjuser() != null) {

			lSprotocolworkflow.get(0).getObjmanualaudit()
					.setComments(lSprotocolworkflow.get(0).getObjuser().getComments());
			lSprotocolworkflow.get(0).getObjmanualaudit().setTableName("lSprotocolworkflow");
			lSprotocolworkflow.get(0).getObjmanualaudit()
					.setLsuserMaster(lSprotocolworkflow.get(0).getObjsilentaudit().getLsuserMaster());
			lSprotocolworkflow.get(0).getObjmanualaudit()
					.setLssitemaster(lSprotocolworkflow.get(0).getLssitemaster().getSitecode());

//			lscfttransactionRepository.save(lSprotocolworkflow.get(0).getObjmanualaudit());
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

	public Map<String, Object> addProtocolOrder(LSlogilabprotocoldetail lSlogilabprotocoldetail) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		if (lSlogilabprotocoldetail != null) {

			LSlogilabprotocoldetailRepository.save(lSlogilabprotocoldetail);

			if (lSlogilabprotocoldetail.getProtocolordercode() != null) {

				String ProtocolOrderName = "ELN" + lSlogilabprotocoldetail.getProtocolordercode();

				lSlogilabprotocoldetail.setProtoclordername(ProtocolOrderName);
				lSlogilabprotocoldetail.setOrderflag("N");

				List<LSprotocolstep> lstSteps = LSProtocolStepRepositoryObj.findByProtocolmastercode(
						lSlogilabprotocoldetail.getLsprotocolmaster().getProtocolmastercode());

				// int i =0;

				List<LSlogilabprotocolsteps> lststep1 = new ObjectMapper().convertValue(lstSteps,
						new TypeReference<List<LSlogilabprotocolsteps>>() {
						});

				/*
				 * while(i < lstSteps.size()) {
				 * lststep1.get(i).setProtocolordercode(lSlogilabprotocoldetail.
				 * getProtocolordercode()); lststep1.get(i).setOrderstepflag("N");
				 * 
				 * LSlogilabprotocolstepsRepository.save(lststep1);
				 * 
				 * if(lSlogilabprotocoldetail.getIsmultitenant() == 1) {
				 * CloudLsLogilabprotocolstepInfo CloudLSprotocolstepInfoObj = new
				 * CloudLsLogilabprotocolstepInfo();
				 * CloudLSprotocolstepInfoObj.setId(lststep1.get(i).getProtocolorderstepcode());
				 * CloudLSprotocolstepInfoObj.setLsprotocolstepInfo(lststep1.get(i).
				 * getLsprotocolstepInfo());
				 * CloudLsLogilabprotocolstepInfoRepository.save(CloudLSprotocolstepInfoObj); }
				 * else { Query query = new
				 * Query(Criteria.where("id").is(lststep1.get(i).getProtocolorderstepcode()));
				 * Update update=new Update();
				 * update.set("content",lststep1.get(i).getLsprotocolstepInfo());
				 * 
				 * mongoTemplate.upsert(query, update, LsLogilabprotocolstepInfo.class); }
				 * 
				 * i++; }
				 */

				for (LSlogilabprotocolsteps LSprotocolstepObj1 : lststep1) {

					LSprotocolstepObj1.setProtocolordercode(lSlogilabprotocoldetail.getProtocolordercode());
					LSprotocolstepObj1.setOrderstepflag("N");

					LSlogilabprotocolstepsRepository.save(LSprotocolstepObj1);

					if (lSlogilabprotocoldetail.getIsmultitenant() == 1) {
						// for getting master step into order step
						CloudLSprotocolstepInfo newLSprotocolstepInfo = CloudLSprotocolstepInfoRepository
								.findById(LSprotocolstepObj1.getProtocolstepcode());
						if (newLSprotocolstepInfo != null) {
							LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
						}

						CloudLsLogilabprotocolstepInfo CloudLSprotocolstepInfoObj = new CloudLsLogilabprotocolstepInfo();
						CloudLSprotocolstepInfoObj.setId(LSprotocolstepObj1.getProtocolorderstepcode());
						CloudLSprotocolstepInfoObj.setLsprotocolstepInfo(LSprotocolstepObj1.getLsprotocolstepInfo());
						CloudLsLogilabprotocolstepInfoRepository.save(CloudLSprotocolstepInfoObj);
					} else {
						// for getting master step into order step
						LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate
								.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
						if (newLSprotocolstepInfo != null) {
							LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
						}

						LsLogilabprotocolstepInfo LsLogilabprotocolstepInfoObj = new LsLogilabprotocolstepInfo();

						LsLogilabprotocolstepInfoObj.setId(LSprotocolstepObj1.getProtocolorderstepcode());
						LsLogilabprotocolstepInfoObj.setContent(LSprotocolstepObj1.getLsprotocolstepInfo());
						mongoTemplate.insert(LsLogilabprotocolstepInfoObj);

						// Query query = new
						// Query(Criteria.where("id").is(LSprotocolstepObj1.getProtocolorderstepcode()));
						// Update update=new Update();
						// update.set("content",LSprotocolstepObj1.getLsprotocolstepInfo());
						// mongoTemplate.upsert(query, update, LsLogilabprotocolstepInfo.class);
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
				LSlogilabprotocoldetailRepository.save(lSlogilabprotocoldetail);
			}

			mapObj.put("AddedProtocol", lSlogilabprotocoldetail);
		}
		List<LSlogilabprotocoldetail> lstOrder = LSlogilabprotocoldetailRepository
				.findByProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), "N");
		int pendingcount =LSlogilabprotocoldetailRepository.countByProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), "N");
		Collections.sort(lstOrder, Collections.reverseOrder());
		mapObj.put("ListOfProtocol", lstOrder);
		mapObj.put("pendingcount", pendingcount);
		return mapObj;
	}

	public Map<String, Object> getProtocolOrderList(LSlogilabprotocoldetail lSlogilabprotocoldetail) {
		Map<String, Object> lstOrder = new HashMap<String, Object>();
//		List<LSlogilabprotocoldetail> lstPendingOrder = LSlogilabprotocoldetailRepository
//				.findByProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), "N");
		
		List<LSlogilabprotocoldetail> lstPendingOrder = LSlogilabprotocoldetailRepository.findTop10ByProtocoltypeAndOrderflagOrderByCreatedtimestampDesc(lSlogilabprotocoldetail.getProtocoltype(), "N");
		int pendingcount =LSlogilabprotocoldetailRepository.countByProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), "N");
		int completedcount =LSlogilabprotocoldetailRepository.countByProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), "R");
		List<LSlogilabprotocoldetail> lstCompletedOrder = LSlogilabprotocoldetailRepository.findTop10ByProtocoltypeAndOrderflagOrderByCreatedtimestampDesc(lSlogilabprotocoldetail.getProtocoltype(), "R");
		
//		List<LSlogilabprotocoldetail> lstPendingOrder1 = LSlogilabprotocoldetailRepository.getProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), "N");
		
//		List<LSlogilabprotocoldetail> lstCompletedOrder = LSlogilabprotocoldetailRepository
//				.findByProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), "R");
//		Collections.sort(lstPendingOrder, Collections.reverseOrder());
//		Collections.sort(lstCompletedOrder, Collections.reverseOrder());
		lstOrder.put("lstPendingOrder", lstPendingOrder);
		lstOrder.put("lstCompletedOrder", lstCompletedOrder);
		lstOrder.put("pendingcount", pendingcount);
		lstOrder.put("completedcount", completedcount);

		return lstOrder;
	}

	public Map<String, Object> getProtocolOrderListfortabchange(LSlogilabprotocoldetail lSlogilabprotocoldetail) {
		Map<String, Object> lstOrder = new HashMap<String, Object>();

		if(lSlogilabprotocoldetail.getOrderflag().equals("N")) {
			List<LSlogilabprotocoldetail> lstPendingOrder = LSlogilabprotocoldetailRepository.findTop10ByProtocoltypeAndOrderflagOrderByCreatedtimestampDesc(lSlogilabprotocoldetail.getProtocoltype(), "N");
			int pendingcount =LSlogilabprotocoldetailRepository.countByProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), "N");
			lstOrder.put("lstPendingOrder", lstPendingOrder);
			lstOrder.put("pendingcount", pendingcount);
		}else if(lSlogilabprotocoldetail.getOrderflag().equals("R")) {
			int completedcount =LSlogilabprotocoldetailRepository.countByProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), "R");
			List<LSlogilabprotocoldetail> lstCompletedOrder = LSlogilabprotocoldetailRepository.findTop10ByProtocoltypeAndOrderflagOrderByCreatedtimestampDesc(lSlogilabprotocoldetail.getProtocoltype(), "R");
			lstOrder.put("lstCompletedOrder", lstCompletedOrder);			
			lstOrder.put("completedcount", completedcount);
		}
		return lstOrder;
	}
	
	
	public Map<String, Object> getreminProtocolOrderList(LSlogilabprotocoldetail lSlogilabprotocoldetail) {
		Map<String, Object> lstOrder = new HashMap<String, Object>();
	List<LSlogilabprotocoldetail> lstreminingPendingOrder = LSlogilabprotocoldetailRepository.getProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), "N");
	List<LSlogilabprotocoldetail> lstreminingCompletedOrder = LSlogilabprotocoldetailRepository.getProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), "R");
	

		lstOrder.put("lstreminingPendingOrder", lstreminingPendingOrder);
		lstOrder.put("lstreminingCompletedOrder", lstreminingCompletedOrder);
//		lstOrder.put("pendingcount", pendingcount);
//		lstOrder.put("completedcount", completedcount);

		return lstOrder;
	}
	public Map<String, Object> getreminProtocolOrderListontab(LSlogilabprotocoldetail lSlogilabprotocoldetail) {
		Map<String, Object> lstOrder = new HashMap<String, Object>();
		if(lSlogilabprotocoldetail.getOrderflag().equals("N")) {
			List<LSlogilabprotocoldetail> lstreminingPendingOrder = LSlogilabprotocoldetailRepository.getProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), "N");	
			lstOrder.put("lstreminingPendingOrder", lstreminingPendingOrder);
		}else if(lSlogilabprotocoldetail.getOrderflag().equals("R")) {
			List<LSlogilabprotocoldetail> lstreminingCompletedOrder = LSlogilabprotocoldetailRepository.getProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), "R");
			lstOrder.put("lstreminingCompletedOrder", lstreminingCompletedOrder);
		}
		return lstOrder;
	}
	public Map<String, Object> updateProtocolOrderStep(Map<String, Object> argObj) {	Map<String, Object> mapObj = new HashMap<String, Object>();

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
		List<LSprotocolordersampleupdates> lsprotocolordersampleupdates =  new ObjectMapper().convertValue(argObj.get("modifiedsamplestep"),
				new TypeReference<List<LSprotocolordersampleupdates>>() {
				});
		for(LSprotocolordersampleupdates sample :lsprotocolordersampleupdates) {	
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
		List<Lsrepositoriesdata> lsrepositoriesdata =  new ObjectMapper().convertValue(argObj.get("repositorydata"),
				new TypeReference<List<Lsrepositoriesdata>>() {
				});
		for(Lsrepositoriesdata lsrepositoriesdataobj :lsrepositoriesdata) {	
			LsrepositoriesdataRepository.save(lsrepositoriesdataobj);
		}
		
	}

//	List<LSlogilabprotocolsteps> LSprotocolsteplst = LSlogilabprotocolstepsRepository
//			.findByProtocolordercode(LSprotocolstepObj.getProtocolordercode());
	List<LSlogilabprotocolsteps> LSprotocolsteplst = LSlogilabprotocolstepsRepository
			.findByProtocolordercodeAndStatus(LSprotocolstepObj.getProtocolordercode(),1);
	int countforstep =LSlogilabprotocolstepsRepository.countByProtocolordercodeAndStatus(LSprotocolstepObj.getProtocolordercode(),1);
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

	return mapObj;}

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

			int countforstep=0;
			long ipInt = ((Number) argObj.get("protocolmastercode")).longValue();
			List<LSlogilabprotocolsteps> LSprotocolsteplst =new ArrayList<LSlogilabprotocolsteps>();
			if(argObj.containsKey("getfirstlist")) {
				int firstrecord =(int) argObj.get("getfirstlist");
				if(firstrecord == 1) {
					 LSprotocolsteplst = LSlogilabprotocolstepsRepository.findByProtocolordercodeAndStatusAndStepno(ipInt,1,1);
					  countforstep =LSlogilabprotocolstepsRepository.countByProtocolordercodeAndStatus(ipInt,1);
				}else {
					 LSprotocolsteplst = LSlogilabprotocolstepsRepository.findByProtocolordercodeAndStatusAndStepnoNot(ipInt,1,1);
					  countforstep =LSlogilabprotocolstepsRepository.countByProtocolordercodeAndStatus(ipInt,1);
				}
			}else {		
			 LSprotocolsteplst = LSlogilabprotocolstepsRepository.findByProtocolordercode(ipInt);
			}
	
			List<LSlogilabprotocolsteps> LSprotocolstepLst = new ArrayList<LSlogilabprotocolsteps>();

			for (LSlogilabprotocolsteps LSprotocolstepObj1 : LSprotocolsteplst) {

				if (multitenent == 1) {

					CloudLsLogilabprotocolstepInfo newLSprotocolstepInfo = CloudLsLogilabprotocolstepInfoRepository
							.findById(LSprotocolstepObj1.getProtocolorderstepcode());
					if (newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
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
		return mapObj;}

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
		CloudLSprotocolstepInfo CloudLSprotocolstepInfo = new CloudLSprotocolstepInfo();
		LSprotocolstepInfo newLSprotocolstepInfo = new LSprotocolstepInfo();
		@SuppressWarnings("unused")
		List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();
		int i = 0;
		for (LSprotocolstep LSprotocolstepObj1 : LSprotocolsteplst) {
			if (multitenent == 1) {
				CloudLSprotocolstepInfo = CloudLSprotocolstepInfoRepository
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
			}
			LSProtocolStepRepositoryObj.save(LSprotocolstep);
			
			List<LSprotocolsampleupdates> sampleupdatelst = new ArrayList<LSprotocolsampleupdates>();
			if (LSprotocolstep.getProtocolstepcode() != null) {
					sampleupdatelst = LSprotocolsampleupdatesRepository
							.findByprotocolstepcodeAndProtocolmastercode(LSprotocolstepObj1.getProtocolstepcode(),argObj.get("olsprotocolmastercode"));
					for(LSprotocolsampleupdates sampleupdate :sampleupdatelst) {
						sampleupdate.setProtocolmastercode(LSprotocolstep.getProtocolmastercode());
						sampleupdate.setProtocolstepcode(LSprotocolstep.getProtocolstepcode());
						LSprotocolsampleupdatesRepository.save(sampleupdate);
					}
				}
			
			
			List<LSprotocolstep> LSprotocolsteplstforsecond = LSProtocolStepRepositoryObj
					.findByProtocolmastercodeAndStatus(LSprotocolstepObj.getProtocolmastercode(), 1);
			for (int j = i; j < LSprotocolsteplstforsecond.size(); j++) {
				if (multitenent == 1 && CloudLSprotocolstepInfo != null) {
					CloudLSprotocolstepInfo CloudLSprotocolstepInfoforinsert = new CloudLSprotocolstepInfo();
					CloudLSprotocolstepInfoforinsert.setId(LSprotocolsteplstforsecond.get(i).getProtocolstepcode());
					CloudLSprotocolstepInfoforinsert
							.setLsprotocolstepInfo(CloudLSprotocolstepInfo.getLsprotocolstepInfo());
					CloudLSprotocolstepInfoRepository.save(CloudLSprotocolstepInfoforinsert);
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

	public List<LSprotocolsampleupdates> GetProtocolResourcesQuantitylst(LSprotocolstep LSprotocolstep) {

		List<LSprotocolsampleupdates> sampleupdatelst = new ArrayList<LSprotocolsampleupdates>();
		if (LSprotocolstep.getProtocolstepcode() != null) {
			sampleupdatelst = LSprotocolsampleupdatesRepository
					.findByprotocolstepcode(LSprotocolstep.getProtocolstepcode());
		}
		return sampleupdatelst;
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

	public List<LSprotocolordersampleupdates> GetProtocolorderResourcesQuantitylst(
			LSlogilabprotocolsteps LSlogilabprotocolsteps) {

		List<LSprotocolordersampleupdates> sampleupdatelst = new ArrayList<LSprotocolordersampleupdates>();
		if (LSlogilabprotocolsteps.getProtocolordercode() != null) {
			sampleupdatelst = lsprotocolordersampleupdatesRepository.findByprotocolordercodeAndProtocolstepcode(
					LSlogilabprotocolsteps.getProtocolordercode(), LSlogilabprotocolsteps.getProtocolstepcode());
		}
		return sampleupdatelst;
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
		LScfttransaction LScfttransactionobj = new LScfttransaction();

		if (argObj.containsKey("objsilentaudit")) {

			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});

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
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
					}
				} else {

					// Query query = new
					// Query(Criteria.where("id").is(LSprotocolstepObj1.getProtocolstepcode()));
					// query.addCriteria(Criteria.where("versionno").is(LSprotocolstepObj1.getVersionno()));

					// LSprotocolversionstepInfo newLSprotocolstepInfo =
					// mongoTemplate.findOne(query, LSprotocolversionstepInfo.class);

					LSprotocolversionstepInfo newLSprotocolstepInfo = mongoTemplate
							.findById(LSprotocolstepObj1.getProtocolstepversioncode(), LSprotocolversionstepInfo.class);

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
		}
		return mapObj;
	}
}