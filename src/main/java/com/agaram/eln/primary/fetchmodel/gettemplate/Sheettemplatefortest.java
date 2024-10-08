package com.agaram.eln.primary.fetchmodel.gettemplate;

import java.util.Date;
import java.util.List;

import com.agaram.eln.primary.model.sheetManipulation.LSfiletest;

public class Sheettemplatefortest {
	private Integer filecode;
	private String filenameuser;
	private List<LSfiletest> lstest;
	private Date createddate;
	private Integer tagsheet;
	private Date modifieddate;

	public Sheettemplatefortest(Integer filecode, String filenameuser, Date createddate, List<LSfiletest> lstest,
			Integer tagsheet,Date modifieddate) {
		this.filecode = filecode;
		this.filenameuser = filenameuser;
		this.lstest = lstest;
		this.createddate = createddate;
		this.tagsheet = tagsheet;
		this.modifieddate=modifieddate;
	}

	public Date getModifieddate() {
		return modifieddate;
	}

	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}

	public Integer getTagsheet() {
		return tagsheet;
	}

	public void setTagsheet(Integer tagsheet) {
		this.tagsheet = tagsheet;
	}

	public Integer getFilecode() {
		return filecode;
	}

	public void setFilecode(Integer filecode) {
		this.filecode = filecode;
	}

	public String getFilenameuser() {
		return filenameuser;
	}

	public void setFilenameuser(String filenameuser) {
		this.filenameuser = filenameuser;
	}

	public List<LSfiletest> getLstest() {
		return lstest;
	}

	public void setLstest(List<LSfiletest> lstest) {
		this.lstest = lstest;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

}
