package com.agaram.eln.primary.model.material;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.bson.types.Binary;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="elnmaterialchemdiagref")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElnmaterialChemDiagRef {

	@Id
	@Column(name = "diagramcode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long diagramcode;
	
	@Column(name = "smiles")
	private String smiles;
	
	@Column(name = "moljson")
	private String moljson;
	
	@Column(columnDefinition = "varchar(250)", name = "fileid")
	private String fileid;
	
	private Integer nmaterialcode;

	public Integer getNmaterialcode() {
		return nmaterialcode;
	}

	public String getMoljson() {
		return moljson;
	}

	public void setMoljson(String moljson) {
		this.moljson = moljson;
	}

	public void setNmaterialcode(Integer nmaterialcode) {
		this.nmaterialcode = nmaterialcode;
	}

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
	
	@ManyToOne 
	private LSuserMaster createby;
	
	@Transient
	private Binary file;
	
	@Transient
	private Response response;
	
	@Transient
	LScfttransaction objsilentaudit;
	
	public String getSmiles() {
		return smiles;
	}

	public void setSmiles(String smiles) {
		this.smiles = smiles;
	}

	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	public Long getDiagramcode() {
		return diagramcode;
	}

	public void setDiagramcode(Long diagramcode) {
		this.diagramcode = diagramcode;
	}

	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public LSuserMaster getCreateby() {
		return createby;
	}

	public void setCreateby(LSuserMaster createby) {
		this.createby = createby;
	}

	public Binary getFile() {
		return file;
	}

	public void setFile(Binary file) {
		this.file = file;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}
}
