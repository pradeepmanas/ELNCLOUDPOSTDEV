package com.agaram.eln.primary.service.material;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.material.Unit;
import com.agaram.eln.primary.repository.material.UnitRepository;

@Service
public class UnitService {

	@Autowired
	UnitRepository unitRepository;

	public ResponseEntity<Object> createUnit(Unit objUnit) {

		final Unit objUnit2 = unitRepository.findByNstatusAndSunitname(1, objUnit.getSunitname());

		if (objUnit2 == null && objUnit.getNunitcode() == null) {
			objUnit.setNdefaultstatus(1);
			objUnit.setNsitecode(1);
			objUnit.setNstatus(1);

			unitRepository.save(objUnit);

			return getUnit();
		} else {

			return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYEXISTS.getreturnstatus(), HttpStatus.CONFLICT);
		}
	}

//	private List<Unit> getUnitListByName(String sunitname, Integer nsitecode) {
//		List<Unit> lstUnit = unitRepository.findBySunitnameAndNstatus(sunitname, 1);
//		return lstUnit;
//	}

	public ResponseEntity<Object> getUnit() {
		List<Unit> lstUnit = unitRepository.findByNstatusOrderByNunitcodeDesc(1);

		return new ResponseEntity<>(lstUnit, HttpStatus.OK);
	}

	public ResponseEntity<Object> updateUnit(Unit objUnit) {

		final Unit unit = getActiveUnitById(objUnit.getNunitcode());

		if (unit == null) {
			return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYDELETED.getreturnstatus(),
					HttpStatus.EXPECTATION_FAILED);
		} else {

			final Unit unit1 = unitRepository.findByNstatusAndSunitname(1, objUnit.getSunitname());

//			final List<Unit> unitList = getUnitListByName(objUnit.getSunitname(), objUnit.getNsitecode());
			if (unit1 == null || (unit1.getNunitcode() == objUnit.getNunitcode())) {
				unitRepository.save(objUnit);
				return getUnit();
			} else {
				return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYEXISTS.getreturnstatus(),
						HttpStatus.CONFLICT);
			}
		}
	}

	public ResponseEntity<Object> deleteUnit(Unit objUnit) {
		final Unit unit = getActiveUnitById(objUnit.getNunitcode());

		if (unit == null) {

			return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYDELETED.getreturnstatus(),
					HttpStatus.EXPECTATION_FAILED);
		} else {

			objUnit.setNstatus(Enumeration.TransactionStatus.DELETED.gettransactionstatus());
			unitRepository.save(objUnit);
			return getUnit();

		}
	}

	public Unit getActiveUnitById(int nunitCode) {

		final Unit objUnit = unitRepository.findByNunitcodeAndNstatus(nunitCode, 1);

		return objUnit;
	}

}
