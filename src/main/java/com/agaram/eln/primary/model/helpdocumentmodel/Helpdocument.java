package com.agaram.eln.primary.model.helpdocumentmodel;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

@Entity
@Table(name = "Helpdocument")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})

public class Helpdocument {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	private Integer id;

	@Type(type = "jsonb")
	 @Column(columnDefinition = "jsonb")
		public String lshelpdocumentcontent;
	 
	@Column(columnDefinition = "varchar(255)")
	 public String documentname;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLshelpdocumentcontent() {
		return lshelpdocumentcontent;
	}
	public void setLshelpdocumentcontent(String lshelpdocumentcontent) {
		this.lshelpdocumentcontent = lshelpdocumentcontent;
	}
	public String getDocumentname() {
		return documentname;
	}
	public void setDocumentname(String documentname) {
		this.documentname = documentname;
	}

	 

}
