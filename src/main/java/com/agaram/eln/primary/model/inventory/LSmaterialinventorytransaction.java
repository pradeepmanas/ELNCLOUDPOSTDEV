package com.agaram.eln.primary.model.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "LSmaterialinventorytransaction")
public class LSmaterialinventorytransaction {
	@Id
	@Column(name="materialinventtranscode")
	private Integer nmaterialinventtranscode;
	@Column(name="materialinventorycode")
	private Integer nmaterialinventorycode;
//	@Column(name="qtyreceived")
//	private Integer nqtyreceived;
//	@Column(name="qtyissued")
//	private Integer nqtyissued;
//	@Column(name="amountleft")
//	private Integer namountleft;
	@Column(name="transactiontype")
	private Integer ntransactiontype;
	@Column(name="status")
	private Integer nstatus;
	@Column(name="transauditcode")
	private Integer ntransauditcode;
	@Column(name="masterauditcode")
	private Integer nmasterauditcode;
	@Column(name="preregno")
	private Integer npreregno;
	@Column(name="inventorytranscode")
	private Integer ninventorytranscode;
	@Column(name="sectioncode")
	private Integer nsectioncode;
	@Column(name="sitecode")
	private Integer nsitecode;
	@Column(name="resultusedmaterialcode")
	private Integer nresultusedmaterialcode;

	//,columnDefinition="varchar(50)"
	@Column(name="qtyreceived",columnDefinition="varchar(50)")
	private String nqtyreceived;
	//,columnDefinition="varchar(50)"
	@Column(name="qtyissued",columnDefinition="varchar(50)")
	private String nqtyissued;
	//,columnDefinition="varchar(50)"
	@Column(name="amountleft",columnDefinition="varchar(50)")
	private String namountleft;
	//columnDefinition = "varchar(120)",
	@Column(name="arno",columnDefinition="varchar(120)")
	private String sarno;
	//columnDefinition = "varchar(250)",
	@Column(name="description",columnDefinition="varchar(250)")
	private String sdescription;
	//columnDefinition = "datetime",
	@Column(name="transactiondate",columnDefinition="date")
	private String dtransactiondate;
	//columnDefinition = "datetime",
	@Column(columnDefinition = "date",name="opendate")
	private String dopendate;
	//columnDefinition = "datetime",
	@Column(columnDefinition = "date",name="openexpirydate")
	private String dopenexpirydate;

	transient private Integer availableqty;
	transient private String stransactiondate;
	transient private String transdisplaystatus;
	transient private String inventorytypename;
	transient private String sectionname;
	transient private String inventorycode;
	
	public Integer getNmaterialinventtranscode() {
		return nmaterialinventtranscode;
	}
	public void setNmaterialinventtranscode(Integer nmaterialinventtranscode) {
		this.nmaterialinventtranscode = nmaterialinventtranscode;
	}
	public Integer getNmaterialinventorycode() {
		return nmaterialinventorycode;
	}
	public void setNmaterialinventorycode(Integer nmaterialinventorycode) {
		this.nmaterialinventorycode = nmaterialinventorycode;
	}
//	public Integer getNqtyreceived() {
//		return nqtyreceived;
//	}
//	public void setNqtyreceived(Integer nqtyreceived) {
//		this.nqtyreceived = nqtyreceived;
//	}
//	public Integer getNqtyissued() {
//		return nqtyissued;
//	}
//	public void setNqtyissued(Integer nqtyissued) {
//		this.nqtyissued = nqtyissued;
//	}
//	public Integer getNamountleft() {
//		return namountleft;
//	}
//	public void setNamountleft(Integer namountleft) {
//		this.namountleft = namountleft;
//	}
	public Integer getNtransactiontype() {
		return ntransactiontype;
	}
	public String getNqtyreceived() {
		return nqtyreceived;
	}
	public void setNqtyreceived(String nqtyreceived) {
		this.nqtyreceived = nqtyreceived;
	}
	public String getNqtyissued() {
		return nqtyissued;
	}
	public void setNqtyissued(String nqtyissued) {
		this.nqtyissued = nqtyissued;
	}
	public String getNamountleft() {
		return namountleft;
	}
	public void setNamountleft(String namountleft) {
		this.namountleft = namountleft;
	}
	public void setNtransactiontype(Integer ntransactiontype) {
		this.ntransactiontype = ntransactiontype;
	}
	public Integer getNstatus() {
		return nstatus;
	}
	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}
	public Integer getNtransauditcode() {
		return ntransauditcode;
	}
	public void setNtransauditcode(Integer ntransauditcode) {
		this.ntransauditcode = ntransauditcode;
	}
	public Integer getNmasterauditcode() {
		return nmasterauditcode;
	}
	public void setNmasterauditcode(Integer nmasterauditcode) {
		this.nmasterauditcode = nmasterauditcode;
	}
	public Integer getNpreregno() {
		return npreregno;
	}
	public void setNpreregno(Integer npreregno) {
		this.npreregno = npreregno;
	}
	public Integer getNinventorytranscode() {
		return ninventorytranscode;
	}
	public void setNinventorytranscode(Integer ninventorytranscode) {
		this.ninventorytranscode = ninventorytranscode;
	}
	public Integer getNsectioncode() {
		return nsectioncode;
	}
	public void setNsectioncode(Integer nsectioncode) {
		this.nsectioncode = nsectioncode;
	}
	public Integer getNsitecode() {
		return nsitecode;
	}
	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}
	public Integer getNresultusedmaterialcode() {
		return nresultusedmaterialcode;
	}
	public void setNresultusedmaterialcode(Integer nresultusedmaterialcode) {
		this.nresultusedmaterialcode = nresultusedmaterialcode;
	}
	public String getSarno() {
		return sarno;
	}
	public void setSarno(String sarno) {
		this.sarno = sarno;
	}
	public String getSdescription() {
		return sdescription;
	}
	public void setSdescription(String sdescription) {
		this.sdescription = sdescription;
	}
	public String getDtransactiondate() {
		return dtransactiondate;
	}
	public void setDtransactiondate(String dtransactiondate) {
		this.dtransactiondate = dtransactiondate;
	}
	public String getDopendate() {
		return dopendate;
	}
	public void setDopendate(String dopendate) {
		this.dopendate = dopendate;
	}
	public String getDopenexpirydate() {
		return dopenexpirydate;
	}
	public void setDopenexpirydate(String dopenexpirydate) {
		this.dopenexpirydate = dopenexpirydate;
	}
	public Integer getAvailableqty() {
		return availableqty;
	}
	public void setAvailableqty(Integer availableqty) {
		this.availableqty = availableqty;
	}
	public String getStransactiondate() {
		return stransactiondate;
	}
	public void setStransactiondate(String stransactiondate) {
		this.stransactiondate = stransactiondate;
	}
	public String getTransdisplaystatus() {
		return transdisplaystatus;
	}
	public void setTransdisplaystatus(String transdisplaystatus) {
		this.transdisplaystatus = transdisplaystatus;
	}
	public String getInventorytypename() {
		return inventorytypename;
	}
	public void setInventorytypename(String inventorytypename) {
		this.inventorytypename = inventorytypename;
	}
	public String getSectionname() {
		return sectionname;
	}
	public void setSectionname(String sectionname) {
		this.sectionname = sectionname;
	}
	public String getInventorycode() {
		return inventorycode;
	}
	public void setInventorycode(String inventorycode) {
		this.inventorycode = inventorycode;
	}
    
    
}
