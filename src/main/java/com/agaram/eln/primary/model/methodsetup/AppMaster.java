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
 * This class is used to map the fields of 'appmaster' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   01- Apr- 2020
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@Entity
@Table(name = "appmaster")
public class AppMaster implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "appmasterkey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer appmasterkey;
	
	@Column(name = "appmastername")
	private String appmastername;

	public Integer getAppmasterkey() {
		return appmasterkey;
	}

	public void setAppmasterkey(Integer appmasterkey) {
		this.appmasterkey = appmasterkey;
	}

	public String getAppmastername() {
		return appmastername;
	}

	public void setAppmastername(String appmastername) {
		this.appmastername = appmastername;
	}

}
