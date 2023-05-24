package com.agaram.eln.primary.model.usermanagement.embeddable;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class ActiveuserId implements Serializable   {
	
	private Integer activeusercode;
	
	public ActiveuserId()
	{
		
	}
	
	public ActiveuserId(Integer activeusercode) {
        this.activeusercode = activeusercode;
    }

	public Integer getActiveusercode() {
		return activeusercode;
	}

	public void setActiveusercode(Integer activeusercode) {
		this.activeusercode = activeusercode;
	}

	public int hashCode() {
        return (int)this.activeusercode.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ActiveuserId)) return false;
        ActiveuserId pk = (ActiveuserId) obj;
        return pk.activeusercode.equals(this.activeusercode);
    }
}
