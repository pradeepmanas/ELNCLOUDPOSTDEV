package com.agaram.eln.primary.repository.methodsetup;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.methodsetup.Delimiter;

/**
 * This interface holds JpaRepository method declarations relevant to Delimiters.
 * @author ATE153
 * @version 1.0.0
 * @since   07- Feb- 2020
 */
@Repository
public interface DelimiterRepository extends JpaRepository<Delimiter, Integer>{

	/**
	 * This interface declaration is used to retrieve list of all delimiters based on status.
	 * @param status [int] 1 - Active or -1- Inactive
	 * @param sort [Sort] to sort the retrieved list based on 'field' and 'sort direction' provided through
	 *             value=Sort.by(Sort.Direction.DESC, "delimiterkey")
	 * @return list of available delimiters 
	 */
	List<Delimiter> findByStatus(final int status, final Sort sort);
	
	/**
	 * This interface declaration is used to fetch details of delimiters based on its 
	 * delimitername and status
	 * @param delimiterName [String] name of the delimiter
	 * @param status  [int] 1 - Active or -1- Inactive
	 * @return delimiter entity based on delimitername
	 */
	Optional<Delimiter> findByDelimiternameAndStatus(final String delimiterName, final int status);
	
	/**
	 * This interface declaration is used to fetch details of delimiters based on its 
	 * primary key and status
	 * @param delimterKey [int] primary key of delimiters object
	 * @param status [int] 1 - Active or -1- Inactive
	 * @return delimiter entity based on delimiterkey
	 */
	Optional<Delimiter> findByDelimiterkeyAndStatus(final int delimterKey, final int status);
}
