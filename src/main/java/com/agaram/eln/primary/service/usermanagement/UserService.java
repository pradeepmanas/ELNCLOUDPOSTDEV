package com.agaram.eln.primary.service.usermanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.config.AESEncryption;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.notification.Email;
import com.agaram.eln.primary.model.usermanagement.LSPasswordPolicy;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSactiveUser;
import com.agaram.eln.primary.model.usermanagement.LScentralisedUsers;
import com.agaram.eln.primary.model.usermanagement.LSnotification;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserActions;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.model.usermanagement.LSusergrouprights;
import com.agaram.eln.primary.model.usermanagement.LSusergrouprightsmaster;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
import com.agaram.eln.primary.repository.usermanagement.LSPasswordPolicyRepository;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSactiveUserRepository;
import com.agaram.eln.primary.repository.usermanagement.LScentralisedUsersRepository;
import com.agaram.eln.primary.repository.usermanagement.LSnotificationRepository;
import com.agaram.eln.primary.repository.usermanagement.LSprojectmasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserActionsRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusergroupRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusergrouprightsRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusergrouprightsmasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusersteamRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;
import com.agaram.eln.primary.service.notification.EmailService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
@EnableJpaRepositories(basePackageClasses = LSusergroupRepository.class)
public class UserService {

	@Autowired
    private LSusergroupRepository lSusergroupRepository;
	
	@Autowired
    private LSuserMasterRepository lsuserMasterRepository;
	
	@Autowired
    private LSusersteamRepository lsusersteamRepository;
	
	@Autowired
    private LSuserteammappingRepository lsuserteammappingRepository;
	
	@Autowired
    private LSusergrouprightsRepository lsusergrouprightsRepository;
	
	@Autowired
    private LSusergrouprightsmasterRepository lsusergrouprightsmasterRepository;
	
	@Autowired
	private LScfttransactionRepository lscfttransactionRepository;
	
	@Autowired
	private LSactiveUserRepository lsactiveUserRepository;
	
	@Autowired
	private LSPasswordPolicyRepository lSpasswordpolicyRepository;
	
	@Autowired
	private LSnotificationRepository lsnotificationRepository;
	
	@Autowired
	private LSuserActionsRepository lsuserActionsRepository;
	
	@SuppressWarnings("unused")
	@Autowired
	private LSSiteMasterRepository LSSiteMasterRepository;
	
	@Autowired
	private LSlogilablimsorderdetailRepository LSlogilablimsorderdetailRepository;
	
	@Autowired
	private LSprojectmasterRepository LSprojectmasterRepository;
	
	@Autowired
	private LScentralisedUsersRepository lscentralisedUsersRepository;
	
	@Autowired
    private EmailService emailService;
	
	public LSusergroup InsertUpdateUserGroup(LSusergroup objusergroup)
	{
		if(lSusergroupRepository.findByusergroupnameIgnoreCaseAndLssitemaster(objusergroup.getUsergroupname(),objusergroup.getLssitemaster())!= null  && objusergroup.getUsergroupcode() == null)
		{
			objusergroup.setResponse(new Response());
			objusergroup.getResponse().setStatus(false);
			objusergroup.getResponse().setInformation("ID_EXIST");
			if(objusergroup.getObjsilentaudit() != null)
	    	{   
				objusergroup.getObjsilentaudit().setActions("Warning");
				objusergroup.getObjsilentaudit().setComments(objusergroup.getCreateby().getUsername()+" "+"made attempt to create existing group name");
				objusergroup.getObjsilentaudit().setTableName("LSusergroup");
	    		lscfttransactionRepository.save(objusergroup.getObjsilentaudit());
	    	}
//			manual audit
			if(objusergroup.getObjuser() != null)
	    	{
				objusergroup.getObjmanualaudit().setActions("Warning");
				objusergroup.getObjmanualaudit().setTableName("LScfttransaction");
				objusergroup.getObjmanualaudit().setComments(objusergroup.getObjuser().getComments());
	    		lscfttransactionRepository.save(objusergroup.getObjmanualaudit());
	    	}
			return objusergroup;
		}
		
		lSusergroupRepository.save(objusergroup);
		objusergroup.setResponse(new Response());
		objusergroup.getResponse().setStatus(true);
		objusergroup.getResponse().setInformation("ID_SUCCESSMSG");
		
		if(objusergroup.getObjsilentaudit() != null)
    	{
			//objusergroup.getObjsilentaudit().setModuleName("UserManagement");
			//objusergroup.getObjsilentaudit().setComments("Insert/Update UserGroup Successfully");
			//objusergroup.getObjsilentaudit().setActions("Insert/Update UserGroup");
			//objusergroup.getObjsilentaudit().setSystemcoments("System Generated");
			objusergroup.getObjsilentaudit().setTableName("LSusergroup");
    		lscfttransactionRepository.save(objusergroup.getObjsilentaudit());
    	}
//		Manual Audit
	
		if(objusergroup.getObjuser() != null) {
			//LScfttransaction manualAudit=new LScfttransaction();
			Date date = new Date();
			if(objusergroup.getObjmanualaudit() != null)
	    	{
			//objusergroup.getObjmanualaudit().setComments("Insert Test Successfully");
			objusergroup.getObjmanualaudit().setComments(objusergroup.getObjuser().getComments());
			//manualAudit.setActions("Insert Test");
			//manualAudit.setSystemcoments("User Generated");
			objusergroup.getObjmanualaudit().setTableName("LStestmasterlocal");
			//manualAudit.setManipulatetype("Insert");
			objusergroup.getObjmanualaudit().setLsuserMaster(objusergroup.getLSuserMaster().getUsercode());
			objusergroup.getObjmanualaudit().setLssitemaster(objusergroup.getLSuserMaster().getLssitemaster().getSitecode());
			objusergroup.getObjmanualaudit().setTransactiondate(date);
    		lscfttransactionRepository.save(objusergroup.getObjmanualaudit());
		}
		}
		return objusergroup;
	}
	
