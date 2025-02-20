package com.agaram.eln.primary.model.masters;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.agaram.eln.primary.model.general.Response;

@Entity
@Table(name = "Lslogbooksdata")
public class Lslogbooksdata {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "logbookdatacode")
	private Integer logbookdatacode;
	
	@Column(name = "logbookcode")
	private Integer logbookcode;
	
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	private String logbookdatafields;
	
	private String logbookitemname;
	
	private String addedby;
	
	private Date addedon;
	
	private Integer usercode;
	
	private Integer sitecode;
	
	private Integer itemstatus =1;
	
	private String logbookuniqueid;
	
	@Transient
	Response objResponse;
	
	@Transient
	private Date fromdate;
	
	@Transient
	private Date todate;
	@Column(name = "logitemstatus")
	private String logitemstatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifieddate;

	@Column(name = "modifiedby")
	private String modifiedby;
	public Date getModifieddate() {
		return modifieddate;
	}

	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}

	public String getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}
	

	public String getLogitemstatus() {
		if(logitemstatus != null) {
			return logitemstatus.trim().equals("A")?"Active":"Retired";
		}else {
			return "";
		}
		
	}

	public void setLogitemstatus(String logitemstatus) {
		this.logitemstatus = logitemstatus;
	}

	public Integer getLogbookdatacode() {
		return logbookdatacode;
	}

	public void setLogbookdatacode(Integer logbookdatacode) {
		this.logbookdatacode = logbookdatacode;
	}

	public Integer getLogbookcode() {
		return logbookcode;
	}

	public void setLogbookcode(Integer logbookcode) {
		this.logbookcode = logbookcode;
	}

	public String getAddedby() {
		return addedby;
	}

	public void setAddedby(String addedby) {
		this.addedby = addedby;
	}

	public Date getAddedon() {
		return addedon;
	}

	public void setAddedon(Date addedon) {
		this.addedon = addedon;
	}

	public Integer getUsercode() {
		return usercode;
	}

	public void setUsercode(Integer usercode) {
		this.usercode = usercode;
	}

	public Integer getSitecode() {
		return sitecode;
	}

	public void setSitecode(Integer sitecode) {
		this.sitecode = sitecode;
	}

	public Integer getItemstatus() {
		return itemstatus;
	}

	public void setItemstatus(Integer itemstatus) {
		this.itemstatus = itemstatus;
	}

	public Response getObjResponse() {
		return objResponse;
	}

	public void setObjResponse(Response objResponse) {
		this.objResponse = objResponse;
	}

	public Date getFromdate() {
		return fromdate;
	}

	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}

	public Date getTodate() {
		return todate;
	}

	public void setTodate(Date todate) {
		this.todate = todate;
	}

	public String getLogbookdatafields() {
		return logbookdatafields;
	}

	public void setLogbookdatafields(String logbookdatafields) {
		this.logbookdatafields = logbookdatafields;
	}

	public String getLogbookitemname() {
		return logbookitemname;
	}

	public void setLogbookitemname(String logbookitemname) {
		this.logbookitemname = logbookitemname;
	}

	public String getLogbookuniqueid() {
		return logbookuniqueid;
	}

	public void setLogbookuniqueid(String logbookuniqueid) {
		this.logbookuniqueid = logbookuniqueid;
	}
	
	
}
