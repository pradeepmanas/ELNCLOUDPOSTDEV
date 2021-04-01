package com.agaram.eln.primary.model.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LSmanufacturer")
public class LSmanufacturer {
	@Id
	
	@Column(name="manufcode")
	private Integer nmanufcode;
	@Column(name="status")
	private Integer nstatus;
	@Column(name="transactionstatus")
	private Integer ntransactionstatus;
	@Column(name="masterauditcode")
	private Integer nmasterauditcode;
	@Column(name="sitecode")
	private Integer nsitecode;

	
	@Column(columnDefinition = "varchar(50)",name="manufname")
	private String smanufname;
	
	@Column(columnDefinition = "varchar(250)",name="description")
	private String sdescription;
		
	transient private String manufsitename;
	transient private String displaystatus;
	transient private String officialmanufname;
	transient private String transdisplaystatus;
	transient private String productgroupname;

	transient private Integer productmanufcode;
	transient private Integer manufsitecode;
	
	
	public Integer getNmanufcode() {
		return nmanufcode;
	}
	public void setNmanufcode(Integer nmanufcode) {
		this.nmanufcode = nmanufcode;
	}
	public Integer getNstatus() {
		return nstatus;
	}
	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}
	public Integer getNtransactionstatus() {
		return ntransactionstatus;
	}
	public void setNtransactionstatus(Integer ntransactionstatus) {
		this.ntransactionstatus = ntransactionstatus;
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
	public String getSmanufname() {
		return smanufname;
	}
	public void setSmanufname(String smanufname) {
		this.smanufname = smanufname;
	}
	public String getSdescription() {
		return sdescription;
	}
	public void setSdescription(String sdescription) {
		this.sdescription = sdescription;
	}
	public String getManufsitename() {
		return manufsitename;
	}
	public void setManufsitename(String manufsitename) {
		this.manufsitename = manufsitename;
	}
	public String getDisplaystatus() {
		return displaystatus;
	}
	public void setDisplaystatus(String displaystatus) {
		this.displaystatus = displaystatus;
	}
	public Integer getManufsitecode() {
		return manufsitecode;
	}
	public void setManufsitecode(Integer manufsitecode) {
		this.manufsitecode = manufsitecode;
	}
	public String getOfficialmanufname() {
		return officialmanufname;
	}
	public void setOfficialmanufname(String officialmanufname) {
		this.officialmanufname = officialmanufname;
	}
	public String getTransdisplaystatus() {
		return transdisplaystatus;
	}
	public void setTransdisplaystatus(String transdisplaystatus) {
		this.transdisplaystatus = transdisplaystatus;
	}
	public String getProductgroupname() {
		return productgroupname;
	}
	public void setProductgroupname(String productgroupname) {
		this.productgroupname = productgroupname;
	}
	public Integer getProductmanufcode() {
		return productmanufcode;
	}
	public void setProductmanufcode(Integer productmanufcode) {
		this.productmanufcode = productmanufcode;
	}
	
	
}
