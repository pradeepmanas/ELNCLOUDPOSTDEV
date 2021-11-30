package com.agaram.eln.primary.model.protocols;

import java.util.Date;
import java.util.List;

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

import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "LSprotocolmaster")
public class LSprotocolmaster implements Comparable<LSprotocolmaster> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer protocolmastercode;
	@Column(columnDefinition = "varchar(120)")
	public String protocolmastername;
	public Integer protocolstatus;
	public Integer status;
	public Integer createdby;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;

	public Integer sharewithteam = 0;
	@Column(columnDefinition = "varchar(120)")
	public String createdbyusername;
	@Column(name = "lssitemaster_sitecode")
	private Integer lssitemaster;

	@Transient
	private String createdateprotocol;

	@ManyToOne
	private LSprotocolworkflow lSprotocolworkflow;

	private Integer approved;

	private Integer rejected;

	public Integer versionno = 0;

	@Transient
	private List<LSprotocolworkflowhistory> lsprotocolworkflowhistory;

	@Transient
	private List<LSprotocolversion> lsprotocolversion;

	@Transient
	LSuserMaster LSuserMaster;

	@OneToMany
	@JoinColumn(name = "protocolmastercode")
	private List<LSprotocolmastertest> lstest;

	public Integer getRejected() {
		return rejected;
	}

	public void setRejected(Integer rejected) {
		this.rejected = rejected;
	}

	public Integer getApproved() {
		return approved;
	}

	public void setApproved(Integer approved) {
		this.approved = approved;
	}

	public LSprotocolworkflow getlSprotocolworkflow() {
		return lSprotocolworkflow;
	}

	public void setlSprotocolworkflow(LSprotocolworkflow lSprotocolworkflow) {
		this.lSprotocolworkflow = lSprotocolworkflow;
	}

	public Integer getLssitemaster() {
		return lssitemaster;
	}

	public void setLssitemaster(Integer lssitemaster) {
		this.lssitemaster = lssitemaster;
	}

	public Integer getProtocolmastercode() {
		return protocolmastercode;
	}

	public void setProtocolmastercode(Integer protocolmastercode) {
		this.protocolmastercode = protocolmastercode;
	}

	public String getProtocolmastername() {
		return protocolmastername;
	}

	public void setProtocolmastername(String protocolmastername) {
		this.protocolmastername = protocolmastername;
	}

	public Integer getProtocolstatus() {
		return protocolstatus;
	}

	public void setProtocolstatus(Integer protocolstatus) {
		this.protocolstatus = protocolstatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	@Transient
	private Integer ismultitenant;

	public Integer getIsmultitenant() {
		return ismultitenant;
	}

	public void setIsmultitenant(Integer ismultitenant) {
		this.ismultitenant = ismultitenant;
	}

	// public LSSiteMaster getLssitemaster() {
//		return lssitemaster;
//	}
//	public void setLssitemaster(LSSiteMaster lssitemaster) {
//		this.lssitemaster = lssitemaster;
//	}
	public Integer getCreatedby() {
		return createdby;
	}

	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}

	public Integer getSharewithteam() {
		return sharewithteam;
	}

	public void setSharewithteam(Integer sharewithteam) {
		this.sharewithteam = sharewithteam;
	}

	public String getCreatedbyusername() {
		return createdbyusername;
	}

	public void setCreatedbyusername(String createdbyusername) {
		this.createdbyusername = createdbyusername;
	}

	public String getCreatedateprotocol() {
		return createdateprotocol;
	}

	public void setCreatedateprotocol(String createdateprotocol) {
		this.createdateprotocol = createdateprotocol;
	}

	public List<LSprotocolworkflowhistory> getLsprotocolworkflowhistory() {
		return lsprotocolworkflowhistory;
	}

	public void setLsprotocolworkflowhistory(List<LSprotocolworkflowhistory> lsprotocolworkflowhistory) {
		this.lsprotocolworkflowhistory = lsprotocolworkflowhistory;
	}

	public List<LSprotocolversion> getLsprotocolversion() {
		return lsprotocolversion;
	}

	public void setLsprotocolversion(List<LSprotocolversion> lsprotocolversion) {
		this.lsprotocolversion = lsprotocolversion;
	}

	public Integer getVersionno() {
		return versionno;
	}

	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}

	@Override
	public String toString() {
		return "LSprotocolmaster [protocolmastercode=" + protocolmastercode + ", protocolmastername="
				+ protocolmastername + ", protocolstatus=" + protocolstatus + ", status=" + status + ", createdby="
				+ createdby + ", createdate=" + createdate + ", sharewithteam=" + sharewithteam + ", createdbyusername="
				+ createdbyusername + ", lssitemaster=" + lssitemaster + ", createdateprotocol=" + createdateprotocol
				+ ", lSprotocolworkflow=" + lSprotocolworkflow + ", approved=" + approved + ", rejected=" + rejected
				+ ", versionno=" + versionno + ", lsprotocolworkflowhistory=" + lsprotocolworkflowhistory
				+ ", lsprotocolversion=" + lsprotocolversion + ", ismultitenant=" + ismultitenant + "]";
	}

	@Override
	public int compareTo(LSprotocolmaster o) {
		return this.getProtocolmastercode().compareTo(o.getProtocolmastercode());
	}

	public LSuserMaster getLSuserMaster() {
		return LSuserMaster;
	}

	public void setLSuserMaster(LSuserMaster lSuserMaster) {
		LSuserMaster = lSuserMaster;
	}

	public List<LSprotocolmastertest> getLstest() {
		return lstest;
	}

	public void setLstest(List<LSprotocolmastertest> lstest) {
		this.lstest = lstest;
	}

}
