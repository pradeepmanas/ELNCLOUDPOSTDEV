package com.agaram.eln.primary.controller.instrumentDetails;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

import com.agaram.eln.primary.fetchmodel.getorders.Logilabordermaster;
import com.agaram.eln.primary.fetchmodel.getorders.Logilaborders;
import com.agaram.eln.primary.model.cfr.LSactivity;
//import com.agaram.eln.primary.model.cfr.LSaudittrailconfiguration;
import com.agaram.eln.primary.model.fileManipulation.Fileimages;
import com.agaram.eln.primary.model.fileManipulation.Fileimagestemp;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSSheetOrderStructure;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderSampleUpdate;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.instrumentDetails.LsSheetorderlimsrefrence;
import com.agaram.eln.primary.model.instrumentDetails.Lsordersharedby;
import com.agaram.eln.primary.model.instrumentDetails.Lsordershareto;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.methodsetup.ELNFileAttachments;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefileversion;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.repository.sheetManipulation.LSsamplefileRepository;
import com.agaram.eln.primary.service.instrumentDetails.InstrumentService;
import com.mongodb.gridfs.GridFSDBFile;

@RestController
@RequestMapping(value = "/Instrument")
public class InstrumentController {

	@Autowired
	private InstrumentService instrumentService;

	@Autowired
	private LSsamplefileRepository lssamplefileRepository;

	@PostMapping("/GetInstrumentParameters")
	public Map<String, Object> getInstrumentparameters(@RequestBody LSSiteMaster lssiteMaster)throws Exception {
		return instrumentService.getInstrumentparameters(lssiteMaster);
	}

	@PostMapping("/InsertELNOrder")
	public LSlogilablimsorderdetail InsertELNOrder(@RequestBody LSlogilablimsorderdetail objorder)throws Exception {

		return instrumentService.InsertELNOrder(objorder);
	}
	
	@PostMapping("/GetOrderonClose")
	public Logilabordermaster GetOrderonClose(@RequestBody LSlogilablimsorderdetail objorder) {

		return instrumentService.GetOrderonClose(objorder);
	}
	
	@PostMapping("/GetdOrderCount")
	public Map<String, Object> getdOrderCount(@RequestBody LSuserMaster objuser)throws Exception {
		return instrumentService.getdOrderCount(objuser);
	}

	@PostMapping("/InsertActivities")
	public LSactivity InsertActivities(@RequestBody LSactivity objActivity)throws Exception {
		return instrumentService.InsertActivities(objActivity);
	}

	@PostMapping("/Getorderbytype")
	public Map<String, Object> Getorderbytype(@RequestBody LSlogilablimsorderdetail objorder)throws Exception {
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		objorder.setOrderflag("N");
		if (objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
			mapOrders.put("Pending", instrumentService.Getorderbytype(objorder));
		} else {
			if (objorder.getFiletype().equals(0)) {
				mapOrders.put("Pending", instrumentService.Getorderbytype(objorder));
			} else {
				mapOrders.put("Pending", instrumentService.Getorderbytypeanduser(objorder));
			}
		}
		objorder.setOrderflag("R");
		if (objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
			mapOrders.put("Completed", instrumentService.Getorderbytype(objorder));
		} else {
			if (objorder.getFiletype().equals(0)) {
				mapOrders.put("Completed", instrumentService.Getorderbytype(objorder));
			} else {
				mapOrders.put("Completed", instrumentService.Getorderbytypeanduser(objorder));
			}
		}
		return mapOrders;
	}

