package com.agaram.eln.primary.service.methodsetup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.repository.methodsetup.AppMasterRepository;

/**
 * This Service class is used to access the AppMasterRepository to fetch details
 * from the 'appmaster' table through AppMaster Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Apr- 2020
 */
@Service
public class AppMasterService {

	@Autowired
	AppMasterRepository appMasterRepo;
	
	/**
	 * This method is used to retrieve list of all AppMasters based on which the 
	 * General fields are to be created.
	 * @return list of appmaster entities
	 */
	@Transactional
	public ResponseEntity<Object> getAppMaster(){
		return new ResponseEntity<>(appMasterRepo.findAll(), HttpStatus.OK);		
	}
}
