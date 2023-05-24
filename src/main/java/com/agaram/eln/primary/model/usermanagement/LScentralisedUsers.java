package com.agaram.eln.primary.model.usermanagement;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LScentralisedUsers")
public class LScentralisedUsers {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "centralisedusercode")
	private int centralisedusercode;
	
	@Column(columnDefinition = "varchar(500)")
	private String unifieduserid;
	
	@Column(name = "usercode")
	private Integer usercode;
	
	@Column(columnDefinition = "varchar(255)")
	private String username;
	
	@Column(name = "sitecode")
	private Integer sitecode;
	
	@Column(name = "sitename", columnDefinition = "varchar(255)")
	private String sitename;
	
	@Column(name = "id")
	private Long id;
	
	@Column(name = "tenantid", columnDefinition = "varchar(255)")
	private String tenantid;
	
	@Column(name = "tenantname", columnDefinition = "varchar(255)")
    private String tenantname;

	public int getCentralisedusercode() {
		return centralisedusercode;
	}

	public void setCentralisedusercode(int centralisedusercode) {
		this.centralisedusercode = centralisedusercode;
	}

	public String getUnifieduserid() {
		return unifieduserid;
	}

	public void setUnifieduserid(String unifieduserid) {
		this.unifieduserid = unifieduserid;
	}

	public Integer getUsercode() {
		return usercode;
	}

	public void setUsercode(Integer usercode) {
		this.usercode = usercode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getSitecode() {
		return sitecode;
	}

	public void setSitecode(Integer sitecode) {
		this.sitecode = sitecode;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTenantid() {
		return tenantid;
	}

	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}

	public String getTenantname() {
		return tenantname;
	}

	public void setTenantname(String tenantname) {
		this.tenantname = tenantname;
	}
	
	
	
}
