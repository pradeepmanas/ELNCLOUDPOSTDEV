package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolorderworkflowhistory;

public interface LSprotocolorderworkflowhistoryRepository extends JpaRepository<LSprotocolorderworkflowhistory, Integer>{

	List<LSprotocolorderworkflowhistory> findByProtocolordercode(Integer protocolordercode);

}
