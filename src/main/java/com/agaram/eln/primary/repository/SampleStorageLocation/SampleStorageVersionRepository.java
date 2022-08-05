package com.agaram.eln.primary.repository.SampleStorageLocation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.SampleStorageLocation.SampleStorageLocation;
import com.agaram.eln.primary.model.SampleStorageLocation.SampleStorageVersion;


public interface SampleStorageVersionRepository extends JpaRepository<SampleStorageVersion, Integer>{

	Optional<SampleStorageVersion>  findBySampleStorageLocationAndVersionno(final SampleStorageLocation Samplestoragelocation, final int Versionno);
	
	List<SampleStorageVersion>  findBySampleStorageLocationAndApprovalstatus(final SampleStorageLocation Samplestoragelocation, final int Approvalstatus);
	
	List<SampleStorageVersion>  findBySampleStorageLocationAndVersionnoNotIn(final SampleStorageLocation Samplestoragelocation, final List<Integer> Versionno);
	
	List<SampleStorageVersion>  findBySampleStorageLocation(final SampleStorageLocation Samplestoragelocation);	
	
	@Query( value = " select max(versionno) + 1 from SampleStorageVersion where samplestoragelocationkey = ?1 ", nativeQuery = true) 
	
	int getMaxVersionNo(final int sampleStorageLocationKey);

}
