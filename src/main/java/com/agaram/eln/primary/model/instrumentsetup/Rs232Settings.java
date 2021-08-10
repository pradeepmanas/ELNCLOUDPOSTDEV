package com.agaram.eln.primary.model.instrumentsetup;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;

/**
 * This class is used to map the fields of 'rs232settings' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   05- Dec- 2019
 */
@XmlRootElement  (name = "rs232settings")
@XmlType(propOrder = { "rs232mastkey", "instmaster", "comport", "baudrate",
		"databits","parity","stopbits", "handshake","timeout"})
@Entity
@Table(name = "rs232settings")
public class Rs232Settings implements Serializable, Diffable<Rs232Settings>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@Column(name = "rs232settingskey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer rs232mastkey;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "instmasterkey", nullable = false)
	private InstrumentMaster instmaster;
	
	@Column(name = "comport")
	private int comport = 1;
	
	@Column(name = "baudrate")
	private String baudrate = "9600";
	
	@Column(name = "databits")
	private String databits = "8";
	
	@Column(name = "parity")
	private String parity = "None" ;
	
	@Column(name = "stopbits")
	private String stopbits = "1";
	
	@Column(name = "handshake")
	private int handshake = 0;
	
	@Column(name = "timeout")
	private int timeout = 250;
	
	@XmlAttribute	
	public Integer getRs232mastkey() {
		return rs232mastkey;
	}

	public void setRs232mastkey(Integer rs232mastkey) {
		this.rs232mastkey = rs232mastkey;
	}

	@XmlElement
	public InstrumentMaster getInstmaster() {
		return instmaster;
	}

	public void setInstmaster(InstrumentMaster instmaster) {
		this.instmaster = instmaster;
	}

	@XmlElement
	public int getComport() {
		return comport;
	}

	public void setComport(int comport) {
		this.comport = comport;
	}

	@XmlElement
	public String getBaudrate() {
		return baudrate;
	}

	public void setBaudrate(String baudrate) {
		this.baudrate = baudrate;
	}

	@XmlElement
	public String getDatabits() {
		return databits;
	}

	public void setDatabits(String databits) {
		this.databits = databits;
	}

	@XmlElement
	public String getParity() {
		return parity;
	}

	public void setParity(String parity) {
		this.parity = parity;
	}

	@XmlElement
	public String getStopbits() {
		return stopbits;
	}

	public void setStopbits(String stopbits) {
		this.stopbits = stopbits;
	}

	@XmlElement
	public int getHandshake() {
		return handshake;
	}

	public void setHandshake(int handshake) {
		this.handshake = handshake;
	}

	@XmlElement
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	/**
	 * To find difference between two entity objects by implementing Diffable interface  
	 */
	@Override
	public DiffResult diff(Rs232Settings obj) {
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("instmaster", this.instmaster.getInstrumentcode(), obj.instmaster.getInstrumentcode()) 
	       .append("comport", this.comport, obj.comport)	     
	       .append("baudrate", this.baudrate, obj.baudrate)	    
	       .append("databits",this.databits, obj.databits)
	       .append("parity", this.parity, obj.parity)
	       .append("stopbits", this.stopbits, obj.stopbits)
	       .append("handshake", this.handshake, obj.handshake)
	       .append("timeout", this.timeout, obj.timeout)
	       .build();
	}

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param rs232Settings [Rs232Settings]
	 */
	public Rs232Settings(Rs232Settings rs232Settings)
	{
		this.instmaster = rs232Settings.instmaster;
		this.comport = rs232Settings.comport;
		this.baudrate = rs232Settings.baudrate;
		this.databits = rs232Settings.databits;
		this.parity = rs232Settings.parity;
		this.stopbits = rs232Settings.stopbits;
		this.handshake = rs232Settings.handshake;
		this.timeout = rs232Settings.timeout;
	}
	
	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public Rs232Settings() {}

}
