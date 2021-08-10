package com.agaram.eln.primary.repository.instrumentsetup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.instrumentsetup.InstrumentType;

/**
 * This interface holds JpaRepository method declarations relevant to InstrumentType.
 * @author ATE153
 * @version 1.0.0
 * @since   08- Nov- 2019
 */
@Repository
public interface InstTypeRepository extends JpaRepository<InstrumentType, Integer>{
	
}