	public LSusergroup InsertUpdateUserGroupFromSDMS(LSusergroup objusergroup)
	{
		if(lSusergroupRepository.findByusergroupnameAndLssitemaster(objusergroup.getUsergroupname(),objusergroup.getLssitemaster())!= null  && objusergroup.getUsergroupcode() == null)
		{
			objusergroup.setResponse(new Response());
			objusergroup.getResponse().setStatus(false);
			objusergroup.getResponse().setInformation("ID_EXIST");
			return objusergroup;
		}
		
		lSusergroupRepository.save(objusergroup);
		objusergroup.setResponse(new Response());
		objusergroup.getResponse().setStatus(true);
		objusergroup.getResponse().setInformation("ID_SUCCESSMSG");
		
		if(objusergroup.getObjsilentaudit() != null)
    	{
			LScfttransaction lscfttransactionObj = new ObjectMapper().convertValue(objusergroup.getObjsilentaudit(), new TypeReference<LScfttransaction>() {}); 
//			LSuserMaster lsuserMasterObj = lsuserMasterRepository.findByusername(lscfttransactionObj.getLsuserMaster().getUsername());
			LSuserMaster lsuserMasterObj = lsuserMasterRepository.findByusercode(lscfttransactionObj.getLsuserMaster());
			objusergroup.getObjsilentaudit().setLsuserMaster(lsuserMasterObj.getUsercode());
			//objusergroup.getObjsilentaudit().setModuleName("UserManagement");
			//objusergroup.getObjsilentaudit().setComments("Insert/Update UserGroup Successfully");
			//objusergroup.getObjsilentaudit().setActions("Insert/Update UserGroup");
			objusergroup.getObjsilentaudit().setTableName("LSusergroup");
    		lscfttransactionRepository.save(objusergroup.getObjsilentaudit());
    	}
//		Manual Audit
	
		if(objusergroup.getObjuser() != null) {
			//LScfttransaction manualAudit=new LScfttransaction();
			Date date = new Date();
			if(objusergroup.getObjmanualaudit() != null)
	    	{
			//objusergroup.getObjmanualaudit().setComments("Insert Test Successfully");
			objusergroup.getObjmanualaudit().setComments(objusergroup.getObjuser().getComments());
			//manualAudit.setActions("Insert Test");
			//manualAudit.setSystemcoments("User Generated");
			objusergroup.getObjmanualaudit().setTableName("LStestmasterlocal");
			//manualAudit.setManipulatetype("Insert");
			objusergroup.getObjmanualaudit().setLsuserMaster(objusergroup.getLSuserMaster().getUsercode());
			objusergroup.getObjmanualaudit().setLssitemaster(objusergroup.getLSuserMaster().getLssitemaster().getSitecode());
			objusergroup.getObjmanualaudit().setTransactiondate(date);
    		lscfttransactionRepository.save(objusergroup.getObjmanualaudit());
		}
		}
		return objusergroup;
	}
	
	public List<LSusergroup> GetUserGroup(LSuserMaster objusergroup)
	{
		if(objusergroup.getObjsilentaudit() != null)
    	{
			//objusergroup.getObjsilentaudit().setModuleName("UserManagement");
			//objusergroup.getObjsilentaudit().setComments("Allow to view UserGroup");
			//objusergroup.getObjsilentaudit().setActions("Allow to view");
			//objusergroup.getObjsilentaudit().setSystemcoments("System Generated");
			objusergroup.getObjsilentaudit().setTableName("LSusergroup");
    		lscfttransactionRepository.save(objusergroup.getObjsilentaudit());
    	}
		
//		return lSusergroupRepository.findByusergroupnameNotOrderByUsergroupcodeDesc("Administrator");
		
	    return lSusergroupRepository.findByusergroupnameNotOrderByUsergroupcodeAsc("Administrator");
	}
	
//	public LSusergroup ActDeactUserGroup(LSusergroup objusergroup) 
//	{
//		lSusergroupRepository.save(objusergroup);
//		return objusergroup;
//	}
	
	public List<LSuserMaster> GetUsers(LSuserMaster objusergroup)
	{
		if(objusergroup.getObjsilentaudit() != null)
    	{
			objusergroup.getObjsilentaudit().setTableName("LSuserMaster");
    		lscfttransactionRepository.save(objusergroup.getObjsilentaudit());
    	}
		if(objusergroup.getUsername().equalsIgnoreCase("Administrator")) {
//			return lsuserMasterRepository.findByusernameNot("Administrator");
			return lsuserMasterRepository.findByusernameNotAndUserretirestatusNot("Administrator",1);
		}
//		return lsuserMasterRepository.findByUsernameNotAndLssitemaster("Administrator",objusergroup.getLssitemaster());
		return lsuserMasterRepository.findByUsernameNotAndUserretirestatusNotAndLssitemaster("Administrator",1,objusergroup.getLssitemaster());
	}
	
	public List<LSuserMaster> GetUsersOnsite(LSSiteMaster objclass)
	{
		if(objclass.getObjsilentaudit() != null)
    	{
			objclass.getObjsilentaudit().setTableName("LSuserMaster");
    		lscfttransactionRepository.save(objclass.getObjsilentaudit());
    	}
		if(objclass.getSitecode() == 0) {
			return lsuserMasterRepository.findByusernameNot("Administrator");
		}
		return lsuserMasterRepository.findByUsernameNotAndLssitemaster("Administrator",objclass);
	}
	
