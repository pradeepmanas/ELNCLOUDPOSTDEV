package com.agaram.eln.primary.model.communicationsetting;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stopbits")
public class StopBits implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "stopbitkey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer stopbitkey;
	
	private String stopbitname;
	
	private int status=1;

	public Integer getStopbitkey() {
		return stopbitkey;
	}

	public void setStopbitkey(Integer stopbitkey) {
		this.stopbitkey = stopbitkey;
	}

	public String getStopbitname() {
		return stopbitname;
	}

	public void setStopbitname(String stopbitname) {
		this.stopbitname = stopbitname;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	


}
