package com.agaram.eln.primary.model.instrumentDetails;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "LSresultvalues")
@Table(name = "ResultFieldValues_Arch")
public class LSresultvaluesArch {

	@EmbeddedId
	private LSresultvaluesIdArch id;
	 
	
	@Column(columnDefinition = "varchar(50)",name = "FieldName") 
	private String fieldname;
	@Column(columnDefinition = "varchar(250)",name = "FieldValue") 
	private String fieldvalue;
	
	
	
	public LSresultvaluesIdArch getId() {
		return id;
	}
	public void setId(LSresultvaluesIdArch id) {
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
