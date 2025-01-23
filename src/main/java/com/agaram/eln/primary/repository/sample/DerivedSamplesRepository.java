package com.agaram.eln.primary.repository.sample;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sample.DerivedSamples;
import com.agaram.eln.primary.model.sample.Sample;

public interface DerivedSamplesRepository extends JpaRepository<DerivedSamples, Integer>{
	List<DerivedSamples> findByParentsampleInOrderByDerivedsamplecode(List<Sample> ssample);
}
