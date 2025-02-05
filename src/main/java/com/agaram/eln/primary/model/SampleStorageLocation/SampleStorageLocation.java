package com.agaram.eln.primary.model.samplestoragelocation;

import static javax.persistence.GenerationType.SEQUENCE;

//import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity(name = "SampleStorageLocation")
@Table(name = "samplestoragelocation")
public class SampleStorageLocation {

	@Id
	@SequenceGenerator(name = "samplestoragelocation_sequence", sequenceName = "samplestoragelocation_sequence", allocationSize = 1)
	@GeneratedValue(strategy = SEQUENCE, generator = "samplestoragelocation_sequence")
	@Column(name = "samplestoragelocationkey", updatable = false)
	private Integer samplestoragelocationkey;
	
	@Column(name = "createdby", nullable = false, columnDefinition = "TEXT")
	private String createdby = "administrator";
	
	@Column(name = "createddate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createddate;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "sampleStorageLocation", fetch = FetchType.LAZY)	
    @OrderBy("samplestorageversionkey")
	private List<SampleStorageVersion> sampleStorageVersion;
	
//	@Max(value = 100)
	@Column(name = "samplestoragelocationname", nullable = false, columnDefinition = "character varying")
	private String samplestoragelocationname;
	
	@Column(name = "status", nullable = false)
	private Integer status = 1;
	
	@Transient
	private LScfttransaction objsilentaudit;
	
	@Transient
	public String info;
	
	@Transient
	private LScfttransaction objmanualaudit;
	
	@Column(name = "sitekey", nullable = false)
	private Integer sitekey = -1;
	
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
	
	public Integer getSamplestoragelocationkey() {
		return samplestoragelocationkey;
	}


	public void setSamplestoragelocationkey(Integer samplestoragelocationkey) {
		this.samplestoragelocationkey = samplestoragelocationkey;
	}


	public String getCreatedby() {
		return createdby;
	}


	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}


	public Date getCreateddate() {
		return createddate;
	}


	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}


	public String getSamplestoragelocationname() {
		return samplestoragelocationname;
	}


	public void setSamplestoragelocationname(String samplestoragelocationname) {
		this.samplestoragelocationname = samplestoragelocationname;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getSitekey() {
		return sitekey;
	}


	public void setSitekey(Integer sitekey) {
		this.sitekey = sitekey;
	}

   @JsonManagedReference
   public List<SampleStorageVersion> getSampleStorageVersion() {
		return sampleStorageVersion;
	}


	public void setSampleStorageVersion(List<SampleStorageVersion> sampleStorageVersion) {
		this.sampleStorageVersion = sampleStorageVersion;
	}


	public SampleStorageLocation() {		
	}


	public SampleStorageLocation(Integer samplestoragelocationkey, String createdby, Date createddate,
			List<SampleStorageVersion> sampleStorageVersion,String samplestoragelocationname, Integer status,
			Integer sitekey) {
		super();
		this.samplestoragelocationkey = samplestoragelocationkey;
		this.createdby = createdby;
		this.createddate = createddate;
		this.sampleStorageVersion = sampleStorageVersion;
		this.samplestoragelocationname = samplestoragelocationname;
		this.status = status;
		this.sitekey = sitekey;
	}


	@Override
	public String toString() {
		return "SampleStorageLocation [samplestoragelocationkey=" + samplestoragelocationkey + ", createdby="
				+ createdby + ", createddate=" + createddate + ", sampleStorageVersion=" + sampleStorageVersion.size()
				+ ", samplestoragelocationname=" + samplestoragelocationname + ", status=" + status + ", sitekey="
				+ sitekey + "]";
	}	
}