package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sheetManipulation.LSfiletest;

public interface LSfiletestRepository  extends JpaRepository<LSfiletest, Integer>{
	public List<LSfiletest> findByTestcodeAndTesttype(Integer testcode, Integer testtype);
}