	public LSuserMaster InsertUpdateUser(LSuserMaster objusermaster) throws MessagingException
	{
		boolean isnewuser = false;
		
		if(objusermaster.getUsercode() == null)
		{
			isnewuser = true;
		}
		
		if(objusermaster.getUsercode() == null && lsuserMasterRepository.findByusernameIgnoreCase(objusermaster.getUsername()) != null) {
			
			objusermaster.setResponse(new Response());
			objusermaster.getResponse().setStatus(false);
			objusermaster.getResponse().setInformation("ID_EXIST");
			if(objusermaster.getObjsilentaudit() != null)
	    	{   
				objusermaster.getObjsilentaudit().setActions("Warning");
				objusermaster.getObjsilentaudit().setComments(objusermaster.getModifiedby()+" "+"made attempt to create existing user name");
				objusermaster.getObjsilentaudit().setTableName("LSusergroup");
	    		lscfttransactionRepository.save(objusermaster.getObjsilentaudit());
	    	}

			if(objusermaster.getObjuser() != null)
	    	{
				objusermaster.getObjmanualaudit().setActions("Warning");
				objusermaster.getObjmanualaudit().setTableName("LScfttransaction");
				objusermaster.getObjmanualaudit().setComments(objusermaster.getObjuser().getComments());
	    		lscfttransactionRepository.save(objusermaster.getObjmanualaudit());
	    	}
			return objusermaster;
		}
		else if(objusermaster.getUsercode()!=null && objusermaster.getUserstatus()!= null && objusermaster.getLsusergroup() == null){
			LSuserMaster updateUser=lsuserMasterRepository.findOne(objusermaster.getUsercode());
			updateUser.setUserstatus(objusermaster.getUserstatus().equals("Active")?"A":"D");
			updateUser.setPassword(objusermaster.getPassword());
			updateUser.setLockcount(objusermaster.getLockcount());
			lsuserMasterRepository.save(updateUser);
			
			if(objusermaster.getObjsilentaudit() != null)
	    	{
				objusermaster.getObjsilentaudit().setTableName("LSuserMaster");
	    		lscfttransactionRepository.save(objusermaster.getObjsilentaudit());
	    	}

			if(objusermaster.getObjuser() != null) {
				Date date = new Date();
				objusermaster.getObjmanualaudit().setComments(objusermaster.getObjuser().getComments());
				objusermaster.getObjmanualaudit().setTableName("LStestmasterlocal");
				objusermaster.getObjmanualaudit().setLsuserMaster(objusermaster.getUsercode());
				objusermaster.getObjmanualaudit().setLssitemaster(objusermaster.getLssitemaster().getSitecode());
				objusermaster.getObjmanualaudit().setTransactiondate(date);
	    		lscfttransactionRepository.save(objusermaster.getObjmanualaudit());
			}
			objusermaster.setResponse(new Response());
			objusermaster.getResponse().setStatus(true);
	     	objusermaster.getResponse().setInformation("ID_SUCCESSMSG");
			
			return objusermaster;
		}
		
		if(objusermaster.getUsercode() == null && objusermaster.getIsmultitenant() != null && objusermaster.getMultitenantusercount() != null && objusermaster.getIsmultitenant() == 1)
		{
//			if(lsuserMasterRepository.countByusercodeNot(1) >= objusermaster.getMultitenantusercount())
				if(lsuserMasterRepository.countByusercodeNotAndUserretirestatusNot(1,1) >= objusermaster.getMultitenantusercount())
			{
				Response objResponse = new Response();
				objResponse.setStatus(false);
				objResponse.setInformation("ID_USERCOUNTEXCEEDS");
				
				objusermaster.setResponse(objResponse);
				
				return objusermaster;
			}
			
//			int passwordstatus=1;
//			String password = Generatetenantpassword();
//			String passwordadmin=AESEncryption.encrypt(password);
//			LSuserMaster lsuserMaster =new LSuserMaster();
//			objusermaster.setPassword(passwordadmin);	
//			objusermaster.setPasswordstatus(passwordstatus);
//			Email email = new Email();
//			email.setMailto(objusermaster.getEmailid());
//			email.setSubject("Usercreation success");
//			email.setMailcontent("<b>Dear Customer</b>,<br>"
//					+ "<i>You have successfully create user</i><br>"
////					+ "<i>Your organisation ID is <b>"+Tenant.getTenantid()+"</b>.</i><br>"
//					+ "<i>This is for your username and password.</i><br>"
//					+ "<b>UserName:\t\t "+objusermaster.getUsername()+" </b><br><b>Password:\t\t"+password+"</b>");
//			
//			emailService.sendEmail(email);
		}
		
		lsuserMasterRepository.save(objusermaster);
		

		if(isnewuser)
		{
			String unifieduser = objusermaster.getUsername().toLowerCase().replaceAll("[^a-zA-Z0-9]", "")+"u"+objusermaster.getUsercode()+"s"+objusermaster.getLssitemaster().getSitecode()+
					objusermaster.getUnifieduserid();
			
			objusermaster.setUnifieduserid(unifieduser);
			lsuserMasterRepository.save(objusermaster);
		}

		if(objusermaster.getObjsilentaudit() != null)
    	{
			objusermaster.getObjsilentaudit().setTableName("LSuserMaster");
    		lscfttransactionRepository.save(objusermaster.getObjsilentaudit());
    	}
		//Manual Audit
		if(objusermaster.getObjuser() != null) {
			Date date = new Date();
			objusermaster.getObjmanualaudit().setComments(objusermaster.getObjuser().getComments());
			objusermaster.getObjmanualaudit().setTableName("LStestmasterlocal");
			objusermaster.getObjmanualaudit().setLsuserMaster(objusermaster.getUsercode());
			objusermaster.getObjmanualaudit().setLssitemaster(objusermaster.getLssitemaster().getSitecode());
			objusermaster.getObjmanualaudit().setTransactiondate(date);
    		lscfttransactionRepository.save(objusermaster.getObjmanualaudit());
		}
		objusermaster.setResponse(new Response());
		objusermaster.getResponse().setStatus(true);
     	objusermaster.getResponse().setInformation("ID_SUCCESSMSG");
		
		return objusermaster;
	}
	
	
	private String Generatetenantpassword()
	{
		
       String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*-_=+\',/?";
       String pwd = RandomStringUtils.random( 15, characters );
      
       return pwd;
	}
	
