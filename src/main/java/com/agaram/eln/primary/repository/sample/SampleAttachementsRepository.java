package com.agaram.eln.primary.repository.sample;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.agaram.eln.primary.model.sample.SampleAttachments;

public interface SampleAttachementsRepository extends JpaRepository<SampleAttachments, Integer>{

	List<SampleAttachments> findBySamplecodeOrderByNsampleattachcodeDesc(int samplecode);

}
