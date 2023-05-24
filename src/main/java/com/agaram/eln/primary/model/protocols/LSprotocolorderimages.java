package com.agaram.eln.primary.model.protocols;

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
@Table(name="LSprotocolorderimages")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class LSprotocolorderimages {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer protocolorderstepimagecode;
	public Integer protocolorderstepcode;
	public Long protocolordercode;
	public Integer stepno;
	public String protocolstepname;
	public String fileid;
	public String oldfileid;
	public String extension;
	public String filename;
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	public String src;
	
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	public String oldsrc;
	
	public String getOldsrc() {
		return oldsrc;
	}
	public void setOldsrc(String oldsrc) {
		this.oldsrc = oldsrc;
	}
	public String getOldfileid() {
		return oldfileid;
	}
	public void setOldfileid(String oldfileid) {
		this.oldfileid = oldfileid;
	}
	public Integer getProtocolorderstepimagecode() {
		return protocolorderstepimagecode;
	}
	public Integer getProtocolorderstepcode() {
		return protocolorderstepcode;
	}
	public Long getProtocolordercode() {
		return protocolordercode;
	}
	public Integer getStepno() {
		return stepno;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getProtocolstepname() {
		return protocolstepname;
	}
	public String getFileid() {
		return fileid;
	}
	public String getExtension() {
		return extension;
	}
	public String getFilename() {
		return filename;
	}
	public void setProtocolorderstepimagecode(Integer protocolorderstepimagecode) {
		this.protocolorderstepimagecode = protocolorderstepimagecode;
	}
	public void setProtocolorderstepcode(Integer protocolorderstepcode) {
		this.protocolorderstepcode = protocolorderstepcode;
	}
	public void setProtocolordercode(Long protocolordercode) {
		this.protocolordercode = protocolordercode;
	}
	public void setStepno(Integer stepno) {
		this.stepno = stepno;
	}
	public void setProtocolstepname(String protocolstepname) {
		this.protocolstepname = protocolstepname;
	}
	public void setFileid(String fileid) {
		this.fileid = fileid;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	
}
