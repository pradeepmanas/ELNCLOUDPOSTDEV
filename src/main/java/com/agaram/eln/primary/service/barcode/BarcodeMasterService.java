package com.agaram.eln.primary.service.barcode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.barcode.BarcodeMaster;
import com.agaram.eln.primary.model.material.ElnmaterialInventory;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.barcode.BarcodeMasterRepository;
import com.agaram.eln.primary.repository.material.ElnmaterialInventoryRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.fileManipulation.FileManipulationservice;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BarcodeMasterService {
	
	@Autowired
	private BarcodeMasterRepository barcodemasterrepository;
	
	@Autowired
	private FileManipulationservice fileManipulationservice;
	
	@Autowired
	private CloudFileManipulationservice cloudFileManipulationservice;
	
	@Autowired
	private ElnmaterialInventoryRepository elnmaterialInventoryReppository;
	
	public ResponseEntity<Object> InsertBarcode(MultipartHttpServletRequest request)
			throws JsonMappingException, JsonProcessingException, ParseException {
		Map<String, Object> returnMap = new HashMap();
		final ObjectMapper mapper = new ObjectMapper();
		try {
			final BarcodeMaster barcode = mapper.readValue(request.getParameter("barcode"), BarcodeMaster.class);
			Integer isMultitenant = Integer.parseInt(request.getParameter("isMultitenant"));
			String filename = request.getParameter("filename");
			List<MultipartFile> file = request.getFiles("file");
			String UUId = "";
			Date currentdate = commonfunction.getCurrentUtcTime();
			barcode.setCreatedon(currentdate);
			if(file.size() > 0)
			{
				if(isMultitenant == 1 || isMultitenant==2)
				{
					UUID objGUID = UUID.randomUUID();
					String randomUUIDString = objGUID.toString();
					UUId = cloudFileManipulationservice.storecloudfilesreturnwithpreUUID(file.get(0), "barcodefiles", randomUUIDString,
							isMultitenant);
				}
				else
				{
					UUId = fileManipulationservice.storeLargeattachment(filename, file.get(0));
				}
				barcode.setBarcodefilename(filename);
				barcode.setBarcodefileid(UUId);
			}
			barcodemasterrepository.save(barcode);
			returnMap.put("Barcode", barcode);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ResponseEntity<Object>(returnMap, HttpStatus.OK);
	}
	
	public List<BarcodeMaster> GetBarcodemaster(LSuserMaster objuser)
	{
		List<BarcodeMaster> lstbarcode = new ArrayList<BarcodeMaster>();
		lstbarcode = barcodemasterrepository.findByOrderByBarcodenoDesc();
		return lstbarcode;
	}
	
	public ResponseEntity<InputStreamResource> getbarcodefileoncode(String barcodeid, String ismultitenant, String tenant,
			String screen, String primarykey, String path, String username
			) throws JsonParseException, JsonMappingException, IOException, NumberFormatException, ParseException {
		
		BarcodeMaster barcode = barcodemasterrepository.findOne(Integer.parseInt(barcodeid));
		HttpHeaders header = new HttpHeaders();
		InputStreamResource resource = null;
		
		if(Integer.parseInt(ismultitenant) == 1 || Integer.parseInt(ismultitenant)==2)
		{
			InputStream stream =  cloudFileManipulationservice.retrieveCloudFile(barcode.getBarcodefileid(), tenant+"barcodefiles");
			String data = readFromInputStream(stream);
			
			switch(screen)
			{
				case "1":
					data = updatematerialcontent(data, Integer.parseInt(primarykey),path,username);
				break;
			}
			
//			data = data.replace("$BarcodeId$", barcode.getBarcodename());
			RestTemplate restTemplate = new RestTemplate();
			String uri = "http://api.labelary.com/v1/printers/8dpmm/labels/4x6/0/"+data;
			HttpHeaders headers = new HttpHeaders();

			HttpEntity<String> entity = new HttpEntity<String>(headers);
			
			ResponseEntity<byte[]> ctc = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
			byte[] barcodearray = ctc.getBody();
			
			int size = barcodearray.length;
			InputStream is = null;
			try {
				is = new ByteArrayInputStream(barcodearray);
				resource = new InputStreamResource(is);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (is != null)
						is.close();
				} catch (Exception ex) {

				}
			}
			
			header.set("Content-Disposition", "attachment; filename=" + "label.png");
			header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			header.setContentLength(size);
			
		}
		else
		{
			
		}
		return new ResponseEntity<>(resource, header, HttpStatus.OK);
	}
	
	@SuppressWarnings("deprecation")
	private String updatematerialcontent(String data, Integer materialcode,String path, String username) throws ParseException
	{
		ElnmaterialInventory inventory = elnmaterialInventoryReppository.findOne(materialcode);
		if(inventory != null)
		{
			Date currentdata = commonfunction.getCurrentUtcTime();
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd ");
			data = data.replace("$materialid$", inventory.getSinventoryid()).replace("$materialname$", inventory.getMaterial().getSmaterialname())
					.replace("$storagepath$", path).replace("$batchno$", inventory.getSbatchno()).replace("$generatedby$", username)
					.replace("$generateddate$", dateFormat.format(currentdata));
			
		}
		return data;
	}
	
	private String readFromInputStream(InputStream inputStream)
			  throws IOException {
			    StringBuilder resultStringBuilder = new StringBuilder();
			    try (BufferedReader br
			      = new BufferedReader(new InputStreamReader(inputStream))) {
			        String line;
			        while ((line = br.readLine()) != null) {
			            resultStringBuilder.append(line).append("\n");
			        }
			    }
			  return resultStringBuilder.toString();
	}

}
