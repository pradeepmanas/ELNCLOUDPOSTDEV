package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.ResultUsedMaterial;

public interface ResultUsedMaterialRepository extends JpaRepository<ResultUsedMaterial, Integer>{
	List<ResultUsedMaterial> findByNinventorycodeOrderByNresultusedmaterialcodeDesc(int nmaterialinventorycode);
}