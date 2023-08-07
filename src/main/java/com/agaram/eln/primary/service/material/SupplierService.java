package com.agaram.eln.primary.service.material;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.material.Supplier;
import com.agaram.eln.primary.repository.material.SupplierRepository;

@Service
public class SupplierService {

	@Autowired
	SupplierRepository supplierRepository;

	public ResponseEntity<Object> getSupplier(Integer nsiteInteger) {
		List<Supplier> lstSupplier = supplierRepository.findByNsitecodeOrderByNsuppliercode(nsiteInteger);
		return new ResponseEntity<>(lstSupplier, HttpStatus.OK);
	}

	public ResponseEntity<Object> createSupplier(Supplier objSupplier) {
		final Supplier objUnit2 = supplierRepository.findBySsuppliernameIgnoreCaseAndNsitecode(objSupplier.getSsuppliername(),objSupplier.getNsitecode());
		
		objSupplier.setResponse(new Response());

		if (objUnit2 == null && objSupplier.getNsuppliercode() == null) {
			objSupplier.setNsitecode(objSupplier.getNsitecode());
			objSupplier.setNstatus(1);
			objSupplier.getResponse().setStatus(true);
			objSupplier.getResponse().setInformation("IDS_SUCCESS");

			supplierRepository.save(objSupplier);
			
			return new ResponseEntity<>(objSupplier, HttpStatus.OK);
		} else {
			objSupplier.getResponse().setStatus(false);
			objSupplier.getResponse().setInformation("IDS_ALREADYEXIST");
			objSupplier.setInfo("Duplicate Entry:  Unit - " + objSupplier.getSsuppliername());
			return new ResponseEntity<>(objSupplier, HttpStatus.OK);
		}
	}

	public ResponseEntity<Object> updateSupplier(Supplier objSupplier) {
		final Supplier grade = getActiveSupplierById(objSupplier.getNsuppliercode());
		objSupplier.setResponse(new Response());
		if (grade == null) {
			objSupplier.getResponse().setStatus(false);
			objSupplier.getResponse().setInformation("IDS_ALREADYDELETED");
			return new ResponseEntity<>(objSupplier, HttpStatus.OK);
		} else {

			final Supplier grade1 = supplierRepository.findBySsuppliernameIgnoreCaseAndNsitecode(objSupplier.getSsuppliername(), objSupplier.getNsitecode());

			if (grade1 == null || (grade1.getNsuppliercode().equals(objSupplier.getNsuppliercode()))) {
				grade.setObjsilentaudit(objSupplier.getObjsilentaudit());
				supplierRepository.save(objSupplier);
				objSupplier.getResponse().setStatus(true);
				objSupplier.getResponse().setInformation("IDS_SUCCESS");
				return new ResponseEntity<>(objSupplier, HttpStatus.OK);

			} else {
				objSupplier.getResponse().setStatus(false);
				objSupplier.getResponse().setInformation("IDS_ALREADYEXIST");
				return new ResponseEntity<>(objSupplier, HttpStatus.OK);
			}
		}
	}

	public ResponseEntity<Object> deleteSupplier(Supplier objSupplier) {
		final Supplier supplier = getActiveSupplierById(objSupplier.getNsuppliercode());
		if (supplier == null) {
			return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYDELETED.getreturnstatus(), HttpStatus.EXPECTATION_FAILED);
		} else {
			supplier.setObjsilentaudit(objSupplier.getObjsilentaudit());
			supplier.setNstatus(Enumeration.TransactionStatus.DELETED.gettransactionstatus());
			supplierRepository.save(supplier);
			return getSupplier(objSupplier.getNsitecode());
		}
	}

	public Supplier getActiveSupplierById(int nunitCode) {
		final Supplier objUnit = supplierRepository.findByNsuppliercode(nunitCode);
		return objUnit;
	}
}