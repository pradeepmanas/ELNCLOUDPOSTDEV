package com.agaram.eln.primary.repository.webParser;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.fetchmodel.getorders.Logilaborders;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.webParser.Lswebparsermethod;

public interface LswebparsermethodRepository  extends JpaRepository<Lswebparsermethod,Integer> {
	

}
