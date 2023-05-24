package com.agaram.eln.primary.repository.methodsetup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.methodsetup.SampleLineSplit;
import com.agaram.eln.primary.model.methodsetup.Method;


/**
 * This interface holds JpaRepository method declarations relevant to SampleLineSplit.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Feb- 2020
 */
@Repository
public interface SampleLineSplitRepository extends JpaRepository<SampleLineSplit, Integer>{

	/**
	 * This interface declaration is used to fetch SampleLineSplit Entities based on 'Method' and its
	 * status
	 * @param method [Method] object for which list is to be fetched
	 * @param status [int] '1'-Active, '-1'-Inactive
	 * @return list of SampleLineSplit Entities
	 */
	List<SampleLineSplit> findByMethodAndStatus(final Method method, final int status);
	
	/**
	 * This interface declaration is used to fetch SampleLineSplit Entities based on the primary keys and its
	 * status
	 * @param sampleLineSplitKeyList [List] primary keys of SampleLineSplit entities
	 * @param status [int] '1'-Active, '-1'-Inactive
	 * @return list of SampleLineSplit Entities
	 */
	List<SampleLineSplit> findBySamplelinesplitkeyInAndStatus(final List<Integer> sampleLineSplitKeyList, final int status);
}
