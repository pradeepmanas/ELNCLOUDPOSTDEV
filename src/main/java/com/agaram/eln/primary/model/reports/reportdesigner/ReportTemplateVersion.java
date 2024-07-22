package com.agaram.eln.primary.model.reports.reportdesigner;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class ReportTemplateVersion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	private Integer templateversioncode;
	
	private Long templatecode;

	private String templatename;
	
	@Column(columnDefinition = "varchar(255)")
	private String versionname;
	
	private Integer versionno;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifieddate;
	
	public Integer createdby;
	
	public String createdbyname;

	private Integer sitecode;
	
	private Integer templatetype;
	
	private String fileuid;

	private String fileuri;
	
	public String getCreatedbyname() {
		return createdbyname;
	}

	public void setCreatedbyname(String createdbyname) {
		this.createdbyname = createdbyname;
	}
	
	@Transient
	private boolean isnewversion;
	
	@Transient
	private Integer ismultitenant;
	
	public Integer getIsmultitenant() {
		return ismultitenant;
	}

	public void setIsmultitenant(Integer ismultitenant) {
		this.ismultitenant = ismultitenant;
	}

	@Transient
	private String templateversioncontent;

	public String getTemplateversioncontent() {
		return templateversioncontent;
	}

	public void setTemplateversioncontent(String templateversioncontent) {
		this.templateversioncontent = templateversioncontent;
	}

	public boolean isIsnewversion() {
		return isnewversion;
	}

	public void setIsnewversion(boolean isnewversion) {
		this.isnewversion = isnewversion;
	}

	public Integer getTemplateversioncode() {
		return templateversioncode;
	}

	public void setTemplateversioncode(Integer templateversioncode) {
		this.templateversioncode = templateversioncode;
	}

	public Long getTemplatecode() {
		return templatecode;
	}

	public void setTemplatecode(Long templatecode) {
		this.templatecode = templatecode;
	}

	public String getTemplatename() {
		return templatename;
	}

	public void setTemplatename(String templatename) {
		this.templatename = templatename;
	}

	public String getVersionname() {
		return versionname;
	}

	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}

	public Integer getVersionno() {
		return versionno;
	}

	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getModifieddate() {
		return modifieddate;
	}

	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}

	public Integer getCreatedby() {
		return createdby;
	}

	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}

	public Integer getSitecode() {
		return sitecode;
	}

	public void setSitecode(Integer sitecode) {
		this.sitecode = sitecode;
	}

	public Integer getTemplatetype() {
		return templatetype;
	}

	public void setTemplatetype(Integer templatetype) {
		this.templatetype = templatetype;
	}

	public String getFileuid() {
		return fileuid;
	}

	public void setFileuid(String fileuid) {
		this.fileuid = fileuid;
	}

	public String getFileuri() {
		return fileuri;
	}

	public void setFileuri(String fileuri) {
		this.fileuri = fileuri;
	}
	

}
