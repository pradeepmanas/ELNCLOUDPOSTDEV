package com.agaram.eln.primary.repository.sample;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sample.SampleProjectHistory;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;


public interface SampleProjectHistoryRepository extends JpaRepository<SampleProjectHistory,Integer>{

	List<SampleProjectHistory> findByLsproject(LSprojectmaster project);

}


