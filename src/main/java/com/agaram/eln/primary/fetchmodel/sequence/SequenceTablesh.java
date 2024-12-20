package com.agaram.eln.primary.fetchmodel.sequence;

public class SequenceTablesh {
	private Integer sc;
	private Integer sqv;
	private String sqf;
	private Integer rp;
	private String sn;
	private String sep;
	
	public SequenceTablesh(Integer sequencecode,Integer sequenceview, String sequenceformat,
			Integer resetperiod,String screenname,String seperator)
	{
		this.sc = sequencecode;
		this.sqv = sequenceview;
		this.sqf = sequenceformat;
		this.rp = resetperiod;
		this.sn = screenname;
		this.sep = seperator;
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

	public Integer getRp() {
		return rp;
	}

	public void setRp(Integer rp) {
		this.rp = rp;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getSep() {
		return sep;
	}

	public void setSep(String sep) {
		this.sep = sep;
	}
	
	
}
