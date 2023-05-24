package com.agaram.eln.primary.repository.sheetManipulation;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sheetManipulation.LStestmaster;

public interface LStestmasterRepository extends JpaRepository<LStestmaster, Integer>{

	public LStestmaster findByntestcode(Integer ntestcode);
	public LStestmaster findBystestname(Integer ntestcode);
	
}
