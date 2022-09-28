package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.MaterialType;


public interface MaterialTypeRepository extends JpaRepository<MaterialType, Integer>{
	public List<MaterialType> findByNmaterialtypecode(Integer nmaterialtypecode);
	public List<MaterialType> findByNstatus(Integer status);
	public List<MaterialType> findByNmaterialtypecodeAndNstatusOrderByNmaterialtypecode(Integer integer, int i);
	public MaterialType findByNmaterialtypecodeAndNstatus(Integer integer, int i);
} 
