package com.agaram.eln.primary.controller.usermanagement;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.model.cloudFileManip.CloudProfilePicture;
import com.agaram.eln.primary.model.fileManipulation.ProfilePicture;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSPasswordPolicy;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSactiveUser;
import com.agaram.eln.primary.model.usermanagement.LScentralisedUsers;
import com.agaram.eln.primary.model.usermanagement.LSnotification;
import com.agaram.eln.primary.model.usermanagement.LSuserActions;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.model.usermanagement.LSusergrouprights;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.service.cfr.AuditService;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.fileManipulation.FileManipulationservice;
import com.agaram.eln.primary.service.usermanagement.UserService;

@RestController
@RequestMapping(value = "/User", method = RequestMethod.POST)
public class UserController {

	@Autowired
    private UserService userService;
	@Autowired
	private AuditService auditService;
	
	@Autowired
	private FileManipulationservice fileManipulationservice;
	
	@Autowired
	private CloudFileManipulationservice cloudFileManipulationservice;
	/**
	 * UserGroup
	 * 
	 * @param objusergroup
	 * @return
	 */
	Map<String, Object> map=new HashMap<>();
	
	@PostMapping("/InsertUpdateUserGroup")
	public LSusergroup InsertUpdateUserGroup(@RequestBody LSusergroup objusergroup)
	{
		if(objusergroup.getObjuser() != null) {
			
			LSuserMaster userClass = auditService.CheckUserPassWord(objusergroup.getObjuser());
			
			if(userClass.getObjResponse().getStatus()) {
				
				objusergroup.setLSuserMaster(userClass);
				
				return userService.InsertUpdateUserGroup(objusergroup);
			}
			else
			{
				objusergroup.getObjsilentaudit().setComments("Entered invalid username and password");
				Map<String, Object> map=new HashMap<>();
				map.put("objsilentaudit",objusergroup.getObjsilentaudit());
				map.put("objmanualaudit",objusergroup.getObjmanualaudit());
				map.put("objUser",objusergroup.getObjuser());
				auditService.AuditConfigurationrecord(map);
				objusergroup.setResponse(new Response());
				objusergroup.getResponse().setStatus(false);
				objusergroup.getResponse().setInformation("ID_VALIDATION");
				return objusergroup;
			}
			
		}
		return userService.InsertUpdateUserGroup(objusergroup);
	}
	
	@PostMapping("/InsertUpdateUserGroupFromSDMS")
	public LSusergroup InsertUpdateUserGroupFromSDMS(@RequestBody LSusergroup objusergroup)
	{
		if(objusergroup.getObjuser() != null) {
			
			LSuserMaster userClass = auditService.CheckUserPassWord(objusergroup.getObjuser());
			
			if(userClass.getObjResponse().getStatus()) {
				
				objusergroup.setLSuserMaster(userClass);
				
				return userService.InsertUpdateUserGroupFromSDMS(objusergroup);
			}
			else
			{
				objusergroup.setResponse(new Response());
				objusergroup.getResponse().setStatus(false);
				objusergroup.getResponse().setInformation("ID_VALIDATION");
				return objusergroup;
			}
			
		}
		return userService.InsertUpdateUserGroupFromSDMS(objusergroup);
	}
	
	@PostMapping("/GetUserGroup")
	public List<LSusergroup> GetUserGroup(@RequestBody LSuserMaster objusermaster)
	{
	  return userService.GetUserGroup(objusermaster);
	}
	
	@PostMapping("/GetActiveUserGroup")
	public List<LSusergroup> GetActiveUserGroup(@RequestBody LSuserMaster objusermaster)
	{
	  return userService.GetActiveUserGroup(objusermaster);
	}

