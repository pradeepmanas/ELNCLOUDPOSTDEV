package com.agaram.eln.primary.service.cloudFileManip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cloudFileManip.CloudOrderAttachment;
import com.agaram.eln.primary.model.cloudFileManip.CloudProfilePicture;
import com.agaram.eln.primary.model.cloudFileManip.CloudUserSignature;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudOrderAttachmentRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudProfilePictureRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudUserSignatureRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

@Service
@Transactional 
public class CloudFileManipulationservice {
	
	@Autowired
	private CloudOrderAttachmentRepository cloudOrderAttachmentRepository;
 	
	@Autowired
    private CloudProfilePictureRepository cloudProfilePictureRepository;

	@Autowired
	private LScfttransactionRepository lscfttransactionRepository;
 	
	@Autowired
    private LSuserMasterRepository lsuserMasterRepository;
	
	@Autowired
    private CloudUserSignatureRepository cloudusersignatureRepository;
	
	@Autowired
    private Environment env;
	
	
    
	public CloudProfilePicture addPhoto(Integer usercode, MultipartFile file,Date currentdate) throws IOException { 
    	
    	LSuserMaster username=lsuserMasterRepository.findByusercode(usercode);
    	String name=username.getUsername();
       	LScfttransaction list= new LScfttransaction();
    	list.setModuleName("UserManagement");
    	list.setComments(name+" "+"Uploaded the profile picture successfully");
    	list.setActions("View / Load");
    	list.setSystemcoments("System Generated");
    	list.setTableName("profile");
    	list.setTransactiondate(currentdate);
    	list.setLsuserMaster(usercode);
		lscfttransactionRepository.save(list);
		deletePhoto(usercode,list);
		
		CloudProfilePicture profile = new CloudProfilePicture(); 
    	profile.setId(usercode);
    	profile.setName(file.getName());
    	profile.setImage(
          new Binary(BsonBinarySubType.BINARY, file.getBytes())); 
    	profile = cloudProfilePictureRepository.save(profile); 
 
    	return profile; 
    }
	
	public CloudUserSignature addusersignature(Integer usercode, String username, MultipartFile file,Date currentdate) throws IOException { 
		CloudUserSignature usersignature = new CloudUserSignature();
		usersignature.setId(usercode);
		usersignature.setName(file.getName());
		usersignature.setImage(
          new Binary(BsonBinarySubType.BINARY, file.getBytes())); 
		usersignature = cloudusersignatureRepository.save(usersignature); 
		return usersignature;
	}
	
	public CloudProfilePicture getPhoto(Integer id) { 
		   
        return cloudProfilePictureRepository.findById(id); 
    }
	
	public CloudUserSignature getSignature(Integer id) { 
		   
        return cloudusersignatureRepository.findOne(id); 
    }
    
    public Long deletePhoto(Integer id,LScfttransaction list) { 
    	list.setTableName("ProfilePicture");
    	lscfttransactionRepository.save(list);
        return cloudProfilePictureRepository.deleteById(id); 
    }
    
    public CloudOrderAttachment storeattachment(MultipartFile file) throws IOException
    {
    	UUID objGUID = UUID.randomUUID();
        String randomUUIDString = objGUID.toString();
        
    	CloudOrderAttachment objattachment = new CloudOrderAttachment();
    	objattachment.setFile(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
    	objattachment.setFileid(randomUUIDString);
    	
    	objattachment = cloudOrderAttachmentRepository.save(objattachment);
    	
    	return objattachment;
    }
    
    public String storeLargeattachment(String title, MultipartFile file) throws IOException { 
       
    	String bloburi="";
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");

		UUID objGUID = UUID.randomUUID();
        String randomUUIDString = objGUID.toString();
        
		try {    
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());

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
			
			bloburi = blob.getName();

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
       
        return bloburi; //id.toString(); 
    }
    
    public CloudOrderAttachment retrieveFile(LsOrderattachments objattachment){
    
    	CloudOrderAttachment objfile = cloudOrderAttachmentRepository.findByFileid(objattachment.getFileid());
    	
    	if(objfile == null)
    	{
    		cloudOrderAttachmentRepository.findById(Integer.parseInt(objattachment.getFileid()));
    	}
	
        return objfile;
	}
    
    public InputStream retrieveLargeFile(String fileid) throws IOException{
    	
	    CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {    
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());
			
			blob = container.getBlockBlobReference(fileid);
			return blob.openInputStream();
		}
		catch (StorageException ex)
		{
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
			throw new IOException(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
		}
		catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
		return null;
	    
    }
    
    public Long deleteattachments(String id) { 
    	Long count = cloudOrderAttachmentRepository.deleteByFileid(id);
    	if(count <= 0)
    	{
    		count = cloudOrderAttachmentRepository.deleteById(Integer.parseInt(id)); 
    	}
        return count;
    }
    
    public void deletelargeattachments(String id) { 
    	CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {    
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());
			
			blob = container.getBlockBlobReference(id);
			blob.deleteIfExists();
		}
		catch (StorageException ex)
		{
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
		}
		catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
    }
    
    

