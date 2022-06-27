package com.agaram.eln.primary.service.usermanagement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.agaram.eln.config.ADS_Connection;
import com.agaram.eln.config.AESEncryption;
import com.agaram.eln.config.JwtTokenUtil;
import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.cfr.LSaudittrailconfiguration;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cfr.LSpreferences;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.jwt.JwtResponse;
import com.agaram.eln.primary.model.sheetManipulation.Notification;
import com.agaram.eln.primary.model.usermanagement.LSMultiusergroup;
import com.agaram.eln.primary.model.usermanagement.LSPasswordHistoryDetails;
import com.agaram.eln.primary.model.usermanagement.LSPasswordPolicy;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSactiveUser;
import com.agaram.eln.primary.model.usermanagement.LSdomainMaster;
import com.agaram.eln.primary.model.usermanagement.LSnotification;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.cfr.LSpreferencesRepository;
import com.agaram.eln.primary.repository.sheetManipulation.NotificationRepository;
import com.agaram.eln.primary.repository.usermanagement.LSMultiusergroupRepositery;
import com.agaram.eln.primary.repository.usermanagement.LSPasswordHistoryDetailsRepository;
import com.agaram.eln.primary.repository.usermanagement.LSPasswordPolicyRepository;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSactiveUserRepository;
import com.agaram.eln.primary.repository.usermanagement.LSdomainMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSnotificationRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusergroupRepository;
import com.agaram.eln.primary.service.JWTservice.JwtUserDetailsService;
import com.agaram.eln.primary.service.cfr.AuditService;

@Service
@EnableJpaRepositories(basePackageClasses = LSSiteMasterRepository.class)

public class LoginService {

	@Autowired
	private LSSiteMasterRepository lSSiteMasterRepository;
	@Autowired
	private LSdomainMasterRepository lSDomainMasterRepository;
	@Autowired
	private LSuserMasterRepository lSuserMasterRepository;
	@Autowired
	private LScfttransactionRepository lscfttransactionRepository;

	@Autowired
	private LSpreferencesRepository LSpreferencesRepository;

	@Autowired
	private LSactiveUserRepository lsactiveUserRepository;

	@Autowired
	private LSusergroupRepository LSusergroupRepository;

	@Autowired
	private LSPasswordHistoryDetailsRepository LSPasswordHistoryDetailsRepository;

	@Autowired
	private LSPasswordPolicyRepository LSPasswordPolicyRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private AuditService auditService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private LSuserMasterRepository lsuserMasterRepository;

	private String ModuleName = "UserManagement";

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Autowired
	private LSMultiusergroupRepositery LSMultiusergroupRepositery;

	// added for notification
	@Autowired
	private NotificationRepository NotificationRepository;

	@Autowired
	private LSnotificationRepository LSnotificationRepository;

//	@Autowired
//	private LSfileRepository lSfileRepository;

//	@Autowired
//	private commonfunction commonfunction;

	// added for notification

	static final Logger logger = Logger.getLogger(LoginService.class.getName());

	public List<LSSiteMaster> loadSite() {
		List<LSSiteMaster> result = new ArrayList<LSSiteMaster>();
		lSSiteMasterRepository.findByIstatus(1).forEach(result::add);
		return result;
	}

	public List<LSSiteMaster> LoadSiteMaster() {
		return lSSiteMasterRepository.findByOrderBySitecodeAsc();
	}

	public List<LSdomainMaster> loadDomain(LSSiteMaster objsite) {
		List<LSdomainMaster> result = new ArrayList<LSdomainMaster>();
		result = lSDomainMasterRepository.findBylssitemaster(objsite);
		return result;
	}

//	private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {{
//        put("^\\d{8}$", "yyyyMMdd");
//        put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
//        put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
//        put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
//        put("^\\d{1,2}/\\d{1,2}/\\d{2}$", "dd/MM/yy");//
//        put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
//        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
//        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
//        put("^\\d{12}$", "yyyyMMddHHmm");
//        put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
//        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
//        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
//        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
//        put("^\\d{1,2}/\\d{1,2}/\\d{2}\\s\\d{1,2}:\\d{2}\\s[A-Z]{2}$", "dd/MM/yy HH:mm a");//
//        put("^\\d{1,2}/\\d{1,2}/\\d{2}\\s\\d{1,2}:\\d{2}$", "dd/MM/yy HH:mm");//
//        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
//        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
//        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
//        put("^\\d{14}$", "yyyyMMddHHmmss");
//        put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
//        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
//        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
//        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
//        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
//        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
//        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
//    }};

