package com.agaram.eln.primary.model.masters;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.agaram.eln.primary.model.general.Response;

@Entity
@Table(name = "Lslogbooks")
public class Lslogbooks {
	
	@Id
	@SequenceGenerator(name = "orderGen", sequenceName = "orderDetail", initialValue = 1000000, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderGen")
	@Column(columnDefinition = "numeric(17,0)", name = "logbookcode")
	private Long logbookcode;
	
	private String logbookid;
	
//	public Long getLogbookbatchid() {
//		return logbookbatchid;
//	}
//
//	public void setLogbookbatchid(Long logbookbatchid) {
//		this.logbookbatchid = logbookbatchid;
//	}

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Basic(optional = false)
//	@Column(name = "logbookcode")
//	private Integer logbookcode;
	
	public Long getLogbookcode() {
		return logbookcode;
	}

	public void setLogbookcode(Long logbookcode) {
		this.logbookcode = logbookcode;
	}

	public String getLogbookid() {
		return logbookid;
	}

	public void setLogbookid(String logbookid) {
		this.logbookid = logbookid;
	}

	private String logbookname;
	private String logbookcategory;
	
	public String getLogbookcategory() {
		return logbookcategory;
	}

	public void setLogbookcategory(String logbookcategory) {
		this.logbookcategory = logbookcategory;
	}

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
	
	
	public Integer getRetirestatus() {
		return retirestatus;
	}

	public void setRetirestatus(Integer retirestatus) {
		this.retirestatus = retirestatus;
	}

	@Transient
	private Date fromdate;
	
	@Transient
	private Date todate;

	@Column(name = "retirestatus")
	private Integer retirestatus;
	
	@Column(columnDefinition = "varchar(10)")
	private String userstatus;
	
	public String getUserstatus() {
		if(userstatus != null)
		{
		return  userstatus.trim().equals("Active")?"Active":"Retired";
		}
		else
		{
			return "";
		}
	}
	private String reviewstatus;
	
	public String getReviewstatus() {
		if(reviewstatus != null)
		{
		return  reviewstatus.trim().equals("Review")?"Review":"";
		}
		else
		{
			return "";
		}
	}

	public void setReviewstatus(String reviewstatus) {
		this.reviewstatus = reviewstatus;
	}

	public void setUserstatus(String userstatus) {
		this.userstatus = userstatus;
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
	
	@Transient
	private boolean select;

	public boolean isSelect() {
		return select;
	}
	public void setSelect(boolean select) {
		this.select = select;
	}
	
	
}
