package com.agaram.eln.primary.model.general;

import javax.persistence.Id;

import org.bson.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SheetCreation {
	 @Id
	 @JsonProperty
	 private long id;
	 @JsonProperty
	 String content;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
