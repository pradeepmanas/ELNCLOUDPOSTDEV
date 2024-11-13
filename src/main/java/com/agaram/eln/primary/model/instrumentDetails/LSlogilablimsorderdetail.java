package com.agaram.eln.primary.model.instrumentDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.dashboard.LsActiveWidgets;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.general.SearchCriteria;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.ElnmaterialInventory;
import com.agaram.eln.primary.model.material.Material;
import com.agaram.eln.primary.model.material.MaterialInventory;
import com.agaram.eln.primary.model.methodsetup.ELNFileAttachments;
import com.agaram.eln.primary.model.protocols.LSprotocolorderstephistory;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSparsedparameters;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.sheetManipulation.LStestparameter;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LScentralisedUsers;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;

@Entity(name = "LSlogilablimsorderdetail")
@Table(name = "LSlogilablimsorderdetail")
public class LSlogilablimsorderdetail {
	@Id
	@SequenceGenerator(name = "orderGen", sequenceName = "orderDetail", initialValue = 1000000, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderGen")
	@Column(columnDefinition = "numeric(17,0)", name = "batchcode")
	private Long batchcode;

	// columnDefinition = "varchar(250)",
	@Column(columnDefinition = "varchar(250)", name = "BatchID")
	private String batchid;
	
	@Transient
	private String period;
	
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	@Transient
	private SearchCriteria searchCriteria;

	public SearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Column(name = "filetype")
	private Integer filetype;
	@Transient
	LoggedUser objuser;

	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	private Date duedate;
	
	public Date getDuedate() {
		return duedate;
	}

	public void setDuedate(Date duedate) {
		this.duedate = duedate;
	}
	
	@OneToMany
	@JoinColumn(name="batchcode")
	private List<LSOrderElnMethod> lsorderelnmethod;
	
	
	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	private Date cautiondate;
	
	public Date getCautiondate() {
		return cautiondate;
	}

	public void setCautiondate(Date cautiondate) {
		this.cautiondate = cautiondate;
	}

	private String approvelaccept;

	public String getApprovelaccept() {
		return approvelaccept;
	}

	public void setApprovelaccept(String approvelaccept) {
		this.approvelaccept = approvelaccept;
	}

	private Boolean sentforapprovel;
	
	public Boolean getSentforapprovel() {
		return sentforapprovel;
	}

	public void setSentforapprovel(Boolean sentforapprovel) {
		this.sentforapprovel = sentforapprovel;
	}

	public LoggedUser getObjuser() {
		return objuser;
	}

	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
	}

	@Column(name = "approvelstatus")
	private Integer approvelstatus;
	@Column(name = "lockeduser")
	private Integer lockeduser;
	@Column(columnDefinition = "varchar(50)", name = "lockedusername")
	private String lockedusername;

	private Integer testcode;
	private String testname;

	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	Date modifidate;
	
	@Transient
	private LScentralisedUsers lscentralisedusers;
	
	@Transient
	private LSSheetOrderStructure lssheetOrderStructure;
	
	public LSSheetOrderStructure getLssheetOrderStructure() {
		return lssheetOrderStructure;
	}

	public void setLssheetOrderStructure(LSSheetOrderStructure lssheetOrderStructure) {
		this.lssheetOrderStructure = lssheetOrderStructure;
	}

	public LScentralisedUsers getLscentralisedusers() {
		return lscentralisedusers;
	}

	public void setLscentralisedusers(LScentralisedUsers lscentralisedusers) {
		this.lscentralisedusers = lscentralisedusers;
	}

	@Transient
	private List<LSuserMaster> lstuserMaster;
	
	public List<LSuserMaster> getLstuserMaster() {
		return lstuserMaster;
	}

	public void setLstuserMaster(List<LSuserMaster> lstuserMaster) {
		this.lstuserMaster = lstuserMaster;
	}

	public Date getModifidate() {
		return modifidate;
	}

	public void setModifidate(Date modifidate) {
		this.modifidate = modifidate;
	}

