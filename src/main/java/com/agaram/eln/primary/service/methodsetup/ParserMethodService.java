package com.agaram.eln.primary.service.methodsetup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.repository.methodsetup.ParserMethodRepository;

/**
 * This Service class is used to access the ParserMethodRepository to fetch details
 * from the 'parsermethod' table through ParserMethod Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Apr- 2020
 */
@Service
public class ParserMethodService {
	
	@Autowired
	ParserMethodRepository parserMethodRepo;
	
	/**
	 * This method is used to fetch list of all ParserMethod entities.
	 * @return response object with list of all ParserMethod entities
	 */
	@Transactional
	public ResponseEntity<Object> getParserMethod(){
		return  new ResponseEntity<>(parserMethodRepo.findAll(), HttpStatus.OK);
	}

}
