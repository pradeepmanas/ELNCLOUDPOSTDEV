package com.agaram.eln.primary.model.multitenant;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.general.Response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "Invoice")
public class Invoice {

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Basic(optional = false)
//	    private Long id;
	 @Id
	 private Integer invoice_id;
	 private String type;
	 private String invoice_status;
//	 private Integer subscription_id;
	 private Integer customer_id;
	 
	 @OneToOne(cascade=CascadeType.ALL)
	    @JoinColumn(name = "customer_subscription_id", referencedColumnName = "customer_subscription_id")
	    private CustomerSubscription CustomerSubscription;
	  
	 @Transient
		Response objResponse;
	 
	public Response getObjResponse() {
		return objResponse;
	}

	public void setObjResponse(Response objResponse) {
		this.objResponse = objResponse;
	}

	public CustomerSubscription getCustomerSubscription() {
		return CustomerSubscription;
	}

	public void setCustomerSubscription(CustomerSubscription customerSubscription) {
		CustomerSubscription = customerSubscription;
	}

//	public Long getId() {
//		return id;
//	}

	public String getType() {
		return type;
	}

	public Integer getInvoice_id() {
		return invoice_id;
	}

	public String getInvoice_status() {
		return invoice_status;
	}

//	public Integer getSubscription_id() {
//		return subscription_id;
//	}

	public Integer getCustomer_id() {
		return customer_id;
	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}

	public void setType(String type) {
		this.type = type;
	}

	public void setInvoice_id(Integer invoice_id) {
		this.invoice_id = invoice_id;
	}

	public void setInvoice_status(String invoice_status) {
		this.invoice_status = invoice_status;
	}

//	public void setSubscription_id(Integer subscription_id) {
//		this.subscription_id = subscription_id;
//	}

	public void setCustomer_id(Integer customer_id) {
		this.customer_id = customer_id;
	}


	  
	  
	  
}
