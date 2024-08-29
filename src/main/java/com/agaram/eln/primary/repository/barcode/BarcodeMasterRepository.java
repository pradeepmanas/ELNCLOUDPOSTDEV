package com.agaram.eln.primary.repository.barcode;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.barcode.BarcodeMaster;

public interface BarcodeMasterRepository  extends JpaRepository<BarcodeMaster, Integer>{
	List<BarcodeMaster> findByOrderByBarcodenoDesc();
}
