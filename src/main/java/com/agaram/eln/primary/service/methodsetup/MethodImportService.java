package com.agaram.eln.primary.service.methodsetup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.instrumentsetup.InstrumentCategory;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.methodsetup.Delimiter;
import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.methodsetup.MethodDelimiter;
import com.agaram.eln.primary.model.methodsetup.MethodVersion;
import com.agaram.eln.primary.model.methodsetup.ParserBlock;
import com.agaram.eln.primary.model.methodsetup.ParserField;
import com.agaram.eln.primary.model.methodsetup.ParserTechnique;
import com.agaram.eln.primary.model.methodsetup.SampleExtract;
import com.agaram.eln.primary.model.methodsetup.SampleLineSplit;
import com.agaram.eln.primary.model.methodsetup.SampleTextSplit;
import com.agaram.eln.primary.model.methodsetup.SubParserField;
import com.agaram.eln.primary.model.methodsetup.SubParserTechnique;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.instrumentsetup.InstCategoryRepository;
import com.agaram.eln.primary.repository.instrumentsetup.InstMasterRepository;
import com.agaram.eln.primary.repository.methodsetup.DelimiterRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodDelimiterRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodVersionRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserBlockRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserFieldRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserTechniqueRepository;
import com.agaram.eln.primary.repository.methodsetup.SampleExtractRepository;
import com.agaram.eln.primary.repository.methodsetup.SampleLineSplitRepository;
import com.agaram.eln.primary.repository.methodsetup.SampleTextSplitRepository;
import com.agaram.eln.primary.repository.methodsetup.SubParserFieldRepository;
import com.agaram.eln.primary.repository.methodsetup.SubParserTechniqueRepository;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unchecked")
@Service
public class MethodImportService {
	
	@Autowired
	LSSiteMasterRepository siteRepo;
	
	@Autowired
	LSuserMasterRepository usersRepo;
	
	@Autowired
	InstCategoryRepository instCategoryRepo;
	
	@Autowired
	InstMasterRepository instMasterRepo;
	
	@Autowired
	MethodRepository methodRepo;
	
	@Autowired
	ParserBlockRepository parserBlockRepo;
	
	@Autowired
	SampleTextSplitRepository textSplitRepo;
	
	@Autowired
	SampleLineSplitRepository lineSplitRepo;
	
	@Autowired
	SampleExtractRepository extractRepo;
	
	@Autowired
	ParserTechniqueRepository parserTechniqueRepo;

	@Autowired
	ParserFieldRepository parserFieldRepo;
	
	@Autowired
	MethodDelimiterRepository methodDelimiterRepo;
	
	@Autowired
	DelimiterRepository delimiterRepo;

	@Autowired
	SubParserTechniqueRepository subParserTechniqueRepo;
	
	@Autowired
	SubParserFieldRepository subParserFieldRepo;
	
	@Autowired
	MethodVersionRepository methversionRepo;
//	@Autowired
//	ReadWriteXML readWriteXML;
	
//	@Autowired
//	CfrTransactionService cfrTransService;
	
//	@Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.READ_UNCOMMITTED)

