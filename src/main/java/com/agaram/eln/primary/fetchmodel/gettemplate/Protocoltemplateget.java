package com.agaram.eln.primary.fetchmodel.gettemplate;

import java.util.Date;

import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;


public class Protocoltemplateget implements Comparable<Protocoltemplateget>{
	public Integer protocolmastercode;
	public String protocolmastername;
	public Integer protocolstatus;
	public Integer status;
	public String createdbyusername;
	public String transactionstatus;
	private Date createdate;
	private String lssheetworkflowname;
	public Integer versionno;
	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getTransactionstatus() {
		return transactionstatus;
	}

	public void setTransactionstatus(String transactionstatus) {
		this.transactionstatus = transactionstatus;
	}

	public Integer getProtocolmastercode() {
		return protocolmastercode;
	}

	public void setProtocolmastercode(Integer protocolmastercode) {
		this.protocolmastercode = protocolmastercode;
	}

	public String getProtocolmastername() {
		return protocolmastername;
	}

	public void setProtocolmastername(String protocolmastername) {
		this.protocolmastername = protocolmastername;
	}

	public Integer getProtocolstatus() {
		return protocolstatus;
	}

	public void setProtocolstatus(Integer protocolstatus) {
		this.protocolstatus = protocolstatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreatedbyusername() {
		return createdbyusername;
	}

	public void setCreatedbyusername(String createdbyusername) {
		this.createdbyusername = createdbyusername;
	}

	public String getLssheetworkflowname() {
		return lssheetworkflowname;
	}

	public void setLssheetworkflowname(String lssheetworkflowname) {
		this.lssheetworkflowname = lssheetworkflowname;
	}

	
	public Integer getVersionno() {
		return versionno;
	}

	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}

	public Protocoltemplateget(Integer protocolmastercode, String protocolmastername, Integer protocolstatus,
			Integer status, String createdbyusername, Integer approved, Integer rejected,Date createdate,LSsheetworkflow lssheetworkflow,Integer versionno) {

		this.protocolmastercode = protocolmastercode;
		this.protocolmastername = protocolmastername;
		this.protocolstatus = protocolstatus;
		this.status = status;
		this.createdbyusername = createdbyusername;
		this.createdate = createdate;
		this.transactionstatus = (rejected != null && rejected == 1) ? "Rejected"
				: (approved == null ? "Created" :approved == 1 ? "Approved" : approved == 0 ? "Initiated":approved == 2?"Return":"");
		this.lssheetworkflowname=lssheetworkflow!=null?lssheetworkflow.getWorkflowname():null;
		this.versionno=versionno;
	}

	@Override
	public int compareTo(Protocoltemplateget o) {
		return this.getProtocolmastercode().compareTo(o.getProtocolmastercode());
	}	
}
