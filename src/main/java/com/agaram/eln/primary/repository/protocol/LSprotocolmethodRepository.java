package com.agaram.eln.primary.repository.protocol;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolmethod;

public interface LSprotocolmethodRepository extends JpaRepository<LSprotocolmethod, Integer>{

	@Transactional
	void deleteByprotocolmastercode(Integer protocolmastercode);

}