	@PostMapping("/GetSiteWiseUserGroup")
	public List<LSusergroup> GetSiteWiseUserGroup(@RequestBody LSSiteMaster objclass)
	{
	  return userService.GetSiteWiseUserGroup(objclass);
	}
	@PostMapping("/GetUserGroupSiteWise")
	public List<LSusergroup> GetUserGroupSiteWise(@RequestBody LSSiteMaster objclass)
	{
	  return userService.GetUserGroupSiteWise(objclass);
	}
//	@PostMapping("/ActDeactUserGroup")
//	public LSusergroup ActDeactUserGroup(@RequestBody LSusergroup objusergroup)
//	{
//		return userService.ActDeactUserGroup(objusergroup);
//	}
	
	/**
	 * UserMaster
	 * 
	 * @param objuser
	 * @return
	 * @throws MessagingException 
	 */
	@PostMapping("/InsertUpdateUser")
	public LSuserMaster InsertUpdateUser(@RequestBody LSuserMaster objusermaster) throws MessagingException
	{
		if(objusermaster.getObjuser() != null) {
			if(objusermaster.getUserstatus().trim() == "Active") {
				objusermaster.setUserstatus("A");
			} else if(objusermaster.getUserstatus().trim() == "Deactive") {
				objusermaster.setUserstatus("D");
			}
			else {
				objusermaster.setUserstatus("Locked");
			}
			LSuserMaster userClass = auditService.CheckUserPassWord(objusermaster.getObjuser());
			
			if(userClass.getObjResponse().getStatus()) {
				
//				objusermaster.setUserClass(userClass);
				
				return userService.InsertUpdateUser(objusermaster);
			}
			else
			{
				objusermaster.getObjsilentaudit().setComments("Entered invalid username and password");
//				Map<String, Object> map=new HashMap<>();
				map.put("objsilentaudit",objusermaster.getObjsilentaudit());
				map.put("objmanualaudit",objusermaster.getObjmanualaudit());
				map.put("objUser",objusermaster.getObjuser());
				auditService.AuditConfigurationrecord(map);
				objusermaster.setResponse(new Response());
				objusermaster.getResponse().setStatus(false);
				objusermaster.getResponse().setInformation("ID_VALIDATION");
				return objusermaster;
			}
			
		}
		return userService.InsertUpdateUser(objusermaster);
	}
	
	@PostMapping("/InsertUpdateUserfromSDMS")
	public LSuserMaster InsertUpdateUserfromSDMS(@RequestBody LSuserMaster objusermaster)
	{
		if(objusermaster.getObjuser() != null) {
			if(objusermaster.getUserstatus().trim() == "Active") {
				objusermaster.setUserstatus("A");
			} else if(objusermaster.getUserstatus().trim() == "Deactive") {
				objusermaster.setUserstatus("D");
			}
			else {
				objusermaster.setUserstatus("Locked");
			}
			LSuserMaster userClass = auditService.CheckUserPassWord(objusermaster.getObjuser());
			
			if(userClass.getObjResponse().getStatus()) {
				
//				objusermaster.setUserClass(userClass);
				
				return userService.InsertUpdateUserfromSDMS(objusermaster);
			}
			else
			{
				objusermaster.setResponse(new Response());
				objusermaster.getResponse().setStatus(false);
				objusermaster.getResponse().setInformation("ID_VALIDATION");
				return objusermaster;
			}
			
		}
		return userService.InsertUpdateUserfromSDMS(objusermaster);
	}
	
	@PostMapping("/ResetPassword")
	public LSuserMaster ResetPassword(@RequestBody LSuserMaster objuser)
	{
		return userService.ResetPassword(objuser);
	}
	
	@PostMapping("/GetUsers")
	public List<LSuserMaster> GetUsers(@RequestBody LSuserMaster objusermaster)
	{
		return userService.GetUsers(objusermaster);
	}
	
