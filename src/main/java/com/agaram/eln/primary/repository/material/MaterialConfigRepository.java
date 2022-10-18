package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.MaterialConfig;

public interface MaterialConfigRepository extends JpaRepository<MaterialConfig, Integer>{
	public List<MaterialConfig> findByNmaterialtypecodeAndNformcode(Integer nmaterialtypecode,Integer nformcode);
	public List<MaterialConfig> findByNformcode(Integer nformcode);
	public List<MaterialConfig> findByNmaterialconfigcode(Integer nmaterialconfigcode);
	public List<MaterialConfig> findByNmaterialtypecodeAndNformcodeAndNstatus(Integer nmaterialtypecode,Integer nformcode,Integer nstatus);
	public MaterialConfig findByNformcodeAndNmaterialtypecodeAndNstatus(int i, int j, int k);
	public List<MaterialConfig> findByNformcodeAndNmaterialtypecodeAndNstatusOrderByNmaterialconfigcode(int i,
			int ntypecode, int j);
}
