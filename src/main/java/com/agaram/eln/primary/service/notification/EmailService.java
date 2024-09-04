package com.agaram.eln.primary.service.notification;

import java.net.URL;
import java.util.Random;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

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
		String ch = String.format("%06d", number);
		number = Integer.parseInt(ch);

		// email.setOptcode(number);

		boolean html = true;
		helper.setText("<b>Dear Customer</b>,<br><i>use code <b>" + number
				+ "</b> to login our account Never share your OTP with anyone</i>", html);

		mailSender.send(message);

		EmailRepository.save(email);

		// model.addAttribute("message", "A plain text email has been sent");
		return email;

	}

	public Email sendusernamepassemail(Email email) throws MessagingException {
//		boolean valid = true;
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
			int nextRandomChar = lowerLimit + (int) (random.nextFloat() * (upperLimit - lowerLimit + 1));
			r.append((char) nextRandomChar);
		}
		String pass = r.toString();
		// return the resultant string
		System.out.println(pass);

//		String passwordtenant = AESEncryption.encrypt(pass);
		// email.setPassword(passwordtenant);

//		boolean html = true;
		// helper.setText("<b>Dear Customer</b>,<br><i>This is for your username and
		// password</i><br><b>UserName:\t\t"+email.getTenantid()+"</b><br><b>Password:\t\t"+pass+"</b>",
		// html);

		mailSender.send(message);

		// String username=email.getTenantid();
		// DataSourceConfigRepository.setverifiedemailandtenantpassword(valid,passwordtenant,username);

		// EmailRepository.setpasswordBytenantid(email.getPassword(),email.getTenantid());
		return email;
	}

	public Email sendEmail(Email email) throws MessagingException {
		String from = env.getProperty("spring.mail.username");
		String to = email.getMailto();
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setSubject(email.getSubject());
		helper.setFrom(from);
		helper.setTo(to);

		// Set Subject: header field
		message.setSubject(email.getSubject());

		// This mail has 2 part, the BODY and the embedded image
		MimeMultipart multipart = new MimeMultipart("related");

		// first part (the html)
		BodyPart messageBodyPart = new MimeBodyPart();

		String htmlText = email.getMailcontent();

		messageBodyPart.setContent(htmlText, "text/html");
		// add it
		multipart.addBodyPart(messageBodyPart);

		/*
		 * messageBodyPart = new MimeBodyPart(); String userDirectory = new
		 * File("").getAbsolutePath(); DataSource fds1 = new
		 * FileDataSource(userDirectory +
		 * "/src/main/resources/images/Logilab ELN_vertical.jpg");
		 * messageBodyPart.setDataHandler(new DataHandler(fds1));
		 * messageBodyPart.addHeader("Content-ID", "<image>");
		 * multipart.addBodyPart(messageBodyPart);
		 */

		// second part (the image)
		/*
		 * messageBodyPart = new MimeBodyPart(); DataSource fds = new FileDataSource(
		 * userDirectory +
		 * "/src/main/resources/images/AgaramTechnologies_vertical.jpg");
		 * 
		 * messageBodyPart.setDataHandler(new DataHandler(fds));
		 * messageBodyPart.setHeader("Content-ID", "<seconimage>");
		 * 
		 * // add image to the multipart multipart.addBodyPart(messageBodyPart);
		 */

		// put everything together
		message.setContent(multipart);
		// Send message
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

		return email;

	}

	public Email sendEmailelnLite(Email email) throws MessagingException {
		String from = env.getProperty("spring.mail.username");
		String cc = env.getProperty("spring.mail.mailcc");
        String to = email.getMailto();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true indicates multipart message

        helper.setSubject(email.getSubject());
        helper.setFrom(from);
        helper.setTo(to);
        helper.setCc(cc);
        // Create the multipart for mixed content (text and images)
        MimeMultipart multipart = new MimeMultipart("related");

        // Part 1: HTML content
        BodyPart htmlPart = new MimeBodyPart();
        String htmlText = email.getMailcontent(); // Ensure this HTML has correct cid references
        htmlPart.setContent(htmlText, "text/html; charset=utf-8"); // Set HTML content
        multipart.addBodyPart(htmlPart);

        // Attach inline images
        attachInlineImage(multipart, "images/eln_lite_logo.png", "<image1>");
        attachInlineImage(multipart, "images/ag_logo.png", "<image2>");
        attachInlineImage(multipart, "images/Agaram_Technologies_horizontal.png", "<image3>");

        // Set the multipart as the message content
        message.setContent(multipart);

        // Send the message
        mailSender.send(message);

        // Optionally, save the email to repository
        EmailRepository.save(email);

        return email;
		
	}
	private void attachInlineImage(MimeMultipart multipart, String imagePath, String contentId) throws MessagingException {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            MimeBodyPart imagePart = new MimeBodyPart();
            URL imageUrl = classLoader.getResource(imagePath);
            DataSource fds = new URLDataSource(imageUrl);
            imagePart.setDataHandler(new DataHandler(fds));
            imagePart.setHeader("Content-ID", contentId);
            imagePart.setDisposition(MimeBodyPart.INLINE); // Set Content-Disposition to inline
            multipart.addBodyPart(imagePart);
        } catch (Exception e) {
            throw new MessagingException("Failed to attach inline image: " + imagePath, e);
        }
    }
}
