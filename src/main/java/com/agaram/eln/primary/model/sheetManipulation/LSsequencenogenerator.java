package com.agaram.eln.primary.model.sheetManipulation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LSsequencenogenerator")
public class LSsequencenogenerator {
	@Id
	private Integer settingcode;
	@Column(columnDefinition = "varchar(50)")
	private String tablename;
	@Column(columnDefinition = "varchar(50)")
	private String prefix;
	private Integer sequenceno;
	private Integer nseqnolength;
	@Column(columnDefinition = "varchar(50)")
	private String nformattype;
	public Integer getSettingcode() {
		return settingcode;
	}
	public void setSettingcode(Integer settingcode) {
		this.settingcode = settingcode;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public Integer getSequenceno() {
		return sequenceno;
	}
	public void setSequenceno(Integer sequenceno) {
		this.sequenceno = sequenceno;
	}
	public Integer getNseqnolength() {
		return nseqnolength;
	}
	public void setNseqnolength(Integer nseqnolength) {
		this.nseqnolength = nseqnolength;
	}
	public String getNformattype() {
		return nformattype;
	}
	public void setNformattype(String nformattype) {
		this.nformattype = nformattype;
	}
	
	
}
