package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.ElnmaterialChemDiagRef;

public interface ElnmaterialChemDiagRefRepository  extends JpaRepository<ElnmaterialChemDiagRef, Integer>{

	List<ElnmaterialChemDiagRef> findByNmaterialcodeOrderByDiagramcodeDesc(Integer nmaterialcode);

	ElnmaterialChemDiagRef findByFileid(String fileName);

}
