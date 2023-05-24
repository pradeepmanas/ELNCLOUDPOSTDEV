package com.agaram.eln.primary.model.usermanagement.embeddable;

import java.io.Serializable;

import javax.persistence.Embeddable;


@SuppressWarnings("serial")
@Embeddable
public class DomainId implements Serializable  {
	private Integer domaincode;
	private String domainid;
	
	public DomainId()
	{
		
	}
	
	public Integer getDomaincode() {
		return domaincode;
	}

	public void setDomaincode(Integer domaincode) {
		this.domaincode = domaincode;
	}

	public String getDomainid() {
		return domainid;
	}

	public void setDomainid(String domainid) {
		this.domainid = domainid;
	}

	public DomainId(Integer domaincode, String domainid) {
        this.domaincode = domaincode;
        this.domainid = domainid;
    }

}
