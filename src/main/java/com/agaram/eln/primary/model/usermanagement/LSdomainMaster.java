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
@Table(name = "LSdomainmaster")
public class LSdomainMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "domaincode") 
	private Integer domaincode;

	//, columnDefinition = "varchar(255)"
	@Column(name = "domainname", columnDefinition = "varchar(255)")
	private String domainname;
	//
	@Column(name = "categories", columnDefinition = "varchar(255)")
	private String categories;
	@Column(name = "domainstatus")
	private Integer domainstatus;
	//
	@Column(name = "createdby", columnDefinition = "varchar(255)")
	private String createdby;
	@Column(name = "createdon")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdon;
	@Column(name = "modifiedby", columnDefinition = "varchar(255)")
	private String modifiedby;
	@Column(name = "modifiedon")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedon;
	
//	@ManyToOne(cascade=CascadeType.ALL) 
//	//@JoinColumn(name="sitecode") // Foreign Key 
//	private LSSiteMaster lssitemaster; 
	
	@ManyToOne 
	private LSSiteMaster lssitemaster;
	
	@Transient
	LoggedUser objuser;
	
	@Transient
	LSuserMaster LSuserMaster;
	
	@Transient
	private Response response;
	
	@Transient
	LScfttransaction objsilentaudit;
	
	@Transient
	LScfttransaction Objmanualaudit;
	
//	@Id
//	@ManyToOne(cascade=CascadeType.ALL) 
//	@JoinColumn(name="siteid") // Foreign Key
//	private LSSiteMaster objsiteid; 
	
	
	public LScfttransaction getObjmanualaudit() {
		return Objmanualaudit;
	}
	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		Objmanualaudit = objmanualaudit;
	}
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}
	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
	public LSuserMaster getLSuserMaster() {
		return LSuserMaster;
	}
	public void setLSuserMaster(LSuserMaster lSuserMaster) {
		LSuserMaster = lSuserMaster;
	}
	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}
	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}
	public LoggedUser getObjuser() {
		return objuser;
	}
	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
	}
	public String getDomainname() {
		return domainname;
	}
	public void setDomainname(String domainname) {
		this.domainname = domainname;
	}
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public Integer getDomainstatus() {
		return domainstatus;
	}
	public void setDomainstatus(Integer domainstatus) {
		this.domainstatus = domainstatus;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public Date getCreatedon() {
		return createdon;
	}
	public void setCreatedon(Date createdon) {
		this.createdon = createdon;
	}
	public String getModifiedby() {
		return modifiedby;
	}
	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}
	public Date getModifiedon() {
		return modifiedon;
	}
	public void setModifiedon(Date modifiedon) {
		this.modifiedon = modifiedon;
	}
	public Integer getDomaincode() {
		return domaincode;
	}
	public void setDomaincode(Integer domaincode) {
		this.domaincode = domaincode;
	}
	
	
}
