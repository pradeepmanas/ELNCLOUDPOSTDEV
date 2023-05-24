package com.agaram.eln.primary.model.methodsetup;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to map the fields of 'controltype' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   01- Apr- 2020
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@Entity
@Table(name = "controltype")
public class ControlType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "controltypekey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer controltypekey;
	
	@Column(name = "controltypename")
	private String controltypename;

	public Integer getControltypekey() {
		return controltypekey;
	}

	public void setControltypekey(Integer controltypekey) {
		this.controltypekey = controltypekey;
	}

	public String getControltypename() {
		return controltypename;
	}

	public void setControltypename(String controltypename) {
		this.controltypename = controltypename;
	}	
}
