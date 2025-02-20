package com.agaram.eln.primary.fetchmodel.inventory;

import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public class Sampleget {
	private Integer samcode;
	private String samname;
	private String seqid;
	private Double qtynot;
	private Integer qty;
	private Integer sts; 
	private LSuserMaster crby;
	
	public Sampleget(Integer samplecode,String samplename, String sequenceid,
			Double nqtynotification,Integer quantity,Integer ntransactionstatus,LSuserMaster createby)
	{
		this.samcode = samplecode;
		this.samname = samplename;
		this.seqid = sequenceid;
		this.qtynot= nqtynotification;
		this.qty = quantity;
		this.sts = ntransactionstatus;
		this.crby = createby;
	}

	public Integer getSamcode() {
		return samcode;
	}

	public void setSamcode(Integer samcode) {
		this.samcode = samcode;
	}

	public String getSamname() {
		return samname;
	}

	public void setSamname(String samname) {
		this.samname = samname;
	}

	public String getSeqid() {
		return seqid;
	}

	public void setSeqid(String seqid) {
		this.seqid = seqid;
	}

	public Double getQtynot() {
		return qtynot;
	}

	public void setQtynot(Double qtynot) {
		this.qtynot = qtynot;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
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
	
	
}
