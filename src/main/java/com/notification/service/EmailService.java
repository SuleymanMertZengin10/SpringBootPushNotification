package com.notification.service;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmailService {
	
	@Autowired
	JavaMailSender emailSender;
	

	public void sendEmail(String to, String subject, String text) throws MessagingException {
		 MimeMessage message = emailSender.createMimeMessage();
		 MimeMessageHelper helper = new MimeMessageHelper(message, true);
		 helper.setTo(to);
		 helper.setSubject(subject);
		 helper.setText(text);
		 
		 FileSystemResource file= new FileSystemResource(new File("a/deneme.txt"));
		 FileSystemResource file2= new FileSystemResource(new File("a/deneme11.pdf"));
		 FileSystemResource file3= new FileSystemResource(new File("a/deneme2.xls"));
		 Resource res = new FileSystemResource(new File("a/a.jpg"));
	
		 helper.addInline("myİmage", res);
		 helper.addInline("exel", file2);
		 helper.addInline("pdf", file3);
		 helper.addAttachment("deneme.txt",file);
		 
		 emailSender.send(message);
		 
	}
}
