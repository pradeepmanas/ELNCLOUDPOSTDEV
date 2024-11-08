package com.agaram.eln.primary.model.equipment;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "equipmentcategory")
public class EquipmentCategory {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nequipmentcatcode")
	private Integer nequipmentcatcode;

	@ManyToOne
	private EquipmentType equipmenttype;

	@Column(name = "sequipmentcatname", length = 100, nullable = false)
	private String sequipmentcatname;

	@Column(name = "sdescription", length = 255)
	private String sdescription;

	@ColumnDefault("4")
	@Column(name = "ndefaultstatus", nullable = false)
	private Integer ndefaultstatus = 4;

	@ColumnDefault("-1")
	@Column(name = "nsitecode", nullable = false)
	private Integer nsitecode = -1;

	@Transient
	private LScfttransaction objsilentaudit;
	
	@Transient
	public String info;
	
	@Transient
	public String displaystatus;
	
	@Transient
	private LScfttransaction objmanualaudit;
	
	@Transient
	private Response response;
	
	@ManyToOne
	private LSuserMaster createby;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;

	private transient String sDate;
	
	@ColumnDefault("1")
	@Column(name = "nstatus", nullable = false)
	private Integer nstatus = 1;

	public String getDisplaystatus() {
		return displaystatus;
	}

	public void setDisplaystatus(String displaystatus) {
		this.displaystatus = displaystatus;
	}

	public Integer getNstatus() {
		return nstatus;
	}

	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}

	public Integer getNequipmentcatcode() {
		return nequipmentcatcode;
	}

	public void setNequipmentcatcode(Integer nequipmentcatcode) {
		this.nequipmentcatcode = nequipmentcatcode;
	}

	public EquipmentType getEquipmenttype() {
		return equipmenttype;
	}

	public void setEquipmenttype(EquipmentType equipmenttype) {
		this.equipmenttype = equipmenttype;
	}

	public String getSequipmentcatname() {
		return sequipmentcatname;
	}

	public void setSequipmentcatname(String sequipmentcatname) {
		this.sequipmentcatname = sequipmentcatname;
	}

	public String getSdescription() {
		return sdescription;
	}

	public void setSdescription(String sdescription) {
		this.sdescription = sdescription;
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

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
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
