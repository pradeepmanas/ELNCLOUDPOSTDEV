package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolsampleupdates;

public interface LSprotocolsampleupdatesRepository  extends JpaRepository<LSprotocolsampleupdates, Integer>{

	public List<LSprotocolsampleupdates> findByprotocolstepcode(Integer protocolstepcode);

	public List<LSprotocolsampleupdates> findByProtocolmastercode(Integer protocolmastercode);
}
