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
 * This class is used to map the fields of 'filesettings' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   05- Dec- 2019
 */
@XmlRootElement  (name = "filesettings")
@XmlType(propOrder = { "filemastkey", "instmaster", "filemode", "fileinterval"})
@Entity
@Table(name = "filesettings")
public class FileSettings implements Serializable, Diffable<FileSettings>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@Column(name = "filesettingskey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer filemastkey;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "instmasterkey", nullable = false)
	private InstrumentMaster instmaster;
	
	@Column(name = "filemode")
	private int filemode = 0;
	
	@Column(name = "fileinterval")
	private int fileinterval = 0;

	@XmlAttribute	
	public Integer getFilemastkey() {
		return filemastkey;
	}

	public void setFilemastkey(Integer filemastkey) {
		this.filemastkey = filemastkey;
	}

	@XmlElement
	public InstrumentMaster getInstmaster() {
		return instmaster;
	}

	public void setInstmaster(InstrumentMaster instmaster) {
		this.instmaster = instmaster;
	}

	@XmlElement
	public int getFilemode() {
		return filemode;
	}

	public void setFilemode(int filemode) {
		this.filemode = filemode;
	}

	@XmlElement
	public int getFileinterval() {
		return fileinterval;
	}

	public void setFileinterval(int fileinterval) {
		this.fileinterval = fileinterval;
	}
	
	
	/**
	 * To find difference between two entity objects by implementing Diffable interface  
	 */
	@Override
	public DiffResult diff(FileSettings obj) {
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("instmaster", this.instmaster.getInstrumentcode(), obj.instmaster.getInstrumentcode()) 
	       .append("filemode", this.filemode, obj.filemode)	     
	       .append("fileinterval", this.fileinterval, obj.fileinterval)	  
	       .build();
	}
	
	public FileSettings() {}
	
	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param fileSettings [FileSettings]
	 */
	public FileSettings(FileSettings fileSettings)
	{
		this.fileinterval = fileSettings.fileinterval;
		this.filemastkey = fileSettings.filemastkey;
		this.filemode = fileSettings.filemode;
		this.instmaster = fileSettings.instmaster;
	}
}
