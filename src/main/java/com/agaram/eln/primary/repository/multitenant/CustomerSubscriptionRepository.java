package com.agaram.eln.primary.repository.multitenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.multitenant.CustomerSubscription;
import com.agaram.eln.primary.model.multitenant.DataSourceConfig;

public interface CustomerSubscriptionRepository extends JpaRepository<CustomerSubscription, Long>  {
//	void save(CustomerSubscription customerSubscription);
}
