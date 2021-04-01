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
@Table(name = "LSOrderCreationfiles")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class CloudOrderCreation {
	@Id
	private long id;
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	private String content;
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	private String contentvalues;
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	private String contentparameter;
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
