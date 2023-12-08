package com.agaram.eln.primary.repository.protocol;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolorderversion;

public interface LSprotocolorderversionRepository extends JpaRepository<LSprotocolorderversion, Integer> {

	List<LSprotocolorderversion> findByProtocolordercode(long ipInt);

	List<LSprotocolorderversion> findByProtocolordercodeOrderByVersionnoDesc(long ipInt);

	List<LSprotocolorderversion> findByProtocolordercodeAndStatus(Long protocolordercode, int i);

	List<LSprotocolorderversion> findByProtocolordercodeAndStatusAndCreatedateBetween(Long protocolordercode, int i,
			Date fromdate, Date todate);

	LSprotocolorderversion findFirstByProtocolordercodeAndVersionno(Long protocolordercode, Integer versionno);

}
