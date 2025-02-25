package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.MaterialProjectMap;

public interface MaterialProjectMapRepository extends JpaRepository<MaterialProjectMap, Integer>{
	List<MaterialProjectMap> findByNmaterialcodeOrderByMaterialprojectcode(Integer nmaterialcode);
}
