package com.agaram.eln.primary.model.Material;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Lsmaterialcategorytype")
public class Lsmaterialcategory {
	
	@Id
	@Column(name = "materialtypecode")
    private Integer nmaterialtypecode;
    @Column(name = "status")
    private Integer nstatus;
    @Column(name = "sitecode")
	private Integer nsitecode;
    @Column(name = "defaultstatus")
	private Integer ndefaultstatus;
    @Column(name = "materialtypename",columnDefinition = "varchar(100)")
    private String smaterialtypename;
	public Integer getNmaterialtypecode() {
		return nmaterialtypecode;
	}
	public void setNmaterialtypecode(Integer nmaterialtypecode) {
		this.nmaterialtypecode = nmaterialtypecode;
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
	public String getSmaterialtypename() {
		return smaterialtypename;
	}
	public void setSmaterialtypename(String smaterialtypename) {
		this.smaterialtypename = smaterialtypename;
	}
}
