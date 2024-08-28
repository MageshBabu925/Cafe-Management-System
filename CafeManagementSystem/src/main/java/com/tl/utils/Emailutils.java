package com.tl.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class Emailutils {
	
	@Autowired
	private JavaMailSender sender;
	
	public void sendSimpleMessage(String to,String subject,String text,List<String> list) {
		SimpleMailMessage message=new SimpleMailMessage();
		message.setFrom("mageshbabu92000@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		if(list!=null&&list.size()>0)
		message.setCc(getccArray(list));
		sender.send(message);
		
	}
	private String[] getccArray(List<String>ccArray) {
		String[] cc=new String[ccArray.size()];
		for(int i=0;i<ccArray.size();i++) {
			cc[i]=ccArray.get(i);
		}
		return cc;
	}
	
	public void forgotmail(String to,String subject,String password) throws MessagingException {
		MimeMessage msg=sender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(msg,true);
		helper.setFrom("mageshbabu92000@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		String htmlMsg = "<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b>" + to +" <br><b>Password: </b>" + password + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
		msg.setContent(htmlMsg,"text/html");
		sender.send(msg);
	}

}
