package com.agaram.eln.primary.model.getorders;

public class Sheet {
	
	private Integer filecode;
	private String extension;
	private String filenameuser;
	private String filenameuuid;
	
	public Sheet(Integer filecode,String extension,String filenameuser, String filenameuuid)
	{
		this.filecode = filecode;
		this.extension = extension;
		this.filenameuser = filenameuser;
		this.filenameuuid = filenameuuid;
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
	
	
	

}
