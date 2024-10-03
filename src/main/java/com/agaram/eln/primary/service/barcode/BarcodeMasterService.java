package com.agaram.eln.primary.service.barcode;

import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Collections;

import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.supercsv.cellprocessor.ParseInt;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.barcode.BarcodeMaster;
import com.agaram.eln.primary.model.barcode.Printer;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.material.ElnmaterialInventory;
import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LsfilemapBarcode;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.repository.barcode.BarcodeMasterRepository;
import com.agaram.eln.primary.repository.material.ElnmaterialInventoryRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LsfilemapBarcodeRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.fileManipulation.FileManipulationservice;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.gridfs.GridFSDBFile;

@Service
public class BarcodeMasterService {

	@Autowired
	private BarcodeMasterRepository barcodemasterrepository;

	@Autowired
	private FileManipulationservice fileManipulationservice;

	@Autowired
	private CloudFileManipulationservice cloudFileManipulationservice;

	@Autowired
	private ElnmaterialInventoryRepository elnmaterialInventoryReppository;

	@Autowired
	private LsfilemapBarcodeRepository lsfilemapBarcodeRepository;

	public ResponseEntity<Object> InsertBarcode(MultipartHttpServletRequest request)
			throws JsonMappingException, JsonProcessingException, ParseException {
		Map<String, Object> returnMap = new HashMap<>();
		final ObjectMapper mapper = new ObjectMapper();
		Response response = new Response();
		try {
			final BarcodeMaster barcode = mapper.readValue(request.getParameter("barcode"), BarcodeMaster.class);
			List<BarcodeMaster> Existbarcode = new ArrayList<>();
			if (barcode.getBarcodeno() == null) {
				Existbarcode = barcodemasterrepository.findByBarcodenameAndLssitemaster(barcode.getBarcodename(),
						barcode.getLssitemaster());
			} else {
				Existbarcode = barcodemasterrepository.findByBarcodenameAndLssitemasterAndBarcodenoNot(
						barcode.getBarcodename(), barcode.getLssitemaster(), barcode.getBarcodeno());
			}
			if (Existbarcode.isEmpty()) {
				Integer isMultitenant = Integer.parseInt(request.getParameter("isMultitenant"));
				String filename = request.getParameter("filename");
				List<MultipartFile> file = request.getFiles("file");
				String UUId = "";
				Date currentdate = commonfunction.getCurrentUtcTime();
				barcode.setCreatedon(currentdate);
				if (barcode.getBarcodeno() != null) {
					BarcodeMaster barcodeobj = barcodemasterrepository.findByBarcodeno(barcode.getBarcodeno());
					if (!barcodeobj.getBarcodefilename().equals(filename) && !file.isEmpty()
							&& !file.get(0).isEmpty()) {
						UUId = processFileUpload(file.get(0), barcodeobj.getBarcodefileid(), isMultitenant);
					}
					barcodeobj.setBarcodename(barcode.getBarcodename());
					barcodeobj.setBarcodefilename(filename);
					barcodeobj.setBarcodefileid(barcodeobj.getBarcodefileid());
					barcodemasterrepository.save(barcodeobj);

					response.setStatus(true);
					barcodeobj.setResponse(response);
					returnMap.put("Barcode", barcodeobj);

				} else {
					if (!file.isEmpty() && !file.get(0).isEmpty()) {
						UUId = processFileUpload(file.get(0), UUID.randomUUID().toString(), isMultitenant);
						barcode.setBarcodefilename(filename);
						barcode.setBarcodefileid(UUId);
					}
					barcodemasterrepository.save(barcode);
					response.setStatus(true);
					barcode.setResponse(response);
					returnMap.put("Barcode", barcode);
				}

			} else {
				response.setStatus(false);
				response.setInformation("Name Already Exist");
				barcode.setResponse(response);
				returnMap.put("Barcode", barcode);
			}

		} catch (JsonParseException | JsonMappingException e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setInformation("Error parsing JSON");
			returnMap.put("Error", "JSON parsing error: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setInformation("IO Exception");
			returnMap.put("Error", "IO error: " + e.getMessage());
		}

		return new ResponseEntity<>(returnMap, HttpStatus.OK);
	}

	private String processFileUpload(MultipartFile file, String existingUUID, Integer isMultitenant)
			throws IOException {
		String UUId;
		if (isMultitenant == 1 || isMultitenant == 2) {
			UUId = cloudFileManipulationservice.storecloudfilesreturnwithpreUUID(file, "barcodefiles", existingUUID,
					isMultitenant);
		} else {
			UUId = fileManipulationservice.storeLargeattachment(file.getOriginalFilename(), file, existingUUID);
		}
		return UUId;
	}

	public List<BarcodeMaster> GetBarcodemaster(LoggedUser objuser) {
		List<BarcodeMaster> lstbarcode = new ArrayList<BarcodeMaster>();
		Integer sitecode=Integer.parseInt(objuser.getsSiteCode());
		if(objuser.getsUsername().equals("Administrator") && sitecode== 0) {
			lstbarcode = barcodemasterrepository.findByOrderByBarcodenoDesc();
		}else {
			LSSiteMaster site=new LSSiteMaster();
		
			site.setSitecode(sitecode);
			lstbarcode = barcodemasterrepository.findByLssitemasterOrderByBarcodenoDesc(site);
		}
		
		return lstbarcode;
	}

	public ResponseEntity<InputStreamResource> getbarcodefileoncode(String barcodeid, String ismultitenant,
			String tenant, String screen, String primarykey, String path, String username)
			throws JsonParseException, JsonMappingException, IOException, NumberFormatException, ParseException {

		BarcodeMaster barcode = barcodemasterrepository.findOne(Integer.parseInt(barcodeid));
		HttpHeaders header = new HttpHeaders();
		InputStreamResource resource = null;
		InputStream stream = null;
		if (Integer.parseInt(ismultitenant) == 1 || Integer.parseInt(ismultitenant) == 2) {
			stream = cloudFileManipulationservice.retrieveCloudFile(barcode.getBarcodefileid(),
					tenant + "barcodefiles");
		} else {
			GridFSDBFile gridFsFile = null;

			try {
				gridFsFile = retrieveLargeFile(barcode.getBarcodefileid());
				stream = gridFsFile.getInputStream();
			} catch (IllegalStateException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		String data = readFromInputStream(stream);

		switch (screen) {
		case "1":
			data = updatematerialcontent(data, Integer.parseInt(primarykey), path, username);
			break;
		}

//		data = data.replace("$BarcodeId$", barcode.getBarcodename());
		RestTemplate restTemplate = new RestTemplate();
		String uri = "http://api.labelary.com/v1/printers/8dpmm/labels/4x6/0/" + data;
		HttpHeaders headers = new HttpHeaders();

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<byte[]> ctc = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
		byte[] barcodearray = ctc.getBody();

		int size = barcodearray.length;
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(barcodearray);
			resource = new InputStreamResource(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception ex) {

			}
		}

		header.set("Content-Disposition", "attachment; filename=" + "label.png");
		header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		header.setContentLength(size);

		return new ResponseEntity<>(resource, header, HttpStatus.OK);
	}

	public GridFSDBFile retrieveLargeFile(String fileid) throws IllegalStateException, IOException {
		return fileManipulationservice.retrieveLargeFile(fileid);
	}

	private String UpdatecontentOnBarcodeusingOrders(String data, String username,
			List<Map<String, Object>> barcodedata) throws ParseException {
		Date currentdata = commonfunction.getCurrentUtcTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd ");
		for (Map<String, Object> map : barcodedata) {
			for (String key : map.keySet()) {
				data = data.replace(key, map.get(key).toString());
			}
		}
		data = data.replace("$generatedby$", username).replace("$generateddate$", dateFormat.format(currentdata));
		return data;

	}

	private String updatematerialcontent(String data, Integer materialcode, String path, String username)
			throws ParseException {
		ElnmaterialInventory inventory = elnmaterialInventoryReppository.findOne(materialcode);
		if (inventory != null) {
			Date currentdata = commonfunction.getCurrentUtcTime();
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd ");
			data = data.replace("$materialid$", inventory.getSinventoryid())
					.replace("$materialname$", inventory.getMaterial().getSmaterialname())
					.replace("$storagepath$", path).replace("$batchno$", inventory.getSbatchno())
					.replace("$generatedby$", username).replace("$generateddate$", dateFormat.format(currentdata));
			MaterialCategory materialCategory = inventory.getMaterialcategory();
			if (materialCategory != null && "Cell Bank".equals(materialCategory.getSmaterialtypename())) {
				JsonParser parser = new JsonParser();
				JsonObject jsonObject = parser.parse(inventory.getJsondata()).getAsJsonObject();
				JsonObject materialJsonDataObject = parser.parse(inventory.getMaterial().getJsondata())
						.getAsJsonObject();
				JsonElement dynamicfields = materialJsonDataObject.get("dynamicfields");
				if (jsonObject.has("depositorName") && !jsonObject.get("depositorName").getAsString().isEmpty()) {
					data = data.replace("$depositor$", jsonObject.get("depositorName").getAsString());
				}
				if (dynamicfields.isJsonArray()) {
					JsonArray dynamicFieldsArray = dynamicfields.getAsJsonArray();

					for (JsonElement element : dynamicFieldsArray) {
						if (element.isJsonObject()) {
							JsonObject field = element.getAsJsonObject();
							int datatype = field.get("datatype").getAsInt();
							String fieldName = field.get("fieldname").getAsString();

							if (datatype == 4 && "Storage Condition".equalsIgnoreCase(fieldName)) {
								JsonElement valueElement = field.get("value");
								if (valueElement.isJsonObject()) {
									String storageCondition = valueElement.getAsJsonObject().get("value").getAsString();
									data = data.replace("$storagecondition$", storageCondition);
								}
							} else if (datatype == 1 && "Project id".equalsIgnoreCase(fieldName)) {
								String projectId = field.get("value").getAsString();
								data = data.replace("$projectid$", projectId);
							}
						}
					}
				}
				if (jsonObject.has("dateofarrival") && !jsonObject.get("dateofarrival").getAsString().isEmpty()) {
					data = data.replace("$freezingdate$", jsonObject.get("dateofarrival").getAsString());
				}

				System.out.println(data);
			}
		}
		return data;
	}

	private String readFromInputStream(InputStream inputStream) throws IOException {
		StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
		}
		return resultStringBuilder.toString();
	}

	public Map<String, Object> printBarcode(Map<String, Object> inputMap)
			throws IOException, NumberFormatException, ParseException, PrintException {
		Map<String, Object> returnmap = new HashMap<String, Object>();
		String sprintername = (String) inputMap.get("sprintername");
		Integer barcodeid = (Integer) inputMap.get("barcode");
		Integer ismultitenant = (Integer) inputMap.get("ismultitenant");
		String tenant = (String) inputMap.get("tenant");
		Integer screen = (Integer) inputMap.get("screen");
		Integer primarykey = (Integer) inputMap.get("primarykey");
		String path = (String) inputMap.get("path");
		String username = (String) inputMap.get("username");

		BarcodeMaster barcode = barcodemasterrepository.findOne(barcodeid);
		InputStream stream = null;
		if (ismultitenant == 1 || ismultitenant == 2) {
			stream = cloudFileManipulationservice.retrieveCloudFile(barcode.getBarcodefileid(),
					tenant + "barcodefiles");
		} else {
			GridFSDBFile gridFsFile = null;

			try {
				gridFsFile = retrieveLargeFile(barcode.getBarcodefileid());
				stream = gridFsFile.getInputStream();
			} catch (IllegalStateException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		String data = readFromInputStream(stream);

		switch (screen) {
		case 1:
			data = updatematerialcontent(data, primarykey, path, username);
			break;
		}

//		UUID objGUID = UUID.randomUUID();
//		String randomUUIDString = objGUID.toString();
//		File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + randomUUIDString +".prn"); 
//		FileWriter writer = new FileWriter(convFile);
//	    writer.write(data);
//	    writer.close();
//		FileInputStream psStream = new FileInputStream(convFile);
//        String printerPath = "";
//        DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
//        Doc myDoc = new SimpleDoc(psStream, psInFormat, null);
//        PrintServiceAttributeSet aset = new HashPrintServiceAttributeSet();
//        aset.add(new PrinterName(printerPath, null)); // Ensure correct printerPath is provided
//
//        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
//        for (PrintService printer : services) {
//            if (printer.getName().equalsIgnoreCase(sprintername)) {
//                DocPrintJob job = printer.createPrintJob();
//                job.print(myDoc, null);
//            }
//        }

		returnmap.put("data", data);

		return returnmap;
	}

	public List<Printer> getPrinter() {

		List<Printer> lstprinter = new ArrayList<>();
		Printer p1 = new Printer();

		PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
		PrintService[] services = PrinterJob.lookupPrintServices();
		String serviceName = "";
		if (defaultService != null) {
			serviceName = defaultService.getName();
			p1.setSprintername(serviceName);
			lstprinter.add(p1);
		}

		// NIBSCRT-2110
		for (PrintService printer : services) {
			if (serviceName != null && !serviceName.equals(printer.getName())) {
				Printer p = new Printer();
				p.setSprintername(printer.getName());
				lstprinter.add(p);
			}

		}

		return lstprinter;
	}

	public BarcodeMaster RetiredBarcode(BarcodeMaster objClass) {

		objClass.setStatus(-1);
		barcodemasterrepository.save(objClass);
		return objClass;
	}

	public BarcodeMaster getActiveBarcodeById(int barcodeno) {

		BarcodeMaster objBarcode = barcodemasterrepository.findByBarcodeno(barcodeno);

		return objBarcode;
	}

	public ResponseEntity<Object> updateBarcodeMaster(BarcodeMaster objBarcode) {
		BarcodeMaster objBarcode2 = barcodemasterrepository.findByBarcodeno(objBarcode.getBarcodeno());
		// final BarcodeMaster unit = getActiveBarcodeById(objBarcode.getBarcodeno());

		objBarcode2.setResponse(new Response());

//        if (unit == null) {
//        	objBarcode.getResponse().setStatus(false);
//        	objBarcode.getResponse().setInformation("IDS_ALREADYDELETED");
//			return new ResponseEntity<>(objBarcode, HttpStatus.OK);
//		}
//        else {

		// final BarcodeMaster unit1 =
		// barcodemasterrepository.findByBarcodenameIgnoreCaseAndStatus(objBarcode2.getBarcodename(),1);

//			if (unit1 == null || (unit1.getBarcodeno().equals(objBarcode.getBarcodeno()))) {

		if (objBarcode2 != null && objBarcode2.getBarcodeno() != null) {

			objBarcode2.setBarcodename(objBarcode.getBarcodename());
			barcodemasterrepository.save(objBarcode2);
			// objBarcode.getResponse().setStatus(true);
			// objBarcode.getResponse().setInformation("IDS_SUCCESS");
			return new ResponseEntity<>(objBarcode2, HttpStatus.OK);

		}
//			else {
//				objBarcode.getResponse().setStatus(false);
//				objBarcode.getResponse().setInformation("IDS_ALREADYEXIST");
//				return new ResponseEntity<>(objBarcode, HttpStatus.OK);
//			}
		return new ResponseEntity<>(objBarcode2, HttpStatus.OK);
	}

	public List<BarcodeMaster> GetBarcodemasterOnScreenbased(BarcodeMaster objuser) {

		List<BarcodeMaster> lstbarcode = new ArrayList<BarcodeMaster>();
		lstbarcode = barcodemasterrepository.findByLssitemasterAndScreenOrderByBarcodenoDesc(objuser.getLssitemaster(),
				objuser.getScreen());
		return lstbarcode;
	}

	public ResponseEntity<String> getbarcodeContent(Integer barcodeid, Integer ismultitenant, String tenant)
			throws JsonParseException, JsonMappingException, IOException, NumberFormatException, ParseException {

		BarcodeMaster barcode = barcodemasterrepository.findOne(barcodeid);
		HttpHeaders header = new HttpHeaders();
		InputStream stream = null;
		if (ismultitenant == 1 || ismultitenant == 2) {
			stream = cloudFileManipulationservice.retrieveCloudFile(barcode.getBarcodefileid(),
					tenant + "barcodefiles");
		} else {
			GridFSDBFile gridFsFile = null;

			try {
				gridFsFile = retrieveLargeFile(barcode.getBarcodefileid());
				stream = gridFsFile.getInputStream();
			} catch (IllegalStateException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		String data = readFromInputStream(stream);
		return new ResponseEntity<>(data, header, HttpStatus.OK);

	}

	public List<LsfilemapBarcode> getmappedbarcode(LSfile objuser) {

		List<LsfilemapBarcode> filemapBarcode = lsfilemapBarcodeRepository.findByFilecode(objuser.getFilecode());
		return filemapBarcode;
	}

	public List<LsfilemapBarcode> onupdateSheetmapbarcode(LsfilemapBarcode[] objOrder) {
		List<LsfilemapBarcode> LsfilemapBarcode = Arrays.asList(objOrder);
		if (!LsfilemapBarcode.isEmpty() && LsfilemapBarcode.get(0).getFilecode() != null) {
			lsfilemapBarcodeRepository.deleteByFilecode(LsfilemapBarcode.get(0).getFilecode());
		}
		lsfilemapBarcodeRepository.save(LsfilemapBarcode);
		return LsfilemapBarcode;
	}

	public Map<String, Object> getmappedbarcodeOnsheetorder(LSfile objuser) {
		Map<String, Object> rtn = new HashMap<>();
		List<LsfilemapBarcode> filemapBarcode = lsfilemapBarcodeRepository.findByFilecode(objuser.getFilecode());
		List<Integer> barcodes = filemapBarcode.stream().map(LsfilemapBarcode::getBarcodeno)
				.collect(Collectors.toList());
		List<BarcodeMaster> barcodesList = barcodemasterrepository.findByBarcodenoIn(barcodes);
		rtn.put("filemapBarcode", filemapBarcode);
		rtn.put("barcodesList", barcodesList);
		return rtn;
	}

	public ResponseEntity<InputStreamResource> GetbarcodefilecodeonOrderscreen(String barcodeid, String ismultitenant,
			String tenant, String screen, String username, List<Map<String, Object>> barcodedata)
			throws IOException, NumberFormatException, ParseException {

		BarcodeMaster barcode = barcodemasterrepository.findOne(Integer.parseInt(barcodeid));
		HttpHeaders header = new HttpHeaders();
		InputStreamResource resource = null;
		InputStream stream = null;
		if (Integer.parseInt(ismultitenant) == 1 || Integer.parseInt(ismultitenant) == 2) {
			stream = cloudFileManipulationservice.retrieveCloudFile(barcode.getBarcodefileid(),
					tenant + "barcodefiles");
		} else {
			GridFSDBFile gridFsFile = null;

			try {
				gridFsFile = retrieveLargeFile(barcode.getBarcodefileid());
				stream = gridFsFile.getInputStream();
			} catch (IllegalStateException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		String data = readFromInputStream(stream);

		switch (screen) {
		case "2":
			data = UpdatecontentOnBarcodeusingOrders(data, username, barcodedata);
			break;
		}

		RestTemplate restTemplate = new RestTemplate();
		String uri = "http://api.labelary.com/v1/printers/8dpmm/labels/4x6/0/" + data;
		HttpHeaders headers = new HttpHeaders();

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<byte[]> ctc = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
		byte[] barcodearray = ctc.getBody();

		int size = barcodearray.length;
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(barcodearray);
			resource = new InputStreamResource(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception ex) {

			}
		}

		header.set("Content-Disposition", "attachment; filename=" + "label.png");
		header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		header.setContentLength(size);

		return new ResponseEntity<>(resource, header, HttpStatus.OK);
	}

	public Map<String, Object> PrintBarcodeorders(String barcodeid, String ismultitenant, String tenant, String screen,
			String username, List<Map<String, Object>> barcodedata) throws IOException, ParseException {
		Map<String, Object> returnmap = new HashMap<String, Object>();
		BarcodeMaster barcode = barcodemasterrepository.findOne(Integer.parseInt(barcodeid));
		HttpHeaders header = new HttpHeaders();
		InputStreamResource resource = null;
		InputStream stream = null;
		if (Integer.parseInt(ismultitenant) == 1 || Integer.parseInt(ismultitenant) == 2) {
			stream = cloudFileManipulationservice.retrieveCloudFile(barcode.getBarcodefileid(),
					tenant + "barcodefiles");
		} else {
			GridFSDBFile gridFsFile = null;

			try {
				gridFsFile = retrieveLargeFile(barcode.getBarcodefileid());
				stream = gridFsFile.getInputStream();
			} catch (IllegalStateException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		String data = readFromInputStream(stream);

		switch (screen) {
		case "2":
			data = UpdatecontentOnBarcodeusingOrders(data, username, barcodedata);
			break;
		}
		returnmap.put("Data", data);
		return returnmap;
	}

}