	@PostMapping("/GetUsersOnsite")
	public List<LSuserMaster> GetUsersOnsite(@RequestBody LSSiteMaster objclass)
	{
		return userService.GetUsersOnsite(objclass);
	}
	/**
	 * Project Team
	 * 
	 * @param objteam
	 * @return
	 */
	@PostMapping("/InsertUpdateTeam")
	public LSusersteam InsertUpdateTeam(@RequestBody LSusersteam objteam)
	{
		if(objteam.getObjuser() != null) {
			
			LSuserMaster userClass = auditService.CheckUserPassWord(objteam.getObjuser());
			
			if(userClass.getObjResponse().getStatus()) {
				
				objteam.setModifieduserMaster(userClass);
				
				return userService.InsertUpdateTeam(objteam);
			}
			else
			{
				objteam.getObjsilentaudit().setComments("Entered invalid username and password");
//				Map<String, Object> map=new HashMap<>();
				map.put("objsilentaudit",objteam.getObjsilentaudit());
				map.put("objmanualaudit",objteam.getObjmanualaudit());
				map.put("objUser",objteam.getObjuser());
				auditService.AuditConfigurationrecord(map);
				objteam.setResponse(new Response());
				objteam.getResponse().setStatus(false);
				objteam.getResponse().setInformation("ID_VALIDATION");
				return objteam;
			}
			
		}
		return userService.InsertUpdateTeam(objteam);
	}
	
	@PostMapping("/GetUserTeam")
	public List<LSusersteam> GetUserTeam(@RequestBody LSuserMaster LSuserMaster)
	{
		return userService.GetUserTeam(LSuserMaster);
	}
	
	@PostMapping("/GetUserTeamonSitevise")
	public  Map<String,Object> GetUserTeamonSitevise(@RequestBody LSSiteMaster objclass)
	{
		return userService.GetUserTeamonSitevise(objclass);
	}
	
	@PostMapping("/GetUserRightsonGroup")
	public Map<String, Object> GetUserRightsonGroup(@RequestBody LSusergroup lsusergroup)
	{
		return userService.GetUserRightsonGroup(lsusergroup);
	}
	
	@PostMapping("/GetUserRightsonUser")
	public Map<String, Object> GetUserRightsonUser(@RequestBody LSuserMaster objUser)
	{
		return userService.GetUserRightsonUser(objUser);
	}
	
	@PostMapping("/SaveUserRights")
	public List<LSusergrouprights> SaveUserRights(@RequestBody List<LSusergrouprights> lsrights)
	{
		if(lsrights.get(0).getObjuser() != null) {
		
		LSuserMaster userClass = auditService.CheckUserPassWord(lsrights.get(0).getObjuser());
        if(userClass.getObjResponse().getStatus()) {
			
        	lsrights.get(0).setLsuserMaster(userClass);
			
        	return userService.SaveUserRights(lsrights);
		}
		else
		{
			lsrights.get(0).getObjsilentaudit().setComments("Entered invalid username and password");
			map.put("objsilentaudit",lsrights.get(0).getObjsilentaudit());
			map.put("objmanualaudit",lsrights.get(0).getObjmanualaudit());
			map.put("objUser",lsrights.get(0).getObjuser());
			auditService.AuditConfigurationrecord(map);
        lsrights.get(0).setResponse(new Response());
        lsrights.get(0).getResponse().setStatus(false);
        lsrights.get(0).getResponse().setInformation("ID_VALIDATION");
			return lsrights;
		}
		
		
	}
		return userService.SaveUserRights(lsrights);
	}
	
	@PostMapping("/GetActiveUsers")
	public List<LSactiveUser> GetActiveUsers(@RequestBody LSuserMaster lsuserMaster)
	{
		return userService.GetActiveUsers(lsuserMaster);
	}
	
	@PostMapping("/GetActiveUsersOnsitewise")
	public List<LSactiveUser> GetActiveUsersOnsitewise(@RequestBody LSSiteMaster objclass)
	{
		return userService.GetActiveUsersOnsitewise(objclass);
	}
	
