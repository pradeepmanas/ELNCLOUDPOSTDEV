package com.agaram.eln.primary.model.sheetManipulation;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.json.JSONException;
import org.json.JSONObject;


@Entity
@Table(name = "LSparsedparameters")
public class LSparsedparameters {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "parsedparametercode") 
	private Integer parsedparametercode;
	@Column(columnDefinition = "numeric(17,0)",name = "batchcode") 
	private Long batchcode;
	private Integer row;
	private Integer sheet;
	private Integer col;
	//@Lob
	@Column(columnDefinition = "text")
	private String value;
	//@Column(columnDefinition = "nvarchar(max)")
	private String parameter;
	//@Column(columnDefinition = "nvarchar(100)")
	private String fieldcode;
	//@Column(columnDefinition = "nvarchar(max)")
	private String resultvalue;
	private Long orderid;
	//@Column(columnDefinition = "datetime")
	private String analysedate;
	//@Column(columnDefinition = "nvarchar(200)")
	private String batchid;
	
	@Transient
	private String fieldkey;
	
	@Transient
	private String methodname;
	
	@Transient
	private String instrumentnames;
	
	@Transient
	private String instrumentid;
	
	@Transient
	private String fieldname;
	
	@Transient
	private String instrumentname;
	
	
	public LSparsedparameters()
	{
		
	}
	
	public LSparsedparameters(Integer parsedparametercode,Long batchcode,String fieldcode,String analysedate,String resultvalue,String value)
	{
		JSONObject objinst = null;
		try {
			objinst = new JSONObject(value);
		}catch (JSONException err){
		    
		}
		
		this.parsedparametercode =parsedparametercode;
		this.batchcode = batchcode;
		this.fieldcode = fieldcode;
		this.analysedate = analysedate;
		this.resultvalue = resultvalue;
		
		if(objinst != null)
		{
			this.fieldkey =	objinst.optString("fieldkey");
			this.methodname = objinst.optString("methodname");
			this.instrumentnames = objinst.optString("instrumentnames");
			this.instrumentid = objinst.optString("instrumentid");
			this.fieldname = objinst.optString("fieldname");
			this.instrumentname = objinst.optString("instrumentname");
			
		}
	}
	
	public Integer getParsedparametercode() {
		return parsedparametercode;
	}
	public void setParsedparametercode(Integer parsedparametercode) {
		this.parsedparametercode = parsedparametercode;
	}
	public Integer getRow() {
		return row;
	}
	public void setRow(Integer row) {
		this.row = row;
	}
	public Integer getSheet() {
		return sheet;
	}
	public void setSheet(Integer sheet) {
		this.sheet = sheet;
	}
	public Integer getCol() {
		return col;
	}
	public void setCol(Integer col) {
		this.col = col;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getFieldcode() {
		return fieldcode;
	}
	public void setFieldcode(String fieldcode) {
		this.fieldcode = fieldcode;
	}
	public String getResultvalue() {
		return resultvalue;
	}
	public void setResultvalue(String resultvalue) {
		this.resultvalue = resultvalue;
	}
	public Long getOrderid() {
		return orderid;
	}
	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}
	public String getAnalysedate() {
		return analysedate;
	}
	public void setAnalysedate(String analysedate) {
		this.analysedate = analysedate;
	}
	public String getBatchid() {
		return batchid;
	}
	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}
	public Long getBatchcode() {
		return batchcode;
	}
	public void setBatchcode(Long batchcode) {
		this.batchcode = batchcode;
	}
	public String getFieldkey() {
		return fieldkey;
	}
	public void setFieldkey(String fieldkey) {
		this.fieldkey = fieldkey;
	}

	public String getMethodname() {
		return methodname;
	}

	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}

	public String getInstrumentnames() {
		return instrumentnames;
	}

	public void setInstrumentnames(String instrumentnames) {
		this.instrumentnames = instrumentnames;
	}

	public String getInstrumentid() {
		return instrumentid;
	}

	public void setInstrumentid(String instrumentid) {
		this.instrumentid = instrumentid;
	}

	public String getFieldname() {
		return fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public String getInstrumentname() {
		return instrumentname;
	}

	public void setInstrumentname(String instrumentname) {
		this.instrumentname = instrumentname;
	}
	
}
