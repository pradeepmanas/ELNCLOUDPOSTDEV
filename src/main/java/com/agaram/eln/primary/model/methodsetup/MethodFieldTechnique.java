package com.agaram.eln.primary.model.methodsetup;

import java.io.Serializable;
import java.util.List;

import com.agaram.eln.primary.model.methodsetup.ParserField;
import com.agaram.eln.primary.model.methodsetup.ParserTechnique;
import com.agaram.eln.primary.model.methodsetup.SubParserField;
import com.agaram.eln.primary.model.methodsetup.SubParserTechnique;

/**
 * This class is used to hold the fields that are to be used while evaluating parser.
 * @version 1.0.0
 * @since   04- Feb- 2020
 */
public class MethodFieldTechnique implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fieldname;
	private String fieldtype;
	private String fieldid;
	private int fieldkey;
	private ParserField parserfield;
	private List<ParserTechnique> parsertechniques;
	private List<SubParserTechnique> subparsertechniques;
	private List<SubParserField> subparserfields;
	private List<String> parseddata;
	
	
	public String getFieldname() {
		return fieldname;
	}
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}
	public String getFieldtype() {
		return fieldtype;
	}
	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
	}
	public String getFieldid() {
		return fieldid;
	}
	public void setFieldid(String fieldid) {
		this.fieldid = fieldid;
	}
	public int getFieldkey() {
		return fieldkey;
	}
	public void setFieldkey(int fieldkey) {
		this.fieldkey = fieldkey;
	}	
	
	public ParserField getParserfield() {
		return parserfield;
	}
	public void setParserfield(ParserField parserfield) {
		this.parserfield = parserfield;
	}
	public List<ParserTechnique> getParsertechniques() {
		return parsertechniques;
	}
	public void setParsertechniques(List<ParserTechnique> parsertechniques) {
		this.parsertechniques = parsertechniques;
	}
	public List<SubParserTechnique> getSubparsertechniques() {
		return subparsertechniques;
	}
	public void setSubparsertechniques(List<SubParserTechnique> subparsertechniques) {
		this.subparsertechniques = subparsertechniques;
	}
	public List<SubParserField> getSubparserfields() {
		return subparserfields;
	}
	public void setSubparserfields(List<SubParserField> subparserfields) {
		this.subparserfields = subparserfields;
	}
	public List<String> getParseddata() {
		return parseddata;
	}
	public void setParseddata(List<String> parseddata) {
		this.parseddata = parseddata;
	}
	
	public MethodFieldTechnique(MethodFieldTechnique techn) {
		super();
		this.fieldname = techn.fieldname;
		this.fieldtype = techn.fieldtype;
		this.fieldid = techn.fieldid;
		this.fieldkey = techn.fieldkey;
		this.parserfield = techn.parserfield;
		this.parsertechniques = techn.parsertechniques;
		this.subparsertechniques = techn.subparsertechniques;
		this.subparserfields = techn.subparserfields;
		this.parseddata = techn.parseddata;
	}
	
	public MethodFieldTechnique() {
	}
	
}
