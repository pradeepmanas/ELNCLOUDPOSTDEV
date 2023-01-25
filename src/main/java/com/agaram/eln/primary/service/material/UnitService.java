package com.agaram.eln.primary.service.material;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.material.Unit;
import com.agaram.eln.primary.repository.material.UnitRepository;

@Service
public class UnitService {

	@Autowired
	UnitRepository unitRepository;

	public ResponseEntity<Object> createUnit(Unit objUnit) {

		final Unit objUnit2 = unitRepository.findByNstatusAndSunitnameAndNsitecode(1, objUnit.getSunitname(),objUnit.getNsitecode());

		if (objUnit2 == null && objUnit.getNunitcode() == null) {
			objUnit.setNdefaultstatus(1);
			objUnit.setNsitecode(objUnit.getNsitecode());
			objUnit.setNstatus(1);

			unitRepository.save(objUnit);

			return getUnit(objUnit.getNsitecode());
		} else {
			objUnit.setInfo("Duplicate Entry:  Unit - " + objUnit.getSunitname());
			//return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYEXISTS.getreturnstatus(), HttpStatus.CONFLICT);
			return new ResponseEntity<>(objUnit, HttpStatus.CONFLICT);
		}
	}

//	private List<Unit> getUnitListByName(String sunitname, Integer nsitecode) {
//		List<Unit> lstUnit = unitRepository.findBySunitnameAndNstatus(sunitname, 1);
//		return lstUnit;
//	}

	public ResponseEntity<Object> getUnit(Integer nsiteInteger) {
		
		List<Unit> lstUnit = unitRepository.findByNstatusAndNsitecodeOrderByNunitcodeDesc(1,nsiteInteger);

		return new ResponseEntity<>(lstUnit, HttpStatus.OK);
	}

	public ResponseEntity<Object> updateUnit(Unit objUnit) {

		final Unit unit = getActiveUnitById(objUnit.getNunitcode());

		final Unit unitobj = unitRepository.findByNunitcodeAndNstatus(objUnit.getNunitcode(), 1);

		if (unit == null) {
			return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYDELETED.getreturnstatus(),
					HttpStatus.EXPECTATION_FAILED);
		} else {

			final Unit unit1 = unitRepository.findByNstatusAndSunitnameAndNsitecode(1, objUnit.getSunitname(),objUnit.getNsitecode());

//			final List<Unit> unitList = getUnitListByName(objUnit.getSunitname(), objUnit.getNsitecode());
			if (unit1 == null || (unit1.getNunitcode() == objUnit.getNunitcode())) {
				
				unitobj.setObjsilentaudit(objUnit.getObjsilentaudit());
				unitRepository.save(objUnit);
				return getUnit(objUnit.getNsitecode());
			} else {
				
//				return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYEXISTS.getreturnstatus(),
//						HttpStatus.CONFLICT);
				objUnit.setInfo("Duplicate Entry:  Unit - " + objUnit.getSunitname());
				return new ResponseEntity<>(objUnit,
						HttpStatus.CONFLICT);
			}
		}
	}

	public ResponseEntity<Object> deleteUnit(Unit objUnit,LScfttransaction obj) {
		final Unit unit = getActiveUnitById(objUnit.getNunitcode());
		final Unit unitobj = unitRepository.findByNunitcodeAndNstatus(objUnit.getNunitcode(), 1);
		if (unit == null) {

			return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYDELETED.getreturnstatus(),
					HttpStatus.EXPECTATION_FAILED);
		} else {

			unitobj.setObjsilentaudit(obj);
			unitobj.setNstatus(Enumeration.TransactionStatus.DELETED.gettransactionstatus());
			unitRepository.save(unitobj);
			return getUnit(objUnit.getNsitecode());

		}
	}

	public Unit getActiveUnitById(int nunitCode) {

		final Unit objUnit = unitRepository.findByNunitcodeAndNstatus(nunitCode, 1);

		return objUnit;
	}

}