	public ResponseEntity<Object> importMethodByEntity(Map<String, Object> mapObject) throws Exception {
		
		final ObjectMapper mapper = new ObjectMapper();
		
		
		final List<Method> importedMethods = new ArrayList<>();
//		final boolean saveAuditTrial = false;
		
		final Map<String, Object> inputData = (Map<String, Object>) mapObject.get("inputData");
		
//		Site
		String siteJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(inputData.get("site"));
		final LSSiteMaster site =  mapper.readValue(siteJson, LSSiteMaster.class);
		
//		CreatedUser
		String usersJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(inputData.get("createdBy"));
		final LSuserMaster createdUser =  mapper.readValue(usersJson, LSuserMaster.class);
		
		List<Map<String, Object>> methodMap = (List<Map<String, Object>>) inputData.get("methodMap");
		
		//userdefinedinstdetails
		String instdetailsJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(inputData.get("instdetails"));
		final InstrumentMaster userdefinedinstdetails =  mapper.readValue(instdetailsJson, InstrumentMaster.class);
		
		//userdefinedmethodname
		String userdefmethodJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(inputData.get("method"));
		final Method userdefinedmethod =  mapper.readValue(userdefmethodJson, Method.class);
		
		for (Map<String, Object> item : methodMap) {
			
			String methodJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(item.get("Method"));
			final Method expMethod = mapper.readValue(methodJson, Method.class);
			
//			Instrument Category
			String instCategoryJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expMethod.getInstmaster().getInstcategory());
			final InstrumentCategory expInstCategory = mapper.readValue(instCategoryJson, InstrumentCategory.class);
			final InstrumentCategory impInstCategory = new InstrumentCategory(expInstCategory);
			final List<InstrumentCategory> instCategoryList = new ArrayList<>(1);
		
			final Optional<InstrumentCategory> instCategoryExist = instCategoryRepo.findByInstcatnameAndStatusAndLssitemaster(expInstCategory.getInstcatname(), 1,site);
            Date date = new Date();		
            if(!instCategoryExist.isPresent()) {
				impInstCategory.setInstcatkey(0);
				impInstCategory.setCreatedby(createdUser);
				impInstCategory.setCreateddate(date);
				instCategoryList.add(instCategoryRepo.save(impInstCategory));
			
			} else {
				instCategoryList.add(instCategoryExist.get());
			}

//			Instrument Master
			String instMasterJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expMethod.getInstmaster());
			final InstrumentMaster expInstMaster = mapper.readValue(instMasterJson, InstrumentMaster.class);
			final InstrumentMaster impInstMaster = new InstrumentMaster(expInstMaster);
			
			impInstMaster.setInstrumentcode(userdefinedinstdetails.getInstrumentcode());
			impInstMaster.setInstrumentname(userdefinedinstdetails.getInstrumentname());
			
			final List<InstrumentMaster> instMasterList = new ArrayList<>(1);
//	    	final Optional<InstrumentMaster> instMasterExist = instMasterRepo.findByInstrumentcodeAndSiteAndStatus(
//	    			expInstMaster.getInstrumentcode(), site, 1);
			final Optional<InstrumentMaster> instMasterExist = instMasterRepo.findByInstrumentcodeAndSiteAndStatus(
					impInstMaster.getInstrumentcode(), site, 1);
	    	if(!instMasterExist.isPresent()) {
	    		impInstMaster.setInstmastkey(0);
	    		impInstMaster.setSite(site);
	    		impInstMaster.setInstcategory(instCategoryList.get(0));
	    		impInstMaster.setCreatedby(createdUser);
	    		impInstMaster.setCreateddate(date);
	    		instMasterList.add(instMasterRepo.save(impInstMaster));
	    	} else {
	    		instMasterList.add(instMasterExist.get());
	    	}			
			
//	    	Method
			final Method impMethod = new Method(expMethod);
			final List<Method> methodList = new ArrayList<>(1);
			
			final MethodVersion impmethodversion = new MethodVersion(expMethod.getMethodversion().get(0));
			final List<MethodVersion> methversionList = new ArrayList<>(1);
			final MethodVersion methodversionobj = new MethodVersion();
			
			//newly added for userdefined methodname
			impMethod.setMethodname(userdefinedmethod.getMethodname());
			impMethod.setInstrawdataurl(userdefinedmethod.getInstrawdataurl());
			
