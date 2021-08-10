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
import com.agaram.eln.primary.model.methodsetup.SampleLineSplit;
import com.agaram.eln.primary.model.methodsetup.SampleTextSplit;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

/**
 * This class is used to map the fields of 'sampleextract' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   13- Feb- 2020
 */
@XmlRootElement  (name = "sampleextract")
@XmlType(propOrder = { "sampleextractkey", "method", 
		"extracttextorlines", "matchtext",	"absolutelines", "sampletextsplit", "samplelinesplit", "type",
		"status", "createdby", "createddate"})
@Entity
@Table(name = "sampleextract")
public class SampleExtract implements Serializable, Diffable<SampleExtract>{
		
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "sampleextractkey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sampleextractkey;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "methodkey", nullable = false)
	private Method method;
	
	@Column(name = "extracttextorlines")
	private int extracttextorlines;
	
	@Column(name = "matchtext")
	private String matchtext;

	
	@Column(name = "absolutelines")
	private Integer absolutelines;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "sampletextsplitkey", nullable = true)
	private SampleTextSplit sampletextsplit;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "samplelinesplitkey", nullable = true)
	private SampleLineSplit samplelinesplit;
	
	private transient String type = "sampleExtract";
	
	@Range(min=-1, max=1)
	@Column(name = "status")
	private int status=1;	

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usercode", nullable = false)
	private LSuserMaster createdby;
	
	@Column(name = "createddate")
	private Date createddate;

	@XmlAttribute	
	public Integer getSampleextractkey() {
		return sampleextractkey;
	}

	public void setSampleextractkey(Integer sampleextractkey) {
		this.sampleextractkey = sampleextractkey;
	}

	@XmlElement	
	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	@XmlElement	
	public int getExtracttextorlines() {
		return extracttextorlines;
	}

	public void setExtracttextorlines(int extracttextorlines) {
		this.extracttextorlines = extracttextorlines;
	}

	@XmlElement(nillable=true)	
	public String getMatchtext() {
		return matchtext;
	}

	public void setMatchtext(String matchtext) {
		this.matchtext = matchtext;
	}

	@XmlElement(nillable=true)	
	public Integer getAbsolutelines() {
		return absolutelines;
	}

	public void setAbsolutelines(Integer absolutelines) {
		this.absolutelines = absolutelines;
	}	
	
	@XmlElement(nillable=true)	
	public SampleTextSplit getSampletextsplit() {
		return sampletextsplit;
	}

	public void setSampletextsplit(SampleTextSplit sampletextsplit) {
		this.sampletextsplit = sampletextsplit;
	}

	@XmlElement(nillable=true)	
	public SampleLineSplit getSamplelinesplit() {
		return samplelinesplit;
	}

	public void setSamplelinesplit(SampleLineSplit samplelinesplit) {
		this.samplelinesplit = samplelinesplit;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	
	/**
	 * To find difference between two entity objects by implementing Diffable interface  
	 */
	@Override
	public DiffResult diff(SampleExtract obj) {
		
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("method", this.method.getMethodname(), obj.method.getMethodname()) 
	       .append("extracttextorlines", this.extracttextorlines, obj.extracttextorlines)	     
	       .append("matchtext", this.matchtext, obj.matchtext)
	       .append("absolutelines", this.absolutelines, obj.absolutelines)
	       .append("sampletextsplit", getSampleTextSplitBlock(this.sampletextsplit), getSampleTextSplitBlock(obj.sampletextsplit))
	       .append("samplelinesplit", getSampleLineSplitBlock(this.samplelinesplit), getSampleLineSplitBlock(obj.samplelinesplit))
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getUsername(), obj.createdby.getUsername())
	       .append("createddate", this.createddate, obj.createddate)
	       .build();
	}
	
	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param sampleExtract [SampleExtract]
	 */
	public SampleExtract(SampleExtract sampleExtract)
	{
		this.sampleextractkey = sampleExtract.sampleextractkey;
		this.absolutelines = sampleExtract.absolutelines;
		this.createdby = sampleExtract.createdby;
		this.createddate = sampleExtract.createddate;
		this.extracttextorlines = sampleExtract.extracttextorlines;
		this.matchtext = sampleExtract.matchtext;
		this.method = sampleExtract.method;
		this.status = sampleExtract.status;		
		this.samplelinesplit = sampleExtract.samplelinesplit;
		this.sampletextsplit = sampleExtract.sampletextsplit;
	}
	
	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public SampleExtract() {}
	
	private Object getSampleTextSplitBlock(SampleTextSplit sampleTextSplit)
	{
		if (sampleTextSplit == null)
		{
			return null;
		}
		else {
			return sampleTextSplit.getExtractblock();
		}		
	}
	
	private Object getSampleLineSplitBlock(SampleLineSplit sampleLineSplit)
	{
		if (sampleLineSplit == null)
		{
			return null;
		}
		else {
			return sampleLineSplit.getExtractblock();
		}		
	}
}
