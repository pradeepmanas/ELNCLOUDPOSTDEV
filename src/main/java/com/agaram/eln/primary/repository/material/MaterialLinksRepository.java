package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.MaterialLinks;

public interface MaterialLinksRepository extends JpaRepository<MaterialLinks, Integer>{
	List<MaterialLinks> findByNmaterialcode(Integer nmaterialcode);
}
