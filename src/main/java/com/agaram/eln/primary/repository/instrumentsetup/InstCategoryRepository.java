package com.agaram.eln.primary.repository.instrumentsetup;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.instrumentsetup.InstrumentCategory;

/**
 * This interface holds JpaRepository method declarations relevant to InstrumentCategory.
 * @author ATE153
 * @version 1.0.0
 * @since   08- Nov- 2019
 */
@Repository
public interface InstCategoryRepository extends JpaRepository<InstrumentCategory, Integer>{
	
	/**
	 * This interface declaration is used to retrieve list of instrument categories based on
	 * status.
	 * @param status [int] 1 or -1
	 * @param sort  [Sort] to sort the retrieved list based on 'field' and 'sort direction' provided through
	 *             value=Sort.by(Sort.Direction.DESC, "instcatkey")
	 * @return list of instrument categories
	 */
	List<InstrumentCategory> findByStatus(final int status,  Sort sort);
	
	/**
	 * This declaration is used to fetch specific InstrumentCategory based on its category name
	 * and status.
	 * @param categoryName [String] name of instrument category
	 * @param status  [int] 1 or -1
	 * @return instrument category based on category name
	 */
	Optional<InstrumentCategory> findByInstcatnameAndStatus(final String categoryName, final int status);

}
