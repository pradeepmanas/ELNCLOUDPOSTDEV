package com.agaram.eln.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;

//import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Converter extends AbstractHttpMessageConverter<Object> {

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

//	@Inject
//	private ObjectMapper objectMapper;

	public Converter() {
		super(MediaType.APPLICATION_JSON_UTF8, new MediaType("application", "*+json", DEFAULT_CHARSET));
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//    	mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//    	System.out.println(inputMessage.getHeaders().get("authorization").get(0));
		String contenttype = inputMessage.getHeaders().get("Content-Type").get(0);
		String encoding = inputMessage.getHeaders().get("accept-encoding") != null
				? inputMessage.getHeaders().get("accept-encoding").get(0)
				: inputMessage.getHeaders().get("accept").get(0);
//		String contendencoding = inputMessage.getHeaders().get("content-encoding");

		String contendencoding = "";

		if (inputMessage.getHeaders().containsKey("content-encoding")) {
			contendencoding = inputMessage.getHeaders().get("content-encoding").get(0);
			System.out.println("content-encoding header : " + contendencoding);
		} else {
			contendencoding = "normal";
		}

		String decryptionkey = "1234567812345678";
//    	if(inputMessage.getHeaders().get("authorization") != null && 
//    			inputMessage.getHeaders().get("authorization").size()>0 && 
//    			!inputMessage.getHeaders().get("authorization").get(0).equals("undefined"))
//    	{
//    		String requestTokenHeader = inputMessage.getHeaders().get("authorization").get(0);
//    		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
//    			String jwtToken = requestTokenHeader.substring(7);
//    			decryptionkey = jwtToken.substring(0, 16);
//    		}
//    		else
//    		{
//    			decryptionkey = "1234567812345678";
//    		}
//    	}
//    	else 
//    	{
//    		decryptionkey = "1234567812345678";
//    	}


//    	  System.out.println("Encoding for print :" + encoding );
		System.out.println("contant type :" + contenttype);
		System.out.println("encoding :" + encoding);
		System.out.println("contendencoding :" + contendencoding);
		if (contenttype.equalsIgnoreCase("application/json;charset=UTF-8")
				&& encoding.contains("gzip")
//				&& (encoding.equalsIgnoreCase("gzip, deflate") || encoding.equalsIgnoreCase("gzip, deflate, br")|| encoding.equalsIgnoreCase("gzip, deflate, br, zstd"))
				&& contendencoding.equalsIgnoreCase("gzip")
				) {
			System.out.println("come decryption");
			return mapper.readValue(decrypt(inputMessage.getBody(), decryptionkey), clazz);
		} else {
			System.out.println("normal");
			return mapper.readValue(inputMessage.getBody(), clazz);
		}

	}

//	@Override
//	protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
//	    ObjectMapper mapper = new ObjectMapper();
//	    mapper.setSerializationInclusion(Include.NON_NULL);
//	    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//	    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//
//	    // Convert object to bytes
//	    byte[] bytes = mapper.writeValueAsBytes(o);
//	    outputMessage.getBody().write(encrypt(mapper.writeValueAsBytes(o)));
//	    // Compress the bytes
//	    byte[] compressedBytes = compress(bytes);
//
//	    // Set response headers
//	    HttpHeaders headers = outputMessage.getHeaders();
//	    headers.setContentType(MediaType.APPLICATION_JSON);
//	    headers.set("Content-Encoding", "gzip");
//
//	    // Write the compressed bytes to the response body
//	    OutputStream os = outputMessage.getBody();
//	    os.write(compressedBytes);
//	}
//
//	public byte[] compress(byte[] data) throws IOException {
//	    ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
//	    GZIPOutputStream gzip = new GZIPOutputStream(bos);
//	    gzip.write(data);
//	    gzip.close();
//	    byte[] compressedData = bos.toByteArray();
//	    bos.close();
//	    return compressedData;
//	}

