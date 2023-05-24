package com.agaram.eln.primary.repository.inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.inventory.LSmaterial;

public interface LSmaterialRepository extends JpaRepository<LSmaterial, Integer>{
	
	public List<LSmaterial> findByNmaterialcatcode(Integer nmaterialcatcode);

}
