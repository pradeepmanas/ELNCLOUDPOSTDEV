package com.agaram.eln.primary.repository.equipment;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.equipment.Equipment;
import com.agaram.eln.primary.model.equipment.EquipmentCategory;
import com.agaram.eln.primary.model.equipment.EquipmentType;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer>{

	List<Equipment> findByNsitecodeAndCreateddateBetweenOrderByNequipmentcodeDesc(Integer nsiteInteger, Date fromDate,
			Date toDate);

	List<Equipment> findByEquipmentcategoryAndNsitecodeAndCreateddateBetweenOrderByNequipmentcodeDesc(
			EquipmentCategory objCategory, Integer nsiteInteger, Date fromDate, Date toDate);

	List<Equipment> findByEquipmenttypeAndNsitecodeAndCreateddateBetweenOrderByNequipmentcodeDesc(EquipmentType objType,
			Integer nsiteInteger, Date fromDate, Date toDate);

	List<Equipment> findByEquipmenttypeAndEquipmentcategoryAndNsitecodeAndCreateddateBetweenOrderByNequipmentcodeDesc(
			EquipmentType objType, EquipmentCategory objCategory, Integer nsiteInteger, Date fromDate, Date toDate);

	List<Equipment> findByNsitecodeAndNstatusOrderByNequipmentcodeDesc(Integer nsiteInteger, int i);

	Equipment findByNsitecodeAndSequipmentnameAndEquipmentcategory(Integer nsitecode, String sequipmentname,
			EquipmentCategory equipmentcategory);

	Equipment findByNsitecodeAndSequipmentnameAndEquipmentcategoryAndNequipmentcodeNot(Integer nsitecode,
			String sequipmentname, EquipmentCategory equipmentcategory, Integer nequipmentcode);

	List<Equipment> findBySequipmentnameStartingWithIgnoreCaseAndNsitecode(String searchString, Integer nsiteInteger);

	List<Equipment> findBySequipmentnameStartingWithIgnoreCase(String searchname);

	List<Equipment> findByNequipmentcodeIn(List<Integer> nequipmentcode);

	List<Equipment> findByEquipmentusedAndNsitecodeAndCreateddateBetweenOrderByNequipmentcodeDesc(boolean b,
			Integer nsiteInteger, Date fromDate, Date toDate);

	List<Equipment> findByEquipmentcategoryAndNsitecodeAndNstatus(EquipmentCategory equipmentCategory,
			Integer nsiteInteger, int i);

	List<Equipment> findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatus(boolean b,
			EquipmentCategory equipmentCategory, Integer nsiteInteger, int i);

	List<Equipment> findByEquipmentusedAndEquipmentcategoryAndNsitecode(boolean b, EquipmentCategory equipmentCategory,
			Integer nsiteInteger);

	List<Equipment> findByEquipmentusedAndNsitecodeAndNstatusAndCreateddateBetweenOrderByNequipmentcodeDesc(boolean b,
			Integer nsiteInteger, int i, Date fromDate, Date toDate);

	List<Equipment> findByEquipmentusedAndNsitecodeAndNstatusAndCreateddateBetweenAndLastmaintainedNotNullAndLastcallibratedNotNullOrderByNequipmentcodeDesc(
			boolean b, Integer nsiteInteger, int i, Date fromDate, Date toDate);

	List<Equipment> findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatusAndLastmaintainedNotNullAndLastcallibratedNotNull(
			boolean b, EquipmentCategory equipmentCategory, Integer nsiteInteger, int i);

}
