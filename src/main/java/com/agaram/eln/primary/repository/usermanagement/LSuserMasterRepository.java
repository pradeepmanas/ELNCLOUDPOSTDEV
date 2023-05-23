package com.agaram.eln.primary.repository.usermanagement;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.usermanagement.LSuserMaster;



public interface LSuserMasterRepository extends JpaRepository<LSuserMaster, Integer> {


	public LSuserMaster findByUsernameIgnoreCase(String usernamevalue);
}