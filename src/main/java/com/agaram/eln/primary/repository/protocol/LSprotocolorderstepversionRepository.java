package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolorderstepversion;

public interface LSprotocolorderstepversionRepository extends JpaRepository<LSprotocolorderstepversion, Integer>{

	LSprotocolorderstepversion findByProtocolorderstepcodeAndVersionno(Integer protocolorderstepcode,
			Integer versionno);

	List<LSprotocolorderstepversion> findByProtocolordercodeAndVersionno(Long protocolordercode, Integer versionno);

//	List<LSprotocolorderstepversion> findByProtocolordercodeAndVersionnoOrderByVersionno(Long protocolordercode,
//			Integer versionno);

	List<LSprotocolorderstepversion> findByProtocolordercodeAndVersionnoAndStatusOrderByVersionno(
			Long protocolordercode, Integer versionno, int i);

	int countByProtocolordercodeAndStatusAndVersionno(Long protocolordercode, int i, Integer versionno);

}
