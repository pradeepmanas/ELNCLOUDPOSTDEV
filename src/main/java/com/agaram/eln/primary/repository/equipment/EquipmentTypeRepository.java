package com.agaram.eln.primary.repository.equipment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.equipment.EquipmentType;

public interface EquipmentTypeRepository extends JpaRepository<EquipmentType, Integer>{
//	public List<EquipmentType>  findByNequipmenttypecodeNotAndNstatusAndNsitecodeOrNequipmenttypecodeNotAndNstatusAndNdefaultstatusOrderByNequipmenttypecodeDesc(
//			int i, int j, Integer nsitecode, int k, int l, int m);

	public List<EquipmentType>  findByNequipmenttypecodeNotAndNstatusAndNsitecodeOrderByNequipmenttypecodeDesc(
			int i, int j, Integer nsitecode);
	
	public EquipmentType findByNequipmenttypecodeAndNstatus(Integer Nequipmenttypecode, int i);

	public List<EquipmentType> findBySequipmenttypenameIgnoreCaseAndNsitecodeOrderByNequipmenttypecode(
			String sequipmenttypename, Integer nsitecode);

	public List<EquipmentType> findBySequipmenttypenameIgnoreCaseAndNsitecodeAndNequipmenttypecodeNot(
			String sequipmenttypename, Integer nsitecode, Integer nequipmenttypecode);

//	public List<EquipmentType> findByNequipmenttypecodeNotAndNstatusAndNsitecodeOrNequipmenttypecodeNotAndNstatusAndNdefaultstatus(
//			int i, int j, Integer nsiteInteger, int k, int l, int m);

	public List<EquipmentType> findByNequipmenttypecode(Integer ntypecode);

	public List<EquipmentType> findByNstatusAndNsitecodeOrderByNequipmenttypecodeDesc(int i, Integer nsiteInteger);
}