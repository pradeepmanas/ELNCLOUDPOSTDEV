package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sheetManipulation.LStestparameter;

public interface LStestparameterRepository  extends JpaRepository<LStestparameter, Integer>{
	public List<LStestparameter> findByntestcode(Integer testcode);
}
