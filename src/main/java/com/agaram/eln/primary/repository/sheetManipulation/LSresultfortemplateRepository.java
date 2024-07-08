package com.agaram.eln.primary.repository.sheetManipulation;

import org.springframework.data.jpa.repository.JpaRepository;
import com.agaram.eln.primary.model.sheetManipulation.Lsresultfortemplate;

public interface LSresultfortemplateRepository  extends JpaRepository<Lsresultfortemplate, Long>{
	Lsresultfortemplate findById(long id);
}
