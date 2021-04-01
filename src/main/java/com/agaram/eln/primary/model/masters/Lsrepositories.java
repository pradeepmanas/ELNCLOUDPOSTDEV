package com.agaram.eln.primary.model.masters;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.agaram.eln.primary.model.general.Response;

@Entity
@Table(name = "Lsrepositories")
public class Lsrepositories {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "repositorycode")
	private Integer repositorycode;
	
	private String repositoryname;
	
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	private String repositoryfields;
	
	private String addedby;
	
	private Date addedon;
	
	private Integer usercode;
	
	private Integer sitecode;
	
	private Integer fieldcount=-1;
	
	@Transient
	Response objResponse;

	public Integer getRepositorycode() {
		return repositorycode;
	}

	public void setRepositorycode(Integer repositorycode) {
		this.repositorycode = repositorycode;
	}
	
	public String getRepositoryname() {
		return repositoryname;
	}

	public void setRepositoryname(String repositoryname) {
		this.repositoryname = repositoryname;
	}

	public String getRepositoryfields() {
		return repositoryfields;
	}

	public void setRepositoryfields(String repositoryfields) {
		this.repositoryfields = repositoryfields;
	}

	public String getAddedby() {
		return addedby;
	}

	public void setAddedby(String addedby) {
		this.addedby = addedby;
	}

	public Date getAddedon() {
		return addedon;
	}

	public void setAddedon(Date addedon) {
		this.addedon = addedon;
	}

	public Integer getUsercode() {
		return usercode;
	}

	public void setUsercode(Integer usercode) {
		this.usercode = usercode;
	}

	public Integer getSitecode() {
		return sitecode;
	}

	public void setSitecode(Integer sitecode) {
		this.sitecode = sitecode;
	}

	public Response getObjResponse() {
		return objResponse;
	}

	public void setObjResponse(Response objResponse) {
		this.objResponse = objResponse;
	}

	public Integer getFieldcount() {
		return fieldcount;
	}

	public void setFieldcount(Integer fieldcount) {
		this.fieldcount = fieldcount;
	}
	
	
}
