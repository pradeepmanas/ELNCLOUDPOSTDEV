package com.agaram.eln.primary.model.protocols;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LSprotocolversionstepInfo {

	@Id
	@JsonProperty
	private Integer stepcode;
	
	public Integer getStepcode() {
		return stepcode;
	}
	public void setStepcode(Integer idRuncode) {
		this.stepcode = idRuncode;
	}
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
