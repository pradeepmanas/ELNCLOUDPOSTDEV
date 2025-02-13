package com.agaram.eln.primary.service.material;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.barcode.BarcodeMaster;
import com.agaram.eln.primary.model.cfr.LSpreferences;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.model.material.MaterialConfig;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.repository.cfr.LSpreferencesRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
import com.agaram.eln.primary.repository.material.ElnmaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialCategoryRepository;
import com.agaram.eln.primary.repository.material.MaterialConfigRepository;
import com.agaram.eln.primary.repository.material.MaterialTypeRepository;
import com.agaram.eln.primary.repository.protocol.LSlogilabprotocoldetailRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsamplemasterRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.jsonwebtoken.lang.Arrays;

@Service
public class MaterialTypeService {
	
	@Autowired
	MaterialTypeRepository materialTypeRepository;
	@Autowired
	MaterialConfigRepository materialConfigRepository;
	@Autowired
	LSsamplemasterRepository lssamplemasterRepository;
	@Autowired
	MaterialCategoryRepository materialCategoryRepository;
	@Autowired
	ElnmaterialRepository elnmaterialRepository;
	@Autowired
	LSlogilablimsorderdetailRepository lSlogilablimsorderdetailRepository;
	@Autowired
	LSlogilabprotocoldetailRepository lSlogilabprotocoldetailRepository;
	@Autowired
	LSpreferencesRepository lspreferencesRepository;

