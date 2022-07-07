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

import com.agaram.eln.config.SMTPMailvalidation;
import com.agaram.eln.primary.model.cloudFileManip.CloudProfilePicture;
import com.agaram.eln.primary.model.cloudFileManip.CloudUserSignature;
import com.agaram.eln.primary.model.fileManipulation.ProfilePicture;
import com.agaram.eln.primary.model.fileManipulation.UserSignature;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSMultiusergroup;
import com.agaram.eln.primary.model.usermanagement.LSPasswordPolicy;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSactiveUser;
import com.agaram.eln.primary.model.usermanagement.LScentralisedUsers;
import com.agaram.eln.primary.model.usermanagement.LSnotification;
import com.agaram.eln.primary.model.usermanagement.LSuserActions;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.model.usermanagement.LSusergroupedcolumns;
import com.agaram.eln.primary.model.usermanagement.LSusergrouprights;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.model.usermanagement.Lsusersettings;
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
	public LSusergroup InsertUpdateUserGroup(@RequestBody LSusergroup objusergroup)throws Exception
	{
		return userService.InsertUpdateUserGroup(objusergroup);
	}
	
	@PostMapping("/InsertUpdateUserGroupFromSDMS")
	public LSusergroup InsertUpdateUserGroupFromSDMS(@RequestBody LSusergroup objusergroup)throws Exception
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
	public List<LSusergroup> GetUserGroup(@RequestBody LSuserMaster objusermaster)throws Exception
	{
	  return userService.GetUserGroup(objusermaster);
	}
	
	@PostMapping("/getMultiUserGroup")
	public List<LSMultiusergroup> getMultiUserGroup(@RequestBody LSuserMaster objusermaster)throws Exception
	{
	  return userService.getMultiUserGroup(objusermaster);
	}
	
	@PostMapping("/GetActiveUserGroup")
	public List<LSusergroup> GetActiveUserGroup(@RequestBody LSuserMaster objusermaster)throws Exception
	{
	  return userService.GetActiveUserGroup(objusermaster);
	}

	@PostMapping("/GetSiteWiseUserGroup")
	public List<LSusergroup> GetSiteWiseUserGroup(@RequestBody LSSiteMaster objclass)throws Exception
	{
	  return userService.GetSiteWiseUserGroup(objclass);
	}
	
	@PostMapping("/GetSiteWiseActiveUserGroup")
	public List<LSusergroup> GetSiteWiseActiveUserGroup(@RequestBody LSSiteMaster Objclass)throws Exception {
		return userService.GetSiteWiseActiveUserGroup(Objclass);
	}
	
	@PostMapping("/GetUserGroupSiteWise")
	public List<LSusergroup> GetUserGroupSiteWise(@RequestBody LSSiteMaster objclass)throws Exception
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
	public LSuserMaster InsertUpdateUser(@RequestBody LSuserMaster objusermaster) throws MessagingException {

		return userService.InsertUpdateUser(objusermaster);
	}
	
	@PostMapping("/InsertUpdateUserfromSDMS")
	public LSuserMaster InsertUpdateUserfromSDMS(@RequestBody LSuserMaster objusermaster)throws Exception
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
	public LSuserMaster ResetPassword(@RequestBody LSuserMaster objuser)throws Exception
	{
		return userService.ResetPassword(objuser);
	}
	
	@PostMapping("/GetUsers")
	public List<LSuserMaster> GetUsers(@RequestBody LSuserMaster objusermaster)throws Exception
	{
		return userService.GetUsers(objusermaster);
	}
	
	@PostMapping("/GetUsersOnsite")
	public List<LSuserMaster> GetUsersOnsite(@RequestBody LSSiteMaster objclass)throws Exception
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
	public LSusersteam InsertUpdateTeam(@RequestBody LSusersteam objteam)throws Exception
	{
		return userService.InsertUpdateTeam(objteam);
	}
	
	@PostMapping("/GetUserTeam")
	public List<LSusersteam> GetUserTeam(@RequestBody LSuserMaster LSuserMaster)throws Exception
	{
		return userService.GetUserTeam(LSuserMaster);
	}
	
	@PostMapping("/GetUserTeamonSitevise")
	public  Map<String,Object> GetUserTeamonSitevise(@RequestBody LSSiteMaster objclass)throws Exception
	{
		return userService.GetUserTeamonSitevise(objclass);
	}
	
	@PostMapping("/GetUserRightsonGroup")
	public Map<String, Object> GetUserRightsonGroup(@RequestBody LSusergroup lsusergroup)throws Exception
	{
		return userService.GetUserRightsonGroup(lsusergroup);
	}
	
	@PostMapping("/GetUserRightsonUser")
	public Map<String, Object> GetUserRightsonUser(@RequestBody LSuserMaster objUser)throws Exception
	{
		return userService.GetUserRightsonUser(objUser);
	}
	
	@PostMapping("/SaveUserRights")
	public List<LSusergrouprights> SaveUserRights(@RequestBody LSusergrouprights[] lsrights)throws Exception
	{
		return userService.SaveUserRights(lsrights);
	}
	
	@PostMapping("/GetActiveUsers")
	public List<LSactiveUser> GetActiveUsers(@RequestBody LSuserMaster lsuserMaster)throws Exception
	{
		return userService.GetActiveUsers(lsuserMaster);
	}
	
	@PostMapping("/GetActiveUsersOnsitewise")
	public List<LSactiveUser> GetActiveUsersOnsitewise(@RequestBody LSSiteMaster objclass)throws Exception
	{
		return userService.GetActiveUsersOnsitewise(objclass);
	}
	
	@PostMapping("/ValidateSignature")
	public LSuserMaster ValidateSignature(@RequestBody LoggedUser objuser)throws Exception {
		return userService.ValidateSignature(objuser);
	}

	
	@PostMapping("/PasswordpolicySave")
	public LSPasswordPolicy PasswordpolicySave(@RequestBody LSPasswordPolicy lspasswordpolicy)throws Exception
	{
		return userService.PasswordpolicySave(lspasswordpolicy);
	}
	
	@PostMapping("/GetPasswordPolicy")
	public LSPasswordPolicy GetPasswordPolicy(@RequestBody LSPasswordPolicy lspasswordpolicy)throws Exception
	{
		return userService.GetPasswordPolicy(lspasswordpolicy);
	}
	
	@PostMapping("/GetPasswordPolicySitewise")
	public LSPasswordPolicy GetPasswordPolicySitewise(@RequestBody LSPasswordPolicy objpwd)throws Exception {

		
		return userService.GetPasswordPolicySitewise(objpwd);
		
	}
