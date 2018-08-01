package com.notification.controller;

import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.notification.service.EmailService;

@RestController
public class EmailController {
	
	@Autowired
	EmailService emailService;
	
	@PostMapping("/sendEmail")
	public String sendEmail(@RequestBody Map<String,String>mail) throws MessagingException {
		String to=mail.get("to");
	    String subject=mail.get("subject");
	    String text=mail.get("text");
		try {
			emailService.sendEmail(to,subject,text);
			
		} catch (MessagingException e) {
			return e.getMessage();
		}
	
		return "mesaj basarıyla yollandi";
	
	}

}
