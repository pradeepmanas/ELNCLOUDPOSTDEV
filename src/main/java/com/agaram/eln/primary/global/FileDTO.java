package com.agaram.eln.primary.global;

import java.util.Base64;

public class FileDTO {

	private String fileName;
    private String fileContentBase64;
    private String moljson;

    public FileDTO(String fileName, String moljson, byte[] fileContent) {
        this.fileName = fileName;
        this.moljson = moljson;
        this.fileContentBase64 = Base64.getEncoder().encodeToString(fileContent);
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileContentBase64() {
		return fileContentBase64;
	}

	public void setFileContentBase64(String fileContentBase64) {
		this.fileContentBase64 = fileContentBase64;
	}

	public String getMoljson() {
		return moljson;
	}

	public void setMoljson(String moljson) {
		this.moljson = moljson;
	}
}