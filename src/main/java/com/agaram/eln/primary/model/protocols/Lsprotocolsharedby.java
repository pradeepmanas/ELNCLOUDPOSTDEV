package com.agaram.eln.primary.model.protocols;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity(name = "Lsprotocolsharedby")
@Table(name = "Lsprotocolsharedby")
public class Lsprotocolsharedby {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "sharedbytoprotocolcode")
	private Long sharedbytoprotocolcode;

	@Column(name = "sharetoprotocolcode")
	private Long sharetoprotocolcode;

	@Column(columnDefinition = "varchar(250)")
	private String sharebyunifiedid;

	@Column(columnDefinition = "varchar(250)")
	private String sharetounifiedid;

	@Column(columnDefinition = "varchar(250)")
	private String sharebyusername;

	@Column(columnDefinition = "varchar(250)")
	private String sharetousername;

	private Long shareprotocolcode;

	@Column(columnDefinition = "varchar(250)")
	private String shareprotocolname;

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private String shareitemdetails;

	private Date sharedon;

	private Date unsharedon;

	private Date sharemodifiedon;

	private int sharerights = 0;

	private int sharestatus = 0;

	@Transient
	LSuserMaster objLoggeduser;

	@Transient
	LScfttransaction objsilentaudit;

	@Transient
	LScfttransaction objmanualaudit;

	@Transient
	private Integer ismultitenant;
	
	public Long getSharedbytoprotocolcode() {
		return sharedbytoprotocolcode;
	}

	public void setSharedbytoprotocolcode(Long sharedbytoprotocolcode) {
		this.sharedbytoprotocolcode = sharedbytoprotocolcode;
	}

	public Long getSharetoprotocolcode() {
		return sharetoprotocolcode;
	}

	public void setSharetoprotocolcode(Long sharetoprotocolcode) {
		this.sharetoprotocolcode = sharetoprotocolcode;
	}

	public Long getShareprotocolcode() {
		return shareprotocolcode;
	}

	public void setShareprotocolcode(Long shareprotocolcode) {
		this.shareprotocolcode = shareprotocolcode;
	}

	public String getShareprotocolname() {
		return shareprotocolname;
	}

	public void setShareprotocolname(String shareprotocolname) {
		this.shareprotocolname = shareprotocolname;
	}

	public String getSharebyunifiedid() {
		return sharebyunifiedid;
	}

	public String getSharetounifiedid() {
		return sharetounifiedid;
	}

	public String getSharebyusername() {
		return sharebyusername;
	}

	public String getSharetousername() {
		return sharetousername;
	}

	public String getShareitemdetails() {
		return shareitemdetails;
	}

	public Date getSharedon() {
		return sharedon;
	}

	public Date getUnsharedon() {
		return unsharedon;
	}

	public Date getSharemodifiedon() {
		return sharemodifiedon;
	}

	public int getSharerights() {
		return sharerights;
	}

	public int getSharestatus() {
		return sharestatus;
	}

	public LSuserMaster getObjLoggeduser() {
		return objLoggeduser;
	}

	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}

	public Integer getIsmultitenant() {
		return ismultitenant;
	}

	public void setSharebyunifiedid(String sharebyunifiedid) {
		this.sharebyunifiedid = sharebyunifiedid;
	}

	public void setSharetounifiedid(String sharetounifiedid) {
		this.sharetounifiedid = sharetounifiedid;
	}

	public void setSharebyusername(String sharebyusername) {
		this.sharebyusername = sharebyusername;
	}

	public void setSharetousername(String sharetousername) {
		this.sharetousername = sharetousername;
	}

	public void setShareitemdetails(String shareitemdetails) {
		this.shareitemdetails = shareitemdetails;
	}

	public void setSharedon(Date sharedon) {
		this.sharedon = sharedon;
	}

	public void setUnsharedon(Date unsharedon) {
		this.unsharedon = unsharedon;
	}

	public void setSharemodifiedon(Date sharemodifiedon) {
		this.sharemodifiedon = sharemodifiedon;
	}

	public void setSharerights(int sharerights) {
		this.sharerights = sharerights;
	}

	public void setSharestatus(int sharestatus) {
		this.sharestatus = sharestatus;
	}

	public void setObjLoggeduser(LSuserMaster objLoggeduser) {
		this.objLoggeduser = objLoggeduser;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}

	public void setIsmultitenant(Integer ismultitenant) {
		this.ismultitenant = ismultitenant;
	}
}