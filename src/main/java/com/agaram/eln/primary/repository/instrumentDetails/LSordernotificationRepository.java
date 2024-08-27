package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.LSOrdernotification;

public interface LSordernotificationRepository extends JpaRepository<LSOrdernotification, Integer>{

	List<LSOrdernotification> findByUsercodeAndCautiondateBetween(Integer usercode, Date fromDate, Date toDate);

	List<LSOrdernotification> findByUsercodeAndDuedateBetween(Integer usercode, Date fromDate, Date toDate);

	List<LSOrdernotification> findByIsduedateexhaustedAndIscompleted(boolean b, boolean c);

	List<LSOrdernotification> findByIsduedateexhaustedAndIscompletedOrIsduedateexhaustedAndIscompleted(boolean b,
			boolean c, boolean d, Object object);
    
	List<LSOrdernotification> findByDuestatusAndDuedateBetween(int i, Date fromDate, Date toDate);

	List<LSOrdernotification> findByOverduestatusAndDuedateBetween(int i, Date overduefromDate, Date toDate);

	List<LSOrdernotification> findByCautiondateBetweenAndCautionstatus(Date fromDate, Date toDate, int i);

	LSOrdernotification findByBatchcode(Long batchcode);

	LSOrdernotification findByBatchcodeAndScreen(Long batchcode, String screen);



}
