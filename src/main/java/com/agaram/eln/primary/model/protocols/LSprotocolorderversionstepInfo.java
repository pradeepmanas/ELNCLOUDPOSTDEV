package com.agaram.eln.primary.model.protocols;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LSprotocolorderversionstepInfo {
	@Id
	@JsonProperty
	private Integer stepcode;
	@JsonProperty
	private long id;
	@JsonProperty
	String content;
	private int versionno;
	public Integer getStepcode() {
		return stepcode;
	}
	public long getId() {
		return id;
	}
	public String getContent() {
		return content;
	}
	public int getVersionno() {
		return versionno;
	}
	public void setStepcode(Integer stepcode) {
		this.stepcode = stepcode;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setVersionno(int versionno) {
		this.versionno = versionno;
	}
}
