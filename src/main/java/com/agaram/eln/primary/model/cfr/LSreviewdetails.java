package com.agaram.eln.primary.model.cfr;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;


@Entity
@Table(name = "LSreviewdetails")
public class LSreviewdetails {
	@Id
	private Integer serialno;
	@Column(columnDefinition = "varchar(100)")
	private String reviewstatus;
	@Column(columnDefinition = "varchar(100)")
	private String reviewedstatus;
	@Column(columnDefinition = "varchar(100)")
	private String reviewcomments;
	@Column(columnDefinition = "varchar(100)")
	private String reviewedby;
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date reviewdate;


	public Date getReviewdate() {
		return reviewdate;
	}
	public void setReviewdate(Date reviewdate) {
		this.reviewdate = reviewdate;
	}
	public LSuserMaster getLScfttransaction() {
		return LScfttransaction;
	}
	public void setLScfttransaction(LSuserMaster lScfttransaction) {
		LScfttransaction = lScfttransaction;
	}
	public LSuserMaster getLSreviewdetails() {
		return LSreviewdetails;
	}
	public void setLSreviewdetails(LSuserMaster lSreviewdetails) {
		LSreviewdetails = lSreviewdetails;
	}
	@ManyToOne 
	private LSuserMaster lsusermaster;
	@ManyToOne 
	private LSuserMaster LScfttransaction;
	@ManyToOne 
	private LSuserMaster LSreviewdetails;
	@ManyToOne 
	private LSSiteMaster lssitemaster;
	
	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}
	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}
	@Transient
	LScfttransaction objsilentaudit;
	
	@Transient
	LScfttransaction objmanualaudit;

	@Transient
	private Response response;
	
	@Transient
	LoggedUser objuser;
	
	@Transient
	private List<LSuserMaster> listofuserMaster;
	

	public List<LSuserMaster> getListofuserMaster() {
		return listofuserMaster;
	}
	public void setListofuserMaster(List<LSuserMaster> listofuserMaster) {
		this.listofuserMaster = listofuserMaster;
	}

	public LSuserMaster getLsusermaster() {
		return lsusermaster;
	}
	public void setLsusermaster(LSuserMaster lsusermaster) {
		this.lsusermaster = lsusermaster;
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
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
	public LoggedUser getObjuser() {
		return objuser;
	}
	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
	}
	public Integer getSerialno() {
		return serialno;
	}
	public void setSerialno(Integer serialno) {
		this.serialno = serialno;
	}
	public String getReviewstatus() {
		if(reviewstatus == "A")
		{
			return "Active";
		}
		else
		{
			return "Deactive";
		}
	}
	public void setReviewstatus(String reviewstatus) {
		this.reviewstatus = reviewstatus;
	}
	public String getReviewcomments() {
		return reviewcomments;
	}
	public void setReviewcomments(String reviewcomments) {
		this.reviewcomments = reviewcomments;
	}
	public String getReviewedby() {
		return reviewedby;
	}
	public void setReviewedby(String reviewedby) {
		this.reviewedby = reviewedby;
	}

	public String getReviewedstatus() {
		return reviewedstatus;
	}
	public void setReviewedstatus(String reviewedstatus) {
		this.reviewedstatus = reviewedstatus;
	}
	
}
