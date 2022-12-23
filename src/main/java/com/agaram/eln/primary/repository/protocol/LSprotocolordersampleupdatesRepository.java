package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.protocols.LSprotocolordersampleupdates;
import com.agaram.eln.primary.repository.instrumentDetails.LsOrderSampleUpdateRepository.UserProjection;


	public interface LSprotocolordersampleupdatesRepository  extends JpaRepository<LSprotocolordersampleupdates, Long>{
		
		public List<LSprotocolordersampleupdates> findByprotocolordercode(Long protocolordercode);

		public List<LSprotocolordersampleupdates> findByprotocolordercodeAndProtocolstepcode(Long protocolordercode,Integer protocolstepcode);

		public List<LSprotocolordersampleupdates> findByProtocolordercodeAndProtocolstepcodeAndIndexofIsNotNullAndStatus(
				Long protocolordercode, Integer protocolstepcode, int i);

		public List<LSprotocolordersampleupdates> findByProtocolstepcodeAndIndexofIsNotNullAndStatus(
				 Integer protocolstepcode, int i);

		

		public List<LSprotocolordersampleupdates> findByRepositorydatacodeAndUsedquantityNotAndStatusOrderByProtocolsamplecodeDesc(
				Integer repositorydatacode, int i, int j);

		public interface UserProjection {
		    String getBatchcode();
		    String getBatchname();
		    Integer getUsercode();

		}

		
		public interface UserProjection1 {
		    String getProtocolordercode();
		    String getBatchname();
		    Integer getUsercode();
//		    String getComment();
		}

		@Transactional
		@Modifying
		@Query(value = "select DISTINCT ls.protocolordercode ,(select protocolordername from lslogilabprotocoldetail  where protocolordercode=ls.protocolordercode ) as batchname,usercode from LSprotocolordersampleupdates ls where repositorydatacode=?", nativeQuery = true)
	public List<UserProjection1> getDistinctRepositorydatacode(Integer repositorydatacode);



	
}
