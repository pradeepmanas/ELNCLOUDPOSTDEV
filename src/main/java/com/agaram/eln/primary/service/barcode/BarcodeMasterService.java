package com.agaram.eln.primary.service.barcode;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.barcode.BarcodeMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.barcode.BarcodeMasterRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.fileManipulation.FileManipulationservice;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BarcodeMasterService {
	
	@Autowired
	private BarcodeMasterRepository barcodemasterrepository;
	
	@Autowired
	private FileManipulationservice fileManipulationservice;
	
	@Autowired
	private CloudFileManipulationservice cloudFileManipulationservice;
	
	public ResponseEntity<Object> InsertBarcode(MultipartHttpServletRequest request)
			throws JsonMappingException, JsonProcessingException, ParseException {
		Map<String, Object> returnMap = new HashMap();
		final ObjectMapper mapper = new ObjectMapper();
		try {
			final BarcodeMaster barcode = mapper.readValue(request.getParameter("barcode"), BarcodeMaster.class);
			Integer isMultitenant = Integer.parseInt(request.getParameter("isMultitenant"));
			String filename = request.getParameter("filename");
			List<MultipartFile> file = request.getFiles("file");
			String UUId = "";
			Date currentdate = commonfunction.getCurrentUtcTime();
			barcode.setCreatedon(currentdate);
			if(file.size() > 0)
			{
				if(isMultitenant == 1 || isMultitenant==2)
				{
					UUID objGUID = UUID.randomUUID();
					String randomUUIDString = objGUID.toString();
					UUId = cloudFileManipulationservice.storecloudfilesreturnwithpreUUID(file.get(0), "barcodefiles", randomUUIDString,
							isMultitenant);
				}
				else
				{
					UUId = fileManipulationservice.storeLargeattachment(filename, file.get(0));
				}
				barcode.setBarcodefilename(filename);
				barcode.setBarcodefileid(UUId);
			}
			barcodemasterrepository.save(barcode);
			returnMap.put("Barcode", barcode);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ResponseEntity<Object>(returnMap, HttpStatus.OK);
	}
	
	public List<BarcodeMaster> GetBarcodemaster(LSuserMaster objuser)
	{
		List<BarcodeMaster> lstbarcode = new ArrayList<BarcodeMaster>();
		lstbarcode = barcodemasterrepository.findAll();
		return lstbarcode;
	}
}
