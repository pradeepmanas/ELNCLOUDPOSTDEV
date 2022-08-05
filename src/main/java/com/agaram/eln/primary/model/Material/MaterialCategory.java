package com.agaram.eln.primary.model.Material;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "materialcategory")
public class MaterialCategory implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "nmaterialcatcode")
	private Integer nmaterialcatcode;

	@Column(name = "nmaterialtypecode", nullable = false)
	private Integer nmaterialtypecode = -1;

	@Column(name = "needSectionwise")
	private short needSectionwise;

	@Column(name = "nuserrolecode", nullable = false)
	private int nuserrolecode;

	@ColumnDefault("-1")
	@Column(name = "nbarcode", nullable = false)
	private int nbarcode = -1;

	@ColumnDefault("4")
	@Column(name = "ncategorybasedflow", nullable = false)
	private short ncategorybasedflow = 4;

	@ColumnDefault("1")
	@Column(name = "nactivestatus", nullable = false)
	private short nactivestatus;

	@Column(name = "smaterialcatname", length = 100, nullable = false)
	private String smaterialcatname;

	@Column(name = "sdescription", length = 255)
	private String sdescription;

	@ColumnDefault("4")
	@Column(name = "ndefaultstatus", nullable = false)
	private short ndefaultstatus = 4;

	@ColumnDefault("-1")
	@Column(name = "nsitecode", nullable = false)
	private short nsitecode = -1;

	@ColumnDefault("1")
	@Column(name = "nstatus", nullable = false)
	private short nstatus = 1;
	private transient String sneedSectionwise;

	private transient String sdisplaystatus;

	private transient String sbarcodename;
	private transient int nsuppliercode;
	private transient int nsuppliermatrixcode;
	private transient String scategorybasedflow;
	private transient String smaterialtypename;
	private transient boolean needSectionwisedisabled;
	private transient int nproductcatcode;
	private transient String sproductcatname;


	public int getNsuppliercode() {
		return nsuppliercode;
	}

	public String getScategorybasedflow() {
		return scategorybasedflow;
	}

	public void setScategorybasedflow(String scategorybasedflow) {
		this.scategorybasedflow = scategorybasedflow;
	}

	public void setNsuppliercode(int nsuppliercode) {
		this.nsuppliercode = nsuppliercode;
	}

	public int getNsuppliermatrixcode() {
		return nsuppliermatrixcode;
	}

	public void setNsuppliermatrixcode(int nsuppliermatrixcode) {
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

	public short getNeedSectionwise() {
		return needSectionwise;
	}

	public void setNeedSectionwise(short needSectionwise) {
		this.needSectionwise = needSectionwise;
	}

	public int getNuserrolecode() {
		return nuserrolecode;
	}

	public void setNuserrolecode(int nuserrolecode) {
		this.nuserrolecode = nuserrolecode;
	}

	public int getNbarcode() {
		return nbarcode;
	}

	public void setNbarcode(int nbarcode) {
		this.nbarcode = nbarcode;
	}

	public short getNcategorybasedflow() {
		return ncategorybasedflow;
	}

	public void setNcategorybasedflow(short ncategorybasedflow) {
		this.ncategorybasedflow = ncategorybasedflow;
	}

	public short getNactivestatus() {
		return nactivestatus;
	}

	public void setNactivestatus(short nactivestatus) {
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

	public short getNdefaultstatus() {
		return ndefaultstatus;
	}

	public void setNdefaultstatus(short ndefaultstatus) {
		this.ndefaultstatus = ndefaultstatus;
	}

	public short getNsitecode() {
		return nsitecode;
	}

	public void setNsitecode(short nsitecode) {
		this.nsitecode = nsitecode;
	}

	public short getNstatus() {
		return nstatus;
	}

	public void setNstatus(short nstatus) {
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

	public int getNproductcatcode() {
		return nproductcatcode;
	}

	public void setNproductcatcode(int nproductcatcode) {
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
