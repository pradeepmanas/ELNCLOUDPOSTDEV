package com.agaram.eln.primary.repository.usermanagement;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.usermanagement.LScentralisedUsers;

public interface LScentralisedUsersRepository extends JpaRepository<LScentralisedUsers, Integer> {
	public LScentralisedUsers findByUnifieduseridIgnoreCase(String unifieduserid);
}
