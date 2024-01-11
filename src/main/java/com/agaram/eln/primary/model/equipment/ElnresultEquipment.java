package com.agaram.eln.primary.model.equipment;

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
@Table(name = "elnresultequipment")
public class ElnresultEquipment {
	
	@Id
	@Column(name = "nresultequipmentcode") 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer nresultequipmentcode;

	@Column(name = "ordercode")
	private Long ordercode;

	@Column(name = "transactionscreen")//1 protocol template,2 sheet order,3 protocol order
	private Integer transactionscreen;
	
	@Column(name = "templatecode")
	private Integer templatecode;

	@Column(name = "nequipmenttypecode")
	private Integer nequipmenttypecode;

	@Column(name = "nequipmentcatcode")
	private Integer nequipmentcatcode;

	@Column(name = "nequipmentcode")
	private Integer nequipmentcode;
	
	@Column(name = "nstatus", nullable = false)
	private Integer nstatus = 1;
	
	private String batchid;

	@ManyToOne
	private LSuserMaster createdby;
	
	@ManyToOne
	private LStestmasterlocal lstestmasterlocal;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createddate;
	
	@Transient
	Response response;

	public Integer getNresultequipmentcode() {
		return nresultequipmentcode;
	}

	public void setNresultequipmentcode(Integer nresultequipmentcode) {
		this.nresultequipmentcode = nresultequipmentcode;
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

	public Integer getNequipmenttypecode() {
		return nequipmenttypecode;
	}

	public void setNequipmenttypecode(Integer nequipmenttypecode) {
		this.nequipmenttypecode = nequipmenttypecode;
	}

	public Integer getNequipmentcatcode() {
		return nequipmentcatcode;
	}

	public void setNequipmentcatcode(Integer nequipmentcatcode) {
		this.nequipmentcatcode = nequipmentcatcode;
	}

	public Integer getNequipmentcode() {
		return nequipmentcode;
	}

	public void setNequipmentcode(Integer nequipmentcode) {
		this.nequipmentcode = nequipmentcode;
	}

	public Integer getNstatus() {
		return nstatus;
	}

	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}

	public String getBatchid() {
		return batchid;
	}

	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}

	public LSuserMaster getCreatedby() {
		return createdby;
	}

	public void setCreatedby(LSuserMaster createdby) {
		this.createdby = createdby;
	}

	public LStestmasterlocal getLstestmasterlocal() {
		return lstestmasterlocal;
	}

	public void setLstestmasterlocal(LStestmasterlocal lstestmasterlocal) {
		this.lstestmasterlocal = lstestmasterlocal;
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