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

import com.agaram.eln.primary.fetchmodel.getorders.Logilaborders;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;

@Entity
@Table(name = "LSSheetOrderStructure")
public class LSSheetOrderStructure {
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

	@Transient
	private Long dircodetomove;

	
	@Transient
	private Integer filetype;
	
	@ManyToOne
	public LSuserMaster createdby;

	@ManyToOne
	private LSuserMaster modifiedby;

	@ManyToOne
	private LSSiteMaster sitemaster;

	private Integer viewoption;
	
	@Transient
	private List<Logilaborders> lsorderitems;
	
	@Transient
	LoggedUser objuser;
	
	@Transient
	private LSuserMaster lsuserMaster;
	
	@Transient
	private String teamname;
	
	@Transient
	private List<LSuserMaster> lstuserMaster;
	
	public List<LSuserMaster> getLstuserMaster() {
		return lstuserMaster;
	}

	public void setLstuserMaster(List<LSuserMaster> lstuserMaster) {
		this.lstuserMaster = lstuserMaster;
	}
	
	public String getTeamname() {
		return teamname;
	}

	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

	private Integer teamcode;

	public LSuserMaster getLsuserMaster() {
		return lsuserMaster;
	}

	public void setLsuserMaster(LSuserMaster lsuserMaster) {
		this.lsuserMaster = lsuserMaster;
	}

	public Integer getTeamcode() {
		return teamcode;
	}

	public void setTeamcode(Integer teamcode) {
		this.teamcode = teamcode;
	}

	public LSuserMaster getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(LSuserMaster modifiedby) {
		this.modifiedby = modifiedby;
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

//	public List<LSlogilablimsorderdetail> getLsorderitems() {
//		return lsorderitems;
//	}
//
//	public void setLsorderitems(List<LSlogilablimsorderdetail> lsorderitems) {
//		this.lsorderitems = lsorderitems;
//	}

	public Long getParentdircode() {
		return parentdircode;
	}

	public List<Logilaborders> getLsorderitems() {
		return lsorderitems;
	}

	public void setLsorderitems(List<Logilaborders> lsorderitems) {
		this.lsorderitems = lsorderitems;
	}

	public String getDirectoryname() {
		return directoryname;
	}

	public void setDirectoryname(String directoryname) {
		this.directoryname = directoryname;
	}

	public void setParentdircode(Long parentdircode) {
		this.parentdircode = parentdircode;
	}

	public Long getDircodetomove() {
		return dircodetomove;
	}

	public Integer getFiletype() {
		return filetype;
	}

	public void setFiletype(Integer filetype) {
		this.filetype = filetype;
	}

	public void setDircodetomove(Long dircodetomove) {
		this.dircodetomove = dircodetomove;
	}

	@Transient
	private Response response;

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
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

	public LoggedUser getObjuser() {
		return objuser;
	}

	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
	}

}
