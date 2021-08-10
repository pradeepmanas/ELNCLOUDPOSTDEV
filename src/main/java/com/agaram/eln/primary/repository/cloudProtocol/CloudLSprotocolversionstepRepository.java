package com.agaram.eln.primary.repository.cloudProtocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.cloudProtocol.CloudLSprotocolversionstep;


public interface CloudLSprotocolversionstepRepository extends JpaRepository<CloudLSprotocolversionstep, Integer>{
	public CloudLSprotocolversionstep findByVersionno(Integer versionno);

	public CloudLSprotocolversionstep findById(Integer protocolversioncode);

	public List<CloudLSprotocolversionstep> findByprotocolmastercode(Integer protocolmastercode);

	public CloudLSprotocolversionstep findByIdAndVersionno(Integer protocolstepcode, Integer versionno);
}