//	public LSPasswordPolicy GetPolicySitewise(LSPasswordPolicy objpwd) {
//		// TODO Auto-generated method stub 
//		return  userService.GetPasswordPolicySitewise(objpwd);
//	}
	@PostMapping("/Uploadprofilepic")
    public ProfilePicture Uploadprofilepic(@RequestParam("file") MultipartFile file,
    		@RequestParam("usercode") Integer usercode, @RequestParam("date") Date currentdate)throws Exception {
        
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
    public CloudProfilePicture CloudUploadprofilepic(@RequestParam("file") MultipartFile file,
    		@RequestParam("usercode") Integer usercode, @RequestParam("date") Date currentdate)throws Exception {
        
		CloudProfilePicture profilePicture = new CloudProfilePicture();
        try {
        	profilePicture = cloudFileManipulationservice.addPhoto(usercode, file,currentdate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return profilePicture;
    }
	
	@PostMapping("/CloudUploadusersignature")
    public CloudUserSignature CloudUploadusersignature(@RequestParam("file") MultipartFile file, @RequestParam("usercode") Integer usercode
    		, @RequestParam("username") String username , @RequestParam("date") Date currentdate)throws Exception {
        
		CloudUserSignature usersignature = new CloudUserSignature();
        try {
        	usersignature = cloudFileManipulationservice.addusersignature(usercode, username, file,currentdate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return usersignature;
    }


	@PostMapping("/Getprofilepic")
    public ProfilePicture Getprofilepic(@RequestBody ProfilePicture profilePicture)throws Exception
    {
		return fileManipulationservice.getPhoto(profilePicture.getId());
    }
	
	@PostMapping("/CloudGetprofilepic")
    public CloudProfilePicture CloudGetprofilepic(@RequestBody CloudProfilePicture profilePicture)throws Exception
    {
		return cloudFileManipulationservice.getPhoto(profilePicture.getId());
    }
	
	@PostMapping("/DeleteProfilepic")
    public ProfilePicture DeleteProfilepic(@RequestBody ProfilePicture profilePicture)throws Exception
    {
		fileManipulationservice.deletePhoto(profilePicture.getId(),profilePicture.getObjsilentaudit());
		return profilePicture;
    }
	
	@PostMapping("/CloudDeleteProfilepic")
    public CloudProfilePicture CloudDeleteProfilepic(@RequestBody CloudProfilePicture profilePicture)throws Exception
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
	
	@RequestMapping(value = "Cloudsignatur/{fileid}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> Cloudsignatur(@PathVariable Integer fileid) throws IllegalStateException, IOException {
		CloudUserSignature objsignature = cloudFileManipulationservice.getSignature(fileid);
		
		byte[] data = null;
		
		if(objsignature != null)
		{
			data = objsignature.getImage().getData();
		}
		else
		{
			data = StreamUtils.copyToByteArray(new ClassPathResource("images/nosignature.png").getInputStream());
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		
	    HttpHeaders header = new HttpHeaders();
	    header.setContentType(MediaType.parseMediaType("image/png"));
	    header.set("Content-Disposition", "attachment; filename=gg.pdf");
	    
	    return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);
	}
	
	@RequestMapping(value = "ELNSignature/{fileid}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> ELNSignature(@PathVariable Integer fileid) throws IllegalStateException, IOException {
		UserSignature objprofile = fileManipulationservice.getsignature(fileid);
		
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
	
	@PostMapping("/UploadELNUserSignature")
    public UserSignature UploadELNUserSignature1(@RequestParam("file") MultipartFile file,
    		@RequestParam("usercode") Integer usercode, @RequestParam("date") Date currentdate)throws Exception {
        
		UserSignature UserSignature = new UserSignature();
        try {
        	UserSignature = fileManipulationservice.addsignature(usercode, file,currentdate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return UserSignature;
    }
	
	@RequestMapping("/Getnotification")
	public Map<String, Object> Getnotification(@RequestBody LSuserMaster lsuserMaster)throws Exception
	{
		return userService.Getnotification(lsuserMaster);
	}
	
	@RequestMapping("/Updatenotificationread")
	public Map<String, Object> Updatenotificationread(@RequestBody LSnotification lsnotification)throws Exception
	{
		return userService.Updatenotificationread(lsnotification);
	}
	
	@RequestMapping("/GetnotificationonLazyload")
	public Map<String, Object> GetnotificationonLazyload(@RequestBody LSnotification lsnotification)throws Exception
	{
		return userService.GetnotificationonLazyload(lsnotification);
	}
	
	@RequestMapping("/GetLatestnotification")
	public Map<String, Object> GetLatestnotification(@RequestBody LSnotification lsnotification)throws Exception
	{
		return userService.GetLatestnotification(lsnotification);
	}
	
	@RequestMapping("/GetLatestnotificationcount")
	public Map<String, Object> GetLatestnotificationcount(@RequestBody LSnotification lsnotification)throws Exception
	{
		return userService.GetLatestnotificationcount(lsnotification);
	}
	
	@PostMapping("/UpdateUseraction")
	public LSuserActions UpdateUseraction(@RequestBody LSuserActions objuseractions)throws Exception {
		return userService.UpdateUseraction(objuseractions);
	}
	
	@PostMapping("/UpdatefreshUseraction")
	public LSuserActions UpdatefreshUseraction(@RequestBody LSuserActions objuseractions)throws Exception {
		return userService.UpdatefreshUseraction(objuseractions);
	}
	
	@GetMapping("/Loadtenantusergroups")
	public List<LSusergroup> Loadtenantusergroups(HttpServletRequest request)throws Exception {
		return userService.Loadtenantusergroups();
	}
	
	@GetMapping("/Createcentraliseduser")
	public LScentralisedUsers Createcentraliseduser(@RequestBody LScentralisedUsers objctrluser)throws Exception
	{
		return userService.Createcentraliseduser(objctrluser);
	}

	@PostMapping("/Usersendpasswormail")
	public LSuserMaster Usersendpasswormail(@RequestBody LSuserMaster objusermaster) throws MessagingException
	{
		return userService.Usersendpasswormail(objusermaster);
		
	}

	@GetMapping("/Getallcentraliseduser")
	public List<LScentralisedUsers> Getallcentraliseduser(@RequestBody LScentralisedUsers objctrluser)throws Exception
	{
		return userService.Getallcentraliseduser(objctrluser);
	}
	
	@GetMapping("/Getcentraliseduserbyid")
	public LScentralisedUsers Getcentraliseduserbyid(@RequestBody LScentralisedUsers objctrluser)throws Exception {
		return userService.Getcentraliseduserbyid(objctrluser);
	}
	
	@PostMapping("/GetUserslocal")
	public List<LSuserMaster> GetUserslocal(@RequestBody LSuserMaster objusermaster)throws Exception
	{
		return userService.GetUserslocal(objusermaster);
	}
	
	@PostMapping("/getUserOnCode")
	public LSuserMaster getUserOnCode(@RequestBody LSuserMaster objuser)throws Exception
	{
		return userService.getUserOnCode(objuser);
	}
	
	@PostMapping("/validatemailaddress")
	public Response validatemailaddress(@RequestBody String mailaddress)throws Exception
	{
		Response objresponse =  new Response();
		objresponse.setStatus(SMTPMailvalidation.isAddressValid(mailaddress));
		return objresponse;
	}
	
	@PostMapping("/updateUserDateFormat")
	public Lsusersettings updateUserDateFormat(@RequestBody Lsusersettings objuser)throws Exception
	{
		return userService.updateUserDateFormat(objuser);
	}
	
	@PostMapping("/GetAllActiveUsers")
	public List<LSuserMaster> GetAllActiveUsers(@RequestBody LSuserMaster objusergroup)throws Exception
	{
		return userService.GetAllActiveUsers(objusergroup);
	}
	
	@PostMapping("/getUserPrefrences")
	public Lsusersettings getUserPrefrences(@RequestBody LSuserMaster objuser)throws Exception
	{
		return userService.getUserPrefrences(objuser);
	}
	
	@PostMapping("/setGroupedcolumn")
	public LSusergroupedcolumns setGroupedcolumn(@RequestBody LSusergroupedcolumns objgroupped)throws Exception
	{
		return userService.setGroupedcolumn(objgroupped);
	}
	
	@PostMapping("/getGroupedcolumn")
	public LSusergroupedcolumns getGroupedcolumn(@RequestBody LSusergroupedcolumns objgroupped)throws Exception
	{
		return userService.getGroupedcolumn(objgroupped);
	}
	
	@PostMapping("/getUsersManinFormLicenseStatus")
	public Boolean getUsersManinFrameLicenseStatus(@RequestBody LSSiteMaster objsite)throws Exception
	{
		return userService.getUsersManinFrameLicenseStatus(objsite);
	}
}