	@PostMapping("/Getorderbytypeandflag")
	public Map<String, Object> Getorderbytypeandflag(@RequestBody LSlogilablimsorderdetail objorder)throws Exception {
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		if (objorder.getLsuserMaster().getUsername() != null
				&& objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {

			instrumentService.GetorderbytypeandflagOrdersonly(objorder, mapOrders);
		} else {
			if (objorder.getFiletype().equals(0)) {

				instrumentService.GetorderbytypeandflagOrdersonly(objorder, mapOrders);
			} else {

				instrumentService.GetorderbytypeandflaganduserOrdersonly(objorder, mapOrders);
			}
		}

		mapOrders.put("Orderflag", objorder.getOrderflag());

		return mapOrders;
	}

	@PostMapping("/Getorderbytypeandflaglazy")
	public Map<String, Object> Getorderbytypeandflaglazy(@RequestBody LSlogilablimsorderdetail objorder)throws Exception {
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		if (objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
			instrumentService.Getorderbytypeandflaglazy(objorder, mapOrders);
		} else {
			if (objorder.getFiletype().equals(0)) {
				instrumentService.Getorderbytypeandflaglazy(objorder, mapOrders);
			} else {
				instrumentService.Getorderbytypeandflaganduserlazy(objorder, mapOrders);
			}
		}

		mapOrders.put("Orderflag", objorder.getOrderflag());

		return mapOrders;
	}

	@PostMapping("/Getorderallbytypeandflaglazy")
	public Map<String, Object> Getorderallbytypeandflaglazy(@RequestBody LSlogilablimsorderdetail objorder)throws Exception {
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		if (objorder.getLsuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
			instrumentService.Getorderallbytypeandflaglazy(objorder, mapOrders);
		} else {
			if (objorder.getFiletype().equals(0)) {
				instrumentService.Getorderallbytypeandflaglazy(objorder, mapOrders);
			} else {
				instrumentService.Getorderallbytypeandflaganduserlazy(objorder, mapOrders);
			}
		}

		mapOrders.put("Orderflag", objorder.getOrderflag());

		return mapOrders;
	}

	@PostMapping("/GetWorkflowonUser")
	public List<LSworkflow> GetWorkflowonUser(@RequestBody LSuserMaster objuser)throws Exception {
		return instrumentService.GetWorkflowonUser(objuser);
	}

	@PostMapping("/GetWorkflowanduseronUsercode")
	public Map<String, Object> GetWorkflowanduseronUsercode(@RequestBody LSuserMaster usercode)throws Exception {
		return instrumentService.GetWorkflowanduseronUsercode(usercode);
	}

	@PostMapping("/GetorderStatus")
	public LSlogilablimsorderdetail GetorderStatus(@RequestBody LSlogilablimsorderdetail objorder)throws Exception {
		return instrumentService.GetorderStatus(objorder);
	}

	@PostMapping("/GetdetailorderStatus")
	public LSlogilablimsorderdetail GetdetailorderStatus(@RequestBody LSlogilablimsorderdetail objupdatedorder)throws Exception {
		return instrumentService.GetdetailorderStatus(objupdatedorder);
	}

	
	@PostMapping("/GetResults")
	public Map<String, Object> GetResults(@RequestBody LSlogilablimsorderdetail objorder)throws Exception {
		return instrumentService.GetResults(objorder);
	}
	
	
	@PostMapping("/SaveResultfile")
	public LSsamplefile SaveResultfile(@RequestBody LSsamplefile objfile)throws Exception {

		return instrumentService.SaveResultfile(objfile);
	}

	@PostMapping("/UpdateLimsOrder")
	public LSlogilablimsorderdetail UpdateLimsOrder(@RequestBody LSlogilablimsorderdetail objorder)throws Exception {
		
		return instrumentService.UpdateLimsOrder(objorder);
	}

	@PostMapping("/Getupdatedorder")
	public LSlogilablimsorderdetail Getupdatedorder(@RequestBody LSlogilablimsorderdetail objorder)throws Exception {
		return instrumentService.Getupdatedorder(objorder);
	}

	@PostMapping("/Getorderforlink")
	public Map<String, Object> Getorderforlink(@RequestBody LSlogilablimsorderdetail objorder)throws Exception {
		return instrumentService.Getorderforlink(objorder);
	}

	@PostMapping("/CompleteOrder")
	public LSlogilablimsorderdetail CompleteOrder(@RequestBody LSlogilablimsorderdetail objorder)throws Exception {
		return instrumentService.CompleteOrder(objorder);
	}

	@PostMapping("/updateworflowforOrder")
	public LSlogilablimsorderdetail updateworflowforOrder(@RequestBody LSlogilablimsorderdetail objorder)throws Exception {
		return instrumentService.updateworflowforOrder(objorder);
	}

	@PostMapping("/Getfileversions")
	public List<LSsamplefileversion> Getfileversions(@RequestBody LSsamplefile objfile)throws Exception {
		return instrumentService.Getfileversions(objfile);
	}

	@PostMapping("/GetfileverContent")
	public String GetfileverContent(@RequestBody LSsamplefile objfile)throws Exception {
		return instrumentService.GetfileverContent(objfile);
	}

	@PostMapping("/GetResultfileverContent")
	public LSsamplefile GetResultfileverContent(@RequestBody LSsamplefile objfile)throws Exception {
		return instrumentService.GetResultfileverContent(objfile);
	}

	@PostMapping("/Getorderbyfile")
	public List<LSlogilablimsorderdetail> Getorderbyfile(@RequestBody LSlogilablimsorderdetail objorder)throws Exception {
		return instrumentService.Getorderbyfile(objorder);
	}

	@PostMapping("/Getexcelorder")
	public List<LSlogilablimsorderdetail> Getexcelorder(@RequestBody LSlogilablimsorderdetail objorder)throws Exception {
		return instrumentService.Getexcelorder(objorder);
	}

	@PostMapping("/updateVersionandWorkflowhistory")
	public LSlogilablimsorderdetail updateVersionandWorkflowhistory(@RequestBody LSlogilablimsorderdetail objorder)throws Exception {
		return instrumentService.updateVersionandWorkflowhistory(objorder);
	}

	@PostMapping("/Uploadattachments")
	public LSlogilablimsorderdetail Uploadattachments(@RequestParam("file") MultipartFile file,
			@RequestParam("order") Long batchcode, @RequestParam("filename") String filename,
			@RequestParam("fileexe") String fileexe, @RequestParam("usercode") Integer usercode,
			@RequestParam("date") Date currentdate, @RequestParam("islargefile") Integer islargefile)
			throws IOException {
		return instrumentService.Uploadattachments(file, batchcode, filename, fileexe, usercode, currentdate,
				islargefile);
	}

	@PostMapping("/CloudUploadattachments")
	public LSlogilablimsorderdetail CloudUploadattachments(@RequestParam("file") MultipartFile file,
			@RequestParam("order") Long batchcode, @RequestParam("filename") String filename,
			@RequestParam("fileexe") String fileexe, @RequestParam("usercode") Integer usercode,
			@RequestParam("date") Date currentdate, @RequestParam("islargefile") Integer islargefile)
			throws IOException {
		return instrumentService.CloudUploadattachments(file, batchcode, filename, fileexe, usercode, currentdate,
				islargefile);
	}
	
	@PostMapping("/CloudELNFileUploadattachments")
	public Map<String, Object> CloudELNFileUploadattachments(@RequestParam("file") MultipartFile file,
			@RequestParam("order") Long batchcode, @RequestParam("filename") String filename,
			@RequestParam("fileexe") String fileexe, @RequestParam("usercode") Integer usercode,
			@RequestParam("date") Date currentdate, @RequestParam("islargefile") Integer islargefile,@RequestParam("methodkey") Integer methodkey)
			throws IOException {
		return instrumentService.CloudELNFileUploadattachments(file, batchcode, filename, fileexe, usercode, currentdate,
				islargefile,methodkey);
	}
	
	@PostMapping("/Uploadelnfileattachments")
	public Map<String, Object> Uploadelnfileattachments(@RequestParam("file") MultipartFile file,
			@RequestParam("order") Long batchcode, @RequestParam("filename") String filename,
			@RequestParam("fileexe") String fileexe, @RequestParam("usercode") Integer usercode,
			@RequestParam("date") Date currentdate, @RequestParam("islargefile") Integer islargefile)
			throws IOException {
//		return instrumentService.Uploadelnfileattachments(file, batchcode, filename, fileexe, usercode, currentdate,
//				islargefile);
		return instrumentService.Uploadelnfileattachments(file, batchcode, filename, fileexe, usercode, currentdate,
				islargefile);
	}

	@PostMapping("/downloadattachments")
	public LsOrderattachments downloadattachments(@RequestBody LsOrderattachments objattachments)
			throws IllegalStateException, IOException {
		return instrumentService.downloadattachments(objattachments);
	}
	
	@PostMapping("/downloadparserattachments")
	public ELNFileAttachments downloadparserattachments(@RequestBody ELNFileAttachments objattachments)
			throws IllegalStateException, IOException {
		return instrumentService.downloadparserattachments(objattachments);
	}

	@PostMapping("/Clouddownloadattachments")
	public LsOrderattachments Clouddownloadattachments(@RequestBody LsOrderattachments objattachments)
			throws IllegalStateException, IOException {
		return instrumentService.Clouddownloadattachments(objattachments);
	}
	
	@PostMapping("/Cloudparserdownloadattachments")
	public LsOrderattachments Cloudparserdownloadattachments(@RequestBody LsOrderattachments objattachments)
			throws IllegalStateException, IOException {
		return instrumentService.Cloudparserdownloadattachments(objattachments);
	}

	@RequestMapping(value = "attachment/{fileid}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadlargeattachment(@PathVariable String fileid)
			throws IllegalStateException, IOException {
		GridFSDBFile gridFsFile = instrumentService.retrieveLargeFile(fileid);

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType(gridFsFile.getContentType()));
		header.setContentLength(gridFsFile.getLength());
		header.set("Content-Disposition", "attachment; filename=gg.pdf");

		return new ResponseEntity<>(new InputStreamResource(gridFsFile.getInputStream()), header, HttpStatus.OK);
	}

	@PostMapping("/cloudattachment")
	public ResponseEntity<InputStreamResource> downloadlargecloudattachment(
			@RequestBody LsOrderattachments objattachments) throws IllegalStateException, IOException {

		HttpHeaders header = new HttpHeaders();
		header.set("Content-Disposition", "attachment; filename=gg.pdf");
		return new ResponseEntity<>(
				new InputStreamResource(instrumentService.retrieveColudLargeFile(objattachments.getFileid())), header,
				HttpStatus.OK);
	}

	@PostMapping("/deleteattachments")
	public LsOrderattachments deleteattachments(@RequestBody LsOrderattachments objattachments)throws Exception {
		return instrumentService.deleteattachments(objattachments);
	}

	@PostMapping("/Clouddeleteattachments")
	public LsOrderattachments Clouddeleteattachments(@RequestBody LsOrderattachments objattachments)throws Exception {
		return instrumentService.Clouddeleteattachments(objattachments);
	}

	@GetMapping("/getmetatag")
	public String getmetatag()throws Exception {
		String jsonString = "[{\"1e89782a-1357-4108-928c-4c05e2731397-T2\":{\"ParsedData\":{\"MultiFileds\":{\"MultiParserFields\":{\"MultiParsedFields_0\":[{\"SampleID\":\"Baseline\",\"Value1\":\"0.005\",\"Value2\":\"0.000\",\"Value3\":\"0.000\"},{\"SampleID\":\"Primer\",\"Value1\":\"0.502\",\"Value2\":\"0.982\",\"Value3\":\"1.004\"},{\"SampleID\":\"Cal.\",\"Value1\":\"0.506\",\"Value2\":\"0.996\",\"Value3\":\"0.996\"},{\"SampleID\":\"Cal.\",\"Value1\":\"0.396\",\"Value2\":\"0.504\",\"Value3\":\"0.508\"},{\"SampleID\":\"Cal.\",\"Value1\":\"0.297\",\"Value2\":\"0.207\",\"Value3\":\"0.200\"},{\"SampleID\":\"Cal.\",\"Value1\":\"0.196\",\"Value2\":\"0.099\",\"Value3\":\"0.095\"},{\"SampleID\":\"0.257\",\"Value1\":\"0.399\",\"Value2\":\"0.402\",\"Value3\":\"\"},{\"SampleID\":\"0.252\",\"Value1\":\"0.400\",\"Value2\":\"0.399\",\"Value3\":\"\"},{\"SampleID\":\"Final\",\"Value1\":\"Base\",\"Value2\":\"0.005\",\"Value3\":\"0.000\"}]},\"MultiParsedFieldsTableInfo\":{\"MultiParsedFieldsTableInfo_0\":[{\"sFieldName\":\"Method1\",\"sFieldValue\":\"MBAS\"},{\"sFieldName\":\"Method2\",\"sFieldValue\":\"phenol\"},{\"sFieldName\":\"Method3\",\"sFieldValue\":\"CN\"}]}},\"SingleFields\":[{\"FieldName\":\"Method1\",\"FieldValue\":\"MBAS\"},{\"FieldName\":\"Method2\",\"FieldValue\":\"phenol\"},{\"FieldName\":\"Method3\",\"FieldValue\":\"CN\"}]},\"FileMetaTags\":[{\"Property\":\"Name\",\"Values\":\"200701DR1-RUN-02\"},{\"Property\":\"Size\",\"Values\":\"14.1 KB\"},{\"Property\":\"Itemtype\",\"Values\":\"Adobe Acrobat Document\"},{\"Property\":\"Date modified\",\"Values\":\"18-02-2021 18:08\"},{\"Property\":\"Date created\",\"Values\":\"19-02-2021 17:36\"},{\"Property\":\"Date accessed\",\"Values\":\"19-02-2021 17:36\"},{\"Property\":\"Attributes\",\"Values\":\"A\"},{\"Property\":\"Perceived type\",\"Values\":\"Unspecified\"},{\"Property\":\"Owner\",\"Values\":\"AGL66\\\\Pasupathi\"},{\"Property\":\"Kind\",\"Values\":\"Document\"},{\"Property\":\"Rating\",\"Values\":\"Unrated\"}],\"BatchID\":\"001\",\"FileLink\":\"http://AGL66:8081/SDMS_Web/Login.html?GUID=1e89782a-1357-4108-928c-4c05e2731397&TaskID=T2\",\"TransferID\":\"BF204D77-F1D3-49E7-883B-61611C5A9F31\",\"OrderID\":\"20210218124411210\",\"Tags\":{\"UnMappedTemplateTags\":[],\"MappedTemplateTags\":{\"QC\":[{\"TagName\":\"Sample\",\"TagValue\":\"Pantoprazole tablets IP\",\"CreatedBy\":\"Administrator\",\"CreatedOn\":\"2021-02-19 12:06:15\"},{\"TagName\":\"Test\",\"TagValue\":\"Dissolution\",\"CreatedBy\":\"Administrator\",\"CreatedOn\":\"2021-02-19 12:06:15\"}]}}}}]";

		jsonString = instrumentService.getsampledata();
		try {

			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray("sheets");

			jsonArray.forEach(item -> {
				JSONObject obj = (JSONObject) item;
				JSONArray jsonrowsArray = obj.getJSONArray("rows");
				jsonrowsArray.forEach(rowitem -> {
					JSONObject rowobj = (JSONObject) rowitem;
					if (rowobj.getInt("index") == 2) {
						JSONArray jsoncellsArray = rowobj.getJSONArray("cells");
						jsoncellsArray.forEach(cellitem -> {
							JSONObject cellobj = (JSONObject) cellitem;
							if (cellobj.getInt("index") == 4) {
								cellobj.put("value", "feee");
							}
						});
					}

				});
			});

			jsonObject.put("sheets", jsonArray);

			jsonString = jsonObject.toString();

			System.out.println("\n\njsonArray: " + jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonString;
	}

	@PostMapping("/Insertshareorder")
	public Lsordershareto Insertshareorder(@RequestBody Lsordershareto objordershareto)throws Exception {
		return instrumentService.Insertshareorder(objordershareto);
	}

	@PostMapping("/Insertshareorderby")
	public Map<String, Object> Insertshareorderby(@RequestBody Lsordersharedby objordersharedby)throws Exception {
		return instrumentService.Insertshareorderby(objordersharedby);
	}

	@PostMapping("/Getordersharedbyme")
	public List<Lsordersharedby> Getordersharedbyme(@RequestBody Lsordersharedby lsordersharedby)throws Exception {
		return instrumentService.Getordersharedbyme(lsordersharedby);
	}

	@PostMapping("/Getordersharetome")
	public List<Lsordershareto> Getordersharetome(@RequestBody Lsordershareto lsordershareto)throws Exception {
		return instrumentService.Getordersharetome(lsordershareto);
	}

	@PostMapping("/Unshareorderby")
	public Lsordersharedby Unshareorderby(@RequestBody Lsordersharedby objordershareby)throws Exception {
		return instrumentService.Unshareorderby(objordershareby);
	}

	@PostMapping("/Unshareorderto")
	public Lsordershareto Unshareorderto(@RequestBody Lsordershareto lsordershareto)throws Exception {
		return instrumentService.Unshareorderto(lsordershareto);
	}

	@PostMapping("/GetsharedorderStatus")
	public Lsordersharedby GetsharedorderStatus(@RequestBody Lsordersharedby objorder) throws IOException {
		return instrumentService.GetsharedorderStatus(objorder);
	}

	@PostMapping("/GetsharedtomeorderStatus")
	public Lsordershareto GetsharedtomeorderStatus(@RequestBody Lsordershareto objorder)throws Exception {
		return instrumentService.GetsharedtomeorderStatus(objorder);
	}

	@PostMapping("/GetResultsharedfileverContent")
	public LSsamplefile GetResultsharedfileverContent(@RequestBody LSsamplefile objfile)throws Exception {
		return instrumentService.GetResultsharedfileverContent(objfile);
	}

	@PostMapping("/SaveSharedResultfile")
	public LSsamplefile SaveSharedResultfile(@RequestBody LSsamplefile objfile)throws Exception {
		return instrumentService.SaveSharedResultfile(objfile);
	}

	@PostMapping("/SharedCloudUploadattachments")
	public LSlogilablimsorderdetail SharedCloudUploadattachments(@RequestParam("file") MultipartFile file,
			@RequestParam("order") Long batchcode, @RequestParam("filename") String filename,
			@RequestParam("fileexe") String fileexe, @RequestParam("usercode") Integer usercode,
			@RequestParam("date") Date currentdate, @RequestParam("islargefile") Integer islargefile)
			throws IOException {
		return instrumentService.SharedCloudUploadattachments(file, batchcode, filename, fileexe, usercode, currentdate,
				islargefile);
	}

	@PostMapping("/SharedUploadattachments")
	public LSlogilablimsorderdetail SharedUploadattachments(@RequestParam("file") MultipartFile file,
			@RequestParam("order") Long batchcode, @RequestParam("filename") String filename,
			@RequestParam("fileexe") String fileexe, @RequestParam("usercode") Integer usercode,
			@RequestParam("date") Date currentdate, @RequestParam("islargefile") Integer islargefile)
			throws IOException {
		return instrumentService.SharedUploadattachments(file, batchcode, filename, fileexe, usercode, currentdate,
				islargefile);
	}

	@PostMapping("/SharedClouddeleteattachments")
	public LsOrderattachments SharedClouddeleteattachments(@RequestBody LsOrderattachments objattachments)throws Exception {
		return instrumentService.SharedClouddeleteattachments(objattachments);
	}

	@PostMapping("/shareddeleteattachments")
	public LsOrderattachments shareddeleteattachments(@RequestBody LsOrderattachments objattachments)throws Exception {
		return instrumentService.shareddeleteattachments(objattachments);
	}

	@PostMapping("/SharedClouddownloadattachments")
	public LsOrderattachments SharedClouddownloadattachments(@RequestBody LsOrderattachments objattachments)
			throws IllegalStateException, IOException {
		return instrumentService.SharedClouddownloadattachments(objattachments);
	}

	@PostMapping("/Shareddownloadattachments")
	public LsOrderattachments Shareddownloadattachments(@RequestBody LsOrderattachments objattachments)
			throws IllegalStateException, IOException {
		return instrumentService.Shareddownloadattachments(objattachments);
	}

	@RequestMapping(value = "Sharedattachment/{fileid}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> Shareddownloadlargeattachment(@PathVariable String fileid)
			throws IllegalStateException, IOException {
		GridFSDBFile gridFsFile = instrumentService.sharedretrieveLargeFile(fileid);

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType(gridFsFile.getContentType()));
		header.setContentLength(gridFsFile.getLength());
		header.set("Content-Disposition", "attachment; filename=gg.pdf");

		return new ResponseEntity<>(new InputStreamResource(gridFsFile.getInputStream()), header, HttpStatus.OK);
	}

	@RequestMapping(value = "Sharedcloudattachment/{fileid}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> Shareddownloadlargecloudattachment(@PathVariable String fileid)
			throws IllegalStateException, IOException {

		HttpHeaders header = new HttpHeaders();
		header.set("Content-Disposition", "attachment; filename=gg.pdf");
		return new ResponseEntity<>(new InputStreamResource(instrumentService.sharedretrieveColudLargeFile(fileid)),
				header, HttpStatus.OK);
	}

	// cloud
	@RequestMapping(path = "/downloadNonCloud/{param}/{fileid}/{tenant}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> download(@PathVariable String param, @PathVariable String fileid,
			@PathVariable String tenant) throws IOException {

		return instrumentService.downloadattachmentsNonCloud(param, fileid);
	}
	
	@RequestMapping(path = "/downloadparserNonCloud/{param}/{fileid}/{tenant}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> downloadparser(@PathVariable String param, @PathVariable String fileid,
			@PathVariable String tenant) throws IOException {

		return instrumentService.downloadparserattachmentsNonCloud(param, fileid);
	}

	// normal
	@RequestMapping(path = "/download/{param}/{fileid}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> downloadNonCloud(@PathVariable String param, @PathVariable String fileid)
			throws IOException {

		return instrumentService.downloadattachments(param, fileid);
	}
	
	@RequestMapping(path = "/downloadparser/{param}/{fileid}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> downloadparserNonCloud(@PathVariable String param, @PathVariable String fileid)
			throws IOException {

		return instrumentService.downloadelnparserattachments(param, fileid);
	}

	@RequestMapping("/GetOrderResourcesQuantitylst")
	public List<LsOrderSampleUpdate> GetOrderResourcesQuantitylst(@RequestBody LsOrderSampleUpdate objorder)throws Exception {
		return instrumentService.GetOrderResourcesQuantitylst(objorder);
	}

	@RequestMapping("/SaveOrderResourcesQuantity")
	public Map<String, Object> SaveOrderResourcesQuantity(@RequestBody Map<String, Object> argobj)throws Exception {
		return instrumentService.SaveOrderResourcesQuantity(argobj);
	}

	@RequestMapping("/GetEditedOrderResources")
	public List<Lsrepositoriesdata> GetEditedOrderResources(@RequestBody Lsrepositoriesdata objorder)throws Exception {
		return instrumentService.GetEditedOrderResources(objorder);
	}

	@PostMapping("/getmetatagdata")
	public String getmetatagdata(@RequestParam("order") Long batchcode, @RequestParam("indexrow") Integer indexrow,
			@RequestParam("cellindex") Integer cellindex, @RequestParam("sheetval") Integer sheetval,
			@RequestParam("tagdata") String tagdata, @RequestParam("tagvalue") String tagvalue,
			@RequestParam("samplevalue") String samplevalue, @RequestParam("sampledetails") String sampledetails,
			@RequestParam("lssamplefile") Integer lssamplefile, @RequestParam("multitenant") Integer multitenant)throws Exception {
		String jsonString = "[{\"1e89782a-1357-4108-928c-4c05e2731397-T2\":{\"ParsedData\":{\"MultiFileds\":{\"MultiParserFields\":{\"MultiParsedFields_0\":[{\"SampleID\":\"Baseline\",\"Value1\":\"0.005\",\"Value2\":\"0.000\",\"Value3\":\"0.000\"},{\"SampleID\":\"Primer\",\"Value1\":\"0.502\",\"Value2\":\"0.982\",\"Value3\":\"1.004\"},{\"SampleID\":\"Cal.\",\"Value1\":\"0.506\",\"Value2\":\"0.996\",\"Value3\":\"0.996\"},{\"SampleID\":\"Cal.\",\"Value1\":\"0.396\",\"Value2\":\"0.504\",\"Value3\":\"0.508\"},{\"SampleID\":\"Cal.\",\"Value1\":\"0.297\",\"Value2\":\"0.207\",\"Value3\":\"0.200\"},{\"SampleID\":\"Cal.\",\"Value1\":\"0.196\",\"Value2\":\"0.099\",\"Value3\":\"0.095\"},{\"SampleID\":\"0.257\",\"Value1\":\"0.399\",\"Value2\":\"0.402\",\"Value3\":\"\"},{\"SampleID\":\"0.252\",\"Value1\":\"0.400\",\"Value2\":\"0.399\",\"Value3\":\"\"},{\"SampleID\":\"Final\",\"Value1\":\"Base\",\"Value2\":\"0.005\",\"Value3\":\"0.000\"}]},\"MultiParsedFieldsTableInfo\":{\"MultiParsedFieldsTableInfo_0\":[{\"sFieldName\":\"Method1\",\"sFieldValue\":\"MBAS\"},{\"sFieldName\":\"Method2\",\"sFieldValue\":\"phenol\"},{\"sFieldName\":\"Method3\",\"sFieldValue\":\"CN\"}]}},\"SingleFields\":[{\"FieldName\":\"Method1\",\"FieldValue\":\"MBAS\"},{\"FieldName\":\"Method2\",\"FieldValue\":\"phenol\"},{\"FieldName\":\"Method3\",\"FieldValue\":\"CN\"}]},\"FileMetaTags\":[{\"Property\":\"Name\",\"Values\":\"200701DR1-RUN-02\"},{\"Property\":\"Size\",\"Values\":\"14.1 KB\"},{\"Property\":\"Itemtype\",\"Values\":\"Adobe Acrobat Document\"},{\"Property\":\"Date modified\",\"Values\":\"18-02-2021 18:08\"},{\"Property\":\"Date created\",\"Values\":\"19-02-2021 17:36\"},{\"Property\":\"Date accessed\",\"Values\":\"19-02-2021 17:36\"},{\"Property\":\"Attributes\",\"Values\":\"A\"},{\"Property\":\"Perceived type\",\"Values\":\"Unspecified\"},{\"Property\":\"Owner\",\"Values\":\"AGL66\\\\Pasupathi\"},{\"Property\":\"Kind\",\"Values\":\"Document\"},{\"Property\":\"Rating\",\"Values\":\"Unrated\"}],\"BatchID\":\"001\",\"FileLink\":\"http://AGL66:8081/SDMS_Web/Login.html?GUID=1e89782a-1357-4108-928c-4c05e2731397&TaskID=T2\",\"TransferID\":\"BF204D77-F1D3-49E7-883B-61611C5A9F31\",\"OrderID\":\"20210218124411210\",\"Tags\":{\"UnMappedTemplateTags\":[],\"MappedTemplateTags\":{\"QC\":[{\"TagName\":\"Sample\",\"TagValue\":\"Pantoprazole tablets IP\",\"CreatedBy\":\"Administrator\",\"CreatedOn\":\"2021-02-19 12:06:15\"},{\"TagName\":\"Test\",\"TagValue\":\"Dissolution\",\"CreatedBy\":\"Administrator\",\"CreatedOn\":\"2021-02-19 12:06:15\"}]}}}}]";

		jsonString = instrumentService.getsampledata(batchcode, indexrow, cellindex, sheetval, tagdata, tagvalue,
				samplevalue, sampledetails, lssamplefile, multitenant);

		LSsamplefile LSsamplefile = lssamplefileRepository.findByfilesamplecode(lssamplefile);
		try {
			// JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray("sheets");

			jsonArray.forEach(item -> {
				JSONObject obj = (JSONObject) item;
				JSONArray jsonrowsArray = obj.getJSONArray("rows");
				jsonrowsArray.forEach(rowitem -> {
					JSONObject rowobj = (JSONObject) rowitem;
					if (rowobj.getInt("index") == indexrow) {
						JSONArray jsoncellsArray = rowobj.getJSONArray("cells");
						jsoncellsArray.forEach(cellitem -> {
							JSONObject cellobj = (JSONObject) cellitem;
							if (cellobj.getInt("index") == cellindex) {
								cellobj.put("tag", tagvalue);
								cellobj.put("value", sampledetails);
							}
						});
					}

				});
			});

			jsonObject.put("sheets", jsonArray);

			jsonString = jsonObject.toString();

			instrumentService.updateordercontent(jsonString, LSsamplefile, multitenant);
			// JSONObject addedObj = (JSONObject) jsonObject.get("Added");

			// JSONArray jsonArray = new JSONArray(jsonString);
			System.out.println("\n\njsonArray: " + jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonString;
	}

	@PostMapping("/Getuserworkflow")
	public List<Integer> Getuserworkflow(@RequestBody LSusergroup lsusergroup)throws Exception {
		return instrumentService.Getuserworkflow(lsusergroup);
	}

	@PostMapping("/Getuserprojects")
	public Map<String, Object> Getuserprojects(@RequestBody LSuserMaster objuser)throws Exception {
		return instrumentService.Getuserprojects(objuser);
	}

	@PostMapping("/Getinitialorders")
	public Map<String, Object> Getinitialorders(@RequestBody LSlogilablimsorderdetail objorder)throws Exception {
		return instrumentService.Getinitialorders(objorder);
	}

	@PostMapping("/Getremainingorders")
	public List<Logilabordermaster> Getremainingorders(@RequestBody LSlogilablimsorderdetail objorder)throws Exception {
		return instrumentService.Getremainingorders(objorder);
	}

	@PostMapping("/uploadsheetimages")
	public Map<String, Object> uploadsheetimages(@RequestParam("file") MultipartFile file,
			@RequestParam("originurl") String originurl, @RequestParam("username") String username,
			@RequestParam("sitecode") String sitecode)throws Exception {
		return instrumentService.uploadsheetimages(file, originurl, username, sitecode);
	}

	@RequestMapping(value = "downloadsheetimages/{fileid}/{tenant}/{filename}/{extension}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadsheetimages(@PathVariable String fileid,
			@PathVariable String tenant, @PathVariable String filename, @PathVariable String extension)
			throws IllegalStateException, IOException {

		ByteArrayInputStream bis = instrumentService.downloadsheetimages(fileid, tenant);

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType("image/png"));
		header.set("Content-Disposition", "attachment; filename=" + filename + "." + extension);

		return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);

	}

	@RequestMapping(value = "downloadsheetimagestemp/{fileid}/{tenant}/{filename}/{extension}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadsheetimagestemp(@PathVariable String fileid,
			@PathVariable String tenant, @PathVariable String filename, @PathVariable String extension)
			throws IllegalStateException, IOException {

		ByteArrayInputStream bis = instrumentService.downloadsheetimagestemp(fileid, tenant);

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType("image/png"));
		header.set("Content-Disposition", "attachment; filename=" + filename + "." + extension);

		return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);

	}

	@PostMapping("/removesheetimage")
	public Response removesheetimage(@RequestBody Map<String, String> body)throws Exception {
		return instrumentService.removesheetimage(body);
	}

	@PostMapping("/updatesheetimagesforversion")
	public boolean updatesheetimagesforversion(@RequestBody List<Map<String, String>> lstfiles)throws Exception {
		return instrumentService.updatesheetimagesforversion(lstfiles);
	}

	@PostMapping("/deletesheetimagesforversion")
	public boolean deletesheetimagesforversion(@RequestBody List<Map<String, String>> lstfiles)throws Exception {
		return instrumentService.deletesheetimagesforversion(lstfiles);
	}

	@PostMapping("/uploadsheetimagesSql")
	public Map<String, Object> uploadsheetimagesSql(@RequestParam("file") MultipartFile file,
			@RequestParam("originurl") String originurl, @RequestParam("username") String username,
			@RequestParam("sitecode") String sitecode) throws IOException {
		return instrumentService.uploadsheetimagesSql(file, originurl, username, sitecode);
	}

	@RequestMapping(value = "downloadsheetimagessql/{fileid}/{filename}/{extension}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadsheetimagessql(@PathVariable String fileid,
			@PathVariable String filename, @PathVariable String extension) throws IllegalStateException, IOException {

		Fileimages objprofile = instrumentService.downloadsheetimagessql(fileid);

		byte[] data = null;

		if (objprofile != null) {
			data = objprofile.getFile().getData();
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("image/png"));
			header.set("Content-Disposition", "attachment; filename=" + filename + "." + extension);

			return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);
		} else {

			return null;
		}

	}

	@RequestMapping(value = "downloadsheetimagestempsql/{fileid}/{filename}/{extension}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadsheetimagestempsql(@PathVariable String fileid,
			@PathVariable String filename, @PathVariable String extension) throws IllegalStateException, IOException {

		Fileimagestemp objprofile = instrumentService.downloadsheetimagestempsql(fileid);

		byte[] data = null;

		if (objprofile != null) {
			data = objprofile.getFile().getData();
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("image/png"));
			header.set("Content-Disposition", "attachment; filename=" + filename + "." + extension);

			return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);
		} else {

			return null;
		}

	}

