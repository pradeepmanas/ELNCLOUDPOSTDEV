package com.agaram.eln.primary.repository.adsconnection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.adsconnection.Tbladssettings;

public interface TbladssettingsRepository extends JpaRepository<Tbladssettings, Integer>{

	public Tbladssettings findByLdaptatus(Integer status);
}
