package com.agaram.eln.primary.model.sheetManipulation;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "LSfileparameter")
public class LSfileparameter {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "fileparametercode")
	private Integer fileparametercode;
	private Integer filecode;
	private Integer sheetid;
	private Integer sheetrowno;
	private Integer sheetcolno;
	//@Column(columnDefinition = "nvarchar(100)")
	private String parameter;
	private Integer isactive;
	private Integer versionno;
	private Integer limsParameterid;
	//@Column(columnDefinition = "nvarchar(255)")
	private String limsParametername;
	
	
	public Integer getFileparametercode() {
		return fileparametercode;
	}
	public void setFileparametercode(Integer fileparametercode) {
		this.fileparametercode = fileparametercode;
	}
	public Integer getFilecode() {
		return filecode;
	}
	public void setFilecode(Integer filecode) {
		this.filecode = filecode;
	}
	public Integer getSheetid() {
		return sheetid;
	}
	public void setSheetid(Integer sheetid) {
		this.sheetid = sheetid;
	}
	public Integer getSheetrowno() {
		return sheetrowno;
	}
	public void setSheetrowno(Integer sheetrowno) {
		this.sheetrowno = sheetrowno;
	}
	public Integer getSheetcolno() {
		return sheetcolno;
	}
	public void setSheetcolno(Integer sheetcolno) {
		this.sheetcolno = sheetcolno;
	}
	
	
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public Integer getIsactive() {
		return isactive;
	}
	public void setIsactive(Integer isactive) {
		this.isactive = isactive;
	}
	public Integer getVersionno() {
		return versionno;
	}
	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}
	public Integer getLimsParameterid() {
		return limsParameterid;
	}
	public void setLimsParameterid(Integer limsParameterid) {
		this.limsParameterid = limsParameterid;
	}
	public String getLimsParametername() {
		return limsParametername;
	}
	public void setLimsParametername(String limsParametername) {
		this.limsParametername = limsParametername;
	}
	
	
}
