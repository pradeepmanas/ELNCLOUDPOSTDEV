package com.agaram.eln.primary.service.usermanagement;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Env;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.config.AESEncryption;
import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.fetchmodel.getmasters.Listofallmaster;
import com.agaram.eln.primary.model.cfr.LSaudittrailconfiguration;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cfr.LSpreferences;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.material.Section;
import com.agaram.eln.primary.model.material.Unit;
import com.agaram.eln.primary.model.notification.Email;
import com.agaram.eln.primary.model.usermanagement.LSMultisites;
import com.agaram.eln.primary.model.usermanagement.LSMultiusergroup;
import com.agaram.eln.primary.model.usermanagement.LSPasswordPolicy;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSactiveUser;
import com.agaram.eln.primary.model.usermanagement.LScentralisedUsers;
import com.agaram.eln.primary.model.usermanagement.LSnotification;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserActions;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.model.usermanagement.LSusergroupedcolumns;
import com.agaram.eln.primary.model.usermanagement.LSusergrouprights;
import com.agaram.eln.primary.model.usermanagement.LSusergrouprightsmaster;
import com.agaram.eln.primary.model.usermanagement.LSusershowhidecolumns;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.model.usermanagement.Lsusersettings;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
import com.agaram.eln.primary.repository.material.ElnmaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialRepository;
import com.agaram.eln.primary.repository.material.SectionRepository;
import com.agaram.eln.primary.repository.material.UnitRepository;
import com.agaram.eln.primary.repository.protocol.LSProtocolMasterRepository;
import com.agaram.eln.primary.repository.protocol.LSlogilabprotocoldetailRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfileRepository;
import com.agaram.eln.primary.repository.usermanagement.LSMultisitesRepositery;
import com.agaram.eln.primary.repository.usermanagement.LSMultiusergroupRepositery;
import com.agaram.eln.primary.repository.usermanagement.LSPasswordPolicyRepository;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSactiveUserRepository;
import com.agaram.eln.primary.repository.usermanagement.LScentralisedUsersRepository;
import com.agaram.eln.primary.repository.usermanagement.LSnotificationRepository;
import com.agaram.eln.primary.repository.usermanagement.LSprojectmasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserActionsRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusergroupRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusergroupedcolumnsRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusergrouprightsRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusergrouprightsmasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusershowhidecolumnsRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusersteamRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;
import com.agaram.eln.primary.repository.usermanagement.LsusersettingsRepository;
import com.agaram.eln.primary.service.cfr.AuditService;
import com.agaram.eln.primary.service.material.MaterialCategoryService;
import com.agaram.eln.primary.service.notification.EmailService;
import com.agaram.eln.primary.service.protocol.Commonservice;
import com.aspose.pdf.internal.imaging.internal.Exceptions.IO.IOException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@EnableJpaRepositories(basePackageClasses = LSusergroupRepository.class)
public class UserService {

	@Autowired
	private com.agaram.eln.primary.repository.cfr.LSpreferencesRepository LSpreferencesRepository;

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

	@Autowired
	private LSMultiusergroupRepositery LSMultiusergroupRepositery;

	@Autowired
	private LsusersettingsRepository LsusersettingsRepository;

	@Autowired
	LSusergroupedcolumnsRepository lsusergroupedcolumnsRepository;

	@Autowired
	LSusershowhidecolumnsRepository lsusershowhidecolumnsRepository;

	@Autowired
	UnitRepository unitRepository;

	@Autowired
	SectionRepository sectionRepository;

	@Autowired
	MaterialRepository materialRepository;

	@Autowired
	private LSMultisitesRepositery LSMultisitesRepositery;

	@Autowired
	ElnmaterialRepository elnmaterialRepository;

	@Autowired
	private AuditService auditService;
	@Autowired
	Commonservice commonservice;

	@Autowired
	private LSProtocolMasterRepository LSProtocolMasterRepository;

	@Autowired
	private LSfileRepository LSfileRepository;

//	@Autowired
//	private FileService fileService;

	@Autowired
	private LSlogilabprotocoldetailRepository LSlogilabprotocoldetailRepository;

	@Autowired
	private MaterialCategoryService MaterialCategoryService;

