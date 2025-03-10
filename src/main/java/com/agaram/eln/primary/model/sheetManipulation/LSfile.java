package com.agaram.eln.primary.model.sheetManipulation;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LSactivity;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;

@Entity
@Table(name = "LSfile")
public class LSfile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "filecode")
	private Integer filecode;
	@Transient
	List<LSsheetupdates> modifiedlist;
	@Column(columnDefinition = "varchar(10)")
	private String extension;
	@Column(columnDefinition = "varchar(100)")
	private String filenameuser;
	@Column(columnDefinition = "varchar(100)")
	private String filenameuuid;
	//@Lob
	@Column(columnDefinition = "text")
	private String filecontent;
	private Integer isactive;
	private Integer versionno;
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifieddate;
	
	@Transient
	private String fileString;
	@Transient
	private Integer testcode;
	@Transient
	private Integer isfinalstep;
	@OneToMany
	@JoinColumn(name="filecode")
	private List<LSfilemethod> lsmethods;
	
	@OneToMany
	@JoinColumn(name="filecode")
	private List<LSfileelnmethod> lselnmethod;
	
	@OneToMany
	@JoinColumn(name="filecode")
	private List<LSfileparameter> lsparameter;
	
	@OneToMany
	@JoinColumn(name="filecode")
	private List<Lssheetworkflowhistory> lssheetworkflowhistory;
	
	@OneToMany
	@JoinColumn(name="filecode")
	private List<LSfiletest> lstest;
	
	@Transient
	private String resultvalues;
	
	@Column(name = "modifiedbyuser")
	private String modifiedbyuser;

	@Column(name = "task")
	private String task;
	
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	
	public String getModifiedbyuser() {
		return modifiedbyuser;
	}
	public void setModifiedbyuser(String modifiedbyuser) {
		this.modifiedbyuser = modifiedbyuser;
	}
	
	public String getResultvalues() {
		return resultvalues;
	}
	public void setResultvalues(String resultvalues) {
		this.resultvalues = resultvalues;
	}
	
	@Transient
	private Boolean isnewsheet;

	public Boolean getIsnewsheet() {
		return isnewsheet;
	}
	public void setIsnewsheet(Boolean isnewsheet) {
		this.isnewsheet = isnewsheet;
	}
	
	@Transient
	private Date notificationdate;
	
	public Date getNotificationdate() {
		return notificationdate;
	}
	public void setNotificationdate(Date notificationdate) {
		this.notificationdate = notificationdate;
	}

	@OneToMany
	@JoinColumn(name="filecode")
	@OrderBy("versionno DESC")
	private List<LSfileversion> lsfileversion;
	
	@ManyToOne 
	private LSSiteMaster lssitemaster;
	
	@ManyToOne 
	private LSuserMaster createby;
	
	@ManyToOne 
	private LSuserMaster modifiedby;
	
	@ManyToOne 
	private LSsheetworkflow lssheetworkflow;
	
	private Integer approved;
	private Integer rejected;
	private Integer retirestatus;
	
	private Integer resultsheet;
	private Integer tagsheet;
	
	public Integer getTagsheet() {
		return tagsheet;
	}
	public void setTagsheet(Integer tagsheet) {
		this.tagsheet = tagsheet;
	}
	public Integer getResultsheet() {
		return resultsheet;
	}
	public void setResultsheet(Integer resultsheet) {
		this.resultsheet = resultsheet;
	}
	public Integer getRetirestatus() {
		return retirestatus;
	}
	public void setRetirestatus(Integer retirestatus) {
		this.retirestatus = retirestatus;
	}

	private String category;
	
	@Transient
	LSuserMaster objLoggeduser;
	
	@Transient
	LScfttransaction objsilentaudit;
	
	@Transient
	LScfttransaction objmanualaudit;
	
	@Transient
	Lssheetworkflowhistory objsheetworkflowhistory;
	
	private Integer viewoption;

	public Integer getViewoption() {
		return viewoption;
	}

	public void setViewoption(Integer viewoption) {
		this.viewoption = viewoption;
	}
	
	public List<LSsheetupdates> getModifiedlist() {
		return modifiedlist;
	}
	public void setModifiedlist(List<LSsheetupdates> arrayList) {
		this.modifiedlist = arrayList;
	}
	public LSfile()
	{
		
	}
	public LSfile(Integer filecode, String filenameuser)
	{
		this.filecode = filecode;
		this.filenameuser = filenameuser;
	}
	
	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}

	@Transient
	private Response response;
	
	@Transient
	LoggedUser objuser;
	
	@Transient
	LSuserMaster LSuserMaster;
	
	@Transient
	LSactivity objActivity;
	
	@Transient
	private Integer ismultitenant;
	
	@Transient
	private LSworkflow currentStep;
	
	public LSworkflow getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(LSworkflow currentStep) {
		this.currentStep = currentStep;
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

	public LSuserMaster getLSuserMaster() {
		return LSuserMaster;
	}

	public void setLSuserMaster(LSuserMaster lSuserMaster) {
		LSuserMaster = lSuserMaster;
	}
	
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}
	
	public Integer getFilecode() {
		return filecode;
	}
	public void setFilecode(Integer filecode) {
		this.filecode = filecode;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getFilenameuser() {
		return filenameuser;
	}
	public void setFilenameuser(String filenameuser) {
		this.filenameuser = filenameuser;
	}
	
	public Integer getIsactive() {
		return isactive;
	}
	public void setIsactive(Integer isactive) {
		this.isactive = isactive;
	}
	
	public Integer getVersionno() {
		return versionno;
	}
	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}
	public String getFilenameuuid() {
		return filenameuuid;
	}
	public void setFilenameuuid(String filenameuuid) {
		this.filenameuuid = filenameuuid;
	}
		
	public String getFilecontent() {
		return filecontent;
	}
	public void setFilecontent(String filecontent) {
		this.filecontent = filecontent;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public List<LSfilemethod> getLsmethods() {
		return lsmethods;
	}
	public void setLsmethods(List<LSfilemethod> lsmethods) {
		this.lsmethods = lsmethods;
	}
	public String getFileString() {
		return fileString;
	}
	public void setFileString(String fileString) {
		this.fileString = fileString;
	}
	public Integer getTestcode() {
		return testcode;
	}
	public void setTestcode(Integer testcode) {
		this.testcode = testcode;
	}
	public List<LSfileparameter> getLsparameter() {
		return lsparameter;
	}
	public void setLsparameter(List<LSfileparameter> lsparameter) {
		this.lsparameter = lsparameter;
	}
	public List<LSfiletest> getLstest() {
		return lstest;
	}
	public void setLstest(List<LSfiletest> lstest) {
		this.lstest = lstest;
	}
	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}
	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}
	public LSuserMaster getObjLoggeduser() {
		return objLoggeduser;
	}
	public void setObjLoggeduser(LSuserMaster objLoggeduser) {
		this.objLoggeduser = objLoggeduser;
	}

	public Date getModifieddate() {
		return modifieddate;
	}

	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}

	public LSuserMaster getCreateby() {
		return createby;
	}

	public void setCreateby(LSuserMaster createby) {
		this.createby = createby;
	}

	public LSuserMaster getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(LSuserMaster modifiedby) {
		this.modifiedby = modifiedby;
	}

	public LSsheetworkflow getLssheetworkflow() {
		return lssheetworkflow;
	}

	public void setLssheetworkflow(LSsheetworkflow lssheetworkflow) {
		this.lssheetworkflow = lssheetworkflow;
	}

	public Integer getApproved() {
		return approved;
	}

	public void setApproved(Integer approved) {
		this.approved = approved;
	}

	public Integer getRejected() {
		return rejected;
	}

	public void setRejected(Integer rejected) {
		this.rejected = rejected;
	}

	public LSactivity getObjActivity() {
		return objActivity;
	}

	public void setObjActivity(LSactivity objActivity) {
		this.objActivity = objActivity;
	}

	public List<Lssheetworkflowhistory> getLssheetworkflowhistory() {
		return lssheetworkflowhistory;
	}

	public void setLssheetworkflowhistory(List<Lssheetworkflowhistory> lssheetworkflowhistory) {
		this.lssheetworkflowhistory = lssheetworkflowhistory;
	}
	public Lssheetworkflowhistory getObjsheetworkflowhistory() {
		return objsheetworkflowhistory;
	}
	public void setObjsheetworkflowhistory(Lssheetworkflowhistory objsheetworkflowhistory) {
		this.objsheetworkflowhistory = objsheetworkflowhistory;
	}
	public List<LSfileversion> getLsfileversion() {
		return lsfileversion;
	}
	public void setLsfileversion(List<LSfileversion> lsfileversion) {
		this.lsfileversion = lsfileversion;
	}
	public Integer getIsmultitenant() {
		return ismultitenant;
	}
	public void setIsmultitenant(Integer ismultitenant) {
		this.ismultitenant = ismultitenant;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Integer getIsfinalstep() {
		return isfinalstep;
	}
	public void setIsfinalstep(Integer isfinalstep) {
		this.isfinalstep = isfinalstep;
	}
	public List<LSfileelnmethod> getLselnmethod() {
		return lselnmethod;
	}
	public void setLselnmethod(List<LSfileelnmethod> lselnmethod) {
		this.lselnmethod = lselnmethod;
	}
	
	
	
	
}