//	@Override
//    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(Include.NON_NULL);
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//
//        // Convert object to bytes
//        byte[] bytes = mapper.writeValueAsBytes(o);
//
//        // Create a GZIPOutputStream to compress the bytes
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        GZIPOutputStream gzipos = new GZIPOutputStream(baos);
//        gzipos.write(bytes);
//        gzipos.close();
//
//        // Set response headers
//        HttpHeaders headers = outputMessage.getHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Content-Encoding", "gzip");
//
//        // Write the output stream to the response body
////        outputMessage.getBody().write(encrypt(baos.toByteArray()));
//        OutputStream os = outputMessage.getBody();
//        os.write(baos.toByteArray());
////        os.flush();
////        os.close();
//    }

	private byte[] compress(byte[] data) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
			gzipStream.write(data);
		}
		return byteStream.toByteArray();
	}

	@Override
	protected void writeInternal(Object o, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//    	System.out.println("write "+ outputMessage.getHeaders().get("authorization").get(0));
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String requestUrl = request.getRequestURI();
		System.out.println(
				"---------------------------------------------------------------------------------------------------------------");
		System.out.println("ZipDataKumu" + requestUrl);
		requestUrl = requestUrl.contains("/ELNPOSTGRE-0.0.1-SNAPSHOT")
				? requestUrl.substring("/ELNPOSTGRE-0.0.1-SNAPSHOT".length())
				: requestUrl.contains("/ELN-0.0.1-SNAPSHOT") ? requestUrl.substring("/ELN-0.0.1-SNAPSHOT".length())
						: requestUrl;

		List<String> uncompressedUrls = Arrays.asList("/protocol/uploadprotocolsfile",
				"/protocol/Uploadprotocolimage", "/protocol/removeprotocolimage", "/protocol/removeprotocolimagesql", 
				"/protocol/loadprotocolfiles", "/protocol/uploadvideo", "/protocol/uploadvideosql", 
				"/protocol/removeprotocolvideo", "/protocol/removeprotocolvideossql", "/protocol/uploadprotocolsorderfile",
				"/protocol/uploadprotocolsorderfilesql", "/protocol/Uploadprotocolorderimage",
				"/protocol/Uploadprotocolorderimagesql", "/protocol/removeprotocoorderlimage",
				"/protocol/removeprotocoorderlimagesql", "/protocol/loadprotocolorderfiles",
				"/protocol/uploadprotocolordervideo", "/protocol/downloadprotocolordervideosql",
				"/protocol/removeprotocolordervideo", "/protocol/removeprotocolordervideossql",
				"/protocol/downloadprotocolimage", "/Lims/getSheetsFromELN", "/Lims/downloadSheetFromELN",
				"/Lims/downloadResultFromELN", "/Lims/updateSheetsParameterForELN", "/Lims/getAttachmentsForLIMS",
				"/Lims/getOrdersFromELN", "/Lims/getOrderTagFromELN", "/Lims/getSiteFromELN", "/Lims/getUsersFromELN",
				"/Instrument/uploadsheetimagesSql", "/Instrument/uploadsheetimages", "/Instrument/downloadsheetimages",
				"/Instrument/downloadsheetimagessql",
                "/evaluateParser",
        		"/findByStatus",
				"/getMethod", "/getParserData", "/getMethodDelimiter", "/getParserMethod", "/getDelimiters",
				"/getSubParserMethod", "/getParserFieldTechniqueListByMethodKey", "/getmethodversion",
//				"/protocol/Uploadprotocolimagesql",
//				"/protocol/uploadprotocolsfilesql",
//        		"/MethodExportController/exportMethods",
//        		"/MethodImportController/importMethods",
//        		"/downloadFile/{fileName}",
				// "/getFileData",
				"/Login/LoadSitewithoutgzip", "/Login/Logintenat/","/Login/importchemdata",
				    "/DashBoardDemo/Getdashboardordercount",
	                "/DashBoardDemo/Getdashboardorders","/documenteditor/api/wordeditor/Import","/smartdevice/Getdata"
	                ,"/documenteditor/Import","/documenteditor/api/wordeditor/RestrictEditing"
	          
//				"/protocol/Uploadprotocolimageondrag"

		);
		byte[] uncompressedBytes = mapper.writeValueAsBytes(o);
		byte[] compressedBytes;

		if (uncompressedUrls.contains(requestUrl)) {
		    // Don't compress the response
		    compressedBytes = uncompressedBytes;
		} else {
		    // Compress the response
		    compressedBytes = compress(uncompressedBytes);
		}
		outputMessage.getBody().write(compressedBytes);
	}