		//	final Optional<Method> methodExist = methodRepo.findByMethodnameAndInstmasterAndStatus(expMethod.getMethodname(), instMasterList.get(0), 1);
			final Optional<Method> methodExist = methodRepo.findByMethodnameAndInstmasterAndStatus(impMethod.getMethodname(), instMasterList.get(0), 1);
			if(!methodExist.isPresent()) {
				
				//method version
//				impmethodversion.setMvno(0);
//				impmethodversion.setMethodkey(0);
//				methversionList.add(methversionRepo.save(impmethodversion));
				
				//method version
				methodversionobj.setBlobid(impmethodversion.getBlobid());
				methodversionobj.setCreateddate(impmethodversion.getCreateddate());
				methodversionobj.setFilename(impmethodversion.getFilename());
				methodversionobj.setInstrawdataurl(impMethod.getInstrawdataurl());
				methodversionobj.setMvno(0);
				methodversionobj.setStatus(impmethodversion.getStatus());
				methodversionobj.setVersion(impmethodversion.getVersion());
				methversionList.add(methversionRepo.save(methodversionobj));
				
				
				impMethod.setMethodkey(0);
				impMethod.setInstmaster(instMasterList.get(0));
				impMethod.setSite(site);
				impMethod.setCreatedby(createdUser);
				impMethod.setCreateddate(date);
				impMethod.setMethodstatus("A");
				impMethod.setMethodversion(methversionList);
				methodList.add(methodRepo.save(impMethod));
				importedMethods.add(methodList.get(0));
				// added to get newly imported method key in version table
				//methodversionobj.setMethodkey(methodList.get(0).getMethodkey());
				methversionList.get(0).setMethodkey(methodList.get(0).getMethodkey());
				methversionList.add(methversionRepo.save(methversionList.get(0)));
				
			} else {
//				methodList.add(methodExist.get());
	    		return new ResponseEntity<>("Import Failed - "+methodExist.get().getMethodname()+" Already Exists!", HttpStatus.ALREADY_REPORTED);
			}
			
			 
			
			
			
//			ParserBlock
			String parserBlockJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(item.get("ParserBlock"));
			List<ParserBlock> expParserBlock = mapper.readValue(parserBlockJson, new TypeReference<List<ParserBlock>>(){});
		    final List<ParserBlock> newParserBlock = new ArrayList<>();
		    final List<ParserBlock> parserBlockExist = parserBlockRepo.getParserBlockByMethodKey(methodList.get(0).getMethodkey());
		    if(parserBlockExist.isEmpty()) {
			    expParserBlock.forEach(parserBlockItem -> {
			    	parserBlockItem.setParserblockkey(0);
			    	parserBlockItem.setCreatedby(createdUser);
			    	parserBlockItem.setMethod(methodList.get(0));
			    	parserBlockItem.setCreateddate(date);
			    	
			    	newParserBlock.add(parserBlockItem);
			    });
			    parserBlockRepo.save(newParserBlock);
			    
			    
//			    final List<ParserBlock> parserBlockList = parserBlockRepo.saveAll(newParserBlock);
			    
//				if (saveAuditTrial) {
////					final String xmlData = saveToXML(null, parserBlockList);
//					final Map<String, String> fieldMap = new HashMap<String, String>();
//					fieldMap.put("site", "sitename");		
////					fieldMap.put("users", "loginid");		
//					fieldMap.put("createdby", "loginid");
//					
////					final ReadWriteXML readWriteXML =  new ReadWriteXML();
//					final String xmlData =  readWriteXML.saveXML(parserBlockList, ParserBlock.class, null, "listpojo", fieldMap);
//					
//					cfrTransService.saveCfrTransaction(page, actionType, action, userComments, 
//							site, xmlData, createdUser, request.getRemoteAddr());	
//					
//				}
				
		    }
		    final List<ParserBlock> parserBlock = parserBlockRepo.getParserBlockByMethodKey(methodList.get(0).getMethodkey());
		
//		    SampleTextSplit
			String sampleTextSplitJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(item.get("SampleTextSplit"));
			final List<SampleTextSplit> expSampleTextSplit = mapper.readValue(sampleTextSplitJson, new TypeReference<List<SampleTextSplit>>(){});
			final List<SampleTextSplit> newSampleTextSplit = new ArrayList<>();
			final List<SampleTextSplit> sampleTextSplitExist = textSplitRepo.findByMethodAndStatus(methodList.get(0), 1);
			if(sampleTextSplitExist.isEmpty() && expSampleTextSplit.size() != 0) {
				expSampleTextSplit.forEach(textSplit -> {
					textSplit.setSampletextsplitkey(0);
					textSplit.setCreatedby(createdUser);
					textSplit.setMethod(methodList.get(0));
					textSplit.setCreateddate(date);
					
					newSampleTextSplit.add(textSplit);
				});
				textSplitRepo.save(newSampleTextSplit);
			}
			final List<SampleTextSplit> sampleTextSplit = textSplitRepo.findByMethodAndStatus(methodList.get(0), 1);

//			SampleLineSplit
			String sampleLineSplitJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(item.get("SampleLineSplit"));
			List<SampleLineSplit> expSampleLineSplit = mapper.readValue(sampleLineSplitJson, new TypeReference<List<SampleLineSplit>>(){});
			final List<SampleLineSplit> newSampleLineSplit = new ArrayList<>();
			final List<SampleLineSplit> sampleLineSplitExist = lineSplitRepo.findByMethodAndStatus(methodList.get(0), 1);
			if(sampleLineSplitExist.isEmpty() && expSampleLineSplit.size() != 0) {
				expSampleLineSplit.forEach(lineSplit -> {
					lineSplit.setSamplelinesplitkey(0);
					lineSplit.setCreatedby(createdUser);
					lineSplit.setMethod(methodList.get(0));
					lineSplit.setCreateddate(date);

					newSampleLineSplit.add(lineSplit);
				});
				lineSplitRepo.save(newSampleLineSplit);
			} 
			final List<SampleLineSplit> sampleLineSplit = lineSplitRepo.findByMethodAndStatus(methodList.get(0), 1);
			
//			SampleExtract
			String sampleExtractJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(item.get("SampleExtract"));
			List<SampleExtract> expSampleExtract = mapper.readValue(sampleExtractJson, new TypeReference<List<SampleExtract>>(){});
			final List<SampleExtract> newSampleExtract = new ArrayList<>();
			final List<SampleExtract> sampleExtractExist = extractRepo.findByMethodAndStatus(methodList.get(0), 1);
			if(sampleExtractExist.isEmpty() && expSampleExtract.size() != 0) {
				expSampleExtract.forEach(sampleExtract -> {
					sampleExtract.setSampleextractkey(0);
					sampleExtract.setCreatedby(createdUser);
					sampleExtract.setCreateddate(date);
					sampleExtract.setMethod(methodList.get(0));
					if(sampleExtract.getSampletextsplit() != null ) {
						final SampleTextSplit textSplit = sampleTextSplit.stream()
															.filter(extractText -> sampleExtract.getSampletextsplit().getExtractblock().equals(extractText.getExtractblock()))
															.findAny()
															.orElse(null);
						sampleExtract.setSampletextsplit(textSplit);
					}
					if(sampleExtract.getSamplelinesplit() != null ) {
						final SampleLineSplit lineSplit = sampleLineSplit.stream()
															.filter(extractLine -> sampleExtract.getSamplelinesplit().getExtractblock().equals(extractLine.getExtractblock()))
															.findAny()
															.orElse(null);
						sampleExtract.setSamplelinesplit(lineSplit);
					}
					newSampleExtract.add(sampleExtract);
				});
				extractRepo.save(newSampleExtract);
			}
//			final List<SampleExtract> sampleExtract = extractRepo.findByMethodAndStatus(methodList.get(0), 1);
			
		
//			ParserTechnique
			String parserTechniqueJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(item.get("ParserTechnique"));
			List<ParserTechnique> expParserTechnique = mapper.readValue(parserTechniqueJson, new TypeReference<List<ParserTechnique>>(){});

//		    Delimiter
			String delimiterJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(item.get("Delimiter"));
			List<Delimiter> expDelimiter = mapper.readValue(delimiterJson, new TypeReference<List<Delimiter>>(){});
			 List<Delimiter> expdelimiterStatusList = expDelimiter.stream()
			            .filter(register -> register.getStatus() == 1)
			            .collect(Collectors.toList());
			
			
			final List<Delimiter> currDelimiter = delimiterRepo.findByStatus(1, new Sort(Sort.Direction.ASC, "delimiterkey"));
			final List<Delimiter> newDelimiter = new ArrayList<>();
			final List<Delimiter> delimiterExist = getAnyDelimiter(expdelimiterStatusList, currDelimiter);
			if(!delimiterExist.isEmpty()) {
				delimiterExist.forEach(delimiterItem -> {
					delimiterItem.setDelimiterkey(0);
					delimiterItem.setCreatedby(createdUser);
					delimiterItem.setCreateddate(date);
					delimiterItem.setLssitemaster(site);
					delimiterItem.setDelimiterstatus("A");
					
					newDelimiter.add(delimiterItem);
				});
				delimiterRepo.save(newDelimiter);
			}
			final List<Delimiter> delimiter = delimiterRepo.findByStatus(1, new Sort(Sort.Direction.ASC, "delimiterkey"));
			
//			MethodDelimiter
			String methodDelimiterJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(item.get("MethodDelimiter"));
			List<MethodDelimiter> expMethodDelimiter = mapper.readValue(methodDelimiterJson, new TypeReference<List<MethodDelimiter>>(){});
			final List<MethodDelimiter> currMethodDelimiter = methodDelimiterRepo.findByStatus(1, new Sort(Sort.Direction.ASC, "methoddelimiterkey"));
			final List<MethodDelimiter> newMethodDelimiter = new ArrayList<>();
			final List<MethodDelimiter> finalMethoddelimiterlist = new ArrayList<>();
			
