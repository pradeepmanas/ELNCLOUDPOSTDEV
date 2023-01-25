package com.agaram.eln.primary.model.material;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;

@Entity
@Table(name = "section")
public class Section {
	@Id
	@Column(name = "nsectioncode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer nsectioncode;
	
	@Column(name = "ssectionname",columnDefinition = "varchar(120)")
	private String ssectionname;
	
	@Column(name = "sdescription",columnDefinition = "varchar(250)")
	private String sdescription;
	
	@Column(name = "nstatus")
	private Integer nstatus;
	
	@Column(name = "ndefaultstatus")
	private Integer ndefaultstatus;
	
	@Column(name = "nsitecode")
	private Integer nsitecode;

	@Transient
	private LScfttransaction objsilentaudit;
	
	@Transient
	public String info;
	
	@Transient
	private LScfttransaction objmanualaudit;
	
	public Integer getNsectioncode() {
		return nsectioncode;
	}

	public void setNsectioncode(Integer nsectioncode) {
		this.nsectioncode = nsectioncode;
	}

	public String getSsectionname() {
		return ssectionname;
	}

	public void setSsectionname(String ssectionname) {
		this.ssectionname = ssectionname;
	}

	public String getSdescription() {
		return sdescription;
	}

	public void setSdescription(String sdescription) {
		this.sdescription = sdescription;
	}

	public Integer getNstatus() {
		return nstatus;
	}

	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}

	public Integer getNdefaultstatus() {
		return ndefaultstatus;
	}

	public void setNdefaultstatus(Integer ndefaultstatus) {
		this.ndefaultstatus = ndefaultstatus;
	}

	public Integer getNsitecode() {
		return nsitecode;
	}

	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}

	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	
}
