package com.agaram.eln.primary.service.multitenant;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.mail.MessagingException;
import javax.sql.DataSource;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.agaram.eln.config.AESEncryption;
import com.agaram.eln.primary.config.DataSourceBasedMultiTenantConnectionProviderImpl;
import com.agaram.eln.primary.config.TenantDataSource;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.multitenant.CustomerSubscription;
import com.agaram.eln.primary.model.multitenant.DataSourceConfig;
import com.agaram.eln.primary.model.multitenant.Invoice;
import com.agaram.eln.primary.model.notification.Email;
import com.agaram.eln.primary.model.usermanagement.LSPasswordPolicy;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.repository.multitenant.CustomerSubscriptionRepository;
import com.agaram.eln.primary.repository.multitenant.DataSourceConfigRepository;
import com.agaram.eln.primary.repository.multitenant.InvoiceRepository;
import com.agaram.eln.primary.repository.usermanagement.LSPasswordPolicyRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.service.notification.EmailService;
import com.agaram.eln.secondary.config.ArchiveDataSourceBasedMultiTenantConnectionProviderImpl;

@Service
public class DatasourceService {

	@Autowired
	private Environment env;

	@Autowired
	private DataSourceConfigRepository configRepo;

	@Autowired
	private CustomerSubscriptionRepository CustomerSubscriptionRepository;

	@Autowired
	private DataSourceBasedMultiTenantConnectionProviderImpl dataSourceBasedMultiTenantConnectionProviderImpl;

	@Autowired
	private ArchiveDataSourceBasedMultiTenantConnectionProviderImpl archiveDataSourceBasedMultiTenantConnectionProviderImpl;

	@Autowired
	TenantDataSource objtenantsource;

	@Autowired
	private EmailService emailService;

	@Autowired
	private LSuserMasterRepository lsuserMasterRepository;

	@Autowired
	private LSPasswordPolicyRepository LSPasswordPolicyRepository;

	@Autowired
	private InvoiceRepository InvoiceRepository;

	public DataSourceConfig Validatetenant(DataSourceConfig Tenantname) {

		// DataSourceConfig objdatasource =
		// configRepo.findByTenantid(Tenantname.getTenantid());

		DataSourceConfig objdatasource = configRepo.findByTenantidIgnoreCase(Tenantname.getTenantid());

		Response objreponse = new Response();
		if (objdatasource != null) {
			if (objdatasource.isInitialize() && objdatasource.isIsenable()) {
				objreponse.setStatus(true);
				objdatasource.setObjResponse(objreponse);

//				try {
//					Connection con = dataSourceBasedMultiTenantConnectionProviderImpl.getConnection(objdatasource.getName());
//					if(con.isClosed())
//					{
//						objreponse.setStatus(false);
//					}
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			} else if (!objdatasource.isInitialize()) {
				objreponse.setInformation("ID_ORGREGINPROGRESS");
				objreponse.setStatus(false);
				objdatasource.setObjResponse(objreponse);
			} else if (!objdatasource.isIsenable()) {
				objreponse.setInformation("ID_ORGDISABLED");
				objreponse.setStatus(false);
				objdatasource.setObjResponse(objreponse);
			}

		} else {
			objreponse.setInformation("ID_ORGNOTEXIST");
			objreponse.setStatus(false);
			objdatasource = new DataSourceConfig();
			objdatasource.setObjResponse(objreponse);
		}

		return objdatasource;
	}