	@Column(columnDefinition = "char(10)", name = "OrderFlag")
	private String orderflag;

	@Column(name = "CreatedTimeStamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdtimestamp;

	@Column(name = "CompletedTimeStamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date completedtimestamp;

	@Column(columnDefinition = "varchar(100)", name = "MethodCode")
	private String methodcode;

	@Column(columnDefinition = "varchar(100)", name = "InstrumentCode")
	private String instrumentcode;

	@Column(columnDefinition = "varchar(250)", name = "Keyword")
	private String keyword;

	@Transient
	private String comment;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@ManyToOne
	private LSworkflow lsworkflow;

	@ManyToOne
	private LSfile lsfile;

	@Transient
	List<LSprojectmaster> lstproject;

	@Transient
	List<LSworkflow> lstworkflow;
	
	private Boolean repeat;
	
    private Integer autoregistercount;

	public Integer getAutoregistercount() {
		return autoregistercount;
	}

	public void setAutoregistercount(Integer autoregistercount) {
		this.autoregistercount = autoregistercount;
	}

	public Boolean getRepeat() {
		return repeat;
	}

	public void setRepeat(Boolean repeat) {
		this.repeat = repeat;
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
	private Material material;
	@ManyToOne
	private MaterialInventory materialinventory;
	
	@ManyToOne
	private Elnmaterial elnmaterial;
	@ManyToOne
	private ElnmaterialInventory elnmaterialinventory;

	public Elnmaterial getElnmaterial() {
		return elnmaterial;
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

	@OneToMany
	@JoinColumn(name = "batchcode")
	private List<LsOrderattachments> lsOrderattachments;

	@OneToMany
	@JoinColumn(name = "batchcode")
	private List<ELNFileAttachments> ELNFileAttachments;

	@Transient
	LScfttransaction objmanualaudit;

	@Transient
	private LsAutoregister lsautoregisterorderdetail;

	public LsAutoregister getLsautoregisterorderdetail() {
		return lsautoregisterorderdetail;
	}

	public void setLsautoregisterorderdetail(LsAutoregister lsautoregisterorderdetail) {
		this.lsautoregisterorderdetail = lsautoregisterorderdetail;
	}

	@ManyToOne
	private LSuserMaster lsuserMaster;

	@ManyToOne
	private LSsamplemaster lssamplemaster;

	@ManyToOne
	private LSprojectmaster lsprojectmaster;

	@ManyToOne
	private LSsamplefile lssamplefile;

	@ManyToOne
	private LStestmasterlocal lstestmasterlocal;

	@OneToMany
	@JoinColumn(name = "batchcode")
	private List<LSparsedparameters> lsparsedparameters;

	@OneToMany
	@JoinColumn(name = "batchcode")
	private List<Lsorderworkflowhistory> lsorderworkflowhistory;

	@OneToMany
	@JoinColumn(name = "batchcode")
	private List<Lsbatchdetails> lsbatchdetails;

	@ManyToOne
	private Lsrepositoriesdata lsrepositoriesdata;

	@ManyToOne
	private Lsrepositories lsrepositories;

//	@Transient
//	private List<LSlimsorder> lsLSlimsorder;

	@ManyToOne
	private LsAutoregister lsautoregisterorders;

	public LsAutoregister getLsautoregisterorders() {
		return lsautoregisterorders;
	}

	public void setLsautoregisterorders(LsAutoregister lsautoregisterorders) {
		this.lsautoregisterorders = lsautoregisterorders;
	}
	@Transient
	private List<LSlogilablimsorder> lsLSlogilablimsorder;

	@Transient
	private String projectname;

	@Transient
	private String samplename;

	@Transient
	private String filename;

	@Transient
	private Integer isLock;

	@Transient
	private String nbatchcode;

	@Column(name = "approved")
	private Integer approved;

	@Transient
	private Integer rejected;
	
	
	@Transient
	private List<Long> lstdirectorycode;

	private Integer activeuser;

	public Integer getActiveuser() {
		return activeuser;
	}

	public void setActiveuser(Integer activeuser) {
		this.activeuser = activeuser;
	}
	
	public List<Long> getLstdirectorycode() {
		return lstdirectorycode;
	}

	public void setLstdirectorycode(List<Long> lstdirectorycode) {
		this.lstdirectorycode = lstdirectorycode;
	}

	@Transient
	List<LSprotocolorderstephistory> lSprotocolorderstephistory;

	public List<LSprotocolorderstephistory> getlSprotocolorderstephistory() {
		return lSprotocolorderstephistory;
	}

	public void setlSprotocolorderstephistory(List<LSprotocolorderstephistory> lSprotocolorderstephistory) {
		this.lSprotocolorderstephistory = lSprotocolorderstephistory;
	}
	
	@Transient
	private Date notificationdate;
	
	public Date getNotificationdate() {
		return notificationdate;
	}

	public void setNotificationdate(Date notificationdate) {
		this.notificationdate = notificationdate;
	}

	private Integer filecode;

	private Long directorycode;

	private Integer ordercancell;

	private Integer teamcode;

	public Integer getTeamcode() {
		return teamcode;
	}

	public void setTeamcode(Integer teamcode) {
		this.teamcode = teamcode;
	}

	public Integer getOrdercancell() {
		return ordercancell;
	}

	public void setOrdercancell(Integer ordercancell) {
		this.ordercancell = ordercancell;
	}

	// public Integer getRejected() {
//		return rejected;
//	}
//	public void setRejected(Integer rejected) {
//		this.rejected = rejected;
//	}
	@ManyToOne
	private LSuserMaster assignedto;

	@Transient
	private Integer ismultitenant;

	private Integer orderdisplaytype;

	private Integer viewoption;

	@Column(name = "ordersaved")
	private Integer ordersaved = 0;
	
	public Integer getOrdersaved() {
		return ordersaved;
	}

	public void setOrdersaved(Integer ordersaved) {
		this.ordersaved = ordersaved;
	}
	
	public List<LsOrderattachments> getLsOrderattachments() {
		return lsOrderattachments;
	}

	public void setLsOrderattachments(List<LsOrderattachments> lsOrderattachments) {
		this.lsOrderattachments = lsOrderattachments;
	}

	public List<ELNFileAttachments> getELNFileAttachments() {
		return ELNFileAttachments;
	}

	public void setELNFileAttachments(List<ELNFileAttachments> ELNFileAttachments) {
		this.ELNFileAttachments = ELNFileAttachments;
	}

	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}

	public Lsrepositoriesdata getLsrepositoriesdata() {
		return lsrepositoriesdata;
	}

	public void setLsrepositoriesdata(Lsrepositoriesdata lsrepositoriesdata) {
		this.lsrepositoriesdata = lsrepositoriesdata;
	}

	public List<LSprojectmaster> getLstproject() {
		return lstproject;
	}

	public void setLstproject(List<LSprojectmaster> lstproject) {
		this.lstproject = lstproject;
	}

	public Lsrepositories getLsrepositories() {
		return lsrepositories;
	}

	public void setLsrepositories(Lsrepositories lsrepositories) {
		this.lsrepositories = lsrepositories;
	}

	public List<LSworkflow> getLstworkflow() {
		return lstworkflow;
	}
//	public void setLstworkflow(List<LSworkflow> lstworkflow) {
//		this.lstworkflow = lstworkflow;
//	}

	@Transient
	private boolean canuserprocess;

	public boolean isCanuserprocess() {
		return canuserprocess;
	}

	public void setCanuserprocess(boolean canuserprocess) {
		this.canuserprocess = canuserprocess;
	}

	public void setLstworkflow(List<LSworkflow> lstworkflow) {

//		this.lstworkflow = lstworkflow;

		if (this.lsworkflow != null && lstworkflow != null && lstworkflow.size() > 0) {
			List<Integer> lstworkflowcode = new ArrayList<Integer>();
			if (lstworkflow != null && lstworkflow.size() > 0) {
				lstworkflowcode = lstworkflow.stream().map(LSworkflow::getWorkflowcode).collect(Collectors.toList());

				if (lstworkflowcode.contains(this.lsworkflow.getWorkflowcode())) {
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

		this.lstworkflow = lstworkflow;
	}

	public Integer getApproved() {
		return approved;
	}

	public void setApproved(Integer approved) {
		this.approved = approved;
	}

	public String getNbatchcode() {
		return nbatchcode;
	}

	public void setNbatchcode(String nbatchcode) {
		this.nbatchcode = nbatchcode;
	}

	@Transient
	private Integer isLockbycurrentuser;

	@Transient
	private Integer isFinalStep;

	@Transient
	LSuserMaster objLoggeduser;

	@Transient
	LScfttransaction objsilentaudit;

	@Transient
	private Long orderid;
	@Transient
	private String sampleid;
	@Transient
	private String instrumentname;
	@Transient
	private String parserflag;

	@Transient
	private Response response;

	@Transient
	private Date fromdate;

	@Transient
	private Date todate;

	@Transient
	private Integer checked = 0;

	public Integer getChecked() {
		return checked;
	}

	public void setChecked(Integer checked) {
		this.checked = checked;
	}

	@Transient
	List<LStestparameter> lstestparameter;

	@Transient
	private Boolean noworkflow = false;

	public LSlogilablimsorderdetail() {

	}

	public LSlogilablimsorderdetail(Long batchcode, String batchid, Integer testcode, String testname,
			Date createdtimestamp, Date completedtimestamp, Integer filetype, LSworkflow lsworkflow, String projectname,
			String samplename, String filename) {

		this.batchcode = batchcode;
		this.batchid = batchid;
		this.testcode = testcode;
		this.testname = testname;
		this.lsworkflow = lsworkflow;
		this.createdtimestamp = createdtimestamp;
		this.projectname = projectname;
		this.samplename = samplename;
		this.filename = filename;
		this.filetype = filetype;
		this.completedtimestamp = completedtimestamp;

	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public String getInstrumentname() {
		return instrumentname;
	}

	public Integer getFilecode() {
		return filecode;
	}

	public void setFilecode(Integer filecode) {
		this.filecode = filecode;
	}

	public void setInstrumentname(String instrumentname) {
		this.instrumentname = instrumentname;
	}

	public String getParserflag() {
		return parserflag;
	}

	public void setParserflag(String parserflag) {
		this.parserflag = parserflag;
	}

	public String getSampleid() {
		return sampleid;
	}

	public void setSampleid(String sampleid) {
		this.sampleid = sampleid;
	}

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	public Long getBatchcode() {
		return batchcode;
	}

	public void setBatchcode(Long batchcode) {
		this.batchcode = batchcode;
	}

	public String getBatchid() {

		String Batchid = "ELN" + this.batchcode;

		if (this.filetype != null) {

			if (this.filetype == 3) {
				Batchid = "RESEARCH" + this.batchcode;
			} else if (this.filetype == 4) {
				Batchid = "EXCEL" + this.batchcode;
			} else if (this.filetype == 5) {
				Batchid = "VALIDATE" + this.batchcode;
			} else if (this.filetype == 0) {
				Batchid = this.batchid;
			}

		} else {
			Batchid = this.batchid;
		}
		
		return Batchid;
	}

	public void setBatchid(String batchid) {

		String Batchid = "ELN" + this.batchcode;

		if (this.filetype != null) {

			if (this.filetype == 3) {
				Batchid = "RESEARCH" + this.batchcode;
			} else if (this.filetype == 4) {
				Batchid = "EXCEL" + this.batchcode;
			} else if (this.filetype == 5) {
				Batchid = "VALIDATE" + this.batchcode;
			}

		} else {
			Batchid = batchid;
		}

		this.batchid = Batchid;
	}

	public Integer getFiletype() {
		return filetype;
	}

	public void setFiletype(Integer filetype) {
		this.filetype = filetype;
	}

	public LSworkflow getLsworkflow() {
		return lsworkflow;
	}

	public void setLsworkflow(LSworkflow lsworkflow) {
		this.lsworkflow = lsworkflow;
	}

	public Integer getApprovelstatus() {
		return approvelstatus;
	}

	public void setApprovelstatus(Integer approvelstatus) {
		this.approvelstatus = approvelstatus;
	}

	public Integer getLockeduser() {
		return lockeduser;
	}

	public void setLockeduser(Integer lockeduser) {
		this.lockeduser = lockeduser;
	}

	public LSfile getLsfile() {
		return lsfile;
	}

	public void setLsfile(LSfile lsfile) {
		this.lsfile = lsfile;
	}

	public LSuserMaster getLsuserMaster() {
		return lsuserMaster;
	}

	public void setLsuserMaster(LSuserMaster lsuserMaster) {
		this.lsuserMaster = lsuserMaster;
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

//	public List<LSlimsorder> getLsLSlimsorder() {
//		return lsLSlimsorder;
//	}
//
//	public void setLsLSlimsorder(List<LSlimsorder> lsLSlimsorder) {
//		this.lsLSlimsorder = lsLSlimsorder;
//	}

	public List<LSlogilablimsorder> getLsLSlogilablimsorder() {
		return lsLSlogilablimsorder;
	}

	public void setLsLSlogilablimsorder(List<LSlogilablimsorder> lsLSlogilablimsorder) {
		this.lsLSlogilablimsorder = lsLSlogilablimsorder;
	}

	public Integer getTestcode() {
		return testcode;
	}

	public void setTestcode(Integer testcode) {
		this.testcode = testcode;
	}

	public String getOrderflag() {
		return orderflag;
	}

	public void setOrderflag(String orderflag) {
		this.orderflag = orderflag;
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

	public String getTestname() {
		if (this.lstestmasterlocal != null) {
			return this.lstestmasterlocal.getTestname();
		}
		return testname;
	}

	public void setTestname(String testname) {
		this.testname = testname;
	}

	public String getProjectname() {
		if (projectname == null || projectname == "") {
			return this.lsprojectmaster != null ? this.lsprojectmaster.getProjectname() : "";
		} else {
			return projectname;
		}

	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getSamplename() {

		if (samplename == null || samplename == "") {
			return this.lssamplemaster != null ? this.lssamplemaster.getSamplename() : "";
		} else {
			return samplename;
		}
	}

	public void setSamplename(String samplename) {
		this.samplename = samplename;
	}

	public Integer getIsLock() {
		return isLock;
	}

	public Integer getRejected() {
		return rejected;
	}

	public Integer getIsLockbycurrentuser() {
		return isLockbycurrentuser;
	}

	public Integer getIsFinalStep() {
		return isFinalStep;
	}

	public void setIsLock(Integer isLock) {
		this.isLock = isLock;
	}

	public void setRejected(Integer rejected) {
		this.rejected = rejected;
	}

	public void setIsLockbycurrentuser(Integer isLockbycurrentuser) {
		this.isLockbycurrentuser = isLockbycurrentuser;
	}

	public void setIsFinalStep(Integer isFinalStep) {
		this.isFinalStep = isFinalStep;
	}

	public LSsamplefile getLssamplefile() {
		return lssamplefile;
	}

	public void setLssamplefile(LSsamplefile lssamplefile) {
		this.lssamplefile = lssamplefile;
	}

	public LSuserMaster getObjLoggeduser() {
		return objLoggeduser;
	}

	public void setObjLoggeduser(LSuserMaster objLoggeduser) {
		this.objLoggeduser = objLoggeduser;
	}

	public String getMethodcode() {
		return methodcode;
	}

	public void setMethodcode(String methodcode) {
		this.methodcode = methodcode;
	}

	public String getInstrumentcode() {
		return instrumentcode;
	}

	public void setInstrumentcode(String instrumentcode) {
		this.instrumentcode = instrumentcode;
	}

	public List<LSparsedparameters> getLsparsedparameters() {
		return lsparsedparameters;
	}

	public void setLsparsedparameters(List<LSparsedparameters> lsparsedparameters) {
		this.lsparsedparameters = lsparsedparameters;
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

	public List<Lsorderworkflowhistory> getLsorderworkflowhistory() {
		return lsorderworkflowhistory;
	}

	public void setLsorderworkflowhistory(List<Lsorderworkflowhistory> lsorderworkflowhistory) {
		this.lsorderworkflowhistory = lsorderworkflowhistory;
	}

	public List<LStestparameter> getLstestparameter() {
		return lstestparameter;
	}

	public void setLstestparameter(List<LStestparameter> lstestparameter) {
		this.lstestparameter = lstestparameter;
	}

	public String getFilename() {
		if (filename == null || filename == "") {
			return this.lsfile != null ? this.lsfile.getFilenameuser() : "";
		} else {
			return filename;
		}
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public List<Lsbatchdetails> getLsbatchdetails() {
		return lsbatchdetails;
	}

	public void setLsbatchdetails(List<Lsbatchdetails> lsbatchdetails) {
		this.lsbatchdetails = lsbatchdetails;
	}

	public LSuserMaster getAssignedto() {
		return assignedto;
	}

	public void setAssignedto(LSuserMaster assignedto) {
		this.assignedto = assignedto;
	}

	public Integer getIsmultitenant() {
		return ismultitenant;
	}

	public void setIsmultitenant(Integer ismultitenant) {
		this.ismultitenant = ismultitenant;
	}

	public Boolean getNoworkflow() {
		return noworkflow;
	}

	public void setNoworkflow(Boolean noworkflow) {
		this.noworkflow = noworkflow;
	}

	public String getLockedusername() {
		return lockedusername;
	}

	public void setLockedusername(String lockedusername) {
		this.lockedusername = lockedusername;
	}

	public Long getDirectorycode() {
		return directorycode;
	}

	public void setDirectorycode(Long directorycode) {
		this.directorycode = directorycode;
	}

	public Integer getOrderdisplaytype() {
		return orderdisplaytype;
	}

	public void setOrderdisplaytype(Integer orderdisplaytype) {
		this.orderdisplaytype = orderdisplaytype;
	}

	public LStestmasterlocal getLstestmasterlocal() {
		return lstestmasterlocal;
	}

	public void setLstestmasterlocal(LStestmasterlocal lstestmasterlocal) {
		this.lstestmasterlocal = lstestmasterlocal;
	}

	public Integer getViewoption() {
		return viewoption;
	}

	public void setViewoption(Integer viewoption) {
		this.viewoption = viewoption;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Transient
	private String orderlink;
	
	@Transient
	private LsActiveWidgets lsActiveWidgets;

	public LsActiveWidgets getLsActiveWidgets() {
		return lsActiveWidgets;
	}

	public void setLsActiveWidgets(LsActiveWidgets lsActiveWidgets) {
		this.lsActiveWidgets = lsActiveWidgets;
	}

	public String getOrderlink() {
		return orderlink;
	}

	public void setOrderlink(String orderlink) {
		this.orderlink = orderlink;
	}
	
	 public interface ordersinterface {
	        Long getBatchcode();
	        String getBatchid();
	        Date getCreatedtimestamp();
	        String getTestname();
	        LSuserMasterInterfaceforassign getAssignedto();
	        LSuserMasterInterface getLsuserMaster();
	        Integer getOrdercancell();
	    }
	 
	 public interface LSuserMasterInterfaceforassign {
		    String getUsername();
		}
	 public interface LSuserMasterInterface {
		 String getUsername();
	 }
	 @Transient
		private Integer accouttype;	

		public Integer getAccouttype() {
			return accouttype;
		}

		public void setAccouttype(Integer accouttype) {
			this.accouttype = accouttype;
		}

		public List<LSOrderElnMethod> getLsorderelnmethod() {
			return lsorderelnmethod;
		}

		public void setLsorderelnmethod(List<LSOrderElnMethod> lsorderelnmethod) {
			this.lsorderelnmethod = lsorderelnmethod;
		}



}