	public LSusergroup InsertUpdateUserGroup(LSusergroup objusergroup) {
		if (lSusergroupRepository.findByusergroupnameIgnoreCaseAndLssitemaster(objusergroup.getUsergroupname(),
				objusergroup.getLssitemaster()) != null && objusergroup.getUsergroupcode() == null) {
			objusergroup.setResponse(new Response());
			objusergroup.getResponse().setStatus(false);
			objusergroup.getResponse().setInformation("ID_EXIST");

			return objusergroup;
		} else if (objusergroup.getUsergroupcode() != null && lSusergroupRepository
				.findByusergroupnameIgnoreCaseAndUsergroupcodeNotAndLssitemaster(objusergroup.getUsergroupname(),
						objusergroup.getUsergroupcode(), objusergroup.getLssitemaster()) != null) {
			objusergroup.setResponse(new Response());
			objusergroup.getResponse().setStatus(false);
			objusergroup.getResponse().setInformation("ID_EXIST");

			return objusergroup;
		}

		if (objusergroup.getUsergroupcode() != null) {
			LSusergroup objGroup = lSusergroupRepository.findOne(objusergroup.getUsergroupcode());
			objusergroup.setCreatedon(objGroup.getCreatedon());
			try {
				objusergroup.setModifiedon(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (objusergroup.getUsergroupcode() == null) {
			try {
				objusergroup.setCreatedon(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		lSusergroupRepository.save(objusergroup);
		objusergroup.setResponse(new Response());
		objusergroup.getResponse().setStatus(true);
		objusergroup.getResponse().setInformation("ID_SUCCESSMSG");

		return objusergroup;
	}

	public LSusergroup InsertUpdateUserGroupFromSDMS(LSusergroup objusergroup) {
		if (lSusergroupRepository.findByusergroupnameAndLssitemaster(objusergroup.getUsergroupname(),
				objusergroup.getLssitemaster()) != null && objusergroup.getUsergroupcode() == null) {
			objusergroup.setResponse(new Response());
			objusergroup.getResponse().setStatus(false);
			objusergroup.getResponse().setInformation("ID_EXIST");
			return objusergroup;
		}

		lSusergroupRepository.save(objusergroup);
		objusergroup.setResponse(new Response());
		objusergroup.getResponse().setStatus(true);
		objusergroup.getResponse().setInformation("ID_SUCCESSMSG");

		if (objusergroup.getObjsilentaudit() != null) {
			LScfttransaction lscfttransactionObj = new ObjectMapper().convertValue(objusergroup.getObjsilentaudit(),
					new TypeReference<LScfttransaction>() {
					});
//			LSuserMaster lsuserMasterObj = lsuserMasterRepository.findByusername(lscfttransactionObj.getLsuserMaster().getUsername());
			LSuserMaster lsuserMasterObj = lsuserMasterRepository.findByusercode(lscfttransactionObj.getLsuserMaster());
			objusergroup.getObjsilentaudit().setLsuserMaster(lsuserMasterObj.getUsercode());
			// objusergroup.getObjsilentaudit().setModuleName("UserManagement");
			// objusergroup.getObjsilentaudit().setComments("Insert/Update UserGroup
			// Successfully");
			// objusergroup.getObjsilentaudit().setActions("Insert/Update UserGroup");
			objusergroup.getObjsilentaudit().setTableName("LSusergroup");
			try {
				objusergroup.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(objusergroup.getObjsilentaudit());
		}
//		Manual Audit

		if (objusergroup.getObjuser() != null) {
			// LScfttransaction manualAudit=new LScfttransaction();
			Date date = new Date();
			if (objusergroup.getObjmanualaudit() != null) {
				// objusergroup.getObjmanualaudit().setComments("Insert Test Successfully");
				objusergroup.getObjmanualaudit().setComments(objusergroup.getObjuser().getComments());
				// manualAudit.setActions("Insert Test");
				// manualAudit.setSystemcoments("User Generated");
				objusergroup.getObjmanualaudit().setTableName("LStestmasterlocal");
				// manualAudit.setManipulatetype("Insert");
				objusergroup.getObjmanualaudit().setLsuserMaster(objusergroup.getLSuserMaster().getUsercode());
				objusergroup.getObjmanualaudit()
						.setLssitemaster(objusergroup.getLSuserMaster().getLssitemaster().getSitecode());
				objusergroup.getObjmanualaudit().setTransactiondate(date);
				try {
					objusergroup.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lscfttransactionRepository.save(objusergroup.getObjmanualaudit());
			}
		}
		return objusergroup;
	}

	public List<LSusergroup> GetUserGroup(LSuserMaster objusergroup) {

		return lSusergroupRepository.findByOrderByUsergroupcodeDesc();
	}

//	public LSusergroup ActDeactUserGroup(LSusergroup objusergroup) 
//	{
//		lSusergroupRepository.save(objusergroup);
//		return objusergroup;
//	}

	public List<LSuserMaster> GetUsers(LSuserMaster objusergroup) {

		if (objusergroup.getUsername().equalsIgnoreCase("Administrator")) {

//			return lsuserMasterRepository.findByUserretirestatusNotOrderByCreateddateDesc(1);
			return lsuserMasterRepository.findAllByOrderByCreateddateDesc();
		}

		List<LSMultisites> obj = LSMultisitesRepositery.findByLssiteMaster(objusergroup.getLssitemaster());
		List<Integer> usercode = obj.stream().map(LSMultisites::getUsercode).collect(Collectors.toList());

		if (objusergroup.getObjResponse() != null
				&& objusergroup.getObjResponse().getInformation().equals("UserMaster")) {
			return lsuserMasterRepository.findByUsercodeIn(usercode);
		} else {
			return lsuserMasterRepository.findByUsercodeInAndUserretirestatusNot(usercode, 1);
		}
//		return lsuserMasterRepository.findByLssitemasterOrderByCreateddateDesc(objusergroup.getLssitemaster());

//		return lsuserMasterRepository.findByUserretirestatusNotAndLssitemasterOrderByCreateddateDesc(1,
//				objusergroup.getLssitemaster());
	}

	public List<LSuserMaster> GetUsersregister(LSuserMaster objusergroup) {

//		if (objusergroup.getUsername().equalsIgnoreCase("Administrator")) {
//
////			return lsuserMasterRepository.findByUserretirestatusNotOrderByCreateddateDesc(1);
//			return lsuserMasterRepository.findAllByOrderByCreateddateDesc();
//		}

		List<LSMultisites> obj = LSMultisitesRepositery.findByLssiteMaster(objusergroup.getLssitemaster());
		List<Integer> usercode = obj.stream().map(LSMultisites::getUsercode).collect(Collectors.toList());
		return lsuserMasterRepository.findByUsercodeInAndUserretirestatusNot(usercode, 1);
//		return lsuserMasterRepository.findByLssitemasterOrderByCreateddateDesc(objusergroup.getLssitemaster());

//		return lsuserMasterRepository.findByUserretirestatusNotAndLssitemasterOrderByCreateddateDesc(1,
//				objusergroup.getLssitemaster());
	}

	public List<LSuserMaster> GetUsersOnsite(LSSiteMaster objclass) {
		if (objclass.getObjsilentaudit() != null) {
			objclass.getObjsilentaudit().setTableName("LSuserMaster");
			try {
				objclass.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			lscfttransactionRepository.save(objclass.getObjsilentaudit());
		}
		if (objclass.getSitecode() == 0) {
			return lsuserMasterRepository.findAllByOrderByCreateddateDesc();
		}
		List<LSMultisites> obj = LSMultisitesRepositery.findByLssiteMaster(objclass);
		List<Integer> usercode = obj.stream().map(LSMultisites::getUsercode).collect(Collectors.toList());
		return lsuserMasterRepository.findByUserretirestatusNotAndUsercodeInOrderByCreateddateDesc(1, usercode);
//		return lsuserMasterRepository.findByUserretirestatusNotAndLssitemasterOrderByCreateddateDesc(1, objclass);
	}

	@SuppressWarnings("unused")
	public LSuserMaster InsertUpdateUser(LSuserMaster objusermaster)
			throws MessagingException, JsonParseException, JsonMappingException, IOException {
		boolean isnewuser = false;

		if (objusermaster.getUsercode() == null) {
			isnewuser = true;
		} else {
			LSuserMaster obj1 = lsuserMasterRepository.findByusercode(objusermaster.getUsercode());
		}

		List<LSSiteMaster> siteobj = objusermaster.getLsmultisites().stream()
				.map(multisite -> multisite.getLssiteMaster()).collect(Collectors.toList());

		List<LSMultisites> userocedsites = LSMultisitesRepositery.findByLssiteMasterIn(siteobj);
		List<Integer> usercodes = userocedsites.stream().map(LSMultisites::getUsercode).collect(Collectors.toList());
//		if (objusermaster.getUsercode() == null
//				&& lsuserMasterRepository.findByLssitemasterAndUsernameIgnoreCase(objusermaster.getLssitemaster(),
//						objusermaster.getUsername()) != null) {
//		List<LSuserMaster> kumu=lsuserMasterRepository.findByUsernameIgnoreCaseAndUsercodeIn(objusermaster.getUsername(), usercodes);
		if (objusermaster.getUsercode() == null && lsuserMasterRepository
				.findByUsernameIgnoreCaseAndUsercodeIn(objusermaster.getUsername(), usercodes).size() > 0) {
			objusermaster.setResponse(new Response());
			objusermaster.getResponse().setStatus(false);
			objusermaster.getResponse().setInformation("ID_EXIST");

			return objusermaster;
		} else if (objusermaster.getUsercode() != null && objusermaster.getUserstatus() != null
				&& objusermaster.getLsusergroup() == null) {
			LSuserMaster updateUser = lsuserMasterRepository.findOne(objusermaster.getUsercode());
			updateUser.setUserstatus(objusermaster.getUserstatus().equals("Active") ? "A"
					: objusermaster.getUserstatus().equals("Deactive") ? "D" : "Locked");
//			objusermaster.setUserstatus(objusermaster.getUserstatus().equals("Active") || objusermaster.getUserstatus().equals("Locked") ? "A" : "D");
			if (!isnewuser && objusermaster.isReset()) {
				updateUser.setPassword(objusermaster.getPassword());
			}
			updateUser.setLockcount(objusermaster.getLockcount());
			updateUser
					.setUserretirestatus(objusermaster.getUserretirestatus() == 1 ? objusermaster.getUserretirestatus()
							: updateUser.getUserretirestatus());

			if (objusermaster.getMultiusergroupcode() != null && objusermaster.getUsercode() != null) {

				if (objusermaster.getLsmultisites().size() > 0) {
					LSMultisitesRepositery.deleteByusercode(objusermaster.getUsercode());
					LSMultisitesRepositery.save(objusermaster.getLsmultisites());
				}
				LSMultiusergroupRepositery.deleteByusercode(objusermaster.getUsercode());
				List<LSMultiusergroup> objects = objusermaster.getMultiusergroupcode();
				LSMultiusergroupRepositery.save(objects);
				updateUser.setMultiusergroupcode(objects);
				updateUser.setUserstatus(objusermaster.getUserstatus().equals("Active") ? "A"
						: objusermaster.getUserstatus().equals("Deactive") ? "D" : "Locked");
				updateUser.setUserfullname(objusermaster.getUserfullname());
				updateUser.setEmailid(objusermaster.getEmailid());
				updateUser.setUnifieduserid(objusermaster.getUnifieduserid());
				updateUser.setMultiusergroupcode(objusermaster.getMultiusergroupcode());
				updateUser.setDesignationname(objusermaster.getDesignationname());
				updateUser.setEdulevel(objusermaster.getEdulevel());
			}

			objusermaster.setCreateddate(updateUser.getCreateddate());
			try {
				updateUser.setModifieddate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			objusermaster.setUserstatus(objusermaster.getUserstatus().equals("Active") ? "A"
					: objusermaster.getUserstatus().equals("Deactive") ? "D" : "Locked");
			lsuserMasterRepository.save(updateUser);

			objusermaster.setResponse(new Response());
			objusermaster.getResponse().setStatus(true);
			objusermaster.getResponse().setInformation("ID_SUCCESSMSG");
			updatenotificationforuserrole(objusermaster);

			return objusermaster;
		}
		LSMultisitesRepositery.save(objusermaster.getLsmultisites());
		LSMultiusergroupRepositery.save(objusermaster.getMultiusergroupcode());
		lsuserMasterRepository.save(objusermaster);
		AtomicInteger siteCodeHolder = new AtomicInteger(0);
		objusermaster.getLsmultisites().stream().filter(items -> items.getDefaultsiteMaster() != null)
				.forEach(items -> {
					siteCodeHolder.set(items.getLssiteMaster().getSitecode());
				});
		Integer sitecode = siteCodeHolder.get();
		if (isnewuser) {
			String unifieduser = objusermaster.getUsername().toLowerCase().replaceAll("[^a-zA-Z0-9]", "") + "u"
					+ objusermaster.getUsercode() + "s" + sitecode + objusermaster.getUnifieduserid();

			objusermaster.setUnifieduserid(unifieduser);
			try {
				objusermaster.setCreateddate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LSSiteMaster obj = new LSSiteMaster();
			obj.setSitecode(sitecode);
			objusermaster.setLssitemaster(obj);
			lsuserMasterRepository.save(objusermaster);
		} else {
			LSuserMaster editobj = lsuserMasterRepository.findByusercode(objusermaster.getUsercode());
			if (editobj.getLssitemaster().getSitecode() != sitecode) {
				String unifieduser = objusermaster.getUsername().toLowerCase().replaceAll("[^a-zA-Z0-9]", "") + "u"
						+ objusermaster.getUsercode() + "s" + sitecode + objusermaster.getUnifieduserid();

				editobj.setUnifieduserid(unifieduser);
				LSSiteMaster obj = new LSSiteMaster();
				obj.setSitecode(sitecode);
				editobj.setLssitemaster(obj);
				lsuserMasterRepository.save(editobj);
				objusermaster.setUnifieduserid(unifieduser);
			}
		}

		objusermaster.setResponse(new Response());
		objusermaster.getResponse().setStatus(true);
		objusermaster.getResponse().setInformation("ID_SUCCESSMSG");

		Lsusersettings getUserPreference = new Lsusersettings();
		getUserPreference.setDFormat("Mon-DD-YYYY HH:mm:ss");
		getUserPreference.setUsercode(objusermaster.getUsercode());
		LsusersettingsRepository.save(getUserPreference);

		if (objusermaster.getUsercode() != null) {
			LSuserMaster obj1 = lsuserMasterRepository.findByusercode(objusermaster.getUsercode());
			if (obj1.getPasswordexpirydate() != null) {
				objusermaster.setPasswordexpirydate(obj1.getPasswordexpirydate());
			}

		}

		return objusermaster;
	}

	private void updatenotificationforuserrole(LSuserMaster objusermaster) {
		String Details = "";
		String Notifiction = "";

		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();

		LSuserMaster createby = lsuserMasterRepository.findByusercode(objusermaster.getLoggedinuser().getUsercode());
		if (objusermaster.getMultiusergroupcode() != null && objusermaster.getDeleterole() != null) {
//			if (objusermaster.getMultiusergroupcode().size() > objusermaster.getDeleterole().size()) {
			if (!objusermaster.getUsernotify().isEmpty() && objusermaster.getUsernotify() != null) {
				StringBuffer sb = new StringBuffer();
				for (LSuserMaster rowValues : objusermaster.getUsernotify()) {
					sb.append(rowValues.getUsergroupname()).append(",");
				}

				if (createby.getUsercode() != objusermaster.getUsercode()) {
					String Detailwithcomma = sb.toString();
					Details = "{\"role\":\"" + Detailwithcomma + "\", \"site\":\"" + objusermaster.getSitename()
							+ "\"}";
					Notifiction = "USERROLEADD";

					LSnotification objnotify = new LSnotification();
					objnotify.setNotifationfrom(objusermaster.getLoggedinuser());
					objnotify.setNotifationto(objusermaster);
					objnotify.setNotificationdate(objusermaster.getModifieddate());
					objnotify.setNotification(Notifiction);
					objnotify.setNotificationdetils(Details);
					objnotify.setNotificationfor(1);
					objnotify.setNotificationpath("/Usermaster");
					objnotify.setIsnewnotification(1);
					lstnotifications.add(objnotify);
					lsnotificationRepository.save(lstnotifications);
				}
			}
//			}
//			if (objusermaster.getMultiusergroupcode().size() != objusermaster.getDeleterole().size()) {
			if (!objusermaster.getUserroleremovenotify().isEmpty() && objusermaster.getUserroleremovenotify() != null) {
				StringBuffer sb = new StringBuffer();
				for (LSuserMaster rowValues : objusermaster.getUserroleremovenotify()) {
					sb.append(rowValues.getUsergroupname()).append(",");
				}
				if (createby.getUsercode() != objusermaster.getUsercode()) {
					String Detailwithcomma = sb.toString();
					Details = "{\"role\":\"" + Detailwithcomma + "\", \"site\":\"" + objusermaster.getSitename()
							+ "\", \"fromuser\":\"" + objusermaster.getLoggedinuser().getUsername() + "\"}";
					Notifiction = "USERROLEREMOVE";

					LSnotification objnotify = new LSnotification();
					objnotify.setNotifationfrom(objusermaster.getLoggedinuser());
					objnotify.setNotifationto(objusermaster);
					objnotify.setNotificationdate(objusermaster.getModifieddate());
					objnotify.setNotification(Notifiction);
					objnotify.setNotificationdetils(Details);
					objnotify.setNotificationfor(1);
					objnotify.setNotificationpath("/Usermaster");
					objnotify.setIsnewnotification(1);
					lstnotifications.add(objnotify);
					lsnotificationRepository.save(lstnotifications);
				}
			}
//			}
		}

	}

	private String Generatetenantpassword() {

		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*-_=+\',/?";
		String pwd = RandomStringUtils.random(15, characters);

		return pwd;
	}

	public LSuserMaster InsertUpdateUserfromSDMS(LSuserMaster objusermaster) {
		if (objusermaster.getUsercode() == null && lsuserMasterRepository
				.findByusernameAndLssitemaster(objusermaster.getUsername(), objusermaster.getLssitemaster()) != null) {

			objusermaster.setResponse(new Response());
			objusermaster.getResponse().setStatus(false);
			objusermaster.getResponse().setInformation("ID_EXIST");

			return objusermaster;
		}

		LScfttransaction lscfttransactionObj = new ObjectMapper().convertValue(objusermaster.getObjsilentaudit(),
				new TypeReference<LScfttransaction>() {
				});
//		LSuserMaster lsuserMasterObj = lsuserMasterRepository.findByusername(lscfttransactionObj.getLsuserMaster().getUsername());
		LSuserMaster lsuserMasterObj = lsuserMasterRepository.findByusercode(lscfttransactionObj.getLsuserMaster());
		LSusergroup LSusergroupObj = lSusergroupRepository
				.findByusergroupname(objusermaster.getLsusergroup().getUsergroupname());
		objusermaster.setLsusergroup(LSusergroupObj);
		objusermaster.getObjsilentaudit().setLsuserMaster(lsuserMasterObj.getUsercode());
		lsuserMasterRepository.save(objusermaster);

		if (objusermaster.getObjsilentaudit() != null) {
			// objusermaster.getObjsilentaudit().setModuleName("UserManagement");
			// objusermaster.getObjsilentaudit().setComments("Insert/Update User
			// Successfully");
			// objusermaster.getObjsilentaudit().setActions("Insert/Update User");
			// objusermaster.getObjsilentaudit().setSystemcoments("System Generated");
			objusermaster.getObjsilentaudit().setTableName("LSuserMaster");
			try {
				objusermaster.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(objusermaster.getObjsilentaudit());
		}
		// Manual Audit
		if (objusermaster.getObjuser() != null) {
			// LScfttransaction manualAudit=new LScfttransaction();
//			Date date = new Date();
			objusermaster.getObjmanualaudit().setComments(objusermaster.getObjuser().getComments());
			// manualAudit.setModuleName("UserManagement");
			// manualAudit.setComments("Insert Test Successfully");
			// manualAudit.setActions("Insert Test");
			// manualAudit.setSystemcoments("User Generated");
			objusermaster.getObjmanualaudit().setTableName("LStestmasterlocal");
			// manualAudit.setManipulatetype("Insert");
			// manualAudit.setLsuserMaster(objusermaster);
			objusermaster.getObjmanualaudit().setLsuserMaster(objusermaster.getUsercode());
			objusermaster.getObjmanualaudit().setLssitemaster(objusermaster.getLssitemaster().getSitecode());
//			objusermaster.getObjmanualaudit().setTransactiondate(date);
			try {
				objusermaster.getObjmanualaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(objusermaster.getObjmanualaudit());
		}
		objusermaster.setResponse(new Response());
		objusermaster.getResponse().setStatus(true);
		objusermaster.getResponse().setInformation("ID_SUCCESSMSG");

		return objusermaster;
	}

	public LSuserMaster ResetPassword(LSuserMaster objuser) {
		LSuserMaster updateUser = lsuserMasterRepository.findOne(objuser.getUsercode());
		updateUser.setPassword(objuser.getPassword());
		lsuserMasterRepository.save(updateUser);

		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setModuleName("UserManagement");
			objuser.getObjsilentaudit().setComments("Reset Password Successfully");
			objuser.getObjsilentaudit().setActions("Reset Password");
			objuser.getObjsilentaudit().setSystemcoments("System Generated");
			objuser.getObjsilentaudit().setTableName("LSuserMaster");
			try {
				objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}

		return objuser;
	}

	public LSusersteam InsertUpdateTeam(LSusersteam objteam) {
		if (objteam.getTeamcode() == null && lsusersteamRepository
				.findByTeamnameIgnoreCaseAndLssitemaster(objteam.getTeamname(), objteam.getLssitemaster())
				.size() != 0) {

			objteam.setResponse(new Response());
			objteam.getResponse().setStatus(false);
			objteam.getResponse().setInformation("ID_EXIST");
			if (objteam.getObjsilentaudit() != null) {
				objteam.getObjsilentaudit().setActions("Warning");
				objteam.getObjsilentaudit().setComments(
						objteam.getModifiedby().getUsername() + " " + "made attempt to create existing team name");
				objteam.getObjsilentaudit().setTableName("LSusersteam");

			}

			return objteam;
		} else if (objteam.getTeamcode() != null
				&& lsusersteamRepository.findByTeamnameIgnoreCaseAndTeamcodeNotAndLssitemaster(objteam.getTeamname(),
						objteam.getTeamcode(), objteam.getLssitemaster()).size() != 0) {
			objteam.setResponse(new Response());
			objteam.getResponse().setStatus(false);
			objteam.getResponse().setInformation("ID_EXIST");
			return objteam;

		} else if (objteam.getStatus() == -1) {
			List<LSprojectmaster> team = new ArrayList<LSprojectmaster>();
			team = LSprojectmasterRepository.findByLsusersteam(objteam);
//			List<LSprojectmaster> findByLsusersteam(LSusersteam lsusersteam);
			List<LSlogilablimsorderdetail> order = new ArrayList<LSlogilablimsorderdetail>();
			List<LSprojectmaster> projcode = new ArrayList<LSprojectmaster>();
			projcode = LSprojectmasterRepository.findByLsusersteam(objteam);
			if (projcode.size() > 0) {
				order = LSlogilablimsorderdetailRepository.findByOrderflagAndLsprojectmasterIn("N", projcode);
				if (team.get(0).getStatus() == 1) {
					objteam.setResponse(new Response());
					objteam.getResponse().setStatus(false);
					objteam.getResponse().setInformation("IDS_TEAMPROGRESS");
				} else if (order.size() > 0) {
					objteam.setResponse(new Response());
					objteam.getResponse().setStatus(false);
					objteam.getResponse().setInformation("IDS_TEAMINPROGRESS");
					if (objteam.getObjsilentaudit() != null) {
						objteam.getObjsilentaudit().setActions("Warning");
						objteam.getObjsilentaudit().setComments(objteam.getModifiedby().getUsername() + " "
								+ "made attempt to delete existing team associated with orders");
						objteam.getObjsilentaudit().setTableName("LSusersteam");
					}
					return objteam;
				} else {
					lsusersteamRepository.save(objteam);
					objteam.setResponse(new Response());
					objteam.getResponse().setStatus(true);
					objteam.getResponse().setInformation("ID_SUCCESSMSG");
				}
			} else {

				lsusersteamRepository.save(objteam);
				objteam.setResponse(new Response());
				objteam.getResponse().setStatus(true);
				objteam.getResponse().setInformation("ID_SUCCESSMSG");
			}
			if (objteam.getObjsilentaudit() != null) {
				objteam.getObjsilentaudit().setTableName("LSuserteam");
			}

			return objteam;
		}

		lsusersteamRepository.save(objteam);
		objteam.setResponse(new Response());
		objteam.getResponse().setStatus(true);
		objteam.getResponse().setInformation("ID_SUCCESSMSG");

		for (LSuserMaster objuser : objteam.getLsuserMaster()) {
			LSuserteammapping objmap = new LSuserteammapping();

			objmap.setLsuserMaster(objuser);
			objmap.setTeamcode(objteam.getTeamcode());
			lsuserteammappingRepository.save(objmap);
		}

		if (objteam.getObjsilentaudit() != null) {
			objteam.getObjsilentaudit().setTableName("LSuserteam");
		}
		updatenotificationforteam(objteam);

		return objteam;
	}

	@SuppressWarnings("unused")
	private void updatenotificationforteam(LSusersteam objteam) {
		String Details = "";
		String Notifiction = "";
		Notifiction = "TEAMCREATED";
		LSuserMaster createby = lsuserMasterRepository.findByusercode(objteam.getCreateby().getUsercode());
		LSnotification objnotify = new LSnotification();
		LSuserMaster lstusers = lsuserMasterRepository.findByusercode(objteam.getLsuserMaster().get(0).getUsercode());
		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
		for (int i = 0; i < objteam.getLsuserMaster().size(); i++) {
			// LSusersteam userteam =
			// lsusersteamRepository.findByusercode(objteam.getLsuserMaster().get(i).getUsercode());
			if (createby.getUsercode() != lstusers.getUsercode()) {

				Details = "{\"teamname\":\"" + objteam.getTeamname() + "\", \"team\":\"" + "\"}";

				objnotify.setNotifationfrom(objteam.getModifiedby());
				objnotify.setNotifationto(lstusers);
				objnotify.setNotificationdate(objteam.getModifieddate());
				objnotify.setNotification(Notifiction);
				objnotify.setNotificationdetils(Details);
				objnotify.setNotificationfor(1);
				objnotify.setNotificationpath("/Projectteam");
				objnotify.setNotificationfor(1);
				objnotify.setIsnewnotification(1);

			}
			lsnotificationRepository.save(objnotify);
		}
	}

	public List<LSusersteam> GetUserTeam(LSuserMaster LSuserMaster) {
		if (LSuserMaster.getUsername().equalsIgnoreCase("Administrator")) {
			return lsusersteamRepository.findBylssitemasterOrderByTeamcodeDesc(LSuserMaster.getLssitemaster());
		} else {
//			List<LSuserteammapping> teams = lsuserteammappingRepository.findBylsuserMaster(LSuserMaster);
//			return lsusersteamRepository.findByLsuserteammappingInAndStatus(teams, 1);
			return lsusersteamRepository.findBylssitemasterOrderByTeamcodeDesc(LSuserMaster.getLssitemaster());
			// return
			// lsusersteamRepository.findBylssitemasterAndStatus(LSuserMaster.getLssitemaster(),
			// 1);
		}
	}

	public List<LSusersteam> GetActiveUserTeam(LSuserMaster LSuserMaster) {
		return lsusersteamRepository.findBylssitemasterAndStatus(LSuserMaster.getLssitemaster(), 1);
	}

	public Map<String, Object> GetUserTeamonSitevise(LSSiteMaster objclass) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (objclass.getSitecode() == 0) {
			List<LSusersteam> obj = new ArrayList<LSusersteam>();
			obj = lsusersteamRepository.findBystatus(1);
			List<LSuserMaster> user = lsuserMasterRepository.findByusernameNot("Administrator");
			map.put("obj", obj);
			map.put("user", user);
			return map;
		}
		map.put("user", lsuserMasterRepository.findByUsernameNotAndLssitemaster("Administrator", objclass));
		map.put("obj", lsusersteamRepository.findBylssitemasterOrderByTeamcodeDesc(objclass));
		return map;
	}

	public Map<String, Object> GetUserRightsonGroup(LSusergroup lsusergroup) {

		Map<String, Object> maprights = new HashMap<String, Object>();

		LSpreferences objpref = LSpreferencesRepository.findByTasksettingsAndValuesettings("ELNparser", "0");

		if (lsusergroup.getUsergroupcode() == null) {
			List<LSusergrouprightsmaster> lstUserrightsmaster = lsusergrouprightsmasterRepository
					.findByOrderBySequenceorder();
			maprights.put("new", true);

			if (objpref != null) {

				List<LSusergrouprightsmaster> remLst = new ArrayList<LSusergrouprightsmaster>();

				lstUserrightsmaster.stream().filter(item -> item.getModulename().equalsIgnoreCase("Parser"))
						.forEach(item -> {
							item.operate();
							remLst.add(item);
						});

				lstUserrightsmaster.removeAll(remLst);

				maprights.put("rights", lstUserrightsmaster);
			} else {
				maprights.put("rights", lstUserrightsmaster);
			}
		} else {
			List<LSusergrouprights> lstUserrights = lsusergrouprightsRepository.findByUsergroupid(lsusergroup);
			List<LSusergrouprightsmaster> lstUserrightsmasterlst = lsusergrouprightsmasterRepository
					.findByOrderBySequenceorder();
			if (lstUserrights != null && lstUserrights.size() > 0 && lstUserrights.size() > 10) {
				maprights.put("new", false);

				if (objpref != null) {

					List<LSusergrouprightsmaster> remLst = new ArrayList<LSusergrouprightsmaster>();

					lstUserrightsmasterlst.stream().filter(item -> item.getModulename().equalsIgnoreCase("Parser"))
							.forEach(item -> {
								item.operate();
								remLst.add(item);
							});

					lstUserrightsmasterlst.removeAll(remLst);

					List<LSusergrouprights> remRightsLst = new ArrayList<LSusergrouprights>();

					lstUserrights.stream().filter(item -> item.getModulename().equalsIgnoreCase("Parser"))
							.forEach(item -> {
								item.operate();
								remRightsLst.add(item);
							});

					lstUserrights.removeAll(remRightsLst);

					maprights.put("rights", lstUserrights);
					maprights.put("masterrights", lstUserrightsmasterlst);
				} else {
					maprights.put("rights", lstUserrights);
					maprights.put("masterrights", lstUserrightsmasterlst);
				}

			} else {
				List<LSusergrouprightsmaster> lstUserrightsmaster = lsusergrouprightsmasterRepository
						.findByOrderBySequenceorder();
				maprights.put("new", true);
				if (objpref != null) {

					List<LSusergrouprightsmaster> remLst = new ArrayList<LSusergrouprightsmaster>();

					lstUserrightsmaster.stream().filter(item -> item.getModulename().equalsIgnoreCase("Parser"))
							.forEach(item -> {
								item.operate();
								remLst.add(item);
							});

					lstUserrightsmaster.removeAll(remLst);

					maprights.put("rights", lstUserrightsmaster);
				} else {
					maprights.put("rights", lstUserrightsmaster);
				}
			}
		}

		return maprights;
	}

	public Map<String, Object> GetUserRightsonUser(LSuserMaster objUser) {

		Map<String, Object> maprights = new HashMap<String, Object>();
		LSusergroup lsusergroup = objUser.getLsusergrouptrans();

		LSpreferences objpref = LSpreferencesRepository.findByTasksettingsAndValuesettings("ELNparser", "0");

		if (lsusergroup.getUsergroupcode() == null) {
			List<LSusergrouprightsmaster> lstUserrightsmaster = lsusergrouprightsmasterRepository.findAll();
			maprights.put("new", true);
			if (objpref != null) {

				List<LSusergrouprightsmaster> remLst = new ArrayList<LSusergrouprightsmaster>();

				lstUserrightsmaster.stream().filter(item -> item.getModulename().equalsIgnoreCase("Parser"))
						.forEach(item -> {
							item.operate();
							remLst.add(item);
						});

				lstUserrightsmaster.removeAll(remLst);

				maprights.put("rights", lstUserrightsmaster);
			} else {
				maprights.put("rights", lstUserrightsmaster);
			}
		} else {
			List<LSusergrouprights> lstUserrights = lsusergrouprightsRepository.getrightsonUsergroupid(lsusergroup);
			if (lstUserrights != null && lstUserrights.size() > 0) {
				maprights.put("new", false);
				if (objpref != null) {

					List<LSusergrouprights> remLst = new ArrayList<LSusergrouprights>();

					lstUserrights.stream().filter(item -> item.getModulename().equalsIgnoreCase("Parser"))
							.forEach(item -> {
								item.operate();
								remLst.add(item);
							});

					lstUserrights.removeAll(remLst);

					maprights.put("rights", lstUserrights);
				} else {
					maprights.put("rights", lstUserrights);
				}

			} else {
				List<LSusergrouprightsmaster> lstUserrightsmaster = lsusergrouprightsmasterRepository.findAll();
				maprights.put("new", true);

				if (objpref != null) {

					List<LSusergrouprightsmaster> remLst = new ArrayList<LSusergrouprightsmaster>();

					lstUserrightsmaster.stream().filter(item -> item.getModulename().equalsIgnoreCase("Parser"))
							.forEach(item -> {
								item.operate();
								remLst.add(item);
							});

					lstUserrightsmaster.removeAll(remLst);

					maprights.put("rights", lstUserrightsmaster);
				} else {
					maprights.put("rights", lstUserrightsmaster);
				}
			}
		}

		return maprights;
	}

	public List<LSusergrouprights> SaveUserRights(LSusergrouprights[] lsrites) {
		List<LSusergrouprights> lsrights = Arrays.asList(lsrites);
		try {
			updateNotificationForUserRoleRights(lsrights);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (lsrights.size() > 0 && lsrights.get(0).getOrderno() == null) {
			lsrights = lsrights.stream().map(obj -> {
				try {
					obj.setCreatedon(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return obj;
			}).collect(Collectors.toList());
		}
		if (lsrights.get(0).getUsergroupid() == null) {
			lsrights.get(0).setResponse(new Response());
			lsrights.get(0).getResponse().setStatus(false);
			lsrights.get(0).getResponse().setInformation("ID_USERRYTSERR");
			return lsrights;
		} else {

			lsusergrouprightsRepository.save(lsrights);

			lsrights.get(0).setResponse(new Response());
			lsrights.get(0).getResponse().setStatus(true);
			lsrights.get(0).getResponse().setInformation("ID_ALERT");
		}
		return lsrights;

	}

	private void updateNotificationForUserRoleRights(List<LSusergrouprights> objRights) throws ParseException {
		String Details = "";
		String Notifiction = "";
		LSnotification objnotify = new LSnotification();
		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();

		Details = "{\"role\":\"" + objRights.get(0).getUsergroupid().getUsergroupname() + "\", \"user\":\""
				+ objRights.get(0).getObjLoggedUser().getUsername() + "\"}";
		Notifiction = "USERRIGHTSADD";

		List<LSMultiusergroup> userobj = LSMultiusergroupRepositery
				.findBylsusergroup(objRights.get(0).getUsergroupid());

		List<Integer> objnotifyuser = userobj.stream().map(LSMultiusergroup::getUsercode).collect(Collectors.toList());
		List<LSuserMaster> objuser = lsuserMasterRepository.findByUsercodeInAndUserretirestatusNot(objnotifyuser, 1);
		try {
			for (int i = 0; i < objuser.size(); i++) {
				if (objuser.get(i).getUsercode() != objRights.get(0).getObjLoggedUser().getUsercode()) {
					objnotify.setNotifationfrom(objRights.get(0).getObjLoggedUser());
					objnotify.setNotifationto(objuser.get(i));
					objnotify.setNotificationdate(commonfunction.getCurrentUtcTime());
					objnotify.setNotification(Notifiction);
					objnotify.setNotificationdetils(Details);
					objnotify.setNotificationfor(1);
					objnotify.setNotificationpath("/Userrights");
					objnotify.setIsnewnotification(1);
					lstnotifications.add(objnotify);
					lsnotificationRepository.save(lstnotifications);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<LSactiveUser> GetActiveUsers(LSuserMaster objuser) {

		return lsactiveUserRepository.findAll();

	}

	public List<LSactiveUser> GetActiveUsersOnsitewise(LSSiteMaster objclass) {
		if (objclass.getObjsilentaudit() != null) {
			objclass.getObjsilentaudit().setTableName("LSuserMaster");
			try {
				objclass.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(objclass.getObjsilentaudit());
		}
		return lsactiveUserRepository.findBylssitemaster(objclass);
	}

	public List<LSusergroup> GetActiveUserGroup(LSuserMaster objusergroup) {

		if (objusergroup.getObjsilentaudit() != null) {
			objusergroup.getObjsilentaudit().setTableName("LSusergroup");
			try {
				objusergroup.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(objusergroup.getObjsilentaudit());
		}

		return lSusergroupRepository.findByusergroupstatusAndUsergroupnameNot("A", "Administrator");
	}

	public List<LSusergroup> GetSiteWiseUserGroup(LSSiteMaster Objclass) {

		if (Objclass.getObjsilentaudit() != null) {
			Objclass.getObjsilentaudit().setTableName("LSusergroup");
//			try {
//				Objclass.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			lscfttransactionRepository.save(Objclass.getObjsilentaudit());
		}
		if (Objclass.getSitecode() == 0) {
			return lSusergroupRepository.findByOrderByUsergroupcodeDesc();
		}

		return lSusergroupRepository
				.findBylssitemasterAndUsergroupnameNotOrderByUsergroupcodeDesc(Objclass.getSitecode(), "");
	}

	public List<LSusergroup> GetSiteWiseActiveUserGroup(LSSiteMaster Objclass) {

		if (Objclass.getObjsilentaudit() != null) {
			Objclass.getObjsilentaudit().setTableName("LSusergroup");
			try {
				Objclass.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(Objclass.getObjsilentaudit());
		}
		List<String> status = Arrays.asList("A", "Active");
		if (Objclass.getSitecode() == 0) {
			return lSusergroupRepository.findByUsergroupstatusInOrderByUsergroupcodeDesc(status);
		}

		List<LSusergroup> lstusergroup = new ArrayList<LSusergroup>();

		if (Objclass.getIsmultitenant() != null && Objclass.getIsmultitenant() == 2) {
			lstusergroup = lSusergroupRepository.findBylssitemasterAndUsergroupstatusInOrderByUsergroupcodeDesc(1,
					status);
		} else {
			lstusergroup = lSusergroupRepository
					.findBylssitemasterAndUsergroupstatusInOrderByUsergroupcodeDesc(Objclass.getSitecode(), status);
		}

		return lstusergroup;
	}

	public List<LSusersteam> GetLoggedUsersTeamSitewise(LSSiteMaster site , LSuserMaster userobj) {

		List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(userobj);
		List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
				site);
		
		return lstteam;
	}
	
	public List<LSusergroup> GetUserGroupSiteWise(LSSiteMaster Objclass) {

		if (Objclass.getObjsilentaudit() != null) {
			Objclass.getObjsilentaudit().setTableName("LSusergroup");
			try {
				Objclass.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(Objclass.getObjsilentaudit());
		}
		if (Objclass.getSitecode() == 0) {
			return lSusergroupRepository.findByusergroupnameNotOrderByUsergroupcodeDesc("Administrator");
		}
		return lSusergroupRepository
				.findBylssitemasterAndUsergroupnameNotOrderByUsergroupcodeDesc(Objclass.getSitecode(), "");
	}

	public LSuserMaster ValidateSignature(LoggedUser objuser) {
		LSuserMaster objExitinguser = new LSuserMaster();
		String username = objuser.getsUsername();
		LSSiteMaster objsite = LSSiteMasterRepository.findBysitecode(Integer.parseInt(objuser.getsSiteCode()));
		objExitinguser = lsuserMasterRepository.findByusernameAndLssitemaster(username, objsite);

		if (objExitinguser != null) {
			String Password = AESEncryption.decrypt(objExitinguser.getPassword());
			objExitinguser.setObjResponse(new Response());

			if (objuser.getIsmultitenant() == 1 || objuser.getIsmultitenant() == 2) {

				if (objuser.getLoggedfrom() != null && objuser.getLoggedfrom() == 1) {

					objExitinguser.getObjResponse().setInformation("Valid user");
					objExitinguser.getObjResponse().setStatus(true);
					return objExitinguser;
				}

				if (Password.equals(objuser.getsPassword()) && !objuser.getsComments().isEmpty()) {

					objExitinguser.getObjResponse().setStatus(true);

					LScfttransaction manualAudit = new LScfttransaction();

					manualAudit.setModuleName("IDS_SCN_SHEETORDERS");
					manualAudit.setComments(objuser.getsComments());
					manualAudit.setActions("IDS_TSK_ESIGN");
					manualAudit.setSystemcoments("User Generated");
					manualAudit.setTableName("Lsusermaster");
					manualAudit.setManipulatetype("view");
					manualAudit.setLsuserMaster(objExitinguser.getUsercode());
					manualAudit.setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
//					manualAudit.setTransactiondate(objuser.getLogindate());
					try {
						manualAudit.setTransactiondate(commonfunction.getCurrentUtcTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					lscfttransactionRepository.save(manualAudit);
				} else if (objuser.getsComments().isEmpty()) {
					objExitinguser.getObjResponse().setInformation("IDS_INVALIDCOMMENTS");
					objExitinguser.getObjResponse().setStatus(false);
				} else {
					objExitinguser.getObjResponse().setInformation("Invalid password");
					objExitinguser.getObjResponse().setStatus(false);
				}
			} else {
				if (Password.equals(objuser.getsPassword())) {
					objExitinguser.getObjResponse().setStatus(true);

					LScfttransaction manualAudit = new LScfttransaction();

					manualAudit.setModuleName("IDS_SCN_SHEETORDERS");
					manualAudit.setComments(objuser.getsComments());
					manualAudit.setActions("IDS_TSK_ESIGN");
					manualAudit.setSystemcoments("User Generated");
					manualAudit.setTableName("Lsusermaster");
					manualAudit.setManipulatetype("view");
					manualAudit.setLsuserMaster(objExitinguser.getUsercode());
					manualAudit.setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
//					manualAudit.setTransactiondate(objuser.getLogindate());
					try {
						manualAudit.setTransactiondate(commonfunction.getCurrentUtcTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					lscfttransactionRepository.save(manualAudit);
				} else {
					objExitinguser.getObjResponse().setInformation("Invalid password");
					objExitinguser.getObjResponse().setStatus(false);
				}
			}
		} else {
			objExitinguser = lsuserMasterRepository.findByusernameAndLssitemaster("Administrator", objsite);
			objExitinguser.setObjResponse(new Response());
			objExitinguser.getObjResponse().setInformation("User not exist");
			objExitinguser.getObjResponse().setStatus(false);
		}

		return objExitinguser;
	}

	public LSPasswordPolicy PasswordpolicySave(LSPasswordPolicy objpwd) {

		if (objpwd.getComplexpasswrd() == 0) {
			objpwd.setMincapitalchar(0);
			objpwd.setMinsmallchar(0);
			objpwd.setMinnumericchar(0);
			objpwd.setMinspecialchar(0);
		}

		lSpasswordpolicyRepository.save(objpwd);

		if (objpwd.getObjsilentaudit() != null) {
			objpwd.getObjsilentaudit().setTableName("LSPasswordPolicy");
			try {
				objpwd.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(objpwd.getObjsilentaudit());
		}
		if (objpwd.getObjuser() != null) {
			objpwd.getObjmanualaudit().setComments(objpwd.getObjuser().getComments());
			objpwd.getObjmanualaudit().setTableName("LSPasswordPolicy");
		}
		objpwd.setResponse(new Response());
		objpwd.getResponse().setStatus(true);
		objpwd.getResponse().setInformation("Changes saved successfully");
		return objpwd;

	}

	public LSPasswordPolicy GetPasswordPolicy(LSPasswordPolicy objpwd) {

		if (objpwd.getObjsilentaudit() != null) {
			objpwd.getObjsilentaudit().setTableName("LSPasswordPolicy");
		}
		LSPasswordPolicy policy = new LSPasswordPolicy();
		if (objpwd.getLssitemaster() != null) {
			policy = lSpasswordpolicyRepository.findTopByAndLssitemasterOrderByPolicycodeDesc(objpwd.getLssitemaster());
		} else {
			policy = lSpasswordpolicyRepository.findTopByOrderByPolicycodeAsc();
		}
		return policy;

	}

	public LSPasswordPolicy GetLoginPasswordPolicy(final int objuser) {

		LSSiteMaster sitelist = new LSSiteMaster();
		sitelist = LSSiteMasterRepository.findBysitecode(objuser);

		LSPasswordPolicy policy = new LSPasswordPolicy();
//		if (objuser.get != null) {
		policy = lSpasswordpolicyRepository.findTopByAndLssitemasterOrderByPolicycodeDesc(sitelist);
//		} else {
//			
		// policy = lSpasswordpolicyRepository.findTopByOrderByPolicycodeAsc();
		// policy =
		// lSpasswordpolicyRepository.findByLssitemaster(objuser.getSitecode());

		// }
		return policy;

	}

	public LSPasswordPolicy GetPasswordPolicySitewise(LSPasswordPolicy objpwd) {

		LSPasswordPolicy policy = lSpasswordpolicyRepository
				.findTopByAndLssitemasterOrderByPolicycodeDesc(objpwd.getLssitemaster());
		LSuserMaster user = lsuserMasterRepository.findByusercode(1);
		if (policy == null && objpwd.getLssitemaster() != null) {

			LSPasswordPolicy value = new LSPasswordPolicy();

			value.setComplexpasswrd(0);

			value.setMinpasswrdlength(4);
			value.setMaxpasswrdlength(10);
			value.setPasswordhistory(5);
			value.setPasswordexpiry(90);

			value.setMincapitalchar(0);
			value.setLockpolicy(5);
			value.setMinsmallchar(0);
			value.setMinnumericchar(0);
			value.setMinspecialchar(0);
			value.setIdletime(15);
			value.setIdletimeshowcheck(1);
			value.setLsusermaster(objpwd.getLsusermaster() != null ? objpwd.getLsusermaster() : user);
			value.setLssitemaster(lSpasswordpolicyRepository.findByLssitemaster(objpwd.getLssitemaster()) == null
					? objpwd.getLssitemaster()
					: null);

			lSpasswordpolicyRepository.save(value);
			return value;
		}

		return policy;
	}

	public Map<String, Object> Getnotification(LSuserMaster lsuserMaster) {
		Map<String, Object> objresmap = new HashMap<String, Object>();
		Integer notifyfor = lsuserMaster.getObjuser().getFiltertype();
		objresmap.put("newnotificationcount", lsnotificationRepository
				.countByNotifationtoAndIsnewnotificationAndNotificationforOrNotifationtoAndIsnewnotificationAndNotificationfor(
						lsuserMaster, 1, 1, lsuserMaster, 1, 2));
		objresmap.put("notification", lsnotificationRepository
				.findFirst20ByNotifationtoAndNotificationforOrderByNotificationcodeDesc(lsuserMaster, notifyfor));

		objresmap.put("mynotificationcount",
				lsnotificationRepository.countByNotifationtoAndIsnewnotificationAndNotificationfor(lsuserMaster, 1, 1));
		objresmap.put("teamnotificationcount",
				lsnotificationRepository.countByNotifationtoAndIsnewnotificationAndNotificationfor(lsuserMaster, 1, 2));

		return objresmap;
	}

	public Map<String, Object> Updatenotificationread(LSnotification lsnotification) {
		Map<String, Object> objresmap = new HashMap<String, Object>();

		lsnotificationRepository.updatenotificationstatus(lsnotification.getNotifationto(),
				lsnotification.getNotificationcode());

		lsnotification.setIsnewnotification(0);

		objresmap = Getnotification(lsnotification.getNotifationto());
		objresmap.put("currentnotify", lsnotification);

		return objresmap;
	}

	public Map<String, Object> GetnotificationonLazyload(LSnotification lsnotification) {
		Map<String, Object> objresmap = new HashMap<String, Object>();

		objresmap.put("notification", lsnotificationRepository
				.findFirst20ByNotifationtoAndNotificationcodeLessThanAndNotificationforOrderByNotificationcodeDesc(
						lsnotification.getNotifationto(), lsnotification.getNotificationcode(),
						lsnotification.getNotificationfor()));

		return objresmap;
	}

	public Map<String, Object> GetLatestnotification(LSnotification lsnotification) {
		Map<String, Object> objresmap = new HashMap<String, Object>();

		objresmap.put("newnotificationcount",
				lsnotificationRepository.countByNotifationtoAndIsnewnotificationAndNotificationcodeGreaterThan(
						lsnotification.getNotifationto(), 1, lsnotification.getNotificationcode()));
		objresmap.put("notification",
				lsnotificationRepository.findByNotifationtoAndNotificationcodeGreaterThanOrderByNotificationcodeDesc(
						lsnotification.getNotifationto(), lsnotification.getNotificationcode()));

		return objresmap;
	}

	public Map<String, Object> GetLatestnotificationcount(LSnotification lsnotification) {
		Map<String, Object> objresmap = new HashMap<String, Object>();

		objresmap.put("newnotificationcount", lsnotificationRepository
				.countByNotifationtoAndIsnewnotificationAndNotificationforOrNotifationtoAndIsnewnotificationAndNotificationfor(
						lsnotification.getNotifationto(), 1, 1, lsnotification.getNotifationto(), 1, 2));
		objresmap.put("mynotificationcount", lsnotificationRepository
				.countByNotifationtoAndIsnewnotificationAndNotificationfor(lsnotification.getNotifationto(), 1, 1));

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
		result = lSusergroupRepository.findAll();
		return result;
	}

	public LScentralisedUsers Createcentraliseduser(LScentralisedUsers objctrluser) {
		LScentralisedUsers objunifieduser = lscentralisedUsersRepository
				.findByUnifieduseridIgnoreCase(objctrluser.getUnifieduserid());
		if (objunifieduser == null || objunifieduser.getUnifieduserid() == null) {
			lscentralisedUsersRepository.save(objctrluser);
		} else if (objunifieduser != null && objunifieduser.getSitecode() != objctrluser.getSitecode()) {
			lscentralisedUsersRepository.updatesitecodeonLScentralisedUsers(objctrluser.getSitecode(),
					objctrluser.getUsercode());
		}
		return objctrluser;
	}

	@SuppressWarnings("unused")
	public LSuserMaster Usersendpasswormail(LSuserMaster objusermaster) throws MessagingException {

		if ((objusermaster.getIsmultitenant() == 2
				&& objusermaster.getObjResponse().getInformation() == "Forget password")
				|| objusermaster.getIsmultitenant() != null && objusermaster.getMultitenantusercount() != null
						&& (objusermaster.getIsmultitenant() == 1 || objusermaster.getIsmultitenant() == 2)) {
			String password = Generatetenantpassword();
			String passwordadmin = AESEncryption.encrypt(password);
			LSuserMaster lsuserMaster = new LSuserMaster();
			objusermaster.setPassword(passwordadmin);
			Email email = new Email();
			email.setMailto(objusermaster.getEmailid());
			LocalDate date = LocalDate.now();
			// Define a DateTimeFormatter with a specific pattern
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			// Format the date
			String formattedDate = date.format(formatter);
			Integer accounttype;
			Integer days = null;
			String Accounttype = null;
			if (objusermaster.getIsmultitenant().equals(2)) {
				accounttype = objusermaster.getLssitemaster().getAccouttype();
				// days print
				days = accounttype.toString().equals("3") ? 90 : 30;
				Accounttype = accounttype.toString().equals("3") ? "Academic"
						: accounttype.toString().equals("2") ? "Corporate" : "Personal";
			}
			if (objusermaster.getAutenticatefrom() != null
					&& (objusermaster.getAutenticatefrom() == 1 || objusermaster.getAutenticatefrom() == 2)
					&& objusermaster.getIsmultitenant() == 2) {

				email.setSubject("ELN LITE User Credentials");
				email.setMailcontent(

						"<div class=\"container\" style=\" width: 100%;max-width: 600px; margin: 0 auto;background-color: #ffffff;font-family: Arial, sans-serif;\">\r\n"
								+ "        <!-- Header -->\r\n"
								+ "        <div class=\"header\" style=\" padding: 20px; background-color: #ffffff; color: #ffffff;\">\r\n"
								+ "           <img src=\"cid:image1\" alt=\"Logo 1\" width=\"130px\"/>\r\n"
								+ "           <img src=\"cid:image2\" alt=\"Logo 2\" width=\"75px\" style=\"float: right;\"/>\r\n"
								+ "        </div>\r\n" + "        \r\n" + "        <!-- Content -->\r\n"
								+ "        <div class=\"content\" style=\" padding: 20px; background-color: #cce0fb;\">\r\n"
								+ "            <p><b>Dear Customer,</b></p>\r\n"
								+ "            <div class=\"content-text\" style=\"text-align: center;\">\r\n"
								+ "                <p class=\"text-align\" style=\"color: #000; font-size: 14px;\">Thanks for your interest in Logilab ELN LITE.</p>\r\n"
								+ "                <p style=\"font-size: 14px; color: #032c67;\"><b><a href=\""
								+ objusermaster.getUserloginlink()
								+ "\" style=\"color: #032c67; text-decoration: underline\"><u>Click here to visit Logilab ELN LITE Login page</u></a></b></p>\r\n"
								+ "<br /><p style=\"font-size: 14px; color: #000000;\">Registration successful. Your Logilab ELN lite trial "
								+ Accounttype
								+ " account has been activated successfully. The free trial will expire <b>" + days
								+ "</b> days from today on <b>" + formattedDate + "</b> </p><br />"
								+ "                <p style=\"color: #000;font-size: 13px;\"><b>Plase contact our support team by mail at <span style=\"color: #1d75f1;\">support@agaramtech.com</span> for any queries.</b></p>\r\n"
								+ "            </div>\r\n" + "            \r\n" + "            \r\n"
								+ "        </div>\r\n" + "        \r\n" + "        <!-- Footer -->\r\n"
								+ "        <div class=\"footer\" style=\"padding: 10px; background-color: #cce0fb; color: #000; text-align: center; font-size: 12px;\">\r\n"
								+ "            <img src=\"cid:image3\" alt=\"Logo 3\" width=\"125px\" style=\"float: left;margin-top: 10px;\"/>\r\n"
								+ "            <div>\r\n"
								+ "                <p>Copyrights &copy; 2024 - 2025 Agaram Technology Pvt Ltd.</p>\r\n"
								+ "                <p style=\"color: #032c67;\"><a href=\"https://www.agaramtech.com/about-us\" style=\"color: #032c67; text-decoration: underline;\"><u>About</u></a> | <a href=\"https://www.agaramtech.com/terms-and-conditions\" style=\"color: #032c67; text-decoration: underline;\"><u>Terms & Conditions</u></a> | <a href=\"https://www.logilabeln.com/contact\" style=\"color: #032c67; text-decoration: underline;\"><u>Contact Us</u></a></p>\r\n"
								+ "            </div>\r\n" + "        </div>\r\n" + "    </div>");

			} else if (objusermaster.getAutenticatefrom() != null && objusermaster.getAutenticatefrom() == 0
					&& objusermaster.getIsmultitenant() == 2
					&& objusermaster.getObjResponse().getInformation().equals("Forget password")) {
				email.setSubject("Reset your Logilab ELN lite Password");
				email.setMailcontent(
						"<div class=\"container\" style=\" width: 100%;max-width: 600px; margin: 0 auto;background-color: #ffffff;font-family: Arial, sans-serif;\">\r\n"
								+ "        <!-- Header -->\r\n"
								+ "        <div class=\"header\" style=\" padding: 20px; background-color: #ffffff; color: #ffffff;\">\r\n"
								+ "           <img src=\"cid:image1\" alt=\"Logo 1\" width=\"130px\"/>\r\n"
								+ "           <img src=\"cid:image2\" alt=\"Logo 2\" width=\"75px\" style=\"float: right;\"/>\r\n"
								+ "        </div>\r\n" + "        \r\n" + "        <!-- Content -->\r\n"
								+ "        <div class=\"content\" style=\" padding: 20px; background-color: #cce0fb;\">\r\n"
								+ "            <p><b>Dear Customer,</b></p>\r\n"
								+ "            <div class=\"content-text\" style=\"text-align: center;\">\r\n"
								+ "                <p class=\"text-align\" style=\"color: #000; font-size: 14px;\">We received your request to reset your Logilab ELN lite account password</p>\r\n"
								+ "                <p style=\"font-size: 14px; color: #032c67;\"><b><a href=\""
								+ objusermaster.getUserloginlink()
								+ "\" style=\"color: #032c67; text-decoration: underline\"><u>Click here to Reset your Password</u></a></b></p>\r\n"
								+ "                <ul style=\"list-style: none; margin-bottom: 25px;\">\r\n"
								+ "                    <li style=\"padding-bottom: 5px; color: #626367; font-size: 14px;\">1. After logging in, a new password generation screen will appear</li>\r\n"
								+ "                    <li style=\"color: #626367; font-size: 14px;\">2. Paste the temporary password and proceed.</li>\r\n"
								+ "                </ul>\r\n"
								+ "                <div class=\"text-box\" style=\"width: 265px;margin: auto;padding: 8px;background: #fff;border-radius: 6px;margin-bottom: 20px;display: inline-block;\"><b style=\"font-size: 12px;float: left;margin-right: 19px;text-align: left;color: #626367;\">Username</b><span style=\" color: #1d75f1; font-size: 13px;text-align: left;float: left;display: inline-block;\">"
								+ objusermaster.getUsername() + "</span></div>\r\n"
								+ "                <div class=\"text-box\" style=\"width: 265px;margin: auto;padding: 8px;background: #fff;border-radius: 6px;margin-bottom: 20px;display: inline-block;\"><b style=\"font-size: 12px;float: left;margin-right: 19px;text-align: left;color: #626367;\">Temporary Password</b><span style=\" color: #1d75f1; font-size: 13px;text-align: left;float: left;display: inline-block;\">"
								+ password + "</span></div>\r\n"
								+ "                <p style=\"font-size: 12px;color: #626367;\">Please note that organization's password policies might apply to your password</p>\r\n"
								+ "                <p style=\"color: #000;font-size: 13px;\"><b>Plase contact our support team by mail at <span style=\"color: #1d75f1;\">support@agaramtech.com</span> for any queries.</b></p>\r\n"
								+ "            </div>\r\n" + "            \r\n" + "            \r\n"
								+ "        </div>\r\n" + "        \r\n" + "        <!-- Footer -->\r\n"
								+ "        <div class=\"footer\" style=\"padding: 10px; background-color: #cce0fb; color: #000; text-align: center; font-size: 12px;\">\r\n"
								+ "            <img src=\"cid:image3\" alt=\"Logo 3\" width=\"125px\" style=\"float: left;margin-top: 10px;\"/>\r\n"
								+ "            <div>\r\n"
								+ "                <p>Copyrights &copy; 2024 - 2025 Agaram Technology Pvt Ltd.</p>\r\n"
								+ "                <p style=\"color: #032c67;\"><a href=\"https://www.agaramtech.com/about-us\" style=\"color: #032c67; text-decoration: underline;\"><u>About</u></a> | <a href=\"https://www.agaramtech.com/terms-and-conditions\" style=\"color: #032c67; text-decoration: underline;\"><u>Terms & Conditions</u></a> | <a href=\"https://www.logilabeln.com/contact\" style=\"color: #032c67; text-decoration: underline;\"><u>Contact Us</u></a></p>\r\n"
								+ "            </div>\r\n" + "        </div>\r\n" + "    </div>");
			} else if (objusermaster.getAutenticatefrom() != null && objusermaster.getAutenticatefrom() == 0
					&& objusermaster.getIsmultitenant() == 2) {
				email.setSubject("ELN LITE User Credentials");
				email.setMailcontent(
						"<div class=\"container\" style=\" width: 100%;max-width: 600px; margin: 0 auto;background-color: #ffffff;font-family: Arial, sans-serif;\">\r\n"
								+ "        <!-- Header -->\r\n"
								+ "        <div class=\"header\" style=\" padding: 20px; background-color: #ffffff; color: #ffffff;\">\r\n"
								+ "           <img src=\"cid:image1\" alt=\"Logo 1\" width=\"130px\"/>\r\n"
								+ "           <img src=\"cid:image2\" alt=\"Logo 2\" width=\"75px\" style=\"float: right;\"/>\r\n"
								+ "        </div>\r\n" + "        \r\n" + "        <!-- Content -->\r\n"
								+ "        <div class=\"content\" style=\" padding: 20px; background-color: #cce0fb;\">\r\n"
								+ "            <p><b>Dear Customer,</b></p>\r\n"
								+ "            <div class=\"content-text\" style=\"text-align: center;\">\r\n"
								+ "                <p class=\"text-align\" style=\"color: #000; font-size: 14px;\">Thanks for your interest in Logilab ELN LITE.</p>\r\n"
								+ "                <p style=\"color: #626367; font-size: 14px;\">we've set up an "
								+ Accounttype + " account for you. Kindly create a password.</p>\r\n"
								+ "                <p style=\"font-size: 14px; color: #032c67;\"><b><a href=\""
								+ objusermaster.getUserloginlink()
								+ "\" style=\"color: #032c67; text-decoration: underline\"><u>Click here to visit Logilab ELN LITE Login page</u></a></b></p>\r\n"
								+ "                <ul style=\"list-style: none; margin-bottom: 25px;\">\r\n"
								+ "                    <li style=\"padding-bottom: 5px; color: #626367; font-size: 14px;\">1. After logging in, a new password generation screen will appear</li>\r\n"
								+ "                    <li style=\"color: #626367; font-size: 14px;\">2. Paste the temporary password and proceed.</li>\r\n"
								+ "                </ul>\r\n"
								+ "                <div class=\"text-box\" style=\"width: 265px;margin: auto;padding: 8px;background: #fff;border-radius: 6px;margin-bottom: 20px;display: inline-block;\"><b style=\"font-size: 12px;float: left;margin-right: 19px;text-align: left;color: #626367;\">Username</b><span style=\" color: #1d75f1; font-size: 13px;text-align: left;float: left;display: inline-block;\">"
								+ objusermaster.getUsername() + "</span></div>\r\n"
								+ "                <div class=\"text-box\" style=\"width: 265px;margin: auto;padding: 8px;background: #fff;border-radius: 6px;margin-bottom: 20px;display: inline-block;\"><b style=\"font-size: 12px;float: left;margin-right: 19px;text-align: left;color: #626367;\">Temporary Password</b><span style=\" color: #1d75f1; font-size: 13px;text-align: left;float: left;display: inline-block;\">"
								+ password + "</span></div>\r\n"
								+ "                <p style=\"font-size: 12px;color: #626367;\">Please note that organization's password policies might apply to your password</p>\r\n"
								+ "                <p style=\"color: #000;font-size: 13px;\"><b>Plase contact our support team by mail at <span style=\"color: #1d75f1;\">support@agaramtech.com</span> for any queries.</b></p>\r\n"
								+ "            </div>\r\n" + "            \r\n" + "            \r\n"
								+ "        </div>\r\n" + "        \r\n" + "        <!-- Footer -->\r\n"
								+ "        <div class=\"footer\" style=\"padding: 10px; background-color: #cce0fb; color: #000; text-align: center; font-size: 12px;\">\r\n"
								+ "            <img src=\"cid:image3\" alt=\"Logo 3\" width=\"125px\" style=\"float: left;margin-top: 10px;\"/>\r\n"
								+ "            <div>\r\n"
								+ "                <p>Copyrights &copy; 2024 - 2025 Agaram Technology Pvt Ltd.</p>\r\n"
								+ "                <p style=\"color: #032c67;\"><a href=\"https://www.agaramtech.com/about-us\" style=\"color: #032c67; text-decoration: underline;\"><u>About</u></a> | <a href=\"https://www.agaramtech.com/terms-and-conditions\" style=\"color: #032c67; text-decoration: underline;\"><u>Terms & Conditions</u></a> | <a href=\"https://www.logilabeln.com/contact\" style=\"color: #032c67; text-decoration: underline;\"><u>Contact Us</u></a></p>\r\n"
								+ "            </div>\r\n" + "        </div>\r\n" + "    </div>");
			} else {
				email.setSubject("ELN User Credentials");
				email.setMailcontent(
						"<b>Dear Customer,</b><br><center><img src=\"cid:image\"  style =width:120px; height:100px border: 3px;'></center><br><br>"
								+ "<p><p>Thanks for your interest in Logilab ELN.</p>Please use below mentioned Username and Password for your Login in ELN Application.<br><br>"
								+ "Click the URL mentioned below to go to Logilab ELN Login page. <br><br>"
								+ "After entered the username and click the password field, new password generation screen will appear.<br><br>"
								+ "Paste the password in the Old Password Textbox, and then generate your new password.<br><br>"
								+ "<b style='margin-left: 76px;'>Username:</b>\t\t " + objusermaster.getUsername()
								+ "<br><br>" + "<b style='margin-left: 76px;'>Password &nbsp;:</b>\t\t" + password
								+ "<br><br>" + "<b style='margin-left: 76px;'><a href="
								+ objusermaster.getUserloginlink()
								+ ">Click here to Logilab ELN Login page</a></b><br><br>"
								+ "<p>If you have any queries, please contact our support by mail to info@agaramtech.com <br><br><br>"
								+ "Regards,</p>" + "<b>Agaram Technologies Private Limited</b><br><br>"
								+ "<img src=\"cid:seconimage\"  style ='width:120px; height:120px;border: 3px;'"
								+ "<br><br><p>T: +91 44 4208 2005</p><p>T: +91 44 42189406</p>"
								+ "W:<a href='https://www.agaramtech.com'>https://www.agaramtech.com</a></p>");
			}

			if (objusermaster.getIsmultitenant() == 2) {
				emailService.sendEmailelnLite(email);

				// mail copy
				if (objusermaster.getObjResponse().getInformation() != null
						&& objusermaster.getObjResponse().getInformation() == "User registered successfully") {

					Integer atype = objusermaster.getLssitemaster().getAccouttype();

					String Actype = atype.toString().equals("3") ? "Academic"
							: atype.toString().equals("2") ? "Corporate" : "Personal";

					email.setSubject("New Logilab ELN lite registration received");
					email.setMailcontent(

							"<div class=\"container\" style=\" width: 100%;max-width: 600px; margin: 0 auto;background-color: #ffffff;font-family: Arial, sans-serif;\">\r\n"
									+ "        <!-- Header -->\r\n"
									+ "        <div class=\"header\" style=\" padding: 20px; background-color: #ffffff; color: #ffffff;\">\r\n"
									+ "           <img src=\"cid:image1\" alt=\"Logo 1\" width=\"130px\"/>\r\n"
									+ "           <img src=\"cid:image2\" alt=\"Logo 2\" width=\"75px\" style=\"float: right;\"/>\r\n"
									+ "        </div>\r\n" + "        \r\n" + "        <!-- Content -->\r\n"
									+ " <div class=\"content\"> " + " <div class=\"content-text\">"
									+ "  <p style=\"color: #000; font-size: 14px;\">Name : "
									+ objusermaster.getUsername() + "</p>"
									+ "  <p style=\"color: #000; font-size: 14px;\">Company : "
									+ objusermaster.getLssitemaster().getOrganisationname() + "</p>"
									+ " <p style=\"color: #000; font-size: 14px;\">Plan type : " + Actype + " </p>"
									+ " <p style=\"color: #000; font-size: 14px;\">Email ID : "
									+ objusermaster.getEmailid() + "</p>"
									+ "<p style=\"color: #000; font-size: 14px;\">Designation : "
									+ objusermaster.getDesignationname() + "</p>"
									+ "<p style=\"color: #000; font-size: 14px;\">Country : "
									+ objusermaster.getLssitemaster().getCountry() + "</p>" + "</div>" + " </div>"
									+ "        <!-- Footer -->\r\n"
									+ "        <div class=\"footer\" style=\"padding: 10px; background-color: #cce0fb; color: #000; text-align: center; font-size: 12px;\">\r\n"
									+ "            <img src=\"cid:image3\" alt=\"Logo 3\" width=\"125px\" style=\"float: left;margin-top: 10px;\"/>\r\n"
									+ "            <div>\r\n"
									+ "                <p>Copyrights &copy; 2024 - 2025 Agaram Technology Pvt Ltd.</p>\r\n"
									+ "                <p style=\"color: #032c67;\"><a href=\"https://www.agaramtech.com/about-us\" style=\"color: #032c67; text-decoration: underline;\"><u>About</u></a> | <a href=\"https://www.agaramtech.com/terms-and-conditions\" style=\"color: #032c67; text-decoration: underline;\"><u>Terms & Conditions</u></a> | <a href=\"https://www.logilabeln.com/contact\" style=\"color: #032c67; text-decoration: underline;\"><u>Contact Us</u></a></p>\r\n"
									+ "            </div>\r\n" + "        </div>\r\n" + "    </div>");

					emailService.sendEmailelnLiteCopy(email);
				}
			} else {
				emailService.sendEmail(email);
			}

			if (objusermaster.getIsmultitenant() == 2) {
				lsuserMasterRepository.setpasswordandpasswordstatusandforgetstatusByusercode(
						objusermaster.getPassword(), objusermaster.getPasswordstatus(), objusermaster.getUsercode());
			} else {
				lsuserMasterRepository.setpasswordandpasswordstatusByusercode(objusermaster.getPassword(),
						objusermaster.getPasswordstatus(), objusermaster.getUsercode());
			}
		}

		return objusermaster;

	}

	public List<LScentralisedUsers> Getallcentraliseduser(LScentralisedUsers objctrluser) {
		return lscentralisedUsersRepository.findAll();
	}

	public LScentralisedUsers Getcentraliseduserbyid(LScentralisedUsers objctrluser) {
		return lscentralisedUsersRepository.findByUnifieduseridIgnoreCase(objctrluser.getUnifieduserid());
	}

	public List<LSuserMaster> GetUserslocal(LSuserMaster objusergroup) {
		if (objusergroup.getObjsilentaudit() != null) {
			objusergroup.getObjsilentaudit().setTableName("LSuserMaster");
			try {
				objusergroup.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(objusergroup.getObjsilentaudit());
		}
		if (objusergroup.getUsername() != null && objusergroup.getUsername().equalsIgnoreCase("Administrator")) {
			return lsuserMasterRepository.findAll();
		}
		return lsuserMasterRepository.findByUsernameNotAndLssitemaster("Administrator", objusergroup.getLssitemaster());
	}

	public LSuserMaster getUserOnCode(LSuserMaster objuser) {
		LSuserMaster objExitinguser = lsuserMasterRepository.findByusercode(objuser.getUsercode());

		String encryptionStr = objExitinguser.getPassword() + "_" + objExitinguser.getUsername()
				+ objExitinguser.getLssitemaster().getSitename();

		String encryptPassword = AESEncryption.encrypt(encryptionStr);

		objExitinguser.setEncryptedpassword(encryptPassword);

		return objExitinguser;
	}

	public Lsusersettings updateUserDateFormat(Lsusersettings objuser) {
		Lsusersettings getUserPreference = LsusersettingsRepository.findByUsercode(objuser.getUsercode());
		if (getUserPreference != null) {
			getUserPreference.setDFormat(objuser.getDFormat());
			LsusersettingsRepository.save(getUserPreference);
		} else {
			LsusersettingsRepository.save(objuser);
		}
		return objuser;
	}

	public List<LSuserMaster> GetAllActiveUsers(LSuserMaster objuser) {
		List<Integer> lstuser = new ArrayList<Integer>();
		lstuser.add(1);
		lstuser.add(objuser.getUsercode());

		return lsuserMasterRepository
				.findByLssitemasterAndUsercodeNotInAndUserstatusAndUserretirestatusAndUnifieduseridNotNullOrderByUsercodeDesc(
						objuser.getLssitemaster(), lstuser, "A", 0);
	}

	public Lsusersettings getUserPrefrences(LSuserMaster objuser) {
		return LsusersettingsRepository.findByUsercode(objuser.getUsercode());
	}

	public Map<String, Object> getMultiUserGroup(LSuserMaster objusermaster) {
		Map<String, Object> rtnobj = new HashMap<>();
		LSuserMaster usermaster = lsuserMasterRepository.findByusercode(objusermaster.getUsercode());
		List<LSuserMaster> userobj = Collections.singletonList(usermaster);
		List<LSMultiusergroup> filteredMultiusergroupcode = userobj.stream()
				.flatMap(items -> items.getMultiusergroupcode().stream().filter(values -> {
					boolean result = values.getLsusergroup().getLssitemaster()
							.equals(objusermaster.getLssitemaster().getSitecode());
					System.out.println("Result: " + result); // Add logging here
					return result;
				})).collect(Collectors.toList());

		rtnobj.put("lsmultiusergroup", filteredMultiusergroupcode);
		List<LSuserMaster> usermastersite = lsuserMasterRepository
				.findByUsernameIgnoreCaseAndUserretirestatusNot(objusermaster.getUsername(), 1);
		List<Integer> usercode = usermastersite.stream().map(LSuserMaster::getUsercode).collect(Collectors.toList());
		rtnobj.put("lsmultisites", LSMultisitesRepositery.findByusercodeIn(usercode));
		rtnobj.put("multiplenames", usermastersite.size() == 1 ? false : usermastersite.size() > 1 ? true : false);
		return rtnobj;
	}

	public LSusergroupedcolumns setGroupedcolumn(LSusergroupedcolumns objgroupped) {
		LSusergroupedcolumns objexist = getGroupedcolumn(objgroupped);
		if (objexist != null) {
			objgroupped.setUsergroupedcolcode(objexist.getUsergroupedcolcode());
		}
		return lsusergroupedcolumnsRepository.save(objgroupped);
	}

	public LSusergroupedcolumns getGroupedcolumn(LSusergroupedcolumns objgroupped) {
		return lsusergroupedcolumnsRepository.findFirstByUsercodeAndSitecodeAndGridname(objgroupped.getUsercode(),
				objgroupped.getSitecode(), objgroupped.getGridname());
	}

	public Boolean getUsersManinFrameLicenseStatus(LSSiteMaster objsite) {

		Boolean bool = true;

		LSpreferences objPrefrence = LSpreferencesRepository.findByTasksettingsAndValuesettings("MainFormUser",
				"Active");

		if (objPrefrence != null) {

			String dvalue = objPrefrence.getValueencrypted();

			String sConcurrentUsers = AESEncryption.decrypt(dvalue);

			sConcurrentUsers = sConcurrentUsers.replaceAll("\\s", "");

			int nConcurrentUsers = Integer.parseInt(sConcurrentUsers);

//			Long userCount = lsuserMasterRepository.countByusercodeNotAndUserretirestatusNotAndLssitemaster(1, 1,
//					objsite);
			Long userCount = lsuserMasterRepository.countByUserretirestatusNot(1);

			if (userCount < nConcurrentUsers) {

				bool = true;

			} else {
				bool = false;
			}
			return bool;
		}

		return bool;
	}

	public Boolean Notificationmarkallasread(LSuserMaster lsuserMaster) {
		if (lsuserMaster.getObjuser().getFiltertype().equals(1)) {
			lsnotificationRepository.updatereadallnotificationstatusforme(lsuserMaster);
		} else if (lsuserMaster.getObjuser().getFiltertype().equals(2)) {
			lsnotificationRepository.updatereadallnotificationstatusforteam(lsuserMaster);
		}
		return true;
	}

//	public Long getActiveUserCount(LSSiteMaster lsSiteMaster) {
//
//		List<LSactiveUser> lstActiveUser = lsactiveUserRepository.findAll();
//
//		return (long) lstActiveUser.size();
//	}

	public Map<String, Object> getActiveUserCount(Map<String, Object> objMap) {
		Map<String, Object> obj = new HashMap<>();

		List<LSactiveUser> lstActiveUser = lsactiveUserRepository.findAll();
		obj.put("activeUserCount", lstActiveUser.size());

		LSpreferences objPrefrence;
		int loginType = 2;
		objPrefrence = LSpreferencesRepository.findByTasksettingsAndValuesettings("ConCurrentUser", "Active");
		obj.put("logintype", "ConCurrentUser");
		if (objPrefrence == null) {
			objPrefrence = LSpreferencesRepository.findByTasksettingsAndValuesettings("MainFormUser", "Active");
			obj.put("logintype", "MainFormUser");
			loginType = 1;
		}
		if (objPrefrence != null) {
			String dvalue = objPrefrence.getValueencrypted();
			if (dvalue != null) {
				String sConcurrentUsers = AESEncryption.decrypt(dvalue);
				sConcurrentUsers = sConcurrentUsers.replaceAll("\\s", "");
				int userCount = Integer.parseInt(sConcurrentUsers);
				obj.put("ConCurrentUser", userCount);
				obj.put("licencetype", loginType);
			}
//			obj.put("licencetype", objPrefrence != null ? 2 : (LSpreferencesRepository.findByTasksettingsAndValuesettings("MainFormUser","Active") != null ? 1 : 0));
		} else {
			obj.put("licencetype", 0);
		}
		return obj;
	}

	public Listofallmaster InsertImportedlist(Listofallmaster listofallmaster) {

		if (listofallmaster.getScreenname().equalsIgnoreCase("usermaster")) {
			List<String> usermname = listofallmaster.getLsusermaster().stream().map(LSuserMaster::getUsername)
					.collect(Collectors.toList());
			List<LSuserMaster> lsusermaster = lsuserMasterRepository.findByusernameInAndLssitemaster(usermname,
					listofallmaster.getLssitemaster());
			if (lsusermaster.size() == 0) {
				for (LSuserMaster usermansterobj : listofallmaster.getLsusermaster()) {

					LSMultiusergroupRepositery.save(usermansterobj.getMultiusergroupcode());
					lsuserMasterRepository.save(usermansterobj);
					String unifieduser = usermansterobj.getUsername().toLowerCase().replaceAll("[^a-zA-Z0-9]", "") + "u"
							+ usermansterobj.getUsercode() + "s" + usermansterobj.getLssitemaster().getSitecode()
							+ usermansterobj.getUnifieduserid();

					usermansterobj.setUnifieduserid(unifieduser);
					lsuserMasterRepository.save(usermansterobj);
					Lsusersettings getUserPreference = new Lsusersettings();
					getUserPreference.setDFormat("Mon-DD-YYYY HH:mm:ss");
					getUserPreference.setUsercode(usermansterobj.getUsercode());
					LsusersettingsRepository.save(getUserPreference);

					if (usermansterobj.getUsercode() != null) {
						LSuserMaster obj1 = lsuserMasterRepository.findByusercode(usermansterobj.getUsercode());
						if (obj1.getPasswordexpirydate() != null) {
							usermansterobj.setPasswordexpirydate(obj1.getPasswordexpirydate());
						}
					}
				}
				listofallmaster.setObjResponse(new Response());
				listofallmaster.getObjResponse().setStatus(true);
				listofallmaster.getObjResponse().setInformation("IDS_MSG_SUCCESSMSG");

				return listofallmaster;
			} else {
				listofallmaster.setObjResponse(new Response());
				listofallmaster.getObjResponse().setStatus(false);
				listofallmaster.getObjResponse().setInformation("IDS_MSG_EXIST");
				return listofallmaster;
			}
		} else if (listofallmaster.getScreenname().equalsIgnoreCase("unitmaster")) {

			List<String> lstUnitname = listofallmaster.getUnit().stream().map(Unit::getSunitname)
					.collect(Collectors.toList());

			List<Unit> lstUnit = unitRepository.findBySunitnameInAndNstatusAndNsitecode(lstUnitname, 1,
					listofallmaster.getLssitemaster().getSitecode());

			if (lstUnit.isEmpty()) {

				List<Unit> objLst = listofallmaster.getUnit().stream().peek(f -> {
					try {
						f.setCreatedate(commonfunction.getCurrentUtcTime());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}).collect(Collectors.toList());

				unitRepository.save(objLst);
				listofallmaster.setUnit(listofallmaster.getUnit());
				listofallmaster.setObjResponse(new Response());
				listofallmaster.getObjResponse().setStatus(true);
				listofallmaster.getObjResponse().setInformation("IDS_MSG_SUCCESSMSG");

				return listofallmaster;
			} else {
				listofallmaster.setObjResponse(new Response());
				listofallmaster.getObjResponse().setStatus(false);
				listofallmaster.getObjResponse().setInformation("IDS_MSG_EXIST");

				return listofallmaster;
			}
		} else if (listofallmaster.getScreenname().equalsIgnoreCase("sectionmaster")) {

			List<String> lstSectionname = listofallmaster.getSection().stream().map(Section::getSsectionname)
					.collect(Collectors.toList());

			List<Section> lstSection = sectionRepository.findBySsectionnameInAndNstatusAndNsitecode(lstSectionname, 1,
					listofallmaster.getLssitemaster().getSitecode());

			if (lstSection.isEmpty()) {

				List<Section> objLst = listofallmaster.getSection().stream().peek(f -> {
					try {
						f.setCreatedate(commonfunction.getCurrentUtcTime());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}).collect(Collectors.toList());

				sectionRepository.save(objLst);
				listofallmaster.setSection(listofallmaster.getSection());
				listofallmaster.setObjResponse(new Response());
				listofallmaster.getObjResponse().setStatus(true);
				listofallmaster.getObjResponse().setInformation("IDS_MSG_SUCCESSMSG");

				return listofallmaster;
			} else {
				listofallmaster.setObjResponse(new Response());
				listofallmaster.getObjResponse().setStatus(false);
				listofallmaster.getObjResponse().setInformation("IDS_MSG_EXIST");

				return listofallmaster;
			}
		} else if (listofallmaster.getScreenname().equalsIgnoreCase("material")) {

//			List<String> lstMatName = listofallmaster.getMaterial().stream().map(Material::getSmaterialname)
//					.collect(Collectors.toList());

//			List<String> lstPrefixName = listofallmaster.getMaterial().stream().map(Material::getSprefix)
//					.collect(Collectors.toList());

//			List<Material> lstMaterialByName = materialRepository.findBySmaterialnameInAndAndNmaterialtypecodeAndNstatusAndNsitecode(lstMatName,listofallmaster.getMaterial().get(0).getNmaterialtypecode(), 1,
//					listofallmaster.getLssitemaster().getSitecode());

//			List<Material> lstMaterialByPrefix = materialRepository.findBySprefixInAndNstatusAndNsitecode(lstPrefixName, 1,
//					listofallmaster.getLssitemaster().getSitecode());

//			if (lstMaterialByName.isEmpty() && lstMaterialByPrefix.isEmpty()) {
//			if (lstMaterialByName.isEmpty()) {

			/**
			 * Start Old code
			 */
//			listofallmaster.getMaterial().stream().peek(f -> {
//				try {
//					f.setCreateddate(commonfunction.getCurrentUtcTime());
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//			}).collect(Collectors.toList());
//
//			materialRepository.save(listofallmaster.getMaterial());
//			listofallmaster.setMaterial(listofallmaster.getMaterial());
//			listofallmaster.setObjResponse(new Response());
//			listofallmaster.getObjResponse().setStatus(true);
//			listofallmaster.getObjResponse().setInformation("IDS_MSG_SUCCESSMSG");

			/**
			 * end Old code
			 */

			/**
			 * Start new code Added by sathishkumar chandrasekar, 28-08-2024 For import
			 * multiple materials(master record)
			 */

			listofallmaster.getElnmaterial().stream().peek(f -> {
				try {
					f.setCreateddate(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}).collect(Collectors.toList());

			elnmaterialRepository.save(listofallmaster.getElnmaterial());
			listofallmaster.setElnmaterial(listofallmaster.getElnmaterial());
			listofallmaster.setObjResponse(new Response());
			listofallmaster.getObjResponse().setStatus(true);
			listofallmaster.getObjResponse().setInformation("IDS_MSG_SUCCESSMSG");
			/**
			 * end New code
			 */

			return listofallmaster;
//			} else {
//				
//				if(!lstMaterialByName.isEmpty()) {
//					listofallmaster.setObjResponse(new Response());
//					listofallmaster.getObjResponse().setStatus(false);
//					listofallmaster.getObjResponse().setInformation("IDS_MSG_EXIST");
//				}else {
//					listofallmaster.setObjResponse(new Response());
//					listofallmaster.getObjResponse().setStatus(false);
//					listofallmaster.getObjResponse().setInformation("IDS_MSG_EXIST_PREFIX");
//				}
//
//				return listofallmaster;
//			}
		}
		return null;
	}

	public List<LSuserMaster> GetUsersonprojectbased(LSprojectmaster objusermaster) {
		LSprojectmaster obj = LSprojectmasterRepository.findByProjectcode(objusermaster.getProjectcode());
		List<LSuserteammapping> teamobj = lsuserteammappingRepository
				.findByteamcode(obj.getLsusersteam().getTeamcode());
		List<LSuserMaster> lsusermasterobj = new ArrayList<LSuserMaster>();
		if (teamobj != null && teamobj.size() > 0) {
			lsusermasterobj = teamobj.stream().map(LSuserteammapping::getLsuserMaster).collect(Collectors.toList());
		}

		return lsusermasterobj;
	}

	public List<LSuserMaster> GetUserslocalnonRetired(LSuserMaster objusermaster) {
		return lsuserMasterRepository.findByusernameNotAndUserretirestatusNot("Administrator", 1);
	}

	public Map<String, Object> GetSiteWiseUserGrouplist(LSuserMaster objclass) {
		Map<String, Object> retobj = new HashMap<>();
		List<LSSiteMaster> lsrights = Arrays.asList(objclass.getLstiteMaster());
		List<Integer> sitecode = lsrights.stream().map(LSSiteMaster::getSitecode).collect(Collectors.toList());
		boolean isnewuser = objclass.getUsercode() == null;
		boolean usernameExists = false;

		if (objclass.getUsername() != null) {
			for (LSSiteMaster tempobj : lsrights) {
				List<LSMultisites> userocedsites = LSMultisitesRepositery.findByLssiteMaster(tempobj);
				List<Integer> usercode = userocedsites.stream().map(LSMultisites::getUsercode)
						.collect(Collectors.toList());

				List<LSuserMaster> existingUsers;
				if (isnewuser) {
					existingUsers = lsuserMasterRepository.findByUsernameIgnoreCaseAndUsercodeIn(objclass.getUsername(),
							usercode);
				} else {
					existingUsers = lsuserMasterRepository.findByUsernameIgnoreCaseAndUsercodeInAndUsercodeNot(
							objclass.getUsername(), usercode, objclass.getUsercode());
				}

				if (!existingUsers.isEmpty()) {
					retobj.put("sitemaster", LSSiteMasterRepository.findBysitecode(tempobj.getSitecode()));
					retobj.put("username", objclass.getUsername());
//		            retobj.put("message", "username already exists");
					usernameExists = true;
					break;
				}
			}

			retobj.put("usernameExists", usernameExists);
		}
		if (!usernameExists) {
			retobj.put("listofusergroup", lSusergroupRepository
					.findBylssitemasterInAndUsergroupnameNotOrderByUsergroupcodeDesc(sitecode, ""));
		}

		return retobj;
	}

	public LSuserMaster getkumum(Map<String, Object> argObj) {
		LSuserMaster obj = new ObjectMapper().convertValue(argObj.get("usermaster"), new TypeReference<LSuserMaster>() {
		});
		return obj;
	}

	public Map<String, Object> getUsersinglOnCode(LSuserMaster objuser) {
		LSuserMaster objExitinguser = lsuserMasterRepository.findByusercode(objuser.getUsercode());
		Map<String, Object> obj = new HashMap<>();
//		String encryptionStr = objExitinguser.getPassword() + "_" + objExitinguser.getUsername()
//				+ objExitinguser.getLssitemaster().getSitename();
//
//		String encryptPassword = AESEncryption.encrypt(encryptionStr);
		String Password = AESEncryption.decrypt(objExitinguser.getPassword());

		objExitinguser.setEncryptedpassword(Password);
		objExitinguser.setLssitemaster(objuser.getLsmultisites().get(0).getLssiteMaster());
		objExitinguser.setLsusergroup(objuser.getLsusergroup());

		String groupstatus = objExitinguser.getLsusergroup().getUsergroupstatus();
		if (groupstatus.trim().equals("Active")) {
			if (objExitinguser.getLsusergroup() != null) {
				obj.put("userrights", GetUserRightsonGroup(objExitinguser.getLsusergroup()));
				LSaudittrailconfiguration objauditconfig = new LSaudittrailconfiguration();
				objauditconfig.setLsusermaster(objExitinguser);
				obj.put("auditconfig", auditService.GetAuditconfigUser(objauditconfig.getLsusermaster()));

			}
		}
		obj.put("objExitinguser", objExitinguser);
		return obj;
	}

	public List<LSusergroup> getmultiusergroup(LSuserMaster objuser) {
//		List<LSMultiusergroup> rtnobj=LSMultiusergroupRepositery.findByusercode(objuser.getUsercode());
		LSuserMaster usermaster = lsuserMasterRepository.findByusercode(objuser.getUsercode());
		List<LSuserMaster> userobj = Collections.singletonList(usermaster);
		List<LSusergroup> filteredMultiusergroupcode = userobj.stream()
				.flatMap(items -> items.getMultiusergroupcode().stream().peek(itemsva -> {
					if (itemsva.getDefaultusergroup() != null) {
						itemsva.getLsusergroup().setDefaultusergroup(itemsva.getDefaultusergroup());
					}
				}).filter(values ->
//			        values.getLsusergroup().getLssitemaster() == objuser.getLssitemaster().getSitecode())
				values.getLsusergroup().getLssitemaster().equals(objuser.getLssitemaster().getSitecode()))
						.map(LSMultiusergroup::getLsusergroup))
				.collect(Collectors.toList());
		return filteredMultiusergroupcode;

	}

	public Map<String, Object> GetTemplateStatus(Map<String, Object> obj) {

		String status = "";
		String screen = (String) obj.get("screen");
		String templatename = (String) obj.get("name");
		if (screen.equals("/protocols")) {
			status = LSProtocolMasterRepository.getRetirestatus(templatename);
		} else if (screen.equals("/sheetcreation")) {
			status = LSfileRepository.getRetirestatus(templatename);
		} else if (screen.equals("/registertask")) {
			status = LSlogilablimsorderdetailRepository.getRetirestatus(templatename);
		} else if (screen.equals("/Protocolorder")) {
			status = LSlogilabprotocoldetailRepository.getRetirestatus(templatename);
		}
		obj.put("status", status);
		return obj;
	}

	public Map<String, Object> updateProfileDetails(Map<String, Object> obj) {
		String status = "";
		String profile_name = (String) obj.get("userFullname");
		String destination_name = (String) obj.get("designationname");
		Integer usercode = (Integer) obj.get("usercode");
		if (profile_name != null) {
			lsuserMasterRepository.updateProfile(profile_name, usercode);
			status = "1";
		}
		if (destination_name != null) {
			lsuserMasterRepository.updateDestination(destination_name, usercode);
			status = "2";
		}
		obj.put("status", status);
		return obj;
	}

	public LSusershowhidecolumns setShowHideolumn(LSusershowhidecolumns objgroupped) {
		LSusershowhidecolumns objexist = getShowHideolumn(objgroupped);
		if (objexist != null) {
			objgroupped.setUsergroupedcolcode(objexist.getUsergroupedcolcode());
		}
		return lsusershowhidecolumnsRepository.save(objgroupped);
	}

	public LSusershowhidecolumns getShowHideolumn(LSusershowhidecolumns objgroupped) {
		return lsusershowhidecolumnsRepository.findFirstByUsercodeAndSitecodeAndGridname(objgroupped.getUsercode(),
				objgroupped.getSitecode(), objgroupped.getGridname());
	}

	public ResponseEntity<List<LSuserMaster>> InsertImportUserMaster(Map<String, Object> mapObjects)
			throws ParseException {
		ObjectMapper mapper = new ObjectMapper();
		Integer siteCode = Integer.parseInt((String) mapObjects.get("sitecode"));
		Integer ismultitenant = (Integer) mapObjects.get("ismultitenant");
		Date currentDate = commonfunction.getCurrentUtcTime();
		List<LSusergroup> userGroupList = mapper.convertValue(mapObjects.get("UserGroupList"),
				new TypeReference<List<LSusergroup>>() {
				});
		List<String> userGroupNameList = mapper.convertValue(mapObjects.get("UserGroupName"),
				new TypeReference<List<String>>() {
				});
		List<LSusergroup> existingUserGroups = lSusergroupRepository
				.findByLssitemasterAndUsergroupnameIgnoreCaseIn(siteCode, userGroupNameList);
		Map<String, LSusergroup> userGroupExistMap = existingUserGroups.stream()
				.collect(Collectors.toMap(LSusergroup::getUsergroupname, Function.identity()));
		userGroupList = userGroupList.stream().map(item -> {
			LSusergroup lsusergroup = userGroupExistMap.get(item.getUsergroupname());
			if (lsusergroup != null) {
				return lsusergroup;
			} else {
				item.setCreatedon(currentDate);
				item.setModifiedon(currentDate);
			}
			return item;
		}).collect(Collectors.toList());
		lSusergroupRepository.save(userGroupList); // kumu - user group Save
		List<String> userNameList = mapper.convertValue(mapObjects.get("UserMasterNameList"),
				new TypeReference<List<String>>() {
				});
		List<LSuserMaster> userMasterList = mapper.convertValue(mapObjects.get("UserMasterList"),
				new TypeReference<List<LSuserMaster>>() {
				});
		Map<String, LSusergroup> updatedUserGroupExistMap = userGroupList.stream()
				.collect(Collectors.toMap(LSusergroup::getUsergroupname, Function.identity()));
		List<LSuserMaster> usermasterlist = lsuserMasterRepository.findByusernameIn(userNameList);
		List<Integer> usercodelist = usermasterlist.stream().map(LSuserMaster::getUsercode)
				.collect(Collectors.toList());
		LSSiteMaster sitemaster = new LSSiteMaster();
		sitemaster.setSitecode(siteCode);
		List<Integer> multisitebasedusercode = LSMultisitesRepositery
				.findByusercodeInAndLssiteMaster(usercodelist, sitemaster).stream().map(LSMultisites::getUsercode)
				.collect(Collectors.toList());
		List<LSuserMaster> filteredUserMasterExistList = usermasterlist.stream()
				.filter(user -> multisitebasedusercode.contains(user.getUsercode())).collect(Collectors.toList());
		Map<String, LSuserMaster> userMasterExistList = filteredUserMasterExistList.stream()
				.collect(Collectors.toMap(LSuserMaster::getUsername, Function.identity()));
		List<LSMultiusergroup> multiuserGroupList = new ArrayList<>();
		List<LSMultisites> multisiteList = new ArrayList<>();
		for (LSuserMaster item : userMasterList) {
			LSuserMaster existUser = userMasterExistList.get(item.getUsername());
			if (existUser == null) {
				item.setCreateddate(currentDate);
				item.setModifieddate(currentDate);
				String unifiedUserId = createUnifiedUserId(item, siteCode);
				item.setUnifieduserid(unifiedUserId);
				String password = Generatetenantpassword();
				String passwordadmin = AESEncryption.encrypt(password);
				item.setPassword(passwordadmin);
				if (ismultitenant == 1) {
					item.setPasswordstatus(1);
				}
				LSusergroup existingGroup = updatedUserGroupExistMap.get(item.getUsergroupname());
				if (existingGroup != null) {
					LSMultiusergroup multiuserGroup = new LSMultiusergroup();
					multiuserGroup.setLsusergroup(existingGroup);
					multiuserGroup.setDefaultusergroup(1);
					multiuserGroupList.add(multiuserGroup);
					item.setMultiusergroupcode(Collections.singletonList(multiuserGroup));
				}
				multisiteList.addAll(item.getLsmultisites());
			} else {
				item = existUser;
			}
		}
		LSMultisitesRepositery.save(multisiteList);
		LSMultiusergroupRepositery.save(multiuserGroupList);
		lsuserMasterRepository.save(userMasterList);// kumu - user Master Save
		return new ResponseEntity<>(userMasterList, HttpStatus.OK);
	}

	private String createUnifiedUserId(LSuserMaster item, Integer siteCode) {
		return item.getUsername().toLowerCase().replaceAll("[^a-zA-Z0-9]", "") + "u" + item.getUsercode() + "s"
				+ siteCode + item.getUnifieduserid();
	}

	public List<LSuserMaster> sendEmailListWithBatch(Map<String, Object> objusermasterlist) {
		List<Email> emaillist = new ArrayList<>();
		List<LSuserMaster> objusermasterlistobj = new ObjectMapper().convertValue(objusermasterlist.get("UserList"),
				new TypeReference<List<LSuserMaster>>() {
				});
		for (LSuserMaster objusermaster : objusermasterlistobj) {
			if (objusermaster.getPassword() != null) {
				Email email = new Email();
				email.setMailto(objusermaster.getEmailid());
				email.setSubject("ELN User Credentials");
				email.setMailcontent(generateEmailContent(objusermaster, objusermaster.getPassword()));
				emaillist.add(email);
			}
		}
		if (emaillist.size() > 0) {
			sendEmailsInParallel(emaillist);
		}
		return objusermasterlistobj;
	}

	private String generateEmailContent(LSuserMaster objusermaster, String password) {
		return "<b>Dear Customer,</b><br><center><img src=\"cid:image\"  style='width:120px; height:100px; border: 3px;'></center><br><br>"
				+ "<p>Thanks for your interest in Logilab ELN.</p>Please use below mentioned Username and Password for your Login in ELN Application.<br><br>"
				+ "Click the URL mentioned below to go to Logilab ELN Login page. <br><br>"
				+ "After entered the username and click the password field, new password generation screen will appear.<br><br>"
				+ "Paste the password in the Old Password Textbox, and then generate your new password.<br><br>"
				+ "<b style='margin-left: 76px;'>Username:</b>\t\t " + objusermaster.getUsername() + "<br><br>"
				+ "<b style='margin-left: 76px;'>Password &nbsp;:</b>\t\t" + password + "<br><br>"
				+ "<b style='margin-left: 76px;'><a href=" + objusermaster.getUserloginlink()
				+ ">Click here to Logilab ELN Login page</a></b><br><br>"
				+ "<p>If you have any queries, please contact our support by mail to info@agaramtech.com <br><br><br>"
				+ "Regards,</p>" + "<b>Agaram Technologies Private Limited</b><br><br>"
				+ "<img src=\"cid:seconimage\"  style='width:120px; height:120px;border: 3px;'"
				+ "<br><br><p>T: +91 44 4208 2005</p><p>T: +91 44 42189406</p>"
				+ "W:<a href='https://www.agaramtech.com'>https://www.agaramtech.com</a></p>";
	}

	private void sendEmailsInParallel(List<Email> emaillist) {
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		List<Callable<Void>> tasks = new ArrayList<>();

		for (Email email : emaillist) {
			try {
				emailService.sendEmail(email);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}

		try {
			executorService.invokeAll(tasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executorService.shutdown();
		}
	}

	public List<LSusergroup> insertImportUserGroup(Map<String, Object> objusergroup) throws ParseException {
		Integer siteCode = Integer.parseInt((String) objusergroup.get("sitecode"));
		Date currentDate = commonfunction.getCurrentUtcTime();
		ObjectMapper mapper = new ObjectMapper();
		List<LSusergroup> userGroupList = mapper.convertValue(objusergroup.get("UserGroupList"),
				new TypeReference<List<LSusergroup>>() {
				});
		List<String> userGroupNameList = mapper.convertValue(objusergroup.get("UserGroupName"),
				new TypeReference<List<String>>() {
				});

		List<LSusergroup> existingUserGroups = lSusergroupRepository
				.findByLssitemasterAndUsergroupnameIgnoreCaseIn(siteCode, userGroupNameList);
		Map<String, LSusergroup> userGroupExistMap = existingUserGroups.stream()
				.collect(Collectors.toMap(LSusergroup::getUsergroupname, Function.identity()));
		userGroupList = userGroupList.stream().map(item -> {
			LSusergroup lsusergroup = userGroupExistMap.get(item.getUsergroupname());
			if (lsusergroup != null) {
				return lsusergroup;
			} else {
				item.setCreatedon(currentDate);
				item.setModifiedon(currentDate);
			}
			return item;
		}).collect(Collectors.toList());
		lSusergroupRepository.save(userGroupList); // kumu - user group Save

		return userGroupList;
	}
}