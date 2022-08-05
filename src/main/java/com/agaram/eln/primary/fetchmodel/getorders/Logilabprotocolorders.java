package com.agaram.eln.primary.fetchmodel.getorders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;

public class Logilabprotocolorders implements Comparable<Logilabprotocolorders>{ 

	private Long protocolordercode;
	private String protoclordername;

	private String orderflag;
	private Integer protocoltype;

	private Date createdtimestamp;
	private Date completedtimestamp;

	private String samplename;
	private LSprotocolmaster lsprotocolmaster;
	private String projectname;
	private String protocolmastername;

	private String keyword;
	
	List<LSprotocolworkflow> lstworkflow;
	private LSprotocolworkflow lSprotocolworkflow;
	private Integer workflowcode;
	private boolean canuserprocess;

	public Logilabprotocolorders(Long protocolordercode, String protoclordername, String orderflag,
			Integer protocoltype, Date createdtimestamp, Date completedtimestamp, LSprotocolmaster lsprotocolmaster,
			LSprotocolworkflow lSprotocolworkflow,
			LSsamplemaster lssamplemaster, LSprojectmaster lsprojectmaster,String keyword) {

		this.protocolordercode = protocolordercode;
		this.protoclordername = protoclordername;
		this.orderflag = orderflag;
		this.workflowcode = lSprotocolworkflow != null ? lSprotocolworkflow.getWorkflowcode() : null;
		this.protocoltype = protocoltype;
		this.createdtimestamp = createdtimestamp;
		this.completedtimestamp = completedtimestamp;
		this.protocolmastername = lsprotocolmaster != null ? lsprotocolmaster.getProtocolmastername() : "";
		this.samplename = lssamplemaster != null ? lssamplemaster.getSamplename():"";
		this.projectname = lsprojectmaster != null ? lsprojectmaster.getProjectname():"";
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getProtocolmastername() {
		return protocolmastername;
	}

	public void setProtocolmastername(String protocolmastername) {
		this.protocolmastername = protocolmastername;
	}

	public LSprotocolworkflow getlSprotocolworkflow() {
		return lSprotocolworkflow;
	}

	public void setlSprotocolworkflow(LSprotocolworkflow lSprotocolworkflow) {
		this.lSprotocolworkflow = lSprotocolworkflow;
	}

	public Integer getWorkflowcode() {
		return workflowcode;
	}

	public void setWorkflowcode(Integer workflowcode) {
		this.workflowcode = workflowcode;
	}

	public String getSamplename() {
		return samplename;
	}

	public void setSamplename(String samplename) {
		this.samplename = samplename;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public Long getProtocolordercode() {
		return protocolordercode;
	}

	public void setProtocolordercode(Long protocolordercode) {
		this.protocolordercode = protocolordercode;
	}

	public String getProtoclordername() {
		return protoclordername;
	}

	public void setProtoclordername(String protoclordername) {
		this.protoclordername = protoclordername;
	}

	public String getOrderflag() {
		return orderflag;
	}

	public void setOrderflag(String orderflag) {
		this.orderflag = orderflag;
	}

	public Integer getProtocoltype() {
		return protocoltype;
	}

	public void setProtocoltype(Integer protocoltype) {
		this.protocoltype = protocoltype;
	}

	public Date getCreatedtimestamp() {
		return createdtimestamp;
	}

	public void setCreatedtimestamp(Date createdtimestamp) {
		this.createdtimestamp = createdtimestamp;
	}

	public Date getCompletedtimestamp() {
		return completedtimestamp;
	}

	public void setCompletedtimestamp(Date completedtimestamp) {
		this.completedtimestamp = completedtimestamp;
	}

	public LSprotocolmaster getLsprotocolmaster() {
		return lsprotocolmaster;
	}

	public void setLsprotocolmaster(LSprotocolmaster lsprotocolmaster) {
		this.lsprotocolmaster = lsprotocolmaster;
	}
	
	public List<LSprotocolworkflow> getLstworkflow() {
		return lstworkflow;
	}
	
	public void setLstworkflow(List<LSprotocolworkflow> lstworkflow) {

		if (lstworkflow != null  && this.workflowcode !=null && lstworkflow.size() > 0) {

			List<Integer> lstworkflowcode = new ArrayList<Integer>();
			if (lstworkflow != null && lstworkflow.size() > 0) {
				lstworkflowcode = lstworkflow.stream().map(LSprotocolworkflow::getWorkflowcode).collect(Collectors.toList());

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

	public boolean isCanuserprocess() {
		return canuserprocess;
	}

	public void setCanuserprocess(boolean canuserprocess) {
		this.canuserprocess = canuserprocess;
	}

	@Override
	public int compareTo(Logilabprotocolorders o) {
		return this.getProtocolordercode().compareTo(o.getProtocolordercode());
	}
}