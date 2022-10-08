package com.agaram.eln.primary.model.material;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "unit")
public class Unit {
	
	@Id
	@Column(name = "nunitcode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private Integer nunitcode;
	@Column(name = "nstatus")
	private Integer nstatus;
	@Column(name = "nsitecode")
	private Integer nsitecode;
	@Column(name = "ndefaultstatus")
	private Integer ndefaultstatus;
	@Column(name = "sunitname")
	private String sunitname;
	@Column(name = "sdescription")
	private String sdescription;
	
	public String getSunitname() {
		return sunitname;
	}
	public void setSunitname(String sunitname) {
		this.sunitname = sunitname;
	}
	public Integer getNunitcode() {
		return nunitcode;
	}
	public void setNunitcode(Integer nunitcode) {
		this.nunitcode = nunitcode;
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
	public Integer getNdefaultstatus() {
		return ndefaultstatus;
	}
	public void setNdefaultstatus(Integer ndefaultstatus) {
		this.ndefaultstatus = ndefaultstatus;
	}
	public String getSdescription() {
		return sdescription;
	}
	public void setSdescription(String sdescription) {
		this.sdescription = sdescription;
	}	
}