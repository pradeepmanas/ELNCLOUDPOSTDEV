package com.agaram.eln.primary.model.configuration;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LSconfiguration")
public class LSConfiguration {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	private Integer serialno;
	@Column(columnDefinition = "varchar(25)")
	private String configgrouptype;
	@Column(columnDefinition = "varchar(255)")
	private String configname;
	@Column(columnDefinition = "varchar(150)")
	private String configpath;
	@Column(columnDefinition = "varchar(255)")
	private String configusername;
	@Column(columnDefinition = "varchar(255)")
	private String configuserpass;
	private Integer configactive = 0;
	private Integer connectionstatus;
	private Integer status;
	public Integer getSerialno() {
		return serialno;
	}
	public void setSerialno(Integer serialno) {
		this.serialno = serialno;
	}
	public String getConfiggrouptype() {
		return configgrouptype;
	}
	public void setConfiggrouptype(String configgrouptype) {
		this.configgrouptype = configgrouptype;
	}
	public String getConfigname() {
		return configname;
	}
	public void setConfigname(String configname) {
		this.configname = configname;
	}
	public String getConfigpath() {
		return configpath;
	}
	public void setConfigpath(String configpath) {
		this.configpath = configpath;
	}
	public String getConfigusername() {
		return configusername;
	}
	public void setConfigusername(String configusername) {
		this.configusername = configusername;
	}
	public String getConfiguserpass() {
		return configuserpass;
	}
	public void setConfiguserpass(String configuserpass) {
		this.configuserpass = configuserpass;
	}
	public Integer getConfigactive() {
		return configactive;
	}
	public void setConfigactive(Integer configactive) {
		this.configactive = configactive;
	}
	public Integer getConnectionstatus() {
		return connectionstatus;
	}
	public void setConnectionstatus(Integer connectionstatus) {
		this.connectionstatus = connectionstatus;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
