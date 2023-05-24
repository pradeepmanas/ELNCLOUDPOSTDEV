package com.agaram.eln.primary.model.cloudFileManip;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bson.types.Binary;

//import org.springframework.data.elasticsearch.annotations.Document;

@Entity
@Table(name = "LSOrderAttachmentfiles")
public class CloudOrderAttachment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
    private Integer id;
        
    private Binary file;

    private String fileid;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Binary getFile() {
		return file;
	}

	public void setFile(Binary file) {
		this.file = file;
	}

	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}

	
    
    
}
