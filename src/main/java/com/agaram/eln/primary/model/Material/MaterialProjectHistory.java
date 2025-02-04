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

import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "materialprojecthistory")
public class MaterialProjectHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "materialprojectcode")
	private Integer materialprojectcode;
	
	private Integer nmaterialcode;
	
	@ManyToOne
	private LSprojectmaster lsproject;
	
	private Date createddate;
	
	@ManyToOne
	private LSuserMaster createby;
	
	private String description;

	public Integer getMaterialprojectcode() {
		return materialprojectcode;
	}

	public void setMaterialprojectcode(Integer materialprojectcode) {
		this.materialprojectcode = materialprojectcode;
	}

	public Integer getNmaterialcode() {
		return nmaterialcode;
	}

	public void setNmaterialcode(Integer nmaterialcode) {
		this.nmaterialcode = nmaterialcode;
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
