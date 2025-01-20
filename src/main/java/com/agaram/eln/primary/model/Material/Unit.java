package com.agaram.eln.primary.model.material;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "unit")
public class Unit {
	
	@Id
	@Column(name = "nunitcode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private Integer nunitcode;
	@Column(name = "nstatus")
	private Integer nstatus;
	@Column(name = "nsitecode")
	private Integer nsitecode;
	@Column(name = "ndefaultstatus")
	private Integer ndefaultstatus;
	@Column(name = "sunitname")
	private String sunitname;
	@Column(name = "sdescription")
	private String sdescription;
	@Transient
	private LScfttransaction objsilentaudit;
	
	@ManyToOne
	private LSuserMaster createby;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;

	private transient String sDate;
	
	@Transient
	private String smaterialcatname;
	
	private transient String mDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifieddate;
	
	@Column(name = "modifiedby")
	private String modifiedby;
	
	public String getmDate() {
		return mDate;
	}
	public void setmDate(String mDate) {
		this.mDate = mDate;
	}
	public String getModifiedby() {
		return modifiedby;
	}
	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}
	public Date getModifieddate() {
		return modifieddate;
	}
	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}
	
	
	public String getSmaterialcatname() {
		return smaterialcatname;
	}
	public void setSmaterialcatname(String smaterialcatname) {
		this.smaterialcatname = smaterialcatname;
	}
	public String getsDate() {
		return sDate;
	}
	public void setsDate(String sDate) {
		this.sDate = sDate;
	}
	
	public LSuserMaster getCreateby() {
		return createby;
	}
	public void setCreateby(LSuserMaster createby) {
		this.createby = createby;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
	@Transient
	public String info;
	
	@Transient
	private Response response;
	
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
	public String getDisplaystatus() {
		return displaystatus;
	}
	public void setDisplaystatus(String displaystatus) {
		this.displaystatus = displaystatus;
	}
	private transient String displaystatus;
	
	@Transient
	private LScfttransaction objmanualaudit;
	
	public String getSunitname() {
		return sunitname;
	}
	public void setSunitname(String sunitname) {
		this.sunitname = sunitname;
	}
	public Integer getNunitcode() {
		return nunitcode;
	}
	public void setNunitcode(Integer nunitcode) {
		this.nunitcode = nunitcode;
	}
	public Integer getNstatus() {
		return nstatus;
	}
	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}
	public Integer getNsitecode() {
		return nsitecode;
	}
	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}
	public Integer getNdefaultstatus() {
		return ndefaultstatus;
	}
	public void setNdefaultstatus(Integer ndefaultstatus) {
		this.ndefaultstatus = ndefaultstatus;
	}
	public String getSdescription() {
		return sdescription;
	}
	public void setSdescription(String sdescription) {
		this.sdescription = sdescription;
	}
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}
	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}
	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}	
	
}