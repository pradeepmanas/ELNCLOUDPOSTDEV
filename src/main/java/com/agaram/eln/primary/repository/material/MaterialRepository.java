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

	Material findBySmaterialnameAndNmaterialtypecodeAndNstatusAndNsitecode(String string, Integer integer, int i,
			Integer nsiteInteger);

	List<Material> findByNmaterialcatcodeAndNmaterialtypecodeAndNsitecode(Integer integer, Integer integer2,
			Integer nsiteInteger);

	List<Material> findByNmaterialcatcodeAndNmaterialtypecodeAndNsitecodeAndCreateddateBetween(Integer integer,
			Integer integer2, Integer nsiteInteger, Date fromDate, Date toDate);

	Material findByNmaterialcode(Integer integer);

	Material findBySmaterialnameAndNmaterialtypecodeAndNsitecode(String string, Integer integer, Integer nsiteInteger);

	Material findBySmaterialnameAndNmaterialtypecode(String string, Integer integer);

	Material findBySprefixAndNmaterialtypecode(String string, Integer integer);
	
	List<Material> findByNmaterialtypecodeAndSprefix(Integer integer,String string);

	Material findByNstatusAndSprefixAndNsitecode(int i, String string, Integer nsiteInteger);

	List<Material> findByNmaterialcatcodeAndNmaterialtypecodeAndNstatusOrderByNmaterialcode(Integer nmaterialcatcode,
			Integer nmaterialtypecode, int i);

	List<Material> findByNmaterialcatcodeAndNstatusOrderByNmaterialcode(Integer nmaterialcatcode, int i);

//	Material findBynstatusAndnmaterialcode(int i, Integer integer);
}