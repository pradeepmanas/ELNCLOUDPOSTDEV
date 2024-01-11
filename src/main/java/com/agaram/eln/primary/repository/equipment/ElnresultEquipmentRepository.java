package com.agaram.eln.primary.repository.equipment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.equipment.ElnresultEquipment;

public interface ElnresultEquipmentRepository extends JpaRepository<ElnresultEquipment, Integer>{

	List<ElnresultEquipment> findByNequipmentcode(Integer nequipmentcode);

}
