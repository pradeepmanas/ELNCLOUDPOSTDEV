package com.agaram.eln.primary.model.sample;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.samplestoragelocation.SampleStorageLocation;

@Entity
@Table(name = "samplestoragemapping")
public class SampleStorageMapping {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "mappedid") 
	private Integer samplemapid;
	
	private String storagepath;

	public String id;
	
	@OneToOne
	private Sample sample;
	
	@Transient
	private String samplename;
	
	public String getSamplename() {
		return samplename;
	}

	public void setSamplename(String samplename) {
		this.samplename = samplename;
	}

	@OneToOne
	@JoinColumn(name = "samplestoragelocationkey", referencedColumnName = "samplestoragelocationkey")
	private SampleStorageLocation samplestoragelocationkey;

	public Integer getSamplemapid() {
		return samplemapid;
	}

	public void setSamplemapid(Integer samplemapid) {
		this.samplemapid = samplemapid;
	}

	public String getStoragepath() {
		return storagepath;
	}

	public void setStoragepath(String storagepath) {
		this.storagepath = storagepath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Sample getSample() {
		return sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
	}

	public SampleStorageLocation getSamplestoragelocationkey() {
		return samplestoragelocationkey;
	}

	public void setSamplestoragelocationkey(SampleStorageLocation samplestoragelocationkey) {
		this.samplestoragelocationkey = samplestoragelocationkey;
	}
	
	
}
