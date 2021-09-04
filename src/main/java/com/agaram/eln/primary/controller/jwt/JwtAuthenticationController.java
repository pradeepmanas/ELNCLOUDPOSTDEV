package com.agaram.eln.primary.controller.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.config.JwtTokenUtil;
import com.agaram.eln.primary.model.jwt.JwtRequest;
import com.agaram.eln.primary.model.jwt.JwtResponse;
import com.agaram.eln.primary.model.jwt.UserDTO;
import com.agaram.eln.primary.service.JWTservice.JwtUserDetailsService;


import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

//		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}
	
//	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
//	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
//
//		try {
//			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
//		}
//		catch (BadCredentialsException e) {
//			System.out.println("Bad Credential");
//		}
//		
//		final UserDetails userDetails = userDetailsService
//				.loadUserByUsername(authenticationRequest.getUsername());
//
//		final String token = jwtTokenUtil.generateToken(userDetails);
//
//		return ResponseEntity.ok(new JwtResponse(token));
//	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception {
		return ResponseEntity.ok(userDetailsService.save(user));
	}

	
	@SuppressWarnings("unused")
	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	
	/**
	 * Used for refrence mail send
	 * 
	 * @param args
	 */
	/*
	 * @RequestMapping(value = "/registerMail", method = RequestMethod.POST) public
	 * static void main(String[] args) { // Recipient's email ID needs to be
	 * mentioned. String to = "kumaresan.b@agaramtech.com";
	 * 
	 * // Sender's email ID needs to be mentioned String from =
	 * "kumaresan.b@agaramtech.com"; final String username =
	 * "kumaresan.b@agaramtech.com";//change accordingly final String password =
	 * "kumaresan123";//change accordingly
	 * 
	 * // Assuming you are sending email through relay.jangosmtp.net String host =
	 * "smtp.gmail.com";
	 * 
	 * Properties props = new Properties(); props.put("mail.smtp.auth", "true");
	 * props.put("mail.smtp.starttls.enable", "true"); props.put("mail.smtp.host",
	 * host); props.put("mail.smtp.port", "587");
	 * 
	 * Session session = Session.getInstance(props, new javax.mail.Authenticator() {
	 * protected PasswordAuthentication getPasswordAuthentication() { return new
	 * PasswordAuthentication(username, password); } });
	 * 
	 * try {
	 * 
	 * // Create a default MimeMessage object. Message message = new
	 * MimeMessage(session);
	 * 
	 * // Set From: header field of the header. message.setFrom(new
	 * InternetAddress(from));
	 * 
	 * // Set To: header field of the header. //
	 * message.setRecipients(Message.RecipientType.TO, //
	 * InternetAddress.parse(to));
	 * 
	 * // Set Subject: header field message.setSubject("Testing Subject");
	 * 
	 * // This mail has 2 part, the BODY and the embedded image MimeMultipart
	 * multipart = new MimeMultipart("related");
	 * 
	 * // first part (the html) BodyPart messageBodyPart = new MimeBodyPart();
	 * String htmlText =
	 * "<H1>Hello</H1><img src=\"cid:image\"  style ='margin-left: 200px; width: 16%;border: 3px;'>"
	 * + ""; messageBodyPart.setContent(htmlText, "text/html"); // add it
	 * multipart.addBodyPart(messageBodyPart);
	 * 
	 * // second part (the image) messageBodyPart = new MimeBodyPart(); DataSource
	 * fds = new FileDataSource(
	 * "D:/WORKING FOLDER/ELN/branches/ELN 6.6/ELNCLOUDPOST/src/main/resources/images/AgaramTechnologies_vertical.jpg"
	 * );
	 * 
	 * messageBodyPart.setDataHandler(new DataHandler(fds));
	 * messageBodyPart.setHeader("Content-ID", "<image>");
	 * 
	 * // add image to the multipart multipart.addBodyPart(messageBodyPart);
	 * 
	 * // put everything together message.setContent(multipart); // Send message
	 * Transport.send(message);
	 * 
	 * System.out.println("Sent message successfully....");
	 * 
	 * } catch (MessagingException e) { throw new RuntimeException(e); } }
	 */
}