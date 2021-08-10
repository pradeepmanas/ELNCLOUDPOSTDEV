package com.agaram.eln.primary.service.instrumentsetup;

import java.util.Optional;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.instrumentsetup.Rs232Settings;
import com.agaram.eln.primary.repository.instrumentsetup.InstMasterRepository;
import com.agaram.eln.primary.repository.instrumentsetup.Rs232SettingsRepository;
/**
 * This Service class is used to access the Rs232SettingsRepository to fetch details
 * from the 'rs232settings' table through Rs232Settings Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   08- Nov- 2019
 */
@Service
public class Rs232SettingsService {
	
	@Autowired
	Rs232SettingsRepository rs232Repo;

	@Autowired
	InstMasterRepository masterRepo;

	/**
	 * This method is used to add new entry of instrument master to rs232 settings
	 * @param rs232Settings [Rs232Settings] -  holding details of all fields in 'Rs232Settings' Pojo.
	 * @return newly added Rs232Settings object with status Message
	 */
	public ResponseEntity<Object> createRs232Settings(Rs232Settings rs232Settings) {		
	
    	final Rs232Settings savedRs232Settings = rs232Repo.save(rs232Settings);
   		return new ResponseEntity<>(savedRs232Settings, HttpStatus.OK);
		
	}

	/**
	 * This method is used to update the selected 'Rs232settings' details based on provided 
	 * primary key id and details of the  updated fields.
	 * @param rs232MastKey [int] 'Rs232settings' primary key to update
	 * @param rs232Settings [Rs232settings] Object with details to update
	 * @param instMastKey [int] primary key of InstrumentMaster
	 * @return updated 'rs232settings' object
	 */
	public ResponseEntity<Object> updateRs232Settings(Rs232Settings rs232Settings,
			final Integer rs232MastKey, final int instMastKey) {
		
	    final Rs232Settings searchEntity = rs232Repo.findOne(rs232MastKey);
	
	    if (searchEntity != null) {
	    	final Rs232Settings rs232Obj = searchEntity;
	    	
	    	final InstrumentMaster instMaster = masterRepo.findOne(instMastKey);
	    	
	    	rs232Obj.setInstmaster(instMaster);
	    	
	    	rs232Obj.setBaudrate(rs232Settings.getBaudrate());
	    	rs232Obj.setComport(rs232Settings.getComport());
	    	rs232Obj.setDatabits(rs232Settings.getDatabits());
	    	rs232Obj.setHandshake(rs232Settings.getHandshake());
	    	rs232Obj.setParity(rs232Settings.getParity());
	    	rs232Obj.setStopbits(rs232Settings.getStopbits());
	    	rs232Obj.setTimeout(rs232Settings.getTimeout());	    	
	    	
	    	return saveEntity(rs232Obj);     	
	    } 
	    else {
	        throw new EntityNotFoundException();
	    }
	}
	
	/**
	 * This method is used to check for duplicate entry of Rs232settings based on 'instrument master'
	 * while add and update.
	 * @param rs232Settings [Rs232Settings] entity with all its fields
	 * @return response status of add and update
	 */
	private ResponseEntity<Object> saveEntity(final Rs232Settings rs232Settings)
	{
		final Optional<Rs232Settings> rs232SettingsByInstrument = rs232Repo.findByInstmaster(rs232Settings.getInstmaster());
	 	
	    if(rs232SettingsByInstrument.isPresent())
	    {
	       	//Update loop
	   		if ( rs232SettingsByInstrument.get().getRs232mastkey() == rs232Settings.getRs232mastkey())
	   		{ // Updating rest of the fields while update
	   			//ok=200
	   			final Rs232Settings saveSettings = rs232Repo.save(rs232Settings);
	       		return new ResponseEntity<>(saveSettings, HttpStatus.OK);
	   		}
	   		else
	   		{ 	
	   			//Conflict =409 - Duplicate entry
	   			return new ResponseEntity<>(rs232Settings.getInstmaster().getInstrumentcode(), HttpStatus.CONFLICT);
	   		}
	   	}
	   	else
	   	{
	   		//new entry loop
	   		//ok=200
	   		final Rs232Settings savedSettings = rs232Repo.save(rs232Settings);
	   		return new ResponseEntity<>(savedSettings, HttpStatus.OK);
	   	}  
	}
	
//	/**
//	 * This method is used to retrieve list of Rs232settings
//	 * @return list of active Rs232settings.
//	 */  
//	public List<Rs232Settings> getRs232Settings() {      	
//		return rs232Repo.findAll();
//	}	
	
	  /**
     * This method is used to delete Rs232Settings entry based on InstrumentMaster entity.
     * @param master [InstrumentMaster] entity for which Rs232settings is to be deleted
     * @return response object relevant to status of transaction completion.
     */
	 public ResponseEntity<Object> deleteByInstrument(InstrumentMaster master)
	 {
    	final Optional<Rs232Settings> rs232Settings = rs232Repo.findByInstmaster(master);
    	if (rs232Settings.isPresent())
    	{
    		rs232Repo.delete(rs232Settings.get());
    	}
    	
    	return new ResponseEntity<>(HttpStatus.OK);	    	
	 }

}
