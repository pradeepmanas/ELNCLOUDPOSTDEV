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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "equipmenttype")
public class EquipmentType implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "nequipmenttypecode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer nequipmenttypecode;

	@Type(type = "jsonb")
	@Column(name = "jsondata", columnDefinition = "jsonb")	private String jsondata;

	@ColumnDefault("4")
	@Column(name = "ndefaultstatus", nullable = false)private Integer ndefaultstatus = 4;

	@ColumnDefault("-1")
	@Column(name = "nsitecode", nullable = false)private Integer nsitecode = -1;

	@ColumnDefault("1")
	@Column(name = "nstatus", nullable = false)	private Integer nstatus = 1;
	
	@Transient
	private String displaystatus;

	@Transient
	private LScfttransaction objsilentaudit;
	
	@Transient
	private LScfttransaction objmanualaudit;
	
	private String sequipmenttypename;
	
	@ManyToOne
	private LSuserMaster createby;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;

	private transient String sDate;
	
	@Transient
	public String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Integer getNequipmenttypecode() {
		return nequipmenttypecode;
	}

	public void setNequipmenttypecode(Integer nequipmenttypecode) {
		this.nequipmenttypecode = nequipmenttypecode;
	}

	public String getJsondata() {
		return jsondata;
	}

	public void setJsondata(String jsondata) {
		this.jsondata = jsondata;
	}

	public Integer getNdefaultstatus() {
		return ndefaultstatus;
	}

	public void setNdefaultstatus(Integer ndefaultstatus) {
		this.ndefaultstatus = ndefaultstatus;
	}

	public Integer getNsitecode() {
		return nsitecode;
	}

	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}

	public Integer getNstatus() {
		return nstatus;
	}

	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}

	public String getDisplaystatus() {
		return displaystatus;
	}

	public void setDisplaystatus(String displaystatus) {
		this.displaystatus = displaystatus;
	}

	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}

	public String getSequipmenttypename() {
		return sequipmenttypename;
	}

	public void setSequipmenttypename(String sequipmenttypename) {
		this.sequipmenttypename = sequipmenttypename;
	}

	public LSuserMaster getCreateby() {
		return createby;
	}

	public void setCreateby(LSuserMaster createby) {
		this.createby = createby;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getsDate() {
		return sDate;
	}

	public void setsDate(String sDate) {
		this.sDate = sDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}