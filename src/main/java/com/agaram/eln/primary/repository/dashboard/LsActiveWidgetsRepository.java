package com.agaram.eln.primary.repository.dashboard;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.dashboard.LsActiveWidgets;

public interface LsActiveWidgetsRepository extends JpaRepository<LsActiveWidgets, Integer>{

	List<LsActiveWidgets> findFirst30ByUserIdOrderByActivewidgetscodeDesc(Integer usercode);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM lsactivewidgets WHERE activewidgetscode in (SELECT activewidgetscode FROM lsactivewidgets order by activewidgetscode desc OFFSET ?1 ) and userId=?2", nativeQuery = true)
    void deleteCustomRows(int limit, Integer integer);

}
