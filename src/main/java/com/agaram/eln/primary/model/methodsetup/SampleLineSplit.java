package com.agaram.eln.primary.model.methodsetup;

import java.io.Serializable;
import java.util.Date;

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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Range;

import com.agaram.eln.primary.model.methodsetup.InstantDateAdapter;
import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

/**
 * This class is used to map the fields of 'samplelinesplit' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   13- Feb- 2020
 */
@XmlRootElement  (name = "samplelinesplit")
@XmlType(propOrder = { "samplelinesplitkey", "method","removeorextractlines", "centertext",
		"excludecentertext", "centertextrowindex", "centertextoccurrenceno", "toplines", "bottomlines", "repeatlines",
		"extractblock", "status", "createdby", "createddate", "type"
		})
@Entity
@Table(name = "samplelinesplit")
public class SampleLineSplit implements Serializable, Diffable<SampleLineSplit>{
	
	private static final long serialVersionUID = 1L;

	@Id 
	@Column(name = "samplelinesplitkey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer samplelinesplitkey;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "methodkey", nullable = false)
	private Method method;
	
	@Column(name = "removeorextractlines")
	private int removeorextractlines = 0;
	
	@Column(name = "centertext")
	private String centertext;
	
	@Column(name = "excludecentertext")
	private int excludecentertext = 0;
	
	@Column(name = "centertextrowindex")
	private int centertextrowindex;
	
	@Column(name = "centertextoccurrenceno")
	private int centertextoccurrenceno;
	
	@Column(name = "toplines")
	private int toplines;

	@Column(name = "bottomlines")
	private int bottomlines;
	
	@Column(name = "repeatlines")
	private int repeatlines = 0;
	
	@Column(name = "extractblock")
	private String extractblock;
	
	private transient String type = "sampleLineSplit";
	
	@Range(min=-1, max=1)
	@Column(name = "status")
	private int status=1;	

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usercode", nullable = false)
	private LSuserMaster createdby;
	
	@Column(name = "createddate")
	private Date createddate;	
	
	@XmlAttribute	
	public Integer getSamplelinesplitkey() {
		return samplelinesplitkey;
	}

	public void setSamplelinesplitkey(Integer samplelinesplitkey) {
		this.samplelinesplitkey = samplelinesplitkey;
	}

	@XmlElement	
	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	@XmlElement	
	public int getRemoveorextractlines() {
		return removeorextractlines;
	}

	public void setRemoveorextractlines(int removeorextractlines) {
		this.removeorextractlines = removeorextractlines;
	}

	@XmlElement	
	public String getCentertext() {
		return centertext;
	}

	public void setCentertext(String centertext) {
		this.centertext = centertext;
	}

	@XmlElement	
	public int getExcludecentertext() {
		return excludecentertext;
	}

	public void setExcludecentertext(int excludecentertext) {
		this.excludecentertext = excludecentertext;
	}
	
	@XmlElement	
	public int getCentertextrowindex() {
		return centertextrowindex;
	}

	public void setCentertextrowindex(int centertextrowindex) {
		this.centertextrowindex = centertextrowindex;
	}
	
	@XmlElement	
	public int getCentertextoccurrenceno() {
		return centertextoccurrenceno;
	}

	public void setCentertextoccurrenceno(int centertextoccurrenceno) {
		this.centertextoccurrenceno = centertextoccurrenceno;
	}

	@XmlElement	
	public int getToplines() {
		return toplines;
	}

	public void setToplines(int toplines) {
		this.toplines = toplines;
	}

	@XmlElement	
	public int getBottomlines() {
		return bottomlines;
	}

	public void setBottomlines(int bottomlines) {
		this.bottomlines = bottomlines;
	}

	@XmlElement	
	public int getRepeatlines() {
		return repeatlines;
	}

	public void setRepeatlines(int repeatlines) {
		this.repeatlines = repeatlines;
	}	
	
	@XmlElement	
	public String getExtractblock() {
		return extractblock;
	}

	public void setExtractblock(String extractblock) {
		this.extractblock = extractblock;
	}
	
	@XmlElement	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@XmlElement	
	public LSuserMaster getCreatedby() {
		return createdby;
	}

	public void setCreatedby(LSuserMaster createdby) {
		this.createdby = createdby;
	}

	@XmlElement	
	@XmlJavaTypeAdapter(InstantDateAdapter.class)
	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * To find difference between two entity objects by implementing Diffable interface  
	 */
	@Override
	public DiffResult diff(SampleLineSplit obj) {
		
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("method", this.method.getMethodname(), obj.method.getMethodname()) 
	       .append("removeorextractlines", this.removeorextractlines, obj.removeorextractlines)	     
	       .append("centertext", this.centertext, obj.centertext)
	       .append("excludecentertext", this.excludecentertext, obj.excludecentertext)
	       .append("centertextrowindex",  this.centertextrowindex, obj.centertextrowindex)
	       .append("centertextoccurrenceno",this.centertextoccurrenceno, obj.centertextoccurrenceno)
	       .append("toplines", this.toplines, obj.toplines)
	       .append("bottomlines", this.bottomlines, obj.bottomlines)
	       .append("repeatlines", this.repeatlines, obj.repeatlines)
	       .append("extractblock", this.extractblock, obj.extractblock)
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getUsername(), obj.createdby.getUsername())
	       .append("createddate", this.createddate, obj.createddate)
	       .build();
	}
	
	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param sampleLineSplit [SampleLineSplit]
	 */
	public SampleLineSplit(SampleLineSplit sampleLineSplit)
	{
		this.bottomlines = sampleLineSplit.bottomlines;
		this.centertext = sampleLineSplit.centertext;
		this.createdby = sampleLineSplit.createdby;
		this.createddate = sampleLineSplit.createddate;
		this.excludecentertext = sampleLineSplit.excludecentertext;
		this.centertextrowindex = sampleLineSplit.centertextrowindex;
		this.centertextoccurrenceno = sampleLineSplit.centertextoccurrenceno;
		this.method = sampleLineSplit.method;
		this.removeorextractlines =  sampleLineSplit.removeorextractlines;
		this.repeatlines = sampleLineSplit.repeatlines;
		this.samplelinesplitkey = sampleLineSplit.samplelinesplitkey;
		this.status = sampleLineSplit.status;
		this.toplines = sampleLineSplit.toplines;
		this.extractblock = sampleLineSplit.extractblock;
	}	
	
	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public SampleLineSplit() {}
}
