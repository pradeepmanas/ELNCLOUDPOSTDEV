package com.agaram.eln.primary.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.notification.Email;

public interface EmailRepository extends JpaRepository<Email, Integer> {
//	 @Transactional
//	 @Modifying
//	 @Query("update Email u set u.password = ?1 where u.tenantid = ?2")
//	 void setpasswordBytenantid(String password, String string);
//	  Email findBytenantid(String tenantid);
//	@Modifying
//	 @Query("update email u set u.password = ?1 where u.tenantid = ?2")
//	 void setpasswordBytenantid(String password, String string);
}
