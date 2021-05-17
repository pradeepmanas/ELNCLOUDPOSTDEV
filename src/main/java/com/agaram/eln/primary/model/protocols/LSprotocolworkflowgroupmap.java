package com.agaram.eln.primary.model.protocols;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.agaram.eln.primary.model.usermanagement.LSusergroup;

@Entity
@Table(name = "lsprotocolworkflowgroupmap")
public class LSprotocolworkflowgroupmap {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "workflowmapid")
	private Integer workflowmapid;

	@Column(nullable=false)
	private Integer workflowcode;
	
	@ManyToOne 
	private LSusergroup lsusergroup;
	
	public Integer getWorkflowmapid() {
		return workflowmapid;
	}
	public void setWorkflowmapid(Integer workflowmapid) {
		this.workflowmapid = workflowmapid;
	}
	public Integer getWorkflowcode() {
		return workflowcode;
	}
	public void setWorkflowcode(Integer workflowcode) {
		this.workflowcode = workflowcode;
	}
	public LSusergroup getLsusergroup() {
		return lsusergroup;
	}
	public void setLsusergroup(LSusergroup lsusergroup) {
		this.lsusergroup = lsusergroup;
	}
	
}
