package com.agaram.eln.primary.repository.methodsetup;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.methodsetup.MethodDelimiter;
import com.agaram.eln.primary.model.methodsetup.Delimiter;
import com.agaram.eln.primary.model.methodsetup.ParserMethod;

/**
 * This interface holds JpaRepository MethodDelimiter declarations relevant to Method.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Mar- 2020
 */
@Repository
public interface MethodDelimiterRepository extends JpaRepository<MethodDelimiter, Integer>{
	
	/**
	 * This interface declaration is used to get list of MethodDelimiter entities based on its status.
	 * @param status [int] 1 - Active or -1- Inactive
	 * @param sort [Sort] to sort the retrieved list based on 'field' and 'sort direction' provided through
	 *             value=Sort.by(Sort.Direction.DESC, "methoddelimiterkey")
	 * @return list of MethodDelimiter entities
	 */
	List<MethodDelimiter> findByStatus(final int status, final Sort sort);
	
	/**
	 * This interface declaration is used to retrieve MethodDelimiter entity based on ParserMethod, Delimiter
	 * and status
	 * @param parserMethod [ParserMethod] entity based on which record is to be fetched
	 * @param delimiter [Delimiter] entity based on which record is to be fetched
	 * @param status  [int] 1 - Active or -1- Inactive
	 * @return MethodDelimiter entity based on specified input
	 */
	Optional<MethodDelimiter> findByParsermethodAndDelimiterAndStatus(final ParserMethod parserMethod,
			final Delimiter delimiter, final int status);
	
	/**
	 * This interface declaration is used to retrieve MethodDelimiter entity based on its primary key
	 * and status
	 * @param methodDelimiterKey [int] primary key of MethodDelimiter entity
	 * @param status  [int] 1 - Active or -1- Inactive
	 * @return MethodDelimiter entity based on specified input
	 */
	Optional<MethodDelimiter> findByMethoddelimiterkeyAndStatus(final int methodDelimiterKey, final int status);

	/**
	 * This interface declaration is used to retrieve list of MethodDelimiter entities based on Delimiter
	 * and status 
	 * @param delimiter  [Delimiter] entity based on which record is to be fetched
	 * @param status  [int] 1 - Active or -1- Inactive
	 * @return list of MethodDelimiter entities
	 */
	List<MethodDelimiter> findByDelimiterAndStatus(final Delimiter delimiter, final int status);
}
