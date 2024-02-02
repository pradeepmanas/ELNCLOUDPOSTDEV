package com.agaram.eln.primary.repository.equipment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.equipment.EquipmentCategory;
import com.agaram.eln.primary.model.equipment.EquipmentType;

public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, Integer>{

	List<EquipmentCategory> findByNsitecodeOrderByNequipmentcatcodeDesc(Integer nsiteInteger);

	List<EquipmentCategory> findBySequipmentcatnameIgnoreCaseAndNsitecode(String sequipmentcatname, Integer nsitecode);

	EquipmentCategory findByNequipmentcatcode(Integer nequipmentcatcode);

	EquipmentCategory findByNsitecodeAndSequipmentcatnameIgnoreCase(Integer nsitecode, String sequipmentcatname);

	List<EquipmentCategory> findByNsitecodeAndNstatus(Integer nsiteInteger, int i);

	List<EquipmentCategory> findByEquipmenttypeAndNsitecodeAndNstatus(EquipmentType equipmentType, Integer nsiteInteger,
			int i);

	List<EquipmentCategory> findBySequipmentcatnameIgnoreCaseAndNsitecodeAndNstatus(String sequipmentcatname,
			Integer nsitecode, int i);

	EquipmentCategory findByNsitecodeAndSequipmentcatnameIgnoreCaseAndNstatus(Integer nsitecode,
			String sequipmentcatname, int i);

	List<EquipmentCategory> findByNsitecodeAndNstatusOrderByNequipmentcatcodeDesc(Integer nsiteInteger, int i);

	List<EquipmentCategory> findByEquipmenttypeAndNsitecodeAndNstatusOrderByNequipmentcatcodeDesc(
			EquipmentType equipmentType, Integer nsiteInteger, int i);

}
