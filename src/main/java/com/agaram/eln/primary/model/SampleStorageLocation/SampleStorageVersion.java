package com.agaram.eln.primary.model.SampleStorageLocation;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity(name = "SampleStorageVersion")
@Table(name = "samplestorageversion")
public class SampleStorageVersion {

	@Id
	@SequenceGenerator(name = "samplestorageversion_sequence", sequenceName = "samplestorageversion_sequence", allocationSize = 1)
	@GeneratedValue(strategy = SEQUENCE, generator = "samplestorageversion_sequence")
	@Column(name = "samplestorageversionkey", updatable = false)
	private Integer samplestorageversionkey;
	
	@Column(name = "createdby", nullable = false, columnDefinition = "TEXT")
	private String createdby = "administrator";
	
	@Column(name = "createddate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createddate;
	
	@ManyToOne
	@JoinColumn(name = "samplestoragelocationkey", nullable = false)
	private SampleStorageLocation sampleStorageLocation;
	
	@Column(name = "jsonbresult", nullable = false, columnDefinition = "jsonb")
	@Type(type ="jsonb")
	private String jsonbresult;	
	
	@Column(name = "versionno", nullable = false)
	private Integer versionno = 1;
	
	@Column(name = "approvalstatus", nullable = false)
	private Integer approvalstatus = 8;

	public Integer getSamplestorageversionkey() {
		return samplestorageversionkey;
	}

	public void setSamplestorageversionkey(Integer samplestorageversionkey) {
		this.samplestorageversionkey = samplestorageversionkey;
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

	public String getJsonbresult() {
		return jsonbresult;
	}

	public void setJsonbresult(String jsonbresult) {
		this.jsonbresult = jsonbresult;
	}

	public Integer getVersionno() {
		return versionno;
	}

	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}

	public Integer getApprovalstatus() {
		return approvalstatus;
	}

	public void setApprovalstatus(Integer approvalstatus) {
		this.approvalstatus = approvalstatus;
	}

	@JsonBackReference
	public SampleStorageLocation getSampleStorageLocation() {
		return sampleStorageLocation;
	}

	public void setSampleStorageLocation(SampleStorageLocation sampleStorageLocation) {
		this.sampleStorageLocation = sampleStorageLocation;
	}

	public SampleStorageVersion() {		
	}

	public SampleStorageVersion(Integer samplestorageversionkey, String createdby, Date createddate,
			SampleStorageLocation sampleStorageLocation, String jsonbresult,
			Integer versionno, Integer approvalstatus) {
		super();
		this.samplestorageversionkey = samplestorageversionkey;
		this.createdby = createdby;
		this.createddate = createddate;
		this.sampleStorageLocation = sampleStorageLocation;	
		this.jsonbresult = jsonbresult;
		this.versionno = versionno;
		this.approvalstatus = approvalstatus;
	}

	@Override
	public String toString() {
		return "SampleStorageVersion [samplestorageversionkey=" + samplestorageversionkey + ", createdby=" + createdby
				+ ", createddate=" + createddate + ", sampleStorageLocation=" + sampleStorageLocation
				+ ", jsonbresult=" + jsonbresult
				+ ", versionno=" + versionno + ", approvalstatus=" + approvalstatus + "]";
	}
}
