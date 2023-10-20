package com.agaram.eln.primary.model.cloudProtocol;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

@Entity
@Table(name = "LSprotocolstepInformation")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class LSprotocolstepInformation {
	@Id
	private Integer id;
	
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	public String lsprotocolstepInfo;

	
//	private String fileuid;
//	
//	private Integer containerstored;
//	
//	private String fileuri;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLsprotocolstepInfo() {
		return lsprotocolstepInfo;
	}
	public void setLsprotocolstepInfo(String myJSON) {
		this.lsprotocolstepInfo = myJSON;
	}
//	public String getFileuid() {
//		return fileuid;
//	}
//	public void setFileuid(String fileuid) {
//		this.fileuid = fileuid;
//	}
//	public Integer getContainerstored() {
//		return containerstored;
//	}
//	public void setContainerstored(Integer containerstored) {
//		this.containerstored = containerstored;
//	}
//	public String getFileuri() {
//		return fileuri;
//	}
//	public void setFileuri(String fileuri) {
//		this.fileuri = fileuri;
//	}
}
