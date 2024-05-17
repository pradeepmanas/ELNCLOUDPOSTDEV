package com.agaram.eln.primary.service.usermanagement;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.agaram.eln.config.AESEncryption;
import com.agaram.eln.config.JwtTokenUtil;
import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.fetchmodel.getmasters.Usermaster;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.protocols.ElnprotocolTemplateworkflow;
import com.agaram.eln.primary.model.protocols.ElnprotocolTemplateworkflowgroupmap;
import com.agaram.eln.primary.model.protocols.Elnprotocolworkflow;
import com.agaram.eln.primary.model.protocols.Elnprotocolworkflowgroupmap;
import com.agaram.eln.primary.model.protocols.LSprotocolmastertest;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSfilemethod;
import com.agaram.eln.primary.model.sheetManipulation.LSfileparameter;
import com.agaram.eln.primary.model.sheetManipulation.LSfiletest;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflowgroupmap;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflowgroupmapping;
import com.agaram.eln.primary.model.usermanagement.LSMultisites;
import com.agaram.eln.primary.model.usermanagement.LSMultiusergroup;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.model.usermanagement.Lsusersettings;
import com.agaram.eln.primary.repository.protocol.ElnprotocolTemplateworkflowRepository;
import com.agaram.eln.primary.repository.protocol.ElnprotocolTemplateworkflowgroupmapRepository;
import com.agaram.eln.primary.repository.protocol.ElnprotocolworkflowRepository;
import com.agaram.eln.primary.repository.protocol.ElnprotocolworkflowgroupmapRepository;
import com.agaram.eln.primary.repository.protocol.LSProtocolMasterRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolmastertestRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfileRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfiletestRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsheetworkflowRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsheetworkflowgroupmapRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LStestmasterlocalRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowgroupmappingRepository;
import com.agaram.eln.primary.repository.usermanagement.LSMultisitesRepositery;
import com.agaram.eln.primary.repository.usermanagement.LSMultiusergroupRepositery;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSprojectmasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusersteamRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;
import com.agaram.eln.primary.repository.usermanagement.LsusersettingsRepository;
import com.agaram.eln.primary.service.JWTservice.JwtUserDetailsService;
import com.agaram.eln.primary.service.protocol.Commonservice;
import com.agaram.eln.primary.service.protocol.ProtocolService;
import com.agaram.eln.primary.service.sheetManipulation.FileService;
import com.aspose.pdf.internal.imaging.internal.Exceptions.IO.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Service
public class FreeUserService {
	
	@Autowired
	private LSuserMasterRepository lsuserMasterRepository;
	
	@Autowired
	private LSSiteMasterRepository lSSiteMasterRepository;
	
	@Autowired
	private LsusersettingsRepository LsusersettingsRepository;
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private LSMultisitesRepositery lsmultisitesrepositery;
	
	@Autowired
	private LSMultiusergroupRepositery lsmultiusergroupRepositery;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@Autowired
	private LStestmasterlocalRepository lStestmasterlocalRepository;
	
	@Autowired
	private LSusersteamRepository lsusersteamrepository;
	
	@Autowired
	private LSuserteammappingRepository lsuserteammappingrepository;
	
	@Autowired
	private LSprojectmasterRepository lsprojectmasterrepository;
	
	@Autowired
	private LSworkflowRepository lsworkflowrepository;
	
	@Autowired
	private LSworkflowgroupmappingRepository lsworkflowgroupmappingrepository;
	
	@Autowired
	private LSsheetworkflowRepository lssheetworkflowrepository;
	
	@Autowired
	private LSsheetworkflowgroupmapRepository lssheetworkflowgroupmaprepository;
	
	@Autowired
	private ElnprotocolworkflowRepository elnprotocolworkflowRepository;

	@Autowired
	private ElnprotocolworkflowgroupmapRepository elnprotocolworkflowgroupmapRepository;
	
	@Autowired
	private ElnprotocolTemplateworkflowRepository elnprotocoltemplateworkflowrepository;
	
	@Autowired
	private ElnprotocolTemplateworkflowgroupmapRepository elnprotocoltemplateworkflowgroupmaprepository;
	
	@Autowired
	Commonservice commonservice;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	ProtocolService ProtocolMasterService;
	
