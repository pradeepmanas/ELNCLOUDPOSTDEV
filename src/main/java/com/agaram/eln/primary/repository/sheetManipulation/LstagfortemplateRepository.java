package com.agaram.eln.primary.repository.sheetManipulation;

import org.springframework.data.jpa.repository.JpaRepository;
import com.agaram.eln.primary.model.sheetManipulation.Lstagfortemplate;

public interface LstagfortemplateRepository extends JpaRepository<Lstagfortemplate, Long>{
	Lstagfortemplate findById(long id);
}
