package com.agaram.eln.primary.model.getsheetdetails;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public class Sheettemplateget {

	private Long batchcode;
	private String batchid;
	private String orderflag;
	private Integer approvelstatus;
	private Integer lockeduser;
	private Integer testcode;
	private String testname;
	private LSsamplemaster lssamplemaster;
	private LSprojectmaster lsprojectmaster;
	private LSuserMaster lsuserMaster;
	private Integer filecode;
	private String filename;
	private String projectname;
	private String samplename;
	private Integer filetype;
	private LSsamplefile lssamplefile;
	private LSworkflow lsworkflow;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdtimestamp;

	public Sheettemplateget(Long batchcode, String batchid, String orderflag, Integer approvelstatus,
			Integer lockeduser, Integer testcode, String testname, LSsamplemaster lssamplemaster,
			LSprojectmaster lsprojectmaster, LSfile lsfile, Integer filetype, LSuserMaster lsuserMaster,
			LSsamplefile lssamplefile, LSworkflow lsworkflow, Date createdtimestamp) {

		LSsamplefile lssamplefile1 = new LSsamplefile();
		lssamplefile1.setModifieddate(lssamplefile.getModifieddate());
		lssamplefile1.setCreatedate(lssamplefile.getCreatedate());

		LSworkflow lsworkflow1 = new LSworkflow();
		lsworkflow1.setWorkflowcode(lsworkflow.getWorkflowcode());
		lsworkflow1.setWorkflowname(lsworkflow.getWorkflowname());

		this.batchcode = batchcode;
		this.batchid = batchid;
		this.orderflag = orderflag;
		this.approvelstatus = approvelstatus;
		this.lockeduser = lockeduser;
		this.testcode = testcode;
		this.testname = testname;
		this.lssamplemaster = lssamplemaster;
		this.lsprojectmaster = lsprojectmaster;
		this.filecode = lsfile != null ? lsfile.getFilecode() : -1;
		this.filename = lsfile != null ? lsfile.getFilenameuser() : null;
		this.projectname = lsprojectmaster != null ? lsprojectmaster.getProjectname() : null;
		this.samplename = lssamplemaster != null ? lssamplemaster.getSamplename() : null;
		this.filetype = filetype;
		this.lsuserMaster = lsuserMaster;
		this.lssamplefile = lssamplefile1;
		this.lsworkflow = lsworkflow1;
		this.createdtimestamp = createdtimestamp;
	}
	
	public Date getCreatedtimestamp() {
		return createdtimestamp;
	}

	public void setCreatedtimestamp(Date createdtimestamp) {
		this.createdtimestamp = createdtimestamp;
	}

	public LSworkflow getLsworkflow() {
		return lsworkflow;
	}

	public void setLsworkflow(LSworkflow lsworkflow) {
		this.lsworkflow = lsworkflow;
	}

	public LSuserMaster getLsuserMaster() {
		return lsuserMaster;
	}

	public void setLsuserMaster(LSuserMaster lsuserMaster) {
		this.lsuserMaster = lsuserMaster;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getSamplename() {
		return samplename;
	}

	public void setSamplename(String samplename) {
		this.samplename = samplename;
	}

	public LSsamplefile getLssamplefile() {
		return lssamplefile;
	}

	public void setLssamplefile(LSsamplefile lssamplefile) {
		this.lssamplefile = lssamplefile;
	}
	
	public Long getBatchcode() {
		return batchcode;
	}
	public void setBatchcode(Long batchcode) {
		this.batchcode = batchcode;
	}
	public String getBatchid() {
		return batchid;
	}
	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}

	public String getOrderflag() {
		return orderflag;
	}

	public void setOrderflag(String orderflag) {
		this.orderflag = orderflag;
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

	public Integer getTestcode() {
		return testcode;
	}

	public void setTestcode(Integer testcode) {
		this.testcode = testcode;
	}

	public String getTestname() {
		return testname;
	}

	public void setTestname(String testname) {
		this.testname = testname;
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

	public Integer getFilecode() {
		return filecode;
	}

	public void setFilecode(Integer filecode) {
		this.filecode = filecode;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Integer getFiletype() {
		return filetype;
	}

	public void setFiletype(Integer filetype) {
		this.filetype = filetype;
	}	
}
