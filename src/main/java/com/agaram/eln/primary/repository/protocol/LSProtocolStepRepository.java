package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSlogilabprotocolsteps;
import com.agaram.eln.primary.model.protocols.LSprotocolstep;

public interface LSProtocolStepRepository extends JpaRepository<LSprotocolstep, Integer>{

	List<LSprotocolstep> findByProtocolmastercode(Integer protocolmastercode);
	
	List<LSlogilabprotocolsteps> findByprotocolmastercode(Integer protocolmastercode);
	
	List<LSprotocolstep> findByProtocolmastercodeAndStatus(Integer protocolmastercode, Integer status);
	
	@SuppressWarnings("unchecked")
	LSprotocolstep save(LSprotocolstep LSprotocolstepObj);
	
	List<LSprotocolstep> findByStatusAndSitecode(Integer status, Integer sitecode);

	List<LSprotocolstep> findByProtocolmastercodeAndStatus(Object object, int status);

	LSprotocolstep findByProtocolstepcodeAndStatus(Integer protocolstepcode, int i);


	LSprotocolstep findByProtocolmastercodeAndProtocolstepcodeAndStatus(Integer protocolmastercode,
			int protocolstepcode, int i);

	List<LSprotocolstep> findByProtocolmastercodeAndProtocolstepnameAndStatus(Integer protocolmastercode,
			String protocolstepname, int i);
}
