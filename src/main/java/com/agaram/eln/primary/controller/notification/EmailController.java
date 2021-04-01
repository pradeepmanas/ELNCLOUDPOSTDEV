package com.agaram.eln.primary.controller.notification;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.agaram.eln.primary.model.notification.Email;
import com.agaram.eln.primary.service.notification.EmailService;

public class EmailController {
	
	@Autowired
    private EmailService emailService;
	
	@PostMapping("/send_text_email")
	public Email sendPlainTextEmail(@RequestBody Email email) throws MessagingException {
	
		return emailService.sendPlainTextEmail(email);
	}
	
	
	@PostMapping("/sendusernamepassemail")
	public Email sendusernamepassemail(@RequestBody Email email) throws MessagingException {
	
		return emailService.sendusernamepassemail(email);
	}
}
