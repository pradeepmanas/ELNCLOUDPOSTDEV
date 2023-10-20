package com.agaram.eln.primary.model.material;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "elnresultusedmaterial")
public class ElnresultUsedMaterial {
	
	@Id
	@Column(name = "nresultusedmaterialcode") 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer nresultusedmaterialcode;

	@Column(name = "ordercode", nullable = false)
	private Long ordercode;

	@Column(name = "transactionscreen", nullable = false)//1 protocol template,2 sheet order,3 protocol order
	private Integer transactionscreen;
	
	@Column(name = "templatecode", nullable = false)
	private Integer templatecode;

	@Column(name = "nmaterialtypecode", nullable = false)
	private Integer nmaterialtypecode;

	@Column(name = "nmaterialcategorycode", nullable = false)
	private Integer nmaterialcategorycode;

	@Column(name = "nmaterialcode", nullable = false)
	private Integer nmaterialcode;

	@Column(name = "ninventorycode", nullable = false)
	private Integer ninventorycode;
	
	private Double nqtyissued;
	
	private Double nqtyused;

	private Double nqtyleft;
	
	@Column(name = "nstatus", nullable = false)
	private Integer nstatus = 1;
	
	@Column(name = "jsondata")
	private String jsondata;
	
	private String batchid;

	@ManyToOne
	private LSuserMaster createdbyusercode;
	
	@ManyToOne
	private LStestmasterlocal testcode;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createddate;
	
	@Transient
	Response response;

	public Integer getNresultusedmaterialcode() {
		return nresultusedmaterialcode;
	}

	public void setNresultusedmaterialcode(Integer nresultusedmaterialcode) {
		this.nresultusedmaterialcode = nresultusedmaterialcode;
	}

	public Long getOrdercode() {
		return ordercode;
	}

	public void setOrdercode(Long ordercode) {
		this.ordercode = ordercode;
	}

	public Integer getTransactionscreen() {
		return transactionscreen;
	}

	public void setTransactionscreen(Integer transactionscreen) {
		this.transactionscreen = transactionscreen;
	}

	public Integer getTemplatecode() {
		return templatecode;
	}

	public void setTemplatecode(Integer templatecode) {
		this.templatecode = templatecode;
	}

	public Integer getNmaterialtypecode() {
		return nmaterialtypecode;
	}

	public void setNmaterialtypecode(Integer nmaterialtypecode) {
		this.nmaterialtypecode = nmaterialtypecode;
	}

	public Integer getNmaterialcategorycode() {
		return nmaterialcategorycode;
	}

	public void setNmaterialcategorycode(Integer nmaterialcategorycode) {
		this.nmaterialcategorycode = nmaterialcategorycode;
	}

	public Integer getNmaterialcode() {
		return nmaterialcode;
	}

	public void setNmaterialcode(Integer nmaterialcode) {
		this.nmaterialcode = nmaterialcode;
	}

	public Integer getNinventorycode() {
		return ninventorycode;
	}

	public void setNinventorycode(Integer ninventorycode) {
		this.ninventorycode = ninventorycode;
	}

	public Double getNqtyissued() {
		return nqtyissued;
	}

	public void setNqtyissued(Double nqtyissued) {
		this.nqtyissued = nqtyissued;
	}

	public Double getNqtyused() {
		return nqtyused;
	}

	public void setNqtyused(Double nqtyused) {
		this.nqtyused = nqtyused;
	}

	public Double getNqtyleft() {
		return nqtyleft;
	}

	public void setNqtyleft(Double nqtyleft) {
		this.nqtyleft = nqtyleft;
	}

	public Integer getNstatus() {
		return nstatus;
	}

	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}

	public String getJsondata() {
		return jsondata;
	}

	public void setJsondata(String jsondata) {
		this.jsondata = jsondata;
	}

	public String getBatchid() {
		return batchid;
	}

	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}

	public LSuserMaster getCreatedbyusercode() {
		return createdbyusercode;
	}

	public void setCreatedbyusercode(LSuserMaster createdbyusercode) {
		this.createdbyusercode = createdbyusercode;
	}

	public LStestmasterlocal getTestcode() {
		return testcode;
	}

	public void setTestcode(LStestmasterlocal testcode) {
		this.testcode = testcode;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}
}