package com.agaram.eln.primary.service.usermanagement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.agaram.eln.config.ADS_Connection;
import com.agaram.eln.config.AESEncryption;
import com.agaram.eln.config.JwtTokenUtil;
import com.agaram.eln.primary.model.cfr.LSaudittrailconfiguration;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.jwt.JwtResponse;
import com.agaram.eln.primary.model.usermanagement.LSPasswordHistoryDetails;
import com.agaram.eln.primary.model.usermanagement.LSPasswordPolicy;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSactiveUser;
import com.agaram.eln.primary.model.usermanagement.LSdomainMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.usermanagement.LSPasswordHistoryDetailsRepository;
import com.agaram.eln.primary.repository.usermanagement.LSPasswordPolicyRepository;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSactiveUserRepository;
import com.agaram.eln.primary.repository.usermanagement.LSdomainMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusergroupRepository;
import com.agaram.eln.primary.service.JWTservice.JwtUserDetailsService;
import com.agaram.eln.primary.service.cfr.AuditService;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
@EnableJpaRepositories(basePackageClasses = LSSiteMasterRepository.class)
//@EnableJpaRepositories(basePackageClasses = LSDomainMasterRepository.class)
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
	public Map<String, Object> Login(LoggedUser objuser)
	{
		Map<String, Object> obj = new HashMap<>();
		LSuserMaster objExitinguser = new LSuserMaster();
		
		String username = objuser.getsUsername();
		objExitinguser = lSuserMasterRepository.findByUsernameIgnoreCaseAndLoginfrom(username,"0");
		LSPasswordPolicy lockcount =objExitinguser!=null? LSPasswordPolicyRepository.findTopByAndLssitemasterOrderByPolicycodeDesc(objExitinguser.getLssitemaster()):null;
//		if(objExitinguser != null && objExitinguser.getLssitemaster().getSitecode().toString().equals(objuser.getsSiteCode()))
		if(objExitinguser != null)
		{
			objExitinguser.setObjResponse(new Response());
			objExitinguser.setObjsilentaudit(new LScfttransaction());
			if(Integer.parseInt(objuser.getsSiteCode()) == objExitinguser.getLssitemaster().getSitecode()) 
			{
				String Password = AESEncryption.decrypt(objExitinguser.getPassword());
				System.out.println(" password: " + Password);
			    
			    Date passwordexp=objExitinguser.getPasswordexpirydate();
			    if(Password.equals(objuser.getsPassword()) && objExitinguser.getUserstatus()!="Locked")
			    {
			    	
			    	String status = objExitinguser.getUserstatus();
			    	String groupstatus=objExitinguser.getLsusergroup().getUsergroupstatus();
			    	if(status.equals("Deactive"))
			    	{
			    		objExitinguser.getObjResponse().setInformation("ID_NOTACTIVE");
						objExitinguser.getObjResponse().setStatus(false);
//						if(objExitinguser.getObjsilentaudit() != null)
//				    	{   
						objuser.getObjsilentaudit().setActions("Warning");
						objuser.getObjsilentaudit().setComments(objExitinguser.getUsername()+" "+"was not active to login");
						objuser.getObjsilentaudit().setTableName("LSuserMaster");
						objuser.getObjsilentaudit().setUsername(objExitinguser.getUsername());
//						objuser.getObjsilentaudit().setTransactiondate(new Date());
						objuser.getObjsilentaudit().setSystemcoments("System Generated");
						objuser.getObjsilentaudit().setModuleName(ModuleName);
						objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
			    		objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
				    		
				    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
//				    	}
						obj.put("user", objExitinguser);
						return obj;
			    	}
			    	else if(groupstatus.trim().equals("Deactive")) 
			    	{
			    		objExitinguser.getObjResponse().setInformation("ID_GRPNOACT");
						objExitinguser.getObjResponse().setStatus(false);
						  
						objuser.getObjsilentaudit().setActions("Warning");
						objuser.getObjsilentaudit().setComments("Currently group was not active for the user"+" "+objExitinguser.getUsername()+" "+"to login");
						objuser.getObjsilentaudit().setTableName("LSusergroup");
						objuser.getObjsilentaudit().setUsername(objExitinguser.getUsername());
//						objuser.getObjsilentaudit().setTransactiondate(new Date());
						objuser.getObjsilentaudit().setSystemcoments("System Generated");
						objuser.getObjsilentaudit().setModuleName(ModuleName);
						objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
						objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
				    	lscfttransactionRepository.save(objuser.getObjsilentaudit());
				    	
						obj.put("user", objExitinguser);
						return obj;
			    	}
			    	else
			    	{
//			    		System.out.println(Locale.getDefault());
//			    		Locale locale = Locale.getDefault();
//			    		DateFormat df1 = DateFormat.getDateInstance(DateFormat.SHORT, locale);
//			    		System.out.println(df1.format(new Date()));
			    		
			    		try {
							Date newDate = new SimpleDateFormat( "yyyy/dd/MM hh:mm:ss" ).parse( "4444/31/12 23:58:57" );
							System.out.println(newDate);
							Locale locale = Locale.getDefault();
//							Locale currentLocale = new Locale("en_CA");
//							DateFormat df1 = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, currentLocale);
							DateFormat datetimeFormatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
							DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
							logger.info(locale);
							logger.info(datetimeFormatter.format(newDate));
							logger.info(dateFormatter.format(newDate));
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
//							dateSString = dateSString.replaceAll("AM", "a");
//							dateSString = dateSString.replaceAll("PM", "a");
							dateSString = dateSString.replaceAll(" AM", "");
							dateSString = dateSString.replaceAll(" PM", "");
							logger.info(dateSString);
							dateSString="MM-dd-yyyy hh:mm:ss";
		                	objExitinguser.setDFormat(dateSString);
						} catch (ParseException e) {
							e.printStackTrace();
						}
			    		
//			    		String newDate1 = DateFormat.getInstance().format(new Date());
//			    		if(newDate1.contains("AM") || newDate1.contains("PM")) {
//			    			newDate1 = ((String) newDate1.subSequence(0, newDate1.length() -2)).trim();
//			    		}
//			    		System.out.println("new date: " + newDate1);
//			    		System.out.println("new date: " + DateFormat.getInstance());
//			    		for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
//			                if (newDate1.toLowerCase().matches(regexp)) {
//			                	System.out.println(DATE_FORMAT_REGEXPS.get(regexp));
//			                	objExitinguser.setDFormat(DATE_FORMAT_REGEXPS.get(regexp)); 
//			                }
//			            }
			    		if(!username.trim().toLowerCase().equals("administrator")) {
			    			Date date = new Date();
//					    	String df = DateFormat.getDateInstance().format(date);
//					    	String expiry=null;
//					    	expiry=DateFormat.getDateInstance().format(objExitinguser.getPasswordexpirydate());
					 	   boolean comp1= objExitinguser.getPasswordexpirydate().compareTo(date) > 0;
						   boolean  comp2= objExitinguser.getPasswordexpirydate().compareTo(date) < 0;
						   boolean  comp3= objExitinguser.getPasswordexpirydate().compareTo(date) ==0;
			    		if(comp3== true  ||(comp1==false && comp2== true)) {
//					    	if(expiry.compareTo(df)==0 || expiry.compareTo(df) < 0) {
				    			objExitinguser.setPassword(null);
				    			lSuserMasterRepository.save(objExitinguser);
				    			objExitinguser.getObjResponse().setInformation("ID_EXPIRY");
								objExitinguser.getObjResponse().setStatus(false);
								  
								objuser.getObjsilentaudit().setActions("Warning");
								objuser.getObjsilentaudit().setComments("Password was expired for "+""+objExitinguser.getUsername());
								objuser.getObjsilentaudit().setTableName("LSuserMaster");
								objuser.getObjsilentaudit().setUsername(objExitinguser.getUsername());
//								objuser.getObjsilentaudit().setTransactiondate(new Date());
								objuser.getObjsilentaudit().setSystemcoments("System Generated");
								objuser.getObjsilentaudit().setModuleName(ModuleName);
								objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
					    		objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
					    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
						    
								obj.put("user", objExitinguser);
								return obj;
				    		}
				    		else {
						    	objExitinguser.getObjResponse().setStatus(true);
						    	
						    	if(objuser.getObjsilentaudit() != null)
						    	{
						    		objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
						    		objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
//						    		objuser.getObjsilentaudit().setModuleName(ModuleName);
//						    		objuser.getObjsilentaudit().setComments("User Logged in Successfully");
//						    		objuser.getObjsilentaudit().setActions("Login Success");
//						    		objuser.getObjsilentaudit().setSystemcoments("System Generated");
//						    		objuser.getObjsilentaudit().setManipulatetype("Login");
						    		objuser.getObjsilentaudit().setTableName("LSactiveuser");
						    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
						    		
						    		LSactiveUser objactiveuser = new LSactiveUser();
						    	 	objactiveuser.setLsusermaster(objExitinguser);
						    	 	objactiveuser.setLssitemaster(objExitinguser.getLssitemaster());
						    	 	objactiveuser.setTimestamp(objuser.getLogindate());
						    		
						    	 	lsactiveUserRepository.save(objactiveuser);
						    	
						    	}
					    	
						    	// if(lsactiveUserRepository.findBylsusermaster(objExitinguser) == null)
						    	// {
							    // 	LSactiveUser objactiveuser = new LSactiveUser();
						    	// 	objactiveuser.setLsusermaster(objExitinguser);
						    	// 	objactiveuser.setLssitemaster(objExitinguser.getLssitemaster());
						    	// 	objactiveuser.setTimestamp(objuser.getLogindate());
						    		
						    	// 	lsactiveUserRepository.save(objactiveuser);
						    	// }
					    	
				    		}
			    		}
				    		else {
					    	objExitinguser.getObjResponse().setStatus(true);
					    	
					    	if(objuser.getObjsilentaudit() != null)
					    	{
					    		objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
					    		objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
					    		objuser.getObjsilentaudit().setModuleName(ModuleName);
//					    		objuser.getObjsilentaudit().setComments("User Logged in Successfully");
//					    		objuser.getObjsilentaudit().setActions("Login Success");
//					    		objuser.getObjsilentaudit().setSystemcoments("System Generated");
//					    		objuser.getObjsilentaudit().setManipulatetype("Login");
					    		objuser.getObjsilentaudit().setTableName("LSactiveuser");
					    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
					    		
					    		LSactiveUser objactiveuser = new LSactiveUser();
					    	 	objactiveuser.setLsusermaster(objExitinguser);
					    	 	objactiveuser.setLssitemaster(objExitinguser.getLssitemaster());
					    	 	objactiveuser.setTimestamp(objuser.getLogindate());
					    	
					    	}
					    	
					    	// if(lsactiveUserRepository.findBylsusermaster(objExitinguser) == null)
					    	// {
						    // 	LSactiveUser objactiveuser = new LSactiveUser();
					    	// 	objactiveuser.setLsusermaster(objExitinguser);
					    	// 	objactiveuser.setLssitemaster(objExitinguser.getLssitemaster());
					    	// 	objactiveuser.setTimestamp(objuser.getLogindate());
					    		
					    	// 	lsactiveUserRepository.save(objactiveuser);
					    	// }
					    	
				    		}
				    	}
			    	}
			    	else if(!Password.equals(objuser.getsPassword()) 
			    				|| objExitinguser.getLockcount() == 5 
			    					|| objExitinguser.getUserstatus()=="Locked") {
				    	if(!username.trim().toLowerCase().equals("administrator")) 
				    	{
					    	Integer count= objExitinguser.getLockcount()==null?0: objExitinguser.getLockcount();
					    	count++;
					    	if(count.equals(lockcount.getLockpolicy())) {
					    		objExitinguser.setUserstatus("Locked");
					    		objExitinguser.setLockcount(count++);
							objExitinguser.getObjResponse().setInformation("ID_LOCKED");
							objExitinguser.getObjResponse().setStatus(false);
							  
							objuser.getObjsilentaudit().setActions("Warning");
							objuser.getObjsilentaudit().setComments("User Account was locked,"+objExitinguser.getUsername()+ " "+"entered incorrect password many times.");
							objuser.getObjsilentaudit().setTableName("LSuserMaster");
							objuser.getObjsilentaudit().setUsername(objExitinguser.getUsername());
//							objuser.getObjsilentaudit().setTransactiondate(new Date());
							objuser.getObjsilentaudit().setSystemcoments("System Generated");
							objuser.getObjsilentaudit().setModuleName(ModuleName);
								objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
					    		objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
					    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
					    	
							lSuserMasterRepository.save(objExitinguser);
					    	}
					    	else if(count<lockcount.getLockpolicy()) {
					    		objExitinguser.setLockcount(count++);
								objExitinguser.getObjResponse().setInformation("ID_INVALID");
								objExitinguser.getObjResponse().setStatus(false);
								  
								objuser.getObjsilentaudit().setActions("Warning");
								objuser.getObjsilentaudit().setComments(objExitinguser.getUsername()+ " "+"entered incorrect password");
								objuser.getObjsilentaudit().setTableName("LSuserMaster");
								objuser.getObjsilentaudit().setUsername(objExitinguser.getUsername());
//								objuser.getObjsilentaudit().setTransactiondate(new Date());
								objuser.getObjsilentaudit().setSystemcoments("System Generated");
								objuser.getObjsilentaudit().setModuleName(ModuleName);
								objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
					    		objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
					    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
						    	
								lSuserMasterRepository.save(objExitinguser);
					    	}
					    	else {
							objExitinguser.getObjResponse().setInformation("ID_LOCKED");
							objExitinguser.getObjResponse().setStatus(false);
							 
							objuser.getObjsilentaudit().setActions("Warning");
							objuser.getObjsilentaudit().setComments("User Account was locked,"+objExitinguser.getUsername()+ " "+"entered incorrect password many times.");
							objuser.getObjsilentaudit().setTableName("LSuserMaster");
							objuser.getObjsilentaudit().setUsername(objExitinguser.getUsername());
//							objuser.getObjsilentaudit().setTransactiondate(new Date());
							objuser.getObjsilentaudit().setSystemcoments("System Generated");
							objuser.getObjsilentaudit().setModuleName(ModuleName);
								objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
					    		objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
					    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
					    	
					    	}
				    	}
				    	else {
				    		objExitinguser.getObjResponse().setInformation("ID_INVALID");
							objExitinguser.getObjResponse().setStatus(false);
						
							objuser.getObjsilentaudit().setActions("Warning");
							objuser.getObjsilentaudit().setComments(objExitinguser.getUsername()+ " "+"entered incorrect password");
							objuser.getObjsilentaudit().setTableName("LSuserMaster");
							objuser.getObjsilentaudit().setUsername(objExitinguser.getUsername());
//							objuser.getObjsilentaudit().setTransactiondate(new Date());
							objuser.getObjsilentaudit().setSystemcoments("System Generated");
							objuser.getObjsilentaudit().setModuleName(ModuleName);
								objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
					    		objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
					    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
					    	
				    	}
				}
			}
			else {
				objExitinguser.getObjResponse().setInformation("ID_SITEVALID");
				objExitinguser.getObjResponse().setStatus(false);
				 
				objuser.getObjsilentaudit().setActions("Warning");
					objuser.getObjsilentaudit().setComments(objExitinguser.getUsername()+ " "+"does not belongs to the site");
					objuser.getObjsilentaudit().setTableName("LSuserMaster");
					objuser.getObjsilentaudit().setUsername(objExitinguser.getUsername());
//					objuser.getObjsilentaudit().setTransactiondate(new Date());
					objuser.getObjsilentaudit().setSystemcoments("System Generated");
					objuser.getObjsilentaudit().setModuleName(ModuleName);
					objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
		    		objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
		    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
		    	
				obj.put("user", objExitinguser);
				return obj;
			}
		}
		    
		else
		{
			LSSiteMaster objsite = lSSiteMasterRepository.findBysitecode(Integer.parseInt(objuser.getsSiteCode()));
			objExitinguser = lSuserMasterRepository.findByusernameAndLssitemaster("Administrator", objsite);
			objExitinguser.setObjResponse(new Response());
			objExitinguser.getObjResponse().setInformation("ID_NOTEXIST");
			objExitinguser.getObjResponse().setStatus(false);
		  
			objuser.getObjsilentaudit().setActions("Warning");
			objuser.getObjsilentaudit().setComments("User"+" "+objuser.getsUsername()+ " "+"does not exist");
			objuser.getObjsilentaudit().setTableName("LSusergroup");
			objuser.getObjsilentaudit().setUsername(objExitinguser.getUsername());
//			objuser.getObjsilentaudit().setTransactiondate(new Date());
			objuser.getObjsilentaudit().setSystemcoments("System Generated");
			objuser.getObjsilentaudit().setModuleName(ModuleName);
			objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
    		objuser.getObjsilentaudit().setLssitemaster(Integer.parseInt(objuser.getsSiteCode()));
    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
	    	
		}
		
		obj.put("user", objExitinguser);
		if(objExitinguser.getLsusergroup() != null)
		{
			obj.put("userrights", userService.GetUserRightsonGroup(objExitinguser.getLsusergroup()));
			LSaudittrailconfiguration objauditconfig = new LSaudittrailconfiguration();
			objauditconfig.setLsusermaster(objExitinguser);
			obj.put("auditconfig", auditService.GetAuditconfigUser(objauditconfig));
		}
		
		return obj;
	}
	
	public Boolean Updatepassword(int UserId, String Password) {
		LSuserMaster objuserforupdate = lSuserMasterRepository.findByusercode(UserId);
		objuserforupdate.setPassword(AESEncryption.encrypt(Password));
		lSuserMasterRepository.save(objuserforupdate);
		
		//lSuserMasterRepository.setpasswordByusercode("new", objExitinguser.getUsercode());
		
		return true;
	}

	public LSuserMaster CheckUserAndPassword(LoggedUser objuser) {
		
		LSuserMaster objExitinguser = new LSuserMaster();
		String username = objuser.getsUsername();
//		objExitinguser = lSuserMasterRepository.findByUsernameIgnoreCaseAndLoginfrom(username,"0");
		objExitinguser = lSuserMasterRepository.findByUsernameIgnoreCaseAndLoginfromAndUserretirestatusNot(username,"0",1);
		
		if(objExitinguser != null)
		{
			String Password = AESEncryption.decrypt(objExitinguser.getPassword());
		    objExitinguser.setObjResponse(new Response());
		    
		    if(Password == null ){
		    	objExitinguser.getObjResponse().setInformation("GenerateNewPassword");
		    	objExitinguser.getObjResponse().setStatus(true);
		    	objuser.getObjsilentaudit().setActions("Warning");
				objuser.getObjsilentaudit().setComments("User"+" "+objuser.getsUsername()+ " "+"does not have the password to login, rendered to create password");
				objuser.getObjsilentaudit().setTableName("LSuserMaster");
				objuser.getObjsilentaudit().setUsername(objExitinguser.getUsername());
//				objuser.getObjsilentaudit().setTransactiondate(new Date());
				objuser.getObjsilentaudit().setSystemcoments("System Generated");
				objuser.getObjsilentaudit().setModuleName(ModuleName);
					objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
		    		objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
		    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
		    }
		    else {
		    	objExitinguser.getObjResponse().setInformation("Valid user and password exist");
		    	objExitinguser.getObjResponse().setStatus(false);
		    } 
		}
		else
		{
			LSuserMaster objusermaster = lSuserMasterRepository.findByusercode(1);
			
			objExitinguser = new LSuserMaster();
			objExitinguser.setUserstatus("");
			objExitinguser.setObjResponse(new Response());
			objExitinguser.getObjResponse().setInformation("Invalid user");
			objExitinguser.getObjResponse().setStatus(false);
			objuser.getObjsilentaudit().setActions("Warning");
			objuser.getObjsilentaudit().setComments("User"+" "+objuser.getsUsername()+ " "+"does not exist");
			objuser.getObjsilentaudit().setTableName("LSuserMaster");
			objuser.getObjsilentaudit().setUsername(objusermaster.getUsername());
//			objuser.getObjsilentaudit().setTransactiondate(new Date());
			objuser.getObjsilentaudit().setSystemcoments("System Generated");
			objuser.getObjsilentaudit().setModuleName(ModuleName);
			objuser.getObjsilentaudit().setLsuserMaster(objusermaster.getUsercode());
//	    			objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
			objuser.getObjsilentaudit().setLssitemaster(Integer.parseInt(objuser.getsSiteCode()));
    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}
		return objExitinguser;
	}

	@SuppressWarnings({ "unused" })
	public LSuserMaster UpdatePassword(LoggedUser objuser) {
		LSuserMaster objExitinguser = new LSuserMaster();
		String username = objuser.getsUsername();
//		objExitinguser = lSuserMasterRepository.findByusername(username);
		objExitinguser = lSuserMasterRepository.findByusernameIgnoreCase(username);
//		objExitinguser = lSuserMasterRepository.findByusercode(objuser.getUserID());
		
		List<LSPasswordHistoryDetails> listofpwd = new ArrayList<LSPasswordHistoryDetails>();
		LSPasswordHistoryDetails objectpwd = new LSPasswordHistoryDetails();
		List<LSPasswordHistoryDetails> result= new ArrayList<LSPasswordHistoryDetails>();
//		List<LSPasswordPolicy> passHistorycount = LSPasswordPolicyRepository.findAll();
		LSPasswordPolicy passHistorycount = LSPasswordPolicyRepository.findByLssitemaster(objExitinguser.getLssitemaster());


		 listofpwd = LSPasswordHistoryDetailsRepository.findTop5ByAndLsusermasterInOrderByPasswordcodeDesc(objExitinguser);

		if(objExitinguser != null)
		{
			objExitinguser.setObjResponse(new Response());
		    
		    String newpassword=AESEncryption.encrypt(objuser.getsNewPassword());
		  if(listofpwd.size()!=0)  {
			  if(listofpwd.size()> passHistorycount.getPasswordhistory()) {
		 listofpwd.subList( passHistorycount.getPasswordhistory(),listofpwd.size()).clear();
			  }
		 result = listofpwd.stream().filter(LSPasswordHistoryDetails -> newpassword.equals(LSPasswordHistoryDetails.getPassword()))
	                .collect(Collectors.toList());
		  }
		 if(result.size()!=0) {
				objExitinguser.getObjResponse().setInformation("ID_HISTORY");
				objExitinguser.getObjResponse().setStatus(false);
				if(objuser.getObjsilentaudit() != null)
		    	{
		    		objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
		    		objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
//		    		objuser.getObjsilentaudit().setModuleName(ModuleName);
		    		objuser.getObjsilentaudit().setComments(objuser.getsUsername()+" "+"entered password does not reach the history range");
//		    		objuser.getObjsilentaudit().setActions("PassWord Created");
//		    		objuser.getObjsilentaudit().setSystemcoments("System Generated");
		    		objuser.getObjsilentaudit().setManipulatetype("Password");
		    		objuser.getObjsilentaudit().setTableName("LSPasswordHistoryDetails");
		    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
		    		
		    	}
				return objExitinguser;

		 }
		 
		 String existingpassword = AESEncryption.decrypt(objExitinguser.getPassword());
		 if(objuser.getsOLDPassword().equals(existingpassword) && objuser.getsOLDPassword() !="")
		 {
			 if(objuser.getsNewPassword().equals(objuser.getsConfirmPassword()))
					 {
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
				
				if(objuser.getObjsilentaudit() != null)
		    	{
		    		objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
		    		objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
//		    		objuser.getObjsilentaudit().setModuleName(ModuleName);
//		    		objuser.getObjsilentaudit().setComments("PassWord Created Successfully");
//		    		objuser.getObjsilentaudit().setActions("PassWord Created");
//		    		objuser.getObjsilentaudit().setSystemcoments("System Generated");
		    		objuser.getObjsilentaudit().setManipulatetype("Password");
		    		objuser.getObjsilentaudit().setTableName("LSuserMaster");
		    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
		    		
		    	}
				
					 }
				else
				{
					objExitinguser.getObjResponse().setInformation("ID_NOTMATCH");
					objExitinguser.getObjResponse().setStatus(false);
				}
		 }
		 else if((!objuser.getsOLDPassword().equals(existingpassword)) && (existingpassword!=null))
			{
				objExitinguser.getObjResponse().setInformation("ID_NOTOLDPASSMATCH");
				objExitinguser.getObjResponse().setStatus(false);
			}
			 else if(objuser.getsNewPassword().equals(objuser.getsConfirmPassword()) && objuser.getsOLDPassword() =="") {
				

				objectpwd.setPassword(AESEncryption.encrypt(objuser.getsNewPassword()));
				objectpwd.setPasswordcreatedate(new Date());
				objectpwd.setLsusermaster(objExitinguser);
				LSPasswordHistoryDetailsRepository.save(objectpwd);
				
				objExitinguser.setPassword(AESEncryption.encrypt(objuser.getsNewPassword()));
				objExitinguser.setPasswordexpirydate(objuser.getPasswordexpirydate());
				lSuserMasterRepository.save(objExitinguser);
				
				objExitinguser.getObjResponse().setInformation("ID_SUCCESSMSG");
				objExitinguser.getObjResponse().setStatus(true);
				
				if(objuser.getObjsilentaudit() != null)
		    	{
		    		objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
		    		objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
//		    		objuser.getObjsilentaudit().setModuleName(ModuleName);
//		    		objuser.getObjsilentaudit().setComments("PassWord Created Successfully");
//		    		objuser.getObjsilentaudit().setActions("PassWord Created");
//		    		objuser.getObjsilentaudit().setSystemcoments("System Generated");
		    		objuser.getObjsilentaudit().setManipulatetype("Password");
		    		objuser.getObjsilentaudit().setTableName("LSuserMaster");
		    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
		    		
		    	}
				
				
			}
			else
			{
				objExitinguser.getObjResponse().setInformation("ID_NOTMATCH");
				objExitinguser.getObjResponse().setStatus(false);
			}
		}
		else
		{
			objExitinguser.getObjResponse().setInformation("ID_INVALID");
			objExitinguser.getObjResponse().setStatus(false);
		}
		return objExitinguser;
	}
	
