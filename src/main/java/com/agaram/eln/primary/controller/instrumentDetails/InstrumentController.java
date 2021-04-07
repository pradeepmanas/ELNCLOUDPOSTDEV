package com.agaram.eln.primary.controller.instrumentDetails;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.model.cfr.LSactivity;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.instrumentDetails.LSresultdetails;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.instrumentDetails.Lsordersharedby;
import com.agaram.eln.primary.model.instrumentDetails.Lsordershareto;
import com.agaram.eln.primary.model.notification.Email;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefileversion;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.service.cfr.AuditService;
import com.agaram.eln.primary.service.instrumentDetails.InstrumentService;
import com.mongodb.gridfs.GridFSDBFile;

@RestController
@RequestMapping(value = "/Instrument")
public class InstrumentController {
	
	@Autowired
    private InstrumentService instrumentService;
	

	
	@Autowired
	private AuditService auditService;
	@PostMapping("/GetInstrumentParameters")
	public Map<String, Object> getInstrumentparameters(@RequestBody LSSiteMaster lssiteMaster) {
		return instrumentService.getInstrumentparameters(lssiteMaster);
	}
	
	@PostMapping("/InsertELNOrder")
	public LSlogilablimsorderdetail InsertELNOrder(@RequestBody LSlogilablimsorderdetail objorder) {
		if(objorder.getObjuser() != null) {
			
			LSuserMaster userClass = auditService.CheckUserPassWord(objorder.getObjuser());
	        if(userClass.getObjResponse().getStatus()) {
				
	        	objorder.setLsuserMaster(userClass);
				
	        	return instrumentService.InsertELNOrder(objorder);
			}
			else
			{
				objorder.getObjsilentaudit().setComments("Entered invalid username and password");
				Map<String, Object> map=new HashMap<>();
				map.put("objsilentaudit",objorder.getObjsilentaudit());
				map.put("objmanualaudit",objorder.getObjmanualaudit());
				map.put("objUser",objorder.getObjuser());
				auditService.AuditConfigurationrecord(map);
				objorder.setResponse(new Response());
				objorder.getResponse().setStatus(false);
				objorder.getResponse().setInformation("ID_VALIDATION");
				return objorder;
			}
			
			
		}
		return instrumentService.InsertELNOrder(objorder);
	}
	
	@PostMapping("/InsertActivities")
	public LSactivity InsertActivities(@RequestBody LSactivity objActivity)
	{
		return instrumentService.InsertActivities(objActivity);
	}
	
