package com.agaram.eln.primary.repository.protocol;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolordervideos;
import com.agaram.eln.primary.model.protocols.Protocolordervideos;

public interface LSprotocolordervideosRepository extends JpaRepository<LSprotocolordervideos, String>{

	Protocolordervideos findByFileid(String fileid);

}
