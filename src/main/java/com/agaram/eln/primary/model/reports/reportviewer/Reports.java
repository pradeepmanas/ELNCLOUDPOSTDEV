package com.agaram.eln.primary.model.reports.reportviewer;

import java.util.Date;
import java.util.List;

import com.agaram.eln.primary.model.general.Response;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.reports.reportdesigner.Reporttemplate;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "Reports")
public class Reports {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private Long reportcode;

	private String reportname;

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

	@ManyToOne
	private ReportViewerStructure reportviewerstructure;

	@Transient
	private List<LStestmasterlocal> lstestmasterlocal;

	public List<LStestmasterlocal> getLstestmasterlocal() {
		return lstestmasterlocal;
	}

	public void setLstestmasterlocal(List<LStestmasterlocal> lstestmasterlocal) {
		this.lstestmasterlocal = lstestmasterlocal;
	}

	@ManyToOne
	private Reporttemplate reporttemplate;

	@Transient
	private String reporttemplatecontent;

//	@Transient
	private Date fromdate;

	@Transient
	private Response response;

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	// @Transient
	private Date todate;

	public Long getReportcode() {
		return reportcode;
	}

	public void setReportcode(Long reportcode) {
		this.reportcode = reportcode;
	}

	public String getReportname() {
		return reportname;
	}

	public void setReportname(String reportname) {
		this.reportname = reportname;
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

	public ReportViewerStructure getReportviewerstructure() {
		return reportviewerstructure;
	}

	public void setReportviewerstructure(ReportViewerStructure reportviewerstructure) {
		this.reportviewerstructure = reportviewerstructure;
	}

	public String getReporttemplatecontent() {
		return reporttemplatecontent;
	}

	public void setReporttemplatecontent(String reporttemplatecontent) {
		this.reporttemplatecontent = reporttemplatecontent;
	}

	public Reporttemplate getReporttemplate() {
		return reporttemplate;
	}

	public void setReporttemplate(Reporttemplate reporttemplate) {
		this.reporttemplate = reporttemplate;
	}

	public Date getFromdate() {
		return fromdate;
	}

	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}

	public Date getTodate() {
		return todate;
	}

	public void setTodate(Date todate) {
		this.todate = todate;
	}

}
