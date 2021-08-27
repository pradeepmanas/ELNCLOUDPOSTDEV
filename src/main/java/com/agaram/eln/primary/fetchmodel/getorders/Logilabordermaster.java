package com.agaram.eln.primary.fetchmodel.getorders;

import java.util.List;

import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;

public class Logilabordermaster {
	private Long batchcode;
	private String batchid;
	List<LSworkflow> lstworkflow;
	private boolean canuserprocess;
	private LSworkflow lsworkflow;
	private String testname;
	private String filename;
	private String projectname;
	private String samplename;
	
	public Logilabordermaster(Long batchcode, String batchid, LSworkflow lsworkflow
			, String testname, LSfile lsfile, LSsamplemaster lssamplemaster,
			LSprojectmaster lsprojectmaster)
	{
		this.batchcode = batchcode;
		this.batchid = batchid;
		this.lsworkflow = lsworkflow;
		this.testname = testname;
		this.filename = lsfile != null ? lsfile.getFilenameuser() : null;
		this.projectname = lsprojectmaster != null ? lsprojectmaster.getProjectname() : null;
		this.samplename = lssamplemaster != null ? lssamplemaster.getSamplename() : null;
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
	public LSworkflow getLsworkflow() {
		return lsworkflow;
	}

	public void setLsworkflow(LSworkflow lsworkflow) {
		this.lsworkflow = lsworkflow;
	}
	public List<LSworkflow> getLstworkflow() {
		return lstworkflow;
	}

	public void setLstworkflow(List<LSworkflow> lstworkflow) {
		
		if(lstworkflow != null && this.lsworkflow !=null && lstworkflow.size() >0)
		{
			if(lstworkflow.contains(this.lsworkflow))
			{
				this.setCanuserprocess(true);
			}
			else
			{
				this.setCanuserprocess(false);
			}
		}
		else
		{
			this.setCanuserprocess(false);
		}
		this.lstworkflow = null;
	}

	public boolean isCanuserprocess() {
		return canuserprocess;
	}

	public void setCanuserprocess(boolean canuserprocess) {
		this.canuserprocess = canuserprocess;
	}
	
	public String getTestname() {
		return testname;
	}

	public void setTestname(String testname) {
		this.testname = testname;
	}
	
	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getSamplename() {
		return samplename;
	}

	public void setSamplename(String samplename) {
		this.samplename = samplename;
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
}
