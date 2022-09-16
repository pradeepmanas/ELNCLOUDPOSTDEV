package com.agaram.eln.primary.fetchmodel.getorders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

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
	private LSprojectmaster lsprojectmaster;


	private String keyword;
	
	private Integer Testcode;
	
	private Long directorycode;
	
	private Integer createby;
	
	private String repositoryitemname;
	private LSuserMaster assignedto;
	private String repositoryname;
	


	List<LSprotocolworkflow> lstworkflow;
	private LSprotocolworkflow lSprotocolworkflow;
	private Integer workflowcode;
	private boolean canuserprocess;
	private LSsamplemaster lssamplemaster;

	public Logilabprotocolorders(Long protocolordercode,Integer Testcode, String protoclordername, String orderflag,
			Integer protocoltype, Date createdtimestamp, Date completedtimestamp, LSprotocolmaster lsprotocolmaster,
			LSprotocolworkflow lSprotocolworkflow,
			LSsamplemaster lssamplemaster, LSprojectmaster lsprojectmaster,String keyword,Long directorycode,Integer createby,LSuserMaster assignedto,Lsrepositoriesdata lsrepositoriesdata,Lsrepositories lsrepositories) {
		
		this.protocolordercode = protocolordercode;
		this.Testcode =Testcode;
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
		this.directorycode = directorycode;
		this.lsprojectmaster = lsprojectmaster;
		this.createby =createby;
		this.assignedto =assignedto;
		this.lssamplemaster= lssamplemaster != null ? lssamplemaster:null;
		this.repositoryitemname =lsrepositoriesdata !=null ?lsrepositoriesdata.getRepositoryitemname():null;
		this.repositoryname =lsrepositories !=null ?lsrepositories.getRepositoryname():null;
		this.directorycode = directorycode;
	}

	public LSsamplemaster getLssamplemaster() {
		return lssamplemaster;
	}

	public void setLssamplemaster(LSsamplemaster lssamplemaster) {
		this.lssamplemaster = lssamplemaster;
	}

	public Integer getCreateby() {
		return createby;
	}

	public void setCreateby(Integer createby) {
		this.createby = createby;
	}

	public Integer getTestcode() {
		return Testcode;
	}

	public void setTestcode(Integer testcode) {
		Testcode = testcode;
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
	public LSprojectmaster getLsprojectmaster() {
		return lsprojectmaster;
	}
	
	public String getRepositoryitemname() {
		return repositoryitemname;
	}

	public void setRepositoryitemname(String repositoryitemname) {
		this.repositoryitemname = repositoryitemname;
	}

	public LSuserMaster getAssignedto() {
		return assignedto;
	}

	public void setAssignedto(LSuserMaster assignedto) {
		this.assignedto = assignedto;
	}

	public String getRepositoryname() {
		return repositoryname;
	}

	public void setRepositoryname(String repositoryname) {
		this.repositoryname = repositoryname;
	}

	public void setLsprojectmaster(LSprojectmaster lsprojectmaster) {
		this.lsprojectmaster = lsprojectmaster;
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
	public Long getDirectorycode() {
		return directorycode;
	}

	public void setDirectorycode(Long directorycode) {
		this.directorycode = directorycode;
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