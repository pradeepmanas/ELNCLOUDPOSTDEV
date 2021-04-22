package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolworkflowhistory;


public interface LSprotocolworkflowhistoryRepository extends JpaRepository<LSprotocolworkflowhistory, Integer>{

	List<LSprotocolworkflowhistory> findByProtocolmastercode(Integer protocolmastercode);

}
