package com.agaram.eln.primary.model.usermanagement;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "LSusergrouprightsmaster")
public class LSusergrouprightsmaster {
	@Id
	private Integer orderno;
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
	@Column(columnDefinition = "char(10)")
	private String status;

	@Transient
	private String createdby;
	@Transient
	private Date createdon;
	@Transient
	private String modifiedby;
	@Transient
	private Date modifiedon;
	@Transient
	private LSusergroup usergroupid;
	@Transient
	private LSSiteMaster lssitemaster;
	
	private Integer sequenceorder;
	
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
	public Integer getOrderno() {
		return orderno;
	}
	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public Integer getSequenceorder() {
		return sequenceorder;
	}
	public void setSequenceorder(Integer sequenceorder) {
		this.sequenceorder = sequenceorder;
	}
	
}
