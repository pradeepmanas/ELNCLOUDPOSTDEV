package com.agaram.eln.primary.repository.cloudFileManip;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agaram.eln.primary.model.cloudFileManip.CloudOrderCreation;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;

public interface CloudOrderCreationRepository  extends JpaRepository<CloudOrderCreation, Long> {
	public CloudOrderCreation findById(Long Id);
	public List<CloudOrderCreation> findByContentvaluesContaining(String Value);
	public List<CloudOrderCreation> findByContentparameterContaining(String Value);
	
	@Query(value = "SELECT * FROM lsOrderCreationfiles WHERE text(contentvalues) like %:value%", nativeQuery = true)
	public List<CloudOrderCreation> findByContentvaluesequal(@Param("value") String value);
	
	@Query(value = "SELECT * FROM lsOrderCreationfiles WHERE text(contentparameter) like %:value%", nativeQuery = true)
	public List<CloudOrderCreation> findByContentparameterequal(@Param("value") String value);
	@Transactional
	@Modifying
	@Query(value = "select fileuid from "
			+ "lsordercreationfiles where id in (?1) ORDER BY id DESC", nativeQuery=true)
	public List<CloudOrderCreationRepository> getFileuid(List<Integer> sampleFileCodeList1);

}
