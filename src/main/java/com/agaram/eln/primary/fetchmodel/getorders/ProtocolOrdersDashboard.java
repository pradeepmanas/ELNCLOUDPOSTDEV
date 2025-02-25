package com.agaram.eln.primary.fetchmodel.getorders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.agaram.eln.primary.model.instrumentDetails.LSOrdernotification;
import com.agaram.eln.primary.model.instrumentDetails.LsAutoregister;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.ElnmaterialInventory;
import com.agaram.eln.primary.model.protocols.Elnprotocolworkflow;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public class ProtocolOrdersDashboard implements Comparable<ProtocolOrdersDashboard> {
	
	private Long protocolordercode;
	private String protoclordername;
	private String orderflag;
	private Integer protocoltype;
	private Date createdtimestamp;
	private Date completedtimestamp;
	private String samplename;
	private String projectname;
	private String protocolmastername;
	private String keyword;
	private Integer Testcode;
	private Integer approved;
	private Integer rejected;
	private Integer viewoption;
	private String materialname;
	private String materialinventoryname;
	private Integer workflowcode;
	private Integer ordercancell;
	private Integer orderstarted;
	private Date orderstartedon;
	private Elnprotocolworkflow elnprotocolworkflow;
	List<Elnprotocolworkflow> lstelnprotocolworkflow;
	private boolean canuserprocess;
	private LSprotocolmaster lsprotocolmaster;
	private String sequenceid;
	private Date modifieddate;

	
	public ProtocolOrdersDashboard(Long protocolordercode, Integer Testcode, String protoclordername, String orderflag,
			Integer protocoltype, Date createdtimestamp, Date completedtimestamp, LSprotocolmaster lsprotocolmaster,
			LSprotocolworkflow lSprotocolworkflow, LSsamplemaster lssamplemaster, LSprojectmaster lsprojectmaster,
			String keyword, Long directorycode, Integer createby, LSuserMaster assignedto,
			Lsrepositoriesdata lsrepositoriesdata, Lsrepositories lsrepositories,
			Elnmaterial elnmaterial,ElnmaterialInventory elnmaterialinventory, Integer approved, Integer rejected,
			Integer ordercancell, Integer viewoption, Integer orderstarted, LSuserMaster orderstartedby,
			Date orderstartedon,Integer lockeduser,String lockedusername, Integer versionno,Elnprotocolworkflow elnprotocolworkflow,
			LSOrdernotification lsordernotification,LsAutoregister lsautoregister,Boolean repeat,
			Boolean sentforapprovel,String approvelaccept,Integer autoregistercount, LSuserMaster lsuserMaster,String sequenceid,Date modifieddate) {

		this.protocolordercode = protocolordercode;
		this.Testcode = Testcode;
		this.protoclordername = protoclordername;
		this.orderflag = orderflag;
		this.workflowcode = elnprotocolworkflow != null ? elnprotocolworkflow.getWorkflowcode() : null;
		this.protocoltype = protocoltype;
		this.createdtimestamp = createdtimestamp;
		this.completedtimestamp = completedtimestamp;
		this.protocolmastername = lsprotocolmaster != null ? lsprotocolmaster.getProtocolmastername() : "";
		this.samplename = lssamplemaster != null ? lssamplemaster.getSamplename() : "";
		this.projectname = lsprojectmaster != null ? lsprojectmaster.getProjectname() : "";
		this.keyword = keyword;
		this.materialname = elnmaterial != null ? elnmaterial.getSmaterialname() : null;
		this.materialinventoryname = elnmaterialinventory != null ? elnmaterialinventory.getSinventoryid() : null;
		this.approved = approved;
		this.rejected = rejected;
		this.ordercancell = ordercancell;
		this.viewoption = viewoption;
		this.orderstarted = orderstarted != null && orderstarted == 1 ? orderstarted : 0;
		this.orderstartedon = orderstartedon;
		this.elnprotocolworkflow = elnprotocolworkflow;
		this.lsprotocolmaster=lsprotocolmaster;
		this.sequenceid = sequenceid;
		this.modifieddate=modifieddate;
	}
	
	public List<Elnprotocolworkflow> getLstelnprotocolworkflow() {
		return lstelnprotocolworkflow;
	}

	public void setLstelnprotocolworkflow(List<Elnprotocolworkflow> lstelnprotocolworkflow) {
//		this.lstelnprotocolworkflow = lstelnprotocolworkflow;
		if (lstelnprotocolworkflow != null && this.workflowcode != null && lstelnprotocolworkflow.size() > 0) {

			List<Integer> lstworkflowcode = new ArrayList<Integer>();
			if (lstelnprotocolworkflow != null && lstelnprotocolworkflow.size() > 0) {
				lstworkflowcode = lstelnprotocolworkflow.stream().map(Elnprotocolworkflow::getWorkflowcode).collect(Collectors.toList());

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
		this.lstelnprotocolworkflow = null;
	}
	
	public LSprotocolmaster getLsprotocolmaster() {
		return lsprotocolmaster;
	}

	public void setLsprotocolmaster(LSprotocolmaster lsprotocolmaster) {
		this.lsprotocolmaster = lsprotocolmaster;
	}

	@Override
	public int compareTo(ProtocolOrdersDashboard o) {
		return this.getProtocolordercode().compareTo(o.getProtocolordercode());
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

	public String getProtocolmastername() {
		return protocolmastername;
	}

	public void setProtocolmastername(String protocolmastername) {
		this.protocolmastername = protocolmastername;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getTestcode() {
		return Testcode;
	}

	public void setTestcode(Integer testcode) {
		Testcode = testcode;
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

	public Integer getViewoption() {
		return viewoption;
	}

	public void setViewoption(Integer viewoption) {
		this.viewoption = viewoption;
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

	public Integer getWorkflowcode() {
		return workflowcode;
	}

	public void setWorkflowcode(Integer workflowcode) {
		this.workflowcode = workflowcode;
	}

	public Integer getOrdercancell() {
		return ordercancell;
	}

	public void setOrdercancell(Integer ordercancell) {
		this.ordercancell = ordercancell;
	}

	public Integer getOrderstarted() {
		return orderstarted;
	}

	public void setOrderstarted(Integer orderstarted) {
		this.orderstarted = orderstarted;
	}

	public Date getOrderstartedon() {
		return orderstartedon;
	}

	public void setOrderstartedon(Date orderstartedon) {
		this.orderstartedon = orderstartedon;
	}

	public Elnprotocolworkflow getElnprotocolworkflow() {
		return elnprotocolworkflow;
	}

	public void setElnprotocolworkflow(Elnprotocolworkflow elnprotocolworkflow) {
		this.elnprotocolworkflow = elnprotocolworkflow;
	}



	public boolean isCanuserprocess() {
		return canuserprocess;
	}

	public void setCanuserprocess(boolean canuserprocess) {
		this.canuserprocess = canuserprocess;
	}

	public String getSequenceid() {
		return sequenceid;
	}

	public void setSequenceid(String sequenceid) {
		this.sequenceid = sequenceid;
	}
	public Date getModifieddate() {
		return modifieddate;
	}
	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}
	
	
}