	@PostMapping("/updatesheetimagesforversionSql")
	public boolean updatesheetimagesforversionSql(@RequestBody List<Map<String, String>> lstfiles) throws IOException {
		return instrumentService.updatesheetimagesforversionSql(lstfiles);
	}

	@PostMapping("/deletesheetimagesforversionSql")
	public boolean deletesheetimagesforversionSql(@RequestBody List<Map<String, String>> lstfiles)throws Exception {
		return instrumentService.deletesheetimagesforversionSql(lstfiles);
	}
	
	@PostMapping("/UploadLimsFile")
	public Map<String, Object> UploadLimsFile(@RequestParam("file") MultipartFile file,
			@RequestParam("order") Long batchcode, @RequestParam("filename") String filename) throws IOException {
		return instrumentService.UploadLimsFile(file, batchcode, filename);
	}
	
	@PostMapping("/downloadSheetFromELN")
	public LsSheetorderlimsrefrence downloadSheetFromELN(@RequestBody LsSheetorderlimsrefrence objattachments)
			throws IllegalStateException, IOException {
		return instrumentService.downloadSheetFromELN(objattachments);
	}
	
	@PostMapping("/GetLimsorderid")
	public Map<String, Object> GetLimsorderid(@RequestBody String orderid)throws Exception {
		return instrumentService.GetLimsorderid(orderid);
	}
	
