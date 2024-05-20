package com.agaram.eln.primary.model.protocols;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

import org.hibernate.annotations.Type;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cloudProtocol.CloudLsLogilabprotocolstepInfo;
import com.agaram.eln.primary.model.dashboard.LsActiveWidgets;
import com.agaram.eln.primary.model.instrumentDetails.LSOrdernotification;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorder;
import com.agaram.eln.primary.model.instrumentDetails.LsAutoregister;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.ElnmaterialInventory;
import com.agaram.eln.primary.model.material.Material;
import com.agaram.eln.primary.model.material.MaterialInventory;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
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
	@Column(name = "approvelstatus")
	private Integer approvelstatus;
	@Column(columnDefinition = "varchar(250)", name = "Protocolordername")
	private String protoclordername;
	@Transient
	private LSworkflow isFinalStep;
	@Column(columnDefinition = "varchar(250)", name = "Keyword")
	private String keyword;
	@Transient
	private Integer multiusergroupcode;
	@Transient
	public Integer finalworkflow;
	@Transient
	private LSsheetworkflow isfinalstep;
	@Transient
	LoggedUser objuser;
	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	Date modifidate;
	@Transient
	private String comment;

	private Integer lockeduser;

	private String lockedusername;

	@Transient
	private String period;
	
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	private Date duedate;

	public Date getDuedate() {
		return duedate;
	}

	public void setDuedate(Date duedate) {
		this.duedate = duedate;
	}

	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	private Date cautiondate;

	public Date getCautiondate() {
		return cautiondate;
	}

	public void setCautiondate(Date cautiondate) {
		this.cautiondate = cautiondate;
	}

	@ManyToOne
	private Material material;
	@ManyToOne
	private MaterialInventory materialinventory;

	@Transient
	private List<LSlogilablimsorder> lsLSlogilablimsorder;

	public List<LSlogilablimsorder> getLsLSlogilablimsorder() {
		return lsLSlogilablimsorder;
	}

	public void setLsLSlogilablimsorder(List<LSlogilablimsorder> lsLSlogilablimsorder) {
		this.lsLSlogilablimsorder = lsLSlogilablimsorder;
	}

	private Boolean approvelaccept;

	public Boolean getApprovelaccept() {
		return approvelaccept;
	}

	public void setApprovelaccept(Boolean approvelaccept) {
		this.approvelaccept = approvelaccept;
	}

	private Boolean sentforapprovel;

	public Boolean getSentforapprovel() {
		return sentforapprovel;
	}

	public void setSentforapprovel(Boolean sentforapprovel) {
		this.sentforapprovel = sentforapprovel;
	}

	@ManyToOne
	private LSOrdernotification lsordernotification;

	public LSOrdernotification getLsordernotification() {
		return lsordernotification;
	}

	public void setLsordernotification(LSOrdernotification lsordernotification) {
		this.lsordernotification = lsordernotification;
	}

	@ManyToOne
	private Elnmaterial elnmaterial;
	@ManyToOne
	private ElnmaterialInventory elnmaterialinventory;

	private Integer activeuser;

	public Integer getActiveuser() {
		return activeuser;
	}

	public void setActiveuser(Integer activeuser) {
		this.activeuser = activeuser;
	}

	public Elnmaterial getElnmaterial() {
		return elnmaterial;
	}

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

	public void setElnmaterial(Elnmaterial elnmaterial) {
		this.elnmaterial = elnmaterial;
	}

	public ElnmaterialInventory getElnmaterialinventory() {
		return elnmaterialinventory;
	}

	public void setElnmaterialinventory(ElnmaterialInventory elnmaterialinventory) {
		this.elnmaterialinventory = elnmaterialinventory;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public MaterialInventory getMaterialinventory() {
		return materialinventory;
	}

	public void setMaterialinventory(MaterialInventory materialinventory) {
		this.materialinventory = materialinventory;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getModifidate() {
		return modifidate;
	}

	public void setModifidate(Date modifidate) {
		this.modifidate = modifidate;
	}

	public LoggedUser getObjuser() {
		return objuser;
	}

	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
	}

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

	private Integer testcode;
	@ManyToOne
	@Transient
	private LStestmasterlocal lstestmasterlocal;

	public LStestmasterlocal getLstestmasterlocal() {
		return lstestmasterlocal;
	}

	public void setLstestmasterlocal(LStestmasterlocal lstestmasterlocal) {
		this.lstestmasterlocal = lstestmasterlocal;
	}

	private Long directorycode;

	private Integer teamcode;

	@ManyToOne
	private LSsamplemaster lssamplemaster;

	@ManyToOne
	private LSprojectmaster lsprojectmaster;

	@ManyToOne
	private LSprotocolworkflow lSprotocolworkflow;

	@ManyToOne
	private Elnprotocolworkflow elnprotocolworkflow;

	public Elnprotocolworkflow getElnprotocolworkflow() {
		return elnprotocolworkflow;
	}

	public void setElnprotocolworkflow(Elnprotocolworkflow elnprotocolworkflow) {
		this.elnprotocolworkflow = elnprotocolworkflow;
	}

	@ManyToOne
	private LSworkflow lsworkflow;

	private Integer ordercancell;

	private Integer orderstarted = 0;

	public Integer getOrderstarted() {
		return orderstarted;
	}

	public void setOrderstarted(Integer orderstarted) {
		this.orderstarted = orderstarted;
	}

	@ManyToOne
	LSuserMaster orderstartedby;

	public LSuserMaster getOrderstartedby() {
		return orderstartedby;
	}

	public void setOrderstartedby(LSuserMaster orderstartedby) {
		this.orderstartedby = orderstartedby;
	}

	@Column(name = "orderstartedon")
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderstartedon;

	public Date getOrderstartedon() {
		return orderstartedon;
	}

	public void setOrderstartedon(Date orderstartedon) {
		this.orderstartedon = orderstartedon;
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
	LSuserMaster objLoggeduser;

	@Transient
	List<LSworkflow> lstworkflow;

	@Transient
	List<Elnprotocolworkflow> lstelnprotocolworkflow;

	@Transient
	private List<Long> lstdirectorycode;

	@Transient
	Integer searchCriteriaType;

	@Transient
	private List<LSuserMaster> lstuserMaster;

	public List<Elnprotocolworkflow> getLstelnprotocolworkflow() {
		return lstelnprotocolworkflow;
	}

	public void setLstelnprotocolworkflow(List<Elnprotocolworkflow> lstelnprotocolworkflow) {
//		this.lstelnprotocolworkflow = lstelnprotocolworkflow;
		if (this.elnprotocolworkflow != null && lstelnprotocolworkflow != null && lstelnprotocolworkflow.size() > 0) {
			List<Integer> lstworkflowcode = new ArrayList<Integer>();
			if (lstelnprotocolworkflow != null && lstelnprotocolworkflow.size() > 0) {
				lstworkflowcode = lstelnprotocolworkflow.stream().map(Elnprotocolworkflow::getWorkflowcode)
						.collect(Collectors.toList());

				if (lstworkflowcode.contains(this.elnprotocolworkflow.getWorkflowcode())) {
					this.setCanuserprocess(true);
				} else {
					this.setCanuserprocess(false);
				}
			} else {
				this.setCanuserprocess(false);
			}
		} else {
			this.setCanuserprocess(false);
		}

		this.lstelnprotocolworkflow = lstelnprotocolworkflow;
	}

	public List<LSuserMaster> getLstuserMaster() {
		return lstuserMaster;
	}

	public void setLstuserMaster(List<LSuserMaster> lstuserMaster) {
		this.lstuserMaster = lstuserMaster;
	}

	@Transient
	private LSworkflow currentStep;

	public LSworkflow getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(LSworkflow currentStep) {
		this.currentStep = currentStep;
	}

	public List<Long> getLstdirectorycode() {
		return lstdirectorycode;
	}

	public void setLstdirectorycode(List<Long> lstdirectorycode) {
		this.lstdirectorycode = lstdirectorycode;
	}

	public Integer getSearchCriteriaType() {
		return searchCriteriaType;
	}

	public void setSearchCriteriaType(Integer searchCriteriaType) {
		this.searchCriteriaType = searchCriteriaType;
	}

	private Integer viewoption;

	public List<LSworkflow> getLstworkflow() {
		return lstworkflow;
	}

	public void setLstworkflow(List<LSworkflow> lstworkflow) {
		this.lstworkflow = lstworkflow;
//		if (this.lsworkflow != null && lstworkflow != null  && lstworkflow.size() > 0) {
//			List<Integer> lstworkflowcode = new ArrayList<Integer>();
//			if (lstworkflow != null && lstworkflow.size() > 0) {
//				lstworkflowcode = lstworkflow.stream().map(LSworkflow::getWorkflowcode).collect(Collectors.toList());
//
//				if (lstworkflowcode.contains(this.lsworkflow.getWorkflowcode())) {
//					this.setCanuserprocess(true);
//				} else {
//					this.setCanuserprocess(false);
//				}
//			} else {
//				this.setCanuserprocess(false);
//			}
//		} else {
//			this.setCanuserprocess(false);
//		}
//		
//		this.lstworkflow = lstworkflow;

	}

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

	private Integer orderdisplaytype;

	@Transient
	List<LSprojectmaster> lstproject;

	public LSworkflow getLsworkflow() {
		return lsworkflow;
	}

	public void setLsworkflow(LSworkflow lsworkflow) {
		this.lsworkflow = lsworkflow;
	}

	@Transient
	private Integer activekey;

	public Integer getActivekey() {
		return activekey;
	}

	public void setActivekey(Integer activekey) {
		this.activekey = activekey;
	}

	@Transient
	private Date notificationdate;

	public Date getNotificationdate() {
		return notificationdate;
	}

	public void setNotificationdate(Date notificationdate) {
		this.notificationdate = notificationdate;
	}

	public List<LSprojectmaster> getLstproject() {
		return lstproject;
	}

	public void setLstproject(List<LSprojectmaster> lstproject) {
		this.lstproject = lstproject;
	}

	public Integer getOrderdisplaytype() {
		return orderdisplaytype;
	}

	public void setOrderdisplaytype(Integer orderdisplaytype) {
		this.orderdisplaytype = orderdisplaytype;
	}

	public Long getDirectorycode() {
		return directorycode;
	}

	public void setDirectorycode(Long directorycode) {
		this.directorycode = directorycode;
	}

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

	@ManyToOne
	private LsAutoregister lsautoregister;

	public LsAutoregister getLsautoregister() {
		return lsautoregister;
	}

	public void setLsautoregister(LsAutoregister lsautoregister) {
		this.lsautoregister = lsautoregister;
	}

	private Boolean repeat;

	public Boolean getRepeat() {
		return repeat;
	}

	public void setRepeat(Boolean repeat) {
		this.repeat = repeat;
	}

	@Transient
	private String originurl;

	@Transient
	private Boolean canuserprocess;

	@Transient
	private String unifielduserid;

	@Transient
	private LsAutoregister lsautoregisterorder;

	public LsAutoregister getLsautoregisterorder() {
		return lsautoregisterorder;
	}

	public void setLsautoregisterorder(LsAutoregister lsautoregisterorder) {
		this.lsautoregisterorder = lsautoregisterorder;
	}

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
		return testcode;
	}
	
	public void setTestcode(Integer testcode) {
		this.testcode = testcode;
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

	public Integer getViewoption() {
		return viewoption;
	}

	public void setViewoption(Integer viewoption) {
		this.viewoption = viewoption;
	}

	public Integer getOrdercancell() {
		return ordercancell;
	}

	public void setOrdercancell(Integer ordercancell) {
		this.ordercancell = ordercancell;
	}

	public Integer getTeamcode() {
		return teamcode;
	}

	public void setTeamcode(Integer teamcode) {
		this.teamcode = teamcode;
	}

	public Integer getApprovelstatus() {
		return approvelstatus;
	}

	public void setApprovelstatus(Integer approvelstatus) {
		this.approvelstatus = approvelstatus;
	}

	public LSworkflow getIsFinalStep() {
		return isFinalStep;
	}

	public void setIsFinalStep(LSworkflow isFinalStep) {
		this.isFinalStep = isFinalStep;
	}

	public Integer getFinalworkflow() {
		return finalworkflow;
	}

	public void setFinalworkflow(Integer finalworkflow) {
		this.finalworkflow = finalworkflow;
	}

	public LSsheetworkflow getIsfinalstep() {
		return isfinalstep;
	}

	public void setIsfinalstep(LSsheetworkflow isfinalstep) {
		this.isfinalstep = isfinalstep;
	}

	public LSuserMaster getObjLoggeduser() {
		return objLoggeduser;
	}

	public void setObjLoggeduser(LSuserMaster objLoggeduser) {
		this.objLoggeduser = objLoggeduser;
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

	@Transient
	private String orderlink;

	public String getOrderlink() {
		return orderlink;
	}

	public void setOrderlink(String orderlink) {
		this.orderlink = orderlink;
	}

	@Transient
	private List<LSprotocolorderversion> lSprotocolorderversion;

	public List<LSprotocolorderversion> getlSprotocolorderversion() {
		return lSprotocolorderversion;
	}

	public void setlSprotocolorderversion(List<LSprotocolorderversion> lSprotocolorderversion) {
		this.lSprotocolorderversion = lSprotocolorderversion;
	}

	@Transient
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public interface Protocolorder {
		public Long getProtocolordercode();

		public String getProtoclordername();

		public Date getCreatedtimestamp();

		public LSuserMasterInterfaceforassignto getAssignedto();

		public LSuserMasterInterface getLsuserMaster();
		
		public Integer getTestcode();
		
		public Integer getOrdercancell();

	}

	public interface LSuserMasterInterfaceforassignto {
		String getUsername();
	}
	public interface LSuserMasterInterface {
		String getUsername();
	}
	
	@Transient
	private LsActiveWidgets lsActiveWidgets;

	public LsActiveWidgets getLsActiveWidgets() {
		return lsActiveWidgets;
	}

	public void setLsActiveWidgets(LsActiveWidgets lsActiveWidgets) {
		this.lsActiveWidgets = lsActiveWidgets;
	}
	@Transient
	private Integer accouttype;

	public Integer getAccouttype() {
		return accouttype;
	}

	public void setAccouttype(Integer accouttype) {
		this.accouttype = accouttype;
	}	
}
