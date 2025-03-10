package com.agaram.eln.primary.repository.material;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agaram.eln.primary.fetchmodel.inventory.Materialget;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;

public interface ElnmaterialRepository extends JpaRepository<Elnmaterial, Integer> {

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
	@Query(value = "select * from elnmaterial where smaterialname = ?1 and nsitecode = ?2", nativeQuery = true)
	Elnmaterial getMaterialDetails(String material_name, Number sitecode);

	Elnmaterial findByNsitecodeAndSmaterialnameIgnoreCaseAndMaterialcategory(Integer nsitecode, String smaterialname,
			MaterialCategory materialcategory);

	List<Elnmaterial> findByNsitecodeAndSmaterialnameIgnoreCaseIn(Integer siteCode, List<String> elnmaterialName);

	Elnmaterial findByNstatusAndNmaterialcode(int i, Integer integer);

	List<Elnmaterial> findByNsitecodeAndCreateddateBetweenAndAssignedprojectOrNsitecodeAndCreateddateBetweenAndAssignedprojectIsNullOrderByNmaterialcodeDesc(
			Integer nsiteInteger, Date fromDate, Date toDate, String string, Integer nsiteInteger2, Date fromDate2,
			Date toDate2);

	@Transactional
	@Query(value = "select em.* from elnmaterial em "
			+ "where em.materialcategory_nmaterialcatcode = ?1 and em.nsitecode = ?2 and "
			+ "em.createddate BETWEEN ?3 AND ?4 and (select count(*) from materialprojectmap where nmaterialcode = em.nmaterialcode) = 0 Order By Nmaterialcode Desc", nativeQuery = true)
	List<Elnmaterial> getMaterialOnGeneralProjects(Integer objMaterialCategory, Integer nsiteInteger, Date fromDate,
			Date toDate);

	@Transactional
	@Query(value = "select em.* from elnmaterial em "
			+ "where em.materialcategory_nmaterialcatcode = ?1 and em.nsitecode = ?2 and "
			+ "em.createddate BETWEEN ?3 AND ?4 and (select count(*) from materialprojectmap where nmaterialcode = em.nmaterialcode and lsproject_projectcode in (?5)) > 0 Order By Nmaterialcode Desc", nativeQuery = true)
	List<Elnmaterial> getMaterialOnProjects(Integer objMaterialCategory, Integer nsiteInteger, Date fromDate,
			Date toDate, List<Integer> projectcode);

	List<Elnmaterial> findByNsitecodeAndCreateddateBetweenAndNmaterialcodeNotInOrderByNmaterialcodeDesc(
			Integer nsiteInteger, Date fromdate, Date todate, List<Integer> nmaterialcode);


	
	@Query("SELECT e FROM Elnmaterial e WHERE e.nsitecode = :nsitecode "
	        + "AND e.createddate BETWEEN :fromDate AND :toDate "
	        + "AND e.nmaterialcode IN (SELECT m.nmaterialcode FROM MaterialProjectMap m WHERE lsproject =:project) "
	        + "AND e.materialcategory.nmaterialcatcode IN "
	        + "(SELECT c.nmaterialcatcode FROM MaterialCategory c WHERE c.nstatus = 1) "
	        + "AND e.materialtype.nmaterialtypecode IN "
	        + "(SELECT t.nmaterialtypecode FROM MaterialType t WHERE t.nstatus = 1) "
	        + "ORDER BY e.nmaterialcode DESC")
	List<Elnmaterial> findElnmaterialsWithProjectMaps(
	        @Param("nsitecode") Integer nsitecode,
	        @Param("fromDate") Date fromDate,
	        @Param("toDate") Date toDate,@Param("project") LSprojectmaster project);
	
