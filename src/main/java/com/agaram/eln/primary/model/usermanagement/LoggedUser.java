package com.agaram.eln.primary.model.usermanagement;

import java.util.Date;

import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;

public class LoggedUser {
	private String sUsername;
	private String sPassword;
	private String sCategories;
	private String sSiteCode;
	private Integer UserID;
	private Integer UserGroupID;
	private String sUserGroupName;
	private String sSiteName;
	private String sUserMailID;
	private String sReason;
	private String sComments;
	private String dLoginDate;
	private Boolean bStatus;
	private String sInformation;
	
	private String sOLDPassword;

	//	private LSSiteMaster objsite;
	private Integer objsite;

	private Integer ismultitenant;
	
	private Integer multitenantusercount;
	
	public Integer getObjsite() {
		return objsite;
	}

	public void setObjsite(Integer objsite) {
		this.objsite = objsite;
	}
	private String sNewPassword;
	private String sConfirmPassword;
	private Date logindate;
	private Date passwordexpirydate;
	
	public String getsOLDPassword() {
		return sOLDPassword;
	}

	public void setsOLDPassword(String sOLDPassword) {
		this.sOLDPassword = sOLDPassword;
	}
	
	public Date getPasswordexpirydate() {
		return passwordexpirydate;
	}

	public void setPasswordexpirydate(Date passwordexpirydate) {
		this.passwordexpirydate = passwordexpirydate;
	}

	private LScfttransaction objsilentaudit;
	private LScfttransaction objmanualaudit;
	private LSuserMaster lsusermaster;
	@Transient
	private LoggedUser objuser;
	
	public LSuserMaster getLsusermaster() {
		return lsusermaster;
	}

	public void setLsusermaster(LSuserMaster lsusermaster) {
		this.lsusermaster = lsusermaster;
	}

	@Transient
	private String sUserPassword;
	
	@Transient
	private String sReasonNo;
	
	@Transient
	private String sReasonName;
	
	@Transient
	private String comments;
	
	
	
	public String getsUserPassword() {
		return sUserPassword;
	}

	public void setsUserPassword(String sUserPassword) {
		this.sUserPassword = sUserPassword;
	}

	public String getsReasonNo() {
		return sReasonNo;
	}

	public void setsReasonNo(String sReasonNo) {
		this.sReasonNo = sReasonNo;
	}

	public String getsReasonName() {
		return sReasonName;
	}

	public void setsReasonName(String sReasonName) {
		this.sReasonName = sReasonName;
	}

	

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}
	
	public String getsUsername() {
		return sUsername;
	}
	public void setsUsername(String sUsername) {
		this.sUsername = sUsername;
	}
	
	
	public String getsNewPassword() {
		return sNewPassword;
	}
	public void setsNewPassword(String sNewPassword) {
		this.sNewPassword = sNewPassword;
	}
	public String getsConfirmPassword() {
		return sConfirmPassword;
	}
	public void setsConfirmPassword(String sConfirmPassword) {
		this.sConfirmPassword = sConfirmPassword;
	}
	public String getsPassword() {
		return sPassword;
	}
	public void setsPassword(String sPassword) {
		this.sPassword = sPassword;
	}
	public String getsCategories() {
		return sCategories;
	}
	public void setsCategories(String sCategories) {
		this.sCategories = sCategories;
	}
	public String getsSiteCode() {
		return sSiteCode;
	}
	public void setsSiteCode(String sSiteCode) {
		this.sSiteCode = sSiteCode;
	}
	
	public Integer getUserID() {
		return UserID;
	}
	public void setUserID(Integer userID) {
		UserID = userID;
	}
	
	public Integer getUserGroupID() {
		return UserGroupID;
	}
	public void setUserGroupID(Integer userGroupID) {
		UserGroupID = userGroupID;
	}
	public String getsUserGroupName() {
		return sUserGroupName;
	}
	public void setsUserGroupName(String sUserGroupName) {
		this.sUserGroupName = sUserGroupName;
	}
	public String getsSiteName() {
		return sSiteName;
	}
	public void setsSiteName(String sSiteName) {
		this.sSiteName = sSiteName;
	}
	public String getsUserMailID() {
		return sUserMailID;
	}
	public void setsUserMailID(String sUserMailID) {
		this.sUserMailID = sUserMailID;
	}
	public String getsReason() {
		return sReason;
	}
	public void setsReason(String sReason) {
		this.sReason = sReason;
	}
	public String getsComments() {
		return sComments;
	}
	public void setsComments(String sComments) {
		this.sComments = sComments;
	}
	public String getdLoginDate() {
		return dLoginDate;
	}
	public void setdLoginDate(String dLoginDate) {
		this.dLoginDate = dLoginDate;
	}
	public Boolean getbStatus() {
		return bStatus;
	}
	public void setbStatus(Boolean bStatus) {
		this.bStatus = bStatus;
	}
	public String getsInformation() {
		return sInformation;
	}
	public void setsInformation(String sInformation) {
		this.sInformation = sInformation;
	}
//	public LSSiteMaster getObjsite() {
//		return objsite;
//	}
//	public void setObjsite(LSSiteMaster objsite) {
//		this.objsite = objsite;
//	}

	public Date getLogindate() {
		return logindate;
	}

	public void setLogindate(Date logindate) {
		this.logindate = logindate;
	}

	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}

	public LoggedUser getObjuser() {
		return objuser;
	}

	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
	}

	public Integer getIsmultitenant() {
		return ismultitenant;
	}

	public void setIsmultitenant(Integer ismultitenant) {
		this.ismultitenant = ismultitenant;
	}

	public Integer getMultitenantusercount() {
		return multitenantusercount;
	}

	public void setMultitenantusercount(Integer multitenantusercount) {
		this.multitenantusercount = multitenantusercount;
	}
		
	
}
