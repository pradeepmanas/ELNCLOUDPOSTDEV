package com.agaram.eln.primary.service.material;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.material.Unit;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.material.UnitRepository;

@Service
public class UnitService {

	@Autowired
	UnitRepository unitRepository;

	public ResponseEntity<Object> createUnit(Unit objUnit) {

		final Unit objUnit2 = unitRepository.findBySunitnameIgnoreCaseAndNsitecodeAndNstatus(objUnit.getSunitname(),
				objUnit.getNsitecode(),1);
		
		objUnit.setResponse(new Response());
		
		LSuserMaster objMaster = new LSuserMaster();
		objMaster.setUsercode(objUnit.getObjsilentaudit().getLsuserMaster());

		if (objUnit2 == null && objUnit.getNunitcode() == null) {
			objUnit.setNdefaultstatus(1);
			objUnit.setNsitecode(objUnit.getNsitecode());
			objUnit.setNstatus(1);
			objUnit.setCreateby(objMaster);
			try {
				objUnit.setCreatedate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			objUnit.getResponse().setStatus(true);
			objUnit.getResponse().setInformation("IDS_SUCCESS");

			unitRepository.save(objUnit);
			
			return new ResponseEntity<>(objUnit, HttpStatus.OK);
		} else {
			objUnit.getResponse().setStatus(false);
			objUnit.getResponse().setInformation("IDS_ALREADYEXIST");
			objUnit.setInfo("Duplicate Entry:  Unit - " + objUnit.getSunitname());
			return new ResponseEntity<>(objUnit, HttpStatus.OK);
		}
	}

	public ResponseEntity<Object> getUnit(Integer nsiteInteger) {

		List<Unit> lstUnit = unitRepository.findByNsitecodeOrderByNunitcodeDesc(nsiteInteger);

		return new ResponseEntity<>(lstUnit, HttpStatus.OK);
	}

	public ResponseEntity<Object> updateUnit(Unit objUnit) throws ParseException {

		final Unit unit = getActiveUnitById(objUnit.getNunitcode());
		
		objUnit.setResponse(new Response());

		final Unit unitobj = unitRepository.findByNunitcodeAndNsitecode(objUnit.getNunitcode(), objUnit.getNsitecode());

		if (unit == null) {
			objUnit.getResponse().setStatus(false);
			objUnit.getResponse().setInformation("IDS_ALREADYDELETED");
			return new ResponseEntity<>(objUnit, HttpStatus.OK);
		} else {

			final Unit unit1 = unitRepository.findBySunitnameIgnoreCaseAndNsitecodeAndNstatus(objUnit.getSunitname(), objUnit.getNsitecode(),1);

			if (unit1 == null || (unit1.getNunitcode().equals(objUnit.getNunitcode()))) {

				unitobj.setObjsilentaudit(objUnit.getObjsilentaudit());
				objUnit.setModifieddate(commonfunction.getCurrentUtcTime());
				unitRepository.save(objUnit);
				objUnit.getResponse().setStatus(true);
				objUnit.getResponse().setInformation("IDS_SUCCESS");
				return new ResponseEntity<>(objUnit, HttpStatus.OK);

			} else {
				objUnit.getResponse().setStatus(false);
				objUnit.getResponse().setInformation("IDS_ALREADYEXIST");
				return new ResponseEntity<>(objUnit, HttpStatus.OK);
			}
		}
	}

	public ResponseEntity<Object> deleteUnit(Unit objUnit) {
		final Unit unit = getActiveUnitById(objUnit.getNunitcode());
		final Unit unitobj = unitRepository.findByNunitcode(objUnit.getNunitcode());
		if (unit == null) {

			return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYDELETED.getreturnstatus(),
					HttpStatus.EXPECTATION_FAILED);
		} else {

			unitobj.setObjsilentaudit(objUnit.getObjsilentaudit());
			unitobj.setNstatus(Enumeration.TransactionStatus.DELETED.gettransactionstatus());
			unitRepository.save(unitobj);
			return getUnit(objUnit.getNsitecode());

		}
	}

	public Unit getActiveUnitById(int nunitCode) {

		final Unit objUnit = unitRepository.findByNunitcode(nunitCode);

		return objUnit;
	}

}
