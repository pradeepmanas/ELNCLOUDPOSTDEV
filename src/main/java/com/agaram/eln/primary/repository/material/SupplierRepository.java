package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer>{
	List<Supplier> findByNstatusAndNsitecodeOrderByNsuppliercode(int i, Integer nsiteInteger);
	Supplier findBySsuppliernameAndNsitecode(String ssuppliername, Integer nsitecode);
	Supplier findByNsuppliercode(int nunitCode);
	List<Object> findByNstatusAndNsitecode(int i, Integer nsiteInteger);
}