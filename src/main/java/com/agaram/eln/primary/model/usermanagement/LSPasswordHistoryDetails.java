package com.agaram.eln.primary.model.usermanagement;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "LSpasswordhistorydetails")
public class LSPasswordHistoryDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "passwordcode")
	private Integer passwordcode;
	@Column(columnDefinition = "varchar(255)")
	private String password;
	@Temporal(TemporalType.TIMESTAMP)
	private Date passwordcreatedate;
	
	@ManyToOne
	private LSuserMaster lsusermaster;
	
	public Integer getPasswordcode() {
		return passwordcode;
	}
	public void setPasswordcode(Integer passwordcode) {
		this.passwordcode = passwordcode;
	}
	public LSuserMaster getLsusermaster() {
//		if(this.objuser!=null)
//			{
//				this.lsusermaster = this.objuser.getLsusermaster();
//			}
		return lsusermaster;
	}
	public void setLsusermaster(LSuserMaster lsusermaster) {
		this.lsusermaster = lsusermaster;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getPasswordcreatedate() {
		return passwordcreatedate;
	}
	public void setPasswordcreatedate(Date passwordcreatedate) {
		this.passwordcreatedate = passwordcreatedate;
	}
}
