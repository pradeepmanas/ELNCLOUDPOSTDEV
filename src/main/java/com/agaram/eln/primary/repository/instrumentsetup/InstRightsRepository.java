package com.agaram.eln.primary.repository.instrumentsetup;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.instrumentsetup.InstRightsEssential;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentRights;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

/**
 * This interface holds JpaRepository method declarations relevant to InstrumentRights.
 * @author ATE153
 * @version 1.0.0
 * @since   08- Nov- 2019
 */
@Repository
public interface InstRightsRepository extends JpaRepository<InstrumentRights, Integer>{
	
	/**
	 * This interface declaration is used to fetch  Instrument rights based
	 * on status.
	 * @param status [int] 1- Active, -1 -Inactive
	 * @return list of instrument rights
	 */
	List<InstrumentRights> findByStatus(final int status);
	
	/**
	 * This interface declaration is used to fetch  Instrument rights based
	 * on usersite.
	 * @param userSite [UserSite] object holding user of the site to
	 * 			whom the instrument rights are to be fetched
	 * @return list of instrument rights
	 */
	List<InstrumentRights> findByUsersite(final LSSiteMaster userSite);
	
	/**
	 * This interface declaration is used to fetch  the list of all instruments available
	 * in the site along with the specified usersite's instrument assigned status.
	 * @param usersitekey [int] primary key of usersite object
	 * @param sitekey [int] primary key of site object
	 * @return list of instrument rights assigned to the selected user of the site
	 */
//	@Query(value=" select new com.agaram.lleln.instrumentsetup.instrumentrights.InstRightsEssential(im as master,ir.instrightskey,ir.status) "
//		     + " from InstrumentMaster im  left join InstrumentRights ir" 
//			 + " on im.instmastkey = ir.master.instmastkey and ir.usersite.usersitekey=?1 where im.status=1 and im.site.sitekey=?2")
//	List<InstRightsEssential> getInstRightsByUserSite(final int usersitekey, final int sitekey);
		
	/**
	 * This interface declaration is used to fetch instrument association status for the specified instrument 
	 * and the site user.
	 * @param master [InstrumentMaster] entity for which rights detailis to be fetched
	 * @param userSite [UserSite] entity for whom rights detail is to be fetched
	 * @return instrument association status with the specified instrument
	 */
	Optional<InstrumentRights> findByMasterAndUsersite(final InstrumentMaster master, final LSSiteMaster userSite);
	
	/**
	 * The interface declaration is used to fetch list of instrument rights based on its primary keys.
	 * @param instRightsKeyList [List] list of primary keys of instrumentrights for which the
	 * 							details are to be fetched.
	 * @return list of instrument's rights based on primary keys.
	 */
	List<InstrumentRights> findByInstrightskeyIn(final List<Integer> instRightsKeyList);
	
}

