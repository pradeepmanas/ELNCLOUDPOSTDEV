package com.agaram.eln.primary.repository.cloudProtocol;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.cloudProtocol.CloudLSprotocolorderversionstep;

public interface CloudLSprotocolorderversionstepRepository extends JpaRepository<CloudLSprotocolorderversionstep, Integer>{

	CloudLSprotocolorderversionstep findByProtocolorderstepversioncode(Integer protocolorderstepversioncode);

}
