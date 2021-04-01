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
import com.agaram.eln.primary.model.cloudProtocol.CloudLsLogilabprotocolstepInfo;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocolsteps;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolstep;
import com.agaram.eln.primary.model.protocols.LSprotocolstepInfo;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflow;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflowgroupmap;
import com.agaram.eln.primary.model.protocols.LsLogilabprotocolstepInfo;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.cloudProtocol.CloudLSprotocolstepInfoRepository;
import com.agaram.eln.primary.repository.cloudProtocol.CloudLsLogilabprotocolstepInfoRepository;
import com.agaram.eln.primary.repository.protocol.LSProtocolMasterRepository;
import com.agaram.eln.primary.repository.protocol.LSProtocolStepRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolworkflowgroupmapRepository;
import com.agaram.eln.primary.repository.protocol.lSprotocolworkflowRepository;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;
import com.agaram.eln.primary.service.basemaster.BaseMasterService;
import com.agaram.eln.primary.repository.protocol.LSlogilabprotocoldetailRepository;
import com.agaram.eln.primary.repository.protocol.LSlogilabprotocolstepsRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesdataRepository;

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
	private CloudLsLogilabprotocolstepInfoRepository CloudLsLogilabprotocolstepInfoRepository;
	
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
	
	
	public Map<String, Object> getProtocolMasterInit(Map<String, Object> argObj){
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			LScfttransactionobj.setTableName("LSprotocolmaster");
			lscfttransactionRepository.save(LScfttransactionobj);
		}
			@SuppressWarnings("unchecked")
			List<LSprotocolmaster> LSprotocolmasterLst = (List<LSprotocolmaster>) getLSProtocolMasterLst(argObj).get("LSProtocolMasterLst");
			
			Integer isMulitenant = (Integer) argObj.get("ismultitenant");
			
			if(LSprotocolmasterLst.size() > 0) {
				List<LSprotocolstep> LSprotocolstepLst = LSProtocolStepRepositoryObj.findByProtocolmastercodeAndStatus(LSprotocolmasterLst.get(0).getProtocolmastercode(), 1);
				for(LSprotocolstep LSprotocolstepObj: LSprotocolstepLst) {

					if(isMulitenant == 1) {
						CloudLSprotocolstepInfo newLSprotocolstepInfo = CloudLSprotocolstepInfoRepository.findById(LSprotocolstepObj.getProtocolstepcode());
						if(newLSprotocolstepInfo != null) {
							LSprotocolstepObj.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
						}
					}
					else {
						LSprotocolstepInfo LSprotocolstepInfoObj = mongoTemplate.findById(LSprotocolstepObj.getProtocolstepcode(), LSprotocolstepInfo.class);
						if(LSprotocolstepInfoObj != null) {
							LSprotocolstepObj.setLsprotocolstepInfo(LSprotocolstepInfoObj.getContent());
						}
					}
//					LSprotocolstepInfo LSprotocolstepInfoObj = mongoTemplate.findById(LSprotocolstepObj.getProtocolstepcode(), LSprotocolstepInfo.class);
//					if(LSprotocolstepInfoObj != null) {
//						LSprotocolstepObj.setLsprotocolstepInfo(LSprotocolstepInfoObj.getContent());
//					}
				}
				
				LSprotocolmasterLst = LSprotocolmasterLst.stream()
						.distinct().collect(Collectors.toList());
				
				Collections.sort(LSprotocolmasterLst, Collections.reverseOrder());
				
				mapObj.put("protocolmasterLst", LSprotocolmasterLst);
				mapObj.put("protocolstepLst", LSprotocolstepLst);
			}else {
				LSprotocolmasterLst = LSprotocolmasterLst.stream()
						.distinct().collect(Collectors.toList());
				
				Collections.sort(LSprotocolmasterLst, Collections.reverseOrder());
				
				mapObj.put("protocolmasterLst", LSprotocolmasterLst);
				mapObj.put("protocolstepLst", new ArrayList<>());
			}
		
		return mapObj;
	}
	
	public Map<String, Object> getLSProtocolMasterLst(Map<String, Object> argObj){
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			
			List<LSprotocolmaster> LSprotocolmasterLst = new ArrayList<>();
		
			LSprotocolworkflow lsprotocolworkflow = new LSprotocolworkflow();
			if(LScfttransactionobj.getUsername().equalsIgnoreCase("Administrator")) {
//				for need site vice filter on protocol for Administrator login
				//LSprotocolmasterLst = LSProtocolMasterRepositoryObj.findByStatusAndLssitemaster( 1, LScfttransactionobj.getLssitemaster());
				
				LSprotocolmasterLst =  LSProtocolMasterRepositoryObj.findByStatus(1);
			}
			else {
				LSprotocolmasterLst =  LSProtocolMasterRepositoryObj.findByCreatedbyAndStatusAndLssitemaster(LScfttransactionobj.getLsuserMaster(), 1, LScfttransactionobj.getLssitemaster());
				
				if(argObj.containsKey("lsusergroup")) {
					LSusergroup lsusergroup= new ObjectMapper().convertValue(argObj.get("lsusergroup"),
							new TypeReference<LSusergroup>() {
							});
					
					LSprotocolworkflowgroupmap lsprotocolworkflowgroupmap= LSprotocolworkflowgroupmapRepository.findBylsusergroup(lsusergroup);
					
					if(lsprotocolworkflowgroupmap != null) {
						lsprotocolworkflow = lSprotocolworkflowRepository.findByworkflowcode(lsprotocolworkflowgroupmap.getWorkflowcode());
						
//						List<LSprotocolmaster> LSprotocolmasterLst1 = LSProtocolMasterRepositoryObj.findByStatusAndLssitemasterAndLSprotocolworkflowAndCreatedbyNot(1, LScfttransactionobj.getLssitemaster(),lsprotocolworkflow ,LScfttransactionobj.getLsuserMaster());
						List<LSprotocolmaster> LSprotocolmasterLst1 = LSProtocolMasterRepositoryObj
								.findByStatusAndLssitemasterAndLSprotocolworkflowAndCreatedbyNotAndSharewithteam(1, LScfttransactionobj.getLssitemaster(),lsprotocolworkflow ,LScfttransactionobj.getLsuserMaster(),0);
						LSprotocolmasterLst.addAll(LSprotocolmasterLst1);
						
						List<LSprotocolmaster> LSprotocolmasterLst2 = LSProtocolMasterRepositoryObj
								.findByStatusAndLssitemasterAndLSprotocolworkflowNotAndCreatedbyNotAndSharewithteamAndApproved
								(1, LScfttransactionobj.getLssitemaster(),lsprotocolworkflow ,LScfttransactionobj.getLsuserMaster(),0,0);
						
						LSprotocolmasterLst.addAll(LSprotocolmasterLst2);
					}
				}	
			}			
			
			List<Integer> teamCodeLst = LSuserteammappingRepositoryObj.getTeamcodeByLsuserMaster4postgressandsql(LScfttransactionobj.getLsuserMaster());
				
				if(teamCodeLst.size() > 0) {
					List<LSuserMaster> lsusermasterLst = LSuserteammappingRepositoryObj.getLsuserMasterByTeamcode(teamCodeLst);
				
					if(lsusermasterLst.size() > 0) {
					for(LSuserMaster lsusermasterObj : lsusermasterLst) {
//						if(lsusermasterObj.getUsercode() != LScfttransactionobj.getLsuserMaster()) {
	//						List<LSprotocolmaster> LSprotocolmasterTempLst = LSProtocolMasterRepositoryObj.findByCreatedbyNotAndStatusAndLssitemasterAndSharewithteam(lsusermasterObj.getUsercode(), 1, lsusermasterObj.getLssitemaster().getSitecode(), 1);
							List<LSprotocolmaster> LSprotocolmasterTempLst = new ArrayList<>();
							if(lsprotocolworkflow.getWorkflowname() != null) {
								LSprotocolmasterTempLst = LSProtocolMasterRepositoryObj.
										findByCreatedbyNotAndStatusAndLssitemasterAndSharewithteamAndLSprotocolworkflowNot(
												lsusermasterObj.getUsercode(), 1,lsusermasterObj.getLssitemaster().getSitecode(), 1,lsprotocolworkflow);
							}
							else {
								LSprotocolmasterTempLst = LSProtocolMasterRepositoryObj.
										findByCreatedbyNotAndStatusAndLssitemasterAndSharewithteam(
												lsusermasterObj.getUsercode(), 1,lsusermasterObj.getLssitemaster().getSitecode(),1);
							}
							if(LSprotocolmasterTempLst.size() > 0) {
								LSprotocolmasterLst.addAll(LSprotocolmasterTempLst);
							}
//						}
					}
				}
			}
				//			Collections.sort(LSprotocolmasterLst);
				//			Collections.sort(LSprotocolmasterLst, Collections.reverseOrder());
				LSprotocolmasterLst = LSprotocolmasterLst.stream()
						.distinct().collect(Collectors.toList());
				
				Collections.sort(LSprotocolmasterLst, Collections.reverseOrder());
				
				mapObj.put("LSProtocolMasterLst", LSprotocolmasterLst);
		}
		
		return mapObj;
	}
	
	public Map<String, Object> getProtocolStepLst(Map<String, Object> argObj){
		Map<String, Object> mapObj = new HashMap<String, Object>();
		@SuppressWarnings("unused")
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			LSprotocolmaster newProtocolMasterObj = new ObjectMapper().convertValue(argObj.get("ProtocolMasterObj"), new TypeReference<LSprotocolmaster>(){});
			List<LSprotocolstep> LSprotocolsteplst = LSProtocolStepRepositoryObj.findByProtocolmastercodeAndStatus(newProtocolMasterObj.getProtocolmastercode() , 1);
			List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();
			for(LSprotocolstep LSprotocolstepObj1: LSprotocolsteplst) {
				if(newProtocolMasterObj.getIsmultitenant() == 1) {
					CloudLSprotocolstepInfo newLSprotocolstepInfo = CloudLSprotocolstepInfoRepository.findById(LSprotocolstepObj1.getProtocolstepcode());
					if(newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
					}
				}
				else {
					LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
					if(newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
					}
				}
//				LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
//				if(newLSprotocolstepInfo != null) {
//					LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
//				}
				LSprotocolstepLst.add(LSprotocolstepObj1);
//				LSprotocolstepObj1.setLsprotocolstepInfo(mongoTemplate.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class).getContent());
			}
			if(LSprotocolsteplst != null) {
				mapObj.put("protocolstepLst", LSprotocolstepLst);
			}else {
				mapObj.put("protocolstepLst", new ArrayList<>());
			}
		}
		return mapObj;
	}
	
	public Map<String, Object> getAllProtocolStepLst(Map<String, Object> argObj){
		Map<String, Object> mapObj = new HashMap<String, Object>();
		
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			@SuppressWarnings("unused")
			LSprotocolmaster newProtocolMasterObj = new ObjectMapper().convertValue(argObj.get("ProtocolMasterObj"), new TypeReference<LSprotocolmaster>(){});
			List<LSprotocolstep> LSprotocolsteplst = LSProtocolStepRepositoryObj.findByStatusAndSitecode(1, LScfttransactionobj.getLssitemaster());
			List<LSprotocolstep> LSprotocolstepLstUpdate = new ArrayList<LSprotocolstep>();
			for(LSprotocolstep LSprotocolstepObj1: LSprotocolsteplst) {
				/**
				 * Added by sathishkumar chandrasekar for smultitenant
				 * 
				 */
				if((int)argObj.get("ismultitenant") == 1) {
					CloudLSprotocolstepInfo newLSprotocolstepInfo = CloudLSprotocolstepInfoRepository.findById(LSprotocolstepObj1.getProtocolstepcode());
					if(newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
					}
				}
				else {
					LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
					if(newLSprotocolstepInfo != null) {
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
			if(LSprotocolsteplst != null) {
				mapObj.put("protocolstepLst", LSprotocolstepLstUpdate);
			}else {
				mapObj.put("protocolstepLst", new ArrayList<>());
			}
		}
		return mapObj;
	}
	
	public Map<String, Object> getOrdersLinkedToProtocol(Map<String, Object> argObj){
		Map<String, Object> mapObj = new HashMap<String, Object>();
		@SuppressWarnings("unused")
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			@SuppressWarnings("unchecked")
			ArrayList<Integer> protocolmastercodeArray = (ArrayList<Integer>) argObj.get("protocolmastercodeArray");
			List<LSprotocolmaster> protocolmasterArray = LSProtocolMasterRepositoryObj.findByProtocolmastercodeIn(protocolmastercodeArray);
			List<LSlogilabprotocoldetail> LSlogilabprotocoldetailLst = LSlogilabprotocoldetailRepository.findByLsprotocolmaster(protocolmasterArray);
			if(LSlogilabprotocoldetailLst != null) {
				mapObj.put("LSlogilabprotocoldetailLst", LSlogilabprotocoldetailLst);
			}else {
				mapObj.put("LSlogilabprotocoldetailLst", new ArrayList<>());
			}
		}
		return mapObj;
	}
	
	public Map<String, Object> addProtocolStep(Map<String, Object> argObj){
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			
		}
		ObjectMapper objMapper= new ObjectMapper();
		LoggedUser objUser = new LoggedUser();
		//		silent audit
		if(LScfttransactionobj!=null ) {
			LScfttransactionobj.setTableName("LSprotocolmaster");
			if(argObj.containsKey("username")) {
				String username= objMapper.convertValue(argObj.get("username"), String.class);
				//			String sitecode= objMapper.convertValue(argObj.get("lssitemaster"), String.class);
				LSSiteMaster objsite = LSSiteMasterRepository.findBysitecode(LScfttransactionobj.getLssitemaster());
				LSuserMaster objuser= LSuserMasterRepositoryObj.findByusernameAndLssitemaster(username, objsite);
				LScfttransactionobj.setLsuserMaster(objuser.getUsercode());
//				cfttransaction.setLssitemaster(objuser.getLssitemaster());
				LScfttransactionobj.setLssitemaster(objuser.getLssitemaster().getSitecode());
				LScfttransactionobj.setUsername(username);
			}
			lscfttransactionRepository.save(LScfttransactionobj);
		}
//		manual audit
		if(argObj.containsKey("objuser")) {
			objUser=objMapper.convertValue(argObj.get("objuser"), LoggedUser.class);
			if(argObj.containsKey("objmanualaudit")) {
				LScfttransaction objmanualaudit=new LScfttransaction();
				objmanualaudit = objMapper.convertValue(argObj.get("objmanualaudit"), LScfttransaction.class);
				
				objmanualaudit.setComments(objUser.getComments());
				lscfttransactionRepository.save(objmanualaudit);
			}
		}
		
			if(argObj.containsKey("newProtocolstepObj")) {
				LSprotocolstep LSprotocolstepObj = new ObjectMapper().convertValue(argObj.get("newProtocolstepObj"), new TypeReference<LSprotocolstep>() {});
				LSuserMaster LsuserMasterObj = LSuserMasterRepositoryObj.findByusercode(LScfttransactionobj.getLsuserMaster());
				if(LSprotocolstepObj.getStatus() == null) {
					LSprotocolstepObj.setStatus(1);
					LSprotocolstepObj.setCreatedby(LScfttransactionobj.getLsuserMaster());
					LSprotocolstepObj.setCreatedbyusername(LsuserMasterObj.getUsername());
					LSprotocolstepObj.setCreateddate(new Date());
					LSprotocolstepObj.setSitecode(LScfttransactionobj.getLssitemaster());
				}
				LSProtocolStepRepositoryObj.save(LSprotocolstepObj); 

				CloudLSprotocolstepInfo CloudLSprotocolstepInfoObj = new CloudLSprotocolstepInfo();
				if(LSprotocolstepObj.getIsmultitenant() == 1) {
					if(LSprotocolstepObj.getNewStep() == 1) {
						
						CloudLSprotocolstepInfoObj.setId(LSprotocolstepObj.getProtocolstepcode());
						CloudLSprotocolstepInfoObj.setLsprotocolstepInfo(LSprotocolstepObj.getLsprotocolstepInfo());
						CloudLSprotocolstepInfoRepository.save(CloudLSprotocolstepInfoObj);	
					}else {
						CloudLSprotocolstepInfo updateLSprotocolstepInfo = CloudLSprotocolstepInfoRepository.findById(LSprotocolstepObj.getProtocolstepcode());
						updateLSprotocolstepInfo.setLsprotocolstepInfo(LSprotocolstepObj.getLsprotocolstepInfo());
						CloudLSprotocolstepInfoRepository.save(updateLSprotocolstepInfo);
					}
				}
				else {
						Query query = new Query(Criteria.where("id").is(LSprotocolstepObj.getProtocolstepcode()));
						Update update=new Update();
						update.set("content",LSprotocolstepObj.getLsprotocolstepInfo());
						
						mongoTemplate.upsert(query, update, LSprotocolstepInfo.class);
				}
				/**
				 * Existing code start
				 */
				
//				if(LSprotocolstepObj.getStatus() != null) {
////					mongoTemplate.remove(LSprotocolstepInfoObj);
//					Query query = new Query(Criteria.where("id").is(LSprotocolstepObj.getProtocolstepcode()));
//					Update update=new Update();
//					update.set("content",LSprotocolstepObj.getLsprotocolstepInfo());
//					
//					mongoTemplate.upsert(query, update, LSprotocolstepInfo.class);
//				}else {
//					LSprotocolstepInfoObj.setId(LSprotocolstepObj.getProtocolstepcode());
//					LSprotocolstepInfoObj.setContent(LSprotocolstepObj.getLsprotocolstepInfo());
//					mongoTemplate.insert(LSprotocolstepInfoObj);
//				}
				/**
				 * end
				 */
				List<LSprotocolstep> tempLSprotocolstepLst = LSProtocolStepRepositoryObj.findByProtocolmastercodeAndStatus(LSprotocolstepObj.getProtocolmastercode(), 1);
				List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();
				for(LSprotocolstep LSprotocolstepObj1: tempLSprotocolstepLst) {
					if(LSprotocolstepObj.getIsmultitenant() == 1) {
						if(LSprotocolstepObj.getNewStep() == 1) {
							LSprotocolstepObj1.setLsprotocolstepInfo(CloudLSprotocolstepInfoObj.getLsprotocolstepInfo());
						}else {
							CloudLSprotocolstepInfo newLSprotocolstepInfo = CloudLSprotocolstepInfoRepository.findById(LSprotocolstepObj.getProtocolstepcode());
							if(newLSprotocolstepInfo != null) {
								LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
							}	
						}
					}
					else {
						LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
						if(newLSprotocolstepInfo != null) {
							LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
						}
					}
					LSprotocolstepLst.add(LSprotocolstepObj1);
				}

				mapObj.put("protocolstepLst", LSprotocolstepLst);
			}
			
		return mapObj;
	}
	
	public Map<String, Object> deleteProtocolStep(Map<String, Object> argObj){
		Map<String, Object> mapObj = new HashMap<String, Object>();
		@SuppressWarnings("unused")
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),new TypeReference<LScfttransaction>() {});
			
			List<LSprotocolstep> updateLSprotocolstepLst = new ObjectMapper().convertValue(argObj.get("protocolstepLst"), new TypeReference<List<LSprotocolstep>>() {});
			for(LSprotocolstep LSprotocolstepObj1: updateLSprotocolstepLst) {
				LSProtocolStepRepositoryObj.save(LSprotocolstepObj1);				
			}
			
			List<LSprotocolstep> tempLSprotocolstepLst = LSProtocolStepRepositoryObj.findByProtocolmastercodeAndStatus((Integer)argObj.get("protocolmastercode"), 1);
			List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();
			for(LSprotocolstep LSprotocolstepObj1: tempLSprotocolstepLst) {
				/**
				 * Added by sathishkumar chandrasekar for multitenant
				 */
				if(LSprotocolstepObj1.getIsmultitenant() == 1) {
					CloudLSprotocolstepInfo newLSprotocolstepInfo = CloudLSprotocolstepInfoRepository.findById(LSprotocolstepObj1.getProtocolstepcode());
					if(newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
					}
				}
				else {
					LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
					if(newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
					}
				}
//				LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
//				if(newLSprotocolstepInfo != null) {
//					LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
//				}
				LSprotocolstepLst.add(LSprotocolstepObj1);
			}
			mapObj.put("protocolstepLst", LSprotocolstepLst);
		}
		return mapObj;
	}
	
	public Map<String, Object> addProtocolMaster(Map<String, Object> argObj){
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
		}
		ObjectMapper objMapper= new ObjectMapper();
		LoggedUser objUser = new LoggedUser();
		
