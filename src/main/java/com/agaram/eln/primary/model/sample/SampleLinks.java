package com.agaram.eln.primary.model.sample;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "samplelinks")
public class SampleLinks {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "nsamplelinkcode")
	private Integer nsamplelinkcode;

	@Column(name = "nsamplecode")
	private Integer nsamplecode;

	@Column(name = "nsamplecatcode")
	private Integer nsamplecatcode;

	@Column(name = "nsampletypecode")
	private Integer nsampletypecode;

	@Column(name = "nstatus")
	private Integer nstatus;

	@ManyToOne
	private LSuserMaster createby;

	private Date createddate;

	private Integer nsitecode;

	@Column(columnDefinition = "varchar(500)", name = "link")
	private String link;

	public Integer getNsamplelinkcode() {
		return nsamplelinkcode;
	}

	public void setNsamplelinkcode(Integer nsamplelinkcode) {
		this.nsamplelinkcode = nsamplelinkcode;
	}

	public Integer getNsampletypecode() {
		return nsampletypecode;
	}

	public void setNsampletypecode(Integer nsampletypecode) {
		this.nsampletypecode = nsampletypecode;
	}

	public Integer getNsamplecatcode() {
		return nsamplecatcode;
	}

	public void setNsamplecatcode(Integer nsamplecatcode) {
		this.nsamplecatcode = nsamplecatcode;
	}

	public Integer getNsamplecode() {
		return nsamplecode;
	}

	public void setNsamplecode(Integer nsamplecode) {
		this.nsamplecode = nsamplecode;
	}

	public Integer getNstatus() {
		return nstatus;
	}

	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}

	public LSuserMaster getCreateby() {
		return createby;
	}

	public void setCreateby(LSuserMaster createby) {
		this.createby = createby;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public Integer getNsitecode() {
		return nsitecode;
	}

	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
