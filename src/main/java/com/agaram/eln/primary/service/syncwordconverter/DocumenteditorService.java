package com.agaram.eln.primary.service.syncwordconverter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.spi.IIORegistry;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.reports.reportdesigner.Reporttemplate;
import com.agaram.eln.primary.model.syncwordconverter.CustomParameter;
import com.agaram.eln.primary.model.syncwordconverter.CustomRestrictParameter;
import com.agaram.eln.primary.model.syncwordconverter.SaveParameter;
import com.agaram.eln.primary.model.syncwordconverter.SpellCheckJsonData;
import com.agaram.eln.primary.repository.reports.reportdesigner.ReporttemplateRepository;
import com.agaram.eln.primary.service.protocol.Commonservice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syncfusion.docio.WordDocument;
import com.syncfusion.ej2.spellchecker.SpellChecker;
import com.syncfusion.ej2.wordprocessor.FormatType;
import com.syncfusion.ej2.wordprocessor.MetafileImageParsedEventArgs;
import com.syncfusion.ej2.wordprocessor.MetafileImageParsedEventHandler;
import com.syncfusion.ej2.wordprocessor.WordProcessorHelper;
import com.syncfusion.javahelper.system.collections.generic.ListSupport;
import com.syncfusion.javahelper.system.io.StreamSupport;
import com.syncfusion.javahelper.system.reflection.AssemblySupport;
import com.twelvemonkeys.imageio.plugins.tiff.TIFFImageReaderSpi;

@Service
public class DocumenteditorService {

	@Autowired
	Commonservice commonservice;

	@Autowired
	public ReporttemplateRepository reporttemplateRepository;

	public String uploadFile(MultipartFile file) throws Exception {
		try {
			return WordProcessorHelper.load(file.getInputStream(), FormatType.Docx);
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"sections\":[{\"blocks\":[{\"inlines\":[{\"text\":" + e.getMessage() + "}]}]}]}";
		}
	}

	public String test() {
		System.out.println("==== in test ====");
		return "{\"sections\":[{\"blocks\":[{\"inlines\":[{\"text\":\"Hello World\"}]}]}]}";
	}

