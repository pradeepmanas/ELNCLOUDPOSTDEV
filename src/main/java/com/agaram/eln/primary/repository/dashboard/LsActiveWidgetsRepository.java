package com.agaram.eln.primary.repository.dashboard;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agaram.eln.primary.model.dashboard.LsActiveWidgets;

public interface LsActiveWidgetsRepository extends JpaRepository<LsActiveWidgets, Integer>{

	List<LsActiveWidgets> findFirst50ByUserIdOrderByActivewidgetscodeDesc(Integer usercode);
	
    @Transactional
    @Modifying
    @Query(value = "WITH CTE AS ("
            + "    SELECT activewidgetscode"
            + "    FROM lsactivewidgets"
            + "    WHERE userId = :userId"
            + "    ORDER BY activewidgetscode DESC"
            + "    OFFSET :offset ROWS"
            + ") "
            + "DELETE FROM lsactivewidgets "
            + "WHERE activewidgetscode IN (SELECT activewidgetscode FROM CTE) AND userId = :userId", 
            nativeQuery = true)
    void deleteCustomRows(@Param("offset") int offset, @Param("userId") Integer userId);

    
    @Transactional
    @Modifying
    @Query(value = "update lsactivewidgets set cancelstatus = 1 where activewidgetsdetailscode = ?1", nativeQuery = true)
	void updateRetirestatus(Number protocolordercode);
    


}
