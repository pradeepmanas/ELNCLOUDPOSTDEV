package com.agaram.eln.primary.repository.material;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.Material;

public interface MaterialRepository  extends JpaRepository<Material, Integer>{

	List<Material> findByNmaterialcatcode(Integer nmaterialcatcode);
	
	List<Integer> findByNmaterialcodeAndNstatus(Integer nmaterialcode,Integer i);
	
	Material findByNstatusAndNmaterialcode(Integer nstatus,Integer nmaterialcode);
	
	Material findBySmaterialname(String smaterialname);

	Material findBySmaterialnameAndNstatus(String string, Integer i);

	List<Material> findByNmaterialcatcodeAndNmaterialtypecodeAndNstatus(Integer nmaterialcatcode, Integer nmaterialtypecode, int i);

	int countByNmaterialcodeAndNstatus(Integer integer, int i);

	List<Material> findByNmaterialcatcodeAndNmaterialtypecodeAndNstatusOrderByNmaterialcodeDesc(Integer integer,
			Integer integer2, int i);

	Material findByNstatusAndSprefix(int i, String string);

	List<Material> findByNmaterialcatcodeOrderByNmaterialcode(Integer nmaterialcatcode);

	List<Material> findByNstatus(int i);

	List<Material> findByNmaterialcatcodeAndNmaterialtypecodeAndNsitecodeAndNstatus(Integer integer, Integer integer2,
			Integer nsiteInteger, int i);

	List<Material> findByNmaterialcatcodeAndNsitecodeOrderByNmaterialcode(Integer nmaterialcatcode,
			Integer nsiteInteger);

	List<Material> findByNstatusAndNsitecodeOrderByNmaterialcodeDesc(int i, Integer nsiteInteger);

	List<Material> findByNmaterialcatcodeAndNsitecodeOrderByNmaterialcodeDesc(Integer nmaterialcatcode,
			Integer nsiteInteger);

	List<Material> findByNmaterialcatcodeOrderByNmaterialcodeDesc(Integer nCategoryCode);

	List<Material> findBySmaterialnameInAndNstatusAndNsitecode(List<String> lstMatName, int i, Integer sitecode);

	List<Material> findBySprefixInAndNstatusAndNsitecode(List<String> lstPrefixName, int i, Integer sitecode);

	List<Material> findByNmaterialcatcodeAndNmaterialtypecodeAndNsitecodeAndNstatusAndCreateddateBetween(
			Integer integer, Integer integer2, Integer nsiteInteger, int i, Date fromDate, Date toDate);

	Material findBySmaterialnameAndNmaterialtypecodeAndNstatus(String string, Integer integer, int i);

	List<Material> findBySmaterialnameInAndAndNmaterialtypecodeAndNstatusAndNsitecode(List<String> lstMatName,
			Integer nmaterialtypecode, int i, Integer sitecode);

//	Material findBynstatusAndnmaterialcode(int i, Integer integer);
}