	public LSuserMaster InsertUpdateUserfromSDMS(LSuserMaster objusermaster)
	{
		if(objusermaster.getUsercode() == null && lsuserMasterRepository.findByusernameAndLssitemaster(objusermaster.getUsername(), objusermaster.getLssitemaster()) != null) {
			
			objusermaster.setResponse(new Response());
			objusermaster.getResponse().setStatus(false);
			objusermaster.getResponse().setInformation("ID_EXIST");
			
			return objusermaster;
		}
//		else if(objusermaster.getUsercode() != null && objusermaster.getPassword().equals("reset")) {
//			
//			LSuserMaster user = lsuserMasterRepository.findByusercode(objusermaster.getUsercode());
//			user.setUserstatus(objusermaster.getUserstatus());
//			lsuserMasterRepository.save(user);
//		}
//		if(objusermaster.getUsercode() != null && objusermaster.getUserfullname() != null) {
//			LSuserMaster updateUser=lsuserMasterRepository.findOne(objusermaster.getUsercode());
//			updateUser.setUserfullname(objusermaster.getUserfullname());
//			lsuserMasterRepository.save(updateUser);
//			return objusermaster;
//		}
//		else if(objusermaster.getUsercode()!=null && objusermaster.getUserstatus()!= null){
//			LSuserMaster updateUser=lsuserMasterRepository.findOne(objusermaster.getUsercode());
//			updateUser.setUserstatus(objusermaster.getUserstatus());
//			lsuserMasterRepository.save(updateUser);
//			return objusermaster;
//		}
		LScfttransaction lscfttransactionObj = new ObjectMapper().convertValue(objusermaster.getObjsilentaudit(), new TypeReference<LScfttransaction>() {}); 
//		LSuserMaster lsuserMasterObj = lsuserMasterRepository.findByusername(lscfttransactionObj.getLsuserMaster().getUsername());
		LSuserMaster lsuserMasterObj = lsuserMasterRepository.findByusercode(lscfttransactionObj.getLsuserMaster());
		LSusergroup LSusergroupObj = lSusergroupRepository.findByusergroupname(objusermaster.getLsusergroup().getUsergroupname());
		objusermaster.setLsusergroup(LSusergroupObj);
		objusermaster.getObjsilentaudit().setLsuserMaster(lsuserMasterObj.getUsercode());
		lsuserMasterRepository.save(objusermaster);

		if(objusermaster.getObjsilentaudit() != null)
    	{
			//objusermaster.getObjsilentaudit().setModuleName("UserManagement");
			//objusermaster.getObjsilentaudit().setComments("Insert/Update User Successfully");
			//objusermaster.getObjsilentaudit().setActions("Insert/Update User");
			//objusermaster.getObjsilentaudit().setSystemcoments("System Generated");
			objusermaster.getObjsilentaudit().setTableName("LSuserMaster");
    		lscfttransactionRepository.save(objusermaster.getObjsilentaudit());
    	}
		//Manual Audit
		if(objusermaster.getObjuser() != null) {
			//LScfttransaction manualAudit=new LScfttransaction();
			Date date = new Date();
			objusermaster.getObjmanualaudit().setComments(objusermaster.getObjuser().getComments());
			//manualAudit.setModuleName("UserManagement");
			//manualAudit.setComments("Insert Test Successfully");
			//manualAudit.setActions("Insert Test");
			//manualAudit.setSystemcoments("User Generated");
			objusermaster.getObjmanualaudit().setTableName("LStestmasterlocal");
			//manualAudit.setManipulatetype("Insert");
			//manualAudit.setLsuserMaster(objusermaster);
			objusermaster.getObjmanualaudit().setLsuserMaster(objusermaster.getUsercode());
			objusermaster.getObjmanualaudit().setLssitemaster(objusermaster.getLssitemaster().getSitecode());
			objusermaster.getObjmanualaudit().setTransactiondate(date);
    		lscfttransactionRepository.save(objusermaster.getObjmanualaudit());
		}
		objusermaster.setResponse(new Response());
		objusermaster.getResponse().setStatus(true);
     	objusermaster.getResponse().setInformation("ID_SUCCESSMSG");
		
		return objusermaster;
	}
	
	public LSuserMaster ResetPassword(LSuserMaster objuser) {
		LSuserMaster updateUser=lsuserMasterRepository.findOne(objuser.getUsercode());
		updateUser.setPassword(objuser.getPassword());
		lsuserMasterRepository.save(updateUser);
		
		if(objuser.getObjsilentaudit() != null)
    	{
			objuser.getObjsilentaudit().setModuleName("UserManagement");
			objuser.getObjsilentaudit().setComments("Reset Password Successfully");
			objuser.getObjsilentaudit().setActions("Reset Password");
			objuser.getObjsilentaudit().setSystemcoments("System Generated");
			objuser.getObjsilentaudit().setTableName("LSuserMaster");
    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
    	}
		
		return objuser;
	}
	
	public LSusersteam InsertUpdateTeam(LSusersteam objteam)
	{	
		if(objteam.getTeamcode() == null && lsusersteamRepository.findByTeamnameIgnoreCaseAndStatusAndLssitemaster(objteam.getTeamname(),1,objteam.getLssitemaster()) != null) {
		
			objteam.setResponse(new Response());
			objteam.getResponse().setStatus(false);
			objteam.getResponse().setInformation("ID_EXIST");
			if(objteam.getObjsilentaudit() != null)
	    	{   
				objteam.getObjsilentaudit().setActions("Warning");
				objteam.getObjsilentaudit().setComments(objteam.getModifiedby().getUsername()+" "+"made attempt to create existing team name");
				objteam.getObjsilentaudit().setTableName("LSusersteam");
	    		lscfttransactionRepository.save(objteam.getObjsilentaudit());
	    	}
//			manual audit
			if(objteam.getObjuser() != null)
	    	{
				objteam.getObjmanualaudit().setActions("Warning");
				objteam.getObjmanualaudit().setTableName("LScfttransaction");
				objteam.getObjmanualaudit().setComments(objteam.getObjuser().getComments());
	    		lscfttransactionRepository.save(objteam.getObjmanualaudit());
	    	}
			return objteam;
		}
		else if(objteam.getStatus()== -1) {

			List<LSlogilablimsorderdetail> order= new ArrayList<LSlogilablimsorderdetail>();
//			order=LSlogilablimsorderdetailRepository.findByOrderflagAndLsprojectmaster()
			 List<LSprojectmaster> projcode=  new ArrayList<LSprojectmaster>();
			 projcode=LSprojectmasterRepository.findByLsusersteam(objteam);
				order=LSlogilablimsorderdetailRepository.findByOrderflagAndLsprojectmasterIn("N",projcode);
				if(order.size()>0) {
					objteam.setResponse(new Response());
					objteam.getResponse().setStatus(false);
					objteam.getResponse().setInformation("IDS_TEAMINPROGRESS");
					if(objteam.getObjsilentaudit() != null)
			    	{   
						objteam.getObjsilentaudit().setActions("Warning");
						objteam.getObjsilentaudit().setComments(objteam.getModifiedby().getUsername()+" "+"made attempt to delete existing team associated with orders");
						objteam.getObjsilentaudit().setTableName("LSusersteam");
			    		lscfttransactionRepository.save(objteam.getObjsilentaudit());
			    	}
//					manual audit
					if(objteam.getObjuser() != null)
			    	{
						objteam.getObjmanualaudit().setActions("Warning");
						objteam.getObjmanualaudit().setTableName("LScfttransaction");
						objteam.getObjmanualaudit().setComments(objteam.getObjuser().getComments());
			    		lscfttransactionRepository.save(objteam.getObjmanualaudit());
			    	}
					return objteam;
				}
				else {
			lsusersteamRepository.save(objteam);
			objteam.setResponse(new Response());
			objteam.getResponse().setStatus(true);
			objteam.getResponse().setInformation("ID_SUCCESSMSG");
				}
			if(objteam.getObjsilentaudit() != null)
	    	{
				//objteam.getObjsilentaudit().setModuleName("UserManagement");
				//objteam.getObjsilentaudit().setComments("Delete team Successfully");
				//objteam.getObjsilentaudit().setActions("DeleteTeam");
				//objteam.getObjsilentaudit().setSystemcoments("System Generated");
				objteam.getObjsilentaudit().setTableName("LSuserteam");
	    		lscfttransactionRepository.save(objteam.getObjsilentaudit());
	    	}
//			//Manual Audit
			if(objteam.getObjuser() != null) {
				//LScfttransaction manualAudit=new LScfttransaction();
				
				if(objteam.getObjmanualaudit() != null)
		    	{
				
				Date date = new Date();
				
				/*manualAudit.setModuleName("UserManagement");
				manualAudit.setComments("Insert Test Successfully");
				manualAudit.setActions("Insert Test");
				manualAudit.setSystemcoments("User Generated");*/
				objteam.getObjmanualaudit().setTableName("LStestmasterlocal");
				objteam.getObjmanualaudit().setComments(objteam.getObjuser().getComments());
				//manualAudit.setManipulatetype("Insert");
				objteam.getObjmanualaudit().setLsuserMaster(objteam.getModifieduserMaster().getUsercode());
				objteam.getObjmanualaudit().setLssitemaster(objteam.getModifieduserMaster().getLssitemaster().getSitecode());
				objteam.getObjmanualaudit().setTransactiondate(date);
	    		lscfttransactionRepository.save(objteam.getObjmanualaudit());
			}
			}
			return objteam;
		}
		
		/*lsusersteamRepository.save(objteam);
		
		List<LSuserMaster> lstuser = objteam.getLsuserMaster();
		
		List<LSuserteammapping> lstuserteam = new ArrayList<LSuserteammapping>();
		for (LSuserMaster objuser : lstuser) 
		{ 
			LSuserteammapping objmap = new LSuserteammapping();
			objmap.setLsuserMaster(objuser);
			objmap.setTeamcode(objteam.getTeamcode());
			lstuserteam.add(objmap);
		}
		
		lsuserteammappingRepository.save(lstuserteam);*/
		
		lsusersteamRepository.save(objteam);
		objteam.setResponse(new Response());
		objteam.getResponse().setStatus(true);
		objteam.getResponse().setInformation("ID_SUCCESSMSG");
		
		for(LSuserMaster objuser : objteam.getLsuserMaster())
		{
			LSuserteammapping objmap = new LSuserteammapping();
			
			objmap.setLsuserMaster(objuser);
			objmap.setTeamcode(objteam.getTeamcode());
			lsuserteammappingRepository.save(objmap);	
		}
		
//		lsuserteammappingRepository.deleteLSuserteammappingByteamcode();

		if(objteam.getObjsilentaudit() != null)
    	{
			objteam.getObjsilentaudit().setTableName("LSuserteam");
    		lscfttransactionRepository.save(objteam.getObjsilentaudit());
    	}
//		if(objteam.getObjuser() != null) {
//			Date date = new Date();
			
//			if(objteam.getObjmanualaudit() != null)
//	    	{
//				objteam.getObjmanualaudit().setTableName("LStestmasterlocal");
//				objteam.getObjmanualaudit().setComments(objteam.getObjuser().getComments());
//				objteam.getObjmanualaudit().setLsuserMaster(objteam.getModifieduserMaster());
//				objteam.getObjmanualaudit().setLssitemaster(objteam.getModifieduserMaster().getLssitemaster());
//				objteam.getObjmanualaudit().setTransactiondate(date);
//	    		lscfttransactionRepository.save(objteam.getObjmanualaudit());
//	    	}
//		}
		return objteam;
	}
	
