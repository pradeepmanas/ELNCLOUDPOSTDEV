package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.MaterialProjectHistory;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;

public interface MaterialProjectHistoryRepository  extends JpaRepository<MaterialProjectHistory,Integer>{

	List<MaterialProjectHistory> findByLsproject(LSprojectmaster project);


}
