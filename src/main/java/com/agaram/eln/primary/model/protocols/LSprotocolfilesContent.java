package com.agaram.eln.primary.model.protocols;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;

public class LSprotocolfilesContent {

	@Id
    private Integer id;
	
    private String name;
    
    private String fileid;
    
        
    private Binary file;


	public Integer getId() {
		return id;
	}


	public String getName() {
		return name;
	}


	public String getFileid() {
		return fileid;
	}


	public Binary getFile() {
		return file;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setFileid(String fileid) {
		this.fileid = fileid;
	}


	public void setFile(Binary file) {
		this.file = file;
	}
}
