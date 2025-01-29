package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.fetchmodel.getorders.Logilaborders;
import com.agaram.eln.primary.model.instrumentDetails.LSSelectedTeam;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;


@Repository
public interface LSSelectedTeamRepository extends JpaRepository<LSSelectedTeam, Integer> {

	List<LSSelectedTeam> findByUserteamIn(List<LSusersteam> lsusersteam);

	List<LSSelectedTeam> findByBatchcode(Long batchcode);

	List<LSSelectedTeam> findByUserteamInAndCreatedtimestampBetween(List<LSusersteam> userteamlist, Date fromdate,
			Date todate);

	

}