package com.agaram.eln.primary.repository.methodsetup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.methodsetup.ParserIgnoreChars;

/**
 * This interface holds JpaRepository method declarations relevant to ParserIgnoreChars.
 * @author ATE153
 * @version 1.0.0
 * @since   01- May- 2020
 */
@Repository
public interface ParserIgnoreCharsRepository extends JpaRepository<ParserIgnoreChars, Integer>{

}
