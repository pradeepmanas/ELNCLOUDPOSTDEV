package com.agaram.eln.primary.model.sample;

import java.io.Serializable;
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

import org.hibernate.annotations.Type;

import com.agaram.eln.primary.model.material.Unit;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "sample")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sample implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "samplecode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer samplecode;
	
	@ManyToOne
	private SampleCategory samplecategory;
	
	@ManyToOne
	private SampleType sampletype;
	
	@ManyToOne
	private Unit unit;
	
	@ManyToOne
	private LSuserMaster createby;
	
	@Column(name = "nstatus")
	private Integer nstatus;
	
	private Date createddate;
	
	private Integer nsitecode;
	private Long sitesequence;
	
	@Type(type = "jsonb")
	@Column(name = "jsondata", columnDefinition = "jsonb")
	private String jsondata;
	
	@Column(name = "samplename")
	private String samplename;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifieddate;
	
	@Column(name = "modifiedby")
	private String modifiedby;
	
	@Column(name = "applicationsequence")
	private Long applicationsequence;
	
	@Column(name = "sequenceid")
	private String sequenceid;
	
	@OneToMany
	@JoinColumn(name="samplecode")
	private List<DerivedSamples> parentsamples;
	
	private Integer derivedtype;

	public Integer getSamplecode() {
		return samplecode;
	}

	public void setSamplecode(Integer samplecode) {
		this.samplecode = samplecode;
	}

	public SampleCategory getSamplecategory() {
		return samplecategory;
	}

	public void setSamplecategory(SampleCategory samplecategory) {
		this.samplecategory = samplecategory;
	}

	public SampleType getSampletype() {
		return sampletype;
	}

	public void setSampletype(SampleType sampletype) {
		this.sampletype = sampletype;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public LSuserMaster getCreateby() {
		return createby;
	}

	public void setCreateby(LSuserMaster createby) {
		this.createby = createby;
	}

	public Integer getNstatus() {
		return nstatus;
	}

	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public Integer getNsitecode() {
		return nsitecode;
	}

	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}

	public String getJsondata() {
		return jsondata;
	}

	public void setJsondata(String jsondata) {
		this.jsondata = jsondata;
	}

	public String getSamplename() {
		return samplename;
	}

	public void setSamplename(String samplename) {
		this.samplename = samplename;
	}

	public Date getModifieddate() {
		return modifieddate;
	}

	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}

	public String getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}

	public Long getApplicationsequence() {
		return applicationsequence;
	}

	public void setApplicationsequence(Long applicationsequence) {
		this.applicationsequence = applicationsequence;
	}

	public String getSequenceid() {
		return sequenceid;
	}

	public void setSequenceid(String sequenceid) {
		this.sequenceid = sequenceid;
	}
	public Long getSitesequence() {
		return sitesequence;
	}
 
	public void setSitesequence(Long sitesequence) {
		this.sitesequence = sitesequence;
	}
	public List<DerivedSamples> getParentsamples() {
		return parentsamples;
	}

	public void setParentsamples(List<DerivedSamples> parentsamples) {
		this.parentsamples = parentsamples;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getDerivedtype() {
		return derivedtype;
	}

	public void setDerivedtype(Integer derivedtype) {
		this.derivedtype = derivedtype;
	}

}

