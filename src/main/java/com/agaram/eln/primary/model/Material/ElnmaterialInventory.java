package com.agaram.eln.primary.model.material;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name="elnmaterialinventory")
public class ElnmaterialInventory {
	
	@Id
	@Column(name = "nmaterialinventorycode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer nmaterialinventorycode;
	
	@ManyToOne
	private MaterialType materialtype;
	
	@ManyToOne
	private MaterialCategory materialcategory;
	
	@ManyToOne
	private Elnmaterial material;
	
	@ManyToOne
	private Unit unit;
	
	@ManyToOne
	private Section section;
	
	@ManyToOne
	private MaterialGrade materialgrade;
	
	@ManyToOne
	private Supplier supplier;
	
	@ManyToOne
	private Manufacturer manufacturer;
	
	@Column(name = "nstatus")
	private Integer nstatus;
	private Integer nsitecode;
	private Integer ntransactionstatus;
	
	@Column(name = "jsondata")
	private String jsondata;
	private String remarks;
	private String sinventoryid;
	private String savailablequantity;
	private String sbatchno;
	private String sreceivedquantity;

	private Boolean isexpiry;
	
	private Date expirydate;
	private Date createddate;
	private Date opendate;
	private Date manufacdate;
	private Date receiveddate;
	
	private Double nqtynotification;
	
	public String getSbatchno() {
		return sbatchno;
	}

	public void setSbatchno(String sbatchno) {
		this.sbatchno = sbatchno;
	}

	@OneToMany
	@JoinColumn(name = "nmaterialinventorycode")
	private List<LsOrderattachments> lsOrderattachments;
	
	@OneToMany
	@JoinColumn(name="ninventorycode")
	private List<ElnresultUsedMaterial> resultusedmaterial;	
	
	@ManyToOne
	private LSuserMaster createdby;
	
	@Transient
	private LScfttransaction objsilentaudit;
	
	@Transient
	public String info;	
	
	@Transient
	private LScfttransaction objmanualaudit;

	public Date getOpendate() {
		return opendate;
	}

	public void setOpendate(Date opendate) {
		this.opendate = opendate;
	}

	public LSuserMaster getCreatedby() {
		return createdby;
	}

	public void setCreatedby(LSuserMaster createdby) {
		this.createdby = createdby;
	}

	public Integer getNtransactionstatus() {
		return ntransactionstatus;
	}

	public void setNtransactionstatus(Integer ntransactionstatus) {
		this.ntransactionstatus = ntransactionstatus;
	}

	public MaterialGrade getMaterialgrade() {
		return materialgrade;
	}

	public void setMaterialgrade(MaterialGrade materialgrade) {
		this.materialgrade = materialgrade;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Date getManufacdate() {
		return manufacdate;
	}

	public void setManufacdate(Date manufacdate) {
		this.manufacdate = manufacdate;
	}

	public Date getReceiveddate() {
		return receiveddate;
	}

	public void setReceiveddate(Date receiveddate) {
		this.receiveddate = receiveddate;
	}

	public Integer getNmaterialinventorycode() {
		return nmaterialinventorycode;
	}

	public void setNmaterialinventorycode(Integer nmaterialinventorycode) {
		this.nmaterialinventorycode = nmaterialinventorycode;
	}

	public MaterialType getMaterialtype() {
		return materialtype;
	}

	public void setMaterialtype(MaterialType materialtype) {
		this.materialtype = materialtype;
	}

	public MaterialCategory getMaterialcategory() {
		return materialcategory;
	}

	public void setMaterialcategory(MaterialCategory materialcategory) {
		this.materialcategory = materialcategory;
	}

	public Elnmaterial getMaterial() {
		return material;
	}

	public void setMaterial(Elnmaterial material) {
		this.material = material;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public Integer getNstatus() {
		return nstatus;
	}

	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}

	public String getJsondata() {
		return jsondata;
	}

	public void setJsondata(String jsondata) {
		this.jsondata = jsondata;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSinventoryid() {
		return sinventoryid;
	}

	public void setSinventoryid(String sinventoryid) {
		this.sinventoryid = sinventoryid;
	}

	public String getSavailablequantity() {
		return savailablequantity;
	}

	public void setSavailablequantity(String savailablequantity) {
		this.savailablequantity = savailablequantity;
	}

	public String getSreceivedquantity() {
		return sreceivedquantity;
	}

	public void setSreceivedquantity(String sreceivedquantity) {
		this.sreceivedquantity = sreceivedquantity;
	}

	public Boolean getIsexpiry() {
		return isexpiry;
	}

	public void setIsexpiry(Boolean isexpiry) {
		this.isexpiry = isexpiry;
	}

	public Date getExpirydate() {
		return expirydate;
	}

	public void setExpirydate(Date expirydate) {
		this.expirydate = expirydate;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public List<LsOrderattachments> getLsOrderattachments() {
		return lsOrderattachments;
	}

	public void setLsOrderattachments(List<LsOrderattachments> lsOrderattachments) {
		this.lsOrderattachments = lsOrderattachments;
	}

	public Double getNqtynotification() {
		return nqtynotification;
	}

	public void setNqtynotification(Double nqtynotification) {
		this.nqtynotification = nqtynotification;
	}

	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}

	public Integer getNsitecode() {
		return nsitecode;
	}

	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}

	public List<ElnresultUsedMaterial> getResultusedmaterial() {
		return resultusedmaterial;
	}

	public void setResultusedmaterial(List<ElnresultUsedMaterial> resultusedmaterial) {
		this.resultusedmaterial = resultusedmaterial;
	}
}