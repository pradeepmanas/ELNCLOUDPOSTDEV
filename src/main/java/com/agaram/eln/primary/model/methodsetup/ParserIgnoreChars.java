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
 * This class is used to map the fields of 'parserignorechars' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   01- May- 2020
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@Entity
@Table(name = "parserignorechars")
public class ParserIgnoreChars implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "parserignorecharskey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer parserignorecharskey;
	
	@Column(name = "ignorechars")
	private String ignorechars;

	public Integer getParserignorecharskey() {
		return parserignorecharskey;
	}

	public void setParserignorecharskey(Integer parserignorecharskey) {
		this.parserignorecharskey = parserignorecharskey;
	}

	public String getIgnorechars() {
		return ignorechars;
	}

	public void setIgnorechars(String ignorechars) {
		this.ignorechars = ignorechars;
	}	
}
