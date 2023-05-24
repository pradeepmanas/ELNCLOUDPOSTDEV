package com.agaram.eln.primary.model.webParser;

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
import javax.persistence.Transient;

import com.agaram.eln.primary.model.methodsetup.MethodDelimiter;
import com.agaram.eln.primary.model.methodsetup.ParserBlock;



@Entity
@Table(name = "lswebparserfield")
public class Lswebparserfield implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer parserfieldcode;
	
	@Column(name = "parserfieldkey", nullable = false)
	private Integer parserfieldkey;
	
	@Column(name = "parserfieldname")
	private String parserfieldname;	
	
	@Column(name = "fieldid")
	private String fieldid;	
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "methodkey", nullable = false)
	private Lswebparsermethod method;
	
	@Column(name="datatype")
	private String datatype;
	
	@Transient
	private ParserBlock parserblock;
	
	@Transient
	private MethodDelimiter methoddelimiter;

	@Transient
	private int status=1;	

	@Transient
	private Lswebparseruser createdby;
	
	@Transient
	private Lswebparsersite site;
	
	@Transient
	private Date createddate;
	
	@Transient
	private String subparserfieldname;
	
	@Transient
	private Integer subparserfieldkey;
	
	@Transient
	private Lswebparserfield parserfield;
	
	public Integer getSubparserfieldkey() {
		return subparserfieldkey;
	}

	public void setSubparserfieldkey(Integer subparserfieldkey) {
		this.subparserfieldkey = subparserfieldkey;
	}

	public Lswebparserfield getParserfield() {
		return parserfield;
	}

	public void setParserfield(Lswebparserfield parserfield) {
		this.parserfield = parserfield;
	}

	public Lswebparsersite getSite() {
		return site;
	}

	public void setSite(Lswebparsersite site) {
		this.site = site;
	}

	public String getSubparserfieldname() {
		return subparserfieldname;
	}

	public void setSubparserfieldname(String subparserfieldname) {
		this.subparserfieldname = subparserfieldname;
	}

	public Integer getParserfieldkey() {
		
		if(this.parserfield != null) {
			return parserfieldkey = this.parserfield.getParserfieldkey();
		}
		
		return parserfieldkey;
	}

	public void setParserfieldkey(Integer parserfieldkey) {
		this.parserfieldkey = parserfieldkey;
	}

	public String getParserfieldname() {
		
		if(this.getSubparserfieldname() != null) {
			return parserfieldname = this.getSubparserfieldname();
		}
		
		return parserfieldname;
	}

	public void setParserfieldname(String parserfieldname) {
		this.parserfieldname = parserfieldname;
		
		if(this.getSubparserfieldname() != null) {
			this.parserfieldname = this.getSubparserfieldname();
		}
		
	}

	public String getFieldid() {
		return fieldid;
	}

	public void setFieldid(String fieldid) {
		this.fieldid = fieldid;
	}

	public Lswebparsermethod getMethod() {
		return method;
	}

	public void setMethod(Lswebparsermethod method) {
		
		if(this.parserblock != null && this.parserblock.getMethod()!= null) {
			this.method = new Lswebparsermethod(this.parserblock.getMethod().getMethodkey()) ;
		}else {
			this.method = method;
		}
		
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ParserBlock getParserblock() {
		return parserblock;
	}

	public void setParserblock(ParserBlock parserblock) {
		this.parserblock = parserblock;
	}

	public MethodDelimiter getMethoddelimiter() {
		return methoddelimiter;
	}

	public void setMethoddelimiter(MethodDelimiter methoddelimiter) {
		this.methoddelimiter = methoddelimiter;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Lswebparseruser getCreatedby() {
		return createdby;
	}

	public void setCreatedby(Lswebparseruser createdby) {
		this.createdby = createdby;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public Integer getParserfieldcode() {
		return parserfieldcode;
	}

	public void setParserfieldcode(Integer parserfieldcode) {
		this.parserfieldcode = parserfieldcode;
	}
	
	
}
