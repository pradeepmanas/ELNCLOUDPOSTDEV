package com.agaram.eln.primary.service.fileuploaddownload;


import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FilenameUtils;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.exception.FileNotFoundException;
import com.agaram.eln.primary.exception.FileStorageException;
import com.agaram.eln.primary.model.methodsetup.CloudParserFile;
import com.agaram.eln.primary.property.FileStorageProperties;
import com.agaram.eln.primary.repository.methodsetup.CloudParserFileRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
//import net.javaguides.springboot.fileuploaddownload.exception.FileStorageException;
//import net.javaguides.springboot.fileuploaddownload.exception.FileNotFoundException;
//import net.javaguides.springboot.fileuploaddownload.property.FileStorageProperties;
import com.mongodb.gridfs.GridFSDBFile;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;


@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    
    @Autowired
    private CloudFileManipulationservice cloudFileManipulationservice;
    
    @Autowired
    private CloudParserFileRepository cloudparserfilerepository;
    
    
	@Autowired
	GridFsOperations gridFsOps;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private GridFsTemplate gridFsTemplate;
	

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
    
   
    public String storeFile(MultipartFile file , String tenant , Integer isMultitenant,String originalfilename) throws IOException {
      
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
      //  try {
            // Check if the file's name contains invalid characters
//            if(fileName.contains("..")) {
//                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
//            }
            String id = null;
          
    		try {
    			id = cloudFileManipulationservice.storecloudfilesreturnUUID(file, "parserfile");
    		} catch (IOException e) {
    			
    			e.printStackTrace();
    		}

    		CloudParserFile objfile = new CloudParserFile();
    		objfile.setFileid(id);
            objfile.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
    		objfile.setFilename(fileName);
    		objfile.setOriginalfilename(originalfilename);
    		cloudparserfilerepository.save(objfile);
          
    		
    		
          return fileName;
      //  } 
//        catch (IOException e) {
//            throw  FileStorageException("Could not store file " + fileName + ". Please try again!", e);
//        }
    }

    
    private Exception FileStorageException(String string, IOException e) {
		// TODO Auto-generated method stub
		return null;
	}


	public String storeSQLFile(MultipartFile file , String tenant,Integer isMultitenant,String originalfilename) {
        // Normalize file name
    	
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    	
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
//            Path targetLocation = this.fileStorageLocation.resolve(fileName);
//            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
         //   String id = null;
                    
            String fileid = fileName;
			GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileid)));
			if (largefile != null) {
				gridFsTemplate.delete(new Query(Criteria.where("filename").is(fileid)));
			}
			gridFsTemplate.store(new ByteArrayInputStream(file.getBytes()), fileid);
           
			CloudParserFile objfile = new CloudParserFile();
    		objfile.setFileid(fileid);
   	        objfile.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
    	 	objfile.setFilename(fileName);
    	 	objfile.setOriginalfilename(originalfilename);
    		cloudparserfilerepository.save(objfile);
            
             		
            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
}
    
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }
}
