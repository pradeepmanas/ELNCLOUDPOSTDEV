package com.agaram.eln.primary.model.protocols;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LSprotocolmethod")
public class LSprotocolmethod{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer protocolmethodcode;
	
	public Integer protocolmastercode;

	@Column(columnDefinition = "varchar(120)")
	private String methodid;
	
	@Column(columnDefinition = "varchar(120)")
	private String instrumentid;
	
	public Integer stepcode;
	
	public Integer sectioncode;

	public Integer getProtocolmethodcode() {
		return protocolmethodcode;
	}

	public void setProtocolmethodcode(Integer protocolmethodcode) {
		this.protocolmethodcode = protocolmethodcode;
	}

	public Integer getProtocolmastercode() {
		return protocolmastercode;
	}

	public void setProtocolmastercode(Integer protocolmastercode) {
		this.protocolmastercode = protocolmastercode;
	}

	public String getMethodid() {
		return methodid;
	}

	public void setMethodid(String methodid) {
		this.methodid = methodid;
	}

	public String getInstrumentid() {
		return instrumentid;
	}

	public void setInstrumentid(String instrumentid) {
		this.instrumentid = instrumentid;
	}

	public Integer getStepcode() {
		return stepcode;
	}

	public void setStepcode(Integer stepcode) {
		this.stepcode = stepcode;
	}

	public Integer getSectioncode() {
		return sectioncode;
	}

	public void setSectioncode(Integer sectioncode) {
		this.sectioncode = sectioncode;
	}
	
}
