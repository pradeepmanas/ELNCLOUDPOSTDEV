package com.agaram.eln.primary.model.fileManipulation;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;

//@Document(indexName = "blog", type = "article")
public class OrderAttachment {
	@Id
    private String id;
        
    private Binary file;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Binary getFile() {
		return file;
	}

	public void setFile(Binary file) {
		this.file = file;
	}

	
    
    
}
