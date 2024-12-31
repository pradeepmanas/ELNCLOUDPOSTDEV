package com.agaram.eln.primary.repository.communicationsetting;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.communicationsetting.CommunicationSetting;

public interface CommunicationRepository extends JpaRepository<CommunicationSetting, Long> {

	CommunicationSetting findByNequipmentcode(Integer nequipmentcode);

	Optional<CommunicationSetting> findByCmmsettingcode(Long id);

	
	@Transactional
	@Modifying
	@Query(value="select * from communicationsetting where cmmtype=1",nativeQuery = true)
	List<CommunicationSetting> GetInstrumentType();
}
