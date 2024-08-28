package com.agaram.eln.primary.model.instrumentDetails;

import java.util.Date;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity(name = "LsAutoregister")
@Table(name = "LsAutoregister")
public class LsAutoregister {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	
	
//	 @Id
//	 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lsautoregister_seq")
	// @SequenceGenerator( allocationSize = 1, name = "lsautoregister_seq")
	 
//	@Column(columnDefinition = "numeric(17,0)",name = "regcode") 
	private Long regcode;
	
	@Column(columnDefinition = "numeric(17,0)", name = "batchcode")
	private Long batchcode;


	private String screen;
	
	private Boolean repeat;
	
	private Integer interval;
	
	private String timespan;
	
	private Long delayinminutes;
	
	public Long getDelayinminutes() {
		return delayinminutes;
	}

	public void setDelayinminutes(Long delayinminutes) {
		this.delayinminutes = delayinminutes;
	}

	@Column(name = "autocreatedate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date autocreatedate;

	@Column(name = "stoptime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date stoptime;
	
	private Integer ismultitenant;
	
    private Boolean isautoreg;
    
    private String timerIdname;
    
	public String getTimerIdname() {
		return timerIdname;
	}

	public void setTimerIdname(String timerIdname) {
		this.timerIdname = timerIdname;
	}

	public Boolean getIsautoreg() {
		return isautoreg;
	}

	public void setIsautoreg(Boolean isautoreg) {
		this.isautoreg = isautoreg;
	}

	public Integer getIsmultitenant() {
		return ismultitenant;
	}

	public void setIsmultitenant(Integer ismultitenant) {
		this.ismultitenant = ismultitenant;
	}

	public Long getRegcode() {
		return regcode;
	}

	public void setRegcode(Long regcode) {
		this.regcode = regcode;
	}

	public Long getBatchcode() {
		return batchcode;
	}

	public void setBatchcode(Long batchcode) {
		this.batchcode = batchcode;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public Boolean getRepeat() {
		return repeat;
	}

	public void setRepeat(Boolean repeat) {
		this.repeat = repeat;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public String getTimespan() {
		return timespan;
	}

	public void setTimespan(String timespan) {
		this.timespan = timespan;
	}

	public Date getAutocreatedate() {
		return autocreatedate;
	}

	public void setAutocreatedate(Date autocreatedate) {
		this.autocreatedate = autocreatedate;
	}

	public Date getStoptime() {
		return stoptime;
	}

	public void setStoptime(Date stoptime) {
		this.stoptime = stoptime;
	}
	
	
}
