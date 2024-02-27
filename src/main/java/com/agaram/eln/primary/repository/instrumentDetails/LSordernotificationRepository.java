package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.LSOrdernotification;

public interface LSordernotificationRepository extends JpaRepository<LSOrdernotification, Integer>{

	List<LSOrdernotification> findByUsercodeAndCautiondateBetween(Integer usercode, Date fromDate, Date toDate);

	List<LSOrdernotification> findByUsercodeAndDuedateBetween(Integer usercode, Date fromDate, Date toDate);


}
