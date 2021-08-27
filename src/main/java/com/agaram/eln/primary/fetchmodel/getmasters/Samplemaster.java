package com.agaram.eln.primary.fetchmodel.getmasters;

public class Samplemaster {
	
	private Integer samplecode;
	private String samplename;
	
	public Samplemaster(Integer samplecode, String samplename)
	{
		this.samplecode = samplecode;
		this.samplename = samplename;
	}
	
	public Integer getSamplecode() {
		return samplecode;
	}
	public void setSamplecode(Integer samplecode) {
		this.samplecode = samplecode;
	}
	public String getSamplename() {
		return samplename;
	}
	public void setSamplename(String samplename) {
		this.samplename = samplename;
	}
	
	

}
