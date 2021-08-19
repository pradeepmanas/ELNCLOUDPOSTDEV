package com.agaram.eln.primary.model.getorders;

import javax.persistence.Column;

import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;

public class Logilaborders {

	private Long batchcode;
	private String batchid;
	private String orderflag;
	private Integer approvelstatus;
	private Integer lockeduser;
	private Integer testcode;
	private String testname;
	private LSsamplemaster lssamplemaster;
	private LSprojectmaster lsprojectmaster;
	private LSfile lsfile;
	
	public Logilaborders(Long batchcode, String batchid, String orderflag,
			Integer approvelstatus, Integer lockeduser, Integer testcode, String testname,
			LSsamplemaster lssamplemaster, LSprojectmaster lsprojectmaster,LSfile lsfile)
	{
		this.batchcode = batchcode;
		this.batchid = batchid;
		this.orderflag = orderflag;
		this.approvelstatus= approvelstatus;
		this.lockeduser= lockeduser;
		this.testcode= testcode; 
		this.testname = testname;
		this.lssamplemaster = lssamplemaster;
		this.lsprojectmaster = lsprojectmaster;
		this.lsfile = lsfile;
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

	public LSfile getLsfile() {
		return lsfile;
	}

	public void setLsfile(LSfile lsfile) {
		this.lsfile = new LSfile(lsfile.getFilecode(), lsfile.getFilenameuser());
	}
	
	
}
