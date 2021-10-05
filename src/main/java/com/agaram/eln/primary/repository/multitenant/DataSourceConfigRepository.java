package com.agaram.eln.primary.repository.multitenant;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.fetchtenantsource.Datasourcemaster;
import com.agaram.eln.primary.model.multitenant.DataSourceConfig;

public interface DataSourceConfigRepository extends JpaRepository<DataSourceConfig, Long> {
	DataSourceConfig findByName(String name);

	DataSourceConfig findByTenantid(String tenantid);

	DataSourceConfig findByTenantidIgnoreCase(String tenantid);

	DataSourceConfig findByuseremail(String useremail);

	DataSourceConfig findBytenantcontactno(String tenantcontactno);

	DataSourceConfig findByTenantidAndIsenable(String tenantid, boolean enabled);

	DataSourceConfig findByNameAndTenantid(String name, String tenantid);

	DataSourceConfig findByArchivename(String archivename);

	DataSourceConfig findById(Long id);
	
//	DataSourceConfig findBycustomersubscriptionConfig(CustomerSubscription CustomerSubscription);
	
//	@Query("select id from DataSourceConfig id where id.customer_subscription_id =?1")
//	DataSourceConfig findBycustomersubscriptionid(Long customer_subscription_id);

	List<DataSourceConfig> findByInitialize(boolean initiated);
	
	List<Datasourcemaster> findByinitialize(boolean initiated);
	
	List<Datasourcemaster> findBytenantid(String tenantid);
	
	@Transactional
	@Modifying
	@Query("update DataSourceConfig u set u.verifiedemail = ?1 where u.tenantid = ?2")
	void setverifiedemailandtenantpassword(boolean tenantpassword, String tenantid);

	@Transactional
	@Modifying
	@Query("update DataSourceConfig u set u.varificationOTP = ?1 where u.tenantid = ?2")
	void setotp(String varificationOTP, String tenantid);

	@Transactional
	@Modifying
	@Query("update DataSourceConfig u set u.tenantcontactno = ?1,u.tenantaddress=?2 ,u.tenantstate=?3 ,u.tenantcity=?4 ,u.tenantpincode=?5 ,u.tenantcountry=?6  where u.tenantid = ?7")
	int setcontactandaddressandstateandcityandpincodeandcountry(String contact, String address, String state,
			String city, String pincocde, String country, String tenantid);

	List<DataSourceConfig> findAllByOrderByIdDesc();

}
