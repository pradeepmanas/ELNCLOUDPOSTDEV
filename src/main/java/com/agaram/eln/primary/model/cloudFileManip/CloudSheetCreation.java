package com.agaram.eln.primary.model.cloudFileManip;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

@Entity
@Table(name = "LSSheetCreationfiles")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class CloudSheetCreation {
	@Id
	private long id;
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	private String content;
	private String fileuid;
	private Integer containerstored;
	private String fileuri;
	public String getFileuri() {
		return fileuri;
	}
	public void setFileuri(String fileuri) {
		this.fileuri = fileuri;
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
	public String getFileuid() {
		return fileuid;
	}
	public void setFileuid(String fileuid) {
		this.fileuid = fileuid;
	}
	public Integer getContainerstored() {
		return containerstored;
	}
	public void setContainerstored(Integer containerstored) {
		this.containerstored = containerstored;
	}
}