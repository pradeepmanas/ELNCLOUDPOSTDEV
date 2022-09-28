package com.agaram.eln.primary.model.material;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="materialinventory")
public class MaterialInventory {
	
	@Id
	@Column(name = "nmaterialinventorycode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer nmaterialinventorycode;
	
	@Column(name = "nmaterialcode")
	private Integer nmaterialcode;
	
	@Column(name = "nmaterialcatcode")
	private Integer nmaterialcatcode;
	
	@Column(name = "nmaterialtypecode")
	private Integer nmaterialtypecode;
	
	private Integer nsectioncode;
	private Integer ntransactionstatus;
	
	@Column(name = "nstatus")
	private Integer nstatus;
	
	@Column(name = "jsondata", columnDefinition = "jsonb")
	private String jsondata;
	
	@Column(name = "jsonuidata", columnDefinition = "jsonb")
	private String jsonuidata;
	
	private transient String sinventoryid;
	private transient String savailablequatity;
	private transient String sunitname;
	
	public Integer getNmaterialcatcode() {
		return nmaterialcatcode;
	}
	public void setNmaterialcatcode(Integer nmaterialcatcode) {
		this.nmaterialcatcode = nmaterialcatcode;
	}
	public Integer getNmaterialtypecode() {
		return nmaterialtypecode;
	}
	public void setNmaterialtypecode(Integer nmaterialtypecode) {
		this.nmaterialtypecode = nmaterialtypecode;
	}
	public Integer getNsectioncode() {
		return nsectioncode;
	}
	public void setNsectioncode(Integer nsectioncode) {
		this.nsectioncode = nsectioncode;
	}
	public Integer getNtransactionstatus() {
		return ntransactionstatus;
	}
	public void setNtransactionstatus(Integer ntransactionstatus) {
		this.ntransactionstatus = ntransactionstatus;
	}
	public void setNmaterialinventorycode(Integer nmaterialinventorycode) {
		this.nmaterialinventorycode = nmaterialinventorycode;
	}
	public int getNmaterialinventorycode() {
		return nmaterialinventorycode;
	}
	public void setNmaterialinventorycode(int nmaterialinventorycode) {
		this.nmaterialinventorycode = nmaterialinventorycode;
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
	public String getSinventoryid() {
		return sinventoryid;
	}
	public void setSinventoryid(String sinventoryid) {
		this.sinventoryid = sinventoryid;
	}
	public String getSavailablequatity() {
		return savailablequatity;
	}
	public void setSavailablequatity(String savailablequatity) {
		this.savailablequatity = savailablequatity;
	}
	public String getSunitname() {
		return sunitname;
	}
	public void setSunitname(String sunitname) {
		this.sunitname = sunitname;
	}
}
