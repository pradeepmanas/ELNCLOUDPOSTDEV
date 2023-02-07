package com.agaram.eln.primary.controller.fileuploaddownload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.methodsetup.MethodVersion;
import com.agaram.eln.primary.payload.Response;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodVersionRepository;
import com.agaram.eln.primary.service.fileuploaddownload.FileStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;



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

    @Autowired
    private MethodRepository methodrepo; 
    
    @Autowired
    private MethodVersionRepository methodversionrepo; 
    
//    @Autowired
//    private MethodService methodService; 
    
//    @Autowired
//    private MethodVersion methodversion; 
//    
    public FileStorageService getFileStorageService() {
		return fileStorageService;
	}

	public void setFileStorageService(FileStorageService fileStorageService)throws Exception {
		this.fileStorageService = fileStorageService;
	}

	/**
	 * This method invokes FileStorageService to store file in specified location and then
	 * returns its file name
	 * @param file [MultipartFile]
	 * @return uploaded file name
	 * @throws IOException 
	 */
	@PostMapping("/uploadFile")
    public Response uploadFile(@RequestParam("file") MultipartFile file ,@RequestParam("tenant") String tenant,
    		@RequestParam("isMultitenant") Integer isMultitenant,@RequestParam("originalfilename") String originalfilename,@RequestParam("version") Integer version) throws IOException{
		
		
        String fileName = fileStorageService.storeFile(file,tenant, isMultitenant,originalfilename,version);
		
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new Response(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }
	
//	importFileversion
	
//	@SuppressWarnings("unchecked")
	@PostMapping("/uploadimportFile")
    public String uploadimportFile(@RequestParam("file") MultipartFile file ,@RequestParam("tenant") String tenant,
    		@RequestParam("isMultitenant") Integer isMultitenant,@RequestParam("originalfilename") String originalfilename,
    		@RequestParam("version") Integer version
    		,@RequestParam("methodkey") Integer methodkey,
    		@RequestParam("filename") String filename,@RequestParam("instrawdataurl") String instrawdataurl
    		) throws IOException{
		
		
//		List<Method> methodfile = methodrepo.findByFilenameAndMethodkey(filename,methodkey);
	
	//	List<MethodVersion> methodfile = methodversionrepo.findByFilenameAndMethodkey(filename,methodkey);
		//if(methodfile.isEmpty())
	//	{
		final String rawData = fileStorageService.storeimportFile(file,tenant, isMultitenant,originalfilename,version);
		
		List<Method> method = new ArrayList<Method>();
		Method newobj = new Method();
		MethodVersion obj = new MethodVersion();
		
		ArrayList<MethodVersion> methodversion = new ArrayList<MethodVersion>();
	    methodversion =methodversionrepo.findByMethodkey(methodkey);
		
		obj.setFilename(filename);
		obj.setMethodkey(methodkey);
		obj.setInstrawdataurl(instrawdataurl);
		obj.setVersion(version);
		methodversionrepo.save(obj);	

		method=methodrepo.findByMethodkey(methodkey);
		

		List <MethodVersion> metverobj = new ArrayList<MethodVersion>();
		metverobj.add(obj);
		metverobj.addAll(methodversion);
				
		newobj.setCreatedby(method.get(0).getCreatedby());
		newobj.setCreateddate(method.get(0).getCreateddate());
		newobj.setInstmaster(method.get(0).getInstmaster());
		newobj.setMethodkey(method.get(0).getMethodkey());
        newobj.setMethodname(method.get(0).getMethodname());
		newobj.setInstrawdataurl(instrawdataurl);
		newobj.setFilename(filename);
		newobj.setSite(method.get(0).getSite());
		newobj.setParser(method.get(0).getParser());
		newobj.setSamplesplit(method.get(0).getSamplesplit());
		newobj.setStatus(method.get(0).getStatus());
		newobj.setVersion(version);
		newobj.setMethodversion(metverobj);
		
		methodrepo.save(newobj);	
		
		return rawData;
//		}else {
//		return null;
//		}
		// return new Response(file.getContentType(), instrawdataurl, instrawdataurl, file.getSize());
    }
	
	@PostMapping("/sqluploadFile")
    public Response sqluploadFile(@RequestParam("file") MultipartFile file ,@RequestParam("tenant") String tenant,
    		@RequestParam("isMultitenant") Integer isMultitenant,@RequestParam("originalfilename") String originalfilename){
		
		
        String fileName = fileStorageService.storeSQLFile(file,tenant, isMultitenant,originalfilename);
		
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new Response(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }
	
//    @PostMapping("/uploadMultipleFiles")
//    public List<Response> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files)throws Exception {
//        return Arrays.asList(files)
//                .stream()
//                .map(file -> uploadFile(file))
//                .collect(Collectors.toList());
//    }
}
