package com.agaram.eln.primary.model.material;

import java.io.Serializable;
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

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import com.agaram.eln.primary.model.barcode.BarcodeMaster;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "materialtype")
public class MaterialType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "nmaterialtypecode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer nmaterialtypecode;

	@Type(type = "jsonb")
	@Column(name = "jsondata", columnDefinition = "jsonb")	private String jsondata;

	@ColumnDefault("4")
	@Column(name = "ndefaultstatus", nullable = false)private Integer ndefaultstatus = 4;

	@ColumnDefault("-1")
	@Column(name = "nsitecode", nullable = false)private Integer nsitecode = -1;

	@ColumnDefault("1")
	@Column(name = "nstatus", nullable = false)	private Integer nstatus = 1;
	
	@Transient
	private String displaystatus;

	@Transient
	private LScfttransaction objsilentaudit;
	
	@Transient
	public String info;
	
	@Transient
	private LScfttransaction objmanualaudit;
	
	private String smaterialtypename;
	private transient String sdescription;
	private  transient String jsonconfigdata;
	
	@ManyToOne
	private LSuserMaster createby;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;

	private transient String sDate;
	
	private Boolean expvalidation;
	private Boolean quarvalidation;
	private Integer sampletype;
	@Transient
	private String smaterialcatname;
	
	public String getSmaterialcatname() {
		return smaterialcatname;
	}
	public void setSmaterialcatname(String smaterialcatname) {
		this.smaterialcatname = smaterialcatname;
	}
	@ManyToOne
	private BarcodeMaster barcode;
	
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
	
	public Integer getSampletype() {
		return sampletype;
	}
	public void setSampletype(Integer sampletype) {
		this.sampletype = sampletype;
	}
	public Boolean getExpvalidation() {
		return expvalidation;
	}
	public void setExpvalidation(Boolean expvalidation) {
		this.expvalidation = expvalidation;
	}
	public Boolean getQuarvalidation() {
		return quarvalidation;
	}
	public void setQuarvalidation(Boolean quarvalidation) {
		this.quarvalidation = quarvalidation;
	}
	public String getDisplaystatus() {
		return displaystatus;
	}
	public void setDisplaystatus(String displaystatus) {
		this.displaystatus = displaystatus;
	}
	public String getJsonconfigdata() {
		return jsonconfigdata;
	}
	public void setJsonconfigdata(String jsonconfigdata) {
		this.jsonconfigdata = jsonconfigdata;
	}
	public Integer getNmaterialtypecode() {
		return nmaterialtypecode;
	}
	public void setNmaterialtypecode(Integer nmaterialtypecode) {
		this.nmaterialtypecode = nmaterialtypecode;
	}
	public String getJsondata() {
		return jsondata;
	}
	public void setJsondata(String jsondata) {
		this.jsondata = jsondata;
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
	public Integer getNstatus() {
		return nstatus;
	}
	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	} 
	
	public String getSmaterialtypename() {
		
		return smaterialtypename;
	}
	public void setSmaterialtypename(String smaterialtypename) {
		this.smaterialtypename = smaterialtypename;
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
	public String getsDate() {
		return sDate;
	}
	public void setsDate(String sDate) {
		this.sDate = sDate;
	}
	public BarcodeMaster getBarcode() {
		return barcode;
	}
	public void setBarcode(BarcodeMaster barcode) {
		this.barcode = barcode;
	}
}