	@Autowired
	private LSfileRepository lSfileRepository;
	
	@Autowired
	LSProtocolMasterRepository LSProtocolMasterRepositoryObj;
	
	@Autowired
	LSfiletestRepository LSfiletestRepository;
	
	@Autowired
	LSprotocolmastertestRepository LSprotocolmastertestRepository;
	
	@SuppressWarnings("unchecked")
	public LSuserMaster Createuser( LSuserMaster objuser) throws Exception {
		Long usercount = lsuserMasterRepository.countByUsernameIgnoreCaseAndAutenticatefromAndSubcode(objuser.getUsername(),objuser.getAutenticatefrom(),objuser.getSubcode());
		if(usercount <=0)
		{
			if(objuser.getLssitemaster() != null) 
				{
					Calendar current = Calendar.getInstance();
				    current.add(Calendar.DATE, 100);
				    Date resultdate = new Date(current.getTimeInMillis());
					objuser.getLssitemaster().setExpirydate(resultdate);
				}
			LSSiteMaster site = lSSiteMasterRepository.save(objuser.getLssitemaster());
			
			
			
			String unifieduser = objuser.getUsername().toLowerCase().replaceAll("[^a-zA-Z0-9]", "") + "u"
					+ objuser.getUsercode() + "s" + site.getSitecode() + objuser.getUnifieduserid();

			objuser.setUnifieduserid(unifieduser);
			try {
				objuser.setCreateddate(commonfunction.getCurrentUtcTime());
				objuser.setModifieddate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(objuser.getAutenticatefrom()!=0)
			{
				objuser.setPassword(objuser.getSubcode());
			}
			
			lsuserMasterRepository.save(objuser);
			
			LSMultisites lmultisites = new LSMultisites();
			lmultisites.setDefaultsiteMaster(objuser.getLssitemaster().getSitecode());
			lmultisites.setLssiteMaster(objuser.getLssitemaster());
			lmultisites.setUsercode(objuser.getUsercode());
			
			lsmultisitesrepositery.save(lmultisites);
			
			LSMultiusergroup lsmultiusergroup = new LSMultiusergroup();
			lsmultiusergroup.setDefaultusergroup(1);
			lsmultiusergroup.setUsercode(objuser.getUsercode());
			LSusergroup objusergroup =  new LSusergroup();
			objusergroup.setUsergroupcode(1);
			lsmultiusergroup.setLsusergroup(objusergroup);
			
			lsmultiusergroupRepositery.save(lsmultiusergroup);
			
			Response objResponse = new Response();
			objResponse.setStatus(true);
			objResponse.setInformation("User registered successfully");
			objuser.setObjResponse(objResponse);
			
			Lsusersettings getUserPreference = new Lsusersettings();
			getUserPreference.setDFormat("Mon-DD-YYYY HH:mm:ss");
			getUserPreference.setUsercode(objuser.getUsercode());
			LsusersettingsRepository.save(getUserPreference);
			
			LStestmasterlocal test1 = new LStestmasterlocal();
			test1.setCreateby(objuser);
			test1.setCreatedate(commonfunction.getCurrentUtcTime());
			test1.setStatus(1);
			test1.setTestname("task 1");
			test1.setTeststatus("A");
			test1.setLssitemaster(site);
			test1.setModifiedby(objuser);
			lStestmasterlocalRepository.save(test1);
			
			LStestmasterlocal test2 = new LStestmasterlocal();
			test2.setCreateby(objuser);
			test2.setCreatedate(commonfunction.getCurrentUtcTime());
			test2.setStatus(1);
			test2.setTestname("task 2");
			test2.setTeststatus("A");
			test2.setLssitemaster(site);
			test2.setModifiedby(objuser);
			lStestmasterlocalRepository.save(test2);
			
			LStestmasterlocal test3 = new LStestmasterlocal();
			test3.setCreateby(objuser);
			test3.setCreatedate(commonfunction.getCurrentUtcTime());
			test3.setStatus(1);
			test3.setTestname("task 3");
			test3.setTeststatus("A");
			test3.setLssitemaster(site);
			test3.setModifiedby(objuser);
			lStestmasterlocalRepository.save(test3);
			
			LSusersteam team = new LSusersteam();
			team.setCreateby(objuser);
			team.setModifiedby(objuser);
			team.setTeamname("team");
			team.setLssitemaster(site);
			team.setCreatedate(commonfunction.getCurrentUtcTime());
			team.setModifieddate(commonfunction.getCurrentUtcTime());
			team.setProjectteamstatus("A");
			team.setStatus(1);
			team.setLssitemaster(site);
			
			lsusersteamrepository.save(team);
			
			LSuserteammapping teammap = new LSuserteammapping();
			teammap.setTeamcode(team.getTeamcode());
			teammap.setLsuserMaster(objuser);
			
			lsuserteammappingrepository.save(teammap);
			
			LSprojectmaster project = new LSprojectmaster();
			project.setCreatedby(objuser.getUsername());
			project.setCreatedon(commonfunction.getCurrentUtcTime());
			project.setLssitemaster(site);
			project.setLsusermaster(objuser);
			project.setLsusersteam(team);
			project.setModifiedby(objuser);
			project.setProjectname("project");
			project.setProjectstatus("A");
			project.setStatus(1);
			
			lsprojectmasterrepository.save(project);
			
			LSworkflow workflowsheet = new LSworkflow();
			if(!objuser.getLssitemaster().getAccouttype().equals(1)) {
			workflowsheet.setLssitemaster(site);
			workflowsheet.setWorkflowname("Review");
			
			lsworkflowrepository.save(workflowsheet);
			
			LSworkflowgroupmapping workflowsheetmap = new LSworkflowgroupmapping();
			workflowsheetmap.setLsusergroup(objusergroup);
			workflowsheetmap.setWorkflowcode(workflowsheet.getWorkflowcode());
			
			lsworkflowgroupmappingrepository.save(workflowsheetmap);
			}
			
			LSworkflow workflowsheet2 = new LSworkflow();
			workflowsheet2.setLssitemaster(site);
			workflowsheet2.setWorkflowname("Approve");
			
			lsworkflowrepository.save(workflowsheet2);
			
			LSworkflowgroupmapping workflowsheetmap2 = new LSworkflowgroupmapping();
			workflowsheetmap2.setLsusergroup(objusergroup);
			workflowsheetmap2.setWorkflowcode(workflowsheet2.getWorkflowcode());
			
			lsworkflowgroupmappingrepository.save(workflowsheetmap2);
			
			LSsheetworkflow workflowsheettemp = new LSsheetworkflow();
			if(!objuser.getLssitemaster().getAccouttype().equals(1)) {
			workflowsheettemp.setLssitemaster(site);
			workflowsheettemp.setStatus(1);
			workflowsheettemp.setWorkflowname("Review");
			
			lssheetworkflowrepository.save(workflowsheettemp);
			
			LSsheetworkflowgroupmap workflowsheettempmap = new LSsheetworkflowgroupmap();
			workflowsheettempmap.setLsusergroup(objusergroup);
			workflowsheettempmap.setWorkflowcode(workflowsheet.getWorkflowcode());
			
			lssheetworkflowgroupmaprepository.save(workflowsheettempmap);
			}
			
			LSsheetworkflow workflowsheettemp2 = new LSsheetworkflow();
			workflowsheettemp2.setLssitemaster(site);
			workflowsheettemp2.setStatus(1);
			workflowsheettemp2.setWorkflowname("Approve");
			
			lssheetworkflowrepository.save(workflowsheettemp2);
			
			LSsheetworkflowgroupmap workflowsheettempmap2 = new LSsheetworkflowgroupmap();
			workflowsheettempmap2.setLsusergroup(objusergroup);
			workflowsheettempmap2.setWorkflowcode(workflowsheettemp2.getWorkflowcode());
			
			lssheetworkflowgroupmaprepository.save(workflowsheettempmap2);
			
			Elnprotocolworkflow workflowprotocol = new Elnprotocolworkflow();
			if(!objuser.getLssitemaster().getAccouttype().equals(1)) {
			workflowprotocol.setLssitemaster(site);
			workflowprotocol.setWorkflowname("Review");
			
			elnprotocolworkflowRepository.save(workflowprotocol);
			
			Elnprotocolworkflowgroupmap workflowprotocolmap = new Elnprotocolworkflowgroupmap();
			workflowprotocolmap.setLsusergroup(objusergroup);
			workflowprotocolmap.setWorkflowcode(workflowprotocol.getWorkflowcode());
			
			elnprotocolworkflowgroupmapRepository.save(workflowprotocolmap);
			}
			
			Elnprotocolworkflow workflowprotocol2 = new Elnprotocolworkflow();
			workflowprotocol2.setLssitemaster(site);
			workflowprotocol2.setWorkflowname("Approve");
			
			elnprotocolworkflowRepository.save(workflowprotocol2);
			
			Elnprotocolworkflowgroupmap workflowprotocolmap2 = new Elnprotocolworkflowgroupmap();
			workflowprotocolmap2.setLsusergroup(objusergroup);
			workflowprotocolmap2.setWorkflowcode(workflowprotocol2.getWorkflowcode());
			
			elnprotocolworkflowgroupmapRepository.save(workflowprotocolmap2);
			
			ElnprotocolTemplateworkflow workflowprotocoltemp = new ElnprotocolTemplateworkflow();
			if(!objuser.getLssitemaster().getAccouttype().equals(1)) {
			workflowprotocoltemp.setLssitemaster(site);
			workflowprotocoltemp.setStatus(1);
			workflowprotocoltemp.setWorkflowname("Review");
		
			
			elnprotocoltemplateworkflowrepository.save(workflowprotocoltemp);
			
			ElnprotocolTemplateworkflowgroupmap workflowprotocoltempmap = new ElnprotocolTemplateworkflowgroupmap();
			workflowprotocoltempmap.setLsusergroup(objusergroup);
			workflowprotocoltempmap.setWorkflowcode(workflowprotocoltemp.getWorkflowcode());
			
			elnprotocoltemplateworkflowgroupmaprepository.save(workflowprotocoltempmap);
			}
			
			ElnprotocolTemplateworkflow workflowprotocoltemp2 = new ElnprotocolTemplateworkflow();
			workflowprotocoltemp2.setLssitemaster(site);
			workflowprotocoltemp2.setStatus(1);
			workflowprotocoltemp2.setWorkflowname("Approve");
			
			elnprotocoltemplateworkflowrepository.save(workflowprotocoltemp2);
			
			ElnprotocolTemplateworkflowgroupmap workflowprotocoltempmap2 = new ElnprotocolTemplateworkflowgroupmap();
			workflowprotocoltempmap2.setLsusergroup(objusergroup);
			workflowprotocoltempmap2.setWorkflowcode(workflowprotocoltemp2.getWorkflowcode());
			
			elnprotocoltemplateworkflowgroupmaprepository.save(workflowprotocoltempmap2);
			
			
			if(objuser.getAutenticatefrom()==0)
			{
				userService.Usersendpasswormail(objuser);
			}
			//for template create
			ObjectMapper objectMapper = new ObjectMapper();
			ClassPathResource resource = new ClassPathResource("import_elnlite.json");
			try {
			    if (resource.exists()) {
			        System.out.println("--------------File Exists ----------------");
			        
			     // Read JSON file into a Map
					Map<String, Object> jsonData = objectMapper.readValue(resource.getInputStream(), Map.class);
					Gson gson = new Gson();
					// sheet template
					List<Map<String, Object>> templates = (List<Map<String, Object>>) jsonData.get("templates");
					templates.stream().forEach(template -> {
						String Content = gson.toJson(template.get("filecontent"));
						List<LSfiletest> lstest = (List<LSfiletest>) template.get("lstest");
						List<LSfilemethod> lstfilemethd = (List<LSfilemethod>) template.get("lsmethods");
						List<LSfileparameter> lstfileparam = (List<LSfileparameter>) template.get("lsparameter");
						LSfile objfile = new LSfile();
						objfile.setFilecontent(Content);
						objfile.setFilenameuser(template.get("filenameuser").toString());
						objfile.setIsmultitenant(2);
						objfile.setLssitemaster(objuser.getLssitemaster());
//				        objfile.setLssheetworkflow(workflowsheettemp2);
//				        objfile.setApproved(1);
						objfile.setCreateby(objuser);
						objfile.setLstest(lstest);
						objfile.setLsmethods(lstfilemethd);
						objfile.setLsparameter(lstfileparam);
						objfile.setViewoption(1);
						objfile.setRejected(0);
						
						try {
							fileService.InsertupdateSheet(objfile);
						} catch (java.io.IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						// for approve
						objfile.setApproved(1);
						lSfileRepository.save(objfile);
						// for default task mapping 
						LSfiletest objfiletest=  new LSfiletest();						
						objfiletest.setFilecode(objfile.getFilecode());
						objfiletest.setTestcode(test1.getTestcode());
						objfiletest.setTesttype(1);
						LSfiletestRepository.save(objfiletest);
					});
					
					// protocol template
					List<Map<String, Object>> protocols = (List<Map<String, Object>>) jsonData.get("protocols");
					Map<String, Object> objsilentaudit = new HashMap<>();
					Map<String, Object> newProtocolMasterObj = new HashMap<>();
					List<Integer> lstteamusercode = new ArrayList<>();
					lstteamusercode.add(team.getTeamcode());
					protocols.stream().forEach(protocol -> {
						objsilentaudit.put("lssitemaster", objuser.getLssitemaster().getSitecode());
						objsilentaudit.put("lsuserMaster", objuser.getUsercode());
						newProtocolMasterObj.put("createdby", objuser);
						newProtocolMasterObj.put("protocolmastername", protocol.get("protocolmastername"));
						newProtocolMasterObj.put("protocolstatus", 1);
						newProtocolMasterObj.put("status", 1);
						newProtocolMasterObj.put("ismultitenant", 2);
						protocol.put("objsilentaudit", objsilentaudit);
						protocol.put("createdby", objuser.getUsercode());
						protocol.put("usercode", objuser.getUsercode());
						protocol.put("username", objuser.getUsername());
						protocol.put("newProtocolMasterObj", newProtocolMasterObj);
						protocol.put("lsuserMaster", objuser);
						protocol.put("teamuserscode", lstteamusercode);
						try {
							ProtocolMasterService.addProtocolMaster(protocol);
						} catch (java.io.IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						int protocolmastercode = new ObjectMapper().convertValue(protocol.get("protocolmastercode"),Integer.class);
						// for default task mapping 
						LSprotocolmastertest objprotocoltest = new LSprotocolmastertest();
						objprotocoltest.setProtocolmastercode(protocolmastercode);
						objprotocoltest.setTestcode(test1.getTestcode());
						objprotocoltest.setTesttype(1);
						LSprotocolmastertestRepository.save(objprotocoltest);
						
					});
					
			    } else {
			        System.out.println("--------------File Does Not Exist ----------------");
			    }
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}
		else
		{
			Response objResponse = new Response();
			objResponse.setStatus(false);
			objResponse.setInformation("User exists");
			objuser.setObjResponse(objResponse);
		}
		return objuser;
	}
	
	public Map<String, Object> Loginfreeuser(LSuserMaster objuser) throws Exception {
		Map<String, Object> loginobject = new HashMap<>();
		Long usercount = lsuserMasterRepository.countByUsernameIgnoreCaseAndAutenticatefromAndSubcode(objuser.getUsername(),objuser.getAutenticatefrom(),objuser.getSubcode());
		if(usercount >0)
		{	
			objuser = lsuserMasterRepository.findByUsernameIgnoreCaseAndAutenticatefromAndSubcode(objuser.getUsername(),objuser.getAutenticatefrom(),objuser.getSubcode());
		
			Response objResponse = new Response();
			objResponse.setStatus(true);
			objResponse.setInformation("User exists");
			objuser.setObjResponse(objResponse);
			
			LSMultisites lmultisites = lsmultisitesrepositery.findTop1Byusercode(objuser.getUsercode());
			if(lmultisites != null && lmultisites.getLssiteMaster() != null && lmultisites.getLssiteMaster().getExpirydate() != null)
			{
				Calendar current = Calendar.getInstance();
				if(lmultisites.getLssiteMaster().getExpirydate().before(new Date(current.getTimeInMillis()))){
					objResponse.setStatus(false);
					objResponse.setInformation("User expired");
				}
			}
		}
		else
		{
			Response objResponse = new Response();
			objResponse.setStatus(false);
			objResponse.setInformation("User not exists");
			objuser.setObjResponse(objResponse);
		}
		loginobject.put("user", objuser);
		loginobject.put("Logintime", commonfunction.getCurrentUtcTime());
		return loginobject;
	}
	
	public Usermaster Validateuser(LSuserMaster objuser) throws Exception {
		 Usermaster validuser = objuser.getUsername().equalsIgnoreCase("Administrator") ? 
				 lsuserMasterRepository.findTop1ByUsernameIgnoreCase(objuser.getUsername())
				: lsuserMasterRepository.findTop1ByUsernameIgnoreCaseAndAutenticatefrom(objuser.getUsername(),objuser.getAutenticatefrom());
		 return validuser!= null ?validuser:new Usermaster(null,null,null,null,null);
	}
	
	public Response Setpassword(LoggedUser objuser) throws Exception {
		Integer usercode = objuser.getLsusermaster().getUsercode();
		String Password = AESEncryption.encrypt(objuser.getLsusermaster().getPassword());
		
		lsuserMasterRepository.setpasswordandpasswordstatusByusercode(Password,
				0, usercode);
		
		Response objresponse = new Response();
		objresponse.setStatus(true);
		objresponse.setInformation("Password reset successfully");
		
		return objresponse;
	}
	
	public Response Resetpassword(LoggedUser objuser) throws Exception {
		Integer usercode = objuser.getLsusermaster().getUsercode();
		String Password = AESEncryption.encrypt(objuser.getLsusermaster().getPassword());
		String Oldpassword = objuser.getsPassword();
		
		LSuserMaster objcurrentuser = lsuserMasterRepository.findByusercode(usercode);
		Response objresponse = new Response();
		
		if(objcurrentuser != null && objcurrentuser.getPassword() != null &&
				Oldpassword.equals(AESEncryption.decrypt(objcurrentuser.getPassword())))
		{
			lsuserMasterRepository.setpasswordandpasswordstatusByusercode(Password,
					0, usercode);
			objresponse.setStatus(true);
			objresponse.setInformation("Password reset successfully");
		}
		else
		{
			objresponse.setStatus(false);
			objresponse.setInformation("Old password is incorrect");
		}
		
		return objresponse;
	}
	
	public Map<String, Object> Loginfreeuserwithname(LSuserMaster objuser) throws Exception {
		Map<String, Object> loginobject = new HashMap<>();
		LSuserMaster objcurrentuser = lsuserMasterRepository.findByusercode(objuser.getUsercode());
		Response objresponse = new Response();
		if(objcurrentuser.getPassword().equals(AESEncryption.encrypt(objuser.getPassword()))) {
			
			final UserDetails userDetails = userDetailsService
					.loadUserByUsername(objcurrentuser.getUsername()+ "[" + objcurrentuser.getLssitemaster().getSitecode() + "]");

			final String token = jwtTokenUtil.generateToken(userDetails);
			loginobject.put("token", token);
			objresponse.setStatus(true);
			objresponse.setInformation("Logged in successfully");
			
			LSMultisites lmultisites = lsmultisitesrepositery.findTop1Byusercode(objuser.getUsercode());
			if(lmultisites != null && lmultisites.getLssiteMaster() != null && lmultisites.getLssiteMaster().getExpirydate() != null)
			{
				Calendar current = Calendar.getInstance();
				if(lmultisites.getLssiteMaster().getExpirydate().before(new Date(current.getTimeInMillis()))){
					objresponse.setStatus(false);
					objresponse.setInformation("User expired");
				}
			}
		}
		else
		{
			objresponse.setStatus(false);
			objresponse.setInformation("Invalid password");
		}
		loginobject.put("user", objcurrentuser);
		loginobject.put("objResponse", objresponse);
		return loginobject;
	}
	
	public Boolean GetUsersaddrestrict(LSSiteMaster objsite) {
		Boolean allowuseradd = false;
		LSSiteMaster updatedsite = lSSiteMasterRepository.findBysitecode(objsite.getSitecode());
		if(updatedsite != null && updatedsite.getTeamsize()!= null)
		{
			Long userCount = lsuserMasterRepository.getactiveusersonsite(objsite.getSitecode());

			if (userCount < updatedsite.getTeamsize()) {

				allowuseradd = true;

			} else {
				allowuseradd = false;
			}
		}
		return allowuseradd;
	}
}
