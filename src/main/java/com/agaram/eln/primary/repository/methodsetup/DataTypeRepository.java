package com.agaram.eln.primary.repository.methodsetup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.methodsetup.DataType;

/**
 * This interface holds JpaRepository method declarations relevant to ParserBlock.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Mar - 2020
 */
@Repository
public interface DataTypeRepository extends JpaRepository<DataType, Integer>{


}
