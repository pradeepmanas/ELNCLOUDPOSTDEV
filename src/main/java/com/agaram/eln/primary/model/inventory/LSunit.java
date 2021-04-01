package com.agaram.eln.primary.model.inventory;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LSunit")
public class LSunit {
	@Id
	@Column(name = "unitcode")
	private Integer nunitcode;
	@Column(name = "status")
	private Integer nstatus;
	@Column(name = "masterauditcode")
	private Integer nmasterauditcode;
	@Column(name = "sitecode")
	private Integer nsitecode;
	@Column(name = "defaultstatus")
	private Integer ndefaultstatus;
	
	//columnDefinition = "varchar(100)",
	@Column(name = "unitname",columnDefinition = "varchar(100)")
	private String sunitname;
	//columnDefinition = "varchar(max)",
	@Column(name = "description",columnDefinition = "text")
	private String sdescription;
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
	public String getSunitname() {
		return sunitname;
	}
	public void setSunitname(String sunitname) {
		this.sunitname = sunitname;
	}
	public String getSdescription() {
		return sdescription;
	}
	public void setSdescription(String sdescription) {
		this.sdescription = sdescription;
	} 
}