	public DataSourceConfig Registertenant(DataSourceConfig Tenantname) throws MessagingException, IOException {
		DataSourceConfig objconfig = configRepo.findByTenantid(Tenantname.getTenantid().trim());
		Response objres = new Response();

		if (objconfig != null) {
			DataSourceConfig objdata = new DataSourceConfig();
			objres.setStatus(false);
			objres.setInformation("Organisation ID already esixts.");
			objdata.setObjResponse(objres);
			return objdata;
		}

		Tenantname.setInitialize(false);
		Tenantname.setIsenable(false);

		objres.setStatus(true);
		Tenantname.setObjResponse(objres);

		String password = Generatetenantpassword();
		String passwordtenant = AESEncryption.encrypt(password);
		Tenantname.setTenantpassword(passwordtenant);

		configRepo.save(Tenantname);

//		Email email = new Email();

		if (!Tenantname.getAdministratormailid().equals("")) {
//			int countmail=2;
			String mails[] = { Tenantname.getUseremail(), Tenantname.getAdministratormailid() };
			for (int i = 0; i < mails.length; i++) {
				Email email = new Email();
				email.setMailto(mails[i]);
				email.setSubject("Tenant Username & Password");
//				email.setMailcontent(
//						"<b>Dear Customer</b>,<br><i>This is for your username and password</i><br><b>UserName:\t\t"
//								+ Tenantname.getTenantid() + "</b><br><b>Password:\t\t" + password
//								+ "</b><br><b><a href=" + Tenantname.getLoginpath() + ">click here to login</a></b>");
//				emailService.sendEmail(email);

				email.setMailcontent(
						"<b>Dear Customer,</b><br><center><img src=\"cid:image\"  style =width:120px; height:100px border: 3px;'></center><br><br>"
								+ "<p><p>Thanks for Registering your Organization in Logilab ELN.</p>Please use below mentioned Username and password for Tenant Login.<br><br>"
								+ "Click the URL mentioned below to generate the OTP for verifying the Organization.<br><br>"
								+ "Once the verification is completed, you will receive an email with the ELN Administrator login details.<br><br>"
								+ "<b style='margin-left: 76px;'>Username:</b>\t\t " + Tenantname.getTenantid()
								+ "<br><br>" + "<b style='margin-left: 76px;'>Password &nbsp;:</b>\t\t" + password
								+ "<br><br>" + "<b style='margin-left: 76px;'><a href=" + Tenantname.getLoginpath()
								+ ">Click here to Tenant Login</a></b><br><br>"
								+ "<p>If you have any queries, please contact our support by mail to info@agaramtech.com <br><br><br>"
								+ "Regards,</p>" + "<b>Agaram Technologies Private Limited</b><br><br>"
								+ "<img src=\"cid:seconimage\"  style ='width:120px; height:100px;border: 3px;'"
								+ "<br><br><p>T: +91 44 4208 2005</p><p>T: +91 44 42189406</p>"
								+ "W:<a href='https://www.agaramtech.com'>https://www.agaramtech.com</a></p>");
				emailService.sendEmail(email);
			}
		} else {
			Email email = new Email();
			email.setMailto(Tenantname.getUseremail());
			email.setSubject("Tenant Username & Password");
//			email.setMailcontent(
//					"<b>Dear Customer</b>,<br><i>This is for your username and password</i><br><b>UserName:\t\t"
//							+ Tenantname.getTenantid() + "</b><br><b>Password:\t\t" + password + "</b><br><b><a href="
//							+ Tenantname.getLoginpath() + ">click here to login</a></b>");
//			emailService.sendEmail(email);
			email.setMailcontent(
					"<b>Dear Customer,</b><br><center><img src=\"cid:image\"  style =width:120px; height:100px border: 3px;'></center><br><br>"
							+ "<p><p>Thanks for Registering your Organization in Logilab ELN.</p>Please use below mentioned Username and password for Tenant Login.<br><br>"
							+ "Click the URL mentioned below to generate the OTP for verifying the Organization.<br><br>"
							+ "Once the verification is completed, you will receive an email with the ELN Administrator login details.<br><br>"
							+ "<b style='margin-left: 76px;'>Username:</b>\t\t " + Tenantname.getTenantid() + "<br><br>"
							+ "<b style='margin-left: 76px;'>Password &nbsp;:</b>\t\t" + password + "<br><br>"
							+ "<b style='margin-left: 76px;'><a href=" + Tenantname.getLoginpath()
							+ ">Click here to Tenant Login</a></b><br><br>"
							+ "<p>If you have any queries, please contact our support by mail to info@agaramtech.com <br><br><br>"
							+ "Regards,</p>" + "<b>Agaram Technologies Private Limited</b><br><br>"
							+ "<img src=\"cid:seconimage\"  style ='width:120px; height:100px;border: 3px;'"
							+ "<br><br><p>T: +91 44 4208 2005</p><p>T: +91 44 42189406</p>"
							+ "W:<a href='https://www.agaramtech.com'>https://www.agaramtech.com</a></p>");
			emailService.sendEmail(email);

		}

		return Tenantname;
	}

	public DataSourceConfig Registertenantid(DataSourceConfig Tenantname) throws MessagingException, IOException {
		DataSourceConfig objconfig = configRepo.findByTenantid(Tenantname.getTenantid().trim());
		Response objres = new Response();

		if (objconfig != null) {
//			DataSourceConfig objdata = new DataSourceConfig();
			objres.setStatus(false);
			objres.setInformation("Organisation ID already esixts.");
//			objdata.setObjResponse(objres);
			Tenantname.setObjResponse(objres);
			return Tenantname;
		}

		Tenantname.setInitialize(false);
		Tenantname.setIsenable(false);

		objres.setStatus(true);
		Tenantname.setObjResponse(objres);

		String password = Generatetenantpassword();
		String passwordtenant = AESEncryption.encrypt(password);
		Tenantname.setTenantpassword(passwordtenant);

//		CustomerSubscriptionRepository.save(Tenantname.getCustomerSubscription());
		configRepo.save(Tenantname);
		objres.setInformation("Organisation ID Successfully Created");
		Tenantname.setObjResponse(objres);

//		Email email = new Email();

		if (!Tenantname.getAdministratormailid().equals("")) {
//			int countmail=2;
			String mails[] = { Tenantname.getUseremail(), Tenantname.getAdministratormailid() };
			for (int i = 0; i < mails.length; i++) {
				Email email = new Email();
				email.setMailto(mails[i]);
				email.setSubject("Tenant Username & Password");
//				email.setMailcontent(
//						"<b>Dear Customer</b>,<br><i>This is for your username and password</i><br><b>UserName:\t\t"
//								+ Tenantname.getTenantid() + "</b><br><b>Password:\t\t" + password
//								+ "</b><br><b><a href=" + Tenantname.getLoginpath() + ">click here to login</a></b>");
//				emailService.sendEmail(email);
				email.setMailcontent(
						"<b>Dear Customer,</b><br><center><img src=\"cid:image\"  style =width:120px; height:100px border: 3px;'></center><br><br>"
								+ "<p><p>Thanks for Registering your Organization in Logilab ELN.</p>Please use below mentioned Username and password for Tenant Login.<br><br>"
								+ "Click the URL mentioned below to generate the OTP for verifying the Organization.<br><br>"
								+ "Once the verification is completed, you will receive an email with the ELN Administrator login details.<br><br>"
								+ "<b style='margin-left: 76px;'>Username:</b>\t\t " + Tenantname.getTenantid()
								+ "<br><br>" + "<b style='margin-left: 76px;'>Password &nbsp;:</b>\t\t" + password
								+ "<br><br>" + "<b style='margin-left: 76px;'><a href=" + Tenantname.getLoginpath()
								+ ">Click here to Tenant Login</a></b><br><br>"
								+ "<p>If you have any queries, please contact our support by mail to info@agaramtech.com <br><br><br>"
								+ "Regards,</p>" + "<b>Agaram Technologies Private Limited</b><br><br>"
								+ "<img src=\"cid:seconimage\"  style ='width:120px; height:100px;border: 3px;'"
								+ "<br><br><p>T: +91 44 4208 2005</p><p>T: +91 44 42189406</p>"
								+ "W:<a href='https://www.agaramtech.com'>https://www.agaramtech.com</a></p>");
				emailService.sendEmail(email);
			}
		} else {
			Email email = new Email();
			email.setMailto(Tenantname.getUseremail());
			email.setSubject("Tenant Username & Password");
//			email.setMailcontent(
//					"<b>Dear Customer</b>,<br><i>This is for your username and password</i><br><b>UserName:\t\t"
//							+ Tenantname.getTenantid() + "</b><br><b>Password:\t\t" + password + "</b><br><b><a href="
//							+ Tenantname.getLoginpath() + ">click here to login</a></b>");
//			emailService.sendEmail(email);
			email.setMailcontent(
					"<b>Dear Customer,</b><br><center><img src=\"cid:image\"  style =width:120px; height:100px border: 3px;'></center><br><br>"
							+ "<p><p>Thanks for Registering your Organization in Logilab ELN.</p>Please use below mentioned Username and password for Tenant Login.<br><br>"
							+ "Click the URL mentioned below to generate the OTP for verifying the Organization.<br><br>"
							+ "Once the verification is completed, you will receive an email with the ELN Administrator login details.<br><br>"
							+ "<b style='margin-left: 76px;'>Username:</b>\t\t " + Tenantname.getTenantid() + "<br><br>"
							+ "<b style='margin-left: 76px;'>Password &nbsp;:</b>\t\t" + password + "<br><br>"
							+ "<b style='margin-left: 76px;'><a href=" + Tenantname.getLoginpath()
							+ ">Click here to Tenant Login</a></b><br><br>"
							+ "<p>If you have any queries, please contact our support by mail to info@agaramtech.com <br><br><br>"
							+ "Regards,</p>" + "<b>Agaram Technologies Private Limited</b><br><br>"
							+ "<img src=\"cid:seconimage\"  style ='width:120px; height:100px;border: 3px;'"
							+ "<br><br><p>T: +91 44 4208 2005</p><p>T: +91 44 42189406</p>"
							+ "W:<a href='https://www.agaramtech.com'>https://www.agaramtech.com</a></p>");
			emailService.sendEmail(email);
		}

		return Tenantname;
	}

