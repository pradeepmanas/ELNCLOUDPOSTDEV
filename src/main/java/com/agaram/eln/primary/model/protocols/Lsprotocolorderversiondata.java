package com.agaram.eln.primary.model.protocols;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Lsprotocolorderversiondata {
	@Id
	@JsonProperty
	private long id;
	@JsonProperty
	String content;
	private int versionno;
	
	public int getVersionno() {
		return versionno;
	}
	public void setVersionno(int versionno) {
		this.versionno = versionno;
	}
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
