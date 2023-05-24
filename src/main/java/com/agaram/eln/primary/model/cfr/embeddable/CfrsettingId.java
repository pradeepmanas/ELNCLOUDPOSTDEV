package com.agaram.eln.primary.model.cfr.embeddable;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class CfrsettingId  implements Serializable   {
	
	private Integer cfrsettingcode;
	
	public Integer getcfrsettingcode() {
		return cfrsettingcode;
	}

	public void setcfrsettingcode(Integer cfrsettingcode) {
		this.cfrsettingcode = cfrsettingcode;
	}

	public CfrsettingId()
	{
		
	}
	
	public CfrsettingId(Integer cfrsettingcode) {
        this.cfrsettingcode = cfrsettingcode;
    }


	public int hashCode() {
        return (int)this.cfrsettingcode.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof CfrsettingId)) return false;
        CfrsettingId pk = (CfrsettingId) obj;
        return pk.cfrsettingcode.equals(this.cfrsettingcode);
    }

}

