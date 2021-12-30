package com.agaram.eln.primary.model.sheetManipulation;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LSactivity;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;

@Entity
@Table(name = "LSsamplefile")
public class LSsamplefile {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "filesamplecode")
	private Integer filesamplecode;
	
	//@Lob
	@Column(columnDefinition = "text")
	private String filecontent;
	@Column(columnDefinition = "numeric(17,0)")
	private long batchcode;
	
	private Integer testid;
	@Column(columnDefinition = "varchar(150)")
	private String resetflag;
	@Transient
	LoggedUser objuser;
	@Transient
	private LSuserMaster lsuserMaster;
//	@Transient
//	private String tempfilecontent;
//	
//	public String getTempfilecontent() {
//		return tempfilecontent;
//	}
//	public void setTempfilecontent(String tempfilecontent) {
//		this.tempfilecontent = tempfilecontent;
//	}
	public LSuserMaster getLsuserMaster() {
		return lsuserMaster;
	}
	public void setLsuserMaster(LSuserMaster lsuserMaster) {
		this.lsuserMaster = lsuserMaster;
	}
	public LoggedUser getObjuser() {
		return objuser;
	}
	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
	}
	@Transient
	private Response response;
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
	private Integer versionno;
	private Integer createby;
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifieddate;
	private Integer processed;
	
	@ManyToOne 
	private LSuserMaster createbyuser;
	
	@ManyToOne 
	private LSuserMaster modifiedby;
	
	@OneToMany
	@JoinColumn(name="filesamplecode")
	private List<LSsampleresult> lssampleresult;

	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "filesamplecode_filesamplecode")
	@OrderBy("filesamplecodeversion DESC")
	private List<LSsamplefileversion> lssamplefileversion;
	
	@Transient
	LSuserMaster objLoggeduser;
	@Transient
	private String contentvalues;
	@Transient
	LScfttransaction objsilentaudit;
	@Transient
	LScfttransaction objmanualaudit;
	@Transient
	private String contentparameter;
	@Transient
	private boolean doversion;
	
	@Transient
	private Integer ismultitenant;
	
	public String getContentparameter() {
		return contentparameter;
	}
	public void setContentparameter(String contentparameter) {
		this.contentparameter = contentparameter;
	}
	public String getContentvalues() {
		return contentvalues;
	}
	public void setContentvalues(String contentvalues) {
		this.contentvalues = contentvalues;
	}
	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}
	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}
	@Transient
	LSactivity objActivity;
	
	
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}
	
	public Integer getFilesamplecode() {
		return filesamplecode;
	}
	public void setFilesamplecode(Integer filesamplecode) {
		this.filesamplecode = filesamplecode;
	}
	
	public String getFilecontent() {
		return filecontent;
	}
	public void setFilecontent(String filecontent) {
		this.filecontent = filecontent;
	}
	public long getBatchcode() {
		return batchcode;
	}
	public void setBatchcode(long batchcode) {
		this.batchcode = batchcode;
	}
	public Integer getTestid() {
		return testid;
	}
	public void setTestid(Integer testid) {
		this.testid = testid;
	}
	public String getResetflag() {
		return resetflag;
	}
	public void setResetflag(String resetflag) {
		this.resetflag = resetflag;
	}
	public Integer getVersionno() {
		return versionno;
	}
	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}
	public Integer getCreateby() {
		return createby;
	}
	public void setCreateby(Integer createby) {
		this.createby = createby;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public LSuserMaster getObjLoggeduser() {
		return objLoggeduser;
	}
	public void setObjLoggeduser(LSuserMaster objLoggeduser) {
		this.objLoggeduser = objLoggeduser;
	}
	public Integer getProcessed() {
		return processed;
	}
	public void setProcessed(Integer processed) {
		this.processed = processed;
	}

	public List<LSsampleresult> getLssampleresult() {
		return lssampleresult;
	}

	public void setLssampleresult(List<LSsampleresult> lssampleresult) {
		this.lssampleresult = lssampleresult;
	}

	public LSactivity getObjActivity() {
		return objActivity;
	}

	public void setObjActivity(LSactivity objActivity) {
		this.objActivity = objActivity;
	}

	public List<LSsamplefileversion> getLssamplefileversion() {
		return lssamplefileversion;
	}

	public void setLssamplefileversion(List<LSsamplefileversion> lssamplefileversion) {
		this.lssamplefileversion = lssamplefileversion;
	}

	public Date getModifieddate() {
		return modifieddate;
	}

	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}

	public LSuserMaster getCreatebyuser() {
		return createbyuser;
	}

	public void setCreatebyuser(LSuserMaster createbyuser) {
		this.createbyuser = createbyuser;
	}

	public LSuserMaster getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(LSuserMaster modifiedby) {
		this.modifiedby = modifiedby;
	}
	public Integer getIsmultitenant() {
		return ismultitenant;
	}
	public void setIsmultitenant(Integer ismultitenant) {
		this.ismultitenant = ismultitenant;
	}
	public boolean isDoversion() {
		return doversion;
	}
	public void setDoversion(boolean doversion) {
		this.doversion = doversion;
	}
	
	
}
