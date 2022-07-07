package com.agaram.eln.primary.repository.instrumentsetup;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.instrumentsetup.InstrumentCategory;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

/**
 * This interface holds JpaRepository method declarations relevant to InstrumentMaster.
 * @author ATE153
 * @version 1.0.0
 * @since   08- Nov- 2019
 */
@Repository
public interface InstMasterRepository extends JpaRepository<InstrumentMaster, Integer>{
	
	/**
	 * This interface declaration is used to  retrieve list of instruments based on site and status
	 * through "InstrumentMaster' entity.
	 * @param status [int] 1 or -1
	 * @param site [Site] Entity for which the instruments are to be fetched
	 * @param sort  [Sort] to sort the retrieved list based on 'field' and 'sort direction' provided through
	 *             value=Sort.by(Sort.Direction.DESC, "instmastkey")
	 * @return list of instruments for a specific site
	 */
	 List<InstrumentMaster> findByStatusAndSite(final int status, final LSSiteMaster site, final Sort sort);
	 
	 List<InstrumentMaster> findByStatusAndSiteOrderByInstmastkeyDesc(final int status, final LSSiteMaster site);
	
	 /**
	  * This interface declaration is used to fetch the instrument detail based on instrument code ans site
	  * and status.
	  * @param instCode [String] code for which the details is to be fetched
	  * @param site [Site] Entity for which the instrument is to be fetched
	  * @param status [int] 1 or -1
	  * @return instrument for a specified instrument code and site and status
	  */
	 Optional<InstrumentMaster> findByInstrumentcodeAndSiteAndStatus(final String instCode, final LSSiteMaster site, final int status);

	 /**
	  * This interface declaration is used to fetch the count of number of users assigned with the
	  * specified instrument in the site excluding the 'Administrator' user.
	  * @param instMastKey [int] primary key of instrument for which the result is to be fetched
	  * @param siteKey [int] primary key of site for which the result is to be fetched
	  * @return number of users assigned with the instrument. 
	  */
//	 @Query(value = "select count(*) from InstrumentMaster im  inner join InstrumentRights ir " + 
//	 		"	on im.instmastkey = ir.master.instmastkey and im.instmastkey = ?1 " + 
//	 		"	inner join UserSite us on us.usersitekey = ir.usersite.usersitekey  "+
//	 		"   inner join Users u on u.userkey = us.users.userkey and us.users.userkey !=1 " + 
//	 		"	where im.status=1 and im.site.sitekey=?2 and ir.status=1")
//	 Integer getAdminExcludedAssignedInstrumentsCount(final int instMastKey, final int siteKey);
	 
	 /**
	  * This interface declaration is used to fetch list of instruments available under the specified instrument
	  * category.
	  * @param instCategory [InstrumentCategory] entity for which the instruments are to be fetched
	  * @param status [int] 1 or -1
	  * @return list of instruments 
	  */
	 List<InstrumentMaster> findByInstcategoryAndStatus(final InstrumentCategory instCategory, final int status);
	 
	 /**
	  * This interface declaration is used to fetch of list of instrument details based on its
	  * primary key.
	  * @param instMastKeyList [List] list of primary keys of instruments.
	  * @return list of instruments 
	  */
	 List<InstrumentMaster> findByInstmastkeyIn(final List<Integer> instMastKeyList);
	 
	 /**
	  * This interface declaration is used to fetch list of instruments available under the specified instrument
	  * category and site.
	  * @param instCategory [InstrumentCategory] entity for which the instruments are to be fetched
	  * @param site [Site] Object for which the result is to be fetched
	  * @param status [int] 1 or -1
	  * @return list of instruments 
	  */
	 List<InstrumentMaster> findBySiteAndInstcategoryAndStatus(final LSSiteMaster site, final InstrumentCategory instCategory, final int status);

	List<InstrumentMaster> findByStatusAndSite(int i, LSSiteMaster site);

	Optional<InstrumentMaster> findByInstmastkeyAndSiteAndStatus(Integer instmastkey, LSSiteMaster site, int i);

	Object findByStatusAndSite(int i, Sort sort);

	List<InstrumentMaster> findByStatus(int i);

}
