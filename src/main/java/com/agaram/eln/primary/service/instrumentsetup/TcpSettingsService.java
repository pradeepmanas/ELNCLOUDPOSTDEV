package com.agaram.eln.primary.service.instrumentsetup;

import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.instrumentsetup.TcpSettings;
import com.agaram.eln.primary.repository.instrumentsetup.InstMasterRepository;
import com.agaram.eln.primary.repository.instrumentsetup.TcpSettingsRepository;

/**
 * This Service class is used to access the TcpSettingsRepository to fetch details
 * from the 'tcpsettings' table through TcpSettings Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   08- Nov- 2019
 */
@Service
public class TcpSettingsService {
	
	@Autowired
	TcpSettingsRepository tcpRepo;

	@Autowired
	InstMasterRepository masterRepo;

	/**
	 * This method is used to add new entry of instrument master to tcp settings
	 * @param tcpSettings [TcpSettings] -  holding details of all fields in 'TcpSettings' Pojo.
	 * @return newly added TcpSettings object with status Message
	 */
	public ResponseEntity<Object> createTcpSettings(TcpSettings tcpSettings) {  		
		
		final TcpSettings saveSettings = tcpRepo.save(tcpSettings);
   		return new ResponseEntity<>(saveSettings, HttpStatus.OK);
	}

	/**
	 * This method is used to update the selected 'Tcpsettings' details based on provided 
	 * primary key id and details of the  updated fields.
	 * @param tcpMastKey [int] 'Tcpsettings' primary key to update
	 * @param tcpSettings Object with details to update
	 * @param instMastKey [int] primary key of InstrumentMaster
	 * @return updated 'tcpsettings' object
	 */
	public ResponseEntity<Object> updateTcpSettings(TcpSettings tcpSettings,
			final Integer tcpMastKey, final int instMastKey) {
		
	    TcpSettings searchEntity = tcpRepo.findOne(tcpMastKey);
	
	    if (searchEntity != null) {
	    	final TcpSettings tcpObj = searchEntity;
	    	
	    	final InstrumentMaster instMaster = masterRepo.findOne(instMastKey);
	    	
	    	tcpObj.setInstmaster(instMaster);   
	    	
	    	tcpObj.setConnectmode(tcpSettings.getConnectmode());
	    	tcpObj.setProtocol(tcpSettings.getProtocol());
	    	tcpObj.setRemotehostip(tcpSettings.getRemotehostip());
	    	tcpObj.setRemoteport(tcpSettings.getRemoteport());
	    	tcpObj.setRetrycount(tcpSettings.getRetrycount());
	    	tcpObj.setServerport(tcpSettings.getServerport());
	    	tcpObj.setTimeout(tcpSettings.getTimeout());
	    	
	    	return saveEntity(tcpObj);     	
	    } 
	    else {
	        throw new EntityNotFoundException();
	    }
	}
	
	/**
	 * This method is used to check for duplicate entry of TCPsettings based on 'instrument master'
	 * while add and update.
	 * @param tcpSettings [TCPSettings] entity with all its fields
	 * @return response status of add and update
	 */
	private ResponseEntity<Object> saveEntity(final TcpSettings tcpSettings)
	{
		final Optional<TcpSettings> tcpSettingsByInstrument = tcpRepo.findByInstmaster(tcpSettings.getInstmaster());
	 	
	    if(tcpSettingsByInstrument.isPresent())
	    {
	       	//Update loop
	   		if ( tcpSettingsByInstrument.get().getTcpmastkey() == tcpSettings.getTcpmastkey())
	   		{ // Updating rest of the fields while update
	   			//ok=200
	   			final TcpSettings saveSettings = tcpRepo.save(tcpSettings);
	       		return new ResponseEntity<>(saveSettings, HttpStatus.OK);
	   		}
	   		else
	   		{ 	
	   			//Conflict =409 - Duplicate entry
	   			return new ResponseEntity<>(tcpSettings.getInstmaster().getInstrumentcode(), HttpStatus.CONFLICT);
	   		}
	   	}
	   	else
	   	{
	   		//new entry loop
	   		//ok=200
	   		final TcpSettings savedSettings = tcpRepo.save(tcpSettings);
	   		return new ResponseEntity<>(savedSettings, HttpStatus.OK);
	   	}  
	}
	
//	/**
//	 * This method is used to retrieve list of Tcpsettings
//	 * @return list of active Tcpsettings.
//	 */  
//	public List<TcpSettings> getTcpSettings() {      	
//		return tcpRepo.findAll();
//	}	
//	
	  /**
     * This method is used to delete tcpSettings entry based on InstrumentMaster entity.
     * @param master [InstrumentMaster] entity for which tcpsettings is to be deleted
     * @return response object relevant to status of transaction completion.
     */
	 public ResponseEntity<Object> deleteByInstrument(InstrumentMaster master)
	 {
	    final Optional<TcpSettings> tcpSettings = tcpRepo.findByInstmaster(master);
	    if (tcpSettings.isPresent())
	    {
	    	tcpRepo.delete(tcpSettings.get());
	    }
	    	
	    return new ResponseEntity<>(HttpStatus.OK);	    	
	 }

}