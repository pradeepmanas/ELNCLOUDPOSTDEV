package com.agaram.eln.primary.repository.barcode;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.barcode.BarcodeMaster;
import com.agaram.eln.primary.model.general.ScreenMaster;
import com.agaram.eln.primary.model.material.Unit;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

public interface BarcodeMasterRepository  extends JpaRepository<BarcodeMaster, Integer>{
	List<BarcodeMaster> findByOrderByBarcodenoDesc();
	public BarcodeMaster findByBarcodeno(Integer barcodeno);
	public BarcodeMaster findByBarcodenameIgnoreCaseAndStatus(String Barcodename, int i);
	List<BarcodeMaster> findByLssitemasterAndScreenOrderByBarcodenoDesc(LSSiteMaster lssitemaster, ScreenMaster screen);
	List<BarcodeMaster> findByBarcodenoIn(List<Integer> barcodes);
	List<BarcodeMaster> findByBarcodenameAndLssitemaster(String barcodename, LSSiteMaster lssitemaster);
	List<BarcodeMaster> findByBarcodenameAndLssitemasterAndBarcodenoNot(String barcodename, LSSiteMaster lssitemaster,
			Integer barcodeno);
	List<BarcodeMaster> findByLssitemasterOrderByBarcodenoDesc(LSSiteMaster lssitemaster);
}
