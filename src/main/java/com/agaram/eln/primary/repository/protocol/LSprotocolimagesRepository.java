package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolimages;

public interface LSprotocolimagesRepository  extends JpaRepository<LSprotocolimages, Integer>{
	
	public LSprotocolimages findByfileid(String fileid);

	public List<LSprotocolimages> findByProtocolstepcode(Integer protocolstepcode);

}
