package com.agaram.eln.primary.repository.multitenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.multitenant.CustomerSubscription;

public interface CustomerSubscriptionRepository extends JpaRepository<CustomerSubscription, Long>  {
//	void save(CustomerSubscription customerSubscription);
	
	@Query("select log from CustomerSubscription log where log.customer_subscription_id = ?1")
	CustomerSubscription findBycustomersubscriptionid(Long customer_subscription_id);
	
	CustomerSubscription findByid(Long id);
}
