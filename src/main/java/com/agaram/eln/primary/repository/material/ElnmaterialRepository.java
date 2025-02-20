package com.agaram.eln.primary.repository.material;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.fetchmodel.inventory.Materialget;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.model.material.MaterialType;

public interface ElnmaterialRepository extends JpaRepository<Elnmaterial, Integer>{

	List<Elnmaterial> findByNsitecodeOrderByNmaterialcodeDesc(Integer nsiteInteger);

	Elnmaterial findByNsitecodeAndSmaterialnameAndMaterialcategory(Integer nsitecode, String smaterialname,
			MaterialCategory materialcategory);

	Elnmaterial findByNsitecodeAndSmaterialnameAndMaterialcategoryAndNmaterialcodeNot(Integer nsitecode,
			String smaterialname, MaterialCategory materialcategory, Integer nmaterialcode);

	List<Elnmaterial> findByMaterialtypeAndMaterialcategoryAndNsitecodeAndCreateddateBetweenOrderByNmaterialcodeDesc(
			MaterialType objMaterialType, MaterialCategory objMaterialCategory, Integer nsiteInteger, Date fromDate,
			Date toDate);

	List<Elnmaterial> findByNsitecodeAndCreateddateBetweenOrderByNmaterialcodeDesc(Integer nsiteInteger, Date fromDate,
			Date toDate);

	List<Elnmaterial> findByMaterialcategoryAndNsitecodeAndCreateddateBetweenOrderByNmaterialcodeDesc(
			MaterialCategory objMaterialCategory, Integer nsiteInteger, Date fromDate, Date toDate);

	List<Elnmaterial> findByMaterialtypeAndNsitecodeAndCreateddateBetweenOrderByNmaterialcodeDesc(
			MaterialType objMaterialType, Integer nsiteInteger, Date fromDate, Date toDate);

	List<Elnmaterial> findByMaterialcategoryAndNsitecodeOrderByNmaterialcodeDesc(MaterialCategory materialCategory,
			Integer nsiteInteger);

	List<Elnmaterial> findByMaterialcategoryOrderByNmaterialcodeDesc(MaterialCategory materialCategory);

	List<Elnmaterial> findBySmaterialnameContainsIgnoreCase(String searchname);

	List<Elnmaterial> findBySmaterialnameStartingWithIgnoreCaseAndNsitecode(String searchString, Integer nsiteInteger);

	List<Materialget> findByNstatusAndNsitecodeOrderByNmaterialcodeDesc(int i, Integer integer);

	List<Elnmaterial> findByNsitecodeAndNstatusOrderByNmaterialcodeDesc(Integer nsiteInteger, int i);

	List<Elnmaterial> findByMaterialcategoryAndNsitecodeAndNstatusOrderByNmaterialcodeDesc(
			MaterialCategory materialCategory, Integer nsiteInteger, int i);

	List<Elnmaterial> findBySamplecodeIsNotNull();

	List<Elnmaterial> findBySmaterialnameContainsIgnoreCaseAndNsitecode(String searchname, Integer sitecode);

	List<Elnmaterial> findByNsitecode(Integer sitecode);

	List<Elnmaterial> findByNmaterialcodeIn(List<Integer> nmaterialcodes);
	@Transactional
	@Query(value = "select * from elnmaterial where smaterialname = ?1 and nsitecode = ?2",nativeQuery = true)
	Elnmaterial getMaterialDetails(String material_name, Number sitecode);

	Elnmaterial findByNsitecodeAndSmaterialnameIgnoreCaseAndMaterialcategory(Integer nsitecode, String smaterialname,
			MaterialCategory materialcategory);

	List<Elnmaterial> findByNsitecodeAndSmaterialnameIgnoreCaseIn(Integer siteCode,
			List<String> elnmaterialName);

	Elnmaterial findByNstatusAndNmaterialcode(int i, Integer integer);


	List<Elnmaterial> findByNsitecodeAndCreateddateBetweenAndAssignedprojectOrNsitecodeAndCreateddateBetweenAndAssignedprojectIsNullOrderByNmaterialcodeDesc(
			Integer nsiteInteger, Date fromDate, Date toDate, String string, Integer nsiteInteger2, Date fromDate2,
			Date toDate2);

	List<Elnmaterial> findByNsitecodeAndCreateddateBetweenAndNmaterialcodeInOrderByNmaterialcodeDesc(
			Integer nsiteInteger, Date fromDate, Date toDate, List<Integer> nmaterialcode);

}
