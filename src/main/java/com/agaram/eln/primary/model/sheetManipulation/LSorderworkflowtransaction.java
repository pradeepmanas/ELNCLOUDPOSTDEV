package com.agaram.eln.primary.model.sheetManipulation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "LSorderworkflowtransaction")
public class LSorderworkflowtransaction {
	@Id
	private Integer workflowtransactioncode;
	@Column(columnDefinition = "varchar(50)")
	private String usercode;
	private Integer workflowstep;
	private Integer approvelstatus;
	@Column(columnDefinition = "numeric(17,0)")
	private Long orderid;
	
	public Integer getWorkflowtransactioncode() {
		return workflowtransactioncode;
	}
	public void setWorkflowtransactioncode(Integer workflowtransactioncode) {
		this.workflowtransactioncode = workflowtransactioncode;
	}
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public Integer getWorkflowstep() {
		return workflowstep;
	}
	public void setWorkflowstep(Integer workflowstep) {
		this.workflowstep = workflowstep;
	}
	public Integer getApprovelstatus() {
		return approvelstatus;
	}
	public void setApprovelstatus(Integer approvelstatus) {
		this.approvelstatus = approvelstatus;
	}
	public Long getOrderid() {
		return orderid;
	}
	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}
	
	
}
