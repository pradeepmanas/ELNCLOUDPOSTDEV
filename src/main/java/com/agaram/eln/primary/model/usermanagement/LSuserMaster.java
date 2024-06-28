package com.agaram.eln.primary.model.usermanagement;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import com.agaram.eln.primary.model.protocols.Elnprotocolworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;

@Entity
@Table(name = "LSusermaster")
public class LSuserMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "usercode")
	private Integer usercode;
	@Transient
	private LSuserMaster lsusermaster;
	@Column(columnDefinition = "varchar(255)")
	private String userfullname;
	@Column(columnDefinition = "varchar(255)")
	private String username;
	@Column(columnDefinition = "varchar(255)")
	private String password;
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
	private String subcode;
	private String picture;
	private Integer autenticatefrom;
	@Transient
	private List<LSuserMaster> usernotify;
	@Transient
	private List<LSuserMaster> userroleremovenotify;

	@Transient
	private Integer activeusercode;
	
	public Integer getActiveusercode() {
		return activeusercode;
	}

	public void setActiveusercode(Integer activeusercode) {
		this.activeusercode = activeusercode;
	}

	@Transient
	private LSuserMaster loggedinuser;
	
	public LSuserMaster() {

	}

	public LSuserMaster(Integer usercode, String username, LSSiteMaster lssitemaster) {
		this.usercode = usercode;
		this.username = username;
		this.lssitemaster = lssitemaster;
	}


	public List<LSuserMaster> getUsernotify() {
		return usernotify;
	}

	public void setUsernotify(List<LSuserMaster> usernotify) {
		this.usernotify = usernotify;
	}

	public LSuserMaster getLoggedinuser() {
		return loggedinuser;
	}

	public void setLoggedinuser(LSuserMaster loggedinuser) {
		this.loggedinuser = loggedinuser;
	}

	@Column(name = "passwordstatus")
	private Integer passwordstatus;

	@Column(name = "userretirestatus")
	private Integer userretirestatus;

	private Integer labsheet;
	private String emailid;
	private String designationname;
	private String profileimage;
	private String profileimagename;
	private Integer verificationcode;
	private Integer isadsuser = 0;

	public Integer getIsadsuser() {
		return isadsuser;
	}

	public void setIsadsuser(Integer isadsuser) {
		this.isadsuser = isadsuser;
	}
	
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

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "usercode")
//	@JsonManagedReference
	private List<LSMultiusergroup> multiusergroupcode;
	
	@OneToMany
	@JoinColumn(name = "usercode")
//	@JsonManagedReference
	private List<LSMultisites> lsmultisites;

	public List<LSMultisites> getLsmultisites() {
		return lsmultisites;
	}

	public void setLsmultisites(List<LSMultisites> lsmultisites) {
		this.lsmultisites = lsmultisites;
	}

	@Transient
	private Integer multiusergroups;
	
	@Transient
	private Integer pagesize;
	
	@Transient
	private Integer pageperorder;

	public Integer getPagesize() {
		return pagesize;
	}

	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}

	public Integer getPageperorder() {
		return pageperorder;
	}

	public void setPageperorder(Integer pageperorder) {
		this.pageperorder = pageperorder;
	}

	public Integer getIdletime() {
		return idletime;
	}

	public void setIdletime(Integer idletime) {
		this.idletime = idletime;
	}

	@Transient
	private List<LSMultiusergroup> deleterole;

	public List<LSMultiusergroup> getDeleterole() {
		return deleterole;
	}

	public void setDeleterole(List<LSMultiusergroup> deleterole) {
		this.deleterole = deleterole;
	}

	@Transient
	private Integer idletime;
	@Transient
	private Integer idletimeshowcheck;

	public Integer getIdletimeshowcheck() {
		return idletimeshowcheck;
	}

	public void setIdletimeshowcheck(Integer idletimeshowcheck) {
		this.idletimeshowcheck = idletimeshowcheck;
	}

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
	private Integer testcode;
	
	@Transient
	LSprojectmaster lstprojectforfilter;


	@Transient
	List<LSworkflow> lstworkflow;
	
	@Transient
	List<Elnprotocolworkflow> lstelnprotocolworkflow;
	
	@Transient
	LSSiteMaster[] lstiteMaster;

	@Transient
	private boolean reset;
	@Transient
	private boolean resendmail;

	public LSSiteMaster[] getLstiteMaster() {
		return lstiteMaster;
	}

	public void setLstiteMaster(LSSiteMaster[] lstiteMaster) {
		this.lstiteMaster = lstiteMaster;
	}

	public LSprojectmaster getLstprojectforfilter() {
		return lstprojectforfilter;
	}

	public void setLstprojectforfilter(LSprojectmaster lstprojectforfilter) {
		this.lstprojectforfilter = lstprojectforfilter;
	}

	public Integer getTestcode() {
		return testcode;
	}

	public void setTestcode(Integer testcode) {
		this.testcode = testcode;
	}

	public boolean isResendmail() {
		return resendmail;
	}

	public void setResendmail(boolean resendmail) {
		this.resendmail = resendmail;
	}

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

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
		if (userstatus != null) {
			/*
			 * if(userstatus.trim().equals("Locked")) { return "Locked"; } return
			 * userstatus.trim().equals("A")?"Active":"Deactive";
			 */
//			return  userstatus.trim().equals("A")?"Active":"Deactive";
			
			return this.userretirestatus !=null &&this.userretirestatus == 1?"Retired":userstatus.trim().equals("A") || userstatus.trim().equals("Active") ? "Active" : userstatus.trim().equals("D") || userstatus.trim().equals("Inactive") ? "Deactive" : "Locked";
//			return userstatus.trim().equals("A") && this.userretirestatus == 0 ? "Active" : userstatus.trim().equals("D") && this.userretirestatus == 0 ? "Deactive" :this.userretirestatus == 1?"Retired": "Locked";
		} else {
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
		return usergroupname;
	}

	public void setUsergroupname(String usergroupname) {
		this.usergroupname = usergroupname;
	}

	public String getSitename() {
		if (this.lssitemaster != null) {
			return this.lssitemaster.getSitename();
		} else {
			return "";
		}
		
//		if(this.lsmultisites.size()>0) {
//			return this.lsmultisites.get(0).getLssiteMaster().getSitename();
//		}else {
//			return "";
//		}
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
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

	public List<Elnprotocolworkflow> getLstelnprotocolworkflow() {
		return lstelnprotocolworkflow;
	}

	public void setLstelnprotocolworkflow(List<Elnprotocolworkflow> lstelnprotocolworkflow) {
		this.lstelnprotocolworkflow = lstelnprotocolworkflow;
	}

	public List<LSuserMaster> getUserroleremovenotify() {
		return userroleremovenotify;
	}

	public void setUserroleremovenotify(List<LSuserMaster> userroleremovenotify) {
		this.userroleremovenotify = userroleremovenotify;
	}

	public LSuserMaster getLsusermaster() {
		return lsusermaster;
	}

	public void setLsusermaster(LSuserMaster lsusermaster) {
		this.lsusermaster = lsusermaster;
	}

	public String getSubcode() {
		return subcode;
	}

	public void setSubcode(String subcode) {
		this.subcode = subcode;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Integer getAutenticatefrom() {
		return autenticatefrom;
	}

	public void setAutenticatefrom(Integer autenticatefrom) {
		this.autenticatefrom = autenticatefrom;
	}

	public String getDesignationname() {
		return designationname;
	}

	public void setDesignationname(String designationname) {
		this.designationname = designationname;
	}

	
	}


