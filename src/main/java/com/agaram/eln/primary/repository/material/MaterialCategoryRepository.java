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
}
