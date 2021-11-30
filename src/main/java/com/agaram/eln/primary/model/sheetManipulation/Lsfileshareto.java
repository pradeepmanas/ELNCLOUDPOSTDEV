package com.agaram.eln.primary.model.sheetManipulation;

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
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@SuppressWarnings("unused")
@Entity(name = "Lsfileshareto")
@Table(name = "Lsfileshareto")
public class Lsfileshareto {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "sharetofilecode")
	private Long sharetofilecode;

	@Column(columnDefinition = "varchar(250)")
	private String sharebyunifiedid;
	
	@Column(columnDefinition = "varchar(250)")
	private String sharetounifiedid;

	@Column(columnDefinition = "varchar(250)")
	private String sharebyusername;

	@Column(columnDefinition = "varchar(250)")
	private String sharetousername;

	private Long sharefilecode;

	@Column(columnDefinition = "varchar(250)")
	private String sharefilename;

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private String shareitemdetails;

	private Date sharedon;

	private Date unsharedon;

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

	@Transient
	private Long sharedbytofilecode;

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

	public int getSharerights() {
		return sharerights;
	}

	public int getSharestatus() {
		return sharestatus;
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

	public void setSharerights(int sharerights) {
		this.sharerights = sharerights;
	}

	public void setSharestatus(int sharestatus) {
		this.sharestatus = sharestatus;
	}

	public LSuserMaster getObjLoggeduser() {
		return objLoggeduser;
	}

	public void setObjLoggeduser(LSuserMaster objLoggeduser) {
		this.objLoggeduser = objLoggeduser;
	}

	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}

	public Integer getIsmultitenant() {
		return ismultitenant;
	}

	public void setIsmultitenant(Integer ismultitenant) {
		this.ismultitenant = ismultitenant;
	}

	public Long getSharetofilecode() {
		return sharetofilecode;
	}

	public void setSharetofilecode(Long sharetofilecode) {
		this.sharetofilecode = sharetofilecode;
	}

	public Long getSharefilecode() {
		return sharefilecode;
	}

	public void setSharefilecode(Long sharefilecode) {
		this.sharefilecode = sharefilecode;
	}

	public String getSharefilename() {
		return sharefilename;
	}

	public void setSharefilename(String sharefilename) {
		this.sharefilename = sharefilename;
	}

	public Long getSharedbytofilecode() {
		return sharedbytofilecode;
	}

	public void setSharedbytofilecode(Long sharedbytofilecode) {
		this.sharedbytofilecode = sharedbytofilecode;
	}
}