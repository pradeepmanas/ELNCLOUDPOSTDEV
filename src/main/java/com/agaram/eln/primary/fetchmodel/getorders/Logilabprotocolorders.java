package com.agaram.eln.primary.fetchmodel.getorders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.material.Material;
import com.agaram.eln.primary.model.material.MaterialInventory;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public class Logilabprotocolorders implements Comparable<Logilabprotocolorders> {

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
	private Integer approved;
	private Integer rejected;
	private Integer viewoption;

	private String materialname;
	private String materialinventoryname;
	List<LSworkflow> lstworkflow;
	private LSprotocolworkflow lSprotocolworkflow;
	private LSworkflow lsworkflow;
	private Integer workflowcode;
	private boolean canuserprocess;
	private LSsamplemaster lssamplemaster;
	private Integer ordercancell;
	private Integer orderstarted;
	private LSuserMaster orderstartedby;
	private Date orderstartedon;
	private Integer lockeduser;
	private String lockedusername;
	private Integer versionno;

	public Logilabprotocolorders(Long protocolordercode, Integer Testcode, String protoclordername, String orderflag,
			Integer protocoltype, Date createdtimestamp, Date completedtimestamp, LSprotocolmaster lsprotocolmaster,
			LSprotocolworkflow lSprotocolworkflow, LSsamplemaster lssamplemaster, LSprojectmaster lsprojectmaster,
			String keyword, Long directorycode, Integer createby, LSuserMaster assignedto,
			Lsrepositoriesdata lsrepositoriesdata, Lsrepositories lsrepositories, LSworkflow lsworkflow,
			Material material, MaterialInventory materialinventory, Integer approved, Integer rejected,
			Integer ordercancell, Integer viewoption, Integer orderstarted, LSuserMaster orderstartedby,
			Date orderstartedon,Integer lockeduser,String lockedusername, Integer versionno) {

		this.protocolordercode = protocolordercode;
		this.Testcode = Testcode;
		this.protoclordername = protoclordername;
		this.orderflag = orderflag;
		this.workflowcode = lsworkflow != null ? lsworkflow.getWorkflowcode() : null;
		this.protocoltype = protocoltype;
		this.createdtimestamp = createdtimestamp;
		this.completedtimestamp = completedtimestamp;
		this.protocolmastername = lsprotocolmaster != null ? lsprotocolmaster.getProtocolmastername() : "";
		this.samplename = lssamplemaster != null ? lssamplemaster.getSamplename() : "";
		this.projectname = lsprojectmaster != null ? lsprojectmaster.getProjectname() : "";
		this.keyword = keyword;
		this.directorycode = directorycode;
		this.lsprojectmaster = lsprojectmaster;
		this.createby = createby;
		this.assignedto = assignedto;
		this.lssamplemaster = lssamplemaster != null ? lssamplemaster : null;
		this.repositoryitemname = lsrepositoriesdata != null ? lsrepositoriesdata.getRepositoryitemname() : null;
		this.repositoryname = lsrepositories != null ? lsrepositories.getRepositoryname() : null;
		this.materialname = material != null ? material.getSmaterialname() : null;
		this.materialinventoryname = materialinventory != null ? materialinventory.getSinventoryid() : null;
		this.directorycode = directorycode;
		this.lsworkflow = lsworkflow;
		this.lsprotocolmaster = lsprotocolmaster;
		this.approved = approved;
		this.rejected = rejected;
		this.ordercancell = ordercancell;
		this.viewoption = viewoption;
		this.orderstarted = orderstarted != null && orderstarted == 1 ? orderstarted : 0;
		this.orderstartedby = orderstartedby != null ? orderstartedby : null;
		this.orderstartedon = orderstartedon != null ? orderstartedon : null;
		this.lockeduser=lockeduser!=null?lockeduser:null;
		this.lockedusername=lockedusername!=null?lockedusername:null;
		this.versionno = versionno;
	}

	public Integer getLockeduser() {
		return lockeduser;
	}

	public void setLockeduser(Integer lockeduser) {
		this.lockeduser = lockeduser;
	}

	public String getLockedusername() {
		return lockedusername;
	}

	public void setLockedusername(String lockedusername) {
		this.lockedusername = lockedusername;
	}

	public Integer getViewoption() {
		return viewoption;
	}

	public void setViewoption(Integer viewoption) {
		this.viewoption = viewoption;
	}

	public Integer getOrdercancell() {
		return ordercancell;
	}

	public void setOrdercancell(Integer ordercancell) {
		this.ordercancell = ordercancell;
	}

	public Integer getApproved() {
		return approved;
	}

	public void setApproved(Integer approved) {
		this.approved = approved;
	}

	public Integer getRejected() {
		return rejected;
	}

	public void setRejected(Integer rejected) {
		this.rejected = rejected;
	}

	public LSworkflow getLsworkflow() {
		return lsworkflow;
	}

	public void setLsworkflow(LSworkflow lsworkflow) {
		this.lsworkflow = lsworkflow;
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

	public List<LSworkflow> getLstworkflow() {
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

	public void setLstworkflow(List<LSworkflow> lstworkflow) {

		if (lstworkflow != null && this.workflowcode != null && lstworkflow.size() > 0) {

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

	public String getMaterialname() {
		return materialname;
	}

	public void setMaterialname(String materialname) {
		this.materialname = materialname;
	}

	public String getMaterialinventoryname() {
		return materialinventoryname;
	}

	public void setMaterialinventoryname(String materialinventoryname) {
		this.materialinventoryname = materialinventoryname;
	}

	public Integer getOrderstarted() {
		return orderstarted;
	}

	public void setOrderstarted(Integer orderstarted) {
		this.orderstarted = orderstarted;
	}

	public LSuserMaster getOrderstartedby() {
		return orderstartedby;
	}

	public void setOrderstartedby(LSuserMaster orderstartedby) {
		this.orderstartedby = orderstartedby;
	}

	public Date getOrderstartedon() {
		return orderstartedon;
	}

	public void setOrderstartedon(Date orderstartedon) {
		this.orderstartedon = orderstartedon;
	}

	public Integer getVersionno() {
		return versionno;
	}

	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}

}