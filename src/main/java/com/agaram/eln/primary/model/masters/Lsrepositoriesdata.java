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
@Table(name = "Lsrepositoriesdata")
public class Lsrepositoriesdata {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "repositorydatacode")
	private Integer repositorydatacode;
	
	@Column(name = "repositorycode")
	private Integer repositorycode;
	
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	private String repositorydatafields;
	
	private String repositoryitemname;
	
	private String addedby;
	
	private Date addedon;
	
	private Integer usercode;
	
	private Integer sitecode;
	
	private Integer itemstatus =1;
	
	private String repositoryuniqueid;
	
	@Transient
	Response objResponse;
	
	@Transient
	private Integer expireddatecount;
	
	@Transient
	private Date fromdate;
	
	@Transient
	private Date todate;
	
	public Integer getRepositorydatacode() {
		return repositorydatacode;
	}

	public Integer getExpireddatecount() {
		return expireddatecount;
	}

	public void setExpireddatecount(Integer expireddatecount) {
		this.expireddatecount = expireddatecount;
	}

	public void setRepositorydatacode(Integer repositorydatacode) {
		this.repositorydatacode = repositorydatacode;
	}

	public Integer getRepositorycode() {
		return repositorycode;
	}

	public void setRepositorycode(Integer repositorycode) {
		this.repositorycode = repositorycode;
	}

	public String getRepositorydatafields() {
		return repositorydatafields;
	}

	public void setRepositorydatafields(String repositorydatafields) {
		this.repositorydatafields = repositorydatafields;
	}

	public String getRepositoryitemname() {
		return repositoryitemname;
	}

	public void setRepositoryitemname(String repositoryitemname) {
		this.repositoryitemname = repositoryitemname;
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

	public Response getObjResponse() {
		return objResponse;
	}

	public void setObjResponse(Response objResponse) {
		this.objResponse = objResponse;
	}

	public Integer getSitecode() {
		return sitecode;
	}

	public void setSitecode(Integer sitecode) {
		this.sitecode = sitecode;
	}

	public Integer getItemstatus() {
		return itemstatus;
	}

	public void setItemstatus(Integer itemstatus) {
		this.itemstatus = itemstatus;
	}

	public String getRepositoryuniqueid() {
		return repositoryuniqueid;
	}

	public void setRepositoryuniqueid(String repositoryuniqueid) {
		this.repositoryuniqueid = repositoryuniqueid;
	}

	public Date getFromdate() {
		return fromdate;
	}

	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}

	public Date getTodate() {
		return todate;
	}

	public void setTodate(Date todate) {
		this.todate = todate;
	}
}