	@SuppressWarnings("unused")
	public Map<String, Object> Login(LoggedUser objuser) {
		Map<String, Object> obj = new HashMap<>();
		LSuserMaster objExitinguser = new LSuserMaster();

		String username = objuser.getsUsername();

		LSSiteMaster objsiteobj = lSSiteMasterRepository.findBysitecode(Integer.parseInt(objuser.getsSiteCode()));

		objExitinguser = lSuserMasterRepository.findByUsernameIgnoreCaseAndLssitemasterAndLoginfrom(username,
				objsiteobj, "0");

		LSPasswordPolicy lockcount = objExitinguser != null
				? LSPasswordPolicyRepository
						.findTopByAndLssitemasterOrderByPolicycodeDesc(objExitinguser.getLssitemaster())
				: null;

		LSpreferences objPrefrence = LSpreferencesRepository.findByTasksettingsAndValuesettings("ConCurrentUser",
				"Active");

		if (objPrefrence != null) {

			List<LSactiveUser> lstActUsrs = lsactiveUserRepository.findBylssitemaster(objsiteobj);

			String dvalue = objPrefrence.getValueencrypted();

			String sConcurrentUsers = AESEncryption.decrypt(dvalue);

			sConcurrentUsers = sConcurrentUsers.replaceAll("\\s", "");

			int nConcurrentUsers = Integer.parseInt(sConcurrentUsers);
			int actUsr = lstActUsrs.size();

			if (actUsr >= nConcurrentUsers) {

				objExitinguser.setObjResponse(new Response());

				objExitinguser.getObjResponse().setInformation("IDS_LICENCERCHD");
				objExitinguser.getObjResponse().setStatus(false);

				obj.put("user", objExitinguser);
				return obj;

			}

		}

		if (objExitinguser != null) {

			if (objuser.getLsusergroup() == null) {

				objExitinguser.setLsusergroup(LSusergroupRepository.findOne(objuser.getMultiusergroupcode()));
			} else {
				objExitinguser.setLsusergroup(objuser.getLsusergroup());
			}

			objExitinguser.setObjResponse(new Response());
			objExitinguser.setObjsilentaudit(new LScfttransaction());

			if ((Integer.parseInt(objuser.getsSiteCode()) == objExitinguser.getLssitemaster().getSitecode())
					|| objuser.getsUsername().equalsIgnoreCase("Administrator")) {
				String Password = AESEncryption.decrypt(objExitinguser.getPassword());
				System.out.println(" password: " + Password);

				Date passwordexp = objExitinguser.getPasswordexpirydate();
				if (Password.equals(objuser.getsPassword()) && objExitinguser.getUserstatus() != "Locked"
						&& objExitinguser.getUserretirestatus() == 0) {

					String encryptionStr = objExitinguser.getPassword() + "_" + objExitinguser.getUsername()
							+ objExitinguser.getLssitemaster().getSitename();

					String encryptPassword = AESEncryption.encrypt(encryptionStr);

					obj.put("encryptedpassword", encryptPassword);

					String status = objExitinguser.getUserstatus();
					String groupstatus = objExitinguser.getLsusergrouptrans().getUsergroupstatus();
					if (status.equals("Deactive")) {
						objExitinguser.getObjResponse().setInformation("ID_NOTACTIVE");
						objExitinguser.getObjResponse().setStatus(false);

						obj.put("user", objExitinguser);
						return obj;
					} else if (groupstatus.trim().equals("Deactive")) {
						objExitinguser.getObjResponse().setInformation("ID_GRPNOACT");
						objExitinguser.getObjResponse().setStatus(false);

						obj.put("user", objExitinguser);
						return obj;
					} else {

						if (!username.trim().toLowerCase().equals("administrator")) {
							Date date = new Date();

							boolean comp1 = objExitinguser.getPasswordexpirydate().compareTo(date) > 0;
							boolean comp2 = objExitinguser.getPasswordexpirydate().compareTo(date) < 0;
							boolean comp3 = objExitinguser.getPasswordexpirydate().compareTo(date) == 0;
							if (comp3 == true || (comp1 == false && comp2 == true)) {
								objExitinguser.setPassword(null);
								lSuserMasterRepository.save(objExitinguser);
								objExitinguser.getObjResponse().setInformation("ID_EXPIRY");
								objExitinguser.getObjResponse().setStatus(false);

								obj.put("user", objExitinguser);
								return obj;
							} else {
								objExitinguser.getObjResponse().setStatus(true);
								objExitinguser.setLockcount(0);
								lSuserMasterRepository.save(objExitinguser);
							}
						} else {
							objExitinguser.getObjResponse().setStatus(true);
							objExitinguser.setLockcount(0);
							lSuserMasterRepository.save(objExitinguser);
						}
					}
				} else if (objExitinguser.getUserretirestatus() != 0) {

					objExitinguser.getObjResponse().setInformation("ID_RETIREDUSER");
					objExitinguser.getObjResponse().setStatus(false);

					obj.put("user", objExitinguser);
					return obj;

				}

				else if (!Password.equals(objuser.getsPassword()) || objExitinguser.getLockcount() == 5
						|| objExitinguser.getUserstatus() == "Locked") {
					if (!username.trim().toLowerCase().equals("administrator")) {
						Integer count = objExitinguser.getLockcount() == null ? 0 : objExitinguser.getLockcount();
						count++;
						if (count.equals(lockcount.getLockpolicy())) {
							objExitinguser.setUserstatus("Locked");
							objExitinguser.setLockcount(count++);
							objExitinguser.getObjResponse().setInformation("ID_LOCKED");
							objExitinguser.getObjResponse().setStatus(false);

							lSuserMasterRepository.save(objExitinguser);
						} else if (count < lockcount.getLockpolicy()) {
							objExitinguser.setLockcount(count++);
							objExitinguser.getObjResponse().setInformation("ID_INVALID");
							objExitinguser.getObjResponse().setStatus(false);

							lSuserMasterRepository.save(objExitinguser);
						} else {
							objExitinguser.getObjResponse().setInformation("ID_LOCKED");
							objExitinguser.getObjResponse().setStatus(false);

						}
					} else {
						objExitinguser.getObjResponse().setInformation("ID_INVALID");
						objExitinguser.getObjResponse().setStatus(false);

					}
				}
			} else {
				objExitinguser.getObjResponse().setInformation("ID_SITEVALID");
				objExitinguser.getObjResponse().setStatus(false);

				obj.put("user", objExitinguser);
				return obj;
			}

			obj.put("multiusergroupcode", objuser.getMultiusergroupcode());
		} else {
			LSSiteMaster objsite = lSSiteMasterRepository.findBysitecode(Integer.parseInt(objuser.getsSiteCode()));
			objExitinguser = lSuserMasterRepository.findByusernameAndLssitemaster("Administrator", objsite);
			objExitinguser.setObjResponse(new Response());
			objExitinguser.getObjResponse().setInformation("ID_NOTEXIST");
			objExitinguser.getObjResponse().setStatus(false);

		}

		obj.put("user", objExitinguser);

		return obj;
	}

	public Boolean Updatepassword(int UserId, String Password) {
		LSuserMaster objuserforupdate = lSuserMasterRepository.findByusercode(UserId);
		objuserforupdate.setPassword(AESEncryption.encrypt(Password));
		lSuserMasterRepository.save(objuserforupdate);

		return true;
	}

	public List<LSuserMaster> CheckUserAndPassword(LoggedUser objuser) {
		List<LSuserMaster> objExitinguser = new ArrayList<LSuserMaster>();
		String username = objuser.getsUsername();

		LSSiteMaster objsite = lSSiteMasterRepository.findBysitecode(Integer.parseInt(objuser.getsSiteCode()));
		objExitinguser = lSuserMasterRepository
				.findByUsernameIgnoreCaseAndLssitemasterAndLoginfromAndUserretirestatusNot(username, objsite, "0", 1);

		if (objExitinguser.size() != 0) {
			String Password = AESEncryption.decrypt(objExitinguser.get(0).getPassword());
			objExitinguser.get(0).setObjResponse(new Response());

			if (Password == null) {
				objExitinguser.get(0).getObjResponse().setInformation("GenerateNewPassword");
				objExitinguser.get(0).getObjResponse().setStatus(true);

			} else {
				objExitinguser.get(0).getObjResponse().setInformation("Valid user and password exist");
				objExitinguser.get(0).getObjResponse().setStatus(true);
			}
		} else {

			objExitinguser = lSuserMasterRepository
					.findByUsernameIgnoreCaseAndLoginfromAndUserretirestatusNotOrderByCreateddateDesc(username, "0", 1);

			if (objExitinguser.size() != 0) {
//				objExitinguser = new LSuserMaster();
				objExitinguser.get(0).setUserstatus("");
				objExitinguser.get(0).setObjResponse(new Response());
				objExitinguser.get(0).getObjResponse().setInformation("User is not present on the site.");
				objExitinguser.get(0).getObjResponse().setStatus(false);

			} else {
				LSuserMaster objExitinguser1 = new LSuserMaster();
				objExitinguser1.setUserstatus("");
				objExitinguser1.setObjResponse(new Response());
				objExitinguser1.getObjResponse().setInformation("Invalid user");
				objExitinguser1.getObjResponse().setStatus(false);
				objExitinguser.add(objExitinguser1);
			}
		}
		return objExitinguser;
	}

