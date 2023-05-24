package com.agaram.eln.primary.model.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LSmaterial")
public class LSmaterial {
	@Id
	@Column(name = "materialcode")private Integer nmaterialcode;
	@Column(name = "materialcatcode")private Integer nmaterialcatcode;
	@Column(name = "storagaeconditioncode")private Integer nstorageconditioncode;
	@Column(name = "unitcode")private Integer nunitcode;
	@Column(name = "qarentainstatus")private Integer nqarentainstatus;
	@Column(name = "openexpiry")private Integer nopenexpiry;
	@Column(name = "openexpiryperiodcode")private Integer nopenexpiryperiodcode;
	@Column(name = "nextvalidation")private Integer nnextvalidation;
	@Column(name = "nextvalidationperiodcode")private Integer nnextvalidationperiodcode;
	@Column(name = "expirypolicy")private Integer nexpirypolicy;
	@Column(name = "expirypolicyperiodcode")private Integer nexpirypolicyperiodcode;
	@Column(name = "status")private Integer nstatus;
	@Column(name = "masterauditcode")private Integer nmasterauditcode;
	@Column(name = "totalqty")private Integer ntotalqty;
	@Column(name = "reusable")private Integer nreusable;
	@Column(name = "noexpiry")private Integer nnoexpiry;
	@Column(name = "suppliercode")private Integer nsuppliercode;
	@Column(name = "materialtypecode")private Integer nmaterialtypecode;

	//,columnDefinition = "nvarchar(150)"
	@Column(name = "reorderlevel")
	private String nreorderlevel;
	//,columnDefinition = "nvarchar(150)"
	@Column(name = "materialname")
	private String smaterialname;
	//,columnDefinition = "nvarchar(max)"
	@Column(name = "description")
	private String sdescription;
	//,columnDefinition = "nvarchar(50)"
	@Column(name = "prefix")
	private String sprefix;
	//,columnDefinition = "nvarchar(50)"
	@Column(name ="erpcode")
	private String serpcode;
	//,columnDefinition = "nvarchar(150)"
	@Column(name ="manfactcatnum")
	private String smanfactcatnum;
	//,columnDefinition = "nvarchar(150)"
	@Column(name ="internalcatnum")
	private String sinternalcatnum;
	//,columnDefinition = "nvarchar(50)"
	@Column(name ="materialcatname")
	private String smaterialcatname;
	//,columnDefinition = "nvarchar(50)"
	@Column(name ="materialtypename")
	private String smaterialtypename;
	//,columnDefinition = "nvarchar(150)"
	@Column(name ="remarks")
	private String sremarks;
	
	public Integer getNmaterialcode() {
		return nmaterialcode;
	}
	public void setNmaterialcode(Integer nmaterialcode) {
		this.nmaterialcode = nmaterialcode;
	}
	public Integer getNmaterialcatcode() {
		return nmaterialcatcode;
	}
	public void setNmaterialcatcode(Integer nmaterialcatcode) {
		this.nmaterialcatcode = nmaterialcatcode;
	}
	public Integer getNunitcode() {
		return nunitcode;
	}
	public void setNunitcode(Integer nunitcode) {
		this.nunitcode = nunitcode;
	}
	public Integer getNqarentainstatus() {
		return nqarentainstatus;
	}
	public void setNqarentainstatus(Integer nqarentainstatus) {
		this.nqarentainstatus = nqarentainstatus;
	}
	
	public Integer getNstorageconditioncode() {
		return nstorageconditioncode;
	}
	public void setNstorageconditioncode(Integer nstorageconditioncode) {
		this.nstorageconditioncode = nstorageconditioncode;
	}
	public String getNreorderlevel() {
		return nreorderlevel;
	}
	public void setNreorderlevel(String nreorderlevel) {
		this.nreorderlevel = nreorderlevel;
	}
	public Integer getNopenexpiry() {
		return nopenexpiry;
	}
	public void setNopenexpiry(Integer nopenexpiry) {
		this.nopenexpiry = nopenexpiry;
	}
	public Integer getNopenexpiryperiodcode() {
		return nopenexpiryperiodcode;
	}
	public void setNopenexpiryperiodcode(Integer nopenexpiryperiodcode) {
		this.nopenexpiryperiodcode = nopenexpiryperiodcode;
	}
	public Integer getNnextvalidation() {
		return nnextvalidation;
	}
	public void setNnextvalidation(Integer nnextvalidation) {
		this.nnextvalidation = nnextvalidation;
	}
	public Integer getNnextvalidationperiodcode() {
		return nnextvalidationperiodcode;
	}
	public void setNnextvalidationperiodcode(Integer nnextvalidationperiodcode) {
		this.nnextvalidationperiodcode = nnextvalidationperiodcode;
	}
	public Integer getNexpirypolicy() {
		return nexpirypolicy;
	}
	public void setNexpirypolicy(Integer nexpirypolicy) {
		this.nexpirypolicy = nexpirypolicy;
	}
	public Integer getNexpirypolicyperiodcode() {
		return nexpirypolicyperiodcode;
	}
	public void setNexpirypolicyperiodcode(Integer nexpirypolicyperiodcode) {
		this.nexpirypolicyperiodcode = nexpirypolicyperiodcode;
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
	public Integer getNtotalqty() {
		return ntotalqty;
	}
	public void setNtotalqty(Integer ntotalqty) {
		this.ntotalqty = ntotalqty;
	}
	public Integer getNreusable() {
		return nreusable;
	}
	public void setNreusable(Integer nreusable) {
		this.nreusable = nreusable;
	}
	public Integer getNnoexpiry() {
		return nnoexpiry;
	}
	public void setNnoexpiry(Integer nnoexpiry) {
		this.nnoexpiry = nnoexpiry;
	}
	public Integer getNsuppliercode() {
		return nsuppliercode;
	}
	public void setNsuppliercode(Integer nsuppliercode) {
		this.nsuppliercode = nsuppliercode;
	}
	public Integer getNmaterialtypecode() {
		return nmaterialtypecode;
	}
	public void setNmaterialtypecode(Integer nmaterialtypecode) {
		this.nmaterialtypecode = nmaterialtypecode;
	}
	public String getSmaterialname() {
		return smaterialname;
	}
	public void setSmaterialname(String smaterialname) {
		this.smaterialname = smaterialname;
	}
	public String getSdescription() {
		return sdescription;
	}
	public void setSdescription(String sdescription) {
		this.sdescription = sdescription;
	}
	public String getSprefix() {
		return sprefix;
	}
	public void setSprefix(String sprefix) {
		this.sprefix = sprefix;
	}
	public String getSerpcode() {
		return serpcode;
	}
	public void setSerpcode(String serpcode) {
		this.serpcode = serpcode;
	}
	public String getSmanfactcatnum() {
		return smanfactcatnum;
	}
	public void setSmanfactcatnum(String smanfactcatnum) {
		this.smanfactcatnum = smanfactcatnum;
	}
	public String getSinternalcatnum() {
		return sinternalcatnum;
	}
	public void setSinternalcatnum(String sinternalcatnum) {
		this.sinternalcatnum = sinternalcatnum;
	}
	public String getSmaterialcatname() {
		return smaterialcatname;
	}
	public void setSmaterialcatname(String smaterialcatname) {
		this.smaterialcatname = smaterialcatname;
	}
	public String getSmaterialtypename() {
		return smaterialtypename;
	}
	public void setSmaterialtypename(String smaterialtypename) {
		this.smaterialtypename = smaterialtypename;
	}
	public String getSremarks() {
		return sremarks;
	}
	public void setSremarks(String sremarks) {
		this.sremarks = sremarks;
	}
}
