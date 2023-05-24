package com.agaram.eln.primary.model.usermanagement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "LSpasswordhistory")
public class LSpasswordhistory {
	@Id
//	@ManyToOne
	@JoinColumn(name = "userid", referencedColumnName = "userid")
	@Column(columnDefinition = "char(20)")
	private String userid;
//	@ManyToOne
//	@JoinColumn(name = "bok_aut_id", referencedColumnName = "aut_id")
//	private Author bok_aut_id;
	@Column(columnDefinition = "varchar(100)")
	private String firstpassword;
	@Column(columnDefinition = "varchar(100)")
	private String secondpassword;
	@Column(columnDefinition = "varchar(100)")
	private String thirdpassword;
	@Column(columnDefinition = "varchar(100)")
	private String fourthpassword;
	@Column(columnDefinition = "varchar(100)")
	private String fifthpassword;
	@Column(columnDefinition = "char(10)")
	private String sitecode;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getFirstpassword() {
		return firstpassword;
	}
	public void setFirstpassword(String firstpassword) {
		this.firstpassword = firstpassword;
	}
	public String getSecondpassword() {
		return secondpassword;
	}
	public void setSecondpassword(String secondpassword) {
		this.secondpassword = secondpassword;
	}
	public String getThirdpassword() {
		return thirdpassword;
	}
	public void setThirdpassword(String thirdpassword) {
		this.thirdpassword = thirdpassword;
	}
	public String getFourthpassword() {
		return fourthpassword;
	}
	public void setFourthpassword(String fourthpassword) {
		this.fourthpassword = fourthpassword;
	}
	public String getFifthpassword() {
		return fifthpassword;
	}
	public void setFifthpassword(String fifthpassword) {
		this.fifthpassword = fifthpassword;
	}
	public String getSitecode() {
		return sitecode;
	}
	public void setSitecode(String sitecode) {
		this.sitecode = sitecode;
	}
	
	
}
