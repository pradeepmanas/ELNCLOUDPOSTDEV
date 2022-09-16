package com.agaram.eln.primary.model.material;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "material")
public class Material implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "nmaterialcode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer nmaterialcode;
	
	private Integer nmaterialcatcode;
	private Integer ntransactionstatus;
	
	@Column(name = "nstatus")
	private Integer nstatus;
	
	@Column(name = "nmaterialtypecode")
	private Integer nmaterialtypecode;
	
	@Type(type = "jsonb")
	@Column(name = "jsondata", columnDefinition = "jsonb")
	private String jsondata;
	
	@Type(type = "jsonb")
	@Column(name = "jsonuidata", columnDefinition = "jsonb")
	private String jsonuidata;
	
	@Column(name = "smaterialname")
	private String smaterialname;
	
	private transient String sunitname;
	private transient short needsection;
	private transient int nproductcode;
	private transient String sproductname;
	
	public Integer getNmaterialtypecode() {
		return nmaterialtypecode;
	}
	public void setNmaterialtypecode(Integer nmaterialtypecode) {
		this.nmaterialtypecode = nmaterialtypecode;
	}
	public Integer getNtransactionstatus() {
		return ntransactionstatus;
	}
	public void setNtransactionstatus(Integer ntransactionstatus) {
		this.ntransactionstatus = ntransactionstatus;
	}
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
	public Integer getNstatus() {
		return nstatus;
	}
	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}
	public String getJsondata() {
		return jsondata;
	}
	public void setJsondata(String jsondata) {
		this.jsondata = jsondata;
	}
	public String getJsonuidata() {
		return jsonuidata;
	}
	public void setJsonuidata(String jsonuidata) {
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
