package com.agaram.eln.primary.model.usermanagement.embeddable;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class SiteId  implements Serializable{

	private Integer sitecode;
	private String siteid;
	
	public SiteId()
	{
		
	}
	
	public SiteId(Integer sitecode, String siteid) {
        this.sitecode = sitecode;
        this.siteid = siteid;
    }

	public Integer getSitecode() {
		return sitecode;
	}

	public void setSitecode(Integer sitecode) {
		this.sitecode = sitecode;
	}

	public String getsiteid() {
		return siteid;
	}

	public void setsiteid(String siteid) {
		this.siteid = siteid;
	}
	
	public int hashCode() {
        return (int)this.sitecode.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof SiteId)) return false;
        SiteId pk = (SiteId) obj;
        return pk.sitecode.equals(this.sitecode) && pk.siteid.equals(this.siteid);
    }
	
	
}
