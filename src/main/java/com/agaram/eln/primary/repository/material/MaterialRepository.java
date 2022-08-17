package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.Material;

public interface MaterialRepository  extends JpaRepository<Material, Integer>{

	List<Material> findByNmaterialcatcode(Integer nmaterialcatcode);

}
