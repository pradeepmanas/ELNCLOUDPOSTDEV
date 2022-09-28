package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.MappedTemplateFieldPropsMaterial;


public interface MappedTemplateFieldPropsMaterialRepository extends JpaRepository<MappedTemplateFieldPropsMaterial, Integer>{
	
	public List<MappedTemplateFieldPropsMaterial> findByNmaterialconfigcode(Integer nmaterialconfigcode);

	public MappedTemplateFieldPropsMaterial findByNmaterialconfigcodeAndNstatus(int typeCode, int i);
}