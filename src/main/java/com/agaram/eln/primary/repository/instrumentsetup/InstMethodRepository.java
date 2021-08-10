package com.agaram.eln.primary.repository.instrumentsetup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.instrumentsetup.InstMethod;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;


/**
 * This interface holds JpaRepository method declarations relevant to InstMethod.
 * @author ATE153
 * @version 1.0.0
 * @since   07- May- 2020
 */
@Repository
public interface InstMethodRepository extends JpaRepository<InstMethod, Integer>{
	
	/**
	 * This interface declaration is used to retrieve list of active methods associated with
	 * the specified InstrumentMaster entity 
	 * @param instMaster [InstrumentMaster] entity for which method association is to be fetched
	 * @param status [int] 1 - Active or -1- Inactive
	 * @return  list of instruments associated with the InstMethod entity
	 */
	List<InstMethod> findByInstmasterAndStatus(final InstrumentMaster instMaster, final int status);
}