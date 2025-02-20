package com.agaram.eln.primary.fetchmodel.inventory;

import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public class MaterialInventoryget {
	private Integer invencode;
	private String invenname;
	private String invenid;
	private Double qtynot;
	private String qty;
	private Integer sts; 
	private LSuserMaster crby;
	private String seqid;
	
	public MaterialInventoryget(Integer nmaterialinventorycode, String inventoryname, String sinventoryid,
			Double nqtynotification,String savailablequantity,Integer ntransactionstatus,LSuserMaster createdby,String sequenceid
			)
	{
		this.invencode = nmaterialinventorycode;
		this.invenname = inventoryname;
		this.invenid = sinventoryid;
		this.qtynot= nqtynotification;
		this.qty = savailablequantity;
		this.sts = ntransactionstatus;
		this.crby = createdby;
		this.seqid = sequenceid;
	}

	public Integer getInvencode() {
		return invencode;
	}

	public void setInvencode(Integer invencode) {
		this.invencode = invencode;
	}

	public String getInvenname() {
		return invenname;
	}

	public void setInvenname(String invenname) {
		this.invenname = invenname;
	}

	public String getInvenid() {
		return invenid;
	}

	public void setInvenid(String invenid) {
		this.invenid = invenid;
	}

	public Double getQtynot() {
		return qtynot;
	}

	public void setQtynot(Double qtynot) {
		this.qtynot = qtynot;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public Integer getSts() {
		return sts;
	}

	public void setSts(Integer sts) {
		this.sts = sts;
	}

	public LSuserMaster getCrby() {
		return crby;
	}

	public void setCrby(LSuserMaster crby) {
		this.crby = crby;
	}

	public String getSeqid() {
		return seqid;
	}

	public void setSeqid(String seqid) {
		this.seqid = seqid;
	}
	
	
}
