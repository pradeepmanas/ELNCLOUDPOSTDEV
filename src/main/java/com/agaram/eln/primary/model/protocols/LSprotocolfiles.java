package com.agaram.eln.primary.model.protocols;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="LSprotocolfiles")
public class LSprotocolfiles {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer protocolstepfilecode;
	public Integer protocolstepcode;
	public Integer protocolmastercode;
	public Integer stepno;
	public String protocolstepname;
	public String fileid;
	public String extension;
	public String filename;
	private Integer version;
	public String editoruuid;
	
	@Transient
	private String link;
	
	public boolean islinkfile;
	
	public boolean isIslinkfile() {
		return islinkfile;
	}
	public void setIslinkfile(boolean islinkfile) {
		this.islinkfile = islinkfile;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Integer getProtocolstepfilecode() {
		return protocolstepfilecode;
	}
	public void setProtocolstepfilecode(Integer protocolstepfilecode) {
		this.protocolstepfilecode = protocolstepfilecode;
	}
	public Integer getProtocolstepcode() {
		return protocolstepcode;
	}
	public void setProtocolstepcode(Integer protocolstepcode) {
		this.protocolstepcode = protocolstepcode;
	}
	public Integer getProtocolmastercode() {
		return protocolmastercode;
	}
	public void setProtocolmastercode(Integer protocolmastercode) {
		this.protocolmastercode = protocolmastercode;
	}
	public Integer getStepno() {
		return stepno;
	}
	public void setStepno(Integer stepno) {
		this.stepno = stepno;
	}
	public String getProtocolstepname() {
		return protocolstepname;
	}
	public void setProtocolstepname(String protocolstepname) {
		this.protocolstepname = protocolstepname;
	}
	public String getFileid() {
		return fileid;
	}
	public void setFileid(String fileid) {
		this.fileid = fileid;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getEditoruuid() {
		return editoruuid;
	}
	public void setEditoruuid(String editoruuid) {
		this.editoruuid = editoruuid;
	}
	
	
	
}