			 List<MethodDelimiter> expMethodDelimiterStatusList = expMethodDelimiter.stream().filter(record -> record.getStatus() == 1).collect(Collectors.toList());

			final List<MethodDelimiter> methodDelimiterExist = getAnyMethodDelimiter(expMethodDelimiterStatusList, currMethodDelimiter);
		    if(!methodDelimiterExist.isEmpty()) {
		    	methodDelimiterExist.forEach(methodDelimiterItem -> {
		    		methodDelimiterItem.setMethoddelimiterkey(0);
		    		methodDelimiterItem.setCreatedby(createdUser);
		    		methodDelimiterItem.setCreateddate(date);
		    		methodDelimiterItem.setLssitemaster(site);
		    		methodDelimiterItem.setMethoddelimiterstatus("A");
					
		    		final Delimiter delimItem = delimiter.stream()
		    			    .filter(delim -> delim.getDelimitername().equalsIgnoreCase(methodDelimiterItem.getDelimiter().getDelimitername())) // Using equalsIgnoreCase
		    			    .findAny()
		    			    .orElse(null);
		    		methodDelimiterItem.setDelimiter(delimItem);
		    		newMethodDelimiter.add(methodDelimiterItem);

		    	});
		        finalMethoddelimiterlist.addAll(newMethodDelimiter.stream()
		            .filter(register -> register.getDelimiter() != null && register.getDelimiter().getDelimiterkey() != null)
		            .collect(Collectors.toList()));

		        methodDelimiterRepo.save(finalMethoddelimiterlist); // Save final list
		    }
		    final List<MethodDelimiter> methodDelimiter = methodDelimiterRepo.findByStatus(1, new Sort(Sort.Direction.ASC, "methoddelimiterkey"));
			
//		    ParserField
			String parserFieldJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(item.get("ParserField"));
			List<ParserField> expParserField = mapper.readValue(parserFieldJson, new TypeReference<List<ParserField>>(){});
			final List<ParserField> parserFieldExist = parserFieldRepo.getParserFieldByMethodKey(methodList.get(0).getMethodkey());
			final List<ParserField> newParserField = new ArrayList<>();
			if(parserFieldExist.isEmpty()) {
				expParserField.forEach(parserFieldItem -> {
					parserFieldItem.setParserfieldkey(0);
					parserFieldItem.setCreatedby(createdUser);
					parserFieldItem.setCreateddate(date);
					final MethodDelimiter methodDelimItem = methodDelimiter.stream()
							.filter(methodDelim -> methodDelim.getDelimiter().getDelimitername()
									.equals(parserFieldItem.getMethoddelimiter().getDelimiter().getDelimitername()) && 
									methodDelim.getParsermethod().getParsermethodkey().equals(parserFieldItem.getMethoddelimiter().getParsermethod().getParsermethodkey()))
							.findAny()
							.orElse(null);
					parserFieldItem.setMethoddelimiter(methodDelimItem);
					final ParserBlock parserBlockItem = parserBlock.stream()
							.filter(pBItem -> pBItem.getParserblockname().equals(parserFieldItem.getParserblock().getParserblockname()))
							.findAny()
							.orElse(null);
					parserFieldItem.setParserblock(parserBlockItem);
					
					newParserField.add(parserFieldItem);
				});
				parserFieldRepo.save(newParserField);
			}
			final List<ParserField> parserField = parserFieldRepo.getParserFieldByMethodKey(methodList.get(0).getMethodkey());

