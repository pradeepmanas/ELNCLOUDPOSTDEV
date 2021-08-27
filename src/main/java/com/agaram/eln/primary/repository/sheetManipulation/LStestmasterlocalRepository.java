package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.fetchmodel.getmasters.Testmaster;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

public interface LStestmasterlocalRepository extends JpaRepository<LStestmasterlocal, Integer>{
	public LStestmasterlocal findByTestname(String Testname);
	public List<LStestmasterlocal> findBystatus(Integer status);
	public LStestmasterlocal findByTestnameAndStatus(String Testname, Integer status);
	public LStestmasterlocal findByTestnameAndStatusAndTestcodeNot(String Testname, Integer status, Integer testcode);
	public LStestmasterlocal findBytestcode(Integer testcode);
	
	public List<Testmaster> findBystatusAndLssitemaster(Integer status,LSSiteMaster lssitemaster);
	public LStestmasterlocal findByTestnameAndStatusAndLssitemaster(String Testname, Integer status,LSSiteMaster lssitemaster);
	public LStestmasterlocal findByTestnameAndStatusAndTestcodeNotAndLssitemaster(String Testname, Integer status, Integer testcode,LSSiteMaster lssitemaster);
	public Object findByTestnameIgnoreCaseAndStatusAndTestcodeNotAndLssitemaster(String testname, Integer status,
			Integer testcode, LSSiteMaster lssitemaster);
	public Object findByTestnameIgnoreCaseAndStatusAndLssitemaster(String testname, Integer status, LSSiteMaster lssitemaster);
}