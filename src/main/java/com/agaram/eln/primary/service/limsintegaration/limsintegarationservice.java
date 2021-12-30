package com.agaram.eln.primary.service.limsintegaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.fetchmodel.gettemplate.Sheettemplateget;
import com.agaram.eln.primary.model.fileManipulation.SheetorderlimsRefrence;
import com.agaram.eln.primary.model.instrumentDetails.LsSheetorderlimsrefrence;
import com.agaram.eln.primary.model.sheetManipulation.LSfiletest;
import com.agaram.eln.primary.model.sheetManipulation.LStestmaster;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.repository.instrumentDetails.LsSheetorderlimsrefrenceRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfileRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfiletestRepository;
import com.agaram.eln.primary.service.fileManipulation.FileManipulationservice;

@Service
public class limsintegarationservice {

	@Autowired
	private LSfileRepository lsfileRepository;

	@Autowired
	private LSfiletestRepository LSfiletestRepository;
	
	@Autowired
	private LsSheetorderlimsrefrenceRepository LsSheetorderlimsrefrenceRepository;
	
	@Autowired
	private FileManipulationservice fileManipulationservice;

	public Map<String, Object> getSheets(LStestmaster objTest) {

		Map<String, Object> map = new HashMap<>();

		List<Sheettemplateget> lstsheets = new ArrayList<Sheettemplateget>();

		LSSiteMaster site = new LSSiteMaster();

		site.setSitecode(1);

		if (objTest.getNtestcode() == -1) {

			lstsheets = lsfileRepository.findByApprovedAndLssitemasterAndFilecodeGreaterThan(1, site, 1);

		} else {

			List<LSfiletest> lstTest = LSfiletestRepository.findByTestcodeAndTesttype(objTest.getNtestcode(), 0);

			if (lstTest.size() > 0) {

				lstsheets = lsfileRepository.findBylstestIn(lstTest);

			}
		}

		map.put("lstSheets", lstsheets);

		return map;

	}

	public LsSheetorderlimsrefrence downloadSheetFromELN(LsSheetorderlimsrefrence objattachments) {

		System.out.print("Sheet download lims call service " + objattachments);

		LsSheetorderlimsrefrence objSheet = LsSheetorderlimsrefrenceRepository
				.findFirst1BybatchcodeOrderByRefrencecodeDesc(objattachments.getBatchcode());
		
		if(objSheet != null) {
		
			SheetorderlimsRefrence objfile = fileManipulationservice.LimsretrieveELNsheet(objSheet);

			if (objfile != null) {
				objattachments.setFile(objfile.getFile());
			}
			
		}

		return objattachments;
	}
}