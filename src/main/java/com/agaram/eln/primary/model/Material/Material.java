package com.agaram.eln.primary.model.material;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "material")
public class Material implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "nmaterialcode")
	private Integer nmaterialcode;
	
	private Integer nmaterialcatcode;

	@Column(name = "nstatus")
	private short nstatus;
	
	@Type(type = "jsonb")
	@Column(name = "jsondata", columnDefinition = "jsonb")
	private Map<String, Object> jsondata;
	
	@Type(type = "jsonb")
	@Column(name = "jsonuidata", columnDefinition = "jsonb")
	private Map<String, Object> jsonuidata;
	private transient String smaterialname;
	private transient String sunitname;
	private transient short needsection;
	private transient int nproductcode;
	private transient String sproductname;
	
	public Integer getNmaterialcatcode() {
		return nmaterialcatcode;
	}
	public void setNmaterialcatcode(Integer nmaterialcatcode) {
		this.nmaterialcatcode = nmaterialcatcode;
	}
	public Integer getNmaterialcode() {
		return nmaterialcode;
	}
	public void setNmaterialcode(Integer nmaterialcode) {
		this.nmaterialcode = nmaterialcode;
	}
	public short getNstatus() {
		return nstatus;
	}
	public void setNstatus(short nstatus) {
		this.nstatus = nstatus;
	}
	public Map<String, Object> getJsondata() {
		return jsondata;
	}
	public void setJsondata(Map<String, Object> jsondata) {
		this.jsondata = jsondata;
	}
	public Map<String, Object> getJsonuidata() {
		return jsonuidata;
	}
	public void setJsonuidata(Map<String, Object> jsonuidata) {
		this.jsonuidata = jsonuidata;
	}
	public String getSmaterialname() {
		return smaterialname;
	}
	public void setSmaterialname(String smaterialname) {
		this.smaterialname = smaterialname;
	}
	public String getSunitname() {
		return sunitname;
	}
	public void setSunitname(String sunitname) {
		this.sunitname = sunitname;
	}
	public short getNeedsection() {
		return needsection;
	}
	public void setNeedsection(short needsection) {
		this.needsection = needsection;
	}
	public int getNproductcode() {
		return nproductcode;
	}
	public void setNproductcode(int nproductcode) {
		this.nproductcode = nproductcode;
	}
	public String getSproductname() {
		return sproductname;
	}
	public void setSproductname(String sproductname) {
		this.sproductname = sproductname;
	}
}
