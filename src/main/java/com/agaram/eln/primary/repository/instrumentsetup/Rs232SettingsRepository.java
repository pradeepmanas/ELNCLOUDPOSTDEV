package com.agaram.eln.primary.repository.instrumentsetup;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.instrumentsetup.Rs232Settings;

/**
 * This interface holds JpaRepository method declarations relevant to Rs232Settings.
 * @author ATE153
 * @version 1.0.0
 * @since   08- Nov- 2019
 */
@Repository
public interface Rs232SettingsRepository extends JpaRepository<Rs232Settings, Integer> {
    
	/**
	 * This interface declaration is used to fetch Rs232Settings for the
	 * specified instrument.
	 * @param instMaster [InstrumentMaster] entity for which result is
	 * 			to be fetched
	 * @return rs232 settings object
	 */
    Optional<Rs232Settings> findByInstmaster(final InstrumentMaster instMaster); 

}
