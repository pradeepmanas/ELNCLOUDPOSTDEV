package com.agaram.eln.primary.repository.methodsetup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.methodsetup.SampleTextSplit;
import com.agaram.eln.primary.model.methodsetup.Method;


/**
 * This interface holds JpaRepository method declarations relevant to SampleTextSplit.
 * @author ATE153
 * @version 1.0.0
 * @since   13- Feb- 2020
 */
@Repository
public interface SampleTextSplitRepository extends JpaRepository<SampleTextSplit, Integer>{

	/**
	 * This interface declaration is used to fetch SampleTextSplit Entities based on 'Method' and its
	 * status
	 * @param method [Method] object for which list is to be fetched
	 * @param status [int] '1'-Active, '-1'-Inactive
	 * @return list of SampleTextSplit Entities
	 */
	List<SampleTextSplit> findByMethodAndStatus(final Method method, final int status);
	
	/**
	 * This interface declaration is used to fetch SampleTextSplit Entities based on the primary keys and its
	 * status
	 * @param sampleTextSplitKeyList [List] primary keys of SampleTextSplit entities
	 * @param status [int] '1'-Active, '-1'-Inactive
	 * @return list of SampleTextSplit Entities
	 */
	List<SampleTextSplit> findBySampletextsplitkeyInAndStatus(final List<Integer> sampleTextSplitKeyList, final int status);
}
