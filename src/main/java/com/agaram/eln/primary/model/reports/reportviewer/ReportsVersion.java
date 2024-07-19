package com.agaram.eln.primary.model.reports.reportviewer;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "ReportsVersion")
public class ReportsVersion {
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    private Long reportversioncode;

    private Long reportcode;

    private Integer versionno;

    private Date datecreated;

    private Date datemodified;

    private String fileuid;

    private String fileuri;

    private Integer containerstored;

    private String fileextention;
    
    public Integer createdby;
    
    private Integer sitecode;
    
    @Transient
	private boolean isnewversion;
	
	@Transient
	private Integer ismultitenant;

    // Getters and Setters
    public Long getReportversioncode() {
        return reportversioncode;
    }

    public void setReportversioncode(Long reportversioncode) {
        this.reportversioncode = reportversioncode;
    }

    public Integer getCreatedby() {
		return createdby;
	}

	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}

	public Integer getSitecode() {
		return sitecode;
	}

	public void setSitecode(Integer sitecode) {
		this.sitecode = sitecode;
	}

	public boolean isIsnewversion() {
		return isnewversion;
	}

	public void setIsnewversion(boolean isnewversion) {
		this.isnewversion = isnewversion;
	}

	public Integer getIsmultitenant() {
		return ismultitenant;
	}

	public void setIsmultitenant(Integer ismultitenant) {
		this.ismultitenant = ismultitenant;
	}

	public Long getReportcode() {
		return reportcode;
	}

	public void setReportcode(Long reportcode) {
		this.reportcode = reportcode;
	}

    public Integer getVersionno() {
		return versionno;
	}

	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}

	public Date getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(Date datecreated) {
        this.datecreated = datecreated;
    }

    public Date getDatemodified() {
        return datemodified;
    }

    public void setDatemodified(Date datemodified) {
        this.datemodified = datemodified;
    }

    public String getFileuid() {
        return fileuid;
    }

    public void setFileuid(String fileuid) {
        this.fileuid = fileuid;
    }

    public String getFileuri() {
        return fileuri;
    }

    public void setFileuri(String fileuri) {
        this.fileuri = fileuri;
    }

    public Integer getContainerstored() {
        return containerstored;
    }

    public void setContainerstored(Integer containerstored) {
        this.containerstored = containerstored;
    }

    public String getFileextention() {
        return fileextention;
    }

    public void setFileextention(String fileextention) {
        this.fileextention = fileextention;
    }
}
