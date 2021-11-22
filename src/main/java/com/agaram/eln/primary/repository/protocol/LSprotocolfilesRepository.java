package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolfiles;

public interface LSprotocolfilesRepository extends JpaRepository<LSprotocolfiles, Integer>{
	public List<LSprotocolfiles> findByProtocolmastercodeAndProtocolstepcodeOrderByProtocolstepfilecodeDesc(Integer protocolcode, Integer stepcode);
	List<LSprotocolfiles> findByProtocolstepcode(Integer protocolstepcode);

}
