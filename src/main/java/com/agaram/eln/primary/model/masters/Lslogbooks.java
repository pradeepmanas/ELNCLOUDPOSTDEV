package com.agaram.eln.primary.model.masters;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.agaram.eln.primary.model.general.Response;

@Entity
@Table(name = "Lslogbooks")
public class Lslogbooks {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "logbookcode")
	private Integer logbookcode;
	
	private String logbookname;
	
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	private String logbookfields;
	
	private String addedby;
	
	private Date addedon;
	
	private Integer usercode;
	
	private Integer sitecode;
	
	private Integer fieldcount=-1;

	@Transient
	Response objResponse;
	
	@Transient
	private Date fromdate;
	
	@Transient
	private Date todate;

	public Integer getLogbookcode() {
		return logbookcode;
	}

	public void setLogbookcode(Integer logbookcode) {
		this.logbookcode = logbookcode;
	}

	public String getLogbookname() {
		return logbookname;
	}

	public void setLogbookname(String logbookname) {
		this.logbookname = logbookname;
	}

	public String getLogbookfields() {
		return logbookfields;
	}

	public void setLogbookfields(String logbookfields) {
		this.logbookfields = logbookfields;
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

	public Integer getFieldcount() {
		return fieldcount;
	}

	public void setFieldcount(Integer fieldcount) {
		this.fieldcount = fieldcount;
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
	
	
}
