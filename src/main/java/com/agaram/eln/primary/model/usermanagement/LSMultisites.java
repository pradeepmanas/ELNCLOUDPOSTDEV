package com.agaram.eln.primary.model.usermanagement;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "LSMultisites")
public class LSMultisites {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private int multisitecode;
	private Integer usercode;
	@ManyToOne 
	private LSSiteMaster lssiteMaster;
	private Integer defaultsiteMaster;
	
	@Transient
	private Integer multiusergroupcode;
	
	public int getMultisitecode() {
		return multisitecode;
	}
	public void setMultisitecode(int multisitecode) {
		this.multisitecode = multisitecode;
	}
	public Integer getMultiusergroupcode() {
		return multiusergroupcode;
	}
	public void setMultiusergroupcode(Integer multiusergroupcode) {
		this.multiusergroupcode = multiusergroupcode;
	}
	public Integer getUsercode() {
		return usercode;
	}
	public void setUsercode(Integer usercode) {
		this.usercode = usercode;
	}
	public LSSiteMaster getLssiteMaster() {
		return lssiteMaster;
	}
	public void setLssiteMaster(LSSiteMaster lssiteMaster) {
		this.lssiteMaster = lssiteMaster;
	}
	public Integer getDefaultsiteMaster() {
		return defaultsiteMaster;
	}
	public void setDefaultsiteMaster(Integer defaultsiteMaster) {
		this.defaultsiteMaster = defaultsiteMaster;
	}

	
}