//	@Override
//	protected void writeInternal(Object o, HttpOutputMessage outputMessage)
//	        throws IOException, HttpMessageNotWritableException {
//	    ObjectMapper mapper = new ObjectMapper();
//	    mapper.setSerializationInclusion(Include.NON_NULL);
//	    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//	    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//
//	    // Compress the response body.
//	    GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputMessage.getBody());
//	    gzipOutputStream.write(mapper.writeValueAsBytes(o));
//	    gzipOutputStream.close();
//
//	    // Add the "Content-Encoding" header
////	    HttpHeaders headers = new HttpHeaders();
////
////        // Add the "Content-Encoding" header.
////        headers.add(HttpHeaders.CONTENT_ENCODING, "gzip");
//
//        // Copy the headers from the new HttpHeaders object to the outputMessage object.
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Custom-Header", "Value");
//        outputMessage.getHeaders().putAll(headers);
//	}

	// correct
//    @Override
//    protected void writeInternal(Object o, HttpOutputMessage outputMessage)
//            throws IOException, HttpMessageNotWritableException {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(Include.NON_NULL);
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//
//        // Compress the response body.
//        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputMessage.getBody());
//        gzipOutputStream.write(mapper.writeValueAsBytes(o));
//        gzipOutputStream.close();
//    }

	private InputStream decrypt(InputStream inputStream, String key) {

		InputStream in;
		byte[] bytes = null;
		try {
			in = new GZIPInputStream(inputStream);

			bytes = IOUtils.toByteArray(in);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

//        StringBuilder requestParamString = new StringBuilder();
//        try (Reader reader = new BufferedReader(new InputStreamReader
//                (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
//            int c;
//            while ((c = reader.read()) != -1) {
//                requestParamString.append((char) c);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {

//            JSONObject requestJsonObject = new
//                    JSONObject(requestParamString.toString().replace("\n", ""));

//            String decryptRequestString = AESEncryption.decryptcontant(requestJsonObject.getString("data"), key);
//            String decryptRequestString = requestParamString.toString().replace("\n", "");
//            System.out.println("decryptRequestString: " + decryptRequestString);
//
//            if (decryptRequestString != null) {
//            	return new ByteArrayInputStream(decryptRequestString.getBytes(StandardCharsets.UTF_8));
//            } else {
//                return inputStream;
//            }
//        } catch (JSONException err) {
//            return inputStream;
//        }

		return new ByteArrayInputStream(bytes);
	}

	private byte[] encrypt(byte[] bytesToEncrypt) {
//        String apiJsonResponse = new String(bytesToEncrypt);
//
//        String encryptedString = AESEncryption.encryptcontant(apiJsonResponse);
//        if (encryptedString != null) {
//           
//            Map<String, String> hashMap = new HashMap<>();

//        	try { 	
//    		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
//    		DeflaterOutputStream outputStream = new DeflaterOutputStream(arrayOutputStream);
//    	      outputStream.write(bytesToEncrypt);
//    	      outputStream.close();
//    	      hashMap.put("data", arrayOutputStream.toString());
//              JSONObject jsob = new JSONObject(hashMap);
//              return jsob.toString().getBytes();
//    	  
//    	} catch (IOException e) {
//    	    throw new RuntimeException(e);
//    	  }

//        } else
//        {
		return bytesToEncrypt;
//        }

//        return encryptedString.getBytes();
	}
}
