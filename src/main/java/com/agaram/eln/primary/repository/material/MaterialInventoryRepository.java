package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.MaterialInventory;

public interface MaterialInventoryRepository extends JpaRepository<MaterialInventory, Integer>{

	public int countByNmaterialcodeAndNstatus(Integer integer, int i);

	public List<MaterialInventory> findByNmaterialcode(Integer integer);

	public MaterialInventory findByNmaterialinventorycode(Integer integer);

	public List<MaterialInventory> findByNmaterialcodeOrderByNmaterialinventorycode(Integer nmaterialcode);

	public List<MaterialInventory> findByNmaterialcodeAndNmaterialcatcodeAndNmaterialtypecode(Integer integer,
			Integer integer2, Integer integer3);

	public MaterialInventory findByNmaterialinventorycodeAndNstatus(Integer integer, int i);

	public List<MaterialInventory> findByNmaterialcodeAndNmaterialcatcodeAndNmaterialtypecodeAndNstatus(Integer integer,
			Integer integer2, Integer integer3, int i);

	public List<MaterialInventory> findByNmaterialcodeAndNtransactionstatusOrderByNmaterialinventorycode(Integer nmaterialcode,
			int i);

	public MaterialInventory findByNmaterialinventorycodeAndNtransactionstatus(Integer nmaterialinventorycode, int i);

}
