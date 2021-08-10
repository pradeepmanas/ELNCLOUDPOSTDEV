package com.agaram.eln.primary.repository.instrumentsetup;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.instrumentsetup.TcpSettings;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;

/**
 * This interface holds JpaRepository method declarations relevant to TcpSettings.
 * @author ATE153
 * @version 1.0.0
 * @since   08- Nov- 2019
 */
@Repository
public interface TcpSettingsRepository extends JpaRepository<TcpSettings, Integer> {
    
	/**
	 * This interface declaration is used to fetch TcpSettings for the
	 * specified instrument.
	 * @param instMaster [InstrumentMaster] entity for which result is
	 * 			to be fetched
	 * @return tcpsettings object
	 */
    Optional<TcpSettings> findByInstmaster(final InstrumentMaster instMaster); 

}

