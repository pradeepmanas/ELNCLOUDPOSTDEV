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

import org.hibernate.annotations.Type;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
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
	
	@Column(name = "sequipmentname", length = 100, nullable = false)
	private String sequipmentname;
	
	@Column(name = "sequipmentid", length = 100)
	private String sequipmentid;
	
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

	@Transient
	private Response response;
	
	private Date callibrationdate;
	private Date manintanancedate;
	
	@Type(type = "jsonb")
	@Column(name = "jsondata", columnDefinition = "jsonb")
	private String jsondata;
	
	public String getJsondata() {
		return jsondata;
	}

	public void setJsondata(String jsondata) {
		this.jsondata = jsondata;
	}

	public Date getCallibrationdate() {
		return callibrationdate;
	}

	public void setCallibrationdate(Date callibrationdate) {
		this.callibrationdate = callibrationdate;
	}

	public Date getManintanancedate() {
		return manintanancedate;
	}

	public void setManintanancedate(Date manintanancedate) {
		this.manintanancedate = manintanancedate;
	}
	
	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public String getSequipmentname() {
		return sequipmentname;
	}

	public void setSequipmentname(String sequipmentname) {
		this.sequipmentname = sequipmentname;
	}

	public String getSequipmentid() {
		return sequipmentid;
	}

	public void setSequipmentid(String sequipmentid) {
		this.sequipmentid = sequipmentid;
	}

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