	public Map<String, String> importFile(MultipartFile file) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		;
		try {
			String name = file.getOriginalFilename();
			if (name == null || name.isEmpty()) {
				name = "Document1.docx";
			}
			String format = retrieveFileType(name);

			MetafileImageParsedEventHandler metafileImageParsedEvent = new MetafileImageParsedEventHandler() {

				ListSupport<MetafileImageParsedEventHandler> delegateList = new ListSupport<MetafileImageParsedEventHandler>(
						MetafileImageParsedEventHandler.class);

				// Represents event handling for MetafileImageParsedEventHandlerCollection.
				public void invoke(Object sender, MetafileImageParsedEventArgs args) throws Exception {
					onMetafileImageParsed(sender, args);
				}

				// Represents the method that handles MetafileImageParsed event.
				public void dynamicInvoke(Object... args) throws Exception {
					onMetafileImageParsed((Object) args[0], (MetafileImageParsedEventArgs) args[1]);
				}

				// Represents the method that handles MetafileImageParsed event to add
				// collection item.
				public void add(MetafileImageParsedEventHandler delegate) throws Exception {
					if (delegate != null)
						delegateList.add(delegate);
				}

				// Represents the method that handles MetafileImageParsed event to remove
				// collection
				// item.
				public void remove(MetafileImageParsedEventHandler delegate) throws Exception {
					if (delegate != null)
						delegateList.remove(delegate);
				}
			};
			// Hooks MetafileImageParsed event.
			WordProcessorHelper.MetafileImageParsed.add("OnMetafileImageParsed", metafileImageParsedEvent);
			// Converts DocIO DOM to SFDT DOM.
			String sfdtContent = WordProcessorHelper.load(file.getInputStream(), getFormatType(format));
			// Unhooks MetafileImageParsed event.
			WordProcessorHelper.MetafileImageParsed.remove("OnMetafileImageParsed", metafileImageParsedEvent);

			JSONObject jsonObject = new JSONObject(sfdtContent);
			map.put("sfdt", jsonObject.getString("sfdt"));
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("sfdt", "{\"sections\":[{\"blocks\":[{\"inlines\":[{\"text\":" + e.getMessage() + "}]}]}]}");
			return map;
		}
	}

	// Converts Metafile to raster image.
	private static void onMetafileImageParsed(Object sender, MetafileImageParsedEventArgs args) throws Exception {
		if (args.getIsMetafile()) {
			// You can write your own method definition for converting Metafile to raster
			// image using any third-party image converter.
			args.setImageStream(convertMetafileToRasterImage(args.getMetafileStream()));
		} else {
			InputStream inputStream = StreamSupport.toStream(args.getMetafileStream());
			// Use ByteArrayOutputStream to collect data into a byte array
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			// Read data from the InputStream and write it to the ByteArrayOutputStream
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				byteArrayOutputStream.write(buffer, 0, bytesRead);
			}

			// Convert the ByteArrayOutputStream to a byte array
			byte[] tiffData = byteArrayOutputStream.toByteArray();
			// Read TIFF image from byte array
			ByteArrayInputStream tiffInputStream = new ByteArrayInputStream(tiffData);
			IIORegistry.getDefaultInstance().registerServiceProvider(new TIFFImageReaderSpi());

			// Create ImageReader and ImageWriter instances
			ImageReader tiffReader = ImageIO.getImageReadersByFormatName("TIFF").next();
			ImageWriter pngWriter = ImageIO.getImageWritersByFormatName("PNG").next();

			// Set up input and output streams
			tiffReader.setInput(ImageIO.createImageInputStream(tiffInputStream));
			ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
			pngWriter.setOutput(ImageIO.createImageOutputStream(pngOutputStream));

			// Read the TIFF image and write it as a PNG
			BufferedImage image = tiffReader.read(0);
			pngWriter.write(image);
			pngWriter.dispose();
			byte[] jpgData = pngOutputStream.toByteArray();
			InputStream jpgStream = new ByteArrayInputStream(jpgData);
			args.setImageStream(StreamSupport.toStream(jpgStream));
		}
	}

	private static StreamSupport convertMetafileToRasterImage(StreamSupport ImageStream) throws Exception {
		// Here we are loading a default raster image as fallback.
		StreamSupport imgStream = getManifestResourceStream("ImageNotFound.jpg");
		return imgStream;
		// To do : Write your own logic for converting Metafile to raster image using
		// any third-party image converter(Syncfusion doesn't provide any image
		// converter).
	}

	private static StreamSupport getManifestResourceStream(String fileName) throws Exception {
		AssemblySupport assembly = AssemblySupport.getExecutingAssembly();
		return assembly.getManifestResourceStream("ImageNotFound.jpg");
	}

	public String spellCheck(SpellCheckJsonData spellChecker) throws Exception {
		try {
			SpellChecker spellCheck = new SpellChecker();
			String data = spellCheck.getSuggestions(spellChecker.languageID, spellChecker.texttoCheck,
					spellChecker.checkSpelling, spellChecker.checkSuggestion, spellChecker.addWord);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"SpellCollection\":[],\"HasSpellingError\":false,\"Suggestions\":null}";
		}
	}

	public String spellCheckByPage(SpellCheckJsonData spellChecker) throws Exception {
		try {
			SpellChecker spellCheck = new SpellChecker();
			String data = spellCheck.checkSpelling(spellChecker.languageID, spellChecker.texttoCheck);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"SpellCollection\":[],\"HasSpellingError\":false,\"Suggestions\":null}";
		}
	}

	public String[] restrictEditing(CustomRestrictParameter param) throws Exception {
		if (param.passwordBase64 == "" && param.passwordBase64 == null)
			return null;
		return WordProcessorHelper.computeHash(param.passwordBase64, param.saltBase64, param.spinCount);
	}

	public String systemClipboard(CustomParameter param) {
		if (param.content != null && param.content != "") {
			try {
				MetafileImageParsedEventHandler metafileImageParsedEvent = new MetafileImageParsedEventHandler() {

					ListSupport<MetafileImageParsedEventHandler> delegateList = new ListSupport<MetafileImageParsedEventHandler>(
							MetafileImageParsedEventHandler.class);

					// Represents event handling for MetafileImageParsedEventHandlerCollection.
					public void invoke(Object sender, MetafileImageParsedEventArgs args) throws Exception {
						onMetafileImageParsed(sender, args);
					}

					// Represents the method that handles MetafileImageParsed event.
					public void dynamicInvoke(Object... args) throws Exception {
						onMetafileImageParsed((Object) args[0], (MetafileImageParsedEventArgs) args[1]);
					}

					// Represents the method that handles MetafileImageParsed event to add
					// collection item.
					public void add(MetafileImageParsedEventHandler delegate) throws Exception {
						if (delegate != null)
							delegateList.add(delegate);
					}

					// Represents the method that handles MetafileImageParsed event to remove
					// collection
					// item.
					public void remove(MetafileImageParsedEventHandler delegate) throws Exception {
						if (delegate != null)
							delegateList.remove(delegate);
					}
				};
				// Hooks MetafileImageParsed event.
				WordProcessorHelper.MetafileImageParsed.add("OnMetafileImageParsed", metafileImageParsedEvent);
				String json = WordProcessorHelper.loadString(param.content, getFormatType(param.type.toLowerCase()));
				// Unhooks MetafileImageParsed event.
				WordProcessorHelper.MetafileImageParsed.remove("OnMetafileImageParsed", metafileImageParsedEvent);
				return json;
			} catch (Exception e) {
				return "";
			}
		}
		return "";
	}

	public Reporttemplate save(Reporttemplate data) throws Exception {
		try {
			Response response = new Response();
			boolean Isnew_Template = false;
			if (data.getTemplatecode() == null) {
				Isnew_Template = true;
			}
			Reporttemplate existingTemplate = new Reporttemplate();
			String name = data.getTemplatename() + ".json";
			String jsonContent = convertObjectToJson(data.getTemplatecontent());
			byte[] documentBytes = jsonContent.getBytes(StandardCharsets.UTF_8);
			String uniqueDocumentName = name + "_" + UUID.randomUUID().toString() + ".json";
			if (Isnew_Template) {
				existingTemplate = reporttemplateRepository
						.findByTemplatenameIgnoreCaseAndSitemaster(data.getTemplatename(), data.getSitemaster());
				if (existingTemplate == null) {
					data = commonservice.uploadToAzureBlobStorage(documentBytes, data, uniqueDocumentName);
					data.setDateCreated(commonfunction.getCurrentUtcTime());
					reporttemplateRepository.save(data);
					response.setStatus(true);
				
				} else {
					response.setStatus(false);
					response.setInformation("IDS_MSG_ALREADY");
				
				}
			}else {
				uniqueDocumentName=data.getFileuid();
				data = commonservice.uploadToAzureBlobStorage(documentBytes, data, uniqueDocumentName);
				data.setDateModified(commonfunction.getCurrentUtcTime());
				reporttemplateRepository.save(data);
				response.setStatus(true);
				
			}
            	data.setResponce(response);
            	return data;
		} catch (Exception ex) {
			throw new Exception("Failed to save document: " + ex.getMessage(), ex);
		}

	}

	private String convertObjectToJson(Object object) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ResponseEntity<Resource> exportSFDT(SaveParameter data) throws Exception {
		try {
			String name = data.getFileName();
			if (name == null || name.isEmpty()) {
				name = "Document1.docx";
			}
			String format = retrieveFileType(name);

			WordDocument document = WordProcessorHelper.save(data.getContent());
			return saveDocument(document, format);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public ResponseEntity<Resource> export(MultipartFile data, String fileName) throws Exception {
		try {
			String name = fileName;
			if (name == null || name.isEmpty()) {
				name = "Document1";
			}
			String format = retrieveFileType(name);

			WordDocument document = new WordDocument(data.getInputStream(), com.syncfusion.docio.FormatType.Docx);
			return saveDocument(document, format);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	private ResponseEntity<Resource> saveDocument(WordDocument document, String format) throws Exception {
		String contentType = "";
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		com.syncfusion.docio.FormatType type = getWFormatType(format);
		switch (type.toString()) {
		case "Rtf":
			contentType = "application/rtf";
			break;
		case "WordML":
			contentType = "application/xml";
			break;
		case "Dotx":
			contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.template";
			break;
		case "Docx":
			contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
			break;
		case "Html":
			contentType = "application/html";
			break;
		}
		document.save(outStream, type);
		ByteArrayResource resource = new ByteArrayResource(outStream.toByteArray());
		outStream.close();
		document.close();

		return ResponseEntity.ok().contentLength(resource.contentLength())
				.contentType(MediaType.parseMediaType(contentType)).body(resource);
	}

	private String retrieveFileType(String name) {
		int index = name.lastIndexOf('.');
		String format = index > -1 && index < name.length() - 1 ? name.substring(index) : ".docx";
		return format;
	}

	static com.syncfusion.docio.FormatType getWFormatType(String format) throws Exception {
		if (format == null || format.trim().isEmpty())
			throw new Exception("EJ2 WordProcessor does not support this file format.");
		switch (format.toLowerCase()) {
		case ".dotx":
			return com.syncfusion.docio.FormatType.Dotx;
		case ".docm":
			return com.syncfusion.docio.FormatType.Docm;
		case ".dotm":
			return com.syncfusion.docio.FormatType.Dotm;
		case ".docx":
			return com.syncfusion.docio.FormatType.Docx;
		case ".rtf":
			return com.syncfusion.docio.FormatType.Rtf;
		case ".html":
			return com.syncfusion.docio.FormatType.Html;
		case ".txt":
			return com.syncfusion.docio.FormatType.Txt;
		case ".xml":
			return com.syncfusion.docio.FormatType.WordML;
		default:
			throw new Exception("EJ2 WordProcessor does not support this file format.");
		}
	}

	static FormatType getFormatType(String format) {
		switch (format) {
		case ".dotx":
		case ".docx":
		case ".docm":
		case ".dotm":
			return FormatType.Docx;
		case ".dot":
		case ".doc":
			return FormatType.Doc;
		case ".rtf":
			return FormatType.Rtf;
		case ".txt":
			return FormatType.Txt;
		case ".xml":
			return FormatType.WordML;
		case ".html":
			return FormatType.Html;
		default:
			return FormatType.Docx;
		}
	}
}
