package com.agaram.eln.primary.model.usermanagement;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LSusergroupedcolumns")
public class LSusergroupedcolumns {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "usergroupedcolcode")
	private Integer usergroupedcolcode;
	
	@Column(name = "usercode")
	private Integer usercode;
	
	@Column(name = "sitecode")
	private Integer sitecode;
	
	@Column(columnDefinition = "varchar(100)")
	private String gridname;
	
	@Column(columnDefinition = "TEXT")
	private String gridcolumns;

	public Integer getUsergroupedcolcode() {
		return usergroupedcolcode;
	}

	public void setUsergroupedcolcode(Integer usergroupedcolcode) {
		this.usergroupedcolcode = usergroupedcolcode;
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

	public String getGridname() {
		return gridname;
	}

	public void setGridname(String gridname) {
		this.gridname = gridname;
	}

	public String getGridcolumns() {
		return gridcolumns;
	}

	public void setGridcolumns(String gridcolumns) {
		this.gridcolumns = gridcolumns;
	}
	
	
}
