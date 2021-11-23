package com.agaram.eln.primary.model.usermanagement;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
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

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;


@Entity
@Table(name = "LSusermaster")
public class LSuserMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "usercode")
	private Integer usercode;
	
	@Column(columnDefinition = "varchar(255)")
	private String userfullname;
	@Column(columnDefinition = "varchar(255)")
	private String username;
	@Column(columnDefinition = "varchar(255)")
	private	String password;
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastloggedon;
	@Temporal(TemporalType.TIMESTAMP)
	private Date passwordexpirydate;
	@Column(columnDefinition = "varchar(10)")
	private String userstatus;
	private Integer lockcount;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createddate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifieddate;
	@Column(columnDefinition = "varchar(255)")
	private String createdby;
	@Column(columnDefinition = "varchar(255)")
	private String modifiedby;
	
	@Column(name = "passwordstatus")
	private Integer passwordstatus;
	
	@Column(name = "userretirestatus")
	private Integer userretirestatus;
	
	private Integer labsheet;
	private String emailid;
	private String profileimage;
	private String profileimagename;
	private Integer verificationcode;

	@ManyToOne
	private LSusergroup lsusergroup;
	
	@ManyToOne 
	private LSSiteMaster lssitemaster;
	
	@ManyToOne
	private LSuserActions lsuserActions;
	
	@Transient
	Response objResponse;
	
	@Transient
	private String usergroupname;
	
	@Transient
	LScfttransaction objsilentaudit;
	
	private Integer modifiedUser;
	
	@Transient
	LScfttransaction Objmanualaudit;
	
	@Transient
	String DFormat = "dd/MM/yyyy";
	
	@Transient
	private String sitename;
	
	@Transient
	private boolean sameusertologin;
	
	private String loginfrom = "0";
	
	@Transient
	private Integer ismultitenant;
	
	@Transient
	private Integer multitenantusercount;
	
	@Transient
	private LSusergroup lsusergrouptrans;
	
	@OneToMany
	@JoinColumn(name="usercode")
