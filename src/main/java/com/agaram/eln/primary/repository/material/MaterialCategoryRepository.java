package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.MaterialCategory;

public interface MaterialCategoryRepository extends JpaRepository<MaterialCategory, Integer>{

	public List<MaterialCategory> findByNmaterialtypecode(Integer nmaterialtypecode);
	public List<MaterialCategory> findByNmaterialcatcode(Integer nmaterialcatcode);
	public List<Object> findByNstatus(Integer status);
	public List<MaterialCategory> findBySmaterialcatname(String smaterialcatname);
}
