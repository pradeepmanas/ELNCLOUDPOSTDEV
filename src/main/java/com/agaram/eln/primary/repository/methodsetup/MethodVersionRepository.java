package com.agaram.eln.primary.repository.methodsetup;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.methodsetup.MethodVersion;

@Repository
public interface MethodVersionRepository extends JpaRepository<MethodVersion, Integer>{

	ArrayList<MethodVersion> findByMethodkey(int methodKey);

//	List<MethodVersion> findByMethodkeyAndVersion(Integer methodkey, Integer version);

//	List<MethodVersion> findByOrderByMethodkeyDesc(int methodKey);

	List<MethodVersion> findByMethodkeyOrderByVersionDesc(int methodKey);
	
}