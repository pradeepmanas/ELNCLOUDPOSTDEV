package com.agaram.eln.primary.model.material;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import com.agaram.eln.primary.model.cfr.LScfttransaction;

@Entity
@Table(name = "materialtype")
public class MaterialType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "nmaterialtypecode")	private Integer nmaterialtypecode;

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
	public String info;
	
	@Transient
	private LScfttransaction objmanualaudit;
	
	private String smaterialtypename;
	private transient String sdescription;
	private  transient String jsonconfigdata;
	
	public String getDisplaystatus() {
		return displaystatus;
	}
	public void setDisplaystatus(String displaystatus) {
		this.displaystatus = displaystatus;
	}
	public String getJsonconfigdata() {
		return jsonconfigdata;
	}
	public void setJsonconfigdata(String jsonconfigdata) {
		this.jsonconfigdata = jsonconfigdata;
	}
	public Integer getNmaterialtypecode() {
		return nmaterialtypecode;
	}
	public void setNmaterialtypecode(Integer nmaterialtypecode) {
		this.nmaterialtypecode = nmaterialtypecode;
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
	
	public String getSmaterialtypename() {
		
		return smaterialtypename;
	}
	public void setSmaterialtypename(String smaterialtypename) {
		this.smaterialtypename = smaterialtypename;
	}
	public String getSdescription() {
		return sdescription;
	}
	public void setSdescription(String sdescription) {
		this.sdescription = sdescription;
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
}

