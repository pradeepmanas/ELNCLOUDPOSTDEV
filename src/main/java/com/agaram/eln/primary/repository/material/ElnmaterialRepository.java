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
	
	@Transactional
	@Query(value = "select em.* from elnmaterial em "
			+ "where em.materialcategory_nmaterialcatcode = ?1 and em.nsitecode = ?2 and "
			+ "em.createddate BETWEEN ?3 AND ?4 and (select count(*) from materialprojectmap where nmaterialcode = em.nmaterialcode) = 0 Order By Nmaterialcode Desc",nativeQuery = true)
	List<Elnmaterial> getMaterialOnGeneralProjects(Integer objMaterialCategory, Integer nsiteInteger, Date fromDate, Date toDate);

	@Transactional
	@Query(value = "select em.* from elnmaterial em "
			+ "where em.materialcategory_nmaterialcatcode = ?1 and em.nsitecode = ?2 and "
			+ "em.createddate BETWEEN ?3 AND ?4 and (select count(*) from materialprojectmap where nmaterialcode = em.nmaterialcode and lsproject_projectcode in (?5)) > 0 Order By Nmaterialcode Desc",nativeQuery = true)
	List<Elnmaterial> getMaterialOnProjects(Integer objMaterialCategory, Integer nsiteInteger, Date fromDate, Date toDate, List<Integer> projectcode);

	List<Elnmaterial> findByNsitecodeAndCreateddateBetweenAndNmaterialcodeNotInOrderByNmaterialcodeDesc(
			Integer nsiteInteger, Date fromdate, Date todate, List<Integer> nmaterialcode);

	List<Elnmaterial> findByNsitecodeAndCreateddateBetweenAndMaterialtypeAndMaterialcategoryOrderByNmaterialcodeDesc(
			Integer nsiteInteger, Date fromdate, Date todate, MaterialType objmaterialtype,
			MaterialCategory objMaterialCategory);

	List<Elnmaterial> findByNsitecodeAndCreateddateBetweenAndMaterialtypeOrderByNmaterialcodeDesc(Integer nsiteInteger,
			Date fromdate, Date todate, MaterialType objmaterialtype);

	List<Elnmaterial> findByNsitecodeAndCreateddateBetweenAndMaterialcategoryOrderByNmaterialcodeDesc(
			Integer nsiteInteger, Date fromdate, Date todate, MaterialCategory objMaterialCategory);

	List<Elnmaterial> findByNsitecodeAndCreateddateBetweenAndNmaterialcodeInAndMaterialtypeAndMaterialcategoryOrderByNmaterialcodeDesc(
			Integer nsiteInteger, Date fromdate, Date todate, List<Integer> nmaterialcode, MaterialType objmaterialtype,
			MaterialCategory objMaterialCategory);

	List<Elnmaterial> findByNsitecodeAndCreateddateBetweenAndNmaterialcodeNotInAndMaterialtypeAndMaterialcategoryOrderByNmaterialcodeDesc(
			Integer nsiteInteger, Date fromdate, Date todate, List<Integer> nmaterialcode, MaterialType objmaterialtype,
			MaterialCategory objMaterialCategory);

	List<Elnmaterial> findByNsitecodeAndCreateddateBetweenAndNmaterialcodeInAndMaterialtypeOrderByNmaterialcodeDesc(
			Integer nsiteInteger, Date fromdate, Date todate, List<Integer> nmaterialcode, MaterialType objmaterialtype,
			MaterialCategory objMaterialCategory);

	List<Elnmaterial> findByNsitecodeAndCreateddateBetweenAndNmaterialcodeNotInAndMaterialtypeOrderByNmaterialcodeDesc(
			Integer nsiteInteger, Date fromdate, Date todate, List<Integer> nmaterialcode, MaterialType objmaterialtype,
			MaterialCategory objMaterialCategory);

	List<Elnmaterial> findByNsitecodeAndCreateddateBetweenAndNmaterialcodeInAndMaterialcategoryOrderByNmaterialcodeDesc(
			Integer nsiteInteger, Date fromdate, Date todate, List<Integer> nmaterialcode,
			MaterialCategory objMaterialCategory);

	List<Elnmaterial> findByNsitecodeAndCreateddateBetweenAndNmaterialcodeNotInAndMaterialcategoryOrderByNmaterialcodeDesc(
			Integer nsiteInteger, Date fromdate, Date todate, List<Integer> nmaterialcode,
			MaterialCategory objMaterialCategory);

}
