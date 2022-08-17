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
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

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

	public Integer createdby;
	public String createdbyusername;

	@ManyToOne
	private LSuserMaster modifiedby;

	private Integer sitecode;

	private Integer onlytome;

	private Integer tothesite;

	public Integer getCreatedby() {
		return createdby;
	}

	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}

	public String getCreatedbyusername() {
		return createdbyusername;
	}

	public void setCreatedbyusername(String createdbyusername) {
		this.createdbyusername = createdbyusername;
	}

	public LSuserMaster getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(LSuserMaster modifiedby) {
		this.modifiedby = modifiedby;
	}

	public Integer getSitecode() {
		return sitecode;
	}

	public void setSitecode(Integer sitecode) {
		this.sitecode = sitecode;
	}

	public Integer getOnlytome() {
		return onlytome;
	}

	public void setOnlytome(Integer onlytome) {
		this.onlytome = onlytome;
	}

	public Integer getTothesite() {
		return tothesite;
	}

	public void setTothesite(Integer tothesite) {
		this.tothesite = tothesite;
	}

	// @OneToMany
//	@JoinColumn(name="directorycode")
	@Transient
	private List<Logilaborders> lsorderitems;

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

}
