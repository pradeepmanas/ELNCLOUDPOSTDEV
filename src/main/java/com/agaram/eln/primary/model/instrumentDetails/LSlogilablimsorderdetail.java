package com.agaram.eln.primary.model.instrumentDetails;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.general.SearchCriteria;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSparsedparameters;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LStestparameter;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;

@Entity(name = "LSlogilablimsorderdetail")
@Table(name = "LSlogilablimsorderdetail")
public class LSlogilablimsorderdetail {
	@Id
	@SequenceGenerator(name = "orderGen", sequenceName = "orderDetail", initialValue = 1000000, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "orderGen")
	@Column(columnDefinition = "numeric(17,0)",name = "batchcode") 
	private Long batchcode;

	//columnDefinition = "varchar(250)",
	@Column(columnDefinition = "varchar(250)",name = "BatchID") 
	private String batchid;
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
	
	private Integer testcode;
	private String testname;
	
	//columnDefinition = "nchar(10)",
	@Column(columnDefinition = "char(10)",name = "OrderFlag")
	private String orderflag;
	//columnDefinition = "date",
	@Column(name = "CreatedTimeStamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdtimestamp;
	//columnDefinition = "date",
	@Column(name = "CompletedTimeStamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date completedtimestamp;
	//columnDefinition = "varchar(100)",
	@Column(columnDefinition = "varchar(100)",name = "MethodCode")
	private String methodcode;
	//columnDefinition = "varchar(100)",
	@Column(columnDefinition = "varchar(100)",name = "InstrumentCode")
	private String instrumentcode;
	
	@ManyToOne
	private LSworkflow lsworkflow;
	
	@ManyToOne
	private LSfile lsfile;
	
	@Transient
	List<LSprojectmaster> lstproject;
	
	@Transient
	List<LSworkflow> lstworkflow;
	
	@OneToMany
	@JoinColumn(name="batchcode")
	private List<LsOrderattachments> lsOrderattachments;
	
	@Transient
	LScfttransaction objmanualaudit;
	
	@ManyToOne
	private LSuserMaster lsuserMaster;
	
	@ManyToOne
	private LSsamplemaster lssamplemaster;
	
	@ManyToOne
	private LSprojectmaster lsprojectmaster;
	
	@ManyToOne
	private LSsamplefile lssamplefile;
	
	@OneToMany
	@JoinColumn(name="batchcode")
	private List<LSparsedparameters> lsparsedparameters;
	
	@OneToMany
	@JoinColumn(name="batchcode")
	private List<Lsorderworkflowhistory> lsorderworkflowhistory;
	
	@OneToMany
	@JoinColumn(name="batchcode")
	private List<Lsbatchdetails> lsbatchdetails;
	
	
	@ManyToOne
	private Lsrepositoriesdata lsrepositoriesdata;
	
	@ManyToOne
	private Lsrepositories lsrepositories;
	
	@Transient
	private List<LSlimsorder> lsLSlimsorder;
	
	@Transient
	private String projectname;
	
	@Transient
	private String samplename;
	
	@Transient
	private String filename;
	
	@Transient
	private int isLock;
	
	@Transient
	private String nbatchcode;
	
	@Column(name = "approved") 
	private Integer approved;
	
	@Transient
	private Integer rejected;
	
	private Integer filecode;
	
	public Integer getRejected() {
		return rejected;
	}
	public void setRejected(Integer rejected) {
		this.rejected = rejected;
	}
	@ManyToOne
	private LSuserMaster assignedto;
	
	@Transient
	private Integer ismultitenant;
	
	public List<LsOrderattachments> getLsOrderattachments() {
		return lsOrderattachments;
	}
	public void setLsOrderattachments(List<LsOrderattachments> lsOrderattachments) {
		this.lsOrderattachments = lsOrderattachments;
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
	public void setLstworkflow(List<LSworkflow> lstworkflow) {
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
	private int isLockbycurrentuser;
	
	@Transient
	private int isFinalStep;
	
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
	List<LStestparameter> lstestparameter;
	
	public LSlogilablimsorderdetail()
	{
		
	}
	
	public LSlogilablimsorderdetail(Long batchcode, String batchid, Integer testcode, String testname
			, Date createdtimestamp,Date completedtimestamp,Integer filetype, LSworkflow lsworkflow, String projectname, String samplename, String filename)
	{
		
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
		this.completedtimestamp=completedtimestamp;
		
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
		
		if (this.filetype == 3) {
			Batchid = "RESEARCH" + this.batchcode;
		} else if (this.filetype == 4) {
			Batchid = "EXCEL" + this.batchcode;
		} else if (this.filetype == 5) {
			Batchid = "VALIDATE" + this.batchcode;
		}
		
		return Batchid;
	}

	public void setBatchid(String batchid) {
		
		String Batchid = "ELN" + this.batchcode;
	
		if(this.filetype != null) {
		
			if (this.filetype == 3) {
				Batchid = "RESEARCH" + this.batchcode;
			} else if (this.filetype == 4) {
				Batchid = "EXCEL" + this.batchcode;
			} else if (this.filetype == 5) {
				Batchid = "VALIDATE" + this.batchcode;
			}
			
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

	public List<LSlimsorder> getLsLSlimsorder() {
		return lsLSlimsorder;
	}

	public void setLsLSlimsorder(List<LSlimsorder> lsLSlimsorder) {
		this.lsLSlimsorder = lsLSlimsorder;
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
		return testname;
	}

	public void setTestname(String testname) {
		this.testname = testname;
	}

	public String getProjectname() {
		if(projectname == null || projectname =="")
		{
			return this.lsprojectmaster != null? this.lsprojectmaster.getProjectname():"";
		}
		else
		{		
			return projectname;
		}
		
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getSamplename() {
		
		if(samplename == null || samplename =="")
		{
			return this.lssamplemaster != null ? this.lssamplemaster.getSamplename():"";
		}
		else
		{
			return samplename;
		}
	}

	public void setSamplename(String samplename) {
		this.samplename = samplename;
	}

	public int getIsLock() {
		return isLock;
	}

	public void setIsLock(int isLock) {
		this.isLock = isLock;
	}

	public int getIsLockbycurrentuser() {
		return isLockbycurrentuser;
	}

	public void setIsLockbycurrentuser(int isLockbycurrentuser) {
		this.isLockbycurrentuser = isLockbycurrentuser;
	}

	public int getIsFinalStep() {
		return isFinalStep;
	}

	public void setIsFinalStep(int isFinalStep) {
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
		if(filename == null || filename =="")
		{
			return this.lsfile != null ? this.lsfile.getFilenameuser():"";
		}
		else
		{
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

	
}
