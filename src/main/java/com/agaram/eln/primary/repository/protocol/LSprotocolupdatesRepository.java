package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolupdates;

public interface LSprotocolupdatesRepository extends JpaRepository<LSprotocolupdates, Integer>{

	List<LSprotocolupdates> findByprotocolmastercode(Integer protocolmastercode);

}
