package com.agaram.eln.primary.repository.instrumentsetup;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.instrumentsetup.FileSettings;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;

/**
 * This interface holds JpaRepository method declarations relevant to FileSettings.
 * @author ATE153
 * @version 1.0.0
 * @since   08- Nov- 2019
 */
@Repository
public interface FileSettingsRepository extends JpaRepository<FileSettings, Integer> {
    
	/**
	 * This interface declaration is used to fetch FileSettings for the
	 * specified instrument.
	 * @param instMaster [InstrumentMaster] entity for which result is
	 * 			to be fetched
	 * @return filesettings object
	 */
    Optional<FileSettings> findByInstmaster(final InstrumentMaster instMaster);   

}
