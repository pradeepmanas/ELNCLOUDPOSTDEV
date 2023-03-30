package com.agaram.eln.primary.service.material;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.material.Manufacturer;
import com.agaram.eln.primary.repository.material.ManufacturerRepository;

@Service
public class ManufacturerService {

	@Autowired
	ManufacturerRepository manufacturerRepository;

	public ResponseEntity<Object> getManufacturer(Integer nsiteInteger) {
		List<Manufacturer> lstManufacturer = manufacturerRepository.findByNsitecodeOrderByNmanufcodeDesc(nsiteInteger);
		return new ResponseEntity<>(lstManufacturer, HttpStatus.OK);
	}

	public ResponseEntity<Object> createManufacturer(Manufacturer objManufacturer) {
		final Manufacturer objUnit2 = manufacturerRepository.findBySmanufnameAndNsitecode(objManufacturer.getSmanufname(),objManufacturer.getNsitecode());
		
		objManufacturer.setResponse(new Response());

		if (objUnit2 == null && objManufacturer.getNmanufcode() == null) {
			objManufacturer.setNsitecode(objManufacturer.getNsitecode());
			objManufacturer.setNstatus(1);
			objManufacturer.getResponse().setStatus(true);
			objManufacturer.getResponse().setInformation("IDS_SUCCESS");

			manufacturerRepository.save(objManufacturer);
			
			return new ResponseEntity<>(objManufacturer, HttpStatus.OK);
		} else {
			objManufacturer.getResponse().setStatus(false);
			objManufacturer.getResponse().setInformation("IDS_ALREADYEXIST");
			objManufacturer.setInfo("Duplicate Entry:  Unit - " + objManufacturer.getSmanufname());
			return new ResponseEntity<>(objManufacturer, HttpStatus.OK);
		}
	}

	public ResponseEntity<Object> updateManufacturer(Manufacturer objManufacturer) {
		final Manufacturer grade = getActiveManufacturerById(objManufacturer.getNmanufcode());
		objManufacturer.setResponse(new Response());
		if (grade == null) {
			objManufacturer.getResponse().setStatus(false);
			objManufacturer.getResponse().setInformation("IDS_ALREADYDELETED");
			return new ResponseEntity<>(objManufacturer, HttpStatus.OK);
		} else {

			final Manufacturer grade1 = manufacturerRepository.findBySmanufnameAndNsitecode(objManufacturer.getSmanufname(), objManufacturer.getNsitecode());

			if (grade1 == null || (grade1.getNmanufcode().equals(objManufacturer.getNmanufcode()))) {
				grade.setObjsilentaudit(objManufacturer.getObjsilentaudit());
				manufacturerRepository.save(objManufacturer);
				objManufacturer.getResponse().setStatus(true);
				objManufacturer.getResponse().setInformation("IDS_SUCCESS");
				return new ResponseEntity<>(objManufacturer, HttpStatus.OK);

			} else {
				objManufacturer.getResponse().setStatus(false);
				objManufacturer.getResponse().setInformation("IDS_ALREADYEXIST");
				return new ResponseEntity<>(objManufacturer, HttpStatus.OK);
			}
		}
	}

	public ResponseEntity<Object> deleteManufacturer(Manufacturer objManufacturer) {
		final Manufacturer supplier = getActiveManufacturerById(objManufacturer.getNmanufcode());
		if (supplier == null) {
			return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYDELETED.getreturnstatus(), HttpStatus.EXPECTATION_FAILED);
		} else {
			supplier.setObjsilentaudit(objManufacturer.getObjsilentaudit());
			supplier.setNstatus(Enumeration.TransactionStatus.DELETED.gettransactionstatus());
			manufacturerRepository.save(supplier);
			return getManufacturer(objManufacturer.getNsitecode());
		}
	}

	public Manufacturer getActiveManufacturerById(int nunitCode) {
		final Manufacturer objUnit = manufacturerRepository.findByNmanufcode(nunitCode);
		return objUnit;
	}

}
