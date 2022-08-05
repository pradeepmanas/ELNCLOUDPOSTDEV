package com.agaram.eln.primary.model.Material;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "materialtype")
public class MaterialType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "nmaterialtypecode")	private Integer nmaterialtypecode;

	@Type(type = "jsonb")
	@Column(name = "jsondata", columnDefinition = "jsonb")	private Map<String, Object> jsondata;

	@ColumnDefault("4")
	@Column(name = "ndefaultstatus", nullable = false)private short ndefaultstatus = 4;

	@ColumnDefault("-1")
	@Column(name = "nsitecode", nullable = false)private short nsitecode = -1;

	@ColumnDefault("1")
	@Column(name = "nstatus", nullable = false)	private short nstatus = 1;

	private transient String smaterialtypename;
	private transient String sdescription;
	public Integer getNmaterialtypecode() {
		return nmaterialtypecode;
	}
	public void setNmaterialtypecode(Integer nmaterialtypecode) {
		this.nmaterialtypecode = nmaterialtypecode;
	}
	public Map<String, Object> getJsondata() {
		return jsondata;
	}
	public void setJsondata(Map<String, Object> jsondata) {
		this.jsondata = jsondata;
	}
	public short getNdefaultstatus() {
		return ndefaultstatus;
	}
	public void setNdefaultstatus(short ndefaultstatus) {
		this.ndefaultstatus = ndefaultstatus;
	}
	public short getNsitecode() {
		return nsitecode;
	}
	public void setNsitecode(short nsitecode) {
		this.nsitecode = nsitecode;
	}
	public short getNstatus() {
		return nstatus;
	}
	public void setNstatus(short nstatus) {
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	// Default constructor
	public MaterialType() {

	};

	// Parameterized Constructor to make a copy of object
	public MaterialType(final MaterialType materialtype) {
		this.nmaterialtypecode = materialtype.nmaterialtypecode;
		this.smaterialtypename = materialtype.smaterialtypename;
		this.sdescription = materialtype.sdescription;
		this.ndefaultstatus = materialtype.getNdefaultstatus();
		this.nsitecode = materialtype.nsitecode;
		this.nstatus = materialtype.nstatus;
	}
}