	@Query("SELECT e FROM Elnmaterial e WHERE e.nsitecode = :nsitecode "
	        + "AND e.createddate BETWEEN :fromDate AND :toDate "
	        + "AND e.nmaterialcode IN (SELECT m.nmaterialcode FROM MaterialProjectMap m WHERE m.lsproject = :project) "
	        + "AND e.materialcategory.nmaterialcatcode IN "
	        + "(SELECT c.nmaterialcatcode FROM MaterialCategory c WHERE c.nstatus = 1) "
	        + "AND e.materialtype.nmaterialtypecode IN "
	        + "(SELECT t.nmaterialtypecode FROM MaterialType t WHERE t.nstatus = 1) "
	        + "AND e.materialtype = :objmaterialtype "
	        + "AND e.materialcategory = :objMaterialCategory "
	        + "ORDER BY e.nmaterialcode DESC")
	List<Elnmaterial> findElnmaterialsWithProjectMapsforfilter(
	        @Param("nsitecode") Integer nsitecode,
	        @Param("fromDate") Date fromDate,
	        @Param("toDate") Date toDate,
	        @Param("project") LSprojectmaster project,
	        @Param("objmaterialtype") MaterialType objmaterialtype,
	        @Param("objMaterialCategory") MaterialCategory objMaterialCategory);

	@Query("SELECT e FROM Elnmaterial e WHERE e.nsitecode = :nsitecode "
	        + "AND e.createddate BETWEEN :fromDate AND :toDate "
	        + "AND e.nmaterialcode IN (SELECT m.nmaterialcode FROM MaterialProjectMap m WHERE m.lsproject = :project) "
	        + "AND e.materialcategory.nmaterialcatcode IN "
	        + "(SELECT c.nmaterialcatcode FROM MaterialCategory c WHERE c.nstatus = 1) "
	        + "AND e.materialtype.nmaterialtypecode IN "
	        + "(SELECT t.nmaterialtypecode FROM MaterialType t WHERE t.nstatus = 1) "
	        + "AND e.materialtype = :objmaterialtype "
	        + "ORDER BY e.nmaterialcode DESC")
	List<Elnmaterial> findElnmaterialsWithProjectMapsforfilterformattype(
	        @Param("nsitecode") Integer nsitecode,
	        @Param("fromDate") Date fromDate,
	        @Param("toDate") Date toDate,
	        @Param("project") LSprojectmaster project,
	        @Param("objmaterialtype") MaterialType objmaterialtype);

	@Query("SELECT e FROM Elnmaterial e WHERE e.nsitecode = :nsitecode "
	        + "AND e.createddate BETWEEN :fromDate AND :toDate "
	        + "AND e.nmaterialcode IN (SELECT m.nmaterialcode FROM MaterialProjectMap m WHERE m.lsproject = :project) "
	        + "AND e.materialcategory.nmaterialcatcode IN "
	        + "(SELECT c.nmaterialcatcode FROM MaterialCategory c WHERE c.nstatus = 1) "
	        + "AND e.materialtype.nmaterialtypecode IN "
	        + "(SELECT t.nmaterialtypecode FROM MaterialType t WHERE t.nstatus = 1) "
	        + "AND e.materialcategory = :objMaterialCategory "
	        + "ORDER BY e.nmaterialcode DESC")
	List<Elnmaterial> findElnmaterialsWithProjectMapsforfilterformatcat(
	        @Param("nsitecode") Integer nsitecode,
	        @Param("fromDate") Date fromDate,
	        @Param("toDate") Date toDate,
	        @Param("project") LSprojectmaster project,
	        @Param("objMaterialCategory") MaterialCategory objMaterialCategory);

	
	//1

	@Query("SELECT e FROM Elnmaterial e WHERE e.nsitecode = :nsitecode "
	        + "AND e.createddate BETWEEN :fromDate AND :toDate "
	        + "AND e.nmaterialcode NOT IN (SELECT m.nmaterialcode FROM MaterialProjectMap m) "
	        + "AND e.materialcategory.nmaterialcatcode IN "
	        + "(SELECT c.nmaterialcatcode FROM MaterialCategory c WHERE c.nstatus = 1) "
	        + "AND e.materialtype.nmaterialtypecode IN "
	        + "(SELECT t.nmaterialtypecode FROM MaterialType t WHERE t.nstatus = 1) "
	        + "ORDER BY e.nmaterialcode DESC")
	List<Elnmaterial> findElnmaterialsWithoutProjectMaps(
	        @Param("nsitecode") Integer nsitecode,
	        @Param("fromDate") Date fromDate,
	        @Param("toDate") Date toDate);


