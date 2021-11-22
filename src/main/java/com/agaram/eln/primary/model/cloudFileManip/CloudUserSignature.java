package com.agaram.eln.primary.model.cloudFileManip;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.bson.types.Binary;

import com.agaram.eln.primary.model.cfr.LScfttransaction;

@Entity
@Table(name = "LSUsersignature")
public class CloudUserSignature {
	 @Id
	 private Integer id;
	    
	 private String name;
	        
	 private Binary image;

	 @Transient
	LScfttransaction objsilentaudit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Binary getImage() {
		return image;
	}

	public void setImage(Binary image) {
		this.image = image;
	}

	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}
	 
	 
}