//		public Boolean Logout(LSuserMaster lsuserMaster)
//		{
//			if(lsuserMaster.getObjsilentaudit() != null)
//			{
//			lsuserMaster.getObjsilentaudit().setLsuserMaster(lsuserMaster.getUsercode());
//			lsuserMaster.getObjsilentaudit().setLssitemaster(lsuserMaster.getLssitemaster().getSitecode());
//			lsuserMaster.getObjsilentaudit().setModuleName(ModuleName);
//			lsuserMaster.getObjsilentaudit().setComments("User Logged out Successfully");
//			lsuserMaster.getObjsilentaudit().setActions("Logout Success");
//			lsuserMaster.getObjsilentaudit().setSystemcoments("System Generated");
//			lsuserMaster.getObjsilentaudit().setManipulatetype("Logout");
//			lsuserMaster.getObjsilentaudit().setTableName("LSuserMaster");
//			lscfttransactionRepository.save(lsuserMaster.getObjsilentaudit());
//			}
//			lsactiveUserRepository.deleteBylsusermaster(lsuserMaster);
//			return true;
//		}
	
	public Boolean Logout(LSuserMaster lsuserMaster)
	{
		if(lsuserMaster.getObjsilentaudit() != null)
		{
			lsuserMaster.getObjsilentaudit().setLsuserMaster(lsuserMaster.getUsercode());
			lsuserMaster.getObjsilentaudit().setLssitemaster(lsuserMaster.getObjsilentaudit().getLssitemaster());
			lsuserMaster.getObjsilentaudit().setModuleName(ModuleName);
			lsuserMaster.getObjsilentaudit().setComments("User Logged out Successfully");
			lsuserMaster.getObjsilentaudit().setActions("Logout Success");
			lsuserMaster.getObjsilentaudit().setSystemcoments("System Generated");
			lsuserMaster.getObjsilentaudit().setManipulatetype("Logout");
			lsuserMaster.getObjsilentaudit().setTableName("LSuserMaster");
			lscfttransactionRepository.save(lsuserMaster.getObjsilentaudit());
		}
		lsactiveUserRepository.deleteBylsusermaster(lsuserMaster);
		return true;
	}	
		
		@SuppressWarnings({ "unused" })
		public LSuserMaster ChangePassword(LoggedUser objuser) {
		LSuserMaster objExitinguser = new LSuserMaster();
		
		LSPasswordHistoryDetails objectpwd = new LSPasswordHistoryDetails();
//		LSPasswordHistoryDetails passTopRecord=LSPasswordHistoryDetailsRepository.findTopByOrderByPasswordcodeDesc();
		List<LSPasswordHistoryDetails> listofpwd = new ArrayList<LSPasswordHistoryDetails>();
//		LSPasswordHistoryDetails objectpwd = new LSPasswordHistoryDetails();
		List<LSPasswordHistoryDetails> result= new ArrayList<LSPasswordHistoryDetails>();
	
//		Integer diff=0;
		 
		String username = objuser.getsUsername();
		LSSiteMaster objsite = lSSiteMasterRepository.findBysitecode(objuser.getLsusermaster().getLssitemaster().getSitecode());
		objExitinguser = lSuserMasterRepository.findByusernameAndLssitemaster(username, objsite);
		LSPasswordPolicy passHistorycount =  LSPasswordPolicyRepository.findByLssitemaster(objExitinguser.getLssitemaster());
//		 
		 listofpwd = LSPasswordHistoryDetailsRepository.findTop5ByAndLsusermasterInOrderByPasswordcodeDesc(objExitinguser);

		if(objExitinguser != null)
		{
			objExitinguser.setObjResponse(new Response());
			
			String Password = AESEncryption.decrypt(objExitinguser.getPassword());
		    objExitinguser.setObjResponse(new Response());
		    
		    String newpassword=AESEncryption.encrypt(objuser.getsNewPassword());
//		   LSPasswordHistoryDetails list=LSPasswordHistoryDetailsRepository.findTopByPasswordOrderByPasswordcodeDesc(newpassword);
		   if(listofpwd.size()!=0)  {
				  if(listofpwd.size()> passHistorycount.getPasswordhistory()) {
			 listofpwd.subList( passHistorycount.getPasswordhistory(),listofpwd.size()).clear();
				  }
			 result = listofpwd.stream().filter(LSPasswordHistoryDetails -> newpassword.equals(LSPasswordHistoryDetails.getPassword()))
		                .collect(Collectors.toList());
			  }
//		   if(list!=null) {
//			   diff =passTopRecord.getPasswordcode()-list.getPasswordcode();
//		   }
		    if(!Password.equals(objuser.getsPassword()))
		    {
		    	objExitinguser.getObjResponse().setInformation("ID_EXIST");
				objExitinguser.getObjResponse().setStatus(false);
				if(objuser.getObjsilentaudit() != null)
		    	{
		    		objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
		    		objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
		    		objuser.getObjsilentaudit().setComments(objuser.getsUsername()+" "+"entered existing password incorrectly");
		    		objuser.getObjsilentaudit().setManipulatetype("Password");
		    		objuser.getObjsilentaudit().setTableName("LSuserMaster");
		    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
		    		
		    	}
				return objExitinguser;
		    }
//			if(!diff.equals(passHistorycount.getPasswordhistory()) && diff<=passHistorycount.getPasswordhistory() && diff!=0) {
		    if(result.size()!=0) {	
				objExitinguser.getObjResponse().setInformation("ID_HISTORY");
				objExitinguser.getObjResponse().setStatus(false);
				if(objuser.getObjsilentaudit() != null)
		    	{
		    		objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
		    		objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
//		    		objuser.getObjsilentaudit().setModuleName(ModuleName);
		    		objuser.getObjsilentaudit().setComments(objuser.getsUsername()+" "+"entered password does not reach the history range");
//		    		objuser.getObjsilentaudit().setActions("PassWord Created");
//		    		objuser.getObjsilentaudit().setSystemcoments("System Generated");
		    		objuser.getObjsilentaudit().setManipulatetype("Password");
		    		objuser.getObjsilentaudit().setTableName("LSPasswordHistoryDetails");
		    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
		    		
		    	}
				return objExitinguser;
			}
			if(objuser.getsNewPassword().equals(objuser.getsConfirmPassword())) {
				
				objExitinguser.setPassword(AESEncryption.encrypt(objuser.getsNewPassword()));
				lSuserMasterRepository.save(objExitinguser);
				
				objectpwd.setPassword(AESEncryption.encrypt(objuser.getsNewPassword()));
				objectpwd.setPasswordcreatedate(new Date());
				objectpwd.setLsusermaster(objuser.getLsusermaster());
				LSPasswordHistoryDetailsRepository.save(objectpwd);
				
				objExitinguser.getObjResponse().setInformation("ID_CHANGESUC");
				objExitinguser.getObjResponse().setStatus(true);
				
				if(objuser.getObjsilentaudit() != null)
		    	{
		    		objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
		    		objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
//		    		objuser.getObjsilentaudit().setModuleName(ModuleName);
//		    		objuser.getObjsilentaudit().setComments("PassWord Created Successfully");
//		    		objuser.getObjsilentaudit().setActions("PassWord Created");
		    		objuser.getObjsilentaudit().setSystemcoments("System Generated");
//		    		objuser.getObjsilentaudit().setManipulatetype("Password");
		    		objuser.getObjsilentaudit().setTableName("LSuserMaster");
		    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
		    		
		    	
			}
			
			}
			else
			{
				objExitinguser.getObjResponse().setInformation("ID_NOTMATCH");
				objExitinguser.getObjResponse().setStatus(false);
			}
		}
		else
		{
			objExitinguser.getObjResponse().setInformation("ID_INVALID");
			objExitinguser.getObjResponse().setStatus(false);
		}
		return objExitinguser;
	}

	public LSdomainMaster InsertupdateDomain(LSdomainMaster objClass) {
		
		objClass.setResponse(new Response());
		if(objClass.getDomaincode() == null && lSDomainMasterRepository.findByDomainnameIgnoreCaseAndDomainstatus(objClass.getDomainname(),1) != null)
		{
			objClass.getResponse().setStatus(false);
			objClass.getResponse().setInformation("ID_EXIST");
//			silent audit
			if(objClass.getObjsilentaudit() != null)
	    	{   
				objClass.getObjsilentaudit().setActions("Warning");
				objClass.getObjsilentaudit().setComments(objClass.getObjsilentaudit().getUsername()+" "+"made attempt to create existing domain name");
				objClass.getObjsilentaudit().setTableName("LSusergroup");
	    		lscfttransactionRepository.save(objClass.getObjsilentaudit());
	    	}
//			manual audit
			if(objClass.getObjuser() != null)
	    	{
				objClass.getObjmanualaudit().setActions("Warning");
				objClass.getObjmanualaudit().setTableName("LScfttransaction");
				objClass.getObjmanualaudit().setComments(objClass.getObjuser().getComments());
	    		lscfttransactionRepository.save(objClass.getObjmanualaudit());
	    	}
			return objClass;
		}
		else if(objClass.getDomaincode() != null && objClass.getDomainstatus() != 1) {
			

			if(objClass.getObjsilentaudit() != null)
	    	{
				//objClass.getObjsilentaudit().setModuleName("UserManagement");
				//objClass.getObjsilentaudit().setComments("Insert Domain Successfully");
				//objClass.getObjsilentaudit().setActions("Insert Domain");
				//objClass.getObjsilentaudit().setSystemcoments("System Generated");
				objClass.getObjsilentaudit().setTableName("LSdomainMaster");
	    		lscfttransactionRepository.save(objClass.getObjsilentaudit());
	    	}
			
			if(objClass.getObjuser() != null) {
				//LScfttransaction manualAudit=new LScfttransaction();
				if(objClass.getObjmanualaudit() != null)
		    	{
				
				Date date = new Date();
				
				//manualAudit.setModuleName("UserManagement");
				//manualAudit.setComments("Insert Domain Successfully");
				//manualAudit.setActions("Insert Domain");
				//manualAudit.setSystemcoments("User Generated");
				objClass.getObjmanualaudit().setTableName("LSdomainMaster");
				objClass.getObjmanualaudit().setComments(objClass.getObjuser().getComments());
				//manualAudit.setManipulatetype("Insert");
				objClass.getObjmanualaudit().setLsuserMaster(objClass.getLSuserMaster().getUsercode());
				objClass.getObjmanualaudit().setLssitemaster(objClass.getLSuserMaster().getLssitemaster().getSitecode());
				objClass.getObjmanualaudit().setTransactiondate(date);
	    		lscfttransactionRepository.save(objClass.getObjmanualaudit());
			}
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
		
		//silent AuditTrail
		if(objClass.getObjsilentaudit() != null)
    	{
			//objClass.getObjsilentaudit().setModuleName("UserManagement");
			//objClass.getObjsilentaudit().setComments("Insert Domain Successfully");
			//objClass.getObjsilentaudit().setActions("Insert Domain");
			//objClass.getObjsilentaudit().setSystemcoments("System Generated");
			objClass.getObjsilentaudit().setTableName("LSdomainMaster");
    		lscfttransactionRepository.save(objClass.getObjsilentaudit());
    	}
		
		//Manual Audit
		if(objClass.getObjuser() != null) {
			//LScfttransaction manualAudit=new LScfttransaction();
			if(objClass.getObjmanualaudit() != null)
	    	{
			
			Date date = new Date();
			
			//manualAudit.setModuleName("UserManagement");
			//manualAudit.setComments("Insert Domain Successfully");
			//manualAudit.setActions("Insert Domain");
			//manualAudit.setSystemcoments("User Generated");
			objClass.getObjmanualaudit().setTableName("LSdomainMaster");
			objClass.getObjmanualaudit().setComments(objClass.getObjuser().getComments());
			//manualAudit.setManipulatetype("Insert");
			objClass.getObjmanualaudit().setLsuserMaster(objClass.getLSuserMaster().getUsercode());
			objClass.getObjmanualaudit().setLssitemaster(objClass.getLSuserMaster().getLssitemaster().getSitecode());
			objClass.getObjmanualaudit().setTransactiondate(date);
    		lscfttransactionRepository.save(objClass.getObjmanualaudit());
		}
		}
		return objClass;
	}

	public LSuserMaster importADSScreen(LSuserMaster objClass) {
		//silent AuditTrail
		if(objClass.getObjsilentaudit() != null)
		{
			objClass.getObjsilentaudit().setModuleName("UserManagement");
			objClass.getObjsilentaudit().setComments("Import ADSscreen Successfully");
			objClass.getObjsilentaudit().setActions("View / Load");
			objClass.getObjsilentaudit().setSystemcoments("System Generated");
			objClass.getObjsilentaudit().setTableName("LSuserMaster");
			lscfttransactionRepository.save(objClass.getObjsilentaudit());
		}

		//Manual Audit
		if(objClass.getObjuser() != null) {
			LScfttransaction manualAudit=new LScfttransaction();
			Date date = new Date();
			
			manualAudit.setModuleName("UserManagement");
			manualAudit.setComments("Import ADSscreen Successfully");
			manualAudit.setActions("View / Load");
			manualAudit.setSystemcoments("User Generated");
			manualAudit.setTableName("LSuserMaster");
			manualAudit.setManipulatetype("Insert");
//			manualAudit.setLsuserMaster(objClass);
//			manualAudit.setLssitemaster(objClass.getLssitemaster());
			manualAudit.setTransactiondate(date);
			lscfttransactionRepository.save(manualAudit);
		}
		objClass.setResponse(new Response());
		objClass.getResponse().setStatus(false);
		objClass.getResponse().setInformation("Username and password invalid");
		
		return objClass;								
	}

	public Response ADSDomainServerConnection(Map<String, Object> objMap) {
		
		Response res=new Response();
		
		Map<String, Object> objCredentials = new HashMap<>();
		
		ObjectMapper objMapper= new ObjectMapper();
		String sUsername = (String) objMap.get("sUsername");
		String sPassword = (String) objMap.get("sPassword");
		String sDomain = ((String) objMap.get("sDomain")).trim();
		try {

			String[] sDomainAry = sDomain.split(".");

			if (sDomainAry.length > 0) {
				sDomain = sDomainAry[0];
			}

//			String url = "ldap://" + sDomain + ".com";
			String userName =  sUsername ;
			String password = sPassword;
			
//			userName = userName.replace("\\", "/");
			
			objCredentials.put("sServerUserName", userName);
			objCredentials.put("sPassword", password);
			objCredentials.put("sDomainName", sDomain);
			
//			Boolean isConnect = ADS_Connection.CheckLDAPConnection(url, userName, password);
			Boolean isConnect = ADS_Connection.CheckDomainLDAPConnection(objCredentials);
			

			if (isConnect) {
				res.setInformation("ID_CONNECTSUCC");
				res.setStatus(true);
			
				LScfttransaction cfttransaction;
				if(objMap.containsKey("objsilentaudit")) {
					cfttransaction = objMapper.convertValue(objMap.get("objsilentaudit"), LScfttransaction.class);
					lscfttransactionRepository.save(cfttransaction);
				}
			} else {
				res.setInformation("ID_CONNECTFAIL");
				res.setStatus(false);
				LScfttransaction silentaudit;
				silentaudit = objMapper.convertValue(objMap.get("objsilentaudit"), LScfttransaction.class);
				silentaudit.setComments("Failed to connect domain server");
				silentaudit.setActions("Warning");
				silentaudit.setTableName("LSuserMaster");
				lscfttransactionRepository.save(silentaudit);
			}
		} catch (Exception ex) {
			res.setInformation("ID_CONNECTFAIL");
			res.setStatus(false);
		}
		return res;
	}

	public Map<String, Object> addImportADSUsers(Map<String, Object> objMap) {
		
		Map<String, Object> rtnMap=new HashMap<>();
		boolean isCompleted = false;
	
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> lstAdsUsers = (List<Map<String, Object>>) objMap.get("ADSUsers");	
		ObjectMapper objMapper= new ObjectMapper();
		
		if (!lstAdsUsers.isEmpty()) {
			
			@SuppressWarnings("unchecked")
			Map<String, Object> uGroup = (Map<String, Object>) objMap.get("userGroup");
			@SuppressWarnings("unchecked")
			Map<String, Object> uSite = (Map<String, Object>) uGroup.get("lssitemaster");
			
			LSusergroup userGroup = LSusergroupRepository.findOne((Integer) uGroup.get("usergroupcode"));
			LSSiteMaster sSiteCode = lSSiteMasterRepository.findBysitecode((Integer) uSite.get("sitecode"));
			
			rtnMap.put("LSusergroup", userGroup);
			rtnMap.put("LSSiteMaster", sSiteCode);
			
			List<LSuserMaster> lstUsers=new ArrayList<>();
			
			@SuppressWarnings("unchecked")
			Map<String, Object> sObject = (Map<String, Object>) objMap.get("objsilentaudit");
			@SuppressWarnings("unchecked")
			Map<String, Object> master = (Map<String, Object>) sObject.get("lsuserMaster");
			
			String sCreateBy = (String) master.get("username");
			String sRepeatedUser = "";
			
			for (int u = 0; u < lstAdsUsers.size(); u++) {
				String sUserDomainID = (String) lstAdsUsers.get(u).get("DomainUserID");
				
				List<LSuserMaster> lstUserName=lSuserMasterRepository.findByUsernameAndLssitemaster(sUserDomainID, sSiteCode);
				
				LSuserMaster lsUser=new LSuserMaster();
				
				if (lstUserName.isEmpty()) {
					
					String sUserFullName = (String) lstAdsUsers.get(u).get("UserName");
					int sApprove = Integer.parseInt((String) lstAdsUsers.get(u).get("sApprove"));

					String sUserStatus = "I";
					if (sApprove == 1)
						sUserStatus = "A";
					
					lsUser.setCreatedby(sCreateBy);
					lsUser.setLockcount(0);
					lsUser.setLsusergroup(userGroup);
					lsUser.setUserfullname(sUserFullName);
					lsUser.setUsername(sUserDomainID);
					lsUser.setUserstatus(sUserStatus);
					lsUser.setLssitemaster(sSiteCode);
				} 
				else {
					if(sRepeatedUser.length() > 0) {
						sRepeatedUser += (String) ", " + sUserDomainID;
					}else {
						sRepeatedUser = (String) sUserDomainID;
					}
				}
				lstUsers.add(lsUser);
			}
			rtnMap.put("sRepeatedUser", sRepeatedUser);
			if (lstUsers.size() > 0) {
				lSuserMasterRepository.save(lstUsers);
				
				isCompleted = true;
				
				
				LScfttransaction cfttransaction;
				if(objMap.containsKey("objsilentaudit")) {
					cfttransaction = objMapper.convertValue(objMap.get("objsilentaudit"), LScfttransaction.class);
					lscfttransactionRepository.save(cfttransaction);
				}
			} else {
				isCompleted = false;
				if(objMap.containsKey("objsilentaudit")) {
					LScfttransaction silentaudit;
					silentaudit = objMapper.convertValue(objMap.get("objsilentaudit"), LScfttransaction.class);
					silentaudit.setComments("Error in saving imported user");
					silentaudit.setActions("Warning");
					silentaudit.setTableName("LSuserMaster");
					lscfttransactionRepository.save(silentaudit);
				}
			}
		}
		if(isCompleted==false) {
		if(objMap.containsKey("objsilentaudit")) {
			LScfttransaction silentaudit;
			silentaudit = objMapper.convertValue(objMap.get("objsilentaudit"), LScfttransaction.class);
			silentaudit.setComments("Failed to import the user");
			silentaudit.setActions("Warning");
			silentaudit.setTableName("LSuserMaster");
			lscfttransactionRepository.save(silentaudit);
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
			listGroup = ADSGroupnameLoad();
			rtnObjMap.put("oResObj", listDomain);
			rtnObjMap.put("oResObj1", listGroup);
			if(Objclass.getObjsilentaudit() != null)
	    	{
				Objclass.getObjsilentaudit().setTableName("LSuserMaster");
	    		lscfttransactionRepository.save(Objclass.getObjsilentaudit());
	    	}

		} catch (Exception ex) {
			
		}
		return rtnObjMap;
	}

	public List<LSusergroup> ADSGroupnameLoad() {	
		
		return LSusergroupRepository.findByOrderByUsergroupcodeDesc();
	
	}

	public List<LSdomainMaster> ADSServerDomainLoad() {
		
		return lSDomainMasterRepository.findBycategoriesAndDomainstatus("server",1);
	}

	public List<LSuserMaster> UserMasterDetails(LSusergroup sUserGroupID, LSSiteMaster sSiteCode) {
		
		List<LSuserMaster> lstUsers = lSuserMasterRepository.findByLssitemasterAndLsusergroup(sSiteCode,sUserGroupID);
		
		return lstUsers;
	}

	public List<LSdomainMaster> LoadDomainMaster(LSSiteMaster objsite) {
		
		if(objsite.getObjsilentaudit() != null)
    	{
			//objsite.getObjsilentaudit().setModuleName("UserManagement");
			//objsite.getObjsilentaudit().setComments("Allow to view Domain");
			//objsite.getObjsilentaudit().setActions("view");
			//objsite.getObjsilentaudit().setSystemcoments("System Generated");
			objsite.getObjsilentaudit().setTableName("LSSiteMaster");
    		lscfttransactionRepository.save(objsite.getObjsilentaudit());
    	}
		
		return lSDomainMasterRepository.findBylssitemasterAndDomainstatus(objsite,1);
	}
public LSuserMaster validateuser(LSuserMaster objClass) {
		LSuserMaster objuser = new LSuserMaster();
		LSuserMaster objExitinguser = new LSuserMaster();
		String username = objClass.getUsername();
		objExitinguser = lSuserMasterRepository.findByusernameAndLssitemaster(username, objClass.getLssitemaster());
		objuser.setObjResponse(new Response());
		if(objExitinguser != null)
		{
			String Password = AESEncryption.decrypt(objExitinguser.getPassword());
			
		    if(Password.equals(objClass.getPassword()) && objExitinguser.getUserstatus()!="Locked")
		    {
		    	objuser.getObjResponse().setStatus(true);
		    	objuser.setUsername(AESEncryption.encrypt(objExitinguser.getUsername()));
		    	objuser.setPassword(objExitinguser.getPassword());
		    }
		    else if(!Password.equals(objClass.getPassword()))
		    {
		    	objuser.getObjResponse().setStatus(false);
				objuser.getObjResponse().setInformation("Password mismatch");
		    }
		    else
		    {
		    	objuser.getObjResponse().setStatus(false);
				objuser.getObjResponse().setInformation("User Locked");
		    }
		}
		else
		{
			objuser.getObjResponse().setStatus(false);
			objuser.getObjResponse().setInformation("Invalid user");
		}
		return objuser;
	}
	
	public LSuserMaster LinkLogin(LSuserMaster objClass) {
		LSuserMaster objuser = new LSuserMaster();
		LSuserMaster objExitinguser = new LSuserMaster();
		
		objExitinguser = lSuserMasterRepository.findByUsernameAndPassword(AESEncryption.decrypt(objClass.getUsername()), objClass.getPassword());
		
		if(objExitinguser != null)
		{
			objuser = objExitinguser;
			objuser.setObjResponse(new Response());
			objuser.getObjResponse().setStatus(true);
		}
		else
		{
			objuser.setObjResponse(new Response());
			objuser.getObjResponse().setStatus(false);
			objuser.getObjResponse().setInformation("Invalid user");
		}
		
		return objuser;
	}

	public LSSiteMaster InsertupdateSite(LSSiteMaster objClass) {
		objClass.setResponse(new Response());
		if(objClass.getSitecode() == null && lSSiteMasterRepository.findBySitenameIgnoreCaseAndIstatus(objClass.getSitename(),1) != null)
		{
			objClass.getResponse().setStatus(false);
			objClass.getResponse().setInformation("ID_EXIST");
//			silent audit
			if(objClass.getObjsilentaudit() != null)
	    	{   
				objClass.getObjsilentaudit().setActions("Warning");
				objClass.getObjsilentaudit().setComments(objClass.getUsername().getUsername()+" "+"made attempt to create existing site name");
				objClass.getObjsilentaudit().setTableName("LSusergroup");
	    		lscfttransactionRepository.save(objClass.getObjsilentaudit());
	    	}
//			manual audit
			if(objClass.getObjuser() != null)
	    	{
				objClass.getObjmanualaudit().setActions("Warning");
				objClass.getObjmanualaudit().setTableName("LScfttransaction");
				objClass.getObjmanualaudit().setComments(objClass.getObjuser().getComments());
	    		lscfttransactionRepository.save(objClass.getObjmanualaudit());
	    	}
			return objClass;
		}
		else if(objClass.getSitecode() != null && objClass.getSitecode() != 1) {
			if(objClass.getObjsilentaudit() != null)
	    	{
				objClass.getObjsilentaudit().setTableName("LSSiteMaster");
	    		lscfttransactionRepository.save(objClass.getObjsilentaudit());
	    	}
			
			if(objClass.getObjuser() != null) {
				if(objClass.getObjmanualaudit() != null)
		    	{	
					Date date = new Date();
			
					objClass.getObjmanualaudit().setTableName("LSSiteMaster");
					objClass.getObjmanualaudit().setComments(objClass.getObjuser().getComments());
					objClass.getObjmanualaudit().setLsuserMaster(objClass.getLSuserMaster().getUsercode());
					objClass.getObjmanualaudit().setLssitemaster(objClass.getLSuserMaster().getLssitemaster().getSitecode());
					objClass.getObjmanualaudit().setTransactiondate(date);
		    		lscfttransactionRepository.save(objClass.getObjmanualaudit());
				}
			}
			
//			objClass = lSSiteMasterRepository.findBysitecode(objClass.getSitecode());
//			if(objClass.getIstatus()==1) {
//			objClass.setIstatus(-1);
//			}
//			else	{
//				objClass.setIstatus(1);
//				}
			lSSiteMasterRepository.save(objClass);
			objClass.setResponse(new Response());
			objClass.getResponse().setStatus(true);
			objClass.getResponse().setInformation("ID_DOMAINDEL");
			
			return objClass;
		}
		if(objClass.getSitecode()==null) {
			List<LSSiteMaster> site= new ArrayList<LSSiteMaster>();
			site=lSSiteMasterRepository.findAll();
			if(site.size()==1) {
				objClass.setSitecode(2);
			}
		}
		
		lSSiteMasterRepository.save(objClass);
		objClass.getResponse().setStatus(true);
		objClass.getResponse().setInformation("");
		
		//silent AuditTrail
		if(objClass.getObjsilentaudit() != null)
    	{
			objClass.getObjsilentaudit().setTableName("LSSiteMaster");
    		lscfttransactionRepository.save(objClass.getObjsilentaudit());
    	}
		
		//Manual Audit
		if(objClass.getObjuser() != null) {
			if(objClass.getObjmanualaudit() != null)
	    	{
				Date date = new Date();
				
				objClass.getObjmanualaudit().setTableName("LSSiteMaster");
				objClass.getObjmanualaudit().setComments(objClass.getObjuser().getComments());
				objClass.getObjmanualaudit().setLsuserMaster(objClass.getLSuserMaster().getUsercode());
				objClass.getObjmanualaudit().setLssitemaster(objClass.getLSuserMaster().getLssitemaster().getSitecode());
				objClass.getObjmanualaudit().setTransactiondate(date);
	    		lscfttransactionRepository.save(objClass.getObjmanualaudit());
			}
		}
		return objClass;
	}
	
	public Map<String, Object> azureauthenticatelogin(LoggedUser objuser)
	{
		Map<String, Object> obj = new HashMap<>();
		LSuserMaster objExitinguser = new LSuserMaster();
		
		String username = objuser.getsUsername();
		LSSiteMaster objsite = lSSiteMasterRepository.findBysitecode(Integer.parseInt(objuser.getsSiteCode()));
		objExitinguser = lSuserMasterRepository.findByUsernameIgnoreCaseAndLoginfromAndLssitemaster(username,"1", objsite);
		
		if(objExitinguser != null)
		{
			objExitinguser.setObjResponse(new Response());
			objExitinguser.setObjsilentaudit(new LScfttransaction());
			if(Integer.parseInt(objuser.getsSiteCode()) == objExitinguser.getLssitemaster().getSitecode()) 
			{
				
			    if(objExitinguser.getUserstatus()!="Locked")
			    {
			    	
			    	String status = objExitinguser.getUserstatus();
			    	String groupstatus=objExitinguser.getLsusergroup().getUsergroupstatus();
			    	if(status.equals("Deactive"))
			    	{
			    		objExitinguser.getObjResponse().setInformation("ID_NOTACTIVE");
						objExitinguser.getObjResponse().setStatus(false);
						objuser.getObjsilentaudit().setActions("Warning");
						objuser.getObjsilentaudit().setComments(objExitinguser.getUsername()+" "+"was not active to login");
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
			    	else if(groupstatus.trim().equals("Deactive")) 
			    	{
			    		objExitinguser.getObjResponse().setInformation("ID_GRPNOACT");
						objExitinguser.getObjResponse().setStatus(false);
						  
						objuser.getObjsilentaudit().setActions("Warning");
						objuser.getObjsilentaudit().setComments("Currently group was not active for the user"+" "+objExitinguser.getUsername()+" "+"to login");
						objuser.getObjsilentaudit().setTableName("LSusergroup");
						objuser.getObjsilentaudit().setUsername(objExitinguser.getUsername());
						objuser.getObjsilentaudit().setSystemcoments("System Generated");
						objuser.getObjsilentaudit().setModuleName(ModuleName);
						objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
						objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
				    	lscfttransactionRepository.save(objuser.getObjsilentaudit());
				    	
						obj.put("user", objExitinguser);
						return obj;
			    	}
			    	else
			    	{
		    		
			    		try {
							Date newDate = new SimpleDateFormat( "yyyy/dd/MM hh:mm:ss" ).parse( "4444/31/12 23:58:57" );
							System.out.println(newDate);
							Locale locale = Locale.getDefault();
							DateFormat datetimeFormatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
							
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
							dateSString="MM-dd-yyyy hh:mm:ss";
		                	objExitinguser.setDFormat(dateSString);
						} catch (ParseException e) {
							e.printStackTrace();
						}
			    	
			    			
						    	objExitinguser.getObjResponse().setStatus(true);
						    	
						    	if(objuser.getObjsilentaudit() != null)
						    	{
						    		objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
						    		objuser.getObjsilentaudit().setLssitemaster(objExitinguser.getLssitemaster().getSitecode());
						    		objuser.getObjsilentaudit().setModuleName(ModuleName);
						    		objuser.getObjsilentaudit().setComments("User Logged in Successfully");
						    		objuser.getObjsilentaudit().setActions("Login Success");
						    		objuser.getObjsilentaudit().setSystemcoments("System Generated");
						    		objuser.getObjsilentaudit().setManipulatetype("Login");
						    		objuser.getObjsilentaudit().setTableName("LSactiveuser");
						    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
						    		
						    		LSactiveUser objactiveuser = new LSactiveUser();
						    	 	objactiveuser.setLsusermaster(objExitinguser);
						    	 	objactiveuser.setLssitemaster(objExitinguser.getLssitemaster());
						    	 	objactiveuser.setTimestamp(objuser.getLogindate());
						    		
						    	 	lsactiveUserRepository.save(objactiveuser);
						    	
						    	}
				    		
				    	}
			    	}
			    	
			}
			else {
				objExitinguser.getObjResponse().setInformation("ID_SITEVALID");
				objExitinguser.getObjResponse().setStatus(false);
				 
				objuser.getObjsilentaudit().setActions("Warning");
					objuser.getObjsilentaudit().setComments(objExitinguser.getUsername()+ " "+"does not belongs to the site");
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
		    
		else
		{
			objExitinguser = lSuserMasterRepository.findByusernameAndLssitemaster("Administrator", objsite);
			objExitinguser.setObjResponse(new Response());
			objExitinguser.getObjResponse().setInformation("ID_NOTEXIST");
			objExitinguser.getObjResponse().setStatus(false);
		  
			objuser.getObjsilentaudit().setActions("Warning");
			objuser.getObjsilentaudit().setComments("User"+" "+objuser.getsUsername()+ " "+"does not exist");
			objuser.getObjsilentaudit().setTableName("LSusergroup");
			objuser.getObjsilentaudit().setUsername(objExitinguser.getUsername());
			objuser.getObjsilentaudit().setSystemcoments("System Generated");
			objuser.getObjsilentaudit().setModuleName(ModuleName);
			objuser.getObjsilentaudit().setLsuserMaster(objExitinguser.getUsercode());
    		objuser.getObjsilentaudit().setLssitemaster(Integer.parseInt(objuser.getsSiteCode()));
    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
	    	
		}
		
		obj.put("user", objExitinguser);
		if(objExitinguser.getLsusergroup() != null)
		{
			obj.put("userrights", userService.GetUserRightsonGroup(objExitinguser.getLsusergroup()));
			LSaudittrailconfiguration objauditconfig = new LSaudittrailconfiguration();
			objauditconfig.setLsusermaster(objExitinguser);
			obj.put("auditconfig", auditService.GetAuditconfigUser(objauditconfig));
		}
		
		return obj;

	}
	
	public LSuserMaster createuserforazure(LSuserMaster objuser)
	{
		LSuserMaster userDetails = lsuserMasterRepository.findByUsernameIgnoreCaseAndLoginfromAndLssitemaster(objuser.getUsername(),"1",objuser.getLssitemaster());

		if(userDetails == null)
		{
			if(objuser.getIsmultitenant() != null && objuser.getMultitenantusercount() != null && objuser.getIsmultitenant() == 1)
			{
//				if(lsuserMasterRepository.countByusercodeNot(1) >= objuser.getMultitenantusercount())
					if(lsuserMasterRepository.countByusercodeNotAndUserretirestatusNot(1,1) >= objuser.getMultitenantusercount())
				{
					Response objResponse = new Response();
					objResponse.setStatus(false);
					objResponse.setInformation("ID_USERCOUNTEXCEEDS");
					objuser.setObjResponse(objResponse);
					
					return objuser;
				}
			}
			LSusergroup objaadsgroup = LSusergroupRepository.findByusergroupnameAndLssitemaster("Azure aads", objuser.getLssitemaster().getSitecode());
			LSusergroup objgroup = new LSusergroup();
			if(objaadsgroup == null)
			{
				
				objgroup.setUsergroupname("Azure aads");
				objgroup.setLssitemaster(objuser.getLssitemaster().getSitecode());
				objgroup.setCreatedby(objuser.getUsername());
				objgroup.setModifiedby(objuser.getUsername());
				objgroup.setCreatedon(objuser.getCreateddate());
				objgroup.setModifiedon(objuser.getCreateddate());
				objgroup.setUsergroupstatus("A");
				
				LSusergroupRepository.save(objgroup);
				
				objuser.setLsusergroup(objgroup);
			}
			else
			{
				objuser.setLsusergroup(objaadsgroup);
			}
			
			objuser.setCreatedby(objuser.getUsername());
			objuser.setModifiedby(objuser.getUsername());
			objuser.setUserstatus("A");
			objuser.setLockcount(0);
			objuser.setPassword(objuser.getToken());
			
			Response objResponse = new Response();
			objResponse.setStatus(true);
			objuser.setObjResponse(objResponse);
			objuser.setLoginfrom("1");
			lsuserMasterRepository.save(objuser);
			
			String unifieduser = objuser.getUsername().toLowerCase().replaceAll("[^a-zA-Z0-9]", "")+"u"+objuser.getUsercode()+"s"+objuser.getLssitemaster().getSitecode()+
					objuser.getUnifieduserid();
			
			objuser.setUnifieduserid(unifieduser);
			lsuserMasterRepository.save(objuser);
		}
		else
		{
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

		if(objuser.getUsername() == null) return null;
		
		LSuserMaster userDetails = lsuserMasterRepository.findByUsernameIgnoreCaseAndLoginfromAndLssitemaster(objuser.getUsername(),"1",objuser.getLssitemaster());

		
		LSPasswordPolicy policydays= LSPasswordPolicyRepository.findByLssitemaster(objuser.getLssitemaster());
		Calendar c = Calendar.getInstance(); 
		c.add(Calendar.DATE, policydays.getPasswordexpiry());
		
		if(userDetails == null)
		{
			
			LSusergroup objaadsgroup = LSusergroupRepository.findByusergroupnameAndLssitemaster("Azure aads", objuser.getLssitemaster().getSitecode());
			LSusergroup objgroup = new LSusergroup();
			if(objaadsgroup == null)
			{
				
				objgroup.setUsergroupname("Azure aads");
				objgroup.setLssitemaster(objuser.getLssitemaster().getSitecode());
				objgroup.setCreatedby(objuser.getUsername());
				objgroup.setModifiedby(objuser.getUsername());
				objgroup.setCreatedon(objuser.getCreateddate());
				objgroup.setModifiedon(objuser.getCreateddate());
				objgroup.setUsergroupstatus("A");
				
				LSusergroupRepository.save(objgroup);
				
				objuser.setLsusergroup(objgroup);
			}
			else
			{
				objuser.setLsusergroup(objaadsgroup);
			}
			
			objuser.setCreatedby(objuser.getUsername());
			objuser.setModifiedby(objuser.getUsername());
			objuser.setUserstatus("A");
			objuser.setLockcount(0);
			objuser.setPassword(objuser.getToken());
			objuser.setPasswordexpirydate(c.getTime());
		
			objuser.setLoginfrom("1");
			lsuserMasterRepository.save(objuser);
		}
		else
		{
			objuser.setPassword(objuser.getToken());
			userDetails.setPassword(objuser.getToken());
			objuser.setPasswordexpirydate(userDetails.getPasswordexpirydate()==null? c.getTime(): userDetails.getPasswordexpirydate());
			userDetails.setPasswordexpirydate(userDetails.getPasswordexpirydate()==null? c.getTime(): userDetails.getPasswordexpirydate());
			lsuserMasterRepository.save(userDetails);
		}
		
		String Tokenuser = objuser.getUsername() +"["+objuser.getLssitemaster().getSitecode()+"]";
		
		final UserDetails userDetailstoken = userDetailsService.loadUserByUsername(Tokenuser);
		
		final String token = jwtTokenUtil.generateToken(userDetailstoken);

		return ResponseEntity.ok(new JwtResponse(token));
	}
}