	@Query("SELECT e FROM Elnmaterial e WHERE e.nsitecode = :nsitecode "
	        + "AND e.createddate BETWEEN :fromDate AND :toDate "
	        + "AND e.materialtype = :objmaterialtype AND e.materialcategory = :objMaterialCategory "
	        + "AND e.nmaterialcode NOT IN (SELECT m.nmaterialcode FROM MaterialProjectMap m) "
	        + "AND e.materialcategory.nmaterialcatcode IN "
	        + "(SELECT c.nmaterialcatcode FROM MaterialCategory c WHERE c.nstatus = 1) "
	        + "AND e.materialtype.nmaterialtypecode IN "
	        + "(SELECT t.nmaterialtypecode FROM MaterialType t WHERE t.nstatus = 1) "
	        + "ORDER BY e.nmaterialcode DESC")
	List<Elnmaterial> findElnmaterialsWithoutProjectMapsforfilter(
	        @Param("nsitecode") Integer nsitecode,
	        @Param("fromDate") Date fromDate,
	        @Param("toDate") Date toDate,
	        @Param("objmaterialtype") MaterialType objmaterialtype,
	        @Param("objMaterialCategory") MaterialCategory objMaterialCategory);

	@Query("SELECT e FROM Elnmaterial e WHERE e.nsitecode = :nsitecode "
	        + "AND e.createddate BETWEEN :fromDate AND :toDate "
	        + "AND e.materialtype = :objmaterialtype "
	        + "AND e.nmaterialcode NOT IN (SELECT m.nmaterialcode FROM MaterialProjectMap m) "
	        + "AND e.materialcategory.nmaterialcatcode IN "
	        + "(SELECT c.nmaterialcatcode FROM MaterialCategory c WHERE c.nstatus = 1) "
	        + "AND e.materialtype.nmaterialtypecode IN "
	        + "(SELECT t.nmaterialtypecode FROM MaterialType t WHERE t.nstatus = 1) "
	        + "ORDER BY e.nmaterialcode DESC")
	List<Elnmaterial> findElnmaterialsWithoutProjectMapsforfilterformaterialtype(
	        @Param("nsitecode") Integer nsitecode,
	        @Param("fromDate") Date fromDate,
	        @Param("toDate") Date toDate,
	        @Param("objmaterialtype") MaterialType objmaterialtype);

	@Query("SELECT e FROM Elnmaterial e WHERE e.nsitecode = :nsitecode "
	        + "AND e.createddate BETWEEN :fromDate AND :toDate "
	        + "AND e.materialcategory = :objMaterialCategory "
	        + "AND e.nmaterialcode NOT IN (SELECT m.nmaterialcode FROM MaterialProjectMap m) "
	        + "AND e.materialcategory.nmaterialcatcode IN "
	        + "(SELECT c.nmaterialcatcode FROM MaterialCategory c WHERE c.nstatus = 1) "
	        + "AND e.materialtype.nmaterialtypecode IN "
	        + "(SELECT t.nmaterialtypecode FROM MaterialType t WHERE t.nstatus = 1) "
	        + "ORDER BY e.nmaterialcode DESC")
	List<Elnmaterial> findElnmaterialsWithoutProjectMapsforfilterforcat(
	        @Param("nsitecode") Integer nsitecode,
	        @Param("fromDate") Date fromDate,
	        @Param("toDate") Date toDate,
	        @Param("objMaterialCategory") MaterialCategory objMaterialCategory);


}