//		silent audit
		if(LScfttransactionobj!=null ) {
			LScfttransactionobj.setTableName("LSprotocolmaster");
			if(argObj.containsKey("username")) {
				String username= objMapper.convertValue(argObj.get("username"), String.class);
				//String sitecode= objMapper.convertValue(argObj.get("lssitemaster"), String.class);
				LSSiteMaster objsite = LSSiteMasterRepository.findBysitecode(LScfttransactionobj.getLssitemaster());
				LSuserMaster objuser= LSuserMasterRepositoryObj.findByusernameAndLssitemaster(username, objsite);
				LScfttransactionobj.setLsuserMaster(objuser.getUsercode());
//				cfttransaction.setLssitemaster(objuser.getLssitemaster());
				LScfttransactionobj.setLssitemaster(objuser.getLssitemaster().getSitecode());
				LScfttransactionobj.setUsername(username);
			}
			lscfttransactionRepository.save(LScfttransactionobj);
		}
//		manual audit
		if(argObj.containsKey("objuser")) {
			objUser=objMapper.convertValue(argObj.get("objuser"), LoggedUser.class);
			if(argObj.containsKey("objmanualaudit")) {
				LScfttransaction objmanualaudit=new LScfttransaction();
				objmanualaudit = objMapper.convertValue(argObj.get("objmanualaudit"), LScfttransaction.class);
				
				objmanualaudit.setComments(objUser.getComments());
				lscfttransactionRepository.save(objmanualaudit);
			}
		}
			if(argObj.containsKey("newProtocolMasterObj")) {
				LSuserMaster LsuserMasterObj = LSuserMasterRepositoryObj.findByusercode(LScfttransactionobj.getLsuserMaster());
				LSprotocolmaster newProtocolMasterObj = new LSprotocolmaster();
				if(argObj.containsKey("edit")) {
					LSprotocolmaster newProtocolMasterObj1 = new ObjectMapper().convertValue(argObj.get("newProtocolMasterObj"), new TypeReference<LSprotocolmaster>() {});
					newProtocolMasterObj = LSProtocolMasterRepositoryObj.findFirstByProtocolmastercodeAndStatusAndLssitemaster(newProtocolMasterObj1.getProtocolmastercode(), 1, LScfttransactionobj.getLssitemaster());
					newProtocolMasterObj.setProtocolmastername(newProtocolMasterObj1.getProtocolmastername());
				}else {
					newProtocolMasterObj = new ObjectMapper().convertValue(argObj.get("newProtocolMasterObj"), new TypeReference<LSprotocolmaster>() {});
					newProtocolMasterObj.setCreatedate(new Date());
					newProtocolMasterObj.setLssitemaster(LScfttransactionobj.getLssitemaster());
					newProtocolMasterObj.setCreatedbyusername(LsuserMasterObj.getUsername());
					LSSiteMaster lssitemaster = LSSiteMasterRepository.findBysitecode(LScfttransactionobj.getLssitemaster());
					LSprotocolworkflow lsprotocolworkflow = lSprotocolworkflowRepository.findTopByAndLssitemasterOrderByWorkflowcodeAsc(lssitemaster);
					newProtocolMasterObj.setlSprotocolworkflow(lsprotocolworkflow);
				}
				LSProtocolMasterRepositoryObj.save(newProtocolMasterObj);
				
				List<LSprotocolmaster> LSprotocolmasterLst = LSProtocolMasterRepositoryObj.
						findByCreatedbyAndStatusAndLssitemaster(LScfttransactionobj.getLsuserMaster(), 1, 
								LScfttransactionobj.getLssitemaster());
				List<LSprotocolmaster> AddedLSprotocolmasterObj =LSProtocolMasterRepositoryObj.
						findByStatusAndLssitemasterAndProtocolmastername(1, LScfttransactionobj.getLssitemaster(), 
								newProtocolMasterObj.getProtocolmastername());
				
				LSprotocolworkflow lsprotocolworkflow = new LSprotocolworkflow();
				
				if(argObj.containsKey("lsusergroup")) {
					LSusergroup lsusergroup= new ObjectMapper().convertValue(argObj.get("lsusergroup"),
							new TypeReference<LSusergroup>() {
							});
					
					LSprotocolworkflowgroupmap lsprotocolworkflowgroupmap= LSprotocolworkflowgroupmapRepository.findBylsusergroup(lsusergroup);
					
					if(lsprotocolworkflowgroupmap != null) {
						lsprotocolworkflow = lSprotocolworkflowRepository.findByworkflowcode(lsprotocolworkflowgroupmap.getWorkflowcode());
						
						List<LSprotocolmaster> LSprotocolmasterLst1 = LSProtocolMasterRepositoryObj.
								findByStatusAndLssitemasterAndLSprotocolworkflowAndCreatedbyNot
								(1,LScfttransactionobj.getLssitemaster(),
										lsprotocolworkflow,LScfttransactionobj.getLsuserMaster());
					
						LSprotocolmasterLst.addAll(LSprotocolmasterLst1);
						
						List<LSprotocolmaster> LSprotocolmasterLst2 = LSProtocolMasterRepositoryObj
								.findByStatusAndLssitemasterAndLSprotocolworkflowNotAndCreatedbyNotAndSharewithteamAndApproved
								(1, LScfttransactionobj.getLssitemaster(),lsprotocolworkflow ,LScfttransactionobj.getLsuserMaster(),0,0);
						
						LSprotocolmasterLst.addAll(LSprotocolmasterLst2);
					}
				}
				
				if(argObj.containsKey("edit")) {
					Map<String, Object> argObj1 = new HashMap<String, Object>();
					argObj1.put("objsilentaudit", argObj.get("objsilentaudit"));
					argObj1.put("ProtocolMasterObj", argObj.get("newProtocolMasterObj"));
					Map<String, Object> ProtocolStepLstMap = getProtocolStepLst(argObj1);
					mapObj.put("protocolstepLst", ProtocolStepLstMap.get("protocolstepLst"));
				}else {
					List<Integer> teamCodeLst = LSuserteammappingRepositoryObj.
							getTeamcodeByLsuserMaster4postgressandsql(LScfttransactionobj.getLsuserMaster());
					
					if(teamCodeLst.size() > 0) {
						List<LSuserMaster> lsusermasterLst = LSuserteammappingRepositoryObj.getLsuserMasterByTeamcode(teamCodeLst);
					
						if(lsusermasterLst.size() > 0) {
							for(LSuserMaster lsusermasterObj : lsusermasterLst) {
								List<LSprotocolmaster> LSprotocolmasterTempLst = new ArrayList<>();
								if(lsprotocolworkflow.getWorkflowname() != null) {
									LSprotocolmasterTempLst = LSProtocolMasterRepositoryObj.
											findByCreatedbyNotAndStatusAndLssitemasterAndSharewithteamAndLSprotocolworkflowNot(
													lsusermasterObj.getUsercode(), 1,lsusermasterObj.getLssitemaster().getSitecode(), 1,lsprotocolworkflow);
								}
								else {
									LSprotocolmasterTempLst = LSProtocolMasterRepositoryObj.
											findByCreatedbyNotAndStatusAndLssitemasterAndSharewithteam(
													lsusermasterObj.getUsercode(), 1,lsusermasterObj.getLssitemaster().getSitecode(),1);
								}
								if(LSprotocolmasterTempLst.size() > 0) {
									LSprotocolmasterLst.addAll(LSprotocolmasterTempLst);
								}
							}
						}
					}
					mapObj.put("protocolstepLst", new ArrayList<Object>());
				}
				LSprotocolmasterLst = LSprotocolmasterLst.stream()
						.distinct().collect(Collectors.toList());
				
				Collections.sort(LSprotocolmasterLst, Collections.reverseOrder());
				
				mapObj.put("protocolmasterLst", LSprotocolmasterLst);
				mapObj.put("AddedLSprotocolmasterObj", AddedLSprotocolmasterObj);
			}
		
		return mapObj;
	}
	
	public Map<String, Object> deleteProtocolMaster(Map<String, Object> argObj){
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			}
		ObjectMapper objMapper= new ObjectMapper();
		LoggedUser objUser = new LoggedUser();
		
