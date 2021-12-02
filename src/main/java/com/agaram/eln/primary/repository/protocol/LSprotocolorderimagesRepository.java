package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolorderimages;

public interface LSprotocolorderimagesRepository extends JpaRepository<LSprotocolorderimages, Integer>{

	List<LSprotocolorderimages> findByProtocolorderstepcode(Integer protocolorderstepcode);
	List findByProtocolordercodeAndProtocolorderstepcode(Long protocolordercode, Integer protocolorderstepcode);

}
