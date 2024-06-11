package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.material.MaterialType;


public interface MaterialTypeRepository extends JpaRepository<MaterialType, Integer>{
	public List<MaterialType> findByNmaterialtypecode(Integer nmaterialtypecode);
	public List<MaterialType> findByNstatus(Integer status);
	public List<MaterialType> findByNmaterialtypecodeAndNstatusOrderByNmaterialtypecode(Integer integer, int i);
	public MaterialType findByNmaterialtypecodeAndNstatus(Integer integer, int i);
	public List<MaterialType> findByNstatusOrderByNmaterialtypecode(int i);
	public List<MaterialType> findAllByOrderByNmaterialtypecode();
	public List<MaterialType> findByNmaterialtypecodeNotAndNstatusOrderByNmaterialtypecode(int i, int j);
	public List<MaterialType> findByNstatusAndNmaterialtypecodeNotOrderByNmaterialtypecode(int i, int j);
	public List<MaterialType> findByNmaterialtypecodeNotOrderByNmaterialtypecode(int i);
	public List<MaterialType> findAllByOrderByNmaterialtypecodeDesc();
	public List<MaterialType> findByNdefaultstatusAndNsitecodeOrderByNmaterialtypecode(int i, Integer nsitecode);
	
	public List<MaterialType> findBySmaterialtypenameAndNdefaultstatusAndNsitecodeAndNmaterialtypecodeNot(
			String smaterialtypename, int i, Integer nsitecode, Integer nmaterialtypecode);
	public List<MaterialType> findBySmaterialtypenameAndNdefaultstatusAndNsitecodeOrderByNmaterialtypecode(
			String smaterialtypename, int i, Integer nsitecode);
	public List<MaterialType> findByNmaterialtypecodeNotAndNstatusAndNsitecodeOrNmaterialtypecodeNotAndNstatusAndNdefaultstatus(
			int i, int j, Integer nsitecode, int k, int l, int m);
	public List<MaterialType> findByAndSmaterialtypenameAndNsitecodeOrderByNmaterialtypecode(String smaterialtypename,
			Integer nsitecode);
	public List<MaterialType> findBySmaterialtypenameAndNsitecodeAndNmaterialtypecodeNot(String smaterialtypename,
			Integer nsitecode, Integer nmaterialtypecode);
	public List<MaterialType> findBySmaterialtypenameIgnoreCaseAndNsitecodeOrderByNmaterialtypecode(
			String smaterialtypename, Integer nsitecode);
	public List<MaterialType> findBySmaterialtypenameIgnoreCaseAndNsitecodeAndNmaterialtypecodeNot(
			String smaterialtypename, Integer nsitecode, Integer nmaterialtypecode);
	public List<MaterialType> findByNmaterialtypecodeNotAndNstatusAndNsitecodeOrNmaterialtypecodeNotAndNstatusAndNdefaultstatusOrderByNmaterialtypecodeDesc(
			int i, int j, Integer nsitecode, int k, int l, int m);
	public MaterialType findBySmaterialtypenameIgnoreCase(String string);
	@Query(value = "select nmaterialtypecode from materialtype where smaterialtypename = ?1 and nsitecode = ?2", nativeQuery = true)
	public Number getNameAndKey(Object value, Number sitecode);
	@Query(value = "select * from materialtype where smaterialtypename = ?1 and nsitecode = ?2 ", nativeQuery = true)
	public MaterialType getMaterialTypeDetails(String string, Number sitecode);
} 