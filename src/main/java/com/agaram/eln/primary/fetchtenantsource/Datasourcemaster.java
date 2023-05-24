package com.agaram.eln.primary.fetchtenantsource;

public class Datasourcemaster {
	private Long id;
	private String name;
	private String url;
	private String username;
	private String password;
	private String useremail;
	private String tenantpassword;
	private String driverClassName;
	private String tenantid;
	private String archivename;
	private String archiveurl;
	
	public Datasourcemaster(Long id, String name, String url, String username, String password, String useremail,
			String tenantpassword, String driverClassName, String tenantid, String archivename, String archiveurl) {
		
		this.id = id;
		this.name = name;
		this.url = url;
		this.username = username;
		this.password = password;
		this.useremail = useremail;
		this.tenantpassword = tenantpassword;
		this.driverClassName = driverClassName;
		this.tenantid = tenantid;
		this.archivename = archivename;
		this.archiveurl = archiveurl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public String getTenantpassword() {
		return tenantpassword;
	}

	public void setTenantpassword(String tenantpassword) {
		this.tenantpassword = tenantpassword;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getTenantid() {
		return tenantid;
	}

	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}

	public String getArchivename() {
		return archivename;
	}

	public void setArchivename(String archivename) {
		this.archivename = archivename;
	}

	public String getArchiveurl() {
		return archiveurl;
	}

	public void setArchiveurl(String archiveurl) {
		this.archiveurl = archiveurl;
	}
}