//	@JsonManagedReference
	private List<LSMultiusergroup> multiusergroupcode;
	
	@Transient
	private Integer multiusergroups;
	
	@Column(columnDefinition = "varchar(500)")
	private String unifieduserid;
	
	@Transient
	private Response response;
	
	@Transient
	LoggedUser objuser;
	
	@Transient
	private String token;
	
	@Transient
	private String userloginlink;
	
	@Transient
	private String encryptedpassword;
	
	@Transient
	private String sharebyunifiedid;
	
	@Transient
	private String sharetounifiedid;
	
	@Transient
	List<LSprojectmaster> lstproject;
	
	@Transient
	List<LSworkflow> lstworkflow;
	
	public String getEncryptedpassword() {
		return encryptedpassword;
	}

	public void setEncryptedpassword(String encryptedpassword) {
		this.encryptedpassword = encryptedpassword;
	}
	
	public String getUserloginlink() {
		return userloginlink;
	}

	public void setUserloginlink(String userloginlink) {
		this.userloginlink = userloginlink;
	}

	public LSusergroup getLsusergrouptrans() {
		return lsusergrouptrans;
	}

	public void setLsusergrouptrans(LSusergroup lsusergrouptrans) {
		this.lsusergrouptrans = lsusergrouptrans;
	}
	
	public String getSharebyunifiedid() {
		return sharebyunifiedid;
	}

	public void setSharebyunifiedid(String sharebyunifiedid) {
		this.sharebyunifiedid = sharebyunifiedid;
	}

	public String getSharetounifiedid() {
		return sharetounifiedid;
	}

	public void setSharetounifiedid(String sharetounifiedid) {
		this.sharetounifiedid = sharetounifiedid;
	}

	public Integer getMultiusergroups() {
		return multiusergroups;
	}

	public void setMultiusergroups(Integer multiusergroups) {
		this.multiusergroups = multiusergroups;
	}
	
	public String getDFormat() {
		return DFormat;
	}

	public void setDFormat(String dFormat) {
		DFormat = dFormat;
	}
	
	public boolean isSameusertologin() {
		return sameusertologin;
	}

	public void setSameusertologin(boolean sameusertologin) {
		this.sameusertologin = sameusertologin;
	}

	public Integer getPasswordstatus() {
		return passwordstatus;
	}

	public void setPasswordstatus(Integer passwordstatus) {
		this.passwordstatus = passwordstatus;
	}
	
	public Integer getUserretirestatus() {
		return userretirestatus;
	}

	public void setUserretirestatus(Integer userretirestatus) {
		this.userretirestatus = userretirestatus;
	}
	
	
	public LScfttransaction getObjmanualaudit() {
		return Objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		Objmanualaudit = objmanualaudit;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public LoggedUser getObjuser() {
		return objuser;
	}

	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
	}
	
	public List<LSMultiusergroup> getMultiusergroupcode() {
		return multiusergroupcode;
	}

	public void setMultiusergroupcode(List<LSMultiusergroup> multiusergroupcode) {
		this.multiusergroupcode = multiusergroupcode;
	}

	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}
	
	public Integer getUsercode() {
		return usercode;
	}
	
	public void setUsercode(Integer usercode) {
		this.usercode = usercode;
	}
	
	public String getUserfullname() {
		return userfullname;
	}
	public void setUserfullname(String userfullname) {
		this.userfullname = userfullname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getLastloggedon() {
		return lastloggedon;
	}
	public void setLastloggedon(Date lastloggedon) {
		this.lastloggedon = lastloggedon;
	}
	public Date getPasswordexpirydate() {
		return passwordexpirydate;
	}
	public void setPasswordexpirydate(Date passwordexpirydate) {
		this.passwordexpirydate = passwordexpirydate;
	}
	public String getUserstatus() {
		if(userstatus != null)
		{
			/*
			 * if(userstatus.trim().equals("Locked")) { return "Locked"; } return
			 * userstatus.trim().equals("A")?"Active":"Deactive";
			 */
//			return  userstatus.trim().equals("A")?"Active":"Deactive";
			return  userstatus.trim().equals("A")?"Active":
				userstatus.trim().equals("D")?"Deactive" : "Locked";
		}
		else
		{
			return "";
		}
	}
	public void setUserstatus(String userstatus) {
		this.userstatus = userstatus;
	}
	public Integer getLockcount() {
		return lockcount;
	}
	public void setLockcount(Integer lockcount) {
		this.lockcount = lockcount;
	}
	public Date getCreateddate() {
		return createddate;
	}
	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}
	public Date getModifieddate() {
		return modifieddate;
	}
	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public String getModifiedby() {
		return modifiedby;
	}
	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}
	public Integer getLabsheet() {
		return labsheet;
	}
	public void setLabsheet(Integer labsheet) {
		this.labsheet = labsheet;
	}
	public String getEmailid() {
		return emailid;
	}
	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}
	public String getProfileimage() {
		return profileimage;
	}
	public void setProfileimage(String profileimage) {
		this.profileimage = profileimage;
	}
	public String getProfileimagename() {
		return profileimagename;
	}
	public void setProfileimagename(String profileimagename) {
		this.profileimagename = profileimagename;
	}
	public Integer getVerificationcode() {
		return verificationcode;
	}
	public void setVerificationcode(Integer verificationcode) {
		this.verificationcode = verificationcode;
	}
	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}
	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}

	public LSusergroup getLsusergroup() {
		return lsusergrouptrans;
	}

	public void setLsusergroup(LSusergroup lsusergroup) {
		this.lsusergrouptrans = lsusergroup;
	}

	public Response getObjResponse() {
		return objResponse;
	}

	public void setObjResponse(Response objResponse) {
		this.objResponse = objResponse;
	}

	public String getUsergroupname() {
		if(this.lsusergroup != null)
		{
			return this.lsusergroup.getUsergroupname();
		}
		else
		{
			return "";
		}
	}

	public void setUsergroupname(String usergroupname) {
		this.usergroupname = usergroupname;
	}

	public String getSitename() {
		if(this.lssitemaster != null)
		{
			return this.lssitemaster.getSitename();
		}
		else
		{
			return "";
		}
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public Integer getModifiedUser() {
		return modifiedUser;
	}

	public void setModifiedUser(Integer modifiedUser) {
		this.modifiedUser = modifiedUser;
	}

	public LSuserActions getLsuserActions() {
		return lsuserActions;
	}

	public void setLsuserActions(LSuserActions lsuserActions) {
		this.lsuserActions = lsuserActions;
	}

	public String getLoginfrom() {
		return loginfrom;
	}

	public void setLoginfrom(String loginfrom) {
		this.loginfrom = loginfrom;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public String getUnifieduserid() {
		return unifieduserid;
	}

	public void setUnifieduserid(String unifieduserid) {
		this.unifieduserid = unifieduserid;
	}

	public List<LSprojectmaster> getLstproject() {
		return lstproject;
	}

	public void setLstproject(List<LSprojectmaster> lstproject) {
		this.lstproject = lstproject;
	}

	public List<LSworkflow> getLstworkflow() {
		return lstworkflow;
	}

	public void setLstworkflow(List<LSworkflow> lstworkflow) {
		this.lstworkflow = lstworkflow;
	}
	
	
}
