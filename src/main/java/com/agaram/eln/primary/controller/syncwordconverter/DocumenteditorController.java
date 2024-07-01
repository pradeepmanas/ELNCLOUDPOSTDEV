package com.agaram.eln.primary.controller.syncwordconverter;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.model.syncwordconverter.CustomParameter;
import com.agaram.eln.primary.model.syncwordconverter.CustomRestrictParameter;
import com.agaram.eln.primary.model.syncwordconverter.SaveParameter;
import com.agaram.eln.primary.model.syncwordconverter.SpellCheckJsonData;
import com.agaram.eln.primary.service.syncwordconverter.DocumenteditorService;

@RestController
@RequestMapping(value = "/documenteditor")
public class DocumenteditorController {

	@Autowired
	private DocumenteditorService documenteditorService;
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/Import")
    public String uploadFile(@RequestParam("files") MultipartFile file) throws Exception {
        return documenteditorService.uploadFile(file);
    }
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/api/wordeditor/test")
	public String test() {
		return documenteditorService.test();
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/api/wordeditor/Import")
	public Map<String, String> importFile(@RequestParam("files") MultipartFile file) throws Exception {
		return documenteditorService.importFile(file);
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/api/wordeditor/SpellCheck")
	public String spellCheck(@RequestBody SpellCheckJsonData spellChecker) throws Exception {
		return documenteditorService.spellCheck(spellChecker);
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/api/wordeditor/RestrictEditing")
	public String[] restrictEditing(@RequestBody CustomRestrictParameter param) throws Exception {
		return documenteditorService.restrictEditing(param);
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/api/wordeditor/SystemClipboard")
	public String systemClipboard(@RequestBody CustomParameter param) {
		return documenteditorService.systemClipboard(param);
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/api/wordeditor/Save")
	public void save(@RequestBody SaveParameter data) throws Exception {
		documenteditorService.save(data);
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/api/wordeditor/ExportSFDT")
	public ResponseEntity<Resource> exportSFDT(@RequestBody SaveParameter data) throws Exception {
		return documenteditorService.exportSFDT(data);
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/api/wordeditor/Export")
	public ResponseEntity<Resource> export(@RequestParam("data") MultipartFile data, String fileName) throws Exception {
		return documenteditorService.export(data,fileName);
	}
}