	public List<LSusersteam> GetUserTeam(LSuserMaster LSuserMaster)
	{	
		if(LSuserMaster.getUsername().equalsIgnoreCase("Administrator")) {
			return lsusersteamRepository.findBystatus(1);
		}
		return lsusersteamRepository.findBylssitemasterAndStatus(LSuserMaster.getLssitemaster(),1);
	}
	
	public Map<String,Object> GetUserTeamonSitevise(LSSiteMaster objclass)
	{	
		Map<String,Object> map= new HashMap<String, Object>();
		if(objclass.getSitecode() == 0) {
			List<LSusersteam> obj= new ArrayList<LSusersteam>();
			obj=lsusersteamRepository.findBystatus(1);
//			obj.get(0).getLsuserMaster().get(0).setLsuserMaster(lsuserMasterRepository.findByusernameNot("Administrator"));
			List<LSuserMaster> user= lsuserMasterRepository.findByusernameNot("Administrator");
			 map.put("obj", obj);
			 map.put("user", user);
			return map;
		}
	 map.put("user", lsuserMasterRepository.findByUsernameNotAndLssitemaster("Administrator",objclass));
		map.put("obj", lsusersteamRepository.findBylssitemasterAndStatus(objclass,1));
		return map;
	}
	
	public Map<String, Object> GetUserRightsonGroup(LSusergroup lsusergroup)
	{
		Map<String, Object> maprights = new HashMap<String, Object>();
		if(lsusergroup.getUsergroupcode()==null) {
			List<LSusergrouprightsmaster> lstUserrightsmaster = lsusergrouprightsmasterRepository.findByOrderBySequenceorder();
			maprights.put("new", true);
			maprights.put("rights", lstUserrightsmaster);
		}
		else {
		List<LSusergrouprights> lstUserrights = lsusergrouprightsRepository.findByusergroupid(lsusergroup);
		List<LSusergrouprightsmaster> lstUserrightsmasterlst = lsusergrouprightsmasterRepository.findByOrderBySequenceorder();
		if(lstUserrights != null && lstUserrights.size() >0 && lstUserrights.size() >10)
		{
			maprights.put("new", false);
			maprights.put("rights", lstUserrights);
			maprights.put("masterrights", lstUserrightsmasterlst);
		}
		else
		{
			List<LSusergrouprightsmaster> lstUserrightsmaster = lsusergrouprightsmasterRepository.findByOrderBySequenceorder();
			maprights.put("new", true);
			maprights.put("rights", lstUserrightsmaster);
		}
		}
		if(lsusergroup.getObjsilentaudit() != null)
    	{
			//lsusergroup.getObjsilentaudit().setModuleName("UserManagement");
			//lsusergroup.getObjsilentaudit().setComments("Allow to view UserRights");
			//lsusergroup.getObjsilentaudit().setActions("Allow to view");
			//lsusergroup.getObjsilentaudit().setSystemcoments("System Generated");
			lsusergroup.getObjsilentaudit().setTableName("LSusergroup");
    		lscfttransactionRepository.save(lsusergroup.getObjsilentaudit());
    	}
		return maprights;
	}
	
