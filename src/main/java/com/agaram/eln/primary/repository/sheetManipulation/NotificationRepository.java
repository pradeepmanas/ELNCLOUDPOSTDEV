package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sheetManipulation.Notification;

public interface NotificationRepository extends JpaRepository<Notification , Integer> {

	List<Notification> findByUsercode(Integer usercode);
	
}
