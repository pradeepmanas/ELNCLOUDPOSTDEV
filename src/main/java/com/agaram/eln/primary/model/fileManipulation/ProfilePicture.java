package com.agaram.eln.primary.model.fileManipulation;

import javax.persistence.Transient;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.agaram.eln.primary.model.cfr.LScfttransaction;

@Document(collection = "ProfilePicture")
public class ProfilePicture {
    @Id
    private Integer id;
    
    private String name;
        
    private Binary image;

    @Transient
	LScfttransaction objsilentaudit;
    
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

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
    
    
}
