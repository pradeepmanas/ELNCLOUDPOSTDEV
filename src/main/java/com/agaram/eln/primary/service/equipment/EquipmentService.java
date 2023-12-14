package com.agaram.eln.primary.service.equipment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.repository.equipment.EquipmentRepository;

@Service
public class EquipmentService {

	@Autowired
	EquipmentRepository equipmentRepository;
}
