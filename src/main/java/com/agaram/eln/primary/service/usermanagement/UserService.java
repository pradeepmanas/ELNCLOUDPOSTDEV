package com.agaram.eln.primary.service.usermanagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import com.agaram.eln.config.AESEncryption;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.notification.Email;
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
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.model.usermanagement.Lsusersettings;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
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
import com.agaram.eln.primary.repository.usermanagement.LSusersteamRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;
import com.agaram.eln.primary.repository.usermanagement.LsusersettingsRepository;
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

	@Autowired
	private LSMultiusergroupRepositery LSMultiusergroupRepositery;

	@Autowired
	private LsusersettingsRepository LsusersettingsRepository;
	
	@Autowired
	LSusergroupedcolumnsRepository lsusergroupedcolumnsRepository;

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
		
		if(objusergroup.getUsergroupcode() != null) {
			LSusergroup objGroup = lSusergroupRepository.findOne(objusergroup.getUsergroupcode());	
			objusergroup.setCreatedon(objGroup.getCreatedon());
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
				lscfttransactionRepository.save(objusergroup.getObjmanualaudit());
			}
		}
		return objusergroup;
	}

	public List<LSusergroup> GetUserGroup(LSuserMaster objusergroup) {

		return lSusergroupRepository.findByusergroupnameNotOrderByUsergroupcodeDesc("Administrator");
	}