	@PostMapping("/ValidateSignature")
	public LSuserMaster ValidateSignature(@RequestBody LoggedUser objuser) {
		return userService.ValidateSignature(objuser);
	}

	
	@PostMapping("/PasswordpolicySave")
	public LSPasswordPolicy PasswordpolicySave(@RequestBody LSPasswordPolicy lspasswordpolicy)
	{
			if(lspasswordpolicy.getObjuser() != null) {
			
			LSuserMaster userClass = auditService.CheckUserPassWord(lspasswordpolicy.getObjuser());
			
			if(userClass.getObjResponse().getStatus()) {
				
				lspasswordpolicy.setLsusermaster(userClass);
				
				return userService.PasswordpolicySave(lspasswordpolicy);
			}
			else
			{
				lspasswordpolicy.getObjsilentaudit().setComments("Entered invalid username and password");
				map.put("objsilentaudit",lspasswordpolicy.getObjsilentaudit());
				map.put("objmanualaudit",lspasswordpolicy.getObjmanualaudit());
				map.put("objUser",lspasswordpolicy.getObjuser());
				auditService.AuditConfigurationrecord(map);
				lspasswordpolicy.setResponse(new Response());
				lspasswordpolicy.getResponse().setStatus(false);
				lspasswordpolicy.getResponse().setInformation("ID_VALIDATION");
				return lspasswordpolicy;
			}
			
		}
		return userService.PasswordpolicySave(lspasswordpolicy);
	}
	
