package com.agaram.eln.primary.model.methodsetup;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bson.types.Binary;

@Entity
@Table(name="CloudParserFile")
public class CloudParserFile {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer parserfilecode;
	public String fileid;
	public String extension;
	public String filename;
	public String originalfilename;
	//private Binary file;
	 
	public Integer getParserfilecode() {
		return parserfilecode;
	}
	public void setParserfilecode(Integer parserfilecode) {
		this.parserfilecode = parserfilecode;
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
//	public Binary getFile() {
//		return file;
//	}
//	public void setFile(Binary file) {
//		this.file = file;
//	}
	public String getOriginalfilename() {
		return originalfilename;
	}
	public void setOriginalfilename(String originalfilename) {
		this.originalfilename = originalfilename;
	}

	
}
