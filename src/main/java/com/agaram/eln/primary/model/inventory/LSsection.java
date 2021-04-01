package com.agaram.eln.primary.model.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LSsection")
public class LSsection {

	@Id
	@Column(name = "sectioncode")
	private Integer nsectioncode;
	
	//,columnDefinition = "varchar(100)"
	@Column(name = "sectionname",columnDefinition = "varchar(120)")
	private String ssectionname;
	
	//,columnDefinition = "varchar(250)"
	@Column(name = "description",columnDefinition = "varchar(250)")
	private String sdescription;
	
	@Column(name = "status")
	private Integer nstatus;
	
	@Column(name = "masterauditcode")
	private Integer nmasterauditcode;
	
	//,columnDefinition = "varchar(120)"
	@Column(name = "erpcode",columnDefinition = "varchar(120)")
	private String serpcode;
	
	@Column(name = "defaultstatus")
	private Integer ndefaultstatus;
	
	@Column(name = "sitecode")
	private Integer nsitecode;
	
	@Column(name = "sectiontypecode")
	private Integer nsectiontypecode;
	

	public Integer getNsectiontypecode() {
		return nsectiontypecode;
	}

	public void setNsectiontypecode(Integer nsectiontypecode) {
		this.nsectiontypecode = nsectiontypecode;
	}

	public Integer getNsectioncode() {
		return nsectioncode;
	}

	public void setNsectioncode(Integer nsectioncode) {
		this.nsectioncode = nsectioncode;
	}

	public String getSsectionname() {
		return ssectionname;
	}

	public void setSsectionname(String ssectionname) {
		this.ssectionname = ssectionname;
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

	public String getSerpcode() {
		return serpcode;
	}

	public void setSerpcode(String serpcode) {
		this.serpcode = serpcode;
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
	
}
