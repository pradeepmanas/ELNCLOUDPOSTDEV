package com.agaram.eln.primary.controller.limsintegaration;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.instrumentDetails.LsSheetorderlimsrefrence;
import com.agaram.eln.primary.model.sheetManipulation.LStestmaster;
import com.agaram.eln.primary.service.limsintegaration.limsintegarationservice;

@RestController
@RequestMapping(value = "/Lims", method = RequestMethod.POST)
public class limsintegarationcontroller {

	@Autowired
	private limsintegarationservice limsintegarationservice;
	
	@PostMapping("/getSheetsFromELN")
	public Map<String, Object> getSheets(@RequestBody LStestmaster objTest) {
		return limsintegarationservice.getSheets(objTest);
	}
	
	@PostMapping("/downloadSheetFromELN")
	public LsSheetorderlimsrefrence downloadSheetFromELN(@RequestBody LsSheetorderlimsrefrence objattachments)
			throws IllegalStateException, IOException {
		return limsintegarationservice.downloadSheetFromELN(objattachments);
	}
	
}