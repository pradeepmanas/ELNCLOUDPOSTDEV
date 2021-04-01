package com.agaram.eln.primary.model.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LSmaterialcategory")
public class LSmaterialcategory {
	@Id
	@Column(name ="materialcatcode")
	private Integer nmaterialcatcode;
	@Column(name ="materialtypecode")
	private Integer nmaterialtypecode;
	@Column(name ="qarentainstatus")
	private Integer nqarentainstatus;
	@Column(name ="barcode")
	private Integer nbarcode;
	@Column(name ="categorybasedflow")
	private Integer ncategorybasedflow;
	@Column(name ="componentrequired")
	private Integer ncomponentrequired;
	@Column(name ="sitecode")
	private Integer nsitecode;
	@Column(name ="defaultstatus")
	private Integer ndefaultstatus;
	@Column(name ="status")
	private Integer nstatus;
	@Column(name ="masterauditcode")
	private Integer nmasterauditcode;
	@Column(name ="userrolecode")
	private Integer nuserrolecode;

	@Column(name ="erpcode",length=30)
	private String serpcode;
	@Column(name ="materialcatname",length=100)
	private String smaterialcatname;
	@Column(name ="description",length=250)
	private String sdescription;
	
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
	public Integer getNuserrolecode() {
		return nuserrolecode;
	}
	public void setNuserrolecode(Integer nuserrolecode) {
		this.nuserrolecode = nuserrolecode;
	}
	public Integer getNqarentainstatus() {
		return nqarentainstatus;
	}
	public void setNqarentainstatus(Integer nqarentainstatus) {
		this.nqarentainstatus = nqarentainstatus;
	}
	public Integer getNbarcode() {
		return nbarcode;
	}
	public void setNbarcode(Integer nbarcode) {
		this.nbarcode = nbarcode;
	}
	public Integer getNcategorybasedflow() {
		return ncategorybasedflow;
	}
	public void setNcategorybasedflow(Integer ncategorybasedflow) {
		this.ncategorybasedflow = ncategorybasedflow;
	}
	public Integer getNcomponentrequired() {
		return ncomponentrequired;
	}
	public void setNcomponentrequired(Integer ncomponentrequired) {
		this.ncomponentrequired = ncomponentrequired;
	}
	public Integer getNsitecode() {
		return nsitecode;
	}
	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}
	public Integer getNdefaultstatus() {
		return ndefaultstatus;
	}
	public void setNdefaultstatus(Integer ndefaultstatus) {
		this.ndefaultstatus = ndefaultstatus;
	}
	public Integer getNstatus() {
		return nstatus;
	}
	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}
	public Integer getNmasterauditcode() {
		return nmasterauditcode;
	}
	public void setNmasterauditcode(Integer nmasterauditcode) {
		this.nmasterauditcode = nmasterauditcode;
	}
	public String getSerpcode() {
		return serpcode;
	}
	public void setSerpcode(String serpcode) {
		this.serpcode = serpcode;
	}
	public String getSmaterialcatname() {
		return smaterialcatname;
	}
	public void setSmaterialcatname(String smaterialcatname) {
		this.smaterialcatname = smaterialcatname;
	}
	public String getSdescription() {
		return sdescription;
	}
	public void setSdescription(String sdescription) {
		this.sdescription = sdescription;
	}
	
}
