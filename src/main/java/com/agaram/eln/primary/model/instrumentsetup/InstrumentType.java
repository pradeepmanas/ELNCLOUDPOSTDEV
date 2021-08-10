package com.agaram.eln.primary.model.instrumentsetup;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class is used to map the fields of 'instrumenttype' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   05- Dec- 2019
 */
@Entity
@Table(name = "instrumenttype")
public class InstrumentType implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@Column(name = "insttypekey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer insttypekey;
	
	@Column(name = "insttypename")
	private String insttypename;
	
	@Column(name = "status")
	private int status=1;
	
	public InstrumentType() {}

	public Integer getInsttypekey() {
		return insttypekey;
	}

	public void setInsttypekey(Integer insttypekey) {
		this.insttypekey = insttypekey;
	}

	public String getInsttypename() {
		return insttypename;
	}

	public void setInsttypename(String insttypename) {
		this.insttypename = insttypename;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
