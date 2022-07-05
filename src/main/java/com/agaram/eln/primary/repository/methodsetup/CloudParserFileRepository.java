package com.agaram.eln.primary.repository.methodsetup;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.methodsetup.CloudParserFile;


public interface CloudParserFileRepository extends JpaRepository<CloudParserFile, Integer> {

	void save(String namewithext);

	public CloudParserFile findByfilename(String fileName);

	public CloudParserFile findTop1Byfilename(String fileName);

}
