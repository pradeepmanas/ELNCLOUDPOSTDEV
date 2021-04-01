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
@Table(name = "LSusergrouprights")
public class LSusergrouprights {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "orderno")
	private Integer orderno;
	
	@Column(columnDefinition = "varchar(100)")
	private String modulename;
	@Column(columnDefinition = "varchar(100)")
	private String displaytopic;
	@Column(columnDefinition = "char(10)")
	private String screate;
	@Column(columnDefinition = "char(10)")
	private String sedit;
	@Column(columnDefinition = "char(10)")
	private String sdelete;
	@Column(columnDefinition = "char(10)")
	private String sallow;
	@Column(columnDefinition = "varchar(250)")
	private String createdby;
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdon;
	@Column(columnDefinition = "varchar(250)")
	private String modifiedby;
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedon;
	
	@ManyToOne
	private LSusergroup usergroupid;
	
	@ManyToOne
	private LSSiteMaster lssitemaster;
	
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
	private LSuserMaster lsuserMaster;
	
	@Transient
	private Response response;
	
	public LSuserMaster getLsuserMaster() {
		return lsuserMaster;
	}

	public void setLsuserMaster(LSuserMaster lsuserMaster) {
		this.lsuserMaster = lsuserMaster;
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
	@Transient
	LoggedUser objuser;
	
	
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}
	
	public Integer getOrderno() {
		return orderno;
	}
	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}
	
	public String getModulename() {
		return modulename;
	}
	public void setModulename(String modulename) {
		this.modulename = modulename;
	}
	public String getDisplaytopic() {
		return displaytopic;
	}
	public void setDisplaytopic(String displaytopic) {
		this.displaytopic = displaytopic;
	}
	public String getScreate() {
		return screate;
	}
	public void setScreate(String screate) {
		this.screate = screate;
	}
	public String getSedit() {
		return sedit;
	}
	public void setSedit(String sedit) {
		this.sedit = sedit;
	}
	public String getSdelete() {
		return sdelete;
	}
	public void setSdelete(String sdelete) {
		this.sdelete = sdelete;
	}
	public String getSallow() {
		return sallow;
	}
	public void setSallow(String sallow) {
		this.sallow = sallow;
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
	
	public LSusergroup getUsergroupid() {
		return usergroupid;
	}
	public void setUsergroupid(LSusergroup usergroupid) {
		this.usergroupid = usergroupid;
	}
	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}
	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}
	public Date getCreatedon() {
		return createdon;
	}
	public void setCreatedon(Date createdon) {
		this.createdon = createdon;
	}
	public Date getModifiedon() {
		return modifiedon;
	}
	public void setModifiedon(Date modifiedon) {
		this.modifiedon = modifiedon;
	}
	
}
