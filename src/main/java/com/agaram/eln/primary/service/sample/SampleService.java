package com.agaram.eln.primary.service.sample;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.fetchmodel.inventory.Sampleget;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.material.Period;
import com.agaram.eln.primary.model.material.Unit;
import com.agaram.eln.primary.model.sample.DerivedSamples;
import com.agaram.eln.primary.model.sample.ElnresultUsedSample;
import com.agaram.eln.primary.model.sample.Sample;
import com.agaram.eln.primary.model.sample.SampleAttachments;
import com.agaram.eln.primary.model.sample.SampleCategory;
import com.agaram.eln.primary.model.sample.SampleLinks;
import com.agaram.eln.primary.model.sample.SampleProjectHistory;
import com.agaram.eln.primary.model.sample.SampleProjectMap;
import com.agaram.eln.primary.model.sample.SampleStorageMapping;
import com.agaram.eln.primary.model.sample.SampleType;
import com.agaram.eln.primary.model.samplestoragelocation.SelectedInventoryMapped;
import com.agaram.eln.primary.model.sequence.SequenceTable;
import com.agaram.eln.primary.model.sequence.SequenceTableProjectLevel;
import com.agaram.eln.primary.model.sequence.SequenceTableSite;
import com.agaram.eln.primary.model.sequence.SequenceTableTaskLevel;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.material.PeriodRepository;
import com.agaram.eln.primary.repository.material.UnitRepository;
import com.agaram.eln.primary.repository.sample.DerivedSamplesRepository;
import com.agaram.eln.primary.repository.sample.ElnresultUsedSampleRepository;
import com.agaram.eln.primary.repository.sample.SampleAttachementsRepository;
import com.agaram.eln.primary.repository.sample.SampleCategoryRepository;
import com.agaram.eln.primary.repository.sample.SampleLinkRepository;
import com.agaram.eln.primary.repository.sample.SampleProjectHistoryRepository;
import com.agaram.eln.primary.repository.sample.SampleProjectMapRepository;
import com.agaram.eln.primary.repository.sample.SampleRepository;
import com.agaram.eln.primary.repository.sample.SampleStorageMappingRepository;
import com.agaram.eln.primary.repository.sample.SampleTypeRepository;
import com.agaram.eln.primary.repository.sequence.SequenceTableProjectLevelRepository;
import com.agaram.eln.primary.repository.sequence.SequenceTableRepository;
import com.agaram.eln.primary.repository.sequence.SequenceTableSiteRepository;
import com.agaram.eln.primary.repository.sequence.SequenceTableTaskLevelRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.fileManipulation.FileManipulationservice;
import com.agaram.eln.primary.service.material.MaterialCategoryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class SampleService<ParentSample>{
	
	@Autowired
	private SampleRepository samplerepository;
	@Autowired
	SampleTypeRepository sampleTypeRepository;
	@Autowired
	SampleCategoryRepository sampleCategoryRepository;
	@Autowired
	PeriodRepository periodRepository;
	@Autowired
	private SampleAttachementsRepository sampleAttachementsRepository;
	@Autowired
	private FileManipulationservice fileManipulationservice;
	@Autowired
	private LSuserMasterRepository lsuserMasterRepository;
	@Autowired
	private CloudFileManipulationservice cloudFileManipulationservice;	
	@Autowired
	private SampleLinkRepository sampleLinkRepository;
	@Autowired
	private SequenceTableProjectLevelRepository sequencetableprojectlevelrepository;
	@Autowired
	private SequenceTableTaskLevelRepository sequencetabletasklevelrepository;
	@Autowired
	private DerivedSamplesRepository derivedsamplesrepository;
	@Autowired
	private SequenceTableRepository sequencetableRepository;
	@Autowired
	private SequenceTableSiteRepository sequencetablesiteRepository;
	@Autowired
	private SampleStorageMappingRepository samplestoragemappingrepository;
	@Autowired
	private SampleProjectHistoryRepository sampleprojecthistoryrepository;
	
	@Autowired
	ElnresultUsedSampleRepository ElnresultUsedSampleRepository;
	
	@Autowired
	UnitRepository unitRepository;
	
	@Autowired
	MaterialCategoryService MaterialCategoryService;
	
	@Autowired
	SampleStorageMappingRepository sampleStorageMappingRepository;
	
	@Autowired
	SampleProjectMapRepository sampleprojectmaprepository;

	public ResponseEntity<Object> getSampleonCategory(SampleCategory objsamplecat){			
			List<Sample> lstsample = samplerepository.findBySamplecategoryAndNsitecodeOrderBySamplecodeDesc(objsamplecat,objsamplecat.getNsitecode());
			return new ResponseEntity<>(lstsample, HttpStatus.OK);
	}
public ResponseEntity<Object> getSampleonCategoryFillter(@RequestBody Map<String, Object> inputMap){
		
		final ObjectMapper objmapper = new ObjectMapper();
		Boolean isallproject = (Boolean) inputMap.get("isallproject");
		Boolean isgeneralproject = (Boolean) inputMap.get("isgeneralproject");
		List<Integer> objlstProject = objmapper.convertValue(inputMap.get("projects"),
				new TypeReference<List<Integer>>() {
				});
		SampleCategory objsamplecat = objmapper.convertValue(inputMap.get("category"), SampleCategory.class);
		Date fromdate = objmapper.convertValue(inputMap.get("fromdate"), Date.class);
		Date todate = objmapper.convertValue(inputMap.get("todate"), Date.class);

		List<Sample> lstsample = new ArrayList<Sample>();
		if(isallproject)
		{
			lstsample = samplerepository.findBySamplecategoryAndNsitecodeAndCreateddateBetweenOrderBySamplecodeDesc(
				objsamplecat,objsamplecat.getNsitecode(),fromdate,todate);
		}
		else if(isgeneralproject)
		{
			lstsample = samplerepository.getSampleOnGeneralProjects(
					objsamplecat.getNsamplecatcode(),objsamplecat.getNsitecode(),fromdate,todate);
		}
		else {
			lstsample = samplerepository.getSampleOnProjects(
					objsamplecat.getNsamplecatcode(),objsamplecat.getNsitecode(),fromdate,todate,objlstProject);
		}
		
		return new ResponseEntity<>(lstsample, HttpStatus.OK);
	}
	
	
	public ResponseEntity<Object> getSampleonSite(Sample objsample){	
		List<Sample> lstsample = samplerepository.findByNsitecodeOrderBySamplecodeDesc(objsample.getNsitecode());
		return new ResponseEntity<>(lstsample, HttpStatus.OK);
	}
	
	public void GetSampleSequence(Sample objsampl,SequenceTable seqsampl,SequenceTableProjectLevel objprojectseq,SequenceTableTaskLevel objtaskseq) throws ParseException{
        
		SequenceTable sqa = seqsampl;
		
		if(sqa != null)
		{
			objsampl.setApplicationsequence(sqa.getApplicationsequence()+1);
			
			if(objsampl !=null && objsampl.getNsitecode() != null&&
					objsampl.getNsitecode()!=null && 
							objsampl.getNsitecode()!=null)
			{
				SequenceTableSite sqsite = sqa.getSequencetablesite().stream()
				        .filter(sq -> sq.getSitecode().equals(objsampl.getNsitecode())
				        && sq.getSequencecode().equals(sqa.getSequencecode())).findFirst().orElse(null);
				if(sqsite != null)
				{
					objsampl.setSitesequence(sqsite.getSitesequence()+1);
				}
			}
			Date currentdate = commonfunction.getCurrentUtcTime();
			String sequence = objsampl.getSequenceid();
			String sequencetext = sequence;
			if(sequence.contains("{s&") && sequence.contains("$s}"))
			{
				sequencetext = sequence.substring(sequence.indexOf("{s&")+3, sequence.indexOf("$s}"));
				String replacedseq ="";
				if(sqa.getSequenceview().equals(2) && objsampl.getApplicationsequence()!=null && !sequencetext.equals(""))
				{
					replacedseq = String.format("%0"+sequencetext.length()+"d", objsampl.getApplicationsequence());
				}
				else if(sqa.getSequenceview().equals(3) && objsampl.getSitesequence() != null && !sequencetext.equals(""))
				{
					replacedseq = String.format("%0"+sequencetext.length()+"d", objsampl.getSitesequence());
					
				}
				else if(!sequencetext.equals("") && objsampl.getApplicationsequence()!=null)
				{
					replacedseq = String.format("%0"+sequencetext.length()+"d", objsampl.getApplicationsequence());
				}
				
				if(!sequencetext.equals("") && !replacedseq.equals(""))
				{
					sequencetext = sequence.substring(0, sequence.indexOf("{s&"))+replacedseq+sequence.substring(sequence.indexOf("$s}")+3, sequence.length());
				}
			}
			
			if(sequence.contains("{m&") && sequence.contains("$m}"))
			{
				SimpleDateFormat month = new SimpleDateFormat("MM");
		        String currentMonth = month.format(currentdate);
		        String namedmonth = sequencetext.substring(sequencetext.indexOf("{m&")+3, sequencetext.indexOf("$m}"));
		        
		        if(!namedmonth.equals(""))
		        {
					sequencetext = sequencetext.substring(0, sequencetext.indexOf("{m&"))+currentMonth+sequencetext.substring(sequencetext.indexOf("$m}")+3, sequencetext.length());
		        }
			}
			
			if(sequence.contains("{mm&") && sequence.contains("$mm}"))
			{
				SimpleDateFormat month = new SimpleDateFormat("MMM");
		        String currentMonth = month.format(currentdate);
		        String namedmonth = sequencetext.substring(sequencetext.indexOf("{mm&")+4, sequencetext.indexOf("$mm}"));
		        
		        if(!namedmonth.equals(""))
		        {
					sequencetext = sequencetext.substring(0, sequencetext.indexOf("{mm&"))+currentMonth+sequencetext.substring(sequencetext.indexOf("$mm}")+4, sequencetext.length());
		        }
			}
			
			if(sequence.contains("{dd&") && sequence.contains("$dd}"))
			{
				SimpleDateFormat day = new SimpleDateFormat("dd");
		        String currentMonth = day.format(currentdate);
		        String namedday = sequencetext.substring(sequencetext.indexOf("{dd&")+4, sequencetext.indexOf("$dd}"));
		        
		        if(!namedday.equals(""))
		        {
					sequencetext = sequencetext.substring(0, sequencetext.indexOf("{dd&"))+currentMonth+sequencetext.substring(sequencetext.indexOf("$dd}")+4, sequencetext.length());
		        }
			}
			
			if(sequence.contains("{y&") && sequence.contains("$y}"))
			{
				SimpleDateFormat year = new SimpleDateFormat("yy");
		        String currentMonth = year.format(currentdate);
		        String namedyear = sequencetext.substring(sequencetext.indexOf("{y&")+3, sequencetext.indexOf("$y}"));
		        
		        if(!namedyear.equals(""))
		        {
					sequencetext = sequencetext.substring(0, sequencetext.indexOf("{y&"))+currentMonth+sequencetext.substring(sequencetext.indexOf("$y}")+3, sequencetext.length());
		        }
			}
			
			if(sequence.contains("{yy&") && sequence.contains("$yy}"))
			{
				SimpleDateFormat year = new SimpleDateFormat("yyyy");
		        String currentMonth = year.format(currentdate);
		        String namedyear = sequencetext.substring(sequencetext.indexOf("{yy&")+4, sequencetext.indexOf("$yy}"));
		        
		        if(!namedyear.equals(""))
		        {
					sequencetext = sequencetext.substring(0, sequencetext.indexOf("{yy&"))+currentMonth+sequencetext.substring(sequencetext.indexOf("$yy}")+4, sequencetext.length());
		        }
			}
			
			objsampl.setSequenceid(sequencetext);
		}
		
	}
	
	
	public SequenceTable validateandupdatesamplesequencenumber(Sample obj, SequenceTableProjectLevel objprojectseq, SequenceTableTaskLevel objtaskseq) throws ParseException
	{
		SequenceTable seqorder= new SequenceTable();
		int sequence =5;
		seqorder = sequencetableRepository.findOne(sequence);
		if(seqorder!=null && seqorder.getApplicationsequence()==-1)
		{
			long appcount = samplerepository.count();
			sequencetableRepository.setinitialapplicationsequence(appcount,sequence);
			seqorder.setApplicationsequence(appcount);
		}
		Date currentdate = commonfunction.getCurrentUtcTime();
		SimpleDateFormat day = new SimpleDateFormat("dd");
		SimpleDateFormat month = new SimpleDateFormat("MM");
		SimpleDateFormat year = new SimpleDateFormat("yyyy");
		if(seqorder.getSequenceday() == 0)
		{
			seqorder.setSequenceday(Integer.parseInt(day.format(currentdate)));
		}
		if(seqorder.getSequencemonth() == 0)
		{
			seqorder.setSequencemonth(Integer.parseInt(month.format(currentdate)));
		}
		if(seqorder.getSequenceyear() == 0)
		{
			seqorder.setSequenceyear(Integer.parseInt(year.format(currentdate)));
		}
		if(sequencetablesiteRepository.findBySequencecodeAndSitecode(sequence,obj.getNsitecode()) == null)
		{
			SequenceTableSite objsiteseq= new SequenceTableSite();
			objsiteseq.setSequencecode(sequence);
			objsiteseq.setSitesequence((long)0);
			objsiteseq.setSitecode(obj.getNsitecode());
			sequencetablesiteRepository.save(objsiteseq);
			if(seqorder.getSequencetablesite() !=null)
			{
				seqorder.getSequencetablesite().add(objsiteseq);
			}
			else
			{
				List<SequenceTableSite> lstseq= new ArrayList<SequenceTableSite>();
				lstseq.add(objsiteseq);
				seqorder.setSequencetablesite(lstseq);
			}
		}
		return seqorder;
	}
	
	public void updatesequence(Integer sequenceno, Sample objsample ) {
		 
		//long sitecodeInt = objsample.getNsitecode();
		
		if (objsample.getApplicationsequence() != null) {
			sequencetableRepository.setinitialapplicationsequence(objsample.getApplicationsequence(), sequenceno);
		}
 
		if (objsample.getNsitecode() != null) {
			sequencetablesiteRepository.setinitialsitesequence(objsample.getSitesequence(), sequenceno,
					objsample.getNsitecode());
		}
	}
	
	public ResponseEntity<Object> createSample(Sample sample) throws ParseException
	{
		SequenceTableProjectLevel objprojectseq = new SequenceTableProjectLevel();
		SequenceTableTaskLevel objtaskseq = new SequenceTableTaskLevel();
		SequenceTable seqorder = null;
		try {
			seqorder = validateandupdatesamplesequencenumber(sample, objprojectseq, objtaskseq);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		boolean isrest = false;
		if(sample.getDerivedtype() == 3)
		{
			derivedsamplesrepository.save(sample.getParentsamples());
		}
		
		
			GetSampleSequence(sample,seqorder, objprojectseq, objtaskseq);
			if(sample.getSampleprojecthistory() != null) {				
			
			sample.getSampleprojecthistory().forEach(history -> {
				try {
					history.setCreateddate(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			sampleprojecthistoryrepository.save(sample.getSampleprojecthistory());
			}
		if(sample.getSampleprojectmap() != null) {
			sampleprojectmaprepository.save(sample.getSampleprojectmap());
		}
		
		if(sample.getSamplestoragemapping()!=null)
		{
			samplestoragemappingrepository.save(sample.getSamplestoragemapping());
		}
		
		samplerepository.save(sample);
		updatesequence(5,sample);
		return new ResponseEntity<>(sample, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> updateSample(Sample sample) throws ParseException {
		
		Sample objSample = samplerepository.findOne(sample.getSamplecode());
		objSample.setAssignedproject(sample.getAssignedproject());
		objSample.setModifieddate(commonfunction.getCurrentUtcTime());
		objSample.setModifiedby(sample.getModifiedby());
		objSample.setJsondata(sample.getJsondata());
		objSample.setExpirytype(sample.getExpirytype());
		objSample.setExpirydate(sample.getExpirydate());
		objSample.setOpenexpiry(sample.getOpenexpiry());
		objSample.setOpenexpiryperiod(sample.getOpenexpiryperiod());
		objSample.setOpenexpiryvalue(sample.getOpenexpiryvalue());
		objSample.setQuantity(sample.getQuantity());
		objSample.setQuarantine(sample.getQuarantine());
		objSample.setTrackconsumption(sample.getTrackconsumption());
		objSample.setStoragecondition(sample.getStoragecondition());
		objSample.setUsageoption(sample.getUsageoption());
		objSample.setNtransactionstatus(sample.getNtransactionstatus());
		objSample.setUnit(sample.getUnit());
		objSample.setDateofcollection(sample.getDateofcollection());
		if(sample.getSamplestoragemapping()!=null)
		{
			objSample.setSamplestoragemapping(sample.getSamplestoragemapping());
			samplestoragemappingrepository.save(objSample.getSamplestoragemapping());
		}
		if(sample.getDerivedtype() == 3)
		{
			derivedsamplesrepository.save(sample.getParentsamples());
			objSample.setParentsamples(sample.getParentsamples());
		}
		if (sample.getSampleprojecthistory() != null) {
			sample.getSampleprojecthistory().forEach(history -> {
				try {
					history.setCreateddate(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
		
		if(sample.getSampleprojectmap() != null) {
			sampleprojectmaprepository.save(sample.getSampleprojectmap());
		}
		
		sampleprojecthistoryrepository.save(sample.getSampleprojecthistory());
		samplerepository.save(objSample);
		return new ResponseEntity<>(objSample, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> getchildsample(List<Sample> sample)
	{
		List<DerivedSamples> derived = derivedsamplesrepository.findByParentsampleInOrderByDerivedsamplecode(sample);
		List<Integer> result = derived.stream()
                .map(DerivedSamples::getSamplecode)
                .collect(Collectors.toList());
		while (result.remove(null)) { 
        } 
		List<Sample> lstsample = samplerepository.findBysamplecodeInOrderBySamplecodeDesc(result);
		return new ResponseEntity<>(lstsample, HttpStatus.OK);
	}

	public Map<String, Object> getSampleAttachments(Map<String, Object> inputMap) {
			Map<String, Object> objMap = new HashMap<>();
			int samplecode = (int) inputMap.get("samplecode");
			List<SampleAttachments> objFilels = sampleAttachementsRepository.findBySamplecodeOrderByNsampleattachcodeDesc(samplecode);
			objMap.put("lsSampleAttachments", objFilels);
			return objMap;
	}

	public ResponseEntity<Object> getLinksOnSample(SampleLinks sampleLinks) {
		Map<String, Object> objMap = new HashMap<>();
		List<SampleLinks> objFilels = sampleLinkRepository.findByNsamplecodeOrderByNsamplelinkcodeDesc(sampleLinks.getNsamplecode());
		objMap.put("lsSampleLinks", objFilels);
		return new ResponseEntity<>(objMap, HttpStatus.OK);
	}

	public Sample sampleCloudUploadattachments(MultipartFile file, Integer nsampletypecode, Integer nsamplecatcode,
			Integer samplecode, String filename, String fileexe, Integer usercode, Date currentdate,
			Integer isMultitenant, Integer nsitecode) throws IOException {
		Sample objAttach = samplerepository.findOne(samplecode);
		SampleAttachments objattachment = new SampleAttachments();
		if (isMultitenant == 0) {
			if (fileManipulationservice.storeLargeattachment(filename, file) != null) {
				objattachment.setFileid(fileManipulationservice.storeLargeattachment(filename, file));
			}
		}

		objattachment.setFilename(filename);
		objattachment.setFileextension(fileexe);
		objattachment.setCreateby(lsuserMasterRepository.findByusercode(usercode));
		objattachment.setCreateddate(currentdate);
		objattachment.setSamplecode(samplecode);
		objattachment.setNsamplecatcode(nsamplecatcode);
		objattachment.setNsampletypecode(nsampletypecode);
		objattachment.setNstatus(1);
		objattachment.setNsitecode(nsitecode);

		if (objAttach != null && objAttach.getlsSampleAttachments() != null) {
			objAttach.getlsSampleAttachments().add(objattachment);
		} else {
			objAttach.setlsSampleAttachments(new ArrayList<SampleAttachments>());
			objAttach.getlsSampleAttachments().add(objattachment);
		}
		sampleAttachementsRepository.save(objAttach.getlsSampleAttachments());
		if (isMultitenant != 0) {
			String filenameval = "attach_" + objAttach.getSamplecode() + "_" + objAttach.getlsSampleAttachments()
					.get(objAttach.getlsSampleAttachments().lastIndexOf(objattachment)).getNsampleattachcode() + "_"
					+ filename;
			String id = cloudFileManipulationservice.storeLargeattachment(filenameval, file);
			if (id != null) {
				objattachment.setFileid(id);
			}

			sampleAttachementsRepository.save(objAttach.getlsSampleAttachments());
		}
		List<SampleAttachments> objFilels = sampleAttachementsRepository.findBySamplecodeOrderByNsampleattachcodeDesc(samplecode);
		objAttach.setlsSampleAttachments(objFilels);
		return objAttach;
	}

	public ResponseEntity<Object> uploadLinkforSample(SampleLinks sampleLinks) throws ParseException {
		sampleLinks.setCreateddate(commonfunction.getCurrentUtcTime());
		sampleLinkRepository.save(sampleLinks);
		return new ResponseEntity<>(sampleLinks, HttpStatus.OK);
	}

	public ResponseEntity<Object> deleteLinkforSample(SampleLinks sampleLinks) {
		sampleLinkRepository.delete(sampleLinks);
		return new ResponseEntity<>(sampleLinks, HttpStatus.OK);
	}
	public ResponseEntity<Object> updateAssignedProjectOnSample(Sample sam) {
	
		Sample sample = samplerepository.findOne(sam.getSamplecode());
		sample.setSampleprojectmap(sam.getSampleprojectmap());
		sampleprojectmaprepository.save(sample.getSampleprojectmap());
		
		samplerepository.save(sample);
		return new ResponseEntity<>(sample, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> getAssignedTaskOnSample(Sample sam) {
		
		List<SampleProjectMap> lssample = sampleprojectmaprepository.findBySamplecodeOrderBySampleprojectcode(sam.getSamplecode());
		return new ResponseEntity<>(lssample, HttpStatus.OK);
	}
	public ResponseEntity<Object> updatemsampleprojecthistory(SampleProjectHistory[] samplelist)
	{
		List<SampleProjectHistory> history = Arrays.asList(samplelist);
		history.forEach(his -> {
			try {
				his.setCreateddate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		sampleprojecthistoryrepository.save(history);
		return new ResponseEntity<>(samplelist, HttpStatus.OK);
	}

	public ResponseEntity<Object> getSampleProps(Integer nsiteInteger) {
		Map<String, Object> objMap = new HashMap<>();
		List<SampleType> lstSampleTypes =  new ArrayList<SampleType>();
		List<SampleCategory> lstCategories = new ArrayList<SampleCategory>();
		lstSampleTypes = sampleTypeRepository.findByNsitecodeOrderByNsampletypecodeDesc(nsiteInteger);
		lstCategories = sampleCategoryRepository.findByNsitecodeOrderByNsamplecatcodeDesc(nsiteInteger);
		List<Period> lstPeriods = periodRepository.findByNstatusOrderByNperiodcode(1);
		List<Unit> lstUnits = unitRepository.findByNsitecodeAndNstatusOrderByNunitcodeDesc(nsiteInteger,1);
		objMap.put("lstUnits", lstUnits);
		objMap.put("lstPeriods", lstPeriods);
		objMap.put("lstCategories", lstCategories);
		objMap.put("lstType", lstSampleTypes);
		return new ResponseEntity<>(objMap, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getSampleCategoryByIdBarCodeFilter(Map<String, Object> inputMap) {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		
		List<String> lstIds = (List<String>) inputMap.get("selectedRecord");
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		
		List<Sample> lstSampleCat = samplerepository.findBySamplecodeInAndNsitecode(lstIds, nsiteInteger);
		
		objmap.put("lstSampleCategory", lstSampleCat);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> updateSampleExpiry(Sample objSample)
			throws ParseException {

		Sample objInventory = samplerepository.findBySamplecode(objSample.getSamplecode());
		if(objSample.getPreviousstatus()!=null) {
			long Ntransactionstatus= objSample.getNtransactionstatus();
			updatesampleinventorytransactiondetails(objSample.getCreateby(),0,objInventory,Ntransactionstatus,objSample.getPreviousstatus());
		}
		if (objSample.getOpenexpiry()) {
			objInventory.setOpenexpiry(true);
			objInventory.setExpirydate(objSample.getExpirydate());
			objInventory.setNtransactionstatus(objSample.getNtransactionstatus());
			samplerepository.save(objInventory);
			return new ResponseEntity<>(objInventory, HttpStatus.OK);
		}
		objInventory.setOpenexpiry(objSample.getOpenexpiry());
		objInventory.setNstatus(objSample.getNstatus());
		objInventory.setNtransactionstatus(objSample.getNtransactionstatus());

		samplerepository.save(objInventory);
		return new ResponseEntity<>(objInventory, HttpStatus.OK);
	}
	public void updatesampleinventorytransactiondetails(LSuserMaster createdby ,Integer transactionscreen,Sample objInventory, long ntransactionstatus,long Previousstatus) throws ParseException {
		ElnresultUsedSample ElnresultUsedSample = new ElnresultUsedSample();
		ElnresultUsedSample.setCreatedbyusercode(createdby);
		ElnresultUsedSample.setSamplecode(objInventory.getSamplecode());
		//ElnresultUsedSample.setNmaterialcategorycode(objInventory.getSamplecategory().getNsamplecatcode());
		//ElnresultUsedSample.setNinventorycode(objInventory.getNmaterialinventorycode());
		//ElnresultUsedSample.setNmaterialtypecode(objInventory.getSampletype().getNsampletypecode());
		ElnresultUsedSample.setOrdercode(ntransactionstatus);
		ElnresultUsedSample.setTransactionscreen(transactionscreen);
		ElnresultUsedSample.setJsondata("");
		ElnresultUsedSample.setTemplatecode(-1);
		ElnresultUsedSample.setNstatus(1);
		ElnresultUsedSample.setResponse(new Response());
		ElnresultUsedSample.setCreateddate(commonfunction.getCurrentUtcTime());
		ElnresultUsedSample.getResponse().setStatus(true);
		ElnresultUsedSample.setStatuschangesFrom(Previousstatus);
		ElnresultUsedSample.setStatuschangesTo(ntransactionstatus);
		ElnresultUsedSampleRepository.save(ElnresultUsedSample);
	}
	public List<Sampleget> getsample(LSuserMaster objClass)
	{
		return samplerepository.findByNstatusAndNsitecodeOrderBySamplecodeDesc(1, objClass.getLssitemaster().getSitecode());
	}
	
	public ResponseEntity<Map<String, Object>> ImportDatatoStoreonSample(Map<String, Object> inputMap) throws ParseException {
		ObjectMapper obj = new ObjectMapper();
		Integer siteCode = Integer.parseInt((String) inputMap.get("sitecode"));
		List<String> UnitName = (List<String>) inputMap.get("UnitName");
		Date currentDate = commonfunction.getCurrentUtcTime();
		Map<String, Object> responseMap = new HashMap<>();
		List<Unit> unitvalues = obj.convertValue(inputMap.get("Unit"), new TypeReference<List<Unit>>() {
		});
		if (!UnitName.isEmpty()) {
			List<Unit> existunitData = unitRepository.findByNsitecodeAndSunitnameIgnoreCaseIn(siteCode, UnitName);
			Map<String, Unit> existUnitListvalues = existunitData.stream()
					.collect(Collectors.toMap(Unit::getSunitname, unit -> unit));
			unitvalues = unitvalues.stream().map(item -> {
				Unit existingUnit = existUnitListvalues.get(item.getSunitname());

				if (existingUnit != null) {
					return existingUnit;
				} else {
					item.setCreatedate(currentDate);
					return item;
				}
			}).collect(Collectors.toList());

			unitRepository.save(unitvalues);
		}

		List<SampleProjectMap> SampleProjectMapobj = obj.convertValue(inputMap.get("sampleprojectmap"),
				new TypeReference<List<SampleProjectMap>>() {
				});
		final Map<String, List<SampleProjectMap>> customizedSampleProjectMap = new HashMap<>();
		if (SampleProjectMapobj.size()>0) {
			sampleprojectmaprepository.save(SampleProjectMapobj);
			customizedSampleProjectMap.putAll(SampleProjectMapobj.stream()
					.collect(Collectors.groupingBy(SampleProjectMap::getSamplename, Collectors.toList())));
		}
		List<SampleProjectHistory> SampleProjectHistoryMapobj = obj.convertValue(inputMap.get("sampleprojecthistory"),
				new TypeReference<List<SampleProjectHistory>>() {
				});
		final Map<String, List<SampleProjectHistory>> customizedSampleProjectHistoryMap = new HashMap<>();
		if (SampleProjectHistoryMapobj.size()>0) {
			SampleProjectHistoryMapobj=SampleProjectHistoryMapobj.stream().map((items) ->{
				items.setCreateddate(currentDate);
				return items;
			}).collect(Collectors.toList());
			sampleprojecthistoryrepository.save(SampleProjectHistoryMapobj);
			customizedSampleProjectHistoryMap.putAll(SampleProjectHistoryMapobj.stream()
					.collect(Collectors.groupingBy(SampleProjectHistory::getSamplename, Collectors.toList())));
		}

		List<Sample> sampleDatas = obj.convertValue(inputMap.get("Sample"), new TypeReference<List<Sample>>() {
		});
		List<String> ElnsampleName = (List<String>) inputMap.get("SampleName");
		Map<String, Unit> unitfinalmap = unitvalues.stream()
				.collect(Collectors.toMap(Unit::getSunitname, unit -> unit));
		if (!sampleDatas.isEmpty()) {
			List<SampleStorageMapping> selectedStorageLocation = obj.convertValue(
					inputMap.get("selectedStorageLocation"), new TypeReference<List<SampleStorageMapping>>() {
					});
			if (selectedStorageLocation != null) {
				samplestoragemappingrepository.save(selectedStorageLocation);
			}
			Map<String, SampleStorageMapping> selectedStorageLocationobj = selectedStorageLocation.stream()
					.collect(Collectors.toMap(SampleStorageMapping::getSamplename,
							SampleStorageMapping -> SampleStorageMapping));
			Map<String, Long> existMaterialList = MaterialCategoryService.checkDeblicaterecord(ElnsampleName, siteCode,
					"Sample", "samplename");
			sampleDatas = sampleDatas.stream().map(itemsv -> {
				if (itemsv.getUnit() != null && itemsv.getUnit().getSunitname() != null && !unitfinalmap.isEmpty()) {
					itemsv.setUnit(unitfinalmap.get(itemsv.getUnit().getSunitname()));
				}
				String oldSamplename=itemsv.getSamplename();
				if (existMaterialList.get(itemsv.getSamplename()) > 0) {
					Long existcount = existMaterialList.get(itemsv.getSamplename()) + 1;
					itemsv.setSamplename(itemsv.getSamplename() + "(" + existcount + ")");
				}
				SequenceTableProjectLevel objprojectseq = new SequenceTableProjectLevel();
				SequenceTableTaskLevel objtaskseq = new SequenceTableTaskLevel();
				SequenceTable seqorder = null;
				try {
					seqorder = validateandupdatesamplesequencenumber(itemsv, objprojectseq, objtaskseq);
					GetSampleSequence(itemsv, seqorder, objprojectseq, objtaskseq);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				itemsv.setCreateddate(currentDate);
				if (selectedStorageLocationobj.get(oldSamplename) != null) {
					itemsv.setSamplestoragemapping(selectedStorageLocationobj.get(oldSamplename));
				}
//				if (existMaterialList.get(oldSamplename) == 0) {
				if (SampleProjectMapobj.size()>0 
						&& customizedSampleProjectMap.get(oldSamplename)!=null) {
					List<SampleProjectMap> mpm = customizedSampleProjectMap.get(oldSamplename);
					itemsv.setSampleprojectmap(mpm);
				}
				if (inputMap.get("sampleprojecthistory")!=null && customizedSampleProjectHistoryMap.get(oldSamplename)!=null) {
					List<SampleProjectHistory> mpmh = customizedSampleProjectHistoryMap.get(oldSamplename);
					itemsv.setSampleprojecthistory(mpmh);
				}
//				}
				return itemsv;
			}).filter(Objects::nonNull).collect(Collectors.toList());

			samplerepository.save(sampleDatas);

		}
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}
}
