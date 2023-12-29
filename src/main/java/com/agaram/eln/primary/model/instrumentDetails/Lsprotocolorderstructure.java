package com.agaram.eln.primary.model.instrumentDetails;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.fetchmodel.getorders.Logilabprotocolorders;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.protocols.Elnprotocolworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;

@Entity
@Table(name = "Lsprotocolorderstructure")
public class Lsprotocolorderstructure {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(columnDefinition = "numeric(17,0)", name = "directorycode")
	private Long directorycode;
	private String path;
	@Transient
	private boolean expanded;
	@Transient
	private boolean selected;
	private Integer size;
	private Date dateCreated;
	private Date dateModified;
	private String icon;
	private String directoryname;

	@Transient
	private boolean edit;
	private Integer length;
	private Long parentdircode;
	
	
	private Long floatvalues;

	public Long getFloatvalues() {
		return floatvalues;
	}

	public void setFloatvalues(Long floatvalues) {
		this.floatvalues = floatvalues;
	}

	@ManyToOne
	public LSuserMaster createdby;

	@ManyToOne
	private LSuserMaster modifiedby;

	@ManyToOne
	private LSSiteMaster sitemaster;
	
	@Transient
	LoggedUser objuser;
	
	@Transient
	private Integer protocoltype;
	
	@Transient
	Integer searchCriteriaType;
	
	@Transient
	private String orderflag;
	
	@Transient
	private String teamname;
	
	public String getTeamname() {
		return teamname;
	}

	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

	@Transient
	private Integer rejected;
	
	@Transient
	List<LSworkflow> lstworkflow;
	
	@Transient
	List<Elnprotocolworkflow> lstelnprotocolworkflow;

	@Transient
	private LSuserMaster lsuserMaster;
	
	@Transient
	private Integer[] lstuserMaster;
	


	public Integer[] getLstuserMaster() {
		return lstuserMaster;
	}

	public void setLstuserMaster(Integer[] lstuserMaster) {
		this.lstuserMaster = lstuserMaster;
	}

	public Integer getRejected() {
		return rejected;
	}

	public void setRejected(Integer rejected) {
		this.rejected = rejected;
	}

	public String getOrderflag() {
		return orderflag;
	}

	public void setOrderflag(String orderflag) {
		this.orderflag = orderflag;
	}

	public Integer getSearchCriteriaType() {
		return searchCriteriaType;
	}

	public void setSearchCriteriaType(Integer searchCriteriaType) {
		this.searchCriteriaType = searchCriteriaType;
	}

	public LoggedUser getObjuser() {
		return objuser;
	}

	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
	}

	public Integer getProtocoltype() {
		return protocoltype;
	}

	public void setProtocoltype(Integer protocoltype) {
		this.protocoltype = protocoltype;
	}

	private Integer viewoption;

	private Integer teamcode;

	public Integer getTeamcode() {
		return teamcode;
	}

	public void setTeamcode(Integer teamcode) {
		this.teamcode = teamcode;
	}

	public LSuserMaster getCreatedby() {
		return createdby;
	}

	public void setCreatedby(LSuserMaster createdby) {
		this.createdby = createdby;
	}

	public LSSiteMaster getSitemaster() {
		return sitemaster;
	}

	public void setSitemaster(LSSiteMaster sitemaster) {
		this.sitemaster = sitemaster;
	}

	public Integer getViewoption() {
		return viewoption;
	}

	public void setViewoption(Integer viewoption) {
		this.viewoption = viewoption;
	}

	public LSuserMaster getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(LSuserMaster modifiedby) {
		this.modifiedby = modifiedby;
	}

	@Transient
	private Long dircodetomove;

	@Transient
	private List<Logilabprotocolorders> logilabprotocolorders;

	@Transient
	private Response response;

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public Long getDirectorycode() {
		return directorycode;
	}

	public void setDirectorycode(Long directorycode) {
		this.directorycode = directorycode;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDirectoryname() {
		return directoryname;
	}

	public void setDirectoryname(String directoryname) {
		this.directoryname = directoryname;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Long getParentdircode() {
		return parentdircode;
	}

	public void setParentdircode(Long parentdircode) {
		this.parentdircode = parentdircode;
	}

	public Long getDircodetomove() {
		return dircodetomove;
	}

	public void setDircodetomove(Long dircodetomove) {
		this.dircodetomove = dircodetomove;
	}

	public List<Logilabprotocolorders> getLogilabprotocolorders() {
		return logilabprotocolorders;
	}

	public void setLogilabprotocolorders(List<Logilabprotocolorders> logilabprotocolorders) {
		this.logilabprotocolorders = logilabprotocolorders;
	}
	public List<LSworkflow> getLstworkflow() {
		return lstworkflow;
	}

	public void setLstworkflow(List<LSworkflow> lstworkflow) {
		this.lstworkflow = lstworkflow;
	}

	public List<Elnprotocolworkflow> getLstelnprotocolworkflow() {
		return lstelnprotocolworkflow;
	}

	public void setLstelnprotocolworkflow(List<Elnprotocolworkflow> lstelnprotocolworkflow) {
		this.lstelnprotocolworkflow = lstelnprotocolworkflow;
	}

	public LSuserMaster getLsuserMaster() {
		return lsuserMaster;
	}

	public void setLsuserMaster(LSuserMaster lsuserMaster) {
		this.lsuserMaster = lsuserMaster;
	}
}
