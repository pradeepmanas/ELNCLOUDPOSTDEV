package com.agaram.eln.primary.fetchmodel.gettemplate;

import java.util.Date;


public class Protocoltemplateget implements Comparable<Protocoltemplateget>{
	public Integer protocolmastercode;
	public String protocolmastername;
	public Integer protocolstatus;
	public Integer status;
	public String createdbyusername;
	public String transactionstatus;
	private Date createdate;

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

	public Protocoltemplateget(Integer protocolmastercode, String protocolmastername, Integer protocolstatus,
			Integer status, String createdbyusername, Integer approved, Integer rejected,Date createdate) {

		this.protocolmastercode = protocolmastercode;
		this.protocolmastername = protocolmastername;
		this.protocolstatus = protocolstatus;
		this.status = status;
		this.createdbyusername = createdbyusername;
		this.createdate = createdate;
		this.transactionstatus = (rejected != null && rejected == 1) ? "rejected"
				: (approved == null ? "created" :approved == 1 ? "approved" : approved == 0 ? "initiated":"");
	}

	@Override
	public int compareTo(Protocoltemplateget o) {
		return this.getProtocolmastercode().compareTo(o.getProtocolmastercode());
	}	
}
