package com.agaram.eln.primary.model.protocols;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cloudProtocol.CloudLsLogilabprotocolstepInfo;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;

@Entity(name = "LSlogilabprotocoldetail")
@Table(name = "LSlogilabprotocoldetail")
public class LSlogilabprotocoldetail implements Comparable<LSlogilabprotocoldetail> {
	@Id
	@SequenceGenerator(name = "orderGen", sequenceName = "orderDetail", initialValue = 1000000, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderGen")
	@Column(columnDefinition = "numeric(17,0)", name = "Protocolordercode")
	private Long protocolordercode;

	@Column(columnDefinition = "varchar(250)", name = "Protocolordername")
	private String protoclordername;

	@Column(columnDefinition = "varchar(250)", name = "Keyword")
	private String keyword;
	@Transient
	private Integer multiusergroupcode;
	public Integer getMultiusergroupcode() {
		return multiusergroupcode;
	}

	public void setMultiusergroupcode(Integer multiusergroupcode) {
		this.multiusergroupcode = multiusergroupcode;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	private String orderflag;

	public String getOrderflag() {
		return orderflag;
	}

	public void setOrderflag(String orderflag) {
		this.orderflag = orderflag;
	}

	@Column(name = "Protocoltype")
	private Integer protocoltype;

	@Column(name = "CreatedTimeStamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdtimestamp;

	@Column(name = "CompletedTimeStamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date completedtimestamp;

	@ManyToOne
	private LSprotocolmaster lsprotocolmaster;

	@ManyToOne
	private LSuserMaster lsuserMaster;

	private Integer Testcode;

	@ManyToOne
	private LSsamplemaster lssamplemaster;

	@ManyToOne
	private LSprojectmaster lsprojectmaster;

	@ManyToOne
	private LSprotocolworkflow lSprotocolworkflow;
	
	@ManyToOne
	private Lsrepositoriesdata lsrepositoriesdata;
	
	@ManyToOne
	private Lsrepositories lsrepositories;
	
//	@Transient
//	private Integer lStprotocolworkflow;
//	
//	public Integer getlStprotocolworkflow() {
//		return lStprotocolworkflow;
//	}
//
//	public void setlStprotocolworkflow(LSprotocolworkflow lStprotocolworkflow) {
//		this.lStprotocolworkflow = lSprotocolworkflow != null ? lSprotocolworkflow.getWorkflowcode() : null;
//	}

	@ManyToOne
	private LSuserMaster assignedto;
	
	public Integer versionno = 1;

	@Transient
	LoggedUser objuser;

	@Transient
	LScfttransaction objmanualaudit;

	@Transient
	LScfttransaction objsilentaudit;

	@Transient
	private Date fromdate;

	@Transient
	private Date todate;
	
	@Transient
	private String testname;
	
	@Transient
	private String createdbyusername;

	
//	@Transient
//	private String tenantname;
	
	public String getCreatedbyusername() {
		return createdbyusername;
	}

	public void setCreatedbyusername(String createdbyusername) {
		this.createdbyusername = createdbyusername;
	}

	public String getTestname() {
		return testname;
	}

	public Lsrepositoriesdata getLsrepositoriesdata() {
		return lsrepositoriesdata;
	}

	public Lsrepositories getLsrepositories() {
		return lsrepositories;
	}

	public void setLsrepositoriesdata(Lsrepositoriesdata lsrepositoriesdata) {
		this.lsrepositoriesdata = lsrepositoriesdata;
	}

	public void setLsrepositories(Lsrepositories lsrepositories) {
		this.lsrepositories = lsrepositories;
	}

	public void setTestname(String testname) {
		this.testname = testname;
	}

	@Transient
	private String originurl;

	@Transient
	private Boolean canuserprocess;
	
	@Transient
	private String unifielduserid;
	
	public List<CloudLsLogilabprotocolstepInfo> getCloudLsLogilabprotocolstepInfo() {
		return CloudLsLogilabprotocolstepInfo;
	}

	public void setCloudLsLogilabprotocolstepInfo(List<CloudLsLogilabprotocolstepInfo> cloudLsLogilabprotocolstepInfo) {
		CloudLsLogilabprotocolstepInfo = cloudLsLogilabprotocolstepInfo;
	}

	@Transient
	private List<LSprotocolorderworkflowhistory> lsprotocolorderworkflowhistory;
	
	@Transient
	private Integer register;
	
	@Transient
	private List<CloudLsLogilabprotocolstepInfo> CloudLsLogilabprotocolstepInfo;
	
	@Transient
	private List<LsLogilabprotocolstepInfo> LsLogilabprotocolstepInfo;
	
	public List<LsLogilabprotocolstepInfo> getLsLogilabprotocolstepInfo() {
		return LsLogilabprotocolstepInfo;
	}

	public void setLsLogilabprotocolstepInfo(List<LsLogilabprotocolstepInfo> lsLogilabprotocolstepInfo) {
		LsLogilabprotocolstepInfo = lsLogilabprotocolstepInfo;
	}

	public Integer getVersionno() {
		return versionno;
	}

	public Integer getRegister() {
		return register;
	}

	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}

	public void setRegister(Integer register) {
		this.register = register;
	}

	public List<LSprotocolorderworkflowhistory> getLsprotocolorderworkflowhistory() {
		return lsprotocolorderworkflowhistory;
	}

	public void setLsprotocolorderworkflowhistory(List<LSprotocolorderworkflowhistory> lsprotocolorderworkflowhistory) {
		this.lsprotocolorderworkflowhistory = lsprotocolorderworkflowhistory;
	}

	public String getUnifielduserid() {
		return unifielduserid;
	}

	public void setUnifielduserid(String unifielduserid) {
		this.unifielduserid = unifielduserid;
	}

	public Boolean getCanuserprocess() {
		return canuserprocess;
	}

	public void setCanuserprocess(Boolean canuserprocess) {
		this.canuserprocess = canuserprocess;
	}

	public String getOriginurl() {
		return originurl;
	}

	public void setOriginurl(String originurl) {
		this.originurl = originurl;
	}

//	public String getTenantname() {
//		return tenantname;
//	}
//
//	public void setTenantname(String tenantname) {
//		this.tenantname = tenantname;
//	}

	private Integer approved;

	private Integer rejected;
	
	private Integer sitecode;
	
	private Integer createby;
	
	public Integer getCreateby() {
		return createby;
	}

	public void setCreateby(Integer createby) {
		this.createby = createby;
	}

	public Integer getSitecode() {
		return sitecode;
	}

	public void setSitecode(Integer sitecode) {
		this.sitecode = sitecode;
	}

	public Integer getApproved() {
		return approved;
	}

	public void setApproved(Integer approved) {
		this.approved = approved;
	}

	public LSuserMaster getAssignedto() {
		return assignedto;
	}
	public void setAssignedto(LSuserMaster assignedto) {
		this.assignedto = assignedto;
	}
	
	public Integer getRejected() {
		return rejected;
	}

	public void setRejected(Integer rejected) {
		this.rejected = rejected;
	}

	public LSprotocolworkflow getlSprotocolworkflow() {
		return lSprotocolworkflow;
	}

	public void setlSprotocolworkflow(LSprotocolworkflow lSprotocolworkflow) {
		this.lSprotocolworkflow = lSprotocolworkflow;
	}

	public Date getFromdate() {
		return fromdate;
	}

	public Date getTodate() {
		return todate;
	}

	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}

	public void setTodate(Date todate) {
		this.todate = todate;
	}

	public Long getProtocolordercode() {
		return protocolordercode;
	}

	public void setProtocolordercode(Long protocolordercode) {
		this.protocolordercode = protocolordercode;
	}

	public String getProtoclordername() {
		return protoclordername;
	}

	public void setProtoclordername(String protoclordername) {
		this.protoclordername = protoclordername;
	}

	public Integer getProtocoltype() {
		return protocoltype;
	}

	public void setProtocoltype(Integer protocoltype) {
		this.protocoltype = protocoltype;
	}

	public Date getCreatedtimestamp() {
		return createdtimestamp;
	}

	public void setCreatedtimestamp(Date createdtimestamp) {
		this.createdtimestamp = createdtimestamp;
	}

	public Date getCompletedtimestamp() {
		return completedtimestamp;
	}

	public void setCompletedtimestamp(Date completedtimestamp) {
		this.completedtimestamp = completedtimestamp;
	}

	public LSprotocolmaster getLsprotocolmaster() {
		return lsprotocolmaster;
	}

	public void setLsprotocolmaster(LSprotocolmaster lsprotocolmaster) {
		this.lsprotocolmaster = lsprotocolmaster;
	}

	public LSuserMaster getLsuserMaster() {
		return lsuserMaster;
	}

	public void setLsuserMaster(LSuserMaster lsuserMaster) {
		this.lsuserMaster = lsuserMaster;
	}

	public Integer getTestcode() {
		return Testcode;
	}

	public void setTestcode(Integer testcode) {
		Testcode = testcode;
	}

	public LSsamplemaster getLssamplemaster() {
		return lssamplemaster;
	}

	public void setLssamplemaster(LSsamplemaster lssamplemaster) {
		this.lssamplemaster = lssamplemaster;
	}

	public LSprojectmaster getLsprojectmaster() {
		return lsprojectmaster;
	}

	public void setLsprojectmaster(LSprojectmaster lsprojectmaster) {
		this.lsprojectmaster = lsprojectmaster;
	}

	public LoggedUser getObjuser() {
		return objuser;
	}

	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
	}

	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}

	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	public Integer getIsmultitenant() {
		return ismultitenant;
	}

	public void setIsmultitenant(Integer ismultitenant) {
		this.ismultitenant = ismultitenant;
	}

	@Transient
	private Integer ismultitenant;

	@Override
	public int compareTo(LSlogilabprotocoldetail o) {
		return this.getProtocolordercode().compareTo(o.getProtocolordercode());
	}
}
