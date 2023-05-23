package com.agaram.eln.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

//import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

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
		String encoding = inputMessage.getHeaders().get("accept-encoding").get(0);
//		String contendencoding = inputMessage.getHeaders().get("content-encoding");

		String contendencoding ="";
		
		if(inputMessage.getHeaders().containsKey("content-encoding")) {
			contendencoding = inputMessage.getHeaders().get("content-encoding").get(0);
			System.out.println("content-encoding header : " + contendencoding);
		}else {
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
		if (contenttype.equalsIgnoreCase("application/json;charset=UTF-8")
				&& (encoding.equalsIgnoreCase("gzip, deflate") || encoding.equalsIgnoreCase("gzip, deflate, br"))
				&& contendencoding.equalsIgnoreCase("gzip")) {
			System.out.println("come decryption");
			return mapper.readValue(decrypt(inputMessage.getBody(), decryptionkey), clazz);
		} else {
			System.out.println("normal");
			return mapper.readValue(inputMessage.getBody(), clazz);
		}

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

		outputMessage.getBody().write(encrypt(mapper.writeValueAsBytes(o)));
	}

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
