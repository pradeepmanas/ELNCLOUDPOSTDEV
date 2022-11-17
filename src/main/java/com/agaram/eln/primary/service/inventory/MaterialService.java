package com.agaram.eln.primary.service.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.instrumentDetails.LSinstrumentmaster;
import com.agaram.eln.primary.model.inventory.LSinstrument;
import com.agaram.eln.primary.model.inventory.LSinstrumentcategory;
import com.agaram.eln.primary.model.inventory.LSinstrumentsection;
import com.agaram.eln.primary.model.inventory.LSmaterial;
import com.agaram.eln.primary.model.inventory.LSmaterialcategory;
import com.agaram.eln.primary.model.inventory.LSmaterialgrade;
import com.agaram.eln.primary.model.inventory.LSmaterialinventory;
import com.agaram.eln.primary.model.inventory.LSmaterialinventorytransaction;
import com.agaram.eln.primary.model.inventory.LSmaterialtype;
import com.agaram.eln.primary.model.inventory.LSsection;
import com.agaram.eln.primary.model.inventory.LSunit;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.instrumentDetails.LSinstrumentmasterRepository;
import com.agaram.eln.primary.repository.inventory.LSinstrumentRepository;
import com.agaram.eln.primary.repository.inventory.LSinstrumentcategoryRepository;
import com.agaram.eln.primary.repository.inventory.LSinstrumentsectionRepository;
import com.agaram.eln.primary.repository.inventory.LSmaterialRepository;
import com.agaram.eln.primary.repository.inventory.LSmaterialcategoryRepository;
import com.agaram.eln.primary.repository.inventory.LSmaterialgradeRepository;
import com.agaram.eln.primary.repository.inventory.LSmaterialinventoryRepository;
import com.agaram.eln.primary.repository.inventory.LSmaterialinventorytransactionRepository;
import com.agaram.eln.primary.repository.inventory.LSmaterialtypeRepository;
import com.agaram.eln.primary.repository.inventory.LSsectionRepository;
import com.agaram.eln.primary.repository.inventory.LSunitRepository;

@Service
@EnableJpaRepositories(basePackageClasses = LSsectionRepository.class)
public class MaterialService {
	@Autowired
    private LSsectionRepository lSsectionRepository;
	
	/**
	 * For Materials And Repository Repository
	 */
	@Autowired
    private LSmaterialRepository LSmaterialRepository;
	@Autowired
    private LSinstrumentRepository LSinstrumentRepository;
	@Autowired
    private LSinstrumentmasterRepository LSinstrumentmasterRepository;
	@Autowired
	private LSinstrumentcategoryRepository lSinstrumentcategoryRepository;
	
	@Autowired
	private LSinstrumentsectionRepository lSinstrumentsectionRepository;
	@Autowired
	private LSmaterialtypeRepository LSmaterialtypeRepository;
	@Autowired 
	private LSunitRepository LSunitRepository;
	@Autowired 
	private LSmaterialgradeRepository LSmaterialgradeRepository;
	@Autowired 
	private LSmaterialcategoryRepository LSmaterialcategoryRepository;
	@Autowired 
	private LSsectionRepository LSsectionRepository;
	@Autowired 
	private LSmaterialinventoryRepository LSmaterialinventoryRepository;
	@Autowired 
	private LSmaterialinventorytransactionRepository LSmaterialinventorytransactionRepository;
	
	public List<LSsection> getSection() {
        List<LSsection> result = new ArrayList<LSsection>();
        lSsectionRepository.findAll().forEach(result::add);
        return result;
    }

	/**
	 * For Materials And Repository Repository
	 */
	public List<LSmaterial> getlsMaterial(LSuserMaster objuser) {
		List<LSmaterial> result = new ArrayList<LSmaterial>();
		LSmaterialRepository.findAll().forEach(result::add);
		return result;
	}

	public List<LSinstrument> getLsInstrument(LSuserMaster objuser) {
		List<LSinstrument> result = new ArrayList<LSinstrument>();
		LSinstrumentRepository.findAll().forEach(result::add);
		return result;
	}

	public List<LSinstrumentmaster> getLsInstrumentMaster(LSuserMaster objuser) {
		List<LSinstrumentmaster> result = new ArrayList<LSinstrumentmaster>();
		LSinstrumentmasterRepository.findAll().forEach(result::add);
		return result;
	}
	
