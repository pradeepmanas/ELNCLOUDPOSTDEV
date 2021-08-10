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
 * This class is used to map the fields of 'sampletextsplit' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   13- Feb- 2020
 */
@XmlRootElement  (name = "sampletextsplit")
@XmlType(propOrder = { "sampletextsplitkey", "method", "removeorextracttext", 
		"begintext", "excludebegintext", "begintextrowindex", "begintextoccurrenceno",
		"endtext", "excludeendtext", "endtextrowindex", "endtextoccurrenceno", "repeattext", 
		 "extractblock","status", "createdby", "createddate", "type"//, "sampleextracts"
		 })
@Entity
@Table(name = "sampletextsplit")
public class SampleTextSplit implements Serializable, Diffable<SampleTextSplit>{
	
	private static final long serialVersionUID = 1L;

	@Id 
	@Column(name = "sampletextsplitkey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sampletextsplitkey;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "methodkey", nullable = false)
	private Method method;
	
	@Column(name = "removeorextracttext")
	private int removeorextracttext;
	
	@Column(name = "begintext")
	private String begintext;
	
	@Column(name = "excludebegintext")
	private int excludebegintext;
	
	@Column(name = "begintextrowindex")
	private int begintextrowindex;
	
	@Column(name = "begintextoccurrenceno")
	private int begintextoccurrenceno;
	
	@Column(name = "endtext")
	private String endtext;
	
	@Column(name = "excludeendtext")
	private int excludeendtext;
	
	@Column(name = "endtextrowindex")
	private int endtextrowindex;
	
	@Column(name = "endtextoccurrenceno")
	private int endtextoccurrenceno;
	
	@Column(name = "repeattext")
	private int repeattext;
	
	@Column(name = "extractblock")
	private String extractblock;
	
	private transient String type = "sampleTextSplit";
	
	@Range(min=-1, max=1)
	@Column(name = "status")
	private int status=1;	

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usercode", nullable = false)
	private LSuserMaster createdby;
	
	@Column(name = "createddate")
	private Date createddate;
	
	@XmlAttribute	
	public Integer getSampletextsplitkey() {
		return sampletextsplitkey;
	}

	public void setSampletextsplitkey(Integer sampletextsplitkey) {
		this.sampletextsplitkey = sampletextsplitkey;
	}

	@XmlElement	
	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	@XmlElement	
	public int getRemoveorextracttext() {
		return removeorextracttext;
	}

	public void setRemoveorextracttext(int removeorextracttext) {
		this.removeorextracttext = removeorextracttext;
	}

	@XmlElement	
	public String getBegintext() {
		return begintext;
	}

	public void setBegintext(String begintext) {
		this.begintext = begintext;
	}

	@XmlElement	
	public int getExcludebegintext() {
		return excludebegintext;
	}

	public void setExcludebegintext(int excludebegintext) {
		this.excludebegintext = excludebegintext;
	}	
	
	@XmlElement	
	public int getBegintextrowindex() {
		return begintextrowindex;
	}

	public void setBegintextrowindex(int begintextrowindex) {
		this.begintextrowindex = begintextrowindex;
	}	
		
	@XmlElement	
	public int getBegintextoccurrenceno() {
		return begintextoccurrenceno;
	}

	public void setBegintextoccurrenceno(int begintextoccurrenceno) {
		this.begintextoccurrenceno = begintextoccurrenceno;
	}

	@XmlElement	
	public String getEndtext() {
		return endtext;
	}

	public void setEndtext(String endtext) {
		this.endtext = endtext;
	}

	@XmlElement	
	public int getExcludeendtext() {
		return excludeendtext;
	}

	public void setExcludeendtext(int excludeendtext) {
		this.excludeendtext = excludeendtext;
	}
	
	@XmlElement	
	public int getEndtextrowindex() {
		return endtextrowindex;
	}

	public void setEndtextrowindex(int endtextrowindex) {
		this.endtextrowindex = endtextrowindex;
	}
	
	@XmlElement	
	public int getEndtextoccurrenceno() {
		return endtextoccurrenceno;
	}

	public void setEndtextoccurrenceno(int endtextoccurrenceno) {
		this.endtextoccurrenceno = endtextoccurrenceno;
	}

	@XmlElement	
	public int getRepeattext() {
		return repeattext;
	}

	public void setRepeattext(int repeattext) {
		this.repeattext = repeattext;
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
	public DiffResult diff(SampleTextSplit obj) {
		
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("method", this.method.getMethodname(), obj.method.getMethodname()) 
	       .append("removeorextracttext", this.removeorextracttext, obj.removeorextracttext)	     
	       .append("begintext", this.begintext, obj.begintext)	      
	       .append("excludebegintext", this.excludebegintext, obj.excludebegintext)
	       .append("begintextrowindex", this.begintextrowindex, obj.begintextrowindex)
	       .append("endtext", this.endtext, obj.endtext)
	       .append("excludeendtext", this.excludeendtext, obj.excludeendtext)
	       .append("endtextrowindex", this.endtextrowindex, obj.endtextrowindex)
	       .append("begintextoccurrenceno", this.begintextoccurrenceno, obj.begintextoccurrenceno)
	       .append("endtextoccurrenceno", this.endtextoccurrenceno, obj.endtextoccurrenceno) 
	       .append("repeattext", this.repeattext, obj.repeattext)
	       .append("extractblock", this.extractblock, obj.extractblock)
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getUsername(), obj.createdby.getUsername())
	       .append("createddate", this.createddate, obj.createddate)
	       .build();
	}
	
	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param sampleTextSplit [SampleTextSplit]
	 */
	public SampleTextSplit(SampleTextSplit sampleTextSplit)
	{
		this.createdby = sampleTextSplit.createdby;
		this.createddate = sampleTextSplit.createddate;
		this.method = sampleTextSplit.method;
		this.removeorextracttext =  sampleTextSplit.removeorextracttext;
		this.status = sampleTextSplit.status;
		this.begintext =sampleTextSplit.begintext;
		this.endtext = sampleTextSplit.endtext;
		this.excludebegintext = sampleTextSplit.excludebegintext;
		this.excludeendtext = sampleTextSplit.excludeendtext;
		this.begintextrowindex = sampleTextSplit.begintextrowindex;	
		this.endtextrowindex = sampleTextSplit.endtextrowindex;
		this.begintextoccurrenceno = sampleTextSplit.begintextoccurrenceno;
		this.endtextoccurrenceno = sampleTextSplit.endtextoccurrenceno;
		this.repeattext = sampleTextSplit.repeattext;
		this.sampletextsplitkey = sampleTextSplit.sampletextsplitkey;
		this.status = sampleTextSplit.status;		
		this.extractblock = sampleTextSplit.extractblock;
	}
	
	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public SampleTextSplit() {}
	
}
