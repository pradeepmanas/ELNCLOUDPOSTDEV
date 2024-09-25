package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sheetManipulation.LsfilemapBarcode;

public interface LsfilemapBarcodeRepository extends JpaRepository<LsfilemapBarcode, Integer> {

	List<LsfilemapBarcode> findByFilecode(Integer filecode);

		@Transactional
	void deleteByFilecode(Integer filecode);

}
