package com.agaram.eln.primary.service.methodsetup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.repository.methodsetup.ControlTypeRepository;

/**
 * This Service class is used to access the ControlTypeRepository to fetch details
 * from the 'controltype' table through ControlType Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Apr- 2020
 */
@Service
public class ControlTypeService {

	@Autowired
	ControlTypeRepository controlTypeRepo;
	
	/**
	 * This method is used to retrieve list of all ControlTypes that are to be associated with
	 * Custom fields.
	 * @return list of control types entities.
	 */
	@Transactional
	public ResponseEntity<Object> getControlType(){
		return new ResponseEntity<>(controlTypeRepo.findAll(), HttpStatus.OK);		
	}
}
