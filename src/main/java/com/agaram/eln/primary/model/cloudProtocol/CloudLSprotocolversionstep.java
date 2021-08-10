package com.agaram.eln.primary.model.cloudProtocol;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

@Entity
@Table(name = "CloudLSprotocolversionstep")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class CloudLSprotocolversionstep {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer idversioncode;
	
	private Integer id;
	
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	public String lsprotocolstepInfo;
	
	@Column(columnDefinition = "varchar(100)")
	private String versionname;
	
	private Integer versionno;
	
	public String getVersionname() {
		return versionname;
	}
	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}
	public Integer status;
	
	public Integer protocolmastercode;
	
	public Integer getProtocolmastercode() {
		return protocolmastercode;
	}
	public void setProtocolmastercode(Integer protocolmastercode) {
		this.protocolmastercode = protocolmastercode;
	}
	public Integer getIdversioncode() {
		return idversioncode;
	}
	public void setIdversioncode(Integer idversioncode) {
		this.idversioncode = idversioncode;
	}
	
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
	public Integer getVersionno() {
		return versionno;
	}
	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}