//		silent audit
		if(LScfttransactionobj!=null ) {
			LScfttransactionobj.setTableName("LSprotocolmaster");
			if(argObj.containsKey("username")) {
				String username= objMapper.convertValue(argObj.get("username"), String.class);
				LSSiteMaster objsite = LSSiteMasterRepository.findBysitecode(LScfttransactionobj.getLssitemaster());
				LSuserMaster objuser= LSuserMasterRepositoryObj.findByusernameAndLssitemaster(username, objsite);
				LScfttransactionobj.setLsuserMaster(objuser.getUsercode());
//				cfttransaction.setLssitemaster(objuser.getLssitemaster());
				LScfttransactionobj.setLssitemaster(objuser.getLssitemaster().getSitecode());
				LScfttransactionobj.setUsername(username);
			}
			lscfttransactionRepository.save(LScfttransactionobj);
		}
//		manual audit
		if(argObj.containsKey("objuser")) {
			objUser=objMapper.convertValue(argObj.get("objuser"), LoggedUser.class);
			if(argObj.containsKey("objmanualaudit")) {
				LScfttransaction objmanualaudit=new LScfttransaction();
				objmanualaudit = objMapper.convertValue(argObj.get("objmanualaudit"), LScfttransaction.class);
				
				objmanualaudit.setComments(objUser.getComments());
				lscfttransactionRepository.save(objmanualaudit);
			}
		}
			if(argObj.containsKey("ProtocolMasterObj")) {
				LSprotocolmaster newProtocolMasterObj = new ObjectMapper().convertValue(argObj.get("ProtocolMasterObj"), new TypeReference<LSprotocolmaster>() {});
				newProtocolMasterObj.setProtocolstatus(0);
				newProtocolMasterObj.setStatus(0);
				LSProtocolMasterRepositoryObj.save(newProtocolMasterObj);
				List<LSprotocolmaster> LSprotocolmasterLst = LSProtocolMasterRepositoryObj.findByCreatedbyAndStatusAndLssitemaster(LScfttransactionobj.getLsuserMaster(), 1,  LScfttransactionobj.getLssitemaster());
				
				/**
				 * Added by sathishkumar chandrasekar for getting after deleted records getting with workflow also
				 */
				
				LSprotocolworkflow lsprotocolworkflow = new LSprotocolworkflow();
				
				if(argObj.containsKey("lsusergroup")) {
					LSusergroup lsusergroup= new ObjectMapper().convertValue(argObj.get("lsusergroup"),
							new TypeReference<LSusergroup>() {
							});
					
					LSprotocolworkflowgroupmap lsprotocolworkflowgroupmap= LSprotocolworkflowgroupmapRepository.findBylsusergroup(lsusergroup);
					
					if(lsprotocolworkflowgroupmap != null) {
						lsprotocolworkflow = lSprotocolworkflowRepository.findByworkflowcode(lsprotocolworkflowgroupmap.getWorkflowcode());
						
						List<LSprotocolmaster> LSprotocolmasterLst1 = LSProtocolMasterRepositoryObj.
								findByStatusAndLssitemasterAndLSprotocolworkflowAndCreatedbyNot
								(1,LScfttransactionobj.getLssitemaster(),
										lsprotocolworkflow,LScfttransactionobj.getLsuserMaster());
					
						LSprotocolmasterLst.addAll(LSprotocolmasterLst1);
					}
				}
				/**
				 * END
				 */
				LSprotocolmasterLst = LSprotocolmasterLst.stream()
						.distinct().collect(Collectors.toList());
				
				Collections.sort(LSprotocolmasterLst, Collections.reverseOrder());
				
				mapObj.put("protocolmasterLst", LSprotocolmasterLst);
				
				Map<String, Object> argObj1 = new HashMap<String, Object>();
				argObj1.put("objsilentaudit", argObj.get("objsilentaudit"));
				if(LSprotocolmasterLst.size() > 0) {
					LSprotocolmasterLst.get(0).setIsmultitenant(newProtocolMasterObj.getIsmultitenant());
					argObj1.put("ProtocolMasterObj", LSprotocolmasterLst.get(0));
					Map<String, Object> ProtocolStepLstMap = getProtocolStepLst(argObj1);
					mapObj.put("protocolstepLst", ProtocolStepLstMap.get("protocolstepLst"));
				}
			}
		
		return mapObj;
	}
	
	public Map<String, Object> sharewithteam(Map<String, Object> argObj){
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),new TypeReference<LScfttransaction>() {});
		ObjectMapper objMapper= new ObjectMapper();
		LoggedUser objUser = new LoggedUser();
		
