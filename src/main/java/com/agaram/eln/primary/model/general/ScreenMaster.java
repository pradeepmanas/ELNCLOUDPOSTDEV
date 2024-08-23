package com.agaram.eln.primary.model.general;
 
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ScreenMaster")
public class ScreenMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer screencode;
	private String screenname;
	public Integer getScreencode() {
		return screencode;
	}
	public void setScreencode(Integer screencode) {
		this.screencode = screencode;
	}
	public String getScreenname() {
		return screenname;
	}
	public void setScreenname(String screenname) {
		this.screenname = screenname;
	}
	
	
}
