package com.agaram.eln.primary.service.smartdevice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.Smartdevice;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;

@Service
public class SmartdeviceService {
	@Autowired
	private LSSiteMasterRepository lSSiteMasterRepository;
	public List<LSSiteMaster> Getdata(Smartdevice smartdevice)
	{
		return lSSiteMasterRepository.findByOrderBySitecodeDesc();
	}
}
