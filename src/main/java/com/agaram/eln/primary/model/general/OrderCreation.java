package com.agaram.eln.primary.model.general;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.index.TextIndexed;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderCreation {
	@Id
	 @JsonProperty
	 private long id;
	 @JsonProperty
	 String content;
	 @JsonProperty
	String contentvalues;
	 @JsonProperty
		String contentparameter;
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
	public String getContentvalues() {
		return contentvalues;
	}
	public void setContentvalues(String contentvalues) {
		this.contentvalues = contentvalues;
	}
	public String getContentparameter() {
		return contentparameter;
	}
	public void setContentparameter(String contentparameter) {
		this.contentparameter = contentparameter;
	}
	 
}