	public Map<String, Object> GetUserRightsonUser(LSuserMaster objUser)
	{
		LSuserMaster objupdateduser = lsuserMasterRepository.findByusercode(objUser.getUsercode());
		LSusergroup lsusergroup = objupdateduser.getLsusergroup();
		Map<String, Object> maprights = new HashMap<String, Object>();
		if(lsusergroup.getUsergroupcode()==null) {
			List<LSusergrouprightsmaster> lstUserrightsmaster = lsusergrouprightsmasterRepository.findAll();
			maprights.put("new", true);
			maprights.put("rights", lstUserrightsmaster);
		}
		else {
		List<LSusergrouprights> lstUserrights = lsusergrouprightsRepository.findByusergroupid(lsusergroup);
		if(lstUserrights != null && lstUserrights.size() >0)
		{
			maprights.put("new", false);
			maprights.put("rights", lstUserrights);
			
		}
		else
		{
			List<LSusergrouprightsmaster> lstUserrightsmaster = lsusergrouprightsmasterRepository.findAll();
			maprights.put("new", true);
			maprights.put("rights", lstUserrightsmaster);
		}
		}
		if(lsusergroup.getObjsilentaudit() != null)
    	{
			//lsusergroup.getObjsilentaudit().setModuleName("UserManagement");
			//lsusergroup.getObjsilentaudit().setComments("Allow to view UserRights");
			//lsusergroup.getObjsilentaudit().setActions("Allow to view");
			//lsusergroup.getObjsilentaudit().setSystemcoments("System Generated");
			lsusergroup.getObjsilentaudit().setTableName("LSusergroup");
    		lscfttransactionRepository.save(lsusergroup.getObjsilentaudit());
    	}
		return maprights;
	}
	
	public List<LSusergrouprights> SaveUserRights(List<LSusergrouprights> lsrights)
	{
		if(lsrights.get(0).getUsergroupid()==null) {
			   lsrights.get(0).setResponse(new Response());
		        lsrights.get(0).getResponse().setStatus(false);
		        lsrights.get(0).getResponse().setInformation("ID_USERRYTSERR");
					return lsrights;
					}
		else {
		if(((LSusergrouprights) lsrights.get(0)).getObjsilentaudit() != null)
    	{
			//((LSusergrouprights) lsrights.get(0)).getObjsilentaudit().setModuleName("UserManagement");
			//objuser.getObjsilentaudit().setComments("Allow to view Active user");
			//objuser.getObjsilentaudit().setActions("Allow to view");
			//objuser.getObjsilentaudit().setSystemcoments("System Generated");
			((LSusergrouprights) lsrights.get(0)).getObjsilentaudit().setTableName("LSuserMaster");
    		lscfttransactionRepository.save(((LSusergrouprights) lsrights.get(0)).getObjsilentaudit());
    	}
		
		lsusergrouprightsRepository.save(lsrights);
		//Manual Audit
		if(lsrights.get(0).getObjuser() != null) {
			
			//LScfttransaction manualAudit=new LScfttransaction();
			Date date = new Date();
			if(lsrights.get(0).getObjmanualaudit() != null)
	    	{

			
			//manualAudit.setModuleName("User Master");
			//manualAudit.setComments("Update file test Successfully");
			//manualAudit.setActions("Update file test");
			//manualAudit.setSystemcoments("System Generated");
			lsrights.get(0).getObjmanualaudit().setComments(lsrights.get(0).getObjuser().getComments());
			lsrights.get(0).getObjmanualaudit().setTableName("LSworkflow");
			//manualAudit.setManipulatetype("Insert");
			lsrights.get(0).getObjmanualaudit().setLsuserMaster(lsrights.get(0).getObjmanualaudit().getLsuserMaster());
			lsrights.get(0).getObjmanualaudit().setLssitemaster(lsrights.get(0).getObjmanualaudit().getLssitemaster());
			lsrights.get(0).getObjmanualaudit().setTransactiondate(date);
    		lscfttransactionRepository.save(lsrights.get(0).getObjmanualaudit());
		}
		}
		lsrights.get(0).setResponse(new Response());
		lsrights.get(0).getResponse().setStatus(true);
		lsrights.get(0).getResponse().setInformation("ID_ALERT");
		}
		return lsrights;
		
	}

	public List<LSactiveUser> GetActiveUsers(LSuserMaster objuser) {
		if(objuser.getObjsilentaudit() != null)
    	{
			objuser.getObjsilentaudit().setTableName("LSuserMaster");
    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
    	}
		return lsactiveUserRepository.findAll();
		
	}
	
	public List<LSactiveUser> GetActiveUsersOnsitewise(LSSiteMaster objclass) {
		if(objclass.getObjsilentaudit() != null)
    	{
			objclass.getObjsilentaudit().setTableName("LSuserMaster");
    		lscfttransactionRepository.save(objclass.getObjsilentaudit());
    	}
		return lsactiveUserRepository.findBylssitemaster(objclass);
	}

	public List<LSusergroup> GetActiveUserGroup(LSuserMaster objusergroup) {
		
		if(objusergroup.getObjsilentaudit() != null)
    	{
			objusergroup.getObjsilentaudit().setTableName("LSusergroup");
    		lscfttransactionRepository.save(objusergroup.getObjsilentaudit());
    	}
		
		return lSusergroupRepository.findByusergroupstatusAndUsergroupnameNot("A","Administrator");
	}
	
	public List<LSusergroup> GetSiteWiseUserGroup(LSSiteMaster Objclass) {
		
		if(Objclass.getObjsilentaudit() != null)
    	{
			Objclass.getObjsilentaudit().setTableName("LSusergroup");
    		lscfttransactionRepository.save(Objclass.getObjsilentaudit());
    	}
		if(Objclass.getSitecode() == 0) {
//			return lSusergroupRepository.findByusergroupstatusAndUsergroupnameNot("A","Administrator");
			return lSusergroupRepository.findByusergroupstatusAndUsergroupnameNotOrderByUsergroupcodeAsc("A","Administrator");
		}
//		return lSusergroupRepository.findBylssitemasterAndUsergroupnameNotOrderByUsergroupcodeDesc(Objclass.getSitecode(),"Administrator");
		return lSusergroupRepository.findBylssitemasterAndUsergroupstatusAndUsergroupnameNotOrderByUsergroupcodeAsc(Objclass.getSitecode(),"A","Administrator");
	}
	
