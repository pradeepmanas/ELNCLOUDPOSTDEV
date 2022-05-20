package com.agaram.eln.primary.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.agaram.eln.primary.response.ErrorMessage;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler 
{
	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request)
	{
		System.out.println("------Inside handleAnyException--------");

//		ex.printStackTrace();

		ErrorMessage errorMessage = new ErrorMessage(301, ex.toString(), new Date());

		return new ResponseEntity<>(errorMessage, new HttpHeaders(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
//	@ExceptionHandler(value = { Exception.class })
//		    protected ResponseEntity<Object> handleConflict(
//		      RuntimeException ex, WebRequest request) {
//		        String bodyOfResponse = "This should be application specific";
//		        return handleExceptionInternal(ex, bodyOfResponse, 
//		          new HttpHeaders(), HttpStatus.CONFLICT, request);
//		    }

}
