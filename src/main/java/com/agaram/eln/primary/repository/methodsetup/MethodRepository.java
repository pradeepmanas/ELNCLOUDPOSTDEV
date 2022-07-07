package com.agaram.eln.primary.repository.methodsetup;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

/**
 * This interface holds JpaRepository method declarations relevant to Method.
 * @author ATE153
 * @version 1.0.0
 * @since   07- Feb- 2020
 */
@Repository
public interface MethodRepository extends JpaRepository<Method, Integer>{
	
	/**
	 * This interface declaration is used to retrieve list of all methods based on Site and status.
	 * @param site [Site] Object for which method list is to be fetched
	 * @param status [int] 1 - Active or -1- Inactive
	 * @param sort [Sort] to sort the retrieved list based on 'field' and 'sort direction' provided through
	 *             value=Sort.by(Sort.Direction.DESC, "methodkey")
	 * @return list of methods available in the site
	 */
	List<Method> findBySiteAndStatus(final LSSiteMaster site, final int status, final Sort sort);
	
	/**
	 * This interface declaration is used to retrieve method entity based on Site, method name and status.
	 * @param site [Site] Object for which method object is to be fetched
	 * @param methodName [String] name of the method
	 * @param status  [int] 1 - Active or -1- Inactive
	 * @return method entity based on site, name and status
	 */
	Optional<Method> findBySiteAndMethodnameAndStatus(final LSSiteMaster site, final String methodName, final int status);
	
	/**
	 * This interface declaration is used to retrieve method entity based on its primary key status.
	 * @param methodKey [int] primary key of method object
	 * @param status  [int] 1 - Active or -1- Inactive
	 * @return method entity based on primary key and status
	 */
	Optional<Method> findByMethodkeyAndStatus(final int methodKey, final int status);
	
	/**
	 * This interface declaration is used to fetch list of active instruments that are not yet assigned
	 * with the method.
	 * @param siteKey [int] primary key of site object for which the instruments are to be
	 * 	fetched
	 * @return list of unassociated instruments 
	 */
//	@Query(value = "SELECT im FROM InstrumentMaster im LEFT JOIN Method m "
//			      + " ON im.instmastkey = m.instmaster.instmastkey  "
//			      + " AND im.status=1 and m.status =1 where "
//			      + " m.instmaster.instmastkey is null AND im.site.sitekey = ?1")
//	List<InstrumentMaster> getInstListToAssociateMethod(final int siteKey);
	
	/**
	 * This interface declaration is used to find list of Method entities in the specified site
	 * for which parsing is done.
	 * @param parser [int] 1- parsing done, null - parsing  not yet done, 0 - parsing has been
	 * deleted.
	 * @param site [Site] object for which the methods are to be fetched
	 * @param status  [int] 1 - Active or -1- Inactive
	 * @return list of Method entities for the specified inputs
	 */
	List<Method> findByParserAndSiteAndStatus(final int parser, final LSSiteMaster site, final int status);
	
	/**
	 * This method is used to fetch active Method Entity based on its name and InstrumentMaster.
	 * @param methodName [String] name of the Method which is to be fetched
	 * @param instMaster [InstrumentMaster] object for which the Method is to fetched
	 * @param status  [int] 1 - Active or -1- Inactive
	 * @return Method entity matching specified inputs.
	 */
	Optional<Method> findByMethodnameAndInstmasterAndStatus(final String methodName, final InstrumentMaster instMaster, final int status);

	List<Method> findByMethodnameContainingAndInstmasterAndStatus(final String methodName, final InstrumentMaster instMaster, final int status);

//	Optional<Method> findBySiteMethodnameAndInstmasterAndStatus(final Site site, final String methodName, final InstrumentMaster instMaster, final int status);

	List<Method> findByMethodkey(final int methodKey);

	List<Method> findByStatus(int i);
}
