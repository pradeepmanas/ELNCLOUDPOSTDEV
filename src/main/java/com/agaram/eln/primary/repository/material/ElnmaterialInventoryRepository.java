package com.agaram.eln.primary.repository.material;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

	List<ElnmaterialInventory> findByMaterialAndNtransactionstatusOrderByNmaterialinventorycodeDesc(
			Elnmaterial elnmaterial, int i);

	ElnmaterialInventory findByNmaterialinventorycodeAndNtransactionstatus(Integer nmaterialinventorycode, int i);

	List<ElnmaterialInventory> findByMaterialtypeAndMaterialcategoryAndMaterialAndNsitecodeAndCreateddateBetweenOrderByNmaterialinventorycodeDesc(
			MaterialType objMaterialType, MaterialCategory objMaterialCategory, Elnmaterial objElnmaterial,
			Integer nsiteInteger, Date fromDate, Date toDate);

	List<ElnmaterialInventory> findByMaterialtypeAndMaterialcategoryAndNsitecodeAndCreateddateBetweenOrderByNmaterialinventorycodeDesc(
			MaterialType objMaterialType, MaterialCategory objMaterialCategory, Integer nsiteInteger, Date fromDate,
			Date toDate);

	List<ElnmaterialInventory> findByNsitecodeAndNmaterialinventorycodeInAndCreateddateBetweenOrderByNmaterialinventorycodeDesc(
			Integer nsiteInteger, List<Integer> objLstInvKey, Date fromDate, Date toDate);

	List<ElnmaterialInventory> findByMaterialtypeAndNsitecodeAndNmaterialinventorycodeInAndCreateddateBetweenOrderByNmaterialinventorycodeDesc(
			MaterialType objMaterialType, Integer nsiteInteger, List<Integer> objLstInvKey, Date fromDate, Date toDate);

	List<ElnmaterialInventory> findByMaterialtypeAndMaterialcategoryAndMaterialAndNsitecodeAndNmaterialinventorycodeInAndCreateddateBetweenOrderByNmaterialinventorycodeDesc(
			MaterialType objMaterialType, MaterialCategory objMaterialCategory, Elnmaterial objElnmaterial,
			Integer nsiteInteger, List<Integer> objLstInvKey, Date fromDate, Date toDate);

	List<ElnmaterialInventory> findByMaterialtypeAndMaterialcategoryAndNsitecodeAndNmaterialinventorycodeInAndCreateddateBetweenOrderByNmaterialinventorycodeDesc(
			MaterialType objMaterialType, MaterialCategory objMaterialCategory, Integer nsiteInteger,
			List<Integer> objLstInvKey, Date fromDate, Date toDate);

	List<ElnmaterialInventory> findByNsitecodeAndNtransactionstatusAndIsexpiryAndExpirydateBetween(Integer lssitemaster,
			int i, boolean b, Date currentDate, Date endDate);

	List<ElnmaterialInventory> findByNmaterialinventorycodeIn(List<Integer> nmaterialinventorycode);

	List<ElnmaterialInventory> findByMaterialIn(List<Elnmaterial> material);

	List<ElnmaterialInventory> findByNsitecodeAndNtransactionstatusAndCreateddateBetweenOrderByNmaterialinventorycodeDesc(
			Integer nsiteInteger, int i, Date fromDate, Date toDate);

	List<ElnmaterialInventory> findByMaterialtypeAndNtransactionstatusAndNsitecodeAndCreateddateBetweenOrderByNmaterialinventorycodeDesc(
			MaterialType objMaterialType, int i, Integer nsiteInteger, Date fromDate, Date toDate);

	List<ElnmaterialInventory> findByMaterialtypeAndNtransactionstatusAndMaterialcategoryAndNsitecodeAndCreateddateBetweenOrderByNmaterialinventorycodeDesc(
			MaterialType objMaterialType, int i, MaterialCategory objMaterialCategory, Integer nsiteInteger,
			Date fromDate, Date toDate);

	List<ElnmaterialInventory> findByMaterialtypeAndNtransactionstatusAndMaterialcategoryAndMaterialAndNsitecodeAndCreateddateBetweenOrderByNmaterialinventorycodeDesc(
			MaterialType objMaterialType, int i, MaterialCategory objMaterialCategory, Elnmaterial objElnmaterial,
			Integer nsiteInteger, Date fromDate, Date toDate);

	List<ElnmaterialInventory> findBySinventoryidAndNsitecode(String searchString, Integer nsiteInteger);

	List<ElnmaterialInventory> findBySinventoryidInAndNsitecode(List<String> lstIds, Integer nsiteInteger);

	List<ElnmaterialInventory> findByMaterialOrderByNmaterialinventorycodeDesc(Elnmaterial objElnmaterial);

	@Transactional
	@Query(value = "SELECT * "
			+ "FROM elnmaterialInventory "
			+ "WHERE elnmaterialInventory.Nsitecode = ?1"
			+ "  AND ("
			+ "    LOWER(elnmaterialInventory.sinventoryid) LIKE LOWER(?2)"
			+ "    OR"
			+ "    LOWER(("
			+ "      SELECT Elnmaterial.smaterialname"
			+ "      FROM Elnmaterial"
			+ "      WHERE Elnmaterial.nmaterialcode = elnmaterialInventory.material_nmaterialcode"
			+ "    )) LIKE LOWER(?2)"
			+ "  )"
			+ "ORDER BY elnmaterialInventory.Nmaterialinventorycode DESC OFFSET ?3 ROWS FETCH NEXT ?4 ROWS ONLY"
			+ "", nativeQuery = true)
	List<ElnmaterialInventory> getNsitecodeAndSinventoryidLikeIgnoreCaseOrderByNmaterialinventorycodeDesc(
			Integer sitecode, String search_Key, int m, Integer pageperorder);
	@Query(value = "SELECT count(*) "
			+ "FROM elnmaterialInventory "
			+ "WHERE elnmaterialInventory.Nsitecode = ?1"
			+ "  AND ("
			+ "    LOWER(elnmaterialInventory.sinventoryid) LIKE LOWER(?2)"
			+ "    OR"
			+ "    LOWER(("
			+ "      SELECT Elnmaterial.smaterialname"
			+ "      FROM Elnmaterial"
			+ "      WHERE Elnmaterial.nmaterialcode = elnmaterialInventory.material_nmaterialcode"
			+ "    )) LIKE LOWER(?2)"
			+ "  )", nativeQuery = true)	
	long getcounrNsitecodeAndSinventoryidLikeIgnoreCaseOrderByNmaterialinventorycodeDesc(Integer sitecode,
			String search_Key);

	List<ElnmaterialInventory> findBySinventoryidStartingWithIgnoreCaseAndNsitecode(String searchString,
			Integer nsiteInteger);

	List<ElnmaterialInventory> findByMaterialInOrderByNmaterialinventorycodeDesc(List<Elnmaterial> material);

}
