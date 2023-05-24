package com.agaram.eln.primary.model.inventory;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
@Table(name = "LSmaterialinventory")
public class LSmaterialinventory {
	@Id
	@Column(name="materialinventorycode")
	private Integer nmaterialinventorycode;
	@Column(name="materialcode")
	private Integer nmaterialcode;	
	@Column(name="transactionstatus")
	private Integer ntransactionstatus;
	@Column(name="receivedqty")
	private String nreceivedqty;
	@Column(name="storagelocationcode")
	private Integer nstoragelocationcode;
	@Column(name="suppliercode")
	private Integer nsuppliercode;
	@Column(name="qtyreceived")
	private Integer nqtyreceived;
	@Column(name="unitcode")
	private Integer nunitcode;  
	@Column(name="cost")
	private Integer ncost;
	@Column(name="status")
	private Integer nstatus;
	@Column(name="reusablestatus")
	private Integer nreusablestatus;
	@Column(name="transauditcode")
	private Integer ntransauditcode;
	@Column(name="masterauditcode")
	private Integer nmasterauditcode;
	@Column(name="suborderno")
	private Integer nsuborderno;
	@Column(name="storagemethodcode")
	private Integer nstoragemethodcode;
	@Column(name="packagetypecode")
	private Integer npackagetypecode;
	@Column(name="projectcode")
	private Integer nprojectcode;
	@Column(name="taskcode")
	private Integer ntaskcode;
	@Column(name="sectioncode")
	private Integer nsectioncode;
	@Column(name="manufcode")
	private Integer nmanufcode;
	@Column(name="usagecount")
	private Integer nusagecount;
	@Column(name="materialgradecode")
	private Integer nmaterialgradecode;
	@Column(name="jqprojectcode")
	private Integer njqprojectcode;

	//columnDefinition = "nvarchar(100)",
	@Column(name="internalreferenceno")
	private String sinternalreferenceno;
	//columnDefinition = "nvarchar(100)",
	@Column(name="orderreferenceno")
	private String sorderreferenceno;
	//columnDefinition = "nvarchar(100)",
	@Column(name="containerid")
	private String scontainerid;
	//columnDefinition = "nvarchar(100)",
	@Column(name="batchno")
	private String sbatchno;
	//columnDefinition = "nvarchar(100)",
	@Column(name="lotno")
	private String slotno;
	//columnDefinition = "nvarchar(100)",
	@Column(name="inventorycode")
	private String sinventorycode;
	//columnDefinition = "nvarchar(100)",
	@Column(name="catalogno")
	private String scatalogno;
	//columnDefinition = "nvarchar(100)",
	@Column(name="invoiceno")
	private String sinvoiceno;
	//columnDefinition = "nvarchar(100)",
	@Column(name="internalprefix")
	private String sinternalprefix;
	//columnDefinition = "nvarchar(100)",
	@Column(name="remarks")
	private String sremarks;
	//columnDefinition = "nvarchar(100)",
	@Column(name="erpcode")
	private String serpcode;
	//columnDefinition = "nvarchar(100)",
	@Column(name="preparedby")
	private String spreparedby;

	//columnDefinition = "datetime",
	@Column(name="receiveddate")
	@Temporal(TemporalType.DATE)
	private Date dreceiveddate;
	//columnDefinition = "datetime",
	@Column(name="expirydate")
	@Temporal(TemporalType.DATE)
	private Date dexpirydate;
	//columnDefinition = "datetime",
	@Column(name="expirypolicydate")
	@Temporal(TemporalType.DATE)
	private Date dexpirypolicydate;	
	//columnDefinition = "datetime",
	@Column(name="manufacdate")
	@Temporal(TemporalType.DATE)
	private Date dmanufacdate;
	//columnDefinition = "datetime",
	@Column(name="opendate")
	@Temporal(TemporalType.DATE)
	private Date dopendate;
	//columnDefinition = "datetime",
	@Column(name="openexpirydate")
	@Temporal(TemporalType.DATE)
	private Date dopenexpirydate;
	//columnDefinition = "datetime",
	@Column(name="retestdate")
	@Temporal(TemporalType.DATE)
	private Date dretestdate;
	//columnDefinition = "datetime",
	@Column(name="releasedate")
	@Temporal(TemporalType.DATE)
	private Date dreleasedate;
	//columnDefinition = "datetime",
	@Column(name="retireddate")
	@Temporal(TemporalType.DATE)
	private Date dretireddate;
	//columnDefinition = "datetime",
	@Column(name="preparedate")
	@Temporal(TemporalType.DATE)
	private Date dpreparedate;

