package com.agaram.eln.primary.model.inventory;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LSinstrumentcategory")
public class LSinstrumentcategory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name="instrumentcatcode")
	private Integer ninstrumentcatcode;
	@Column(name="status")
	private Integer nstatus;
	@Column(name="masterauditcode")
	private Integer nmasterauditcode;
	@Column(name="techniquecode")
	private Integer ntechniquecode;
	@Column(name="interfacetype")
	private Integer ninterfacetype;
	@Column(name="calibrationreq")
	private Integer ncalibrationreq;
	@Column(name="categorybasedflow")
	private Integer ncategorybasedflow;
	@Column(name="componentrequired")
	private Integer ncomponentrequired;
	@Column(name="sitecode")
	private Integer nsitecode;
	@Column(name="defaultstatus")
	private Integer ndefaultstatus;

	@Column(name="erpcode")
	private String serpcode;
	@Column(name="instrumentcatname")
	private String sinstrumentcatname;
	@Column(name="description")
	private String sdescription;
	
	public Integer getNinstrumentcatcode() {
		return ninstrumentcatcode;
	}
	public void setNinstrumentcatcode(Integer ninstrumentcatcode) {
		this.ninstrumentcatcode = ninstrumentcatcode;
	}
	public String getSinstrumentcatname() {
		return sinstrumentcatname;
	}
	public void setSinstrumentcatname(String sinstrumentcatname) {
		this.sinstrumentcatname = sinstrumentcatname;
	}
	public String getSdescription() {
		return sdescription;
	}
	public void setSdescription(String sdescription) {
		this.sdescription = sdescription;
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
	public Integer getNtechniquecode() {
		return ntechniquecode;
	}
	public void setNtechniquecode(Integer ntechniquecode) {
		this.ntechniquecode = ntechniquecode;
	}
	public Integer getNinterfacetype() {
		return ninterfacetype;
	}
	public void setNinterfacetype(Integer ninterfacetype) {
		this.ninterfacetype = ninterfacetype;
	}
	public String getSerpcode() {
		return serpcode;
	}
	public void setSerpcode(String serpcode) {
		this.serpcode = serpcode;
	}
	public Integer getNcalibrationreq() {
		return ncalibrationreq;
	}
	public void setNcalibrationreq(Integer ncalibrationreq) {
		this.ncalibrationreq = ncalibrationreq;
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
}
