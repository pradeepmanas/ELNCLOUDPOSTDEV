package com.agaram.eln.primary.repository.methodsetup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.methodsetup.ParserMethod;

/**
 * This interface holds JpaRepository method declarations relevant to ParserMethod.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Mar- 2020
 */
@Repository
public interface ParserMethodRepository extends JpaRepository<ParserMethod, Integer>{

	/**
	 * This interface declaration is used to fetch list of ParserMethod entities based on
	 * its field 'parsermethodtype'
	 * @param parserMethodType [int] 1- Parser Method, 2 -SubParser Method
	 * @return list of ParserMethod entities
	 */
	List<ParserMethod> findByParsermethodtype(final int parserMethodType);
}
