package com.agaram.eln.primary.repository.instrumentDetails;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.LSOrderElnMethod;
import com.agaram.eln.primary.model.sheetManipulation.LSfileelnmethod;

public interface LSOrderElnMethodRepository extends JpaRepository<LSOrderElnMethod, Integer> {

	

}
