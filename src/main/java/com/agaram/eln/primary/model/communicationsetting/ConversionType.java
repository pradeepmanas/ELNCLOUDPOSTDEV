package com.agaram.eln.primary.model.communicationsetting;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "conversiontype")
public class ConversionType implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "conversiontypekey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer conversiontypekey;
	
	private String conversiontypename;
	
	private int status=1;

	public Integer getConversiontypekey() {
		return conversiontypekey;
	}

	public void setConversiontypekey(Integer conversiontypekey) {
		this.conversiontypekey = conversiontypekey;
	}

	public String getConversiontypename() {
		return conversiontypename;
	}

	public void setConversiontypename(String conversiontypename) {
		this.conversiontypename = conversiontypename;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
