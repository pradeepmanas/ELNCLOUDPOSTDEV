package com.agaram.eln.primary.model.multitenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.general.Response;

@Entity
@Table(name = "CustomerSubscription")
public class CustomerSubscription {
	@Id
	@Column(columnDefinition = "numeric(30,0)", name = "customer_subscription_id")
	private Long customer_subscription_id;
	private String username;
	private String organization_name;
	private String currency;
	private String academic;
	private String plan_type;
	private String monthly_Annual;
	private Integer number_of_users;
	private Integer subscription_amount;
	private String gst_treatment;
	private String gst_number;
	private String online_offline_payment;
	private boolean i_agree_to_the_terms_of_agreement;
	private String subscription_number;
	private String customer_register_id;
	private String zoho_subscription_id;
	private String creator_subscription_id;
	private String hosted_page_id;
	private String type;
	private Integer totalamount;
	private Long id;

//	@OneToOne
//	@JoinColumn(name="customer_subscription_id")
//	private Invoice invoice;
//	
//	public Invoice getInvoice() {
//		return invoice;
//	}
//
//	public void setInvoice(Invoice invoice) {
//		this.invoice = invoice;
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(Integer totalamount) {
		this.totalamount = totalamount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Transient
	Response objResponse;

	public Response getObjResponse() {
		return objResponse;
	}

	public void setObjResponse(Response objResponse) {
		this.objResponse = objResponse;
	}

	public Long getCustomer_subscription_id() {
		return customer_subscription_id;
	}

	public void setCustomer_subscription_id(Long customer_subscription_id) {
		this.customer_subscription_id = customer_subscription_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOrganization_name() {
		return organization_name;
	}

	public void setOrganization_name(String organization_name) {
		this.organization_name = organization_name;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAcademic() {
		return academic;
	}

	public void setAcademic(String academic) {
		this.academic = academic;
	}

	public String getPlan_type() {
		return plan_type;
	}

	public void setPlan_type(String plan_type) {
		this.plan_type = plan_type;
	}

	public String getMonthly_Annual() {
		return monthly_Annual;
	}

	public void setMonthly_Annual(String monthly_Annual) {
		this.monthly_Annual = monthly_Annual;
	}

	public Integer getNumber_of_users() {
		return number_of_users;
	}

	public void setNumber_of_users(Integer number_of_users) {
		this.number_of_users = number_of_users;
	}

	public Integer getSubscription_amount() {
		return subscription_amount;
	}

	public void setSubscription_amount(Integer subscription_amount) {
		this.subscription_amount = subscription_amount;
	}

	public String getGst_treatment() {
		return gst_treatment;
	}

	public void setGst_treatment(String gst_treatment) {
		this.gst_treatment = gst_treatment;
	}

	public String getGst_number() {
		return gst_number;
	}

	public void setGst_number(String gst_number) {
		this.gst_number = gst_number;
	}

	public String getOnline_offline_payment() {
		return online_offline_payment;
	}

	public void setOnline_offline_payment(String online_offline_payment) {
		this.online_offline_payment = online_offline_payment;
	}

	public boolean isI_agree_to_the_terms_of_agreement() {
		return i_agree_to_the_terms_of_agreement;
	}

	public void setI_agree_to_the_terms_of_agreement(boolean i_agree_to_the_terms_of_agreement) {
		this.i_agree_to_the_terms_of_agreement = i_agree_to_the_terms_of_agreement;
	}

	public String getSubscription_number() {
		return subscription_number;
	}

	public void setSubscription_number(String subscription_number) {
		this.subscription_number = subscription_number;
	}

	public String getCustomer_register_id() {
		return customer_register_id;
	}

	public void setCustomer_register_id(String customer_register_id) {
		this.customer_register_id = customer_register_id;
	}

	public String getZoho_subscription_id() {
		return zoho_subscription_id;
	}

	public void setZoho_subscription_id(String zoho_subscription_id) {
		this.zoho_subscription_id = zoho_subscription_id;
	}

	public String getCreator_subscription_id() {
		return creator_subscription_id;
	}

	public void setCreator_subscription_id(String creator_subscription_id) {
		this.creator_subscription_id = creator_subscription_id;
	}

	public String getHosted_page_id() {
		return hosted_page_id;
	}

	public void setHosted_page_id(String hosted_page_id) {
		this.hosted_page_id = hosted_page_id;
	}

}
