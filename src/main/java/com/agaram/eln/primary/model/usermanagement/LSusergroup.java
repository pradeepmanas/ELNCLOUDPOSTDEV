package com.agaram.eln.primary.model.usermanagement;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;

@Entity
@Table(name = "LSusergroup")
public class LSusergroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "usergroupcode")
	private Integer usergroupcode;
	@Column(columnDefinition = "varchar(100)")
	private String usergroupname;
	@Column(columnDefinition = "char(50)")
	private String usergroupstatus;
	@Column(columnDefinition = "char(50)")
	private String createdby;
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdon;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedon;
//	private String createdon;
	@Column(columnDefinition = "char(50)")
	private String modifiedby;
	
//	@ManyToOne
//	private LSuserMaster createby;
//	@ManyToOne
//	private LSuserMaster modifieduser;
	
	@Transient
	private String sitename;
	
	public void setCreatedon(Date createdon) {
		this.createdon = createdon;
	}
	public Date getCreatedon() {
		return createdon;
	}
	public Date getModifiedon() {
		return modifiedon;
	}
	public void setModifiedon(Date modifiedon) {
		this.modifiedon = modifiedon;
	}
	
//	public LSuserMaster getCreateby() {
//		return createby;
//	}
//	public void setCreateby(LSuserMaster createby) {
//		this.createby = createby;
//	}

//	@ManyToOne
//	@OneToOne
//	private LSSiteMaster lssitemaster;
	@Column(name="lssitemaster_sitecode")
	private Integer lssitemaster;
	
	public Integer getLssitemaster() {
		return lssitemaster;
	}
	public void setLssitemaster(Integer lssitemaster) {
		this.lssitemaster = lssitemaster;
	}

	@Transient
	LScfttransaction objsilentaudit;
	
	@Transient
	LScfttransaction Objmanualaudit;
	
	public LScfttransaction getObjmanualaudit() {
		return Objmanualaudit;
	}
	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		Objmanualaudit = objmanualaudit;
	}

	@Transient
	LoggedUser objuser;
	
	@Transient
	LSuserMaster LSuserMaster;
	
	private Integer modifiedUser;
	private String modifiedUsername;
	
	@Transient
	private Response response;
	
	@Transient
	private String encryptedpassword;
	
	public String getEncryptedpassword() {
		return encryptedpassword;
	}

	public void setEncryptedpassword(String encryptedpassword) {
		this.encryptedpassword = encryptedpassword;
	}
	
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}
	
	public Integer getUsergroupcode() {
		return usergroupcode;
	}
	public void setUsergroupcode(Integer usergroupcode) {
		this.usergroupcode = usergroupcode;
	}
	
	public String getUsergroupname() {
		return usergroupname;
	}
	public void setUsergroupname(String usergroupname) {
		this.usergroupname = usergroupname;
	}
	public String getUsergroupstatus() {
		return usergroupstatus.trim().equals("A") || usergroupstatus.trim().equals("Active")?"Active":"Deactive";
	}
	public void setUsergroupstatus(String usergroupstatus) {
		this.usergroupstatus = usergroupstatus;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public String getModifiedby() {
		return modifiedby;
	}
	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}

	public LoggedUser getObjuser() {
		return objuser;
	}

	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
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
//	public LSuserMaster getModifieduser() {
//		return modifieduser;
//	}
//	public void setModifieduser(LSuserMaster modifieduser) {
//		this.modifieduser = modifieduser;
//	}
	public String getSitename() {
//		if(this.l != null)
//		{
//			return this.createby.getSitename();
//		}
//		else
//		{
			return "";
//		}
	}
	public void setSitename(String sitename) {
		this.sitename = sitename;
	}
	public Integer getModifiedUser() {
		return modifiedUser;
	}
	public void setModifiedUser(Integer modifiedUser) {
		this.modifiedUser = modifiedUser;
	}
	public String getModifiedUsername() {
		return modifiedUsername;
	}
	public void setModifiedUsername(String modifiedUsername) {
		this.modifiedUsername = modifiedUsername;
	}
	
}