	@SuppressWarnings({ "unused" })
	public LSuserMaster UpdatePassword(LoggedUser objuser) {
		LSuserMaster objExitinguser = new LSuserMaster();
		String username = objuser.getsUsername();
		LSSiteMaster objsiteobj = lSSiteMasterRepository.findBysitecode(Integer.parseInt(objuser.getsSiteCode()));
		objExitinguser = lSuserMasterRepository.findByusernameIgnoreCaseAndLssitemaster(username, objsiteobj);
		List<LSPasswordHistoryDetails> listofpwd = new ArrayList<LSPasswordHistoryDetails>();
		LSPasswordHistoryDetails objectpwd = new LSPasswordHistoryDetails();
		List<LSPasswordHistoryDetails> result = new ArrayList<LSPasswordHistoryDetails>();
		LSPasswordPolicy passHistorycount = LSPasswordPolicyRepository
				.findByLssitemaster(objExitinguser.getLssitemaster());

		listofpwd = LSPasswordHistoryDetailsRepository
				.findTop5ByAndLsusermasterInOrderByPasswordcodeDesc(objExitinguser);

		if (objExitinguser != null) {
			objExitinguser.setObjResponse(new Response());

			String newpassword = AESEncryption.encrypt(objuser.getsNewPassword());
			if (listofpwd.size() != 0) {
				if (listofpwd.size() > passHistorycount.getPasswordhistory()) {
					listofpwd.subList(passHistorycount.getPasswordhistory(), listofpwd.size()).clear();
				}
				result = listofpwd.stream()
						.filter(LSPasswordHistoryDetails -> newpassword.equals(LSPasswordHistoryDetails.getPassword()))
						.collect(Collectors.toList());
			}
			if (result.size() != 0) {
				objExitinguser.getObjResponse().setInformation("ID_HISTORY");
				objExitinguser.getObjResponse().setStatus(false);
				if (objuser.getObjsilentaudit() != null) {
					objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
					objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
					objuser.getObjsilentaudit().setComments(
							objuser.getsUsername() + " " + "entered password does not reach the history range");
					objuser.getObjsilentaudit().setManipulatetype("Password");
					objuser.getObjsilentaudit().setTableName("LSPasswordHistoryDetails");
					lscfttransactionRepository.save(objuser.getObjsilentaudit());

				}
				return objExitinguser;

			}

			String existingpassword = AESEncryption.decrypt(objExitinguser.getPassword());
			if (objuser.getsOLDPassword().equals(existingpassword) && objuser.getsOLDPassword() != "") {
				if (objuser.getsNewPassword().equals(objuser.getsConfirmPassword())) {
					objectpwd.setPassword(AESEncryption.encrypt(objuser.getsNewPassword()));
					objectpwd.setPasswordcreatedate(new Date());
					objectpwd.setLsusermaster(objExitinguser);
					LSPasswordHistoryDetailsRepository.save(objectpwd);

					objExitinguser.setPassword(AESEncryption.encrypt(objuser.getsNewPassword()));
					objExitinguser.setPasswordexpirydate(objuser.getPasswordexpirydate());
					objExitinguser.setPasswordstatus(0);
					lSuserMasterRepository.save(objExitinguser);

					objExitinguser.getObjResponse().setInformation("ID_SUCCESSMSG");
					objExitinguser.getObjResponse().setStatus(true);

					if (objuser.getObjsilentaudit() != null) {
						objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
						objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
						objuser.getObjsilentaudit().setManipulatetype("Password");
						objuser.getObjsilentaudit().setTableName("LSuserMaster");
						lscfttransactionRepository.save(objuser.getObjsilentaudit());

					}

				} else {
					objExitinguser.getObjResponse().setInformation("ID_NOTMATCH");
					objExitinguser.getObjResponse().setStatus(false);
				}
			} else if ((!objuser.getsOLDPassword().equals(existingpassword)) && (existingpassword != null)) {
				objExitinguser.getObjResponse().setInformation("ID_NOTOLDPASSMATCH");
				objExitinguser.getObjResponse().setStatus(false);
			} else if (objuser.getsNewPassword().equals(objuser.getsConfirmPassword())
					&& objuser.getsOLDPassword() == "") {

				objectpwd.setPassword(AESEncryption.encrypt(objuser.getsNewPassword()));
				objectpwd.setPasswordcreatedate(new Date());
				objectpwd.setLsusermaster(objExitinguser);
				LSPasswordHistoryDetailsRepository.save(objectpwd);

				objExitinguser.setPassword(AESEncryption.encrypt(objuser.getsNewPassword()));
				objExitinguser.setPasswordexpirydate(objuser.getPasswordexpirydate());
				lSuserMasterRepository.save(objExitinguser);

				objExitinguser.getObjResponse().setInformation("ID_SUCCESSMSG");
				objExitinguser.getObjResponse().setStatus(true);

				if (objuser.getObjsilentaudit() != null) {
					objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
					objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
					objuser.getObjsilentaudit().setManipulatetype("Password");
					objuser.getObjsilentaudit().setTableName("LSuserMaster");
					lscfttransactionRepository.save(objuser.getObjsilentaudit());

				}

			} else {
				objExitinguser.getObjResponse().setInformation("ID_NOTMATCH");
				objExitinguser.getObjResponse().setStatus(false);
			}
		} else {
			objExitinguser.getObjResponse().setInformation("ID_INVALID");
			objExitinguser.getObjResponse().setStatus(false);
		}
		return objExitinguser;
	}

	public Boolean Logout(LSuserMaster lsuserMaster) {
		lsactiveUserRepository.deleteBylsusermaster(lsuserMaster);
		return true;
	}

