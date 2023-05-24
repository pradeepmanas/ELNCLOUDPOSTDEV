package com.agaram.eln.primary.model.usermanagement;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;

@Entity
@Table(name = "LSSitemaster")
public class LSSiteMaster {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "sitecode")
	private Integer sitecode;
	
	
	@Column(name = "sitename", columnDefinition = "varchar(255)")
	private String sitename;
	//, columnDefinition = "nvarchar(500)"
	@Column(name = "siteaddress", columnDefinition = "text")
	private String siteaddress;
	//, columnDefinition = "nvarchar(255)"
	@Column(name = "contactperson", columnDefinition = "varchar(255)")
	private String contactperson;
	//, columnDefinition = "nvarchar(255)"
	@Column(name = "phoneno", columnDefinition = "varchar(255)")
	private String phoneno;
	//, columnDefinition = "nvarchar(255)"
	@Column(name = "faxno", columnDefinition = "varchar(255)")
	private String faxno;
	//, columnDefinition = "nvarchar(255)"
	@Column(name = "email", columnDefinition = "varchar(255)")
	private String email;
	
	@Column(name = "istatus")
	private Integer istatus;

	@Transient
	LScfttransaction objsilentaudit;
	
	@Transient
	LoggedUser objuser;
	
	@Transient
	LScfttransaction objmanualaudit;

	@Transient
	LSuserMaster LSuserMaster;
	
	@Transient
	LSuserMaster username;
	
	@Transient
	private Response response;

	@Transient
	private Integer ismultitenant;
	
	public LSSiteMaster()
	{
		
	}
	
	public LSSiteMaster(Integer sitecode)
	{
		this.sitecode = sitecode;
	}
	
	public LSuserMaster getLSuserMaster() {
		return LSuserMaster;
	}

	public void setLSuserMaster(LSuserMaster lSuserMaster) {
		LSuserMaster = lSuserMaster;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}


	public LoggedUser getObjuser() {
		return objuser;
	}

	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
	}
	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}

	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	@Transient
	private String sitestatus;

	public String getSitestatus() {
		if(istatus != null)
		{
			return istatus == 1 ? "Active" : "Deactive";
		}
		else
		{
			return "";
		}
	}

	public void setSitestatus(String sitestatus) {
		this.sitestatus = sitestatus;
	}
	
	public LSuserMaster getUsername() {
		return username;
	}

	public void setUsername(LSuserMaster username) {
		this.username = username;
	}

	public Integer getSitecode() {
		return sitecode;
	}

	public void setSitecode(Integer sitecode) {
		this.sitecode = sitecode;
	}

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public String getSiteaddress() {
		return siteaddress;
	}

	public void setSiteaddress(String siteaddress) {
		this.siteaddress = siteaddress;
	}

	public String getContactperson() {
		return contactperson;
	}

	public void setContactperson(String contactperson) {
		this.contactperson = contactperson;
	}

	public String getPhoneno() {
		return phoneno;
	}

	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}

	public String getFaxno() {
		return faxno;
	}

	public void setFaxno(String faxno) {
		this.faxno = faxno;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getStatus() {
		return istatus;
	}

	public void setStatus(Integer istatus) {
		this.istatus = istatus;
	}

	public Integer getIsmultitenant() {
		return ismultitenant;
	}

	public void setIsmultitenant(Integer ismultitenant) {
		this.ismultitenant = ismultitenant;
	}

	
	
	
}
