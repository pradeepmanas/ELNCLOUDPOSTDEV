package com.agaram.eln.primary.repository.methodsetup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.methodsetup.ControlType;

/**
 * This interface holds JpaRepository method declarations relevant to ControlType.
 * @author ATE153
 * @version 1.0.0
 * @since   01- Apr- 2020
 */
@Repository
public interface ControlTypeRepository extends JpaRepository<ControlType, Integer>{

}
