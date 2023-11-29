package com.agaram.eln.primary.repository.samplestoragelocation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.samplestoragelocation.SampleStorageLocation;

public interface SampleStorageLocationRepository extends JpaRepository<SampleStorageLocation, Integer> {

	public Optional<SampleStorageLocation> findByStatusAndSamplestoragelocationkey(final int Status, final int Samplestoragelocationkey);

//	public List<SampleStorageLocation> findByStatus(final int Status, final Sort sort);

	public List<SampleStorageLocation> findByStatus(int i);
	
	public List<Object> findByStatusAndSitekeyOrderBySamplestoragelocationkeyDesc(Integer nstatus,Integer nsiteInteger);

	public SampleStorageLocation findBySamplestoragelocationkey(int sampleStorageLocationKey);

	public SampleStorageLocation findBySamplestoragelocationnameAndStatus(String samplestoragelocationname, int i);

	public List<SampleStorageLocation> findBystatusOrderBySamplestoragelocationkeyDesc(int i);

	public List<SampleStorageLocation> findBystatusAndSitekeyOrderBySamplestoragelocationkeyDesc(int i,
			Integer nsiteInteger);

	public SampleStorageLocation findBySamplestoragelocationnameAndStatusAndSitekey(String samplestoragelocationname,
			int i, Integer sitekey);

	public List<SampleStorageLocation> findBySitekeyOrderBySamplestoragelocationkeyDesc(Integer nsiteInteger);

	public SampleStorageLocation findBySamplestoragelocationnameAndSitekey(String samplestoragelocationname,
			Integer sitekey);

	public Optional<SampleStorageLocation> findBySitekeyAndSamplestoragelocationkey(Integer sitekey,
			Integer samplestoragelocationkey);

	public List<SampleStorageLocation> findByStatusAndSitekey(int i, Integer nsiteInteger);

	public List<SampleStorageLocation> findBySamplestoragelocationkeyOrSitekeyOrderBySamplestoragelocationkeyDesc(int i,
			Integer nsiteInteger);

	public List<SampleStorageLocation> findBySitekeyAndStatusOrderBySamplestoragelocationkeyDesc(Integer nsiteInteger,
			int i);

	public List<SampleStorageLocation> findBySamplestoragelocationkeyOrSitekeyAndStatusOrderBySamplestoragelocationkeyDesc(
			int i, Integer nsiteInteger, int j);

	public List<SampleStorageLocation> findBySamplestoragelocationkeyOrSitekeyAndStatusOrderBySamplestoragelocationkey(
			int i, Integer nsiteInteger, int j);

	public SampleStorageLocation findBySamplestoragelocationnameAndSitekeyAndStatus(String samplestoragelocationname,
			Integer sitekey, int i);

}
