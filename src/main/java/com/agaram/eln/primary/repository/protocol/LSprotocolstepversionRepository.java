package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.agaram.eln.primary.model.protocols.LSprotocolstepversion;

public interface LSprotocolstepversionRepository extends JpaRepository<LSprotocolstepversion, Integer>{
	
	List<LSprotocolstepversion> findByprotocolmastercodeAndVersionno(Integer protocolmastercode,Integer versionno);
	
	LSprotocolstepversion findByprotocolstepcodeAndVersionno(Integer protocolstepcode,Integer versionno);

}
