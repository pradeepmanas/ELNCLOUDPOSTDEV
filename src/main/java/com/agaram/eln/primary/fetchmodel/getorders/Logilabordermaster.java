package com.agaram.eln.primary.fetchmodel.getorders;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;

public class Logilabordermaster implements Comparable<Logilabordermaster>{
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
	private Date completedtimestamp;
	@SuppressWarnings("unused")
	private LSworkflow lsworkflow;
	private String keyword;

	public Logilabordermaster(Long batchcode, String batchid, LSworkflow lsworkflow, String testname, LSfile lsfile,
			LSsamplemaster lssamplemaster, LSprojectmaster lsprojectmaster, Integer filetype, String orderflag,
			Date createdtimestamp, Date completedtimestamp,String keyword) {
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
		this.completedtimestamp = completedtimestamp;
		this.keyword = keyword;
	}
	
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

//	public void setLstworkflow(List<LSworkflow> lstworkflow) {
//		
//		if(lstworkflow != null && this.workflowcode !=null && lstworkflow.size() >0)
//		{
//			this.setCanuserprocess(lstworkflow.stream().map(LSworkflow::getWorkflowcode).anyMatch(this.workflowcode::equals));
////			if(lstworkflow.contains(this.workflowcode))
////			{
////				this.setCanuserprocess(true);
////			}
////			else
////			{
////				this.setCanuserprocess(false);
////			}
//		}
//		else
//		{
//			this.setCanuserprocess(false);
//		}
//		this.lstworkflow = null;
//	}
	public void setLstworkflow(List<LSworkflow> lstworkflow) {

		if (lstworkflow != null  && this.workflowcode !=null && lstworkflow.size() > 0) {
			// if(lstworkflow.contains(this.lsworkflow))

			List<Integer> lstworkflowcode = new ArrayList<Integer>();
			if (lstworkflow != null && lstworkflow.size() > 0) {
				lstworkflowcode = lstworkflow.stream().map(LSworkflow::getWorkflowcode).collect(Collectors.toList());

				if (lstworkflowcode.contains(this.workflowcode)) {
					this.setCanuserprocess(true);
				} else {
					this.setCanuserprocess(false);
				}
			} else {
				this.setCanuserprocess(false);
			}
		} else {
			this.setCanuserprocess(false);
		}
		this.lstworkflow = null;
	}

	public Date getCompletedtimestamp() {
		return completedtimestamp;
	}

	public void setCompletedtimestamp(Date completedtimestamp) {
		this.completedtimestamp = completedtimestamp;
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
	
	@Override
	public int compareTo(Logilabordermaster o) {
		return this.getBatchcode().compareTo(o.getBatchcode());
	}
}