	public List<LSusergroup> GetUserGroupSiteWise(LSSiteMaster Objclass) {
		
		if(Objclass.getObjsilentaudit() != null)
    	{
			Objclass.getObjsilentaudit().setTableName("LSusergroup");
    		lscfttransactionRepository.save(Objclass.getObjsilentaudit());
    	}
		if(Objclass.getSitecode() == 0) {
			return lSusergroupRepository.findByusergroupnameNotOrderByUsergroupcodeDesc("Administrator");
		}
		return lSusergroupRepository.findBylssitemasterAndUsergroupnameNotOrderByUsergroupcodeDesc(Objclass.getSitecode(),"Administrator");
	}
	public LSuserMaster ValidateSignature(LoggedUser objuser) {
		LSuserMaster objExitinguser = new LSuserMaster();
		String username = objuser.getsUsername();
		LSSiteMaster objsite = LSSiteMasterRepository.findBysitecode(Integer.parseInt(objuser.getsSiteCode()));
		objExitinguser = lsuserMasterRepository.findByusernameAndLssitemaster(username, objsite);
		
		if(objExitinguser != null)
		{
		    String Password = AESEncryption.decrypt(objExitinguser.getPassword());
		    objExitinguser.setObjResponse(new Response());
		    
		    if(objuser.getIsmultitenant() == 1) {
		    	objExitinguser.getObjResponse().setStatus(true);
		    	
		    	LScfttransaction manualAudit=new LScfttransaction();
				Date date = new Date();
				
				manualAudit.setModuleName("Register Task Orders & Execute");
				manualAudit.setComments(objuser.getsComments());
				manualAudit.setActions("E-Signature");
				manualAudit.setSystemcoments("User Generated");
				manualAudit.setTableName("E-Signature");
				manualAudit.setManipulatetype("E-Signature");
				manualAudit.setLsuserMaster(objExitinguser.getUsercode());
				manualAudit.setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
				manualAudit.setTransactiondate(date);
	    		lscfttransactionRepository.save(manualAudit);
		    }
		    else {
		    	if(Password.equals(objuser.getsPassword()))
			    {
			    	objExitinguser.getObjResponse().setStatus(true);
			    	
			    	LScfttransaction manualAudit=new LScfttransaction();
					Date date = new Date();
					
					manualAudit.setModuleName("Register Task Orders & Execute");
					manualAudit.setComments(objuser.getsComments());
					manualAudit.setActions("E-Signature");
					manualAudit.setSystemcoments("User Generated");
					manualAudit.setTableName("E-Signature");
					manualAudit.setManipulatetype("E-Signature");
					manualAudit.setLsuserMaster(objExitinguser.getUsercode());
					manualAudit.setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
					manualAudit.setTransactiondate(date);
		    		lscfttransactionRepository.save(manualAudit);
			    }
			    else
				{
					objExitinguser.getObjResponse().setInformation("Invalid password");
					objExitinguser.getObjResponse().setStatus(false);
				}
		    }
		}
		else
		{   
			objExitinguser = lsuserMasterRepository.findByusernameAndLssitemaster("Administrator",objsite);
			objExitinguser.setObjResponse(new Response());
			objExitinguser.getObjResponse().setInformation("User not exist");
			objExitinguser.getObjResponse().setStatus(false);
		}
		
		return objExitinguser;
	}
	

	public LSPasswordPolicy PasswordpolicySave(LSPasswordPolicy objpwd) {
//		if(objpwd.getPolicycode() == null ) {
//		if(objpwd.getMaxpasswrdlength()>=objpwd.getMinpasswrdlength()) {
		lSpasswordpolicyRepository.save(objpwd);
			
		if(objpwd.getObjsilentaudit() != null)
    	{
			//objpwd.getObjsilentaudit().setModuleName("UserManagement");
			//objpwd.getObjsilentaudit().setComments("Password policy saved Successfully");
			//objpwd.getObjsilentaudit().setActions("Password policy save");
			//objpwd.getObjsilentaudit().setSystemcoments("System Generated");
			objpwd.getObjsilentaudit().setTableName("LSPasswordPolicy");
    		lscfttransactionRepository.save(objpwd.getObjsilentaudit());
    	}
		if(objpwd.getObjuser()!= null)
    	{
			objpwd.getObjmanualaudit().setComments(objpwd.getObjuser().getComments());
			objpwd.getObjmanualaudit().setTableName("LSPasswordPolicy");
    		lscfttransactionRepository.save(objpwd.getObjmanualaudit());
    	}
		objpwd.setResponse(new Response());
	    objpwd.getResponse().setStatus(true);
		objpwd.getResponse().setInformation("Changes saved successfully");
		return objpwd;
		
		
	}
	
	public LSPasswordPolicy GetPasswordPolicy(LSPasswordPolicy objpwd) {

		if(objpwd.getObjsilentaudit() != null)
    	{
			//objpwd.getObjsilentaudit().setModuleName("UserManagement");
			//objpwd.getObjsilentaudit().setComments("Allow to view Password policy");
			//objpwd.getObjsilentaudit().setActions("view");
			//objpwd.getObjsilentaudit().setSystemcoments("System Generated");
			objpwd.getObjsilentaudit().setTableName("LSPasswordPolicy");
    		lscfttransactionRepository.save(objpwd.getObjsilentaudit());
    	}
		LSPasswordPolicy policy= new LSPasswordPolicy();
		if(objpwd.getLssitemaster()!=null)			
		{
			policy =lSpasswordpolicyRepository.findTopByAndLssitemasterOrderByPolicycodeDesc(objpwd.getLssitemaster());
			}
		else {
			policy =lSpasswordpolicyRepository.findTopByOrderByPolicycodeAsc();
		}
		return policy;
		
	}
	
	public LSPasswordPolicy GetPasswordPolicySitewise(LSPasswordPolicy objpwd) {

		LSPasswordPolicy policy=lSpasswordpolicyRepository.findTopByAndLssitemasterOrderByPolicycodeDesc(objpwd.getLssitemaster());
		LSuserMaster user= lsuserMasterRepository.findByusercode(1);
		if(policy == null && objpwd.getLssitemaster()!= null) {
			
			LSPasswordPolicy value=new LSPasswordPolicy();
		
			value.setComplexpasswrd(0);
//			objpwd.setDbbased(dbbased);
			
			value.setMinpasswrdlength(4);
			value.setMaxpasswrdlength(10);
			value.setPasswordhistory(5);
			value.setPasswordexpiry(90);
			
			value.setMincapitalchar(0);
			value.setLockpolicy(5);
			value.setMinsmallchar(0);
			value.setMinnumericchar(0);
			value.setMinspecialchar(0);
			value.setLsusermaster(objpwd.getLsusermaster()!=null ?objpwd.getLsusermaster():user);
			value.setLssitemaster(lSpasswordpolicyRepository.findByLssitemaster(objpwd.getLssitemaster())==null?objpwd.getLssitemaster():null);
			
			lSpasswordpolicyRepository.save(value);
			return value;
		}
//		LSPasswordPolicy policy =lSpasswordpolicyRepository.findByLssitemaster(objpwd.getLssitemaster());
//		
//		if(policy == null)
//			policy = lSpasswordpolicyRepository.findTopByOrderByPolicycodeAsc();
		
		return policy;
	}
	
