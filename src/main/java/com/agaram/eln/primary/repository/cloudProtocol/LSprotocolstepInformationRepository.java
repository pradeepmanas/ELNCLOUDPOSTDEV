package com.agaram.eln.primary.repository.cloudProtocol;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.cloudProtocol.LSprotocolstepInformation;

public interface LSprotocolstepInformationRepository extends JpaRepository<LSprotocolstepInformation, Integer> {

	LSprotocolstepInformation findById(Integer protocolstepcode);

}
