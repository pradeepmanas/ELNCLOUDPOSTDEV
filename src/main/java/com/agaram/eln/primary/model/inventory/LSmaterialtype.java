package com.agaram.eln.primary.model.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "LSmaterialtype")
public class LSmaterialtype {
	@Id
	@Column(name = "materialtypecode")
    private Integer nmaterialtypecode;
    @Column(name = "status")
    private Integer nstatus;
    @Column(name = "masterauditcode")
    private Integer nmasterauditcode;
    @Column(name = "sitecode")
	private Integer nsitecode;
    @Column(name = "defaultstatus")
	private Integer ndefaultstatus;
    
    //,columnDefinition = "varchar(100)"
    @Column(name = "materialtypename",columnDefinition = "varchar(100)")
    private String smaterialtypename;
    //,columnDefinition = "varchar(250)"
    @Column(name = "description",columnDefinition = "varchar(250)")
    private String sdescription;
    //,columnDefinition = "varchar(100)"
    @Column(name = "matnameids",columnDefinition = "varchar(100)")
    private String smatnameids;
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
	public Integer getNmasterauditcode() {
		return nmasterauditcode;
	}
	public void setNmasterauditcode(Integer nmasterauditcode) {
		this.nmasterauditcode = nmasterauditcode;
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
	public String getSdescription() {
		return sdescription;
	}
	public void setSdescription(String sdescription) {
		this.sdescription = sdescription;
	}
	public String getSmatnameids() {
		return smatnameids;
	}
	public void setSmatnameids(String smatnameids) {
		this.smatnameids = smatnameids;
	}
	
}
