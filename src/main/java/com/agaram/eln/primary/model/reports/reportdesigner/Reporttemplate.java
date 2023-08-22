package com.agaram.eln.primary.model.reports.reportdesigner;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "Reporttemplate")
public class Reporttemplate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private Long templatecode;

	private String templatename;

	private Date dateCreated;
	private Date dateModified;
	@ManyToOne
	public LSuserMaster createdby;

	@ManyToOne
	private LSuserMaster modifiedby;

	@ManyToOne
	private LSSiteMaster sitemaster;

	private Integer viewoption;

	private Integer templatetype;

	@Transient
	private Response response;

	@ManyToOne
	private ReportDesignerStructure reportdesignstructure;

	@Transient
	private String templatecontent;
	
	private Integer reporttype;	

	public Integer getReporttype() {
		return reporttype;
	}

	public void setReporttype(Integer reporttype) {
		this.reporttype = reporttype;
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

	public String getTemplatecontent() {
		return templatecontent;
	}

	public void setTemplatecontent(String templatecontent) {
		this.templatecontent = templatecontent;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public LSuserMaster getCreatedby() {
		return createdby;
	}

	public void setCreatedby(LSuserMaster createdby) {
		this.createdby = createdby;
	}

	public LSuserMaster getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(LSuserMaster modifiedby) {
		this.modifiedby = modifiedby;
	}

	public LSSiteMaster getSitemaster() {
		return sitemaster;
	}

	public void setSitemaster(LSSiteMaster sitemaster) {
		this.sitemaster = sitemaster;
	}

	public Integer getViewoption() {
		return viewoption;
	}

	public void setViewoption(Integer viewoption) {
		this.viewoption = viewoption;
	}

	public Integer getTemplatetype() {
		return templatetype;
	}

	public void setTemplatetype(Integer templatetype) {
		this.templatetype = templatetype;
	}

	public ReportDesignerStructure getReportdesignstructure() {
		return reportdesignstructure;
	}

	public void setReportdesignstructure(ReportDesignerStructure reportdesignstructure) {
		this.reportdesignstructure = reportdesignstructure;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

}
