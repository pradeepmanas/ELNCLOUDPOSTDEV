package com.agaram.eln.primary.model.material;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.samplestoragelocation.SelectedInventoryMapped;

@Entity
@Table(name="materialinventory")
public class MaterialInventory {
	
	@Id
	@Column(name = "nmaterialinventorycode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer nmaterialinventorycode;
	
	@Column(name = "nmaterialcode")
	private Integer nmaterialcode;
	
	@Column(name = "nmaterialcatcode")
	private Integer nmaterialcatcode;
	
	@Column(name = "nmaterialtypecode")
	private Integer nmaterialtypecode;
	
	private Integer nsectioncode;
	private Integer ntransactionstatus;
	
	@Column(name = "nstatus")
	private Integer nstatus;
	
	@Column(name = "jsondata")
	private String jsondata;
	
	@Column(name = "jsonuidata")
	private String jsonuidata;
	
	private transient String sinventoryid;
	private transient String savailablequatity;
	private transient String sunitname;
	
	private Boolean isexpiryneed;
	@Temporal(TemporalType.TIMESTAMP)
	private Date expirydate;
	
	public Boolean getIsexpiryneed() {
		return isexpiryneed;
	}
	public void setIsexpiryneed(Boolean isexpiryneed) {
		this.isexpiryneed = isexpiryneed;
	}
	public Date getExpirydate() {
		return expirydate;
	}
	public void setExpirydate(Date expirydate) {
		this.expirydate = expirydate;
	}

	private Boolean validationneed;
	@Temporal(TemporalType.TIMESTAMP)
	private Date validationdate;
	
	public Boolean getValidationneed() {
		return validationneed;
	}
	public void setValidationneed(Boolean validationneed) {
		this.validationneed = validationneed;
	}
	public Date getValidationdate() {
		return validationdate;
	}
	public void setValidationdate(Date validationdate) {
		this.validationdate = validationdate;
	}
	
	@Transient
	private LScfttransaction objsilentaudit;
	
	@Transient
	public String info;
	
	@OneToMany
	@JoinColumn(name = "nmaterialinventorycode")
	private List<LsOrderattachments> lsOrderattachments;	
	
	
	public List<LsOrderattachments> getLsOrderattachments() {
		return lsOrderattachments;
	}
	public void setLsOrderattachments(List<LsOrderattachments> lsOrderattachments) {
		this.lsOrderattachments = lsOrderattachments;
	}
	
	private Double nqtynotification;
	
	public Double getNqtynotification() {
		return nqtynotification;
	}
	public void setNqtynotification(Double nqtynotification) {
		this.nqtynotification = nqtynotification;
	}
	
	@Transient
	private LScfttransaction objmanualaudit;
	
	private Integer nsitecode;
	
	public Integer getNsitecode() {
		return nsitecode;
	}
	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}
	
	@OneToMany
	@JoinColumn(name="nmaterialinventorycode")
	private List<MaterialInventoryTransaction> materialInventoryTransactions;
	
	public List<MaterialInventoryTransaction> getMaterialInventoryTransactions() {
		return materialInventoryTransactions;
	}
	public void setMaterialInventoryTransactions(List<MaterialInventoryTransaction> materialInventoryTransactions) {
		this.materialInventoryTransactions = materialInventoryTransactions;
	}
	
	@OneToMany
	@JoinColumn(name="nmaterialinventorycode")
	private  List<SelectedInventoryMapped> selectedinventorymapped;
	
	public List<SelectedInventoryMapped> getSelectedinventorymapped() {
		return selectedinventorymapped;
	}
	public void setSelectedinventorymapped(List<SelectedInventoryMapped> selectedinventorymapped) {
		this.selectedinventorymapped = selectedinventorymapped;
	}
	
	
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
	public Integer getNsectioncode() {
		return nsectioncode;
	}
	public void setNsectioncode(Integer nsectioncode) {
		this.nsectioncode = nsectioncode;
	}
	public Integer getNtransactionstatus() {
		return ntransactionstatus;
	}
	public void setNtransactionstatus(Integer ntransactionstatus) {
		this.ntransactionstatus = ntransactionstatus;
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
	public String getJsonuidata() {
		return jsonuidata;
	}
	public void setJsonuidata(String jsonuidata) {
		this.jsonuidata = jsonuidata;
	}
	public String getSinventoryid() {
		
		Map<String, Object> objContent = commonfunction.getInventoryValuesFromJsonString(this.jsondata,"Inventory ID");
		
		if(objContent.containsKey("rtnObj")) {
			
			return sinventoryid = (String) objContent.get("rtnObj");
		}
		
		return sinventoryid;
	}
	public void setSinventoryid(String sinventoryid) {
		this.sinventoryid = sinventoryid;
	}
	public String getSavailablequatity() {
		return savailablequatity;
	}
	public void setSavailablequatity(String savailablequatity) {
		this.savailablequatity = savailablequatity;
	}
	public String getSunitname() {
		return sunitname;
	}
	public void setSunitname(String sunitname) {
		this.sunitname = sunitname;
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
	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}
	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}
	
}