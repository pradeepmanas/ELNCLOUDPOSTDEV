package com.agaram.eln.primary.model.inventory;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name = "LSmaterialmap")
public class LSmaterialmap {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "lsmaterialcode")
	private Integer lsmaterialcode;
	private Integer testcode;
	private Integer materialcode;
	
	@ManyToOne
	private LSmaterial LSmaterial;
	
	public LSmaterial getLSmaterial() {
		return LSmaterial;
	}
	public void setLSmaterial(LSmaterial lSmaterial) {
		LSmaterial = lSmaterial;
	}
	public Integer getLsmaterialcode() {
		return lsmaterialcode;
	}
	public void setLsmaterialcode(Integer lsmaterialcode) {
		this.lsmaterialcode = lsmaterialcode;
	}
	public Integer getTestcode() {
		return testcode;
	}
	public void setTestcode(Integer testcode) {
		this.testcode = testcode;
	}
	public Integer getMaterialcode() {
		return materialcode;
	}
	public void setMaterialcode(Integer materialcode) {
		this.materialcode = materialcode;
	}
	
	
}
