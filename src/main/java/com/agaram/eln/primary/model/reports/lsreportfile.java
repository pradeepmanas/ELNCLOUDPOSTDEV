package com.agaram.eln.primary.model.reports;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

@Entity
@Table(name = "lsreportfile")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class lsreportfile {
	
	@Id
	private long id;
	
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	private String content;
	
	@Column(columnDefinition = "numeric(17,0)", name = "batchcode")
	private Long batchcode;
	
	public Long getBatchcode() {
		return batchcode;
	}

	public void setBatchcode(Long batchcode) {
		this.batchcode = batchcode;
	}

	private Integer contentstored;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getContentstored() {
		return contentstored;
	}

	public void setContentstored(Integer contentstored) {
		this.contentstored = contentstored;
	}
	
	
	
	
	
}
