package com.agaram.eln.primary.model.methodsetup;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to map the fields of 'parserfield' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Mar- 2020
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@XmlRootElement  (name = "datatype")
@XmlType(propOrder = { "datatypekey", "datatypename"})
@Entity
@Table(name = "datatype")
public class DataType implements Serializable, Diffable<DataType>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "datatypekey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer datatypekey;


	@Column(name = "datatypename")
	private String datatypename;	
	
	@XmlElement	
	public Integer getDatatypekey() {
		return datatypekey;
	}

	public void setDatatypekey(Integer datatypekey) {
		this.datatypekey = datatypekey;
	}

	@XmlElement	
	public String getDatatypename() {
		return datatypename;
	}

	public void setDatatypename(String datatypename) {
		this.datatypename = datatypename;
	}

	/**
	 * To find difference between two entity objects by implementing Diffable interface  
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public DiffResult diff(DataType obj) {
		
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("datatypename", this.datatypename, obj.datatypename)    
	       .build();
	}

	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public DataType() {
	}

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param parserField [ParserField]
	 */
	public DataType(DataType dataType) {	

		this.datatypekey = dataType.datatypekey;
		this.datatypename = dataType.datatypename;
	
	}	
	
}
