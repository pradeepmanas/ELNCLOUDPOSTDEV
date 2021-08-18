package com.agaram.eln.primary.model.general;

public class Logilaborders {

	private Long batchcode;
	private String batchid;
	private String orderflag;
	
	public Logilaborders(Long batchcode, String batchid, String orderflag)
	{
		this.batchcode = batchcode;
		this.batchid = batchid;
		this.orderflag = orderflag;
	}
	
	public Long getBatchcode() {
		return batchcode;
	}
	public void setBatchcode(Long batchcode) {
		this.batchcode = batchcode;
	}
	public String getBatchid() {
		return batchid;
	}
	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}

	public String getOrderflag() {
		return orderflag;
	}

	public void setOrderflag(String orderflag) {
		this.orderflag = orderflag;
	}
	
	
}