	public Map<String, Object> Getnotification(LSuserMaster lsuserMaster)
	{
		Map<String, Object> objresmap = new HashMap<String, Object>();
		
		objresmap.put("newnotificationcount", lsnotificationRepository.countByNotifationtoAndIsnewnotification(lsuserMaster, 1));
		objresmap.put("notification", lsnotificationRepository.findFirst10ByNotifationtoOrderByNotificationcodeDesc(lsuserMaster));
		
		return objresmap;
	}
	
	public Map<String, Object> Updatenotificationread(LSnotification lsnotification)
	{
		Map<String, Object> objresmap = new HashMap<String, Object>();
		
		lsnotificationRepository.updatenotificationstatus(lsnotification.getNotifationto(), lsnotification.getNotificationcode());

		objresmap = GetLatestnotification(lsnotification);
		
		return objresmap;
	}
	
	public Map<String, Object> GetnotificationonLazyload(LSnotification lsnotification)
	{
		Map<String, Object> objresmap = new HashMap<String, Object>();
		
		objresmap.put("notification", lsnotificationRepository.findFirst10ByNotifationtoAndNotificationcodeLessThanOrderByNotificationcodeDesc(lsnotification.getNotifationto(), lsnotification.getNotificationcode()));
		
		return objresmap;
	}
	
	public Map<String, Object> GetLatestnotification(LSnotification lsnotification)
	{
		Map<String, Object> objresmap = new HashMap<String, Object>();
	
		objresmap.put("newnotificationcount", lsnotificationRepository.countByNotifationtoAndIsnewnotificationAndNotificationcodeGreaterThan(lsnotification.getNotifationto(), 1, lsnotification.getNotificationcode()));
		objresmap.put("notification", lsnotificationRepository.findByNotifationtoAndNotificationcodeGreaterThanOrderByNotificationcodeDesc(lsnotification.getNotifationto(), lsnotification.getNotificationcode()));
		
		return objresmap;
	}
	
	public LSuserActions UpdateUseraction(LSuserActions objuseractions) {
		return lsuserActionsRepository.save(objuseractions);
	}
	
	public LSuserActions UpdatefreshUseraction(LSuserActions objuseractions) {
		
		LSuserActions objupdated = lsuserActionsRepository.save(objuseractions);
		lsuserMasterRepository.setuseractionByusercode(objupdated, objupdated.getUsercode());
		return objupdated;
	}
	
	public List<LSusergroup> Loadtenantusergroups() {
        List<LSusergroup> result = new ArrayList<LSusergroup>();
        result=lSusergroupRepository.findAll();
        return result;
    }
	
	public LScentralisedUsers Createcentraliseduser(LScentralisedUsers objctrluser)
	{
		LScentralisedUsers objunifieduser = lscentralisedUsersRepository.findByUnifieduseridIgnoreCase(objctrluser.getUnifieduserid());
		if(objunifieduser == null || objunifieduser.getUnifieduserid() == null)
		{
			lscentralisedUsersRepository.save(objctrluser);
		}
		return objctrluser;
	}
	
	public LSuserMaster Usersendpasswormail(LSuserMaster objusermaster) throws MessagingException
	{
		
		if( objusermaster.getIsmultitenant() != null && objusermaster.getMultitenantusercount() != null && objusermaster.getIsmultitenant() == 1)
		{
//			int passwordstatus=1;
			String password = Generatetenantpassword();
			String passwordadmin=AESEncryption.encrypt(password);
			LSuserMaster lsuserMaster =new LSuserMaster();
//			lsuserMaster.setPassword(passwordadmin);
			objusermaster.setPassword(passwordadmin);	
//			objusermaster.setPasswordstatus(passwordstatus);
			Email email = new Email();
			email.setMailto(objusermaster.getEmailid());
			email.setSubject("Usercreation success");
			email.setMailcontent("<b>Dear Customer</b>,<br>"
					+ "<i>You have successfully create user</i><br>"
//					+ "<i>Your organisation ID is <b>"+Tenant.getTenantid()+"</b>.</i><br>"
					+ "<i>This is for your username and password.</i><br>"
					+ "<b>UserName:\t\t "+objusermaster.getUsername()+" </b><br><b>Password:\t\t"+password+"</b>");
			
			emailService.sendEmail(email);
			lsuserMasterRepository.setpasswordandpasswordstatusByusercode(objusermaster.getPassword(),objusermaster.getPasswordstatus(),objusermaster.getUsercode());
		}
		return objusermaster;
		
	}
	
	public List<LScentralisedUsers> Getallcentraliseduser(LScentralisedUsers objctrluser)
	{
		return lscentralisedUsersRepository.findAll();
	}
	
	public LScentralisedUsers Getcentraliseduserbyid(LScentralisedUsers objctrluser) {
		return lscentralisedUsersRepository.findByUnifieduseridIgnoreCase(objctrluser.getUnifieduserid());
	}
	
	public List<LSuserMaster> GetUserslocal(LSuserMaster objusergroup)
	{
		if(objusergroup.getObjsilentaudit() != null)
    	{
			objusergroup.getObjsilentaudit().setTableName("LSuserMaster");
    		lscfttransactionRepository.save(objusergroup.getObjsilentaudit());
    	}
		if(objusergroup.getUsername().equalsIgnoreCase("Administrator")) {
//			return lsuserMasterRepository.findByusernameNot("Administrator");
			return lsuserMasterRepository.findByusernameNot("Administrator");
		}
//		return lsuserMasterRepository.findByUsernameNotAndLssitemaster("Administrator",objusergroup.getLssitemaster());
		return lsuserMasterRepository.findByUsernameNotAndLssitemaster("Administrator",objusergroup.getLssitemaster());
	}
	
	public LSuserMaster getUserOnCode(LSuserMaster objuser) {
		return lsuserMasterRepository.findByusercode(objuser.getUsercode());
	}

	
}