			final List<ParserTechnique> parserTechniqueExist = parserTechniqueRepo.getParserTechniqueByMethodKey(methodList.get(0).getMethodkey());
			List<ParserTechnique> newParserTechnique = new ArrayList<>();
			if(parserTechniqueExist.isEmpty()) {
				expParserTechnique.forEach(parserTechniqueItem -> {
					parserTechniqueItem.setParsertechniquekey(0);
					parserTechniqueItem.setCreatedby(createdUser);
					parserTechniqueItem.setCreateddate(date);
					final ParserField parserFieldItem = parserField.stream()
							.filter(pfItem -> pfItem.getFieldid().equals(parserTechniqueItem.getParserfield().getFieldid()))
							.findAny()
							.orElse(null);
					parserTechniqueItem.setParserfield(parserFieldItem);
					
					newParserTechnique.add(parserTechniqueItem);
				});
				parserTechniqueRepo.save(newParserTechnique);
			}
//			final List<ParserTechnique> parserTechnique = parserTechniqueRepo.getParserTechniqueByMethodKey(methodList.get(0).getMethodkey());
			
//			SubParserTechnique
			String subParserTechniqueJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(item.get("SubParserTechnique"));
			List<SubParserTechnique> expSubParserTechnique = mapper.readValue(subParserTechniqueJson, new TypeReference<List<SubParserTechnique>>(){});
			final List<SubParserTechnique> subParserTechniqueExist = subParserTechniqueRepo.getSubParserTechniqueByMethodKey(methodList.get(0).getMethodkey());
			List<SubParserTechnique> newSubParserTechnique = new ArrayList<>();
			if(subParserTechniqueExist.isEmpty()) {
				expSubParserTechnique.forEach(subParserTechniqueItem -> {
					subParserTechniqueItem.setSubparsertechniquekey(0);
					subParserTechniqueItem.setCreatedby(createdUser);
					subParserTechniqueItem.setCreateddate(date);
					final ParserField parserFieldItem = parserField.stream()
							.filter(pfItem -> pfItem.getFieldid().equals(subParserTechniqueItem.getParserfield().getFieldid()))
							.findAny()
							.orElse(null);					
					subParserTechniqueItem.setParserfield(parserFieldItem);
					final MethodDelimiter methodDelimItem = methodDelimiter.stream()
							.filter(methodDelim -> methodDelim.getDelimiter().getDelimitername()
									.equals(subParserTechniqueItem.getMethoddelimiter().getDelimiter().getDelimitername()) && 
									methodDelim.getParsermethod().getParsermethodkey().equals(subParserTechniqueItem.getMethoddelimiter().getParsermethod().getParsermethodkey()))
							.findAny()
							.orElse(null);
					subParserTechniqueItem.setMethoddelimiter(methodDelimItem);
					
					newSubParserTechnique.add(subParserTechniqueItem);
				});
				subParserTechniqueRepo.save(newSubParserTechnique);
			}
//			final List<SubParserTechnique> subParserTechnique = subParserTechniqueRepo.getSubParserTechniqueByMethodKey(methodList.get(0).getMethodkey());
			
//			SubParserField
			String subParserFieldJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(item.get("SubParserField"));
			List<SubParserField> expSubParserField = mapper.readValue(subParserFieldJson, new TypeReference<List<SubParserField>>(){});
			final List<SubParserField> subParserFieldExist = subParserFieldRepo.getSubParserFieldByMethodKey(methodList.get(0).getMethodkey());
			final List<SubParserField> newSubParserField = new ArrayList<>();
			if(subParserFieldExist.isEmpty()) {
				expSubParserField.forEach(subParserFieldItem -> {
					subParserFieldItem.setSubparserfieldkey(0);
					subParserFieldItem.setCreatedby(createdUser);
					subParserFieldItem.setCreateddate(date);
					final ParserField parserFieldItem = parserField.stream()
							.filter(pfItem -> pfItem.getFieldid().equals(subParserFieldItem.getParserfield().getFieldid()))
							.findAny()
							.orElse(null);						
					subParserFieldItem.setParserfield(parserFieldItem);
					
					newSubParserField.add(subParserFieldItem);
				});
				subParserFieldRepo.save(newSubParserField);
			}
			
		}

		Sort.Direction sortBy = Sort.Direction.DESC;
		
		List<Method> metdata = methodRepo.findBySiteAndStatus(site, 1, new Sort(sortBy, "methodkey"));
	//	return new ResponseEntity<>("Import Failed - "+methodExist.get().getMethodname()+" Already Exists!", HttpStatus.ALREADY_REPORTED);
		return new ResponseEntity<Object>(metdata, HttpStatus.OK);
	
	}

    private static List<Delimiter> getAnyDelimiter(List<Delimiter> expDelimiter, List<Delimiter> delimiter)
    {
		final List<Delimiter> delimiterList = expDelimiter.stream()
			    .filter(expDelimiterItem -> delimiter.stream()
			            .noneMatch(delimiterItem -> delimiterItem.getDelimitername()
			                .equalsIgnoreCase(expDelimiterItem.getDelimitername())))
			        .collect(Collectors.toList());
		
				return delimiterList;
    }

    private static List<MethodDelimiter> getAnyMethodDelimiter(List<MethodDelimiter> expMethodDelimiter, List<MethodDelimiter> methodDelimiter)
    {
        Set<String> seen = new HashSet<>();

    	List<MethodDelimiter> filteredMethodDelimiter = expMethodDelimiter.stream()
    		    .filter(record -> {
    		      
    		        String key = record.getDelimiter().getDelimitername().toLowerCase() + "_" + 
    		                     record.getParsermethod().getParsermethodname().toLowerCase();
    		        return seen.add(key); 
    		    })
    		    .collect(Collectors.toList());

    	
		 List<MethodDelimiter> methodDelimiterList = filteredMethodDelimiter.stream()
			    .filter(expMethodDelimiterItem -> methodDelimiter.stream()
			            .noneMatch(methodDelimiterItem -> 
			                methodDelimiterItem.getDelimiter().getDelimitername()
			                    .equalsIgnoreCase(expMethodDelimiterItem.getDelimiter().getDelimitername()) && 
			                methodDelimiterItem.getParsermethod().getParsermethodname()
			                    .equalsIgnoreCase(expMethodDelimiterItem.getParsermethod().getParsermethodname()) // Using equalsIgnoreCase for parsermethodname
			            ))
			        .collect(Collectors.toList());

				return methodDelimiterList;
    }

}
