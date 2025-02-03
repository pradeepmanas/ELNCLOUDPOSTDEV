package com.agaram.eln.primary.repository.sample;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.agaram.eln.primary.model.sample.SampleLinks;

public interface SampleLinkRepository extends JpaRepository<SampleLinks, Integer>{

	List<SampleLinks> findByNsamplecodeOrderByNsamplelinkcodeDesc(Integer nsamplecode);

}
