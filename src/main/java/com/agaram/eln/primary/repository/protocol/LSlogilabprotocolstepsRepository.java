package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSlogilabprotocolsteps;

public interface LSlogilabprotocolstepsRepository extends JpaRepository<LSlogilabprotocolsteps,Integer>{
	List<LSlogilabprotocolsteps> findByProtocolordercode(Long protocolordercode);
	LSlogilabprotocolsteps findByProtocolorderstepcode(Integer protocolordercode);
	List<LSlogilabprotocolsteps> findByprotocolorderstepcode(Integer protocolordercode);
}
