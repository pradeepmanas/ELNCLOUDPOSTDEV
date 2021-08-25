package com.agaram.eln.primary.service.notification;

import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.agaram.eln.config.AESEncryption;
import com.agaram.eln.primary.model.notification.Email;
import com.agaram.eln.primary.repository.notification.EmailRepository;

@Service
@EnableJpaRepositories(basePackageClasses = EmailRepository.class)
public class EmailService {

		@Autowired
	    private JavaMailSender mailSender;

		@Autowired
		private EmailRepository EmailRepository;
		
		@Autowired
	    private org.springframework.core.env.Environment env;
		

		public Email sendPlainTextEmail(Email email) throws MessagingException {
		
			String from = env.getProperty("spring.mail.username");
			String to = email.getMailto();
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			 
			helper.setSubject("This is an OTP verification email");
			helper.setFrom(from);
			helper.setTo(to);
			
			 Random rnd = new Random();
			 int number = rnd.nextInt(999999);
			 String ch=String.format("%06d", number);
			 number= Integer.parseInt(ch); 
			 
			 //email.setOptcode(number);
			 
			boolean html = true;
			helper.setText("<b>Dear Customer</b>,<br><i>use code <b>"+number+"</b> to login our account Never share your OTP with anyone</i>", html);
			
			mailSender.send(message);
			
			EmailRepository.save(email);
			
	//		model.addAttribute("message", "A plain text email has been sent");
			return email;
			
		}
		
		public Email sendusernamepassemail(Email email) throws MessagingException {
			boolean valid =true;
			String from = env.getProperty("spring.mail.username");
			String to = email.getMailto();
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			 
			helper.setSubject("Username and Password");
			helper.setFrom(from);
			helper.setTo(to);
			
			 // lower limit for LowerCase Letters 
	        int lowerLimit = 97; 
	  
	        // lower limit for LowerCase Letters 
	        int upperLimit = 122; 
	  
	        Random random = new Random(); 
	        int n = 6; 
	        // Create a StringBuffer to store the result 
	        StringBuffer r = new StringBuffer(n); 
	  
	        for (int i = 0; i < n; i++) { 
	            int nextRandomChar = lowerLimit 
	                                 + (int)(random.nextFloat() 
	                                         * (upperLimit - lowerLimit + 1));  
	            r.append((char)nextRandomChar); 
	        } 
	        String pass=r.toString();
	        // return the resultant string 
	       System.out.println(pass);
	       
	       String passwordtenant=AESEncryption.encrypt(pass);
	      // email.setPassword(passwordtenant);
			 
			boolean html = true;
		//	helper.setText("<b>Dear Customer</b>,<br><i>This is for your username and password</i><br><b>UserName:\t\t"+email.getTenantid()+"</b><br><b>Password:\t\t"+pass+"</b>", html);
			
			
	
			mailSender.send(message);
			
			//String username=email.getTenantid();
			//DataSourceConfigRepository.setverifiedemailandtenantpassword(valid,passwordtenant,username);
	
			//EmailRepository.setpasswordBytenantid(email.getPassword(),email.getTenantid());
			return email;
		}
		
		public Email sendEmail(Email email) throws MessagingException
		{
			String from = env.getProperty("spring.mail.username");
			String to = email.getMailto();
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			 
			helper.setSubject(email.getSubject());
			helper.setFrom(from);
			helper.setTo(to);
			
			boolean html = true;
			helper.setText(email.getMailcontent(), html);
				
			
			mailSender.send(message);
			
			EmailRepository.save(email);
			
			return email;
		}
		
		
		public Email sendmailOPT(Email email) throws MessagingException {
			
			String from = env.getProperty("spring.mail.username");
			String to = email.getMailto();
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			 
			helper.setSubject(email.getSubject());
			helper.setFrom(from);
			helper.setTo(to);
			
			 
			boolean html = true;

			helper.setText(email.getMailcontent(), html);
			
			mailSender.send(message);
			
			EmailRepository.save(email);
			
	//		model.addAttribute("message", "A plain text email has been sent");
			return email;
			
		}

}
