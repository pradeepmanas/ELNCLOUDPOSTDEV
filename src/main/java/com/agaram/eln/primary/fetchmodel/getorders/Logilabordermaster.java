package com.agaram.eln.primary.fetchmodel.getorders;

import java.util.Date;
import java.util.List;

import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;

public class Logilabordermaster {
	private Long batchcode;
	@SuppressWarnings("unused")
	private String batchid;
	List<LSworkflow> lstworkflow;
	private Integer workflowcode;
	private boolean canuserprocess;
	private String testname;
	private String filename;
	private String projectname;
	private String samplename;
	private Integer filetype;
	private String orderflag;
	private Date createdtimestamp;
	
	public Logilabordermaster(Long batchcode, String batchid, LSworkflow lsworkflow
			, String testname, LSfile lsfile, LSsamplemaster lssamplemaster,
			LSprojectmaster lsprojectmaster, Integer filetype, String orderflag, Date createdtimestamp)
	{
		this.batchcode = batchcode;
		this.batchid = batchid;
		this.workflowcode = lsworkflow != null ? lsworkflow.getWorkflowcode() : null;
		this.testname = testname;
		this.filename = lsfile != null ? lsfile.getFilenameuser() : null;
		this.projectname = lsprojectmaster != null ? lsprojectmaster.getProjectname() : null;
		this.samplename = lssamplemaster != null ? lssamplemaster.getSamplename() : null;
		this.filetype = filetype;
		this.orderflag = orderflag;
		this.createdtimestamp = createdtimestamp;
	}
	
	public Long getBatchcode() {
		return batchcode;
	}
	public void setBatchcode(Long batchcode) {
		this.batchcode = batchcode;
	}
	public String getBatchid() {
		String Batchid = "ELN" + this.batchcode;
		
		if (this.filetype == 3) {
			Batchid = "RESEARCH" + this.batchcode;
		} else if (this.filetype == 4) {
			Batchid = "EXCEL" + this.batchcode;
		} else if (this.filetype == 5) {
			Batchid = "VALIDATE" + this.batchcode;
		}
		
		return Batchid;
	}
	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}
	
	public Integer getWorkflowcode() {
		return workflowcode;
	}

	public void setWorkflowcode(Integer workflowcode) {
		this.workflowcode = workflowcode;
	}

	public List<LSworkflow> getLstworkflow() {
		return lstworkflow;
	}

	public void setLstworkflow(List<LSworkflow> lstworkflow) {
		
		if(lstworkflow != null && this.workflowcode !=null && lstworkflow.size() >0)
		{
			this.setCanuserprocess(lstworkflow.stream().map(LSworkflow::getWorkflowcode).anyMatch(this.workflowcode::equals));
//			if(lstworkflow.contains(this.workflowcode))
//			{
//				this.setCanuserprocess(true);
//			}
//			else
//			{
//				this.setCanuserprocess(false);
//			}
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

	public Integer getFiletype() {
		return filetype;
	}

	public void setFiletype(Integer filetype) {
		this.filetype = filetype;
	}

	public String getOrderflag() {
		return orderflag;
	}

	public void setOrderflag(String orderflag) {
		this.orderflag = orderflag;
	}

	public Date getCreatedtimestamp() {
		return createdtimestamp;
	}

	public void setCreatedtimestamp(Date createdtimestamp) {
		this.createdtimestamp = createdtimestamp;
	}
	
	
}
