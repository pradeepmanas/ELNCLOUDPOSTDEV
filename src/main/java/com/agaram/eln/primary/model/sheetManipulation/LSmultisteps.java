package com.agaram.eln.primary.model.sheetManipulation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LSmultisteps")
public class LSmultisteps {
	@Id
	private Integer Stepcode;
	@Column(columnDefinition = "varchar(100)")
	private String StepName;
	@Column(columnDefinition = "text")
	private String StepDescription;
	private Integer MultiStepcode;
	public Integer getStepcode() {
		return Stepcode;
	}
	public void setStepcode(Integer stepcode) {
		Stepcode = stepcode;
	}
	public String getStepName() {
		return StepName;
	}
	public void setStepName(String stepName) {
		StepName = stepName;
	}
	public String getStepDescription() {
		return StepDescription;
	}
	public void setStepDescription(String stepDescription) {
		StepDescription = stepDescription;
	}
	public Integer getMultiStepcode() {
		return MultiStepcode;
	}
	public void setMultiStepcode(Integer multiStepcode) {
		MultiStepcode = multiStepcode;
	}
}
