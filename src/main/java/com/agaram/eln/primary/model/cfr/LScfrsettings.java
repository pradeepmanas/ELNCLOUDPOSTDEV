package com.agaram.eln.primary.model.cfr;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.agaram.eln.primary.model.cfr.embeddable.CfrsettingId;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

@Entity
@Table(name = "LScfrsettings")
@IdClass(CfrsettingId.class)
public class LScfrsettings {
	@Id
	private Integer cfrsettingcode;
	private Integer auditTrail;
	private Integer enableRecords;
	private Integer cfrarchive;
	private Integer archivenodays;
	private String L49SiteCode;
	
	@Id
	@ManyToOne(cascade=CascadeType.ALL) 
	private LSSiteMaster lssitemaster;

	public Integer getCfrsettingcode() {
		return cfrsettingcode;
	}

	public void setCfrsettingcode(Integer cfrsettingcode) {
		this.cfrsettingcode = cfrsettingcode;
	}

	public Integer getAuditTrail() {
		return auditTrail;
	}

	public void setAuditTrail(Integer auditTrail) {
		this.auditTrail = auditTrail;
	}

	public Integer getEnableRecords() {
		return enableRecords;
	}

	public void setEnableRecords(Integer enableRecords) {
		this.enableRecords = enableRecords;
	}

	public Integer getCfrarchive() {
		return cfrarchive;
	}

	public void setCfrarchive(Integer cfrarchive) {
		this.cfrarchive = cfrarchive;
	}

	public Integer getArchivenodays() {
		return archivenodays;
	}

	public void setArchivenodays(Integer archivenodays) {
		this.archivenodays = archivenodays;
	}

	public String getL49SiteCode() {
		return L49SiteCode;
	}

	public void setL49SiteCode(String l49SiteCode) {
		L49SiteCode = l49SiteCode;
	}

	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}

	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	} 
	
	
}
