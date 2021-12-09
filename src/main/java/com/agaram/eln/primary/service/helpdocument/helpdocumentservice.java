package com.agaram.eln.primary.service.helpdocument;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.helpdocument.Helpdocument;
import com.agaram.eln.primary.model.helpdocument.Helptittle;
import com.agaram.eln.primary.model.protocols.LSprotocolorderimages;
import com.agaram.eln.primary.repository.helpdocument.HelpdocumentRepository;
import com.agaram.eln.primary.repository.helpdocument.HelptittleRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.fileManipulation.FileManipulationservice;
import com.agaram.eln.primary.service.instrumentDetails.InstrumentService;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;




@Service
@EnableJpaRepositories(basePackageClasses = HelpdocumentRepository.class)
public class helpdocumentservice {
	@Autowired
	HelpdocumentRepository HelpdocumentRepository;
	
	@Autowired
	HelptittleRepository helptittleRepository;
	
	@Autowired
    private InstrumentService instrumentService;
	
	@Autowired
	private FileManipulationservice fileManipulationservice;
	
	@Autowired
	private CloudFileManipulationservice cloudFileManipulationservice;
	
	@Autowired
    private Environment env;
	
	public Map<String, Object> adddocument(Map<String, Object> obj) {
		Helpdocument Helpdocument = new Helpdocument();
		Helpdocument.setLshelpdocumentcontent(new JSONObject(obj).toString());
		Helpdocument.setDocumentname((String) obj.get("documentname"));
		Map<String, Object> object = new HashMap<String, Object>();
		List<Helpdocument> objMap=HelpdocumentRepository.findAll();
		if(objMap.size() ==0) {
			Helpdocument =HelpdocumentRepository.save(Helpdocument);
		}else if(objMap.size() ==1) {
			int count =HelpdocumentRepository.setlshelpdocumentcontentanddocumentnameByid(Helpdocument.getLshelpdocumentcontent(),Helpdocument.getDocumentname(),objMap.get(0).getId());
			object.put("success",count);
		}
	
		object.put("success",Helpdocument);
		return object;
		
	}
	
	public Map<String, Object> getdocumentcontent() {
		Map<String, Object> object = new HashMap<String, Object>();
		try {
		List<Helpdocument> objMap=HelpdocumentRepository.findAll();
		
		object.put("Helpdocument", objMap);
		}
		catch (Exception e) {
			object.put("Helpdocument", "notset");
		}
		return object;
	}
	
	public Helptittle savenode(Helptittle objhelp)
	{
		Response objresponse = new Response();
		
		List<Helptittle> lstnode = new ArrayList<Helptittle>();
		if(objhelp.getNodecode() != null)
		{
			lstnode = helptittleRepository.findByTextAndParentcodeAndNodecodeNot(objhelp.getText(),objhelp.getParentcode() ,objhelp.getNodecode());
		}
		else
		{
			lstnode = helptittleRepository.findByTextAndParentcode(objhelp.getText(), objhelp.getParentcode() );
		}
			
		if(lstnode != null && lstnode.size()>0)
		{
			objresponse.setStatus(false);
			objresponse.setInformation("Node already exists");
			objhelp.setObjResponse(objresponse);
			return objhelp;
		}
		
		objhelp = helptittleRepository.save(objhelp);
		
		objresponse.setStatus(true);
		objhelp.setObjResponse(objresponse);
		
		return objhelp;
	}

	public List<Helptittle> gethelpnodes( Helptittle objhelp)
	{
		return helptittleRepository.findByOrderByNodeindexAsc();
	}
	
	public Helpdocument getdocumentonid(Helpdocument objhelp)
	{
		Helpdocument objupdatehelp = HelpdocumentRepository.findFirst1ByNodecodeOrderByNodecodeDesc(objhelp.getNodecode());
		return objupdatehelp != null ?objupdatehelp:objhelp;
	}
	
	public Helpdocument savedocument(Helpdocument objhelp)
	{
		Helpdocument objupdatehelp = HelpdocumentRepository.findFirst1ByNodecodeOrderByNodecodeDesc(objhelp.getNodecode());
		if(objupdatehelp != null)
		{
			objhelp.setId(objupdatehelp.getId());
//			if(objupdatehelp.getFiletype()==1) {
				objhelp.setFiletype(0);
//			}
//			else {
//				objhelp.setFiletype(1);
//			}
			
		}
		
		return HelpdocumentRepository.save(objhelp);
	}
	
	public List<Helptittle>  sortNodesforhelp(List<Helptittle>  objhelp)
	{
		objhelp = helptittleRepository.save(objhelp);
		return objhelp;
	}
	
	public List<Helptittle> deletenode(List<Helptittle> objhelp)
	{
		List<Integer> lstnodecode = objhelp.stream().map(Helptittle::getNodecode).collect(Collectors.toList());
		HelpdocumentRepository.deleteByNodecode(lstnodecode);
		helptittleRepository.deleteByNodecode(lstnodecode);
		return objhelp;
		
	}
	
	public Helptittle deletechildnode(Helptittle objhelp)
	{
		Response objresponse = new Response();
		if(objhelp.getNodecode() != null) {
			HelpdocumentRepository.deleteByNodecode(objhelp.getNodecode());
			helptittleRepository.deleteByNodecode(objhelp.getNodecode());
			objresponse.setInformation("Success");
			objresponse.setStatus(true);
			objhelp.setObjResponse(objresponse);
		}
		else {
			objresponse.setInformation("failed");
			objresponse.setStatus(false);
			objhelp.setObjResponse(objresponse);
		}
		return objhelp;
		
	}
	
