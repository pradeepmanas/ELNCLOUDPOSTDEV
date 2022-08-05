package com.agaram.eln.primary.controller.limsintegaration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.instrumentDetails.LSlimsorder;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.instrumentDetails.LsResultlimsOrderrefrence;
import com.agaram.eln.primary.model.instrumentDetails.LsSheetorderlimsrefrence;
import com.agaram.eln.primary.model.sheetManipulation.LSfileparameter;
import com.agaram.eln.primary.model.sheetManipulation.LStestmaster;
import com.agaram.eln.primary.service.limsintegaration.limsintegarationservice;

@RestController
@RequestMapping(value = "/Lims", method = RequestMethod.POST)
public class limsintegarationcontroller {

	@Autowired
	private limsintegarationservice limsintegarationservice;
	
	@PostMapping("/getSheetsFromELN")
	public Map<String, Object> getSheets(@RequestBody LStestmaster objTest)throws Exception {
		return limsintegarationservice.getSheets(objTest);
	}
	
	@PostMapping("/downloadSheetFromELN")
	public LsSheetorderlimsrefrence downloadSheetFromELN(@RequestBody LsSheetorderlimsrefrence objattachments)
			throws IllegalStateException, IOException {
		return limsintegarationservice.downloadSheetFromELN(objattachments);
	}
	
	@PostMapping("/downloadResultFromELN")
	public LsResultlimsOrderrefrence downloadResultSheetFromELN(@RequestBody LsResultlimsOrderrefrence objattachments)
			throws IllegalStateException, IOException {
		return limsintegarationservice.downloadResultSheetFromELN(objattachments);
	}
	
	@PostMapping("/updateSheetsParameterForELN")
	public Boolean updateSheetsParameterForELN(@RequestBody List<LSfileparameter> objattachments)throws Exception {
		return limsintegarationservice.updateSheetsParameterForELN(objattachments);
	}
	
	@PostMapping("/getAttachmentsForLIMS")
	public List<LsOrderattachments> getAttachmentsForLIMS(@RequestBody LSlimsorder objOrder)throws Exception {
		return limsintegarationservice.getAttachmentsForLIMS(objOrder);
	}
}