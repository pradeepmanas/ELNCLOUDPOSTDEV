package com.agaram.eln.primary.service.fileuploaddownload;


import java.io.BufferedReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.util.Matrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.exception.FileNotFoundException;
import com.agaram.eln.primary.exception.FileStorageException;
import com.agaram.eln.primary.model.methodsetup.CloudParserFile;
import com.agaram.eln.primary.property.FileStorageProperties;
import com.agaram.eln.primary.repository.methodsetup.CloudParserFileRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.methodsetup.MethodService;
//import net.javaguides.springboot.fileuploaddownload.exception.FileStorageException;
//import net.javaguides.springboot.fileuploaddownload.exception.FileNotFoundException;
//import net.javaguides.springboot.fileuploaddownload.property.FileStorageProperties;
import com.mongodb.gridfs.GridFSDBFile;


@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    
    @Autowired
    private CloudFileManipulationservice cloudFileManipulationservice;
    
    @Autowired
    private CloudParserFileRepository cloudparserfilerepository;
    
    
	@Autowired
	GridFsOperations gridFsOps;

//	@Autowired
//	private MongoTemplate mongoTemplate;

	@Autowired
	private GridFsTemplate gridFsTemplate;
	
	@Autowired
    private MethodService methodservice;
	
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
    
   
    public String storeFile(MultipartFile file , String tenant , Integer isMultitenant,String originalfilename,Integer version) throws IOException {
      
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
      //  try {
            // Check if the file's name contains invalid characters
//            if(fileName.contains("..")) {
//                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
//            }
            String id = null;
    		System.out.print("in storefile function");

    		try {
    			id = cloudFileManipulationservice.storecloudfilesreturnUUID(file, "parserfile");
    			System.out.print("Blob ID"+id);
    			System.out.print("stored in blob");

    		} catch (IOException e) {
    			
    			e.printStackTrace();
    		}

    		CloudParserFile objfile = new CloudParserFile();
    		objfile.setFileid(id);
            objfile.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
    		objfile.setFilename(fileName);
    		objfile.setOriginalfilename(originalfilename);
    		objfile.setVersion(version);

    		cloudparserfilerepository.save(objfile);
          
    		
    		
          return fileName;
      //  } 
//        catch (IOException e) {
//            throw  FileStorageException("Could not store file " + fileName + ". Please try again!", e);
//        }
    }


public String storeimportFile(MultipartFile file , String tenant , Integer isMultitenant,String originalfilename,Integer version,Integer methodkey) throws IOException, InterruptedException {
        
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    	final String rawData;

            String id = null;
          if(isMultitenant == 1) {
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
    		objfile.setVersion(version);

    		cloudparserfilerepository.save(objfile);
          }else {
        	  storeSQLFile(file,tenant,isMultitenant,originalfilename) ;
          }
    		//return methodService.getFileData(fileName, tenant);
    		if(isMultitenant == 1) {
    		rawData =  methodservice.getFileData(fileName,tenant,methodkey);
    		}
    		else {
    		rawData =  methodservice.getSQLFileData(fileName,methodkey);
    		}
    		
          return rawData;
      
    }

    
//    private Exception FileStorageException(String string, IOException e) {
//		// TODO Auto-generated method stub
//		return null;
//	}


//	public String storeSQLFile(MultipartFile file , String tenant,Integer isMultitenant,String originalfilename) {
//        // Normalize file name
//    	
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//    	
//        try {
//            // Check if the file's name contains invalid characters
//            if(fileName.contains("..")) {
//                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
//            }
//
//            String fileid = fileName;
//			GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileid)));
//			if (largefile != null) {
//				gridFsTemplate.delete(new Query(Criteria.where("filename").is(fileid)));
//			}
//			//converting multipart file to string
////			ByteArrayInputStream stream = new   ByteArrayInputStream(file.getBytes());
//			//String myString = IOUtils.toString(stream, "UTF-8");
//			
//			gridFsTemplate.store(new ByteArrayInputStream(file.getBytes()), fileid);
//           
//		//	gridFsTemplate.store(new ByteArrayInputStream(myString.getBytes(StandardCharsets.UTF_8)), fileid);
//			
//			CloudParserFile objfile = new CloudParserFile();
//    		objfile.setFileid(fileid);
//   	        objfile.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
//    	 	objfile.setFilename(fileName);
//    	 	objfile.setOriginalfilename(originalfilename);
//    		cloudparserfilerepository.save(objfile);
//            
//             		
//            return fileName;
//        } catch (IOException ex) {
//            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
//        }
//}

    public String storeSQLFile(MultipartFile file , String tenant,Integer isMultitenant,String originalfilename) {
        // Normalize file name
    	
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    	final String ext = FilenameUtils.getExtension(fileName);
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            String fileid = fileName;
			GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileid)));
			if (largefile != null) {
				gridFsTemplate.delete(new Query(Criteria.where("filename").is(fileid)));
			}
//			if(ext.equalsIgnoreCase("txt")){
//            	File tempfile;
//            	tempfile = stream2file(file.getInputStream(),fileName, ext);
//
//		        String charset = "ISO-8859-1"; // or what corresponds
//		        BufferedReader in = new BufferedReader( 
//		            new InputStreamReader (new FileInputStream(tempfile), charset));
//		        String line;
//		       // String result;
//		     
//		        StringBuffer sb = new StringBuffer();
//		        while( (line = in.readLine()) != null) { 
//		        
//		        	sb.append(line).append("\n");
//		        }
//		        String appendedline = sb.toString();
//		        
//		        line="";
//			//  gridFsTemplate.store(new ByteArrayInputStream(appendedline.getBytes(StandardCharsets.UTF_8)),fileid);
//			  gridFsTemplate.store(new ByteArrayInputStream(appendedline.getBytes(StandardCharsets.UTF_8)), fileid, file.getContentType());
//            }
			  gridFsTemplate.store(new ByteArrayInputStream(file.getBytes()), fileid, file.getContentType());
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
    public static File stream2file (InputStream in,String filename,String ext) throws IOException {
        final File tempFile = File.createTempFile(filename, ext);
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
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
