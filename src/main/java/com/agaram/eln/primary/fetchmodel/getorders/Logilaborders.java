package com.agaram.eln.primary.fetchmodel.getorders;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public class Logilaborders extends Logilabordermaster {

	private String orderflag;
	private Integer approvelstatus;
	private Integer lockeduser;
	private Integer testcode;
	private LSsamplemaster lssamplemaster;
	private LSprojectmaster lsprojectmaster;
	private Integer filecode;
	private LSuserMaster lsuserMaster;
	private Integer filetype;
	private LSsamplefile lssamplefile;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdtimestamp;
	private String repositoryitemname;
	private LSuserMaster assignedto;
	private String repositoryname;
	

	public Logilaborders(Long batchcode, String batchid, String orderflag, Integer approvelstatus,
			Integer lockeduser, Integer testcode, String testname, LSsamplemaster lssamplemaster,
			LSprojectmaster lsprojectmaster, LSfile lsfile, Integer filetype, LSuserMaster lsuserMaster,
			LSsamplefile lssamplefile, LSworkflow lsworkflow, Date createdtimestamp,Lsrepositoriesdata lsrepositoriesdata,Lsrepositories lsrepositories) {
		
		super(batchcode, batchid, lsworkflow, testname, lsfile, lssamplemaster, lsprojectmaster, filetype, orderflag);
		 
		if(lssamplefile != null)
		{
			LSsamplefile objSampleFile = new LSsamplefile();
			objSampleFile.setFilesamplecode(lssamplefile.getFilesamplecode());
			objSampleFile.setModifieddate(lssamplefile.getModifieddate());
			objSampleFile.setCreatedate(lssamplefile.getCreatedate());
			this.lssamplefile = objSampleFile;
		}
		
		this.orderflag = orderflag;
		this.approvelstatus = approvelstatus;
		this.lockeduser = lockeduser;
		this.testcode = testcode;
		this.lssamplemaster = lssamplemaster;
		this.lsprojectmaster = lsprojectmaster;
		this.filecode = lsfile != null ? lsfile.getFilecode() : -1;
		this.filetype = filetype;
		this.lsuserMaster = lsuserMaster;
		
		this.createdtimestamp = createdtimestamp;
		this.repositoryitemname =lsrepositoriesdata !=null ?lsrepositoriesdata.getRepositoryitemname():null;
		this.assignedto =lsuserMaster;
		this.repositoryname =lsrepositories !=null ?lsrepositories.getRepositoryname():null;
	}
	
	public String getRepositoryitemname() {
		return repositoryitemname;
	}

	public LSuserMaster getAssignedto() {
		return assignedto;
	}

	public String getRepositoryname() {
		return repositoryname;
	}

	public void setRepositoryitemname(String repositoryitemname) {
		this.repositoryitemname = repositoryitemname;
	}

	public void setAssignedto(LSuserMaster assignedto) {
		this.assignedto = assignedto;
	}

	public void setRepositoryname(String repositoryname) {
		this.repositoryname = repositoryname;
	}

	public LSuserMaster getLsuserMaster() {
		return lsuserMaster;
	}

	public void setLsuserMaster(LSuserMaster lsuserMaster) {
		this.lsuserMaster = lsuserMaster;
	}

	
	public Integer getFiletype() {
		return filetype;
	}

	public void setFiletype(Integer filetype) {
		this.filetype = filetype;
	}

	public LSsamplefile getLssamplefile() {
		return lssamplefile;
	}

	public void setLssamplefile(LSsamplefile lssamplefile) {
		this.lssamplefile = lssamplefile;
	}

	public Date getCreatedtimestamp() {
		return createdtimestamp;
	}

	public void setCreatedtimestamp(Date createdtimestamp) {
		this.createdtimestamp = createdtimestamp;
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

	
	
	
}