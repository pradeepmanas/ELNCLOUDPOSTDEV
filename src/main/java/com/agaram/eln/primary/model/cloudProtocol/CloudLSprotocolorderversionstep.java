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
@Table(name = "CloudLSprotocolorderversionstep")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class CloudLSprotocolorderversionstep {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer idversioncode;
	
	private Integer protocolorderstepversioncode;
	
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	public String lsprotocolstepInfo;
	
	@Column(columnDefinition = "varchar(100)")
	private String versionname;
	
	private Integer versionno;
	
	public Integer status;
	
	private Long protocolordercode;

	public Integer getIdversioncode() {
		return idversioncode;
	}

	public Integer getProtocolorderstepversioncode() {
		return protocolorderstepversioncode;
	}

	public String getLsprotocolstepInfo() {
		return lsprotocolstepInfo;
	}

	public String getVersionname() {
		return versionname;
	}

	public Integer getVersionno() {
		return versionno;
	}

	public Integer getStatus() {
		return status;
	}


	public void setIdversioncode(Integer idversioncode) {
		this.idversioncode = idversioncode;
	}

	public void setProtocolorderstepversioncode(Integer protocolorderstepversioncode) {
		this.protocolorderstepversioncode = protocolorderstepversioncode;
	}

	public void setLsprotocolstepInfo(String lsprotocolstepInfo) {
		this.lsprotocolstepInfo = lsprotocolstepInfo;
	}

	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}

	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getProtocolordercode() {
		return protocolordercode;
	}

	public void setProtocolordercode(Long protocolordercode) {
		this.protocolordercode = protocolordercode;
	}


}
