package com.agaram.eln.primary.repository.instrumentDetails;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.LsResultlimsOrderrefrence;

public interface LsResultlimsOrderrefrenceRepository extends JpaRepository<LsResultlimsOrderrefrence, Long>{

	public LsResultlimsOrderrefrence findFirst1ByfileidOrderByRefrencecodeDesc(String fileid);

	public LsResultlimsOrderrefrence findFirst1BybatchidOrderByRefrencecodeDesc(String batchid);
	
	public LsResultlimsOrderrefrence findFirst1BybatchcodeOrderByRefrencecodeDesc(Long batchcode);
}
