package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolordersampleupdates;

	public interface LSprotocolordersampleupdatesRepository  extends JpaRepository<LSprotocolordersampleupdates, Long>{
		
		public List<LSprotocolordersampleupdates> findByprotocolordercode(Long protocolordercode);

		public List<LSprotocolordersampleupdates> findByprotocolordercodeAndProtocolstepcode(Long protocolordercode,Integer protocolstepcode);
}
