package com.agaram.eln.primary.repository.material;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

	List<Elnmaterial> findByNstatusAndNsitecodeOrderByNmaterialcodeDesc(int i, Integer integer);

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

}
