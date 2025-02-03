package com.agaram.eln.primary.service.usermanagement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.agaram.eln.config.ADS_Connection;
import com.agaram.eln.config.AESEncryption;
import com.agaram.eln.config.JwtTokenUtil;
import com.agaram.eln.primary.commonfunction.commonfunction;
//import com.agaram.eln.primary.fetchtenantsource.Datasourcemaster;
import com.agaram.eln.primary.model.adsconnection.Tbladssettings;
import com.agaram.eln.primary.model.cfr.LSaudittrailconfiguration;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cfr.LSpreferences;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSOrdernotification;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.jwt.JwtResponse;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.multitenant.DataSourceConfig;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.sheetManipulation.Notification;
import com.agaram.eln.primary.model.usermanagement.LSMultisites;
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
import com.agaram.eln.primary.repository.adsconnection.TbladssettingsRepository;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.cfr.LSpreferencesRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSordernotificationRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesdataRepository;
import com.agaram.eln.primary.repository.multitenant.DataSourceConfigRepository;
import com.agaram.eln.primary.repository.protocol.LSlogilabprotocoldetailRepository;
import com.agaram.eln.primary.repository.sheetManipulation.NotificationRepository;
import com.agaram.eln.primary.repository.usermanagement.LSMultisitesRepositery;
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
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
//import com.agaram.eln.primary.service.instrumentDetails.InstrumentService;
@Service
@EnableJpaRepositories(basePackageClasses = LSSiteMasterRepository.class)

public class LoginService {
	@Autowired
	private LsrepositoriesdataRepository lsrepositoriesdataRepository;
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
	private LSlogilablimsorderdetailRepository lslogilablimsorderdetailRepository;
	@Autowired
	private LSlogilabprotocoldetailRepository lslogilabprotocoldetailRepository;
	@Autowired
	private LSactiveUserRepository lsactiveUserRepository;
	
	@Autowired
	private DataSourceConfigRepository configRepo;
	
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

	@Autowired
	private DataSourceConfigRepository DataSourceConfigRepository;

	@Autowired
	private LSactiveUserRepository LSactiveUserRepository;

	@Autowired
	private TbladssettingsRepository tbladssettingsRepository;
	@Autowired
	private LSMultisitesRepositery LSMultisitesRepositery;

	@Autowired
	private LSordernotificationRepository lsordernotificationrepo;
//	
//	@Autowired
//	private InstrumentService instrumentservice;
//	@Autowired
//	private ScheduledtasksRepository scheduledtaskrepo;
	
	
	static final String INITIAL_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
	static final String SECURITY_AUTHENTICATION = "simple";

//	@Autowired
//	private LSfileRepository lSfileRepository;

//	@Autowired
//	private commonfunction commonfunction;

	// added for notification

	public List<LSSiteMaster> loadSite() {
		List<LSSiteMaster> result = new ArrayList<LSSiteMaster>();
		lSSiteMasterRepository.findByIstatus(1).forEach(result::add);
		return result;
	}

	public List<LSSiteMaster> LoadSiteMaster() {
		return lSSiteMasterRepository.findByOrderBySitecodeDesc();
	}

