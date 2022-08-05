package com.agaram.eln.primary.model.SampleStorageLocation;

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
	
	@Column(name = "sitekey", nullable = false)
	private Integer sitekey = -1;

	
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
				+ createdby + ", createddate=" + createddate + ", sampleStorageVersion=" + sampleStorageVersion
				+ ", samplestoragelocationname=" + samplestoragelocationname + ", status=" + status + ", sitekey="
				+ sitekey + "]";
	}	
}