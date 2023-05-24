package com.agaram.eln.primary.model.multitenant;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
	@Column(columnDefinition = "numeric(30,0)", name = "invoice_id")
	private Long invoice_id;
	private String type;
	private String invoice_status;
	@Column(columnDefinition = "numeric(30,0)", name = "customer_id")
	private Long customer_id;
	private String invoiceno;
	public String getInvoiceno() {
		return invoiceno;
	}

	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}

	private Long customersubscriptionid;

	public Long getCustomersubscriptionid() {
		return customersubscriptionid;
	}

	public void setCustomersubscriptionid(Long customersubscriptionid) {
		this.customersubscriptionid = customersubscriptionid;
	}

	@OneToOne(cascade = CascadeType.ALL)
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

	public Long getInvoice_id() {
		return invoice_id;
	}

	public String getInvoice_status() {
		return invoice_status;
	}

//	public Integer getSubscription_id() {
//		return subscription_id;
//	}

	public Long getCustomer_id() {
		return customer_id;
	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}

	public void setType(String type) {
		this.type = type;
	}

	public void setInvoice_id(Long invoice_id) {
		this.invoice_id = invoice_id;
	}

	public void setInvoice_status(String invoice_status) {
		this.invoice_status = invoice_status;
	}

//	public void setSubscription_id(Integer subscription_id) {
//		this.subscription_id = subscription_id;
//	}

	public void setCustomer_id(Long customer_id) {
		this.customer_id = customer_id;
	}

}
