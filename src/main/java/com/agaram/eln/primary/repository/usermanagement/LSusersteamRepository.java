package com.agaram.eln.primary.repository.usermanagement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;

public interface LSusersteamRepository  extends JpaRepository<LSusersteam, Integer> {

	public List<LSusersteam> findBystatus(Integer status);
	public List<LSusersteam> findByLsuserteammappingIn(List<LSuserteammapping> lsuserteammapping);
	public LSusersteam findByTeamnameAndStatus(String teamname,Integer status);
	public List<LSusersteam> findBylssitemasterAndStatus(LSSiteMaster lssitemaster,Integer status);
	public Object findByTeamnameAndStatusAndLssitemaster(String teamname, int i, LSSiteMaster lssitemaster);
	public LSusersteam findByteamcode(Integer teamcode);
	public LSusersteam findByTeamnameIgnoreCaseAndStatusAndLssitemaster(String teamname, int i, LSSiteMaster lssitemaster);
}