	@PostMapping("/Getorderbytype")
	public Map<String, Object> Getorderbytype(@RequestBody LSlogilablimsorderdetail objorder)
	{
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		objorder.setOrderflag("N");
		if(objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator"))
		{
			mapOrders.put("Pending", instrumentService.Getorderbytype(objorder));
		}
		else
		{
			if(objorder.getFiletype().equals(0))
			{
				mapOrders.put("Pending", instrumentService.Getorderbytype(objorder));
			}
			else
			{
				mapOrders.put("Pending", instrumentService.Getorderbytypeanduser(objorder));
			}
		}
		objorder.setOrderflag("R");
		if(objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator"))
		{
			mapOrders.put("Completed", instrumentService.Getorderbytype(objorder));
		}
		else
		{
			if(objorder.getFiletype().equals(0))
			{
				mapOrders.put("Completed", instrumentService.Getorderbytype(objorder));
			}
			else
			{
				mapOrders.put("Completed", instrumentService.Getorderbytypeanduser(objorder));
			}
		}
		return mapOrders;
	}
	
	@PostMapping("/Getorderbytypeandflag")
	public Map<String, Object> Getorderbytypeandflag(@RequestBody LSlogilablimsorderdetail objorder)
	{
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		if(objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator"))
		{
			instrumentService.Getorderbytypeandflag(objorder, mapOrders);
		}
		else
		{
			if(objorder.getFiletype().equals(0))
			{
				instrumentService.Getorderbytypeandflag(objorder, mapOrders);
			}
			else
			{
				instrumentService.Getorderbytypeandflaganduser(objorder,mapOrders);
			}
		}
		
		mapOrders.put("Orderflag",objorder.getOrderflag());

		return mapOrders;
	}
	
	@PostMapping("/Getorderbytypeandflaglazy")
	public Map<String, Object> Getorderbytypeandflaglazy(@RequestBody LSlogilablimsorderdetail objorder)
	{
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		if(objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator"))
		{
			instrumentService.Getorderbytypeandflaglazy(objorder, mapOrders);
		}
		else
		{
			if(objorder.getFiletype().equals(0))
			{
				instrumentService.Getorderbytypeandflaglazy(objorder, mapOrders);
			}
			else
			{
				instrumentService.Getorderbytypeandflaganduserlazy(objorder,mapOrders);
			}
		}
		
		mapOrders.put("Orderflag",objorder.getOrderflag());

		return mapOrders;
	}
	
	@PostMapping("/Getorderallbytypeandflaglazy")
	public Map<String, Object> Getorderallbytypeandflaglazy(@RequestBody LSlogilablimsorderdetail objorder)
	{
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		if(objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator"))
		{
			instrumentService.Getorderallbytypeandflaglazy(objorder, mapOrders);
		}
		else
		{
			if(objorder.getFiletype().equals(0))
			{
				instrumentService.Getorderallbytypeandflaglazy(objorder, mapOrders);
			}
			else
			{
				instrumentService.Getorderallbytypeandflaganduserlazy(objorder,mapOrders);
			}
		}
		
		mapOrders.put("Orderflag",objorder.getOrderflag());

		return mapOrders;
	}
	
	@PostMapping("/GetWorkflowonUser")
	public List<LSworkflow> GetWorkflowonUser(@RequestBody LSuserMaster objuser)
	{
		return instrumentService.GetWorkflowonUser(objuser);
	}
	
	@PostMapping("/GetWorkflowanduseronUsercode")
	public Map<String, Object> GetWorkflowanduseronUsercode(@RequestBody LSuserMaster usercode)
	{
		return instrumentService.GetWorkflowanduseronUsercode(usercode);
	}
	
	@PostMapping("/GetorderStatus")
	public LSlogilablimsorderdetail GetorderStatus(@RequestBody LSlogilablimsorderdetail objorder)
	{
		return instrumentService.GetorderStatus(objorder);
	}
	
	@PostMapping("/GetdetailorderStatus")
	public LSlogilablimsorderdetail GetdetailorderStatus(@RequestBody LSlogilablimsorderdetail objupdatedorder)
	{
		return instrumentService.GetdetailorderStatus(objupdatedorder);
	}
	
	@PostMapping("/GetResults")
	public List<LSresultdetails> GetResults(@RequestBody LSlogilablimsorderdetail objorder)
	{
		return instrumentService.GetResults(objorder);
	}
	
	@PostMapping("/SaveResultfile")
	public LSsamplefile SaveResultfile(@RequestBody LSsamplefile objfile)
	{
		 if(objfile.getObjuser() != null) {
				
				LSuserMaster userClass = auditService.CheckUserPassWord(objfile.getObjuser());
		        if(userClass.getObjResponse().getStatus()) {
					
		        	objfile.setLsuserMaster(userClass);
					
		        	return instrumentService.SaveResultfile(objfile);
				}
				else
				{
					objfile.getObjsilentaudit().setComments("Entered invalid username and password");
					Map<String, Object> map=new HashMap<>();
					map.put("objsilentaudit",objfile.getObjsilentaudit());
					map.put("objmanualaudit",objfile.getObjmanualaudit());
					map.put("objUser",objfile.getObjuser());
					auditService.AuditConfigurationrecord(map);
					objfile.setResponse(new Response());
					objfile.getResponse().setStatus(false);
					objfile.getResponse().setInformation("ID_VALIDATION");
					return objfile;
				}
				
				
			}
		return instrumentService.SaveResultfile(objfile);
	}
	
	@PostMapping("/UpdateLimsOrder")
	public LSlogilablimsorderdetail UpdateLimsOrder(@RequestBody LSlogilablimsorderdetail objorder)
	{
        if(objorder.getObjuser() != null) {
			
			LSuserMaster userClass = auditService.CheckUserPassWord(objorder.getObjuser());
	        if(userClass.getObjResponse().getStatus()) {
				
	        	objorder.setLsuserMaster(userClass);
				
	        	return instrumentService.UpdateLimsOrder(objorder);
			}
			else
			{
				objorder.getObjsilentaudit().setComments("Entered invalid username and password");
				Map<String, Object> map=new HashMap<>();
				map.put("objsilentaudit",objorder.getObjsilentaudit());
				map.put("objmanualaudit",objorder.getObjmanualaudit());
				map.put("objUser",objorder.getObjuser());
				auditService.AuditConfigurationrecord(map);
				objorder.setResponse(new Response());
				objorder.getResponse().setStatus(false);
				objorder.getResponse().setInformation("ID_VALIDATION");
				return objorder;
			}
			
			
		}
		return instrumentService.UpdateLimsOrder(objorder);
	}
	
	@PostMapping("/Getupdatedorder")
	public LSlogilablimsorderdetail Getupdatedorder(@RequestBody LSlogilablimsorderdetail objorder)
	{
		return instrumentService.Getupdatedorder(objorder);
	}
	
	@PostMapping("/Getorderforlink")
	public Map<String, Object> Getorderforlink(@RequestBody LSlogilablimsorderdetail objorder)
	{
		return instrumentService.Getorderforlink(objorder);
	}

//	@PostMapping("/GetLimsOrder")
//	public Map<String, Object> GetLimsOrder(@RequestBody LSlimsorder clsOrder)
//	{
//		return instrumentService.GetLimsOrder(clsOrder);
//	}
	
	@PostMapping("/CompleteOrder")
	public LSlogilablimsorderdetail CompleteOrder(@RequestBody LSlogilablimsorderdetail objorder)
	{
         if(objorder.getObjuser() != null) {
			
			LSuserMaster userClass = auditService.CheckUserPassWord(objorder.getObjuser());
	        if(userClass.getObjResponse().getStatus()) {
				
	        	objorder.setLsuserMaster(userClass);
				
	        	return instrumentService.CompleteOrder(objorder);
			}
			else
			{
				objorder.getObjsilentaudit().setComments("Entered invalid username and password");
				Map<String, Object> map=new HashMap<>();
				map.put("objsilentaudit",objorder.getObjsilentaudit());
				map.put("objmanualaudit",objorder.getObjmanualaudit());
				map.put("objUser",objorder.getObjuser());
				auditService.AuditConfigurationrecord(map);
				objorder.setResponse(new Response());
				objorder.getResponse().setStatus(false);
				objorder.getResponse().setInformation("ID_VALIDATION");
				return objorder;
			}
			
			
		}
		return instrumentService.CompleteOrder(objorder);
	}
	
	@PostMapping("/updateworflowforOrder")
	public LSlogilablimsorderdetail updateworflowforOrder(@RequestBody LSlogilablimsorderdetail objorder)
	{
		return instrumentService.updateworflowforOrder(objorder);
	}
	
	@PostMapping("/Getfileversions")
	public List<LSsamplefileversion> Getfileversions(@RequestBody LSsamplefile objfile)
	{
		return instrumentService.Getfileversions(objfile);
	}
	
	@PostMapping("/GetfileverContent")
	public String GetfileverContent(@RequestBody LSsamplefile objfile)
	{
		return instrumentService.GetfileverContent(objfile);
	}
	
	@PostMapping("/GetResultfileverContent")
	public LSsamplefile GetResultfileverContent(@RequestBody LSsamplefile objfile)
	{
		return instrumentService.GetResultfileverContent(objfile);
	}
	
	@PostMapping("/Getorderbyfile")
	public List<LSlogilablimsorderdetail> Getorderbyfile(@RequestBody LSlogilablimsorderdetail objorder)
	{
		return instrumentService.Getorderbyfile(objorder);
	}
	
	@PostMapping("/Getexcelorder")
	public List<LSlogilablimsorderdetail> Getexcelorder(@RequestBody LSlogilablimsorderdetail objorder)
	{
		return instrumentService.Getexcelorder(objorder);
	}
	
	@PostMapping("/updateVersionandWorkflowhistory")
	public LSlogilablimsorderdetail updateVersionandWorkflowhistory(@RequestBody LSlogilablimsorderdetail objorder)
	{
		return instrumentService.updateVersionandWorkflowhistory(objorder);
	}
	
	@PostMapping("/Uploadattachments")
    public LSlogilablimsorderdetail Uploadattachments(@RequestParam("file") MultipartFile file
    		, @RequestParam("order") Long batchcode,@RequestParam("filename") String filename
    		,@RequestParam("fileexe") String fileexe, @RequestParam("usercode") Integer usercode
    		, @RequestParam("date") Date currentdate, @RequestParam("islargefile") Integer islargefile) throws IOException 
	{    
        return instrumentService.Uploadattachments(file, batchcode,filename,fileexe,usercode,currentdate,islargefile);
    }
	
	@PostMapping("/CloudUploadattachments")
    public LSlogilablimsorderdetail CloudUploadattachments(@RequestParam("file") MultipartFile file
    		, @RequestParam("order") Long batchcode,@RequestParam("filename") String filename
    		,@RequestParam("fileexe") String fileexe, @RequestParam("usercode") Integer usercode
    		, @RequestParam("date") Date currentdate, @RequestParam("islargefile") Integer islargefile) throws IOException 
	{    
        return instrumentService.CloudUploadattachments(file, batchcode,filename,fileexe,usercode,currentdate,islargefile);
    }
	
	@PostMapping("/downloadattachments")
	public LsOrderattachments downloadattachments(@RequestBody LsOrderattachments objattachments) throws IllegalStateException, IOException
	{
		return instrumentService.downloadattachments(objattachments);
	}
	
	@PostMapping("/Clouddownloadattachments")
	public LsOrderattachments Clouddownloadattachments(@RequestBody LsOrderattachments objattachments) throws IllegalStateException, IOException
	{
		return instrumentService.Clouddownloadattachments(objattachments);
	}
	
	
	@RequestMapping(value = "attachment/{fileid}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadlargeattachment(@PathVariable String fileid) throws IllegalStateException, IOException {
	    GridFSDBFile gridFsFile = instrumentService.retrieveLargeFile(fileid);

	    HttpHeaders header = new HttpHeaders();
	    header.setContentType(MediaType.parseMediaType(gridFsFile.getContentType()));
	    header.setContentLength(gridFsFile.getLength());
	    header.set("Content-Disposition", "attachment; filename=gg.pdf");
	    
	    return new ResponseEntity<>(new InputStreamResource(gridFsFile.getInputStream()), header, HttpStatus.OK);
//	    return ResponseEntity.ok()
//	            .contentLength(gridFsFile.getLength())
//	            .contentType(MediaType.parseMediaType(gridFsFile.getContentType()))
//	            .body(new InputStreamResource(gridFsFile.getInputStream()));
	}
	
	@RequestMapping(value = "cloudattachment/{fileid}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadlargecloudattachment(@PathVariable String fileid) throws IllegalStateException, IOException {
	  
	    HttpHeaders header = new HttpHeaders();
	    header.set("Content-Disposition", "attachment; filename=gg.pdf");
	    return new ResponseEntity<>(new InputStreamResource(instrumentService.retrieveColudLargeFile(fileid)), header, HttpStatus.OK);
	}
	
	@PostMapping("/deleteattachments")
	public LsOrderattachments deleteattachments(@RequestBody LsOrderattachments objattachments)
	{
		return instrumentService.deleteattachments(objattachments);
	}
	
	@PostMapping("/Clouddeleteattachments")
	public LsOrderattachments Clouddeleteattachments(@RequestBody LsOrderattachments objattachments)
	{
		return instrumentService.Clouddeleteattachments(objattachments);
	}

	@PostMapping("/getmetatag")
	public String getmetatag()
	{
		return "[{\"e53b1590-7ee9-4d7f-992b-596dae18b9ed-T5\":{\"ParsedData\":{\"MultiFileds\":{\"MultiParserFields\":{\"MultiParsedFields_0\":[{\"No\":\"1\",\"SampleID\":\"TEST\",\"OpRot\":\"0.001\",\"Measurement\":\"0.001\",\"Unit\":\"OR\",\"Field 13\":\"633\",\"Temp\":\"28.7\",\"Time\":\"13:45:12\"},{\"No\":\"2\",\"SampleID\":\"TEST\",\"OpRot\":\"0.000\",\"Measurement\":\"0.000\",\"Unit\":\"OR\",\"Field 13\":\"633\",\"Temp\":\"28.7\",\"Time\":\"13:45:14\"},{\"No\":\"3\",\"SampleID\":\"TEST\",\"OpRot\":\"0.001\",\"Measurement\":\"0.001\",\"Unit\":\"OR\",\"Field 13\":\"633\",\"Temp\":\"28.7\",\"Time\":\"13:45:16\"},{\"No\":\"4\",\"SampleID\":\"TEST\",\"OpRot\":\"0.001\",\"Measurement\":\"0.001\",\"Unit\":\"OR\",\"Field 13\":\"633\",\"Temp\":\"28.7\",\"Time\":\"13:45:18\"},{\"No\":\"5\",\"SampleID\":\"TEST\",\"OpRot\":\"0.001\",\"Measurement\":\"0.001\",\"Unit\":\"OR\",\"Field 13\":\"633\",\"Temp\":\"28.7\",\"Time\":\"13:45:20\"}]},\"MultiParsedFieldsTableInfo\":{\"MultiParsedFieldsTableInfo_0\":[{\"sFieldName\":\"Date:\",\"sFieldValue\":\"Nov 5, 2015\"},{\"sFieldName\":\"LotID\",\"sFieldValue\":\"TEST\"},{\"sFieldName\":\"Set Temp\",\"sFieldValue\":\"20.0\"},{\"sFieldName\":\"Temp Corr\",\"sFieldValue\":\"Off\"},{\"sFieldName\":\"Counts\",\"sFieldValue\":\"5\"},{\"sFieldName\":\"Average\",\"sFieldValue\":\"0.001\"},{\"sFieldName\":\"STD Dev\",\"sFieldValue\":\"0.0004\"},{\"sFieldName\":\"maximum\",\"sFieldValue\":\"0.001\"},{\"sFieldName\":\"minimum\",\"sFieldValue\":\"0.000\"}]}},\"SingleFields\":[{\"FieldName\":\"Date:\",\"FieldValue\":\"Nov 5, 2015\"},{\"FieldName\":\"LotID\",\"FieldValue\":\"TEST\"},{\"FieldName\":\"Set Temp\",\"FieldValue\":\"20.0\"},{\"FieldName\":\"Temp Corr\",\"FieldValue\":\"Off\"},{\"FieldName\":\"Counts\",\"FieldValue\":\"5\"},{\"FieldName\":\"Average\",\"FieldValue\":\"0.001\"},{\"FieldName\":\"STD Dev\",\"FieldValue\":\"0.0004\"},{\"FieldName\":\"maximum\",\"FieldValue\":\"0.001\"},{\"FieldName\":\"minimum\",\"FieldValue\":\"0.000\"}]},\"FileMetaTags\":[{\"Property\":\"Name\",\"Values\":\"or3\"},{\"Property\":\"Size\",\"Values\":\"743 bytes\"},{\"Property\":\"Itemtype\",\"Values\":\"Text Document\"},{\"Property\":\"Date modified\",\"Values\":\"24-09-2020 11:59\"},{\"Property\":\"Date created\",\"Values\":\"25-09-2020 18:49\"},{\"Property\":\"Date accessed\",\"Values\":\"25-09-2020 18:50\"},{\"Property\":\"Attributes\",\"Values\":\"A\"},{\"Property\":\"Perceived type\",\"Values\":\"Text\"},{\"Property\":\"Owner\",\"Values\":\"AGL66\\\\Pasupathi\"},{\"Property\":\"Kind\",\"Values\":\"Document\"},{\"Property\":\"Rating\",\"Values\":\"Unrated\"}],\"Tags\":{\"UnMappedTemplateTags\":[{\"TagName\":\"ARNo\",\"TagValue\":\"arval\",\"CreatedBy\":\"Administrator\",\"CreatedOn\":\"2020-10-09 12:50:58\"},{\"TagName\":\"AR_No\",\"TagValue\":\"arval\",\"CreatedBy\":\"Administrator\",\"CreatedOn\":\"2020-10-10 04:46:56\"}],\"MappedTemplateTags\":{\"QC\":[{\"TagName\":\"Sample\",\"TagValue\":\"Caffeine Oral Citrate\",\"CreatedBy\":\"Administrator\",\"CreatedOn\":\"2020-09-25 13:20:38\"},{\"TagName\":\"Test\",\"TagValue\":\"Assay by HPLC\",\"CreatedBy\":\"Administrator\",\"CreatedOn\":\"2020-09-25 13:20:38\"}],\"Default\":[{\"TagName\":\"Default\",\"TagValue\":\"Default\",\"CreatedBy\":\"Administrator\",\"CreatedOn\":\"2020-09-25 13:20:08\"}],\"Check value\":[{\"TagName\":\"AR_No\",\"TagValue\":\"arval\",\"CreatedBy\":\"Administrator\",\"CreatedOn\":\"2020-10-14 10:00:50\"}]}}}}]";
	}
		
	@PostMapping("/Insertshareorder")
	public Lsordershareto Insertshareorder(@RequestBody Lsordershareto objordershareto)
	{
		return instrumentService.Insertshareorder(objordershareto);
	}
	
	@PostMapping("/Insertshareorderby")
	public Map<String, Object> Insertshareorderby(@RequestBody Lsordersharedby objordersharedby)
	{
		return instrumentService.Insertshareorderby(objordersharedby);
	}
	
	@PostMapping("/Getordersharedbyme")
	public List<Lsordersharedby> Getordersharedbyme(@RequestBody Lsordersharedby lsordersharedby)
	{
		return instrumentService.Getordersharedbyme(lsordersharedby);
	}
	
	@PostMapping("/Getordersharetome")
	public List<Lsordershareto> Getordersharetome(@RequestBody Lsordershareto lsordershareto)
	{
		return instrumentService.Getordersharetome(lsordershareto);
	}
	
	@PostMapping("/Unshareorderby")
	public Lsordersharedby Unshareorderby(@RequestBody Lsordersharedby objordershareby)
	{
		return instrumentService.Unshareorderby(objordershareby);
	}

	@PostMapping("/Unshareorderto")
	public Lsordershareto Unshareorderto(@RequestBody Lsordershareto lsordershareto)
	{
		return instrumentService.Unshareorderto(lsordershareto);
	}
	
	@PostMapping("/GetsharedorderStatus")
	public Lsordersharedby GetsharedorderStatus(@RequestBody Lsordersharedby objorder) throws IOException
	{
		return instrumentService.GetsharedorderStatus(objorder);
	}
	
	@PostMapping("/GetsharedtomeorderStatus")
	public Lsordershareto GetsharedtomeorderStatus(@RequestBody Lsordershareto objorder)
	{
		return instrumentService.GetsharedtomeorderStatus(objorder);
	}
	
	@PostMapping("/GetResultsharedfileverContent")
	public LSsamplefile GetResultsharedfileverContent(@RequestBody LSsamplefile objfile)
	{
		return instrumentService.GetResultsharedfileverContent(objfile);
	}
	
	@PostMapping("/SaveSharedResultfile")
	public LSsamplefile SaveSharedResultfile(@RequestBody LSsamplefile objfile)
	{
		return instrumentService.SaveSharedResultfile(objfile);
	}
	
	@PostMapping("/SharedCloudUploadattachments")
    public LSlogilablimsorderdetail SharedCloudUploadattachments(@RequestParam("file") MultipartFile file
    		, @RequestParam("order") Long batchcode,@RequestParam("filename") String filename
    		,@RequestParam("fileexe") String fileexe, @RequestParam("usercode") Integer usercode
    		, @RequestParam("date") Date currentdate, @RequestParam("islargefile") Integer islargefile) throws IOException 
	{    
        return instrumentService.SharedCloudUploadattachments(file, batchcode,filename,fileexe,usercode,currentdate,islargefile);
    }
	
	@PostMapping("/SharedUploadattachments")
    public LSlogilablimsorderdetail SharedUploadattachments(@RequestParam("file") MultipartFile file
    		, @RequestParam("order") Long batchcode,@RequestParam("filename") String filename
    		,@RequestParam("fileexe") String fileexe, @RequestParam("usercode") Integer usercode
    		, @RequestParam("date") Date currentdate, @RequestParam("islargefile") Integer islargefile) throws IOException 
	{    
        return instrumentService.SharedUploadattachments(file, batchcode,filename,fileexe,usercode,currentdate,islargefile);
    }
	
	@PostMapping("/SharedClouddeleteattachments")
	public LsOrderattachments SharedClouddeleteattachments(@RequestBody LsOrderattachments objattachments)
	{
		return instrumentService.SharedClouddeleteattachments(objattachments);
	}
	
	@PostMapping("/shareddeleteattachments")
	public LsOrderattachments shareddeleteattachments(@RequestBody LsOrderattachments objattachments)
	{
		return instrumentService.shareddeleteattachments(objattachments);
	}
	
	@PostMapping("/SharedClouddownloadattachments")
	public LsOrderattachments SharedClouddownloadattachments(@RequestBody LsOrderattachments objattachments) throws IllegalStateException, IOException
	{
		return instrumentService.SharedClouddownloadattachments(objattachments);
	}
	
	@PostMapping("/Shareddownloadattachments")
	public LsOrderattachments Shareddownloadattachments(@RequestBody LsOrderattachments objattachments) throws IllegalStateException, IOException
	{
		return instrumentService.Shareddownloadattachments(objattachments);
	}
	
	@RequestMapping(value = "Sharedattachment/{fileid}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> Shareddownloadlargeattachment(@PathVariable String fileid) throws IllegalStateException, IOException {
	    GridFSDBFile gridFsFile = instrumentService.sharedretrieveLargeFile(fileid);

	    HttpHeaders header = new HttpHeaders();
	    header.setContentType(MediaType.parseMediaType(gridFsFile.getContentType()));
	    header.setContentLength(gridFsFile.getLength());
	    header.set("Content-Disposition", "attachment; filename=gg.pdf");
	    
	    return new ResponseEntity<>(new InputStreamResource(gridFsFile.getInputStream()), header, HttpStatus.OK);
	}
	
	@RequestMapping(value = "Sharedcloudattachment/{fileid}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> Shareddownloadlargecloudattachment(@PathVariable String fileid) throws IllegalStateException, IOException {
	  
	    HttpHeaders header = new HttpHeaders();
	    header.set("Content-Disposition", "attachment; filename=gg.pdf");
	    return new ResponseEntity<>(new InputStreamResource(instrumentService.sharedretrieveColudLargeFile(fileid)), header, HttpStatus.OK);
	}

}
