package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolversion;

public interface LSprotocolversionRepository extends JpaRepository<LSprotocolversion, Integer>{

	public LSprotocolversion findFirstByProtocolmastercodeOrderByVersionnoDesc(Integer protocolmastercode);

	public List<LSprotocolversion> findByprotocolmastercode(Integer protocolmastercode);

	public List<LSprotocolversion> findByprotocolmastercode(Object object);

}
