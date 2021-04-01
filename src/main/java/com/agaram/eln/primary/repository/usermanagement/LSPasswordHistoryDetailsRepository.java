package com.agaram.eln.primary.repository.usermanagement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.usermanagement.LSPasswordHistoryDetails;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public interface LSPasswordHistoryDetailsRepository extends JpaRepository<LSPasswordHistoryDetails, Integer>{
	public LSPasswordHistoryDetails findTopByOrderByPasswordcodeDesc();
	public LSPasswordHistoryDetails findTopByPasswordOrderByPasswordcodeDesc(String password);
	public List<LSPasswordHistoryDetails> findTop5ByAndLsusermasterInOrderByPasswordcodeDesc(LSuserMaster lsusermaster);
}
