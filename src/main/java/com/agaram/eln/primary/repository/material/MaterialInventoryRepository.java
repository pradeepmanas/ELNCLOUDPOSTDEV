package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.MaterialInventory;

public interface MaterialInventoryRepository extends JpaRepository<MaterialInventory, Integer>{

//	List<MaterialInventory> findByNmaterialcodeAndNmaterialcatcodeAndNmaterialtypecode(Integer integer,
//			Integer integer2, Integer integer3);

	int countByNmaterialcodeAndNstatus(Integer integer, int i);

	List<MaterialInventory> findByNmaterialcode(Integer integer);

	MaterialInventory findByNmaterialinventorycode(Integer integer);

	List<MaterialInventory> findByNmaterialcodeOrderByNmaterialinventorycode(Integer nmaterialcode);



	List<MaterialInventory> findByNmaterialcodeAndNmaterialcatcodeAndNmaterialtypecode(Integer integer,
			Integer integer2, Integer integer3);
	
	

}
