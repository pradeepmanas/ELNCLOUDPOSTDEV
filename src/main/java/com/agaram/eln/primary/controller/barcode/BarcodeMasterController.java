package com.agaram.eln.primary.controller.barcode;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.print.PrintException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.agaram.eln.primary.model.barcode.BarcodeMaster;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LsfilemapBarcode;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.service.barcode.BarcodeMasterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/Barcode", method = RequestMethod.POST)
public class BarcodeMasterController {
	@Autowired
	private BarcodeMasterService barcodemasterservice;

	@PostMapping("/InsertBarcode")
	public ResponseEntity<Object> InsertBarcode(MultipartHttpServletRequest request)
			throws JsonMappingException, JsonProcessingException, ParseException {
		return barcodemasterservice.InsertBarcode(request);
	}

	@PostMapping("/GetBarcodemaster")
	public List<BarcodeMaster> GetBarcodemaster(@RequestBody LoggedUser objuser) {
		return barcodemasterservice.GetBarcodemaster(objuser);
	}

	@RequestMapping(value = "/Getbarcodefileoncode/{barcode}/{ismultitenant}/{tenant}/{screen}/{primarykey}/{path}/{username}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> Getbarcodefileoncode(@PathVariable String barcode,
			@PathVariable String ismultitenant, @PathVariable String tenant, @PathVariable String screen,
			@PathVariable String primarykey, @PathVariable String path, @PathVariable String username)
			throws ParseException, IOException {
		return barcodemasterservice.getbarcodefileoncode(barcode, ismultitenant, tenant, screen, primarykey, path,
				username);
	}

	@PostMapping(value = "/getbarcodeContent")
	public ResponseEntity<String> getbarcodeContent(@RequestBody BarcodeMaster Obj) throws ParseException, IOException {
		return barcodemasterservice.getbarcodeContent(Obj.getBarcodeno(), Obj.getIsmultitenant(),
				Obj.getCommonstring());
	}

//	@PostMapping("/UpdateBarcode")
//	public BarcodeMaster UpdateBarcode(MultipartHttpServletRequest request)
//		throws ParseException, IOException {
//	//	throws JsonMappingException, JsonProcessingException, ParseException {
//		return barcodemasterservice.UpdateBarcode(request);
//	}

	@PostMapping("/RetiredBarcode")
	public BarcodeMaster RetiredBarcode(@RequestBody BarcodeMaster objClass) throws Exception {
		return barcodemasterservice.RetiredBarcode(objClass);
	}

	@PostMapping("/printBarcode")
	public Map<String, Object> printBarcode(@RequestBody Map<String, Object> inputMap)
			throws NumberFormatException, IOException, ParseException, PrintException {
		return barcodemasterservice.printBarcode(inputMap);
	}

	@PostMapping("/updateBarcodeMaster")
	public ResponseEntity<Object> updateBarcodeMaster(@RequestBody Map<String, Object> inputMap) throws Exception {

		ObjectMapper objMapper = new ObjectMapper();
		BarcodeMaster objBarcode = objMapper.convertValue(inputMap.get("Barcode"), new TypeReference<BarcodeMaster>() {
		});

		return barcodemasterservice.updateBarcodeMaster(objBarcode);
	}

	@PostMapping("/GetBarcodemasterOnScreenbased")
	public List<BarcodeMaster> GetBarcodemasterOnScreenbased(@RequestBody BarcodeMaster objuser) {
		return barcodemasterservice.GetBarcodemasterOnScreenbased(objuser);
	}

	@PostMapping("/getmappedbarcode")
	public List<LsfilemapBarcode> getmappedbarcode(@RequestBody LSfile objuser) {
		return barcodemasterservice.getmappedbarcode(objuser);
	}

	@PostMapping("/onupdateSheetmapbarcode")
	public List<LsfilemapBarcode> onupdateSheetmapbarcode(@RequestBody LsfilemapBarcode[] objOrder) {
		return barcodemasterservice.onupdateSheetmapbarcode(objOrder);
	}

	@PostMapping("/getmappedbarcodeOnsheetorder")
	public Map<String, Object> getmappedbarcodeOnsheetorder(@RequestBody LSfile objuser) {
		return barcodemasterservice.getmappedbarcodeOnsheetorder(objuser);
	}

	@RequestMapping(value = "/GetbarcodefilecodeonOrderscreen", method = RequestMethod.POST)
	public ResponseEntity<InputStreamResource> GetbarcodefilecodeonOrderscreen(
	    @RequestBody Map<String, Object> requestBody) throws ParseException, IOException {
	    
	    String barcode =requestBody.get("barcode").toString();
	    String ismultitenant =requestBody.get("ismultitenant").toString();
	    String tenant =  requestBody.get("tenant").toString();
	    String screen = requestBody.get("screen").toString();
	    String username =requestBody.get("username").toString();
	    List<Map<String, Object>> barcodedata = (List<Map<String, Object>>) requestBody.get("barcodedata");

	    return barcodemasterservice.GetbarcodefilecodeonOrderscreen(barcode, ismultitenant, tenant, screen, username, barcodedata);
	}
	
	@RequestMapping(value = "/PrintBarcodeorders", method = RequestMethod.POST)
	public Map<String, Object> PrintBarcodeorders(
			@RequestBody Map<String, Object> requestBody) throws ParseException, IOException {
		
		String barcode =requestBody.get("barcode").toString();
		String ismultitenant =requestBody.get("ismultitenant").toString();
		String tenant =  requestBody.get("tenant").toString();
		String screen = requestBody.get("screen").toString();
		String username =requestBody.get("username").toString();
		List<Map<String, Object>> barcodedata = (List<Map<String, Object>>) requestBody.get("barcodedata");
		
		return barcodemasterservice.PrintBarcodeorders(barcode, ismultitenant, tenant, screen, username, barcodedata);
	}

}
