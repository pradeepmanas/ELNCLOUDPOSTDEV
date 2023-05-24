package com.agaram.eln.primary.model.protocols;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="LSprotocolversion")
public class LSprotocolversion implements Comparable<LSprotocolversion>{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "protocolversioncode")
	private Integer protocolversioncode;
	public Integer protocolmastercode;
	@Column(columnDefinition = "varchar(120)")
	public String protocolmastername;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
	public Integer sharewithteam = 0;
	@Column(columnDefinition = "varchar(120)")
	public String createdbyusername;

	private Integer approved;
	
	private Integer rejected;
	public Integer protocolstatus;
	public Integer createdby;
	@Column(name="lssitemaster_sitecode")
	private Integer lssitemaster;
	private Integer versionno;
	@Column(columnDefinition = "varchar(255)")
	private String versionname;
	
//	@Column(columnDefinition = "datetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifieddate;
	
	public Integer getProtocolversioncode() {
		return protocolversioncode;
	}
	public void setProtocolversioncode(Integer protocolversioncode) {
		this.protocolversioncode = protocolversioncode;
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
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
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
	public Integer getProtocolstatus() {
		return protocolstatus;
	}
	public void setProtocolstatus(Integer protocolstatus) {
		this.protocolstatus = protocolstatus;
	}
	public Integer getCreatedby() {
		return createdby;
	}
	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}
	public Integer getLssitemaster() {
		return lssitemaster;
	}
	public void setLssitemaster(Integer lssitemaster) {
		this.lssitemaster = lssitemaster;
	}
	public Integer getVersionno() {
		return versionno;
	}
	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}
	public String getVersionname() {
		return ""+this.getVersionno();
	}
	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}
	public Date getModifieddate() {
		return modifieddate;
	}
	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}
	
	@Override
	public int compareTo(LSprotocolversion o) {
		return this.getProtocolversioncode().compareTo(o.getProtocolversioncode());
	}
}
