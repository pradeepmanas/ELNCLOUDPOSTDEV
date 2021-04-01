package com.agaram.eln.primary.model.instrumentDetails;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "LSresultvalues")
@Table(name = "ResultFieldValues")
public class LSresultvalues {

	@EmbeddedId
	private LSresultvaluesId id;	 
	//columnDefinition = "varchar(150)",
	@Column(columnDefinition = "varchar(150)",name = "FieldName") 
	private String fieldname;
	//columnDefinition = "text",
	@Column(columnDefinition = "text",name = "FieldValue") 
	private String fieldvalue;
	
	public LSresultvaluesId getId() {
		return id;
	}
	public void setId(LSresultvaluesId id) {
		this.id = id;
	}
	public String getFieldname() {
		return fieldname;
	}
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}
	public String getFieldvalue() {
		return fieldvalue;
	}
	public void setFieldvalue(String fieldvalue) {
		this.fieldvalue = fieldvalue;
	}
}
