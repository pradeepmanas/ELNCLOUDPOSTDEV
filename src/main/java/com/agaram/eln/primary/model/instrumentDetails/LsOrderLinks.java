package com.agaram.eln.primary.model.instrumentDetails;

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
@Table(name = "lsorderlinks")
public class LsOrderLinks {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "norderlinkcode")
	private Integer norderlinkcode;

	@Column(name = "batchcode")
	private Long batchcode;

	@Column(name = "batchid")
	private String batchid;

	@Column(name = "nstatus")
	private Integer nstatus;

	@ManyToOne
	private LSuserMaster createby;

	private Date createddate;

	private Integer nsitecode;

	@Column(columnDefinition = "varchar(500)", name = "link")
	private String link;

	public Integer getOrderlinkcode() {
		return norderlinkcode;
	}

	public void setOrderlinkcode(Integer norderlinkcode) {
		this.norderlinkcode = norderlinkcode;
	}

	public Long getBatchcode() {
		return batchcode;
	}

	public void setBatchcode(Long batchcode) {
		this.batchcode = batchcode;
	}

	public String getBatchid() {
		return batchid;
	}

	public void setBatchid(String batchid) {
		this.batchid = batchid;
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
