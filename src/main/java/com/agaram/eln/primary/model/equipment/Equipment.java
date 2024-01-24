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
	
	private String sequipmentmake;
	private String sequipmentmodel;
	private String sequipmentlotno;
	
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
	
	private transient String sDate;
	private transient String cDate;
	private transient String mDate;
	
	private String callibrationvalue;
	private String callibrationperiod;
	private String manintanancevalue;
	private String maintananceperiod;
	
	private Boolean reqcalibration;
	private Boolean reqmaintanance;
	private Boolean equipmentused;
	
	private Date lastcallibrated;
	private Date lastmaintained;
	
	public Boolean getReqcalibration() {
		return reqcalibration;
	}

	public void setReqcalibration(Boolean reqcalibration) {
		this.reqcalibration = reqcalibration;
	}

	public Boolean getReqmaintanance() {
		return reqmaintanance;
	}

	public void setReqmaintanance(Boolean reqmaintanance) {
		this.reqmaintanance = reqmaintanance;
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

	public String getManintanancevalue() {
		return manintanancevalue;
	}

	public void setManintanancevalue(String manintanancevalue) {
		this.manintanancevalue = manintanancevalue;
	}

	public String getMaintananceperiod() {
		return maintananceperiod;
	}

	public void setMaintananceperiod(String maintananceperiod) {
		this.maintananceperiod = maintananceperiod;
	}

	public Boolean getEquipmentused() {
		return equipmentused;
	}

	public void setEquipmentused(Boolean equipmentused) {
		this.equipmentused = equipmentused;
	}

	public String getCallibrationvalue() {
		return callibrationvalue;
	}

	public void setCallibrationvalue(String callibrationvalue) {
		this.callibrationvalue = callibrationvalue;
	}

	public String getCallibrationperiod() {
		return callibrationperiod;
	}

	public void setCallibrationperiod(String callibrationperiod) {
		this.callibrationperiod = callibrationperiod;
	}

	private String remarks;
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSequipmentmake() {
		return sequipmentmake;
	}

	public void setSequipmentmake(String sequipmentmake) {
		this.sequipmentmake = sequipmentmake;
	}

	public String getSequipmentmodel() {
		return sequipmentmodel;
	}

	public void setSequipmentmodel(String sequipmentmodel) {
		this.sequipmentmodel = sequipmentmodel;
	}

	public String getSequipmentlotno() {
		return sequipmentlotno;
	}

	public void setSequipmentlotno(String sequipmentlotno) {
		this.sequipmentlotno = sequipmentlotno;
	}

	public String getsDate() {
		return sDate;
	}

	public void setsDate(String sDate) {
		this.sDate = sDate;
	}

	public String getcDate() {
		return cDate;
	}

	public void setcDate(String cDate) {
		this.cDate = cDate;
	}

	public String getmDate() {
		return mDate;
	}

	public void setmDate(String mDate) {
		this.mDate = mDate;
	}

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
