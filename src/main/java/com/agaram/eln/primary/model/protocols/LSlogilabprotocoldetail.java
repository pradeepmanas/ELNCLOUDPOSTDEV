package com.agaram.eln.primary.model.protocols;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;

@Entity(name = "LSlogilabprotocoldetail")
@Table(name = "LSlogilabprotocoldetail")
public class LSlogilabprotocoldetail implements Comparable<LSlogilabprotocoldetail>{
	@Id
	@SequenceGenerator(name = "orderGen", 
	sequenceName = "orderDetail", 
	initialValue = 1000000, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "orderGen")
	@Column(columnDefinition = "numeric(17,0)",name = "Protocolordercode") 
	private Long protocolordercode;
	
	@Column(columnDefinition = "varchar(250)",name = "Protocolordername") 
	private String protoclordername;
	
	@Column(columnDefinition = "varchar(250)",name = "Keyword") 
	private String keyword;
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	private String orderflag;
	
	public String getOrderflag() {
		return orderflag;
	}

	public void setOrderflag(String orderflag) {
		this.orderflag = orderflag;
	}

	@Column(name = "Protocoltype")
	private Integer protocoltype;
	
	@Column(name = "CreatedTimeStamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdtimestamp;
	
	@Column(name = "CompletedTimeStamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date completedtimestamp;
	
	@ManyToOne
	private LSprotocolmaster Lsprotocolmaster;
	
	@ManyToOne
	private LSuserMaster LsuserMaster;
	
	private Integer Testcode;
	
	@ManyToOne
	private LSsamplemaster Lssamplemaster;
	
	@ManyToOne
	private LSprojectmaster Lsprojectmaster;
	
	@Transient
	LoggedUser objuser;
	
	@Transient
	LScfttransaction objmanualaudit;
	
	@Transient
	LScfttransaction objsilentaudit;
	
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	@Transient
	private Integer ismultitenant;

	public Long getProtocolordercode() {
		return protocolordercode;
	}

	public void setProtocolordercode(Long protocolordercode) {
		this.protocolordercode = protocolordercode;
	}

	public String getProtoclordername() {
		return protoclordername;
	}

	public void setProtoclordername(String protoclordername) {
		this.protoclordername = protoclordername;
	}

	public Integer getProtocoltype() {
		return protocoltype;
	}

	public void setProtocoltype(Integer protocoltype) {
		this.protocoltype = protocoltype;
	}

	public Date getCreatedtimestamp() {
		return createdtimestamp;
	}

	public void setCreatedtimestamp(Date createdtimestamp) {
		this.createdtimestamp = createdtimestamp;
	}

	public Date getCompletedtimestamp() {
		return completedtimestamp;
	}

	public void setCompletedtimestamp(Date completedtimestamp) {
		this.completedtimestamp = completedtimestamp;
	}

	public LoggedUser getObjuser() {
		return objuser;
	}

	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
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

	public LSprotocolmaster getLsprotocolmaster() {
		return Lsprotocolmaster;
	}

	public void setLsprotocolmaster(LSprotocolmaster lsprotocolmaster) {
		Lsprotocolmaster = lsprotocolmaster;
	}

	public LSuserMaster getLsuserMaster() {
		return LsuserMaster;
	}

	public void setLsuserMaster(LSuserMaster lsuserMaster) {
		LsuserMaster = lsuserMaster;
	}

	public Integer getTestcode() {
		return Testcode;
	}

	public void setTestcode(Integer testcode) {
		Testcode = testcode;
	}

	public LSsamplemaster getLssamplemaster() {
		return Lssamplemaster;
	}

	public void setLssamplemaster(LSsamplemaster lssamplemaster) {
		Lssamplemaster = lssamplemaster;
	}

	public LSprojectmaster getLsprojectmaster() {
		return Lsprojectmaster;
	}

	public void setLsprojectmaster(LSprojectmaster lsprojectmaster) {
		Lsprojectmaster = lsprojectmaster;
	}

	@Override
	public int compareTo(LSlogilabprotocoldetail o) {
		return this.getProtocolordercode().compareTo(o.getProtocolordercode());
	}
}
