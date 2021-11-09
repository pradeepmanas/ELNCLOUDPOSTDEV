package com.agaram.eln.primary.repository.sheetManipulation;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sheetManipulation.temporaryfilestorage;

public interface temporaryfilestorageRepository extends JpaRepository<temporaryfilestorage,Integer>{

	public temporaryfilestorage findById(String id);

}
