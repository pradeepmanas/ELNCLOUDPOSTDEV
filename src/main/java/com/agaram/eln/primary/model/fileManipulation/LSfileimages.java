package com.agaram.eln.primary.model.fileManipulation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LSfileimages")
//@TypeDefs({
//    @TypeDef(name = "json", typeClass = JsonStringType.class),
//    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
//})
public class LSfileimages {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer fileimagecode;
//	public Integer filecode;
//	public String filename;
	public String fileid;
	public String extension;
//	@Type(type = "jsonb")
//    @Column(columnDefinition = "jsonb")
	public String src;
	
	public Integer getFileimagecode() {
		return fileimagecode;
	}
	public void setFileimagecode(Integer fileimagecode) {
		this.fileimagecode = fileimagecode;
	}
//	public Integer getFilecode() {
//		return filecode;
//	}
//	public void setFilecode(Integer filecode) {
//		this.filecode = filecode;
//	}
//	public String getFilename() {
//		return filename;
//	}
//	public void setFilename(String filename) {
//		this.filename = filename;
//	}
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
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}	
}