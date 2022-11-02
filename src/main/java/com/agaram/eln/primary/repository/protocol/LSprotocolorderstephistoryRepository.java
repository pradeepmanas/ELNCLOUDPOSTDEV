package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolorderstephistory;

public interface LSprotocolorderstephistoryRepository extends JpaRepository<LSprotocolorderstephistory, Integer> {

	List<LSprotocolorderstephistory> findByProtocolordercode(Long protocolordercode);

}