	public Helpdocument storevideoforhelp(Integer Nodecode, MultipartFile file) throws IOException { 
		Helpdocument objupdatehelp = HelpdocumentRepository.findFirst1ByNodecodeOrderByNodecodeDesc(Nodecode);
		
		if(objupdatehelp == null)
		{
			objupdatehelp = new Helpdocument();
			objupdatehelp.setNodecode(Nodecode);
		}
		
        UUID objGUID = UUID.randomUUID();
        String randomUUIDString = objGUID.toString();
        
        String bloburi="";
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		
        
		try {    
			
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant().toLowerCase());
			
			if(objupdatehelp.getId() != null && objupdatehelp.getDocumentname() != null)
			{
				CloudBlockBlob oldblob = container.getBlockBlobReference(objupdatehelp.getDocumentname());
				oldblob.deleteIfExists();
			}

			// Create the container if it does not exist with public access.
			System.out.println("Creating container: " + container.getName());
			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());		    

			File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+randomUUIDString);
			file.transferTo(convFile);

			//Getting a blob reference
			CloudBlockBlob blob = container.getBlockBlobReference(convFile.getName());

			//Creating blob and uploading file to it
			System.out.println("Uploading the sample file ");
			blob.uploadFromFile(convFile.getAbsolutePath());
			
			bloburi = blob.getUri().toString();

		} 
		catch (StorageException ex)
		{
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
		}
		catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
		finally 
		{
			System.out.println("The program has completed successfully.");
			System.out.println("Press the 'Enter' key while in the console to delete the sample files, example container, and exit the application.");

		
		}
        
		objupdatehelp.setFiletype(1);
        objupdatehelp.setDocumentname(randomUUIDString);
        objupdatehelp.setFileref(bloburi);
        //objupdatehelp.setLshelpdocumentcontent(randomUUIDString);
        
        HelpdocumentRepository.save(objupdatehelp);
       
        return objupdatehelp;
    }
	
	public ResponseEntity<InputStreamResource> helpdownload(String fileid) throws IOException
	{
			
			HttpHeaders header = new HttpHeaders();
		    header.set("Content-Disposition", "attachment; filename=gg.mp4");

			GridFSDBFile gridFsFile = null;
			
			try {
				gridFsFile = instrumentService.retrieveLargeFile(fileid);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(gridFsFile.getContentType());
		    header.setContentType(MediaType.parseMediaType(gridFsFile.getContentType()));
		    header.setContentLength(gridFsFile.getLength());
		    return new ResponseEntity<>(new InputStreamResource(gridFsFile.getInputStream()), header, HttpStatus.OK);
	}
	
	public Helpdocument storevideoforhelponprim(Integer Nodecode, MultipartFile file) throws IOException { 
		Helpdocument objupdatehelp = HelpdocumentRepository.findFirst1ByNodecodeOrderByNodecodeDesc(Nodecode);
		
		if(objupdatehelp == null)
		{
			objupdatehelp = new Helpdocument();
			objupdatehelp.setNodecode(Nodecode);
			
			if(objupdatehelp.getId() != null && objupdatehelp.getDocumentname() != null)
			{
			fileManipulationservice.deletelargeattachments(objupdatehelp.getDocumentname());
			}
		}
	
		String id=  fileManipulationservice.storeLargeattachment("help", file);
		
		objupdatehelp.setFiletype(1);
        objupdatehelp.setDocumentname(id);
        objupdatehelp.setFileref(id);
        //objupdatehelp.setLshelpdocumentcontent(randomUUIDString);
        
        HelpdocumentRepository.save(objupdatehelp);
       
        return objupdatehelp;
	}
	
	public Helptittle getnodeonpage(String page)
	{
		return helptittleRepository.findFirst1ByPageOrderByNodecodeDesc(page);
	}
	
	public Map<String, Object> uploadhelpimages(MultipartFile file, String originurl,String ismultitenant) {
		Map<String, Object> map = new HashMap<String, Object>();

		String id = null;
		String downloadservice ="downloadhelpimage";
		try {
			if(ismultitenant.equals("1"))
			{
				id = cloudFileManipulationservice.storecloudfilesreturnUUID(file, "");
				downloadservice ="downloadhelpimage";
			}
			else
			{
				id = fileManipulationservice.storeLargeattachment("help", file);
				downloadservice ="downloadhelpimageonprim";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		map.put("link", originurl + "/helpdocument/"+downloadservice+"/" + id + "/"
				+ TenantContext.getCurrentTenant() + "/" + FilenameUtils.removeExtension(file.getOriginalFilename()) + "/" + FilenameUtils.getExtension(file.getOriginalFilename()));

		return map;
	}
	
	public ByteArrayInputStream downloadhelpimage(String fileid, String tenant) {
		TenantContext.setCurrentTenant(tenant);
		byte[] data = null;
		try {
			data = StreamUtils.copyToByteArray(
					cloudFileManipulationservice.retrieveCloudFile(fileid, tenant + ""));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(data);

		return bis;
	}
	
	public GridFSDBFile downloadhelpimageonprim(String fileid) throws IllegalStateException, IOException
	{
		return fileManipulationservice.retrieveLargeFile(fileid);
	}
	
	public boolean removehelpimage(Map<String, String> body) {
		String filid = body.get("fileid");
		String ismultitenant = body.get("ismultitenant");
		if(ismultitenant.equals("1"))
		{
			cloudFileManipulationservice.deleteFile(filid, "main");
		}
		else
		{
			fileManipulationservice.deletelargeattachments(filid);
		}
		
		return true;
	}
}
