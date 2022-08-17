package com.agaram.eln.primary.model.material;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "materialcategory")
public class MaterialCategory implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nmaterialcatcode")
	private Integer nmaterialcatcode;

	@Column(name = "nmaterialtypecode", nullable = false)
	private Integer nmaterialtypecode = -1;

	@Column(name = "needSectionwise")
	private Integer needSectionwise;

	@Column(name = "nuserrolecode", nullable = false)
	private Integer nuserrolecode;

	@ColumnDefault("-1")
	@Column(name = "nbarcode", nullable = false)
	private Integer nbarcode = -1;

	@ColumnDefault("4")
	@Column(name = "ncategorybasedflow", nullable = false)
	private Integer ncategorybasedflow = 4;

	@ColumnDefault("1")
	@Column(name = "nactivestatus", nullable = false)
	private Integer nactivestatus;

	@Column(name = "smaterialcatname", length = 100, nullable = false)
	private String smaterialcatname;

	@Column(name = "sdescription", length = 255)
	private String sdescription;

	@ColumnDefault("4")
	@Column(name = "ndefaultstatus", nullable = false)
	private Integer ndefaultstatus = 4;

	@ColumnDefault("-1")
	@Column(name = "nsitecode", nullable = false)
	private Integer nsitecode = -1;

	@ColumnDefault("1")
	@Column(name = "nstatus", nullable = false)
	private Integer nstatus = 1;
	private transient String sneedSectionwise;

	private transient String sdisplaystatus;

	private transient String sbarcodename;
	private transient Integer nsuppliercode;
	private transient Integer nsuppliermatrixcode;
	private transient String scategorybasedflow;
	private transient String smaterialtypename;
	private transient boolean needSectionwisedisabled;
	private transient Integer nproductcatcode;
	private transient String sproductcatname;


	public Integer getNsuppliercode() {
		return nsuppliercode;
	}

	public String getScategorybasedflow() {
		return scategorybasedflow;
	}

	public void setScategorybasedflow(String scategorybasedflow) {
		this.scategorybasedflow = scategorybasedflow;
	}

	public void setNsuppliercode(Integer nsuppliercode) {
		this.nsuppliercode = nsuppliercode;
	}

	public Integer getNsuppliermatrixcode() {
		return nsuppliermatrixcode;
	}

	public void setNsuppliermatrixcode(Integer nsuppliermatrixcode) {
		this.nsuppliermatrixcode = nsuppliermatrixcode;
	}

	public Integer getNmaterialcatcode() {
		return nmaterialcatcode;
	}

	public void setNmaterialcatcode(Integer nmaterialcatcode) {
		this.nmaterialcatcode = nmaterialcatcode;
	}

	public Integer getNmaterialtypecode() {
		return nmaterialtypecode;
	}

	public void setNmaterialtypecode(Integer nmaterialtypecode) {
		this.nmaterialtypecode = nmaterialtypecode;
	}

	public Integer getNeedSectionwise() {
		return needSectionwise;
	}

	public void setNeedSectionwise(Integer needSectionwise) {
		this.needSectionwise = needSectionwise;
	}

	public Integer getNuserrolecode() {
		return nuserrolecode;
	}

	public void setNuserrolecode(Integer nuserrolecode) {
		this.nuserrolecode = nuserrolecode;
	}

	public Integer getNbarcode() {
		return nbarcode;
	}

	public void setNbarcode(Integer nbarcode) {
		this.nbarcode = nbarcode;
	}

	public Integer getNcategorybasedflow() {
		return ncategorybasedflow;
	}

	public void setNcategorybasedflow(Integer ncategorybasedflow) {
		this.ncategorybasedflow = ncategorybasedflow;
	}

	public Integer getNactivestatus() {
		return nactivestatus;
	}

	public void setNactivestatus(Integer nactivestatus) {
		this.nactivestatus = nactivestatus;
	}

	public String getSmaterialcatname() {
		return smaterialcatname;
	}

	public void setSmaterialcatname(String smaterialcatname) {
		this.smaterialcatname = smaterialcatname;
	}

	public String getSdescription() {
		return sdescription;
	}

	public void setSdescription(String sdescription) {
		this.sdescription = sdescription;
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

	public String getSneedSectionwise() {
		return sneedSectionwise;
	}

	public void setSneedSectionwise(String sneedSectionwise) {
		this.sneedSectionwise = sneedSectionwise;
	}

	public String getSdisplaystatus() {
		return sdisplaystatus;
	}

	public void setSdisplaystatus(String sdisplaystatus) {
		this.sdisplaystatus = sdisplaystatus;
	}

	public String getSbarcodename() {
		return sbarcodename;
	}

	public void setSbarcodename(String sbarcodename) {
		this.sbarcodename = sbarcodename;
	}

	public String getSmaterialtypename() {
		return smaterialtypename;
	}

	public void setSmaterialtypename(String smaterialtypename) {
		this.smaterialtypename = smaterialtypename;
	}

	public boolean isNeedSectionwisedisabled() {
		return needSectionwisedisabled;
	}

	public void setNeedSectionwisedisabled(boolean needSectionwisedisabled) {
		this.needSectionwisedisabled = needSectionwisedisabled;
	}

	public Integer getNproductcatcode() {
		return nproductcatcode;
	}

	public void setNproductcatcode(Integer nproductcatcode) {
		this.nproductcatcode = nproductcatcode;
	}

	public String getSproductcatname() {
		return sproductcatname;
	}

	public void setSproductcatname(String sproductcatname) {
		this.sproductcatname = sproductcatname;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
