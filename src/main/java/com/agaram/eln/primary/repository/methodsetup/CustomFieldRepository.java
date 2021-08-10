package com.agaram.eln.primary.repository.methodsetup;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.methodsetup.CustomField;
import com.agaram.eln.primary.model.methodsetup.ControlType;
import com.agaram.eln.primary.model.methodsetup.Method;

/**
 * This interface holds JpaRepository method declarations relevant to CustomField.
 * @author ATE153
 * @version 1.0.0
 * @since   01- Apr- 2020
 */
@Repository
public interface CustomFieldRepository extends JpaRepository<CustomField, Integer>{
	
	/**
	 * This interface declaration is used to retrieve list of active custom fields based on Method.
	 * @param method [Method] Object for which custom fields are to be fetched.
	 * @param status [int] 1 - Active or -1- Inactive
	 * @return list of active custom fields associated for the Method
	 */
    List<CustomField> findByMethodAndStatus(final Method method, final int status);
	
    /**
     * This interface declaration is used to fetch CustomField entity based on specified ControlType, field name,
     * Method and status.
     * @param controlType [ControlType] object holding datatype of the custom field.
     * @param customFieldName [String] name of the custom field
     * @param method [Method] object for which the custom field is created 
     * @param status [int] 1 - Active or -1- Inactive
     * @return active entity relating to ControlType, field name and Method .
     */
	Optional<CustomField> findByControltypeAndCustomfieldnameAndMethodAndStatus(final ControlType controlType, 
			final String customFieldName, final Method method, final int status);
	
	/**
	 * This interface declaration is used to fetch CustomField entity based on its primary key and status.
	 * @param customFieldKey [int] primary key of CustomField Entity
	 * @param status [int] 1 - Active or -1- Inactive
	 * @return active entity relating to primary key.
	 */
	Optional<CustomField> findByCustomfieldkeyAndStatus(final int customFieldKey, final int status);
	
	/**
	 * This interface declaration is used to retrieve list of active custom fields based on specified site.
	 * @param sitekey [int] primary key of site object for which custom fields are to be fetched
	 * @param sort [Sort] to sort the retrieved list based on 'field' and 'sort direction' provided through
	 *             value=Sort.by(Sort.Direction.DESC, "customfieldkey")
	 * @return list of active custom fields available for the site
	 */
//	@Query(value="select cf from CustomField cf JOIN Method m on m.methodkey = cf.method.methodkey "
//			+ " JOIN InstrumentMaster im ON im.instmastkey = m.instmaster.instmastkey JOIN Site s "
//			+ " ON s.sitekey = im.site.sitekey where s.sitekey =?1 and cf.status= 1")
//	List<CustomField> getCustomFieldbySite(final int sitekey, final Sort sort);

}
