package com.agaram.eln.primary.model.getsheetdetails;

import java.util.Date;

import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public class Sheettemplateget {

	private Integer filecode;
	private String filenameuser;
	private Integer approved;
	private Integer rejected;
	private Date createdate;

	private LSuserMaster createby;
	private LSuserMaster modifiedby;
	
	private long versioncout;
	
//	List<LSfileversion> lsfileversion;

	public Sheettemplateget(Integer filecode, String filenameuser, Date createdate, LSuserMaster createby,
			LSuserMaster modifiedby, Integer approved, Integer rejected) {
		
		LSuserMaster createdUser = new LSuserMaster();
		
		if(createby!=null) {
			createdUser.setUsercode(createby.getUsercode());
			createdUser.setUsername(createby.getUsername());	
		}
		
		LSuserMaster modifiedUser = new LSuserMaster();
		if(modifiedby != null) {
			modifiedUser.setUsercode(modifiedby.getUsercode());
			modifiedUser.setUsername(modifiedby.getUsername());	
		}
		
		
		this.filecode = filecode;
		this.filenameuser = filenameuser;
		this.approved = approved;
		this.rejected = rejected;
		this.createby = createdUser;
		this.createdate = createdate;
		this.modifiedby = modifiedUser;

	}

//	public List<LSfileversion> getLsfileversion() {
//		return lsfileversion;
//	}
//
//	public void setLsfileversion(List<LSfileversion> lsfileversion) {
//		this.lsfileversion = lsfileversion;
//	}

	public long getVersioncout() {
		return versioncout;
	}

	public void setVersioncout(long versioncout) {
		this.versioncout = versioncout;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
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

	public Integer getFilecode() {
		return filecode;
	}

	public void setFilecode(Integer filecode) {
		this.filecode = filecode;
	}

	public String getFilenameuser() {
		return filenameuser;
	}

	public void setFilenameuser(String filenameuser) {
		this.filenameuser = filenameuser;
	}
}