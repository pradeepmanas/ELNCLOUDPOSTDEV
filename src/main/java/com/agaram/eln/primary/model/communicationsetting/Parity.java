package com.agaram.eln.primary.model.communicationsetting;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "parity")
public class Parity implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Id 
	@Column(name = "paritykey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer paritykey;

	private String parityname;
	
	private int status=1;

	public Integer getParitykey() {
		return paritykey;
	}

	public void setParitykey(Integer paritykey) {
		this.paritykey = paritykey;
	}

	public String getParityname() {
		return parityname;
	}

	public void setParityname(String parityname) {
		this.parityname = parityname;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
	
	
}
