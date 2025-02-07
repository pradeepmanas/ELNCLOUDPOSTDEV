package com.agaram.eln.primary.model.sample;


import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "sampleprojecthistory")
public class SampleProjectHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "sampleprojectcode")
	private Integer sampleprojectcode;
	
	private Integer samplecode;
	
	@ManyToOne
	private LSprojectmaster lsproject;
	
	private Date createddate;
	
	@ManyToOne
	private LSuserMaster createby;
	
	private String description;

	public Integer getSampleprojectcode() {
		return sampleprojectcode;
	}

	public void setSampleprojectcode(Integer sampleprojectcode) {
		this.sampleprojectcode = sampleprojectcode;
	}

	public Integer getsamplecode() {
		return samplecode;
	}

	public void setsamplecode(Integer samplecode) {
		this.samplecode = samplecode;
	}

	public LSprojectmaster getLsproject() {
		return lsproject;
	}

	public void setLsproject(LSprojectmaster lsproject) {
		this.lsproject = lsproject;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public LSuserMaster getCreateby() {
		return createby;
	}

	public void setCreateby(LSuserMaster createby) {
		this.createby = createby;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
