package com.agaram.eln.primary.repository.usermanagement;

import org.springframework.data.jpa.repository.JpaRepository;


import com.agaram.eln.primary.model.usermanagement.Lsusersettings;

public interface LsusersettingsRepository extends JpaRepository<Lsusersettings, Integer>{

	Lsusersettings findByUsercode(Integer usercode);
	
}