	@SuppressWarnings({ "unused" })
	public LSuserMaster ChangePassword(LoggedUser objuser) {
		LSuserMaster objExitinguser = new LSuserMaster();

		LSPasswordHistoryDetails objectpwd = new LSPasswordHistoryDetails();

		List<LSPasswordHistoryDetails> listofpwd = new ArrayList<LSPasswordHistoryDetails>();

		List<LSPasswordHistoryDetails> result = new ArrayList<LSPasswordHistoryDetails>();

		String username = objuser.getsUsername();
		LSSiteMaster objsite = lSSiteMasterRepository
				.findBysitecode(objuser.getLsusermaster().getLssitemaster().getSitecode());
		objExitinguser = lSuserMasterRepository.findByusernameAndLssitemaster(username, objsite);
		LSPasswordPolicy passHistorycount = LSPasswordPolicyRepository
				.findByLssitemaster(objExitinguser.getLssitemaster());

		listofpwd = LSPasswordHistoryDetailsRepository
				.findTop5ByAndLsusermasterInOrderByPasswordcodeDesc(objExitinguser);

		if (objExitinguser != null) {
			objExitinguser.setObjResponse(new Response());

			String Password = AESEncryption.decrypt(objExitinguser.getPassword());
			objExitinguser.setObjResponse(new Response());

			String newpassword = AESEncryption.encrypt(objuser.getsNewPassword());
			if (listofpwd.size() != 0) {
				if (listofpwd.size() > passHistorycount.getPasswordhistory()) {
					listofpwd.subList(passHistorycount.getPasswordhistory(), listofpwd.size()).clear();
				}
				result = listofpwd.stream()
						.filter(LSPasswordHistoryDetails -> newpassword.equals(LSPasswordHistoryDetails.getPassword()))
						.collect(Collectors.toList());
			}

			if (!Password.equals(objuser.getsPassword())) {
				objExitinguser.getObjResponse().setInformation("ID_EXIST");
				objExitinguser.getObjResponse().setStatus(false);
				if (objuser.getObjsilentaudit() != null) {
					objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
					objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
					objuser.getObjsilentaudit()
							.setComments(objuser.getsUsername() + " " + "entered existing password incorrectly");
					objuser.getObjsilentaudit().setManipulatetype("Password");
					objuser.getObjsilentaudit().setTableName("LSuserMaster");
					lscfttransactionRepository.save(objuser.getObjsilentaudit());

				}
				return objExitinguser;
			}
			if (result.size() != 0) {
				objExitinguser.getObjResponse().setInformation("ID_HISTORY");
				objExitinguser.getObjResponse().setStatus(false);
				if (objuser.getObjsilentaudit() != null) {
					objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
					objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());

					objuser.getObjsilentaudit().setComments(
							objuser.getsUsername() + " " + "entered password does not reach the history range");


					objuser.getObjsilentaudit().setManipulatetype("Password");
					objuser.getObjsilentaudit().setTableName("LSPasswordHistoryDetails");
					lscfttransactionRepository.save(objuser.getObjsilentaudit());

				}
				return objExitinguser;
			}
			if (objuser.getsNewPassword().equals(objuser.getsConfirmPassword())) {

				objExitinguser.setPassword(AESEncryption.encrypt(objuser.getsNewPassword()));
				if(objuser.getPasswordexpirydate()!=null) {
					objExitinguser.setPasswordexpirydate(objuser.getPasswordexpirydate());
				}
				lSuserMasterRepository.save(objExitinguser);

				objectpwd.setPassword(AESEncryption.encrypt(objuser.getsNewPassword()));
				objectpwd.setPasswordcreatedate(new Date());
				objectpwd.setLsusermaster(objuser.getLsusermaster());
				LSPasswordHistoryDetailsRepository.save(objectpwd);

				objExitinguser.getObjResponse().setInformation("ID_CHANGESUC");
				objExitinguser.getObjResponse().setStatus(true);

				if (objuser.getObjsilentaudit() != null) {
					objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
					objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());



					objuser.getObjsilentaudit().setSystemcoments("System Generated");

					objuser.getObjsilentaudit().setTableName("LSuserMaster");
					lscfttransactionRepository.save(objuser.getObjsilentaudit());

				}

			} else {
				objExitinguser.getObjResponse().setInformation("ID_NOTMATCH");
				objExitinguser.getObjResponse().setStatus(false);
			}
		} else {
			objExitinguser.getObjResponse().setInformation("ID_INVALID");
			objExitinguser.getObjResponse().setStatus(false);
		}
		return objExitinguser;
	}

	public LSdomainMaster InsertupdateDomain(LSdomainMaster objClass) {

		objClass.setResponse(new Response());
		if (objClass.getDomaincode() == null && lSDomainMasterRepository
				.findByDomainnameIgnoreCaseAndDomainstatus(objClass.getDomainname(), 1) != null) {
			objClass.getResponse().setStatus(false);
			objClass.getResponse().setInformation("ID_EXIST");
//			silent audit
			if (objClass.getObjsilentaudit() != null) {
				objClass.getObjsilentaudit().setActions("Warning");
				objClass.getObjsilentaudit().setComments(objClass.getObjsilentaudit().getUsername() + " "
						+ "made attempt to create existing domain name");
				objClass.getObjsilentaudit().setTableName("LSusergroup");

			}
			return objClass;
		} else if (objClass.getDomaincode() != null && objClass.getDomainstatus() != 1) {

			if (objClass.getObjsilentaudit() != null) {
				objClass.getObjsilentaudit().setTableName("LSdomainMaster");

			}

			objClass = lSDomainMasterRepository.findOne(objClass.getDomaincode());
			objClass.setDomainstatus(-1);
			lSDomainMasterRepository.save(objClass);
			objClass.setResponse(new Response());
			objClass.getResponse().setStatus(true);
			objClass.getResponse().setInformation("ID_DOMAINDEL");

			return objClass;
		}
		lSDomainMasterRepository.save(objClass);
		objClass.getResponse().setStatus(true);
		objClass.getResponse().setInformation("");

		return objClass;
	}

	public LSuserMaster importADSScreen(LSuserMaster objClass) {
		objClass.setResponse(new Response());
		objClass.getResponse().setStatus(false);
		objClass.getResponse().setInformation("Username and password invalid");

		return objClass;
	}

	public Response ADSDomainServerConnection(Map<String, Object> objMap) {

		Response res = new Response();

		Map<String, Object> objCredentials = new HashMap<>();

//		ObjectMapper objMapper = new ObjectMapper();
		String sUsername = (String) objMap.get("sUsername");
		String sPassword = (String) objMap.get("sPassword");
		String sDomain = ((String) objMap.get("sDomain")).trim();
		try {

			String[] sDomainAry = sDomain.split(".");

			if (sDomainAry.length > 0) {
				sDomain = sDomainAry[0];
			}

			String userName = sUsername;
			String password = sPassword;

			objCredentials.put("sServerUserName", userName);
			objCredentials.put("sPassword", password);
			objCredentials.put("sDomainName", sDomain);

//			Boolean isConnect = ADS_Connection.CheckLDAPConnection(url, userName, password);
//			Boolean isConnect = ADS_Connection.CheckDomainLDAPConnection(objCredentials);

			Map<String, Object> connectMap = ADS_Connection.CheckDomainLDAPConnection4Msg(objCredentials);

			Boolean isConnect = (Boolean) connectMap.get("connect");

			if (isConnect) {
				res.setInformation("ID_CONNECTSUCC");
				res.setStatus(true);

			} else {
				res.setInformation((String) connectMap.get("Msg"));
				res.setStatus(false);
			}

		} catch (Exception ex) {
			res.setInformation("ID_CONNECTFAIL");
			res.setStatus(false);
		}
		return res;
	}

	public Map<String, Object> addImportADSUsers(Map<String, Object> objMap) {

		Map<String, Object> rtnMap = new HashMap<>();
		boolean isCompleted = false;

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> lstAdsUsers = (List<Map<String, Object>>) objMap.get("ADSUsers");
//		ObjectMapper objMapper = new ObjectMapper();

		if (!lstAdsUsers.isEmpty()) {

			@SuppressWarnings("unchecked")
			Map<String, Object> uGroup = (Map<String, Object>) objMap.get("userGroup");

			Integer siteCode = (Integer) uGroup.get("lssitemaster");

			LSusergroup userGroup = LSusergroupRepository.findOne((Integer) uGroup.get("usergroupcode"));
			LSSiteMaster sSiteCode = lSSiteMasterRepository.findBysitecode(siteCode);

			rtnMap.put("LSusergroup", userGroup);
			rtnMap.put("LSSiteMaster", sSiteCode);

			List<LSuserMaster> lstUsers = new ArrayList<>();

			@SuppressWarnings("unchecked")
			Map<String, Object> sObject = (Map<String, Object>) objMap.get("objsilentaudit");

			LSuserMaster uMaster = lsuserMasterRepository.findByusercode((Integer) sObject.get("lsuserMaster"));

			String sCreateBy = uMaster.getUsername();
			String sRepeatedUser = "";

			for (int u = 0; u < lstAdsUsers.size(); u++) {
				String sUserDomainID = (String) lstAdsUsers.get(u).get("DomainUserID");

				List<LSuserMaster> lstUserName = lSuserMasterRepository.findByUsernameAndLssitemaster(sUserDomainID,
						sSiteCode);

				LSuserMaster lsUser = new LSuserMaster();

				if (lstUserName.isEmpty()) {

					String sUserFullName = (String) lstAdsUsers.get(u).get("UserName");
					int sApprove = Integer.parseInt((String) lstAdsUsers.get(u).get("sApprove"));

					String sUserStatus = "D";
					if (sApprove == 1)
						sUserStatus = "A";

					lsUser.setCreatedby(sCreateBy);
					lsUser.setCreateddate(new Date());
					lsUser.setLockcount(0);
					lsUser.setUserfullname(sUserFullName);
					lsUser.setUsername(sUserDomainID);
					lsUser.setUserstatus(sUserStatus);
					lsUser.setLssitemaster(sSiteCode);
					lsUser.setPasswordstatus(0);
					lsUser.setUserretirestatus(0);

					List<LSMultiusergroup> lstGroup = new ArrayList<LSMultiusergroup>();

					LSMultiusergroup objGroup = new LSMultiusergroup();
					objGroup.setDefaultusergroup(1);
					objGroup.setLsusergroup(userGroup);

					lstGroup.add(objGroup);

					lsUser.setMultiusergroupcode(lstGroup);

					LSMultiusergroupRepositery.save(lsUser.getMultiusergroupcode());
					lsuserMasterRepository.save(lsUser);

				} else {
					if (sRepeatedUser.length() > 0) {
						sRepeatedUser += (String) ", " + sUserDomainID;
					} else {
						sRepeatedUser = (String) sUserDomainID;
					}
				}
				lstUsers.add(lsUser);
			}
			rtnMap.put("sRepeatedUser", sRepeatedUser);
			if (lstUsers.size() > 0) {
				lSuserMasterRepository.save(lstUsers);

				isCompleted = true;

			} else {
				isCompleted = false;
			}
		}
		rtnMap.put("isCompleted", isCompleted);

		return rtnMap;

	}

	public Map<String, Object> ADSServerDomainCombo(LSuserMaster Objclass) {
		Map<String, Object> rtnObjMap = new HashMap<>();
		List<LSdomainMaster> listDomain = new ArrayList<>();
		List<LSusergroup> listGroup = new ArrayList<>();
		try {
			listDomain = ADSServerDomainLoad();
			listGroup = ADSGroupnameLoad(Objclass.getLssitemaster());
			rtnObjMap.put("oResObj", listDomain);
			rtnObjMap.put("oResObj1", listGroup);

		} catch (Exception ex) {

		}
		return rtnObjMap;
	}

	public List<LSusergroup> ADSGroupnameLoad(LSSiteMaster Objclass) {
		List<String> status = Arrays.asList("A", "Active");
		return LSusergroupRepository
				.findByLssitemasterAndUsergroupstatusInOrderByUsergroupcodeDesc(Objclass.getSitecode(), status);

	}

	public List<LSdomainMaster> ADSServerDomainLoad() {

		return lSDomainMasterRepository.findBycategoriesAndDomainstatus("server", 1);
	}

	public List<LSuserMaster> UserMasterDetails(LSusergroup sUserGroupID, LSSiteMaster sSiteCode) {

		List<LSuserMaster> lstUsers = lSuserMasterRepository.findByLssitemasterAndLsusergroup(sSiteCode, sUserGroupID);

		return lstUsers;
	}

	public List<LSdomainMaster> LoadDomainMaster(LSSiteMaster objsite) {
		return lSDomainMasterRepository.findBylssitemasterAndDomainstatus(objsite, 1);
	}

	public List<LSdomainMaster> LoadDomainMasterAdmin(LSSiteMaster objsite) {
		return lSDomainMasterRepository.findAll();
	}

	public LSuserMaster validateuser(LSuserMaster objClass) {
		LSuserMaster objuser = new LSuserMaster();
		LSuserMaster objExitinguser = new LSuserMaster();
		String username = objClass.getUsername();
		String usergroupname = objClass.getLsusergroup().getUsergroupname();
		objExitinguser = lSuserMasterRepository.findByusernameAndLssitemaster(username, objClass.getLssitemaster());
		objuser.setObjResponse(new Response());
		if (objExitinguser != null) {
			if (usergroupname.equalsIgnoreCase(objExitinguser.getLsusergroup().getUsergroupname())) {
				String Password = AESEncryption.decrypt(objExitinguser.getPassword());

				if (Password.equals(objClass.getPassword()) && objExitinguser.getUserstatus() != "Locked") {
					objuser.getObjResponse().setStatus(true);
					objuser.setUsername(AESEncryption.encrypt(objExitinguser.getUsername()));
					objuser.setPassword(objExitinguser.getPassword());
				} else if (!Password.equals(objClass.getPassword())) {
					objuser.getObjResponse().setStatus(false);
					objuser.getObjResponse().setInformation("Password mismatch");
				} else {
					objuser.getObjResponse().setStatus(false);
					objuser.getObjResponse().setInformation("User Locked");
				}
			} else {
				objuser.getObjResponse().setStatus(false);
				objuser.getObjResponse()
						.setInformation("Group name is Mismatched for this Username in ELN Application");
				return objuser;
			}
		} else {
			objuser.getObjResponse().setStatus(false);
			objuser.getObjResponse().setInformation("Invalid user");
		}
		return objuser;
	}

	public LSuserMaster LinkLogin(LSuserMaster objClass) {
		LSuserMaster objuser = new LSuserMaster();
		LSuserMaster objExitinguser = new LSuserMaster();

		// String password =AESEncryption.decrypt(objClass.getPassword());
		String username = AESEncryption.decrypt(objClass.getUsername());

		// String username =objClass.getUsername();
		String password = objClass.getPassword();

		objExitinguser = lSuserMasterRepository.findByUsernameAndPassword(username, password);

		if (objExitinguser != null) {
			// objuser = objExitinguser;
			objExitinguser.setObjResponse(new Response());
			objExitinguser.getObjResponse().setStatus(true);

			return objExitinguser;
		} else {
			objuser.setObjResponse(new Response());
			objuser.getObjResponse().setStatus(false);
			objuser.getObjResponse().setInformation("Invalid user");

			return objuser;
		}
	}

	public LSSiteMaster InsertupdateSite(LSSiteMaster objClass) {
		objClass.setResponse(new Response());
		if (objClass.getSitecode() == null
				&& lSSiteMasterRepository.findBySitenameIgnoreCaseAndIstatus(objClass.getSitename(), 1) != null) {
			objClass.getResponse().setStatus(false);
			objClass.getResponse().setInformation("ID_EXIST");

			return objClass;
		} else if (objClass.getSitecode() != null && objClass.getSitecode() != 1) {

			lSSiteMasterRepository.save(objClass);
			objClass.setResponse(new Response());
			objClass.getResponse().setStatus(true);
			objClass.getResponse().setInformation("ID_DOMAINDEL");

			return objClass;
		}
		if (objClass.getSitecode() == null) {
			List<LSSiteMaster> site = new ArrayList<LSSiteMaster>();
			site = lSSiteMasterRepository.findAll();
			if (site.size() == 1) {
				objClass.setSitecode(2);
			}
		}

		lSSiteMasterRepository.save(objClass);
		objClass.getResponse().setStatus(true);
		objClass.getResponse().setInformation("");

		return objClass;
	}

	@SuppressWarnings("unused")
	public Map<String, Object> azureauthenticatelogin(LoggedUser objuser) {
		Map<String, Object> obj = new HashMap<>();
		LSuserMaster objExitinguser = new LSuserMaster();

		String username = objuser.getsUsername();
		LSSiteMaster objsite = new LSSiteMaster(Integer.parseInt(objuser.getsSiteCode()));
		objExitinguser = lSuserMasterRepository.findByUsernameIgnoreCaseAndLoginfromAndLssitemaster(username, "1",
				objsite);

		if (objExitinguser != null) {
			objExitinguser.setObjResponse(new Response());
			objExitinguser.setObjsilentaudit(new LScfttransaction());
			if ((Integer.parseInt(objuser.getsSiteCode()) == objExitinguser.getLssitemaster().getSitecode())
					&& objExitinguser.getUserretirestatus() == 0) {

				if (objExitinguser.getUserstatus() != "Locked") {

					String status = objExitinguser.getUserstatus();

//			    	if(objExitinguser.getLsusergroup() == null)
//			    	{
					List<LSMultiusergroup> LSMultiusergroup = new ArrayList<LSMultiusergroup>();

					LSMultiusergroup = LSMultiusergroupRepositery.findByusercode(objExitinguser.getUsercode());

					objExitinguser.setLsusergroup(LSMultiusergroup.get(0).getLsusergroup());
//			    	}

					String groupstatus = objExitinguser.getLsusergroup().getUsergroupstatus();
					if (status.equals("Deactive")) {
						objExitinguser.getObjResponse().setInformation("ID_NOTACTIVE");
						objExitinguser.getObjResponse().setStatus(false);
						objuser.getObjsilentaudit().setActions("Warning");
						objuser.getObjsilentaudit()
								.setComments(objExitinguser.getUsername() + " " + "was not active to login");
						objuser.getObjsilentaudit().setTableName("LSuserMaster");
						objuser.getObjsilentaudit().setUsername(objExitinguser.getUsername());
						objuser.getObjsilentaudit().setSystemcoments("System Generated");
						objuser.getObjsilentaudit().setModuleName(ModuleName);
						objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
						objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());

						lscfttransactionRepository.save(objuser.getObjsilentaudit());

						obj.put("user", objExitinguser);
						return obj;
					} else if (groupstatus.trim().equals("Deactive")) {
						objExitinguser.getObjResponse().setInformation("ID_GRPNOACT");
						objExitinguser.getObjResponse().setStatus(false);

						objuser.getObjsilentaudit().setActions("Warning");
						objuser.getObjsilentaudit().setComments("Currently group was not active for the user" + " "
								+ objExitinguser.getUsername() + " " + "to login");
						objuser.getObjsilentaudit().setTableName("LSusergroup");
						objuser.getObjsilentaudit().setUsername(objExitinguser.getUsername());
						objuser.getObjsilentaudit().setSystemcoments("System Generated");
						objuser.getObjsilentaudit().setModuleName(ModuleName);
						objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
						objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
						lscfttransactionRepository.save(objuser.getObjsilentaudit());

						obj.put("user", objExitinguser);
						return obj;
					} else {

						try {
							Date newDate = new SimpleDateFormat("yyyy/dd/MM hh:mm:ss").parse("4444/31/12 23:58:57");
							System.out.println(newDate);
							Locale locale = Locale.getDefault();
							DateFormat datetimeFormatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,
									DateFormat.SHORT, locale);

							String dateSString = datetimeFormatter.format(newDate);
							dateSString = dateSString.replaceAll("31", "dd");
							dateSString = dateSString.replaceAll("12", "MM");
							dateSString = dateSString.replaceAll("Dec", "MMM");
							dateSString = dateSString.replaceAll("4444", "yyyy");
							dateSString = dateSString.replaceAll("44", "yy");
							dateSString = dateSString.replaceAll("11", "hh");
							dateSString = dateSString.replaceAll("23", "hh");
							dateSString = dateSString.replaceAll("58", "mm");
							dateSString = dateSString.replaceAll("57", "ss");
							dateSString = dateSString.replaceAll(" AM", "");
							dateSString = dateSString.replaceAll(" PM", "");
							logger.info(dateSString);
							dateSString = "MM-dd-yyyy hh:mm:ss";
							objExitinguser.setDFormat(dateSString);
						} catch (ParseException e) {
							e.printStackTrace();
						}

						objExitinguser.getObjResponse().setStatus(true);

						if (objuser.getObjsilentaudit() != null) {
							objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
							objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
							objuser.getObjsilentaudit().setModuleName(ModuleName);
							objuser.getObjsilentaudit().setComments("User Logged in Successfully");
							objuser.getObjsilentaudit().setActions("Login Success");
							objuser.getObjsilentaudit().setSystemcoments("System Generated");
							objuser.getObjsilentaudit().setManipulatetype("Login");
							objuser.getObjsilentaudit().setTableName("LSactiveuser");
							lscfttransactionRepository.save(objuser.getObjsilentaudit());

						}

					}
				}

			} else if (objExitinguser.getUserretirestatus() != 0) {

				String status = objExitinguser.getUserstatus();
				String groupstatus = objExitinguser.getLsusergroup().getUsergroupstatus();

				objExitinguser.getObjResponse().setInformation("ID_RETIREDUSER");
				objExitinguser.getObjResponse().setStatus(false);
				objuser.getObjsilentaudit().setActions("Warning");
				objuser.getObjsilentaudit().setComments(objExitinguser.getUsername() + " " + " user was retired");
				objuser.getObjsilentaudit().setTableName("LSuserMaster");
				objuser.getObjsilentaudit().setUsername(objExitinguser.getUsername());
				objuser.getObjsilentaudit().setSystemcoments("System Generated");
				objuser.getObjsilentaudit().setModuleName(ModuleName);
				objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
				objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());

				lscfttransactionRepository.save(objuser.getObjsilentaudit());

				obj.put("user", objExitinguser);
				return obj;

			}

			else {
				objExitinguser.getObjResponse().setInformation("ID_SITEVALID");
				objExitinguser.getObjResponse().setStatus(false);

				objuser.getObjsilentaudit().setActions("Warning");
				objuser.getObjsilentaudit()
						.setComments(objExitinguser.getUsername() + " " + "does not belongs to the site");
				objuser.getObjsilentaudit().setTableName("LSuserMaster");
				objuser.getObjsilentaudit().setUsername(objExitinguser.getUsername());
				objuser.getObjsilentaudit().setSystemcoments("System Generated");
				objuser.getObjsilentaudit().setModuleName(ModuleName);
				objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
				objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
				lscfttransactionRepository.save(objuser.getObjsilentaudit());

				obj.put("user", objExitinguser);
				return obj;
			}
		}

		else {
			objExitinguser = lSuserMasterRepository.findByusernameAndLssitemaster("Administrator", objsite);
			objExitinguser.setObjResponse(new Response());
			objExitinguser.getObjResponse().setInformation("ID_NOTEXIST");
			objExitinguser.getObjResponse().setStatus(false);

			objuser.getObjsilentaudit().setActions("Warning");
			objuser.getObjsilentaudit().setComments("User" + " " + objuser.getsUsername() + " " + "does not exist");
			objuser.getObjsilentaudit().setTableName("LSusergroup");
			objuser.getObjsilentaudit().setUsername(objExitinguser.getUsername());
			objuser.getObjsilentaudit().setSystemcoments("System Generated");
			objuser.getObjsilentaudit().setModuleName(ModuleName);
			objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
			objuser.getObjsilentaudit().setLssitemaster(Integer.parseInt(objuser.getsSiteCode()));
			lscfttransactionRepository.save(objuser.getObjsilentaudit());

		}

		obj.put("user", objExitinguser);
		if (objExitinguser.getLsusergroup() != null) {
			obj.put("userrights", userService.GetUserRightsonGroup(objExitinguser.getLsusergroup()));
			LSaudittrailconfiguration objauditconfig = new LSaudittrailconfiguration();
			objauditconfig.setLsusermaster(objExitinguser);
			obj.put("auditconfig", auditService.GetAuditconfigUser(objauditconfig));
			obj.put("multiusergroupcode",
					objExitinguser.getMultiusergroupcode().get(0).getLsusergroup().getUsergroupcode());
		}

		return obj;

	}

	public LSuserMaster createuserforazure(LSuserMaster objuser) {
		LSuserMaster userDetails = lsuserMasterRepository.findByUsernameIgnoreCaseAndLoginfromAndLssitemaster(
				objuser.getUsername(), "1", objuser.getLssitemaster());

		if (userDetails == null) {
			if (objuser.getIsmultitenant() != null && objuser.getMultitenantusercount() != null
					&& objuser.getIsmultitenant() == 1) {
//				if(lsuserMasterRepository.countByusercodeNot(1) >= objuser.getMultitenantusercount())
				if (lsuserMasterRepository.countByusercodeNotAndUserretirestatusNot(1, 1) >= objuser
						.getMultitenantusercount()) {
					Response objResponse = new Response();
					objResponse.setStatus(false);
					objResponse.setInformation("ID_USERCOUNTEXCEEDS");
					objuser.setObjResponse(objResponse);

					return objuser;
				}
			}
			LSusergroup objaadsgroup = LSusergroupRepository.findByusergroupnameAndLssitemaster("Azure aads",
					objuser.getLssitemaster().getSitecode());
			LSusergroup objgroup = new LSusergroup();
			LSMultiusergroup LSMultiusergroup = new LSMultiusergroup();
			if (objaadsgroup == null) {

				objgroup.setUsergroupname("Azure aads");
				objgroup.setLssitemaster(objuser.getLssitemaster().getSitecode());
				objgroup.setCreatedby(objuser.getUsername());
				objgroup.setModifiedby(objuser.getUsername());
				objgroup.setCreatedon(objuser.getCreateddate());
				objgroup.setModifiedon(objuser.getCreateddate());
				objgroup.setUsergroupstatus("A");

				LSusergroupRepository.save(objgroup);

				objuser.setLsusergroup(objgroup);

				LSMultiusergroup.setLsusergroup(objgroup);
				LSMultiusergroup.setDefaultusergroup(objgroup.getUsergroupcode());
			} else {
				objuser.setLsusergroup(objaadsgroup);
				LSMultiusergroup.setDefaultusergroup(objaadsgroup.getUsergroupcode());
				LSMultiusergroup.setLsusergroup(objaadsgroup);
			}

			objuser.setCreatedby(objuser.getUsername());
			objuser.setModifiedby(objuser.getUsername());
			objuser.setUserstatus("A");
			objuser.setLockcount(0);
			objuser.setUserretirestatus(0);
			objuser.setPassword(objuser.getToken());

			Response objResponse = new Response();
			objResponse.setStatus(true);
			objuser.setObjResponse(objResponse);
			objuser.setLoginfrom("1");

			List<LSMultiusergroup> LSMultiusergroup1 = new ArrayList<LSMultiusergroup>();
			LSMultiusergroup1.add(LSMultiusergroup);
			objuser.setMultiusergroupcode(LSMultiusergroup1);

			LSMultiusergroupRepositery.save(objuser.getMultiusergroupcode());
			lsuserMasterRepository.save(objuser);

			String unifieduser = objuser.getUsername().toLowerCase().replaceAll("[^a-zA-Z0-9]", "") + "u"
					+ objuser.getUsercode() + "s" + objuser.getLssitemaster().getSitecode()
					+ objuser.getUnifieduserid();

			objuser.setUnifieduserid(unifieduser);
			lsuserMasterRepository.save(objuser);
		} else {
			objuser = userDetails;
			Response objResponse = new Response();
			objResponse.setStatus(true);
			objResponse.setInformation("");
			objuser.setUnifieduserid(userDetails.getUnifieduserid());
			objuser.setObjResponse(objResponse);
		}

		return objuser;
	}

	public ResponseEntity<?> azureusertokengenrate(LSuserMaster objuser) throws Exception {

		if (objuser.getUsername() == null)
			return null;

		LSuserMaster userDetails = lsuserMasterRepository.findByUsernameIgnoreCaseAndLoginfromAndLssitemaster(
				objuser.getUsername(), "1", objuser.getLssitemaster());

		LSPasswordPolicy policydays = LSPasswordPolicyRepository.findByLssitemaster(objuser.getLssitemaster());
		if (policydays == null) {
			LSSiteMaster lssitemaster = new LSSiteMaster();
			lssitemaster.setSitecode(1);
			LSPasswordPolicy lspasswordPolicy = LSPasswordPolicyRepository.findByLssitemaster(lssitemaster);

			LSPasswordPolicy objPassword = new LSPasswordPolicy();
			objPassword.setComplexpasswrd(lspasswordPolicy.getComplexpasswrd());
			objPassword.setLockpolicy(lspasswordPolicy.getLockpolicy());
			objPassword.setMaxpasswrdlength(lspasswordPolicy.getMaxpasswrdlength());
			objPassword.setMincapitalchar(lspasswordPolicy.getMincapitalchar());
			objPassword.setMinspecialchar(lspasswordPolicy.getMinspecialchar());
			objPassword.setMinnumericchar(lspasswordPolicy.getMinnumericchar());
			objPassword.setMinpasswrdlength(lspasswordPolicy.getMinpasswrdlength());
			objPassword.setMinsmallchar(lspasswordPolicy.getMinsmallchar());
			objPassword.setPasswordexpiry(lspasswordPolicy.getPasswordexpiry());
			objPassword.setPasswordhistory(lspasswordPolicy.getPasswordhistory());
			objPassword.setLssitemaster(objuser.getLssitemaster());
			LSPasswordPolicyRepository.save(objPassword);
			policydays = LSPasswordPolicyRepository.findByLssitemaster(objuser.getLssitemaster());
		}
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, policydays.getPasswordexpiry());

		if (userDetails == null) {

			LSusergroup objaadsgroup = LSusergroupRepository.findByusergroupnameAndLssitemaster("Azure aads",
					objuser.getLssitemaster().getSitecode());
			LSusergroup objgroup = new LSusergroup();
			if (objaadsgroup == null) {

				objgroup.setUsergroupname("Azure aads");
				objgroup.setLssitemaster(objuser.getLssitemaster().getSitecode());
				objgroup.setCreatedby(objuser.getUsername());
				objgroup.setModifiedby(objuser.getUsername());
				objgroup.setCreatedon(objuser.getCreateddate());
				objgroup.setModifiedon(objuser.getCreateddate());
				objgroup.setUsergroupstatus("A");

				LSusergroupRepository.save(objgroup);

				objuser.setLsusergroup(objgroup);
			} else {
				objuser.setLsusergroup(objaadsgroup);
			}

			objuser.setCreatedby(objuser.getUsername());
			objuser.setModifiedby(objuser.getUsername());
			objuser.setUserstatus("A");
			objuser.setLockcount(0);
			objuser.setUserretirestatus(0);
			objuser.setPassword(objuser.getToken());
			objuser.setPasswordexpirydate(c.getTime());

			objuser.setLoginfrom("1");
			lsuserMasterRepository.save(objuser);
		} else {
			objuser.setPassword(objuser.getToken());
			userDetails.setPassword(objuser.getToken());
			objuser.setPasswordexpirydate(
					userDetails.getPasswordexpirydate() == null ? c.getTime() : userDetails.getPasswordexpirydate());
			userDetails.setPasswordexpirydate(
					userDetails.getPasswordexpirydate() == null ? c.getTime() : userDetails.getPasswordexpirydate());
			lsuserMasterRepository.save(userDetails);
		}

		String Tokenuser = objuser.getUsername() + "[" + objuser.getLssitemaster().getSitecode() + "]";

		final UserDetails userDetailstoken = userDetailsService.loadUserByUsername(Tokenuser);

		final String token = jwtTokenUtil.generateToken(userDetailstoken);

		return ResponseEntity.ok(new JwtResponse(token));
	}

	public ResponseEntity<?> createAuthenticationToken(LSuserMaster objuser) throws Exception {

		String Tokenuser = objuser.getUsername() + "[" + objuser.getLssitemaster().getSitecode() + "]";

		final UserDetails userDetails = userDetailsService.loadUserByUsername(Tokenuser);

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}

	public ResponseEntity<?> limsloginusertokengenarate(LSuserMaster objClass) {

		String Tokenuser = objClass.getUsername() + "[" + objClass.getLssitemaster().getSitecode() + "]";

		final UserDetails userDetails = userDetailsService.loadUserByUsername(Tokenuser);

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));

	}

	public Map<String, Object> Switchusergroup(LSuserMaster lsuserMaster) {
		Map<String, Object> obj = new HashMap<>();
		int multiusergroupcode = lsuserMaster.getMultiusergroupcode().get(0).getMultiusergroupcode();
		LSMultiusergroup objLSMultiusergroup = LSMultiusergroupRepositery.findBymultiusergroupcode(multiusergroupcode);
		String groupstatus = objLSMultiusergroup.getLsusergroup().getUsergroupstatus();
//		LSuserMaster objExitinguser = lSuserMasterRepository.findByUsernameIgnoreCaseAndLoginfrom(lsuserMaster.getUsername(),"0");
		LSuserMaster objExitinguser = lSuserMasterRepository
				.findByUsernameIgnoreCaseAndLssitemaster(lsuserMaster.getUsername(), lsuserMaster.getLssitemaster());
		objExitinguser.setLsusergroup(objLSMultiusergroup.getLsusergroup());
		objExitinguser.setObjResponse(new Response());
		
		if (objLSMultiusergroup != null) {

			if (lsuserMaster.getObjsilentaudit() != null) {
				lsuserMaster.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
				lsuserMaster.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
				lsuserMaster.getObjsilentaudit().setManipulatetype("Password");
				lsuserMaster.getObjsilentaudit().setTableName("LSuserMaster");
				lscfttransactionRepository.save(lsuserMaster.getObjsilentaudit());

			}

			obj.put("user", objExitinguser);
			if (groupstatus.trim().equals("Active")) {
			if (objExitinguser.getLsusergroup() != null) {
				obj.put("userrights", userService.GetUserRightsonGroup(objExitinguser.getLsusergroup()));
				LSaudittrailconfiguration objauditconfig = new LSaudittrailconfiguration();
				objauditconfig.setLsusermaster(objExitinguser);
				obj.put("auditconfig", auditService.GetAuditconfigUser(objauditconfig));

				obj.put("multiusergroupcode", objLSMultiusergroup.getLsusergroup().getUsergroupcode());
			}

			objExitinguser.getObjResponse().setInformation("usergroup switched successfully ");
			objExitinguser.getObjResponse().setStatus(true);
			}else {
				objExitinguser.getObjResponse().setInformation("ID_GRPNOACT");
				objExitinguser.getObjResponse().setStatus(false);
			}
		}

		return obj;
	}

	// added for notification
	public Notification Loginnotification(Notification objNotification) throws ParseException {

		Date currentdate = objNotification.getCurrentdate();

		List<Notification> codelist = NotificationRepository.findByUsercode(objNotification.getUsercode());

		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();

		int i = 0;
		boolean value = false;
		while (i < codelist.size()) {

			value = commonfunction.isSameDay(currentdate, codelist.get(i).getCautiondate());

			LSnotification LSnotification = new LSnotification();

			LSuserMaster LSuserMaster = new LSuserMaster(); /* to get the value */
			LSuserMaster.setUsercode(codelist.get(i).getUsercode());

			LSuserMaster objLSuserMaster = new LSuserMaster();/* to return the value this obj is created */
			objLSuserMaster = userService.getUserOnCode(LSuserMaster);

			String Details = "{\"ordercode\" :\"" + codelist.get(i).getOrderid() + "\",\"order\" :\""
					+ codelist.get(i).getBatchid() + "\",\"description\":\"" + codelist.get(i).getDescription() + "\"}";

			if (codelist.get(i).getStatus() == 1 && value) {

				LSnotification.setIsnewnotification(1);
				LSnotification.setNotification("CAUTIONALERT");
				LSnotification.setNotificationdate(objNotification.getCurrentdate());
				LSnotification.setNotificationdetils(Details);
				LSnotification.setNotificationpath("/registertask");
				LSnotification.setNotifationfrom(objLSuserMaster);
				LSnotification.setNotifationto(objLSuserMaster);
				LSnotification.setRepositorycode(0);
				LSnotification.setRepositorydatacode(0);

				codelist.get(i).setStatus(0);
				lstnotifications.add(LSnotification);
			}

			i++;
		}

		LSnotificationRepository.save(lstnotifications);
		NotificationRepository.save(codelist);

		return null;

	}

	public LSactiveUser activeUserEntry(LSactiveUser objsite) {

		LSuserMaster objUser = lsuserMasterRepository.findByusercode(objsite.getLsusermaster().getUsercode());

		if (objUser != null) {

			objsite.setLssitemaster(objsite.getLssitemaster());
			objsite.setTimestamp(objsite.getTimestamp());

			objUser.setLastloggedon(objsite.getTimestamp());
			lsuserMasterRepository.save(objUser);
			lsactiveUserRepository.save(objsite);

		}

		return objsite;
	}

	public List<LSuserMaster> ValidateuserAndPassword(LoggedUser objuser) {
		List<LSuserMaster> objExitinguser = new ArrayList<LSuserMaster>();
		String username = objuser.getsUsername();
		String userPassword = objuser.getsPassword();
		LSSiteMaster objsite = lSSiteMasterRepository.findBysitecode(Integer.parseInt(objuser.getsSiteCode()));
		objExitinguser = lSuserMasterRepository.findByUsernameIgnoreCaseAndLssitemasterAndUserretirestatusNot(username,
				objsite, 1);

		if (!objExitinguser.isEmpty()) {

			objExitinguser.get(0).setObjResponse(new Response());

			if (objuser.getLoggedfrom() == 1) {

				objExitinguser.get(0).getObjResponse().setInformation("Valid user and password");
				objExitinguser.get(0).getObjResponse().setStatus(true);
				return objExitinguser;

			}

			String Password = AESEncryption.decrypt(objExitinguser.get(0).getPassword());

			if (Password.equals(userPassword)) {
				objExitinguser.get(0).getObjResponse().setInformation("Valid user and password");
				objExitinguser.get(0).getObjResponse().setStatus(true);
			} else {
				objExitinguser.get(0).getObjResponse().setInformation("invalid password");
				objExitinguser.get(0).getObjResponse().setStatus(false);
			}
		} else {
			LSuserMaster objExitinguser1 = new LSuserMaster();
			objExitinguser1.setUserstatus("");
			objExitinguser1.setObjResponse(new Response());
			objExitinguser1.getObjResponse().setInformation("Invalid user");
			objExitinguser1.getObjResponse().setStatus(false);
			objExitinguser.add(objExitinguser1);

		}
		return objExitinguser;
	}

}