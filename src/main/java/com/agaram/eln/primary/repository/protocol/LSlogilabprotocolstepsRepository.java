package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSlogilabprotocolsteps;

public interface LSlogilabprotocolstepsRepository extends JpaRepository<LSlogilabprotocolsteps,Integer>{
	List<LSlogilabprotocolsteps> findByProtocolordercode(Long protocolordercode);
	LSlogilabprotocolsteps findByProtocolorderstepcode(Integer protocolordercode);
	List<LSlogilabprotocolsteps> findByprotocolorderstepcode(Integer protocolordercode);
//	List<LSlogilabprotocolsteps> findByProtocolordercodeAndStepno(long ipInt, int i);
//	List<LSlogilabprotocolsteps> findByProtocolordercodeAndStepnoNot(long ipInt, int i);
//	int countByProtocolordercode(long ipInt);
	List<LSlogilabprotocolsteps> findByProtocolordercodeAndStatus(Long protocolordercode,int i);
	List<LSlogilabprotocolsteps> findByProtocolordercodeAndStatusAndStepno(long ipInt, int i, int j);
	List<LSlogilabprotocolsteps> findByProtocolordercodeAndStatusAndStepnoNot(long ipInt, int i, int j);
	int countByProtocolordercodeAndStatus(long ipInt, int i);
	List<LSlogilabprotocolsteps> findByProtocolordercodeAndProtocolmastercode(Long protocolordercode,
			Integer protocolmastercode);
}
