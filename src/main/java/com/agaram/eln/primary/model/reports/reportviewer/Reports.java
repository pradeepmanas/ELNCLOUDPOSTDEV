package com.agaram.eln.primary.model.reports.reportviewer;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.general.Response;
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

	private Date datecreated;
	private Date datemodified;
	
	@ManyToOne
	public LSuserMaster createdby;

	@ManyToOne
	private LSuserMaster modifiedby;

	@ManyToOne
	private LSSiteMaster sitemaster;

	private Integer viewoption;
	
	private Integer versionno;

	private Integer templatetype;
	
	@Transient
	private boolean isnewversion;

	public boolean isIsnewversion() {
		return isnewversion;
	}

	public void setIsnewversion(boolean isnewversion) {
		this.isnewversion = isnewversion;
	}

	@ManyToOne
	private ReportViewerStructure reportviewerstructure;

	@Transient
	private List<LStestmasterlocal> lstestmasterlocal;
	
	@Transient
	private String keystorevariable;
	
	public String getKeystorevariable() {
		return keystorevariable;
	}

	public void setKeystorevariable(String keystorevariable) {
		this.keystorevariable = keystorevariable;
	}

	public List<LStestmasterlocal> getLstestmasterlocal() {
		return lstestmasterlocal;
	}

	public void setLstestmasterlocal(List<LStestmasterlocal> lstestmasterlocal) {
		this.lstestmasterlocal = lstestmasterlocal;
	}
	
	public Integer getVersionno() {
		return versionno;
	}

	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}

	public List<ReportsVersion> getReportVersions() {
		return reportVersions;
	}

	public void setReportVersions(List<ReportsVersion> reportVersions) {
		this.reportVersions = reportVersions;
	}

	@ManyToOne
	private Reporttemplate reporttemplate;

	@Transient
	private String reporttemplatecontent;

	@Transient
	private Date fromdate;

	@Transient
	private Response response;
	
	@Transient
	private Integer ismultitenant;	
	
	private String fileuid;

	private String fileuri;
	
	private Integer containerstored;
	
	private String fileextention;
	
	@ManyToOne
	private LSuserMaster approvedby;	
	
	@OneToMany
	@JoinColumn(name="reportcode")
	private List<ReportsVersion> reportVersions;
	
	public LSuserMaster getApprovedby() {
		return approvedby;
	}

	public void setApprovedby(LSuserMaster approvedby) {
		this.approvedby = approvedby;
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

	public Integer getContainerstored() {
		return containerstored;
	}

	public void setContainerstored(Integer containerstored) {
		this.containerstored = containerstored;
	}

	public String getFileextention() {
		return fileextention;
	}

	public void setFileextention(String fileextention) {
		this.fileextention = fileextention;
	}

	public Integer getIsmultitenant() {
		return ismultitenant;
	}

	public void setIsmultitenant(Integer ismultitenant) {
		this.ismultitenant = ismultitenant;
	}

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
	
	public Date getDatecreated() {
		return datecreated;
	}

	public void setDatecreated(Date datecreated) {
		this.datecreated = datecreated;
	}

	public Date getDatemodified() {
		return datemodified;
	}

	public void setDatemodified(Date datemodified) {
		this.datemodified = datemodified;
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

	private Date completeddate;
	
	public Date getCompleteddate() {
		return completeddate;
	}

	public void setCompleteddate(Date completeddate) {
		this.completeddate = completeddate;
	}
}
