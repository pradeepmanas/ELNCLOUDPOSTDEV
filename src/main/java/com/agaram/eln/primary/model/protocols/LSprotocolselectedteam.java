package com.agaram.eln.primary.model.protocols;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;

@Entity
@Table(name = "LSprotocolselectedteam")
public class LSprotocolselectedteam {
private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "selectionid", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer selectionid;

	@ManyToOne
	private LSusersteam userteam;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name="teamcode")
	private List<LSuserteammapping> lsuserteammapping;

	@Column(columnDefinition = "numeric(17,0)", name = "protocolordercode")
	private Long protocolordercode;
	
	@ManyToOne
	private LSSiteMaster sitemaster;

	@ManyToOne
	private Elnmaterial elnmaterial;
	
	private Long directorycode;
	
	@Transient
	private List<Long> lstdirectorycode;
	
	@Column(name = "CreatedTimeStamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdtimestamp;
	
	public List<Long> getLstdirectorycode() {
		return lstdirectorycode;
	}

	public void setLstdirectorycode(List<Long> lstdirectorycode) {
		this.lstdirectorycode = lstdirectorycode;
	}

	public Long getDirectorycode() {
		return directorycode;
	}

	public void setDirectorycode(Long directorycode) {
		this.directorycode = directorycode;
	}

	public Integer getSelectionid() {
		return selectionid;
	}

	public void setSelectionid(Integer selectionid) {
		this.selectionid = selectionid;
	}

	public LSusersteam getUserteam() {
		return userteam;
	}

	public void setUserteam(LSusersteam userteam) {
		this.userteam = userteam;
	}

	public List<LSuserteammapping> getLsuserteammapping() {
		return lsuserteammapping;
	}

	public void setLsuserteammapping(List<LSuserteammapping> lsuserteammapping) {
		this.lsuserteammapping = lsuserteammapping;
	}

	public LSSiteMaster getSitemaster() {
		return sitemaster;
	}

	public void setSitemaster(LSSiteMaster sitemaster) {
		this.sitemaster = sitemaster;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Elnmaterial getElnmaterial() {
		return elnmaterial;
	}

	public void setElnmaterial(Elnmaterial elnmaterial) {
		this.elnmaterial = elnmaterial;
	}

	public Date getCreatedtimestamp() {
		return createdtimestamp;
	}

	public void setCreatedtimestamp(Date createdtimestamp) {
		this.createdtimestamp = createdtimestamp;
	}

	public Long getProtocolordercode() {
		return protocolordercode;
	}

	public void setProtocolordercode(Long protocolordercode) {
		this.protocolordercode = protocolordercode;
	}

}
