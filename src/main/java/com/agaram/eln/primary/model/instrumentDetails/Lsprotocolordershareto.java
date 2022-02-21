package com.agaram.eln.primary.model.instrumentDetails;

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
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity(name = "Lsprotocolordershareto")
@Table(name = "Lsprotocolordershareto")
public class Lsprotocolordershareto {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "sharetoprotocolordercode")
	private Long sharetoprotocolordercode;

	@Column(columnDefinition = "varchar(250)")
	private String sharebyunifiedid;
	@Column(columnDefinition = "varchar(250)")
	private String sharetounifiedid;

	@Column(columnDefinition = "varchar(250)")
	private String sharebyusername;

	@Column(columnDefinition = "varchar(250)")
	private String sharetousername;

	private Long shareprotocolordercode;
	@Column(columnDefinition = "varchar(250)")
	private String shareprotoclordername;

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private String shareitemdetails;

	private Date sharedon;

	private Date unsharedon;
	private int sharerights = 0;

	private int sharestatus = 0;

	@Column(name = "Protocoltype")
	private Integer protocoltype;

	@Transient
	LSuserMaster objLoggeduser;

	@Transient
	LScfttransaction objsilentaudit;

	@Transient
	LScfttransaction objmanualaudit;

	@Transient
	private Integer ismultitenant;

	@Transient
	private LSlogilabprotocoldetail lslogilabprotocoldetail;

	@Transient
	private Long sharedbytoprotocolordercode;

	@Transient
	private Date fromdate;

	@Transient
	private Date todate;

	@Transient
	private String orderflag;

	public Date getFromdate() {
		return fromdate;
	}

	public Date getTodate() {
		return todate;
	}

	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}

	public void setTodate(Date todate) {
		this.todate = todate;
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

	public LSlogilabprotocoldetail getLslogilabprotocoldetail() {
		return lslogilabprotocoldetail;
	}

	public Long getSharedbytoprotocolordercode() {
		return sharedbytoprotocolordercode;
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

	public void setLslogilabprotocoldetail(LSlogilabprotocoldetail lslogilabprotocoldetail) {
		this.lslogilabprotocoldetail = lslogilabprotocoldetail;
	}

	public void setSharedbytoprotocolordercode(Long sharedbytoprotocolordercode) {
		this.sharedbytoprotocolordercode = sharedbytoprotocolordercode;
	}

	public Long getSharetoprotocolordercode() {
		return sharetoprotocolordercode;
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

	public int getSharerights() {
		return sharerights;
	}

	public int getSharestatus() {
		return sharestatus;
	}

	public Integer getProtocoltype() {
		return protocoltype;
	}

	public void setSharetoprotocolordercode(Long sharetoprotocolordercode) {
		this.sharetoprotocolordercode = sharetoprotocolordercode;
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

	public Long getShareprotocolordercode() {
		return shareprotocolordercode;
	}

	public String getShareprotoclordername() {
		return shareprotoclordername;
	}

	public void setShareprotocolordercode(Long shareprotocolordercode) {
		this.shareprotocolordercode = shareprotocolordercode;
	}

	public void setShareprotoclordername(String shareprotoclordername) {
		this.shareprotoclordername = shareprotoclordername;
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

	public void setProtocoltype(Integer protocoltype) {
		this.protocoltype = protocoltype;
	}

	public String getOrderflag() {
		return orderflag;
	}

	public void setOrderflag(String orderflag) {
		this.orderflag = orderflag;
	}

}
