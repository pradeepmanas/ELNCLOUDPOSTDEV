package com.agaram.eln.primary.service.instrumentsetup;

import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.repository.instrumentsetup.FileSettingsRepository;
import com.agaram.eln.primary.repository.instrumentsetup.InstMasterRepository;
import com.agaram.eln.primary.model.instrumentsetup.FileSettings;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;

/**
 * This Service class is used to access the FileSettingsRepository to fetch details
 * from the 'filesettings' table through FileSettings Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   08- Nov- 2019
 */
@Service
public class FileSettingsService {
	
	@Autowired
	FileSettingsRepository fileRepo;
	
	@Autowired
	InstMasterRepository masterRepo;
	
	/**
     * This method is used to add new entry of instrument master to file settings
     * @param fileSettings FileSettings -  holding details of all fields in 'FileSettings' Pojo.
     * @return newly added fileSettings object with status Message
     */
    public ResponseEntity<Object> createFileSettings(FileSettings fileSettings) {
   	
    	final FileSettings savedFile = fileRepo.save(fileSettings);
   		return new ResponseEntity<>(savedFile, HttpStatus.OK);
    }
    
    /**
     * This method is used to update the selected 'filesettings' details based on provided 
     * primary key id and details of the  updated fields.
     * @param fileMastKey [int] 'filesettings' primary key to update
     * @param fileSettings [FileSettings] Object with details to update
     * @param instMastKey [int] primary key of InstrumentMaster
     * @return updated 'filesettings' object
     */
    public ResponseEntity<Object> updateFileSettings(FileSettings fileSettings,
    		final Integer fileMastKey, final int instMastKey) {
    	
        FileSettings searchEntity = fileRepo.findOne(fileMastKey);
    
        if (searchEntity != null) {
        	final FileSettings fileObj = searchEntity;
        	
        	final InstrumentMaster instMaster = masterRepo.findOne(instMastKey);
        	
        	fileObj.setInstmaster(instMaster);
        	fileObj.setFileinterval(fileSettings.getFileinterval());
        	fileObj.setFilemode(fileSettings.getFilemode());        	
        	
        	return saveEntity(fileObj);     	
        } 
        else {
            throw new EntityNotFoundException();
        }
    }
    
    /**
     * This method is used to check for duplicate entry of filesettings based on 'instrument master'
     * while add and update.
     * @param fileSettings [FileSettings] entity with all its fields
     * @return response status of add and update
     */
    private ResponseEntity<Object> saveEntity(final FileSettings fileSettings)
    {
    	final Optional<FileSettings> fileSettingsByInstrument = fileRepo.findByInstmaster(fileSettings.getInstmaster());
     	
	    if(fileSettingsByInstrument.isPresent())
	    {
	       	//Update loop
       		if ( fileSettingsByInstrument.get().getFilemastkey() == fileSettings.getFilemastkey())
       		{ // Updating rest of the fields while update
       			//ok=200
       			final FileSettings savedFile = fileRepo.save(fileSettings);
	       		return new ResponseEntity<>(savedFile, HttpStatus.OK);
       		}
       		else
       		{ 	
       			//Conflict =409 - Duplicate entry
       			return new ResponseEntity<>(fileSettings.getInstmaster().getInstrumentcode(), HttpStatus.CONFLICT);
       		}
       	}
       	else
       	{
       		//new entry loop
       		//ok=200
       		final FileSettings savedFile = fileRepo.save(fileSettings);
       		return new ResponseEntity<>(savedFile, HttpStatus.OK);
       	}  
    }
    
//    /**
//     * This method is used to retrieve list of filesettings
//     * @return list of active filesettings.
//     */  
//    public List<FileSettings> getFileSettings() {      	
//    	return fileRepo.findAll();
//    }
    
//    /**
//     * This method is used to retrieve list of filesettings based on Instrument
//     * @return Instrument related filesettings.
//     */  
//    public List<FileSettings> getFileSettings(InstrumentMaster master) {      	
//    	return fileRepo.findByInstmaster(master);
//    }

    /**
     * This method is used to delete FileSettings entry based on InstrumentMaster entity.
     * @param master [InstrumentMaster] entity for which filesettings is to be deleted
     * @return response object relevant to status of transaction completion.
     */
    public ResponseEntity<Object> deleteByInstrument(InstrumentMaster master)
    {
    	final Optional<FileSettings> fileSettings = fileRepo.findByInstmaster(master);
    	if (fileSettings.isPresent()) {
    		fileRepo.delete(fileSettings.get());
    	}
    	return new ResponseEntity<>(HttpStatus.OK);
    	
    }
}
