package com.agaram.eln.primary.model.reports.reportdesigner;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	private LSuserMaster approvedby;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name="templatecode")
    private List<ReportTemplateMapping> reportTemplateMappings;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name="templatecode")
    private List<ReportTemplateVersion> reportTemplateVersion;
	
	@Transient
	private String keystorevariable;
	
	public String keyword;
	
	private Integer lockeduser;
	
	@Transient
	LSuserMaster objLoggeduser;
	
	@Transient 
	private boolean isalreadyLock;


	
	public boolean isIsalreadyLock() {
		return isalreadyLock;
	}

	public void setIsalreadyLock(boolean isalreadyLock) {
		this.isalreadyLock = isalreadyLock;
	}

	public LSuserMaster getObjLoggeduser() {
		return objLoggeduser;
	}

	public void setObjLoggeduser(LSuserMaster objLoggeduser) {
		this.objLoggeduser = objLoggeduser;
	}

	private String lockedusername;

	
	public Integer getLockeduser() {
		return lockeduser;
	}

	public void setLockeduser(Integer lockeduser) {
		this.lockeduser = lockeduser;
	}

	public String getLockedusername() {
		return lockedusername;
	}

	public void setLockedusername(String lockedusername) {
		this.lockedusername = lockedusername;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getKeystorevariable() {
		return keystorevariable;
	}

	public void setKeystorevariable(String keystorevariable) {
		this.keystorevariable = keystorevariable;
	}

	public List<ReportTemplateVersion> getReportTemplateVersion() {
		return reportTemplateVersion;
	}

	public void setReportTemplateVersion(List<ReportTemplateVersion> reportTemplateVersion) {
		this.reportTemplateVersion = reportTemplateVersion;
	}

	public Integer versionno = 1;

	public Integer getVersionno() {
		return versionno;
	}

	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}

	public LSuserMaster getApprovedby() {
		return approvedby;
	}

	public List<ReportTemplateMapping> getReportTemplateMappings() {
		return reportTemplateMappings;
	}

	public void setReportTemplateMappings(List<ReportTemplateMapping> reportTemplateMappings) {
		this.reportTemplateMappings = reportTemplateMappings;
	}

	public void setApprovedby(LSuserMaster approvedby) {
		this.approvedby = approvedby;
	}

	private Date completeddate;
	
	public Date getCompleteddate() {
		return completeddate;
	}

	public void setCompleteddate(Date completeddate) {
		this.completeddate = completeddate;
	}

	@ManyToOne
	private LSSiteMaster sitemaster;

	private Integer viewoption;

	private Integer templatetype;
	
	
	private String fileuid;

	private String fileuri;
	
	private Integer containerstored;
	
	private String fileextention;
	
	
	@Transient
	private Reporttemplate saveastemplate;
	
	
//	@Transient
//	List<LSprojectmaster> lstproject;
//
//	public List<LSprojectmaster> getLstproject() {
//		return lstproject;
//	}
//
//	public void setLstproject(List<LSprojectmaster> lstproject) {
//		this.lstproject = lstproject;
//	}

	public Reporttemplate getSaveastemplate() {
		return saveastemplate;
	}

	public void setSaveastemplate(Reporttemplate saveastemplate) {
		this.saveastemplate = saveastemplate;
	}

	public String getFileextention() {
		return fileextention;
	}

	public void setFileextention(String fileextention) {
		this.fileextention = fileextention;
	}

	public Integer getContainerstored() {
		return containerstored;
	}

	public void setContainerstored(Integer containerstored) {
		this.containerstored = containerstored;
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

	
	@Transient
	private Response response;

	@ManyToOne
	private ReportDesignerStructure reportdesignstructure;

	@Transient
	private String templatecontent;
	
	@Transient
	private Integer ismultitenant;
	
	@Transient
	private boolean isnewversion;
	
	@Transient
	private Long dircodetomove;


	public Long getDircodetomove() {
		return dircodetomove;
	}

	public void setDircodetomove(Long dircodetomove) {
		this.dircodetomove = dircodetomove;
	}

	public boolean isIsnewversion() {
		return isnewversion;
	}

	public void setIsnewversion(boolean isnewversion) {
		this.isnewversion = isnewversion;
	}

	public Integer getIsmultitenant() {
		return ismultitenant;
	}

	public void setIsmultitenant(Integer ismultitenant) {
		this.ismultitenant = ismultitenant;
	}

	
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
