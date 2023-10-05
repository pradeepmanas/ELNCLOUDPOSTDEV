package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

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
	
}
