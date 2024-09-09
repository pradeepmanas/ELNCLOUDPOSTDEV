package com.agaram.eln.primary.repository.barcode;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.barcode.BarcodeMaster;
import com.agaram.eln.primary.model.material.Unit;

public interface BarcodeMasterRepository  extends JpaRepository<BarcodeMaster, Integer>{
	List<BarcodeMaster> findByOrderByBarcodenoDesc();
	public BarcodeMaster findByBarcodeno(Integer barcodeno);
	public BarcodeMaster findByBarcodenameIgnoreCaseAndStatus(String Barcodename, int i);
}
