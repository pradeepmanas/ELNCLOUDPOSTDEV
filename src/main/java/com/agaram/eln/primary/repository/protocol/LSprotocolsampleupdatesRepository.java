package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolsampleupdates;

public interface LSprotocolsampleupdatesRepository  extends JpaRepository<LSprotocolsampleupdates, Integer>{

	public List<LSprotocolsampleupdates> findByprotocolstepcode(Integer protocolstepcode);

	public List<LSprotocolsampleupdates> findByProtocolmastercode(Integer protocolmastercode);

	public List<LSprotocolsampleupdates> findByprotocolstepcodeAndProtocolmastercode(Integer protocolstepcode,
			Object object);

//	public List<LSprotocolsampleupdates> findByprotocolstepcodeAndIndexIsNotNull(Integer protocolstepcode);

//	public List<LSprotocolsampleupdates> findByprotocolstepcodeAndProtocolmastercodeAndIndexIsNotNull(
//			Integer protocolstepcode, Integer protocolmastercode);

	public List<LSprotocolsampleupdates> findByprotocolstepcodeAndProtocolmastercodeAndIndexofIsNotNullAndStatus(
			Integer protocolstepcode, Integer protocolmastercode, int i);

	public List<LSprotocolsampleupdates> findByprotocolstepcodeAndIndexofIsNotNullAndStatus(Integer protocolstepcode,
			int i);




	public List<LSprotocolsampleupdates> findByRepositorydatacodeAndUsedquantityNotAndStatusOrderByProtocolsamplecodeDesc(
			Integer repositorydatacode, int i, int j);
}
