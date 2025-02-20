package com.agaram.eln.primary.fetchmodel.inventory;

import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public class Materialget {
	private Integer matcode;
	private String matname;
	private String seqid;
	private LSuserMaster crby;
	private Integer sts;
	
	public Materialget(Integer nmaterialcode, String smaterialname, String sequenceid, LSuserMaster createby,Integer nstatus)
	{
		this.matcode = nmaterialcode;
		this.matname = smaterialname;
		this.seqid = sequenceid;
		this.crby = createby;
		this.sts = nstatus;
	}

	public Integer getMatcode() {
		return matcode;
	}

	public void setMatcode(Integer matcode) {
		this.matcode = matcode;
	}

	public String getMatname() {
		return matname;
	}

	public void setMatname(String matname) {
		this.matname = matname;
	}

	public String getSeqid() {
		return seqid;
	}

	public void setSeqid(String seqid) {
		this.seqid = seqid;
	}

	public LSuserMaster getCrby() {
		return crby;
	}

	public void setCrby(LSuserMaster crby) {
		this.crby = crby;
	}

	public Integer getSts() {
		return sts;
	}

	public void setSts(Integer sts) {
		this.sts = sts;
	}
	
	
}