	transient private Integer quantityunit;
	transient private String quantityname;
	transient private String sectionname;
	transient private String transdisplaystatus;
//	transient private String dpreparedate;
//	transient private String dreceiveddate;
//	transient private String dexpirypolicydate;
//	transient private String dexpirydate;
//	transient private String dmanufacdate;
//	transient private String dopenexpirydate;
//	transient private String dretestdate;
//	transient private String dreleasedate;
//	transient private String dretireddate;
	public Integer getNmaterialinventorycode() {
		return nmaterialinventorycode;
	}
	public void setNmaterialinventorycode(Integer nmaterialinventorycode) {
		this.nmaterialinventorycode = nmaterialinventorycode;
	}
	public Integer getNmaterialcode() {
		return nmaterialcode;
	}
	public void setNmaterialcode(Integer nmaterialcode) {
		this.nmaterialcode = nmaterialcode;
	}
	public Integer getNtransactionstatus() {
		return ntransactionstatus;
	}
	public void setNtransactionstatus(Integer ntransactionstatus) {
		this.ntransactionstatus = ntransactionstatus;
	}
	public String getNreceivedqty() {
		return nreceivedqty;
	}
	public void setNreceivedqty(String nreceivedqty) {
		this.nreceivedqty = nreceivedqty;
	}
	public Integer getNstoragelocationcode() {
		return nstoragelocationcode;
	}
	public void setNstoragelocationcode(Integer nstoragelocationcode) {
		this.nstoragelocationcode = nstoragelocationcode;
	}
	public Integer getNsuppliercode() {
		return nsuppliercode;
	}
	public void setNsuppliercode(Integer nsuppliercode) {
		this.nsuppliercode = nsuppliercode;
	}
	public Integer getNqtyreceived() {
		return nqtyreceived;
	}
	public void setNqtyreceived(Integer nqtyreceived) {
		this.nqtyreceived = nqtyreceived;
	}
	public Integer getNunitcode() {
		return nunitcode;
	}
	public void setNunitcode(Integer nunitcode) {
		this.nunitcode = nunitcode;
	}
	public Integer getNcost() {
		return ncost;
	}
	public void setNcost(Integer ncost) {
		this.ncost = ncost;
	}
	public Integer getNstatus() {
		return nstatus;
	}
	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}
	public Integer getNreusablestatus() {
		return nreusablestatus;
	}
	public void setNreusablestatus(Integer nreusablestatus) {
		this.nreusablestatus = nreusablestatus;
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
	public Integer getNsuborderno() {
		return nsuborderno;
	}
	public void setNsuborderno(Integer nsuborderno) {
		this.nsuborderno = nsuborderno;
	}
	public Integer getNstoragemethodcode() {
		return nstoragemethodcode;
	}
	public void setNstoragemethodcode(Integer nstoragemethodcode) {
		this.nstoragemethodcode = nstoragemethodcode;
	}
	public Integer getNpackagetypecode() {
		return npackagetypecode;
	}
	public void setNpackagetypecode(Integer npackagetypecode) {
		this.npackagetypecode = npackagetypecode;
	}
	public Integer getNprojectcode() {
		return nprojectcode;
	}
	public void setNprojectcode(Integer nprojectcode) {
		this.nprojectcode = nprojectcode;
	}
	public Integer getNtaskcode() {
		return ntaskcode;
	}
	public void setNtaskcode(Integer ntaskcode) {
		this.ntaskcode = ntaskcode;
	}
	public Integer getNsectioncode() {
		return nsectioncode;
	}
	public void setNsectioncode(Integer nsectioncode) {
		this.nsectioncode = nsectioncode;
	}
	public Integer getNmanufcode() {
		return nmanufcode;
	}
	public void setNmanufcode(Integer nmanufcode) {
		this.nmanufcode = nmanufcode;
	}
	public Integer getNusagecount() {
		return nusagecount;
	}
	public void setNusagecount(Integer nusagecount) {
		this.nusagecount = nusagecount;
	}
	public Integer getNmaterialgradecode() {
		return nmaterialgradecode;
	}
	public void setNmaterialgradecode(Integer nmaterialgradecode) {
		this.nmaterialgradecode = nmaterialgradecode;
	}
	public Integer getNjqprojectcode() {
		return njqprojectcode;
	}
	public void setNjqprojectcode(Integer njqprojectcode) {
		this.njqprojectcode = njqprojectcode;
	}
	public String getSinternalreferenceno() {
		return sinternalreferenceno;
	}
	public void setSinternalreferenceno(String sinternalreferenceno) {
		this.sinternalreferenceno = sinternalreferenceno;
	}
	public String getSorderreferenceno() {
		return sorderreferenceno;
	}
	public void setSorderreferenceno(String sorderreferenceno) {
		this.sorderreferenceno = sorderreferenceno;
	}
	public String getScontainerid() {
		return scontainerid;
	}
	public void setScontainerid(String scontainerid) {
		this.scontainerid = scontainerid;
	}
	public String getSbatchno() {
		return sbatchno;
	}
	public void setSbatchno(String sbatchno) {
		this.sbatchno = sbatchno;
	}
	public String getSlotno() {
		return slotno;
	}
	public void setSlotno(String slotno) {
		this.slotno = slotno;
	}
	public String getSinventorycode() {
		return sinventorycode;
	}
	public void setSinventorycode(String sinventorycode) {
		this.sinventorycode = sinventorycode;
	}
	public String getScatalogno() {
		return scatalogno;
	}
	public void setScatalogno(String scatalogno) {
		this.scatalogno = scatalogno;
	}
	public String getSinvoiceno() {
		return sinvoiceno;
	}
	public void setSinvoiceno(String sinvoiceno) {
		this.sinvoiceno = sinvoiceno;
	}
	public String getSinternalprefix() {
		return sinternalprefix;
	}
	public void setSinternalprefix(String sinternalprefix) {
		this.sinternalprefix = sinternalprefix;
	}
	public String getSremarks() {
		return sremarks;
	}
	public void setSremarks(String sremarks) {
		this.sremarks = sremarks;
	}
	public String getSerpcode() {
		return serpcode;
	}
	public void setSerpcode(String serpcode) {
		this.serpcode = serpcode;
	}
	public String getSpreparedby() {
		return spreparedby;
	}
	public void setSpreparedby(String spreparedby) {
		this.spreparedby = spreparedby;
	}
	
	public Date getDreceiveddate() {
		return dreceiveddate;
	}
	public void setDreceiveddate(Date dreceiveddate) {
		this.dreceiveddate = dreceiveddate;
	}
	public Date getDexpirydate() {
		return dexpirydate;
	}
	public void setDexpirydate(Date dexpirydate) {
		this.dexpirydate = dexpirydate;
	}
	public Date getDexpirypolicydate() {
		return dexpirypolicydate;
	}
	public void setDexpirypolicydate(Date dexpirypolicydate) {
		this.dexpirypolicydate = dexpirypolicydate;
	}
	public Date getDmanufacdate() {
		return dmanufacdate;
	}
	public void setDmanufacdate(Date dmanufacdate) {
		this.dmanufacdate = dmanufacdate;
	}
	public Date getDopendate() {
		return dopendate;
	}
	public void setDopendate(Date dopendate) {
		this.dopendate = dopendate;
	}
	public Date getDopenexpirydate() {
		return dopenexpirydate;
	}
	public void setDopenexpirydate(Date dopenexpirydate) {
		this.dopenexpirydate = dopenexpirydate;
	}
	public Date getDretestdate() {
		return dretestdate;
	}
	public void setDretestdate(Date dretestdate) {
		this.dretestdate = dretestdate;
	}
	public Date getDreleasedate() {
		return dreleasedate;
	}
	public void setDreleasedate(Date dreleasedate) {
		this.dreleasedate = dreleasedate;
	}
	public Date getDretireddate() {
		return dretireddate;
	}
	public void setDretireddate(Date dretireddate) {
		this.dretireddate = dretireddate;
	}
	public Date getDpreparedate() {
		return dpreparedate;
	}
	public void setDpreparedate(Date dpreparedate) {
		this.dpreparedate = dpreparedate;
	}
	public Integer getQuantityunit() {
		return quantityunit;
	}
	public void setQuantityunit(Integer quantityunit) {
		this.quantityunit = quantityunit;
	}
	public String getQuantityname() {
		return quantityname;
	}
	public void setQuantityname(String quantityname) {
		this.quantityname = quantityname;
	}
	public String getSectionname() {
		return sectionname;
	}
	public void setSectionname(String sectionname) {
		this.sectionname = sectionname;
	}
	public String getTransdisplaystatus() {
		return transdisplaystatus;
	}
	public void setTransdisplaystatus(String transdisplaystatus) {
		this.transdisplaystatus = transdisplaystatus;
	}
		
}
