package com.agaram.eln.primary.model.sheetManipulation;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LSfilemethod")
public class LSfilemethod {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "filemethodcode")
	private Integer filemethodcode;
	
	private Integer filecode;
	@Column(columnDefinition = "varchar(120)")
	private String methodid;
	//private Integer isactive;
	//private Integer versionno;
	@Column(columnDefinition = "varchar(120)")
	private String instrumentid;
	public Integer getFilemethodcode() {
		return filemethodcode;
	}
	public void setFilemethodcode(Integer filemethodcode) {
		this.filemethodcode = filemethodcode;
	}
	public Integer getFilecode() {
		return filecode;
	}
	public void setFilecode(Integer filecode) {
		this.filecode = filecode;
	}
	public String getMethodid() {
		return methodid;
	}
	public void setMethodid(String methodid) {
		this.methodid = methodid;
	}
//	public Integer getIsactive() {
//		return isactive;
//	}
//	public void setIsactive(Integer isactive) {
//		this.isactive = isactive;
//	}
//	public Integer getVersionno() {
//		return versionno;
//	}
//	public void setVersionno(Integer versionno) {
//		this.versionno = versionno;
//	}
	public String getInstrumentid() {
		return instrumentid;
	}
	public void setInstrumentid(String instrumentid) {
		this.instrumentid = instrumentid;
	}
	
	
}
