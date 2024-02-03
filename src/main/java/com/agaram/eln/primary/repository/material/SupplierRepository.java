package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer>{
	List<Supplier> findByNstatusAndNsitecodeOrderByNsuppliercode(int i, Integer nsiteInteger);
	Supplier findBySsuppliernameAndNsitecode(String ssuppliername, Integer nsitecode);
	Supplier findByNsuppliercode(int nunitCode);
	List<Object> findByNstatusAndNsitecode(int i, Integer nsiteInteger);
	List<Supplier> findByNsitecodeOrderByNsuppliercode(Integer nsiteInteger);
	Supplier findBySsuppliernameIgnoreCaseAndNsitecode(String ssuppliername, Integer nsitecode);
	List<Supplier> findByNstatusAndNsitecodeOrderByNsuppliercodeDesc(int i, Integer nsiteInteger);
	Supplier findBySsuppliernameIgnoreCaseAndNsitecodeAndNstatus(String ssuppliername, Integer nsitecode, int i);
}