	public CustomerSubscription Registercustomersubscription(CustomerSubscription CustomerSubscription) {
		Response objres = new Response();
		if (CustomerSubscription.getCustomer_subscription_id() != null) {

			CustomerSubscriptionRepository.save(CustomerSubscription);

			if (CustomerSubscription.getPlan_type() != null) {
				DataSourceConfig getTenant = configRepo.findById(Long.valueOf(CustomerSubscription.getId()));
				if (getTenant != null) {
					getTenant.setPackagetype(
							CustomerSubscription.getType().equalsIgnoreCase("Free") ? 3914465000000065045L
									: CustomerSubscription.getType().equalsIgnoreCase("Standard") ? 3914465000000065049L
											: 3914465000000065053L);

					if (CustomerSubscription.getType().equalsIgnoreCase("cancel")) {
						getTenant.setIsenable(false);
					}

					getTenant.setNoofusers(CustomerSubscription.getNumber_of_users());

					configRepo.save(getTenant);
				} else {
					objres.setStatus(false);
					objres.setInformation("Please Pass CustomerSubscriptionID ");
					CustomerSubscription.setObjResponse(objres);
					return CustomerSubscription;
				}
			}

			objres.setStatus(true);
			objres.setInformation("Customer Subscription Successfully stored ");
			CustomerSubscription.setObjResponse(objres);
		} else {
			objres.setStatus(false);
			objres.setInformation("Please Pass CustomerSubscriptionID ");
			CustomerSubscription.setObjResponse(objres);
		}

		return CustomerSubscription;
	}

	public Invoice Registerinvoice(Invoice invoice) {

		Response objres = new Response();
		if (invoice.getCustomerSubscription() != null) {
			CustomerSubscription CustomerSubscription = CustomerSubscriptionRepository
					.findBycustomersubscriptionid(invoice.getCustomerSubscription().getCustomer_subscription_id());
			invoice.setCustomerSubscription(CustomerSubscription);
			invoice.setCustomersubscriptionid(CustomerSubscription.getCustomer_subscription_id());
			InvoiceRepository.save(invoice);

			if (invoice.getInvoice_status() != null) {
				DataSourceConfig getTenant = configRepo.findById(Long.valueOf(CustomerSubscription.getId()));
				if (invoice.getInvoice_status().equalsIgnoreCase("overdue")) {
					getTenant.setIsenable(false);
					configRepo.save(getTenant);
				}
			}

			objres.setStatus(true);
			objres.setInformation("Invoice Successfully created ");
			invoice.setObjResponse(objres);
		} else {
			objres.setStatus(false);
			objres.setInformation("Please Pass CustomerSubscriptionID ");
			invoice.setObjResponse(objres);
		}
		return invoice;
	}

	private String Generatetenantpassword() {

		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*-_=+\',/?";
		String pwd = RandomStringUtils.random(15, characters);

		return pwd;
	}

