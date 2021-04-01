package com.agaram.eln.primary.model.cloudProtocol;

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
@Table(name = "LSprotocolstepInfoCloud")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class CloudLSprotocolstepInfo {
	@Id
	private Integer id;
	
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	public String lsprotocolstepInfo;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLsprotocolstepInfo() {
		return lsprotocolstepInfo;
	}
	public void setLsprotocolstepInfo(String lsprotocolstepInfo) {
		this.lsprotocolstepInfo = lsprotocolstepInfo;
	}
}
