package com.agaram.eln.primary.repository.protocol;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.protocols.LSprotocolselectedteam;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;

@Repository
public interface LSprotocolselectedteamRepository extends JpaRepository<LSprotocolselectedteam, Integer> {

	List<LSprotocolselectedteam> findByUserteamInAndCreatedtimestampBetween(List<LSusersteam> lstteam, Date fromdate,
			Date todate);

	List<LSprotocolselectedteam> findByProtocolordercode(Long protocolordercode);

}
