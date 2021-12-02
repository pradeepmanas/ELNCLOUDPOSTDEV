package com.agaram.eln.primary.model.protocols;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LSprotocolorderfiles")
public class LSprotocolorderfiles {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer protocolorderstepfilecode;
	public Integer protocolorderstepcode;
	public Long protocolordercode;
	public Integer stepno;
	public String protocolstepname;
	public String fileid;
	public String extension;
	public String filename;
	public String oldfileid;
	public String getOldfileid() {
		return oldfileid;
	}
	public void setOldfileid(String oldfileid) {
		this.oldfileid = oldfileid;
	}
	public Integer getProtocolorderstepfilecode() {
		return protocolorderstepfilecode;
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
	public void setProtocolorderstepfilecode(Integer protocolorderstepfilecode) {
		this.protocolorderstepfilecode = protocolorderstepfilecode;
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
