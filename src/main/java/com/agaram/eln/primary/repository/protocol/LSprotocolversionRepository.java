package com.agaram.eln.primary.repository.protocol;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolversion;

public interface LSprotocolversionRepository extends JpaRepository<LSprotocolversion, Integer>{

	public LSprotocolversion findFirstByProtocolmastercodeOrderByVersionnoDesc(Integer protocolmastercode);

	public List<LSprotocolversion> findByprotocolmastercode(Integer protocolmastercode);

	public List<LSprotocolversion> findByprotocolmastercode(Object object);

	public List<LSprotocolversion> findByprotocolmastercodeIn(List<Integer> lstprotocoltemp);

	public LSprotocolversion findFirstByProtocolmastercodeAndVersionno(Integer protocolmastercode, Integer versionno);

	public List<LSprotocolversion> findByprotocolmastercodeOrderByVersionnoDesc(Integer protocolmastercode);

	public List<LSprotocolversion> findByprotocolmastercodeAndCreatedateBetween(Integer protocolmastercode,
			Date fromdate, Date todate);

}
