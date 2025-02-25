package com.agaram.eln.primary.model.sample;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;

@Entity
@Table(name = "sampleprojectmap")
public class SampleProjectMap {

	@Id
	@Column(name = "sampleprojectcode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sampleprojectcode;
	
	private Integer samplecode;
	
	@ManyToOne
	private LSprojectmaster lsproject;
	
	@Transient
	private String samplename;

	public String getSamplename() {
		return samplename;
	}

	public void setSamplename(String samplename) {
		this.samplename = samplename;
	}

	public Integer getSampleprojectcode() {
		return sampleprojectcode;
	}

	public void setSampleprojectcode(Integer sampleprojectcode) {
		this.sampleprojectcode = sampleprojectcode;
	}

	public Integer getSamplecode() {
		return samplecode;
	}

	public void setSamplecode(Integer samplecode) {
		this.samplecode = samplecode;
	}

	public LSprojectmaster getLsproject() {
		return lsproject;
	}

	public void setLsproject(LSprojectmaster lsproject) {
		this.lsproject = lsproject;
	}
	
	
}
