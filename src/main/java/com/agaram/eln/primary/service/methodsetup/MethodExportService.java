package com.agaram.eln.primary.service.methodsetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.methodsetup.ParserBlock;
import com.agaram.eln.primary.model.methodsetup.ParserField;
import com.agaram.eln.primary.repository.instrumentsetup.InstCategoryRepository;
import com.agaram.eln.primary.repository.methodsetup.DelimiterRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodDelimiterRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserBlockRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserFieldRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;



@Service
public class MethodExportService {

	@Autowired
	MethodRepository methodRepo;

	@Autowired
	LSuserMasterRepository usersRepo;
	
	@Autowired
	InstCategoryRepository instCategoryRepo;
	
	@Autowired
	ParserBlockRepository parserBlockRepo;
	
	@Autowired
	private SampleTextSplitService textSplitService;
	
	@Autowired
	private SampleLineSplitService lineSplitService;
	
	@Autowired
	private SampleExtractService extractService;
	
	@Autowired
	ParserFieldRepository parserFieldRepo;
	
	@Autowired
	MethodDelimiterRepository methodDelimiterRepo;
	
	@Autowired
	DelimiterRepository delimiterRepo;
	
	@Autowired
	private ParserTechniqueService parserTechniqueService;
	
	@Autowired
	private SubParserTechniqueService subParserTechniqueService;

	@Autowired
	private SubParserFieldService subParserFieldService;

	public ResponseEntity<Object> getAllByMethodKey(final Integer methodKey) throws Exception {
		
		final Map<String, Object> exportMethodMap = new HashMap<String, Object>();
//		Method Setup
		final Method method = methodRepo.findOne(methodKey);
		
		exportMethodMap.put("Method", method);
		
		List<ParserBlock> lstparserblock = new ArrayList<ParserBlock>();
		List<ParserField> lstparserfield = new ArrayList<ParserField>();
		
		if(method != null)
		{
			lstparserblock = parserBlockRepo.findByMethodAndStatus(method, 1);
			
			if(lstparserblock != null)
			{
				lstparserfield = parserFieldRepo.findByParserblockInAndStatus(lstparserblock, 1);
			}
		}
		
		exportMethodMap.put("ParserBlock", lstparserblock);
		exportMethodMap.put("ParserField", lstparserfield);
		exportMethodMap.put("MethodDelimiter", methodDelimiterRepo.findAll());
		exportMethodMap.put("Delimiter", delimiterRepo.findAll());
		
//		Sample Split
		exportMethodMap.put("SampleTextSplit", textSplitService.getSampleTextSplitByMethod(methodKey).getBody());
		exportMethodMap.put("SampleLineSplit", lineSplitService.getSampleLineSplitByMethod(methodKey).getBody());
		exportMethodMap.put("SampleExtract", extractService.getSampleExtractByMethod(methodKey).getBody());
		
//		Parser Setup
		exportMethodMap.put("ParserTechnique", parserTechniqueService.getParserTechniqueByMethodKey(methodKey).getBody());
		exportMethodMap.put("SubParserTechnique", subParserTechniqueService.getSubParserTechniqueByMethodKey(methodKey).getBody());
		exportMethodMap.put("SubParserField", subParserFieldService.getSubParserFieldByMethodKey(methodKey).getBody());
		
		return new ResponseEntity<Object>(exportMethodMap, HttpStatus.OK);
	}
	
}
