package com.agaram.eln.primary.model.protocols;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LSprotocolvideos")
public class LSprotocolvideos {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer protocolstepvideoscode;
	public Integer protocolstepcode;
	public Integer protocolmastercode;
	public Integer stepno;
	public String protocolstepname;
	public String fileid;
	public String extension;
	public String filename;
	public Integer getProtocolstepvideoscode() {
		return protocolstepvideoscode;
	}
	public Integer getProtocolstepcode() {
		return protocolstepcode;
	}
	public Integer getProtocolmastercode() {
		return protocolmastercode;
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
	public void setProtocolstepvideoscode(Integer protocolstepvideoscode) {
		this.protocolstepvideoscode = protocolstepvideoscode;
	}
	public void setProtocolstepcode(Integer protocolstepcode) {
		this.protocolstepcode = protocolstepcode;
	}
	public void setProtocolmastercode(Integer protocolmastercode) {
		this.protocolmastercode = protocolmastercode;
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
