package com.agaram.eln.primary.service.material;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.material.MaterialGrade;
import com.agaram.eln.primary.repository.material.MaterialGradeRepository;

@Service
public class MaterialGradeService {

	@Autowired
	MaterialGradeRepository materialGradeRepository;
	
	public ResponseEntity<Object> getGrade(Integer nsiteInteger) {
		List<MaterialGrade> lstGrade = materialGradeRepository.findByNsitecodeOrderByNmaterialgradecodeDesc(nsiteInteger);
		return new ResponseEntity<>(lstGrade, HttpStatus.OK);
	}

	public ResponseEntity<Object> createGrade(MaterialGrade objGrade) {
		final MaterialGrade objUnit2 = materialGradeRepository.findBySmaterialgradenameIgnoreCaseAndNsitecode(objGrade.getSmaterialgradename(),objGrade.getNsitecode());
		
		objGrade.setResponse(new Response());

		if (objUnit2 == null && objGrade.getNmaterialgradecode() == null) {
			objGrade.setNdefaultstatus(1);
			objGrade.setNsitecode(objGrade.getNsitecode());
			objGrade.setNstatus(1);
			
			objGrade.getResponse().setStatus(true);
			objGrade.getResponse().setInformation("IDS_SUCCESS");

			materialGradeRepository.save(objGrade);
			
			return new ResponseEntity<>(objGrade, HttpStatus.OK);
		} else {
			objGrade.getResponse().setStatus(false);
			objGrade.getResponse().setInformation("IDS_ALREADYEXIST");
			objGrade.setInfo("Duplicate Entry:  Unit - " + objGrade.getSmaterialgradename());
			return new ResponseEntity<>(objGrade, HttpStatus.OK);
		}
	}

	public ResponseEntity<Object> updateGrade(MaterialGrade objGrade) {
		final MaterialGrade grade = getActiveGradeById(objGrade.getNmaterialgradecode());
		objGrade.setResponse(new Response());
		if (grade == null) {
			objGrade.getResponse().setStatus(false);
			objGrade.getResponse().setInformation("IDS_ALREADYDELETED");
			return new ResponseEntity<>(objGrade, HttpStatus.OK);
		} else {

			final MaterialGrade grade1 = materialGradeRepository.findBySmaterialgradenameIgnoreCaseAndNsitecode(objGrade.getSmaterialgradename(), objGrade.getNsitecode());

			if (grade1 == null || (grade1.getNmaterialgradecode().equals(objGrade.getNmaterialgradecode()))) {

				grade.setObjsilentaudit(objGrade.getObjsilentaudit());
				materialGradeRepository.save(objGrade);
				objGrade.getResponse().setStatus(true);
				objGrade.getResponse().setInformation("IDS_SUCCESS");
				return new ResponseEntity<>(objGrade, HttpStatus.OK);

			} else {
				objGrade.getResponse().setStatus(false);
				objGrade.getResponse().setInformation("IDS_ALREADYEXIST");
				return new ResponseEntity<>(objGrade, HttpStatus.OK);
			}
		}
	}

	public ResponseEntity<Object> deleteGrade(MaterialGrade objGrade) {
		final MaterialGrade grade = getActiveGradeById(objGrade.getNmaterialgradecode());
		final MaterialGrade gradeObj = materialGradeRepository.findByNmaterialgradecode(objGrade.getNmaterialgradecode());
		if (grade == null) {
			return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYDELETED.getreturnstatus(), HttpStatus.EXPECTATION_FAILED);
		} else {
			gradeObj.setObjsilentaudit(objGrade.getObjsilentaudit());
			gradeObj.setNstatus(Enumeration.TransactionStatus.DELETED.gettransactionstatus());
			materialGradeRepository.save(gradeObj);
			return getGrade(objGrade.getNsitecode());
		}
	}

	public MaterialGrade getActiveGradeById(int nunitCode) {
		final MaterialGrade objUnit = materialGradeRepository.findByNmaterialgradecode(nunitCode);
		return objUnit;
	}

}