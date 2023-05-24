package com.agaram.eln.primary.repository.methodsetup;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.methodsetup.AppMaster;
import com.agaram.eln.primary.model.methodsetup.GeneralField;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

/**
 * This interface holds JpaRepository method declarations relevant to GeneralField.
 * @author ATE153
 * @version 1.0.0
 * @since   01- Apr- 2020
 */
@Repository
public interface GeneralFieldRepository extends JpaRepository<GeneralField, Integer>{
	
	/**
	 * This interface declaration is used to retrieve list of active general fields based on Site sorted by its
	 * primary key
	 * @param site [Site] site object for which general fields are to be fetched
	 * @param status [int] 1 - Active or -1- Inactive
	 * @param sort [Sort] to sort the retrieved list based on 'field' and 'sort direction' provided through
	 *             value=Sort.by(Sort.Direction.DESC, "generalfieldkey")
	 * @return list of active general fields based on Site sorted by its primary key
	 */
	List<GeneralField> findBySiteAndStatus(final LSSiteMaster site, final int status, final Sort sort);
	
	/**
	 * This interface declaration is used to retrieve active GeneralField entity based on AppMaster,
	 * fieldname and Site .
	 * @param appMaster [AppMaster] object based on which the general field is to be fetched
	 * @param generalFieldName [String] matched entity to be fecthed
	 * @param site [Site] site object for which GeneralFeld  entity is to be fetched
	 * @param status [int] 1 - Active or -1- Inactive
	 * @return active GeneralField Entity based on specified AppMaster, fieldname and Site 
	 */
	Optional<GeneralField> findByAppmasterAndGeneralfieldnameAndSiteAndStatus(final AppMaster appMaster, 
			final String generalFieldName, final LSSiteMaster site, final int status);
	
	/**
	 * This interface declaration is used to retrieve active GeneralField entity based on its primary key 
	 * @param generalFieldKey [int] primary key of GeneralField Entity
	 * @param status [int] 1 - Active or -1- Inactive
	 * @return active GeneralField Entity based on its primary key
	 */
	Optional<GeneralField> findByGeneralfieldkeyAndStatus(final int generalFieldKey, final int status);

}
