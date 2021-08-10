package com.agaram.eln.primary.model.protocols;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LSprotocolstepversion")
public class LSprotocolstepversion implements Comparable<LSprotocolstepversion>{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer protocolstepversioncode;
	public Integer getProtocolstepversioncode() {
		return protocolstepversioncode;
	}
	public void setProtocolstepversioncode(Integer protocolstepversioncode) {
		this.protocolstepversioncode = protocolstepversioncode;
	}
	public Integer protocolstepcode;
	public Integer protocolmastercode;
	public Integer stepno;
	public String protocolstepname;
	
	public Integer status;
	
	private Integer versionno;
	
	public Integer getVersionno() {
		return versionno;
	}
	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}
	public Integer getProtocolstepcode() {
		return protocolstepcode;
	}
	public void setProtocolstepcode(Integer protocolstepcode) {
		this.protocolstepcode = protocolstepcode;
	}
	public Integer getProtocolmastercode() {
		return protocolmastercode;
	}
	public void setProtocolmastercode(Integer protocolmastercode) {
		this.protocolmastercode = protocolmastercode;
	}
	public Integer getStepno() {
		return stepno;
	}
	public void setStepno(Integer stepno) {
		this.stepno = stepno;
	}
	public String getProtocolstepname() {
		return protocolstepname;
	}
	public void setProtocolstepname(String protocolstepname) {
		this.protocolstepname = protocolstepname;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public int compareTo(LSprotocolstepversion o) {
		return this.getProtocolstepcode().compareTo(o.getProtocolstepcode());
	}
}