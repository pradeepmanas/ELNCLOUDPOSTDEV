package com.agaram.eln.primary.model.sheetManipulation;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.inventory.LSequipmentmap;
import com.agaram.eln.primary.model.inventory.LSinstrument;
import com.agaram.eln.primary.model.inventory.LSmaterial;
import com.agaram.eln.primary.model.inventory.LSmaterialmap;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
@Entity
@Table(name = "LStestmasterlocal")
public class LStestmasterlocal {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "testcode") 
	private Integer testcode;
	private Integer status;
	@Column(columnDefinition = "varchar(100)")
	private String testname;
	private String description;

	@Column(columnDefinition = "varchar(20)")
	private String teststatus;
	
	@OneToMany
	@JoinColumn(name="testcode")
	private List<LSmaterialmap> lsmaterialmap;
	@Transient
	private List<LSmaterial> LSmaterial;
	
	@OneToMany
	@JoinColumn(name="testcode")
	private List<LSequipmentmap> LSequipmentmap;
	@Transient
	private List<LSinstrument> LSinstrument;
	
	@ManyToOne
	private LSuserMaster createby;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	private String taskcategory;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
	
	@Transient
	private Response response;
	
	@Transient
	LScfttransaction objsilentaudit;
	
	@Transient
	LScfttransaction objmanualaudit;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifieddate;
	
	@Column(name = "modifiedbyuser")
	private String modifiedbyuser;
	
	
	public String getModifiedbyuser() {
		return modifiedbyuser;
	}
	public void setModifiedbyuser(String modifiedbyuser) {
		this.modifiedbyuser = modifiedbyuser;
	}
	public Date getModifieddate() {
		return modifieddate;
	}
	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}

	
	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}
	@Transient
	LoggedUser objuser;
	
	@Transient
	LSuserMaster LSuserMaster;
	
	@ManyToOne
	private LSuserMaster modifiedby;
	
	@ManyToOne
	private LSSiteMaster lssitemaster;

	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}

	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}
	
	public String getTeststatus() {
		if(teststatus != null)
		{
		return  teststatus.trim().equals("A")?"Active":"Retired";
		}
		else
		{
			return "";
		}
	}

	public void setTeststatus(String teststatus) {
		this.teststatus = teststatus;
	}
	
	public LoggedUser getObjuser() {
		return objuser;
	}

	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
	}

	public LSuserMaster getLSuserMaster() {
		return LSuserMaster;
	}

	public void setLSuserMaster(LSuserMaster lSuserMaster) {
		LSuserMaster = lSuserMaster;
	}

	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	public Integer getTestcode() {
		return testcode;
	}
	public void setTestcode(Integer testcode) {
		this.testcode = testcode;
	}
	
	public List<LSmaterialmap> getLsmaterialmap() {
		return lsmaterialmap;
	}
	public void setLsmaterialmap(List<LSmaterialmap> lsmaterialmap) {
		this.lsmaterialmap = lsmaterialmap;
	}
	
	public List<LSmaterial> getLSmaterial() {
		return LSmaterial;
	}
	public void setLSmaterial(List<LSmaterial> lSmaterial) {
		LSmaterial = lSmaterial;
	}
	public List<LSequipmentmap> getLSequipmentmap() {
		return LSequipmentmap;
	}
	public void setLSequipmentmap(List<LSequipmentmap> lSequipmentmap) {
		LSequipmentmap = lSequipmentmap;
	}
	public List<LSinstrument> getLSinstrument() {
		return LSinstrument;
	}
	public void setLSinstrument(List<LSinstrument> lSinstrument) {
		LSinstrument = lSinstrument;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getTestname() {
		return testname;
	}
	public void setTestname(String testname) {
		this.testname = testname;
	}
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}

	public LSuserMaster getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(LSuserMaster modifiedby) {
		this.modifiedby = modifiedby;
	}

	public LSuserMaster getCreateby() {
		return createby;
	}

	public void setCreateby(LSuserMaster createby) {
		this.createby = createby;
	}

	public String getTaskcategory() {
		return taskcategory;
	}

	public void setTaskcategory(String taskcategory) {
		this.taskcategory = taskcategory;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	
	
}
