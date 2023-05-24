package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolorderfiles;

public interface LSprotocolorderfilesRepository extends JpaRepository<LSprotocolorderfiles, Integer>{

	List<LSprotocolorderfiles> findByProtocolordercodeAndProtocolorderstepcodeOrderByProtocolorderstepfilecodeDesc(
			Long protocolordercode, int protocolorderstepcode);

}
