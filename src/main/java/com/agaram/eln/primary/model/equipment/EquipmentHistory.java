package com.agaram.eln.primary.model.equipment;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "equipmenthistory")
public class EquipmentHistory{

	@Id
	@Column(name = "nequipmenthistorycode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer nequipmenthistorycode;

	@Column(name = "nequipmentcode")
	private Integer nequipmentcode;
	@Column(name = "historytype")
	private Integer historytype;
	
	@Column(name = "nstatus", nullable = false)
	private Integer nstatus = 1;
	
	@ManyToOne
	private LSuserMaster createdby;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createddate;
	
	private Date callibrationdate;
	private Date manintanancedate;
	private Date lastcallibrated;
	private Date lastmaintained;
	
	@Transient
	Response response;

	public Integer getNequipmenthistorycode() {
		return nequipmenthistorycode;
	}

	public void setNequipmenthistorycode(Integer nequipmenthistorycode) {
		this.nequipmenthistorycode = nequipmenthistorycode;
	}

	public Integer getNequipmentcode() {
		return nequipmentcode;
	}

	public void setNequipmentcode(Integer nequipmentcode) {
		this.nequipmentcode = nequipmentcode;
	}

	public Integer getNstatus() {
		return nstatus;
	}

	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}

	public LSuserMaster getCreatedby() {
		return createdby;
	}

	public void setCreatedby(LSuserMaster createdby) {
		this.createdby = createdby;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
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

	public Date getLastcallibrated() {
		return lastcallibrated;
	}

	public void setLastcallibrated(Date lastcallibrated) {
		this.lastcallibrated = lastcallibrated;
	}

	public Date getLastmaintained() {
		return lastmaintained;
	}

	public void setLastmaintained(Date lastmaintained) {
		this.lastmaintained = lastmaintained;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public Integer getHistorytype() {
		return historytype;
	}

	public void setHistorytype(Integer historytype) {
		this.historytype = historytype;
	}
}
