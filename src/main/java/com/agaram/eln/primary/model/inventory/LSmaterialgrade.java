package com.agaram.eln.primary.model.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "LSmaterialgrade")
public class LSmaterialgrade {
	@Id
	@Column(name = "materialgradecode")
	private Integer nmaterialgradecode;
	@Column(name = "masterauditcode")
	private Integer nmasterauditcode;
	@Column(name = "defaultstatus")
	private Integer ndefaultstatus;
	@Column(name = "sitecode")
	private Integer nsitecode;
	@Column(name = "status")
	private Integer nstatus;
	
	//columnDefinition = "nvarchar(max)",
	@Column(name = "description")
	private String sdescription;
	//,columnDefinition = "nvarchar(20)"
	@Column(name = "materialgradename")
	private String smaterialgradename;
	
	public Integer getNmaterialgradecode() {
		return nmaterialgradecode;
	}
	public void setNmaterialgradecode(Integer nmaterialgradecode) {
		this.nmaterialgradecode = nmaterialgradecode;
	}
	public Integer getNmasterauditcode() {
		return nmasterauditcode;
	}
	public void setNmasterauditcode(Integer nmasterauditcode) {
		this.nmasterauditcode = nmasterauditcode;
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
	
	public String getSdescription() {
		return sdescription;
	}
	public void setSdescription(String sdescription) {
		this.sdescription = sdescription;
	}
	public String getSmaterialgradename() {
		return smaterialgradename;
	}
	public void setSmaterialgradename(String smaterialgradename) {
		this.smaterialgradename = smaterialgradename;
	}
	
}
