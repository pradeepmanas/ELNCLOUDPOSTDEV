package com.agaram.eln.primary.repository.material;

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

//	Material findBynstatusAndnmaterialcode(int i, Integer integer);
}