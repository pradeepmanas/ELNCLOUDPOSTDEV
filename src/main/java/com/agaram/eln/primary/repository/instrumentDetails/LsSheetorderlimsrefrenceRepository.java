package com.agaram.eln.primary.repository.instrumentDetails;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.LsSheetorderlimsrefrence;

public interface LsSheetorderlimsrefrenceRepository extends JpaRepository<LsSheetorderlimsrefrence, Long>{

	public LsSheetorderlimsrefrence findFirst1ByfileidOrderByRefrencecodeDesc(String fileid);

	public LsSheetorderlimsrefrence findFirst1BybatchcodeOrderByRefrencecodeDesc(Long batchcode);
}
