package com.agaram.eln.primary.model.usermanagement.embeddable;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class PolicyId implements Serializable {
	
	private Integer policycode;
	
	public PolicyId()
	{
		
	}
	public PolicyId(Integer policycode) {
        this.policycode = policycode;
    }

	public Integer PolicyCode() {
		return policycode;
	}

	public Integer getPolicycode() {
		return policycode;
	}

	public void setPolicycode(Integer policycode) {
		this.policycode = policycode;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
        if (!(obj instanceof PolicyId)) return false;
        PolicyId pk = (PolicyId) obj;
        return pk.policycode.equals(this.policycode);
	}

}