	@PostMapping("/GetorderforlinkLIMS")
	public Map<String, Object> GetorderforlinkLIMS(@RequestBody LSlogilablimsorderdetail objorder)throws Exception {
		return instrumentService.GetorderforlinkLIMS(objorder);
	}
	
	@PostMapping("/UploadLimsResultFile")
	public Map<String, Object> UploadLimsResultFile(@RequestParam("file") MultipartFile file,
			@RequestParam("order") Long batchcode, @RequestParam("filename") String filename) throws IOException {
		return instrumentService.UploadLimsResultFile(file, batchcode, filename);
	}
	
	@RequestMapping("/Insertdirectory")
	public LSSheetOrderStructure Insertdirectory(@RequestBody LSSheetOrderStructure objdir)throws Exception {
		return instrumentService.Insertdirectory(objdir);
	}
	
	@RequestMapping("/Insertnewdirectory")
	public LSSheetOrderStructure Insertnewdirectory(@RequestBody LSSheetOrderStructure objdir)throws Exception {
		return instrumentService.Insertnewdirectory(objdir);
	}

	@RequestMapping("/Getfoldersfororders")
	public List<LSSheetOrderStructure> Getfoldersfororders(@RequestBody LSSheetOrderStructure objdir)throws Exception {
		return instrumentService.Getfoldersfororders(objdir);
	}
	