	@PostMapping("/GetPasswordPolicy")
	public LSPasswordPolicy GetPasswordPolicy(@RequestBody LSPasswordPolicy lspasswordpolicy)
	{
		return userService.GetPasswordPolicy(lspasswordpolicy);
	}
	@PostMapping("/GetPasswordPolicySitewise")
	public LSPasswordPolicy GetPasswordPolicySitewise(@RequestBody LSPasswordPolicy objpwd) {

		
		return userService.GetPasswordPolicySitewise(objpwd);
		
	}
//	public LSPasswordPolicy GetPolicySitewise(LSPasswordPolicy objpwd) {
//		// TODO Auto-generated method stub 
//		return  userService.GetPasswordPolicySitewise(objpwd);
//	}
	@PostMapping("/Uploadprofilepic")
    public ProfilePicture Uploadprofilepic(@RequestParam("file") MultipartFile file, @RequestParam("usercode") Integer usercode, @RequestParam("date") Date currentdate) {
        
		ProfilePicture profilePicture = new ProfilePicture();
        try {
        	profilePicture = fileManipulationservice.addPhoto(usercode, file,currentdate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return profilePicture;
    }
	
	
	@PostMapping("/CloudUploadprofilepic")
    public CloudProfilePicture CloudUploadprofilepic(@RequestParam("file") MultipartFile file, @RequestParam("usercode") Integer usercode, @RequestParam("date") Date currentdate) {
        
		CloudProfilePicture profilePicture = new CloudProfilePicture();
        try {
        	profilePicture = cloudFileManipulationservice.addPhoto(usercode, file,currentdate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return profilePicture;
    }

	@PostMapping("/Getprofilepic")
    public ProfilePicture Getprofilepic(@RequestBody ProfilePicture profilePicture)
    {
		return fileManipulationservice.getPhoto(profilePicture.getId());
    }
	
	@PostMapping("/CloudGetprofilepic")
    public CloudProfilePicture CloudGetprofilepic(@RequestBody CloudProfilePicture profilePicture)
    {
		return cloudFileManipulationservice.getPhoto(profilePicture.getId());
    }
	
	@PostMapping("/DeleteProfilepic")
    public ProfilePicture DeleteProfilepic(@RequestBody ProfilePicture profilePicture)
    {
		fileManipulationservice.deletePhoto(profilePicture.getId(),profilePicture.getObjsilentaudit());
		return profilePicture;
    }
	
	@PostMapping("/CloudDeleteProfilepic")
    public CloudProfilePicture CloudDeleteProfilepic(@RequestBody CloudProfilePicture profilePicture)
    {
		cloudFileManipulationservice.deletePhoto(profilePicture.getId(),profilePicture.getObjsilentaudit());
		return profilePicture;
    }
	
	@RequestMapping(value = "profile/{fileid}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadlargeattachment(@PathVariable Integer fileid) throws IllegalStateException, IOException {
		ProfilePicture objprofile = fileManipulationservice.getPhoto(fileid);
		
		byte[] data = null;
		
		if(objprofile != null)
		{
			data = objprofile.getImage().getData();
		}
		else
		{
			data = StreamUtils.copyToByteArray(new ClassPathResource("images/userimg.jpg").getInputStream());
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		
	    HttpHeaders header = new HttpHeaders();
	    header.setContentType(MediaType.parseMediaType("image/png"));
	    header.set("Content-Disposition", "attachment; filename=gg.pdf");
	    
	    return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);
	}
	
	@RequestMapping(value = "Cloudprofile/{fileid}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> Clouddownloadlargeattachment(@PathVariable Integer fileid) throws IllegalStateException, IOException {
		CloudProfilePicture objprofile = cloudFileManipulationservice.getPhoto(fileid);
		
		byte[] data = null;
		
		if(objprofile != null)
		{
			data = objprofile.getImage().getData();
		}
		else
		{
			data = StreamUtils.copyToByteArray(new ClassPathResource("images/userimg.jpg").getInputStream());
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		
	    HttpHeaders header = new HttpHeaders();
	    header.setContentType(MediaType.parseMediaType("image/png"));
	    header.set("Content-Disposition", "attachment; filename=gg.pdf");
	    
	    return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);
	}
	
	@RequestMapping("/Getnotification")
	public Map<String, Object> Getnotification(@RequestBody LSuserMaster lsuserMaster)
	{
		return userService.Getnotification(lsuserMaster);
	}
	
	@RequestMapping("/Updatenotificationread")
	public Map<String, Object> Updatenotificationread(@RequestBody LSnotification lsnotification)
	{
		return userService.Updatenotificationread(lsnotification);
	}
	
	@RequestMapping("/GetnotificationonLazyload")
	public Map<String, Object> GetnotificationonLazyload(@RequestBody LSnotification lsnotification)
	{
		return userService.GetnotificationonLazyload(lsnotification);
	}
	
	@RequestMapping("/GetLatestnotification")
	public Map<String, Object> GetLatestnotification(@RequestBody LSnotification lsnotification)
	{
		return userService.GetLatestnotification(lsnotification);
	}
	
	@PostMapping("/UpdateUseraction")
	public LSuserActions UpdateUseraction(@RequestBody LSuserActions objuseractions) {
		return userService.UpdateUseraction(objuseractions);
	}
	
	@PostMapping("/UpdatefreshUseraction")
	public LSuserActions UpdatefreshUseraction(@RequestBody LSuserActions objuseractions) {
		return userService.UpdatefreshUseraction(objuseractions);
	}
	
	@GetMapping("/Loadtenantusergroups")
	public List<LSusergroup> Loadtenantusergroups(HttpServletRequest request) {
		return userService.Loadtenantusergroups();
	}
	
	@GetMapping("/Createcentraliseduser")
	public LScentralisedUsers Createcentraliseduser(@RequestBody LScentralisedUsers objctrluser)
	{
		return userService.Createcentraliseduser(objctrluser);
	}

	@PostMapping("/Usersendpasswormail")
	public LSuserMaster Usersendpasswormail(@RequestBody LSuserMaster objusermaster) throws MessagingException
	{
		return userService.Usersendpasswormail(objusermaster);
		
	}

	@GetMapping("/Getallcentraliseduser")
	public List<LScentralisedUsers> Getallcentraliseduser(@RequestBody LScentralisedUsers objctrluser)
	{
		return userService.Getallcentraliseduser(objctrluser);
	}
	
	@GetMapping("/Getcentraliseduserbyid")
	public LScentralisedUsers Getcentraliseduserbyid(@RequestBody LScentralisedUsers objctrluser) {
		return userService.Getcentraliseduserbyid(objctrluser);
	}
	
	@PostMapping("/GetUserslocal")
	public List<LSuserMaster> GetUserslocal(@RequestBody LSuserMaster objusermaster)
	{
		return userService.GetUserslocal(objusermaster);
	}
	
	@PostMapping("/getUserOnCode")
	public LSuserMaster getUserOnCode(@RequestBody LSuserMaster objuser)
	{
		return userService.getUserOnCode(objuser);
	}
}