	public Map<String, Object> getEquipmentDetails(Map<String, Object> objMap)
	{
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		
		if(objMap.containsKey("nFlag") && (Integer)objMap.get("nFlag") == 1 )
		{
			List<LSinstrumentcategory> lstInstrumentCategory = lSinstrumentcategoryRepository.findAll();
			
			if(lstInstrumentCategory != null && lstInstrumentCategory.size() > 0)
			{
				mapOrders.put("InstrumentType", lstInstrumentCategory);
			
				GetInstrumentByCategory(mapOrders, lstInstrumentCategory.get(0).getNinstrumentcatcode());
			}
			else
			{
				mapOrders.put("InstrumentType", new ArrayList<LSinstrumentcategory>());
				mapOrders.put("Instrument", new ArrayList<LSinstrument>());
				mapOrders.put("InstrumentSection", new ArrayList<LSinstrumentsection>());
			}
			
		}
		else if(objMap.containsKey("nFlag") && (Integer)objMap.get("nFlag") == 2 )
		{
			GetInstrumentByCategory(mapOrders, (Integer)objMap.get("ninstrumentcatcode"));
		}
		
		
		return mapOrders;
	}
	
	public void GetInstrumentByCategory(Map<String, Object> mapOrders, int InstrumentCategory)
	{
		List<LSinstrument> lstLsinstrument = LSinstrumentRepository.findByinstrumentcatcode(InstrumentCategory);
		
		if(lstLsinstrument != null && lstLsinstrument.size() > 0)
		{
			mapOrders.put("Instrument", lstLsinstrument);
			List<Integer> lstSection = new ArrayList<Integer>();
			lstSection = lSinstrumentsectionRepository.getNsectioncodeByNinstrumentcode(lstLsinstrument.get(0).getInstrumentcode());
			
			if(lstSection != null && lstSection.size()>0)
			{
				mapOrders.put("InstrumentSection", lSsectionRepository.findBynsectioncodeIn(lstSection));
			}
			else
			{
				mapOrders.put("InstrumentSection", new ArrayList<LSinstrumentsection>());
			}
		}
		else
		{
			mapOrders.put("Instrument", new ArrayList<LSinstrument>());
			mapOrders.put("InstrumentSection", new ArrayList<LSinstrumentsection>());
		}
	}

