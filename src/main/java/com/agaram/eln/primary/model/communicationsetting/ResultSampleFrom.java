package com.agaram.eln.primary.model.communicationsetting;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="resultsamplefrom")
public class ResultSampleFrom implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="resultsamplekey" , nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer resultsamplekey;
	
	private String resultsamplename;
	
	private int status=1;

	public Integer getResultsamplekey() {
		return resultsamplekey;
	}

	public void setResultsamplekey(Integer resultsamplekey) {
		this.resultsamplekey = resultsamplekey;
	}

	public String getResultsamplename() {
		return resultsamplename;
	}

	public void setResultsamplename(String resultsamplename) {
		this.resultsamplename = resultsamplename;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	

}
