package com.agaram.eln.primary.controller.fileuploaddownload;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.agaram.eln.primary.payload.Response;
import com.agaram.eln.primary.service.fileuploaddownload.FileStorageService;



/**
 * This class holds method to upload file  in "uploads" directory
 * of the application
 * @author ATE153
 * @version 1.0.0
 * @since   05- Dec- 2019
 *
 */
@RestController
public class FileUploadController {

    @Autowired
    private FileStorageService fileStorageService;  

    public FileStorageService getFileStorageService() {
		return fileStorageService;
	}

	public void setFileStorageService(FileStorageService fileStorageService) {
		this.fileStorageService = fileStorageService;
	}

	/**
	 * This method invokes FileStorageService to store file in specified location and then
	 * returns its file name
	 * @param file [MultipartFile]
	 * @return uploaded file name
	 */
	@PostMapping("/uploadFile")
    public Response uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new Response(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

	
    @PostMapping("/uploadMultipleFiles")
    public List<Response> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }
}
