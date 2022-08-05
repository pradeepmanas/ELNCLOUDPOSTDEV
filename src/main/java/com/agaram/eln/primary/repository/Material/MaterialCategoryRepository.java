package com.agaram.eln.primary.repository.Material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.Material.MaterialCategory;

public interface MaterialCategoryRepository extends JpaRepository<MaterialCategory, Integer>{

	public List<MaterialCategory> findByNmaterialtypecode(Integer nmaterialtypecode);
	
}
