package com.agaram.eln.primary.model.material;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "materialinventorytransaction")
public class MaterialInventoryTransaction {
	@Id
	@Column(name = "nmaterialinventtranscode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer nmaterialinventtranscode;
	
	private Integer nmaterialinventorycode;
	private Integer ninventorytranscode;
	private Integer ntransactiontype;
	private Integer nstatus;
	private Integer nsectioncode;
	
	private Integer nresultusedmaterialcode;
	private Double nqtyreceived;
	private Double nqtyissued;
	
//	@Type(type = "jsonb")
	@Column(name = "jsondata")
	private String jsondata;
//	@Type(type = "jsonb")
	@Column(name = "jsonuidata")
	private String jsonuidata;
	
	private Integer nsitecode;
	
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
	public Integer getNinventorytranscode() {
		return ninventorytranscode;
	}
	public void setNinventorytranscode(Integer ninventorytranscode) {
		this.ninventorytranscode = ninventorytranscode;
	}
	public Integer getNtransactiontype() {
		return ntransactiontype;
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
	public Integer getNsectioncode() {
		return nsectioncode;
	}
	public void setNsectioncode(Integer nsectioncode) {
		this.nsectioncode = nsectioncode;
	}
	public Integer getNresultusedmaterialcode() {
		return nresultusedmaterialcode;
	}
	public void setNresultusedmaterialcode(Integer nresultusedmaterialcode) {
		this.nresultusedmaterialcode = nresultusedmaterialcode;
	}
	public Double getNqtyreceived() {
		return nqtyreceived;
	}
	public void setNqtyreceived(Double nqtyreceived) {
		this.nqtyreceived = nqtyreceived;
	}
	public Double getNqtyissued() {
		return nqtyissued;
	}
	public void setNqtyissued(Double nqtyissued) {
		this.nqtyissued = nqtyissued;
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
	public Integer getNsitecode() {
		return nsitecode;
	}
	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}
}