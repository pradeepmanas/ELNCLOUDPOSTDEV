package com.agaram.eln.primary.model.usermanagement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LScontrol")
public class LScontrol {
	@Id
	private Integer controlcode;

	@Column(columnDefinition = "char(10)")
	private String controlid;
	@Column(columnDefinition = "varchar(120)")
	private String dbversion;
	@Column(columnDefinition = "BYTEA")
	private String licence;
	@Column(columnDefinition = "int")
	private Integer norder;
	@Column(columnDefinition = "int")
	private Integer llpro;
	@Column(columnDefinition = "varchar(120)")
	private String llprodatasource;
	@Column(columnDefinition = "varchar(120)")
	private String llprodbname;
	@Column(columnDefinition = "varchar(120)")
	private String llprousername;
	@Column(columnDefinition = "varchar(120)")
	private String llpropassword;
	@Column(columnDefinition = "char(10)")
	private String sitecode;
	public Integer getControlcode() {
		return controlcode;
	}
	public void setControlcode(Integer controlcode) {
		this.controlcode = controlcode;
	}
	public String getControlid() {
		return controlid;
	}
	public void setControlid(String controlid) {
		this.controlid = controlid;
	}
	public String getDbversion() {
		return dbversion;
	}
	public void setDbversion(String dbversion) {
		this.dbversion = dbversion;
	}
	public String getLicence() {
		return licence;
	}
	public void setLicence(String licence) {
		this.licence = licence;
	}
	public Integer getNorder() {
		return norder;
	}
	public void setNorder(Integer norder) {
		this.norder = norder;
	}
	public Integer getLlpro() {
		return llpro;
	}
	public void setLlpro(Integer llpro) {
		this.llpro = llpro;
	}
	public String getLlprodatasource() {
		return llprodatasource;
	}
	public void setLlprodatasource(String llprodatasource) {
		this.llprodatasource = llprodatasource;
	}
	public String getLlprodbname() {
		return llprodbname;
	}
	public void setLlprodbname(String llprodbname) {
		this.llprodbname = llprodbname;
	}
	public String getLlprousername() {
		return llprousername;
	}
	public void setLlprousername(String llprousername) {
		this.llprousername = llprousername;
	}
	public String getLlpropassword() {
		return llpropassword;
	}
	public void setLlpropassword(String llpropassword) {
		this.llpropassword = llpropassword;
	}
	public String getSitecode() {
		return sitecode;
	}
	public void setSitecode(String sitecode) {
		this.sitecode = sitecode;
	}
}
