package com.agaram.eln.primary.repository.communicationsetting;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.communicationsetting.CommunicationSetting;

public interface CommunicationRepository extends JpaRepository<CommunicationSetting, Long> {

	CommunicationSetting findByNequipmentcode(Integer nequipmentcode);

	Optional<CommunicationSetting> findByCmmsettingcode(Long id);
}
