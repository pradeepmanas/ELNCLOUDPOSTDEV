package com.agaram.eln.primary.model.equipment;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "equipment")
public class Equipment implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "nequipmentcode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer nequipmentcode;
	
	@ManyToOne
	private EquipmentCategory equipmentcategory;
	
	@ManyToOne
	private EquipmentType equipmenttype;
	
	private Integer ntransactionstatus;
	
	private Integer nstatus;
	
	private Integer nsitecode;
	
	private Date createddate;
	
	@ManyToOne
	private LSuserMaster createby;

	@Transient
	private LScfttransaction objsilentaudit;
	
	@Transient
	public String info;
	
	@Transient
	private LScfttransaction objmanualaudit;

	public Integer getNequipmentcode() {
		return nequipmentcode;
	}

	public void setNequipmentcode(Integer nequipmentcode) {
		this.nequipmentcode = nequipmentcode;
	}

	public EquipmentCategory getEquipmentcategory() {
		return equipmentcategory;
	}

	public void setEquipmentcategory(EquipmentCategory equipmentcategory) {
		this.equipmentcategory = equipmentcategory;
	}

	public EquipmentType getEquipmenttype() {
		return equipmenttype;
	}

	public void setEquipmenttype(EquipmentType equipmenttype) {
		this.equipmenttype = equipmenttype;
	}

	public Integer getNtransactionstatus() {
		return ntransactionstatus;
	}

	public void setNtransactionstatus(Integer ntransactionstatus) {
		this.ntransactionstatus = ntransactionstatus;
	}

	public Integer getNstatus() {
		return nstatus;
	}

	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}

	public Integer getNsitecode() {
		return nsitecode;
	}

	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
