package com.agaram.eln.primary.model.protocols;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.agaram.eln.primary.model.dashboard.LsActiveWidgets;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "LSprotocolmaster")
public class LSprotocolmaster implements Comparable<LSprotocolmaster> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer protocolmastercode;
	@Column(columnDefinition = "varchar(120)")
	public String protocolmastername;
	public Integer protocolstatus;
	public Integer status;
	public Integer createdby;
	@Transient
	private LSsheetworkflow isfinalstep;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastmodified;
	@Transient
	public Integer finalworkflow;
	public Integer sharewithteam;
	@Column(columnDefinition = "varchar(120)")
	public String createdbyusername;
	@Column(name = "lssitemaster_sitecode")
	private Integer lssitemaster;

	private Integer defaulttemplate;
	
	private Integer retirestatus;

	public Integer getRetirestatus() {
		return retirestatus;
	}

	public void setRetirestatus(Integer retirestatus) {
		this.retirestatus = retirestatus;
	}

	@Transient
	private String createdateprotocol;

	@ManyToOne
	private LSprotocolworkflow lSprotocolworkflow;

	@ManyToOne
	private LSsheetworkflow lssheetworkflow;
	
	@ManyToOne
	private ElnprotocolTemplateworkflow elnprotocoltemplateworkflow;

	private Integer approved;

	private Integer rejected;

	public Integer versionno = 0;

	private String category;
	
	private Integer viewoption;

	public Integer getViewoption() {
		return viewoption;
	}

	@OneToMany
	@JoinColumn(name="protocolmastercode")
	private List<LSprotocolmethod> lsprotocolmethod;
	
	public List<LSprotocolmethod> getLsprotocolmethod() {
		return lsprotocolmethod;
	}

	public void setLsprotocolmethod(List<LSprotocolmethod> lsprotocolmethod) {
		this.lsprotocolmethod = lsprotocolmethod;
	}

	public void setViewoption(Integer viewoption) {
		this.viewoption = viewoption;
	}

	@Transient
	private List<LSprotocolworkflowhistory> lsprotocolworkflowhistory;

	@Transient
	private List<LSprotocolversion> lsprotocolversion;

	@Transient
	LSuserMaster LSuserMaster;
	
	@Transient
	private List<LSuserMaster> lstuserMaster;

	@OneToMany
	@JoinColumn(name = "protocolmastercode")
	private List<LSprotocolmastertest> lstest;

	@Transient
	private Date notificationdate;
	
	public Date getNotificationdate() {
		return notificationdate;
	}

	public void setNotificationdate(Date notificationdate) {
		this.notificationdate = notificationdate;
	}
	
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	public String protocoldatainfo;
	
	public String getProtocoldatainfo() {
		return protocoldatainfo;
	}

	public void setProtocoldatainfo(String protocoldatainfo) {
		this.protocoldatainfo = protocoldatainfo;
	}

	@Transient
	private LSworkflow currentStep;
	
	@Transient
	private Integer activekey;
	
	public Integer getActivekey() {
		return activekey;
	}

	public void setActivekey(Integer activekey) {
		this.activekey = activekey;
	}
	
	public LSworkflow getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(LSworkflow currentStep) {
		this.currentStep = currentStep;
	}
	
	public Integer getDefaulttemplate() {
		return defaulttemplate;
	}

	public void setDefaulttemplate(Integer defaulttemplate) {
		this.defaulttemplate = defaulttemplate;
	}

	public Integer getRejected() {
		return rejected;
	}

	public LSsheetworkflow getLssheetworkflow() {
		return lssheetworkflow;
	}

	public void setLssheetworkflow(LSsheetworkflow lssheetworkflow) {
		this.lssheetworkflow = lssheetworkflow;
	}

	public void setRejected(Integer rejected) {
		this.rejected = rejected;
	}

	public Integer getApproved() {
		return approved;
	}

	public void setApproved(Integer approved) {
		this.approved = approved;
	}

	public LSprotocolworkflow getlSprotocolworkflow() {
		return lSprotocolworkflow;
	}

	public void setlSprotocolworkflow(LSprotocolworkflow lSprotocolworkflow) {
		this.lSprotocolworkflow = lSprotocolworkflow;
	}

	public Integer getLssitemaster() {
		return lssitemaster;
	}

	public void setLssitemaster(Integer lssitemaster) {
		this.lssitemaster = lssitemaster;
	}

	public Integer getProtocolmastercode() {
		return protocolmastercode;
	}

	public void setProtocolmastercode(Integer protocolmastercode) {
		this.protocolmastercode = protocolmastercode;
	}

	public String getProtocolmastername() {
		return protocolmastername;
	}

	public void setProtocolmastername(String protocolmastername) {
		this.protocolmastername = protocolmastername;
	}

	public Integer getProtocolstatus() {
		return protocolstatus;
	}

	public void setProtocolstatus(Integer protocolstatus) {
		this.protocolstatus = protocolstatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	@Transient
	private Integer ismultitenant;

	public Integer getIsmultitenant() {
		return ismultitenant;
	}

	public void setIsmultitenant(Integer ismultitenant) {
		this.ismultitenant = ismultitenant;
	}

	// public LSSiteMaster getLssitemaster() {
//		return lssitemaster;
//	}
//	public void setLssitemaster(LSSiteMaster lssitemaster) {
//		this.lssitemaster = lssitemaster;
//	}
	public Integer getCreatedby() {
		return createdby;
	}

	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}

	public Integer getSharewithteam() {
		return sharewithteam;
	}

	public void setSharewithteam(Integer sharewithteam) {
		this.sharewithteam = sharewithteam;
	}

	public String getCreatedbyusername() {
		return createdbyusername;
	}

	public void setCreatedbyusername(String createdbyusername) {
		this.createdbyusername = createdbyusername;
	}

	public String getCreatedateprotocol() {
		return createdateprotocol;
	}

	public void setCreatedateprotocol(String createdateprotocol) {
		this.createdateprotocol = createdateprotocol;
	}

	public List<LSprotocolworkflowhistory> getLsprotocolworkflowhistory() {
		return lsprotocolworkflowhistory;
	}

	public void setLsprotocolworkflowhistory(List<LSprotocolworkflowhistory> lsprotocolworkflowhistory) {
		this.lsprotocolworkflowhistory = lsprotocolworkflowhistory;
	}

	public List<LSprotocolversion> getLsprotocolversion() {
		return lsprotocolversion;
	}

	public void setLsprotocolversion(List<LSprotocolversion> lsprotocolversion) {
		this.lsprotocolversion = lsprotocolversion;
	}

	public Integer getVersionno() {
		return versionno;
	}

	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}

	@Override
	public String toString() {
		return "LSprotocolmaster [protocolmastercode=" + protocolmastercode + ", protocolmastername="
				+ protocolmastername + ", protocolstatus=" + protocolstatus + ", status=" + status + ", createdby="
				+ createdby + ", createdate=" + createdate + ", lastmodified=" + lastmodified + " , sharewithteam=" + sharewithteam + ", createdbyusername="
				+ createdbyusername + ", lssitemaster=" + lssitemaster + ", createdateprotocol=" + createdateprotocol
				+ ", lSprotocolworkflow=" + lSprotocolworkflow + ", approved=" + approved + ", rejected=" + rejected
				+ ", versionno=" + versionno + ", lsprotocolworkflowhistory=" + lsprotocolworkflowhistory
				+ ", lsprotocolversion=" + lsprotocolversion + ", ismultitenant=" + ismultitenant + "]";
	}

	@Override
	public int compareTo(LSprotocolmaster o) {
		return this.getProtocolmastercode().compareTo(o.getProtocolmastercode());
	}

	public LSuserMaster getLSuserMaster() {
		return LSuserMaster;
	}

	public void setLSuserMaster(LSuserMaster lSuserMaster) {
		LSuserMaster = lSuserMaster;
	}

	public List<LSprotocolmastertest> getLstest() {
		return lstest;
	}

	public void setLstest(List<LSprotocolmastertest> lstest) {
		this.lstest = lstest;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public LSsheetworkflow getIsfinalstep() {
		return isfinalstep;
	}

	public void setIsfinalstep(LSsheetworkflow isfinalstep) {
		this.isfinalstep = isfinalstep;
	}

	public Integer getFinalworkflow() {
		return finalworkflow;
	}

	public void setFinalworkflow(Integer finalworkflow) {
		this.finalworkflow = finalworkflow;
	}
	
	private String fileuid;
	
	private Integer containerstored;
	
	private String fileuri;

	public String getFileuid() {
		return fileuid;
	}

	public void setFileuid(String fileuid) {
		this.fileuid = fileuid;
	}

	public Integer getContainerstored() {
		return containerstored;
	}

	public void setContainerstored(Integer containerstored) {
		this.containerstored = containerstored;
	}

	public String getFileuri() {
		return fileuri;
	}

	public void setFileuri(String fileuri) {
		this.fileuri = fileuri;
	}

	public Date getLastmodified() {
		return lastmodified;
	}

	public void setLastmodified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}
	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	private Date fromdate; 
	
	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	private Date todate;

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

	public ElnprotocolTemplateworkflow getElnprotocoltemplateworkflow() {
		return elnprotocoltemplateworkflow;
	}

	public void setElnprotocoltemplateworkflow(ElnprotocolTemplateworkflow elnprotocoltemplateworkflow) {
		this.elnprotocoltemplateworkflow = elnprotocoltemplateworkflow;
	}

	public List<LSuserMaster> getLstuserMaster() {
		return lstuserMaster;
	}

	public void setLstuserMaster(List<LSuserMaster> lstuserMaster) {
		this.lstuserMaster = lstuserMaster;
	} 
	
	@Transient
	private LsActiveWidgets lsActiveWidgets;

	public LsActiveWidgets getLsActiveWidgets() {
		return lsActiveWidgets;
	}

	public void setLsActiveWidgets(LsActiveWidgets lsActiveWidgets) {
		this.lsActiveWidgets = lsActiveWidgets;
	}
	
	
}
