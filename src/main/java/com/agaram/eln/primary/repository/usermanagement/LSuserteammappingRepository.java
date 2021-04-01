package com.agaram.eln.primary.repository.usermanagement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;

public interface LSuserteammappingRepository  extends JpaRepository<LSuserteammapping, Integer> {
	public List<LSuserteammapping> findBylsuserMaster(LSuserMaster lsuserMaster);
	
	public List<LSuserteammapping> findByTeamcodeNotNullAndLsuserMaster(LSuserMaster lsuserMaster);
	
	public LSuserteammapping findByLsuserMaster(LSuserMaster lsuserMaster);
	
	public List<LSuserteammapping> findByteamcode(Integer teamcode);
	
	@Query(value = "select l.teamcode from LSuserteammapping l where lsusermaster_usercode = :userID and l.teamcode is not null", nativeQuery=true)
	public List<Integer> getTeamcodeByLsuserMaster(@Param("userID") Integer userid);
	
	@Query("select l.teamcode from LSuserteammapping l where lsusermaster_usercode = ?1 and l.teamcode is not null")
	public List<Integer> getTeamcodeByLsuserMaster4postgressandsql(Integer userid);
	
	@Query("select l.lsuserMaster from LSuserteammapping l where l.teamcode in (:teamCode)")
	public List<LSuserMaster> getLsuserMasterByTeamcode(@Param("teamCode") List<Integer> teamCode);

}