//		silent audit
		if(LScfttransactionobj!=null ) {
			LScfttransactionobj.setTableName("LSprotocolmaster");
			if(argObj.containsKey("username")) {
				String username= objMapper.convertValue(argObj.get("username"), String.class);
				//			String sitecode= objMapper.convertValue(argObj.get("lssitemaster"), String.class);
				LSSiteMaster objsite = LSSiteMasterRepository.findBysitecode(LScfttransactionobj.getLssitemaster());
				LSuserMaster objuser= LSuserMasterRepositoryObj.findByusernameAndLssitemaster(username, objsite);
				LScfttransactionobj.setLsuserMaster(objuser.getUsercode());
//				cfttransaction.setLssitemaster(objuser.getLssitemaster());
				LScfttransactionobj.setLssitemaster(objuser.getLssitemaster().getSitecode());
				LScfttransactionobj.setUsername(username);
			}
			lscfttransactionRepository.save(LScfttransactionobj);
		}
//		manual audit
		if(argObj.containsKey("objuser")) {
			objUser=objMapper.convertValue(argObj.get("objuser"), LoggedUser.class);
			if(argObj.containsKey("objmanualaudit")) {
				LScfttransaction objmanualaudit=new LScfttransaction();
				objmanualaudit = objMapper.convertValue(argObj.get("objmanualaudit"), LScfttransaction.class);
				
				objmanualaudit.setComments(objUser.getComments());
				lscfttransactionRepository.save(objmanualaudit);
			}
		}
			LSprotocolmaster LSprotocolmasterObj = new ObjectMapper().convertValue(argObj.get("ProtocolMasterObj"), new TypeReference<LSprotocolmaster>() { });
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
		
		if(objClass.getApproved() != null) {
			approved = objClass.getApproved();
		}
		
		LSprotocolmaster LsProto = LSProtocolMasterRepositoryObj.findFirstByProtocolmastercode(objClass.getProtocolmastercode());
		LSProtocolMasterRepositoryObj.updateFileWorkflow(objClass.getlSprotocolworkflow(),
				approved,objClass.getRejected(), objClass.getProtocolmastercode());
		
		LsProto.setlSprotocolworkflow(objClass.getlSprotocolworkflow());
		if(LsProto.getApproved() == null) {
			LsProto.setApproved(0);	
		}
		
		mapObj.put("ProtocolObj", LsProto);
		mapObj.put("status", "success");
		
		return mapObj;		
	}

	public List<LSprotocolworkflow> GetProtocolWorkflow(LSprotocolworkflow objclass) {
		return lSprotocolworkflowRepository.findBylssitemaster(objclass.getLssitemaster());
	}

	public List<LSprotocolworkflow> InsertUpdatesheetWorkflow(List<LSprotocolworkflow> lSprotocolworkflow) {
		for(LSprotocolworkflow flow : lSprotocolworkflow)
		{
			LSprotocolworkflowgroupmapRepository.save(flow.getLsprotocolworkflowgroupmap());
			lSprotocolworkflowRepository.save(flow);
		}
		
		if(lSprotocolworkflow.get(0).getObjsilentaudit() != null)
		{
			lSprotocolworkflow.get(0).getObjsilentaudit().setTableName("LSfiletest");
			lscfttransactionRepository.save(lSprotocolworkflow.get(0).getObjsilentaudit());
		}
		if(lSprotocolworkflow.get(0).getObjuser() != null) {
			Date date = new Date();
			
			lSprotocolworkflow.get(0).getObjmanualaudit().setComments(lSprotocolworkflow.get(0).getObjuser().getComments());
			lSprotocolworkflow.get(0).getObjmanualaudit().setTableName("lSprotocolworkflow");
			lSprotocolworkflow.get(0).getObjmanualaudit().setLsuserMaster(lSprotocolworkflow.get(0).getObjsilentaudit().getLsuserMaster());
			lSprotocolworkflow.get(0).getObjmanualaudit().setLssitemaster(lSprotocolworkflow.get(0).getLssitemaster().getSitecode());
			lSprotocolworkflow.get(0).getObjmanualaudit().setTransactiondate(date);
			lscfttransactionRepository.save(lSprotocolworkflow.get(0).getObjmanualaudit());
		}
		lSprotocolworkflow.get(0).setResponse(new Response());
		lSprotocolworkflow.get(0).getResponse().setStatus(true);
		lSprotocolworkflow.get(0).getResponse().setInformation("ID_SHEETMSG");
		
		return lSprotocolworkflow;
	}

	public Response Deletesheetworkflow(LSprotocolworkflow objflow) {
		Response response = new Response();
			
		long onprocess = LSProtocolMasterRepositoryObj.countBylSprotocolworkflowAndApproved(objflow, 0);
		if(onprocess > 0)
		{
			response.setStatus(false);
		}
		else
		{
			LSProtocolMasterRepositoryObj.setWorkflownullforApprovedProtcol(objflow, 1);
			lSprotocolworkflowRepository.delete(objflow);
			LSprotocolworkflowgroupmapRepository.delete(objflow.getLsprotocolworkflowgroupmap());
			response.setStatus(true);
			if(objflow.getObjsilentaudit()!=null) {
				objflow.getObjsilentaudit().setTableName("LSsheetworkflow");
	    		lscfttransactionRepository.save(objflow.getObjsilentaudit());
			}
		}
		return response;
	}

	public List<LSprotocolmaster> getProtocolMasterList(LSuserMaster objClass) {
		return LSProtocolMasterRepositoryObj.findByStatusAndLssitemasterAndApproved(1, objClass.getLssitemaster().getSitecode(),1);
	}

	public Map<String, Object> addProtocolOrder(LSlogilabprotocoldetail lSlogilabprotocoldetail) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		if(lSlogilabprotocoldetail != null) {
			
			LSlogilabprotocoldetailRepository.save(lSlogilabprotocoldetail);
			
			if(lSlogilabprotocoldetail.getProtocolordercode() != null) {
				
				String ProtocolOrderName ="ELN"+lSlogilabprotocoldetail.getProtocolordercode();
				
				lSlogilabprotocoldetail.setProtoclordername(ProtocolOrderName);
				lSlogilabprotocoldetail.setOrderflag("N");
				
				List<LSprotocolstep> lstSteps=LSProtocolStepRepositoryObj
						.findByProtocolmastercode(lSlogilabprotocoldetail.getLsprotocolmaster().getProtocolmastercode());
				
				//int i =0;
				
				List<LSlogilabprotocolsteps> lststep1 = new ObjectMapper().convertValue(lstSteps,
						new TypeReference<List<LSlogilabprotocolsteps>>() {
						});
				
				/*while(i < lstSteps.size()) {
					lststep1.get(i).setProtocolordercode(lSlogilabprotocoldetail.getProtocolordercode());
					lststep1.get(i).setOrderstepflag("N");
					
					LSlogilabprotocolstepsRepository.save(lststep1);
					
					if(lSlogilabprotocoldetail.getIsmultitenant() == 1) {						
						CloudLsLogilabprotocolstepInfo CloudLSprotocolstepInfoObj = new CloudLsLogilabprotocolstepInfo();
						CloudLSprotocolstepInfoObj.setId(lststep1.get(i).getProtocolorderstepcode());
						CloudLSprotocolstepInfoObj.setLsprotocolstepInfo(lststep1.get(i).getLsprotocolstepInfo());
						CloudLsLogilabprotocolstepInfoRepository.save(CloudLSprotocolstepInfoObj);
					}
					else {
						Query query = new Query(Criteria.where("id").is(lststep1.get(i).getProtocolorderstepcode()));
						Update update=new Update();
						update.set("content",lststep1.get(i).getLsprotocolstepInfo());
	
						mongoTemplate.upsert(query, update, LsLogilabprotocolstepInfo.class);
					}
					/*if(lSlogilabprotocoldetail.getIsmultitenant() == 1) {
							CloudLSprotocolstepInfo updateLSprotocolstepInfo = CloudLSprotocolstepInfoRepository.findById(lststep1.get(i).getProtocolstepcode());
							updateLSprotocolstepInfo.setLsprotocolstepInfo(lststep1.get(i).getLsprotocolstepInfo());
							CloudLSprotocolstepInfoRepository.save(updateLSprotocolstepInfo);
					}
					else {
							Query query = new Query(Criteria.where("id").is(lststep1.get(i).getProtocolstepcode()));
							Update update=new Update();
							update.set("content",lststep1.get(i).getLsprotocolstepInfo());
		
							mongoTemplate.upsert(query, update, LSprotocolstepInfo.class);
					}*/
				/*i++;
				}*/
				
			for(LSlogilabprotocolsteps LSprotocolstepObj1: lststep1) {
					
					LSprotocolstepObj1.setProtocolordercode(lSlogilabprotocoldetail.getProtocolordercode());
					LSprotocolstepObj1.setOrderstepflag("N");
					
					LSlogilabprotocolstepsRepository.save(LSprotocolstepObj1);
					
					if(lSlogilabprotocoldetail.getIsmultitenant() == 1) {	
						// for getting master step into order step
						CloudLSprotocolstepInfo newLSprotocolstepInfo = CloudLSprotocolstepInfoRepository.findById(LSprotocolstepObj1.getProtocolstepcode());
						if(newLSprotocolstepInfo != null) {
							LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
						}
						
						CloudLsLogilabprotocolstepInfo CloudLSprotocolstepInfoObj = new CloudLsLogilabprotocolstepInfo();
						CloudLSprotocolstepInfoObj.setId(LSprotocolstepObj1.getProtocolorderstepcode());
						CloudLSprotocolstepInfoObj.setLsprotocolstepInfo(LSprotocolstepObj1.getLsprotocolstepInfo());
						CloudLsLogilabprotocolstepInfoRepository.save(CloudLSprotocolstepInfoObj);
					}
					else {
						// for getting master step into order step
						LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
						if(newLSprotocolstepInfo != null) {
							LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
						}
						
						Query query = new Query(Criteria.where("id").is(LSprotocolstepObj1.getProtocolorderstepcode()));
						Update update=new Update();
						update.set("content",LSprotocolstepObj1.getLsprotocolstepInfo());
	
						mongoTemplate.upsert(query, update, LsLogilabprotocolstepInfo.class);
					}
				}
				
				LSlogilabprotocoldetailRepository.save(lSlogilabprotocoldetail);
			}
			
			mapObj.put("AddedProtocol",lSlogilabprotocoldetail);
		}
		List<LSlogilabprotocoldetail> lstOrder = LSlogilabprotocoldetailRepository.findByProtocoltype(lSlogilabprotocoldetail.getProtocoltype());
		Collections.sort(lstOrder, Collections.reverseOrder());
		mapObj.put("ListOfProtocol",lstOrder);
		return mapObj;
	}

	public List<LSlogilabprotocoldetail> getProtocolOrderList(LSlogilabprotocoldetail lSlogilabprotocoldetail) {
//		List<LSlogilabprotocoldetail> lstOrder = LSlogilabprotocoldetailRepository.findByProtocoltype(lSlogilabprotocoldetail.getProtocoltype());
		List<LSlogilabprotocoldetail> lstOrder = LSlogilabprotocoldetailRepository.findByProtocoltypeAndOrderflag(lSlogilabprotocoldetail.getProtocoltype(), lSlogilabprotocoldetail.getOrderflag());
		Collections.sort(lstOrder, Collections.reverseOrder());
		return lstOrder;
	}
	
	public Map<String, Object> updateProtocolOrderStep(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LSlogilabprotocolsteps LSprotocolstepObj= new ObjectMapper().convertValue(argObj.get("ProtocolOrderStepObj"),
				new TypeReference<LSlogilabprotocolsteps>() {
				});
//		List<LSlogilabprotocoldetail> lstOrder = LSlogilabprotocoldetailRepository.findByProtocoltype(lSlogilabprotocoldetail.getProtocoltype());
//		Collections.sort(lstOrder, Collections.reverseOrder());
		
		@SuppressWarnings("unused")
		CloudLSprotocolstepInfo CloudLSprotocolstepInfoObj = new CloudLSprotocolstepInfo();
		if(LSprotocolstepObj.getIsmultitenant() == 1) {
				/*CloudLSprotocolstepInfo updateLSprotocolstepInfo = CloudLSprotocolstepInfoRepository.findById(LSprotocolstepObj.getProtocolstepcode());
				updateLSprotocolstepInfo.setLsprotocolstepInfo(LSprotocolstepObj.getLsprotocolstepInfo());
				CloudLSprotocolstepInfoRepository.save(updateLSprotocolstepInfo);*/
			CloudLsLogilabprotocolstepInfo updateLSprotocolstepInfo = CloudLsLogilabprotocolstepInfoRepository.findById(LSprotocolstepObj.getProtocolorderstepcode());
			updateLSprotocolstepInfo.setLsprotocolstepInfo(LSprotocolstepObj.getLsprotocolstepInfo());
			CloudLsLogilabprotocolstepInfoRepository.save(updateLSprotocolstepInfo);
		}
		else {
				/*Query query = new Query(Criteria.where("id").is(LSprotocolstepObj.getProtocolstepcode()));
				Update update=new Update();
				update.set("content",LSprotocolstepObj.getLsprotocolstepInfo());
				
				mongoTemplate.upsert(query, update, LSprotocolstepInfo.class);*/
			Query query = new Query(Criteria.where("id").is(LSprotocolstepObj.getProtocolorderstepcode()));
			Update update=new Update();
			update.set("content",LSprotocolstepObj.getLsprotocolstepInfo());

			mongoTemplate.upsert(query, update, LsLogilabprotocolstepInfo.class);
		}
		
		List<LSlogilabprotocolsteps> LSprotocolsteplst = 
				LSlogilabprotocolstepsRepository.findByProtocolordercode(LSprotocolstepObj.getProtocolordercode());
		List<LSlogilabprotocolsteps> LSprotocolstepLst = new ArrayList<LSlogilabprotocolsteps>();
		
		for(LSlogilabprotocolsteps LSprotocolstepObj1: LSprotocolsteplst) {
		
			if(LSprotocolstepObj.getIsmultitenant() == 1) {
				//CloudLSprotocolstepInfo newLSprotocolstepInfo = CloudLSprotocolstepInfoRepository.findById(LSprotocolstepObj1.getProtocolstepcode());
				//if(newLSprotocolstepInfo != null) {
				//	LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
				//}
				CloudLsLogilabprotocolstepInfo newLSprotocolstepInfo = CloudLsLogilabprotocolstepInfoRepository.findById(LSprotocolstepObj1.getProtocolorderstepcode());
				if(newLSprotocolstepInfo != null) {
					LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
				}
			}
			else {
				//LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
				//if(newLSprotocolstepInfo != null) {
				//	LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
				//}
				LsLogilabprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(LSprotocolstepObj1.getProtocolorderstepcode(), LsLogilabprotocolstepInfo.class);
				if(newLSprotocolstepInfo != null) {
					LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
				}
			}

			LSprotocolstepLst.add(LSprotocolstepObj1);
		}
		if(LSprotocolsteplst != null) {
			mapObj.put("protocolstepLst", LSprotocolstepLst);
		}else {
			mapObj.put("protocolstepLst", new ArrayList<>());
		}
		
		mapObj.put("protocolstepLst", LSprotocolstepLst);
		return mapObj;
	}

	public Map<String, Object> getProtocolOrderStepLst(Map<String, Object> argObj) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		@SuppressWarnings("unused")
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			LSlogilabprotocoldetail newProtocolOrderObj = new ObjectMapper().
					convertValue(argObj.get("ProtocolOrderObj"),new TypeReference<LSlogilabprotocoldetail>(){});
				
			List<LSlogilabprotocolsteps> LSprotocolsteplst = 
					LSlogilabprotocolstepsRepository.findByProtocolordercode(newProtocolOrderObj.getProtocolordercode());
			List<LSlogilabprotocolsteps> LSprotocolstepLst = new ArrayList<LSlogilabprotocolsteps>();
			
			/*for(LSlogilabprotocolsteps LSprotocolstepObj1: LSprotocolsteplst) {
			
				if(newProtocolOrderObj.getIsmultitenant() == 1) {
					CloudLSprotocolstepInfo newLSprotocolstepInfo = CloudLSprotocolstepInfoRepository.findById(LSprotocolstepObj1.getProtocolstepcode());
					if(newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
					}
				}
				else {
					LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
					if(newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
					}
				}

				LSprotocolstepLst.add(LSprotocolstepObj1);
			}*/
			for(LSlogilabprotocolsteps LSprotocolstepObj1: LSprotocolsteplst) {
				
				if(newProtocolOrderObj.getIsmultitenant() == 1) {
					/*CloudLSprotocolstepInfo newLSprotocolstepInfo = CloudLSprotocolstepInfoRepository.findById(LSprotocolstepObj1.getProtocolstepcode());
					if(newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
					}*/
					CloudLsLogilabprotocolstepInfo newLSprotocolstepInfo = CloudLsLogilabprotocolstepInfoRepository.findById(LSprotocolstepObj1.getProtocolorderstepcode());
					if(newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
					}
				}
				else {
					/*LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
					if(newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
					}*/
					LsLogilabprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(LSprotocolstepObj1.getProtocolorderstepcode(), LsLogilabprotocolstepInfo.class);
					if(newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
					}
				}

				LSprotocolstepLst.add(LSprotocolstepObj1);
			}
			if(LSprotocolsteplst != null) {
				mapObj.put("protocolstepLst", LSprotocolstepLst);
			}else {
				mapObj.put("protocolstepLst", new ArrayList<>());
			}
		}
		return mapObj;
	}

	public Map<String, Object> getAllMasters(LSuserMaster objuser)
	{
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		
		mapOrders.put("test", masterService.getTestmaster(objuser));
		mapOrders.put("sample", masterService.getsamplemaster(objuser));
		mapOrders.put("project", masterService.getProjectmaster(objuser));
//		mapOrders.put("sheets", GetApprovedSheets(0,objuser));
		if(objuser.getObjsilentaudit() != null)
    	{
			objuser.getObjsilentaudit().setTableName("LSfiletest");
    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
    	}
		return mapOrders;
	}
	
	public Map<String, Object> startStep(LSuserMaster objuser)
	{
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		if(objuser.getObjsilentaudit() != null)
    	{
			objuser.getObjsilentaudit().setTableName("lslogilabprotocolsteps");
    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
    	}
		return mapOrders;
	}
	
	public Map<String, Object> updateStepStatus(Map<String, Object> argMap)
	{
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argMap.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argMap.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			LSlogilabprotocolsteps LSlogilabprotocolstepsObj = new ObjectMapper().convertValue(argMap.get("protocolstep"),
					new TypeReference<LSlogilabprotocolsteps>() {
					});
			LSlogilabprotocolstepsRepository.save(LSlogilabprotocolstepsObj);
			
			mapOrders = getProtocolOrderStepLst(argMap);
			LScfttransactionobj.setTableName("lslogilabprotocolsteps");
    		lscfttransactionRepository.save(LScfttransactionobj);
		}
		return mapOrders;
	}
	
	public Map<String, Object> updateOrderStatus(Map<String, Object> argMap)
	{
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argMap.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argMap.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			LSlogilabprotocoldetail newProtocolOrderObj = new ObjectMapper().
					convertValue(argMap.get("ProtocolOrderObj"),new TypeReference<LSlogilabprotocoldetail>(){});
			LSlogilabprotocoldetailRepository.save(newProtocolOrderObj);
			LScfttransactionobj.setTableName("LSlogilabprotocoldetail");
    		lscfttransactionRepository.save(LScfttransactionobj);
		}
		return mapOrders;
	}
	
	public Map<String, Object> getLsrepositoriesLst(Map<String, Object> argMap)
	{
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argMap.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argMap.get("objsilentaudit"), new TypeReference<LScfttransaction>() {});
			List<Lsrepositories> LsrepositoriesLst=LsrepositoriesRepository.findBySitecodeOrderByRepositorycodeAsc(LScfttransactionobj.getLssitemaster());
			if(LsrepositoriesLst.size() > 0) {
				mapObj.put("LsrepositoriesLst", LsrepositoriesLst);
//				List<Lsrepositoriesdata> LsrepositoriesdataLst=LsrepositoriesdataRepository.findByRepositorycodeAndSitecodeOrderByRepositorydatacodeDesc(LsrepositoriesLst.get(0).getRepositorycode(), LScfttransactionobj.getLssitemaster());
				Map<String, Object> temp= new HashMap<>();
				temp.put("objsilentaudit", LScfttransactionobj);
				temp.put("LsrepositoriesObj", LsrepositoriesLst.get(0));
				mapObj.putAll( getLsrepositoriesDataLst(temp));
			}else {
				mapObj.put("LsrepositoriesLst", LsrepositoriesLst);
				mapObj.put("LsrepositoriesdataLst", new ArrayList<>());
			}
			
		}
		return mapObj;
	}
	
	public Map<String, Object> getLsrepositoriesDataLst(Map<String, Object> argMap)
	{
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argMap.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argMap.get("objsilentaudit"), new TypeReference<LScfttransaction>() {});
			Lsrepositories LsrepositoriesLst= new ObjectMapper().convertValue(argMap.get("LsrepositoriesObj"), new TypeReference<Lsrepositories>() {});
			if(LsrepositoriesLst.getRepositorycode() > 0) {
				List<Lsrepositoriesdata> LsrepositoriesdataLst=LsrepositoriesdataRepository.findByRepositorycodeAndSitecodeAndItemstatusOrderByRepositorydatacodeDesc(LsrepositoriesLst.getRepositorycode(), LScfttransactionobj.getLssitemaster(), 1);
				mapObj.put("LsrepositoriesdataLst", LsrepositoriesdataLst);
			}else {
				mapObj.put("LsrepositoriesdataLst", new ArrayList<>());
			}
			
		}
		return mapObj;
	}
	
	/*public Map<String, Object> addProtocolOrderStep(Map<String, Object> argObj) {
		
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
		}
		ObjectMapper objMapper= new ObjectMapper();
		LoggedUser objUser = new LoggedUser();
		//		silent audit
		if(LScfttransactionobj!=null ) {
			LScfttransactionobj.setTableName("LSprotocolmaster");
			if(argObj.containsKey("username")) {
				String username= objMapper.convertValue(argObj.get("username"), String.class);
				LSSiteMaster objsite = LSSiteMasterRepository.findBysitecode(LScfttransactionobj.getLssitemaster());
				LSuserMaster objuser= LSuserMasterRepositoryObj.findByusernameAndLssitemaster(username, objsite);
				LScfttransactionobj.setLsuserMaster(objuser.getUsercode());
				LScfttransactionobj.setLssitemaster(objuser.getLssitemaster().getSitecode());
				LScfttransactionobj.setUsername(username);
			}
			lscfttransactionRepository.save(LScfttransactionobj);
		}
		//		manual audit
		if(argObj.containsKey("objuser")) {
			objUser=objMapper.convertValue(argObj.get("objuser"), LoggedUser.class);
			if(argObj.containsKey("objmanualaudit")) {
				LScfttransaction objmanualaudit=new LScfttransaction();
				objmanualaudit = objMapper.convertValue(argObj.get("objmanualaudit"), LScfttransaction.class);
				
				objmanualaudit.setComments(objUser.getComments());
				lscfttransactionRepository.save(objmanualaudit);
			}
		}
		
			if(argObj.containsKey("newProtocolstepObj")) {
				LSlogilabprotocolsteps LSprotocolstepObj = new ObjectMapper().convertValue(argObj.get("newProtocolstepObj"), 
						new TypeReference<LSlogilabprotocolsteps>() {});
				
				CloudLSlogilabprotocolstepsInfo CloudLSprotocolstepInfoObj = new CloudLSlogilabprotocolstepsInfo();
					if(LSprotocolstepObj.getIsmultitenant() == 1) {
//					if(LSprotocolstepObj.getNewStep() == 1) {	
						CloudLSprotocolstepInfoObj.setId(LSprotocolstepObj.getProtocolorderstepcode());
						CloudLSprotocolstepInfoObj.setLsprotocolstepInfo(LSprotocolstepObj.getLsprotocolstepInfo());
						CloudLSlogilabprotocolstepsInfoRepository.save(CloudLSprotocolstepInfoObj);	
//					}else {
//						CloudLSlogilabprotocolstepsInfo updateLSprotocolstepInfo = CloudLSlogilabprotocolstepsInfoRepository.
//								findById(LSprotocolstepObj.getProtocolorderstepcode());
//						updateLSprotocolstepInfo.setLsprotocolstepInfo(LSprotocolstepObj.getLsprotocolstepInfo());
//						CloudLSlogilabprotocolstepsInfoRepository.save(updateLSprotocolstepInfo);
//					}
				}
				else {
					Query query = new Query(Criteria.where("id").is(LSprotocolstepObj.getProtocolorderstepcode()));
						Update update=new Update();
						update.set("content",LSprotocolstepObj.getLsprotocolstepInfo());					
						mongoTemplate.upsert(query, update, LSlogilabprotocolstepsInfo.class);
				}

				List<LSlogilabprotocolsteps> tempLSprotocolstepLst = LSlogilabprotocolstepsRepository.findByprotocolorderstepcode(LSprotocolstepObj.getProtocolorderstepcode());
				List<LSlogilabprotocolsteps> LSprotocolstepLst = new ArrayList<LSlogilabprotocolsteps>();
				for(LSlogilabprotocolsteps LSprotocolstepObj1: tempLSprotocolstepLst) {		
					if(LSprotocolstepObj.getIsmultitenant() == 1) {
//						if(LSprotocolstepObj.getNewStep() == 1) {
							LSprotocolstepObj1.setLsprotocolstepInfo(CloudLSprotocolstepInfoObj.getLsprotocolstepInfo());
//						}else {
//							CloudLSlogilabprotocolstepsInfo newLSprotocolstepInfo = CloudLSlogilabprotocolstepsInfoRepository.findById(LSprotocolstepObj.getProtocolstepcode());
//							if(newLSprotocolstepInfo != null) {
//								LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
//							}	
//						}
					}
					else {
						LSlogilabprotocolstepsInfo newLSprotocolstepInfo = mongoTemplate.findById(LSprotocolstepObj1.getProtocolorderstepcode(), LSlogilabprotocolstepsInfo.class);
						if(newLSprotocolstepInfo != null) {
							LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
						}
					}
					LSprotocolstepLst.add(LSprotocolstepObj1);
				}
				mapObj.put("protocolstepLst", LSprotocolstepLst);
			}
			
		return mapObj;
	}*/

}