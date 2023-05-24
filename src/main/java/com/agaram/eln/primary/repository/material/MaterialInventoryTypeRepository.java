package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.MaterialInventoryType;

public interface MaterialInventoryTypeRepository extends JpaRepository<MaterialInventoryType, Integer>{
	
	public List<MaterialInventoryType> findByNstatusAndNinventorytypecodeIn(Integer nstatus,List<Integer> lstNinventorytypecode);

}
