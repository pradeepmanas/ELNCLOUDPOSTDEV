package com.agaram.eln.primary.repository.material;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.ElnmaterialInventory;
import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.model.material.MaterialType;

public interface ElnmaterialInventoryRepository extends JpaRepository<ElnmaterialInventory, Integer>{

	ElnmaterialInventory findByNmaterialinventorycode(int inventoryCode);

	List<ElnmaterialInventory> findByNsitecodeAndCreateddateBetweenOrderByNmaterialinventorycodeDesc(Integer nsiteInteger,
			Date fromDate, Date toDate);

	List<ElnmaterialInventory> findByMaterialcategoryAndNsitecodeAndCreateddateBetweenOrderByNmaterialinventorycodeDesc(
			MaterialCategory objMaterialCategory, Integer nsiteInteger, Date fromDate, Date toDate);

	List<ElnmaterialInventory> findByMaterialtypeAndNsitecodeAndCreateddateBetweenOrderByNmaterialinventorycodeDesc(
			MaterialType objMaterialType, Integer nsiteInteger, Date fromDate, Date toDate);

	List<ElnmaterialInventory> findByMaterialtypeAndMaterialcategoryAndNsitecodeAndCreateddateBetweenOrderByNmaterialinventorycodeDesc(
			MaterialType objMaterialType, MaterialCategory objMaterialCategory, Integer nsiteInteger, Date fromDate,
			Date toDate);

	List<ElnmaterialInventory> findByMaterialAndNtransactionstatusOrderByNmaterialinventorycodeDesc(
			Elnmaterial elnmaterial, int i);

	ElnmaterialInventory findByNmaterialinventorycodeAndNtransactionstatus(Integer nmaterialinventorycode, int i);

}
