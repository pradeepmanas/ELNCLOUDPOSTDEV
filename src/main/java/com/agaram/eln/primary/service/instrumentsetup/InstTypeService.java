package com.agaram.eln.primary.service.instrumentsetup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.instrumentsetup.InstrumentType;
import com.agaram.eln.primary.repository.instrumentsetup.InstTypeRepository;

/**
 * This Service class is used to access the InstTypeRepository to fetch details
 * from the 'instrumenttype' table through InstrumentType Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   11- Nov- 2019
 */
@Service
public class InstTypeService {

	@Autowired
	InstTypeRepository typeRepo;
	
	 /**
     * This method is used to retrieve list of instrument types
     * @return list of instrument types.
     */
    public List<InstrumentType> getInstType() {     	
    	return typeRepo.findAll();
    }
}