	public boolean createDatabase(String url, String databasename, DataSourceConfig config) {
		String user = env.getProperty("app.datasource.eln.username");
		String password = env.getProperty("app.datasource.eln.password");

		try (Connection con = DriverManager.getConnection(url, user, password);
				Statement st = con.createStatement();

				ResultSet rs = st.executeQuery("SELECT VERSION()")) {
			st.execute("CREATE DATABASE " + databasename);
			st.execute("CREATE DATABASE " + databasename + "archive");
			con.commit();
			if (rs.next()) {
				System.out.println(rs.getString(1));
			}

		} catch (SQLException ex) {

//	            Logger lgr = Logger.getLogger(JavaPostgreSqlVersion.class.getName());
//	            lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}

		DataSource datasource = createDataSource(databasename, config.getUrl(), config);
		objtenantsource.addDataSource(datasource, databasename);
		dataSourceBasedMultiTenantConnectionProviderImpl.addDataSource(datasource, databasename);
//	        Flyway flyway = Flyway.configure().dataSource(datasource).load();
//            flyway.repair();
//            flyway.migrate();
//            

		DataSource archivedatasource = createDataSource(databasename + "archive", config.getArchiveurl(), config);
		objtenantsource.addarchiveDataSource(archivedatasource, databasename + "archive");
		archiveDataSourceBasedMultiTenantConnectionProviderImpl.addDataSource(archivedatasource,
				databasename + "archive");
//            Flyway archiveflyway = Flyway.configure().dataSource(archivedatasource).locations("filesystem:./src/main/resources/db/migration_archive").load();
//            archiveflyway.repair();
//            archiveflyway.migrate();

		return true;
	}

	public List<DataSourceConfig> Getalltenant() {
		return configRepo.findAllByOrderByIdDesc();
	}

	public DataSourceConfig Gettenantonid(DataSourceConfig Tenant) {

		DataSourceConfig datasourceconfig = configRepo.findOne(Tenant.getId());

//		CustomerSubscription getSubs = CustomerSubscriptionRepository
//				.findBycustomersubscriptionid(datasourceconfig.getCustomerSubscription().getCustomer_subscription_id());

		CustomerSubscription getSubs = CustomerSubscriptionRepository.findByid(datasourceconfig.getId());

		Invoice invoice = new Invoice();

		if (getSubs != null) {
			invoice = InvoiceRepository.findBycustomersubscriptionid(getSubs.getCustomer_subscription_id());
		}

		datasourceconfig.setInvoice(invoice);

		return datasourceconfig;
	}

	public DataSourceConfig Updatetenant(DataSourceConfig Tenant) {
		configRepo.save(Tenant);
		return Tenant;
	}

	public DataSourceConfig Initiatetenant(DataSourceConfig Tenant) {
		String Databasename = Tenant.getTenantid().toLowerCase().replaceAll("[^a-zA-Z0-9]", "") + Tenant.getId();
		Tenant.setName(Databasename);
		Tenant.setArchivename(Databasename + "archive");
		Tenant.setUrl(gettenanturlDataBasename(Databasename));
		Tenant.setArchiveurl(gettenanturlDataBasename(Databasename + "archive"));
		Tenant.setDriverClassName(env.getProperty("app.datasource.eln.driverClassName"));
		Tenant.setUsername(env.getProperty("app.datasource.eln.username"));
		Tenant.setPassword(env.getProperty("app.datasource.eln.password"));

		configRepo.save(Tenant);

		createDatabase(env.getProperty("app.datasource.eln.url"), Databasename, Tenant);

		return Tenant;
	}

	private DataSource createDataSource(String name, String url, DataSourceConfig config) {
		if (config != null) {
			DataSourceBuilder factory = DataSourceBuilder.create().driverClassName(config.getDriverClassName())
					.username(config.getUsername()).password(config.getPassword()).url(url);
			DataSource ds = factory.build();
			return ds;
		}
		return null;
	}

	private String gettenanturlDataBasename(String tenantDatabase) {
		String url = env.getProperty("app.datasource.eln.url");
		String tenanturl = "";
		String[] arrremoveappname = url.split("\\?");
		if (arrremoveappname != null && arrremoveappname.length > 0) {
			String urlvalue = arrremoveappname[0];
			String[] arrurl = urlvalue.split("\\/");
			if (arrurl != null && arrurl.length > 0) {
				arrurl[arrurl.length - 1] = tenantDatabase;
				for (int i = 0; i < arrurl.length; i++) {
					if (i != 0) {
						tenanturl += "/";
					}
					tenanturl += arrurl[i];
				}
				if (arrremoveappname.length > 1) {
					tenanturl += "?" + arrremoveappname[arrremoveappname.length - 1];
				}
			}
		}
		return tenanturl;
	}

	public int Updaprofiletetenant(DataSourceConfig Tenantname) {
		int value = configRepo.setcontactandaddressandstateandcityandpincodeandcountry(Tenantname.getTenantcontactno(),
				Tenantname.getTenantaddress(), Tenantname.getTenantstate(), Tenantname.getTenantcity(),
				Tenantname.getTenantpincode(), Tenantname.getTenantcountry(), Tenantname.getTenantid());
		return value;

	}

	@SuppressWarnings("unused")
	public Map<String, Object> login(LoggedUser objuser) {

		Map<String, Object> obj = new HashMap<>();
		LSuserMaster objExitinguser = new LSuserMaster();
		String username = objuser.getsUsername();
		objExitinguser = lsuserMasterRepository.findByUsernameIgnoreCaseAndLoginfrom(username, "0");
		LSPasswordPolicy lockcount = objExitinguser != null
				? LSPasswordPolicyRepository
						.findTopByAndLssitemasterOrderByPolicycodeDesc(objExitinguser.getLssitemaster())
				: null;
//		if(objExitinguser != null && objExitinguser.getLssitemaster().getSitecode().toString().equals(objuser.getsSiteCode()))
		if (objExitinguser != null) {
			objExitinguser.setObjResponse(new Response());
			if (Integer.parseInt(objuser.getsSiteCode()) == objExitinguser.getLssitemaster().getSitecode()) {
				String Password = AESEncryption.decrypt(objExitinguser.getPassword());
				System.out.println(" password: " + Password);

				Date passwordexp = objExitinguser.getPasswordexpirydate();
				if (Password.equals(objuser.getsPassword()) && objExitinguser.getUserstatus() != "Locked") {
//					objExitinguser.setLsusergroup(objExitinguser.getMultiusergroupcode().get(0).getLsusergroup());
//					String status = objExitinguser.getUserstatus();
//					String groupstatus = objExitinguser.getLsusergroup().getUsergroupstatus();

//					if (status.equals("Deactive")) {
//						objExitinguser.getObjResponse().setInformation("ID_NOTACTIVE");
//						objExitinguser.getObjResponse().setStatus(false);
//						obj.put("user", objExitinguser);
//						return obj;
//					} else if (groupstatus.trim().equals("Deactive")) {
//						objExitinguser.getObjResponse().setInformation("ID_GRPNOACT");
//						objExitinguser.getObjResponse().setStatus(false);
//
//						obj.put("user", objExitinguser);
//						return obj;
//					} else {
					objExitinguser.getObjResponse().setStatus(true);
					obj.put("user", objExitinguser);
//					}
				} else if (!Password.equals(objuser.getsPassword()) || objExitinguser.getLockcount() == 5
						|| objExitinguser.getUserstatus() == "Locked") {
					if (!username.trim().toLowerCase().equals("administrator")) {
						Integer count = objExitinguser.getLockcount() == null ? 0 : objExitinguser.getLockcount();
						count++;
						if (count.equals(lockcount.getLockpolicy())) {
							objExitinguser.setUserstatus("Locked");
							objExitinguser.setLockcount(count++);
							objExitinguser.getObjResponse().setInformation("ID_LOCKED");
							objExitinguser.getObjResponse().setStatus(false);
							lsuserMasterRepository.save(objExitinguser);
							obj.put("user", objExitinguser);
							return obj;
						} else if (count < lockcount.getLockpolicy()) {
							objExitinguser.setLockcount(count++);
							objExitinguser.getObjResponse().setInformation("ID_INVALID");
							objExitinguser.getObjResponse().setStatus(false);
							lsuserMasterRepository.save(objExitinguser);
							obj.put("user", objExitinguser);
							return obj;
						}
					} else {
						objExitinguser.getObjResponse().setInformation("ID_INVALID");
						objExitinguser.getObjResponse().setStatus(false);
						obj.put("user", objExitinguser);
						return obj;
					}
				} else {
					objExitinguser.getObjResponse().setStatus(false);
					obj.put("user", objExitinguser);
				}
			}

		}

		return obj;

	}

	public Map<String, Object> checktenantid(DataSourceConfig DataSourceConfig) {
		DataSourceConfig objDataSourceConfig = new DataSourceConfig();
		String username = DataSourceConfig.getTenantid();
		Map<String, Object> mapOrder = new HashMap<String, Object>();
		objDataSourceConfig = configRepo.findByTenantid(username);
//		String Password = AESEncryption.decrypt(objDataSourceConfig.getPassword());
//		objDataSourceConfig.setPassword(Password);
		mapOrder.put("tenantId", objDataSourceConfig);
//		if((mapOrder == null))
//		{
//			mapOrder.put("information","Invalid user");
//		}
		return mapOrder;

	}

	public DataSourceConfig tenantlogin(DataSourceConfig tenant) {
		String Password = "";
		DataSourceConfig objtenant = configRepo.findByTenantid(tenant.getTenantid());
		if (objtenant != null) {
			Password = AESEncryption.decrypt(objtenant.getTenantpassword());
		}

		Response objresponse = new Response();

		if (Password.equals(tenant.getTenantpassword())) {
			objresponse.setStatus(true);
		} else {
			objresponse.setStatus(false);
		}

		objtenant.setObjResponse(objresponse);

		return objtenant;

	}

	public DataSourceConfig sendotp(DataSourceConfig Tenantname) throws MessagingException, IOException {

		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		String otp = String.format("%06d", number);

		Tenantname.setVarificationOTP(otp);

		configRepo.setotp(Tenantname.getVarificationOTP(), Tenantname.getTenantid());

//		Email email = new Email();

		if (Tenantname.getAdministratormailid() != null && !Tenantname.getAdministratormailid().equals("")) {
//			int countmail=2;
			String mails[] = { Tenantname.getUseremail(), Tenantname.getAdministratormailid() };
			for (int i = 0; i < mails.length; i++) {
				Email email = new Email();
				email.setMailto(mails[i]);
//				email.setMailto(Tenantname.getUseremail());
				email.setSubject(" Tenant OTP Verification");
//				email.setMailcontent("<b>Dear Customer</b>,<br><i>use code <b>" + otp
//						+ "</b> to login our account Never share your OTP with anyone</i>");
				email.setMailcontent(
						"<b>Dear Customer,</b><br><center><img src=\"cid:image\"  style =width:120px; height:100px border: 3px;'></center><br><br>"
								+ "<p><p>Thanks for generating an OTP in Logilab ELN.</p>Please use below mentioned OTP for Verifying your Tenant Registration.<br><br>"
								+ "Once the verification is completed, you will receive an email with the ELN Administrator login details. <br><br>"
								+ "<b style='margin-left: 76px;'>OTP: </b>\t\t " + otp + "<br><br>"
								+ "<p>If you have any queries, please contact our support by mail to info@agaramtech.com <br><br><br>"
								+ "Regards,</p>" + "<b>Agaram Technologies Private Limited</b><br><br>"
								+ "<img src=\"cid:seconimage\"  style ='width:120px; height:120px;border: 3px;'"
								+ "<br><br><p>T: +91 44 4208 2005</p><p>T: +91 44 42189406</p>"
								+ "W:<a href='https://www.agaramtech.com'>https://www.agaramtech.com</a></p>");
//				emailService.sendmailOPT(email);
				emailService.sendEmail(email);
			}
		} else {
			Email email = new Email();
			email.setMailto(Tenantname.getUseremail());
			email.setSubject("This is an OTP verification email");
//			email.setMailcontent("<b>Dear Customer</b>,<br><i>use code <b>" + otp
//					+ "</b> to login our account Never share your OTP with anyone</i>");
//			emailService.sendmailOPT(email);
			email.setMailcontent(
					"<b>Dear Customer,</b><br><center><img src=\"cid:image\"  style =width:120px; height:100px border: 3px;'></center><br><br>"
							+ "<p><p>Thanks for generating an OTP in Logilab ELN.</p>Please use below mentioned OTP for Verifying your Tenant Registration.<br><br>"
							+ "Once the verification is completed, you will receive an email with the ELN Administrator login details. <br><br>"
							+ "<b style='margin-left: 76px;'>OTP: </b>\t\t " + otp + "<br><br>"
							+ "<p>If you have any queries, please contact our support by mail to info@agaramtech.com <br><br><br>"
							+ "Regards,</p>" + "<b>Agaram Technologies Private Limited</b><br><br>"
							+ "<img src=\"cid:seconimage\"  style ='width:120px; height:120px;border: 3px;'"
							+ "<br><br><p>T: +91 44 4208 2005</p><p>T: +91 44 42189406</p>"
							+ "W:<a href='https://www.agaramtech.com'>https://www.agaramtech.com</a></p>");
			emailService.sendEmail(email);
		}

		return Tenantname;

	}

	public DataSourceConfig otpvarification(DataSourceConfig Tenantname) throws MessagingException {
		boolean valid = false;
//		DataSourceConfig config=new DataSourceConfig();
		DataSourceConfig code = configRepo.findByTenantid(Tenantname.getTenantid());

		if ((Tenantname.getVarificationOTP().equals(code.getVarificationOTP()))) {
			valid = true;
			configRepo.setverifiedemailandtenantpassword(valid, Tenantname.getTenantid());
			code.setVerifiedemail(valid);
		} else {
			configRepo.setverifiedemailandtenantpassword(valid, Tenantname.getTenantid());
			code.setVerifiedemail(valid);
		}
//		code =configRepo.findByTenantid(Tenantname.getTenantid());
		return code;

	}

	public Map<String, Object> checkusermail(DataSourceConfig DataSourceConfig) throws MessagingException {
		DataSourceConfig objDataSourceConfig = new DataSourceConfig();
		String useremail = DataSourceConfig.getUseremail();
		Map<String, Object> mapOrder = new HashMap<String, Object>();
		objDataSourceConfig = configRepo.findByuseremail(useremail);
		mapOrder.put("usermail", objDataSourceConfig);

		// mapOrder.put("isvalidmail", SMTPMailvalidation.isAddressValid(useremail));

		return mapOrder;
	}

	public Map<String, Object> tenantcontactno(DataSourceConfig DataSourceConfig) throws MessagingException {
		DataSourceConfig objDataSourceConfig = new DataSourceConfig();
		String tenantcontactno = DataSourceConfig.getTenantcontactno();
		Map<String, Object> mapOrder = new HashMap<String, Object>();
		objDataSourceConfig = configRepo.findBytenantcontactno(tenantcontactno);
		mapOrder.put("tenantcontactno", objDataSourceConfig);

		return mapOrder;
	}

	public DataSourceConfig Completeregistration(DataSourceConfig Tenant) {
		DataSourceConfig updatetenant = configRepo.findByTenantid(Tenant.getTenantid());

		if (updatetenant != null) {
			updatetenant.setPackagetype(Tenant.getPackagetype());
			updatetenant.setValidatenodays(Tenant.getValidatenodays());
			updatetenant.setNoofusers(Tenant.getNoofusers());
			configRepo.save(updatetenant);
		}

		return updatetenant;
	}

	public DataSourceConfig updatetenantadminpassword(DataSourceConfig Tenant) throws MessagingException, IOException {
		LSuserMaster lsuserMaster = lsuserMasterRepository.findOne(1);
		if (lsuserMaster != null) {
			lsuserMaster.setEmailid(Tenant.getUseremail());
			String password = Generatetenantpassword();
			String passwordadmin = AESEncryption.encrypt(password);
			lsuserMaster.setPassword(passwordadmin);
			lsuserMaster.setPasswordstatus(1);

//			System.out.print(Tenant.getLoginpath());

//			Email email = new Email();
			if (!Tenant.getAdministratormailid().equals("")) {
				String mails[] = { Tenant.getUseremail(), Tenant.getAdministratormailid() };

				for (int i = 0; i < mails.length; i++) {
					Email email = new Email();
					email.setMailto(mails[i]);
					email.setSubject("ELN Admin Credentials");
//					email.setMailcontent("<b>Dear Customer</b>,<br>"
//							+ "<i>You have successfully registered to Logilab ELN.</i><br>"
//							+ "<i>Your organisation ID is <b>" + Tenant.getTenantid() + "</b>.</i><br>"
//							+ "<i>we create a default administrator user for you and this are the login credentials.</i><br>"
//							+ "<b>UserName:\t\t Administrator </b><br><b>Password:\t\t" + password + "</b>"
//							+ "</b><br><b><a href=" + Tenant.getLoginpath() + ">click here to login</a></b>");
//					emailService.sendEmail(email);

					email.setMailcontent(
							"<b>Dear Customer,</b><br><center><img src=\"cid:image\"  style =width:120px; height:100px border: 3px;'></center><br><br>"
									+ "<p><p>Thanks for your interest in Logilab ELN.</p>Please use below mentioned Username and Password for Administrator Login in ELN Application.<br><br>"
									+ "Click the URL mentioned below to go to Logilab ELN Login page. <br><br>"
									+ "After entered the username and click the password field, new password generation screen will appear.<br><br>"
									+ "Paste the password in the Old Password Textbox, and then generate your new password.<br><br>"
									+ "<b style='margin-left: 76px;'>Organisation ID:</b>\t\t " + Tenant.getTenantid()
									+ "<br><br>" + "<b style='margin-left: 76px;'>" + "Username    :"
									+ "</b>\t\t Administrator<br><br>"
									+ "<b style='margin-left: 76px;'>Password       :</b>\t\t" + password + "<br><br>"
									+ "<b style='margin-left: 76px;'><a href=" + Tenant.getLoginpath()
									+ ">Click here to Logilab ELN Login page</a></b><br><br>"
									+ "<p>If you have any queries, please contact our support by mail to info@agaramtech.com <br><br><br>"
									+ "Regards,</p>" + "<b>Agaram Technologies Private Limited</b><br><br>"
									+ "<img src=\"cid:seconimage\"  style ='width:120px; height:120px;border: 3px;'"
									+ "<br><br><p>T: +91 44 4208 2005</p><p>T: +91 44 42189406</p>"
									+ "W:<a href='https://www.agaramtech.com'>https://www.agaramtech.com</a></p>");

					emailService.sendEmail(email);
				}
			} else {
				Email email = new Email();
				email.setMailto(Tenant.getUseremail());
				email.setSubject("ELN Admin Credentials");
//				email.setMailcontent("<b>Dear Customer</b>,<br>"
//						+ "<i>You have successfully registered to Logilab ELN.</i><br>"
//						+ "<i>Your organisation ID is <b>" + Tenant.getTenantid() + "</b>.</i><br>"
//						+ "<i>we create a default administrator user for you and this are the login credentials.</i><br>"
//						+ "<b>UserName:\t\t Administrator </b><br><b>Password:\t\t" + password + "</b>"
//						+ "</b><br><b><a href=" + Tenant.getLoginpath() + ">click here to login</a></b>");
//				emailService.sendEmail(email);
				email.setMailcontent(
						"<b>Dear Customer,</b><br><center><img src=\"cid:image\"  style =width:120px; height:100px border: 3px;'></center><br><br>"
								+ "<p><p>Thanks for your interest in Logilab ELN.</p>Please use below mentioned Username and Password for Administrator Login in ELN Application.<br><br>"
								+ "Click the URL mentioned below to go to Logilab ELN Login page. <br><br>"
								+ "After entered the username and click the password field, new password generation screen will appear.<br><br>"
								+ "Paste the password in the Old Password Textbox, and then generate your new password.<br><br>"
								+ "<b style='margin-left: 76px;'>Organisation ID:</b>\t\t " + Tenant.getTenantid()
								+ "<br><br>" + "<b style='margin-left: 76px;'>" + "Username    :"
								+ "</b>\t\t Administrator<br><br>"
								+ "<b style='margin-left: 76px;'>Password       :</b>\t\t" + password + "<br><br>"
								+ "<b style='margin-left: 76px;'><a href=" + Tenant.getLoginpath()
								+ ">Click here to Logilab ELN Login page</a></b><br><br>"
								+ "<p>If you have any queries, please contact our support by mail to info@agaramtech.com <br><br><br>"
								+ "Regards,</p>" + "<b>Agaram Technologies Private Limited</b><br><br>"
								+ "<img src=\"cid:seconimage\"  style ='width:120px; height:120px;border: 3px;'"
								+ "<br><br><p>T: +91 44 4208 2005</p><p>T: +91 44 42189406</p>"
								+ "W:<a href='https://www.agaramtech.com'>https://www.agaramtech.com</a></p>");

				emailService.sendEmail(email);
			}
			lsuserMasterRepository.save(lsuserMaster);
		}
		return Tenant;
	}

	public DataSourceConfig ValidatetenantByID(DataSourceConfig tenantID) {

		DataSourceConfig objdatasource = configRepo.findById(tenantID.getId());

		Response objreponse = new Response();
		if (objdatasource != null) {
			if (objdatasource.isInitialize() && objdatasource.isIsenable()) {
				objreponse.setStatus(true);
				objdatasource.setObjResponse(objreponse);
			} else if (!objdatasource.isInitialize()) {
				objreponse.setInformation("ID_ORGREGINPROGRESS");
				objreponse.setStatus(false);
				objdatasource.setObjResponse(objreponse);
			} else if (!objdatasource.isIsenable()) {
				objreponse.setInformation("ID_ORGDISABLED");
				objreponse.setStatus(false);
				objdatasource.setObjResponse(objreponse);
			}

		} else {
			objreponse.setInformation("ID_ORGNOTEXIST");
			objreponse.setStatus(false);
			objdatasource = new DataSourceConfig();
			objdatasource.setObjResponse(objreponse);
		}

		return objdatasource;
	}

	public DataSourceConfig ValidatetenantByName(DataSourceConfig tenantID) {

		DataSourceConfig objdatasource = configRepo.findByName(tenantID.getName());

		Response objreponse = new Response();
		if (objdatasource != null) {
			if (objdatasource.isInitialize() && objdatasource.isIsenable()) {
				objreponse.setStatus(true);
				objdatasource.setObjResponse(objreponse);
			} else if (!objdatasource.isInitialize()) {
				objreponse.setInformation("ID_ORGREGINPROGRESS");
				objreponse.setStatus(false);
				objdatasource.setObjResponse(objreponse);
			} else if (!objdatasource.isIsenable()) {
				objreponse.setInformation("ID_ORGDISABLED");
				objreponse.setStatus(false);
				objdatasource.setObjResponse(objreponse);
			}
		} else {
			objreponse.setInformation("ID_ORGNOTEXIST");
			objreponse.setStatus(false);
			objdatasource = new DataSourceConfig();
			objdatasource.setObjResponse(objreponse);
		}
		return objdatasource;
	}

	public DataSourceConfig Remindertenant(DataSourceConfig Tenantname) throws MessagingException, IOException {

		if (!Tenantname.getAdministratormailid().equals("")) {

			String mails[] = { Tenantname.getUseremail(), Tenantname.getAdministratormailid() };
			for (int i = 0; i < mails.length; i++) {
				Email email = new Email();
				email.setMailto(mails[i]);
				email.setSubject("UserName and PassWord");
				email.setMailcontent(
						"<b>Dear Customer</b>,<br><i>You need to validate your OTP before organization initiated </i><br><b>Your OTP validation path <br><b><a href="
								+ Tenantname.getLoginpath() + ">click here to validate OTP</a></b>");

				email.setMailcontent(
						"<b>Dear Customer,</b><br><center><img src=\"cid:image\"  style =width:120px; height:100px border: 3px;'></center><br><br>"
								+ "<p><p>Thanks for your interest in Logilab ELN.</p>You need to validate your OTP before organization initiated <br><br>"
								+ "<b style='margin-left: 76px;'>Your OTP validation path:</b>\t\t <a href="
								+ Tenantname.getLoginpath() + ">click here to validate OTP</a><br><br>"
								+ "<p>If you have any queries, please contact our support by mail to info@agaramtech.com <br><br><br>"
								+ "Regards,</p>" + "<b>Agaram Technologies Private Limited</b><br><br>"
								+ "<img src=\"cid:seconimage\"  style ='width:120px; height:120px;border: 3px;'"
								+ "<br><br><p>T: +91 44 4208 2005</p><p>T: +91 44 42189406</p>"
								+ "W:<a href='https://www.agaramtech.com'>https://www.agaramtech.com</a></p>");

				emailService.sendEmail(email);
			}
		} else {
			Email email = new Email();
			email.setMailto(Tenantname.getUseremail());
			email.setSubject("UserName and PassWord");

			email.setMailcontent(
					"<b>Dear Customer,</b><br><center><img src=\"cid:image\"  style =width:120px; height:100px border: 3px;'></center><br><br>"
							+ "<p><p>Thanks for your interest in Logilab ELN.</p>You need to validate your OTP before organization initiated <br><br>"
							+ "<b style='margin-left: 76px;'>Your OTP validation path:</b>\t\t <a href="
							+ Tenantname.getLoginpath() + ">click here to validate OTP</a><br><br>"
							+ "<p>If you have any queries, please contact our support by mail to info@agaramtech.com <br><br><br>"
							+ "Regards,</p>" + "<b>Agaram Technologies Private Limited</b><br><br>"
							+ "<img src=\"cid:seconimage\"  style ='width:120px; height:120px;border: 3px;'"
							+ "<br><br><p>T: +91 44 4208 2005</p><p>T: +91 44 42189406</p>"
							+ "W:<a href='https://www.agaramtech.com'>https://www.agaramtech.com</a></p>");

			emailService.sendEmail(email);
		}
		return Tenantname;
	}

	public DataSourceConfig updateTenantPlan(DataSourceConfig tenantDetails) {

		if (tenantDetails.getPlantype() != null) {

			Integer plantType = tenantDetails.getPlantype();

			if (plantType == 3) {
				tenantDetails.setPackagetype(3914465000000065053L);
			} else if (plantType == 2) {
				tenantDetails.setPackagetype(3914465000000065049L);
			} else {
				tenantDetails.setPackagetype(3914465000000065045L);
			}

			configRepo.save(tenantDetails);

		}

		return tenantDetails;
	}

	public DataSourceConfig afterotpverified(DataSourceConfig objtenant) {

		List<LSuserMaster> lstUsr = lsuserMasterRepository
				.findByusernameNotAndUserretirestatusNotOrderByCreateddateDesc("Administrator", 1);

		if (lstUsr != null && lstUsr.size() > 0) {
			for(LSuserMaster user : lstUsr ) {
			
				Email email = new Email();
				try {
					email.setMailto(user.getEmailid());
					email.setSubject("UserName and PassWord");

					email.setMailcontent(
							"<b>Dear ELN System Admin,</b><br><center><img src=\"cid:image\"  style =width:120px; height:100px border: 3px;'></center><br><br>"
									+ "<p><p>A customer has registered and completed the verification in ELN System with the following Tenant </p>details:<br><br>"
									+ "<b style='margin-left: 76px;'>User Name: </b>\t\t " + objtenant.getTenantid()
									+ "<br><br>" + "<b style='margin-left: 76px;'>Email id: </b>\t\t "
									+ objtenant.getUseremail() + "<br><br>"
									+ "<p>Please initiate the activation of Admin user for the above mentioned Tenant in ELN system so that<br><br><br>"
									+ "<p>the customer can receive the email with ELN Admin Credentials.<br><br><br>"
									+ "Regards,</p>" + "<b>Agaram Technologies Private Limited</b><br><br>"
									+ "<img src=\"cid:seconimage\"  style ='width:120px; height:120px;border: 3px;'"
									+ "<br><br><p>T: +91 44 4208 2005</p><p>T: +91 44 42189406</p>"
									+ "W:<a href='https://www.agaramtech.com'>https://www.agaramtech.com</a></p>");

					emailService.sendEmail(email);

				} catch (MessagingException e) {

					e.printStackTrace();
				}
				
			}
		}

		Email email = new Email();
		try {
			email.setMailto(env.getProperty("spring.mail.username"));
			email.setSubject("UserName and PassWord");

			email.setMailcontent(
					"<b>Dear ELN System Admin,</b><br><center><img src=\"cid:image\"  style =width:120px; height:100px border: 3px;'></center><br><br>"
							+ "<p><p>A customer has registered and completed the verification in ELN System with the following Tenant </p>details:<br><br>"
							+ "<b style='margin-left: 76px;'>User Name: </b>\t\t " + objtenant.getTenantid()
							+ "<br><br>" + "<b style='margin-left: 76px;'>Email id: </b>\t\t "
							+ objtenant.getUseremail() + "<br><br>"
							+ "<p>Please initiate the activation of Admin user for the above mentioned Tenant in ELN system so that<br><br><br>"
							+ "<p>the customer can receive the email with ELN Admin Credentials.<br><br><br>"
							+ "Regards,</p>" + "<b>Agaram Technologies Private Limited</b><br><br>"
							+ "<img src=\"cid:seconimage\"  style ='width:120px; height:120px;border: 3px;'"
							+ "<br><br><p>T: +91 44 4208 2005</p><p>T: +91 44 42189406</p>"
							+ "W:<a href='https://www.agaramtech.com'>https://www.agaramtech.com</a></p>");

			emailService.sendEmail(email);

		} catch (MessagingException e) {

			e.printStackTrace();
		}

		return objtenant;

	}

}