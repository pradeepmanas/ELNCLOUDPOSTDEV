package com.agaram.eln.primary.controller.usermanagement;

public class MolExportRequest {
	private String structure;
    private String parameters;
    private String inputFormat;
	
	public String getStructure() {
		return structure;
	}
	public void setStructure(String structure) {
		this.structure = structure;
	}
	public String getinputFormat() {
		return inputFormat;
	}
	public void setinputFormat(String inputFormat) {
		this.inputFormat = inputFormat;
	}
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

}
