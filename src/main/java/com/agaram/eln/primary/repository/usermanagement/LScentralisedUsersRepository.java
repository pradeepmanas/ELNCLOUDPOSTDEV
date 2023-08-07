package com.agaram.eln.primary.repository.usermanagement;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LScentralisedUsers;

public interface LScentralisedUsersRepository extends JpaRepository<LScentralisedUsers, Integer> {
	public LScentralisedUsers findByUnifieduseridIgnoreCase(String unifieduserid);
	
	@Transactional
	@Modifying
	@Query("update LScentralisedUsers o set o.sitecode =?1 where o.usercode =?2")
	void updatesitecodeonLScentralisedUsers(Integer sitecode,Integer usercode);
}
