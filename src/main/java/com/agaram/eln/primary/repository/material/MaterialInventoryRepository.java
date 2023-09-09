package com.agaram.eln.primary.repository.material;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agaram.eln.primary.model.material.MaterialInventory;

public interface MaterialInventoryRepository extends JpaRepository<MaterialInventory, Integer>{

	public MaterialInventory findByNmaterialinventorycode(Integer integer);
//
	public MaterialInventory findByNmaterialinventorycodeAndNstatus(Integer integer, int i);
	
//	@Query("SELECT mi.*, m.smaterialname,mc.smaterialcatname FROM materialinventory mi INNER JOIN material m ON mi.nmaterialcode = m.nmaterialcode"
//			+ "INNER JOIN materialcategory mc ON mi.nmaterialcatcode = mc.nmaterialcatcode"
//			+ " WHERE m.nmaterialcode = :materialCode AND m.nmaterialtypecode = :materialTypeCode " + 
//			" AND mc.nmaterialcatcode = :materialCatCode")
//    List<MaterialInventory>findMaterialWithCategory(@Param("materialCode") Integer materialCode,
//            @Param("materialTypeCode") Integer materialTypeCode,
//            @Param("materialCatCode") Integer materialCatCode);

	public MaterialInventory findByNmaterialinventorycodeAndNtransactionstatus(Integer nmaterialinventorycode, int i);

	public List<MaterialInventory> findByNtransactionstatusOrderByNmaterialinventorycode(int i);

	public List<MaterialInventory> findByNsitecodeAndNtransactionstatusOrderByNmaterialinventorycodeDesc(Integer integer,
			int i);

	public MaterialInventory findByNmaterialinventorycodeAndNtransactionstatusNot(Integer integer, int i);

	public List<MaterialInventory> findByNtransactionstatusAndIsexpiryneedAndExpirydateBetween(int i, boolean b,
			Date currentDate, Date endDate);

	public List<MaterialInventory> findByNsitecodeAndNtransactionstatusAndIsexpiryneedAndExpirydateBetween(
			Integer lssitemaster, int i, boolean b, Date currentDate, Date endDate);

	public List<MaterialInventory> findByNstatus(int i);
	
	public List<MaterialInventory> findByNmaterialcodeAndNmaterialcatcodeAndNmaterialtypecodeAndNstatusOrderByNmaterialinventorycodeDesc(
			Integer integer, Integer integer2, Integer integer3, int i);
	public List<MaterialInventory> findByNmaterialcodeAndNmaterialcatcodeAndNmaterialtypecodeAndNstatusAndNmaterialinventorycodeInOrderByNmaterialinventorycodeDesc(
			Integer integer, Integer integer2, Integer integer3, int i, List<Integer> lstInventKey);
	public List<MaterialInventory> findByNmaterialcodeAndNtransactionstatusOrderByNmaterialinventorycodeDesc(
			Integer nmaterialcode, int i);
	public int countByNmaterialcodeAndNstatus(Integer nmaterialcode, int i);

}