	public ResponseEntity<Object> getMaterialType(MaterialType objMaterialType) {
		List<MaterialType> lstmaterialtype = materialTypeRepository.
				findByNmaterialtypecodeNotAndNsitecodeOrNmaterialtypecodeNotAndNdefaultstatusOrderByNmaterialtypecodeDesc(-1,objMaterialType.getNsitecode(),-1,4);
		return new ResponseEntity<>(lstmaterialtype, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> getMaterialTypeonId(MaterialType objMaterialType) {
		MaterialType lsmaterialtype = materialTypeRepository.findOne(objMaterialType.getNmaterialtypecode());
		return new ResponseEntity<>(lsmaterialtype, HttpStatus.OK);
	}

	public ResponseEntity<Object> getMaterialTypeField(MaterialType objMaterialType) {
		MaterialConfig objConfig = materialConfigRepository.findByNformcodeAndNmaterialtypecodeAndNstatus(40, objMaterialType.getNmaterialtypecode(), 1);
		if(objConfig != null) {
			return new ResponseEntity<>(objConfig.getJsondata(),HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public ResponseEntity<Object> updateMaterialTypeField(MaterialConfig objMaterialType) {
		MaterialConfig objConfig = materialConfigRepository.findByNformcodeAndNmaterialtypecodeAndNstatus(40, objMaterialType.getNmaterialtypecode(), 1);
		if(objConfig != null) {
			objConfig.setJsondata(objMaterialType.getJsondata());
			materialConfigRepository.save(objConfig);
		}else {
			
			if(objMaterialType.getNmaterialtypecode() == 5) {
				objMaterialType.setNmaterialconfigcode(12);
			}else if(objMaterialType.getNmaterialtypecode() == 6) {
				objMaterialType.setNmaterialconfigcode(13);
			}else if(objMaterialType.getNmaterialtypecode() == 7) {
				objMaterialType.setNmaterialconfigcode(14);
			}
			objMaterialType.setNformcode(40);
			objMaterialType.setNstatus(1);
			materialConfigRepository.save(objMaterialType);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	public ResponseEntity<Object> createMaterialType(MaterialType objMaterialType) throws JsonParseException, JsonMappingException, IOException, ParseException {
		
		if(objMaterialType.getNmaterialtypecode() == null) {
			
			List<MaterialType> objlstTypes = materialTypeRepository.
					findBySmaterialtypenameIgnoreCaseAndNsitecodeOrderByNmaterialtypecode(objMaterialType.getSmaterialtypename(),objMaterialType.getNsitecode());
			
			if(objlstTypes.isEmpty()) {
				
//				List<MaterialType> objlstTypes1 = materialTypeRepository.findAll();
//				
//				objMaterialType.setNmaterialtypecode(objlstTypes1.size()+1);
				objMaterialType.setNdefaultstatus(3);
				objMaterialType.setNstatus(1);
				objMaterialType.setCreatedate(commonfunction.getCurrentUtcTime());
				objMaterialType.setCreateby(objMaterialType.getCreateby());
				objMaterialType.setInfo("IDS_SUCCESS");
				materialTypeRepository.save(objMaterialType);
				return new ResponseEntity<>(objMaterialType, HttpStatus.OK);
				
			}else {
				
				objMaterialType.setInfo("IDS_FAIL_ALREADY_EXIST");
				return new ResponseEntity<>(objMaterialType, HttpStatus.OK);
			}
		}
		else {
			List<MaterialType> objlstTypes = materialTypeRepository.findBySmaterialtypenameIgnoreCaseAndNsitecodeAndNmaterialtypecodeNot(
					objMaterialType.getSmaterialtypename(),objMaterialType.getNsitecode(),objMaterialType.getNmaterialtypecode());
			
			if(objlstTypes.isEmpty()) {
				
				MaterialType objMType = materialTypeRepository.findByNmaterialtypecodeAndNstatus(objMaterialType.getNmaterialtypecode(), 1);				
				
				objMaterialType.setNdefaultstatus(objMType.getNdefaultstatus());
				objMaterialType.setNstatus(1);
				objMaterialType.setCreatedate(objMType.getCreatedate());
				objMaterialType.setCreateby(objMType.getCreateby());
				objMaterialType.setModifieddate(commonfunction.getCurrentUtcTime());
				
				materialTypeRepository.save(objMaterialType);
				
				objMaterialType.setInfo("IDS_SUCCESS");
				return new ResponseEntity<>(objMaterialType, HttpStatus.OK);
			}else {
				
				objMaterialType.setInfo("IDS_FAIL_ALREADY_EXIST");
				return new ResponseEntity<>(objMaterialType, HttpStatus.OK);
			}
		}				
		
	}

	public ResponseEntity<Object> syncSamplestoType() throws ParseException {
		
		LSpreferences objPrefrence = lspreferencesRepository.findByTasksettingsAndValuesettings("samplesync", "1");
		
		if(objPrefrence == null) {
			
			List<Elnmaterial> objMatLst = new ArrayList<Elnmaterial>();
			List<LSsamplemaster> objList = lssamplemasterRepository.findBystatus(1);
			
			MaterialType objType = materialTypeRepository.findBySmaterialtypenameIgnoreCase("Samples");
			MaterialCategory objCategory = materialCategoryRepository.findBySmaterialcatnameAndNstatus("Samples", 1);
			
			objList.stream().peek(f -> {
				Elnmaterial objMaterial = new Elnmaterial();
				
				objMaterial.setSmaterialname(f.getSamplename());
				objMaterial.setNstatus(1);
				objMaterial.setCreateddate(f.getCreatedate());
				objMaterial.setCreateby(f.getCreateby());
				objMaterial.setNsitecode(f.getLssitemaster().getSitecode());
				objMaterial.setJsondata(null);
				objMaterial.setMaterialcategory(objCategory);
				objMaterial.setMaterialtype(objType);
				objMaterial.setExpirytype(0);
				objMaterial.setSamplecode(f.getSamplecode());
				objMaterial.setRemarks("");
				
				objMatLst.add(objMaterial);
				
			}).collect(Collectors.toList());
			
			if(!objMatLst.isEmpty()) {
				elnmaterialRepository.save(objMatLst);
			}
			
			List<Elnmaterial> objMatSampleLst = elnmaterialRepository.findBySamplecodeIsNotNull();
			
			if(!objMatSampleLst.isEmpty()) {
				List<Integer> lstSamples = new ArrayList<Integer>();

				objMatSampleLst.stream().peek(f -> { lstSamples.add(f.getSamplecode()); }).collect(Collectors.toList());
				
				List<LSsamplemaster> lsSampleLst = lssamplemasterRepository.findBySamplecodeIn(lstSamples);
				
				List<LSlogilablimsorderdetail> objOrders = lSlogilablimsorderdetailRepository.findByLssamplemasterIn(lsSampleLst);
				
				if(!objOrders.isEmpty()) {
					
					objOrders.stream().peek(x -> {
						objMatSampleLst.stream().peek(y -> {
							if(x.getLssamplemaster().getSamplecode() == y.getSamplecode()) {
								x.setElnmaterial(y);
							}
						}).collect(Collectors.toList());
					}).collect(Collectors.toList());
					
					lSlogilablimsorderdetailRepository.save(objOrders);
				}
				
				List<LSlogilabprotocoldetail> objProOrders = lSlogilabprotocoldetailRepository.findByLssamplemasterIn(lsSampleLst);
				
				if(!objProOrders.isEmpty()) {
					
					objProOrders.stream().peek(x -> {
						objMatSampleLst.stream().peek(y -> {
							if(x.getLssamplemaster().getSamplecode() == y.getSamplecode()) {
								x.setElnmaterial(y);
							}
						}).collect(Collectors.toList());
					}).collect(Collectors.toList());
					
					lSlogilabprotocoldetailRepository.save(objProOrders);
				}
			}
			
			LSpreferences lSpreferences = lspreferencesRepository.findByTasksettings("samplesync");
			
			if(lSpreferences != null) {
				lSpreferences.setValuesettings("1");
				lspreferencesRepository.save(lSpreferences);
			}
		}
		
		return null;
	}
	
	public MaterialType RetiredMaterial(MaterialType objMaterialType) {

		objMaterialType.setNstatus(-1);
		materialTypeRepository.save(objMaterialType);  	
		return objMaterialType;
	}


}