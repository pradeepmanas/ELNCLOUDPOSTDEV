package com.agaram.eln.primary.repository.dashboard;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.dashboard.LsActiveWidgets;

public interface LsActiveWidgetsRepository extends JpaRepository<LsActiveWidgets, Integer>{

	List<LsActiveWidgets> findFirst30ByUserIdOrderByActivewidgetscodeDesc(Integer usercode);

}