	public Map<String, Object> getMaterialDetails(Map<String, Object> objMap) throws Exception {
		
		Map<String,Object> map=new HashMap<>();
		
		List<LSunit> lstUnit=LSunitRepository.findAll();
		map.put("Unit", lstUnit);
		
		List<LSsection> lstmaterialsec=getMaterialSection();
		map.put("MaterialSection", lstmaterialsec);
		
		List<LSmaterialgrade> lstmaterialgrd=LSmaterialgradeRepository.findAll();
		map.put("MaterialGrade", lstmaterialgrd);
		
		if(objMap.containsKey("nFlag") && (Integer)objMap.get("nFlag") == 1 ) {
			
			List<LSmaterialtype> lstmaterialtype=LSmaterialtypeRepository.findAll();
			map.put("MaterialType", lstmaterialtype);
			
//			List<LSunit> lstUnit=LSunitRepository.findAll();
//			map.put("Unit", lstUnit);
//			
//			List<LSsection> lstmaterialsec=getMaterialSection();
//			map.put("MaterialSection", lstmaterialsec);
//			
//			List<LSmaterialgrade> lstmaterialgrd=LSmaterialgradeRepository.findAll();
//			map.put("MaterialGrade", lstmaterialgrd);
			
			
			map.put("MaterialSection",lSsectionRepository.findAll());
			
			if(lstmaterialtype.size()>0) {
				List<LSmaterialcategory> lstmaterialcat=getMaterialCat((int)lstmaterialtype.get(0).getNmaterialtypecode());
				map.put("MaterialCategory", lstmaterialcat);
				
				if(lstmaterialcat.size()>0) {
					List<LSmaterial> lstmaterial=LSmaterialRepository.findByNmaterialcatcode((Integer) lstmaterialcat.get(0).getNmaterialcatcode());
					map.put("Material", lstmaterial);
					
					if(lstmaterial.size()>0) {
						
						List<LSmaterialinventory> lstmaterialinvty=LSmaterialinventoryRepository.findByNmaterialcodeAndNtransactionstatus((int)lstmaterial.get(0).getNmaterialcode(),28);
						map.put("MaterialInventory", lstmaterialinvty);
					}
				}
			}
		}else if(objMap.containsKey("nFlag") && (int)objMap.get("nFlag")==2) {
			int nmaterialtypecode=(int) objMap.get("nmaterialtypecode");
			List<LSmaterialcategory> lstmaterialcat=getMaterialCat(nmaterialtypecode);
			map.put("MaterialCategory", lstmaterialcat);
			
			if(lstmaterialcat.size()>0) {
				List<LSmaterial> lstmaterial=LSmaterialRepository.findByNmaterialcatcode((Integer) lstmaterialcat.get(0).getNmaterialcatcode());
				map.put("Material", lstmaterial);
				
				if(lstmaterial.size()>0) {
//					List<LSsection> lstmaterialsec=getMaterialSection();
//					map.put("MaterialSection", lstmaterialsec);
					
					List<LSmaterialinventory> lstmaterialinvty=LSmaterialinventoryRepository.findByNmaterialcodeAndNtransactionstatus((int)lstmaterial.get(0).getNmaterialcode(),28);
					map.put("MaterialInventory", lstmaterialinvty);
				}
			}
		}else if(objMap.containsKey("nFlag") && (int)objMap.get("nFlag")==3) {
			int nmaterialcatcode=(int) objMap.get("nmaterialcatcode");
			List<LSmaterial> lstmaterial=LSmaterialRepository.findByNmaterialcatcode(nmaterialcatcode);
			map.put("Material", lstmaterial);
			
			if(lstmaterial.size()>0) {
//				List<LSsection> lstmaterialsec=getMaterialSection();
//				map.put("MaterialSection", lstmaterialsec);
				
				List<LSmaterialinventory> lstmaterialinvty=LSmaterialinventoryRepository.findByNmaterialcodeAndNtransactionstatus((int)lstmaterial.get(0).getNmaterialcode(),28);
				map.put("MaterialInventory", lstmaterialinvty);
			}
			
		}else if(objMap.containsKey("nFlag") && (int)objMap.get("nFlag")==4) {
			int nmaterialcode=(int) objMap.get("nmaterialcode");
//			List<LSsection> lstmaterialsec=getMaterialSection();
//			map.put("MaterialSection", lstmaterialsec);
			
			List<LSmaterialinventory> lstmaterialinvty=LSmaterialinventoryRepository.findByNmaterialcodeAndNtransactionstatus(nmaterialcode,28);
			map.put("MaterialInventory", lstmaterialinvty);
		}else if(objMap.containsKey("nFlag") && (int)objMap.get("nFlag")==5) {
			int nmaterialinventorycode=(int) objMap.get("nmaterialinventorycode");
			
//			List<LSmaterialinventory> lstmaterialinvty=getMaterialInvenTrans(nmaterialinventorycode);
//			List<LSmaterialinventory> lstmaterialinvty=getMaterialInvenTrans(nmaterialinventorycode);
			List<LSmaterialinventory> lstmaterialinvty = LSmaterialinventoryRepository.findByNmaterialinventorycode(nmaterialinventorycode);
			map.put("MaterialInventory", lstmaterialinvty);
		
		}
		
		return map;
	}
	
	public List<LSmaterialcategory> getMaterialCat(int getnmaterialtypecode) throws Exception{
		
		
		List<LSmaterialcategory> lstMatCat =  LSmaterialcategoryRepository.findByNmaterialtypecode(getnmaterialtypecode);
		/*List<LSmaterial> lstMaterial=new ArrayList<LSmaterial>();
		if(lstMatCat.size()>0) {
			int i=0;
			
			while(i < lstMatCat.size()) {
				
				LSmaterial matcat=new LSmaterial();
				
				matcat= LSmaterialRepository.findByNmaterialcatcodeAndNstatus(lstMatCat.get(i).getNmaterialcatcode(),1);
				
				lstMaterial.add(matcat);
				
				i++;
			}	
		}*/
		return lstMatCat;	
	}
	
	public List<LSsection>  getMaterialSection() throws Exception{
		
//		List<LSmaterialsection> lsMatSec=LSmaterialsectionRepository.findAll();
		
		List<LSsection> lstSec = LSsectionRepository.findAll();
		
		return lstSec;	
	}

	public List<LSmaterialinventory> getMaterialInvenTrans(int nmaterialinventorycode) throws Exception{
		
		List<LSmaterialinventorytransaction> lstTrans=(List<LSmaterialinventorytransaction>) LSmaterialinventorytransactionRepository.findBynmaterialinventorycode(nmaterialinventorycode);
		List<LSmaterialinventory> lstMaterialInv=new ArrayList<LSmaterialinventory>();
		if(lstTrans.size()>0) {
			int i=0;
			
			while(i < lstTrans.size()) {
				
				LSmaterialinventory matInv=LSmaterialinventoryRepository.findBynmaterialinventorycode(lstTrans.get(i).getNmaterialinventorycode());
				lstMaterialInv.add(matInv);
				
				i++;
			}	
		}
		
		return lstMaterialInv;
	}
}
