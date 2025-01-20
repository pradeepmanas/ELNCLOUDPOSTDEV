package com.agaram.eln.primary.model.material;

import java.util.Date;

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
@Table(name = "section")
public class Section {
	@Id
	@Column(name = "nsectioncode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer nsectioncode;
	
	@Column(name = "ssectionname",columnDefinition = "varchar(120)")
	private String ssectionname;
	
	@Column(name = "sdescription",columnDefinition = "varchar(250)")
	private String sdescription;
	
	@Column(name = "nstatus")
	private Integer nstatus;
	
	@Column(name = "ndefaultstatus")
	private Integer ndefaultstatus;
	
	@Column(name = "nsitecode")
	private Integer nsitecode;

	@Transient
	private LScfttransaction objsilentaudit;
	
	private transient String sdisplaystatus;
	
	@Transient
	private String displaystatus;

	@Transient
	public String info;
	
	@Transient
	private LScfttransaction objmanualaudit;
	
	@Transient
	private Response response;
	
	@ManyToOne
	private LSuserMaster createby;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
	
	private transient String sDate;
	
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
	
	public String getDisplaystatus() {
		return displaystatus;
	}
	public void setDisplaystatus(String displaystatus) {
		this.displaystatus = displaystatus;
	}
	
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
	
	public String getSdisplaystatus() {
		return sdisplaystatus;
	}

	public void setSdisplaystatus(String sdisplaystatus) {
		this.sdisplaystatus = sdisplaystatus;
	}
	
	public Integer getNsectioncode() {
		return nsectioncode;
	}

	public void setNsectioncode(Integer nsectioncode) {
		this.nsectioncode = nsectioncode;
	}

	public String getSsectionname() {
		return ssectionname;
	}

	public void setSsectionname(String ssectionname) {
		this.ssectionname = ssectionname;
	}

	public String getSdescription() {
		return sdescription;
	}

	public void setSdescription(String sdescription) {
		this.sdescription = sdescription;
	}

	public Integer getNstatus() {
		return nstatus;
	}

	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}

	public Integer getNdefaultstatus() {
		return ndefaultstatus;
	}

	public void setNdefaultstatus(Integer ndefaultstatus) {
		this.ndefaultstatus = ndefaultstatus;
	}

	public Integer getNsitecode() {
		return nsitecode;
	}

	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	
}