    public String storeReportFile(String title, File file) throws IOException { 
        
    	String bloburi="";
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");

		try {    
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());

			// Create the container if it does not exist with public access.
			System.out.println("Creating container: " + container.getName());
			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());		    

//			File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+title);
//			file.transferTo(convFile);

			
			//Getting a blob reference
			CloudBlockBlob blob = container.getBlockBlobReference(file.getName());

			//Creating blob and uploading file to it
			System.out.println("Uploading the sample file ");
			blob.uploadFromFile(file.getAbsolutePath());
			
			bloburi = blob.getName();

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
       
        return bloburi; //id.toString(); 
    }
    
    public InputStream retrieveReportFiles(String fileid) throws IOException{

	    CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {    
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());
			
			blob = container.getBlockBlobReference(fileid);
			return blob.openInputStream();
		}
		catch (StorageException ex)
		{
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
			throw new IOException(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
		}
		catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
		return null;
	    
    }
    
    public void deleteReportFile(String id) { 
    	CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {    
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());
			
			blob = container.getBlockBlobReference(id);
			blob.deleteIfExists();
		}
		catch (StorageException ex)
		{
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
		}
		catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
    }
    
    public String storecloudfilesreturnUUID(MultipartFile file, String containername) throws IOException { 
        
    	//String bloburi="";
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");

		UUID objGUID = UUID.randomUUID();
        String randomUUIDString = objGUID.toString();
        
		try {    
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant()+containername);

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
			
			//bloburi = blob.getName();

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
       
        return randomUUIDString; //id.toString(); 
    }
    
 public InputStream retrieveCloudFile(String fileid, String containername) throws IOException{
    	
	    CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {    
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(containername);
			
			blob = container.getBlockBlobReference(fileid);
			return blob.openInputStream();
		}
		catch (StorageException ex)
		{
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
			throw new IOException(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
		}
		catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
		return null;
	    
    }
 
 public InputStream updateversionCloudFile(String fileid, String containername,String newfieldid) throws IOException{
 	
	    CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {    
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(containername);
			
			blob = container.getBlockBlobReference(fileid);
			
			File targetFile = new File(System.getProperty("java.io.tmpdir")+"/"+newfieldid);
			
			FileUtils.copyInputStreamToFile(blob.openInputStream(), targetFile);
			
			//Getting a blob reference
			CloudBlockBlob blob1 = container.getBlockBlobReference(targetFile.getName());

			//Creating blob and uploading file to it
			System.out.println("Uploading the sample file ");
			blob1.uploadFromFile(targetFile.getAbsolutePath());
			
			return blob.openInputStream();
		}
		catch (StorageException ex)
		{
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
			throw new IOException(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
		}
		catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
		return null;
	    
 }
 
 public InputStream movestreamstocontainer(InputStream InputStream, String containername,String fileid) throws IOException{
	 	
	    CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {    
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(containername);
			
			File targetFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileid);
			
			FileUtils.copyInputStreamToFile(InputStream, targetFile);
			
			//Getting a blob reference
			blob = container.getBlockBlobReference(targetFile.getName());

			//Creating blob and uploading file to it
			System.out.println("Uploading the sample file ");
			blob.uploadFromFile(targetFile.getAbsolutePath());
			
			return blob.openInputStream();
		}
		catch (StorageException ex)
		{
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
			throw new IOException(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
		}
		catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
		return null;
	    
}
 
 public boolean movefiletoanothercontainerandremove(String fromcontainer, String tocontainer, String fileid)
 {
	 try {
		InputStream stream = retrieveCloudFile(fileid,fromcontainer);
		movestreamstocontainer(stream, tocontainer, fileid);
		deleteFile(fileid,fromcontainer);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
	 return true;
 }


 
 public void deletecloudFile(String id, String containername) { 
 	CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {    
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant()+containername);
			
			blob = container.getBlockBlobReference(id);
			blob.deleteIfExists();
		}
		catch (StorageException ex)
		{
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
		}
		catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
 }
 
 public void deleteFile(String id, String containername) { 
	 	CloudStorageAccount storageAccount;
			CloudBlobClient blobClient = null;
			CloudBlobContainer container=null;
			CloudBlockBlob blob = null;
			String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
			try {    
				// Parse the connection string and create a blob client to interact with Blob storage
				storageAccount = CloudStorageAccount.parse(storageConnectionString);
				blobClient = storageAccount.createCloudBlobClient();
				container = blobClient.getContainerReference(containername);
				
				
				blob = container.getBlockBlobReference(id);
				blob.deleteIfExists();
			}
			catch (StorageException ex)
			{
				System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
			}
			catch (Exception ex) 
			{
				System.out.println(ex.getMessage());
			}
	 }

public void updateVersiononFile(byte[] data,String fileid) throws Exception {
	File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileid);
    OutputStream outStream = new FileOutputStream(convFile);
    outStream.write(data);
	
}
}
