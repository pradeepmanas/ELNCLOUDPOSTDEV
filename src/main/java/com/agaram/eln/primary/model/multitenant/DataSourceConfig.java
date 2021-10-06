package com.agaram.eln.primary.model.multitenant;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.general.Response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "DataSourceConfig")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataSourceConfig implements Serializable {
	private static final long serialVersionUID = 5104181924076372196L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String url;
	private String username;
	private String password;
	private String useremail;
	private String tenantpassword;
	private boolean verifiedemail;
	private String driverClassName;
	private boolean initialize;
	private String tenantid;
	private String tenantname;
	private String tenantcontactno;
	private String tenantaddress;
	private String tenantpincode;
	private String tenantcity;
	private String tenantstate;
	private String tenantcountry;
	private String archivename;
	private String archiveurl;
	private boolean isenable;
	private boolean isadministrator_verify;
	private String administratormailid;
	private int loginfrom = 0;
	private String varificationOTP;

	private long packagetype = 0;
	private Date registereddate;
	private Date activateddate;
	private int validatenodays = 0;
	private int noofusers = 0;
	@Column(columnDefinition = "numeric(30,0)", name = "customer_crm_id")
	private Long customer_crm_id;
	@Column(columnDefinition = "numeric(30,0)", name = "customer_creator_id")
	private Long customer_creator_id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "customer_subscription_id", referencedColumnName = "customer_subscription_id")
	private CustomerSubscription CustomerSubscription;

	@OneToOne
	@JoinColumn(name = "id")
	private CustomerSubscription customersubscriptionid;

	@Transient
	private Invoice invoice;

	@Transient
	private Integer plantype = 1;

	public Integer getPlantype() {
		return plantype;
	}

	public void setPlantype(Integer plantype) {
		this.plantype = plantype;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public CustomerSubscription getCustomersubscriptionid() {
		return customersubscriptionid;
	}

	public void setCustomersubscriptionid(CustomerSubscription customersubscriptionid) {
		this.customersubscriptionid = customersubscriptionid;
	}

	public Long getCustomer_crm_id() {
		return customer_crm_id;
	}

	public void setCustomer_crm_id(Long customer_crm_id) {
		this.customer_crm_id = customer_crm_id;
	}

	public Long getCustomer_creator_id() {
		return customer_creator_id;
	}

	public void setCustomer_creator_id(Long customer_creator_id) {
		this.customer_creator_id = customer_creator_id;
	}

	public String getVarificationOTP() {
		return varificationOTP;
	}

	public CustomerSubscription getCustomerSubscription() {
		return CustomerSubscription;
	}

	public void setCustomerSubscription(CustomerSubscription customerSubscription) {
		CustomerSubscription = customerSubscription;
	}

	public void setVarificationOTP(String varificationOTP) {
		this.varificationOTP = varificationOTP;
	}

	public boolean isIsadministrator_verify() {
		return isadministrator_verify;
	}

	public void setIsadministrator_verify(boolean isadministrator_verify) {
		this.isadministrator_verify = isadministrator_verify;
	}

	public String getAdministratormailid() {
		return administratormailid;
	}

	public void setAdministratormailid(String administratormailid) {
		this.administratormailid = administratormailid;
	}

	@Transient
	Response objResponse;

	@Transient
	private String loginpath;

	public String getLoginpath() {
		return loginpath;
	}

	public void setLoginpath(String loginpath) {
		this.loginpath = loginpath;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public boolean isVerifiedemail() {
		return verifiedemail;
	}

	public void setVerifiedemail(boolean verifiedemail) {
		this.verifiedemail = verifiedemail;
	}

	public String getTenantpassword() {
		return tenantpassword;
	}

	public void setTenantpassword(String tenantpassword) {
		this.tenantpassword = tenantpassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public boolean isInitialize() {
		return initialize;
	}

	public void setInitialize(boolean initialize) {
		this.initialize = initialize;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Response getObjResponse() {
		return objResponse;
	}

	public void setObjResponse(Response objResponse) {
		this.objResponse = objResponse;
	}

	public String getTenantid() {
		return tenantid;
	}

	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}

	public String getTenantname() {
		return tenantname;
	}

	public void setTenantname(String tenantname) {
		this.tenantname = tenantname;
	}

	public String getTenantcontactno() {
		return tenantcontactno;
	}

	public void setTenantcontactno(String tenantcontactno) {
		this.tenantcontactno = tenantcontactno;
	}

	public String getTenantaddress() {
		return tenantaddress;
	}

	public void setTenantaddress(String tenantaddress) {
		this.tenantaddress = tenantaddress;
	}

	public String getTenantpincode() {
		return tenantpincode;
	}

	public void setTenantpincode(String tenantpincode) {
		this.tenantpincode = tenantpincode;
	}

	public String getTenantcity() {
		return tenantcity;
	}

	public void setTenantcity(String tenantcity) {
		this.tenantcity = tenantcity;
	}

	public String getTenantstate() {
		return tenantstate;
	}

	public void setTenantstate(String tenantstate) {
		this.tenantstate = tenantstate;
	}

	public String getTenantcountry() {
		return tenantcountry;
	}

	public void setTenantcountry(String tenantcountry) {
		this.tenantcountry = tenantcountry;
	}

	public boolean isIsenable() {
		return isenable;
	}

	public void setIsenable(boolean isenable) {
		this.isenable = isenable;
	}

	public String getArchivename() {
		return archivename;
	}

	public void setArchivename(String archivename) {
		this.archivename = archivename;
	}

	public String getArchiveurl() {
		return archiveurl;
	}

	public void setArchiveurl(String archiveurl) {
		this.archiveurl = archiveurl;
	}

	public int getLoginfrom() {
		return loginfrom;
	}

	public void setLoginfrom(int loginfrom) {
		this.loginfrom = loginfrom;
	}

	public long getPackagetype() {

		int plantype = 0;

		if (this.packagetype == 3914465000000065049L) {
			plantype = 2;
		} else if (this.packagetype == 3914465000000065053L) {
			plantype = 3;
		} else if (this.packagetype == 3914465000000065045L) {
			plantype = 1;
		}

		setPlantype(plantype);

		return packagetype;

	}

	public void setPackagetype(long packagetype) {
		this.packagetype = packagetype;
	}

	public Date getRegistereddate() {
		return registereddate;
	}

	public void setRegistereddate(Date registereddate) {
		this.registereddate = registereddate;
	}

	public Date getActivateddate() {
		return activateddate;
	}

	public void setActivateddate(Date activateddate) {
		this.activateddate = activateddate;
	}

	public int getValidatenodays() {
		return validatenodays;
	}

	public void setValidatenodays(int validatenodays) {
		this.validatenodays = validatenodays;
	}

	public int getNoofusers() {
		return noofusers;
	}

	public void setNoofusers(int noofusers) {
		this.noofusers = noofusers;
	}

}