	@RequestMapping("/UpdateFolderfororder")
	public LSlogilablimsorderdetail UpdateFolderfororder(@RequestBody LSlogilablimsorderdetail order)throws Exception
	{
		return instrumentService.UpdateFolderfororder(order);
	}
	
	@RequestMapping("/Getordersondirectory")
	public List<Logilaborders> Getordersondirectory(@RequestBody LSSheetOrderStructure objdir)throws Exception
	{
		return instrumentService.Getordersondirectory(objdir);
	}
	
	@RequestMapping("/UpdateFolderfororders")
	public List<LSlogilablimsorderdetail> UpdateFolderfororders(@RequestBody LSlogilablimsorderdetail[] order)throws Exception
	{
		return instrumentService.UpdateFolderfororders(order);
	}
	
	@RequestMapping("/Deletedirectories")
	public List<LSSheetOrderStructure> Deletedirectories(@RequestBody LSSheetOrderStructure[] directories)throws Exception
	{
		return instrumentService.Deletedirectories(directories);
	}
	
	@RequestMapping("/getMoveDirectory")
	public LSSheetOrderStructure getMoveDirectory(@RequestBody LSSheetOrderStructure objdir)throws Exception {
		return instrumentService.getMoveDirectory(objdir);
	}
	
	@RequestMapping("/Movedirectory")
	public LSSheetOrderStructure Movedirectory(@RequestBody LSSheetOrderStructure directory)throws Exception
	{
		return instrumentService.Movedirectory(directory);
	}
	
	@PostMapping("/getlsorderfileversion")
	public List<LSsamplefileversion> getlsorderfileversion(@RequestBody LSsamplefile objfile)throws Exception {
		return instrumentService.getlsorderfileversion(objfile);
	}
	
	@RequestMapping("/GetAssignedtoUserorders")
	public List<LSlogilablimsorderdetail> GetAssignedtoUserorders(@RequestBody LSlogilablimsorderdetail order)throws Exception
	{
		return instrumentService.GetAssignedtoUserorders(order);
	}
	
	@PostMapping("/getLockedOrders")
	private List<LSlogilablimsorderdetail> GetLockedOrders(@RequestBody LSlogilablimsorderdetail objorder) {
		return instrumentService.GetLockedOrders(objorder);
	}
	
	@PostMapping("/unLockedOrders")
	private Response UnLockOrders(@RequestBody LSlogilablimsorderdetail[] lstOrder) {
		return instrumentService.UnLockOrders(lstOrder);
	}
	
	@RequestMapping(value = "/GetSheetorderversions")
	public Map<String, Object> GetSheetorderversions(@RequestBody Map<String, Object> objMap) throws Exception {

		return instrumentService.GetSheetorderversions(objMap);
	}
}
