package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.material.MaterialCategory;

public interface MaterialCategoryRepository extends JpaRepository<MaterialCategory, Integer>{

	public List<MaterialCategory> findByNmaterialtypecode(Integer nmaterialtypecode);
	public MaterialCategory findByNmaterialcatcode(Integer nmaterialcatcode);
	public MaterialCategory findByNmaterialcatcodeAndNstatus(Integer nmaterialcatcode,Integer nstatus);
	public List<Object> findByNstatus(Integer status);
	public List<MaterialCategory> findBySmaterialcatname(String smaterialcatname);
	public List<MaterialCategory> findByNmaterialtypecodeAndNstatus(Integer integer, int i);
	public MaterialCategory findBySmaterialcatnameAndNmaterialcatcodeAndNstatus(String smaterialcatname,
			Integer nmaterialcatcode, int i);
	public List<MaterialCategory> findByNmaterialtypecodeOrderByNmaterialcatcode(Integer nmaterialtypecode);
	public List<Object> findByNstatusOrderByNmaterialcatcodeDesc(int i);
	public List<Object> findByNstatusAndNmaterialtypecode(int i, int parseInt);
	public List<Object> findByNstatusAndNsitecodeOrderByNmaterialcatcodeDesc(int i, Integer nsitecode);
	public List<MaterialCategory> findByNmaterialtypecodeAndNsitecode(Integer nmaterialtypecode, Integer nsitecode);
	public List<Object> findByNstatusAndNsitecodeAndNmaterialtypecode(int i, Integer nsiteInteger, int parseInt);
	public List<MaterialCategory> findByNmaterialtypecodeAndNmaterialcatcodeAndNsitecodeOrderByNmaterialcatcode(
			Integer nmaterialtypecode,Integer nmaterialcatcode, Integer nsiteInteger);
	public List<MaterialCategory> findByNmaterialtypecodeAndNsitecodeOrderByNmaterialcatcode(Integer nmaterialtypecode,
			Integer nsiteInteger);
	public List<MaterialCategory> findByNmaterialtypecodeAndNsitecodeOrderByNmaterialcatcodeDesc(
			Integer nmaterialtypecode, Integer nsiteInteger);
	public List<MaterialCategory> findByNmaterialtypecodeAndNsitecodeAndNstatus(Integer nmaterialtypecode,
			Integer nsitecode, int i);
	public List<MaterialCategory> findByNmaterialtypecodeAndNsitecodeAndNstatusOrderByNmaterialcatcode(
			Integer nmaterialtypecode, Integer nsiteInteger, int i);
	public List<MaterialCategory> findByNmaterialtypecodeAndNmaterialcatcodeAndNsitecodeAndNstatusOrderByNmaterialcatcode(
			Integer nmaterialtypecode, Integer nmaterialcatcode, Integer nsiteInteger, int i);
	public List<MaterialCategory> findByNsitecodeOrderByNmaterialcatcodeDesc(Integer nsitecode);
	public List<MaterialCategory> findBySmaterialcatnameAndNsitecode(String smaterialcatname, Integer nsitecode);
	public MaterialCategory findByNsitecodeAndSmaterialcatname(Integer nsitecode,String smaterialcatname );
	public MaterialCategory findBySmaterialcatnameAndNstatus(String smaterialcatname, int i);
	public MaterialCategory findBySmaterialcatnameAndNstatusAndNsitecode(String smaterialcatname, int i,
			Integer nsitecode);
	public MaterialCategory findByNsitecodeAndSmaterialcatnameIgnoreCase(Integer nsitecode, String smaterialcatname);
	public List<MaterialCategory> findBySmaterialcatnameIgnoreCaseAndNsitecode(String smaterialcatname,
			Integer nsitecode);
	public List<MaterialCategory> findByNsitecodeAndNstatusOrderByNmaterialcatcode(Integer nsitecode, int i);
	public List<MaterialCategory> findByNmaterialtypecodeOrderByNmaterialcatcodeDesc(Integer ntypecode);
	public List<MaterialCategory> findByNsitecodeAndNstatus(Integer nsiteInteger, int i);
	public List<MaterialCategory> findByNmaterialtypecodeAndNsitecodeAndNstatusOrderByNmaterialcatcodeDesc(
			Integer nmaterialtypecode, Integer nsiteInteger, int i);
	public List<MaterialCategory> findBySmaterialcatnameIgnoreCaseAndNsitecodeAndNstatus(String smaterialcatname,
			Integer nsitecode, int i);
	public MaterialCategory findByNsitecodeAndSmaterialcatnameIgnoreCaseAndNstatus(Integer nsitecode,
			String smaterialcatname, int i);
	public List<MaterialCategory> findByNsitecodeOrNdefaultstatusOrderByNmaterialcatcodeDesc(Integer nsitecode, int i);
	public List<MaterialCategory> findByNsitecodeAndNstatusOrNstatusAndNdefaultstatusOrderByNmaterialcatcodeDesc(
			Integer nsiteInteger, int i, int j, int k);
	public List<MaterialCategory> findByNmaterialtypecodeAndNsitecodeAndNstatusOrNmaterialtypecodeAndNstatusAndNdefaultstatusOrderByNmaterialcatcodeDesc(
			Integer nmaterialtypecode, Integer nsiteInteger, int i, Integer nmaterialtypecode2, int j, int k);
	@Query(value ="select * from materialcategory where smaterialcatname = ?1 and nsitecode = ?2", nativeQuery = true)
	public MaterialCategory getMaterialCatgeoryDetails(String string, Number sitecode);
	public List<MaterialCategory> findByNsitecodeAndNstatusOrderByNmaterialcatcodeDesc(Integer nsiteInteger, int i);
	
}
