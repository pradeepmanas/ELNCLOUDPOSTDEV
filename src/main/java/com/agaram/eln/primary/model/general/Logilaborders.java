package com.agaram.eln.primary.model.general;

public class Logilaborders {

	private Long batchcode;
	private String batchid;
	
	public Logilaborders(Long batchcode, String batchid)
	{
		this.batchcode = batchcode;
		this.batchid = batchid;
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
	
	
}
