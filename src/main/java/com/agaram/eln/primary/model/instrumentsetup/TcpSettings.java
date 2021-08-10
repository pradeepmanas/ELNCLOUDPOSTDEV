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
 * This class is used to map the fields of 'tcpsettings' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   05- Dec- 2019
 */
@XmlRootElement  (name = "tcpsettings")
@XmlType(propOrder = { "tcpmastkey", "instmaster", "protocol", "connectmode",
		"remotehostip","remoteport", "serverport", "retrycount", "timeout"})
@Entity
@Table(name = "tcpsettings")
public class TcpSettings implements Serializable, Diffable<TcpSettings>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@Column(name = "tcpsettingskey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer tcpmastkey;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "instmasterkey", nullable = false)
	private InstrumentMaster instmaster;
	
	@Column(name = "protocol")
	private int protocol = 0;
	
	@Column(name = "connectmode")
	private int connectmode = 0;
	
	@Column(name = "remotehostip")
	private String remotehostip;
	
	@Column(name = "remoteport")
	private int remoteport = 1024;
	
	@Column(name = "serverport")
	private int serverport = 1024;
	
	@Column(name = "retrycount")
	private int retrycount = -1;
	
	@Column(name = "timeout")
	private int timeout = 250;

	@XmlAttribute	
	public Integer getTcpmastkey() {
		return tcpmastkey;
	}

	public void setTcpmastkey(Integer tcpmastkey) {
		this.tcpmastkey = tcpmastkey;
	}

	@XmlElement
	public InstrumentMaster getInstmaster() {
		return instmaster;
	}

	public void setInstmaster(InstrumentMaster instmaster) {
		this.instmaster = instmaster;
	}

	@XmlElement
	public int getProtocol() {
		return protocol;
	}

	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}

	@XmlElement
	public int getConnectmode() {
		return connectmode;
	}

	public void setConnectmode(int connectmode) {
		this.connectmode = connectmode;
	}

	@XmlElement
	public String getRemotehostip() {
		return remotehostip;
	}

	public void setRemotehostip(String remotehostip) {
		this.remotehostip = remotehostip;
	}

	@XmlElement
	public int getRemoteport() {
		return remoteport;
	}

	public void setRemoteport(int remoteport) {
		this.remoteport = remoteport;
	}

	@XmlElement
	public int getServerport() {
		return serverport;
	}

	public void setServerport(int serverport) {
		this.serverport = serverport;
	}

	@XmlElement
	public int getRetrycount() {
		return retrycount;
	}

	public void setRetrycount(int retrycount) {
		this.retrycount = retrycount;
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
	public DiffResult diff(TcpSettings obj) {
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("instmaster", this.instmaster.getInstrumentcode(), obj.instmaster.getInstrumentcode()) 
	       .append("protocol", this.protocol, obj.protocol)	     
	       .append("connectmode", this.connectmode, obj.connectmode)	    
	       .append("remotehostip",this.remotehostip, obj.remotehostip)
	       .append("remoteport", this.remoteport, obj.remoteport)
	       .append("serverport", this.serverport, obj.serverport)
	       .append("retrycount", this.retrycount, obj.retrycount)
	       .append("timeout", this.timeout, obj.timeout)
	       .build();
	}

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param tcpSettings [TcpSettings]
	 */
	public TcpSettings(TcpSettings tcpSettings)
	{
		this.tcpmastkey = tcpSettings.tcpmastkey;
		this.instmaster = tcpSettings.instmaster;
		this.protocol = tcpSettings.protocol;
		this.connectmode = tcpSettings.connectmode;
		this.remotehostip = tcpSettings.remotehostip;
		this.remoteport = tcpSettings.remoteport;
		this.serverport = tcpSettings.serverport;
		this.retrycount = tcpSettings.retrycount;
		this.timeout = tcpSettings.timeout;
	}
	
	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public TcpSettings() {}

}
