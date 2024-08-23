package com.agaram.eln.primary.service.barcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.agaram.eln.primary.model.barcode.BarcodeMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.barcode.BarcodeMasterRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class BarcodeMasterService {
	
	@Autowired
	private BarcodeMasterRepository barcodemasterrepository;
	
	public ResponseEntity<Object> InsertBarcode(MultipartHttpServletRequest request)
			throws JsonMappingException, JsonProcessingException {
		Map<String, Object> returnMap = new HashMap();
		
		return new ResponseEntity<Object>(returnMap, HttpStatus.OK);
	}
	
	public List<BarcodeMaster> GetBarcodemaster(LSuserMaster objuser)
	{
		List<BarcodeMaster> lstbarcode = new ArrayList<BarcodeMaster>();
		lstbarcode = barcodemasterrepository.findAll();
		return lstbarcode;
	}
}
