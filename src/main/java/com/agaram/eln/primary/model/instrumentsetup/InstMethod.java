package com.agaram.eln.primary.model.instrumentsetup;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.validator.constraints.Range;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

/**
 * This class is used to map the fields of 'method' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   07- May- 2020
 */
@Entity
@Table(name = "method")
public class InstMethod implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "methodkey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer methodkey;
	
	@Column(name = "methodname")
	private String methodname;	

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "instmasterkey", nullable = false)
	private InstrumentMaster instmaster;	
	
	@Column(name = "instrawdataurl")
	private String instrawdataurl;	
	
	@Column(name = "samplesplit")
	private Integer samplesplit;
	
	@Column(name = "parser")
	private Integer parser;
		
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sitecode", nullable = false)
	private LSSiteMaster site;
	
	@Range(min=-1, max=1)
	@Column(name = "status")
	private int status=1;	

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usercode", nullable = false)
	private LSuserMaster createdby;
	
	@Column(name = "createddate")
	private Date createddate;	
	
	public Integer getMethodkey() {
		return methodkey;
	}

	public void setMethodkey(Integer methodkey) {
		this.methodkey = methodkey;
	}

	public String getMethodname() {
		return methodname;
	}

	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}

	public InstrumentMaster getInstmaster() {
		return instmaster;
	}

	public void setInstmaster(InstrumentMaster instmaster) {
		this.instmaster = instmaster;
	}

	public String getInstrawdataurl() {
		return instrawdataurl;
	}

	public void setInstrawdataurl(String instrawdataurl) {
		this.instrawdataurl = instrawdataurl;
	}

	public Integer getSamplesplit() {
		return samplesplit;
	}

	public void setSamplesplit(Integer samplesplit) {
		this.samplesplit = samplesplit;
	}
	
	public Integer getParser() {
		return parser;
	}

	public void setParser(Integer parser) {
		this.parser = parser;
	}

	public LSSiteMaster getSite() {
		return site;
	}

	public void setSite(LSSiteMaster site) {
		this.site = site;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public LSuserMaster getCreatedby() {
		return createdby;
	}

	public void setCreatedby(LSuserMaster createdby) {
		this.createdby = createdby;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}
}