	public List<LSdomainMaster> loadDomain(LSSiteMaster objsite) {
		List<LSdomainMaster> result = new ArrayList<LSdomainMaster>();
		// result = lSDomainMasterRepository.findBylssitemaster(objsite,1);

		result = lSDomainMasterRepository.findBylssitemasterAndDomainstatus(objsite, 1);
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String CheckLDAPUserConnection(Tbladssettings objAdsConnectConfig) throws NamingException {
		String str = "";
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		env.put(Context.SECURITY_AUTHENTICATION, SECURITY_AUTHENTICATION);
		env.put(Context.PROVIDER_URL, objAdsConnectConfig.getLdaplocation());
		env.put(Context.SECURITY_PRINCIPAL,
				objAdsConnectConfig.getSloginid() + "@" + objAdsConnectConfig.getLdapserverdomainname());
		env.put(Context.SECURITY_CREDENTIALS, objAdsConnectConfig.getSpassword());
		env.put(Context.REFERRAL, "follow");
		try {
			DirContext ctx = new InitialDirContext(env);

			ctx.close();
			return "success";
		} catch (AuthenticationNotSupportedException ex) {
			str = "IDS_AUTHENTICATIONNOTSUPPORTEDBYSERVER";

		} catch (AuthenticationException ex) {
			String errorcodes[] = { "52e", "525", "530", "531", "532", "533", "701", "773" };
			for (int i = 0; i < errorcodes.length; i++) {
				String strser = errorcodes[i];
				if (ex.getLocalizedMessage().toString().contains(strser)) {
					switch (errorcodes[i]) {
					case "52e":
						str = "IDS_WRONGCREDENTIAL";
						break;
					case "525":
						str = "IDS_USERNOTFOUND";
						break;
					case "530":
						str = "IDS_NOTPERMITTEDCONTACTADMIN";
						break;
					case "531":
						str = "IDS_NOTPERMITTED";
						break;
					case "532":
						str = "IDS_PASSWORDEXPIRED";
						break;
					case "533":
						str = "IDS_ACCOUNTDISABLED";
						break;
					case "701":
						str = "IDS_ACCOUNTEXPIRED";
						break;
					case "773":
						str = "IDS_USERMUSTRESETPASSWORD";
						break;
					default:
						str = "IDS_UNKNOWNERROR";
						break;
					}
					break;
				}
			}
		}
		return str;
	}

	@SuppressWarnings({ "null" })
	public Map<String, Object> adsUserValidation(LoggedUser objuser) throws Exception {

		Map<String, Object> obj = new HashMap<>();
		String username = objuser.getsUsername();
		LSSiteMaster objsiteobj = lSSiteMasterRepository.findBysitecode(Integer.parseInt(objuser.getsSiteCode()));
		LSuserMaster objExitinguser = lSuserMasterRepository
				.findByUsernameIgnoreCaseAndLssitemasterAndLoginfromAndIsadsuser(username, objsiteobj, "0", 1);

		if (objExitinguser != null) {
			Tbladssettings objTbladssettings = tbladssettingsRepository.findByLdaptatus(1);

			if (objTbladssettings != null) {

				objTbladssettings.setSloginid(username);
				objTbladssettings.setSpassword(objuser.getsPassword());
				String str = "";

				try {
					str = CheckLDAPUserConnection(objTbladssettings);
				} catch (NamingException e) {
					str = "failure";
					System.out.print(e);
				}

				if (str.equalsIgnoreCase("success")) {
					objExitinguser.setObjResponse(new Response());
					objExitinguser.getObjResponse().setStatus(true);
					objExitinguser.setLockcount(0);
					lSuserMasterRepository.save(objExitinguser);

					if (objExitinguser.getObjResponse().getStatus() == true) {
						LSuserMaster objUser1 = lsuserMasterRepository.findByusercode(objExitinguser.getUsercode());
						if (objUser1 != null) {
							LSactiveUser activeUser = new LSactiveUser();
							objExitinguser.setLssitemaster(objExitinguser.getLssitemaster());
							try {
								activeUser.setTimestamp(commonfunction.getCurrentUtcTime());
								activeUser.setClientname(null);
								activeUser.setLastactivetime(commonfunction.getCurrentUtcTime());
								activeUser.setLssitemaster(objExitinguser.getLssitemaster());
								activeUser.setLsusermaster(objUser1);
								objUser1.setLastloggedon(commonfunction.getCurrentUtcTime());
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							String userPassword = AESEncryption.encrypt(objuser.getsPassword());
							objUser1.setPassword(userPassword);
							objExitinguser.setPassword(userPassword);

							lsuserMasterRepository.save(objUser1);
							lsactiveUserRepository.save(activeUser);
							obj.put("activeUserId", activeUser);

							if (objuser.getLsusergroup() == null) {
								objExitinguser
										.setLsusergroup(LSusergroupRepository.findOne(objuser.getMultiusergroupcode()));
							} else {
								objExitinguser.setLsusergroup(objuser.getLsusergroup());
							}

							String encryptionStr = objUser1.getPassword() + "_" + objUser1.getUsername()
									+ objUser1.getLssitemaster().getSitename();

							String encryptPassword = AESEncryption.encrypt(encryptionStr);

							obj.put("encryptedpassword", encryptPassword);

							obj.put("multiusergroupcode", objuser.getMultiusergroupcode());
						}
					}
					obj.put("user", objExitinguser);

					return obj;

				} else {
					objExitinguser.setObjResponse(new Response());
					objExitinguser.getObjResponse().setInformation(str);
					objExitinguser.getObjResponse().setStatus(false);

					obj.put("user", objExitinguser);
					return obj;
				}
			} else {
				objExitinguser.setObjResponse(new Response());
				objExitinguser.getObjResponse().setInformation("IDS_ADD_LDAP_CONFIGUARATION");
				objExitinguser.getObjResponse().setStatus(false);

				obj.put("user", objExitinguser);
				return obj;
			}
		} else {
			objExitinguser.setObjResponse(new Response());
			objExitinguser.getObjResponse().setInformation("IDS_ADS_USERNOT_PRESENTIN_ELN");
			objExitinguser.getObjResponse().setStatus(false);

			obj.put("user", objExitinguser);
			return obj;
		}
	}

	@SuppressWarnings("unused")
	public Map<String, Object> Login(LoggedUser objuser) {
		Map<String, Object> obj = new HashMap<>();
		LSuserMaster objExitinguser = new LSuserMaster();
		String username = objuser.getsUsername();	
		LSSiteMaster objsiteobj = lSSiteMasterRepository.findBysitecode(Integer.parseInt(objuser.getsSiteCode()));
		List<LSMultisites> objformultisite = LSMultisitesRepositery.findByLssiteMaster(objsiteobj);
		List<Integer> usercode = objformultisite.stream().map(LSMultisites::getUsercode).collect(Collectors.toList());
		List<LSuserMaster> userobj = lSuserMasterRepository
				.findByUsernameIgnoreCaseAndUsercodeInAndLoginfromAndUserretirestatusNot(username, usercode, "0", 1);
		LSSiteMaster[] tempobj = { null };
		LSSiteMaster[] multisitelogin = { null };
		if (!userobj.isEmpty()) {
			userobj.forEach(items -> {
				items.getLsmultisites().forEach(values -> {

					if (values.getLssiteMaster().getSitecode() == Integer.parseInt(objuser.getsSiteCode())) {
						tempobj[0] = items.getLssitemaster();
						multisitelogin[0] = values.getLssiteMaster();
						items.setLssitemaster(values.getLssiteMaster());
					}
				});
			});
			objExitinguser = userobj.get(0);
		}

		// kumaresan for multisite purpose
//		objExitinguser = lSuserMasterRepository.findByUsernameIgnoreCaseAndLssitemasterAndLoginfrom(username,
//				objsiteobj, "0");
		LSPasswordPolicy lockcount = objExitinguser != null
				? LSPasswordPolicyRepository
						.findTopByAndLssitemasterOrderByPolicycodeDesc(objExitinguser.getLssitemaster())
				: null;

		LSpreferences objPrefrence = LSpreferencesRepository.findByTasksettingsAndValuesettings("ConCurrentUser",
				"Active");

		if (objPrefrence != null) {

			List<LSactiveUser> lstActUsrs = lsactiveUserRepository.findAll();
			String dvalue = objPrefrence.getValueencrypted();

			if (dvalue != null) {
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
			} else {
				objExitinguser.setObjResponse(new Response());
				objExitinguser.getObjResponse().setInformation("IDS_SETCONCURRENTLICENCE");
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
			objExitinguser.setIdletime(lockcount.getIdletime());
			objExitinguser.setIdletimeshowcheck(lockcount.getIdletimeshowcheck());

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
								objExitinguser.setLssitemaster(tempobj[0]);
								lSuserMasterRepository.save(objExitinguser);
								objExitinguser.setLssitemaster(multisitelogin[0]);
								objExitinguser.getObjResponse().setInformation("ID_EXPIRY");
								objExitinguser.getObjResponse().setStatus(false);

								obj.put("user", objExitinguser);
								return obj;
							} else {
								objExitinguser.getObjResponse().setStatus(true);
								objExitinguser.setLockcount(0);
								objExitinguser.setLssitemaster(tempobj[0]);
								lSuserMasterRepository.save(objExitinguser);
								objExitinguser.setLssitemaster(multisitelogin[0]);
							}
						} else {
							objExitinguser.getObjResponse().setStatus(true);
							objExitinguser.setLockcount(0);
							objExitinguser.setLssitemaster(tempobj[0]);
							lSuserMasterRepository.save(objExitinguser);
							objExitinguser.setLssitemaster(multisitelogin[0]);
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
							objExitinguser.setLssitemaster(tempobj[0]);
							lSuserMasterRepository.save(objExitinguser);
							objExitinguser.setLssitemaster(multisitelogin[0]);
						} else if (count < lockcount.getLockpolicy()) {
							objExitinguser.setLockcount(count++);
							objExitinguser.getObjResponse().setInformation("ID_INVALID");
							objExitinguser.getObjResponse().setStatus(false);
							objExitinguser.setLssitemaster(tempobj[0]);
							lSuserMasterRepository.save(objExitinguser);
							objExitinguser.setLssitemaster(multisitelogin[0]);
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
		if (objExitinguser.getObjResponse().getStatus() == true) {
			LSuserMaster objUser = lsuserMasterRepository.findByusercode(objExitinguser.getUsercode());
			if (objUser != null) {
				LSactiveUser activeUser = new LSactiveUser();
				objExitinguser.setLssitemaster(objExitinguser.getLssitemaster());
				try {
					activeUser.setTimestamp(commonfunction.getCurrentUtcTime());
					activeUser.setClientname(null);
					activeUser.setLastactivetime(commonfunction.getCurrentUtcTime());
//					activeUser.setLssitemaster(objExitinguser.getLssitemaster());
					activeUser.setLssitemaster(tempobj[0]);
					activeUser.setLsusermaster(objUser);
					objUser.setLastloggedon(commonfunction.getCurrentUtcTime());
					activeUser.setUsergroupcode(objuser.getLsusergroup().getUsergroupcode());
					activeUser.setUsergroupname(objuser.getLsusergroup().getUsergroupname());
				
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				objUser.setLssitemaster(tempobj[0]);
				lsuserMasterRepository.save(objUser);
				lsactiveUserRepository.save(activeUser);
				activeUser.setLssitemaster(multisitelogin[0]);
				objUser.setLssitemaster(multisitelogin[0]);
				obj.put("activeUserId", activeUser);
			}
		}
		try {
			obj.put("Logintime", commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj.put("user", objExitinguser);

		LSpreferences IsRegulated = LSpreferencesRepository.findByTasksettings("RegulatedIndustry");
		obj.put("IsRegulated", IsRegulated);
		return obj;
	}
	
	@SuppressWarnings("unused")
	public Map<String, Object> LoginonIOT(LoggedUser objuser) {
		Map<String, Object> obj = new HashMap<>();
		LSuserMaster objExitinguser = new LSuserMaster();
		String username = objuser.getsUsername();	
		LSSiteMaster objsiteobj = lSSiteMasterRepository.findBysitecode(Integer.parseInt(objuser.getsSiteCode()));
		List<LSMultisites> objformultisite = LSMultisitesRepositery.findByLssiteMaster(objsiteobj);
		List<Integer> usercode = objformultisite.stream().map(LSMultisites::getUsercode).collect(Collectors.toList());
		List<LSuserMaster> userobj = lSuserMasterRepository
				.findByUsernameIgnoreCaseAndUsercodeInAndLoginfromAndUserretirestatusNot(username, usercode, "0", 1);
		LSSiteMaster[] tempobj = { null };
		LSSiteMaster[] multisitelogin = { null };
		if (!userobj.isEmpty()) {
			userobj.forEach(items -> {
				items.getLsmultisites().forEach(values -> {

					if (values.getLssiteMaster().getSitecode() == Integer.parseInt(objuser.getsSiteCode())) {
						tempobj[0] = items.getLssitemaster();
						multisitelogin[0] = values.getLssiteMaster();
						items.setLssitemaster(values.getLssiteMaster());
					}
				});
			});
			objExitinguser = userobj.get(0);
		}

		// kumaresan for multisite purpose
//		objExitinguser = lSuserMasterRepository.findByUsernameIgnoreCaseAndLssitemasterAndLoginfrom(username,
//				objsiteobj, "0");
		LSPasswordPolicy lockcount = objExitinguser != null
				? LSPasswordPolicyRepository
						.findTopByAndLssitemasterOrderByPolicycodeDesc(objExitinguser.getLssitemaster())
				: null;

		LSpreferences objPrefrence = LSpreferencesRepository.findByTasksettingsAndValuesettings("ConCurrentUser",
				"Active");

		if (objPrefrence != null) {

			List<LSactiveUser> lstActUsrs = lsactiveUserRepository.findAll();
			String dvalue = objPrefrence.getValueencrypted();

			if (dvalue != null) {
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
			} else {
				objExitinguser.setObjResponse(new Response());
				objExitinguser.getObjResponse().setInformation("IDS_SETCONCURRENTLICENCE");
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
			objExitinguser.setIdletime(lockcount.getIdletime());
			objExitinguser.setIdletimeshowcheck(lockcount.getIdletimeshowcheck());

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
								objExitinguser.setLssitemaster(tempobj[0]);
								lSuserMasterRepository.save(objExitinguser);
								objExitinguser.setLssitemaster(multisitelogin[0]);
								objExitinguser.getObjResponse().setInformation("ID_EXPIRY");
								objExitinguser.getObjResponse().setStatus(false);

								obj.put("user", objExitinguser);
								return obj;
							} else {
								objExitinguser.getObjResponse().setStatus(true);
								objExitinguser.setLockcount(0);
								objExitinguser.setLssitemaster(tempobj[0]);
								lSuserMasterRepository.save(objExitinguser);
								objExitinguser.setLssitemaster(multisitelogin[0]);
							}
						} else {
							objExitinguser.getObjResponse().setStatus(true);
							objExitinguser.setLockcount(0);
							objExitinguser.setLssitemaster(tempobj[0]);
							lSuserMasterRepository.save(objExitinguser);
							objExitinguser.setLssitemaster(multisitelogin[0]);
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
							objExitinguser.setLssitemaster(tempobj[0]);
							lSuserMasterRepository.save(objExitinguser);
							objExitinguser.setLssitemaster(multisitelogin[0]);
						} else if (count < lockcount.getLockpolicy()) {
							objExitinguser.setLockcount(count++);
							objExitinguser.getObjResponse().setInformation("ID_INVALID");
							objExitinguser.getObjResponse().setStatus(false);
							objExitinguser.setLssitemaster(tempobj[0]);
							lSuserMasterRepository.save(objExitinguser);
							objExitinguser.setLssitemaster(multisitelogin[0]);
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
		if (objExitinguser.getObjResponse().getStatus() == true) {
			LSuserMaster objUser = lsuserMasterRepository.findByusercode(objExitinguser.getUsercode());
			if (objUser != null) {
				LSactiveUser activeUser = new LSactiveUser();
				objExitinguser.setLssitemaster(objExitinguser.getLssitemaster());
				try {
					activeUser.setTimestamp(commonfunction.getCurrentUtcTime());
					activeUser.setClientname(null);
					activeUser.setLastactivetime(commonfunction.getCurrentUtcTime());
//					activeUser.setLssitemaster(objExitinguser.getLssitemaster());
					activeUser.setLssitemaster(tempobj[0]);
					activeUser.setLsusermaster(objUser);
					objUser.setLastloggedon(commonfunction.getCurrentUtcTime());
					activeUser.setUsergroupcode(objuser.getLsusergroup().getUsergroupcode());
					activeUser.setUsergroupname(objuser.getLsusergroup().getUsergroupname());
				
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				objUser.setLssitemaster(tempobj[0]);
				lsuserMasterRepository.save(objUser);
				lsactiveUserRepository.save(activeUser);
				activeUser.setLssitemaster(multisitelogin[0]);
				objUser.setLssitemaster(multisitelogin[0]);
				obj.put("activeUserId", activeUser);
			}
		}
		try {
			obj.put("Logintime", commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj.put("user", objExitinguser);

		LSpreferences IsRegulated = LSpreferencesRepository.findByTasksettings("RegulatedIndustry");
		obj.put("IsRegulated", IsRegulated);
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
		List<LSMultisites> obj = LSMultisitesRepositery.findByLssiteMaster(objsite);
		List<Integer> usercode = obj.stream().map(LSMultisites::getUsercode).collect(Collectors.toList());
		objExitinguser = lSuserMasterRepository
				.findByUsernameIgnoreCaseAndUsercodeInAndLoginfromAndUserretirestatusNot(username, usercode, "0", 1);
		objExitinguser.stream().map(items -> {
			items.setMultiusergroupcode(items.getMultiusergroupcode().stream().filter(
					values -> values.getLsusergroup().getLssitemaster() == Integer.parseInt(objuser.getsSiteCode()))
					.collect(Collectors.toList()));
			return items;
		}).collect(Collectors.toList());

		// objExitinguser = lSuserMasterRepository
//				.findByUsernameIgnoreCaseAndLssitemasterAndLoginfromAndUserretirestatusNot(username, objsite, "0", 1);
		if (objExitinguser.size() != 0) {
			String Password = AESEncryption.decrypt(objExitinguser.get(0).getPassword());
			objExitinguser.get(0).setObjResponse(new Response());
			Integer Isads_User = objExitinguser.get(0).getIsadsuser();
			if (Password == null && (Isads_User==null || Isads_User==0) ) {
//			if (Password == null) {
				objExitinguser.get(0).getObjResponse().setInformation("GenerateNewPassword");
				objExitinguser.get(0).getObjResponse().setStatus(true);

			} else {
				if(objExitinguser.get(0).getUserstatus().equals("Locked")) {
					objExitinguser.get(0).getObjResponse().setInformation("user locked");
					objExitinguser.get(0).getObjResponse().setStatus(false);
				}else {
					objExitinguser.get(0).getObjResponse().setInformation("Valid user and password exist");
					objExitinguser.get(0).getObjResponse().setStatus(true);
				}
			}
		} else {

			objExitinguser = lSuserMasterRepository
					.findByUsernameIgnoreCaseAndLoginfromAndUserretirestatusNotOrderByCreateddateDesc(username, "0", 1);

			if (objExitinguser.size() != 0) {
//				objExitinguser = new LSuserMaster();
//				objExitinguser.get(0).setUserstatus("");
				objExitinguser.get(0).setObjResponse(new Response());
				objExitinguser.get(0).getObjResponse().setInformation("User is not present on the site.");
				objExitinguser.get(0).getObjResponse().setStatus(false);

			} else {
				LSuserMaster objExitinguser1 = new LSuserMaster();
//				objExitinguser1.setUserstatus("");
				objExitinguser1.setObjResponse(new Response());
				objExitinguser1.getObjResponse().setInformation("Invalid user");
				objExitinguser1.getObjResponse().setStatus(false);
				objExitinguser.add(objExitinguser1);
			}
		}

//		objExitinguser.stream()
//        .peek(items -> {
//            if (items.getDefaultsiteMaster() != null) {
//                items.getLssiteMaster().setDefaultsiteMaster(items.getDefaultsiteMaster());
//            }
//        })
//        .map(LSMultiusergroup::getLsusergroup)
//        .filter(LSusergroup -> newpassword.equals(LSPasswordHistoryDetails.getPassword()))
//        .collect(Collectors.toList());

		return objExitinguser;
	}

	@SuppressWarnings({ "unused" })
	public LSuserMaster UpdatePassword(LoggedUser objuser) {
		LSuserMaster objExitinguser = new LSuserMaster();
		String username = objuser.getsUsername();
		
		LSSiteMaster objsiteobj = lSSiteMasterRepository.findBysitecode(Integer.parseInt(objuser.getsSiteCode()));
		List<LSMultisites> objformultisite = LSMultisitesRepositery.findByLssiteMaster(objsiteobj);
		List<Integer> usercode = objformultisite.stream().map(LSMultisites::getUsercode).collect(Collectors.toList());
		List<LSuserMaster> userobj = lSuserMasterRepository
				.findByUsernameIgnoreCaseAndUsercodeInAndLoginfromAndUserretirestatusNot(username, usercode, "0", 1);
		LSSiteMaster[] tempobj = { null };
		LSSiteMaster[] multisitelogin = { null };
		if (!userobj.isEmpty()) {
			userobj.forEach(items -> {
				items.getLsmultisites().forEach(values -> {

					if (values.getLssiteMaster().getSitecode() == Integer.parseInt(objuser.getsSiteCode())) {
						tempobj[0] = items.getLssitemaster();
						multisitelogin[0] = values.getLssiteMaster();
						items.setLssitemaster(values.getLssiteMaster());
					}
				});
			});
			objExitinguser = userobj.get(0);
		}
	
		
		//objExitinguser = lSuserMasterRepository.findByusernameIgnoreCaseAndLssitemaster(username, objsiteobj);
		List<LSPasswordHistoryDetails> listofpwd = new ArrayList<LSPasswordHistoryDetails>();
		LSPasswordHistoryDetails objectpwd = new LSPasswordHistoryDetails();
		List<LSPasswordHistoryDetails> result = new ArrayList<LSPasswordHistoryDetails>();
		LSPasswordPolicy passHistorycount = null ;
		if(objExitinguser != null) {
			passHistorycount = LSPasswordPolicyRepository
				.findFirst1ByLssitemaster(objsiteobj);
		}
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
							"Entered password has been already used by the user " + objuser.getsUsername());
					objuser.getObjsilentaudit().setManipulatetype("Password");
					objuser.getObjsilentaudit().setTableName("LSPasswordHistoryDetails");
					try {
						objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
						try {
							objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
					try {
						objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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

		if (lsuserMaster.getActiveusercode() != null) {
			lsactiveUserRepository.deleteByActiveusercode(lsuserMaster.getActiveusercode());

			removeOrdersOnInActive(lsuserMaster.getActiveusercode());
		} else {
			lsactiveUserRepository.deleteBylsusermaster(lsuserMaster);
		}
		return true;
	}

	public void removeOrdersOnInActive(Integer activeuser) {
		List<LSlogilablimsorderdetail> lsOrder = lslogilablimsorderdetailRepository.findByActiveuser(activeuser);

		if (!lsOrder.isEmpty()) {
			lsOrder = lsOrder.stream().peek(f -> {
				f.setLockeduser(null);
				f.setLockedusername(null);
				f.setActiveuser(null);
			}).collect(Collectors.toList());
			lslogilablimsorderdetailRepository.save(lsOrder);
		}
	}

	public void removeOrdersOnInActiveLst(List<Integer> activeuser) {
		List<LSlogilablimsorderdetail> lsOrder = lslogilablimsorderdetailRepository.findByActiveuserIn(activeuser);

		if (!lsOrder.isEmpty()) {
			lsOrder = lsOrder.stream().peek(f -> {
				f.setLockeduser(null);
				f.setLockedusername(null);
				f.setActiveuser(null);
			}).collect(Collectors.toList());
			lslogilablimsorderdetailRepository.save(lsOrder);
		}
	}
	public void removeOrdersOnInActiveLstforprotocolorders(List<Integer> activeuser) {
		List<LSlogilabprotocoldetail> lsOrder = lslogilabprotocoldetailRepository.findByActiveuserIn(activeuser);
		
		if (!lsOrder.isEmpty()) {
			lsOrder = lsOrder.stream().peek(f -> {
				f.setLockeduser(null);
				f.setLockedusername(null);
				f.setActiveuser(null);
			}).collect(Collectors.toList());
			lslogilabprotocoldetailRepository.save(lsOrder);
		}
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
				objExitinguser.getObjResponse().setInformation("IDS_MSG_EXIST");
				objExitinguser.getObjResponse().setStatus(false);
				if (objuser.getObjsilentaudit() != null) {
					objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
					objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
					objuser.getObjsilentaudit()
							.setComments(objuser.getsUsername() + " " + "entered existing password incorrectly");
					objuser.getObjsilentaudit().setManipulatetype("Password");
					objuser.getObjsilentaudit().setTableName("LSuserMaster");
					try {
						objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
							"Entered password has been already used by the user " + objuser.getsUsername());

					objuser.getObjsilentaudit().setManipulatetype("Password");
					objuser.getObjsilentaudit().setTableName("LSPasswordHistoryDetails");
					try {
						objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					lscfttransactionRepository.save(objuser.getObjsilentaudit());

				}
				return objExitinguser;
			}
			if (objuser.getsNewPassword().equals(objuser.getsConfirmPassword())) {

				objExitinguser.setPassword(AESEncryption.encrypt(objuser.getsNewPassword()));
				if (objuser.getPasswordexpirydate() != null) {
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
					try {
						objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
//					int sApprove = Integer.parseInt((String) lstAdsUsers.get(u).get("sApprove"));
//
//					String sUserStatus = "D";
//					if (sApprove == 1)
//						sUserStatus = "A";

					lsUser.setCreatedby(sCreateBy);
					lsUser.setCreateddate(new Date());
					lsUser.setLockcount(0);
					lsUser.setUserfullname(sUserFullName);
					lsUser.setUsername(sUserDomainID);
//					lsUser.setUserstatus(sUserStatus);
					lsUser.setLssitemaster(sSiteCode);
					lsUser.setPasswordstatus(0);
					lsUser.setUserretirestatus(0);
					lsUser.setIsadsuser(1);
					lsUser.setUserstatus("A");

					List<LSMultiusergroup> lstGroup = new ArrayList<LSMultiusergroup>();
					

					LSMultiusergroup objGroup = new LSMultiusergroup();
					objGroup.setDefaultusergroup(1);
					objGroup.setLsusergroup(userGroup);

					lstGroup.add(objGroup);

					lsUser.setMultiusergroupcode(lstGroup);
					
					LSMultiusergroupRepositery.save(lsUser.getMultiusergroupcode());
					lsuserMasterRepository.save(lsUser);
					
					List<LSMultisites> lstmsiteGroup = new ArrayList<LSMultisites>();
					
					LSMultisites objmsiteGroup = new LSMultisites();
					objmsiteGroup.setDefaultsiteMaster(siteCode);
					objmsiteGroup.setUsercode(lsUser.getUsercode());
					objmsiteGroup.setLssiteMaster(sSiteCode);
					
					lstmsiteGroup.add(objmsiteGroup);
					LSMultisitesRepositery.save(lstmsiteGroup);
					

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
			if (lstUsers.size() > 0 ) {
//				lSuserMasterRepository.save(lstUsers);

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
		return lSDomainMasterRepository.findBylssitemasterOrderByDomaincodeDesc(objsite);
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
		if ((objClass.getSitecode() == null || objClass.getSitecode() != null)
				&& lSSiteMasterRepository.findBySitenameIgnoreCaseAndIstatus(objClass.getSitename(), 1).size() > 0) {
			if (objClass.getIstatus() != 0) {
				// lSSiteMasterRepository.save(objClass);

				objClass.getResponse().setStatus(false);
				objClass.getResponse().setInformation("ID_EXIST");

				return objClass;
			}
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
		LSSiteMaster objsite  = lSSiteMasterRepository.findBysitecode(Integer.parseInt(objuser.getsSiteCode())); 
//		new LSSiteMaster(Integer.parseInt(objuser.getsSiteCode()));
		
		
		List < LSMultisites > objformultisite = LSMultisitesRepositery.findByLssiteMaster(objsite);
		List < Integer > usercode = objformultisite.stream().map(LSMultisites:: getUsercode).collect(Collectors.toList());
		List < LSuserMaster > userobj = lSuserMasterRepository
		    .findByUsernameIgnoreCaseAndUsercodeInAndLoginfromAndUserretirestatusNot(username,
		        usercode, "1", 1);
		
		
//		objExitinguser = lSuserMasterRepository.findByUsernameIgnoreCaseAndLoginfromAndLssitemaster(username, "1",
//				objsite);
		if (!userobj.isEmpty()) {
//			List<LSMultiusergroup> objGrouplist = userobj.stream()
//				    .flatMap(items -> items.getMultiusergroupcode().stream()
//				        .filter(values -> values.getLsusergroup().getLssitemaster() == objsite.getSitecode())
//				    )
//				    .collect(Collectors.toList());
//			if(objGrouplist == null) {
//				 String str = "IDS_MSG_NOMULTIUSERGROUP";
//			        obj.put("IDS_MSG_NOMULTIUSERGROUP", str);
//			        return obj;
//			}
			objExitinguser=userobj.get(0);
			objExitinguser.setLssitemaster(objsite);
			LSPasswordPolicy lockcount = objExitinguser != null
					? LSPasswordPolicyRepository
							.findTopByAndLssitemasterOrderByPolicycodeDesc(objExitinguser.getLssitemaster())
					: null;
//		if (objExitinguser != null) {
			objExitinguser.setObjResponse(new Response());
			objExitinguser.setObjsilentaudit(new LScfttransaction());
			objExitinguser.setIdletime(lockcount.getIdletime());
			objExitinguser.setIdletimeshowcheck(lockcount.getIdletimeshowcheck());
			if ((Integer.parseInt(objuser.getsSiteCode()) == objExitinguser.getLssitemaster().getSitecode())
					&& objExitinguser.getUserretirestatus() == 0) {

				if (objExitinguser.getUserstatus() != "Locked") {

					String status = objExitinguser.getUserstatus();

//			    	if(objExitinguser.getLsusergroup() == null)
//			    	{
					List<LSMultiusergroup> LSMultiusergroup = new ArrayList<LSMultiusergroup>();
					LSMultiusergroup = LSMultiusergroupRepositery.findByusercode(objExitinguser.getUsercode());
					List<LSMultiusergroup> objGroup = objExitinguser.getMultiusergroupcode().stream()
							.filter(obj1 -> (obj1.getDefaultusergroup() != null && obj1.getDefaultusergroup() == 1))
							.collect(Collectors.toList());
					if (objGroup.isEmpty()) {
						obj.put("multiusergroupcode",
								objExitinguser.getMultiusergroupcode().get(0).getLsusergroup().getUsergroupcode());
						objExitinguser.setLsusergroup(LSMultiusergroup.get(0).getLsusergroup());
					} else {
						obj.put("multiusergroupcode", objGroup.get(0).getLsusergroup().getUsergroupcode());
						objExitinguser.setLsusergroup(objGroup.get(0).getLsusergroup());
					}

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
						try {
							objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
						try {
							objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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

							dateSString = "MM-dd-yyyy hh:mm:ss";
							objExitinguser.setDFormat(dateSString);
						} catch (ParseException e) {
							e.printStackTrace();
						}

						objExitinguser.getObjResponse().setStatus(true);

						if (objuser.getObjsilentaudit() != null) {
							LScfttransaction lScfttransaction = new LScfttransaction();
							lScfttransaction.setLsuserMaster(objExitinguser.getUsercode());
							lScfttransaction.setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
							lScfttransaction.setModuleName(ModuleName);
							lScfttransaction.setComments("User Logged in Successfully");
							lScfttransaction.setActions("IDS_TSK_LOGIN");
							lScfttransaction.setSystemcoments("System Generated");
							lScfttransaction.setManipulatetype("Login");
							lScfttransaction.setTableName("LSactiveuser");
							try {
								lScfttransaction.setTransactiondate(commonfunction.getCurrentUtcTime());
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							lscfttransactionRepository.save(lScfttransaction);

						}

						if (objExitinguser.getObjResponse().getStatus() == true) {
							LSuserMaster objUser = lsuserMasterRepository.findByusercode(objExitinguser.getUsercode());
							if (objUser != null) {
								LSactiveUser activeUser = new LSactiveUser();
								objExitinguser.setLssitemaster(objExitinguser.getLssitemaster());
								try {
									activeUser.setTimestamp(commonfunction.getCurrentUtcTime());
									activeUser.setClientname(null);
									activeUser.setLastactivetime(commonfunction.getCurrentUtcTime());
									activeUser.setLssitemaster(objExitinguser.getLssitemaster());
									activeUser.setLsusermaster(objUser);
									objUser.setLastloggedon(commonfunction.getCurrentUtcTime());
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								lsuserMasterRepository.save(objUser);
								lsactiveUserRepository.save(activeUser);
								obj.put("activeUserId", activeUser);
							}
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
				try {
					objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				try {
					objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
			try {
				objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(objuser.getObjsilentaudit());

		}

		obj.put("user", objExitinguser);
		if (objExitinguser.getLsusergroup() != null) {
			obj.put("userrights", userService.GetUserRightsonGroup(objExitinguser.getLsusergroup()));
			LSaudittrailconfiguration objauditconfig = new LSaudittrailconfiguration();
			objauditconfig.setLsusermaster(objExitinguser);
			obj.put("auditconfig", auditService.GetAuditconfigUser(objauditconfig.getLsusermaster()));
//			obj.put("multiusergroupcode",
//					objExitinguser.getMultiusergroupcode().get(0).getLsusergroup().getUsergroupcode());
		}
		try {
			obj.put("Logintime", commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;

	}

	public LSuserMaster createuserforazure(LSuserMaster objuser) {
		
		List<LSMultisites> objformultisite = LSMultisitesRepositery.findByLssiteMaster(objuser.getLssitemaster());
		List<Integer> usercode = objformultisite.stream().map(LSMultisites::getUsercode).collect(Collectors.toList());
		List<LSuserMaster> userobj = lSuserMasterRepository
				.findByUsernameIgnoreCaseAndUsercodeInAndLoginfromAndUserretirestatusNot(objuser.getUsername(),
						usercode, "1", 1);
//		LSuserMaster userDetails = lsuserMasterRepository.findByUsernameIgnoreCaseAndLoginfromAndLssitemaster(
//				objuser.getUsername(), "1", objuser.getLssitemaster());

//		if (userDetails == null) {
		if (userobj.isEmpty()) {
			if (objuser.getIsmultitenant() != null && objuser.getMultitenantusercount() != null
					&& objuser.getIsmultitenant() == 1) {
//				if(lsuserMasterRepository.countByusercodeNot(1) >= objuser.getMultitenantusercount())
				if (lsuserMasterRepository.countByusercodeNotAndUserretirestatusNot(1, 1) >= objuser
						.getMultitenantusercount()) {
					Response objResponse = new Response();
					objResponse.setStatus(false);
					objResponse.setInformation("IDS_MSG_USERCOUNTEXCEEDS");
					objuser.setObjResponse(objResponse);

					return objuser;
				}
			}
			LSusergroup objaadsgroup = LSusergroupRepository.findByusergroupnameAndLssitemaster("Azure aads",
					objuser.getLssitemaster().getSitecode());
			LSusergroup objgroup = new LSusergroup();
			LSMultiusergroup LSMultiusergroup = new LSMultiusergroup();
			LSMultisites multisiteobj = new LSMultisites();
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
				LSMultiusergroup.setDefaultusergroup(1);
			} else {
				objuser.setLsusergroup(objaadsgroup);
//				LSMultiusergroup.setDefaultusergroup(objaadsgroup.getUsergroupcode());
				LSMultiusergroup.setDefaultusergroup(1);
				LSMultiusergroup.setLsusergroup(objaadsgroup);
			}

			multisiteobj.setLssiteMaster(objuser.getLssitemaster());
			multisiteobj.setDefaultsiteMaster(1);
//			multisiteobj.setUsercode();

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
			List<LSMultisites> lstmultisites = new ArrayList<LSMultisites>();
			lstmultisites.add(multisiteobj);
			LSMultiusergroup1.add(LSMultiusergroup);
			objuser.setLsmultisites(lstmultisites);
			objuser.setMultiusergroupcode(LSMultiusergroup1);
			LSMultisitesRepositery.save(objuser.getLsmultisites());
			LSMultiusergroupRepositery.save(objuser.getMultiusergroupcode());
			lsuserMasterRepository.save(objuser);

			String unifieduser = objuser.getUsername().toLowerCase().replaceAll("[^a-zA-Z0-9]", "") + "u"
					+ objuser.getUsercode() + "s" + objuser.getLssitemaster().getSitecode()
					+ objuser.getUnifieduserid();

			objuser.setUnifieduserid(unifieduser);
			lsuserMasterRepository.save(objuser);
		} else {
			LSuserMaster userDetails = userobj.get(0);
			userDetails.setLssitemaster(lSSiteMasterRepository.findBysitecode(objuser.getLssitemaster().getSitecode()));
			objuser = userDetails;
			Response objResponse = new Response();
			objResponse.setStatus(true);
			objResponse.setInformation("");
			objuser.setUnifieduserid(userDetails.getUnifieduserid());
			objuser.setObjResponse(objResponse);
		}

		return objuser;
	}

	@SuppressWarnings("unused")
	public ResponseEntity<?> azureusertokengenrate(LSuserMaster objuser) throws Exception {

		if (objuser.getUsername() == null)
			return null;
		
		Map<String, Object> obj = new HashMap<>();
		LSuserMaster userDetails = lsuserMasterRepository.findTop1ByUsernameIgnoreCaseAndLoginfromAndLssitemaster(
				objuser.getUsername(), "1", objuser.getLssitemaster());

		if(userDetails != null)
		{
			LSPasswordPolicy policydays = LSPasswordPolicyRepository.findTopByAndLssitemasterOrderByPolicycodeDesc(objuser.getLssitemaster());
			if (policydays == null) {
				LSSiteMaster lssitemaster = new LSSiteMaster();
				lssitemaster.setSitecode(1);
				LSPasswordPolicy lspasswordPolicy = LSPasswordPolicyRepository.findFirst1ByLssitemaster(lssitemaster);
	
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
		else
		{
			Response res = new Response();
			res.setStatus(false);
			res.setInformation("User not registered in the selected site.");
			return ResponseEntity.ok(res);
		}
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
		LSSiteMaster siteobj = lSSiteMasterRepository.findBysitecode(lsuserMaster.getLssitemaster().getSitecode());
		int multiusergroupcode = lsuserMaster.getMultiusergroupcode().get(0).getMultiusergroupcode();
		LSMultiusergroup objLSMultiusergroup = LSMultiusergroupRepositery.findBymultiusergroupcode(multiusergroupcode);
		String groupstatus = objLSMultiusergroup.getLsusergroup().getUsergroupstatus();
//		List<LSMultisites> siteobj = LSMultisitesRepositery.findByLssiteMaster(lsuserMaster.getLssitemaster());
//		List<Integer> usercode = siteobj.stream().map(LSMultisites::getUsercode).collect(Collectors.toList());
//		List<LSuserMaster> userobj = lSuserMasterRepository.findByUsernameIgnoreCaseAndUsercodeInAndUserretirestatusNot(lsuserMaster.getUsername(), usercode,1);
//		LSuserMaster objExitinguser=userobj.get(0)
		LSuserMaster objExitinguser = lSuserMasterRepository.findByusercode(lsuserMaster.getUsercode());
		; // LSuserMaster objExitinguser = lSuserMasterRepository
//				.findByUsernameIgnoreCaseAndLssitemaster(lsuserMaster.getUsername(), lsuserMaster.getLssitemaster());
		objExitinguser.setLsusergroup(objLSMultiusergroup.getLsusergroup());
		objExitinguser.setObjResponse(new Response());
		objExitinguser.setLssitemaster(siteobj);

		if (objLSMultiusergroup != null) {

			if (lsuserMaster.getObjsilentaudit() != null) {
				lsuserMaster.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
				lsuserMaster.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
				lsuserMaster.getObjsilentaudit().setManipulatetype("Password");
				lsuserMaster.getObjsilentaudit().setTableName("LSuserMaster");
				try {
					lsuserMaster.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lscfttransactionRepository.save(lsuserMaster.getObjsilentaudit());

			}

			obj.put("user", objExitinguser);
			if (groupstatus.trim().equals("Active")) {
				if (objExitinguser.getLsusergroup() != null) {
					obj.put("userrights", userService.GetUserRightsonGroup(objExitinguser.getLsusergroup()));
					LSaudittrailconfiguration objauditconfig = new LSaudittrailconfiguration();
					objauditconfig.setLsusermaster(objExitinguser);
					obj.put("auditconfig", auditService.GetAuditconfigUser(objauditconfig.getLsusermaster()));

					obj.put("multiusergroupcode", objLSMultiusergroup.getLsusergroup().getUsergroupcode());
				}

				objExitinguser.getObjResponse().setInformation("usergroup switched successfully ");
				objExitinguser.getObjResponse().setStatus(true);
			} else {
				objExitinguser.getObjResponse().setInformation("ID_GRPNOACT");
				objExitinguser.getObjResponse().setStatus(false);
			}
		}

		return obj;
	}
	
//	public Notification OrdersAutoRegisterLogin(Notification objNotification) throws ParseException, SQLException, IOException {
//		
//		LSuserMaster LSuserMaster = new LSuserMaster(); /* to get the value */
//		LSuserMaster.setUsercode(objNotification.getUsercode());
//
////		List<LSlogilablimsorderdetail> orderobjdata = new ArrayList<LSlogilablimsorderdetail>();
////		orderobjdata=lslogilablimsorderdetailRepository.findByLsuserMasterAndRepeat(LSuserMaster,true);
////		try {
////			instrumentservice.InsertAutoRegisterOrder(orderobjdata);
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		instrumentservice.retrieveIntervalFromStorage();
//		return objNotification;
//	}
	
	// added for duedate notification 
	
	public LSOrdernotification mapResultSetToNotification(ResultSet rs) throws SQLException {
        LSOrdernotification ordernot = new LSOrdernotification();
        ordernot.setBatchcode(rs.getLong("batchcode"));
        ordernot.setBatchid(rs.getString("batchid"));
        ordernot.setCautiondate(rs.getTimestamp("cautiondate"));
        ordernot.setCautionstatus(rs.getInt("cautionstatus"));
        ordernot.setDuedate(rs.getTimestamp("duedate"));
        ordernot.setDuestatus(rs.getInt("duestatus"));
        ordernot.setIscompleted(rs.getBoolean("iscompleted"));
        ordernot.setNotificationcode(rs.getLong("notificationcode"));
        //ordernot.setOverduedays(rs.getString("overduedays"));
        ordernot.setOverduestatus(rs.getInt("overduestatus"));
        ordernot.setPeriod(rs.getString("period"));
        ordernot.setScreen(rs.getString("screen"));
       // ordernot.setSitecode(rs.getInt("sitecode"));
        ordernot.setUsercode(rs.getInt("usercode"));
        return ordernot;
    }
	
	
//	@Scheduled(fixedRate = 36000, initialDelay = 1000)
//	public void checkAndScheduleReminders() throws SQLException {
//		
//		List<Datasourcemaster> configList = configRepo.findByinitialize(true);
//		
//		LocalDateTime localDateTime = LocalDateTime.now();
//        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
//
//        Date fromDate = Date.from(instant);
//        Date toDate = Date.from(instant);
//        
//		Calendar calendar1 = Calendar.getInstance();
//		calendar1.setTime(fromDate);
//		fromDate = calendar1.getTime();
//		
//		Calendar calendar2 = Calendar.getInstance();
//		calendar2.setTime(toDate);
//		calendar2.add(Calendar.HOUR_OF_DAY, 1);
//		toDate = calendar2.getTime();
//		
//		if(!configList.isEmpty()) {
//			
//			for(Datasourcemaster objData : configList) {
//				
//				HikariConfig configuration = new HikariConfig();
//	            configuration.setDriverClassName("org.postgresql.Driver");
//	            configuration.setJdbcUrl(objData.getUrl());
//	            configuration.setUsername(objData.getUsername());
//	            configuration.setPassword(objData.getPassword());
//	            configuration.setMaximumPoolSize(10);
//	            configuration.setPoolName(objData.getUrl());
//	            configuration.setMinimumIdle(5);
//	            configuration.setConnectionTestQuery("SELECT 1");
//	            configuration.setConnectionTimeout(300000);
//	            configuration.setConnectionTimeout(120000);
//	            configuration.setLeakDetectionThreshold(300000);
//
//	            // Like this you can configure multiple properties here 
//				HikariDataSource dataSource = new HikariDataSource(configuration);
//	            
//	            Connection con = null;
//	            
//				try {
//					con = dataSource.getConnection();
//					
//					//String updateString ="SELECT * FROM lsordernotification WHERE cautionstatus = 1 and cautiondate BETWEEN '"+fromDate+"' AND '"+toDate+"';";
//				    
//					String updateString ="SELECT * FROM lsordernotification WHERE cautionstatus = 1";
//					   
//					Statement st = con.createStatement();
//					
//					ResultSet rs = st.executeQuery(updateString);
//					
//					while(rs.next()){
//						
//						LSOrdernotification objNotification = mapResultSetToNotification(rs);
//						
//						Date cautionDate = objNotification.getCautiondate();
//						Instant caution = cautionDate.toInstant();
//						
//						LocalDateTime cautionTime = LocalDateTime.ofInstant(caution, ZoneId.systemDefault());
//						
//						LocalDateTime currentTime = LocalDateTime.now();
//						
//						if(cautionTime.isAfter(currentTime) && rs != null) {
//							Duration duration = Duration.between(currentTime, cautionTime);
//							long delay = duration.toMillis();
//							try {
//								scheduleNotification(objNotification);
//							} catch (ParseException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//				    }
//					
//					con.close();
//					dataSource.close();
//		        	dataSource = null;
//				} catch (SQLException e) {
//				    
//				} finally {
//				    if (con != null) {
//				        try {
//				        	con.close();
//				        } catch (SQLException e) {
//				        }
//				    }
//				}
//			}
//		}
//	}

	
	public void ValidateNotification(LSOrdernotification objnotification) throws ParseException {
		lsordernotificationrepo.save(objnotification);
		scheduleNotificationduringregister(objnotification);
	}
	
	
	public void scheduleNotificationduringregister(LSOrdernotification objnotification) throws ParseException {
		
		Date cautionDate = objnotification.getCautiondate();
		Instant caution = cautionDate.toInstant();
		
		LocalDateTime cautionTime = LocalDateTime.ofInstant(caution, ZoneId.systemDefault());
		LocalDateTime currentTime = LocalDateTime.now();
	
		if(cautionTime.isAfter(currentTime) && objnotification != null) {
			Duration cautionduration = Duration.between(currentTime, cautionTime);
			long cautiondelay = cautionduration.toMillis();
			schedulecautionNotification(objnotification ,cautiondelay);
			
		}
		
		Date dueDate = objnotification.getDuedate();
		Instant due = dueDate.toInstant();
		
		LocalDateTime dueTime = LocalDateTime.ofInstant(due, ZoneId.systemDefault());
		if(dueTime.isAfter(currentTime) && objnotification != null) {
			Duration dueduration = Duration.between(currentTime, dueTime);
			long duedelay = dueduration.toMillis();
			scheduledueNotification(objnotification ,duedelay);
		}
		Date overdueDate = objnotification.getDuedate();
        Date copyOverdueDate = (Date) overdueDate.clone();

        copyOverdueDate.setMinutes(overdueDate.getMinutes()+5);
		Instant overdue = copyOverdueDate.toInstant();
	
		
		
		LocalDateTime overdueTime = LocalDateTime.ofInstant(overdue, ZoneId.systemDefault());
		if(overdueTime.isAfter(currentTime) && objnotification != null) {
			Duration overdueduration = Duration.between(currentTime, overdueTime);
			long overduedelay = overdueduration.toMillis();
			scheduleoverdueNotification(objnotification ,overduedelay);
		}
	}

	private void schedulecautionNotification(LSOrdernotification objNotification , long delay) {
	
		if(objNotification.getIscompleted() == null || objNotification.getIscompleted() == false){
			TimerTask task = new TimerTask() {
				@SuppressWarnings("unlikely-arg-type")
				public void run() {
					try {
						cautiondatenotification(objNotification);
					} catch (Exception e) {
						e.printStackTrace();
					}
					scheduledTasks.remove(objNotification.getNotificationcode());
				}
			};
			Timer timer = new Timer();
			timer.schedule(task, delay);
			scheduledTasks.put(Integer.parseInt(objNotification.getNotificationcode().toString()), task);
		}
	}
	
	private void scheduledueNotification(LSOrdernotification objNotification , long delay) {
		if(objNotification.getIscompleted() == null || objNotification.getIscompleted() == false){
			TimerTask task = new TimerTask() {
				@SuppressWarnings("unlikely-arg-type")
				public void run() {
					try {
						duedatenotification(objNotification);
					} catch (Exception e) {
						e.printStackTrace();
					}
					scheduledTasks.remove(objNotification.getNotificationcode());
				}
			};
			Timer timer = new Timer();
			timer.schedule(task, delay);
			scheduledTasks.put(Integer.parseInt(objNotification.getNotificationcode().toString()), task);
		}
	}
	
	private void scheduleoverdueNotification(LSOrdernotification objNotification , long delay) {
		if(objNotification.getIscompleted() == null || objNotification.getIscompleted() == false){
			TimerTask task = new TimerTask() {
				@SuppressWarnings("unlikely-arg-type")
				public void run() {
					try {
						overduenotification(objNotification);
					} catch (Exception e) {
						e.printStackTrace();
					}
					scheduledTasks.remove(objNotification.getNotificationcode());
				}
			};
			Timer timer = new Timer();
			timer.schedule(task, delay);
			scheduledTasks.put(Integer.parseInt(objNotification.getNotificationcode().toString()), task);
		}
	}
	
	private Map<Integer, TimerTask> scheduledTasks = new HashMap<>();

	public void duedatenotification(LSOrdernotification objNotification) throws ParseException {
		
		LSOrdernotification notobj = lsordernotificationrepo.findByBatchcodeAndScreen(objNotification.getBatchcode(), objNotification.getScreen());	
		LSlogilablimsorderdetail order = null;
		LSlogilabprotocoldetail protocolorder = null;
		
		if(objNotification.getScreen().equals("sheetorder")) {
		    order = lslogilablimsorderdetailRepository.findByBatchcodeOrderByBatchcodeDesc(objNotification.getBatchcode());
		}else {
		    protocolorder = lslogilabprotocoldetailRepository.findByProtocolordercode(objNotification.getBatchcode());
		}
		
		int cancel;
		int approvelstatus;
		
		List<LSOrdernotification> ordernotifylist = new ArrayList<LSOrdernotification>();
		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
	
		LSuserMaster assigneduser = new LSuserMaster();
		
		Date dueDate = objNotification.getDuedate();
		Instant due = dueDate.toInstant();
		
		LocalDateTime dueTime = LocalDateTime.ofInstant(due, ZoneId.systemDefault());
		LocalDate duedate = dueTime.toLocalDate();
		
		LSnotification LSnotification = new LSnotification();
		
		String Details = "{\"ordercode\" :\"" + objNotification.getBatchcode() 
	    + "\",\"order\" :\"" + objNotification.getBatchid() 
	    + "\",\"date\" :\"" + duedate 
	    + "\",\"screen\":\"" + objNotification.getScreen() 
		+ "\"}";
		
		String path = objNotification.getScreen().equals("sheetorder") ? "/registertask" : "/Protocolorder";
		
		
		if(order==null) {
			 cancel = protocolorder.getOrdercancell() == null ? 0 : protocolorder.getOrdercancell();
			 approvelstatus = protocolorder.getApprovelstatus()== null ? 0 :protocolorder.getApprovelstatus();
			 assigneduser=protocolorder.getAssignedto();
			   if(protocolorder.getAssignedto()!=null) {
				   LSnotification.setIsnewnotification(1);
					LSnotification.setNotification("ORDERONDUEALERT");
					LSnotification.setNotificationdate(new Date());
					LSnotification.setNotificationdetils(Details);
					LSnotification.setNotificationpath(path);
					LSnotification.setNotifationfrom(assigneduser);
					LSnotification.setNotifationto(assigneduser);
					LSnotification.setRepositorycode(0);
					LSnotification.setRepositorydatacode(0);
					LSnotification.setNotificationfor(1);
					lstnotifications.add(LSnotification);	
			   }
		}else {
		   cancel = order.getOrdercancell() == null ? 0 : order.getOrdercancell();
		   approvelstatus = order.getApprovelstatus()== null ? 0 :order.getApprovelstatus();
		   
		   assigneduser=order.getAssignedto();
		   if(order.getAssignedto()!=null) {
			   LSnotification.setIsnewnotification(1);
				LSnotification.setNotification("ORDERONDUEALERT");
				LSnotification.setNotificationdate(new Date());
				LSnotification.setNotificationdetils(Details);
				LSnotification.setNotificationpath(path);
				LSnotification.setNotifationfrom(assigneduser);
				LSnotification.setNotifationto(assigneduser);
				LSnotification.setRepositorycode(0);
				LSnotification.setRepositorydatacode(0);
				LSnotification.setNotificationfor(1);
				lstnotifications.add(LSnotification);	
		   }
		}
		 
		if((notobj.getIscompleted() == null || notobj.getIscompleted() == false) && 
				(cancel == 0) && (approvelstatus != 3)){
			
			LSuserMaster LSuserMaster = new LSuserMaster();
			LSuserMaster.setUsercode(objNotification.getUsercode());
			
			
					if(objNotification.getDuestatus() == 1) {
					        LSnotification LSnotification1 = new LSnotification();
	
							LSnotification1.setIsnewnotification(1);
							LSnotification1.setNotification("ORDERONDUEALERT");
							LSnotification1.setNotificationdate(new Date());
							LSnotification1.setNotificationdetils(Details);
							LSnotification1.setNotificationpath(path);
							LSnotification1.setNotifationfrom(LSuserMaster);
							LSnotification1.setNotifationto(LSuserMaster);
							LSnotification1.setRepositorycode(0);
							LSnotification1.setRepositorydatacode(0);
							LSnotification1.setNotificationfor(1);
		
							objNotification.setDuestatus(0);
							ordernotifylist.add(objNotification);
							lstnotifications.add(LSnotification1);				
					        }
					
		
						LSnotificationRepository.save(lstnotifications);
						lsordernotificationrepo.save(ordernotifylist);
						notifyoverduedays(objNotification);
						
		}
	}

	public void overduenotification(LSOrdernotification objNotification) throws ParseException {
		
		LSOrdernotification notobj = lsordernotificationrepo.findByBatchcodeAndScreen(objNotification.getBatchcode(), objNotification.getScreen());	
		LSlogilablimsorderdetail order = null;
		LSlogilabprotocoldetail protocolorder = null;
		
		if(objNotification.getScreen().equals("sheetorder")) {
		    order = lslogilablimsorderdetailRepository.findByBatchcodeOrderByBatchcodeDesc(objNotification.getBatchcode());
		}else {
		    protocolorder = lslogilabprotocoldetailRepository.findByProtocolordercode(objNotification.getBatchcode());
		}
		
		int cancel;
		int approvelstatus;
		
		List<LSOrdernotification> ordernotifylist = new ArrayList<LSOrdernotification>();
		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
		LSnotification LSnotification = new LSnotification();
		
		LSuserMaster assigneduser = new LSuserMaster();
	
		Date dueDate = objNotification.getDuedate();
		Instant due = dueDate.toInstant();
		
		LocalDateTime dueTime = LocalDateTime.ofInstant(due, ZoneId.systemDefault());
		LocalDate duedate = dueTime.toLocalDate();
		
		String path = objNotification.getScreen().equals("sheetorder") ? "/registertask" : "/Protocolorder";
		
		String Details = "{\"ordercode\" :\"" + objNotification.getBatchcode() 
	    + "\",\"order\" :\"" + objNotification.getBatchid()
	    + "\",\"days\" :\"" + objNotification.getOverduedays()
	    + "\",\"date\" :\"" + duedate
	    + "\",\"screen\":\"" + objNotification.getScreen() 
		+ "\"}";
		
		if(order==null) {
			 cancel = protocolorder.getOrdercancell() == null ? 0 : protocolorder.getOrdercancell();
			 approvelstatus = protocolorder.getApprovelstatus()== null ? 0 :protocolorder.getApprovelstatus();
			 
			 assigneduser = protocolorder.getAssignedto();
			 if(protocolorder.getAssignedto()!=null) {
				 
				 LSnotification.setIsnewnotification(1);
				 LSnotification.setNotification("ORDEROVERDUEALERT");
				 LSnotification.setNotificationdate(new Date());
				 LSnotification.setNotificationdetils(Details);
				 LSnotification.setNotificationpath(path);
				 LSnotification.setNotifationfrom(assigneduser);
				 LSnotification.setNotifationto(assigneduser);
				 LSnotification.setRepositorycode(0);
				 LSnotification.setRepositorydatacode(0);
				 LSnotification.setNotificationfor(1);
				 lstnotifications.add(LSnotification);	
			 }
		}else {
		   cancel = order.getOrdercancell() == null ? 0 : order.getOrdercancell();
		   approvelstatus = order.getApprovelstatus()== null ? 0 :order.getApprovelstatus();
		   
		   assigneduser=order.getAssignedto();
		   if(order.getAssignedto()!=null) {
			   
			    LSnotification.setIsnewnotification(1);
				LSnotification.setNotification("ORDEROVERDUEALERT");
				LSnotification.setNotificationdate(new Date());
				LSnotification.setNotificationdetils(Details);
				LSnotification.setNotificationpath(path);
				LSnotification.setNotifationfrom(assigneduser);
				LSnotification.setNotifationto(assigneduser);
				LSnotification.setRepositorycode(0);
				LSnotification.setRepositorydatacode(0);
				LSnotification.setNotificationfor(1);
				lstnotifications.add(LSnotification);	
		   }
		}
		 
		 
		if((notobj.getIscompleted() == null || notobj.getIscompleted() == false) && 
				(cancel == 0) && (approvelstatus != 3)){
		
			LSuserMaster LSuserMaster = new LSuserMaster();
			LSuserMaster.setUsercode(objNotification.getUsercode());
			
			
					if(objNotification.getOverduestatus() == 1) {
						//if(indexoverdueorders.getOverduestatus() == 1) {
							
						    LSnotification LSnotification1 = new LSnotification();
							LSnotification1.setIsnewnotification(1);
							LSnotification1.setNotification("ORDEROVERDUEALERT");
							LSnotification1.setNotificationdate(new Date());
							LSnotification1.setNotificationdetils(Details);
							LSnotification1.setNotificationpath(path);
							LSnotification1.setNotifationfrom(LSuserMaster);
							LSnotification1.setNotifationto(LSuserMaster);
							LSnotification1.setRepositorycode(0);
							LSnotification1.setRepositorydatacode(0);
							LSnotification1.setNotificationfor(1);
							
							objNotification.setIsduedateexhausted(true);
							objNotification.setOverduestatus(0);
							
							ordernotifylist.add(objNotification);
							lstnotifications.add(LSnotification1);				
					        }
					
		
			LSnotificationRepository.save(lstnotifications);
			lsordernotificationrepo.save(ordernotifylist);
			notifyoverduedays(objNotification);
		}
	}
	
	public Notification notifyoverduedays(LSOrdernotification objNotification) throws ParseException {
		List<LSOrdernotification> exhaustdays = lsordernotificationrepo.findByIsduedateexhaustedAndIscompletedOrIsduedateexhaustedAndIscompleted(true,false,true,null);
		if(!exhaustdays.isEmpty()) {
			exhaustdays.stream().forEach(indexexhaustdays->{
				Date Date1=indexexhaustdays.getDuedate();
				Date Date2=new Date();
		 
		        Instant instant1 = Date1.toInstant();
		        Instant instant2 = Date2.toInstant();		        
		        LocalDateTime dateTime1 = LocalDateTime.ofInstant(instant1, ZoneId.systemDefault());
		        LocalDateTime dateTime2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());
		        
		        Duration duration = Duration.between(dateTime1, dateTime2);
		        
		        long totalHours = duration.toHours();
		        long days = totalHours / 24;
		        long hours = totalHours % 24;
	
		        System.out.println("Difference: " + days + " days and " + hours + " hours");
		  
		        if(days == 0 && hours!=0) {
			        String diffdays=(int)hours+" Hours";
					indexexhaustdays.setOverduedays(diffdays);
		        }else if(days == 0 && hours==0) {
		        	String diffdays="<1hour";
		        	indexexhaustdays.setOverduedays(diffdays);
		        }
		        else {
		        	String diffdays=(int)days+"Days "+(int)hours+"Hours ";
	
					indexexhaustdays.setOverduedays(diffdays);
		        }
			 });
		}
		lsordernotificationrepo.save(exhaustdays);
		return null;
	
	}
	
	@SuppressWarnings("deprecation")
	public void cautiondatenotification(LSOrdernotification objNotification) throws ParseException {
			
		LSOrdernotification notobj = lsordernotificationrepo.findByBatchcodeAndScreen(objNotification.getBatchcode(), objNotification.getScreen());	
		LSlogilablimsorderdetail order = null;
		LSlogilabprotocoldetail protocolorder = null;
			
		if(objNotification.getScreen().equals("sheetorder")) {
		    order = lslogilablimsorderdetailRepository.findByBatchcodeOrderByBatchcodeDesc(objNotification.getBatchcode());
		}else {
			protocolorder = lslogilabprotocoldetailRepository.findByProtocolordercode(objNotification.getBatchcode());
		}
			
		int cancel;
		int approvelstatus;
			
		Date cautionDate = objNotification.getCautiondate();
		Instant caution = cautionDate.toInstant();
		LocalDateTime cautionTime = LocalDateTime.ofInstant(caution, ZoneId.systemDefault());
		LocalDate cautiondate = cautionTime.toLocalDate();
		
		LSuserMaster assigneduser = new LSuserMaster();
		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
		LSnotification LSnotification = new LSnotification();
		
		String Details = "{\"ordercode\" :\"" + objNotification.getBatchcode() 
        + "\",\"order\" :\"" + objNotification.getBatchid() 
        + "\",\"date\" :\"" + cautiondate
        + "\",\"screen\":\"" + objNotification.getScreen() 
		+ "\"}";
        String path = objNotification.getScreen().equals("sheetorder") ? "/registertask" : "/Protocolorder"; 


		if(order==null) {
			cancel = protocolorder.getOrdercancell() == null ? 0 : protocolorder.getOrdercancell();
	        approvelstatus = protocolorder.getApprovelstatus()== null ? 0 :protocolorder.getApprovelstatus();
	        
	        if(protocolorder.getAssignedto()!= null) {
		        assigneduser=protocolorder.getAssignedto();
		        
		        LSnotification.setIsnewnotification(1);
				LSnotification.setNotification("ORDERCAUTIONALERT");
				LSnotification.setNotificationdate(new Date());
				LSnotification.setNotificationdetils(Details);
				LSnotification.setNotificationpath(path);
				LSnotification.setNotifationfrom(assigneduser);
				LSnotification.setNotifationto(assigneduser);
				LSnotification.setRepositorycode(0);
				LSnotification.setRepositorydatacode(0);
				LSnotification.setNotificationfor(1);
				lstnotifications.add(LSnotification);	
	       }
		}else {
		    cancel = order.getOrdercancell() == null ? 0 : order.getOrdercancell();
			approvelstatus = order.getApprovelstatus()== null ? 0 :order.getApprovelstatus();
			
			if(order.getAssignedto() != null) {
				assigneduser=order.getAssignedto();
				
				LSnotification.setIsnewnotification(1);
				LSnotification.setNotification("ORDERCAUTIONALERT");
				LSnotification.setNotificationdate(new Date());
				LSnotification.setNotificationdetils(Details);
				LSnotification.setNotificationpath(path);
				LSnotification.setNotifationfrom(assigneduser);
				LSnotification.setNotifationto(assigneduser);
				LSnotification.setRepositorycode(0);
				LSnotification.setRepositorydatacode(0);
				LSnotification.setNotificationfor(1);
				lstnotifications.add(LSnotification);	
				
			}
		}
			 
		 
		if((notobj.getIscompleted() == null || notobj.getIscompleted() == false) && 
				(cancel == 0) && (approvelstatus != 3)){
			
			LSuserMaster LSuserMaster = new LSuserMaster();
			LSuserMaster.setUsercode(objNotification.getUsercode());
			
			List<LSOrdernotification> ordernotifylist = new ArrayList<LSOrdernotification>();
			
					if(objNotification.getCautionstatus() == 1) {
	
						LSnotification LSnotification1 = new LSnotification();
							LSnotification1.setIsnewnotification(1);
							LSnotification1.setNotification("ORDERCAUTIONALERT");
							LSnotification1.setNotificationdate(new Date());
							LSnotification1.setNotificationdetils(Details);
							LSnotification1.setNotificationpath(path);
							LSnotification1.setNotifationfrom(LSuserMaster);
							LSnotification1.setNotifationto(LSuserMaster);
							LSnotification1.setRepositorycode(0);
							LSnotification1.setRepositorydatacode(0);
							LSnotification1.setNotificationfor(1);
	
							objNotification.setCautionstatus(0);
							ordernotifylist.add(objNotification);
							lstnotifications.add(LSnotification1);				
					        }
					
			LSnotificationRepository.save(lstnotifications);
			lsordernotificationrepo.save(ordernotifylist);
			notifyoverduedays(objNotification);
		 }
	}

	// added for notification
	public Notification Loginnotification(Notification objNotification) throws ParseException {

		Date fromDate = objNotification.getCurrentdate();
		Date toDate = objNotification.getCurrentdate();
		
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(fromDate);
		fromDate = calendar1.getTime();
		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(toDate);
		calendar2.add(Calendar.HOUR_OF_DAY, 1);
		toDate = calendar2.getTime();

		List<Notification> codelist = NotificationRepository.findByUsercodeAndCautiondateBetween(objNotification.getUsercode(), fromDate, toDate);

		LSuserMaster LSuserMaster = new LSuserMaster();
		LSuserMaster.setUsercode(objNotification.getUsercode());

		LSuserMaster objLSuserMaster = userService.getUserOnCode(LSuserMaster);

		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
		codelist.stream()
				.filter(notification -> notification.getStatus() == 1).map(notification -> {

					LSnotification LSnotification = new LSnotification();
					
					String Details = "{\"ordercode\" :\"" + notification.getOrderid() + "\",\"order\" :\""
							+ notification.getBatchid() + "\",\"description\":\"" + notification.getDescription() + "\",\"screen\":\"" + notification.getScreen() 
							+ "\"}";
							
					LSnotification.setIsnewnotification(1);
					LSnotification.setNotification("CAUTIONALERT");
					LSnotification.setNotificationdate(objNotification.getCurrentdate());
					LSnotification.setNotificationdetils(Details);
					LSnotification.setNotificationpath(notification.getScreen().equals("Sheet Order") ? "/registertask" : "/Protocolorder");
					LSnotification.setNotifationfrom(objLSuserMaster);
					LSnotification.setNotifationto(objLSuserMaster);
					LSnotification.setRepositorycode(0);
					LSnotification.setRepositorydatacode(0);
					LSnotification.setNotificationfor(1);

					notification.setStatus(0);
					lstnotifications.add(LSnotification);
					return LSnotification;
				}).collect(Collectors.toList());

		LSnotificationRepository.save(lstnotifications);
		NotificationRepository.save(codelist);
		return objNotification;
	}

	@SuppressWarnings({ "unused" })
	public Lsrepositoriesdata Resourcenotification(Lsrepositoriesdata objNotification) {
		Date currentdate = objNotification.getCurrentdate();
		List<Lsrepositoriesdata> codelist = lsrepositoriesdataRepository.findByUsercode(objNotification.getUsercode());
		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
		String Details = "";
		int i = 0;
		boolean value = false;
		while (i < codelist.size()) {
			SimpleDateFormat Datefor = new SimpleDateFormat("yyyy-MM-dd");
			String expirydate = Datefor.format(new Date());
			LocalDate formatdate = LocalDate.parse(expirydate);

			String currentdate1 = Datefor.format(currentdate);
			LocalDate currentformatdate = LocalDate.parse(currentdate1);
			LocalDate expirydate1 = formatdate.minusDays(14);
			LocalDate expirydate2 = formatdate.minusDays(7);
			LocalDate expirydate3 = formatdate.minusDays(3);

			Boolean minusdate1 = expirydate1.equals(currentformatdate);
			Boolean minusdate2 = expirydate2.equals(currentformatdate);
			Boolean minusdate3 = expirydate3.equals(currentformatdate);

			LSnotification LSnotification = new LSnotification();

			LSuserMaster LSuserMaster = new LSuserMaster();
			LSuserMaster.setUsercode(codelist.get(i).getUsercode());

			LSuserMaster objLSuserMaster = new LSuserMaster();
			objLSuserMaster = userService.getUserOnCode(LSuserMaster);
			if (codelist.get(i).getItemstatus() == 1) {

				if (minusdate1 == true) {
					Details = "{\"inventory\" :\"" + codelist.get(i).getRepositoryitemname() + "\",\"inventoryid\" :\""
							+ codelist.get(i).getInventoryid() + codelist.get(i).getRepositorydatacode()
							+ "\",\"days\" :\"" + 14 + "\"}";
					LSnotification.setIsnewnotification(1);
					LSnotification.setNotification("INVENTORYEXPIRE");
					LSnotification.setNotificationdate(objNotification.getCurrentdate());
					LSnotification.setNotificationpath("/inventory");
					LSnotification.setNotificationdetils(Details);
					LSnotification.setNotifationfrom(objLSuserMaster);
					LSnotification.setNotifationto(objLSuserMaster);
					LSnotification.setNotificationfor(1);
					lstnotifications.add(LSnotification);
				} else if (minusdate2 == true) {

					Details = "{\"inventory\" :\"" + codelist.get(i).getRepositoryitemname() + "\",\"inventoryid\" :\""
							+ codelist.get(i).getInventoryid() + codelist.get(i).getRepositorydatacode()
							+ "\",\"days\" :\"" + 7 + "\"}";
					LSnotification.setNotificationdetils(Details);
					LSnotification.setIsnewnotification(1);
					LSnotification.setNotification("INVENTORYEXPIRE");
					LSnotification.setNotificationdate(objNotification.getCurrentdate());
					LSnotification.setNotificationpath("/inventory");
					LSnotification.setNotificationdetils(Details);
					LSnotification.setNotifationfrom(objLSuserMaster);
					LSnotification.setNotifationto(objLSuserMaster);
					LSnotification.setNotificationfor(1);
					lstnotifications.add(LSnotification);
				} else if (minusdate3 == true) {
					Details = "{\"inventory\" :\"" + codelist.get(i).getRepositoryitemname() + "\",\"inventoryid\" :\""
							+ codelist.get(i).getInventoryid() + codelist.get(i).getRepositorydatacode()
							+ "\",\"days\" :\"" + 3 + "\"}";
					LSnotification.setNotificationdetils(Details);
					LSnotification.setIsnewnotification(1);
					LSnotification.setNotification("INVENTORYEXPIRE");
					LSnotification.setNotificationdate(objNotification.getCurrentdate());
					LSnotification.setNotificationpath("/inventory");
					LSnotification.setNotificationdetils(Details);
					LSnotification.setNotifationfrom(objLSuserMaster);
					LSnotification.setNotifationto(objLSuserMaster);
					LSnotification.setNotificationfor(1);
					lstnotifications.add(LSnotification);
				}

			}

			i++;
		}

		LSnotificationRepository.save(lstnotifications);
		return null;

	}

//	public LSactiveUser activeUserEntry(LSactiveUser objsite) {
//
//		LSuserMaster objUser = lsuserMasterRepository.findByusercode(objsite.getLsusermaster().getUsercode());
//
//		if (objUser != null) {
//
//			objsite.setLssitemaster(objsite.getLssitemaster());
//			objsite.setTimestamp(objsite.getTimestamp());
//
//			objUser.setLastloggedon(objsite.getTimestamp());
//			lsuserMasterRepository.save(objUser);
//			lsactiveUserRepository.save(objsite);
//
//		}
//
//		return objsite;
//	}

	public List<LSuserMaster> ValidateuserAndPassword(LoggedUser objuser) throws ParseException {
		List<LSuserMaster> objExitinguser = new ArrayList<LSuserMaster>();
//		String username = objuser.getsUsername();
		String userPassword = objuser.getsPassword();
//		if (objuser.getLoggedfrom() == 1) {
//			LSSiteMaster objsite = lSSiteMasterRepository.findBysitecode(Integer.parseInt(objuser.getsSiteCode()));
//			objExitinguser = lSuserMasterRepository.findByUsernameIgnoreCaseAndLssitemasterAndUserretirestatusNot(username,
//					objsite, 1);	
//		}else {
//			LSSiteMaster objsite = lSSiteMasterRepository.findBysitecode(Integer.parseInt(objuser.getsSiteCode()));
//			List<LSMultisites> obj = LSMultisitesRepositery.findByLssiteMaster(objsite);
//			List<Integer> usercode = obj.stream().map(LSMultisites::getUsercode).collect(Collectors.toList());
//			objExitinguser = lSuserMasterRepository
//					.findByUsernameIgnoreCaseAndUsercodeInAndLoginfromAndUserretirestatusNot(username, usercode, "0", 1);
//			
//		}
		
		LSSiteMaster objsite = lSSiteMasterRepository.findBysitecode(Integer.parseInt(objuser.getsSiteCode()));
		List<LSMultisites> obj = LSMultisitesRepositery.findByLssiteMasterAndUsercode(objsite,objuser.getUserID());
		List<Integer> usercode = obj.stream().map(LSMultisites::getUsercode).collect(Collectors.toList());
		objExitinguser = lSuserMasterRepository
				.findByUsercodeInAndUserretirestatusNot(usercode, 1);
		
		
		

		if (!objExitinguser.isEmpty()) {

			objExitinguser.get(0).setObjResponse(new Response());

			if (objuser.getLoggedfrom() == 1) {

				objExitinguser.get(0).getObjResponse().setInformation("Valid user and password");
				objExitinguser.get(0).getObjResponse().setStatus(true);
				objExitinguser.get(0).getObjResponse().setCurrentutcdate(commonfunction.getCurrentUtcTime());
				return objExitinguser;

			}

			String Password = AESEncryption.decrypt(objExitinguser.get(0).getPassword());

			if (Password.equals(userPassword)) {
				objExitinguser.get(0).getObjResponse().setInformation("Valid user and password");
				objExitinguser.get(0).getObjResponse().setStatus(true);
				objExitinguser.get(0).getObjResponse().setCurrentutcdate(commonfunction.getCurrentUtcTime());
			} else {
				objExitinguser.get(0).getObjResponse().setInformation("invalid password");
				objExitinguser.get(0).getObjResponse().setStatus(false);
				objExitinguser.get(0).getObjResponse().setCurrentutcdate(commonfunction.getCurrentUtcTime());
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

	public Map<String, Object> CheckUserPassword(LoggedUser objuser) {

		Map<String, Object> obj = new HashMap<>();
		if (objuser.getsPassword() != null) {
			String password = objuser.getsPassword();
			String Password = AESEncryption.decrypt(password);
			obj.put("password", Password);
			System.out.println(" password: " + Password);
		} else if (objuser.getIsmultitenant() != null) {
			String kumu = objuser.getIsmultitenant().toString();
			String Password1 = AESEncryption.encrypt(kumu);
			System.out.println(" passwordint: " + Password1);
			obj.put("passwordint", Password1);
		}

//	license encryption code 
//		String license = "400";
//		String licensekey = AESEncryption.encrypt(license);
//		System.out.println(" license update key: " + licensekey);
		return obj;

	}

	public void updateActiveUserTime(Map<String, Object> objMap) {

		if (objMap.containsKey("activeusercode")) {
			Integer activerUsercode = (Integer) objMap.get("activeusercode");

			LSactiveUser objUser = lsactiveUserRepository.findByActiveusercode(activerUsercode);

			if (objUser != null) {
				objUser.setRemoveinititated(false);
				objUser.setLastactivetime(new Date());
				lsactiveUserRepository.save(objUser);
			}
		}

		List<LSactiveUser> lstUsers = lsactiveUserRepository
				.findByLastactivetimeLessThan(new Date(System.currentTimeMillis() - 3600 * 1000));

		if (!lstUsers.isEmpty()) {

			List<Integer> objnotifyuser = lstUsers.stream().map(LSactiveUser::getActiveusercode)
					.collect(Collectors.toList());

			removeOrdersOnInActiveLst(objnotifyuser);
			removeOrdersOnInActiveLstforprotocolorders(objnotifyuser);

		}

		lsactiveUserRepository.delete(lstUsers);

//		List<LSactiveUser> lstUsersRemoved = lsactiveUserRepository.findByRemoveinititatedAndLastactivetimeLessThan(
//				true, new Date(System.currentTimeMillis() - 45 * 1000));
		
		List<LSactiveUser> lstUsersRemoved = lsactiveUserRepository.findByRemoveinititatedAndLastactivetimeLessThan(
				true, new Date(System.currentTimeMillis() - 3600 * 1000));

		if (!lstUsersRemoved.isEmpty()) {

			List<Integer> objnotifyuser = lstUsersRemoved.stream().map(LSactiveUser::getActiveusercode)
					.collect(Collectors.toList());

			removeOrdersOnInActiveLst(objnotifyuser);
			removeOrdersOnInActiveLstforprotocolorders(objnotifyuser);

			lsactiveUserRepository.delete(lstUsersRemoved);
		}

	}

	public Boolean autoLogout(LSuserMaster lsuserMaster) {

		if (lsuserMaster.getActiveusercode() != null) {

			LSactiveUser objUser = lsactiveUserRepository.findByActiveusercode(lsuserMaster.getActiveusercode());
			objUser.setRemoveinititated(true);
			lsactiveUserRepository.save(objUser);

		}
		return true;
	}

	public Map<String, Object> getlicense(Map<String, Object> obj) {

		Map<String, Object> rtnobj = new HashMap<>();
		
		//for eln lite lic
				if(obj.get("isMultitenant").toString().equals("2")) {
					String strNumber = (String) obj.get("lssitemaster");
					int sitecode = Integer.parseInt(strNumber);		
					Integer usercode = (Integer) obj.get("usercode");
					Long usercount = lsuserMasterRepository.GetActiveuser(sitecode);
					LSSiteMaster objsite = lSSiteMasterRepository.findBysitecode(sitecode);			
					LSuserMaster objuser = lsuserMasterRepository.findByusercode(usercode);
					
					rtnobj.put("activeuser", usercount);
					rtnobj.put("Noofuser", objsite.getTeamsize());
					rtnobj.put("startDate", objuser.getCreateddate());
					rtnobj.put("expiryDate", objsite.getExpirydate());
					return rtnobj;
				}
		if (obj.get("licencetype") != null && obj.get("licencetype").equals("2")) {
			Long activeusercount = LSactiveUserRepository.count();
			rtnobj.put("activeuser", activeusercount);

		} else if (obj.get("licencetype") != null && obj.get("licencetype").equals("1")) {
			Long usercount = lsuserMasterRepository.countByUserretirestatusNot(1);
			rtnobj.put("activeuser", usercount);

		} else if ((Integer) obj.get("isMultitenant") == 1) {
			DataSourceConfig tenant = DataSourceConfigRepository.findByTenantid(obj.get("tenantdomain").toString());
			rtnobj.put("Noofuser", tenant.getNoofusers());
			LSSiteMaster sitemaster = new LSSiteMaster();
			sitemaster.setSitecode(Integer.parseInt((String) obj.get("lssitemaster")));
			Long usercount = lsuserMasterRepository.countByLssitemasterAndUserstatus(sitemaster, "A");
			rtnobj.put("activeuser", usercount);
		} else {

			LSpreferences objPrefrence = LSpreferencesRepository.findByTasksettingsAndValuesettings("ConCurrentUser",
					"Active");
			LSpreferences objMainFormUser = LSpreferencesRepository.findByTasksettingsAndValuesettings("MainFormUser",
					"Active");
			if (objPrefrence != null) {
				Long activeusercount = LSactiveUserRepository.count();
				String dvalue = objPrefrence.getValueencrypted();
				String sConcurrentUsers = AESEncryption.decrypt(dvalue);
				sConcurrentUsers = sConcurrentUsers.replaceAll("\\s", "");
				rtnobj.put("Noofuser", Integer.parseInt(sConcurrentUsers));
				rtnobj.put("activeuser", activeusercount);
			} else if(objMainFormUser != null){
				String dvalue = objMainFormUser.getValueencrypted();
				String sConcurrentUsers = AESEncryption.decrypt(dvalue);
				sConcurrentUsers = sConcurrentUsers.replaceAll("\\s", "");
				Long usercount = lsuserMasterRepository.countByUserretirestatus(0);
				rtnobj.put("activeuser", usercount);
				rtnobj.put("Noofuser", Integer.parseInt(sConcurrentUsers));
			}else {
				Long usercount = lsuserMasterRepository.countByUsercodeNotAndUserretirestatus(1, 0);
				rtnobj.put("activeuser", usercount);
				rtnobj.put("Noofuser", 3);
			}
		}
		return rtnobj;

	}
	
//	public void autoShedulerInactiveUserkill() {
//		// TODO Auto-generated method stub
//		
//	}

	public List<LSSiteMaster> loadmultisite(LSuserMaster objsite) {
		List<LSuserMaster> usermaster = lsuserMasterRepository
				.findByUsernameIgnoreCaseAndUserretirestatusNot(objsite.getUsername(), 1);
		List<Integer> usercode = usermaster.stream().map(LSuserMaster::getUsercode).collect(Collectors.toList());
		List<LSMultisites> multisites = LSMultisitesRepositery.findByusercodeIn(usercode);
		for (LSMultisites items : multisites) {
			if (items.getDefaultsiteMaster() != null) {
				items.getLssiteMaster().setDefaultsiteMaster(items.getDefaultsiteMaster());
			}
		}
		return multisites.stream().peek(items -> {
			if (items.getDefaultsiteMaster() != null) {
				items.getLssiteMaster().setDefaultsiteMaster(items.getDefaultsiteMaster());
			}
		}).map(LSMultisites::getLssiteMaster).sorted(Comparator.comparingInt(LSSiteMaster::getSitecode)).filter((items)->(items.getIstatus()!=0))
				.collect(Collectors.toList());

	}

//	public List<LSSiteMaster> LoadCreatedBySite(LSuserMaster objsite) {
//		List<LSSiteMaster> lstsite = lSSiteMasterRepository.getLoadCreatedBySite(objsite.getUsercode());
//		
//		return lstsite;
//	}
}