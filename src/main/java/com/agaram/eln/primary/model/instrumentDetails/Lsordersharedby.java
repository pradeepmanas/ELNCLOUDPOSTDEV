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
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity(name = "Lsordersharedby")
@Table(name = "Lsordersharedby")
public class Lsordersharedby {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "sharedbycode")
	private Long sharedbycode;
	
	@Column(name = "sharetocode")
	private Long sharetocode;
	
	@Column(columnDefinition = "varchar(250)")
	private String sharebyunifiedid;
	
	@Column(columnDefinition = "varchar(250)")
	private String sharetounifiedid;
	
	@Column(columnDefinition = "varchar(250)")
	private String sharebyusername;
	
	@Column(columnDefinition = "varchar(250)")
	private String sharetousername;
	
	private Long sharebatchcode;
	@Column(columnDefinition = "varchar(250)")
	private String sharebatchid;
	
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	private String shareitemdetails;
	
	private Date sharedon;
	
	private Date unsharedon;
	
	private Date sharemodifiedon;
	
	private int sharerights =0;
	
	private int sharestatus =0;
	
	private int ordertype =0;
	
	@Transient
	LSuserMaster objLoggeduser;
	
	@Transient
	LScfttransaction objsilentaudit;
	
	@Transient
	LScfttransaction objmanualaudit;
	
	@Transient
	private Integer ismultitenant;
	
	@Transient
	private LSlogilablimsorderdetail objorder;

	public Long getSharedbycode() {
		return sharedbycode;
	}

	public void setSharedbycode(Long sharedbycode) {
		this.sharedbycode = sharedbycode;
	}

	public Long getSharetocode() {
		return sharetocode;
	}

	public void setSharetocode(Long sharetocode) {
		this.sharetocode = sharetocode;
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

	public Long getSharebatchcode() {
		return sharebatchcode;
	}

	public void setSharebatchcode(Long sharebatchcode) {
		this.sharebatchcode = sharebatchcode;
	}

	public String getSharebatchid() {
		return sharebatchid;
	}

	public void setSharebatchid(String sharebatchid) {
		this.sharebatchid = sharebatchid;
	}

	public String getShareitemdetails() {
		return shareitemdetails;
	}

	public void setShareitemdetails(String shareitemdetails) {
		this.shareitemdetails = shareitemdetails;
	}

	public int getSharerights() {
		return sharerights;
	}

	public void setSharerights(int sharerights) {
		this.sharerights = sharerights;
	}

	public int getSharestatus() {
		return sharestatus;
	}

	public void setSharestatus(int sharestatus) {
		this.sharestatus = sharestatus;
	}

	public int getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(int ordertype) {
		this.ordertype = ordertype;
	}

	public String getSharebyusername() {
		return sharebyusername;
	}

	public void setSharebyusername(String sharebyusername) {
		this.sharebyusername = sharebyusername;
	}

	public String getSharetousername() {
		return sharetousername;
	}

	public void setSharetousername(String sharetousername) {
		this.sharetousername = sharetousername;
	}

	public Date getSharedon() {
		return sharedon;
	}

	public void setSharedon(Date sharedon) {
		this.sharedon = sharedon;
	}

	public Date getUnsharedon() {
		return unsharedon;
	}

	public void setUnsharedon(Date unsharedon) {
		this.unsharedon = unsharedon;
	}

	public Date getSharemodifiedon() {
		return sharemodifiedon;
	}

	public void setSharemodifiedon(Date sharemodifiedon) {
		this.sharemodifiedon = sharemodifiedon;
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

	public LSlogilablimsorderdetail getObjorder() {
		return objorder;
	}

	public void setObjorder(LSlogilablimsorderdetail objorder) {
		this.objorder = objorder;
	}
	
}