//	public LSusergroup ActDeactUserGroup(LSusergroup objusergroup) 
//	{
//		lSusergroupRepository.save(objusergroup);
//		return objusergroup;
//	}

	public List<LSuserMaster> GetUsers(LSuserMaster objusergroup) {

		if (objusergroup.getUsername().equalsIgnoreCase("Administrator")) {

			return lsuserMasterRepository.findByusernameNotAndUserretirestatusNotOrderByCreateddateDesc("Administrator",
					1);
		}

		return lsuserMasterRepository.findByUsernameNotAndUserretirestatusNotAndLssitemasterOrderByCreateddateDesc(
				"Administrator", 1, objusergroup.getLssitemaster());
	}

	public List<LSuserMaster> GetUsersOnsite(LSSiteMaster objclass) {
		if (objclass.getObjsilentaudit() != null) {
			objclass.getObjsilentaudit().setTableName("LSuserMaster");
			lscfttransactionRepository.save(objclass.getObjsilentaudit());
		}
		if (objclass.getSitecode() == 0) {
			return lsuserMasterRepository.findByusernameNotAndUserretirestatusNotOrderByCreateddateDesc("Administrator",
					1);
		}
//		return lsuserMasterRepository.findByUsernameNotAndLssitemaster("Administrator", objclass);
		return lsuserMasterRepository.findByUsernameNotAndUserretirestatusNotAndLssitemasterOrderByCreateddateDesc(
				"Administrator", 1, objclass);
	}

	public LSuserMaster InsertUpdateUser(LSuserMaster objusermaster) throws MessagingException {
		boolean isnewuser = false;

		if (objusermaster.getUsercode() == null) {
			isnewuser = true;
		}

		if (objusermaster.getUsercode() == null
				&& lsuserMasterRepository.findByLssitemasterAndUsernameIgnoreCase(objusermaster.getLssitemaster(),
						objusermaster.getUsername()) != null) {

			objusermaster.setResponse(new Response());
			objusermaster.getResponse().setStatus(false);
			objusermaster.getResponse().setInformation("ID_EXIST");

			return objusermaster;
		} else if (objusermaster.getUsercode() != null && objusermaster.getUserstatus() != null
				&& objusermaster.getLsusergroup() == null) {
			LSuserMaster updateUser = lsuserMasterRepository.findOne(objusermaster.getUsercode());
			updateUser.setUserstatus(objusermaster.getUserstatus().equals("Active") ? "A" : "D");
			updateUser.setPassword(objusermaster.getPassword());
			updateUser.setLockcount(objusermaster.getLockcount());
			updateUser
					.setUserretirestatus(objusermaster.getUserretirestatus() == 1 ? objusermaster.getUserretirestatus()
							: updateUser.getUserretirestatus());
			if (objusermaster.getMultiusergroupcode() != null && objusermaster.getUsercode() != null) {
				if (!objusermaster.isSameusertologin()) {
					LSMultiusergroupRepositery.deleteByusercode(objusermaster.getUsercode());
					LSMultiusergroupRepositery.save(objusermaster.getMultiusergroupcode());
				}

				updateUser.setUserstatus(objusermaster.getUserstatus().equals("Active") ? "A" : "D");
				updateUser.setUserfullname(objusermaster.getUserfullname());
				updateUser.setEmailid(objusermaster.getEmailid());
				updateUser.setUnifieduserid(objusermaster.getUnifieduserid());

			}

			objusermaster.setCreateddate(updateUser.getCreateddate());

			lsuserMasterRepository.save(updateUser);

			objusermaster.setResponse(new Response());
			objusermaster.getResponse().setStatus(true);
			objusermaster.getResponse().setInformation("ID_SUCCESSMSG");

			return objusermaster;
		}

		/*
		 * if (objusermaster.getUsercode() == null && objusermaster.getIsmultitenant()
		 * != null && objusermaster.getMultitenantusercount() != null &&
		 * objusermaster.getIsmultitenant() == 1) {
		 * 
		 * if (lsuserMasterRepository.countByusercodeNotAndUserretirestatusNot(1, 1) >=
		 * objusermaster .getMultitenantusercount() &&
		 * lsuserMasterRepository.countByusercodeNotAndUserretirestatusNot(1, 1) != 0) {
		 * Response objResponse = new Response(); objResponse.setStatus(false);
		 * objResponse.setInformation("ID_USERCOUNTEXCEEDS");
		 * 
		 * objusermaster.setResponse(objResponse);
		 * 
		 * return objusermaster; }
		 * 
		 * }
		 */

		LSMultiusergroupRepositery.save(objusermaster.getMultiusergroupcode());
		lsuserMasterRepository.save(objusermaster);

		if (isnewuser) {
			String unifieduser = objusermaster.getUsername().toLowerCase().replaceAll("[^a-zA-Z0-9]", "") + "u"
					+ objusermaster.getUsercode() + "s" + objusermaster.getLssitemaster().getSitecode()
					+ objusermaster.getUnifieduserid();

			objusermaster.setUnifieduserid(unifieduser);
			lsuserMasterRepository.save(objusermaster);
		}

		objusermaster.setResponse(new Response());
		objusermaster.getResponse().setStatus(true);
		objusermaster.getResponse().setInformation("ID_SUCCESSMSG");

		return objusermaster;
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
			lscfttransactionRepository.save(objusermaster.getObjsilentaudit());
		}
		// Manual Audit
		if (objusermaster.getObjuser() != null) {
			// LScfttransaction manualAudit=new LScfttransaction();
			Date date = new Date();
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
			objusermaster.getObjmanualaudit().setTransactiondate(date);
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
//			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}

		return objuser;
	}

	public LSusersteam InsertUpdateTeam(LSusersteam objteam) {
		if (objteam.getTeamcode() == null
				&& lsusersteamRepository.findByTeamnameIgnoreCaseAndStatusAndLssitemaster(objteam.getTeamname(), 1,
						objteam.getLssitemaster()) != null) {

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
		} else if (objteam.getStatus() == -1) {

			List<LSlogilablimsorderdetail> order = new ArrayList<LSlogilablimsorderdetail>();
			List<LSprojectmaster> projcode = new ArrayList<LSprojectmaster>();
			projcode = LSprojectmasterRepository.findByLsusersteam(objteam);
			order = LSlogilablimsorderdetailRepository.findByOrderflagAndLsprojectmasterIn("N", projcode);
			if (order.size() > 0) {
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

		return objteam;
	}

	public List<LSusersteam> GetUserTeam(LSuserMaster LSuserMaster) {
		if (LSuserMaster.getUsername().equalsIgnoreCase("Administrator")) {
			return lsusersteamRepository.findBystatus(1);
		}
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
		map.put("obj", lsusersteamRepository.findBylssitemasterAndStatus(objclass, 1));
		return map;
	}

	public Map<String, Object> GetUserRightsonGroup(LSusergroup lsusergroup) {
		Map<String, Object> maprights = new HashMap<String, Object>();
		if (lsusergroup.getUsergroupcode() == null) {
			List<LSusergrouprightsmaster> lstUserrightsmaster = lsusergrouprightsmasterRepository
					.findByOrderBySequenceorder();
			maprights.put("new", true);
			maprights.put("rights", lstUserrightsmaster);
		} else {
			List<LSusergrouprights> lstUserrights = lsusergrouprightsRepository.findByUsergroupid(lsusergroup);
			List<LSusergrouprightsmaster> lstUserrightsmasterlst = lsusergrouprightsmasterRepository
					.findByOrderBySequenceorder();
			if (lstUserrights != null && lstUserrights.size() > 0 && lstUserrights.size() > 10) {
				maprights.put("new", false);
				maprights.put("rights", lstUserrights);
				maprights.put("masterrights", lstUserrightsmasterlst);
			} else {
				List<LSusergrouprightsmaster> lstUserrightsmaster = lsusergrouprightsmasterRepository
						.findByOrderBySequenceorder();
				maprights.put("new", true);
				maprights.put("rights", lstUserrightsmaster);
			}
		}
		if (lsusergroup.getObjsilentaudit() != null) {
			lsusergroup.getObjsilentaudit().setTableName("LSusergroup");
			lscfttransactionRepository.save(lsusergroup.getObjsilentaudit());
		}
		return maprights;
	}

	public Map<String, Object> GetUserRightsonUser(LSuserMaster objUser) {

		LSusergroup lsusergroup = objUser.getLsusergrouptrans();

		Map<String, Object> maprights = new HashMap<String, Object>();
		if (lsusergroup.getUsergroupcode() == null) {
			List<LSusergrouprightsmaster> lstUserrightsmaster = lsusergrouprightsmasterRepository.findAll();
			maprights.put("new", true);
			maprights.put("rights", lstUserrightsmaster);
		} else {
			List<LSusergrouprights> lstUserrights = lsusergrouprightsRepository.findByUsergroupid(lsusergroup);
			if (lstUserrights != null && lstUserrights.size() > 0) {
				maprights.put("new", false);
				maprights.put("rights", lstUserrights);

			} else {
				List<LSusergrouprightsmaster> lstUserrightsmaster = lsusergrouprightsmasterRepository.findAll();
				maprights.put("new", true);
				maprights.put("rights", lstUserrightsmaster);
			}
		}
		if (lsusergroup.getObjsilentaudit() != null) {

			lsusergroup.getObjsilentaudit().setTableName("LSusergroup");
		}
		return maprights;
	}

	public List<LSusergrouprights> SaveUserRights(LSusergrouprights[] lsrites) {
		List<LSusergrouprights> lsrights = Arrays.asList(lsrites);
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

	public List<LSactiveUser> GetActiveUsers(LSuserMaster objuser) {
		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("LSuserMaster");
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}
		return lsactiveUserRepository.findAll();

	}

	public List<LSactiveUser> GetActiveUsersOnsitewise(LSSiteMaster objclass) {
		if (objclass.getObjsilentaudit() != null) {
			objclass.getObjsilentaudit().setTableName("LSuserMaster");
			lscfttransactionRepository.save(objclass.getObjsilentaudit());
		}
		return lsactiveUserRepository.findBylssitemaster(objclass);
	}

	public List<LSusergroup> GetActiveUserGroup(LSuserMaster objusergroup) {

		if (objusergroup.getObjsilentaudit() != null) {
			objusergroup.getObjsilentaudit().setTableName("LSusergroup");
			lscfttransactionRepository.save(objusergroup.getObjsilentaudit());
		}

		return lSusergroupRepository.findByusergroupstatusAndUsergroupnameNot("A", "Administrator");
	}

	public List<LSusergroup> GetSiteWiseUserGroup(LSSiteMaster Objclass) {

		if (Objclass.getObjsilentaudit() != null) {
			Objclass.getObjsilentaudit().setTableName("LSusergroup");
			lscfttransactionRepository.save(Objclass.getObjsilentaudit());
		}
		if (Objclass.getSitecode() == 0) {
			return lSusergroupRepository.findByUsergroupnameNotOrderByUsergroupcodeDesc("Administrator");
		}

		return lSusergroupRepository
				.findBylssitemasterAndUsergroupnameNotOrderByUsergroupcodeDesc(Objclass.getSitecode(), "Administrator");
	}
	
	public List<LSusergroup> GetSiteWiseActiveUserGroup(LSSiteMaster Objclass) {

		if (Objclass.getObjsilentaudit() != null) {
			Objclass.getObjsilentaudit().setTableName("LSusergroup");
			lscfttransactionRepository.save(Objclass.getObjsilentaudit());
		}
		List<String> status = Arrays.asList("A", "Active");
		if (Objclass.getSitecode() == 0) {
			return lSusergroupRepository.findByUsergroupnameNotAndUsergroupstatusInOrderByUsergroupcodeDesc("Administrator",status);
		}
		
		List<LSusergroup> lstusergroup = lSusergroupRepository
				.findBylssitemasterAndUsergroupnameNotAndUsergroupstatusInOrderByUsergroupcodeDesc(Objclass.getSitecode(), "Administrator", status);

		return lstusergroup;
	}

	public List<LSusergroup> GetUserGroupSiteWise(LSSiteMaster Objclass) {

		if (Objclass.getObjsilentaudit() != null) {
			Objclass.getObjsilentaudit().setTableName("LSusergroup");
			lscfttransactionRepository.save(Objclass.getObjsilentaudit());
		}
		if (Objclass.getSitecode() == 0) {
			return lSusergroupRepository.findByusergroupnameNotOrderByUsergroupcodeDesc("Administrator");
		}
		return lSusergroupRepository
				.findBylssitemasterAndUsergroupnameNotOrderByUsergroupcodeDesc(Objclass.getSitecode(), "Administrator");
	}

	public LSuserMaster ValidateSignature(LoggedUser objuser) {
		LSuserMaster objExitinguser = new LSuserMaster();
		String username = objuser.getsUsername();
		LSSiteMaster objsite = LSSiteMasterRepository.findBysitecode(Integer.parseInt(objuser.getsSiteCode()));
		objExitinguser = lsuserMasterRepository.findByusernameAndLssitemaster(username, objsite);

		if (objExitinguser != null) {
			String Password = AESEncryption.decrypt(objExitinguser.getPassword());
			objExitinguser.setObjResponse(new Response());

			if (objuser.getIsmultitenant() == 1) {
				objExitinguser.getObjResponse().setStatus(true);

				LScfttransaction manualAudit = new LScfttransaction();
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
			} else {
				if (Password.equals(objuser.getsPassword())) {
					objExitinguser.getObjResponse().setStatus(true);

					LScfttransaction manualAudit = new LScfttransaction();
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

		lSpasswordpolicyRepository.save(objpwd);

		if (objpwd.getObjsilentaudit() != null) {
			objpwd.getObjsilentaudit().setTableName("LSPasswordPolicy");
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

		objresmap.put("newnotificationcount",
				lsnotificationRepository.countByNotifationtoAndIsnewnotification(lsuserMaster, 1));
		objresmap.put("notification",
				lsnotificationRepository.findFirst10ByNotifationtoOrderByNotificationcodeDesc(lsuserMaster));

		return objresmap;
	}

	public Map<String, Object> Updatenotificationread(LSnotification lsnotification) {
		Map<String, Object> objresmap = new HashMap<String, Object>();

		lsnotificationRepository.updatenotificationstatus(lsnotification.getNotifationto(),
				lsnotification.getNotificationcode());

		objresmap = GetLatestnotification(lsnotification);

		return objresmap;
	}

	public Map<String, Object> GetnotificationonLazyload(LSnotification lsnotification) {
		Map<String, Object> objresmap = new HashMap<String, Object>();

		objresmap.put("notification",
				lsnotificationRepository
						.findFirst10ByNotifationtoAndNotificationcodeLessThanOrderByNotificationcodeDesc(
								lsnotification.getNotifationto(), lsnotification.getNotificationcode()));

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
		}
		return objctrluser;
	}

	@SuppressWarnings("unused")
	public LSuserMaster Usersendpasswormail(LSuserMaster objusermaster) throws MessagingException {

		if (objusermaster.getIsmultitenant() != null && objusermaster.getMultitenantusercount() != null
				&& objusermaster.getIsmultitenant() == 1) {
			String password = Generatetenantpassword();
			String passwordadmin = AESEncryption.encrypt(password);
			LSuserMaster lsuserMaster = new LSuserMaster();
			objusermaster.setPassword(passwordadmin);
			Email email = new Email();
			email.setMailto(objusermaster.getEmailid());
			email.setSubject("ELN User Credentials");

			email.setMailcontent(
					"<b>Dear Customer,</b><br><center><img src=\"cid:image\"  style =width:120px; height:100px border: 3px;'></center><br><br>"
							+ "<p><p>Thanks for your interest in Logilab ELN.</p>Please use below mentioned Username and Password for your Login in ELN Application.<br><br>"
							+ "Click the URL mentioned below to go to Logilab ELN Login page. <br><br>"
							+ "After entered the username and click the password field, new password generation screen will appear.<br><br>"
							+ "Paste the password in the Old Password Textbox, and then generate your new password.<br><br>"
							+ "<b style='margin-left: 76px;'>Username:</b>\t\t " + objusermaster.getUsername()
							+ "<br><br>" + "<b style='margin-left: 76px;'>Password &nbsp;:</b>\t\t" + password
							+ "<br><br>" + "<b style='margin-left: 76px;'><a href=" + objusermaster.getUserloginlink()
							+ ">Click here to Logilab ELN Login page</a></b><br><br>"
							+ "<p>If you have any queries, please contact our support by mail to info@agaramtech.com <br><br><br>"
							+ "Regards,</p>" + "<b>Agaram Technologies Private Limited</b><br><br>"
							+ "<img src=\"cid:seconimage\"  style ='width:120px; height:120px;border: 3px;'"
							+ "<br><br><p>T: +91 44 4208 2005</p><p>T: +91 44 42189406</p>"
							+ "W:<a href='https://www.agaramtech.com'>https://www.agaramtech.com</a></p>");

			emailService.sendEmail(email);
			lsuserMasterRepository.setpasswordandpasswordstatusByusercode(objusermaster.getPassword(),
					objusermaster.getPasswordstatus(), objusermaster.getUsercode());
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
			lscfttransactionRepository.save(objusergroup.getObjsilentaudit());
		}
		if (objusergroup.getUsername().equalsIgnoreCase("Administrator")) {
			return lsuserMasterRepository.findByusernameNot("Administrator");
		}
		return lsuserMasterRepository.findByUsernameNotAndLssitemaster("Administrator", objusergroup.getLssitemaster());
	}

	public LSuserMaster getUserOnCode(LSuserMaster objuser) {
		return lsuserMasterRepository.findByusercode(objuser.getUsercode());
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
				.findByLssitemasterAndUsercodeNotInAndUserretirestatusAndUnifieduseridNotNullOrderByUsercodeDesc(objuser.getLssitemaster(),lstuser, 0);
	}

	public Lsusersettings getUserPrefrences(LSuserMaster objuser) {
		return LsusersettingsRepository.findByUsercode(objuser.getUsercode());
	}

	public List<LSMultiusergroup> getMultiUserGroup(LSuserMaster objusermaster) {

		return LSMultiusergroupRepositery.findByusercode(objusermaster.getUsercode());
	}
	
	public LSusergroupedcolumns setGroupedcolumn(LSusergroupedcolumns objgroupped)
	{
		return lsusergroupedcolumnsRepository.save(objgroupped);
	}
	
	public LSusergroupedcolumns getGroupedcolumn(LSusergroupedcolumns objgroupped)
	{
		return lsusergroupedcolumnsRepository.findByUsercodeAndSitecodeAndGridname(objgroupped.getUsercode(), objgroupped.getSitecode(), objgroupped.getGridname());
	}
}


