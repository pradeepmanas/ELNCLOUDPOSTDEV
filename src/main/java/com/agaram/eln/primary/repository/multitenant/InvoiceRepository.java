package com.agaram.eln.primary.repository.multitenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.multitenant.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>  {
	
	Invoice findBycustomersubscriptionid(Long customer_subscription_id);
	
}