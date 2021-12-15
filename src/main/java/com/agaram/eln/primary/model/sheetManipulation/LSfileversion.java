package com.agaram.eln.primary.model.sheetManipulation;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
@Entity
@Table(name = "LSfileversion")
public class LSfileversion {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "fileversioncode")
	private Integer fileversioncode;
	
	//@ManyToOne 
	private Integer filecode;
	
	@Column(columnDefinition = "varchar(10)")
	private String extension;
	@Column(columnDefinition = "varchar(100)")
	private String filenameuser;
	@Column(columnDefinition = "varchar(100)")
	private String filenameuuid;
	//@Lob
	private String filecontent;
	private Integer isactive;
	private Integer versionno;
	@Column(columnDefinition = "varchar(255)")
	private String versionname;
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifieddate;
	@ManyToOne 
	private LSSiteMaster lssitemaster;
	
	@ManyToOne 
	private LSuserMaster createby;
	
	@ManyToOne 
	private LSuserMaster modifiedby;
	
	@ManyToOne 
	private LSsheetworkflow lssheetworkflow;
	
	private Integer approved;
	private Integer rejected;
	public Integer getFileversioncode() {
		return fileversioncode;
	}
	public void setFileversioncode(Integer fileversioncode) {
		this.fileversioncode = fileversioncode;
	}
	
	
	
	public Integer getFilecode() {
		return filecode;
	}
	public void setFilecode(Integer filecode) {
		this.filecode = filecode;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getFilenameuser() {
		return filenameuser;
	}
	public void setFilenameuser(String filenameuser) {
		this.filenameuser = filenameuser;
	}
	public String getFilenameuuid() {
		return filenameuuid;
	}
	public void setFilenameuuid(String filenameuuid) {
		this.filenameuuid = filenameuuid;
	}
	public String getFilecontent() {
		return filecontent;
	}
	public void setFilecontent(String filecontent) {
		this.filecontent = filecontent;
	}
	public Integer getIsactive() {
		return isactive;
	}
	public void setIsactive(Integer isactive) {
		this.isactive = isactive;
	}
	public Integer getVersionno() {
		return versionno;
	}
	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public Date getModifieddate() {
		return modifieddate;
	}
	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}
	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}
	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}
	public LSuserMaster getCreateby() {
		return createby;
	}
	public void setCreateby(LSuserMaster createby) {
		this.createby = createby;
	}
	public LSuserMaster getModifiedby() {
		return modifiedby;
	}
	public void setModifiedby(LSuserMaster modifiedby) {
		this.modifiedby = modifiedby;
	}
	public LSsheetworkflow getLssheetworkflow() {
		return lssheetworkflow;
	}
	public void setLssheetworkflow(LSsheetworkflow lssheetworkflow) {
		this.lssheetworkflow = lssheetworkflow;
	}
	public Integer getApproved() {
		return approved;
	}
	public void setApproved(Integer approved) {
		this.approved = approved;
	}
	public Integer getRejected() {
		return rejected;
	}
	public void setRejected(Integer rejected) {
		this.rejected = rejected;
	}
	public String getVersionname() {
		return ""+this.getVersionno();
	}
	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}
	
//	@Override
//    public String toString()
//    {
//        return "{modifiedby : { usercode : "
//            + this.modifiedby.getUsercode()
//            + ", username : "
//            + this.modifiedby.getUsername()
//            + "}, versionname : "
//            + this.versionname + "}";
//    }
}
