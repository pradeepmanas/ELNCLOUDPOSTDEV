package com.agaram.eln.primary.service.sample;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.equipment.Equipment;
import com.agaram.eln.primary.model.material.MaterialAttachments;
import com.agaram.eln.primary.model.sample.DerivedSamples;
import com.agaram.eln.primary.model.sample.Sample;
import com.agaram.eln.primary.model.sample.SampleAttachments;
import com.agaram.eln.primary.model.sample.SampleCategory;
import com.agaram.eln.primary.model.sample.SampleLinks;
import com.agaram.eln.primary.model.sequence.SequenceTable;
import com.agaram.eln.primary.model.sequence.SequenceTableProjectLevel;
import com.agaram.eln.primary.model.sequence.SequenceTableSite;
import com.agaram.eln.primary.model.sequence.SequenceTableTaskLevel;
import com.agaram.eln.primary.model.sheetManipulation.LSfiletest;
import com.agaram.eln.primary.repository.sample.DerivedSamplesRepository;
import com.agaram.eln.primary.repository.sample.SampleAttachementsRepository;
import com.agaram.eln.primary.repository.sample.SampleLinkRepository;
import com.agaram.eln.primary.repository.sample.SampleRepository;
import com.agaram.eln.primary.repository.sample.SampleStorageMappingRepository;
import com.agaram.eln.primary.repository.sequence.SequenceTableProjectLevelRepository;
import com.agaram.eln.primary.repository.sequence.SequenceTableRepository;
import com.agaram.eln.primary.repository.sequence.SequenceTableSiteRepository;
import com.agaram.eln.primary.repository.sequence.SequenceTableTaskLevelRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.fileManipulation.FileManipulationservice;

@Service
public class SampleService<ParentSample>{
	
	@Autowired
	private SampleRepository samplerepository;
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

	public ResponseEntity<Object> getSampleonCategory(SampleCategory objsamplecat){			
			List<Sample> lstsample = samplerepository.findBySamplecategoryAndNsitecodeOrderBySamplecodeDesc(objsamplecat,objsamplecat.getNsitecode());
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
				SimpleDateFormat month = new SimpleDateFormat("mm");
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
		SimpleDateFormat month = new SimpleDateFormat("mm");
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
	
	public ResponseEntity<Object> createSample(Sample sample)
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
		
		try {
			GetSampleSequence(sample,seqorder, objprojectseq, objtaskseq);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		if(sample.getSamplestoragemapping()!=null)
		{
			samplestoragemappingrepository.save(sample.getSamplestoragemapping());
		}
		
		samplerepository.save(sample);
		updatesequence(5,sample);
		return new ResponseEntity<>(sample, HttpStatus.OK);
	}

	public ResponseEntity<Object> updateSample(Sample sample)
	{
		samplerepository.save(sample);
		return new ResponseEntity<>(sample, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> getchildsample(List<Sample> sample)
	{
		List<DerivedSamples> derived = derivedsamplesrepository.findByParentsampleInOrderByDerivedsamplecode(sample);
		List<Integer> result = derived.stream()
                .map(DerivedSamples::getSamplecode)
                .collect(Collectors.toList());
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
	public void updateAssignedProjectOnSample(Map<String, Object> inputMap) {
		
		Integer selectedSample = Integer.parseInt(inputMap.get("selectedSample").toString());
		String task = inputMap.get("task").toString(); 
		
		Sample sample = samplerepository.findOne(selectedSample);
		sample.setAssignedproject(task);
		
		samplerepository.save(sample);
	}
	
	public ResponseEntity<Object> getAssignedTaskOnSample(Map<String, Object> inputMap) {
		Integer selectedSample = Integer.parseInt(inputMap.get("selectedSample").toString());
		
		Sample sample = samplerepository.findOne(selectedSample);
		return new ResponseEntity<>(sample.getAssignedproject(), HttpStatus.OK);
	}
}
