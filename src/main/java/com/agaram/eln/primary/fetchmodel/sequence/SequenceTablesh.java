package com.agaram.eln.primary.fetchmodel.sequence;

public class SequenceTablesh {
	private Integer sc;
	private Integer sqv;
	private String sqf;
	
	public SequenceTablesh(Integer sequencecode,Integer sequenceview, String sequenceformat)
	{
		this.sc = sequencecode;
		this.sqv = sequenceview;
		this.sqf = sequenceformat;
	}
	
	public Integer getSc() {
		return sc;
	}
	public void setSc(Integer sc) {
		this.sc = sc;
	}
	public Integer getSqv() {
		return sqv;
	}
	public void setSqv(Integer sqv) {
		this.sqv = sqv;
	}
	public String getSqf() {
		return sqf;
	}
	public void setSqf(String sqf) {
		this.sqf = sqf;
	}
	
	
}
