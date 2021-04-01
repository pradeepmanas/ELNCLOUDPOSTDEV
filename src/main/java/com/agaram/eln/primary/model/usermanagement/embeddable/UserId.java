package com.agaram.eln.primary.model.usermanagement.embeddable;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class UserId implements Serializable {
	private Integer usercode;
	private String userid;
	
	public Integer getUsercode() {
		return usercode;
	}

	public void setUsercode(Integer usercode) {
		this.usercode = usercode;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public UserId()
	{
		
	}
	
	public UserId(Integer usercode, String userid) {
        this.usercode = usercode;
        this.userid = userid;
    }

	public int hashCode() {
        return (int)this.usercode.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof UserId)) return false;
        UserId pk = (UserId) obj;
        return pk.usercode.equals(this.usercode) && pk.userid.equals(this.userid);
    }
	
	

}
